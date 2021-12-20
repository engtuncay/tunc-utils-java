package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.FxScene;

/**
 *
 */
public interface IModuleFormView {

	public void initGui();

	public Stage getFxStage();
	public void setFxStage(Stage fxStage);

	public FxScene getFxScene();
	public void setFxScene(FxScene scene);

}
