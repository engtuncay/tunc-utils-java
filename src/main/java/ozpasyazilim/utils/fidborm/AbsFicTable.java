package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

public abstract class AbsFicTable {

  // IFxTable sorun çıkarmaması için eklendi - sonradan çıkarılacak (2026-05-06)

  public static String getTxTableName() {
    return "";
  }

  public String getITxTableName() {
    return getTxTableName();
  }

  public String getITxPrefix() {
    return "";
  }

  public FicList genITableCols() {
    return new FicList();
  }

  public FicList genITableColsTrans() {
    return new FicList();
  }

  // --- end IFxTable...

}
