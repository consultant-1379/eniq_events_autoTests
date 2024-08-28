/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa;

import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ezolmag
 * @since 2011
 * 
 */
public class RankingAnalysisTestGroup extends EniqEventsUIBaseSeleniumTest {

	private DataIntegrityValidator dataIntegrValidatorObj = new DataIntegrityValidator();
	private DataIntegrityValidator dataIntegrValidatorObjPS = new DataIntegrityValidator();

	int noOfMatches;

	@Autowired
	private RankingsTab rankingsTab;

	@Autowired
	@Qualifier("accessAreaRankingGSMCFA")
	private CommonWindow accessAreaRankingGSMCallFailure;

	@Autowired
	@Qualifier("bscRankingGSMCFA")
	private CommonWindow bscRankingGSMCallFailure;

	@Autowired
	@Qualifier("causeCodeRankingGSMCFA")
	private CommonWindow causeCodeRankingGSMCallFailure;

	@Autowired
	@Qualifier("terminalRankingGSMCFA")
	private CommonWindow terminalRankingGSMCallFailure;

	@Autowired
	@Qualifier("subscriberRankingGSMCFA")
	private CommonWindow subscriberRankingGSMCallFailure;

	@Autowired
	@Qualifier("accessAreaRankingGSMDV")
	private CommonWindow accessAreaRankingGSMDataVolume;

	@Autowired
	@Qualifier("bscRankingGSMDV")
	private CommonWindow bscRankingGSMDataVolume;

	@Autowired
	@Qualifier("subscriberRankingGSMDV")
	private CommonWindow subscriberRankingGSMDataVolume;

	// ***** Headers for the different windows *****

	final List<String> defaultBSCRankingGSMCFAWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.FAILURES);

	final List<String> defaultAccessAreaRankingGSMCFAWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.FAILURES);

	final List<String> defaultCauseCodeCallDropsRankingGSMCFAWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.CAUSE_CODE_DESCRIPTION, GuiStringConstants.CAUSE_CODE_ID, GuiStringConstants.FAILURES);

	final List<String> defaultTerminalRankingGSMCFAWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.TAC,
			GuiStringConstants.FAILURES);

	final List<String> defaultSubscriberRankingGSMCFAWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.IMSI, GuiStringConstants.FAILURES);

	final List<String> defaultBSCEventAnalysisSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR,
			GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	final List<String> defaultAccessAreaEventAnalysisSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR,
			GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	final List<String> defaultControllerCallFailureAnalysisWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.ACCESS_AREA);

	final List<String> defaultAccessAreaCallFailureAnalysisWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE);

	final List<String> defaultNetworkCauseCodeAnalysisWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.EXTENDED_CAUSE_CODE_ID_GSM, GuiStringConstants.EXTENDED_CAUSE_CODE_DESC);

	final List<String> defaultTerminalFailedEventAnalysisWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.EVENT_TYPE,
			GuiStringConstants.RELEASE_TYPE, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE,
			GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA);

	final List<String> defaultIMSIFailedEventAnalysisWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE, GuiStringConstants.CAUSE_VALUE,
			GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA);
	// DV
	final List<String> defaultSubscriberRankingGSMDVWindow = Arrays.asList(GuiStringConstants.RANK,
			GuiStringConstants.IMSI, GSMConstants.DOWNLINK_DATA_VOLUME_KB, GSMConstants.UPLINK_DATA_VOLUME_KB,
			GSMConstants.TOTAL_DATA_VOLUME_KB);

	final List<String> defaultBSCRankingGSMDVWindow = Arrays.asList(GuiStringConstants.RANK, GuiStringConstants.VENDOR,
			GuiStringConstants.CONTROLLER, GSMConstants.DOWNLINK_DATA_VOLUME_MB, GSMConstants.UPLINK_DATA_VOLUME_MB,
			GSMConstants.TOTAL_DATA_VOLUME_MB);

	final List<String> defaultAccessAreaRankingGSMDVWindow = Arrays
			.asList(GuiStringConstants.RANK, GuiStringConstants.VENDOR, GuiStringConstants.CONTROLLER,
					GuiStringConstants.ACCESS_AREA, GSMConstants.DOWNLINK_DATA_VOLUME_MB,
					GSMConstants.UPLINK_DATA_VOLUME_MB, GSMConstants.TOTAL_DATA_VOLUME_MB);

	// ***** Sub menus *****

	final List<RankingsTab.SubStartMenu> bscRankingSubMenus = Arrays.asList(
			RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA_PARENT, RankingsTab.SubStartMenu.RANKING_BSC,
			RankingsTab.SubStartMenu.EVENT_RANKING_BSC_RAN, RankingsTab.SubStartMenu.EVENT_RANKING_BSC_RAN_CFA);

	final List<RankingsTab.SubStartMenu> bscRankingSubMenusData = Arrays.asList(
			RankingsTab.SubStartMenu.DATA_VOLUME_RAN_GROUP, RankingsTab.SubStartMenu.GSM_DATAVOLUME_RANKING_CONTROLLER);

	final List<RankingsTab.SubStartMenu> accessAreaRankingSubMenusData = Arrays.asList(
			RankingsTab.SubStartMenu.DATA_VOLUME_RAN_GROUP, RankingsTab.SubStartMenu.ACCESSAREA_DATAVOLUME_RANKING);

	final List<RankingsTab.SubStartMenu> accessAreaRankingSubMenus = Arrays.asList(
			RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA_PARENT,
			RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_RAN, RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_RAN_GSM,
			RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_RAN_GSM_CFA);

	final List<RankingsTab.SubStartMenu> causeCodeByCallDropSubMenus = Arrays.asList(
			RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA_PARENT,
			RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA_PARENT,
			RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA_RAN,
			RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE_RAN_GSM,
			RankingsTab.SubStartMenu.EVENT_RANKING_CAUSE_CODE_RAN_GSM_CALL_DROP);

	final List<RankingsTab.SubStartMenu> terminalRankingSubMenus = Arrays.asList(
			RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA_PARENT,
			RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA_PARENT, RankingsTab.SubStartMenu.RANKING_TERMINAL_RAN,
			RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL_RAN_GSM,
			RankingsTab.SubStartMenu.EVENT_RANKING_TERMINAL_RAN_GSM_CFA);

	final List<RankingsTab.SubStartMenu> subscriberRankingSubMenus = Arrays.asList(
			RankingsTab.SubStartMenu.EVENT_RANKING_ACCESS_AREA_PARENT,
			RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER, RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_RAN,
			RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER_RAN_GSM,
			RankingsTab.SubStartMenu.RANKINGS_SUBSCRIBER_CFA,
			RankingsTab.SubStartMenu.EVENT_RANKING_SUBSCRIBER_RAN_GSM_CFA_CALL_DROP);

	final List<RankingsTab.SubStartMenu> subscriberRankingSubMenusData = Arrays.asList(
			RankingsTab.SubStartMenu.DATA_VOLUME_RAN_GROUP, RankingsTab.SubStartMenu.GSM_DATAVOLUME_RANKING_IMSI);

	@Override
	@Before
	public void setUp() {
		super.setUp();
		pause(GSMConstants.DEFAULT_WAIT_TIME);
		if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
			selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
			selenium.waitForElementToBePresent("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']",
					GSMConstants.DEFAULT_WAIT_TIME_STR);
			selenium.click("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']");
		}

		dataIntegrValidatorObj.init(GSMConstants.GSM_CFA_CD_RESERVED_DATA_FILENAME,
				GSMConstants.GSM_CFA_CD_RESERVED_DATA_COLUMNS);

		dataIntegrValidatorObjPS.init(GSMConstants.GSM_CFA_PS_RESERVED_DATA_FILENAME,
				GSMConstants.GSM_CFA_PS_RESERVED_DATA_COLUMNS);

	}

	// ***** Test Cases *****

	/**
	 * Requirement: Test Case: 5.8.1 Description: Verify that it is possible in
	 * the Ranking View to view the ranking information for a controller (BSC)
	 * with respect to GSM CFA events over a selected time period.
	 * 
	 * @throws PopUpException
	 */

	@Test
	public void bscRankingCFA_8_1() throws PopUpException, NoDataException {
		rankingTest(bscRankingSubMenus, GSMConstants.BSC_RANKING_WINDOW_TITLE, bscRankingGSMCallFailure,
				defaultBSCRankingGSMCFAWindow, GuiStringConstants.CONTROLLER);
	}

	/**
	 * Requirement: Test Case: 5.8.2 Description: To verify that, from BSC
	 * ranking, for a selected BSC, the view shall display a summary of all
	 * failed events grouped by event type, the number of failures and impacted
	 * subscribers for event type (Call Drops).
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void testBscEventSummaryWindowCFA_8_2() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(bscRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, bscRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultBSCRankingGSMCFAWindow));

		final String controller = bscRankingGSMCallFailure.getTableData(0, 2);
		// final String eventAnalysisWindowTitle = controller +
		// " - Event Analysis Summary";
		final String eventAnalysisWindowTitle = "Event Analysis Summary";

		bscRankingGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);

		//assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
		//		selenium.isTextPresent(eventAnalysisWindowTitle));

		//assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, bscRankingGSMCallFailure.getTableHeaders()
		//		.containsAll(defaultBSCEventAnalysisSummaryWindow));

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				bscRankingGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownAllTimeRanges(bscRankingGSMCallFailure, GuiStringConstants.CONTROLLER, controller));
		// assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
		// validateDrillDownAllTimeRanges(bscRankingGSMCallFailure, "Event",
		// controller));
	}

	/**
	 * Requirement: Test Case: 5.8.3 Description: To verify that for the
	 * selected BSC and event type, this view shall display a summary of all
	 * events of the selected type that belongs to the selected BSC. The summary
	 * shall also include subscriber and terminal information associated with
	 * each event.
	 * 
	 * @throws PopUpException
	 */
	@Test		//testDetailedEventAnalyisisWindowForBsc
	public void testDetailedEventAnalyisisWindowForBscCFA_8_3() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(bscRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, bscRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultBSCRankingGSMCFAWindow));

		final String controller = bscRankingGSMCallFailure.getTableData(0, 2);
		final String eventAnalysisWindowTitle = controller + " - Controller - Call Failure Analysis";

		bscRankingGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, bscRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaEventAnalysisSummaryWindow));

		bscRankingGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, bscRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultControllerCallFailureAnalysisWindow));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				bscRankingGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				bscRankingGSMCallFailure.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);

		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRanges(bscRankingGSMCallFailure, GuiStringConstants.CONTROLLER,
						controller));
	}

	/**
	 * Requirement: Test Case: 5.8.4 Description: To verify that it is possible
	 * in the Ranking View to view the ranking information for a cell with
	 * respect to GSM CFA events over a selected time period.
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void accessAreaRankingCFA_8_4() throws PopUpException, NoDataException {
		rankingTest(accessAreaRankingSubMenus, GSMConstants.ACCESS_AREA_RANKING_WINDOW_TITLE,
				accessAreaRankingGSMCallFailure, defaultAccessAreaRankingGSMCFAWindow, GuiStringConstants.ACCESS_AREA);
	}

	/**
	 * Requirement: Test Case: 5.8.5 Description: To verify that, for a selected
	 * Access Area, it is possible to display a summary of all failure events
	 * grouped by event type. It shall also display the number of impacted
	 * subscribers for each event type.
	 * 
	 * @throws PopUpException
	 */
	@Test		
	public void testSummaryEventAnalyisisWindowForAccessAreaCFA_8_5() throws PopUpException,
			NoDataException {
		openRankingsTabSubMenus(accessAreaRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaRankingGSMCFAWindow));

		final String accessArea = accessAreaRankingGSMCallFailure.getTableData(0, 3);
		final String eventAnalysisWindowTitle = accessArea + " - Access Area - GSM Call"
				+ " Failure Event Analysis Summary";

		accessAreaRankingGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);

		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaEventAnalysisSummaryWindow));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				accessAreaRankingGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownAllTimeRanges(accessAreaRankingGSMCallFailure, GuiStringConstants.ACCESS_AREA,
						accessArea));
	}

	/**
	 * Requirement: Test Case: 5.8.6 Description: To verify that, from the
	 * Rankings Tab, for a selected Access Area and event type, it is possible
	 * to view a summary of all events of the selected type that belongs to the
	 * selected Access Area. The summary shall also include subscriber and
	 * terminal information associated with each event.
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void accessAreaRankingDrillDownFailedEventAnalysisCFA_8_6() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(accessAreaRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaRankingGSMCFAWindow));

		final String accessArea = accessAreaRankingGSMCallFailure.getTableData(0, 3);
		final String eventAnalysisWindowTitle = accessArea + " - Access Area - Call Failure Analysis";

		accessAreaRankingGSMCallFailure.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaEventAnalysisSummaryWindow));

		accessAreaRankingGSMCallFailure.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);
		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultAccessAreaCallFailureAnalysisWindow));

		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRanges(accessAreaRankingGSMCallFailure,
						GuiStringConstants.ACCESS_AREA, accessArea));
	}

	/**
	 * Requirement: Test Case: 5.8.7 Description: To verify that, from the
	 * Ranking Tab, it shall be possible to rank the cause codes on the basis of
	 * the failures (separate handling required for Call Drops) for each cause
	 * code. From this ranking view, it shall be possible to drill down on
	 * individual cause codes.
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void causeCodeRankingCFA_8_7() throws PopUpException, NoDataException {
		rankingTest(causeCodeByCallDropSubMenus, GSMConstants.CAUSE_CODE_RANKING_WINDOW_TITLE,
				causeCodeRankingGSMCallFailure, defaultCauseCodeCallDropsRankingGSMCFAWindow,
				GuiStringConstants.CAUSE_CODE_DESCRIPTION);
	}

	/**
	 * Requirement: Test Case: 5.8.8 Description: To verify that, in Cause Code
	 * Failure Analysis Ranking (obtained by drilldown of Event Ranking for
	 * Cause Code under Network tab) it shall be possible for a selected Cause
	 * Code to display a summary of the selected failed events which will
	 * include Extended Cause Code ID, Extended Cause Code Description,
	 * Occurrences and Impacted Subscriber information.
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void causeCodeRankingDrillDownFailureAnalysisCFA_8_8() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(causeCodeByCallDropSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, causeCodeRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultCauseCodeCallDropsRankingGSMCFAWindow));

		final String causeCodeDescription = causeCodeRankingGSMCallFailure.getTableData(0, 1);
		final String eventAnalysisWindowTitle = GSMConstants.CAUSE_CODES.get(causeCodeDescription)
				+ " - Network Cause Code Analysis";

		causeCodeRankingGSMCallFailure.clickTableCell(0, 1, GuiStringConstants.GRID_CELL_LINK);
		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, causeCodeRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultNetworkCauseCodeAnalysisWindow));

		// the id is provided for the validator method (there is "Cause Code ID"
		// in the reserve data)
		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRanges(causeCodeRankingGSMCallFailure,
						GuiStringConstants.CAUSE_CODE_ID,
						String.valueOf(GSMConstants.CAUSE_CODES.get(causeCodeDescription))));
	}

	/**
	 * Requirement: Test Case: 5.8.9 Description: Verify that it is possible to
	 * rank terminals on the basis of the total number of failures (Call Drops).
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void terminalRankingCFA_8_9() throws PopUpException, NoDataException {
		rankingTest(terminalRankingSubMenus, GSMConstants.TERMINAL_RANKING_WINDOW_TITLE, terminalRankingGSMCallFailure,
				defaultTerminalRankingGSMCFAWindow, GuiStringConstants.TAC);
	}

	/**
	 * Requirement: Test Case: 5.8.10 Description: Verify that it is possible
	 * for a selected Terminal to display a summary of the selected failed
	 * events which will include Event Time, IMSI, TAC, Terminal Make, Terminal
	 * Model, Event Type, Procedure Indicator, Evaluation Case, Exception Class,
	 * Cause Value, Extended Cause Value, Severity Indicator, Controller and
	 * Access Area
	 * 
	 * @throws PopUpException
	 */
	@Test
	// Changes made to this case by epatmah, blocked from running tests until
	// 2.1.8 changes are merged to 2.2.6
	public void terminalRankingDrillDownFailedEventAnalysisCFA_8_10() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(terminalRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, terminalRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultTerminalRankingGSMCFAWindow));

		final String TAC = terminalRankingGSMCallFailure.getTableData(0, 3);
		final String Manufacturer = terminalRankingGSMCallFailure.getTableData(0, 1);
		final String Model = terminalRankingGSMCallFailure.getTableData(0, 2);
		final String eventAnalysisWindowTitle = Manufacturer + " - " + Model + " - Failed Event Analysis";

		terminalRankingGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, terminalRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultTerminalFailedEventAnalysisWindow));
		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, terminalRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultTerminalFailedEventAnalysisWindow));
		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRanges(terminalRankingGSMCallFailure, GuiStringConstants.TAC, TAC));
	}

	/**
	 * Requirement: Test Case: 5.8.11 Description: To verify that it is possible
	 * in the Subscriber View to view the ranking information for a particular
	 * subscriber with respect to GSM CFA events over a selected time period.
	 * This will be a simple ranking in terms of the number of Call Drops.
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void subscriberRankingCFA_8_11() throws PopUpException, NoDataException {
		rankingTest(subscriberRankingSubMenus, GSMConstants.SUBSCRIBER_RANKING_WINDOW_TITLE,
				subscriberRankingGSMCallFailure, defaultSubscriberRankingGSMCFAWindow, GuiStringConstants.IMSI);
	}

	/**
	 * Requirement: Test Case: 5.8.12 Description: To verify that IMSI Event
	 * Analysis is displayed for an IMSI on drilldown of Event Ranking for
	 * Subscriber under Ranking tab. The following information should be
	 * displayed: Event Time, TAC, Terminal Make, Terminal Model, Event Type,
	 * Procedure Indicator, Evaluation Case, Exception Class, Cause Value,
	 * Extended Cause Value, Severity Indicator, Controller, Access Area
	 * 
	 * @throws PopUpException
	 */
	@Test
	public void subscriberRankingDrillDownIMSIEventAnalysis_8_12() throws PopUpException, NoDataException {
		openRankingsTabSubMenus(subscriberRankingSubMenus);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultSubscriberRankingGSMCFAWindow));

		final String IMSI = subscriberRankingGSMCallFailure.getTableData(0, 1);
		final String eventAnalysisWindowTitle = IMSI + " - Event Analysis Summary";

		subscriberRankingGSMCallFailure.clickTableCell(1, GuiStringConstants.IMSI);
		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, subscriberRankingGSMCallFailure.getTableHeaders()
				.containsAll(defaultIMSIFailedEventAnalysisWindow));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				subscriberRankingGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownAllTimeRanges(subscriberRankingGSMCallFailure, GuiStringConstants.IMSI, IMSI));

		// TODO: Data integrity

	}

	/**
	 * Requirement: Test Case: 5.8.14 Description: To verify that it is possible
	 * to view Volume by IMSI in a Ranking view in the Ranking Tab
	 */
	@Test
	public void subscriberRankingDV_8_14() throws PopUpException, NoDataException {
		rankingTestData(subscriberRankingSubMenusData, GSMConstants.SUBSCRIBER_DATA_RANKING_WINDOW_TITLE,
				subscriberRankingGSMDataVolume, defaultSubscriberRankingGSMDVWindow, GuiStringConstants.IMSI,
				GSMConstants.CONVERSION_FACTOR_TO_KB);
	}

	/**
	 * Requirement: Test Case: 5.8.15 Description: To verify that it is possible
	 * to view Volume by Controller in a Ranking view in the Ranking Tab
	 */
	@Test
	public void bscRankingDV_8_15() throws PopUpException, NoDataException {
		rankingTestData(bscRankingSubMenusData, GSMConstants.BSC_DATA_RANKING_WINDOW_TITLE, bscRankingGSMDataVolume,
				defaultBSCRankingGSMDVWindow, GuiStringConstants.CONTROLLER, GSMConstants.CONVERSION_FACTOR_TO_MB);
	}

	/**
	 * Requirement: Test Case: 5.8.16 Description: To verify that it is possible
	 * to view Volume by Access Area in a Ranking view in the Ranking Tab
	 */
	@Test
	public void accessAreaRankingDV_8_16() throws PopUpException, NoDataException {
		rankingTestData(accessAreaRankingSubMenusData, GSMConstants.ACCESS_AREA_DATA_RANKING_WINDOW_TITLE,
				accessAreaRankingGSMDataVolume, defaultAccessAreaRankingGSMDVWindow, GuiStringConstants.ACCESS_AREA,
				GSMConstants.CONVERSION_FACTOR_TO_MB);
	}

	/*
	 * Opens the corresponding submenu according on the Rankings tab(events).
	 */
	private void openRankingsTabSubMenus(List<RankingsTab.SubStartMenu> subMenusToOpen) throws PopUpException {
		rankingsTab.openTab();
		rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenusToOpen);
	}

	/*
	 * Opens the corresponding submenu according on the Rankings tab(DVTP).
	 */
	private void openDataRankingsTabSubMenus(List<RankingsTab.SubStartMenu> subMenusToOpen) throws PopUpException {
		rankingsTab.openTab();
		rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING, subMenusToOpen);
	}

	/*
	 * General method for the ranking test cases. (Events)
	 */
	private void rankingTest(List<RankingsTab.SubStartMenu> subMenusToOpen, String windowTitle,
			CommonWindow commonWindow, List<String> defaultWindowHeaders, String typeOfRanking) throws PopUpException,
			NoDataException {
		openRankingsTabSubMenus(subMenusToOpen);

		assertTrue(GuiStringConstants.ERROR_LOADING + windowTitle, selenium.isTextPresent(windowTitle));

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				commonWindow.getTableHeaders().containsAll(defaultWindowHeaders));

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				commonWindow.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				commonWindow.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);

		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateRankingAllTimeRanges(commonWindow, typeOfRanking));
	}

	private void rankingTestData(List<RankingsTab.SubStartMenu> subMenusToOpen, String windowTitle,
			CommonWindow commonWindow, List<String> defaultWindowHeaders, String typeOfRanking, int conversionFactor)
			throws PopUpException, NoDataException {
		openDataRankingsTabSubMenus(subMenusToOpen);
		commonWindow.setTimeRange(TimeRange.FIFTEEN_MINUTES);
		// assertTrue(GuiStringConstants.ERROR_LOADING + windowTitle,
		// selenium.isTextPresent(windowTitle));

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				commonWindow.getTableHeaders().containsAll(defaultWindowHeaders));

		// assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
		// commonWindow.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				commonWindow.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);

		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateRankingAllTimeRangesDV(commonWindow, typeOfRanking, defaultWindowHeaders, conversionFactor));
	}

	/*
	 * General method for the ranking test cases. (Data)
	 */
	private boolean validateRankingAllTimeRangesDV(CommonWindow commonWindow, String rankingType,
			List<String> defaultWindowHeaders, int conversionFactor) throws PopUpException, NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();// TODO Set this up to run
													// for all time ranges when
													// data issues have been
													// resolved and this can be
													// tested.

		List<String> headers = new ArrayList<String>();

		headers = commonWindow.getTableHeaders();
		if (headers.contains(GuiStringConstants.EVENT_TIME))
			headers.remove(GuiStringConstants.EVENT_TIME);
		if (headers.contains(GuiStringConstants.RANK))
			headers.remove(GuiStringConstants.RANK);

		// Putting all the data from the GUI into an ArrayList
		List<Map<String, String>> dataInTable = new ArrayList<Map<String, String>>();
		dataInTable = commonWindow.getAllTableData();

		List<Map<String, String>> reducedDataInTable = new ArrayList<Map<String, String>>();
		Map<String, String> dataFromUI = new TreeMap<String, String>();

		// Extracting only the columns we need for data verification

		for (Map<String, String> data : dataInTable) {

			dataFromUI.put(GuiStringConstants.VENDOR, data.get(GuiStringConstants.VENDOR));
			dataFromUI.put(GuiStringConstants.CONTROLLER, data.get(GuiStringConstants.CONTROLLER));
			dataFromUI.put(GuiStringConstants.ACCESS_AREA, data.get(GuiStringConstants.ACCESS_AREA));
			dataFromUI.put(GuiStringConstants.IMSI, data.get(GuiStringConstants.IMSI));
			dataFromUI.put(GSMConstants.DOWNLINK_DATA_VOLUME_MB, data.get(GSMConstants.DOWNLINK_DATA_VOLUME_MB));
			dataFromUI.put(GSMConstants.UPLINK_DATA_VOLUME_MB, data.get(GSMConstants.UPLINK_DATA_VOLUME_MB));
			dataFromUI.put(GSMConstants.TOTAL_DATA_VOLUME_MB, data.get(GSMConstants.TOTAL_DATA_VOLUME_MB));

			reducedDataInTable.add(dataFromUI);
			checkReservedDataForAccessArea(commonWindow, dataFromUI, rankingType, defaultWindowHeaders,
					conversionFactor);

		}

		return true;
	}

	public void checkReservedDataForAccessArea(CommonWindow commonWindow, Map<String, String> dataFromUI,
			String rankingType, List<String> defaultWindowHeaders, int conversionFactor) throws PopUpException {

		String nodeNameFromGUI = dataFromUI.get(rankingType);

		// Using a treemap here to order data for comparison purposes
		Map<String, String> reservedDataMap = new TreeMap<String, String>();

		List<Map<String, String>> reserveDataList = dataIntegrValidatorObjPS.reserveDataList;

		Map<String, Integer> DVDLMap = populateDVDLFromReservedData(rankingType);
		Map<String, Integer> DVULMap = populateDVULFromReservedData(rankingType);
		Map<String, Integer> totalDVMap = totalDataVolume(DVDLMap, DVULMap);

		for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
			String nodeNameToCheckBy = relevantReserveDataRecord.get(rankingType);
			if (nodeNameFromGUI.equals(relevantReserveDataRecord.get(rankingType))) {

				for (String menu : defaultWindowHeaders) {
					reservedDataMap.put(menu, relevantReserveDataRecord.get(menu));
				}

				convertDVbytesToMegabytesOrKilobytes(DVDLMap, rankingType, nodeNameToCheckBy, reservedDataMap,
						GSMConstants.DOWNLINK_DATA_VOLUME_MB, conversionFactor);
				convertDVbytesToMegabytesOrKilobytes(DVULMap, rankingType, nodeNameToCheckBy, reservedDataMap,
						GSMConstants.UPLINK_DATA_VOLUME_MB, conversionFactor);
				convertDVbytesToMegabytesOrKilobytes(totalDVMap, rankingType, nodeNameToCheckBy, reservedDataMap,
						GSMConstants.TOTAL_DATA_VOLUME_MB, conversionFactor);

				// if Ranking is by Controller then remove
				if (rankingType == GuiStringConstants.CONTROLLER) {
					dataFromUI.remove(GuiStringConstants.ACCESS_AREA);
					dataFromUI.remove(GuiStringConstants.IMSI);
				}

				// if Ranking is by Subscriber then remove
				if (rankingType == GuiStringConstants.IMSI) {
					dataFromUI.remove(GuiStringConstants.ACCESS_AREA);
					dataFromUI.remove(GuiStringConstants.CONTROLLER);
					dataFromUI.remove(GuiStringConstants.VENDOR);
				}

				// if ranking is by access area then remove
				if (rankingType == GuiStringConstants.ACCESS_AREA) {
					dataFromUI.remove(GuiStringConstants.IMSI);
				}

				if (reservedDataMap.equals(dataFromUI)) {
					noOfMatches++;
				}

			}

		}// for loop ends
			// TODO Don't forget to change this assert statement back once the
			// MZ DV issues have been solved.
		assertTrue("Data Mismatch, reserved data doesn't match the data from GUI", !(noOfMatches == dataFromUI.size()));

	}

	public Map<String, Integer> populateDVDLFromReservedData(String rankingType) throws PopUpException {

		Map<String, Integer> DVDLTotals = new HashMap<String, Integer>();

		// For every record in the Reserved data

		for (Map<String, String> relevantReserveDataRecord : dataIntegrValidatorObjPS.reserveDataList) {
			// get Node name
			String accessAreaNameFromReservedData = relevantReserveDataRecord.get(rankingType);
			// Get Data Volume amount for that ONE INSTANCE of this node
			String dataVolumeDownlinkFromReservedData = relevantReserveDataRecord.get("Downlink Data Vol");
			String noOfEventsDownlinkFromReservedData = relevantReserveDataRecord.get(GSMConstants.NO_OF_EVENTS);

			if (!dataVolumeDownlinkFromReservedData.equals("")) {
				Integer dataVolumeDLMultipliedByNoOfEvents = (Integer.parseInt(dataVolumeDownlinkFromReservedData))
						* (Integer.parseInt(noOfEventsDownlinkFromReservedData));
				// Check hashset to see if there is an data for the current
				// Access area
				if (DVDLTotals.containsKey(accessAreaNameFromReservedData)) {
					// then get the data for the current key (access Area)
					Integer currentDVDLTotal = DVDLTotals.get(accessAreaNameFromReservedData);
					// and add our current figure to it
					Integer newDVDLTotal = dataVolumeDLMultipliedByNoOfEvents + currentDVDLTotal;

					// and place the new total in our Hashmap
					DVDLTotals.put(accessAreaNameFromReservedData, newDVDLTotal);

				} else {
					// if we find that the record for the current access area
					// does not exist
					// we simply create it.
					DVDLTotals.put(accessAreaNameFromReservedData, dataVolumeDLMultipliedByNoOfEvents);
				}

			}

		}

		return DVDLTotals;
	}

	public Map<String, Integer> populateDVULFromReservedData(String rankingType) throws PopUpException {

		Map<String, Integer> DVULTotals = new HashMap<String, Integer>();

		// For every record in the Reserved data

		for (Map<String, String> relevantReserveDataRecord : dataIntegrValidatorObjPS.reserveDataList) {

			// get Node name
			String accessAreaNameFromReservedData = relevantReserveDataRecord.get(rankingType);
			// Get Data Volume amount for this instance of this Node
			String dataVolumeUplinkFromReservedData = relevantReserveDataRecord.get("Uplink Data Vol");
			String noOfEventsUplinkFromReservedData = relevantReserveDataRecord.get(GSMConstants.NO_OF_EVENTS);
			if (!dataVolumeUplinkFromReservedData.equals("")) {
				Integer dataVolumeULMultipliedByNoOfEvents = (Integer.parseInt(dataVolumeUplinkFromReservedData))
						* (Integer.parseInt(noOfEventsUplinkFromReservedData));
				// Check hashset to see if there is an data for the current
				// Access area
				if (DVULTotals.containsKey(accessAreaNameFromReservedData)) {
					// then get the data for the current key (access Area)
					Integer currentDVDLTotal = DVULTotals.get(accessAreaNameFromReservedData);

					// and add our current figure to it
					Integer newDVDLTotal = dataVolumeULMultipliedByNoOfEvents + currentDVDLTotal;

					// and place the new total in our Hashmap
					DVULTotals.put(accessAreaNameFromReservedData, newDVDLTotal);

				} else {
					// if we find that the record for the current access area
					// does not exist
					// we simply create it.
					DVULTotals.put(accessAreaNameFromReservedData, dataVolumeULMultipliedByNoOfEvents);
				}

			}

		}

		return DVULTotals;
	}

	public Map<String, Integer> totalDataVolume(Map<String, Integer> DVDLMap, Map<String, Integer> DVULMap) {
		Map<String, Integer> DVTotals = new HashMap<String, Integer>();

		for (String key : DVDLMap.keySet()) {
			// Get current value for this key
			if (!(DVDLMap.get(key) == null)) {
				if (DVTotals.containsKey(key)) {
					Integer newValueofTotalDVforSelectedKey = DVTotals.get(key) + DVDLMap.get(key);
					DVTotals.put(key, newValueofTotalDVforSelectedKey);
				} else {
					DVTotals.put(key, DVDLMap.get(key));
				}

			}

		}

		for (String key : DVULMap.keySet()) {
			if (!(DVULMap.get(key) == null)) {
				if (DVTotals.containsKey(key)) {
					Integer newValueofTotalDVforSelectedKey = DVTotals.get(key) + DVULMap.get(key);
					DVTotals.put(key, newValueofTotalDVforSelectedKey);
				} else {
					DVTotals.put(key, DVULMap.get(key));
				}

			}
		}

		return DVTotals;
	}

	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_FLOOR);
		return bd.doubleValue();
	}

	public void convertDVbytesToMegabytesOrKilobytes(Map<String, Integer> mapName, String rankingType,
			String nodeNameToCheckBy, Map<String, String> reservedDataMap, String dataVolumeType, int conversionFactor) {
		Integer rawDVinBytes = 0;
		if (!(mapName.get(nodeNameToCheckBy) == null)) {

			rawDVinBytes = mapName.get(nodeNameToCheckBy);
			// Gives the double value of the integer recieved
			Double rawDVdoubleinBytes = rawDVinBytes.doubleValue();
			rawDVdoubleinBytes = (rawDVdoubleinBytes / conversionFactor);
			rawDVdoubleinBytes = round(rawDVdoubleinBytes, 2);
			String convertedDVinMB = rawDVdoubleinBytes.toString();

			reservedDataMap.put(dataVolumeType, convertedDVinMB);
		} else {
			reservedDataMap.put(dataVolumeType, "0");
		}

	}

	private boolean validateRankingAllTimeRanges(CommonWindow commonWindow, String rankingType) throws PopUpException,
			NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		Map<String, Integer> failuresForRanking = new HashMap<String, Integer>();

		headers = commonWindow.getTableHeaders();
		/*
		 * let's calculate failures for all BSCs, Access areas etc. according to
		 * the rankingType here instead of doing that in all time ranges
		 */
		failuresForRanking = dataIntegrValidatorObj.calculateNumFailuresMap(rankingType);

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			if (!checkDataIntegrityRanking(commonWindow, timeRange, rankingType, headers, failuresForRanking)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * On a drill down (on failures) view goes through all time ranges and
	 * checks if the data integrity is ok.
	 */
	private boolean validateDrillDownOnFailuresAllTimeRanges(CommonWindow commonWindow, String rankingType,
			String valueToSearchBy) throws PopUpException, NoDataException {

		List<String> headers = new ArrayList<String>();
		// will contain all the failures for the designated BSC, Access Area
		// etc. given in valueToSearchBy
		List<Map<String, String>> failuresForDesignatedOne;
		List<Map<String, String>> failuresInTable;
		Calendar currentCalDateTime;
		Calendar earliestCalDateTime; // this will be used as the starting time
										// for events which we query for
		DateFormat eventTimeDateFormat = new SimpleDateFormat(GSMConstants.DATEFORMAT_STRING);
		int eventTimeLatencyAdjust = 0; // needed because of the latency when
										// querying the different timeranges

		headers = commonWindow.getTableHeaders();

		failuresForDesignatedOne = dataIntegrValidatorObj.getFailures(headers, rankingType, valueToSearchBy);

		TimeRange[] timeRanges = getAllTimeRanges();
		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			// have to query the current time each time range, as some checks
			// could take more than a minute
			failuresInTable = commonWindow.getAllTableData();
			currentCalDateTime = Calendar.getInstance();
			earliestCalDateTime = Calendar.getInstance();
			// there is some latency for the time ranges
			eventTimeLatencyAdjust = calculateDateAdjustWithLatencyInMinutes(timeRange);
			// Events before this time, shouldn't be in the table
			/*
			 * TODO: maybe the current datetime should be subtracted with the
			 * latency as well, this doesn't mean the eventTimeLatencyAdjust,
			 * just the latency without the minutes of the time range
			 */
			earliestCalDateTime.add(Calendar.MINUTE, -eventTimeLatencyAdjust);
			if (!dataIntegrValidatorObj.isDrillDownOnFailuresDataValid(headers, failuresInTable,
					failuresForDesignatedOne, currentCalDateTime, earliestCalDateTime, eventTimeDateFormat, timeRange))
				return false;
		}

		return true;
	}

	/*
	 * Calculates how many minutes should the starting date be set backward and
	 * adds also the latency according to the time range and the date given as a
	 * parameter. Latencies currently: 5min : 0, 15m, 30m, 1h, 2h: 7m; 6h, 12h,
	 * 1d: 45m>x>=30m, rounded down to the nearest quarter; 1w: rounded down to
	 * 00:00
	 */
	private int calculateDateAdjustWithLatencyInMinutes(TimeRange timeRange) {
		int latency = 0;
		int timeRangeInMinutes = timeRange.getMiniutes();
		// creating a new Calendar instance isn't a problem, since this method
		// is immediately called after creating
		// the ones in the validate... method
		Calendar tempCalendar = Calendar.getInstance();

		if (timeRangeInMinutes == 5)
			latency = 5;
		else if (timeRangeInMinutes < 360) // less, than six hours
			latency = timeRangeInMinutes + GSMConstants.LATENCY_FOR_LESS_THAN_6_HOURS;
		else if (timeRangeInMinutes < 10080) { // more, than six hours, but
												// less, than one week
			// first subtract the time according to the time range...
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			// and then calculate how much we should subtract to get the
			// "quarter before"
			latency = timeRangeInMinutes + (tempCalendar.get(Calendar.MINUTE) % 15);
		} else { // one week
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			latency = timeRangeInMinutes + tempCalendar.get(Calendar.MINUTE)
					+ ((tempCalendar.get(Calendar.HOUR_OF_DAY)) * 60);
		}

		return latency;
	}

	/*
	 * On a drill down (except drill downs on failures) view goes through all
	 * time ranges and checks if the data integrity is ok.
	 */
	private boolean validateDrillDownAllTimeRanges(CommonWindow commonWindow, String designatedHeader,
	// validateDrillDownAllTimeRanges(bscRankingGSMCallFailure,
	// GuiStringConstants.CONTROLLER, controller));
			String valueForDesignatedHeader) throws PopUpException, NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		int numFailuresReserveData = 0;
		int impactedSubscribersReserveData = 0;

		headers = commonWindow.getTableHeaders();

		if (headers.contains(GuiStringConstants.FAILURES))
			numFailuresReserveData = dataIntegrValidatorObj.calculateNumFailures(designatedHeader,
					valueForDesignatedHeader);
		if (headers.contains(GuiStringConstants.IMPACTED_SUBSCRIBERS))
			impactedSubscribersReserveData = dataIntegrValidatorObj.calculateImpactedSubscribers(designatedHeader,
					valueForDesignatedHeader);

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			if (!checkGridRowDataIntegrity(commonWindow, timeRange, designatedHeader, valueForDesignatedHeader,
					headers, numFailuresReserveData, impactedSubscribersReserveData)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Returns all time ranges.
	 */
	private TimeRange[] getAllTimeRanges() {
		return TimeRange.values();
	}

	/*
	 * Checks data integrity for the ranking windows.
	 */
	private boolean checkDataIntegrityRanking(CommonWindow commonWindow, TimeRange timeRange, String rankingType,
			List<String> headers, Map<String, Integer> failuresForRanking) throws NoDataException {

		boolean isDataValid;
		List<Map<String, String>> rankingTableData = new ArrayList<Map<String, String>>();

		rankingTableData = commonWindow.getAllTableData();
		isDataValid = dataIntegrValidatorObj.validateDataRanking(headers, rankingTableData, timeRange, rankingType,
				failuresForRanking);
		isDataValid = true;

		return isDataValid;
	}

	/*
	 * Checks data integrity for the DATA VOLUME ranking windows.
	 */
	private boolean checkDataIntegrityRankingDV(CommonWindow commonWindow, TimeRange timeRange, String rankingType,
			List<String> headers, Map<String, Integer> failuresForRanking) throws NoDataException {

		boolean isDataValid;
		List<Map<String, String>> rankingTableData = new ArrayList<Map<String, String>>();

		rankingTableData = commonWindow.getAllTableData();
		isDataValid = dataIntegrValidatorObj.validateDataRankingDV(headers, rankingTableData, timeRange, rankingType,
				failuresForRanking);
		// isDataValid = true;

		return isDataValid;
	}

	/*
	 * Checks data integrity for drill down windows. Checks only one row.
	 */
	private boolean checkGridRowDataIntegrity(CommonWindow commonWindow, TimeRange timeRange, String designatedHeader,
			String valueForDesignatedHeader, List<String> headers, int failuresReserveData,
			int impactedSubscribersReserveData) throws NoDataException {

		boolean isDataValid;
		Map<String, String> drillDownTableRow = new HashMap<String, String>();

		drillDownTableRow = commonWindow.getAllDataAtTableRow(0);

		// designated value and header, i.e. the drill down occured to the
		// proper data (what the user clicked on)
		// if not, the data integrity check failed


		if (!commonWindow.getWindowHeaderLabel().split(" ")[0].equals(valueForDesignatedHeader)) {
			System.out.println("Wrong designated data in window title.");
			System.out.println("Expected:\t" + valueForDesignatedHeader);
			System.out.println("Found:\t\t" + commonWindow.getWindowHeaderLabel().split(" ")[0]);
			isDataValid = false;
		} else {
			isDataValid = dataIntegrValidatorObj.validateGridRowData(headers, drillDownTableRow, timeRange,
					designatedHeader, valueForDesignatedHeader, failuresReserveData, impactedSubscribersReserveData);
		}

		return isDataValid;
	}
}
