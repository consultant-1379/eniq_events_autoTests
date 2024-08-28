/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author ericker
 * @since 2010
 *
 */
public class RankingTestGroup extends EniqEventsUIBaseSeleniumTest {

    public enum ColumnNames {
        CONTROLLER("Controller"), TAC("TAC"), SGSN("SGSN-MME"), ACCESS_AREA("Access Area");//, APN("APN");
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
    @Qualifier("bscRankings")
    private CommonWindow bscRankingsWindow;

    @Autowired
    @Qualifier("rncRankings")
    private CommonWindow rncRankingsWindow;

    @Autowired
    @Qualifier("subRankings")
    private CommonWindow subRankingsWindow;

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysisWindow;

    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysisWindow;

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

    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

    @Autowired
    @Qualifier("causeCodeRankings")
    private CommonWindow causeCodeRankingsWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Ranking test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Ranking test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        rankingsTab.openTab();
    }

    /**
     *  105 65-0528/00317  
     *          The following time intervals shall be selectable within the user interface for ranking : 1 minute,
     *          5 minute, 15 minute, 30 minute, 1 hour, 2 hours, 6 hours, 1 day, 1 week. If aggregate data is 
     *          used, it needs to be retained to cater for the longest rank function.
     *          Test Case - 4.7.2
     */
    @Test
    public void temporalReferencesForRanking_4_7_2() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_BSC);
        checkRankingWindowIsUpdatedForAllTimeRanges("BSC", bscRankingsWindow);
    }

    /**
     *  105 65-0528/00318
     *          It shall be possible to view the top XX subscribers who are experiencing the most event failures.
     *          XX is a configurable parameter, the default shall be 50. The default temporal aggregation for this
     *          view shall be 30 minutes.
     *          Test Case - 4.7.3
     */
    @Test
    public void showWorstPerformingSubscribersByFailures_4_7_3() throws Exception {
       selenium.setSpeed("1");
    	rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER);
        selenium.click("//*[@id='SUBSCRIBER_RANKING_CORE']");
        assertTrue("Can't open SubscriberRanking page", selenium.isTextPresent("Subscriber Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("Subscriber", subRankingsWindow);
    }

    /**
     *  105 65-0528/00319
     *          It shall be possible to select a subscriber from the subscriber ranking view and view all their
     *          events. The time interval used in the ranking shall be used to show all the relevant events.
     *          Test Case - 4.7.4
     */
    @Test
    public void drillBySubscriberToViewTheEventsForSameTemporalReference_4_7_4() throws Exception {
        //Level 1
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER);
        selenium.click("//*[@id='SUBSCRIBER_RANKING_CORE']");
        assertTrue("Can't open SubscriberRanking page", selenium.isTextPresent("Subscriber Ranking"));

        openEventAnalysis(subRankingsWindow, "IMSI", "IMSI");
        drillAllLinkOnFailedEventAnalysis(subscriberEventAnalysisWindow, "IMSI");
    }

    /**
     *  105 65-0528/00321
     *          It shall be possible to view the top XX handsets which are experiencing the most event failures.
     *          XX is a configurable parameter, the default shall be 50. The default temporal aggregation for this
     *          view shall be 30 minutes.
     *          Test Case - 4.7.6
     */
    @Test
    public void showWorstPerformingDevicesByFailures_4_7_6() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL);
        selenium.click("//*[@id='TERMINAL_RANKINGS']");
        checkRankingWindowIsUpdatedForAllTimeRanges("Terminal", termRankingsWindow);
    }

    /**
     *  105 65-0528/00322
     *          It shall be possible to select a handset make/model from the IMEI TAC ranking view and view the
     *          associated events. The temporal reference using in the ranking shall be used to show all the
     *          relevant events.
     *          Test Case - 4.7.7
     */
    @Test
    public void drillByHandsetMakeModelToViewEventsForSameTemporalReference_4_7_7() throws Exception {
        //Level 1
    	selenium.setSpeed("500");
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL);
        selenium.click("//*[@id='TERMINAL_RANKINGS']");
        assertTrue("Can't open Cell Ranking page", selenium.isTextPresent("Terminal Ranking"));
        
        openEventAnalysis(termRankingsWindow, "Terminal", "Model");
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "SERVICE_REQUEST", "ISRAU", "L_PDN_DISCONNECT", "L_SERVICE_REQUEST","L_TAU");
        drillAllLinkOnFailedEventAnalysis(terminalEventAnalysisWindow, "Terminal");
    }

    /**
     *  105 65-0528/00323
     *          It shall be possible to select a handset make from the IMEI TAC ranking view and view the
     *          associated models events. The individual models shall be drillable. The temporal reference using in
     *          the ranking shall be used to show all the relevant events.
     *          Test Case - 4.7.8
     */
    @Test
    public void drillByHandsetMakeToViewTheAssociatedModelsEventsForSameTemporalReference_4_7_8() throws Exception {
        //Level 1
    	selenium.setSpeed("100");
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL);
        selenium.click("//*[@id='TERMINAL_RANKINGS']");
        assertTrue("Can't open Cell Ranking page", selenium.isTextPresent("Terminal Ranking"));

        System.out.println("1");
        openEventAnalysis(termRankingsWindow, "Terminal", "Manufacturer");
        System.out.println("2");
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "SERVICE_REQUEST", "ISRAU", "L_PDN_DISCONNECT", "L_SERVICE_REQUEST","L_TAU");
        System.out.println("3");
        drillAllLinkOnFailedEventAnalysis(terminalEventAnalysisWindow, "Terminal");
    }

    /**
     *  105 65-0528/00324
     *          It shall be possible to view the top XX cells which are experiencing the most event failures. XX
     *          is a configurable parameter, the default shall be 50. The default temporal aggregation for this
     *          view shall be 30 minutes.
     *          Test Case - 4.7.9
     */
    @Test
    public void worstPerformingCellsByFailures_4_7_9() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
        checkRankingWindowIsUpdatedForAllTimeRanges("Access Area", cellRankingsWindow);
    }

    /**
     *  105 65-0528/00325
     *          It shall be possible to select a cell from the cell ranking view and view the associated events.
     *          The time interval used in the ranking shall be used to show all the relevant events.
     *          Test Case - 4.7.10
     */
   @Test
    public void drillByCellToViewTheEventsForSameTemporalReference_4_7_10() throws Exception {
        //Level 1
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
        assertTrue("Can't open Cell Ranking page", selenium.isTextPresent("Access Area Ranking"));

        openEventAnalysis(cellRankingsWindow, "Access Area", "Access Area");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Access Area");
    }

    /**
     *  105 65-0528/00326  
     *          It shall be possible to view the top XX RNCs which are experiencing the most event failures. 
     *          XX is a configurable parameter, the default shall be 50. 
     *          The default temporal aggregation for this view shall be 30 minutes.
     *          Test Case - 4.7.11
     */
    @Test
    public void worstPerformingRNCsByFailures_4_7_11A() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_RNC);
        checkRankingWindowIsUpdatedForAllTimeRanges("RNC", rncRankingsWindow);

    }

    /**
     *  105 65-0528/00326  
     *          It shall be possible to view the top XX BSCs which are experiencing the most event failures. 
     *          XX is a configurable parameter, the default shall be 50. 
     *          The default temporal aggregation for this view shall be 30 minutes.
     *          Test Case - 4.7.11
     */
    @Test
    public void worstPerformingBSCsByFailures_4_7_11B() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_BSC);
        checkRankingWindowIsUpdatedForAllTimeRanges("BSC", bscRankingsWindow);
    }

    /**
     *  105 65-0528/00327A
     *          It shall be possible to select a cell from the cell ranking view and view the associated events. 
     *          The time interval used in the ranking shall be used to show all the relevant events.
     *          Test Case - 4.7.12
     */
    @Test
    public void drillByBSCToViewTheEventsForSameTemporalReference_4_7_12A() throws Exception {
        //Level 1
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.BSC_PARENT);
        
        selenium.click("//*[@id='NETWORK_BSC_RANKING']");
        assertTrue("Can't open BSC Ranking page", selenium.isTextPresent("BSC Ranking"));

        openEventAnalysis(bscRankingsWindow, "Controller", "BSC");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Controller");
    }

    /**
     *  105 65-0528/00327B
     *          It shall be possible to select RNC from the ranking view and view the associated events. 
     *          The time interval used in the ranking shall be used to show all the relevant events.
     *          Test Case - 4.7.12
     */
    @Test
    public void drillByRNCToViewTheEventsForSameTemporalReference_4_7_12B() throws Exception {
        //Level 1
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.RNC_PARENT);
        selenium.click("//*[@id='NETWORK_RNC_RANKING']");
        assertTrue("Can't open RSC Ranking page", selenium.isTextPresent("RNC Ranking"));

        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Controller");
    }

    /**
     *  105 65-0528/00328  
     *          It shall be possible to view the top XX cause codes which are experiencing the most event failures. 
     *          XX is a configurable parameter, the default shall be 50. 
     *          The default temporal aggregation for this view shall be 30 minutes.
     *          Test Case - 4.7.13
     */
    @Test
    public void worstPerformingCauseCodeAnalysisByFailures_4_7_13() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE);
        selenium.click("//*[@id='NETWORK_CAUSE_CODE_RANKING']");
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
       
        checkRankingWindowIsUpdatedForAllTimeRanges("Cause Code", causeCodeRankingsWindow);
    }

    /**
     *  105 65-0528/00329  
     *          It shall be possible to select a cause code from the cause code ranking view and view a breakdown of subcause codes, 
     *          how often they occur, and the number of subscribers involved The time interval used in the ranking shall be used to show 
     *          all the relevant events.
     *          Test Case - 4.7.14
     */
    @Test
    public void drillByCauseCodeAnalysisToViewTheEventsForSameTemporalReference_4_7_14() throws Exception {
        //Level 1
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));

        //Level 2
        causeCodeRankingsWindow.clickTableCell(0, causeCodeRankingsWindow.getTableHeaders().indexOf(
                "Cause Code description"), "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Cause Code Event Analysis page", selenium.isTextPresent("Network Cause Code Analysis"));

        networkCauseCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        pause(5000);
    }

    /**
     *  105 65-0528/00375  
     *          It shall be possible to view the top XX APN which subscribers are experiencing the most event failures on. 
     *          XX is a configurable parameter, the default shall be 50. 
     *          The default time interval for this view shall be 30 minutes.
     *          Test Case - 4.7.16
     */
    @Test
    public void showWorstPerformingAPNsByFailures_4_7_16() throws Exception {
        //selenium.setSpeed("2000");
    	rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        selenium.click("//*[@id='NETWORK_APN_RANKING']");
        assertTrue("Can't open 'Events APN Ranking' page", selenium.isTextPresent("APN Ranking (Events)"));
        
        checkRankingWindowIsUpdatedForAllTimeRanges("APN", apnRankingsWindow);
        final List<String> expectedHeaders = new ArrayList<String>(Arrays
                .asList("Rank", "APN", "Failures", "Successes"));
        assertTrue(apnRankingsWindow.getTableHeaders().containsAll(expectedHeaders));
    }

    /**
     *  105 65-0528/00376  
     *          It shall be possible to select an APN from the APN ranking view and view all associated events. 
     *          The time interval used in the ranking shall be used to show the events.
     *         Test Case - 4.7.17
     */
    @Test
    public void drillByAPNToViewTheEventsForSameTemporalReference_4_7_17() throws Exception {
        //Level 1
    	selenium.setSpeed("2000");
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        
        selenium.click("//*[@id='NETWORK_APN_RANKING']");
        assertTrue("Can't open 'Events APN Ranking' page", selenium.isTextPresent("APN Ranking (Events)"));

        openEventAnalysis(apnRankingsWindow, "APN", "APN");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE");
        //drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "APN");
    }

    /**
     * 4.7.18 - Check default number of rows for ranking is 50
     */
    @Test
    public void defaultNumberOfRows_4_7_18() throws Exception {
        //Standalone blade data gen will not have 50 entities for all objects (e.g. there will only be data for 2 RNC's)
        //Need to open a Subscriber Ranking window as there should always be data for 50 IMSI's
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER);
        //No more testing below as we are using limited number of reserved data.
        //final int rowCount = subRankingsWindow.getTableRowCount();
        //assertEquals(50, rowCount);
    }

    /**
     * 4.7.21 - To Verify that it is possible to drill the ranking tables by RNC to view the events for the same 
     * temporal reference as on the ranking table
     */
    @Test
    public void displayAssociatedEventsForRNCRanking_4_7_21() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.RNC_PARENT);
        
        selenium.click("//*[@id='NETWORK_RNC_RANKING']");
        assertTrue("Can't open RSC Ranking page", selenium.isTextPresent("RNC Ranking"));
                
        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
    }

    /**
     * 4.7.22 - To Verify that it is possible to drill to raw events from the ranking tables and verify the all 3G events 
     * are available for use in the ranking engine application
     */
    @Test
    public void verifyAllRelevant3GEventsAreDisplayedOnDrillDownFromRankingViews_4_7_22() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.RNC_PARENT);
        
        selenium.click("//*[@id='NETWORK_RNC_RANKING']");
        assertTrue("Can't open RSC Ranking page", selenium.isTextPresent("RNC Ranking"));
        
        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE");
        //verify2Gor3GEventData(networkEventAnalysisWindow);
    }

    /**
     * Test Case: 4.7.24
     * verify that it is possible get Sub Cause Code Help for Ranking
     */
    @Test
    public void subCauseCodeHelpfromRanking_4_7_24() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE);
        causeCodeRankingsWindow.clickCauseCodes("x-grid-group-div");
        pause(1000);
        causeCodeRankingsWindow.openTableHeaderMenu(0);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void checkRankingWindowIsUpdatedForAllTimeRanges(final String networkType, final CommonWindow commonWindow)
            throws NoDataException, PopUpException {
        assertTrue("Can't load " + networkType + " Ranking window", selenium.isTextPresent(networkType + " Ranking"));
        assertTrue("Default time range is NOT equal to 30 minutes", commonWindow.getTimeRange().equals("30 minutes"));

        for (final TimeRange time : TimeRange.values()) {
            commonWindow.setTimeRange(time);
            assertTrue(time.getLabel() + " is NOT a valid setting for the Time Dialog", selenium
                    .isTextPresent("Time Settings"));
        }
    }

    private void openEventAnalysis(final CommonWindow rankingWindow, final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        rankingWindow.clickTableCell(0, rankingWindow.getTableHeaders().indexOf(columnName), "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networktype + " Event Analysis page", selenium.isTextPresent(" Event Analysis") && selenium.isTextPresent(networktype));
    }

    /**
     * @param window
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck,
            final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Failures");
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
    }

    /**
     * @param eventAnalysisWindow
     * @param networktype
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillAllLinkOnFailedEventAnalysis(final CommonWindow eventAnalysisWindow, final String networktype)
            throws NoDataException, PopUpException {
        for (final ColumnNames columnName : ColumnNames.values()) {
            logger.log(Level.INFO, networktype + ": Drill started for Column " + columnName);

            //Level 3
            eventAnalysisWindow.sortTable(SortType.DESCENDING, columnName.getLabel());
            pause(1000);
            eventAnalysisWindow.clickTableCell(0, columnName.getLabel());
            waitForPageLoadingToComplete();
            assertTrue("Can't open " + columnName + " Event Analysis page", selenium.isTextPresent("Event Analysis"));

            //Level 4
            eventAnalysisWindow.clickTableCell(0, "Failures");
            waitForPageLoadingToComplete();
            assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

            logger.log(Level.INFO, networktype + ": Drill ended for Column " + columnName);

            // go back to the first IMSI Event Analysis window
            for (int i = 0; i < 2; i++) {
                eventAnalysisWindow.clickButton(SelectedButtonType.BACK_BUTTON);
                waitForPageLoadingToComplete();
            }
        }
    }
}
