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

public class SubscriberOverviewFailuresView extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("subscriberOverview")
    private CommonWindow subscriberOverviewWindow;
    @Autowired
    @Qualifier("subRankings")
    private CommonWindow subscriberRankingWindow;
    @Autowired
    private GroupManagementWindow groupManagementWindow;
    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of IMSI Group Subscriber Overview - Failures Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of IMSI Group Subscriber Overview - Failures Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    /**
     * TSID: 14937 Title: Confirm drill down on bar chart uses the same time range as the original query.
     */
    @Test
    public void confirmDrillDownOnBarChartUsesTheSameTimeRangeAsTheOriginalQueryFor30Minutes_ImsiGroup_EQEV_12588() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.THIRTY_MINUTES);
    }

    /**
     * TSID: 14937 Title: Confirm drill down on bar chart uses the same time range as the original query.
     */
    @Test
    public void confirmDrillDownOnBarChartUsesTheSameTimeRangeAsTheOriginalQueryFor6Hours_ImsiGroup_EQEV_12588() throws Exception {
        drillOnFailures_timeRangeConsistent(TimeRange.SIX_HOURS);
    }

    /**
     * TSID: 14881 Title: Verify Subscriber Overview views are returning Successes for IMSI Group when using IMSI Aggregations.
     */
    @Test
    public void drillOnFailuresNoOfFailuresMatch30Minutes_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.THIRTY_MINUTES);
    }

    /**
     * TSID: 14881 Title: Verify Subscriber Overview views are returning Successes for IMSI Group when using IMSI Aggregations.
     */
    @Test
    public void drillOnFailuresNoOfFailuresMatch6Hours_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.SIX_HOURS);
    }

    /**
     * TSID: 14881 Title: Verify Subscriber Overview views are returning Successes for IMSI Group when using IMSI Aggregations.
     */
    @Test
    public void drillOnFailuresNoOfFailuresMatch1Week_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        drillOnFailures_NoOfFailuresMatch(TimeRange.ONE_WEEK);
    }

    @Test
    public void propertiesPopupWindowMatchRow30Minutes_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.THIRTY_MINUTES);
    }

    @Test
    public void propertiesPopupWindowMatchRow6Hours_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.SIX_HOURS);
    }

    @Test
    public void propertiesPopupWindowMatchRow1Week_IMSI_Group_SubscriberOverview_Failures() throws Exception {
        propertiesPopupWindowMatchRow(TimeRange.ONE_WEEK);
    }

    // ----------------------------- Private Methods --------------------------
    private void drillOnFailures_timeRangeConsistent(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        subscriberOverviewWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        subscriberOverviewWindow.toggleGraphToGrid();

        int eventNameColumnIndex = 0;
        int eventFailuresColumnIndex = 0;
        try {
            eventNameColumnIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_NAME);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Column " + GuiStringConstants.EVENT_NAME + " not found.");
        }

        try {
            eventFailuresColumnIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_FAILURES);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Column " + GuiStringConstants.EVENT_FAILURES + " not found.");
        }

        for (int rowIndex = 0; rowIndex < subscriberOverviewWindow.getTableRowCount(); rowIndex++) {
            String eventName = subscriberOverviewWindow.getTableData(rowIndex, eventNameColumnIndex);
            if (eventName.length() > 10) {
                eventName = eventName.substring(0, 10) + "...";
            }
            final String eventFailures = subscriberOverviewWindow.getTableData(rowIndex, eventFailuresColumnIndex);
            if (Integer.parseInt(eventFailures) > 0) {

                dataFound = true;

                final String timeFromInitWindow = subscriberOverviewWindow.getTimeLabelText();

                pause(SeleniumConstants.DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS);

                subscriberOverviewWindow.toggleGridToGraph();

                subscriberOverviewWindow.drilldownOnBarChartPortion(eventName);
                WorkspaceUtils.pauseUntilWindowLoads(selenium);

                final String timeFromDrilldownWindow = subscriberOverviewWindow.getTimeLabelText();
                assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);

                subscriberOverviewWindow.toggleToUsingView("Failures");
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

        subscriberOverviewWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        boolean dataFound = false;
        subscriberOverviewWindow.toggleGraphToGrid();

        final int eventNameColumnIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_NAME);
        final int eventFailuresColumnIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_FAILURES);
        String eventName = "";
        int eventFailuresFromInitWindow = 0;

        for (int rowIndex = 0; rowIndex < subscriberOverviewWindow.getTableRowCount(); rowIndex++) {
            eventFailuresFromInitWindow = Integer.parseInt(subscriberOverviewWindow.getTableData(rowIndex, eventFailuresColumnIndex));
            if (eventFailuresFromInitWindow > 0) {
                dataFound = true;
                eventName = subscriberOverviewWindow.getTableData(rowIndex, eventNameColumnIndex);
                if (eventName.length() > 10) {
                    eventName = eventName.substring(0, 10) + "...";
                }
                break;
            }
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the time range: "
                    + timeRange.getMiniutes());
        }

        subscriberOverviewWindow.toggleGridToGraph();

        subscriberOverviewWindow.drilldownOnBarChartPortion(eventName);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);

        final int numberOfPages = subscriberOverviewWindow.getPageCount();

        int eventFailuresFromDrilldownWindow = 0;

        for (int pageIndex = 0; pageIndex < numberOfPages; pageIndex++) {
            eventFailuresFromDrilldownWindow = eventFailuresFromDrilldownWindow + subscriberOverviewWindow.getTableRowCount();
            subscriberOverviewWindow.clickNextPage();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
        }

        if (eventFailuresFromInitWindow > 100) {
            assertTrue("Number of failures in summary window is less than the number of failures in drilldown window.",
                    eventFailuresFromInitWindow > eventFailuresFromDrilldownWindow);
            assertEquals("Number of failures in drilldown window is not equal to max row count.", 100, eventFailuresFromDrilldownWindow);
        } else {
            assertEquals("Number of failures does not match number of failures in drilldown window.", eventFailuresFromInitWindow,
                    eventFailuresFromDrilldownWindow);
        }

        subscriberOverviewWindow.toggleToUsingView("Failures");

    }

    private void propertiesPopupWindowMatchRow(final TimeRange timeRange) throws Exception {
        launchWindow();

        boolean dataFound = false;

        subscriberOverviewWindow.setTimeRange(timeRange);
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
        subscriberOverviewWindow.toggleGraphToGrid();

        final int eventNameIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_NAME);
        final int eventFailuresIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_FAILURES);
        final int eventSuccessesIndex = subscriberOverviewWindow.getTableHeaderIndex(GuiStringConstants.EVENT_SUCCESS);

        for (int rowIndex = 0; rowIndex < subscriberOverviewWindow.getTableRowCount(); rowIndex++) {
            dataFound = true;

            String eventName = "";
            String eventFailures = "";
            String eventSuccesses = "";

            try {
                eventName = subscriberOverviewWindow.getTableData(rowIndex, eventNameIndex);
                eventFailures = subscriberOverviewWindow.getTableData(rowIndex, eventFailuresIndex);
                eventSuccesses = subscriberOverviewWindow.getTableData(rowIndex, eventSuccessesIndex);
            } catch (final ArrayIndexOutOfBoundsException e) {
                fail("Column not found in window.");
            }

            assertEquals("Row does not match properties popup window.", eventName,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.EVENT_NAME));
            assertEquals("Row does not match properties popup window.", eventFailures,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.EVENT_FAILURES));
            assertEquals("Row does not match properties popup window.", eventSuccesses,
                    findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.EVENT_SUCCESS));
        }

        if (!dataFound) {
            throw new NoDataException("No Data Found For IMSI Group: " + GroupManagementConstants.topFailingImsisGroupName + " for the time range: "
                    + timeRange.getMiniutes());
        }
    }

    private void launchWindow() throws InterruptedException, PopUpException, NoDataException {
        GroupCreationUtils.makeImsiGroupFromTopFailingImsis(workspace, groupManagementWindow, groupManagementEditWindow, subscriberRankingWindow,
                selenium);
        workspace.selectDimension(SeleniumConstants.IMSI_GROUP);
        workspace.enterDimensionValueForGroups(GroupManagementConstants.topFailingImsisGroupName);
        workspace.selectWindowType(GuiStringConstants.LMAW_SUBSCRIBER_OVERVIEW, GuiStringConstants.LMAW_CORE_PS);
        workspace.launch();
        WorkspaceUtils.pauseUntilWindowLoads(selenium);
    }

    private String findValueOfPropertyFromPropertiesPopupWindow(final int rowIndex, final String propertyName) {
        final String xPathForRowInGrid = "//div[@class='x-grid3-body']/div[" + (rowIndex + 1)
                + "][contains(concat(' ',@class,' '), ' x-grid3-row ')]";
        selenium.click(xPathForRowInGrid);
        subscriberOverviewWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);

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