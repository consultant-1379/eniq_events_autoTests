/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.baseunittest;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;

/**
 * @author eseuhon
 * @since 2011
 * This class is used to start up selenium tests i.e. start up selenium and log in Eniq Event UI web page.
 * ANY test classes for Eniq Event UI will need to extend this class.
 */
@SuppressWarnings("JUnitTestCaseWithNoTests")
public class EniqEventsUIBaseSeleniumTest extends BaseSeleniumTest {
    @Autowired
    private WorkspaceRC workspaceRC;

    @Override
    @Before
    public void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        selenium.start("captureNetworkTraffic=true");
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath(), "true");
        selenium.waitForPageToLoad("30000");
        eventsLogin.logIn();
        if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
            assertFalse("Error during loading of Landing Page", selenium.getText(
                    "//span[@class='x-window-header-text']").equals("Error"));
        }
        selenium.waitForElementToBePresent("//*[contains(@id,'headerPnl')]//div[contains(text(),'" +
                UIConstants.ENIQ_LOGGED_IN_MSG + "')]", "30000");
        selenium.waitForElementToBePresent("//*[contains(@role,'tablist') and contains(@class,'tab')]", "30000");
    }
    
   // @After
   // @Override
    public void tearDown() throws Exception {
    	eventsLogin.logOut();
    
    	//TODO: Add timestamp
    	if (selenium != null) {
    		selenium.close();
    		selenium.stop();
    		selenium = null;
    	}
    	
    	super.tearDown();
    }

    /**
     * The 3G events have RNC data in the Controller column and SAC data in the Access Area column.
     * 2G events have BSC data in the Controller column and Cell data in the Access Area column.
     * @param subscriberEventAnalysis Subscriber Event Analysis window
     * @throws NoDataException
     */
    public void verify2Gor3GEventData(final CommonWindow subscriberEventAnalysis) throws NoDataException {
        if (!subscriberEventAnalysis.getTableHeaders().contains("RAT")) {
            subscriberEventAnalysis.openTableHeaderMenu(0);
            subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList("RAT"));
        }
        checkStringListContainsArray(subscriberEventAnalysis.getTableHeaders(), "RAT", "Controller", "Access Area");

        for (final Map<String, String> tableDataAtRow : subscriberEventAnalysis.getAllTableData()) {
            final String rat = tableDataAtRow.get("RAT");
            final String controller = tableDataAtRow.get("Controller");
            final String accessArea = tableDataAtRow.get("Access Area");

            if (rat.isEmpty() || controller.isEmpty() || accessArea.isEmpty()) {
                throw new NoDataException(" RAT: " + rat + "Controller: " + controller + "accessArea: " + accessArea);
            }
            if (rat.contains("GSM")) {
                assertTrue("2G event has wrong data type in Controller", controller.contains("BSC"));
                assertTrue("2G event has wrong data type in Access Area", accessArea.contains("CELL"));
            } else if (rat.contains("3G")) {
                assertTrue("3G event has wrong data type in Controller", controller.contains("RNC"));
                assertTrue("3G event has wrong data type in Access Area" + accessArea, accessArea.matches("\\d+"));//SAC data is only consist of digit ?
            }
        }
    }
}
