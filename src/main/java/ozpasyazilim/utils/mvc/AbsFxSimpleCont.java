package ozpasyazilim.utils.mvc;

import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.FxStage;

public abstract class AbsFxSimpleCont implements IFxSimpleCont {

	Stage fxStage;
	String moduleCode;
	String moduleLabel;
	String closeReason;

	@Override
	public abstract void initCont();

	@Override
	public abstract IFxSimpleView getModView();

	public Stage getFxStage() {
		if (fxStage == null) {
			fxStage = new FxStage();
		}
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

	protected void closeStageWithDoneReason() {
		closeStage("done");
	}
	protected void closeStageWithCancelReason() {
		closeStage("cancel");
	}


	protected void closeStage(String closeReason) {
		if (getFxStage() != null) {
			if(closeReason!=null){
				setCloseReason(closeReason);
			}
			getFxStage().close();
		}
	}

}
