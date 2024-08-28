/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
import org.springframework.stereotype.Component;

/**
 * @author ericker
 * @since 2010
 *
 */
@Component
public class EricssonSelenium extends DefaultSelenium {

    public EricssonSelenium() {
        super(PropertyReader.getInstance().getServerHost(), PropertyReader.getInstance().getServerPort(),
                PropertyReader.getInstance().getBrowserType(), PropertyReader.getInstance().getEventURL());
    }
    
    @Override
    public void open(final String URL) {
        super.open(URL, "true");
    }

    @Override public void waitForCondition(String script, String timeout) {
        try {
            super.waitForCondition(script, timeout);
        } catch (SeleniumException e) { // to add more information
            throw new SeleniumException(e.getMessage() + " [script=" + script +
                    " timeout=" + timeout + "]", e);
        }
    }

    /** Waits for an element to be present */
    public void waitForElementToBePresent(final String locator, final String timeout) {
        if (!this.isElementPresent(locator)) {
            this.waitForCondition("var value = selenium.isElementPresent('" + locator.replace("'", "\\'")
                    + "'); value == true", timeout);
        }
    }

    public void waitForWindowToBeReady(String locator, final String timeout) {
        locator += "@class";
        if (!this.getAttribute(locator).equalsIgnoreCase(" x-window ")) {
            this.waitForCondition("var value = selenium.getAttribute('" + locator.replace("'", "\\'")
                    + "'); value == ' x-window '", timeout);
        }
    }

    public void waitForTextToDisappear(final String text, final String timeout) {
        if (this.isTextPresent(text)) {
            this.waitForCondition("var value = selenium.isTextPresent('" + text + "'); value == false", timeout);
        }
    }

    /**
     * Returns any error messages while page loading completes
     * @return errorMessage
     */
    public String getPageLoadingError() {
        try {
            waitForTextToDisappear("Loading please wait...", "240000");
        } catch (final com.thoughtworks.selenium.SeleniumException e) {
            //sometime we need to wait till loading page message is timed-out to retrieve any error message in pop up window 
            if (e.getMessage().contains("Timed out") && !getErrorMessageOfPopUpWindow().isEmpty()) {
                return getErrorMessageOfPopUpWindow();
            }
            // in case there is no error message in pop up window but if some other runtime errors
            // let original exception go
            throw e;
        }
        return getErrorMessageOfPopUpWindow();
    }

    private String getErrorMessageOfPopUpWindow() {
        String errorMessage = "";
        if (this.isElementPresent("//span[@class='x-window-header-text'][contains(text(),'Error')]")) {
            errorMessage = this
                    .getText("//div[contains(@class, 'x-window')]//div[contains(@class, 'x-window-body pad-text')]/div");
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
     * @param value the millisecond length of a delay which will follow each selenium operation
     */
    @Override public void setSpeed(String value) {
        super.setSpeed(value);
    }
}
