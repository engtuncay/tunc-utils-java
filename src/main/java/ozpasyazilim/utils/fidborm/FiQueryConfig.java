package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link FiQueryConfig} : FiSqlGen Query Generation Configuration Class
 */
public class FiQueryConfig {

  IFiTableMeta iFiTableMeta;
  FicList ficList;
  Boolean boUpdateFieldsOnly;
  Fkfic fkbDataDef;

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
