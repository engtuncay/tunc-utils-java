package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.table.FiCol;

public abstract class AbsFecTable {

  public abstract FiCol qcfTxSqTableName();

  public FiCol qcfTxSqTableNameNtn() {
    if (qcfTxSqTableName() == null) {
      return new FiCol();
    }
    return qcfTxSqTableName();
  }

  public abstract Fkf getFkfAll();

  public Fkf getFkfAllNtn(){
    if (getFkfAll() == null) {
      return new Fkf();
    }
    return getFkfAll();
  }

  // IFxTable sorun çıkarmaması için eklendi - sonradan çıkarılacak (2026-05-06)

  //public static String getTxTableName() {
//    return "";
//  }

//  public String getITxTableName() {
//    return getTxTableName();
//  }

//  public String getITxPrefix() {
//    return "";
//  }

//  public FicList genITableCols() {
//    return new FicList();
//  }

//  public FicList genITableColsTrans() {
//    return new FicList();
//  }


  // --- end IFxTable...

}
