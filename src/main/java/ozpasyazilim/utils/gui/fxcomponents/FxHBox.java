package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class FxHBox extends HBox {

	public FxHBox() {
		super();
	}

	public FxHBox(double spacing) {
		super(spacing);
	}

	public FxHBox(Node... children) {
		super(children);
	}

	public FxHBox(double spacing, Node... children) {
		super(spacing, children);
	}
}
