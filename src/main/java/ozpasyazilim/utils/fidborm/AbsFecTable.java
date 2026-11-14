package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

public abstract class AbsFecTable {

  public abstract FiCol qcfTxSqTableName();

  public abstract Fkf getFkfAll();

  public FiCol qcfTxSqTableNameNtn() {
    if (qcfTxSqTableName() == null) {
      return new FiCol();
    }
    return qcfTxSqTableName();
  }

  public Fkf getFkfAllNtn() {
    if (getFkfAll() == null) {
      return new Fkf();
    }
    return getFkfAll();
  }


  // AbsFicTable için eklendi - sonradan çıkarılacak (2026-11-13)

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

}
