package ozpasyazilim.utils.gui.fxcomponents;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class FxToastPopup {

	private static Stage fxStage;

	private static int TOAST_TIMEOUT = 1400;

	private static Popup createPopup(final String message) {
		final Popup popup = new Popup();
		popup.setAutoFix(true);
		Label label = new Label(message);
		label.getStylesheets().add("/main.css");
		label.getStyleClass().add("fxToastPopup");
		popup.getContent().add(label);
		return popup;
	}

	public static void show(final String message) {
		show(message,null);
	}

	public static void show(final String message, final Control control) {
		Stage stage; // = (Stage) control.getScene().getWindow();

		if (getFxStage() != null) {
			stage = getFxStage();
		} else if(control!=null) {
			stage = (Stage) control.getScene().getWindow();
		} else {
			Notifications.create().text(message).showInformation();
			return;
		}

		final Popup popup = createPopup(message);
		popup.setOnShown(e -> {
			popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
			popup.setY(stage.getY() + stage.getHeight() / 1.2 - popup.getHeight() / 2);
		});
		popup.show(stage);

		new Timeline(new KeyFrame(
				Duration.millis(TOAST_TIMEOUT),
				ae -> popup.hide())).play();
	}

	public static Stage getFxStage() {
		return fxStage;
	}

	public static void setFxStage(Stage fxStage) {
		FxToastPopup.fxStage = fxStage;
	}
}