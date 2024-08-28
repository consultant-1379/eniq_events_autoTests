/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controller;

import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controller.coreps.CorePsTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author eeikbe
 * @since 12/2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CorePsTestSuite.class
})
public class ControllerTestSuite {
}
