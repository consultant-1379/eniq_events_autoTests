package com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import org.junit.Test;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class LaunchMenuTestGroup extends SonvisWebDriverBaseUnitTest {
    private final LaunchMenu launchMenu = new LaunchMenu();

    @Test
    public void testDefaults() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
        assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
        assertFalse("Configuration button should be disabled when date is not selected",
                launchMenu.isConfigButtonEnabled());
        assertFalse("Launch button should be disabled if configurations are not selected",
                launchMenu.isLaunchButtonEnabled());
        
    }

    @Test
    public void testButtonStates() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption("Today", null, null);
        assertEquals("Today should be selected in date dropdown", "Today", launchMenu.getSelectedDateOption());
        assertTrue("Configuration button should be enabled after date selection", launchMenu.isConfigButtonEnabled());
        assertFalse("Launch button should be disabled", launchMenu.isLaunchButtonEnabled());

        launchMenu.selectTab(Tab.RANKING);
        launchMenu.launchConfigurationWindow();
        assertEquals("No date should be selected after switching tab", "--Select Date--",
                launchMenu.getSelectedDateOption());
        assertFalse("Configuration button should be disabled after switching tab", launchMenu.isConfigButtonEnabled());
        assertFalse("Launch button should be disabled if configurations are not selected",
                launchMenu.isLaunchButtonEnabled());
    }
    
 
   
}