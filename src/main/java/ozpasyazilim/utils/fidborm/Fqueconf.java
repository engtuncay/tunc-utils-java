package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link Fqueconf} : Fi-Query-Config : (For Query Generation)
 */
public class Fqueconf {

  // Main
  Fkfic fkcFieldsAll;

  // Alt-1
  Fkfic fkcDmFields;
  // Alt-2
  IFiTableMeta iFiTableMeta;
  Boolean boUpdateFieldsOnly;
  FiCol ficIdAuto;
  FicList fclTable;

  public Fqueconf() {
  }

  public Fqueconf(FicList fclTable) {
    setFclTable(fclTable);
  }

  public static Fqueconf bui(FicList ficList) {
    return new Fqueconf(ficList);
  }

  // FicList ficWhereFields;

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

  public Fkfic getFkcDmFields() {
    return fkcDmFields;
  }

  public void setFkcDmFields(Fkfic fkcDmFields) {
    this.fkcDmFields = fkcDmFields;
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
