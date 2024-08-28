package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class SonvisWebDriverBaseUnitTest extends EniqEventsWebDriverBaseUnitTest {

    private static final String DEFAULT_TIMEOUT = "30000";

    public void init() throws PopUpException {
        webDriverSelenium = NewEricssonSelenium.getSharedInstance();
        driver = webDriverSelenium.getWrappedDriver();
        //        webDriverSelenium.click(tabButtonXPath);
    }

    /** Waits for an element to be present */
    public void waitForElementToBePresent(final String locator, final String timeout) {
        if (!webDriverSelenium.isElementPresent(locator)) {
            webDriverSelenium.waitForCondition("var value = selenium.isElementPresent('" + locator.replace("'", "\\'")
                    + "'); value == true", timeout);
        }
    }

    public void sleep(final long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
    
	public void waitForPageToLoad() {
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
}
