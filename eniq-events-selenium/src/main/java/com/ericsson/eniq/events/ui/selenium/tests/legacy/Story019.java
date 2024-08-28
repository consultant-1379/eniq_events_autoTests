package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;
import org.junit.Before;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;

/*
 * Epic 1 : Story 019 : Refresh Button
 * 
 * The following behaviour needs to be verified
 * 		Refresh button present on window toolbar
 * 		Pressing refresh button remakes call to server with the same 
 * 			Parameters as was initially used to populate the window. 
 */
public class Story019 extends SeleneseTestCase {
    @Override
    @Before
    public void setUp() throws Exception {
        setUp("http://10.42.33.235:10086/", "*firefox");
        //setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*iexplore");
        //setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*chrome");
    }

    public void teststory019() throws Exception {
        int i, j;

        //open URL
        selenium.open("/EniqEventsUI/");
        selenium.windowMaximize();

        //login
        //Common.logIn(selenium);
        Thread.currentThread();

        //Loop each tab "i"
        //for (i=0; i<1; i++){ ALL TABS
        //REFRESH BUTTON ONLY IMPLEMENTED ON FIRST WINDOW OF FIRST TAB 
        for (i = 0; i < 4; i++) {
            //setup for tab i
            final String tab = "//li[@id='x-auto-2__" + TABNAME[i] + "_TAB']/a[2]/em/span/span";
            final String startButton = "//table[@id='" + TABNAME[i] + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
            final String[] startMenu = getStartmenu(i);
            //
            selenium.click(tab);
            for (j = 0; j < startMenu.length; j++) {
                //setup for menuItem
                final String windowName = startMenu[j];
                openWindow(startButton, windowName);
                final String mystring = selenium.getText("//td[3]/label");
                if (mystring.equals("") == false) {
                    final Date firstRefreshTime = getRefreshTime(selenium.getText("//td[3]/label"));
                    Thread.sleep(60000);
                    selenium.click(REFRESH_BUTTON);
                    Thread.sleep(5000);
                    final Date thisRefreshTime = getRefreshTime(selenium.getText("//td[3]/label"));
                    assertTrue(firstRefreshTime.before(thisRefreshTime));
                }
            }
        }
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

    public Date getRefreshTime(final String displayedTime) {
        final String strFormat = "yy-MM-dd HH:mm";
        final DateFormat formatter = new SimpleDateFormat(strFormat);
        Date refreshTime = null;
        try {
            refreshTime = formatter.parse(displayedTime);
        } catch (final ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return refreshTime;
    }
}
