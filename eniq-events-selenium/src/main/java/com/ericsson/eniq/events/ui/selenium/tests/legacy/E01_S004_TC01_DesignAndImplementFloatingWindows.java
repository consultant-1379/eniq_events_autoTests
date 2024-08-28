package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.ericsson.eniq.events.ui.selenium.events.login.EventsLogin;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;

/*
 * Epic 1 : Story 004 : Design and Implement floating windows
 * 
 * 		Design and implement the window class 
 *		Card layout 
 *		Launch window from button 
 *		Minimise window 
 *		Close window 
 * 
 */

public class E01_S004_TC01_DesignAndImplementFloatingWindows extends BaseSeleniumTest {

    @Autowired
    protected EventsLogin login;

    @Test
    public void teststory004() throws Exception {
        int i, j;

        //open URL
        selenium.open("/EniqEventsUI/");
        pause(100);

        login.logIn();

        selenium.setSpeed("500");
        //Loop each tab "i"
        for (i = 0; i < 4; i++) {
            //setup for tab i
            final String tab = "//li[@id='x-auto-2__" + TABNAME[i] + "_TAB']/a[@class=\"x-tab-right\"]";
            final String startButton = "//table[@id='" + TABNAME[i] + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
            final String[] startMenu = getStartmenu(i);
            //open tab
            selenium.click(tab);
            //loop to open each window option on the start menu
            //cascade and tile? Not in this US
            //exit loop then move onto next tab.
            for (j = 0; j < startMenu.length; j++) {
                //setup for menuItem
                final String windowName = startMenu[j];
                checkMenuItemEnabled(startButton, windowName);
                openWindow(startButton, windowName);
                checkTaskbarButton(windowName);
                checkMenuItemDisabled(startButton, windowName);
                checkWindowControls(windowName);
            }
            selenium.open("/EniqEventsUI/");
        }
        //Close window check: open each window, close and refresh GUI
        checkClose();
    }

    //get menu for tab
    public static String[] getStartmenu(final int a) {
        String[] startMenu = TAB0_STARTMENU;
        switch (a) {
        case 0:
            startMenu = TAB0_STARTMENU;
            break;
        case 1:
            startMenu = TAB1_STARTMENU;
            break;
        case 2:
            startMenu = TAB2_STARTMENU;
            break;
        case 3:
            startMenu = TAB3_STARTMENU;
            break;
        }
        return startMenu;
    }

    //open window
    public void openWindow(final String startButton, final String windowName) {
        selenium.click(startButton);
        selenium.click(windowName);
    }

    //check button for window is present on the task bar
    public void checkTaskbarButton(final String menuItem) {
        assertTrue(selenium.isElementPresent(TASKBAR_BUTTON_TAG + menuItem));
    }

    //Verify menu item is disabled
    public void checkMenuItemDisabled(final String startButton, final String menuItem) {
        selenium.click(startButton);
        assertTrue(selenium.isElementPresent(menuItem));
        assertEquals("x-menu-list-item x-item-disabled", selenium.getAttribute("x-menu-el-" + menuItem + "@class"));
    }

    //Check minimize, restore, maximize & restore
    public void checkWindowControls(final String windowName) {
        //ClickMin(MAXIMIZE, COORDS);
        selenium.click(MINIMIZE);
        assertFalse(selenium.isVisible(WINDOW_TAG + windowName));
        selenium.click(TASKBAR_BUTTON_TAG + windowName);
        assertTrue(selenium.isVisible(WINDOW_TAG + windowName));
        selenium.click(MAXIMIZE);
        assertEquals(" x-window  x-window-maximized ", selenium.getAttribute(WINDOW_TAG + windowName + "@class"));
        selenium.click(RESTORE);
        assertEquals(" x-window ", selenium.getAttribute(WINDOW_TAG + windowName + "@class"));
    }

    //Verify menu item is enabled
    public void checkMenuItemEnabled(final String startButton, final String menuItem) {
        selenium.click(startButton);
        assertTrue(selenium.isElementPresent(menuItem));
        assertEquals("x-menu-list-item", selenium.getAttribute("x-menu-el-" + menuItem + "@class"));
    }

    public void checkClose() {
        int m, n;
        for (m = 0; m < TABNAME.length; m++) {
            final String tab = "//li[@id='x-auto-2__" + TABNAME[m] + "_TAB']/a[2]/em/span/span";
            final String startButton = "//table[@id='" + TABNAME[m] + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
            final String[] startMenu = getStartmenu(m);
            for (n = 0; n < startMenu.length; n++) {
                selenium.click(tab);
                final String windowName = startMenu[n];
                openWindow(startButton, windowName);
                selenium.click(CLOSE);
                selenium.open("/EniqEventsUI/");
            }
        }
    }
}
