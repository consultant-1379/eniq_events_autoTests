/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.BaseSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author ekeviry
 * @since 2011
 * 
 */

public class SubscriberTestGroupWcdmaCfa extends BaseSubscriber {

    @Autowired
    @Qualifier("subscriberCallSetupFailuresRanking")
    private CommonWindow subscriberCallSetupFailuresRankingWindow;

    @Autowired
    @Qualifier("subscriberCallDropsRanking")
    private CommonWindow subscriberCallDropsRankingWindow;

    @Autowired
    @Qualifier("subscriberMultipleRecurringFailureRanking")
    private CommonWindow multipleFailureRankingWindow;

    @Autowired
    @Qualifier("subscriberCallFailureEventAnalysis")
    private CommonWindow subCallFailureEventAnalysisWindow;

    @Autowired
    @Qualifier("subscriberFailureOverviewWcdmaCfa")
    private SubscriberOverviewWindow subFailureOverviewWcdmaCfaWindow;

    private static final String TIME_BUTTON_XPATH = "*[contains(@id, 'selenium_tag_BaseWindow_SUBSCRIBER_RANKING_MULTIPLE_RECURRING_FAILURES')]table[@id='btnTime']button";

    // Launch Button Sub Menus yet
    private final List<SubscriberTab.SubStartMenu> subMenuEventRankingWCDMACallFailure = new ArrayList<SubscriberTab.SubStartMenu>(
            Arrays.asList(SubscriberTab.SubStartMenu.EVENT_RANKING,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_CFA_MENU_ITEM));

    private final List<SubscriberTab.SubStartMenu> subMenuSubscriberOverviewWcdmaCfa = Arrays.asList(
            SubscriberTab.SubStartMenu.SUB_OVERVIEW_RAN, SubscriberTab.SubStartMenu.SUB_OVERVIEW_RAN_WCDMA,
            SubscriberTab.SubStartMenu.SUB_OVERVIEW_RAN_WCDMA_CFA);

    private final List<SubscriberTab.SubStartMenu> subMenuSubscriberOverviewDisabledRan = Arrays
            .asList(SubscriberTab.SubStartMenu.SUB_OVERVIEW_RAN);

    private final List<SubscriberTab.SubStartMenu> subMenuEventAnalysisWcdmaCfa = Arrays.asList(
            SubscriberTab.SubStartMenu.SUB_EVENT_ANALYSIS_RAN, SubscriberTab.SubStartMenu.SUB_EVENT_ANALYSIS_WCDMA,
            SubscriberTab.SubStartMenu.SUB_EVENT_ANALYSIS_WCDMA_CFA);

    // Default Column Headers
    private final List<String> defaultSubscriberCallFailureColumns = Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.IMSI, GuiStringConstants.FAILURES);

    private final List<String> defaultIMSIEventAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.SEVERITY_INDICATOR_CFA,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM,
            GuiStringConstants.RSCP_CELL_ONE_DBM, GuiStringConstants.DISCONNECTION_CODE,
            GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);

    private final List<String> defaultIMSIGroupAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    @Override
    @Before
    public void setUp() {
        super.setUp();

        subscriberTab.openTab();
    }

    @Test
    public void subscriberTabVerifyWcdmaCfaSubscriberRankings_5_7_1a() throws PopUpException {

        subMenuEventRankingWCDMACallFailure
                .add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_CALL_SETUP_FAILURES);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        final List<String> actualWindowHeaders = subscriberCallSetupFailuresRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultSubscriberCallFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultSubscriberCallFailureColumns));
    }

    @Test
    public void subscriberTabVerifyWcdmaCfaSubscriberRankings_5_7_1b() throws PopUpException {

        subMenuEventRankingWCDMACallFailure.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_CALL_DROPS);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        final List<String> actualWindowHeaders = subscriberCallDropsRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultSubscriberCallFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultSubscriberCallFailureColumns));
    }

    @Test
    public void subscriberTabFromWcdmaCfaSubscriberRankingVerifyDrilldownToImsiEventAnalysis_5_7_2a()
            throws PopUpException, NoDataException, InterruptedException {

        subMenuEventRankingWCDMACallFailure
                .add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_CALL_SETUP_FAILURES);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        subscriberCallSetupFailuresRankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        subscriberCallSetupFailuresRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        //emosjil
        //final List<String> actualWindowHeaders = subscriberCallSetupFailuresRankingWindow.getTableHeaders();
        
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.RAB_TYPE);
        	optionalHeaders.add(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
        	optionalHeaders.add(GuiStringConstants.UL_INT_CELLONE_DBM);
        	optionalHeaders.add(GuiStringConstants.RSCP_CELL_ONE_DBM);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_CODE);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_SUBCODE);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_DESC);
        	optionalHeaders.add(GuiStringConstants.TRIGGER_POINT);
        	optionalHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
            subscriberCallSetupFailuresRankingWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders);
        }else{
        	//nothing yet
        }
        
        final List<String> actualWindowHeaders = subscriberCallSetupFailuresRankingWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + BaseWcdmaCfa.callSetupImsiEventAnalysisColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaCfa.callSetupImsiEventAnalysisColumns));
    }

    @Test
    public void subscriberTabFromWcdmaCfaSubscriberRankingVerifyDrilldownToImsiEventAnalysis_5_7_2b()
            throws PopUpException, NoDataException, InterruptedException {

        subMenuEventRankingWCDMACallFailure.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_CALL_DROPS);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        subscriberCallDropsRankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        subscriberCallDropsRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        //emosjil
        //final List<String> actualWindowHeaders = subscriberCallDropsRankingWindow.getTableHeaders();
        
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.RAB_TYPE);
        	optionalHeaders.add(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
        	optionalHeaders.add(GuiStringConstants.UL_INT_CELLONE_DBM);
        	optionalHeaders.add(GuiStringConstants.RSCP_CELL_ONE_DBM);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_CODE);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_SUBCODE);
        	optionalHeaders.add(GuiStringConstants.DISCONNECTION_DESC);
        	optionalHeaders.add(GuiStringConstants.TRIGGER_POINT);
        	optionalHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
        	subscriberCallDropsRankingWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders);
        }else{
        	//nothing yet
        }
        
        final List<String> actualWindowHeaders = subscriberCallDropsRankingWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + BaseWcdmaCfa.callDropsImsiEventAnalysisColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaCfa.callDropsImsiEventAnalysisColumns));
    }

    @Test
    public void subscriberTabVerifyWcdmaCfaSubscriberRankingForRecurringFailures_5_7_3() throws PopUpException {

        final List<String> defaultRecurringFailureColumns = Arrays.asList(GuiStringConstants.RANK,
                GuiStringConstants.IMSI, GuiStringConstants.FAILURES, GuiStringConstants.CONTROLLER,
                GuiStringConstants.ACCESS_AREA);

        subMenuEventRankingWCDMACallFailure
                .add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_RECURRING_FAILURES);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        final List<String> actualWindowHeaders = multipleFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultRecurringFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultRecurringFailureColumns));

        assertFalse(FailureReasonStringConstants.TIME_BUTTON_NOT_DISABLED, selenium.isElementPresent(TIME_BUTTON_XPATH));
    }

    @Test
    public void subscriberTabVerifyWcdmaCfaSubscriberRankingForRecurringFailures_5_7_4() throws PopUpException,
            NoDataException {

        final List<String> defaultIMSIEventAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
                GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
                GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CAUSE_VALUE,
                GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.SEVERITY_INDICATOR_CFA);

        subMenuEventRankingWCDMACallFailure
                .add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_CFA_RECURRING_FAILURES);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                subMenuEventRankingWCDMACallFailure);

        final String imsi = multipleFailureRankingWindow.getTableData(0, 1);
        final String accessArea = multipleFailureRankingWindow.getTableData(0, 4);
        final String controller = multipleFailureRankingWindow.getTableData(0, 3);

        final String eventAnalysisWindowName = imsi + GuiStringConstants.DASH + accessArea + GuiStringConstants.DASH
                + controller + GuiStringConstants.DASH + "IMSI Event Analysis for Recurring Failures";

        multipleFailureRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);
        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowName,
                selenium.isTextPresent(eventAnalysisWindowName));

        final List<String> actualWindowHeaders = multipleFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

        assertFalse(FailureReasonStringConstants.TIME_BUTTON_NOT_DISABLED, selenium.isElementPresent(TIME_BUTTON_XPATH));
    }

    //@Test
    public void subscriberTabVerifySubscriberOverviewAnalysisOnTheBasisOfImsi_5_7_5a_Ptmsi() throws PopUpException,
            InterruptedException {
        final boolean isSubMenuItemDisabled = subscriberTab.subscriberOverviewSubMenuWithDisabledItem(
                subMenuSubscriberOverviewDisabledRan, ImsiMenu.PTMSI, BaseWcdmaCfa.PTMSI_VALUE);

        assertTrue("Launch menu sub item NOT disabled", isSubMenuItemDisabled == true);
    }

    // @Test
    public void subscriberTabVerifySubscriberOverviewAnalysisOnTheBasisOfIMSI_5_7_5b_Msisdn() throws PopUpException,
            InterruptedException {
        final boolean isSubMenuItemDisabled = subscriberTab.subscriberOverviewSubMenuWithDisabledItem(
                subMenuSubscriberOverviewDisabledRan, ImsiMenu.MSISDN, BaseWcdmaCfa.MSISDN_VALUE);

        assertTrue("Launch menu sub item NOT disabled", isSubMenuItemDisabled == true);
    }

    @Test
    public void subscriberTabFromSubscriberOverviewAnalysisDrilldownToFailureAnalysisForImsi_5_7_6()
            throws PopUpException, InterruptedException {

        subscriberTab.openSubscriberOverviewWindowSubMenu(subMenuSubscriberOverviewWcdmaCfa, ImsiMenu.IMSI,
                BaseWcdmaCfa.IMSI_VALUE);
        pause(2000);

        subFailureOverviewWcdmaCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile();
        pause(2000);

        final String subOverviewWindow = BaseWcdmaCfa.IMSI_VALUE + GuiStringConstants.DASH
                + GuiStringConstants.SUB_OVERVIEW_WINDOW_TITLE;
        assertTrue(GuiStringConstants.ERROR_LOADING + subOverviewWindow, selenium.isTextPresent(subOverviewWindow));

        verifyChartWindowTextValues();

        subFailureOverviewWcdmaCfaWindow.drillOnChartObject(1);

        //emosjil
        //final List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version == 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.IMSI);
        	subFailureOverviewWcdmaCfaWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Default");
        }else{
        	//nothing yet
        }
        
        final List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

        //emosjil
        //subFailureOverviewWcdmaCfaWindow.clickViewSubMenu(ViewMenu.CALL_FAILURE_ANALYSIS);
        subFailureOverviewWcdmaCfaWindow.toggleGraphToGrid();
        pause(2000);

        subFailureOverviewWcdmaCfaWindow.drillOnChartObject(2);
        
        if(version == 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.IMSI);
        	subFailureOverviewWcdmaCfaWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Default");
        }else{
        	//nothing yet
        }

        final List<String> actualWindowHeaders2 = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders2);
        //assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subFailureOverviewWcdmaCfaWindow.getTableHeaders().equals(defaultIMSIEventAnalysisColumns));
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders2.equals(defaultIMSIEventAnalysisColumns));
    }

    @Test
    public void subscriberTabFromSubscriberOverviewAnalysisDrilldownToFailureAnalysisForImsiGroup_5_7_7()
            throws PopUpException, InterruptedException {
        final List<String> defaultSubOverviewGridColumns = Arrays.asList(GuiStringConstants.EVENT_FAILURES,
                GuiStringConstants.EVENT_NAME);

        subscriberTab.openSubscriberOverviewWindowSubMenu(subMenuSubscriberOverviewWcdmaCfa, ImsiMenu.IMSI_GROUP,
                BaseWcdmaCfa.IMSI_GROUP_VALUE);
        pause(2000);

        subFailureOverviewWcdmaCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile();
        pause(2000);

        verifyChartWindowTextValues();

        subFailureOverviewWcdmaCfaWindow.clickRefreshButton();

        subFailureOverviewWcdmaCfaWindow.toggleGraphToGrid();
        
        //emosjil
        //final String gridWindowName = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + "Subscriber Overview - Failure Analysis";
        final String gridWindowName;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	gridWindowName = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_Group_OVERVIEW_WINDOW_TITLE;
        }else{
        	gridWindowName = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + "Subscriber Overview - Failure Analysis";
        }
        
        assertTrue("Error toggling to Grid View", selenium.isTextPresent(gridWindowName));

        final List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultSubOverviewGridColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultSubOverviewGridColumns));

        subFailureOverviewWcdmaCfaWindow.toggleGridToGraph();

        //emosjil
        //final String imsiGroupSubOverview = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_OVERVIEW_WINDOW_TITLE;
        final String imsiGroupSubOverview = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_Group_OVERVIEW_WINDOW_TITLE;
        assertTrue("Error toggling to Graph View", selenium.isTextPresent(imsiGroupSubOverview));
      
        //12.2
        assertFalse(
                "Subscriber Details Button NOT disabled!",
                selenium.isElementPresent("//*[@id='selenium_tag_BaseWindow_SUB_BI_FAILED_EVENTS_CFA']//div[@id='btnSubscriberDetailsWcdmaCFA']"));
        //12.1
        assertFalse(
                "Subscriber Details Button NOT disabled!",
                selenium.isElementPresent("//*[@id='selenium_tag_BaseWindow_SUB_BI_FAILED_EVENTS_CFA']//table[@id='btnSubscriberDetailsWcdmaCFA']//input"));
    }

    @Test
    public void subscriberTabVerifySubscriberOverviewAnalysisOnTheBasisOfImsiGroup_5_7_8() throws PopUpException,
            InterruptedException {

        subscriberTab.openSubscriberOverviewWindowSubMenu(subMenuSubscriberOverviewWcdmaCfa, ImsiMenu.IMSI_GROUP,
                BaseWcdmaCfa.IMSI_GROUP_VALUE);
        pause(2000);

        subFailureOverviewWcdmaCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile();
        pause(2000);

        //emosjil
        //final String imsiGroupSubOverview = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_OVERVIEW_WINDOW_TITLE;
        final String imsiGroupSubOverview;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	imsiGroupSubOverview = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_Group_OVERVIEW_WINDOW_TITLE;
        }else{
        	imsiGroupSubOverview = BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH + GuiStringConstants.SUB_OVERVIEW_WINDOW_TITLE;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + imsiGroupSubOverview,
                selenium.isTextPresent(imsiGroupSubOverview));

        subFailureOverviewWcdmaCfaWindow.drillOnChartObject(1);
        
        if(version >= 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.IMSI);
        	subFailureOverviewWcdmaCfaWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Default");
        }else{
        	//nothing yet
        }

        List<String> actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

        //emosjil
        //subFailureOverviewWcdmaCfaWindow.clickViewSubMenu(ViewMenu.CALL_FAILURE_ANALYSIS);
        subFailureOverviewWcdmaCfaWindow.toggleGraphToGrid();
        pause(2000);

        subFailureOverviewWcdmaCfaWindow.drillOnChartObject(2);
        
        //emosjil
        if(version >= 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.IMSI);
        	subFailureOverviewWcdmaCfaWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Default");
        }else{
        	//nothing yet
        }

        actualWindowHeaders = subFailureOverviewWcdmaCfaWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));
    }

    @Test
    public void subscriberTabVerifySubscriberEventAnalysisOnTheBasisOfImsi_5_7_9() throws InterruptedException,
            PopUpException {

        subscriberTab.openEventAnalysisWindowSubMenu(subMenuEventAnalysisWcdmaCfa, ImsiMenu.IMSI,
                BaseWcdmaCfa.IMSI_VALUE);

        final List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));
    }

    @Test
    public void subscriberTabVerifySubscriberGroupEventAnalysisView_5_7_10() throws InterruptedException,
            PopUpException {

        subscriberTab.openEventAnalysisWindowSubMenu(subMenuEventAnalysisWcdmaCfa, ImsiMenu.IMSI_GROUP,
                BaseWcdmaCfa.IMSI_GROUP_VALUE);

        final List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIGroupAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIGroupAnalysisColumns));
    }

    @Test
    public void subscriberTabFromSubscriberGroupEventAnalysisDrilldownToFailureAnalysis_5_7_11()
            throws InterruptedException, PopUpException, NoDataException {

        final String groupFailureWindowTitle = "IMSI Group Failure Analysis";

        subscriberTab.openEventAnalysisWindowSubMenu(subMenuEventAnalysisWcdmaCfa, ImsiMenu.IMSI_GROUP,
                BaseWcdmaCfa.IMSI_GROUP_VALUE);

        subCallFailureEventAnalysisWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        subCallFailureEventAnalysisWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        assertTrue(
                GuiStringConstants.ERROR_LOADING + BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH
                        + groupFailureWindowTitle,
                selenium.isTextPresent(BaseWcdmaCfa.IMSI_GROUP_VALUE + GuiStringConstants.DASH
                        + groupFailureWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	List<String> optionalHeaders = new ArrayList<String>();
        	optionalHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
        	subCallFailureEventAnalysisWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Default");
        }else{
        	//nothing yet
        }

        List<String> actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + BaseWcdmaCfa.callDropsFailedEventAnalysisColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaCfa.callDropsFailedEventAnalysisColumns));

        subCallFailureEventAnalysisWindow.clickBackwardNavigation();

        subCallFailureEventAnalysisWindow.clickTableCell(1, 1, GuiStringConstants.GRID_CELL_LINK);

        actualWindowHeaders = subCallFailureEventAnalysisWindow.getTableHeaders();
        if(version >= 13.0){
        	actualWindowHeaders.remove(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
        	actualWindowHeaders.remove(GuiStringConstants.UL_INT_CELLONE_DBM);
        	actualWindowHeaders.remove(GuiStringConstants.RSCP_CELL_ONE_DBM);
        	actualWindowHeaders.remove(GuiStringConstants.DISCONNECTION_CODE);
        	actualWindowHeaders.remove(GuiStringConstants.DISCONNECTION_SUBCODE);
        	actualWindowHeaders.remove(GuiStringConstants.DISCONNECTION_DESC);
        }else{
        	//nothing yet
        }
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + BaseWcdmaCfa.callSetupFailedEventAnalysisColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(BaseWcdmaCfa.callSetupFailedEventAnalysisColumns));
    }

    @Test
    public void subscriberTabVerifyThatSubscriberIconWorksInSubscriberOverviewAnalysisOnTheBasisOfImsi_5_7_12()
            throws InterruptedException, PopUpException, NoDataException {

        final String[] subscriberDetailsFields = { "Mapped MSISDN", "Home Country", "Mobile Network Operator",
                "Roaming Status", "Last Cell", "Last RNC", "First Event Date", "Last Event Date" };

        subscriberTab.openSubscriberOverviewWindowSubMenu(subMenuSubscriberOverviewWcdmaCfa, ImsiMenu.IMSI,
                BaseWcdmaCfa.IMSI_VALUE);
        pause(3000);

        subFailureOverviewWcdmaCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile();
        pause(3000);

        subFailureOverviewWcdmaCfaWindow.openSubscriberDetailsDialogWcdmaCfa();
        selenium.waitForPageLoadingToComplete();
        pause(3000);

        for (int i = 0; i < subscriberDetailsFields.length; i++) {
            assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, selenium.isTextPresent(subscriberDetailsFields[i]));
        }
    }

    // Private method to verify text labels on Subscriber Overview charts
    private void verifyChartWindowTextValues() {
        final String[] chartWindowText = { "Number of Events", "Event", "Call Setup Failures", "Call Drops",
                "Event Failures", "Failure Event Analysis", "Drag To Zoom" };

        for (int i = 0; i < chartWindowText.length; i++) {
            assertTrue(GuiStringConstants.TEXT_NOT_FOUND + chartWindowText[i],
                    selenium.isTextPresent(chartWindowText[i]));
        }
    }
}
