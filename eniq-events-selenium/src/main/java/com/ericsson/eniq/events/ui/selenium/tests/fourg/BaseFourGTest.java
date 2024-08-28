/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author ekeviry
 * @since 2011
 *
 */
public class BaseFourGTest extends EniqEventsUIBaseSeleniumTest {

    //    @Autowired
    //    private NetworkTab networkTab;
    //
    //    @Autowired
    //    @Qualifier("networkEventAnalysis")
    //    private CommonWindow eventAnalysisWindow;
    //
    //    @Autowired
    //    @Qualifier("eventVolume")
    //    private CommonWindow eventVolumeWindow;

    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Network test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Network test section");
    }

    final String[] specifiedTerminalRankingHeaders = { "TAC", "Failures" };

    // **** Update with correct column headers ****
    final String[] specifiedTerminalEventAnalysisHeaders = { "TAC", "Failures" };

    final String[] specifiedAPNRankingHeaders = { "APN", "Failures", "Successes" };

    final String[] specifiedECellRankingHeaders = { "Access Area", "Failures", "Successes" };

    public String getUITimeRangeFromPropertiesFile(final CommonWindow rankingsWin) throws FileNotFoundException,
            IOException, PopUpException {
        final int timeRange = RegressionPropertiesFileReader.getTimeRangeFromPropFile();
        String propFileTimeRange;

        if (timeRange == 7) {
            setTimeRangeToOneWeek(rankingsWin);
            propFileTimeRange = "1 week";
        }

        else {
            setTimeRangeToOneDay(rankingsWin);
            propFileTimeRange = "1 day";
        }
        return propFileTimeRange;
    }

    public int[] getColumnNumbers(final CommonWindow window, final String[] specifiedHeaders) {
        final ArrayList<String> allTableHeaders = (ArrayList<String>) window.getTableHeaders();

        final int[] headerLocations = new int[specifiedHeaders.length];

        for (int j = 0; j < specifiedHeaders.length; j++) {

            for (int i = 0; i < allTableHeaders.size(); i++) {
                if (specifiedHeaders[j].equals(allTableHeaders.get(i))) {
                    headerLocations[j] = i;

                    break;
                }
            }
        }
        // System.out.print("Columns: ");
        // for (int i = 0; i < headerLocations.length; i++) {
        // System.out.print(headerLocations[i] + ", ");
        // }
        // System.out.println("");

        return headerLocations;
    }

}
