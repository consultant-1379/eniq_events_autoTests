package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.thoughtworks.selenium.Selenium;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;
/**
 * 
 * @author ekumpen 2012
 */
public abstract class WebUserLogin {

	public static final int LOGOUT_DELAY = 2000;
	private Selenium selenium;

	public WebUserLogin() {
	}

	public abstract String getUserName();

	protected abstract String getUserPwd();

	public void logIn(Selenium selenium) {
		this.selenium = selenium;
		logIn(getUserName(), getUserPwd());
	}

	public void logIn(final String user, final String pass) {

		if (selenium.isElementPresent(EVENTS_LOG_IN_BUTTON)) {

			final String usernameField = "//input[@type='text' and not(@tabindex)][1]";
			final String passwordField = "//input[@class='EInputBox EInputBox-light EInputBox-prompt'][@type='text'][1]";
			
			//input[@type='password'][1]
			
			
			selenium.click(usernameField);
			pause(1000);
			selenium.type(usernameField, user);
			pause(1000);
			selenium.typeKeys(usernameField, "");
			pause(1000);
			selenium.click(passwordField);
			String p="//input[@class='EInputBox EInputBox-light'][@type='password'][1]";
			pause(1000);
			selenium.type(p, pass);
			pause(1000);
			selenium.typeKeys(p, "");
			pause(1000);
			
//			System.out.println("element present" + selenium.isElementPresent(passwordField));
//			selenium.mouseOver(passwordField);
			
			selenium.keyPress(EVENTS_LOG_IN_BUTTON, "\\13");
			selenium.waitForPageToLoad("30000");

		}

	}
	
	 /**
     * Assumes user is logged in.
	 * @param Selenium 
     */
    public void relogin(Selenium Selenium) {
        logOut();
        //Thread.sleep(LOGOUT_DELAY); // TODO: replace delay by an element detection
//        ((Object) selenium).waitForElementToBePresent("//div[text() = 'User Login']", "" + LOGOUT_DELAY);
        logIn(Selenium );
    }

    public void logOut() {
        if (selenium.isElementPresent(OPTIONS_BUTTON)) {
            selenium.click(OPTIONS_BUTTON);

            selenium.isElementPresent(EVENTS_LOG_OUT_BUTTON);
            selenium.click(EVENTS_LOG_OUT_BUTTON);
            pause(2000);
            selenium.waitForPageToLoad("30000");
            selenium.close();
        }
    }

	private void pause(final int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (final InterruptedException e) {
		}
	}

}
