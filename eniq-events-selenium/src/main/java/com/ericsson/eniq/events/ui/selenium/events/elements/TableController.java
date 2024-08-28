/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ericker
 * @since 2010
 * 
 */
@Scope("prototype")
@Component
public class TableController implements TableControls {

    private static final String FILTERS_INPUT_TEXTBOX = "//div[contains(@class,'x-menu-list-item x-menu-list-item-indent')]//input";
    private static final String FILTERS_XPATH_IN_HEADERS_MENU = "//div[contains(@class,'x-ignore x-menu x-component')]//a[text()='Filters']";

    @Autowired private EricssonSelenium selenium;

    private final String tableHeaderXPath;
    private final String tableRowsXPath;
    private final String windowXPath;
    private final String tableControllerID;

    /**
     * updated by efreass 03 August 2012
     */
    public TableController(final String id) {
        tableControllerID = id;
        if (id.contains("SON")) {
            if (id.contains("CELL_DETAILS")) {
                windowXPath = "//div[@id='SONVIS_TAB']//div[@class='contentAreaPanel']//div[@class='contentTable']";
                tableHeaderXPath = "//div[@id='SONVIS_TAB']//div[@class='contentAreaPanel']//div[@class='contentTable']//div[@class='x-grid3-header']//table";
                tableRowsXPath = "//div[@id='SONVIS_TAB']//div[@class='contentAreaPanel']//div[@class='contentTable']//div[@class='x-grid3-body']/div";
            } else {
                windowXPath = "//*[contains(@id, 'selenium_tag_baseWindow" + id + "')]";
                tableHeaderXPath = "//div[contains(@id, '" + id + "')]//div[@class='x-grid3-header']//table";
                tableRowsXPath = "//div[@class='x-grid3-body']/div";
            }
        } else {
            /*windowXPath = "//*[contains(@id, 'selenium_tag_baseWindow_" + id
                    + "')][@class=\" x-window x-component \" or @class=\"x-window x-component\"]";*/

            //changed window x-path -- note the missing under-score after BaseWindow and missing id and change in case for baseWindow
            windowXPath = "//*[contains(@id, 'selenium_tag_baseWindow')][@class=\" x-window x-component \" or @class=\"x-window x-component\"]";

            if (id.startsWith("CS_")) {
                //using different x-path for MSS related windows
                tableHeaderXPath = "//div[@class='x-grid3-header']//table";
            } else {
                tableHeaderXPath = "//div[contains(@id, '" + id + "')]//div[@class='x-grid3-header']//table";
            }
            //working XPath for majority of tests
            //tableRowsXPath = "//div[@class='x-grid3-body']/div";
            // changed because it was not working for two windows
            tableRowsXPath = "//div[@class='x-grid3-body']/div[contains(@id,'" + id + "')]";
        }
    }

    public List<String> getTableHeaders() {
        final List<String> headers = new ArrayList<String>();
        final int columnCount = getTableHeaderCount();
        for (int i = 0; i < columnCount; i++) {
            headers.add(selenium.getTable(tableHeaderXPath + ".0." + i));

        }
        return headers;
    }

    public List<String> clickTableHeadersMenu() {
        final List<String> headers = new ArrayList<String>();
        final int columnCount = getTableHeaderCount();
        for (int i = 0; i < columnCount; i++) {
            headers.add(selenium.getTable(tableHeaderXPath + ".0." + i));
            // System.out.println("Table HeaderXpath="+tableHeaderXPath);
        }
        return headers;
    }

    public List<Map<String, String>> getAllTableData() throws NoDataException {
        final List<Map<String, String>> tableData = new ArrayList<Map<String, String>>();
        final List<String> columnHeaders = getTableHeaders();
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(0);

        for (int row = 0; row < getTableRowCount(); row++) {
            tableData.add(getAllDataAtCurrentRow(row, columnHeaders, validColumnIndicies));
        }

        return tableData;
    }

    public List<Map<String, String>> getAllPagesData() throws NoDataException {
        final List<Map<String, String>> allData = new ArrayList<Map<String, String>>();
        //FOR_NEW_UI
        /* final String pageCount = windowXPath
                 + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[6]//div";*/

        final String pageCount = "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[6]//div";

        final String pageCountNumberText = selenium.getText(pageCount);
        int numOfPages = 0;

        if (pageCountNumberText != null && !pageCountNumberText.isEmpty()) {
            final String pageCountNumber = pageCountNumberText.substring("of ".length(), pageCountNumberText.length());
            numOfPages = Integer.parseInt(pageCountNumber);
        }

        for (int i = 0; i < numOfPages; i++) {
            allData.addAll(getAllTableData());
            //FOR_NEW_UI
            /*            selenium.click(windowXPath
                    + "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[8]//button");
             */
            selenium.click("//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[8]//button");

        }

        return allData;

    }
    
	private Map<String, String> getAllDataAtCurrentRow(final int row, final List<String> columnHeaders, final List<Integer> validColumnIndicies) {
        final Map<String, String> rowData = new HashMap<String, String>();

        for (int indexOfColumn = 0; indexOfColumn < columnHeaders.size(); indexOfColumn++) {
            // in case no data at current column we don't throw a NoDataException
            // instead put empty string as this column's value
            final String tableCellAddress = tableRowsXPath + "[" + (row + 1) + "]//table.0."
                    + validColumnIndicies.get(indexOfColumn);
            final String data = selenium.getTable(tableCellAddress);

            if (data.isEmpty()) {
                rowData.put(columnHeaders.get(indexOfColumn), "");
            } else {
                rowData.put(columnHeaders.get(indexOfColumn), data);
            }
        }
        return rowData;
    }

    /**
     * Return all data at current row. 
     * @param row
     * @return Map<String, String> - key : column name, value: data at current row & column
     * @throws NoDataException 
     */
    public Map<String, String> getAllDataAtTableRow(final int row) throws NoDataException {
        final Map<String, String> rowData = new HashMap<String, String>();
        final List<String> columnHeaders = getTableHeaders();
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);

        for (int index = 0; index < columnHeaders.size(); index++) {
            final String tableCellAddress = tableRowsXPath + "[" + (row + 1) + "]//table.0."
                    + validColumnIndicies.get(index);
            final String data = selenium.getTable(tableCellAddress);

            if (data.isEmpty()) {
                rowData.put(columnHeaders.get(index), "");
            } else {
                rowData.put(columnHeaders.get(index), data);
            }
        }

        return rowData;
    }

    /**
     * Gets the data from a single square
     * 
     * @param row
     * @param column
     * @return
     * @throws NoDataException
     */
    public String getTableData(final int row, final int column) throws NoDataException {

        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);
        final String tableCellAddress = tableRowsXPath + "[" + (row + 1) + "]//table.0."
                + validColumnIndicies.get(column);
        final String data = selenium.getTable(tableCellAddress);

        if (data.isEmpty()) {
            throw new NoDataException("tableCellAddress : " + tableCellAddress);
        }
        return data;
    }

    /**
     * @param row: 0 based
     * @return a 0 based list of column indices
     */
    private List<Integer> getTableDataValidColumnIndicies(final int row) throws NoDataException {
        final List<Integer> validIndices = new ArrayList<Integer>();
        final int rowCount = getTableRowCount();
        if (rowCount > 0) {
            selenium.waitForElementToBePresent(tableRowsXPath + "[1]", "30000");
            final int columnCount = selenium.getXpathCount(tableRowsXPath + "[1]//td").intValue();

            // for each column - 1 based
            for (int i = 1; i < columnCount + 1; i++) {
                // check if the item has the style property of "display: none"
                final String styleAttr = selenium.getAttribute(tableRowsXPath + "[" + (row + 1) + "]//td[" + i
                        + "]@style");
                if (!(styleAttr.contains("display: none") || styleAttr.contains("display:none"))) {
                    validIndices.add(i - 1); // 0 based
                }
            }
        }

        return validIndices;
    }

    public int getTableRowCount() throws NoDataException {
        final int rowCount = selenium.getXpathCount(tableRowsXPath).intValue();
        final int emptyRowBarCount = selenium.getXpathCount(
                "//div[@class='x-grid3-body']/div[contains(@class,'x-grid-empty')]").intValue();
        final int totalData = rowCount - emptyRowBarCount;

        if (totalData == 0) {
            throw new NoDataException("Current window has no data.");
        }
        return totalData;
    }

    public void clickTableCell(final int row, final String columnHeader) throws NoDataException, PopUpException {
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);
        final int column = getTableHeaders().indexOf(columnHeader);
        final String linkXPath = tableRowsXPath + "[" + (row + 1) + "]//td[" + (validColumnIndicies.get(column) + 1)
                + "]//span[@class='gridCellLink' or @class='gridCellLauncherLink']";

        if (!selenium.isElementPresent(linkXPath) || selenium.getText(linkXPath).isEmpty()) {
            throw new NoDataException(" Row: " + row + " of Column: " + columnHeader + " - XPath of table cell: "
                    + linkXPath);
        }
        selenium.click(linkXPath);
        selenium.waitForPageLoadingToComplete();
    }

    public void clickTableCell(final int row, final int column, final String spanId) throws NoDataException,
            PopUpException {
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);
        final String linkXPath = tableRowsXPath + "[" + (row + 1) + "]//td[" + (validColumnIndicies.get(column) + 1)
                + "]//span[contains(@class, '" + spanId + "')]";

        selenium.click(linkXPath);
        selenium.waitForPageLoadingToComplete();
    }

    public void clickTableCell(final int row, final String columnHeader, final String spanId) throws NoDataException,
            PopUpException {
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);
        final int column = getTableHeaders().indexOf(columnHeader);
        final String linkXPath = tableRowsXPath + "[" + (row + 1) + "]//td[" + (validColumnIndicies.get(column) + 1)
                + "]//span[contains(@class, '" + spanId + "')]";

        selenium.click(linkXPath);
        selenium.waitForPageLoadingToComplete();
    }

    public String selectTableCell(final int row, final String columnHeader) throws NoDataException, PopUpException {
        final int column = getTableHeaders().indexOf(columnHeader);
        final List<Integer> validColumnIndicies = getTableDataValidColumnIndicies(row);
        StringBuilder sb = new StringBuilder();
        sb.append(tableRowsXPath);
        sb.append("[");
        sb.append(row + 1);
        sb.append("]//td[");
        sb.append(validColumnIndicies.get(column) + 1);
        sb.append("]");
        return sb.toString();
    }

    public void clickRankingDrills(final String spanId, final String drillOn) throws NoDataException, PopUpException {
        String path = "";
        if (drillOn.equals("failure")) {
            path = windowXPath + "//span[@class= '" + spanId + "']";
        } else if (drillOn.equals("rawEvent")) {
            path = windowXPath + "//span[@id= '" + spanId + "']";
        } else {
            path = windowXPath + "//*[@id='" + spanId + "']";
        }

        if (!selenium.isElementPresent(path) || selenium.getText(path).isEmpty()) {
            throw new NoDataException("XPath is : " + path);
        }
        selenium.clickAt(path, "0,0");
        selenium.waitForPageLoadingToComplete();
    }

    /**
     * Written by elukpot.
     *
     * Very similar to {@code clickRankingDrills}, but this method checks that the column it's about to drill on does
     * not have a zero value.
     *
     * @param rowDivId The id attribute of the div node of the row to drill on.
     * @param drillColumnSpanId The id attribute of the span node of the column to drill on.
     */
    public void clickRankingDrillsNonZeroValue(final String rowDivId, final String drillColumnSpanId) throws NoDataException, PopUpException {
        
        String divNode = "//div[contains(@id, '" + rowDivId + "')][";
        String spanNode = "]//span[@id='" + drillColumnSpanId + "']";
        int rowNumber = 0, numberOfFailures = 0;
        
        do {
            rowNumber++;

            if (selenium.isElementPresent(selenium.getText(divNode + rowNumber + spanNode)))
                throw new NoDataException("No acceptable data for grid.");

            numberOfFailures = Integer.parseInt(selenium.getText(divNode + rowNumber + spanNode + "[text()]"));
        } while (numberOfFailures == 0);

        selenium.click(divNode + rowNumber + spanNode);
        selenium.waitForPageLoadingToComplete();
    }

    public void clickCauseCodes(final String spanId) throws NoDataException {
        final String path = windowXPath + "//span[@class= '" + spanId + "']";

        if (!selenium.isElementPresent(path) || selenium.getText(path).isEmpty()) {
            throw new NoDataException("XPath is : " + path);
        }
        selenium.clickAt(path, "0,0");
    }

    // TODO eseuhon, replace this with sortTable(final SortType order, final String name)
    public void sortTable(final int column) {
        final int columnCount = getTableHeaderCount();
        if (column >= 0 && column < columnCount) {
            selenium.click(tableHeaderXPath + "//td[" + (column + 1) + "]/div");
        }
    }

    public void sortTable(final SortType order, final String name) {
        openTableHeaderMenu(getTableHeaders().indexOf(name));
        selenium.click(order.getXPath());
    }

    public void openTableHeaderMenu(final int column) {
        final int columnCount = getTableHeaderCount();
        if (column >= 0 && column < columnCount) {
            selenium.click(tableHeaderXPath + "//td[" + (column + 1) + "]/div/a");
        }
    }

    //updated by efreass 03 August 2012
    public void openGridView() throws PopUpException {
        final String radioButton = "//div[@class='x-panel-bwrap']//label[contains(text(), 'Grid View')]";
        final String launchButton = "//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader']//button[text()='Launch']";
        if (selenium.isElementPresent(radioButton)) {
            selenium.click(radioButton);
            selenium.click(launchButton);
            selenium.waitForPageLoadingToComplete();
        } else {
            System.err.println(this.getClass().toString() + ": XPath not found for Grid View Radio Button : "
                    + radioButton);
        }
    }

    public void openChartView(final List<String> defaultViewContentOnEventVolumeVoice) throws PopUpException {
        //FOR_NEW_UI
        final String radioButton = /*windowXPath +*/"//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body wizard-top-body']//label[contains(text(), 'Chart View')]";

        final String launchButton = /*windowXPath +*/"//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader']//div[@class=' x-component']//button";

        final String chartViewXPath = /*windowXPath +*/"//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader wizard-content-body']//span";

        selenium.click(radioButton);

        for (final String item : defaultViewContentOnEventVolumeVoice) {
            selenium.click(chartViewXPath + "[@title='" + item + "']//input");
        }

        selenium.click(launchButton);

        selenium.waitForPageLoadingToComplete();
    }

    /**
     * By eikrwaq
     * Select item Using names displayed in UI
     * @param itemsToSelectList
     * @throws PopUpException
     */
    public void openChartViewUsingDisplayedNames(final List<String> itemsToSelectList) throws PopUpException {
        //FOR_NEW_UI
        final String radioButton = "//div[@class='x-panel-bwrap']//label[contains(text(), 'Chart View')]";

        final String launchButton = "//div[@class='x-panel-bwrap']//div[@class='x-panel-body x-panel-body-noheader']//button[text()='Launch']";

        final String chartViewXPath = "//div[@class='x-panel-bwrap']//span[contains(@class,'gwt-CheckBox')]";

        selenium.click(radioButton);

        for (final String item : itemsToSelectList) {
            selenium.click(chartViewXPath + "//label[text()='" + item + "']");
        }
        selenium.click(launchButton);

        selenium.waitForPageLoadingToComplete();
    }

    public void openChartViewViaClickingSingleSelectAllCheckBox() {
        
        final String wizardPath = "//div[@class='x-panel-bwrap']";
        final String chartViewRadioButton = wizardPath + "//label[text()='Chart View']";
        final String selectAllCheckBox = wizardPath + "//label[text()='Select all']";
        final String launchButton = wizardPath + "//button[text()='Launch']";

        selenium.click(chartViewRadioButton);
        selenium.click(selectAllCheckBox);
        selenium.click(launchButton);
    }

    /**
     * updated by efreass 17 August 2012
     * more appropriate for rankingTestGroup hfa/cfa
     * will be refactor later to be more usable or merge with code above
     */
    public void openChartView() {
        final String CHART_RADIO_BUTTON = "//div[contains(@id, 'selenium_tag_BaseWindow_WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS')]//div[@class='x-window-bwrap']//div[@class='x-panel-bwrap']//div[@class='x-panel-body x-panel-body-noheader'] /div/div[2]/div/span[1]/input";
        //"//div[contains(@id, 'selenium_tag_BaseWINDOW_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Chart View']/input";
        final String CHART_SELECT_ALL = "//div[contains(@id, 'selenium_tag_BaseWindow_WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS')]//div[@class='x-window-bwrap']//div[@class='x-panel-bwrap']//div[@class='x-panel-body x-panel-body-noheader']//div//div[2]/span/input";
        final String CHART_LAUNCH_BUTTON = "//div[contains(@id, 'selenium_tag_BaseWindow_WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS')]//div[@class='x-window-bwrap']//div[@class='x-panel-bwrap']//div[@class='x-panel-body x-panel-body-noheader']//div//div[2]//div[3]/button";
        System.out.println(" ############# BUTTONS EXIST ");
        if (selenium.isElementPresent(CHART_RADIO_BUTTON)) {
            System.out.println(" ############# if(selenium.isElementPresent(CHART_RADIO_BUTTON)) "
                    + selenium.isElementPresent(CHART_RADIO_BUTTON));
            selenium.click(CHART_RADIO_BUTTON);

            //  selenium.click(CHART_RADIO_BUTTON);
            if (selenium.isElementPresent(CHART_SELECT_ALL)) {
                System.out.println(" ############# if(selenium.isElementPresent(CHART_SELECT_ALL)) "
                        + selenium.isElementPresent(CHART_SELECT_ALL));
                selenium.click(CHART_SELECT_ALL);
                //  selenium.click(CHART_SELECT_ALL);
                if (selenium.isElementPresent(CHART_LAUNCH_BUTTON))
                    System.out.println(" ############# if(selenium.isElementPresent(CHART_LAUNCH_BUTTON)) "
                            + selenium.isElementPresent(CHART_LAUNCH_BUTTON));
                selenium.click(CHART_LAUNCH_BUTTON);
                // selenium.click(CHART_LAUNCH_BUTTON);
            }
        }
    }

    /**
     * ekumpen
     */
    public void filterColumnGreaterThan(final int greaterValue, final String columnHeader) throws PopUpException {
        final String linkXpath = "//table//div[span='" + columnHeader + "']/a";
        selenium.click(linkXpath);
        selenium.mouseOver("//div[contains(@class,' x-ignore x-menu x-component')]//div/a[contains(@class,'x-menu-item x-menu-check-item "
                + "x-menu-item-arrow x-component')]");
        selenium.waitForElementToBePresent(
                "//div[contains(@class,'x-ignore x-menu x-component')]/div[@class=' x-menu-list']/div[2]/a/div/input",
                "3000");
        final String GreaterValue = Integer.toString(greaterValue);
        selenium.type(
                "//div[contains(@class,'x-ignore x-menu x-component')]/div[@class=' x-menu-list']/div[2]/a/div/input",
                GreaterValue);
        selenium.waitForPageLoadingToComplete();

    }

    public void filterColumnLessThan(final int column) {
        // TODO Implement this
    }

    /** by eikrwaq
     * Only Works with Columns which are Text not number, or in other words, works only when there is only 1 textbox in filter's menu
     */
    public void filterColumnEquals(final String column, final String valToType) throws PopUpException {
        selenium.setSpeed("100");
        final String linkXpath = "//table//div[span='" + column + "']/a";
        selenium.click(linkXpath);
        selenium.mouseOver(FILTERS_XPATH_IN_HEADERS_MENU);
        selenium.waitForElementToBePresent(FILTERS_XPATH_IN_HEADERS_MENU, "3000");
        selenium.focus(FILTERS_INPUT_TEXTBOX);
        selenium.type(FILTERS_INPUT_TEXTBOX, valToType);
        selenium.click(FILTERS_XPATH_IN_HEADERS_MENU + "//img");
        selenium.waitForPageLoadingToComplete();
        selenium.setSpeed("0");
    }

    public int getTableHeaderCount() {
        selenium.waitForElementToBePresent(tableHeaderXPath, "30000");
        final int columnCount = selenium.getXpathCount(tableHeaderXPath + "//td").intValue();
        //        System.out.println("Column Count: " + columnCount);
        return columnCount;
    }

    public List<String> getTableHeaderOptions() {
        // TODO Implement this
        return null;
    }

    public void checkInOptionalHeaderCheckBoxes(final List<String> optionalHeadersToBeChecked, final String... headerCheckBoxGroup) {

        //        selenium.click(tableHeaderXPath + "//div[@class='x-grid3-header']//table");
        selenium.click(tableHeaderXPath + "//a[contains(@class,'x-grid3-hd-btn')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");

        if ((headerCheckBoxGroup.length != 0)) {
            for (final String option : headerCheckBoxGroup) {
                selenium.mouseOver("//div[contains(@class,'x-menu-list')]//a[contains(text(),'" + option + "')]");

                for (final String header : optionalHeadersToBeChecked) {
                    if (!selenium
                            .isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='"
                                    + header + "']")) {
                        if (selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='"
                                + header + "']")) {
                            selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header
                                    + "']");
                        }
                    }
                    /*if(!selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='" + header + "']")){
                    	selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header + "']");
                    }else{
                     	selenium.click("//a[contains(@class, 'x-menu-checked')][text()='" + header + "']");
                    }*/
                }
            }
        } else {
            for (final String header : optionalHeadersToBeChecked) {
                if (!selenium
                        .isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='"
                                + header + "']")) {
                    if (selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='"
                            + header + "']")) {
                        selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header
                                + "']");
                    }
                }
            }
        }

        // Press the ESC key        
        selenium.keyPressNative("27");
        //selenium.click(windowXPath);        

        //selenium.deselectPopUp();

    }

    public void checkInOptionalHeaderCheckBoxes(final String option, final String headerCheckBoxGroup, final String optionalHeadersToBeChecked) {

        // selenium.click(tableHeaderXPath +
        // "//div[@class='x-grid3-header']//table");
        selenium.click(tableHeaderXPath + "//a[contains(@class,'x-grid3-hd-btn')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");

        selenium.mouseOver("//div[contains(@class,'x-menu-list')]//a[contains(text(),'" + headerCheckBoxGroup + "')]");
        selenium.mouseOver("//div[contains(@class,'x-menu-list')]//a[contains(text(),'" + option + "')]");

        final String header = optionalHeadersToBeChecked;
        if (!selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='"
                + header + "']")) {
            if (selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header
                    + "']")) {
                selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header + "']");
            }
        }

        // Press the ESC key
        selenium.keyPressNative("27");
        // selenium.click(windowXPath);

        // selenium.deselectPopUp();

    }

    public void uncheckOptionalHeaderCheckBoxes(final List<String> optionalHeadersToBeUnchecked,
            final String... headerCheckBoxGroup) throws InterruptedException {

        selenium.click(tableHeaderXPath + "//a[contains(@class,'x-grid3-hd-btn')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");

        if (headerCheckBoxGroup.length != 0) {//new UI
            for (final String option : headerCheckBoxGroup) {
                selenium.mouseOver("//div[contains(@class,'x-menu-list')]//a[contains(text(),'" + option + "')]");

                for (final String header : optionalHeadersToBeUnchecked) {
                    if (selenium
                            .isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='"
                                    + header + "']")) {
                        selenium.click("//a[contains(@class, 'x-menu-checked')][text()='" + header + "']");
                    }/*else{
                     selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header + "']");
                     
                     }*/
                }
            }
        } else {//Old UI 
            for (final String header : optionalHeadersToBeUnchecked) {
                if (selenium
                        .isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked')][text()='"
                                + header + "']")) {
                    Thread.sleep(3000);
                    selenium.click("//a[contains(@class, 'x-menu-checked')][text()='" + header + "']");
                }/*else{
                 selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][text()='" + header + "']");
                 
                 }*/
            }
        }

        // Press the ESC key        
        selenium.keyPressNative("27");

    }

    @Deprecated public void checkInKPIViewCheckBoxes(final List<String> optionalHeadersToBeChecked, final String... headerCheckBoxGroup) {
        for (final String header : optionalHeadersToBeChecked) {
            selenium.click("//a[contains(@class, 'x-menu-item x-menu-check-item')][contains(text(),'" + header + "')]");
        }

    }

    public void clickConfigureBarAndConfigureItems(final List<String> optionParameters) throws PopUpException {
        final String configureXPath = windowXPath + "//div[@class='x-window-bwrap']//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader']";
        selenium.click(configureXPath + "//div[@class=' x-small-editor x-panel-header x-component x-unselectable']");
        // added x-component as other class didn't exist (eosudam)
        for (final String parameter : optionParameters) {
            selenium.click(configureXPath
                    + "//div[contains(@class, 'x-panel-body x-panel-body-noheader wizard-content-body')]"
                    + "//span[@title='" + parameter + "']//input");
        }
        final String launchButton = configureXPath + "//div[@class=' x-component']//button";
        // changed the path to the launch button
        selenium.click(launchButton);
        selenium.waitForPageLoadingToComplete();
    }

    public void clickConfigureBarAndConfigureItemsForMSS(final List<String> optionParameters) throws PopUpException {
        final String configureXPath = windowXPath + "//div[@class='x-window-bwrap']//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader']";

        final String launchButton = windowXPath + "//div[@class='x-panel-bwrap']"
                + "//div[@class='x-panel-body x-panel-body-noheader']//div[@class=' x-component']//button";

        //selenium.click(configureXPath + "//div[@class=' x-small-editor x-panel-header x-unselectable']");
        selenium.click(configureXPath + "//div[@class=' x-small-editor x-panel-header x-component x-unselectable']");
        for (final String parameter : optionParameters) {
            if (!selenium.isChecked("//span[@title='" + parameter + "']//input")) {
                selenium.click(configureXPath
                        + "//div[contains(@class, 'x-panel-body x-panel-body-noheader wizard-content-body')]"
                        + "//span[@title='" + parameter + "']//input");
            }
        }
        //selenium.click(configureXPath + "//div[@class='x-panel-body x-panel-body-noheader wizard-btns-body']//button");
        selenium.click(launchButton);
        selenium.waitForPageLoadingToComplete();
    }

    public int findFirstTableRowWhereMatchingAnyValue(final String columnToCheck, final String[] values) throws NoDataException {
        int row = 0; // set first row by default
        int index = 0;
        boolean found = false;
        //find out first matching row which column has any values
        for (final Map<String, String> currentData : getAllTableData()) {
            for (final String value : values) {
                if (currentData.get(columnToCheck).equals(value)) {
                    row = index;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
            index++;
        }

        if (!found) {
            throw new NoDataException("Can't find any matching data: " + Arrays.toString(values) + " of Column: "
                    + columnToCheck + " in current window");
        }
        return row;
    }
    
    public List<String> getAllPagesDataAtColumn(final String column) throws NoDataException {
        final List<String> data = new ArrayList<String>();

        final String pageCount = "//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[6]//div";

        final String pageCountNumberText = selenium.getText(pageCount);
        int numOfPages = 0;

        if (pageCountNumberText != null && !pageCountNumberText.isEmpty()) {
            final String pageCountNumber = pageCountNumberText.substring("of ".length(), pageCountNumberText.length());
            numOfPages = Integer.parseInt(pageCountNumber);
        }

        for (int i = 0; i < numOfPages; i++) {
            data.addAll(getAllTableDataAtColumn(column));
            selenium.click("//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[8]//button");
        }

        return data;
    }

    public List<String> getAllTableDataAtColumn(final String column) throws NoDataException {
        final List<String> result = new ArrayList<String>();
        final int columnIndex = getTableHeaders().indexOf(column);
        final List<Integer> validColumnIndices = getTableDataValidColumnIndicies(0);

        String tableCellAddress, data;
        for (int i = 0; i < getTableRowCount(); i++) {
            tableCellAddress = tableRowsXPath + "[" + (i + 1) + "]/table.0." + validColumnIndices.get(columnIndex);
            data = selenium.getTable(tableCellAddress);
            if (!data.isEmpty()) {
                result.add(data);
            }
        }
        return result;
    }

    public List<Map<String, String>> getALLRowsWithMatchingValueForGivenColumn(final String column, final String value)
            throws NoDataException {
        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        final List<String> columnHeaders = getTableHeaders();
        final int columnIndex = columnHeaders.indexOf(column);
        final List<Integer> validColumnIndices = getTableDataValidColumnIndicies(0);

        String tableCellAddress, data;
        for (int i = 0; i < getTableRowCount(); i++) {
            tableCellAddress = tableRowsXPath + "[" + (i + 1) + "]/table.0." + validColumnIndices.get(columnIndex);
            data = selenium.getTable(tableCellAddress);
            if (!data.isEmpty()) {
                if (data.equals(value)) {
                    final Map<String, String> rowData = new HashMap<String, String>();
                    for (int index = 0; index < columnHeaders.size(); index++) {
                        final String matchedtableCellAddress = tableRowsXPath + "[" + (i + 1) + "]//table.0."
                                + validColumnIndices.get(index);
                        final String matcheddata = selenium.getTable(matchedtableCellAddress);
                        if (matcheddata.isEmpty()) {
                            rowData.put(columnHeaders.get(index), "");
                        } else {
                            rowData.put(columnHeaders.get(index), matcheddata);
                        }
                    }
                    result.add(rowData);
                }
            }
        }
        return result;
    }

    public int getRowNumberWithMatchingValueForGivenColumn(final String column, final String value) throws NoDataException {
        boolean found = false;
        int row = -1;
        final List<String> columnHeaders = getTableHeaders();
        final int columnIndex = columnHeaders.indexOf(column);
        final List<Integer> validColumnIndices = getTableDataValidColumnIndicies(0);

        String tableCellAddress, data;
        for (int i = 0; i < getTableRowCount(); i++) {
            tableCellAddress = tableRowsXPath + "[" + (i + 1) + "]/table.0." + validColumnIndices.get(columnIndex);
            data = selenium.getTable(tableCellAddress);
            if (!data.isEmpty()) {
                if (data.equals(value)) {
                    found = true;
                    row = i;
                    break;
                }
            }
        }
        if (!found) {
            throw new NoDataException("Can't find any matching data: " + value + " of Column: " + column
                    + " in current window");
        }
        return row;
    }

    public boolean isCSSClassPresentOnTableRow(final int row, final String cssClassName) {
        String xPath = "//div[@class='x-grid3-body']/div[" + row + "][contains(concat(' ',@class,' '), ' "
                + cssClassName + " ')]";
        return selenium.isElementPresent(xPath);
    }

    /**
     * This method is used to check the selected menu options in the column selection menu popup.
     * It takes in a list of Menu Option titles that are expected to be selected in the menu.
     * @param selectedMenuOptions
     * @return List - returns a list of menu options that should have been selected but were not.
     */
    public List<String> isSelectedMenuOptions(String... selectedMenuOptions) {
        List<String> result = new ArrayList<String>();
        selenium.click(tableHeaderXPath + "//a[contains(@class,'x-grid3-hd-btn')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");

        if (selectedMenuOptions.length != 0) {
            for (final String selectedMenuOption : selectedMenuOptions) {
                if (!selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-menu-checked x-component')][text()='"
                        + selectedMenuOption + "']")){
                    result.add(selectedMenuOption);
                }
            }
        }
        return result;
    }

    /**
     * This method is used to check the unselected menu options in the column selection menu popup.
     * It takes in a list of Menu Option titles that are expected to be unselected in the menu.
     * @param unselectedMenuOptions
     * @return List - returns a list of menu options that should have been unselected but were selected.
     */
    public List<String> isUnselectedMenuOptions(String... unselectedMenuOptions) {
        List<String> result = new ArrayList<String>();
        selenium.click(tableHeaderXPath + "//a[contains(@class,'x-grid3-hd-btn')]");
        selenium.mouseOver("//div[contains(@class,' x-menu-list')]//a[contains(text(), 'Columns')]");

        if (unselectedMenuOptions.length != 0) {
            for (final String unselectedMenuOption : unselectedMenuOptions) {
                if (!selenium.isElementPresent("//a[contains(@class, 'x-menu-item x-menu-check-item x-component')][text()='"
                        + unselectedMenuOption + "']")){
                    result.add(unselectedMenuOption);
                }
            }
        }
        return result;
    }
}
