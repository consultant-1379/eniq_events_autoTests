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

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import java.util.logging.Level;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.TerminalChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.*;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.SubscriberTabUI;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.TerminalTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.events.windows.TerminalGroupAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.mss.DataIntegrityConstants;
import com.ericsson.eniq.events.ui.selenium.tests.mss.TerminalAnalysis;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.GroupTerminalAnalysisViewType;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class TerminalTestGroup extends EniqEventsUIBaseSeleniumTest {

    private static final String TEXT_TERMINAL_ANALYSIS_MOST_FREQUENT_SIGNALING_SUMMARY = "Terminal Analysis - Most Frequent Signaling Summary";
    private static final String TEXT_GROUP_ANALYSIS = "Group Analysis";
    private static final String TEXT_TERMINAL_GROUP_ANALYSIS = "Terminal Group Analysis";
    private static final List<String> EXPECTED_HEADERS_ON_SGSN_EVENT_ANALYSIS = new ArrayList<String>(Arrays.asList("Rank", "TAC", "Manufacturer",
            "Model", "Failures", "Successes", "Total Events", "Success Ratio"));

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    private SubscriberTabUI subcriber;

    @Autowired
    private TerminalTabUI terminalTabUI;

    @Autowired
    private Selection selection;

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

    final List<String> defaultHeadersOnTerminalEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> eventType = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC, GuiStringConstants.MSORIGINATING,
            GuiStringConstants.MSTERMINATING, GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.MSTERMINATINGSMSINMSC,
            GuiStringConstants.LOCATIONSERVICES));

    @Test
    public void displayEventSummaryForSelectedDeviceType_4_9_2() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.TERMINAL, getMake(), SgehConstants.CATEGORY_CORE_TERMINAL_EVENT_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, getModel(), getTac());
        openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Terminal - Event Analysis page", selenium.isTextPresent("Terminal - Event Analysis"));
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Failures");
    }

    @Test
    public void displayEventSummaryForSelectedDeviceTypeGroup_4_9_3() throws Exception {
        final String FAILURES = "Failures";
        List<String> optionalHeaders = new ArrayList<String>(Arrays.asList("Event Type"));
        String data = reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.TERMINAL_GROUP, data, SgehConstants.CATEGORY_CORE_TERMINAL_EVENT_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, false, null, null);
        openEventAnalysisWindowForSGEH(selection);
        selenium.waitForPageLoadingToComplete();
        terminalEventAnalysisWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, FAILURES);
        pause(3000);
        assertEquals("Table headers are not matching.\n", defaultHeadersOnTerminalEventAnalysisWindow, terminalEventAnalysisWindow.getTableHeaders());
        drillDownFailuresOnEventAnalysisWindow(terminalEventAnalysisWindow, "Failures");
    }

    @Test
    public void viewMostFrequentSignallingTerminalsForSelectedTimeInterval_4_9_4() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Terminal Analysis - Most Frequent Signaling Summary page",
                selenium.isTextPresent(TEXT_TERMINAL_ANALYSIS_MOST_FREQUENT_SIGNALING_SUMMARY));
        varifyTableHeaders(EXPECTED_HEADERS_ON_SGSN_EVENT_ANALYSIS);
    }

    @Test
    public void viewEventSummaryForMostFrequentSignallingTerminalsForSelectedTimeInterval_4_9_6() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_POPULAR_EVENT_SUMMARY);
        assertTrue("Can't open Terminal Analysis - Most Frequent Signaling Summary page",
                selenium.isTextPresent("Terminal Analysis - Most Frequent Signaling Summary"));
        waitForPageLoadingToComplete();
    }

    @Test
    public void viewDeviceWhichExperienceMostAttachFailures_4_9_12() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Terminal Analysis - Most Frequent Signaling Summary page",
                selenium.isTextPresent(TEXT_TERMINAL_ANALYSIS_MOST_FREQUENT_SIGNALING_SUMMARY));
        varifyTableHeaders(EXPECTED_HEADERS_ON_SGSN_EVENT_ANALYSIS);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_ATTACH_FAILURES);
        assertTrue("Can't open Terminal Analysis - Most Attach Failures page", selenium.isTextPresent("Terminal Analysis - Most Attach Failures"));
        drillDownFailuresOnAnalysisWindow(terminalAnalysisWindow, "Failures");
    }

    @Test
    public void viewTerminalGroupsWhichExperienceMostAttachFailures_4_9_13() throws Exception {
        String toggleGraphToGridXPath = "//table[@id='btnToggleToGrid']//button";
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_GROUP_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Group Analysis", selenium.isTextPresent(TEXT_GROUP_ANALYSIS));
        waitForPageLoadingToComplete();
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_ATTACH_FAILURES);
        waitForPageLoadingToComplete();
        if (terminalAnalysisWindow.isButtonEnabled(SelectedButtonType.TOGGLE_BUTTON)) {
            terminalAnalysisWindow.toggleGraphToGrid();
        } else {
            throw new NoDataException("NO data");
        }
        assertTrue("Can't open Group Analysis - Most Attach Failures page", selenium.isTextPresent("Group Analysis - Most Attach Failures"));
        waitForPageLoadingToComplete();
    }

    @Test
    public void viewDeviceWhichExperienceMostPDPFailures_4_9_14() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Terminal Analysis - Most Frequent Signaling Summary page",
                selenium.isTextPresent(TEXT_TERMINAL_ANALYSIS_MOST_FREQUENT_SIGNALING_SUMMARY));
        waitForPageLoadingToComplete();
        varifyTableHeaders(EXPECTED_HEADERS_ON_SGSN_EVENT_ANALYSIS);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_PDP_SESSION_SETUP_FAILURES);
        assertTrue("Can't open Terminal Analysis - Most PDP Session Setup Failures page",
                selenium.isTextPresent("Terminal Analysis - Most PDP Session Setup Failures"));
        waitForPageLoadingToComplete();
        drillDownFailuresOnAnalysisWindow(terminalAnalysisWindow, "Failures");
        waitForPageLoadingToComplete();
    }

    @Test
    public void viewDeviceWhichExperienceMostPDPFailuresGroup_4_9_15() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_GROUP_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue(selenium.isTextPresent(TEXT_TERMINAL_GROUP_ANALYSIS));
        waitForPageLoadingToComplete();
        terminalGroupAnalysis.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_PDP_SESSION_SETUP_FAILURES);
        waitForPageLoadingToComplete();
        assertTrue("Can't open Group Analysis", selenium.isTextPresent(TEXT_GROUP_ANALYSIS));
        waitForPageLoadingToComplete();
        String drillableBarChartObject = "//div[@id='selenium_tag_baseWindow']//div[starts-with(@id,'highcharts')]/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-tracker')]/*[name()='g']/*[name()='rect']";
        try {
            selenium.mouseOver(drillableBarChartObject);
            selenium.click(drillableBarChartObject);
        } catch (Exception e) {
            throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
    }

    @Test
    public void viewTerminalWhichHasTheMostMobilityFailures_4_9_16() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue("Can't open Terminal Analysis - Most Frequent Signaling Summary page",
                selenium.isTextPresent(TEXT_TERMINAL_ANALYSIS_MOST_FREQUENT_SIGNALING_SUMMARY));
        varifyTableHeaders(EXPECTED_HEADERS_ON_SGSN_EVENT_ANALYSIS);
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_MOBILITY_ISSUES);
        assertTrue("Can't open Terminal Analysis - Most Mobility Failure", selenium.isTextPresent("Terminal Analysis - Most Mobility Failure"));
        drillDownFailuresOnAnalysisWindow(terminalAnalysisWindow, "Failures");
        waitForPageLoadingToComplete();
    }

    @Test
    public void viewDeviceGroupsWhichExperienceMostMobilityIssues_4_9_17() throws Exception {
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.CORE_NETWORK_PS, null, SgehConstants.CATEGORY_CORE_TERMINAL_GROUP_ANALYSIS,
                SgehConstants.WINDOW_OPTION_CORE_PS, true, null, null);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue(selenium.isTextPresent(TEXT_GROUP_ANALYSIS));
        terminalAnalysisWindow.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_MOBILITY_ISSUES);
        String drillableBarChartObject = "//div[@id='selenium_tag_baseWindow']//div[starts-with(@id,'highcharts')]/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-tracker')]/*[name()='g']/*[name()='rect']";
        try {
            selenium.mouseOver(drillableBarChartObject);
            selenium.click(drillableBarChartObject);
        } catch (Exception e) {
            throw new NoDataException("No Data");
        }
        waitForPageLoadingToComplete();
    }

    private void varifyTableHeaders(final List<String> expectedHeaders) {
        final List<String> tableHeaders = terminalAnalysisWindow.getTableHeaders();
        assertTrue("Table headers not contains expected entries", tableHeaders.containsAll(expectedHeaders));
    }

    private void drillDownFailuresOnAnalysisWindow(final CommonWindow window, final String columnToCheck) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, columnToCheck);
        final List<Map<String, String>> allTableData = terminalAnalysisWindow.getAllTableData();
        if (!(allTableData.isEmpty())) {
            for (int i = 0; i < allTableData.size(); i++) {
                Map<String, String> result = allTableData.get(i);
                if (!result.get("Failures").equals("0")) {
                    terminalAnalysisWindow.clickTableCell(i, "Failures");
                    waitForPageLoadingToComplete();
                    assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
                    break;
                }
            }
        } else {
            throw new NoDataException("No Data");
        }
    }

    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, columnToCheck);
        final List<Map<String, String>> allTableData = terminalEventAnalysisWindow.getAllTableData();
        if (!(allTableData.isEmpty())) {
            for (int i = 0; i < allTableData.size(); i++) {
                Map<String, String> result = allTableData.get(i);
                if (!result.get("Failures").equals("0")) {
                    terminalEventAnalysisWindow.clickTableCell(i, "Failures");
                    waitForPageLoadingToComplete();
                    assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
                    break;
                }
            }
        } else {
            throw new NoDataException("No Data");
        }
    }

    private Exception NoDataException(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    private void verifyTerminalAnalysisWindow(final String view, final CommonWindow terminalWindow) throws NoDataException, PopUpException {
        if (terminalWindow.equals(terminalGroupAnalysis)) {
            terminalWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        }
        pause(1000);
        assertTrue("Can't open window - " + view, selenium.isTextPresent(view));
        logger.log(Level.INFO, view + " returns " + terminalWindow.getTableRowCount() + " Number of rows.");
        pause(1000);
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
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TERMINAL_MAKE);
    }

    private String getModel() {
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TERMINAL_MODEL);
    }

    private String getTac() {
        return reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC);
    }

    private void openEventAnalysisWindowForSGEH(final Selection selection) throws PopUpException, InterruptedException {
        terminalTabUI.openEventAnalysisWindow(selection);
        if (selection.getIsGroup()) {
            assertTrue("Can't open " + selection.getDimensionValue() + "- Terminal Event Analysis", selenium.isTextPresent("Terminal Event Analysis"));
        } else {
            assertTrue("Can't open Terminal Event Analysis", selenium.isTextPresent("Terminal Event Analysis"));
        }
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
                delayAndSetTimeRange(time, commonWindow);
                waitForPageLoadingToComplete();
                assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
                if (!ValidationString.equalsIgnoreCase(DataIntegrityConstants.STATUS_NONE)) {
                    final int duration = time.getMiniutes();
                    final List<Map<String, String>> completeUITableValues = commonWindow.getAllTableData();
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
        if (timeRange == TimeRange.ONE_DAY) {
            endDateTime = endDateTime - (TimeRange.ONE_DAY.getMiniutes() * 60 * 1000);
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
}
