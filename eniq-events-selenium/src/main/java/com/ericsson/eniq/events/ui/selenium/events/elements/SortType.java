/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

/**
 * @author eseuhon
 * @since 2011
 */
public enum SortType {
    //descending
	DESCENDING("//div[@class='x-menu-list-item']//a[contains(text(), 'Sort Descending')]", "desc"),
    //ascending
	ASCENDING("//div[@class='x-menu-list-item']//a[contains(text(), 'Sort Ascending')]", "asc");

    private final String xPath;

    private final String sortClassPart;

    SortType(final String xPath, final String sortClassPart) {
        this.xPath = xPath;
        this.sortClassPart = sortClassPart;
    }

    public String getXPath() {
        return xPath;
    }

    public String getSortClassPart() {
        return sortClassPart;
    }
}
