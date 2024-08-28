package com.ericsson.eniq.events.ui.selenium.tests.workspace.network;

import com.ericsson.eniq.events.ui.selenium.tests.workspace.network.coreps.CorePsNetworkTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author elukpot
 * @since 14.0.15
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CorePsNetworkTestSuite.class
})
public class NetworkTestSuite {
}
