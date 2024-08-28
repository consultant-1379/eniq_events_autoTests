package com.ericsson.eniq.events.ui.selenium.tests.mss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TopologyTestGroupWithNewUILaunch.class,
		NetworkAnalysisTestGroupWithNewUILaunch.class,
		RankingEngineTestGroupWithNewUILaunch.class,
		SubscriberSessionTestGroupWithNewUILaunch.class,
		TerminalAnalysisTestGroupWithNewUILaunch.class,
		SubscriberBusinessIntelligenceTestGroupWithNewUILaunch.class })
public class MssTestSuiteWithNewUILaunch {
}
