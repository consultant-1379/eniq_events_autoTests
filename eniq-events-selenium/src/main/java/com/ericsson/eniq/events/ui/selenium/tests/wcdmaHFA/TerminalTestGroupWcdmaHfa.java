package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.SubscriberBusinessIntelligenceChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.GroupTerminalAnalysisViewType;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
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
public class TerminalTestGroupWcdmaHfa extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("terminalGroupAnalysisWCDMAHFA")
    private CommonWindow terminalGroupAnalysisWCDMAHFA;

    @Autowired
    @Qualifier("terminalAnalysisWCDMAHFA")
    private CommonWindow terminalAnalysisWCDMAHFA;

    @Autowired
    @Qualifier("terminalEventAnalysisWCDMAHFA")
    private CommonWindow terminalEventAnalysisWCDMAHFA;

    @Autowired
    private SubscriberBusinessIntelligenceChart subChart;

     private static final String EXPECTED_HEADER = " EXPECTED HEADERS : ";
    private static final String WINDOW_HEADER = "\n ACTUAL HEADERS : ";

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
    }

    //EE12.2_WHFA_8.1; Terminal Group Analysis Chart – Most SOHO Failures.
    @Test
    public void Terminal_Group_Analysis_Chart_Most_SOHO_Failures_8_1() throws Exception {
    	openTerminalGroupAnalysisWindow();
    	terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);
    	terminalGroupAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
    	terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);
    	basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
    }

    //EE12.2_WHFA_8.1b; Drill down from Terminal Group Analysis Chart to Detailed Event Analysis (SOHO).
    @Test
    public void Terminal_Group_Analysis_Chart_Most_SOHO_Failures_8_1b() throws Exception {
    	selenium.setSpeed("2000");
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);
       // terminalGroupAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);
      //  basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
         final List<String> windowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
               windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow));
    }

    //EE12.2_WHFA_8.2; Terminal Group Analysis Chart – Most IFHO Failures.
    @Test
    public void Terminal_Group_Analysis_Chart_Most_IFHO_Failures_8_2() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA
                .setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);
         terminalGroupAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
         terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);
         basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
    }

    //EE12.2_WHFA_8.2b; Drill down from Terminal Group Analysis Chart to Detailed Event Analysis (IFHO).
    @Test
    public void Terminal_Group_Analysis_Chart_Most_IFHO_Failures_8_2b() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);
    }

    //EE12.2_WHFA_8.3; Terminal Group Analysis Chart – Most IRAT Failures.
    @Test
    public void Terminal_Group_Analysis_Chart_Most_IRAT_Failures_8_3() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
        terminalGroupAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);

        basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
    }

    //EE12.2_WHFA_8.3b; Drill down from Terminal Group Analysis Chart to Detailed Event Analysis (IRAT).
    @Test
    public void Terminal_Group_Analysis_Chart_Most_IRAT_Failures_8_3b() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
    }

     //EE12.2_WHFA_8.4; Terminal Group Analysis Chart – Most HSDSCH Failures.
    @Test
    public void Terminal_Group_Analysis_Chart_Most_HSDSCH_Failures_8_4() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);
        terminalGroupAnalysisWCDMAHFA.setTimeRange(getTimeRangeFromProperties());
        terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
    }

    //EE12.2_WHFA_8.4b; Drill down from Terminal Group Analysis Chart to Detailed Event Analysis (HSDSCH).
    @Test
    public void Terminal_Group_Analysis_Chart_Most_HSDSCH_Failures_8_4b() throws Exception {
        openTerminalGroupAnalysisWindow();
        terminalGroupAnalysisWCDMAHFA
                .setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);
        terminalGroupAnalysisWCDMAHFA.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        basicWindowFunctionalityCheck(terminalGroupAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalGroupAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO,EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow));

    }

     //EE12.2_WHFA_8.5; Terminal Analysis – Most SOHO Failures.
    @Test
    public void Terminal_Analysis_Most_SOHO_Failures_8_5() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.6; Terminal Analysis – Most IFHO Failures.
    @Test
    public void Terminal_Analysis_Most_IFHO_Failures_8_6() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.7; Terminal Analysis – Most IRAT Failures.
    @Test
    public void Terminal_Analysis_Most_IRAT_Failures_8_7() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.8; Terminal Analysis – Most HSDSCH Failures.
    @Test
    public void Terminal_Analysis_Most_HSDSCH_Failures_8_8() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.9; Drill down from Terminal Analysis to Detailed Event Analysis (SOHO).
    @Test
    public void Terminal_Analysis_To_Detailed_Event_Analysis_SOHO_8_9() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_SOHO_HANDOVER_FAILUES);
        //basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.10; Drill down from Terminal Analysis to Detailed Event Analysis (IFHO).
    @Test
    public void Terminal_Analysis_To_Detailed_Event_Analysis_IFHO_8_10() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IFHO_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
    }

    //EE12.2_WHFA_8.11; Drill down from Terminal Analysis to Detailed Event Analysis (IRAT).
    @Test
    public void Terminal_Analysis_To_Detailed_Event_Analysis_IRAT_8_11() throws Exception {
    	selenium.setSpeed("2000");
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_IRAT_HANDOVER_FAILUES);
        terminalAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
       // basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
        terminalAnalysisWCDMAHFA.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();
    }

     //EE12.2_WHFA_8.12; Drill down from Terminal Analysis to Detailed Event Analysis (HSDSCH).
    @Test
    public void Terminal_Analysis_To_Detailed_Event_Analysis_HSDSCH_8_12() throws Exception {
        openTerminalAnalysisWindow();
        terminalAnalysisWCDMAHFA.setGroupTerminalAnalysisView(GroupTerminalAnalysisViewType.MOST_HSDSCH_HANDOVER_FAILUES);
        basicWindowFunctionalityCheck(terminalAnalysisWCDMAHFA);
        terminalAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalAnalysisWindow_ + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,windowHeaders.containsAll(BaseWcdmaHfa.defaultTerminalAnalysisWindow_));
        terminalAnalysisWCDMAHFA.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();
    }

    //EE12.2_WHFA_8.13; Terminal Handover Failure Event Analysis
    @Test
    public void Terminal_Failures_EventAnalysis_8_13() throws Exception {

        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
                ImsiSpecificDataType.TERMINAL_MAKE);
        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
                ImsiSpecificDataType.TERMINAL_MODEL);
        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL, true, terminalMake, terminalModel, tac);
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        //data available for one day.
        terminalAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
    }

     //EE12.2_WHFA_8.14; Drill down from Terminal Event Analysis to Detailed Event Analysis (SOHO).
    @Test
    public void Terminal_Event_Analysis_To_Detailed_Event_Analysis_SOHO_8_14() throws Exception {

//        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MAKE);
//        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MODEL);
        final String terminalMake2 = "Sony Ericsson";
        final String terminalModel2 = "T316";
        final String tac2 = "1015771";
//        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);

        openTerminalEventAnalysisWindow(TerminalType.TERMINAL, true, terminalMake2, terminalModel2, tac2);
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        //data available for one day.
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow));
    }

    //EE12.2_WHFA_8.15; Drill down from Terminal Event Analysis to Detailed Event Analysis (IFHO).

    @Test
    public void Terminal_Event_Analysis_To_Detailed_Event_Analysis_IFHO_8_15() throws Exception {
//        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MAKE);
//        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MODEL);
//        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        final String terminalMake = "Sony Ericsson";
        final String terminalModel = "T316";
        final String tac = "1015771";
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL, true, terminalMake, terminalModel, tac);
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
      //data available for one day.
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIFHOWindow));
    }

    //EE12.2_WHFA_8.16; Drill down from Terminal Event Analysis to Detailed Event Analysis (IRAT).

    @Test
    public void Terminal_Event_Analysis_To_Detailed_Event_Analysis_IRAT_8_16() throws Exception {
//        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MAKE);
//        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,
//                ImsiSpecificDataType.TERMINAL_MODEL);
//        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        final String terminalMake = "Sony Ericsson";
        final String terminalModel = "T316";
        final String tac = "1015771";
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL, true, terminalMake, terminalModel, tac);
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIRATWindow));
    }

    //EE12.2_WHFA_8.17; Drill down from Terminal Event Analysis to Detailed Event Analysis (HSDSCH).

    @Test
    public void Terminal_Event_Analysis_To_Detailed_Event_Analysis_HSDSCH_8_17() throws Exception {
//        final String terminalMake = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,ImsiSpecificDataType.TERMINAL_MAKE);
//        final String terminalModel = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS,ImsiSpecificDataType.TERMINAL_MODEL);
//        final String tac = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.TAC);
        final String terminalMake = "Sony Ericsson";
        final String terminalModel = "T316";
        final String tac = "1015771";
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL, true, terminalMake, terminalModel, tac);
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, windowHeaders.containsAll(BaseWcdmaHfa.defaultEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAHSDSCHWindow));
    }

    //EE12.2_WHFA_8.18; Terminal Group Handover Failure Event Analysis
    @Test
    public void Terminal_Group_Failures_EventAnalysis_8_18() throws Exception {
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS));
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupEventAnalysisWindow));
    }

     //EE12.2_WHFA_8.19; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (SOHO).
    @Test
    public void Terminal_Group_Event_Analysis_To_Detailed_Event_Analysis_SOHO_8_19() throws Exception {
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS));
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOFT_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMASOHOWindow));
    }

     //EE12.2_WHFA_8.20; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (IFHO).
    @Test
    public void Terminal_Group_Event_Analysis_To_Detailed_Event_Analysis_IFHO_8_20() throws Exception {
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS));
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.INTERFREQUENCY_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIFHOWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIFHOWindow));
    }


     //EE12.2_WHFA_8.21; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (IRAT).
    @Test
    public void Terminal_Group_Event_Analysis_To_Detailed_Event_Analysis_IRAT_8_21() throws Exception {
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true,
        reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS));
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.IRAT_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIRATWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAIRATWindow));

    }

    //EE12.2_WHFA_8.22; Drill down from Terminal Group Event Analysis to Detailed Event Analysis (HSDSCH).
    @Test
    public void Terminal_Group_Event_Analysis_To_Detailed_Event_Analysis_HSDSCH_8_22() throws Exception {
        openTerminalEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true,
                reservedDataHelper.getCommonReservedData(CommonDataType.TERMINAL_GROUP_MSS));
        basicWindowFunctionalityCheck(terminalEventAnalysisWCDMAHFA);
        terminalEventAnalysisWCDMAHFA.setTimeRange(TimeRange.ONE_DAY);
        final List<String> windowHeaders = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultGroupEventAnalysisWindow + WINDOW_HEADER + windowHeaders);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders.containsAll(BaseWcdmaHfa.defaultGroupEventAnalysisWindow));
        drillDownOnParticularCell(GuiStringConstants.FAILURES, terminalEventAnalysisWCDMAHFA,
                GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.HSDSCH_HANDOVER);
        final List<String> windowHeaders1 = terminalEventAnalysisWCDMAHFA.getTableHeaders();
        logger.log(Level.INFO, EXPECTED_HEADER + BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAHSDSCHWindow + WINDOW_HEADER
                + windowHeaders1);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
                windowHeaders1.containsAll(BaseWcdmaHfa.defaultTerminalEventAnalysiWCDMAHSDSCHWindow));
    }

    //EE12.2_WHFA_8.23; Exclusive TAC
    @Test
    public void Exclusive_TAC_8_23() throws Exception {
        rankingsTab.openTab();
    }

    //EE12.2_WHFA_8.24; Unknown TAC
    @Test
    public void Unknown_TAC_8_23() throws Exception {
        rankingsTab.openTab();
    }

    /* This method is to open the terminal group analysis.
     *
     * @param TerminalType - Type of terminal analysis
     * @param submitButton - true or false
     * @param values - Terminal group name
     */

    private void openTerminalGroupAnalysisWindow() throws PopUpException, InterruptedException {
         terminalTab.openTab();
        final List<TerminalTab.SubStartMenu> subMenus = new ArrayList<TerminalTab.SubStartMenu>(Arrays.asList(
                TerminalTab.SubStartMenu.TERMINAL_GROUP_RAN_MENU_ITEM_WCDMA,
                TerminalTab.SubStartMenu.TERMINAL_GROUP_WCDMA_MENU_ITEM_WCDMA,
                TerminalTab.SubStartMenu.TERMINAL_GROUP_HFA_SOHO_MENU_ITEM_WCDMA));
         terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_GROUP_ANALYSIS, subMenus);
        waitForPageLoadingToComplete();
    }

    /* This method is to open the terminal group analysis.
     *
     * @param TerminalType - Type of terminal analysis
     * @param submitButton - true or false
     * @param values - Terminal group name
     */

    private void openTerminalAnalysisWindow() throws PopUpException, InterruptedException {
        terminalTab.openTab();
        final List<TerminalTab.SubStartMenu> subMenus = new ArrayList<TerminalTab.SubStartMenu>(Arrays.asList(
                TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_RAN_MENU_ITEM_WDCMA,
                TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_WCDMA_MENU_ITEM_WDCMA,
                TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_HFA_MENU_ITEM_WDCMA));
        terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_ANALYSIS_WCDMA, subMenus);
        waitForPageLoadingToComplete();
    }

    /* This method is to open the terminal event analysis.
     *
     * @param TerminalType - Type of terminal analysis
     * @param submitButton - true or false
     * @param values - Terminal group name
     */
    private void openTerminalEventAnalysisWindow(final TerminalType type, final boolean submitButton,
            final String... values) throws PopUpException, InterruptedException {
        terminalTab.enterAnalysisValues(type, submitButton, values);
        final List<TerminalTab.SubStartMenu> subMenus = new ArrayList<TerminalTab.SubStartMenu>(Arrays.asList(
                TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_RAN_MENU_ITEM_WDCMA,
                TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_WCDMA_MENU_ITEM_WDCMA,
                TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_WCDMA_HFA_SUMMARY));
        terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.TERMINAL_EVENT_ANALYSIS, subMenus);
        waitForPageLoadingToComplete();
    }

    /* This method is to do the basic check on the windows
     *
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
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
    }

}
