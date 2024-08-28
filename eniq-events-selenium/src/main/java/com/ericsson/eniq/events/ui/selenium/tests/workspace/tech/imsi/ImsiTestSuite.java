/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.imsi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.imsi.coreps.CorePsTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CorePsTestSuite.class
})
public class ImsiTestSuite {
}
