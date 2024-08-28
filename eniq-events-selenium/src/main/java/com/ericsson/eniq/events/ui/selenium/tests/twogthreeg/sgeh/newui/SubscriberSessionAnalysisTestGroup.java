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
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.SubscriberTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.BaseSubscriber;
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
public class SubscriberSessionAnalysisTestGroup extends BaseSubscriber {

    @Autowired
    private Selection selection;

    @Autowired
    private SubscriberTabUI subcriber;

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;

    Date gStartDate = new Date();
    String gStartDateTimeCandidate = "";
    Date gEndDate = new Date();
    String gEndDateTimeCandidate = "";

    @Autowired
    private WorkspaceRC workspace;

    private final List<String> expectedHeadersOnSelectedEventTypeOfFailure = new ArrayList<String>(Arrays.asList("Event Time", "IMSI", "TAC",
            "Terminal Make", "Terminal Model", "Event Type", "Event Result", "Cause Code", "Sub Cause Code", "SGSN-MME", "RAT", "Controller", "Access Area",
            "RAN Vendor", "APN"));

    final List<String> defaultHeadersOnIMSIEventAnalysisWindow = new ArrayList<String>(Arrays.asList("Event Time", "TAC", "Terminal Make",
            "Terminal Model", "Event Type", "Event Result", "Cause Code", "Sub Cause Code", "SGSN-MME", "Controller", "Access Area", "RAN Vendor",
            "APN"));

    final List<String> expectedHeadersOnIMSIGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList("Total", "Success Ratio", "Failures",
            "Successes", "Impacted Subscribers", "Event Type"));

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberSession test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberSession test section");
    }

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

    @Override
    public void tearDown() throws Exception {
        logger.log(Level.INFO, "The Element ID : " + closeButtonXPath);
        while (selenium.isElementPresent(closeButtonXPath))
            selenium.click(closeButtonXPath);
        super.tearDown();
    }

    @Test
    public void imsiSearchEventDetailsSummaryDisplay_4_5_2() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        subscriberEventAnalysis.sortTable(0);
        subscriberEventAnalysis.openTableHeaderMenu(0);
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, SgehConstants.OPTION_OTHERS, SgehConstants.GROUP_2GCORE,
                SgehConstants.COLUMN_PTMSI);
        assertTrue("PTMSI column should be displayed", subscriberEventAnalysis.getTableHeaders().contains("PTMSI"));
    }

    @Test
    public void imsiEventAccessAreaDrillDown_4_5_6() throws Exception {
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller", "Access Area", "Event Type", "Failures",
                "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        setTimeRangeFromTimeConstrants(TimeRange.ONE_DAY);
        waitForPageLoadingToComplete();
        clickTableCellWhereEventResultEqualsReject("Access Area", subscriberEventAnalysis);
        assertTrue("Headers contains expected entries: ", subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeaders));
        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure, subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    @Test
    public void imsiEventControllerDrillDown_4_5_7() throws Exception {
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller", "Event Type", "Failures", "Successes",
                "Total Events", "Success Ratio", "Impacted Subscribers"));
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        setTimeRangeFromTimeConstrants(TimeRange.ONE_DAY);
        waitForPageLoadingToComplete();
        clickTableCellWhereEventResultEqualsReject("Controller", subscriberEventAnalysis);
        assertTrue("Headers contains expected entries: ", subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeaders));
        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure, subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    @Test
    public void imsiEventSGSNDrillDown_4_5_8() throws Exception {
        final List<String> expectedHeadersOnSGSNEventAnalysis = new ArrayList<String>(Arrays.asList("SGSN-MME", "Total Events", "Failures",
                "Successes", "Success Ratio", "Event Type", "Impacted Subscribers"));
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        setTimeRangeFromTimeConstrants(TimeRange.ONE_DAY);
        waitForPageLoadingToComplete();
        clickTableCellWhereEventResultEqualsReject("SGSN-MME", subscriberEventAnalysis);
        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();
        assertTrue(tableHeaders.containsAll(expectedHeadersOnSGSNEventAnalysis));
        assertTrue("Table headers contains expected entries: ", tableHeaders.size() == expectedHeadersOnSGSNEventAnalysis.size());
        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure, subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    @Test
    public void imsiEventAPNDrillDown_4_5_9() throws Exception {
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList( "APN", "Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();
        clickTableCellWhereEventResultEqualsReject("APN", subscriberEventAnalysis);
        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();
        assertEquals("Table headers are not matching.\n", expectedHeaders, subscriberEventAnalysis.getTableHeaders());
        assertTrue("Table headers contains unexpected entries: ", tableHeaders.size() == expectedHeaders.size());
        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure, subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    @Test
    public void imsiEventTACDrillDown_4_5_10_() throws Exception {
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("Manufacturer", "Model", "TAC", "Event Type", "Failures",
                "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_1DAY, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        setTimeRangeFromTimeConstrants(TimeRange.ONE_DAY);
        waitForPageLoadingToComplete();
        clickTableCellWhereEventResultEqualsReject("TAC", subscriberEventAnalysis);
        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();
        assertTrue(tableHeaders.containsAll(expectedHeaders));
        assertTrue("Table headers contains expected entries: ", tableHeaders.size() == expectedHeaders.size());
        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure, subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    @Test
    public void openDefaultViewForIMSI_4_5_11() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.IMSI, imsi, SgehConstants.CATEGORY_CORE_EVENT_TRACE,
                SgehConstants.WINDOW_OPTION_CORE_PS);
        subcriber.openEventAnalysisWindowForSGEH(selection);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
    }

    private void clickTableCellWhereEventResultEqualsReject(final String tableHeader, final CommonWindow subscriberEventAnalysis)
            throws NoDataException, PopUpException {
        final int row = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue("Event Result", "REJECT");
        final Map<String, String> expect = subscriberEventAnalysis.getAllDataAtTableRow(row);
        subscriberEventAnalysis.clickTableCell(row, tableHeader);
        waitForPageLoadingToComplete();
        //take the first row's value as tableHeader's value is same at all row  
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(0);
        assertEquals(expect.get(tableHeader), result.get(tableHeader));
    }

    private void validateByClickingEachFailurelinkInTableRow(final List<String> tableHeadersAtNewlyOpenedTable,
                                                             final CommonWindow subscriberEventAnalysis, final ImsiNumber imsi)
                                                                     throws NoDataException, PopUpException {
        final List<Map<String, String>> allTableData = subscriberEventAnalysis.getAllTableData();
        for (int i = 0; i < allTableData.size(); i++) {
            Map<String, String> result = allTableData.get(i);
            if (!result.get("Failures").equals("0")) {
                subscriberEventAnalysis.clickTableCell(i, "Failures");
                waitForPageLoadingToComplete();
                if(!subscriberEventAnalysis.getTableHeaders().contains("RAT")){
                checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, SgehConstants.OPTION_OTHERS, SgehConstants.GROUP_2GCORE,
                        SgehConstants.COLUMN_RAT);
                }
                assertEquals("Table headers are not matching.\n", tableHeadersAtNewlyOpenedTable, subscriberEventAnalysis.getTableHeaders());
                subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
                pause(1000);
            }
        }
    }

    private void selectOptions(String timeRange, String dimension, String dimensionValue, String windowCategory, String windowOption) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
    }

    private boolean isGroupDimension(String dimension) {
        return dimension.equals(SeleniumConstants.CONTROLLER_GROUP) || dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)
                || dimension.equals(SeleniumConstants.SGSN_MME_GROUP) || dimension.equals(SeleniumConstants.MSC_GROUP)
                || dimension.equals(SeleniumConstants.TERMINAL_GROUP) || dimension.equals(SeleniumConstants.IMSI_GROUP)
                || dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
    }

    protected void checkHeadersAreOptionalAndHiddenByDefault(final CommonWindow window, final String option, String headerCheckBoxGroup,
                                                             String optionalHeadersToBeChecked) {
        assertFalse(window.getTableHeaders().contains(option));
        window.openTableHeaderMenu(0);
        window.checkInOptionalMenu(option, headerCheckBoxGroup, optionalHeadersToBeChecked);
        checkStringListContainsArray(window.getTableHeaders(), optionalHeadersToBeChecked);
    }

    private void setTimeRangeFromTimeConstrants(TimeRange time) {
        try {
            delayAndSetTimeRange(time);
        } catch (PopUpException e) {
            e.printStackTrace();
        }
        Date startDate = gStartDate;
        String startDateTimeCandidate = gStartDateTimeCandidate;
        Date endDate = gEndDate;
        String endDateTimeCandidate = gEndDateTimeCandidate;
        try {
            subscriberEventAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));
        } catch (PopUpException e) {
            e.printStackTrace();
        }
    }

    public void delayAndSetTimeRange(final TimeRange timeRange) throws PopUpException {
        final Calendar cal = Calendar.getInstance();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat AMPMFormatter = new SimpleDateFormat("a");
        final Formatter startDatefmt = new Formatter();
        final Formatter endDatefmt = new Formatter();
        int offset = 0;
        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }
        cal.set(Calendar.SECOND, 0);
        final Date currentDate = cal.getTime();
        logger.log(Level.INFO, "Current Date : " + currentDate);
        final long currentTime = currentDate.getTime();
        if (timeRange == TimeRange.FIFTEEN_MINUTES || timeRange == TimeRange.THIRTY_MINUTES || timeRange == TimeRange.ONE_HOUR
                || timeRange == TimeRange.TWO_HOURS) {
            offset = 10;
        } else {
            offset = 30;
        }
        final int timeRangeInMins = timeRange.getMiniutes();
        long endDateTime = currentTime - (offset * 60 * 1000);
        cal.setTimeInMillis(endDateTime);
        Date endDate = cal.getTime();
        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));
        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }
        cal.setTimeInMillis(endDateTime);
        endDate = cal.getTime();
        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);
        cal.setTimeInMillis(startDateTime);
        Date startDate = cal.getTime();
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
        String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_" + minFormatter.format(endDate.getTime());
        gStartDate = startDate;
        gStartDateTimeCandidate = startDateTimeCandidate.toUpperCase().replace(".", "");
        gEndDate = endDate;
        gEndDateTimeCandidate = endDateTimeCandidate.toUpperCase().replace(".", "");
        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate + " " + startDateTimeCandidate
                + " and End Date Time Candidate : " + endDate + " " + endDateTimeCandidate);
    }
}
