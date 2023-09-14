package ozpasyazilim.utils.core;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.joda.time.DateTime;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.log.Loghelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import ozpasyazilim.log.Loghelper;
// import ozpasyazilim.mikro.models.UtilModel;

public class FiDate {

	public FiDate() {
	}

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy", getLocaleTr());
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	private static Locale getLocaleTr() {
		return new Locale("tr", "TR");
	}

	public static LocalDate dateToLocalDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		LocalDate localDate = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}

	public static LocalDate dateToLocalDate(String strDateyyyymmdd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDate = LocalDate.parse(strDateyyyymmdd, formatter);
		return localDate;
	}

	public static Date dateAddDay(Date basedate, Integer count) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(basedate);
		cal.add(Calendar.DATE, count);
		return cal.getTime();
	}

	public static String dateToStrAsddDmmDyyyy(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		if (date == null) return null;
		return sdf.format(date);
	}

	/**
	 * dd.MM.yyyy Formatı
	 *
	 * @param stDate
	 * @return
	 * @throws Exception
	 */
	public static Date strToDateAsddmmyyyyByDotWitExc(String stDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.parse(stDate);
	}

	public static Date strToDateAsddmmyyyyWitDot(String stDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return sdf.parse(stDate);
		} catch (ParseException e) {
			Loghelper.get(FiDate.class).debug(FiException.exTosMain(e));
			return null;
		}
	}

	public static int getYear() throws Exception {
		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.get(calendar.YEAR);
	}

	public static int getYearAsInt(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(calendar.YEAR);
	}

	public static String getDay(Date date) {
		SimpleDateFormat sdfGun = new SimpleDateFormat("dd");
		return sdfGun.format(date);
	}

	public static String getMonth(Date date) {
		SimpleDateFormat sdfAy = new SimpleDateFormat("MM");
		return sdfAy.format(date);
	}

	public static String getYear(Date date) {
		SimpleDateFormat sdfYil = new SimpleDateFormat("yyyy");
		return sdfYil.format(date);
	}

	public static Date getDatewithTimeZero() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		return now.getTime();
	}

	/**
	 * set Time to 0
	 *
	 * @param mydate
	 * @return
	 */
	public static Date clearTimeFromDate(Date mydate) {

		Calendar mycal = Calendar.getInstance();
		mycal.setTime(mydate);
		mycal.set(Calendar.HOUR, 0);
		mycal.set(Calendar.MINUTE, 0);
		mycal.set(Calendar.SECOND, 0);
		mycal.set(Calendar.HOUR_OF_DAY, 0);
		mycal.set(Calendar.MILLISECOND, 0);
		return mycal.getTime();

	}

	public static void setTimeZone(TimeZone tz) {
		SimpleDateFormat sdfOnlyHourFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdDateAndDayFormat = new SimpleDateFormat("dd.MM.yyyy EEEEE");
		SimpleDateFormat sdfOnlyTimeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdfDateAndTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		sdf.setTimeZone(tz);
		sdfDateAndTimeFormat.setTimeZone(tz);
		sdfOnlyTimeFormat.setTimeZone(tz);
		sdDateAndDayFormat.setTimeZone(tz);
		sdfOnlyHourFormat.setTimeZone(tz);
	}


	public static String toStringYmd(Date date) {
		if (date == null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}

	public static Integer toIntegerYmd(Date date) {
		if (date == null) return null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return FiNumber.strToInt(formatter.format(date));
	}

	/**
	 * yyyyHmmHdd -> yyyy-MM-dd  ( H means hyphen )
	 *
	 * @param date
	 * @return
	 */
	public static String dateToStrGlobalFormatAsyyyyHmmHdd(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	public static String dateToStrAsddmmyyyyWitSlash(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}

	public static String dateToStrAsddmmyyyyWitDot(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}

	public static String dateToStrAsddmmyyyyTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss EEE");
		return formatter.format(date);
	}

	public static String datetoString_timestamptText(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_kkmm_ss");
		return formatter.format(date);
	}

	public static String toStringTimestamptPlain(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddkkmmss");
		return formatter.format(date);
	}

	public static String datetoString_timeHourMinute(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_kkmm");
		return formatter.format(date);
	}

	public static String toStringDateTimeDotFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm");
		return formatter.format(date);
	}

	public static String toStringDateTimeSecDotFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
		return formatter.format(date);
	}

	public static String datetoString_timestamptShort(Date date) {
		SimpleDateFormat sdfDateAndTimeShortFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return sdfDateAndTimeShortFormat.format(date);
	}

	public static Date parseDate_yyyymmdd(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	/**
	 * Converts yyyymmdd format to Date
	 *
	 * @param date
	 * @return
	 */
	public static Date strToDateYmd(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	public static Date strToDateByDash(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	@FiDraft
	public static Date strToDateGeneric3(String date) {

		Date dateSelected = null;

		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd.MM.yyyy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd.MM.20yy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd,MM,yyyy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd,MM,20yy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd-MM-yyyy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd-MM-20yy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd/MM/yyyy");
		if (dateSelected == null) dateSelected = stringToDate_FormatWoutException(date, "dd/MM/20yy");

		return dateSelected;

	}

	public static String convertDateSeparator(String value, String seperator) {

		if (seperator == null) seperator = ".";

		// eklenen ayraçlar . / - ,
		Pattern pattern = Pattern.compile("([0-9]{1,2})[\\.\\-,/]([0-9]{1,2})[\\.\\-,/]([0-9]{2,4})");

		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {

			String day = matcher.group(1);
			String month = matcher.group(2);
			String year = matcher.group(3);

			if (day.equals("0")) return null;
			if (month.equals("0")) return null;

			return day + seperator + month + seperator + year;
		}

		return null;
	}

	public static String generateDatePattern(String value, String seperator) {

		if (seperator == null) seperator = ".";

		if (value == null) return null;

		// eklenen ayraçlar . / - ,
		Pattern pattern = Pattern.compile("([0-9]{1,2})[\\.\\-,/]([0-9]{1,2})[\\.\\-,/]([0-9]{2,4})");

		Matcher matcher = pattern.matcher(value);

		if (matcher.find()) {

			String day = matcher.group(1);
			String month = matcher.group(2);
			String year = matcher.group(3);

			day = day.replaceAll(".", "d");
			month = month.replaceAll(".", "M");
			year = year.replaceAll(".", "y");

			return day + seperator + month + seperator + year;
		}

		return null;
	}

	public static Date strToDateGeneric(String date) {

		// eklenen ayraçlar . / - ,
		Pattern pattern = Pattern.compile("([0-9]{1,2})[\\.\\-,/]([0-9]{1,2})[\\.\\-,/]([0-9]{2,4})");

		Matcher matcher = pattern.matcher(date);

		//String seperator=".";

		if (matcher.find()) {

			Calendar cal = Calendar.getInstance();

			String day = matcher.group(1);
			String month = matcher.group(2);
			String year = matcher.group(3);

			//System.out.println(String.format(" Day: %s , Month: %s, Year: %s",day,month,year));

			if (month.matches("0[1-9]")) month = month.replace("0", "");
			if (day.matches("0[1-9]")) day = day.replace("0", "");
			// FIXME baştaki sıfırı sadece silecek
			if (year.matches("0[1-9]")) day = day.replace("0", "");

			Integer ntYear = Integer.parseInt(year);
			if (year.length() < 4) {
				//year="20"+year;
				ntYear = ntYear + 2000;
			}

			cal.set(Calendar.DATE, Integer.parseInt(day));
			cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			cal.set(Calendar.YEAR, ntYear);

			new FiCal().clearTime(cal);

			return cal.getTime();
		}

		return null;

	}

	public static Date stringToDate_ddmmyyyyWithSlash(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	public static Date stringToDate_ddmmyyyyWithComma(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd,MM,yyyy");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	// FiReflection.setter da kullanılıyor , objeye çevrilirken
	public static Date strToDateGeneric2(String strDate) {

		Date cellvalue = null;

		if (FiString.isEmptyTrim(strDate)) return null;

		if (strDate.contains("T")) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//			try {
//				cellvalue = sdf.parse(strDate);
//			} catch (ParseException e) {
//				Loghelper.get(FiDate.class).debug(FiException.exceptiontostring(e));
//			}
//			return cellvalue;
			LocalDateTime dateTime = LocalDateTime.parse(strDate);
			cellvalue = convertLocalDateTimeToSimpleDate(dateTime);
			return cellvalue;
		}

		// dd/MM/YY[YY] dd.MM.YY[YY]
		// FIXME 2018 yerin 18 kabul eder mi :
		if (strDate.matches("[0-9]{1,2}[\\/\\.][0-9]{1,2}[\\/\\.][0-9]{2,4}")) {

			if (strDate.matches(".*\\..*")) {
				strDate = strDate.replace(".", "/");
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			//String dateInString = "7-Jun-2013";

			try {
				Date date = formatter.parse(strDate);
				//System.out.println(date);
				//System.out.println(formatter.format(date));
				cellvalue = date;

			} catch (ParseException e) {
				Loghelper.get(FiDate.class).debug(FiException.exTosMain(e));
			}

			return cellvalue;
		}

		if (strDate.matches("[0-9]+[\\,\\.]*[0-9]*")) {
			Date date = HSSFDateUtil.getJavaDate(Double.parseDouble(strDate));
			cellvalue = date;
			return cellvalue;
		}


		String regexDateTime = "(\\d{1,2})[\\/\\.](\\d{1,2})[\\/\\.](\\d{2,4})\\s*(\\d{2}):(\\d{2}):(\\d{2})";

		if (strDate.matches(regexDateTime)) {   //24.11.2017 00:00:00

			Pattern pattern = Pattern.compile(regexDateTime);
			Matcher matcher = pattern.matcher(strDate);
			Calendar cal = Calendar.getInstance();
			String seperator = ".";

			if (matcher.find()) {
				String day = clearFirstZero(matcher.group(1));
				String month = clearFirstZero(matcher.group(2));
				String year = clearFirstZero(matcher.group(3));

				String hour = clearFirstZero(matcher.group(4));
				String minute = clearFirstZero(matcher.group(5));
				String second = clearFirstZero(matcher.group(6));

				//System.out.println(String.format(" Day: %s , Month: %s, Year: %s",day,month,year));
				//System.out.println(String.format(" Hour: %s , Minute: %s, Second: %s",hour,minute,second));

				cal.set(Calendar.DATE, Integer.parseInt(day));
				cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
				cal.set(Calendar.YEAR, Integer.parseInt(year));
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour)); //12 saatlik dilim
				cal.set(Calendar.MINUTE, Integer.parseInt(minute));
				cal.set(Calendar.SECOND, Integer.parseInt(second));
				cal.set(Calendar.MILLISECOND, 0);

				//System.out.println("Date:" + cal.getTime().toString());

				return cal.getTime();
			}

			if (matcher.groupCount() != 7)
				Loghelper.get(FiDate.class).error("Tarih parça group sayısı hatası");

		}

		// YYYY-MM-DD
		if (strDate.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			try {
				Date date = formatter.parse(strDate);
				//System.out.println(date);
				//System.out.println(formatter.format(date));
				cellvalue = date;

			} catch (ParseException e) {
				Loghelper.get(FiDate.class).debug(FiException.exTosMain(e));
			}

			return cellvalue;
		}

		//cellvalue = null;
		Loghelper.get(FiDate.class).debug("date cell value (unsupported format):" + strDate);

		return null;
	}

	public static String clearFirstZero(String strValue) {

		String regex = "(^0{1})(.+)";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(strValue);

		if (strValue.matches(regex) && matcher.find()) {
			return matcher.group(2);
		}

		return strValue;

	}


	/**
	 * convert dd.mm.yyyy String to date
	 *
	 * @param date String format of date
	 * @return
	 */
	public static Date stringToDateByDot(String date) {
		if (FiString.isEmpty(date)) return null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Date dtValue = formatter.parse(date);
			return dtValue;
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	public static Date strToDateByFormat(String sDateFormat, String sDate) {

		SimpleDateFormat formatter = new SimpleDateFormat(sDateFormat);
		try {
			return formatter.parse(sDate);
		} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}

	}

	public static String gunadinacevir(int gunkodu) {
		switch (gunkodu) {
			case 0:
				return "Tüm Günler";
			case 1:
				return "Pazartesi";
			case 2:
				return "Salı";
			case 3:
				return "Çarşamba";
			case 4:
				return "Perşembe";
			case 5:
				return "Cuma";
			case 6:
				return "Cumartesi";
			case 7:
				return "Pazar";
			case 8:
				return "Her gün";
		}
		return null;
	}

	public static String gunadinacevir(String gunkodu) {
		Integer intgunkodu = Integer.parseInt(gunkodu);
		switch (intgunkodu) {
			case 0:
				return "Tüm Günler";
			case 1:
				return "Pazartesi";
			case 2:
				return "Salı";
			case 3:
				return "Çarşamba";
			case 4:
				return "Perşembe";
			case 5:
				return "Cuma";
			case 6:
				return "Cumartesi";
			case 7:
				return "Pazar";
			case 8:
				return "Her gün";
		}
		return null;
	}

	public static Integer gunkodunacevir(String gunadi) {
		switch (gunadi.toLowerCase()) {
			case "pazartesi":
				return Integer.valueOf(1);
			case "salı":
				return Integer.valueOf(2);
			case "sali":
				return Integer.valueOf(2);
			case "çarşamba":
				return Integer.valueOf(3);
			case "carsamba":
				return Integer.valueOf(3);
			case "perşembe":
				return Integer.valueOf(4);
			case "persembe":
				return Integer.valueOf(4);
			case "cuma":
				return Integer.valueOf(5);
			case "cumartesi":
				return Integer.valueOf(6);
			case "pazar":
				return Integer.valueOf(7);
			case "1 günde bir": // tum günleri ifade ediyor
				return Integer.valueOf(8);
			case "1":
				return Integer.valueOf(1);
			case "2":
				return Integer.valueOf(2);
			case "3":
				return Integer.valueOf(3);
			case "4":
				return Integer.valueOf(4);
			case "5":
				return Integer.valueOf(5);
			case "6":
				return Integer.valueOf(6);
			case "7":
				return Integer.valueOf(7);
		}
		return Integer.valueOf(0);
	}

	// eski yöntem gün farkı hesaplama
	// Date today = new Date();
	//			Date tarih = mkCariHar3.getCha_tarihi();
	//			long diff = today.getTime() - tarih.getTime();
	//			long gecikmegunufarkı = diff / (24 * 60 * 60 * 1000);
	//			toplambakiyecarpigunfarki += meblag * gecikmegunufarkı;

	public static Date convertLocalDateToSimpleDate(LocalDate localDate) {

		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

//		int day = localDate.getDayOfMonth();
//		int month = localDate.getMonthValue();
//		int year = localDate.getYear();
//
//		Calendar cal = Calendar.getInstance();
//
//		cal.set(year, month-1, day, 0, 0, 0);

		//		cal.set(Calendar.YEAR, year);
		//		cal.set(Calendar.MONTH, month-1);
		//		cal.set(Calendar.Date, day);
		//
		//		cal.clear(Calendar.HOUR);
		//		cal.clear(Calendar.MINUTE);
		//		cal.clear(Calendar.SECOND);
		//		cal.clear(Calendar.MILLISECOND);

		//Date date = cal.getTime();

		return date;
	}

	public static Date convertLocalDateTimeToSimpleDate(LocalDateTime localDate) {
		Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
		return date;
	}

	public static Date stringToDate_Format(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			Loghelper.get(FiDate.class).info("Error :" + FiException.exTosMain(e));
			return null;
		}

	}

	public static Date stringToDate_FormatWoutException(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			//Loghelperr.getInstance(FiDate.class).info("Error :" + FiException.exceptiontostring(e));
			return null;
		}

	}

	public static String datetoString_mmddyyyyslashformat(Date date) {
		SimpleDateFormat formatter5 = new SimpleDateFormat("dd/MM/yyyy");
		return formatter5.format(date);
	}


	public static String datetoString_timestampt2(Date date) {
		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyyMMddhhmmss");
		return formatter5.format(date);
	}

	public static String datetoString_timestampt3(Date date) {
		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyyMMddhhmmssSS");
		return formatter5.format(date);
	}

	public static String datetoString_timestamptHourMinute(Date date) {
		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyyMMddhhmm");
		return formatter5.format(date);
	}


	public static Date buildDate(String value) {
		return FiDate.parseDate_yyyymmdd(value);
	}


	public static Boolean isEqualAsYmd(Date dt1, Date dt2) {
		if (dt1 == null || dt2 == null) return false;

		if (FiDate.toStringYmd(dt1).equals(FiDate.toStringYmd(dt2))) {
			return true;
		}
		return false;
	}

	/**
	 * Saat bölgesine (+1,+2 gibi) gösterir.
	 *
	 * Örnek : 2023-07-26T09:34:59.505+03:00
	 *
	 * @return
	 */
	public static String getNowStringAsIso8601DatetimeWithZone() {
		DateTime dt = new DateTime();
		return dt.toString();
	}

	public static String getNowStringAsIso8601DatetimeWoutZone() {
		LocalDateTime currentDateAndTime = LocalDateTime.now();
		return currentDateAndTime.toString();
	}

	/**
	 * Ör String Iso Date 2022-05-12T10:03:33.935 , Date çevirir
	 *
	 * @param txIsoDateTime
	 */
	public static Date strToIsoDate(String txIsoDateTime) {
		//String txIsoDateTime = "2022-05-12T10:03:33.935";
		try {
			DateTime customDateTimeFromString = new DateTime(txIsoDateTime);
			Date date = customDateTimeFromString.toDate();
			//System.out.println("String To Date: "+ FiDate.datetoString_timestampt2(date));
			return date;
		} catch (Exception ex) {
			Loghelper.get(FiDate.class).debug(FiException.exTosMain(ex));
			return null;
		}
	}

	/**
	 * Date değişkenini 2022-05-12T10:03:33.935 şeklinde IsoDateTime'a çevirir
	 *
	 * @param dtToConvert
	 * @return
	 */
	public static String dateToStrIsoDate(Date dtToConvert) {
		org.joda.time.LocalDateTime currentDateAndTime = org.joda.time.LocalDateTime.fromDateFields(dtToConvert);
		return currentDateAndTime.toString();
	}

	public static Date atEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date atStartOfDay(Date date) {
		if (date == null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


	public static String dateToStrEndOfDayAsIsoDateTime(Date dtToConvert) {
		return FiDate.dateToStrIsoDate(FiDate.atEndOfDay(dtToConvert));
	}

	public static Calendar dateToCalender(Date date) {
		Calendar tCalendar = Calendar.getInstance();
		tCalendar.setTime(date);
		return tCalendar;
	}
}
