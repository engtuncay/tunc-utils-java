package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ozpasyazilim.utils.mvc.IFxViewContainer;
import ozpasyazilim.utils.returntypes.Fdr;

public class FxStackPane extends StackPane implements IFxViewContainer {

	Stage fxStage;
	FxScene fxScene;
	FxMigPane fxMigPane;
	Fdr fdr;

	public FxStackPane() {
	}

	public FxStackPane(Node... children) {
		super(children);
	}

	@Override
	public Parent getView() {
		return this;
	}

	public Stage getFxStage() {
		return fxStage;
	}

	public void setFxStage(Stage fxStage) {
		this.fxStage = fxStage;
	}

	@Override
	public Pane getRootPane() {return this;}

	public FxScene getFxScene() {
		return fxScene;
	}

	public void setFxScene(FxScene fxScene) {
		this.fxScene = fxScene;
	}

	@Override
	public FxMigPane getRootMigPane() {
		return fxMigPane;
	}

	@Override
	public FxStackPane getRootStackPane() {
		return this;
	}


	public void setupStage() {
		FxStage fxStage = new FxStage();
		setFxStage(fxStage);
	}

	public Fdr getFdrResult() {
		return fdr;
	}

	public void setFdrResult(Fdr fdr) {
		this.fdr = fdr;
	}
}
