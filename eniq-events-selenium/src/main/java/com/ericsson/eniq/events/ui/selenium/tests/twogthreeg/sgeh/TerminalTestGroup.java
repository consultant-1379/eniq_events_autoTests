package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.TerminalChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.GroupTerminalAnalysisViewType;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.events.windows.TerminalGroupAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.logging.Level;


/**
 * @author eantmcm
 * @since 2011
 */
public class TerminalTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysisWindow;

    @Autowired
    @Qualifier("terminalAnalysis")
    private CommonWindow terminalAnalysisWindow;

    @Autowired
    @Qualifier("terminalKPI")
    private CommonWindow terminalKPI;

    @Autowired
    @Qualifier("termRankings")
    private CommonWindow terminalRankingWindow;

    @Autowired
    @Qualifier("terminalGroupAnalysis")
    private TerminalGroupAnalysisWindow terminalGroupAnalysis;

    @Autowired
    private TerminalChart subChart;

    private final String JSON_MEMBER_ELEMENT = "2";

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Terminal test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Terminal test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        terminalTab.openTab();
    }

    /**
     * REQ - 105 65-0528/00255
     * Test Case - 4.9.2
     * It shall be possible to view an event summary for a nominated device type.
     */
    @Test
    public void displayEventSummaryForSelectedDeviceType_4_9_2() throws Exception {
    	selenium.setSpeed("2000");
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalEventAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00256
     * Test Case - 4.9.3
     * It shall be possible to view an event summary for a nominated device type group.
     */
    @Test
    public void displayEventSummaryForSelectedDeviceTypeGroup_4_9_3() throws Exception {
    	selenium.setSpeed("2000");
    	
        openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, false, reservedDataHelper
                .getCommonReservedData(CommonDataType.TERMINAL_GROUP));
        
        terminalEventAnalysisWindow.setTimeRange(TimeRange.ONE_WEEK);
        
        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalEventAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00257
     * Test Case - 4.9.4
     * It shall be possible to view the most popular device types (i.e. make and model) for a 
     * time interval. Most popular is defined as device types occurring most often in signalling 
     * events. The most popular list shall be limited to 50 entries, this being configurable 
     * to a maximum of 100.
     */
    @Test
    public void viewMostFrequentSignallingTerminalsForSelectedTimeInterval_4_9_4() throws Exception {
        selenium.setSpeed("2000");
    	openTerminalAnalysisDefaultWindow(false);
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 
     * Test Case - 4.9.5
     * 
     */
    //@Test
    public void configureNumberOfEntriesInMostPopularDeviceTypeList_4_9_5() throws Exception {
        openTerminalAnalysisDefaultWindow(true);
        // TODO UI does not allow user to change number of rows displayed - return to this test case when UI is updated
    }

    /**
     * REQ - 105 65-0528/00258
     * Test Case - 4.9.6
     * It shall be possible to view an event summary for the most popular device types 
     * for a time interval. Most popular is defined as device types occurring most 
     * often in signalling events. The most popular list shall be limited to 50 entries, 
     * this being configurable to a maximum of 100.
     */
    @Test
    public void viewEventSummaryForMostFrequentSignallingTerminalsForSelectedTimeInterval_4_9_6() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_POPULAR_EVENT_SUMMARY);
        verifyTerminalAnalysisWindow("Most Frequent Signaling Summary", terminalAnalysisWindow);
    }

    /**
     * REQ - 105 65-0528/00259
     * Test Case - 4.9.7
     * It shall be possible to view the most popular devices type groups for a temporal period. 
     * Most popular is defined as device type group occurring most often in signalling events. 
     * The most popular group list shall be limited to 10 entries, this being configurable to 
     * a maximum of 20.
     */
    @Test
    public void viewMostMostFrequentSignallingTerminalsGroupsForSelectedTimeInterval_4_9_7() throws Exception {
        openTerminalAnalysisDefaultWindow(true);
        // TODO "Support Flash objects"
    }

    /**
     * REQ - 
     * Test Case - 4.9.8
     * 
     */
    //@Test
    public void configureNumberOfEntriesInMostPopularDeviceTypeGroupList_4_9_8() throws Exception {
        openTerminalAnalysisDefaultWindow(true);
        //TODO - Configure number of entries. 
    }

    /**
     * REQ - 105 65-0528/00260
     * Test Case - 4.9.9
     * It shall be possible to view an event summary for the most popular device type groups for 
     * a time interval. Most popular is defined as device type groups occurring most often in 
     * signalling events. The most popular group list shall be limited to 10 entries, this being 
     * configurable to a maximum of 20.
     */
    @Test
    public void viewEventSummaryForMostFrequentSignallingTerminalsForSelectedTimeInterval_4_9_9() throws Exception {
        terminalTab.openSubStartMenu(TerminalTab.StartMenu.GROUP_ANALYSIS);
        setTimeRangeToOneWeek(terminalGroupAnalysis);
        final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
                terminalGroupAnalysis.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
        subChart.processDrillDownChart(chartElements.get(0), "Most Frequent Signaling Event Summary");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00261
     * Test Case - 4.9.10
     * It shall be possible to view statistics for PDP sessions of device types. Specifically 
     * average and max PDP session duration for the last 24hours shall be available on a 
     * quarter-hourly basis.
     */
    //@Test
    public void viewPDPSessionStatisticsForDeviceTypes_4_9_10() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        //Functionality not implemented yet!
    }

    /**
     * 
     * REQ - 105 65-0528/00262
     * Test Case - 4.9.11
     * It shall be possible to view statistics for PDP sessions of device type groups. 
     * Specifically average and max PDP session for the last 24hours shall be available on a 
     * quarter-hourly basis.
     */
    //@Test
    public void viewPDPSessionStatisticsForDeviceTypesGroups_4_9_11() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        //Functionality not implemented yet!
    }

    /**
     * REQ - 105 65-0528/00265
     * Test Case - 4.9.12
     * It shall be possible to view device types which experience most attach failures.
     */
    @Test
    public void viewDeviceWhichExperienceMostAttachFailures_4_9_12() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        pause(1000);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_ATTACH_FAILURES);
        verifyTerminalAnalysisWindow("Terminal Analysis - Most Attach Failures", terminalAnalysisWindow);
        terminalAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00266
     * Test Case - 4.9.13
     * It shall be possible to view device type groups which experience most attach failures.
     */
    @Test
    public void viewTerminalGroupsWhichExperienceMostAttachFailures_4_9_13() throws Exception {
    	selenium.setSpeed("1000");
        terminalTab.openSubStartMenu(TerminalTab.StartMenu.GROUP_ANALYSIS);
        selenium.click("//div[@id='x-menu-el-TERMINAL_GA_MOST_POPULAR_SUMMARY']/a[@id='TERMINAL_GA_MOST_POPULAR_SUMMARY']");
    	      
        setTimeRangeToOneWeek(terminalGroupAnalysis);
        terminalGroupAnalysis.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_ATTACH_FAILURES);
        waitForPageLoadingToComplete();
        pause(3000);
        selenium.click("//*[@id='btnToggleToGrid']");
        waitForPageLoadingToComplete();
        //        setTimeRangeToOneWeek(terminalGroupAnalysis);
//        final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
//                terminalGroupAnalysis.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
//        subChart.processDrillDownChart(chartElements.get(0), "Most Attach Failures");
//        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00267
     * Test Case - 4.9.14
     * It shall be possible to view device types which experience most PDP session setup failures
     */
    @Test
    public void viewDeviceWhichExperienceMostPDPFailures_4_9_14() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        pause(1000);
        terminalAnalysisWindow
                .setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_PDP_SESSION_SETUP_FAILURES);
        verifyTerminalAnalysisWindow("Terminal Analysis - Most PDP Session Setup Failures", terminalAnalysisWindow);
        terminalAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00268
     * Test Case - 4.9.15
     * It shall be possible to view device type groups which experience most PDP session setup 
     * failures.
     */
    @Test
    public void viewDeviceGroupsWhichExperienceMostPDPFailures_4_9_15() throws Exception {
        openTerminalAnalysisDefaultWindow(true);
        pause(1000);
        terminalGroupAnalysis.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_PDP_SESSION_SETUP_FAILURES);
        waitForPageLoadingToComplete();
//        pause(3000);
//        setTimeRangeToOneWeek(terminalGroupAnalysis);
//        final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
//                terminalGroupAnalysis.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
//        subChart.processDrillDownChart(chartElements.get(0), "Most PDP Session Setup Failures");
//        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00269
     * Test Case - 4.9.16
     * 
     */
    @Test
    public void viewTerminalWhichHasTheMostMobilityFailures_4_9_16() throws Exception {
        selenium.setSpeed("2000");
    	openTerminalAnalysisDefaultWindow(false);
        pause(1000);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_MOBILITY_ISSUES);
        verifyTerminalAnalysisWindow("Terminal Analysis - Most Mobility Failures", terminalAnalysisWindow);
        terminalAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00270
     * Test Case - 4.9.17
     * It shall be possible to view device type groups which experience most mobility issues.
     */
    @Test
    public void viewDeviceGroupsWhichExperienceMostMobilityIssues_4_9_17() throws Exception {
        openTerminalAnalysisDefaultWindow(true);
        pause(1000);
        terminalGroupAnalysis.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_MOBILITY_ISSUES);
        setTimeRangeToOneWeek(terminalGroupAnalysis);
        verifyTerminalAnalysisWindow("Group Analysis", terminalGroupAnalysis);
//        setTimeRangeToOneWeek(terminalGroupAnalysis);
//        final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
//                terminalGroupAnalysis.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
//        subChart.processDrillDownChart(chartElements.get(0), "Most PDP Session Setup Failures");
//        waitForPageLoadingToComplete();
        // TODO "Support Flash objects"
    }

    /**
     * REQ - 105 65-0528/00271 and 105 65-0528/00250
     * Test Case - 4.9.18
     * It shall be possible to view a list of the handset types most swapped to.
     * It shall be possible to view a list of the handset types most swapped from.
     */
    //@Test
    public void viewListOfMostSwappedHandsetTypes_4_9_18() {

    }

    /**
     * REQ - 105 65-0528/00273
     * Test Case - 4.9.19
     * It shall be possible to view the following key performance indicators on device type 
     * group: Attach Success Rate, PDP Context Activation Success Rate, Routing Area Update 
     * Success Rate, Inter SGSN Routing Area Update Success Rate, PDP Context Cutoff Ratio. 
     * It shall be possible drill from device type group into KPIs for individual device types. 
     * The number of subscribers and number of events shall be displayed alongside the performance indicator value. 
     * From this view it shall be possible drilldown and see the relevant errored events that contribute to the selected performance indicator.
     */
    @Test
    public void viewKPIOnDeviceTypeAndDrillDownToViewErrorEvents_4_9_19() throws Exception {

        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL));

        if (!terminalEventAnalysisWindow.isButtonEnabled(SelectedButtonType.KPI_BUTTON)
                && (terminalEventAnalysisWindow.getTableRowCount() == 0)) {
            throw new NoDataException("KPI button is disalbed as there is no data in Terminal Event Analysis Window");
        }
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON);
        waitForPageLoadingToComplete();
    }

    /**
     * REQ - 105 65-0528/00272
     * Test Case - 4.9.20
     * It shall be possible to view the following key performance indicators on device type: 
     * Attach Success Rate, PDP Context Activation Success Rate, Routing Area Update Success Rate,
     *  Inter SGSN Routing Area Update Success Rate, PDP Context Cutoff Ratio. The number of 
     *  subscribers and number of events shall be displayed alongside the performance indicator 
     *  value. From this view it shall be possible drilldown and see the relevant errored events 
     *  that contribute to the selected performance indicator.
     */
    @Test
    public void viewKPIOnDeviceGroupsDrillDownTOKPIAndErrorEvents_4_9_20() throws Exception {
        openEventAnalysisWindow(TerminalType.TERMINAL, true, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL));

        if (!terminalEventAnalysisWindow.isButtonEnabled(SelectedButtonType.KPI_BUTTON)
                && (terminalEventAnalysisWindow.getTableRowCount() == 0)) {
            throw new NoDataException("KPI button is disalbed as there is no data in Terminal Event Analysis Window");
        }
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON);
        waitForPageLoadingToComplete();

        terminalKPI.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        waitForPageLoadingToComplete();
        //TODO-The KPIs not drilable. Functionality not implemented
    }

    /**
     * REQ - 105 65-0528/00274
     * Test Case - 4.9.21
     * It shall be possible to exclude devices from all device analysis algorithms.
     */
    //@Test
    public void excludeDevicesFromAllDeviceAnalysisAlgorithms_4_9_21() {

    }

    /**
     * REQ - 105 65-0528/00275
     * Test Case - 4.9.22
     *Events from devices excluded from all device analysis algorithms shall not be lost. 
     *It shall still be possible to view them in dedicated user interface views for excluded 
     *devices. 
     */
    //@Test
    public void viewEventsFromExcludedDevicesInDedicatedUserInterfaceViews_4_9_22() {

    }

    /**
     * REQ - 
     * Test Case - 4.9.23
     * 
     */
    //@Test
    public void viewListOfHandsetTypesMostSwappedFrom_4_9_23() {

    }

    /**
     * REQ - 105 65-0528/00253
     * Test Case - 4.9.24
     * When the device analysis feature is uninstalled the data contained within the TAC 
     * database tables should be removed.
     */
    //@Test
    public void uninstallDeviceAnalysisFeature_4_9_24() {

    }

    /**
     * REQ - 105 65-0528/00254
     * Test Case - 4.9.25
     * If information for a given IMEI is not in the TAC database then the TAC shall be 
     * displayed on the user interface.
     */
    //@Test
    public void IMEINotInTACDatabase_4_9_25() throws Exception {
        terminalTab.openSubStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS);
        setTimeRangeToOneWeek(terminalRankingWindow);
        //TODO. TAC drilable - functionality not implemented
        pause(1000);
        terminalRankingWindow.clickRankingDrills("gridCellLink", "failure");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis", selenium.isTextPresent("Failed Event Analysis"));

        logger.log(Level.INFO, "Failed Event Analysis returns " + terminalRankingWindow.getTableRowCount()
                + " Number of rows.");
        assertTrue("No Data retrieved for 'Failed Event Analysis' for 1 week time range.", terminalRankingWindow
                .getTableRowCount() > 0);
    }

    /**
     * REQ - 
     * Test Case - 4.9.26
     * 
     */
    @Test
    public void openDefaultViewForSingleTerminalType_4_9_26() throws Exception {
        openEventAnalysisWindow(TerminalType.TERMINAL, true, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL));
    }

    /**
     * REQ - 
     * Test Case - 4.9.27
     */
    @Test
    public void openDefaultViewForSingleTerminalGroup_4_9_27() throws Exception {
        openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true, reservedDataHelper
                .getCommonReservedData(CommonDataType.TERMINAL_GROUP));
    }

    /**
     * REQ - 
     * Test Case - 4.9.28
     * 
     */
    @Test
    public void searchBarCombinedForSingleAndGroupSearch_4_9_28() throws Exception {
        terminalTab.setSearchType(TerminalType.TERMINAL);
        assertTrue("Terminal make search field does not exist.", selenium
                .isElementPresent("//input[@id='selenium_tag_typesCombo-input']"));
        assertTrue("Terminal search field does not exist.", selenium
                .isElementPresent("//input[@id='selenium_tag_searchField-input']"));
        pause(2000);

        openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, false, reservedDataHelper
                .getCommonReservedData(CommonDataType.TERMINAL_GROUP));
    }

    /**
     * REQ - 
     * Test Case - 4.9.29
     * 
     */
    @Test
    public void terminalTabEventAnalysisShouldDisplayBoth2GAnd3GEventsForATerminalAndTerminalGroup_4_9_29()
            throws Exception {
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL));

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalEventAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();

        verify2Gor3GEventData(terminalEventAnalysisWindow);
        selenium.refresh();
        pause(2000);

        // Level 2
        openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, false, reservedDataHelper
                .getCommonReservedData(CommonDataType.TERMINAL_GROUP));
        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalEventAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
        verify2Gor3GEventData(terminalEventAnalysisWindow);
    }

    /**
     * REQ - 
     * Test Case - 4.9.30A
     * @throws PopUpException 
     * @throws NoDataException 
     * 
     */
    @Test
    public void display2Gand3GEventsInTerminalAnalysisAndGroupAnalysis_4_9_30A() throws Exception {
        openTerminalAnalysisDefaultWindow(false);
        pause(2000);

        // Level 1
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_ATTACH_FAILURES);
        pause(2000);
        drillTerminalAnaylsisFor2G3GEvents();

        //Level 2
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_MOBILITY_ISSUES);
        pause(2000);
        drillTerminalAnaylsisFor2G3GEvents();

        //Level 3
        terminalAnalysisWindow
                .setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_PDP_SESSION_SETUP_FAILURES);
        pause(2000);
        terminalAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
        pause(1000);
        verify2Gor3GEventData(terminalAnalysisWindow);

    }

    /**
     * REQ - 
     * Test Case - 4.9.30B
     * @throws PopUpException 
     * @throws NoDataException 
     * 
     */
    @Test
    public void display2Gand3GEventsInTerminalAnalysisAndGroupAnalysis_4_9_30B() throws Exception {

        terminalTab.openSubStartMenu(TerminalTab.StartMenu.GROUP_ANALYSIS);
        setTimeRangeToOneWeek(terminalGroupAnalysis);
        pause(3000);
        verifydrillGroupAnalysisChart();
        waitForPageLoadingToComplete();
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void openTerminalAnalysisDefaultWindow(final boolean group) throws NoDataException, PopUpException {
        if (group) {
            terminalTab.openSubStartMenu(TerminalTab.StartMenu.GROUP_ANALYSIS);
            selenium.click("//a[@id='TERMINAL_GA_MOST_POPULAR_SUMMARY']");
            setTimeRangeToOneWeek(terminalGroupAnalysis);
            verifyTerminalAnalysisWindow("Group Analysis",
                    terminalGroupAnalysis);
        } else {
        	selenium.click("//*[@id='TERMINAL_TAB_START']/tbody[@class='x-btn-small x-btn-icon-small-left']/tr[2]/td[@class='x-btn-mc']");
        	selenium.mouseOver("//div[contains(@class,'x-menu-list')]/a[@id='TERMINAL_ANALYSIS']");
        	selenium.click("//*[@id='TERMINAL_MOST_POPULAR_SUMMARY']");
            waitForPageLoadingToComplete();
            setTimeRangeToOneWeek(terminalAnalysisWindow);
            verifyTerminalAnalysisWindow("Terminal Analysis - Most Frequent Signaling Summary", terminalAnalysisWindow);
        }
    }

    private void drillTerminalAnaylsisFor2G3GEvents() throws NoDataException, PopUpException {
        terminalAnalysisWindow.sortTable(SortType.DESCENDING, "Failures");
        terminalAnalysisWindow.clickTableCell(0, "Failures");
        waitForPageLoadingToComplete();
        verify2Gor3GEventData(terminalAnalysisWindow);

        selenium.refresh();
        pause(2000);
        terminalTab.openTab();
        openTerminalAnalysisDefaultWindow(false);
    }

    private void verifyTerminalAnalysisWindow(final String view, final CommonWindow terminalWindow)
            throws NoDataException, PopUpException {
        if (terminalWindow.equals(terminalGroupAnalysis)) {
            terminalWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        }
        pause(1000);
        assertTrue("Can't open window - " + view, selenium.isTextPresent(view));
        logger.log(Level.INFO, view + " returns " + terminalWindow.getTableRowCount() + " Number of rows.");
        pause(1000);
    }

    private void verifydrillGroupAnalysisChart() throws PopUpException, NoDataException {
        for (final TerminalGroupAnalysisWindow.ViewMenu menu : TerminalGroupAnalysisWindow.ViewMenu.values()) {
            terminalGroupAnalysis.clickViewSubMenu(menu);

            final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
                    terminalGroupAnalysis.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
            subChart.processDrillDownChart(chartElements.get(0), menu.getDisplayName());
            //            verify2Gor3GEventData(termOverviewWindow);
        }
    }

    private void openEventAnalysisWindow(final TerminalType type, final boolean submitButton, final String... values)
            throws PopUpException, InterruptedException {
        terminalTab.openEventAnalysisWindow(type, submitButton, values);
               
        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            assertTrue("Can't open " + values[0] + " - Terminal Event Analysis", selenium
                    .isTextPresent("Terminal Group - Event Analysis"));
        } else {
            assertTrue("Can't open Terminal Event Analysis.", selenium.isTextPresent("Event Analysis"));
        }
    }
}
