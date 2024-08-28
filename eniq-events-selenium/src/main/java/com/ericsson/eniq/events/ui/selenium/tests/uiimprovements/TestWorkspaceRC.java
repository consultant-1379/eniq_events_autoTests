package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 18/10/12
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class TestWorkspaceRC extends EniqEventsUIBaseSeleniumTest {



    @Autowired
    private WorkspaceRC workspaceRC;

    @Test
    public void testWork() throws PopUpException, InterruptedException {
//        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_CS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
//        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
//        workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
    }


    @Test
    public void testAddWorkspace() throws InterruptedException {
        String categoryPanel = "Roaming by Operator";
        String demVal = "blackberry.net";
        String windowOption = "Core PS";

        workspaceRC.selectTimeRange(SeleniumConstants.DATE_TIME_15);
        workspaceRC.selectDimension(SeleniumConstants.APN);
        workspaceRC.enterDimensionValue(demVal);
        workspaceRC.enterWindowFilter(windowOption);

        workspaceRC.selectWindowType(categoryPanel, windowOption);
        workspaceRC.clickLaunchButton();

        assertTrue(true);
    }

}
