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

package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt.newui;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.NetworkTabUI;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.TerminalTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.TerminalGroupAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
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

public class DataVolumeRankingTestGroup extends BaseDvdtTest {

    final String DATA_VOLUME_RANKING = "Data Volume Ranking";

    @Autowired
    private TerminalTabUI terminalTab;

    @Autowired
    private NetworkTabUI networkTab;

    @Autowired
    WorkspaceRC workspaceRC;
    @Autowired
    private Selection selection;

    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysisWindow;

    @Autowired
    @Qualifier("subRankingsDataVolume")
    private CommonWindow subscriberRankingDataVolumeWindow;

    @Autowired
    @Qualifier("apnRankingDataVolume")
    private CommonWindow apnRankingDataVolumeWindow;

    @Autowired
    @Qualifier("terminalRankingDataVolume")
    private CommonWindow terminalRankingDataVolumeWindow;

    @Autowired
    @Qualifier("terminalGroupRankingDataVolume")
    private CommonWindow terminalGroupRankingDataVolumeWindow;

    @Autowired
    @Qualifier("ggsnRankingDataVolume")
    private CommonWindow ggsnRankingDataVolumeWindow;

    @Autowired
    @Qualifier("terminalGroupAnalysis")
    private TerminalGroupAnalysisWindow terminalGroupAnalysis;

    @Autowired
    @Qualifier("terminalAnalysis")
    private TerminalGroupAnalysisWindow terminalAnalysis;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of 2G/3G Data Bearer Throughput and Data Volume's Terminal Analysis test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of 2G/3G Data Bearer Throughput and Data Volume's Terminal Analysis test section");
        reservedDataHelperReplacement = null;

    }

    @Test
    public void subscriberRankingDataVolumeWindowFromRankingTab_9_1() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, IMSI);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume subscriber Ranking", selenium.isTextPresent("Data Volume Subscriber Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI, TOTAL_DATA_VOL_KB, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkStringListContainsArray(subscriberRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void subscriberGroupRankingDataVolumeWindowFromRankingTab_9_2() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, IMSI_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume subscriber group Ranking", selenium.isTextPresent("Data Volume Subscriber Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI_GROUP, TOTAL_DATA_VOL_KB, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkStringListContainsArray(subscriberRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void apnRankingDataVolumeWindowFromRankingTab_9_3() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, APN);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume APN Ranking", selenium.isTextPresent("Data Volume APN Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void apnGroupRankingDataVolumeWindowFromRankingTab_9_4() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, APN_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume APN group Ranking", selenium.isTextPresent("Data Volume APN Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN_GROUP, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void terminalRankingDataVolumeWindowFromRankingTab_9_5() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, TERMINAL);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume terminal Ranking", selenium.isTextPresent("Data Volume Terminal Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, TAC, MANUFACTURER, MODEL, TAC, TOTAL_DATA_VOL_MB,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(terminalRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void terminalGroupRankingDataVolumeWindowFromRankingTab_9_6() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, TERMINAL_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume Terminal Group Ranking", selenium.isTextPresent("Data Volume Terminal Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, TERMINAL_GROUP, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(terminalGroupRankingDataVolumeWindow.getTableHeaders(),
                expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void ggsnRankingDataVolumeWindowFromRankingTab_9_7() throws Exception {

        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, GGSN);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume GGSN Ranking", selenium.isTextPresent("Data Volume GGSN Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, GGSN, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(ggsnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void subscriberRankingDataVolumeWindowWithDataIntegrity_9_8() throws Exception {

        selectOptionsForSubscriberAndHeaderChecks();
        final String imsi = reservedDataHelperReplacement.getReservedData(IMSI);

        final int rowIndex = subscriberRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(IMSI, imsi);
        final Map<String, String> result = subscriberRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_KB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_KB), result.get(TOTAL_DATA_VOL_KB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));

    }

    @Test
    public void subscriberGroupRankingDataVolumeWindowWithDataIntegrity_9_9() throws Exception {

        selectOptionsForSubscriberGroupAndHeaderChecks();
        final String imsigroup = reservedDataHelperReplacement.getReservedData(IMSI_GROUP);

        final int rowIndex = subscriberRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(IMSI_GROUP, imsigroup);
        final Map<String, String> result = subscriberRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_KB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_KB), result.get(TOTAL_DATA_VOL_KB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLOUME_KB),
                result.get(DOWNLINK_DATA_VOLOUME_KB));
    }

    @Test
    public void apnRankingDataVolumeWindowWithDataIntegrity_9_10() throws Exception {

        selectOptionsForApnAndHeaderChecks();
        final String apn = reservedDataHelperReplacement.getReservedData(APN);

        final int rowIndex = apnRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(APN, apn);
        final Map<String, String> result = apnRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result.get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
    }

    @Test
    public void apnGroupRankingDataVolumeWindowWithDataIntegrity_9_11() throws Exception {

        selectOptionsForApnGroupAndHeaderChecks();
        final String apngroup = reservedDataHelperReplacement.getReservedData(APN_GROUP);

        final int rowIndex = apnRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(APN_GROUP, apngroup);
        final Map<String, String> result = apnRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result.get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
    }

    @Test
    public void terminalRankingDataVolumeWindowWithDataIntegrity_9_12() throws Exception {

        selectOptionsForTerminalAndHeaderChecks();
        final String tac = reservedDataHelperReplacement.getReservedData(TAC);

        final int rowIndex = terminalRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(TAC, tac);
        final Map<String, String> result = terminalRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result.get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
    }

    @Test
    public void terminalGroupRankingDataVolumeWindowWithDataIntegrity_9_13() throws Exception {

        selectOptionsForTerminalGroupAndHeaderChecks();
        final String tacgroup = reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP);

        final int rowIndex = terminalGroupRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(TERMINAL_GROUP, tacgroup);
        final Map<String, String> result = terminalGroupRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result.get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));

    }

    @Test
    public void ggsnRankingDataVolumeWindowWithDataIntegrity_9_14() throws Exception {

        selectOptionsForGgsnAndheaderChecks();
        final String ggsn = reservedDataHelperReplacement.getReservedData(EVENT_SOURCE_NAME);

        final int rowIndex = ggsnRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(GGSN, ggsn);
        final Map<String, String> result = ggsnRankingDataVolumeWindow.getAllDataAtTableRow(rowIndex);

        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result.get(TOTAL_DATA_VOL_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(DOWNLINK_DATA_VOLUME_MB),
                result.get(DOWNLINK_DATA_VOLUME_MB));

    }

    private void selectOptionsForSubscriberAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, IMSI);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume subscriber Ranking", selenium.isTextPresent("Data Volume Subscriber Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI, TOTAL_DATA_VOL_KB, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkStringListContainsArray(subscriberRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForSubscriberGroupAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, IMSI_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume subscriber group Ranking", selenium.isTextPresent("Data Volume Subscriber Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI_GROUP, TOTAL_DATA_VOL_KB, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB));
        checkStringListContainsArray(subscriberRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForApnAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, APN);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume APN Ranking", selenium.isTextPresent("Data Volume APN Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForApnGroupAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, APN_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume APN group Ranking", selenium.isTextPresent("Data Volume APN Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN_GROUP, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForTerminalAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, TERMINAL);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume terminal Ranking", selenium.isTextPresent("Data Volume Terminal Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, MANUFACTURER, MODEL, TAC, TOTAL_DATA_VOL_MB,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(terminalRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForTerminalGroupAndHeaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, TERMINAL_GROUP);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume Terminal Group Ranking", selenium.isTextPresent("Data Volume Terminal Group Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, TERMINAL_GROUP, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(terminalGroupRankingDataVolumeWindow.getTableHeaders(),
                expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void selectOptionsForGgsnAndheaderChecks() throws PopUpException, InterruptedException {
        selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CORE_NETWORK_PS, false, DATA_VOLUME_RANKING, GGSN);
        openEventAnalysisWindow(selection);
        assertTrue("Can't open Data Volume GGSN Ranking", selenium.isTextPresent("Data Volume GGSN Ranking"));
        waitForPageLoadingToComplete();
        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, GGSN, TOTAL_DATA_VOL_MB, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB));
        checkStringListContainsArray(ggsnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders.toArray(new String[expectedHeaders.size()]));
    }

    private void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        networkTab.openEventAnalysisWindow(selection);
        waitForPageLoadingToComplete();

    }

    private void selectOptions(String timeRange, String dimension, Boolean isNetworkType, String windowCategory, String windowOption,
                               String... values) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setIsNetwork(isNetworkType);
        selection.setTimeRange(timeRange);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);

    }
}
