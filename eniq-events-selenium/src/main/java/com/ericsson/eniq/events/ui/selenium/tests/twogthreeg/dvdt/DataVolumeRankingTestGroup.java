/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
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
public class DataVolumeRankingTestGroup extends BaseDvdtTest {

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    private SubscriberTab subscriberTab;

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("subRankingsDataVolume")
    private CommonWindow subscriberRankingsDataVolume;

    @Autowired
    @Qualifier("subGroupRankingsDataVolume")
    private CommonWindow subscriberGroupRankingsDataVolume;

    @Autowired
    @Qualifier("terminalRankingDataVolume")
    private CommonWindow terminalRankingDataVolumeWindow;

    @Autowired
    @Qualifier("terminalGroupRankingDataVolume")
    private CommonWindow terminalGroupRankingDataVolumeWindow;

    @Autowired
    @Qualifier("sgsnMmeRankingDataVolume")
    private CommonWindow sgsnMmeRankingDataVolumeWindow;

    @Autowired
    @Qualifier("sgsnMmeGroupRankingDataVolume")
    private CommonWindow sgsnMmeGroupRankingDataVolumeWindow;

    @Autowired
    @Qualifier("ggsnRankingDataVolume")
    private CommonWindow ggsnRankingDataVolumeWindow;

    @Autowired
    @Qualifier("apnRankingDataVolume")
    private CommonWindow apnRankingDataVolumeWindow;

    @Autowired
    @Qualifier("apnGroupRankingDataVolume")
    private CommonWindow apnGroupRankingDataVolumeWindow;

    @Autowired
    @Qualifier("qosClassRankingDataVolume")
    private CommonWindow qosClassRankingDataVolumeWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of 2G/3G Data Bearer Throughput and Data Volume's Ranking Analysis test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of 2G/3G Data Bearer Throughput and Data Volume's Ranking Analysis test section");
        reservedDataHelperReplacement = null;
    }

    @Test
    public void subscriberRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_1() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_SUBSCRIBER);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB, TOTAL_DATA_VOL_KB));
        checkStringListContainsArray(subscriberRankingsDataVolume.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        subscriberRankingsDataVolume.setTimeRange(timeToLoadReservedData);
        final int row = subscriberRankingsDataVolume.findFirstTableRowWhereMatchingAnyValue(IMSI,
                reservedDataHelperReplacement.getReservedData(IMSI));
        final Map<String, String> result = subscriberRankingsDataVolume.getAllDataAtTableRow(row);
        checkDataIntegrity(IMSI, reservedDataHelperReplacement.getReservedData(IMSI), result.get(IMSI));
        checkDataIntegrity(DOWNLINK_DATA_VOLOUME_KB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLOUME_KB), result.get(DOWNLINK_DATA_VOLOUME_KB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_KB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_KB),
                result.get(UPLINK_DATA_VOLUME_KB));
        checkDataIntegrity(TOTAL_DATA_VOL_KB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_KB), result
                .get(TOTAL_DATA_VOL_KB));
    }

    @Test
    public void subscriberRankingDataVolumeWindowFromSubscriberTab_9_2() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubOfSubStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                SubscriberTab.SubStartMenu.DATA_VOLUME_RANKING,
                SubscriberTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_SUBSCRIBER);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI, DOWNLINK_DATA_VOLOUME_KB,
                UPLINK_DATA_VOLUME_KB, TOTAL_DATA_VOL_KB));
        checkStringListContainsArray(subscriberRankingsDataVolume.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void subscriberGroupRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_3() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_SUBSCRIBER_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(subscriberGroupRankingsDataVolume.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        subscriberGroupRankingsDataVolume.setTimeRange(timeToLoadReservedData);
        final int row = subscriberGroupRankingsDataVolume.findFirstTableRowWhereMatchingAnyValue(IMSI_GROUP,
                reservedDataHelperReplacement.getReservedData(IMSI_GROUP));
        final Map<String, String> result = subscriberGroupRankingsDataVolume.getAllDataAtTableRow(row);
        checkDataIntegrity(IMSI_GROUP, reservedDataHelperReplacement.getReservedData(IMSI_GROUP), result
                .get(IMSI_GROUP));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));

    }

    @Test
    public void subscriberGroupRankingDataVolumeWindowFromSubsriberTab_9_4() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openSubOfSubStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS,
                SubscriberTab.SubStartMenu.DATA_VOLUME_RANKING,
                SubscriberTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_SUBSCRIBER_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, IMSI_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(subscriberGroupRankingsDataVolume.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void terminalRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_5() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_TERMINAL);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, MANUFACTURER, MODEL, TAC,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(terminalRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        terminalRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = terminalRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(MODEL,
                reservedDataHelperReplacement.getReservedData(MODEL));
        final Map<String, String> result = terminalRankingDataVolumeWindow.getAllDataAtTableRow(row);
        checkDataIntegrity(MANUFACTURER, reservedDataHelperReplacement.getReservedData(MANUFACTURER), result
                .get(MANUFACTURER));
        checkDataIntegrity(MODEL, reservedDataHelperReplacement.getReservedData(MODEL), result.get(MODEL));
        checkDataIntegrity(TAC, reservedDataHelperReplacement.getReservedData(TAC), result.get(TAC));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));

    }

    @Test
    public void terminalRankingDataVolumeWindowFromTerminalTab_9_6() throws Exception {
        terminalTab.openTab();
        terminalTab.openSubOfSubStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS,
                TerminalTab.SubStartMenu.DATA_VOLUME_RANKING,
                TerminalTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_TERMINAL);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, MANUFACTURER, MODEL, TAC,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(terminalRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void terminalGroupRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_7() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_TERMINAL_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, TERMINAL_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(terminalGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        terminalGroupRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = terminalGroupRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(TERMINAL_GROUP,
                reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP));
        final Map<String, String> result = terminalGroupRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));
    }

    @Test
    public void terminalGroupRankingDataVolumeWindowFromTerminalTab_9_8() throws Exception {
        terminalTab.openTab();
        terminalTab.openSubOfSubStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS,
                TerminalTab.SubStartMenu.DATA_VOLUME_RANKING,
                TerminalTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_TERMINAL_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, TERMINAL_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(terminalGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void SGSNandMMERankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_9() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_SGSN_MME);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, SGSN_MME,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(sgsnMmeRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        sgsnMmeRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = sgsnMmeRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(SGSN_MME,
                reservedDataHelperReplacement.getReservedData(SGSN));
        final Map<String, String> result = sgsnMmeRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));
    }

    @Test
    public void SGSNandMMERankingDataVolumeWindowFromNetworkTab_9_10() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_SGSN_MME);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, SGSN_MME,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(sgsnMmeRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void SGSNandMMEGroupRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_11() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_SGSN_MME_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, SGSN_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(sgsnMmeGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        sgsnMmeGroupRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = sgsnMmeGroupRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(SGSN_GROUP,
                reservedDataHelperReplacement.getReservedData(SGSN_GROUP));
        final Map<String, String> result = sgsnMmeGroupRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));
    }

    @Test
    public void SGSNandMMEGroupRankingDataVolumeWindowFromNetworkTab_9_12() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_SGSN_MME_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, SGSN_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(sgsnMmeGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void GGSNRankingDataVolumeWindowFromRankingTab_9_13() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_GGSN);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, GGSN, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(ggsnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

    }

    @Test
    public void GGSNRankingDataVolumeWindowFromNetworkTab_9_14() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_GGSN);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, GGSN, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(ggsnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void APNRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_15() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_APN);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        apnRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = apnRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(APN,
                reservedDataHelperReplacement.getReservedData(APN));
        final Map<String, String> result = apnRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));
    }

    @Test
    public void APNRankingDataVolumeWindowFromNetworkTab_9_16() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_APN);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN, DOWNLINK_DATA_VOLUME_MB,
                UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(apnRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void APNGroupRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_17() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_APN_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(apnGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        apnGroupRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = apnGroupRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(APN_GROUP,
                reservedDataHelperReplacement.getReservedData(APN_GROUP));
        final Map<String, String> result = apnGroupRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));

    }

    @Test
    public void APNGroupRankingDataVolumeWindowFromNetworkTab_9_18() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_APN_GROUP);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, APN_GROUP,
                DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB, TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(apnGroupRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

    @Test
    public void QoSClassRankingDataVolumeWindowFromRankingTabWithDataIntegrity_9_19() throws Exception {
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.DATA_VOLUME_RANKING,
                RankingsTab.SubStartMenu.DATA_VOLUME_RANKING_QOS_CLASS);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, QOS_MEANTPUT, QOS_PEAKTPUT,
                QOS_RELIABILITY, QOS_DELAY, QOS_PRECEDENCE, DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB,
                TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(qosClassRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));

        //data integrity check
        qosClassRankingDataVolumeWindow.setTimeRange(timeToLoadReservedData);
        final int row = qosClassRankingDataVolumeWindow.findFirstTableRowWhereMatchingAnyValue(QOS_MEANTPUT,
                reservedDataHelperReplacement.getReservedData(QOS_MEANTPUT));
        final Map<String, String> result = qosClassRankingDataVolumeWindow.getAllDataAtTableRow(row);

        checkDataIntegrity(QOS_PEAKTPUT, reservedDataHelperReplacement.getReservedData(QOS_PEAKTPUT), result
                .get(QOS_PEAKTPUT));
        checkDataIntegrity(QOS_RELIABILITY, reservedDataHelperReplacement.getReservedData(QOS_RELIABILITY), result
                .get(QOS_RELIABILITY));
        checkDataIntegrity(QOS_DELAY, reservedDataHelperReplacement.getReservedData(QOS_DELAY), result.get(QOS_DELAY));
        checkDataIntegrity(QOS_PRECEDENCE, reservedDataHelperReplacement.getReservedData(QOS_PRECEDENCE), result
                .get(QOS_PRECEDENCE));
        checkDataIntegrity(DOWNLINK_DATA_VOLUME_MB, reservedDataHelperReplacement
                .getReservedData(DOWNLINK_DATA_VOLUME_MB), result.get(DOWNLINK_DATA_VOLUME_MB));
        checkDataIntegrity(UPLINK_DATA_VOLUME_MB, reservedDataHelperReplacement.getReservedData(UPLINK_DATA_VOLUME_MB),
                result.get(UPLINK_DATA_VOLUME_MB));
        checkDataIntegrity(TOTAL_DATA_VOL_MB, reservedDataHelperReplacement.getReservedData(TOTAL_DATA_VOL_MB), result
                .get(TOTAL_DATA_VOL_MB));

    }

    @Test
    public void QoSClassRankingDataVolumeWindowFromNetworkTab_9_20() throws Exception {
        networkTab.openTab();
        networkTab.openSubOfSubStartMenu(NetworkTab.StartMenu.RANKINGS, NetworkTab.SubStartMenu.DATA_VOLUME_RANKING,
                NetworkTab.SubOfSubStartMenu.DATA_VOLUME_RANKING_QOS_CLASS);

        final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, QOS_MEANTPUT, QOS_PEAKTPUT,
                QOS_RELIABILITY, QOS_DELAY, QOS_PRECEDENCE, DOWNLINK_DATA_VOLUME_MB, UPLINK_DATA_VOLUME_MB,
                TOTAL_DATA_VOL_MB));
        checkStringListContainsArray(qosClassRankingDataVolumeWindow.getTableHeaders(), expectedHeaders
                .toArray(new String[expectedHeaders.size()]));
    }

}
