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
 * Updated/rewrote testcases by eswasas for the new new GUI which includes workspace and replaces tabs on 28/1/2013
 */
@SuppressWarnings("deprecation")
public class NetworkTestGroupWcdmaHfaNewUI extends BaseWcdmaHfa {

    private String dataIntegrityFlag;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaHfaReserveDataS.csv");

    final List<String> headersToTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE));

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    @Override
    @Before
    public void setUp() {
        //CommonUtils.truncateUserPrederencesTable();
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        dataIntegrityFlag = PropertyReader.getInstance().getDataIntegrityFlag();
        pause(2000);
    }

    @Autowired
    @Qualifier("networkEventAnalysisWCDMAHFA")
    private CommonWindow networkEventAnalysisWCDMAHFA;

    @Autowired
    @Qualifier("gridGraphviewRankingGroupTests")
    private CommonWindow gridGraphviewRankingGroupTests;

    @Autowired
    @Qualifier("controllerGroupCauseCodeAnalysisIRAT")
    private CommonWindow controllerGroupCauseCodeAnalysisIRAT;

    @Autowired
    @Qualifier("causeCodeAnalysisHSDSCH")
    private CommonWindow causeCodeAnalysisHSDSCH;

    @Autowired
    @Qualifier("controllerCauseCodeAnalysisHSDSCH")
    private CommonWindow controllerCauseCodeAnalysisHSDSCH;

    @Autowired
    @Qualifier("controllerCauseCodeAnalysisIFHO")
    private CommonWindow controllerCauseCodeAnalysisIFHO;

    @Autowired
    @Qualifier("accessAreacauseCodeAnalysisHSDSCH")
    private CommonWindow accessAreacauseCodeAnalysisHSDSCH;

    @Autowired
    @Qualifier("accessAreaCauseCodeAnalysisSOHO")
    private CommonWindow accessAreaCauseCodeAnalysisSOHO;

    @Autowired
    @Qualifier("causeCodeAalysis")
    private CommonWindow causeCodeAnalysis;

    private static final String EXPECTED_HEADER = " EXPECTED HEADERS : ";

    private static final String ACTUAL_WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    /*
     * EE12.2_WHFA_7.1; Controller Handover Failure Analysis – Summary Pane VS No: 4.10.1
     */
    @Test
    public void controllerHandoverFailureAnalysisOnNetworkTab_7_1() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerEventAnalysisWindow));

    }

    /*
     * EE12.2_WHFA_7.1a; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.2
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisSOHO_7_1a() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceNetworkEventAnalysiWCDMASOHOWindow));

    }

    /*
     * EE12.1_WHFA_7.1b; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.3
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIFHO_7_1b() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceNetworkEventAnalysiWCDMAIFHOWindow));

    }

    /*
     * EE12.1_WHFA_7.1c; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT) VS No: 4.10.
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIRAT_7_1c() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultNetworkEventAnalysiWCDMAIRATWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultNetworkEventAnalysiWCDMAIRATWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH) VS No:
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventHSDSCH_7_1d() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultNetworkEventAnalysiWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultNetworkEventAnalysiWCDMAHSDSCHWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.2 Action 3
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventSOHO_7_1e() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TAC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTerminalEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.2 Action 3
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIFHO_7_1f() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TAC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTerminalEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT) VS No: 4.10.2 Action 3
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIRAT_7_1g() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TAC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTerminalEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH) VS No: 4.10.2 Action 3
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventHSDSCH_7_1h() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TAC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTerminalEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.2 Action 4
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventSOHO_7_1i() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, SOURCE_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceCellEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceCellEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.2 Action 4
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIFHO_7_1j() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, SOURCE_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceCellEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceCellEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT) VS No: 4.10.2 Action 4
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIRAT_7_1k() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, SOURCE_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceCellEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceCellEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH) VS No: 4.10.2 Action 4
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventHSDSCH_7_1l() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, SOURCE_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultSourceCellEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultSourceCellEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.2 Action 5
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventSOHO_7_1m() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TARGET_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTargetCellCodeHOAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTargetCellCodeHOAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.2 Action 5
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIFHO_7_1n() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TARGET_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTargetCellCodeHOAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTargetCellCodeHOAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT) VS No: 4.10.2 Action 5
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIRAT_7_1o() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TARGET_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTargetCellCodeHOAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTargetCellCodeHOAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH) VS No: 4.10.2 Action 5
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventHSDSCH_7_1p() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, TARGET_CELL);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultTargetCellCodeHOAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTargetCellCodeHOAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.2 Action 6
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventSOHO_7_1q() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_RNC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.2 Action 6
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIFHO_7_1r() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_RNC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT) VS No: 4.10.2 Action 6
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventIRAT_7_1s() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_RNC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH) VS No: 4.10.2 Action 6
     */
    @Test
    public void rncHOFailureAnalysisOnNetworkTabToDetailedEventHSDSCH_7_1t() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_RNC);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.2; Controller Group Handover Failure Analysis  Summary Pane VS No: 4.10.6
     */
    @Test
    public void controllerGroupHandoverFailureAnalysisOnNetworkTab_7_2() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                Network_Event_Analysis);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultControllerGroupEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultControllerGroupEventAnalysisWindow));

    }

    /*
     * EE12.1_WHFA_7.2a; Drill down from Controller Group HO failure totals to Detailed Event Analysis (SOHO) VS No: 4.10.6
     */
    @Test
    public void rncGroupHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisSOHO_7_2a() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultGroupNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(defaultGroupNetworkEventAnalysiWCDMASOHOWindow));

    }

    /*
     * EE12.1_WHFA_7.2b; Drill down from Controller Group HO failure totals to Detailed Event Analysis (IFHO) VS No: 4.10.7
     */
    @Test
    public void rncGroupHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIFHO_7_2b() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, BaseWcdmaHfa.CONTROLLER_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));

    }

    /*
     * EE12.1_WHFA_7.2c; Drill down from Controller Group HO failure totals to Detailed Event Analysis (IRAT) 4.10.8
     */
    @Test
    public void rncGroupHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIRAT_7_2c() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, BaseWcdmaHfa.CONTROLLER_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + ACTUAL_WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));

    }

    /*
     * EE12.1_WHFA_7.2d; Drill down from Controller Group HO failure totals to Detailed Event Analysis (HSDSCH) VS No: 4.10.9
     */
    @Test
    public void rncGroupHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisHSDSCH_7_2d() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, BaseWcdmaHfa.CONTROLLER_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow));

    }

    /*
     * EE12.2_WHFA_7.3; Access Area Handover Failure Analysis – Summary Pane - Managed 3G Cell VS No: 4.10.10
     */
    @Test
    public void accessAreaSUMMARYMANAGED3GHOFailureAnalysisOnNetworkTab_7_3() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

    }

    /*
     * EE12.2_WHFA_7.3d; Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO Source Cell) VS No: No Corresponding VS
     */
    @Test
    public void accessAreaSOURCEHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisSOHO_7_3d() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow));

    }

    //EE12.2_WHFA_7.3e; Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO Target Cell)
    @Test
    public void accessAreaTotalTARGETHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisSOHO_7_3e() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.TARGET_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMASOHOWindow));

    }

    /*
     * EE12.2_WHFA_7.3f; Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO Source Cell) VS No:4.10.11 (IFHO Drill on
     * Source Cell)
     */
    @Test
    public void accessAreaTotalSOURCEHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIFHO_7_3f() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow));

    }

    /*
     * EE12.2_WHFA_7.3g; Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO Target Cell) VS No: 4.10.11 (IFHO Drill on
     * Target Cell)
     */
    @Test
    public void accessAreaTotalTARGETHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIFHO_7_3g() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.TARGET_FAILURES);
        networkEventAnalysisWCDMAHFA.filterColumnEquals(GuiStringConstants.EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TYPE));

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAIFHOWindow));

    }

    /*
     * EE12.2_WHFA_7.3h; Drill down from Access Area total per HO type to Detailed Event Analysis (IRAT Source Cell) VS No: 4.10.12 (IRAT Drill on
     * Source Cell)
     */
    @Test
    public void accessAreaTotalSOURCEHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisIRAT_7_3h() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow + ACTUAL_WINDOW_HEADER
                + windowHeadersDrillDown);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow));

    }

    /*
     * EE12.2_WHFA_7.3i; Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH Source Cell) VS No:4.10.13 (HSDSCH Drill for
     * Source Cell)
     */
    @Test
    public void accessAreaTotalSOURCEHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisHSDSCH_7_3i() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.SOURCE_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER
                + windowHeadersDrillDown);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow));

    }

    /*
     * EE12.2_WHFA_7.3j; Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH Target Cell) VS No: 4.10.13
     */
    @Test
    public void accessAreaTotalTARGETHOFailureAnalysisOnNetworkTabToDetailedEventAnalysisHSDSCH_7_3j() throws InterruptedException, PopUpException,
            NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), BaseWcdmaHfa.Handover_Failure_WCDMA,
                BaseWcdmaHfa.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        networkEventAnalysisWCDMAHFA.clickTableCell(rowIndex, GuiStringConstants.TARGET_FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER
                + windowHeadersDrillDown);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAHSDSCHWindow));

    }

    /*
     * 4.10.42 13B_WCDMA_CFA_HFA_4.10.42: WCDMA_HFA: Cause Code Analysis by Access Area - Grid View
     */
    @Test
    public void causeCodeAnalysisByAccessAreaFirstWindow_7_6() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedAccessAreaCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedAccessAreaCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.43 13B_WCDMA_CFA_HFA_4.10.43: WCDMA_HFA: Drill down from Access Area Cause Code Analysis to Subcase Code -Grid view [SOHO/IFHO/IRAT] SOHO
     * Implementation
     */

    @Test
    public void sohoCauseCodeAnalysisByAccessArea_7_6a() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, ACCESS_AREA_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedControllerCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedControllerCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.43 13B_WCDMA_CFA_HFA_4.10.43: WCDMA_HFA: Drill down from Access Area Cause Code Analysis to Subcase Code -Grid view [SOHO/IFHO/IRAT] SOHO
     * Implementation Drill down on sub cause code
     */
    @Ignore
    @Test
    public void sohoCauseCodeAnalysisByAccessArea_7_6b() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.LABEL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.LABEL));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.SUB_CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedSOHOCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedSOHOCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
                    GuiStringConstants.TARGET_CELL, GuiStringConstants.SOURCE_RAC, GuiStringConstants.SOURCE_LAC);
        }
    }

    /*
     * 4.10.44 13B_WCDMA_CFA_HFA_4.10.44: WCDMA_HFA: Drill down from Access Area Cause Code Analysis to Detailed Event Analysis -Grid view [HSDSCH]
     * HSDSCH Implementation - Drill down on cause code
     */
    @Ignore
    @Test
    public void hsdschCauseCodeAnalysisByAccessArea_7_6c() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA), Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.ACCESS_AREA) + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedHSDSCHCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedHSDSCHCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.TARGET_CELL, GuiStringConstants.SOURCE_RAC, GuiStringConstants.SOURCE_LAC);
        }
    }

    /*
     * EE12.2_WHFA_7.4; Access Area Group Handover Failure Analysis – Summary Pane – 2G Cell Group VS No: 4.10.14
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessAreaGroupSUMMARY2GHandoverFailureAnalysisOnNetworkTab_7_4() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
    }

    /*
     * EE12.2_WHFA_7.4a; Access Area Group Handover Failure Analysis – Summary Pane – 3G Cell Group VS No: 4.10.15 (SOHO Drill on Source Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_SUMMARY_3G_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4a() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));

    }

    /*
     * FRED EE12.2_WHFA_7.4b; Drill down from Access Area Group total per HO type to Detailed Event Analysis (SOHO – Source Cell) VS No: 4.10.15
     * (SOHO Drill on Source Cell) Same test as previous
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4b() throws InterruptedException,
            PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));
    }

    /*
     * EE12.2_WHFA_7.4c; Drill down from Access Area Group total per HO type to Detailed Event Analysis (SOHO – Target Cell) VS No: 4.10.15 (SOHO
     * Drill on Target Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4c() throws InterruptedException,
            PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));
    }

    /*
     * FRED EE12.2_WHFA_7.4d; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IFHO Source Cell) VS No: 4.10.16 (IFHO
     * Drill on Source Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_4d() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));
    }

    /*
     * EE12.2_WHFA_7.4e; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IFHO Target Cell) VS No: 4.10.16 (IFHO Drill
     * on Target Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_4e() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));
    }

    /*
     * EE12.2_WHFA_7.4f; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IRAT Source Cell) VS No: 4.10.17 (IRAT Drill
     * on Source Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_4f() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + ACTUAL_WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));
    }

    /*
     * EE12.2_WHFA_7.4g; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IRAT Target Cell) VS No: 4.10.17 (IRAT Drill
     * on Target Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_4g() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + ACTUAL_WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));
    }

    /*
     * EE12.2_WHFA_7.4h; Drill down from Access Area Group total per HO type to Detailed Event Analysis (HSDSCH Source Cell) VS No: 4.10.18 (HSDSCH
     * Drill on Source Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_4h() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupSourceNetworkDetailEventAnalysisWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupSourceNetworkDetailEventAnalysisWCDMAHSDSCHWindow));
    }

    /*
     * EE12.2_WHFA_7.4i; Drill down from Access Area Group total per HO type to Detailed Event Analysis (HSDSCH Target Cell) VS No: 4.10.18 (HSDSCH
     * Drill on Target Cell)
     */
    @Ignore("Problem with Access Area Group, previous JIRA on this")
    @Test
    public void accessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_4i() throws InterruptedException,
            PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, BaseWcdmaHfa.ACCESS_AREA_GROUP_VALUE,
                BaseWcdmaHfa.Handover_Failure_WCDMA, BaseWcdmaHfa.Network_Event_Analysis);

        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));

        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE,
                GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow + ACTUAL_WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow));

    }

    /*
     * VS No. 4.10.19 13B_WCDMA_CFA_HFA_4.10.19: WCDMA_HFA: Cause Code Analysis by Controller - Grid view
     */
    @Test
    public void causeCodeAnalysisByControllerFirstWindow_7_5() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultCauseCodeControllerEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultCauseCodeControllerEventAnalysisWindow));

    }

    /*
     * 4.10.20 13B_WCDMA_CFA_HFA_4.10.20: WCDMA_HFA: Drill down from Controller Cause Code Analysis to Sub Cause Code -Grid view [SOHO/IFHO/IRAT] SOHO
     * Implementation
     */
    @Test
    public void sohoCauseCodeAnalysisByController_7_5a() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);

        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_VALUE) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = networkEventAnalysisWCDMAHFA.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedControllerCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedControllerCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.20 13B_WCDMA_CFA_HFA_4.10.20: WCDMA_HFA: Drill down from Controller Cause Code Analysis to Sub Cause Code -Grid view [SOHO/IFHO/IRAT] IFHO
     * Implementation
     */
    @Test
    public void ifhoCauseCodeAnalysisByController_7_5b() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_VALUE) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedControllerCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedControllerCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.20 13B_WCDMA_CFA_HFA_4.10.20: WCDMA_HFA: Drill down from Controller Cause Code Analysis to Sub Cause Code -Grid view [SOHO/IFHO/IRAT] IRAT
     * Implementation
     */
    @Test
    public void iratCauseCodeAnalysisByController_7_5c() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_VALUE) + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedControllerCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedControllerCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.21 13B_WCDMA_CFA_HFA_4.10.21: WCDMA_HFA: Drill down from Controller Cause Code Analysis to Detailed Event Analysis -Grid view [HSDSCH]
     * HSDSCH Implementation
     */
    @Ignore
    @Test
    public void hsdschCauseCodeAnalysisByController_7_5d() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedHSDSCHCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedHSDSCHCauseCodeEventAnalysisWindow));
    }

    /*
     * 4.10.22 13B_WCDMA_CFA_HFA_4.10.22: WCDMA_HFA: Drill down from Controller Sub Cause Code to Detailed Event Analysis- [SOHO] SOHO Implementation
     * (Drill down on Sub Cause Code)
     */
    @Ignore
    @Test
    public void sohoCauseCodeAnalysisByController_7_5e() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.LABEL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.LABEL_2));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.SUB_CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedSOHOCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedSOHOCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.TARGET_CELL);
        }
    }

    /*
     * 4.10.23 13B_WCDMA_CFA_HFA_4.10.23: WCDMA_HFA: Drill down from Controller Sub Cause Code to Detailed Event Analysis- [IRAT] IRAT Implementation
     * (Drill down on Sub Cause Code)
     */
    @Ignore
    @Test
    public void iratCauseCodeAnalysisByController_7_5f() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.LABEL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.LABEL_2));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.SUB_CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedIRATCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedIRATCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.TARGET_CELL);
        }
    }

    /*
     * 4.10.24 13B_WCDMA_CFA_HFA_4.10.24: WCDMA_HFA: Drill down from Controller Sub Cause Code to Detailed Event Analysis- [IFHO-Grid view] IFHO
     * Implementation (Drill down on Sub Cause Code)
     */
    @Ignore
    @Test
    public void ifhoCauseCodeAnalysisByController_7_5g() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.LABEL,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.LABEL_2));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.SUB_CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.RNC) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedIFHOCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedIFHOCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                    reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.TARGET_CELL);
        }
    }

    /*
     * 4.10.32 13B_WCDMA_CFA_HFA_4.10.31: WCDMA_HFA: Cause Code Analysis by Controller Group - Grid View SOHO Implementation
     */
    @Test
    public void sohoCauseCodeAnalysisByControllerGroupFirstWindow_9_32() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.32 13B_WCDMA_CFA_HFA_4.10.31: WCDMA_HFA: Cause Code Analysis by Controller Group - Grid View SOHO Implementation
     */
    @Test
    public void sohoCauseCodeAnalysisByControllerGroup_9_33() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, BaseWcdmaHfa.CONTROLLER_GROUP_VALUE,
                Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE) + GuiStringConstants.DASH
                + CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultDetailedCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultDetailedCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.32 13B_WCDMA_CFA_HFA_4.10.31: WCDMA_HFA: Cause Code Analysis by Controller Group - Grid View IRAT Implementation
     */
    @Test
    public void iratCauseCodeAnalysisByControllerGroup_9_34() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE) + GuiStringConstants.DASH
                + CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(defaultDetailedCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.32 13B_WCDMA_CFA_HFA_4.10.31: WCDMA_HFA: Cause Code Analysis by Controller Group - Grid View IFHO Implementation
     */
    @Test
    public void ifhoCauseCodeAnalysisByControllerGroup_9_35() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE) + GuiStringConstants.DASH
                + CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders1 = causeCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultDetailedCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(BaseWcdmaHfa.defaultDetailedCauseCodeEventAnalysisWindow));

    }

    /*
     * 4.10.33 13B_WCDMA_CFA_HFA_4.10.33: WCDMA_HFA: Drill down from Controller Group Cause Code Analysis to Detailed Event Analysis -Grid view
     * [HSDSCH HSDSCH Implementation
     */
    @Ignore
    @Test
    public void hsdschCauseCodeAnalysisByControllerGroup_9_36() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);
        gridGraphviewRankingGroupTests.openGridView();

        int rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        gridGraphviewRankingGroupTests.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String actualWindowTitle = gridGraphviewRankingGroupTests.getWindowHeaderLabel();
        Assert.assertEquals(GuiStringConstants.ERROR_LOADING, expectedWindowTitle, actualWindowTitle);

        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + defaultDetailedHSDSCHCauseCodeEventAnalysisWindow + ACTUAL_WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultDetailedHSDSCHCauseCodeEventAnalysisWindow));

        if (isDataIntegrityFlagOn()) {
            rowIndex = gridGraphviewRankingGroupTests.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            Map<String, String> result = gridGraphviewRankingGroupTests.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.TARGET_CELL,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL);
        }
    }

    /*
     * 44.10.38 13B_WCDMA_CFA_HFA_4.10.38: WCDMA_HFA: Drill down from Controller Group Cause Code Analysis to Detailed Event Analysis - Chart view
     * [HSDSCH]
     */

    @Test
    public void causeCodeAnalysisByControllerGroup_10_1() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC49-HSDSCH_NO_CELL_SELEC...");
        itemsToSelectList.add("CC49-HSDSCH_FAILED_CELL_C...");
        itemsToSelectList.add("CC49-IFHO_OUT_HARD_HO_FAI...");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));

        final String expectedWindowTitle2 = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.SPACE
                + GuiStringConstants.FORCED_PROCEDURE_ABORT;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));

    }

    /*
     * 4.10.37 13B_WCDMA_CFA_HFA_4.10.37: WCDMA_HFA: Drill down from Controller Group Cause Code Analysis to Subcase Code -Chart view [SOHO/IFHO/IRAT]
     * IRAT Implementation
     */
    @Test
    public void causeCodeAnalysisByControllerGroup_10_1a() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, CONTROLLER_GROUP_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC101-IRAT_OUT_HARD_HO_FA...");
        itemsToSelectList.add("CC89-IRAT_OUT_HARD_HO_FAI...");
        itemsToSelectList.add("CC112-IRAT_OUT_HARD_HO_FA...");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));
        gridGraphviewRankingGroupTests.closeWindow();

        final String expectedWindowTitle2 = CONTROLLER_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.SUB_CAUSE_CODES_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN + reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_VALUE)
                + GuiStringConstants.BRACKET_CLOSE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.SUB_CAUSE_CODE));

    }

    /*
     * VS No: 4.10.27 13B_WCDMA_CFA_HFA_4.10.27: WCDMA_HFA: Drill down from Controller Cause Code Analysis to Detailed Event Analysis - Chart view
     * [HSDSCH]
     */
    @Test
    public void causeCodeAnalysisByController_10_1b() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC49-HSDSCH_NO_CELL_SELEC...");
        itemsToSelectList.add("CC49-HSDSCH_FAILED_CELL_C...");
        itemsToSelectList.add("CC49-IFHO_OUT_HARD_HO_FAI...");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA + GuiStringConstants.THREE_G
                + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));

        final String expectedWindowTitle2 = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.FORCED_PROCEDURE_ABORT;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));

    }

    /*
     *
     * IFHO
     */
    @Test
    public void causeCodeAnalysisByController_10_1d() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, RNC09, Handover_Failure_WCDMA, CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC2-IFHO_EXE_ACTIVE");
        itemsToSelectList.add("CC3-IFHO_EXE_ACTIVE");
        itemsToSelectList.add("CC19-IFHO_OUT_HARD_HO_FAI...");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA + GuiStringConstants.THREE_G
                + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));
        gridGraphviewRankingGroupTests.closeWindow();

        final String expectedWindowTitle2 = reservedDataHelperReplacement.getReservedData(GuiStringConstants.CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA + GuiStringConstants.THREE_G
                + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_HANDOVER_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODES_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN
                + reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_VALUE) + GuiStringConstants.BRACKET_CLOSE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));
        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.SUB_CAUSE_CODE));

    }

    /*
     * VS No: 4.10.50 13B_WCDMA_CFA_HFA_4.10.50: WCDMA_HFA: Drill down from Access Area Cause Code Analysis to Detailed Event Analysis -Chart view
     * [HSDSCH]
     */
    @Test
    public void causeCodeAnalysisByAccessArea_10_1c() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, ACCESS_AREA_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC9-HSDSCH_NO_CELL_SELECT...");
        itemsToSelectList.add("CC71-HSDSCH_FAILED_CELL_C...");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = ACCESS_AREA_VALUE + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));

        final String expectedWindowTitle2 = ACCESS_AREA_VALUE + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.OTHER;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));

    }

    /*
     * VS No: 4.10.49 13B_WCDMA_CFA_HFA_4.10.49: WCDMA_HFA: Drill down from Access Area Cause Code Analysis to Subcase Code -Chart view
     * [SOHO/IFHO/IRAT] SOHO Implementation
     */
    @Test
    public void causeCodeAnalysisByAccessArea_10_1e() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, ACCESS_AREA_VALUE, Handover_Failure_WCDMA,
                CAUSE_CODE_ANALYSIS);

        final List<String> itemsToSelectList = new ArrayList<String>();
        itemsToSelectList.add("CC2-SOHO");
        itemsToSelectList.add("CC3-SOHO");

        gridGraphviewRankingGroupTests.openChartViewUsingDisplayedNames(itemsToSelectList);

        final String expectedWindowTitle = ACCESS_AREA_VALUE + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_HANDOVER_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.CAUSE_CODE_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.CAUSE_CODE));
        gridGraphviewRankingGroupTests.closeWindow();

        final String expectedWindowTitle2 = ACCESS_AREA_VALUE + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.TIMEOUT_ON_RRC;

        gridGraphviewRankingGroupTests.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(GuiStringConstants.SUB_CAUSE_CODE));

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle2, selenium.isTextPresent(expectedWindowTitle2));

    }

    /**
     * Drill down one link on Failure column on event analysis.
     *
     * @param window
     *        the object of CommonWindow
     * @param columnToCheck
     *        This column to locate row number
     * @param values
     *        These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownOnParticularCell(final String drillDownHeader, final CommonWindow window, final String columnToCheck,
                                           final String... values) throws NoDataException, PopUpException {
        //window.sortTable(SortType.DESCENDING, columnToCheck);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, drillDownHeader);
        waitForPageLoadingToComplete();

        final String eventType = GuiStringConstants.EVENT_TYPE;
        checkInOptionalHeadersWCDMA(window, eventType);
    }

    /*
     * This method is to do the basic check on the windows Repeat of Method in TerminalTestGroup
     *
     * @param commonWindow - The window id eswasas: for new GUI launch with workspace, selenium exception is being thrown from commonWindow for loop
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
                final int pageCount = commonWindow.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    commonWindow.clickNextPage();
                }
            }
        }

    }

    private void checkInOptionalHeadersWCDMA(final CommonWindow window, final String eventType) {
        window.openTableHeaderMenu(0);
        window.checkInOptionalHeaderCheckBoxes(headersToTickIfPresent);
    }

    /**
     * check if Data integrity flag is True
     *
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(BaseWcdmaHfa.TRUE);
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
}
