/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.wcdma;

import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.NetworkTestGroupWcdmaCfaNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.RankingTestGroupWcdmaCfaNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.SubscriberTestGroupWcdmaCfaNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA.TerminalTestGroupWcdmaCfaNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.NetworkTestGroupWcdmaHfaNewUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.RankingTestGroupWcdmaHfaNewGUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.SubscriberTestGroupWcdmaHfaNewGUI;
import com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA.TerminalTestGroupWcdmaHfaNewGUI;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author emosjil
 * @since Dec 2013
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ NetworkTestGroupWcdmaCfaNewUI.class, TerminalTestGroupWcdmaCfaNewUI.class,
    SubscriberTestGroupWcdmaCfaNewUI.class, RankingTestGroupWcdmaCfaNewUI.class,  
    NetworkTestGroupWcdmaHfaNewUI.class ,
	SubscriberTestGroupWcdmaHfaNewGUI.class,RankingTestGroupWcdmaHfaNewGUI.class, TerminalTestGroupWcdmaHfaNewGUI.class })
public class WcdmaCfaHfaTestGroupNewUI {

}
