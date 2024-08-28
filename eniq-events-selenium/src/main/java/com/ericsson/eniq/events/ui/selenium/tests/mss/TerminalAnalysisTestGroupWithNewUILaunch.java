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
 * @since 2011           
*/

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.TerminalTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
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


public class TerminalAnalysisTestGroupWithNewUILaunch extends EniqEventsUIBaseSeleniumTest {

    public static final String CORE_TERMINAL_EVENT_ANALYSIS = "Core Terminal Event Analysis";
    public static final String CORE_CS = "Core CS";

    @Autowired
    private TerminalTabUI terminalTab;

    @Autowired
    private Selection selection;

    @Autowired
    @Qualifier("terminalEventAnalysisForMSS")
    private CommonWindow terminalEventAnalysisWindow;

    @Autowired
    @Qualifier("KPIWindow")
    private CommonWindow kpiWindow;

    @BeforeClass
    public static void openLog() {

        logger.info("ENIQ Version:" + PropertyReader.getInstance().getEniqVersion());
        logger.log(Level.INFO, "Start of Terminal test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Terminal test section");
    }

    @Autowired
    private WorkspaceRC workspace;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);

        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_30);
        pause(2000);
    }

    String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";

    final List<String> defaultHeadersOnTerminalEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> eventType = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSORIGINATING,
            GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSTERMINATINGSMSINMSC,
            GuiStringConstants.LOCATIONSERVICES));

    final List<String> defaultContentOnTheKPIView = new ArrayList<String>(Arrays.asList(GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.MS_ORIGINATING_CALL_DROP_RATIO, GuiStringConstants.MS_TERMINATING_CALL_COMPLETION_RATIO,
            GuiStringConstants.MS_TERMINATING_CALL_DROP_RATIO, GuiStringConstants.MS_ORIGINATING_CALL_ATTEMPTS_INTENSITY,
            GuiStringConstants.MS_TERMINATING_CALL_ATTEMPTS_INTENSITY, GuiStringConstants.MS_ORIGINATING_CALL_DROP_INTENSITY,
            GuiStringConstants.MS_TERMINATING_CALL_DROP_INTENSITY, GuiStringConstants.CALL_FORWARDING_SUCCESS_RATIO,
            GuiStringConstants.ROAMING_CALL_SUCCESS_RATIO, GuiStringConstants.CALL_FORWARDING_DROP_RATIO, GuiStringConstants.ROAMING_CALL_DROP_RATIO,
            GuiStringConstants.SMS_ORIGINATING_SUCCESS_RATIO, GuiStringConstants.SMS_TERMINATING_SUCCESS_RATIO,
            GuiStringConstants.MS_ORIGINATING_CALL_COMPLETION_RATIO, GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_COMPLETION_RATIO,
            GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_DROP_RATIO, GuiStringConstants.LOCATION_REQUESTS_SUCCESS_RATIO));

    /**
     * Test Case: 4.8.1 Verify Terminal Analysis can display event summary for selected terminal.
     */
    @Test
    public void TerminalanalysisForSelectedTerminal_8_1_1() throws Exception {
        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MAKE);
        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MODEL);
        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, terminalMake, CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true,
                terminalModel, tac);
        openEventAnalysisWindow(selection);

        final TimeRange timeRange = getTimeRangeFromProperties();

        delayAndSetTimeRange(timeRange, terminalEventAnalysisWindow);

        waitForPageLoadingToComplete();

        terminalEventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).containsAll(eventType);
        final List<Map<String, String>> completeUITableValues = terminalEventAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.TERMINAL_ANALYSIS_ERROR_MESSAGE + " " + terminalMake + " " + terminalModel + " " + tac,
                TerminalAnalysis.eventAnalysisOnTerminal(completeUITableValues, tac, terminalMake, terminalModel, eventType,
                        defaultHeadersOnTerminalEventAnalysisWindow, timeRange.getMiniutes()));
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        checkWindowUpdatedForTimeRanges("KPI Analysis Voice", kpiWindow);
        kpiWindow.clickConfigureBarAndConfigureItems(defaultContentOnTheKPIView);

    }

    /**
     * Test Case: 4.8.1 Verify Terminal Analysis can display event summary for selected terminal.
     */
    @Test
    public void TerminalanalysisForSelectedTerminal_8_1_2() throws Exception {
        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MAKE);
        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MODEL);
        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, terminalMake, CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true,
                terminalModel, tac);
        openEventAnalysisWindow(selection);

        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange, terminalEventAnalysisWindow);

        terminalEventAnalysisWindow.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).containsAll(eventType);
        final List<Map<String, String>> completeUITableValues = terminalEventAnalysisWindow.getAllTableData();
        assertTrue(DataIntegrityConstants.TERMINAL_ANALYSIS_ERROR_MESSAGE + " " + terminalMake + " " + terminalModel + " " + tac,
                TerminalAnalysis.eventAnalysisOnTerminal(completeUITableValues, tac, terminalMake, terminalModel, eventType,
                        defaultHeadersOnTerminalEventAnalysisWindow, timeRange.getMiniutes()));
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openGridView();
    }

    /**
     * Test Case: 4.8.2 Verify Terminal Analysis statics in event summary for selected terminal.
     */

    @Test
    public void TerminalAnalysisForSelectedTerminal_8_2() throws Exception {
        String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MAKE);
        String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MODEL);
        String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, terminalMake, CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true,
                terminalModel, tac);
        openEventAnalysisWindow(selection);
        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.openTableHeaderMenu(0);
        terminalEventAnalysisWindow.checkInOptionalHeaderCheckBoxes(defaultHeadersOnTerminalEventAnalysisWindow);

    }

    /**
     * Test Case: 4.8.3 Verify by clicking failure, it can be drill into Failed Event Analysis view.
     */
    // sync pass
    @Test
    public void TerminalAnalysisDataForSelectedTerminalFailureDrillDown_8_3() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                getTac());
        openEventAnalysisWindow(selection);
        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        drillDownAllFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSORIGINATING, GuiStringConstants.MSTERMINATING,
                GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.LOCATIONSERVICES);
    }

    /**
     * Test Case: 4.8.4 Verify by clicking Success Ratio, it can be drill into Failed Event Analysis view.
     */
    @Test
    public void TerminalAnalysisDataForSelectedTerminalSuccessRatioDrillDown_8_4() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                getTac());
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        drillDownSuccessRatioOnEventAnalysisWindow(terminalEventAnalysisWindow, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSORIGINATING, GuiStringConstants.MSTERMINATING,
                GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.LOCATIONSERVICES);
    }

    /**
     * Test Case: 4.8.5 Verify by clicking the particular time interval, it can display the analysis statics for selected interval.
     */
    @Test
    public void TerminalAnalysisDataForSelectedInterval_8_5() throws Exception {
        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MAKE);
        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MODEL);
        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                tac);
        openEventAnalysisWindow(selection);
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Terminal", terminalEventAnalysisWindow, tac, terminalMake, terminalModel, null,
                DataIntegrityConstants.TERMINAL_ANALYSIS);
    }

    /**
     * Test Case: 4.8.6 -TC NOT VALID FOR NEW UI Verify by inputing the invalid value; it can show a promotion window.
     */

    /**
     * Test Case: 4.8.7 Verify by clicking the launch button, the KPI voice window can be shown.
     */
    @Test
    public void ThePromptionForInvalidValue_8_7() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                getTac());
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.8.8 Verify on KPI voice window the KPI items can be configured.
     */

    @Test
    public void ThePageButtonForTerminalAnalysis_8_8() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                getTac());
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        kpiWindow.clickConfigureBarAndConfigureItemsForMSS(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.8.9 Verify on KPI voice window the KPI items can be configured.
     */
    @Test
    public void TheInputFieldWithPageNumberForTerminalAnalysis_8_9() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL_GROUP,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, false, null, null);
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();

        kpiWindow.openChartView(defaultContentOnTheKPIView);
        kpiWindow.clickConfigureBarAndConfigureItemsForMSS(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.8.10 Verify Terminal Analysis can display event summary for selected terminal group.
     */
    @Test
    public void TerminalAnalysisStaticsForSelectedTerminalGroup_8_10() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL_GROUP,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, false, null, null);
        openEventAnalysisWindow(selection);

        assertTrue(terminalEventAnalysisWindow.getTableHeaders().containsAll(defaultHeadersOnTerminalEventAnalysisWindow));

        final List<String> tacListForGroups = new ArrayList<String>(Arrays.asList(reservedDataHelper
                .getCommonReservedData(CommonDataType.TERMINAL_GROUP_DATA_MSS)));

        checkWindowUpdatedForTimeRangesWithDataIntegrity("Terminal", terminalEventAnalysisWindow, DataIntegrityConstants.BLANK_SPACE,
                DataIntegrityConstants.BLANK_SPACE, DataIntegrityConstants.BLANK_SPACE, tacListForGroups,
                DataIntegrityConstants.TERMINAL_GROUP_ANALYSIS);
    }

    /**
     * Test Case: 4.8.11 Verify that a set of key performance indicators should be viewed on a selected terminal type.
     */
    @Test
    public void TerminalTypeKPIs_8_11() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL, getMake(), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, true, getModel(),
                getTac());
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.8.12 Verify that a set of key performance indicators should be viewed on a selected terminal group.
     */
    // sync pass
    @Test
    public void TerminalGroupKPIs_8_12_1() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL_GROUP,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, false, null, null);
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openChartView(defaultContentOnTheKPIView);
        checkWindowUpdatedForTimeRanges("KPI Analysis Voice", kpiWindow);
        kpiWindow.clickConfigureBarAndConfigureItems(defaultContentOnTheKPIView);
    }

    /**
     * Test Case: 4.8.12 Verify that a set of key performance indicators should be viewed on a selected terminal group.
     */
    // sync pass
    @Test
    public void TerminalGroupKPIs_8_12_2() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.TERMINAL_GROUP,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS), CORE_TERMINAL_EVENT_ANALYSIS, CORE_CS, false, null, null);
        openEventAnalysisWindow(selection);

        terminalEventAnalysisWindow.setTimeRange(getTimeRangeFromProperties());
        terminalEventAnalysisWindow.clickButton(SelectedButtonType.KPI_BUTTON_MSS);
        waitForPageLoadingToComplete();
        kpiWindow.openGridView();
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

    private void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        terminalTab.openEventAnalysisWindow(selection);

        if (selection.getIsGroup()) {
            assertTrue("Can't open " + selection.getDimensionValue() + " - Terminal Event Analysis",
                    selenium.isTextPresent("Terminal Group - Event Analysis Voice"));
        } else {
            assertTrue("Can't open Terminal Event Analysis", selenium.isTextPresent("Terminal Event Analysis"));
        }
        waitForPageLoadingToComplete();
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
    }

    private void selectOptions(String timeRange, String dimension, String dimensionValue, String windowCategory, String windowOption,
                               boolean isTerminalType, String... values) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
        selection.setIsTerminal(isTerminalType);
        if (values != null) {
            selection.setTerminalModel(values[0]);
            selection.setTac(values[1]);
        }
    }

    private boolean isGroupDimension(String dimension) {
        return dimension.equals(SeleniumConstants.CONTROLLER_GROUP) || dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)
                || dimension.equals(SeleniumConstants.SGSN_MME_GROUP) || dimension.equals(SeleniumConstants.MSC_GROUP)
                || dimension.equals(SeleniumConstants.TERMINAL_GROUP) || dimension.equals(SeleniumConstants.IMSI_GROUP)
                || dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
    }

    private String getMake() {
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MAKE);
    }

    private String getModel() {
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TERMINAL_MODEL);
    }

    private String getTac() {
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
    }

    private void drillDownAllFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        for (final String value : values) {
            if (window.getAllTableDataAtColumn(columnToCheck).contains(value)) {
                drillDownFailuresOnEventAnalysisWindow(window, columnToCheck, value);
                window.clickButton(SelectedButtonType.BACK_BUTTON);
                waitForPageLoadingToComplete();
            }
        }
    }

    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Failures");
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
    }

    private void drillDownSuccessRatioOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck, final String... values)
            throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Success Ratio");
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        final int column = window.getTableHeaders().indexOf("Success Ratio");
        window.clickTableCell(row, column, "gridCellLinkCodeRed");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
    }

    public void checkWindowUpdatedForTimeRangesWithDataIntegrity(final String networkType, final CommonWindow commonWindow, final String tac,
                                                                 final String terminalMake, final String terminalModel,
                                                                 final List<String> tacListForGroups, final String ValidationString)
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
            final String timeLabel = time.getLabel();
            if (!timeLabel.equalsIgnoreCase("5 minutes")) {

                commonWindow.setTimeRange(time);
                delayAndSetTimeRange(time, commonWindow);

                waitForPageLoadingToComplete();
                assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
                if (!ValidationString.equalsIgnoreCase(DataIntegrityConstants.STATUS_NONE)) {
                    final int duration = time.getMiniutes();
                    final List<Map<String, String>> completeUITableValues = commonWindow.getAllTableData();
                    pause(2000);
                    if (ValidationString.equalsIgnoreCase(DataIntegrityConstants.TERMINAL_ANALYSIS)) {
                        assertTrue(DataIntegrityConstants.TERMINAL_ANALYSIS_ERROR_MESSAGE + " " + terminalMake + " " + terminalModel + " " + tac,
                                TerminalAnalysis.eventAnalysisOnTerminal(completeUITableValues, tac, terminalMake, terminalModel, eventType,
                                        defaultHeadersOnTerminalEventAnalysisWindow, duration));
                    } else if (ValidationString.equalsIgnoreCase(DataIntegrityConstants.TERMINAL_GROUP_ANALYSIS)) {
                        assertTrue(DataIntegrityConstants.TERMINAL_GROUP_ANALYSIS_ERROR_MESSAGE + " " + tacListForGroups.toString(),
                                TerminalAnalysis.eventAnalysisOnTerminalGroups(completeUITableValues, tacListForGroups, eventType,
                                        defaultHeadersOnTerminalEventAnalysisWindow, duration));

                    }
                }
            }
        }
    }
}
