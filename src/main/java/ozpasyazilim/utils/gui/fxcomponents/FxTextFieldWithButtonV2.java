package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class FxTextFieldWithButtonV2<EntClazz> extends HBox {

	private FxTextField<EntClazz> fxTextField;
	private FxButton fxButton;

	public FxTextFieldWithButtonV2() {
		fxButton = new FxButton("*");
		fxTextField = new FxTextField<>();
		fxTextField.setDisable(true);
		getChildren().add(fxTextField);
		getChildren().add(fxButton);
		activateF10Action();
	}

	private void activateF10Action() {

		addEventHandler(KeyEvent.KEY_PRESSED, event -> {

			if (event.getCode() == KeyCode.F10) {
				fxButton.fire();
			}

		});

//		setOnKeyReleased(event -> {
//			if(event.getCode() == KeyCode.F10 ){
//				fxButton.fire();
//			}
//		});

	}

	public FxTextField<EntClazz> getFxTextField() {
		return fxTextField;
	}

	public FxButton getFxButton() {
		return fxButton;
	}

	public String getFxTxValue() {
		if(getFxTextField().txValueProperty()==null) return null;
		return getFxTextField().getTxValue();
	}

	public StringProperty fxTxValueProperty() {
		return getFxTextField().txValueProperty();
	}

	public void setFxTxValue(String txValue) {
		getFxTextField().txValueProperty().set(txValue);
	}

	public void bindUniTxValueToText() {
		getFxTextField().txValueProperty().addListener((observable, oldValue, newValue) -> {
			getFxTextField().setText(newValue);
		});
	}

	public EntClazz getPropObjValue() {
		return getFxTextField().getObjValue();
	}

	public ObjectProperty<EntClazz> propObjValueProperty() {
		return getFxTextField().objValueProperty();  //propObjValue;
	}

	public void setPropObjValue(EntClazz prmPropObjValue) {
		getFxTextField().setObjValue(prmPropObjValue);
		//this.propObjValue.set(prmPropObjValue);
	}
}
