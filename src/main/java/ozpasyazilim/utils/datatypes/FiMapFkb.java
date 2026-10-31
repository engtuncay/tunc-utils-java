package ozpasyazilim.utils.datatypes;

import java.util.HashMap;
import java.util.Map;

public class FiMapFkb<String,Fkb> extends HashMap<String, Fkb> {

  public FiMapFkb(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public FiMapFkb(int initialCapacity) {
    super(initialCapacity);
  }

  public FiMapFkb() {
  }

  public FiMapFkb(Map<? extends String, ? extends Fkb> m) {
    super(m);
  }


}
