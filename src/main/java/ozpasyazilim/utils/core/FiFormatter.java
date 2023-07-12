package ozpasyazilim.utils.core;

import ozpasyazilim.utils.fidborm.FiQueryTools;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * namedArguments veya posArguments (sıraya göre) göre string yer değiştirme yapar.
 *
 * Sql ile ilgili metodlar kaldırılacak
 */
public class FiFormatter {

    private String value;
    private Map<String, Object> namedArguments;
    private List<Object> posArguments;

    private FiFormatter(String value) {
        this.value = value;
    }

    public static FiFormatter fif(String str) {
        return new FiFormatter(str);
    }

    public static FiFormatter fif(String str, Object... args) {
        FiFormatter ozf = new FiFormatter(str);
        if (args != null) {
            ozf.posArguments = Arrays.asList(args);
        }
        return ozf;
    }

    @Deprecated
    public static String fimSqlAtTire(String sqlQuery) {
        if (sqlQuery == null) return null;
        sqlQuery = FiQueryTools.fixSqlProblems(sqlQuery);
        sqlQuery = sqlQuery.replaceAll("@_", ":");
        return sqlQuery;
    }

    /**
     * {named_parameter} 'leri string değerine çevirir.
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

    /**
     * namedParametreleri ({...}) string değerleri ile yer değiştirir.
     * <p>
     * Source : https://www.baeldung.com/java-string-formatting-named-placeholders
     *
     * @return
     */
    public String namedFormatter2() {  //String template, Map<String, Object> parameters
        StringBuilder newTemplate = new StringBuilder(getValue());
        List<Object> valueList = new ArrayList<>();

        //Matcher matcher = Pattern.compile("[$][{](\\w+)}").matcher(getTxTemplate());
        Matcher matcher = Pattern.compile("[{](\\w+)}").matcher(getValue());

        // String'de bulunan key'lerin yerine %s eklemiş, bir sonraki adımda %s leri String.format ile yer değiştirmiş
        while (matcher.find()) {
            String key = matcher.group(1);

            //String paramName = "${" + key + "}";
            String paramName = "{" + key + "}";
            int index = newTemplate.indexOf(paramName);
            if (index != -1) {
                newTemplate.replace(index, index + paramName.length(), "%s");
                valueList.add(getNamedArguments().get(key));
            }
        }

        return String.format(newTemplate.toString(), valueList.toArray());
    }

    public String numFmt() {

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
     * '@' karakterini ':' ya çevirir.
     *
     * @return
     */
    @Deprecated
    public String sqlFmtAt() {
        this.value = value.replaceAll("@", ":");
        return this.value;
    }

    // ???????
    @Deprecated
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

    @Deprecated
    public String sqlListAt() {
        //String regex = "(?s)\\(@(.*?)\\)";  //?s includes newline
        this.value = FiQueryTools.fixSqlProblems(this.value); //str.replaceAll("(--.*)(@)", "$1#");
        this.value = FiQueryTools.convertSqlParamToListJdbiParamMain(this.value); //value.replaceAll("\\(.*@(.*)\\)", "(<$1>)");
        this.value = this.value.replaceAll("@", ":"); // [Ii][Nn].* başına eklenebilir
        return value;
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
            return numFmt();
        }
        return value;
    }

    public String generate() {

        if (getPosArguments().size() > 0) {
            return numFmt();
        }

        if (getNamedArguments().size() > 0) {
            return namedFormatter();
        }

        return value;
    }


    public FiFormatter putNamed(String argName, Object object) {
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
    @Deprecated
    public FiFormatter buildActivateSingleLineAndFix(String key) {

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
    // use FiQueryTools
    @Deprecated
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

    // use FiQueryTools
    @Deprecated
    public FiFormatter fhrConvertSqlParamToComment(String key) {
        String newSql = FiQueryTools.convertSqlParamToCommentMain(key, value);
        if (newSql != null) this.value = newSql;
        return this;
    }


}