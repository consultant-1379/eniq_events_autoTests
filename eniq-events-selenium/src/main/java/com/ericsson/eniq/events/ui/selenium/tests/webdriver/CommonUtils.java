package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.common.DBConnection;
import com.ericsson.eniq.events.ui.selenium.common.constants.TableNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.TimerRangeMaxTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * 
 * @author ekurshi
 * @since 2012
 * 
 */
public abstract class CommonUtils {
    public static final String YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd HH:mm:ss z";

    private static NewEricssonSelenium webDriverSelenium;

    private static WebDriver driver;

    private static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    public static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String YYYY_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.S";

    public static String DD_MM_YY_HH_MM_SS = "dd/MM/yy HH:mm:ss";

    public static final String UTC_TIMEZONE = "UTC";

    public static final String DUBLIN_TIMEZONE = "Europe/Dublin";

    public static String format(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    public static String formatMiliSeconds(int n) {
        if ((n > 9) && (n <= 99)) {
            return "0" + n;
        } else if (n < 10) {
            return "00" + n;
        } else
            return "" + n;
    }

    /**
     * Select default dates and time from calendar
     * 
     * @throws ParseException
     * @throws InterruptedException
     */
    public static void selectDefaultCustomDateTimeRange() throws ParseException, InterruptedException {
        final String startDateStr = "2012-05-16 00:00:00.000";
        final String endDateStr = "2012-05-17 00:00:00.000";
        Date startDate = CommonUtils.parseDate(startDateStr, CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        Date endDate = CommonUtils.parseDate(endDateStr, CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        selectCustomDateTimeRange(startDate, endDate);
    }

    /**
     * Select default dates and time from calendar
     * 
     * @throws ParseException
     * @throws InterruptedException
     */
    public static void selectDefaultCustomDateTimeRange(String startDateStr, String endDateStr) throws ParseException, InterruptedException {
        Date startDate = CommonUtils.parseDate(startDateStr, CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        Date endDate = CommonUtils.parseDate(endDateStr, CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
        selectCustomDateTimeRange(startDate, endDate);
    }

    /**
     * Select the custom date and time from calendar
     * 
     * @param startDate
     * @param endDate
     * @throws InterruptedException
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static void selectCustomDateTimeRange(Date startDate, Date endDate) throws InterruptedException {
        String[] yearMonths = new String[] { "2012 Jan", "2012 Feb", "2012 Mar", "2012 Apr", "2012 May", "2012 Jun", "2012 Jul", "2012 Aug",
                "2012 Sep", "2012 Oct", "2012 Nov", "2012 Dec" };
        String startMonthYear = yearMonths[startDate.getMonth()];
        String endMonthYear = yearMonths[endDate.getMonth()];
        String monthYearPathFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']";
        String previousMonthButtonFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]";
        String dateDivFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//tr[2]//tr[2]//td[text()='"
                + startDate.getDate() + "' and not(contains(@class,'datePickerDayIsFiller'))]";
        String hourDropdownFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";
        String hoursOptionFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div[1]/div//div[text()='"
                + format(startDate.getHours()) + "']";
        String minutesDropdownFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div";
        String minutesOptionFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div[1]/div/div//div[text()='"
                + format(startDate.getMinutes()) + "']";
        String monthYearPathSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']";
        String previousMonthButtonSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]";
        String dateDivSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//td[text()='"
                + endDate.getDate() + "' and not(contains(@class,'datePickerDayIsFiller'))]";
        String hourDropdownSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";
        String hoursOptionSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div[1]/div//div[text()='"
                + format(endDate.getHours()) + "']";
        String minutesDropdownSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div";
        String minutesOptionSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div[1]/div/div//div[text()='"
                + format(endDate.getMinutes()) + "']";

        selectDateTime(startMonthYear, monthYearPathFirst, previousMonthButtonFirst, dateDivFirst, hourDropdownFirst, hoursOptionFirst,
                minutesDropdownFirst, minutesOptionFirst);
        Thread.sleep(1000);
        selectDateTime(endMonthYear, monthYearPathSecond, previousMonthButtonSecond, dateDivSecond, hourDropdownSecond, hoursOptionSecond,
                minutesDropdownSecond, minutesOptionSecond);

        Thread.sleep(1000);
        String okButton = "//button[text()='Ok']";
        driver.findElement(By.xpath(okButton)).click();
        // gotoTop();
    }

    public static void gotoTop() {
        driver.findElement(By.xpath("//body/div[2]")).click();
    }

    private static void selectDateTime(String startMonthYear, String monthYearPath, String previousMonthButton, String dateDiv, String hourDropdown,
                                       String hoursOptions, String minutesDropdown, String minutesOption) throws InterruptedException {
        webDriverSelenium = NewEricssonSelenium.getSharedInstance();
        driver = webDriverSelenium.getWrappedDriver();
        while (true) {// select month start date month and year
            String currentMonthYear = webDriverSelenium.getText(monthYearPath);
            if (startMonthYear.equals(currentMonthYear)) {
                break;
            }
            Thread.sleep(50);
            driver.findElement(By.xpath(previousMonthButton)).click();
        }

        driver.findElement(By.xpath(dateDiv)).click();
        driver.findElement(By.xpath(hourDropdown)).click();
        driver.findElement(By.xpath(hoursOptions)).click();
        driver.findElement(By.xpath(minutesDropdown)).click();
        driver.findElement(By.xpath(minutesOption)).click();
    }

    private static void displayTimezoneInfo(TimeZone t) {
        System.err.println("timezone info:" + t.getID() + ", " + t.getDisplayName() + ", offset:" + (t.getRawOffset() / (60.0 * 60 * 1000))
                + ", daylight:" + (t.getDSTSavings() == 0 ? t.getDSTSavings() : (t.getDSTSavings() / (60.0 * 60 * 1000))));
    }

    /**
     * Converts the date from the TimeZone t1 to the TimeZone t2 using the patterns provided
     * 
     * @param date
     * @param t1
     * @param pattern1
     * @param t2
     * @param pattern2
     * @return
     * @throws ParseException
     */
    public static String convertTimezone(String date, TimeZone t1, String pattern1, TimeZone t2, String pattern2) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(pattern1);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern2);
        parser.setTimeZone(t1);
        formatter.setTimeZone(t2);

        Date d = parser.parse(date);

        return formatter.format(d);
    }

    /**
     * Checks to see if the dateTime is in daylight saving hours and if true - converts the UTC date to Dublin Timezone dateTime otherwise returns the
     * parsed dateTime passed in. This function will be change later to return back date in current time zone.
     * 
     * @param dateTime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date convertToDublinTimeZoneDate(final String dateTime, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (isInDayLight(dateTime, sdf)) {
            String convertTimezone = convertTimezone(dateTime, TimeZone.getTimeZone(CommonUtils.UTC_TIMEZONE), pattern,
                    TimeZone.getTimeZone(CommonUtils.DUBLIN_TIMEZONE), pattern);
            return sdf.parse(convertTimezone);
        }
        return sdf.parse(dateTime);
    }

    /**
     * @param dateTime
     * @param sdf
     * @return
     * @throws ParseException
     */
    private static boolean isInDayLight(final String dateTime, SimpleDateFormat sdf) throws ParseException {
        return TimeZone.getTimeZone(CommonUtils.DUBLIN_TIMEZONE).inDaylightTime(sdf.parse(dateTime));
    }

    /**
     * Convert the UTC date to default timezone date. This function will be change later to return back date in current time zone.
     * 
     * @param START_DATE
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date convertToLocalTimeZoneDate(final String START_DATE, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(convertTimezone(START_DATE, TimeZone.getTimeZone(CommonUtils.UTC_TIMEZONE), pattern, TimeZone.getDefault(), pattern));
    }

    /**
     * Parse date with given format
     * 
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * Convert Dublin timezone date to UTC date to make database query
     * 
     * @param START_DATE
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date convertToUTCTimeZoneDate(final String START_DATE, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(convertTimezone(START_DATE, TimeZone.getTimeZone(CommonUtils.DUBLIN_TIMEZONE), pattern,
                TimeZone.getTimeZone(CommonUtils.UTC_TIMEZONE), pattern));
    }

    public static boolean isElementPresent(String xPath) {
        try {
            return driver.findElement(By.xpath(xPath)) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void main(String[] args) throws ParseException {
        String timeRange = getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        System.err.println(timeRange);
    }

    public static String parseCalenderDateToString(final Calendar calenderDate, final String pattern) {
        return new SimpleDateFormat(pattern).format(calenderDate.getTime());
    }

    /**
     * convert String date/time to calendar i.e to format "yyyy-MM-dd HH:mm:ss.SSS" and convert TimeZone to Dublin TimeZone
     * 
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Calendar convertStringToCalendarAndConvetToDublinTimeZone(String dateStr, String pattern) {
        Date date = null;
        try {
            date = convertToDublinTimeZoneDate(dateStr, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        return cal;
    }

    /**
     * convert String date/time to calendar
     * 
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Calendar convetStringToCanlendar(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;

    }

    public static int getRandomNumber(int min, int max) {
        if (max < 1) {
            return 0;
        }
        Random rn = new Random();
        int n = (max - min) + 1;
        int i = rn.nextInt(max) % n;
        return min + i;
    }

    /**
     * returns closest15min date/time
     * 
     * @param date
     * @return
     */
    public static Calendar getclosest15minDate(Calendar date) {

        Calendar convertedDate = Calendar.getInstance();

        convertedDate.setTime(date.getTime());

        int mins = date.get(Calendar.MINUTE);

        if ((mins % 15) > 0) {
            if (mins >= 45) {
                convertedDate.set(Calendar.MINUTE, 45);
            } else if (mins >= 30 & mins < 45) {
                convertedDate.set(Calendar.MINUTE, 30);
            } else if (mins >= 15 & mins < 30) {
                convertedDate.set(Calendar.MINUTE, 15);
            } else if (mins > 0 & mins < 15) {
                convertedDate.set(Calendar.MINUTE, 0);
            }
        }
        return convertedDate;
    }

    /**
     * Get Max Time from DB for particular Table, and returns formatted time range. Taken into account Daylight saving hours
     * 
     * @param tablename
     * @return
     */
    public static String getCustomTimeRange(final String tablename) {
        Map<String, String> maxTime = DBConnection.getInstance().getDBResult("SELECT MAX(MAX_DATE) AS MAX_DATE FROM " + tablename,
                new TimerRangeMaxTransformer());
        String timeRange = maxTime.get(TimerRangeMaxTransformer.MAX_DATE);
        String formattedTimeRange = getTimeInDublinTimeZone(timeRange);
        return formattedTimeRange;

    }

    /**
     * Gets the formatted time for the timeRange for Dublin Timezone.
     * 
     * @param timeRange
     * @return
     */
    protected static String getTimeInDublinTimeZone(String timeRange) {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_S);
        Date formattedTimeRange = null;
        try {
            formattedTimeRange = convertToDublinTimeZoneDate(timeRange, YYYY_MM_DD_HH_MM_SS_S);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return formatter.format(formattedTimeRange);
    }

    /**
     * Truncate User Preferences Table
     */
    public static void truncateUserPrederencesTable() {
        DBConnection.getInstance().getRunQueryInRepDB("truncate table " + TableNameConstants.USERPREFERENCES_TABLE_NAME);
    }

    /**
     * @param listOfEventTimes
     * @param val
     * @return
     */
    public static int getIndexofElementContains(final List<String> listOfEventTimes, final String val) {
        int index = 0;
        for (int i = 0; i < listOfEventTimes.size(); i++) {
            if (listOfEventTimes.get(i).contains(val)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String dayLightSavingTimeCheck(String eventTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_S);
        Date date = new Date();
        eventTime = (getTimeInDublinTimeZone(dateFormat.format(date).substring(0, 11) + eventTime + dateFormat.format(date).substring(19, 23))
                ).substring(11, 19);
        return eventTime;
    }
}