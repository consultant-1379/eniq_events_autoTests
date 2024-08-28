/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author eseuhon
 * @since 2011
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { SubscriberSessionManagementTestGroup.class, DvdtSubscriberBusinessIntelligenceTestGroup.class,
        NetworkAnalysisTestGroup.class, TerminalAnalysisTestGroup.class, DataVolumeRankingTestGroup.class })
public class TwoGThreeGDvdtTestSuite {

}
