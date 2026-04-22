package ozpasyazilim.utils.javafx;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class FiFxKeymap {

  public static void registerCtrlKey(Pane pane, KeyCode keyCode, Runnable runnable) {
    pane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.isControlDown() && event.getCode() == keyCode) {
        runnable.run();
      }
    });
  }

  public static void registerAltKey(Pane pane, KeyCode keyCode, Runnable runnable) {
    pane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.isAltDown() && event.getCode() == keyCode) {
        runnable.run();
      }
    });
  }

  public static void registerKey(Pane pane, KeyCode keyCode, Runnable runnable) {
    pane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.getCode() == keyCode) {
        runnable.run();
      }
    });
  }


}
