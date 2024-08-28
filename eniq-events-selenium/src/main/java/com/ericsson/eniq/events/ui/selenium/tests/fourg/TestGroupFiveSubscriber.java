/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author ekeviry
 * @since 2011
 * 
 */
public class TestGroupFiveSubscriber extends IMSIDataFromUI {

    ArrayList<String> eventTypeUI = new ArrayList<String>();

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;

    protected String[] specifiedSubscriberColumnHeaders = { "IMSI", "Failures", "Successes" };

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.12: Verify 4G data is
     * displayed in the Subscriber ranking view for L_ATTACH event Purpose: To
     * verify that 4G data is displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLATTACHevent_5_12() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_ATTACH");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.16: Verify 4G data is
     * displayed in the Subscriber ranking view for L_DETACH event Purpose: To
     * verify that 4G data is displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLDETACHevent_5_16() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_DETACH");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.20: Verify 4G data is
     * displayed in the Subscriber ranking view for L_SERVICE_REQUEST event
     * Purpose: To verify that 4G data is displayed in the Subscriber ranking
     * view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLSERVICEREQUESTevent_5_20() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_SERVICE_REQUEST");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.24: Verify 4G data is
     * displayed in the Subscriber ranking view for L_HANDOVER event Purpose: To
     * verify that 4G data is displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLHANDOVERevent_5_24() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_HANDOVER");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.28: Verify 4G data is
     * displayed in the Subscriber ranking view for L_TAU event Purpose: To
     * verify that 4G data is displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLTAUevent_5_28() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_TAU");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.32: Verify 4G data is
     * displayed in the Subscriber ranking view for L_PDN_CONNECT event Purpose:
     * To verify that 4G data is displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLPDNCONNECTevent_5_32() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_PDN_CONNECT");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.36: Verify 4G data is
     * displayed in the Subscriber ranking view for L_PDN_DISCONNECT event
     * Purpose: To verify that 4G data is displayed in the Subscriber ranking
     * view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLPDNDISCONNECTevent_5_36() throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_PDN_DISCONNECT");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.40: Verify 4G data is
     * displayed in the Subscriber ranking view for L_DEDICATED_BEARER_ACTIVATE
     * event Purpose: To verify that 4G data is displayed in the Subscriber
     * ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLDEDICATEDBEARERACTIVATEevent_5_40()
            throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_DEDICATED_BEARER_ACTIVATE");
    }

    /**
     * Requirement: 105 65-0528/00391 Test Case: ENIQ_EE_5.44: Verify 4G data is
     * displayed in the Subscriber ranking view for
     * L_DEDICATED_BEARER_DEACTIVATE event Purpose: To verify that 4G data is
     * displayed in the Subscriber ranking view
     */
    @Test
    public void verify4GDataIsDisplayedInTheSubscriberRankingViewForLDEDICATEDBEARERDEACTIVATEevent_5_44()
            throws Exception {

        verify4GDataIsDisplayedInTheSubscriberRankingView("L_DEDICATED_BEARER_DEACTIVATE");
    }

    // /////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    // /////////////////////////////////////////////////////////////////////////////

    private void verify4GDataIsDisplayedInTheSubscriberRankingView(final String eventType) throws PopUpException,
            FileNotFoundException, IOException, NoDataException, ParseException {

        final RegressionPropertiesFileReader propFileReader = new RegressionPropertiesFileReader();

        subscriberTab.openTab();
        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS);
        waitForPageLoadingToComplete();

        final String propFileTimeRange = getUITimeRangeFromPropertiesFile(subRankingsWindow);
        waitForPageLoadingToComplete();

        final int[] numberOfEvents = getNumberOfEventsSubscriber();

        eventTypeUI = searchSubscriberEventType(eventType);

        assertFalse("No " + eventType + " events found for IMSI " + propFileReader.readIMSIValue(),
                eventTypeUI.size() == 0);

        verifyNumberOfEvents(numberOfEvents);

        // Action 2
        final String eventAnalysisTimeRange = subscriberEventAnalysis.getTimeRange();
        assertTrue("Selected temporal reference not maintained", propFileTimeRange.equals(eventAnalysisTimeRange));

    }

    private int[] getNumberOfEventsSubscriber() throws NoDataException, FileNotFoundException, IOException,
            ParseException, PopUpException {

        final int[] columnNumbers = getColumnNumbers(subRankingsWindow, specifiedSubscriberColumnHeaders);
        final int imsiColumn = columnNumbers[0];

        final String subRankingTopIMSI = subRankingsWindow.getTableData(0, imsiColumn);
        System.out.println("Sub Ranking IMSI: " + subRankingTopIMSI);

        final int subRankingWindowTotal = getTotalSuccessesAndFailuresSubRankingWindow();
        System.out.println("Subscriber Ranking Window Total: " + subRankingWindowTotal);

        subRankingsWindow.clickTableCell(0, imsiColumn, "gridCellLauncherLink");

        final int subEventAnalysisEventCount = subscriberEventAnalysis.getTableRowCount();
        System.out.println("Subscriber Event Analysis Window Row Count: " + subEventAnalysisEventCount);

        // int numberOfEventsDB = 0;
        // if (defaultTime == true) {
        // numberOfEventsDB = IMSIDataFromDatabase
        // .getNumberOfEventsForDefaultTimeRange(subRankingTopIMSI);
        // System.out.println("Number of events in DB: " + numberOfEventsDB);
        // }
        //
        // else {
        // Use properties file time range
        final int numberOfEventsDB = IMSIDataFromDatabase.getNumberOfEventsForGivenIMSI(subRankingTopIMSI);
        System.out.println("Number of events in DB: " + numberOfEventsDB);
        // }

        final int[] numberOfEvents = { subRankingWindowTotal, subEventAnalysisEventCount, numberOfEventsDB };

        return numberOfEvents;
    }

    private void verifyNumberOfEvents(final int[] numberOfEvents) throws NoDataException {

        final int[] columnNumbers = getColumnNumbers(subRankingsWindow, specifiedSubscriberColumnHeaders);
        final int imsiColumn = columnNumbers[0];

        final String subRankingTopIMSI = subRankingsWindow.getTableData(0, imsiColumn);
        System.out.println("Sub Ranking IMSI: " + subRankingTopIMSI);

        final int subRankingWindowTotal = numberOfEvents[0];
        final int subEventAnalysisEventCount = numberOfEvents[1];
        final int numberOfEventsDB = numberOfEvents[2];

        if (subRankingWindowTotal != subEventAnalysisEventCount) {
            logger.log(Level.SEVERE, "Number of events - Subscriber Ranking Window: " + subRankingWindowTotal
                    + "\nNumber of events - Subscriber Event Analysis Window: " + subEventAnalysisEventCount
                    + "\nNumber of events - Database Raw Table: " + numberOfEventsDB);
        }

        if (numberOfEventsDB != subEventAnalysisEventCount) {
            logger.log(Level.SEVERE, "Number of events - Subscriber Ranking Window: " + subRankingWindowTotal
                    + "\nNumber of events - Subscriber Event Analysis Window: " + subEventAnalysisEventCount
                    + "\nNumber of events - Database Raw Table: " + numberOfEventsDB);
        }

        assertTrue("Differing number of events for IMSI " + subRankingTopIMSI,
                subRankingWindowTotal == subEventAnalysisEventCount);
        assertTrue("Differing number of events for IMSI " + subRankingTopIMSI,
                numberOfEventsDB == subEventAnalysisEventCount);

    }

    private int getTotalSuccessesAndFailuresSubRankingWindow() throws NoDataException {

        final int[] columnNumbers = getColumnNumbers(subRankingsWindow, specifiedSubscriberColumnHeaders);
        final int failuresColumn = columnNumbers[1];
        final int successesColumn = columnNumbers[2];

        final String subRankingFailuresString = subRankingsWindow.getTableData(0, failuresColumn);
        final String subRankingSuccessesString = subRankingsWindow.getTableData(0, successesColumn);

        final int subRankingFailures = Integer.parseInt(subRankingFailuresString);
        final int subRankingSuccesses = Integer.parseInt(subRankingSuccessesString);

        final int subRankingWindowTotal = subRankingFailures + subRankingSuccesses;

        return subRankingWindowTotal;
    }

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
