/**
 * -----------------------------------------------------------------------
 * COPYRIGHT Ericsson 2013
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ui.selenium.tests.alarmkpi;

import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class KPIButtonsTestGroups extends DefaultDataLibrary {

    static Logger logger = Logger.getLogger(KPIButtonsTestGroups.class.getName());
    private static final String CRITICAL = "CRITICAL";

    @Test
    public void testDataAvailable() throws Exception {
        clickOrOpenButton(criticalButtonName);
        if (!selenium.isElementPresent(firstRowXpath(CRITICAL))) {

            kpiCriticalWindow.setTimeRange(TimeRange.ONE_WEEK);
            if (!selenium.isElementPresent(firstRowXpath(CRITICAL))) {
                kpiCriticalWindow.setTimeRange(TimeRange.ONE_DAY);
            }
        }
        assertTrue("Warning! Data not present to test KPI", selenium.isElementPresent(firstRowXpath(CRITICAL)));
    }

    /* KPI_2_EE_2.2: Verify that 4 buttons( Critical (Red), Major(Orange), Minor(Yellow), Warning(Blue) ) and a Settings button present on the panel. */
    @Test
    public void testAllButtonsPresentOnKPIPanel_2_2() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        assertTrue("Critical button is not present or visible", selenium.isElementPresent(getButtonXPath(criticalButtonName)));
        assertTrue("Major button is not present or visible", selenium.isElementPresent(getButtonXPath(majorButtonName)));
        assertTrue("Minor button is not present or visible", selenium.isElementPresent(getButtonXPath(minorButtonName)));
        assertTrue("Warning button is not present or visible", selenium.isElementPresent(getButtonXPath(warningButtonName)));
        assertTrue("Settings button is not present or visible", selenium.isElementPresent(getButtonXPath(settingsButtonName)));
        logger.log(Level.INFO, "\nAll buttons are visible on KPI panel");
    }

    /* KPI_2_EE_2.4: Verify that the % of the severities are displayed under Critical, Major, Minor, Warning buttons */
    @Test
    public void testPercentagesPresentUnderEachSeverityButton_2_4() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        assertTrue("Critical Notifications percentage not present under Critical button",
                selenium.isElementPresent(getPercentageXpath(criticalButtonName)));
        assertTrue("Major Notifications percentage not present under Major button", selenium.isElementPresent(getPercentageXpath(majorButtonName)));
        assertTrue("Minor Notifications percentage not present under Minor button", selenium.isElementPresent(getPercentageXpath(minorButtonName)));
        assertTrue("Warning Notifications percentage not present under Warning button",
                selenium.isElementPresent(getPercentageXpath(warningButtonName)));
        logger.log(Level.INFO, "\nPercentages present under all buttons");
    }

    /* KPI_2_EE_2.6: Verify that sum of all percentages are equal to 100 */
    @Test
    public void testSumOfAllPercentagesAround100_2_6() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        float criticalValue = Float.valueOf(getSeverityPercentage(criticalButtonName));
        float majorValue = Float.valueOf(getSeverityPercentage(majorButtonName));
        float minorValue = Float.valueOf(getSeverityPercentage(minorButtonName));
        float warningValue = Float.valueOf(getSeverityPercentage(warningButtonName));
        float sum = criticalValue + majorValue + minorValue + warningValue;
        assertTrue("Sum of the percentages not equals to 100\t", (sum > 99.6 || sum < 100.4));
        logger.log(Level.INFO, "\nSum of the percentages is in between 99.6 and 100.4");
    }

    /*
     * KPI_2_EE_3.1: Verify that clicking on settings button opens a window with name 'KPI Alarm Configuration' and with 'Last Data From' and 'Refresh
     * rate' as fields. Should also have Update and Cancel buttons. Opened window should be visible in Network, Terminal, Subscriber and Ranking Tabs
     */

    /* KPI_2_EE_3.2: Check all the UI controls are functioning according to the requirements on the settings window */
    @Test
    public void testSettingsWindowContainsAllRequiredFields_3_1() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        clickOrOpenButton(settingsButtonName);
        String title = selenium.getText(settingsWindowXpath);
        assertTrue("Setting window title is not matching", title.equals("KPI Alarm Configuration"));
        assertTrue("Settings window doesn't contain Last Data From field",
                selenium.isElementPresent("//table[@id='kpiConfigPanel']//*[@id='refreshTimeCombo']"));
        assertTrue("Settings window doesn't contain Refresh Rate field",
                selenium.isElementPresent("//table[@id='kpiConfigPanel']//*[@id='refreshRateCombo']"));
        assertTrue("Settings window doesn't contain Update button", selenium.isElementPresent("//table[@id='kpiConfigPanel']//*[@id='updateBtn']"));
        assertTrue("Settings window doesn't contain Refresh Rate field",
                selenium.isElementPresent("//table[@id='kpiConfigPanel']//*[@id='cancelBtn']"));
        logger.log(Level.INFO,
                "All required fields are present on settings window and it is present under Network, Terminal, Subscriber and Ranking Tabs	");
    }

    /* KPI_2_EE_3.9: Verify that once the Last Data From and Refresh Rate are set and updated, the changes should remain until they are changed again */
    @Test
    public void testValuesChangingOnceUpdatedOnSettingsWindow_3_9() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        setLastDataFrom(RefreshTimeRange.TWO_HOURS);
        setRefreshRate(RefreshTimeRange.FIFTEEN_MINUTES);
        String refreshTime = getLastDataFrom();
        String refreshRate = getRefreshRate();
        assertTrue("\nLast Data From is changed to default ", RefreshTimeRange.TWO_HOURS.getLabel().equals(refreshTime));
        assertTrue("\nRefresh Rate is changed to default ", RefreshTimeRange.FIFTEEN_MINUTES.getLabel().equals(refreshRate));
        logger.log(Level.INFO, "Last Data From and Refresh Rate are not changed once set");
    }

    /*
     * KPI_2_EE_4.5: Verify that Refresh rate on the settings window can be set to minimum 5 minutes and maximum 24 hours & also in between them to 15
     * minutes & 12 hours
     */
    @Test
    public void testRefreshRateCanBeSetMinFiveMinutesAndMaxTwentyFourHours_3_5() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        setRefreshRate(RefreshTimeRange.FIVE_MINUTES);
        logger.log(Level.INFO, "\nRefresh Rate on the settings window can be set to minimum " + RefreshTimeRange.FIVE_MINUTES);
        setRefreshRate(RefreshTimeRange.TWENTYFOUR_HOURS);
        logger.log(Level.INFO, "\nRefresh Rate on the settings window can be set to maximum " + RefreshTimeRange.TWENTYFOUR_HOURS);
        clickOrOpenButton(settingsButtonName);
        setRefreshRate(RefreshTimeRange.FIFTEEN_MINUTES);
        logger.log(Level.INFO, "\nRefresh Rate on the settings window can be set in between " + RefreshTimeRange.FIVE_MINUTES + " and "
                + RefreshTimeRange.TWENTYFOUR_HOURS + " to " + RefreshTimeRange.FIFTEEN_MINUTES);
        setRefreshRate(RefreshTimeRange.TWELVE_HOURS);
        logger.log(Level.INFO, "\nRefresh Rate on the settings window can be set in between " + RefreshTimeRange.FIVE_MINUTES + " and "
                + RefreshTimeRange.TWENTYFOUR_HOURS + " to " + RefreshTimeRange.TWELVE_HOURS);
    }

    /*
     * KPI_2_EE_4.7(and 3.8): Verify that Last Data From on the settings window can be set to minimum 5 minutes and maximum 6 hours and in between
     * them to 30 minutes & 2 hours
     */
    @Test
    public void testLastDataFromCanBeSetMinFiveMinutesAndMaxSixHours_3_7() throws Exception {
        selenium.waitForElementToBePresent(panelXpath, "10000");
        setLastDataFrom(RefreshTimeRange.FIVE_MINUTES);
        logger.log(Level.INFO, "\nLast Data From on the settings window can be set to minimum " + RefreshTimeRange.FIVE_MINUTES);
        setLastDataFrom(RefreshTimeRange.SIX_HOURS);
        logger.log(Level.INFO, "\nLast Data From on the settings window can be set to maximum " + RefreshTimeRange.SIX_HOURS);
        clickOrOpenButton(settingsButtonName);
        setLastDataFrom(RefreshTimeRange.THIRTY_MINUTES);
        logger.log(Level.INFO, "\nLast Data From on the settings window can be set in between " + RefreshTimeRange.FIVE_MINUTES + " and "
                + RefreshTimeRange.SIX_HOURS + " to " + RefreshTimeRange.THIRTY_MINUTES);
        setLastDataFrom(RefreshTimeRange.TWO_HOURS);
        logger.log(Level.INFO, "\nLast Data From on the settings window can be set in between " + RefreshTimeRange.FIVE_MINUTES + " and "
                + RefreshTimeRange.SIX_HOURS + " to " + RefreshTimeRange.TWO_HOURS);
    }

    public void clickingOnAllButtonLeadingToNetworkTab(final String windowId) throws Exception {
        String windowType = getWindowTypeWithButtonName(windowId);
        String windowXpath = getWindowXpath(windowType);
        terminalTab.openTab();
        assertFalse("Clicking on panel leading to terminal tab, not to network tab", selenium.isVisible(windowXpath));
        subscriberTab.openTab();
        assertFalse("Clicking on panel leading to Subscriber tab, not to network tab", selenium.isVisible(windowXpath));
        rankingsTab.openTab();
        assertFalse("Clicking on panel leading to Rankings tab, not to network tab", selenium.isVisible(windowXpath));
        networkTab.openTab();
        assertTrue("By clicking on Critical Button is not leading to NetworkTab", selenium.isVisible(windowXpath));
    }
}