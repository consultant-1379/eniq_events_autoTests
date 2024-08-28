/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.StartMenu;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.events.windows.TerminalGroupAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.TerminalGroupAnalysisWindow.ViewMenu;
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
 * 
 */
public class TerminalAnalysisTestGroup extends BaseDvdtTest {

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysisWindow;

    @Autowired
    @Qualifier("terminalGroupAnalysis")
    private TerminalGroupAnalysisWindow terminalGroupAnalysis;

    @Autowired
    @Qualifier("terminalAnalysis")
    private TerminalGroupAnalysisWindow terminalAnalysis;

    @BeforeClass
    public static void openLog() {
        logger
                .log(Level.INFO,
                        "Start of 2G/3G Data Bearer Throughput and Data Volume's Terminal Analysis test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of 2G/3G Data Bearer Throughput and Data Volume's Terminal Analysis test section");
        reservedDataHelperReplacement = null;
    }

    @Test
    public void terminalEventAnalysisWithDataIntegrity_8_1() throws Exception {
        final String[] terminal = { reservedDataHelperReplacement.getReservedData(MANUFACTURER),
                reservedDataHelperReplacement.getReservedData(MODEL),
                reservedDataHelperReplacement.getReservedData(TAC) };
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL, true, terminal);
        assertTrue(selenium.isTextPresent("Terminal Event Analysis"));

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_THROUGHPUT_KB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_KB,
                UPLINK_THROUGHPUT_KB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders);

        //data integrity check
        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);
        final int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                "DEACTIVATE");
        final Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(BEARER_COUNT, reservedDataHelperReplacement.getReservedData(BEARER_COUNT), result
                .get(BEARER_COUNT));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_THROUGHPUT_KB_SEC, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_THROUGHPUT_KB_SEC), result.get(DOWNLINK_THROUGHPUT_KB_SEC));
        checkDataIntegrity(DOWNLINK_AVERAGE_BEARER_VOLUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_AVERAGE_BEARER_VOLUME_KB), result.get(DOWNLINK_AVERAGE_BEARER_VOLUME_KB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_THROUGHPUT_KB_SEC, reservedDataHelperReplacement
                .getReservedData(UPLINK_THROUGHPUT_KB_SEC), result.get(UPLINK_THROUGHPUT_KB_SEC));
        checkDataIntegrity(UPLINK_AVERAGE_BEARER_VOLUME_KB, reservedDataHelperReplacement
                .getReservedData(UPLINK_AVERAGE_BEARER_VOLUME_KB), result.get(UPLINK_AVERAGE_BEARER_VOLUME_KB));
    }

    @Test
    public void drilldownOnULDLToDataVolumeAnalysisFromTerminalEventAnalysisWithDataIntegrity_8_2() throws Exception {
        final String[] terminal = { reservedDataHelperReplacement.getReservedData(MANUFACTURER),
                reservedDataHelperReplacement.getReservedData(MODEL),
                reservedDataHelperReplacement.getReservedData(TAC) };
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL, true, terminal);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders);
        // in order to remove check boxes appeared we will re-load a current window
        selenium.refresh();

        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL, true, terminal);
        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, DOWNLINK_DATA_VOLOUME_KB);
        terminalEventAnalysisWindow.clickTableCell(0, DOWNLINK_DATA_VOLOUME_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(IMSI, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkStringListContainsArray(terminalEventAnalysisWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));

        terminalEventAnalysisWindow.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, UPLINK_DATA_VOLUME_KB);
        terminalEventAnalysisWindow.clickTableCell(0, UPLINK_DATA_VOLUME_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkStringListContainsArray(terminalEventAnalysisWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
    }

    @Test
    public void terminalGroupEventAnalysisWithDataIntegrity_8_3() throws Exception {
        final String[] terminalGroup = { reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP) };
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true, terminalGroup);

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(BEARER_COUNT, DOWNLINK_DATA_VOLUME_MB,
                DOWNLINK_THROUGHPUT_MB_SEC, DOWNLINK_AVERAGE_BEARER_VOLUME_KB, UPLINK_DATA_VOLUME_MB,
                UPLINK_THROUGHPUT_MB_SEC, UPLINK_AVERAGE_BEARER_VOLUME_KB));

        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders);

        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);
        final int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE,
                "DEACTIVATE");
        final Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(BEARER_COUNT, reservedDataHelperReplacement.getReservedData(BEARER_COUNT), result
                .get(BEARER_COUNT));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_THROUGHPUT_MB_SEC, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_THROUGHPUT_MB_SEC), result.get(DOWNLINK_THROUGHPUT_MB_SEC));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_THROUGHPUT_MB_SEC, reservedDataHelperReplacement
                .getReservedData(UPLINK_THROUGHPUT_MB_SEC), result.get(UPLINK_THROUGHPUT_MB_SEC));
        checkDataIntegrity(UPLINK_AVERAGE_BEARER_VOLUME_KB, reservedDataHelperReplacement
                .getReservedData(UPLINK_AVERAGE_BEARER_VOLUME_KB), result.get(UPLINK_AVERAGE_BEARER_VOLUME_KB));
    }

    @Test
    public void drilldownOnULDLToDataVolumeAnalysisFromTerminalGroupEventAnalysisWithDataIntegrity_8_4()
            throws Exception {
        final String[] terminalGroup = { reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP) };
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true, terminalGroup);
        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders);
        // in order to remove check boxes appeared we will re-load a current window
        selenium.refresh();
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true, terminalGroup);
        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, DOWNLINK_DATA_VOLUME_MB);
        terminalEventAnalysisWindow.clickTableCell(0, DOWNLINK_DATA_VOLUME_MB);

        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(IMSI, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(terminalEventAnalysisWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));

        terminalEventAnalysisWindow.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        terminalEventAnalysisWindow.sortTable(SortType.DESCENDING, UPLINK_DATA_VOLUME_MB);
        terminalEventAnalysisWindow.clickTableCell(0, UPLINK_DATA_VOLUME_MB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));
        checkStringListContainsArray(terminalEventAnalysisWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
    }

    @Test
    public void highestDataVolumeViewWithTerminalGroupWithDataIntegrity_8_6() throws Exception {
        terminalTab.openTab();
        terminalTab.openSubStartMenu(StartMenu.GROUP_ANALYSIS);

        terminalGroupAnalysis.clickButton(SelectedButtonType.VIEW_BUTTON);
        terminalGroupAnalysis.clickViewSubMenu(ViewMenu.HIGHEST_DATAVOLUME);
        assertTrue(selenium.isTextPresent("Group Analysis"));

        // change time to load reserved data 
        terminalGroupAnalysis.setTimeRange(timeToLoadReservedData);
        terminalGroupAnalysis.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        waitForPageLoadingToComplete();

        //data integrity check
        final int row = terminalGroupAnalysis.findFirstTableRowWhereMatchingAnyValue("Groupname",
                reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP));
        final Map<String, String> result = terminalGroupAnalysis.getAllDataAtTableRow(row);

        checkDataIntegrity(TOTAL_DOWNLINK_DATA_VOL_MB, reservedDataHelperReplacement
                .getReservedData(TOTAL_DOWNLINK_DATA_VOL_MB), result.get(TOTAL_DOWNLINK_DATA_VOL_MB));
        checkDataIntegrity(TOTAL_UPLINK_DATA_VOL_MB, reservedDataHelperReplacement
                .getReservedData(TOTAL_UPLINK_DATA_VOL_MB), result.get(TOTAL_UPLINK_DATA_VOL_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(TOTAL_BEARER_COUNT, reservedDataHelperReplacement.getReservedData(TOTAL_BEARER_COUNT),
                result.get(TOTAL_BEARER_COUNT));
    }

    @Test
    public void highestDataVolumeViewWithTerminalWithDataIntegrity_8_6_b() throws Exception {
        final String[] terminal = { reservedDataHelperReplacement.getReservedData(MANUFACTURER),
                reservedDataHelperReplacement.getReservedData(MODEL),
                reservedDataHelperReplacement.getReservedData(TAC) };

        terminalTab.openTab();
        terminalTab.setSearchType(TerminalType.TERMINAL);
        terminalTab.openTerminalMakerSelect(terminal[0]); // terminal maker
        terminalTab.enterSearchValue(terminal[1] + "," + terminal[2], false); // terminal model + TAC 
        terminalTab.openSubStartMenu(StartMenu.TERMINAL_ANALYSIS);
        terminalAnalysis.clickButton(SelectedButtonType.VIEW_BUTTON);
        terminalAnalysis.clickViewSubMenu(ViewMenu.HIGHEST_DATAVOLUME);
        assertTrue(selenium.isTextPresent("Terminal Analysis - Highest Data Volume"));

        //data integrity check
        terminalAnalysis.setTimeRange(timeToLoadReservedData);
        final int row = terminalAnalysis.findFirstTableRowWhereMatchingAnyValue(MODEL, terminal[1]);
        final Map<String, String> result = terminalAnalysis.getAllDataAtTableRow(row);

        checkDataIntegrity(MANUFACTURER, reservedDataHelperReplacement.getReservedData(MANUFACTURER), result
                .get(MANUFACTURER));
        checkDataIntegrity(TAC, reservedDataHelperReplacement.getReservedData(TAC), result.get(TAC));
        checkDataIntegrity(TOTAL_DOWNLINK_DATA_VOL_KB, reservedDataHelperReplacement
                .getReservedData(TOTAL_DOWNLINK_DATA_VOL_KB), result.get(TOTAL_DOWNLINK_DATA_VOL_KB));
        checkDataIntegrity(TOTAL_UPLINK_DATA_VOL_KB, reservedDataHelperReplacement
                .getReservedData(TOTAL_UPLINK_DATA_VOL_KB), result.get(TOTAL_UPLINK_DATA_VOL_KB));
        checkDataIntegrity(TOTAL_DATA_VOL_KB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_KB), result
                .get(TOTAL_DATA_VOL_KB));
        checkDataIntegrity(TOTAL_BEARER_COUNT, reservedDataHelperReplacement.getReservedData(TOTAL_BEARER_COUNT),
                result.get(TOTAL_BEARER_COUNT));

    }

    @Test
    public void checkDrillDownOnULAndDLFromHighestDataVolumeViewWithTerminalGroup_8_7() throws Exception {
        terminalTab.openTab();
        terminalTab.setSearchType(TerminalType.TERMINAL_GROUP);
        terminalTab.enterSearchValue(reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP), true);
        terminalTab.openSubStartMenu(StartMenu.GROUP_ANALYSIS);

        terminalGroupAnalysis.clickButton(SelectedButtonType.VIEW_BUTTON);
        terminalGroupAnalysis.clickViewSubMenu(ViewMenu.HIGHEST_DATAVOLUME);
        assertTrue(selenium.isTextPresent("Group Analysis"));
    }

    @Test
    public void checkDrillDownOnULAndDLFromHighestDataVolumeViewWithTerminalWithDataIntegrity_8_7_b() throws Exception {
        final String[] terminal = { reservedDataHelperReplacement.getReservedData(MANUFACTURER),
                reservedDataHelperReplacement.getReservedData(MODEL),
                reservedDataHelperReplacement.getReservedData(TAC) };

        terminalTab.openTab();
        terminalTab.openSubStartMenu(StartMenu.TERMINAL_ANALYSIS);

        terminalAnalysis.clickButton(SelectedButtonType.VIEW_BUTTON);
        terminalAnalysis.clickViewSubMenu(ViewMenu.HIGHEST_DATAVOLUME);
        assertTrue(selenium.isTextPresent("Terminal Analysis - Highest Data Volume"));

        //drill down on UL and DL
        terminalAnalysis.setTimeRange(timeToLoadReservedData);
        final int row = terminalAnalysis.findFirstTableRowWhereMatchingAnyValue(MODEL, terminal[1]);
        //        final Map<String, String> result = terminalAnalysis.getAllDataAtTableRow(row);

        terminalAnalysis.clickTableCell(row, TOTAL_DOWNLINK_DATA_VOL_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        int rowIndex = terminalAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI, reservedDataHelperReplacement
                .getReservedData(IMSI));
        Map<String, String> result = terminalAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));

        terminalAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
        waitForPageLoadingToComplete();

        terminalAnalysis.clickTableCell(row, TOTAL_UPLINK_DATA_VOL_KB);
        assertTrue(selenium.isTextPresent("Data Volume Analysis"));

        rowIndex = terminalAnalysis.findFirstTableRowWhereMatchingAnyValue(IMSI, reservedDataHelperReplacement
                .getReservedData(IMSI));
        result = terminalAnalysis.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));

    }

    @Test
    public void drillToFailedEventAnalysisFromTerminalEventAnalysisWindowWithDataIntegrity_8_8() throws Exception {
        final String[] terminal = { reservedDataHelperReplacement.getReservedData(MANUFACTURER),
                reservedDataHelperReplacement.getReservedData(MODEL),
                reservedDataHelperReplacement.getReservedData(TAC) };

        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL, true, terminal);

        //Drill to Failures
        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);
        int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(EVENT_TYPE, "DEACTIVATE");
        terminalEventAnalysisWindow.clickTableCell(rowIndex, "Failures");

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE, QOS_UPGRADE, QOS_DOWNGRADE,
                CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders, DV_THROUGHOUTPUT);

        rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(PDP_DURATION, reservedDataHelperReplacement.getReservedData(PDP_DURATION), result
                .get(PDP_DURATION));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_AVERAGE_USAGES, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_AVERAGE_USAGES), result.get(DOWNLINK_AVERAGE_USAGES));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_AVERAGE_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_USAGE),
                result.get(UPLINK_AVERAGE_USAGE));
        checkDataIntegrity(QOS_UPGRADE, reservedDataHelperReplacement.getReservedData(QOS_UPGRADE), result
                .get(QOS_UPGRADE));
        checkDataIntegrity(QOS_DOWNGRADE, reservedDataHelperReplacement.getReservedData(QOS_DOWNGRADE), result
                .get(QOS_DOWNGRADE));
    }

    @Test
    public void drillToFailedEventAnalysisFromTerminalGroupEventAnalysisWindowWithDataIntegrity_8_9() throws Exception {
        final String[] terminalGroup = { reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP) };
        terminalTab.openEventAnalysisWindow(TerminalType.TERMINAL_GROUP, true, terminalGroup);

        //Drill to Failures
        terminalEventAnalysisWindow.setTimeRange(timeToLoadReservedData);
        terminalEventAnalysisWindow.clickTableCell(0, "Failures");

        final List<String> hiddenHeaders = new ArrayList<String>(Arrays.asList(PDP_DURATION, DOWNLINK_DATA_VOLOUME_KB,
                DOWNLINK_AVERAGE_USAGES, UPLINK_DATA_VOLUME_KB, UPLINK_AVERAGE_USAGE, QOS_UPGRADE, QOS_DOWNGRADE,
                CHARGING_ID));
        checkHeadersAreOptionalAndHiddenByDefault(terminalEventAnalysisWindow, hiddenHeaders, DV_THROUGHOUTPUT);

        final int rowIndex = terminalEventAnalysisWindow.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = terminalEventAnalysisWindow.getAllDataAtTableRow(rowIndex);
        checkDataIntegrity(PDP_DURATION, reservedDataHelperReplacement.getReservedData(PDP_DURATION), result
                .get(PDP_DURATION));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(DOWNLINK_AVERAGE_USAGES, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_AVERAGE_USAGES), result.get(DOWNLINK_AVERAGE_USAGES));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(UPLINK_AVERAGE_USAGE, reservedDataHelperReplacement.getReservedData(UPLINK_AVERAGE_USAGE),
                result.get(UPLINK_AVERAGE_USAGE));
        checkDataIntegrity(QOS_UPGRADE, reservedDataHelperReplacement.getReservedData(QOS_UPGRADE), result
                .get(QOS_UPGRADE));
        checkDataIntegrity(QOS_DOWNGRADE, reservedDataHelperReplacement.getReservedData(QOS_DOWNGRADE), result
                .get(QOS_DOWNGRADE));
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

}
