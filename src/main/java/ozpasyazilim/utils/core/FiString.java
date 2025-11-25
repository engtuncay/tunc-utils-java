package ozpasyazilim.utils.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.StrSubstitutor;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.log.Loghelper;

public class FiString {

    public static Locale locale = new Locale("tr", "TR");

    public static void main(String[] args) {

        Loghelper.installLogger(true);

        //System.out.println(FiString.clearIllegalCharactersOtherThanAlphaNumeric("32f343%d2"));

        final String text = "--{abc}asdas\n"
                + "dasdasd\n"
                + "asdasd\n"
                + "adasda\n"
                + "{dd}\n"
                + "dasdasdas\n"
                + "asddasdassdas\n";

        System.out.println(FiString.stringPaddingFirst("1245687", 6, '0'));
        System.out.println(FiString.clearFirstChars("00010", '0'));

        //FiConsole.debug(FiString.findCurlyParanthesisVars(text));
        //System.out.println(FiString.findCurlyParanthesisVars(text));

        //System.out.println(FiString.splitDoubleParanthesisFirst("{{abc}}{{dd}}"));

        //System.out.println("1234 : " + FiString.cropString("1234",10));

//		String sqOZV_CARI_HESAP_HAREKETLER_ENT2 = "IF object_id(N'dbo.OZV_CARI_HESAP_HAREKETLER_ENT2', N'V') IS NOT NULL\n" +
//				"BEGIN\n" +
//				"    DROP VIEW dbo.OZV_CARI_HESAP_HAREKETLER_ENT2\n" +
//				"END\n" +
//				"\n" +
//				"GO\n" +
//				"\n" +
//				"--sq1910311605\n" +
//				"--v1.1\n" +
//				"CREATE VIEW dbo.OZV_CARI_HESAP_HAREKETLER_ENT2\n" +
//				"AS\n" +
//				"    SELECT chh.*,dbo.OzFns_CariHesapAdi(cha_kod,cha_cari_cins) cari_unvan1\n" +
//				"    ,dbo.OzFns_CariHesapAdi( chh.cha_kasa_hizkod,chh.cha_kasa_hizmet) cari_unvan1_karsi,cpt.cari_per_adi \n" +
//				"    FROM OZV_CARI_HESAP_HAREKETLER_ENT chh\n" +
//				"    LEFT JOIN CARI_PERSONEL_TANIMLARI cpt on chh.cha_satici_kodu = cpt.cari_per_kod\n";
//
//
//		String[] split = FiString.split(sqOZV_CARI_HESAP_HAREKETLER_ENT2, "GO.*\\s*");
//		FiConsole.debug(split);
    }

    public static boolean isEmpty(String strvalue) {
        if (strvalue == null) return true;
        if (strvalue.equals("")) return true;
        return false;
    }

    public static boolean isEmptyTrim(String strvalue) {
        if (strvalue == null) return true;
        if (strvalue.trim().equals("")) return true;
        return false;
    }

    public static boolean isEmptyToString(Object strvalue) {
        if (strvalue == null) return true;
        if (strvalue.toString().equals("")) return true;
        return false;
    }

    public static boolean isEmptyToStringWithTrim(Object strvalue) {
        if (strvalue == null) return true;
        if (strvalue.toString().trim().equals("")) return true;
        return false;
    }

    public static boolean isNotEmpty(String strvalue) {
        return !isEmpty(strvalue);
    }


    public static String stringPaddingFirst(String toPad, Integer width, char fill) {

        // kendisi width büyükse padding yapmadan dönüş yapra
        if (toPad.length() > width) {
            return toPad;
        }

        String padded = new String(new char[width - toPad.length()]).replace('\0', fill) + toPad;
        return padded;
    }

    public static String stringPaddingFirst(String toPad, Integer width) {
        return stringPaddingFirst(toPad, width, ' ');
    }

    public static String stringPaddingEnd(String toPad, Integer width, char fill) {

        if (toPad.length() > width) {
            String padded = toPad.substring(0, width - 3) + "...";
            return padded;
        }

        //Loghelperr.getInstance(FiString.class).debug("eklecek : "+ (width-toPad.length()));

        String padded = toPad + new String(new char[width - toPad.length()]).replace('\0', fill);
        return padded;

    }

    public static String stringPaddingEnd(String toPad, Integer width) {
        return stringPaddingEnd(toPad, width, ' ');
    }

    public static String regexChangePatternCaseInsensitive(String spText) {
        // ğüşiöç türkçe karakterleri büyük küçük varyasyonları için regex şablona
        // çevrildi

        String strText = new String(spText);
        strText.replaceAll("i", "[İi])");
        strText.replaceAll("İ", "[İi])");
        strText.replaceAll("ş", "[Şş])");
        strText.replaceAll("Ş", "[Şş]");
        strText.replaceAll("ç", "[Çç]");
        strText.replaceAll("Ç", "[Çç]");
        strText.replaceAll("ö", "[Öö]");
        strText.replaceAll("Ö", "[Öö]");
        strText.replaceAll("ğ", "[Ğğ]");
        strText.replaceAll("Ğ", "[Ğğ]");
        strText.replaceAll("ü", "[Üü]");
        strText.replaceAll("Ü", "[Üü]");
        return strText;
    }

    public static String replaceTurkishCharacterstoLatin(String spText) {
        String strText = new String(spText);
        strText = strText.replaceAll("İ", "I");
        strText = strText.replaceAll("ş", "s");
        strText = strText.replaceAll("Ş", "S");
        strText = strText.replaceAll("ç", "c");
        strText = strText.replaceAll("Ç", "C");
        strText = strText.replaceAll("ö", "o");
        strText = strText.replaceAll("Ö", "O");
        strText = strText.replaceAll("ğ", "g");
        strText = strText.replaceAll("Ğ", "G");
        strText = strText.replaceAll("ü", "u");
        strText = strText.replaceAll("Ü", "U");
        strText = strText.replaceAll("ı", "i");
        return strText;
    }

    public static String replaceTurkishCharacterstoLatinWithLowerI(String spText) {
        String strText = new String(spText);
        strText = strText.replaceAll("İ", "I");
        strText = strText.replaceAll("ı", "i");
        strText = strText.replaceAll("ş", "s");
        strText = strText.replaceAll("Ş", "S");
        strText = strText.replaceAll("ç", "c");
        strText = strText.replaceAll("Ç", "C");
        strText = strText.replaceAll("ö", "o");
        strText = strText.replaceAll("Ö", "O");
        strText = strText.replaceAll("ğ", "g");
        strText = strText.replaceAll("Ğ", "G");
        strText = strText.replaceAll("ü", "u");
        strText = strText.replaceAll("Ü", "U");
        return strText;
    }


    public static String subString(String txValue, int i) {

        if (i > txValue.length()) i = txValue.length();
        String newstring = txValue.substring(0, i);
        return newstring;

    }

    public static String cutStringAndReturnResidual(String unvan, int i) {

        String kalan = unvan.substring(i, unvan.length());
        unvan = unvan.substring(0, i - 1);
        return kalan;
    }

    public static String truncateDoublevaluedotzero(String strDoubleValue) {

        Pattern pattern = Pattern.compile("(\\d*)" + "\\.0$");
        Matcher matcher = pattern.matcher(strDoubleValue);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "-1";
        }

    }

    public static String truncateServerPort(String strServerName) {

        Pattern pattern = Pattern.compile("(.*)" + ":.*$");
        Matcher matcher = pattern.matcher(strServerName);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "-1";
        }

    }

    public static String escapePathString(String pString) {

        String escaped = pString;
        escaped = escaped.replaceAll("\b", "\\");
        return escaped;
    }

    public static String clearIllegalCharactersOtherThanAlphaNumeric(String pString) {
        // var cleanString = dirtyString.replace(/[|&;$%@"<>()+,]/g, "");
        String escaped = pString;
        escaped = escaped.replaceAll("[^\\w]", "");
        escaped = escaped.replaceFirst("[0-9]*", "");
        return escaped;
    }

    public static String getMatchGroupFirst(String sRegex, String sText) {

        Pattern pattern = Pattern.compile(sRegex);
        Matcher matcher = pattern.matcher(sText);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }

    }

    public static Double formatlafloat(Number number) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

        Double dblnumber = Double.valueOf(Double.parseDouble(decimalpattern.format(number)));
        return dblnumber;
    }

    public static String formatlaNumbertoString2(Float flonumber) {

        if (flonumber == null) {
            return "0.00";
        }

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

        String strdblnumber = decimalpattern.format(flonumber);
        return strdblnumber;
    }

    public static String formatNumberParagosterimi(Number number) {
        if (number == null) return "";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
        String strnumber = decimalpattern.format(number);
        return strnumber;
    }

    public static String formatNumberParagosterimi(Object number) {
        if (number == null) return "";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
        String strnumber = decimalpattern.format(number);
        return strnumber;
    }

    public static String[] split(String value, String regex) {
        String[] split = value.split(regex);
        return split;
    }

    public static List<String> findCurlyParanthesisVars(String text) {

        final String regex = "\\{(.*)\\}";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(text);

        List<String> list = new ArrayList<>();

        while (matcher.find()) {
            //System.out.println("Full match: " + matcher.group(0)); // full parantezle birlikte verir
            for (int i = 1; i <= matcher.groupCount(); i++) {
                //System.out.println("Group " + i + ": " + matcher.group(i));
                list.add(matcher.group(i));
            }
        }

        return list;
    }

    @FiDraft()
    public static String splitDoubleParanthesisFirst(String key) {

        // make your * to nongreedy/reluctant -->  .* greedy , .*? non greedy
        Pattern pattern = Pattern.compile("\\{\\{.*?\\}\\}");
        Matcher matcher = pattern.matcher(key);

        String[] values = new String[2];

        int i = 0;
        while (matcher.find()) {
            String grup = matcher.group();
            if (grup != null && grup.length() > 2) values[i] = grup.substring(2, grup.length() - 2);
            i++;
        }

        if (matcher.find()) {
            //return matcher.group(1);
            String grup = matcher.group();
            return grup.substring(2, grup.length() - 2);
        } else {
            return null;
        }

        // find multiple occurrences
        //	    String s = "Hello my name is Neo and my company brand name is Company Country1. "
        //                + "But we have also other companies like Company Country2 (in Europe), "
        //                + "and also Company Country3 (in Asia)";
        //
        //        Pattern pattern = Pattern.compile("Company Country\\d");
        //        Matcher matcher = pattern.matcher(s);
        //        while (matcher.find()) {
        //            String group = matcher.group();
        //            System.out.println(group);
        //        }

    }

    public static String firstLetterUpperRestLower(String data) {
        if (data.equals("")) return "";
        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public static String firstLetterUpperOnly(String txValue) {
        if (txValue == null) return null;
        if (txValue.equals("")) return "";
        String firstLetter = txValue.substring(0, 1).toUpperCase();
        String restLetters = txValue.substring(1);
        return firstLetter + restLetters;
    }

    public static String firstLetterLowerOnly(String data) {
        if (data.equals("")) return "";
        String firstLetter = data.substring(0, 1).toLowerCase();
        String restLetters = data.substring(1);
        return firstLetter + restLetters;
    }


    public static String trimFieldNameWithEngAccent(String header) {

        header = header.replaceAll("\\s", "");
        header = header.toLowerCase();
        header = replaceTurkishCharacterstoLatinWithLowerI(header);

        //Loghelperr.getInstance(FiString.class).debug(" Header:"+ header);

        return header;
    }

    /**
     * If Exists crop string by cropAmount, if not exists then return the string
     *
     * @param text
     * @param cropAmount
     * @return
     */
    public static String cropStringFromBegin(String text, int cropAmount) {
        if (FiString.isEmpty(text)) return text;
        return text.substring(0, Math.min(text.length(), cropAmount));
    }

    /**
     * sondan başlayarak cropAmount kadar olan text'i alır. Mesela son 50 karekter gibi.
     *
     * @param text
     * @param cropAmount
     * @return
     */
    public static String cropStringFromEnd(String text, int cropAmount) {
        if (text == null) return null;
        if(text.length()>cropAmount){
            return text.substring(text.length()-cropAmount);
        }
        return text;
    }

    /**
     * null ise boş string döner <br>
     * null değilse değerin kendisini döner
     *
     * @param value
     * @return
     */
    public static String orEmpty(String value) {
        if (value == null) return "";
        return value;
    }

//	public static String orEmpty(String value) {
//		if (value == null) return "";
//		return value;
//	}

    public static String ToStrOrEmpty(Object value) {
        if (value == null) return "";

        return value.toString();
    }

    public static Object orEmptyStrElseObj(Object value) {
        if (value == null) return "";

        return value;
    }

    public static String orEmpty(Object value) {
        if (value == null) return "";

        return value.toString();
    }

    /**
     * null ise elseValue değerini dönderir <br>
     * null degilse kendi değerini döner
     *
     * @param value
     * @param elseValue
     * @return
     */
    public static String orElse(String value, String elseValue) {

        if (value == null) return elseValue;

        return value;

    }

    public static String ifEmptyElseValue(String value, String elseValue) {

        if (isEmpty(value)) return elseValue;

        return value;

    }

    private static final Pattern INVALID_CHARS_PATTERNWITH_SPACE = Pattern.compile("^.*[~#@*+%{}<>\\[\\]|\"\\_\\s].*$");

    public static Boolean checkIllegalCharsIncludeSpace(String value) {
        return INVALID_CHARS_PATTERNWITH_SPACE.matcher(value).matches();
    }

    /**
     * FiGuid e taşındı
     *
     * @return
     */
    @Deprecated
    public static String genUuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String clearFirstZeros(String value) {
        return clearFirstChars(value, '0');
    }

    public static String clearFirstChars(String value, char clearChar) {
        if (value == null) return null;
        return value.replaceAll("^" + clearChar + "*", "");
    }

    public static String clearFirstZerosIfNullElseValue(String value, String elseValue) {
        if (value == null) return elseValue;
        return value.replaceAll("^0*", "");
    }

    public static String[] convertListToArray(List<String> list) {
        if (list == null || list.size() == 0) return null;

        String[] stArray = new String[list.size()];
        int index = 0;
        for (String value : list) {
            stArray[index] = value;
            index++;
        }
        return stArray;
    }


    public static String ifNullThenEmpty(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    public static String ifNullThenNull(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    public static String ifNullStringThenNullChar(Object value) {
        if (value == null) return "null";
        return value.toString();
    }

    public static List<String> getListStringOneEmpty() {
        List<String> list = new ArrayList<>();
        list.add("");
        return list;
    }

    public static String getOneSpaceifNotEmpty(String egk_evracik1) {
        if (!FiString.isEmpty(egk_evracik1)) return " ";
        return "";
    }

    public static String getCommaIfNotEmpty(String value) {
        if (!FiString.isEmptyTrim(value)) return ",";
        return "";
    }

    public static String getDashIfNotEmpty(String value) {
        if (!FiString.isEmptyTrim(value)) return "-";
        return "";
    }

    public static String cevir(String metin) {
        if (metin == null || metin.isEmpty())
            return metin;

        return cevir(metin.substring(1)) + metin.charAt(0);
    }

    /**
     * Null değer gelirse empty string döner
     * <p>
     * eğer dolu ise sonuna yeni satır ekleyip döner
     *
     * @param txValue
     * @return
     */
    public static String addNewLineToEndIfNotEmpty(String txValue) {
        if (!FiString.isEmptyTrim(txValue)) {
            txValue = txValue + "\n";
            return txValue;
        }
        return "";
    }

    public static String getNewLineIfFull(String txValue) {
        if (!FiString.isEmptyTrim(txValue)) {
            return "\n";
        }
        return "";
    }

    /**
     * Boş degilse başına yeni satır işareti ekler
     *
     * @param txValue
     * @return
     */
    public static String addNewLineToBeginIfNotEmpty(String txValue) {
        if (!FiString.isEmptyTrim(txValue)) {
            txValue = "\n" + txValue;
            return txValue;
        }
        return "";
    }

    public static String toStringOrEmpty(Object obj) {
        if (obj == null) return "";
        return obj.toString();
    }

    public static String toStringOrNull(Object obj) {
        if (obj == null) return "null";
        return obj.toString();
    }

    /**
     * İçlerinde bir değer boş ise false döner, hepsi dolu ise true döner
     *
     * @param txtVal
     * @return
     */
    public static boolean isFullAllWithTrim(String... txtVal) {
        boolean boFull = true;
        for (String val : txtVal) {
            if (isEmptyTrim(val)) {
                boFull = false;
                return boFull;
            }
        }
        return boFull;
    }
    public static boolean hasEmptyWithTrim(String... txtVal) {
        boolean hasEmpty = false;
        for (String val : txtVal) {
            if (isEmptyTrim(val)) {
                hasEmpty = true;
                return hasEmpty;
            }
        }
        return hasEmpty;
    }

    /**
     * Bir tanesi dolu ise true döner
     *
     * @param txtVal
     * @return
     */
    public static boolean isFullOneOfThemWithTrim(String... txtVal) {
        for (String val : txtVal) {
            if (!isEmptyTrim(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Biri boşsa true döner
     *
     * @param txtVal
     * @return
     */
    public static boolean isEmptyOneOfThemWithTrim(String... txtVal) {
        Boolean boEmptyOneOfThem = false;

        for (String val : txtVal) {
            if (isEmptyTrim(val)) {
                boEmptyOneOfThem = true;
            }
        }

        return boEmptyOneOfThem;
    }

    /**
     * txCheckValue, Deger dizisinden birine eşitse true döner
     *
     * null eşitliğini kabul etmez
     *
     * @param txCheckValue
     * @param txtValueArr
     * @return
     */
    public static boolean equalsOne(String txCheckValue, String... txtValueArr) {
        if (txCheckValue == null) return false;
        for (String val : txtValueArr) {

            if (val == null) continue;

            if (txCheckValue.equals(val)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsOne(String txCheckValue, Object... txtValueArr) {
        if (txCheckValue == null) return false;
        for (Object val : txtValueArr) {

            if (val == null) continue;

            if (txCheckValue.equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * txCheckValue, Deger dizisinden birine eşitse true döner (case insensetive)
     *
     * @param txCheckValue
     * @param txtValueArr
     * @return
     */
    public static boolean equalsSomeIgnoreCase(String txCheckValue, String... txtValueArr) {
        if (txCheckValue == null) return false;
        for (String val : txtValueArr) {

            if (val == null) continue;

            if (txCheckValue.equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

    public static String addSpaceStartIfNotEmpty(String txValue) {
        if (!isEmptyTrim(txValue)) {
            return " " + txValue;
        }
        if (txValue == null) return "";
        return txValue;
    }

    public static String addSpaceIfFull(String txValue) {
        if (!isEmptyTrim(txValue)) {
            return " ";
        }
        return "";
    }

    public static String combineWithSpace(String txValue1, String txValue2) {
        if (!isEmptyTrim(txValue1)) {
            if (!isEmptyTrim(txValue2)) {
                return txValue1 + " " + txValue2;
            } else { // txValue2 Empty
                return FiString.orEmpty(txValue1);
            }
        } else { // txValue1 is empty
            return FiString.orEmpty(txValue2);
        }
    }

    public static String combineListWithNewLine(List<String> listNew) {
        if (FiCollection.isEmpty(listNew)) {
            return "";
        }

        StringBuilder txResult = new StringBuilder();
        for (int index = 0; index < listNew.size(); index++) {
            if(index!=0){
                txResult.append("\n");
            }
            txResult.append(FiString.orEmpty(listNew.get(index)));
        }

        return txResult.toString();
    }

    public static String combineListAsAText(List<?> listNew,String txSeperator) {
        if (FiCollection.isEmpty(listNew)) {
            return "";
        }

        StringBuilder txResult = new StringBuilder();
        boolean boFirstItem = true;
        for (int index = 0; index < listNew.size(); index++) {
            if(listNew.get(index)==null)continue;
            if(!boFirstItem){
                txResult.append(txSeperator);
            }
            txResult.append(FiString.orEmpty(listNew.get(index)));
            boFirstItem=false;
        }

        return txResult.toString();
    }

    public static String combineListWitComma(List<?> listNew) {
        return combineListAsAText(listNew, ",");
    }

    public static Boolean checkEqualNotNull(String vdaireno, String cari_vdaire_no) {
        if (vdaireno == null || cari_vdaire_no == null) return false;

        if (vdaireno.equals(cari_vdaire_no)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAllUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String icineAlKoseliParantez(String commaSeperatedParse) {
        return "[" + commaSeperatedParse + "]";
    }


    public static boolean isEmpty(Integer cha_cari_cins) {
        if (cha_cari_cins == null) return true;
        return false;
    }

    public static Boolean isEqualWitOrEmpty(String txData1, String txData2) {
        if (FiString.orEmpty(txData1).equals(FiString.orEmpty(txData2))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * txData1 veya txData2 null olursa otomatik false döner
     * <p>
     * (null==null) true olarak değerlendirmez, eşitlik kontrolü dışına atılır
     *
     * @param txData1
     * @param txData2
     * @return
     */
    public static Boolean isEqual(String txData1, String txData2) {
        if (txData1 == null || txData2 == null) return false;
        // txDate1 ve 2 null degil
        if (txData1.equals(txData2)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isEqualWithTrim(String txData1, String txData2) {
        if (txData1 != null) txData1 = txData1.trim();
        if (txData2 != null) txData2 = txData2.trim();
        return FiString.orEmpty(txData1).equals(FiString.orEmpty(txData2));
    }

    /**
     * Nte : Not Empty , iki datadan biri boşsa false döner (boşla boş eşit kabul etmez)
     *
     * @param txData1
     * @param txData2
     * @return
     */
    public static Boolean isEqualWithTrimAndNte(String txData1, String txData2) {
        if (FiString.isEmptyTrim(txData1)) return false;
        if (FiString.isEmptyTrim(txData2)) return false;

        txData1 = txData1.trim();
        txData2 = txData2.trim();

        return FiString.orEmpty(txData1).equals(FiString.orEmpty(txData2));
    }

    /**
     * İçerikleri dolu bir şekilde eşit ise
     *
     * @param txData1
     * @param txData2
     * @return
     */
    public static Boolean isEqualAndFull(String txData1, String txData2) {

        if (FiString.isEmptyOneOfThemWithTrim(txData1, txData2)) {
            return false;
        }

        if (txData1.equals(txData2)) {
            return true;
        } else {
            return false;
        }
    }

    public static <PrmEnt1> String itemOrEmpty(PrmEnt1 itemGun, Function<PrmEnt1, String> fnValue) {
        if (itemGun == null) return "";
        return fnValue.apply(itemGun);
    }

    public static boolean equalsWithTrim(String txValue1, String txValue2) {

        if (txValue1 == null || txValue2 == null) {
            return false;
        }

        return txValue1.trim().equals(txValue2.trim());
    }

    /**
     * Eğer boş degilse (trimli) txValue 1 dön, yoksa txValue2 dön
     * <p>
     * Emptyt means : Empty With Trim
     *
     * @param txValue1
     * @param txValue2
     * @return
     */
    public static String getIfNotEmptytOr(String txValue1, String txValue2) {
        if (FiString.isEmptyTrim(txValue1)) return txValue2;
        return txValue1;
    }

    public static int getSize(String cari_vergiKimlikNo) {
        if (cari_vergiKimlikNo == null) return 0;
        return cari_vergiKimlikNo.length();
    }

    /**
     * {{namedParameter}} ile değerlerini yer değişitirir
     *
     * @param txTemplate
     * @param fiKeyBean
     * @return
     */
    public static String substitutor(String txTemplate, FiKeybean fiKeyBean) {
        StrSubstitutor sub = new StrSubstitutor(fiKeyBean, "{{", "}}");
        return sub.replace(txTemplate);
    }

    public static <PrmEnt1> String joinStrings(List<PrmEnt1> listData, Function<PrmEnt1, String> spStringValue, String txDelimiter) {

        StringBuilder sbResult = new StringBuilder();

        for (int index = 0; index < listData.size(); index++) {
            PrmEnt1 prmEnt1 = listData.get(index);
            if (index != 0) sbResult.append(FiString.orEmpty(txDelimiter));
            sbResult.append(spStringValue.apply(prmEnt1));
        }
        return sbResult.toString();
    }

    public static String joinStrings(List<String> listData, String txDelimiter) {

        StringBuilder sbResult = new StringBuilder();

        for (int index = 0; index < listData.size(); index++) {
            String txValue = listData.get(index);
            if (index != 0) sbResult.append(FiString.orEmpty(txDelimiter));
            sbResult.append(txValue);
        }
        return sbResult.toString();
    }

    public static String capitalizeFirstLetter(String str) {

        if (str == null || str.isEmpty()) {
            return str; // Boş veya null bir string döndürülür.
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

  /**
   * Eğer txValue başında harf varsa, onu kaldırır.
   *
   * @param txValue Güncellenmesi gereken değer
   * @return Güncellenmiş değer
   */
  public static String removeFirstLetterIfPresent(String txValue) {

    if (!FiString.isEmpty(txValue) && Character.isLetter(txValue.charAt(0))) {
      return txValue.substring(1);
    }

    return txValue;
  }

  /**
   * txValue içerisindeki baştaki tüm harfleri kaldırır.
   *
   * @param txValue Güncellenmesi gereken değer
   * @return Güncellenmiş değer
   */
  public static String removeLeadingLetters(String txValue) {
    if (!FiString.isEmptyTrim(txValue)) {
      return txValue.replaceFirst("^[a-zA-Z]+", "");
    }
    return txValue;
  }
}
