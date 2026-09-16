package ozpasyazilim.utils.datatypes;

import java.util.HashMap;
import java.util.Map;

public class FiMap<K,V> extends HashMap<K, V> {

  public FiMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public FiMap(int initialCapacity) {
    super(initialCapacity);
  }

  public FiMap() {
  }

  public FiMap(Map<? extends K, ? extends V> m) {
    super(m);
  }


}
