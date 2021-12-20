package ozpasyazilim.utils.javafx;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.FxScene;

public class JavaFXInitializer extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        // JavaFX should be initialized
        //someGlobalVar.setInitialized(true);
        //stage.setScene(new FxScene(new StackPane()));
        //stage.show();
        stage = stage;
    }

    public static void startApp(){
        launch(null);
    }

}