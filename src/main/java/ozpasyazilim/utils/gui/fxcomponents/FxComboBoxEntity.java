package ozpasyazilim.utils.gui.fxcomponents;

import com.google.common.base.Function;
import javafx.collections.ObservableList;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.gui.components.ComboItemGen;

import java.util.List;

public class FxComboBoxEntity<EntClazz> extends FxComboBox<ComboItemGen<EntClazz>> {

	public FxComboBoxEntity() {
		super();
	}

//	public void addFiItems(List<String> list) {
//		for (String s : list) {
//			getItems().add(new ComboItem(s, s));
//		}
//	}

	public void activateSelectedItem() {

		if (FiString.isEmpty(getTxValue())) {
			getSelectionModel().select(0);
		} else {
			setFiSelectedItemByTxValue();
		}

	}

	public void setFiSelectedItemByTxValue() {

		if (FiString.isEmpty(getTxValue()) || isEmptyComboBox()) return;

		ObservableList<ComboItemGen<EntClazz>> items = getItems();

		int index = 0;
		for (ComboItemGen item : items) {
			if(item.getValue()==null) continue;
			if (item.getValue().equals(getTxValue())) {
				getSelectionModel().select(index);
			}
			index++;
		}
	}

	public void addFiItem(ComboItemGen<EntClazz> comboItem) {
		getItems().add(comboItem);
	}

	public void addFiList(List<EntClazz> list, Function<EntClazz, String> fnLabel) {

		for (EntClazz prmEnt : list) {
			getItems().add(new ComboItemGen<EntClazz>(fnLabel.apply(prmEnt),prmEnt));
		}

	}

	public void addFirstItemAsNull() {
		addFiItem(new ComboItemGen<EntClazz>("Seçiniz", null));
	}

}
