package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 18/10/12
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class TestWorkspace extends EniqEventsWebDriverBaseUnitTest {

    @Autowired
    private Workspace workspace;

    @Test
    public void testAddWorkspace() throws InterruptedException {
        String categoryPanel = "Roaming by Operator";
        String windowOption = "Core PS";

        String demVal = "blackberry.net";

        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_15);
        workspace.selectDimension(SeleniumConstants.APN);
        workspace.enterDimensionValue(demVal);
        workspace.enterWindowFilter(windowOption);


        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
    }

}

