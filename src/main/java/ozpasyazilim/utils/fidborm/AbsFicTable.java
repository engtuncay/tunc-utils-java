package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

public abstract class AbsFicTable {

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

//  public static FiCol sqTableName()
//  {
//    FiCol fiCol = new FiCol();
//    return fiCol;
//  }
//
//  public static FiKeybean genFkbFields()
//  {
//    FiKeybean fkbCol = new FiKeybean();
//    return fkbCol;
//  }


}
