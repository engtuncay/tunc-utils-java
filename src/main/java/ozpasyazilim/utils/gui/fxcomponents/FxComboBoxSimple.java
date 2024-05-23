package ozpasyazilim.utils.gui.fxcomponents;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.gui.components.ComboItemText;
import ozpasyazilim.utils.log.Loghelper;

import java.util.List;

public class FxComboBoxSimple extends FxComboBox<ComboItemText> {

	public FxComboBoxSimple() {
		super();
	}

	public FxComboBoxSimple(Boolean isEnabledListenValue) {
		super();
		enableListenValue(isEnabledListenValue);
	}

	public void enableListenValue(Boolean isEnabledListenValue) {
		if(FiBool.isTrue(isEnabledListenValue)){
			getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setTxValue(newValue.getValue()));
		}
	}


	public FxComboBoxSimple(String prompText) {
		super();
		setPromptText(prompText);
	}

	public FxComboBoxSimple(ObservableList<ComboItemText> items) {
		super(items);
	}

	public void addFiItems(List<String> list) {
		for (String s : list) {
			getItems().add(new ComboItemText(s, s));
		}
	}

	public void activateSelectedItem() {

		if (FiString.isEmpty(getTxValue())) {
			getSelectionModel().select(0);
		} else {
			setSelectedItemByTxValueFi();
		}

	}

	public void setSelectedItemByTxValueFi() {

		if (FiString.isEmpty(getTxValue()) || isEmptyComboBox()) return;

		ObservableList<ComboItemText> items = getItems();

		for (int index = 0; index < items.size(); index++) {

			ComboItemText item = items.get(index);
			if (item.getValue() == null) continue;

			if (item.getValue().equals(getTxValue())) {
				setSelectedItemFiAsync(index);
			}

		}
	}

	public void addComboItem(ComboItemText item) {
        super.addComboItem(item);
    }

	public void addSecureComboItem(ComboItemText comboItem, Predicate<String> predSecureCheck) {
		if(predSecureCheck.apply(comboItem.getTxCode())){
			getItems().add(comboItem);
		}
	}

	public <PrmEnt> void addFiList(List<PrmEnt> list, Function<PrmEnt, Object> fnValue, Function<PrmEnt, Object> fnLabel) {

		for (PrmEnt prmEnt : list) {
			getItems().add(ComboItemText.build(fnLabel.apply(prmEnt), fnValue.apply(prmEnt)));
		}

	}

	public void addFirstItemAsNull() {
		addComboItem(new ComboItemText("Seçiniz", null));
	}

	public void addComboItem(String value, String label) {
		addComboItem(new ComboItemText(label, value));
	}

	public void addComboItemObjFi(Object value, String label) {
		addComboItem(value.toString(),label);
	}

	@FiDraft
	public void addEnterListenerFi() {
		// selection alternatif yöntemi bulunamadı
		setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				ComboItemText selectedItem = getSelectionModel().getSelectedItem();
				Loghelper.get(getClass()).debug("Entered"+selectedItem.getValue());
			}
		});

		addEventHandler(KeyEvent.KEY_PRESSED,event -> {
			ComboItemText selectedItem = getSelectionModel().getSelectedItem();
			Loghelper.get(getClass()).debug("keypressed"+selectedItem.getValue());
		});

		setOnMouseEntered(event -> {
			ComboItemText selectedItem = getSelectionModel().getSelectedItem();
			Loghelper.get(getClass()).debug("Mouse entered:"+selectedItem.getValue());
		});
	}


	public ComboItemText getSelectedItemFiNtn() {
		if (getSelectedItemFi()==null) {
			return new ComboItemText("","");
		}

		return super.getSelectedItemFi();
	}
}
