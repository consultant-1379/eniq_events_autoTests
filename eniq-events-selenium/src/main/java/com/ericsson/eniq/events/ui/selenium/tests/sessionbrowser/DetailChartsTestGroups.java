package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.DetailTab.Chart;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType.*;

public class DetailChartsTestGroups extends SBWebDriverBaseUnitTest {

    private DetailTab detailTab;

    /**
     * US: DEFTFTS-940 : Session Browser - Print Preview for Charts 4.17.2 EE13.0_SESSB&KPI_17.2: Verify Print functionality in Summary View for
     * Application Layer Performance chart 4.10.8 EE13.0_SESSB&KPI_10.8: Verify that a user is able to print all 3 Detail Charts by selecting top
     * right ?Print the chart? icon 4.11.27 EE13.0_SESSB&KPI_11.27: Verify that a user is able to print all 3 Detail Charts by selecting top right
     * ?Print the chart? icon
     *
     * @throws ParseException
     */
    @Override
    public void setUp() {
        super.setUp();
        detailTab = new DetailTab(driver);
    }

    @Test
    public void printall3Charts4_11_27_Radio_and_Cell_Condition_Chart() throws InterruptedException, ParseException {
        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);
        // Test Radio and Cell Load Conditions Chart in Detail Tab
        testPrintPreviewOfChart(Chart.RADIO_CHART.getIndex());
    }

    @Test
    public void printall3Charts4_11_27_TCP_Performance_Chart() throws InterruptedException, ParseException {
        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);
        // Test TCP Performance Chart Chart in Detail Tab
        testPrintPreviewOfChart(Chart.TCP_CHART.getIndex());
    }

    @Test
    public void printall3Charts4_11_27_Application_Performace_Layer_Chart() throws InterruptedException, ParseException {
        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);
        // Test Application Performance Layer Chart Chart in Detail Tab
        testPrintPreviewOfChart(Chart.APPLICATION_CHART.getIndex());
    }

    /**
     * US DEFTFTS-1922 Verify It is possible to view all the legends for all three charts Automation of DEFTFTS-934 : Session Browser - Dropdown and
     * cog filter interaction (details tab)
     *
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void cogWheelInteraction1_Radio_And_Cell_Conditions_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);
        String chartName = "Radio and Cell Load Conditions";
        verifyLegendOfChart(Chart.RADIO_CHART, CommonDataType.SB_IMSI_DATA_BEARER_SESSION, chartName);
    }

    @Test
    public void cogWheelInteraction1_Application_Layer_Performance_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);
        String chartName = "Application Layer Performance";
        verifyLegendOfChart(Chart.APPLICATION_CHART, CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, chartName);
    }

    @Test
    public void cogWheelInteraction1_TCP_Performance_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);
        String chartName = "TCP Performance";
        verifyLegendOfChart(Chart.TCP_CHART, CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, chartName);
    }

    private void verifyLegendOfChart(Chart chart, CommonDataType imsiToSelect, String chartName) throws InterruptedException {
        SimpleDateFormat formatter = new SimpleDateFormat(CommonUtils.DD_MM_YY_HH_MM_SS);

        String imsi = getIMSIParameter(imsiToSelect);
        String dbDate = getEventTimeParameter(imsiToSelect);

        Calendar startDate = getclosest15minStartDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
        Calendar endDate = getclosest15minEndDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));

        Date start = startDate.getTime();
        Date end = endDate.getTime();
        if (detailTab.isChartPresent(chart, detailTab.prepareChartTitle(imsi, chartName, formatter.format(start), formatter.format(end)))) {
            String[] allLegends = getSelectedLegends(chart).toArray(new String[1]);
            verifyLegends(allLegends, chart);
        }
    }

    private void verifyLegends(String[] allLegends, Chart chart) {
        List<String> legendsFromChart = detailTab.getLegendsFromChart(chart);
        System.out.println("Legends Size " + allLegends.length + " " + legendsFromChart.size());
        assertEquals("Legends are not present.", allLegends.length, legendsFromChart.size());
        for (int i = 0; i < allLegends.length; i++) {
            assertTrue(allLegends[i] + " is not present in chart.", legendsFromChart.remove(allLegends[i]));
        }
    }

    private List<String> getSelectedLegends(Chart chart) throws InterruptedException {
        detailTab.openConfigurationWindow(chart);
        List<String> selectedLegends = detailTab.getChartConfigWindow().getSelectedLegends(chart);
        detailTab.getChartConfigWindow().cancel();
        return selectedLegends;
    }

    /**
     * US DEFTFTS-1922 Verify It is legend selected in dropdown is also selected in config window drop down and is enabled adn also selected in master
     * chart title Automation of DEFTFTS-934 : Session Browser - Dropdown and cog filter interaction (details tab)
     *
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void cogWheelInteraction2_Radio_And_Cell_Condition_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);
        String chartName = "Radio and Cell Load Conditions";
        verifyLegendsAndDropDownSelectionOfCharts(Chart.RADIO_CHART, CommonDataType.SB_IMSI_DATA_BEARER_SESSION, chartName);
    }

    @Test
    public void cogWheelInteraction2_TCP_Performance_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);
        String chartName = "TCP Performance";
        verifyLegendsAndDropDownSelectionOfCharts(Chart.TCP_CHART, CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, chartName);
    }

    @Test
    public void cogWheelInteraction2_Application_Layer_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();
        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);
        String chartName = "Application Layer Performance";
        verifyLegendsAndDropDownSelectionOfCharts(Chart.APPLICATION_CHART, CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, chartName);
    }

    private void verifyLegendsAndDropDownSelectionOfCharts(Chart chart, CommonDataType imsiToSelect, String chartName) throws InterruptedException {
        SimpleDateFormat formatter = new SimpleDateFormat(CommonUtils.DD_MM_YY_HH_MM_SS);
        String imsi = getIMSIParameter(imsiToSelect);
        String dbDate = getEventTimeParameter(imsiToSelect);

        Calendar startDate = getclosest15minStartDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
        Calendar endDate = getclosest15minEndDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));

        Date start = startDate.getTime();
        Date end = endDate.getTime();

        if (detailTab.isChartPresent(chart, detailTab.prepareChartTitle(imsi, chartName, formatter.format(start), formatter.format(end)))) {
            verifyDropdownSelection(chart);
            detailTab.selectRandomOptionInDropDown(chart);
            verifyDropdownSelection(chart);
            detailTab.selectRandomOptionInDropDown(chart);
            verifyDropdownSelection(chart);
        }
    }

    private void verifyDropdownSelection(Chart chart) throws InterruptedException {
        String selectedLegend = detailTab.getSelectedLegend(chart);
        assertTrue("Selected legend is not present in chart.", detailTab.isLegendPresent(chart, selectedLegend));
        List<String> masterChartLegend = detailTab.getMasterChartLegend(chart);
        for (String legendSubString : masterChartLegend) {
            assertTrue("Selected legend is not matched with master chart title.", selectedLegend.contains(legendSubString));

        }
        detailTab.openConfigurationWindow(chart);
        ChartConfigWindow chartConfigWindow = detailTab.getChartConfigWindow();
        assertEquals("Selected legend is not same in config window dropdown.", selectedLegend, chartConfigWindow.getSelectedDropdownLegend(chart));
        assertFalse("Selected legend is not disabled in config window.", chartConfigWindow.isLegendEnabled(chart, selectedLegend));
        chartConfigWindow.cancel();
    }

    /**
     * US DEFTFTS-1922 Verify It is possible to switch legends from configuration window for all three charts Automation of DEFTFTS-934 : Session
     * Browser - Dropdown and cog filter interaction (details tab)
     *
     * @throws ParseException
     * @throws InterruptedException
     */

    @Test
    public void cogWheelInteraction3_Radio_And_Cell_Conditions_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);

        verifyLegendsOnCharts(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, Chart.RADIO_CHART, "Radio and Cell Load Conditions");
    }

    @Test
    public void cogWheelInteraction3_TCP_Performance_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);

        verifyLegendsOnCharts(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, Chart.TCP_CHART, "TCP Performance");
    }

    @Test
    public void cogWheelInteraction3_Application_Layer_Performace_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);

        verifyLegendsOnCharts(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, Chart.APPLICATION_CHART, "Application Layer Performance");
    }

    private void verifyLegendsOnCharts(CommonDataType imsiParameter, Chart radioChart, String chartName) throws InterruptedException {
        String imsi = getIMSIParameter(imsiParameter);
        String dbDate = getEventTimeParameter(imsiParameter);

        Calendar startDate = getclosest15minStartDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
        Calendar endDate = getclosest15minEndDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));

        Date start = startDate.getTime();
        Date end = endDate.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(CommonUtils.DD_MM_YY_HH_MM_SS);
        if (detailTab.isChartPresent(radioChart, detailTab.prepareChartTitle(imsi, chartName, formatter.format(start), formatter.format(end)))) {
            verifyLegendSwitching(radioChart);

        }
    }

    private void verifyLegendSwitching(Chart chart) throws InterruptedException {
        List<String> selectedLegend = detailTab.getLegendsFromChart(chart);
        selectedLegend.remove(detailTab.getSelectedLegend(chart));
        Set<String> legends = new HashSet<String>();
        while (legends.size() != 2) {
            legends.add(selectedLegend.get(CommonUtils.getRandomNumber(0, selectedLegend.size() - 1)));
        }
        String[] legendsArray = legends.toArray(new String[1]);
        ChartConfigWindow chartConfigWindow = detailTab.getChartConfigWindow();
        detailTab.openConfigurationWindow(chart);
        if (chart == Chart.APPLICATION_CHART) {
            chartConfigWindow.selectTop3Option(false);
        }
        chartConfigWindow.selectLegends(chart, false, legendsArray);
        chartConfigWindow.update();
        for (int i = 0; i < legendsArray.length; i++) {
            assertFalse(legendsArray[i] + " is present in chart.", detailTab.isLegendPresentInChart(chart, legendsArray[i]));
        }
        // reverse
        detailTab.openConfigurationWindow(chart);
        if (chart == Chart.APPLICATION_CHART) {
            chartConfigWindow.selectTop3Option(false);
        }
        chartConfigWindow.selectLegends(chart, true, legendsArray);
        chartConfigWindow.update();
        for (int i = 0; i < legendsArray.length; i++) {
            assertTrue(legendsArray[i] + " is not present in chart.", detailTab.isLegendPresent(chart, legendsArray[i]));
        }
    }

    /**
     * US DEFTFTS-1922 Verify It is possible to add legends from dropdown menu inside configuration window for all three charts Automation of
     * DEFTFTS-934 : Session Browser - Dropdown and cog filter interaction (details tab)
     */
    @Test
    public void cogWheelInteraction4_Radio_And_Cell_Conditions_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);

        verfiyDropdownLegendSwitchOnChart(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, Chart.RADIO_CHART, "Radio and Cell Load Conditions");
    }

    @Test
    public void cogWheelInteraction4_TCP_Performace_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);

        verfiyDropdownLegendSwitchOnChart(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, Chart.TCP_CHART, "TCP Performance");
    }

    @Test
    public void cogWheelInteraction4_Application_Layer_Performance_Chart() throws ParseException, InterruptedException {
        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);

        verfiyDropdownLegendSwitchOnChart(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, Chart.APPLICATION_CHART, "Application Layer Performance");
    }

    private void verfiyDropdownLegendSwitchOnChart(CommonDataType imsiParameter, Chart radioChart, String chartName) throws InterruptedException {
        String imsi = getIMSIParameter(imsiParameter);
        String dbDate = getEventTimeParameter(imsiParameter);

        Calendar startDate = getclosest15minStartDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
        Calendar endDate = getclosest15minEndDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));

        Date start = startDate.getTime();
        Date end = endDate.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat(CommonUtils.DD_MM_YY_HH_MM_SS);
        if (detailTab.isChartPresent(radioChart, detailTab.prepareChartTitle(imsi, chartName, formatter.format(start), formatter.format(end)))) {
            verifyDropdownLegendSwitching(radioChart);

        }
    }

    private void verifyDropdownLegendSwitching(Chart chart) throws InterruptedException {
        List<String> selectedLegend = detailTab.getLegendsFromChart(chart);
        selectedLegend.remove(detailTab.getSelectedLegend(chart));
        String[] legendsArray = selectedLegend.toArray(new String[1]);
        ChartConfigWindow chartConfigWindow = detailTab.getChartConfigWindow();
        detailTab.openConfigurationWindow(chart);
        if (chart == Chart.APPLICATION_CHART) {
            chartConfigWindow.selectTop3Option(false);
        }
        chartConfigWindow.selectLegends(chart, false, legendsArray);
        chartConfigWindow.update();
        for (int i = 0; i < legendsArray.length; i++) {
            assertFalse(legendsArray[i] + " is present in chart.", detailTab.isLegendPresentInChart(chart, legendsArray[i]));
        }
        // adding legends from dropdown
        for (String legend : legendsArray) {
            detailTab.selectLegend(chart, legend);
            assertTrue(legend + " is not present in chart.", detailTab.isLegendPresentInChart(chart, legend));
        }

    }

    /**
     * US- DEFTFTS-1928 Automation of DEFTFTS-1030 Session Browser - Export Charts
     */
    @Test
    public void checkExportButton_Radio_And_Cell_Conditions_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);
        verifyExportOfCharts(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, Chart.RADIO_CHART, "Radio and Cell Load Conditions");
    }

    @Test
    public void checkExportButton_TCP_Performance_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);
        verifyExportOfCharts(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, Chart.TCP_CHART, "TCP Performance");
    }

    @Test
    public void checkExportButton_Application_Layer_Performance_Chart() throws ParseException, InterruptedException {

        driver.manage().window().maximize();

        openSessionDetail(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, false, false);
        verifyExportOfCharts(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, Chart.APPLICATION_CHART, "Application Layer Performance");
    }

    private void verifyExportOfCharts(CommonDataType imsiParameter, Chart radioChart, String chartName) throws InterruptedException {
        String imsi = getIMSIParameter(imsiParameter);
        String dbDate = getEventTimeParameter(imsiParameter);

        Calendar startDate = getclosest15minStartDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));
        Calendar endDate = getclosest15minEndDate(convertStringToCalendar(dbDate, "yyyy-MM-dd HH:mm:ss.SSS"));

        Date start = startDate.getTime();
        Date end = endDate.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat(CommonUtils.DD_MM_YY_HH_MM_SS);
        if (detailTab.isChartPresent(radioChart, detailTab.prepareChartTitle(imsi, chartName, formatter.format(start), formatter.format(end)))) {
            verifyExportChart(radioChart);

        }
    }

    private void verifyExportChart(Chart chart) throws InterruptedException {
        String exportButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//descendant::*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']//*[@id='exportButton']";
        driver.findElement(By.xpath(exportButton)).click();
        Thread.sleep(500);
        String pngExportButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//div[@class='highcharts-export-menu']/div/div[text()='Download PNG image']";
        String jpegExportButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//div[@class='highcharts-export-menu']/div/div[text()='Download JPEG image']";
        String pdfExportButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//div[@class='highcharts-export-menu']/div/div[text()='Download PDF document']";
        assertTrue("Export as PNG is not available on '" + chart.toString() + "' chart.", SessionBrowserTab.isElementPresent(pngExportButton));
        assertTrue("Export as JPEG is not available on '" + chart.toString() + "' chart.", SessionBrowserTab.isElementPresent(jpegExportButton));
        assertTrue("Export as PDF is not available on '" + chart.toString() + "' chart.", SessionBrowserTab.isElementPresent(pdfExportButton));
        // driver.findElement(By.xpath(pdfExportButton)).click();
        // Thread.sleep(5000);
    }

    /**
     * US - DEFTFTS-1925,DEFTFTS-1926
     *
     * Automation of DEFTFTS-283, Session Browser - Print Preview of "view details" Automation of DEFTFTS-941, Session Browser - Export "view details"
     * dialog box
     */
    @Ignore
    @Test
    public void checkViewDetailPrint() throws ParseException, InterruptedException {

        driver.manage().window().maximize();
        CommonDataType[] values = new CommonDataType[] { SB_IMSI_ATTACH, SB_IMSI_PDP_ACTIVATE, SB_IMSI_RAU, SB_IMSI_ISRAU, SB_IMSI_DEACTIVATE,
                SB_IMSI_DETACH, SB_IMSI_SERVICE_REQUEST, SB_IMSI_DATA_BEARER_SESSION, SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE,
                SB_IMSI_INT_CALL_SETUP_FAILURES, SB_IMSI_INT_SYSTEM_RELEASE, SB_IMSI_INT_OUT_HARD_HANDOVER, SB_IMSI_RRC_MEASUREMENT_REPORT,
                SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, SB_IMSI_INT_IFHO_HANDOVER_EXECUTION_FAILURE, SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE,
                SB_IMSI_INT_HSDSCH_NO_CELL_SELECTED };

        openSessionDetail(values[CommonUtils.getRandomNumber(0, values.length - 1)], true, true);

        String sessionRowPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index]";
        int actualCount = driver.findElements(By.xpath(sessionRowPath)).size();

        String dropdownPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
                + CommonUtils.getRandomNumber(0, Math.min(actualCount - 1, 18)) + "']/td[2]";
        driver.findElement(By.xpath(dropdownPath)).click();
        Thread.sleep(10);
        driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
        Thread.sleep(5000);

        String sectionHeadingPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div";
        int noOfheadings = driver.findElements(By.xpath(sectionHeadingPath)).size();
        String windowTitlepath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[1]/tbody/tr/td[2]/div/div";
        String windowTitle = driver.findElement(By.xpath(windowTitlepath)).getText();
        String[] headingArray = new String[noOfheadings];
        for (int index = 1; index <= noOfheadings; index++) {
            String sectionLabelPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[" + index
                    + "]/div[1]/span[1]";
            headingArray[index - 1] = driver.findElement(By.xpath(sectionLabelPath)).getText();
            expandheading(index);
        }
        String printPreviewButtonPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[1]/div";
        // checking export button
        String exportButtonPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[2]/div[@title='Export']";
        assertTrue("Export button is not present.", SessionBrowserTab.isElementPresent(exportButtonPath));
        driver.findElement(By.xpath(printPreviewButtonPath)).click();
        Thread.sleep(1000);
        String windowHandle = driver.getWindowHandle();
        driver.switchTo().frame("__printingFrame");

        // checking preview title matched window title
        String previewLabelPath = "html/body/div[2]/label";
        assertEquals("Window title not mached with preview window title.", windowTitle, driver.findElement(By.xpath(previewLabelPath)).getText());
        // checking sections are not collapsed
        for (int index = 1; index <= noOfheadings; index++) {
            String collapseImagePath = "html/body/div[2]/div/div/div[" + index + "]/div[2]";
            assertTrue("Section is not present.", SessionBrowserTab.isElementPresent(collapseImagePath));
            // checking section heading
            String sectionLabelPath = "html/body/div[2]/div/div/div[" + index + "]/div[1]/span[1]";
            assertEquals("Section heading not matched.", headingArray[index - 1], driver.findElement(By.xpath(sectionLabelPath)).getText());
            assertFalse("Section is collapsed.", isCollapsed(collapseImagePath));
        }
        driver.switchTo().window(windowHandle);
        // checking print button
        String printButtonPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td/div[@title='Print']";

        assertTrue("Print button is not present.", driver.findElement(By.xpath(printButtonPath)).isDisplayed());
        // SessionBrowserTab.isElementPresent(printButtonPath));
        String closeButtonPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[3]/div/table[1]/tbody/tr/td[5]/div";

        driver.findElement(By.xpath(closeButtonPath)).click();
        Thread.sleep(100);
        closeViewDetails();
    }

    /*
     * ********PRIVATE METHODS*********
     */

    private void expandheading(int eachHeading) {
        String collapseImagePath = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                + eachHeading + "]/div[2]";
        if (isCollapsed(collapseImagePath)) {
            String collapseImageButtonPath = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                    + eachHeading + "]//span[2]";
            driver.findElement(By.xpath(collapseImageButtonPath)).click();
        }
    }

    private boolean isCollapsed(String collapseImagePath) {
        return !driver.findElement(By.xpath(collapseImagePath)).getAttribute("style").isEmpty();
    }

    private void testPrintPreviewOfChart(int i) throws InterruptedException {
        String chartTitleXPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + i
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//*[@class='highcharts-title']";
        String expectedChartTitle = driver.findElement(By.xpath(chartTitleXPath)).getText();
        clickOnPrintChartButton(i);

        String previewWindowHeader = ".//*[text()='Print Preview']";
        assertEquals(driver.findElement(By.xpath(previewWindowHeader)).getText(), "Print Preview");
        Thread.sleep(10);
        String windowHandle = driver.getWindowHandle();
        driver.switchTo().frame("__printingFrame");
        Thread.sleep(10);

        String previewChartTitleXPath = "*//div[contains(@id,'Detail')]//div[@class='highcharts-container']//*[@class='highcharts-title']";
        assertTrue("Chart title not present.", SessionBrowserTab.isElementPresent(previewChartTitleXPath));

        String actualChartTitle = driver.findElement(By.xpath(previewChartTitleXPath)).getText();
        assertEquals("Chart title not matched.", expectedChartTitle, actualChartTitle);

        driver.switchTo().window(windowHandle);
        driver.findElement(By.xpath(Constants.PRINT_PREVIEW_WINDOW_CLOSE)).click();
    }

    private void clickOnPrintChartButton(int i) {
        String printButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + i + "]//*[@class='highcharts-container']//*[@id='printButton']";
        driver.findElement(By.xpath(printButton)).click();
    }
}
