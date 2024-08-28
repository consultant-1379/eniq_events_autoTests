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
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.imsigroup.coreps;

import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.*;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.GroupCreationUtils;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

public class CoreEventTraceTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow summaryEventAnalysisWindow;
    @Autowired
    @Qualifier("subRankings")
    private CommonWindow subscriberRankingWindow;
    @Autowired
    private GroupManagementWindow groupManagementWindow;
    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of IMSI Group Core Event Analysis Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of IMSI Group Core Event Analysis Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    @Test
    public void drillOnFailuresTimeRangeConsistent30Minutes_ImsiGroup() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void drillOnFailuresTimeRangeConsistent6Hours_ImsiGroup() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.SIX_HOURS);
    }

    @Test
    public void drillOnFailuresTimeRangeConsistent1Week_ImsiGroup() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.ONE_WEEK);
    }

    @Test
    public void drillOnFailuresNoOfFailuresMatch30Minutes_ImsiGroup() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void drillOnFailuresNoOfFailuresMatch6Hours_ImsiGroup() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.SIX_HOURS);
    }

    @Test
    public void drillOnFailuresNoOfFailuresMatch1Week_ImsiGroup() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.ONE_WEEK);
    }

    @Test
    public void totalEventsEqualNoOfSuccessesAndErrors30Minutes_ImsiGroup() throws Exception {
        totalEventsEqualNoOfSuccessesAndErrors(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void totalEventsEqualNoOfSuccessesAndErrors6Hours_ImsiGroup() throws Exception {
        totalEventsEqualNoOfSuccessesAndErrors(TimeRange.SIX_HOURS);
    }

    @Test
    public void totalEventsEqualNoOfSuccessesAndErrors1Week_ImsiGroup() throws Exception {
        totalEventsEqualNoOfSuccessesAndErrors(TimeRange.ONE_WEEK);
    }

    @Test
    public void validSuccessRatio30Minutes_ImsiGroup() throws Exception {
        validSuccessRatio(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void validSuccessRatio6Hours_ImsiGroup() throws Exception {
        validSuccessRatio(TimeRange.SIX_HOURS);
    }

    @Test
    public void validSuccessRatio1Week_ImsiGroup() throws Exception {
        validSuccessRatio(TimeRange.ONE_WEEK);
    }

    @Test
    public void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures30Minutes_ImsiGroup() throws Exception {
        noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures6Hours_ImsiGroup() throws Exception {
        noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange.SIX_HOURS);
    }

    @Test
    public void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures1Week_ImsiGroup() throws Exception {
        noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange.ONE_WEEK);
    }

    @Test
    public void propertiesPopupWindowMatchRow30Minutes_ImsiGroup() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void propertiesPopupWindowMatchRow6Hours_ImsiGroup() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.SIX_HOURS);
    }

    @Test
    public void propertiesPopupWindowMatchRow1Week_ImsiGroup() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.ONE_WEEK);
    }

    // --------- Private Method ----------
    private void drillOnFailures_timeRangeConsistent(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {
            final int eventFailuresFromInitWindow = Integer.parseInt(summaryEventAnalysisWindow.getTableData(rowIndex, 1));
            if (eventFailuresFromInitWindow > 0) {

                dataFound = true;

                String timeFromInitWindow = summaryEventAnalysisWindow.getTimeLabelText();

                pause(SeleniumConstants.DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS);

                summaryEventAnalysisWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);

                final String timeFromDrilldownWindow = summaryEventAnalysisWindow.getTimeLabelText();
                assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);

                summaryEventAnalysisWindow.clickBackwardNavigation();

                timeFromInitWindow = summaryEventAnalysisWindow.getTimeLabelText();
                assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);
                break;
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void drillOnFailures_NoOfFailuresMatch(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);

        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {
            final int failuresIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final int eventFailuresFromInitWindow = Integer.parseInt(summaryEventAnalysisWindow.getTableData(rowIndex, failuresIndex));
            if (eventFailuresFromInitWindow > 0) {

                dataFound = true;

                summaryEventAnalysisWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);

                final int numberOfPages = summaryEventAnalysisWindow.getPageCount();

                int eventFailuresFromDrilldownWindow = 0;

                for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
                    eventFailuresFromDrilldownWindow = eventFailuresFromDrilldownWindow + summaryEventAnalysisWindow.getTableRowCount();
                    summaryEventAnalysisWindow.clickNextPage();
                }

                if (eventFailuresFromInitWindow > 500) {
                    assertTrue("Number of failures in summary window is less than the number of failures in drilldown window.",
                            eventFailuresFromInitWindow > eventFailuresFromDrilldownWindow);
                    assertEquals("Number of failures in drilldown window is not equal to max row count.", 500, eventFailuresFromDrilldownWindow);
                } else {
                    assertEquals("Number of failures does not match number of failures in drilldown window.", eventFailuresFromInitWindow,
                            eventFailuresFromDrilldownWindow);
                }
            }

            if (dataFound) {
                break;
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void totalEventsEqualNoOfSuccessesAndErrors(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {

            dataFound = true;

            final int failuresIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final String noOfFailures = summaryEventAnalysisWindow.getTableData(rowIndex, failuresIndex);
            final int successesIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.SUCCESSES);
            final String noOfSuccesses = summaryEventAnalysisWindow.getTableData(rowIndex, successesIndex);
            final int totalIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.TOTAL);
            final String total = summaryEventAnalysisWindow.getTableData(rowIndex, totalIndex);

            assertEquals("Total number of events(" + total + ") does not match no of successes(" + noOfSuccesses + ") and failures(" + noOfFailures
                    + ").", Integer.parseInt(total), (Integer.parseInt(noOfSuccesses) + Integer.parseInt(noOfFailures)));
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void validSuccessRatio(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);

        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {

            dataFound = true;

            final int totalIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.TOTAL);
            final int successesIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.SUCCESSES);
            final int successRatioIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.SUCCESS_RATIO);

            final double total = Double.parseDouble(summaryEventAnalysisWindow.getTableData(rowIndex, totalIndex));
            final double noOfSuccesses = Double.parseDouble(summaryEventAnalysisWindow.getTableData(rowIndex, successesIndex));
            final double expectedSuccessRatio = Double.parseDouble(summaryEventAnalysisWindow.getTableData(rowIndex, successRatioIndex));

            if (noOfSuccesses == 0) {
                assertTrue("Success Ratio is calculated incorrectly.", expectedSuccessRatio == 0.0);
            } else if (noOfSuccesses == total) {
                assertTrue("Success Ratio is calculated incorrectly.", expectedSuccessRatio == 100);
            } else {
                final double calculatedSuccessRatio = ((noOfSuccesses / total) * 100.0);
                final double actualSuccessRatio = Math.round(calculatedSuccessRatio * 100.0) / 100.0;
                assertEquals("Success Ratio is calculated incorrectly.", expectedSuccessRatio, actualSuccessRatio);
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);

        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {

            dataFound = true;

            final int failuresIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final String noOfFailures = summaryEventAnalysisWindow.getTableData(rowIndex, failuresIndex);
            final int impactedSubscribersIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);
            final String impactedSubscribers = summaryEventAnalysisWindow.getTableData(rowIndex, impactedSubscribersIndex);

            assertTrue("Number of impacted subscribers(" + impactedSubscribers + ") is greater than the number of failures(" + noOfFailures + ").",
                    Integer.parseInt(impactedSubscribers) <= Integer.parseInt(noOfFailures));
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void propertiesPopupWindowMatchRow(final TimeRange timeRange) throws Exception {
        launchWindow();

        summaryEventAnalysisWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);

        boolean dataFound = false;

        for (int rowIndex = 0; rowIndex < summaryEventAnalysisWindow.getTableRowCount(); rowIndex++) {

            dataFound = true;

            final int eventTypeIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.EVENT_TYPE);
            final int failuresIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
            final int successesIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.SUCCESSES);
            final int successRatioIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.SUCCESS_RATIO);
            final int totalIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.TOTAL);
            final int impactedSubscriberIndex = summaryEventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);

            final String eventType = summaryEventAnalysisWindow.getTableData(rowIndex, eventTypeIndex);
            final String failures = summaryEventAnalysisWindow.getTableData(rowIndex, failuresIndex);
            final String successes = summaryEventAnalysisWindow.getTableData(rowIndex, successesIndex);
            final String successRatio = summaryEventAnalysisWindow.getTableData(rowIndex, successRatioIndex);
            final String total = summaryEventAnalysisWindow.getTableData(rowIndex, totalIndex);
            final String impactedSubscribers = summaryEventAnalysisWindow.getTableData(rowIndex, impactedSubscriberIndex);

            assertEquals("Row does not match properties popup window.", eventType,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.EVENT_TYPE));
            assertEquals("Row does not match properties popup window.", failures,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.FAILURES));
            assertEquals("Row does not match properties popup window.", successes,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.SUCCESSES));
            assertEquals("Row does not match properties popup window.", successRatio,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.SUCCESS_RATIO));
            assertEquals("Row does not match properties popup window.", total,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.TOTAL));
            assertEquals("Row does not match properties popup window.", impactedSubscribers,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.IMPACTED_SUBSCRIBERS));
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the Time Range: "
                    + timeRange.getLabel());
        }
    }

    private void launchWindow() throws InterruptedException, PopUpException, NoDataException {
        GroupCreationUtils.makeImsiGroupFromTopFailingImsis(workspace, groupManagementWindow, groupManagementEditWindow, subscriberRankingWindow,
                selenium);
        workspace.selectDimension(SeleniumConstants.IMSI_GROUP);
        workspace.enterDimensionValueForGroups(GroupManagementConstants.topFailingImsisGroupName);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_EVENT_TRACE, GuiStringConstants.LMAW_CORE_PS);
        workspace.launch();
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
    }

    private String findValueOfPropertyFromPropertiesPopupWindow(final int rowIndex, final String propertyName) {
        final String xPathForRowInGrid = "//div[@class='x-grid3-body']/div[" + (rowIndex + 1)
                + "][contains(concat(' ',@class,' '), ' x-grid3-row ')]";
        selenium.click(xPathForRowInGrid);
        summaryEventAnalysisWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);

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
}