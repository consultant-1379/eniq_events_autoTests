/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

import java.util.List;
import java.util.Map;

/**
 * @author ericker
 * @since 2010
 *
 */
public interface CauseCodeTableControls {
    public List<String> getTableHeaders();

    public int getTableHeaderCount();

    public List<Map<String, String>> getAllTableData();

    public int getCauseCodeItemCount();

    public List<String> getCauseCodeItemNames();

    public List<Map<String, String>> getCauseCodeItemAllData(final int item);

    public Map<String, String> getCauseCodeItemRow(final int item, final int row);

    public String getCauseCodeItemData(final int item, final int row, final int column);

    public int getCauseCodeItemRowCount(final int item);

    public void clickCauseCodeItem(final int item);

    public void clickTableCell(final int item, final int row, final int column);

    public void sortTable(final int column);

    public void filterColumnGreaterThan(int greaterValue, String columnHeader) throws PopUpException;

    public void filterColumnLessThan(final int column);

    public void filterColumnEquals(final String column, final String valToType) throws PopUpException;

    public List<String> getTableHeaderOptions();

    public boolean isCellClickable(final int item, final int row, final int column);
}
