package com.ericsson.eniq.events.ui.selenium.tests.ltecfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;

public class SubscriberSessionTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    private WorkspaceRC workspacerc;

    @Autowired
    @Qualifier("subscriberEventAnalysisForLTECFA")
    private CommonWindow subscriberCallFailureAnalysis;

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    final List<String> defaultHeadersOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfRrcConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.GUMMEI_Type_Desc, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfS1SigConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfInitialCtxtSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.DURATION, GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR,
            GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfErabSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfUeCntxtReleaseOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.INTERNAL_RELEASE_CAUSE, GuiStringConstants.TRIGGERING_NODE,
            GuiStringConstants.ERAB_DATA_LOST_BITMAP, GuiStringConstants.ERAB_DATA_LOST, GuiStringConstants.ERAB_RELEASE_SUCCESS,
            GuiStringConstants.NUMBER_OF_FAILED_ERABS, GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.TTI_Bundling_Mode_Desc));

    final List<String> optionalHeadersOfCallFailureAnalysisERABWindow = new ArrayList<String>(Arrays.asList("Setup Attempted Access Type",
            "Setup Successful Access Type", "Setup Failure 3GPP Cause Group"));

    final List<String> optionalHeadersOfCallFailureAnalysisERABWindowForUEctxtRel = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.ERAB_DATA_LOST, GuiStringConstants.ERAB_RELEASE_SUCCESS));

    final List<String> listOfEvents = new ArrayList<String>(Arrays.asList(GuiStringConstants.RRC_CONN_SETUP, GuiStringConstants.S1_SIG_CONN_SETUP,
            GuiStringConstants.ERAB_SETUP, GuiStringConstants.INITIAL_CTXT_SETUP, GuiStringConstants.UE_CTXT_RELEASE));

    final List<String> headersToTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.VENDOR, GuiStringConstants.NUMBER_OF_ERABS, GuiStringConstants.RRC_ESTABL_CAUSE,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR,
            GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.GROUP_NAME, "Setup Request PCI", "Setup Request ARP", "Setup Result", "Setup Successful Access Type",
            "Failure 3GPP Cause", "Setup Attempted Access Type", "Setup Failure 3GPP Cause Group", "Access Area", "Terminal Model", "Event Type",
            "Setup Request QCI", "Setup Request PVI"));

    final List<String> headersToUnTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC));

    final List<String> optionalHeadersOfSummaryWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.CONTROLLER,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA));

    final List<String> optionalHeadersToUntickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC));

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
        workspacerc.checkAndOpenSideLaunchBar();

        pause(2000);
        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: 105 65-0582/00228 Test Case: 5.1 and 5.2 Description: To verify that it shall be possible to drill-down into eNodeB info.
     */
    @Test
    public void IMSISummaryEventsViewEnodeBdrilldown_5_1_and_5_2() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        drillDownOnFailedAnalysisWindow(subscriberCallFailureAnalysis, GuiStringConstants.CONTROLLER, imsi);
    }

    /**
     * Requirement: 105 65-0582/00228 Test Case: 5.3 and 5.4 Description: To verify that it shall be possible to drill-down into eNodeB info.
     */
    @Test
    public void IMSISummaryEventsViewEcellDrilldown_5_3_and_5_4() throws Exception {

        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        drillDownOnFailedAnalysisWindow(subscriberCallFailureAnalysis, GuiStringConstants.ACCESS_AREA, imsi);
    }

    /**
     * Requirement: 105 65-0582/00231 Test Case: 5.7 Description: To verify that it shall be possible to drill-down into failed events from IMSI event
     * summary window.
     */
    @Test
    public void IMSISummaryEventsViewdrilldown_5_7() throws Exception {

        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(imsi);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        validateDataOfIMSIAnalysisWindowForAllTimeRanges(subscriberCallFailureAnalysis, imsi, true);
    }

    /**
     * Requirement: 105 65-0582/00231 Test Case: 5.7 Description: To verify that it shall be possible to drill-down "Number of ERABs" from IMSI event
     * summary window.
     */
    @Test
    public void IMSISummaryERABdrilldown_5_7() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(imsi);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        drilldownOnNumberOfERABs(subscriberCallFailureAnalysis, imsi);
    }

    /**
     * Requirement: 105 65-0582/00226 Test Case: 5.8 Description: IMSI Analysis - Test to check the Event Summary Analysis window.
     */
    @Test
    public void IMSISummaryEventsView_5_8() throws Exception {

        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(imsi);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        validateDataOfIMSIAnalysisWindowForAllTimeRanges(subscriberCallFailureAnalysis, imsi, false);
    }

    /**
     * Requirement: 105 65-0582/00227 Test Case: 5.9 Description: IMSI Group Analysis - Test to view the LTE RAN events in Summary Event Analysis
     * window .
     */
    @Test
    public void IMSIgroupAnalysisViewLTERANeventsSummaryEventAnalysis_5_9() throws Exception {
        final String imsiGroup = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_GROUP_LTE);

        workspacerc.selectDimension(SeleniumConstants.IMSI_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(imsiGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> imsiGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.IMSI_GROUP_DATA_LTE).split(",")));

        validateDataOfIMSIGroupAnalysisWindowForAllTimeRanges(subscriberCallFailureAnalysis, imsiGroupList, false);
    }

    /**
     * Requirement: 105 65-0582/00232 Test Case: 5.10 Description: Open the Subscriber tab and through IMSI to go to an IMSI Group Event Analysis
     * view. It shall be possible to go by Group Event Type Failure Count to a detailed Failed Group Event Analysis view.
     */
    @Test
    public void IMSIgroupSummaryEventsViewDrillDown_5_10() throws Exception {
        final String imsiGroup = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(imsiGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Call Failure");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();
        final List<String> imsiGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.IMSI_GROUP_DATA_LTE).split(",")));

        validateDataOfIMSIGroupAnalysisWindowForAllTimeRanges(subscriberCallFailureAnalysis, imsiGroupList, true);

    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Drill down one link on column on failed event analysis.
     * @param window
     *            the object of CommonWindow
     * @param columnToCheck
     *            This column to locate row number
     * @param values
     *            These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */

    private void drillDownOnFailedAnalysisWindow(final CommonWindow window, final String drillDownColumn, final String value) throws NoDataException,
            PopUpException {

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        // Drill down on Category
        int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.FAILURE_CATEGORY, GuiStringConstants.CALL_SETUP_FAILURES);
        window.clickTableCell(row, GuiStringConstants.FAILURE_CATEGORY);
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("IMSI - LTE Call Failure Event Analysis"));

        // Drill down on Failures
        window.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();

        // Drill down on Network Element        
        window.clickTableCell(0, drillDownColumn);
        waitForPageLoadingToComplete();

        row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.CATEGORY, GuiStringConstants.CALL_SETUP_FAILURES);
        window.clickTableCell(row, GuiStringConstants.CATEGORY);
        waitForPageLoadingToComplete();

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

            if (time == TimeRange.FIVE_MINUTES) {
                continue;
            }

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            subscriberCallFailureAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfSummaryWindow, GuiStringConstants.EVENT_TYPE);

        }
    }

    private void drilldownOnNumberOfERABs(CommonWindow window, String imsi) throws NoDataException, PopUpException {

        for (int rowNumber = 0; rowNumber < 2; rowNumber++) {
            window.clickTableCell(0, GuiStringConstants.FAILURE_CATEGORY);
            waitForPageLoadingToComplete();

            List<String> listOfEventTypes = new ArrayList<String>();
            listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);
            List<String> listOfOptionalHeaders = new ArrayList<String>();

            int columnNumber = 0;

            // Drill down for all event types
            for (final String eventType : listOfEventTypes) {
                if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP) || eventType.equals(GuiStringConstants.ERAB_SETUP)
                        || eventType.equals(GuiStringConstants.UE_CTXT_RELEASE)) {
                    final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);
                    window.clickTableCell(row, GuiStringConstants.FAILURES);
                    waitForPageLoadingToComplete();
                    assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

                    if (eventType.equals(GuiStringConstants.ERAB_SETUP) || eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP)) {
                        columnNumber = 10;
                        listOfOptionalHeaders = optionalHeadersOfCallFailureAnalysisERABWindow;
                    } else if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE)) {
                        columnNumber = 10;
                        listOfOptionalHeaders = optionalHeadersOfCallFailureAnalysisERABWindowForUEctxtRel;
                    } else {
                        assertTrue("Invalid LTE CFA ERAB Event Type", false);
                        return;
                    }

                    final int uiNumberOfERABs = Integer.parseInt(window.getTableData(0, columnNumber));

                    if ((uiNumberOfERABs == 0)) {
                        logger.log(Level.INFO, "ERABs are not available for event type " + eventType);
                        window.clickBackwardNavigation();
                        continue;
                    }

                    window.clickTableCell(0, GuiStringConstants.NUMBER_OF_ERABS);
                    waitForPageLoadingToComplete();
                    assertTrue("Failed to open Call Failure Analysis ERABs window", selenium.isTextPresent("Call Failure ERABs Event Analysis"));

                    window.openTableHeaderMenu(0);
                    window.checkInOptionalHeaderCheckBoxes(listOfOptionalHeaders);

                    window.openTableHeaderMenu(0);
                    window.checkInOptionalHeaderCheckBoxes(listOfOptionalHeaders);

                    window.clickBackwardNavigation();
                    window.clickBackwardNavigation();
                }
            }
            window.clickBackwardNavigation();
        }
    }

    private void drillDownFailuresOnIMSIEventAnalysisWindow(final CommonWindow window, String imsi, int timeRangeInMinutes) throws NoDataException,
            PopUpException, ParseException {

        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);


            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();
            assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

            checkInOptionalHeadersLTECFA(window, eventType);

            window.clickBackwardNavigation();
        }
    }

    private void validateDataOfIMSIAnalysisWindowForAllTimeRanges(final CommonWindow commonWindow, String imsi, boolean drillDownImsiAnalysisWindow)
            throws NoDataException, PopUpException, ParseException {

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
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

            if (time == TimeRange.FIVE_MINUTES) {
                continue;
            }

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            subscriberCallFailureAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<String> categoryList = commonWindow.getAllTableDataAtColumn(GuiStringConstants.FAILURE_CATEGORY);

            for (String category : categoryList) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.FAILURE_CATEGORY, category);
                commonWindow.clickTableCell(row, GuiStringConstants.FAILURE_CATEGORY);
                waitForPageLoadingToComplete();

                if (!drillDownImsiAnalysisWindow) {
                    commonWindow.clickBackwardNavigation();
                    continue;
                }

                drillDownFailuresOnIMSIEventAnalysisWindow(commonWindow, imsi, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
            }
        }
    }

    /* IMSI Group */
    private void validateDataOfIMSIGroupAnalysisWindowForAllTimeRanges(final CommonWindow commonWindow, List<String> imsiGroupList,
                                                                       boolean drillDownFailures) throws NoDataException, PopUpException,
            ParseException, IOException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
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

            if (time == TimeRange.FIVE_MINUTES) {
                continue;
            }

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }
            subscriberCallFailureAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<String> categoryList = commonWindow.getAllTableDataAtColumn(GuiStringConstants.CATEGORY);

            for (String category : categoryList) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.CATEGORY, category);
                commonWindow.clickTableCell(row, GuiStringConstants.CATEGORY);
                waitForPageLoadingToComplete();

                if (!drillDownFailures) {
                    commonWindow.clickBackwardNavigation();
                    continue;
                }

                drillDownFailuresOnIMSIGroupEventAnalysisWindow(commonWindow, imsiGroupList, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
            }
        }
    }

    private void drillDownFailuresOnIMSIGroupEventAnalysisWindow(final CommonWindow window, List<String> imsiGroupList, int timeRangeInMinutes)
            throws NoDataException, PopUpException, ParseException, IOException {

        boolean returnValue = false;
        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);


            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();
            window.openTableHeaderMenu(0);
            window.checkInOptionalHeaderCheckBoxes(headersToTickIfPresent);
            assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

            checkInOptionalHeadersLTECFA(window, eventType);

            window.clickBackwardNavigation();
        }
    }

    private void checkInOptionalHeadersLTECFA(CommonWindow window, String eventType) {
        window.openTableHeaderMenu(0);
        try {
            window.uncheckOptionalHeaderCheckBoxes(optionalHeadersToUntickIfPresent, GuiStringConstants.EVENT_TYPE);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (eventType.equals(GuiStringConstants.RRC_CONN_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfRrcConnSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfS1SigConnSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfInitialCtxtSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.ERAB_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfErabSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfUeCntxtReleaseOnIMSIFailedEventAnalysisWindow);
        } else {
            logger.log(Level.SEVERE, "Invalid Event Type for LTE Call Failures");
            return;
        }

    }

}
