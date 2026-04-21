package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Key'e karşılık FiCol tutulduğu Obje. FiCol'da bir değeri tutan, gelişmiş bir objedir.
 */
public class FiKeyfkc extends LinkedHashMap<String, FiKeybean> {

  public FiKeyfkc() {
    super();
  }

  public FiKeyfkc(Map<? extends String, ? extends FiKeybean> m) {
    super(m);
  }

  public void addFic(FiKeybean fiCol) {
    put(fiCol.getFnm(), fiCol);
  }

}
