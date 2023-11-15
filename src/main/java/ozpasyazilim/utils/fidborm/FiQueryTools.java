package ozpasyazilim.utils.fidborm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeyBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Sorgu üzerindeki işlemler için statik(!) metodlar
 */
public class FiQueryTools {

	/**
	 * Tüm optional parametreleri ( --!optParam ) deaktif eder. (alt satır yoruma alınmasa bile deaktif olur)
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

	/**
	 * Optional Sql Param Syntax : --!sqlparam
	 * <p>
	 * opsiyonel sql parametrelerin , parametre işaretini(!),varsa sonraki satırda yorum işaretini kaldırır ve aktif eder.
	 *
	 * @param sql
	 * @param param
	 * @return
	 */
	public static String activateOptParamMain(String sql, String param) {
		// Eski kullanımlar
		//String.format("\\s*.*--!(%s)\\s*(.*)", param); // regex bulunan --- çıkarıldı // 4-11-19
		//String.format("--!(%s)(\\n|\\s.*\\n)(.*)", param);
		//String.format("--!(%s).*\\s*-*(.*)", param);// 16-03-2020 öncesinde

		// 200317_1741 sql param altındaki ifade yorum satırı olursa, yorum satırını kaldırır.
		final String regex = String.format("--!(%s)\\b.*\\s*-*(.*)", param);

		final String subst = "--$1 activated \\\n$2"; // 17-03-2020
		return sql.replaceAll(regex, subst);
	}

	public static String activateSqlAtParamMain(String sql, String param) {
		final String regexPatt = " *(--)*(.*)@(%s)\\b(.*)\\n";
		final String subst = "$2@$3$4 --$3 activated\\\n"; // 17-03-2020

		final String regex = String.format(regexPatt, param);
		return sql.replaceAll(regex, subst);
	}

	/**
	 * '{{namedParam}}' şeklindeki namedParam yerine değerini yazar ve yoruma alınmışsa yorum satırı işaretlerini kaldırır ve aktif olmasını sağlar.
	 *
	 * @param sql
	 * @param param
	 * @param value
	 * @return
	 */
	public static String activateNamedParamsMain(String sql, String param, String value) {
		if (FiString.isEmptyTrim(value) || FiString.isEmptyTrim(param)) return sql;

		final String regex = String.format(" *(--)*(.*)(\\{\\{%s\\}\\})(.*)\\n{0,1}", param);
		final String regexReplaceFormat = String.format("$2%s$4 --%s activated\n", value, param);

		// System.out.println(regex);
		// System.out.println(regexReplaceFormat);
		return sql.replaceAll(regex, regexReplaceFormat);
	}

	public static String deactivateSqlAtParamMain(String sql, String param) {
		final String regexPatt = "(.*)@(%s)\\b(.*)\\n";
		final String subst = "--$1$2$3 --$2 deactivated\\\n";

		final String regex = String.format(regexPatt, param);
		return sql.replaceAll(regex, subst);
	}

	/**
	 * Opt parametreyi aktive eder ve
	 * <p>
	 * Sql String Property değerini de günceller.
	 *
	 * @param sql
	 * @param param
	 */
	public static void activateAndUpdateOptParam(StringProperty sql, String param) {
		sql.set(activateOptParamMain(sql.get(), param));
	}

	/**
	 * Comment içerisinde değişkenler varsa iptal eder.# işareti ile gösterir.
	 * <br>
	 *
	 * @param sql
	 * @return
	 */
	public static String fixSqlProblems(String sql) {
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
	public static String convertSqlParamToListJdbiParamMain(String sql) {
		String sqlNew = sql.replaceAll("\\(.*@(.*)\\)", "(<$1>)");
		return sqlNew;
	}

	public static String makeMultiParamTemplate(String param, Integer index) {
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
	public static String convertSqlForMultiParamByTemplate2(String sql, String param, Integer count) {

		//sadece parentez içinde olanları bulmak için
		//final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
		final String regex = String.format("@%s", param);

		StringBuilder customParam = new StringBuilder();

		for (int index = 0; index < count; index++) {
			if (index != 0) customParam.append(",");
			String sablon = makeMultiParamTemplate(param, index);
			customParam.append("@" + sablon);
		}

		String sqlNew = sql.replaceAll(regex, String.format("%s", customParam)); //(%s)
		return sqlNew;
	}

	/**
	 * '@' leri ':' çevirir.
	 *
	 * @param sql
	 * @return
	 */
	public static String convertSqlParamToJdbiParamMain(String sql) {
		return sql.replaceAll("@", ":");
	}

	public static String convertSqlParamToCommentMain(String key, String sql) {
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
		sqlQuery = fixSqlProblems(sqlQuery);
		sqlQuery = convertSqlParamToJdbiParamMain(sqlQuery);
		return sqlQuery;
	}


	/**
	 * sql sorgu içindeki parametreyi (@paramName), parametre_indexNo şeklinde multi parametreye çevirir. (@paramName_1,@paramName_2,...)
	 * <p>
	 * Örnek @paramName -> @paramName_1 , @paramName_2 , ...
	 */
	public static String convertSqlForMultiParamByTemplate(String sql, String param, Integer count) {

		//sadece parentez içinde olanları bulmak istenirse
		//final String regex = String.format("\\(\\s*@%s\\s*\\)",param);
		final String regex = String.format("@%s", param);

		StringBuilder customParam = new StringBuilder();

		Integer indexParam = getMultiParamStartIndex();

		for (int index = 0; index < count; index++) {
			if (index != 0) customParam.append(",");
			String sablon = makeMultiParamTemplate(param, indexParam);
			customParam.append("@" + sablon);
			indexParam++;
		}

		String sqlNew = sql.replaceAll(regex, customParam.toString()); //(%s)
		return sqlNew;
	}

	public static Set<String> findParamsOptional(String sql) {
		// regexr: Sql Parametre List rg2011201511
		String regEx = "--!(\\w*).*\\n";
		return FiRegExp.matchGroupOneToSet(regEx, sql);
	}

	public static Set<String> findParams(String sql) {
		String regEx = "@(\\w*)";
		return FiRegExp.matchGroupOneToSet(regEx, sql);
	}


	public static Boolean checkOptParamExist(String sql, String param) {

		final String regex240320 = "--!(%s)\\b.*\\s*-*(.*)";
		final String regex = String.format(regex240320, param);

		return FiRegExp.checkPatternExist(sql, regex);
	}

	public static Integer getMultiParamStartIndex() {
		return 0;
	}

	public static Boolean checkParamsEmpty(Object value) {

		// Dolu olanları aktif edecek, boş olanları deaktif edecek
		if (value instanceof String) {
			return FiString.isEmpty((String) value);
		} else if (value instanceof Collection) { // Collection size'a göre karar verecek
			return FiCollection.isEmpty((Collection) value);
		} else { // string ve collection tipinden dışında olanlar, null degilse aktif edilir
			return value == null;
		}

	}

	/**
	 * FiMapParam'da olan parametreleri aktive eder.
	 * <p>
	 * boActivateOnlyFullParams true olursa sadece dolu olan parametreleri aktif eder, parametre dolu değilse (null dahil) deaktif eder.
	 * <p>
	 * Deaktif edilecek parametrelerde FiKeyBean'de bulunmalı
	 * <p>
	 * Dolu olma Şartları : String boş string degilse
	 * <p>
	 * Collection larda size > 0 olmalı
	 * <p>
	 * Diger türler için null olmamalı
	 */
	public static String activateParamsMain(String txQuery, FiKeyBean mapParams, Boolean boActivateOnlyFullParams) {

		List<String> listParamsDeActivated = new ArrayList<>();
		StringProperty spQuery = new SimpleStringProperty(txQuery);

		mapParams.forEach((key, value) -> {

			if (FiBoolean.isTrue(boActivateOnlyFullParams)) {

				// Dolu olanları aktif edecek, boş olanları deaktif edecek
				Boolean boCheckParamsEmpty = FiQueryTools.checkParamsEmpty(value);

				if (FiBoolean.isFalse(boCheckParamsEmpty)) {
					spQuery.set(activateOptParamMain(spQuery.get(), key));
				} else {
					spQuery.set(FiQueryTools.deActivateOptParamMain(spQuery.get(), key));
					listParamsDeActivated.add(key);
				}

			} else { // boActivateOnlyFullParams false veya null ise, tüm parametreleri aktif eder
				spQuery.set(FiQueryTools.activateOptParamMain(spQuery.get(), key));
			}
		});

		for (String deActivatedParam : listParamsDeActivated) {
			mapParams.remove(deActivatedParam);
		}

		return spQuery.get();
	}

	/**
	 * Sorgu içinde bulunan optinal parametrelerden , mapParams kullanılmamışsa deactif eder.
	 *
	 * @param txQuery
	 * @param mapParams
	 * @return
	 */
	public static String deActivateOptParamsNotUsed(String txQuery, FiKeyBean mapParams) {

		//List<String> listParamsWillDeActivate = new ArrayList<>();
		StringProperty spQuery = new SimpleStringProperty(txQuery);
		Set<String> hstParamsOptional = findParamsOptional(txQuery);

		for (String optParam : hstParamsOptional) {

			if (!mapParams.containsKey(optParam)) {
				//listParamsWillDeActivate.add(optParam);
				spQuery.set(FiQueryTools.deActivateOptParamMain(spQuery.get(), optParam));
				//mapParams.remove(deActivatedParam);
			}

		}

		return spQuery.get();
	}

	public static String convertListParamToMultiParams(String txQuery,FiKeyBean mapParams,Boolean boKeepOldMultiParamInFkb) {

		if (mapParams == null) return txQuery;

		// (1) List türündeki parametreler bulunur.
		List<String> listMultiParamsName = new ArrayList<>();
		mapParams.forEach((param, value) -> {

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
		StringProperty spQuery = new SimpleStringProperty(txQuery);

		for (String param : listMultiParamsName) {
			Collection paramCollection = (Collection) mapParams.get(param);
			String queryNew = convertSqlFromSingleParamToMultiParam(spQuery.get(), mapParams, param, paramCollection, boKeepOldMultiParamInFkb);
			spQuery.set(queryNew);
		}

		return spQuery.get();
	}

	/**
	 * Collection (List,Set) değerindeki parametreyi abc_1,abc_2 gibi multi parametreye çevirir
	 *
	 * @param param
	 * @param collParamData
	 * @param boKeepOldParam
	 */
	private static String convertSqlFromSingleParamToMultiParam(String txQuery, FiKeyBean mapParams, String param, Collection collParamData, Boolean boKeepOldParam) {

		// (1) şablona göre yeni eklenecek parametre listesi
		FiKeyBean paramsNew = new FiKeyBean();
		StringBuilder sbNewParamsForQuery = new StringBuilder();

		int index = 0;
		for (Object listDatum : collParamData) {
			String sablonParam = FiQueryTools.makeMultiParamTemplate(param, index);
			if (index != 0) sbNewParamsForQuery.append(",");
			sbNewParamsForQuery.append("@" + sablonParam);
			paramsNew.put(sablonParam, listDatum);
			index++;
		}
		// end-1

		// Sorgu cümlesi güncellenir (eski parametre çıkarılır , yeni multi parametreler eklenir.)
		// setTxQuery(fhrConvertSqlForMultiParamByTemplate(getTxQuery(), param, collParamData.size()));
		String sqlNew = txQuery.replaceAll("@" + param, sbNewParamsForQuery.toString()); //(%s)

		// map paramden eski parametre çıkarılıp, yenileri eklenir
		if (!FiBoolean.isTrue(boKeepOldParam)) {
			mapParams.remove(param);
		}

		mapParams.putAll(paramsNew);

		return sqlNew;
	}

	/**
	 * 1 fhrDeActivateAllOptionalParams
	 * <p>
	 * 2 fixSqlProblems
	 * <p>
	 * Önce opsiyonel parametreleri deaktivite yapar , daha sonra sql problemleri götürür
	 * Daha sonra @ ifadelerini : ye çevirir.
	 *
	 * <br>
	 * Deaktivite Yapacağı örnek satır. Ünlem işareti ile parametre olan satırın alt satırını siler
	 * <br>--!cari_sektor_kodu
	 * <br>AND ch.cari_sektor_kodu IN (@cari_sektor_kodu)
	 * <p>
	 *
	 */
	public static String fimSqlQueryWithDeActType1(String sqlQuery) {
		sqlQuery = deActivateAllOptParams(sqlQuery);
		sqlQuery = fixSqlProblems(sqlQuery);
		return convertSqlParamToJdbiParamMain(sqlQuery);
	}

	/**
	 * 1 fhrDeActivateAllOptionalParams
	 * <p>
	 * 2 fixSqlProblems
	 * <p>
	 * (!!! fhrConvertSqlParamToJdbi yapmıyor)
	 *
	 * @param sql
	 * @return
	 */
	public static String fhrFixAndDeActivateOptParams(String sql) {
		return fixSqlProblems(deActivateAllOptParams(sql));
	}

	/**
	 * Sorguda bulunan __userParam şeklindeki user parametrelerini bulur
	 *
	 * iki alt çizgi seçilmesinin nedeni, değişken tanımlarında _ alt çizgiye izin veriyor oluşu.
	 * <p>
	 * Bu parametreler eğer mapParams'da var ise, değeri yer değiştirir.
	 */
	public static String convertUserParamsToValue(String txQuery,FiKeyBean mapParams,String txUserParamPrefix) {

		if (mapParams.isEmpty()) return txQuery;

		// *** sorguda user_param sayısı tespit edilir, eğer varsa işlemler yapılır.
		String txPattern = "\\b" + txUserParamPrefix + "\\w+\\b";
		Set<String> setUserParam = FiRegExp.matchGroupZeroToSet(txPattern, txQuery);

		if (!setUserParam.isEmpty()) {
			for (String txUserParam : setUserParam) {
				String sqlParam = txUserParam.substring(txUserParamPrefix.length()); // txUserParam.length() parametre çıkarıldı
				//System.out.println("sqlparam:" + sqlParam);
				if (mapParams.containsKey(sqlParam)) {
					Object paramValue = mapParams.get(sqlParam);
					if (paramValue != null) {
						// URFIX paramvalue sql injection engellebilir
						String upQuery = txQuery.replaceAll(String.format("\\b%s%s\\b", txUserParamPrefix, sqlParam), paramValue.toString());
						return upQuery;
					} else {
						//getTxQuery().replaceAll(String.format("\\b__%s\\b", txUserParam), "NULL");
					}
				}
			}
		}

		return txQuery;
	}



}
