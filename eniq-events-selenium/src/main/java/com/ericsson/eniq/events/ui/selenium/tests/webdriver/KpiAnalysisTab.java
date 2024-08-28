/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.events.tabs.Tab;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KpiConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * @author eeikbe
 * @since 10/2012
 */
@Component
public class KpiAnalysisTab extends Tab {
	private static final By LAUNCH_HANDLE = By
			.xpath(".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[2]/img");

	private static WebDriver driver;

	private static String TAB_NAME = "KPI_ANALYSIS_TAB";
	
	protected static final String DEFAULT_TIMEOUT = "30000";

	public KpiAnalysisTab() {
		super(TAB_NAME);
	}

	public void openTab() {
		webDriverSelenium = NewEricssonSelenium.getSharedInstance();
		driver = webDriverSelenium.getWrappedDriver();
		String tabName = TAB_NAME;
		String tabButtonXPath = "//li[contains(@id,'" + tabName
				+ "')]/a[@class='x-tab-right']";
		loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
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

	/**
	 * Click on a live load popup
	 * 
	 * @param string
	 * @throws InterruptedException
	 */
	public String liveloadPopupItemClick(final int value)
			throws InterruptedException {
		 String xpath =
		 "//div[@class='gwt-SuggestBoxPopup']/div/table/tbody/tr[2]/td[2]/div/div/table/tbody/tr["
		 + value + "]/td";

		Thread.sleep(3000);
		WebElement findElement = driver.findElement(By.xpath(xpath));
		String text = findElement.getText();
		findElement.click();
		return text;
	}

	/**
	 * This method clicks a live load dropdown, then clicks on a determined item
	 * in the popup.
	 * 
	 * @param dropdownXPath
	 *            - take this from the KpiConstants Class.
	 * @param cell
	 *            - The number of the row you want to select in the dropdown
	 *            (index starts at 1).
	 * @throws InterruptedException
	 */
	public String liveloadDropdownClick(final String dropdownXPath,
			final int cell) throws InterruptedException {
		// Click the liveload dropdown
		WebElement findElement = driver.findElement(By.xpath(dropdownXPath));
		findElement.click();
		return this.liveloadPopupItemClick(cell);
	}
	
	public String liveloadControllerDropdown(final String dropdownXPath,
			final String controller) throws InterruptedException {
		// Click the liveload dropdown
		WebElement findElement = driver.findElement(By.xpath(dropdownXPath));
		findElement.click();
		
		String xpath = "//div[@class='gwt-SuggestBoxPopup']/div/table/tbody/tr[2]/td[2]/div/div/table/tbody/tr/td[text()='"
				+ controller + "']";

		Thread.sleep(3000);
		WebElement element = driver.findElement(By.xpath(xpath));
		String text = element.getText();
		element.click();
		return text;
		
//		return this.liveloadPopupItemClick(string);
	}

	public String[] launchKPIWindowWithCustomDate(String timeRange,
			String dateFrom, String dateTo, String controllerOption,
			String controller, int cell, String kpi) throws InterruptedException,
			ParseException {
		openTab();
		final List<String> timePeriodList = Arrays.asList("15 minutes",
				"30 minutes", "1 hour", "2 hours", "6 hours", "12 hours",
				"Custom Time");
		if (timePeriodList.contains(timeRange)) {
			selectTimeRange(timeRange, dateFrom, dateTo);
		}

		driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_LAUNCH))
				.click();
		String[] selectedController = null;
		if (controllerOption.equalsIgnoreCase("Controller")) {
			driver.findElement(By.xpath(KpiConstants.CONTROLLER)).click();
			selectedController = new String[2];
			selectedController[0] = liveloadControllerDropdown(
					KpiConstants.CONTROLLER_DROPDOWN_LAUNCH, controller);
			selectedController[1] = liveloadDropdownClick(
					KpiConstants.CELL_DROPDOWN_LAUNCH, cell);
		} else if (controllerOption.equalsIgnoreCase("Network")) {
			driver.findElement(By.xpath(KpiConstants.NETWORK)).click();
		}

		driver.findElement(By.xpath(KpiConstants.KPI_DROPDOWN_LAUNCH)).click();
		driver.findElement(
				By.xpath(KpiConstants.KPI_OPTION_LIST + "[text()='" + kpi
						+ "']")).click();

		driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON)).click();
		return selectedController;
	}

	public String[] launchKPIWindow(String timeRange, String controllerOption,
			String controller, int cell, String kpi) throws InterruptedException,
			ParseException {
		openTab();
		final List<String> timePeriodList = Arrays.asList("15 minutes",
				"30 minutes", "1 hour", "2 hours", "6 hours", "12 hours",
				"1 day", "Custom Time");
		if (timePeriodList.contains(timeRange)) {
			driver.findElement(By.xpath(KpiConstants.DATE_TIME_DROPDOWN_LAUNCH))
					.click();
			driver.findElement(
					By.xpath("//div[@class='popupContent']//div/div[text()='"
							+ timeRange + "']")).click();
			// CommonUtils.selectDefaultCustomDateTimeRange();
		}

		driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_LAUNCH))
				.click();
		String[] selectedController = null;
		if (controllerOption.equalsIgnoreCase("Controller")) {
			driver.findElement(By.xpath(KpiConstants.CONTROLLER)).click();
			selectedController = new String[2];
			selectedController[0] = liveloadControllerDropdown(
					KpiConstants.CONTROLLER_DROPDOWN_LAUNCH, controller);
			selectedController[1] = liveloadDropdownClick(
					KpiConstants.CELL_DROPDOWN_LAUNCH, cell);
		} else if (controllerOption.equalsIgnoreCase("Network")) {
			driver.findElement(By.xpath(KpiConstants.NETWORK)).click();
		}

		driver.findElement(By.xpath(KpiConstants.KPI_DROPDOWN_LAUNCH)).click();
		driver.findElement(
				By.xpath(KpiConstants.KPI_OPTION_LIST + "[text()='" + kpi
						+ "']")).click();

		driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON)).click();
		return selectedController;
	}

	private void selectTimeRange(String timeRange, String dateFrom,
			String dateTo) throws ParseException, InterruptedException {
		driver.findElement(By.xpath(KpiConstants.DATE_TIME_DROPDOWN_LAUNCH))
				.click();
		driver.findElement(
				By.xpath("//div[@class='popupContent']//div/div[text()='"
						+ timeRange + "']")).click();
		if (timeRange.equals("Custom Time")) {
			if (dateFrom.equals("") || dateTo.equals("")) {
				CommonUtils.selectDefaultCustomDateTimeRange();
			} else {
				openCustomDateAndTime(dateFrom, dateTo);
				// CommonUtils.selectDefaultCustomDateTimeRange(dateFrom,
				// dateTo);
			}
		}
	}

	public void openCustomDateAndTime(final String startDateStr,
			final String endDateStr) throws InterruptedException,
			ParseException {

		Date startDate = CommonUtils.parseDate(startDateStr,
				CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);
		Date endDate = CommonUtils.parseDate(endDateStr,
				CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS);

		//
		// System.out.println(startDate);
		// System.out.println(endDate);

		final String startDay = String.valueOf(startDate.getDate());
		final String endDay = String.valueOf(endDate.getDate());

		String startlabel = formatYear(startDate) + " "
				+ formatMonth(startDate.getMonth());
		System.out.println(startlabel);
		String endlabel = formatYear(endDate) + " "
				+ formatMonth(endDate.getMonth());
		System.out.println(endlabel);

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

		// final int index_1 = listOfmonths.indexOf(startDateStr);
		final int index_1 = listOfmonths.indexOf(startlabel);
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
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//tr[2]//tr[2]//tr//td[text()='"
						+ startDay + "']")).click();

		Thread.sleep(2000);

		final int index_2 = listOfmonths.indexOf(endlabel);
		// final int index_2 = listOfmonths.indexOf(endDateStr);
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
				By.xpath("//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//tr[2]//tr[2]//tr//td[text()='"
						+ endDay + "']")).click();
		driver.findElement(By.xpath("//button[text()='Ok']")).click();

	}

	private String formatYear(Date startDate) {

		return startDate.toString().split("CET")[1].trim();
	}

	public String formatMonth(int month) {
		DateFormat formatter = new SimpleDateFormat("MMM");
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.MONTH, month);
		return formatter.format(calendar.getTime());
	}

	public void clickLaunch() {
		driver.findElement(LAUNCH_HANDLE).click();
	}

	@Override
	protected void clickStartMenu() {
		// To change body of implemented methods use File | Settings | File
		// Templates.

	}
}
