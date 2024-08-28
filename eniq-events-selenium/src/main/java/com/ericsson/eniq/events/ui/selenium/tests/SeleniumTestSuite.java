/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.runner.JUnitCore;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.events.login.CreateDashboardUser;
import com.ericsson.eniq.events.ui.selenium.tests.aac.AacTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.adminui.AdminUITestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.alarmkpi.KPITestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.dummy.DummyTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.fourg.FourGTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.TestSuiteGsm;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.KPIAnalysisTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.legacy.SampleWebDriver;
import com.ericsson.eniq.events.ui.selenium.tests.ltecfa.*;
import com.ericsson.eniq.events.ui.selenium.tests.ltehfa.*;
import com.ericsson.eniq.events.ui.selenium.tests.mss.*;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.*;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt.newui.TwoGThreeGDvdtTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.newui.*;
import com.ericsson.eniq.events.ui.selenium.tests.uiimprovements.*;
import com.ericsson.eniq.events.ui.selenium.tests.wcdma.*;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.WcdmaCfaTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.WcdmaCfaTestSuiteNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.WcdmaHfaSuite;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.WcdmaHfaSuiteNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.WorkspaceTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.upgrade.WorkspacePostUpgradeTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.upgrade.WorkspacePreUpgradeTestSuite;

//import com.ericsson.eniq.events.ui.selenium.tests.mss.MssTestSuite;

/**
 * @author ericker
 * @since 2010
 * 
 */
public class SeleniumTestSuite {

    static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    static Map<String, Class<?>> testGroupToTestSuiteMap = new HashMap<String, Class<?>>();

    static {
        testGroupToTestSuiteMap.put("WcdmaHfaTestGroup", WcdmaHfaSuite.class);
        testGroupToTestSuiteMap.put("WcdmaCfaTestGroup", WcdmaCfaTestSuite.class);
        testGroupToTestSuiteMap.put("WcdmaCfaTestGroupNewUI", WcdmaCfaTestSuiteNewUI.class);
        testGroupToTestSuiteMap.put("WcdmaHfaTestGroupNewUI", WcdmaHfaSuiteNewUI.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI", WcdmaCfaHfaTestGroupNewUI.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Cfa_Network", WcdmaCfaHfaTestGroupNewUICfaNetwork.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Cfa_Ranking", WcdmaCfaHfaTestGroupNewUICfaRanking.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Cfa_Subscriber", WcdmaCfaHfaTestGroupNewUICfaSubscriber.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Cfa_Terminal", WcdmaCfaHfaTestGroupNewUICfaTerminal.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Hfa_Network", WcdmaCfaHfaTestGroupNewUIHfaNetwork.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Hfa_Ranking", WcdmaCfaHfaTestGroupNewUIHfaRanking.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Hfa_Subscriber", WcdmaCfaHfaTestGroupNewUIHfaSubscriber.class);
        testGroupToTestSuiteMap.put("WcdmaCfaHfaTestGroupNewUI_Hfa_Terminal", WcdmaCfaHfaTestGroupNewUIHfaTerminal.class);
        testGroupToTestSuiteMap.put("4GTestGroup", FourGTestSuite.class);
        testGroupToTestSuiteMap.put("2G3GSgehTestGroup", TwoGThreeGSgehTestSuiteNewGui.class);
        testGroupToTestSuiteMap.put("2G3GSgehTestGroup_Ranking", TwoGThreeGSgehTestSuiteNewGui_Ranking.class);
        testGroupToTestSuiteMap.put("2G3GSgehTestGroup_SubscriberSessionAnalysis", TwoGThreeGSgehTestSuiteNewGui_SubscriberSessionAnalysis.class);
        testGroupToTestSuiteMap.put("2G3GSgehTestGroup_Terminal", TwoGThreeGSgehTestSuiteNewGui_Terminal.class);
        testGroupToTestSuiteMap.put("2G3GDvdtTestGroup", TwoGThreeGDvdtTestSuite.class);
        // testGroupToTestSuiteMap.put("MssTestGroup", MssTestSuite.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch", MssTestSuiteWithNewUILaunch.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_NetworkAnalysis", MssTestSuiteWithNewUILaunch_NetworkAnalysis.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_RankingEngine", MssTestSuiteWithNewUILaunch_RankingEngine.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_SubscriberSession", MssTestSuiteWithNewUILaunch_SubscriberSession.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_SubscriberBusinessIntelligence",
                MssTestSuiteWithNewUILaunch_SubscriberBusinessIntelligence.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_TerminalAnalysis", MssTestSuiteWithNewUILaunch_TerminalAnalysis.class);
        testGroupToTestSuiteMap.put("MssTestGroupUILaunch_Topology", MssTestSuiteWithNewUILaunch_Topology.class);
        testGroupToTestSuiteMap.put("AacTestGroup", AacTestSuite.class);
        testGroupToTestSuiteMap.put("DummyTestGroup", DummyTestSuite.class);
        testGroupToTestSuiteMap.put("LteCfaTestGroup", TestSuiteLTECFA.class);
        testGroupToTestSuiteMap.put("LteCfaTestGroup_NetworkAnalysis", TestSuiteLTECFA_NetworkAnalysis.class);
        testGroupToTestSuiteMap.put("LteCfaTestGroup_RankingEngine", TestSuiteLTECFA_RankingEngine.class);
        testGroupToTestSuiteMap.put("LteCfaTestGroup_SubscriberSession", TestSuiteLTECFA_SubscriberSession.class);
        testGroupToTestSuiteMap.put("LteCfaTestGroup_TerminalAnalysis", TestSuiteLTECFA_TerminalAnalysis.class);
        testGroupToTestSuiteMap.put("LteHfaTestGroup", TestSuiteLTEHFA.class);
        testGroupToTestSuiteMap.put("LteHfaTestGroup_NetworkAnalysis", TestSuiteLTEHFA_NetworkAnalysis.class);
        testGroupToTestSuiteMap.put("LteHfaTestGroup_RankingEngine", TestSuiteLTEHFA_RankingEngine.class);
        testGroupToTestSuiteMap.put("LteHfaTestGroup_SubscriberSession", TestSuiteLTEHFA_SubscriberSession.class);
        testGroupToTestSuiteMap.put("LteHfaTestGroup_TerminalAnalysis", TestSuiteLTEHFA_TerminalAnalysis.class);
        testGroupToTestSuiteMap.put("CreateDashboardUser", CreateDashboardUser.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup", UIimprovementsTestSuite.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_ColumnChooserFunctionality", UIimprovementsTestSuite_ColumnChooserFunctionality.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_GridImprovement", UIimprovementsTestSuite_GridImprovement.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_GroupManagement", UIimprovementsTestSuite_GroupManagement.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_ImprovingDateTimeFilter", UIimprovementsTestSuite_ImprovingDateTimeFilter.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_OptionsMenu", UIimprovementsTestSuite_OptionsMenu.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_PersistUserScreenAndLayoutSettings",
                UIimprovementsTestSuite_PersistUserScreenAndLayoutSettings.class);
        testGroupToTestSuiteMap.put("UIimprovementsTestGroup_RoamingAnalysisDrills", UIimprovementsTestSuite_RoamingAnalysisDrills.class);
        testGroupToTestSuiteMap.put("KPINotificationTestGroup", KPITestSuite.class);
        testGroupToTestSuiteMap.put("GsmTestGroup", TestSuiteGsm.class);
        testGroupToTestSuiteMap.put("SampleWebDriver", SampleWebDriver.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite", SessionBrowserTestSuite.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_Launch", SessionBrowserTestSuite_Launch.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_ApplicationLayerDetail", SessionBrowserTestSuite_ApplicationLayerDetail.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_ApplicationLayerSummary", SessionBrowserTestSuite_ApplicationLayerSummary.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_CoreSignalling", SessionBrowserTestSuite_CoreSignalling.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_DBSCore", SessionBrowserTestSuite_DBSCore.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_DBSRan", SessionBrowserTestSuite_DBSRan.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_DetailCharts", SessionBrowserTestSuite_DetailCharts.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_RadioAndCellConditions", SessionBrowserTestSuite_RadioAndCellConditions.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_RanCoreSignallingSessionList",
                SessionBrowserTestSuite_RanCoreSignallingSessionList.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_RanSignalling", SessionBrowserTestSuite_RanSignalling.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_TCPPerformanceChart", SessionBrowserTestSuite_TCPPerformanceChart.class);
        testGroupToTestSuiteMap.put("3GSessionBrowserTestSuite_TCPPerformanceSummary", SessionBrowserTestSuite_TCPPerformanceSummary.class);
        testGroupToTestSuiteMap.put("3GKPIAnalysisTestSuite", KPIAnalysisTestSuite.class);
        testGroupToTestSuiteMap.put("WorkspaceTestSuite", WorkspaceTestSuite.class);
        testGroupToTestSuiteMap.put("WorkspacePreUpgradeTestSuite", WorkspacePreUpgradeTestSuite.class);
        testGroupToTestSuiteMap.put("WorkspacePostUpgradeTestSuite", WorkspacePostUpgradeTestSuite.class);
        testGroupToTestSuiteMap.put("WorkspaceTestSuite", WorkspaceTestSuite.class);
        testGroupToTestSuiteMap.put("AdminUITestGroup", AdminUITestSuite.class);
        testGroupToTestSuiteMap.put("SonvisTests", SonvisTestSuite.class);
        testGroupToTestSuiteMap.put("All", AllTestSuite.class);

    }

    public static void main(final String[] args) {
        try {
            SeleniumLogger.setUp();
        } catch (final SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log file");
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log file");
        }
        boolean doListener = false;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("listener")) {
                    doListener = true;
                    break;
                }
            }
        }
        if (doListener) {
            System.out.println("Starting selenium listener");
            startListener();
        } else {
            doTests();
        }
    }

    public static void startListener() {
        Provider server = new Provider();
        try {
            while (true) {
                server.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doTests() {
        try {
            SeleniumLogger.setUp();
        } catch (final SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log file");
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log file");
        }
        logger.info("ENIQ Version: " + PropertyReader.getInstance().getEniqVersion());
        final JUnitCore core = new JUnitCore();
        final String testGroup = PropertyReader.getInstance().getTestGroup().trim();
        logger.info("Test group to run : " + testGroup);

        final Class<?> testSuite = testGroupToTestSuiteMap.get(testGroup);
        core.run(testSuite);
        SeleniumLogger.tearDown();
    }
}
