package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link Fqc} : FiSqlGen Query Generation Configuration Class
 */
public class Fqc {

  IFiTableMeta iFiTableMeta;
  FicList ficList;
  Boolean boUpdateFieldsOnly;
  Fkfic fkbDataDef;

  public Fqc() {
  }

  public Fqc(FicList ficList) {
    setFicList(ficList);
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

  public FicList getFicList() {
    return ficList;
  }

  public void setFicList(FicList ficList) {
    this.ficList = ficList;
  }

  public Boolean getBoUpdateFieldsOnly() {
    return boUpdateFieldsOnly;
  }

  public void setBoUpdateFieldsOnly(Boolean boUpdateFieldsOnly) {
    this.boUpdateFieldsOnly = boUpdateFieldsOnly;
  }

  public Fkfic getFkbDataDef() {
    return fkbDataDef;
  }

  public void setFkbDataDef(Fkfic fkbDataDef) {
    this.fkbDataDef = fkbDataDef;
  }

}
