/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Level;

/**
* @author ejenkav
* @since 2011
*
*/
public class RoleMgmtTestGroup extends AdminUIBaseSeleniumTest {

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Role Management test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Role Management section");
    }

    @Test
    public void test1() {
        assertTrue(roleMgmt.verifyPermissionGroups("marketing", "subscribergroup,terminalgroup"));
    }

}
