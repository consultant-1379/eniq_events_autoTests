/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author ekeviry
 * @since 2011
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ NetworkTestGroupWcdmaCfaNewUI.class, TerminalTestGroupWcdmaCfaNewUI.class,
        SubscriberTestGroupWcdmaCfaNewUI.class, RankingTestGroupWcdmaCfaNewUI.class, AccessAreaRankingTests.class, ControllerRankingTests.class})
public class WcdmaCfaTestSuiteNewUI {

}
