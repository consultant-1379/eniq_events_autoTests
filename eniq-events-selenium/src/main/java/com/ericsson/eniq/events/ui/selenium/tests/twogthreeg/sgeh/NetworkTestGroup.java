package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.CheckboxGroup;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.logging.Level;

public class NetworkTestGroup extends EniqEventsUIBaseSeleniumTest {
	
	public static WebElement element;
	
	@Autowired
	private Workspace workspace;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("eventVolume")
    private CommonWindow eventVolumeWindow;

    @Autowired
    @Qualifier("roamingByOperatorEvents")
    private CommonWindow roamingByOperatorEventsWindow;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow eventAnalysisWindow;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow apnRankingsWindow;

    @Autowired
    @Qualifier("accessAreaRankings")
    private CommonWindow accessAreaRankingsWindow;

    @Autowired
    @Qualifier("controllerRankings")
    private CommonWindow bscRankingsWindow;

    @Autowired
    @Qualifier("rncRankings")
    private CommonWindow rncRankingsWindow;

    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Network test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Network test section");
    }

    /*
    * REQ - 105 65-0528/00303 Test Case - 4.8.10 It shall be possible to
    * view a failure breakdown analysis on a selected SGSN. The breakdown shall
    * include number of events and number of subscribers for which the events
    * occurred. The cause code breakdown revealed shall be drillable to subcause
    * code level.
    */
    @Test
    public void displayFailureForSelectedSGSNAndDrillCauseCodeToSubCauseCodeLevel_4_8_10() throws Exception {
        // Action 1
        openCauseCodeAnalysisWindow();
        // Action 2
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN);
        networkTab.setSearchType(NetworkType.SGSN_MME);
        enterValueAndClickSubmitButton(searchValue, false);

        // final List<String> expectedHeaders = new
        // ArrayList<String>(Arrays.asList("Cause Code", "Sub Cause Code",
        // "Occurrences", "Subscribers")); final List<String> tableHeaders =
        // causeCodeAnalysisWindow.getTableHeaders(); assertTrue(
        // "SGSN Cause Code Analysis page, doesn't contain any of the expected Headers"
        // , tableHeaders.containsAll(expectedHeaders));

        // Action 3
        causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        waitForPageLoadingToComplete();
        assertTrue("Can't open SGSN Cause Code Analysis page", selenium.isTextPresent(searchValue
                + " - SGSN Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);

        // Action 4
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("SGSN Sub Cause Code Analysis page didn't open", selenium
                .isTextPresent("SGSN Sub Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);
    }

    /*
     * REQ - 105 65-0528/00304 Test Case - 4.8.11 It shall be possible to
     * view a failure breakdown analysis on a selected Controller. The breakdown
     * shall include number of events and number of subscribers for which the
     * events occurred. The cause code breakdown revealed shall be drillable to
     * subcause code level.
     */
    @Test
    public void displayFailureForSelectedBSCAndDrillCauseCodeToSubCauseCodeLevel_4_8_11() throws Exception {
        openCauseCodeAnalysisWindow();

        // Action 2
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER);
        networkTab.setSearchType(NetworkType.CONTROLLER);
        enterValueAndClickSubmitButton(searchValue, false);

        // Action 3
        causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Controller Cause Code Analysis page", selenium.isTextPresent(searchValue
                + " - Controller Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);

        // Action 4
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Controller Sub Cause Code Analysis page didn't open", selenium
                .isTextPresent("Controller Sub Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);
    }

    /*
     * REQ - 105 65-0528/00305 Test Case - 4.8.12 It shall be possible to
     * view a failure breakdown analysis on a selected cell. The breakdown shall
     * include number of events and number of subscribers for which the events
     * occurred. The cause code breakdown revealed shall be drillable to subcause
     * code level.
     */
    @Test
    public void displayFailureForSelectedCellAndDrillCauseCodeToSubCauseCodeLevel_4_8_12() throws Exception {

        openCauseCodeAnalysisWindow();

        // Action 2
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA);
        networkTab.setSearchType(NetworkType.ACCESS_AREA);
        networkTab.enterSearchValue(searchValue, false);
        pause(5000);

        networkTab.enterSubmit(false);
        waitForPageLoadingToComplete();

        // Action 3
        causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Access Area Cause Code Analysis page", selenium.isTextPresent(searchValue
                + " - Access Area Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);

        // Action 4
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Access Area Sub Cause Code Analysis page didn't open", selenium
                .isTextPresent("Access Area Sub Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);
    }

    /*
     * REQ - 105 65-0528/00306 Test Case - 4.8.13 It shall be possible to
     * view a failure breakdown analysis on a selected APN. The breakdown shall
     * include number of events and number of subscribers for which the events
     * occurred. The cause code breakdown revealed shall be drillable to subcause
     * code level.
     */
    @Test
    public void displayFailureForSelectedAPNAndDrillCauseCodeToSubCauseCodeLevel_4_8_13() throws Exception {

        openCauseCodeAnalysisWindow();

        // Action 2
        final String searchValue = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.APN);
        networkTab.enterSearchValue(searchValue, false);
        pause(5000);

        networkTab.enterSubmit(false);
        waitForPageLoadingToComplete();

        // Action 3
        causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        waitForPageLoadingToComplete();
        assertTrue("Can't open APN Cause Code Analysis page", selenium.isTextPresent(searchValue
                + " - APN Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);

        // Action 4
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("APN Sub Cause Code Analysis page didn't open", selenium
                .isTextPresent("APN Sub Cause Code Analysis"));
        // networkTab.checkTableValidity(causeCodeAnalysisWindow, searchValue);
    }

    /*
     * REQ - 105 65-0528/00307 Test Case - 4.8.14 It shall be possible to
     * graphically view an event summary for all the network, or from a selected
     * node, for a selected temporal reference. The successful and failed events
     * shall be distinguishable, and the number of subscribers shall also be
     * shown.
     */
    @Test
    public void displayGraphicalEventSummary_4_8_14() throws Exception {

        // Action 1
        networkTab.openTab();
        networkTab.openStartMenu(NetworkTab.StartMenu.EVENT_VOLUME);
        assertTrue("Can't open Event Volume window", selenium.isTextPresent("Event Volume"));
        waitForPageLoadingToComplete();

        // Action 2
        eventVolumeWindow.clickButton(SelectedButtonType.VIEW_BUTTON);

        // Action 3
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX2);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX4);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX7);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX10);

        // Action 4
        // PASSING BUT NOT WORKING - Action 4:Click away from the View menu -
        // Result 4:The view menu disappears.

        eventVolumeWindow.clickWindowHeader();
        pause(5000);

        // Action 5
        // Action 5:In the main EniqEventsUI, enter or select any node. Open the
        // network tab and from the start menu click on Event Volume. Result 5:A
        // window opens and displays a graph. The graph displays event information
        // for the given node showing the total number of successful events for each
        // event type and total number of failed events for each event type.

        // ***Does not make sense to re-open a window that is already open***
    }

    /*
     * REQ - 105 65-0528/00308 and 105 65-0528/00309 Test Case - 4.8.15 It
     * shall be possible to graphically view event attempts and errors for all
     * event types over a selected time period. The number of subscribers shall
     * also be shown. It shall be possible to dynamically add and remove a data
     * series for a given event type.
     */
    @Test
    public void eventVolume_4_8_15() throws Exception {

        // Action 1
        networkTab.openTab();
        networkTab.openStartMenu(NetworkTab.StartMenu.EVENT_VOLUME);
        assertTrue("Can't open Event Volume window", selenium.isTextPresent("Event Volume"));
        waitForPageLoadingToComplete();

        // Action 2
        eventVolumeWindow.clickButton(SelectedButtonType.VIEW_BUTTON);

        // Action 3
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX2);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX4);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX7);
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX10);

        // Action 4
        // PASSING BUT NOT WORKING - Action 4:Click away from the View menu - Result
        // 4:The view menu disappears.
        eventVolumeWindow.clickWindowHeader();
        pause(5000);
    }

    /*
     * REQ - 105 65-0528/00310 Test Case - 4.8.16 A simple roaming analysis
     * view shall be provided, showing counts of roaming subscribers, numbers of
     * events, and numbers of failures. Country lists and operator lists should be
     * provided. A drilldown into the event details should not be provided.
     */
    @Test
    public void displayRoamingAnalysisView_4_8_16() throws Exception {

        // Action 1
        networkTab.openTab();
        networkTab.openSubStartMenu(NetworkTab.StartMenu.ROAMING_ANALYSIS, NetworkTab.SubStartMenu.ROAMING_BY_OPERATOR);
        assertTrue("Can't open Roaming by Operator window", selenium.isTextPresent("Roaming by Operator"));
        waitForPageLoadingToComplete();

        // Action 2
        // Action2:Hover the mouse over one of the data points on the chart.
        // Result2:A tool tip is displayed showing the value for that data point.
        // ***Unable to interact with flash graph***

        // Action 6 & 7

        // Action 3
        roamingByOperatorEventsWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        pause(5000);

        // Action 4
        // Action4:Click the export button. Result4:The table contents are exported
        // ***NO EXPORT FUNCTION on chart***

        // Action 5
        roamingByOperatorEventsWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        pause(5000);

        // Action 8
        // ****NO View button on toolbar. Code below is Roaming By Country selected
        // via Start Menu***

        // networkTab.openTab();
        // networkTab.openRoamingByCountryWindow();
        // roamingByCountryWindow.setTimeRange(TimeRange.ONE_WEEK);
        // assertTrue("Error occured in page loading",
        // selenium.waitForPageLoadingToComplete());
    }

    /*
     * REQ - 105 65-0528/00311 Test Case - 4.8.17 It shall be possible to
     * exclude devices from all network analysis computations.
     */
    //@Test
    public void excludeADeviceFromNetworkAnalysisComputations_4_8_17() {
        // No Functionality in UI
        // ***Not relative to Network Tab - Terminal Tab Test Case***
    }

    /*
     * REQ - 105 65-0528/00312 Test Case - 4.8.18 Events from devices
     * excluded from all network analysis algorithms shall not be lost. It shall
     * still be possible to view them in dedicated user interface views for
     * excluded devices.
     */
    //@Test
    public void viewEventsFromExcludedDevicesInDedicatedUserInterfaceViews_4_8_18() {
        // No Functionality in UI
        // ***Not relative to Network Tab - Terminal Tab Test Case***
    }

    /*
     * REQ - Test Case - 4.8.19 Open default view for a single node (APN)
     */
    @Test
    public void openDefaultViewForSingleNodeAPN_4_8_19() throws Exception {
        // Action 1
        networkTab.openTab();

        // Action 2
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.EVENT_RANKING,
                NetworkTab.SubOfSubStartMenu.EVENT_RANKING_APN);
        pause(500);
        assertTrue("Can't open Ranking window", selenium.isTextPresent("APN Ranking"));
        waitForPageLoadingToComplete();

        // Action 3
        apnRankingsWindow.clickTableCell(0, 1, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        pause(500);
        eventAnalysisWindow.closeWindow();
        pause(500);

        apnRankingsWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);
        assertTrue("Can't open APN property window", selenium.isTextPresent("APN - Properties"));
        pause(3000);
        final String apnValue = networkTab.getAPNFromPropertiesWindow();
        pause(1000);

        // Action 4
        networkTab.enterSearchValue(apnValue, false);
        pause(2000);
        networkTab.enterSubmit(false);
        waitForPageLoadingToComplete();
        assertTrue("Can't open Ranking window", selenium.isTextPresent(apnValue + " - APN Event Analysis"));
    }

    /*
     * REQ - Test Case - 4.8.19b Open default view for a single node
     * (Access Area)
     */
    @Test
    public void openDefaultViewForSingleNodeAccessArea_4_8_19b() throws Exception {
        // Action 1
        networkTab.openTab();

        // *****Action 5: Repeat Actions 2-4 for 'AccessArea'*****
        // Repeat of Action 2 for 'Access Area'
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.EVENT_RANKING,
                NetworkTab.SubOfSubStartMenu.EVENT_RANKING_ACCESS_AREA);
        pause(500);
        networkTab.setSearchType(NetworkType.ACCESS_AREA);
        pause(500);
        assertTrue("Can't open Access Area Ranking window", selenium.isTextPresent("Access Area Ranking"));
        waitForPageLoadingToComplete();

        // Repeat of Action 3 for 'Access Area'
        accessAreaRankingsWindow.clickTableCell(0, 4, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        pause(1500);
        eventAnalysisWindow.closeWindow();
        pause(1000);

        accessAreaRankingsWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);
        assertTrue("Can't open Access Area property window", selenium.isTextPresent("Access Area - Properties"));
        pause(2000);
        final String accessAreaValue = networkTab.getAccessAreaFromPropertiesWindow();

        // Repeat of Action 4 for 'Access Area'
        networkTab.enterSearchValue(accessAreaValue, false);
        pause(1000);
        networkTab.enterSubmit(false);
        // assertTrue("Can't open Event Analysis window",
        // selenium.isTextPresent(accessAreaValue +
        // " - Access Area Event Analysis"));
        waitForPageLoadingToComplete();
    }

    /*
     * REQ - Test Case - 4.8.19c Open default view for a single node (BSC)
     */
    @Test
    public void openDefaultViewForSingleNodeBSC_4_8_19c() throws Exception {
        // Action 1
        networkTab.openTab();

        // *****Action 5: Repeat Actions 2-4 for 'BSC Controller'*****
        // Repeat of Action 2 for 'BSC'
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.EVENT_RANKING,
                NetworkTab.SubOfSubStartMenu.EVENT_RANKING_BSC);
        pause(500);
        networkTab.setSearchType(NetworkType.CONTROLLER);
        pause(500);
        assertTrue("Can't open BSC Ranking window", selenium.isTextPresent("BSC Ranking"));
        waitForPageLoadingToComplete();

        setTimeRangeToOneWeek(bscRankingsWindow);

        // Repeat of Action 3 for 'BSC'
        bscRankingsWindow.clickTableCell(0, 2, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        pause(1500);
        eventAnalysisWindow.closeWindow();
        pause(1000);

        bscRankingsWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);
        assertTrue("Can't open BSC property window", selenium.isTextPresent("BSC - Properties"));
        pause(1000);
        final String controllerValue = networkTab.getControllerValueFromPropertiesWindow();

        // Repeat of Action 4 for 'BSC'
        networkTab.enterSearchValue(controllerValue, false);
        pause(1000);
        networkTab.enterSubmit(false);
        // assertTrue("Can't open Event Analysis window",
        // selenium.isTextPresent(accessAreaValue +
        // " - Access Area Event Analysis"));
        waitForPageLoadingToComplete();
    }

    /*
     * REQ - Test Case - 4.8.19d Open default view for a single node (RNC)
     */
    @Test
    public void openDefaultViewForSingleNodeRNC_4_8_19d() throws Exception {
        // Action 1
        networkTab.openTab();

        // *****Action 5: Repeat Actions 2-4 for 'RNC Controller'*****
        // Repeat of Action 2 for 'RNC'
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.EVENT_RANKING,
                NetworkTab.SubOfSubStartMenu.EVENT_RANKING_RNC);
        pause(500);
        networkTab.setSearchType(NetworkType.CONTROLLER);
        pause(500);
        assertTrue("Can't open RNC Ranking window", selenium.isTextPresent("RNC Ranking"));
        waitForPageLoadingToComplete();

        // Repeat of Action 3 for 'RNC'
        rncRankingsWindow.clickTableCell(0, 2, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        pause(1500);
        eventAnalysisWindow.closeWindow();
        pause(1000);

        rncRankingsWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);
        assertTrue("Can't open RNC property window", selenium.isTextPresent("RNC - Properties"));
        pause(1000);
        final String controllerValue = networkTab.getControllerValueFromPropertiesWindow();

        // Repeat of Action 4 for 'RNC'
        networkTab.enterSearchValue(controllerValue, false);
        pause(1000);
        networkTab.enterSubmit(false);
        // assertTrue("Can't open Event Analysis window",
        // selenium.isTextPresent(accessAreaValue +
        // " - Access Area Event Analysis"));
        waitForPageLoadingToComplete();
    }

    /*
     * REQ - Test Case - 4.8.20 Open Default View for Node Group (APN
     * Group)
     */
    @Test
    public void openDefaultViewForNodeGroupAPN_4_8_20() throws Exception {
        // Action 1
        networkTab.openTab();
        pause(1000);

        // Action 2
        networkTab.setSearchType(NetworkType.APN_GROUP);
        final String searchGroupValue = reservedDataHelper.getCommonReservedData(CommonDataType.APN_GROUP);
        pause(5000);

        // Action 3
        networkTab.enterSearchValue(searchGroupValue, true);
        pause(4000);
        networkTab.enterSubmit(true);
        waitForPageLoadingToComplete();
        // assertTrue("Can't open APN Group Event Analysis window",
        // selenium.isTextPresent(searchGroupValue +
        // " - APN Group Event Analysis"));
        // assertTrue("Error occured in page loading",
        // selenium.waitForPageLoadingToComplete());
        // pause(4000);
    }

    /*
     * REQ - Test Case - 4.8.20b Open Default View for Node Group
     * (Controller Group)
     */
    @Test
    public void openDefaultViewForNodeGroupController_4_8_20b() throws Exception {
        // Action 1
        networkTab.openTab();
        pause(1000);

        // Action 2
        networkTab.setSearchType(NetworkType.CONTROLLER_GROUP);
        final String searchGroupValue = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP);
        pause(5000);

        // Action 3
        networkTab.enterSearchValue(searchGroupValue, true);
        pause(4000);
        networkTab.enterSubmit(true);
        waitForPageLoadingToComplete();
    }

    /*
     * REQ - Test Case - 4.8.21
     */
    @Test
    public void viewKPIDrillDownsForAnAPN_4_8_21() throws Exception {

        final String searchValue = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.APN);

        selenium.setSpeed("2000");
        System.out.println("search value is " + searchValue);
        setSearchType(NetworkType.APN); // Puts APN into the select input dropdown menu
        enterValueAndClickSubmitButton(searchValue, false);
        assertTrue("Can't open APN Event Analysis window", selenium.isTextPresent("APN - Event Analysis"));

        // Action 3: "KPI Analysis by SGSN-MME view
        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
        assertTrue("Can't open KPI Analysis By SGSN window", selenium.isTextPresent("KPI Analysis By SGSN-MME"));

//        // Action 4: "KPI Analysis by Controller view
//        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
//        assertTrue("Can't open KPI Analysis By Controller window", selenium.isTextPresent("KPI Analysis By Controller"));
//
//        // Action 5: "KPI Analysis by Access Area view
//        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
//        assertTrue("Can't open KPI Analysis By Access Area window", selenium
//                .isTextPresent("KPI Analysis By Access Area"));
//
//        // Action 6: "KPI Analysis by Cause Code view
//        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
//        assertTrue("Can't open KPI Analysis By Cause Code window", selenium.isTextPresent("KPI Analysis By Cause Code"));
//
//        // Action 7: "KPI Analysis by Event Analysis view
//        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
//        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /*
     * REQ - Test Case - 4.8.22
     */
    @Test
    public void viewKPIDrillDownsForSGSN_4_8_22() throws Exception {
        // Action 1
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN);
        setSearchType(NetworkType.SGSN_MME);
        enterValueAndClickSubmitButton(searchValue, false);

        // Action3: KPI Analysis by Controller view
        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
        assertTrue("Can't open KPI Analysis By Controller window", selenium.isTextPresent("KPI Analysis By Controller"));

        // Action4: KPI Analysis by Access Area view
        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
        assertTrue("Can't open KPI Analysis By Controller window", selenium
                .isTextPresent("KPI Analysis By Access Area"));

        // Action5: "KPI Analysis by Cause Code view
        sortAndClickTableDataAtFirstRow("Failures", "Success Ratio");
        assertTrue("Can't open KPI Analysis By Controller window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        // "Occurrences"
        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Analysis By Controller window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /*
     * REQ - Test Case - 4.8.23
     */
    @Test
    public void viewOnKPIDrillDownsForControllerGroup_4_8_23() throws Exception {

        // Action 1
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER);
        setSearchType(NetworkType.CONTROLLER);
        enterValueAndClickSubmitButton(searchValue, false);
        assertTrue("Can't open Controller Event Analysis window", selenium.isTextPresent(searchValue
                + " - Controller Event Analysis"));

        // Action 3: KPI Analysis by Controller view
        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Access Area window", selenium
                .isTextPresent("KPI Analysis By Access Area"));

        // Action 4: KPI Analysis by Access Area view
        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Cause Code window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        // Action 5: "Occurrences Access"
        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /*
     * REQ - Test Case - 4.8.24
     */
    @Test
    public void viewKPIDrillDownsForAccessArea_4_8_24() throws Exception {

        // Action 1
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA);
        setSearchType(NetworkType.ACCESS_AREA);
        enterValueAndClickSubmitButton(searchValue, false);
        assertTrue("Can't open Access Area Event Analysis window", selenium.isTextPresent(searchValue
                + " - Access Area Event Analysis"));

        // Action 3: KPI Analysis by Controller view
        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open Access Area Event Analysis window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        // Action 4: "KPI Event Analysis"
        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open Access Area Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /*
     * REQ - Test Case - 4.8.25
     */
    //    @Test
    public void viewKPIDrillDownsForAPNGroup_4_8_25() throws Exception {

    }

    /*
     * REQ - Test Case - 4.8.26
     */
    //@Test
    public void searchBarCombinedForSingleSearchAndGroupSearch_4_8_26() {

    }

    /*
     * REQ - Test Case - 4.8.27
     */
    @Test
    public void display3GEventsForNetworkAnalysis_4_8_27() throws Exception {
    	selenium.setSpeed("2000");
    	networkTab.openTab();
    	final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN);
        setSearchType(NetworkType.SGSN_MME);
        networkTab.enterSearchValue(searchValue, false);
        selenium.click("//*[@class='x-btn-text ']");
		selenium.mouseOver("//*[@id='NETWORK_EVENT_ANALYSIS_PARENT']");
		selenium.click("//*[@id='NETWORK_EVENT_ANALYSIS']");
		eventAnalysisWindow.setTimeRange(TimeRange.ONE_WEEK);
		eventAnalysisWindow.clickTableCell(0, "Failures");
    }

    /*
     * REQ - Test Case - 4.8.28
     */
    @Test
    public void viewKPIDrillDownsForAnIMSIgroup_4_8_28() throws Exception {
    	selenium.setSpeed("2000");
    	networkTab.openTab();
    	final String searchValue = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.APN);
        setSearchType(NetworkType.APN);
        networkTab.enterSearchValue(searchValue, false);
        selenium.click("//*[@class='x-btn-text ']");
		selenium.mouseOver("//*[@id='NETWORK_EVENT_ANALYSIS_PARENT']");
		selenium.click("//*[@id='NETWORK_EVENT_ANALYSIS']");
		eventAnalysisWindow.setTimeRange(TimeRange.ONE_WEEK);
		eventAnalysisWindow.clickTableCell(0, "Failures");
    }

    /*
     * REQ - Test Case - 4.8.29
     */
    //@Test
    public void displayCellIDConnectedToASAC_4_8_29() {
        // No Functionality on UI

    }

    /**
     * Test Case: 4.8.33
     * display failure breakdown view for a selected Access area (Cell/SAC) group and drill cause code 
     * to sub cause code level to Failed events level
     */
    @Test
    public void drillSubCauseCodeToFailedEventsLevelForSelectedAccessAreaGroup_4_8_33() throws Exception {
        drillCauseCodeAnalysisDownToSubCauseCode(reservedDataHelper
                .getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP), NetworkType.ACCESS_AREA_GROUP,
                "Access Area Group", "Access Area");
    }

    /**
     * Test Case: 4.8.34
     */
    @Test
    public void drillSubCauseCodeToFailedEventsLevelForSelectedControllerGroup_4_8_34() throws Exception {
        drillCauseCodeAnalysisDownToSubCauseCode(reservedDataHelper
                .getCommonReservedData(CommonDataType.CONTROLLER_GROUP), NetworkType.CONTROLLER_GROUP,
                "Controller Group", "Controller");
    }

    /**
     * Test Case: 4.8.35
     */
    @Test
    public void drillSubCauseCodeToFailedEventsLevelForSelectedSGSNGroup_4_8_35() throws Exception {
        drillCauseCodeAnalysisDownToSubCauseCode(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN_GROUP),
                NetworkType.SGSN_MME_GROUP, "SGSN Group", "SGSN");
    }

    /**
     * Test Case: 4.8.36
     */
    @Test
    public void drilldownKPIForSelectedSGSNGroup_4_8_36() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN_GROUP);
        setSearchType(NetworkType.SGSN_MME_GROUP);
        enterValueAndClickSubmitButton(searchValue, true);
        assertTrue("Can't open SGSN Group Event Analysis window", selenium.isTextPresent("SGSN Group Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open SGSN Event Analysis window", selenium.isTextPresent("SGSN Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Controller window", selenium.isTextPresent("KPI Analysis By Controller"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Access Area window", selenium
                .isTextPresent("KPI Analysis By Access Area"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Cause Code window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /**
     * Test Case: 4.8.37
     */
    @Test
    public void drilldownKPIForSelectedControllerGroup_4_8_37() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP);
        setSearchType(NetworkType.CONTROLLER_GROUP);
        enterValueAndClickSubmitButton(searchValue, true);
        assertTrue("Can't open Controller Group Event Analysis window", selenium
                .isTextPresent("Controller Group Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open Controller Event Analysis window", selenium.isTextPresent("Controller Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Access Area window", selenium
                .isTextPresent("KPI Analysis By Access Area"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Cause Code window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));
    }

    /**
     * Test Case: 4.8.38
     */
    @Test
    public void drilldownKPIForSelectedAccessAreaGroup_4_8_38() throws Exception {
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP);
        setSearchType(NetworkType.ACCESS_AREA_GROUP);
        enterValueAndClickSubmitButton(searchValue, true);
        assertTrue("Can't open Access Area Group Event Analysis window", selenium
                .isTextPresent("Access Area Group Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Access Area window", selenium
                .isTextPresent("Access Area Event Analysis"));

        sortAndClickTableDataAtFirstRow("Failures", "KPI");
        assertTrue("Can't open KPI Analysis By Cause Code window", selenium.isTextPresent("KPI Analysis By Cause Code"));

        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Event Analysis window", selenium.isTextPresent("KPI Event Analysis"));

    }

    ///////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void openCauseCodeAnalysisWindow() throws PopUpException {
        networkTab.openTab();
        networkTab.openStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS);
        assertTrue("Can't open Cause Code Analysis window", selenium.isTextPresent("Cause Code Analysis"));
    }

    /**
     * @param type
     */
    private void setSearchType(final NetworkType type) {
        networkTab.openTab();
        pause(3000);
        networkTab.setSearchType(type);
        pause(5000);
    }

    private void enterValueAndClickSubmitButton(final String searchValue, final boolean group) throws PopUpException {
        networkTab.enterSearchValue(searchValue, group);
        logger.info("enter a value: " + searchValue);
        pause(2000);
        networkTab.enterSubmit(group);
        waitForPageLoadingToComplete();
    }

    private void sortAndClickTableDataAtFirstRow(final String sortBy, final String columnHeader)
            throws NoDataException, PopUpException {
        eventAnalysisWindow.sortTable(SortType.DESCENDING, sortBy);
        System.out.println("sorted descending");//jj
        pause(2000);
        eventAnalysisWindow.clickTableCell(0, columnHeader);
        waitForPageLoadingToComplete();
    }

    private void drillCauseCodeAnalysisDownToSubCauseCode(final String accessAreaGroup, final NetworkType type,
            final String groupCauseCodeAnalysis, final String subCauseCodeAnalysis) throws PopUpException,
            NoDataException {
        networkTab.openTab();
        networkTab.setSearchType(type);
        pause(3000);
        networkTab.enterSearchValue(accessAreaGroup, true);
        pause(3000);
        networkTab.openStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS);
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + groupCauseCodeAnalysis + " Cause Code Analysis window", selenium
                .isTextPresent(groupCauseCodeAnalysis + " Cause Code Analysis"));
        causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
        waitForPageLoadingToComplete();
        causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + subCauseCodeAnalysis + " Sub Cause Code Analysis window", selenium
                .isTextPresent(subCauseCodeAnalysis + " Sub Cause Code Analysis"));
        selenium.refresh();
        pause(2000);
    }
    

}
