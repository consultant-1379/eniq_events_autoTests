package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GridImprovementsTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspaceRC;

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;

    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysis;

    static final String tabName = "NETWORK_TAB";
    static final String TABLE_HEADER_EA = "//div[contains(@id,'" + tabName + "')]//table//div[span='Event Type']/a ";
    static final String NETWORK_SUBMIT_BUTTON = "//div[@id='" + "NETWORK_TAB" + "']//div[@id='selenium_tag_launchButton']/img";
    static final String COLUMNS = "//div[contains(@class, 'x-ignore x-menu x-component')]//div/a[contains(@class,' x-menu-item x-menu-item-arrow x-component')]";
    static final String CURRENT_WINDOW = "//div[@id='NETWORK_EVENT_ANALYSIS']//div[contains(@class,'x-grid3-scroller')]";

    int index = 0;

    /* Test case 4.7.1: Verify that the user should be able to drag a column and drop it to desire location in the grid using column header. */
    @Test
    public void dragColumnAndDropItToDesireLocationInGridUsingColumnHeader_4_7_1() throws InterruptedException, PopUpException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "Core PS", "Network Event Analysis");
        dragAndDropObject("APN");
    }

    /*
     * Test case 4.7.3: Verify that if user moves the column to new position in the grid, it should persist that for all same tables even in different
     * tab and should show the grid in same manner if user revisit
     */
    @Test
    public void shouldPersistTheColumnNewPositionEvenInDifferentTabIfUserRevisit_4_7_3() throws PopUpException, InterruptedException, NoDataException {

        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, "Core PS", "Network Event Analysis");
        dragAndDropObject("SGSN-MME");

    }

    /* Test case 4.7.6: Verify that if user shouldnt be able to move the column underneath other column. */

    @Test
    public void userShouldNotAbleToMoveTheColumnUnderneathOtherColumn_4_7_6() throws InterruptedException, PopUpException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", "Core PS", "Network Event Analysis");
        waitForPageLoadingToComplete();
        List<String> tableHeadersBeforeColReordering = networkEventAnalysis.getTableHeaders();
        int indexBeforeColReordering = tableHeadersBeforeColReordering.indexOf("APN");
        selenium.mouseDownAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']", "10, 5");
        selenium.mouseMoveAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']", "20, 5");
        selenium.mouseOut("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']");
        selenium.mouseOver("//div[2]/table/tbody/tr/td[3]/div");
        selenium.mouseMoveAt("//div[2]/table/tbody/tr/td[3]/div", "");
        selenium.mouseUpAt("//div[2]/table/tbody/tr/td[3]/div", "");
        Thread.sleep(5000);
        List<String> tableHeadersAfterColReordering = networkEventAnalysis.getTableHeaders();
        int indexAftercolReordering = tableHeadersAfterColReordering.indexOf("APN");
        if (indexBeforeColReordering == indexAftercolReordering) {
            assertTrue(true);
        } else {
            assertTrue("User able to move the column underneath the column", false);
        }
    }

    /* Test case 4.7.7: Verify that if user apply filter to a grid, it shouldnt effect the position of the columns. */
    @Test
    public void userApplyFilterToGridItShouldNotEffectThePositionOfTheColumns_4_7_7() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", "Core PS", "Network Event Analysis");
        waitForPageLoadingToComplete();
        dragAndDropObject("APN");
        Thread.sleep(5000);
        List<String> tableHeadersAfterColReordering = networkEventAnalysis.getTableHeaders();
        int indexAfterColReordering = tableHeadersAfterColReordering.indexOf("APN");

        int rowCount = networkEventAnalysis.getTableRowCount();

        if (rowCount != 0) {

            networkEventAnalysis.filterColumnGreaterThan(0, GuiStringConstants.FAILURES);
            Thread.sleep(3000);
            List<String> tableHeadersAfterFiltering = networkEventAnalysis.getTableHeaders();
            int indexAfterFiltering = tableHeadersAfterFiltering.indexOf("APN");

            if (indexAfterColReordering == indexAfterFiltering) {
                assertTrue(true);
            } else {
                assertTrue("User apply filter to a grid, it effect the position of the columns", false);
            }
        }
    }

    /* Test Case 4.7.9: Verify that if user jumps from page to page, it shouldnt effect the position of the columns. */
    @Test
    public void userJumpsFromPageToPageItShouldNotEffectThePositionOfTheColumns_4_7_9() throws InterruptedException, PopUpException, NoDataException {
        workspaceRC.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", "Core PS", "Network Event Analysis");
        waitForPageLoadingToComplete();

        dragAndDropObject("APN");
        Thread.sleep(5000);
        List<String> tableHeadersAfterColReordering = networkEventAnalysis.getTableHeaders();
        int indexAfterColReordering = tableHeadersAfterColReordering.indexOf("APN");
        int indexOfFailures = tableHeadersAfterColReordering.indexOf("Failures");
        try {
            networkEventAnalysis.clickTableCell(0, indexOfFailures, GuiStringConstants.GRID_CELL_LINK);
            waitForPageLoadingToComplete();
        } catch (NoDataException e) {
            setTimeRangeToOneWeek(networkEventAnalysis);
            waitForPageLoadingToComplete();
            networkEventAnalysis.clickTableCell(0, indexOfFailures, GuiStringConstants.GRID_CELL_LINK);
        }

        selenium.click("//table[@id='btnNav']/tbody/tr[2]/td[2]/em/button[text()='History']");
        Thread.sleep(5000);
        try {
            selenium.click("//div[@id='x-menu-el-NETWORK_EVENT_ANALYSIS_APN_0']/a[@id='NETWORK_EVENT_ANALYSIS_APN_0'][text()='APN Event Analysis']");
        } catch (com.thoughtworks.selenium.SeleniumException e) {
            selenium.click("//div[@id='x-menu-el-NETWORK_EVENT_ANALYSIS_APN_0']/a[@id='NETWORK_EVENT_ANALYSIS_APN_0'][text()='"
                    + DataIntegrityStringConstants.APN_SEARCH_VALUE + " - APN - Event Analysis']");
        }
        Thread.sleep(5000);
        List<String> tableHeaders = networkEventAnalysis.getTableHeaders();
        int indexAfterPageJump = tableHeaders.indexOf("APN");

        if (indexAfterColReordering == indexAfterPageJump) {
            assertTrue(true);
        } else {
            fail("When user jumps from page to page, It effects the position of the column");
        }
    }

    /* Test case4.7.10: Verify that user should be able to resize the column. */

    @Test
    public void userShouldBeAbleToResizeTheColumn_4_7_10() throws InterruptedException, PopUpException {
        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", "Core PS", "Network Event Analysis");
        pause(2000);
        waitForPageLoadingToComplete();
        pause(2000);
        if (selenium
                .isElementPresent("//*[@class=' x-window x-component ' or @class='x-window x-component']//div[@id='NETWORK_EVENT_ANALYSIS']/div[2]")) {
            assertTrue(true);
            pause(2000);
            selenium.dragAndDropToObject(
                    "//*[@class=' x-window x-component ' or @class='x-window x-component']//div[@id='NETWORK_EVENT_ANALYSIS']/div[2]",
                    "//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']");
            Thread.sleep(3000);
            selenium.dragAndDropToObject(
                    "//*[@class=' x-window x-component ' or @class='x-window x-component']//div[@id='NETWORK_EVENT_ANALYSIS']/div[2]",
                    "//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Failures']");
        } else {
            pause(2000);
            assertTrue(false);
        }
    }

    /* Test case4.7.11: Verify that user should be able to sort a grid by a particular column. */

    @Test
    public void userShouldBeAbleToSortGridByaParticularColumn_4_7_11() throws InterruptedException, PopUpException, NoDataException {

        workspaceRC.launchWindow(SeleniumConstants.APN, "blackberry.net", "Core PS", "Network Event Analysis");
        pause(2000);
        waitForPageLoadingToComplete();
        pause(2000);
        List<String> failuresBeforeSort = networkEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.FAILURES);
        networkEventAnalysis.sortTable(SortType.DESCENDING, GuiStringConstants.FAILURES);
        pause(2000);
        List<String> failuresAfterSort = networkEventAnalysis.getAllTableDataAtColumn(GuiStringConstants.FAILURES);
        pause(2000);
        if (failuresBeforeSort != failuresAfterSort) {
            pause(2000);
            assertTrue(true);
        } else {
            pause(2000);
            assertTrue("User should not be able to sort a grid by a particular column ", false);
        }
    }

    private void dragAndDropObject(String headerType) throws InterruptedException {

        int oldEventTypeIndex = -1;
        int oldTotalEventsIndex = -1;

        int i = 0;
        pause(3000);
        selenium.waitForElementToBePresent("//div[@id='NETWORK_EVENT_ANALYSIS']/div/div/div/div/div/div/table/tbody/tr/td/div[span]", "5000");
        while (i < selenium.getXpathCount("//div[@id='NETWORK_EVENT_ANALYSIS']/div/div/div/div/div/div/table/tbody/tr/td/div[span]").intValue()) {
            if (selenium.isElementPresent("//div[@id='NETWORK_EVENT_ANALYSIS']/div/div/div/div/div/div/table//tbody//tr/td[" + i
                    + "]/div[span='APN']")) {
                oldEventTypeIndex = i;
            } else if (selenium.isElementPresent("//div[@id='NETWORK_EVENT_ANALYSIS']/div/div/div/div/div/div//table//tbody//tr/td[" + i
                    + "]/div[span='SGSN-MME']")) {
                oldEventTypeIndex = i;
            } else if (selenium.isElementPresent("//div[@id='NETWORK_EVENT_ANALYSIS']/div/div/div/div/div/div//table//tbody//tr/td[" + i
                    + "]/div[span='Total Events']")) {
                oldTotalEventsIndex = i;
            }
            i++;
        }
        pause(10000);
        assertTrue(oldEventTypeIndex != -1);
        assertTrue(oldTotalEventsIndex != -1);

        if (headerType.equals("APN")) {

            selenium.mouseDownAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']", "10, 5");
            selenium.mouseMoveAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']", "20, 5");
            selenium.mouseOut("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='APN']");
            selenium.mouseOver("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Total Events']");
        } else {
            selenium.mouseDownAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='SGSN-MME']", "10, 5");
            selenium.mouseMoveAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='SGSN-MME']", "20, 5");
            selenium.mouseOut("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='SGSN-MME']");
            selenium.mouseOver("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Total Events']");
        }

        Number w = selenium.getElementWidth("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Total Events']");
        int halfWidth = w.intValue() / 2;

        boolean apnBeforeTotals = oldEventTypeIndex < oldTotalEventsIndex;
        int offset = halfWidth + 10 * (apnBeforeTotals ? 1 : -1);

        selenium.mouseMoveAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Total Events']", offset + ",0");
        Thread.sleep(5000);

        selenium.mouseUpAt("//div[@id='NETWORK_EVENT_ANALYSIS']//div[span='Total Events']", offset + ",0");
        Thread.sleep(5000);
        if (headerType.equals("APN")) {
            assertTrue("Not able to drag and drop", selenium.isElementPresent("//div[@id='NETWORK_EVENT_ANALYSIS']//table//tbody//tr/td["
                    + oldTotalEventsIndex + "]/div[span='APN']"));
        } else {
            assertTrue(
                    "Not able to drag and drop",
                    selenium.isElementPresent("//div[@id='NETWORK_EVENT_ANALYSIS']//table//tbody//tr/td[" + oldTotalEventsIndex
                            + "]/div[span='SGSN-MME']"));
        }

        index = oldTotalEventsIndex - 1;
    }

    private void openEventAnalysisWindow(final TerminalType type, final boolean submitButton, final String... values) throws PopUpException,
            InterruptedException {

        terminalTab.openEventAnalysisWindow(type, submitButton, values);

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            assertTrue("Can't open " + values[0] + " - Event Analysis", selenium.isTextPresent("Event Analysis"));
        } else {
            assertTrue("Can't open Terminal Event Analysis", selenium.isTextPresent("Event Analysis"));
        }
        waitForPageLoadingToComplete();
    }

    private Date getStartDate() {
        final String stDate = "2011,02,01";
        final String startDateArray[] = stDate.split(",");
        final Date sDate = terminalEventAnalysis.getDate(Integer.parseInt(startDateArray[0]), Integer.parseInt(startDateArray[1]),
                Integer.parseInt(startDateArray[2]));
        return sDate;
    }

    private Date getEndDate() {
        final String edDate = "2012,02,29";
        final String EndDateArray[] = edDate.split(",");
        final Date eDate = terminalEventAnalysis.getDate(Integer.parseInt(EndDateArray[0]), Integer.parseInt(EndDateArray[1]),
                Integer.parseInt(EndDateArray[2]));
        return eDate;
    }

    final String OPTIONAL_HEADERS_UNCHECKED = "//a[contains(@class, 'x-menu-checked')][contains(text(),'";
    final String NEW_HEADERS = "//a[contains(@class, 'x-menu-check')][contains(text(),'";

    private void optionalCloumnsAreUncheckedForAPN() {
        final List<String> optionalCloumnsForAPN = new ArrayList<String>(Arrays.asList("Bearer Count", "Downlink Data Vol (MB)",
                "Downlink Throughput (Mb/s)", "Downlink Avg Bearer Volume (KB)", "Uplink Data Vol (MB)", "Uplink Throughput (Mb/s)",
                "Uplink Avg Bearer Volume (KB)"));
        for (String string : optionalCloumnsForAPN) {
            selenium.click(OPTIONAL_HEADERS_UNCHECKED + string + "')]");
            assertTrue(selenium.isElementPresent(NEW_HEADERS + string + "')]"));

        }

    }

    private void launchEventAnalysisWindow() {
        selenium.click(NETWORK_SUBMIT_BUTTON);

    }

    public void tableHeader() throws InterruptedException {
        selenium.click(TABLE_HEADER_EA);
        Thread.sleep(2000);
        selenium.mouseOver(COLUMNS);
        Thread.sleep(2000);
    }

}
