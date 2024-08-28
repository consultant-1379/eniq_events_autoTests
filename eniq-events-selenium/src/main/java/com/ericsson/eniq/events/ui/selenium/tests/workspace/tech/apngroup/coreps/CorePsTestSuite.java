/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2014 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apngroup.coreps;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author elukpot
 * @since 14.0.14
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CauseCodeAnalysisTests.class,
        QOSTests.class
})
public class CorePsTestSuite {
}