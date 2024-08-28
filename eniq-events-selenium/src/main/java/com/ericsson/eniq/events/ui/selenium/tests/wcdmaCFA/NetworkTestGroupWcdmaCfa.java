package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class NetworkTestGroupWcdmaCfa extends BaseWcdmaCfa {
    private String dataIntegrityFlag;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("wcdmaCFARANRanking")
    private CommonWindow wcdmaCallFailureRANRankings;

    @Autowired
    @Qualifier("accessAreaCFARanking")
    private CommonWindow accessAreaCFARankingWindow;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallDrops")
    private CommonWindow causeCodeCallDropsWindow;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallSetupFailures")
    private CommonWindow causeCodeCallFailureWindow;

    @Autowired
    @Qualifier("detailedEventAnalysisWcdmaCfaCallDrop")
    private CommonWindow detailedEventAnalysisCallDropWindow;

    @Autowired
    @Qualifier("detailedEventAnalysisWcdmaCfaCallSetup")
    private CommonWindow detailedEventAnalysisCallSetupWindow;

    @Autowired
    @Qualifier("networkEventAnalysisWcdmaCfa")
    private CommonWindow networkEventAnalysisWindow;

    @Autowired
    @Qualifier("subCauseCodeAnalysisCallDropsWcdmaCfa")
    private CommonWindow subCauseCodeAnalysisCallDrops;

    @Autowired
    @Qualifier("subCauseCodeAnalysisCallSetupWcdmaCfa")
    private CommonWindow subCauseCodeAnalysisCallSetup;

    // Launch Button Sub Menus

    private final static List<NetworkTab.SubStartMenu> subMenuRncRanCfa = Arrays.asList(
            NetworkTab.SubStartMenu.EVENT_RANKING, NetworkTab.SubStartMenu.NETWORK_RANKING_RNC_MENU_ITEM_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_RANKING_RNC_RAN_MENU_ITEM_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_RAN_RNC_WCDMA_CFA);

    private final static List<NetworkTab.SubStartMenu> subMenuAccessAreaWcdmaCfa = Arrays.asList(
            NetworkTab.SubStartMenu.EVENT_RANKING, NetworkTab.SubStartMenu.NETWORK_RANKING_ACCESS_AREA_MENU_ITEM_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_RANKING_AA_RAN_MENU_ITEM_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_RANKING_AA_WCDMA_MENU_ITEM_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_ACCESS_AREA_WCDMA_CFA);

    private final static List<NetworkTab.SubStartMenu> subMenuCauseCodeWcdma = new ArrayList<NetworkTab.SubStartMenu>(
            Arrays.asList(NetworkTab.SubStartMenu.EVENT_RANKING,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_RAN_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_WCDMA_MENU_ITEM_WCDMA));

    private final static List<NetworkTab.SubStartMenu> subMenuEventAnalysisWcdmaCfa = Arrays.asList(
            NetworkTab.SubStartMenu.EVENT_ANALYSIS_RAN, NetworkTab.SubStartMenu.NETWORK_EVENT_ANALYSIS_WCDMA,
            NetworkTab.SubStartMenu.NETWORK_EVENT_ANALYSIS_WCDMA_CFA);
    
    private final static List<String> defaultCallSetupFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA,
            EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, LAC,
            RAC, GuiStringConstants.SEVERITY_INDICATOR_CFA, GuiStringConstants.RAB_TYPE, 
            GuiStringConstants.TRIGGER_POINT, RRC_ESTABLISHMENT_CAUSE));
    
    private final static List<String> defaultCallDropFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA,
            EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.LAC, GuiStringConstants.RAC, GuiStringConstants.SEVERITY_INDICATOR_CFA,
            GuiStringConstants.RAB_TYPE, GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB,
            GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE,
            GuiStringConstants.DISCONNECTION_DESC, GuiStringConstants.TRIGGER_POINT));

    private final static List<String> defaultCallSetupFailureAccessAreaColumns = Arrays.asList(RAN_VENDOR, CONTROLLER,
            ACCESS_AREA, EVENT_TYPE, FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    private final static List<String> defaultAccessAreaTerminalEventAnalysis = Arrays.asList(
            GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, EVENT_TYPE, FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS);

    private final static List<String> defaultControllerGroupAnalysisColumns = Arrays.asList(EVENT_TYPE, FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS);

    // View Titles
    private final static String expectedViewTitleFailuresDrillDown = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH
            + CALL_FAILURE_ANALYSIS + DASH + GuiStringConstants.WCDMA;

    static ReservedDataHelperReplacement reservedDataHelperReplacement = new ReservedDataHelperReplacement(
            "WcdmaCfaReserveDataS.xls");

    @Override
    @Before
    public void setUp() {
        super.setUp();

        networkTab.openTab();

        // networkTab.setPacketSwitchedMenu(PacketSwitchedMenuOptions.CORE_DATA);

        dataIntegrityFlag = PropertyReader.getInstance().getDataIntegrityFlag();
    }

    @Test
    public void networkTabVerifyWcdmaCfaRncRankingViews_5_5_1() throws PopUpException, NoDataException {

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuRncRanCfa);

        // emosjil
        // SPACE in between WCDMA_CALL_FAILURE and EVENT_RANKING instead of COLON
        // final String expectedWindowTitle = RNC + SPACE + WCDMA_CALL_FAILURE + SPACE + EVENT_RANKING;
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	expectedWindowTitle = RNC + SPACE + WCDMA_CALL_FAILURE + COLON + EVENT_RANKING;
        }else{
        	expectedWindowTitle = RNC + SPACE + WCDMA_CALL_FAILURE + SPACE + EVENT_RANKING;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallFailureAnalysisColumns));

        if (dataIntegrityFlag.equals("true")) {
            wcdmaCallFailureRANRankings.setTimeRangeBasedOnSeleniumPropertiesFile();

            int rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(RANK, "1");
            Map<String, String> result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RNC, RAN_VENDOR, FAILURES);

            rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(RANK, "2");
            result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);

            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, FAILURES);
            checkDataIntegrity(RNC, reservedDataHelperReplacement.getReservedData("RNC 2"), result.get(RNC));
        }
    }

    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToControllerEventAnalysisView_5_5_2()
            throws PopUpException, NoDataException {

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuRncRanCfa);

        wcdmaCallFailureRANRankings.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(RNC,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));
        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, 2, GRID_CELL_LINK);
        
        final String expectedWindowTitle = GuiStringConstants.CONTROLLER_EVENT_ANALYSIS;
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(FAILURES, "480");
            Map<String, String> result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, EVENT_TYPE, RAN_VENDOR, CONTROLLER, IMPACTED_SUBSCRIBERS);

            rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(FAILURES, "300");
            result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);

            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, IMPACTED_SUBSCRIBERS, TERMINAL_MODEL,
                    PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
                    SEVERITY_INDICATOR);
            checkDataIntegrity(EVENT_TYPE, reservedDataHelperReplacement.getReservedData("Event Type 2"),
                    result.get(EVENT_TYPE));
        }
    }

    @Test
    public void networkTabFromWcdmaCfaRncRankingVerifyDrilldownToFailedEventAnalysis_5_5_3() throws PopUpException,
            NoDataException {
        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuRncRanCfa);

        wcdmaCallFailureRANRankings.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(RNC,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));
        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, 2, GRID_CELL_LINK);

        rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        wcdmaCallFailureRANRankings.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.FAILED_EVENT_ANALYSIS,
                selenium.isTextPresent(GuiStringConstants.FAILED_EVENT_ANALYSIS));

        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = wcdmaCallFailureRANRankings.findFirstTableRowWhereMatchingAnyValue(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            final Map<String, String> result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);

            checkMultipleDataEntriesOnSameRow(result, TAC, TERMINAL_MAKE, TERMINAL_MODEL, PROCEDURE_INDICATOR,
                    EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR);
        }
    }

    @Test
    public void networkTabVerifyWcdmaCfaAccessAreaCellRankingViews_5_5_4() throws PopUpException, NoDataException {

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuAccessAreaWcdmaCfa);

        final String expectedWindowTitle = ACCESS_AREA + SPACE + WCDMA_CALL_FAILURE + COLON + EVENT_RANKING;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaCFAColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultAccessAreaCFAColumns));

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(RANK,
                    reservedDataHelperReplacement.getReservedData(RANK));
            final Map<String, String> result = wcdmaCallFailureRANRankings.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, CONTROLLER, FAILURES, ACCESS_AREA);
        }
    }

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingsVerifyDrilldownToAccessAreaEventAnalysisView_5_5_5()
            throws NoDataException, PopUpException {
        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuAccessAreaWcdmaCfa);

        accessAreaCFARankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String accessArea = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(ACCESS_AREA, accessArea);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        final String expectedWindowTitle = ACCESS_AREA + DASH + EVENT_ANALYSIS_SUMMARY;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultAccessAreaEventAnalysisColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(FAILURES,
                    reservedDataHelperReplacement.getReservedData(FAILURES));
            Map<String, String> result = accessAreaCFARankingWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, RNC, EVENT_TYPE, FAILURES, IMPACTED_SUBSCRIBERS);

            rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(FAILURES,
                    reservedDataHelperReplacement.getReservedData("Event Type 2"));
            result = accessAreaCFARankingWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, RNC, IMPACTED_SUBSCRIBERS);
            checkDataIntegrity(EVENT_TYPE, reservedDataHelperReplacement.getReservedData("Event Type 2"),
                    result.get(EVENT_TYPE));
            checkDataIntegrity(FAILURES, reservedDataHelperReplacement.getReservedData("Failures 2"),
                    result.get(FAILURES));
        }
    }

    @Test
    public void networkTabFromWcdmaCfaAccessAreaRankingVerifyDrilldownToFailureAnalysisView_5_5_6()
            throws NoDataException, PopUpException {

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuAccessAreaWcdmaCfa);

        accessAreaCFARankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        int rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(ACCESS_AREA, accessAreaValue);
        accessAreaCFARankingWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        accessAreaCFARankingWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = accessAreaCFARankingWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            final Map<String, String> result = accessAreaCFARankingWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, EVENT_TYPE, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
                    PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
                    SEVERITY_INDICATOR);
        }
    }

    @Test
    public void networkTabVerifyWcdmaCfaCauseCodeRankingViews_5_5_7a() throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(NetworkTab.SubStartMenu.NETWORK_CC_RANKING_CALL_DROPS);

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuCauseCodeWcdma);

        causeCodeCallDropsWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Drops : Event Ranking";
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeCallDropsWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = causeCodeCallDropsWindow.findFirstTableRowWhereMatchingAnyValue(RANK,
                    reservedDataHelperReplacement.getReservedData(RANK));
            final Map<String, String> result = causeCodeCallDropsWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, CAUSE_CODE, CAUSE_CODE_ID, FAILURES);
        }
    }

    @Test
    public void networkTabVerifyWcdmaCfaCauseCodeRankingViews_5_5_7b() throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(NetworkTab.SubStartMenu.NETWORK_CC_RANKING_CALL_SETUP_FAILURES);

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuCauseCodeWcdma);

        causeCodeCallFailureWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Setup Failure : Event Ranking";
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeCallFailureWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = causeCodeCallFailureWindow.findFirstTableRowWhereMatchingAnyValue(RANK,
                    reservedDataHelperReplacement.getReservedData(RANK));
            final Map<String, String> result = causeCodeCallFailureWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, CAUSE_CODE, CAUSE_CODE_ID, FAILURES);
        }
    }

    @Test
    public void networkTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheNetworkCauseCodeAnalysisViews_5_5_8a()
            throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(NetworkTab.SubStartMenu.NETWORK_CC_RANKING_CALL_DROPS);

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuCauseCodeWcdma);

        causeCodeCallDropsWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = causeCodeCallDropsWindow.findFirstTableRowWhereMatchingAnyValue(CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));

        final String causeCode = causeCodeCallDropsWindow.getTableData(rowIndex, 1);
        final String causeCodeId = causeCodeCallDropsWindow.getTableData(rowIndex, 2);
        final String expectedWindowTitle = causeCode + GuiStringConstants.COMMA_SPACE + causeCodeId + DASH
                + "Sub Cause Code Analysis for WCDMA CFA Call Drops";

        causeCodeCallDropsWindow.clickTableCell(rowIndex, GuiStringConstants.CAUSE_CODE);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subCauseCodeAnalysisCallDrops.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = detailedEventAnalysisCallDropWindow.findFirstTableRowWhereMatchingAnyValue(SUB_CAUSE_CODE,
                    reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE));
            final Map<String, String> result = detailedEventAnalysisCallDropWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, SUB_CAUSE_CODE_ID, SUB_CAUSE_CODE, FAILURES, IMPACTED_SUBSCRIBERS);
        }
    }

    @Test
    public void networkTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheNetworkCauseCodeAnalysisViews_5_5_8b()
            throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(NetworkTab.SubStartMenu.NETWORK_CC_RANKING_CALL_SETUP_FAILURES);

        networkTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, subMenuCauseCodeWcdma);

        causeCodeCallFailureWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String causeCodeDescription = causeCodeCallFailureWindow.getTableData(0, 1);
        final String causeCodeId = causeCodeCallFailureWindow.getTableData(0, 2);
        final String expectedWindowTitle = causeCodeDescription + GuiStringConstants.COMMA_SPACE + causeCodeId + DASH
                + "Sub Cause Code Analysis for WCDMA CFA Call Setup";

        causeCodeCallFailureWindow.clickTableCell(0, CAUSE_CODE);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subCauseCodeAnalysisCallSetup.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_9() throws PopUpException, InterruptedException,
            NoDataException {

        final String searchFieldInput = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER, subMenuEventAnalysisWcdmaCfa,
                searchFieldInput);
        
        // emosjil: modified expectedWindowTitle
        // final String expectedWindowTitle = searchFieldInput + DASH + GuiStringConstants.CONTROLLER_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        
        final String expectedWindowTitle;
        double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	expectedWindowTitle = searchFieldInput + DASH + GuiStringConstants.CONTROLLER + DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = searchFieldInput + DASH + GuiStringConstants.CONTROLLER + DASH + GuiStringConstants.EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        }

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, CONTROLLER, FAILURES, EVENT_TYPE,
                    IMPACTED_SUBSCRIBERS);
        }
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByEventTypeToFailedEventAnalysis_5_5_10()
            throws InterruptedException, PopUpException, NoDataException {

        final String searchFieldInput = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER, subMenuEventAnalysisWcdmaCfa,
                searchFieldInput);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);
        
        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER) + DASH
                + GuiStringConstants.CALL_DROPS + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH
                + GuiStringConstants.WCDMA_CALL_FAILURE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));

        // **************************************************************************************

        // Get full list of non-default columns
        // De-select non-default columns
        // Check that non-default columns have been de-selected
        // Create method for all of this
        final List<String> nonDefaultEventSummaryColumns = Arrays.asList(
                GuiStringConstants.RAB_TYPE_ATTEMPTED_RECONFIGURE, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
                GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL,
                GuiStringConstants.UTRAN_RANAP_CAUSE, GuiStringConstants.DATA_IN_DL_RLC_BUFFERS);
        networkEventAnalysisWindow.checkInOptionalHeaderCheckBoxes(nonDefaultEventSummaryColumns,
                GuiStringConstants.EVENT_SUMMARY_DETAILS);
        final List<String> defaultAndNonDefaultColumns = new ArrayList<String>();
        defaultAndNonDefaultColumns.addAll(defaultCallDropFailedEventAnalysisColumns);
        defaultAndNonDefaultColumns.addAll(nonDefaultEventSummaryColumns);

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAndNonDefaultColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.containsAll(defaultAndNonDefaultColumns));

        networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(nonDefaultEventSummaryColumns,
                GuiStringConstants.EVENT_SUMMARY_DETAILS);

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));
        
        // **************************************************************************************

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, CONTROLLER, EVENT_TYPE, TERMINAL_MAKE, TERMINAL_MODEL,
                    PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
                    SEVERITY_INDICATOR, ACCESS_AREA, LAC, RAC);
        }

        // RSCP_CELL_2 (dBm)
        // RSCP_CELL_3 (dBm)
        // RSCP_CELL_4 (dBm)
        // CPICH_EC_NO_CELL_2 (dB)
        // CPICH_EC_NO_CELL_3 (dB)
        // CPICH_EC_NO_CELL_4 (dB)
        // UL_INT_CELL2 (dBm)
        // UL_INT_CELL3 (dBm)
        // UL_INT_CELL4 (dBm)
        // SCRAMBLING_CODE_CELL_1
        // SCRAMBLING_CODE_CELL_2
        // SCRAMBLING_CODE_CELL_3
        // SCRAMBLING_CODE_CELL_4
        // GuiStringConstants.DISCONNECTION_CODE
        // GuiStringConstants.DISCONNECTION_SUBCODE
        // GuiStringConstants.DISCONNECTION_DESC
        // GuiStringConstants.TRIGGER_POINT
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByTacFromFailedEventAnalysis_5_5_11a()
            throws InterruptedException, PopUpException, NoDataException {

        final String searchFieldInput = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER, subMenuEventAnalysisWcdmaCfa,
                searchFieldInput);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        drillDownOnTacAndFailedEventAnalysis(defaultAccessAreaTerminalEventAnalysis, TAC);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis_5_5_12a()
            throws InterruptedException, PopUpException, NoDataException {

        // Based on Reserve Data excel sheet, this method call will drill down
        // on Call Setup Failures
        networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(defaultCallSetupFailedEventAnalysisColumns);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis_5_5_12b()
            throws InterruptedException, PopUpException, NoDataException {

        // Based on Reserve Data excel sheet, this method call will drill down
        // on Call Drops
        networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(defaultCallDropFailedEventAnalysisColumns);
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_13() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA, subMenuEventAnalysisWcdmaCfa,
                accessAreaValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        // emosjil
        // final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(DRILL_ON) + DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS;
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();        
        if(version >= 13.0){
        	expectedWindowTitle = reservedDataHelperReplacement.getReservedData(DRILL_ON) + DASH + GuiStringConstants.ACCESS_AREA + DASH + GuiStringConstants.WCDMA_CALL_FAILURE + SPACE + GuiStringConstants.EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = reservedDataHelperReplacement.getReservedData(DRILL_ON) + DASH + GuiStringConstants.ACCESS_AREA +GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailureAccessAreaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailureAccessAreaColumns));

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, CONTROLLER, FAILURES, ACCESS_AREA);
        }
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_14()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA, subMenuEventAnalysisWcdmaCfa,
                accessAreaValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, 4, GRID_CELL_LINK);

        // accessAreaValue + DASH +
        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, TAC, EVENT_TYPE, TERMINAL_MODEL, PROCEDURE_INDICATOR,
                    EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR,
                    ACCESS_AREA);
        }
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByTacFromFailedEventAnalysis_5_5_15a()
            throws NoDataException, PopUpException, InterruptedException {

        final String liveLoadValue = reservedDataHelperReplacement.getReservedData(LIVE_LOAD);
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA, subMenuEventAnalysisWcdmaCfa,
                liveLoadValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        // Based on Reserve Data excel sheet, this method call will drill down
        // on Call Drops
        drillDownOnTacAndFailedEventAnalysis(defaultAccessAreaTerminalEventAnalysis, TAC);
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByTacFromFailedEventAnalysis_5_5_15b()
            throws NoDataException, PopUpException, InterruptedException {

        final String liveLoadValue = reservedDataHelperReplacement.getReservedData(LIVE_LOAD);
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA, subMenuEventAnalysisWcdmaCfa,
                liveLoadValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        // Based on Reserve Data excel sheet, this method call will drill down
        // on Call Setup Failures
        drillDownOnTacAndFailedEventAnalysis(defaultAccessAreaTerminalEventAnalysis, TAC);
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByControllerFromFailedEventAnalysis_5_5_16()
            throws InterruptedException, PopUpException, NoDataException {

        final String liveLoadValue = reservedDataHelperReplacement.getReservedData(LIVE_LOAD);

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA, subMenuEventAnalysisWcdmaCfa,
                liveLoadValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        if (dataIntegrityFlag.equals("true")) {
            final int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
            networkEventAnalysisWindow.clickTableCell(rowIndex, 4, GRID_CELL_LINK);
        } else {
            networkEventAnalysisWindow.clickTableCell(0, 4, GRID_CELL_LINK);
        }

        networkEventAnalysisWindow.clickTableCell(0, 5, GRID_CELL_LINK);
        String expectedWindowTitle = DASH + GuiStringConstants.CONTROLLER_EVENT_ANALYSIS + DASH + WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        networkEventAnalysisWindow.clickTableCell(0, 3, GRID_CELL_LINK);
        expectedWindowTitle = DASH + GuiStringConstants.CALL_DROPS + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + DASH + WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroup_5_5_17() throws InterruptedException, PopUpException,
            NoDataException {
        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(LIVE_LOAD);
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                controllerGroupValue);

        // emosjil: modified expectedWindowTitle
        //final String expectedWindowTitle = controllerGroupValue + DASH
        //        + GuiStringConstants.CONTROLLER_GROUP_EVENT_ANALYSIS;
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = controllerGroupValue + DASH + GuiStringConstants.CONTROLLER_GROUP + DASH + GuiStringConstants.WCDMA_CALL_FAILURE + SPACE + GuiStringConstants.EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = controllerGroupValue + DASH + GuiStringConstants.CONTROLLER_GROUP + DASH + GuiStringConstants.EVENT_ANALYSIS;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerGroupAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerGroupAnalysisColumns));

        if (dataIntegrityFlag.equals("true")) {
            int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
            Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, FAILURES, IMPACTED_SUBSCRIBERS);

            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue("Event Type 2",
                    reservedDataHelperReplacement.getReservedData("Event Type 2"));
            result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, TAC, IMPACTED_SUBSCRIBERS);
            checkDataIntegrity(FAILURES, reservedDataHelperReplacement.getReservedData("Failures 2"),
                    result.get(FAILURES));
        }
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByEventTypeToFailedEventAnalysisCallDrops_5_5_18()
            throws InterruptedException, PopUpException, NoDataException {

        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(LIVE_LOAD);
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                controllerGroupValue);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        networkEventAnalysisWindow.clickTableCell(0, 1, GRID_CELL_LINK);

        final String expectedWindowTitle = controllerGroupValue + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("RRC_ESTABLISHMENT_CAUSE");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck,"Active Set Measurements");
        }else{
        	//nothing
        }

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                    reservedDataHelperReplacement.getReservedData(IMSI));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, TAC, TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA,
                    EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
                    EXTENDED_CAUSE_VALUE, LAC, RAC, SEVERITY_INDICATOR);
        }
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByEventTypeToFailedEventAnalysisSetupFailures_5_5_18()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(1, 1, GRID_CELL_LINK);

        final String expectedWindowTitle = CONTROLLER_GROUP_VALUE + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("DISCONNECTION_CODE");
        	headersToUncheck.add("DISCONNECTION_SUBCODE");
        	headersToUncheck.add("DISCONNECTION_DESC");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck,"Active Columns");
        }else{
        	
        }

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByTacFromFailedEventAnalysisCallDrops_5_5_19a()
            throws InterruptedException, PopUpException, NoDataException {

        drillOnTacToNetworkEventAnalysis();
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByTacFromFailedEventAnalysisSetupFailures_5_5_19a()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(1, 1, GRID_CELL_LINK);

        // drillDownOnTacAndFailedEventAnalysis(defaultAccessAreaTerminalEventAnalysis,
        // TAC);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByControllerFromFailedEventAnalysisCallDrops_5_5_20a()
            throws NoDataException, PopUpException, InterruptedException {

        // Based on reserve data excel sheet, the drill down will be on Call
        // Drops
        drillDownOnController(defaultCallDropFailedEventAnalysisColumns, FAILURES);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByControllerFromFailedEventAnalysisSetupFailures_5_5_20b()
            throws NoDataException, PopUpException, InterruptedException {

        // Based on reserve data excel sheet, the drill down will be on Call
        // Setup Failures
        drillDownOnController(defaultCallSetupFailedEventAnalysisColumns, FAILURES);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByAccessAreaFromFailedEventAnalysis_5_5_21a()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(0, 1, GRID_CELL_LINK);

        final String accessArea = networkEventAnalysisWindow.getTableData(0, 6);
        networkEventAnalysisWindow.clickTableCell(0, 6, GRID_CELL_LINK);

        String expectedWindowTitle = accessArea + DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailureAccessAreaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailureAccessAreaColumns));

        networkEventAnalysisWindow.clickTableCell(1, 4, GRID_CELL_LINK);

        expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("RRC_ESTABLISHMENT_CAUSE");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Columns");
        }else{
        	
        }

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByAccessAreaFromFailedEventAnalysis_5_5_21b()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(1, 1, GRID_CELL_LINK);

        final String accessArea = networkEventAnalysisWindow.getTableData(0, 6);
        networkEventAnalysisWindow.clickTableCell(0, 6, GRID_CELL_LINK);

        String expectedWindowTitle = accessArea + DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailureAccessAreaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailureAccessAreaColumns));

        networkEventAnalysisWindow.clickTableCell(0, 4, GRID_CELL_LINK);

        expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("DISCONNECTION_CODE");
        	headersToUncheck.add("DISCONNECTION_SUBCODE");
        	headersToUncheck.add("DISCONNECTION_DESC");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Columns");
        }

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroup_5_5_22() throws InterruptedException, PopUpException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA_GROUP,
                subMenuEventAnalysisWcdmaCfa, ACCESS_AREA_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        //emosjil: added WCDMA_CALL_FAILURE + SPACE constants to the string
        //final String expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP
        //        + GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;        
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerGroupAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerGroupAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByEventTypeToFailedEventAnalysis_5_5_23()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA_GROUP,
                subMenuEventAnalysisWcdmaCfa, ACCESS_AREA_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(0, 1, GRID_CELL_LINK);

        String expectedWindowTitle = GuiStringConstants.CALL_DROPS + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("RRC_ESTABLISHMENT_CAUSE");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Set Measurements");
        }

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallDropFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallDropFailedEventAnalysisColumns));

        networkEventAnalysisWindow.clickBackwardNavigation();

        //emosjil
        //expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP + GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
        
        if(version >= 13.0){
        	expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP
        			+ GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE
        			+ GuiStringConstants.EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = ACCESS_AREA_GROUP_VALUE + DASH + GuiStringConstants.ACCESS_AREA_GROUP 
        			+ GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        networkEventAnalysisWindow.clickTableCell(1, 1, GRID_CELL_LINK);

        expectedWindowTitle = GuiStringConstants.CALL_SETUP_FAILURES + DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        if(version >= 13.0){
        	List<String> headersToUncheck = new ArrayList<String>();
        	headersToUncheck.add("DISCONNECTION_CODE");
        	headersToUncheck.add("DISCONNECTION_SUBCODE");
        	headersToUncheck.add("DISCONNECTION_DESC");
        	networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Columns");
        }

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByTacFromFailedEventAnalysis_5_5_24()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA_GROUP,
                subMenuEventAnalysisWcdmaCfa, ACCESS_AREA_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        drillDownOnTacAndFailedEventAnalysis(defaultAccessAreaTerminalEventAnalysis, TAC);

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedViewTitleFailuresDrillDown,
                selenium.isTextPresent(expectedViewTitleFailuresDrillDown));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsFailedEventAnalysisColumns));

        networkEventAnalysisWindow.clickBackwardNavigation();

        networkEventAnalysisWindow.clickTableCell(getCallSetupFailuresRowNumber(), 3, GRID_CELL_LINK);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedViewTitleFailuresDrillDown,
                selenium.isTextPresent(expectedViewTitleFailuresDrillDown));

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callSetupFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByControllerFromFailedEventAnalysis_5_5_25a()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA_GROUP,
                subMenuEventAnalysisWcdmaCfa, ACCESS_AREA_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(getCallDropsRowNumber(), 1, GRID_CELL_LINK);

        networkEventAnalysisWindow.clickTableCell(0, TAC);

        networkEventAnalysisWindow.clickTableCell(getCallDropsRowNumber(), 3, GRID_CELL_LINK);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsFailedEventAnalysisColumns));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByControllerFromFailedEventAnalysis_5_5_25b()
            throws InterruptedException, PopUpException, NoDataException {

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.ACCESS_AREA_GROUP,
                subMenuEventAnalysisWcdmaCfa, ACCESS_AREA_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(getCallSetupFailuresRowNumber(), 1, GRID_CELL_LINK);

        networkEventAnalysisWindow.clickTableCell(0, TAC);

        networkEventAnalysisWindow.clickTableCell(getCallSetupFailuresRowNumber(), 3, GRID_CELL_LINK);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callSetupFailedEventAnalysisColumns));
    }

    /**
     * 
     * PRIVATE METHODS
     * 
     */

    private int getCallDropsRowNumber() throws NoDataException {

        final ArrayList<String> eventTypes = (ArrayList<String>) networkEventAnalysisWindow
                .getAllTableDataAtColumn(EVENT_TYPE);
        int callDropRowNumber = 0;
        for (int i = 0; i < eventTypes.size(); i++) {
            if (eventTypes.get(i).equals(GuiStringConstants.CALL_DROPS)) {
                callDropRowNumber = i;
                break;
            }
        }
        return callDropRowNumber;
    }

    private int getCallSetupFailuresRowNumber() throws NoDataException {

        final ArrayList<String> eventTypes = (ArrayList<String>) networkEventAnalysisWindow
                .getAllTableDataAtColumn(EVENT_TYPE);
        int callSetupFailuresRowNumber = 0;
        for (int i = 0; i < eventTypes.size(); i++) {
            if (eventTypes.get(i).equals(GuiStringConstants.CALL_SETUP_FAILURES)) {
                callSetupFailuresRowNumber = i;
                break;
            }
        }
        return callSetupFailuresRowNumber;
    }

    private void networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(
            final List<String> defaultFailedEventAnalysisColumns) throws InterruptedException, PopUpException,
            NoDataException {

        final String searchFieldInput = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER, subMenuEventAnalysisWcdmaCfa,
                searchFieldInput);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER) + DASH
                + reservedDataHelperReplacement.getReservedData(EVENT_TYPE) + DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultFailedEventAnalysisColumns));

        rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(ACCESS_AREA,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA));
        final String accessArea = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);
        networkEventAnalysisWindow.clickTableCell(rowIndex, 6, GRID_CELL_LINK);

        expectedWindowTitle = accessArea + DASH + reservedDataHelperReplacement.getReservedData(CONTROLLER) + DASH
                + VENDOR_VALUE_DASH + DASH + ACCESS_AREA + " " + GuiStringConstants.EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailureAccessAreaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallSetupFailureAccessAreaColumns));

        if (dataIntegrityFlag.equals("true")) {
            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, CONTROLLER, ACCESS_AREA, FAILURES,
                    IMPACTED_SUBSCRIBERS);
        }
    }

    private void drillDownOnTacAndFailedEventAnalysis(final List<String> expectedColumnHeaders, final String column)
            throws NoDataException, PopUpException, InterruptedException {
        int rowIndex = 0;

        rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(column,
        		reservedDataHelperReplacement.getReservedData(column));
        
        networkEventAnalysisWindow.clickTableCell(rowIndex, 2, GRID_CELL_LINK);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(MANUFACTURER) + DASH
                + reservedDataHelperReplacement.getReservedData(MODEL) + DASH
                + reservedDataHelperReplacement.getReservedData(TAC) + DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + DASH + GuiStringConstants.CALL_FAILURE_ANALYSIS + DASH
                + GuiStringConstants.WCDMA;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedColumnHeaders
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, expectedColumnHeaders.equals(actualWindowHeaders));

        if (dataIntegrityFlag.equals("true")) {
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, MANUFACTURER, MODEL, EVENT_TYPE, FAILURES, IMPACTED_SUBSCRIBERS);

            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                    reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

            networkEventAnalysisWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);
        } else {
            networkEventAnalysisWindow.clickTableCell(0, 3, GRID_CELL_LINK);
        }

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedViewTitleFailuresDrillDown,
                selenium.isTextPresent(expectedViewTitleFailuresDrillDown));

        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsFailedEventAnalysisColumns));
    }

    private void drillOnTacToNetworkEventAnalysis() throws InterruptedException, PopUpException, NoDataException {
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        int rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, 1, GRID_CELL_LINK);
        networkEventAnalysisWindow.clickTableCell(rowIndex, 2, GRID_CELL_LINK);

        rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        if (dataIntegrityFlag.equals("true")) {
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, EVENT_TYPE, FAILURES, IMPACTED_SUBSCRIBERS);
        }

        networkEventAnalysisWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedViewTitleFailuresDrillDown,
                selenium.isTextPresent(expectedViewTitleFailuresDrillDown));

        final List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsFailedEventAnalysisColumns));
    }

    private void drillDownOnController(final List<String> failedEventAnalysisColumns, final String column)
            throws NoDataException, PopUpException, InterruptedException {
        networkTab.openEventAnalysisWindowUsingSubStartMenu(NetworkType.CONTROLLER_GROUP, subMenuEventAnalysisWcdmaCfa,
                CONTROLLER_GROUP_VALUE);

        networkEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        networkEventAnalysisWindow.clickTableCell(0, column);

        int rowIndex = 0;
        String controller = networkEventAnalysisWindow.getTableData(rowIndex, 5);
        if (dataIntegrityFlag.equals("true")) {
            rowIndex = networkEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(CONTROLLER,
                    reservedDataHelperReplacement.getReservedData(DRILL_ON));
            controller = reservedDataHelperReplacement.getReservedData(CONTROLLER);
        }

        networkEventAnalysisWindow.clickTableCell(rowIndex, 5, GRID_CELL_LINK);

        String expectedWindowTitle = controller + DASH + GuiStringConstants.CONTROLLER_EVENT_ANALYSIS + DASH
                + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));

        
        if (dataIntegrityFlag.equals("true")) {
            final Map<String, String> result = networkEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
            checkMultipleDataEntriesOnSameRow(result, RAN_VENDOR, CONTROLLER, EVENT_TYPE, FAILURES,
                    IMPACTED_SUBSCRIBERS);
        }

        networkEventAnalysisWindow.clickTableCell(rowIndex, 3, GRID_CELL_LINK);

        expectedWindowTitle = controller + DASH + GuiStringConstants.CALL_DROPS + DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	if(failedEventAnalysisColumns.containsAll(defaultCallDropFailedEventAnalysisColumns)){
        		List<String> headersToUncheck = new ArrayList<String>();
        		headersToUncheck.add("RRC_ESTABLISHMENT_CAUSE");
        		networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Set Measurements");
        		
        		List<String> headersToCheck = new ArrayList<String>();
        		headersToCheck.add("CPICH_EC_NO_CELL_1 (dB)");
        		headersToCheck.add("UL_INT_CELL1 (dBm)");
        		headersToCheck.add("RSCP_CELL_1 (dBm)");
        		headersToCheck.add("DISCONNECTION_CODE");
        		headersToCheck.add("DISCONNECTION_SUBCODE");
        		headersToCheck.add("DISCONNECTION_DESC");
        		networkEventAnalysisWindow.checkInOptionalHeaderCheckBoxes(headersToCheck, "Active Set Measurements");
        	}else{
        		List<String> headersToUncheck = new ArrayList<String>();
        		headersToUncheck.add("CPICH_EC_NO_CELL_1 (dB)");
        		headersToUncheck.add("UL_INT_CELL1 (dBm)");
        		headersToUncheck.add("RSCP_CELL_1 (dBm)");
        		headersToUncheck.add("DISCONNECTION_CODE");
        		headersToUncheck.add("DISCONNECTION_SUBCODE");
        		headersToUncheck.add("DISCONNECTION_DESC");
        		networkEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(headersToUncheck, "Active Set Measurements");
        		
        		List<String> headersToCheck = new ArrayList<String>();
        		headersToCheck.add("RRC_ESTABLISHMENT_CAUSE");
        		networkEventAnalysisWindow.checkInOptionalHeaderCheckBoxes(headersToCheck, "Active Set Measurements");
        	}
        }
        
        actualWindowHeaders = networkEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + failedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(failedEventAnalysisColumns));
    }

    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {

        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck
                + GuiStringConstants.DOT, expected, actual);
    }

    protected void checkMultipleDataEntriesOnSameRow(final Map<String, String> result, final String... dataFields) {
        for (int i = 0; i < dataFields.length; i++) {
            checkDataIntegrity(dataFields[i], reservedDataHelperReplacement.getReservedData(dataFields[i]),
                    result.get(dataFields[i]));
        }
    }

}
