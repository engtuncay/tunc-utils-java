package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.DialogConf;
import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.gui.fxcomponents.FxStage;
import ozpasyazilim.utils.gui.fxcomponents.FxWindow;

/**
 * Tüm Module Kontrollerde olması gereken alanlar ve metodlar
 */
public abstract class AbsFiModBaseCont implements IFiModCont {

    protected Stage fxStage;
    protected String moduleCode;
    protected String moduleLabel;
    protected String closeReason;

    /**
     * Modulun connProfil bilgisini içerir
     * <p>
     * 8-12-2022 eklendi
     */
    protected String connProfile;

    @Override
    public abstract void initCont();

    @Override
    public abstract IFiModView getModView();

    public Stage getFxStageInit() {
        if (fxStage == null) {
            fxStage = new FxStage();
        }
        return fxStage;
    }

    public Stage getFxStage() {
        return fxStage;
    }

    public void setFxStage(Stage fxStage) {
        this.fxStage = fxStage;
    }

    public String getModuleCode() {
        if (moduleCode == null) {
            return "";
        }

        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleLabel() {
        if (moduleCode == null) {
            return "";
        }
        return moduleLabel;
    }

    public void setModuleLabel(String moduleLabel) {
        this.moduleLabel = moduleLabel;
    }

    public String getCloseReason() {
        if (closeReason == null) {
            return "";
        }
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public void setCloseReasonDone() {
        setCloseReason(getDoneText());
    }

    public void closeStageWithDoneReason() {
        closeStage(getDoneText());
    }

    public static String getDoneText() {
        return "done";
    }

    public static String getCancelText() {
        return "cancel";
    }

    public void closeStageWithCancelReason() {
        closeStage(getCancelText());
    }

    protected void closeStage(String closeReason) {
        if (getFxStageInit() != null) {
            if (closeReason != null) {
                setCloseReason(closeReason);
            }
            getFxStageInit().close();
        }
    }

    public Boolean checkClosedWithDone() {
        return getCloseReason().equals(getDoneText());
    }

    public void openAsNonModal() {
        openAsNonModalMain(null);
    }

    public void openAsNonModalMain(DialogConf dialogConf) {
        if (dialogConf == null) {
            dialogConf = new DialogConf();
        }
        dialogConf.setBoNonModal(true);
        openAsWindowMain(dialogConf);
    }

    public void openAsModalMain(DialogConf dialogConf) {
        if (dialogConf == null) {
            dialogConf = new DialogConf();
        }
        dialogConf.setBoNonModal(false);
        openAsWindowMain(dialogConf);
    }

    public void openAsWindowMain(DialogConf dialogConf) {

        //FxDialogShow fxDialogShow = new FxDialogShow();

        if (getModView() == null || getModView().getRootPane() == null) {
            //Loghelper.debug(getClass(), "init çalıştırıldı openas den");
            initCont();
        }

        dialogConf.setCssFileName("main.css");
        FxWindow.nodeWindow(this, dialogConf);
    }

    public String getConnProfile() {
        return connProfile;
    }

    public void setConnProfile(String connProfile) {
        this.connProfile = connProfile;
    }

}
