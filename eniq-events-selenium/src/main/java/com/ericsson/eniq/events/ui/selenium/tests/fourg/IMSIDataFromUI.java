/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh.BaseSubscriber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author ekeviry/eantmcm
 * @since 2011
 * 
 */
public class IMSIDataFromUI extends BaseSubscriber {

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberSession test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberSession test section");
    }

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;

    /**
     * Enter one of the IMSI number and open the Event Analysis window using it.
     * Then set time selection to 1 week and sort the result.
     * 
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void openIMSIEventAnalysisWindow(final String imsiValue, final boolean useStartMenu) throws NoDataException,
            PopUpException, FileNotFoundException, IOException {
        openPreSelectedIMSI(imsiValue, SubscriberTab.ImsiMenu.IMSI, useStartMenu, false);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        final int timeRange = RegressionPropertiesFileReader.getTimeRangeFromPropFile();

        if (timeRange == 7) {
            setTimeRangeToOneWeek(subscriberEventAnalysis);
        }

        else {
            setTimeRangeToOneDay(subscriberEventAnalysis);
        }
    }

    public void openIMSIEventAnalysisWindowDefaultTime(final String imsiValue, final boolean useStartMenu)
            throws NoDataException, PopUpException, FileNotFoundException, IOException {
        openPreSelectedIMSI(imsiValue, SubscriberTab.ImsiMenu.IMSI, useStartMenu, false);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));
    }

    public void openPreSelectedIMSI(final String imsiValue, final ImsiMenu type, final boolean useStartMenu,
            final boolean group) throws PopUpException {
        subscriberTab.openTab();
        subscriberTab.setSearchType(type);
        pause(7000); // eseuhon, DO NOT change this otherwise you won't expect
        // below method working normally.

        subscriberTab.enterSearchValue(imsiValue, group);
        pause(2000);

        if (useStartMenu) {
            subscriberTab.openStartMenu(SubscriberTab.StartMenu.EVENT_ANALYSIS);
        } else {
            subscriberTab.enterSubmit(group);
        }
        waitForPageLoadingToComplete();
        pause(3000);
    }

    public ArrayList<String> searchSubscriberEventType(final String eventValue) throws NoDataException,
            PopUpException {

        // Level 3
        final List<Map<String, String>> tableData = subscriberEventAnalysis.getAllTableData();
        final ArrayList<String> resultSetUI = new ArrayList<String>();

        for (int i = 0; i < tableData.size(); i++) {
            final Map<String, String> tableDataAtCurrentRow = tableData.get(i);
            final Map<String, String> tableMapUI = tableData.get(i);
            if (tableDataAtCurrentRow.get("Event Type").equals(eventValue)) {

                resultSetUI.add(tableMapUI.get("Event Time"));

                resultSetUI.add(tableMapUI.get("TAC"));
                resultSetUI.add(tableMapUI.get("Terminal Make"));
                resultSetUI.add(tableMapUI.get("Terminal Model"));

                resultSetUI.add(tableMapUI.get("Event Result"));

                resultSetUI.add(tableMapUI.get("Cause Code"));
                resultSetUI.add(tableMapUI.get("Sub Cause Code"));
                resultSetUI.add(tableMapUI.get("SGSN-MME"));
                resultSetUI.add(tableMapUI.get("Controller"));
                resultSetUI.add(tableMapUI.get("Access Area"));
                resultSetUI.add(tableMapUI.get("RAN Vendor"));
                resultSetUI.add(tableMapUI.get("APN"));
                resultSetUI.add(tableMapUI.get("MCC"));
                resultSetUI.add(tableMapUI.get("MNC"));
            }
        }
        return resultSetUI;
    }

    public void verifyEventTypeIsPresent(final String eventType, final CommonWindow eventAnalysisWindow)
            throws NoDataException, PopUpException {

        final List<Map<String, String>> tableData = eventAnalysisWindow.getAllTableData();

        boolean containsEventType = false;

        for (int i = 0; i < tableData.size(); i++) {
            final Map<String, String> tableDataAtCurrentRow = tableData.get(i);

            if (tableDataAtCurrentRow.get("Event Type").equals(eventType)) {
                containsEventType = true;
            }
        }

        assertTrue("No " + eventType + " events found for IMSI ", containsEventType == true);
    }
}
