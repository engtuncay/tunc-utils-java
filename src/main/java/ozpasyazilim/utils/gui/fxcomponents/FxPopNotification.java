package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import org.controlsfx.control.Notifications;
import ozpasyazilim.utils.core.FiType;
import ozpasyazilim.utils.returntypes.FnResult;

/**
 * Use FxDialogShow
 */
@Deprecated
public class FxPopNotification {

	public static String titleGeneral;

	public void showPopNotification(String title, String message){

		Platform.runLater(() -> {
			Notifications.create().title(title).text(message).showInformation();
		});
	}

	public void showPopNotification(String message){

		Platform.runLater(() -> {
			Notifications.create().title(FiType.orEmpty(FxDialogShow.titleGeneral)).text(message).showInformation();
		});

	}

	public static String getTitleGeneral() {
		if(titleGeneral==null) return "";
		return titleGeneral;
	}

	public static void setTitleGeneral(String titleGeneral) {
		FxPopNotification.titleGeneral = titleGeneral;
	}

	public void showDeleteMessage(FnResult fnResult1) {

		if(FiType.isTrue(fnResult1)){
			showPopNotification("Kayıt Silindi...");
		}else{
			showPopNotification("Kayıt Silinirken hata oluştu !!!");
		}
	}
}
