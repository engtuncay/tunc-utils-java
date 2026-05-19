package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.Fkb;
//import ozpasyazilim.utils.ficRfcCoding;
import ozpasyazilim.utils.datatypes.Fkfic;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metadata.fimCodegen.FimQcSpecFields;
import ozpasyazilim.utils.metadata.fimCodegen.FimQcSql;
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
   * @param fqc
   * @return
   */
  public static String upQuery(Fqc fqc) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fqc.getiFiTableMeta();
    FicList ficFields = fqc.getFicList();

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
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAndByFic(fiCol));
      } else {
        sbTxSetBlock.append(FiQuGenUtil.formSqlAssignCommaByFic(fiCol));
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxSetBlock, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimQcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimQcSql.sfTxUpSetBlock(), sbTxSetBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());

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
   * @param fqc
   * @return
   */
  public static Fdr upQuery2(Fqc fqc) {

    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");
    Fdr fdr = new Fdr();

    // arguments
    FicList ficFields = fqc.getFicList();
    Fkfic fkbDataDef = fqc.getFkbDataDef();

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

    if(fkbDataDef != null) {
      txTableName = fkbDataDef.getFimHeaderValNtn(FimQcSpecFields.qcfTxSqTableName());
    }

    for (FiCol fiCol : ficFields) {

      // if (FiBool.isTrue(fiCol.getBoKeyIdField())) {
      // if (FiBool.isTrue(boUpdateFieldsOnly)) {
      // if (FiBool.isTrue(fiCol.getBoUpdateFieldForQuery())) {

      if (txTableName==null && fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        //fiCol.getFcTxFieldName()).append(" = @").append(fiCol.getFcTxFieldName()).append(getTxAnd()
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
      } else {
        sbTxUpSetBlock.append(FiQuGenUtil.formSqlAssignCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxUpSetBlock, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimQcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimQcSql.sfTxUpSetBlock(), sbTxUpSetBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock == 0 || txTableName == null) {
      sql = "no where fields or tablename";
      fdr.setFdTxValue(sql);
      fdr.setBoResult(false);
      return fdr;
    }

    //UPDATE EnmCariEvrakEk SET ceveLnNormalFatura = @ceveLnNormalFatura
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip
    fdr.setBoResult(true);
    fdr.setFdTxValue(sql);

    return fdr;
  }

  /**
   * IFiTableMeta ve FicList'ten Select Query Generation
   *
   * @param fqc
   * @return
   */
  public static Fdr selQuery(Fqc fqc) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fqc.getiFiTableMeta();
    FicList ficList = fqc.getFicList();

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

      if (fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoTransient())) {
        continue;
      }

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
      } else {
        sbTxFieldsBlock.append(FiQuGenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, FiQuGenUtil.getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, FiQuGenUtil.getTxComma());

    Fkb fkbParams = new Fkb();

    fkbParams.addFieldBy(FimQcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimQcSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());

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

  public static Fdr insIfNot(Fqc fqc) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fqc.getiFiTableMeta();
    FicList ficUpFields = fqc.getFicList();

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
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
        sbTxFieldsBlock.append(FiQuGenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));
        sbTxFieldsVar.append(FiQuGenUtil.formSqlVarComma(fiCol.getFcTxFieldName()));
      }
    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimQcSql.sfTableName(), iFiTableMeta.getITxTableName());
    fkbParams.addFieldBy(FimQcSql.sfTxFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

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
   * @param fqc
   * @return
   */
  public static Fdr insUpdateByCandId(Fqc fqc) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fqc.getiFiTableMeta();
    FicList ficList = fqc.getFicList();

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

      if (fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
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
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAndByFic(fiCol));

        // MEDFIX auto fimE eklenmeli
        // identity alan inserte eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsIns.append(FiQuGenUtil.formSqlFieldCommaByFic(fiCol));
        sbTxFieldsVarIns.append(FiQuGenUtil.formSqlVarCommaByFic(fiCol));
      } else {
        // auto identity alan update'e eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsVarUp.append(FiQuGenUtil.formSqlAssignCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarUp, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFim(FimQcSql.sfTableName(), txTableName);
    fkbParams.addFim(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    // Insert argümanları
    fkbParams.addFim(FimQcSql.sfTxFieldsIns(), sbTxFieldsIns.toString());
    fkbParams.addFim(FimQcSql.sfTxFieldsVarIns(), sbTxFieldsVarIns.toString());
    // Update argümanları
    fkbParams.addFim(FimQcSql.sfTxFieldsVarUp(), sbTxFieldsVarUp.toString());
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
   * @param fqc
   * @return
   */
  public static Fdr insUpdateByIdIdentity(Fqc fqc) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    IFiTableMeta iFiTableMeta = fqc.getiFiTableMeta();
    FicList ficList = fqc.getFicList();

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

      if (fiCol.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
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
        sbTxWhereBlock.append(FiQuGenUtil.formSqlAssignAndByFic(fiCol));

        // MEDFIX auto fimE eklenmeli
        // identity alan inserte eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsIns.append(FiQuGenUtil.formSqlFieldCommaByFic(fiCol));
        sbTxFieldsVarIns.append(FiQuGenUtil.formSqlVarCommaByFic(fiCol));
      } else {
        // auto identity alan update'e eklenemez
        if (fiCol.getFcTxIdTypeNtn().equals("auto")) {
          continue;
        }
        sbTxFieldsVarUp.append(FiQuGenUtil.formSqlAssignCommaByFic(fiCol));
      }

    }

    FiString.rtrimSb(sbTxWhereBlock, getTxAnd());
    FiString.rtrimSb(sbTxFieldsIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarIns, getTxComma());
    FiString.rtrimSb(sbTxFieldsVarUp, getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFim(FimQcSql.sfTableName(), txTableName);
    fkbParams.addFim(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    // Insert argümanları
    fkbParams.addFim(FimQcSql.sfTxFieldsIns(), sbTxFieldsIns.toString());
    fkbParams.addFim(FimQcSql.sfTxFieldsVarIns(), sbTxFieldsVarIns.toString());
    // Update argümanları
    fkbParams.addFim(FimQcSql.sfTxFieldsVarUp(), sbTxFieldsVarUp.toString());
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

  public static Fdr insert(Fqc fqc) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    FicList ficInsFields = fqc.getFicList();
    Fkfic fkbDataDef = fqc.getFkbDataDef();

    //FimQcSql.sfTableName();

    String template = "INSERT INTO {{sfTableName}} ({{sfTxFields}})\n" +
        "  VALUES ({{sfTxFieldsVar}})";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if(fkbDataDef != null) {
      txTableName = fkbDataDef.getFimHeaderValNtn(FimQcSpecFields.qcfTxSqTableName());
    }

    for (FiCol ficItem : ficInsFields) {

      if (txTableName==null && ficItem.getFcTxFieldName().equals(FimQcSpecFields.qcfTxSqTableName().getKey())) {
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
      sbTxFieldsBlock.append(FiQuGenUtil.formSqlFieldCommaByFic(ficItem));
      sbTxFieldsVar.append(FiQuGenUtil.formSqlVarCommaByFic(ficItem));

    }

    FiString.rtrimSb(sbTxFieldsBlock, getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, getTxComma());



    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimQcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimQcSql.sfTxFields(), sbTxFieldsBlock.toString());
    //fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimQcSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

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
    Fqc fqc = new Fqc();
    fqc.setFicList(ficList);

    return selQuery(fqc);
  }

}
