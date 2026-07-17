package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link Fqueconf} : FiSqlGen Query Generation Configuration Class
 */
public class Fqueconf {

  IFiTableMeta iFiTableMeta;
  FicList fclTable;
  Boolean boUpdateFieldsOnly;
  Fkfic fkficDataDefs;
  Fkfic fkcFieldsAll;
  FiCol ficIdAuto;

  public Fqueconf() {
  }

  public Fqueconf(FicList fclTable) {
    setFclTable(fclTable);
  }

  public static Fqueconf bui(FicList ficList) {
    return new Fqueconf(ficList);
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

  public Fkfic getFkficDataDefs() {
    return fkficDataDefs;
  }

  public void setFkficDataDefs(Fkfic fkficDataDefs) {
    this.fkficDataDefs = fkficDataDefs;
  }

  public Fkfic getFkcFieldsAll() {
    return fkcFieldsAll;
  }

  public void setFkcFieldsAll(Fkfic fkcFieldsAll) {
    this.fkcFieldsAll = fkcFieldsAll;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }
}
