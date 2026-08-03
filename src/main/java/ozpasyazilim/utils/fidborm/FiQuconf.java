package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link FiQuconf} : Fi-Query-Config : (For Query Generation)
 */
public class FiQuconf {

  // Main
  Fkfic fkficAll;

  // Alt-1
  Fkfic fkcDmFields;
  // Alt-2
  IFiTableMeta iFiTableMeta;
  Boolean boUpdateFieldsOnly;
  FiCol ficIdAuto;
  FicList ficListTable;

  public FiQuconf() {
  }

  public FiQuconf(FicList ficListTable) {
    setFicListTable(ficListTable);
  }

  public static FiQuconf bui(FicList ficList) {
    return new FiQuconf(ficList);
  }

  // FicList ficWhereFields;

  // Getters and Setters

  public IFiTableMeta getiFiTableMeta() {
    return iFiTableMeta;
  }

  public void setiFiTableMeta(IFiTableMeta iFiTableMeta) {
    this.iFiTableMeta = iFiTableMeta;
  }

  public FicList getFicListTable() {
    return ficListTable;
  }

  public void setFicListTable(FicList ficListTable) {
    this.ficListTable = ficListTable;
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

  public Fkfic getFkficAll() {
    return fkficAll;
  }

  public void setFkficAll(Fkfic fkficAll) {
    this.fkficAll = fkficAll;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }
}
