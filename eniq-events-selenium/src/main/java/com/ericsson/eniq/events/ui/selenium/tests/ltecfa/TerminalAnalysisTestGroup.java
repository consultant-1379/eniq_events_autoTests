package com.ericsson.eniq.events.ui.selenium.tests.ltecfa;

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
    @Qualifier("terminalEventAnalysisForLTECFA")
    private CommonWindow terminalEventAnalysis;

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    final List<String> optionalHeadersOfRrcConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.GUMMEI_Type_Desc, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersOfS1SigConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.RAN_VENDOR, GuiStringConstants.EVENT_TYPE));

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

    final List<String> optionalHeadersToUntickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC));

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
        Thread.sleep(5000);
        workspacerc.enterTerminalNameValue(terminalModel);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Terminal Event Analysis", "Call Failure");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
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
        workspacerc.selectDimension(SeleniumConstants.TERMINAL_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(terminalGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Terminal Event Analysis", "Call Failure");
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

            List<String> categoryList = commonWindow.getAllTableDataAtColumn(GuiStringConstants.FAILURE_CATEGORY);

            for (String category : categoryList) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.FAILURE_CATEGORY, category);
                commonWindow.clickTableCell(row, GuiStringConstants.FAILURE_CATEGORY);
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
            assertTrue("Invalid Event Type for LTE Call Failures", false);
            return;
        }
    }

}