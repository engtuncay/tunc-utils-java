package ozpasyazilim.utils.fidborm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.datatypes.Fkf;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FicList;

import java.util.*;

/**
 * Syntax
 * <p>
 * Jdbi Param ":paramName"
 * <p>
 * Sql (At) Param "@paramName"
 * <p>
 * Opt Param "--!paramName"
 */
public class FiQuery {

  String txQuery;
  Fkb fkbParams;
  String txCandIdFieldName;
  String txPrimaryKeyFieldName;

  /**
   * class of return object
   */
  Class retClass;

  // Query Oluşturmak için eklenen alanlar
  FicList ficListQuery;

  // FicQuery
  Fkf fkfAll;

  /**
   * Tablo ismi buranın header alanından alınır
   */
  FiCol qcfTxSqTableName;

  //List<FiField> queryFieldList;
  //List<FiField> queryWhereList;

  public FiQuery() {
  }

  public FiQuery(String sql) {
    this.txQuery = sql;
  }

  public FiQuery(String sql, Fkb fkbParams) {
    this.txQuery = sql;

    if (fkbParams != null) {
      this.fkbParams = new Fkb(fkbParams);
    }

  }

  public FiQuery(FicList ficList) {
    this.ficListQuery = ficList;
  }

  public FiQuery(Fkf fkfTable) {
    this.fkfAll = fkfTable;
  }

  public FiQuery(Fkb fkbParams) {
    if(fkbParams!=null) {
      this.fkbParams = new Fkb(fkbParams);
    }
  }

  public static FiQuery bui() {
    return new FiQuery();
  }

  public static FiQuery bui(String sql) {
    return new FiQuery(sql);
  }

  public static FiQuery bui(String sql, Fkb fkbParams) {
    return new FiQuery(sql, fkbParams);
  }


  public void deActivateOptParam(String txOptParamName) {
    setTxQuery(FiQueryUtils.deActivateOptParamMain(getTxQuery(), txOptParamName));
  }

  public void deActivateSqlAtParam(String param) {
    setTxQuery(FiQueryUtils.deactivateSqlAtParamMain(getTxQuery(), param));
  }

  public void activateOptParam(String txOptParamName) {
    setTxQuery(FiQueryUtils.activateOptParamMain(getTxQuery(), txOptParamName));
  }

  public void activateSqlAtParam(String fieldName) {
    setTxQuery(FiQueryUtils.activateSqlAtParamMain(getTxQuery(), fieldName));
  }

  /**
   * Collection (List,Set) Türündeki parametreleri multi param (abc_1,abc_2... gibi) çevirir
   */
  public void convertListParamsToMultiParams() {
    if (getFkbParams() == null) return;

    setTxQuery(FiQueryUtils.convertListParamsToMultiParams(getTxQuery(), getFkbParams(), false));
  }


  /**
   * listParametresini parametre listesinde tutar, listeden çıkarmaz.
   */
  public void convertListParamsToMultiParamsWithKeep() {
    if (getFkbParams() == null) return;

    setTxQuery(FiQueryUtils.convertListParamsToMultiParams(getTxQuery(), getFkbParams(), true));
  }

  /**
   * Not : list parametresini listeden çıkarır.
   *
   * @param mapBind
   */
  public void convertListParamsToMultiParams(Fkb mapBind) {
    setFkbParams(mapBind);
    convertListParamsToMultiParams();
  }

  /**
   * Parametreyi multi parametreye çevirir, birleştirme olarak new line kullanır. Parametreyi mapParam'dan çıkarır. multiler kalır.
   *
   * @param txParamName
   * @param collParams
   */
  public void convertParamToMultiParamsWithSqlNewLine(String txParamName, Collection collParams) {
    if (getFkbParams() == null) return;
    String txCombineSeperator = "+char(13)+char(10)+";
    setTxQuery(FiQueryUtils.convertSingleParamToMultiParam2(getTxQuery(), getFkbParams(), txParamName, collParams, false, txCombineSeperator));
  }

  public Fkb getFkbParams() {
    return fkbParams;
  }

  public Fkb getMapParamsInit() {
    if (fkbParams == null) {
      fkbParams = new Fkb();
    }
    return fkbParams;
  }

  public FiQuery setFkbParams(Fkb fkbParams) {
    this.fkbParams = fkbParams;
    return this;
  }

  public void activateFiSqlFieldIfExists(String txFieldName) {
    if (getFkbParams() != null && getFkbParams().containsKey(txFieldName)) {
      activateOptParam(txFieldName);
    }
  }

  /**
   * FiMapde olan parametreleri aktif eder
   * <p>
   * Sorguda yoruma alınmamış(!) satır aynı şekilde kalır.
   * <p>
   * DeAktivite yapılmaz. (!!!)
   */
  public void activateParamsByMapParams() {
    activateParamsMain(false);
  }

  /**
   * FiMapde olan parametreleri aktif eder
   * <p>
   * Sorguda yoruma alınmamış(!) satır aynı şekilde kalır. DeAktivite yapılmaz.
   */
  public void activateParams() {
    activateParamsMain(false);
  }

  /**
   * (1) activateParamsMain(boActiOnlyFullParams);
   * <p>
   * (2) deActivateAllOptParams();
   */
  public void processAllParams(Boolean boActiOnlyFullParams) {
    activateParamsMain(boActiOnlyFullParams);
    deActivateAllOptParams();
  }

  /**
   * activateParamsMain(boActiOnlyFullParams);
   * <p>
   * convertListParamsToMultiParams();
   * <p>
   * deActivateAllOptParams();
   */
  public void processAllParamsWitMulti(Boolean boActiOnlyFullParams) {
    activateParamsMain(boActiOnlyFullParams);
    convertListParamsToMultiParams();
    deActivateAllOptParams();
  }

  public void activateParamsOnlyFull() {
    activateParamsMain(true);
  }

  private Set<String> getParamOptionalsFromQuery() {
    return FiQueryUtils.findParamsOptional(getTxQuery());
  }

  /**
   * "--!" optional işareti olan satırların alt satırını kapatır, deactivated edildiği belirtir. (alt satır yoruma alınmasa bile deaktif olur)
   * <p>
   * --! satırlar optional satırdır.
   * <p>
   * Optional Format --!paramName .....
   *
   * @return
   */
  public void deActivateAllOptParams() {
    setTxQuery(FiQueryUtils.deActivateAllOptParams(getTxQuery()));
  }

  /**
   * FiMapParam'da olan parametreleri aktive eder.
   * <p>
   * boActivateOnlyFullParams true olursa sadece dolu olan parametreleri aktif eder, parametre dolu değilse (null dahil) deaktif eder.
   * <p>
   * Deaktif edilecek parametrelerde FiKeyBean'de bulunmalı
   * <p>
   * Dolu olma Şartları : String boş string degilse, Collection larda size > 0 olmalı, Diger türler için null olmamalı
   */
  public void activateParamsMain(Boolean boActivateOnlyFullParams) {
    if (getFkbParams() != null) {
      setTxQuery(FiQueryUtils.activateParamsMain(getTxQuery(), getFkbParams(), boActivateOnlyFullParams));
    }
  }

  public void deActivateOptParamsNotUsed() {
    setTxQuery(FiQueryUtils.deActivateOptParamsNotUsed(getTxQuery(), getMapParamsInit()));
  }


  public void logQuery() {
    Loghelper.get(getClass()).debug("Fiquery log-Query");
    Loghelper.get(getClass()).debug(getTxQuery());
  }

  public void logQueryAndParams() {
    Loghelper.get(getClass()).debug("Fiquery log- Params");
    Loghelper.get(getClass()).debug(FiConsole.textFkb(getMapParamsInit()));
    // Query
    Loghelper.get(getClass()).debug("Fiquery log - Query");
    Loghelper.get(getClass()).debug(getTxQuery());

  }

  // Getter and Setter

  public String getTxQuery() {
    return txQuery;
  }

  public FiQuery setTxQuery(String txQueryPrm) {
    this.txQuery = txQueryPrm;
    return this;
  }

  public String getTxCandIdFieldName() {
    return txCandIdFieldName;
  }

  public void setTxCandIdFieldName(String txCandIdFieldName) {
    this.txCandIdFieldName = txCandIdFieldName;
  }

  public String getTxPrimaryKeyFieldName() {
    return txPrimaryKeyFieldName;
  }

  public void setTxPrimaryKeyFieldName(String txPrimaryKeyFieldName) {
    this.txPrimaryKeyFieldName = txPrimaryKeyFieldName;
  }

  public void addParam(Object key, Object value) {
    if (key == null) return;
    getMapParamsInit().add(key.toString(), value);
  }

  /**
   * value empty ise eklenmez !!!
   * <p>
   * Map Params eklerken değeri '%' içerisine alır ( % value % )
   *
   * @param key
   * @param value
   */
  public void addParamLike(Object key, String value) {
    if (key == null) return;

    if (FiString.isEmpty(value)) {
      getMapParamsInit().add(key.toString(), value);
      return;
    }

    getMapParamsInit().add(key.toString(), "%" + value + "%");
  }

  // *** Fiquery Tools taşınabilecek metodlar

  /**
   * Sql sorgu içindeki parametreyi, parametre_index şeklinde multi parametreye çevirir.
   * <p>
   * param_1 param_2 gibi
   */
  private void convertSqlParamToMultiParam(String param, Integer count) {

    String sql = getTxQuery();

    //sadece parentez içinde olanları bulmak istenirse
    //final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
    final String regex = String.format("@%s", param);

    StringBuilder customParam = new StringBuilder();

    Integer indexParam = FiQueryUtils.getMultiParamStartIndex();
    for (int countPrm = 0; countPrm < count; countPrm++) {
      if (countPrm != 0) customParam.append(",");
      String sablon = FiQueryUtils.makeMultiParamTemplate(param, indexParam);
      customParam.append("@" + sablon);
      indexParam++;
    }

    String sqlNew = sql.replaceAll(regex, String.format("%s", customParam.toString())); //(%s)
    setTxQuery(sqlNew);
  }

  public void activateFiSqlFieldIfNotEmpty(String txFieldName) {
    if (getFkbParams() != null) {
      Object value = getFkbParams().getOrDefault(txFieldName, null);

      if (value instanceof String) {
        if (!FiString.isEmpty((String) value)) {
          activateOptParam(txFieldName);
        }
      } else { // string tipinden dışında olanlar
        if (value != null) {
          activateOptParam(txFieldName);
        }
      }
    }
  }

  /**
   * FiMapParam'da null olmayan parametreleri aktive eder, null olanları deAktif eder.
   * <p>
   * Deaktif edilmek istenen parametreler null olarak gönderilir !!!
   * <p>
   * Not: Sql içinde olup, FiMapParams de olmayan parametreler etkilenmez
   */
  public void activateParamsNotNull() {

    if (getFkbParams() != null) {

      List<String> listParamsWillDeactivate = new ArrayList<>();

      getFkbParams().forEach((key, value) -> {
        // Null olanlar deaktif olacak
        if (value != null) { // null degilse aktif edilir.
          String newQuery = FiQueryUtils.activateOptParamMain(getTxQuery(), key);
          setTxQuery(newQuery);
        } else { // param null ise,deaktif edilir
          String newQuery = FiQueryUtils.deActivateOptParamMain(getTxQuery(), key);
          setTxQuery(newQuery);
          listParamsWillDeactivate.add(key);
        }
      });

      // deAktif edilen parametreler çıkarıldı.
      for (String deActivatedParam : listParamsWillDeactivate) {
        getFkbParams().remove(deActivatedParam);
      }
    }
  }

  /**
   * 1. FiMapParam'da olan parametreleri aktif eder
   * <p>
   * 2. FiMapParam'da olmayan parametreleri pasif eder
   * <p>
   * 3. List tipinde parametre varsa, çoklu parametreye (convertMultiToSingle) çevirir
   */
  public void processParamsC1() {
    if (getFkbParams() != null) {

      // list değer varsa işlem sonunda list paramları single paramlara çevrilecek
      BooleanProperty boListDegerVarMi = new SimpleBooleanProperty(false);

      // MapParams olan parametreler aktif edilir ve collection olan parametre olup olmadığı kontrol edilir
      getFkbParams().forEach((key, value) -> {
        //Optional Param'ın sorguda olup olmadığının kontrolüne gerek yok.
        activateOptParam(key);

        if (value instanceof Collection) {
          boListDegerVarMi.setValue(true);
        }

      });

      // MapParam'da olmayan parametreler pasif edilir
      Set<String> setParams = getParamOptionalsFromQuery();

      for (String setParam : setParams) {
        if (!getFkbParams().containsKey(setParam)) {
          deActivateOptParam(setParam);
        }
      }

      if (boListDegerVarMi.getValue()) {
        convertListParamsToMultiParams();
      }

    }
  }


  public FiQuery addActivateParamIfNotEmpty(Object objKey, Object value, Boolean addPercentage) {
    if (value != null) {
      if (value instanceof String) {
        if (FiString.isEmpty((String) value)) {
//					Loghelper.get(getClass()).debug("Boş olduğu için aktive edilmedi Param:" + objKey.toString());
//					return this;
          return this;
        }
        if (FiBool.isTrue(addPercentage)) value = "%" + value.toString() + "%";
        getMapParamsInit().put(objKey.toString(), value);
        activateOptParam(objKey.toString());
        return this;
      }

      // list harici desteklenmiyor , ama boşsa o da alınmaz
      if (value instanceof Collection) {
        if (FiCollection.isEmpty((Collection) value)) return this;
      }

      if (value instanceof List) {
        if (FiCollection.isEmpty((List) value)) return this;
//				Loghelper.get(getClass()).debug("List Param Ekleniyor");
        String param = objKey.toString();
        addMultiParams(param, (List) value);
        activateOptParam(param);
        return this;
      }

//			Loghelper.get(getClass()).debug("Aktive edildi Param:" + objKey.toString());
      getMapParamsInit().put(objKey.toString(), value);
      activateOptParam(objKey.toString());
    } else {
//			Loghelper.get(getClass()).debug("Aktive edilmedi Param:" + objKey.toString());
    }
    return this;
  }

  private void addMultiParams(String paramName, List listData) {

    if (FiCollection.isEmpty(listData)) return;

    //@ multi paramlar , yeni sablon parametresi şeklinde mapParamsNew Olarak oluşturulur.
    Map<String, Object> mapParamsNew = new HashMap<>();

    Integer index = FiQueryUtils.getMultiParamStartIndex();
    for (Object paramValue : listData) {
      String paramNameTemplate = FiQueryUtils.makeMultiParamTemplate(paramName, index);
      mapParamsNew.put(paramNameTemplate, paramValue);
      index++;
    }

    //@ eski param , parametre listesinde varsa , çıkarılır
    getMapParamsInit().remove(paramName);
    getMapParamsInit().putAll(mapParamsNew);

    //@ sql sorgusu multi parama çevrilir
    convertSqlParamToMultiParam(paramName, listData.size());
  }


  /**
   * Sorguda bulunan __userParam şeklindeki user parametrelerini ilgili değere çevirir
   * <p>
   * iki alt çizgi seçilmesinin nedeni, değişken tanımlarında _ alt çizgiye izin veriyor oluşu.
   * <p>
   * Bu parametreler eğer mapParams'da var ise, değeri yer değiştirir.
   */
  public void convertUserParamsToValue() {

    if (getMapParamsInit().isEmpty()) return;
    setTxQuery(FiQueryUtils.convertUserParamsToValue(getTxQuery(), getMapParamsInit()));

  }

//    public String getTxUserParamPrefix() {
//        return "__";
//    }

  public void logParams() {
    Loghelper.get(getClass()).debug("Fiquery log-params");
    Loghelper.get(getClass()).debug(FiConsole.textFkb(getMapParamsInit()));
  }

  public Class getRetClass() {
    return retClass;
  }

  public void setRetClass(Class retClass) {
    this.retClass = retClass;
  }

  public FicList getFicListQuery() {
    return ficListQuery;
  }

  public void setFicListQuery(FicList ficListQuery) {
    this.ficListQuery = ficListQuery;
  }

  public Fkf getFkfAll() {
    return fkfAll;
  }

  public void setFkfAll(Fkf fkfAll) {
    this.fkfAll = fkfAll;
  }

  public FiCol getQcfTxSqTableName() {
    return qcfTxSqTableName;
  }

  public void setQcfTxSqTableName(FiCol qcfTxSqTableName) {
    this.qcfTxSqTableName = qcfTxSqTableName;
  }
}