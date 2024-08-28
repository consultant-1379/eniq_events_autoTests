package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
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
public class RankingTestGroupWcdmaHfa extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    private NetworkTab networksTab;

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("rncRankingWCDMAHandOverFailure")
    private CommonWindow rncRankingWCDMAHandOverFailure;

    @Autowired
    @Qualifier("sourceAccessAreaRankingWCDMAHandOverFailure")
    private CommonWindow sourceAccessAreaRankingWCDMAHOF;

    @Autowired
    @Qualifier("targetAccessAreaRankingWCDMAHandOverFailure")
    private CommonWindow targetAccessAreaRankingWCDMAHOF;

    @Autowired
    @Qualifier("terminalRankingWCDMAHandOverFailure")
    private CommonWindow terminalRankingWCDMAHOF;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureSOHO")
    private CommonWindow subscriberRankingWCDMAHOFSOHO;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureIRAT")
    private CommonWindow subscriberRankingWCDMAHOFIRAT;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureIFHO")
    private CommonWindow subscriberRankingWCDMAHOFIFHO;

    @Autowired
    @Qualifier("subscriberRankingWCDMAHandOverFailureHSDSCH")
    private CommonWindow subscriberRankingWCDMAHOFHSDSCH;

    @Autowired
    @Qualifier("accessAreaEventAnalysisWindow")
    private CommonWindow accessAreaEventAnalysisWindow;

    @Autowired
    @Qualifier("controllerEventAnalysisWindow")
    private CommonWindow controllerEventAnalysisWindow;

    @Autowired
    @Qualifier("causeCodeRankingWCDMAHandOverFailure")
    private CommonWindow causeCodeRankingWCDMAHandOverFailure;

    @Autowired
    @Qualifier("subscriberWCDMASOHOWindow")
    private CommonWindow subscriberWCDMASOHOWindow;
    
    //added by Fred
    @Autowired
    @Qualifier("gridGraphviewRankingGroupTests")
    private CommonWindow gridGraphviewRankingGroupTests;


    final static List<String> defaultEventFailureAnalysisWindowCauseCode = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    private final String EXPECTED_HEADER = " EXPECTED HEADERS : ";

    private final String WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
            selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
            selenium.waitForElementToBePresent("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']", "2000");
            selenium.click("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']");
        }
        pause(2000);
    }

    /*
     * Pass Pass Pass
     */
    // EE12.1_WHFA_9.1; Rankings Tab: Verify that accurate HFA RNC Rankings are displayed.
    //EE12.2_WHFA_9.1; Rankings Tab: Verify that accurate HFA RNC Rankings are displayed.
    @Test
    public void HFA_RankingTab_RNC_Rankings_Accuracy_Verification_On_9_1() throws Exception {
       rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        basicWindowFunctionalityCheck(rncRankingWCDMAHandOverFailure);
      }    
  
    // EE12.1_WHFA_9.2; Network Tab: Verify that accurate HFA RNC Rankings are displayed.
    //EE12.2_WHFA_9.2; Network Tab: Verify that accurate HFA RNC Rankings are displayed.
    @Test
    public void HFA_NetworkTab_RNC_Rankings_Accuracy_Verification_9_2() throws Exception {
         networksTab.openTab();
        networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.3; Rankings Tab: Verify that accurate HFA Access Area (CELL) Rankings are displayed. 
    //EE12.2_WHFA_9.3; Rankings Tab: Verify that accurate HFA Access Area (SOURCE CELL) Rankings are displayed.
    @Test
    public void HFA_RankingTab_Access_Area_SourceCell_Rankings_Accuracy_Verification_9_3() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.4; Network Tab: Verify that accurate HFA Access Area (CELL) Rankings are displayed. 
    //EE12.2_WHFA_9.4; Network Tab: Verify that accurate HFA Access Area (SOURCE CELL) Rankings are displayed.
    @Test
    public void HFA_NetworkTab_Access_Area_SourceCell_Rankings_Accuracy_Verification_9_4() throws Exception {
        networksTab.openTab();
         networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.5; Rankings Tab: Verify that accurate HFA Terminal Type (TAC) Rankings are displayed. 
    //EE12.2_WHFA_9.5; Rankings Tab: Verify that accurate HFA Terminal Type (TAC) Rankings are displayed.
    @Test
    public void HFA_RankingTab_Terminal_Type_TAC_Rankings_Accuracy_9_5() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.6; Terminal Tab: Verify that accurate HFA Terminal Type (TAC) Rankings are displayed.
    //EE12.2_WHFA_9.6; Terminal Tab: Verify that accurate HFA Terminal Type (TAC) Rankings are displayed.
    @Test
    public void HFA_TerminalTab_Terminal_Type_TAC_Rankings_Accuracy_Verification_9_6() throws Exception {
        terminalTab.openTab();
        terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.7; Rankings Tab: Verify that accurate HFA Subscriber SOHO Rankings are displayed.
    //EE12.2_WHFA_9.7; Rankings Tab: Verify that accurate HFA Subscriber SOHO Rankings are displayed.
    @Test
    public void HFA_RankingTab_Subscriber_Rankings_By_SOHO_Accuracy_Verification_9_7() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMA_HFA_SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));

    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.7b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (SOHO).
    //EE12.2_WHFA_9.7b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void HFA_RankingTab_Subscriber_Rankings_SOHO_FailureTotalToEventAnalysis_9_7b() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMA_HFA_SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO,EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFSOHO.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFSOHO.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberWCDMASOHOWindow.getTableHeaders();
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        //emosjil changed:
        //logger.log(Level.INFO,EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow + WINDOW_HEADER + windowHeaders1);
        
        if(version >= 13.0){
            logger.log(Level.INFO,EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover + WINDOW_HEADER + windowHeaders1);
            assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                    windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover));
        }else{
            logger.log(Level.INFO,EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow + WINDOW_HEADER + windowHeaders1);
            assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                    windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow));
        }
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.8; Subscriber Tab: Verify that accurate HFA Subscriber SOHO Rankings are displayed.
    //EE12.2_WHFA_9.8; Subscriber Tab: Verify that accurate HFA Subscriber SOHO Rankings are displayed.
    @Test
    public void HFA_SubscriberTab_SOHO_Rankings_Accuracy_Verification_9_8() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusSUBSCRIBER_RANKING_WCDMA_HFA_SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.8b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (SOHO).
    //EE12.2_WHFA_9.8b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void HFA_SubscriberTab_DrillDown_SubscriberFailureTotalEventAnalysis_SOHO_Rankings_Accuracy_Verification_9_8b() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusSUBSCRIBER_RANKING_WCDMA_HFA_SOFT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFSOHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFSOHO.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFSOHO.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberWCDMASOHOWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberWCDMASOHOWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover));
    }

    // EE12.1_WHFA_9.9; Rankings Tab: Verify that accurate HFA Subscriber IFHO Rankings are displayed.
    //EE12.2_WHFA_9.9; Rankings Tab: Verify that accurate HFA Subscriber IFHO Rankings are displayed.
    @Test
    public void HFA_RankingTab_Subscriber_IFHO_Rankings_Accuracy_Verification_9_9() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_SUBSCRIBER_WCDMA_HFA_IFHO_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.9b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IFHO).
    //EE12.2_WHFA_9.9b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IFHO).
    @Test
    public void HFA_RankingTab_DrillDown_Subscriber_FailureTotalToDetailEvents_IFHO_9_9b() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_SUBSCRIBER_WCDMA_HFA_IFHO_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFIFHO.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFIFHO.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow));
    }

    // EE12.1_WHFA_9.10; Subscriber Tab: Verify that accurate HFA Subscriber IFHO Rankings are displayed.
    //EE12.2_WHFA_9.10; Subscriber Tab: Verify that accurate HFA Subscriber IFHO Rankings are displayed.
    @Test
    public void HFA_SubscriberTab_AccuracyVerification_Subscriber_IFHO_Rankings_9_10() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusRankingWCDMAHFAIFHOHANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.10b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IFHO).
    //4.9.14	EE12.2_WHFA_9.10b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IFHO).
    @Test
    public void HFA_SubscriberTab_DrillDown_FailureTotalsToDetailedEventAnalysis_IFHA_Accuracy_Verification_9_10b() throws Exception {
        subscriberTab.openTab();
         subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusRankingWCDMAHFAIFHOHANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFIFHO.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFIFHO.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIFHO.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIFHOWindow));
    }

    // EE12.1_WHFA_9.11; Rankings Tab: Verify that accurate HFA Subscriber IRAT Rankings are displayed.
//4.9.15	EE12.2_WHFA_9.11; Rankings Tab: Verify that accurate HFA Subscriber IRAT Rankings are displayed.
    @Test
    public void HFA_RankingTab_AccuracyVerification_Subscriber_Rankings_By_IRAT_Handover_9_11() throws Exception {
        rankingsTab.openTab();
       rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMAHFAIratHandover);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.11b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IRAT).
//4.9.16	EE12.2_WHFA_9.11b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IRAT).
    @Test
    public void HFA_DrillDown_FailureAnalysisToDetailedEvent_Subscriber_Rankings_By_IRAT_Handover_9_11b() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMAHFAIratHandover);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFIRAT.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFIRAT.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow));
    }

    // EE12.1_WHFA_9.12; Subscriber Tab: Verify that accurate HFA Subscriber IRAT Rankings are displayed.
//4.9.17	EE12.2_WHFA_9.12; Subscriber Tab: Verify that accurate HFA Subscriber IRAT Rankings are displayed.
    @Test
    public void HFA_SubscriberTab_Subscriber_Rankings_By_IRAT_Handover_Accuracy_Verification_9_12() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusSUBSCRIBER_RANKING_WCDMA_HFA_IRAT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.12b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IRAT).
//4.9.18	EE12.2_WHFA_9.12b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (IRAT).
    @Test
    public void HFA_SubscriberTab_DrillDown_FailureTotalToDetailedEventAnalysis_IRAT_Accuracy_Verification_9_12b() throws Exception {
        subscriberTab.openTab();
         subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusSUBSCRIBER_RANKING_WCDMA_HFA_IRAT_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFIRAT.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFIRAT.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFIRAT.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAIRATWindow));
    }

    // EE12.1_WHFA_9.13; Rankings Tab: Verify that accurate HFA Subscriber HSDSCH Handover Rankings are displayed.
    //	EE12.2_WHFA_9.13; Rankings Tab: Verify that accurate HFA Subscriber HSDSCH Handover Rankings are displayed.
    @Test
    public void HFA_SubscriberTab_Subscriber_Rankings_By_HSDSCH_Handover_AccuracyVerification_9_13() throws Exception {
        rankingsTab.openTab();
            rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMA_HFA_HSDSCH_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.13b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (HSDSCH).
//4.9.20	EE12.2_WHFA_9.13b; Rankings Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void HFA_RankingTab_DrillDown_Subscriber_FailureTotalToDetailedEventAnalysis_HSDSCH_9_13b() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingSubscriberWCDMA_HFA_HSDSCH_HANDOVER);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFHSDSCH.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFHSDSCH.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow));
    }

    // EE12.1_WHFA_9.14; Subscriber Tab: Verify that accurate HFA Subscriber HSDSCH Handover Rankings are displayed.
    //EE12.2_WHFA_9.14; Subscriber Tab: Verify that accurate HFA Subscriber HSDSCH Handover Rankings are displayed.
    @Test
    public void HFA_SubscriberTab_Subscriber_Rankings_AccuracyVerification_HSDSCH_9_14() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusHFA_SubscriberRankingsByHSDSCH_Handover);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
    }

    // EE12.1_WHFA_9.14b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (HSDSCH).
//4.9.22	EE12.2_WHFA_9.14b; Subscriber Tab: Drill down from Subscriber HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void HFA_SubscriberTab_DrillDown_Subscriber_FailureTotalToDetailedEventAnalysis_HSDSCH_Accuracy_Verification_9_14b() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, BaseWcdmaHfa.subMenusHFA_SubscriberRankingsByHSDSCH_Handover);
        final List<String> windowHeaders = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSubscriberRankingWCDMAHandOverFailureWindow));
        subscriberRankingWCDMAHOFHSDSCH.setTimeRange(getTimeRangeFromProperties());
        subscriberRankingWCDMAHOFHSDSCH.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = subscriberRankingWCDMAHOFHSDSCH.getTableHeaders();
        logger.log(Level.INFO, "The Expected headers are : " + BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow
                + "\n  The Window headers are      : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriberWCDMAHSDSCHWindow));
    }

    // EE12.1_WHFA_9.16; Rankings Tab: Drilldown from Access Area total to HO failure type totals
////4.9.24	EE12.2_WHFA_9.16; Rankings Tab: Drilldown from Source Cell total to HO failure type totals
    @Test
    public void RankingTab_Drilldown_From_Access_Area_SourceCell_Total_To_HO_Failure_Type_Totals_9_16() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));

    }

    // EE12.1_WHFA_9.17; Network Tab: Drilldown from Access Area total to HO failure type totals
//4.9.25	EE12.2_WHFA_9.17; Network Tab: Drilldown from Source Cell total to HO failure type totals
    @Test
    public void NetworkTab_HFA_Access_Area_Rankings_Accuracy_Verification_On_Network_Tab_9_17() throws Exception {
        networksTab.openTab();
         networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));

       }

    //4.9.18	EE12.0_HFA_9.18; Ranking Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (IRAT)
    //4.9.26	EE12.2_WHFA_9.18; Ranking Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (IRAT)    
    @Test
    public void RankingTab_Drilldown_From_Access_Area_Total_To_HO_Type_To_Event_Analysis_IRAT_9_18() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT
                + "\n    The Window headers are : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT));
    }

    //4.9.19	EE12.0_HFA_9.19; Network Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (IRAT)
//4.9.27	EE12.2_WHFA_9.19; Network Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (IRAT)
    @Test
    public void NetworkTab_Drilldown_From_Access_Area_Total_To_HO_Type_To_Event_Analysis_IRAT_9_19() throws Exception {
        networksTab.openTab();
          networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders2 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT
                + "\n    The Window headers are : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT));
    }

    // 4.9.20	EE12.0_HFA_9.20; Ranking Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO)
//4.9.28	EE12.2_WHFA_9.20; Ranking Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (IFHO)
    @Test
    public void RankingTab_Drilldown_From_Access_Area_Total_To_HO_Type_To_Event_Analysis_IFHO_9_20() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO
                + "\n    The Window headers are : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO));
    }

    //4.9.21	EE12.0_HFA_9.21; Network Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (IFHO)
//4.9.29	EE12.2_WHFA_9.21; Network Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (IFHO)
    @Test
    public void NetworkTab_Drilldown_From_Access_Area_Total_To_HO_Type_To_Event_Analysis_IFHO_9_21() throws Exception {
        networksTab.openTab();
         networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders2 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO
                + "\n    The Window headers are : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO));
    }

    //4.9.22	EE12.0_HFA_9.22; Ranking Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH)
//4.9.30	EE12.2_WHFA_9.22; Ranking Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (HSDSCH)
    @Test
    public void RankingsTab_Drilldown_From_Access_Area__SourCellTotal_To_HO_Type_To_Event_Analysis_HSDSCH_9_22()
            throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH
                + "\n    The Window headers are : " + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH));
    }

    //4.9.23	EE12.0_HFA_9.23; Network Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (HSDSCH)
//4.9.31	EE12.2_WHFA_9.23; Network Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (HSDSCH)
    @Test
    public void NetworkTab_Drilldown_From_Access_Area__SourCellTotal_To_HO_Type_To_Event_Analysis_HSDSCH_9_23()
            throws Exception {
        networksTab.openTab();
         networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders2 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH
                + "\n    The Window headers are : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH));
    }

    /*
     *PASS PASS PASS 
     */
    //4.9.24	EE12.0_HFA_9.24; Ranking Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO)
//4.9.32	EE12.2_WHFA_9.24; Ranking Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (SOHO)
    @Test
    public void RankingTab_Drilldown_From_Access_Area__SourCellTotal_To_HO_Type_To_Event_Analysis_SOHO_9_24() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRANKING_ACCESS_AREA_WCDMA_HFA);
        final List<String> sourceWindowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + sourceWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                sourceWindowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover
                + "\n    The Window headers are : " + windowHeaders1 );
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover));
    }

    //4.9.25	EE12.0_HFA_9.25; Network Tab: Drill down from Access Area total per HO type to Detailed Event Analysis (SOHO)
//4.9.33	EE12.2_WHFA_9.25; Network Tab: Drill down from Source Cell total per HO type to Detailed Event Analysis (SOHO)
    @Test
    public void Network_Tab_Drilldown_From_Access_Area__SourCellTotal_To_HO_Type_To_Event_Analysis_SOHO_9_25() throws Exception {
        networksTab.openTab();
             networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultAccessAreaRankingWCDMAHandOverFailureWindow));
        sourceAccessAreaRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        sourceAccessAreaRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.FAILURES);
        final List<String> windowHeaders1 = accessAreaEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultSourceCellEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, accessAreaEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders2 = sourceAccessAreaRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover
                + "\n    The Window headers are : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover));
    }

    // EE12.1_WHFA_9.26; Rankings Tab: Drilldown from RNC total to HO failure type totals
//4.9.34	EE12.2_WHFA_9.26; Rankings Tab: Drilldown from RNC total to HO failure type totals
    @Test
    public void RankingTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_9_26() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
    }

    // EE12.1_WHFA_9.26a; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO).
//4.9.35	EE12.2_WHFA_9.26a; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void RankingTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_SOHO_9_26a() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowSOHO
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowSOHO));
    }

    // EE12.1_WHFA_9.26b; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO)
//4.9.36	EE12.2_WHFA_9.26b; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO)
    @Test
    public void RankingTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_IFHO_9_26b() throws Exception {
        rankingsTab.openTab();
           rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO));
    }

    // EE12.1_WHFA_9.26c; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT)
//4.9.37	EE12.2_WHFA_9.26c; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT)
    @Test
    public void RankingTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_IRAT_9_26c() throws Exception {
        rankingsTab.openTab();
           rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT));
    }

    // EE12.1_WHFA_9.26d; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH).
//4.9.38	EE12.2_WHFA_9.26d; Rankings Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void RankingTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_HSDSCH_9_26d() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingRNC);
        final List<String> windowHeaders = rncRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH));
    }

    // EE12.1_WHFA_9.27; Network Tab: Drilldown from RNC total to HO failure type totals
//4.9.39	EE12.2_WHFA_9.27; Network Tab: Drilldown from RNC total to HO failure type totals
    @Test
    public void NetworkTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_9_27() throws Exception {
        networksTab.openTab();
           networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, rncRankingWCDMAHandOverFailure.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
    }

    
    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.27a; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO)
//4.9.40	EE12.2_WHFA_9.27a; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (SOHO)
    @Test
    public void NetworkTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_SOHO_9_27a() throws Exception {
        networksTab.openTab();
          networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, rncRankingWCDMAHandOverFailure.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultSubscriber_Rankings_By_Soft_Handover));
    }

    // EE12.1_WHFA_9.27b; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO)
//4.9.41	EE12.2_WHFA_9.27b; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IFHO)
    @Test
    public void NetworkTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_IFHO_9_27b() throws Exception {
        networksTab.openTab();
          networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, rncRankingWCDMAHandOverFailure.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIFHO));
    }

    // EE12.1_WHFA_9.27c; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT)
//4.9.42	EE12.2_WHFA_9.27c; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (IRAT)
    @Test
    public void NetworkTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_IRAT_9_27c() throws Exception {
        networksTab.openTab();
           networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, rncRankingWCDMAHandOverFailure.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowIRAT));
    }

    // EE12.1_WHFA_9.27d; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH)
//4.9.43	EE12.2_WHFA_9.27d; Network Tab: Drill down from RNC HO failure totals to Detailed Event Analysis (HSDSCH)
    @Test
    public void NetworkTab_Drilldown_From_RNC_Total_To_HO_failure_Type_Totals_HSDSCH_9_27d() throws Exception {
        networksTab.openTab();
         networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, rncRankingWCDMAHandOverFailure.getTableHeaders()
                .containsAll(BaseWcdmaHfa.defaultRNCRankingWCDMAHandOverFailureWindow));
        rncRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        rncRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.RNC);
        final List<String> windowHeaders1 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultControllerEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultControllerEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, controllerEventAnalysisWindow,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders2 = controllerEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH
                + "\n    The Window headers are    : " + windowHeaders2);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders2.containsAll(BaseWcdmaHfa.defaultEventFailureAnalysisWindowHSDSCH));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.28; Rankings Tab: Drilldown from Terminal total to HO failure type totals
//4.9.44	EE12.2_WHFA_9.28; Rankings Tab: Drilldown from Terminal total to HO failure type totals
    @Test
    public void RankingTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_28() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        
        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.28a; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (SOHO).
//4.9.45	EE12.2_WHFA_9.28a; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void RankingTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_28a() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);
        
        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.28b; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IFHO).
//4.9.46	EE12.2_WHFA_9.28b; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IFHO).
    @Test
    public void RankingTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_28b() throws Exception {
        rankingsTab.openTab();
          rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.28c; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IRAT).
//4.9.47	EE12.2_WHFA_9.28c; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IRAT).
    @Test
    public void RankingTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_28c() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.28d; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (HSDSCH).
    //4.9.48	EE12.2_WHFA_9.28d; Rankings Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void RankingTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_28d() throws Exception {
        rankingsTab.openTab();
         rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingTerminalWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.29; Terminal Tab: Drilldown from Terminal total to HO failure type totals
//4.9.49	EE12.2_WHFA_9.29; Terminal Tab: Drilldown from Terminal total to HO failure type totals
    @Test
    public void TerminalTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_29() throws Exception {
        terminalTab.openTab();
          terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.29a; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (SOHO).
//4.9.50	EE12.2_WHFA_9.29a; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (SOHO).
    @Test
    public void TerminalTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_29a() throws Exception {
        terminalTab.openTab();
       terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.29b; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IFHO).
//4.9.51	EE12.2_WHFA_9.29b; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IFHO).
    @Test
    public void TerminalTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_29b() throws Exception {
        terminalTab.openTab();
          terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.29c; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IRAT).
    //4.9.52	EE12.2_WHFA_9.29c; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (IRAT).
    @Test
    public void TerminalTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_29c() throws Exception {
        terminalTab.openTab();
        terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    /*
     * PASS PASS PASS
     */
    // EE12.1_WHFA_9.29d; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (HSDSCH).
    //4.9.53	EE12.2_WHFA_9.29d; Terminal Tab: Drill down from Terminal HO failure totals to Detailed Event Analysis (HSDSCH).
    @Test
    public void TerminalTab_Drilldown_From_Terminal_Total_To_HO_Failure_Type_Totals_9_29d() throws Exception {
        terminalTab.openTab();
            terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS, BaseWcdmaHfa.subMenusTerminalRankingWCDMA);
        final List<String> windowHeaders = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalRankingWCDMAHandOverFailureWindow));
        terminalRankingWCDMAHOF.setTimeRange(getTimeRangeFromProperties());
        terminalRankingWCDMAHOF.clickTableCell(0, GuiStringConstants.TAC);
        final List<String> windowHeaders1 = terminalRankingWCDMAHOF.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysisWindow + WINDOW_HEADER + windowHeaders1);

        // emosjil changed
        // assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        //        windowHeaders1.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysisWindow));
    }

    // EE12.1_WHFA_9.30; Rankings Tab: Cause Code Rankings by Handover Type
//4.9.54	EE12.2_WHFA_9.30; Rankings Tab: Cause Code Rankings by Handover Type
    @Test
    public void HFA_RankingTab_Cause_Code_Rankings_On_Ranking_Tab_9_30() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow));

    }

    // EE12.1_WHFA_9.31; Network Tab: Cause Code Rankings by Handover Type 
    @Test
    public void HFA_NetworkTab_Cause_Code_Rankings_On_Netwok_Tab_9_31() throws Exception {
        networksTab.openTab();
        networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
        + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
        windowHeaders.containsAll(BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow));
    }

    /*
     * FRED FRED 03 august 2012
     */
    //EE12.2_WHFA_9.31a; Network Tab: Cause Code Analysis by controller -grid view.
    @Test
    public void WHFA_NetwokTab_CauseCodeAnalysisControllerGridView_Rankings_On_NetwokTab_9_31a() throws Exception {
       final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);       
        gridGraphviewRankingGroupTests.openGridView(); 		
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //EE12.2_WHFA_9.31b; Network Tab: Cause Code Analysis by controller -chart view.
   
    //EE12.2_WHFA_9.31c; Network Tab: Cause Code Analysis by controller group -grid view.
    @Test
    public void WHFA_NetwokTab_CauseCodeAnalysisControllerGroupGridView_Rankings_9_31c() throws Exception {
        final String groupController = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, groupController);       
        gridGraphviewRankingGroupTests.openGridView(); 
        
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
  
    //EE12.2_WHFA_9.31d; Network Tab: Cause Code Analysis by controller group-chart view.
    
    //EE12.2_WHFA_9.31e; Network Tab: Cause Code Analysis by Access Area -grid view.
    @Test
    public void WHFA_NetwokTab_CauseCodeAnalysis_AccessArea_GridView_Rankings_9_31e() throws Exception {
        final String accessAreaValue = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, accessAreaValue);       
        gridGraphviewRankingGroupTests.openGridView(); 
    
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
        
    //EE12.2_WHFA_9.31f; Network Tab: Cause Code Analysis by Access Area -chart view 
    @Test
    public void WHFA_NetwokTab_CauseCodeAnalysis_AccessAreaGroup_GridView_Rankings_9_31g() throws Exception {
        final String accessAreaGroupValue = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, accessAreaGroupValue);       
        gridGraphviewRankingGroupTests.openGridView(); 
    
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //EE12.2_WHFA_9.31g; Network Tab: Cause Code Analysis by Access Area Group -chart view 
       
    //EE12.2_WHFA_9.32; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals
    //EE12.0_HFA_9.32; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals
    @Test
    public void RankingTab_Drilldown_Failures_On_HFA_Cause_Code_Rankings_9_32() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, causeCodeRankingWCDMAHandOverFailure,GuiStringConstants.CAUSE_CODE, "FAILURE","CELL_CONGESTION","FAILURE_RELEASE_RC","FAILURE_RETAIN_RC","PROCEDURE_TIMEOUT");
         basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
    }
    
    //4.9.65	EE12.2_WHFA_9.32a; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals-SOHO
    @Test
    public void Drilldown_Failures_On_SOHO_Cause_Code_Rankings_On_Ranking_Tab_9_32() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, causeCodeRankingWCDMAHandOverFailure,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
      }
    
    //4.9.66	EE12.2_WHFA_9.32b; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals-IFHO
    @Test
    public void Drilldown_Failures_On_IFHO_Cause_Code_Rankings_On_Ranking_Tab_9_32() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, causeCodeRankingWCDMAHandOverFailure,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
      }
    
    //4.9.67	EE12.2_WHFA_9.32c; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals-IRAT
    @Test
    public void Drilldown_Failures_On_IRAT_Cause_Code_Rankings_On_Ranking_Tab_9_32() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, causeCodeRankingWCDMAHandOverFailure,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
      }
    
    
    //4.9.68	EE12.2_WHFA_9.32d; Rankings Tab: Drilldown from Cause Code failure totals to Sub-Cause Code failure totals-HSDSCH
    @Test
    public void Drilldown_Failures_On_HSDSCH_Cause_Code_Rankings_On_Ranking_Tab_9_32() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, BaseWcdmaHfa.subMenusRankingCauseCodeWCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCauseCodeRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
        basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
      }
    
    //EE12.2_WHFA_9.33; Network Tab: Drilldown to Cause Code Analysis by EVENT TYPE

    @Test
    public void Drilldown_Failures_On_HFA_Cause_Code_Rankings_On_Netwok_Tab_9_33() throws Exception {
        networksTab.openTab();
            networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties()); 
        basicWindowFunctionalityCheck(causeCodeRankingWCDMAHandOverFailure);
    }
    
    //4.9.70	EE12.2_WHFA_9.33.1; Network Tab: Drilldown from controller Cause Code Analysis to subcause code-grid view [SOHO]
    @Test
    public void Drilldown_NetwokTab_ControllerCauseCodeAnalysis_GridView_SOHO_Rankings_9_33_1() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);       
         gridGraphviewRankingGroupTests.openGridView();         
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
          drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
        
    //4.9.71	EE12.2_WHFA_9.33.2; Network Tab Drilldown from controller Cause Code Analysis to subcause code--grid view [IFHO]
    @Test
    public void Drilldown_Controller_Cause_Code_Rankings_On_NetwokTab_GridView_IFHO_9_33_2() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));

          drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
    
     //4.9.72	EE12.2_WHFA_9.33.3; Network Tab:  Drilldown from controller Cause Code Analysis to subcause code--grid view [IRAT]
    @Test
    public void Drilldown_Failures_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_IRAT_9_33_3() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
          
    //4.9.73	EE12.2_WHFA_9.33.4; Network Tab:  Drilldown controller Cause Code Analysis to subcause code-grid view [HSDSCH]
    @Test
    public void Drilldown_Failures_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_HSDSCH_9_33_4() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
          
    //4.9.74	EE12.2_WHFA_9.33.5; Network Tab:  Drilldown from controller Cause Code Analysis to subcause code-chart view [soho]
    
    //EE12.2_WHFA_9.33.6; Network Tab:  Drilldown controller Cause Code Analysis to subcause code-chart view [ifho]
    
    //EE12.2_WHFA_9.33.7; Network Tab:  Drilldown from controller Cause Code Analysis to subcause code-chart view [IRAT]
    
    //EE12.2_WHFA_9.33.8; Network Tab:  Drilldown from controller Cause Code Analysis to subcause code-chart view [HSDSCH]
    
    
    
     //EE12.2_WHFA_9.33.9; Network Tab:  Drilldown from Controller Group Cause Code Analysis to subcause code-grid view [SOHO]
    @Test
    public void Drilldown_ControllerGroup_CauseCode_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_SOHO_9_33_9() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         selenium.setSpeed("2000");
         gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
    
    //4.9.79	EE12.2_WHFA_9.33.10; Network Tab:  Drilldown from Controller  Group Cause Code Analysis to subcause code- grid view [IFHO]
    @Test
    public void Drilldown_ControllerGroup_CauseCode_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_IFHO_9_33_10() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         selenium.setSpeed("2000");
         gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
    
    
    //EE12.2_WHFA_9.33.11; Network Tab:  Drilldown from Controller Group Cause Code Analysis to subcause code -grid view [IRAT]
    @Test
    public void Drilldown_ControllerGroup_CauseCode_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_IRAT_9_33_11() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         selenium.setSpeed("2000");
         gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
    
    
    //4.9.81	EE12.2_WHFA_9.33.12; Network Tab:  Drilldown from Controller Group Cause Code Analysis to subcause code -grid view [HSDSCH]
    @Test
    public void Drilldown_ControllerGroup_CauseCode_On_HFA_Cause_Code_Rankings_On_NetwokTab_GridView_HSDSCH_9_33_12() throws Exception {
    	 final String controller = BaseWcdmaCfa.CONTROLLER_GROUP_VALUE;
         networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.CONTROLLER_GROUP, true, controller);       
         gridGraphviewRankingGroupTests.openGridView(); 		
         final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                 windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
         selenium.setSpeed("2000");
         gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
         drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                 GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
             gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
            basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
         }
    
    
    
    //4.9.82	EE12.2_WHFA_9.33.13; Network Tab:  Drilldown from Controller Group Cause Code Analysis to subcause code- chart view [soho]
    
    //EE12.2_WHFA_9.33.14; Network Tab:  Drilldown from Controller  Group Cause Code Analysis to subcause code- chart view [ifho]
    
    //EE12.2_WHFA_9.33.15; Network Tab:  Drilldown from Controller  Group Cause Code Analysis to subcause code- chart view [IRAT]
    
    //EE12.2_WHFA_9.33.16; Network Tab:  Drilldown from Controller Group Cause Code Analysis to subcause code- chart view [HSDSCH]
    
    
    
    
    //4.9.86	EE12.2_WHFA_9.33.17; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -grid view [SOHO]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroupGridView_Rankings_On_NetwokTab_SOHO_9_33_17() throws Exception {
        final String accessArea = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, accessArea);       
        gridGraphviewRankingGroupTests.openGridView(); 
        
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.87	EE12.2_WHFA_9.33.18; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -grid view [IFHO]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroupGridView_Rankings_On_NetwokTab_IFHO_9_33_18() throws Exception {
        final String accessArea = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, accessArea);       
        gridGraphviewRankingGroupTests.openGridView(); 
        
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.88	EE12.2_WHFA_9.33.19; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -grid view [IRAT]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroup_GridView_Rankings_On_NetwokTab_IRAT_9_33_19() throws Exception {
        final String accessArea = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, accessArea);       
        gridGraphviewRankingGroupTests.openGridView(); 
        
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.89	EE12.2_WHFA_9.33.20; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -grid view [HSDSCH]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroup_GridView_Rankings_On_NetwokTab__HSDSCH_9_33_20() throws Exception {
        final String accessArea = BaseWcdmaCfa.ACCESS_AREA_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA, true, accessArea);       
        gridGraphviewRankingGroupTests.openGridView();         
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.90	EE12.2_WHFA_9.33.21; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -chart view [soho]
    
    //EE12.2_WHFA_9.33.22; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -chart view [ifho]
    
    //EE12.2_WHFA_9.33.23; Network Tab:  Drilldown from Access Area Cause Code Analysis to subcause code -chart view [IRAT]
    
    //EE12.2_WHFA_9.33.24; Network Tab Drilldown from Access Area Cause Code Analysis to subcause code -chart view [HSDSCH]
    
    
    
    //4.9.94	EE12.2_WHFA_9.33.25; Network Tab Drilldown from Access Area Group Cause Code Analysis to subcause code -grid view [SOHO]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroup_GridView_Rankings_On_NetwokTab_SOHO_9_33_25() throws Exception {
        final String accessAreaGroup = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, accessAreaGroup);       
        gridGraphviewRankingGroupTests.openGridView();         
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.95	EE12.2_WHFA_9.33.26; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -grid view [IFHO]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_ControllerGroup_GridView_Rankings_On_NetwokTab_IFHO_9_33_26() throws Exception {
        final String accessAreaGroup = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, accessAreaGroup);       
        gridGraphviewRankingGroupTests.openGridView();         
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.96	EE12.2_WHFA_9.33.27; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -grid view [IRAT]
    @Test
    public void Drilldown_WHFA_CauseCodeAnalysis_Controller_GroupGridView_Rankings_On_NetwokTab_IRAT_9_33_27() throws Exception {
        final String accessAreaGroup = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, accessAreaGroup);       
        gridGraphviewRankingGroupTests.openGridView();         
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    //4.9.97	EE12.2_WHFA_9.33.28; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -grid view [HSDSCH]
    @Test
    public void Drilldown_WHFA_AccessAreaGroupCode_HSDSCH_AnalysisGridView_Rankings_On_NetwokTab_9_33_28() throws Exception {
        final String accessAreaGroup = BaseWcdmaCfa.ACCESS_AREA_GROUP_VALUE;
        networksTab.openControllerCauseCodeViewWindowForWCDMAHFA(NetworkType.ACCESS_AREA_GROUP, true, accessAreaGroup);       
        gridGraphviewRankingGroupTests.openGridView();         
        final List<String> windowHeaders = gridGraphviewRankingGroupTests.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultRankingGridViewWCDMAHFAWindow));
        gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
        drillDownOnParticularCell(GuiStringConstants.CAUSE_CODE, gridGraphviewRankingGroupTests,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
            gridGraphviewRankingGroupTests.setTimeRange(getTimeRangeFromProperties());
           basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
        basicWindowFunctionalityCheck(gridGraphviewRankingGroupTests);
     }
    
    
    //4.9.98	EE12.2_WHFA_9.33.29; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -chart view [soho]
    
    //EE12.2_WHFA_9.33.30; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -chart view [ifho]
    
    //EE12.2_WHFA_9.33.31; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -chart view [IRAT]
    
    //EE12.2_WHFA_9.33.32; Network Tab:  Drilldown from Access Area Group Cause Code Analysis to subcause code -chart view [HSDSCH]
    
    
    //4.9.102	EE12.2_WHFA_9.34 Verify that Cause Code / Subcause Code Combinations in the Whitelist group are excluded from Cause Code Failure Analysis
    @Test
    public void Drilldown_Failures_On_HFA_Cause_Code_Rankings_On_Netwok_Tab_9_34() throws Exception {    	
        networksTab.openTab();
        networksTab.openSubMenusFromStartMenu(NetworkTab.StartMenu.RANKINGS, BaseWcdmaHfa.subMenusNETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA);
        final List<String> windowHeaders = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow + WINDOW_HEADER
                + windowHeaders);
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow));
        causeCodeRankingWCDMAHandOverFailure.setTimeRange(getTimeRangeFromProperties());
        causeCodeRankingWCDMAHandOverFailure.clickTableCell(0, GuiStringConstants.CAUSE_CODE);
         final List<String> windowHeaders1 = causeCodeRankingWCDMAHandOverFailure.getTableHeaders();
         logger.log(Level.INFO, EXPECTED_HEADER + defaultEventFailureAnalysisWindowCauseCode + WINDOW_HEADER
                + windowHeaders1 );
         assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
               // windowHeaders1.containsAll(defaultEventFailureAnalysisWindowCauseCode));
        windowHeaders1.containsAll(BaseWcdmaHfa.defaultCCRankingWCDMAHandOverFailureWindow));
    }
    
    
    //4.9.103	EE12.2_WHFA_9.34b Verify View All Possible Cause Codes and View All Possible Sub Cause Codes 
        
    //4.9.104	EE12.2_WHFA_9.35; Rankings Tab: Verify that accurate HFA Access Area (TARGET CELL) Rankings are displayed.
   
    
    //4.9.105	EE12.2_WHFA_9.36; Network Tab: Verify that accurate HFA Access Area (TARGET CELL) Rankings are displayed.
    
    //4.9.106	EE12.2_WHFA_9.37; Rankings Tab: Drilldown from Target Cell total to HO failure type totals
    
    //4.9.107	EE12.2_WHFA_9.38; Network Tab: Drilldown from Target Cell total to HO failure type totals
    
    //4.9.108	EE12.2_WHFA_9.39; Ranking Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (IRAT)
    
    //4.9.109	EE12.2_WHFA_9.40; Network Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (IRAT)
    
    //4.9.110	EE12.2_WHFA_9.41; Ranking Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (IFHO)
    
    //4.9.111	EE12.2_WHFA_9.42; Network Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (IFHO)
    
    //4.9.112	EE12.2_WHFA_9.43; Ranking Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (HSDSCH)
    
    //4.9.113	EE12.2_WHFA_9.44; Network Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (HSDSCH)
    
    //4.9.114	EE12.2_WHFA_9.45; Ranking Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (SOHO)
    
    //4.9.115	EE12.2_WHFA_9.46; Network Tab: Drill down from Target Cell total per HO type to Detailed Event Analysis (SOHO)
    
    //4.9.116	EE12.2_WHFA_9.47; Services - Latency Settings
    
    //4.9.117	EE12.2_WHFA_9.48; Network Tab:  Drilldown from SubCause Code  PIE chart to DEA [Chart View]
    
    //4.9.118	EE12.2_WHFA_9.48.1; Network Tab: Drilldown from  controller SubCause Code  PIE chart to DEA[soho]
    
    //EE12.2_WHFA_9.48.2; Network Tab: Drilldown from controller subcause Code PIE chart to Detailed Event Analysis - [ifho]
    
    //EE12.2_WHFA_9.48.3; Network Tab:  Drilldown from controller subcause Code PIE chart to Detailed Event Analysis - [IRAT]
    
    //4.9.121	EE12.2_WHFA_9.48.4; Network Tab:  Drilldown from Controller Group subcause Code PIE chart to Detailed Event Analysis - [soho]
    
    //EE12.2_WHFA_9.48.5; Network Tab:   Drilldown from Controller Group subcause Code PIE chart to Detailed Event Analysis - [ifho]
    
    //EE12.2_WHFA_9.48.6; Network Tab:  Drilldown from Controller Group subcause Code PIE chart to Detailed Event Analysis - [IRAT]
    
    //4.9.124	EE12.2_WHFA_9.48.7; Network Tab:  Drilldown from Access Area subcause Code PIE chart to Detailed Event Analysis - [soho]
    
    //EE12.2_WHFA_9.48.8; Network Tab:   Drilldown from Access Area subcause Code PIE chart to Detailed Event Analysis - [ifho
    
    //EE12.2_WHFA_9.48.9; Network Tab:  Drilldown from Access Area subcause Code PIE chart to Detailed Event Analysis - [IRAT]
    
    //4.9.127	EE12.2_WHFA_9.48.10; Network Tab:  Drilldown from Access Area Group subcause Code PIE chart to Detailed Event Analysis - [soho]
    
    //EE12.2_WHFA_9.48.11; Network Tab:   Drilldown from Access Area Group subcause Code PIE chart to Detailed Event Analysis - [ifho]
    
    //EE12.2_WHFA_9.48.12; Network Tab:  Drilldown from Access Area Group subcause Code PIE chart to Detailed Event Analysis - [IRAT]
    
    //4.9.130	EE12.2_WHFA_9.49; Network Tab:  Drilldown from   subcause Code to Detailed Event Analysis -grid view 
    
    //4.9.131	EE12.2_WHFA_9.49.1; Network Tab:  Drilldown from Access Area subcause Code to Detailed Event Analysis –SOHO [grid view]
    
    //4.9.132	EE12.2_WHFA_9.49.2; Network Tab:  Drilldown from Access Area subcause Code to Detailed Event Analysis -IFHO [grid view]
    
    //4.9.133	EE12.2_WHFA_9.49.3; Network Tab:  Drilldown from Access Area subcause Code to Detailed Event Analysis -IRAT [grid view]
    
    //4.9.134	EE12.2_WHFA_9.49.4; Network Tab:  Drilldown from Access Area Group subcause Code to Detailed Event Analysis -SOHO [grid view]
    
    //4.9.135	EE12.2_WHFA_9.49.5; Network Tab:  Drilldown from Access Area Group subcause Code to Detailed Event Analysis -IFHO [grid view]
    
    //4.9.136	EE12.2_WHFA_9.49.6; Network Tab:  Drilldown from Access Area Group subcause Code to Detailed Event Analysis -IRAT [grid view]
    
    //4.9.137	EE12.2_WHFA_9.49.7; Network Tab:  Drilldown from Controller subcause Code to Detailed Event Analysis –SOHO [grid view]
    
    //4.9.138	EE12.2_WHFA_9.49.8; Network Tab:  Drilldown from Controller subcause Code to Detailed Event Analysis -IFHO [grid view]
    
    //4.9.139	EE12.2_WHFA_9.49.9; Network Tab:  Drilldown from Controller subcause Code to Detailed Event Analysis -IRAT [grid view]
    
    //4.9.140	EE12.2_WHFA_9.49.10; Network Tab:  Drilldown from Controller Group subcause Code to Detailed Event Analysis -SOHO [grid view]
    
    //4.9.141	EE12.2_WHFA_9.49.11; Network Tab:  Drilldown from Controller Group subcause Code to Detailed Event Analysis -IFHO [grid view]
    
    //4.9.142	EE12.2_WHFA_9.49.12; Network Tab:  Drilldown from Controller Group subcause Code to Detailed Event Analysis -IRAT [grid view]
    
    //4.10	Test Group 10: CPI & Documentation
    //4.10.1	EE12.2_WHFA_10.1; ENIQ Events User Guide
    
    //4.10.2	EE12.2_WHFA_10.2; Events System Administration Guide
    
    //4.10.3	EE12.2_WHFA_10.3; Summary of External Interfaces
    
    //4.10.4	EE12.2_WHFA_10.4; Events AdminUI System Administration Guide
    
    //4.10.5	EE12.2_WHFA_10.5; Events Command Line Interface User Guide
    
    //4.10.6	EE12.2_WHFA_10.6; TP descriptions for WCDMA HFA
    
    //4.10.7	EE12.2_WHFA_10.7; Events Network Impact Report
    
    //4.10.8	EE12.2_WHFA_10.8; Events System Description
    
    //4.10.9	EE12.2_WHFA_10.9; Events Feature Mapping Document
    
    //4.11	Test Group 11: Restart Tests
    //4.11.1	EE12.2_WHFA_11.1; Standalone Server Restart
    
    //4.11.1	EE12.2_WHFA_11.1; Standalone Server Restart
    
    //4.11.2	EE12.2_WHFA_11.2; Glassfish Restart
    
    //4.11.3	EE12.2_WHFA_11.3; Scheduler Restart
    
    //4.11.4	EE12.2_WHFA_11.4; Engine Restart
    
    //4.11.5	EE12.2_WHFA_11.5; Webserver Restart
    
    //4.11.6	EE12.2_WHFA_11.6; License Manager Restart
    
    //4.11.7	EE12.2_WHFA_11.7; Stop / Start all Services 


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
                commonWindow.refresh();
                
                final int pageCount = commonWindow.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    commonWindow.clickNextPage();
                }
            }
        }

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
    
    }
}
