/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author eseuhon
 * @since 2010
 *
 */
public class ViewSubMenuController implements IViewSubMenu {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());
    
    @Autowired
    private EricssonSelenium selenium;
    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();

    private final String viewButtonXPath = "//table[@id='btnView']//button";

    //@Override
    public void openViewSubMenu(final String xPath) throws PopUpException {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + viewButtonXPath);
        selenium.click(viewButtonXPath);
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + xPath);
        selenium.click(xPath);
        selenium.waitForPageLoadingToComplete();
    }

}
