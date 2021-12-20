package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

public class FxScene extends Scene {

	FxStackPane fxStackPane;

	public FxScene(Parent root) {
		super(root);
	}

	public FxScene(Parent root, double width, double height) {
		super(root, width, height);
	}

	public FxScene(Parent root, Paint fill) {
		super(root, fill);
	}

	public FxScene(Parent root, double width, double height, Paint fill) {
		super(root, width, height, fill);
	}

	public FxScene(Parent root, double width, double height, boolean depthBuffer) {
		super(root, width, height, depthBuffer);
	}

	public FxScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
		super(root, width, height, depthBuffer, antiAliasing);
	}

	public FxStackPane getFxStackPane() {return fxStackPane;}

	public void setFxStackPane(FxStackPane fxStackPane) {this.fxStackPane = fxStackPane;}

}
