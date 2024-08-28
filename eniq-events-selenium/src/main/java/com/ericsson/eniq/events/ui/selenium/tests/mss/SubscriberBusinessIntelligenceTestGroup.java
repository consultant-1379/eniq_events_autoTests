package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.EventChartData;
import com.ericsson.eniq.events.ui.selenium.core.charts.SubscriberBusinessIntelligenceChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author evinpra
 * @since 2011
 */ 

public class SubscriberBusinessIntelligenceTestGroup extends EniqEventsUIBaseSeleniumTest {

	   @Autowired
	    protected SubscriberTab subscriberTab;	    
	   
	    @Autowired
	    private SubscriberBusinessIntelligenceChart subChart;
	    	       
	    @Autowired
	    @Qualifier("subscriberOverviewForMSS")
	    private CommonWindow subOverviewWindow;
	    
	    @Autowired
	    @Qualifier("subscriberOverview")
	    private SubscriberOverviewWindow subscriberOverview;
	    
	    final List<String> defaultHeadersOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
	            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, 
	            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
	            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION,
	            GuiStringConstants.MSC, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, 
	            GuiStringConstants.MCC, GuiStringConstants.MNC));
	    	    	    
	    final List<String> defaultHeadersOnIMSIEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE,
	            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO));

	    
	    final List<String> expectedHeadersOnIMSIGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.TOTAL,
	            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.IMPACTED_SUBSCRIBERS, 
	            GuiStringConstants.EVENT_TYPE));

	    final List<String> defaultHeadersOnSelectedTAC = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC, GuiStringConstants.EVENT_TYPE,
	            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, 
	            GuiStringConstants.IMPACTED_SUBSCRIBERS));

	    final List<String> defaultHeadersOnSelectedAccessArea = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
	            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
	            GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
	            GuiStringConstants.IMPACTED_SUBSCRIBERS));

	    final List<String> defaultHeadersOnSelectedMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC, GuiStringConstants.EVENT_TYPE,
	            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, 
	            GuiStringConstants.IMPACTED_SUBSCRIBERS));

	    final List<String> defaultHeadersOnSelectedController = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
	            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
	            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS,
	            GuiStringConstants.CONTROLLER));
	    
	    final List<String> optionalHeadersToBeChecked = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSISDN,
	            GuiStringConstants.IMSI, GuiStringConstants.INTERNAL_LOCATION_CODE, GuiStringConstants.BEARER_SERVICE_CODE,
	            GuiStringConstants.TELE_SERVICE_CODE, GuiStringConstants.RAT, GuiStringConstants.CALL_ID_NUMBER, 
	            GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER, GuiStringConstants.CALLING_PARTY_NUMBER, GuiStringConstants.CALLED_PARTY_NUMBER, 
	            GuiStringConstants.CALLING_SUBS_IMSI, GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI, 
	            GuiStringConstants.CALLED_SUBS_IMEI, GuiStringConstants.MS_ROAMING_NUMBER, GuiStringConstants.DISCONNECTING_PARTY, 
	            GuiStringConstants.CALL_DURATION, GuiStringConstants.SEIZURE_TIME, GuiStringConstants.ORIGINAL_CALLED_NUMBER, 
	            GuiStringConstants.REDIRECT_NUMBER, GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI, 
	            GuiStringConstants.REDIRECT_SPN, GuiStringConstants.CALL_POSITION, GuiStringConstants.EOS_INFO, GuiStringConstants.RECORD_SEQUENCE_NUMBER, 
	            GuiStringConstants.NETWORK_CALL_REFERENCE, GuiStringConstants.RAC, GuiStringConstants.LAC));

	    final List<String> allHeaders = new ArrayList<String> (Arrays.asList(  GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, 
	            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
	            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, 
	            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC, GuiStringConstants.MNC, 
	            GuiStringConstants.MSISDN, GuiStringConstants.IMSI, GuiStringConstants.INTERNAL_LOCATION_CODE, GuiStringConstants.BEARER_SERVICE_CODE, 
	            GuiStringConstants.TELE_SERVICE_CODE, GuiStringConstants.RAT, GuiStringConstants.CALL_ID_NUMBER, GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER,
	            GuiStringConstants.CALLING_PARTY_NUMBER, GuiStringConstants.CALLED_PARTY_NUMBER, GuiStringConstants.CALLING_SUBS_IMSI, 
	            GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI, GuiStringConstants.CALLED_SUBS_IMEI, 
	            GuiStringConstants.MS_ROAMING_NUMBER, GuiStringConstants.DISCONNECTING_PARTY, GuiStringConstants.CALL_DURATION, 
	            GuiStringConstants.SEIZURE_TIME, GuiStringConstants.ORIGINAL_CALLED_NUMBER, GuiStringConstants.REDIRECT_NUMBER, 
	            GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI, GuiStringConstants.REDIRECT_SPN, GuiStringConstants.CALL_POSITION, 
	            GuiStringConstants.EOS_INFO, GuiStringConstants.RECORD_SEQUENCE_NUMBER, GuiStringConstants.NETWORK_CALL_REFERENCE, GuiStringConstants.RAC, 
	            GuiStringConstants.LAC));
	    
	    final List<String> defaultHeadersOnMSISDN = new ArrayList<String> (Arrays.asList(GuiStringConstants.EVENT_TYPE, 
	            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, 
	            GuiStringConstants.SUCCESS_RATIO));
	    
	    final List<String> defaultHeadersOnMSISDNClickedFailure = new ArrayList<String> (Arrays.asList(GuiStringConstants.EVENT_TIME, 
	            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, 
	            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT, GuiStringConstants.INTERNAL_CAUSE_CODE, 
	            GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, 
	            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC, 
	            GuiStringConstants.MNC));
	    
	    final List<String> eventType = new ArrayList<String> (Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC, 
	            GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING, 
	            GuiStringConstants.CALLFORWARDING, GuiStringConstants.MSORIGINATING, GuiStringConstants.LOCATIONSERVICES));
	    
	    final List<String> defaultContentOnSubscriberDetails = new ArrayList<String>(Arrays.asList("Mapped MSISDN", "HomeCountry", 
	    		"MobileNetworkOperator", "RoamingStatus", "LastCell", "LastMSC", "FirstEventDate", "LastEventDate"));
	    
	    @BeforeClass
	    public static void openLog() {
	        logger.log(Level.INFO, "Start of SubscriberBusinessIntelligence test section");
	    }

	    @AfterClass
	    public static void closeLog() {
	        logger.log(Level.INFO, "End of SubscriberBusinessIntelligence test section");
	    }

	    @Override
	    @Before
	    public void setUp() {
	        super.setUp();
	        pause(2000);
	        selenium.waitForElementToBePresent("//table[@id='selenium_tag_MetaDataChangeComponent']","50000");
	        if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
	            selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
	            selenium.waitForElementToBePresent("//a[@id='selenium_tag_Core - Voice']", "50000");
	            if (selenium.isElementPresent("//a[@id='selenium_tag_Core - Voice']")) {
	                selenium.click("//a[@id='selenium_tag_Core - Voice']");
	            }else{
	                logger.log(Level.SEVERE, "Element //a[@id='selenium_tag_Core - Voice'] is not present");
	            }
	        }else{
	            logger.log(Level.SEVERE, "Element //table[@id='selenium_tag_MetaDataChangeComponent'] is not present");
	        }
	        pause(2000);
	    }

	    /**
	     * Test Case: 4.9.1 Display Subscriber BI details using IMSI
	     * verify that Subscriber Details are displayed when a valid IMSI is provided
	     */	        
	    @Test
	    public void DisplaySubscriberBusinessIntelligenceDetailsUsingImsi_4_9_1() throws Exception {
	    	String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);	    	
	    	
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, imsi);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.clickButton(SelectedButtonType.VIEW_SUBSCRIBER_DETAILS_BUTTON);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	final Map<String, String> subscriberDetailsMap= subscriberOverview.getSubscriberDetailDialogData();
	    		    	
	    	for (final String title : defaultContentOnSubscriberDetails) {
	            assertTrue(title + " is not displayed in Subscriber Details Dialog!", subscriberDetailsMap.keySet().contains(title));
	        }
	    	
	    	assertSame(defaultContentOnSubscriberDetails.size(), subscriberDetailsMap.size());
	    }
	    
	    /**
	     * Test Case: 4.9.2 Drill down subscriber chart to view raw event data.
	     * verify that Subscriber Details are displayed when a valid IMSI is provided, and then drill down
	     * the subscriber chart to view raw event data.
	     */
	    @Test
	    public void DrillDownSubscriberChart_4_9_2() throws Exception {
	    	String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);	    	
	    	
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, imsi);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
	    	selenium.waitForPageLoadingToComplete();
	    	setUrlParametersOfChartBeforeSendingAnyRequest(imsi, ImsiMenu.IMSI);
	    	waitForPageLoadingToComplete();
	    	pause(3000);
	 
	    	final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange.ONE_DAY.getMiniutes());
	        final EventChartData data = listChartData.get(3);
	        subChart.processDrillDownChart(data.getEventName(), "Failures");
	        waitForPageLoadingToComplete();
	        pause(3000);
	        
	        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", 
	        		selenium.isTextPresent(imsi + " - Failed Event Analysis Voice"));	                	    	
	    }
	    
	    /**
	     * Test Case:4.9.3
	     * verify that subscriber business intelligence is displayed when a valid IMSI is provided
	     */
	    @Test
	    public void invalidIMSIForSubscribeBIDetails_4_9_3() throws Exception {
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, "");
	    	selenium.waitForPageLoadingToComplete();
	        assertTrue(selenium.isTextPresent("Missing or invalid input data"));
	        pause(2000);
	        subscriberTab.closeAllWindowsStart();
            pause(3000);  
	        
	        final String invalidImsi = "11";
	        selenium.waitForPageLoadingToComplete();
	        subscriberTab.enterSearchValue(invalidImsi, false);
	       
	        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_OVERVIEW_FOR_MSS);
	        assertTrue(selenium.isTextPresent("Please input a valid value"));	        
	        subscriberTab.closeAllWindowsStart();
	        
	    }
	    
	    /**
	     * Test Case: 4.9.4 
	     * Verify the recent failed event information displayed for a subscriber in the business intelligence feature
	     * when a valid IMSI is provided.
	     */	        
	    @Test
	    public void DisplayRecentFailedEventsForSubscriber_4_9_4() throws Exception {
	        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
	    	
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, imsi);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.FAILURES);
	    	pause(3000);
	    	
	        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", 
	        		selenium.isTextPresent("Failure Event Analysis for Subscriber Voice"));
	    }
	    
	    /**
	     * Test Case: 4.9.7 
	     * Verify that busy hour of the day information is displayed for a subscriber in the business intelligence feature
	     * when a valid IMSI is provided.
	     */	        
	    @Test
	    public void DisplayBusyHourOfTheDayForSubscriber_4_9_7() throws Exception {
	    	String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
	    	
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, imsi);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_HOUR);
	    	pause(3000);
	    	
	        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", 
	        		selenium.isTextPresent("Busy Hour Event Summary for Subscriber Voice"));
	    }
	    
	    /**
	     * Test Case: 4.9.8 
	     * Verify that busy day of the week information is displayed for a subscriber in the business intelligence feature
	     * when a valid IMSI is provided.
	     */	        
	    @Test
	    public void DisplayBusyDayOfTheWeekForSubscriber_4_9_8() throws Exception {
	    	String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
	    	
	    	openSubscriberOverviewWindow(ImsiMenu.IMSI, imsi);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
	    	selenium.waitForPageLoadingToComplete();
	    	
	    	subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_DAY);
	    	pause(3000);
	    	
	        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", 
	        		selenium.isTextPresent("Busy Day Event Summary for Subscriber Voice"));
	    }
	    
	    
 
	    /////////////////////////////////////////////////////////////////////////////
	    //   P R I V A T E   M E T H O D S
	    ///////////////////////////////////////////////////////////////////////////////
	    
	    private void openSubscriberOverviewWindow(final ImsiMenu type, final String value) throws PopUpException {
	        subscriberTab.openTab();
	        pause(2000);
	        subscriberTab.setSearchType(type);
	        pause(2000);
	        subscriberTab.enterSearchValue(value, type == ImsiMenu.IMSI_GROUP);
	        pause(5000);
	        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_OVERVIEW_FOR_MSS);
	        waitForPageLoadingToComplete();
	        assertTrue("Can't load a Subscriber Overview window", selenium.isTextPresent("Subscriber Overview Voice"));
	    }
	    
	    private void setUrlParametersOfChartBeforeSendingAnyRequest(final String value, final ImsiMenu menu) {
	        subChart.setParametersToBeIncludedInURL(value, menu);
	    }
	    
}
