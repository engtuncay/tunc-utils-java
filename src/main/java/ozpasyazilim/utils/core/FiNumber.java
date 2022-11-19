package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiNumber {

	Number value;

	public static Locale locale = new Locale("tr", "TR");

	public FiNumber(BigDecimal dmValue) {
		if (dmValue == null) {
			this.value = BigDecimal.ZERO;
		} else {
			this.value = dmValue;
		}
	}

	public FiNumber(Double dmValue) {
		if (dmValue == null) {
			this.value = 0.0d;
		} else {
			this.value = dmValue;
		}
	}

	public FiNumber() {
	}

	public static Double orZero(Double dmValue) {
		if (dmValue == null) return 0d;
		return dmValue;
	}

	public static Double orZeroDouble(Object dmValue) {
		if (dmValue == null) return 0d;
		return (Double)dmValue;
	}

	public static String formatNumberIfExistDecimal(Number hedef) {
		NumberFormat formatter = new DecimalFormat("##.####");
		//System.out.println("The Decimal Value is:"+formatter.format(hedef));
		return formatter.format(hedef);
	}

	public static Double strToDouble(Object sNumberold) {

		if (sNumberold == null) return null;

		if(sNumberold instanceof Double) return (Double) sNumberold;

		if(sNumberold instanceof Integer){
			Integer ntValue = (Integer) sNumberold;
			return ntValue.doubleValue();
		}

		if(sNumberold instanceof String){

			String txValue = (String) sNumberold;

			if(FiString.isEmptyTrim(txValue)) return null;

			String sNumbernew = FiNumberToText.convertNumberFormatToDotSeperator(txValue); // sNumberold.toString()

			try {
				return Double.parseDouble(sNumbernew);
			} catch (Exception e) {
				Loghelper.get().debug("String sayı double'a çevrilemedi." + sNumberold.toString());
				return null;
			}
		}

		return null;
	}

	public static Double convertNumberStrtoDoubleWth(String sNumberold) throws Exception {

		String sNumbernew = FiNumberToText.convertNumberFormatToDotSeperator(sNumberold);
		Double numbernew = Double.parseDouble(sNumbernew);
		return numbernew;

	}

	public static Optional<Double> strToDoubleOpt(String sNumber) {

		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		//symbols.setGroupingSeparator(' ');
		df.setDecimalFormatSymbols(symbols);
		//df.parse(sNumber);

		try {
			return Optional.of(df.parse(sNumber).doubleValue());
		} catch (ParseException e) {
			Loghelper.get(FiNumber.class).error("Hata :" + FiException.exceptiontostring(e));
			return Optional.empty();
		}

	}

	public static Boolean isNullOrZeroOr(Double... dbValues) {

		for (Double dbValue : dbValues) {
			if(dbValue==null || dbValue == 0d){
				return true;
			}
		}
		return false;

	}

	public static Boolean isNullOrZeroAll(Double... dbValues) {
		for (Double dbValue : dbValues) {
			if(dbValue!=null && dbValue != 0d){
				return false;
			}
		}
		return true;

	}

	public static Integer compareBigDecimal(BigDecimal number1, Double number2) {

		int nDecimalAmount = 5;

		BigDecimal nNumber1 = number1.setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);
		BigDecimal nNumber2 = BigDecimal.valueOf(number2).setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);

		return nNumber1.compareTo(nNumber2);

	}

	public static Integer compareBigDecimal(BigDecimal number1, BigDecimal number2) {

		int nDecimalAmount = 5;

		BigDecimal nNumber1 = number1.setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);
		BigDecimal nNumber2 = number2.setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);

		return nNumber1.compareTo(nNumber2);

	}

	public static Integer compareBigDecimals(BigDecimal number1, BigDecimal number2, Integer nDecimalAmount) {

		BigDecimal nNumber1 = number1.setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);
		BigDecimal nNumber2 = number2.setScale(nDecimalAmount, BigDecimal.ROUND_HALF_UP);

		return nNumber1.compareTo(nNumber2);

	}

	public static Boolean compareEqualBigDecimals(BigDecimal number1, BigDecimal number2, Integer lnDecimalLength,
	                                              Double hassasiyet) {

		BigDecimal nNumber1 = number1.setScale(lnDecimalLength, BigDecimal.ROUND_HALF_UP);
		BigDecimal nNumber2 = number2.setScale(lnDecimalLength, BigDecimal.ROUND_HALF_UP);

		BigDecimal nFark = nNumber1.subtract(nNumber2).abs();

		return nFark.compareTo(BigDecimal.valueOf(hassasiyet)) < 0;

	}

	public static Boolean isZero(Double dbValue, Double sifirHassasiyet) {
		if(dbValue==null) return false;

		if(Math.abs(dbValue)<sifirHassasiyet) return true;
		return false;
	}

	/**
	 * Null values assumes Zero
	 *
	 * @param dbValue
	 * @return
	 */
	public static Boolean isZeroWithNull(Double dbValue) {
		if(dbValue==null) return true;

		if(dbValue==0d) return true;
		return false;
	}

	public static Boolean isZero001(Double dbValue) {
		return isZero(dbValue, 0.001);
	}

	public static Boolean isZero25(Double dbValue) {
		return isZero(dbValue, 0.25);
	}

	public static Boolean isZeroOrNull(Double dbValue, Double sifirHassasiyet) {
		if(dbValue==null) return true;
		if(Math.abs(dbValue)<=sifirHassasiyet) return true;
		return false;
	}

	public static boolean checkZero(Double sum, double dbHassasiyet) {
		if(Math.abs(sum)>=dbHassasiyet){
			return false;
		}
		return true;
	}

	public static String formatlaNumbertoString(Double dblnumber) {

		if (dblnumber == null) {
			return "0.00";
		}

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

		String strdblnumber = decimalpattern.format(dblnumber);
		return strdblnumber;
	}

	public static String formatlaNumbertoStringDetayli(Double dblnumber) {

		if (dblnumber == null) {
			return "0.00";
		}

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00000", otherSymbols);

		String strdblnumber = decimalpattern.format(dblnumber);
		return strdblnumber;
	}

	public static Double formatStringParatutartoDouble(String number) {
		if (number.equals("")) {
			return 0.0D;
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

		Double value = 0.0D;
		try {
			value = decimalpattern.parse(number).doubleValue();
		} catch (Exception e) {

			Loghelper.get(FiString.class)
					.info("Double çeviri hata" + FiException.exceptiontostring(e));
			value = 0.0D;
		}

		return value;
	}

	public static String formatlaNumbertoString(Float flonumber) {

		if (flonumber == null) {
			return "0.00";

		}

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

		String strdblnumber = decimalpattern.format(flonumber);
		return strdblnumber;
	}

	public static Double formatStringParatutartoDoubleDetayli(String number) {
		if (number.equals("")) {
			return 0.0D;
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00000", otherSymbols);

		Double value = 0.0D;
		try {
			value = decimalpattern.parse(number).doubleValue();
		} catch (Exception e) {
			Loghelper.get(FiString.class)
					.info("Double çeviri hata" + FiException.exceptiontostring(e));
			value = 0.0D;
		}

		return value;
	}

	public static String formatlaStringtoStringParagosterim(String coldata) {
		Double tutar = formatStringParatutartoDouble(coldata);
		if (tutar != null) {
			return formatlaNumbertoString(tutar);
		}
		return "";
	}

	public static String formatlaStringtoStringParagosterimDetayli(String coldata) {
		Double tutar = formatStringParatutartoDoubleDetayli(coldata);
		if (tutar != null) {
			return formatlaNumbertoStringDetayli(tutar);
		}
		return "";
	}

	public static String formatlaParaBy2Decimal(Double dblnumber) {
		return formatlaPara(dblnumber, 2);
	}

	public static String formatlaPara(Double dblnumber, Integer nOndalikKisim) {

		if (dblnumber == null) {
			dblnumber = 0.0d;
		}

		String ondalikpattern = "";

		if (nOndalikKisim == null) nOndalikKisim = 2;

		for (int i = 1; i <= nOndalikKisim; i++) ondalikpattern += "0";

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(FiString.locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0." + ondalikpattern, otherSymbols);

		String strdblnumber = decimalpattern.format(dblnumber);
		return strdblnumber;

	}

	public static String formatlaPara(BigDecimal bdnumber, Integer nOndalikKisim) {

		return formatlaPara(bdnumber.doubleValue(), nOndalikKisim);
	}

	public static Integer orZero(Integer lnInsertedRows) {
		if(lnInsertedRows==null) return 0;
		return lnInsertedRows;
	}

	public static Integer orMinusOne(Integer lnInsertedRows) {
		if(lnInsertedRows==null) return -1;
		return lnInsertedRows;
	}

	/**
	 * null değerlerde false döner
	 *
	 * @param value1
	 * @param values
	 * @return
	 */
	public static Boolean equalsSome(Integer value1,Integer... values) {
		if(value1==null) return false;
		for (Integer value : values) {
			if(value==null) continue;
			if(value1==value) return true;
		}
		return false;
	}

	public static Boolean checkDbNegativeSome(Double ...objValues) {
		for (Double objValue : objValues) {
			if(FiNumber.orZero(objValue)<0) return true;
		}
		return false;
	}

	public static String orEmpty(Integer lnValue) {
		if(lnValue==null) return "";
		return lnValue.toString();
	}


	public FiNumber buildNullCheckOrZero(Double dmValue) {
		if (dmValue == null) {
			this.value = 0d;
		} else {
			this.value = dmValue;
		}
		return this;
	}

	public FiNumber buildNullCheckOrZero(BigDecimal dmValue) {
		if (dmValue == null) {
			this.value = BigDecimal.ZERO;
		} else {
			this.value = dmValue;
		}
		return this;
	}

	public Double getValueAsDouble() {
		return this.value.doubleValue();
	}

	/**
	 * Rounding Half Up
	 *
	 * @param dblNumber
	 * @param scale
	 * @return
	 */
	public static Double truncateByHalfUp(Double dblNumber, Integer scale) {
		Double truncatedDouble = BigDecimal.valueOf(dblNumber)
				.setScale(scale, RoundingMode.HALF_UP)
				.doubleValue();
		return truncatedDouble;
	}

	public static Double truncateByHalfDown(Double dblNumber, Integer scale) {
		Double truncatedDouble = BigDecimal.valueOf(dblNumber)
				.setScale(scale, RoundingMode.HALF_DOWN)
				.doubleValue();
		return truncatedDouble;
	}

	/**
	 * Default scale 2
	 * Rounding Half Up
	 *
	 * @param dblNumber
	 * @return
	 */
	public static Double truncateByHalfUp(Double dblNumber) {
		return truncateByHalfUp(dblNumber, 2);
	}

	public static String formatNumber(Number number) {

		if (number == null) return "";
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
		String strnumber = decimalpattern.format(number);
		return strnumber;
	}

	public static String formatNumber(Object number) {

		if (number == null) return "";
		if(number instanceof String){
			return "";
		}
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);
		String strnumber = decimalpattern.format(number);
		return strnumber;

	}

	public static String formatNumber(Double dblnumberp) {

		if (dblnumberp == null) {
			return "";
		}

		Double dblnumber = dblnumberp;
		//Double dblnumber = truncateByHalfUp(dblnumberp);

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("#,###,##0.00", otherSymbols);

		String strdblnumber = decimalpattern.format(dblnumber);

		return strdblnumber;

	}

	public static String formatNumberPlain(Double dblnumberp) {

		if (dblnumberp == null) {
			return "";
		}

		Double dblnumber = dblnumberp;
		//Double dblnumber = truncateByHalfUp(dblnumberp);

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat decimalpattern = new DecimalFormat("##########.##", otherSymbols);

		String strdblnumber = decimalpattern.format(dblnumber);

		return strdblnumber;

	}

	public static Double convertStringToDouble(String txDoubleValue) {

		if (txDoubleValue == null) return null;

		//String str = "2.00000181334612E15";
		Double dbValue = (Double.parseDouble(txDoubleValue));
		//System.out.println(dbValue);
		return dbValue;

	}

	public static Optional<Integer> parseToIntOpt(String txValue) {

		if (txValue == null) return Optional.empty();

		try {
			Integer value = Integer.parseInt(txValue);
			return Optional.of(value);
		} catch (Exception e) {
			return Optional.empty();
		}

	}

	public static Integer strToInt(Object txValue) {

		if (FiString.isEmptyToStringWithTrim(txValue)) return null;

		try {
			return Integer.parseInt(txValue.toString());
		} catch (NumberFormatException e) {
			Loghelper.debugLog(FiNumber.class,"Integer Parse Edilirken Hata oluştu. Deger:"+FiString.ifNullThenEmpty(txValue));
			return null;
		}

	}

	public static Double convertToDouble(Object sNumberold) {

		if (sNumberold == null) return null;

		String sNumbernew = convertNumberFormatToDotSeperator(sNumberold.toString());

		try {
			Double numbernew = Double.parseDouble(sNumbernew);
			return numbernew;
		} catch (Exception e) {
			Loghelper.get().trace("String sayı double'a çevrilemedi.");
			return null;
		}

	}

	/**
	 * Farklı bölgesel ayarlarda ondalık ayraç farklı geldikleri için tek tipe dönüşüm yapılması saglandı.
	 * Virgüllü veya Noktalı Sayıları (23,12 veya 23.12 ) , Noktalı Tipe Dönüştürür ( 23.12 )
	 *
	 * @param strNumberOld
	 * @return
	 */
	public static String convertNumberFormatToDotSeperator(String strNumberOld) {

		// 1024 gibi nokta virgülsüz sayı gelirse

		String regExpCommaDot = ".*[\\.|,][0-9]{0,2}";

		if (!strNumberOld.matches(regExpCommaDot))
			return strNumberOld;

		String regExp = "(.*)[,\\.]([0-9]{0,2})";
		Pattern pattern = Pattern.compile(regExp); // "(\\d*)" + "\\.0$");
		Matcher matcher = pattern.matcher(strNumberOld);

		// FIXME mathcer 2 adet dönüş yaptı mı -- try catch
		if (matcher.find()) {

			String grup1 = matcher.group(1);
			String grup2 = matcher.group(2);

			// System.out.println("grup1:" + grup1);
			// . ve , tam sayı kısmından temizle
			grup1 = grup1.replaceAll("\\.", "");
			grup1 = grup1.replaceAll(",", "");

			if (grup1.length() == 0)
				grup1 = "0";
			if (grup2.length() == 0) {
				grup2 = "";
			} else {
				grup2 = "." + grup2;
			}

			// System.out.println("grup2:" + grup2);
			// System.out.println("Yeni:" + grup1 + grup2);

			return grup1 + grup2;
		}

		return null;

	}

	public static String formatStringExpoNumber(String txExpoNumber) {
		if (txExpoNumber == null) return null;
		if (txExpoNumber.trim().equals("")) return "";

		Double dbValue = convertStringToDouble(txExpoNumber);
		return formatNumberPlain(dbValue);

	}


	public FiNumber buildScale(int scale, RoundingMode roundingMode) {

		this.value = BigDecimal.valueOf(getValueAsDouble())
				.setScale(scale, roundingMode).doubleValue();

		return this;
	}

	public FiNumber buildScaleHalfUp(int scale) {
		return buildScale(scale, RoundingMode.HALF_UP);
	}
}
