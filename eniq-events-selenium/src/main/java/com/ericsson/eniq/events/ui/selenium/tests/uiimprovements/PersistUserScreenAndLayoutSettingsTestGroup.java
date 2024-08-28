package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants.*;
import static com.ericsson.eniq.events.ui.selenium.tests.uiimprovements.DataIntegrityStringConstants.SGSN_SEARCH_VALUE2;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 05 2012
 */
//@SuppressWarnings({"JUnitTestClassNamingConvention", "JUnit4AnnotatedMethodInJUnit3TestCase"})
public class PersistUserScreenAndLayoutSettingsTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired private NetworkTab networkTab;
    @Autowired WorkspaceRC workspaceRC;
    @Autowired @Qualifier("networkEventAnalysis") private CommonWindow networkEventAnalysis;
    static final String CURRENT_WINDOW = "//div[@id='NETWORK_EVENT_ANALYSIS']//div[contains(@class,'x-grid3-scroller')]";
   
    /**
     * Test Case:4.3.1:Verify that If user sorts table by a particular column, it should persist that setting and it
     * should sort that table by the same column, if user revisit that table.
     * Test Case:4.3.2:Verify that there should be an icon beside the sort column which indicates the sorted column.
     * Test Case:4.3.4:Verify that the direction of the icon for sorted column upwards if the column is sorted in
     * ascending order.
     * Test Case:4.3.5:Verify that the direction of the icon for sorted column downwards if the column is sorted in
     * descending order.
     */
    @Test public void testTableSortPersistOnRevisit_4_3_1_2_4_5() throws PopUpException, InterruptedException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        List<String> successesBeforeSort = retrieveEventAnalysisSuccessesList(false);
       // the first time we invoke sorting
        SortType sortType = SortType.DESCENDING;
        networkEventAnalysis.sortTable(sortType, SUCCESSES);

        List<String> successesAfterSort = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);

        checkIconForSortedColumn("Before a sort", SUCCESSES, sortType);

        Collections.sort(successesBeforeSort, Collections.reverseOrder(new StringAsNumberComparator()));
        
        assertEquals("Sorting is incorrect", successesBeforeSort.toString(), successesAfterSort.toString());
        
        eventsLogin.relogin();

         workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        checkIconForSortedColumn("On reopen after relogin [EUI-405 is to fix it]", SUCCESSES, sortType);

        // the second time we do NOT do sort cause the sorting should be kept from the first time:
        List<String> successesAfterPrevSort;
        boolean isSortAfterReloginChecked = false;
        try {
            successesAfterPrevSort = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);
            waitForPageLoadingToComplete();

            ArrayList<String> tempList = new ArrayList<String>(successesAfterPrevSort);

            Collections.sort(tempList, Collections.reverseOrder(new StringAsNumberComparator()));

            assertEquals("Sorting after relogin is incorrect (must be DESCENDING sorting)",
                    tempList.toString(), successesAfterPrevSort.toString());

            isSortAfterReloginChecked = true;
        } catch (NoDataException ignore) { // if not data for 30 minutes - ignore it
        }

        successesAfterPrevSort = retrieveEventAnalysisSuccessesList(false);

        checkIconForSortedColumn("On refresh [EUI-405 is to fix it]", SUCCESSES, sortType);

        ArrayList<String> tempList = new ArrayList<String>(successesAfterPrevSort);

        Collections.sort(tempList, Collections.reverseOrder(new StringAsNumberComparator()));

        String errMsg = isSortAfterReloginChecked ?
                "Sorting after time refresh (30m -> 1week) is incorrect (must be DESCENDING sorting)"
                : "Sorting after relogin and time refresh (30m -> 1week) is incorrect (must be DESCENDING sorting";

        // Does not work until EUI-405 fix as well
        assertEquals(errMsg, tempList.toString(), successesAfterPrevSort.toString());

        // Verify that the direction of the icon for sorted column upwards/downwards if the column is sorted in
        // ascending/descending order
        sortType = SortType.ASCENDING;
        networkEventAnalysis.sortTable(sortType, FAILURES);
        checkIconForSortedColumn("On sorting as " + sortType, FAILURES, sortType);
    }
    
    /**
     * Test Case 4.3.3:Verify that If user reorganize particular table it should persist that setting and it should
     * display the table in same manner, if user revisit that table.
     */
    @Test public void testUserTableReorganisingPersistOnRevisit_4_3_3() throws PopUpException, InterruptedException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        String headersStrBeforeDragAndDrop = networkEventAnalysis.getTableHeaders().toString();
        networkEventAnalysis.dragAndDropHeader(SGSN__MME, TOTAL_EVENTS);

        String headersListStr = networkEventAnalysis.getTableHeaders().toString();
        assertFalse("Drag and drop (\"" + SGSN__MME + "\" -> \"" + TOTAL_EVENTS +
                "\") did not change the headers list! " +
                "Before: " + headersStrBeforeDragAndDrop + ", " + " after: " + headersListStr,
                headersStrBeforeDragAndDrop.equals(headersListStr));

        workspaceRC.closeWindow(CURRENT_WINDOW);
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        List<String> tableHeadersListAfterReopening = networkEventAnalysis.getTableHeaders();

        assertEquals("Headers list must persist after window reopening and on revisit that table",
                headersListStr, tableHeadersListAfterReopening.toString());

        headersStrBeforeDragAndDrop = headersListStr;
        networkEventAnalysis.dragAndDropHeader(FAILURES, SUCCESS_RATIO);
        headersListStr = networkEventAnalysis.getTableHeaders().toString();
        assertFalse("Drag and drop (\"" + FAILURES + "\" -> \"" + SUCCESS_RATIO +
                "\") did not change the headers list! " +
                "Before: " + headersStrBeforeDragAndDrop + ", " + " after: " + headersListStr,
                headersStrBeforeDragAndDrop.equals(headersListStr));

        eventsLogin.relogin();
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        List<String> tableHeadersListAfterRelogin = networkEventAnalysis.getTableHeaders();
        assertEquals("Headers list must persist after relogin on revisit that table",
                headersListStr, tableHeadersListAfterRelogin.toString());

        workspaceRC.closeWindow(CURRENT_WINDOW);
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        waitForPageLoadingToComplete();

        List<String> tableHeadersListAfterDifferentSearch = networkEventAnalysis.getTableHeaders();
        assertEquals("Headers list must persist after search by another value ('" +
                SGSN_SEARCH_VALUE2 + "') on revisit that table",
                headersListStr, tableHeadersListAfterDifferentSearch.toString());
    }

    // *********************************************** PRIVATE METHODS ********************************************** //

    private void checkIconForSortedColumn(final String contextMsg, final String columnName, SortType sortType) {
        // Verify that there should be an icon beside the sort column which indicates the sorted column.
        String msgPrefix = contextMsg != null ? contextMsg : "";

        String locator = "//*[@id='NETWORK_EVENT_ANALYSIS']//*[contains(@aria-sort,'ascending') or contains(@aria-sort,'descending')]//*[text()='" +
                columnName + "']";
        boolean isColumnSuccessesSorted = selenium.isElementPresent(locator);
        assertTrue(msgPrefix + ": Column \"" + columnName + "\" not sorted; locator=" + locator,
                isColumnSuccessesSorted);

        locator = "//*[@id='NETWORK_EVENT_ANALYSIS']//*[contains(@aria-sort,'ascending') or contains(@aria-sort,'descending')]//img[contains(@class,'sort')]";
        boolean isImageInSortedColumn = selenium.isElementPresent(locator);
        assertTrue(msgPrefix + ": No icon in the sorted column; locator=" + locator,
                isImageInSortedColumn);

        // Verify that the direction of the icon for sorted column upwards/downwards if the column is sorted in
        // ascending/descending order:
        // Note: for upward and downward icons the same picture is used, the only difference is class: "sort-desc" and
        // "sort-asc" - it changes how the picture is shown - upward or downward
        int successesColumnIndex = networkEventAnalysis.getTableHeaderIndex(columnName) + 1;
        locator = "//*[@id='NETWORK_EVENT_ANALYSIS']//table//td[" + successesColumnIndex +
                "]//*[contains(@role,'columnheader')][contains(@class,'sort-" + sortType.getSortClassPart() +
                "')]//img[contains(@class,'sort')]";
        boolean isImageInProperSortedColumn = selenium.isElementPresent(locator);
        assertTrue(msgPrefix + ": No icon beside the sorted column \"" + columnName + "\"; locator=" + locator,
                isImageInProperSortedColumn);

    }

    private List<String> retrieveEventAnalysisSuccessesList(final boolean isLongPeriod) throws PopUpException, NoDataException {
        if (isLongPeriod) {
            setLongTimeRange();
        }

        List<String> allTableDataAtColumn = null;
        try {
            allTableDataAtColumn = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);
        } catch (NoDataException e) {
            boolean isLoaded = false;
            if (!isLongPeriod) {
                try {
                    setLongTimeRange();
                    allTableDataAtColumn = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);
                    isLoaded = true;
                } catch (NoDataException ignore) {
                }
            }

            if (!isLoaded) {
                networkEventAnalysis.setTimeAndDateRange(getStartDate(),
                        TimeCandidates.AM_00_00, getEndDate(), TimeCandidates.AM_00_00);
                waitForPageLoadingToComplete();
                allTableDataAtColumn = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);
            }
        }

        if ((allTableDataAtColumn == null || allTableDataAtColumn.isEmpty()) && !isLongPeriod) {
            setLongTimeRange();
            allTableDataAtColumn = networkEventAnalysis.getAllTableDataAtColumn(SUCCESSES);
        }

        return allTableDataAtColumn;
    }

    private void setLongTimeRange() throws PopUpException {
        networkEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();
    }

    private Date getStartDate() {
        String stDate = "2007,04,09";
        String startDateArray[] = stDate.split(",");
        return networkEventAnalysis.getDate(Integer.parseInt(startDateArray[0]),
                Integer.parseInt(startDateArray[1]), Integer.parseInt(startDateArray[2]));
    }

    private Date getEndDate() {
        String edDate = new SimpleDateFormat("yyyy,MM,dd").format(new Date()); // "2012,02,29"
        String EndDateArray[] = edDate.split(",");
        return networkEventAnalysis.getDate(Integer.parseInt(EndDateArray[0]),
                Integer.parseInt(EndDateArray[1]), Integer.parseInt(EndDateArray[2]));
    }

    private static class StringAsNumberComparator implements Comparator<String> {
        //@Override 
        public int compare(String n1, String n2) {
            try {
                return Long.valueOf(n1).compareTo(Long.valueOf(n2));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
    }
}