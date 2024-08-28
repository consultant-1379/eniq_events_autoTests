package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.CSVReaderCFAHFA;
import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TableNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.GroupTerminalAnalysisViewType;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/*
 * Updated/refactored by efreass based on VS_ENIQ_Events12_2_WCDMA_HFA_2.2.14_SERVER2012
 * 23 july 2012
 */
@SuppressWarnings("deprecation")
public class RankingTestGroupWcdmaHfaNewGUI extends BaseWcdmaHfa {

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaHfaReserveDataS.csv");

    @Autowired
    @Qualifier("terminalGroupAnalysisWCDMAHFA")
    private CommonWindow terminalGroupAnalysisWCDMAHFA;

    @Autowired
    @Qualifier("terminalEventAnalysisHandoverFailureAnalysisWCDMA")
    private CommonWindow terminalEventAnalysisHandoverFailureAnalysisWCDMA;

    @Autowired
    @Qualifier("rncRankingWCDMAHandOverFailure")
    private CommonWindow rncRankingWCDMAHandOverFailure;

    @Autowired
    @Qualifier("sourceAccessAreaRankingWCDMAHandOverFailure")
    private CommonWindow sourceAccessAreaRankingWCDMAHOF;

    @Autowired
    @Qualifier("targetAccessAreaRankingWCDMAHandOverFailure")
    private CommonWindow targetAccessAreaRankingWCDMAHOF;

    @Autowired
    @Qualifier("terminalRankingWCDMAHOF")
    private CommonWindow terminalRankingWCDMAHOF;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureSOHO")
    private CommonWindow subscriberRankingWCDMAHOFSOHO;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureIRAT")
    private CommonWindow subscriberRankingWCDMAHOFIRAT;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureIFHO")
    private CommonWindow subscriberRankingWCDMAHOFIFHO;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureHSDSCH")
    private CommonWindow subscriberRankingWCDMAHOFHSDSCH;

    @Autowired
    @Qualifier("subscriberMultipleRecurringFailureRanking")
    private CommonWindow subscriberMultipleRecurringFailureRanking;

    @Autowired
    @Qualifier("causeCodeRankingWCDMAHandOverFailure")
    private CommonWindow causeCodeRankingWCDMAHandOverFailure;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallSetupFailures")
    private CommonWindow rankingsCauseCodeWcdmaCallSetupFailures;

    @Autowired
    @Qualifier("terminalAnalysisWCDMAHFA")
    private CommonWindow terminalAnalysisWCDMAHFA;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    final static List<String> defaultEventFailureAnalysisWindowCauseCode = new ArrayList<String>(Arrays.asList(GuiStringConstants.SUB_CAUSE_CODE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    private final String EXPECTED_HEADER = " EXPECTED HEADERS : ";

    private final String WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    private String dataIntegrityFlag;

    private static final String TRUE = "true";

    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
    }

    @Override
    @Before
    public void setUp() {
        //CommonUtils.truncateUserPrederencesTable();
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        dataIntegrityFlag = PropertyReader.getInstance().getDataIntegrityFlag();
    }

    /*
     * EE12.2_WHFA_8.1; Terminal Group Analysis Chart – Most SOHO Failures (VS No. 4.11.1) throws InterruptedException, PopUpException,
     * NoDataException
     */

    @Test
    public void terminalGroupAnalysisChartMostSOHOFailures_8_1() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Group_Analysis_3G);
        selenium.waitForPageLoadingToComplete();

        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);
        selenium.waitForPageLoadingToComplete();

        assertTrue(GuiStringConstants.ERROR_LOADING + MOST_SOFT_HANDOVER_FAILURES_SUMMARY,
                selenium.isTextPresent(MOST_SOFT_HANDOVER_FAILURES_SUMMARY));

        final String expectedWindowTitle = GuiStringConstants.WCDMA + GuiStringConstants.SPACE + GuiStringConstants.HANDOVER_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        terminalGroupAnalysisWCDMAHFA.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventSOHOAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventSOHOAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.2; Terminal Group Analysis Chart – Most IFHO Failures (VS No. 4.11.2)
     */

    @Test
    public void terminalGroupAnalysisChartMostIFHOFailures_8_2() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Group_Analysis_3G);

        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);

        final String expectedWindowTitle = GuiStringConstants.WCDMA + GuiStringConstants.SPACE + GuiStringConstants.HANDOVER_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + MOST_INTER_FREQUENCY_HANDOVER_FAILURES,
                selenium.isTextPresent(MOST_INTER_FREQUENCY_HANDOVER_FAILURES));

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        terminalGroupAnalysisWCDMAHFA.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventIFHOAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventIFHOAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.3; Terminal Group Analysis Chart – Most IRAT Failures(VS No. 4.11.3)
     */

    @Test
    public void terminalGroupAnalysisChartMostIRATFailures_8_3() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Group_Analysis_3G);
        selenium.waitForPageLoadingToComplete();

        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
        selenium.waitForPageLoadingToComplete();

        final String expectedWindowTitle = GuiStringConstants.WCDMA + GuiStringConstants.SPACE + GuiStringConstants.HANDOVER_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + MOST_IRAT_HANDOVER_FAILURES, selenium.isTextPresent(MOST_IRAT_HANDOVER_FAILURES));

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        terminalGroupAnalysisWCDMAHFA.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventIRATAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventIRATAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.4; Terminal Group Analysis Chart – Most HSDSCH Failures (VS No. 4.11.4)
     */

    @Test
    public void terminalGroupAnalysisChartMostHSDSCHFailures_8_4() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Group_Analysis_3G);

        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);

        assertTrue(GuiStringConstants.ERROR_LOADING + MOST_HSDSCH_HANDOVER_FAILURE, selenium.isTextPresent(MOST_HSDSCH_HANDOVER_FAILURE));

        final String expectedWindowTitle = GuiStringConstants.WCDMA + GuiStringConstants.SPACE + GuiStringConstants.HANDOVER_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        terminalGroupAnalysisWCDMAHFA.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventHSDSCHAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventHSDSCHAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.5; Terminal Analysis – Most SOHO Failures.(VS No. 4.11.6)
     */
    @Test
    public void terminalAnalysisMostSOHOFailures_8_5() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);

        final String expectedWindowTitle = GuiStringConstants.TERMINAL_ANALYSIS_MOST_SOHO_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalAnalysisColumns));
    }

    /*
     * EE12.2_WHFA_8.6; Terminal Analysis – Most IFHO Failures (VS No. 4.11.10)
     */
    @Test
    public void terminalAnalysisMostIFHOFailures_8_6() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);

        final String expectedWindowTitle = GuiStringConstants.TERMINAL_ANALYSIS_MOST_IFHO_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        final List<String> actualWindowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.7; Terminal Analysis – Most IRAT Failures (VS No. 4.11.14)
     */

    @Test
    public void terminalAnalysisMostIRATFailures_8_7() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
        final String expectedWindowTitle = GuiStringConstants.TERMINAL_ANALYSIS_MOST_IRAT_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        final List<String> actualWindowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalAnalysisColumns));
    }

    /*
     * EE12.2_WHFA_8.8; Terminal Analysis – Most HSDSCH Failures (VS No. 4.11.18)
     */

    @Test
    public void terminalAnalysisMostHSDSCHFailures_8_8() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);
        final String expectedWindowTitle = GuiStringConstants.TERMINAL_ANALYSIS_MOST_HSDSCH_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.9; Drill down from Terminal Analysis to Detailed Event Analysis (SOHO)(VS No. 4.11.5) Action 2
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisSOHO_8_9() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.TAC));

        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultEvent_Failure_Analysis_SOHO
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultEvent_Failure_Analysis_SOHO));

    }

    /*
     * EE12.2_WHFA_8.9; Drill down from Terminal Analysis to Detailed Event Analysis (SOHO)(VS No. 4.11.6) Action 2 further drill down on Source Cell
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisDrillDownOnSorceCellSOHO_8_9a() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);

        drillDownOnFailureThanOnGivenTypesUsingTacAndIMSI(terminalAnalysisWCDMAHFA, GuiStringConstants.SOURCE_CELL);

        final String expectedWindowTitle = GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;

        assertWindowTitleAndWindowsHeaders(terminalAnalysisWCDMAHFA, expectedWindowTitle, defaultSourceCellEventAnalysisWindow);

    }

    /*
     * EE12.2_WHFA_8.9; Drill down from Terminal Analysis to Detailed Event Analysis (SOHO)(VS No. 4.11.6) Action 3 further drill down on Source Cell
     * and than, on failures
     */
    @Ignore
    @Test
    public void terminalAnalysisToDetailedEventAnalysisDrillDownOnSorceCellThanFailuresSOHO_8_9b() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);

        drillDownOnFailureThanOnGivenTypesUsingTacAndIMSI(terminalAnalysisWCDMAHFA, GuiStringConstants.SOURCE_CELL);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(terminalAnalysisWCDMAHFA, expectedWindowTitle, defaultFailedEventSOHOAnalysisColumns);

        if (isDataIntegrityFlagOn()) {
            doDataIntegrityForGivenIMSIInFailedEA(terminalAnalysisWCDMAHFA, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
                    GuiStringConstants.TARGET_CELL, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
                    GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL);
        }
    }

    /*
     * EE12.2_WHFA_8.10; Drill down from Terminal Analysis to Detailed Event Analysis (IFHO) (VS No. 4.11.9) Action 2
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisIFHO_8_10() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.TAC));

        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultEvent_Failure_Analysis_IFHO
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultEvent_Failure_Analysis_IFHO));

    }

    /*
     * 4.11.12 13B_WCDMA_CFA_HFA_4.11.12: WCDMA_HFA: Terminal Analysis - Most IFHO Failures - Drill on Target Cell to Detailed Event Analysis
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisIFHODrillDownTargetCell_8_10a() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);

        drillDownOnFailureThanOnGivenTypesUsingTacAndIMSI(terminalAnalysisWCDMAHFA, GuiStringConstants.TARGET_CELL);

        final String expectedWindowTitle = GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;

        assertWindowTitleAndWindowsHeaders(terminalAnalysisWCDMAHFA, expectedWindowTitle, defaultTargetCellCodeHOAnalysisWindow);

    }

    /*
     * 4.11.12 13B_WCDMA_CFA_HFA_4.11.12: WCDMA_HFA: Terminal Analysis - Most IFHO Failures - Drill on Target Cell to Detailed Event Analysis Action 4
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisIFHODrillDownTargetCellThanFailures_8_10b() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);

        drillDownOnFailureThanOnGivenTypesUsingTacAndIMSI(terminalAnalysisWCDMAHFA, GuiStringConstants.TARGET_CELL);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(terminalAnalysisWCDMAHFA, expectedWindowTitle, defaultTargetNetworkEventAnalysiWCDMAIFHOWindow);

    }

    /*
     * EE12.2_WHFA_8.11; Drill down from Terminal Analysis to Detailed Event Analysis (IRAT) (VS No. 4.11.13) Action 2
     */
    @Test
    public void terminalAnalysisToDetailedEventAnalysisIRAT_8_11() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.TAC));

        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalEventAnalysiWCDMAIRATWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysiWCDMAIRATWindow));

    }

    /*
     * EE12.2_WHFA_8.12; Drill down from Terminal Analysis to Detailed Event Analysis (HSDSCH)(VS No. 4.11.17)
     */

    @Test
    public void terminalAnalysisToDetailedEventAnalysisHSDSCH_8_12() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.TAC));

        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultEvent_Failure_Analysis_HSDSCH
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultEvent_Failure_Analysis_HSDSCH));

    }

    /*
     * VS No: 4.12.1 13B_WCDMA_CFA_HFA_4.12.1: WCDMA_HFA: Drill down from RNC Ranking to Detailed Event Analysis by Handover Type-SOHO
     */

    @Test
    public void hfaRankingTabRNCRankingsAccuracyVerificationOn_9_1() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Controller,
                BaseWcdmaHfa.Ranking_3G);

        String expectedWindowTitle = BaseWcdmaHfa.RNC_WCDMA_Handover_Failure_Event_Ranking;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final String actualWindowTitle = rncRankingWCDMAHandOverFailure.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));

    }

    /*
     * VS No: 4.12.1 13B_WCDMA_CFA_HFA_4.12.1: WCDMA_HFA: Drill down from RNC Ranking to Detailed Event Analysis by Handover Type-SOHO Drill down on
     * Failures
     */

    @Test
    public void hfaRankingTabRNCRankingsAccuracyVerificationOn_9_2() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Controller,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = rncRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.RNC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER));
        rncRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.RNC);

        final String expectedWindowTitle = GuiStringConstants.CONTROLLER_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders2 = rncRankingWCDMAHandOverFailure.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultControllerEventAnalysisWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders2.equals(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));

    }

    /*
     * VS No: 4.12.1 13B_WCDMA_CFA_HFA_4.12.1: WCDMA_HFA: Drill down from RNC Ranking to Detailed Event Analysis by Handover Type-SOHO Drill down on
     * HSDSCH
     */

    @Test
    public void hfaRankingTabRNCRankingsAccuracyVerificationHSDSCH_9_3() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Controller,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = rncRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.RNC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER));
        rncRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.RNC);

        rowIndex = rncRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        rncRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultControllerEventAnalysisHSDSCHWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(BaseWcdmaHfa.defaultControllerEventAnalysisHSDSCHWindow));

    }

    /*
     * VS No: 4.12.1 13B_WCDMA_CFA_HFA_4.12.1: WCDMA_HFA: Drill down from RNC Ranking to Detailed Event Analysis by Handover Type-SOHO Drill down on
     * IFHO
     */

    @Test
    public void hfaRankingTabRNCRankingsAccuracyVerificationIFHO_9_4() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Controller,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = rncRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.RNC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER));
        rncRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.RNC);

        rowIndex = rncRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        rncRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultControllerEventAnalysisIFHOWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(BaseWcdmaHfa.defaultControllerEventAnalysisIFHOWindow));

    }

    /*
     * VS No: 4.12.5 13B_WCDMA_CFA_HFA_4.12.5: WCDMA_HFA: Access Area Ranking Analysis for Source Cells
     */
    @Test
    public void hfaRankingTabAccessAreaSourceCellRankingsAccuracyVerification_9_5() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Source_Cell,
                BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_WCDMA_Handover_Failure_by_Source_Cell;
        final String actualWindowTitle = sourceAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));

    }

    /*
     * VS No: 4.12.5 13B_WCDMA_CFA_HFA_4.12.5: WCDMA_HFA: Access Area Ranking Analysis for Source Cells Drill down on Failures
     */
    @Test
    public void hfaRankingTabAccessAreaSourceCellRankingsAccuracyVerification_9_6() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Source_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = sourceAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.SOURCE_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Source;
        final String actualWindowTitle = sourceAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.sourceCell3GRankingWCDMAFailuresDrillDownWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaHfa.sourceCell3GRankingWCDMAFailuresDrillDownWindow));
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.sourceCell3GRankingWCDMAFailuresDrillDownWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.sourceCell3GRankingWCDMAFailuresDrillDownWindow));

    }

    /*
     * VS No: 4.12.5 13B_WCDMA_CFA_HFA_4.12.5: WCDMA_HFA: Access Area Ranking Analysis for Source Cells Drill down on HSDSCH Failures
     */
    @Test
    public void hfaRankingTabAccessAreaSourceCellRankingsAccuracyVerification_9_7() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Source_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = sourceAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.SOURCE_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        rowIndex = sourceAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = sourceAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH));
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH));

    }

    /*
     * VS No: 4.12.5 13B_WCDMA_CFA_HFA_4.12.5: WCDMA_HFA: Access Area Ranking Analysis for Source Cells Drill down on SOHO Failures
     */
    @Test
    public void hfaRankingTabAccessAreaSourceCellRankingsAccuracyVerification_9_8() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Source_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = sourceAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.SOURCE_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        rowIndex = sourceAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = sourceAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowSOHO
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowSOHO));
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowSOHO + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowSOHO));

    }

    /*
     * 4.12.6 13B_WCDMA_CFA_HFA_4.12.6: WCDMA_HFA: Access Area Ranking Analysis for Target Cells
     */
    @Test
    public void hfaAccessAreaRankingAnalysisForTargetCells_9_9() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Target_Cell,
                BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_WCDMA_Handover_Failure_by_Target_Cell;
        final String actualWindowTitle = sourceAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = targetAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTargetCellEventAnalysisWindow));
    }

    /*
     * 4.12.6 13B_WCDMA_CFA_HFA_4.12.6: WCDMA_HFA: Access Area Ranking Analysis for Target Cells Drill Down on Failures
     */
    @Test
    public void hfaAccessAreaRankingAnalysisForTargetCells_9_10() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Target_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = targetAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TARGET_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        targetAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Target;
        final String actualWindowTitle = targetAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = targetAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetAccesAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTargetAccesAreaEventAnalysisWindow));

    }

    /*
     * 4.12.6 13B_WCDMA_CFA_HFA_4.12.6: WCDMA_HFA: Access Area Ranking Analysis for Target Cells Drill Down on SOHO Failures
     */
    @Test
    public void hfaAccessAreaRankingAnalysisForTargetCells_9_11() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Target_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = targetAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TARGET_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        targetAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        rowIndex = targetAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        targetAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = targetAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = targetAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowSOHO + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowSOHO));

    }

    /*
     * 4.12.6 13B_WCDMA_CFA_HFA_4.12.6: WCDMA_HFA: Access Area Ranking Analysis for Target Cells Drill Down on SOHO Failures
     */
    @Test
    public void hfaAccessAreaRankingAnalysisForTargetCells_9_12() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Target_Cell,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = targetAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TARGET_CELL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        targetAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        rowIndex = targetAccessAreaRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        targetAccessAreaRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = targetAccessAreaRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = targetAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH));

    }

    /*
     * 4.12.7 13B_WCDMA_CFA_HFA_4.12.7: WCDMA_HFA: Terminal Handover Ranking Analysis
     */
    @Test
    public void rankingTabDrilldownFromTerminalTotalToHOFailureTypeTotals_9_13() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Terminal,
                BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Terminal_WCDMA_Handover_Failure_Event_Ranking;
        final String actualWindowTitle = terminalRankingWCDMAHOF.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
    }

    /*
     * 4.12.7 13B_WCDMA_CFA_HFA_4.12.7: WCDMA_HFA: Terminal Handover Ranking Analysis Drill Down on TAC
     */
    @Test
    public void rankingTabDrilldownFromTerminalTotalToHOFailureTypeTotals_9_14() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Terminal,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = terminalRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        terminalRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.TAC);
        terminalRankingWCDMAHOF.closeWindow();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON) + GuiStringConstants.DASH
                + BaseWcdmaHfa.Terminal_Event_Analysis_Handover_Failure_Analysis;
        final String actualWindowTitle = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));

    }

    /*
     * 4.12.7 13B_WCDMA_CFA_HFA_4.12.7: WCDMA_HFA: Terminal Handover Ranking Analysis Drill Down on HSDSCH Failures
     */
    @Test
    public void rankingTabDrilldownFromTerminalTotalToHOFailureTypeTotals_9_15() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Terminal,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = terminalRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        terminalRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.TAC);
        terminalRankingWCDMAHOF.closeWindow();

        rowIndex = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        terminalEventAnalysisHandoverFailureAnalysisWCDMA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON) + GuiStringConstants.DASH
                + BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventTerminalAnalysisWindowHSDSCH + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventTerminalAnalysisWindowHSDSCH));

    }

    /*
     * 4.12.7 13B_WCDMA_CFA_HFA_4.12.7: WCDMA_HFA: Terminal Handover Ranking Analysis Drill Down on IRAT Failures
     */
    @Test
    public void rankingTabDrilldownFromTerminalTotalToHOFailureTypeTotals_9_16() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure_By_Terminal,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = terminalRankingWCDMAHOF.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        terminalRankingWCDMAHOF.clickTableCell(rowIndex, GuiStringConstants.TAC);
        terminalRankingWCDMAHOF.closeWindow();

        rowIndex = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.HANDOVER_TYPE));
        terminalEventAnalysisHandoverFailureAnalysisWCDMA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON) + GuiStringConstants.DASH
                + BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalEventAnalysisHandoverFailureAnalysisWCDMA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalFailureAnalysisWindowIFHO + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalFailureAnalysisWindowIFHO));

    }

    /*
     * 4.12.9 13B_WCDMA_CFA_HFA_4.12.9: WCDMA_HFA: Cause Code Rankings Analysis by Handover Type
     */
    @Test
    public void hfaCauseCodeRankingsAnalysisHandoverFailure_9_17() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.CauseCode_Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Cause_Code_WCDMA_Handover_Failure_Event_Ranking;
        final String actualWindowTitle = causeCodeRankingWCDMAHandOverFailure.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));

    }

    /*
     * 4.12.9 13B_WCDMA_CFA_HFA_4.12.9: WCDMA_HFA: Cause Code Rankings Analysis by Handover Type Drill Down on IFHO Cause Code
     */
    @Test
    public void hfaCauseCodeRankingsAnalysisHandoverFailure_9_18() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.CauseCode_Ranking_3G);

        int rowIndex = causeCodeRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        causeCodeRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);
        causeCodeRankingWCDMAHandOverFailure.closeWindow();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON)
                + GuiStringConstants.COMMA_SPACE + reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE_ID)
                + GuiStringConstants.DASH + BaseWcdmaHfa.Sub_Cause_Code_Analysis_Handover_Failure;
        final String actualWindowTitle = causeCodeRankingWCDMAHandOverFailure.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = rankingsCauseCodeWcdmaCallSetupFailures.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCauseCodeHOAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeHOAnalysisWindow));

    }

    /*
     * 4.12.9 13B_WCDMA_CFA_HFA_4.12.9: WCDMA_HFA: Cause Code Rankings Analysis by Handover Type Drill Down on IFHO Cause Code
     */
    @Test
    public void hfaCauseCodeRankingsAnalysisHandoverFailure_9_19() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.CauseCode_Ranking_3G);

        int rowIndex = causeCodeRankingWCDMAHandOverFailure.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        causeCodeRankingWCDMAHandOverFailure.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);
        causeCodeRankingWCDMAHandOverFailure.closeWindow();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON)
                + GuiStringConstants.COMMA_SPACE + reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE_ID)
                + GuiStringConstants.DASH + BaseWcdmaHfa.Sub_Cause_Code_Analysis_Handover_Failure;
        final String actualWindowTitle = causeCodeRankingWCDMAHandOverFailure.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = rankingsCauseCodeWcdmaCallSetupFailures.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCauseCodeHOAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeHOAnalysisWindow));

    }

    /*
     *
     */
    @Test
    public void hfaCauseCodeRankingsAnalysisHandoverFailure_9_20() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Terminal_Analysis_Most_Soft_Handover_Failures;
        final String actualWindowTitle = terminalAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));

    }

    @Test
    public void hfaCauseCodeRankingsAnalysisHandoverFailure_9_21() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Analysis_3G);

        int rowIndex = terminalAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        terminalAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = terminalAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow));

    }

    /*
     * VS No: 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type SOHO Action 1 Implementation
     */
    @Test
    public void hfaRankingTabSubscriberRankingsBySOHOAccuracyVerification_9_22() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Soft_Handover, BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Subscriber_WCDMA_Handover_Failure_by_SOHO;
        final String actualWindowTitle = subscriberRankingWCDMAHOFSOHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> actualWindowHeaders = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));

        if (isDataIntegrityFlagOn()) {
            int rowIndex = subscriberRankingWCDMAHOFSOHO.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = subscriberRankingWCDMAHOFSOHO.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.IMSI, GuiStringConstants.FAILURES);
        }

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type SOHO Action 2 Implementation
     */
    @Test
    public void hfaRankingTabSubscriberRankingsSOHOFailureTotalToEventAnalysis_9_23() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Soft_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFSOHO.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFSOHO.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = subscriberRankingWCDMAHOFSOHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeadersSOHOEventAnalysis = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.default_Rankings_By_Soft_Handover + WINDOW_HEADER + windowHeadersSOHOEventAnalysis);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeadersSOHOEventAnalysis.containsAll(BaseWcdmaHfa.default_Rankings_By_Soft_Handover));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IFHO Action 1 Implementation
     */
    @Test
    public void hfaRankingTabSubscriberIFHORankingsAccuracyVerification_9_24() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Inter_Frequency_Handover,
                BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Subscriber_WCDMA_Handover_Failure_by_IFHO;
        final String actualWindowTitle = subscriberRankingWCDMAHOFSOHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IFHO Action 2 Implementation
     */
    @Test
    public void hfaRankingTabDrillDownSubscriberFailureTotalToDetailEventsIFHO_9_25() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Inter_Frequency_Handover,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFIFHO.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFIFHO.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = subscriberRankingWCDMAHOFIFHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriber_Rankings_By_Inter_Frequency_Handover
                + "\n  The Window headers are      : " + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Inter_Frequency_Handover));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IRAT Action 1 Implementation
     */
    @Test
    public void hfaRankingTabAccuracyVerificationSubscriberRankingsByIRATHandover_9_26() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.IRAT_Handover, BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Subscriber_WCDMA_Handover_Failure_by_IRAT;
        final String actualWindowTitle = subscriberRankingWCDMAHOFSOHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IRAT Action 2 Implementation
     */
    @Test
    public void hfaDrillDownFailureAnalysisToDetailedEventSubscriberRankingsByIRATHandover_9_27() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.IRAT_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFIRAT.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFIRAT.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = subscriberRankingWCDMAHOFIRAT.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriber_Rankings_By_IRAT_Handover
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_IRAT_Handover));

        if (isDataIntegrityFlagOn()) {
            List<String> listOfEventTimes = subscriberRankingWCDMAHOFIRAT.getAllTableDataAtColumn(GuiStringConstants.EVENT_TIME);

            String eventTimeDSL = CommonUtils.dayLightSavingTimeCheck(reservedDataHelperReplacement.getReservedData(EVENT_TIME));

            rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes, eventTimeDSL);

            Map<String, String> result = subscriberRankingWCDMAHOFIRAT.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL);
        }

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type HSDSCH Action 1 Implementation
     */
    @Test
    public void hfaSubscriberTabSubscriberRankingsByHSDSCHHandoverAccuracyVerification_9_28() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.HSDSCH_Handover, BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Subscriber_WCDMA_Handover_Failure_by_HSDSCH;
        final String actualWindowTitle = subscriberRankingWCDMAHOFHSDSCH.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type HSDSCH Action 2 Implementation
     */
    @Test
    public void hfaRankingTabDrillDownSubscriberFailureTotalToDetailedEventAnalysisHSDSCH_9_29() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.HSDSCH_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFHSDSCH.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFHSDSCH.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = BaseWcdmaHfa.Failed_Event_Analysis;
        final String actualWindowTitle = subscriberRankingWCDMAHOFHSDSCH.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriber_Rankings_By_HSDSCH_Handover
                + "\n  The Window headers are      : " + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_HSDSCH_Handover));

    }

    /*
     *
     * Recurring Action 1 Implementation
     */
    @Test
    public void hfaSubscriberTabSubscriberRankingsByHSDSCHHandoverAccuracyVerification_9_30() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Recurring_Failures,
                BaseWcdmaHfa.Ranking_3G);

        final String expectedWindowTitle = BaseWcdmaHfa.Subscriber_WCDMA_Handover_Failure_by_Recurring_Failures;
        final String actualWindowTitle = subscriberMultipleRecurringFailureRanking.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberMultipleRecurringFailureRanking.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMARecurringFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMARecurringFailureWindow));

    }

    /*
     *
     * Recurring Action 2 Implementation
     */
    @Test
    public void hfaSubscriberTabSubscriberRankingsByHSDSCHHandoverAccuracyVerification_9_31() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Recurring_Failures,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberMultipleRecurringFailureRanking.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberMultipleRecurringFailureRanking.clickTableCell(rowIndex, GuiStringConstants.IMSI);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER) + GuiStringConstants.DASH
                + BaseWcdmaHfa.IMSI_Event_Analysis_For_Recurring_Failures;
        final String actualWindowTitle = subscriberMultipleRecurringFailureRanking.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberMultipleRecurringFailureRanking.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingWCDMARecurringFailureWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingWCDMARecurringFailureWindow));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type SOHO Action Drill Source Cell Implementation
     */
    @Test
    public void hfaRankingTabSubscriberRankingsSOHOFailureTotalToEventAnalysis_9_42() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Soft_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFSOHO.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFSOHO.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        List<String> listOfEventTimes = subscriberRankingWCDMAHOFSOHO.getAllTableDataAtColumn(GuiStringConstants.EVENT_TIME);
        String eventTimeDSL = CommonUtils.dayLightSavingTimeCheck(reservedDataHelperReplacement.getReservedData(EVENT_TIME));

        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes, eventTimeDSL);

        subscriberRankingWCDMAHOFSOHO.clickTableCell(rowIndex, GuiStringConstants.SOURCE_CELL);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Source;
        final String actualWindowTitle = subscriberRankingWCDMAHOFSOHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeadersSOHOEventAnalysis = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnSourceCell + WINDOW_HEADER
                + windowHeadersSOHOEventAnalysis);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeadersSOHOEventAnalysis.containsAll(BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnSourceCell));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IFHO Action Drill down on Target Cell Implementation
     */
    @Test
    public void hfaRankingTabDrillDownSubscriberFailureTotalToDetailEventsIFHO_9_43() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.Inter_Frequency_Handover,
                BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFIFHO.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFIFHO.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        List<String> listOfEventTimes = subscriberRankingWCDMAHOFIFHO.getAllTableDataAtColumn(GuiStringConstants.EVENT_TIME);
        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TIME));
        subscriberRankingWCDMAHOFIFHO.clickTableCell(rowIndex, GuiStringConstants.TARGET_CELL);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Target;
        final String actualWindowTitle = subscriberRankingWCDMAHOFIFHO.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnTargetCell
                + "\n  The Window headers are      : " + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnTargetCell));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type IRAT Action Drill down on Source Cell Implementation
     */
    @Test
    public void hfaDrillDownFailureAnalysisToDetailedEventSubscriberRankingsByIRATHandover_9_44() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.IRAT_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFIRAT.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFIRAT.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        List<String> listOfEventTimes = subscriberRankingWCDMAHOFIRAT.getAllTableDataAtColumn(GuiStringConstants.EVENT_TIME);
        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TIME));
        subscriberRankingWCDMAHOFIRAT.clickTableCell(rowIndex, GuiStringConstants.SOURCE_CELL);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Source;
        final String actualWindowTitle = subscriberRankingWCDMAHOFIRAT.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnSourceCell
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnSourceCell));

    }

    /*
     * 4.12.8 13B_WCDMA_CFA_HFA_4.12.8: WCDMA_HFA: Subscriber Ranking Analysis by Handover type HSDSCH Action 2 Implementation
     */
    @Test
    public void hfaRankingTabDrillDownSubscriberFailureTotalToDetailedEventAnalysisHSDSCH_9_45() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, BaseWcdmaHfa.HSDSCH_Handover, BaseWcdmaHfa.Ranking_3G);

        int rowIndex = subscriberRankingWCDMAHOFHSDSCH.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        subscriberRankingWCDMAHOFHSDSCH.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        List<String> listOfEventTimes = subscriberRankingWCDMAHOFHSDSCH.getAllTableDataAtColumn(GuiStringConstants.EVENT_TIME);
        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TIME));
        subscriberRankingWCDMAHOFHSDSCH.clickTableCell(rowIndex, GuiStringConstants.TARGET_CELL);

        final String expectedWindowTitle = BaseWcdmaHfa.Access_Area_Event_Analysis_Target;
        final String actualWindowTitle = subscriberRankingWCDMAHOFHSDSCH.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnTargetCell
                + "\n  The Window headers are      : " + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingsBySoftHandoverDrillOnTargetCell));

    }

    /*
     * This method is to do the basic check on the windows Repeat of Method in TerminalTestGroup
     *
     * @param commonWindow - The window id
     */
    void basicWindowFunctionalityCheck(final CommonWindow commonWindow) throws PopUpException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
        TimeRange[] timeRanges;
        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
            for (final TimeRange timeRange : timeRanges) {
                commonWindow.setTimeRange(timeRange);
                commonWindow.maximizeWindow();
                commonWindow.minimizeWindow();
                commonWindow.restoreWindow();
                commonWindow.refresh();

                final int pageCount = commonWindow.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    commonWindow.clickNextPage();
                }
            }
        }

    }

    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {

        logger.log(Level.INFO, "Checking Data Integrity for " + dataToCheck + " " + GuiStringConstants.DOT + "Expected Value :" + expected
                + " Actual Value : " + actual);

        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck + GuiStringConstants.DOT, expected, actual);
    }

    protected void checkMultipleDataEntriesOnSameRow(final Map<String, String> result, final String... dataFields) {
        for (int i = 0; i < dataFields.length; i++) {
            checkDataIntegrity(dataFields[i], reservedDataHelperReplacement.getReservedData(dataFields[i]), result.get(dataFields[i]));
        }
    }

    private void doDataIntegrityForGivenIMSIInFailedEA(final CommonWindow commonWindow, final String... columnsToCheck) throws NoDataException {
        int rowIndex;
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI, reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnsToCheck);
    }

    private void doDataIntegrity(final CommonWindow commonWindow, final String column, final String... columnsToCheck) throws NoDataException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(column, reservedDataHelperReplacement.getReservedData(column));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnsToCheck);
    }

    private void assertWindowTitleAndWindowsHeaders(final CommonWindow commonWindow, final String expectedWindowTitle,
                                                    final List<String> expectedWindowsHearderList) {
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedWindowsHearderList + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(expectedWindowsHearderList));
    }

    private void drillDownOnFailureThanOnGivenTypesUsingTacAndIMSI(final CommonWindow commonWindow, final String drillOnInSecondWindow)
            throws NoDataException, PopUpException {
        // Drill down on failure Using TAC
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.TAC,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.TAC));
        commonWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        // Now, Drill down Using Give Value
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
        commonWindow.clickTableCell(rowIndex, drillOnInSecondWindow);
    }
}
