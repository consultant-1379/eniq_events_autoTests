/* COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import static com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.events.tabs.Tab;

@Component
public class WorkspaceRC extends Tab {

    private static final String CALL_FAILURES_ANALYSIS_WINDOWS_CHECKBOX_WITHOUT_DROPDOWN = "//table[@class='container']/tbody/tr";

    private static final String CALL_FAILURES_ANALYIS_WINDOWS_DROPDOWN = "//div[@class='popupContent']";

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public WorkspaceRC() {
        super(TAB_NAME);
    }

    /** Waits for an element to be present */
    public void waitForElementToBePresent(final String locator, final String timeout) {
        if (!selenium.isElementPresent(locator)) {
            selenium.waitForElementToBePresent(locator, timeout);
        }
        loggerDuplicate.log(Level.INFO, "The Element ID : " + locator);
    }

    public void waitForElementToBePresent(final String locator) {
        waitForElementToBePresent(locator, DEFAULT_TIMEOUT);
    }

    public void checkAndOpenSideLaunchBar() {
        // check if Side Launch Bar is closed
        final boolean sideLaunchBarClosed = selenium.isElementPresent(SIDE_LAUNCH_BAR_OPEN);
        if (sideLaunchBarClosed) {
            selenium.click(SIDE_LAUNCH_BAR_OPEN);
            loggerDuplicate.log(Level.INFO, "Opening the Side Launch Bar");
        } else {
            loggerDuplicate.log(Level.INFO, "Side Launch Bar is already opened");
        }
    }

    public void clickPlusTab() {
        final String PLUS_TAB_XPATH = "//li[contains(@id,\"PLUS_TAB\")]";
        selenium.waitForElementToBePresent(PLUS_TAB_XPATH, "10000");
        selenium.click(PLUS_TAB_XPATH);
    }

    public void selectTimeRange() {
        // Using max range ( 1 week ) may result in many empty tables in freshly jumped servers.
        selectTimeRange(DATE_TIME_1DAY);
    }

    /**
     * @param timeRange
     *        - xpath of the time range(15,30, etc... found in SeleniumConstants.java)
     */
    public void selectTimeRange(final String timeRange) {
        waitForElementToBePresent(TIME_RANGE_ELEMENT_XPATH, DEFAULT_TIMEOUT);
        selenium.click(TIME_RANGE_ELEMENT_XPATH);
        selenium.click(timeRange);
    }

    public void selectDimension() throws InterruptedException {
        selectDimension(CORE_NETWORK_CS);
    }

    /**
     * @param dimension
     *        - xpath of the dimension value(2G Radio Network,3G Radio Network,APN,Controller, etc...)
     */
    public void selectDimension(final String dimension) throws InterruptedException {
        waitForElementToBePresent(DIMENSION_ELEMENT_XPATH);
        selenium.click(DIMENSION_ELEMENT_XPATH);
        selenium.click(dimension);
    }

    public void enterDimensionValueForGroups(final String dimensionString) throws InterruptedException {
        if (dimensionString != null) {
            int endIndex = 6;
            if (dimensionString.length() < 6) {
                endIndex = dimensionString.length();
            }
            selenium.click(DIMENSION_GROUP_TEXTFIELD_ID);
            selenium.type(DIMENSION_GROUP_TEXTFIELD_ID, dimensionString.substring(0, endIndex));
            selenium.typeKeys(DIMENSION_GROUP_TEXTFIELD_ID, dimensionString.substring(endIndex));
            waitForElementToBePresent("//*[@class='popupContent']", DEFAULT_TIMEOUT);
            Thread.sleep(5000);
            selenium.click("//div[contains(text(),'" + dimensionString + "')]");
        } else {
            enterDimensionValueForGroups();
        }
    }

    public void enterDimensionValueForGroups() throws InterruptedException {
        waitForElementToBePresent(DIMENSION_GROUP_TEXTFIELD_ARROW);
        Thread.sleep(5000);
        selenium.click(DIMENSION_GROUP_TEXTFIELD_ARROW);
        waitForElementToBePresent(DIMENTION_GROUP_POP_UP_MENU, DEFAULT_TIMEOUT);
        selenium.click(DIMENTION_GROUP_POP_UP_MENU);
    }

    /**
     * @param dimensionString
     *        - string to be entered in the dimension text area (BSC1 Ericsson GSM ,etc ... )
     */
    public void enterDimensionValue(final String dimensionString) throws InterruptedException {
        if (dimensionString != null) {
            if (selenium.isElementPresent(DIMENSION_IMSI) && selenium.isVisible(DIMENSION_IMSI)) {
                selenium.type(DIMENSION_IMSI, dimensionString);
            } else if (selenium.isElementPresent(TERMINAL_MAKE_XPATH) && selenium.isVisible(TERMINAL_MAKE_XPATH)) {
                selenium.click(TERMINAL_MAKE_XPATH);
                selenium.type(TERMINAL_MAKE_XPATH, dimensionString);
                selenium.typeKeys(TERMINAL_MAKE_XPATH, " ");
                waitForElementToBePresent("//*[@class='popupContent']", DEFAULT_TIMEOUT);
                Thread.sleep(5000);
                selenium.click("//*[@id='selenium_tag_pairedSuggestBox']/div[1]/div/div/div[1]");
            } else {
                selenium.click(DIMENSION_TEXTFIELD_ID);
                selenium.type(DIMENSION_TEXTFIELD_ID, dimensionString);
                selenium.typeKeys(DIMENSION_TEXTFIELD_ID, " ");
                waitForElementToBePresent(DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
                Thread.sleep(5000);
                selenium.click("//td[contains(text(),'" + dimensionString + "')]");
            }
        } else {
            enterDimensionValue();
        }
    }

    public void enterDimensionValue() throws InterruptedException {
        waitForElementToBePresent(DIMENSION_TEXTFIELD_ARROW);
        if (!selenium.isVisible(DIMENSION_TEXTFIELD_ARROW)) {
            return;
        }
        Thread.sleep(5000);
        // epagarv 03/01/2013, Check for DimensionTextFieldArrow
        if (selenium.isEditable(DIMENSION_TEXTFIELD_ID)) {
            selenium.click(DIMENSION_TEXTFIELD_ARROW);
            waitForElementToBePresent(DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
            selenium.click(DIMENTION_POP_UP_MENU + "/div/table/tbody/tr[1]/td");
        }
    }

    /**
     * This method can be used to enter dimension values like IMSI, MSISDN, PTIMSI where there is no popup while we type the input.
     */
    public void enterDimensionValueWithoutPopup(final String dimensionString) {
        final String DIMENSION_IMSI_TEXTFIELD_ID = "//input[contains(@class, \"GKH1R2KJ2 GKH1R2KGMB GKH1R2KH2\")]";
        waitForElementToBePresent(DIMENSION_IMSI_TEXTFIELD_ID, DEFAULT_TIMEOUT);
        selenium.type(DIMENSION_IMSI_TEXTFIELD_ID, dimensionString);
    }

    public void enterSecondDimensionValue(final String secondDimensionValue) throws InterruptedException {
        selenium.type(DIMENSION_SECOND_VALUE, secondDimensionValue);
        selenium.typeKeys(DIMENSION_SECOND_VALUE, " ");
        waitForElementToBePresent(SECOND_DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
        selenium.click("//td[contains(text(),'" + secondDimensionValue + "')]");
    }

    /**
     * @param filterValue
     *        - text to be entered into the filter text box
     */
    public void enterWindowFilter(final String filterValue) {
        selenium.click(WINDOW_FILTER_ID);
        selenium.type(WINDOW_FILTER_ID, filterValue);
    }

    /**
     * @param category
     *        - selected category from categoryPanel
     * @param windowOption
     *        - selected category from categoryPanel
     */
    public void selectWindowType(final String category, final String windowOption) {
        final int categories = (Integer) selenium.getXpathCount(DIMENSION_CATEGORY);
        for (int i = 1; i <= categories; i++) {
            if (selenium.isElementPresent(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]")) {
                selenium.click(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]");
                pause(5000);
                selenium.click(DIMENSION_CATEGORY + "[" + i + "]/div[2]/div/div/div//div[contains(text(), '" + windowOption + "')]");
            }
        }
    }

    public void enterTerminalNameValue(final String terminalName) throws InterruptedException {
        selenium.click(TERMINAL_NAME_TEXTFIELD_ID);
        selenium.typeKeys(TERMINAL_NAME_TEXTFIELD_ID, terminalName);
        waitForElementToBePresent(DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
        Thread.sleep(5000);
        selenium.click("//td[contains(text(),'" + terminalName + "')]");
    }

    /**
     * @param dimensionValue
     *        - xpath of the dimension value(2G Radio Network,3G Radio Network,APN,Controller, etc...)
     */
    public void selectTerminalDimensionValue(final String dimensionValue) throws InterruptedException {
        selenium.click(TERMINAL_MAKE_XPATH);
        selenium.typeKeys(TERMINAL_MAKE_XPATH, dimensionValue);
        waitForElementToBePresent("//*[@class='popupContent']", DEFAULT_TIMEOUT);
        pause(4000);
        selenium.click("//div[contains(text(),'" + dimensionValue + "')]");
    }

    public void setElementPathValue(final String path, final String value) {
        selenium.click(path);
        selenium.type(path, value);
        selenium.typeKeys(path, value);
        waitForElementToBePresent(DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
        pause(7000);
        selenium.click("//td[contains(text(),'" + value + "')]");
    }

    public void pause(final int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (final InterruptedException e) {
        }
    }

    public void clickLaunchButton() {
        selenium.click(LAUNCH_BUTTON_ID);
    }

    public void launchWindow(final String time, final String dimension, final String dimensionValue, final String dimensionSecondValue,
                             final String windowOption, final String categoryPanel) throws InterruptedException, PopUpException {
        selenium.waitForPageLoadingToComplete();
        if (time != null) {
            if (Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d", time)) {
                selectCustomTime(time);
            } else {
                selectTimeRange(time);
            }
        } else {
            selectTimeRange();
        }
        selectDimension(dimension);
        Thread.sleep(6000);
        if (selenium.isVisible(DIMENSION_GROUP_TEXTFIELD_ID)) {
            enterDimensionValueForGroups(dimensionValue);
        } else {
            enterDimensionValue(dimensionValue);
        }
        if (dimensionSecondValue != null) {
            enterSecondDimensionValue(dimensionSecondValue);
        }
        enterWindowFilter(windowOption);
        selectWindowType(categoryPanel, windowOption);
        clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    public void launchWindow(final String dimension, final String dimensionValue, final String dimensionSecondValue, final String windowOption,
                             final String categoryPanel) throws InterruptedException, PopUpException {
        launchWindow(null, dimension, dimensionValue, dimensionSecondValue, windowOption, categoryPanel);
    }

    public void launchWindow(final String dimension, final String dimensionValue, final String windowOption, final String categoryPanel)
            throws InterruptedException, PopUpException {
        launchWindow(null, dimension, dimensionValue, null, windowOption, categoryPanel);
    }

    public void launchWindow(final String dimension, final String windowOption, final String categoryPanel) throws InterruptedException,
            PopUpException {
        launchWindow(null, dimension, null, null, windowOption, categoryPanel);
    }

    public void closeWindow(final String currentWindow) {
        selenium.mouseDown(currentWindow);
        selenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    @Override
    protected void clickStartMenu() {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    /**
     * it will select closest 15 min Date/Time using custom Date in UI, on bases of given Time
     * 
     * @param time
     */
    private void selectCustomTime(final String time) {
        selectTimeRange(SeleniumConstants.DATE_TIME_Custom);
        final Calendar startTime = CommonUtils.getclosest15minDate(convertTimeToCalenderTime(time));
        final Calendar endTime = Calendar.getInstance();
        endTime.setTime(startTime.getTime());
        startTime.add(Calendar.MINUTE, -15);
        openCustomDateAndTime(startTime, endTime);
        System.err.println(CommonUtils.parseCalenderDateToString(startTime, CommonUtils.YYYY_MM_DD_HH_MM_SS_SSS));
    }

    /**
     * @param time
     * @return
     */
    private Calendar convertTimeToCalenderTime(String time) {
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        return cal;
    }

    /**
     * Select Customize Date Time using given start and end time
     * 
     * @param startCal
     * @param endCal
     */
    private void openCustomDateAndTime(final Calendar startCal, final Calendar endCal) {
        final String startDay = String.valueOf(startCal.get(Calendar.DAY_OF_MONTH));
        final String startMonth = getMonth(startCal.get(Calendar.MONTH) + 1).substring(0, 3);
        final String startYear = String.valueOf(startCal.get(Calendar.YEAR));
        final int startHrs = startCal.get(Calendar.HOUR_OF_DAY);
        final int startMins = startCal.get(Calendar.MINUTE);
        final String endDay = String.valueOf(endCal.get(Calendar.DAY_OF_MONTH));
        final String endMonth = getMonth(endCal.get(Calendar.MONTH) + 1).substring(0, 3);
        final String endYear = String.valueOf(endCal.get(Calendar.YEAR));
        final int endHrs = endCal.get(Calendar.HOUR_OF_DAY);
        final int endMins = endCal.get(Calendar.MINUTE);
        final String dateNeedToSelectFrom = startYear + " " + startMonth;
        final String dateNeedToSelectTo = endYear + " " + endMonth;
        // Select start Month and Year
        while (true) {
            final String currentMonthFrom = selenium.getText(DATE_PICKER_START_MONTH_YEAR);
            if (currentMonthFrom.equals(dateNeedToSelectFrom)) {
                break;
            }
            if (selenium.isElementPresent(DATE_PICKER_START_DATE_PERVIOUS_BUTTON)) {
                selenium.keyPress(DATE_PICKER_START_DATE_PERVIOUS_BUTTON, "\\13");
            }
        }
        // Select Start Day
        selectDay("start", startDay);
        // Select Start Hour and min
        selenium.click(DATE_PICKER_START_DATE_HOUR_DROPDOWN_BUTTON);
        selenium.click(DATE_PICKER_SELECT_START_DATE_HOUR_OPTION_PATH + CommonUtils.format(startHrs) + DATE_PICKER_END_TAG);
        selenium.click(DATE_PICKER_START_DATE_MIN_DROPDOWN_BUTTON);
        selenium.click(DATE_PICKER_SELECT_START_DATE_MIN_OPTION_PATH + CommonUtils.format(startMins) + DATE_PICKER_END_TAG);
        // sleep for while in order to render UI
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        // Select end Month and Year
        while (true) {
            final String currentMonthTo = selenium.getText(DATE_PICKER_END_MONTH_YEAR);
            if (currentMonthTo.equals(dateNeedToSelectTo)) {
                break;
            }
            if (selenium.isElementPresent(DATE_PICKER_END_DATE_PERVIOUS_BUTTON)) {
                selenium.keyPress(DATE_PICKER_END_DATE_PERVIOUS_BUTTON, "\\13");
            }
        }
        // Select end day
        selectDay("end", endDay);
        // select end Hour and Min in UI
        selenium.click(DATE_PICKER_END_DATE_HOUR_DROPDOWN_BUTTON);
        selenium.click(DATE_PICKER_SELECT_END_DATE_HOUR_OPTION_PATH + CommonUtils.format(endHrs) + DATE_PICKER_END_TAG);
        selenium.click(DATE_PICKER_END_DATE_MIN_DROPDOWN_BUTTON);
        selenium.click(DATE_PICKER_SELECT_END_DATE_MIN_OPTION_PATH + CommonUtils.format(endMins) + DATE_PICKER_END_TAG);
        // Click on Okay Button
        selenium.click(DATE_PICKER_OK_BUTTON);
    }

    /**
     * select Day in custom date/time
     * 
     * @param type
     * @param day
     */
    private void selectDay(final String type, final String day) {
        String path = "";
        if (isStartDate(type)) {
            path = DATE_PICKER_STARTDATE_CALENDER_DAY + day + DATE_PICKER_AND_TABINDER_IS_EQUAL_TO_ZERO + DATE_PICKER_END_TAG;
        } else if (isEndDate(type)) {
            path = DATE_PICKER_ENDDATE_CALENDER_DAY + day + DATE_PICKER_AND_TABINDER_IS_EQUAL_TO_ZERO + DATE_PICKER_END_TAG;
        }
        if (selenium.isElementPresent(path)) {
            selenium.click(path);
        }
    }

    private boolean isEndDate(final String type) {
        return type.equals("end");
    }

    private boolean isStartDate(final String type) {
        return type.equals("start");
    }

    protected String getMonth(final int month) {
        final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        return months[month - 1];
    }

    public void launchWindowWithCustomTime(final String time, final String dimension, final String windowOption, final String categoryPanel)
            throws InterruptedException, PopUpException {
        launchWindow(time, dimension, null, null, windowOption, categoryPanel);
    }

    public void launchWindowWithCustomTime(final String time, final String dimension, final String dimensionValue, final String windowOption,
                                           final String categoryPanel) throws InterruptedException, PopUpException {
        launchWindow(time, dimension, dimensionValue, null, windowOption, categoryPanel);
    }

    public void launchWindowWithCustomTime(final String time, final String dimension, final String dimensionValue, final String dimensionSecondValue,
                                           final String windowOption, final String categoryPanel) throws InterruptedException, PopUpException {
        launchWindow(time, dimension, dimensionValue, dimensionSecondValue, windowOption, categoryPanel);
    }

    public void selectRabTypeValueInCallfailureAnalysisWindow(final String rabType, final String... failureTypes) throws PopUpException,
            InterruptedException {
        if (selenium.isElementPresent(CALL_FAILURE_ANALYSIS_WINDOWS_SPAN_ID)) {
            waitForElementToBePresent(CALL_FAILURES_ANALYSIS_DROPDOWN_ARROW_XPATH);
            selenium.click(CALL_FAILURES_ANALYSIS_DROPDOWN_ARROW_XPATH);
            selenium.click(CALL_FAILURES_ANALYIS_WINDOWS_DROPDOWN + "/div//div[contains(text(),'" + rabType + "')]");

            final int countCheckboxs = (selenium.getXpathCount(CALL_FAILURES_ANALYSIS_WINDOWS_CHECKBOX_WITHOUT_DROPDOWN)).intValue();
            final ArrayList<String> types = new ArrayList<String>();
            Collections.addAll(types, failureTypes);
            for (int i = 2; i < countCheckboxs + 1; i++) {
                final String type = selenium.getText("//table[@class='container']/tbody/tr[" + i + "]//label").trim();
                if (!types.contains(type)) {
                    if (selenium.isElementPresent("//label[contains(text(), '" + type + "')]")) {
                        selenium.click("//label[contains(text(), '" + type + "')]");
                    }
                }
            }
        }
        selenium.click(SeleniumConstants.CALL_FAILURES_ANALYSIS_WINDOW_LAUNCH_BUTTON);
        selenium.waitForPageLoadingToComplete();
        Thread.sleep(8000);
    }

    /**
     * Select Grid or Chart View
     * 
     * @param type
     *        : name of selection i.e. Grid View or Chart View
     */
    public void selectGridOrChartView(final String type) {
        selenium.click("//label[contains(text(),'" + type + "')]");
    }

    public void launch() {
        selenium.click("//button[contains(text(),'Launch')]");
    }

    @SuppressWarnings("deprecation")
    public void saveWorkspaceAs(String workspaceName) {
        selenium.click("//span[@class='optionsMenu']");
        selenium.click("//div[@class='popupContent']//div[2]");
        selenium.click("//div[@id='selenium_tag_WORKSPACE_SAVE_AS']/div/div/div//input");
        selenium.type("//div[@id='selenium_tag_WORKSPACE_SAVE_AS']/div/div/div//input", workspaceName);
        selenium.typeKeys("//div[@id='selenium_tag_WORKSPACE_SAVE_AS']/div/div/div//input", "1");
        selenium.click("//div[@id='selenium_tag_WORKSPACE_SAVE_AS']//td[1]/button[@class='gwt-Button']");
    }

    public void openSavedWorkspace(String workspace) throws Exception {
        pause(2000);
        checkAndOpenSideLaunchBar();
        selenium.click(SeleniumConstants.WORKSPACES_SIDE_BAR_TAB_XPATH);
        if (selenium.isElementPresent("//div[text()='" + workspace + "']")) {
            selenium.click("//div[text()='" + workspace + "']");
            selenium.click("//button[text()='Launch (1)']");
            pause(2000);
            selenium.click("//button[text()='Update']");
            pause(2000);
        } else {
            throw new Exception("Workspace: " + workspace + " does not exist.");
        }
    }
}