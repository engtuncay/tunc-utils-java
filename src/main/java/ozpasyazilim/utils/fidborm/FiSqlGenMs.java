package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeybean;
//import ozpasyazilim.utils.ficRfcCoding;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metas.ocg.FimOcgSql;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import javax.annotation.Nonnull;

/**
 * FiSql Query Generation Class for MS SQL Server
 */
public class FiSqlGenMs {

  /**
   * Update Query Generation
   * <p>
   * FiSqlGenConfig Fields Used : iFiTableMeta, ficUpFields, ficWhereFields
   *
   * @param fiSqlGenConfig
   * @return
   */
  public static String upQuery(FiSqlGenConfig fiSqlGenConfig) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiSqlGenConfig.getiFiTableMeta();
    FicList ficFields = fiSqlGenConfig.getFclFields();

    //FimOcgSql.sfTableName();
    //FimOcgSql.sfTxWhere();

    String template = "UPDATE {{sfTableName}} SET {{sfTxSet}} \n"
        + " WHERE {{sfTxWhere}} ";

    StringBuilder sbTxSetBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficFields) {

      // if (FiBool.isTrue(fiCol.getBoKeyIdField())) {
      // if (FiBool.isTrue(boUpdateFieldsOnly)) {
      // if (FiBool.isTrue(fiCol.getBoUpdateFieldForQuery())) {

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd());
      } else {
        sbTxSetBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd());
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxSetBlock, getTxAnd());

    FiKeybean fkbParams = new FiKeybean();
    fkbParams.addFieldBy(FimOcgSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcgSql.sfTxSet(), sbTxSetBlock.toString());
    fkbParams.addFieldBy(FimOcgSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock==0) sql = "no where fields";

    //UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    return sql;
  }

  public static Fdr selQuery(FiSqlGenConfig fiSqlGenConfig) {
// Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiSqlGenConfig.getiFiTableMeta();
    FicList ficUpFields = fiSqlGenConfig.getFclFields();

    //FimOcgSql.sfTableName();
    //FimOcgSql.sfTxWhere();
    //FimOcgSql.sfTxFields();

    String template = "SELECT {{sfTxFields}}\n" +
        "FROM {{sfTableName}}\n"
        + "WHERE {{sfTxWhere}}";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficUpFields) {

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd());
      } else {
        sbTxFieldsBlock.append(fiCol.getFcTxFieldName()).append(getTxComma());
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());

    FiKeybean fkbParams = new FiKeybean();
    fkbParams.addFieldBy(FimOcgSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcgSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimOcgSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock==0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    //SELECT cha_cinsi
    //FROM CARI_HESAP_HAREKETLERI
    //WHERE cha_evrakno_seri = @cha_evrakno_seri AND cha_evrakno_sira = @cha_evrakno_sira AND cha_evrak_tip = @cha_evrak_tip

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  @Nonnull
  private static String getTxComma() {
    return ", ";
  }

  @Nonnull
  private static String getTxAnd() {
    return " AND ";
  }
}
