package com.ericsson.eniq.events.ui.selenium.tests.baseunittest;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.events.login.DashboardLogin;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"JUnit4AnnotatedMethodInJUnit3TestCase", "JUnitTestCaseWithNoTests"})
public class DashboardUIBaseSeleniumTest extends BaseSeleniumTest {

    @Autowired
    protected DashboardLogin dashboardLogin;
 
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath(), "true");
        selenium.waitForPageToLoad("30000");

        dashboardLogin.logIn();

        if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
            assertFalse("Error during loading of Landing Page", selenium.getText(
                    "//span[@class='x-window-header-text']").equals("Error"));
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        dashboardLogin.logOut();

        //TODO: Add timestamp
        if (selenium != null) {
            selenium.close();
            selenium.stop();
            selenium = null;
        }

        super.tearDown();
    }
}