package ozpasyazilim.utils.ficols;// Java FiCol Class Generation - v0.4

import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.fidborm.AbsFicTable;

public class FicFiCol extends AbsFicTable {


  public static FicList genTableCols() {
    FicList ficList = new FicList();

    ficList.add(fcTxEntityName());
    ficList.add(fcTxFieldType());
    ficList.add(fcTxFieldName());
    ficList.add(fcTxHeader());
    ficList.add(fcTxFieldDesc());
    ficList.add(fcTxIdType());
    ficList.add(fcBoTransient());
    ficList.add(fcLnLength());
    ficList.add(fcBoNullable());
    ficList.add(fcLnPrecision());
    ficList.add(fcLnScale());
    ficList.add(fcBoUnique());
    ficList.add(fcBoUniqGro1());
    ficList.add(fcTxDefValue());
    ficList.add(fcTxCollation());
    ficList.add(fcTxTypeName());
    ficList.add(fcBoFilterLike());
    ficList.add(fcTxId());


    return ficList;
  }

  public static FicList genTableColsTrans() {
    FicList ficList = new FicList();


    return ficList;
  }

  public static FiCol fcTxEntityName() {
    FiCol fiCol = new FiCol("fcTxEntityName");

    return fiCol;
  }

  public static FiCol fcTxFieldType() {
    FiCol fiCol = new FiCol("fcTxFieldType");

    return fiCol;
  }

  public static FiCol fcTxFieldName() {
    FiCol fiCol = new FiCol("fcTxFieldName");

    return fiCol;
  }

  public static FiCol fcTxHeader() {
    FiCol fiCol = new FiCol("fcTxHeader");

    return fiCol;
  }

  public static FiCol fcTxFieldDesc() {
    FiCol fiCol = new FiCol("fcTxFieldDesc");

    return fiCol;
  }

  public static FiCol fcTxIdType() {
    FiCol fiCol = new FiCol("fcTxIdType");

    return fiCol;
  }

  public static FiCol fcBoTransient() {
    FiCol fiCol = new FiCol("fcBoTransient");

    return fiCol;
  }

  public static FiCol fcLnLength() {
    FiCol fiCol = new FiCol("fcLnLength");

    return fiCol;
  }

  public static FiCol fcBoNullable() {
    FiCol fiCol = new FiCol("fcBoNullable");

    return fiCol;
  }

  public static FiCol fcLnPrecision() {
    FiCol fiCol = new FiCol("fcLnPrecision");

    return fiCol;
  }

  public static FiCol fcLnScale() {
    FiCol fiCol = new FiCol("fcLnScale");

    return fiCol;
  }

  public static FiCol fcBoUnique() {
    FiCol fiCol = new FiCol("fcBoUnique");

    return fiCol;
  }

  public static FiCol fcBoUniqGro1() {
    FiCol fiCol = new FiCol("fcBoUniqGro1");

    return fiCol;
  }

  public static FiCol fcTxDefValue() {
    FiCol fiCol = new FiCol("fcTxDefValue");

    return fiCol;
  }

  public static FiCol fcTxCollation() {
    FiCol fiCol = new FiCol("fcTxCollation");

    return fiCol;
  }

  public static FiCol fcTxTypeName() {
    FiCol fiCol = new FiCol("fcTxTypeName");

    return fiCol;
  }

  public static FiCol fcBoFilterLike() {
    FiCol fiCol = new FiCol("fcBoFilterLike");

    return fiCol;
  }

  public static FiCol fcTxId() {
    FiCol fiCol = new FiCol("fcTxId");

    return fiCol;
  }


  public static Fkfic getFkbFieldsAll() {

    Fkfic fkb = new Fkfic();

    fkb.addFic(fcTxEntityName());
    fkb.addFic(fcTxFieldType());
    fkb.addFic(fcTxFieldName());
    fkb.addFic(fcTxHeader());
    fkb.addFic(fcTxFieldDesc());
    fkb.addFic(fcTxIdType());
    fkb.addFic(fcBoTransient());
    fkb.addFic(fcLnLength());
    fkb.addFic(fcBoNullable());
    fkb.addFic(fcLnPrecision());
    fkb.addFic(fcLnScale());
    fkb.addFic(fcBoUnique());
    fkb.addFic(fcBoUniqGro1());
    fkb.addFic(fcTxDefValue());
    fkb.addFic(fcTxCollation());
    fkb.addFic(fcTxTypeName());
    fkb.addFic(fcBoFilterLike());
    fkb.addFic(fcTxId());

    return fkb;
  }

  public static Fkfic getFkbDdFields() {

    Fkfic fkb = new Fkfic();


    return fkb;
  }

}
