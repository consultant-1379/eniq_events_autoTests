/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("deprecation")
public class NetworkTestGroupWcdmaCfaNewUI extends BaseWcdmaCfa {

    private String dataIntegrityFlag;

    @Autowired
    @Qualifier("networkEventAnalysisWcdmaCfa")
    private CommonWindow networkEventAnalysisWindow;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaBscCallSetup")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaBscCallSetup;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaCellCallSetupConfig")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaCellCallSetup;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops;

    @Autowired
    @Qualifier("networkDetailedEventAnalysisWcdmaCfaCellCallDropsConfig")
    private CommonWindow networkDetailedEventAnalysisWcdmaCfaCellCallDrops;

    @Autowired
    @Qualifier("wcdmaNetworkCauseCodeAnalysis")
    private CommonWindow wcdmaNetworkCauseCodeAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkSubCauseCodeDetailedAnalysis")
    private CommonWindow wcdmaNetworkSubCauseCodeDetailedAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkRncRabTypeAnalysisDrillByBsc")
    private CommonWindow wcdmaNetworkRncRabTypeAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkRncDisconnectionCodeAnalysisDrillByBsc")
    private CommonWindow wcdmaNetworkRncDisconnectionCodeAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkAccessAreaSubCauseCodeDrillByCell")
    private CommonWindow wcdmaNetworkAccessAreaSubCauseCode;

    @Autowired
    @Qualifier("wcdmaNetworkAccessAreaRabTypeAnalysis")
    private CommonWindow wcdmaNetworkAccessAreaRabTypeAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkAccessAreaDisconnectionCodeAnalysis")
    private CommonWindow wcdmaNetworkAccessAreaDisconnectionCodeAnalysis;

    @Autowired
    @Qualifier("wcdmaNetworkRncDisconnectionCodeAnalysisToggleToGrid")
    private CommonWindow wcdmaNetworkRncDisconnectionCodeAnalysisToggleToGrid;

    @Autowired
    @Qualifier("wcdmaNetworkAccessAreaDisconnectionCodeAnalysisToggleToGrid")
    private CommonWindow wcdmaNetworkAccessAreaDisconnectionCodeAnalysisToggleToGrid;

    @Autowired
    @Qualifier("networkControllerSubCauseCodeWidget")
    private CommonWindow networkControllerSubCauseCodeWidget;

    private final static List<String> defaultCallSetupFailureAccessAreaColumns = Arrays.asList(RAN_VENDOR, CONTROLLER,
            ACCESS_AREA, EVENT_TYPE, FAILURES, RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS,
            GuiStringConstants.RAB_FAILURE_RATIO_PER);

    private final static List<String> defaultControllerGroupAnalysisColumns = Arrays.asList(EVENT_TYPE, FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS);

    private final static String[] controllerNetworkEventAnalysisColumnsToCheckForDataIntegrity = new String[] {
            RAN_VENDOR, CONTROLLER, FAILURES, EVENT_TYPE, RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS };

    static CSVReaderCFAHFA reservedDataHelperReplacement = new CSVReaderCFAHFA("WcdmaCfaReserveDataS.csv");

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

    /*
     *  VS Reference -- 4.6.12 13B_WCDMA_CFA_HFA_4.6.12 :
     *  Verify that the selection of Total RAB Failures option along with Call Setup Failures will
     *  display an appropriate Call Failure Event Analysis window
     *   */
    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_9() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHearders(TOTAL_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(RNC),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);
    }

    /*
     *  VS Reference -- 4.6.12 13B_WCDMA_CFA_HFA_4.6.12 :
     *  Verify that the selection of Total RAB Failures option along with Call Setup Failures will
     *  display an appropriate Call Failure Event Analysis window
     *
     *  Action Four further drill down on Failures
     *   */
    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFour_5_5_9() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(RNC),
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, defaultFailedEventAnalysisWindowColumns);

    }

    /*
     * 4.6.20   13B_WCDMA_CFA_HFA_4.6.20: WCDMA_CFA: From Circuit Switched RAB Failures, verify drilldown to Call Setup Failures
     */
    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_9a() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHearders(CIRCUIT_SWITCHED_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(RNC),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);

    }

    /*
     * 4.6.20   13B_WCDMA_CFA_HFA_4.6.20: WCDMA_CFA: From Circuit Switched RAB Failures, verify drilldown to Call Setup Failures
     *
     *  Action 4 : further drill down on failures
     */
    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFour_5_5_9a() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);
        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(RNC),
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, defaultFailedEventAnalysisWindowColumns);

    }

    /*
     * 4.6.23   13B_WCDMA_CFA_HFA_4.6.23: WCDMA_CFA: From Packet Switched RAB Failures,
     *          verify drilldown to Call Setup Failures
     */

    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_9b() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHearders(PACKET_SWITCHED_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(RNC),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);

    }

    /*
     * 4.6.23   13B_WCDMA_CFA_HFA_4.6.23: WCDMA_CFA: From Packet Switched RAB Failures,
     *          verify drilldown to Call Setup Failures
     * Action 4 Further drill down on failures
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFour_5_5_9b() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);
        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(RNC),
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, defaultFailedEventAnalysisWindowColumns);

    }

    /*
     * 4.6.25   13B_WCDMA_CFA_HFA_4.6.25: WCDMA_CFA: From Multi RAB Failures, verify drilldown to Call Setup Failures
     */
    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_9c() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHearders(MULTI_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(RNC),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);

    }

    /*
     * 4.6.25   13B_WCDMA_CFA_HFA_4.6.25: WCDMA_CFA: From Multi RAB Failures, verify drilldown to Call Setup Failures
     * Action 4 Further drill down on Failures
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFour_5_5_9c() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(RNC),
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, defaultFailedEventAnalysisWindowColumns);

    }

    /*
     * 4.6.16   13B_WCDMA_CFA_HFA_4.6.16: WCDMA_CFA: From Total RAB Failures,
     * verify drilldown to Call Drops (Detailed Event Analysis)
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByEventTypeToFailedEventAnalysis_5_5_10()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHearders(TOTAL_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);

    }

    /*
     * 4.6.16   13B_WCDMA_CFA_HFA_4.6.16: WCDMA_CFA: From Total RAB Failures,
     * verify drilldown to Call Drops (Detailed Event Analysis)
     *
     * Action 5 : Drill-down into the hyper-linked failures and select "Detailed Event Analysis"
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByEventTypeToFailedEventAnalysisActionFive_5_5_10()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, callDropsFailedEventAnalysisColumns);

    }

    /*
     * 4.6.19   13B_WCDMA_CFA_HFA_4.6.19: WCDMA_CFA: From Circuit Switched RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     */

    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_10a() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHearders(CIRCUIT_SWITCHED_RAB_FAILURES,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), networkEventAnalysisWindow,
                defaultControllerNetworkEventAnalysisWindowColumns);

    }

    /*
     * 4.6.19   13B_WCDMA_CFA_HFA_4.6.19: WCDMA_CFA: From Circuit Switched RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     * Action 5 Drill-down into the hyper-linked failures and select "Detailed Event Analysis"
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFive_5_5_10a() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, callDropsFailedEventAnalysisColumns);
    }

    /*
     * 4.6.22   13B_WCDMA_CFA_HFA_4.6.22: WCDMA_CFA: From Packet Switched RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     */

    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_10b() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHearders(PACKET_SWITCHED_RAB_FAILURES,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), networkEventAnalysisWindow,
                defaultControllerNetworkEventAnalysisWindowColumns);
    }

    /*
     * 4.6.22   13B_WCDMA_CFA_HFA_4.6.22: WCDMA_CFA: From Packet Switched RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     *
     *  Action 5 Drill-down into the hyper-linked failures and select "Detailed Event Analysis"
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFive_5_5_10b() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, callDropsFailedEventAnalysisColumns);
    }

    /*
     * 4.6.26   13B_WCDMA_CFA_HFA_4.6.26: WCDMA_CFA: From Multi RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     */
    @Test
    public void networkTabNetworkEventAnalysisForController_5_5_10c() throws PopUpException, InterruptedException,
            NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHearders(MULTI_RAB_FAILURES, reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkEventAnalysisWindow, defaultControllerNetworkEventAnalysisWindowColumns);
    }

    /*
     * 4.6.26   13B_WCDMA_CFA_HFA_4.6.26: WCDMA_CFA: From Multi RAB Failures,
     *          verify drilldown to Call Drops (Detailed Event Analysis)
     * Action 5 Drill-down into the hyper-linked failures and select "Detailed Event Analysis"
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerActionFive_5_5_10c() throws PopUpException,
            InterruptedException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);
        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        assertWindowTitleAndHearders(reservedDataHelperReplacement.getReservedData(CONTROLLER),
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, callDropsFailedEventAnalysisColumns);
    }

    /*
     *  Select 'Controller' as dimension and select a controller from the controller list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failures' and call drops
     *
     *  Drill down on Failure by Event Type and finally drill Down on TAC and then, assert record
     */
    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByTacFromFailedEventAnalysis_5_5_11a()
            throws InterruptedException, PopUpException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops.getRowNumberWithMatchingValueForGivenColumn(
                IMSI, reservedDataHelperReplacement.getReservedData(IMSI));
        networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, defaultTerminalEventAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));
    }

    /*
     *  Select 'Controller' as dimension and select a controller from the controller list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failures' and Call Setup Failures
     *
     *  Drill down on Failure by Event Type and finally drill Down on TAC and then, assert record
     */
    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByTacFromFailedEventAnalysis_5_5_11b()
            throws InterruptedException, PopUpException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaBscCallSetup.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkDetailedEventAnalysisWcdmaCfaBscCallSetup.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, defaultTerminalEventAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER));
    }

    /*
     *  Select 'Controller' as dimension and select a controller from the controller list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Circuit Switched Rab Failures' and Call Setup Failures
     *
     *  Drill down on Failure by Event Type and finally drill Down on Access Area and then, assert record
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis_5_5_12a()
            throws InterruptedException, PopUpException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaBscCallSetup.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));

        networkDetailedEventAnalysisWcdmaCfaBscCallSetup.clickTableCell(rowIndex, ACCESS_AREA);

        networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaBscCallSetup, networkDetailedEventAnalysisWcdmaCfaCellCallSetup,
                defaultCallSetupFailureAccessAreaColumns, false);
    }

    /*
     *  Select 'Controller' as dimension and select a controller from the controller list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Multi Rab Failures' and Call Drops
     *
     *  Drill down on Failure by Event Type and finally drill Down on Access Area and then, assert record
     */

    @Test
    public void networkTabNetworkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis_5_5_12b()
            throws InterruptedException, PopUpException, NoDataException {

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops.getRowNumberWithMatchingValueForGivenColumn(
                IMSI, reservedDataHelperReplacement.getReservedData(IMSI));

        networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops.clickTableCell(rowIndex, ACCESS_AREA);

        networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaBscCfaCallDrops, networkDetailedEventAnalysisWcdmaCfaCellCallDrops,
                defaultCallSetupFailureAccessAreaColumns, true);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failues' and Call Setup Failures
     *
     *  assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_13() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHeardersForAccessArea(TOTAL_RAB_FAILURES, accessAreaValue, networkEventAnalysisWindow,
                defaultCallSetupFailureAccessAreaColumns);

    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failues' and Call Setup Failures
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_13()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallSetup,
                defaultAccessAreaFailedEventAnalysisWindowColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Circuit Switch Rab Failures' and Call Setup Failures
     *
     *  assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_13a() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHeardersForAccessArea(CIRCUIT_SWITCHED_RAB_FAILURES, accessAreaValue,
                networkEventAnalysisWindow, defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Circuit Switch Rab Failures' and Call Setup Failures
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_13a()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallSetup,
                defaultAccessAreaFailedEventAnalysisWindowColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Packet Switched Rab Failures' and Call Setup Failures
     *
     *  assert the Record for Event Type 'Call Set Failures'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_13b() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHeardersForAccessArea(PACKET_SWITCHED_RAB_FAILURES, accessAreaValue,
                networkEventAnalysisWindow, defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Packet Switched Rab Failures' and Call Setup Failures
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_13b()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallSetup,
                defaultAccessAreaFailedEventAnalysisWindowColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Multi Rab Failures' and Call Setup Failures
     *
     *  assert the Record for Event Type 'Call Set Failures'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_13c() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_SETUP_FAILURES);

        assertWindowTitleAndHeardersForAccessArea(MULTI_RAB_FAILURES, accessAreaValue, networkEventAnalysisWindow,
                defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Multi Rab Failures' and Call Setup Failures
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Set Failures'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_13c()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_SETUP_FAILURES);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallSetup,
                defaultAccessAreaFailedEventAnalysisWindowColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failures' and Call Drops
     *
     *  assert the Record for Event Type 'Call Drops'
     *
     */

    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_14() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHeardersForAccessArea(TOTAL_RAB_FAILURES, accessAreaValue, networkEventAnalysisWindow,
                defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Total Rab Failures' and 'Call Drops'
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_14()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallDrops,
                defaultcallDropsFailedEventAnalysisColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Circuit Switched Rab Failures' and Call Drops
     *
     *  assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_14a() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHeardersForAccessArea(CIRCUIT_SWITCHED_RAB_FAILURES, accessAreaValue,
                networkEventAnalysisWindow, defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Circuit Switched Rab Failures' and 'Call Drops'
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_14a()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallDrops,
                defaultcallDropsFailedEventAnalysisColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Packet Switched Rab Failures' and Call Drops
     *
     *  assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_14b() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHeardersForAccessArea(PACKET_SWITCHED_RAB_FAILURES, accessAreaValue,
                networkEventAnalysisWindow, defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Packet Switched Rab Failures' and 'Call Drops'
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_14b()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallDrops,
                defaultcallDropsFailedEventAnalysisColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Multi Rab Failures' and Call Drops
     *
     *  assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessArea_5_5_14c() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        assertWindowTitleAndHeardersForAccessArea(MULTI_RAB_FAILURES, accessAreaValue, networkEventAnalysisWindow,
                defaultCallSetupFailureAccessAreaColumns);
    }

    /*
     *  Select 'Access Area' as dimension and select a Access Area from the list.
     *  Select 'Network Event Analysis' Analysis Window followed by 'Call Failure (3G)'. Click 'Launch'.
     *  Select 'Multi Rab Failures' and 'Call Drops'
     *
     *  Drill down by Event Analysis Summary on failures for given event
     *  and assert the Record for Event Type 'Call Drops'
     *
     */
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_14c()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);

        assertWindowTitleAndHearders(accessAreaValue, networkDetailedEventAnalysisWcdmaCfaCellCallDrops,
                defaultcallDropsFailedEventAnalysisColumns);
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByEventTypeToFailedEventAnalysis_5_5_15()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);

        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                       reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        selenium.click(WcdmaStingConstantsNewUI.Access_Area_Toggle);
        selenium.waitForPageLoadingToComplete();

        assertWindowTitleAndHeardersForAccessArea(TOTAL_RAB_FAILURES, accessAreaValue, wcdmaNetworkAccessAreaDisconnectionCodeAnalysisToggleToGrid,
        defaultDisconnectionCodeWcdmaColumnsGrid);

       selenium.click(WcdmaStingConstantsNewUI.Access_Area_Export);
       selenium.waitForPageLoadingToComplete();
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByTacFromFailedEventAnalysis_5_5_15a()
            throws NoDataException, PopUpException, InterruptedException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);
        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaCellCallDrops.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkDetailedEventAnalysisWcdmaCfaCellCallDrops.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaCellCallDrops, defaultTerminalEventAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA));
    }

    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaDrillByTacFromFailedEventAnalysis_5_5_15b()
            throws NoDataException, PopUpException, InterruptedException {

        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_SETUP_FAILURES);
        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);
        networkEventAnalysisWindow.closeWindow();

        rowIndex = networkDetailedEventAnalysisWcdmaCfaCellCallSetup.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkDetailedEventAnalysisWcdmaCfaCellCallSetup.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(
                networkDetailedEventAnalysisWcdmaCfaCellCallSetup, defaultTerminalEventAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA));
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroup_5_5_16() throws InterruptedException, PopUpException,
            NoDataException {
        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, controllerGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        assertWindowTitleAndColumnsForGroupEventAnalysisWindow(controllerGroupValue, networkEventAnalysisWindow,
                defaultControllerGroupAnalysisColumns);

    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByEventTypeToFailedEventAnalysisCallDrops_5_5_17()
            throws InterruptedException, PopUpException, NoDataException {

        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, controllerGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowsTitleAndColumnsForGroupFailedEA(controllerGroupValue, networkEventAnalysisWindow,
                callDropsFailedEventAnalysisColumns);

    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByEventTypeToFailedEventAnalysisCallSetupFailures_5_5_17a()
            throws InterruptedException, PopUpException, NoDataException {

        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, controllerGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowsTitleAndColumnsForGroupFailedEA(controllerGroupValue, networkEventAnalysisWindow,
                defaultFailedEventAnalysisWindowColumns);

    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByTacFromFailedEventAnalysisCallDrops_5_5_18()
            throws InterruptedException, PopUpException, NoDataException {

        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, controllerGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWindow.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(networkEventAnalysisWindow,
                defaultTerminalEventAnalysis, null);
    }

    @Test
    public void networkTabNetworkEventAnalysisForControllerGroupDrillByTacFromFailedEventAnalysisSetupFailures_5_5_19()
            throws InterruptedException, PopUpException, NoDataException {

        final String controllerGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER_GROUP, controllerGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWindow.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(networkEventAnalysisWindow,
                defaultTerminalEventAnalysis, null);
    }

    @Ignore("Ignoring because Access Area Group Analysis is not working. Jira Ref : http://jira-oss.lmera.ericsson.se/browse/EQEV-5564")
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroup_5_5_20() throws InterruptedException, PopUpException,
            NoDataException {

        final String accessAreaGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, accessAreaGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        assertWindowTitleAndColumnsForGroupEventAnalysisWindow(accessAreaGroupValue, networkEventAnalysisWindow,
                defaultControllerGroupAnalysisColumns);
        if (isDataIntegrityFlagOn()) {
            doDataIntegrityOnGroupEventAnalysis(networkEventAnalysisWindow);
        }
    }

    @Ignore("Ignoring because Access Area Group Analysis is not working. will need to go throught the xml file and code for this TC Jira Ref : http://jira-oss.lmera.ericsson.se/browse/EQEV-5564")
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByEventTypeToFailedEventAnalysis_5_5_21()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, accessAreaGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        assertWindowsTitleAndColumnsForGroupFailedEA(accessAreaGroupValue, networkEventAnalysisWindow,
                defaultFailedEventAnalysisWindowColumns);

        if (isDataIntegrityFlagOn()) {
            doDataIntegrityForGivenIMSIInFailedEA(networkEventAnalysisWindow);
        }
    }

    @Ignore("Ignoring because Access Area Group Analysis is not working. will need to go throught the xml file and code for this TC Jira Ref : http://jira-oss.lmera.ericsson.se/browse/EQEV-5564")
    @Test
    public void networkTabNetworkEventAnalysisForAccessAreaGroupDrillByTacFromFailedEventAnalysis_5_5_22()
            throws InterruptedException, PopUpException, NoDataException {

        final String accessAreaGroupValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA_GROUP, accessAreaGroupValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        networkEventAnalysisWindow.clickTableCell(rowIndex, TAC);

        networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(networkEventAnalysisWindow,
                defaultTerminalEventAnalysis, null);
    }

    /*
     * 4.6.33   13B_WCDMA_CFA_HFA_4.6.33: WCDMA_CFA: Network Cause Code Analysis by Controller - Drill to Detailed Event Analysis from Grid
     */
    @Test
    public void networkTabControllerWcdmaCallFailureCauseCodeAnalysis_5_5_23() throws InterruptedException,
            PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER)
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(RAN_VENDOR)
                + GuiStringConstants.COMMA + GuiStringConstants.THREE_G + GuiStringConstants.DASH
                + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE
                + GuiStringConstants.SPACE + WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerCauseCodeWcdmaColumns));

    }

    /*
     * 4.6.33   13B_WCDMA_CFA_HFA_4.6.33: WCDMA_CFA: Network Cause Code Analysis by Controller - Drill to Detailed Event Analysis from Grid
     *          Action 4
     */
    @Test
    public void networkTabControllerWcdmaCallFailureCauseCodeAnalysisDrillOnCauseCode_5_5_23a()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        int rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE_ID,
                reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, CAUSE_CODE);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER)
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(RAN_VENDOR)
                + GuiStringConstants.COMMA + GuiStringConstants.THREE_G + GuiStringConstants.DASH
                + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE
                + GuiStringConstants.ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));
    }

    /*
     * 4.6.33   13B_WCDMA_CFA_HFA_4.6.33: WCDMA_CFA: Network Cause Code Analysis by Controller - Drill to Detailed Event Analysis from Grid
     *          Action 5
     */
    @Test
    public void networkTabControllerWcdmaCallFailureCauseCodeAnalysisDrillOnSubCauseCode_5_5_23b()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        int rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE_ID,
                reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, CAUSE_CODE);

        rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(SUB_CAUSE_CODE_ID,
                reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE_ID));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, FAILURES);

        final String causeCodeAndSubCauseCode = CAUSE_CODE + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID) + GuiStringConstants.DASH
                + SUB_CAUSE_CODE + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE_ID) + GuiStringConstants.DASH;

        final String expectedWindowTitle = causeCodeAndSubCauseCode
                + reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE
                + GuiStringConstants.DETAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + defaultWcdmaCallFailureControllerSubCauseWindowColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureControllerSubCauseWindowColumns));

    }

    /*
     * 4.6.34   13B_WCDMA_CFA_HFA_4.6.34: WCDMA_CFA: Network Cause Code Analysis by Controller Group - Drill to Detailed Event Analysis from Chart
     */

    @Test
    public void networkTabControllerWcdmaCallFailureCauseCodeAnalysisChartView_5_5_24() throws InterruptedException,
            PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkSubCauseCodeDetailedAnalysis.openChartViewUsingDisplayedNames(Arrays
                .asList(GuiStringConstants.SELECT_ALL));
        selenium.waitForPageLoadingToComplete();

        wcdmaNetworkSubCauseCodeDetailedAnalysis.drilldownOnHeaderPortion(reservedDataHelperReplacement
                .getReservedData(CAUSE_CODE));

        wcdmaNetworkSubCauseCodeDetailedAnalysis.closeWindow();

        wcdmaNetworkSubCauseCodeDetailedAnalysis.drilldownOnHeaderPortion(reservedDataHelperReplacement
                .getReservedData(SUB_CAUSE_CODE));

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER)
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(RAN_VENDOR)
                + GuiStringConstants.COMMA + GuiStringConstants.THREE_G + GuiStringConstants.DASH
                + GuiStringConstants.CONTROLLER + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE
                + GuiStringConstants.DETAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkSubCauseCodeDetailedAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + defaultWcdmaCallFailureControllerSubCauseWindowColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureControllerSubCauseWindowColumns));

    }

    /* WCDMA_CFA: Network Cause Code Analysis by Controller - Drill to Sub Cause Code widget from cause code Chart view*/
    @Test
    public void networkTabControllerWcdmaCallFailureSubCauseCodeWidget_5_5_24a() throws InterruptedException,
            PopUpException, NoDataException {
      String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
      //launch Window
      workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);
        wcdmaNetworkSubCauseCodeDetailedAnalysis.openChartViewUsingDisplayedNames(Arrays
                .asList(GuiStringConstants.SELECT_ALL));
        selenium.mouseOver(WcdmaStingConstantsNewUI.WCDMA_Sub_Cause_Code_Button);
        selenium.click(WcdmaStingConstantsNewUI.WCDMA_Sub_Cause_Code_Button);
        wcdmaNetworkSubCauseCodeDetailedAnalysis.closeWindow();
        final String expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE
                     + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODES_TABLE;
             assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
             List<String> actualWindowHeaders = networkControllerSubCauseCodeWidget.getTableHeaders();
             logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                     + defaultSubCauseCodeWidgetColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                     + actualWindowHeaders);
             assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                     actualWindowHeaders.equals(defaultSubCauseCodeWidgetColumns));
              int rowIndex = networkControllerSubCauseCodeWidget.getRowNumberWithMatchingValueForGivenColumn(SUB_CAUSE_CODE_ID,
                     reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE_ID));
             final Map<String, String> result = networkControllerSubCauseCodeWidget.getAllDataAtTableRow(rowIndex);
             checkMultipleDataEntriesOnSameRow(result, SUB_CAUSE_CODE);
             networkControllerSubCauseCodeWidget.closeWindow();
    }


    /*
     * 4.6.15   13B_WCDMA_CFA_HFA_4.6.15: WCDMA_CFA: From Total RAB Failures, verify drilldown to Call Setup Failures (RAB Type)
     */
    @Test
    public void networkTabControllerNetworkEventAnalysisRabTypeTotalRabFailure_5_5_25() throws InterruptedException,
            PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByRabType);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.DASH
                + GuiStringConstants.RAB_DESCRIPTION + GuiStringConstants.SPACE + GuiStringConstants.ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + GuiStringConstants.TOTAL_RAB_FAILURES
                + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN + reservedDataHelperReplacement.getReservedData(DRILL_ON) + GuiStringConstants.BRACKET_CLOSE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        assertColumnsInWindow(wcdmaNetworkRncRabTypeAnalysis);
    }

    /*
     * 4.6.14   13B_WCDMA_CFA_HFA_4.6.14: WCDMA_CFA: From Total RAB Failures, verify drilldown to Call Drops (RAB Type)
     */
    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypeTotalRabFailure_5_5_26()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByRabType);
        selenium.waitForPageLoadingToComplete();

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.DASH
                + GuiStringConstants.RAB_DESCRIPTION + GuiStringConstants.SPACE + GuiStringConstants.ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + GuiStringConstants.TOTAL_RAB_FAILURES
                + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN + reservedDataHelperReplacement.getReservedData(DRILL_ON) + GuiStringConstants.BRACKET_CLOSE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        assertColumnsInWindow(wcdmaNetworkRncRabTypeAnalysis);

    }

    /*
     * 4.6.13   13B_WCDMA_CFA_HFA_4.6.13: WCDMA_CFA: From Total RAB Failures, verify drilldown to Call Drops (Disconnection Code)
     */
    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypeTotalRabFailureDisconnectionCode_5_5_27()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();

        assertTitleAndColumnsInWindow(wcdmaNetworkRncDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), CONTROLLER,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.TOTAL_RAB_FAILURES);

    }
    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypeTotalRabFailureDisconnectionCode_5_5_27a()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        selenium.click(WcdmaStingConstantsNewUI.DrillByFailures);
        waitForPageLoadingToComplete();
        selenium.click(WcdmaStingConstantsNewUI.DrillByDisconnCode);
        waitForPageLoadingToComplete();
        assertWindowTitle(wcdmaNetworkRncDisconnectionCodeAnalysisToggleToGrid,reservedDataHelperReplacement.getReservedData(CONTROLLER));
        selenium.mouseOver(WcdmaStingConstantsNewUI.ToggleToGrid);
        selenium.click(WcdmaStingConstantsNewUI.ToggleToGrid);
        waitForPageLoadingToComplete();
        assertWindowTitle(wcdmaNetworkRncDisconnectionCodeAnalysisToggleToGrid,reservedDataHelperReplacement.getReservedData(CONTROLLER));

        selenium.mouseOver(WcdmaStingConstantsNewUI.ExportToCsv);
        selenium.click(WcdmaStingConstantsNewUI.ExportToCsv);
        waitForPageLoadingToComplete();
        }

    /*
     * 4.6.18   13B_WCDMA_CFA_HFA_4.6.18: WCDMA_CFA: From Circuit Switched RAB Failures, verify drilldown to Call Drops (Disconnection Code)
     */

    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypeCircuitSwitchedRabFailureDisconnectionCode_5_5_28()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkRncDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), CONTROLLER,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.CIRCUIT_SWITCHED_RAB_FAILURES);
    }

    /*
     * 4.6.21   13B_WCDMA_CFA_HFA_4.6.21: WCDMA_CFA: From Packet Switched RAB Failures, verify drilldown to Call Drops (Disconnection Code)
     */
    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypePacketSwitchedRabFailureDisconnectionCode_5_5_29()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkRncDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), CONTROLLER,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.PACKET_SWITCHED_RAB_FAILURES);
    }

    /*
     * 4.6.24     13B_WCDMA_CFA_HFA_4.6.24: WCDMA_CFA: From Multi RAB Failures, verify drilldown to Call Drops (Disconnection Code)
     *
     */

    @Test
    public void networkTabControllerNetworkEventAnalysisCallDropsRabTypeMultiRabFailureDisconnectionCode_5_5_30()
            throws InterruptedException, PopUpException, NoDataException {
        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.CONTROLLER, WcdmaStingConstantsNewUI.RNC09,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkRncDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(CONTROLLER), CONTROLLER,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.MULTI_RAB_FAILURES);
    }

    /*
     * 4.6.37   13B_WCDMA_CFA_HFA_4.6.37: WCDMA_CFA: Network Cause Code Analysis by Access Area - Drill to Detailed Event Analysis from Grid
     */
    @Test
    public void networkTabControllerAccessAreaWcdmaCauseCodeAnalyis_5_5_31() throws InterruptedException,
            PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        final String expectedWindowTitle = accessAreaValue + GuiStringConstants.COMMA + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE
                + WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
    }

    /*
     * 4.6.37   13B_WCDMA_CFA_HFA_4.6.37: WCDMA_CFA: Network Cause Code Analysis by Access Area - Drill to Detailed Event Analysis from Grid
     *          Action 4
     */
    @Test
    public void networkTabControllerAccessAreaWcdmaCauseCodeAnalyis_5_5_31a() throws InterruptedException,
            PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        String causeCode = reservedDataHelperReplacement.getReservedData(CAUSE_CODE);

        int rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE_ID,
                reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, CAUSE_CODE);

        final String expectedWindowTitle = causeCode + GuiStringConstants.DASH + accessAreaValue
                + GuiStringConstants.COMMA + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE + GuiStringConstants.ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescWindowColumns));
    }

    /*
     * 4.6.37   13B_WCDMA_CFA_HFA_4.6.37: WCDMA_CFA: Network Cause Code Analysis by Access Area - Drill to Detailed Event Analysis from Grid
     *          Action 5
     */
    @Test
    public void networkTabControllerAccessAreaWcdmaCauseCodeAnalyis_5_5_31b() throws InterruptedException,
            PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkCauseCodeAnalysis.openGridView();

        int rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(CAUSE_CODE_ID,
                reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, CAUSE_CODE);

        rowIndex = wcdmaNetworkCauseCodeAnalysis.getRowNumberWithMatchingValueForGivenColumn(SUB_CAUSE_CODE,
                reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE));
        wcdmaNetworkCauseCodeAnalysis.clickTableCell(rowIndex, FAILURES);

        //need to raise jira on this Sub-Cause Code, shouldn't have '-'
        final String causeCodeAndSubCauseCode = CAUSE_CODE + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(CAUSE_CODE_ID) + GuiStringConstants.DASH
                + "Sub-Cause Code" + GuiStringConstants.DASH
                + reservedDataHelperReplacement.getReservedData(SUB_CAUSE_CODE_ID) + GuiStringConstants.DASH;

        final String expectedWindowTitle = causeCodeAndSubCauseCode + accessAreaValue + GuiStringConstants.COMMA
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(CONTROLLER)
                + GuiStringConstants.COMMA + reservedDataHelperReplacement.getReservedData(RAN_VENDOR)
                + GuiStringConstants.COMMA + GuiStringConstants.THREE_G + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE
                + GuiStringConstants.SPACE + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE
                + GuiStringConstants.DETAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkCauseCodeAnalysis.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + defaultWcdmaCallFailureControllerSubCauseWindowColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureControllerSubCauseWindowColumns));
    }

    /*
     * 4.6.36   13B_WCDMA_CFA_HFA_4.6.36: WCDMA_CFA: Network Cause Code Analysis by Access Area - Drill to Detailed Event Analysis from Chart
     */
    @Test
    public void networkTabControllerAccessAreaWcdmaCauseCodeAnalyisChartView_5_5_32() throws InterruptedException,
            PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(DRILL_ON);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.CAUSE_CODE_ANALYSIS);

        wcdmaNetworkAccessAreaSubCauseCode.openChartViewUsingDisplayedNames(Arrays
                .asList(GuiStringConstants.SELECT_ALL));
        selenium.waitForPageLoadingToComplete();

        wcdmaNetworkAccessAreaSubCauseCode.drilldownOnHeaderPortion(reservedDataHelperReplacement
                .getReservedData(CAUSE_CODE));
        wcdmaNetworkAccessAreaSubCauseCode.closeWindow();
        wcdmaNetworkAccessAreaSubCauseCode.drilldownOnHeaderPortion(reservedDataHelperReplacement
                .getReservedData(SUB_CAUSE_CODE));

        final String expectedWindowTitle = accessAreaValue + GuiStringConstants.COMMA + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(CONTROLLER) + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE
                + GuiStringConstants.SUB_CAUSE_CODE + GuiStringConstants.SPACE
                + GuiStringConstants.DETAILED_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = wcdmaNetworkAccessAreaSubCauseCode.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + defaultWcdmaCallFailureControllerSubCauseWindowColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultWcdmaCallFailureControllerSubCauseWindowColumns));
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisRabTypeTotalRabFailure_5_5_33() throws InterruptedException,
            PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_SETUP_FAILURES);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByRabType);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaRabTypeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.RAB_DESCRIPTION, reservedDataHelperReplacement.getReservedData(DRILL_ON_2),
                GuiStringConstants.TOTAL_RAB_FAILURES);
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisCallDropRabTypeTotalRabFailure_5_5_34()
            throws InterruptedException, PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByRabType);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaRabTypeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.RAB_DESCRIPTION, reservedDataHelperReplacement.getReservedData(DRILL_ON_2),
                GuiStringConstants.TOTAL_RAB_FAILURES);
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisCallDropRabTypeTotalRabFailureDisconnectionCode_5_5_35()
            throws InterruptedException, PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(TOTAL_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.TOTAL_RAB_FAILURES);
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisCallDropRabTypeCircuitSwitchedRABFailuresDisconnectionCode_5_5_36()
            throws InterruptedException, PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(CIRCUIT_SWITCHED_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.CIRCUIT_SWITCHED_RAB_FAILURES);
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisCallDropRabTypePacketSwitchedRABFailuresDisconnectionCode_5_5_37()
            throws InterruptedException, PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(PACKET_SWITCHED_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.PACKET_SWITCHED_RAB_FAILURES);
    }

    @Test
    public void networkTabAccessAreaNetworkEventAnalysisCallDropRabTypeMultiRABFailuresDisconnectionCode_5_5_38()
            throws InterruptedException, PopUpException, NoDataException {
        final String accessAreaValue = reservedDataHelperReplacement.getReservedData(ACCESS_AREA);

        String timeRange = CommonUtils.getCustomTimeRange(TableNameConstants.EVENT_E_RAN_CFA_ERR_RAW_TIMERANGE);
        //launch Window
        workspaceRC.launchWindowWithCustomTime(timeRange, SeleniumConstants.ACCESS_AREA, accessAreaValue,
                WcdmaStingConstantsNewUI.Call_Failure_WCDMA, WcdmaStingConstantsNewUI.Network_Event_Analysis);

        workspaceRC.selectRabTypeValueInCallfailureAnalysisWindow(MULTI_RAB_FAILURES, CALL_DROPS);

        drillDownOnFailureAndPieChart(networkEventAnalysisWindow, WcdmaStingConstantsNewUI.DrillByDisconnectionCode);
        selenium.waitForPageLoadingToComplete();
        assertTitleAndColumnsInWindow(wcdmaNetworkAccessAreaDisconnectionCodeAnalysis,
                reservedDataHelperReplacement.getReservedData(ACCESS_AREA), ACCESS_AREA,
                GuiStringConstants.DISCONNECTION_TYPE, reservedDataHelperReplacement.getReservedData(DRILL_ON),
                GuiStringConstants.MULTI_RAB_FAILURES);
    }

    /**
     * @param firstCommonWindow
     * @param expectedColumnsHeaders
     * @throws InterruptedException
     * @throws PopUpException
     * @throws NoDataException
     */
    private void networkEventAnalysisForControllerDrillByAccessAreaFromFailedEventAnalysis(
            final CommonWindow firstCommonWindow, final CommonWindow secondCommowWindow,
            final List<String> expectedColumnsHeaders, final boolean drillDownOnDetailedEA)
            throws InterruptedException, PopUpException, NoDataException {

        /* changed because of UI changes
         * final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(ACCESS_AREA) + DASH
                  + reservedDataHelperReplacement.getReservedData(CONTROLLER) + DASH + VENDOR_VALUE_DASH + DASH
                  + ACCESS_AREA + " " + GuiStringConstants.EVENT_ANALYSIS;
          */
        final String expectedWindowTitle = GuiStringConstants.ACCESS_AREA_EVENT_ANALYSIS;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        List<String> actualWindowHeaders = firstCommonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallSetupFailureAccessAreaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(expectedColumnsHeaders));

        if (isDataIntegrityFlagOn()) {
            doDataIntegrityOnWcdmaCallFailureAnalysisWindow(firstCommonWindow, secondCommowWindow, new String[] {
                    RAN_VENDOR, CONTROLLER, ACCESS_AREA, FAILURES, RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS }, drillDownOnDetailedEA);
        }
    }

    /**
     * @param firstCommonWindow : bean of first window before drill down
     * @param secondCommonWindow : bean of second window
     * @param columnNamesArray : list of first window columns
     * @throws NoDataException
     * @throws PopUpException
     */
    private void doDataIntegrityOnWcdmaCallFailureAnalysisWindow(final CommonWindow firstCommonWindow,
            final CommonWindow secondCommonWindow, final String[] columnNamesArray, final boolean drillDownOnDetailedEA)
            throws NoDataException, PopUpException {
        int rowIndex = firstCommonWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        final Map<String, String> result = firstCommonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnNamesArray);

        firstCommonWindow.clickTableCell(rowIndex, FAILURES);
        if (drillDownOnDetailedEA) {
            selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        }

        assertDataIntegrityOnWindow(secondCommonWindow, EVENT_TYPE, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
                PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
                SEVERITY_INDICATOR);
    }

    /**
     * @param commonWindow
     * @param expectedColumnHeaders
     * @throws NoDataException
     * @throws PopUpException
     * @throws InterruptedException
     */
    private void networkEventAnalysisForControllerDrillByTACFromFailedEventAnalysis(final CommonWindow commonWindow,
            final List<String> expectedColumnHeaders, String node) throws NoDataException, PopUpException,
            InterruptedException {

        node = (node == null ? "" : DASH + node);

        final String expectedWindowTitle = reservedDataHelperReplacement.getReservedData(TAC) + node + DASH
                + GuiStringConstants.TERMINAL_EVENT_ANALYSIS + DASH + GuiStringConstants.CALL_FAILURE_ANALYSIS + DASH
                + GuiStringConstants.WCDMA;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedColumnHeaders
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, expectedColumnHeaders.equals(actualWindowHeaders));

        if (isDataIntegrityFlagOn()) {
            doDataIntegrityOnWcdmaCallFailureAnalysisWindow(commonWindow, commonWindow, new String[] { MANUFACTURER,
                    MODEL, EVENT_TYPE, FAILURES, RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS }, false);
        }
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

    private void assertWindowTitleAndHearders(final String node, final CommonWindow commonWindow,
            final List<String> expectedHearderList) {
        final String expectedWindowTitle = node + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS
                + GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + expectedHearderList
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(expectedHearderList));
    }

    private void assertWindowTitle(final CommonWindow commonWindow, final String controller) {
        final String expectedWindowTitle = controller + GuiStringConstants.DASH + "Controller Disconnections" + GuiStringConstants.GREATER_THEN_SIGN + GuiStringConstants.TOTAL_RAB_FAILURES;

                assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
    }

    private void assertDataIntegrityFailedEventAanalysisWindow(final CommonWindow commonWindow,
            final String[] columnsDatatoCheck) throws NoDataException {
        final int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, columnsDatatoCheck);
    }

    private void assertWindowTitleAndHearders(final String rabType, final String controller,
            final CommonWindow commonWindow, final List<String> windowsHearderList) {
        final String expectedWindowTitle = controller + GuiStringConstants.COMMA
                + reservedDataHelperReplacement.getReservedData(RAN_VENDOR) + GuiStringConstants.COMMA
                + GuiStringConstants.THREE_G + GuiStringConstants.DASH + GuiStringConstants.CONTROLLER
                + GuiStringConstants.DASH + GuiStringConstants.NETWORK_EVENT_ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + rabType;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + windowsHearderList
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(windowsHearderList));
    }

    /**
     * Drill Down on Failure in Controller Failed Event Analysis window and assert Headers, columns and do data integrity
     * for drop calls
     * @param controller
     * @throws NoDataException
     * @throws PopUpException
     */
    protected void drillDownFailuresAndassertHeadersAndDoDataIntegrityForCallSetFailures(final String controller,
            final boolean drilldownOnDetailEventAnalysis) throws NoDataException, PopUpException {

        int rowIndex = networkEventAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        networkEventAnalysisWindow.clickTableCell(rowIndex, FAILURES);

        if (drilldownOnDetailEventAnalysis) {
            selenium.click(WcdmaStingConstantsNewUI.DrillByDetailedEventAnalysis);
        }

        final String expectedWindowTitle = controller + GuiStringConstants.DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_CALL_FAILURE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = networkDetailedEventAnalysisWcdmaCfaBscCallSetup.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));

        if (isDataIntegrityFlagOn()) {
            doDataIntegrityForGivenIMSIInFailedEA(networkDetailedEventAnalysisWcdmaCfaBscCallSetup);
        }
    }

    /**
     * @param rabType
     * @param accessAreaValue
     * @param commonWindow
     * @param windowsHeaderList
     */
    private void assertWindowTitleAndHeardersForAccessArea(final String rabType, final String accessAreaValue,
            final CommonWindow commonWindow, final List<String> windowsHeaderList) {
        final String expectedWindowTitle = accessAreaValue + GuiStringConstants.COMMA + GuiStringConstants.COMMA
                + GuiStringConstants.SMARTONE_R_RNC09_RNC09_ERICSSON_3G + GuiStringConstants.DASH
                + GuiStringConstants.ACCESS_AREA + GuiStringConstants.DASH + GuiStringConstants.EVENT_ANALYSIS
                + GuiStringConstants.GREATER_THEN_SIGN + rabType;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + windowsHeaderList
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(windowsHeaderList));
    }

    /**
     * @param commonWindow
     * @throws NoDataException
     */
    private void doDataIntegrityOnGroupEventAnalysis(final CommonWindow commonWindow) throws NoDataException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));
        Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, FAILURES, IMPACTED_SUBSCRIBERS);

        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE_2));
        result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(FAILURES, reservedDataHelperReplacement.getReservedData(FAILURES_2), result.get(FAILURES));

        checkDataIntegrity(IMPACTED_SUBSCRIBERS, reservedDataHelperReplacement.getReservedData(IMPACTED_SUBSCRIBERS_2),
                result.get(IMPACTED_SUBSCRIBERS));
    }

    /**
     * @param node : like Access Area or Controller
     * @param commonWindow
     * @param expectedColumns
     */
    private void assertWindowTitleAndColumnsForGroupEventAnalysisWindow(final String node,
            final CommonWindow commonWindow, final List<String> expectedColumns) {

        final String expectedWindowTitle = node + DASH + GuiStringConstants.CONTROLLER_GROUP + DASH
                + GuiStringConstants.EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerGroupAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(expectedColumns));
    }

    /**
     * @param commonWindow
     * @throws NoDataException
     */
    private void doDataIntegrityForGivenIMSIInFailedEA(final CommonWindow commonWindow) throws NoDataException {
        int rowIndex;
        rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = commonWindow.getAllDataAtTableRow(rowIndex);
        checkMultipleDataEntriesOnSameRow(result, TAC, TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA,
                EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
                LAC, RAC, SEVERITY_INDICATOR);
    }

    /**
     * @param controllerGroupValue
     * @param commonWindow
     * @param expectedColumns
     */
    private void assertWindowsTitleAndColumnsForGroupFailedEA(final String controllerGroupValue,
            final CommonWindow commonWindow, final List<String> expectedColumns) {
        // need to change header once the bug is fixed (http://jira-oss.lmera.ericsson.se/browse/EQEV-7123)
        final String expectedWindowTitle = controllerGroupValue + DASH + GuiStringConstants.CONTROLLER_GROUP + DASH
                + GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + GuiStringConstants.WCDMA_CALL_FAILURE;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsFailedEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(expectedColumns));
    }

    /**
     * @param commonWindow
     * @param nodeHeading
     * @param node
     * @param type
     * @param rab
     * @param rabTitleConstant
     */
    private void assertTitleAndColumnsInWindow(final CommonWindow commonWindow, final String nodeHeading,
            final String node, final String type, final String rab, final String rabTitleConstant) {
        final String expectedWindowTitle = nodeHeading + GuiStringConstants.DASH
                + GuiStringConstants.WCDMA_CALL_FAILURE + GuiStringConstants.SPACE + GuiStringConstants.ANALYSIS
                + GuiStringConstants.DASH + node + GuiStringConstants.DASH + type
                + GuiStringConstants.GREATER_THEN_SIGN + rabTitleConstant
                + GuiStringConstants.SPACE + GuiStringConstants.BRACKET_OPEN + rab + GuiStringConstants.BRACKET_CLOSE;

        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = commonWindow.getTableHeaders();

        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisTerminalColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCallFailureAnalysisTerminalColumns));
    }
    private void assertColumnsInWindow(final CommonWindow commonWindow){
    final List<String> actualWindowHeaders = commonWindow.getTableHeaders();

    logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCallFailureAnalysisTerminalColumns
            + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
            actualWindowHeaders.equals(defaultCallFailureAnalysisTerminalColumns));
    }
    /**
     * @param commonWindow
     * @param drillby
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownOnFailureAndPieChart(final CommonWindow commonWindow, final String drillby)
            throws NoDataException, PopUpException {
        int rowIndex = commonWindow.getRowNumberWithMatchingValueForGivenColumn(EVENT_TYPE,
                reservedDataHelperReplacement.getReservedData(EVENT_TYPE));

        commonWindow.clickTableCell(rowIndex, FAILURES);
        selenium.click(drillby);
        commonWindow.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON));
        //workaround to Drill down on second pie chart
        commonWindow.closeWindow();
        if (drillby.equals(WcdmaStingConstantsNewUI.DrillByRabType)) {
            commonWindow.closeWindow();
            commonWindow.drilldownOnHeaderPortion(reservedDataHelperReplacement.getReservedData(DRILL_ON_2));
        }
    }

    /**
     * check if Data integrity flag is True
     * @return
     */
    private boolean isDataIntegrityFlagOn() {
        return dataIntegrityFlag.equals(TRUE);
    }

}
