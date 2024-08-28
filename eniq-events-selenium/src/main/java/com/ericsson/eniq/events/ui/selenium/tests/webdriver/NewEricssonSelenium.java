package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.thoughtworks.selenium.SeleniumException;
import org.openqa.selenium.WebDriverBackedSelenium;

/**
 * New Ericsson Selenium instance for backward compatibility. This class having
 * all capability of EricssonSelenium. We need to remove EricssonSelenium class
 * to avoid duplication. This will replace EricssonSelenium in future when all
 * code is shifted to WebDriver framework.
 * 
 * @author ekumpen 2012
 */
// @Component
public class NewEricssonSelenium extends WebDriverBackedSelenium {
	private static NewEricssonSelenium INSTANCE;

	// TODO need to use spring framework for singleton instance
	public static NewEricssonSelenium getSharedInstance() {
		//System.setProperty("webdriver.chrome.driver","E:\\emosjil\\browsers\\chromedriver.exe");
		if (INSTANCE == null) {
			INSTANCE = new NewEricssonSelenium();
		}
		
		return INSTANCE;
	}

	public static void destroy() {
		INSTANCE = null;
	}

	private NewEricssonSelenium() {
		super(EricssonFirefoxWebDriver.getSharedInstance(), PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort() + PropertyReader.getInstance().getPath());
	}

	@Override
	public void open(final String URL) {
		super.open(URL, "true");
	}

	@Override
	public void waitForCondition(String script, String timeout) {
		try {
			super.waitForCondition(script, timeout);
		} catch (SeleniumException e) { // to add more information
			throw new SeleniumException(e.getMessage() + " [script=" + script + " timeout=" + timeout + "]", e);
		}
	}

	/** Waits for an element to be present */
	public void waitForElementToBePresent(final String locator, final String timeout) {
		if (!this.isElementPresent(locator)) {
			this.waitForCondition("var value = selenium.isElementPresent('" + locator.replace("'", "\\'") + "'); value == true", timeout);
		}
	}

	public void waitForWindowToBeReady(String locator, final String timeout) {
		locator += "@class";
		if (!this.getAttribute(locator).equalsIgnoreCase(" x-window ")) {
			this.waitForCondition("var value = selenium.getAttribute('" + locator.replace("'", "\\'") + "'); value == ' x-window '", timeout);
		}
	}

	public void waitForTextToDisappear(final String text, final String timeout) {
		if (this.isTextPresent(text)) {
			this.waitForCondition("var value = selenium.isTextPresent('" + text + "'); value == false", timeout);
		}
	}

	/**
	 * Returns any error messages while page loading completes
	 * 
	 * @return errorMessage
	 */
	public String getPageLoadingError() {
		try {
			waitForTextToDisappear("Loading please wait...", "240000");
		} catch (final com.thoughtworks.selenium.SeleniumException e) {
			// sometime we need to wait till loading page message is timed-out
			// to retrieve any error message in pop up window
			if (e.getMessage().contains("Timed out") && !getErrorMessageOfPopUpWindow().isEmpty()) {
				return getErrorMessageOfPopUpWindow();
			}
			// in case there is no error message in pop up window but if some
			// other runtime errors
			// let original exception go
			throw e;
		}
		return getErrorMessageOfPopUpWindow();
	}

	private String getErrorMessageOfPopUpWindow() {
		String errorMessage = "";
		if (this.isElementPresent("//span[@class='x-window-header-text'][contains(text(),'Error')]")) {
			errorMessage = this.getText("//div[contains(@class, 'x-window')]//div[contains(@class, 'x-window-body pad-text')]/div");
		}
		return errorMessage;
	}

	public void waitForPageLoadingToComplete() throws PopUpException {
		final String error = getPageLoadingError();
		if (!error.isEmpty()) {
			throw new PopUpException(error);
		}
	}

	/**
	 * Sets execution speed.
	 * 
	 * @param value
	 *            the millisecond length of a delay which will follow each
	 *            selenium operation
	 */
	@Override
	public void setSpeed(String value) {
		super.setSpeed(value);
	}

}
