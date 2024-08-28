/***------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
---------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.newui;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.*;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.SubscriberTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class NetworkTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private Selection selection;

    public static final String NETWORK_EVENT_ANALYSIS = "Network Event Analysis";
    public static final String EVENT_VOLUME = "Event Volume";
    public static final String CORE_PS = "Core PS";

    public static WebElement element;

    @Autowired
    protected EricssonSelenium selenium;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow eventAnalysisWindow;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    @Qualifier("EventVolumeOverview")
    private CommonWindow EventVolumeOverview;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow apnRankingsWindow;

    @Autowired
    @Qualifier("KPIAnalysis")
    private CommonWindow KPIAnalysis;

    @Autowired
    private SubscriberTabUI network;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Network test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Network test section");
    }

    @Test
    public void viewKPIDrillDownsForAPN_4_8_25() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.APN);

        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.APN,
                searchValue, NETWORK_EVENT_ANALYSIS, CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        assertTrue("Can't open APN Event Analysis window",
                selenium.isTextPresent("APN - Event Analysis"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Controller",
                selenium.isTextPresent("KPI Analysis By SGSN-MME"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Controller",
                selenium.isTextPresent("KPI Analysis By Controller"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Access Area",
                selenium.isTextPresent("KPI Analysis By Access Area"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Cause Code",
                selenium.isTextPresent("KPI Analysis By Cause Code"));
        selenium.waitForPageLoadingToComplete();

        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        selenium.waitForPageLoadingToComplete();
        assertTrue("Can't open KPI Analysis By Controller window",
                selenium.isTextPresent("KPI Event Analysis"));
    }

    @Test
    public void viewKPIDrillDownsForSGSN_4_8_22() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.SGSN);

        selectOptions(SeleniumConstants.DATE_TIME_1DAY,
                SeleniumConstants.SGSN_MME, searchValue,
                NETWORK_EVENT_ANALYSIS, CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        assertTrue("Can't open SGSN-MME Event Analysis window",
                selenium.isTextPresent("SGSN-MME - Event Analysis"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Controller",
                selenium.isTextPresent("KPI Analysis By Controller"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Access Area",
                selenium.isTextPresent("KPI Analysis By Access Area"));
        selenium.waitForPageLoadingToComplete();

        drillDownSuccessRatio(eventAnalysisWindow,
                GuiStringConstants.EVENT_TYPE);
        assertTrue("Can't open KPI Analysis By Cause Code",
                selenium.isTextPresent("KPI Analysis By Cause Code"));
        selenium.waitForPageLoadingToComplete();

        sortAndClickTableDataAtFirstRow("Occurrences", "Occurrences");
        assertTrue("Can't open KPI Analysis By Controller window",
                selenium.isTextPresent("KPI Event Analysis"));
    }

    @Test
    public void display3GEventsForNetworkAnalysis_4_8_27() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.SGSN);
        selectOptions(SeleniumConstants.DATE_TIME_30,
                SeleniumConstants.SGSN_MME, searchValue,
                NETWORK_EVENT_ANALYSIS, CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        checkRankingWindowIsUpdatedForAllTimeRanges("SGSN-MME",
                eventAnalysisWindow);
        drillonFailedEventAnalysis(eventAnalysisWindow, "Failures");
        assertTrue("Can't open SGSN-MME Event Analysis window",
                selenium.isTextPresent("SGSN-MME - Failed Event Analysis"));
        selenium.waitForPageLoadingToComplete();
    }

    @Test
    public void view2GAnd3GDataForSameSession_4_8_28() throws Exception {
        String data = reservedDataHelper
                .getCommonReservedData(CommonDataType.SGSN);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY,
                SeleniumConstants.SGSN_MME, data,
                SgehConstants.CATEGORY_NETWORK_EVENT_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        pause(2000);
        verifyEventAnalysisWindow("Event Analysis", eventAnalysisWindow);
        drillonFailedEventAnalysis(eventAnalysisWindow, "Failures");

        verifyEventAnalysisWindow("Failed Event Analysis", eventAnalysisWindow);
        checkHeadersAreOptionalAndHiddenByDefault(eventAnalysisWindow,
                SgehConstants.OPTION_OTHERS, SgehConstants.GROUP_2GCORE,
                SgehConstants.COLUMN_RAT);
        verify2Gor3GEventData(eventAnalysisWindow);
    }

    @Test
    public void display2GAnd3GEventsForEventVolume_4_8_29() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.SGSN);
        selectOptions(SeleniumConstants.DATE_TIME_Week,
                SeleniumConstants.SGSN_MME, searchValue, EVENT_VOLUME, CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(EventVolumeOverview, "Time");
    }

    @Test
    public void display2GAnd3GEventsForEventVolume_4_8_30() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.CONTROLLER);
        selectOptions(SeleniumConstants.DATE_TIME_Week,
                SeleniumConstants.CONTROLLER, searchValue, EVENT_VOLUME,
                CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(EventVolumeOverview, "Time");
    }

    @Test
    public void display2GAnd3GEventsForEventVolume_4_8_31() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.ACCESS_AREA);
        selectOptions(SeleniumConstants.DATE_TIME_Week,
                SeleniumConstants.ACCESS_AREA, searchValue, EVENT_VOLUME,
                CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(EventVolumeOverview, "Time");
    }

    @Test
    public void display2GAnd3GEventsForEventVolume_4_8_32() throws Exception {
        final String searchValue = reservedDataHelper
                .getCommonReservedData(CommonDataType.APN);
        selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.APN,
                searchValue, EVENT_VOLUME, CORE_PS);
        network.openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(EventVolumeOverview, "Time");
    }

    @Test
    public void drillByApnToViewTheEventsForSameTemporalReference_4_8_33()
            throws Exception {
        openRankingWindowForSgeh(SgehConstants.CATEGORY_CORE_RANKING,
                GuiStringConstants.APN);
        openEventAnalysis(apnRankingsWindow, GuiStringConstants.APN,
                GuiStringConstants.APN);
        selenium.click("//div[@id='btnKPI']");
        pause(5000);
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(KPIAnalysis, "Time");
    }

    @Test
    public void showPerformingNetworkEventVolume_4_8_34() throws Exception {
        openRankingWindowForSgeh(SgehConstants.CATEGORY_NETWORK_EVENT_VOLUME,
                CORE_PS);
        selenium.click("//button[@class='gwt-Button']");
        pause(5000);
        checkTimeFormat(EventVolumeOverview, "Time");
    }

    private void selectOptions(String timeRange, String dimension,
            String dimensionValue, String windowCategory, String windowOption) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
    }

    protected void checkHeadersAreOptionalAndHiddenByDefault(
            final CommonWindow window, final String option,
            String headerCheckBoxGroup, String optionalHeadersToBeChecked) {
        assertFalse(window.getTableHeaders().contains(option));
        window.openTableHeaderMenu(0);
        window.checkInOptionalMenu(option, headerCheckBoxGroup,
                optionalHeadersToBeChecked);
        checkStringListContainsArray(window.getTableHeaders(),
                optionalHeadersToBeChecked);
    }

    private boolean isGroupDimension(String dimension) {
        return dimension.equals(SeleniumConstants.CONTROLLER_GROUP)
                || dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)
                || dimension.equals(SeleniumConstants.SGSN_MME_GROUP)
                || dimension.equals(SeleniumConstants.MSC_GROUP)
                || dimension.equals(SeleniumConstants.TERMINAL_GROUP)
                || dimension.equals(SeleniumConstants.IMSI_GROUP)
                || dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
    }

    private void sortAndClickTableDataAtFirstRow(final String sortBy,
            final String columnHeader) throws NoDataException, PopUpException {
        try {

            eventAnalysisWindow.sortTable(SortType.DESCENDING, sortBy);
            pause(2000);
            eventAnalysisWindow.clickTableCell(0, columnHeader);
            waitForPageLoadingToComplete();
        } catch (Exception e) {
            throw new NoDataException("No Data");
        }
    }

    private void drillonFailedEventAnalysis(final CommonWindow window,
            final String columnToCheck) throws NoDataException, PopUpException {
        final List<Map<String, String>> allTableData = window.getAllTableData();
        if (!(allTableData.isEmpty())) {
            for (int i = 0; i < allTableData.size(); i++) {
                Map<String, String> result = allTableData.get(i);
                if (!result.get("Failures").equals("0")) {
                    window.clickTableCell(i, "Failures");
                    waitForPageLoadingToComplete();
                    break;
                }
            }
        } else {
            throw new NoDataException("No Data");
        }
    }

    private void checkRankingWindowIsUpdatedForAllTimeRanges(
            final String networkType, final CommonWindow commonWindow)
            throws NoDataException, PopUpException {
        assertTrue("Default time range is NOT equal to 30 minutes",
                commonWindow.getTimeRange().equals("30 minutes"));
        for (final TimeRange time : TimeRange.values()) {
            commonWindow.setTimeRange(time);
            assertTrue(time.getLabel()
                    + " is NOT a valid setting for the Time Dialog",
                    selenium.isTextPresent("Time Settings"));
        }
    }

    private void drillDownSuccessRatio(final CommonWindow window,
            final String columnToCheck) throws NoDataException, PopUpException {
        window.sortTable(SortType.ASCENDING, GuiStringConstants.SUCCESS_RATIO);
        try {
            final int column = window.getTableHeaders()
                    .indexOf("Success Ratio");
            window.clickTableCell(0, column, "gridCellLink");
        } catch (Exception e) {
            throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
    }

    private void verifyEventAnalysisWindow(final String view,
            final CommonWindow eventWindow) throws NoDataException,
            PopUpException {
        assertTrue("Can't open window - " + view, selenium.isTextPresent(view));
        logger.log(Level.INFO,
                view + " returns " + eventWindow.getTableRowCount()
                        + " Number of rows.");
    }

    private void openRankingWindowForSgeh(String categoryPanel,
            String windowOption) throws Exception {

        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        pause(2000);
        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
        pause(2000);
    }

    private void openEventAnalysis(final CommonWindow rankingWindow,
            final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        logger.info("In openEventAnanysis()");
        rankingWindow.clickTableCell(0, rankingWindow.getTableHeaders()
                .indexOf(columnName), "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        assertTrue(
                "Can't open " + networktype + " Event Analysis page",
                selenium.isTextPresent(" Event Analysis")
                        && selenium.isTextPresent(networktype));
        logger.info("Out of openEventAnanysis()with networktype =  "
                + networktype);
    }

    private void checkTimeFormat(final CommonWindow window,
            final String columnToCheck) throws NoDataException, PopUpException,
            ParseException {
        final List<Map<String, String>> allTableData = window.getAllTableData();
        System.out.println(allTableData);
        if (!(allTableData.isEmpty())) {
            for (int i = 0; i < allTableData.size(); i++) {
                Map<String, String> result = allTableData.get(i);
                String timeFormat = result.get("Time");
                assertEquals(timeFormat.length(), 19);
                waitForPageLoadingToComplete();
                break;
            }
        } else {
            throw new NoDataException("No Data");
        }
    }
}
