package ozpasyazilim.utils.gui.fxcomponents;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class FxListView<T> extends ListView<T> {

	public FxListView() {
	}

	public FxListView(ObservableList items) {
		super(items);
	}




}
