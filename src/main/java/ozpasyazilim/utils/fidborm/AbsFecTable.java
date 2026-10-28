package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.table.FiCol;

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

}
