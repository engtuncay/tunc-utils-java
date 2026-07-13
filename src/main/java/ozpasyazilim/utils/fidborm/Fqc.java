package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link Fqc} : FiSqlGen Query Generation Configuration Class
 */
public class Fqc {

  IFiTableMeta iFiTableMeta;
  FicList fclTable;
  Boolean boUpdateFieldsOnly;
  Fkfic fkbDataDefs;
  Fkfic fkficFieldsAll;
  FiCol ficIdAuto;

  public Fqc() {
  }

  public Fqc(FicList fclTable) {
    setFclTable(fclTable);
  }

  public static Fqc bui(FicList ficList) {
    return new Fqc(ficList);
  }

  //FicList ficWhereFields;


  // Getters and Setters

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public FicList getFclTable() {
    return fclTable;
  }

  public void setFclTable(FicList fclTable) {
    this.fclTable = fclTable;
  }

  public Boolean getBoUpdateFieldsOnly() {
    return boUpdateFieldsOnly;
  }

  public void setBoUpdateFieldsOnly(Boolean boUpdateFieldsOnly) {
    this.boUpdateFieldsOnly = boUpdateFieldsOnly;
  }

  public Fkfic getFkbDataDefs() {
    return fkbDataDefs;
  }

  public void setFkbDataDefs(Fkfic fkbDataDefs) {
    this.fkbDataDefs = fkbDataDefs;
  }

  public Fkfic getFkficFieldsAll() {
    return fkficFieldsAll;
  }

  public void setFkficFieldsAll(Fkfic fkficFieldsAll) {
    this.fkficFieldsAll = fkficFieldsAll;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }
}
