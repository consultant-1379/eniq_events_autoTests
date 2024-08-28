/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;

/**
 * @author ericker
 * @since 2010
 * 
 */
public interface BreadcrumbControls {

    public void clickButton(SelectedButtonType button);

    public boolean isButtonEnabled(SelectedButtonType button);

    public void clickWindowHeader();
}
