package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;

/**
 * IFiModCont - Module Pencerelerinin interface tanımı
 * <p>
 * IFiModCont (eski adı IFxSimpleCont) Interface : void initCont(),IFiModView getModView(),Stage getFxStage(),void setFxStage(..)
 * <p>
 * String getModuleCode(),String getModuleLabel(),String getCloseReason(),void setCloseReason(..);
 *
 */
public interface IFiModCont {

    void initCont();

    IFiModView getModView();

    // IFiModView üzerinden çalışmayacaksa düşünülebilir, gerekli değil gibi, review yapılmalı
    //Pane getRootPane();

    // Stage objesi, controller üzerinden kullanılması için
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

    public String getConnProfile();

    public void setConnProfile(String connProfile);

}