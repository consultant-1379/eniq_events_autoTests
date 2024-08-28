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
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.TerminalTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class TerminalAnalysisTestGroup extends BaseDvdtTest {

    public static final String CORE_TERMINAL_EVENT_ANALYSIS = "Core Terminal Event Analysis";
    public static final String CORE_TERMINAL_GROUP_ANALYSIS = "Core Terminal Group Analysis";
    public static final String CORE_PS = "Core PS";
    public static final String CORE_TERMINAL_ANALYSIS = "Core Terminal Analysis";
    public static final String DATA_VOLUME = "Data Volume";
    SelectedButtonType button = SelectedButtonType.TOGGLE_BUTTON;

    @Autowired
    private TerminalTabUI terminalTab;

    @Autowired
    WorkspaceRC workspaceRC;
    @Autowired
    private Selection selection;

    @Autowired
    @Qualifier("terminalRankingDataVolume")
    private CommonWindow terminalRankingDataVolumeWindow;

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
    public void verifyTerminalDataVolumeChart_7_21() throws Exception {

        final String Manufacturer = reservedDataHelperReplacement.getReservedData(MANUFACTURER);
        final String TerminalModel = reservedDataHelperReplacement.getReservedData(MODEL);
        final String tac = reservedDataHelperReplacement.getReservedData(TAC);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.TERMINAL, Manufacturer, DATA_VOLUME, CORE_PS, true, TerminalModel, tac);
        terminalTab.openEventAnalysisWindow1(selection);
        assertTrue("Can't load TERMINAL Data Volume window", selenium.isTextPresent("Terminal - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("Terminal", terminalRankingDataVolumeWindow);
        terminalRankingDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    @Test
    public void verifyTerminalGroupDataVolumeChart_9_13() throws Exception {

        final String tacGroup = reservedDataHelperReplacement.getReservedData(TERMINAL_GROUP);
        selectOptions(SeleniumConstants.DATE_TIME_2HOUR, SeleniumConstants.TERMINAL_GROUP, tacGroup, DATA_VOLUME, CORE_PS, true, null, null);
        terminalTab.openEventAnalysisWindow1(selection);
        assertTrue("Can't load TERMINAL Group Data Volume window", selenium.isTextPresent("Terminal Group - Data Volume"));
        waitForPageLoadingToComplete();
        checkWindowUpdatedForTimeRanges("Terminal Group", terminalRankingDataVolumeWindow);
        terminalRankingDataVolumeWindow.toggleGraphToGrid();
        waitForPageLoadingToComplete();
    }

    /**
     * Check time range, the time ranges is read from properties file //ewandaf
     * 
     * @param window the object of CommonWindow
     * @param values These values will compare with the values on "columnToCheck"
     * @param columnName the name of column where the link is
     */

    private void openEventAnalysisWindow(final Selection selection) throws PopUpException, InterruptedException {
        terminalTab.openEventAnalysisWindow(selection);

        if (selection.getIsGroup()) {

            assertTrue("Can't open " + selection.getDimensionValue() + " - Terminal Event Analysis",
                    selenium.isTextPresent("Terminal Group - Event Analysis"));//changed
        } else {

            assertTrue("Can't open Terminal Event Analysis", selenium.isTextPresent("Terminal Event Analysis"));
        }

        waitForPageLoadingToComplete();
    }

    private void selectOptions(String timeRange, String dimension, String dimensionValue, String windowCategory, String windowOption,
                               boolean isTerminalType, String... values) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
        selection.setIsTerminal(isTerminalType);
        if (values != null) {
            selection.setTerminalModel(values[0]);
            selection.setTac(values[1]);
        }
    }

    private boolean isGroupDimension(String dimension) {
        return dimension.equals(SeleniumConstants.CONTROLLER_GROUP) || dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)
                || dimension.equals(SeleniumConstants.SGSN_MME_GROUP) || dimension.equals(SeleniumConstants.MSC_GROUP)
                || dimension.equals(SeleniumConstants.TERMINAL_GROUP) || dimension.equals(SeleniumConstants.IMSI_GROUP)
                || dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
    }

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
                    terminalRankingDataVolumeWindow.isButtonEnabled(button));
        }
    }

    private void ChechForValidTime(final TimeRange time, final CommonWindow commonWindow) throws NoDataException, PopUpException {
        commonWindow.setTimeRange(time);
        waitForPageLoadingToComplete();
        assertFalse(time.getLabel() + " is NOT a vaild setting for the Time Dialog", selenium.isTextPresent("Time Settings"));
    }

}
