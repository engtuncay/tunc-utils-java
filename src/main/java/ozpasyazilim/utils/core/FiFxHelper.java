package ozpasyazilim.utils.core;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class FiFxHelper {

  public static void copyToClipboard(String text) {
    Platform.runLater(() -> {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      ClipboardContent content = new ClipboardContent();
      content.putString(text);
      clipboard.setContent(content);
    });
  }

}
