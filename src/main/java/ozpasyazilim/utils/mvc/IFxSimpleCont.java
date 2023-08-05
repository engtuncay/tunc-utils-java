package ozpasyazilim.utils.mvc;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * IFxSimpleCont Interface : void initCont(),IFxSimpleView getModView(),Stage getFxStage(),void setFxStage(...)
 * <p>
 * String getModuleCode(),String getModuleLabel(),String getCloseReason(),void setCloseReason(...);
 * <p>
 * Evalaution : Pane getRootPane(); eklenebilir
 */
public interface IFxSimpleCont {
    void initCont();

    IFxSimpleView getModView();

    //Pane getRootPane();

    // Stage tanımı controller üzerinden kullanılacak
    Stage getFxStage();

    void setFxStage(Stage stage);

    String getModuleCode();

    String getModuleLabel();

    /**
     * Pencere kapatırken kapatma durumunu belirtmek için
     *
     * @return
     */
    String getCloseReason();

    void setCloseReason(String closeReason);

}