package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Key'e karşılık FiCol tutulduğu Obje. FiCol'da bir değeri tutan, gelişmiş bir objedir.
 */
public class FiKeyfic extends LinkedHashMap<String, FiCol> {

  public FiKeyfic() {
    super();
  }

  public FiKeyfic(Map<? extends String, ? extends FiCol> m) {
    super(m);
  }

  public void addFic(FiCol fiCol) {
    put(fiCol.getFcTxFieldName(), fiCol);
  }

}
