package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.events.tabs.Tab;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants.*;

/**
 * 
 * @author ekumpen 2012
 */
@Component
public class SessionBrowserTab extends Tab {

	private static WebDriver driver;

	private static final String DEFAULT_TIMEOUT = "30000";

	private static final String TAB_NAME = "SESSION_BROWSER_TAB";

	protected static Logger logger = Logger
			.getLogger(SeleniumLoggerDuplicate.class.getName());

	public SessionBrowserTab() {
		super(TAB_NAME);
	}

	@Override
	public void openTab() {
		webDriverSelenium = NewEricssonSelenium.getSharedInstance();
		driver = webDriverSelenium.getWrappedDriver();
		final String tabName = TAB_NAME;
		final String tabButtonXPath = "//li[contains(@id,'" + tabName
				+ "')]//a[@class='x-tab-right']";
		logger.log(Level.INFO, "The Element ID : " + tabButtonXPath);
		waitForElementToBePresent(tabButtonXPath, "60000");
		webDriverSelenium.click(tabButtonXPath);
	}

	public void openTab(WebDriver driverIn) throws PopUpException {
		webDriverSelenium = NewEricssonSelenium.getSharedInstance();
		// driver = webDriverSelenium.getWrappedDriver();
		driver = driverIn;
		String tabName = TAB_NAME;
		String tabButtonXPath = "//li[contains(@id,'" + tabName
				+ "')]/a[@class='x-tab-right']";
		logger.log(Level.INFO, "The Element ID : " + tabButtonXPath);
		waitForElementToBePresent(tabButtonXPath, DEFAULT_TIMEOUT);
		webDriverSelenium.click(tabButtonXPath);
	}

	/** Waits for an element to be present */
	public void waitForElementToBePresent(final String locator,
			final String timeout) {
		if (!webDriverSelenium.isElementPresent(locator)) {
			webDriverSelenium
					.waitForCondition(
							"var value = selenium.isElementPresent('"
									+ locator.replace("'", "\\'")
									+ "'); value == true", timeout);
		}
	}

	public void openSummarySessionBrowser(final String IMSI) {
		logger.log(Level.INFO, "IMSI : " + IMSI);
		driver.findElement(By.xpath(Constants.SUMMARY_BUTTON)).click();
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				IMSI);
		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();
		driver.findElement(By.xpath(Constants.DROPDOWN_CUSTOM_OPTION)).click();

	}

	public void openDetailsSessionBrowser(final String IMSI) {
		logger.log(Level.INFO, "IMSI : " + IMSI);
		driver.findElement(By.xpath(Constants.DETAILS_BUTTON)).click();
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				IMSI);
		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();
		driver.findElement(By.xpath(Constants.DROPDOWN_CUSTOM_OPTION)).click();
	}

	public void openDetailsSessionBrowser2Hour(final String IMSI) {
		logger.log(Level.INFO, "IMSI : " + IMSI + " Period: 2 hour");
		driver.findElement(By.xpath(Constants.DETAILS_BUTTON)).click();
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				IMSI);
		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();
		driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT)).get(3)
				.click();
	}
	
	/**
	 * Open detail view using Custom Date
	 * @param IMSI
	 * @param start
	 * @param end
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public void openDetailsSessionBrowserCustom(String IMSI, Calendar start, Calendar end, boolean ran, boolean core) throws InterruptedException, ParseException {
		logger.log(Level.INFO, "IMSI : " + IMSI);
		driver.findElement(By.xpath(Constants.DETAILS_BUTTON)).click();
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				IMSI);
		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();
		driver.findElement(By.xpath(Constants.DROPDOWN_CUSTOM_OPTION)).click();
		openCustomDateAndTime(start, end);
		clickLaunchToggleButtons(ran, core);
		Thread.sleep(1000);
		driver.findElement(By.xpath(UPDATE_BUTTON)).click();
		Thread.sleep(1000);
	}
	
	/**
	 * Open summary view using Custom Date
	 * @param IMSI
	 * @param start
	 * @param end
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public void openSummarySessionBrowserCustom(String IMSI, Calendar start, Calendar end, boolean ran, boolean core) throws InterruptedException, ParseException {
		logger.log(Level.INFO, "IMSI : " + IMSI);
		driver.findElement(By.xpath(Constants.SUMMARY_BUTTON)).click();
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				IMSI);
		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();
		driver.findElement(By.xpath(Constants.DROPDOWN_CUSTOM_OPTION)).click();
		openCustomDateAndTime(start, end);
		clickLaunchToggleButtons(ran, core);
		driver.findElement(By.xpath(UPDATE_BUTTON)).click();
	}

	private void clickLaunchToggleButtons(boolean ran, boolean core) {
		if (ran) {
			driver.findElement(By.xpath(RAN_SIGNALLING_BUTTON)).click();
		}
		if (core) {
			driver.findElement(By.xpath(CORE_SIGNALLING_BUTTON)).click();
		}
	}

	/**
	 * Open detail tab with default dates and given IMSI
	 * 
	 * @param imsi
	 * @param ranOn
	 * @param coreOn
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public void openDetailsSessionBrowserWithDefaultDates(final String imsi,
			final boolean ranOn, final boolean coreOn) throws ParseException,
			InterruptedException {
		final String startDateStr = "2012-05-16 00:00:00.000";
		final String endDateStr = "2012-05-17 00:00:00.000";
		final Date startDate = CommonUtils.parseDate(startDateStr,
				CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
		final Date endDate = CommonUtils.parseDate(endDateStr,
				CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
		openDetailsTab(imsi, startDate, endDate, ranOn, coreOn);
	}

	/**
	 * open detail tab with provided dates and IMSI
	 * 
	 * @param imsi
	 * @param startDate
	 * @param endDate
	 * @param ranOn
	 * @param coreOn
	 * @throws InterruptedException
	 */
	public void openDetailsTab(final String imsi, final Date startDate,
			final Date endDate, final boolean ranOn, final boolean coreOn)
			throws InterruptedException {
		openTab();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);// setting
																		// default
																		// delay
		openDetailsSessionBrowser(imsi);
		CommonUtils.selectCustomDateTimeRange(startDate, endDate);
		if (ranOn) {
			driver.findElement(By.xpath(RAN_SIGNALLING_BUTTON)).click();
		}
		if (coreOn) {
			driver.findElement(By.xpath(CORE_SIGNALLING_BUTTON)).click();
		}
		driver.findElement(By.xpath(UPDATE_BUTTON)).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);// resetting
																		// to
																		// default
		Thread.sleep(20000);
	}

	// public void waitSessionActivityPanceToLoadData() throws
	// InterruptedException {
	// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	//
	// }

	/**
	 * use {@link #SessionBrowserUtils.selectDefaultCustomDateTimeRange()}
	 * 
	 * @throws InterruptedException
	 */
	@Deprecated
	public void openCustomDateAndTime() throws InterruptedException {

		final ArrayList<String> listOfmonths = new ArrayList<String>();
		listOfmonths.add("2012 Jan");
		listOfmonths.add("2012 Feb");
		listOfmonths.add("2012 Mar");
		listOfmonths.add("2012 Apr");
		listOfmonths.add("2012 May");
		listOfmonths.add("2012 Jun");
		listOfmonths.add("2012 Jul");
		listOfmonths.add("2012 Aug");
		listOfmonths.add("2012 Sep");
		listOfmonths.add("2012 Oct");
		listOfmonths.add("2012 Nov");
		listOfmonths.add("2012 Dec");
		final String dateNeedToSelectFrom = "2012 May";
		final int index_1 = listOfmonths.indexOf(dateNeedToSelectFrom);
		while (true) {
			final String currentMonth_1 = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']"))
					.getText();
			final int currentMonthIndex = listOfmonths.indexOf(currentMonth_1);
			if (currentMonthIndex == index_1) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();

		}
		driver.findElement(
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//tr[2]//tr[2]//tr[4]//td[3][text()='16']"))
				.click();

		Thread.sleep(2000);
		final String dateNeedToSelectTo = "2012 May";
		final int index_2 = listOfmonths.indexOf(dateNeedToSelectTo);
		while (true) {
			final String currentMonth_2 = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']"))
					.getText();
			final int currentMonthIndex_2 = listOfmonths
					.indexOf(currentMonth_2);
			if (currentMonthIndex_2 == index_2) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();
		}
		driver.findElement(
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//tr[4]//td[5][text()='18']"))
				.click();
		driver.findElement(By.xpath("//button[text()='Ok']")).click();
	}

	/**
	 * use
	 * {@link #SessionBrowserUtils.selectCustomDateTimeRange(Date startDate, Date endDate)}
	 * 
	 * @throws InterruptedException
	 */
	public void openCustomDateAndTime(final String dateNeedToSelectFrom,
			final String dateNeedToSelectTo, final String startDay,
			final String endDay) throws InterruptedException {

		while (true) {
			final String currentMonthFrom = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']"))
					.getText();

			if (currentMonthFrom.equals(dateNeedToSelectFrom)) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();
		}
		driver.findElement(
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//tr[2]//tr[2]//tr//td[text()='"
						+ startDay + "']")).click();

		Thread.sleep(2000);

		while (true) {
			final String currentMonthTo = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']"))
					.getText();

			if (currentMonthTo.equals(dateNeedToSelectTo)) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();
		}
		driver.findElement(
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//tr//td[text()='"
						+ endDay + "']")).click();
		driver.findElement(By.xpath("//button[text()='Ok']")).click();

	}

	/**
	 * This method was create to replace existing methods for custom date
	 * selection which take two date parameters.
	 * 
	 * @param startCal
	 * @param endCal
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public void openCustomDateAndTime(Calendar startCal, Calendar endCal)
			throws InterruptedException, ParseException {

		WebElement dayToSelect;
		
		logger.log(Level.INFO, "Start Time: " + startCal.getTime()
				+ " End Time: " + endCal.getTime());

		String startDay = String.valueOf(startCal.get(Calendar.DAY_OF_MONTH));
		String startMonth = getMonth(startCal.get(Calendar.MONTH) + 1)
				.substring(0, 3);
		String startYear = String.valueOf(startCal.get(Calendar.YEAR));
		int startHrs = startCal.get(Calendar.HOUR_OF_DAY);
		int startMins = startCal.get(Calendar.MINUTE);

		String endDay = String.valueOf(endCal.get(Calendar.DAY_OF_MONTH));
		String endMonth = getMonth(endCal.get(Calendar.MONTH) + 1).substring(0,
				3);
		String endYear = String.valueOf(endCal.get(Calendar.YEAR));
		int endHrs = endCal.get(Calendar.HOUR_OF_DAY);
		int endMins = endCal.get(Calendar.MINUTE);

		String dateNeedToSelectFrom = startYear + " " + startMonth;
		String dateNeedToSelectTo = endYear + " " + endMonth;

		while (true) {
			final String currentMonthFrom = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']"))
					.getText();

			if (currentMonthFrom.equals(dateNeedToSelectFrom)) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();
		}

		dayToSelect = getDayPath("start", startDay);

		dayToSelect.click();

		String hourDropdown = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]//td[1]/div/div/table//tbody/tr/td[2]";
		String minutesDropdown = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]//td[2]/div/div/table//tbody/tr/td[2]";
		String hoursOption = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='"
				+ CommonUtils.format(startHrs) + "']";
		String minutesOption = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='"
				+ CommonUtils.format(startMins) + "']";

		driver.findElement(By.xpath(hourDropdown)).click();
		driver.findElement(By.xpath(hoursOption)).click();
		driver.findElement(By.xpath(minutesDropdown)).click();
		driver.findElement(By.xpath(minutesOption)).click();

		Thread.sleep(2000);

		while (true) {
			final String currentMonthTo = driver
					.findElement(
							By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']"))
					.getText();

			if (currentMonthTo.equals(dateNeedToSelectTo)) {
				break;
			}
			driver.findElement(
					By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]"))
					.click();
		}
		
		dayToSelect = getDayPath("end", endDay);

		dayToSelect.click();
//		driver.findElement(
//				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//tr//td[text()='"
//						+ endDay + "']")).click();

		hourDropdown = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]//td[1]/div/div/table//tbody/tr/td[2]";
		minutesDropdown = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]//td[2]/div/div/table//tbody/tr/td[2]";
		hoursOption = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='"
				+ CommonUtils.format(endHrs) + "']";
		minutesOption = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='"
				+ CommonUtils.format(endMins) + "']";

		driver.findElement(By.xpath(hourDropdown)).click();
		driver.findElement(By.xpath(hoursOption)).click();
		driver.findElement(By.xpath(minutesDropdown)).click();
		driver.findElement(By.xpath(minutesOption)).click();

		driver.findElement(By.xpath("//button[text()='Ok']")).click();

	}

	/**
	 * Created due to a bug. Some dates in the calendar appear twice. This means two xpaths can be found.
	 * This code will handle this and ensure the correct day is selected.
	 * @param type
	 * @param day
	 * @return
	 */
	private WebElement getDayPath(String type, String day) {
		String path = "";
		
		if(type.equals("start")){                 
		      path = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//table[@class='datePickerDays']//td/div[text()='"
		                  + day + "']";
		}
		else if(type.equals("end")){
		      path = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//table[@class='datePickerDays']//td/div[text()='"
		                  + day + "']";
		}

		List<WebElement> paths = driver.findElements(By.xpath(path));
		
		int amount = paths.size();
	
		if(amount > 1){
			if(Integer.valueOf(day) > 20){
				return paths.get(1);
			}
			else
				return paths.get(0);
		}
		
		return paths.get(0);

	}

	protected String getMonth(int month) {
		String[] months = new String[] { "Jan", "Feb",
				"Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		return months[month-1];
	}

	public void selectCustomDateTimeRange(Date startDate, Date endDate)
			throws InterruptedException {
		String[] yearMonths = new String[] { "2012 Jan", "2012 Feb",
				"2012 Mar", "2012 Apr", "2012 May", "2012 Jun", "2012 Jul",
				"2012 Aug", "2012 Sep", "2012 Oct", "2012 Nov", "2012 Dec" };
		String startMonthYear = yearMonths[startDate.getMonth()];
		System.out.println("**Get start date: " + startDate.getMonth() + "  "
				+ startDate.getYear());
		String endMonthYear = yearMonths[endDate.getMonth()];
		String monthYearPathFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']";
		String previousMonthButtonFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]";
		String dateDivFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//tr[2]//tr[2]//td[text()='"
				+ startDate.getDate()
				+ "' and not(contains(@class,'datePickerDayIsFiller'))]";
		String hourDropdownFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";
		String hoursOptionFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div[1]/div//div[text()='"
				+ CommonUtils.format(startDate.getHours()) + "']";
		String minutesDropdownFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div";
		String minutesOptionFirst = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div[1]/div/div//div[text()='"
				+ CommonUtils.format(startDate.getMinutes()) + "']";
		String monthYearPathSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']";
		String previousMonthButtonSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]";
		String dateDivSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//td[text()='"
				+ endDate.getDate()
				+ "' and not(contains(@class,'datePickerDayIsFiller'))]";
		String hourDropdownSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";
		String hoursOptionSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/div/div[1]/div//div[text()='"
				+ CommonUtils.format(endDate.getHours()) + "']";
		String minutesDropdownSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div";
		String minutesOptionSecond = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td[2]/div/div[1]/div/div//div[text()='"
				+ CommonUtils.format(endDate.getMinutes()) + "']";

		selectDateTime(startMonthYear, monthYearPathFirst,
				previousMonthButtonFirst, dateDivFirst, hourDropdownFirst,
				hoursOptionFirst, minutesDropdownFirst, minutesOptionFirst);
		Thread.sleep(1000);
		selectDateTime(endMonthYear, monthYearPathSecond,
				previousMonthButtonSecond, dateDivSecond, hourDropdownSecond,
				hoursOptionSecond, minutesDropdownSecond, minutesOptionSecond);

		Thread.sleep(1000);
		String okButton = "//button[text()='Ok']";
		driver.findElement(By.xpath(okButton)).click();
		// gotoTop();
	}

	private void selectDateTime(String startMonthYear, String monthYearPath,
			String previousMonthButton, String dateDiv, String hourDropdown,
			String hoursOptions, String minutesDropdown, String minutesOption)
			throws InterruptedException {
		while (true) {// select month start date month and year
			String currentMonthYear = driver.findElement(
					By.xpath(monthYearPath)).getText();
			// String currentMonthYear =
			// webDriverSelenium.getText(monthYearPath);

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

	@Override
	protected void clickStartMenu() {
	}

	public void scrollToBottomOfSessionList() {
		int oldPos = 0;
		int newPos = 0;

		do {
			oldPos = getSessionListScrollBarPosition();
			for (int x = 0; x < 10; x++) {
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//div[2]/img[2]"))
						.click();
			}
			newPos = getSessionListScrollBarPosition();
		} while (oldPos < newPos);
	}

	public int getSessionListScrollBarPosition() {
		if (driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//div[2]/div/div"))
				.isDisplayed()) {
			final String scrollAttr = driver
					.findElement(
							By.xpath("//div[@class='sessionScrollPanel']//div[2]/div/div"))
					.getAttribute("style");
			final String[] temp = scrollAttr.split("top:");

			return Integer.parseInt((temp[1].split("px")[0]).trim());
		}

		return 0;
	}

	public static boolean isElementPresent(final String xPath) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			boolean present = NewEricssonSelenium.getSharedInstance()
					.getWrappedDriver().findElement(By.xpath(xPath)) != null;
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			return present;
		} catch (final NoSuchElementException e) {
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			return false;
		}
	}

	public int getDBSRecordPosition(final List<WebElement> table) {

		final String dbsValue = "Data Bearer Session";
		for (int i = 0; i < table.size(); i++) {
			final String text = table.get(i).getText().trim();
			if (dbsValue.equals(text)) {
				return i;
			}
		}
		return -1;
	}

	@Deprecated
	/**
	 * Should not use this method because it overrides the default delay(which is 3seconds) to specified interval. Every time when element not find it will wait for specified time. Should use Thread.delay() instead of this. In case if it is required that after performing the task reset to original value. 
	 */
	public void waitForPageToLoad() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void waitForSummaryLoadingToComplete() {
		try {
			do {
				Thread.sleep(1000);

			} while (driver
					.findElement(
							By.xpath("//div[text()='Loading Data for TCP Performance ...']"))
					.isDisplayed());
		} catch (final Exception ex) {
			// ignore
		}
	}

	public void launchViewDetais() {
		final List<WebElement> sessionListTable = driver.findElements(By
				.xpath(Constants.DBS_SESSION_LIST));
		getDBSRecordPosition(sessionListTable);
		if (driver.findElement(By.xpath(Constants.DBS_SESSION_LIST))
				.isDisplayed()) {
			final int position = getDBSRecordPosition(sessionListTable);

			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + position) + "]/td[2]")).click();
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
		}
	}

	/**
	 * Opens the event in Summary view with only default DBS turned on
	 * 
	 * @param IMSI
	 * @throws InterruptedException
	 */
	public void openEventSummary(final String IMSI) throws InterruptedException {
		openTab();
		waitForPageToLoad();
		openSummarySessionBrowser(IMSI);
		openCustomDateAndTime("2012 May", "2012 May", "16", "18");
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	/**
	 * Opens the event in Detail view with only default DBS turned on
	 * 
	 * @param IMSI
	 * @throws InterruptedException
	 */
	public void openEventDetails(final String IMSI) throws InterruptedException {
		openTab();
		waitForPageToLoad();
		openDetailsSessionBrowser(IMSI);
		openCustomDateAndTime();
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	/**
	 * Opens the event with Core Signalling turned on
	 * 
	 * @param IMSI
	 * @throws InterruptedException
	 */
	public void openEventDetailsWithCoreSignalling(final String IMSI)
			throws InterruptedException {
		openTab();
		waitForPageToLoad();
		openDetailsSessionBrowser(IMSI);
		openCustomDateAndTime();
		driver.findElement(By.xpath(Constants.CORE_SIGNALLING_BUTTON)).click();
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	/**
	 * Opens the event with Ran Signalling turned on
	 * 
	 * @param IMSI
	 * @throws InterruptedException
	 */
	public void openEventDetailsWithRanSignalling(final String IMSI)
			throws InterruptedException {
		openTab();
		waitForPageToLoad();
		openDetailsSessionBrowser(IMSI);
		openCustomDateAndTime();
		driver.findElement(By.xpath(Constants.RAN_SIGNALLING_BUTTON)).click();
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	/**
	 * Opens the event with Core and Ran Signalling turned on
	 * 
	 * @param IMSI
	 * @throws InterruptedException
	 */
	public void openEventDetailsWithCoreAndRanSignalling(final String IMSI)
			throws InterruptedException {
		openTab();
		waitForPageToLoad();
		openDetailsSessionBrowser(IMSI);
		openCustomDateAndTime();
		driver.findElement(By.xpath(Constants.CORE_SIGNALLING_BUTTON)).click();
		driver.findElement(By.xpath(Constants.RAN_SIGNALLING_BUTTON)).click();
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	/**
	 * This method will parse a style of an element and return the required
	 * fields value, e.g. top, width or height
	 * 
	 * @param style
	 * @param field
	 * @return the parsed integer
	 */
	public int parseStyle(final String style, final String field) {
		final String[] temp = style.split(field + ":");

		return Integer.parseInt((temp[1].split("px")[0]).trim());
	}

	/**
	 * This method will give the index of any event given the event name and
	 * session table
	 * 
	 * @param eventName
	 * @param table
	 * @return
	 */
	public int getRecordPosition(final String eventName,
			final List<WebElement> table) {
		for (int i = 0; i < table.size(); i++) {
			final String text = table.get(i).getText().trim();
			if (text.equals(eventName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Check whether or not a particular event is in the table
	 * 
	 * @param expected
	 * @param table
	 * @return
	 */
	public boolean isEventFound(final String expected,
			final List<WebElement> table) {
		for (final WebElement row : table) {
			final String text = row.getText().trim();
			if (expected.equals(text)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the event name as it is displayed in the session list table
	 * 
	 * @param value
	 * @param table
	 * @return
	 */
	public String getEventName(final String value, final List<WebElement> table) {

		for (final WebElement row : table) {
			final String text = row.getText().trim();
			if (text.equals(value)) {
				return text;
			}
		}
		return null;
	}

	public WebElement getRadioAndCellConditionsChartSVGElement() {
		return driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_SVG));
	}

	public void mouseOver(final WebElement element) {
		try {
			element.click();
			final Actions builder = new Actions(driver);
			final Action action = builder.moveToElement(element).build();
			action.perform();
			Thread.sleep(5000L);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

}