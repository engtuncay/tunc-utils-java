package ozpasyazilim.utils.gui.fxcomponents;

import javafx.stage.WindowEvent;

public class FxStageHelper {

	public static void stopClosingWindow(WindowEvent event) {
		event.consume();
	}
}
