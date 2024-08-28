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
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
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
public class SubscriberTestGroupWcdmaCfaNewUI extends BaseWcdmaCfa {

    @Autowired
    @Qualifier("subscriberCallFailureEventAnalysis")
    private CommonWindow subCallFailureEventAnalysisWindow;

    @Autowired
    @Qualifier("subscriberFailureOverviewWcdmaCfa")
    private SubscriberOverviewWindow subFailureOverviewWcdmaCfaWindow;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaCfaReserveDataS.csv");

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
     * 4.8.3    13B_WCDMA_CFA_HFA_4.8.3: WCDMA_CFA: From Subscriber Overview Analysis, drilldown to Failure Analysis for IMSI.
     */
    @Test
    public void subscriberTabSubscriberOverviewFailureAnalysis_5_7_1() throws PopUpException, InterruptedException,
            NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Subscriber_Overview);

        final String subOverviewWindow = imsiVal + GuiStringConstants.DASH
                + GuiStringConstants.SUB_OVERVIEW_WINDOW_TITLE;
        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        subFailureOverviewWcdmaCfaWindow.maximizeWindow();
        verifyChartWindowTextValues("Number of Events", "Event", "Call Setup...", "Event Failures",
                "Failure Event Analysis", "Drag To Zoom");

        subFailureOverviewWcdmaCfaWindow.toggleGraphToGrid();

    }

    /*
     * 4.8.3    13B_WCDMA_CFA_HFA_4.8.3: WCDMA_CFA: From Subscriber Overview Analysis, drilldown to Failure Analysis for IMSI.
     *          Action 2
     */
    @Test
    public void subscriberTabFromSubscriberOverviewAnalysisDrilldownToFailureAnalysisForImsi_5_7_2()
            throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Subscriber_Overview);

        subFailureOverviewWcdmaCfaWindow.maximizeWindow();
        subFailureOverviewWcdmaCfaWindow.drilldownOnBarChartPortion("Call Setup...");

        final String subOverviewWindow = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER_OVERVIEW + GuiStringConstants.DASH
                + GuiStringConstants.FAILURE_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN
                + imsiVal + GuiStringConstants.COLON + CALL_SETUP_FAILURES;
        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        final List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

    }

    /*
     * 4.8.5    13B_WCDMA_CFA_HFA_4.8.5: WCDMA_CFA: Verify Subscriber Event Analysis on the basis of IMSI
     */

    @Test
    public void subscriberTabFromSubscriberEventAnalysisForImsi_5_7_3() throws PopUpException, InterruptedException,
            NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.Event_Trace_3G);

        final String subOverviewWindow = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER + GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        final List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

    }

    /*
     * 4.8.4    13B_WCDMA_CFA_HFA_4.8.4: WCDMA_CFA: Verify Subscriber Overview Analysis on the basis of IMSI group
     */

    @Test
    public void subscriberTabFromSubscriberOverviewAnalysisDrilldownToFailureAnalysisForImsiGroup_5_7_4()
            throws PopUpException, InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Subscriber_Overview);

        subFailureOverviewWcdmaCfaWindow.maximizeWindow();
        subFailureOverviewWcdmaCfaWindow.drilldownOnBarChartPortion(CALL_DROPS);

        final String subOverviewWindow = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.SUBSCRIBER_OVERVIEW + GuiStringConstants.DASH
                + GuiStringConstants.FAILURE_ANALYSIS + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN
                + imsiGroupVal + GuiStringConstants.COLON + CALL_DROPS;
        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        final List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

    }

    /*
     * 4.8.6        13B_WCDMA_CFA_HFA_4.8.6: WCDMA_CFA: From Subscriber Group Event Analysis, drilldown to Failure Analysis.
     */

    @Test
    public void subscriberTabFromSubscriberEventAnalysisForImsiGroup_5_7_5() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.Event_Trace_3G);

        final String subOverviewWindow = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        final List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultGroupEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultGroupEventAnalysisColumns));

    }

    /*
     * 4.8.6        13B_WCDMA_CFA_HFA_4.8.6: WCDMA_CFA: From Subscriber Group Event Analysis, drilldown to Failure Analysis.
     */

    @Test
    public void subscriberTabFromSubscriberEventAnalysisForImsiGroup_5_7_6() throws PopUpException,
            InterruptedException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                WcdmaStingConstantsNewUI.Call_Failure, WcdmaStingConstantsNewUI.Event_Trace_3G);

        int rowIndex = subCallFailureEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        subCallFailureEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        final String subOverviewWindow = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILURE_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        final List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsTacEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsTacEventAnalysisColumns));

    }

    // Private method to verify text labels on Subscriber Overview charts
    private void verifyChartWindowTextValues(final String... chartWindowText) {
        for (int i = 0; i < chartWindowText.length; i++) {
            assertTrue(GuiStringConstants.TEXT_NOT_FOUND + chartWindowText[i],
                    selenium.isTextPresent(chartWindowText[i]));
        }
    }

    /**
     * check if Data integrity flag is True 
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

        logger.log(Level.INFO, "Checking Data Integrity for " + dataToCheck + " " + GuiStringConstants.DOT
                + "Expected Value :" + expected + " Actual Value : " + actual);

        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck
                + GuiStringConstants.DOT, expected, actual);
    }

    /**
     * @param result
     * @param dataFields
     */
    protected void checkMultipleDataEntriesOnSameRow(final Map<String, String> result, final String... dataFields) {
        for (int i = 0; i < dataFields.length; i++) {
            checkDataIntegrity(dataFields[i], reservedDataHelperReplacement.getReservedData(dataFields[i]),
                    result.get(dataFields[i]));
        }
    }

}
