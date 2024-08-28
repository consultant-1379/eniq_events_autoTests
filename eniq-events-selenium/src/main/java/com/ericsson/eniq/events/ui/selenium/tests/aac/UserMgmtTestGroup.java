/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac;

import com.ericsson.eniq.events.ui.selenium.tests.aac.data.Predefined;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Level;

/**
* @author ejenkav
* @since 2011
*
*/
public class UserMgmtTestGroup extends AdminUIBaseSeleniumTest {

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of User Management test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of User Management test section");
    }

    @Test
    public void TC1_createUser4EachPredefinedRole() {

        for (int i = 1; i <= 7; i++) {
            final User user = getUser("UM.TC1." + i);
            assertTrue(userMgmt.createUser(user));
            userMgmt.logOut();
            if (user.getRolesAsString().toLowerCase().contains(Predefined.SYSADMIN_ROLE.getName())) {
                assertTrue(userMgmt.logIn(user.getUserID(), user.getPassword()));
                eniqMgmt.login(user.getUserID(), user.getPassword());
            } else {
                assertTrue(eniqMgmt.firstTimeLogin(user.getUserID(), user.getPassword(), getData("UM.TC1." + i
                        + ".user.newpassword")));
            }

            assertTrue(isTabEnabledForRoles(user.getRoles()));
        }

    }

    @Test
    public void test2() {
        assertTrue(userMgmt.verifyLastName("myuser", "Kavadiya"));

    }
}
