package com.ericsson.eniq.events.ui.selenium.tests.mss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TopologyTestGroup.class, NetworkAnalysisTestGroup.class, RankingEngineTestGroup.class,
        SubscriberSessionTestGroup.class, TerminalAnalysisTestGroup.class,
        SubscriberBusinessIntelligenceTestGroup.class })
public class MssTestSuite {
}
