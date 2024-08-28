/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.network.coreps;

import static com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

public class CoreRankingSubscriberTests extends EniqEventsUIBaseSeleniumTest {

    public static final String CATEGORY_CORE_RANKING = "Core Ranking";

    private final static long MINUTE1 = 60000, MINUTE3 = 180000, MINUTE6 = 360000;
    private final static String TIME_GAP_ONE_CSS = "timeGapOne", TIME_GAP_TWO_CSS = "timeGapTwo", TIME_GAP_THREE_CSS = "timeGapThree";

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("subRankings")
    private CommonWindow subRankingsWindow;
    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysisWindow;
    @Autowired
    @Qualifier("CoreRanking")
    private CommonWindow eventsSubscriberRankingWindow;
    @Autowired
    @Qualifier("ImsiEventAnalysis")
    private CommonWindow imsiEventAnalysisWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Core(PS) Network, Core Ranking, Subscriber Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Core(PS) Network, Core Ranking, Subscriber Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    /**
     * TSID: 14906 Title: Confirm drill down on grid hyperlinks uses the same time range as the original query.
     * 
     * @throws PopUpException
     * @throws NoDataException
     * @throws InterruptedException
     */
    @Test
    public void confirmDrillDownOnGridHyperlinksUsesTheSameTimeRangeAsTheOriginalQuery_EQEV_11095() throws NoDataException, PopUpException,
            InterruptedException {

        /* Span Ids to drill on. */
        final String rankingGridImsiColumn = "SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED";
        final String imsiEventAnalysisTacColumn = "SUBSCRIBER_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_TAC";
        final String tacEventAnalysisFailuresColumn = "SUBSCRIBER_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_BY_TAC";

        /* Launch the Events Subscriber Ranking window. */
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_30);
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
        workspace.launch();
        waitForPageLoadingToComplete();

        /* With the Core Ranking for Subscriber grid open, note the time range in the footer. */
        String footerTime = eventsSubscriberRankingWindow.getFooterTimeRange();

        /*
         * Wait for 15 minutes (configuration parameter), then click on the first Subscriber, note the time range in the footer of the IMSI Event
         * Analysis grid.
         */

        WorkspaceUtils.sleepInFooterTimeRangeTests();
        eventsSubscriberRankingWindow.clickRankingDrills(rankingGridImsiColumn, GuiStringConstants.IMSI);
        Thread.sleep(2000); // Sleep to allow the TimeDots to appear.
        makeAssertionOnFooterTime(footerTime, "IMSI Event Analysis");

        /*
         * Wait for 15 minutes (configuration parameter) and drill on a TAC, note the footer time range of the TAC Event Analysis grid.
         */

        WorkspaceUtils.sleepInFooterTimeRangeTests();
        imsiEventAnalysisWindow.clickRankingDrills(imsiEventAnalysisTacColumn, GuiStringConstants.TAC);
        makeAssertionOnFooterTime(footerTime, "TAC Event Analysis");

        /*
         * Wait for 15 minutes (configuration parameter) and drill on a Failures, note the footer time range of the Failed Event Analysis grid.
         */

        WorkspaceUtils.sleepInFooterTimeRangeTests();
        imsiEventAnalysisWindow.clickRankingDrills(tacEventAnalysisFailuresColumn, GuiStringConstants.FAILURES);
        makeAssertionOnFooterTime(footerTime, "TAC Failed Event Analysis");

        /* Use the back button whilst check the footer time range. */
        imsiEventAnalysisWindow.clickBackwardNavigation();
        makeAssertionOnFooterTime(footerTime, "TAC Event Analysis");
        imsiEventAnalysisWindow.clickBackwardNavigation();
        makeAssertionOnFooterTime(footerTime, "IMSI Event Analysis");

        /* Use the forward button whilst check the footer time range. */
        imsiEventAnalysisWindow.clickForwardNavigation();
        makeAssertionOnFooterTime(footerTime, "TAC Event Analysis");
        imsiEventAnalysisWindow.clickForwardNavigation();
        makeAssertionOnFooterTime(footerTime, "TAC Failed Event Analysis");

        /*
         * Change the TimeRange, press the back button, press forward check the time range. The footerTime range should be the same as what it was
         * changed to, before the back button was clicked.
         */
        imsiEventAnalysisWindow.setTimeRange(TimeRange.ONE_HOUR);
        footerTime = imsiEventAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(2);
        imsiEventAnalysisWindow.clickBackwardNavigation();
        imsiEventAnalysisWindow.clickForwardNavigation();
        makeAssertionOnFooterTime(footerTime, "TAC Failed Event Analysis");

        /*
         * Change the TimeRange back to 30 mins, click the back button, set the IMSI EA grid to another value, click the forward button. Check the
         * TimeRange of the TAC EA grid. It should be the same as it was previously.
         */
        imsiEventAnalysisWindow.setTimeRange(TimeRange.THIRTY_MINUTES);
        footerTime = imsiEventAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(2);
        imsiEventAnalysisWindow.clickBackwardNavigation();
        imsiEventAnalysisWindow.setTimeRange(TimeRange.ONE_HOUR);
        imsiEventAnalysisWindow.clickForwardNavigation();
        makeAssertionOnFooterTime(footerTime, "TAC Failed Event Analysis");

    }

    /**
     * TSID: 13752 Title: Verify that all the grids with the "Event Result" column display the colour coded bars.
     */
    @Test
    public void verifyThatAllTheGridsWithTheEventResultColumnDisplayTheColourCodedBars_EQEV_6705() {

        try {
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
            workspace.launch();
            waitForPageLoadingToComplete();

            try {
                eventsSubscriberRankingWindow.clickRankingDrills("SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED", GuiStringConstants.IMSI);
            } catch (NoDataException e) {
                fail("There was no Data in the Core(PS) Network, Core Ranking, Subscriber, Events Subscriber Ranking.");
            }

            assertTrue(
                    "The colorBlock is not present in the Core(PS) Network, 4g Ranking, Core PS, Controller Ranking, Controller Event Analysis, Failed Event Analysis grid.",
                    imsiEventAnalysisWindow.isEventResultColumnColorBlockPresent());

        } catch (PopUpException e) {
            fail("The " + GuiStringConstants.SUBSCRIBER + " grid did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault with the Core(PS) Network.");
        }
    }

    @Test
    public void drillBySubscriberToViewTheEventsEventResultsTimeGap() throws Exception {

        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_30);
        openRankingWindow(CATEGORY_CORE_RANKING, SUBSCRIBER);

        openEventAnalysis(subRankingsWindow, SeleniumConstants.IMSI, SeleniumConstants.IMSI);
        closePreviousWindow();

        assertTrue("Time Gaps are not present in window even though time difference between events is greater than one minute.",
                checkTimeGap(subscriberEventAnalysisWindow));
    }

    // ---------- Private Method ----------
    private void openEventAnalysis(final CommonWindow rankingWindow, final String networkType, final String columnName) throws NoDataException,
            PopUpException {
        rankingWindow.clickRankingDrills("SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED", columnName);
        waitForPageLoadingToComplete();
    }

    private boolean checkTimeGap(final CommonWindow window) throws NoDataException {
        boolean areTimeGapsPresent = true;
        window.sortTable(SortType.DESCENDING, GuiStringConstants.EVENT_TIME);

        for (int i = 1; i < window.getTableRowCount(); i++) {
            String currentRowDateTimeString = window.getTableData(i, 0);
            String previousRowDateTimeString = window.getTableData(i - 1, 0);
            String timeGapStyle = compareTimesToDetermineTimeGapClassName(currentRowDateTimeString, previousRowDateTimeString);
            boolean isTimeGapPresent = window.isCSSClassPresentOnTableRow(i + 1, timeGapStyle);

            if (!isTimeGapPresent) {
                areTimeGapsPresent = false;
                break;
            }
        }
        return areTimeGapsPresent;
    }

    private String compareTimesToDetermineTimeGapClassName(String currentRowDateTimeString, String previousRowDateTimeString) {
        String timeGapClassName = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date currentRowDate = dateFormat.parse(currentRowDateTimeString);
            Date previousRowDate = dateFormat.parse(previousRowDateTimeString);
            long currentRowTime = currentRowDate.getTime();
            long previousRowTime = previousRowDate.getTime();
            long timeDifference = currentRowTime - previousRowTime;

            if (timeDifference >= MINUTE1 && timeDifference <= MINUTE3) {
                timeGapClassName = TIME_GAP_ONE_CSS;
            } else if (timeDifference > MINUTE3 && timeDifference <= MINUTE6) {
                timeGapClassName = TIME_GAP_TWO_CSS;
            } else if (timeDifference > MINUTE6) {
                timeGapClassName = TIME_GAP_THREE_CSS;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeGapClassName;
    }

    private void openRankingWindow(String categoryPanel, String windowOption) throws Exception {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void closePreviousWindow() {
        String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";
        selenium.click(closeButtonXPath);
    }

    private void makeAssertionOnFooterTime(String footerTime, String gridName) {
        assertTrue("The Footer Time Ranges are different for " + gridName + ". " + "Expected: " + footerTime + ". " + "Actual: "
                + imsiEventAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(2) + ".",
                footerTime.equals(imsiEventAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(2)));
    }
}