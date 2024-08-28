package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.BaseWcdmaCfa;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Updated/refactored by efreass based on VS_ENIQ_Events12_2_WCDMA_HFA_2.2.14_SERVER2012
 * 23 july 2012 
 */
public class SubscriberTestGroupWcdmaHfa extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberEventAnalysisForeWCDMAHFA")
    private CommonWindow subscriberWCDMAHFAEventAnalysis;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

     private final static String EXPECTED_HEADER = " EXPECTED HEADERS : ";

    private final static String WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    private final static String IMSI_VALUE = "460010000000001";

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "\n START OF SUBSCRIBER SESSION TESTS SECTION ");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "\n END OF SUBSCRIBER SESSION TESTS SECTION ");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
    }

     //EE12.2_WHFA_5.1; Subscriber Handover Failure Analysis – IMSI based 
    @Test
    public void Subscriber_Handover_Failure_Analysis_5_1() throws Exception {
    	subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
       basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberHFAWindow,
                GuiStringConstants.HANDOVER_TYPE);
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(subscriberWCDMAHFAEventAnalysis, timeRange);
    }

    //EE12.2_WHFA_5.1a; Drill down from Subscriber HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void Drilldown_Subscriber_Handover_Failure_Analysis_To_EventAnalysis_SOHO_5_1a() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
      //  subscriberWCDMAHFAEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow));
    }

    //EE12.2_WHFA_5.1b; Drill down from Subscriber HO failure totals to Detailed Event Analysis (IFHO).
    @Test
    public void Drilldown_Subscriber_Handover_Failure_Analysis_To_EventAnalysis_IFHO_5_1b() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow));
    }

    //EE12.2_WHFA_5.1c; Drill down from Subscriber HO failure totals to Detailed Event Analysis (IRAT).
    @Test
    public void Drilldown_Subscriber_Handover_Failure_Analysis_To_EventAnalysis_IRAT_5_1c() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
        subscriberWCDMAHFAEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow));
    }

    //EE12.2_WHFA_5.1d; Drill down from Subscriber HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void Drilldown_Subscriber_Handover_Failure_Analysis_To_EventAnalysis_HSDSCH_5_1d() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
        subscriberWCDMAHFAEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow));
    }

     //EE12.2_WHFA_5.2; Subscriber Handover Failure Analysis – MSISDN based 
    @Test
    public void MSISDN_Handover_Failure_Analysis_5_2() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
        basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberHFAWindow,
                GuiStringConstants.HANDOVER_TYPE);
    }

    //EE12.2_WHFA_5.3; Subscriber Handover Failure Analysis – PTMSI based 
    @Test
    public void PTMSI_Handover_Failure_Analysis_5_3() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI, true, IMSI_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberHFAWindow));
        basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberHFAWindow,
                GuiStringConstants.HANDOVER_TYPE);
    }

    //EE12.2_WHFA_5.4; Subscriber Group Handover Failure Analysis
    @Test
    public void Subscriber_Group_Handover_Failure_Analysis_5_4() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI_GROUP, true, BaseWcdmaCfa.IMSI_GROUP_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));
        basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberGroupHFAWindow,
                GuiStringConstants.HANDOVER_TYPE);
    }

     //EE12.2_WHFA_5.4a; Drilldown to Subscriber Group Handover Failure Event Analysis from summary pane (SOHO)
    @Test
    public void Subscriber_Group_Handover_Failure_Analysis_Drilldown_SOHO_5_4a() throws Exception {
         subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI_GROUP, true, BaseWcdmaCfa.IMSI_GROUP_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));
        /*no data for 1 week so need to remove this for now or hack into basicWindowFunctionalityCheck timerange*/
          basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberGroupHFAWindow,
                GuiStringConstants.HANDOVER_TYPE); 
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI_GROUP, GuiStringConstants.IMSI_GROUP, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupSubscriberWCDMASOHOWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupSubscriberWCDMASOHOWindow));
    }

    //EE12.2_WHFA_5.4b; Drilldown to Subscriber Group Handover Failure Event Analysis from summary pane (IFHO)
    @Test
    public void Subscriber_Group_Handover_Failure_Analysis_Drilldown_IFHO_5_4b() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI_GROUP, true, BaseWcdmaCfa.IMSI_GROUP_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));
        /*no data for 1 week so need to remove this for now or hack into basicWindowFunctionalityCheck timerange*/
          basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberGroupHFAWindow,
                GuiStringConstants.HANDOVER_TYPE); 
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI_GROUP, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow));
    }

    //EE12.2_WHFA_5.4c; Drilldown to Subscriber Group Handover Failure Event Analysis from summary pane (IRAT)
    @Test
    public void Subscriber_Group_Handover_Failure_Analysis_Drilldown_IRAT_5_4c() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI_GROUP, true, BaseWcdmaCfa.IMSI_GROUP_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));
        /*no data for 1 week so need to remove this for now or hack into basicWindowFunctionalityCheck timerange*/
         basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberGroupHFAWindow,
                GuiStringConstants.HANDOVER_TYPE); 
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI_GROUP, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow));
    }

     //EE12.2_WHFA_5.4d; Drilldown to Subscriber Group Handover Failure Event Analysis from summary pane (HSDSCH)
    @Test
    public void Subscriber_Group_Handover_Failure_Analysis_Drilldown_HSDSCH_5_4d() throws Exception {
        subscriberTab.openEventAnalysisWindowForWCDMAHFA(ImsiMenu.IMSI_GROUP, true, BaseWcdmaCfa.IMSI_GROUP_VALUE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));
        /*no data for 1 week so need to remove this for now or hack into basicWindowFunctionalityCheck timerange*/
        basicWindowFunctionalityCheck(subscriberWCDMAHFAEventAnalysis, BaseWcdmaHfa.completeHeadersOnSubscriberGroupHFAWindow,
                GuiStringConstants.HANDOVER_TYPE); 
        subscriberWCDMAHFAEventAnalysis.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, subscriberWCDMAHFAEventAnalysis,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IMSI_GROUP, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders = subscriberWCDMAHFAEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow));
    }

    /* This method is to do the basic check on the windows
     * 
     *  @param commonWindow - The window id
     *  @param optionalheader - Optional headers on the window 
     */
    private void basicWindowFunctionalityCheck(final CommonWindow commonWindow,
            final List<String> completeHeadersOnSubscriberHFAWindow, final String optionalheader) throws PopUpException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
        TimeRange[] timeRanges;
        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
            for (final TimeRange timeRange : timeRanges) {
                commonWindow.setTimeRange(timeRange);
                commonWindow.checkInOptionalHeaderCheckBoxes(
                        getCompleteHeaderList(commonWindow, completeHeadersOnSubscriberHFAWindow), optionalheader);
                commonWindow.checkInOptionalHeaderCheckBoxes(
                        getCompleteHeaderList(commonWindow, completeHeadersOnSubscriberHFAWindow), optionalheader);
                commonWindow.maximizeWindow();
                commonWindow.minimizeWindow();
                commonWindow.restoreWindow();
                commonWindow.refresh();
                final int pageCount = commonWindow.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    commonWindow.clickNextPage();
                }
            }
        }
    }

    /* This method is to get the headers other than the table headers
     * seen. 
     */
    private List<String> getCompleteHeaderList(final CommonWindow commonWindow,
            final List<String> completeHeadersOnSubscriberHFAWindow) {
        final List<String> headers = commonWindow.getTableHeaders();
        final List<String> resultantList = new ArrayList<String>();
        for (final String head : completeHeadersOnSubscriberHFAWindow) {
            if (!headers.contains(head)) {
                resultantList.add(head);
            }
        }
        return resultantList;
    }

    /**
     * Drill down one link on Failure column on event analysis. 
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck This column to locate row number
     * @param testType i.e. IMSI or IMSI group
     * @param values These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownOnParticularCell(final String drillDownHeader, final CommonWindow window,
            final String columnToCheck, final String testType, final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, columnToCheck);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        
        // emosjil: added expectedWindowTitle string constant
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();

        if(version >= 13.0){
        	if(testType.equals(GuiStringConstants.IMSI)){
        		expectedWindowTitle = IMSI_VALUE + GuiStringConstants.DASH + GuiStringConstants.IMSI
        				+ GuiStringConstants.DASH + GuiStringConstants.WCDMA  + GuiStringConstants.SPACE + "Handover Failure Event Analysis";
        	}else{
        		expectedWindowTitle = BaseWcdmaHfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
        				+ GuiStringConstants.DASH + GuiStringConstants.WCDMA  + GuiStringConstants.SPACE + "Handover Failure Event Analysis";  
        	}
        }else{
        	if(testType.equals(GuiStringConstants.IMSI)){
        		expectedWindowTitle = IMSI_VALUE + GuiStringConstants.DASH + GuiStringConstants.IMSI
        				+ GuiStringConstants.DASH + "Failed Event Analysis";
        	}else{
        		expectedWindowTitle = BaseWcdmaHfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
        				+ GuiStringConstants.DASH + "Failed Event Analysis";
        	}
        }
        
        window.clickTableCell(row, drillDownHeader);
        waitForPageLoadingToComplete();
        
        // enosjil: modified
        // assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent(expectedWindowTitle));
    }

}
