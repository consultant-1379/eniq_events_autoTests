/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 *
 */
public class PagingController implements PagingControls {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());
    
    @Autowired
    private EricssonSelenium selenium;
    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();

    private final String id;

    private final String pagingDisplayXPath;

    private final String windowXPath;
    
    private final String firstPageButton;
    
    private final String lastPageButton;
    
    private final String previousPageButton;
    
    private final String nextPageButton;
    
    private final String refreshButton;
    
    private final String pageTextBox;
    
    private final String pageCount;
    
    private String lastPageCount;
    /**
     * @param divId
     */
    public PagingController(final String divId) {
        id = divId;
        windowXPath = "//*[@id='selenium_tag_BaseWindow_" + id + "']";
        pagingDisplayXPath = windowXPath + "//div[@class='my-paging-display x-component']";
        firstPageButton = "//*[starts-with(@id,'x-auto-')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-434px')]";
        lastPageButton = "//*[starts-with(@id,'x-auto')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-402px')]";
        previousPageButton = "//*[starts-with(@id,'x-auto-')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-338px')]";
        nextPageButton = "//*[starts-with(@id,'x-auto')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-370px')]";
        refreshButton = "//*[starts-with(@id,'x-auto')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-322px')]";
        pageTextBox = "//*[contains(@class,'gwt-TextBox x-component currentPage')]";
        pageCount = "//*[contains(@id,'x-auto-')]/table/tbody/tr/td[1]/table/tbody/tr/td[6][contains(.,'of')]";
        lastPageCount = "1";
        /* This code is commented because of new workspace management from 13.0 -- xdivsig
        firstPageButton = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[1]//button";
        lastPageButton = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[9]//button";
        previousPageButton = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']/td[2]//td[2]//button";
        nextPageButton = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[8]//button";
        refreshButton = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[11]//button";
        pageTextBox = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[5]//input";
        pageCount = windowXPath + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[6]//div";
        lastPageCount = "1";*/
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#clickFirstPage()
     */
    //@Override
    public void clickFirstPage() {
        // TODO Auto-generated method stub
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + firstPageButton);
        if (selenium.isElementPresent(firstPageButton)) {
            selenium.click(firstPageButton);
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#clickLastPage()
     */
    //@Override
    public void clickLastPage() {
        // TODO Auto-generated method stub
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + lastPageButton);
        if (selenium.isElementPresent(lastPageButton)) {
            selenium.click(lastPageButton);
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#clickNextPage()
     */
    //@Override
    public void clickNextPage() {
        // TODO Auto-generated method stub
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + nextPageButton);
        if (selenium.isElementPresent(nextPageButton)) {
            selenium.click(nextPageButton);
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#clickPreviousPage()
     */
    //@Override
    public void clickPreviousPage() {
        // TODO Auto-generated method stub
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + previousPageButton);
        if (selenium.isElementPresent(previousPageButton)) {
            selenium.click(previousPageButton);
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#getCurrentPageNumber()
     */
    //@Override
    public int getCurrentPageNumber() {
        // TODO Auto-generated method stub
        String currentPageNumber = selenium.getValue(pageTextBox);
        if (currentPageNumber != null && !currentPageNumber.isEmpty()) {
            return Integer.parseInt(currentPageNumber);
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#getItemDisplayCount()
     */
    //@Override
    public String getItemDisplayCount() {
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + pagingDisplayXPath);
        return selenium.getText(pagingDisplayXPath);
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#getLastRefresh()
     */
    //@Override
    public String getLastRefresh() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#getPageCount()
     */
    //@Override
    public int getPageCount() {
        // TODO Auto-generated method stub
        String pageCountNumberText = selenium.getText(pageCount);
        if ( pageCountNumberText != null && !pageCountNumberText.isEmpty()) {
            String pageCountNumber = pageCountNumberText.substring("of ".length(), pageCountNumberText.length());
            return Integer.parseInt(pageCountNumber);
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.elements.PagingControls#refresh()
     */
    //@Override
    public void refresh() {
        // TODO Auto-generated method stub
        lastPageCount = Integer.toString(this.getCurrentPageNumber());
    	loggerDuplicate.log(Level.INFO, "The Element ID : " + refreshButton);
        if (selenium.isElementPresent(refreshButton)) {
            selenium.click(refreshButton);
        }
    }

}
