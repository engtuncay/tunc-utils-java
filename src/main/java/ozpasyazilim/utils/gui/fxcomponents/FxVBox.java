package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class FxVBox extends VBox {

	public FxVBox() {
		super();
	}

	public FxVBox(double spacing) {
		super(spacing);
	}

	public FxVBox(Node... children) {
		super(children);
	}

	public FxVBox(double spacing, Node... children) {
		super(spacing, children);
	}

}
