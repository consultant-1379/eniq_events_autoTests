/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author ekeviry
 * @since 2011
 * 
 */
public class GeneralTestCases extends BaseFourGTest {

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("termRankings")
    private CommonWindow termRankingsWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Ranking test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Ranking test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        rankingsTab.openTab();
    }

    ArrayList<String> eventTypeUI = new ArrayList<String>();

    IMSIDataFromUI dataFromUI = new IMSIDataFromUI();

    IMSIDataFromDatabase dataFromDB = new IMSIDataFromDatabase();

    // final String[] specifiedTerminalRankingHeaders = { "TAC", "Failures" };

    /**
     * Requirement: ************** Test Case: ENIQ_EE_5.13: Timezone Handling
     * Purpose: Timestamps displayed on ENIQ Events User Interface use local
     * time. That is, user interfaces use the time settings of the client
     * machine on which they run to display timezone information, therefore a
     * conversion from UTC to local time shall be applied.
     */
    @Test
    public void ENIQ_E_5_5_timeZoneHandling() throws Exception {

        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING,
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL);
        waitForPageLoadingToComplete();

        getUITimeRangeFromPropertiesFile(termRankingsWindow);
        waitForPageLoadingToComplete();

        int[] columnNumbers = getColumnNumbers(termRankingsWindow, specifiedTerminalRankingHeaders);

        final int failuresColumn = columnNumbers[1];

        termRankingsWindow.clickTableCell(0, failuresColumn, "gridCellLink");
        waitForPageLoadingToComplete();

        columnNumbers = getColumnNumbers(termRankingsWindow, specifiedTerminalEventAnalysisHeaders);

        final int eventTimeColumn = columnNumbers[0];
        final int imsiColumn = columnNumbers[1];

        final String topEventTime = termRankingsWindow.getTableData(0, eventTimeColumn);
        final String topIMSIValue = termRankingsWindow.getTableData(0, imsiColumn);

        final int numberOfEvents = IMSIDataFromDatabase.getNumberOfEventsForGivenIMSI(topIMSIValue);

        final Timestamp[] eventTimeWithoutTimezoneOffset = IMSIDataFromDatabase.getEventTime(topIMSIValue,
                numberOfEvents);

        final String[] eventTimeSet = IMSIDataFromDatabase.timezoneOffsetHandling(eventTimeWithoutTimezoneOffset);
        final String firstDBTime = eventTimeSet[0];

        if (!topEventTime.equals(firstDBTime)) {
            logger.log(Level.SEVERE, "IMSI: " + topIMSIValue + "\nFirst UI Time: " + topEventTime + "\nFirst DB Time: "
                    + firstDBTime);
        }

        assertTrue("Timezone not handled correctly", topEventTime == firstDBTime);

        pause(2000);
    }
}
