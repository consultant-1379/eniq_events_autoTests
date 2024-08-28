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
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsn.coreps;

import java.util.List;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

public class QOSTests extends EniqEventsUIBaseSeleniumTest {

    private static final String SGSN_VALUE = "TCGLMME05";
    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("networkQosStatisticsAnalysis")
    private CommonWindow qosWindow;

    private String sgsn = "";

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SGSN QOS Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SGSN QOS Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        sgsn = findSGSNFromSubscriberRanking_DrillOnFailures_Window();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    /**
     * TSID: 14811 - Action 8 Title: Verify SGSN services query update for Quality of Service.
     */
    @Test
    public void drillOnFailuresTimeRangeConsistent1Week_EQEV_8549_SgsnQos() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.ONE_WEEK);
    }

    /**
     * TSID: 14811 - Action 8 Title: Verify SGSN services query update for Quality of Service.
     */
    @Test
    public void drillOnFailuresNoOfFailuresMatch1Week_EQEV_8549_SgsnQos() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.ONE_WEEK);
    }

    /**
     * TSID: 14811 - Action 8 Title: Verify SGSN services query update for Quality of Service.
     */
    @Test
    public void drillOnFailuresQCIMatch1Week_EQEV_8549_SgsnQos() throws Exception {
        drillOnFailures_QCIMatch(TimeRange.ONE_WEEK);
    }

    /**
     * TSID: 14811 - Action 8 Title: Verify SGSN services query update for Quality of Service.
     */
    @Test
    public void propertiesPopupWindowMatchesRow1Week_EQEV_8549_SgsnQos() throws Exception {
        propertiesPopupWindowMatchesRow(TimeRange.ONE_WEEK);
    }

    /**
     * TSID: 14811 - Action 8 Title: Verify SGSN services query update for Quality of Service.
     */
    @Test
    public void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures1Week_EQEV_8549_SgsnQos() throws Exception {
        noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange.ONE_WEEK);
    }

    // ---------- Private Methods ----------
    private void drillOnFailures_timeRangeConsistent(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;
        qosWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        final int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);

        for (int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++) {
            final String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
            if (Integer.parseInt(noOfFailures) > 0) {
                dataFound = true;
                String timeFromInitWindow = qosWindow.getTimeLabelText();

                pause(SeleniumConstants.DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS);

                qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);

                final String timeFromDrilldownWindow = qosWindow.getTimeLabelText();
                assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);

                qosWindow.clickBackwardNavigation();

                timeFromInitWindow = qosWindow.getTimeLabelText();
                assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);
                break;
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For SGSN: " + SGSN_VALUE + " for the Time Range: " + timeRange.getLabel());
        }
    }

    private void drillOnFailures_NoOfFailuresMatch(final TimeRange timeRange) throws Exception {

        launchWindow();

        boolean dataFound = false;
        qosWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++) {

            final int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);

            if (Integer.parseInt(noOfFailures) > UIConstants.MAX_ROWS_RETURNED) {
                noOfFailures = "" + UIConstants.MAX_ROWS_RETURNED;
            }

            if (Integer.parseInt(noOfFailures) > 0) {

                dataFound = true;

                qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);
                int drilldownNoOfFailures = 0;
                WorkspaceUtils.tickSelectAllQCIColumns(qosWindow, selenium);

                final List<String> data = qosWindow.getAllPagesDataAtColumn(GuiStringConstants.QCI_ERR + " " + (rowIndex + 1));

                for (final String value : data) {
                    drilldownNoOfFailures = drilldownNoOfFailures + Integer.parseInt(value);
                }
                WorkspaceUtils.unTickSelectAllQCIColumns(qosWindow, selenium);

                assertEquals("Number of failures does not match number of failures in drilldown window.\n"
                        + "Number of Failures from Parent Window: " + noOfFailures + ".\n" + "Number of Failures in drilled windows: "
                        + drilldownNoOfFailures + ".\n", Integer.parseInt(noOfFailures), drilldownNoOfFailures);

                qosWindow.clickBackwardNavigation();
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For SGSN: " + SGSN_VALUE + " for the Time Range: " + timeRange.getLabel());
        }
    }

    private void drillOnFailures_QCIMatch(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        qosWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++) {
            final int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
            if (Integer.parseInt(noOfFailures) > 0) {

                dataFound = true;

                qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);

                WorkspaceUtils.tickSelectAllQCIColumns(qosWindow, selenium);
                final int qciHeaderIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.QCI_ERR + " " + (rowIndex + 1));
                final String qciValue = qosWindow.getTableData(0, qciHeaderIndex);

                WorkspaceUtils.unTickSelectAllQCIColumns(qosWindow, selenium);

                assertTrue("QCI value is incorrect.", Integer.parseInt(qciValue) > 0);

                qosWindow.clickBackwardNavigation();
                WorkspaceUtils.pauseUntilWindowLoads(selenium);
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For SGSN: " + SGSN_VALUE + " for the Time Range: " + timeRange.getLabel());
        }
    }

    private void propertiesPopupWindowMatchesRow(final TimeRange timeRange) throws Exception {
        launchWindow();

        qosWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++) {
            final int qosIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.QOS);
            final int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final int successesIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.SUCCESSES);
            final int impactedSubscriberIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);

            final String qos = qosWindow.getTableData(rowIndex, qosIndex);
            final String failures = qosWindow.getTableData(rowIndex, failuresIndex);
            final String successes = qosWindow.getTableData(rowIndex, successesIndex);
            final String impactedSubscribers = qosWindow.getTableData(rowIndex, impactedSubscriberIndex);

            assertEquals("Row does not match properties popup window.", Integer.toString(rowIndex + 1),
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.QCI_ID));
            assertEquals("Row does not match properties popup window.", qos,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.QOS));
            assertEquals("Row does not match properties popup window.", failures,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.FAILURES));
            assertEquals("Row does not match properties popup window.", successes,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.SUCCESSES));
            assertEquals("Row does not match properties popup window.", impactedSubscribers,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.IMPACTED_SUBSCRIBERS));
        }
    }

    private void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        qosWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++) {

            dataFound = true;

            final int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
            final int impactedSubscribersIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);
            final String impactedSubscribers = qosWindow.getTableData(rowIndex, impactedSubscribersIndex);

            assertTrue("Number of impacted subscribers(" + impactedSubscribers + ") is greater than the number of failures(" + noOfFailures + ").",
                    Integer.parseInt(impactedSubscribers) <= Integer.parseInt(noOfFailures));
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For SGSN: " + SGSN_VALUE + " for the Time Range: " + timeRange.getLabel());
        }
    }

    private void launchWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.SGSN_MME);
        workspace.enterDimensionValue(SGSN_VALUE);
        workspace.selectWindowType(GuiStringConstants.QOS, GuiStringConstants.LMAW_CORE_PS);
        workspace.launch();
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
    }

    private String findValueOfPropertyFromPropertiesPopupWindow(final int rowIndex, final String propertyName) {
        final String xPathForRowInGrid = "//div[@class='x-grid3-body']/div[" + (rowIndex + 1)
                + "][contains(concat(' ',@class,' '), ' x-grid3-row ')]";
        selenium.click(xPathForRowInGrid);
        qosWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);

        final String xPathForPropertiesPopup = "//*[@id='selenium_tag_PropertiesWindow']//div[contains(concat(' ',@class,' '), ' contentPanel ')]";
        final String[] propertyNamesAndValues = selenium.getText(xPathForPropertiesPopup).split("\n\n");
        String propertyValue = null;
        for (final String propertyNameAndValue : propertyNamesAndValues) {
            if (propertyNameAndValue.startsWith(propertyName)) {
                propertyValue = propertyNameAndValue.split(propertyName + ": ")[1];
                break;
            }
        }

        selenium.click("//*[@id='selenium_tag_PropertiesWindow']//div[contains(concat(' ',@class,' '), ' closeIcon ')]");

        return propertyValue;
    }

    private String findSGSNFromSubscriberRanking_DrillOnFailures_Window() {
        String sgsn = "";
        try {
            // Open the SGSN Ranking grid.
            workspace.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);

            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);

            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            // Drill on the Top Failing IMSI.
            selenium.click("//div[contains(@id, 'SUBSCRIBER_RANKING_CORE_x-auto')][1]//span[@id='SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED']");
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            // Sort the SGSN-MME Column to get a value.
            selenium.click("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//span[text()='SGSN-MME']");
            sgsn = selenium
                    .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//div[contains(@id,'SUBSCRIBER_EVENT_ANALYSIS_x-auto-')][1]//td//span[@id='SUBSCRIBER_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_SGSN'][text()]");

            // Close the open ranking grids.
            final String closeWindowButton = "//div[contains(@class, 'x-tool-close')]";
            while (selenium.isElementPresent(closeWindowButton)) {
                selenium.click(closeWindowButton);
            }
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sgsn;
    }
}