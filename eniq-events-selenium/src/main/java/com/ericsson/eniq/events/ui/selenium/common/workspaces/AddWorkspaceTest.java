package com.ericsson.eniq.events.ui.selenium.common.workspaces;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 15/10/12
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */


public class AddWorkspaceTest extends EniqEventsWebDriverBaseUnitTest {

    public static WebElement element;
    @Autowired
    private SessionBrowserTab sessionbrowserTab;

    @Override
    public void waitForPageLoadingToComplete() throws PopUpException {
        webDriverSelenium.waitForPageLoadingToComplete();
    }

   @Test
    public void testLaunchWorkspace()throws InterruptedException, PopUpException{
       sessionbrowserTab.openTab();
       waitForPageLoadingToComplete();

       webDriverSelenium.click(WorkspaceConstants.addNewWorkspace);
//       driver.findElement(By.xpath(WorkspaceConstants.addNewWorkspace)).click();

       waitForPageLoadingToComplete();
//       webDriverSelenium.click(WorkspaceConstants.SELECT_DIMENSION);
//       driver.findElement(By.xpath(WorkspaceConstants.SELECT_DIMENSION)).click();

//       webDriverSelenium.click(WorkspaceConstants.addWorkspace);

       pause(1000000);
    }
}
