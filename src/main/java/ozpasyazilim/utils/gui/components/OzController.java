package ozpasyazilim.utils.gui.components;

import javafx.application.Platform;
import javafx.stage.Stage;
import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;

/**
 * Genel olarak kontroller sınıfının ihtiyaç duyacağı metodlar
 *
 * @author TUNC
 */
@Deprecated
public abstract class OzController {

	public String moduleName;

	public String moduleCode;

	public Stage fxStage;

	private String closeReason;

	/**
	 * Kullanıcıya diyalog mesajı verecek.
	 * <br> Action/Bindings Controller da , Tasarım View de olur.
	 *
	 * @param message
	 */
	public void dialogMessage(String message) {

		if (message == null) return;;
			//message = "null";

		Platform.runLater(() -> {
			 new FxDialogShow().showModalInfoAlert(message);
		});

//		Toolkit.getDefaultToolkit().beep();
//		JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
//		JDialog dialog = optionPane.createDialog("Uyarı!");
//		dialog.setAlwaysOnTop(true);
//		dialog.setVisible(true);

	}

	public void initCont() { }

	public void afterInit() { }

	public abstract OzPanel getOzPanel(); // {return null;}

	public void logmodel(String message) {}

	public String getModuleLabel() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Stage getFxStage() {return fxStage;}

	public void setFxStage(Stage fxStage) {this.fxStage = fxStage;}

	public String getCloseReason() {return closeReason;}

	public void setCloseReason(String closeReason) {this.closeReason = closeReason;}
}
