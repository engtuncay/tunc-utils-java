package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FicList;

/**
 * FiSqlGenConfig : FiSqlGen Query Generation Configuration Class
 */
public class FiSqlGenConfig {

  IFiTableMeta iFiTableMeta;
  FicList ficUpFields;
  FicList ficWhereFields;
  Boolean boUpdateFieldsOnly;


  // Getters and Setters

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public FicList getFicUpFields() {
    return ficUpFields;
  }

  public void setFicUpFields(FicList ficUpFields) {
    this.ficUpFields = ficUpFields;
  }

  public FicList getFicWhereFields() {
    return ficWhereFields;
  }

  public void setFicWhereFields(FicList ficWhereFields) {
    this.ficWhereFields = ficWhereFields;
  }

  public Boolean getBoUpdateFieldsOnly() {
    return boUpdateFieldsOnly;
  }

  public void setBoUpdateFieldsOnly(Boolean boUpdateFieldsOnly) {
    this.boUpdateFieldsOnly = boUpdateFieldsOnly;
  }
}
