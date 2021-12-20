package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FxTextFieldWithButtonLabelCustom<EntClazz> extends FxMigPane {

	private final FxLabel lblAciklama;
	private FxTextField<EntClazz> fxTextField;
	private FxButton fxButton;

	public FxTextFieldWithButtonLabelCustom() {
		super(FxMigPane.lcStandardInset0Gap00);
		fxButton = new FxButton("*");
		getFxTextField();
		fxButton.setMaxHeight(22d);
		fxButton.setPrefHeight(22d);
		add(fxTextField, "growx,pushx");

		lblAciklama = new FxLabel("");
		lblAciklama.setPrefWidth(50);
		add(lblAciklama, "");
		add(fxButton, "");
		activateF10Action();
	}

	private void activateF10Action() {

		addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			if (event.getCode() == KeyCode.F10) {
				fxButton.fire();
			}

		});

	}

	public FxTextField<EntClazz> getFxTextField() {
		if (fxTextField == null) {
			fxTextField = new FxTextField<>();
		}
		return fxTextField;
	}

	public FxButton getFxButton() {
		return fxButton;
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

	public FxLabel getLblAciklama() {return lblAciklama;}
}
