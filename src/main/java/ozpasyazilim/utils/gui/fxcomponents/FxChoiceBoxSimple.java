package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.ObservableList;
import ozpasyazilim.utils.gui.components.ComboItemText;

public class FxChoiceBoxSimple extends FxChoiceBox<ComboItemText> {

	public FxChoiceBoxSimple() {
	}

	public FxChoiceBoxSimple(ObservableList<ComboItemText> items) {
		super(items);
	}


	public ComboItemText getFiSelectedItem() {
		return selectionModelProperty().get().getSelectedItem();
	}


}
