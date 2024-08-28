/* COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.ltecfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.SubStartMenu;
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
    @Qualifier("networkEventAnalysisForLTECFA")
    private CommonWindow networkEventAnalysis;

    @Autowired
    @Qualifier("networkCauseCodeAnalysisForLTECFA")
    private CommonWindow networkCauseCodeAnalysis;

    @Autowired
    @Qualifier("networkQCIAnalysisForLTECFA")
    private CommonWindow networkQCIAnalysis;

    @Autowired
    @Qualifier("networkEventVolumeAnalysisForLTECFA")
    private CommonWindow networkEventVolumeAnalysis;

    @Autowired
    @Qualifier("networkEventVolAnalysisForLTECFA")
    private CommonWindow networkEventVolAnalysis;

    final List<String> defaultHeadersOnAccessAreaEventAnalysisSummaryWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.CATEGORY, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnControllerEventAnalysisSummaryWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CONTROLLER, GuiStringConstants.CATEGORY, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnTrackingAreaEventAnalysisSummaryWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.TRACKING_AREA,
            GuiStringConstants.CATEGORY, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOfQCIFailuresWindow = new ArrayList<String>(Arrays.asList("QCI", "QCI Description", GuiStringConstants.CATEGORY,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.VENDOR, GuiStringConstants.NUMBER_OF_ERABS));

    final List<String> headersToTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.VENDOR,
            GuiStringConstants.NUMBER_OF_ERABS, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, GuiStringConstants.RAN_VENDOR, GuiStringConstants.GROUP_NAME, "Setup Request PCI",
            "Setup Request ARP", "Setup Result", "Setup Successful Access Type", "Failure 3GPP Cause", "Setup Attempted Access Type",
            "Setup Failure 3GPP Cause Group", "Access Area", "Terminal Model", "Event Type", "Setup Request QCI", "Setup Request PVI",
            GuiStringConstants.ERAB_DATA_LOST, GuiStringConstants.ERAB_RELEASE_SUCCESS));

    final List<String> ERABRealeaseHeadersToTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.VENDOR,
            GuiStringConstants.NUMBER_OF_ERABS, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, GuiStringConstants.RAN_VENDOR, GuiStringConstants.GROUP_NAME, "Setup Request PCI",
            "Setup Request ARP", "Setup Result", "Setup Successful Access Type", "Failure 3GPP Cause", "Setup Attempted Access Type",
            "Setup Failure 3GPP Cause Group", "Access Area", "Terminal Model", "Event Type", "Setup Request QCI", "Setup Request PVI",
            GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_GROUP,GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_CODE,
            GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_GROUP, GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_CODE,
            GuiStringConstants.ERAB_DATA_LOST, GuiStringConstants.ERAB_RELEASE_SUCCESS));

    final List<String> headersToUnTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC,GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_GROUP,
    GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_CODE, GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_GROUP, GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_CODE));

    final List<String> optionalHeadersOfRrcConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
    // eeirmen ***** temporarily added , GuiStringConstants.EVENT_TYPE
            GuiStringConstants.DURATION, GuiStringConstants.RRC_ESTABL_CAUSE, GuiStringConstants.GUMMEI_Type_Desc));//Adding new Column Gummei Type for 13B

    final List<String> optionalHeadersOfS1SigConnSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
    //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.RRC_ESTABL_CAUSE));

    final List<String> optionalHeadersOfInitialCtxtSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC,
            GuiStringConstants.MNC, GuiStringConstants.RAN_VENDOR, GuiStringConstants.DURATION, GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR,
            GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR));

    final List<String> optionalHeadersOfErabSetupOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR,
            GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR));

    final List<String> optionalHeadersOfUeCntxtReleaseOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            //GuiStringConstants.MCC, GuiStringConstants.MNC,
            GuiStringConstants.DURATION, GuiStringConstants.INTERNAL_RELEASE_CAUSE, GuiStringConstants.TRIGGERING_NODE,
            GuiStringConstants.ERAB_DATA_LOST_BITMAP, GuiStringConstants.ERAB_DATA_LOST, GuiStringConstants.ERAB_RELEASE_SUCCESS,
            GuiStringConstants.NUMBER_OF_FAILED_ERABS, GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, GuiStringConstants.TTI_Bundling_Mode_Desc));//Adding new column TTI_Bundling_Mode_Desc for 13B

    final List<String> optionalHeadersOfCallFailureAnalysisERABWindow = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.VENDOR, "Setup Attempted Access Type", "Setup Successful Access Type", "Setup Failure 3GPP Cause Group"));

    final List<String> optionalHeadersOfCallFailureAnalysisERABWindowForUEctxtRel = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.VENDOR));

    @Override
    @Before
    public void setUp() {
        super.setUp();
        workspacerc.checkAndOpenSideLaunchBar();
        pause(2000);
        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: 105 65-0582/00233, 105 65-0582/00234 Test Case: 7.1 Description: To verify that it shall be possible to view a summary of Call
     * Setup/call drop Failure events for a nominated eCell. From The summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void eCellCallFailureAnalysis_7_1() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();
        // Check if default timerange is set to 1 Hour

        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkEventAnalysis.getTimeRange()
                .equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Access Area will hold Controller, Vendor information as well, we have to segregate that from them
        String eCellArray[] = eCell.split(",");

        final List<String> eCellList = new ArrayList<String>();
        eCellList.add(eCellArray[0]);

        // Validate Data and Drill Down on required Columns
        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, eCellList, true, GuiStringConstants.ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0582/00235, 105 65-0582/00236 Test Case: 7.2 Description: It shall be possible to view a summary of Call Setup/Call Drop
     * Failure events for a nominated eNodeB. From the summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void eNodeBCallFailureAnalysis_7_2() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
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
        final List<String> eNodeBGroupList = new ArrayList<String>();
        eNodeBGroupList.add(eNodeBArray[0]);

        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, eNodeBGroupList, true, GuiStringConstants.CONTROLLER);
    }

    /**
     * Requirement: 105 65-0582/00243, 105 65-0582/00244 Test Case: 7.3 Description: It shall be possible to view a summary of Call Setup/Call drop
     * Failure events for a nominated eNodeB Group. From the summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void eNodeBGroupCallFailureAnalysis_7_3() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> eNodeBGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",")));

        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, eNodeBGroupList, true, GuiStringConstants.CONTROLLER);
        pause(5000);
    }

    /**
     * Requirement: 105 65-0582/00241, 105 65-0582/00242 Test Case: 7.4 Description: To verify that it shall be possible to view a summary of Call
     * Setup/call drop Failure events for a nominated eCell Group. From The summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void eCellGroupCallFailureAnalysis_7_4() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        selenium.waitForPageLoadingToComplete();

        final List<String> eCellGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",")));

        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, eCellGroupList, true, GuiStringConstants.ACCESS_AREA);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.5 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void eCellCauseCodeAnalysis_7_5() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.ACCESS_AREA, eCellArray, false, "", false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.6 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void eCellGroupCauseCodeAnalysis_7_6() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(10000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        Thread.sleep(5000);

        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.ACCESS_AREA, eCellArray, true, eCellGroup, false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.7 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void eNodeBCauseCodeAnalysis_7_7() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.CONTROLLER, eNodeBArray, false, "", false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.8 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void eNodeBGroupCauseCodeAnalysis_7_8() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.CONTROLLER, eNodeBArray, true, eNodeBGroup, false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.9 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void trackingAreaCauseCodeAnalysis_7_9() throws Exception {

        final String trackingArea = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(trackingArea);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = new String[1];
        trackingAreaArray[0] = trackingArea;
        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, false, "", false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.10 Description: To verify that it shall be possible to view a Network Cause Code
     * Analysis which are experiencing the call setup/call drop failures.
     */
    @Test
    public void trackingAreaGroupCauseCodeAnalysis_7_10() throws Exception {

        final String trackingAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA_Group);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(trackingAreaGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_DATA_LTE).split(",");
        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, true, trackingAreaGroup, false);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.11 Description: It shall be possible to view cause code detailed analysis for
     * eNodeB.
     */
    @Test
    public void eNodeBCauseCodeDetailAnalysis_7_11() throws Exception {

        final String eNodeB = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.CONTROLLER, eNodeBArray, false, "", true);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.12 Description: It shall be possible to view cause code detailed analysis for
     * eNodeB group.
     */
    @Test
    public void eNodeBGroupCauseCodeDetailAnalysis_7_12() throws Exception {

        final String eNodeBGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.CONTROLLER, eNodeBArray, true, eNodeBGroup, true);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.13 Description: It shall be possible to view cause code detailed analysis for
     * eCell.
     */
    @Test
    public void eCellCauseCodeDetailAnalysis_7_13() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.ACCESS_AREA, eCellArray, false, "", true);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.14 Description: It shall be possible to view cause code detailed analysis for
     * eCell group.
     */
    @Test
    public void eCellGroupCauseCodeDetailAnalysis_7_14() throws Exception {

        final String eCellGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eCellGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.ACCESS_AREA, eCellArray, true, eCellGroup, true);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.15 Description: It shall be possible to view cause code detailed analysis for
     * Tracking Area.
     */
    @Test
    public void trackingAreaCauseCodeDetailAnalysis_7_15() throws Exception {

        final String trackingArea = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(trackingArea);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = new String[1];
        trackingAreaArray[0] = trackingArea;
        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, false, "", true);
    }

    /**
     * Requirement: 105 65-0582/00239, 105 65-0582/00240 Test Case: 7.16 Description: It shall be possible to view cause code detailed analysis for
     * Tracking Area.
     */
    @Test
    public void trackingAreaGroupCauseCodeDetailAnalysis_7_16() throws Exception {

        final String trackingAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA_Group);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(trackingAreaGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Cause Code Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour",
                networkCauseCodeAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_DATA_LTE).split(",");
        // Validate Data and Drill Down on required Columns
        validateCauseCodeAnalysisWindow(networkCauseCodeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, true, trackingAreaGroup, true);
    }

    /**
     * Requirement: 105 65-0582/00237, 105 65-0582/00238 Test Case: 7.17 Description: It shall be possible to view a summary of Call Setup/Call drop
     * Failure events for a nominated TA. From The TA Event Analysis summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void TrackingAreaCallFailureAnalysis_7_17() throws Exception {

        final String trackingArea = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(trackingArea);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkEventAnalysis.getTimeRange()
                .equals(GuiStringConstants.DEFAULT_TIME_RANGE));


        final List<String> trackingAreaList = new ArrayList<String>();
        trackingAreaList.add(trackingArea);

        // Validate Data and Drill Down on required Columns
        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, trackingAreaList, true, GuiStringConstants.TRACKING_AREA);

    }

    /**
     * Requirement: 105 65-0582/00245, 105 65-0582/00246 Test Case: 7.19 Description: It shall be possible to view a summary of Call Setup/Call drop
     * Group Failure events for a nominated TA. From The TA Event Analysis summary window it shall be possible to drill-down into failed events.
     */
    @Test
    public void TrackingAreaGroupCallFailureAnalysis_7_19() throws Exception {

        final String trackingAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA_Group);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(trackingAreaGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Network Event Analysis", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> trackingAreaGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.TRACKING_AREA_GROUP_DATA_LTE).split(",")));

        validateDataOfEventAnalysisWindowForAllTimeRanges(networkEventAnalysis, trackingAreaGroupList, true, GuiStringConstants.TRACKING_AREA);
    }

    /************************************************************************************************/
    /**** Test Cases 7.21 to 7.26 is already handled in test cases 7.1, 7.2, 7.3, 7.4, 7.17, 7.19 ***/
    /************************************************************************************************/

    /**
     * Requirement: Test Case: 7.27 and 7.28 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call
     * drop failures for specified Tracking Area.
     */
    @Test
    public void TrackingAreaQCIStatistics_7_27_and_7_28() throws Exception {

        final String trackingArea = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA);
        Thread.sleep(2000);
        workspacerc.enterDimensionValue(trackingArea);
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = new String[1];
        trackingAreaArray[0] = trackingArea;

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, true);
    }

    /**
     * Requirement: Test Case: 7.29 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call drop
     * failures for specified Tracking Area Group. Also From the No of Failures it shall be possible to drill-down to event analysis window (summary
     * by event types.
     */
    @Test
    public void TrackingAreaGroupQCIstatistics_7_29() throws Exception {

        final String trackingAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA_Group);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(trackingAreaGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String trackingAreaArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_DATA_LTE).split(",");
        // Validate Data and Drill Down on required Columns        
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaArray, true);
    }

    /**
     * Requirement: Test Case: 7.30 and 7.21 Description: It shall be possible to view Network QCI Analysis which are experiencing the call setup/call
     * drop failures for specified Controller.
     */
    @Test
    public void ControllerQCIStatistics_7_30_and_7_31() throws Exception {

        final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(2000);
        workspacerc.enterDimensionValue(controller);
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        String controllerArray[] = new String[0];
        controllerArray = controller.split(",", 2);

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.CONTROLLER, controllerArray, true);
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
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(eNodeBGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.CONTROLLER, eNodeBArray, true);
    }

    /**
     * Requirement: Test Case: 7.33 and 7.34 Description: It shall be possible to drill-down to event analysis window (summary by event types) from
     * Access area summary window.
     */
    @Test
    public void AccessAreaQCIStatistics_7_33_and_7_34() throws Exception {


        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");
        List<String> eCellList = new ArrayList<String>();
        eCellList.add(eCellArray[0]);

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));


        String accessAreaArray[] = new String[1];
        accessAreaArray = eCell.split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.ACCESS_AREA, accessAreaArray, true);
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
        workspacerc.selectWindowType("QOS", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Check if default time range is set to 1 Hour
        assertTrue("ERROR - Default TimeRange is not set to 1 Hour", networkQCIAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",");

        // Validate Data and Drill Down on required Columns
        validateQCIstatisticsWindow(networkQCIAnalysis, GuiStringConstants.ACCESS_AREA, eCellArray, true);
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
        workspacerc.selectDimension(SeleniumConstants.CONTROLLER);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eNodeB);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eNodeBArray[] = eNodeB.split(",");
        List<String> eNodeBList = new ArrayList<String>();
        eNodeBList.add(eNodeBArray[0]);

        // Validate Data and Drill Down on required Columns
        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.CONTROLLER, eNodeBList, false);
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
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> eNodeBGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.CONTROLLER_GROUP_DATA_LTE).split(",")));

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.CONTROLLER, eNodeBGroupList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.3 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified eCell. Over a number of intervals within the specified query time range. It shall be possible to view individual
     * sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume by failure
     * types.
     */
    @Test
    public void AccessAreaEventVolumeAnalysis_8_3() throws Exception {

        final String eCell = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.ACCESS_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(eCell);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        // Since Controller will hold Vendor information as well, we have to segregate them
        String eCellArray[] = eCell.split(",");
        List<String> eCellList = new ArrayList<String>();
        eCellList.add(eCellArray[0]);

        // Validate Data and Drill Down on required Columns
        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.ACCESS_AREA, eCellList, false);
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
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> eCellGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.ACCESS_AREA_GROUP_DATA_LTE).split(",")));

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.ACCESS_AREA, eCellGroupList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.5 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified Tracking Area. Over a number of intervals within the specified query time range. It shall be possible to view
     * individual sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume
     * by failure types.
     */
    @Test
    public void TrackingAreaEventVolumeAnalysis_8_5() throws Exception {

        final String trackingArea = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_LTE);
;

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA);
        Thread.sleep(5000);
        workspacerc.enterDimensionValue(trackingArea);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        String trackingAreaArray[] = trackingArea.split(",");
        List<String> trackingAreaList = new ArrayList<String>();
        trackingAreaList.add(trackingAreaArray[0]);

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaList, false);
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.6 Description: It shall be possible to view a chart which represents the number of failures per
     * event type for a specified Tracking Area Group. Over a number of intervals within the specified query time range. It shall be possible to view
     * individual sets of values for each of Connection Setup Failures and Call Drop Failures. It shall be possible to view the overall event volume
     * by failure types.
     */
    @Test
    public void TrackingAreaGroupEventVolumeAnalysis_8_6() throws Exception {

        final String trackingAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.TRACKING_AREA_GROUP_LTE);


        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.TRACKING_AREA_Group);
        Thread.sleep(5000);
        workspacerc.enterDimensionValueForGroups(trackingAreaGroup);
        Thread.sleep(5000);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        selenium.waitForPageLoadingToComplete();

        final List<String> trackingAreaGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper.getCommonReservedData(
                CommonDataType.TRACKING_AREA_GROUP_DATA_LTE).split(",")));

        validateEventVolumeAnalysisWindow(networkEventVolumeAnalysis, GuiStringConstants.TRACKING_AREA, trackingAreaGroupList, false);
    }


    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.7 Part 1 Check that the Grid Headings are Correct Description: It shall be possible to view a chart
     * which represents the number of failures per event type for a specified eNodeB, eCell, Tracking Area, eNodeB Group, eCell Group or Tracking Area
     * Group. GAT 18.3.14
     */
    @Test
    public void NetworkEventVolumeAnaflysis_8_7_1() throws Exception {
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(3000);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        workspacerc.clickLaunchButton();
        Thread.sleep(3000);
        selenium.click(SeleniumConstants.GRID_LAUNCH_BUTTON);
        selenium.waitForPageLoadingToComplete();
        Thread.sleep(3000);
        validateNetworkEventVolumeGridHeaders();
    }

    /**
     * Requirement: 105 65-0582/00624 Test Case: 8.7 Part 2 Check that the Chart Headings are Visible Description: It shall be possible to view a
     * chart which represents the number of failures per event type for a specified eNodeB, eCell, Tracking Area, eNodeB Group, eCell Group or
     * Tracking Area Group. GAT 18.3.14
     */
    @Test
    public void NetworkEventVolumeAnalysis_8_7_2() throws Exception {
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        workspacerc.selectWindowType("Event Volume", "Call Failure (4G)");
        workspacerc.clickLaunchButton();
        Thread.sleep(3000);
        selenium.click(SeleniumConstants.CHART_VIEW);
        selenium.click(SeleniumConstants.SELECT_ALL);
        selenium.click(SeleniumConstants.CHART_LAUNCH_BUTTON);
        selenium.waitForPageLoadingToComplete();
        Thread.sleep(3000);
        assertTrue("ERROR - Grid Headings are Missing", selenium.isElementPresent(SeleniumConstants.GRID_HEADINGS));
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void validateDataOfEventAnalysisWindowForAllTimeRanges(final CommonWindow commonWindow, List<String> networkGroupList,
                                                                   boolean drillDownFailures, String networkEntityType) throws NoDataException,
            PopUpException, ParseException, IOException {
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

            networkEventAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<String> categoryList = commonWindow.getAllTableDataAtColumn(GuiStringConstants.FAILURE_CATEGORY);

            for (String category : categoryList) {
                final int row = commonWindow.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.FAILURE_CATEGORY, category);
                commonWindow.clickTableCell(row, GuiStringConstants.FAILURE_CATEGORY);
                waitForPageLoadingToComplete();

                List<Map<String, String>> networkGroupEventFailureList = commonWindow.getAllTableData();

                if (!drillDownFailures) {
                    commonWindow.clickBackwardNavigation();
                    continue;
                }

                drillDownFailuresOnNetworkGroupEventAnalysisWindow(commonWindow, networkGroupList, networkEntityType, time.getMiniutes());
                commonWindow.clickBackwardNavigation();
            }
        }
    }

    private void drillDownFailuresOnNetworkGroupEventAnalysisWindow(final CommonWindow window, List<String> networkGroupList,
                                                                    String networkEntityType, int timeRangeInMinutes) throws NoDataException,
            PopUpException, ParseException, IOException {

        boolean returnValue = false;
        List<String> listOfEventTypes = new ArrayList<String>();
        listOfEventTypes = window.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE);

        // Drill down for all event types
        for (final String eventType : listOfEventTypes) {
            final int row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            window.clickTableCell(row, GuiStringConstants.FAILURES);
            waitForPageLoadingToComplete();
            assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));

            checkInOptionalHeadersLTECFA(window, eventType);

            window.clickBackwardNavigation();
        }

    }

    private void checkInOptionalHeadersLTECFA(CommonWindow window, String eventType) {
        window.openTableHeaderMenu(0);
        window.checkInOptionalHeaderCheckBoxes(headersToTickIfPresent);
        try {
            window.uncheckOptionalHeaderCheckBoxes(headersToUnTickIfPresent);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (eventType.equals(GuiStringConstants.RRC_CONN_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfRrcConnSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfS1SigConnSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfInitialCtxtSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.ERAB_SETUP)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfErabSetupOnIMSIFailedEventAnalysisWindow);
        } else if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE)) {
            window.checkInOptionalHeaderCheckBoxes(optionalHeadersOfUeCntxtReleaseOnIMSIFailedEventAnalysisWindow);
        } else {
            assertTrue("Invalid Event Type for LTE Call Failures", false);
            return;
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
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }

            window.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

            List<Map<String, String>> listOfCauseCodeData = window.getAllTableData();

            if (drillDownRequired) {
                drillDownOccurrencesCauseCodeAnalysisWindow(window, listOfCauseCodeData, isGroup, time.getMiniutes());
            }
        }

    }

    private void drillDownOccurrencesCauseCodeAnalysisWindow(final CommonWindow window, List<Map<String, String>> listOfCauseCodeData,
                                                             boolean isGroup, int timeRangeInMinutes) throws NoDataException, PopUpException,
            ParseException, IOException {

        boolean returnValue = false;
        int row = 0;
        int column = 0;
        int causeCodeColumn = 0;

        // Drill down for all event types
        for (final Map<String, String> uiCauseCodeMap : listOfCauseCodeData) {
            String eventType = uiCauseCodeMap.get(GuiStringConstants.EVENT_TYPE);
            String causeCodeEventType = uiCauseCodeMap.get(GuiStringConstants.CAUSE_CODE_DESCRIPTION) + "," + eventType;

            row = window.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.EVENT_TYPE, eventType);

            if (isGroup) {
                column = 2;
                causeCodeColumn = 1;
            } else {
                column = 2;
                causeCodeColumn = 1;
            }
            String causeCodeDesc = window.getTableData(row, causeCodeColumn);

            // If EventType and CauseCode not found in same row then ignore processing it
            if (!causeCodeDesc.equals(uiCauseCodeMap.get(GuiStringConstants.CAUSE_CODE_DESCRIPTION))) {
                continue;
            }

            String occurrences = window.getTableData(row, column);
            int uiOccurrences = Integer.parseInt(occurrences);

            window.clickTableCell(row, GuiStringConstants.OCCURRENCES);
            waitForPageLoadingToComplete();

            checkInOptionalHeadersLTECFA(window, eventType);

            window.clickBackwardNavigation();

            row++;
        }

    }

    private void validateQCIstatisticsWindow(final CommonWindow window, String neType, String neValue[], boolean drillDownRequired)
            throws NoDataException, PopUpException, ParseException, IOException {

        window.openGridView();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
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
            window.clickTableCell(0, GuiStringConstants.FAILURE_CATEGORY);
            waitForPageLoadingToComplete();
            List<Map<String, String>> qciDataEventType = window
            .getAllTableData();
            for (int i = 0; i < qciDataEventType.size(); i++) {
            Map<String, String> result = qciDataEventType.get(i);
            if (result.get(GuiStringConstants.EVENT_TYPE).equals(
            GuiStringConstants.INTERNAL_PROC_ERAB_RELEASE)) {
            window.clickTableCell(i, GuiStringConstants.FAILURES);
            window.openTableHeaderMenu(0);
            assertFalse(
            "ERAB Release Request 3GPP Cause Group column should be hidden by default",
            window.getTableHeaders()
            .contains(
            GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_GROUP));

            assertFalse(
            "ERAB Release Request 3GPP Cause Code column should be hidden by default",
             window.getTableHeaders().contains(
             GuiStringConstants.ERAB_RELEASE_REQUEST_3GPP_CAUSE_CODE));

             assertFalse(
             "ERAB Release Failure 3GPP Cause Group column should be hidden by default",
             window.getTableHeaders()
                .contains(
             GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_GROUP));

             assertFalse(
             "ERAB Release Failure 3GPP Cause Code column should be hidden by default",
             window.getTableHeaders().contains(
             GuiStringConstants.ERAB_RELEASE_FAILURE_3GPP_CAUSE_CODE));

             window.checkInOptionalHeaderCheckBoxes(ERABRealeaseHeadersToTickIfPresent);

             try {
             window.uncheckOptionalHeaderCheckBoxes(headersToUnTickIfPresent);
             } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             }
             try {
             window.clickBackwardNavigation();
             } catch (Exception e) {

            }

            } else {
             window.clickTableCell(i, GuiStringConstants.FAILURES);
             window.openTableHeaderMenu(0);
             window.checkInOptionalHeaderCheckBoxes(headersToTickIfPresent);
             try {
             window.uncheckOptionalHeaderCheckBoxes(headersToUnTickIfPresent);
             } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             }
             try {
             window.clickBackwardNavigation();
             } catch (Exception e) {

             }
           }
          }
         }
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
            System.out.println("time = " + time);
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

    /**
     * Checks that the Correct Headers are in the Window "Network Event Volume LTE Call Failure Event Analysis"
     */
    private void validateNetworkEventVolumeGridHeaders() {
        assertTrue("ERROR - Time Column is Missing", GuiStringConstants.TIME.equals(selenium.getText(SeleniumConstants.TIME_COLUMN_HEADING)));
        assertTrue("ERROR - INTERNAL_PROC_RRC_CONN_SETUP Fail Count Column is Missing",
                (GuiStringConstants.RRC_CONN_SETUP + " Fail Count").equals(selenium
                        .getText(SeleniumConstants.INTERNAL_PROC_RRC_CONN_SETUP_Count_COLUMN_HEADING)));
        assertTrue("ERROR - INTERNAL_PROC_S1_SIG_CONN_SETUP Fail Count Column is Missing",
                (GuiStringConstants.S1_SIG_CONN_SETUP + " Fail Count").equals(selenium
                        .getText(SeleniumConstants.INTERNAL_PROC_S1_SIG_CONN_SETUP_COLUMN_HEADING)));
        assertTrue("ERROR - INTERNAL_PROC_INITIAL_CTXT_SETUP Fail Count Column is Missing",
                (GuiStringConstants.INITIAL_CTXT_SETUP + " Fail Count").equals(selenium
                        .getText(SeleniumConstants.INTERNAL_PROC_INITIAL_CTXT_SETUP_COLUMN_HEADING)));
        assertTrue("ERROR - INTERNAL_PROC_ERAB_SETUP Fail Count Column is Missing",
                (GuiStringConstants.ERAB_SETUP + " Fail Count").equals(selenium.getText(SeleniumConstants.INTERNAL_PROC_ERAB_SETUP_COLUMN_HEADING)));
        assertTrue("ERROR - INTERNAL_PROC_UE_CTXT_RELEASE Fail Count Column is Missing",
                (GuiStringConstants.UE_CTXT_RELEASE + " Fail Count").equals(selenium
                        .getText(SeleniumConstants.INTERNAL_PROC_UE_CTXT_RELEASE_COLUMN_HEADING)));
    }

}