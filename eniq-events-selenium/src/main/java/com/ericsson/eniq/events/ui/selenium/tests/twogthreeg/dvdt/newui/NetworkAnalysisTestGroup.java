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

import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.*;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.NetworkTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;

public class NetworkAnalysisTestGroup extends BaseDvdtTest {

    public static final String DATA_VOLUME_RANKING = "Data Volume Ranking";
    public static final String CORE_PS = "Core PS";
    public static final String NETWORK_DATA_VOLUME = "Network Data Volume";

    public static final String DATA_VOLUME = "Data Volume";
    SelectedButtonType button = SelectedButtonType.TOGGLE_BUTTON;

    @Autowired
    private Selection selection;

    @Autowired
    public NetworkTabUI netwotkTab;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;

    @Autowired
    @Qualifier("networkDataVolumeWindow")
    private CommonWindow networkDataVolumeWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of 2G/3G Data Bearer Throughput and Data Volume's Network Analysis test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of 2G/3G Data Bearer Throughput and Data Volume's Network Analysis test section");
        reservedDataHelperReplacement = null;
    }

    @Test
    public void verifyApnDataVolumeChart_7_19() throws Exception {

        final String apn = reservedDataHelperReplacement.getReservedData(APN);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.APN, apn, DATA_VOLUME, CORE_PS, true);
        openNETWORK_DATA_VOLUME(selection);
        assertTrue("Can't load APN Data Volume window", selenium.isTextPresent("APN - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("APN", networkDataVolumeWindow);
        networkDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    @Test
    public void verifyApnGroupDataVolumeChart_9_11() throws Exception {

        final String apnGroup = reservedDataHelperReplacement.getReservedData(APN_GROUP);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.APN_GROUP, apnGroup, DATA_VOLUME, CORE_PS, true);
        openNETWORK_DATA_VOLUME(selection);
        assertTrue("Can't load APN Group Data Volume window", selenium.isTextPresent("APN Group - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("APN Group", networkDataVolumeWindow);
        networkDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    @Test
    public void verifySubscriberDataVolumeChart_7_20() throws Exception {

        final String imsi = reservedDataHelperReplacement.getReservedData(IMSI);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.IMSI, imsi, DATA_VOLUME, CORE_PS, true);
        openNETWORK_DATA_VOLUME(selection);
        assertTrue("Can't load IMSI Data Volume window", selenium.isTextPresent("IMSI - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("IMSI", networkDataVolumeWindow);
        networkDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    @Test
    public void verifySubscriberGroupDataVolumeChart_9_9() throws Exception {

        final String imsiGroup = reservedDataHelperReplacement.getReservedData(IMSI_GROUP);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.IMSI_GROUP, imsiGroup, DATA_VOLUME, CORE_PS, true);
        openNETWORK_DATA_VOLUME(selection);
        assertTrue("Can't load IMSI Group Data Volume window", selenium.isTextPresent("IMSI Group - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("IMSI Group", networkDataVolumeWindow);
        networkDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    @Test
    public void verifyMsisdnDataVolumeChart_7_21() throws Exception {

        final String msisdn = reservedDataHelperReplacement.getReservedData(MSISDN);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.MSISDN, msisdn, DATA_VOLUME, CORE_PS, true);
        openNETWORK_DATA_VOLUME(selection);
        assertTrue("Can't load MSISDN Data Volume window", selenium.isTextPresent("MSISDN - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("MSISDN", networkDataVolumeWindow);
        networkDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();

    }

    private void openNETWORK_DATA_VOLUME(final Selection selection) throws PopUpException, InterruptedException {
        netwotkTab.openEventAnalysisWindow(selection);
        waitForPageLoadingToComplete();
    }

    private void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        netwotkTab.openEventAnalysisWindow(selection);

        if (selection.getIsGroup()) {
            assertTrue("Can't open " + selection.getDimensionValue() + " - APN Event Analysis", selenium.isTextPresent("APN Group - Event Analysis"));

        } else {

            assertTrue("Can't open " + selection.getDimensionValue() + " - APN - Event Analysis", selenium.isTextPresent("APN - Event Analysis"));

        }

        waitForPageLoadingToComplete();
    }

    private void openDATA_VOLUMEEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        netwotkTab.openEventAnalysisWindow(selection);

        waitForPageLoadingToComplete();
    }

    private void selectOptions(String timeRange, String dimension, String dimensionValue, String windowCategory, String windowOption,
                               boolean isNetworkType, String... values

    ) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
        selection.setIsNetwork(isNetworkType);

    }

    private boolean isGroupDimension(String dimension) {
        return dimension.equals(SeleniumConstants.CONTROLLER_GROUP) || dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)
                || dimension.equals(SeleniumConstants.SGSN_MME_GROUP) || dimension.equals(SeleniumConstants.MSC_GROUP)
                || dimension.equals(SeleniumConstants.APN_GROUP) || dimension.equals(SeleniumConstants.IMSI_GROUP)
                || dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
    }

    /**
     * Check time range, the time ranges is read from properties file
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */
    private void checkWindowUpdatedForTimeRanges(final String networkType, final CommonWindow commonWindow) throws NoDataException, PopUpException {

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

        int j = 0;

        for (final TimeRange time : timeRanges) {
            ChechForValidTime(time, commonWindow);
            assertTrue("No data available in " + networkType + " Data Volume window for " + allTimeLabel.split(",")[j++] + " time range selection",
                    networkDataVolumeWindow.isButtonEnabled(button));
        }
    }

    private void ChechForValidTime(final TimeRange time, final CommonWindow commonWindow) throws NoDataException, PopUpException {
        commonWindow.setTimeRange(time);
        waitForPageLoadingToComplete();
        assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
    }
}
