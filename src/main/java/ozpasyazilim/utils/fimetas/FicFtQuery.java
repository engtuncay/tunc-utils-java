package ozpasyazilim.utils.fimetas;

// Java FiCol Class Generation - v0.4

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.fidborm.AbsFicTable;

public class FicFtQuery extends AbsFicTable {


  public static FicList genTableCols() {
    FicList ficList = new FicList();

    ficList.add(fqpLnPageOffset());
    ficList.add(fqpLnPageLength());
    ficList.add(fqpLnTotalCount());
    ficList.add(fqpLnPageNo());


    return ficList;
  }

  public static FicList genTableColsTrans() {
    FicList ficList = new FicList();


    return ficList;
  }

  public static FiCol fqpLnPageOffset() {
    FiCol fiCol = new FiCol("fqpLnPageOffset");
    fiCol.setFcTxFieldType("int");

    return fiCol;
  }

  public static FiCol fqpLnPageLength() {
    FiCol fiCol = new FiCol("fqpLnPageLength");
    fiCol.setFcTxFieldType("int");

    return fiCol;
  }

  public static FiCol fqpLnTotalCount() {
    FiCol fiCol = new FiCol("fqpLnTotalCount");
    fiCol.setFcTxFieldType("int");

    return fiCol;
  }

  public static FiCol fqpLnPageNo() {
    FiCol fiCol = new FiCol("fqpLnPageNo");
    fiCol.setFcTxFieldType("int");

    return fiCol;
  }


  public static Fkfic getFkbFieldsAll() {

    Fkfic fkb = new Fkfic();

    fkb.addFic(fqpLnPageOffset());
    fkb.addFic(fqpLnPageLength());
    fkb.addFic(fqpLnTotalCount());
    fkb.addFic(fqpLnPageNo());

    return fkb;
  }

  public static Fkfic getFkbDdFields() {

    Fkfic fkb = new Fkfic();


    return fkb;
  }

}
