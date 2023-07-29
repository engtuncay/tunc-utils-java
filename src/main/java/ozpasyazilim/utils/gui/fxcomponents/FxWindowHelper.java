package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class FxWindowHelper {

	/**
	 * MaxHeight = Screen Height - 70d
	 *
	 * @param rootPane
	 */
	public static void setMaxHeightAndWidthForWindows(Pane rootPane) {
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		//System.out.println(screenBounds);
		rootPane.setMaxHeight(screenBounds.getHeight()-70d); // 70d başlık çubuğu için eksi yapıldı.
		rootPane.setMaxWidth(screenBounds.getWidth());
	}


}
