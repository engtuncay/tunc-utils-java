package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.gui.components.ComboItem;

public class FxChoiceBoxSimple extends FxChoiceBox<ComboItem> {

	public FxChoiceBoxSimple() {
	}

	public FxChoiceBoxSimple(ObservableList<ComboItem> items) {
		super(items);
	}


	public ComboItem getFiSelectedItem() {
		return selectionModelProperty().get().getSelectedItem();
	}


}
