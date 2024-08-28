package com.ericsson.eniq.events.ui.selenium.events.login;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 05 2012
 */
abstract class AUserLogin {

    public static final int LOGOUT_DELAY = 2000;

    @Autowired
    protected EricssonSelenium selenium;

    public abstract String getUserName();

    protected abstract String getUserPwd();

    /**
     * Assumes user is logged out.
     */
    public void logIn() {
        logIn(getUserName(), getUserPwd());
    }

    /**
     * Assumes user is logged out.
     *
     * @param user
     *        username
     * @param pass
     *        password
     */
    public void logIn(final String user, final String pass) {
        if (selenium.isElementPresent(EVENTS_LOG_IN_BUTTON)) {

            final String usernameField = "//input[@type='text' and not(@tabindex)][1]";
            final String passwordField = "//input[@type='password'][1]";

            selenium.click(usernameField);
            selenium.type(usernameField, user);
            selenium.typeKeys(usernameField, "");

            selenium.click(passwordField);
            selenium.type(passwordField, pass);
            selenium.typeKeys(passwordField, "");

            selenium.keyPress(EVENTS_LOG_IN_BUTTON, "\\13");
            selenium.waitForPageToLoad("30000");
        }
    }

    /**
     * Assumes user is logged in.
     */
    public void relogin() {
        logOut();
        pause(3000);
        //Thread.sleep(LOGOUT_DELAY); // TODO: replace delay by an element detection
        selenium.waitForElementToBePresent("//div[text() = 'User Login']", "" + LOGOUT_DELAY);
        logIn();
    }

    private void pause(int milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
        }
    }

    public void logOut() {
        while (selenium.isElementPresent("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]")) {
            selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        }
        if (selenium.isElementPresent(OPTIONS_BUTTON)) {
            selenium.click(OPTIONS_BUTTON);

            selenium.isElementPresent(EVENTS_LOG_OUT_BUTTON);
            selenium.click(EVENTS_LOG_OUT_BUTTON);
        }
    }
}