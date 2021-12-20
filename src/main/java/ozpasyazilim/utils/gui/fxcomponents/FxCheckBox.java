package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import ozpasyazilim.utils.gui.components.ComboItem;

import java.util.function.Consumer;

public class FxCheckBox extends CheckBox implements IFxEditorNode {

	// Generic alanlar
	String fieldName;
	String fxId;

	private Boolean boOldValue;

	ComboItem comboitem;

	public FxCheckBox(ComboItem comboitem) {
		this.comboitem= comboitem;
		setText(comboitem.getLabel());
	}

	public FxCheckBox(String text) {
		super(text);
	}

	public FxCheckBox() {

	}

	public FxCheckBox(String text, String txTooltip) {
		super(text);
		setSimpleTooltip(txTooltip);
	}

	public void setSimpleTooltip(String txTooltip) {
		setTooltip(new Tooltip(txTooltip));
	}

	public ComboItem getComboitem() {
		return comboitem;
	}

	public void setComboitem(ComboItem comboitem) {
		this.comboitem = comboitem;
		setText(comboitem.getLabel());
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getFxId() {
		return fxId;
	}

	@Override
	public void setFxId(String fxId) {
		this.fxId = fxId;
	}

	public Boolean getBoOldValue() {
		return boOldValue;
	}

	public void setBoOldValue(Boolean boOldValue) {
		this.boOldValue = boOldValue;
	}

	public void setOnActions(Runnable selectedAction, Runnable unSelectedAction) {
		selectedProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue==true){
		        selectedAction.run();
		    }else{
		    	unSelectedAction.run();
		    }
		});
	}

	public void setOnChangeAction(Consumer<Boolean> changeAction) {
		selectedProperty().addListener((observable, oldValue, newValue) -> {
			changeAction.accept(newValue);
		});
	}
}
