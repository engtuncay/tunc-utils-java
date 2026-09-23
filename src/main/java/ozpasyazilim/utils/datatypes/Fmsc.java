package ozpasyazilim.utils.datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Fi-Map-String-Class
 */
public class Fmsc extends HashMap<String, Class> {

  public Fmsc(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public Fmsc(int initialCapacity) {
    super(initialCapacity);
  }

  public Fmsc() {
  }

  public Fmsc(Map<? extends String, ? extends Class> m) {
    super(m);
  }

}
