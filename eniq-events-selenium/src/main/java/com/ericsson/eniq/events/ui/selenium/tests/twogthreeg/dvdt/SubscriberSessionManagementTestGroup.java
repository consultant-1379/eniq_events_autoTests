/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
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
 * @author eseuhon
 * @since 2011
 *
 * All testing behaviors are defined in the VS(Verification Spec) 
 * 1/10264-CNA 403 2210 Uen, Rev G 
 */
public class SubscriberSessionManagementTestGroup extends BaseDvdtTest {

    @Autowired
    private SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO,
                "Start of 2G/3G Data Bearer Throughput and Data Volume's SubscriberSessionManagement test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO,
                "End of 2G/3G Data Bearer Throughput and Data Volume's SubscriberSessionManagement test section");
        reservedDataHelperReplacement = null;
    }

    final String imsi = reservedDataHelperReplacement.getReservedData(IMSI);

    final String imsiGroup = reservedDataHelperReplacement.getReservedData(IMSI_GROUP);

    final String ptmsi = reservedDataHelperReplacement.getReservedData(PTMSI);

    final String msisdn = reservedDataHelperReplacement.getReservedData(MSISDN);

    @Test
    public void IMSIEventAnalysisWithDataIntegrity_5_1() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, DOWNLINK_PEAK_USAGE, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE,
                UPLINK_PEAK_USAGE, QOS_UPGRADE, QOS_DOWNGRADE, CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders, DV_THROUGHOUTPUT);

        //data integrity check
        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int rowIndex = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");

        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(PDP_DURATION, reservedDataHelperReplacement.getReservedData(PDP_DURATION),
                result.get(PDP_DURATION));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_AVERAGE_USAGES,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_AVERAGE_USAGES),
                result.get(DOWNLINK_AVERAGE_USAGES));
        checkDataIntegrity(DOWNLINK_PEAK_USAGE, reservedDataHelperReplacement.getReservedData(DOWNLINK_PEAK_USAGE),
                result.get(DOWNLINK_PEAK_USAGE));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_AVERAGE_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_USAGE),
                result.get(UPLINK_AVERAGE_USAGE));
        checkDataIntegrity(UPLINK_PEAK_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_PEAK_USAGE),
                result.get(UPLINK_PEAK_USAGE));
    }

    @Test
    public void IMSIEventAnalysisWithIMSIGroupWithDataIntegrity_5_3() throws Exception {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.IMSI_GROUP, true, imsiGroup);
        assertTrue(selenium.isTextPresent("IMSI Group Summary Event Analysis"));

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int rowIndex = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");

        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(BEARER_COUNT, reservedDataHelperReplacement.getReservedData(BEARER_COUNT),
                result.get(BEARER_COUNT));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_THROUGHPUT_MB_SEC,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_THROUGHPUT_MB_SEC),
                result.get(DOWNLINK_THROUGHPUT_MB_SEC));
        checkDataIntegrity(DOWNLINK_AVERAGE_BEARER_VOLUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_AVERAGE_BEARER_VOLUME_KB),
                result.get(DOWNLINK_AVERAGE_BEARER_VOLUME_KB));

        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_THROUGHPUT_MB_SEC,
                reservedDataHelperReplacement.getReservedData(UPLINK_THROUGHPUT_MB_SEC),
                result.get(UPLINK_THROUGHPUT_MB_SEC));
        checkDataIntegrity(UPLINK_AVERAGE_BEARER_VOLUME_KB,
                reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_BEARER_VOLUME_KB),
                result.get(UPLINK_AVERAGE_BEARER_VOLUME_KB));
    }

    @Test
    public void drillToSGSNEventAnalysisFromIMSIEventAnalysis_5_5() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, "SGSN-MME");

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);
    }

    @Test
    public void drillToAPNEventAnalysisFromIMSIEventAnalysis_5_6() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");

        subscriberEventAnalysis.clickTableCell(tableRow, APN);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);
    }

    @Test
    public void drillToTACEventAnalysisFromIMSIEventAnalysisWithDataIntegrity_5_7() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, reservedDataHelperReplacement.getReservedData(IMSI));
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, TAC);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_THROUGHPUT_KB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_KB,
                UPLINK_THROUGHPUT_KB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);

        //data integrity check
        final int rowIndex = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(TAC, reservedDataHelperReplacement.getReservedData(TAC), result.get(TAC));
        checkDataIntegrity(BEARER_COUNT, reservedDataHelperReplacement.getReservedData(BEARER_COUNT),
                result.get(BEARER_COUNT));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_THROUGHPUT_KB_SEC,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_THROUGHPUT_KB_SEC),
                result.get(DOWNLINK_THROUGHPUT_KB_SEC));
        checkDataIntegrity(DOWNLINK_AVERAGE_BEARER_VOLUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_AVERAGE_BEARER_VOLUME_KB),
                result.get(DOWNLINK_AVERAGE_BEARER_VOLUME_KB));

        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_THROUGHPUT_KB_SEC,
                reservedDataHelperReplacement.getReservedData(UPLINK_THROUGHPUT_KB_SEC),
                result.get(UPLINK_THROUGHPUT_KB_SEC));
        checkDataIntegrity(UPLINK_AVERAGE_BEARER_VOLUME_KB,
                reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_BEARER_VOLUME_KB),
                result.get(UPLINK_AVERAGE_BEARER_VOLUME_KB));

    }

    @Test
    public void checkPanesAndLayout_5_8() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        //The following columns should be hidden by default
        final List<String> notExpectedHeadersOnControllerEventAnalysis = new ArrayList<String>(Arrays.asList("PTMSI",
                "Cause Protocol Type", "Tracking Area", "Handover Type", "Old TAI", "Old ECI", "Old MME", "Old MTMSI",
                "Old SGW IPv4", "Old SGW IPv6", "Deactivation PDN Type", "Attach Type", "Detach Type",
                "Detach Trigger", "GGSN", "MS IP Address 1", "MS IP Address 2", "RAC", "LAC", "Update Type", "HLR",
                "Deactivation Trigger", "QOS Upgrade", "QOS Downgrade"));
        List<String> headers = subscriberEventAnalysis.getTableHeaders();
        assertFalse(headers.containsAll(notExpectedHeadersOnControllerEventAnalysis));

        //PDP Duration column should be placed before the Downlink and Uplink clusters.
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE), DV_THROUGHOUTPUT);

        headers = subscriberEventAnalysis.getTableHeaders();
        final List<String> headersOfDownAndUplink = new ArrayList<String>(Arrays.asList(DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE));
        for (final String header : headersOfDownAndUplink) {
            assertTrue(headers.indexOf(PDP_DURATION) < headers.indexOf(header));
        }

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        //All %'s displayed with 2-3 decimal places only.
        for (final String value : subscriberEventAnalysis.getAllTableDataAtColumn(DOWNLINK_AVERAGE_USAGES)) {
            final int indexOfDot = value.indexOf(".");
            assertTrue(value + " is not displayed with 2-3 decimal places", (value.length() - indexOfDot - 1) < 4);
        }

        for (final String value : subscriberEventAnalysis.getAllTableDataAtColumn(UPLINK_AVERAGE_USAGE)) {
            final int indexOfDot = value.indexOf(".");
            assertTrue(value + " is not displayed with 2-3 decimal places", (value.length() - indexOfDot - 1) < 4);
        }

    }

    @Test
    public void drillToDataVolumeAnalysisFromTACEventWithDataIntegrity_5_11() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, reservedDataHelperReplacement.getReservedData(IMSI));
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, "TAC");
        assertTrue(selenium.isTextPresent("TAC Event Analysis"));

        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList(DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));

        //data integrity check for downlink data volume 
        final int rowIndexOfDeactivateEventType = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(
                EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, DOWNLINK_DATA_VOLOUME_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        final int rowIndexOfIMSI = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndexOfIMSI);
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));
        subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        //data integrity check for uplink data volume 
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, UPLINK_DATA_VOLUME_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
    }

    @Test
    public void drillToDataVolumeAnalysisFromAPNEventWithDataIntegrity_5_12() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, reservedDataHelperReplacement.getReservedData(IMSI));
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, "APN");
        assertTrue(selenium.isTextPresent("APN Event Analysis"));

        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList(DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));

        //data integrity check for downlink data volume 
        final int rowIndexOfDeactivateEventType = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(
                EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, DOWNLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        final int rowIndexOfIMSI = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndexOfIMSI);
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
        subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        //data integrity check for uplink data volume 
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, UPLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
    }

    @Test
    public void drillToDataVolumeAnalysisFromSGSNEventWithDataIntegrity_5_13() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, reservedDataHelperReplacement.getReservedData(IMSI));
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, "SGSN-MME");
        assertTrue(selenium.isTextPresent("SGSN-MME Event Analysis"));

        //data integrity check for downlink data volume 
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList(DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));

        final int rowIndexOfDeactivateEventType = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(
                EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, DOWNLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        final int rowIndexOfIMSI = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndexOfIMSI);
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
        subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        //data integrity check for uplink data volume 
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, UPLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
    }

    @Test
    public void drillToNonDvDtRelatedEventAnalysis_5_14() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        subscriberEventAnalysis.sortTable(SortType.DESCENDING, "Controller");
        subscriberEventAnalysis.clickTableCell(0, "Controller");
        final List<String> notExpectedHeadersOnControllerEventAnalysis = new ArrayList<String>(Arrays.asList(
                BEARER_COUNT, DOWNLINK_DATA_VOLOUME_KB, DOWNLINK_THROUGHPUT_KB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB,
                UPLINK_DATA_VOLUME_KB, UPLINK_THROUGHPUT_KB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        assertFalse(subscriberEventAnalysis.getTableHeaders().containsAll(notExpectedHeadersOnControllerEventAnalysis));

        selenium.refresh();
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        subscriberEventAnalysis.sortTable(SortType.DESCENDING, "Access Area");
        subscriberEventAnalysis.clickTableCell(0, "Access Area");
        final List<String> notExpectedHeadersOnAccessAreaEventAnalysis = new ArrayList<String>(Arrays.asList(
                BEARER_COUNT, DOWNLINK_DATA_VOLOUME_KB, DOWNLINK_THROUGHPUT_KB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB,
                UPLINK_DATA_VOLUME_KB, UPLINK_THROUGHPUT_KB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        assertFalse(subscriberEventAnalysis.getTableHeaders().containsAll(notExpectedHeadersOnAccessAreaEventAnalysis));
    }

    @Test
    public void drillToEventAnalysisFromNonDvDtRelatedEventAnalysis_5_15() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        subscriberEventAnalysis.sortTable(SortType.DESCENDING, "Controller");
        subscriberEventAnalysis.clickTableCell(0, "Controller");

        subscriberEventAnalysis.clickTableCell(0, "Failures");
        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, DOWNLINK_PEAK_USAGE, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE,
                UPLINK_PEAK_USAGE, QOS_UPGRADE, QOS_DOWNGRADE, CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders, DV_THROUGHOUTPUT);

        //go back to IMSI Event Analysis window
        selenium.refresh();
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        subscriberEventAnalysis.sortTable(SortType.DESCENDING, "Access Area");
        subscriberEventAnalysis.clickTableCell(0, "Access Area");

        subscriberEventAnalysis.clickTableCell(0, "Failures");
        //as we already check optional headers before,  those headers are now displayed by default.
        checkStringListContainsArray(subscriberEventAnalysis.getTableHeaders(), hiddenHeaders.toArray(new String[0]));
    }

    @Test
    public void drillToDataVolumeAnalysisFromIMSIGroupEventAnalysisWithDataIntegrity_5_16() throws Exception {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.IMSI_GROUP, true,
                reservedDataHelperReplacement.getReservedData(IMSI_GROUP));
        assertTrue(selenium.isTextPresent("IMSI Group Summary Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);

        //data integrity check for downlink data volume
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(Arrays.asList(DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));

        final int rowIndexOfDeactivateEventType = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(
                EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, DOWNLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        final int rowIndexOfIMSI = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndexOfIMSI);
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
        subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        //data integrity check for uplink data volume 
        subscriberEventAnalysis.clickTableCell(rowIndexOfDeactivateEventType, UPLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
    }

    @Test
    public void PTMSIEventAnalysis_5_17() throws Exception {
        subscriberTab.openEventAnalysisWindow(SubscriberTab.ImsiMenu.PTMSI, false, ptmsi);
        assertTrue(selenium.isTextPresent("PTMSI Event Analysis"));

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, DOWNLINK_PEAK_USAGE, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE,
                UPLINK_PEAK_USAGE, QOS_UPGRADE, QOS_DOWNGRADE, CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders, DV_THROUGHOUTPUT);
    }

    @Test
    public void drillToEventAnalysisFromSGSNEventAnalysis_5_22() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, imsi);
        assertTrue(selenium.isTextPresent("IMSI Event Analysis"));

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        subscriberEventAnalysis.clickTableCell(0, "SGSN-MME");

        final int row = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(row, "Failures");

        //check headers
        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE, QOS_UPGRADE, QOS_DOWNGRADE,
                CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders, DV_THROUGHOUTPUT);
    }

    @Test
    public void MSISDNEventAnalysisWithDataIntegrity_5_23() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.MSISDN, true, msisdn);
        assertTrue(selenium.isTextPresent("MSISDN Event Analysis"));

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, DOWNLINK_PEAK_USAGE, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE,
                UPLINK_PEAK_USAGE, QOS_UPGRADE, QOS_DOWNGRADE, CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders, DV_THROUGHOUTPUT);

        //data integrity check
        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int rowIndex = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");

        final Map<String, String> result = subscriberEventAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(PDP_DURATION, reservedDataHelperReplacement.getReservedData(PDP_DURATION),
                result.get(PDP_DURATION));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_AVERAGE_USAGES,
                reservedDataHelperReplacement.getReservedData(DOWNLINK_AVERAGE_USAGES),
                result.get(DOWNLINK_AVERAGE_USAGES));
        checkDataIntegrity(DOWNLINK_PEAK_USAGE, reservedDataHelperReplacement.getReservedData(DOWNLINK_PEAK_USAGE),
                result.get(DOWNLINK_PEAK_USAGE));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_AVERAGE_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_USAGE),
                result.get(UPLINK_AVERAGE_USAGE));
        checkDataIntegrity(UPLINK_PEAK_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_PEAK_USAGE),
                result.get(UPLINK_PEAK_USAGE));
    }

    @Test
    public void drillToTACEventAnalysisFromMSISDNEventAnalysis_5_24() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.MSISDN, true, msisdn);

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        //        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(0, TAC);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_THROUGHPUT_KB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_KB,
                UPLINK_THROUGHPUT_KB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);
    }

    @Test
    public void drillToAPNEventAnalysisFromMSISDNEventAnalysis_5_25() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.MSISDN, true, msisdn);

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, APN);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);
    }

    @Test
    public void drillToSGSNEventAnalysisFromMSISDNEventAnalysis_5_26() throws Exception {
        subscriberTab.openEventAnalysisWindow(ImsiMenu.MSISDN, true, msisdn);

        subscriberEventAnalysis.setTimeRange(timeToLoadReservedData);
        final int tableRow = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        subscriberEventAnalysis.clickTableCell(tableRow, "SGSN-MME");

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(subscriberEventAnalysis, hiddenHeaders);
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

}
