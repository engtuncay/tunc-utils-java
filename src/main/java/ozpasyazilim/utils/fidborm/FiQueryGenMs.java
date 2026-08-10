package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.Fkb;
//import ozpasyazilim.utils.ficRfcCoding;
import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metadata.fimCodegen.FimFtSpecFields;
import ozpasyazilim.utils.metadata.fimCodegen.FimFtSql;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import javax.annotation.Nonnull;

/**
 * FiSql Query Generation Class for Ms-sql
 */
public class FiQueryGenMs {

  /**
   * Update Query Generation
   * <p>
   * FiSqlGenConfig Fields Used : iFiTableMeta, ficUpFields, ficWhereFields
   *
   * @param fiQuconf
   * @return
   */
  public static String upQueryV2(FiQuconf fiQuconf) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQuconf.getiFiTableMeta();
    FicList ficFields = fiQuconf.getFicListTable();

    // FimOcSql.sfTableName();
    // FimOcSql.sfTxWhere();
    // FimQcSql.sfTxUpSetBlock();

    String template = "UPDATE {{sfTableName}} SET {{sfTxUpSetBlock}} \n"
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
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));
      } else {
        sbTxSetBlock.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxSetBlock, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimFtSql.sfTxUpSetBlock(), sbTxSetBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock == 0) sql = "no where fields";

    //UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    return sql;
  }

  /**
   * Update Query Generation
   * <p>
   * FiSqlGenConfig Fields Used : iFiTableMeta, ficUpFields, ficWhereFields
   *
   * @param fiQuconf
   * @return
   */
  public static Fdr upQueryV3(FiQuconf fiQuconf) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");
    Fdr fdr = new Fdr();

    // arguments
    FicList ficFields = fiQuconf.getFicListTable();
    Fkfic fkbDataDef = fiQuconf.getFkficDataDef();

    // FimOcSql.sfTableName();
    // FimOcSql.sfTxWhere();
    // FimQcSql.sfTxFieldsVarUp();
    // FimQcSql.sfTxUpSetBlock();

    String template = "UPDATE {{sfTableName}} SET {{sfTxUpSetBlock}} \n"
        + " WHERE {{sfTxWhere}} ";

    StringBuilder sbTxUpSetBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();

    String txTableName = null;
    int indexWhereBlock = 0;

    if (fkbDataDef != null) {
      txTableName = fkbDataDef.getFimHeaderValNtn(FimFtSpecFields.qcfTxSqTableName());
    }

    for (FiCol fiCol : ficFields) {

      // if (FiBool.isTrue(fiCol.getBoKeyIdField())) {
      // if (FiBool.isTrue(boUpdateFieldsOnly)) {
      // if (FiBool.isTrue(fiCol.getBoUpdateFieldForQuery())) {

      if (txTableName == null && fiCol.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        //fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd()
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
      } else {
        sbTxUpSetBlock.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxUpSetBlock, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFtSql.sfTxUpSetBlock(), sbTxUpSetBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock == 0 || txTableName == null) {
      sql = "no where fields or tablename";
      fdr.setFdTxValue(sql);
      fdr.setBoResult(false);
      return fdr;
    }

    // UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip
    fdr.setBoResult(true);
    fdr.setFdTxValue(sql);

    return fdr;
  }

  public static Fdr upQueryV1(FiCol qcfTxSqTableName, FicList ficUpFields, FicList ficWhereFields) {
    return upQueryV1(FiQuconf.buiUpV1(qcfTxSqTableName, ficUpFields, ficWhereFields));
  }

  public static Fdr upQueryV1(FiQuconf fiQuconf) {

    FiCol qcfTxSqTableName = fiQuconf.getQcfTxSqTableName();
    FicList ficUpFields  = fiQuconf.getFicListUp();
    FicList ficWhereFields =  fiQuconf.getFicListWhere();

    // Oluşturulan update sorgu formatı
    // UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");
    Fdr fdr = new Fdr();

    // FimOcSql.sfTableName();
    // FimOcSql.sfTxWhere();
    // FimQcSql.sfTxUpSetBlock();

    String template = "UPDATE {{sfTableName}} SET {{sfTxUpSetBlock}} \n"
        + " WHERE {{sfTxWhere}} ";

    StringBuilder sbUpSetBlock = new StringBuilder();
    StringBuilder sbWhereBlock = new StringBuilder();

    String txTableName = qcfTxSqTableName.getFcTxHeader();
    int indexWhereBlock = 0;

    for (FiCol fiCol : ficUpFields) {
      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }
      sbUpSetBlock.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));
    }

    for (FiCol ficWhereField : ficWhereFields) {
      if (FiBool.isTrue(ficWhereField.getFcBoTransient())) {
        continue;
      }
      indexWhereBlock++;
      sbWhereBlock.append(FiQugenUtil.formSqlAssignAnd(ficWhereField.getFcTxFieldName()));
    }

    FiString.rtrimSb(sbWhereBlock, getTxAnd());
    FiString.rtrimSb(sbUpSetBlock, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFtSql.sfTxUpSetBlock(), sbUpSetBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxWhere(), sbWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock == 0 || FiString.isEmptyTrim(txTableName)) {
      sql = "no where fields or tablename";
      fdr.setFdTxValue(sql);
      fdr.setBoResult(false);
      return fdr;
    }

    fdr.setBoResult(true);
    fdr.setFdTxValue(sql);

    return fdr;
  }

  /**
   * IFiTableMeta ve FicList'ten Select Query Generation
   *
   * @param fiQuconf
   * @return
   */
  public static Fdr selQuery(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQuconf.getiFiTableMeta();
    FicList ficList = fiQuconf.getFicListTable();

    String txTableName = null;

    if (iFiTableMeta != null) {
      txTableName = iFiTableMeta.getITxTableName();
    }

    String template = "SELECT {{sfTxFields}}\n" +
        "FROM {{sfTableName}}\n"
        + "WHERE {{sfTxWhere}}";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxWhereBlock = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficList) {

      // Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      if (fiCol.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
      } else {
        sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, FiQugenUtil.getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, FiQugenUtil.getTxComma());

    Fkb fkbParams = new Fkb();

    fkbParams.addFieldBy(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFtSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock == 0) {
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

  private static Class<FiQueryGenMs> getClassi() {
    return FiQueryGenMs.class;
  }

  public static Fdr insIfNot(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQuconf.getiFiTableMeta();
    FicList ficUpFields = fiQuconf.getFicListTable();

    //FimOcgSql.sfTableName();
    //FimOcgSql.sfTxWhere();
    //FimOcgSql.sfTxFields();
    //FimOcgSql.sfTxFieldsVar();

    String template = "DECLARE @__count AS int = 0\n" +
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
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
        sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));
        sbTxFieldsVar.append(FiQugenUtil.formSqlVarComma(fiCol.getFcTxFieldName()));
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimFtSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock == 0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  /**
   * Where şartına göre tabloda Kayıt Sayısına Bakar (cand_id olmalı)
   * <p>
   * Yoksa, Insert Yapar : Where içine giren alanlarla insert eder, tek id varsa boş satır ekler
   * <p>
   * Varsa, Güncelleme sorgusu çalışır: Where içine girmeyen diğer alanları günceller
   *
   * @param fiQuconf
   * @return
   */
  public static Fdr insUpdateByCandId(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQuconf.getiFiTableMeta();
    FicList ficList = fiQuconf.getFicListTable();

    String txTableName = null;

    if (iFiTableMeta != null) {
      txTableName = iFiTableMeta.getITxTableName();
    }

    // FimQcSql.sfTableName();

    //  insert ederken id den başka alan yoksa böyle kayıt edilmeli
    //  INSERT INTO TabloAdı DEFAULT VALUES
    // sfTxFieldsIns, sfTxFieldsVarIns sonradan parantez içine alınıyor
    String template = "--sq202604221135 v1\n" +
        "DECLARE @__count AS int = 0\n" +
        "\n" +
        "SELECT @__count = count(*) FROM {{sfTableName}}\n" +
        "WHERE {{sfTxWhere}}\n" +
        "\n" +
        "IF @__count = 0 \n" +
        "BEGIN\n" +
        "  INSERT INTO {{sfTableName}} ({{sfTxFieldsIns}}) \n" +
        "  VALUES ({{sfTxFieldsVarIns}}) \n" +
        "END;\n" +
        "\n" +
        "UPDATE {{sfTableName}}\n" +
        "SET {{sfTxFieldsVarUp}}\n" +
        "WHERE {{sfTxWhere}}\n";


    StringBuilder sbTxWhereBlock = new StringBuilder();
    // boş degilse parantez eklenecek
    StringBuilder sbTxFieldsIns = new StringBuilder();
    StringBuilder sbTxFieldsVarIns = new StringBuilder();

    StringBuilder sbTxFieldsVarUp = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficList) {

      Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      if (fiCol.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      // transient alanlar atlanmalı
      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }
      // bazı özel alanlar atlanmalı
//      if (fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
//        txTableName = fiCol.getFcTxHeader();
//        continue;
//      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));

        // MEDFIX auto fimE eklenmeli
        // identity alan inserte eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsIns.append(FiQugenUtil.formSqlFieldCommaByFic(fiCol));
        sbTxFieldsVarIns.append(FiQugenUtil.formSqlVarCommaByFic(fiCol));
      } else {
        // auto identity alan update'e eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsVarUp.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarUp, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFim(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFim(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());
    // Insert argümanları
    fkbParams.addFim(FimFtSql.sfTxFieldsIns(), sbTxFieldsIns.toString());
    fkbParams.addFim(FimFtSql.sfTxFieldsVarIns(), sbTxFieldsVarIns.toString());
    // Update argümanları
    fkbParams.addFim(FimFtSql.sfTxFieldsVarUp(), sbTxFieldsVarUp.toString());
    // where ortak kullanılır

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock == 0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  /**
   *
   * Draft !!!
   * <p>
   * Where şartına göre tabloda Kayıt Sayısına Bakar (cand_id olmalı)
   * <p>
   * Yoksa, Insert Yapar : Where içine giren alanlarla insert eder, tek id varsa boş satır ekler
   * <p>
   * Varsa, Güncelleme sorgusu çalışır: Where içine girmeyen diğer alanları günceller
   *
   * @param fiQuconf
   * @return
   */
  public static Fdr insUpdateByIdIdentity(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fiQuconf.getiFiTableMeta();
    FicList ficList = fiQuconf.getFicListTable();

    String txTableName = null;

    if (iFiTableMeta != null) {
      txTableName = iFiTableMeta.getITxTableName();
    }

    // FimQcSql.sfTableName();

    //  insert ederken id den başka alan yoksa böyle kayıt edilmeli
    //  INSERT INTO TabloAdı DEFAULT VALUES
    // sfTxFieldsIns, sfTxFieldsVarIns sonradan parantez içine alınıyor
    String template = "--sq202604221140 v1\n" +
        "DECLARE @__count AS int = 0\n" +
        "\n" +
        "SELECT @__count = count(*) FROM {{sfTableName}}\n" +
        "WHERE {{sfTxWhere}}\n" +
        "\n" +
        "IF @__count = 0 \n" +
        "BEGIN\n" +
        "  INSERT INTO {{sfTableName}} ({{sfTxFieldsIns}}) \n" +
        "  VALUES ({{sfTxFieldsVarIns}}) \n" +
        "END;\n" +
        "\n" +
        "UPDATE {{sfTableName}}\n" +
        "SET {{sfTxFieldsVarUp}}\n" +
        "WHERE {{sfTxWhere}}\n";


    StringBuilder sbTxWhereBlock = new StringBuilder();
    // boş degilse parantez eklenecek
    StringBuilder sbTxFieldsIns = new StringBuilder();
    StringBuilder sbTxFieldsVarIns = new StringBuilder();

    StringBuilder sbTxFieldsVarUp = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficList) {

      Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      if (fiCol.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      // transient alanlar atlanmalı
      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }
      // bazı özel alanlar atlanmalı
//      if (fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
//        txTableName = fiCol.getFcTxHeader();
//        continue;
//      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));

        // MEDFIX auto fimE eklenmeli
        // identity alan inserte eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsIns.append(FiQugenUtil.formSqlFieldCommaByFic(fiCol));
        sbTxFieldsVarIns.append(FiQugenUtil.formSqlVarCommaByFic(fiCol));
      } else {
        // auto identity alan update'e eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsVarUp.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarUp, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFim(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFim(FimFtSql.sfTxWhere(), sbTxWhereBlock.toString());
    // Insert argümanları
    fkbParams.addFim(FimFtSql.sfTxFieldsIns(), sbTxFieldsIns.toString());
    fkbParams.addFim(FimFtSql.sfTxFieldsVarIns(), sbTxFieldsVarIns.toString());
    // Update argümanları
    fkbParams.addFim(FimFtSql.sfTxFieldsVarUp(), sbTxFieldsVarUp.toString());
    // where ortak kullanılır

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexWhereBlock == 0) {
      fdrResult.setFdTxValue("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  /**
   *
   * sql başarılı olursa fdTxValue kaydedilir
   *
   * @param fiQuconf
   * @return
   */
  public static Fdr insert(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    FicList ficInsFields = fiQuconf.getFicListTable();
    Fkfic fkbDataDef = fiQuconf.getFkficDataDef();

    //FimQcSql.sfTableName();

    String template = "INSERT INTO {{sfTableName}} ({{sfTxFields}})\n" +
        "  VALUES ({{sfTxFieldsVar}})";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if (fkbDataDef != null) {
      txTableName = fkbDataDef.getFimHeaderValNtn(FimFtSpecFields.qcfTxSqTableName());
    }

    for (FiCol ficItem : ficInsFields) {

      if (txTableName == null && ficItem.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = ficItem.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(ficItem.getFcBoTransient())) {
        continue;
      }

      // URFIX (user-assign tipi olursa insert'e eklenmeli)
      if (!FiString.isEmpty(ficItem.getFcTxIdType())) {
        continue;
      }

      indexCol++;
      sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldCommaByFic(ficItem));
      sbTxFieldsVar.append(FiQugenUtil.formSqlVarCommaByFic(ficItem));

    }

    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFtSql.sfTxFields(), sbTxFieldsBlock.toString());
    //fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexCol == 0) {
      fdrResult.setBoResult(false);
      fdrResult.setFdTxValue("no insert fields");
      fdrResult.setFdTxMessage("no insert fields");
      return fdrResult;
    }

    fdrResult.setBoResult(true);
    return fdrResult;
  }

  public static Fdr insertV2(FiQuconf fiQuconf) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    Fkfic fkficAllFields = fiQuconf.getFkficAll();
    Fkfic fkbDataDefs = fiQuconf.getFkficDataDef();

    //FimQcSql.sfTableName();

    String template = "INSERT INTO {{sfTableName}} ({{sfTxFields}})\n" +
        "  VALUES ({{sfTxFieldsVar}})";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if (fkbDataDefs != null) {
      txTableName = fkbDataDefs.getFimHeaderValNtn(FimFtSpecFields.qcfTxSqTableName());
    }

    for (FiCol ficItem : fkficAllFields.values()) {

      if (txTableName == null && ficItem.getFcTxFieldName().equals(FimFtSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = ficItem.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(ficItem.getFcBoTransient())) {
        continue;
      }

      // URFIX (user-assign tipi olursa insert'e eklenmeli)
      if (!FiString.isEmpty(ficItem.getFcTxIdType())) {
        continue;
      }

      indexCol++;
      sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldCommaByFic(ficItem));
      sbTxFieldsVar.append(FiQugenUtil.formSqlVarCommaByFic(ficItem));

    }

    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFtSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFtSql.sfTxFields(), sbTxFieldsBlock.toString());
    //fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimFtSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxValue(sql);

    if (indexCol == 0) {
      fdrResult.setBoResult(false);
      fdrResult.setFdTxValue("no insert fields");
      fdrResult.setFdTxMessage("no insert fields");
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

  public static Fdr selQuery(FicList ficList) {
    FiQuconf fiQuconf = new FiQuconf();
    fiQuconf.setFicListTable(ficList);

    return selQuery(fiQuconf);
  }

}
