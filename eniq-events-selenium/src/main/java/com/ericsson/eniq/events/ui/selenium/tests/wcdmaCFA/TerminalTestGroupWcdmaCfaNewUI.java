/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
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

@SuppressWarnings("deprecation")
public class TerminalTestGroupWcdmaCfaNewUI extends BaseWcdmaCfa {

    @Autowired
    @Qualifier("terminalEventAnalysisWcdmaCfa")
    private CommonWindow terminalEventAnalysisWcdmaCfaWindow;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaCfaReserveDataS.csv");

    private final static String CFA_WCDMA = GuiStringConstants.DASH + "Call Failure Analysis" + GuiStringConstants.DASH
            + "WCDMA";

    private String dataIntegrityFlag;

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
     * 4.7.7    13B_WCDMA_CFA_HFA_4.7.7: WCDMA_CFA: Event Analysis for Terminal- Failure drill down - TAC Event Analysis.
     */
    @Test
    public void terminalTabTerminalEventAnalysisCallFailureAnalysis_5_6_1() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String manufactureVal = reservedDataHelperReplacement.getReservedData(MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);
        final String modelVal = reservedDataHelperReplacement.getReservedData(MODEL);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        final String expectedWindowTitle = manufactureVal + GuiStringConstants.DASH + modelVal
                + GuiStringConstants.COMMA + tacVal + GuiStringConstants.DASH + GuiStringConstants.TERMINAL
                + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalEventAnalysis
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultTerminalEventAnalysis));

    }

    /*
     * 4.7.7    13B_WCDMA_CFA_HFA_4.7.7: WCDMA_CFA: Event Analysis for Terminal - Failure drill down - TAC Event Analysis.
     *          Action 2
     */
    @Test
    public void terminalTabTerminalEventAnalysisCallFailureDrillDownCallDrops_5_6_2() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String manufactureVal = reservedDataHelperReplacement.getReservedData(MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        int rowIndex = terminalEventAnalysisWcdmaCfaWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        terminalEventAnalysisWcdmaCfaWindow.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsTacEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsTacEventAnalysisColumns));

    }

    /*
     * 4.7.7    13B_WCDMA_CFA_HFA_4.7.7: WCDMA_CFA: Event Analysis for Terminal - Failure drill down - TAC Event Analysis.
     *          Action 2
     */
    @Test
    public void terminalTabTerminalEventAnalysisCallFailureDrillDownCallSetupFailure_5_6_3() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String manufactureVal = reservedDataHelperReplacement.getReservedData(MANUFACTURER);
        final String tacVal = reservedDataHelperReplacement.getReservedData(TAC);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL, manufactureVal, tacVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        int rowIndex = terminalEventAnalysisWcdmaCfaWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        terminalEventAnalysisWcdmaCfaWindow.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callTerminalSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callTerminalSetupFailedEventAnalysisColumns));

    }

    /*
     * 4.7.8        13B_WCDMA_CFA_HFA_4.7.8: WCDMA_CFA: Event Analysis for Terminal Group - Failure drill down - TAC Event Analysis.
     */
    @Test
    public void terminalTabTerminalGroupEventAnalysisCallFailureAnalysis_5_6_4() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        final String expectedWindowTitle = terminalGroup + GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.CALL_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultGroupEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultGroupEventAnalysisColumns));

    }

    /*
     * 4.7.8        13B_WCDMA_CFA_HFA_4.7.8: WCDMA_CFA: Event Analysis for Terminal Group - Failure drill down - TAC Event Analysis.
     *             Call Drops
     *             Action 2
     */
    @Test
    public void terminalTabTerminalGroupEventAnalysisCallFailureAnalysisDrillDownonCallDrops_5_6_5()
            throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        int rowIndex = terminalEventAnalysisWcdmaCfaWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        terminalEventAnalysisWcdmaCfaWindow.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + CFA_WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsTacEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsTacEventAnalysisColumns));

    }

    /*
     * 4.7.8        13B_WCDMA_CFA_HFA_4.7.8: WCDMA_CFA: Event Analysis for Terminal Group - Failure drill down - TAC Event Analysis.
     *             Call Setup Failure
     *             Action 2
     */

    @Test
    public void terminalTabTerminalGroupEventAnalysisCallFailureAnalysisDrillDownonCallSetupFailure_5_6_6()
            throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String terminalGroup = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.TERMINAL_GROUP, terminalGroup,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.TERMINAL_EVENT_ANALYSIS_3G);

        int rowIndex = terminalEventAnalysisWcdmaCfaWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        terminalEventAnalysisWcdmaCfaWindow.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + CFA_WCDMA;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callTerminalSetupFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callTerminalSetupFailedEventAnalysisColumns));

    }

    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {

        logger.log(Level.INFO, "Checking Data Integrity for " + dataToCheck + " " + GuiStringConstants.DOT
                + "Expected Value :" + expected + " Actual Value : " + actual);

        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck
                + GuiStringConstants.DOT, expected, actual);
    }

    protected void checkMultipleDataEntriesOnSameRow(final Map<String, String> result, final String... dataFields) {
        for (int i = 0; i < dataFields.length; i++) {
            checkDataIntegrity(dataFields[i], reservedDataHelperReplacement.getReservedData(dataFields[i]),
                    result.get(dataFields[i]));
        }
    }

    private void assertDataIntegrityOnWindow(final CommonWindow commonWindow, final String... eventList)
            throws NoDataException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, eventList);
    }

    /**
     * check if Data integrity flag is True
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
    }
}
