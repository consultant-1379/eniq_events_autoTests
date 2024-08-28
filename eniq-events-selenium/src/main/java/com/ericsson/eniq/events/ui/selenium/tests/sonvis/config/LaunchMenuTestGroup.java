package com.ericsson.eniq.events.ui.selenium.tests.sonvis.config;


import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.LaunchMenu.Tab;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

// SONVIS 54
// CONFIG VERSION

public class LaunchMenuTestGroup extends SonvisWebDriverBaseUnitTest {
    private final LaunchMenu launchMenu = new LaunchMenu();
    String[] primaryPis = {"ANR/X2 Relations Added", "ANR/X2 Relations Removed", "PCI Changes", "Total PCI Conflicts"};

    @Test
    public void testDefaults() throws PopUpException, InterruptedException {
    	init();
        sleep(2000);
        assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
        assertEquals("No Date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
        assertFalse("Configuration button should be disabled when Date is not selected",
                launchMenu.isConfigButtonEnabled());
        assertFalse("Launch button should be disabled if configurations are not selected",
                launchMenu.isLaunchButtonEnabled());
    }
    
    @Test
    public void testRankingDefaults() throws PopUpException, InterruptedException {
    	/* 
    	init();
        sleep(2000); */        
    	
    	assertFalse("Configuration button should be disabled after switching tab", launchMenu.isConfigButtonEnabled());
        assertEquals("No Date should be selected after switching tab", "--Select Date--",launchMenu.getSelectedDateOption());        
        assertFalse("Launch button should be disabled if configurations are not selected",
                launchMenu.isLaunchButtonEnabled());
    }

    @Test
    public void testMobilitySuccessRate() throws PopUpException, InterruptedException {
    	
        init();
        sleep(2000);
        
        // test MSR checkbox is present in Config menu, when "Today" DAY_OF_MONTH is selected in "Launch" menu
        testMobilitySuccessRate_Today_Date();
        
        // test MSR checkbox is present in Config menu, when "Custom" DAY_OF_MONTH is selected in "Launch" menu
        testMobilitySuccessRate_Custom_Dates_Valid();      
        testMobilitySuccessRate_Custom_Dates_Invalid();  
    }
    
    @Test
    public void testMobilitySuccessRate_Today_Date() throws PopUpException, InterruptedException {
    	
    	// tests MSR checkbox is present in "Config" Menu, 
    	// when "Today" DAY_OF_MONTH is selected in "Launch" menu
    	
    	/*launchMenu.selectTab(Tab.RANKING);   
    	testRankingDefaults();*/
    	
    	initLaunchMenu();
        launchMenu.selectDateOption("Today", null, null);
        assertEquals("Today should be selected in Date dropdown", "Today", launchMenu.getSelectedDateOption());
        testRankingMSRAfterDatesSelection();        
    }
    
    @Test
    public void testMobilitySuccessRate_Custom_Dates_Valid() throws PopUpException, InterruptedException {
    	
        // initLaunchMenu();       
        launchMenu.selectDateOption("Custom", null, null);
        // check "Calendars" Menu has poped up after selecting "Custom" DAY_OF_MONTH type
    	assertTrue("'StartDate' and 'EndDate' Calendars Menu should pop up", launchMenu.hasCalendarsMenuPopedUp());
    	
    	testRankingCustomValidDates();
    	// launchMenu.clickLaunch();      
    } 
    
    @Test
    public void testMobilitySuccessRate_Custom_Dates_Invalid() throws PopUpException, InterruptedException {
    	
    	// initLaunchMenu();       
        launchMenu.selectDateOption("Custom", null, null);
        // check "Calendars" Menu has poped up after selecting "Custom" DAY_OF_MONTH type
    	assertTrue("'StartDate' and 'EndDate' Calendars Menu should pop up", launchMenu.hasCalendarsMenuPopedUp());
    	testRankingCustomInvalidDates();
    	//testRankingMSRAfterDatesSelection();             
    }  
    
    @Test
    public void testRankingCustomValidDates() {
    	
    	//launchMenu.testCustomDatePickerValidStartEndYesterday();
    	//testRankingMSRAfterDatesSelection();
    	launchMenu.testCustomDatePickerValidStartEndDatesRange();
    	testRankingMSRAfterDatesSelection();
    }
    
    @Test
    public void testRankingCustomInvalidDates() {
    	
    	testCustomDatePickerInvalidDates();
    	//testRankingMSRAfterDatesSelection();
    }
    
    public void testCustomDatePickerInvalidDates() {
    	
    	// test invalid "Start / End Dates" test cases   	
    	boolean differentMonth = false;
			
		// get today's date
		Date today = new Date();		
		Calendar startDateToSelectCal = Calendar.getInstance(); 
		Calendar endDateToSelectCal = Calendar.getInstance(); 
		Calendar todayDate = Calendar.getInstance(); 
		startDateToSelectCal.setTime(today);
		endDateToSelectCal.setTime(today);

		System.out.println(startDateToSelectCal.getClass());
		System.out.println(startDateToSelectCal.getTime());

		System.out.println("ANYO: "+startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("MES: "+startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("DIA: "+startDateToSelectCal.get(Calendar.DATE));
		System.out.println("HORA: "+startDateToSelectCal.get(Calendar.HOUR));
		
		if (startDateToSelectCal.get(Calendar.MONTH) != endDateToSelectCal.get(Calendar.MONTH))
			{ differentMonth = true; }
		
		System.out.println("Test invalid #1 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #1 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));	
		System.out.println("Test invalid #1 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #1 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #1 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #1 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), startDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
		
		// invalid #2: start date = future date
		startDateToSelectCal.add(Calendar.DAY_OF_MONTH, 7);
		
		if (startDateToSelectCal.get(Calendar.MONTH) != todayDate.get(Calendar.MONTH))
			{ differentMonth = true; }
		
		System.out.println("Test invalid #2 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #2 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));	
		System.out.println("Test invalid #2 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #2 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #2 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #2 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), startDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
		
		// invalid #3: start date > 29 days ago
		startDateToSelectCal.add(Calendar.DAY_OF_MONTH, -44);
		if (startDateToSelectCal.get(Calendar.MONTH) != endDateToSelectCal.get(Calendar.MONTH))
		{ differentMonth = true; }
		System.out.println("Test invalid #3 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #3 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));	
		System.out.println("Test invalid #3 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #3 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #3 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #3 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), startDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
		
		// invalid #4: startDate = valid | endDate = today
		startDateToSelectCal.setTime(today);
		startDateToSelectCal.add(Calendar.DAY_OF_MONTH, -13);
		
		if (startDateToSelectCal.get(Calendar.MONTH) != endDateToSelectCal.get(Calendar.MONTH))
		{ differentMonth = true; }
		System.out.println("Test invalid #4 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #4 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));	
		System.out.println("Test invalid #4 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #4 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #4 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #4 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), endDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
		
		// invalid #5: startDate = valid | endDate < startDate
		endDateToSelectCal.add(Calendar.DAY_OF_MONTH, -21);
		if (startDateToSelectCal.get(Calendar.MONTH) != endDateToSelectCal.get(Calendar.MONTH))
		{ differentMonth = true; }
		System.out.println("Test invalid #5 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #5 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #5 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #5 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));		
		System.out.println("Test invalid #5 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));		
		System.out.println("Test invalid #5 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), endDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
		
		// invalid #6: startDate = valid | endDate = future date
		endDateToSelectCal.setTime(today);
		endDateToSelectCal.add(Calendar.DAY_OF_MONTH, 2);
		if (startDateToSelectCal.get(Calendar.MONTH) != endDateToSelectCal.get(Calendar.MONTH))
		{ differentMonth = true; }
		System.out.println("Test invalid #6 - stDate dia: " + startDateToSelectCal.get(Calendar.DATE));
		System.out.println("Test invalid #6 - endDate dia: " + endDateToSelectCal.get(Calendar.DATE));	
		System.out.println("Test invalid #6 - stDate month: " + startDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #6 - endDate month: " + endDateToSelectCal.get(Calendar.MONTH));
		System.out.println("Test invalid #6 - stDate year: " + startDateToSelectCal.get(Calendar.YEAR));
		System.out.println("Test invalid #6 - endDate year: " + endDateToSelectCal.get(Calendar.YEAR));
		launchMenu.selectInvalidDates(startDateToSelectCal.get(Calendar.DATE), endDateToSelectCal.get(Calendar.DATE), differentMonth);
		assertTrue("Error message should be displayed after invalid Date selection", launchMenu.hasCustomDatePickerErrorLabelPopedUp());
    }
    
    @Test
    public void testRankingMSRAfterDatesSelection() {
        boolean configSelectionRight = false;
        
        assertTrue("'Configuration' button should be enabled after Date selection", launchMenu.isConfigButtonEnabled());
        launchMenu.launchConfigurationWindow();
        
        for (int i=0; i<4; i++)
        {
        	launchMenu.selectConfigPrimaryPi(primaryPis[i]);
        	assertTrue("'Update' button in 'Config' menu  should be enabled after Pi selection", launchMenu.isUpdateButtonEnabled());           
        	launchMenu.selectMobilitySuccessRateOption();
        	assertTrue("Mobility Success Rate check box should be selected", launchMenu.isMobilitySuccessRateSelected());
        	configSelectionRight = true;
        }
        
        if (!configSelectionRight)
        {
            assertFalse("'Update' button should be disabled if configurations are not selected", launchMenu.isUpdateButtonEnabled());
            assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());           
        }
        
    	launchMenu.updateLaunchMenu();    	
    	assertTrue("'Launch' button should be enabled after 'Config' menu updating", launchMenu.isLaunchButtonEnabled());      
    }
    
    @Test
    public void initLaunchMenu() throws PopUpException, InterruptedException {
    	
    	// displays "Launch" menu 
        init();
        sleep(2000);

        launchMenu.selectTab(Tab.RANKING);
        testRankingDefaults();    
    }
}