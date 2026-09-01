package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

/**
 * {@link FiQuconf} : Fi-Query-Config : (For Query Generation)
 */
public class FiQuconf {

  // Main
  Fkf fkfAll;

  // Alt-1
  Fkf fkfDataDef;
  // Alt-2
  IFiTableMeta iFiTableMeta;
  Boolean boUpdateFieldsOnly;
  FiCol ficIdAuto;
  FicList ficListTable;

  // Update Query V1 de kullanıldı: qcfTxSqTableName,ficListUp,ficListWhere

  /**
   * Tablo ismi buranın header alanından alınır
   */
  FiCol qcfTxSqTableName;

  /**
   * Update Sorgusunda Set Alanı (Güncellenecek Alanlar)
   */
  FicList ficListUp;

  /**
   * Update Sorgusunda Where Alanı
   */
  FicList ficListWhere;

  //FicMkCariHar.qcfTxSqTableName(), ficListUp, ficListCandId

  public FiQuconf() {
  }

  public FiQuconf(FicList ficListTable) {
    setFicListTable(ficListTable);
  }

  public static FiQuconf bui(FicList ficList) {
    return new FiQuconf(ficList);
  }

  public static FiQuconf buiUpV1(FiCol qcfTxSqTableName, FicList ficUpFields, FicList ficWhereFields) {
    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setQcfTxSqTableName(qcfTxSqTableName);
    fiQuconf.setFicListUp(ficUpFields);
    fiQuconf.setFicListWhere(ficWhereFields);
    return fiQuconf;
  }

  public static FiQuconf buiDelV1(FiCol qcfTxSqTableName, FicList ficWhereFields) {
    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setQcfTxSqTableName(qcfTxSqTableName);
    fiQuconf.setFicListWhere(ficWhereFields);
    return fiQuconf;
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

  public Fkf getFkficDataDef() {
    return fkfDataDef;
  }

  public void setFkficDataDef(Fkf fkfDataDef) {
    this.fkfDataDef = fkfDataDef;
  }

  public Fkf getFkfAll() {
    return fkfAll;
  }

  public void setFkfAll(Fkf fkfAll) {
    this.fkfAll = fkfAll;
  }

  public FiCol getFicIdAuto() {
    return ficIdAuto;
  }

  public void setFicIdAuto(FiCol ficIdAuto) {
    this.ficIdAuto = ficIdAuto;
  }

  public FiCol getQcfTxSqTableName() {
    return qcfTxSqTableName;
  }

  public void setQcfTxSqTableName(FiCol qcfTxSqTableName) {
    this.qcfTxSqTableName = qcfTxSqTableName;
  }

  public FicList getFicListUp() {
    return ficListUp;
  }

  public void setFicListUp(FicList ficListUp) {
    this.ficListUp = ficListUp;
  }

  public FicList getFicListWhere() {
    return ficListWhere;
  }

  public void setFicListWhere(FicList ficListWhere) {
    this.ficListWhere = ficListWhere;
  }
}
