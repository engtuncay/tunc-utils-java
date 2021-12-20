package ozpasyazilim.utils.core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiRegExp {

	public static void main(String[] args) {
		//.*_[0-9]*

		String txValue = "1";

		Boolean testResult = txValue.matches("\\w*(\\d+)");

		System.out.println("Test Result:" + testResult);

//		{ //.*_[0-9]*
//			return FiString.getMatchGroupFirst("(\\d+)", txValue);
//		}

	}

	public static Boolean testMatch(String text, String regexp) {

		Boolean testresult = text.matches(regexp);
		// System.out.println("Text :" + text + "\tregexp:" + regexp + "\tMatch Result:" + testresult);
		return testresult;

	}

	public static String regexChangePatternCaseInsensitive(String spText) {
		// ğüşiöç türkçe karakterleri büyük küçük varyasyonları için regex şablona
		// çevrildi
		String strText = new String(spText);
		strText.replaceAll("i", "[İi])");
		strText.replaceAll("İ", "[İi])");
		strText.replaceAll("I", "[Iı])");
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

	public static String regexChangePatternCaseAndIiInsensitive(String spText) {
		// ğüşiöç türkçe karakterleri büyük küçük varyasyonları için regex şablona
		// çevrildi
		String strText = new String(spText);
		strText.replaceAll("i", "[İiIı])");
		strText.replaceAll("İ", "[İiIı])");
		strText.replaceAll("I", "[Iİıi])");
		strText.replaceAll("ı", "[Iİıi])");
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

	public static String matchFirst(String sRegex, String sText) {

		Pattern pattern = Pattern.compile(sRegex);
		Matcher matcher = pattern.matcher(sText);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}

	}

	public static Set<String> matchGroupOneSet(String sRegex, String sText) {

		Set<String> setFirst = new HashSet<>();

		Pattern pattern = Pattern.compile(sRegex);
		Matcher matcher = pattern.matcher(sText);

		while(matcher.find()) {
			setFirst.add(matcher.group(1));
		}

		return setFirst;
	}

	/**
	 *
	 * matcher.group(0) sonucunu set halinde verir
	 *
	 * @param sRegex
	 * @param sText
	 * @return
	 */
	public static Set<String> matchGroupZeroSet(String sRegex, String sText) {

		Set<String> setFirst = new HashSet<>();

		Pattern pattern = Pattern.compile(sRegex);
		Matcher matcher = pattern.matcher(sText);

		while(matcher.find()) {
			setFirst.add(matcher.group(0));
		}

		return setFirst;
	}



	public static Matcher match(String sRegex, String sText) {
		Pattern pattern = Pattern.compile(sRegex);
		Matcher matcher = pattern.matcher(sText);
		return matcher;
	}

	public static Boolean checkEmail(String eMail) {

		if (eMail == null) eMail = "";

		//String regex = "^(.+)@(.+)$";
		//String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(eMail);

		return matcher.matches();

	}

	public static Boolean checkTel(String cepTel) {

		if (cepTel == null) cepTel = "";

		cepTel = cepTel.trim();
		String regex = "^0\\d{10}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cepTel);

		return matcher.matches();
	}


	public static boolean checkAlpanumericAndSpaceAndTire(String text) {
		if (text == null) text = "";
		if (text.equals(" ")) return true;
		String regex = "[a-zA-Z0-9ÇçŞşÖöĞğÜüİı\\-]";
		Matcher matcher = Pattern.compile(regex).matcher(text);
		return matcher.matches();
	}

	public static boolean checkPatternExist(String txValue, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(txValue);
		return m.find();
	}
}
