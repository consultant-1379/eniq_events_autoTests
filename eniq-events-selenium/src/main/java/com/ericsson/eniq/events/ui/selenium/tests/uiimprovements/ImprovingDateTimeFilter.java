package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.logging.Level;

//import java.sql.Date;

public class ImprovingDateTimeFilter extends EniqEventsUIBaseSeleniumTest {

    @Autowired private NetworkTab networkTab;
    @Autowired @Qualifier("networkEventAnalysis") private CommonWindow networkEventAnalysis;
    @Autowired private WorkspaceRC workspaceRC;

    static final String tabName = "NETWORK_TAB";
    static final String DRILL_ON_EVENTTYPE_BY_APN = "//div[1]//span[@id='NETWORK_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_BY_APN']";
    static final String EVENT_TIME = "//table//div[span='Event Time']/a";
    static final String FILTERS = "//div[contains(@class,' x-ignore x-menu x-component')]//div/a[contains(@class,'x-menu-item x-menu-check-item " + "x-menu-item-arrow x-component')]";
    static final String CHECKBOX = "//div[contains(@class,' x-ignore x-menu x-component')]//div/a[contains(@class, 'x-menu-item x-menu-check-item x-menu-item-arrow x-component')]/img";
    static final String CHECKBOX_CHECKED = "//div[contains(@class,' x-ignore x-menu x-component')]//div" + "/a[contains(@class,'x-menu-item x-menu-check-item x-menu-item-arrow x-component x-menu-checked')]//img ";
    static final String UNCHECKED_CHECKBOX = " //div[contains(@class,' x-ignore x-menu x-component')]//div/a[contains(@class,'x-menu-item x-menu-check-item x-menu-item-arrow x-component ')]//img";
    static final String FILTERS_LABEL = "//div[contains(@class,'x-ignore x-menu x-component')]/div/div[4]/a";
    static final String LESS_THAN = "//div[contains(@class,'x-ignore x-menu x-component')]//*[count(div)=4]/div[1]/a/img";
    static final String GREATER_THAN = "//div[contains(@class,'x-ignore x-menu x-component')]//*[count(div)=4]/div[2]/a/img";
    static final String EQUAL_TO = "//div[contains(@class,'x-ignore x-menu x-component')]//*[count(div)=4]/div[4]/a/img";
    static final String CANCEL_BUTTON = "//div[contains(@class,'x-ignore x-menu date-picker-item x-component')]//div//table//em[button='Cancel']/button";
    static final String OK_BUTTON = "//div[contains(@class,'x-ignore x-menu date-picker-item x-component')]//div//table//em[button='OK']/button";
    static final String DATE_TIME_PICKER = "//div[contains(@class,' x-ignore x-menu date-picker-item x-component')]";
    static final String DATE_COMBOBOX = "//div[contains(@class,'x-ignore x-menu-plain x-menu-nosep x-menu x-date-menu x-component')]";
    static final String TIME_COMBOBOX = "//div[contains(@class,'x-combo-list x-ignore x-component x-border')]";
    static final String DAY_SELECTION = "//div[contains(@class,'x-ignore x-menu-plain x-menu-nosep x-menu x-date-menu x-component')]//table[contains(@class, 'x-date-inner')]//tr[1]/td[1]/a";
    static final String TIME_SELECTION = "//div[contains(@class,'x-combo-list x-ignore x-component x-border')]/div/div[2]";
    static final String SELENIUM_TAG_BASEWINDOW = "//div[contains(@id,'NETWORK_EVENT_ANALYSIS')]";

    /*Test case 4.3.2: "Filters" icon is a CheckBox*/
    @Test public void filterIconIsACheckBox_4_3_2() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net","Core PS", "Network Event Analysis");
        drillOnEventType();
        assertTrue("Filter icon is not a check box", selenium.isElementPresent(CHECKBOX));
    }

    /*Test case 4.3.3: the "Filters" CheckBox icon is Unchecked.*/
    @Test public void filterIconIsUnCheckedByDefault_4_3_3() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net",DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();
        waitForPageLoadingToComplete();
        selenium.click(EVENT_TIME);
        assertFalse("CheckBox is checked by Default", selenium.isElementPresent(CHECKBOX_CHECKED));
    }

    /* Test case 4.3.4: upon Hovering over the "Filters" label a Filter menu appears. */
    @Test public void hoveringOverTheFiltersFilterMenuAppears_4_3_4() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net",DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();
        assertTrue("Filter menu does not appeared", selenium.isElementPresent(FILTERS_LABEL));
    }

    /* Test case 4.3.5: this menu contains a greater than, a less than sign and an equals to box, to denote the effect that each TextBox has on the query.*/
    @Test public void menuContainsThreeSigns_4_3_5() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Three signs are verified against TextBoxes
        assertTrue("Less than sign is not present", selenium.isElementPresent(LESS_THAN));
        assertTrue("Greater than sign is not present", selenium.isElementPresent(GREATER_THAN));
        assertTrue("Equal sign is not present", selenium.isElementPresent(EQUAL_TO));
        logger.log(Level.INFO, "Contains Three different signs in the filters menu");
    }

    /*Test case 4.3.6: Beside each of these icons there is a TextBox*/
    @Test public void besideEachOfTheseIconsThereIsaTextBox_4_3_6() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Against each sign verified TextBox is present
        assertTrue("Less than input is not present", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[1]/a/div/input[contains(@type, 'text')]"));
        assertTrue("Greater than input is not present",selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[2]/a/div/input[contains(@type, 'text')]"));
        assertTrue("Equal to input is not present", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[4]/a/div/input[contains(@type, 'text')]"));
        logger.log(Level.INFO, "Beside each of these icons there is a TextBox");
    }

    /*Test case 4.3.7: beside each of these TextBoxes there is a Date/Time picker icon.*/
    @Test public void besideEachOfTheseTextBoXesThereIsADateTimePickerIcon_4_3_7() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Verified Date/Time picker icon
        assertTrue("Beside Less than input Date/Time picker icon is not present", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[1]/a/div/table//button"));
        assertTrue("Beside Greater than input Date/Time picker icon not present", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[2]/a/div/table//button"));
        assertTrue("Beside Equal than input Date/Time picker icon not present", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[4]/a/div/table//button"));
        logger.log(Level.INFO, "Beside each of these icons there is a TextBox");
    }

    /*Test case 4.3.9: clicking on the Date/Time picker icon brings up a Date/Time dialog box interface.*/
    @Test public void DateTimeDialogBoxInterface_4_3_9() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //verified Date/Time dialog box interface for less than
        improvingDateTimeFilterLessThan();
        assertTrue("Date/Time dialog interface was not present for less than", (selenium.isElementPresent(DATE_TIME_PICKER)));
        selenium.click(CANCEL_BUTTON);

        //for Greater than
        improvingDateTimeFilterGreaterThan();
        assertTrue("Date/Time dialog interface was not present for Greater than", (selenium.isElementPresent(DATE_TIME_PICKER)));
        selenium.click(CANCEL_BUTTON);

        //for Equal to
        improvingDateTimeFilterEqualTo();
        assertTrue("Date/Time dialog interface was not present for EqualTo", (selenium.isElementPresent(DATE_TIME_PICKER)));
        selenium.click(CANCEL_BUTTON);
        logger.log(Level.INFO, "Date time picker pop up exists");
    }

    /* Test case 4.3.10: the Date/Time Dialog has a Date selection ComboBox option.
     * Test case 4.3.11: the Date/Time Dialog has a Time selection ComboBox option.*/
    @Test public void dateTimeSelectionComboBoxOption_4_3_10() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Date/Time comboBox option for less than
        improvingDateTimeFilterLessThan();
        dateTimeComboboxForLesser();
        selenium.click(CANCEL_BUTTON);

        //for Greater than
        improvingDateTimeFilterGreaterThan();
        dateTimeComboboxForGreater();
        selenium.click(CANCEL_BUTTON);

        //for Equal to
        improvingDateTimeFilterEqualTo();
        dateTimeComboboxForEqualto();
        selenium.click(CANCEL_BUTTON);
        logger.log(Level.INFO, "Date/Time Dialog has a Date and Time selection ComboBox option");
    }

    /* Test case 4.3.14: upon making a selection in the Date/Time Dialog, the CheckBox icon beside "Filters" becomes checked.*/
    @Test public void selectionOfDateTimeFiltersCheckboxChecked_4_3_14() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Date Selection
        improvingDateTimeFilterLessThan();
        selenium.click(DATE_BUTTON);
        selenium.click(DAY_SELECTION);
        Thread.sleep(2000);

        //Time Selection
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        Thread.sleep(5000);
        selenium.click(EVENT_TIME);
        assertTrue("Check box unchecked", selenium.isElementPresent(CHECKBOX_CHECKED));

        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        //Date Selection
        improvingDateTimeFilterGreaterThan();
        selenium.click(DATE_BUTTON);
        selenium.click(DAY_SELECTION);
        Thread.sleep(2000);

        //Time Selection
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        Thread.sleep(5000);
        selenium.click(EVENT_TIME);
        assertTrue("Check box unchecked", selenium.isElementPresent(CHECKBOX_CHECKED));

        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        //Date Selection
        improvingDateTimeFilterEqualTo();
        selenium.click(DATE_BUTTON);
        selenium.click(DAY_SELECTION);
        Thread.sleep(2000);

        //Time Selection
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        Thread.sleep(5000);
        selenium.click(EVENT_TIME);
        assertTrue("Check box unchecked", selenium.isElementPresent(CHECKBOX_CHECKED));
    }

    /*Test case 4.3.15: upon making a selection, the selected date is in the appropriate TextBox.*/
    @Test public void selectedDateIsInAppropriateTextBox_4_3_15() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();
        improvingDateTimeFilterLessThan();
        Thread.sleep(2000);
        selenium.click(DATE_BUTTON);
        selenium.click("//div[contains(@class,'x-ignore x-menu-plain x-menu-nosep x-menu x-date-menu x-component')]//table[contains(@class, 'x-date-inner')]//tr[1]/td[1]/a");
        assertTrue("Selected date is not in the appropriate textbox",selenium.isElementPresent("//div[contains(@class,'x-ignore x-menu date-picker-item " + "x-component')]//div/input[contains(@class,'x-form-focus')]"));
    }

    /* Test case 4.3.18: verify that if a filter is applied in the greater than or less than, the equals to box will be reset to be empty*/
    @Test public void equalsToBoxWillBeResetTobeEmpty_4_3_18() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //driver.findElement(By.xpath("")).click();
        //Date&Time selection 
        improvingDateTimeFilterEqualTo();
        waitForPageLoadingToComplete();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        waitForPageLoadingToComplete();
        String value = selenium.getValue("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[4]/a/div/input[contains(@type, 'text')]");
        if (value != null && value.trim().length() > 0) {
            logger.log(Level.INFO, "Equals to textbox contains value");
        } else {
            logger.log(Level.INFO, "Equals to textbox is empty");
        }

        //Date&Time selection 
        improvingDateTimeFilterLessThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        improvingDateTimeFilterGreaterThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        Thread.sleep(5000);
        selenium.click(OK_BUTTON);
        if (value != null && value.trim().length() > 0) {
            logger.log(Level.INFO, "Equals to textbox is empty");
        } else {
            logger.log(Level.INFO, "Equals to textbox contains value");
        }
    }

    /* Test case4.3.19: verify that if a filter is applied in the equals to box, the greater than or less than will be reset to be empty.*/
    @Test public void greaterThanOrLessThanWillBeResetTobeEmpty_4_3_19() throws PopUpException, InterruptedException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Date&Time selection for less than
        improvingDateTimeFilterLessThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        String value = selenium.getValue("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[1]/a/div/input[contains(@type, 'text')]");
        if (value != null && value.trim().length() > 0) {
            logger.log(Level.INFO, "Lesser than textbox contains value");
        } else {
            logger.log(Level.INFO, "Lesser than textbox is empty");
        }

        //Date&Time selection for greater than
        improvingDateTimeFilterGreaterThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        Thread.sleep(5000);
        selenium.click(OK_BUTTON);
        String Value = selenium.getValue("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[2]/a/div/input[contains(@type, 'text')]");
        if (Value != null && Value.trim().length() > 0) {
            logger.log(Level.INFO, "Geater than textbox contains value");
        } else {
            logger.log(Level.INFO, "Greater than textbox is empty");
        }

        //Date&Time selection for equal to
        improvingDateTimeFilterEqualTo();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        selenium.click(OK_BUTTON);
        Thread.sleep(5000);
        if (value != null && value.trim().length() > 0) {
            logger.log(Level.INFO, "Lesser than textbox contains value");
        } else {
            logger.log(Level.INFO, "Lesser than textbox is empty");
        }

        if (Value != null && Value.trim().length() > 0) {
            logger.log(Level.INFO, "Greater than textbox contains value");
        } else {
            logger.log(Level.INFO, "Greater than textbox is empty");
        }
    }

    /* Test case 4.3.20: verify that the Date/Time Dialog disappears when OK is pressed.*/
    @Test public void dateTimeDialogDisappearsWhenOkIsPressed_4_3_20() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Dialog dissapears when OK is pressed
        improvingDateTimeFilterLessThan();
        selenium.click(OK_BUTTON);
        improvingDateTimeFilterGreaterThan();
        selenium.click(OK_BUTTON);
        improvingDateTimeFilterEqualTo();
        selenium.click(OK_BUTTON);
        logger.log(Level.INFO, "Date/Time Dialog dissapears when OK is pressed");
    }

    /* Test case 4.3.21: verify that the Date/Time Dialog disappears when the user clicks outside it*/
    @Test public void dateTimeDialogDisappearsWhenUserClicksOutside_4_3_21() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Dialog dissapears for less than
        improvingDateTimeFilterLessThan();
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        Thread.sleep(5000);
        assertFalse("Date time dialog did not dissapeared", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]"));
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        //Dialog dissapears for greater than
        improvingDateTimeFilterGreaterThan();
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        Thread.sleep(5000);
        assertFalse("Date time dialog did not dissapeared", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]"));
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        //Dialog dissapears for equal to
        improvingDateTimeFilterEqualTo();
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        Thread.sleep(5000);
        assertFalse("Date time dialog did not dissapeared", selenium.isElementPresent("//div[contains(@class,' x-ignore x-menu x-component')]"));
    }

    /* Test case 4.3.22: verify that the Date picker disappears when a date is selected.*/
    @Test public void datePickerDisappearsWhenDateIsSelected_4_3_22() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Date picker dissappears
        improvingDateTimeFilterLessThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));

        improvingDateTimeFilterGreaterThan();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));

        improvingDateTimeFilterEqualTo();
        selenium.click(DATE_BUTTON);
        Thread.sleep(2000);
        selenium.click(DAY_SELECTION);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
    }

    /*Test case 4.3.23: verify that the Date picker disappears when the user clicks outside it.*/
    @Test public void datePickerDisappearsWhenUserClicksOutside_4_3_23() throws PopUpException, InterruptedException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Date picker dissappears when clicks outside
        improvingDateTimeFilterLessThan();
        selenium.click(DATE_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        improvingDateTimeFilterGreaterThan();
        selenium.click(DATE_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        improvingDateTimeFilterEqualTo();
        selenium.click(DATE_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Date picker did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
    }

    /* Test case 4.324: verify that the Time picker drop down disappears when a time is selected*/
    @Test public void timePickerDropDownDisappearsWhenTimeIsSelected_4_3_24() throws InterruptedException, PopUpException , NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Time picker dissapears when time is selected
        improvingDateTimeFilterLessThan();
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        assertFalse("Time picker did not dissapeared", selenium.isElementPresent(TIME_COMBOBOX));

        improvingDateTimeFilterGreaterThan();
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        assertFalse("Time picker did not dissapeared", selenium.isElementPresent(TIME_COMBOBOX));

        improvingDateTimeFilterEqualTo();
        selenium.click(TIME_BUTTON);
        Thread.sleep(2000);
        selenium.mouseDown(TIME_SELECTION);
        assertFalse("Time picker did not dissapeared", selenium.isElementPresent(TIME_COMBOBOX));
    }

    /*  Test case 4.325: verify that the Time picker drop down disappears when the user clicks outside it.*/
    @Test public void timePickerDropDownDisappearsWhenUserClicksOutside_4_3_25() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        drillOnEventType();

        //Time picker dissappears when clicks outside
        improvingDateTimeFilterLessThan();
        selenium.click(TIME_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Time picker drop down did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));

        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        improvingDateTimeFilterGreaterThan();
        selenium.click(TIME_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Time picker drop down did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);

        improvingDateTimeFilterEqualTo();
        selenium.click(TIME_BUTTON);
        selenium.mouseDown(SELENIUM_TAG_BASEWINDOW);
        assertFalse("Time picker drop down did not dissapeared", selenium.isElementPresent(DATE_COMBOBOX));
    }

    // *********************************************** PRIVATE METHODS ********************************************** //

    private void drillOnEventType() throws PopUpException, InterruptedException, NoDataException {
        pause(5000);
        if (networkEventAnalysis.getTableRowCount() == 0) {
            throw new NoDataException("Current window has no data.");
        }
        selenium.click(DRILL_ON_EVENTTYPE_BY_APN);
        waitForPageLoadingToComplete();
        //Thread.sleep(5000);
        pause(5000);
        selenium.click(EVENT_TIME);
        selenium.mouseOver(FILTERS);
    }

    private void improvingDateTimeFilterLessThan() throws PopUpException, InterruptedException {
        selenium.click("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[1]/a/div/table//button");
        Thread.sleep(5000);
    }

    private void improvingDateTimeFilterGreaterThan() throws PopUpException, InterruptedException {
        selenium.click("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[2]/a/div/table//button");
        Thread.sleep(5000);
    }

    private void improvingDateTimeFilterEqualTo() throws PopUpException, InterruptedException {
        selenium.click("//div[contains(@class,' x-ignore x-menu x-component')]//*[count(div)=4]/div[4]/a/div/table//button");
        Thread.sleep(5000);
    }

    final String DATE_BUTTON = "//div[contains(@class,'x-ignore x-menu date-picker-item x-component')]//div/img[contains(@class, 'x-form-trigger x-form-date-trigger')]";
    final String TIME_BUTTON = "//div[contains(@class,'x-ignore x-menu date-picker-item x-component')]//div/img[contains(@class, 'x-form-trigger x-form-trigger-arrow')]";

    private void dateTimeComboboxForLesser() throws InterruptedException {
        selenium.click(DATE_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Date selection ComboBox option", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(TIME_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Time selection ComboBox option", selenium.isElementPresent(TIME_COMBOBOX));
    }

    private void dateTimeComboboxForGreater() throws InterruptedException {
        selenium.click(DATE_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Date selection ComboBox option", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(TIME_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Time selection ComboBox option", selenium.isElementPresent(TIME_COMBOBOX));
    }

    private void dateTimeComboboxForEqualto() throws InterruptedException {
        selenium.click(DATE_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Date selection ComboBox option", selenium.isElementPresent(DATE_COMBOBOX));
        selenium.click(TIME_BUTTON);
        Thread.sleep(5000);
        assertTrue("Date/Time Dialog has a Time selection ComboBox option", selenium.isElementPresent(TIME_COMBOBOX));
    }

    private void setDateAndTimerange() throws PopUpException, InterruptedException {
        String stDate = "2012,03,01";
        String edDate = "2012,04,12";

        final String[] startDateArray = stDate.split(",");
        final String[] endDateArray = edDate.split(",");
        final Date sDate = networkEventAnalysis.getDate(Integer.parseInt(startDateArray[0]), Integer.parseInt(startDateArray[1]), Integer.parseInt(startDateArray[2]));
        final Date eDate = networkEventAnalysis.getDate(Integer.parseInt(endDateArray[0]), Integer.parseInt(endDateArray[1]), Integer.parseInt(endDateArray[2]));
        networkEventAnalysis.setTimeAndDateRange(sDate, TimeCandidates.AM_00_00, eDate, TimeCandidates.AM_00_00);
        Thread.sleep(5000);
    }
}