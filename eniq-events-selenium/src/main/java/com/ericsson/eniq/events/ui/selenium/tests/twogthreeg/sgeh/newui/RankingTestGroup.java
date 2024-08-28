/* COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.newui;

import static com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants.*;
import static com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.newui.SgehConstants.*;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class RankingTestGroup extends EniqEventsUIBaseSeleniumTest {

    public enum ColumnNames {
        TAC("TAC"), SGSN("SGSN-MME"), ACCESS_AREA("Access Area"), CONTROLLER("Controller"), ;
        private final String label;

        private ColumnNames(final String txtLabel) {
            label = txtLabel;
        }

        public String getLabel() {
            return label;
        }
    }

    @Autowired
    private WorkspaceRC workspace;

    private final List<String> expectedHeadersOnSelectedCauseCodeRanking = new ArrayList<String>(Arrays.asList("Rank" ,
            "Cause Protocol Type" , "Cause Code" , "Cause Code Recommended Action" , "Cause Code ID", "Failures" , "Successes"));

    private final List<String> expectedHeadersOnSelectedCorePSCauseCodeTable = new ArrayList<String>(Arrays.asList("Cause Protocol Type", "Cause Code ID",
            "Cause Code", "Cause Code Recommended Action"));

    private final List<String> expectedHeadersOnSelectedCorePSSubCauseCodeTable = new ArrayList<String>(Arrays.asList("Sub Cause Code ID" ,
            "Sub Cause Code", "Sub Cause Code Recommended Action"));

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("bscRankings")
    private CommonWindow bscRankingsWindow;

    @Autowired
    @Qualifier("rncRankings")
    private CommonWindow rncRankingsWindow;

    @Autowired
    @Qualifier("controllerRankings")
    private CommonWindow controllerRankingWindow;

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
    @Qualifier("networkEventAnalysis")
    private CommonWindow controllerEventAnalysisWindow;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow apnRankingsWindow;

    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

    @Autowired
    @Qualifier("causeCodeRankings")
    private CommonWindow causeCodeRankingsWindow;

    @Autowired
    @Qualifier("CauseCodeRankingAnalysis")
    private CommonWindow CauseCodeRankingAnalysis;

    @Autowired
    @Qualifier("CorePSCauseCodeTableAnalysis")
    private CommonWindow CorePSCauseCodeTableAnalysis;

    @Autowired
    @Qualifier("CorePSSubCauseCodeTableAnalysis")
    private CommonWindow CorePSSubCauseCodeTableAnalysis;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Ranking test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Ranking test section");
    }

    String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";

    @Test
    public void showWorstPerformingSubscribersByFailures_4_7_3() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, SUBSCRIBER);
        assertTrue("Can't open SubscriberRanking page", selenium.isTextPresent("Events Subscriber Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("Subscriber", subRankingsWindow);
    }

    @Test
    public void drillBySubscriberToViewTheEventsForSameTemporalReference_4_7_4() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, SUBSCRIBER);
        assertTrue("Can't open SubscriberRanking page", selenium.isTextPresent("Events Subscriber Ranking"));
        openEventAnalysis(subRankingsWindow, "IMSI", "IMSI");
        closePreviousWindow();
        drillAllLinkOnFailedEventAnalysis(subscriberEventAnalysisWindow, "IMSI");
    }

    @Test
    public void showWorstPerformingDevicesByFailures_4_7_6() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, TERMINAL);
        assertTrue("Can't open Terminal Ranking page", selenium.isTextPresent("Events Terminal Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("Terminal", termRankingsWindow);
    }

    @Test
    public void drillByHandsetMakeModelToViewEventsForSameTemporalReference_4_7_7() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, TERMINAL);
        assertTrue("Can't open Terminal Ranking page", selenium.isTextPresent("Terminal Ranking"));
        openEventAnalysisForTerminalRanking(termRankingsWindow, "Terminal", "Model");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(terminalEventAnalysisWindow, "Terminal");
    }

    @Test
    public void drillByHandsetMakeToViewTheAssociatedModelsEventsForSameTemporalReference_4_7_8() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, TERMINAL);
        assertTrue("Can't open Cell Ranking page", selenium.isTextPresent("Terminal Ranking"));
        openEventAnalysisForTerminalRanking(termRankingsWindow, "Terminal", "Manufacturer");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(terminalEventAnalysisWindow, "Terminal");
    }

    @Test
    public void showWorstPerformingAccessAreaByFailures_4_7_9() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, ACCESS_AREA);
        assertTrue("Can't open Access Area Ranking page", selenium.isTextPresent("Access Area Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("Access Area", cellRankingsWindow);
    }

    @Test
    public void drillByAccessAreaToViewTheEventsForSameTemporalReference_4_7_10() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, ACCESS_AREA);
        assertTrue("Can't open Access Ranking page", selenium.isTextPresent("Access Area Ranking"));
        openEventAnalysisforAccessArea(cellRankingsWindow, "Access Area", "Access Area");
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Access Area");
    }

    @Test
    public void worstPerformingRNCsByFailures_4_7_11a() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, RNC);
        assertTrue("Can't open RNC Ranking page", selenium.isTextPresent("RNC Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("RNC", rncRankingsWindow);
    }

    @Test
    public void worstPerformingBSCsByFailures_4_7_11b() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, BSC);
        assertTrue("Can't open BSC Ranking page", selenium.isTextPresent("BSC Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("BSC", bscRankingsWindow);
    }

    @Test
    public void drillByRNCToViewTheEventsForSameTemporalReference_4_7_12a() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, RNC);
        assertTrue("Can't open RSC Ranking page", selenium.isTextPresent("RNC Ranking"));
        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Controller");
    }

    @Test
    public void drillByBSCToViewTheEventsForSameTemporalReference_4_7_12b() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, BSC);
        assertTrue("Can't open Controller  Ranking page", selenium.isTextPresent("BSC Ranking"));
        openEventAnalysis(bscRankingsWindow, "Controller", "BSC");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "Controller");
    }

    @Test
    public void worstPerformingCauseCodeAnalysisByFailures_4_7_13() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_CAUSE_CODE_RANKING, WINDOW_OPTION_CORE_PS);//
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("Cause Code", causeCodeRankingsWindow);
    }

    @Test
    public void drillByCauseCodeAnalysisToViewTheEventsForSameTemporalReference_4_7_14() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_CAUSE_CODE_RANKING, WINDOW_OPTION_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        try{
        causeCodeRankingsWindow
                .clickTableCell(0, causeCodeRankingsWindow.getTableHeaders().indexOf("Cause Code"), "gridCellLauncherLink");
        } catch (Exception e) {
        throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
        assertTrue("Can't open Cause Code Event Analysis page", selenium.isTextPresent("Network Cause Code Analysis"));
    }

    @Test
    public void showWorstPerformingAPNsByFailures_4_7_16() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, APN);//
        assertTrue("Can't open 'Events APN Ranking' page", selenium.isTextPresent("Events APN Ranking"));
        checkRankingWindowIsUpdatedForAllTimeRanges("APN", apnRankingsWindow);
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("Rank", "APN", "Failures", "Successes"));
        assertTrue(apnRankingsWindow.getTableHeaders().containsAll(expectedHeaders));
    }

    @Test
    public void drillByAPNToViewTheEventsForSameTemporalReference_4_7_17() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, APN);
        assertTrue("Can't open 'Events APN Ranking' page", selenium.isTextPresent("Events APN Ranking"));
        openEventAnalysis(apnRankingsWindow, "APN", "APN");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
        drillAllLinkOnFailedEventAnalysis(networkEventAnalysisWindow, "APN");
    }

    @Test
    public void displayAssociatedEventsForRNCRanking_4_7_21() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, RNC);//
        assertTrue("Can't open RNC Ranking page", selenium.isTextPresent("RNC Ranking"));
        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
        checkRankingWindowIsUpdatedForAllTimeRanges("RNC", rncRankingsWindow);
    }

    @Test
    public void verifyAllRelevant3GEventsAreDisplayedOnDrillDownFromRankingViews_4_7_22() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, RNC);//
        assertTrue("Can't open RNC Ranking page", selenium.isTextPresent("RNC Ranking"));
        openEventAnalysis(rncRankingsWindow, "Controller", "RNC");
        closePreviousWindow();
        drillDownFailuresOnEventAnalysisWindow(networkEventAnalysisWindow, "Event Type", "ACTIVATE", "DEACTIVATE", "ATTACH", "RAU", "DETACH",
                "SERVICE_REQUEST", "ISRAU");
    }

    @Test
    public void drillByCauseCodeAnalysisToViewTheEventsForSameTemporalReference_4_7_23() throws Exception {
        List<String> optionalHeaders = new ArrayList<String>(Arrays.asList("Cause Protocol Type", "Cause Code Recommended Action" ));
        openRankingWindowForSgeh(CATEGORY_CORE_CAUSE_CODE_RANKING, WINDOW_OPTION_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        causeCodeRankingsWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        pause(3000);
        assertEquals("Table headers are not matching.\n", expectedHeadersOnSelectedCauseCodeRanking, CauseCodeRankingAnalysis.getTableHeaders());
    }

    @Test
    public void drillByCorePSCauseCodeAnalysisToViewTheEventsForSameTemporalReference_4_7_24() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_CAUSE_CODE_RANKING, WINDOW_OPTION_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        try{
        causeCodeRankingsWindow
                .clickTableCell(0, causeCodeRankingsWindow.getTableHeaders().indexOf("Cause Code"), "gridCellLauncherLink");
        } catch (Exception e) {
        throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
        assertTrue("Can't open Cause Code Event Analysis page", selenium.isTextPresent("Network Cause Code Analysis"));
        selenium.click("//div[@id='btnCC']");
        pause(5000);
        assertTrue("Can't open Core PS Cause Code Table page", selenium.isTextPresent("Core PS Cause Code Table"));
        assertEquals("Table headers are not matching.\n", expectedHeadersOnSelectedCorePSCauseCodeTable, CorePSCauseCodeTableAnalysis.getTableHeaders());
    }

    @Test
    public void drillByCorePSSubCauseCodeAnalysisToViewTheEventsForSameTemporalReference_4_7_25() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_CAUSE_CODE_RANKING, WINDOW_OPTION_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        try{
        causeCodeRankingsWindow
                .clickTableCell(0, causeCodeRankingsWindow.getTableHeaders().indexOf("Cause Code"), "gridCellLauncherLink");
        } catch (Exception e) {
        throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
        assertTrue("Can't open Cause Code Event Analysis page", selenium.isTextPresent("Network Cause Code Analysis"));
        selenium.click("//div[@id='btnSCC']");
        pause(5000);
        assertTrue("Can't open Core PS Sub Cause Code Table page", selenium.isTextPresent("Core PS Sub Cause Code Table"));
        assertEquals("Table headers are not matching.\n", expectedHeadersOnSelectedCorePSSubCauseCodeTable, CorePSSubCauseCodeTableAnalysis.getTableHeaders());
    }

    @Test
    public void drillBySubscriberToViewTheEventsOfControllerForSuccessRatio() throws Exception {
        openRankingWindowForSgeh(CATEGORY_CORE_RANKING, SUBSCRIBER);
        assertTrue("Can't open SubscriberRanking page", selenium.isTextPresent("Events Subscriber Ranking"));
        openEventAnalysis(subRankingsWindow, GuiStringConstants.IMSI, GuiStringConstants.IMSI);
        closePreviousWindow();
        drillAllLinkOnSuccessRatioEventAnalysis(subscriberEventAnalysisWindow, GuiStringConstants.IMSI);
    }

    private void drillAllLinkOnSuccessRatioEventAnalysis(final CommonWindow eventAnalysisWindow, final String networktype) throws NoDataException,
            PopUpException {
        logger.log(Level.INFO, networktype + ": Drill started for Column " + GuiStringConstants.CONTROLLER);
        eventAnalysisWindow.clickTableCell(0, GuiStringConstants.CONTROLLER);
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + GuiStringConstants.CONTROLLER + " Event Analysis page", selenium.isTextPresent("Event Analysis"));
        drillDownOnSuccessRatioAndOccurrenceWindow(eventAnalysisWindow, GuiStringConstants.SUCCESS_RATIO);
        assertTrue("Can't open KPI Analysis By Access Area", selenium.isTextPresent("KPI Analysis By Access Area"));
        selenium.waitForPageLoadingToComplete();
        drillDownOnSuccessRatioAndOccurrenceWindow(eventAnalysisWindow, GuiStringConstants.SUCCESS_RATIO);
        assertTrue("Can't open KPI Analysis By Cause Code", selenium.isTextPresent("KPI Analysis By Cause Code"));
        selenium.waitForPageLoadingToComplete();
        drillDownOnSuccessRatioAndOccurrenceWindow(eventAnalysisWindow, GuiStringConstants.OCCURRENCES);
        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    private void drillDownOnSuccessRatioAndOccurrenceWindow(final CommonWindow window, final String columnHeader) throws NoDataException,
            PopUpException {
        if (columnHeader.equalsIgnoreCase(GuiStringConstants.SUCCESS_RATIO)) {
            window.sortTable(SortType.ASCENDING, columnHeader);
        }else {
            window.sortTable(SortType.DESCENDING, columnHeader);
        }
        try {
            final int column = window.getTableHeaders().indexOf(columnHeader);
            window.clickTableCell(0, column, GuiStringConstants.GRID_CELL_LINK);
        } catch (Exception e) {
            throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
    }

    private void checkRankingWindowIsUpdatedForAllTimeRanges(final String networkType, final CommonWindow commonWindow) throws NoDataException,
            PopUpException {
        assertTrue("Can't load " + networkType + " Ranking window", selenium.isTextPresent(networkType + " Ranking"));
        assertTrue("Default time range is NOT equal to 30 minutes", commonWindow.getTimeRange().equals("30 minutes"));
        for (final TimeRange time : TimeRange.values()) {
            commonWindow.setTimeRange(time);
            assertTrue(time.getLabel() + " is NOT a valid setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
        }
    }

    private void openEventAnalysisforAccessArea(final CommonWindow rankingWindow, final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        logger.info("In openEventAnanysis()");
        final List<Map<String, String>> allTableData = rankingWindow.getAllTableData();
        for (int i = 0; i < allTableData.size(); i++) {
            Map<String, String> result = allTableData.get(i);
            if (!result.get("RAT").equals("LTE")) {
                rankingWindow.clickTableCell(i, rankingWindow.getTableHeaders().indexOf(columnName), "gridCellLauncherLink");
                waitForPageLoadingToComplete();
                assertTrue("Can't open " + networktype + " Event Analysis page",
                        selenium.isTextPresent(" Event Analysis") && selenium.isTextPresent(networktype));
                logger.info("Out of openEventAnanysis()with networktype =  " + networktype);
                break;
            }
        }
    }

    private void openEventAnalysis(final CommonWindow rankingWindow, final String networktype, final String columnName) throws NoDataException,
            PopUpException {
        try{
        logger.info("In openEventAnalysis()");
        rankingWindow.clickTableCell(0, rankingWindow.getTableHeaders().indexOf(columnName), "gridCellLauncherLink");
        } catch (Exception e) {
        throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networktype + " Event Analysis page",
                selenium.isTextPresent(" Event Analysis") && selenium.isTextPresent(networktype));
        logger.info("Out of openEventAnalysis() with networktype =  " + networktype);
    }

    private void openEventAnalysisForTerminalRanking(final CommonWindow rankingWindow, final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        logger.info("In openEventAnalysisForTerminalRanking()");
        for (int rowIndex = 0; rowIndex < rankingWindow.getTableRowCount(); rowIndex++) {
            String model = rankingWindow.getTableData(rowIndex, rankingWindow.getTableHeaders().indexOf(columnName));
            if (!model.equals("Model Unknown") && !model.equals("Manufacturer Unknown")) {
                rankingWindow.clickTableCell(rowIndex, rankingWindow.getTableHeaders().indexOf(columnName), "gridCellLauncherLink");
                break;
            }
        }
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networktype + " Event Analysis page",
                selenium.isTextPresent(" Event Analysis") && selenium.isTextPresent(networktype));
        logger.info("Out of openEventAnalysisForTerminalRanking() with networktype =  " + networktype);
    }

    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Failures");
        final List<Map<String, String>> allTableData = window.getAllTableData();
        for (int i = 0; i < allTableData.size(); i++) {
            Map<String, String> result = allTableData.get(i);
            if (!result.get("Failures").equals("0")) {
                final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
                window.clickTableCell(i, "Failures");
                waitForPageLoadingToComplete();
                assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
                break;
            }
        }
    }

    private void drillAllLinkOnFailedEventAnalysis(final CommonWindow eventAnalysisWindow, final String networktype) throws NoDataException,
            PopUpException {
        for (final ColumnNames columnName : ColumnNames.values()) {
            logger.log(Level.INFO, networktype + ": Drill started for Column " + columnName);
            eventAnalysisWindow.sortTable(SortType.DESCENDING, columnName.getLabel());
            pause(1000);
            eventAnalysisWindow.clickTableCell(0, columnName.getLabel());
            logger.info("0th Value of columnName.getLabel() = ");
            waitForPageLoadingToComplete();
            assertTrue("Can't open " + columnName + " Event Analysis page", selenium.isTextPresent("Event Analysis"));
            logger.info("Level 3 completed");
            final List<Map<String, String>> allTableData = eventAnalysisWindow.getAllTableData();
            for (int i = 0; i < allTableData.size(); i++) {
                Map<String, String> result = allTableData.get(i);
                if (!result.get("Failures").equals("0")) {
                    eventAnalysisWindow.clickTableCell(i, "Failures");
                    waitForPageLoadingToComplete();
                    assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));
                    logger.log(Level.INFO, networktype + ": Drill ended for Column " + columnName);
                    logger.info("Level 4 completed");
                    break;
                }
            }
            for (int i = 0; i < 2; i++) {
                eventAnalysisWindow.clickButton(SelectedButtonType.BACK_BUTTON);
                waitForPageLoadingToComplete();
            }
        }
    }

    private void openRankingWindowForSgeh(String categoryPanel, String windowOption) throws Exception {

        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        pause(2000);
        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
        pause(2000);
    }

    private void closePreviousWindow() {
        selenium.click(closeButtonXPath);
    }

    private void closePropertyWindow() {
        selenium.click("//div[@id='selenium_tag_PropertiesWindow']//table[@class='panelAlign']//button[@class='gwt-Button button']");
        }
  }
