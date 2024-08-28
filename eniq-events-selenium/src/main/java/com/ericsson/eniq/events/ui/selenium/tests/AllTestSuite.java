/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests;

import com.ericsson.eniq.events.ui.selenium.tests.aac.AacTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.adminui.AdminUITestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.alarmkpi.KPITestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.fourg.FourGTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.TestSuiteGsm;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.KPIAnalysisTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.ltecfa.TestSuiteLTECFA;
import com.ericsson.eniq.events.ui.selenium.tests.ltehfa.TestSuiteLTEHFA;
import com.ericsson.eniq.events.ui.selenium.tests.mss.MssTestSuiteWithNewUILaunch;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.SessionBrowserTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt.TwoGThreeGDvdtTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.newui.TwoGThreeGSgehTestSuiteNewGui;
import com.ericsson.eniq.events.ui.selenium.tests.uiimprovements.UIimprovementsTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.WcdmaCfaTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.WcdmaHfaSuite;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//import com.ericsson.eniq.events.ui.selenium.tests.mss.MssTestSuite;

/**
 * @author eseuhon
 * @since 2010
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ FourGTestSuite.class, TwoGThreeGSgehTestSuiteNewGui.class, TwoGThreeGDvdtTestSuite.class,
        MssTestSuiteWithNewUILaunch.class, AacTestSuite.class, WcdmaCfaTestSuite.class, WcdmaHfaSuite.class, TestSuiteLTECFA.class,
        TestSuiteLTEHFA.class, UIimprovementsTestSuite.class, KPITestSuite.class, TestSuiteGsm.class, 
        SessionBrowserTestSuite.class, SonvisTestSuite.class, KPIAnalysisTestSuite.class, AdminUITestSuite.class, Workspace.class})
public class AllTestSuite {
}
