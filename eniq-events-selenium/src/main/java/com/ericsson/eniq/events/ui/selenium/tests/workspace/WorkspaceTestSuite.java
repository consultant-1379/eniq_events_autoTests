/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace;

import com.ericsson.eniq.events.ui.selenium.tests.workspace.network.NetworkTestSuite;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.TechTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author eeikbe
 * @since 12/2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        NetworkTestSuite.class,
        TechTestSuite.class
})
public class WorkspaceTestSuite {
}
