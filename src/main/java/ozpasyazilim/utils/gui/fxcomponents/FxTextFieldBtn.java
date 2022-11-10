package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.javafx.FiNodeFx;
import ozpasyazilim.utils.table.OzColType;

import java.util.function.Consumer;

public class FxTextFieldBtn<EntClazz> extends FxMigPane implements IfxNode {

	private FxTextField<EntClazz> fxTextField;
	private FxButton btnSearch;
	protected FxLabel lblAciklama;
	private Consumer<String> fnTxfDataChanged;
	Boolean boFirstLoaded;

	public FxTextFieldBtn() {
		super(FxMigPane.lcStandardInset0Gap00);
		btnSearch = new FxButton("*");
		getFxTextField();
		btnSearch.setMaxHeight(22d);
		btnSearch.setPrefHeight(22d);
		add(fxTextField, "growx,pushx");
		add(btnSearch, "");
		activateF10Action();
	}

	public void activateF10Action() {
		addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.F10) {
				btnSearch.fire();
			}
		});
	}

	public FxTextField<EntClazz> getFxTextField() {
		if (fxTextField == null) {
			fxTextField = new FxTextField<>();
		}
		return fxTextField;
	}

	public FxButton getBtnSearch() {
		return btnSearch;
	}

	public String getTxValue() {
		return getFxTextField().getTxValue();
	}

	public StringProperty txValueProperty() {
		return getFxTextField().txValueProperty();
	}

	public void setTxValue(String txPropTxfValue) {
		getFxTextField().setTxValue(txPropTxfValue);
	}

	public void setTxValueAndText(String txPropTxfValue) {
		getFxTextField().setTxValue(txPropTxfValue);
		getFxTextField().setText(txPropTxfValue);
	}

	// FIXME bind ile yapalım
	public void bindTextfieldToSelection() {
		txValueProperty().addListener((observable, oldValue, newValue) -> {
			getFxTextField().setText(newValue);
		});
	}

	public void bindBiDirectionalForTxValueAndTextValue() {
		txValueProperty().bindBidirectional(getFxTextField().textProperty());
	}

	public EntClazz getEntValue() {
		return getFxTextField().getObjValue();
	}

	public ObjectProperty<EntClazz> entValueProperty() {
		return getFxTextField().objValueProperty();
	}

	public void setEntValue(EntClazz prmPropObjValue) {
		getFxTextField().setObjValue(prmPropObjValue);
	}

	public void activateLabelTrigger(FxLabel fxLabel) {
		getFxTextField().txAltValueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				fxLabel.setText(newValue);
				if (!FiBoolean.isTrue(getFxTextField().getBoInvalidData())) {
//					Loghelper.get(getClass()).debug("tx value atandı.label");
					fxLabel.setTxValue(newValue);
				} else {
					fxLabel.setTxValue("");
				}
			}
		});
	}

	public FxLabel getLblAciklama() {
		return lblAciklama;
	}

	public void setLblAciklama(FxLabel lblAciklama) {
		this.lblAciklama = lblAciklama;
	}

	public void addLabelBelowRow() {
		if (getLblAciklama() == null) {
			setLblAciklama(new FxLabel(""));
			wrapFi();
			add(getLblAciklama(), "growx,pushx");
		}
	}

	public void activateChangeEvent(Long lnDurationChange) {

		if (getLblAciklama() != null) {

			if(lnDurationChange==null) lnDurationChange = getLnDurationChangeDefaultVal();

			FiNodeFx.registerTextPropertyWithDurationForFxTextfield((s) -> {
				if (getFnTxfDataChanged() != null) {
					getFnTxfDataChanged().accept(s);
				}
			}, lnDurationChange, getFxTextField());

		}

	}

	public Consumer<String> getFnTxfDataChanged() {
		return fnTxfDataChanged;
	}

	@Override
	public void setCompValue(Object objValue) {
		if(objValue==null) return;
		getFxTextField().setText(objValue.toString());
		setTxValue(objValue.toString());
	}

	@Override
	public Object getCompValueByColType(OzColType ozColType) {
		if(FiBoolean.isTrue(getFxTextField().getBoInvalidData())) return null;
		if(ozColType==null){
			return getTxValue();
		}else{
			return FxEditorFactory.convertStringValueToObjectByOzColType(ozColType, getTxValue());
		}
	}

	@Override
	public Boolean getBoInvalidData() {
		return getFxTextField().getBoInvalidData();
	}

	@Override
	public Object getCompValueByColType() {
		return null;
	}

	@Override
	public OzColType getCompValueType() {
		return null;
	}

	@Override
	public void setCompValueType(OzColType ozColType) {

	}

	@Override
	public Object getCompValue() {
		return null;
	}

	public void setText(String textValue) {
		getFxTextField().setText(textValue);
	}

	public Long getLnDurationChangeDefaultVal() {
		return 500l;
	}



	public void setLblTextWitCheck(String txValue) {
		if(getLblAciklama()!=null){
			getLblAciklama().setText(txValue);
		}
	}

	public void setFnTxfDataChanged(Consumer<String> fnTxfDataChanged) {
		this.fnTxfDataChanged = fnTxfDataChanged;
	}

	public Boolean getBoFirstLoaded() {
		if (boFirstLoaded == null) {
			return false;
		}
		return boFirstLoaded;
	}

	public void setBoFirstLoaded(Boolean boFirstLoaded) {
		this.boFirstLoaded = boFirstLoaded;
	}



}
