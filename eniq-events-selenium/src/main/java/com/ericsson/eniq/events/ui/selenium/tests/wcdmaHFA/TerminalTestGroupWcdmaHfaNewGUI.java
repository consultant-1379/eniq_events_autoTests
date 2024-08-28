package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.CSVReaderCFAHFA;
import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TableNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/*
 * Updated/rewritten by eswasas based on new UI launch with workspace.
 * 7 Feb 2013
 *
 *  Refactored and Updated by eikrwaq
 */
@SuppressWarnings("deprecation")
public class TerminalTestGroupWcdmaHfaNewGUI extends BaseWcdmaHfa {
    private String dataIntegrityFlag;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    @Autowired
    @Qualifier("terminalEventAnalysisWCDMAHFA")
    private CommonWindow terminalEventAnalysisWCDMAHFA;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaHfaReserveDataS.csv");

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

    // EE12.2_WHFA_8.13; Terminal Handover Failure Event Analysis (VS No.
    // 4.11.21)
    @Test
    public void terminalFailuresEventAnalysis_8_13() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + BaseWcdmaHfa.defaultEventAnalysisWindow
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(defaultTerminalEventAnalysisColumns));

    }

    /*
     * EE12.2_WHFA_8.14; Drill down from Terminal Event Analysis to Detailed Event Analysis (SOHO) (VS No. 4.11.22) Action 3
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisSOHO_8_14() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMASOHOWindow);

    }

    /*
     * EE12.2_WHFA_8.15; Drill down from Terminal Event Analysis to Detailed Event Analysis (IFHO) (VS No. 4.11.23) Action 3
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisIFHO_8_15() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAIFHOWindow);

    }

    /*
     * EE12.2_WHFA_8.15; Drill down from Terminal Event Analysis to Detailed Event Analysis (IFHO) (VS No. 4.11.23) Action 6 Drill Down on Source RNC
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisDrillDownOnSourceRNCIFHO_8_15a() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        drillDownOnFailureThanOnGivenType(terminalEventAnalysisWCDMAHFA, GuiStringConstants.SOURCE_RNC);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultControllerEventAnalysisWindow);

    }

    /*
     * EE12.2_WHFA_8.16; Drill down from Terminal Event Analysis to Detailed Event Analysis (IRAT)(VS No. 4.11.24) Action 3
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisIRAT_8_16() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAIRATWindow);

    }

    /*
     * EE12.2_WHFA_8.16; Drill down from Terminal Event Analysis to Detailed Event Analysis (IRAT)(VS No. 4.11.24) Action 5 Drill Down on Target Cell
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisDrillDownOnTargetCellIRAT_8_16a() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        drillDownOnFailureThanOnGivenType(terminalEventAnalysisWCDMAHFA, GuiStringConstants.TARGET_CELL);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.TARGET;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTargetCellCodeHOAnalysisWindow);

    }

    /*
     * EE12.2_WHFA_8.17; Drill down from Terminal Event Analysis to Detailed Event Analysis (HSDSCH) (VS No. 4.11.25) Action 3
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisHSDSCH_8_17() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAHSDSCHWindow);

    }

    /*
     * EE12.2_WHFA_8.17; Drill down from Terminal Event Analysis to Detailed Event Analysis (HSDSCH) (VS No. 4.11.25) Action 4 Drill Down on Source
     * Cell
     */

    @Test
    public void terminalEventAnalysisToDetailedEventAnalysisDrillDownOnSourceCellHSDSCH_8_17a() throws InterruptedException, PopUpException,
            NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);
        final String manufactureVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(GuiStringConstants.MODEL);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        drillDownOnFailureThanOnGivenType(terminalEventAnalysisWCDMAHFA, GuiStringConstants.SOURCE_CELL);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal + GuiStringConstants.COMMA + tacVal
                + GuiStringConstants.DASH + GuiStringConstants.TERMINAL + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.SOURCE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultSourceCellEventAnalysisWindow);

    }

    /*
     * EE12.2_WHFA_8.18; Terminal Group Handover Failure Event Analysis (VS No: 4.11.26)
     */
    @Test
    public void terminalGroupFailuresEventAnalysis_8_18() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultGroupEventAnalysisWindow);

    }

    /*
     * EE12.2_WHFA_8.19; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (SOHO) (VS No: 4.11.27)
     */
    @Test
    public void terminalGroupEventAnalysisToDetailedEventAnalysisSOHO_8_19() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMASOHOWindow);

    }

    /*
     * EE12.2_WHFA_8.20; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (IFHO) (VS NO:4.11.28)
     */
    @Test
    public void terminalGroupEventAnalysisToDetailedEventAnalysisIFHO_8_20() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAIFHOWindow);

    }

    /*
     * EE12.2_WHFA_8.21; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (IRAT) (VS No: 4.11.29)
     */
    @Test
    public void terminalGroupEventAnalysisToDetailedEventAnalysisIRAT_8_21() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAIRATWindow);

    }

    /*
     * EE12.2_WHFA_8.22; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (HSDSCH)VS No: (VS No: 4.11.30)
     */
    @Test
    public void terminalGroupEventAnalysisToDetailedEventAnalysisHSDSCH_8_22() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);
        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup, BaseWcdmaHfa.Handover_Failure,
                BaseWcdmaHfa.Terminal_Event_Analysis_3G);

        int rowIndex = terminalEventAnalysisWCDMAHFA.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        terminalEventAnalysisWCDMAHFA.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        assertWindowTitleAndWindowsHeaders(terminalEventAnalysisWCDMAHFA, expectedWindowTitle, defaultTerminalEventAnalysiWCDMAHSDSCHWindow);

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

    /**
     * check if Data integrity flag is True
     *
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
    }

    private void assertWindowTitleAndWindowsHeaders(final CommonWindow commonWindow, final String expectedWindowTitle,
                                                    final List<String> expectedWindowsHearderList) {
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedWindowsHearderList + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(expectedWindowsHearderList));
    }

    private void doDataIntegrityForGivenIMSIInFailedEA(final CommonWindow commonWindow, final String... columnsToCheck) throws NoDataException {
        int rowIndex;
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI, reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnsToCheck);
    }

    private void drillDownOnFailureThanOnGivenType(final CommonWindow commonWindow, final String drillOnInSecondWindow) throws NoDataException,
            PopUpException {
        //Drill down on failure Using Handover
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.DRILL_ON));
        commonWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);

        //Now, Drill down Using Give Value
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.IMSI,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.IMSI));
        commonWindow.clickTableCell(rowIndex, drillOnInSecondWindow);
    }
}
