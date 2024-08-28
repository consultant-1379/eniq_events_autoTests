package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.CSVReaderCFAHFA;
import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.TableNameConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
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
import java.util.logging.Logger;

/*
 * Updated/rewritten by eswasas based on new UI launch with workspace
 * 6 Feb 2013
 *
 * Updated and Refactored By eikrawaq
 */
@SuppressWarnings("deprecation")
public class SubscriberTestGroupWcdmaHfaNewGUI extends BaseWcdmaHfa {
    private static final String LAST_DIV = "]/div[1]";

    private static final String DIV_PATH = "]/div[2]/div[1]/div[1]/div";

    private static final String DIV_ID_SELENIUM_TAG_CATEGORY_PANEL = "//div[@id='selenium_tag_categoryPanel']";

    private String dataIntegrityFlag;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

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
    @Qualifier("subscriberEventAnalysisForWCDMAHFA")
    private CommonWindow subscriberWCDMAHFAEventAnalysis;

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaHfaReserveDataS.csv");

    /* EE12.2_WHFA_5.1; Subscriber Handover Failure Analysis – IMSI based
     * VS No: 4.9.1
     */
    @Test
    public void subscriberHandoverFailureAnalysis_5_1() throws InterruptedException, PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER + GuiStringConstants.SPACE
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultHeadersOnSubscriberHFAWindow);

    }

    /* EE12.2_WHFA_5.1a; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (SOHO)
     * VS No: 4.9.2
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisSOHO_5_1a() throws InterruptedException,
            PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberWCDMASOHOWindow);

    }

    /* EE12.2_WHFA_5.1a; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (SOHO)
     * VS No: 4.9.2
     * Action 3 : Drill Down on TAC
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisSOHOActionThree_5_1a_1()
            throws InterruptedException, PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, TAC);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultEventAnalysisWindow);

    }

    /* EE12.2_WHFA_5.1a; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (SOHO)
     * VS No: 4.9.2
     * Action 4 : further Drill Down on Failure in Terminal Event Analysis
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisSOHOActionFour_5_1a_2()
            throws InterruptedException, PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, TAC);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));

        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberFailedEASOHOWindow);

    }

    /* EE12.2_WHFA_5.1b; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (IFHO)
     * VS No: 4.9.6
     * Action 1
     */

    @Test
    public void subscriberHandoverFailureAnalysisToEventAnalysisIFHO_5_1b() throws InterruptedException,
            PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER + GuiStringConstants.SPACE
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultHeadersOnSubscriberHFAWindow);

    }

    /* EE12.2_WHFA_5.1b; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (IFHO)
     * VS No: 4.9.6
     * Action 2
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisIFHO_5_1b_1() throws InterruptedException,
            PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberWCDMAIFHOWindow);

    }

    /*
     * 4.9.7    13B_WCDMA_CFA_HFA_4.9.7: WCDMA_HFA: Failed Event Analysis (IFHO) for Subscriber Handover Failure Analysis based on IMSI drill on Source Cell
     * Detailed Event Analysis (IFHO)
     * Action 3
     */

    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisAndThanOnSourceCellIFHO_5_1b_2()
            throws InterruptedException, PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        //filter the windows by Event Type
        subscriberWCDMAHFAEventAnalysis.filterColumnEquals(GuiStringConstants.EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TYPE));

        //further drill down on Source Cell
        List<String> listOfEventTimes = subscriberWCDMAHFAEventAnalysis.getAllTableDataAtColumn(EVENT_TIME);
        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(EVENT_TIME));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, GuiStringConstants.SOURCE_CELL);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.SOURCE;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultAccesAreaEventAnalysisWindow);

    }

    /*
     * 4.9.7    13B_WCDMA_CFA_HFA_4.9.7: WCDMA_HFA: Failed Event Analysis (IFHO) for Subscriber Handover Failure Analysis based on IMSI drill on Source Cell
     * Detailed Event Analysis (IFHO)
     * Action 4
     */

    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisAndThanOnSourceCellFutherDrillOnFailureIFHO_5_1b_3()
            throws InterruptedException, PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IFHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        //filter the windows by Event Type
        subscriberWCDMAHFAEventAnalysis.filterColumnEquals(GuiStringConstants.EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(GuiStringConstants.EVENT_TYPE));

        //further drill down on Source Cell
        List<String> listOfEventTimes = subscriberWCDMAHFAEventAnalysis.getAllTableDataAtColumn(EVENT_TIME);
        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(EVENT_TIME));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, GuiStringConstants.SOURCE_CELL);

        //further drill down on failures again
        rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultSubscriberWCDMAIFHOWindow);

    }

    /* EE12.2_WHFA_5.1c; Drill down from Subscriber HO failure totals to
     *  Detailed Event Analysis (IRAT)
     *  VS No: 4.9.10
     *  Action 1
     */
    @Test
    public void subscriberHandoverFailureAnalysisToEventAnalysisIRAT_5_1c() throws InterruptedException,
            PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER + GuiStringConstants.SPACE
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultHeadersOnSubscriberHFAWindow);

    }

    /* EE12.2_WHFA_5.1c; Drill down from Subscriber HO failure totals to
     *  Detailed Event Analysis (IRAT)
     *  VS No: 4.9.10
     *  Action 2
     */

    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisIRAT_5_1c_1() throws InterruptedException,
            PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberWCDMAIRATWindow);

    }

    /*
     * 4.9.13   13B_WCDMA_CFA_HFA_4.9.13: WCDMA_HFA: Failed Event Analysis (IRAT) for Subscriber Handover Failure Analysis based on IMSI drill on Target Cell
     * Detailed Event Analysis (IRAT)
     * Action 3
     */

    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisAndThanOnTargetCellIRAT_5_1c_2()
            throws InterruptedException, PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //drill down on failure Than Target Cell
        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, GuiStringConstants.TARGET_CELL);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.TARGET;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultTargetAccesAreaEventAnalysisWindow);

    }

    /*
     * 4.9.13   13B_WCDMA_CFA_HFA_4.9.13: WCDMA_HFA: Failed Event Analysis (IRAT) for Subscriber Handover Failure Analysis based on IMSI drill on Target Cell
     * Detailed Event Analysis (IRAT)
     * Action 3
     */

    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisAndThanOnTargetCellFurtherDrillDownOnFailureIRAT_5_1c_3()
            throws InterruptedException, PopUpException, NoDataException {

        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //drill down on failure Than Target Cell
        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, GuiStringConstants.TARGET_CELL);

        // Now, Drill down on Failure
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultSubscriberWCDMAIRATWindow);

    }

    /* EE12.2_WHFA_5.1d; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (HSDSCH)
     * VS No:4.9.14
     * Action 1
     */
    @Test
    public void subscriberHandoverFailureAnalysisToEventAnalysisHSDSCH_5_1d() throws InterruptedException,
            PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_IRAT_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.SUBSCRIBER + GuiStringConstants.SPACE
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultHeadersOnSubscriberHFAWindow);

    }

    /* EE12.2_WHFA_5.1d; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (HSDSCH)
     * VS No:4.9.14
     * Action 2
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisHSDSCH_5_1d_1() throws InterruptedException,
            PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberWCDMAHSDSCHWindow);

    }

    /* EE12.2_WHFA_5.1d; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (HSDSCH)
     * VS No:4.9.14
     * Action 3
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisHSDSCHActionThree_5_1d_2()
            throws InterruptedException, PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_HSDSCH_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, TAC);

        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.HANDOVER_FAILURE_ANALYSIS + GuiStringConstants.DASH + GuiStringConstants.WCDMA;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultEventAnalysisWindow);

    }

    /* EE12.2_WHFA_5.1d; Drill down from Subscriber HO failure totals to
     * Detailed Event Analysis (HSDSCH)
     * VS No:4.9.14
     * Action 4
     */
    @Test
    public void drilldownSubscriberHandoverFailureAnalysisToEventAnalysisHSDSCHActionFour_5_1d_3()
            throws InterruptedException, PopUpException, NoDataException {
        final String imsiVal = reservedDataHelperReplacement.getReservedData(IMSI);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI, imsiVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        drillDownOnFailureThanOnGivenTypeUsingEventTime(subscriberWCDMAHFAEventAnalysis, TAC);

        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));

        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(imsiVal, subscriberWCDMAHFAEventAnalysis, defaultSubscriberFailedEAHSDSCHWindow);

    }

    /* 4.9.23 13B_WCDMA_CFA_HFA_4.9.23; WCDMA_HFA; Subscriber Handover Failure
     * Analysis MSISDN based
     * VS No: 4.9.23
     */

    @Test
    public void mSISDNHandoverFailureAnalysis_5_2() throws InterruptedException, PopUpException, NoDataException {
        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.MSISDN, BaseWcdmaHfa.MSISDN_VALUE, "", "");

        final int numberOfDivHeaders = countAnalysisWindowHeaders();

        for (int i = 1; i <= numberOfDivHeaders; i++) {
            final int numberOfChildDivs = countChildDivsOfAnalysisWindowHeaders(i);
            for (int j = 1; j <= numberOfChildDivs; j++) {
                if (selenium.isElementPresent(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL + "[" + i + DIV_PATH + "[" + j
                        + LAST_DIV)) {
                    final String val = selenium.getText(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL + "[" + i + DIV_PATH + "["
                            + j + LAST_DIV);
                    assertFalse(val, val.equalsIgnoreCase(BaseWcdmaHfa.Handover_Failure));
                    assertFalse(val, val.equalsIgnoreCase(BaseWcdmaHfa.Call_Failure));
                }
            }
        }

    }

    /* 4.9.24 13B_WCDMA_CFA_HFA_4.9.24; WCDMA_HFA; Subscriber Handover Failure
     * Analysis PTMSI based
     * VS No:4.9.24
     */
    @Test
    public void pTMSIHandoverFailureAnalysis_5_3() throws InterruptedException, PopUpException, NoDataException {

        final String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.PTIMSI, BaseWcdmaHfa.PTMSI_VALUE, "", "");

        final int numberOfDivHeaders = countAnalysisWindowHeaders();

        for (int i = 1; i <= numberOfDivHeaders; i++) {
            final int countChildDivs = countChildDivsOfAnalysisWindowHeaders(i);
            for (int j = 1; j <= countChildDivs; j++) {
                if (selenium.isElementPresent(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL + "[" + i + DIV_PATH + "[" + j
                        + LAST_DIV)) {
                    System.out.println("i: " + i + ", j: " + j);
                    final String val = selenium.getText(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL + "["

                    + i + DIV_PATH + "[" + j + LAST_DIV);

                    assertTrue(val, val.equalsIgnoreCase(BaseWcdmaHfa.CORE_PS));
                    assertFalse(val, val.equalsIgnoreCase(BaseWcdmaHfa.Handover_Failure));
                    assertFalse(val, val.equalsIgnoreCase(BaseWcdmaHfa.Call_Failure));
                }
            }

        }
    }

    /* EE12.2_WHFA_5.4; Subscriber Group Handover Failure Analysis
     * VS No: 4.9.18
     */
    @Test
    public void subscriberGroupHandoverFailureAnalysis_5_4() throws InterruptedException, PopUpException,
            NoDataException {
        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberWCDMAHFAEventAnalysis.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultHeadersOnSubscriberGroupHFAWindow));

    }

    /* EE12.2_WHFA_5.4a; Drilldown to Subscriber Group Handover Failure Event
     * Analysis from summary pane (SOHO)
     * VS No: 4.9.19
     */
    @Test
    public void subscriberGroupHandoverFailureAnalysisDrilldownSOHO_5_4a() throws InterruptedException, PopUpException,
            NoDataException {
        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures of particular handover type
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultGroupSubscriberWCDMASOHOWindow);

    }

    /* EE12.2_WHFA_5.4b; Drilldown to Subscriber Group Handover Failure Event
     * Analysis from summary pane (IFHO)
     * VS No: 4.9.20
     */
    @Test
    public void subscriberGroupHandoverFailureAnalysisDrilldownIFHO_5_4b() throws InterruptedException, PopUpException,
            NoDataException {

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures of particular handover type
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultGroupNetworkEventAnalysiWCDMAIFHOWindow);

    }

    /* EE12.2_WHFA_5.4c; Drilldown to Subscriber Group Handover Failure Event
     * Analysis from summary pane (IRAT)
     * VS No: 4.9.21
     */
    @Test
    public void subscriberGroupHandoverFailureAnalysisDrilldownIRAT_5_4c() throws InterruptedException, PopUpException,
            NoDataException {

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures of particular handover type
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow);

    }

    /* EE12.2_WHFA_5.4d; Drilldown to Subscriber Group Handover Failure Event
     * Analysis from summary pane (HSDSCH)
     * VS No: 4.9.22
     */
    @Test
    public void subscriberGroupHandoverFailureAnalysisDrilldownHSDSCH_5_4d() throws InterruptedException,
            PopUpException, NoDataException {

        final String imsiGroupVal = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        final String timeRange = CommonUtils
                .getCustomTimeRange(TableNameConstants.EVENT_E_RAN_HFA_SOHO_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.IMSI_GROUP, imsiGroupVal,
                BaseWcdmaHfa.Handover_Failure, BaseWcdmaHfa.Event_Trace_3G);

        //Drill Down on failures of particular handover type
        int rowIndex = subscriberWCDMAHFAEventAnalysis.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(HANDOVER_TYPE_2));
        subscriberWCDMAHFAEventAnalysis.clickTableCell(rowIndex, FAILURES);

        final String expectedWindowTitle = imsiGroupVal + GuiStringConstants.DASH + GuiStringConstants.IMSI_GROUP
                + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertWindowTitleAndWindowsHeaders(subscriberWCDMAHFAEventAnalysis, expectedWindowTitle,
                defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow);

    }

    //---------------------------------------------- Private Methods ----------------------------------------------//

    private int countAnalysisWindowHeaders() {
        return (selenium.getXpathCount(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL)).intValue();
    }

    private int countChildDivsOfAnalysisWindowHeaders(final int i) {
        return (selenium.getXpathCount(DIV_ID_SELENIUM_TAG_CATEGORY_PANEL + "[" + i + DIV_PATH)).intValue();
    }

    /**
     * check if Data integrity flag is True
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
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

    private void doDataIntgrityOnEventTimeBasis(final CommonWindow commonWindow, final String... itemToCheck)
            throws NoDataException {
        int rowIndex;
        List<String> listOfEventTimes = commonWindow.getAllTableDataAtColumn(EVENT_TIME);

        String eventTimeDSL = CommonUtils.dayLightSavingTimeCheck(reservedDataHelperReplacement.getReservedData(EVENT_TIME));

        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,eventTimeDSL);

        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);

        checkMultipleDataEntriesOnSameRow(result, itemToCheck);
    }

    private void assertWindowTitleAndHearders(final String imsiVal, final CommonWindow commonWindow,
            final List<String> windowsHeaderList) {
        final String expectedWindowTitle = imsiVal + GuiStringConstants.DASH + IMSI + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + windowsHeaderList
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(windowsHeaderList));
    }

    private void assertWindowTitleAndWindowsHeaders(final CommonWindow commonWindow, final String expectedWindowTitle,
            final List<String> expectedWindowsHearderList) {
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> windowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedWindowsHearderList
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(expectedWindowsHearderList));
    }

    private void drillDownOnFailureThanOnGivenTypeUsingEventTime(final CommonWindow commonWindow,
            final String drillDownOn) throws NoDataException, PopUpException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(HANDOVER_TYPE,
                reservedDataHelperReplacement.getReservedData(DRILL_ON));
        commonWindow.clickTableCell(rowIndex, FAILURES);

        List<String> listOfEventTimes = commonWindow.getAllTableDataAtColumn(EVENT_TIME);

        rowIndex = CommonUtils.getIndexofElementContains(listOfEventTimes,
                reservedDataHelperReplacement.getReservedData(EVENT_TIME));

        commonWindow.clickTableCell(rowIndex, drillDownOn);
    }

    private void doDataIntegrityForGivenIMSIInFailedEA(final CommonWindow commonWindow, final String... columnsToCheck)
            throws NoDataException {
        int rowIndex;
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnsToCheck);
    }
}
