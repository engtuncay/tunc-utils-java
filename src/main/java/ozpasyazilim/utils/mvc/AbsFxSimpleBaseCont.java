package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.DialogConfig;
import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.gui.fxcomponents.FxStage;

public abstract class AbsFxSimpleBaseCont implements IFxSimpleCont {

    protected Stage fxStage;
    protected String moduleCode;
    protected String moduleLabel;
    protected String closeReason;

    /**
     * 8-12-2022 eklendi
     * <p>
     * Modulun connProfil bilgisini içerir
     */
    public String connProfile;

    @Override
    public abstract void initCont();

    @Override
    public abstract IFxSimpleView getModView();

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

    public void closeStageWithDoneReason() {
        closeStage("done");
    }

    public void closeStageWithCancelReason() {
        closeStage("cancel");
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

        if (getCloseReason().equals("done")) {
            return true;
        }
        return false;
    }

    public void openAsNonModal() {
        openAsNonModalMain(null);
    }

    public void openAsNonModalMain(DialogConfig dialogConfig) {
        if (dialogConfig == null) {
            dialogConfig = new DialogConfig();
        }
        dialogConfig.setBoNonModal(true);
        openAsWindowMain(dialogConfig);
    }

    public void openAsModalMain(DialogConfig dialogConfig) {
        if (dialogConfig == null) {
            dialogConfig = new DialogConfig();
        }
        dialogConfig.setBoNonModal(false);
        openAsWindowMain(dialogConfig);
    }

    public void openAsWindowMain(DialogConfig dialogConfig) {

        FxDialogShow fxDialogShow = new FxDialogShow();

        if (getModView() == null || getModView().getRootPane() == null) {
            //Loghelper.debug(getClass(), "init çalıştırıldı openas den");
            initCont();
        }

        dialogConfig.setCssFileName("main.css");
        FxDialogShow.nodeWindow(this, dialogConfig);
    }

    public String getConnProfile() {
        return connProfile;
    }

    public void setConnProfile(String connProfile) {
        this.connProfile = connProfile;
    }
    
}
