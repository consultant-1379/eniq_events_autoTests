/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsn.coreps;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author eeikbe
 * @since 12/2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CauseCodeAnalysisTests.class,
    QOSTests.class
})
public class CorePsTestSuite {
}
