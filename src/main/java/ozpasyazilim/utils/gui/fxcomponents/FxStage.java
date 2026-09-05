package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FxStage extends Stage {

	public FxStage() {
		super();
	}

	public FxStage(StageStyle style) {
		super(style);
	}

  public void setHeightScreen1Over2() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// 2. Yüksekliği ekran yüksekliğinin yarısına ayarla
		//primaryScreenBounds.getHeight() / 2;
		setHeight(primaryScreenBounds.getHeight() / 2);
  }
}
