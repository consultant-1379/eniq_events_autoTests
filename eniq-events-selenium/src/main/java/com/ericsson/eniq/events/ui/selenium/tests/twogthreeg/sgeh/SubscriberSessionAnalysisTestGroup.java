/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
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
 * 
 * @author ericker
 * @since 2010
 *
 */
public class SubscriberSessionAnalysisTestGroup extends BaseSubscriber {

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;
    
//	@Autowired
//	private Workspace workspace;
//	
//	public static WebElement element; jj added - remove or incorporate before checking in
    
    private final List<String> expectedHeadersOnSelectedEventTypeOfFailure = new ArrayList<String>(Arrays.asList(
            "Event Time", "IMSI", "TAC", "Terminal Make", "Terminal Model", "Event Type", "Event Result",
            "Cause Code", "Sub Cause Code", "SGSN-MME", "Controller", "Access Area", "RAN Vendor", "APN"));

    final List<String> defaultHeadersOnIMSIEventAnalysisWindow = new ArrayList<String>(Arrays.asList("Event Time", "TAC", 
    		"Terminal Make", "Terminal Model", "Event Type", "Event Result",
            "Cause Code", "Sub Cause Code", "SGSN-MME", "Controller", "Access Area", "RAN Vendor", "APN"));

    final List<String> expectedHeadersOnIMSIGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList("Total",
            "Success Ratio", "Failures", "Successes", "Impacted Subscribers", "Event Type"));

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberSession test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberSession test section");
    }

    /**
     *  Requirement: 105 65-0528/00349
     *  Test Case: 4.5.2
     *  It shall be possible to query for a subscribers event history using IMSI. 
     *  The query result shall display all event details on the user interface. 
     *  Other than the input field the IMSI shall not be displayed on the user interface.
     *  This is not true for Subscriber Event Analysis
     */
    @Test
    public void IMSISearchEventDetailsSummaryDisplay_4_5_2() throws Exception {
    	selenium.setSpeed("1");
    	openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        
       /* //Check data appeared in Event Analysis Window
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(0);
        assertEquals(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC), result
                .get(GuiStringConstants.TAC));
        assertEquals(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TERMINAL_MAKE),
                result.get(GuiStringConstants.TERMINAL_MAKE));
        assertEquals(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TERMINAL_MODEL),
                result.get(GuiStringConstants.TERMINAL_MODEL)); 
        //assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), result.get(GuiStringConstants.SGSN)); */
    }

    /**
     *  Requirement: 105 65-0528/00350
     *  Test Case: 4.5.3
     *  It shall be possible to query for a subscriber group event history using group name. 
     *  The query result shall display all event details for the group on the user interface.
     */
    @Test
    public void IMSIGroupSearchEventDetailsSummaryDisplay_4_5_3() throws Exception {
        openIMSIGroupEventAnalysisWindow(true);

        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnIMSIGroupEventAnalysisWindow));
        subscriberEventAnalysis.sortTable(SortType.DESCENDING, "Failures");
        subscriberEventAnalysis.clickTableCell(0, "Failures"); // Failure links
        waitForPageLoadingToComplete();

        //check IMSI is displayed by default but not PTMSI
        assertTrue("IMSI column should be displayed by default", subscriberEventAnalysis.getTableHeaders().contains(
                "IMSI"));
        assertFalse("PTMSI column should be hidden by default", subscriberEventAnalysis.getTableHeaders().contains(
                "PTMSI"));
        //check PTMSI option box so that it can be displayed  
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList("PTMSI"));
        assertTrue("IMSI column should be displayed by default", subscriberEventAnalysis.getTableHeaders().contains(
                "PTMSI"));

        final Map<String, String> resultAtFirstRow = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final ImsiNumber imsi = reservedDataHelper.getImsiNumber(resultAtFirstRow.get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI), resultAtFirstRow
                .get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TAC), resultAtFirstRow
                .get(GuiStringConstants.TAC));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MAKE), resultAtFirstRow
                .get(GuiStringConstants.TERMINAL_MAKE));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MODEL),
                resultAtFirstRow.get(GuiStringConstants.TERMINAL_MODEL));
        assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), resultAtFirstRow
                .get(GuiStringConstants.SGSN));
    }

    /**
     *  Requirement: 105 65-0528/00351
     *  Test Case: 4.5.4
     *  Events shall display the following contents by default: 
     *  eventtime, eventtype, IMSI(obfuscated), CELL, BSC(RNC for UMTS), SGSN, GGSN, 
     *  APN, RESULT, CAUSE CODE, SUBCAUSE CODE, MCC, MNC, RAC, LAC.
     */
    @Test
    public void IMSIEventDetailsDisplay_4_5_4() throws Exception {
        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);

        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();
        assertTrue(tableHeaders.containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
    }

    /**
     *  Requirement: 105 65-0528/00352
     *  Test Case: 4.5.5
     *  The following events fields shall be available for selection, as additional fields, 
     *  within the user interface: 
     *  HLR address, OLD SGSN, OLD MCC, OLD MNC, OLD RAC, OLD LAC, TRANSFERRED PDP, 
     *  LOST PDP, TAC, CELL VENDOR ID.
     */
    @Test
    public void optionalEventFieldsDisplayed_4_5_5() throws Exception {
        final List<String> expectedOptionalHeaders = new ArrayList<String>(Arrays.asList("RAT", "BTS", "Linked NSAPI",
                "PDP NSAPI 1", "PDP NSAPI 2", "PDP GGSN Name 2", "Paging Attempts", "Service Request Trigger",
                "Request Retries", "Old SGSN", "Old MCC", "Old MNC", "Old RAC", "Old LAC", "Transferred PDP",
                "Dropped PDP"));

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);

        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(expectedOptionalHeaders);
        waitForPageLoadingToComplete();

        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();

        assertTrue(tableHeaders.containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
        assertTrue(tableHeaders.containsAll(expectedOptionalHeaders));
    }

    /**
     *  Requirement: 105 65-0528/00353
     *  Test Case: 4.5.6
     *  From an event listing it shall be possible to drill into CELL level summary information. 
     *  The following information shall be displayed: 
     *  total number events, number of failed events, number of successful events, 
     *  success ratio, number of subscribers.
     */
    @Test
    public void IMSIEventAccessAreaDrillDown_4_5_6() throws Exception {
    	selenium.setSpeed("1");
    	final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller",
                "Access Area", "Event Type", "Failures", "Successes", "Total Events", "Success Ratio",
                "Impacted Subscribers"));

        
        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();
        
        
        clickTableCellWhereEventResultEqualsReject("Access Area", subscriberEventAnalysis);

        assertTrue("Headers contains expected entries: ", subscriberEventAnalysis.getTableHeaders().containsAll(
                expectedHeaders));  // checking access area event analysis window against above expected headers

        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
               subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    /**
     *  Requirement: 105 65-0528/00354
     *  Test Case: 4.5.7
     *  From an event listing it shall be possible to drill into Controller level summary information. 
     *  The following information shall be displayed: 
     *  total number events, number of failed events, number of successful events, 
     *  success ratio, number of subscribers. 
     *  It shall be possible to drill down from this screen. 
     *  Note: This is only valid in the case where the event is for GSM/GPRS control plane signalling.
     */
    @Test
    public void IMSIEventControllerDrillDown_4_5_7() throws Exception {
    	final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller",
                "Event Type", "Failures", "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();

        clickTableCellWhereEventResultEqualsReject("Controller", subscriberEventAnalysis);
        
        assertTrue("Headers contains expected entries: ", subscriberEventAnalysis.getTableHeaders().containsAll(
                expectedHeaders));

        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
                subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    /**
     *  Requirement: 105 65-0528/00356
     *  Test Case: 4.5.8
     *  From an event listing it shall be possible to drill into SGSN level summary information. 
     *  The following information shall be displayed: 
     *  total number events, number of failed events, number of successful events, 
     *  success ratio, number of subscribers. 
     *  It shall be possible to drill down from this screen.
     */
    @Test
    public void IMSIEventSGSNDrillDown_4_5_8() throws Exception {
    	selenium.setSpeed("1");
//        final List<String> expectedHeadersOnSGSNEventAnalysis = new ArrayList<String>(Arrays.asList("Total Events",
//                "Failures", "Successes", "Success Ratio", "Event Type", "SGSN"));
        
    	final List<String> expectedHeadersOnSGSNEventAnalysis = new ArrayList<String>(Arrays.asList("SGSN-MME", 
    			"Total Events", "Failures", "Successes", "Success Ratio", "Event Type", "Impacted Subscribers"));
        
    	openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();
        
        clickTableCellWhereEventResultEqualsReject("SGSN-MME", subscriberEventAnalysis);

        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();

        assertTrue(tableHeaders.containsAll(expectedHeadersOnSGSNEventAnalysis));
        assertTrue("Table headers contains expected entries: ",
                tableHeaders.size() == expectedHeadersOnSGSNEventAnalysis.size());

        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
                subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    /**
     *  Requirement: 105 65-0528/00357
     *  Test Case: 4.5.9
     *  From an event listing it shall be possible to drill into APN level summary information. 
     *  The following information shall be displayed: 
     *  total number events, number of failed events, number of successful events, 
     *  success ratio, number of subscribers. 
     *  It shall be possible to drill down from this screen.
     */
    @Test
    public void IMSIEventAPNDrillDown_4_5_9() throws Exception {
    	selenium.setSpeed("1");
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("APN", "Event Type", "Failures",
                "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();
        
        clickTableCellWhereEventResultEqualsReject("APN", subscriberEventAnalysis);

        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();

        assertTrue(tableHeaders.containsAll(expectedHeaders));
        assertTrue("Table headers contains unexpected entries: ", tableHeaders.size() == expectedHeaders.size());

        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
                subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    /**
     *  Requirement: 105 65-0528/00358
     *  Test Case: 4.5.10
     *  From an event listing it shall be possible to drill into TAC level summary information. 
     *  The following information shall be displayed: 
     *  total number events, number of failed events, number of successful events, 
     *  success ratio, number of subscribers. 
     *  It shall be possible to drill down from this screen.
     */
    @Test
    public void IMSIEventTACDrillDown_4_5_10_() throws Exception {
    	selenium.setSpeed("1");
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("Manufacturer", "Model", "TAC", 
        		"Event Type", "Failures", "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK); // change this to getTimeRangeFromProperties()
        waitForPageLoadingToComplete();
        
        clickTableCellWhereEventResultEqualsReject("TAC", subscriberEventAnalysis);

        final List<String> tableHeaders = subscriberEventAnalysis.getTableHeaders();
        assertTrue(tableHeaders.containsAll(expectedHeaders));
        assertTrue("Table headers contains expected entries: ", tableHeaders.size() == expectedHeaders.size());

        validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
                subscriberEventAnalysis, ImsiNumber.IMSI_A);
    }

    /**
     * Test Case: 4.5.11
     * when an IMSI is entered in the search bar and the play button is clicked 
     * the event analysis view is opened and the raw events are shown.
     */
    @Test
    public void openDefaultViewForIMSI_4_5_11() throws Exception {
        openIMSIEventAnalysisWindow(false, ImsiNumber.IMSI_A);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(defaultHeadersOnIMSIEventAnalysisWindow));
    }

    /**
     * Test Case: 4.5.12 
     * when an IMSI group is entered in the search bar and the play button is clicked 
     * the event analysis view is opened and an event summary view is shown
     */
    @Test
    public void openDefaultViewForIMSIGroup_4_5_12() throws Exception {
        openIMSIGroupEventAnalysisWindow(false);
        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnIMSIGroupEventAnalysisWindow));
    }

    /**
     * Test Case: 4.5.13
     * verify that search button is working with IMSI, IMSI GROUP and PTMSI  
     */
    @Test
    public void searchBarCombinedForSingleAndGroup_4_5_13() throws Exception {
        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        subscriberTab.closeAllWindowsStart();

        openIMSIGroupEventAnalysisWindow(true);
        subscriberTab.closeAllWindowsStart();

        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.PTMSI, false, reservedDataHelper
                .getCommonReservedData(CommonDataType.PTMSI));
        assertTrue(selenium.isTextPresent("PTMSI Event Analysis"));
    }

    /**
     * Test Case: 4.5.16
     * RNC and SAC are displayed for 3G events in Subscriber or Subscriber Group Event Analysis
     */
    @Test
    public void RNCAndSACDisplayedFor3GEvent_4_5_16() throws Exception {
        openIMSIGroupEventAnalysisWindow(true);
        for (int i = 0; i < subscriberEventAnalysis.getTableRowCount(); i++) {
            //Action3
            final String failure = subscriberEventAnalysis.getTableData(i, subscriberEventAnalysis.getTableHeaders()
                    .indexOf("Failures"));
            subscriberEventAnalysis.clickTableCell(i, "Failures"); // Failure links
            waitForPageLoadingToComplete();
            //Action4 and 5 - hover the mouse over the Columns option in the context menu 
            //and ensure the Controller, RAT and Access Area checkboxes are checked 
            //eseuhon, As columns options are not actual type of checkbox in code point of view
            //not able to use DefaultSelenium.isChecked() to check if checkbox is checked or not.
            //instead I checked headers in table are visible or not under assumption - if any column is checked
            //then header is visible in table
            if (!failure.equals("0")) {
                verify2Gor3GEventData(subscriberEventAnalysis);
            }
            subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        }
        //simply remove all opened window above testing.  
        selenium.refresh();
        pause(2000);

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        verify2Gor3GEventData(subscriberEventAnalysis);
    }

    /**
     * Test Case: 4.5.18
     * verify that all Detach event summary and KPI values are displayed in Event Analysis 
     * for Subscriber Groups. 
     * verify that 2G/3G events are displayed for a Subscriber Event Analysis.
     */
    @Test
    public void twoGAnd3GEventsAndKPIAreDisplayed_4_5_18() throws Exception {
        openIMSIGroupEventAnalysisWindow(true);
        // click failures link of first row in table
        subscriberEventAnalysis.clickTableCell(0, "Failures"); // Failure links
        waitForPageLoadingToComplete();

        assertTrue(subscriberEventAnalysis.getTableHeaders().containsAll(expectedHeadersOnSelectedEventTypeOfFailure));
        selenium.refresh();
        pause(2000);

        openIMSIEventAnalysisWindow(true, ImsiNumber.IMSI_A);
        verify2Gor3GEventData(subscriberEventAnalysis);
    }

    /**
     * Test Case: 4.5.21
     * verify it is possible to trace subscriber events using PTMSI through the ENIQ Events GUI.
     */
    @Test
    public void traceSubscriberEventsUsingPTMSI_4_5_21() throws Exception {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.PTMSI, true, reservedDataHelper
                .getCommonReservedData(CommonDataType.PTMSI));
        assertTrue(selenium.isTextPresent("PTMSI Event Analysis"));
        //Validate some data
        final Map<String, String> resultAtFirstRow = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final ImsiNumber imsi = reservedDataHelper.getImsiNumber(resultAtFirstRow.get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI), resultAtFirstRow
                .get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TAC), resultAtFirstRow
                .get(GuiStringConstants.TAC));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MAKE), resultAtFirstRow
                .get(GuiStringConstants.TERMINAL_MAKE));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MODEL),
                resultAtFirstRow.get(GuiStringConstants.TERMINAL_MODEL));
        assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), resultAtFirstRow
                .get(GuiStringConstants.SGSN));
    }

    /**
     * Test Case: 4.5.22
     * verify that when an IMSI is entered in the search bar and the play button is clicked 
     * the event analysis view is opened and the raw events are shown
     */
    @Test
    public void openDefaultViewForPTMSI_4_5_22() throws Exception {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.PTMSI, false, reservedDataHelper
                .getCommonReservedData(CommonDataType.PTMSI));
        assertTrue(selenium.isTextPresent("PTMSI Event Analysis"));
        //Validate some data
        final Map<String, String> resultAtFirstRow = subscriberEventAnalysis.getAllDataAtTableRow(0);
        final ImsiNumber imsi = reservedDataHelper.getImsiNumber(resultAtFirstRow.get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI), resultAtFirstRow
                .get(GuiStringConstants.IMSI));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TAC), resultAtFirstRow
                .get(GuiStringConstants.TAC));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MAKE), resultAtFirstRow
                .get(GuiStringConstants.TERMINAL_MAKE));
        assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MODEL),
                resultAtFirstRow.get(GuiStringConstants.TERMINAL_MODEL));
        assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), resultAtFirstRow
                .get(GuiStringConstants.SGSN));
    }

    ///////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void clickTableCellWhereEventResultEqualsReject(final String tableHeader,
        final CommonWindow subscriberEventAnalysis) throws NoDataException, PopUpException {
        final int row = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue("Event Result", "REJECT");
        final Map<String, String> expect = subscriberEventAnalysis.getAllDataAtTableRow(row);
        subscriberEventAnalysis.clickTableCell(row, tableHeader);
        waitForPageLoadingToComplete();
        //take the first row's value as tableHeader's value is same at all row  
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(0);
        assertEquals(expect.get(tableHeader), result.get(tableHeader));
    }

    /**
     * Enter one of the IMSI number and open the Event Analysis window using it.  
     * Then set time selection to 1 week and sort the result.  
     * @throws NoDataException 
     * @throws PopUpException 
     * @throws InterruptedException 
     */
    private void openIMSIEventAnalysisWindow(final boolean useStartMenu, final ImsiNumber imsi) throws NoDataException,
            PopUpException, InterruptedException {
        System.out.println(useStartMenu + " "+ imsi);
    	subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.IMSI, useStartMenu, reservedDataHelper
                .getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI));
        assertTrue(selenium.isTextPresent("IMSI - Event Analysis"));
        
    }

    private void openIMSIGroupEventAnalysisWindow(final boolean useStartMenu) throws PopUpException,
            InterruptedException {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.IMSI_GROUP, useStartMenu, reservedDataHelper
                .getCommonReservedData(CommonDataType.IMSI_GROUP));
        assertTrue(selenium.isTextPresent("IMSI Group Summary Event Analysis"));
    }

    /**
     * Validate each of failure link on table by clicking it. 
     * Then we will check all columns headers of newly opened window
     * @param   tableHeadersAtCurrentTable
     * @param   tableHeadersAtNewlyOpenedTable
     * @param   subscriberEventAnalysis
     * @throws NoDataException 
     * @throws PopUpException 
     */
    private void validateByClickingEachFailurelinkInTableRow(final List<String> tableHeadersAtNewlyOpenedTable,
            final CommonWindow subscriberEventAnalysis, final ImsiNumber imsi) throws NoDataException, PopUpException {

        final List<Map<String, String>> allTableData = subscriberEventAnalysis.getAllTableData();
        for (int i = 0; i < 3 ; i++) {//allTableData.size()
            Map<String, String> result = allTableData.get(i);
            //ignore 0 failures
            if (!result.get("Failures").equals("0")) {
                subscriberEventAnalysis.clickTableCell(i, "Failures"); // Failure links
                waitForPageLoadingToComplete();
                assertEquals("Table headers are not matching.\n", tableHeadersAtNewlyOpenedTable,
                        subscriberEventAnalysis.getTableHeaders());
                //check for first row's values
//                final int row = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.IMSI,
//                        reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI));
//                result = subscriberEventAnalysis.getAllDataAtTableRow(row);
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI), result
//                        .get(GuiStringConstants.IMSI));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TAC), result
//                        .get(GuiStringConstants.TAC));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MAKE), result
//                        .get(GuiStringConstants.TERMINAL_MAKE));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MODEL), result
//                        .get(GuiStringConstants.TERMINAL_MODEL));
//                assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), result
//                        .get(GuiStringConstants.SGSN));
                subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
                pause(1000);
            }
        }
    }

}
