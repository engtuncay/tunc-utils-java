package ozpasyazilim.utils.core;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Get MonthStart , YearStart, DateDiff gibi metodlar
 */
public class FiCal {

    private Date dateBuffer;

    public FiCal() {
    }

    public static FiCal build() {
        return new FiCal();
    }

    public static FiCal buildNow() {
        return new FiCal(new Date());
    }

    public FiCal(Date dateBuffer) {
        setDateBuffer(dateBuffer);
    }

    public static Integer getPrevMonthNumber() {
        return ((FiCal.getMonthNumberBegin0() - 1) % 12) + 1; // 0-1=-1%12=11 + 1 = 12
    }

    public static Integer getPrevMonthNumber(Date dtValue) {
        return ((FiCal.getMonthNumberBegin0(dtValue) - 1 + 12) % 12) + 1; // 0-1=(-1+12)%12=11 + 1 = 12
    }

    public static Date setDayofDate(Date dtSon, int dayNo) {
        Calendar calendar = convertToCal(dtSon);
        clearTime(calendar);
        // set Date to 1, so first date of previous month
        calendar.set(Calendar.DATE, dayNo);
        return calendar.getTime();
    }

    public FiCal buildDateDiffNow(int dayDifference) {
        Calendar cal = Calendar.getInstance();
        clearTime(cal);
        // Calendar.Date günü gösteriyor
        cal.add(Calendar.DATE, dayDifference);
        Date result = cal.getTime();
        setDateBuffer(result);
        return this;
    }

    public FiCal buildDateTimeDiff(int dayDifference) {
        Calendar aCalendar = Calendar.getInstance();
        // Calendar.Date günü gösteriyor
        aCalendar.add(Calendar.DATE, dayDifference);
        Date result = aCalendar.getTime();
        setDateBuffer(result);
        return this;
    }

    public Date getDateBuffer() {
        return dateBuffer;
    }

    public void setDateBuffer(Date dateBuffer) {
        this.dateBuffer = dateBuffer;
    }

    public String dateToString_yyyymmdd() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(getDateBuffer());
    }

    public static Integer getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static Integer getYear(Date dateGiven) {
        return convertToCal(dateGiven).get(Calendar.YEAR);
    }

    /**
     * Ocak 1'den başlayarak, anın ay nosu
     *
     * @return
     */
    public static Integer getMonthNumber() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * Ocak Ayı 0 indeksinden başlar
     *
     * @return
     */
    public static Integer getMonthNumberBegin0() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * Ocak Ayı 0 indeksinden başlar
     *
     * @param dtNow
     * @return
     */
    public static Integer getMonthNumberBegin0(Date dtNow) {
        Calendar calendar = convertToCal(dtNow);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Ocak 1'den başlayarak olan Ay no
     *
     * @param dateGiven
     * @return
     */
    public static Integer getMonthNumber(Date dateGiven) {
        return convertToCal(dateGiven).get(Calendar.MONTH) + 1;
    }

    public static Integer getDayNumber() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getDayNumber(Date dtNow) {
        Calendar calendar = convertToCal(dtNow);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getHour() {
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    public static Integer getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static Integer getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    public static Boolean isBetweenOrEqual(Date selectedDate, Date selectedDate1, Date selectedDate2) {

        //final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        //LocalDate.parse(txFirstInput, formatter);

        final LocalDate firstDate = FiDate.dateToLocalDate(selectedDate);
        final LocalDate secondDate = FiDate.dateToLocalDate(selectedDate1);
        final LocalDate thirdate = FiDate.dateToLocalDate(selectedDate2);
        final long daydiff1 = ChronoUnit.DAYS.between(firstDate, secondDate);
        final long daydiff2 = ChronoUnit.DAYS.between(firstDate, thirdate);

        if (daydiff1 >= 0 && daydiff2 <= 0) return true;

        return false;
    }

    public static Integer compareDate(Date selectedDate1, Date selectedDate2) {

        final LocalDate firstDate = FiDate.dateToLocalDate(selectedDate1);
        final LocalDate secondDate = FiDate.dateToLocalDate(selectedDate2);
        final long daydiff1 = ChronoUnit.DAYS.between(firstDate, secondDate);

        if (daydiff1 > 0) {
            return 1;
        } else if (daydiff1 < 0) {
            return -1;
        } else {
            return 0;
        }
//		if (daydiff1 == 0) return 0;
//
//		return null;
    }

    /**
     * date1 - date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer compareDate2(Date date1, Date date2) {

        final String txDate1 = FiDate.toStringTimestamptPlain(date1);
        final String txDate2 = FiDate.toStringTimestamptPlain(date2);

        return txDate1.compareTo(txDate2);
    }

    //	Date dt = new Date();
    //	DateTime dtOrg = new DateTime(dt);
    //	DateTime dtPlusOne = dtOrg.plusDays(1);
    //	LocalDateTime.from(dt.toInstant()).plusDays(1);

    /**
     * returns dtX-dtY
     *
     * @param dtX
     * @param dtY
     * @return
     */
    public static Long dateDiffxByDay(Date dtX, Date dtY) {
        return dateDiffByDay2(dtY, dtX);
    }

    /**
     * End Date - Start Date
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long dateDiffByDay2(Date startDate, Date endDate) {
        LocalDate lcStart = FiCal.convertLocalDate(startDate);
        LocalDate lcEnd = FiCal.convertLocalDate(endDate);
        long daysBetween = ChronoUnit.DAYS.between(lcStart, lcEnd);
        return daysBetween;
    }

    /**
     * date1 Less Than date2 Then True Else False
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean dateLessThan(Date date1, Date date2) {
        LocalDate lcDate1 = FiCal.convertLocalDate(date1);
        LocalDate lcDate2 = FiCal.convertLocalDate(date2);
        long daysBetween = ChronoUnit.DAYS.between(lcDate2, lcDate1); // lcDate1-lcDate2 10-15=-5 10-8=+2
//		Loghelper.get(FiCal.class).debug("Days Between:"+ daysBetween);
        return daysBetween < 0;
    }

    public static Calendar convertToCal(Date date) {
        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(date);
        return tCalendar;
    }


    /**
     * Compute the number of calendar days between two Calendar objects.
     * The desired value is the number of days of the month between the
     * two Calendars, not the number of milliseconds' worth of days.
     *
     * @param startCal The earlier calendar
     * @param endCal   The later calendar
     * @return the number of calendar days of the month between startCal and endCal
     */
    public static long dateDiffByDay(Calendar startCal, Calendar endCal) {

        // Create copies so we don't update the original calendars.

        Calendar start = Calendar.getInstance();
        start.setTimeZone(startCal.getTimeZone());
        start.setTimeInMillis(startCal.getTimeInMillis());

        Calendar end = Calendar.getInstance();
        end.setTimeZone(endCal.getTimeZone());
        end.setTimeInMillis(endCal.getTimeInMillis());

        // Set the copies to be at midnight, but keep the day information.

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        // At this point, each calendar is set to midnight on
        // their respective days. Now use TimeUnit.MILLISECONDS to
        // compute the number of full days between the two of them.

        return TimeUnit.MILLISECONDS.toDays(Math.abs(end.getTimeInMillis() - start.getTimeInMillis()));
    }

    public static Date getDateYearBegin() {
        Calendar cal = getCalendarWoutTime(); // new GregorianCalendar();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        clearTime(cal);
        return cal.getTime();
    }

    public static Date getDatePrevYearBegin() {
        Calendar cal = getCalendarWoutTime(); // new GregorianCalendar();
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    private static Calendar getCalendarWoutTime() {
        return clearTime(Calendar.getInstance());
    }

    public static Date getYearEnd() {
        Calendar calDate = Calendar.getInstance();
        clearTime(calDate);
        calDate.set(Calendar.DAY_OF_YEAR, calDate.getActualMaximum(Calendar.DAY_OF_YEAR));
        return calDate.getTime();
    }


    public static Date getDatePrevMonthBegin() {
        // geçen ayı bulmak
        Calendar calendar = Calendar.getInstance();
        clearTime(calendar);
        // add -1 month to current month
        calendar.add(Calendar.MONTH, -1);
        // set Date to 1, so first date of previous month
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date getDateTwoMonthsAgoBegin() {
        // geçen ayı bulmak
        Calendar calendar = Calendar.getInstance();
        clearTime(calendar);
        // add -1 month to current month
        calendar.add(Calendar.MONTH, -2);
        // set Date to 1, so first date of previous month
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date getDatePrevMonthBegin(Date dtNow) {
        Calendar calendar = convertToCal(dtNow);
        clearTime(calendar);
        calendar.add(Calendar.MONTH, -1);
        // set Date to 1, so first date of previous month
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, Integer amount) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();

    }


    public static Date addDay(Date date, Integer countDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, countDay);
        return calendar.getTime();

    }

    public static Date addDayIgnoreTime(Date date, Integer countDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DATE, countDay);

        return calendar.getTime();

    }

    public static Calendar clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }


    public static Date getDatePrevMonthEnd() {

        Calendar aCalendar = Calendar.getInstance();

        clearTime(aCalendar);

        aCalendar.set(Calendar.DATE, 1);
        // Calendar.Date günü gösteriyor
        aCalendar.add(Calendar.DATE, -1);

        Date result = aCalendar.getTime();

        return result;

    }

    public static Date getDateTwoMonthsAgoEnd() {

        Calendar aCalendar = Calendar.getInstance();

        clearTime(aCalendar);

        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, 1);
        // Calendar.Date günü gösteriyor
        aCalendar.add(Calendar.DATE, -1);

        Date result = aCalendar.getTime();

        return result;

    }

    /**
     * year is not changed
     *
     * @param lnMonthNo (1-12)
     * @return
     */
    public static Date getMonthEndDay(Integer lnMonthNo) {

        if (lnMonthNo == null) return null;

        if (lnMonthNo == 12) lnMonthNo = 0;

        Calendar cal = Calendar.getInstance();
        clearTime(cal);
        // aylar 0 başladığı , bir sonraki ay olmuş oluyor
        cal.set(Calendar.MONTH, lnMonthNo);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.YEAR, Year.now().getValue());

        return cal.getTime();
    }

    public static Date getDateMonthBegin() {
        Calendar cal = Calendar.getInstance();
        clearTime(cal);
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getDateMonthEnd() {

        Calendar cal = Calendar.getInstance();
        clearTime(cal);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }


    public static Date getTodayWoTime() {

        Calendar cal = Calendar.getInstance();
        clearTime(cal);
        //cal.set(Calendar.Date, 1);
        return cal.getTime();

    }

    public static LocalDate convertLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static LocalDate convertLocalDate(String strDateyyyymmdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(strDateyyyymmdd, formatter);
        return localDate;
    }

    public static Date getYesterday() {

        Calendar aCalendar = Calendar.getInstance();
        clearTime(aCalendar);
        // Calendar.Date günü gösteriyor
        aCalendar.add(Calendar.DATE, -1);

        Date result = aCalendar.getTime();
        return result;
    }

    /**
     * Bugün den dayDifference kadar +,- günün tarihini verir.
     *
     * @param dayDifference
     * @return
     */
    public static Date getDateDiffFromToday(int dayDifference) {
        Calendar aCalendar = Calendar.getInstance();
        clearTime(aCalendar);
        // Calendar.Date günü belirtiyor
        aCalendar.add(Calendar.DATE, dayDifference);
        Date result = aCalendar.getTime();
        return result;
    }

    public static Date getDateDiffFromDate(Date date, int dayDifference) {
        Calendar aCalendar = FiDate.dateToCalender(date);
        clearTime(aCalendar);
        // Calendar.Date günü belirtiyor
        aCalendar.add(Calendar.DATE, dayDifference);
        return aCalendar.getTime();
    }

    public static Date getDateDiffFromToday(int lndayDifference, int lnMonthDiff) {
        Calendar aCalendar = Calendar.getInstance();
        clearTime(aCalendar);
        // Calendar.Date günü belirtiyor
        aCalendar.add(Calendar.DATE, lndayDifference);
        aCalendar.add(Calendar.MONTH, lnMonthDiff);
        Date result = aCalendar.getTime();
        return result;
    }

    public static int getNowHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return hours;
    }

    public static int getNowMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int minutes = calendar.get(Calendar.MINUTE);
        return minutes;
    }

    public static int getNowSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int seconds = calendar.get(Calendar.SECOND);
        return seconds;
    }

    public static Boolean isDaySunday() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }


}
