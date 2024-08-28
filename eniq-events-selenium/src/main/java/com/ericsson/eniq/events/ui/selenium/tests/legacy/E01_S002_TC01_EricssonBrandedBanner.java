/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.ericsson.eniq.events.ui.selenium.core.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.Test;

/**
 * Epic 1 : Story 002 : Design and Implement banner for Ericsson branding
 * 
 *      Test Cases: 
 *      ENIQ _E_4.4.3; Login as system user
 *      
 *      The Ericsson logo is displayed
 *      User name is displayed
 *      Logout and About/Help buttons are displayed
 * 
 * @author ericker
 * @since 2010
 *
 */
public class E01_S002_TC01_EricssonBrandedBanner extends EniqEventsUIBaseSeleniumTest {

    @Test
    public void verifyBanner() {
        // Wait for all fields to be populated
        pause(100);

        // Verify that the user name that we logged in as is present in the appropriate label
        assertEquals("User name field:", eventsLogin.getUserName(), selenium.getText(Constants.USER_NAME_LABEL));

        // Verify the the "Log Out", "User Guide" and "About" buttons are present with the correct text
        assertEquals("Log out button text:", "Log Out", selenium.getText(Constants.EVENTS_LOG_OUT_BUTTON));
        assertEquals("User guide button text:", "User Guide", selenium.getText(Constants.USER_GUIDE_BUTTON));
        assertEquals("About button text:", "About", selenium.getText(Constants.ABOUT_BUTTON));

        // Verify that the Ericsson logo is present and that the URL matches what is expected
        // TODO: Find a better way to validate images
        assertEquals(
                "Ericsson logo URL:",
                Constants.LOGO_URL,
                selenium
                        .getEval("this.findEffectiveStyleProperty(this.browserbot.findElement(\"//div[@id='x-auto-1']/div[2]/div/div[1]\"), \"backgroundImage\")"));
    }
}
