package ozpasyazilim.utils.core;

import javafx.beans.property.StringProperty;
import ozpasyazilim.utils.fidborm.FiQueryTools;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Syntax Anlamlar
 * <p>
 * Jdbi Param :param_name
 * <p>
 * Sql Param @param_name
 * <p>
 * Opt Param --!param_name
 */
public class OzFormatter {

	private String value;
	private Map<String, Object> namedArguments;  // = new HashMap();
	private List<Object> posArguments; // = new ArrayList();

	private OzFormatter(String value) {
		this.value = value;
	}

	public static OzFormatter ofm(String str) {
		return new OzFormatter(str);
	}

	public static OzFormatter ofm(String str, Object... args) {
		OzFormatter ozf = new OzFormatter(str);
		if (args != null) {
			ozf.posArguments = Arrays.asList(args);
		}
		return ozf;
	}

	public static String fimSqlAtTire(String sqlQuery) {
		if (sqlQuery == null) return null;
		sqlQuery = FiQueryTools.fixSqlProblems(sqlQuery);
		sqlQuery = sqlQuery.replaceAll("@_", ":");
		return sqlQuery;
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
	 */
	public static String fimSqlQueryWithDeActType1(String sqlQuery) {
		sqlQuery = FiQueryTools.deActivateAllOptParams(sqlQuery);
		sqlQuery = FiQueryTools.fixSqlProblems(sqlQuery);
		return FiQueryTools.convertSqlParamToJdbiParamMain(sqlQuery);
	}

//	/**
//	 * Clp => (convertListParam)
//	 * <p>
//	 * 1 Opsiyonel parametreleri deaktivite yapar
//	 * <p>
//	 * 2 Parentez içindeki sql parametreyi jdbi list parametresine dönüştürür (<...>)
//	 * <p>
//	 * 3 @ ifadelerini : ye çevirir.
//	 * <br>
//	 * 1 fhrDeActivateAllOptionalParams
//	 * <p>
//	 * 2 fhrConvertSqlParamToListParamJdbi
//	 * <p>
//	 * 3 fhrConvertSqlParamToJdbi
//	 * <p>
//	 * Deaktivite Yapacağı örnek satır. Ünlem işareti ile parametre olan satırın alt satırını siler
//	 * <br>--!cari_sektor_kodu
//	 * <br>AND ch.cari_sektor_kodu IN (@cari_sektor_kodu)
//	 */
//	public static String fimSqlQueryDeActType2WitClp(String sqlQuery) {
//		sqlQuery = FiQuery.deActivateAllOptParams(sqlQuery);
//		sqlQuery = FiQuery.fhrConvertSqlParamToListJdbiParamMain(sqlQuery);
//		sqlQuery = FiQuery.fhrConvertSqlParamToJdbiParamMain(sqlQuery);
//		return sqlQuery;
//	}

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
		return FiQueryTools.fixSqlProblems(FiQueryTools.deActivateAllOptParams(sql));
	}

	public static void main(String[] args) {

		String sql1 = "and chh.cha_belge_tarih>=@cha_tarihi1\n" +
				"and chh.cha_belge_tarih<=@cha_tarihi2 \n" +
				"--!cha_special3\n" +
				"and ISNULL(chh.cha_special3,'') <> 'EFB'\n" +
				"GROUP BY cha_evrakno_seri,cha_evrakno_sira,cha_evrak_tip";

		String sql2 = "--sq200307_1244\n" +
				"select mainQuery.*\n" +
				"FROM (\n" +
				"Select ROW_NUMBER() OVER(order by ecb.cha_RECno DESC) As lnRowNo\n" +
				",chh.*,ch.cari_unvan1,ch.cari_vdaire_no\n" +
				"FROM CARI_HESAP_HAREKETLERI_ENTEGRE ecb\n" +
				"LEFT JOIN CARI_HESAPLAR ch ON ch.cari_kod = ecb.cha_kod \n" +
				"LEFT JOIN OZV_CARI_HESAP_EVRAKLAR_OZEL chh\n" +
				"ON  chh.cha_evrak_tip = ecb.cha_evrak_tip \n" +
				"    and chh.cha_evrakno_seri = ecb.cha_evrakno_seri\n" +
				"    and chh.cha_evrakno_sira = ecb.cha_evrakno_sira\n" +
				"WHERE 1=1 \n" +
				"    AND ecb.cha_evrak_tip = @cha_evrak_tip\n" +
				"    --!cari_unvan1\n" +
				"    --AND LOWER(ch.cari_unvan1) LIKE LOWER(@cari_unvan1)\n" +
				"    --!cha_kod\n" +
				"    --AND LOWER(ecb.cha_kod) LIKE LOWER(@cha_kod)\n" +
				"    --!cha_belge_no\n" +
				"    --AND LOWER(ecb.cha_belge_no) LIKE LOWER(@cha_belge_no)\n" +
				"    --!cha_evrakno_seri\n" +
				"    --AND LOWER(ecb.cha_evrakno_seri) LIKE LOWER(@cha_evrakno_seri)\n" +
				"    --!cha_evrakno_sira\n" +
				"    --AND ecb.cha_evrakno_sira = @cha_evrakno_sira\n" +
				"    --!cari_vdaire_no\n" +
				"    --AND ch.cari_vdaire_no = @cari_vdaire_no\n" +
				"    --!cha_uuid\n" +
				"    --AND LOWER(ecb.cha_uuid) LIKE LOWER(@cha_uuid)\n" +
				") as mainQuery\n" +
				"where mainQuery.lnRowNo >= @lnBegin and mainQuery.lnRowNo <= @lnEnd";

		String sqlTest = sql2;

		String demo1 = new OzFormatter(sqlTest).buildDeActivateOptionalParam("cha_kod").getValue();

		System.out.println("\n\nDeActivate Optional Param(chakod):" + demo1);

		String demo2 = new OzFormatter(sqlTest).buildActivateOptionalParamV1("cha_kod").getValue();

		System.out.println("\n\nActivate Optional Param(chakod):" + demo2);

		String demo3 = new OzFormatter(sqlTest).buildDeActivateAllOptParams().getValue();

		System.out.println("\n\nDeActivate All Optional Params:" + demo3);

	}

	public static void fhrInAssignIntListToSqlParam(StringProperty spSql, List<Integer> cha_evrak_tipList, String param) {
		String sqlParam = "IN ( " + FiCollection.commaSeperatedDeParseIntList(cha_evrak_tipList) + " )";
		spSql.set(spSql.get().replaceAll("=.*@" + param, sqlParam));
	}

	public static void fhrNotInAssignIntListToSqlParam(StringProperty spSql, List<Integer> cha_evrak_tipList, String param) {
		String sqlParam = "NOT IN ( " + FiCollection.commaSeperatedDeParseIntList(cha_evrak_tipList) + " )";
		spSql.set(spSql.get().replaceAll("=.*@" + param, sqlParam));
	}

	/**
	 * {named_parameter} ları prm nin değerine çevirir.
	 * <p>
	 * {adi} --> adi=veli --> veli
	 *
	 * @return
	 */
	public String namedFormatter() {

		String strformatted = value;

		if (getNamedArguments().size() > 0) {
			for (Map.Entry<String, Object> entry : getNamedArguments().entrySet()) {
				strformatted = strformatted.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
			}
		}

		return strformatted;
	}

	public String numfmt() {

		String strformmatted = value;

		if (getPosArguments().size() > 0) {
			for (Integer index = 0; index < getPosArguments().size(); index++) {
				//System.out.println(entry.getKey() + "/" + entry.getValue());
				//String key = entry.getKey();
				strformmatted = strformmatted.replaceAll("\\{" + index.toString() + "\\}", getPosArguments().get(index).toString());
			}
		}

		return strformmatted;
	}

	/**
	 * @return
	 * @p0, @p1 değişkenleri posArguments listesindeki elemanlar ile değiştiriyor. Başına : koyar. :element
	 */
	public String sqlfmtPnumber() {
		//StringBuilder result = new StringBuilder();
		//StringBuilder param = new StringBuilder(16);
		//AlephFormatter.State state = AlephFormatter.State.FREE_TEXT;
		String strformatted = value;

		if (getPosArguments().size() > 0) {
			for (Integer index = 0; index < getPosArguments().size(); index++) {
				//System.out.println(entry.getKey() + "/" + entry.getValue());
				//String key = entry.getKey();
				strformatted = strformatted.replaceAll("@p" + index.toString(), ":" + getPosArguments().get(index).toString());
			}
		}

		return strformatted;
	}

	/**
	 * @return
	 * @_ ifadelerini : ye çevirir.
	 * <p>
	 * instead use FmtAt
	 */
	@Deprecated
	public String sqlFmtConvertAtDash() {
		//StringBuilder result = new StringBuilder();
		//StringBuilder param = new StringBuilder(16);
		//AlephFormatter.State state = AlephFormatter.State.FREE_TEXT;
		String strformatted = value;

		//System.out.println(entry.getKey() + "/" + entry.getValue());
		//String key = entry.getKey();
		strformatted = strformatted.replaceAll("@_", ":");

		return strformatted;
	}

	/**
	 * '@' karakterini ':' ya çevirir.
	 *
	 * @return
	 */
	public String sqlFmtAt() {
		this.value = value.replaceAll("@", ":");
		return this.value;
	}

	// ???????
	public String sqlListAtOld() {

		//String strformatted = str;
		String regex = "(?s)\\(@(.*?)\\)";  //?s includes newline

		//	Pattern.compile("(?<myGroup>[A-Za-z])[0-9]\\k<myGroup>")
		//	.matcher("a9a c0c d68")
		//	.find();//matches:  'a9a' at 0-3, 'c0c' at 4-7
		//	//'a9a c0c d68'

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(this.value);

		this.value = matcher.replaceAll("(<$1>)");

		this.value = this.value.replaceAll("@", ":");

		return this.value;
	}

	/**
	 * sql içindeki variable jdbi list formatına çevirir.
	 * <p>
	 * parentez içindeki @ işaretlerini  <param> formatına çevirir
	 * <p>
	 * ( @xxx ) --> ( <xxx> )
	 */
	private void fhrConvertSqlParamToListJdbiParam() {
		// [Ii][Nn].* başına eklenebilir
		this.value = FiQueryTools.convertSqlParamToListJdbiParamMain(this.value); //value.replaceAll("\\(.*@(.*)\\)", "(<$1>)");
	}

	public String sqlListAt() {
		//String regex = "(?s)\\(@(.*?)\\)";  //?s includes newline
		fhrFixSqlProblems();
		fhrConvertSqlParamToListJdbiParam();
		this.value = this.value.replaceAll("@", ":"); // [Ii][Nn].* başına eklenebilir
		return value;
	}

	private void fhrFixSqlProblems() {
		this.value = FiQueryTools.fixSqlProblems(this.value); //str.replaceAll("(--.*)(@)", "$1#");
	}

	public String getValue() {
		return value;
	}

	/**
	 * {0} {1} {2} sayısal yer imlerini , (pos) argünmanlar ile yer değiştirir.
	 * Dikkat 0 dan başlanmalı
	 *
	 * @return
	 */
	public String fmt() {

		if (getPosArguments().size() > 0) {
			return numfmt();
		}
		return value;
	}

	public String generate() {

		if (getPosArguments().size() > 0) {
			return numfmt();
		}

		if (getNamedArguments().size() > 0) {
			return namedFormatter();
		}

		return value;
	}


	public OzFormatter putNamedd(String argName, Object object) {
		//this.failIfArgExists(argName);
		getNamedArguments().put(argName, object);
		return this;
	}

	/**
	 * Yer Belirteci olarak ! kullanıldı,
	 * <br> $ ın özel anlamı var reg exp da.
	 *
	 * @param key
	 * @return
	 */
	public OzFormatter buildActivateSingleLineAndFix(String key) {

		if (!FiType.isEmptyWithTrim(key)) {
			this.value = this.value.replaceAll("--!" + key, "");
		}

		this.value = FiQueryTools.fixSqlProblems(value);   //value.replaceAll("(--.*)(@)", "$1#");
		return this;
	}

	public Map<String, Object> getNamedArguments() {
		if (namedArguments == null) {
			this.namedArguments = new HashMap<>();
		}
		return namedArguments;
	}

	public List<Object> getPosArguments() {
		if (posArguments == null) {
			posArguments = new ArrayList<>();
		}
		return posArguments;
	}

	/**
	 * --%s leri arguman dizisi ile sırasıyla değiştirir.
	 *
	 * @param txValue
	 * @param args
	 * @return
	 */
	public static String fhrConvertSqlCommentPercToStr(String txValue, Object... args) {

		if (txValue == null) return null;

		String strformmatted = txValue;

		if (args.length > 0) {
			for (Integer index = 0; index < args.length; index++) {
				strformmatted = strformmatted.replaceFirst("--%s", args[0].toString());
			}
		}

		return strformmatted;
	}

	public OzFormatter buildDeActivateAllOptParams() {
		this.value = FiQueryTools.deActivateAllOptParams(value);
		return this;
	}

	public OzFormatter buildDeActivateOptionalParam(String param) {
		this.value = FiQueryTools.deActivateOptParamMain(value, param);
		return this;
	}

	public OzFormatter buildActivateOptionalParamV1(String param) {
		this.value = FiQueryTools.activateOptParamMain(this.value, param);
		return this;
	}

	public OzFormatter fhrConvertSqlParamToComment(String key) {
		String newSql = FiQueryTools.convertSqlParamToCommentMain(key, value);
		if (newSql != null) this.value = newSql;
		return this;
	}


}