package ozpasyazilim.utils.fidborm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiMapParams;
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
	FiMapParams mapParams; // Map<String,Object>
	List<FiField> queryFieldList;
	List<FiField> queryWhereList;

	public FiQuery(String sql) {
		this.txQuery = sql;
	}

	public FiQuery(String sql, FiMapParams fiMapParams) {
		this.txQuery = sql;
		this.mapParams = fiMapParams;
	}

	public FiQuery() {
	}

	public static FiQuery bui() {
		return new FiQuery();
	}

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

	public static String deActivateOptParamMain(String sql, String param) {
		final String regex = String.format("--!(%s).*\\s*.*", param); // 15-10-19
		final String subst = "--$1 deactivated"; // 15-10-19
		return sql.replaceAll(regex, subst);
	}

	public void deActivateOptParam(String txOptParamName) {
		setTxQuery(deActivateOptParamMain(getTxQuery(), txOptParamName));
	}

	public void deActivateSqlAtParam(String param) {
		setTxQuery(deactivateSqlAtParamMain(getTxQuery(), param));
	}

	public static void fhrDeActivateOptParam(StringProperty propSql, String param) {
		propSql.set(deActivateOptParamMain(propSql.get(), param));
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
	 * Comment içerisinde değişkenler varsa iptal eder.
	 * <br>
	 *
	 * @param sql
	 * @return
	 */
	public static String fhrFixSqlProblems(String sql) {
		// yorum satırların @ varsa # diyeze çevirir.
		return sql.replaceAll("(--.*)(@)", "$1#");
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

		if (getMapParams() == null) return;

		List<String> listParamToConvertMulti = new ArrayList<>();

		getMapParams().forEach((param, value) -> {
			if (value instanceof List) {
				// concurrent modification olmaması amacıyla convert işlemi ayrı yapılacak (aşağıda)
				listParamToConvertMulti.add(param);
			}
		});

		for (String param : listParamToConvertMulti) {
			List value = (List) getMapParams().get(param);
			convertSqlAsListParamToMultiParam(param, value);
		}

	}

	public void convertListParamToMultiParams(FiQueryConf fiQueryConf) {

		if (getMapParams() == null) return;
		if (fiQueryConf == null) fiQueryConf = new FiQueryConf();

		FiQueryConf finalFiQueryConf = fiQueryConf;

		FiMapParams fiMapParams = new FiMapParams(getMapParams());

		fiMapParams.forEach((param, value) -> {
			if (value instanceof List) {

				if (FiBoolean.isTrue(finalFiQueryConf.getBoActivateOnlyFullParams())) {
					if (!checkParamValueEmpty(value)) {
						convertSqlAsListParamToMultiParam(param, (List) value);
					}
				} else {
					convertSqlAsListParamToMultiParam(param, (List) value);
				}
			}
		});

	}

	/**
	 * List değerindeki parametreyi abc_1,abc_2 gibi multi parametreye çevirir
	 *
	 * @param param
	 * @param listData
	 */
	private void convertSqlAsListParamToMultiParam(String param, List listData) {

		// şablona göre yeni parametre listesi ve değeri map koleksiyonuna atılır
		Map<String, Object> mapParamNew = new HashMap<>();

		for (int index = 0; index < listData.size(); index++) {
			String sablonParam = FiQuery.genTemplateMultiParam(param, index);
			mapParamNew.put(sablonParam, listData.get(index));
		}

		// Sorgu cümlesi güncellenir (eski parametre , yeni parametre ile değiştirilir.)
		setTxQuery(fhrConvertSqlForMultiParamByTemplate(getTxQuery(), param, listData.size()));
		// map paramden eski parametre çıkarılıp, yenileri eklenir
		getMapParams().remove(param);
		getMapParams().putAll(mapParamNew);

	}

	public void convertListParamToMultiParams(FiMapParams mapBind) {
		setMapParams(mapBind);
		convertListParamToMultiParams();
	}

	/**
	 * sql sorgu içindeki parametreyi, parametre_index (genTem şeklinde multi parametreye çevirir.
	 * <p>
	 * param_1 param_2 gibi
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

	public FiMapParams getMapParams() {
		return mapParams;
	}

	public FiMapParams getMapParamsInit() {
		if (mapParams == null) {
			mapParams = new FiMapParams();
		}
		return mapParams;
	}

	public FiQuery setMapParams(FiMapParams mapParams) {
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
	 * boActivateOnlyFullParams true olursa dolu olan parametreleri aktif eder
	 */
	public void activateParamsMain(Boolean boActivateOnlyFullParams) {
		if (getMapParams() != null) {
			List<String> deActivatedParamList = new ArrayList<>();
			getMapParams().forEach((key, value) -> {
				if (FiBoolean.isTrue(boActivateOnlyFullParams)) {
					// Dolu olanları aktif edecek, boş olanları deaktif edecek
					if (value instanceof String) {
						if (!FiString.isEmpty((String) value)) {
							String newQuery = fsmActivatedOptParamV1Main(getTxQuery(), key);
							setTxQuery(newQuery);
						} else {
							String newQuery = deActivateOptParamMain(getTxQuery(), key);
							setTxQuery(newQuery);
							deActivatedParamList.add(key);
						}
					} else { // string tipinden dışında olanlar
						if (value != null) {
							String newQuery = fsmActivatedOptParamV1Main(getTxQuery(), key);
							setTxQuery(newQuery);
						} else {
							String newQuery = deActivateOptParamMain(getTxQuery(), key);
							setTxQuery(newQuery);
							deActivatedParamList.add(key);
						}
					}
				} else { // boActivateOnlyFullParams false veya null ise, tüm parametreleri aktif eder
					String newQuery = fsmActivatedOptParamV1Main(getTxQuery(), key);
					setTxQuery(newQuery);
				}
			});

			for (String deActivatedParam : deActivatedParamList) {
				getMapParams().remove(deActivatedParam);
			}
		}
	}

	/**
	 * FiMapParam'da olan parametreleri aktive eder.
	 * <p>
	 * boActivateOnlyFullParams true olursa dolu olan parametreleri aktif eder
	 */
	public void activateParamsNotNull(Boolean boActivateNotNullParams) {
		if (getMapParams() != null) {
			List<String> deActivatedParamList = new ArrayList<>();
			getMapParams().forEach((key, value) -> {
				if (FiBoolean.isTrue(boActivateNotNullParams)) {
					// Null olanlar deaktif olacak
					if (value != null) {
						String newQuery = fsmActivatedOptParamV1Main(getTxQuery(), key);
						setTxQuery(newQuery);
					} else {
						String newQuery = deActivateOptParamMain(getTxQuery(), key);
						setTxQuery(newQuery);
						deActivatedParamList.add(key);
					}
				} else { // boActivateNotNullParams false veya null ise, tüm parametreleri aktif eder
					String newQuery = fsmActivatedOptParamV1Main(getTxQuery(), key);
					setTxQuery(newQuery);
				}
			});

			for (String deActivatedParam : deActivatedParamList) {
				getMapParams().remove(deActivatedParam);
			}
		}
	}

	/**
	 * 1. FiMapParam'da olan parametreleri confige göre aktif eder.Config dolu olup olmadığı kontrol edilme şartı eklenebilir. Dolu degilse pasif eder.
	 * <p>
	 * 2. FiMapParam'da olmayan parametreleri pasif eder
	 * <p>
	 * 3. List tipinde parametre varsa, çoklu parametreye (convertMultiToSingle) çevirir
	 */
	public void prepSqlOptParamsByFiMap(FiQueryConf fiQueryConf) {

		getMapParamsInit();

		if (fiQueryConf == null) {
			fiQueryConf = new FiQueryConf();
		}

		// list değer varsa işlem sonunda list paramları single paramlara çevrilecek
		BooleanProperty boListDegerVarMi = new SimpleBooleanProperty(false);

		FiQueryConf finalFiQueryConf = fiQueryConf;

		getMapParams().forEach((key, value) -> {
			//Optional Param'ın olup olmadığı kontroline gerek yok.

			if (FiBoolean.isTrue(finalFiQueryConf.getBoActivateOnlyFullParams())) {
				if (!checkParamValueEmpty(value)) {
					activateOptParam(key);
				} else {
					deActivateOptParam(key);
				}
			} else {
				activateOptParam(key);
			}

			if (value instanceof Collection) {
				boListDegerVarMi.setValue(true);
			}

		});

		for (String activateParam : getMapParams().getActivateParamSet()) {
			activateOptParam(activateParam);
		}

		for (String deActivateParam : getMapParams().getDeActivateParamSet()) {
			deActivateOptParam(deActivateParam);
		}

		// FiMapParam'da olmayan parametreleri pasif eder
		Set<String> setParams = getParamOptionals();
		for (String setParam : setParams) {
			if (!getMapParams().containsKey(setParam)) {
//				fhrDeActivateOptParam(txQuery, setParam);
				deActivateOptParam(setParam);
			}
		}

		if (boListDegerVarMi.getValue()) {
			convertListParamToMultiParams();
		}


	}

	public void prepSqlAtParamsFullByFiMap() {
		prepSqlAtParamsByFiMapMain(FiQueryConf.bui().setBoActivateOnlyFullParams(true));
	}

	/**
	 * without configuration
	 */
	public void prepSqlAtParamsWoutConfByFiMap() {
		prepSqlAtParamsByFiMapMain(null);
	}

	/**
	 * Sql At Parametreleri ile çalışır (!!! Optional ile degil)
	 * <p>
	 * 1. FiMapParam'da olan parametreleri confige göre aktif eder.Config dolu olup olmadığı kontrol edilme şartı eklenebilir. Dolu degilse pasif eder.
	 * <p>
	 * 2. FiMapParam'da olmayan parametreleri pasif eder
	 * <p>
	 * 3. List tipinde parametre varsa, çoklu parametreye (convertMultiToSingle) çevirir
	 */
	public void prepSqlAtParamsByFiMapMain(FiQueryConf fiQueryConf) {

		getMapParamsInit();

		if (fiQueryConf == null) {
			fiQueryConf = new FiQueryConf();
		}

		// list değer varsa işlem sonunda list paramları single paramlara çevrilecek
		BooleanProperty boListDegerVarMi = new SimpleBooleanProperty(false);

		FiQueryConf finalFiQueryConf = fiQueryConf;

		getMapParams().forEach((key, value) -> {
			//Optional Param'ın olup olmadığı kontroline gerek yok.

			if (FiBoolean.isTrue(finalFiQueryConf.getBoActivateOnlyFullParams())) {
				if (!checkParamValueEmpty(value)) {
					activateSqlAtParam(key);
				} else {
					deActivateSqlAtParam(key);
				}
			} else {
				activateSqlAtParam(key);
			}

			if (value instanceof Collection) {
				boListDegerVarMi.setValue(true);
			}

		});

		for (String activateParam : getMapParams().getActivateParamSet()) {
			activateSqlAtParam(activateParam);
		}

		for (String deActivateParam : getMapParams().getDeActivateParamSet()) {
			deActivateSqlAtParam(deActivateParam);
		}

		// FiMapParam'da olmayan parametreleri pasif eder
		Set<String> setParams = getParamsSqlAt();
		for (String setParam : setParams) {
			if (!getMapParams().containsKey(setParam)) {
				deActivateSqlAtParam(setParam);
			}
		}

		if (boListDegerVarMi.getValue()) {
			convertListParamToMultiParams(fiQueryConf);
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
	public void prepSqlOptParamsByFiMap() {
		if (getMapParams() != null) {

			// list değer varsa işlem sonunda list paramları single paramlara çevrilecek
			BooleanProperty boListDegerVarMi = new SimpleBooleanProperty(false);

			getMapParams().forEach((key, value) -> {
				//Optional Param'ın olup olmadığı kontroline gerek yok.
				//String newQuery = activateOptParamV1Main(getTxQuery(), key);
				//setTxQuery(newQuery);
				activateOptParam(key);

				if (value instanceof Collection) {
					boListDegerVarMi.setValue(true);
				}

			});

			for (String activateParam : getMapParams().getActivateParamSet()) {
				activateOptParam(activateParam);
			}

			for (String deActivateParam : getMapParams().getDeActivateParamSet()) {
				deActivateOptParam(deActivateParam);
			}

			// FiMapParam'da olmayan parametreleri pasif eder
			Set<String> setParams = getParamOptionals();
			for (String setParam : setParams) {
				if (!getMapParams().containsKey(setParam)) {
//					fhrDeActivateOptParam(txQuery, setParam);
					deActivateOptParam(setParam);
				}
			}

			if (boListDegerVarMi.getValue()) {
				convertListParamToMultiParams();
			}

		}
	}


	private Set<String> getParamOptionals() {
		return findParamOptionals(getTxQuery());
	}

	private Set<String> getParamsSqlAt() {
		return findParams(getTxQuery());
	}

	public static Set<String> findParamOptionals(String sql) {
		// regexr: Sql Parametre List rg2011201511
		String regEx = "--!(\\w*).*\\n";
		return FiRegExp.matchGroupOneSet(regEx, sql);
	}

	public static Set<String> findParams(String sql) {
		String regEx = "@(\\w*)";
		return FiRegExp.matchGroupOneSet(regEx, sql);
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
	public static String fsmActivatedOptParamV1Main(String sql, String param) {

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

	public static void main(String[] args) {

		String sql = "--sq202109290930\n" +
				"select met2.metLnKod,met2.metTxEvrakAdi,tblDevir.cha_meblag,met2.metBoPanoTip\n" +
				"FROM\n" +
				"(select chh.cha_evrak_tip , chh.cha_cinsi , chh.cha_tip , chh.cha_normal_Iade,met.metLnKod\n" +
				"FROM CARI_HESAP_HAREKETLERI chh \n" +
				"LEFT JOIN CARI_HESAPLAR ch ON chh.cha_kod = ch.cari_kod \n" +
				"WHERE chh.cha_iptal=0\n" +
				"and chh.cha_srmrkkodu = @cha_srmrkkodu \n" +
				"--!cha_tarihi1\n" +
				"--and cha_tarihi >= @cha_tarihi1\n" +
				"--TOP {{top}}\n" +
				"and cha_tarihi <= @cha_tarihi2\n" +
				"and evraklar IN (@listEvrak)\n" +
				"GROUP BY cha_evrak_tip,cha_cinsi,cha_tip,cha_normal_Iade,met.metLnKod) tblDevir\n" +
				"ORDER BY metBoPanoTip DESC,metTxEvrakAdi ";
//
		FiMapParams fiMapParams = new FiMapParams();
		fiMapParams.add("top", 10);

//
//		List<String> listEvrak = new ArrayList<>();
//		listEvrak.add("fatura");
//		listEvrak.add("stok");
//
//		fiMapParams.puto("listEvrak",listEvrak);
//
		FiQuery fiQuery = new FiQuery(sql);
		fiQuery.setMapParams(fiMapParams);
//
//		FiQueryConf fiQueryConf = new FiQueryConf();
//		fiQueryConf.setBoActivateOnlyFullParams(true);
//
//		fiQuery.prepSqlAtParamsFullByFiMap();
//
////		System.out.println(FiQuery.activateSqlAtParamMain(sql, "cha_tarihi2"));
//

		fiQuery.setTxQuery(FiQuery.activateNamedParamMain(fiQuery.getTxQuery(), "top", "1000"));
		System.out.println(fiQuery.getTxQuery());
		FiConsole.printMapFi(fiQuery.getMapParams());

//		final String regex = " *(--)*(.*)(\\{\\{%s\\}\\})(.*)\\n{0,1}";
//		final String string = "--activate named param v211002_12082\n"
//				+ "--orijinal sql parametreleri aktif eder\n"
//				+ "--TOP {{%s}}\n"
//				+ "WHERE 1=1\n"
//				+ "AND chh.cha_tarihi < @cha_tarihi1\n"
//				+ "     --!ps 1 \n"
//				+ "     --AND ch.cari_sektor_kodu IN (@cari_sektor_kodu)\n"
//				+ "GROUP BY ch.cari_vdaire_no \n"
//				+ "AND chh.cha_tarihi >= @cha_tarihi1 AND chh.cha_tarihi <= @cha_tarihi2\n"
//				+ "--!ps 2 dsssds\n"
//				+ "--AND ch.cari_sektor_kodu IN (@cari_sektor_kodu)\n"
//				+ "--!ps 3\n"
//				+ "AND ch.cari_sektor_kodu = @cari_sektor_kodu\n"
//				+ "--!ps_abc\n"
//				+ "AND ch.ps IN (@ps)\n\n";
//		final String subst = "$2%s$4 --%s activated\\n";
//
//		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
//		final Matcher matcher = pattern.matcher(string);
//
//		// The substituted value will be contained in the result variable
//		final String result = matcher.replaceAll(subst);
//
//
//		System.out.println("Substitution result: " + result);
//
//		String string2 = string.replaceAll(regex, subst);
//
//		System.out.println("Substitution result 2: " + string2);
	}

}
