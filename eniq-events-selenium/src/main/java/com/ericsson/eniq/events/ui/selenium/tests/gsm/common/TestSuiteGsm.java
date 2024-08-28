/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.gsm.common;

import com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa.NetworkTabTestGroupGSM;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa.SubscriberTabTestGroupGSM;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ NetworkTabTestGroupGSM.class, SubscriberTabTestGroupGSM.class })
public class TestSuiteGsm {

}
