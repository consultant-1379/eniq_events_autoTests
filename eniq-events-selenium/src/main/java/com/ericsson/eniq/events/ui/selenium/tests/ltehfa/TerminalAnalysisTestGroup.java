package com.ericsson.eniq.events.ui.selenium.tests.ltehfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class TerminalAnalysisTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private WorkspaceRC workspacerc;

    @Autowired
    @Qualifier("terminalEventAnalysisForLTEHFA")
    private CommonWindow terminalEventAnalysis;

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    final List<String> optionalHeadersOfPrepX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_TYPE, GuiStringConstants.RANDOM_ACCESS_TYPE,
            GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfPrepX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT, GuiStringConstants.HO_TYPE,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.PACKET_FORWARD, GuiStringConstants.HO_TYPE));

    final List<String> optionalHeadersOfExecX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX, GuiStringConstants.PACKET_FORWARD,
            GuiStringConstants.HO_TYPE));

    final List<String> optionalHeadersOfPrepS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_TYPE, GuiStringConstants.RANDOM_ACCESS_TYPE,
            GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfPrepS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT, GuiStringConstants.SRVCC_TYPE,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SOURCE_TYPE));

    final List<String> optionalHeadersOfExecS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX));

    @Override
    @Before
    public void setUp() {
        super.setUp();
        workspacerc.checkAndOpenSideLaunchBar();
        pause(2000);
        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: Test Case: Description:
     */
    @Test
    public void terminalEventAnalysis_7_1() throws Exception {

        final String terminalMake = reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_MAKE_LTE);
        final String terminalModel = reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_MODEL_LTE);
        final String tac = reservedDataHelper.getCommonReservedData(CommonDataType.TAC_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TERMINAL);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(terminalMake);
        workspacerc.enterTerminalNameValue(terminalModel);

        workspacerc.selectWindowType("4G Terminal Event Analysis", "Handover Failure");
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        final List<String> tacList = new ArrayList<String>();
        tacList.add(tac);

        validateDataOfEventAnalysisWindowForAllTimeRanges(terminalEventAnalysis, tacList, GuiStringConstants.TAC);
    }

    /**
     * Requirement: Test Case: Description:
     */
    @Test
    public void terminalGroupEventAnalysis_7_2() throws Exception {

        final String terminalGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.TERMINAL_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(terminalGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Terminal Event Analysis", "Handover Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> tacList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.TERMINAL_GROUP_DATA_LTE).split(",")));

        validateDataOfEventAnalysisWindowForAllTimeRanges(terminalEventAnalysis, tacList, GuiStringConstants.TAC);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void validateDataOfEventAnalysisWindowForAllTimeRanges(final CommonWindow commonWindow, List<String> tacList, String networkEntityType)
            throws NoDataException, PopUpException, ParseException, IOException {
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

            List<String> categoryList = commonWindow.getAllTableDataAtColumn(GuiStringConstants.HANDOVER_STAGE);

            for (String category : categoryList) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.HANDOVER_STAGE, category);
                commonWindow.clickTableCell(row, GuiStringConstants.HANDOVER_STAGE);
                waitForPageLoadingToComplete();

                drillDownFailuresOnNetworkGroupEventAnalysisWindow(commonWindow, tacList, networkEntityType, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
            }
        }
    }

    private void drillDownFailuresOnNetworkGroupEventAnalysisWindow(final CommonWindow window, List<String> networkGroupList,
                                                                    String networkEntityType, int timeRangeInMinutes) throws NoDataException,
            PopUpException, ParseException, IOException {
        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();
            assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

            checkInOptionalHeadersLTEHFA(window, eventType);

            window.clickBackwardNavigation();
        }

    }

    private void checkInOptionalHeadersLTEHFA(CommonWindow window, String eventType) {
        window.openTableHeaderMenu(0);
        pause(2000);

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
            assertTrue("ERROR - Invalid EventType : " + eventType, false);
        }

        try {
            waitForPageLoadingToComplete();
        } catch (PopUpException e) {
            e.printStackTrace();
        }
    }

}