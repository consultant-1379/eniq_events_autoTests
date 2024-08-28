package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * @author ewandaf
 * @since 2011
 *
 */

public class SubscriberSessionTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberEventAnalysisForMSS")
    private CommonWindow subscriberEventAnalysis;
    
    //MSS Enhancement

    final List<String> defaultHeadersOnIMSIFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.MSC,GuiStringConstants.EXTERNAL_PROTOCOL,GuiStringConstants.INCOMING_ROUTE,
            GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.CONTROLLER,GuiStringConstants.ACCESS_AREA,GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE,GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.EXTERNAL_CAUSE_VALUE,GuiStringConstants.INTERNAL_CAUSE_CODE,
             GuiStringConstants.FAULT_CODE));

    final List<String> defaultHeadersOnRoamingCallForwardFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.MSC,GuiStringConstants.INCOMING_ROUTE,
            GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.TAC,GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL,GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE
            ));
    final List<String> defaultHeadersOnCallForwardFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.MSC,GuiStringConstants.INCOMING_ROUTE,
            GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE
            ));

    final List<String> defaultHeadersOnSMSEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.SMS_RESULT, GuiStringConstants.MSC, GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.MCC,
            GuiStringConstants.MNC));

    final List<String> defaultHeadersOnIMSIEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO));

    final List<String> expectedHeadersOnIMSIGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList
    		(GuiStringConstants.EVENT_TYPE,GuiStringConstants.FAILURES,GuiStringConstants.SUCCESSES,
    		GuiStringConstants.TOTAL, GuiStringConstants.SUCCESS_RATIO, 
             GuiStringConstants.IMPACTED_SUBSCRIBERS ));

    final List<String> defaultHeadersOnSelectedTAC = new ArrayList<String>(Arrays.asList(GuiStringConstants.TAC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedAccessArea = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedMSC = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES,
            GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> defaultHeadersOnSelectedController = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR,GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS, GuiStringConstants.SUCCESS_RATIO,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final List<String> optionalHeadersToBeChecked = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSISDN,
            GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,GuiStringConstants.EXTERNAL_CAUSE_CODE,
            GuiStringConstants.INTERNAL_CAUSE_CODE_ID, GuiStringConstants.FAULT_CODE_ID,GuiStringConstants.RECOMMENDED_ACTION,
            GuiStringConstants.INTERNAL_LOCATION_CODE, GuiStringConstants.BEARER_SERVICE_CODE,
            GuiStringConstants.TELE_SERVICE_CODE, GuiStringConstants.RAT,GuiStringConstants.RAN_VENDOR, GuiStringConstants.CALL_ID_NUMBER,
            GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER, GuiStringConstants.CALLING_PARTY_NUMBER,
            GuiStringConstants.CALLED_PARTY_NUMBER, GuiStringConstants.CALLING_SUBS_IMSI,
            GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI,
            GuiStringConstants.CALLED_SUBS_IMEI, GuiStringConstants.MS_ROAMING_NUMBER,
            GuiStringConstants.DISCONNECTING_PARTY, GuiStringConstants.CALL_DURATION, GuiStringConstants.SEIZURE_TIME,
            GuiStringConstants.ORIGINAL_CALLED_NUMBER, GuiStringConstants.REDIRECT_NUMBER,
            GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI, GuiStringConstants.REDIRECT_SPN,
            GuiStringConstants.CALL_POSITION, GuiStringConstants.EOS_INFO, GuiStringConstants.RECORD_SEQUENCE_NUMBER,
            GuiStringConstants.NETWORK_CALL_REFERENCE,GuiStringConstants.MCC,GuiStringConstants.MNC, GuiStringConstants.RAC, GuiStringConstants.LAC));

   
    final List<String> allHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME,
    		GuiStringConstants.MSC,GuiStringConstants.EXTERNAL_PROTOCOL,GuiStringConstants.INCOMING_ROUTE,
            GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.CONTROLLER,GuiStringConstants.ACCESS_AREA,GuiStringConstants.TAC,
            GuiStringConstants.MSISDN,GuiStringConstants.IMSI,
             GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,GuiStringConstants.EXTERNAL_CAUSE_CODE,GuiStringConstants.EXTERNAL_CAUSE_VALUE,
            GuiStringConstants.INTERNAL_CAUSE_CODE_ID,GuiStringConstants.INTERNAL_CAUSE_CODE,GuiStringConstants.FAULT_CODE_ID,
            GuiStringConstants.FAULT_CODE, GuiStringConstants.RECOMMENDED_ACTION, 
            GuiStringConstants.INTERNAL_LOCATION_CODE, GuiStringConstants.BEARER_SERVICE_CODE,
            GuiStringConstants.TELE_SERVICE_CODE, GuiStringConstants.RAT,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CALL_ID_NUMBER,
            GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER, GuiStringConstants.CALLING_PARTY_NUMBER,
            GuiStringConstants.CALLED_PARTY_NUMBER, GuiStringConstants.CALLING_SUBS_IMSI,
            GuiStringConstants.CALLED_SUBS_IMSI, GuiStringConstants.CALLING_SUBS_IMEI,
            GuiStringConstants.CALLED_SUBS_IMEI, GuiStringConstants.MS_ROAMING_NUMBER,
            GuiStringConstants.DISCONNECTING_PARTY, GuiStringConstants.CALL_DURATION, GuiStringConstants.SEIZURE_TIME,
            GuiStringConstants.ORIGINAL_CALLED_NUMBER, GuiStringConstants.REDIRECT_NUMBER,
            GuiStringConstants.REDIRECT_COUNTER, GuiStringConstants.REDIRECT_IMSI, GuiStringConstants.REDIRECT_SPN,
            GuiStringConstants.CALL_POSITION, GuiStringConstants.EOS_INFO, GuiStringConstants.RECORD_SEQUENCE_NUMBER,
            GuiStringConstants.NETWORK_CALL_REFERENCE,GuiStringConstants.MCC,GuiStringConstants.MNC, GuiStringConstants.RAC, GuiStringConstants.LAC));

    final List<String> defaultHeadersOnMSISDN = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.SUCCESSES, GuiStringConstants.TOTAL_EVENTS,
            GuiStringConstants.SUCCESS_RATIO));

    final List<String> defaultHeadersOnMSISDNClickedFailure = new ArrayList<String>(Arrays.asList(
    		 GuiStringConstants.EVENT_TIME, GuiStringConstants.MSC,GuiStringConstants.EXTERNAL_PROTOCOL,GuiStringConstants.INCOMING_ROUTE,
             GuiStringConstants.OUTGOING_ROUTE,GuiStringConstants.CONTROLLER,GuiStringConstants.ACCESS_AREA,GuiStringConstants.TAC,
             GuiStringConstants.TERMINAL_MAKE,GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_RESULT,
             GuiStringConstants.EXTERNAL_CAUSE_VALUE,GuiStringConstants.INTERNAL_CAUSE_CODE,
              GuiStringConstants.FAULT_CODE));

    final List<String> eventType = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSORIGINATINGSMSINMSC,
            GuiStringConstants.MSTERMINATINGSMSINMSC, GuiStringConstants.MSTERMINATING,
            GuiStringConstants.ROAMINGCALLFORWARDING, GuiStringConstants.CALLFORWARDING,
            GuiStringConstants.MSORIGINATING, GuiStringConstants.LOCATIONSERVICES));

    final List<String> defaultHeadersOnIMSICallForwardFailedEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
            GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE,
            GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, GuiStringConstants.MCC,
            GuiStringConstants.MNC));

    final List<String> defaultHeadersOnIMSIroamingCallForwardFailedEventAnalysisWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_RESULT,
                    GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.FAULT_CODE,
                    GuiStringConstants.RECOMMENDED_ACTION, GuiStringConstants.MSC, GuiStringConstants.MCC,
                    GuiStringConstants.MNC));

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberSession test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberSession test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        selenium.waitForElementToBePresent("//table[@id='selenium_tag_MetaDataChangeComponent']","50000");
        if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
            selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
            selenium.waitForElementToBePresent("//a[@id='selenium_tag_Core - Voice']", "50000");
            if (selenium.isElementPresent("//a[@id='selenium_tag_Core - Voice']")) {
                selenium.click("//a[@id='selenium_tag_Core - Voice']");
            }else{
                logger.log(Level.SEVERE, "Element //a[@id='selenium_tag_Core - Voice'] is not present");
            }
        }else{
            logger.log(Level.SEVERE, "Element //table[@id='selenium_tag_MetaDataChangeComponent'] is not present");
        }
        pause(2000);
    }

    /**
     *  105 65-0528/00967
     *         MSOriginating event can be displayed in CS event subscriber session.
     *          Test Case - 4.5.1
     *          MSS Enhancement
     */
    // sync pass
    @Test
    public void MSOriginatingEventCanBeDisplayedInSubscriberSession_5_1() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        //final String imsi = "460000987654322";
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, "mSOriginating");
        assertTrue("The table header is not right on click the failures", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnIMSIFailedEventAnalysisWindow));
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked,
                GuiStringConstants.EVENT_TYPE);

    }

    /**
     *  105 65-0528/00967
     *          To verify the MSOriginating event information on UI is correct comparing with raw data.
     *          Test Case - 4.5.2
     */
    //sync pass
    @Test
    public void MSOriginatingEventInformationIntegrity_5_2() throws Exception {
        //                      String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        waitForPageLoadingToComplete();
        assertTrue("The event type is not right",
                subscriberEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).containsAll(eventType));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        assertTrue("The table header is not right on click the failures", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnIMSIFailedEventAnalysisWindow));
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked,
                GuiStringConstants.EVENT_TYPE);
        waitForPageLoadingToComplete();
        
        assertTrue("The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(allHeaders));
        
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi,
                AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, allHeaders, GuiStringConstants.IMSI,
                        imsi, GuiStringConstants.MSORIGINATING));
                        

    }

    /**
     *  105 65-0528/00968
     *          MSTerminating event can be displayed in CS event subscriber session.
     *          Test Case - 4.5.3
     */
    // sync pass
    @Test
    public void MSTerminatingEventCanBeDisplayedInCSEventsSubscriberSession_5_3() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        //final String imsi = "460000987654322";
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSTERMINATING);
        assertTrue("The table header is not right on click the failures", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnIMSIFailedEventAnalysisWindow));
    }

    /**
     *  105 65-0528/00968
     *          To verify the MSTerminating event information on UI is correct comparing with raw data.
     *          Test Case - 4.5.4
     */
    //sync pass
    @Test
    public void MSTerminatingEventInformationIntegrity_5_4() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        assertTrue("The event type is not right",
                subscriberEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).containsAll(eventType));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSTERMINATING);
        waitForPageLoadingToComplete();
        assertTrue("The table header is not right on click the failures", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnIMSIFailedEventAnalysisWindow));
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked,
                GuiStringConstants.EVENT_TYPE);
         assertTrue("The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(allHeaders));
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi,
                AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, allHeaders, GuiStringConstants.IMSI,
                        imsi, GuiStringConstants.MSTERMINATING));
    }

    /**
     *  105 65-0528/00985
     *          Verify IMSI support for Subscriber analysis.
     *          Test Case - 4.5.5
     */
    @Test
    public void VerifyIMSIAndIMSIGroupSupportForSubscriberAnalysis_5_5_1() throws Exception {

        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));

    }

    /**
     *  105 65-0528/00985
     *          Verify IMSI group support for Subscriber analysis.
     *          Test Case - 4.5.5
     *          Integrity Error
     */
    // cannot be run yet no group will be fixed later sync
    @Test
    public void VerifyIMSIAndIMSIGroupSupportForSubscriberAnalysis_5_5_2() throws Exception {
        final String imsiGroup = reservedDataHelper.getCommonReservedData(CommonDataType.IMSI_GROUP_MSS);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI_GROUP, true, imsiGroup);
        assertTrue("The table header is not right on IMSI group", subscriberEventAnalysis.getTableHeaders()
                .containsAll(expectedHeadersOnIMSIGroupEventAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        delayAndSetTimeRange(timeRange);
        
        final List<String> imsiGroupList = new ArrayList<String>(Arrays.asList(reservedDataHelper
                .getCommonReservedData(CommonDataType.IMSI_GROUP_DATA_MSS).split(",")));
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + imsiGroup,
                AggregrationHandlerUtil.eventAnalysisForGroups(dataDisplayedOnUI, eventType, GuiStringConstants.IMSI,
                        imsiGroupList, "NOT_SPECIFIC", expectedHeadersOnIMSIGroupEventAnalysisWindow,
                        timeRange.getMiniutes()));
    }

    /**
     *  105 65-0528/00987
     *          Verify IMSI and IMSI group support for Subscriber analysis.
     *          Test Case - 4.5.6
     *          Integrity Error
     */
    // sync pass
    @Test
    public void VerifyMSCDrillDwonFunction_5_6() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        final TimeRange timeRange = getTimeRangeFromProperties();
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        //subscriberEventAnalysis.setTimeRange(timeRange);
        delayAndSetTimeRange(timeRange);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String msc = firstRowData.get(GuiStringConstants.MSC);
        drillDownColumnAtFirstRow(subscriberEventAnalysis, GuiStringConstants.MSC, GuiStringConstants.MSC);
        assertTrue("The header is not right on select MSC",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnSelectedMSC));
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + msc, AggregrationHandlerUtil.eventAnalysis(
                dataDisplayedOnUI, eventType, GuiStringConstants.MSC, msc, "NOT_SPECIFIC", defaultHeadersOnSelectedMSC,
                timeRange.getMiniutes()));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<Map<String, String>> mssMSOriginatingdataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + msc,
                AggregrationHandlerUtil.getAggregrationResult(mssMSOriginatingdataDisplayedOnUI,
                        defaultHeadersOnIMSIFailedEventAnalysisWindow, GuiStringConstants.MSC, msc,
                        GuiStringConstants.MSORIGINATING));
    }

    /**
     *  105 65-0528/00988
     *          Verify event list it shall be possible to drill-down into Access Area leve summary information.
     *          Test Case - 4.5.7
     *          Integrity Error
     */
    // sync pass
    @Test
    public void VerifyAccessAreaDrillDwonFunction_5_7() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        final TimeRange timeRange = getTimeRangeFromProperties();
        //subscriberEventAnalysis.setTimeRange(timeRange);
        delayAndSetTimeRange(timeRange);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String accessArea = firstRowData.get(GuiStringConstants.ACCESS_AREA);
        drillDownColumnAtFirstRow(subscriberEventAnalysis, GuiStringConstants.ACCESS_AREA,
                GuiStringConstants.ACCESS_AREA);
        assertTrue("The header is not right on select Access Area", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnSelectedAccessArea));
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + accessArea,
                AggregrationHandlerUtil.eventAnalysis(dataDisplayedOnUI, eventType, GuiStringConstants.ACCESS_AREA,
                        accessArea, "NOT_SPECIFIC", defaultHeadersOnSelectedAccessArea, timeRange.getMiniutes()));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<Map<String, String>> accessAreaEventSpecificdataDisplayedOnUI = subscriberEventAnalysis
                .getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + accessArea,
                AggregrationHandlerUtil.getAggregrationResult(accessAreaEventSpecificdataDisplayedOnUI,
                        defaultHeadersOnIMSIFailedEventAnalysisWindow, GuiStringConstants.ACCESS_AREA, accessArea,
                        GuiStringConstants.MSORIGINATING));
    }

    /**
     *  105 65-0528/00986
     *          Verify event list it shall be possible to drill-down into TAC leve summary information.
     *          Test Case - 4.5.8
     *          Integrity Error
     */
    // sync pass
    @Test
    public void VerifyTACDrillDwonFunction_5_8() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        //subscriberEventAnalysis.setTimeRange(timeRange);
        delayAndSetTimeRange(timeRange);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String tac = firstRowData.get(GuiStringConstants.TAC);
        openEventAnalysis(subscriberEventAnalysis, "TAC", GuiStringConstants.TAC);
        assertTrue("The header is not right on select TAC",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnSelectedTAC));
        
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS + tac, AggregrationHandlerUtil.eventAnalysis(
                dataDisplayedOnUI, eventType, GuiStringConstants.TAC, tac, "NOT_SPECIFIC", defaultHeadersOnSelectedTAC,
                timeRange.getMiniutes()));
        
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final List<Map<String, String>> tacEventSpecificdataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + tac,
                AggregrationHandlerUtil.getAggregrationResult(tacEventSpecificdataDisplayedOnUI,
                        defaultHeadersOnIMSIFailedEventAnalysisWindow, GuiStringConstants.TAC, tac,
                        GuiStringConstants.MSORIGINATING));
    }

    /**
     *  105 65-0528/00989
     *          Verify event list it shall be possible to drill-down into controller level summary information.
     *          Test Case - 4.5.9
     *         Integrity Error
     */
    // sync pass
    @Test
    public void VerifyControllerDrillDwonFunction_5_9() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        final TimeRange timeRange = getTimeRangeFromProperties();
        //subscriberEventAnalysis.setTimeRange(timeRange);
        delayAndSetTimeRange(timeRange);
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final Map<String, String> firstRowData = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final String controller = firstRowData.get(GuiStringConstants.CONTROLLER);
        openEventAnalysis(subscriberEventAnalysis, "Controller", GuiStringConstants.CONTROLLER);
        assertTrue("The header is not right on select Controller", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnSelectedController));
        
        final List<Map<String, String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + controller,
                AggregrationHandlerUtil.eventAnalysis(dataDisplayedOnUI, eventType, GuiStringConstants.CONTROLLER,
                        controller, "NOT_SPECIFIC", defaultHeadersOnSelectedController, timeRange.getMiniutes()));
                        
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATING);
        final List<Map<String, String>> controllerEventSpecificdataDisplayedOnUI = subscriberEventAnalysis
                .getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + controller,
                AggregrationHandlerUtil.getAggregrationResult(controllerEventSpecificdataDisplayedOnUI,
                        defaultHeadersOnIMSIFailedEventAnalysisWindow, GuiStringConstants.CONTROLLER, controller,
                        GuiStringConstants.MSORIGINATING));
    }

    /**
     *  105 65-0528/00986
     *          Verify event list it shall be possible to drill-down into controller level summary information.
     *          Test Case - 4.5.10
     */
    // sync pass
    @Test
    public void VerifyTimeRangeSupportForSubscriberAnalysis_5_10() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        //final String imsi = "460000987654322";
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        checkWindowUpdatedForTimeRanges("Event Analysis", subscriberEventAnalysis);
    }

    /**
     *  105 65-0528/00986
     *          Verify date and time range support for Subscriber analysis.
     *          Test Case - 4.5.11
     */
    // sync pass
    @Test
    public void VerifyDateAndTimeRangeSupportForSubscriberAnalysis_5_11() throws Exception {
        //              String imsi = "460000123456790";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        setTimeAndDateAndVerify(subscriberEventAnalysis, "2011,8,1", TimeCandidates.AM_00_00, "2011,8,2",
                TimeCandidates.AM_00_00);
    }

    /**
     *  105 65-0528/00971
     *          mSOriginatingSMSinMSC event can be displayed in CS event subscriber session.
     *          Test Case - 4.5.12
     */
    // sync pass
    @Test
    public void mSOriginatingSMSinMSCEventCanBeDisplayedInSubscriberSession_5_12() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        //final String imsi = "460000987654322";
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        assertTrue(
                "No " + GuiStringConstants.MSORIGINATINGSMSINMSC,
                subscriberEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(
                        GuiStringConstants.MSORIGINATINGSMSINMSC));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        //checkWindowUpdatedForTimeRanges("Failed Event Analysis", subscriberEventAnalysis);
        assertTrue("The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnSMSEventAnalysisWindow));
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Analysis", subscriberEventAnalysis, eventType,
                GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATINGSMSINMSC,
                defaultHeadersOnSMSEventAnalysisWindow, DataIntegrityConstants.AGGREGRATION_ANALYSIS);
    }

    /**
     *  105 65-0528/00972
     *          mSTerminatingSMSinMSC event can be displayed in CS event subscriber session.
     *          Test Case - 4.5.13
     */
    // sync
    @Test
    public void mSTerminatingSMSinMSCEventCanBeDisplayedInSubscriberSession_5_13() throws Exception {
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        //final String imsi = "460000987654322";
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The table header is not right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        assertTrue(
                "No mSTerminatingSMSinMSC",
                subscriberEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(
                        GuiStringConstants.MSTERMINATINGSMSINMSC));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        assertTrue("The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnSMSEventAnalysisWindow));
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Analysis", subscriberEventAnalysis, eventType,
                GuiStringConstants.IMSI, imsi, GuiStringConstants.MSTERMINATINGSMSINMSC,
                defaultHeadersOnSMSEventAnalysisWindow, DataIntegrityConstants.AGGREGRATION_ANALYSIS);
    }

    /**
     *  105 65-0528/00969
     *          callForwarding event can be displayed in voice event subscriber session.
     *          Test Case - 4.5.14
     *          MSS Enhancement
     */
    // sync pass
    @Test
    public void callForwardingEventCanBeDisplayedInSubscriberSession_5_14() throws Exception {
        //              String imsi = "460000834549171";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        assertTrue("Default time range is NOT equal to 30 minutes",
                subscriberEventAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        assertTrue("There is no callForwarding event type for this IMSI", subscriberEventAnalysis
                .getAllTableDataAtColumn("Event Type").contains(GuiStringConstants.CALLFORWARDING));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.CALLFORWARDING);
        waitForPageLoadingToComplete();
        assertTrue(
                "The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(
                        defaultHeadersOnCallForwardFailedEventAnalysisWindow));
        //checkWindowUpdatedForTimeRanges("Failed Event Analysis", subscriberEventAnalysis);
        checkWindowUpdatedForTimeRanges("Event Analysis", subscriberEventAnalysis);
    }

    /**
     *  105 65-0528/00970
     *          roamingCallForwarding event can be displayed in voice event subscriber session.
     *          Test Case - 4.5.15
     *          MSS Enhancement
     */
    // sync pass
    @Test
    public void roamingCallForwardingEventCanBeDisplayedInSubscriberSession_5_15() throws Exception {
        //              String imsi = "460000834549171";
        final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        assertTrue("The header is not right on IMSI",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        assertTrue("Default time range is NOT equal to 30 minutes",
                subscriberEventAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        assertTrue(
                "There is no roamingCallForwarding event type for this IMSI",
                subscriberEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.EVENT_TYPE).contains(
                        GuiStringConstants.ROAMINGCALLFORWARDING));
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE,
                GuiStringConstants.ROAMINGCALLFORWARDING);
        waitForPageLoadingToComplete();
        //checkWindowUpdatedForTimeRanges("Failed Event Analysis", subscriberEventAnalysis);
        assertTrue(
                "The all table header is not right ",
                subscriberEventAnalysis.getTableHeaders().containsAll(
                		defaultHeadersOnRoamingCallForwardFailedEventAnalysisWindow));
        checkWindowUpdatedForTimeRangesWithDataIntegrity("Event Analysis", subscriberEventAnalysis, eventType,
                GuiStringConstants.IMSI, imsi, GuiStringConstants.ROAMINGCALLFORWARDING,
                defaultHeadersOnRoamingCallForwardFailedEventAnalysisWindow, DataIntegrityConstants.AGGREGRATION_ANALYSIS);
    }

    /**
     *  
     *          To verify that it is possible to search by MSISDN and view event analysis data for all time intervals.
     *          Test Case - 4.5.16
     *          MSS Enhancement
     */
    // sync no data
    @Test
    public void SearchByMSISDN_5_16() throws Exception {
        final String msisdn = reservedDataHelper.getCommonReservedData(CommonDataType.MSISDN_MSS);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.MSISDN, true, msisdn);
        assertTrue("Default time range is NOT equal to 30 minutes",
                subscriberEventAnalysis.getTimeRange().equals(GuiStringConstants.DEFAULT_TIME_RANGE));
        subscriberEventAnalysis.setTimeRange(getTimeRangeFromProperties());
        assertTrue("The table header isn't right",
                subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnMSISDN));
        
         drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, "mSOriginating");
        assertTrue("The table header isn't right on clicking failure", subscriberEventAnalysis.getTableHeaders()
                .containsAll(defaultHeadersOnMSISDNClickedFailure));
        
        final List<Map<String, String>> msISDNEventSpecificdataDisplayedOnUI = subscriberEventAnalysis
                .getAllPagesData();
        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + msisdn,
                AggregrationHandlerUtil.getAggregrationResult(msISDNEventSpecificdataDisplayedOnUI,
                        defaultHeadersOnIMSIFailedEventAnalysisWindow, GuiStringConstants.MSISDN, msisdn,
                        GuiStringConstants.MSORIGINATING));
        sortAllTableColumn(subscriberEventAnalysis);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Check time range, the time ranges is read from properties file //ewandaf
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */
    private void checkWindowUpdatedForTimeRanges(final String networkType, final CommonWindow commonWindow)
            throws NoDataException, PopUpException {
        assertTrue("Can't load " + networkType + " window", selenium.isTextPresent(networkType));

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
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
            assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog",
                    selenium.isTextPresent("Time Settings"));
        }
    }

    public void checkWindowUpdatedForTimeRangesWithDataIntegrity(final String networkType,
            final CommonWindow commonWindow, final List<String> allEventTypes, final String typeOfAnalysisSelected,
            final String selectedFieldValue, final String eventType, final List<String> defaultHeadersOnSelectedMSC,
            final String ValidationString) throws NoDataException, PopUpException {
        assertTrue("Can't load " + networkType + " window", selenium.isTextPresent(networkType));
        //        assertTrue("Default time range is NOT equal to 30 minutes", commonWindow.getTimeRange().equals("30 minutes"));

        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
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
            final String timeLabel = time.getLabel();
            if (!timeLabel.equalsIgnoreCase("5 minutes")) {
                delayAndSetTimeRange(time);
                waitForPageLoadingToComplete();
                assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog",
                        selenium.isTextPresent("Time Settings"));
                if (!ValidationString.equalsIgnoreCase(DataIntegrityConstants.STATUS_NONE)) {
                    if (ValidationString.equalsIgnoreCase(DataIntegrityConstants.FAILURE_ANALYSIS)) {
                        final List<Map<String, String>> completeUITableValues = commonWindow.getAllTableData();
                    	assertTrue(DataIntegrityConstants.FAILURE_ANALYSIS_ERROR_MESSAGE + selectedFieldValue,
                                AggregrationHandlerUtil.eventAnalysis(completeUITableValues, allEventTypes,
                                        typeOfAnalysisSelected, selectedFieldValue, eventType,
                                        defaultHeadersOnSelectedMSC, time.getMiniutes()));
                    }
                    if (ValidationString.equalsIgnoreCase(DataIntegrityConstants.AGGREGRATION_ANALYSIS)) {
                    	final List<Map<String, String>> completeUITableValues = commonWindow.getAllPagesData();
                        assertTrue(DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + selectedFieldValue,
                                AggregrationHandlerUtil.getAggregrationResult(completeUITableValues,
                                        defaultHeadersOnSelectedMSC, typeOfAnalysisSelected, selectedFieldValue,
                                        eventType));
                    }
                }
            }
        }
    }

    /**
     * Drill down one link for one column at first row.
     * 
     * @param window the object of CommonWindow
     * @param columnName the name of column where the link is
     */
    private void drillDownColumnAtFirstRow(final CommonWindow window, final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        //Level 2
        window.clickTableCell(0, columnName);
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networktype + " Event Analysis page",
                selenium.isTextPresent(networktype + " Event Analysis"));
    }

    /**
     * Drill down one link on Failure column on event analysis. 
     * 
     * @param window the object of CommonWindow
     * @param columnToCheck This column to locate row number
     * @param values These values will compare with the values on "columnToCheck"
     * @throws NoDataException
     * @throws PopUpException
     */
    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck,
            final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Failures");
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, "Failures");
        waitForPageLoadingToComplete();
        //assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Failed Event Analysis"));
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));
    }

    /**
     * Drill down one link to open the event analysis window
     * 
     * @param rankingWindow the object of CommonWindow
     * @param networkType the type of network. e.g. MSC, Controller, Access Area
     * @param columnName the name of column where the link is
     */
    private void openEventAnalysis(final CommonWindow rankingWindow, final String networktype, final String columnName)
            throws NoDataException, PopUpException {
        //Level 2
        rankingWindow.clickTableCell(0, columnName);
        waitForPageLoadingToComplete();
        assertTrue("Can't open " + networktype + " Event Analysis page",
                selenium.isTextPresent(networktype + " Event Analysis"));
    }

    /**
     * Check Ascending and Descending method for each column
     * 
     * @param window the object of CommonWindow
     */
    private void sortAllTableColumn(final CommonWindow window) {
        final List<String> columnNameList = window.getTableHeaders();
        final int length = columnNameList.size();
        for (int i = 0; i < length; i++) {
            final String column = columnNameList.get(i);
            window.sortTable(SortType.ASCENDING, column);
            window.sortTable(SortType.DESCENDING, column);
        }
    }

    /**
     * Set date and time by opening the time dialog window.
     *  
     * @param window the object of CommonWindow
     * @param startDate e.g. "2011, 1, 2", the delimiter is comma
     * @param startTimeCandidate Enum
     * @param endDate e.g. "2011, 1, 2", the delimiter is comma
     * @param endTimeCnadidate Enum
     */
    private void setTimeAndDateAndVerify(final CommonWindow window, final String startDate,
            final TimeCandidates startTimeCandidate, final String endDate, final TimeCandidates endTimeCnadidate)
            throws PopUpException {

        final String[] startDateArray = startDate.split(",");
        final String[] endDateArray = endDate.split(",");
        final Date sDate = window.getDate(Integer.parseInt(startDateArray[0]), Integer.parseInt(startDateArray[1]),
                Integer.parseInt(startDateArray[2]));
        final Date eDate = window.getDate(Integer.parseInt(endDateArray[0]), Integer.parseInt(endDateArray[1]),
                Integer.parseInt(endDateArray[2]));
        window.setTimeAndDateRange(sDate, startTimeCandidate, eDate, endTimeCnadidate);
        waitForPageLoadingToComplete();
        final SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        final String timeLabel = startTimeCandidate.getDescription() + ", " + datePattern.format(sDate) + " to "
                + endTimeCnadidate.getDescription() + ", " + datePattern.format(eDate);
        assertTrue("The time label is not right", window.getTimeLabelText().equals(timeLabel));
    }

    /**
     * Set date and time in time dialog window with a lag of configurable time.
     * For example : By selecting 15 mins on EventAnalysis window, it will display
     *               data with a specific time lag. 
     *               i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30,
     *               but due to delay in updation of UI at current time, it fails to display all the expected data till 11:30.
     *               Therefore to display all the expected datathis function creates a time lag for example of 5 mins and 
     *               fetches data from 11:10 to 11:25.
     *               Similarly for all other time time ranges, it displays data with a time lag of 5 mins.
     * Note :        This time lag can be configurable from properties.               
     *  
     * @param TimeRange ex: 15mins, 30mins etc.
     * NOTE : Cannot set timeRange for 5 mins due to UI constraints.  
     */
    private void delayAndSetTimeRange(final TimeRange timeRange) throws PopUpException {
        final GregorianCalendar date = new GregorianCalendar();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat AMPMFormatter = new SimpleDateFormat("a");
        final Formatter startDatefmt = new Formatter();
        final Formatter endDatefmt = new Formatter();

        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }

        final Date currentDate = date.getTime();
        logger.log(Level.INFO, "Current Date : " + currentDate);

        final long currentTime = currentDate.getTime(); // gets time in milliseconds                  

        final int timeRangeInMins = timeRange.getMiniutes();

        long endDateTime = currentTime - (15 * 60 * 1000); // 15 minutes delay time - TODO this value should be configurable by adding to properties. 

        date.setTimeInMillis(endDateTime);
        Date endDate = date.getTime();

        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));

        // Since DateAndTime Range Dialog window in UI contains only divisible of 15 in time,
        // Therefore round-off the mins to divisibles of 15 and mins less than 15 will be rounded to 0. 
        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }

        if (timeRange == TimeRange.ONE_DAY) {
            endDateTime = endDateTime - (TimeRange.ONE_DAY.getMiniutes() * 60 * 1000);
        }

        date.setTimeInMillis(endDateTime);
        endDate = date.getTime();

        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);

        date.setTimeInMillis(startDateTime);
        final Date startDate = date.getTime();

        // Get the time in 12 hour format
        startDatefmt.format("%tl", startDate);
        String hourStartDate = startDatefmt.toString();
        int lengthOfHour = hourStartDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourStartDate = "0" + hourStartDate;
        }

        endDatefmt.format("%tl", endDate);
        String hourEndDate = endDatefmt.toString();
        lengthOfHour = hourEndDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourEndDate = "0" + hourEndDate;
        }

        // Concatinate AM/PM, HOUR and MIN to form a member variable of TimeCandidate i.e AM_HOUR_MIN
        final String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        final String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_"
                + minFormatter.format(endDate.getTime());

        //logger.log(Level.INFO, "TimeRange Start Date : " + startDate);
        //logger.log(Level.INFO, "TimeRange End Date : " + endDate);
        //logger.log(Level.INFO, "Start Time Candidate : " + startDateTimeCandidate);
        //logger.log(Level.INFO, "End Time Candidate : " + endDateTimeCandidate);       
        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate
                + " " + startDateTimeCandidate + " and End Date Time Candidate : " + endDate + " "
                + endDateTimeCandidate);
        subscriberEventAnalysis.setTimeAndDateRange(startDate, TimeCandidates.valueOf(startDateTimeCandidate), endDate,
                TimeCandidates.valueOf(endDateTimeCandidate));
    }

}
