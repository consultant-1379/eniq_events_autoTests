/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

/**
 * @author ericker
 * @since 2010
 *
 */
public interface PagingControls {
    public String getItemDisplayCount();

    public int getPageCount();

    public int getCurrentPageNumber();

    public String getLastRefresh();

    public void refresh();

    public void clickNextPage();

    public void clickLastPage();

    public void clickPreviousPage();

    public void clickFirstPage();
}
