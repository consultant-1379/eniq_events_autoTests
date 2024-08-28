package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ KPILaunchMenuTestGroup.class,
				KPIAnalysisChartsTestGroup.class,
				KPIAnalysisThresholdTestGroup.class,
				KPIAnalysisMapTestGroup.class})

public class KPIAnalysisTestSuite {

}
