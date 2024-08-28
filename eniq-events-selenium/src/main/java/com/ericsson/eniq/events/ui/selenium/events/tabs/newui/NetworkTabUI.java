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
/**                     
 * @since 2011           
*/

import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetworkTabUI {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    protected EricssonSelenium selenium;


    public void openRanGSMCFACauseCodeWindowForGSM(final Selection selection) throws InterruptedException, PopUpException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        pause(5000);
        if (selection.getIsGroup()) {
            workspace.enterDimensionValueForGroups(selection.getDimensionValue());
        } else
            workspace.enterDimensionValue(selection.getDimensionValue());
        pause(2000);
        workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    public void openRanGSMCFAEventAnalysisWindowForGSM(final Selection selection) throws InterruptedException, PopUpException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        System.out.println("Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(5000);
        if (selection.getIsGroup()) {
            workspace.enterDimensionValueForGroups(selection.getDimensionValue());
        } else
            workspace.enterDimensionValue(selection.getDimensionValue());

        pause(2000);
        workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    public void openRoamingAnalysisWindowGSM(final Selection selection) throws InterruptedException, PopUpException {
        System.out.println("Values " + selection);
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        pause(2000);
        workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }


    public void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        System.out.println("Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(5000);
        if (selection.getIsNetworkType()) {
            if (selection.getIsGroup()) {
                workspace.enterDimensionValueForGroups(selection.getDimensionValue());
            } else {

                workspace.enterDimensionValue(selection.getDimensionValue());

            }

            pause(2000);
        } else {
            System.out.println("Not a Network Type");

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
