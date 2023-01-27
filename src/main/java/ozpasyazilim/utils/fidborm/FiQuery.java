package ozpasyazilim.utils.fidborm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.log.Loghelper;

import java.util.*;

/**
 * Syntax
 * <p>
 * Jdbi Param :paramName
 * <p>
 * Sql (At) Param @paramName
 * <p>
 * Opt Param --!paramName
 */
public class FiQuery {

	String txQuery;
	String txCandIdFieldName;
	String txPrimaryKeyFieldName;
	FiKeyBean mapParams; // Map<String,Object>
	List<FiField> queryFieldList;
	List<FiField> queryWhereList;

	public FiQuery() {
	}

	public FiQuery(String sql) {
		this.txQuery = sql;
	}

	public FiQuery(String sql, FiKeyBean fiKeyBean) {
		this.txQuery = sql;
		this.mapParams = new FiKeyBean(fiKeyBean);
	}

	public static FiQuery bui() {
		return new FiQuery();
	}

	// ******** Statik Metodlar ******* //

	/**
	 * tüm optional parametreleri ( --!optParam ) deaktif eder.
	 *
	 * @param sql
	 * @return
	 */
	public static String deActivateAllOptParams(String sql) {
		final String regex = "--!(\\w+).*\\s*.*"; // 15-10-19
		final String subst = "--$1 deactivated"; // 15-10-19
		return sql.replaceAll(regex, subst);
	}

	public static String fsmDeActivateOptParamMain(String sql, String param) {
		final String regex = String.format("--!(%s).*\\s*.*", param); // 15-10-19
		final String subst = "--$1 deactivated"; // 15-10-19
		return sql.replaceAll(regex, subst);
	}

	public void deActivateOptParam(String txOptParamName) {
		setTxQuery(fsmDeActivateOptParamMain(getTxQuery(), txOptParamName));
	}

	public void deActivateSqlAtParam(String param) {
		setTxQuery(deactivateSqlAtParamMain(getTxQuery(), param));
	}

	public static void fhrDeActivateOptParam(StringProperty propSql, String param) {
		propSql.set(fsmDeActivateOptParamMain(propSql.get(), param));
	}

	/**
	 * Optional Sql Param Syntax : --!sqlparam
	 * <p>
	 * opsiyonel sql parametrelerin , parametre işaretini(!) kaldırır ve aktif eder.
	 *
	 * @param sql
	 * @param param
	 * @return
	 */
	public static String activateOptParamMain(String sql, String param) {
		// 200317_1741 sql param altındaki ifade yorum satırı olursa, yorum satırını kaldırır.
		final String regex240320 = "--!(%s)\\b.*\\s*-*(.*)";
		final String regex = String.format(regex240320, param);

		final String subst = "--$1 activated \\\n$2"; // 17-03-2020
		return sql.replaceAll(regex, subst);
	}

	public static String activateSqlAtParamMain(String sql, String param) {
		final String regexPatt = " *(--)*(.*)@(%s)\\b(.*)\\n";
		final String subst = "$2@$3$4 --$3 activated\\\n"; // 17-03-2020

		final String regex = String.format(regexPatt, param);
		return sql.replaceAll(regex, subst);
	}


	public static String activateNamedParamMain(String sql, String param, String value) {
		if (FiString.isEmptyTrim(value) || FiString.isEmptyTrim(param)) return sql;

		final String regex = " *(--)*(.*)(\\{\\{%s\\}\\})(.*)\\n{0,1}";
		final String subst = "$2%s$4 --%s activated\n";

		final String substUp = String.format(subst, value, param);
		final String regexUp = String.format(regex, param);

		// System.out.println(regexUp);
		// System.out.println(substUp);
		return sql.replaceAll(regexUp, substUp);
	}


	public static String deactivateSqlAtParamMain(String sql, String param) {
		final String regexPatt = "(.*)@(%s)\\b(.*)\\n";
		final String subst = "--$1$2$3 --$2 deactivated\\\n";

		final String regex = String.format(regexPatt, param);
		return sql.replaceAll(regex, subst);
	}

	public static void fsmActivateOptParamForProp(StringProperty sql, String param) {
		sql.set(activateOptParamMain(sql.get(), param));
	}

	/**
	 * Comment içerisinde değişkenler varsa iptal eder.# işareti ile gösterir.
	 * <br>
	 *
	 * @param sql
	 * @return
	 */
	public static String fhrFixSqlProblems(String sql) {
		// yorum satırların @ varsa # diyeze çevirir.
		//return sql.replaceAll("(--.*)(@)", "$1#"); // yorum satırında iki tane @ olursa, sadece birini değiştiriyor
		return sql.replaceAll("--(.*?)@(\\w+)(.*)", "--fixed$1$2"); // 23-12-22
	}

	/**
	 * sql içindeki parametreyi, jdbi list parametre formatına çevirir.
	 * <p>
	 * parentez içindeki @ ile gösterilen parametreyi büyüktür küçüktür içerisine alır
	 * <p>
	 * Örnek
	 * <p>
	 * ( @xxx ) --> ( <xxx> )
	 */
	public static String fhrConvertSqlParamToListJdbiParamMain(String sql) {
		String sqlNew = sql.replaceAll("\\(.*@(.*)\\)", "(<$1>)");
		return sqlNew;
	}

	public static String genTemplateMultiParam2(String param, Integer index) {
		return param + "_" + index.toString();
	}

	/**
	 * sql sorgu içindeki parametreyi, parametre_index şeklinde multi parametreye çevirir.
	 * <p>
	 * param_1 param_2 gibi
	 *
	 * @param sql
	 * @param param
	 * @param count
	 * @return
	 */
	public static String fhrConvertSqlForMultiParamByTemplate2(String sql, String param, Integer count) {

		//sadece parentez içinde olanları bulmak için
		//final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
		final String regex = String.format("@%s", param);

		StringBuilder customParam = new StringBuilder();

		for (int index = 0; index < count; index++) {
			if (index != 0) customParam.append(",");
			String sablon = genTemplateMultiParam2(param, index);
			customParam.append("@" + sablon);
		}

		String sqlNew = sql.replaceAll(regex, String.format("%s", customParam)); //(%s)
		return sqlNew;
	}

	public static void fhrConvertSqlForMultiParamByTemplate2(StringProperty sql, String param, Integer count) {
		sql.set(fhrConvertSqlForMultiParamByTemplate2(sql.get(), param, count));
	}

	/**
	 * @param sql
	 * @return
	 * @ leri : çevirir.
	 */
	public static String fhrConvertSqlParamToJdbiParamMain(String sql) {
		return sql.replaceAll("@", ":");
	}

	public static String fhrConvertSqlParamToCommentMain(String key, String sql) {
		if (!FiType.isEmptyWithTrim(key)) {
			String regex = "\\n\\s*.*@(" + key + ").*";
			String replacement = "\\\n-- $1 deactivated";
			return sql.replaceAll(regex, replacement); // deactivated
		}
		return null;
	}

	/**
	 * Convert Sql Param(@) To Java Param (:)
	 * <p>
	 * s(sql) to j(ava)
	 * <p>
	 * sorgudaki @ ifadeleri : ye çevirir.
	 *
	 * @param sqlQuery
	 * @return
	 */
	public static String stoj(String sqlQuery) {
		if (sqlQuery == null) return null;
		sqlQuery = fhrFixSqlProblems(sqlQuery);
		sqlQuery = fhrConvertSqlParamToJdbiParamMain(sqlQuery);
		return sqlQuery;
	}

	// end of Statik Metodlar

	// Obje Metodları

	public void activateOptParam(String txOptParamName) {
		setTxQuery(activateOptParamMain(getTxQuery(), txOptParamName));
	}

	public void activateSqlAtParam(String fieldName) {
		setTxQuery(activateSqlAtParamMain(getTxQuery(), fieldName));
	}

	public static String genTemplateMultiParam(String param, Integer index) {
		return param + "_" + index.toString();
	}

	public void convertListParamToMultiParams() {
		convertListParamToMultiParams(false);
	}

	public void convertListParamToMultiParamsWithKeep() {
		convertListParamToMultiParams(true);
	}

	/**
	 * List Türündeki parametreleri multi param (abc_1,abc_2... gibi) çevirir
	 */
	public void convertListParamToMultiParams(Boolean boKeepOldMParamInMapParams) {

		if (getMapParams() == null) return;

		// (1) List türündeki parametreler bulunur.
		List<String> listMultiParamsName = new ArrayList<>();
		getMapParams().forEach((param, value) -> {

			// concurrent modification olmaması amacıyla convert işlemi ayrı yapılacak (aşağıda)
			if (value instanceof List) {
				listMultiParamsName.add(param);
			}

			if (value instanceof Set) {
				listMultiParamsName.add(param);
			}

		});
		// --end-1

		// List-Set türünde olan parametreleri , multi tekli parametrelere çevirir. (abc_1,abc_2 gibi)
		for (String param : listMultiParamsName) {
			Collection paramCollection = (Collection) getMapParams().get(param);
			convertSqlFromSingleParamToMultiParam(param, paramCollection, boKeepOldMParamInMapParams);
		}

	}

	/**
	 * List değerindeki parametreyi abc_1,abc_2 gibi multi parametreye çevirir
	 * <p>
	 * Not: eski parametre çıkarılmış kaladabilirdi
	 *
	 * @param param
	 * @param collData
	 * @param boKeepOldParam
	 */
	private void convertSqlFromSingleParamToMultiParam(String param, Collection collData, Boolean boKeepOldParam) {

		// (1) şablona göre yeni eklenecek parametre listesi
		FiKeyBean paramsNew = new FiKeyBean();
		StringBuilder sbNewParamsForQuery = new StringBuilder("");

		int index = 0;
		for (Object listDatum : collData) {
			String sablonParam = FiQuery.genTemplateMultiParam(param, index);
			if (index != 0) sbNewParamsForQuery.append(",");
			sbNewParamsForQuery.append("@" + sablonParam);
			paramsNew.put(sablonParam, listDatum);
			index++;
		}
		// end-1

		// Sorgu cümlesi güncellenir (eski parametre çıkarılır , yeni multi parametreler eklenir.)
		// setTxQuery(fhrConvertSqlForMultiParamByTemplate(getTxQuery(), param, collData.size()));
		String sqlNew = getTxQuery().replaceAll("@" + param, sbNewParamsForQuery.toString()); //(%s)
		setTxQuery(sqlNew);

		// map paramden eski parametre çıkarılıp, yenileri eklenir
		if (!FiBoolean.isTrue(boKeepOldParam)) {
			getMapParams().remove(param);
		}
		getMapParams().putAll(paramsNew);
	}

	public void convertListParamToMultiParams(FiKeyBean mapBind) {
		setMapParams(mapBind);
		convertListParamToMultiParams();
	}

	/**
	 * sql sorgu içindeki parametreyi (@paramName), parametre_indexNo şeklinde multi parametreye çevirir. (@paramName_1,@paramName_2,...)
	 * <p>
	 * Örnek @paramName -> @paramName_1 , @paramName_2 , ...
	 */
	public static String fhrConvertSqlForMultiParamByTemplate(String sql, String param, Integer count) {

		//sadece parentez içinde olanları bulmak istenirse
		//final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
		final String regex = String.format("@%s", param);

		StringBuilder customParam = new StringBuilder();

		Integer indexParam = getMultiParamStartIndex();

		for (int index = 0; index < count; index++) {
			if (index != 0) customParam.append(",");
			String sablon = genTemplateMultiParam(param, indexParam);
			customParam.append("@" + sablon);
			indexParam++;
		}

		String sqlNew = sql.replaceAll(regex, customParam.toString()); //(%s)
		return sqlNew;
	}

	/**
	 * sql sorgu içindeki parametreyi, parametre_index şeklinde multi parametreye çevirir.
	 * <p>
	 * param_1 param_2 gibi
	 */
	private void convertSqlParamToMultiParam(String param, Integer count) {

		String sql = getTxQuery();

		//sadece parentez içinde olanları bulmak istenirse
		//final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
		final String regex = String.format("@%s", param);

		StringBuilder customParam = new StringBuilder();

		Integer indexParam = getMultiParamStartIndex();
		for (int countPrm = 0; countPrm < count; countPrm++) {
			if (countPrm != 0) customParam.append(",");
			String sablon = genTemplateMultiParam(param, indexParam);
			customParam.append("@" + sablon);
			indexParam++;
		}

		String sqlNew = sql.replaceAll(regex, String.format("%s", customParam.toString())); //(%s)
		setTxQuery(sqlNew);
	}

	public FiKeyBean getMapParams() {
		return mapParams;
	}

	public FiKeyBean getMapParamsInit() {
		if (mapParams == null) {
			mapParams = new FiKeyBean();
		}
		return mapParams;
	}

	public FiQuery setMapParams(FiKeyBean mapParams) {
		this.mapParams = mapParams;
		return this;
	}

	public void activateFiSqlFieldIfExists(String txFieldName) {
		if (getMapParams() != null && getMapParams().containsKey(txFieldName)) {
//			fsmActivateOptParamForProp(txQueryProperty(), txFieldName);
			activateOptParam(txFieldName);
		}
	}

	public void activateFiSqlFieldIfNotEmpty(String txFieldName) {
		if (getMapParams() != null) {
			Object value = getMapParams().getOrDefault(txFieldName, null);

			if (value instanceof String) {
				if (!FiString.isEmpty((String) value)) {
//					fsmActivateOptParamForProp(txQueryProperty(), txFieldName);
					activateOptParam(txFieldName);
				}
			} else { // string tipinden dışında olanlar
				if (value != null) {
//					fsmActivateOptParamForProp(txQueryProperty(), txFieldName);
					activateOptParam(txFieldName);
				}
			}
		}
	}

	/**
	 * FiMapde olan parametreleri aktif eder
	 */
	public void activateParamsByFiMap() {
		activateParamsMain(false);
	}

	/**
	 * use activateParamsMain
	 *
	 * @param boActivateOnlyFullParams
	 */
	@Deprecated
	public void activateParamsNotEmptyDep(Boolean boActivateOnlyFullParams) {
		activateParamsMain(boActivateOnlyFullParams);
	}

	public void activateParamsNotEmpty() {
		activateParamsMain(true);
	}

	/**
	 * FiMapParam'da olan parametreleri aktive eder.
	 * <p>
	 * boActivateOnlyFullParams true olursa sadece dolu olan parametreleri aktif eder, dolu değilse deaktif eder.
	 * <p>
	 * String boş string degilse
	 * <p>
	 * Collection larda size > 0 olmalı
	 * <p>
	 * Diger türler için null olmamalı
	 */
	public void activateParamsMain(Boolean boActivateOnlyFullParams) {
		if (getMapParams() != null) {
			List<String> deActivatedParamList = new ArrayList<>();

			getMapParams().forEach((key, value) -> {
				if (FiBoolean.isTrue(boActivateOnlyFullParams)) {
					// Dolu olanları aktif edecek, boş olanları deaktif edecek
					if (value instanceof String) {
						if (!FiString.isEmpty((String) value)) {
							String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
							setTxQuery(newQuery);
						} else {
							String newQuery = fsmDeActivateOptParamMain(getTxQuery(), key);
							setTxQuery(newQuery);
							deActivatedParamList.add(key);
						}
					} else if (value instanceof Collection) {
						if (!FiCollection.isEmpty((Collection) value)) {
							String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
							setTxQuery(newQuery);
						} else {
							String newQuery = fsmDeActivateOptParamMain(getTxQuery(), key);
							setTxQuery(newQuery);
							deActivatedParamList.add(key);
						}
					} else { // string ve collection tipinden dışında olanlar, null degilse aktif edilir
						if (value != null) {
							String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
							setTxQuery(newQuery);
						} else {
							String newQuery = fsmDeActivateOptParamMain(getTxQuery(), key);
							setTxQuery(newQuery);
							deActivatedParamList.add(key);
						}
					}
				} else { // boActivateOnlyFullParams false veya null ise, tüm parametreleri aktif eder
					String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
					setTxQuery(newQuery);
				}
			});

			for (String deActivatedParam : deActivatedParamList) {
				getMapParams().remove(deActivatedParam);
			}
		}
	}


	/**
	 * FiMapParam'da null olmayan parametreleri aktive eder, null olanları deAktif eder.
	 * <p>
	 * Deaktif edilmek istene parametreler null olarak gönderilir. !!!
	 */
	public void activateParamsNotNull() {
		if (getMapParams() != null) {
			List<String> deActivatedParamList = new ArrayList<>();
			getMapParams().forEach((key, value) -> {
				//if (FiBoolean.isTrue(boActivateNotNullParams)) {
				// Null olanlar deaktif olacak
				if (value != null) { // null degilse aktif edilir.
					String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
					setTxQuery(newQuery);
				} else { // param null ise,deaktif edilir
					String newQuery = fsmDeActivateOptParamMain(getTxQuery(), key);
					setTxQuery(newQuery);
					deActivatedParamList.add(key);
				}
//				} else { // boActivateNotNullParams false veya null ise, tüm parametreleri aktif eder
//					String newQuery = fsmActivateOptParamV1Main(getTxQuery(), key);
//					setTxQuery(newQuery);
//				}
			});

			// deAktif edilen parametreler çıkarıldı.
			for (String deActivatedParam : deActivatedParamList) {
				getMapParams().remove(deActivatedParam);
			}
		}
	}

	/**
	 * String (trimli) içi boş ise true olur
	 * <p>
	 * List değişkenleri miktarı 0 ise true olur
	 * <p>
	 * diger tiplerdeki değişkenler null degilse false (full) olur.
	 *
	 * @param value
	 * @return true boş false dolu
	 */
	private Boolean checkParamValueEmpty(Object value) {

		if (value == null) return true;

		// Boş olanlar için true dönecek

		if (value instanceof String) {
			if (FiString.isEmptyTrim((String) value)) {
				return true;
			} else {
				return false;
			}
		}


		if (value instanceof List) {
			List listData = (List) value;
			if (listData.size() == 0) return true;
			return false;
		}

		// null olmayan diger tipler dolu olarak kabul edildi.
		return false;
	}

	/**
	 * 1. FiMapParam'da olan parametreleri aktif eder
	 * <p>
	 * 2. FiMapParam'da olmayan parametreleri pasif eder
	 * <p>
	 * 3. List tipinde parametre varsa, çoklu parametreye (convertMultiToSingle) çevirir
	 */
	public void prepSqlParamsFull() {
		if (getMapParams() != null) {

			// list değer varsa işlem sonunda list paramları single paramlara çevrilecek
			BooleanProperty boListDegerVarMi = new SimpleBooleanProperty(false);

			// MapParams olan parametreler aktif edilir ve collection olan parametre olup olmadığı kontrol edilir
			getMapParams().forEach((key, value) -> {
				//Optional Param'ın olup olmadığı kontroline gerek yok.
				//String newQuery = activateOptParamV1Main(getTxQuery(), key);
				//setTxQuery(newQuery);
				activateOptParam(key);

				if (value instanceof Collection) {
					boListDegerVarMi.setValue(true);
				}

			});

			// MapParam'da olmayan parametreler pasif edilir
			Set<String> setParams = getParamOptionalsFromQuery();
			for (String setParam : setParams) {
				if (!getMapParams().containsKey(setParam)) {
					deActivateOptParam(setParam);
				}
			}

			if (boListDegerVarMi.getValue()) {
				convertListParamToMultiParams();
			}

		}
	}


	private Set<String> getParamOptionalsFromQuery() {
		return fsmFindParamOptionals(getTxQuery());
	}

	private Set<String> getParamsSqlAt() {
		return findParams(getTxQuery());
	}

	public static Set<String> fsmFindParamOptionals(String sql) {
		// regexr: Sql Parametre List rg2011201511
		String regEx = "--!(\\w*).*\\n";
		return FiRegExp.matchGroupOneToSet(regEx, sql);
	}

	public static Set<String> findParams(String sql) {
		String regEx = "@(\\w*)";
		return FiRegExp.matchGroupOneToSet(regEx, sql);
	}

	/**
	 * Optional Param Syntax : --!paramName
	 * <p>
	 * opsiyonel sql parametrelerin , parametre işaretini(!) kaldırır ve aktif eder.
	 *
	 * @param sql
	 * @param param
	 * @return
	 */
	public static String fsmActivateOptParamV1Main(String sql, String param) {

		//final String regex = String.format("\\s*.*--!(%s)\\s*(.*)", param); // regex bulunan --- çıkarıldı // 4-11-19
		//final String regex = String.format("--!(%s)(\\n|\\s.*\\n)(.*)", param);  // 16-03-2020 ve öncesi
		//final String regex = String.format("--!(%s).*\\s*-*(.*)", param);// 200317_1741 sql param altındaki ifade yorum satırı olursa, yorum satırını kaldırır.
		final String regex240320 = "--!(%s)\\b.*\\s*-*(.*)";
		final String regex = String.format(regex240320, param);

		final String subst = "--$1 activated \\\n$2"; // 17-03-2020

		String sqlYeni = sql.replaceAll(regex, subst);

		return sqlYeni;
	}

	public static Boolean checkOptParamExist(String sql, String param) {

		final String regex240320 = "--!(%s)\\b.*\\s*-*(.*)";
		final String regex = String.format(regex240320, param);

		return FiRegExp.checkPatternExist(sql, regex);
	}

	public List<FiField> getQueryFieldList() {
		if (queryFieldList == null) {
			queryFieldList = new ArrayList<>();
		}
		return queryFieldList;
	}

	public void setQueryFieldList(List<FiField> queryFieldList) {
		this.queryFieldList = queryFieldList;
	}

	public List<FiField> getQueryWhereList() {
		if (queryWhereList == null) {
			queryWhereList = new ArrayList<>();
		}
		return queryWhereList;
	}

	public void setQueryWhereList(List<FiField> queryWhereList) {
		this.queryWhereList = queryWhereList;
	}

	public FiQuery addActivateParamIfNotEmpty(Object objKey, Object value) {
		return addActivateParamIfNotEmpty(objKey, value, null);
	}

	public FiQuery addActivateParamIfNotEmpty(Object objKey, Object value, Boolean addPercentage) {
		if (value != null) {
			if (value instanceof String) {
				if (FiString.isEmpty((String) value)) {
//					Loghelper.get(getClass()).debug("Boş olduğu için aktive edilmedi Param:" + objKey.toString());
//					return this;
					return this;
				}
				if (FiBoolean.isTrue(addPercentage)) value = "%" + value.toString() + "%";
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

		Integer index = getMultiParamStartIndex();
		for (Object paramValue : listData) {
			String paramNameTemplate = genTemplateMultiParam(paramName, index);
			mapParamsNew.put(paramNameTemplate, paramValue);
			index++;
		}

		//@ eski param , parametre listesinde varsa , çıkarılır
		getMapParamsInit().remove(paramName);
		getMapParamsInit().putAll(mapParamsNew);

		//@ sql sorgusu multi parama çevrilir
		convertSqlParamToMultiParam(paramName, listData.size());
	}

	private static Integer getMultiParamStartIndex() {
		return 0;
	}

	public void addParam(Object key, Object value) {
		if (key == null) return;
		getMapParamsInit().add(key.toString(), value);
	}

	/**
	 * value empty ise eklenmez !!!
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

	/**
	 * "--!" optional işareti olan satırların alt satırını kapatır, deactivated edildiği belirtir.
	 * <p>
	 * --! satırlar optional satırdır.
	 * <p>
	 * Optional Format --!param .....
	 *
	 * @return
	 */
	public void deActivateOptionalParams() {
		setTxQuery(deActivateAllOptParams(getTxQuery()));
	}

	public void logQuery() {
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

	/**
	 * Sorguda bulunan __userParam şeklindeki user parametrelerini bulur
	 * <p>
	 * Bu parametreler eğer mapParams'da var ise, değeri yer değiştirir.
	 */
	public void convertUserParamsToValue() {

		if (getMapParamsInit().size() == 0) return;

		String txPattern = "\\b" + getTxUserParamPrefix() + "\\w+\\b";
		Set<String> setUserParam = FiRegExp.matchGroupZeroToSet(txPattern, getTxQuery());

		if (setUserParam.size() > 0) {
			for (String txUserParam : setUserParam) {
				String sqlParam = txUserParam.substring(getTxUserParamPrefix().length(), txUserParam.length());
				//System.out.println("sqlparam:" + sqlParam);
				if (getMapParams().containsKey(sqlParam)) {
					Object paramValue = getMapParams().get(sqlParam);
					if (paramValue != null) {
						// URFIX paramvalue sql injection engellebilir
						String upQuery = getTxQuery().replaceAll(String.format("\\b%s%s\\b", getTxUserParamPrefix(), sqlParam), paramValue.toString());
						setTxQuery(upQuery);
					} else {
						//getTxQuery().replaceAll(String.format("\\b__%s\\b", txUserParam), "NULL");
					}
				}
			}
		}

	}

	public String getTxUserParamPrefix() {
		return "__";
	}

}
