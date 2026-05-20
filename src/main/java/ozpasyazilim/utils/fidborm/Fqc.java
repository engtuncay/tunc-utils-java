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
  Fkfic fkbDdFields;
  Fkfic fkbFieldsAll;
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

  public Fkfic getFkbDdFields() {
    return fkbDdFields;
  }

  public void setFkbDdFields(Fkfic fkbDdFields) {
    this.fkbDdFields = fkbDdFields;
  }

  public Fkfic getFkbFieldsAll() {
    return fkbFieldsAll;
  }

  public void setFkbFieldsAll(Fkfic fkbFieldsAll) {
    this.fkbFieldsAll = fkbFieldsAll;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }
}
