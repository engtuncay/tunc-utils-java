package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.table.FicList;

/**
 * {@link FiQueryConfig} : FiSqlGen Query Generation Configuration Class
 */
public class FiQueryConfig {

  IFiTableMeta iFiTableMeta;
  FicList fclFields;
  Boolean boUpdateFieldsOnly;

  //FicList ficWhereFields;


  // Getters and Setters

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public FicList getFclFields() {
    return fclFields;
  }

  public void setFclFields(FicList fclFields) {
    this.fclFields = fclFields;
  }

  public Boolean getBoUpdateFieldsOnly() {
    return boUpdateFieldsOnly;
  }

  public void setBoUpdateFieldsOnly(Boolean boUpdateFieldsOnly) {
    this.boUpdateFieldsOnly = boUpdateFieldsOnly;
  }

//  public FicList getFicWhereFields() {
//    return ficWhereFields;
//  }
//
//  public void setFicWhereFields(FicList ficWhereFields) {
//    this.ficWhereFields = ficWhereFields;
//  }
}
