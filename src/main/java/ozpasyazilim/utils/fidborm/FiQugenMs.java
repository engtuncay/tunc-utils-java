package ozpasyazilim.utils.fidborm;

import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.metadata.fimCodegen.FimFcSpecFields;
import ozpasyazilim.utils.metadata.fimCodegen.FimFcSql;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;
import ozpasyazilim.utils.table.FicUtil;

public class FiQugenMs {


  /**
   * params: getFicListQuery
   * <p>
   * optional: getFkfAll (for table name)
   *
   * @param fiQuery
   * @return
   */
  public static Fdr delQueryV3(FiQuery fiQuery) {

    // Oluşturulan update sorgu formatı
    // DELETE EnmCariEvrakEk
    // WHERE ceveEvrakSeri = @ceveEvrakSeri AND ceveEvrakSira = @ceveEvrakSira AND ceveEvrakTip = @ceveEvrakTip

    Fdr fdrMain = new Fdr();

    Fkf fkfAllFields = fiQuery.getFkfAll();
    FicList ficListQuery = fiQuery.getFicListQuery();

    // FimFcSql.sfTableName();
    // FimFcSql.sfWhere();

    String template = "DELETE {{sfTableName}} \n"
        + " WHERE {{sfWhere}} ";

    StringBuilder sbWhereBlock = new StringBuilder();

    String txTableName = null;

    if (fkfAllFields != null) {
      txTableName = fkfAllFields.getSpecTxTableName();
    }

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficListQuery) {

      if (fiCol.isTableNameCol()) {
        txTableName = fiCol.getFcTxHeader();
      }

      if (fiCol.isTransient()) continue;

      if (fiCol.isWhereField()) {
        indexWhereBlock++;
        sbWhereBlock.append(FiQugenUtil.formSqlAssignAnd(fiCol.getFcTxFieldName()));
      }

    }

    FiString.rtrimSb(sbWhereBlock, FiQugenMsOld.getTxAnd());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfWhere(), sbWhereBlock.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexWhereBlock == 0) {
      sql = "no where fields";
      fdrMain.setFdTxVal(sql);
      fdrMain.setBoResult(false);
      return fdrMain;
    }

    if (FiString.isEmptyTrim(txTableName)) return fdrMain.buiBoResult(false, "Tablo ismi tanımlanmamış.");

    fdrMain.setBoResult(true);
    fdrMain.setFdTxVal(sql);
    return fdrMain;
  }

  /**
   * ficListQuery ile select alanları ve where alanlarını belirtilmesi gerekir
   * <p>
   * where alanları ayrıca ficList'e eklenmeli ve boWhereField true olarak işaretlenmeli
   *
   * @param fiQuery
   * @return
   */
  public static Fdr selQueryByFicList(FiQuery fiQuery) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    FicList ficListQuery = fiQuery.getFicListQuery();

    String txTableName = null;

    String template = "SELECT {{sfFields}}\n" +
        "FROM {{sfTableName}}\n";

    StringBuilder sbTxFields = new StringBuilder();
    StringBuilder sbTxWhere = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficListQuery) {

      // Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      if (fiCol.isTableNameCol()) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (fiCol.isTransient()) continue;

      if (fiCol.isWhereField() || fiCol.isWhereInField()) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        if (fiCol.isWhereInField()) {
          sbTxWhere.append(FiQugenUtil.formSqlAssignInByFic(fiCol));
          continue;
        }

        sbTxWhere.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));
        continue;
      }

      sbTxFields.append(FiQugenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));

    } // end for

    if (indexWhereBlock > 0) {
      template += "WHERE {{sfWhere}}";
      FiString.rtrimSb(sbTxWhere, FiQugenUtil.getTxAnd());
    }

    FiString.rtrimSb(sbTxFields, FiQugenUtil.getTxComma());

    Fkb fkbParams = new Fkb();

    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfFields(), sbTxFields.toString());
    fkbParams.addFieldBy(FimFcSql.sfWhere(), sbTxWhere.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();

    if (indexWhereBlock == 0) {
      Loghelper.get(FiQugenMs.getClassi()).debug("warn:no where fields for select query");
    }

    if (FiString.isEmptyTrim(txTableName)) {
      fdrResult.setFdTxVal("no table name");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no table name");
      return fdrResult;
    }

    //SELECT cha_cinsi
    //FROM CARI_HESAP_HAREKETLERI
    //WHERE cha_evrakno_seri = @cha_evrakno_seri AND cha_evrakno_sira = @cha_evrakno_sira AND cha_evrak_tip = @cha_evrak_tip

    fiQuery.setTxQuery(sql);
    fdrResult.setFdTxVal(sql);
    fdrResult.setBoResult(true);
    return fdrResult;
  }

  /**
   * ficListQuery ile select alanları ve where alanlarını belirtilmesi gerekir
   * <p>
   * where alanları ayrıca ficList'e eklenmeli ve boWhereField true olarak işaretlenmeli
   *
   * @param fiQuery
   * @return
   */
  public static Fdr selQueryByFicListV2(FiQuery fiQuery) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    FicList ficListQuery = fiQuery.getFicListQuery();

    String txTableName = null;

    String template = "SELECT {{sfFields}}\n" +
        "FROM {{sfTableName}}\n";

    if (fiQuery.getAbsFecTable() != null) {
      txTableName = fiQuery.getAbsFecTable().qcfTxSqTableName().getFcTxHeader();
    }

    StringBuilder sbTxFields = new StringBuilder();
    StringBuilder sbTxWhere = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficListQuery) {

      // Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      if (FiString.isEmptyTrim(txTableName) && fiCol.isTableNameCol()) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (fiCol.isTransient()) continue;

      if (fiCol.isWhereField() || fiCol.isWhereInField()) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        if (fiCol.isWhereInField()) {
          sbTxWhere.append(FiQugenUtil.formSqlAssignInByFic(fiCol));
          continue;
        }

        sbTxWhere.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));
        continue;
      }

      sbTxFields.append(FiQugenUtil.formSqlFieldComma(fiCol.getFcTxFieldName()));

    } // end for

    if (indexWhereBlock > 0) {
      template += "WHERE {{sfWhere}}";
      FiString.rtrimSb(sbTxWhere, FiQugenUtil.getTxAnd());
    }

    FiString.rtrimSb(sbTxFields, FiQugenUtil.getTxComma());

    Fkb fkbParams = new Fkb();

    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfFields(), sbTxFields.toString());
    fkbParams.addFieldBy(FimFcSql.sfWhere(), sbTxWhere.toString());

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();

    if (indexWhereBlock == 0) {
      Loghelper.get(FiQugenMs.getClassi()).debug("warn:no where fields for select query");
    }

    if (FiString.isEmptyTrim(txTableName)) {
      fdrResult.setFdTxVal("no table name");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no table name");
      return fdrResult;
    }

    //SELECT cha_cinsi
    //FROM CARI_HESAP_HAREKETLERI
    //WHERE cha_evrakno_seri = @cha_evrakno_seri AND cha_evrakno_sira = @cha_evrakno_sira AND cha_evrak_tip = @cha_evrak_tip

    fiQuery.setTxQuery(sql);
    fdrResult.setFdTxVal(sql);
    fdrResult.setBoResult(true);
    return fdrResult;
  }

  private static Class<FiQugenMs> getClassi() {
    return FiQugenMs.class;
  }

  /**
   * fkfAll kullanarak Insert Sorgusu üretir
   * <p>
   * Params : fkfAll
   *
   * @param fiQuery
   * @return fdTxVal sorgu sonucu
   */
  public static Fdr insQueryByFkfAll(FiQuery fiQuery) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    Fdr fdrMain = new Fdr();

    // arguments
    Fkf fkfAll = fiQuery.getFkfAll();

    //FimFcSql.sfTableName();
    //FimFcSql.sfFields();
    //FimFcSql.sfVarFields();

    String template = "INSERT INTO {{sfTableName}} ({{sfFields}})\n" +
        " VALUES ( {{sfVarFields}} )";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if (fkfAll != null) {
      //Loghelper.get(getClassi()).debug("tablo col mevcut");
      txTableName = fkfAll.getSpecTxTableName();
    }

    if (FiString.isEmpty(txTableName)) return fdrMain.buiBoResult(false, "Tablo ismi tanımlanmamış.");

    for (FiCol fiCol : fkfAll.values()) {

      if (FiBool.isTrue(fiCol.getFcBoTransient())) continue;
      if (FicUtil.isIdAutoType(fiCol)) continue;
      if (FicUtil.isDefField(fiCol)) continue;

      indexCol++;
      sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldCommaByFic(fiCol));
      sbTxFieldsVar.append(FiQugenUtil.formSqlVarCommaByFic(fiCol));

    }

    FiString.rtrimSb(sbTxFieldsBlock, FiQugenMsOld.getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, FiQugenMsOld.getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfFields(), sbTxFieldsBlock.toString());
    //fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimFcSql.sfVarFields(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    fdrMain.setFdTxVal(sql);

    if (indexCol == 0) {
      fdrMain.setBoResult(false);
      fdrMain.setFdTxVal("no insert fields");
      fdrMain.setFdTxMessage("Sorgu oluştururken hata oluştu. (no insert fields)");
      return fdrMain;
    }

    fdrMain.setBoResult(true);
    fiQuery.setTxQuery(fdrMain.getFdTxVal());
    return fdrMain;
  }

  /**
   * tabloda yoksa öncesinde insert yapar , daha sonra update sorgusu çalıştırır
   *
   * @param fiQuery
   * @return
   */
  public static Fdr insUpdateV1(FiQuery fiQuery) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    // arguments
    // IFiTableMeta iFiTableMeta = fiQuery.getiFiTableMeta();
    FicList ficListQuery = fiQuery.getFicListQuery();

    String txTableName = "";

    if (fiQuery.getFkfAll() != null) {
      txTableName = fiQuery.getFkfAll().getSpecTxTableName();
    }

    //  insert ederken id den başka alan yoksa böyle kayıt edilmeli
    //  INSERT INTO TabloAdı DEFAULT VALUES
    //  sfFieldsIns, sfFieldsVarIns sonradan parantez içine alınıyor

    // Sorguda kullanılan placeholderlar
    FimFcSql.sfTableName();
    FimFcSql.sfFieldsIns();
    // Variable Fields For Ins Query
    FimFcSql.sfVarFieldsIns();
    FimFcSql.sfWhere();
    // Variable Fields For Up Query
    FimFcSql.sfVarFieldsUp();

    String template = "--sq202604221135 v1\n" +
        "DECLARE @__count AS int = 0\n" +
        "\n" +
        "SELECT @__count = count(*) FROM {{sfTableName}}\n" +
        "WHERE {{sfWhere}}\n" +
        "\n" +
        "IF @__count = 0 \n" +
        "BEGIN\n" +
        "  INSERT INTO {{sfTableName}} ({{sfFieldsIns}}) \n" +
        "  VALUES ({{sfVarFieldsIns}}) \n" +
        "END;\n" +
        "\n" +
        "UPDATE {{sfTableName}}\n" +
        "SET {{sfVarFieldsUp}}\n" +
        "WHERE {{sfWhere}}\n";

    StringBuilder sbWhereBlock = new StringBuilder();
    // boş degilse parantez eklenecek
    StringBuilder sbFieldsIns = new StringBuilder();
    // for insert query
    StringBuilder sbFieldsVarIns = new StringBuilder();
    // for update query
    StringBuilder sbFieldsVarUp = new StringBuilder();

    int indexWhereBlock = 0;

    for (FiCol fiCol : ficListQuery) {

      //Loghelper.get(getClassi()).debug("fiCol: " + fiCol.getFcTxFieldName() + " - BoWhereField " + fiCol.getFcBoWhereField());

      // bazı özel alanlar atlanmalı
      if (FiString.isEmptyTrim(txTableName) && FimFcSpecFields.qcfTxSqTableName().getKey().equals(fiCol.getFcTxFieldName())) {
        txTableName = fiCol.getFcTxHeader();
        continue;
      }

      if (fiCol.isTransient()) continue;
      if (fiCol.isDefCols()) continue;

      if (FiBool.isTrue(fiCol.getFcBoWhereField())) {
        indexWhereBlock++;
        //Loghelper.get(FiSqlGenMs.class).debug("where field: " + fiCol.getFcTxFieldName());
        sbWhereBlock.append(FiQugenUtil.formSqlAssignAndByFic(fiCol));

        // identity alan inserte eklenemez
        if (fiCol.isIdAuto()) continue;

        continue;
      }

      // auto identity alan update ve insert'e eklenemez
      if (fiCol.isIdAuto()) continue;

      if (FiBool.isTrue(fiQuery.getBoUseInsFieldsForInsQuery())) {
        if (fiCol.isInsertField()) {
          sbFieldsIns.append(FiQugenUtil.formSqlFieldCommaByFic(fiCol));
          sbFieldsVarIns.append(FiQugenUtil.formSqlVarCommaByFic(fiCol));
        }
        // else durumunda insert'e alan eklenmez
      } else { // buUseInsFieldsOnly değilse direk eklenir
        sbFieldsIns.append(FiQugenUtil.formSqlFieldCommaByFic(fiCol));
        sbFieldsVarIns.append(FiQugenUtil.formSqlVarCommaByFic(fiCol));
      }

      if (fiCol.isIdKey()) {
        continue;
      }
      sbFieldsVarUp.append(FiQugenUtil.formSqlAssignVarAndCommaByFic(fiCol));

    }

    Loghelper.get(FiQugenMs.getClassi()).debug("tableName:" + txTableName);

    FiString.rtrimSb(sbWhereBlock, FiQugenMsOld.getTxAnd());
    FiString.rtrimSb(sbFieldsIns, FiQugenMsOld.getTxComma());
    FiString.rtrimSb(sbFieldsVarIns, FiQugenMsOld.getTxComma());
    FiString.rtrimSb(sbFieldsVarUp, FiQugenMsOld.getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFim(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFim(FimFcSql.sfWhere(), sbWhereBlock.toString());
    // Insert argümanları
    fkbParams.addFim(FimFcSql.sfFieldsIns(), sbFieldsIns.toString());
    fkbParams.addFim(FimFcSql.sfVarFieldsIns(), sbFieldsVarIns.toString());
    // Update argümanları
    fkbParams.addFim(FimFcSql.sfVarFieldsUp(), sbFieldsVarUp.toString());
    // where ortak kullanılır

    String sql = FiString.substitutor(template, fkbParams);

    Fdr fdrResult = new Fdr();
    fdrResult.setFdTxVal(sql);

    // update sorgusu olduğu için whereBlock olmak zorunda
    if (indexWhereBlock == 0) {
      fdrResult.setFdTxVal("no where fields");
      fdrResult.setBoResult(false);
      fdrResult.setFdTxMessage("no where fields");
      return fdrResult;
    }

    fiQuery.setTxQuery(fdrResult.getFdTxVal());
    fdrResult.setBoResult(true);
    return fdrResult;
  }

  public static Fdr selQuery(FicList ficList) {
    FiQuery fiQuery = new FiQuery();
    fiQuery.setFicListQuery(ficList);

    return selQueryByFicList(fiQuery);
  }

  public static Fdr selQueryByFicList(FicList ficList) {
    FiQuery fiQuconf = new FiQuery();
    fiQuconf.setFicListQuery(ficList);

    return selQueryByFicList(fiQuconf);
  }

  public static Fdr insQueryV3Custom(FiQuery fiQuery, FicList fclInsert) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    Fdr fdrMain = new Fdr();

    // arguments
    Fkf fkfAllFields = fiQuery.getFkfAll();

    //FimQcSql.sfTableName();

    String template = "INSERT INTO {{sfTableName}} ({{sfTxFields}})\n" +
        " VALUES ( {{sfTxFieldsVar}} )";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxFieldsVar = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if (fiQuery.getFkfAll() != null) {
      //Loghelper.get(getClassi()).debug("tablo col mevcut");
      txTableName = fiQuery.getFkfAll().getSpecTxTableName();
    }

    if (FiString.isEmpty(txTableName)) return fdrMain.buiBoResult(false, "Tablo ismi tanımlanmamış.");

    for (FiCol ficItem : fclInsert) {

      if (FiBool.isTrue(ficItem.getFcBoTransient())) continue;
      if (FicUtil.isIdAutoType(ficItem)) continue;
      if (FicUtil.isDefField(ficItem)) continue;

      indexCol++;
      sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldCommaByFic(ficItem));
      sbTxFieldsVar.append(FiQugenUtil.formSqlVarCommaByFic(ficItem));

    }

    FiString.rtrimSb(sbTxFieldsBlock, FiQugenMsOld.getTxComma());
    FiString.rtrimSb(sbTxFieldsVar, FiQugenMsOld.getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfTxFields(), sbTxFieldsBlock.toString());
    //fkbParams.addFieldBy(FimQcSql.sfTxWhere(), sbTxWhereBlock.toString());
    fkbParams.addFieldBy(FimFcSql.sfTxFieldsVar(), sbTxFieldsVar.toString());

    String sql = FiString.substitutor(template, fkbParams);

    fdrMain.setFdTxVal(sql);

    if (indexCol == 0) {
      fdrMain.setBoResult(false);
      fdrMain.setFdTxVal("no insert fields");
      fdrMain.setFdTxMessage("Sorgu oluştururken hata oluştu. (no insert fields)");
      return fdrMain;
    }

    fdrMain.setBoResult(true);
    return fdrMain;
  }

  public static Fdr insQueryV4ByFicList(FiQuery fiQuery) {
    // Loghelper.get(FiSqlGenMs.class).debug("upQuery called");

    Fdr fdrMain = new Fdr();

    // arguments
    // Fkf fkfAllFields = fiQuery.getFkfAll();

//    FimFcSql.sfTableName();
//    FimFcSql.sfFields();
//    FimFcSql.sfVarFieldsIns();

    String template = "INSERT INTO {{sfTableName}} ({{sfFields}})\n" +
        " VALUES ( {{sfVarFieldsIns}} )";

    StringBuilder sbTxFieldsBlock = new StringBuilder();
    StringBuilder sbTxVarFieldsIns = new StringBuilder();

    String txTableName = null;
    int indexCol = 0;

    if (fiQuery.getFkfAll() != null) {
      txTableName = fiQuery.getFkfAll().getSpecTxTableName();
    }

    for (FiCol ficItem : fiQuery.getFicListQueryInit()) {

      if (ficItem.isTableNameCol()) {
        txTableName = ficItem.getFcTxHeader();
        continue;
      }

      if (FiBool.isTrue(ficItem.getFcBoTransient())) continue;
      if (FicUtil.isIdAutoType(ficItem)) continue;
      if (FicUtil.isDefField(ficItem)) continue;

      indexCol++;
      sbTxFieldsBlock.append(FiQugenUtil.formSqlFieldCommaByFic(ficItem));
      sbTxVarFieldsIns.append(FiQugenUtil.formSqlVarCommaByFic(ficItem));

    }

    if (FiString.isEmpty(txTableName)) return fdrMain.buiBoResult(false, "Tablo ismi tanımlanmamış.");

    FiString.rtrimSb(sbTxFieldsBlock, FiQugenMsOld.getTxComma());
    FiString.rtrimSb(sbTxVarFieldsIns, FiQugenMsOld.getTxComma());

    Fkb fkbParams = new Fkb();
    fkbParams.addFieldBy(FimFcSql.sfTableName(), txTableName);
    fkbParams.addFieldBy(FimFcSql.sfFields(), sbTxFieldsBlock.toString());
    fkbParams.addFieldBy(FimFcSql.sfVarFieldsIns(), sbTxVarFieldsIns.toString());

    String sql = FiString.substitutor(template, fkbParams);

    if (indexCol == 0) {
      fdrMain.setBoResult(false);
      fdrMain.setFdTxVal("no insert fields");
      fdrMain.setFdTxMessage("Sorgu oluştururken hata oluştu. (no insert fields)");
      return fdrMain;
    }

    fdrMain.setFdTxVal(sql);
    fiQuery.setTxQuery(sql);
    fdrMain.setBoResult(true);
    return fdrMain;
  }
}

