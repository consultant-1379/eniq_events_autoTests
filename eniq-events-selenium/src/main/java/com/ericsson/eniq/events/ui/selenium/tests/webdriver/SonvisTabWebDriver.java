package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import com.ericsson.eniq.events.ui.selenium.events.tabs.Tab;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonVisConstants;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 08/10/12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */

    @Component
    public class SonvisTabWebDriver extends Tab {
        private static WebDriver driver;
        private static String TAB_NAME = "SONVIS_TAB";

        public SonvisTabWebDriver() {
            super(TAB_NAME);
        }

        public void openRankingTab(){
            webDriverSelenium = NewEricssonSelenium.getSharedInstance();
            driver=webDriverSelenium.getWrappedDriver();
            String tabButtonXPath = SonVisConstants.RANKING_TABLE_BUTTON;
            loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
            waitForElementToBePresent(tabButtonXPath, DEFAULT_TIMEOUT);
            webDriverSelenium.click(SonVisConstants.RANKING_TABLE_BUTTON);
        }


        /** Waits for an element to be present */
        public void waitForElementToBePresent(final String locator, final String timeout) {
            if (!webDriverSelenium.isElementPresent(locator)) {
                webDriverSelenium.waitForCondition("var value = selenium.isElementPresent('" + locator.replace("'", "\\'") + "'); value == true", timeout);
            }
        }

        @Override
        protected void clickStartMenu() {
            //To change body of implemented methods use File | Settings | File Templates.

        }
    }
