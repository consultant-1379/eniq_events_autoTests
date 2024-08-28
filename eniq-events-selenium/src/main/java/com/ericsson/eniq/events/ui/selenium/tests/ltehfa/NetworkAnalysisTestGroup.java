package com.ericsson.eniq.events.ui.selenium.tests.ltehfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class NetworkAnalysisTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    private WorkspaceRC workspacerc;

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    @Autowired
    @Qualifier("networkEventAnalysisForLTEHFA")
    private CommonWindow networkEventAnalysis;

    @Autowired
    @Qualifier("networkCauseCodeAnalysisForLTEHFA")
    private CommonWindow networkCauseCodeAnalysis;

    @Autowired
    @Qualifier("networkEventVolumeAnalysisForLTEHFA")
    private CommonWindow networkEventVolumeAnalysis;

    @Autowired
    @Qualifier("networkEventVolAnalysisForLTEHFA")
    private CommonWindow networkEventVolAnalysis;

    @Autowired
    @Qualifier("networkQCIAnalysisForLTEHFA")
    private CommonWindow networkQCIAnalysis;

    final List<String> defaultHeadersOnAccessAreaHandoverStageEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.HANDOVER_STAGE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnAccessAreaEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnControllerHandoverStageEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.HANDOVER_STAGE, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnControllerEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CAUSE_CODE, GuiStringConstants.ENODEB, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.VENDOR, GuiStringConstants.NUMBER_OF_ERABS));

    final List<String> optionalHeadersOfPrepX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_TYPE, GuiStringConstants.RANDOM_ACCESS_TYPE,
            GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfPrepX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT, GuiStringConstants.HO_TYPE,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecX2InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.PACKET_FORWARD, GuiStringConstants.HO_TYPE));

    final List<String> optionalHeadersOfExecX2OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX, GuiStringConstants.PACKET_FORWARD,
            GuiStringConstants.HO_TYPE));

    final List<String> optionalHeadersOfPrepS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_TYPE, GuiStringConstants.RANDOM_ACCESS_TYPE,
            GuiStringConstants.SUBSCRIBER_PROFILE_ID,
            //GuiStringConstants.PREP_IN_RESULT_UE_CTXT,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfPrepS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT, GuiStringConstants.SRVCC_TYPE,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA));

    final List<String> optionalHeadersOfExecS1InIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SOURCE_TYPE));

    final List<String> optionalHeadersOfExecS1OutIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    // GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.HO_ATTEMPT
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX));

    final List<String> optionalHeadersOfHandoverFailureAnalysisERABWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.SOURCE_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_CONTROLLER, GuiStringConstants.TARGET_ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR));

    final List<String> optionalHeadersToUntickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.PREP_IN_RESULT_UE_CTXT, GuiStringConstants.Duration));

    @Override
    @Before
    public void setUp() {
        super.setUp();
        workspacerc.checkAndOpenSideLaunchBar();
        pause(2000);
        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: 105 65-0582/00233, 105 65-0582/00234 Test Case: 7.1 Description: It shall be possible to view a summary of Handover Failure events
     * for a nominated eCell over a predefined period of time from 5 mins to 1 week, the default of which is 30 minutes. From the summary window it
     * shall be possible to drill-down into failed events.
     */
    @Test
    public void eCellHandoverFailureAnalysis_7_1() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);
        Thread.sleep(5000);
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        workspacerc.enterDimensionValue(eCell);
        workspacerc.selectWindowType("Network Event Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        selenium.waitForPageLoadingToComplete();
        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkEventAnalysis.getTimeRange()
                .equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Access Area will hold Controller, Vendor information as well, we have to segregate that
        String eCellArray[] = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        final List<String> eCellList = new ArrayList<String>();
        eCellList.add(eCellArray[0]);
        validateDataOfNetworkAnalysisWindowForAllTimeRanges(networkEventAnalysis, eCellList, false, true, GuiStringConstants.ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0582/00634 Test Case: 7.2 Description: It shall be possible to view a summary of Handover Failure events for a nominated
     * eCell Group over a predefined period of time from 5 mins to 1 week, the default of which is 30 minutes. From The summary window it shall be
     * possible to drill-down into failed events.
     */
    @Test
    public void eCellGroupHandoverFailureAnalysis_7_2() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(10000);

        selenium.waitForPageLoadingToComplete();

        final List<String> eCellGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",")));

        validateDataOfNetworkAnalysisWindowForAllTimeRanges(networkEventAnalysis, eCellGroupList, true, true, GuiStringConstants.ACCESS_AREA);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.3 Description: It shall be possible to view a summary of Handover Failure events for a nominated
     * eNodeB over a predefined period of time from 5 mins to 1 week, the default of which is 30 minutes. From the summary window it shall be possible
     * to drill-down into failed events.
     */
    @Test
    public void eNodeBHandoverFailureAnalysis_7_3() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkEventAnalysis.getTimeRange()
                .equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");

        // Validate Data and Drill Down on required Columns
        final List<String> eNodeBList = new ArrayList<String>();
        eNodeBList.add(eNodeBArray[0]);
        validateDataOfNetworkAnalysisWindowForAllTimeRanges(networkEventAnalysis, eNodeBList, false, true, GuiStringConstants.CONTROLLER);

    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.4 Description: It shall be possible to view a summary of Handover Failure events for a nominated
     * eNodeB Group over a predefined period of time from 5 mins to 1 week, the default of which is 30 minutes. From the summary window it shall be
     * possible to drill-down into failed events.
     */
    @Test
    public void eNodeBGroupHandoverFailureAnalysis_7_4() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Handover Failure (4G)");
        Thread.sleep(10000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        final List<String> eNodeBGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",")));

        validateDataOfNetworkAnalysisWindowForAllTimeRanges(networkEventAnalysis, eNodeBGroupList, true, true, GuiStringConstants.CONTROLLER);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.11 Description: It shall be possible to list the cause codes and the number of failures for a
     * specified Controller.
     */
    @Test
    public void eNodeBCauseCodeAnalysis_7_11() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBArray, false, "", false);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.12 Description: It shall be possible to list the cause codes and the number of handover failures
     * for a specified Controller group.
     */
    @Test
    public void eNodeBGroupCauseCodeAnalysis_7_12() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);

        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBArray, true, eNodeBGroup, false);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.13 Description: It shall be possible to list the cause codes and the number of handover failures
     * for a specified Access Area.
     */
    @Test
    public void eCellCauseCodeAnalysis_7_13() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellArray, false, "", false);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.14 Description: It shall be possible to list the cause codes and the number of handover failures
     * for a specified Access Area group.
     */
    @Test
    public void eCellGroupCauseCodeAnalysis_7_14() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellArray, true, eCellGroup, false);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.15 Description: It shall be possible to drill down from cause code summary view to the handover
     * failure type details of specified Controller.
     */
    @Test
    public void eNodeBCauseCodeDrillDown_7_15() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBArray, false, "", true);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.16 Description: It shall be possible to drill down from cause code summary view to the handover
     * failure type details of specified Controller group.
     */
    @Test
    public void eNodeBGroupCauseCodeDrillDown_7_16() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBArray, true, eNodeBGroup, true);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.17 Description: It shall be possible to drill down from cause code summary view to the handover
     * failure type details of specified Access Area.
     */
    @Test
    public void eCellCauseCodeDrillDown_7_17() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellArray, false, "", true);
    }

    /**
     * Requirement: 112 65-0582/00634 Test Case: 7.18 Description: It shall be possible to drill down from cause code summary view to the handover
     * failure type details of specified Access Area Group.
     */
    @Test
    public void eCellGroupCauseCodeDrillDown_7_18() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellArray, true, eCellGroup, true);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.1 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified eNodeB. Over a number of intervals within the specified query time range. It shall be possible to view individual
     * sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume by failure
     * types.
     */
    @Test
    public void eNodeBEventVolumeAnalysis_8_1() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");
        List<String> eNodeBList = new ArrayList<String>();
        eNodeBList.add(eNodeBArray[0]);

        // Validate Data and Drill Down on required Columns
        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.2 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified eNodeB Group. Over a number of intervals within the specified query time range. It shall be possible to view
     * individual sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume
     * by failure types.
     */
    @Test
    public void eNodeBGroupEventVolumeAnalysis_8_2() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        final List<String> eNodeBGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",")));

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBGroupList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.3 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified eCell. Over a number of intervals within the specified query time range. It shall be possible to view individual
     * sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume by failure
     * types .
     */
    @Test
    public void AccessAreaEventVolumeAnalysis_8_3() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");
        List<String> eCellList = new ArrayList<String>();
        eCellList.add(eCellArray[0]);

        // Validate Data and Drill Down on required Columns
        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.4 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified eCell Group. Over a number of intervals within the specified query time range. It shall be possible to view
     * individual sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume
     * by failure types.
     */
    @Test
    public void AccessAreaGroupEventVolumeAnalysis_8_4() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        final List<String> eCellGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",")));

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellGroupList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.6 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified Tracking Area Group. Over a number of intervals within the specified query time range. It shall be possible to view
     * individual sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume
     * by failure types.
     */
    @Test
    public void NetworkEventVolumeAnalysis_8_6() throws Exception {

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Volume", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        validateEventVolumeAnalysisWindow(networkEventVolAnalysis, "", null, true);
    }

    /**
     * Requirement: Test Case: 7.30 and 7.21 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call
     * drop failures for specified Controller.
     */
    @Test
    public void ControllerQCIStatistics_7_30_and_7_31() throws Exception {

        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(controller);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 30 minutes
        assertTrue("ERROR - Default TimeRange is not set to 30 minutes",
                networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String controllerArray[] = new String[0];
        controllerArray = controller.split(",", 2);

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.SOURCE_CONTROLLER, controllerArray, true);
    }

    /**
     * Requirement: Test Case: 7.32 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call drop
     * failures for specified Controller group. Also from the No of Failures it shall be possible to drill-down to event analysis window (summary by
     * event types)..
     */
    @Test
    public void eNodeBGroupQCIstatistics_7_32() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 30 minutes
        assertTrue("ERROR - Default TimeRange is not set to 30 minutes",
                networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.SOURCE_CONTROLLER, eNodeBArray, true);
    }

    /**
     * Requirement: Test Case: 7.33 and 7.34 Description: It shall be possible to drill-down to event analysis window (summary by event types) from
     * Access area summary window.
     */
    @Test
    public void AccessAreaQCIStatistics_7_33_and_7_34() throws Exception {

        final String accessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        Thread.sleep(5000);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(accessArea);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String accessAreaArray[] = new String[1];
        accessAreaArray = accessArea.split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, accessAreaArray, true);
    }

    /**
     * Requirement: Test Case: 7.35 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call drop
     * failures for specified Access Area Group. Also from the No of Failures it shall be possible to drill-down to event analysis window (summary by
     * event types).
     */
    @Test
    public void AccessAreaGroupQCIstatistics_7_35() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);

        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Handover Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.SOURCE_ACCESS_AREA, eCellArray, true);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void validateDataOfNetworkAnalysisWindowForAllTimeRanges(final CommonWindow commonWindow, List<String> networkGroupList, boolean isGroup,
                                                                     boolean drillDownFailures, String networkEntityType) throws NoDataException,
            PopUpException, ParseException, IOException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        System.out.println("In validateDataOfNetworkAnalysisWindowForAllTimeRanges");
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

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;
            try {
                //Thread.sleep(5000);
            } catch (Exception e) {

            }
            networkEventAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));
            try {
                // Thread.sleep(5000);
            } catch (Exception e) {

            }
            System.out.println("Before Handover Stage1");
            List<String> listOfHandoverStages = commonWindow.getAllTableDataAtColumn("Handover Stage");

            for (String handoverStage : listOfHandoverStages) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue("Handover Stage", handoverStage);
                System.out.println("Before Handover Stage2");
                commonWindow.clickTableCell(row, "Handover Stage");
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {

                }
                waitForPageLoadingToComplete();

                if (!drillDownFailures) {
                    continue;
                }
                try {
                    //Thread.sleep(5000);
                } catch (Exception e) {

                }
                System.out.println("Before drillDown");
                drillDownFailuresOnNetworkGroupEventAnalysisWindow(commonWindow, networkGroupList, networkEntityType, isGroup, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
                commonWindow.clickBackwardNavigation();
            }
        }
    }

    private void drillDownFailuresOnNetworkGroupEventAnalysisWindow(final CommonWindow window, List<String> networkGroupList,
                                                                    String networkEntityType, boolean isGroup, int timeRangeInMinutes)
            throws NoDataException, PopUpException, ParseException, IOException {

        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();

            checkInOptionalHeaderHFA(window, eventType);

            window.clickBackwardNavigation();
        }

    }

    private void checkInOptionalHeaderHFA(CommonWindow window, String eventType) {
        window.openTableHeaderMenu(0);
        pause(2000);
        try {
            window.uncheckOptionalHeaderCheckBoxes(optionalHeadersToUntickIfPresent, GuiStringConstants.EVENT_TYPE);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (eventType.equals(GuiStringConstants.PREP_X2_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepX2InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_X2_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepX2OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_X2_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecX2InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_X2_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecX2OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_S1_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepS1InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.PREP_S1_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfPrepS1OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_S1_IN)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecS1InIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else if (eventType.equals(GuiStringConstants.EXEC_S1_OUT)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfExecS1OutIMSIFailedEventAnalysisWindow, GuiStringConstants.EVENT_TYPE);
        } else {
            assertTrue("ERROR - Invalid Event Type : " + eventType, false);
            return;
        }

        try {
            waitForPageLoadingToComplete();
        } catch (PopUpException e) {
            e.printStackTrace();
        }

    }

    private void validateCauseCodeAnalysisWindow(final CommonWindow window, String causeCodeAnalysisType, String causeCodeAnalysisValue[],
                                                 boolean isGroup, String groupName, boolean drillDownRequired) throws NoDataException,
            PopUpException, ParseException, IOException {

        // Switch to Grid View
        window.openGridView();

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

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

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<Map<String, String>> listOfCauseCodeData = window.getAllTableData();

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (drillDownRequired) {
                drillDownOccurrencesCauseCodeAnalysisWindow(window, listOfCauseCodeData, isGroup, time.getMiniutes());
            }
        }

    }

    private void drillDownOccurrencesCauseCodeAnalysisWindow(final CommonWindow window, List<Map<String, String>> listOfCauseCodeData,
                                                             boolean isGroup, int timeRangeInMinutes) throws NoDataException, PopUpException,
            ParseException, IOException {
        int row = 0;
        //Cause code description column index
        int causeCodeColumn = 1;

        // Drill down for all event types
        for (final Map<String, String> uiCauseCodeMap : listOfCauseCodeData) {
            String eventType = uiCauseCodeMap.get(GuiStringConstants.EVENT_TYPE);
            row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            if (isGroup) {
                causeCodeColumn = 1;
            }
            String causeCodeDesc = window.getTableData(row, causeCodeColumn);

            // If EventType and CauseCode not found in same row then ignore processing it
            if (!causeCodeDesc.equals(uiCauseCodeMap.get(GuiStringConstants.CAUSE_CODE_DESCRIPTION))) {
                continue;
            }

            window.clickTableCell(row, GuiStringConstants.OCCURRENCES);
            waitForPageLoadingToComplete();

            checkInOptionalHeaderHFA(window, eventType);

            window.clickBackwardNavigation();

            row++;
        }
    }

    private void validateEventVolumeAnalysisWindow(final CommonWindow window, String eventAnalysisType, List<String> eventAnalysisValueList,
                                                   boolean allNetworkElements) throws NoDataException, PopUpException {

        // Switch to Grid View
        window.openGridView();

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

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

            if (time == TimeRange.FIVE_MINUTES || time == TimeRange.ONE_WEEK) {
                assertTrue("Limitation : Does not support for 5 mins and 1 week time ranges", true);
                continue;
            }

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));
            int noOfTimes = 0;

            if (time.getMiniutes() > 120) {
                noOfTimes = time.getMiniutes() / 15;
            } else {
                noOfTimes = time.getMiniutes() / 5;
            }

            for (int i = 0; i < noOfTimes; i++) {
                objDataIntegrityValidationUtil.loadEventVolumeData(reservedDataHelper, startDate);
                long timeInMins = startDate.getTime() + (5 * 60 * 1000);
                startDate.setTime(timeInMins);
            }
        }
    }

    private void validateQCIstatisticsWindow(final CommonWindow window, String neType, String neValue[], boolean drillDownRequired)
            throws NoDataException, PopUpException, ParseException, IOException {

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        objDataIntegrityValidationUtil.getERABfailureAnalysisPerQCIFromResvData(neType, neValue);

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

            // Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            if (drillDownRequired) {
                String qciValue = window.getTableData(0, 0);

                window.clickTableCell(0, GuiStringConstants.HANDOVER_STAGE);

                window.clickTableCell(0, GuiStringConstants.FAILURES);

            }
        }
    }

}