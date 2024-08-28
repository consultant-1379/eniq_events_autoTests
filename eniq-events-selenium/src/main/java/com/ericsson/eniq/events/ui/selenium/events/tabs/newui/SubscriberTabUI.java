/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.tabs.newui;

import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author eprabab
 * Added Eniq Events New UI GSM automation.
 */
@Component
public class SubscriberTabUI
{

	@Autowired
	private WorkspaceRC workspace;
	
	@Autowired
	protected EricssonSelenium selenium;

	

	
	/*---------------------------------------------------Event Analysis----------------------------------------------*/
    //GSM
    public void openEventAnalysisWindowForGSMCFA(final Selection selection) throws InterruptedException, PopUpException       
    {
		workspace.selectTimeRange(selection.getTimeRange());
		workspace.selectDimension(selection.getDimension());
		System.out.println("Selected group "+selection.getIsGroup() +" Dimentsion : "+selection.getDimension());
		pause(5000);
		if(selection.getIsGroup())
		{
			workspace.enterDimensionValueForGroups(selection.getDimensionValue());
		}
		else
			workspace.enterDimensionValue(selection.getDimensionValue());
		
		pause(2000);
		workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
		workspace.clickLaunchButton();
		selenium.waitForPageLoadingToComplete();
	}
    /*---------------------------------------------------Event Analysis----------------------------------------------*/
    //SGEH
    public void openEventAnalysisWindowForSGEH(final Selection selection) throws InterruptedException, PopUpException       
    {
		workspace.selectTimeRange(selection.getTimeRange());
		workspace.selectDimension(selection.getDimension());
		System.out.println("Selected group "+selection.getIsGroup() +" Dimentsion : "+selection.getDimension());
		pause(5000);
		if(selection.getIsGroup())
		{
			workspace.enterDimensionValueForGroups(selection.getDimensionValue());
		}
		else
			workspace.enterDimensionValue(selection.getDimensionValue());
		
		pause(2000);
		workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
		workspace.clickLaunchButton();
		selenium.waitForPageLoadingToComplete();
	}
	
	public void pause(final int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (final InterruptedException e) {
		}
	}
}
