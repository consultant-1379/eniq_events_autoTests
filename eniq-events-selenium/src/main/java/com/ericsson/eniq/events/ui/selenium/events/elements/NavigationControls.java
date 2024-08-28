/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.events.elements.NavigationController.SwitchViewDropDown;

/**
 * @author evinpra
 * @since 2011
 * 
 */
public interface NavigationControls {

    public void clickBackwardNavigation();

    public void clickForwardNavigation();

    void clickHistoryDropDown();

    public void switchView(SwitchViewDropDown switchViewDropDown);

    public void toggleGraphToGrid();

    public void toggleGridToGraph() throws InterruptedException;
}
