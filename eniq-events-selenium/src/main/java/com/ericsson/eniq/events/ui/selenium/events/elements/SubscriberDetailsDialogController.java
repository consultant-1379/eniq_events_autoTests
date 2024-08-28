/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
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
public class SubscriberDetailsDialogController implements ISubscriberDetailsDialog {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    @Autowired
    private EricssonSelenium selenium;

    private final String subscriberDetailsIconXPath;

    private final String subscriberDetailsIconWcdmaCfaXPath;

    private final String subscriberDetailsDialogCloseXPath = "//div[contains(@class,'x-panel-btns')]//table[contains(@class, 'x-toolbar-right-ct')]//button[contains(text(), 'Close')]";

    private final String id;

    private final String windowXPath;

    private final String refreshButtonId;

    public SubscriberDetailsDialogController(final String divId) {
        id = divId;
        windowXPath = "//*[@id='selenium_tag_BaseWindow_" + id + "']";

        subscriberDetailsIconXPath = windowXPath + "//table[@id='btnSubscriberDetails']//button";

        subscriberDetailsIconWcdmaCfaXPath = "//*[@id='selenium_tag_baseWindow']//div[@id='btnSubscriberDetailsWcdmaCFA']";

        refreshButtonId = "//*[@id='selenium_tag_baseWindow']//div[@id='btnRefresh']";
    }

    //@Override
    public void openSubscriberDetailsDialog() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subscriberDetailsIconXPath);
        selenium.click(subscriberDetailsIconXPath);
    }

    //@Override
    public void openSubscriberDetailsDialogWcdmaCfa() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subscriberDetailsIconWcdmaCfaXPath);
        selenium.click(subscriberDetailsIconWcdmaCfaXPath);
    }

    //@Override
    public void closeSubscriberDetailsDialog() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subscriberDetailsDialogCloseXPath);
        selenium.click(subscriberDetailsDialogCloseXPath);
    }

    //@Override
    public String getTableData(final int row, final int column) {
        //final String tableCellAddress = "//div[contains(@style,'visibility: visible;')]//div[@class='x-grid3-body']/div"
        // + "[" + (row + 1) + "]/table.0." + column;
        // Changed here, if this change impact other functions, please undo this change.
        final String tableCellAddress = "//div[@class='x-grid3-body']/div" + "[" + (row + 1) + "]/table.0." + column;

        if (selenium.getTable(tableCellAddress).isEmpty()) {
            return GuiStringConstants.EMPTY_STRING;
        }

        System.out.println("Table data is " + selenium.getTable(tableCellAddress));
        return selenium.getTable(tableCellAddress);
    }

    //@Override
    public int getTableRowCount() {
        return selenium.getXpathCount("//div[@class='x-grid3-body']//div[contains(@class,'x-grid3-row')]").intValue();
    }

    //@Override
    public void clickRefreshButton() throws InterruptedException {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + refreshButtonId);
        selenium.click(refreshButtonId);
        Thread.sleep(3000);
    }

}
