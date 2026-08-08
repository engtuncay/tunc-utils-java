package ozpasyazilim.utils.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Module react edeceği metodlar burada tanımlanır, böylelik modal sınıflar , gui'lere anlık mesaj gönderebilir
 * <p>
 * Reactive alanlar tanımlanacak
 */
public class FiObserver {

  List<Runnable> observers;

  public static FiObserver bui() {
    return new FiObserver();
  }

  public List<Runnable> getObservers() {
    if (observers == null) {
      observers = new ArrayList<>();
    }
    return observers;
  }

  public void trigObsMethodFinished() {
    for (Runnable runnable : getObservers()) {
      runnable.run();
    }
  }

}
