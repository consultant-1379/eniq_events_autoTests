/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

import java.util.List;
import java.util.Map;

/**
 * @author ericker
 * @since 2010
 * 
 */
public interface TableControls {

    public List<String> getTableHeaders();

    public int getTableHeaderCount();

    public List<Map<String, String>> getAllTableData() throws NoDataException;

    public Map<String, String> getAllDataAtTableRow(final int row) throws NoDataException;

    public String getTableData(final int row, final int column) throws NoDataException;

    public int findFirstTableRowWhereMatchingAnyValue(final String columnToCheck, final String[] values)
            throws NoDataException;

    public int getTableRowCount() throws NoDataException;

    public void clickTableCell(final int row, final String columnHeader) throws NoDataException, PopUpException;

    public void sortTable(final int column);

    public void filterColumnGreaterThan(int greaterValue, String columnHeader) throws PopUpException;

    public void filterColumnLessThan(final int column);

    public void filterColumnEquals(final String column, final String valToType) throws PopUpException;

    public List<String> getTableHeaderOptions();

    void clickTableCell(int row, int column, String spanId) throws NoDataException, PopUpException;

    public void clickCauseCodes(String spanId) throws NoDataException;

    public void clickRankingDrills(String spanId, String drillOn) throws NoDataException, PopUpException;

    public void openTableHeaderMenu(final int column);

    public void checkInOptionalHeaderCheckBoxes(final List<String> headerOptionsToBeAdded, String... options);

    public void uncheckOptionalHeaderCheckBoxes(final List<String> headerOptionsToBeRemoved, String... options)
            throws InterruptedException;

    @Deprecated
    public void checkInKPIViewCheckBoxes(final List<String> optionalHeadersToBeChecked,
            final String... headerCheckBoxGroup);

    /**
     * @param column table column name to fetch data from
     * @return not <tt>null</tt> and not empty result data list
     * @throws NoDataException no data (e.g. when empty) exception
     */
    public List<String> getAllTableDataAtColumn(final String column) throws NoDataException;

    public void checkInOptionalHeaderCheckBoxes(String option, String headerCheckBoxGroup,
            String optionalHeadersToBeChecked);

    public List<Map<String, String>> getALLRowsWithMatchingValueForGivenColumn(final String column, final String value)
            throws NoDataException;

    public void clickTableCell(final int row, final String column, final String spanId) throws NoDataException,
            PopUpException;

    public int getRowNumberWithMatchingValueForGivenColumn(final String column, final String value)
            throws NoDataException;
}
