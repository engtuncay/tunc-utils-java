package ozpasyazilim.utils.datatypes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Key'e karşılık FiCol tutulduğu Obje. FiCol'da bir değeri tutan, gelişmiş bir objedir.
 */
public class FiKeyfkc extends LinkedHashMap<String, Fkb> {

  public FiKeyfkc() {
    super();
  }

  public FiKeyfkc(Map<? extends String, ? extends Fkb> m) {
    super(m);
  }

  public void addFic(Fkb fiCol) {
    put(fiCol.getFnm(), fiCol);
  }

}
