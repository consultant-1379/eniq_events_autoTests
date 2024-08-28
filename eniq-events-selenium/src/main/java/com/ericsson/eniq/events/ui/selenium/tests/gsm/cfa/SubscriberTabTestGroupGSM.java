package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
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
public class SubscriberTabTestGroupGSM extends EniqEventsUIBaseSeleniumTest {

    private final DataIntegrityValidator dataIntegrValidatorObjCS = new DataIntegrityValidator();

    private final DataIntegrityValidator dataIntegrValidatorObjPS = new DataIntegrityValidator();

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

    // Added as part of US174 for MSISDN flow by earunms.
    @Autowired
    @Qualifier("msisdnSubscriberSummaryGSMCFA")
    private CommonWindow msisdnSubscriberGSMCallFailure;

    // ***** Headers for the different windows *****

    List<String> IMSIEventAnalysisDetailedGSMCFAPSWindow;

    public static final List<String> IMSIEventAnalysisDetailedGSMCFAWindow = Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA);

    final List<String> imsiGroupDetailedEventAnalysisGSMCFAWindow = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.MSISDN, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI, GuiStringConstants.CHANNEL_TYPE,
            GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.GROUP_NAME, GuiStringConstants.EXTENDED_CAUSE_VALUE);
    
    public static final List<String> IMSIGroupSummaryWindow = Arrays.asList(GuiStringConstants.EVENT_TYPE,
			GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);
    
    public static final List<String> IMSISummaryWindow = Arrays.asList(GuiStringConstants.EVENT_TYPE,
			GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO);
    

    // );

    @Override
    @Before
    public void setUp() {
        super.setUp();

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

    // pm

    public boolean checkEventTimesInGuiAreValid(final CommonWindow commonwindow, final TimeRange timeRange)
            throws NoDataException, ParseException, PopUpException {
        commonwindow.setTimeRange(timeRange);
        final String latestEventTimeFromGui = commonwindow.getTableData(0, 0);

        Date latestEventTimeFromGuiStringIntoDateFormat;
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        latestEventTimeFromGuiStringIntoDateFormat = formatter.parse(latestEventTimeFromGui);

        final int temp = calculateLatestAllowableEventTime(timeRange);
        final long latency = temp * 1000 * 60;

        final Date date = new Date();
        final long currentTime = date.getTime();
        final long timeAdjustedForLatencyLong = currentTime - latency;

        final long latestEventTimeFromGuiLong = latestEventTimeFromGuiStringIntoDateFormat.getTime();

        return latestEventTimeFromGuiLong < timeAdjustedForLatencyLong;

    }

    // pm
    private int calculateLatestAllowableEventTime(final TimeRange timeRange) {
        int latency = 0;
        final int timeRangeInMinutes = timeRange.getMiniutes();
        // creating a new Calendar instance isn't a problem, since this method
        // is immediately called after creating
        // the ones in the validate... method

        if (timeRangeInMinutes <= 5)
            latency = 0;
        else if (timeRangeInMinutes <= 120) // less, than or equal to two hours
            // latency = timeRangeInMinutes + 7;
            latency = 7;
        else if (timeRangeInMinutes < 10080) { // more, than two hours, but
                                               // less, than one week
            // latency = timeRangeInMinutes + 30;
            latency = 30;
        } else { // one week
            // latency = timeRangeInMinutes + 180;
            latency = 180;
        }

        return latency;
    }

    // pm/

    /*
     * Gets an IMSI from GUI and compares it to the IMSIs from reserved data, if
     * they match it compares their entire data row to make sure they match.
     * Returns True if all rows match, returns false if they do not.
     */
    public boolean getDataAndCompare(final CommonWindow commonwindow, final TimeRange timeRange)
            throws NoDataException, PopUpException {

        commonwindow.setTimeRange(timeRange);

        final List<Map<String, String>> GuiData = commonwindow.getAllTableData();
        final List<Map<String, String>> ReservedData = dataIntegrValidatorObjCS.reserveDataList;

        final String[] splitWindowHeader = commonwindow.getWindowHeaderLabel().split("\\s+");
        final String imsiFromGui = splitWindowHeader[0];

        int counter = 0;

        for (final Map<String, String> reserveData : ReservedData) {

            String imsiFromReservedData = reserveData.toString();
            imsiFromReservedData = imsiFromReservedData.split("IMSI=")[1];
            imsiFromReservedData = imsiFromReservedData.split(",")[0];
            System.out.println("imsiFromReservedData = " + imsiFromReservedData);

            for (final Map<String, String> guiData : GuiData) {

                if (imsiFromReservedData.equals(imsiFromGui)) {
                    reserveData.remove(GuiStringConstants.EVENT_TIME);
                    reserveData.remove(GuiStringConstants.IMSI);
                    guiData.remove(GuiStringConstants.EVENT_TIME);
                    guiData.remove(GuiStringConstants.IMSI);

                    if (reserveData.equals(guiData)) {
                        counter++;
                        reserveData.put(GuiStringConstants.IMSI, imsiFromReservedData);
                    } else {

                        return false;
                    }
                }
            }

        }

        if (counter == GuiData.size()) {
            return true;
        }
        return false;

    }

    // p,

    public String[] populateSet(final TimeRange timeRange, final CommonWindow commonWindow, final int columnNumber)
            throws PopUpException, NoDataException, InterruptedException {
        commonWindow.setTimeRange(timeRange);
        final Set allNodeNames = new HashSet();
        for (int recordNo = 0; recordNo < GSMConstants.DEFAULT_MAX_TABLE_ROWS; recordNo++) {
            final String nodeName = commonWindow.getTableData(recordNo, columnNumber);
            allNodeNames.add(nodeName);
        }
        // remove any blanks
        allNodeNames.remove("");
        // Put set into an array
        final String[] array = (String[]) allNodeNames.toArray(new String[allNodeNames.size()]);
        return array;
    }// end populateSet function

    /*
     * Returns all time ranges.
     */
    private TimeRange[] getAllTimeRanges() {
        return TimeRange.values();
    }

   
    /*
     * On a drill down (on failures) view goes through all time ranges and
     * checks if the data integrity is ok.
     */
    private boolean validateDrillDownOnFailuresAllTimeRangesPS(final CommonWindow commonWindow,
            final String rankingType, final String valueToSearchBy, final List<String> headers) throws PopUpException,
            NoDataException {

        // will contain all the failures for the designated BSC, Access Area
        // etc. given in valueToSearchBy
        List<Map<String, String>> failuresForDesignatedOne;
        List<Map<String, String>> failuresInTable;
        Calendar currentCalDateTime;
        Calendar earliestCalDateTime; // this will be used as the starting time
                                      // for events which we query for
        final DateFormat eventTimeDateFormat = new SimpleDateFormat(GSMConstants.DATEFORMAT_STRING_WITHOUT_MSEC);
        int eventTimeLatencyAdjust = 0; // needed because of the latency when
                                        // querying the different timeranges

        // there is no "Event Time" column in the reserve data
        if (headers.contains(GuiStringConstants.EVENT_TIME))
            headers.remove(GuiStringConstants.EVENT_TIME);

        failuresForDesignatedOne = dataIntegrValidatorObjPS.getFailures(headers, rankingType, valueToSearchBy);

        final TimeRange[] timeRanges = getAllTimeRanges();

        for (final TimeRange timeRange : timeRanges) {
            commonWindow.setTimeRange(timeRange);
            // have to query the current time each time range, as some checks
            // could take more than a minute
            failuresInTable = commonWindow.getAllTableData();
            final List<Map<String, String>> failuresInTableReduced = new ArrayList<Map<String, String>>();
            final Map<String, String> rowData = new HashMap<String, String>();
            // columns which we want to get from the grid window
            for (final Map<String, String> failure : failuresInTable) {
                rowData.put(GuiStringConstants.EVENT_TIME, failure.get(GuiStringConstants.EVENT_TIME));
                for (final String header : headers) {
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
    private int calculateDateAdjustWithLatencyInMinutes(final TimeRange timeRange) {
        int latency = 0;
        final int timeRangeInMinutes = timeRange.getMiniutes();
        // creating a new Calendar instance isn't a problem, since this method
        // is immediately called after creating
        // the ones in the validate... method
        final Calendar tempCalendar = Calendar.getInstance();

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

   
    /** GSMCFR 164 */

    @Test
    public void imsiEventAnalysisCauseGroupByTerminal164() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);
        selenium.waitForPageLoadingToComplete();
    //    System.out.println(imsiSubscriberCallFailure.getTableHeaders());
    //    System.out.println(IMSIEventAnalysisDetailedGSMCFAWindow);
                
        final String factor = reservedDataHelper.getCommonReservedData(CommonDataType.GSM_CALL_DROP_FAILURE_FACTOR);
        
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);       
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
        System.out.println("TAC: " + tac_value);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        final String event_type = imsiSubscriberGSMCallFailure.getTableData(0, 2);
        final String manufacturer_value = imsiSubscriberGSMCallFailure.getTableData(0, 0);
        final String model_value = imsiSubscriberGSMCallFailure.getTableData(0, 1);

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        //Due to GUI Bug it is commented
        final String causeGroupSummaryWindowTitle = manufacturer_value + " - " + model_value + " - " + event_type
                + " - " + tac_value + " - Terminal GSM Call Failure : Cause Group Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + causeGroupSummaryWindowTitle,
                selenium.isTextPresent(causeGroupSummaryWindowTitle));

     // Set TimeRange
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            imsiSubscriberGSMCallFailure.setTimeRange(time);

            // Code for Data Assertion.
            final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS.validateDataForCauseGroup(uiWindowData,
                    Integer.parseInt(factor), time.getMiniutes());

            assertTrue("Data Validation Failed", returnValue);
        }

    } 

  
    /** GSMCFR 563, 564, 565, 566, 568 */

    @Test
    public void cellSummaryFlow_CallSetupFailure563() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);
        selenium.waitForPageLoadingToComplete();

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // data validation
        /*     assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
        			dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
        				GuiStringConstants.IMSI, GSMConstants.CALLSETUP_IMSI_NUMBER)); */
               
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_ACCESS_AREA_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String event_type = imsiSubscriberGSMCallFailure.getTableData(0, 2);

        if (event_type.equals(("Call Setup Failures"))) {
            System.out.println("Event Type is Call Setup Failure");
        } else {
            System.err.println("Event Type is not Call Setup Failure");
            //return
        }

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CAUSEGROUP_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMSI);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        //  assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
        //           .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_CAUSEGROUPBYTERMINAL_EVENTANALYSIS_GSM_CFA_WINDOW_HEADERS));

    }

    /** GSMCFR 558, 557, 560, 561 */

    @Test
    public void bscSummaryFlow_CallSetupFailure558() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);
        selenium.waitForPageLoadingToComplete();      
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(
                FailureReasonStringConstants.HEADER_MISMATCH,
                imsiSubscriberGSMCallFailure.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String event_type = imsiSubscriberGSMCallFailure.getTableData(0, 2);

        if (event_type.equals(("Call Setup Faliure"))) {
            System.out.println("Event Type is Call Setup Failure");
        } else {
            System.err.println("Event Type is not Call Setup Failure");
            //return
        }

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMPACTED_CELLS);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_IMPACTED_CELL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        
   //     DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CAUSEGROUP_GSM_CFA_WINDOW_HEADERS
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
        	                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CAUSEGROUP_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMSI);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

    }

    /** GSMCFR-570, GSMCFR-573, GSMCFR-574, GSMCFR-575 */

    @Test
    public void subscriberSummaryFlow_ImsiAndTerminal_CSF570() throws PopUpException, NoDataException,
            InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);
        selenium.waitForPageLoadingToComplete();

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(
                FailureReasonStringConstants.HEADER_MISMATCH,
                imsiSubscriberGSMCallFailure.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String event_type = imsiSubscriberGSMCallFailure.getTableData(0, 2);

        if (event_type.equals(("Call Setup Faliure"))) {
            System.out.println("Event Type is Call Setup Failure");
        } else {
            System.err.println("Event Type is not Call Setup Failure");
            //return
        }

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_DETAILS_WINDOW));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMSI);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

    }

/*    
    // GSMCFR 494 -
    /*summary of GSM RAN Call Setup Failures and failure ratio for a selected IMSI as well as a summary of the Dropped Calls and failure ratio
    * so that I can understand the extent of the Call Setup Failures and Dropped Calls experienced by the subscriber. */

    @Test
    public void GSMCFR494() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);
        selenium.waitForPageLoadingToComplete();
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        final String eventAnalysisWindowTitle = GSMConstants.CALLSETUP_IMSI_NUMBER
                + " - IMSI - GSM Call Failure Event Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
                selenium.isTextPresent(eventAnalysisWindowTitle));
        final String event_type = imsiSubscriberGSMCallFailure.getTableData(0, 0);
               
        final boolean returnValue=event_type.equals("Call Setup Failures");
        assertTrue("Data Validation Failed", returnValue);

        //   String dataFromReservedData = dataIntegrValidatorObjCS.checkReservedDataForImsi();

    }
       
    @Test
    public void GSMCFR557() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.CALLSETUP_IMSI_NUMBER);
        selenium.waitForPageLoadingToComplete();
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        final String eventAnalysisWindowTitle = GSMConstants.CALLSETUP_IMSI_NUMBER
                + " - IMSI - GSM Call Failure Event Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
                selenium.isTextPresent(eventAnalysisWindowTitle));
                      
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
    	TimeRange[] timeRanges; 
    	 if (!allTimeLabel.isEmpty()) {
        	final String[] timeLabels = allTimeLabel.split(",");
        	timeRanges = new TimeRange[timeLabels.length];
        	for (int i = 0; i < timeLabels.length; i++) {
        		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
        	}
        } else {
        	timeRanges = TimeRange.values();
        }
    	 
    	 for (final TimeRange time : timeRanges) {
    		 imsiSubscriberGSMCallFailure.setTimeRange(time);
    		 
    		 
    		// Code for Data Assertion.
    		//System.out.println ("Loop Number" + time); 
    		 List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
    		 boolean returnValue = dataIntegrValidatorObjCS.validateCallFailureIMSINumber(uiWindowData, time.getMiniutes());
    		 assertTrue("Data Validation Failed", returnValue);
    	 }
                
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));
       
    }

    //IMSI group
    @Test
    public void IMSIGroupGSMCFR579() throws PopUpException, NoDataException,
            InterruptedException, ParseException {

        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
                GSMConstants.REGRESSION_IMSI_GROUP_NAME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_GROUP_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        /*final String grpSummaryWindowTitle = "Subscriber Group GSM Call Failure Event Analysis";

        assertTrue(GuiStringConstants.ERROR_LOADING + grpSummaryWindowTitle,
                selenium.isTextPresent(grpSummaryWindowTitle));*/
        
        
        
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
    	TimeRange[] timeRanges; 
    	 if (!allTimeLabel.isEmpty()) {
        	final String[] timeLabels = allTimeLabel.split(",");
        	timeRanges = new TimeRange[timeLabels.length];
        	for (int i = 0; i < timeLabels.length; i++) {
        		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
        	}
        } else {
        	timeRanges = TimeRange.values();
        }
    	 
    	 for (final TimeRange time : timeRanges) {
    		 imsiSubscriberGSMCallFailure.setTimeRange(time);
    		 
    		 
    		// Code for Data Assertion.
    		//System.out.println ("Loop Number" + time); 
    		 List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
    		 boolean returnValue = dataIntegrValidatorObjCS.validateCallFailureIMSIGroup(uiWindowData, time.getMiniutes());
    		 assertTrue("Data Validation Failed", returnValue);
    	 }
              

    }

    
    
    @Test
    public void subscriberWithinSubscriberGrpCsfGSMCFR579() throws PopUpException, NoDataException,
            InterruptedException, ParseException {

        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
                GSMConstants.REGRESSION_IMSI_GROUP_NAME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_GROUP_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        /*final String grpSummaryWindowTitle = "Subscriber Group GSM Call Failure Event Analysis";

        assertTrue(GuiStringConstants.ERROR_LOADING + grpSummaryWindowTitle,
                selenium.isTextPresent(grpSummaryWindowTitle));*/

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_GROUP_DETAIL_ANALYSIS_FAILURE_RATIO_HEADERS));

        /*final String summaryWindowTitle = "Subscriber Group GSM Call Failure Event Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + summaryWindowTitle, selenium.isTextPresent(summaryWindowTitle));*/

        for (int index = 0; index < GSMConstants.IMSI_GROUP_MEMBER_NAMES.length; index++) {

            final String imsi_value = imsiSubscriberGSMCallFailure.getTableData(index, 0);

            if (imsi_value == GSMConstants.CALLSETUP_IMSI_NUMBER) {

                imsiSubscriberGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
                break;
            }
        }

        pause(GSMConstants.DEFAULT_WAIT_TIME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CSF_GSM_CFA_WINDOW_HEADERS));

        /*final String detailWindowTitle = "Subscriber GSM Call Failure Event Analysis Detail";

        assertTrue(GuiStringConstants.ERROR_LOADING + detailWindowTitle, selenium.isTextPresent(detailWindowTitle));*/

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                validateEventAnalysisSummary(imsiSubscriberGSMCallFailure));

    }

    /**
     * GSMCFR-248 Subscribers Within Subscriber Group Failure Ratio
     * 
     * As a user I want know the dropped call failure ratio for the subscribers in a selected Subscriber 
     * Group so that I can understand the proportion dropped calls experienced by the subscribers in a selected
     *  Subscriber Group, this will help me understand the level of the problem. 
     * 
     * @throws NoDataException
     * @throws PopUpException
     * @throws InterruptedException
     * @throws ParseException
     * 
     */

  //  @Test
    public void SubscribersWithinSubscriberGroupFailureRatioGSMCFR248() throws PopUpException, NoDataException,
            InterruptedException, ParseException {

        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
                GSMConstants.REGRESSION_IMSI_GROUP_NAME);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_GROUP_DETAIL_ANALYSIS_FAILURE_RATIO_HEADERS));

        //A check to make sure that none of the IMSI's in the GUI are not members or our reserved Data group
        assertTrue(FailureReasonStringConstants.NOT_A_GROUP_MEMBER,
                checkIMSIGroupMembership(imsiSubscriberGSMCallFailure));

        final List<Map<String, String>> allWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
        final List<Map<String, String>> reserveDataList = dataIntegrValidatorObjCS.reserveDataList;

        for (int rowNo = 0; rowNo < allWindowData.size(); rowNo++) {
            final Map<String, String> rowToCheck = imsiSubscriberGSMCallFailure.getAllDataAtTableRow(rowNo);
            for (final Map<String, String> oneRecord : reserveDataList) {

                if (rowToCheck.get(GSMConstants.IMSI).equals(oneRecord.get(GSMConstants.IMSI))) {

                    assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                            dataIntegrValidatorObjCS.validateDrillDownAllTimeRangesForGroupsDetailedView(
                                    imsiSubscriberGSMCallFailure, GuiStringConstants.IMSI,
                                    rowToCheck.get(GSMConstants.IMSI), rowNo));
                }
            }
        }
    }

    @Test
    public void TestExtendeCauseValueDeatilScreenCallDropFailuresGSMCFR166() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        /*final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        	String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        	System.out.println("TAC to be selected: " + tacValue); */

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        /*final String eventAnalysisDetailWindowTitle = manufacturer + " - " + model + " - " + GSMConstants.IMSI_NUMBER_US_165
        + " - IMSI - Terminal GSM Call Failure Event Analysis Detail"; */

        final int rowId = imsiSubscriberGSMCallFailure.findFirstTableRowWhereMatchingAnyValue(
                GuiStringConstants.CAUSE_GROUP, GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE);
        imsiSubscriberGSMCallFailure.clickTableCell(rowId, GuiStringConstants.CAUSE_GROUP);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_DETAILS_WINDOW));

        // List uiWindowData will capture the entire content of the screen as list of maps.
        // If assertions pass through the boolean variable  returnValue willl return and 1 test case passes and vice versa.

        final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
        final boolean returnValue = dataIntegrValidatorObjCS.validateDataDrillDownFailures(uiWindowData);

        assertTrue("Data Validation Failed", returnValue);

        /*----- Code for Data Assertion on further Dril Down. Currently put on hold as the further window headers are not as per expectation -------------------     
        		
        		/*imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMSI);
        		
        		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));
        		
        		//code for back button

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
        		
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
        		
        		//code for back button

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        		
        		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS));*/

    }

    @Test
    public void TestExtendeCauseValueDeatilScreenCallSetUpFailuresGSMCFR166() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        /*final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        System.out.println("TAC to be selected: " + tacValue); */

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        /*final String eventAnalysisDetailWindowTitle = manufacturer + " - " + model + " - " + GSMConstants.IMSI_NUMBER_US_165
        + " - IMSI - Terminal GSM Call Failure Event Analysis Detail"; */

        //assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisDetailWindowTitle,
        //	selenium.isTextPresent(eventAnalysisDetailWindowTitle));

        final int rowId = imsiSubscriberGSMCallFailure.findFirstTableRowWhereMatchingAnyValue(
                GuiStringConstants.CAUSE_GROUP, GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE);
        imsiSubscriberGSMCallFailure.clickTableCell(rowId, GuiStringConstants.CAUSE_GROUP);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_DETAILS_WINDOW));

        final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
        final boolean returnValue = dataIntegrValidatorObjCS.validateDataDrillDownFailures(uiWindowData);

        assertTrue("Data Validation Failed", returnValue);

        /*----- Code for Data Assertion on further Dril Down. Currently put on hold as the further window headers are not as per expectation -------------------     
        		
        		/*imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.IMSI);
        		
        		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));
        		
        		//code for back button

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
        		
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
        		
        		//code for back button

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        		
        		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders().
        				containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS));*/

    }

    @Test
    public void TestCauseGroupCallDropFailuresGSMCFR165() throws PopUpException, NoDataException, InterruptedException,
            ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        final String factor = reservedDataHelper.getCommonReservedData(CommonDataType.GSM_CALL_DROP_FAILURE_FACTOR);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        /*final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        System.out.println("TAC to be selected: " + tacValue); */

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        // Set TimeRange
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            imsiSubscriberGSMCallFailure.setTimeRange(time);

            // Code for Data Assertion.
            final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS.validateDataForCauseGroup(uiWindowData,
                    Integer.parseInt(factor), time.getMiniutes());

            assertTrue("Data Validation Failed", returnValue);
        }

    }

    @Test
    public void TestCauseGroupCallSetUpFailuresGSMCFR165() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        final String factor = reservedDataHelper.getCommonReservedData(CommonDataType.GSM_CALL_SET_UP_FAILURE_FACTOR);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        /*final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        System.out.println("TAC to be selected: " + tacValue); */

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        // Set TimeRange
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            imsiSubscriberGSMCallFailure.setTimeRange(time);

            // Code for Data Assertion.
            final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS.validateDataForCauseGroup(uiWindowData,
                    Integer.parseInt(factor), time.getMiniutes());

            assertTrue("Data Validation Failed", returnValue);
        }
    }

    @Test
    public void TestExtendedCauseCallDropFailures165() throws PopUpException, NoDataException, InterruptedException,
            ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        final String factor = reservedDataHelper.getCommonReservedData(CommonDataType.GSM_CALL_DROP_FAILURE_FACTOR);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        /*final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        System.out.println("TAC to be selected: " + tacValue); */

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        final int rowId = imsiSubscriberGSMCallFailure.findFirstTableRowWhereMatchingAnyValue(
                GuiStringConstants.CAUSE_GROUP, GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE);

        imsiSubscriberGSMCallFailure.clickTableCell(rowId, GuiStringConstants.CAUSE_GROUP);

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            imsiSubscriberGSMCallFailure.setTimeRange(time);

            // Code for Data Assertion.
            //System.out.println ("Loop Number" + time); 
            final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS.validateDataForExtendedCauseCode(uiWindowData,
                    GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE, Integer.parseInt(factor), time.getMiniutes());
            assertTrue("Data Validation Failed", returnValue);
        }

    }

    @Test
    public void TestExtendedCauseCallSetUpFailures165() throws PopUpException, NoDataException, InterruptedException,
            ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        final String factor = reservedDataHelper.getCommonReservedData(CommonDataType.GSM_CALL_SET_UP_FAILURE_FACTOR);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final int tacHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.TAC);
        final String tacValue = imsiSubscriberGSMCallFailure.getTableData(0, tacHeaderIndex);
        System.out.println("TAC to be selected: " + tacValue);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final int manufacturerHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MANUFACTURER);
        //final int modelHeaderIndex = imsiSubscriberGSMCallFailure.getTableHeaderIndex(GuiStringConstants.MODEL);

        //String manufacturer = imsiSubscriberGSMCallFailure.getTableData(0, manufacturerHeaderIndex);
        //String model = imsiSubscriberGSMCallFailure.getTableData(0, modelHeaderIndex);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));

        final int rowId = imsiSubscriberGSMCallFailure.findFirstTableRowWhereMatchingAnyValue(
                GuiStringConstants.CAUSE_GROUP, GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE);

        imsiSubscriberGSMCallFailure.clickTableCell(rowId, GuiStringConstants.CAUSE_GROUP);

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            imsiSubscriberGSMCallFailure.setTimeRange(time);

            // Code for Data Assertion.
            //System.out.println ("Loop Number" + time); 
            final List<Map<String, String>> uiWindowData = imsiSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS.validateDataForExtendedCauseCode(uiWindowData,
                    GSMConstants.CAUSE_GROUP_FOR_EXTENDED_CAUSE, Integer.parseInt(factor), time.getMiniutes());
            assertTrue("Data Validation Failed", returnValue);
        }

    }

    @Test
    public void TestMSISDNSummaryScreenCallSetUpFailuresGSMCFR174() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.MSISDN, true,
                GSMConstants.MSISDN_FOR_CALL_FAILURE_ANALYIS);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, msisdnSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.MSISDN_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // There is a bug for deviation in window title (JIRA 990). The below code can be executed post that alone. May require changes.
        //final String summaryWindowTitle = GSMConstants.MSISDN_FOR_CALL_FAILURE_ANALYIS + " GSM Call Failure : Event Analysis Summary";

        //assertTrue(GuiStringConstants.ERROR_LOADING + summaryWindowTitle,
        //			selenium.isTextPresent(summaryWindowTitle));

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
        TimeRange[] timeRanges;
        if (!allTimeLabel.isEmpty()) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
        } else {
            timeRanges = TimeRange.values();
        }

        for (final TimeRange time : timeRanges) {
            msisdnSubscriberGSMCallFailure.setTimeRange(time);
            //msisdnSubscriberGSMCallFailure.cl

            // Code for Data Assertion.
            //System.out.println ("Loop Number"  + time); 
            final List<Map<String, String>> uiWindowData = msisdnSubscriberGSMCallFailure.getAllTableData();
            final boolean returnValue = dataIntegrValidatorObjCS
                    .validateMSISDNSummary(uiWindowData, time.getMiniutes());
            assertTrue("Data Validation Failed", returnValue);
        }
    }
    
 /* Below test cases is commented because these functionality verification is merged on other test cases 
  * ####################################################
  * */
    /*   @Test
    public void GSMCFR246() throws PopUpException, NoDataException, InterruptedException, ParseException {
        // Code to open event analysis and drill to detailed failed event
        // analysis by imsi
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
                GSMConstants.REGRESSION_IMSI_GROUP_NAME);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_GROUP_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        final String eventAnalysisSummaryWindowTitle = "Subscriber Group GSM Call Failure : Event Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisSummaryWindowTitle,
                selenium.isTextPresent(eventAnalysisSummaryWindowTitle));

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRangesForGroups(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.IMSI, GSMConstants.IMSI_GROUP_MEMBER_NAMES));
    } */  
    
   
    /**
     * Summary of extended cause by Cause Grouping and Cell
     * 
     * As a user
     * I want to be able to view, for a selected Cause Group the distribution of call drops across Extended Causes 
     * applicable for the selected Cell I also want to see the number of impacted subscribers due to each extended cause. 
     * 
     * @throws NoDataException
     * @throws PopUpException
     * @throws InterruptedException
     * 
     */
/*    @Test
    public void summaryOfExtendedCauseByCauseGroupingAndCellGSMCFR163() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true,
                GSMConstants.IMSI_NUMBER_CAUSE_GROUP_ANALYSIS);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String accessArea = imsiSubscriberGSMCallFailure.getTableData(0, 16);
        System.out.println("Access Area: " + accessArea);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_ACCESS_AREA_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
        //        final String causeGroup = imsiSubscriberGSMCallFailure.getTableData(0, 1);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY)); */

        /*final String eventAnalysisDetailWindowTitle = accessArea + " - Call Drops - " + causeGroup
                + " - Access Area GSM Call Failure : Extended Cause Analysis Summary";

        	assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisDetailWindowTitle,
        		selenium.isTextPresent(eventAnalysisDetailWindowTitle));*/

     /*   assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.ACCESS_AREA, accessArea));
    } */
    
    /*  @Test
    public void GSMCFR169() throws PopUpException, NoDataException, InterruptedException, ParseException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String controller = imsiSubscriberGSMCallFailure.getTableData(0, 15);
        System.out.println("controller " + controller);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
        final String causeGroup = imsiSubscriberGSMCallFailure.getTableData(0, 1);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));

        final String eventAnalysisDetailWindowTitle = controller + " - Call Drops - " + causeGroup
                + " - Controller GSM Call Failure : Extended Cause Analysis Summary";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisDetailWindowTitle,
                selenium.isTextPresent(eventAnalysisDetailWindowTitle));

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.CONTROLLER, controller));
    } */

    
    // GSMCFR 162

    /*   @Test
       public void GSMCFR162() throws PopUpException, NoDataException, InterruptedException {
           subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
           selenium.waitForPageLoadingToComplete();

           assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                   dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                           GuiStringConstants.IMSI, GSMConstants.IMSI_NUMBER));

           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
           pause(GSMConstants.DEFAULT_WAIT_TIME);
           pause(GSMConstants.DEFAULT_WAIT_TIME);
           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

           final String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
           System.out.println("TAC: " + tac_value);
           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.TAC);
           pause(GSMConstants.DEFAULT_WAIT_TIME);

       } */

    /*   @Test
       public void GSMCFR168() throws PopUpException, NoDataException, InterruptedException, ParseException {
           subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

           final String controller = imsiSubscriberGSMCallFailure.getTableData(0, 1);
           System.out.println("controller " + controller);
           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS)); */
           
           //window title
           /*	final String eventAnalysisSummaryWindowTitle = controller
           			+ " - Controller GSM Call Failure : Event Analysis Summary";

           	assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisSummaryWindowTitle,
           			selenium.isTextPresent(eventAnalysisSummaryWindowTitle)); */

      /*     assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                   dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                           GuiStringConstants.CONTROLLER, controller)); 

       } */

     
       /**
        * GSMCFR-172 Summary of extended cause by Cause Grouping and Cell
        * 
        * As a user I want to see a summary of GSM RAN Call Drops, number of impacted subscribers experienced by the selected 
        * Cell and the dropped call failure ratio so that I can further understand the proportion of dropped calls for the selected cell.
        * 
        * @throws NoDataException
        * @throws PopUpException
        * @throws InterruptedException
        * @throws ParseException
        * 
        */
     /*  @Test
       public void summaryByCellGSMCFR172() throws PopUpException, NoDataException, InterruptedException, ParseException {
           subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

           final String accessArea = imsiSubscriberGSMCallFailure.getTableData(0, 16);
           System.out.println("Access Area : " + accessArea);
           imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);

           assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                   .containsAll(GSMConstants.DEFAULT_ACCESS_AREA_EVENT_ANALYSIS_SUMMARY));

           final String eventAnalysisWindowTitle = accessArea + " - Access Area GSM Call Failure : Event Analysis Summary";

           assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
                   selenium.isTextPresent(eventAnalysisWindowTitle));

           assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                   dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                           GuiStringConstants.ACCESS_AREA, accessArea));
       } */
    
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

 /*   @Test
    public void imsiGroupRANGSMCFA_7_4() throws InterruptedException, PopUpException, NoDataException {

        subscriberTab.openEventAnalysisWindowForGSMCFA_PS(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        selenium.waitForPageLoadingToComplete();
        System.out.println(imsiGroupSubscriberGSMConnectionFailure.getTableHeaders());
        System.out.println(IMSIEventAnalysisDetailedGSMCFAPSWindow);

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

    } */

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

 /*   @Test
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

    } */

    /**
     * Requirement: Test Case: 4.7.1 Description: To verify that it is possible
     * to view a detailed analysis of Call Drop failure events for a nominated
     * IMSI Group
     * 
     * @throws PopUpException, NoDataException, InterruptedException
     */
/*  @Test
    public void imsiGroupEventAnalysisRANGSMCFA_7_1() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI_GROUP, true,
                GSMConstants.REGRESSION_IMSI_GROUP_NAME);

        imsiGroupSubscriberCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        imsiGroupSubscriberCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(
                GuiStringConstants.ERROR_LOADING + GSMConstants.DETAILS_IMSI_GROUP_EVENT_ANALYSIS_WINDOW_TITLE,
                imsiGroupSubscriberCallFailure.getWindowHeaderLabel().equals(
                        GSMConstants.DETAILS_IMSI_GROUP_EVENT_ANALYSIS_WINDOW_TITLE));

        System.out.println(imsiGroupSubscriberCallFailure.getTableHeaders());
        System.out.println(imsiGroupDetailedEventAnalysisGSMCFAWindow);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiGroupSubscriberCallFailure.getTableHeaders()
                .containsAll(imsiGroupDetailedEventAnalysisGSMCFAWindow));

        assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
                imsiGroupSubscriberCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

        final List<Map<String, String>> allWindowData = imsiGroupSubscriberCallFailure.getAllTableData();
        final List<Map<String, String>> reserveDataList = dataIntegrValidatorObjCS.reserveDataList;

        for (int rowNo = 0; rowNo < allWindowData.size(); rowNo++) {
            final Map<String, String> rowToCheck = imsiGroupSubscriberCallFailure.getAllDataAtTableRow(rowNo);
            for (final Map<String, String> oneRecord : reserveDataList) {

                if (rowToCheck.get(GuiStringConstants.ACCESS_AREA)
                        .equals(oneRecord.get(GuiStringConstants.ACCESS_AREA))) {

                    assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                            dataIntegrValidatorObjCS.validateDrillDownAllTimeRangesForGroupsDetailedView(
                                    imsiGroupSubscriberCallFailure, GuiStringConstants.ACCESS_AREA,
                                    rowToCheck.get(GuiStringConstants.ACCESS_AREA), rowNo));
                }
            }
        }
    }  */

    /**
     * Requirement: Test Case: 4.7.2 Description: To verify that it is possible
     * to view a summary of Call Failure events for a nominated subscriber
     * (IMSI).
     * 
     * @throws PopUpException, NoDataException, InterruptedException
     */

/*  @Test
    public void imsiEventAnalysisRANGSMCFA_7_2() throws PopUpException, NoDataException, InterruptedException {
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        //selenium.waitForPageLoadingToComplete();
        System.out.println(imsiSubscriberGSMCallFailure.getTableHeaders());
        System.out.println(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_WITH_IMSI_GSM_CFA_WINDOW_HEADERS);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_WITH_IMSI_GSM_CFA_WINDOW_HEADERS));

        final String eventAnalysisWindowTitle = "Event Analysis Summary (GSM Call Failure)";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisWindowTitle,
                selenium.isTextPresent(eventAnalysisWindowTitle));

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.IMSI, GSMConstants.IMSI_NUMBER));

    } */

/*    @Test
    public void imsiEventRankingRANGSMCFAByCallDrops_7_3() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        // opening menus
        final List<SubscriberTab.SubStartMenu> SubMenuList = new ArrayList<SubscriberTab.SubStartMenu>();

        SubMenuList.add(SubscriberTab.SubStartMenu.EVENT_GROUP);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_CFA);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM_CALL_DROPS);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, SubMenuList);
        // opening menus end
        // selenium.waitForPageLoadingToComplete();
        selenium.waitForPageLoadingToComplete();

        // Check headers (pre drill)
        assertTrue(
                FailureReasonStringConstants.HEADER_MISMATCH,
                subscriberRankingGSMCallFailureAnalysisByCallDrops.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_SUMMARY_SUBSCRIBER_RANKING_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // Drill on IMSI
        subscriberRankingGSMCallFailureAnalysisByCallDrops.clickTableCell(0, GuiStringConstants.IMSI);

        // Check headers (post drill)
        assertTrue(
                FailureReasonStringConstants.HEADER_MISMATCH,
                subscriberRankingGSMCallFailureAnalysisByCallDrops.getTableHeaders().containsAll(
                        GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        subscriberRankingGSMCallFailureAnalysisByCallDrops.setTimeRange(TimeRange.ONE_DAY);
        selenium.waitForPageLoadingToComplete();

        assertTrue(FailureReasonStringConstants.TEMPORAL_REFERENCE_DIFFERENT_THAN_EXPECTED,
                subscriberRankingGSMCallFailureAnalysisByCallDrops.getTimeRange().equals(TimeRange.ONE_DAY.getLabel()));

        final TimeRange[] timeRanges = getAllTimeRanges();

        for (final TimeRange timeRange : timeRanges) {
            assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                    getDataAndCompare(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));

            assertTrue(FailureReasonStringConstants.INVALID_EVENT_TIME_IN_GUI_DATA,
                    checkEventTimesInGuiAreValid(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));
        }
    } */

    //fot testing only
 /*   @Test
    public void test234() throws PopUpException, NoDataException, InterruptedException, ParseException {
        // opening menus
        final List<SubscriberTab.SubStartMenu> SubMenuList = new ArrayList<SubscriberTab.SubStartMenu>();

        SubMenuList.add(SubscriberTab.SubStartMenu.EVENT_GROUP);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_CFA);
        SubMenuList.add(SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_GSM_CALL_DROPS);

        subscriberTab.openSubMenusFromStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS, SubMenuList);
        // opening menus end
        // selenium.waitForPageLoadingToComplete();
        selenium.waitForPageLoadingToComplete();

        // Check headers (pre drill)

        // Drill on IMSI
        subscriberRankingGSMCallFailureAnalysisByCallDrops.clickTableCell(0, GuiStringConstants.IMSI);

        // Check headers (post drill)

        subscriberRankingGSMCallFailureAnalysisByCallDrops.setTimeRange(TimeRange.ONE_DAY);
        selenium.waitForPageLoadingToComplete();

        assertTrue(FailureReasonStringConstants.TEMPORAL_REFERENCE_DIFFERENT_THAN_EXPECTED,
                subscriberRankingGSMCallFailureAnalysisByCallDrops.getTimeRange().equals(TimeRange.ONE_DAY.getLabel()));

        final TimeRange[] timeRanges = getAllTimeRanges();

        for (final TimeRange timeRange : timeRanges) {
            assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                    getDataAndCompare(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));

            assertTrue(FailureReasonStringConstants.INVALID_EVENT_TIME_IN_GUI_DATA,
                    checkEventTimesInGuiAreValid(subscriberRankingGSMCallFailureAnalysisByCallDrops, timeRange));
        }
    } */
    
    /*  @Test
    public void GSMCFR158() throws PopUpException, NoDataException, InterruptedException, ParseException {

        // Code to open event analysis and drill to detailed failed event
        // analysis by imsi
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
        // validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
        // GuiStringConstants.CONTROLLER, controller));
        // TODO TC has now been modified to take account of extra columns ,
        // integrity will now also need to be altered to take account of failure
        // ratio column,
        // datagen csv may also need modification
    } */

 /*   @Test
    public void subscriberRankingFailureRatio_GSMCFR216() throws PopUpException, NoDataException, InterruptedException,
            ParseException {

        // Code to open Subscriber Ranking Failure Ratio 
        // analysis by imsi

        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        final String imsi = imsiSubscriberGSMCallFailure.getTableData(0, 3);

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.IMSI, imsi));
    } */

 /*   @Test
    public void GSMCFR159() throws PopUpException, NoDataException, InterruptedException, ParseException {
        // Code to open event analysis and drill to detailed failed event
        // analysis by imsi
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        //final String eventAnalysisDetailWindowTitle = "Subscriber GSM Call Failure : Event Analysis Detail";
        final String eventAnalysisDetailWindowTitle = GSMConstants.IMSI_NUMBER
                + " - Call Drops - Event Analysis Summary (GSM Call Failure)";

        assertTrue(GuiStringConstants.ERROR_LOADING + eventAnalysisDetailWindowTitle,
                selenium.isTextPresent(eventAnalysisDetailWindowTitle));

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                validateEventAnalysisSummary(imsiSubscriberGSMCallFailure));
    } */

    /*   @Test
    public void subscriberDetailedExtendedCauseByCauseGroupAndCell_GSMCFR400() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        // Code to open event analysis and drill to detailed failed event analysis by Cause Group and Cell

        // IMSI Summary
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // IMSI Failure detailed analysis
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // Cell summary with failure ratio
        final String cell = imsiSubscriberGSMCallFailure.getTableData(0, 15);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS));

        // Cause Group summary by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // Summary of Extended Cause code within a Cause Group by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // detailed analysis of Extended Cause code within a Cause Group by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // Validate for all time ranges
        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.ACCESS_AREA, cell));
    } */
    
 /*   @Test
    public void causeGrpsSummaryforSelectedBSC_GSMCFR226() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        // Code to open event analysis and drill to summary failed event
        // analysis for a selected IMSI

        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        final String controller = imsiSubscriberGSMCallFailure.getTableData(0, 14);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CONTROLLER);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.CONTROLLER, controller)); 
    } */

/*    @Test
    public void causeGrpsSummaryforSelectedCell_GSMCFR425() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        //Code to open event analysis and drill to summary failed event analysis by imsi and by cause groups for the selected Cell.

        // IMSI Summary
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // IMSI Failure detailed analysis
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));
        final String cell = imsiSubscriberGSMCallFailure.getTableData(0, 15);
        System.out.println("cell " + cell);

        // Cell summary with failure ratio
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS));

        // Cause Group summary by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // Summary of Extended Cause code within a Cause Group by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // Validate for all time ranges
        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.ACCESS_AREA, cell)); 
    } */

 /*   @Test
    public void subscriberDetailedExtendedCauseByCauseGroupAndCell_GSMCFR400() throws PopUpException, NoDataException,
            InterruptedException, ParseException {
        // Code to open event analysis and drill to detailed failed event analysis by Cause Group and Cell

        // IMSI Summary
        subscriberTab.openEventAnalysisWindowForGSMCFA(ImsiMenu.IMSI, true, GSMConstants.IMSI_NUMBER);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // IMSI Failure detailed analysis
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // Cell summary with failure ratio
        final String cell = imsiSubscriberGSMCallFailure.getTableData(0, 15);
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.ACCESS_AREA);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS));

        // Cause Group summary by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // Summary of Extended Cause code within a Cause Group by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS));

        // detailed analysis of Extended Cause code within a Cause Group by Cell
        imsiSubscriberGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, imsiSubscriberGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS));

        // Validate for all time ranges
        assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
                dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(imsiSubscriberGSMCallFailure,
                        GuiStringConstants.ACCESS_AREA, cell));
    } */
 

    
    
// END
    
    private boolean checkIMSIGroupMembership(final CommonWindow commonWindow) throws NoDataException {
        final List<Map<String, String>> allWindowData = commonWindow.getAllTableData();
        final Set<String> imsisFromGUI = new HashSet<String>();
        final Set<String> imsisFromGSMConstants = new HashSet<String>();

        for (final String GSMConstantsData : GSMConstants.IMSI_GROUP_MEMBER_NAMES) {
            imsisFromGSMConstants.add(GSMConstantsData);
        }

        for (final Map<String, String> GUIData : allWindowData) {
            imsisFromGUI.add(GUIData.get(GSMConstants.IMSI));
        }

        return imsisFromGSMConstants.containsAll(imsisFromGUI);
    }

    private boolean validateEventAnalysisSummary(final CommonWindow commonWindow) throws PopUpException,
            NoDataException {

        final TimeRange[] timeRanges = getAllTimeRanges();
        List<Map<String, String>> detailedEventAnalysisTableData = new ArrayList<Map<String, String>>();
        detailedEventAnalysisTableData = commonWindow.getAllTableData();
        final List<Map<String, String>> reserveDataList = dataIntegrValidatorObjCS.reserveDataList;

        for (final TimeRange timeRange : timeRanges) {
            commonWindow.setTimeRange(timeRange);

            for (final Map<String, String> GUIData : detailedEventAnalysisTableData) {
                System.out.println(GUIData);
                for (final Map<String, String> oneRecord : reserveDataList) {
                    if (oneRecord.get(GSMConstants.IMSI).equals(GSMConstants.IMSI_NUMBER))

                    {
                        final Integer releaseType = Integer.parseInt(oneRecord.get(GSMConstants.RELEASE_TYPE_ID));
                        final Integer urgencyCondition = Integer.parseInt(oneRecord
                                .get(GSMConstants.URGENCY_CONDITION_ID));
                        final Integer extendedCause = Integer.parseInt(oneRecord.get(GSMConstants.EXTENDED_CAUSE_ID));
                        final String eventName = oneRecord.get(GSMConstants.EVENT_NAME);

                        if (!(releaseType == 0 && urgencyCondition == 2 && (extendedCause == 2 || extendedCause == 1))
                                && eventName.equals(GSMConstants.CS_CALL_RELEASE)) {
                            final String tacFromGui = GUIData.get(GSMConstants.TAC);
                            final String terminalMakeFromGui = GUIData.get(GSMConstants.TERMINAL_MAKE);
                            final String terminalModelFromGui = GUIData.get(GSMConstants.TERMINAL_MODEL);
                            final String eventTypeFromGui = GUIData.get(GSMConstants.EVENT_TYPE);
                            final String releaseTypeFromGui = GUIData.get(GuiStringConstants.RELEASE_TYPE);
                            final String causeGroupFromGui = GUIData.get(GSMConstants.CAUSE_GROUP);
                            final String extendedCauseValueFromGui = GUIData.get(GSMConstants.EXTENDED_CAUSE_VALUE);
                            final String afCauseValueFromGui = GUIData
                                    .get(GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE);
                            final String vamosNeighborIndicatorFromGui = GUIData
                                    .get(GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR);
                            final String RSAIFromGui = GUIData.get(GuiStringConstants.RSAI);
                            final String channelTypeFromGui = GUIData.get(GuiStringConstants.CHANNEL_TYPE);
                            final String urgencyConditionFromGui = GUIData.get(GuiStringConstants.URGENCY_CONDITION);
                            final String ranVendorFromGui = GUIData.get(GuiStringConstants.RAN_VENDOR);

                            final String tacFromReserveData = oneRecord.get(GSMConstants.TAC);
                            if (!(tacFromReserveData.equals(tacFromGui))) {
                                return false;
                            }

                            final String terminalMakeFromReserveData = oneRecord.get(GSMConstants.TERMINAL_MAKE);
                            if (!(terminalMakeFromReserveData.equals(terminalMakeFromGui))) {
                                return false;
                            }

                            final String terminalModelFromReserveData = oneRecord.get(GSMConstants.TERMINAL_MODEL);
                            if (!(terminalModelFromReserveData.equals(terminalModelFromGui))) {
                                return false;
                            }

                            final String eventTypeFromReserveData = oneRecord.get(GSMConstants.EVENT_TYPE);
                            if (!(eventTypeFromReserveData.equals(eventTypeFromGui))) {
                                return false;
                            }

                            final String releaseTypeFromReserveData = oneRecord.get(GSMConstants.RELEASE_TYPE);
                            System.out.println(releaseTypeFromReserveData.equals(releaseTypeFromGui));
                            if (!(releaseTypeFromReserveData.equals(releaseTypeFromGui))) {
                                return false;
                            }

                            final String causeGroupFromReserveData = oneRecord.get(GSMConstants.CAUSE_GROUP);
                            System.out.println(causeGroupFromReserveData.equals(causeGroupFromGui));
                            if (!(causeGroupFromReserveData.equals(causeGroupFromGui))) {
                                return false;
                            }

                            final String extendedCauseValueFromReserveData = oneRecord.get(GSMConstants.EXTENDED_CAUSE);
                            System.out.println(extendedCauseValueFromReserveData.equals(extendedCauseValueFromGui));
                            if (!(extendedCauseValueFromReserveData.equals(extendedCauseValueFromGui))) {
                                return false;
                            }

                            final String afCauseValueFromReserveData = oneRecord
                                    .get(GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE);
                            System.out.println(extendedCauseValueFromReserveData.equals(extendedCauseValueFromGui));
                            if (afCauseValueFromGui != null
                                    && !(afCauseValueFromReserveData.equals(afCauseValueFromGui))) {
                                return false;
                            }

                            final String vamosNeighborIndicatorFromReserveData = oneRecord
                                    .get(GSMConstants.VAMOS_NEIGHBOR_INDICATOR);
                            System.out.println(vamosNeighborIndicatorFromReserveData
                                    .equals(vamosNeighborIndicatorFromGui));
                            if (!(vamosNeighborIndicatorFromReserveData.equals(vamosNeighborIndicatorFromGui))) {
                                return false;
                            }
                            final String RSAIFromReserveData = oneRecord.get(GSMConstants.RSAI);
                            System.out.println(RSAIFromReserveData.equals(RSAIFromGui));
                            if (!(RSAIFromReserveData.equals(RSAIFromGui))) {
                                return false;
                            }

                            final String channelTypeFromReserveData = oneRecord.get(GSMConstants.CHANNEL_TYPE);
                            System.out.println(channelTypeFromReserveData.equals(channelTypeFromGui));
                            if (!(channelTypeFromReserveData.equals(channelTypeFromGui))) {
                                return false;
                            }

                            final String urgencyConditionFromReserveData = oneRecord
                                    .get(GSMConstants.URGENCY_CONDITION);
                            if (!(urgencyConditionFromReserveData.equals(urgencyConditionFromGui))) {
                                return false;
                            }

                            final String ranVendorFromReserveData = oneRecord.get(GSMConstants.RAN_VENDOR);
                            System.out.println(ranVendorFromReserveData.equals(ranVendorFromGui));
                            if (!(ranVendorFromReserveData.equals(ranVendorFromGui))) {
                                return false;
                            }

                            final String controllerFromGui = GUIData.get(GuiStringConstants.CONTROLLER);
                            final String controllerFromReserveData = oneRecord.get(GuiStringConstants.CONTROLLER);
                            final String accessAreaFromGui = GUIData.get(GuiStringConstants.ACCESS_AREA);
                            final String accessAreaFromReserveData = oneRecord.get(GuiStringConstants.ACCESS_AREA);

                            if (!(controllerFromReserveData.equals(controllerFromGui))) {
                                return false;
                            }

                            if (!(accessAreaFromReserveData.equals(accessAreaFromGui))) {
                                return false;
                            }
                        }
                    }
                }

            }
        }

        return true;
    }

}
