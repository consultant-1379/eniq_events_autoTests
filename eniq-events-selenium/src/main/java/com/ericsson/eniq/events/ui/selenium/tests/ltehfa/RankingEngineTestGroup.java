package com.ericsson.eniq.events.ui.selenium.tests.ltehfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RankingEngineTestGroup extends EniqEventsUIBaseSeleniumTest {
    public enum ColumnNames {
        CONTROLLER("Controller"), TAC("TAC"), ACCESS_AREA("Access Area");
        private final String label;

        private ColumnNames(final String txtLabel) {
            label = txtLabel;
        }

        public String getLabel() {
            return label;
        }
    }

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    @Autowired
    private WorkspaceRC workspacerc;

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("subscriberRankingLteHandOverPrepFailure")
    private CommonWindow subscriberRankingHandOverPrepFailure;

    @Autowired
    @Qualifier("subscriberRankingLteHandOverExecFailure")
    private CommonWindow subscriberRankingHandOverExecFailure;

    @Autowired
    @Qualifier("eNodeBrankingLteHandoverPrepFailures")
    private CommonWindow eNodeBrankingPrepFailures;

    @Autowired
    @Qualifier("eNodeBrankingLteHandoverExecFailures")
    private CommonWindow eNodeBrankingExecFailures;

    @Autowired
    @Qualifier("sourceCellRankingLteHandoverPrepFailures")
    private CommonWindow sourceCellRankingPrepFailures;

    @Autowired
    @Qualifier("targetCellRankingLteHandoverPrepFailures")
    private CommonWindow targetCellRankingPrepFailures;

    @Autowired
    @Qualifier("sourceCellRankingLteHandoverExecFailures")
    private CommonWindow sourceCellRankingExecFailures;

    @Autowired
    @Qualifier("targetCellRankingLteHandoverExecFailures")
    private CommonWindow targetCellRankingExecFailures;

    @Autowired
    @Qualifier("causeCodeRankingLTEHFAprep")
    private CommonWindow causeCodeRankingPrepFailure;

    @Autowired
    @Qualifier("causeCodeRankingLTEHFAexec")
    private CommonWindow causeCodeRankingExecFailure;

    @Autowired
    @Qualifier("terminalRankingLTEprep")
    private CommonWindow terminalRankingPrepFailure;

    @Autowired
    @Qualifier("terminalRankingLTEexec")
    private CommonWindow terminalRankingExecFailure;

    final List<String> optionalRankingHeader = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR));

    @Override
    @Before
    public void setUp() {
        super.setUp();
        workspacerc.checkAndOpenSideLaunchBar();

        pause(2000);

        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: 105 65-0582/00678 Test Case: 6.2 Description: It shall be possible to view the top 50 eNodeBs which are experiencing the most
     * Handover preparation failures.
     */
    @Test
    public void eNodeBRankingPreparationFailures_6_2() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Preparation by Controller");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eNodeBrankingPrepFailures.getTableRowCount() <= 50);

        validateHandOverFailureRankingData(eNodeBrankingPrepFailures, "ENODEB RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00678 Test Case: 6.2 Description: It shall be possible to view the top 50 eNodeBs which are experiencing the most
     * Handover execution failures.
     */
    @Test
    public void eNodeBRankingExecFailures_6_32() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Execution by Controller");
        workspacerc.clickLaunchButton();

        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eNodeBrankingExecFailures.getTableRowCount() <= 50);

        validateHandOverFailureRankingData(eNodeBrankingExecFailures, "ENODEB RANKING", GuiStringConstants.EXECUTION);
    }

    /**
     * Requirement: 105 65-0582/00678 Test Case: 6.6 Description: It shall be possible to view the top 50 Subscribers which are experiencing the most
     * handover failures by preparation.
     */
    @Test
    public void SubscriberRankingHandoverPrepFailure_6_6() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Failure Preparation");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        validateHandOverFailureRankingData(subscriberRankingHandOverPrepFailure, "SUBSCRIBER RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00678 Test Case: 6.7 Description: It shall be possible to view the top 50 Subscribers which are experiencing the most
     * handover failures by execution.
     */
    @Test
    public void SubscriberRankingHandoverExecFailure_6_7() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Failure Execution");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        validateHandOverFailureRankingData(subscriberRankingHandOverExecFailure, "SUBSCRIBER RANKING", GuiStringConstants.EXECUTION);
    }

    /**
     * Requirement: 105 65-0582/00260 Test Case: 4.6.26 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * setup failures.
     */
    @Test
    public void SourceCellRankingPrepFailures_6_26() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Preparation by Source Cell");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        validateHandOverFailureRankingData(sourceCellRankingPrepFailures, "SOURCE ECELL RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00261 Test Case: 4.6.28 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * drops.
     */
    @Test
    public void SourceCellRankingExecFaliures_6_28() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Execution by Source Cell");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", sourceCellRankingExecFailures.getTableRowCount() <= 50);

        validateHandOverFailureRankingData(sourceCellRankingExecFailures, "SOURCE ECELL RANKING", GuiStringConstants.EXECUTION);
    }

    /**
     * Requirement: 105 65-0582/00260 Test Case: 4.6.26 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * setup failures.
     */
    @Test
    public void TargetCellRankingPrepFailures_6_26() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Preparation by Target Cell");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        validateHandOverFailureRankingData(targetCellRankingPrepFailures, "TARGET ECELL RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00261 Test Case: 4.6.28 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * drops.
     */
    @Test
    public void TargetCellRankingExecFaliures_6_28() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Execution by Target Cell");

        workspacerc.clickLaunchButton();

        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", targetCellRankingExecFailures.getTableRowCount() <= 50);

        validateHandOverFailureRankingData(targetCellRankingExecFailures, "TARGET ECELL RANKING", GuiStringConstants.EXECUTION);
    }

    /**
     * Requirement: 105 65-0582/00266 Test Case: 6.14 Description: It shall be possible to view the top 50 Cause Codes which are in result of call
     * setup failed events.
     */
    @Test
    public void CauseCodeRankingPrepFailures_6_14() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Cause Code Ranking", "Handover Preparation");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", causeCodeRankingPrepFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        validateHandOverFailureRankingData(causeCodeRankingPrepFailure, "CAUSE CODE RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00267 Test Case: 6.15 Description: It shall be possible to view the top 50 Cause Codes which are in result of call
     * drops failed events.
     */
    @Test
    public void CauseCodeRankingExecFailures_6_15() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Cause Code Ranking", "Handover Execution");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", causeCodeRankingExecFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);

        validateHandOverFailureRankingData(causeCodeRankingExecFailure, "CAUSE CODE RANKING", GuiStringConstants.EXECUTION);
    }

    /**
     * Requirement: 105 65-0582/00257 Test Case: 4.6.16 Description: It is possible to view the top 50 Terminals which are experiencing the most Prep
     * failures.
     */
    @Test
    public void TerminalRankingPrepFailure_6_16() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Preparation by Terminal");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", terminalRankingPrepFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        checkCallFailuresWindowAscAndDescButton(terminalRankingPrepFailure, listOfColumns);

        validateHandOverFailureRankingData(terminalRankingPrepFailure, "TERMINAL RANKING", GuiStringConstants.PREPARATION);
    }

    /**
     * Requirement: 105 65-0582/00258 Test Case: 4.6.22 Description: To verify that Terminal Ranking dropped calls summary can support to view top 50
     * Terminals who experienced the most Exec failures.
     */
    @Test
    public void TerminalRankingExecFailure_6_22() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("4G Ranking", "Handover Execution by Terminal");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", terminalRankingExecFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        checkCallFailuresWindowAscAndDescButton(terminalRankingExecFailure, listOfColumns);

        validateHandOverFailureRankingData(terminalRankingExecFailure, "TERMINAL RANKING", GuiStringConstants.EXECUTION);
    }

    ///////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Check Ascending and Descending buttons on event analysis window.
     * 
     * @param window
     *            the object of CommonWindow
     * @throws NoDataException
     * @throws PopUpException
     */
    private void checkCallFailuresWindowAscAndDescButton(final CommonWindow window, List<String> listOfColumns) throws NoDataException,
            PopUpException {

        for (String column : listOfColumns) {
            window.sortTable(SortType.ASCENDING, column);
            window.sortTable(SortType.DESCENDING, column);
        }
    }

    private void validateHandOverFailureRankingData(final CommonWindow commonWindow, String rankingType, String failureType) throws NoDataException,
            PopUpException, ParseException, IOException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        // Validate data for all time ranges
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

            //     Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            commonWindow.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            commonWindow.checkInOptionalHeaderCheckBoxes(optionalRankingHeader, GuiStringConstants.RANK);

        }

    }

}
