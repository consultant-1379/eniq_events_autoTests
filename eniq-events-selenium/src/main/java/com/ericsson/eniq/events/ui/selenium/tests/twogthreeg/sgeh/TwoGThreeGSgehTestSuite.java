/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @author eseuhon
 * @since 2011
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { SubscriberSessionAnalysisTestGroup.class, SgehSubscriberBusinessIntelligenceTestGroup.class,
        RankingTestGroup.class, NetworkTestGroup.class, TerminalTestGroup.class })
public class TwoGThreeGSgehTestSuite {

}
