package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa;

import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author epatmah
 * @since 2012
 * 
 */
public class ekuabkuTerminalTabGSM extends EniqEventsUIBaseSeleniumTest {

	private DataIntegrityValidator dataIntegrValidatorObjCS = new DataIntegrityValidator();
	private DataIntegrityValidator dataIntegrValidatorObjPS = new DataIntegrityValidator();
	// ***** Sub menus *****

	@Autowired
	private SubscriberTab subscriberTab;

	@Autowired
	@Qualifier("imsiSubscriberGSMCFA")
	private CommonWindow imsiSubscriberCallFailure;

	@Autowired
	@Qualifier("imsiGroupSubscriberGSMCFA")
	private CommonWindow imsiGroupSubscriberCallFailure;

	@Autowired
	@Qualifier("imsiGroupSubscriberGSMConnectionFailure")
	private CommonWindow imsiGroupSubscriberGSMConnectionFailure;
	
	@Autowired
	@Qualifier("subscriberRankingGSMCallFailureAnalysisByCallDrops")
	private CommonWindow subscriberRankingGSMCallFailureAnalysisByCallDrops;
	
	@Autowired
	@Qualifier("imsiSubscriberSummaryGSMCFA")
	private CommonWindow imsiSubscriberGSMCallFailure;

	// ***** Headers for the different windows *****

	List<String> IMSIEventAnalysisDetailedGSMCFAPSWindow;
	
	public static final List<String> IMSIEventAnalysisDetailedGSMCFAWindow = Arrays.asList(
			GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.CONTROLLER,
			GuiStringConstants.ACCESS_AREA);

	// );

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
		subscriberTab.openTab();
		dataIntegrValidatorObjPS.init(GSMConstants.GSM_CFA_PS_RESERVED_DATA_FILENAME,
				GSMConstants.GSM_CFA_PS_RESERVED_DATA_COLUMNS);
		dataIntegrValidatorObjCS.init(GSMConstants.GSM_CFA_CD_RESERVED_DATA_FILENAME,
				GSMConstants.GSM_CFA_CD_RESERVED_DATA_COLUMNS);
	}

	// ***** Test Cases *****

	/**
	 * Requirement: Test Case: 5.7.1 Description: To verify that it is possible
	 * to view a detailed analysis of Call Drop failure events for a nominated
	 * IMSI Group
	 * 
	 * @throws PopUpException
	 */
	// TODO run this test, could not be run due to server issues so has NOT BEEN
	// FULLY TESTED. <a href="#" class=" x-menu-item x-component" id="
	@Test
	public void imsiGroupEventAnalysisRANGSMCFA_7_1() throws PopUpException, NoDataException, InterruptedException {
		subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
				GSMConstants.REGRESSION_IMSI_GROUP_NAME);
		//selenium.waitForPageLoadingToComplete();

		System.out.println(imsiGroupSubscriberCallFailure.getTableHeaders());
		System.out.println(GSMConstants.GSM_CFA_CD_RESERVED_DATA_COLUMNS);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiGroupSubscriberCallFailure.getTableHeaders()
				.containsAll(GSMConstants.GSM_CFA_CD_RESERVED_DATA_COLUMNS));

		final String eventAnalysisWindowTitle = GSMConstants.REGRESSION_IMSI_GROUP_NAME + " - IMSI Event Analysis";

		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));
		// for each of the imsi group members do to end of function
		// for (String imsiGroupMember : GSMConstants.IMSI_GROUP_MEMBER_NAMES)
		// {//new for loop
		// compare the returned TAC to The Tac in the GUI

		// GSMConstants.IMSI_GROUP_MEMBER_NAMES;
		String[] dataFromReservedData = dataIntegrValidatorObjCS.checkReservedDataForImsiGroup();
		TimeRange[] timeRanges = getAllTimeRanges();
		for (TimeRange timeRange : timeRanges) {
			String[] dataFromGUI = populateSet(timeRange, imsiSubscriberCallFailure, 1);
			for (String dataFromTheGUI : dataFromGUI) {
				for (String reservedData : dataFromReservedData) {
					assertTrue("Reserved data does not equal data from GUI",
							reservedData.equals(dataFromTheGUI));
				}
			}
		}

		// }//end of new for loop

	}// 7.1 function end

	// 7.2...

	/**
	 * Requirement: Test Case: 5.7.2 Description: To verify that it is possible
	 * to view a summary of Call Failure events for a nominated subscriber
	 * (IMSI).
	 * 
	 * @throws PopUpException
	 */

	@Test
	public void imsiEventAnalysisRANGSMCFA_7_2() throws PopUpException, NoDataException, InterruptedException {
		subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
		selenium.waitForPageLoadingToComplete();
		System.out.println(imsiSubscriberCallFailure.getTableHeaders());
		System.out.println(IMSIEventAnalysisDetailedGSMCFAWindow);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberCallFailure.getTableHeaders()
				.containsAll(IMSIEventAnalysisDetailedGSMCFAWindow));

		final String eventAnalysisWindowTitle = GSMConstants.IMSI_NUMBER + " - IMSI Event Analysis";

		assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
				selenium.isTextPresent(eventAnalysisWindowTitle));

		// compare the returned TAC to The Tac in the GUI
		String dataFromReservedData = dataIntegrValidatorObjCS.checkReservedDataForImsi();

		TimeRange[] timeRanges = getAllTimeRanges();

		for (TimeRange timeRange : timeRanges) {
			String[] dataFromGUI = populateSet(timeRange, imsiSubscriberCallFailure, 1);
			for (String dataFromTheGUI : dataFromGUI) {
				assertTrue("Only TAC " + dataFromReservedData + " should be coupled with the IMSI "
						+ GSMConstants.IMSI_NUMBER + ", TAC " + dataFromTheGUI + " should not",
						dataFromReservedData.equals(dataFromTheGUI));
			}
		}

	}
	
	@Test
	public void imsiEventRankingRANGSMCFAByCallDrops_7_3() throws PopUpException, NoDataException, InterruptedException, ParseException {
		//opening menus
		List<SubscriberTab.SubStartMenu> SubMenuList = new ArrayList<SubscriberTab.SubStartMenu>(); 

		SubMenuList.add(SubscriberTab.SubStartMenu.EVENT_GROUP);
		SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN);
		SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM);
		SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_CFA);
		SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM_CALL_DROPS);

		subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS , SubMenuList);
		//opening menus end
		//selenium.waitForPageLoadingToComplete();
		selenium.waitForPageLoadingToComplete();
		
		//Check headers (pre drill)
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
		subscriberRankingGSMCallFailureAnalysisByCallDrops.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_SUMMARY_SUBSCRIBER_RANKING_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
		
		//Drill on IMSI 
		subscriberRankingGSMCallFailureAnalysisByCallDrops.clickTableCell(0, GuiStringConstants.IMSI);
		
		//Check headers (post drill)
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
		subscriberRankingGSMCallFailureAnalysisByCallDrops.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
		
		subscriberRankingGSMCallFailureAnalysisByCallDrops.setTimeRange(TimeRange.ONE_DAY);
		selenium.waitForPageLoadingToComplete();
		
		assertTrue(FailureReasonStringConstants.TEMPORAL_REFERENCE_DIFFERENT_THAN_EXPECTED,
				subscriberRankingGSMCallFailureAnalysisByCallDrops.getTimeRange().equals(TimeRange.ONE_DAY.getLabel()));
		
		TimeRange[] timeRanges = getAllTimeRanges();
		
		for (TimeRange timeRange : timeRanges) {
			assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
					getDataAndCompare(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));	
				
			assertTrue(FailureReasonStringConstants.INVALID_EVENT_TIME_IN_GUI_DATA, checkEventTimesInGuiAreValid(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));
		}
	}
	

	//pm
	
	public boolean checkEventTimesInGuiAreValid (CommonWindow commonwindow, TimeRange timeRange) throws NoDataException, ParseException, PopUpException{
		commonwindow.setTimeRange(timeRange);
		final String latestEventTimeFromGui = commonwindow.getTableData(0, 0);
	
		
		Date latestEventTimeFromGuiStringIntoDateFormat; 
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		latestEventTimeFromGuiStringIntoDateFormat = (Date)formatter.parse(latestEventTimeFromGui);  
		
		int temp = calculateLatestAllowableEventTime(timeRange);
		long latency = temp * 1000 * 60;
		
		Date date = new Date();
		long currentTime = date.getTime();
		long timeAdjustedForLatencyLong = currentTime - latency ;
	
		long latestEventTimeFromGuiLong = latestEventTimeFromGuiStringIntoDateFormat.getTime();
			
		return latestEventTimeFromGuiLong < timeAdjustedForLatencyLong;

	}
	
	//pm
	private int calculateLatestAllowableEventTime(TimeRange timeRange) {
		int latency = 0;
		int timeRangeInMinutes = timeRange.getMiniutes();
		// creating a new Calendar instance isn't a problem, since this method
		// is immediately called after creating
		// the ones in the validate... method


		if (timeRangeInMinutes <= 5)
			latency = 0;
		else if (timeRangeInMinutes <= 120) // less, than or equal to two hours
			//latency = timeRangeInMinutes + 7;
			latency = 7;
		else if (timeRangeInMinutes < 10080) { // more, than two hours, but
												// less, than one week
			//latency = timeRangeInMinutes + 30;
			latency = 30;
		} else { // one week
			//latency = timeRangeInMinutes + 180;
			latency = 180;
		}

		return latency;
	}
	//pm/
	
	/*
	 * Gets an IMSI from GUI and compares it to the IMSIs from reserved data, if they match it compares their entire data row to make sure they match.
	 * Returns True if all rows match, returns false if they do not. 
	 */
	public boolean getDataAndCompare(CommonWindow commonwindow, TimeRange timeRange) throws NoDataException, PopUpException{
		
		commonwindow.setTimeRange(timeRange);
		
		List <Map<String, String>> GuiData = commonwindow.getAllTableData();
		List <Map<String, String>> ReservedData = dataIntegrValidatorObjCS.reserveDataList;
		
		String[] splitWindowHeader = commonwindow.getWindowHeaderLabel().split("\\s+");
		String imsiFromGui = splitWindowHeader[0];
		
		int counter = 0;

		for (Map<String,String> reserveData: ReservedData ){

			String imsiFromReservedData = reserveData.toString();
			imsiFromReservedData = imsiFromReservedData.split("IMSI=")[1];
			imsiFromReservedData = imsiFromReservedData.split(",")[0];
			System.out.println("imsiFromReservedData = " + imsiFromReservedData);

			
			for (Map<String,String> guiData: GuiData ){
				
				if (imsiFromReservedData.equals(imsiFromGui))
				{
					reserveData.remove(GuiStringConstants.EVENT_TIME);
					reserveData.remove(GuiStringConstants.IMSI);
					guiData.remove(GuiStringConstants.EVENT_TIME);
					guiData.remove(GuiStringConstants.IMSI);
				
					if (reserveData.equals(guiData))
					{
						counter ++;
						reserveData.put(GuiStringConstants.IMSI, imsiFromReservedData);
					}else{

						return false;
					}
				}
			}

		}
			
		if (counter == GuiData.size()){
			return true;
		}else{
			return false;
			
		}
		
	}

	//p,

	public String[] populateSet(TimeRange timeRange, CommonWindow commonWindow, int columnNumber)
			throws PopUpException, NoDataException, InterruptedException {
		commonWindow.setTimeRange(timeRange);
		Set allNodeNames = new HashSet();
		for (int recordNo = 0; recordNo < GSMConstants.DEFAULT_MAX_TABLE_ROWS; recordNo++) {
			final String nodeName = commonWindow.getTableData(recordNo, columnNumber);
			allNodeNames.add(nodeName);
		}
		// remove any blanks
		allNodeNames.remove("");
		// Put set into an array
		String[] array = (String[]) allNodeNames.toArray(new String[allNodeNames.size()]);
		return array;
	}// end populateSet function

	/*
	 * Returns all time ranges.
	 */
	private TimeRange[] getAllTimeRanges() {
		return TimeRange.values();
	}

	/**
	 * Requirement: Test Case: 5.7.4 Description: To verify that it is possible
	 * to view Detailed Failed Event Analysis by IMSI in the Event Analysis view
	 * in the Subscriber Tab
	 * 
	 * @throws NoDataException
	 * @throws PopUpException
	 * @throws InterruptedException
	 * 
	 */

	@Test
	public void imsiGroupRANGSMCFA_7_4() throws InterruptedException, PopUpException, NoDataException {

		subscriberTab.openEventAnalysisWindowForGSMCFA_PS(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
		selenium.waitForPageLoadingToComplete();
		System.out.println(imsiGroupSubscriberGSMConnectionFailure.getTableHeaders());
		System.out.println(IMSIEventAnalysisDetailedGSMCFAPSWindow);

		/*assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiGroupSubscriberGSMConnectionFailure
				.getTableHeaders().containsAll(IMSIEventAnalysisDetailedGSMCFAPSWindow));*/

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, imsiGroupSubscriberGSMConnectionFailure.getTimeRange()
				.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				imsiGroupSubscriberGSMConnectionFailure.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);

		final String terminalMake = imsiGroupSubscriberGSMConnectionFailure.getTableData(0, 2);

		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRangesPS(imsiGroupSubscriberGSMConnectionFailure,
						GuiStringConstants.TERMINAL_MAKE, terminalMake,
						imsiGroupSubscriberGSMConnectionFailure.getTableHeaders()));

	}

	/**
	 * Requirement: Test Case: 5.7.5 Description: To verify that it is possible
	 * to view Detailed Failed Event Analysis by IMSI Group in the Event
	 * Analysis view in the Subscriber Tab
	 * 
	 * @throws NoDataException
	 * @throws PopUpException
	 * @throws InterruptedException
	 * 
	 */

	@Test
	public void imsiGroupRANGSMCFA_7_5() throws InterruptedException, PopUpException, NoDataException {
		subscriberTab.openEventAnalysisWindowForGSMCFA_PS(ImsiMenu.IMSI_GROUP, true, "Regression_IMSI_group");
		selenium.waitForPageLoadingToComplete();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiGroupSubscriberGSMConnectionFailure
				.getTableHeaders().containsAll(GSMConstants.DEFAULT_SUMMARY_EVENT_ANALYSIS_GSM_CFA_PS_WINDOW_HEADERS));

		imsiGroupSubscriberGSMConnectionFailure.clickTableCell(0, GuiStringConstants.FAILURES);

		assertTrue(
				GuiStringConstants.ERROR_LOADING + GSMConstants.SUBSCRIBER_DETAILED_EVENT_ANALYSIS_WINDOW_TITLE,
				imsiGroupSubscriberGSMConnectionFailure.getWindowHeaderLabel().equals(
						GSMConstants.SUBSCRIBER_DETAILED_EVENT_ANALYSIS_WINDOW_TITLE));
		selenium.waitForPageLoadingToComplete();

		System.out.println(imsiGroupSubscriberGSMConnectionFailure.getTableHeaders());
		System.out.println(GSMConstants.GSM_CFA_PS_RESERVED_DATA_COLUMNS);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiGroupSubscriberGSMConnectionFailure
				.getTableHeaders().containsAll(GSMConstants.GSM_CFA_PS_RESERVED_DATA_COLUMNS));

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, imsiGroupSubscriberGSMConnectionFailure.getTimeRange()
				.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				imsiGroupSubscriberGSMConnectionFailure.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);

		final String terminalMake = imsiGroupSubscriberGSMConnectionFailure.getTableData(0, 2);

		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRangesPS(imsiGroupSubscriberGSMConnectionFailure,
						GuiStringConstants.TERMINAL_MAKE, terminalMake,
						imsiGroupSubscriberGSMConnectionFailure.getTableHeaders()));

	}

	/*
	 * On a drill down (on failures) view goes through all time ranges and
	 * checks if the data integrity is ok.
	 */
	private boolean validateDrillDownOnFailuresAllTimeRangesPS(CommonWindow commonWindow, String rankingType,
			String valueToSearchBy, List<String> headers) throws PopUpException, NoDataException {

		// will contain all the failures for the designated BSC, Access Area
		// etc. given in valueToSearchBy
		List<Map<String, String>> failuresForDesignatedOne;
		List<Map<String, String>> failuresInTable;
		Calendar currentCalDateTime;
		Calendar earliestCalDateTime; // this will be used as the starting time
										// for events which we query for
		DateFormat eventTimeDateFormat = new SimpleDateFormat(GSMConstants.DATEFORMAT_STRING_WITHOUT_MSEC);
		int eventTimeLatencyAdjust = 0; // needed because of the latency when
										// querying the different timeranges

		// there is no "Event Time" column in the reserve data
		if (headers.contains(GuiStringConstants.EVENT_TIME))
			headers.remove(GuiStringConstants.EVENT_TIME);

		failuresForDesignatedOne = dataIntegrValidatorObjPS.getFailures(headers, rankingType, valueToSearchBy);

		TimeRange[] timeRanges = getAllTimeRanges();

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			// have to query the current time each time range, as some checks
			// could take more than a minute
			failuresInTable = commonWindow.getAllTableData();
			List<Map<String, String>> failuresInTableReduced = new ArrayList<Map<String, String>>();
			Map<String, String> rowData = new HashMap<String, String>();
			// columns which we want to get from the grid window
			for (Map<String, String> failure : failuresInTable) {
				rowData.put(GuiStringConstants.EVENT_TIME, failure.get(GuiStringConstants.EVENT_TIME));
				for (String header : headers) {
					rowData.put(header, failure.get(header));
				}
			}
			failuresInTableReduced.add(rowData);
			currentCalDateTime = Calendar.getInstance();
			currentCalDateTime.clear(Calendar.SECOND);
			currentCalDateTime.clear(Calendar.MILLISECOND);
			earliestCalDateTime = (Calendar) currentCalDateTime.clone();

			// there is some latency for the time ranges
			eventTimeLatencyAdjust = calculateDateAdjustWithLatencyInMinutes(timeRange);
			// Events before this time, shouldn't be in the table
			/*
			 * TODO: maybe the current datetime should be subtracted with the
			 * latency as well, this doesn't mean the eventTimeLatencyAdjust,
			 * just the latency without the minutes of the time range
			 */
			earliestCalDateTime.add(Calendar.MINUTE, -eventTimeLatencyAdjust);

			if (!dataIntegrValidatorObjPS.isDrillDownOnFailuresDataValid(headers, failuresInTableReduced,
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
	
	@Test
	public void GSMCFR168() throws PopUpException, NoDataException, InterruptedException, ParseException {
		
		//Code to open event analysis and drill to detailed failed event analysis by imsi
		
		
		subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
		
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				imsiSubscriberGSMCallFailure.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));
		
		imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
		
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				imsiSubscriberGSMCallFailure.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
			
		String controller = imsiSubscriberGSMCallFailure.getTableData(0, 14);
		System.out.println("controller " + controller);
		imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
	
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				imsiSubscriberGSMCallFailure.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
		
		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure, GuiStringConstants.CONTROLLER, controller));	
		//TODO TC has now been modified to take account of extra columns, integrity will now also need to be altered to take account of failure ratio column, 
		//datagen csv may also need modification
	}
	
	@Test
	public void GSMCFR158() throws PopUpException, NoDataException, InterruptedException, ParseException {
		
		//Code to open event analysis and drill to detailed failed event analysis by imsi
		subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
		
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				imsiSubscriberGSMCallFailure.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));
		
		imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
		
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				imsiSubscriberGSMCallFailure.getTableHeaders().containsAll
		(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
		
		//assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
		//		validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure, GuiStringConstants.CONTROLLER, controller));	
		//TODO TC has now been modified to take account of extra columns , integrity will now also need to be altered to take account of failure ratio column, 
		//datagen csv may also need modification
	}
	
	private boolean validateDrillDownAllTimeRanges(CommonWindow commonWindow, String designatedHeader,
		String valueForDesignatedHeader) throws PopUpException, NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		int numFailuresReserveData = 0;
		int numSuccessesReserveData = 0;
		int impactedSubscribersReserveData = 0;
		int impactedCellsReserveData = 0;
		double failureRatio = 0;

		headers = commonWindow.getTableHeaders();

		if (headers.contains(GuiStringConstants.FAILURES))
			numFailuresReserveData = dataIntegrValidatorObjCS.calculateNumFailures(designatedHeader,
					valueForDesignatedHeader);
			System.out.println("numFailuresReserveData " + numFailuresReserveData);
		if (headers.contains(GuiStringConstants.FAILURE_RATIO)){
			numSuccessesReserveData = dataIntegrValidatorObjCS.calculateNumSuccesses(designatedHeader,
					valueForDesignatedHeader);
			failureRatio = dataIntegrValidatorObjCS.calculateFailureRatio(numFailuresReserveData, numSuccessesReserveData);
			//failureRatio = numFailuresReserveData/numSuccessesReserveData;
		}
		
		System.out.println("Success = " + numSuccessesReserveData);
		System.out.println("Failure = " + numFailuresReserveData);
		//System.out.println("failureRatio = " + failureRatio);
			
		if (headers.contains(GuiStringConstants.IMPACTED_SUBSCRIBERS))
			impactedSubscribersReserveData = dataIntegrValidatorObjCS.calculateImpactedSubscribers(designatedHeader,
					valueForDesignatedHeader);
		
		if (headers.contains(GuiStringConstants.IMPACTED_CELLS))
			impactedCellsReserveData = dataIntegrValidatorObjCS.calculateImpactedCells(designatedHeader,
					valueForDesignatedHeader);
		
		
			System.out.println("impactedSubscribersReserveData " + impactedSubscribersReserveData);
			System.out.println("impactedCellsReserveData " + impactedCellsReserveData);
			System.out.println("failureRatio " + failureRatio);
			
		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			if (!checkGridRowDataIntegrity(commonWindow, timeRange, designatedHeader, valueForDesignatedHeader,
					headers, numFailuresReserveData, impactedSubscribersReserveData, impactedCellsReserveData, failureRatio)) {
				return false;
			}
		}

		return true;
	}
	
	private boolean checkGridRowDataIntegrity(CommonWindow commonWindow, TimeRange timeRange, String designatedHeader,
			String valueForDesignatedHeader, List<String> headers, int failuresReserveData,
			int impactedSubscribersReserveData, int impactedCellsReserveData, double failureRatioReservedData) throws NoDataException {

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
			isDataValid = dataIntegrValidatorObjCS.validateGridRowDataIncludingFailureRatio(headers, drillDownTableRow, timeRange,
					designatedHeader, valueForDesignatedHeader, failuresReserveData, impactedSubscribersReserveData, impactedCellsReserveData, failureRatioReservedData);
		}

		return isDataValid;
	}
	
}
