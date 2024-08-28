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
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
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

public class RankingTestGroupWcdmaCfa extends BaseWcdmaCfa {

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("wcdmaCFARANRanking")
    private CommonWindow wcdmaCallFailureRANRankings;

    @Autowired
    @Qualifier("accessAreaCFARanking")
    private CommonWindow accessAreaCFARankingWindow;

    @Autowired
    @Qualifier("subscriberCallSetupFailuresRanking")
    private CommonWindow subscriberCallSetupFailureRankingWindow;

    @Autowired
    @Qualifier("subscriberCallDropsRanking")
    private CommonWindow subscriberCallDropsRankingWindow;

    @Autowired
    @Qualifier("subscriberMultipleRecurringFailureRanking")
    private CommonWindow recurringFailureRankingWindow;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallDrops")
    private CommonWindow causeCodeCallDropsWindow;

    @Autowired
    @Qualifier("rankingsCauseCodeWcdmaCallSetupFailures")
    private CommonWindow causeCodeCallFailureWindow;

    @Autowired
    @Qualifier("detailedEventAnalysisWcdmaCfaCallDrop")
    private CommonWindow detailedEventAnalysisCallDropWindow;

    @Autowired
    @Qualifier("detailedEventAnalysisWcdmaCfaCallSetup")
    private CommonWindow detailedEventAnalysisCallSetupWindow;

    @Autowired
    @Qualifier("rankingsTerminalWcdmaCfa")
    private CommonWindow rankingsTerminalWcdmaCfa;

    private static final String timeButtonXPath = "*[contains(@id, 'selenium_tag_BaseWindow_SUBSCRIBER_RANKING_MULTIPLE_RECURRING_FAILURES')]table[@id='btnTime']button";

    // Launch Button Sub Menus

    private final List<RankingsTab.SubStartMenu> subMenuCFARNCRANRankings = Arrays.asList(
            RankingsTab.SubStartMenu.RANKINGS_RNC, RankingsTab.SubStartMenu.RANKINGS_RNC_RAN,
            RankingsTab.SubStartMenu.RANKINGS_RNC_WCDMACFA);

    private final List<RankingsTab.SubStartMenu> subMenuAccessAreaRANWCDMACallFailure = Arrays.asList(
            RankingsTab.SubStartMenu.EVENT_RANKING_ECELL, RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_RAN,
            RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA,
            RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_RAN_WCDMA_CFA);

    private final List<RankingsTab.SubStartMenu> subMenuSubscriberRanWcdmaCfa = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER,
                    RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA));

    private final List<RankingsTab.SubStartMenu> subMenuCauseCodeWcdma = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE,
                    RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_RAN, RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA));

    private final List<RankingsTab.SubStartMenu> subMenuTerminalWcdmaCfa = Arrays.asList(
            RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA_PARENT, RankingsTab.SubStartMenu.RANKING_TERMINAL_RAN,
            RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA, RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA_CFA);

    // Default Column Headers

    private final List<String> defaultSubscriberCallFailureColumns = Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.IMSI, GuiStringConstants.FAILURES);

    private final List<String> defaultRecurringFailureRankingColumns = Arrays.asList(GuiStringConstants.RANK,
            GuiStringConstants.IMSI, GuiStringConstants.FAILURES, GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA);

    @Override
    @Before
    public void setUp() {
        super.setUp();

        rankingsTab.openTab();

        //rankingsTab.setPacketSwitchedMenu(PacketSwitchedMenuOptions.CORE_DATA);
    }

    @Test
    public void rankingsTabWcdmaCfaTabVerifyRNCRankingView_5_8_1() throws PopUpException, NoDataException {
        final List<String> defaultRNCRankingWCDMACallFailureWindowColumns = Arrays.asList(GuiStringConstants.RANK,
                GuiStringConstants.RAN_VENDOR, GuiStringConstants.RNC, GuiStringConstants.FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCFARNCRANRankings);
        
        // emosjil changed title to work with 12.2 and 13.0
        // final String expectedWindowTitle = "RNC WCDMA Call Failure : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "RNC WCDMA Call Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "RNC WCDMA Call Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + defaultRNCRankingWCDMACallFailureWindowColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultRNCRankingWCDMACallFailureWindowColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaRncRankingViewVerifyDrilldownToControllerEventAnalysisView_5_8_2()
            throws PopUpException, NoDataException {
        final List<String> defaultControllerEventAnalysisWindowColumns = Arrays.asList(GuiStringConstants.RAN_VENDOR,
                GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
                GuiStringConstants.IMPACTED_SUBSCRIBERS);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCFARNCRANRankings);

        wcdmaCallFailureRANRankings.setTimeRangeBasedOnSeleniumPropertiesFile();

        wcdmaCallFailureRANRankings.clickTableCell(0, 2, GuiStringConstants.GRID_CELL_LINK);

        final String expectedWindowTitle = "Controller Event Analysis";
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultControllerEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultControllerEventAnalysisWindowColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaRncRankingViewVerifyDrilldownToFailedEventAnalysisView_5_8_3()
            throws PopUpException, NoDataException, InterruptedException {

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCFARNCRANRankings);

        wcdmaCallFailureRANRankings.setTimeRangeBasedOnSeleniumPropertiesFile();

        wcdmaCallFailureRANRankings.clickTableCell(0, 2, GuiStringConstants.GRID_CELL_LINK);

        wcdmaCallFailureRANRankings.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);

        assertTrue(GuiStringConstants.ERROR_LOADING + GuiStringConstants.FAILED_EVENT_ANALYSIS,
                selenium.isTextPresent(GuiStringConstants.FAILED_EVENT_ANALYSIS));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();

    	List<String> optionalHeaders = new ArrayList<String>();
    	optionalHeaders.add(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
    	optionalHeaders.add(GuiStringConstants.UL_INT_CELLONE_DBM);
    	optionalHeaders.add(GuiStringConstants.RSCP_CELL_ONE_DBM);
    	optionalHeaders.add(GuiStringConstants.DISCONNECTION_CODE);
    	optionalHeaders.add(GuiStringConstants.DISCONNECTION_SUBCODE);
    	optionalHeaders.add(GuiStringConstants.DISCONNECTION_DESC);
    	
        if(version >= 13.0){
        	wcdmaCallFailureRANRankings.uncheckOptionalHeaderCheckBoxes(optionalHeaders
        			, "Call Failure Details", "Event Summary Details", "Active Set Measurements");
        }else{
        	wcdmaCallFailureRANRankings.checkInOptionalHeaderCheckBoxes(optionalHeaders
        			, "Call Failure Details", "Event Summary Details", "Active Set Measurements");
        }
        
        final List<String> actualWindowHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));
        
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaAAccessAreaCellRankingViews_5_8_4() throws PopUpException, NoDataException {

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuAccessAreaRANWCDMACallFailure);
        
        //emosjil
        //final String expectedWindowTitle = "Access Area WCDMA Call Failure : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Access Area WCDMA Call Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "Access Area WCDMA Call Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaCFAColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultAccessAreaCFAColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaAccessAreaRankingsVerifyDrilldownToAccessAreaEventAnalysisView_5_8_5()
            throws PopUpException, NoDataException {

        rankingsTab
                .openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuAccessAreaRANWCDMACallFailure);

        accessAreaCFARankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String accessArea = accessAreaCFARankingWindow.getTableData(0, 3);

        accessAreaCFARankingWindow.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);

        final String expectedWindowTitle = accessArea + " - Access Area - Event Analysis Summary";
        assertTrue(GuiStringConstants.ERROR_LOADING + accessArea + expectedWindowTitle,
                selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultAccessAreaEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultAccessAreaEventAnalysisColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaAccessAreaRankingsVerifyDrilldownToFailureAnalysisViews_5_8_6()
            throws PopUpException, NoDataException, InterruptedException {

        rankingsTab
                .openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuAccessAreaRANWCDMACallFailure);

        accessAreaCFARankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        accessAreaCFARankingWindow.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);

        accessAreaCFARankingWindow.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);

        //emosjil
        //final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + WCDMA_CALL_FAILURE;
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS + DASH + WCDMA_CALL_FAILURE;
        }else{
        	expectedWindowTitle = "RNC05-1-1" + GuiStringConstants.DASH + GuiStringConstants.ACCESS_AREA
        			+ GuiStringConstants.DASH + GuiStringConstants.CALL_FAILURE_ANALYSIS;
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        
        List<String> optionalHeaders = new ArrayList<String>();
        optionalHeaders.add(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
        optionalHeaders.add(GuiStringConstants.UL_INT_CELLONE_DBM);
        optionalHeaders.add(GuiStringConstants.RSCP_CELL_ONE_DBM);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_CODE);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_SUBCODE);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_DESC);
        
        if(version >= 13.0){
        	accessAreaCFARankingWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, "Active Set Measurements");
        }else{
        }
        
        final List<String> actualWindowHeaders = accessAreaCFARankingWindow.getTableHeaders();
        
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultFailedEventAnalysisWindowColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(defaultFailedEventAnalysisWindowColumns));
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaCauseCodeRankingViews_5_8_7a() throws PopUpException {
        subMenuCauseCodeWcdma.add(RankingsTab.SubStartMenu.RANKING_CC_CFA_CALL_DROPS);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCauseCodeWcdma);

        //emosjil refactored to work with both 12.2 and 13.0
        //final String expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Drops : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Drops : Event Ranking";
        }else{
        	expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Drops Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeCallDropsWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaCauseCodeRankingViews_5_8_7b() throws PopUpException {
        subMenuCauseCodeWcdma.add(RankingsTab.SubStartMenu.RANKING_CC_CFA_CALL_SETUP_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCauseCodeWcdma);

        //emosjil refactored to work with both 12.2 and 13.0
        //final String expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Drops : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Setup Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "Cause Code WCDMA Call Failure by Call Setup Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = causeCodeCallFailureWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultCauseCodeWcdmaColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultCauseCodeWcdmaColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheFailureAnalysisViews_5_8_8a()
            throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(RankingsTab.SubStartMenu.RANKING_CC_CFA_CALL_DROPS);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCauseCodeWcdma);

        causeCodeCallDropsWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String causeCodeDescription = causeCodeCallDropsWindow.getTableData(0, 1);
        final String causeCodeId = causeCodeCallDropsWindow.getTableData(0, 2);
        final String expectedWindowTitle = causeCodeDescription + GuiStringConstants.COMMA_SPACE + causeCodeId
                + GuiStringConstants.DASH + "Sub Cause Code Analysis for WCDMA CFA Call Drops";

        causeCodeCallDropsWindow.clickTableCell(0, 1, GRID_CELL_LAUNCHER_LINK);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = detailedEventAnalysisCallDropWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaCauseCodeRankingVerifyDrilldownToTheFailureAnalysisViews_5_8_8b()
            throws PopUpException, NoDataException {
        subMenuCauseCodeWcdma.add(RankingsTab.SubStartMenu.RANKING_CC_CFA_CALL_SETUP_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuCauseCodeWcdma);

        causeCodeCallFailureWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String causeCodeDescription = causeCodeCallFailureWindow.getTableData(0, 1);
        final String causeCodeId = causeCodeCallFailureWindow.getTableData(0, 2);
        final String expectedWindowTitle = causeCodeDescription + GuiStringConstants.COMMA_SPACE + causeCodeId
                + GuiStringConstants.DASH + "Sub Cause Code Analysis for WCDMA CFA Call Setup";

        causeCodeCallFailureWindow.clickTableCell(0, 1, GRID_CELL_LAUNCHER_LINK);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = detailedEventAnalysisCallSetupWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultExtendedCauseCodeDescColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultExtendedCauseCodeDescColumns));
    }

    @Test
    public void rankingsTabVerifyTerminalCallFailureAnalysisRanking_5_8_9() throws PopUpException {

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuTerminalWcdmaCfa);

        // emosjil
        // final String expectedWindowTitle = "Terminal WCDMA Call Failure : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Terminal WCDMA Call Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "Terminal WCDMA Call Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = rankingsTerminalWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultTerminalCfaRankingColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultTerminalCfaRankingColumns));
    }

    @Test
    public void rankingsTabFromTerminalCallFailureAnalysisRankingVerifyFailedEventAnalysisView_5_8_10()
            throws PopUpException, NoDataException {

        final List<String> callDropsAndSetupFailureFailedEventAnalysisColumns = new ArrayList<String>();
        callDropsAndSetupFailureFailedEventAnalysisColumns.addAll(callDropsFailedEventAnalysisColumns);
        callDropsAndSetupFailureFailedEventAnalysisColumns.add(RRC_ESTABLISHMENT_CAUSE);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuTerminalWcdmaCfa);

        rankingsTerminalWcdmaCfa.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String tac = rankingsTerminalWcdmaCfa.getTableData(0, 3);
        
        //emosjil
        //final String expectedWindowTitle = tac + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        final String expectedWindowTitle;
        
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        if(version >= 13.0){
        	expectedWindowTitle = tac + GuiStringConstants.DASH + GuiStringConstants.FAILED_EVENT_ANALYSIS;
        }else{
        	expectedWindowTitle = GuiStringConstants.TERMINAL + GuiStringConstants.SPACE + GuiStringConstants.WCDMA + GuiStringConstants.SPACE + "Call Failure Event Ranking";
        }
        
        rankingsTerminalWcdmaCfa.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = rankingsTerminalWcdmaCfa.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
                + callDropsAndSetupFailureFailedEventAnalysisColumns + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
                + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsAndSetupFailureFailedEventAnalysisColumns));
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaSubscriberRankings_5_8_11a() throws PopUpException {

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CALL_SETUP_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        //emosjil
        //final String expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Setup Failure : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Setup Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Setup Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subscriberCallSetupFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultSubscriberCallFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultSubscriberCallFailureColumns));
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaSubscriberRankings_5_8_11b() throws PopUpException {

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_CALL_DROPS);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        //emosjil
        //final String expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Drops : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Drops : Event Ranking";
        }else{
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Call Drops Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subscriberCallDropsRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultSubscriberCallFailureColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultSubscriberCallFailureColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaSubscriberRankingVerifyDrilldownToIMSIEventAnalysis_5_8_12a()
            throws PopUpException, NoDataException {

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CALL_SETUP_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        subscriberCallSetupFailureRankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String imsi = subscriberCallSetupFailureRankingWindow.getTableData(0, 1);

        subscriberCallSetupFailureRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        final String expectedWindowTitle = imsi + GuiStringConstants.DASH + GuiStringConstants.IMSI_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = subscriberCallSetupFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callSetupImsiEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callSetupImsiEventAnalysisColumns));
    }

    @Test
    public void rankingsTabFromWcdmaCfaSubscriberRankingVerifyDrilldownToIMSIEventAnalysis_5_8_12b()
            throws PopUpException, NoDataException, InterruptedException {

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_CALL_DROPS);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        subscriberCallDropsRankingWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

        final String imsi = subscriberCallDropsRankingWindow.getTableData(0, 1);

        subscriberCallDropsRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        final String expectedWindowTitle = imsi + GuiStringConstants.DASH + GuiStringConstants.IMSI_EVENT_ANALYSIS;
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));
        
        //emosjil
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        List<String> optionalHeaders = new ArrayList<String>();
        optionalHeaders.add(GuiStringConstants.RAB_TYPE);
        optionalHeaders.add(GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB);
        optionalHeaders.add(GuiStringConstants.UL_INT_CELLONE_DBM);
        optionalHeaders.add(GuiStringConstants.RSCP_CELL_ONE_DBM);
        optionalHeaders.add(GuiStringConstants.TRIGGER_POINT);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_CODE);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_SUBCODE);
        optionalHeaders.add(GuiStringConstants.DISCONNECTION_DESC);
        optionalHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
        
        if(version >= 13.0){
        	subscriberCallDropsRankingWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders);
        }

        final List<String> actualWindowHeaders = subscriberCallDropsRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + callDropsImsiEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(callDropsImsiEventAnalysisColumns));
    }

    @Test
    public void rankingsTabVerifyWcdmaCfaSubscriberRankingForRecurringFailures_5_8_13() throws PopUpException {

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_MULTIPLE_RECURRING_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        //emosjil
        //final String expectedWindowTitle = "Subscriber WCDMA Call Failure by Recurring Failure : Event Ranking";
        final String expectedWindowTitle;
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Recurring Failure : Event Ranking";
        }else{
        	expectedWindowTitle = "Subscriber WCDMA Call Failure by Recurring Failure Event Ranking";
        }
        
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = recurringFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultRecurringFailureRankingColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultRecurringFailureRankingColumns));

        assertFalse(FailureReasonStringConstants.TIME_BUTTON_NOT_DISABLED, selenium.isElementPresent(timeButtonXPath));
    }

    @Test
    public void rankingsTabFromWcdmaCfaSubscriberRankingForRecurringFailuresVerifyDrilldownToIMSIEventAnalysis_5_8_14()
            throws PopUpException, NoDataException {

        final List<String> defaultIMSIEventAnalysisColumns = new ArrayList<String>(Arrays.asList(
                GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
                GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVALUATION_CASE,
                GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CAUSE_VALUE,
                GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.SEVERITY_INDICATOR_CFA));

        subMenuSubscriberRanWcdmaCfa.add(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_MULTIPLE_RECURRING_FAILURES);

        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenuSubscriberRanWcdmaCfa);

        recurringFailureRankingWindow.sortTable(SortType.DESCENDING, GuiStringConstants.IMSI);

        final String imsi = recurringFailureRankingWindow.getTableData(0, 1);
        final String accessArea = recurringFailureRankingWindow.getTableData(0, 4);
        final String controller = recurringFailureRankingWindow.getTableData(0, 3);

        recurringFailureRankingWindow.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);

        final String expectedWindowTitle = imsi + GuiStringConstants.DASH + accessArea + GuiStringConstants.DASH
                + controller + GuiStringConstants.DASH + "IMSI Event Analysis for Recurring Failures";
        assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle, selenium.isTextPresent(expectedWindowTitle));

        final List<String> actualWindowHeaders = recurringFailureRankingWindow.getTableHeaders();
        logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS + defaultIMSIEventAnalysisColumns
                + GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS + actualWindowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                actualWindowHeaders.equals(defaultIMSIEventAnalysisColumns));

        assertFalse(FailureReasonStringConstants.TIME_BUTTON_NOT_DISABLED, selenium.isElementPresent(timeButtonXPath));
    }
}
