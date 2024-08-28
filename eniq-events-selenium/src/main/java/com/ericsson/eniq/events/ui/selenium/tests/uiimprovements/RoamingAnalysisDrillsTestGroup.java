package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RoamingAnalysisDrillsTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired private NetworkTab networkTab;
    @Autowired @Qualifier("roamingByOperatorEvents") private CommonWindow roamingByOperatorEvents;
    @Autowired @Qualifier("roamingByCountryEvents") private CommonWindow roamingByCountryEvents;
    @Autowired @Qualifier("drillCoreRoamingByCountryEventsSubpie") private CommonWindow drillCoreRoamingByCountryEventsSubpie;
    @Autowired @Qualifier("drillCoreRoamingByOperatorEventsSubpie") private CommonWindow drillCoreRoamingByOperatorEventsSubpie;
    @Autowired WorkspaceRC workspaceRC;

    static final String ROAMING_BY_OPERATOR_EVENT_VOLUME = "//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][1]";
    static final String ROAMING_BY_COUNTRY_EVENT_VOLUME = "//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][2]";
    static final String ROAMING_BY_COUNTRY_PIE_CHART = "//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-point')][2]//node()[name()='path'][2]";
    static final String ROAMING_BY_OPERATOR_PIE_CHART = "//div[@id='selenium_tag_baseWindow'][5]//node()[contains(@class,'highcharts-point')][2]//node()[name()='path'][2]";

    final List<NetworkTab.SubStartMenu> OperatorSubMenuList = new ArrayList<NetworkTab.SubStartMenu>(Arrays.asList(
            NetworkTab.SubStartMenu.ROAMING_ANALYSIS_CORE,
            NetworkTab.SubStartMenu.ROAMING_BY_OPERATOR,
            NetworkTab.SubStartMenu.ROAMING_BY_OPERATOR_EVENT_VOLUME));

    final List<NetworkTab.SubStartMenu> CountrySubMenuList = new ArrayList<NetworkTab.SubStartMenu>(Arrays.asList(
            NetworkTab.SubStartMenu.ROAMING_ANALYSIS_CORE,
            NetworkTab.SubStartMenu.ROAMING_BY_COUNTRY,
            NetworkTab.SubStartMenu.ROAMING_BY_COUNTRY_EVENT_VOLUME));

    static final String TIME_SETTINGS_BUTTON_ROAMING_BY_COUNTRY_EVENTS = "//div[@id='selenium_tag_baseWindow']//div[@id='btnTime']/img";
    static final String REFRESH_BUTTON_ROAMING_BY_COUNTRY_EVENTS = "//div[@id='selenium_tag_baseWindow']//div[@id='btnRefresh']/img";
    static final String LEGEND_BUTTON_ROAMING_BY_COUNTRY_EVENTS = "//div[@id='selenium_tag_baseWindow']//div[@id='btnHideShowLegend']/img";
    static final String TIME_RANGE_COMP_ROAMING_BY_COUNTRY_EVENTS = "//div[@id='selenium_tag_baseWindow']//div[@id='timeRangeComp']";
    static final String LEGEND_BUTTON_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS = "//div[@id='selenium_tag_baseWindow']//div[@id='btnHideShowLegend']/img";
    static final String TIME_SETTINGS_BUTTON_DRILL_ROAMING_BY_EVENTS_SUBPIE = "//div[@id='selenium_tag_baseWindow']//div//table//tbody//tr//td//em//button";
    static final String REFRESH_BUTTON_DRILL_ROAMING_BY_EVENTS_SUBPIE = "//div[@id='selenium_tag_baseWindow']//div//table//tbody//tr//td//em//button";
    static final String TIME_RANGE_COMP_DRILL_ROAMING_BY_EVENTS_SUBPIE = "//div[@id='selenium_tag_baseWindow']//div[@id='timeRangeComp']";

    @Override @Before public void setUp() {
     super.setUp();
    }

    /** Test Case 4.8.1:Verify that it is possible to drilldown from all roaming analyses bar charts for Core*/
    @Test public void drillDownFromAllRoamingAnalysisBarChartsForCore_4_8_1() throws InterruptedException, PopUpException {
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_CS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.DATA_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);

        Thread.sleep(3000);
        int barChartSize = selenium
                .getXpathCount(
                        "//div[@id='selenium_tag_BaseWindow_ROAMING_BY_OPERATOR_EVENTS']//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']")
                .intValue();

        for (int i = 1; i <= barChartSize; i++) {
            DRILL_DOWN_BAR_CHARTS_BY_OPERATOR(i);
            waitForPageLoadingToComplete();
        }
    }

    /**
     * Test case 4.8.2: Verify that when drilled down on a particular country's bar chart from any of the roaming
     * analyses by Country, the popup window should contain the country's name in its title and launches a pie chart.*/
    @Test public void windowShouldContainTheCountryNameInItsTitleAndLaunchesPieChart_4_8_2() throws PopUpException, InterruptedException {
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_CS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.DATA_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);

        Thread.sleep(3000);
        int barChartSize = selenium
                .getXpathCount(
                        "//div[@id='selenium_tag_BaseWindow_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']")
                .intValue();
        for (int i = 1; i <= barChartSize; i++) {
            String barChartOperatorsName = selenium
                    .getText("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']["
                            + i + "]//node()[name()='tspan']");
            DRILL_DOWN_BAR_CHARTS_BY_COUNTRY(i);
            String titleOfPieChart = selenium
                    .getText("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS']//div//span");
            String changedPieChartTitle = titleOfPieChart.replace("Roaming by Country", "");
            String pieChartTitle = changedPieChartTitle.trim();

            if (barChartOperatorsName.equals(pieChartTitle)) {
                assertTrue(true);
            } else {
                assertTrue("Title of a launched pie chart is not the coutries name", false);
            }
        }
    }

    /**
     * Test case4.8.3:verify that when drilled down on a particular operators bar chart from any of the roaming analyses
     * by Operator, the popup window should contain the operators name in its title and launches a pie chart.*/
    @Test public void windowShouldContainTheOperatorNameInItsTitleAndLaunchesPieChart_4_8_3() throws PopUpException, InterruptedException {

        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_CS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);
        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.CORE_NETWORK_PS, DataIntegrityStringConstants.DATA_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);

        Thread.sleep(3000);
        int barChartSize = selenium
                .getXpathCount(
                        "//div[@id='selenium_tag_BaseWindow_ROAMING_BY_OPERATOR_EVENTS']//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']")
                .intValue();
        for (int i = 1; i <= barChartSize; i++) {
            String barChartOperatorsName = selenium
                    .getText("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_OPERATOR_EVENTS']//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']["
                            + i + "]//node()[name()='tspan']");
            DRILL_DOWN_BAR_CHARTS_BY_OPERATOR(i);
            waitForPageLoadingToComplete();
            String titleOfPiechart = selenium
                    .getText("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART_ROAMING_BY_OPERATOR_EVENTS']//div//span");

            String changedTitleOfPiechart = titleOfPiechart.replace("Roaming by Operator", "");
            String pieChartTitle = changedTitleOfPiechart.trim();
            if (barChartOperatorsName.equals(pieChartTitle)) {
                assertTrue(true);
            } else {
                assertTrue(
                        "Drilled down on a particular countrys bar chart differs in the title of the launched pie chart",
                        false);
            }
            pause(2000);
        }
    }

    /** Test case4.8.9: Verify that it is possible to drilldown from Pie chart to display the Event Analysis table.*/
    @Test public void possibleToDrillDownFromPieChartToDisplayTheEventAnalysisTable_4_8_9() throws PopUpException, InterruptedException {
        workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);
        
        Thread.sleep(3000);
        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
        //drillCoreRoamingByCountryEventsSubpie.drillOnPieObject(2);
        DRILL_DOWN_PIE_CHARTS_BY_COUNTRY();
        assertTrue(
                "Drill down from pie chart does not display the Event Analysis table",
                selenium.isElementPresent("//div[@id='selenium_tag_baseWindow']/div/div/div/div/span[text()='Roaming by Country (DEACTIVATE)']")
                || selenium.isElementPresent("//div[@id='selenium_tag_baseWindow']/div/div/div/div/span[text()='Roaming by Country (L_ATTACH)']")
                || selenium.isElementPresent("//div[@id='selenium_tag_baseWindow']/div/div/div/div/span[text()='Roaming by Country (ATTACH)']"));
    }

    /**
     * Test case 4.8.11: Verify that all the drilled down windows taskbars have the Time Constraint, Legend Info,
     * Refresh buttons and also time selection dropdown box, wherever required.*/
    @Test public void drilledDownWindowsTaskbarHasTheTimeLegendRefreshButtonsAndTimeSelectionDropdown_4_8_11() throws InterruptedException, PopUpException {
    	workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);
        
        Thread.sleep(3000);
        assertTrue("Time Settings Button Not Found",
                selenium.isElementPresent(TIME_SETTINGS_BUTTON_ROAMING_BY_COUNTRY_EVENTS));
        assertTrue("Refresh Button Not Found", selenium.isElementPresent(REFRESH_BUTTON_ROAMING_BY_COUNTRY_EVENTS));
        assertTrue("Legend Button Not Found", selenium.isElementPresent(LEGEND_BUTTON_ROAMING_BY_COUNTRY_EVENTS));
        assertTrue("Time Constraints Not Found", selenium.isElementPresent(TIME_RANGE_COMP_ROAMING_BY_COUNTRY_EVENTS));
        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
        assertTrue("Legend Button Not Found",
                selenium.isElementPresent(LEGEND_BUTTON_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS));
       
        DRILL_DOWN_PIE_CHARTS_BY_COUNTRY();
        Thread.sleep(2000);
        assertTrue("Time Constraints Not Found",
                selenium.isElementPresent(TIME_RANGE_COMP_DRILL_ROAMING_BY_EVENTS_SUBPIE));
      }

    /**
     * Test case 4.8.12: Verify that the time period on all the drilled down windows matches with that on Roaming
     * Analysis bar chart launched for.*/
    @Test public void timePeriodDrilledDownWindowsMatchesThatOnRoamingAnalysisBarChartLaunched_4_8_12() throws PopUpException, InterruptedException {
    	workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);

        //roamingByCountryEvents.setTimeAndDateRange(getStartDate(), TimeCandidates.AM_00_00, getEndDate(), TimeCandidates.AM_00_00);
        Thread.sleep(5000);
        String BarChartTimePeriod = new String(selenium.getValue("//div[@id='selenium_tag_baseWindow']//div[@id='timeRangeComp']/input"));
        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
        String pieChartTimePeriod = new String(selenium.getText("//div[@id='selenium_tag_baseWindow']//*//label[@id='timeRangeComp']"));
        assertTrue(BarChartTimePeriod.equals(pieChartTimePeriod));
        
        DRILL_DOWN_PIE_CHARTS_BY_COUNTRY();
        String EventAnalysisTableTimePeriod = new String(selenium.getValue("//div[@id='selenium_tag_baseWindow']//div[@id='timeRangeComp']/input"));
        assertTrue("Time period on all the drilled down windows does not matches with Roaming Analysis bar chart launched",BarChartTimePeriod.equals(EventAnalysisTableTimePeriod));
    }

    /** Test Case 4.13.3: Verify that the types of event are mentioned on each slice of the pie chart*/
    @Test public void eventAreMentionedOnEachSliceOfThePieChart_4_13_3() throws PopUpException, InterruptedException {
    	workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);

        Thread.sleep(5000);
        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
        Thread.sleep(5000);

        int eventSize = selenium
                .getXpathCount(
                        "//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-point')]")
                .intValue();

        /*
        for (int i = 1; i <= eventSize; i++) {
        	
            if (selenium
                    .isElementPresent("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-data-labels')]//node()[name()='text']["
                            + i + "]//node()[name()='tspan']")) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
           
            for (int a = 1; a <= eventSize; a++) {
                if (selenium
                        .isElementPresent("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-point')]["
                                + a + "]//node()[name()='path']")) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }

            }
        }
        */
        int lableSize = selenium.getXpathCount("//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-data-labels')]//node()[name()='text']//node()[name()='tspan']").intValue();
        if (lableSize == eventSize){
        	assertTrue(true);
        }else{
        	assertTrue(false);
        }
        
    }

    /**
     * Test case 4.13.7: Verify that the columns on the grid and its children, that results from drilling on the pie
     * chart has column reordering, column order persisting, column hiding/showing, column shown/hidden persisting and
     * filtering enabled.*/
    @Test public void launchedGridFromPieChartHasCrCopChsFilteringEnabled_4_13_7() throws PopUpException, InterruptedException, NoDataException {
        workspaceRC.selectTimeRange(SeleniumConstants.DATE_TIME_6HOUR);
        workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_COUNTRY);
        
        Thread.sleep(5000);
        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
        DRILL_DOWN_PIE_CHARTS_BY_COUNTRY();
        dragAndDropObject();
        Thread.sleep(3000);

        List<String> gridTableHeadersByCountryEvents = drillCoreRoamingByCountryEventsSubpie.getTableHeaders();
        int IMSIindexInRoamingByCountry = gridTableHeadersByCountryEvents.indexOf("IMSI");

        workspaceRC.checkAndOpenSideLaunchBar();
        workspaceRC.launchWindow(SeleniumConstants.DATE_TIME_30,SeleniumConstants.CORE_NETWORK_PS,null,null, DataIntegrityStringConstants.EVENT_VOLUME, DataIntegrityStringConstants.ROAMING_BY_OPERATOR);

        Thread.sleep(5000);
        DRILL_DOWN_BAR_CHARTS_BY_OPERATOR();
        waitForPageLoadingToComplete();
        Thread.sleep(5000);
        DRILL_DOWN_PIE_CHARTS_BY_OPERATOR();
        List<String> gridTableHeadersByOperatorEvents = drillCoreRoamingByOperatorEventsSubpie.getTableHeaders();

        int IMSIindexinRoamingByOperator = gridTableHeadersByOperatorEvents.indexOf("IMSI");
        if (IMSIindexinRoamingByOperator == IMSIindexInRoamingByCountry) {
            assertTrue(true);
        } else {
            assertTrue("Column Order Persisting fails", false);
        }

        int rowCount = drillCoreRoamingByOperatorEventsSubpie.getTableRowCount();
        if (rowCount != 0)
            drillCoreRoamingByOperatorEventsSubpie.filterColumnGreaterThan(30000000, GuiStringConstants.TAC);

        int IMSIindexAfterColumnFiltering = gridTableHeadersByOperatorEvents.indexOf("IMSI");
        if (IMSIindexinRoamingByOperator == IMSIindexAfterColumnFiltering) {
            assertTrue(true);
        } else {
            assertTrue("Column Order changes after filtering", false);
        }
    }

//    /**
//     * Test Case 4.13.6:Verify that drilling down in a pie chart window launches a corresponding grid. The grid window
//     * should have the drilled event as its title.
//     */
//    @Test public void drilledDownPieChartShoudHaveTheDrilledEventAsItsTitleInTheLaunchedGrid_4_13_6() throws PopUpException, InterruptedException {
//
//        networkTab.openSubMenusFromStartMenu(StartMenu.ROAMING_ANALYSIS, CountrySubMenuList);
//        Thread.sleep(5000);
//        DRILL_DOWN_BAR_CHARTS_BY_COUNTRY();
//
//        String pieChartEventID = new String(selenium.getText("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-data-labels')]//node()[name()='text'][2]//node()[name()='tspan']"));
//
//        DRILL_DOWN_PIE_CHARTS_BY_COUNTRY();
//        String titleOfLaunchedGrid = selenium
//                .getText("//div[@id='selenium_tag_BaseWindow_DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div//span");
//
//        String launchedEventGridWindow = titleOfLaunchedGrid.substring(20, titleOfLaunchedGrid.length() - 1);
//
//        if (pieChartEventID.equals(launchedEventGridWindow)) {
//            assertTrue(true);
//        } else {
//            assertTrue("Grid window did not have EventID as its title", false);
//        }
//    }

    // *********************************************** PRIVATE METHODS ********************************************** //
    private void DRILL_DOWN_BAR_CHARTS_BY_OPERATOR() throws InterruptedException {

        selenium.click("//div[@id='selenium_tag_baseWindow']/div/div/div/div/span[text()='Event Volume Roaming by Operator']");
        selenium.mouseOver("//div[@id='selenium_tag_baseWindow'][4]//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][2]");
        selenium.click("//div[@id='selenium_tag_baseWindow'][4]//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][2]");
        Thread.sleep(5000);

        if (selenium
                .isElementPresent("//div[@class='dialogBox']//div//table//div[text()='Server returned unexpected HTTP status code: 500']")) {
            System.out.println("Unexpected 500 Error");
            assertTrue(false);
        } else {
            assertTrue("Drill down operation was not successful from roaming analysis by operator bar charts",
                    selenium.isElementPresent("//div[@id='selenium_tag_baseWindow']"));

        }
    }

    private void DRILL_DOWN_BAR_CHARTS_BY_OPERATOR(int i) throws InterruptedException {

        selenium.mouseOver("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_OPERATOR_EVENTS']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']["
                + i + "]");
        selenium.click("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_OPERATOR_EVENTS']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']["
                + i + "]");
        Thread.sleep(5000);

        if (selenium
                .isElementPresent("//div[@class='dialogBox']//div//table//div[text()='Server returned unexpected HTTP status code: 500']")) {

            assertTrue("Unexpected 500 Error", false);
        } else {
            assertTrue("Drill down operation was not successful from roaming analysis by operator bar charts",
                    selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART"
                            + "_ROAMING_BY_OPERATOR_EVENTS']"));

        }
    }

    private void DRILL_DOWN_BAR_CHARTS_BY_COUNTRY(int i) throws InterruptedException {

        selenium.mouseOver("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']["
                + i + "]");
        selenium.click("//div[@id='selenium_tag_BaseWindow_ROAMING_BY_COUNTRY_EVENTS']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']["
                + i + "]");
        Thread.sleep(5000);

        if (selenium
                .isElementPresent("//div[@class='dialogBox']//div//table//div[text()='Server returned unexpected HTTP status code: 500']")) {

            assertTrue("Unexpected 500 Error", false);
        } else {
            assertTrue("Drill down operation was not successful from roaming analysis by operator bar charts",
                    selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_DRILL_GRID_CHART"
                            + "_ROAMING_BY_COUNTRY_EVENTS']"));

        }

    }

    private void DRILL_DOWN_BAR_CHARTS_BY_COUNTRY() throws InterruptedException, PopUpException {

        waitForPageLoadingToComplete();
        selenium.mouseOver(ROAMING_BY_COUNTRY_EVENT_VOLUME);
        selenium.click(ROAMING_BY_COUNTRY_EVENT_VOLUME);
        Thread.sleep(5000);

        if (selenium
                .isElementPresent("//div[@class='dialogBox']//div//table//div[text()='Server returned unexpected HTTP status code: 500']")) {

            assertTrue(false);
        } else {
            assertTrue("Drill down operation was not successful from roaming analysis by country bar charts",
                    selenium.isElementPresent("//div[@id='selenium_tag_baseWindow'][2]/div/div/div/div/span[text()='United States of America Roaming by Country']")
                    || selenium.isElementPresent("//div[@id='selenium_tag_baseWindow'][2]/div/div/div/div/span[text()='China Roaming by Country']"));
            waitForPageLoadingToComplete();
        }
    }

    private void DRILL_DOWN_PIE_CHARTS_BY_COUNTRY() throws InterruptedException, PopUpException {
    selenium.mouseOver(ROAMING_BY_COUNTRY_PIE_CHART);
    Thread.sleep(5000);
    selenium.click(ROAMING_BY_COUNTRY_PIE_CHART);
    Thread.sleep(10000);
    waitForPageLoadingToComplete();

    }

    private void DRILL_DOWN_PIE_CHARTS_BY_OPERATOR() throws InterruptedException, PopUpException {
        selenium.mouseOver(ROAMING_BY_OPERATOR_PIE_CHART);
        Thread.sleep(5000);
        selenium.click(ROAMING_BY_OPERATOR_PIE_CHART);
        Thread.sleep(10000);
        waitForPageLoadingToComplete();

    }

    private Date getStartDate() {
        final String stDate = "2012,02,01";
        final String startDateArray[] = stDate.split(",");

        final Date sDate = roamingByOperatorEvents.getDate(Integer.parseInt(startDateArray[0]),
                Integer.parseInt(startDateArray[1]), Integer.parseInt(startDateArray[2]));
        return sDate;
    }

    private Date getEndDate() {
        final String edDate = "2012,02,29";
        final String EndDateArray[] = edDate.split(",");
        final Date eDate = roamingByOperatorEvents.getDate(Integer.parseInt(EndDateArray[0]),
                Integer.parseInt(EndDateArray[1]), Integer.parseInt(EndDateArray[2]));
        return eDate;
    }

    // TODO: consider to use method CommonWindow.dragAndDropHeader(...) instead
    private void dragAndDropObject() throws InterruptedException {

        int colCount = selenium.getXpathCount(
                "//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//table//tbody//tr/td/div[span]").intValue();

        int oldIMSIIndex = -1;
        int oldTerminalModelIndex = -1;

        int i = 0;
        while (i < colCount) {
            if (selenium
                    .isElementPresent("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//table//tbody//tr/td["
                            + i + "]/div[span='IMSI']")) {
                oldIMSIIndex = i;
            } else if (selenium
                    .isElementPresent("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//table//tbody//tr/td["
                            + i + "]/div[span='Terminal Model']")) {
                oldTerminalModelIndex = i;
            }

            i++;
        }

        assertTrue(oldIMSIIndex != -1);
        assertTrue(oldTerminalModelIndex != -1);

        selenium.mouseDownAt("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='IMSI']", "10, 5");
        selenium.mouseMoveAt("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='IMSI']", "20, 5");
        selenium.mouseOut("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='IMSI']");
        selenium.mouseOver("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='Terminal Model']");

        Number w = selenium
                .getElementWidth("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='Terminal Model']");
        int halfWidth = w.intValue() / 2;

        boolean apnBeforeTotals = oldIMSIIndex < oldTerminalModelIndex;
        int offset = halfWidth + 10 * (apnBeforeTotals ? 1 : -1);

        selenium.mouseMoveAt("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='Terminal Model']",
                offset + ",0");
        Thread.sleep(2000);

        selenium.mouseUpAt("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//div[span='Terminal Model']",
                offset + ",0");
        Thread.sleep(2000);
        assertTrue(
                "Not able to drag and drop",
                selenium.isElementPresent("//div[@id='DRILL_CORE_ROAMING_BY_COUNTRY_EVENTS_SUBPIE']//table//tbody//tr/td["
                        + oldTerminalModelIndex + "]/div[span='IMSI']"));
    }
}
