/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

/**
 * @author eseuhon
 * @since 2010
 *
 */
public interface IViewSubMenu {

    public void openViewSubMenu(String xPath) throws PopUpException;
}
