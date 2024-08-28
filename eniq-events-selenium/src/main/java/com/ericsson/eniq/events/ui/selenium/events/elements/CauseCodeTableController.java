/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 *
 */
public class CauseCodeTableController implements CauseCodeTableControls {
    @Autowired
    private EricssonSelenium selenium;
    //private NewEricssonSelenium selenium  = NewEricssonSelenium.getSharedInstance();
    
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    final TableController tableController;

    final String divId;

    private final String windowXPath;

    private final String tableHeaderXPath;

    private final String itemsXPath;

    private final String itemHeaderXPath;

    private final String subItemXPath;

    public CauseCodeTableController(final String id) {
        divId = id;
        tableController = new TableController(divId);
        windowXPath = "//*[@id='selenium_tag_BaseWindow_" + id + "']";
        tableHeaderXPath = windowXPath + "//div[@id='" + id + "']//div[@class='x-grid3-header']//table";
        itemsXPath = windowXPath
                + "//div[@class='x-grid-group  x-grid-group-collapsed' or @class='x-grid-group x-grid-group-collapsed' or @class='x-grid-group ']";
        itemHeaderXPath = itemsXPath + "//div[@class='x-grid-group-hd']";
        subItemXPath = "//div[@class='x-grid3-row ' or @class='x-grid3-row  x-grid3-row-alt ']";
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#clickTableCell(int, int, int)
     */
    //@Override
    public void clickTableCell(final int item, final int row, final int column) {
        //TODO: Implement
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#filterColumnEquals(int)
     */
    //@Override
    public void filterColumnEquals(final String column, final String valToType) throws PopUpException {
        tableController.filterColumnEquals(column,valToType);
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#filterColumnGreaterThan(int)
     */
    //@Override
    public void filterColumnGreaterThan(int greaterValue, String columnHeader) throws PopUpException {
        tableController.filterColumnGreaterThan(greaterValue, columnHeader);
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#filterColumnLessThan(int)
     */
    //@Override
    public void filterColumnLessThan(final int column) {
        tableController.filterColumnLessThan(column);
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getAllTableData()
     */
    //@Override
    public List<Map<String, String>> getAllTableData() {
        final int itemCount = getCauseCodeItemCount();
        final List<Map<String, String>> tableData = new ArrayList<Map<String, String>>();
        for (int i = 0; i < itemCount; i++) {
            tableData.addAll(getCauseCodeItemAllData(i));
        }
        return tableData;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemAllData(int)
     */
    //@Override
    public List<Map<String, String>> getCauseCodeItemAllData(final int item) {
        final List<Map<String, String>> itemData = new ArrayList<Map<String, String>>();
        final int rowCount = getCauseCodeItemRowCount(item);
        for (int i = 0; i < rowCount; i++) {
            itemData.add(getCauseCodeItemRow(item, i));
        }
        return itemData;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemCount()
     */
    //@Override
    public int getCauseCodeItemCount() {
        return selenium.getXpathCount(itemsXPath).intValue();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemData(int, int, int)
     */
    //@Override
    public String getCauseCodeItemData(final int item, final int row, final int column) {
        final String data = "";
        // TODO: Implement
        return data;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemNames()
     */
    //@Override
    public List<String> getCauseCodeItemNames() {
        final int itemCount = getCauseCodeItemCount();
        final List<String> names = new ArrayList<String>();
        for (int i = 1; i < itemCount + 1; i++) {
            names.add(selenium.getText(itemHeaderXPath + "[" + i + "]"));
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemRow(int, int)
     */
    //@Override
    public Map<String, String> getCauseCodeItemRow(final int item, final int row) {
        final List<String> columnCount = getTableHeaders();
        final Map<String, String> rowData = new HashMap<String, String>();
        for (int i = 0; i < columnCount.size(); i++) {
            rowData.put(columnCount.get(i), getCauseCodeItemData(item, row, i));
        }
        return rowData;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getCauseCodeItemRowCount(int, int, int)
     */
    //@Override
    public int getCauseCodeItemRowCount(final int item) {
   	   loggerDuplicate.log(Level.INFO, "The Element ID : " + itemsXPath);
   	   loggerDuplicate.log(Level.INFO, "The Element ID : " + subItemXPath);
        return selenium.getXpathCount(itemsXPath + "[" + (item + 1) + "]" + subItemXPath).intValue();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getTableHeaderCount()
     */
    //@Override
    public int getTableHeaderCount() {
        return tableController.getTableHeaderCount();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getTableHeaderOptions()
     */
    //@Override
    public List<String> getTableHeaderOptions() {
        return tableController.getTableHeaderOptions();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#getTableHeaders()
     */
    //@Override
    public List<String> getTableHeaders() {
        return tableController.getTableHeaders();
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#isCellClickable(int, int)
     */
    //@Override
    public boolean isCellClickable(final int item, final int row, final int column) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#sortTable(int)
     */
    //@Override
    public void sortTable(final int column) {
        tableController.sortTable(column);
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.events.elements.CauseCodeTableControls#clickCauseCodeItem(int)
     */
    //@Override
    public void clickCauseCodeItem(final int item) {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + itemHeaderXPath);
        selenium.click(itemHeaderXPath + "[" + (item + 1) + "]");
    }

    // row: 0 based
    // Returns a 0 based list of column indicies
    private List<Integer> getTableDataValidColumnIndicies() {
        final List<Integer> validIndicies = new ArrayList<Integer>();
        final int itemCount = getCauseCodeItemCount();
        final int rowCount = getCauseCodeItemRowCount(0);
    	   loggerDuplicate.log(Level.INFO, "The Element ID : " + itemsXPath);
       	   loggerDuplicate.log(Level.INFO, "The Element ID : " + subItemXPath);
        if (itemCount > 0 && rowCount > 0) {
            final String tableRowXPath = itemsXPath + "[1]" + subItemXPath + "[1]";
            
            selenium.waitForElementToBePresent(tableRowXPath, "30000");
            final int columnCount = selenium.getXpathCount(tableRowXPath + "//td").intValue();
            //for each column - 1 based
            for (int i = 1; i < columnCount + 1; i++) {
                // check if the item has the style property of "display: none"
                if (!selenium.getAttribute(tableRowXPath + "//td[" + i + "]@style").contains("display: none")) {
                    validIndicies.add(i - 1); // 0 based
                }
            }
        }
        return validIndicies;
    }

}
