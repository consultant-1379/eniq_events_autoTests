/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab.StartMenu;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author ekeviry
 * @since 2011
 * 
 */
public class TestGroupFiveTerminalAPNeCell extends BaseFourGTest {

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("termRankings")
    private CommonWindow termRankingsWindow;

    @Autowired
    @Qualifier("cellRankings")
    private CommonWindow cellRankingsWindow;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysisWindow;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow apnRankingsWindow;

    //    @BeforeClass
    //    public static void openLog() {
    //        logger.log(Level.INFO, "Start of Ranking test section");
    //    }
    //
    //    @AfterClass
    //    public static void closeLog() {
    //        logger.log(Level.INFO, "End of Ranking test section");
    //    }

    //    @Override
    //    @Before
    //    public void setUp() {
    //        super.setUp();
    //        rankingsTab.openTab();
    //    }

    ArrayList<String> eventTypeUI = new ArrayList<String>();

    IMSIDataFromUI dataFromUI = new IMSIDataFromUI();

    // final String[] specifiedTerminalRankingHeaders = { "TAC", "Failures" };

    //    final String[] specifiedAPNRankingHeaders = { "APN", "Failures", "Successes" };
    //
    //    final String[] specifiedECellRankingHeaders = { "Access Area", "Failures", "Successes" };

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.13: Verify 4G data is
     * displayed in the Terminal ranking view for L_ATTACH event Purpose: To
     * verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLATTACHEvent_5_13() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_ATTACH");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.14: Verify 4G data is
     * displayed in the APN ranking view for L_ATTACH event Purpose: To verify
     * that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLATTACHEvent_5_14() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_ATTACH", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.15: Verify 4G data is
     * displayed in the APN ranking view for L_ATTACH event Purpose: To verify
     * that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLATTACHEvent_5_15() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_ATTACH", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.17: Verify 4G data is
     * displayed in the Terminal ranking view for L_DETACH event Purpose: To
     * verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLDETACHEvent_5_17() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_DETACH");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.18: Verify 4G data is
     * displayed in the APN ranking view for L_DETACH event Purpose: To verify
     * that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLDETACHEvent_5_18() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DETACH", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.19: Verify 4G data is
     * displayed in the APN ranking view for L_DETACH event Purpose: To verify
     * that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLDETACHEvent_5_19() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DETACH", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.21: Verify 4G data is
     * displayed in the Terminal ranking view for L_SERVICE_REQUEST event
     * Purpose: To verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLSERVICEREQUESTEvent_5_21() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_SERVICE_REQUEST");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.22: Verify 4G data is
     * displayed in the APN ranking view for L_SERVICE_REQUEST event Purpose: To
     * verify that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLSERVICEREQUESTEvent_5_22() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_SERVICE_REQUEST", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.23: Verify 4G data is
     * displayed in the APN ranking view for L_SERVICE_REQUEST event Purpose: To
     * verify that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLSERVICEREQUESTEvent_5_23() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_SERVICE_REQUEST", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.25: Verify 4G data is
     * displayed in the Terminal ranking view for L_HANDOVER event Purpose: To
     * verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLHANDOVEREvent_5_25() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_HANDOVER");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.26: Verify 4G data is
     * displayed in the APN ranking view for L_HANDOVER event Purpose: To verify
     * that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLHANDOVEREvent_5_26() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_HANDOVER", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.27: Verify 4G data is
     * displayed in the APN ranking view for L_HANDOVER event Purpose: To verify
     * that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLHANDOVEREvent_5_27() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_HANDOVER", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.29: Verify 4G data is
     * displayed in the Terminal ranking view for L_TAU event Purpose: To verify
     * that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLTAUEvent_5_29() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_TAU");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.30: Verify 4G data is
     * displayed in the APN ranking view for L_TAU event Purpose: To verify that
     * 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLTAUEvent_5_30() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_TAU", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.31: Verify 4G data is
     * displayed in the APN ranking view for L_TAU event Purpose: To verify that
     * 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLTAUEvent_5_31() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_TAU", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.33: Verify 4G data is
     * displayed in the Terminal ranking view for L_PDN_CONNECT event Purpose:
     * To verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLPDNCONNECTEvent_5_33() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_PDN_CONNECT");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.34: Verify 4G data is
     * displayed in the APN ranking view for L_PDN_CONNECT event Purpose: To
     * verify that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLPDNCONNECTEvent_5_34() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_PDN_CONNECT", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.35: Verify 4G data is
     * displayed in the APN ranking view for L_PDN_CONNECT event Purpose: To
     * verify that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLPDNCONNECTEvent_5_35() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_PDN_CONNECT", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.37: Verify 4G data is
     * displayed in the Terminal ranking view for L_PDN_DISCONNECT event
     * Purpose: To verify that 4G data is displayed in the Terminal ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLPDNDISCONNECTEvent_5_37() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_PDN_DISCONNECT");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.38: Verify 4G data is
     * displayed in the APN ranking view for L_PDN_DISCONNECT event Purpose: To
     * verify that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLPDNDISCONNECTEvent_5_38() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_PDN_DISCONNECT", apnRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.39: Verify 4G data is
     * displayed in the APN ranking view for L_PDN_DISCONNECT event Purpose: To
     * verify that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLPDNDISCONNECTEvent_5_39() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_PDN_DISCONNECT", cellRankingsWindow, networkEventAnalysisWindow,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.41: Verify 4G data is
     * displayed in the Terminal ranking view for L_DEDICATED_BEARER_ACTIVATE
     * event Purpose: To verify that 4G data is displayed in the Terminal
     * ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLDEDICATEDBEARERACTIVATEEvent_5_41() throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_DEDICATED_BEARER_ACTIVATE");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.42: Verify 4G data is
     * displayed in the APN ranking view for L_DEDICATED_BEARER_ACTIVATE event
     * Purpose: To verify that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForL_DEDICATEDE_BEARER_ACTIVATEEvent_5_42() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DEDICATED_BEARER_ACTIVATE", apnRankingsWindow,
                networkEventAnalysisWindow, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.27: Verify 4G data is
     * displayed in the APN ranking view for L_DEDICATED_BEARER_ACTIVATE event
     * Purpose: To verify that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLDEDICATEDBEARERACTIVATEEvent_5_43() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DEDICATED_BEARER_ACTIVATE", cellRankingsWindow,
                networkEventAnalysisWindow, RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.45: Verify 4G data is
     * displayed in the Terminal ranking view for L_DEDICATED_BEARER_DEACTIVATE
     * event Purpose: To verify that 4G data is displayed in the Terminal
     * ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheTerminalRankingViewForLDEDICATEDBEARERDEACTIVATEEvent_5_45()
            throws Exception {

        verify4GDataIsDisplayedInTheTerminalRankingView("L_DEDICATED_BEARER_DEACTIVATE");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.46: Verify 4G data is
     * displayed in the APN ranking view for L_DEDICATED_BEARER_DEACTIVATE event
     * Purpose: To verify that 4G data is displayed in the APN ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheAPNRankingViewForLDEDICATEDBEARERDEACTIVATEEvent_5_46() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DEDICATED_BEARER_DEACTIVATE", apnRankingsWindow,
                networkEventAnalysisWindow, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.47: Verify 4G data is
     * displayed in the APN ranking view for L_DEDICATED_BEARER_DEACTIVATE event
     * Purpose: To verify that 4G data is displayed in the eCell ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheECellRankingViewForLLDEDICATEDBEARERDEACTIVATEEvent_5_47() throws Exception {

        verify4GDataIsDisplayedInTheAPNRankingView("L_DEDICATED_BEARER_DEACTIVATE", cellRankingsWindow,
                networkEventAnalysisWindow, RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
    }

    // //////////////////////////////////////////////////////
    // PRIVATE METHODS
    // //////////////////////////////////////////////////////

    private void verify4GDataIsDisplayedInTheTerminalRankingView(final String eventType) throws PopUpException,
            FileNotFoundException, IOException, NoDataException, ParseException {

        terminalTab.openTab();

        terminalTab.openSubStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS);
        waitForPageLoadingToComplete();
        pause(4000);

        //        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.RANKINGS_TERMINAL);
        //        waitForPageLoadingToComplete();

        final String propFileTimeRange = getUITimeRangeFromPropertiesFile(termRankingsWindow);
        waitForPageLoadingToComplete();

        final int[] numberOfEvents = getNumberOfEventsTerminal(termRankingsWindow);

        dataFromUI.verifyEventTypeIsPresent(eventType, termRankingsWindow);

        verifyTemporalReferenceIsMaintained(termRankingsWindow, propFileTimeRange);

        verifyNumberOfEventsTerminal(numberOfEvents, termRankingsWindow);
        pause(1000);

    }

    private void verify4GDataIsDisplayedInTheAPNRankingView(final String eventType, final CommonWindow rankingsWin,
            final CommonWindow eventAnalysisWindow, final RankingsTab.SubStartMenu tabType) throws PopUpException,
            FileNotFoundException, IOException, NoDataException, ParseException {

        // ****Need to switch from Rankings Tab to Network Tab****

        //  networkTab.openTab();

        System.out.println("Tab Type: " + tabType);
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(StartMenu.EVENT_RANKING, tabType);
        waitForPageLoadingToComplete();

        final String propFileTimeRange = getUITimeRangeFromPropertiesFile(rankingsWin);
        waitForPageLoadingToComplete();

        final int[] numberOfEvents = getNumberOfEventsAPNOrECell(rankingsWin, eventAnalysisWindow);

        dataFromUI.verifyEventTypeIsPresent(eventType, eventAnalysisWindow);

        verifyTemporalReferenceIsMaintained(eventAnalysisWindow, propFileTimeRange);

        verifyNumberOfEventsAPN(numberOfEvents, eventAnalysisWindow);
        pause(1000);
    }

    private int[] getNumberOfEventsTerminal(final CommonWindow rankingsWin) throws NoDataException,
            FileNotFoundException, IOException, ParseException, PopUpException {

        final int[] columnNumbers = getColumnNumbers(rankingsWin, specifiedTerminalRankingHeaders);
        final int tacColumn = columnNumbers[0];
        final int failuresColumn = columnNumbers[1];

        final String rankingTopTAC = rankingsWin.getTableData(0, tacColumn);
        System.out.println("Terminal Ranking TAC: " + rankingTopTAC);

        final int rankingWindowTotal = getNumberFailuresFromRankingsWindow(rankingsWin, columnNumbers);
        System.out.println("Terminal Ranking Window Total: " + rankingWindowTotal);

        rankingsWin.clickTableCell(0, failuresColumn, "gridCellLink");

        waitForPageLoadingToComplete();

        final int eventAnalysisEventCount = rankingsWin.getTableRowCount();
        System.out.println("Terminal Event Analysis Window Row Count: " + eventAnalysisEventCount);

        // Use properties file time range
        final int numberOfEventsDB = IMSIDataFromDatabase.getNumberOfEventsForGivenTAC(rankingTopTAC);
        System.out.println("Number of events in DB: " + numberOfEventsDB);

        final int[] numberOfEvents = { rankingWindowTotal, eventAnalysisEventCount, numberOfEventsDB };

        return numberOfEvents;
    }

    private int[] getNumberOfEventsAPNOrECell(final CommonWindow rankingsWin, final CommonWindow eventAnalysisWindow)
            throws NoDataException, FileNotFoundException, IOException, ParseException, PopUpException {

        String[] specifiedRankingHeaders = null;

        if (rankingsWin.getWindowHeaderLabel().equals("Access Area Ranking")) {
            specifiedRankingHeaders = specifiedECellRankingHeaders;
        }

        else {
            specifiedRankingHeaders = specifiedAPNRankingHeaders;
        }

        System.out.println("Rankings Window: " + rankingsWin.getWindowHeaderLabel());

        int[] columnNumbers = getColumnNumbers(rankingsWin, specifiedRankingHeaders);
        final int apnOrECellColumn = columnNumbers[0];
        final int failuresColumn = columnNumbers[1];
        final int successesColumn = columnNumbers[2];

        final String rankingTopAPN = rankingsWin.getTableData(0, apnOrECellColumn);
        System.out.println("Ranking APN: " + rankingTopAPN);

        final int rankingWindowTotal = getTotalSuccessesAndFailuresRankingWindow(rankingsWin, columnNumbers);
        System.out.println("Ranking Window Total: " + rankingWindowTotal);

        rankingsWin.clickTableCell(0, apnOrECellColumn, "gridCellLauncherLink");

        waitForPageLoadingToComplete();

        columnNumbers = getColumnNumbers(eventAnalysisWindow, specifiedRankingHeaders);

        int totalSuccessesAndFailuresEventAnalysisWindow = 0;

        for (int i = 0; i < eventAnalysisWindow.getTableRowCount(); i++) {
            final String failures = eventAnalysisWindow.getTableData(i, failuresColumn);
            final String successes = eventAnalysisWindow.getTableData(i, successesColumn);

            totalSuccessesAndFailuresEventAnalysisWindow = totalSuccessesAndFailuresEventAnalysisWindow
                    + Integer.parseInt(failures);
            totalSuccessesAndFailuresEventAnalysisWindow = totalSuccessesAndFailuresEventAnalysisWindow
                    + Integer.parseInt(successes);
        }

        System.out.println("Event Analysis Window Total: " + totalSuccessesAndFailuresEventAnalysisWindow);

        // Use properties file time range
        // final int numberOfEventsDB = IMSIDataFromDatabase
        // .getNumberOfEventsForGivenTAC(rankingTopAPN);
        // System.out.println("Number of events in DB: " + numberOfEventsDB);

        final int[] numberOfEvents = { rankingWindowTotal, totalSuccessesAndFailuresEventAnalysisWindow };

        return numberOfEvents;
    }

    private void verifyNumberOfEventsTerminal(final int[] numberOfEvents, final CommonWindow rankingsWin)
            throws NoDataException {

        final int[] columnNumbers = getColumnNumbers(rankingsWin, specifiedTerminalRankingHeaders);
        final int tacColumn = columnNumbers[0];

        final String rankingTopTAC = rankingsWin.getTableData(0, tacColumn);

        final int rankingWindowTotal = numberOfEvents[0];
        final int eventAnalysisEventCount = numberOfEvents[1];
        final int numberOfEventsDB = numberOfEvents[2];

        if (rankingWindowTotal != eventAnalysisEventCount || numberOfEventsDB != eventAnalysisEventCount) {
            logger.log(Level.SEVERE, "Number of events - Ranking Window: " + rankingWindowTotal
                    + "\nNumber of events - Event Analysis Window: " + eventAnalysisEventCount
                    + "\nNumber of events - Database Raw Table: " + numberOfEventsDB);
        }

        assertTrue("Differing number of events for TAC " + rankingTopTAC, rankingWindowTotal == eventAnalysisEventCount);
        assertTrue("Differing number of events for TAC " + rankingTopTAC, numberOfEventsDB == eventAnalysisEventCount);
    }

    private void verifyNumberOfEventsAPN(final int[] numberOfEvents, final CommonWindow eventAnalysisWindow)
            throws NoDataException {

        final int[] columnNumbers = getColumnNumbers(eventAnalysisWindow, specifiedAPNRankingHeaders);
        final int apnColumn = columnNumbers[0];

        final String rankingTopAPN = eventAnalysisWindow.getTableData(0, apnColumn);

        final int rankingWindowTotal = numberOfEvents[0];
        final int eventAnalysisEventCount = numberOfEvents[1];

        if (rankingWindowTotal != eventAnalysisEventCount) {
            logger.log(Level.SEVERE, "Number of events - Ranking Window: " + rankingWindowTotal
                    + "\nNumber of events - Event Analysis Window: " + eventAnalysisEventCount);
        }

        assertTrue("Differing number of events for APN " + rankingTopAPN, rankingWindowTotal == eventAnalysisEventCount);
    }

    private int getNumberFailuresFromRankingsWindow(final CommonWindow rankingsWin, final int[] columnNumbers)
            throws NoDataException {

        final int failuresColumn = columnNumbers[1];
        final String rankingFailuresString = rankingsWin.getTableData(0, failuresColumn);

        final int rankingFailures = Integer.parseInt(rankingFailuresString);

        return rankingFailures;
    }

    private int getTotalSuccessesAndFailuresRankingWindow(final CommonWindow rankingsWin, final int[] columnNumbers)
            throws NoDataException {

        final int failuresColumn = columnNumbers[1];
        final int successesColumn = columnNumbers[2];

        final String rankingFailuresString = rankingsWin.getTableData(0, failuresColumn);
        final String rankingSuccessesString = rankingsWin.getTableData(0, successesColumn);

        final int rankingFailures = Integer.parseInt(rankingFailuresString);
        final int rankingSuccesses = Integer.parseInt(rankingSuccessesString);

        final int totalFailuresAndSuccesses = rankingFailures + rankingSuccesses;

        return totalFailuresAndSuccesses;
    }

    private void verifyTemporalReferenceIsMaintained(final CommonWindow currentWindow, final String propFileTimeRange) {

        final String eventAnalysisTimeRange = currentWindow.getTimeRange();

        if (!propFileTimeRange.equals(eventAnalysisTimeRange)) {
            logger.log(Level.SEVERE, "Temporal reference not maintained - " + "\n Properties File Time Range: "
                    + propFileTimeRange + "\nEvent Analysis Window Time Range: " + eventAnalysisTimeRange);
        }

        System.out.println("Prop File Time Range / Failure Event Analysis Time Range: " + propFileTimeRange + " / "
                + eventAnalysisTimeRange);

        assertTrue("Selected temporal reference not maintained", propFileTimeRange.equals(eventAnalysisTimeRange));
    }
}
