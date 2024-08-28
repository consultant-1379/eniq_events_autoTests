package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.BaseWcdmaCfa;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/*
 * Updated/refactored by efreass based on VS_ENIQ_Events12_2_WCDMA_HFA_2.2.14_SERVER2012
 * 23 july 2012 
 */
public class NetworkTestGroupWcdmaHfa extends EniqEventsUIBaseSeleniumTest {
	 
	final List<String> headersToTickIfPresent = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, 
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, 
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE));
	
   @Autowired
    private NetworkTab networksTab;
 
   @Override
    @Before
    public void setUp() {
     	super.setUp();
    	pause(2000);
    }

    @Autowired
    @Qualifier("networkEventAnalysisWCDMAHFA")
    private CommonWindow networkEventAnalysisWCDMAHFA;
              
    private static final String EXPECTED_HEADER = " EXPECTED HEADERS : ";
    private static final String WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    //EE12.2_WHFA_7.1; Controller Handover Failure Analysis – Summary Pane

    @Test
    public void Controller_Handover_Failure_Analysis_On_Network_Tab_7_1() throws Exception {

        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);        
    }

    //EE12.2_WHFA_7.1a; Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO)   
    @Test
    public void RNC_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_1a() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow));
               
    }

    // EE12.1_WHFA_7.1b; Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO)    
    @Test
    public void RNC_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_1b() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow));
    }

    // EE12.1_WHFA_7.1c; Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT)    
    @Test
    public void RNC_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_1c() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow));
    }

    // EE12.1_WHFA_7.1d; Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH)    
    @Test
    public void RNC_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_1d() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow));
    }

    // EE12.1_WHFA_7.2; Controller Group Handover Failure Analysis  Summary Pane    
    @Test
    public void Controller_Group_Handover_Failure_Analysis_On_Network_Tab_7_2() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        // final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow));
    }

    // EE12.1_WHFA_7.2a; Drill down from Controller Group HO failure totals to Detailed Event Analysis (SOHO)    
    @Test
    public void RNC_Group_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_guy() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CONTROLLER_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));
    }

    // EE12.1_WHFA_7.2b; Drill down from Controller Group HO failure totals to Detailed Event Analysis (IFHO)
    @Test
    public void RNC_Group_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_2b() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow));
        //networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        networkEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));
    }

    // EE12.1_WHFA_7.2c; Drill down from Controller Group HO failure totals to Detailed Event Analysis (IRAT)
    @Test
    public void RNC_Group_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_2c() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));
    }

    // EE12.1_WHFA_7.2d; Drill down from Controller Group HO failure totals to Detailed Event Analysis (HSDSCH)
    @Test
    public void RNC_Group_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_2d() throws Exception {
        final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
        //final String controller = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultControllerGroupEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow));
    }

    
   //EE12.2_WHFA_7.3; Access Area Handover Failure Analysis – Summary Pane - Managed 3G Cell 

    @Test
    public void AccessArea_SUMMARY_MANAGED_3G_HO_Failure_Analysis_On_Network_Tab_7_3() throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        //ADDED
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
    }

    // EE12.2_WHFA_7.3a; Access Area Handover Failure Analysis – Summary Pane - Unmanaged 3G Cell
    @Test
    public void AccessArea_SUMMARY_UMANAGED_3G_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_3a() throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
 }

    //EE12.2_WHFA_7.3b; Access Area Handover Failure Analysis – Summary Pane - Managed 2G Cell
    @Test
    public void AccessArea_SUMMARY_2G_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_3b() throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
    }

    //EE12.2_WHFA_7.3c; Access Area Handover Failure Analysis – Summary Pane - Unmanaged 2G Cell
    @Test
    public void AccessArea_SUMMARY_2G_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_3c() throws Exception {
    	
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
    }


    //EE12.2_WHFA_7.3d; Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO Source Cell)
    @Test
    public void AccessArea_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_3d() throws Exception {    	
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        //        final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMASOHOWindow));
    }
    
    //EE12.2_WHFA_7.3e; Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO Target Cell)
    @Test
    public void AccessAreaTotal_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis__SOHO_7_3e() throws Exception {
    
    	 final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
    	 networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
         final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
         
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
         networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
         basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
         drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
         final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                 + windowHeaders1);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders1.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMASOHOWindow));
 }
    
    //EE12.2_WHFA_7.3f; Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO Source Cell)
    @Test
    public void AccessAreaTotal_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis__IFHO_7_3f() throws Exception {
    
    	 final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
    	 networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
         final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
         networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
         basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
         drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
         final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                 + windowHeaders1);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceNetworkEventAnalysiWCDMAIFHOWindow));
    }
    
    //EE12.2_WHFA_7.3g; Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO Target Cell)
    @Test
    public void AccessAreaTotal_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis__IFHO7_3g() throws Exception {
    	final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
   	 	networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        networkEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders1.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAIFHOWindow));
     }
    
     //EE12.2_WHFA_7.3h; Drill down from Access Area total per HO type to Detailed Event Analysis (IRAT Source Cell) 
     @Test
     public void AccessAreaTotal_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_3h() throws Exception {
       final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow + WINDOW_HEADER + windowHeadersDrillDown);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAIRATWindow));
    }
    
    //EE12.2_WHFA_7.3i; Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH Source Cell)
     @Test
     public void AccessAreaTotal_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_3i() throws Exception {
    	  final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
          networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
          final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
          logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
          assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                  windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
          networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
          basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
         drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
          		GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
          final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
          logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER + windowHeadersDrillDown);
          assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultNetworkEventAnalysiWCDMAHSDSCHWindow));
   
     }
     
     //EE12.2_WHFA_7.3j; Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH Target Cell)
     @Test
     public void AccessAreaTotal_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_3j() throws Exception {
    	 final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_VALUE;
         networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, ACCESS_AREA);
         final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
          networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
          networkEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
         basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
         drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
         		GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
         final List<String> windowHeadersDrillDown = networkEventAnalysisWCDMAHFA.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER + windowHeadersDrillDown);
          assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeadersDrillDown.containsAll(BaseWcdmaHfa.defaultTargetNetworkEventAnalysiWCDMAHSDSCHWindow));
     }
     
     //EE12.2_WHFA_7.4; Access Area Group Handover Failure Analysis – Summary Pane – 2G Cell Group
    @Test
    public void AccessArea_Group_SUMMARY_2G_Handover_Failure_Analysis_On_Network_Tab_7_4() throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        networkEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);        
    }

    //EE12.2_WHFA_7.4a; Access Area Group Handover Failure Analysis – Summary Pane – 3G Cell Group
    @Test
    public void AccessArea_Group_SUMMARY_3G_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4a()
            throws Exception {
        final String GROUP_ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, GROUP_ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
       drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));

    }

   //FRED	EE12.2_WHFA_7.4b; Drill down from Access Area Group total per HO type to Detailed Event Analysis (SOHO – Source Cell)
    @Test
    public void AccessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4b()
            throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);        
        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));
    }

    //EE12.2_WHFA_7.4c; Drill down from Access Area Group total per HO type to Detailed Event Analysis (SOHO – Target Cell)
    @Test
    public void AccessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_SOHO_7_4c()
            throws Exception {
        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);  
        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMASOHOWindow));
    }

     //FRED	EE12.2_WHFA_7.4d; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IFHO Source Cell)
    @Test
    public void AccessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_4d()
            throws Exception {

        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        // final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
       drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));
    }
    
     //EE12.2_WHFA_7.4e; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IFHO Target Cell)
    @Test
    public void AccessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IFHO_7_4e()
            throws Exception {

        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        // final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkEventAnalysiWCDMAIFHOWindow));
    }
    
   //EE12.2_WHFA_7.4f; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IRAT Source Cell)
    @Test
    public void AccessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_4f()
            throws Exception {

        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        //final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
         drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));
    }
    
     //EE12.2_WHFA_7.4g; Drill down from Access Area Group total per HO type to Detailed Event Analysis (IRAT Target Cell)
    @Test
    public void AccessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_IRAT_7_4G()
            throws Exception {

        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        // final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow));
    }
    
    //EE12.2_WHFA_7.4h; Drill down from Access Area Group total per HO type to Detailed Event Analysis (HSDSCH Source Cell)
    @Test
    public void AccessArea_Group_SOURCE_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_4h()
            throws Exception {

        final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        // final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
        networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
        final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
        networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
        drillDownOnParticularCell(GuiStringConstants.SOURCE_FAILURES, networkEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupSourceNetworkDetailEventAnalysisWCDMAHSDSCHWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupSourceNetworkDetailEventAnalysisWCDMAHSDSCHWindow));
    }
    
  //EE12.2_WHFA_7.4i; Drill down from Access Area Group total per HO type to Detailed Event Analysis (HSDSCH Target Cell)
    @Test
    public void AccessArea_Group_TARGET_HO_Failure_Analysis_On_Network_Tab_To_Detailed_Event_Analysis_HSDSCH_7_4i()
            throws Exception {
    	  final String ACCESS_AREA = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
          // final String ACCESS_AREA = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP_MSS);
          networksTab.openEventAnalysisWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, ACCESS_AREA);
          final List<String> windowHeaders = networkEventAnalysisWCDMAHFA.getTableHeaders();
          logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow + WINDOW_HEADER
                  + windowHeaders);
          assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                  windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaEventAnalysisWindow));
          networkEventAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
          basicWindowFunctionalityCheck(networkEventAnalysisWCDMAHFA);
          drillDownOnParticularCell(GuiStringConstants.TARGET_FAILURES, networkEventAnalysisWCDMAHFA,
                  GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
          final List<String> windowHeaders1 = networkEventAnalysisWCDMAHFA.getTableHeaders();
          logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow + WINDOW_HEADER
                  + windowHeaders1);
          assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                  windowHeaders1.containsAll(BaseWcdmaHfa.defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow));
        
    }

    /**
     * Drill down one link on Failure column on event analysis. 
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck This column to locate row number
     * @param values These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownOnParticularCell(final String drillDownHeader, final CommonWindow window,
            final String columnToCheck, final String... values) throws NoDataException, PopUpException {
    	window.sortTable(SortType.DESCENDING, columnToCheck);
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, drillDownHeader);
        waitForPageLoadingToComplete();
        
        String eventType = GuiStringConstants.EVENT_TYPE;
        checkInOptionalHeadersWCDMA(window, eventType);    	
    }
    
    
    
    /* This method is to do the basic check on the windows
     * Repeat of Method in TerminalTestGroup
     *  @param commonWindow - The window id
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
    

    
    private void checkInOptionalHeadersWCDMA(CommonWindow window, String eventType)
    {
    	window.openTableHeaderMenu(0);    
    	window.checkInOptionalHeaderCheckBoxes(headersToTickIfPresent);
    }

}
