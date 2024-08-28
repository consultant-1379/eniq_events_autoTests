package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.events.login.EventsLogin;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class SonvisUIBaseTest extends BaseSeleniumTest {

   @Autowired
   protected EventsLogin sonvisLogin;

   @Override
   @Before
   public void setUp() {
      selenium.start();
      selenium.windowFocus();
      selenium.windowMaximize();
      selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
              + PropertyReader.getInstance().getPath(), "true");
      selenium.waitForPageToLoad("30000");
      sonvisLogin.logIn();
      if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
         assertFalse("Error during loading of Landing Page", selenium.getText(
                 "//span[@class='x-window-header-text']").equals("Error"));
      }

   }

   @After
   @Override
   public void tearDown() throws Exception {
       sonvisLogin.logOut();

       if (selenium != null) {
           selenium.close();
           selenium.stop();
           selenium = null;
       }
   }
}