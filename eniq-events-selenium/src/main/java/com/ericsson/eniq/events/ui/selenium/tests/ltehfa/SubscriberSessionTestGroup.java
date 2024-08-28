package com.ericsson.eniq.events.ui.selenium.tests.ltehfa;

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
    @Qualifier("subscriberEventAnalysisForLTEHFA")
    private CommonWindow subscriberHandoverFailureAnalysis;

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    final List<String> defaultHeadersOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CAUSE_CODE, GuiStringConstants.ENODEB, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.VENDOR));

    final List<String> optionalHeadersOfPrepX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.FAILURE_REASON, GuiStringConstants.Duration, GuiStringConstants.SOURCE_TYPE,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA, GuiStringConstants.HFA_NO_OF_ERABS));

    final List<String> optionalHeadersOfPrepX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT,
            GuiStringConstants.HO_TYPE, GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.Duration, GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.PACKET_FORWARD, GuiStringConstants.HO_TYPE,
            GuiStringConstants.HFA_NO_OF_ERABS));

    final List<String> optionalHeadersOfExecX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX,
            GuiStringConstants.PACKET_FORWARD, GuiStringConstants.HO_TYPE));

    final List<String> optionalHeadersOfPrepS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.Duration, GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_TYPE,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfPrepS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT,
            GuiStringConstants.SRVCC_TYPE, GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.Duration, GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SOURCE_TYPE));

    final List<String> optionalHeadersOfExecS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC, GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX));

    final List<String> optionalHeadersOfHandoverFailureAnalysisERABWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.SOURCE_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_CONTROLLER, GuiStringConstants.TARGET_ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR));

    final List<String> listOfEvents = new ArrayList<String>(Arrays.asList(GuiStringConstants.PREP_X2_IN, GuiStringConstants.PREP_X2_OUT,
            GuiStringConstants.EXEC_X2_IN, GuiStringConstants.EXEC_X2_OUT, GuiStringConstants.PREP_S1_IN, GuiStringConstants.PREP_S1_OUT,
            GuiStringConstants.EXEC_S1_IN, GuiStringConstants.EXEC_S1_OUT));

    final List<String> optionalHeadersPrepERABEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.SETUP_REQ_PCI,
            GuiStringConstants.SETUP_REQ_PVI, GuiStringConstants.HO_PREP_ERAB_RESULT));

    final List<String> optionalHeadersToUntickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.PREP_IN_RESULT_UE_CTXT, GuiStringConstants.HO_ATTEMPT));

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
     * Requirement: 105 65-0582/00634 Test Case: 4.5.3 Description: IMSI Analysis - Test to check the Event Summary Analysis window.
     */
    @Test
    public void IMSISummaryEventsView_5_3() throws Exception {

        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 hour
        assertTrue("ERROR - Default TimeRange is not set to 1 hour",
                subscriberHandoverFailureAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        validateDataOfIMSIAnalysisWindowForAllTimeRanges(subscriberHandoverFailureAnalysis, imsi, false);
    }

    /**
     * Requirement: 105 65-0582/00634 Test Case: 4.5.3 Description: IMSI Analysis - Test to check the Event Summary Analysis window.
     */
    @Test
    public void IMSISummaryEventsViewDrillDown_5_7() throws Exception {

        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 hour
        assertTrue("ERROR - Default TimeRange is not set to 1 hour",
                subscriberHandoverFailureAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        validateDataOfIMSIAnalysisWindowForAllTimeRanges(subscriberHandoverFailureAnalysis, imsi, true);
    }

    /**
     * Requirement: 105 65-0582/00231 Test Case: 4.5.7 Description: To verify that it shall be possible to drill-down "Number of ERABs" from IMSI
     * event summary window.
     */
    @Test
    public void IMSISummaryERABdrilldown_5_7() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        drilldownOnNumberOfERABs(subscriberHandoverFailureAnalysis, imsi);
    }

    @Test
    public void IMSISummaryZeroERABdrilldown_5_7() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE_ZERO_ERAB);
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        drilldownOnNumberOfERABs(subscriberHandoverFailureAnalysis, imsi);
    }

    /**
     * Requirement: 106 65-0582/00634 Test Case: 4.5.4 Description: IMSI Group Analysis - Test to view the LTE RAN events in Summary Event Analysis
     * window .
     */
    @Test
    public void IMSIgroupAnalysisViewLTERANeventsSummaryEventAnalysis_5_4() throws Exception {

        final String imsiGroup = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(imsiGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> imsiGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.IMSI_GROUP_DATA_LTE).split(",")));

        validateDataOfIMSIGroupAnalysisWindowForAllTimeRanges(subscriberHandoverFailureAnalysis, imsiGroupList, false);

    }

    /**
     * Requirement: 106 65-0582/00634 Test Case: 4.5.8 Description: Open the Subscriber tab and through IMSI to go to an IMSI Group Event Analysis
     * view. It shall be possible to go by Group Event Type Failure Count to a detailed Failed Group Event Analysis view.
     */
    @Test
    public void IMSIgroupSummaryEventsViewDrillDown_5_8() throws Exception {
        final String imsiGroup = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(imsiGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> imsiGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.IMSI_GROUP_DATA_LTE).split(",")));

        validateDataOfIMSIGroupAnalysisWindowForAllTimeRanges(subscriberHandoverFailureAnalysis, imsiGroupList, true);

    }

    /**
     * Requirement: 107 65-0582/00634 Test Case: 4.5.7 Description: To verify that it shall be possible to drill-down into eNodeB info.
     */
    @Test
    public void IMSISummaryEventsViewEnodeBdrilldown_5_7() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        drillDownOnFailedAnalysisWindow(subscriberHandoverFailureAnalysis, GuiStringConstants.SOURCE_CONTROLLER);
    }

    /**
     * Requirement: 108 65-0582/00634 Test Case: 4.5.8 Description: To verify that it shall be possible to drill-down into eNodeB info.
     */
    @Test
    public void IMSISummaryEventsViewEcellDrilldown_5_8() throws Exception {
        final String imsi = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.IMSI);
        workspacerc.enterDimensionValue(imsi);
        workspacerc.selectWindowType("4G Event Trace", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        drillDownOnFailedAnalysisWindow(subscriberHandoverFailureAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA);
    }

    ///////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void drillDownOnFailedAnalysisWindow(final CommonWindow window, final String drillDownColumn) throws NoDataException, PopUpException {

        window.clickTableCell(0, "Handover Stage");
        waitForPageLoadingToComplete();

        window.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();

        window.clickTableCell(0, drillDownColumn);
        waitForPageLoadingToComplete();

        assertTrue("Can't open Event Analysis page", selenium.isTextPresent("Event Analysis"));
    }

    private void drilldownOnNumberOfERABs(CommonWindow window, String imsi) throws NoDataException, PopUpException {
        List<String> listOfHandoverStages = window.getAllTableDataAtColumn("Handover Stage");

        for (String handoverStage : listOfHandoverStages) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue("Handover Stage", handoverStage);
            window.clickTableCell(row, "Handover Stage");
            waitForPageLoadingToComplete();

            List<String> listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

            // Drill down for all event types
            for (final String eventType : listOfEventTypes) {
                final int rowEvent = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);
                window.clickTableCell(rowEvent, GuiStringConstants.FAILURES);
                waitForPageLoadingToComplete();

                boolean bDrillDownReq = false;

                List<String> listOfNumberOfERABs = window.getAllTableDataAtColumn("No of ERABS");
                for (String value : listOfNumberOfERABs) {
                    if (value.isEmpty() || value.equals("0")) {
                        bDrillDownReq = false;
                    } else {
                        bDrillDownReq = true;
                    }
                    break;
                }

                if (!bDrillDownReq) {
                    boolean isDrillable = selenium.isElementPresent("//*[@id='RAN_LTE_HFA_IMSI_ERABS_DRILL_ON_INTERNAL_PROC_HO_PREP_S1_IN']");
                    assertTrue("ERROR - No of ERABs should not be drillable for zero value.", !isDrillable);
                    return;
                }

                window.clickTableCell(0, "No of ERABS");
                waitForPageLoadingToComplete();
                //Select the hidden column

                if (handoverStage.equalsIgnoreCase(GuiStringConstants.PREPARATION)) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    window.checkInOptionalHeaderCheckBoxes(optionalHeadersPrepERABEventAnalysisWindow, GuiStringConstants.IMSI);
                }

                window.clickBackwardNavigation();
                window.clickBackwardNavigation();
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

            checkInOptionalHeaderHFA(window, eventType);

            window.clickBackwardNavigation();
        }
    }

    private void checkInOptionalHeaderHFA(CommonWindow window, String eventType) {
        window.openTableHeaderMenu(0);
        pause(2000);
        try {
            window.uncheckOptionalHeaderCheckBoxes(optionalHeadersToUntickIfPresent, GuiStringConstants.EVENT_TYPE);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (eventType.equals(GuiStringConstants.PREP_X2_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepX2InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_X2_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepX2OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_X2_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecX2InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_X2_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecX2OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_S1_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepS1InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_S1_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepS1OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_S1_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecS1InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_S1_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecS1OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else {
            // Throw error here - Unknown Event Type
        }

        try {
            waitForPageLoadingToComplete();
        } catch (PopUpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

            commonWindow.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<String> listOfHandoverStages = commonWindow.getAllTableDataAtColumn("Handover Stage");

            for (String handoverStage : listOfHandoverStages) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue("Handover Stage", handoverStage);
                commonWindow.clickTableCell(row, "Handover Stage");
                waitForPageLoadingToComplete();

                if (!drillDownImsiAnalysisWindow) {
                    commonWindow.clickBackwardNavigation();
                    continue;
                }

                drillDownFailuresOnIMSIEventAnalysisWindow(commonWindow, imsi, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
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

            commonWindow.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<String> listOfHandoverStages = commonWindow.getAllTableDataAtColumn("Handover Stage");

            for (String handoverStage : listOfHandoverStages) {

                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue("Handover Stage", handoverStage);
                commonWindow.clickTableCell(row, "Handover Stage");
                waitForPageLoadingToComplete();

                if (!drillDownFailures) {
                    commonWindow.clickBackwardNavigation();
                    continue;
                }

                drillDownFailuresOnIMSIGroupEventAnalysisWindow(commonWindow, imsiGroupList, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
                commonWindow.clickBackwardNavigation();
            }

        }
    }

    private void drillDownFailuresOnIMSIGroupEventAnalysisWindow(final CommonWindow window, List<String> imsiGroupList, int timeRangeInMinutes)
            throws NoDataException, PopUpException, ParseException, IOException {
        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();

            checkInOptionalHeaderHFA(window, eventType);

            window.clickBackwardNavigation();
        }
    }

}