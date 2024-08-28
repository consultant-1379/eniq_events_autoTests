package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
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

public class TestGroupFourAccessArea extends BaseFourGTest {

    static final String RANK = "Rank";

    static final String RAT = "RAT";

    static final String RAN_VENDOR = "RAN Vendor";

    static final String CONTROLLER = "Controller";

    static final String ACCESS_AREA = "Access Area";

    static final String FAILURES = "Failures";

    static final String SUCCESSES = "Successes";

    final TimeRange timeReference = TimeRange.THIRTY_MINUTES;

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("cellRankings")
    private CommonWindow accessAreaWindow;

    @Autowired
    @Qualifier("networkEventAnalysisForAccessArea")
    private CommonWindow accessAreaEventAnalysis;

    /*
     * Test Cases: 4.5.15, 4.5.19, 4.5.23, 4.5.27, 4.5.31, 4.5.34, 4.5.39, 4.5.43
     * and 4.5.47
     */

    @Test
    public void rankingTabAcessArea() throws PopUpException, NoDataException {

        rankingsTab.openTab();

        final List<RankingsTab.SubStartMenu> drill = new ArrayList<RankingsTab.SubStartMenu>();
        drill.add(RankingsTab.SubStartMenu.EVENT_RANKING_ECELL);
        drill.add(RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA);
        rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, drill);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, RAT, RAN_VENDOR, CONTROLLER,
                ACCESS_AREA, FAILURES, SUCCESSES));

        checkStringListContainsArray(accessAreaWindow.getTableHeaders(),
                expectedHeaders.toArray(new String[expectedHeaders.size()]));

        accessAreaWindow.setTimeRange(timeReference);

        /*
         * Picks the Access Area from the first row
         */
        final int testRow = 0;
        final List<String> allAccessArea = accessAreaWindow.getAllTableDataAtColumn(ACCESS_AREA);
        final int row = accessAreaWindow
                .findFirstTableRowWhereMatchingAnyValue(ACCESS_AREA, allAccessArea.get(testRow));
        final Map<String, String> result = accessAreaWindow.getAllDataAtTableRow(row);

        /*
         * Drill down to the reserve Access Area Event Ranking page.Verify that the
         * number of events on the Event Analysis page matches the number of events
         * (Success + Fail) specified for the reserve Access Area.
         */
        final Integer totalFailure = Integer.parseInt(result.get("Failures"));

        final Integer totalSuccess = Integer.parseInt(result.get("Successes"));

        /*
         * Clickiing on the Access area Details for on particular row
         */
        accessAreaWindow.clickTableCell(testRow, 4, "gridCellLauncherLink");

        /*
         * Gather the number of rows populated on the Event Analysis window
         */
        final Integer rowCount = accessAreaEventAnalysis.getTableRowCount();

        Map<String, String> eventAnalysisData = null;

        Integer totalSuccessInAnalysis = 0;
        Integer totalFailureInAnalysis = 0;

        /*
         * Summing up all the failures for each LTE events Summing up all the
         * succeses for each LTE events
         */
        for (int count = 0; count < rowCount; count++) {
            eventAnalysisData = accessAreaEventAnalysis.getAllDataAtTableRow(count);
            totalSuccessInAnalysis += Integer.parseInt(eventAnalysisData.get(SUCCESSES));
            totalFailureInAnalysis += Integer.parseInt(eventAnalysisData.get(FAILURES));
        }

        /*
         * Checking the equality with the original success and failure values
         * received from the Access Area Ranking
         */
        assertEquals(totalFailureInAnalysis, totalFailure);
        assertEquals(totalSuccessInAnalysis, totalSuccess);

        // To Verify the temporal reference is maintained after drill down.

        final String timeDisplay = accessAreaEventAnalysis.getTimeRange();

        assertEquals(timeDisplay, timeReference);
    }
}
