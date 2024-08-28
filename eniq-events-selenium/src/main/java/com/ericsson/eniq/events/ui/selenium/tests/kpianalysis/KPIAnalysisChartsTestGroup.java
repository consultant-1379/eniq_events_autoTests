package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis;

import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIChartConfigWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.KpiAnalysisTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class KPIAnalysisChartsTestGroup extends KPIBaseUnitTest {
    @Autowired
    private KpiAnalysisTab kpiAnalysisTab;

    private KPIAnalysisWindow window = new KPIAnalysisWindow();

    private KPIChartConfigWindow configWidow = new KPIChartConfigWindow();

    private static final String TIME_RANGE = "2 hours";
    private static final String TIME_RANGE_15_MINUTES = "15 minutes";
    private static final String TIME_RANGE_30_MINUTES = "30 minutes";
    private static final String TIME_RANGE_1_HOUR = "1 hour";
    private static final String TIME_RANGE_12_HOUR = "12 hours";

    private static final int DEFAULT_CELL_INDEX = 1; //All Cells option

    private static final String CHART_BY_TAC = "TAC";
    private static final String CHART_BY_MAKE = "Make";
    private static final String CHART_BY_MAKE_MODEL = "Make-Model";
    
    private static final String RSCP = "RSCP";
    private static final String EC_NO = "EC/No";
    private static final String UL_INTERFFERENCE = "UL Interference";
    private static final String NUM_HS_USERS = "No. HS users in cell";

    private static final String RSCP_TITLE = "Uplink Throughput Charted By RSCP";
    private static final String EC_TITLE = "Uplink Throughput Charted By EC/No";
    private static final String UL_INTERFERENCE_TITLE = "Uplink Throughput Charted By UL Interference";
    private static final String HS_USERS_TITLE = "Uplink Throughput Charted By No. HS users in cell";

    private static final String SELECTED_RNC = "smartone_R:RNC09:RNC09,Ericsson,3G";
    private static final String NETWORK = "Network";
    private static final String UPLINK_THROUGHPUT = "Uplink Throughput";
    private static final String LOADING_DATA =  "Loading Chart Data ...";

    private static final String USER_PLANE_XPATH = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[1]/div[2]/div/div/div/*[name()='svg']/*[name()='g'][@class='highcharts-series-group']/*[name()='g'][last()]/*[name()='path']["+5+"]";
    private static final String OPTIONS_INCORRECT ="Options Not Correct!";
    private static final String TITLE_MISMATCH ="Title Not Correct!";
    
    /**
     * US: DEFTFTS-179
     * Test Case: 27.1 
     * Verify that it is possible to view Control Plane KPIs on a separate chart in main view for a 
     * specified time range when All Cells or Network selected
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void verifyControlPlaneKpis_27_1() throws InterruptedException, ParseException {
        String time = TIME_RANGE_15_MINUTES;
        String controllerOption = "Network";
        String kpiToSelect = "Service Request Failure Ratio";
        kpiAnalysisTab.launchKPIWindow(time, controllerOption, SELECTED_RNC, DEFAULT_CELL_INDEX, kpiToSelect);
        window.waitForWindowToLoad("Loading Map ...");
        
        assertTrue("Window does not launched. ", window.isWindowOpen());
        assertEquals("Selected KPI not matched with one in launch menu.", kpiToSelect, window.getSelectedKpi());
        window.waitForWindowToLoad("Loading Chart Data ...");
        Thread.sleep(2000);
        boolean chartPresent = window.isControlPlaneChartPresent();
        assertTrue("Control Plane Chart not found. ", chartPresent);
    }

    /**
     * US: DEFTFTS-179
     * Test Case: 27.2
	 * Verify for KPI Analysis Chart View that the user should be able to remove or add User Plane KPIs 
	 * to the chart using configuration panel beside dropdown menu
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void verfiyControlPlaneChartConfiguration_27_2() throws InterruptedException, ParseException {
	    String time = TIME_RANGE;
	    String controllerOption = "Network";
	    String[] kpiLegends = new String[] { "Attach Success Rate", "Detach Success Rate",
	            "PDP Context Activation Success Rate", "RAU Success Rate", "ISRAU Success Rate",
	            "Service Request Failure Ratio", "PDP Context Cutoff Ratio", "Paging Failure Ratio" };
	    kpiAnalysisTab.launchKPIWindow(time, controllerOption, SELECTED_RNC, DEFAULT_CELL_INDEX,
	            "Service Request Failure Ratio");
	    window.waitForWindowToLoad("Loading Chart Data ...");
	    window.openConfigurationWindow();
	    assertFalse("Selected kpi legend should be disabled.",
	            configWidow.isControlPlaneLegendEnabled("Service Request Failure Ratio"));
	    configWidow.selectControlPlaneLegends(true, kpiLegends);
	    configWidow.update();
	    Thread.sleep(4000);

        List<String> legends = window.listChartLegendItems();

        //remove the (No Data) tag on the legend item (just in case there is no data returned on a test).
        List<String> trimmed = new ArrayList<String>(legends.size());
        for(String legend : legends){
            trimmed.add(legend.replace("(No Data)", "").trim());
        }
        for(int i = 0; i < kpiLegends.length; i++){
            assertTrue("KPI "+kpiLegends[i] +" is not shown in the chart.", trimmed.contains(kpiLegends[i]));
        }
	}

	/**
	 * US: DEFTFTS-179
     * Test Case: 27.3
     * Verify for KPI Analysis Chart View that the user should be able to remove User Plane KPI chart by deselecting 
     * all Control Plane KPIs in configuration panel beside dropdown menu, when main KPI selected is Control Plane
     * 
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void verifyKpiWindowTitleChart_27_3() throws InterruptedException, ParseException {
        //smartone_R:RNC09:RNC09,Ericsson,3G
        String time = TIME_RANGE;
        String controllerOption = "Controller";
        //Kpi Analysis, Controller, 2G_SUNTRPRMAI_0,Ericsson,GSM, All Cells
        String[] selectedController = kpiAnalysisTab.launchKPIWindow(time, controllerOption, SELECTED_RNC,
                DEFAULT_CELL_INDEX, "Service Request Failure Ratio");
        String windowTitle = createdWindowTitle(controllerOption, selectedController[0], selectedController[1]);
        assertEquals("KPI Window title does not match.", windowTitle, window.getWindowTitle());

        kpiAnalysisTab.clickLaunch();
        controllerOption = "Network";
        //Kpi Analysis, Network
        kpiAnalysisTab.launchKPIWindow(time, controllerOption, SELECTED_RNC, DEFAULT_CELL_INDEX,
                "Downlink Throughput");
        window.waitForWindowToLoad("Loading Map ...");
        
        windowTitle = createdWindowTitle(controllerOption, selectedController[0], selectedController[1]);
        assertEquals("KPI Window title does not match.", windowTitle, window.getWindowTitle());
    }

/* commented by epagarv to remove unfixable test (clicking on svg element. )
    @Test
    public void selectPointOnChart() throws InterruptedException, ParseException {
        
        //Launch KPI...
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_15_MINUTES, "Network", SELECTED_RNC, DEFAULT_CELL_INDEX,
                "Service Request Failure Ratio");
        window.waitForWindowToLoad("Loading Chart Data ...");
        Thread.sleep(4000);

        //Expected Menu options
        List<String> expected = new ArrayList<String>(3);
        expected.add(CHART_BY_TAC);
        expected.add(CHART_BY_MAKE);
        expected.add(CHART_BY_MAKE_MODEL);

        if(window.isChartLegendItemPresent("Service Request Failure Ratio (No Data)")){
            fail("TEST FAILED: Insufficient data available to perform test.");
        }
        //Select the last point on the chart...
        try{
            window.clickPointOnChart(1);
        }catch (NoDataException e){
            fail("WARNING!: No data found for the selected KPI.");
        }
        //WebElement chartByOptionsDialog =  window.getChartByOptions();
        List<String> linkNames = window.getChartByOptionsLinkNames();
        assertTrue("There should be 3 links on the Chart By Options menu but got "+linkNames.size(), linkNames.size()==3);
        
        for(String menuOption:expected){
            assertTrue("The drilldown option "+menuOption+ " is missing from the Drill By Options popup menu", linkNames.contains(menuOption));
        }

        //select Chart By Make from the popup...
        window.clickDrillByOption(2);
        String title = window.getFloatingWindowTitle(2);
        String expectedWindowTitle = "Service Request Failure Ratio Charted By "+CHART_BY_MAKE;

        //check that the correct title is shown in the Chart by Window.
        assertEquals("Expected the window: 'Service Request Failure Ratio Charted By Make'. Actual window: "+title, expectedWindowTitle, title);
        Thread.sleep(1000);
    }

    @Test
    public void openChartByTAC() throws InterruptedException, ParseException {

        //Launch KPI...
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_15_MINUTES, "Network", SELECTED_RNC, DEFAULT_CELL_INDEX,
                "Service Request Failure Ratio");
        window.waitForWindowToLoad("Loading Chart Data ...");
        Thread.sleep(4000);

        if(window.isChartLegendItemPresent("Service Request Failure Ratio (No Data)")){
            fail("TEST FAILED: Insufficient data available to perform test.");
        }

        try {
            window.chartByTAC(1);
        } catch (NoDataException e) {
            fail("WARNING!: No data found for the selected KPI.");
        }
        String title = window.getFloatingWindowTitle(2);
        String expectedWindowTitle = "Service Request Failure Ratio Charted By "+CHART_BY_TAC;

        //check that the correct title is shown in the Chart by Window.
        assertEquals("Expected the window: 'Service Request Failure Ratio Charted By TAC'. Actual window: "+title, expectedWindowTitle, title);
    }

    @Test
    public void openChartByMake() throws InterruptedException, ParseException {

        //Launch KPI...
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_15_MINUTES, "Network", SELECTED_RNC, DEFAULT_CELL_INDEX,
                "Service Request Failure Ratio");
        window.waitForWindowToLoad("Loading Chart Data ...");
        Thread.sleep(4000);

        if(window.isChartLegendItemPresent("Service Request Failure Ratio (No Data)")){
            fail("TEST FAILED: Insufficient data available to perform test.");
        }

        try {
            window.chartByMake(1);
        } catch (NoDataException e) {
            fail("WARNING!: No data found for the selected KPI.");
        }
        String title = window.getFloatingWindowTitle(2);
        String expectedWindowTitle = "Service Request Failure Ratio Charted By "+CHART_BY_MAKE;

        //check that the correct title is shown in the Chart by Window.
        assertEquals("Expected the window: 'Service Request Failure Ratio Charted By Make'. Actual window: "+title, expectedWindowTitle, title);
    }

    @Test
    public void openChartByMakeModel() throws InterruptedException, ParseException {

        //Launch KPI...
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_15_MINUTES, "Network", SELECTED_RNC, DEFAULT_CELL_INDEX,
                "Service Request Failure Ratio");
        window.waitForWindowToLoad("Loading Chart Data ...");
        Thread.sleep(4000);

        if(window.isChartLegendItemPresent("Service Request Failure Ratio (No Data)")){
            fail("TEST FAILED: Insufficient data available to perform test.");
        }

       try {
            window.chartByMakeModel(1);
        } catch (NoDataException e) {
            fail("WARNING!: No data found for the selected KPI.");
        }
        String title = window.getFloatingWindowTitle(2);
        String expectedWindowTitle = "Service Request Failure Ratio Charted By "+CHART_BY_MAKE_MODEL;

        //check that the correct title is shown in the Chart by Window.
        assertEquals("Expected the window: 'Service Request Failure Ratio Charted By Make-Model'. Actual window: "+title, expectedWindowTitle, title);
    }

    @Test
    public void verifyUserPlaneDrilldown() throws InterruptedException, ParseException {
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_12_HOUR, NETWORK, SELECTED_RNC, DEFAULT_CELL_INDEX,
                UPLINK_THROUGHPUT);
        window.waitForWindowToLoad(LOADING_DATA);
        clickPointOnChart();
        verifyOptionNames();
    }

    @Test
    public void verifyUserPlaneRSCPChart() throws InterruptedException, ParseException {
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_12_HOUR, NETWORK, SELECTED_RNC, DEFAULT_CELL_INDEX,
                UPLINK_THROUGHPUT);
        window.waitForWindowToLoad(LOADING_DATA);
        clickPointOnChart();
        window.clickDrillByOption(1);
        verifyRSCPChartTitle();
    }

    @Test
    public void verifyUserPlaneECChart() throws InterruptedException, ParseException {
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_12_HOUR, NETWORK, SELECTED_RNC, DEFAULT_CELL_INDEX,
                UPLINK_THROUGHPUT);
        window.waitForWindowToLoad(LOADING_DATA);
        clickPointOnChart();
        window.clickDrillByOption(2);
        verifyECChartTitle();
    }


    @Test
    public void verifyUserPlaneULInterferenceChart() throws InterruptedException, ParseException {
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_12_HOUR, NETWORK, SELECTED_RNC, DEFAULT_CELL_INDEX,
                UPLINK_THROUGHPUT);
        window.waitForWindowToLoad(LOADING_DATA);
        clickPointOnChart();
        window.clickDrillByOption(3);
        verifyULInterferenceChartTitle();
    }

    @Test
    public void verifyUserPlaneHSUsersChart() throws InterruptedException, ParseException {
        kpiAnalysisTab.launchKPIWindow(TIME_RANGE_12_HOUR, NETWORK, SELECTED_RNC, DEFAULT_CELL_INDEX,
                UPLINK_THROUGHPUT);
        window.waitForWindowToLoad(LOADING_DATA);
        clickPointOnChart();
        window.clickDrillByOption(4);
        verifyHSUsersChartTitle();
    }
*/
    
    private void verifyRSCPChartTitle() {
        String actualTitle = window.getFloatingWindowTitle(2);
        assertEquals(TITLE_MISMATCH, actualTitle, RSCP_TITLE);
    }

    private void verifyECChartTitle() {
        String actualTitle = window.getFloatingWindowTitle(2);
        assertEquals(TITLE_MISMATCH, actualTitle, EC_TITLE);
    }

    private void verifyULInterferenceChartTitle() {
        String actualTitle = window.getFloatingWindowTitle(2);
        assertEquals(TITLE_MISMATCH, actualTitle, UL_INTERFERENCE_TITLE);
    }

    private void verifyHSUsersChartTitle() {
        String actualTitle = window.getFloatingWindowTitle(2);
        assertEquals(TITLE_MISMATCH, actualTitle, HS_USERS_TITLE);
    }

    private void verifyOptionNames() {
        List<String> expectedOptions = Arrays.asList(RSCP, EC_NO, UL_INTERFFERENCE, NUM_HS_USERS);
        List<String> linkNames = window.getChartByOptionsLinkNames();
        assertEquals(OPTIONS_INCORRECT, linkNames, expectedOptions);
    }

    private void clickPointOnChart() {
        String xPath =USER_PLANE_XPATH;
        driver.findElement(By.xpath(xPath)).click();
    }
    
    private String createdWindowTitle(String controllerOption, String selectedController, String selectedCell) {
        return "KPI Analysis, "
                + controllerOption
                + (controllerOption.equalsIgnoreCase("Controller") ? (", " + selectedController + (selectedCell
                        .equalsIgnoreCase("All Cells") ? "" : (", " + selectedCell))) : "");
    }
}
