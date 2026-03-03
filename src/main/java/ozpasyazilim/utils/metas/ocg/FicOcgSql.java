package ozpasyazilim.utils.metas.ocg;
// Java FiCol Class Generation - v0.4

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.fidborm.IFiTableMeta;

public class FicOcgSql implements IFiTableMeta {

  public static String getTxTableName() {
    return "OcgSql";
  }

  public String getITxTableName() {
    return getTxTableName();
  }

  public FicList genITableCols() {
    return genTableCols();
  }

  public FicList genITableColsTrans() {
    return genTableColsTrans();
  }

  public static String getTxPrefix() {
    return "sf";
  }

  public String getITxPrefix() {
    return getTxPrefix();
  }


  public static FicList genTableCols() {
    FicList ficList = new FicList();

    ficList.add(sfTableName());
    ficList.add(sfTableFields());
    ficList.add(sfCsvFields());
    ficList.add(sfTxWhere());
    ficList.add(sfTxFieldName());
    ficList.add(sfTxSet());
    ficList.add(sfLnCount());


    return ficList;
  }

  public static FicList genTableColsTrans() {
    FicList ficList = new FicList();


    return ficList;
  }

  public static FiCol sfTableName() {
    FiCol fiCol = new FiCol("sfTableName");

    return fiCol;
  }

  public static FiCol sfTableFields() {
    FiCol fiCol = new FiCol("sfTableFields");

    return fiCol;
  }

  public static FiCol sfCsvFields() {
    FiCol fiCol = new FiCol("sfCsvFields");

    return fiCol;
  }

  public static FiCol sfTxWhere() {
    FiCol fiCol = new FiCol("sfTxWhere");

    return fiCol;
  }

  public static FiCol sfTxFieldName() {
    FiCol fiCol = new FiCol("sfTxFieldName");

    return fiCol;
  }

  public static FiCol sfTxSet() {
    FiCol fiCol = new FiCol("sfTxSet");

    return fiCol;
  }

  public static FiCol sfLnCount() {
    FiCol fiCol = new FiCol("sfLnCount");

    return fiCol;
  }


}
