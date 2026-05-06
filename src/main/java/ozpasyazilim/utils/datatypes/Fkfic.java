package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.table.FiCol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Key'e karşılık FiCol tutulduğu Obje. FiCol'da bir değeri tutan, gelişmiş bir objedir.
 */
public class Fkfic extends LinkedHashMap<String, FiCol> {

  public Fkfic() {
    super();
  }

  public Fkfic(Map<? extends String, ? extends FiCol> m) {
    super(m);
  }

  public void addFic(FiCol fiCol) {
    put(fiCol.getFcTxFieldName(), fiCol);
  }

  private FiCol getFieldValue(String txKey) {
    return get(txKey);
  }

  public FiCol getFimValue(FiMeta fiCol) {
    return getFieldValue(fiCol.getFtTxKey());
  }

  public String getFimHevalNtn(FiMeta fiCol) {
    FiCol fieldValue = getFieldValue(fiCol.getFtTxKey());
    if (fieldValue != null) {
      return fieldValue.getFcTxHeader();
    }
    return "";
  }
}
