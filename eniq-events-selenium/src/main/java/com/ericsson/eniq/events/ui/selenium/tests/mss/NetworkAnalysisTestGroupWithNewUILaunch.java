/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.mss;
/**                     
 * @since 2012           
*/

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.thoughtworks.selenium.SeleniumException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class NetworkAnalysisTestGroupWithNewUILaunch extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    @Qualifier("networkEventAnalysisForMSS")
    private CommonWindow eventAnalysisWindow;

    @Autowired
    @Qualifier("networkRoamingAnalysisByOperator")
    private CommonWindow roamingAnalysisByOperatorWindow;

    @Autowired
    @Qualifier("networkRoamingAnalysisByCountry")
    private CommonWindow roamingAnalysisByCountryWindow;

    @Autowired
    @Qualifier("networkCauseCodeAnalysisForMSS")
    private CommonWindow causeCodeAnalysisWindow;

    @Autowired
    @Qualifier("networkEventVolumnVoiceForMSS")
    private CommonWindow networkEventVolumnVoiceWindow;

    @Autowired
    @Qualifier("eventVolumnVoiceForMSS")
    private CommonWindow eventVolumnVoiceWindow;

    @Autowired
    @Qualifier("causeCodeRankingsForMSS")
    private CommonWindow causeCodeRankingsWindow;

    @Autowired
    @Qualifier("KPIWindow")
    private CommonWindow kpiWindow;

    @Autowired
    @Qualifier("mscBlockedRankingsForMSS")
    private CommonWindow mscBlockedRankingsWindow;

    @Autowired
    @Qualifier("mscDroppedRankingsForMSS")
    private CommonWindow mscDroppedRankingsWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Network test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Network test section");
    }

    final List<String> defaultHeadersOnMSCEventAnalysisVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnGroupNetworkEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnControllerEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnCauseCodeAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.INTERNAL_CAUSE_CODE_ID,
            GuiStringConstants.FAULT_CODE_ID, GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.OCCURRENCES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnAccessAreaAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedFailesOnMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.EVENT_TYPE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_RESULT, GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE, GuiStringConstants.MSC,
            GuiStringConstants.MCC, GuiStringConstants.MNC));

    final List<String> defaultHeadersOnFailedEventAnalysisLocationServices = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_RESULT, GuiStringConstants.UNSUCCESSFUL_POSITIONING_REASON, GuiStringConstants.MSC,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC,
            GuiStringConstants.MNC));

    final List<String> defaultViewContentOnEventVolumeVoice = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.MSORIGINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_DROP_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_COMPLETION_COUNT,
            GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_DROP_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSTERMINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_DROP_COUNT, GuiStringConstants.CALLFORWARDING_CALL_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_COUNT, GuiStringConstants.ROAMINGCALLFORWARDING_CALL_BLOCK_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_DROP_COUNT, GuiStringConstants.LOCATION_REQUESTS_COUNT,
            GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUEST_COUNT, GuiStringConstants.TOTAL_NETWORK_EVENTS,
            GuiStringConstants.CALL_FORWARDING_CALL_BLOCK_COUNT, GuiStringConstants.CALL_FORWARDING_CALL_DROP_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_SMS_COUNT,
            GuiStringConstants.MSORIGINATING_SMS_FAIL_COUNT, GuiStringConstants.MSTERMINATING_SMS_COUNT,
            GuiStringConstants.MSTERMINATING_SMS_FAIL_COUNT));

    final List<String> defaultViewContentOnNetworkEventVolumeVoice = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.MSORIGINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_DROP_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_COMPLETION_COUNT,
            GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_DROP_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSTERMINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_DROP_COUNT, GuiStringConstants.CALLFORWARDING_CALL_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_COUNT, GuiStringConstants.ROAMINGCALLFORWARDING_CALL_BLOCK_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_DROP_COUNT, GuiStringConstants.LOCATION_REQUESTS_COUNT,
            GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUEST_COUNT, GuiStringConstants.TOTAL_NETWORK_EVENTS,
            GuiStringConstants.CALL_FORWARDING_CALL_BLOCK_COUNT, GuiStringConstants.CALL_FORWARDING_CALL_DROP_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_SMS_COUNT,
            GuiStringConstants.MSORIGINATING_SMS_FAIL_COUNT, GuiStringConstants.MSTERMINATING_SMS_COUNT,
            GuiStringConstants.MSTERMINATING_SMS_FAIL_COUNT));

    final List<String> defaultViewContentOnEventVolumeVoiceGrid = new ArrayList<String>(Arrays.asList(GuiStringConstants.TIME,
            GuiStringConstants.MSORIGINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSORIGINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_DROP_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_COMPLETION_COUNT,
            GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_DROP_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSTERMINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_DROP_COUNT, GuiStringConstants.CALLFORWARDING_CALL_COUNT,
            GuiStringConstants.CALL_FORWARDING_CALL_BLOCK_COUNT, GuiStringConstants.CALL_FORWARDING_CALL_DROP_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_COUNT, GuiStringConstants.ROAMINGCALLFORWARDING_CALL_BLOCK_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_DROP_COUNT, GuiStringConstants.LOCATION_REQUESTS_COUNT,
            GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUEST_COUNT, GuiStringConstants.MSORIGINATING_SMS_COUNT,
            GuiStringConstants.MSORIGINATING_SMS_FAIL_COUNT, GuiStringConstants.MSTERMINATING_SMS_COUNT,
            GuiStringConstants.MSTERMINATING_SMS_FAIL_COUNT, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.TOTAL_NETWORK_EVENTS));

    final List<String> defaultViewContentOnNetworkEventVolumeVoiceGrid = new ArrayList<String>(Arrays.asList(GuiStringConstants.TIME,
            GuiStringConstants.MSORIGINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSORIGINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSORIGINATING_CALL_DROP_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_COMPLETION_COUNT,
            GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT, GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_DROP_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_COMPLETION_COUNT, GuiStringConstants.MSTERMINATING_CALL_BLOCK_COUNT,
            GuiStringConstants.MSTERMINATING_CALL_DROP_COUNT, GuiStringConstants.CALLFORWARDING_CALL_COUNT,
            GuiStringConstants.CALLFORWARDING_BLOCK_COUNT, GuiStringConstants.CALL_FORWARDING_CALL_DROP_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_COUNT, GuiStringConstants.ROAMINGCALLFORWARDING_CALL_BLOCK_COUNT,
            GuiStringConstants.ROAMINGCALLFORWARDING_CALL_DROP_COUNT, GuiStringConstants.LOCATION_REQUESTS_COUNT,
            GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUEST_COUNT, GuiStringConstants.MSORIGINATING_SMS_COUNT,
            GuiStringConstants.MSORIGINATING_SMS_FAIL_COUNT, GuiStringConstants.MSTERMINATING_SMS_COUNT,
            GuiStringConstants.MSTERMINATING_SMS_FAIL_COUNT, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.TOTAL_NETWORK_EVENTS));

    final List<String> defaultHeadersOnKPIAnalysisByControllerVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnKPIAnalysisByAccessAreaVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.VENDOR,
            GuiStringConstants.ACCESS_AREA, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnFailedEventAnalysisSMS = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_RESULT, GuiStringConstants.SMS_RESULT, GuiStringConstants.MSC, GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC, GuiStringConstants.MNC));

    final List<String> eventTypeOnMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC,
            GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING,
            GuiStringConstants.CALLFORWARDING, GuiStringConstants.MSORIGINATING, GuiStringConstants.LOCATIONSERVICES));

    final List<String> eventTypeOnController = new ArrayList<String>(Arrays.asList(GuiStringConstants.LOCATIONSERVICES,
            GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSORIGINATING,
            GuiStringConstants.MSTERMINATING));

    final List<String> eventTypeOnAccessArea = new ArrayList<String>(Arrays.asList(GuiStringConstants.LOCATIONSERVICES,
            GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSORIGINATING,
            GuiStringConstants.MSTERMINATING));

    final List<String> defaultHeadersOnRoamingAnalysisByCountryWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.COUNTRY,
            GuiStringConstants.EVENT_FAILURES, GuiStringConstants.EVENT_SUCCESSES, GuiStringConstants.ROAMING_SUBSCRIBERS));

    final List<String> defaultHeadersOnRoamingAnalysisByOperatorWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.OPERATOR,
            GuiStringConstants.EVENT_FAILURES, GuiStringConstants.EVENT_SUCCESSES, GuiStringConstants.ROAMING_SUBSCRIBERS));

    final List<String> defaultContentOnTheKPIView = new ArrayList<String>(Arrays.asList(GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.MS_ORIGINATING_CALL_COMPLETION_RATIO, GuiStringConstants.MS_TERMINATING_CALL_COMPLETION_RATIO,
            GuiStringConstants.MS_ORIGINATING_CALL_DROP_RATIO, GuiStringConstants.MS_TERMINATING_CALL_DROP_RATIO,
            GuiStringConstants.MS_ORIGINATING_CALL_ATTEMPTS_INTENSITY, GuiStringConstants.MS_TERMINATING_CALL_ATTEMPTS_INTENSITY,
            GuiStringConstants.MS_ORIGINATING_CALL_DROP_INTENSITY, GuiStringConstants.MS_TERMINATING_CALL_DROP_INTENSITY,
            GuiStringConstants.CALL_FORWARDING_SUCCESS_RATIO, GuiStringConstants.ROAMING_CALL_SUCCESS_RATIO,
            GuiStringConstants.CALL_FORWARDING_DROP_RATIO, GuiStringConstants.ROAMING_CALL_DROP_RATIO));

    final List<String> defaultContentOnTheKPIViewGrid = new ArrayList<String>(Arrays.asList(GuiStringConstants.TIME,
            GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.MS_ORIGINATING_CALL_COMPLETION_RATIO,
            GuiStringConstants.MS_TERMINATING_CALL_COMPLETION_RATIO, GuiStringConstants.MS_ORIGINATING_CALL_DROP_RATIO,
            GuiStringConstants.MS_TERMINATING_CALL_DROP_RATIO, GuiStringConstants.MS_ORIGINATING_CALL_ATTEMPTS_INTENSITY,
            GuiStringConstants.MS_TERMINATING_CALL_ATTEMPTS_INTENSITY, GuiStringConstants.MS_ORIGINATING_CALL_DROP_INTENSITY,
            GuiStringConstants.MS_TERMINATING_CALL_DROP_INTENSITY, GuiStringConstants.CALL_FORWARDING_SUCCESS_RATIO,
            GuiStringConstants.ROAMING_CALL_SUCCESS_RATIO, GuiStringConstants.CALL_FORWARDING_DROP_RATIO, GuiStringConstants.ROAMING_CALL_DROP_RATIO,
            GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_COMPLETION_RATIO, GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_DROP_RATIO,
            GuiStringConstants.SMS_ORIGINATING_SUCCESS_RATIO, GuiStringConstants.SMS_TERMINATING_SUCCESS_RATIO,
            GuiStringConstants.LOCATION_REQUESTS_SUCCESS_RATIO));

    final List<String> expectedHeadersOnCauseCodeRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.INTERNAL_CAUSE_CODE_ID));

    final List<String> eventType = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC,
            GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING,
            GuiStringConstants.CALLFORWARDING, GuiStringConstants.MSORIGINATING, GuiStringConstants.LOCATIONSERVICES));

    final List<String> defaultHeaderOnEventAnalysisVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.INTERNAL_CAUSE_CODE_ID,
            GuiStringConstants.FAULT_CODE_ID, GuiStringConstants.FAULT_CODE, GuiStringConstants.EVENT_TYPE, GuiStringConstants.OCCURRENCES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnEventAnalysisVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.MSC, GuiStringConstants.EXTERNAL_PROTOCOL, GuiStringConstants.INCOMING_ROUTE, GuiStringConstants.OUTGOING_ROUTE,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_RESULT, GuiStringConstants.EXTERNAL_CAUSE_VALUE,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE));

    final List<String> defaultHeadersOnFaultCodeAnalysis = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.MSC,
            GuiStringConstants.EXTERNAL_PROTOCOL, GuiStringConstants.INCOMING_ROUTE, GuiStringConstants.OUTGOING_ROUTE,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.EXTERNAL_CAUSE_VALUE, GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE));

    final List<String> defaultHeaderOnGroupEventAnalsyisVoice = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeaderOnNetworkAnalysisRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.VENDOR, GuiStringConstants.MSC, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES));

    final List<String> defaultHeaderOnNetworkAnalysisInternalCauseCodeAnalysis = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.INTERNAL_CAUSE_CODE_ID, GuiStringConstants.FAULT_CODE_ID, GuiStringConstants.FAULT_CODE,
            GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnEventAnalysisVoiceCall_Forwarding = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.MSC, GuiStringConstants.INCOMING_ROUTE, GuiStringConstants.OUTGOING_ROUTE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE));

    final List<String> defaultHeadersOnEventAnalysisVoiceRoamingCall_Forwarding = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.MSC, GuiStringConstants.INCOMING_ROUTE, GuiStringConstants.OUTGOING_ROUTE, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE));

    @Autowired
    private WorkspaceRC workspace;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);

        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";

  //  @After
    @Override
    public void tearDown() throws Exception {

        logger.log(Level.INFO, "The Element ID : " + closeButtonXPath);
        while (selenium.isElementPresent(closeButtonXPath))
            selenium.click(closeButtonXPath);
        super.tearDown();
    }

    /**
     * Test Case: 4.7.1 MSC event can be displayed in CS event network session
     */

    @Test
    public void MSCEventAnalysisCanBeDisplayedInCSeventsNetworkSessionAsSelected_7_1() throws Exception {
        final String msc = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);
        final TimeRange timeRange = getTimeRangeFromProperties();

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, msc);

        assertTrue("The headers are not right on " + eventAnalysisWindow.getTimeLabelText(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnMSCEventAnalysisVoice));
        eventAnalysisWindow.setTimeRange(timeRange);

        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        final List<String> eventTypeFromUI = eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);
        assertTrue("The event types are not " + eventTypeFromUI, eventTypeFromUI.containsAll(eventTypeOnMSC));
        final List<Map<String, String>> dataDisplayedOnUI = eventAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + msc, NetworkAnalysis.eventAnalysis(dataDisplayedOnUI, eventType,
                GuiStringConstants.MSC, msc, "NOT_SPECIFIC", defaultHeadersOnMSCEventAnalysisVoice, timeRange.getMiniutes()));

    }

    private String extractControllerName(String fullControllerName) {

        final String[] test = fullControllerName.split(DataIntegrityConstants.COMMA_SYMBOL);
        String controller = DataIntegrityConstants.BLANK_SPACE;
        if (test.length > 0)
            controller = test[0];
        return controller;
    }

    /**
     * Test Case: 4.7.2 Controller event can be displayed in CS event network session.
     */
    // sync pass
    @Test
    public void ControllerEventAnalysisCanBeDisplayedInCSEventNetwork_7_2() throws Exception {
        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        final TimeRange timeRange = getTimeRangeFromProperties();

        openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(controller));
        waitForPageLoadingToComplete();
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        assertTrue("The headers are not right on " + eventAnalysisWindow.getTimeLabelText(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnControllerEventAnalysisWindow));

        final List<String> eventTypeFromUI = eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        assertTrue("The event types are not " + eventTypeFromUI, eventTypeFromUI.containsAll(eventTypeOnController));
        final List<Map<String, String>> dataDisplayedOnUI = eventAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + controller, NetworkAnalysis.eventAnalysis(dataDisplayedOnUI, eventType,
                GuiStringConstants.CONTROLLER, controller, "NOT_SPECIFIC", defaultHeadersOnControllerEventAnalysisWindow, timeRange.getMiniutes()));

    }

    /**
     * Test Case: 4.7.3 Access Area event can be displayed in CS event network session.
     */
    // sync pass
    @Test
    public void AccessAreaEventAnalysisCanBeDisplayedInCSEventNetwork_7_3() throws Exception {
        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        final TimeRange timeRange = getTimeRangeFromProperties();

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, accessArea);
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        pause(2000);
        assertTrue("The headers are not right on " + eventAnalysisWindow.getTimeLabelText(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnAccessAreaAnalysisWindow));
        final List<String> eventTypeFromUI = eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);
        assertTrue("The event types are not " + eventTypeFromUI, eventTypeFromUI.containsAll(eventTypeOnAccessArea));
        final List<Map<String, String>> dataDisplayedOnUI = eventAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + accessArea, NetworkAnalysis.eventAnalysis(dataDisplayedOnUI, eventType,
                GuiStringConstants.ACCESS_AREA, accessArea, "NOT_SPECIFIC", defaultHeadersOnAccessAreaAnalysisWindow, timeRange.getMiniutes()));
    }

    /**
     * Test Case: 4.7.4 To verify that failure breakdown for a selected MSC is displayed and is drillable to internal cause code and fault code level.
     */
    @Test
    public void DisplayFailureBreakdownViewForASelectedMSC_7_4() throws Exception {
        final String msc = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openCauseCodeAnalysisWindowByGridView(SeleniumConstants.MSC, false, msc);

        assertTrue("The headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnCauseCodeAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, causeCodeAnalysisWindow);
        waitForPageLoadingToComplete();
        final List<Map<String, String>> dataDisplayedOnUI = causeCodeAnalysisWindow.getAllTableData();

        assertTrue(
                DataIntegrityConstants.CAUSE_CODE_ANALYSIS_ERROR_MESSAGE + msc,
                NetworkAnalysis.causeCodeAnalysis(dataDisplayedOnUI, GuiStringConstants.MSC, msc, defaultHeadersOnCauseCodeAnalysisWindow,
                        timeRange.getMiniutes()));
        drilldownCauseCodes();
    }

    /**
     * Test Case: 4.7.5 To verify that failure breakdown for a selected Controller node is displayed and is drillable to internal cause code and fault
     * code level.
     */
    // sync pass
    @Test
    public void DisplayFailureBreakdownViewForASelectedController_7_5() throws Exception {
        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER);

        openCauseCodeAnalysisWindowByGridView(SeleniumConstants.CONTROLLER, false, controller);

        assertTrue("The headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnCauseCodeAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, causeCodeAnalysisWindow);
        waitForPageLoadingToComplete();

        final List<Map<String, String>> dataDisplayedOnUI = causeCodeAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.CAUSE_CODE_ANALYSIS_ERROR_MESSAGE + controller, NetworkAnalysis.causeCodeAnalysis(dataDisplayedOnUI,
                GuiStringConstants.CONTROLLER, "BSC1,CELL1,Ericsson", defaultHeadersOnCauseCodeAnalysisWindow, timeRange.getMiniutes()));

        drilldownCauseCodes();
    }

    /**
     * Test Case: 4.7.6 To verify that failure breakdown for a selected Access Area node is displayed and is drillable to internal cause code and
     * fault level.
     */
    // no data yet so cannot be run
    @Test
    public void DisplayFailureBreakdownViewForASelectedAccessArea_7_6() throws Exception {
        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openCauseCodeAnalysisWindowByGridView(SeleniumConstants.ACCESS_AREA, false, accessArea);

        waitForPageLoadingToComplete();
        assertTrue("The headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnCauseCodeAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, causeCodeAnalysisWindow);
        waitForPageLoadingToComplete();
        final List<Map<String, String>> dataDisplayedOnUI = causeCodeAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.CAUSE_CODE_ANALYSIS_ERROR_MESSAGE + accessArea, NetworkAnalysis.causeCodeAnalysis(dataDisplayedOnUI,
                GuiStringConstants.ACCESS_AREA, accessArea, defaultHeadersOnCauseCodeAnalysisWindow, timeRange.getMiniutes()));
        drilldownCauseCodesAnalysisVoice();
    }

    /**
     * Test Case: 4.7.7 To verify that failure breakdown for a selected group is displayed and is drillable to internal cause code and fault code
     * level.
     */
    @Test
    public void DisplayFailureBreakdownViewForASelectedGroupsAndDrillInternalCauseCodeToFaultCodeLevel_7_7() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openCauseCodeAnalysisWindowByGridView(SeleniumConstants.MSC_GROUP, true, searchValue);

        assertTrue("The headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnCauseCodeAnalysisWindow));
        causeCodeAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        drilldownCauseCodes();
    }

    /**
     * Test Case: 4.7.8 Verify the KPIs supported in Network Analysis per nominated MSC.
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedMSC_7_8() throws Exception {

        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, searchValue);

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        eventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        kpiWindow.openChartView(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.7.8 Verify the KPIs supported in Network Analysis per nominated MSC.
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedMSC_7_8_1() throws Exception {

        final String msc = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, msc);

        final TimeRange timeRange = getTimeRangeFromProperties();
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        eventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        openKPIWindowByGridView();

        closePreviousWindow();

        final List<Map<String, String>> completeUITableValues = kpiWindow.getAllPagesData();

        assertTrue(
                DataIntegrityConstants.KPI_ANALYSIS_ERROR_MESSAGE + msc,
                NetworkAnalysis.kpiAnalysis(completeUITableValues, defaultContentOnTheKPIViewGrid, GuiStringConstants.MSC, msc, eventType,
                        timeRange.getMiniutes(), DataIntegrityConstants.KPI_ANALYSIS));

    }

    /**
     * Test Case: 4.7.9 KPIs supported in Network Analysis with an invalid MSC name input.
     * 
     */
    // sync pass
    @Test
    public void KPIsSupportedInNetworkAnalysisWithAnInvalidMSCNameInput_7_9() throws Exception {
        final String searchValue = "MSS_INVALID";

        try {
            openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, searchValue);
        } catch (Exception e1) {

            logger.log(Level.SEVERE, e1.toString());
            assertTrue(e1.getClass() == SeleniumException.class);
        }
    }

    /**
     * Test Case: 4.7.10 Verify the KPIs supported in Network Analysis per nominated controller(2G)
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedController2G_7_10() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(searchValue));

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        pause(2000);
        eventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        pause(2000);
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        checkWindowUpdatedForTimeRanges("KPI Analysis Voice", kpiWindow);
    }

    /**
     * Test Case: 4.7.10 Verify the KPIs supported in Network Analysis per nominated controller(2G)
     */
    // not sync but pass
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedController2G_7_10_1() throws Exception {
        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(controller));

        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        pause(4000);
        eventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        pause(2000);
        openKPIWindowByGridView();
        pause(2000);
        closePreviousWindow();
        final List<Map<String, String>> completeUITableValues = kpiWindow.getAllPagesData();
        waitForPageLoadingToComplete();
        assertTrue(DataIntegrityConstants.KPI_ANALYSIS_ERROR_MESSAGE + controller, NetworkAnalysis.kpiAnalysis(completeUITableValues,
                defaultContentOnTheKPIViewGrid, GuiStringConstants.CONTROLLER, controller, eventType, timeRange.getMiniutes(),
                DataIntegrityConstants.KPI_ANALYSIS));
    }

    /**
     * Test Case: 4.7.11 KPIs supported in Network Analysis with an invalid controller name input. //NAR these test cases are not relevant
     */

    @Test
    public void KPIsSupportedInNetworkAnalysisWithAnInvalidControllerNameInput_7_11() throws Exception {
        final String searchValue = "MSS_INVALID";

        try {
            openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, searchValue);

        } catch (final Exception e1) {

            logger.log(Level.SEVERE, e1.toString());
            assertTrue(e1.getClass() == SeleniumException.class);
        }
    }

    /**
     * Test Case: 4.7.12 Verify the KPIs supported in Network Analysis per nominated access area(2G)
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedAccessArea2G_7_12_1() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, searchValue);

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        final SelectedButtonType button = SelectedButtonType.KPI_BUTTON_MSS;
        pause(4000);
        assertTrue("The KPI button is not available", eventAnalysisWindow.isButtonEnabled(button));
        eventAnalysisWindow.clickButton(button);
        pause(2000);
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        kpiWindow.clickConfigureBarAndConfigureItems(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.7.12 Verify the KPIs supported in Network Analysis per nominated access area(2G)
     */
    // pass
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedAccessArea2G_7_12_2() throws Exception {
        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, accessArea);
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        pause(4000);
        final SelectedButtonType button = SelectedButtonType.KPI_BUTTON_MSS;
        assertTrue("The KPI button is not available", eventAnalysisWindow.isButtonEnabled(button));
        eventAnalysisWindow.clickButton(button);
        pause(2000);
        kpiWindow.openGridView();
        pause(2000);
        closePreviousWindow();
        final List<Map<String, String>> completeUITableValues = kpiWindow.getAllPagesData();
        waitForPageLoadingToComplete();
        assertTrue(DataIntegrityConstants.KPI_ANALYSIS_ERROR_MESSAGE + accessArea, NetworkAnalysis.kpiAnalysis(completeUITableValues,
                defaultContentOnTheKPIViewGrid, GuiStringConstants.ACCESS_AREA, accessArea, eventType, timeRange.getMiniutes(),
                DataIntegrityConstants.KPI_ANALYSIS));

    }

    /**
     * Test Case: 4.7.12 Verify the KPIs supported in Network Analysis per nominated access area(2G)
     */
    // pass
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedAccessArea2G_7_12_3() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA_GROUP, true, searchValue);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        pause(4000);
        final SelectedButtonType button = SelectedButtonType.KPI_BUTTON_MSS;
        assertTrue("The KPI button is not available", eventAnalysisWindow.isButtonEnabled(button));
        eventAnalysisWindow.clickButton(button);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        waitForPageLoadingToComplete();
        kpiWindow.clickConfigureBarAndConfigureItems(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.7.12 Verify the KPIs supported in Network Analysis per nominated access area(2G)
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisPerNominatedAccessArea2G_7_12_4() throws Exception {
        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA_GROUP, true, accessArea);

        final TimeRange timeRange = getTimeRangeFromProperties();
        final String accessAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_MSS);
        delayAndSetTimeRange(timeRange, eventAnalysisWindow);
        waitForPageLoadingToComplete();
        pause(4000);
        final SelectedButtonType button = SelectedButtonType.KPI_BUTTON_MSS;
        assertTrue("The KPI button is not available", eventAnalysisWindow.isButtonEnabled(button));
        eventAnalysisWindow.clickButton(button);
        kpiWindow.openGridView();
        pause(2000);
        closePreviousWindow();
        final List<Map<String, String>> completeUITableValues = kpiWindow.getAllTableData();
        waitForPageLoadingToComplete();

        assertTrue(DataIntegrityConstants.KPI_ANALYSIS_ERROR_MESSAGE + accessArea, NetworkAnalysis.kpiAnalysis(completeUITableValues,
                defaultContentOnTheKPIViewGrid, GuiStringConstants.ACCESS_AREA, accessAreaGroup, eventType, timeRange.getMiniutes(),
                DataIntegrityConstants.KPI_GROUP_ANALYSIS));
    }

    /**
     * Test Case: 4.7.13 KPIs supported in Network Analysis with an invalid access area name input.
     */
    @Test
    public void KPIsSupportedInNetworkAnalysisWithAnInvalidAccessAreaNameInput_7_13() throws Exception {
        final String searchValue = "MSS_INVALID";
        try {
            openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, searchValue);

        } catch (final Exception e) {
            assertTrue(e.getClass() == SeleniumException.class);
        }

    }

    /**
     * Test Case: 4.7.15 MSC events can be displayed in CS event network session.
     */

    @Test
    public void DrilldownDownViewOfFailedEventAnalysisCanBeDisplayedInCSEventsNetworkSession_7_15() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, searchValue);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        drillDownAllFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.CALLFORWARDING,
                GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.LOCATIONSERVICES);
    }

    /**
     * Test Case: 4.7.16 Verify that it is possible to drill-down into events summary information by clicking Success Ratio
     */
    // sync pass
    @Test
    public void VerifySuccessRatioDrillDownFunction_7_16() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, searchValue);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        assertTrue("The header is not right on Event Analysis",
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnGroupNetworkEventAnalysisWindow));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, "Event Type", "mSOriginating", "mSTerminating");
        assertTrue("Cannot open KPI Analysis By Controller Voice on drill downing success ration", selenium.isTextPresent("Analysis"));

    }

    /**
     * Test Case: 4.7.17 To verify that it is possible to view Cause Code Ranking from the Network Tab.
     */

    @Test
    public void DisplayCauseCodeRankingVoice_7_17() throws Exception {

        openWindowForMSS(MssConstants.CATEGORY_CORE_CAUSE_CODE_RANKING, MssConstants.WINDOW_OPTION_CORE_CS);

        causeCodeRankingsWindow.getTableHeaders().containsAll(expectedHeadersOnCauseCodeRanking);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Cause Code", causeCodeRankingsWindow, expectedHeadersOnCauseCodeRanking,
                GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION, DataIntegrityConstants.STATUS_NONE, null, DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     * Test Case: 4.7.18 Verify Group support for Network Analysis
     */

    @Test
    public void VerifyGroupSupportForNetworkAnalysis_7_18() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC_GROUP, true, searchValue);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        assertTrue("The header is not right", eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnGroupNetworkEventAnalysisWindow));
    }

    /**
     * Test Case: 4.7.21 Roaming Analysis by Operator can be displayed in CS event network session.
     */
    @Test
    public void RoamingAnalysisCanBeDisplayedInCSeventsNetworkSessionRoamingByOperator_7_21() throws Exception {

        openWindowForMSS(MssConstants.CATEGORY_CORE_ROAMING_BY_OPERATOR, MssConstants.CATEGORY_EVENT_VOLUME);

        String toggleBtnXpathOld = "//table[@id='btnToggleToGrid']//button";

        if (selenium.isElementPresent(toggleBtnXpathOld)) {
            logger.log(Level.INFO, "Found  Toggle Button");

        } else {
            logger.log(Level.INFO, "Unable to find Toggle Button");
        }

        pause(4000);
        roamingAnalysisByOperatorWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        waitForPageLoadingToComplete();
        pause(2000);
        assertTrue("The header is not right on Roaming analysis by operator",
                roamingAnalysisByOperatorWindow.getTableHeaders().containsAll(defaultHeadersOnRoamingAnalysisByOperatorWindow));
    }

    /**
     * Test Case: 4.7.22 Roaming Analysis by Operator can be displayed in CS event network session.
     */
    // sync pass
    @Test
    public void RoamingAnalysisCanBeDisplayedInCSeventsNetworkSessionRoamingByCountry_7_22() throws Exception {

        openWindowForMSS(MssConstants.CATEGORY_CORE_ROAMING_BY_COUNTRY, MssConstants.CATEGORY_EVENT_VOLUME);

        String toggleBtnXpathOld = "//table[@id='btnToggleToGrid']//button";

        if (selenium.isElementPresent(toggleBtnXpathOld)) {
            logger.log(Level.INFO, "Found OLD Toggle Button");

        } else {
            logger.log(Level.INFO, "Unable to find OLD Toggle Button");
        }

        pause(2000);
        roamingAnalysisByCountryWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);

        waitForPageLoadingToComplete();
        pause(2000);
        assertTrue("The header is not right on Roaming analysis by operator",
                roamingAnalysisByCountryWindow.getTableHeaders().containsAll(defaultHeadersOnRoamingAnalysisByCountryWindow));
    }

    /**
     * Test Case: 4.7.23 Roaming Analysis by Operator can be displayed in CS event network session.
     */
    // sync pass
    @Test
    public void RoamingAnalysisRawDataCorrectnessRoamingByOperator_7_23() throws Exception {

        openWindowForMSS(MssConstants.CATEGORY_CORE_ROAMING_BY_OPERATOR, MssConstants.CATEGORY_EVENT_VOLUME);

    }

    /**
     * Test Case: 4.7.24 Roaming Analysis by Operator can be displayed in CS event network session.
     */

    @Test
    public void RoamingAnalysisRawDataCorrectnessRoamingByCountry_7_24() throws Exception {

        openWindowForMSS(MssConstants.CATEGORY_CORE_ROAMING_BY_COUNTRY, MssConstants.CATEGORY_EVENT_VOLUME);

    }

    /**
     * Test Case: 4.7.25 This test case is to verify network event analysis can give the event count summary based on the event type for whole network
     */
    @Test
    public void NetworkEventVolumeAnalysis_7_25_1() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC, false, value);
        networkEventVolumnVoiceWindow.openChartView(defaultViewContentOnNetworkEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", networkEventVolumnVoiceWindow);
        networkEventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnNetworkEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.25 This test case is to verify network event analysis can give the event count summary based on the event type for whole network
     */
    // no write
    @Test
    public void NetworkEventVolumeAnalysis_7_25_2() throws Exception {
        final String mss = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC, false, mss);
        networkEventVolumnVoiceWindow.openGridView();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", networkEventVolumnVoiceWindow,
                defaultViewContentOnNetworkEventVolumeVoiceGrid, GuiStringConstants.MSC, mss, null,
                DataIntegrityConstants.NETWORK_EVENT_VOLUME_ANALYSIS);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    // pass
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_1() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        try {
            openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC, false, value);
        } catch (Exception e) {

            logger.log(Level.SEVERE, e.toString());
        }
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    // pass
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_2() throws Exception {
        final String mss = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC, false, mss);
        eventVolumnVoiceWindow.openGridView();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.MSC, mss, null, DataIntegrityConstants.EVENT_VOLUME_ANALYSIS);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_3() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(value));
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_4() throws Exception {
        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(controller));
        eventVolumnVoiceWindow.openGridView();

        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.CONTROLLER, controller, null, DataIntegrityConstants.EVENT_VOLUME_ANALYSIS);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_5() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.ACCESS_AREA, false, value);
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.26 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network element.
     */
    @Test
    // pass
    public void EventVolumeAnalysisPerNominatedNetworkElement_7_26_6() throws Exception {
        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.ACCESS_AREA, false, accessArea);
        eventVolumnVoiceWindow.openGridView();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.ACCESS_AREA, accessArea, null, DataIntegrityConstants.EVENT_VOLUME_ANALYSIS);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_1() throws Exception {
        final String mscGroup = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC_GROUP, true, mscGroup);
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_2() throws Exception {

        final String mssGroupName = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);
        final String[] mssGroupFields = reservedDataHelper.getCommonReservedData(CommonDataType.MSS_GROUP_DATA_MSS).split(
                DataIntegrityConstants.COMMA_SYMBOL);
        final List<String> mssGroupFieldsList = new ArrayList<String>(Arrays.asList(mssGroupFields));

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.MSC_GROUP, true, mssGroupName);
        eventVolumnVoiceWindow.openGridView();
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.MSC, DataIntegrityConstants.STATUS_NONE, mssGroupFieldsList, DataIntegrityConstants.EVENT_VOLUME_GROUP_ANALYSIS);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_3() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.CONTROLLER_GROUP, true, value);
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_4() throws Exception {

        final String controllerGroupName = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_MSS);
        final String[] controllerGroupFields = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_MSS).split(
                DataIntegrityConstants.HYPHEN_SYMBOL);
        final List<String> controllerGroupFieldsList = new ArrayList<String>(Arrays.asList(controllerGroupFields));

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.CONTROLLER_GROUP, true, controllerGroupName);
        eventVolumnVoiceWindow.openGridView();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.CONTROLLER, DataIntegrityConstants.STATUS_NONE, controllerGroupFieldsList,
                DataIntegrityConstants.EVENT_VOLUME_GROUP_ANALYSIS);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_5() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.ACCESS_AREA_GROUP, true, value);
        eventVolumnVoiceWindow.openChartView(defaultViewContentOnEventVolumeVoice);
        checkWindowUpdatedForTimeRanges("Event Volume", eventVolumnVoiceWindow);
        eventVolumnVoiceWindow.clickConfigureBarAndConfigureItems(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Test Case: 4.7.27 This test case is to verify network event analysis can give the event count summary basing on the event type per nominated
     * network group.
     */
    // sync
    @Test
    public void EventVolumeAnalysisPerNominatedNetworkGroup_7_27_6() throws Exception {

        final String accessAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        final String[] accessAreaGroupFields = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_MSS).split(
                DataIntegrityConstants.HYPHEN_SYMBOL);
        final List<String> accessAreaGroupFieldsList = new ArrayList<String>(Arrays.asList(accessAreaGroupFields));

        openNetworkEventVolumeVoiceWindowForMSS(SeleniumConstants.ACCESS_AREA_GROUP, true, accessAreaGroup);
        eventVolumnVoiceWindow.openGridView();
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Volume", eventVolumnVoiceWindow, defaultViewContentOnEventVolumeVoiceGrid,
                GuiStringConstants.ACCESS_AREA, DataIntegrityConstants.STATUS_NONE, accessAreaGroupFieldsList,
                DataIntegrityConstants.EVENT_VOLUME_GROUP_ANALYSIS);
    }

    /**
     * Test Case: 4.7.29 Verify MSOriginatingSMSinMSC event is available in event analysis window of MSC.
     */
    @Test
    public void MSOriginatingSMSinMSCCDRsInEventAnalysisViewForMSC_7_29() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);
    }

    /**
     * Test Case: 4.7.30 Verify the values in window MSS KPIRatio Drilldown from MSC to BSC matches the value in DB.
     */

    @Test
    public void VerificationOfValuesInMSSKPIRatioDrilldownFromMSCToBSCForEventMSOriginatingSMSinMSC_7_30() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
    }

    /**
     * Test Case: 4.7.31 Verify the values in window MSS KPIRatio Drilldown from BSC to CELL matches the value in DB.
     */

    @Test
    public void VerificationOfValuesInMSSKPIRatioDrilldownFromBSCToCELLForEventMSOriginatingSMSinMSC_7_31() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
    }

    /**
     * Test Case: 4.7.32 Verify the values in window MSS KPIRatio Drilldown from BSC to CELL matches the value in DB.
     */
    @Test
    public void VerificationOfValuesInMSSKPIRatioDrilldownFromCELLToEventAnalysisForEventMSOriginatingSMSinMSC_7_32() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
    }

    /**
     * Test Case: 4.7.33 Verify MSOriginatingSMS event is available in event analysis window of Controller.
     */

    @Test
    public void MSOriginatingSMSinMSCCDRsInEventAnalysisViewForController_7_33() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(value));
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);
    }

    /**
     * Test Case: 4.7.34 Verify MSOriginatingSMS event is available in event analysis window of Controller.
     */
    @Test
    public void MSOriginatingSMSinMSCCDRsInEventAnalysisViewForAccessArea_7_34() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);
    }

    /**
     * Test Case: 4.7.35 Verify MSOriginatingSMS event is available in event analysis window of Controller.
     */
    @Test
    public void MSTerminatingSMSinMSCCDRsInEventAnalysisViewForMSC_7_35() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
    }

    /**
     * Test Case: 4.7.36 Verify the values in window MSS KPIRatio Drilldown from BSC to CELL matches the value in DB.
     */
    @Test
    public void verificationOfValuesInMSSKPIRatioDrilldownFromMSCToBSCForEventMSTerminatingSMSinMSC_7_36() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
    }

    /**
     * Test Case: 4.7.37 Verify the values in window MSS KPIRatio Drilldown from BSC to CELL matches the value in DB.
     */
    @Test
    public void verificationOfValuesInMSSKPIRatioDrilldownFromBSCToCELLForEventMSTerminatingSMSinMSC_7_37() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();

        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));

        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
    }

    /**
     * Test Case: 4.7.38 Verify the values in window MSS KPIRatio Drilldown from BSC to CELL matches the value in DB.
     */
    @Test
    public void verificationOfValuesInMSSKPIRatioDrilldownFromCELLToEventAnalysisForEventMSTerminatingSMSinMSC_7_38() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
    }

    /**
     * Test Case: 4.7.39 Verify MSTerminatingSMSinMSC event is available in event analysis window of Controller.
     */
    @Test
    public void mSTerminatingSMSinMSCCDRsInEventAnalysisViewForController_7_39() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.CONTROLLER, false, extractControllerName(value));
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);
    }

    /**
     * Test Case: 4.7.40 Verify MSTerminatingSMSinMSC event is available in event analysis window of Controller.
     */
    @Test
    public void mSTerminatingSMSinMSCCDRsInEventAnalysisViewForAccessArea_7_40() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.ACCESS_AREA, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATINGSMSINMSC, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnFailedEventAnalysisSMS));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);
    }

    /**
     * US-23 Test Case: MSS Enhancement Verify MSORIGINATING event is available in event analysis window of MSC by failures. Test Case: 4.7.41
     */
    @Test
    public void MSORIGINATINGCDRSINEVENTANALYSISVIEWFORMSC_MSS() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSORIGINATING));
        drillDownFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));

    }

    /**
     * US-23 Test Case: MSS Group Verify MSTERMINATING event is available in event analysis window of MSC by failures[Group]. Test Case: 4.7.42
     */
    @Test
    public void MSTERMINATINGCDRSINEVENTANALYSISVIEWFORMSCGROUP_MSS() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC_GROUP, true, searchValue);

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSTERMINATING));
        drillDownFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        assertTrue("The header is not right", eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));
    }

    /**
     * US-22 Test Case: MSS Enhancement Verify MSTERMINATING event is available in event analysis window of MSC- Success Ratio. Test Case: 4.7.43
     */
    @Test
    public void MSTERMINATINGinMSCCDRsInEventAnalysisViewForMSC_MSS() throws Exception {
        final String value = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC, false, value);
        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSTERMINATING));

        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);

        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeaderOnEventAnalysisVoice));

        eventAnalysisWindow.clickTableCell(0, GuiStringConstants.OCCURRENCES);

        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));

    }

    /**
     * US-22 Test Case: MSS Enhancement Verify MSORIGINATING event is available in event analysis window of MSC GROUP- Success Ratio. Test Case:
     * 4.7.44
     */

    @Test
    public void MSORIGINATINGinMSCCDRsInEventAnalysisViewForMSCGROUP_MSS() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openEventAnalysisWindowForMSS(SeleniumConstants.MSC_GROUP, true, searchValue);

        eventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSORIGINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSORIGINATING));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeaderOnGroupEventAnalsyisVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);

        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeaderOnEventAnalysisVoice));

        eventAnalysisWindow.clickTableCell(0, GuiStringConstants.OCCURRENCES);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));

    }

    /**
     * US-21 Test Case: MSS Ranking for Dropped Voice To verify that it is possible to view Ranking of Dropped Voice under Network Tab. MSORGINATING -
     * Failures Test Case: 4.7.45
     */

    @Test
    public void DisplayDroppedVoiceRanking() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openRankingForMSS(MssConstants.WINDOW_OPTION_DROPPED_VOICE_CALLS_BY_MSC);

        mscDroppedRankingsWindow.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + mscDroppedRankingsWindow.getWindowHeaderLabel(), mscDroppedRankingsWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisRanking));
        drillDownMSCRankingBlockedDroppedWindow(mscDroppedRankingsWindow, GuiStringConstants.MSC, searchValue);

        selenium.click(closeButtonXPath);

        waitForPageLoadingToComplete();

        assertTrue("No " + GuiStringConstants.MSORIGINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSORIGINATING));
        drillDownFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));

    }

    /**
     * US-22 Test Case: MSS Ranking for Blocked Voice To verify that it is possible to view Ranking of Blocked Voice under Network Tab. MSTERMINATING
     * - Success Test Case: 4.7.46
     */

    @Test
    public void DisplayBlockedVoiceRanking() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openRankingForMSS(MssConstants.WINDOW_OPTION_BLOCKED_VOICE_CALLS_BY_MSC);

        mscBlockedRankingsWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + mscBlockedRankingsWindow.getWindowHeaderLabel(), mscBlockedRankingsWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisRanking));
        drillDownMSCRankingBlockedDroppedWindow(mscBlockedRankingsWindow, GuiStringConstants.MSC, searchValue);

        selenium.click(closeButtonXPath);

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.MSTERMINATING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.MSTERMINATING));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByControllerVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnKPIAnalysisByAccessAreaVoice));
        drillDownSuccessRatioOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATING);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeaderOnEventAnalysisVoice));

        eventAnalysisWindow.clickTableCell(0, GuiStringConstants.OCCURRENCES);
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoice));
        checkWindowUpdatedForTimeRanges("Event Analysis", eventAnalysisWindow);

    }

    /**
     * US-24 Test Case: MSS Internal Cause code analysis Voice To verify that it is possible to view Internal Cause code analysis Voice under Network
     * Tab for selected MSC Test Case: 4.7.47
     */

    @Test
    public void DisplayInternalCauseCodeAnalysis() throws Exception {

        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openInternalCauseCodeAnalysisWindowForMSS(SeleniumConstants.MSC, false, searchValue);
        waitForPageLoadingToComplete();
        causeCodeAnalysisWindow.openGridView();
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisInternalCauseCodeAnalysis));

        for (int index = 0; index < 732; index++) {
            final String causeCode = causeCodeAnalysisWindow.getTableData(index, 2);

            if (causeCode.equals(DataIntegrityConstants.FAULTCODE)) {
                causeCodeAnalysisWindow.clickTableCell(index, GuiStringConstants.FAULT_CODE);
                break;
            }
        }
        assertTrue("Headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnFaultCodeAnalysis));

    }

    /**
     * US-25 Test Case: MSS Internal Cause code analysis Voice To verify that it is possible to view Internal Cause code analysis Voice under Network
     * Tab for selected MSC group Test Case: 4.7.48
     */
    // sync pass
    @Test
    public void DisplayInternalCauseCodeAnalysisMSSGroup() throws Exception {

        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_GROUP_MSS);

        openInternalCauseCodeAnalysisWindowForMSS(SeleniumConstants.MSC_GROUP, true, searchValue);

        waitForPageLoadingToComplete();

        causeCodeAnalysisWindow.openGridView();
        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisInternalCauseCodeAnalysis));

        for (int index = 0; index < 732; index++) {
            final String causeCode = causeCodeAnalysisWindow.getTableData(index, 2);

            if (causeCode.equals(DataIntegrityConstants.FAULTCODE)) {
                causeCodeAnalysisWindow.clickTableCell(index, GuiStringConstants.FAULT_CODE);
                break;
            }
        }
        assertTrue("Headers are not right on " + causeCodeAnalysisWindow.getWindowHeaderLabel(), causeCodeAnalysisWindow.getTableHeaders()
                .containsAll(defaultHeadersOnFaultCodeAnalysis));

    }

    /**
     * US-92 Test Case: MSS Ranking for Dropped Voice To verify that it is possible to view Ranking of Dropped Voice under Network Tab. CALLFORWARDING
     * - Failures Test Case: 4.7.49
     */
    @Test
    public void DisplayDroppedVoiceRanking_CallForwarding() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openRankingForMSS(MssConstants.WINDOW_OPTION_DROPPED_VOICE_CALLS_BY_MSC);

        mscDroppedRankingsWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + mscDroppedRankingsWindow.getWindowHeaderLabel(), mscDroppedRankingsWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisRanking));
        drillDownMSCRankingBlockedDroppedWindow(mscDroppedRankingsWindow, GuiStringConstants.MSC, searchValue);
        waitForPageLoadingToComplete();
        pause(2000);

        selenium.click(closeButtonXPath);

        assertTrue("No " + GuiStringConstants.CALLFORWARDING,
                eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(GuiStringConstants.CALLFORWARDING));
        drillDownFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.CALLFORWARDING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoiceCall_Forwarding));

    }

    /**
     * US-92 Test Case: MSS Ranking for Blocked Voice To verify that it is possible to view Ranking of Dropped Voice under Network Tab.
     * ROAMINGCALLFORWARDING - Failures Test Case: 4.7.50
     */

    @Test
    public void DisplayDroppedVoiceRanking_RCallForwarding() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.MSC_MSS);

        openRankingForMSS(MssConstants.WINDOW_OPTION_DROPPED_VOICE_CALLS_BY_MSC);
        mscDroppedRankingsWindow.setTimeRange(getTimeRangeFromProperties());

        waitForPageLoadingToComplete();
        assertTrue("Headers are not right on " + mscDroppedRankingsWindow.getWindowHeaderLabel(), mscDroppedRankingsWindow.getTableHeaders()
                .containsAll(defaultHeaderOnNetworkAnalysisRanking));
        drillDownMSCRankingBlockedDroppedWindow(mscDroppedRankingsWindow, GuiStringConstants.MSC, searchValue);

        selenium.click(closeButtonXPath);

        waitForPageLoadingToComplete();
        assertTrue("No " + GuiStringConstants.ROAMINGCALLFORWARDING, eventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE)
                .contains(GuiStringConstants.ROAMINGCALLFORWARDING));
        drillDownFailuresOnEventAnalysisWindow(eventAnalysisWindow, GuiStringConstants.EVENT_TYPE, GuiStringConstants.ROAMINGCALLFORWARDING);
        assertTrue("Headers are not right on " + eventAnalysisWindow.getWindowHeaderLabel(),
                eventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnEventAnalysisVoiceRoamingCall_Forwarding));

    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Check time range, the time ranges is read from properties file //ewandaf
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */
    private void checkWindowUpdatedForTimeRanges(final String networkType, final CommonWindow commonWindow) throws NoDataException, PopUpException {
        assertTrue("Can't load " + networkType + " window", selenium.isTextPresent(networkType));

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
        TimeRange[] timeRanges;

        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            commonWindow.setTimeRange(time);
            waitForPageLoadingToComplete();
            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
        }
    }

    private void openCauseCodeAnalysisWindowByGridView(final String dimensionType, final boolean isGroup, final String searchValue)
            throws PopUpException, InterruptedException {

        workspace.selectDimension(dimensionType);
        pause(2000);
        try {
            if (!isGroup)
                workspace.enterDimensionValue(searchValue);
            else

                workspace.enterDimensionValueForGroups(searchValue);
        } catch (InterruptedException e) {

            logger.log(Level.SEVERE, e.toString());
        }

        pause(2000);
        String categoryPanel = MssConstants.CATEGORY_CAUSE_CODE_ANALYSIS;
        String windowOption = MssConstants.WINDOW_OPTION_CORE_CS;

        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();

        pause(2000);
        waitForPageLoadingToComplete();

        selenium.click("//div[@class='x-panel-body wizard-top-body']//label[contains(text(), 'Grid View')]");

        logger.log(Level.INFO, "" + selenium.isElementPresent("//div[@class='x-panel-body x-panel-body-noheader']"));
        selenium.click("//div[@class='x-panel-body x-panel-body-noheader']//div[@class=' x-component']//button");
        waitForPageLoadingToComplete();
    }

    private void drilldownCauseCodes() throws Exception {
        //causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Sub Cause Code Analysis Voice", selenium.isTextPresent("Fault Code Analysis Voice"));
    }

    private void drilldownCauseCodesAnalysisVoice() throws Exception {
        //causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Sub Cause Code Analysis Voice", selenium.isTextPresent("Cell Internal Cause Code Analysis Voice"));
    }

    private void drillDownAllFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws Exception {
        for (final String value : values) {
            if (window.getAllTableDataAtColumn(columnToCheck).contains(value)) {
                drillDownFailuresOnEventAnalysisWindow(window, columnToCheck, value);
                window.clickButton(SelectedButtonType.BACK_BUTTON);
                waitForPageLoadingToComplete();
            }
        }
    }

    /**
     * drill down the success ration on event analysis window
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck
     * @param values
     */
    private void drillDownSuccessRatioOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.SUCCESS_RATIO);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        final int column = window.getTableHeaders().indexOf("Success Ratio");
        window.clickTableCell(row, column, "gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open KPI Analysis page", selenium.isTextPresent("Analysis"));
    }

    /**
     * drill down the Failure ration on event analysis window
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck
     * @param values
     */
    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.FAILURES);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        final int column = window.getTableHeaders().indexOf("Failures");
        window.clickTableCell(row, column, "gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open KPI Analysis page", selenium.isTextPresent("Analysis"));
    }

    private void drillDownMSCRankingBlockedDroppedWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.MSC);
        waitForPageLoadingToComplete();
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        final int column = window.getTableHeaders().indexOf("MSC");
        window.clickTableCell(row, column, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Event Analysis page", selenium.isTextPresent("Analysis"));
    }

    private void drillDownMSCInternalCauseCodeanalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.FAULT_CODE);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);

        final int column = window.getTableHeaders().indexOf("Fault Code");
        window.clickTableCell(row, column, "gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Internal Cause code Analysis Page", selenium.isTextPresent("Analysis"));
    }

    /**
     * Open KPI window by chart and verify the items are right
     */
    private void openKPIWindowByChartView() throws PopUpException {
        final List<String> content = new ArrayList<String>();
        selenium.click("//div[contains(@id, 'selenium_tag_BaseWindow_KPI')]//div[@class='x-panel-body wizard-top-body']//label[contains(text(), 'Chart View')]");
        final Number count = selenium.getXpathCount("//div[@class='wizard-content wizard-content-body-large']"
                + "//div[@class='x-panel-body x-panel-body-noheader wizard-content-body']//span");
        for (int i = 0; i < count.intValue(); i++) {
            final String xPath = "//div[@class='wizard-content wizard-content-body-large']"
                    + "//div[@class='x-panel-body x-panel-body-noheader wizard-content-body']";
            final String item = selenium.getAttribute(xPath + "//span[" + (i + 1) + "]@title");
            content.add(item);
            selenium.click(xPath + "//span[" + (i + 1) + "]//input");
        }
        assertTrue("The item is not right", content.containsAll(defaultContentOnTheKPIView));
        selenium.click("//div[@class='x-panel-body x-panel-body-noheader wizard-btns-body']//button");
        waitForPageLoadingToComplete();
    }

    private void openKPIWindowByGridView() throws PopUpException {
        selenium.click("//div[@class='x-panel-body wizard-top-body']//label[contains(text(), 'Grid View')]");
        selenium.click("//div[@class='x-panel-body x-panel-body-noheader']//div[@class=' x-component']//button");
        waitForPageLoadingToComplete();
    }

    /**
     * Set time range on KPI window and only for window
     * 
     * @param timeRange the object of TimeRange
     */
    private void setTimeRangeOnKPIWindow(final TimeRange timeRange) throws PopUpException {
        final String timeDialogOKButtonXPath = "//div[contains(@class, 'x-panel-btns')]//tr[contains(@class, 'x-toolbar-right-row')]//button[contains(text(),'Ok') or contains(text(),'OK')]";
        selenium.click("//div[contains(@id, 'selenium_tag_BaseWindow_KPI')]//table[@id='btnTime']//button");
        selenium.waitForElementToBePresent("//div[contains(@class, 'x-window timeDlg')][contains(@style, 'visibility: visible')]", "10000");
        selenium.click("//div[@id='selenium_tag_timeRange']/input[@type='radio']");
        selenium.type("//input[@id='selenium_tag_time-input']", timeRange.getLabel());
        selenium.click(timeDialogOKButtonXPath);
        selenium.waitForPageLoadingToComplete();
    }

    /**
     * check all time range on KPI window and only for KPI window
     */
    private void checkAllTimeRangeOnKPIwindow() throws PopUpException {

        for (final TimeRange time : TimeRange.values()) {
            setTimeRangeOnKPIWindow(time);
            waitForPageLoadingToComplete();
            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
        }
    }

    /**
     * Set date and time by opening the time dialog window.
     * 
     * @param window the object of CommonWindow
     * @param startDate e.g. "2011, 1, 2", the delimiter is comma
     * @param startTimeCandidate Enum
     * @param endDate e.g. "2011, 1, 2", the delimiter is comma
     * @param endTimeCnadidate Enum
     */
    private void setTimeAndDateAndVerify(final CommonWindow window, final String startDate, final TimeCandidates startTimeCandidate,
                                         final String endDate, final TimeCandidates endTimeCnadidate) throws PopUpException {

        final String[] startDateArray = startDate.split(",");
        final String[] endDateArray = endDate.split(",");
        final Date sDate = window.getDate(Integer.parseInt(startDateArray[0].trim()), Integer.parseInt(startDateArray[1].trim()),
                Integer.parseInt(startDateArray[2].trim()));
        final Date eDate = window.getDate(Integer.parseInt(endDateArray[0].trim()), Integer.parseInt(endDateArray[1].trim()),
                Integer.parseInt(endDateArray[2].trim()));
        window.setTimeAndDateRange(sDate, startTimeCandidate, eDate, endTimeCnadidate);
        waitForPageLoadingToComplete();
        final SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        final String timeLabel = startTimeCandidate.getDescription() + ", " + datePattern.format(sDate) + " to " + endTimeCnadidate.getDescription()
                + ", " + datePattern.format(eDate);
        assertTrue("The time label is not right", window.getTimeLabelText().equals(timeLabel));
    }

    /**
     * Set date and time in time dialog window with a lag of configurable time. For example : By selecting 15 mins on EventAnalysis window, it will
     * display data with a specific time lag. i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30, but due to delay
     * in updation of UI at current time, it fails to display all the expected data till 11:30. Therefore to display all the expected datathis
     * function creates a time lag for example of 5 mins and fetches data from 11:10 to 11:25. Similarly for all other time time ranges, it displays
     * data with a time lag of 5 mins. Note : This time lag can be configurable from properties.
     * 
     * @param TimeRange ex: 15mins, 30mins etc. NOTE : Cannot set timeRange for 5 mins due to UI constraints.
     */
    private void delayAndSetTimeRange(final TimeRange timeRange, final CommonWindow window) throws PopUpException {
        final GregorianCalendar date = new GregorianCalendar();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat AMPMFormatter = new SimpleDateFormat("a");
        final Formatter startDatefmt = new Formatter();
        final Formatter endDatefmt = new Formatter();

        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }

        final Date currentDate = date.getTime();
        logger.log(Level.INFO, "Current Date : " + currentDate);

        final long currentTime = currentDate.getTime();
        final int timeRangeInMins = timeRange.getMiniutes();

        long endDateTime = currentTime - (15 * 60 * 1000);

        date.setTimeInMillis(endDateTime);
        Date endDate = date.getTime();

        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));

        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }

        date.setTimeInMillis(endDateTime);
        endDate = date.getTime();

        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);

        date.setTimeInMillis(startDateTime);
        final Date startDate = date.getTime();

        startDatefmt.format("%tl", startDate);
        String hourStartDate = startDatefmt.toString();
        int lengthOfHour = hourStartDate.length();
        if (lengthOfHour == 1) {

            hourStartDate = "0" + hourStartDate;
        }

        endDatefmt.format("%tl", endDate);
        String hourEndDate = endDatefmt.toString();
        lengthOfHour = hourEndDate.length();
        if (lengthOfHour == 1) {

            hourEndDate = "0" + hourEndDate;
        }

        final String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        final String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_"
                + minFormatter.format(endDate.getTime());

        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate + " " + startDateTimeCandidate
                + " and End Date Time Candidate : " + endDate + " " + endDateTimeCandidate);
        window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate, TimeCandidates.valueOf(endDateTimeCandidate));
        waitForPageLoadingToComplete();
    }

    public void checkWindowUpdatedForTimeRangesWithDataIntegrity(final String networkType, final CommonWindow commonWindow,
                                                                 final List<String> defaultRankingHeader, final String analysisFieldName,
                                                                 final String analysisFieldValue, final List<String> fieldsOnGroup,
                                                                 final String validationString) throws NoDataException, PopUpException {
        assertTrue("Can't load " + networkType + " window", selenium.isTextPresent(networkType));

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
        TimeRange[] timeRanges;

        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            final String timeLabel = time.getLabel();

            if (!timeLabel.equalsIgnoreCase("5 minutes")) {
                commonWindow.setTimeRange(time);
                delayAndSetTimeRange(time, commonWindow);
                waitForPageLoadingToComplete();
                assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
                if (!validationString.equalsIgnoreCase(DataIntegrityConstants.STATUS_NONE)) {

                    final int duration = time.getMiniutes();
                    final List<Map<String, String>> completeUITableValues = commonWindow.getAllPagesData();
                    logger.log(Level.INFO, "The headers are :" + commonWindow.getTableHeaders());
                    if (validationString.equalsIgnoreCase(DataIntegrityConstants.EVENT_VOLUME_ANALYSIS)) {

                        assertTrue(DataIntegrityConstants.EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE + analysisFieldName,
                                NetworkAnalysis.eventVolumeAnalysisNewUI(completeUITableValues, defaultRankingHeader, analysisFieldName,
                                        analysisFieldValue, null, eventType, validationString, timeLabel));

                    } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.NETWORK_EVENT_VOLUME_ANALYSIS)) {

                        assertTrue(DataIntegrityConstants.NETWORK_EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE + analysisFieldName,
                                NetworkAnalysis.eventVolumeAnalysisNewUI(completeUITableValues, defaultRankingHeader, analysisFieldName,
                                        analysisFieldValue, null, eventType, validationString, timeLabel));

                    } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.EVENT_VOLUME_GROUP_ANALYSIS)) {

                        assertTrue(DataIntegrityConstants.EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE + analysisFieldName,
                                NetworkAnalysis.eventVolumeAnalysisNewUI(completeUITableValues, defaultRankingHeader, analysisFieldName,
                                        analysisFieldValue, fieldsOnGroup, eventType, validationString, timeLabel));
                    } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.NETWORK_EVENT_VOLUME_GROUP_ANALYSIS)) {

                        assertTrue(DataIntegrityConstants.NETWORK_EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE + analysisFieldName,
                                NetworkAnalysis.eventVolumeAnalysisNewUI(completeUITableValues, defaultRankingHeader, analysisFieldName,
                                        analysisFieldValue, fieldsOnGroup, eventType, validationString, timeLabel));
                    } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.RANKING_ANALYSIS)) {

                        if (validationString.equalsIgnoreCase(DataIntegrityConstants.RANKING_ANALYSIS)) {
                            assertTrue(analysisFieldName + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE, RankingAnalysis.rankingAnalysis(
                                    completeUITableValues, defaultRankingHeader, analysisFieldName, analysisFieldValue, duration));
                        }

                    }
                }
            }
        }
    }

    private void openEventAnalysisWindowForMSS(String dimensionType, boolean isGroup, String dimensionValue) throws InterruptedException {

        workspace.selectDimension(dimensionType);
        pause(2000);
        if (!isGroup) {
            workspace.enterDimensionValue(dimensionValue);
        } else {
            workspace.enterDimensionValueForGroups(dimensionValue);
        }

        pause(2000);
        String categoryPanel = MssConstants.CATEGORY_NETWORK_EVENT_ANALYSIS;
        String windowOption = MssConstants.WINDOW_OPTION_CORE_CS;

        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
    }

    private void openNetworkEventVolumeVoiceWindowForMSS(String dimensionType, boolean isGroup, String dimensionValue) throws InterruptedException,
            PopUpException {

        workspace.selectDimension(dimensionType);
        pause(2000);
        if (!isGroup) {
            workspace.enterDimensionValue(dimensionValue);
        } else {
            workspace.enterDimensionValueForGroups(dimensionValue);
        }

        pause(2000);
        String categoryPanel = MssConstants.CATEGORY_EVENT_VOLUME;
        String windowOption = MssConstants.WINDOW_OPTION_CORE_CS;

        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();

    }

    private void openInternalCauseCodeAnalysisWindowForMSS(String dimensionType, boolean isGroup, String dimensionValue) throws InterruptedException {

        workspace.selectDimension(dimensionType);
        pause(2000);
        if (!isGroup) {
            workspace.enterDimensionValue(dimensionValue);
        } else {
            workspace.enterDimensionValueForGroups(dimensionValue);
        }

        pause(2000);
        String categoryPanel = MssConstants.CATEGORY_CAUSE_CODE_ANALYSIS;
        String windowOption = MssConstants.WINDOW_OPTION_CORE_CS;

        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();

    }

    private void openRankingForMSS(String windowOption) throws InterruptedException, PopUpException {

        openWindowForMSS(MssConstants.CATEGORY_CORE_RANKING, windowOption);

    }

    private void openWindowForMSS(String categoryPanel, String windowOption) throws InterruptedException, PopUpException {

        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_CS);
        pause(2000);
        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void closePreviousWindow() {

        selenium.click(closeButtonXPath);
    }
}
