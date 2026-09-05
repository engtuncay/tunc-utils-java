package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Stage Wrapper Class
 */
public class FxStagew {

	Stage stage;

	public FxStagew(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Plat : Run on Platform Thread
	 *
	 * @param titleAppend
	 */
	public void appendTitlePlat(String titleAppend) {
		Platform.runLater(() -> {
			getStage().setTitle(getStage().getTitle() + " - " + titleAppend);
		});
	}
}
