/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.events.tabs.newui;

import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * @since 2012
 * 
 */
@Component
public class TerminalTabUI {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    protected EricssonSelenium selenium;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    public void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        System.out.println("Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(5000);
        if (selection.getIsTerminalType()) {
            if (selection.getIsGroup()) {
                workspace.enterDimensionValueForGroups(selection.getDimensionValue());
            } else
                workspace.selectTerminalDimensionValue(selection.getDimensionValue());
            pause(2000);
            workspace.setElementPathValue("//*[@id='selenium_tag_searchFieldInput']/div/table/tbody/tr/td[1]/input", selection.getTerminalModel()
                    + "," + selection.getTac());
        } else {
            if (selection.getIsGroup()) {
                workspace.enterDimensionValueForGroups(selection.getDimensionValue());
            } else
                workspace.selectDimension(selection.getDimensionValue());
        }

        pause(2000);
        workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindow1(final Selection selection) throws PopUpException, InterruptedException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        pause(2000);
        if (selection.getIsTerminalType()) {
            if (selection.getIsGroup()) {
                workspace.enterDimensionValueForGroups(selection.getDimensionValue());
            } else {
                workspace.enterDimensionValue(selection.getDimensionValue());
                workspace.enterSecondDimensionValue(selection.getTerminalModel() + "," + selection.getTac());
            }
        } else {
            logger.log(Level.INFO, "Is not a Terminal Type !! ");
        }

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
