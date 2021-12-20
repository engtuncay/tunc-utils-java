package ozpasyazilim.utils.mvc;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.gui.fxcomponents.FxStackPane;
import ozpasyazilim.utils.mvcanno.FiExpiremental;

public interface IFxSimpleView {

	Pane getRootPane();

	// Layout and Comp initilization vs...
	void initGui();

}
