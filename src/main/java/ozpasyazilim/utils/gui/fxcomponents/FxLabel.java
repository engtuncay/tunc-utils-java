package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;

public class FxLabel extends Label implements IFxComp {

	String fxId;
	StringProperty txValue = new SimpleStringProperty("");
	Boolean boInvalidData;

	public FxLabel(String text) {
		super(text);
	}

	public static FxLabel buildLabel(String message){
		return new FxLabel(message);
	}

	public FxLabel buildsetAlignment(Pos value){
		this.setAlignment(value);
		return this;
	}

	public void fiSetImage(File file,Double height){

		if(file.exists()){
			Image image = new Image(file.toURI().toString(),0,height,true,true) ;
			ImageView imageView = new ImageView(image);
			//imageView.setPreserveRatio(true);
			//imageView.setFitHeight(50);
			setGraphic(imageView);
		}

	}

	public void activateBindTxValueToText() {
		textProperty().bindBidirectional(txValueProperty());
	}

	public FxLabel buildsetPrefHeight(double prefHeight) {
		setPrefHeight(prefHeight);
		return this;
	}

	public void setFxIcon(Icons525 icon) {
		new FxAwesomeIcon().loadIcon525ToFxLabel(this, icon, 12, "white", "black");
	}

	@Override
	public String getFxId() {
		return fxId;
	}

	@Override
	public void setFxId(String fxId) {
		this.fxId = fxId;
	}

	public void setFxTextColor(Color color) {
		setTextFill(color);
	}

	public String getTxValue() {return txValue.get();}

	public StringProperty txValueProperty() {return txValue;}

	public void setTxValue(String txValue) {this.txValue.set(txValue);}

	public Boolean getBoInvalidData() {return boInvalidData;}

	public void setBoInvalidData(Boolean boInvalidData) {this.boInvalidData = boInvalidData;}

}

