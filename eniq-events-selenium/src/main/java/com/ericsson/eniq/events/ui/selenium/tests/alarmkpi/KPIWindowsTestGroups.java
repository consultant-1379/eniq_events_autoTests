/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.alarmkpi;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.sybase.jdbc3.jdbc.Convert;

public class KPIWindowsTestGroups extends DefaultDataLibrary {

    private int startyear = 0;
    private int startmonth = 0;
    private int startdate = 0;
    private int endYear = 0;
    private int endMonth = 0;
    private int endDate = 0;
    private int month = 0;
    private int hour = 0;
    private int startTimeHour = 0;
    private int endTimeHour = 0;
    private String dataTime = null;
    private String elementIdBefore = null;
    private String elementIdAfter = null;
    private String startTimeCandidate = null;
    private String endTimeCandidate = null;

    static Calendar cal = Calendar.getInstance();

    static Logger logger = Logger.getLogger(KPIWindowsTestGroups.class.getName());

    /*
     * KPI_2_EE_4.0: Verify the EE release version and the license version of KPI Threshold Notification
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testVerifyingEniqEventsServerReleaseVersionAndKpiNotificationLicense_3_0() {
        selenium.click(OPTIONS_BUTTON);
        pause(3000);
        selenium.waitForElementToBePresent(ABOUT, DEFAULT_TIMEOUT);
        pause(3000);
        selenium.click(ABOUT);
        pause(3000);
        String releaseVersion = selenium.getText(EE_RELEASE_VERSION);
        System.out.println("The current EE release version is : " + releaseVersion);
        String expectedLicenseInfoKpi = "Version : CXC4011291";

        String actualLicenseInfoKpi = selenium.getText(KPI_THRESHOLD_NOTIFICATION_LICENSE_INFO);

        assertEquals("The license information for KPI Threshold Notification is not as expected", expectedLicenseInfoKpi, actualLicenseInfoKpi);
        logger.log(Level.INFO,
                "The EE release version is " + releaseVersion + " and the license version for KPI Threshold Notification is as expected.");
    }

    /*
     * KPI_2_EE_4.1: Verify that clicking on each notification severity button opens a separate window
     */
    @Test
    public void testClickingOnEachButtonOpensASeparateWindow_3_1() throws Exception {

        checkWindowOpenedForEachSeverity(criticalButtonName);
        checkWindowOpenedForEachSeverity(majorButtonName);
        checkWindowOpenedForEachSeverity(minorButtonName);
        checkWindowOpenedForEachSeverity(warningButtonName);
        logger.log(Level.INFO, "clicking on each button opened a separate window");
    }

    /*
     * KPI_2_EE_4.2: Verify that clicking twice(or more) on the same notification severity button doesn't open more than one window, but just move the
     * focus to the already opened window and that window should be refreshed with new data
     */
    @Test
    public void testClickingAgainOnSeverityButtonRefreshesTheWindow_4_2() throws Exception {

        clickOrOpenButton(criticalButtonName);
        checkWindowsRefreshClickingONButtons(kpiCriticalWindow, criticalButtonName);
        clickOrOpenButton(majorButtonName);
        checkWindowsRefreshClickingONButtons(kpiMajorWindow, majorButtonName);
        clickOrOpenButton(minorButtonName);
        checkWindowsRefreshClickingONButtons(kpiMinorWindow, minorButtonName);
        clickOrOpenButton(warningButtonName);
        checkWindowsRefreshClickingONButtons(kpiWarningWindow, warningButtonName);
        logger.log(Level.INFO, "\nWindows are refreshed when clicked again on severity button and not opened twice");
    }

    /*
     * KPI_2_EE_4.3: Verify that if the notification severity button is clicked, window should open with the Last Data From time set in the settings
     * window.
     */
    @Test
    public void testWindowOpensWithLastDataFromTime_4_7() throws Exception {
        RefreshTimeRange timeRange = RefreshTimeRange.FIVE_MINUTES;
        setLastDataFrom(timeRange);
        checkWindowOpensWithLastDataFromTime(criticalButtonName, timeRange);
        checkWindowOpensWithLastDataFromTime(majorButtonName, timeRange);
        checkWindowOpensWithLastDataFromTime(minorButtonName, timeRange);
        checkWindowOpensWithLastDataFromTime(warningButtonName, timeRange);
        logger.log(Level.INFO, "\nAll windows are opened with Last Data From time");
    }

    /*
     * KPI: To verify the option 'Time selection for window' works in all the severity types window and data is present for specified time
     */

    @Test
    public void testTimeSelectionForWindowOptionWorkingForAllSeverityWindows() throws Exception {
        clickOrOpenButton(criticalButtonName);
        checkTimeSelectionForWindowOptionWorkingAndTestDataAvailableForAllSeverityWindows("CRITICAL");
        clickOrOpenButton(majorButtonName);
        checkTimeSelectionForWindowOptionWorkingAndTestDataAvailableForAllSeverityWindows("MAJOR");
        clickOrOpenButton(minorButtonName);
        checkTimeSelectionForWindowOptionWorkingAndTestDataAvailableForAllSeverityWindows("MINOR");
        clickOrOpenButton(warningButtonName);
        checkTimeSelectionForWindowOptionWorkingAndTestDataAvailableForAllSeverityWindows("WARNING");
        logger.log(Level.INFO, "\nOption 'Time selection for Window' working for all the severity type windows and data are available");

    }

    /*
     * KPI_2_EE_4.4: Verify that if the same notification severity button is clicked, and the window is already open for that severity level, the time
     * on the window should change to the Last Data From time set in the settings window
     */
    @Test
    public void testTimeOnWindowShouldntChangeWhenOpenedWindowTypeButtonIsClickedAgain_3_4() throws Exception {
        dataTime = getLastDataFrom();
        clickOrOpenButton(criticalButtonName);
        checkTimeOnWindowChanging(kpiCriticalWindow, criticalButtonName);
        clickOrOpenButton(majorButtonName);
        checkTimeOnWindowChanging(kpiMajorWindow, majorButtonName);
        clickOrOpenButton(minorButtonName);
        checkTimeOnWindowChanging(kpiMinorWindow, minorButtonName);
        clickOrOpenButton(warningButtonName);
        checkTimeOnWindowChanging(kpiWarningWindow, warningButtonName);
        logger.log(Level.INFO, "\nTime is same even after clicking on the all severity buttons which are already opened");
    }

    /*
     * KPI_2_EE_4.7: Verify that each of the UI controls from are visible and functional on each window
     */
    @Test
    public void testUIControlsAreVisibleAndFunctional_3_7() throws Exception {
        clickOrOpenButton(criticalButtonName);
        checkUIControlsAreVisibleAndFunctional(criticalButtonName);
        clickOrOpenButton(majorButtonName);
        checkUIControlsAreVisibleAndFunctional(majorButtonName);
        clickOrOpenButton(minorButtonName);
        checkUIControlsAreVisibleAndFunctional(minorButtonName);
        clickOrOpenButton(warningButtonName);
        checkUIControlsAreVisibleAndFunctional(warningButtonName);
        logger.log(Level.INFO, "\nAll the required UI controls on every KPI severity windows");
    }

    /*
     * KPI_2_EE_4.8: Verify that Refresh button on window is functioning and if it is clicked window is refreshed or not
     */
    @Test
    public void testRefreshButtonOnAllSeverityWindowsFunctioning_3_9() throws Exception {
        clickOrOpenButton(criticalButtonName);
        checkPageRefreshButton(kpiCriticalWindow);

        clickOrOpenButton(majorButtonName);
        checkPageRefreshButton(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkPageRefreshButton(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkPageRefreshButton(kpiWarningWindow);
        logger.log(Level.INFO, "\nAll severity type windows are refreshed when Refresh button is clicked");
    }

    /*
     * KPI_2_EE_4.9: Verify that page number is visible and able to navigate to the required page number using the next or previous page buttons
     * according to the screenshot
     */
    @Test
    public void testPageNavigationsByClickingOnthePageNavigationButtons_3_10() throws Exception {
        clickOrOpenButton(criticalButtonName);
        checkPageNavigation(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        checkPageNavigation(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkPageNavigation(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkPageNavigation(kpiWarningWindow);
        logger.log(Level.INFO, "Page navigating buttona are functioning according to the requirement");
    }

    /*
     * KPI_2_EE_4.10: Verify that if particular time is selected in the time selection listbox on the window, then the window should produce the new
     * data for that time period
     */
    @Test
    public void testIfTimeChangedInListBoxThenDataAlsoShouldBeChanged_3_11() throws Exception {
        clickOrOpenButton(criticalButtonName);
        CheckIfTimeRangeListboxFunctioning(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        CheckIfTimeRangeListboxFunctioning(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        CheckIfTimeRangeListboxFunctioning(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        CheckIfTimeRangeListboxFunctioning(kpiWarningWindow);
        logger.log(Level.INFO, "\nData is changing according to the timerange selected");
    }

    /*
     * KPI_2_EE_4.11: Check all the required columns are visible as per the requirement and also number of columns equals to 8.
     */
    /*
     * KPI_2_EE_4.12: Verify that the columns on the window table are in the required order as per the requirement
     */
    @Test
    public void testColomnOrderAndAllcolumnsAreVisibleForEachSeverityTypeWindow_3_13() throws Exception {

        clickOrOpenButton(criticalButtonName);
        checkColumnsOrderAndVisibility(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        checkColumnsOrderAndVisibility(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkColumnsOrderAndVisibility(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkColumnsOrderAndVisibility(kpiWarningWindow);
        logger.log(Level.INFO, "\nRequired columns are present");
    }

    /* KPI_2_EE_5.1: Verify that data in the window can be filtered by @Time */
    @Test
    public void testDataOnWindowCanBeFilteredByTime_5_1() throws Exception {

        clickOrOpenButton(criticalButtonName);
        checkDataFilteredWRTTime(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        checkDataFilteredWRTTime(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkDataFilteredWRTTime(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkDataFilteredWRTTime(kpiWarningWindow);
        logger.log(Level.INFO, "Data Filtered W.R.T Time on all windows");
    }

    @Test
    /*
     * KPI_2_EE_5.2: Verify that data in the window can be filtered by @severity type
     */
    public void testDataOnWindowCanBeFilteredBySeverityType_5_2() throws Exception {

        clickOrOpenButton(criticalButtonName);
        checkSeverityTypesOfNotificationsOnWindow(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        checkSeverityTypesOfNotificationsOnWindow(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkSeverityTypesOfNotificationsOnWindow(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkSeverityTypesOfNotificationsOnWindow(kpiWarningWindow);
        logger.log(Level.INFO, "Notifications are filtered according to the Severity when clicking on respective buttons");
    }

    /*
     * KPI_2_EE_5.3: Verify that column data can be sorted in ascending or descending order when that column header is clicked
     */
    @Test
    public void testColumnsAreSortableOnAllWindows_5_3() throws Exception {

        clickOrOpenButton(criticalButtonName);
        sortTableHeaderColumns(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        sortTableHeaderColumns(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        sortTableHeaderColumns(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        sortTableHeaderColumns(kpiWarningWindow);
        logger.log(Level.INFO, "All columns are sortable on all windows");
    }

    /*
     * KPI_2_EE_5.4: Verify that calendar settings for historical data accepts 1 hour <= timerange <= 90 days to filter table data
     */
    @Test
    public void testDataCanBefilteredWithCalenterDateAndTime_5_4() throws Exception {
        selenium.setSpeed("1000");
        clickOrOpenButton(criticalButtonName);
        checkFilteringDataWithCalenterDateAndTime(kpiCriticalWindow);
        clickOrOpenButton(majorButtonName);
        checkFilteringDataWithCalenterDateAndTime(kpiMajorWindow);
        clickOrOpenButton(minorButtonName);
        checkFilteringDataWithCalenterDateAndTime(kpiMinorWindow);
        clickOrOpenButton(warningButtonName);
        checkFilteringDataWithCalenterDateAndTime(kpiWarningWindow);
        logger.log(Level.INFO, "Data can be filtered with the calender settings on window");
    }

    /*
     * KPI_2_EE_6.1: Verify that every severity type window has the export as csv button
     */
    @Test
    public void testCSVButtonPresentOnallWindows_6_1() throws Exception {

        clickOrOpenButton(criticalButtonName);
        assertTrue("Export as csv button not present on Critical window", selenium.isElementPresent(csvButtonId));
        clickOrOpenButton(majorButtonName);
        assertTrue("Export as csv button not present on Major window", selenium.isElementPresent(csvButtonId));
        clickOrOpenButton(minorButtonName);
        assertTrue("Export as csv button not present on Minor window", selenium.isElementPresent(csvButtonId));
        clickOrOpenButton(warningButtonName);
        assertTrue("Export as csv button not present on Warning window", selenium.isElementPresent(csvButtonId));
    }

    /* KPI: To verify option refresh works in all the severity types window */
    @Test
    public void testRefreshOnallWindowsFromLastData() throws Exception {
        RefreshTimeRange timeRange = RefreshTimeRange.FIVE_MINUTES;
        setLastDataFrom(timeRange);
        checkWindowOpensWithLastDataFromTime(criticalButtonName, timeRange);
        selenium.click(refreshButtonXpath);
        waitForPageLoadingToComplete();
        setLastDataFrom(timeRange);
        checkWindowOpensWithLastDataFromTime(majorButtonName, timeRange);
        selenium.click(refreshButtonXpath);
        waitForPageLoadingToComplete();
        setLastDataFrom(timeRange);
        checkWindowOpensWithLastDataFromTime(minorButtonName, timeRange);
        selenium.click(refreshButtonXpath);
        waitForPageLoadingToComplete();
        setLastDataFrom(timeRange);
        checkWindowOpensWithLastDataFromTime(warningButtonName, timeRange);
        selenium.click(refreshButtonXpath);
        waitForPageLoadingToComplete();
        logger.info("Clicking On refresh button refresh the window");
        waitForPageLoadingToComplete();
    }

    /*
     * KPI: To verify properties window functions proper in all the severity types window
     */
    @Test
    public void testCheckPropertiesFunctionOnallWindows() throws Exception {
        clickOrOpenButton(criticalButtonName);
        checkPropertiesButton(kpiCriticalWindow);
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        clickOrOpenButton(majorButtonName);
        checkPropertiesButton(kpiMajorWindow);
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        clickOrOpenButton(minorButtonName);
        checkPropertiesButton(kpiMinorWindow);
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        clickOrOpenButton(warningButtonName);
        checkPropertiesButton(kpiWarningWindow);
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        logger.info("properties button selected on respective window");
        waitForPageLoadingToComplete();
    }

    /* PRIVATE METHODS */

    private void checkSeverityTypesOfNotificationsOnWindow(final CommonWindow window) throws Exception {
        String severityType = null;
        double definedThreshold = 0.0;
        double definedThresholdForGreaterThanRule = 0.2;
        double definedThresholdForSmallerThanRule = 98.8;
        List<String> computedKpiValue = null;
        List<String> definedThresholdValue = null;
        String windowName = getWindowTypeWithWindowName(window);
        final double definedSeverity = 1.5;

        window.setTimeRange(TimeRange.SIX_HOURS);
        if (!selenium.isElementPresent(firstRowXpath(windowName))) {
            window.setTimeRange(TimeRange.TWO_HOURS);
        }
        waitForPageLoadingToComplete();
        computedKpiValue = window.getAllTableDataAtColumn("Computed KPI Value");
        definedThresholdValue = window.getAllTableDataAtColumn("Defined Threshold");

        String[] computedData = computedKpiValue.toArray(new String[computedKpiValue.size()]);
        double[] kpiValues = new double[computedData.length];

        String[] definedData = definedThresholdValue.toArray(new String[definedThresholdValue.size()]);
        String[] definedValue = new String[definedData.length];

        for (int i = 0; i < computedData.length; i++) {

            kpiValues[i] = Convert.objectToDouble((computedData[i]).trim());

            definedValue[i] = definedData[i].trim();

            if (definedValue[i].equals(">0.2")) {
                definedThreshold = definedThresholdForGreaterThanRule;
            } else {
                definedThreshold = definedThresholdForSmallerThanRule;
            }

            float difference = (float) Math.abs(definedThreshold - kpiValues[i]);
            float warningThresholdFactor = (float) (definedThreshold * definedSeverity / 100);
            final float minorThresholdFactor = 2 * warningThresholdFactor;
            final float majorThresholdFactor = 2 * minorThresholdFactor;

            if (difference <= warningThresholdFactor) {
                severityType = "WARNING";
            } else if (difference > warningThresholdFactor && difference <= minorThresholdFactor) {
                severityType = "MINOR";
            } else if (difference > minorThresholdFactor && difference <= majorThresholdFactor) {
                severityType = "MAJOR";
            } else if (difference > majorThresholdFactor) {
                severityType = "CRITICAL";
            }
            assertTrue("There is one or more severity types other than " + windowName + "in" + windowName + "severity window",
                    windowName.equals(severityType));
            System.out.println("severityType: " + severityType);
        }

        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void sortTableHeaderColumns(final CommonWindow window) throws Exception {
        String windowName = getWindowTypeWithWindowName(window);
        int colCnt = window.getTableHeaderCount();
        window.setTimeRange(TimeRange.SIX_HOURS);
        if (!selenium.isElementPresent(firstRowXpath(windowName))) {
            window.setTimeRange(TimeRange.TWO_HOURS);
        }
        waitForPageLoadingToComplete();
        if (selenium.isElementPresent(firstRowXpath(windowName))) {
            for (int i = 1; i <= colCnt; i++) {
                String firstRowValueBfrSort = selenium
                        .getAttribute("//div[contains(@id,'NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA')][1]/TABLE//td[" + i + "]" + "@id");
                window.sortTable(i - 1);
                String firstRowValueAftrSort = selenium
                        .getAttribute("//div[contains(@id,'NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA')][1]/TABLE//td[" + i + "]" + "@id");
                System.out.println("firstRowValueAftrSort: " + firstRowValueAftrSort);
                assertTrue("Colomns are not sortable on   " + windowName + "   window", !firstRowValueBfrSort.equals(firstRowValueAftrSort));
            }
        } else {
            float sum = getSumOfAllPercentages();
            assertTrue("Sum of the percentages is not in between 99.6 and 100.4\t", (sum > 99.6 || sum < 100.4));
        }
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void checkWindowOpenedForEachSeverity(final String buttonId) throws Exception {
        String buttonName = buttonId;
        String windowName = getWindowTypeWithButtonName(buttonName);
        clickOrOpenButton(buttonName);
        assertTrue("Separate window is not opened when clicking on  " + windowName + " button",
                selenium.isElementPresent(getWindowXpath(windowName)));
    }

    private void checkWindowOpensWithLastDataFromTime(final String buttonId, final RefreshTimeRange timeRange) throws Exception {
        String buttonName = buttonId;
        String windowName = getWindowTypeWithButtonName(buttonName);
        String setTime = timeRange.getLabel();
        clickOrOpenButton(buttonName);
        String windowTime = getWindowTimeFromListbox(buttonName);
        assertTrue(windowName + " window not opened with Last Data From Time", setTime.equals(windowTime));
    }

    private void checkPageNavigation(final CommonWindow window) throws Exception {
        int pageNumber = 0;
        int currentPageNumber = 0;
        int pagesCnt = window.getPageCount();
        String windowName = getWindowTypeWithWindowName(window);
        pageNumber = window.getCurrentPageNumber();
        window.setTimeRange(TimeRange.SIX_HOURS);
        if (!selenium.isElementPresent(firstRowXpath(windowName))) {
            window.setTimeRange(TimeRange.TWO_HOURS);
        }
        waitForPageLoadingToComplete();
        if (pagesCnt > 1) {
            if (pagesCnt != pageNumber) {
                window.clickNextPage();
                currentPageNumber = window.getCurrentPageNumber();
                assertNotSame("Page numbers are same even after navigating to next page on " + windowName + " window", pageNumber, currentPageNumber);
                pageNumber = currentPageNumber;
            }
            window.clickFirstPage();
            currentPageNumber = window.getCurrentPageNumber();
            assertNotSame("Page numbers are same even after navigating to first page on " + windowName + " window", pageNumber, currentPageNumber);
            pageNumber = currentPageNumber;

            window.clickLastPage();
            currentPageNumber = window.getCurrentPageNumber();
            assertNotSame("Page numbers are same even after navigating to last page on " + windowName + " window", pageNumber, currentPageNumber);
            pageNumber = currentPageNumber;

            window.clickPreviousPage();
            currentPageNumber = window.getCurrentPageNumber();
            assertNotSame("Page numbers are same even after navigating to previuos page on " + windowName + " window", pageNumber, currentPageNumber);
        }
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void checkTimeOnWindowChanging(final CommonWindow window, final String buttonId) throws Exception {
        String buttonName = buttonId;
        String windowName = getWindowTypeWithWindowName(window);

        setLastDataFrom(RefreshTimeRange.FIFTEEN_MINUTES);
        waitForPageLoadingToComplete();
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        clickOrOpenButton(buttonName);
        Thread.sleep(3000);
        String windowTime = getWindowTimeFromListbox(windowName);
        dataTime = getLastDataFrom();
        assertTrue("Time is not same even after clicking on the " + windowName + " severity button which is already opened",
                windowTime.equals(dataTime));
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void checkUIControlsAreVisibleAndFunctional(final String windowId) throws Exception {
        String windowName = getWindowTypeWithButtonName(windowId);
        assertTrue("Refresh button on " + windowName + " window is not visible or clickable", selenium.isElementPresent(refreshButtonXpath));
        assertTrue("Next page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(nextPageButtonXpath));
        assertTrue("Previous Page button on  " + windowName + "  window is not visible or clickable",
                selenium.isElementPresent(previousPageButtonXpath));
        assertTrue("First page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(firstPageButtonXpath));
        assertTrue("Last page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(lastPageButtonXpath));
        assertTrue("Page text box on  " + windowName + "  window is not visible or enabled", selenium.isElementPresent(pageTextBoxXpath));
        assertTrue("Page count on  " + windowName + "  window is not visible", selenium.isElementPresent(pageCountXpath));
        assertTrue("Paging display on  " + windowName + "  window is not visible", selenium.isElementPresent(pagingDisplayXpath));
    }

    private void checkPageRefreshButton(final CommonWindow window) throws Exception {

        String windowName = getWindowTypeWithWindowName(window);
        window.setTimeRange(TimeRange.SIX_HOURS);
        if (!selenium.isElementPresent(firstRowXpath(windowName))) {
            window.setTimeRange(TimeRange.TWO_HOURS);
        }
        waitForPageLoadingToComplete();
        if (selenium.isElementPresent(firstRowXpath(windowName))) {
            elementIdBefore = selenium.getAttribute(firstRowXpath(windowName) + "@id");
            window.refresh();
            waitForPageLoadingToComplete();
            selenium.waitForElementToBePresent(firstRowXpath(windowName), "20000");
            elementIdAfter = selenium.getAttribute(firstRowXpath(windowName) + "@id");
            assertFalse(windowName + " window is not refreshed", elementIdBefore.equals(elementIdAfter));
        } else {
            float sum = getSumOfAllPercentages();
            assertTrue("Sum of the percentages is not in between 99.6 and 100.4\t", (sum > 99.6 || sum < 100.4));
        }
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void checkWindowsRefreshClickingONButtons(final CommonWindow window, final String buttonId) throws Exception {
        String windowName = getWindowTypeWithWindowName(window);
        window.setTimeRange(TimeRange.SIX_HOURS);
        if (!selenium.isElementPresent(firstRowXpath(windowName))) {
            window.setTimeRange(TimeRange.TWO_HOURS);
        }
        waitForPageLoadingToComplete();
        if (selenium.isElementPresent(firstRowXpath(windowName))) {
            elementIdBefore = selenium.getAttribute(firstRowXpath(windowName) + "@id");

            selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
            clickOrOpenButton(buttonId);
            window.setTimeRange(TimeRange.SIX_HOURS);
            if (!selenium.isElementPresent(firstRowXpath(windowName))) {
                window.setTimeRange(TimeRange.TWO_HOURS);
            }
            selenium.waitForElementToBePresent(firstRowXpath(windowName), "20000");
            elementIdAfter = selenium.getAttribute(firstRowXpath(windowName) + "@id");
            int winCnt = (Integer) selenium.getXpathCount(getWindowXpath(windowName));

            assertFalse(windowName + " window is not refreshed or opened twice", (elementIdBefore.equals(elementIdAfter) && winCnt > 1));
        } else {
            float sum = getSumOfAllPercentages();
            assertTrue("Sum of the percentages is not in between 99.6 and 100.4\t", (sum > 99.6 || sum < 100.4));
        }
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void CheckIfTimeRangeListboxFunctioning(final CommonWindow window) throws Exception {

        String windowName = getWindowTypeWithWindowName(window);
        final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.KPI_TIME_RANGES);
        TimeRange[] timeRanges;
        if (allTimeLabel != null) {
            final String[] timeLabels = allTimeLabel.split(",");
            timeRanges = new TimeRange[timeLabels.length];
            for (int i = 0; i < timeLabels.length; i++) {
                timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
            }
            for (TimeRange timeRange : timeRanges) {
                if (selenium.isElementPresent(firstRowXpath(windowName))) {
                    elementIdBefore = selenium.getAttribute(firstRowXpath(windowName) + "@id");
                    window.setTimeRange(timeRange);
                    waitForPageLoadingToComplete();
                    if (selenium.isElementPresent(firstRowXpath(windowName))) {
                        elementIdAfter = selenium.getAttribute(firstRowXpath(windowName) + "@id");
                        assertFalse("Data not changed after changing the time from listbox on " + windowName + " severity button",
                                elementIdBefore.equals(elementIdAfter));
                    } else {
                        window.setTimeRange(timeRanges[1]);
                        waitForPageLoadingToComplete();
                    }
                }
            }

        }
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    private void checkColumnsOrderAndVisibility(final CommonWindow window) throws Exception {
        String windowName = getWindowTypeWithWindowName(window);
        int columnCnt = window.getTableHeaderCount();
        assertTrue("Number of columns are more or less than expected on  " + windowName + " window", columnCnt == 8);
        logger.log(Level.INFO, "Colomn count on " + windowName + " window is: " + columnCnt);
        List<String> columnHeaders = window.getTableHeaders();
        areColumnsEqual(columnHeaders);
    }

    private void checkFilteringDataWithCalenterDateAndTime(final CommonWindow window) throws Exception {
        try {
            String windowName = getWindowTypeWithWindowName(window);
            window.setTimeRange(TimeRange.ONE_HOUR);
            if (selenium.isElementPresent(firstRowXpath(windowName))) {
                month = 1;
                hour = 0;
            } else {
                month = 0;
                hour = 1;
                window.setTimeRange(TimeRange.TWO_HOURS);
            }
            dateFormatting(month, hour);
            System.out.println("\tstartyear" + startyear + "\tstartmonth" + startmonth + "\tstartdate" + startdate + "\tendYear" + endYear
                    + "\tendMonth" + endMonth + "\tendDate" + endDate);
            final Date eDate = window.getDate(endYear, endMonth, endDate);
            final Date sDate = window.getDate(endYear, endMonth, endDate);
            if (selenium.isElementPresent(firstRowXpath(windowName))) {
                elementIdBefore = selenium.getAttribute(firstRowXpath(windowName) + "@id");
            } else {
                logger.warning("No data present for the selected time range for the " + windowName + " window.");
            }
            window.setTimeAndDateRange(sDate, getTimeCandidates(startTimeCandidate), eDate, getTimeCandidates(endTimeCandidate));
            waitForPageLoadingToComplete();
            if (selenium.isElementPresent(firstRowXpath(windowName))) {
                elementIdAfter = selenium.getAttribute(firstRowXpath(windowName) + "@id");
                assertFalse("Data not changed after changing the time from listbox on " + windowName + " severity button",
                        elementIdBefore.equals(elementIdAfter));
            } else {
                logger.warning("No data present for the selected time range for the " + windowName + " window.");
            }
            selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dateFormatting(final int month, final int hour) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        cal = Calendar.getInstance();
        if (cal.getTimeZone().inDaylightTime(cal.getTime())) {
            cal.add(Calendar.HOUR, -1);
        }
        cal.add(Calendar.MONTH, -month);

        String changedTime = dateFormat.format(cal.getTime());
        String currentTime = dateFormat.format(new Date());
        String[] startTime = changedTime.split("-");
        String[] endTime = currentTime.split("-");
        startyear = Integer.parseInt(startTime[0]);
        startmonth = Integer.parseInt(startTime[1]);
        startdate = Integer.parseInt(startTime[2]);
        endYear = Integer.parseInt(endTime[0]);
        endMonth = Integer.parseInt(endTime[1]);
        endDate = Integer.parseInt(endTime[2]);
        int minutes = ((int) (System.currentTimeMillis() / (1000 * 60)) % 15);
        cal.add(Calendar.MINUTE, -(minutes + 15));
        cal.add(Calendar.HOUR, -hour);
        startTimeCandidate = timeFormat.format(cal.getTime());
        String[] start = startTimeCandidate.split(":");
        startTimeHour = Integer.parseInt(start[0]);
        cal.add(Calendar.HOUR, 6);
        endTimeCandidate = timeFormat.format(cal.getTime());
        String[] end = endTimeCandidate.split(":");
        endTimeHour = Integer.parseInt(end[0]);
        if (startTimeHour > endTimeHour) {
            endTimeCandidate = EXCEPTION_END_TIME;
        }
    }

    private void checkDataFilteredWRTTime(final CommonWindow window) throws Exception {
        try {
            boolean flag = false;
            String windowName = getWindowTypeWithWindowName(window);
            window.setTimeRange(TimeRange.SIX_HOURS);
            if (!selenium.isElementPresent(firstRowXpath(windowName))) {
                flag = true;
                window.setTimeRange(TimeRange.TWO_HOURS);
                waitForPageLoadingToComplete();
            }
            if (selenium.isElementPresent(firstRowXpath(windowName))) {
                elementIdBefore = selenium.getAttribute(firstRowXpath(windowName) + "@id");
            } else {
                logger.warning("No data present for the selected time range for the " + windowName + " window.");
            }
            if (flag == true) {
                window.setTimeRange(TimeRange.ONE_HOUR);
                waitForPageLoadingToComplete();
                if (!selenium.isElementPresent(firstRowXpath(windowName))) {
                    window.setTimeRange(TimeRange.SIX_HOURS);
                }
            } else {
                window.setTimeRange(TimeRange.TWO_HOURS);
                waitForPageLoadingToComplete();
            }
            if (selenium.isElementPresent(firstRowXpath(windowName))) {
                elementIdAfter = selenium.getAttribute(firstRowXpath(windowName) + "@id");
                assertFalse("Data not filtered w.r.t Time on " + windowName + " severity window", elementIdBefore.equals(elementIdAfter));
                logger.log(Level.INFO, "Data Filtered w.r.t Timeranges on " + windowName + " window");
            } else {

                logger.warning("No data present for the selected time range for the " + windowName + " window.");
            }
            selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private float getSumOfAllPercentages() throws Exception {
        float criticalValue = Float.valueOf(getSeverityPercentage(criticalButtonName));
        float majorValue = Float.valueOf(getSeverityPercentage(majorButtonName));
        float minorValue = Float.valueOf(getSeverityPercentage(minorButtonName));
        float warningValue = Float.valueOf(getSeverityPercentage(warningButtonName));
        float sum = criticalValue + majorValue + minorValue + warningValue;
        return sum;
    }

    private void checkTimeSelectionForWindowOptionWorkingAndTestDataAvailableForAllSeverityWindows(final String WindowType) {

        int j = 0;
        selenium.setCursorPosition("//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']", "-1");
        pause(2000);
        String timeSelectionDefaultValueString = selenium.getValue("//*[@id='timeRangeComp']/input[2]");
        int timeSelectionDefaultValueInteger = Integer.parseInt(timeSelectionDefaultValueString);

        switch (timeSelectionDefaultValueInteger) {
            case 5:
                j = 5;
                break;
            case 15:
                j = 4;
                break;
            case 30:
                j = 3;
                break;
            case 60:
                j = 2;
                break;
            case 120:
                j = 1;
                break;
            case 360:
                j = 1;
                break;
        }

        for (int i = 0; i < j; i++) {
            selenium.keyDown("//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']", "\\40");
            pause(1000);
        }
        selenium.keyDown("//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']", "\\13");
        pause(1000);
        if (!selenium.isElementPresent(firstRowXpath(WindowType))) {
            for (int i = 0; i < j++; i++) {
                selenium.keyDown("//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']", "\\40");
                pause(1000);
            }
            selenium.keyDown("//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']", "\\13");
            pause(1000);
        }
        assertTrue("Warning! Data not present to test KPI", selenium.isElementPresent(firstRowXpath(WindowType)));
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");

    }

}
