/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.windows;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 *
 */
abstract class Window implements BeanFactoryAware {

    @Autowired
    protected EricssonSelenium selenium;


    
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    protected String windowXPath;

    protected String id;

    protected AutowireCapableBeanFactory beanFactory;

    private final String minimizeButtonXPath;

    private final String maximizeButtonXPath;

    private final String restoreButtonXPath;

    private final String closeButtonXPath;

    private final String windowHeaderLabelXPath;

    private final String lastRefreshLabelXPath;

    /**
     * @param divId
     */
    public Window(final String divId) {
        id = divId;
        windowXPath = "//*[@id='selenium_tag_baseWindow" + "']";

        minimizeButtonXPath = windowXPath + "//div[contains(@class, 'x-nodrag x-tool-minimize x-tool')]";
        maximizeButtonXPath = windowXPath + "//div[contains(@class, 'x-nodrag x-tool-maximize x-tool')]";
        restoreButtonXPath = windowXPath + "//div[contains(@class, 'x-nodrag x-tool-restore x-tool')]";
        closeButtonXPath = windowXPath + "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";
        windowHeaderLabelXPath =  "//span[@class='x-window-header-text']";
        lastRefreshLabelXPath = windowXPath + "//div[contains(@class, 'lastRefreshLabel']";
    }

    public String getWindowXPath() {
        return windowXPath;
    }

    protected abstract void init();

    public boolean isMaximizeButtonActive() {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + maximizeButtonXPath);
        if (selenium.getAttribute(maximizeButtonXPath + "@class").contains("x-hide-display")) {
            return false;
        } // else
        return true;
    }

    public boolean isRestoreButtonActive() {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + restoreButtonXPath);
        if (selenium.getAttribute(restoreButtonXPath + "@class").contains("x-hide-display")) {
            return false;
        } // else
        return true;
    }

    public void minimizeWindow() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + minimizeButtonXPath);
        selenium.click(minimizeButtonXPath);
    }

    public void maximizeWindow() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + maximizeButtonXPath);
        if (isMaximizeButtonActive()) {
            selenium.click(maximizeButtonXPath);
        }
    }

    public void restoreWindow() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + restoreButtonXPath);
        if (isRestoreButtonActive()) {
            selenium.click(restoreButtonXPath);
        }
    }

    public void closeWindow() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + closeButtonXPath);
        selenium.click(closeButtonXPath);
    }

    public String getWindowHeaderLabel() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + windowHeaderLabelXPath);
        return selenium.getText(windowHeaderLabelXPath);
    }

    public String getLastRefresh() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + lastRefreshLabelXPath);
        return selenium.getText(lastRefreshLabelXPath);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    //@Override
    public void setBeanFactory(final BeanFactory arg0) throws BeansException {
        beanFactory = (AutowireCapableBeanFactory) arg0;
    }

}
