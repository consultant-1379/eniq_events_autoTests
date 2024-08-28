/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.CSVReaderCFAHFA;
import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TableNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

//@SuppressWarnings("deprecation")
public class RankingTestGroupWcdmaCfaNewUI extends BaseWcdmaCfa {

    private String dataIntegrityFlag;

    @Autowired
    @Qualifier("wcdmaCFARANRanking")
    private CommonWindow wcdmaCallFailureRANRankings;

    @Autowired
    @Qualifier("accessAreaCFARanking")
    private CommonWindow accessAreaCFARankingWindow;

    @Autowired
    @Qualifier("subscriberCallSetupFailuresRanking")
    private CommonWindow subscriberCallSetupFailureRankingWindow;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallDrops")
    private CommonWindow causeCodeCallDropsWindow;

    @Autowired
    @Qualifier("rankingsDisconnectionCodeWcdmaCallDrops")
    private CommonWindow disconnectionCodeCallDropsWindow;

    @Autowired
    @Qualifier("detailedEventAnalysisWcdmaCfaCallDrop")
    private CommonWindow detailedEventAnalysisCallDropWindow;

    @Autowired
    @Qualifier("rankingsTerminalWcdmaCfa")
    private CommonWindow rankingsTerminalWcdmaCfa;

    @Autowired
    @Qualifier("terminalEventAnalysisWcdmaCfaTac")
    private CommonWindow terminalEventAnalysisWcdmaCfaTac;

    @Autowired
    @Qualifier("causeCodeAnalysisCallSetupFailureWcdmaCfa")
    private CommonWindow causeCodeAnalysisCallSetupFailureWcdmaCfa;

    @Autowired
    @Qualifier("subCauseCodeAnalysisCallDropsWcdmaCfa")
    private CommonWindow subCauseCodeAnalysisCallDrops;

    @Autowired
    @Qualifier("subCauseCodeAnalysisCallSetupWcdmaCfa")
    private CommonWindow subCauseCodeAnalysisCallSetup;

    @Autowired
    @Qualifier("subscriberRankingCallDrops")
    private CommonWindow subscriberRankingCallDrops;

    @Autowired
    @Qualifier("terminalDisconnectionCodeAnalysisFromDrillByTAC")
    private CommonWindow terminalDisconnectionCodeAnalysisFromDrillByTAC;

    @Autowired
    @Qualifier("terminalRabDescAnalysisFromDrillByTAC")
    private CommonWindow terminalRabDescAnalysisFromDrillByTAC;

    @Autowired
    @Qualifier("terminalAnalysisCfaMostDrops")
    private CommonWindow terminalAnalysisCfaMostDrops;

    @Autowired
    @Qualifier("terminalGroupAnalysisWcdmaCfa")
    private CommonWindow terminalGroupAnalysisWcdmaCfa;

    @Autowired
    @Qualifier("ranWcdmaRoamingByCountry")
    private CommonWindow ranWcdmaRoamingByCountry;

    @Autowired
    @Qualifier("ranWcdmaRoamingByOperator")
    private CommonWindow ranWcdmaRoamingByOperator;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaBscCallSetup")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaBscCallSetup;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops;

    @Autowired
    @Qualifier("accessAreaNetworkEventAnalysisCell")
    private CommonWindow accessAreaNetworkEventAnalysisCell;

    @Autowired
    @Qualifier("wcdmaCFANetworkEventAnalysis")
    private CommonWindow wcdmaCFANetworkEventAnalysis;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaCellCallSetupConfig")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaCellCallSetupConfig;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaCellCallDropsConfig")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaCellCallDrops;

    @Autowired
    @Qualifier("rankingsTerminalWcdmaCfaToggleToGrid")
    private CommonWindow rankingsTerminalWcdmaCfaToggleToGrid;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaCfaReserveDataS.csv");

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

    /*
     * 4.6.1 13B_WCDMA_CFA_HFA_4.6.1: WCDMA_CFA: Verify RNC Ranking Views New GUI, Selects Total Ran Failures and Call Setup failures and assert
     * values
     */

    @Test
    public void networkTabVerifyWcdmaCfaRncRankingViews_5_5_1() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        // Set expected Window Header
        String expectedWindowTitle = GuiStringConstants.THREE_G_RADIO_NETWORK + GuiStringConstants.DASH + GuiStringConstants.RANKING
                + GuiStringConstants.DASH + GuiStringConstants.CALL_FAILURE_BY_CONTROLLER + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;

        //Check Window Header
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        //Log WindowHeader Columns
        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        //Check
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        //Check Window HeaderColumns
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultCallFailureAnalysisColumns));

    }

    /*
     * 4.6.1 13B_WCDMA_CFA_HFA_4.6.1: WCDMA_CFA: Verify RNC Ranking Views New GUI, Selects Total Ran Failures and Call Drops and assert values
     */

    @Test
    public void networkTabVerifyWcdmaCfaRncRankingViews_5_5_1a() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        final String expectedWindowTitle = selenium.getText(WcdmaStingConstantsNewUI.Ranking_Table_Title);

        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.RANKING_TABLE_TITLE, expectedWindowTitle);

        //Log WindowHeader Columns
        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        //Check
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        //Check Window HeaderColumns
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultCallFailureAnalysisColumns));

    }

    /* Testing for CI JIRA EQEV 8501- CFA Window Hangs */
    @Test
    public void VerifyWcdmaAccessControllerMultiSelectOptions() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);
        final String expectedWindowTitle = selenium.getText(WcdmaStingConstantsNewUI.Ranking_Table_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.CALL_FAILURE_ANALYSIS_BY_CONTROLLER, expectedWindowTitle);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);
        final String expectedWindowTitle1 = selenium.getText(WcdmaStingConstantsNewUI.ACCESS_AREA_POPUP_TITLE);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.CALL_FAILURE_ANALYSIS_BY_ACCESS_AREA,
                expectedWindowTitle1);

        pause(1000);
        selenium.waitForPageLoadingToComplete();

        final String dropdown1 = "//html/body/div/div/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/div/div/div/div/div/div/table/tbody/tr/td[2]/div/div/img";
        selenium.mouseOver(dropdown1);
        selenium.click(dropdown1);
        pause(1000);
        selenium.waitForPageLoadingToComplete();
        final String dropdown2 = "//html/body/div/div/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/div/div/div/div/div/div/table/tbody/tr[2]/td[2]/div/div/img";
        selenium.mouseOver(dropdown2);
        selenium.click(dropdown2);
        pause(1000);
        final String dropdown3 = "//html/body/div/div/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/div/div/div/div/div/div/table/tbody/tr[3]/td[2]/div/div/img";
        selenium.mouseOver(dropdown3);
        selenium.click(dropdown3);
        pause(1000);
        selenium.waitForPageLoadingToComplete();
    }

    /*
     * Need VS UPDATE 4.6.2 13B_WCDMA_CFA_HFA_4.6.2: WCDMA_CFA: From RNC Ranking, verify drilldown to Controller Event Analysis View Drill By Event
     * Analysis Summary New GUI, Selects Total Rab Failures and Call Setup failures, then drill down on Controller and assert values
     */

    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToControllerEventAnalysisView_5_5_2() throws PopUpException, NoDataException,
            InterruptedException {
        //launch Window
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        final String controller = reservedDataHelperReplacement.getReservedData(CONTROLLER);

        int rowIndex = wcdmaCallFailureRANRankings.getRowNumberWithMatchingValueForGivenColumn(CONTROLLER, controller);

        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, CONTROLLER);

        final String expectedWindowTitle = controller + GuiStringConstants.DASH + GuiStringConstants.NETWORK_EVENT_ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = wcdmaCFANetworkEventAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));

    }

    /*
     * Need VS UPDATE 4.6.2 13B_WCDMA_CFA_HFA_4.6.2: WCDMA_CFA: From RNC Ranking, verify drilldown to Controller Event Analysis View Drill By Event
     * Analysis Summary New GUI, Selects Total Rab Failures and Call Drops, then drill down on Controller and assert values
     */

    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToControllerEventAnalysisView_5_5_2a() throws PopUpException, NoDataException,
            InterruptedException {
        //launch Window
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);
        int rowIndex = wcdmaCallFailureRANRankings.getRowNumberWithMatchingValueForGivenColumn(CONTROLLER,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));

        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, CONTROLLER);
        final List<String> actualWindowHeaders = wcdmaCFANetworkEventAnalysis.getTableHeaders();

        String controller = reservedDataHelperReplacement.getReservedData(CONTROLLER);
        final String expectedWindowTitle = controller + GuiStringConstants.DASH + GuiStringConstants.NETWORK_EVENT_ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));

    }

    /*
     * Need VS UPDATE 4.6.3 13B_WCDMA_CFA_HFA_4.6.3: WCDMA_CFA: From RNC Ranking, verify drilldown to Failed Event Analysis Drill oN Event Type
     * Failure and assert the record
     *
     * New GUI, Selects Total Rab Failures and Call Setup failures, then drill down on Controller > Failures by Detailed event analysis and finally
     * assert the values
     */
    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToFailedEventAnalysis_5_5_3() throws PopUpException, NoDataException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        final String controller = reservedDataHelperReplacement.getReservedData(CONTROLLER);

        int rowIndex = wcdmaCallFailureRANRankings.getRowNumberWithMatchingValueForGivenColumn(CONTROLLER, controller);

        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, CONTROLLER);
        wcdmaCallFailureRANRankings.closeWindow();

        rowIndex = wcdmaCFANetworkEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        wcdmaCFANetworkEventAnalysis.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        wcdmaCFANetworkEventAnalysis.closeWindow();

        final String expectedWindowTitle = controller + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkDetailedEventAnalysisWcdmaCfaBscCallSetup.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));

    }

    /*
     * Need VS UPDATE 4.6.3 13B_WCDMA_CFA_HFA_4.6.3: WCDMA_CFA: From RNC Ranking, verify drilldown to Failed Event Analysis Drill oN Event Type
     * Failure and assert the record
     *
     * New GUI, Selects Total Rab Failures and Call Setup failures, then drill down on Controller > Failures by Detailed event analysis and finally
     * assert the values
     */

    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToFailedEventAnalysis_5_5_3a() throws PopUpException, NoDataException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Controller,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        int rowIndex = wcdmaCallFailureRANRankings.getRowNumberWithMatchingValueForGivenColumn(CONTROLLER,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));

        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, CONTROLLER);
        wcdmaCallFailureRANRankings.closeWindow();

        rowIndex = wcdmaCFANetworkEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        wcdmaCFANetworkEventAnalysis.clickTableCell(rowIndex, FAILURES);

        selenium.click(WcdmaStingConstantsNewUI.Detailed_Event_Analysis);
        wcdmaCFANetworkEventAnalysis.closeWindow();
        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.FAILED_EVENT_ANALYSIS,
                selenium.isTextPresent(GuiStringConstants.FAILED_EVENT_ANALYSIS));

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisTerminalColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultCallFailureAnalysisTerminalColumns));

    }

    /*
     * 4.6.4 13B_WCDMA_CFA_HFA_4.6.4: WCDMA_CFA: Verify Access Area (Cell) Ranking Views. New GUI, Selects Total Rab Failures and Call Setup failures
     * and finally assert the values
     */

    @Test
    public void networkTabVerifyWcdmaCfaAccessAreaCellRankingViews_5_5_4() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        final String expectedWindowTitle = GuiStringConstants.THREE_G_RADIO_NETWORK + GuiStringConstants.DASH + GuiStringConstants.RANKING
                + GuiStringConstants.DASH + GuiStringConstants.CALL_FAILURE_BY_ACCESS_AREA + GuiStringConstants.GREATER_THEN_SIGN
                + TOTAL_RAB_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaCFAColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultAccessAreaCFAColumns));

    }

    /*
     * 4.6.4 13B_WCDMA_CFA_HFA_4.6.4: WCDMA_CFA: Verify Access Area (Cell) Ranking Views. New GUI, Selects Total Rab Failures and Call Drops and
     * finally assert the values
     */

    @Test
    public void networkTabVerifyWcdmaCfaAccessAreaCellRankingViews_5_5_4a() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        final String expectedWindowTitle = selenium.getText(WcdmaStingConstantsNewUI.Ranking_Table_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.RANKING_TABLE_AA_TITLE, expectedWindowTitle);

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaCFAColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultAccessAreaCFAColumns));

    }

    /*
     * Need VS UPDATE 4.6.5 13B_WCDMA_CFA_HFA_4.6.5: WCDMA_CFA: From Access Area Ranking, verify drilldown to Failed Event Analysis View. Drill Down
     * by Event Analysis Summary and assert the record by event type GUI, Selects Total Rab Failures and Call Setup failures, then drill down on
     * access area and finally assert the values
     */

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingsVerifyDrilldownToAccessAreaEventAnalysisView_5_5_5() throws NoDataException, PopUpException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        final String accessArea = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.getRowNumberWithMatchingValueForGivenColumn(ACCESS_AREA, accessArea);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, ACCESS_AREA);
        accessAreaCFARankingWindow.closeWindow();

        final String expectedWindowTitle = accessArea + DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.GREATER_THEN_SIGN
                + TOTAL_RAB_FAILURES;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaNetworkEventAnalysisCell.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultAccessAreaEventAnalysisColumns));

    }

    /*
     * Need VS UPDATE 4.6.5 13B_WCDMA_CFA_HFA_4.6.5: WCDMA_CFA: From Access Area Ranking, verify drilldown to Failed Event Analysis View. Drill Down
     * by Event Analysis Summary and assert the record by event type GUI, Selects Total Rab Failures and Call Drops, then drill down on access area
     * and finally assert the values
     */

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingsVerifyDrilldownToAccessAreaEventAnalysisView_5_5_5a() throws NoDataException, PopUpException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        final String accessArea = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.getRowNumberWithMatchingValueForGivenColumn(ACCESS_AREA, accessArea);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, ACCESS_AREA);

        final String expectedWindowTitle = accessArea + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA + GuiStringConstants.SPACE
                + GuiStringConstants.EVENT_ANALYSIS + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaNetworkEventAnalysisCell.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultAccessAreaEventAnalysisColumns));

    }

    /*
     * Need VS UPDATE 13B_WCDMA_CFA_HFA_4.6.5: WCDMA_CFA: From Access Area Ranking, verify drilldown to Failed Event Analysis View Drill down by Event
     * Analysis Summary then, further drill down on Event Type failure and finally assert record
     */

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingVerifyDrilldownToFailureAnalysisView_5_5_6() throws NoDataException, PopUpException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.getRowNumberWithMatchingValueForGivenColumn(ACCESS_AREA, accessAreaValue);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, ACCESS_AREA);
        accessAreaCFARankingWindow.closeWindow();

        rowIndex = accessAreaNetworkEventAnalysisCell.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        accessAreaNetworkEventAnalysisCell.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        accessAreaNetworkEventAnalysisCell.closeWindow();

        final String expectedWindowTitle = accessAreaValue + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH
                + WCDMA_CALL_FAILURE + GuiStringConstants.GREATER_THEN_SIGN + TOTAL_RAB_FAILURES;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkDetailedEventAnalysisWcdmaCfaCellCallSetupConfig.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFEAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFEAnalysisWindowColumns));

    }

    /*
     * Need VS UPDATE 13B_WCDMA_CFA_HFA_4.6.5: WCDMA_CFA: From Access Area Ranking, verify drilldown to Failed Event Analysis View Drill down by Event
     * Analysis Summary then, further drill down on Event Type failure and finally assert record
     */

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingVerifyDrilldownToFailureAnalysisView_5_5_6a() throws NoDataException, PopUpException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_AccessArea,
                WcdmaStingConstantsNewUI.Ranking_3G);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.getRowNumberWithMatchingValueForGivenColumn(ACCESS_AREA, accessAreaValue);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, ACCESS_AREA);
        accessAreaCFARankingWindow.closeWindow();

        rowIndex = accessAreaNetworkEventAnalysisCell.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        accessAreaNetworkEventAnalysisCell.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        accessAreaNetworkEventAnalysisCell.closeWindow();

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.FAILED_EVENT_ANALYSIS,
                selenium.isTextPresent(GuiStringConstants.FAILED_EVENT_ANALYSIS));

        final String expectedWindowTitle = accessAreaValue + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkDetailedEventAnalysisWcdmaCfaCellCallDrops.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultcallDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultcallDropsFailedEventAnalysisColumns));

    }

    /*
     * 4.6.6 13B_WCDMA_CFA_HFA_4.6.6: WCDMA_CFA: From Cause Code Ranking, verify drilldown to the Network Cause Code Analysis Views.
     */

    @Test
    public void networkTabVerifyWcdmaCfaCauseCodeRankingViews_5_5_7a() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.CauseCode_Ranking_3G);

        final String expectedWindowTitle = GuiStringConstants.CAUSE_CODE_WCDMA_CALL_FAILURE_BY_CALL_DROPS_EVENT_RANKING;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeCallDropsWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));

    }

    /*
     * VS UPDATE NEEDED 4.6.6 13B_WCDMA_CFA_HFA_4.6.6: WCDMA_CFA: From Cause Code Ranking, verify drilldown to the Network Cause Code Analysis Views.
     */
    @Test
    public void networkTabVerifyWcdmaCfaCauseCodeRankingViews_5_5_7b() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_SetupFailure,
                WcdmaStingConstantsNewUI.CauseCode_Ranking_3G);

        final String expectedWindowTitle = CAUSE_CODE_WCDMA_CALL_FAILURE_BY_CALL_SETUP_FAILURE_EVENT_RANKING;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeAnalysisCallSetupFailureWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));

    }

    /*
     * VS UPDATE NEEDED Select '3G Radio Network' from the '- Select Dimension -' dropdown menu. Select '3G Cause Code Ranking' Analysis Window and
     * then 'Call Drops' from the sub-menu that appears. Click 'Launch'. Drill Down on Cause Code and assert record in Sub Cause Code window
     */

    @Test
    public void networkTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheNetworkCauseCodeAnalysisViews_5_5_8a() throws PopUpException,
            NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.CauseCode_Ranking_3G);

        int rowIndex = causeCodeCallDropsWindow.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final String causeCode = causeCodeCallDropsWindow.getTableData(rowIndex, 1);
        final String causeCodeId = causeCodeCallDropsWindow.getTableData(rowIndex, 2);
        final String expectedWindowTitle = causeCode + GuiStringConstants.COMMA_SPACE + causeCodeId + DASH
                + SUB_CAUSE_CODE_ANALYSIS_FOR_WCDMA_CFA_CALL_DROPS;

        causeCodeCallDropsWindow.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subCauseCodeAnalysisCallDrops.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));

    }

    /*
     * VS UPDATE NEEDED Select '3G Radio Network' from the '- Select Dimension -' dropdown menu. Select '3G Cause Code Ranking' Analysis Window and
     * then 'Call Setup Failures' from the sub-menu that appears. Click 'Launch'. Drill Down on Cause Code and assert record in Sub Cause Code window
     */

    @Test
    public void networkTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheNetworkCauseCodeAnalysisViews_5_5_8b() throws PopUpException,
            NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_SetupFailure,
                WcdmaStingConstantsNewUI.CauseCode_Ranking_3G);

        int rowIndex = causeCodeAnalysisCallSetupFailureWcdmaCfa.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final String causeCodeDescription = causeCodeAnalysisCallSetupFailureWcdmaCfa.getTableData(rowIndex, 1);
        final String causeCodeId = causeCodeAnalysisCallSetupFailureWcdmaCfa.getTableData(rowIndex, 2);
        final String expectedWindowTitle = causeCodeDescription + GuiStringConstants.COMMA_SPACE + causeCodeId + DASH
                + SUB_CAUSE_CODE_ANALYSIS_FOR_WCDMA_CFA_CALL_SETUP;

        causeCodeAnalysisCallSetupFailureWcdmaCfa.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subCauseCodeAnalysisCallSetup.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));

    }

    /*
     * 4.8.1 13B_WCDMA_CFA_HFA_4.8.1: WCDMA_CFA: From WCDMA CFA Subscriber Ranking, verify drilldown to IMSI Event Analysis. Action 1: Click on the
     * 'Launch' menu icon at the left screen edge. Select '1 hour' from the time period drop down menu. Select '3G Radio Network' from the '- Select
     * Dimension -' dropdown menu. Select the '3G Ranking' Analysis Window and then 'Call Setup Failure' from the sub-menu that appears. Click
     * 'Launch'.
     */
    @Test
    public void rankingVerifyWcdmaCfaCallDrops3GRanking_5_8_1() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.Ranking_3G);

        // Set expected Window Header
        String expectedWindowTitle = GuiStringConstants.SUBSCRIBER_WCDMA_CALL_FAILURE_BY_CALL_DROPS_EVENT_RANKING;

        //Check Window Header
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        //        subscriberRankingCallDrops

        final List<String> actualWindowHeaders = subscriberRankingCallDrops.getTableHeaders();
        //Check
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + subscriberCallFailureByCallDropsEventRanking
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        //Check Window HeaderColumns
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(subscriberCallFailureByCallDropsEventRanking));

    }

    /*
     * 4.8.1 13B_WCDMA_CFA_HFA_4.8.1: WCDMA_CFA: From WCDMA CFA Subscriber Ranking, verify drilldown to IMSI Event Analysis. Action 2 : Drill-down
     * into the hyper-linked IMSI to show the summary event information for the IMSI.
     */
    @Test
    public void rankingVerifyWcdmaCfaCallDrops3GRanking_5_8_2() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.Ranking_3G);

        int rowIndex = subscriberRankingCallDrops.getRowNumberWithMatchingValueForGivenColumn(RANK,
                reservedDataHelperReplacement.getReservedData(RANK));

        subscriberRankingCallDrops.clickTableCell(rowIndex, IMSI);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(IMSI) + GuiStringConstants.DASH
                + GuiStringConstants.IMSI_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subscriberRankingCallDrops.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsImsiEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(callDropsImsiEventAnalysisColumns));

    }

    /*
     * 4.7.2 13B_WCDMA_CFA_HFA_4.7.2: WCDMA_CFA: From Terminal Call Failure Analysis Ranking, verify Failed Event Analysis (RAB Type) Action 1: Click
     * on the 'Launch' menu icon at the left screen edge. Select '1 hour' from the time period drop down menu. Select '3G Radio Network' from the '-
     * Select Dimension -' dropdown menu. Select the '3G Ranking' Analysis Window and then 'Call Failure by Terminal' from the sub-menu that appears.
     * Click 'Launch'.
     */

    @Test
    public void rankingsTabVerifyTerminalCallFailureAnalysisRanking_5_8_3() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Terminal,
                WcdmaStingConstantsNewUI.Ranking_3G);

        final String expectedWindowTitle = GuiStringConstants.TERMINAL_WCDMA_CALL_FAILURE_EVENT_RANKING;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = rankingsTerminalWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalCfaRankingColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalCfaRankingColumns));

    }

    /*
     * 4.7.3 13B_WCDMA_CFA_HFA_4.7.3: WCDMA_CFA: From Terminal Call Failure Analysis Ranking, verify Failed Event Analysis (Terminal Summary)
     */
    @Test
    public void rankingsTabFromTerminalEvenAnalysisCallFailureAnalysisInTerminalSummary_5_8_4() throws PopUpException, NoDataException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Terminal,
                WcdmaStingConstantsNewUI.Ranking_3G);

        int rowIndex = rankingsTerminalWcdmaCfa
                .getRowNumberWithMatchingValueForGivenColumn(RANK, reservedDataHelperReplacement.getReservedData(RANK));

        rankingsTerminalWcdmaCfa.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByTerminalSummary);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(MODEL) + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.CALL_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaTac.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalEventAnalysis
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalEventAnalysis));

    }

    /*
     * 4.7.1 13B_WCDMA_CFA_HFA_4.7.1: WCDMA_CFA: From Terminal Call Failure Analysis Ranking, verify Failed Event Analysis (Disconnection Code)
     */
    @Test
    public void rankingsTabDrillFromTerminalEvenAnalysisCallFailureAnalysisToTerminalDisconnections_5_8_5() throws PopUpException, NoDataException,
            InterruptedException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Terminal,
                WcdmaStingConstantsNewUI.Ranking_3G);

        int rowIndex = rankingsTerminalWcdmaCfa
                .getRowNumberWithMatchingValueForGivenColumn(RANK, reservedDataHelperReplacement.getReservedData(RANK));

        rankingsTerminalWcdmaCfa.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        rankingsTerminalWcdmaCfa.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final String type = DISCONNECTION_TYPE + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN
                + reservedDataHelperReplacement.getReservedData(DRILL_ON) + GuiStringConstants.BRACKET_CLOSE;

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(TAC), type, terminalDisconnectionCodeAnalysisFromDrillByTAC,
                defaultCallFailureAnalysisTerminalColumns);

    }

    /*
     * 4.7.2 13B_WCDMA_CFA_HFA_4.7.2: WCDMA_CFA: From Terminal Call Failure Analysis Ranking, verify Failed Event Analysis (RAB Type)
     */

    @Test
    public void rankingsTabDrillFromTerminalEvenAnalysisCallFailureAnalysisToTerminalDisconnections_5_8_6() throws PopUpException, NoDataException,
            InterruptedException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Terminal,
                WcdmaStingConstantsNewUI.Ranking_3G);

        int rowIndex = rankingsTerminalWcdmaCfa
                .getRowNumberWithMatchingValueForGivenColumn(RANK, reservedDataHelperReplacement.getReservedData(RANK));

        rankingsTerminalWcdmaCfa.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByRabType);
        selenium.waitForPageLoadingToComplete();
        rankingsTerminalWcdmaCfa.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));
        selenium.waitForPageLoadingToComplete();

        //workaround for drilling down on second pie chart window
        selenium.click(WcdmaStingConstantsNewUI.ClickRabTypeAnalysisWindow);
        selenium.waitForPageLoadingToComplete();
        rankingsTerminalWcdmaCfa.closeWindow();
        rankingsTerminalWcdmaCfa.closeWindow();
        rankingsTerminalWcdmaCfa.drilldownOnHeaderPortion(SRB_13_6_13_6);

        final String type = RAB_DESCRIPTION + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN + SRB_13_6_13_6
                + GuiStringConstants.BRACKET_CLOSE;

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(TAC), type, terminalRabDescAnalysisFromDrillByTAC,
                defaultCallFailureAnalysisTerminalColumns);

    }

    /*
     * 4.8.1 13B_WCDMA_CFA_HFA_4.8.1: WCDMA_CFA: From WCDMA CFA Subscriber Ranking, verify drilldown to IMSI Event Analysis.
     */

    @Test
    public void rankingsTabSubscriberCallFailureByCallSetupFailureEvenRanking_5_8_7() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_SetupFailure,
                WcdmaStingConstantsNewUI.Ranking_3G);

        subscriberCallSetupFailureRankingWindow.sortTable(SortType.ASCENDING, GuiStringConstants.RANK);

        String expectedWindowTitle = GuiStringConstants.SUBSCRIBER_WCDMA_CALL_FAULURE_BY_CALL_SETUP_FAILURE_EVENT_RANKING;
        //Check Window Header
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        //        subscriberRankingCallDrops

        final List<String> actualWindowHeaders = subscriberCallSetupFailureRankingWindow.getTableHeaders();
        //Check
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + subscriberCallFailureByCallSetupEventRanking
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        //Check Window HeaderColumns
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(subscriberCallFailureByCallSetupEventRanking));

    }

    /*
     * 4.8.1 13B_WCDMA_CFA_HFA_4.8.1: WCDMA_CFA: From WCDMA CFA Subscriber Ranking, verify drilldown to IMSI Event Analysis. Action 2
     */

    @Test
    public void rankingVerifyWcdmaCfaCallDrops3GRanking_5_8_8() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_SetupFailure,
                WcdmaStingConstantsNewUI.Ranking_3G);

        subscriberCallSetupFailureRankingWindow.sortTable(SortType.ASCENDING, GuiStringConstants.RANK);

        int rowIndex = subscriberCallSetupFailureRankingWindow.getRowNumberWithMatchingValueForGivenColumn(RANK,
                reservedDataHelperReplacement.getReservedData(RANK));

        subscriberCallSetupFailureRankingWindow.clickTableCell(rowIndex, IMSI);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(IMSI) + GuiStringConstants.DASH
                + GuiStringConstants.IMSI_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subscriberCallSetupFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsImsiEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(callDropsImsiEventAnalysisColumns));

    }

    /*
     * 4.7.6 13B_WCDMA_CFA_HFA_4.7.6: WCDMA_CFA: Verify Terminal Analysis for Terminal for Most Call Drops view - failure drill down.
     */
    @Test
    public void rankingTabTerminalAnalysisMostCallDrops_5_8_9() throws PopUpException, InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure,
                WcdmaStingConstantsNewUI.Terminal_Analysis_3G);

        String expectedWindowTitle = GuiStringConstants.TERMINAL_ANALYSIS_MOST_CALL_DROPS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalAnalysisCfaMostDrops.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalMostCallDropsColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalMostCallDropsColumns));

    }

    /*
     * 4.7.6 13B_WCDMA_CFA_HFA_4.7.6: WCDMA_CFA: Verify Terminal Analysis for Terminal for Most Call Drops view - failure drill down. Action 4
     */
    @Test
    public void rankingTabTerminalAnalysisMostCallDropsDrillToFailedEventAnalysis_5_8_9a() throws PopUpException, InterruptedException,
            NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure,
                WcdmaStingConstantsNewUI.Terminal_Analysis_3G);

        int rowIndex = terminalAnalysisCfaMostDrops.getRowNumberWithMatchingValueForGivenColumn(RANK,
                reservedDataHelperReplacement.getReservedData(RANK));

        terminalAnalysisCfaMostDrops.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(TAC) + GuiStringConstants.DASH
                + GuiStringConstants.MOST_CALL_DROPS + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalAnalysisCfaMostDrops.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsTacEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(callDropsTacEventAnalysisColumns));

    }

    /*
     * 4.7.4 13B_WCDMA_CFA_HFA_4.7.4: WCDMA_CFA: Verify Group Analysis for Terminal Group for Call Drops only 4.7.5 13B_WCDMA_CFA_HFA_4.7.5:
     * WCDMA_CFA: From Terminal Group Analysis based on Call Drops, verify drilldown to Event Summary.
     */

    @Test
    public void rankingTabCallFailureGroupAnalysisCallDrop_5_8_10() throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure,
                WcdmaStingConstantsNewUI.Terminal_Group_Analysis_3G);
        selenium.waitForPageLoadingToComplete();

        String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.MOST_CALL_DROPS_SUMMARY,
                selenium.isTextPresent(GuiStringConstants.MOST_CALL_DROPS_SUMMARY));

        terminalGroupAnalysisWcdmaCfa.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalGroupAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalGroupAnalysisColumns));

    }

    /*
     * WCDMA_CFA: Verify Group Analysis for Terminal Group for Call Setup Failures
     */
    @Test
    public void rankingTabCallFailureGroupAnalysisCallSetupFailure_5_8_11() throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure,
                WcdmaStingConstantsNewUI.Terminal_Group_Analysis_3G);

        terminalGroupAnalysisWcdmaCfa.toggleToUsingView(GuiStringConstants.MOST_CALL_SETUP_FAILURES);
        selenium.waitForPageLoadingToComplete();
        Thread.sleep(3000);

        String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_GROUP_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.MOST_CALL_SETUP_FAILURES_SUMMARY,
                selenium.isTextPresent(GuiStringConstants.MOST_CALL_SETUP_FAILURES_SUMMARY));

        terminalGroupAnalysisWcdmaCfa.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final List<String> actualWindowHeaders = terminalGroupAnalysisWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalGroupAnalysisCallFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultTerminalGroupAnalysisCallFailureColumns));

    }

    /*
     * 4.6.31 13B_WCDMA_CFA_HFA_4.6.31: WCDMA_CFA: Network Roaming Analysis by Country.
     */
    @Test
    public void rankingsTabWcdmaCallFailureEventValumebyCountryDrillOnCallDrops_5_8_12() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure_WCDMA,
                WcdmaStingConstantsNewUI.ROAMING_BY_COUNTRY);
        selenium.waitForPageLoadingToComplete();

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.INBOUND_ROAMING_BY_COUNTRY_WCDMA_CALL_FALIURE,
                selenium.isTextPresent(GuiStringConstants.INBOUND_ROAMING_BY_COUNTRY_WCDMA_CALL_FALIURE));

        final String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_EVENT_VOLUME_ROAMING_BY_COUNTRY;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        ranWcdmaRoamingByCountry.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));
        selenium.waitForPageLoadingToComplete();

        ranWcdmaRoamingByCountry.closeWindow();
        ranWcdmaRoamingByCountry.drilldownOnHeaderPortion(CALL_DROPS);

        final List<String> actualWindowHeaders = ranWcdmaRoamingByCountry.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns));

    }

    /*
     * 4.6.31 13B_WCDMA_CFA_HFA_4.6.31: WCDMA_CFA: Network Roaming Analysis by Country. Drill down on Call Setup failure
     */
    @Ignore("No data for Call Setup Failures for test case")
    @Test
    public void rankingsTabWcdmaCallFailureEventValumebyCountryDrillOnCallSetupFailure_5_8_12a() throws PopUpException, NoDataException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure_WCDMA,
                WcdmaStingConstantsNewUI.ROAMING_BY_COUNTRY);

        ranWcdmaRoamingByCountry.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        //work around for drill down on second chart
        ranWcdmaRoamingByCountry.closeWindow();

        ranWcdmaRoamingByCountry.drilldownOnHeaderPortion(CALL_SETUP_FAILURES);

        final String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_ROAMING_BY_NETWORK_EVENTS + GuiStringConstants.SPACE
                + GuiStringConstants.BRACKET_OPEN + CALL_SETUP_FAILURES + GuiStringConstants.BRACKET_CLOSE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = ranWcdmaRoamingByCountry.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns));
        if (isDataIntegrityFlagOn()) {
            assertDataIntegrityOnWindow(ranWcdmaRoamingByCountry, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, PROCEDURE_INDICATOR, EVALUATION_CASE,
                    EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR);
        }
    }

    /*
     * 4.6.30 13B_WCDMA_CFA_HFA_4.6.30: WCDMA_CFA: Network Roaming Analysis by Operator
     */

    @Test
    public void rankingsTabWcdmaCallFailureEventValumebyOperatorDrillOnCallDrop_5_8_13() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure_WCDMA,
                WcdmaStingConstantsNewUI.ROAMING_BY_OPERATOR);
        selenium.waitForPageLoadingToComplete();

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.INBOUND_ROAMING_BY_OPERATOR_WCDMA_CALL_FALIURE,
                selenium.isTextPresent(GuiStringConstants.INBOUND_ROAMING_BY_OPERATOR_WCDMA_CALL_FALIURE));

        final String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_EVENT_VOLUME_ROAMING_BY_OPERATOR;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        ranWcdmaRoamingByOperator.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));
        selenium.waitForPageLoadingToComplete();

        ranWcdmaRoamingByOperator.closeWindow();
        ranWcdmaRoamingByOperator.drilldownOnHeaderPortion(CALL_DROPS);

        final List<String> actualWindowHeaders = ranWcdmaRoamingByOperator.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns));

    }

    @Ignore("No data for Call Setup Failures for test case")
    @Test
    public void rankingsTabWcdmaCallFailureEventValumebyOperatorDrillOnCallDrop_5_8_13a() throws PopUpException, NoDataException,
            InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Call_Failure_WCDMA,
                WcdmaStingConstantsNewUI.ROAMING_BY_OPERATOR);

        ranWcdmaRoamingByOperator.drilldownOnBarChartPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));

        //work around for drill dowin on second chart
        ranWcdmaRoamingByOperator.closeWindow();

        ranWcdmaRoamingByOperator.drilldownOnHeaderPortion(CALL_SETUP_FAILURES);

        final String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE_ROAMING_BY_NETWORK_EVENTS + GuiStringConstants.SPACE
                + GuiStringConstants.BRACKET_OPEN + CALL_SETUP_FAILURES + GuiStringConstants.BRACKET_CLOSE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = ranWcdmaRoamingByOperator.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns));
        if (isDataIntegrityFlagOn()) {
            assertDataIntegrityOnWindow(ranWcdmaRoamingByOperator, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, PROCEDURE_INDICATOR, EVALUATION_CASE,
                    EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR);
        }
    }

    @Test
    public void networkTabVerifyWcdmaCfaDisconnectionCodeRankingViews_5_8_14() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.DisconnectionCode_Ranking_3G);

        final String expectedWindowTitle = GuiStringConstants.DISCONNECTION_CODE_RANKING;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = disconnectionCodeCallDropsWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultDisconnectionCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultDisconnectionCodeWcdmaColumns));

    }

    @Test
    public void networkTabVerifyWcdmaCfaDisconnectionCodeRankingViewsDrillToFailedEventAnalysis_5_8_14a() throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_CallDrops,
                WcdmaStingConstantsNewUI.DisconnectionCode_Ranking_3G);

        int rowIndex = disconnectionCodeCallDropsWindow.getRowNumberWithMatchingValueForGivenColumn(RANK,
                reservedDataHelperReplacement.getReservedData(RANK));
        disconnectionCodeCallDropsWindow.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = disconnectionCodeCallDropsWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsDisconnectionEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(callDropsDisconnectionEventAnalysisColumns));
        disconnectionCodeCallDropsWindow.sortTable(SortType.DESCENDING, GuiStringConstants.EVENT_TIME);

    }

    @Test
    public void networkTabVerifyWcdmaCfaDisconnectionCodeRankingViews_5_8_14b() throws PopUpException, NoDataException, InterruptedException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.RADIO_NETWORK_3G, WcdmaStingConstantsNewUI.Ranking_CFA_Terminal,
                WcdmaStingConstantsNewUI.Ranking_3G);
        rankingsTerminalWcdmaCfa.clickTableCell(1, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        selenium.click(WcdmaStingConstantsNewUI.Ranking_Terminal_Toggle);
        selenium.waitForPageLoadingToComplete();

        final List<String> actualWindowHeaders = rankingsTerminalWcdmaCfaToggleToGrid.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultDisconnectionCodeWcdmaColumnsGrid
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultDisconnectionCodeWcdmaColumnsGrid));

        final String Ranking_Terminal_Export = "//*[@id='btnExport']/img";
        selenium.click(Ranking_Terminal_Export);
        selenium.waitForPageLoadingToComplete();
    }

    /**
     * check if Data integrity flag is True
     *
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
    }

    /**
     * @param dataToCheck
     * @param expected
     * @param actual
     */
    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {

        logger.log(Level.INFO, "Checking Data Integrity for " + dataToCheck + " " + GuiStringConstants.DOT + "Expected Value :" + expected
                + " Actual Value : " + actual);

        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck + GuiStringConstants.DOT, expected, actual);
    }

    /**
     * @param result
     * @param dataFields
     */
    protected void checkMultipleDataEntriesOnSameRow(final Map<String, String> result, final String... dataFields) {
        for (int i = 0; i < dataFields.length; i++) {
            checkDataIntegrity(dataFields[i], reservedDataHelperReplacement.getReservedData(dataFields[i]), result.get(dataFields[i]));
        }
    }

    /**
     * @param nodevale
     * @param type
     * @param commonWindow
     * @param windowsHearderList
     */
    private void assertWindowTitleAndHearders(final String nodevale, final String type, final CommonWindow commonWindow,
                                              final List<String> windowsHearderList) {
        final String expectedWindowTitle = nodevale + GuiStringConstants.DASH + GuiStringConstants.WCDMA + GuiStringConstants.SPACE
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + type;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + windowsHearderList + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(windowsHearderList));
    }

    /**
     * @param commonWindow
     * @param eventList
     * @throws NoDataException
     */
    private void assertDataIntegrityOnWindow(final CommonWindow commonWindow, final String... eventList) throws NoDataException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI, reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, eventList);
    }
}
