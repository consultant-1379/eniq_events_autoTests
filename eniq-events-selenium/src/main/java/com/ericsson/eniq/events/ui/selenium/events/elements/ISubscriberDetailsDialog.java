/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

/**
 * @author eseuhon
 * @since 2010
 * 
 */
public interface ISubscriberDetailsDialog {

    public void openSubscriberDetailsDialog();

    public void openSubscriberDetailsDialogWcdmaCfa();

    public void closeSubscriberDetailsDialog();

    public int getTableRowCount();

    public String getTableData(int row, int column);

    public void clickRefreshButton() throws InterruptedException;
}
