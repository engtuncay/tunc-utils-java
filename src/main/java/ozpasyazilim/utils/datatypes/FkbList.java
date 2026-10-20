package ozpasyazilim.utils.datatypes;

import ozpasyazilim.utils.core.FiConsole;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.table.FiCol;

import java.util.*;

/**
 * Fkl : FkbList
 */
public class FkbList extends ArrayList<Fkb> {

  /**
   * FiKeybean listesi ile birlikte kullanılacak kolon bilgilerini tutar.
   */
  List<FiCol> ficColList;

  public FkbList() {
    super();
  }

  public FkbList(Collection<? extends Fkb> c) {
    super(c);
  }

  public static List<Fkb> bui(Fkb... selectedItemFiGen) {
    FkbList fkbList = new FkbList();
    fkbList.addAll(Arrays.asList(selectedItemFiGen));
    return fkbList;
  }

  public List<FiCol> getFicColList() {
    return ficColList;
  }

  public void setFicColList(List<FiCol> ficColList) {
    this.ficColList = ficColList;
  }

  public Fkb getNtn(int index) {
    if (index < 0 || (index + 1) > size()) {
      return new Fkb();
    }
    //rangeCheck(index);
    return get(index);
  }

  public void logContentNtn(String txDesc) {
    Loghelper.get(getClass()).debug(txDesc + "\n" + FiConsole.textFkbListNtn(this));
  }

  public String toString() {
    return "[FkbList size=" + size() + "]";
  }

//	@Override
//	public String toString() {
//		Iterator<Fkb> it = iterator();
//		if (!it.hasNext()) return "[]";
//
//		StringBuilder sb = new StringBuilder();
//		sb.append('[');
//		for (;;) {
//			Fkb e = it.next();
//			sb.append(fkbToStringSafe(e));
//			if (!it.hasNext()) return sb.append(']').toString();
//			sb.append(',').append(' ');
//		}
//	}
//
//	private String fkbToStringSafe(Fkb fkb) {
//		if (fkb == null) return "null";
//		StringBuilder sb = new StringBuilder("{");
//		boolean first = true;
//		for (Map.Entry<String, Object> entry : fkb.entrySet()) {
//			if (!first) sb.append(", ");
//			first = false;
//			sb.append(entry.getKey()).append("=");
//			Object val = entry.getValue();
//			if (val instanceof FkbList) {
//				sb.append("[FkbList size=").append(((FkbList) val).size()).append("]");
//			} else if (val instanceof Collection) {
//				sb.append("[Collection size=").append(((Collection<?>) val).size()).append("]");
//			} else if (val instanceof Map) {
//				sb.append("[Map size=").append(((Map<?, ?>) val).size()).append("]");
//			} else {
//				sb.append(val);
//			}
//		}
//		return sb.append("}").toString();
//	}

}
