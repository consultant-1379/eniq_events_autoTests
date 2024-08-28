/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.baseunittest;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.events.login.AdminLogin;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author eseuhon
 * @since 2011
 * 
 * This class is used to start up selenium tests i.e. start up selenium and log in AdminUI web page.
 * ANY test classes for Admin UI will need to extend this class.
 *    
 */
@SuppressWarnings("JUnitTestCaseWithNoTests")
public class AdminUIBaseSeleniumTest extends BaseSeleniumTest {

    @Autowired
    private AdminLogin adminLogin;

    @Override
    @Before
    public void setUp() {
        selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(PropertyReader.getInstance().getAdminServerHost() + ":"
                + PropertyReader.getInstance().getAdminServerPort() + PropertyReader.getInstance().getAdminServerPath()
                + "/servlet/LoaderStatusServlet");
        adminLogin.logIn();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        adminLogin.logOut();
        //TODO: Add timestamp
        if (selenium != null) {
            selenium.close();
            selenium.stop();
            selenium = null;
        }
    }
}
