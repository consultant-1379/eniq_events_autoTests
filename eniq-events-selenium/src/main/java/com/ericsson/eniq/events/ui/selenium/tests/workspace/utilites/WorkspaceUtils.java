package com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites;

import java.util.List;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;

/**
 * @author elukpot
 * @since 14.0.16
 */
public class WorkspaceUtils {

    /**
     * This method formats the error message for the checkDefaultColumns tests.
     *
     * @param errorMessage
     *        - The error message to output.
     * @param items
     *        - List of items.
     * @return String - Formatted error message to be used in selenium failure.
     */
    public static String formatErrorMessage(final String errorMessage, final List<String> items) {

        final String commaSpace = ", ";

        final StringBuilder stringBuilder = new StringBuilder(errorMessage);
        stringBuilder.append(": (");

        for (final String menuOption : items) {
            stringBuilder.append(menuOption);
            stringBuilder.append(commaSpace);
        }

        final int lastIndex = stringBuilder.lastIndexOf(commaSpace);
        stringBuilder.replace(lastIndex, stringBuilder.length(), "");
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    /**
     * Sleeps the current thread in the Footer Time Range tests.
     */
    public static void sleepInFooterTimeRangeTests() {
        try {
            Thread.sleep(SeleniumConstants.DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS);
        } catch (final InterruptedException ignored) {
        }
    }

    /**
     * Ticks the QCI Columns.
     *
     * @param qosWindow
     * @param selenium
     */
    public static void tickSelectAllQCIColumns(final CommonWindow qosWindow, final EricssonSelenium selenium) {
        qosWindow.openTableHeaderMenu(0);
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), '4G Core')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'QCI')]");
        if (!selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='Select All']")) {
            if (selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='Select All']")) {
                selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='Select All']");
            }
        }

        selenium.click("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");
    }

    /**
     * Unticks the QCI Columns.
     *
     * @param qosWindow
     * @param selenium
     */
    public static void unTickSelectAllQCIColumns(final CommonWindow qosWindow, final EricssonSelenium selenium) {
        qosWindow.openTableHeaderMenu(0);
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), '4G Core')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'QCI')]");

        if (selenium.isElementPresent("//a[contains(@class, 'x-menu-checked')][text()='Select All']")) {
            selenium.click("//a[contains(@class, 'x-menu-checked')][text()='Select All']");
        }

        selenium.click("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");
    }

    public static void pauseUntilWindowLoads(final EricssonSelenium selenium) {
        try {
            Thread.sleep(1000);
            selenium.waitForPageLoadingToComplete();
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } catch (final PopUpException e) {
            e.printStackTrace();
        }
    }
}
