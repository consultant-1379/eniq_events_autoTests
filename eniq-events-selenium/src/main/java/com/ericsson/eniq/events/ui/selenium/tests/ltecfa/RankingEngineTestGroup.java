/* COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.ltecfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class RankingEngineTestGroup extends EniqEventsUIBaseSeleniumTest {

    DataIntegrityValidationUtil objDataIntegrityValidationUtil = new DataIntegrityValidationUtil();

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    private WorkspaceRC workspacerc;

    @Autowired
    @Qualifier("subscriberRankingLTEcallSetupFailure")
    private CommonWindow subscriberRankingCallSetupFailure;

    @Autowired
    @Qualifier("subscriberRankingLTEcallDrop")
    private CommonWindow subscriberRankingCallDrop;

    @Autowired
    @Qualifier("subscriberRankingLTErecurringFailures")
    private CommonWindow subscriberRankingRecurringFailures;

    @Autowired
    @Qualifier("eNodeBrankingLTEcallSetupFailures")
    private CommonWindow eNodeBrankingCallSetupFailure;

    @Autowired
    @Qualifier("eNodeBrankingLTEcallDrop")
    private CommonWindow eNodeBrankingCallDrop;

    @Autowired
    @Qualifier("eCellRankingLTEcallSetupFailures")
    private CommonWindow eCellRankingCallSetupFailure;

    @Autowired
    @Qualifier("eCellRankingLTEcallDrop")
    private CommonWindow eCellRankingCallDrop;

    @Autowired
    @Qualifier("trackingAreaRankingLTEcallSetup")
    private CommonWindow trackingAreaRankingCallSetupFailure;

    @Autowired
    @Qualifier("trackingAreaRankingLTEcallDrop")
    private CommonWindow trackingAreaRankingCallDrop;

    @Autowired
    @Qualifier("causeCodeRankingLTEcallSetup")
    private CommonWindow causeCodeRankingCallSetupFailure;

    @Autowired
    @Qualifier("causeCodeRankingLTEcallDrop")
    private CommonWindow causeCodeRankingCallDrop;

    @Autowired
    @Qualifier("terminalRankingLTEcallSetup")
    private CommonWindow terminalRankingCallSetupFailure;

    @Autowired
    @Qualifier("terminalRankingLTEcallDrop")
    private CommonWindow terminalRankingCallDrop;

    @Autowired
    @Qualifier("subscriberEventAnalysisForLTECFA")
    private CommonWindow subscriberCallFailureAnalysis;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        workspacerc.checkAndOpenSideLaunchBar();
        pause(2000);
        pause(2000);
        objDataIntegrityValidationUtil.init(reservedDataHelper);
    }

    /**
     * Requirement: 105 65-0582/00251 Test Case: 6.2 Description: It shall be possible to view the top 50 Subscribers which are experiencing the most
     * call setup failures.
     */
    @Test
    public void SubscriberRankingCallSetupFailures_6_2() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_CALL_SETUP_FAILURE));
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        Thread.sleep(5000);
        validateDataOfFailuresRankingForAllTimeRanges(subscriberRankingCallSetupFailure, "SUBSCRIBER RANKING", GuiStringConstants.CALL_SETUP_FAILURES);
    }

    /**
     * Requirement: 105 65-0582/00251 Test Case: 6.3 Description: Call Setup Failure - Starting with Subscriber Ranking view it shall be possible
     * through IMSI to go to an IMSI Event Analysis view . From the IMSI Event Analysis view, it shall be possible to go by Event Type Failure Count
     * to a detailed Failed Event Analysis view.
     */
    @Test
    public void DrillDownIMSIsubscriberRankingCallSetupFailures_6_3() throws Exception {
        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_CALL_SETUP_FAILURE));
        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();
        drillDownIMSIOnEventAnalysisWindow(subscriberRankingCallSetupFailure);
    }

    /**
     * Requirement: 105 65-0582/00252 Test Case: 6.4 Description: It shall be possible to view the top 50 Subscribers which are experiencing the most
     * call drops.
     */
    @Test
    public void SubscriberRankingCallDrop_6_4() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_CALL_DROP));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();

        waitForPageLoadingToComplete();

        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.IMSI);
        listOfColumns.add(GuiStringConstants.FAILURES);
        subscriberRankingCallDrop.openTableHeaderMenu(0);
        subscriberRankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(subscriberRankingCallDrop, listOfColumns);

        validateDataOfFailuresRankingForAllTimeRanges(subscriberRankingCallDrop, "SUBSCRIBER RANKING", GuiStringConstants.CALL_DROPS);
        Thread.sleep(5000);
    }

    /**
     * Requirement: 105 65-0582/00252 Test Case: 6.5 Description: Call Drops - Starting with Subscriber Ranking view it shall be possible through IMSI
     * to go to an IMSI Event Analysis view. From the IMSI Event Analysis view, it shall be possible to go by Event Type Failure Count to a detailed
     * Failed Event Analysis view
     */
    @Test
    public void DrillDownIMSIsubscriberRankingCallDrop_6_5() throws Exception {
        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_CALL_DROP));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();

        drillDownIMSIOnEventAnalysisWindow(subscriberRankingCallDrop);
        pause(3000);
    }

    /**
     * Requirement: 105 65-0582/00253 Test Case: 6.6 Description: It shall be possible to view the top 50 Subscribers which are experiencing multiple
     * recurring failures in 30 minutes time range.
     */
    @Test
    public void SubscriberRankingRecurringFailures_6_6() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_RECURRING_FAILURES));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Recurring Failures");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", subscriberRankingRecurringFailures.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.IMSI);
        listOfColumns.add(GuiStringConstants.CONTROLLER);
        listOfColumns.add(GuiStringConstants.ACCESS_AREA);
        listOfColumns.add(GuiStringConstants.RAN_VENDOR);
        listOfColumns.add(GuiStringConstants.EVENT_TYPE);
        listOfColumns.add(GuiStringConstants.FAILURES);
        subscriberRankingRecurringFailures.openTableHeaderMenu(0);
        subscriberRankingRecurringFailures.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(subscriberRankingRecurringFailures, listOfColumns);

        subscriberRankingRecurringFailures.clickTableCell(0, 1, "gridCellLauncherLink");
        waitForPageLoadingToComplete();
        pause(5000);
        subscriberRankingRecurringFailures.clickButton(SelectedButtonType.PROPERTY_BUTTON);
        pause(3000);
    }

    /**
     * Requirement: 105 65-0582/00253 Test Case: 6.7 Description: Recurring Failures - Starting with Subscriber Ranking view it shall be possible
     * through IMSI to go to an IMSI Event Analysis view. From the IMSI Event Analysis view, it shall be possible to go by Event Type Failure Count to
     * a detailed Failed Event Analysis view
     */
    @Test
    public void DrillDownIMSIsubscriberRankingRecurringFailures_6_7() throws Exception {
        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
                RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_LTE_RECURRING_FAILURES));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Recurring Failures");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);
        waitForPageLoadingToComplete();
        drillDownIMSIOnEventAnalysisWindowForRecurrence("IMSI", subscriberRankingRecurringFailures);
        pause(3000);
    }

    /**
     * Requirement: 105 65-0582/00263 Test Case: 6.9 Description: It shall be possible to view the top 50 Tracking Areas which are experiencing the
     * most call setup failures.
     */
    @Test
    public void TrackingAreaRankingCallSetupFailure_6_9() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_TRACKING_AREA, RankingsTab.SubStartMenu.RANKING_TRACKING_AREA_LTE_CALL_SETUP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);

        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);

        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure by Tracking Area");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", trackingAreaRankingCallSetupFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.TRACKING_AREA);
        listOfColumns.add(GuiStringConstants.FAILURES);
        trackingAreaRankingCallSetupFailure.openTableHeaderMenu(0);
        trackingAreaRankingCallSetupFailure.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(trackingAreaRankingCallSetupFailure, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_SETUP_FAILURES, trackingAreaRankingCallSetupFailure);

        Thread.sleep(10000);
        validateDataOfFailuresRankingForAllTimeRanges(trackingAreaRankingCallSetupFailure, "TRACKING AREA RANKING",
                GuiStringConstants.CALL_SETUP_FAILURES);

    }

    /**
     * Requirement: 105 65-0582/00264 Test Case: 6.11 Description: It shall be possible to view the top 50 Tracking Areas which are experiencing the
     * most call drop failures.
     */
    @Test
    public void TrackingAreaRankingCallDrop_6_11() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_TRACKING_AREA, RankingsTab.SubStartMenu.RANKING_TRACKING_AREA_LTE_CALL_DROP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops by Tracking Area");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", trackingAreaRankingCallDrop.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.TRACKING_AREA);
        listOfColumns.add(GuiStringConstants.FAILURES);
        trackingAreaRankingCallDrop.openTableHeaderMenu(0);
        trackingAreaRankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(trackingAreaRankingCallDrop, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_DROPS, trackingAreaRankingCallDrop);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(trackingAreaRankingCallDrop, "TRACKING AREA RANKING", GuiStringConstants.CALL_DROPS);
    }

    /**
     * Requirement: 105 65-0582/00266 Test Case: 6.14 Description: It shall be possible to view the top 50 Cause Codes which are in result of call
     * setup failed events.
     */
    @Test
    public void CauseCodeRankingCallSetupFailure_6_14() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE, RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_RAN,
                RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_LTE, RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_CFA,
                RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_LTE_CALL_SETUP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Cause Code Ranking", "Call Setup Failure");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", causeCodeRankingCallSetupFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        causeCodeRankingCallSetupFailure.openTableHeaderMenu(0);
        causeCodeRankingCallSetupFailure.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(causeCodeRankingCallSetupFailure, listOfColumns);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(causeCodeRankingCallSetupFailure, "CAUSE CODE RANKING", GuiStringConstants.CALL_SETUP_FAILURES);
    }

    /**
     * Requirement: 105 65-0582/00267 Test Case: 6.15 Description: It shall be possible to view the top 50 Cause Codes which are in result of call
     * drops failed events.
     */
    @Test
    public void CauseCodeRankingCallDrop_6_15() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE, RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_RAN,
                RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_LTE, RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_CFA,
                RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_LTE_CALL_DROP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Cause Code Ranking", "Call Drops");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", causeCodeRankingCallDrop.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        causeCodeRankingCallDrop.openTableHeaderMenu(0);
        causeCodeRankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(causeCodeRankingCallDrop, listOfColumns);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(causeCodeRankingCallDrop, "CAUSE CODE RANKING", GuiStringConstants.CALL_DROPS);
    }

    /**
     * Requirement: 105 65-0582/00257 Test Case: 4.6.16 Description: It is possible to view the top 50 Terminals which are experiencing the most call
     * setup failures.
     */
    @Test
    public void TerminalRankingCallSetupFailure_6_16() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL, RankingsTab.SubStartMenu.RANKING_TERMINAL_RAN,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_LTE, RankingsTab.SubStartMenu.RANKING_TERMINAL_CFA,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_LTE_CALL_SETUP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure by Terminal");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", terminalRankingCallSetupFailure.getTableRowCount() <= 50);

        final List<String> headersToUnTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC,
                GuiStringConstants.TERMINAL_MAKE));

        terminalRankingCallSetupFailure.uncheckOptionalHeaderCheckBoxes(headersToUnTickIfPresent);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        terminalRankingCallSetupFailure.openTableHeaderMenu(0);
        terminalRankingCallSetupFailure.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(terminalRankingCallSetupFailure, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_SETUP_FAILURES, terminalRankingCallSetupFailure);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(terminalRankingCallSetupFailure, "TERMINAL RANKING", GuiStringConstants.CALL_SETUP_FAILURES);
    }

    /**
     * Requirement: 105 65-0582/00258 Test Case: 4.6.22 Description: To verify that Terminal Ranking dropped calls summary can support to view top 50
     * Terminals who experienced the most call drop failures.
     */
    @Test
    public void TerminalRankingCallDrop_6_22() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL, RankingsTab.SubStartMenu.RANKING_TERMINAL_RAN,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_LTE, RankingsTab.SubStartMenu.RANKING_TERMINAL_CFA,
                RankingsTab.SubStartMenu.RANKING_TERMINAL_LTE_CALL_DROP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops by Terminal");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", terminalRankingCallDrop.getTableRowCount() <= 50);

        final List<String> headersToUnTickIfPresent = new ArrayList<String>(Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC,
                GuiStringConstants.TERMINAL_MAKE));

        terminalRankingCallDrop.uncheckOptionalHeaderCheckBoxes(headersToUnTickIfPresent);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.FAILURES);
        terminalRankingCallDrop.openTableHeaderMenu(0);
        terminalRankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(terminalRankingCallDrop, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_DROPS, terminalRankingCallDrop);
        Thread.sleep(10000);
        validateDataOfFailuresRankingForAllTimeRanges(terminalRankingCallDrop, "TERMINAL RANKING", GuiStringConstants.CALL_DROPS);
    }

    /**
     * Requirement: 105 65-0582/00260 Test Case: 4.6.25 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * setup failures.
     */
    @Test
    public void eCellRankingCallSetupFailure_6_25() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(RankingsTab.SubStartMenu.EVENT_RANKING_ECELL,
                RankingsTab.SubStartMenu.RANKINGS_ECELL_RAN, RankingsTab.SubStartMenu.RANKINGS_ECELL_LTE,
                RankingsTab.SubStartMenu.RANKINGS_ECELL_CFA, RankingsTab.SubStartMenu.RANKINGS_ECELL_LTE_CALL_SETUP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure by Access Area");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eCellRankingCallSetupFailure.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.RAN_VENDOR);
        listOfColumns.add(GuiStringConstants.CONTROLLER);
        listOfColumns.add(GuiStringConstants.ACCESS_AREA);
        listOfColumns.add(GuiStringConstants.FAILURES);
        eCellRankingCallSetupFailure.openTableHeaderMenu(0);
        eCellRankingCallSetupFailure.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(eCellRankingCallSetupFailure, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_SETUP_FAILURES, eCellRankingCallSetupFailure);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(eCellRankingCallSetupFailure, "ECELL RANKING", GuiStringConstants.CALL_SETUP_FAILURES);
    }

    /**
     * Requirement: 105 65-0582/00261 Test Case: 4.6.26 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * drops.
     */
    @Test
    public void eCellRankingCallDrop_6_26() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(RankingsTab.SubStartMenu.EVENT_RANKING_ECELL,
                RankingsTab.SubStartMenu.RANKINGS_ECELL_RAN, RankingsTab.SubStartMenu.RANKINGS_ECELL_LTE,
                RankingsTab.SubStartMenu.RANKINGS_ECELL_CFA, RankingsTab.SubStartMenu.RANKINGS_ECELL_LTE_CALL_DROP));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops by Access Area");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eCellRankingCallDrop.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.RAN_VENDOR);
        listOfColumns.add(GuiStringConstants.CONTROLLER);
        listOfColumns.add(GuiStringConstants.ACCESS_AREA);
        listOfColumns.add(GuiStringConstants.FAILURES);
        eCellRankingCallDrop.openTableHeaderMenu(0);
        eCellRankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(eCellRankingCallDrop, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_DROPS, eCellRankingCallDrop);
        Thread.sleep(5000);

        validateDataOfFailuresRankingForAllTimeRanges(eCellRankingCallDrop, "ECELL RANKING", GuiStringConstants.CALL_DROPS);
    }

    /**
     * Requirement: 105 65-0582/00254 Test Case: 4.6.29 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * setup failures.
     */
    @Test
    public void eNodeBRankingCallSetupFailures_6_29() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_ENODEB, RankingsTab.SubStartMenu.RANKINGS_ENODEB_RAN,
                RankingsTab.SubStartMenu.RANKINGS_ENODEB_LTE, RankingsTab.SubStartMenu.RANKINGS_ENODEB_CFA,
                RankingsTab.SubStartMenu.RANKINGS_ENODEB_LTE_CALL_SETUP_FAILURE));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Setup Failure by Controller");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eNodeBrankingCallSetupFailure.getTableRowCount() <= 50);

        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.RAN_VENDOR);
        listOfColumns.add(GuiStringConstants.CONTROLLER);
        listOfColumns.add(GuiStringConstants.FAILURES);
        eNodeBrankingCallSetupFailure.openTableHeaderMenu(0);
        eNodeBrankingCallSetupFailure.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(eNodeBrankingCallSetupFailure, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_SETUP_FAILURES, eNodeBrankingCallSetupFailure);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(eNodeBrankingCallSetupFailure, "CONTROLLER RANKING", GuiStringConstants.CALL_SETUP_FAILURES);
    }

    /**
     * Requirement: 105 65-0582/00255 Test Case: 4.6.30 Description: It shall be possible to view the top 50 eNodeBs which are experiencing most call
     * drops.
     */
    @Test
    public void eNodeBRankingCallDrop_6_30() throws Exception {

        List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                RankingsTab.SubStartMenu.EVENT_RANKING_ENODEB, RankingsTab.SubStartMenu.RANKINGS_ENODEB_RAN,
                RankingsTab.SubStartMenu.RANKINGS_ENODEB_LTE, RankingsTab.SubStartMenu.RANKINGS_ENODEB_CFA,
                RankingsTab.SubStartMenu.RANKINGS_ENODEB_LTE_CALL_DROP));

        workspacerc.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
        workspacerc.selectDimension(SeleniumConstants.RADIO_NETWORK_4G);
        Thread.sleep(5000);
        workspacerc.selectWindowType("4G Ranking", "Call Drops by Controller");
        Thread.sleep(5000);
        workspacerc.clickLaunchButton();
        Thread.sleep(5000);

        waitForPageLoadingToComplete();

        assertTrue("The number of events is greater than 50", eNodeBrankingCallDrop.getTableRowCount() <= 50);
        List<String> listOfColumns = new ArrayList<String>();
        listOfColumns.add(GuiStringConstants.RANK);
        listOfColumns.add(GuiStringConstants.RAN_VENDOR);
        listOfColumns.add(GuiStringConstants.CONTROLLER);
        listOfColumns.add(GuiStringConstants.FAILURES);
        eNodeBrankingCallDrop.openTableHeaderMenu(0);
        eNodeBrankingCallDrop.checkInOptionalHeaderCheckBoxes(listOfColumns);
        checkCallFailuresWindowAscAndDescButton(eNodeBrankingCallDrop, listOfColumns);
        checkWindowUpdatedForTimeRanges(GuiStringConstants.CALL_DROPS, eNodeBrankingCallDrop);
        Thread.sleep(10000);

        validateDataOfFailuresRankingForAllTimeRanges(eNodeBrankingCallDrop, "CONTROLLER RANKING", GuiStringConstants.CALL_DROPS);
    }

    private void drillDownIMSIOnEventAnalysisWindow(final CommonWindow window) throws NoDataException, PopUpException {

        window.clickTableCell(0, GuiStringConstants.IMSI);

        waitForPageLoadingToComplete();

        window.closeWindow();

        subscriberCallFailureAnalysis.clickTableCell(0, GuiStringConstants.FAILURE_CATEGORY);

        waitForPageLoadingToComplete();

        subscriberCallFailureAnalysis.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();

    }

    private void drillDownIMSIOnEventAnalysisWindowForRecurrence(final String columnHeader, final CommonWindow window) throws NoDataException,
            PopUpException {

        window.sortTable(SortType.DESCENDING, columnHeader);

        window.clickTableCell(0, GuiStringConstants.IMSI);

        waitForPageLoadingToComplete();

        window.closeWindow();

        subscriberCallFailureAnalysis.clickTableCell(0, GuiStringConstants.FAILURE_CATEGORY);

        waitForPageLoadingToComplete();

        subscriberCallFailureAnalysis.clickTableCell(0, GuiStringConstants.FAILURES);
        waitForPageLoadingToComplete();

    }

    /**
     * Check Ascending and Descending buttons on event analysis window.
     * @param window
     *the object of CommonWindow
     * @throws NoDataException
     * @throws PopUpException
     */
    private void checkCallFailuresWindowAscAndDescButton(final CommonWindow window, List<String> listOfColumns) throws NoDataException,
            PopUpException {

        for (String column : listOfColumns) {
            window.sortTable(SortType.ASCENDING, column);
            window.sortTable(SortType.DESCENDING, column);
        }
    }

    /**
     * Check all time ranges, the time ranges is read from properties file
     * @param name
     *of the window
     * @param object
     *of CommonWindow
     */
    private void checkWindowUpdatedForTimeRanges(final String windowName, final CommonWindow commonWindow) throws NoDataException, PopUpException {

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            commonWindow.setTimeRange(time);
            waitForPageLoadingToComplete();
            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
        }
    }

    private void validateDataOfFailuresRankingForAllTimeRanges(final CommonWindow commonWindow, String rankingType, String failureType)
            throws NoDataException, PopUpException, ParseException, IOException {
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_LTE);
        TimeRange[] timeRanges;

        // Validate data for all time ranges
        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {

            if (time == TimeRange.FIVE_MINUTES) {
                continue;
            }

            //Validate EVENT_TIME
            objDataIntegrityValidationUtil.delayAndSetTimeRange(time);

            Date startDate = objDataIntegrityValidationUtil.gStartDate;
            String startDateTimeCandidate = objDataIntegrityValidationUtil.gStartDateTimeCandidate;
            Date endDate = objDataIntegrityValidationUtil.gEndDate;
            String endDateTimeCandidate = objDataIntegrityValidationUtil.gEndDateTimeCandidate;

            commonWindow.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                    TimeCandidates.valueOf(endDateTimeCandidate));

        }
    }

}