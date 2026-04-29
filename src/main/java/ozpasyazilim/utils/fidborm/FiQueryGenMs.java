package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeybean;
//import ozpasyazilim.utils.ficRfcCoding;
import ozpasyazilim.utils.metadata.fimCodegen.FimOcSql;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import javax.annotation.Nonnull;

/**
 * FiSql Query Generation Class for MS SQL Server
 */
public class FiQueryGenMs {

  /**
   * Update Query Generation
   * <p>
   * FiSqlGenConfig Fields Used : iFiTableMeta, ficUpFields, ficWhereFields
   *
   * @param fiQueryConfig
   * @return
   */
  public static String upQuery(FiQueryConfig fiQueryConfig) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQueryConfig.getiFiTableMeta();
    FicList ficFields = fiQueryConfig.getFicList();

//    FimOcSql.sfTableName();
//    FimOcSql.sfTxWhere();
//    FimOcSql.sfTxUpSet();

    String template = "UPDATE {{sfTableName}} SET {{sfTxUpSet}} \n"
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
    fkbParams.addFieldBy(FimOcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcSql.sfTxUpSet(), sbTxSetBlock.toString());
    fkbParams.addFieldBy(FimOcSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock==0) sql = "no where fields";

    //UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    return sql;
  }

  public static Fdr selQuery(FiQueryConfig fiQueryConfig) {
// Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQueryConfig.getiFiTableMeta();
    FicList ficUpFields = fiQueryConfig.getFicList();

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

      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }

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
    fkbParams.addFieldBy(FimOcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimOcSql.sfTxWhere(), sbTxWhereBlock.toString());

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

  public static Fdr insIfNot(FiQueryConfig fiQueryConfig) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQueryConfig.getiFiTableMeta();
    FicList ficUpFields = fiQueryConfig.getFicList();

    //FimOcgSql.sfTableName();
    //FimOcgSql.sfTxWhere();
    //FimOcgSql.sfTxFields();
    //FimOcgSql.sfTxFieldsVar();

    String template= "DECLARE @__count AS int = 0\n" +
        "\n" +
        "SELECT @__count = count(*) FROM {{sfTableName}}\n" +
        "WHERE {{sfTxWhere}}\n" +
        "\n" +
        "IF @__count = 0 \n" +
        "BEGIN\n" +
        "  INSERT INTO {{sfTableName}} ({{sfTxFields}})\n" +
        "  VALUES ({{sfTxFieldsVar}})\n" +
        "END";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficUpFields) {

      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd());
        sbTxFieldsBlock.append(fiCol.getFcTxFieldName()).append(getTxComma());
        sbTxFieldsVar.append(" @").append(fiCol.getFcTxFieldName()).append(getTxComma());
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());

    FiKeybean fkbParams = new FiKeybean();
    fkbParams.addFieldBy(FimOcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimOcSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimOcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimOcSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock==0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  public static Fdr insUpdate(FiQueryConfig fiQueryConfig) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQueryConfig.getiFiTableMeta();
    FicList ficUpFields = fiQueryConfig.getFicList();

//    FimOcSql.sfTableName();
//    FimOcSql.sfTxWhere();
//    FimOcSql.sfTxFields();
//    FimOcSql.sfTxFieldsVar();
//    FimOcSql.sfTxUpSet();

    String template= "--sq202604221135 v1\n" +
        "DECLARE @__count AS int = 0\n" +
        "\n" +
        "SELECT @__count = count(*) FROM {{sfTableName}}\n" +
        "WHERE {{sfTxWhere}}\n" +
        "\n" +
        "IF @__count = 0 \n" +
        "BEGIN\n" +
        "  INSERT INTO {{sfTableName}} ( {{sfTxFields}} )\n" +
        "  VALUES ( {{sfTxFieldsVar}} )\n" +
        "END;\n" +
        "\n" +
        "UPDATE {{sfTableName}}\n" +
        "SET {{sfTxUpSet}}\n" +
        "WHERE {{sfTxWhere}}\n";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();
    StringBuilder sbTxUpSet = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficUpFields) {

      // transient alanlar atlanmalı
      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd());
        sbTxFieldsBlock.append(fiCol.getFcTxFieldName()).append(getTxComma());
        sbTxFieldsVar.append(" @").append(fiCol.getFcTxFieldName()).append(getTxComma());
      }else {
        sbTxUpSet.append(String.format("%s = @%s ", fiCol.getFcTxFieldName(), fiCol.getFcTxFieldName())).append(getTxComma());
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());
    FiString.rtrimSb(sbTxUpSet, getTxComma());

    FiKeybean fkbParams = new FiKeybean();
    fkbParams.addFim(FimOcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFim(FimOcSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFim(FimOcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFim(FimOcSql.sfTxFieldsVar(), sbTxFieldsVar.toString());
    fkbParams.addFim(FimOcSql.sfTxUpSet(), sbTxUpSet.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock==0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

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
