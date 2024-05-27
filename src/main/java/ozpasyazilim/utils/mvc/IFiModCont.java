package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;

/**
 * IFiModCont - Module Pencerelerinin interface
 * <p>
 * IFiModCont (eski adı IFxSimpleCont) Interface : void initCont(),IFxSimpleView getModView(),Stage getFxStage(),void setFxStage(...)
 * <p>
 * String getModuleCode(),String getModuleLabel(),String getCloseReason(),void setCloseReason(...);
 * <p>
 * Evalaution : Pane getRootPane(); eklenebilir
 */
public interface IFiModCont {

    void initCont();

    IFiModView getModView();

    //Pane getRootPane();

    // Stage tanımı controller üzerinden kullanılacak
    Stage getFxStageInit();

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