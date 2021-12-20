package ozpasyazilim.utils.mvc;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.gui.fxcomponents.FxStackPane;

public interface IFxViewContainer {

	Parent getView();

	// Container ya stackpane ya da migpain kullanılır
	FxMigPane getRootMigPane();
	FxStackPane getRootStackPane();
	public Pane getRootPane();

	public Stage getFxStage();
	public void setFxStage(Stage fxStage);

	// Scene gerekirse eklenir
	// public FxScene getFxScene();
	// public void setFxScene(FxScene fxScene);

}
