/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 * 
 */
public class BreadcrumbController implements BreadcrumbControls {
	
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    @Autowired
    private EricssonSelenium selenium;
    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();

    private final String id;

    private final String windowXPath;

    private final String windowHeaderXPath;

    public BreadcrumbController(final String divId) {
        id = "selenium_tag_BaseWindow_" + divId;
        windowXPath = "//*[contains(@id, '" + id + "')]";
        windowHeaderXPath = "//div[@id='x-auto-23']";
    }

    //@Override
    public void clickWindowHeader() {
  	   loggerDuplicate.log(Level.INFO, "The Element ID : " + windowHeaderXPath);
        selenium.click(windowHeaderXPath);
    }

    //@Override
    public void clickButton(final SelectedButtonType button) {
   	   loggerDuplicate.log(Level.INFO, "The Element ID : " + windowXPath + button.getXPath());
        selenium.click(/*windowXPath +*/ button.getXPath());
    }

    //@Override
    public boolean isButtonEnabled(final SelectedButtonType button) {
   	   loggerDuplicate.log(Level.INFO, "The Element ID : " + windowXPath + "//table[@id='" + button.getId() + "']@class");
        if (selenium.getAttribute(/*windowXPath +*/ "//table[@id='" + button.getId() + "']@class").contains(
                "x-item-disabled")) {
            return false;
        }
        return true;
    }

}
