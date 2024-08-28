package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;

/**
 * 
 * @author ekumpen 2012
 */
public abstract class EniqEventsWebDriverBaseUnitTest extends BaseSeleniumTest {


	protected Connection conn = null;
	protected String url = "jdbc:sybase:Tds:atrcxb2340.athtem.eei.ericsson.se:2640/dwhdb";
	protected String Database_driver = "com.sybase.jdbc3.jdbc.SybDriver";
	protected String USER_NAME = "dc";
	protected String PASSWORD = "dc";

	@Override
	@Before
	public void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// TODO driver here

		webDriverSelenium = NewEricssonSelenium.getSharedInstance();
		
		driver = webDriverSelenium.getWrappedDriver();
		driver.get(PropertyReader.getInstance().getEventHost() + ":"
				+ PropertyReader.getInstance().getEventPort()
				+ PropertyReader.getInstance().getPath());
		driver.manage().window().maximize();
		webDriverSelenium.waitForPageToLoad("30000");
		eventsWebDriverLogin.logIn(webDriverSelenium);
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		eventsWebDriverLogin.logOut();
		if (webDriverSelenium != null) {
			webDriverSelenium.close();
			webDriverSelenium.stop();
			//webDriverSelenium = null;
		}
		NewEricssonSelenium.destroy();
		EricssonFirefoxWebDriver.destroy();
		super.tearDown();
	}
}
