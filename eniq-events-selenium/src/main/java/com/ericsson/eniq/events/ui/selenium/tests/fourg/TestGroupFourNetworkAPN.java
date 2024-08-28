package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author earimon
 * @since 2011
 * 
 */

public class TestGroupFourNetworkAPN extends BaseFourGTest {

    static final String RANK = "Rank";

    static final String APN = "APN";

    static final String FAILURES = "Failures";

    static final String SUCCESSES = "Successes";

    final TimeRange timeReference = TimeRange.THIRTY_MINUTES;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow networkAPNCoreWindow;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;

    /*
     * Test Cases: 4.5.14, 4.5.18, 4.5.22, 4.5.26, 4.5.30, 4.5.34, 4.5.38, 4.5.42
     * and 4.5.46
     */

    @Test
    public void networkTabAPN() throws PopUpException, NoDataException {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.EVENT_RANKING,
                NetworkTab.SubOfSubStartMenu.EVENT_RANKING_APN);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN, FAILURES, SUCCESSES));

        checkStringListContainsArray(networkAPNCoreWindow.getTableHeaders(),
                expectedHeaders.toArray(new String[expectedHeaders.size()]));

        networkAPNCoreWindow.setTimeRange(timeReference);
        /*
         * Picks the APN from the first row
         */
        final int testRow = 0;
        final List<String> allAPN = networkAPNCoreWindow.getAllTableDataAtColumn(APN);
        final int row = networkAPNCoreWindow.findFirstTableRowWhereMatchingAnyValue(APN, allAPN.get(testRow));
        final Map<String, String> result = networkAPNCoreWindow.getAllDataAtTableRow(row);

        /*
         * Drill down to the reserve APN's Event Analysis page.Verify that the
         * number of events on the Event Analysis page matches the number of events
         * (Success + Fail) specified for the reserve APN on theSubscriber
         * Rankingspage.
         */

        final Integer totalFailure = Integer.parseInt(result.get(FAILURES));

        final Integer totalSuccess = Integer.parseInt(result.get(SUCCESSES));

        networkAPNCoreWindow.clickTableCell(testRow, 1, "gridCellLauncherLink");

        /*
         * Gather the number of rows populated on the Event Analysis window
         */
        final Integer rowCount = networkEventAnalysis.getTableRowCount();

        Map<String, String> eventAnalysisData = null;

        Integer totalSuccessInAnalysis = 0;
        Integer totalFailureInAnalysis = 0;

        /*
         * Summing up all the failures for each LTE events Summing up all the
         * succeses for each LTE events
         */
        for (int count = 0; count < rowCount; count++) {
            eventAnalysisData = networkEventAnalysis.getAllDataAtTableRow(count);
            totalSuccessInAnalysis += Integer.parseInt(eventAnalysisData.get(SUCCESSES));

            totalFailureInAnalysis += Integer.parseInt(eventAnalysisData.get(FAILURES));
        }

        /*
         * Checking the equality with the original success and failure values
         * received from the Access Area Ranking
         */

        assertEquals(totalFailureInAnalysis, totalFailure);
        assertEquals(totalSuccessInAnalysis, totalSuccess);

        /*
         * To Verify the temporal reference is maintained after drill down.
         */
        final String timeDisplay = networkEventAnalysis.getTimeRange();
        assertEquals(timeReference, timeDisplay);
    }
}
