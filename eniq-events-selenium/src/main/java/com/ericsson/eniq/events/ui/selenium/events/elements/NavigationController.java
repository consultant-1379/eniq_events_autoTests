/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author evinpra
 * @since 2011
 * 
 */
public class NavigationController implements NavigationControls {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    @Autowired
    private EricssonSelenium selenium;

    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();

    private final String id;

    private final String windowXPath;

    private final String backwardNavigationButton;

    private final String forwardNavigationButton;

    private final String historyButton;

    private final String toggleGraphToGridXPath;

    private final String toggleGridToGraphXPath;

    /**
     * @param divId
     */
    public NavigationController(final String divId) {
        id = divId;
        //windowXPath = "//*[@id='selenium_tag_BaseWindow_" + id + "']";
        windowXPath = "//*[@id='selenium_tag_baseWindow']";

        final String eniqVersion = PropertyReader.getInstance().getEniqVersion();
        if (eniqVersion.equals("12.1")) {
            backwardNavigationButton = windowXPath + "//table[@id='btnBack']//button";
        } else {
            //12.2.X
            backwardNavigationButton = windowXPath + "//div[@id='btnBack']//input";
        }

        forwardNavigationButton = windowXPath + "//div[@id='btnForward']//input";
        historyButton = windowXPath + "//div[@id='btnNav']//input";

        toggleGraphToGridXPath = windowXPath + "//table[@id='btnToggleToGrid']//button";
        toggleGridToGraphXPath = windowXPath + "//table[@id='btnToggleToGrid']//button";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls
     * #clickFirstPage()
     */
    //@Override
    public void clickBackwardNavigation() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + backwardNavigationButton);
        selenium.click(backwardNavigationButton);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls
     * #clickFirstPage()
     */
    //@Override
    public void clickForwardNavigation() {
        // TODO Auto-generated method stub
        loggerDuplicate.log(Level.INFO, "The Element ID : " + forwardNavigationButton);
        if (selenium.isElementPresent(forwardNavigationButton)) {
            selenium.click(forwardNavigationButton);
        }
    }

    //@Override
    public void clickHistoryDropDown() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + historyButton);
        if (selenium.isElementPresent(historyButton)) {
            selenium.click(historyButton);
        }

    }

    public enum SwitchViewDropDown {
        MOST_CALL_DROPS("menuGTA_Most_Call_Drops_Summary"), MOST_CALL_SETUP_FAILURES(
                "menuGTA_Most_Call_Setup_Failures_Summary");

        private final String switchViewDropDownType;

        SwitchViewDropDown(final String name) {
            switchViewDropDownType = name;
        }

        private String getXPath() {
            return "//a[@id='" + switchViewDropDownType + "']";
        }
    }

    /**
     * This method is for switching between Terminal Analysis Views: 'Most Call Drops'
     * and 'Most Call Setup Failures'
     */
    //@Override
    public void switchView(final SwitchViewDropDown switchViewDropDown) {
        final String switchViewDropDownXPath = "//table[@id='btnView']";
        loggerDuplicate.log(Level.INFO, "The Element ID : " + switchViewDropDownXPath);
        selenium.click(switchViewDropDownXPath);

        final String switchViewDropDownType = switchViewDropDown.getXPath();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + switchViewDropDownType);
        selenium.click(switchViewDropDownType);
        try {
            selenium.waitForPageLoadingToComplete();
            Thread.sleep(3000);
        } catch (final PopUpException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void toggleGraphToGrid() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + toggleGraphToGridXPath);
        selenium.click(toggleGraphToGridXPath);
    }

    //@Override
    public void toggleGridToGraph() throws InterruptedException {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + toggleGridToGraphXPath);
        selenium.click(toggleGridToGraphXPath);
        Thread.sleep(3000);
    }

}