package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details.*;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary.ApplicationLayerSummaryTest;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary.RadioAndCellConditions;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary.TCPPerformanceSummaryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author ekumpen August 2012
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ SessionBrowserLaunchTestGroup.class,
				CoreSignallingTestGroup.class, 
				DBSCoreTestGroup.class,
				DBSRanTestGroup.class, 
				DBSUserPaneTestGroup.class,
				RanSignallingTestGroup.class,
				RanCoreSignallingSessionListTestGroup.class,
				ApplicationLayerSummaryTest.class,
				ApplicationLayerDetailTest.class,
				TCPPerformanceChartTest.class,
				TCPPerformanceSummaryTest.class,
				RadioAndCellConditions.class,
				DetailChartsTestGroups.class})
public class SessionBrowserTestSuite {

}
