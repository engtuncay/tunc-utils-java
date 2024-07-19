package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.ObservableList;
import ozpasyazilim.utils.gui.components.ComboItemObj;

public class FxComboBoxObj extends FxComboBox<ComboItemObj> {

	public FxComboBoxObj() {
		super();
	}

	public void activateSelectedItem() {

		if (getObjValue()==null) { // FiString.isEmpty(getTxValue())
			getSelectionModel().select(0);
		} else {
			setSelectedItemByObjValueFi();
		}

	}

	public void setSelectedItemByObjValueFi() {

		if ( getObjValue() == null || isEmptyComboBox()) return;

		ObservableList<ComboItemObj> items = getItems();

		int index = 0;
		for (ComboItemObj item : items) {
			if(item.getValue()==null) continue;
			if (item.getValue().equals(getObjValue())) {
				getSelectionModel().select(index);
			}
			index++;
		}
	}

	public void addFiItem(ComboItemObj comboItem) {
		getItems().add(comboItem);
	}

	public void addComboItem(Object objValue, String txLabel) {
		addFiItem(new ComboItemObj(txLabel, objValue));
	}

//	public void addFiList(List<EntClazz> list, Function<EntClazz, String> fnLabel) {
//
//		for (EntClazz prmEnt : list) {
//			getItems().add(new ComboItemGen<EntClazz>(fnLabel.apply(prmEnt),prmEnt));
//		}
//
//	}

//	public void addFirstItemAsNull() {
//		addFiItem(new ComboItemGen<EntClazz>("Seçiniz", null));
//	}

}
