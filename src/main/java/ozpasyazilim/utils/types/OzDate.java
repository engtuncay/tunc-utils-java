package ozpasyazilim.utils.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Deprecated
public class OzDate extends Date implements java.io.Serializable, Cloneable, Comparable<Date> {

	private static final long serialVersionUID = 7523967970034938905L;

	//Locale locale = new Locale("tr", "TR");

	public static void main(String[] args) {

		//System.out.println(new FiCal().getDateGecenAySonu().toString());
		//System.out.println("Date -30:"+ new OzDate().calDayDiffIgnoreTime(-1));
		System.out.println(" Int Date:"+ new OzDate().intDate());
	}

	public OzDate() {
		super();
		//this(System.currentTimeMillis());
	}

	public OzDate(Date time) {
		this.setTime(time.getTime());
	}

	public OzDate(String dateyyyymmdd) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		setTime(formatter.parse(dateyyyymmdd).getTime());
	}

	public OzDate(LocalDate localDate) {
		setTime(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()) ;
	}

	public OzDate convertLocalDateToSimpleDate(LocalDate localDate) {

		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return new OzDate(date.getTime());

	}



	public OzDate(long date) {
		super(date);
	}

	public static OzDate buildDate(){
		return new OzDate();
	}

	public LocalDate getLocalDate(){
		LocalDate localDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}

	public Integer calYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public OzDate calYearBeginning() {

		Calendar cal = Calendar.getInstance(); // new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return new OzDate(cal.getTimeInMillis());

	}


	public Date calYearEnd() {

		Calendar calDate = Calendar.getInstance();

		//calDate.set(Calendar.DAY_OF_YEAR, 1);
		//Date yearStartDate = calDate.getTime();

		calDate.set(Calendar.DAY_OF_YEAR, calDate.getActualMaximum(Calendar.DAY_OF_YEAR));
		clearTime(calDate);

		Date yearEndDate = calDate.getTime();

		return yearEndDate;
	}


	public OzDate calDateGecenAyBas() {

		// geçen ayı bulmak
		Calendar calendar = Calendar.getInstance();

		// add -1 month to current month
		calendar.add(Calendar.MONTH, -1);
		// set Date to 1, so first date of previous month
		calendar.set(Calendar.DATE, 1);

		return new OzDate(calendar.getTimeInMillis());

	}

	public void clearTime(Calendar calendar) {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

	}

	public OzDate clearTime() {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(this);

		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		setTime(calendar.getTimeInMillis());

		return this;
	}

	public OzDate calDateGecenAySonu() {

		Calendar aCalendar = Calendar.getInstance();
		clearTime(aCalendar);

		aCalendar.set(Calendar.DATE, 1);
		// Calendar.Date günü gösteriyor
		aCalendar.add(Calendar.DATE, -1);

		return new OzDate(aCalendar.getTimeInMillis());

	}

	public OzDate calYesterday() {

		Calendar aCalendar = Calendar.getInstance();
		clearTime(aCalendar);
		aCalendar.add(Calendar.DATE, -1);

		return new OzDate(aCalendar.getTimeInMillis());

	}

	public OzDate calDateAyBasi() {

		Calendar aCalendar = Calendar.getInstance();
		clearTime(aCalendar);
		aCalendar.set(Calendar.DATE, 1);

		return new OzDate(aCalendar.getTimeInMillis());
	}

	public Date calDateBugun() {

		Calendar aCalendar = Calendar.getInstance();
		clearTime(aCalendar);
		//aCalendar.set(Calendar.Date, 1);
		return aCalendar.getTime();

	}

	public Date stringToDate_yyyymmdd(String date) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.parse(date);

		/*} catch (ParseException e) {
			// Loghelper.getInstance(UtilDate.class).info("Hata :" + UtilModel.exceptiontostring(e));
			return null;
		}*/

	}





	public String getDateAndTime() {


		return null;
	}

	public LocalDate convertLocalDate(Date date){
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}



	public LocalDate convertLocalDate(String strDateyyyymmdd){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDate = LocalDate.parse(strDateyyyymmdd, formatter);
		return localDate;
	}


	public Date getDateObj() {
		return new Date(this.getTime());
	}

	public OzDate calDayDiffIgnoreTime(Integer countDay) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DATE, countDay);

		return new OzDate(calendar.getTime());

	}

	public String strShortDateSlash(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(this);
	}


	public Integer intDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String str = formatter.format(this);
		return Integer.parseInt(str);
	}
}
