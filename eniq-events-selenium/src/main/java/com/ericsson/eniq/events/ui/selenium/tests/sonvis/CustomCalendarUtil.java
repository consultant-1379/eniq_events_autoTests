package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author esacsad
 * 
 * This is a Utility class to automatically fill the Custom Date in SON Visualisation UI Calender
 */
public class CustomCalendarUtil {
	
	protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

	protected static final String DEFAULT_TIMEOUT = "20000";
	private static final String LAUNCH_BUTTON = "html/body/div[4]/div/div[3]/div/div[2]/div[2]/button";
	private static final String CUSTOM_DATE_PICKER_OK_BUTTON = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div/button[1]";
	private static final String DATE_DROPDOWN = "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[2]/div/table/tbody/tr/td[1]/div";
	private static final String START_CALENDAR_BACK_BUTTON = "/html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/div/div";
	private static final String CLICK_RANKING_BUTTON = "/html/body/div[4]/div/div[3]/div/div[2]/div/div/div/table/tbody/tr/td[2]";
	private static final String CUSTOM_DATE_DROP_DOWN = "/html/body/div[4]/div/div[3]/div/div[2]/div/div/div[2]/div/div/div/div[2]";
	private static final String CLICK_CONFIGURE_PIs_AND_KPIs_BUTTON = "/html/body/div[4]/div/div[3]/div/div[2]/div/div/button";
	private static final String CLICK_DROP_DOWN_IN_CONFIGURE_PIs_AND_KPIs_BUTTON = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr/td/div/div/div[2]/div/div/div/table/tbody/tr/td[2]/div";
	private static final String CLICK_ANRX2RELATIONS_ADDED_IN_CONFIGURE_PIs_AND_KPIs_BUTTON = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr/td/div/div/div[2]/div/div/div/div/div/div";
	private static final String CLICK_UPDATE_IN_CONFIGURE_PIs_AND_KPIs_BUTTON = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr[3]/td/table/tbody/tr/td/button";
	String todayClass = "datePickerDayIsToday";

	boolean stDate = true;

	public void clickRankingButton() {
		webDriver.findElement(By.xpath(CLICK_RANKING_BUTTON)).click();
	}

	public void clickDateOption() {
		webDriver.findElement(By.xpath(DATE_DROPDOWN)).click();
	}

	/**
	 * 
	 */
	public void chooseSONVisualisationUICustomDate() {

		boolean startDate = true;
		boolean differentMonth = false;
		
		Calendar todayDate = Calendar.getInstance();
		Calendar startDateToSelectCal = Calendar.getInstance();
		Calendar endDateToSelectCal = Calendar.getInstance();

		Date today = new Date();
		todayDate.setTime(today);
		startDateToSelectCal.setTime(today);
		endDateToSelectCal.setTime(today);

		startDateToSelectCal.add(Calendar.DATE, -25);
		

		if (startDateToSelectCal.get(Calendar.MONTH) != todayDate.get(Calendar.MONTH)) {
			differentMonth = true;
		}

		selectDate(startDate, startDateToSelectCal.get(Calendar.DATE), differentMonth);

		endDateToSelectCal.add(Calendar.DATE, -1);

		if (endDateToSelectCal.get(Calendar.MONTH) != todayDate.get(Calendar.MONTH)) {
			differentMonth = true;
		}

		else {
			differentMonth = false;
		}

		selectDate(!startDate, endDateToSelectCal.get(Calendar.DATE), differentMonth);

		clickCustomDayPickerOkButton();
		
	}
	
	private void selectDate(boolean startDate, int selectedDayNumber, boolean differentMonth) {
		String selectDayNumber = String.valueOf(selectedDayNumber);
		webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, selectDayNumber, differentMonth))).click();
	}

	private String getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumberToSelect, boolean differentMonth) {

		String xPathToSelect = "";

		if (startDate && !differentMonth) {
			xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not(contains(@class, 'datePickerDayIsFiller'))]";
		}

		else if (startDate && differentMonth) {
			xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and (contains(@class, 'datePickerDayIsFiller'))]";

			if (isDayInDefaultCalendar(xPathToSelect)) {
				clickOnceBackButton();

				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not (contains(@class, 'datePickerDayIsFiller'))]";
			}
		}

		else if (!startDate && !differentMonth) {
			xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not(contains(@class, 'datePickerDayIsFiller'))]";

		}

		else if (!startDate && differentMonth) {
			xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and (contains(@class, 'datePickerDayIsFiller'))]";

			if (!isDayInDefaultCalendar(xPathToSelect)) {
				clickOnceBackButton();
				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not (contains(@class, 'datePickerDayIsFiller'))]";
			}
		}

		return xPathToSelect;
	}

	public void clickOnceBackButton() {
		webDriver.findElement(By.xpath(START_CALENDAR_BACK_BUTTON)).click();
	}

	private boolean isDayInDefaultCalendar(final String xPath) {

		Collection<WebElement> webElements = (Collection<WebElement>) webDriver.findElements(By.xpath(xPath));
		return !webElements.isEmpty();
	}

	public void clickCustomDayPickerOkButton() {
		webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_OK_BUTTON)).click();
	}

	public void clickConfigurePIsAndKPIs() {
		webDriver.findElement(By.xpath(CLICK_CONFIGURE_PIs_AND_KPIs_BUTTON)).click();

	}

	public void clickCustomDateOption() {
		webDriver.findElement(By.xpath(CUSTOM_DATE_DROP_DOWN)).click();

	}

	public void clickOnDropDonInConfigurePIsAndKPIs() {
		webDriver.findElement(By.xpath(CLICK_DROP_DOWN_IN_CONFIGURE_PIs_AND_KPIs_BUTTON)).click();

	}

	public void selectANRX2RelationsAdded() {
		webDriver.findElement(By.xpath(CLICK_ANRX2RELATIONS_ADDED_IN_CONFIGURE_PIs_AND_KPIs_BUTTON)).click();

	}

	public void ClickUpdateInConfigurePIsAndKPIs() {
		webDriver.findElement(By.xpath(CLICK_UPDATE_IN_CONFIGURE_PIs_AND_KPIs_BUTTON)).click();

	}

	public void clickLaunch() {
		webDriver.findElement(By.xpath(LAUNCH_BUTTON)).click();
	}


}
