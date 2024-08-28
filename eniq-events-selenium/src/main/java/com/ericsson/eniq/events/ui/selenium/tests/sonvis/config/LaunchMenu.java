package com.ericsson.eniq.events.ui.selenium.tests.sonvis.config;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

// CONFIG VERSION

public class LaunchMenu {
    protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    protected static final String DEFAULT_TIMEOUT = "20000";

    private static final String CONFIG_BUTTON = "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/button";

    private static final String LAUNCH_BUTTON = "html/body/div[4]/div/div[3]/div/div[2]/div[2]/button";
    
    private static final String UPDATE_BUTTON = "html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[3]/td/table/tbody/tr/td[1]/button";

    private static final String CUSTOM_DATE_PICKER_OK_BUTTON = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div/button[1]";
    					
    private static final String LAUNCH_FOOTER = "html/body/div[4]/div/div[4]/span";

    private static final String DATE_DROPDOWN = "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[2]/div/table/tbody/tr/td[1]/div";

    private static final String CONFIG_PRIMARY_PI = ".//*[@id='primaryPiSelector']/div/table/tbody/tr/td[1]/div";
    
    private static final String MOBILITY_SUCCESS_RATE = "html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[1]/td/div/div[2]/div[2]/div/table/tbody/tr[4]/td/span/input";
    
    private static final String START_AND_END_DATES_CALENDAR_POPUP = "html/body/div[5]";
    
    private static final String START_DATE_MONTH_YEAR_CALENDAR_LABEL = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[1]/td/table/tbody/tr/td[2]";
	
    private static final String CUSTOM_DATE_PICKER_ERROR_LABEL 	= "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]";

    private static final String START_CALENDAR_BACK_BUTTON  = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[1]/td/table/tbody/tr/td[1]/div/div";


    private static final String CUSTOM_DATE_PICKER_START_DATE_CALENDAR_TABLE = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div";
    				
    private static final String CUSTOM_DATE_PICKER_END_DATE_CALENDAR_TABLE = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div";
    
    String todayClass = "datePickerDayIsToday";
    
    boolean stDate = true;
  
    enum Tab {
        RANKING("Ranking"), NETWORK_ACTIVITY("Network Activity");
        private final String label;

        Tab(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }

    private String getTabPath(final Tab tab) {
        return "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[1]/table/tbody/tr/td[text()='" + tab.getLabel()
                + "']";
    }

    private String getDateDropdownOptionPath(final String type) {
        return "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[2]/div[1]/div/div/div[text()='" + type + "']";
    }
    
    private String getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumber) {
    	
    	System.out.println("numero dia en 'getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumber)': " + dayNumber);
    	System.out.println("startDate? en 'getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumber)': " + startDate);
    	
    	if(startDate)
        { return   "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[contains(text(),'" + dayNumber + "') and not(contains(@class, 'datePickerDayIsFiller'))]"; }
    	else
    	{ return 	"html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[contains(text(),'" + dayNumber + "') and not(contains(@class, 'datePickerDayIsFiller'))]"; }
    }
    

    
    private String getConfigPrimaryPiDropdownOptionPath(final String primaryPi) {
    	 return ".//*[@id='primaryPiSelector']/div[1]/div/div/div[text()='" + primaryPi + "']";
    }
        
    public void selectTab(final Tab tab) {
        webDriver.findElement(By.xpath(getTabPath(tab))).click();
    }

    public boolean selectDateOption(final String type, final String startDate, final String endDate) {
    	
    	boolean todayDateSelected = true;
    	
        webDriver.findElement(By.xpath(DATE_DROPDOWN)).click();
        webDriver.findElement(By.xpath(getDateDropdownOptionPath(type))).click();
        if (type.equalsIgnoreCase("Custom")) {
        return !todayDateSelected;
        }
        
        return todayDateSelected;
    }
      
    public void selectConfigPrimaryPi(final String primaryPi) {
        webDriver.findElement(By.xpath(CONFIG_PRIMARY_PI)).click();
        webDriver.findElement(By.xpath(getConfigPrimaryPiDropdownOptionPath(primaryPi))).click();
        /*if (type.equalsIgnoreCase("Custom")) {

        }*/
    }
    
    private int getTodayNumber() {
    		
    	return Integer.parseInt(getCustomDatePickerMenuStartDate());
    }
    
    private void selectDate(boolean startDate, int dayFromToday, int monthFromCurrent) {
    	        
    	System.out.println("Selected dia from today: " + dayFromToday);
    	System.out.println("Selected month from current: " + monthFromCurrent);
    	
		int dayNumber = getTodayNumber() + dayFromToday;
		System.out.println("Selected dia number: " + dayNumber);
		String selectedDayNumber = String.valueOf(dayNumber);
		System.out.println("Selected dia number Strng: " + dayNumber);
				
		webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, selectedDayNumber))).click();
    }
    
    private void selectDate(boolean startDate, int dayToSelect) {
        
    	System.out.println("Dia to select in 'SelectDate' mthd: " + dayToSelect);

		String selectedDayNumber = String.valueOf(dayToSelect);
				
		webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, selectedDayNumber))).click();
    }
    
    public void selectInvalidDates(int startDateNumber, int endDateNumber, boolean differentMonth) {
    	    	
		System.out.println("****** INSIDE  selectInvalidDates(int startDateNumber, int endDateNumber)  mthd ********");
		System.out.println("Start Date number?: " + startDateNumber);		
    	System.out.println("Selected end dia number: " + endDateNumber);
    	System.out.println("Different month: " + differentMonth);
		
	boolean startDate = true; 	
			
	String dayNumberToSelect = String.valueOf(startDateNumber);
	
	System.out.println("startDate?: " + startDate);		
	// start date
	webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, dayNumberToSelect))).click();
		
	// end date
	dayNumberToSelect = String.valueOf(endDateNumber);
	webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(!startDate, dayNumberToSelect, differentMonth))).click();
		
	// hit "OK" button
	webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_OK_BUTTON)).click(); 	
	System.out.println("**** After clicking 'OK' BUTTON ****");	
	
	// error label should be displayed
}
    
	private void selectDate(boolean startDate, int selectedDayNumber, boolean differentMonth) {
		
		// select for Valid Range
		System.out.println("****** INSIDE  selectDate(boolean startDate, int selectedDayNumber, boolean differentMonth) mthd ********");
		System.out.println("Start Date?: " + startDate);		
    	System.out.println("Selected dia number: " + selectedDayNumber);
    	System.out.println("Different month: " + differentMonth);
		
		String selectDayNumber = String.valueOf(selectedDayNumber);
		
		webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, selectDayNumber, differentMonth))).click();
    }
        
    public void testCustomDatePickerValidStartEndYesterday() {
    	
    	// select "startDate" and "endDate" == yesterday

    	// select "startDate" = yesterday;
    	selectDate(stDate, -1, 0);
    	// select "endDate" = yesterday;
    	selectDate(!stDate, -1, 0);
    	webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_OK_BUTTON)).click(); 	
    }
	
	public void testCustomDatePickerValidStartEndDatesRange() {
		
		System.out.println("**** INSIDE METHOD: testCustomDatePickerValidStartEndDatesRange() *********************");
		
		// boolean validRange = false;
		boolean startDate = true;
		boolean differentMonth = false;
		Calendar todayDate = Calendar.getInstance(); 
		Calendar startDateToSelectCal = Calendar.getInstance(); 
		Calendar endDateToSelectCal = Calendar.getInstance(); 
		
		// get today's date
		Date today = new Date();		
		todayDate.setTime(today);
		startDateToSelectCal.setTime(today);
		endDateToSelectCal.setTime(today);
		
		// boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
		System.out.println("Test valid #1 - today date : " + todayDate.get(Calendar.DATE) + " / " + todayDate.get(Calendar.MONTH) + " / " + todayDate.get(Calendar.YEAR));
		System.out.println("Test valid #1 - stDate = today : " + startDateToSelectCal.get(Calendar.DATE) + " / " + startDateToSelectCal.get(Calendar.MONTH) + " / " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test valid #1 - endDate = today: " + endDateToSelectCal.get(Calendar.DATE) + " / " + endDateToSelectCal.get(Calendar.MONTH) + " / " + endDateToSelectCal.get(Calendar.YEAR));
		
		int todayDia = todayDate.get(Calendar.DATE);
		int todayMonth = todayDate.get(Calendar.MONTH) ; 
		int todayYear = todayDate.get(Calendar.YEAR);
		
		// test first valid range, with minimum and maximum permitted dates
		// valid minimum start date #1: today - 29 days
		startDateToSelectCal.add(Calendar.DATE, -29);
		System.out.println("Test valid #1 - stDate: today - 29 dias: " + startDateToSelectCal.get(Calendar.DATE) + " / " + startDateToSelectCal.get(Calendar.MONTH) + " / " + startDateToSelectCal.get(Calendar.YEAR));
		
		
		if (startDateToSelectCal.get(Calendar.MONTH) != todayDate.get(Calendar.MONTH))
		{
			// dia is going to be filler (ie, != negrita)
			differentMonth = true;			
		}
				
		selectDate(startDate, startDateToSelectCal.get(Calendar.DATE), differentMonth);
				
		// valid maximum end date range: "startDate" + 28
		endDateToSelectCal.add(Calendar.DATE, -1);
		System.out.println("Test valid #1 - endDate: today - 1 dias: " + endDateToSelectCal.get(Calendar.DATE) + " / " + endDateToSelectCal.get(Calendar.MONTH) + " / " + endDateToSelectCal.get(Calendar.YEAR));

		if (endDateToSelectCal.get(Calendar.MONTH) != todayDate.get(Calendar.MONTH))
		{
			// dia is going to be filler (ie, != negrita)
			differentMonth = true;			
		}
		
		else {differentMonth = false; }
	
		selectDate(!startDate, endDateToSelectCal.get(Calendar.DATE), differentMonth);
		
		clickCustomDayPickerOkButton();		
	} 
	
	public boolean checkValidRange(Calendar startDate, Calendar endDate) {
		
		// check date
	
		boolean validDate = true;
		
		// get today's date
		Date today = new Date();
		// construct java selected date
		Calendar minimumStartDate = Calendar.getInstance(); 
		Calendar maximumStartDate = Calendar.getInstance(); 
		//Calendar dateToSelectCal = Calendar.getInstance(); 
		minimumStartDate.setTime(today);
		minimumStartDate.add(Calendar.DATE, -29);
		maximumStartDate.setTime(today);
		maximumStartDate.add(Calendar.DATE, -1);
		
		long milsecs1= startDate.getTimeInMillis();
		long milsecs2 = endDate.getTimeInMillis();
		
		long diff = milsecs2 - milsecs1;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		
		if ( (startDate.before(minimumStartDate)) || (startDate.after(maximumStartDate)) )
		{
			validDate = false;
		}
		
		if ( (endDate.before(minimumStartDate)) || (endDate.after(maximumStartDate)) || (diffDays > 29) )
		{
			validDate = false;
		}
		
		return validDate;
    }
	
	public void testRange(Calendar startDateToSelectCal, Calendar endDateToSelectCal) {
		
		boolean startDate = true;
		boolean differentMonth = false;
		
		// get Picker "Dia / Month" Label
		// example: 2013 Jun
		String currentMonthYear = getCustomDatePickerMenuMonthYearLabel();
			
		// get white space index
		int spaceIndex = currentMonthYear.indexOf(" ");
					
		// get month from "startDate" Calendar Picker
		String currentMonth = currentMonthYear.substring(spaceIndex, currentMonthYear.length());
		
		// get year
		// int selectedYear = Integer.parseInt(selectedMonthYear.substring(0, spaceIndex));
		
		// get month from "startDate" to select
		String strDateToSelectMonth = getMonthString(startDateToSelectCal.MONTH);
		
		// compare both months
		if (!currentMonth.equals(strDateToSelectMonth))
		{
			differentMonth = true;
		}
		
		// select "startDAte"
		selectDate(startDate, startDateToSelectCal.DATE, differentMonth);
		
		// "endDate" month
		String endDateToSelectMonth = getMonthString(endDateToSelectCal.MONTH);
		
		// compare both months
		if (!currentMonth.equals(endDateToSelectMonth))
		{
			differentMonth = true;
		}
		
		// select "startDAte"
		selectDate(!startDate, endDateToSelectCal.DATE, differentMonth);
		
    }
	
	  public void selectValidDates(boolean startDate, int startDateNumber, int endDateNumber, boolean differentMonth) {
			
			// boolean startDate = true; 	
			// int todayNumber = getTodayNumber();
					
			String dayNumberToSelect = "";
			
			// webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, dayNumberToSelect))).click();
			
			if (startDate) 
			{
				dayNumberToSelect = String.valueOf(endDateNumber);
				webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(startDate, dayNumberToSelect, differentMonth))).click();
			}
			
			else 
			{
				dayNumberToSelect = String.valueOf(endDateNumber);
				webDriver.findElement(By.xpath(getSelectedDayNumberForCustomDatePicker(!startDate, dayNumberToSelect, differentMonth))).click();
			}
			
					
			// hit "OK" button
			webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_OK_BUTTON)).click(); 	
			// error label should be displayed
		}
	  
	    private String getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumberToSelect, boolean differentMonth) {
			
	    	System.out.println("****** INSIDE  getSelectedDayNumberForCustomDatePicker(boolean startDate, final String dayNumberToSelect, boolean differentMonth) mthd ********");
			
	    	System.out.println("startDate? en 'getSelectedDayNumberForCustomDatePicker': " + startDate);
			System.out.println("numero dia to select en 'getSelectedDayNumberForCustomDatePicker': " + dayNumberToSelect);
			System.out.println("differentMonth en 'getSelectedDayNumberForCustomDatePicker': " + differentMonth);
			
			String xPathToSelect = "";

			if(startDate && !differentMonth)
	        { 
				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not(contains(@class, 'datePickerDayIsFiller'))]"; 
								
				System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);
			}
					
			else if (startDate && differentMonth)
	    	{ 		
				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and (contains(@class, 'datePickerDayIsFiller'))]";
				System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);
				// if selectedDiaNumber is not in current (= displayed calendar)
				// hit once backk button
				if (!isDayInDefaultCalendar(xPathToSelect))
				{			
					clickOnceBackButton();
					
					// now dayNumber will be in current (= previous month)
					// so it will be in bold
					xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not (contains(@class, 'datePickerDayIsFiller'))]"; 					
					System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);
				}																	
			}
			
			else if (!startDate && !differentMonth)
	    	{ 
				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not(contains(@class, 'datePickerDayIsFiller'))]"; 								
				System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);				
	    	}
			
			else if (!startDate && differentMonth)
	    	{ 
				xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and (contains(@class, 'datePickerDayIsFiller'))]";	
				System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);
			
				if (!isDayInDefaultCalendar(xPathToSelect))
				{			
					clickOnceBackButton();
					
					// now dayNumber will be in current (= previous month)
					// so it will be in bold
					xPathToSelect = "html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/div/table/tbody/tr[2]//td[(text()='" + dayNumberToSelect + "') and not (contains(@class, 'datePickerDayIsFiller'))]"; 												
					System.out.println("xPath en 'getSelectedDayNumberForCustomDatePicker': " + xPathToSelect);
				}	
	    	}
			
			return xPathToSelect;	    
		}
	    
	public void clickOnceBackButton() {
		System.out.println("**** inside 'clickOnceBackButton()' mthd ****");
		
	        webDriver.findElement(By.xpath(START_CALENDAR_BACK_BUTTON)).click();
	    }
		
    public void selectMobilitySuccessRateOption() {
        webDriver.findElement(By.xpath(MOBILITY_SUCCESS_RATE)).click();
    }
    
    public void updateLaunchMenu() {
        webDriver.findElement(By.xpath(UPDATE_BUTTON)).click();
    }

    public void launchConfigurationWindow() {
        webDriver.findElement(By.xpath(CONFIG_BUTTON)).click();
    }

    public boolean isTabSelected(final Tab tab) {
        return !webDriver.findElement(By.xpath(getTabPath(tab))).getAttribute("class").trim().isEmpty();
    }
    
    private static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public boolean isConfigButtonEnabled() {
        return isEnabled(CONFIG_BUTTON);
    }

    public boolean isLaunchButtonEnabled() {
        return isEnabled(LAUNCH_BUTTON);
    }
    
    public boolean isUpdateButtonEnabled() {
        return isEnabled(UPDATE_BUTTON);
    }
    
    public boolean isMobilitySuccessRateSelected() {
        return isSelected(MOBILITY_SUCCESS_RATE);
    }

    private boolean isEnabled(final String xPath) {
        return webDriver.findElement(By.xpath(xPath)).isEnabled();
    }
    
    private boolean isSelected(final String xPath) {
        return webDriver.findElement(By.xpath(xPath)).isSelected();
    }
    
    public boolean hasCalendarsMenuPopedUp() {
    	return hasPopedUp(START_AND_END_DATES_CALENDAR_POPUP);
    } 
    
    public boolean hasCustomDatePickerErrorLabelPopedUp() {
    	boolean result = hasPopedUp(CUSTOM_DATE_PICKER_ERROR_LABEL);
		return result;
    } 
        
    private boolean hasPopedUp(final String xPath) {
        
    	System.out.println("*** INSIDE 'hasPopedUp(final String xPath)' mthd ****");    	
    	System.out.println("*** xPath: " + xPath);
    	
    	Collection <WebElement> webElements = (Collection <WebElement>) webDriver.findElements(By.xpath(xPath));
    	System.out.println("*** webElements size ****: " + webElements.size());
		return !webElements.isEmpty();
    }
    
    private boolean isDayInDefaultCalendar(final String xPath) {
    	System.out.println("*** INSIDE 'isDayInDefaultCalendar()' mthd ****");
    	
    	Collection <WebElement> webElements = (Collection <WebElement>) webDriver.findElements(By.xpath(xPath));
    	System.out.println("*** webElements size ****: " + webElements.size());
		System.out.println("isDia en Calendar?: " + !webElements.isEmpty());
		return !webElements.isEmpty();
		// return  webDriver.findElement(By.xpath(xPath)).isDisplayed();
    }
        
    private boolean isLeapYear(final int year) {
    	boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    	return isLeapYear; 
    }
  
    public void clickCustomDayPickerOkButton() {	
		System.out.println(" **** INSIDE clickCustomDayPickerOkButton() method ****");
        webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_OK_BUTTON)).click();
    }
    
    public void clickLaunch() {  	
    	System.out.println(" **** INSIDE clickLaunch() method ****");
        webDriver.findElement(By.xpath(LAUNCH_BUTTON)).click();
    }

    public String getLaunchFooterText() {
        return webDriver.findElement(By.xpath(LAUNCH_FOOTER)).getText();
    }

    public String getSelectedDateOption() {
        return webDriver.findElement(By.xpath(DATE_DROPDOWN)).getText();
    }
    
    public String getCustomDatePickerMenuMonthYearLabel() {
        return webDriver.findElement(By.xpath(START_DATE_MONTH_YEAR_CALENDAR_LABEL)).getText();
    }
    
    public String getCustomDatePickerMenuStartDate() {
    	String todayNumber = CUSTOM_DATE_PICKER_START_DATE_CALENDAR_TABLE + "//*[contains(@class,'" + todayClass + "')]";
    	System.out.println("todayNumber in PickerStartDate: " + todayNumber);
        return webDriver.findElement(By.xpath(todayNumber)).getText();
    }
    
    // "Custom Date Picker" Menu "End Date" table
    public String getCustomDatePickerMenuEndDate() {
        return webDriver.findElement(By.xpath(CUSTOM_DATE_PICKER_END_DATE_CALENDAR_TABLE)).getText();
    }
    
	// get month String
	private String getMonthString(int month) {

	int monthToCheck = month + 1;
    String monthString;
	
    switch (monthToCheck) {
        case 1:  monthString = "Jan";
                 break;
        case 2:  monthString = "Feb";
                 break;
        case 3:  monthString = "Mar";
                 break;
        case 4:  monthString = "Apr";
                 break;
        case 5:  monthString = "May";
                 break;
        case 6:  monthString = "Jun";
                 break;
        case 7:  monthString = "Jul";
                 break;
        case 8:  monthString = "Aug";
                 break;
        case 9:  monthString = "Sep";
                 break;
        case 10: monthString = "Oct";
                 break;
        case 11: monthString = "Nov";
                 break;
        case 12: monthString = "Dec";
                 break;
        default: monthString = "Invalid month";
                 break;
    }
	return monthString;
}
}
