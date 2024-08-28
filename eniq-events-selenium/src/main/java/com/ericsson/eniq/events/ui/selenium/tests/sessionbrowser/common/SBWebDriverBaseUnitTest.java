package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author ekumpen 2012
 */
public abstract class SBWebDriverBaseUnitTest extends
		EniqEventsWebDriverBaseUnitTest {
	@Autowired
	private SessionBrowserTab sessionbrowserTab;

	protected static Logger logger = Logger.getLogger(SeleniumLogger.class
			.getName());

	private static final String TAB_NAME = "SESSION_BROWSER_TAB";

	private static final String DEFAULT_TIMEOUT = "30000";

	public void openTab() throws PopUpException {
		webDriverSelenium = NewEricssonSelenium.getSharedInstance();
		driver = webDriverSelenium.getWrappedDriver();
		// driver = driverIn;
		String tabName = TAB_NAME;
		String tabButtonXPath = "//li[contains(@id,'" + tabName
				+ "')]//a[@class='x-tab-right']";
		logger.log(Level.INFO, "The Element ID : " + tabButtonXPath);
		waitForElementToBePresent(tabButtonXPath, DEFAULT_TIMEOUT);
		webDriverSelenium.click(tabButtonXPath);
	}

	/**
	 * Entry point method to open an event in session detail view
	 * 
	 * @param parameter
	 * @param ranOn
	 * @param coreOn
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	protected void openSessionDetail(CommonDataType parameter, boolean ranOn,
			boolean coreOn) throws ParseException, InterruptedException {

		String imsi = getIMSIParameter(parameter);
		String dbDate = getEventTimeParameter(parameter);

		Calendar start = getclosest15minStartDate(convertStringToCalendar(
				dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
		Calendar end = getclosest15minEndDate(convertStringToCalendar(dbDate,
				"yyyy-MM-dd HH:mm:ss.SSS"));

		sessionbrowserTab.openTab();
		sessionbrowserTab.waitForPageToLoad();
		sessionbrowserTab.openDetailsSessionBrowserCustom(imsi, start, end,
				ranOn, coreOn);
		
		zoom(true,1);
	}
	
	/**
	 * Entry point method to open an event in session summary view
	 * 
	 * @param parameter
	 * @param ranOn
	 * @param coreOn
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	protected void openSessionSummary(CommonDataType parameter, boolean ranOn,
			boolean coreOn) throws ParseException, InterruptedException {

		String imsi = getIMSIParameter(parameter);
		String dbDate = getEventTimeParameter(parameter);

		Calendar start = getclosest15minStartDate(convertStringToCalendar(
				dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
		Calendar end = getclosest15minEndDate(convertStringToCalendar(dbDate,
				"yyyy-MM-dd HH:mm:ss.SSS"));

		sessionbrowserTab.openTab();
		sessionbrowserTab.waitForPageToLoad();
		sessionbrowserTab.openSummarySessionBrowserCustom(imsi, start, end,
				ranOn, coreOn);

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

	// Need to replace var-arg with String
	protected String RAN_SignallingEvents(List<WebElement> list,
			String... requiredEvent) {

		for (WebElement row : list) {
			String text = row.getText().trim();
			if (requiredEvent[0].equals(text)) {
				return text;
			}
		}
		return null;
	}

	/**
	 * Method to lookup reserved data to retrieve Session Browser IMSI
	 * 
	 * @param lookupParameter
	 * @return
	 */
	protected String getIMSIParameter(CommonDataType lookupParameter) {

		String[] inputs = reservedDataHelper.getCommonReservedData(
				lookupParameter).split(",");

		return inputs[0].trim();
	}

	/**
	 * Method to lookup reserved data to retrieve Session Browser event time
	 * 
	 * @param lookupParameter
	 * @return
	 */
	protected String getEventTimeParameter(CommonDataType lookupParameter) {

		String[] inputs = reservedDataHelper.getCommonReservedData(
				lookupParameter).split(",");

		return inputs[1].trim();
	}

	// Need to replace var-arg with String
	protected int RAN_SignallingPosition(List<WebElement> sessionList,
			String... requiredEvent) {

		for (int i = 0; i < sessionList.size(); i++) {
			String text = sessionList.get(i).getText().trim();
			if (requiredEvent[0].equals(text)) {
				return i;
			}
		}
		return -1;
	}

	protected List<String> getViewDetailsHeadings() {
		List<String> headings = new ArrayList<String>();
		List<WebElement> visibleSectionSize = driver.findElements(By
				.xpath(Constants.COLLAPSABLE_HEADINGS));
		for (int i = 1; i <= visibleSectionSize.size(); i++) {
			boolean flag = driver
					.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]/div[2]")).isDisplayed();
			if (!flag) {
				scrollViewDetailsSectionList(false,40);
			}
			
			String heading = driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
							+ i + "]//span[1]")).getText();
			
			if(heading.length() > 0){
				headings.add(heading);
			}
		}

		return headings;
	}

	protected List<WebElement> getSectionHeadings(int index) {

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ index + "]/div[2]")).getAttribute("style")
				.isEmpty();

		if (!sectionVisiable) {
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
							+ index + "]//span[2]")).click();
		}

		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ index + "]/div[2]//tr/td[1]"));
	}

	protected List<WebElement> getSectionValues(int index) {

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ index + "]/div[2]")).getAttribute("style")
				.isEmpty();

		if (!sectionVisiable) {
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
							+ index + "]//span[2]")).click();
		}

		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ index + "]/div[2]//tr/td[2]"));
	}

	protected Boolean isRanEventFound(String event) {

		List<WebElement> l = null;
		Boolean found = false;
		String path = Constants.RAN_DATA_SIGNALLING + "[text()='" + event
				+ "']";
		l = driver.findElements(By.xpath(path));

		if (!l.isEmpty()) {
			found = true;
		} else {
			found = false;
		}

		return found;
	}

	protected int getViewDetailsScrollBarPosition(String style) {
		String[] temp = style.split("top:");

		return Integer.parseInt((temp[1].split("px")[0]).trim());
	}

	protected void toggleSectionsOfViewDetailsPopup() {
		List<WebElement> visiableSections = driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));

		for (int i = 1; i <= visiableSections.size(); i++) {

			boolean visiable = driver
					.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]/div[2]")).getAttribute("style")
					.isEmpty();

			if (visiable) {
				// Section is expanded so collapse it
				scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")));
				driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")).click();
				assertFalse(
						"The expanded heading did not collapse : " + i,
						driver.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
								.getAttribute("style").isEmpty());

			} else {
				// Section is collapse so expand it
				driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")).click();
				assertTrue(
						"The collapsed heading did not expand : " + i,
						driver.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2][@style='']"))
								.isDisplayed());
			}
		}
	}

	protected void openViewDetails(int position) {
		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
						+ (position) + "']/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
	}
	
	protected boolean isViewDetailsMaximized() {
		int oldHeight = sessionbrowserTab.parseStyle(
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.getAttribute("style"), "height");
		int oldWidth = sessionbrowserTab.parseStyle(
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.getAttribute("style"), "width");
		
		driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW_MAXIMIZE))
		.click();
		
		int newHeight = sessionbrowserTab.parseStyle(
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.getAttribute("style"), "height");
		int newWidth = sessionbrowserTab.parseStyle(
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.getAttribute("style"), "width");
		
		
		if(newHeight > oldHeight && newWidth > oldWidth){
			return true;
		}
		else{
			return false;
		}
	}

	protected void waitUntilTCPPerformanceIsLoaded()
			throws InterruptedException {
		//In some incidences seen in regression testing, this method waits forever
		//modifying it to attempt to check the web element 10 times (can't relay on sleep as driver.findElements has an inbuilt wait)
		int check_attempts = 10;
		System.out.println("Path: " + Constants.TCP_PERFORMANCE_PANE_DIVS);
		while (driver.findElements(
				By.xpath(Constants.TCP_PERFORMANCE_PANE_DIVS)).size() != 4 && check_attempts > 0) {
			Thread.sleep(1000);
			check_attempts -= 1;
		}
	}

	public int evaluateEvent(String eventName, String eventIMSI,
			SessionBrowserTab s) throws InterruptedException {
		s.openEventDetailsWithRanSignalling(eventIMSI);
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		int index = s.getRecordPosition(eventName, sessionTable);
		assertTrue(eventName + " not found in list", index >= 0);

		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
						+ (2 + index) + "]/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

		return index;
	}

	protected Calendar convertStringToCalendar(String dateStr, String pattern) {
		Date date = null;
		try {
			// date = new SimpleDateFormat(pattern).parse(dateStr);
			date = CommonUtils.convertToDublinTimeZoneDate(dateStr,
					"yyyy-MM-dd HH:mm:ss.SSS");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		return cal;
	}

	public List<WebElement> checkPopupTableValues(int section,
			List<String> expectedValues) {

		List<WebElement> sectionHeadings = getSectionHeadings(section);
		List<WebElement> sectionValues = getSectionValues(section);

		for (int i = 0; i < sectionHeadings.size(); i++) {
			String text = sectionHeadings.get(i).getText().trim();
			if (text.equals("Source Sec. Serving HSDSCH Cell CID")
					|| text.equals("Target Sec. Serving HSDSCH Cell CID")) {
				i++;
				text = sectionHeadings.get(i).getText().trim();
			}
			assertTrue("Value: " + text + " does not match expected field: "
					+ expectedValues.get(i), expectedValues.get(i).equals(text));
		}

		assertFalse("No Subscriber Identity values present in list",
				sectionValues.isEmpty());
		return sectionValues;

	}

	protected String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month - 1];
	}

	// Returns true if the string contains a decimal point and one or more
	// trailing zero's
	public String formatValue(int numHours, String s) {

		int i = 0, tempNum = 0;
		String res = "";

		if (s.matches("[+-]\\d+\\.{1}0*")) {
			i = s.indexOf(".");
			res = s.substring(0, i);
			/*
			 * }else
			 * if(s.matches("\\d+\\-\\d+\\-\\d+\\s\\d+\\:\\d+\\:\\d+\\.\\d+")){
			 * i = s.indexOf(":"); res = s.substring(i-2,i); tempNum =
			 * Integer.parseInt(res); if(tempNum==numHours){ res =
			 * "Dates Correct"; }else{ res = s; }
			 */} else {
			res = s;
		}

		return res;

	}

	protected static Calendar getclosest15minStartDate(Calendar date) {

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

	protected static Calendar getclosest15minEndDate(Calendar date) {
		Calendar convertedDate = Calendar.getInstance();

		convertedDate.setTime(date.getTime());

		int mins = date.get(Calendar.MINUTE);
		int hr = date.get(Calendar.HOUR);

		if ((mins % 15) > 0) {
			if (mins >= 45) {
				convertedDate.set(Calendar.HOUR, hr + 1);
				convertedDate.set(Calendar.MINUTE, 0);
			} else if (mins >= 30 & mins < 45) {
				convertedDate.set(Calendar.MINUTE, 45);
			} else if (mins >= 15 & mins < 30) {
				convertedDate.set(Calendar.MINUTE, 30);
			} else if (mins > 0 & mins < 15) {
				convertedDate.set(Calendar.MINUTE, 15);
			}
		} else if ((mins % 15) == 0) {
			if (mins == 0)
				convertedDate.set(Calendar.MINUTE, 15);
			else if (mins == 15)
				convertedDate.set(Calendar.MINUTE, 30);
			else if (mins == 30)
				convertedDate.set(Calendar.MINUTE, 45);
			else if (mins == 45) {
				convertedDate.set(Calendar.HOUR, hr + 1);
				convertedDate.set(Calendar.MINUTE, 0);
			}

		}

		return convertedDate;
	}

	// Returns true if the string contains a decimal point and one or more
	// trailing zero's
	public String formatDate(String s) {
		String result = "";
		if (s.length() > 20) {
			StringBuilder tempStr = new StringBuilder(s);
			String hrs = tempStr.substring(11, 13);
			tempStr = tempStr.replace(11, 13, hrs);
			result = tempStr.toString();
			return result;
		} else {
			return s;
		}

	}

	protected void closeViewDetails() {
		if(isViewDetailsMaximized()){
			driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW_MAXIMIZE))
			.click();
		}
		driver.findElement(
				By.xpath(Constants.VIEW_DETAILS_WINDOW_CLOSE)).click();
	}
	
	protected void scrollViewDetailsSectionList(boolean up, int steps) {
		
		String scrollUpXpath = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[1]";
		String scrollDownXpath = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[2]";
		
		String xPath = up == true ? scrollUpXpath : scrollDownXpath;

		for(int i = 0; i < steps; i++){
			webDriverSelenium.click(xPath);
		}
	}
	
	protected void scrollViewDetailsSectionListUntilElementVisible(boolean up, WebElement webElement) {
		
		String scrollUpXpath = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[1]";
		String scrollDownXpath = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[2]";
		
		int steps = 400;
		
		String xPath = up == true ? scrollUpXpath : scrollDownXpath;

		for(int i = 0; i < steps && !webElement.isDisplayed(); i++){
			webDriverSelenium.click(xPath);
		}
	}
	
	protected void zoom(boolean in, int steps){
		WebElement html = driver.findElement(By.tagName("html"));
		
		for(int i = 0; i < steps; i++){
			if(in)
				html.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
			else
				html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
		}
	}
}
