package ozpasyazilim.utils.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FiCalw {

	private Date dateStored;

	public FiCalw() {}

	public FiCalw(Date dateStored) {
		setDateStored(dateStored);
	}

	public static FiCalw build(){
		return new FiCalw();
	}

	public static FiCalw buildNow(){
		return new FiCalw(new Date());
	}

	public static FiCalw buildNowOnlyDate(){
		return new FiCalw(new Date()).clearTime();
	}

	public Date getDateStored() {
		return dateStored;
	}

	public void setDateStored(Date dateStored) {
		this.dateStored = dateStored;
	}

	// bitirici metodlar

	public String dateToString_yyyymmdd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(getDateStored());
	}

	// wrap methods ve eski metodlar

//	public static Integer getYear() {
//		return Calendar.getInstance().get(Calendar.YEAR);
//	}

//	public Date getYearBeginning() {
//
//		Calendar cal = Calendar.getInstance(); // new GregorianCalendar();
//		cal.set(Calendar.DAY_OF_YEAR, 1);
//		return cal.getTime();
//	}
//
//	public Date getYearEnd() {
//
//		Calendar calDate = Calendar.getInstance();
//
//		//calDate.set(Calendar.DAY_OF_YEAR, 1);
//		//Date yearStartDate = calDate.getTime();
//
//		calDate.set(Calendar.DAY_OF_YEAR, calDate.getActualMaximum(Calendar.DAY_OF_YEAR));
//		clearTime(calDate);
//
//		Date yearEndDate = calDate.getTime();
//
//		return yearEndDate;
//	}
//
//
//	public Date getDateGecenAyBas() {
//
//		// geçen ayı bulmak
//		Calendar calendar = Calendar.getInstance();
//		// add -1 month to current month
//		calendar.add(Calendar.MONTH, -1);
//		// set Date to 1, so first date of previous month
//		calendar.set(Calendar.DATE, 1);
//
//		return calendar.getTime();
//
//	}
//
//	public Date addMonth(Date date, Integer amount) {
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.MONTH, amount);
//		return calendar.getTime();
//
//	}
//
//
//	public Date addDay(Date date, Integer countDay) {
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.DATE, countDay);
//		return calendar.getTime();
//
//	}
//
	public FiCalw addDayIgnoreTime(Date date, Integer countDay) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DATE, countDay);

		return new FiCalw(calendar.getTime());
	}

	public FiCalw clearTime() {  //Calendar calendar

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateStored());
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		setDateStored(calendar.getTime());
		return this;
	}

//
//	public Date getDateGecenAySonu() {
//
//		Calendar aCalendar = Calendar.getInstance();
//
//		clearTime(aCalendar);
//
//		aCalendar.set(Calendar.DATE, 1);
//		// Calendar.Date günü gösteriyor
//		aCalendar.add(Calendar.DATE, -1);
//
//		Date result = aCalendar.getTime();
//
//		return result;
//
//	}
//
//	public Date getDateAyBasi() {
//		Calendar aCalendar = Calendar.getInstance();
//		clearTime(aCalendar);
//		aCalendar.set(Calendar.DATE, 1);
//		return aCalendar.getTime();
//
//	}
//
//	public Date getDateBugun() {
//
//		Calendar aCalendar = Calendar.getInstance();
//		clearTime(aCalendar);
//		//aCalendar.set(Calendar.Date, 1);
//		return aCalendar.getTime();
//
//	}
//
//
//	public static void main(String[] args) {
//		System.out.println(new FiCalw().getDateGecenAySonu().toString());
//	}
//
//
//	public String getDateAndTime() {
//
//
//
//
//
//		return null;
//	}
//
//	public LocalDate convertLocalDate(Date date){
//		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		return localDate;
//	}
//
//	public LocalDate convertLocalDate(String strDateyyyymmdd){
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		LocalDate localDate = LocalDate.parse(strDateyyyymmdd, formatter);
//		return localDate;
//	}
//
//	public Date getYesterday() {
//
//		Calendar aCalendar = Calendar.getInstance();
//		clearTime(aCalendar);
//		// Calendar.Date günü gösteriyor
//		aCalendar.add(Calendar.DATE, -1);
//
//		Date result = aCalendar.getTime();
//		return result;
//	}
//
//	public Date getDateDiff(int dayDifference) {
//		Calendar aCalendar = Calendar.getInstance();
//		clearTime(aCalendar);
//		// Calendar.Date günü gösteriyor
//		aCalendar.add(Calendar.DATE, dayDifference);
//		Date result = aCalendar.getTime();
//		return result;
//	}


}
