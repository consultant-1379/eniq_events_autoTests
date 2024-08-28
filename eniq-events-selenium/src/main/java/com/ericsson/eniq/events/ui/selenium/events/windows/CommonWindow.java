package com.ericsson.eniq.events.ui.selenium.events.windows;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.charts.ChartController;
import com.ericsson.eniq.events.ui.selenium.events.elements.*;
import com.ericsson.eniq.events.ui.selenium.events.elements.NavigationController.SwitchViewDropDown;
import com.thoughtworks.selenium.SeleniumException;
import junit.framework.AssertionFailedError;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonWindow extends Window {

    private static final String VIEW_BUTTON_XPATH = "//table[@id='btnView']//button[text()='View']";
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());
    protected final BreadcrumbControls breadcrumbController;
    private final TableController tableController;
    private final TimeDialogControls timeDialogController;
    private final PagingControls pagingController;
    private final NavigationControls navigationController;
    private final ISubscriberDetailsDialog subscriberDetailsDialogController;
    private final ChartController chartController;

    public CommonWindow(final String divId) {
        super(divId);
        tableController = new TableController(id);
        timeDialogController = new TimeDialogController(id);
        pagingController = new PagingController(id);
        breadcrumbController = new BreadcrumbController(id);
        navigationController = new NavigationController(id);
        subscriberDetailsDialogController = new SubscriberDetailsDialogController(id);
        chartController = new ChartController(id);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.ericsson.eniq.events.ui.selenium.events.windows.Window#init()
     */
    @Override protected void init() {
        beanFactory.autowireBeanProperties(tableController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(timeDialogController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(pagingController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(breadcrumbController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(navigationController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(subscriberDetailsDialogController, AutowireCapableBeanFactory.AUTOWIRE_NO,
                false);
        beanFactory.autowireBeanProperties(chartController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
    }

    public enum GroupTerminalAnalysisViewType {
        MOST_POPULAR("menuGTA_MostPop"), MOST_POPULAR_EVENT_SUMMARY("menuGTA_Pop_Summary"), MOST_ATTACH_FAILURES(
                "menuGTA_AttachedFailures"), MOST_PDP_SESSION_SETUP_FAILURES("menuGTA_PDPSetupFailures"), MOST_MOBILITY_ISSUES(
                "menuGTA_MostMobilityIssues"), MOST_HSDSCH_HANDOVER_FAILUES("menuGTA_HFA_HSDSCH"), MOST_SOHO_HANDOVER_FAILUES(
                "menuGTA_HFA_SOHO"), MOST_IFHO_HANDOVER_FAILUES("menuGTA_HFA_IFHO"), MOST_IRAT_HANDOVER_FAILUES(
                "menuGTA_HFA_IRAT");

        private final String id;

        GroupTerminalAnalysisViewType(final String name) {
            id = name;
        }

        private String getXPath() {
            return "//*[@id='" + id + "']";
        }
    }

    public void setGroupTerminalAnalysisView(final GroupTerminalAnalysisViewType viewType) throws PopUpException {
        selenium.setSpeed("100");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + SelectedButtonType.VIEW_BUTTON);
        breadcrumbController.clickButton(SelectedButtonType.VIEW_BUTTON);
        selenium.click(viewType.getXPath());
        selenium.waitForPageLoadingToComplete();
        selenium.setSpeed("0");

    }

    // Network Tab - Window - View Checkbox drop-down list
    public enum CheckboxGroup {
        CHECKBOX1("x-menu-el-1"), CHECKBOX2("x-menu-el-2"), CHECKBOX3("x-menu-el-3"), CHECKBOX4("x-menu-el-4"), CHECKBOX5(
                "x-menu-el-5"), CHECKBOX6("x-menu-el-6"), CHECKBOX7("x-menu-el-7"), CHECKBOX8("x-menu-el-8"), CHECKBOX9(
                "x-menu-el-9"), CHECKBOX10("x-menu-el-10"), CHECKBOX11("x-menu-el-11");

        private final String id;

        CheckboxGroup(final String name) {
            id = name;
        }

        private String getXPath() {
            return "//*[@id='" + id + "']//a";
        }
    }

    public void setCheckboxGroup(final CheckboxGroup networkViewCheckBoxes) {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + SelectedButtonType.VIEW_BUTTON);
        breadcrumbController.clickButton(SelectedButtonType.VIEW_BUTTON);
        selenium.click(networkViewCheckBoxes.getXPath());
    }

    public void clickWindowHeader() {
        breadcrumbController.clickWindowHeader();
    }

    public void clickTableCell(final int row, final String columnHeader) throws NoDataException, PopUpException {
        tableController.clickTableCell(row, columnHeader);
    }

    public void openTableHeaderMenu(final int column) {
        tableController.openTableHeaderMenu(column);
    }

    public void openGridView() throws PopUpException {
        tableController.openGridView();
    }

    public void openChartView(final List<String> defaultViewContentOnEventVolumeVoice) throws PopUpException {
        tableController.openChartView(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * By eikrwaq
     * Select item Using names displayed in UI, if you want to Select all just pass 'Select all' in list
     * @param defaultViewContentOnEventVolumeVoice
     * @throws PopUpException
     */
    public void openChartViewUsingDisplayedNames(final List<String> defaultViewContentOnEventVolumeVoice) throws PopUpException {
        tableController.openChartViewUsingDisplayedNames(defaultViewContentOnEventVolumeVoice);
    }

    /**
     * Written by elukpot.
     *
     * This method handles clicking on the "Chart View" RadioButton, clicking the single "Select all" CheckBox and
     * clicking the "Launch" button. The after calling this method there will be a chart displayed on the screen.
     *
     * Such Convenient, Much Useful, Very Innovative, Many Better than all the other methods, Wow.
     */
    public void openChartViewViaClickingSingleSelectAllCheckBox() {
        tableController.openChartViewViaClickingSingleSelectAllCheckBox();
    }

    /**
     * added by efreass 17 august 2012
     * to cater only for chartview in Ranking test group cfa hfa
     */
    public void openRankingTestGroupHfaCfaChartView() {
        tableController.openChartView();
    }

    /**
     * added by xchopri 17-05-2013
     * to cater only for SGEH Subscriber Analysis Test Cases for 2g3g
     */
    public void checkInOptionalMenu(final String option, final String headerCheckBoxGroup, String optionalHeadersToBeChecked) {
        tableController.checkInOptionalHeaderCheckBoxes(option, headerCheckBoxGroup, optionalHeadersToBeChecked);
    }

    public void clickTableCell(final int row, final int column, final String spanId) throws NoDataException, PopUpException {
        tableController.clickTableCell(row, column, spanId);
    }

    public void filterColumnEquals(final String column, final String valToType) throws PopUpException {
        tableController.filterColumnEquals(column, valToType);
    }

    public void filterColumnGreaterThan(final int greaterValue, final String columnHeader) throws PopUpException {
        tableController.filterColumnGreaterThan(greaterValue, columnHeader);
    }

    public void filterColumnLessThan(final int column) {
        tableController.filterColumnLessThan(column);
    }

    public List<Map<String, String>> getAllTableData() throws NoDataException {
        return tableController.getAllTableData();
    }

    public List<Map<String, String>> getAllPagesData() throws NoDataException {
        return tableController.getAllPagesData();
    }

    public Map<String, String> getAllDataAtTableRow(final int row) throws NoDataException {
        return tableController.getAllDataAtTableRow(row);
    }

    public int findFirstTableRowWhereMatchingAnyValue(final String columnToCheck, final String... values) throws NoDataException {
        return tableController.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
    }

    public boolean isCSSClassPresentOnTableRow(final int row, final String cssClassName) throws NoDataException {
        return tableController.isCSSClassPresentOnTableRow(row, cssClassName);
    }

    /**
     * @param column table column name to fetch data from
     * @return not <tt>null</tt> and not empty result data list
     * @throws NoDataException no data (e.g. when empty) exception
     */
    public List<String> getAllTableDataAtColumn(final String column) throws NoDataException {
        return tableController.getAllTableDataAtColumn(column);
    }
    
    public List<String> getAllPagesDataAtColumn(final String column) throws NoDataException {
        return tableController.getAllPagesDataAtColumn(column);
    }

    public String getTableData(final int row, final int column) throws NoDataException {
        return tableController.getTableData(row, column);
    }

    public int getTableHeaderCount() {
        return tableController.getTableHeaderCount();
    }

    public List<String> getTableHeaders() {
        return tableController.getTableHeaders();
    }

    public List<Map<String, String>> getALLRowsWithMatchingValueForGivenColumn(final String column, final String value)
            throws NoDataException {
        return tableController.getALLRowsWithMatchingValueForGivenColumn(column, value);
    }

    /**
     * @param headerName header name; can be <tt>null</tt>
     * @return the index of the first occurrence of the specified header name in
     *         this list starting from 0, or -1 if this table header does not contain
     *         the header name or the header name is null
     */
    public int getTableHeaderIndex(final String headerName) {
        if (headerName == null) {
            return -1;
        }
        List<String> tableHeaders = getTableHeaders();
        return tableHeaders.indexOf(headerName);
    }

    public int getTableRowCount() throws NoDataException {
        return tableController.getTableRowCount();
    }

    /**
     * Drags and drops header <tt>headerToMove</tt> on place of <tt>otherHeader</tt>.
     *
     * @param headerToMove name of header that should be moved
     * @param otherHeader  name of header where we should put our header <tt>headerToMove</tt>
     * @return old index of moved header (starting from 0)
     * @throws AssertionFailedError if any assertion fails, immediately throws this exception
     */
    public int dragAndDropHeader(String headerToMove, final String otherHeader) throws AssertionFailedError {
        if (headerToMove == null || otherHeader == null || headerToMove.trim().isEmpty()
                || otherHeader.trim().isEmpty()) {
            throw new AssertionFailedError("Header '" + headerToMove + "' (headerToMove) and other header '"
                    + otherHeader + "' (otherHeader) cannot be null or empty");
        }

        int oldHeaderToMoveIndex = getTableHeaderIndex(headerToMove);
        int oldOtherHeaderIndex = getTableHeaderIndex(otherHeader);

        if (oldHeaderToMoveIndex == -1) {
            throw new AssertionFailedError("Not found header to move: '" + headerToMove + "' (headerToMove)");
        } else if (oldOtherHeaderIndex == -1) {
            throw new AssertionFailedError("Not found other header '" + otherHeader
                    + "' (otherHeader) where we should move header '" + headerToMove + "' (headerToMove)");
        }

        String winXPath = "//*[@id='NETWORK_EVENT_ANALYSIS']";
        String headerToMoveXPath = winXPath + "//div[span='" + headerToMove + "']";
        String otherHeaderXPath = winXPath + "//div[span='" + otherHeader + "']";
        String newHeaderToMoveXpath = winXPath + "//td[" + (oldOtherHeaderIndex + 1) + "]/div[span='" + headerToMove
                + "']";

        int attempts = 0;
        do {
            selenium.mouseDownAt(headerToMoveXPath, "5, 5");
            selenium.mouseMoveAt(headerToMoveXPath, "9, 5");
            selenium.mouseOut(headerToMoveXPath);
            selenium.mouseOver(otherHeaderXPath);

            Number otherHeaderToMoveWidth = selenium.getElementWidth(otherHeaderXPath);

            // it is enough to move mouse cursor a little bit left or right of other header
            int offset = otherHeaderToMoveWidth.intValue() / 2 + 5
                    * (oldHeaderToMoveIndex < oldOtherHeaderIndex ? 1 : -1);

            String goalMouseCoords = offset + ",0";
            selenium.mouseMoveAt(otherHeaderXPath, goalMouseCoords);
            selenium.mouseUpAt(otherHeaderXPath, goalMouseCoords);
            try {
                selenium.waitForElementToBePresent(newHeaderToMoveXpath, "500");
            } catch (SeleniumException ignore) {
            }
        } while (++attempts < 7 && !selenium.isElementPresent(newHeaderToMoveXpath));

        if (!selenium.isElementPresent(newHeaderToMoveXpath)) {
            throw new AssertionFailedError("Not able to drag and drop header '" + headerToMove
                    + "' (headerToMove) to other header '" + otherHeader + "' (otherHeader) place: header '"
                    + headerToMove + "' (headerToMove) is not on the correct place with column index "
                    + (oldOtherHeaderIndex + 1));
        }

        // 2 checks that swap happened:
        int headerToMoveIndex = getTableHeaderIndex(headerToMove);
        if (oldOtherHeaderIndex != headerToMoveIndex) {
            throw new AssertionFailedError("Swap failed: new index for '" + headerToMove + "' (headerToMove) is "
                    + headerToMoveIndex + " while old index for '" + otherHeader + "' (otherHeader) is "
                    + oldOtherHeaderIndex + ": they should be equal after drag-and-drop.");
        }

        int newOtherHeaderIndex = getTableHeaderIndex(otherHeader);
        if (Math.abs(oldOtherHeaderIndex - newOtherHeaderIndex) != 1) {
            throw new AssertionFailedError("Swap failed: new index for '" + otherHeader
                    + "' (otherHeader) was not changed by 1: old index for '" + otherHeader + "' (otherHeader) is "
                    + oldOtherHeaderIndex + " while new is " + newOtherHeaderIndex);
        }

        return oldOtherHeaderIndex;
    }

    // TODO replace this with sortTable(final SortType order, final String name)
    public void sortTable(final int column) {
        tableController.sortTable(column);
    }

    public void sortTable(final SortType order, final String name) {
        tableController.sortTable(order, name);
    }

    /**
     * This method is used to check the selected menu options in the column selection menu popup.
     * It takes in a list of Menu Option titles that are expected to be selected in the menu.
     * @param selectedMenuOptions
     * @return List - returns a list of menu options that should have been selected but were not.
     */
    public List<String> isSelectedMenuOptions(String... selectedMenuOptions){
        return tableController.isSelectedMenuOptions(selectedMenuOptions);
    }

    /**
     * This method is used to check the unselected menu options in the column selection menu popup.
     * It takes in a list of Menu Option titles that are expected to be unselected in the menu.
     * @param unselectedMenuOptions
     * @return List - returns a list of menu options that should have been unselected but were not.
     */
    public List<String> isUnselectedMenuOptions(String... unselectedMenuOptions){
        return tableController.isUnselectedMenuOptions(unselectedMenuOptions);
    }

    public void checkInOptionalHeaderCheckBoxes(final List<String> optionalHeadersToBeChecked, final String... headerCheckBoxGroup) {
        tableController.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked, headerCheckBoxGroup);
    }

    public void uncheckOptionalHeaderCheckBoxes(final List<String> optionalHeadersToBeUnchecked, final String... headerCheckBoxGroup) throws InterruptedException {

        tableController.uncheckOptionalHeaderCheckBoxes(optionalHeadersToBeUnchecked, headerCheckBoxGroup);

    }

    @Deprecated public void checkInKPIViewCheckBoxes(final List<String> optionalHeadersToBeChecked, final String... headerCheckBoxGroup) {
        // replaced by configureKPIwindow
        tableController.checkInKPIViewCheckBoxes(optionalHeadersToBeChecked, headerCheckBoxGroup);
    }

    public void clickConfigureBarAndConfigureItems(final List<String> optionItems) throws PopUpException {
        tableController.clickConfigureBarAndConfigureItems(optionItems);
    }

    public void clickConfigureBarAndConfigureItemsForMSS(final List<String> optionItems) throws PopUpException {
        tableController.clickConfigureBarAndConfigureItemsForMSS(optionItems);
    }

    public void setTimeRange(final TimeRange timeRange) throws PopUpException {
        timeDialogController.setTimeRange(timeRange);
    }

    public void clickFirstPage() {
        pagingController.clickFirstPage();
    }

    public void clickLastPage() {
        pagingController.clickLastPage();
    }

    public void clickNextPage() {
        pagingController.clickNextPage();
    }

    public void clickPreviousPage() {
        pagingController.clickPreviousPage();
    }

    public int getCurrentPageNumber() {
        return pagingController.getCurrentPageNumber();
    }

    public String getItemDisplayCount() {
        return pagingController.getItemDisplayCount();
    }

    public int getPageCount() {
        return pagingController.getPageCount();
    }

    public void refresh() {
        pagingController.refresh();
    }

    public List<String> getTableHeaderOptions() {
        return tableController.getTableHeaderOptions();
    }

    public void setTimeAndDateRange(final Date startDate, final TimeCandidates startTimeCandidate, final Date endDate, final TimeCandidates endTimeCnadidate) throws PopUpException {
        timeDialogController.setTimeAndDateRange(startDate, startTimeCandidate, endDate, endTimeCnadidate);
    }

    public String getTimeRange() {
        return timeDialogController.getTimeRange();
    }

    public void clickCauseCodes(final String spanId) throws NoDataException {
        tableController.clickCauseCodes(spanId);
    }

    public void clickRankingDrills(final String spanId, final String drillOn) throws NoDataException, PopUpException {
        tableController.clickRankingDrills(spanId, drillOn);
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
    public void clickRankingDrillsNonZeroValue(final String rowDivId, final String drillColumnSpanId) throws PopUpException, NoDataException {
        tableController.clickRankingDrillsNonZeroValue(rowDivId, drillColumnSpanId);
    }

    public void clickButton(final SelectedButtonType button) {
        breadcrumbController.clickButton(button);
    }

    public boolean isButtonEnabled(final SelectedButtonType button) {
        return breadcrumbController.isButtonEnabled(button);
    }

    public Date getDate(final int year, final int month, final int day) {
        return timeDialogController.getDate(year, month, day);
    }

    public String getTimeLabelText() {
        return timeDialogController.getTimeLabelText();
    }

    public void clickBackwardNavigation() {
        navigationController.clickBackwardNavigation();
    }

    public void clickForwardNavigation() {
        navigationController.clickForwardNavigation();
    }

    public void switchView(final SwitchViewDropDown switchViewDropDown) {
        navigationController.switchView(switchViewDropDown);
    }

    public void toggleGraphToGrid() {
        navigationController.toggleGraphToGrid();
    }

    public void toggleGridToGraph() throws InterruptedException {
        navigationController.toggleGridToGraph();
    }

    public void setTimeRangeBasedOnSeleniumPropertiesFile() throws PopUpException {
        timeDialogController.setTimeRangeBasedOnSeleniumPropertiesFile();
    }

    public void chartRefresh() throws InterruptedException {
        chartController.chartRefresh();
    }

    public void drillOnChartObject(final int barChartObjectNumber) throws PopUpException {
        chartController.drillOnBarChartObject(barChartObjectNumber);
    }

    public void drillOnPieChartObjectByCountry(final int pieChartObjectNumber) throws PopUpException {
        chartController.drillOnPieChartObjectByCountry(pieChartObjectNumber);
    }

    public void drillOnPieChartObjectByOperator(final int pieChartObjectNumber) throws PopUpException {
        chartController.drillOnPieChartObjectByOperator(pieChartObjectNumber);
    }

    /**
     * Written by elukpot
     *
     * Takes the first item in the chart's legend and uses that that to drill on the pie chart.
     */
    public void drillOnPieChartUsingFirstLegendItem() {
        chartController.drillOnPieChartUsingFirstLegendItem();
    }

    /**
     * Written by elukpot
     *
     * Takes the first item in the chart's legend and uses that that to drill on the pie chart, of the specified seleniumTagBaseWindow.
     *
     * @param seleniumTagBaseWindowNumber The launch order of the window that you want to drill on.
     */
    public void drillOnPieChartUsingFirstLegendItemInSeleniumTagBaseWindow(int seleniumTagBaseWindowNumber) {

        chartController.drillOnPieChartUsingFirstLegendItemInSeleniumTagBaseWindow(seleniumTagBaseWindowNumber);
    }

    public void clickTableCell(int row, String columnHeader, String spanId) throws NoDataException, PopUpException {
        tableController.clickTableCell(row, columnHeader, spanId);
    }

    public String selectTableCell(int row, String columnHeader) throws NoDataException, PopUpException {
        return tableController.selectTableCell(row, columnHeader);
    }

    public int getRowNumberWithMatchingValueForGivenColumn(final String column, final String value) throws NoDataException {
        return tableController.getRowNumberWithMatchingValueForGivenColumn(column, value);
    }

    public void drilldownOnHeaderPortion(final String piePortion) throws PopUpException {
        chartController.drilldownOnHeaderPortion(piePortion);
    }

    public void drilldownOnBarChartPortion(final String barPortionName) throws PopUpException {
        chartController.drilldownOnBarChartPortion(barPortionName);
    }

    public void toggleToUsingView(final String viewTypeName) throws PopUpException {
        selenium.click(VIEW_BUTTON_XPATH);
        selenium.click("//div[contains(@class,'x-menu-list')]//a[text()='" + viewTypeName + "']");
        selenium.waitForPageLoadingToComplete();
    }

    /**
     * Written by elukpot.
     *
     * This method checks for the presence of the colorBlock in the Event Result column.
     * This is a very basic check, so ensure that all other windows containing the Event Result column are closed,
     * before calling this method.
     *
     * @return true - If the colorBlock is existing.
     *         false - If the colorBlock is not existing.
     */
    public boolean isEventResultColumnColorBlockPresent() {

        return selenium.isElementPresent("//*[contains(@class, 'colorBlock')]");
    }

    /**
     * Written by elukpot.
     *
     * The method returns the text, representing the time range of data in the window, in the grid's footer.
     *
     * @return The time range in the grid's footer.
     */
    public String getFooterTimeRange() {

        return selenium.getText("//label[contains(@class, 'lastRefreshLabel')][text()]");
    }

    /**
     * Written by elukpot.
     *
     * Very similar in terms of functionality to {@code getFooterTimeRange}. This method returns the text, representing
     * the time range of data in the window, in the grid's footer. When many windows are launched and have the
     * {@code id="selenium_tag_baseWindow"}, this function looks at the Footer of the desired window.
     *
     * @param launchedWindowOrder - The position in the order of the multiple launched windows that the desired window
     *                              was launched.
     * @return The time range in the grid's footer for the desired window.
     */
    public String getFooterTimeRangeForSeleniumTagBaseWindowNumber(int launchedWindowOrder) {

        return selenium.getText("//div[@id='selenium_tag_baseWindow'][" + launchedWindowOrder + "]//label[contains(@class,'lastRefreshLabel')][text()]");
    }
}
