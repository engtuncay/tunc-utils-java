package ozpasyazilim.utils.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Module react edeceği metodlar burada tanımlanır, böylelik modal sınıflar , gui'lere anlık mesaj gönderebilir
 * <p>
 * Reactive alanlar tanımlanacak
 */
public class FiModObserver {

    List<Runnable> obsMethodFinished;

    public static FiModObserver bui() {
        return new FiModObserver();
    }

    public List<Runnable> getObsMethodFinished() {
        if (obsMethodFinished == null) {
            obsMethodFinished = new ArrayList<>();
        }
        return obsMethodFinished;
    }

    public void trigObsMethodFinished(){
        for (Runnable runnable : getObsMethodFinished()) {
            runnable.run();
        }
    }

}
