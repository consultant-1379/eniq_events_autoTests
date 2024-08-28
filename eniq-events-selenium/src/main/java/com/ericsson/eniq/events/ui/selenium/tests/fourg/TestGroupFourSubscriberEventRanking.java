package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author earimon
 * @since 2011
 * 
 */

public class TestGroupFourSubscriberEventRanking extends BaseFourGTest {

    static final String RANK = "Rank";

    static final String IMSI = "IMSI";

    static final String FAILURES = "Failures";

    static final String SUCCESS = "Successes";

    final TimeRange timeToLoadReservedData = TimeRange.ONE_WEEK;

    @Autowired
    private SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberrankingcore")
    private CommonWindow subscriberRankingsDataVolume;

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of 4G drill down of Subscriber Ranking");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of 4G drill down of Subscriber Ranking");
    }

    /*
     * Test Cases: 4.5.12, 4.5.16, 4.5.20, 4.5.24, 4.5.28, 4.5.32, 4.5.36, 4.5.40
     * and 4.5.44
     */
    @Test
    public void subscriberRankingEventRanking() throws Exception {
        subscriberTab.openTab();

        subscriberTab
                .openSubOfSubStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                        SubscriberTab.SubStartMenu.EVENT_RANKING,
                        SubscriberTab.SubOfSubStartMenu.SUBSCRIBER_EVENT_RANKING_CORE);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI, FAILURES, SUCCESS));

        checkStringListContainsArray(subscriberRankingsDataVolume.getTableHeaders(),
                expectedHeaders.toArray(new String[expectedHeaders.size()]));

        /*
         * Setting time reference
         */
        subscriberRankingsDataVolume.setTimeRange(timeToLoadReservedData);

        /*
         * Picks the IMSI from the first row
         */
        final int testRow = 1;
        final List<String> allIMSI = subscriberRankingsDataVolume.getAllTableDataAtColumn(IMSI);
        final int row = subscriberRankingsDataVolume.findFirstTableRowWhereMatchingAnyValue(IMSI, allIMSI.get(testRow));
        final Map<String, String> result = subscriberRankingsDataVolume.getAllDataAtTableRow(row);

        /*
         * Drill down to the reserve IMSI's Event Analysis page.Verify that the
         * number of events on the Event Analysis page matches the number of events
         * (Success + Fail) specified for the reserve IMSI on theSubscriber
         * Rankingspage.
         */
        final Integer totalOfSuccessFailure = Integer.parseInt(result.get("Failures"))
                + Integer.parseInt(result.get("Successes"));

        subscriberRankingsDataVolume.clickTableCell(testRow, 1, "gridCellLauncherLink");

        Integer rowCount = 0;
        final int pageCount = subscriberEventAnalysis.getPageCount();
        int currentPage = subscriberEventAnalysis.getCurrentPageNumber();
        rowCount += subscriberEventAnalysis.getTableRowCount();

        while (currentPage != pageCount) {

            subscriberEventAnalysis.clickNextPage();
            currentPage = subscriberEventAnalysis.getCurrentPageNumber();
            rowCount += subscriberEventAnalysis.getTableRowCount();
        }

        assertEquals(rowCount, totalOfSuccessFailure);

        /*
         * To Verify the temporal reference is maintained after drill down.
         */
        final String timeDisplay = subscriberEventAnalysis.getTimeRange();
        assertEquals(timeToLoadReservedData, timeDisplay);
    }
}
