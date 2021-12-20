package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class FxMenuButton extends MenuButton {

	public FxMenuButton() {
		super();
	}

	public FxMenuButton(String text) {
		super(text);
	}

	public FxMenuButton(String text, Node graphic) {
		super(text, graphic);
	}

	public FxMenuButton(String text, Node graphic, MenuItem... items) {
		super(text, graphic, items);
	}

}
