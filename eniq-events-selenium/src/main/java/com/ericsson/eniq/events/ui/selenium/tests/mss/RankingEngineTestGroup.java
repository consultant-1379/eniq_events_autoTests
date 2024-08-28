package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
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

/**
 * @author ewandaf
 * @since 2011
 *
 */

public class RankingEngineTestGroup extends EniqEventsUIBaseSeleniumTest {
    public enum ColumnNames {
        CONTROLLER("Controller"), TAC("TAC"), ACCESS_AREA("Access Area");
        private final String label;

        private ColumnNames(final String txtLabel) {
            label = txtLabel;
        }

        public String getLabel() {
            return label;
        }
    }

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("subBlockedRankings")
    private CommonWindow subBlockedWindow;

    @Autowired
    @Qualifier("accessAreaBlockedRankings")
    private CommonWindow accessAreaBlockedWindow;

    @Autowired
    @Qualifier("accessAreaDroppedRankings")
    private CommonWindow accessAreaDroppedWindow;

    @Autowired
    @Qualifier("subscriberEventAnalysisForMSS")
    private CommonWindow subscriberEventAnalysis;

    @Autowired
    @Qualifier("networkEventAnalysisForMSS")
    private CommonWindow networkEventAnalysis;

    @Autowired
    @Qualifier("subDroppedRankingsForMSS")
    private CommonWindow subDroppedWindow;

    @Autowired
    @Qualifier("termRankingsBlocked")
    private CommonWindow termRankingsBlockedWindow;

    @Autowired
    @Qualifier("termRankingsDropped")
    private CommonWindow termRankingsDroppedWindow;

    @Autowired
    @Qualifier("causeCodeRankingsForMSS")
    private CommonWindow causeCodeRankingsWindow;

    @Autowired
    @Qualifier("msOrigUnansweredCallsRankingsForMSS")
    private CommonWindow msOrigUnansweredCallsRankingWindow;

    @Autowired
    @Qualifier("msTermUnansweredCallsRankingsForMSS")
    private CommonWindow msTermUnansweredCallsRankingWindow;

    @Autowired
    @Qualifier("mscBlockedRankingsForMSS")
    private CommonWindow mscBlockedRankingWindow;

    @Autowired
    @Qualifier("mscDroppedRankingsForMSS")
    private CommonWindow mscDroppedRankingWindow;

    @Autowired
    @Qualifier("rncRankingBlockedVoiceCalls")
    private CommonWindow rncBlockedVoiceCallsRankingWindow;

    @Autowired
    @Qualifier("rncRankingDroppedVoiceCalls")
    private CommonWindow rncDroppedVoiceCallsRankingWindow;

    @Autowired
    @Qualifier("bscRankingBlockedVoiceCalls")
    private CommonWindow bscBlockedVoiceCallsRankingWindow;

    @Autowired
    @Qualifier("bscRankingDroppedVoiceCalls")
    private CommonWindow bscDroppedVoiceCallsRankingWindow;

    @Autowired
    @Qualifier("longDurationRankingVoiceCalls")
    private CommonWindow longDruationVoiceCallsRankingWindow;

    @Autowired
    @Qualifier("shortDurationRankingVoiceCalls")
    private CommonWindow shortDruationVoiceCallsRankingWindow;

    final List<String> expectedHeadersOnSelectedIMSI = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE, GuiStringConstants.MSC,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.MNC));

    final List<String> expectedHeadersOnSelectedController = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> expectedHeadersOnSelectedMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> expectedHeadersOnSelectedAccessArea = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.ACCESS_AREA));

    final List<String> expectedHeadersOnSelectedTAC = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> expectedHeadersOnTerminalRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.MANUFACTURER, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TAC, GuiStringConstants.MODEL));

    final List<String> expectedHeadersOnSubscriberRanking = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.RANK, GuiStringConstants.IMSI, GuiStringConstants.FAILURES,
                    GuiStringConstants.SUCCESSES));

    final List<String> expectedHeadersOnCauseCodeRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION, GuiStringConstants.INTERNAL_CAUSE_CODE_ID ,
            GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES));

    final List<String> expectedHeadersOnAccessAreaRanking = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.RAT, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES));

    final List<String> expectedHeadersOnRNCRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.RNC, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES));

    final List<String> expectedHeadersOnBSCRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.BSC, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES));

    final List<String> expectedHeadersOnDurationRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.IMSI, GuiStringConstants.MSISDN, GuiStringConstants.CALLING_PARTY_NUMBER,
            GuiStringConstants.CALL_DURATION));

    final List<String> expectedHeadersOnUnansweredCallsRanking = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.IMSI, GuiStringConstants.CALLING_PARTY_NUMBER,
            GuiStringConstants.UNANSWERED_CALLS));

    final List<String> expectedHeadersOnMSCRanking = new ArrayList<String>(Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.VENDOR, GuiStringConstants.MSC, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES));

    final List<String> eventTypes = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC,
            GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSTERMINATING,
            GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.CALLFORWARDING,
            GuiStringConstants.MSORIGINATING, GuiStringConstants.LOCATIONSERVICES));
    
    //MSS Enhancement
    
    final List<String> expectedHeadersOnEventAnalysis_MSO_MST_Failures = new ArrayList<String>(Arrays.asList(
    		GuiStringConstants.EVENT_TIME,GuiStringConstants.MSC,GuiStringConstants.EXTERNAL_PROTOCOL,
    		GuiStringConstants.INCOMING_ROUTE,GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.CONTROLLER,
    		GuiStringConstants.ACCESS_AREA,GuiStringConstants.TAC,GuiStringConstants.TERMINAL_MAKE,
    		GuiStringConstants.TERMINAL_MODEL,GuiStringConstants.EVENT_RESULT,
    		GuiStringConstants.EXTERNAL_CAUSE_VALUE,GuiStringConstants.INTERNAL_CAUSE_CODE,
    		GuiStringConstants.FAULT_CODE));

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of MSS Ranking test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of MSS Ranking test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        rankingsTab.openTab();
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
     *  Test Case: 4.6.1
     *  Verify Subscriber ranking summary can support to view top 50 subscribers 
     *  who experienced the most dropped call in recent 30 minutes.
     */
    // sync  pass
    @Test
    public void SubscriberRankingSummarySupportTop50AsDefault_6_1() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        assertTrue("The number of events is greater than 50", subDroppedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(subDroppedWindow);
        checkWindowUpdatedForTimeRanges("Subscriber", subDroppedWindow);
    }

    /**
     *  Test Case: 4.6.3
     *  Verify Subscriber ranking summary information is accuracy 
     *  and the information is sorted by the number of experienced dropped call.
     */
    // sync pass
    @Test
    public void SubscriberRankingSummaryAccuracyVerification_6_3() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        assertTrue("The number of events is greater than 50", subDroppedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(subDroppedWindow);
        //checkWindowUpdatedForTimeRanges("Subscriber", subDroppedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Subscriber", subDroppedWindow,
                expectedHeadersOnSubscriberRanking, GuiStringConstants.IMSI, DataIntegrityConstants.STATUS_DROPPED,
                DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.4
     *  Verify CauseCode ranking summary can support to view top 50 CauseCodes 
     *  who experienced the most dropped call in recent 30 minutes.
     */
    // sync pass
    @Test
    public void CauseCodeRankingSummarySupportTop50AsDefault_6_4() throws Exception {
        rankingsTab.openStartMenu(RankingsTab.StartMenu.RANKINGS_CAUSE_CODE);
        assertTrue("The number of events is greater than 50", causeCodeRankingsWindow.getTableRowCount() <= 50);
        assertTrue("Default time range is NOT equal to 30 minutes",
                causeCodeRankingsWindow.getTimeRange().equals("30 minutes"));
        sortAllTableColumn(causeCodeRankingsWindow);
    }

    /**
     *  Test Case: 4.6.6
     *  Verify CauseCode ranking summary information is accuracy and 
     *  the information is sorted by the number of experienced dropped call.
     */
    // sync pass
    @Test
    public void CauseCodeRankingSummaryAccuracyVerification_6_6() throws Exception {
        rankingsTab.openStartMenu(RankingsTab.StartMenu.RANKINGS_CAUSE_CODE);
        assertTrue("The number of events is greater than 50", causeCodeRankingsWindow.getTableRowCount() <= 50);
        assertTrue("The header is not right",
                causeCodeRankingsWindow.getTableHeaders().containsAll(expectedHeadersOnCauseCodeRanking));
        assertTrue("Default time range is NOT equal to 30 minutes",
                causeCodeRankingsWindow.getTimeRange().equals("30 minutes"));
        final TimeRange timeRange = getTimeRangeFromProperties();
        final int duration = timeRange.getMiniutes();
        delayAndSetTimeRange(timeRange, causeCodeRankingsWindow);
        final List<Map<String, String>> completeUITableValues = causeCodeRankingsWindow.getAllTableData();
        assertTrue(GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION
                + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE, RankingAnalysis.rankingAnalysis(
                completeUITableValues, expectedHeadersOnCauseCodeRanking,
                GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION, DataIntegrityConstants.STATUS_NONE, duration));
    }

    /**
     *  Test Case: 4.6.7
     *  verify that the most occurring Cause Codes are displayed
     */
    // sync pass
    @Test
    public void DisplayWorstPerformingInternalCauseCodesByFailures_6_7() throws Exception {
        rankingsTab.openStartMenu(RankingsTab.StartMenu.RANKINGS_CAUSE_CODE);
        causeCodeRankingsWindow.getTableHeaders().containsAll(expectedHeadersOnCauseCodeRanking);
        checkWindowUpdatedForTimeRanges("Cause Code", causeCodeRankingsWindow);
    }

    /**
     *  Test Case: 4.6.8
     *  To verify that it is possible to drill the ranking tables by Internal Cause Codes to 
     *  view the related cause codes analysis.
     */
    // drill down the cause code has problem
    @Test
    public void DrillByInternalCauseCodesToViewTheCauseCodeAnalysis_6_8() throws Exception {
        rankingsTab.openStartMenu(RankingsTab.StartMenu.RANKINGS_CAUSE_CODE);
        causeCodeRankingsWindow.getTableHeaders().containsAll(expectedHeadersOnCauseCodeRanking);
        checkWindowUpdatedForTimeRanges("Cause Code", causeCodeRankingsWindow);
    }

    /**
     *  Test Case: 4.6.9
     *  To verify it is possible to drill in to IMSI through the ENIQ Events GUI.
     */
    // sync pass
    @Test
    public void DrillByIMSIForSubscriberRankingDroppedCalls_6_9() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        openEventAnalysis(subDroppedWindow, "Subscriber", "IMSI");
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        sortAllTableColumn(subscriberEventAnalysis);
        assertTrue("IMSI table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnSelectedIMSI));
        checkWindowUpdatedForTimeRanges("Subscriber Event Analysis", subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.10
     *  To verify that it is possible to drill into TAC level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. To verify that it is possible to drill down 
     *  from the TAC level summary information screen.
     */
    // sync pass
    @Test
    public void DrillByTACForSubscriberRankingDroppedCalls_6_10() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        openEventAnalysis(subDroppedWindow, "Subscriber", "IMSI");
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.TAC);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.TAC, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedTAC));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.TAC, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.11
     *  To verify that it is possible to drill into Controller level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. To verify that it is possible to drill down from the 
     *  Controller level summary information screen.
     */
    // sync pass
    @Test
    public void DrillByControllerForSubscriberRankingDroppedCalls_6_11() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        openEventAnalysis(subDroppedWindow, "IMSI", "IMSI");
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.CONTROLLER);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.CONTROLLER, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedController));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CONTROLLER, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.12
     *  To verify that it is possible to drill into Access area level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed.
     */
    // sync pass
    @Test
    public void DrillByAccessAreaForSubscriberRankingDroppedCalls_6_12() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        openEventAnalysis(subDroppedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.ACCESS_AREA);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.ACCESS_AREA,
                subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnSelectedAccessArea));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.13
     *  To verify that it is possible to drill into MSC level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. To verify that it is possible to drill down 
     *  from the MSC level summary information screen. 
     */
    // sync pass
    @Test
    public void DrillByMSCForSubscriberRankingDroppedCalls_6_13() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_DROPPED_VOICE);
        openEventAnalysis(subDroppedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.MSC);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.MSC, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedMSC));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.MSC, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.14
     *  Verify Subscriber ranking summary can support to view top 50 subscribers who experienced the most dropped call 
     *  in recent 30 minutes.
     */
    // sync pass
    @Test
    public void SubscriberRankingSummarySupportTop50AsDefault_6_14() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        waitForPageLoadingToComplete();
        assertTrue("The number of events is greater than 50", subBlockedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(subBlockedWindow);
        checkWindowUpdatedForTimeRanges("Subscriber", subBlockedWindow);
    }

    /**
     *  Test Case: 4.6.16
     *  Verify Subscriber ranking summary information is accuracy and the information 
     *  is sorted by the number of experienced blocked call.
     */
    // sync pass
    @Test
    public void SubscriberRankingSummaryAccuracyVerification_6_16() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        assertTrue("The number of events is greater than 50", subBlockedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(subBlockedWindow);
        //checkWindowUpdatedForTimeRanges("Subscriber", subBlockedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Subscriber", subBlockedWindow,
                expectedHeadersOnSubscriberRanking, GuiStringConstants.IMSI, DataIntegrityConstants.STATUS_BLOCKED,
                DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.17
     *  To verify it is possible to drill in to IMSI through the ENIQ Events GUI.
     */
    // sync pass
    @Test
    public void DrillByIMSIForSubscriberRankingBlockedCalls_6_17() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        openEventAnalysis(subBlockedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        //sortAllTableColumn(subscriberEventAnalysis);
        assertTrue("IMSI table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnEventAnalysis_MSO_MST_Failures));
        checkWindowUpdatedForTimeRanges("Failed", subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.18
     *  To verify that it is possible to drill into TAC level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. To verify that it is possible to drill down 
     *  from the TAC level summary information screen.
     */
    // sync pass
    @Test
    public void DrillByTACForSubscriberRankingBlockedCalls_6_18() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        openEventAnalysis(subBlockedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.TAC);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.TAC, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedTAC));
        
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, subscriberEventAnalysis);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String tac = firstRowData.get(GuiStringConstants.TAC);
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS + tac, AggregrationHandlerUtil.eventAnalysis(
                dataDisplayedOnUI, eventTypes, GuiStringConstants.TAC, tac, "NOT_SPECIFIC",
                expectedHeadersOnSelectedTAC, timeRange.getMiniutes()));
        
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.TAC, subscriberEventAnalysis);
        
    }

    /**
     *  Test Case: 4.6.19
     *  To verify that it is possible to drill into Access area level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. 
     */
    // sync pass
    @Test
    public void DrillByAccessAreaForSubscriberRankingBlockedCalls_6_19() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        openEventAnalysis(subBlockedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.ACCESS_AREA);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.ACCESS_AREA,
                subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnSelectedAccessArea));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, subscriberEventAnalysis);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String accessArea = firstRowData.get(GuiStringConstants.ACCESS_AREA);
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + accessArea,
                AggregrationHandlerUtil.eventAnalysis(dataDisplayedOnUI, eventTypes, GuiStringConstants.ACCESS_AREA,
                        accessArea, "NOT_SPECIFIC", expectedHeadersOnSelectedAccessArea, timeRange.getMiniutes()));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.20
     *  To verify that it is possible to drill into MSC level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, 
     *  success ratio and number of subscribers is displayed. To verify that it is possible to drill down 
     *  from the MSC level summary information screen. 
     */
    // sync pass
    @Test
    public void DrillByMSCForSubscriberRankingBlockedCalls_6_20() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        openEventAnalysis(subBlockedWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.MSC);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.MSC, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedMSC));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, subscriberEventAnalysis);
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String msc = firstRowData.get(GuiStringConstants.MSC);
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + msc, AggregrationHandlerUtil.eventAnalysis(
                dataDisplayedOnUI, eventTypes, GuiStringConstants.MSC, msc, "NOT_SPECIFIC",
                expectedHeadersOnSelectedMSC, timeRange.getMiniutes()));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.MSC, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.21
     *  Verify Access Area ranking summary can support to view top 50 subscribers 
     *  who experienced the most blocked calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void AccessAreaRankingSummarySupportTop50AsDefaultForBlockedCalls_6_21() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_ACCESS,
                RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_BLOCKED_VOICE);
        assertTrue("The number of events is greater than 50", accessAreaBlockedWindow.getTableRowCount() <= 50);
        assertEquals(accessAreaBlockedWindow.getTimeRange(), "30 minutes");
        sortAllTableColumn(accessAreaBlockedWindow);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, accessAreaBlockedWindow);
    }

    /**
     *  Test Case: 4.6.24
     *  To verify that the worst performing cells by failures are displayed.
     */
    // sync pass
    @Test
    public void ShowWorstPerformingAccessAreaByFailures_6_24() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_ACCESS,
                RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_DROPPED_VOICE);
        assertEquals(accessAreaDroppedWindow.getTimeRange(), "30 minutes");
        sortAllTableColumn(accessAreaDroppedWindow);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, accessAreaDroppedWindow);
    }

    /**
     *  Test Case: 4.6.25
     *  Verify Access Area ranking summary can support to view top 50 subscribers who experienced 
     *  the most dropped calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void AccessAreaRankingSummarySupportTop50AsDefaultForDroppedCalls_6_25() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_ACCESS,
                RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_DROPPED_VOICE);
        assertTrue("The number of events is greater than 50", accessAreaDroppedWindow.getTableRowCount() <= 50);
        assertEquals(accessAreaDroppedWindow.getTimeRange(), "30 minutes");
        sortAllTableColumn(accessAreaDroppedWindow);
        //checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, accessAreaDroppedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity(GuiStringConstants.ACCESS_AREA, accessAreaDroppedWindow,
                expectedHeadersOnAccessAreaRanking, GuiStringConstants.ACCESS_AREA,
                DataIntegrityConstants.STATUS_DROPPED, DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.28
     *  To verify that the worst performing cells by failures are displayed.
     */
    // sync pass
    @Test
    public void ShowWorstPerformingAccessAreaByFailures_6_28() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_ACCESS,
                RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_BLOCKED_VOICE);
        sortAllTableColumn(accessAreaBlockedWindow);
        assertEquals(accessAreaBlockedWindow.getTimeRange(), "30 minutes");
        //checkWindowUpdatedForTimeRanges(GuiStringConstants.ACCESS_AREA, accessAreaBlockedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Terminal", accessAreaBlockedWindow,
                expectedHeadersOnAccessAreaRanking, GuiStringConstants.ACCESS_AREA,
                DataIntegrityConstants.STATUS_BLOCKED, DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.29
     *  Verify a Controller ranking summary can support to view top 50 RNC Controller who experienced 
     *  the most blocked calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void RNCControllerRankingViewBlockedCalls_6_29() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_RNC,
                RankingsTab.SubStartMenu.RANKING_RNC_BLOCKED_VOICE);
        sortAllTableColumn(rncBlockedVoiceCallsRankingWindow);
        assertEquals(rncBlockedVoiceCallsRankingWindow.getTimeRange(), "30 minutes");
        final TimeRange time = getTimeRangeFromProperties();
        delayAndSetTimeRange(time, rncBlockedVoiceCallsRankingWindow);
        final int duration = time.getMiniutes();
        final List<Map<String, String>> completeUITableValues = rncBlockedVoiceCallsRankingWindow.getAllTableData();
        assertTrue(GuiStringConstants.RNC + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE,
                RankingAnalysis.rankingAnalysis(completeUITableValues, expectedHeadersOnRNCRanking,
                        GuiStringConstants.RNC, DataIntegrityConstants.STATUS_BLOCKED, duration));
        assertTrue("The number of events is greater than 50", rncBlockedVoiceCallsRankingWindow.getTableRowCount() <= 50);
    }

    /**
     *  Test Case: 4.6.30
     *  Verify a Controller ranking summary can support to view top 50 RNC Controller who experienced 
     *  the most dropped calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void RNCControllerRankingViewDroppedCalls_6_30() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_RNC,
                RankingsTab.SubStartMenu.RANKING_RNC_DROPPED_VOICE);
        sortAllTableColumn(rncDroppedVoiceCallsRankingWindow);
        assertEquals(rncDroppedVoiceCallsRankingWindow.getTimeRange(), "30 minutes");
        final TimeRange time = getTimeRangeFromProperties();
        delayAndSetTimeRange(time, rncDroppedVoiceCallsRankingWindow);
        final int duration = time.getMiniutes();
        final List<Map<String, String>> completeUITableValues = rncDroppedVoiceCallsRankingWindow.getAllTableData();
       
        assertTrue(GuiStringConstants.RNC + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE,
                RankingAnalysis.rankingAnalysis(completeUITableValues, expectedHeadersOnRNCRanking,
                        GuiStringConstants.RNC, DataIntegrityConstants.STATUS_DROPPED, duration));
        assertTrue("The number of events is greater than 50",
                rncDroppedVoiceCallsRankingWindow.getTableRowCount() <= 50);
       
    }

    /**
     *  Test Case: 4.6.31
     *  Verify a BSC Controller ranking summary can support to view top 50 RNC Controller who experienced 
     *  the most blocked calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void BSCControllerRankingViewBlockedCall_6_31() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_BSC,
                RankingsTab.SubStartMenu.RANKING_BSC_BLOCKED_VOICE);
        sortAllTableColumn(bscBlockedVoiceCallsRankingWindow);
        assertEquals(bscBlockedVoiceCallsRankingWindow.getTimeRange(), "30 minutes");
        final TimeRange time = getTimeRangeFromProperties();
        delayAndSetTimeRange(time, bscBlockedVoiceCallsRankingWindow);
        final int duration = time.getMiniutes();
        final List<Map<String, String>> completeUITableValues = bscBlockedVoiceCallsRankingWindow.getAllTableData();
        assertTrue(GuiStringConstants.BSC + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE,
                RankingAnalysis.rankingAnalysis(completeUITableValues, expectedHeadersOnBSCRanking,
                        GuiStringConstants.BSC, DataIntegrityConstants.STATUS_BLOCKED, duration));
        assertTrue("The number of events is greater than 50",
                bscBlockedVoiceCallsRankingWindow.getTableRowCount() <= 50);
        
    }

    /**
     *  Test Case: 4.6.32
     *  Verify a BSC Controller ranking summary can support to view top 50 RNC Controller who experienced 
     *  the most dropped calls in recent 30 minutes.
     */
    // sync pass
    @Test
    public void BSCControllerRankingViewDroppedCall_6_32() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_BSC,
                RankingsTab.SubStartMenu.RANKING_BSC_DROPPED_VOICE);
        sortAllTableColumn(bscDroppedVoiceCallsRankingWindow);
        assertEquals(bscDroppedVoiceCallsRankingWindow.getTimeRange(), "30 minutes");
        final TimeRange time = getTimeRangeFromProperties();
        delayAndSetTimeRange(time, bscDroppedVoiceCallsRankingWindow);
        final int duration = time.getMiniutes();
        final List<Map<String, String>> completeUITableValues = bscDroppedVoiceCallsRankingWindow.getAllTableData();
        assertTrue(GuiStringConstants.BSC + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE,
                RankingAnalysis.rankingAnalysis(completeUITableValues, expectedHeadersOnBSCRanking,
                        GuiStringConstants.BSC, DataIntegrityConstants.STATUS_DROPPED, duration));
        assertTrue("The number of events is greater than 50",
                bscDroppedVoiceCallsRankingWindow.getTableRowCount() <= 50);
    }

    /**
     *  Test Case: 4.6.34
     *  To verify that Terminal Ranking blocked calls summary can support to view top 50 Terminals who experienced 
     *  the most blocked calls
     */
    // sync pass
    @Test
    public void TerminalRankingViewBlockedCalls_6_34() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_TERMINAL,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_BLOCKED_VOICE);
        assertTrue("Table header is not right on terminal",
                termRankingsBlockedWindow.getTableHeaders().containsAll(expectedHeadersOnTerminalRanking));
        assertTrue("The number of events is greater than 50", termRankingsBlockedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(termRankingsBlockedWindow);
        //checkWindowUpdatedForTimeRanges("Terminal", termRankingsBlockedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Terminal", termRankingsBlockedWindow,
                expectedHeadersOnTerminalRanking, GuiStringConstants.TERMINAL_MAKE,
                DataIntegrityConstants.STATUS_BLOCKED, DataIntegrityConstants.RANKING_ANALYSIS);
    
       
    }

    /**
     *  Test Case: 4.6.35
     *  To verify that Terminal Ranking dropped calls summary can support to view top 50 Terminals who experienced 
     *  the most dropped calls
     */
    // sync pass
    @Test
    public void TerminalRankingViewDroppedCalls_6_35() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_TERMINAL,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_DROPPED_VOICE);
        assertTrue("Table header is not right on terminal",
                termRankingsDroppedWindow.getTableHeaders().containsAll(expectedHeadersOnTerminalRanking));
        assertTrue("The number of events is greater than 50", termRankingsDroppedWindow.getTableRowCount() <= 50);
        sortAllTableColumn(termRankingsDroppedWindow);
        //checkWindowUpdatedForTimeRanges("Terminal", termRankingsDroppedWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Terminal", termRankingsDroppedWindow,
                expectedHeadersOnTerminalRanking, GuiStringConstants.TERMINAL_MAKE,
                DataIntegrityConstants.STATUS_DROPPED, DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.36
     *  To verify that Long Duration Calls Ranking View summary will display the top 50 long duration calls 
     *  and what subscribers participated in the calls
     */
    // sync pass
    @Test
    public void RankingViewLongDurationCalls_6_36() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.DURATION_CALLS,
                RankingsTab.SubStartMenu.LONG_DURATION_CALLS_VOICE);
        assertTrue("Table header is not right on long duration", longDruationVoiceCallsRankingWindow.getTableHeaders()
                .containsAll(expectedHeadersOnDurationRanking));
        assertTrue("The number of events is greater than 50",
                longDruationVoiceCallsRankingWindow.getTableRowCount() <= 50);
        sortAllTableColumn(longDruationVoiceCallsRankingWindow);
        checkWindowUpdatedForTimeRanges("Terminal", longDruationVoiceCallsRankingWindow);
    }

    /**
     *  Test Case: 4.6.37
     *  To verify that Short Duration Calls Ranking View summary will display the top 50 short duration 
     *  calls and what subscribers participated in the calls
     */
    // sync pass
    @Test
    public void RankingViewShortDurationCalls_6_37() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.DURATION_CALLS,
                RankingsTab.SubStartMenu.SHORT_DURATION_CALLS_VOICE);
        assertTrue("Table header is not right on long duration", shortDruationVoiceCallsRankingWindow.getTableHeaders()
                .containsAll(expectedHeadersOnDurationRanking));
        assertTrue("The number of events is greater than 50",
                shortDruationVoiceCallsRankingWindow.getTableRowCount() <= 50);
        sortAllTableColumn(shortDruationVoiceCallsRankingWindow);
        checkWindowUpdatedForTimeRanges("Terminal", shortDruationVoiceCallsRankingWindow);
    }

    /**
     *  Test Case: 4.6.38
     *  To verify that it is possible to drill into Controller level summary information from an event listing. 
     *  To verify that data for total number events, number of failed events, number of successful events, success 
     *  ratio and number of subscribers is displayed. To verify that it is possible to drill down from the Controller 
     *  level summary information screen.
     */
    // sync pass
    @Test
    public void DrillByControllerForSubscriberRankingBlockedCalls_6_38() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        openEventAnalysis(subBlockedWindow, "IMSI", "IMSI");
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        drillDownNonFailuresOnEventAnalysisWindows(subscriberEventAnalysis, 0, GuiStringConstants.CONTROLLER);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.CONTROLLER, subscriberEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedController));
        drillDownAllFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING, GuiStringConstants.MSORIGINATINGSMSINMSC,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
        sortAllTableColumn(subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CONTROLLER, subscriberEventAnalysis);
    }

    /**
     *  Test Case: 4.6.39
     *  To Verify CS events shall be available in ranking engine application.
     */
    // sync pass
    @Test
    public void CSEventsRankingSubscriberRankingBlockedCalls_6_39() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_SUBSCRIBER,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_BLOCKED_VOICE);
        assertTrue("Table header is not right on subscriber block ranking", subBlockedWindow.getTableHeaders()
                .containsAll(expectedHeadersOnSubscriberRanking));
        sortAllTableColumn(subBlockedWindow);
        checkWindowUpdatedForTimeRanges("Subscriber", subBlockedWindow);
    }

    /**
     *  Test Case: 4.6.40
     *  Verify MSOriginating Unanswered Calls Ranking summary can support to view top 50 subscribers who 
     *  initiated the unanswered calls.
     */
    // sync pass
    @Test
    public void MsOriginatingUnansweredCallsRanking50AsDefault_6_40() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_UNANSWERED_CALLS,
                RankingsTab.SubStartMenu.RANKING_MS_ORIGINATING_UNANSWERED_CALLS_VOICE);
        //assertEquals(msOrigUnansweredCallsRankingWindow.getTableRowCount(), 50);
        sortAllTableColumn(msOrigUnansweredCallsRankingWindow);
        assertEquals(msOrigUnansweredCallsRankingWindow.getTimeRange(), "30 minutes");
        checkWindowUpdatedForTimeRanges("Unanswered", msOrigUnansweredCallsRankingWindow);
    }

    /**
     *  Test Case: 4.6.42
     *  Verify MSOriginating Unanswered calls ranking summary information is accurate and the information is sorted by the number of experienced
     *  unanswered calls.
     */
    // sync pass
    @Test
    public void MSOriginatingUnansweredcallsRankingSummaryAccuracyVerification_6_42() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_UNANSWERED_CALLS,
                RankingsTab.SubStartMenu.RANKING_MS_ORIGINATING_UNANSWERED_CALLS_VOICE);
        //assertEquals(msOrigUnansweredCallsRankingWindow.getTableRowCount(), 50);
        sortAllTableColumn(msOrigUnansweredCallsRankingWindow);
        assertTrue("Table header is not right on unanswered calls ranking", msOrigUnansweredCallsRankingWindow
                .getTableHeaders().containsAll(expectedHeadersOnUnansweredCallsRanking));
        assertEquals(msOrigUnansweredCallsRankingWindow.getTimeRange(), "30 minutes");
        //checkWindowUpdatedForTimeRanges("Unanswered", msOrigUnansweredCallsRankingWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Unanswered", msOrigUnansweredCallsRankingWindow,
                expectedHeadersOnUnansweredCallsRanking, GuiStringConstants.UNANSWERED_CALLS,
                GuiStringConstants.MSORIGINATING, DataIntegrityConstants.RANKING_ANALYSIS);

    }

    /**
     *  Test Case: 4.6.44
     *  Verify MSTerminating Unanswered Calls Ranking summary can support to view top 50 subscribers who 
     *  initiated the unanswered calls.
     */
    // sync pass
    @Test
    public void MsTerminatingUnansweredCallsRanking50AsDefault_6_44() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_UNANSWERED_CALLS,
                RankingsTab.SubStartMenu.RANKING_MS_TERMINATING_UNANSWERED_CALLS_VOICE);
        //assertEquals(msTermUnansweredCallsRankingWindow.getTableRowCount(), 50);
        sortAllTableColumn(msTermUnansweredCallsRankingWindow);
        assertEquals(msTermUnansweredCallsRankingWindow.getTimeRange(), "30 minutes");
        checkWindowUpdatedForTimeRanges("Unanswered", msTermUnansweredCallsRankingWindow);
    }

    /**
     *  Test Case: 4.6.46
     *  Verify MSTerminating Unanswered calls ranking summary information is accurate and the information is sorted by the number of experienced 
     *  unanswered calls.
     */
    // sync pass
    @Test
    public void MSTerminatingUnansweredCallsRankingSummaryAccuracyVerification_6_46() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_UNANSWERED_CALLS,
                RankingsTab.SubStartMenu.RANKING_MS_TERMINATING_UNANSWERED_CALLS_VOICE);
        assertTrue("The number of events is greater than 50",
                msTermUnansweredCallsRankingWindow.getTableRowCount() <= 50);
        sortAllTableColumn(msTermUnansweredCallsRankingWindow);
        assertTrue("Table header is not right on unanswered calls ranking", msTermUnansweredCallsRankingWindow
                .getTableHeaders().containsAll(expectedHeadersOnUnansweredCallsRanking));
        //checkWindowUpdatedForTimeRanges("Unanswered", msTermUnansweredCallsRankingWindow);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Unanswered", msTermUnansweredCallsRankingWindow,
                expectedHeadersOnUnansweredCallsRanking, GuiStringConstants.UNANSWERED_CALLS,
                GuiStringConstants.MSTERMINATING, DataIntegrityConstants.RANKING_ANALYSIS);
    }

    /**
     *  Test Case: 4.6.48
     *  Verify MSC ranking summary view and the information displayed is accurate for blocked calls.
     */
    // sync pass
    @Test
    public void VerifyMSCRankingBlockedCallsSummary_6_48() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_MSC,
                RankingsTab.SubStartMenu.RANKING_MSC_BLOCKED_VOICE);
        assertTrue("The header is not right on msc ranking window", mscBlockedRankingWindow.getTableHeaders()
                .containsAll(expectedHeadersOnMSCRanking));
        checkWindowUpdatedForTimeRangesWithDataIntegrity(GuiStringConstants.MSC, mscBlockedRankingWindow,
                expectedHeadersOnMSCRanking, GuiStringConstants.MSC, DataIntegrityConstants.STATUS_BLOCKED,
                DataIntegrityConstants.RANKING_ANALYSIS);
        openEventAnalysis(mscBlockedRankingWindow, GuiStringConstants.MSC, GuiStringConstants.MSC);
        assertTrue("Table header is not right on select MSC",
                networkEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnSelectedMSC));
        checkWindowUpdatedForTimeRanges(GuiStringConstants.MSC, networkEventAnalysis);
        drillDownAllFailuresOnEventAnalysisWindow(networkEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSORIGINATING,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.CALLFORWARDING,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
    }

    /**
     *  Test Case: 4.6.49
     *  Verify MSC ranking summary view and the information displayed is accurate for dropped calls.
     */
    // sync pass
    @Test
    public void VerifyMSCRankingDroppedCallsSummary_6_49() throws Exception {
        rankingsTab.openSubofSubStartMenu(RankingsTab.StartMenu.RANKINGS_MSC,
                RankingsTab.SubStartMenu.RANKING_MSC_DROPPED_VOICE);
        assertTrue("The header is not right on msc ranking window", mscDroppedRankingWindow.getTableHeaders()
                .containsAll(expectedHeadersOnMSCRanking));
        openEventAnalysis(mscDroppedRankingWindow, GuiStringConstants.MSC, GuiStringConstants.MSC);
        assertTrue("Table header is not right after clicking " + GuiStringConstants.MSC, networkEventAnalysis
                .getTableHeaders().containsAll(expectedHeadersOnSelectedMSC));
        //checkWindowUpdatedForTimeRanges(GuiStringConstants.MSC, networkEventAnalysis);
        checkWindowUpdatedForTimeRangesWithDataIntegrity(GuiStringConstants.MSC, mscDroppedRankingWindow,
                expectedHeadersOnMSCRanking, GuiStringConstants.MSC, DataIntegrityConstants.STATUS_DROPPED,
                DataIntegrityConstants.RANKING_ANALYSIS);
        drillDownAllFailuresOnEventAnalysisWindow(networkEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSORIGINATING,
                GuiStringConstants.MSTERMINATING, GuiStringConstants.CALLFORWARDING,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.LOCATIONSERVICES);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Check Ascending and Descending method for each column
     * 
     * @param window the object of CommonWindow
     * @throws PopUpException   
     */
    private void sortAllTableColumn(final CommonWindow window) throws PopUpException {
        final List<String> allTableHeader = window.getTableHeaders();
        for (final String columnName : allTableHeader) {
            window.sortTable(SortType.ASCENDING, columnName);
            waitForPageLoadingToComplete();
            window.sortTable(SortType.DESCENDING, columnName);
            waitForPageLoadingToComplete();
        }
    }

    /**
     * Check time range, the time ranges is read from properties file //ewandaf
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */
    private void checkWindowUpdatedForTimeRanges(final String networkType, final CommonWindow commonWindow)
            throws NoDataException, PopUpException {
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
            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog",
                    selenium.isTextPresent("Time Settings"));
        }
    }

    public void checkWindowUpdatedForTimeRangesWithDataIntegrity(final String networkType,
            final CommonWindow commonWindow, final List<String> defaultRankingHeader, final String field,
            final String analysisType, final String ValidationString) throws NoDataException, PopUpException {
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
                delayAndSetTimeRange(time, commonWindow);
                waitForPageLoadingToComplete();
                assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog",
                        selenium.isTextPresent("Time Settings"));
                if (!ValidationString.equalsIgnoreCase(DataIntegrityConstants.STATUS_NONE)) {
                    final int duration = time.getMiniutes();
                    final List<Map<String, String>> completeUITableValues = commonWindow.getAllTableData();
                    if (ValidationString.equalsIgnoreCase(DataIntegrityConstants.RANKING_ANALYSIS)) {
                        assertTrue(field + DataIntegrityConstants.RANKING_ANALYSIS_ERROR_MESSAGE,
                                RankingAnalysis.rankingAnalysis(completeUITableValues, defaultRankingHeader, field,
                                        analysisType, duration));
                    }
                }
            }
        }
    }

    //    /**
    //     * Check all the time ranges for event analysis window and only for event analysis window
    //     * 
    //     * @param commonWindow the object of CommonWindow
    //     * @param networkType the type of network. e.g. MSC, Controller, Access Area
    //     */
    //    private void checkEventAnalysisWindowIsUpdatedForAllTimeRanges(final String networkType, final CommonWindow commonWindow) throws PopUpException{
    //        assertTrue("Can't load " + networkType + " event analysis window", selenium.isTextPresent(networkType));
    //        assertTrue("Default time range is NOT equal to 30 minutes", commonWindow.getTimeRange().equals("30 minutes"));
    //
    //        for (final TimeRange time : TimeRange.values()) {
    //            commonWindow.setTimeRange(time);
    //            waitForPageLoadingToComplete();
    //            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium
    //                    .isTextPresent("Time Settings"));
    //        }
    //    }

    /**
     * Drill down one link to open the event analysis window
     * 
     * @param rankingWindow the object of CommonWindow
     * @param networkType the type of network. e.g. MSC, Controller, Access Area
     * @param columnName the name of column where the link is
     */
    private void openEventAnalysis(final CommonWindow rankingWindow, final String networkType, final String columnName)
            throws NoDataException, PopUpException {
        //Level 2
        rankingWindow.clickTableCell(0, rankingWindow.getTableHeaders().indexOf(columnName), "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networkType + " Event Analysis page",
                selenium.isTextPresent(" Event Analysis"));//delete networkType
    }

    /**
     * Drill down one link on Failure column on event analysis. 
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck This column to locate row number
     * @param values These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck,
            final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.FAILURES);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();
        assertTrue("Can't open Subscriber Event Analysis page", selenium.isTextPresent("Subscriber Event Analysis")); 
    }

    /**
     * Drill down one non failure link
     * 
     * @param window the object of CommonWindow
     * @param row row number
     * @param columnName the name of column where the link is
     */
    private void drillDownNonFailuresOnEventAnalysisWindows(final CommonWindow window, final int row,
            final String columnName) throws NoDataException, PopUpException {
        window.clickTableCell(row, columnName);
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + columnName + " Event Analysis page",
                selenium.isTextPresent("Subscriber Event Analysis"));
    }

    /**
     * Drill down All failure link
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */
    private void drillDownAllFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck,
            final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, GuiStringConstants.FAILURES);
        for (final String value : values) {
            if (window.getAllTableDataAtColumn(columnToCheck).contains(value)) {
                drillDownFailuresOnEventAnalysisWindow(window, columnToCheck, value);
                window.clickButton(SelectedButtonType.BACK_BUTTON);
                waitForPageLoadingToComplete();
            }
        }
    }

    /**
     * Set date and time in time dialog window with a lag of configurable time.
     * For example : By selecting 15 mins on EventAnalysis window, it will display
     *               data with a specific time lag. 
     *               i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30,
     *               but due to delay in updation of UI at current time, it fails to display all the expected data till 11:30.
     *               Therefore to display all the expected datathis function creates a time lag for example of 5 mins and 
     *               fetches data from 11:10 to 11:25.
     *               Similarly for all other time time ranges, it displays data with a time lag of 5 mins.
     * Note :        This time lag can be configurable from properties.               
     *  
     * @param TimeRange ex: 15mins, 30mins etc.
     * NOTE : Cannot set timeRange for 5 mins due to UI constraints.  
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

        final long currentTime = currentDate.getTime(); // gets time in milliseconds    	    	

        final int timeRangeInMins = timeRange.getMiniutes();

        long endDateTime = currentTime - (15 * 60 * 1000); // 15 minutes delay time - TODO this value should be configurable by adding to properties. 

        date.setTimeInMillis(endDateTime);
        Date endDate = date.getTime();

        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));

        // Since DateAndTime Range Dialog window in UI contains only divisible of 15 in time,
        // Therefore round-off the mins to divisibles of 15 and mins less than 15 will be rounded to 0. 
        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }

        if (timeRange == TimeRange.ONE_DAY) {
            endDateTime = endDateTime - (TimeRange.ONE_DAY.getMiniutes() * 60 * 1000);
        }
        date.setTimeInMillis(endDateTime);
        endDate = date.getTime();

        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);

        date.setTimeInMillis(startDateTime);
        final Date startDate = date.getTime();

        // Get the time in 12 hour format
        startDatefmt.format("%tl", startDate);
        String hourStartDate = startDatefmt.toString();
        int lengthOfHour = hourStartDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourStartDate = "0" + hourStartDate;
        }

        endDatefmt.format("%tl", endDate);
        String hourEndDate = endDatefmt.toString();
        lengthOfHour = hourEndDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourEndDate = "0" + hourEndDate;
        }

        // Concatinate AM/PM, HOUR and MIN to form a member variable of TimeCandidate i.e AM_HOUR_MIN
        final String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        final String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_"
                + minFormatter.format(endDate.getTime());

        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate
                + " " + startDateTimeCandidate + " and End Date Time Candidate : " + endDate + " "
                + endDateTimeCandidate);
        window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                TimeCandidates.valueOf(endDateTimeCandidate));
    }

}
