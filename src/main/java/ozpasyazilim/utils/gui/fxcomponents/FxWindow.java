package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.mvc.IFiModCont;

public class FxWindow {

    public static void nodeWindow(Node nodeRelative, IFiModCont ifxSimpleModCont) {
        nodeWindow(nodeRelative, ifxSimpleModCont, null, null, null);
    }

    public static void nodeWindow(IFiModCont iFiModCont, DialogConf dialogConf) {

        if (dialogConf == null) dialogConf = new DialogConf();

        Pane rootPane = iFiModCont.getModView().getRootPane();

        FxWindowHelper.setMaxHeightAndWidthForWindows(rootPane);

        if (!FiString.isEmpty(dialogConf.getCssFileName())) {
            rootPane.getStylesheets().add(dialogConf.getCssFileName());
        }

        Stage stage = iFiModCont.getFxStageInit();

        if (stage == null) {
            stage = new FxStage();
            iFiModCont.setFxStage(stage);
        }

        if (!FiString.isEmpty(dialogConf.getTitle())) {
            stage.setTitle(dialogConf.getTitle());
        }

        FxScene scene = null;

        if (dialogConf.getWidth() != null && dialogConf.getHeight() != null) {
            scene = new FxScene(rootPane, dialogConf.getWidth(), dialogConf.getHeight());  //,width,height
        } else {
            scene = new FxScene(rootPane);  //,width,height
        }

        if (dialogConf.getNodeRelative() != null) {
            Bounds bounds = dialogConf.getNodeRelative().localToScreen(dialogConf.getNodeRelative().getBoundsInLocal());
            stage.setX(bounds.getMaxX());
            stage.setY(bounds.getMinY());
        }

        stage.setScene(scene);
        //stage.sizeToScene();

        if (dialogConf.getWidth() != null) {
            //Loghelper.debug(getClass(), "width ayarlanıyor" + dialogContext.getWidth());
            //stage.setWidth(dialogConfig.getWidth());
            rootPane.setPrefWidth(dialogConf.getWidth());
        }

        if (dialogConf.getWidth() == null && dialogConf.getHeight() == null) {
            stage.sizeToScene();
        }

        if (FiBool.isTrue(dialogConf.getBoNonModal())) {
            stage.initModality(Modality.WINDOW_MODAL);
        } else {
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        stage.showAndWait();

    }

    public static void nodeWindow(Node nodeRelative, IFiModCont iFiModCont, Integer width, Integer height, Boolean isNonModal) {
        //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont Giriş");

        Stage stage = iFiModCont.getFxStageInit();
        //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont");

        if (stage == null) {
            //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont-A1-Yeni stage-");
            stage = new FxStage();
            iFiModCont.setFxStage(stage);
        }

        FxScene scene = null;

        if (width != null && height != null) {
            scene = new FxScene(iFiModCont.getModView().getRootPane(), width, height);  //,width,height
        } else {
            scene = new FxScene(iFiModCont.getModView().getRootPane());  //,width,height
        }

        if (nodeRelative != null) {
            Bounds bounds = nodeRelative.localToScreen(nodeRelative.getBoundsInLocal());
            stage.setX(bounds.getMaxX());
            stage.setY(bounds.getMinY());
        }

        stage.setScene(scene);
        stage.sizeToScene();
        if (FiBool.isTrue(isNonModal)) {
            stage.initModality(Modality.APPLICATION_MODAL);
        } else {
            stage.initModality(Modality.WINDOW_MODAL);
        }

        stage.showAndWait();
    }
}
