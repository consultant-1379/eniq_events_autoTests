/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.EventChartData;
import com.ericsson.eniq.events.ui.selenium.core.charts.SubscriberBusinessIntelligenceChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.thoughtworks.selenium.SeleniumException;
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

public class SubscriberBusinessIntelligenceTestGroupWithNewUILaunch extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    private SubscriberBusinessIntelligenceChart subChart;

    @Autowired
    @Qualifier("subscriberOverviewForMSS")
    private CommonWindow subOverviewWindow;

    @Autowired
    @Qualifier("subscriberOverview")
    private SubscriberOverviewWindow subscriberOverview;

    final List<String> defaultHeadersOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_RESULT, GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE,
            GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC, GuiStringConstants.MNC));

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
            GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedController = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.CONTROLLER));

    final List<String> optionalHeadersToBeChecked = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSISDN, GuiStringConstants.IMSI,
            GuiStringConstants.INTERNAL_LOCATION_CODE, GuiStringConstants.BEARER_SERVICE_CODE, GuiStringConstants.TELE_SERVICE_CODE,
            GuiStringConstants.RAT, GuiStringConstants.CALL_ID_NUMBER, GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER,
            GuiStringConstants.CALLING_PARTY_NUMBER, GuiStringConstants.CALLED_PARTY_NUMBER, GuiStringConstants.CALLING_SUBS_IMSI,
            GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI, GuiStringConstants.CALLED_SUBS_IMEI,
            GuiStringConstants.MS_ROAMING_NUMBER, GuiStringConstants.DISCONNECTING_PARTY, GuiStringConstants.CALL_DURATION,
            GuiStringConstants.SEIZURE_TIME, GuiStringConstants.ORIGINAL_CALLED_NUMBER, GuiStringConstants.REDIRECT_NUMBER,
            GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI, GuiStringConstants.REDIRECT_SPN, GuiStringConstants.CALL_POSITION,
            GuiStringConstants.EOS_INFO, GuiStringConstants.RECORD_SEQUENCE_NUMBER, GuiStringConstants.NETWORK_CALL_REFERENCE,
            GuiStringConstants.RAC, GuiStringConstants.LAC));

    final List<String> allHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.MSISDN, GuiStringConstants.IMSI, GuiStringConstants.INTERNAL_LOCATION_CODE,
            GuiStringConstants.BEARER_SERVICE_CODE, GuiStringConstants.TELE_SERVICE_CODE, GuiStringConstants.RAT, GuiStringConstants.CALL_ID_NUMBER,
            GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER, GuiStringConstants.CALLING_PARTY_NUMBER, GuiStringConstants.CALLED_PARTY_NUMBER,
            GuiStringConstants.CALLING_SUBS_IMSI, GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI,
            GuiStringConstants.CALLED_SUBS_IMEI, GuiStringConstants.MS_ROAMING_NUMBER, GuiStringConstants.DISCONNECTING_PARTY,
            GuiStringConstants.CALL_DURATION, GuiStringConstants.SEIZURE_TIME, GuiStringConstants.ORIGINAL_CALLED_NUMBER,
            GuiStringConstants.REDIRECT_NUMBER, GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI,
            GuiStringConstants.REDIRECT_SPN, GuiStringConstants.CALL_POSITION, GuiStringConstants.EOS_INFO,
            GuiStringConstants.RECORD_SEQUENCE_NUMBER, GuiStringConstants.NETWORK_CALL_REFERENCE, GuiStringConstants.RAC, GuiStringConstants.LAC));

    final List<String> defaultHeadersOnMSISDN = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO));

    final List<String> defaultHeadersOnMSISDNClickedFailure = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_RESULT, GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE,
            GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC, GuiStringConstants.MNC));

    final List<String> eventType = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC,
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

        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";

   // @After
    @Override
    public void tearDown() throws Exception {

        logger.log(Level.INFO, "The Element ID : " + closeButtonXPath);
        while (selenium.isElementPresent(closeButtonXPath))
            selenium.click(closeButtonXPath);
        super.tearDown();
    }

    /**
     * Test Case: 4.9.1 Display Subscriber BI details using IMSI verify that Subscriber Details are displayed when a valid IMSI is provided
     */
    @Test
    public void DisplaySubscriberBusinessIntelligenceDetailsUsingImsi_4_9_1() throws Exception {
        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS/* IMSI_A */, ImsiSpecificDataType.IMSI);

        openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, imsi);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.clickButton(SelectedButtonType.VIEW_SUBSCRIBER_DETAILS_BUTTON);
        selenium.waitForPageLoadingToComplete();

        final Map<String, String> subscriberDetailsMap = subscriberOverview.getSubscriberDetailDialogData();
        waitForPageLoadingToComplete();

        //logger.log(Level.INFO, "subscriberDetailsMap : " + subscriberDetailsMap.keySet());

        //subscriberOverview.getSubscriberDetailDialogData common framework code has to be changed
        //so added this logic
        String xPathForSubscriberDetailDialog = "//*[@id='selenium_tag_PropertiesWindow']";
        String subscriberDetailDialogDataText = selenium.getText(xPathForSubscriberDetailDialog);
        logger.log(Level.INFO, "Pop up : " + subscriberDetailDialogDataText);
        //TODO you can add logic to parse this string into key,value pair

        for (final String title : defaultContentOnSubscriberDetails) {
            assertTrue(title + " is not displayed in Subscriber Details Dialog!", subscriberDetailDialogDataText.contains(title));
        }
    }

    /**
     * Test Case: 4.9.2 Drill down subscriber chart to view raw event data. verify that Subscriber Details are displayed when a valid IMSI is
     * provided, and then drill down the subscriber chart to view raw event data.
     */
    @Test
    public void DrillDownSubscriberChart_4_9_2() throws Exception {
        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, imsi);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        selenium.waitForPageLoadingToComplete();
        setUrlParametersOfChartBeforeSendingAnyRequest(imsi, ImsiMenu.IMSI);
        waitForPageLoadingToComplete();
        pause(3000);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange.ONE_DAY.getMiniutes());

        EventChartData data = null;
        String eventNameStr = null;
        for (EventChartData listDataItem : listChartData) {
            logger.log(Level.INFO, "Event Name : " + listDataItem.getEventName());
            //Event Name is of the format : mSTerminating,1 
            //so extracting event name from it
            eventNameStr = listDataItem.getEventName().split(",")[0];
            if (eventNameStr.equalsIgnoreCase("mSTerminating")) {
                data = listDataItem;

                subChart.processDrillDownChart(data.getEventName(), "Failures_" + eventNameStr);
                break;
            }
        }

        waitForPageLoadingToComplete();
        pause(3000);

        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", selenium.isTextPresent("Failed Event Analysis Voice"));

        assertTrue("Window title incorrect for Subscriber Overview - Failure Analysis window",
                selenium.isTextPresent(imsi + " - " + GuiStringConstants.IMSI + " - Failed Event Analysis Voice"));

    }

    /**
     * Test Case:4.9.3 verify that subscriber business intelligence is not displayed when an invalid IMSI is provided
     */
    /*
     * Keeping this as an unrooted test (have removed @Test annotation) as the invalid IMSI example given here is converted into a proper IMSI,
     * without any spaces, thus removing the need for throwing an error message.
     */
    public void invalidIMSIForSubscribeBIDetails_4_9_3() throws Exception {
        final String invalidImsi = "1 1";
        try {
            openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, invalidImsi);
            //NAR currently no msg displayed like this 
            assertTrue(selenium.isTextPresent("Please input a valid value"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            assertTrue(e.getClass() == SeleniumException.class);
        }

    }

    /**
     * Test Case: 4.9.4 Verify the recent failed event information displayed for a subscriber in the business intelligence feature when a valid IMSI
     * is provided.
     */
    @Test
    public void DisplayRecentFailedEventsForSubscriber_4_9_4() throws Exception {
        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS/* IMSI_A */, ImsiSpecificDataType.IMSI);

        openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, imsi);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        selenium.waitForPageLoadingToComplete();

        subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.FAILURES);
        pause(3000);

        assertTrue("Can't load a Subscriber Overview - Failure Analysis window",
                selenium.isTextPresent("Failure Event Analysis for Subscriber Voice"));
    }

    /**
     * Test Case: 4.9.7 Verify that busy hour of the day information is displayed for a subscriber in the business intelligence feature when a valid
     * IMSI is provided.
     */
    @Test
    public void DisplayBusyHourOfTheDayForSubscriber_4_9_7() throws Exception {
        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);

        openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, imsi);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        selenium.waitForPageLoadingToComplete();

        subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_HOUR);
        pause(3000);

        assertTrue("Can't load a Subscriber Overview - Failure Analysis window",
                selenium.isTextPresent("Busy Hour Event Summary for Subscriber Voice"));
    }

    /**
     * Test Case: 4.9.8 Verify that busy day of the week information is displayed for a subscriber in the business intelligence feature when a valid
     * IMSI is provided.
     */
    @Test
    public void DisplayBusyDayOfTheWeekForSubscriber_4_9_8() throws Exception {
        String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);

        openSubscriberOverviewWindow(SeleniumConstants.IMSI, false, imsi);
        selenium.waitForPageLoadingToComplete();

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        selenium.waitForPageLoadingToComplete();

        subscriberOverview.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_DAY);
        pause(3000);

        assertTrue("Can't load a Subscriber Overview - Failure Analysis window",
                selenium.isTextPresent("Busy Day Event Summary for Subscriber Voice"));
    }

    private void openSubscriberOverviewWindow(String dimensionType, boolean isGroup, String dimensionValue) throws InterruptedException,
            PopUpException {

        workspace.selectDimension(dimensionType);
        pause(5000);
        if (!isGroup) {
            workspace.enterDimensionValue(dimensionValue);
        } else {
            workspace.enterDimensionValueForGroups(dimensionValue);
        }

        pause(2000);
        String categoryPanel = MssConstants.CATEGORY_SUBSCRIBER_OVERVIEW;
        String windowOption = MssConstants.WINDOW_OPTION_CORE_CS;

        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Overview window", selenium.isTextPresent("Subscriber Overview Voice"));
    }

    private void setUrlParametersOfChartBeforeSendingAnyRequest(final String value, final ImsiMenu menu) {
        subChart.setParametersToBeIncludedInURL(value, menu);
    }

}