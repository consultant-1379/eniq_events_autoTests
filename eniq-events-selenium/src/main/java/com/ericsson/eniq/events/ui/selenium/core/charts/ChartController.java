package com.ericsson.eniq.events.ui.selenium.core.charts;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChartController implements ChartControls {

    @Autowired
    private EricssonSelenium selenium;

    private final String windowId;

    private final String drillableBarChartObjectId;

    private final String chartRefreshButtonId;

    private final String drillablePieChartObjectId;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    private final String PIE_CHART_LEGEND_XPATH = "//node()[@class='highcharts-legend']//node()[name()='text']";

    private final String BAR_CHART_X_ASIX_ELEMENTS_XPATH = "//div[@id='selenium_tag_baseWindow']//div[starts-with(@id,'highcharts')]//node()[contains(@class,'highcharts-axis')][1]//node()[name()='text']";

    public ChartController(final String id) {

        windowId = "//div[@id='selenium_tag_BaseWindow_" + id + "']";
        drillableBarChartObjectId = "//div[@id='selenium_tag_baseWindow']//div[starts-with(@id,'highcharts')]//rect";
        drillablePieChartObjectId = windowId + "//node()[contains(@class,'highcharts-point')]/node()[name()='path']";
        chartRefreshButtonId = "//div[@id='selenium_tag_baseWindow']//div[@id='btnRefresh']";
    }

    public void drillOnBarChartObject(final int barChartObjectNumber) throws PopUpException {
        final String barChartObject = "[" + Integer.toString(barChartObjectNumber) + "]";
        final String drillableBarChartObject = drillableBarChartObjectId + barChartObject;
        selenium.mouseOver(drillableBarChartObject);
        selenium.click(drillableBarChartObject);
        selenium.waitForPageLoadingToComplete();
    }

    public void drillOnPieChartObjectByCountry(final int pieChartObjectNumber) throws PopUpException {
        final String barChartObject = "[" + Integer.toString(pieChartObjectNumber) + "]";
        final String drillablePieChartObject = drillablePieChartObjectId + barChartObject;

        selenium.mouseOver(drillablePieChartObject);
        selenium.click(drillablePieChartObject);
        selenium.waitForPageLoadingToComplete();
    }

    public void drillOnPieChartObjectByOperator(final int pieChartObjectNumber) throws PopUpException {
        final String barChartObject = "[" + Integer.toString(pieChartObjectNumber) + "]";
        final String drillablePieChartObject = drillablePieChartObjectId + barChartObject;

        selenium.mouseOver(drillablePieChartObject);
        selenium.click(drillablePieChartObject);
        selenium.waitForPageLoadingToComplete();
    }

    public void chartRefresh() throws InterruptedException {
        selenium.click(chartRefreshButtonId);
        Thread.sleep(3000);
    }

    /**
     * Retrieve index location of pie
     * portion from high-charts legends
     */
    public int getIndex(final String pieTitle, final String xPath) {
        selenium.setSpeed("500");
        threadSleep(5000);
        logger.log(Level.INFO, " ********* IS ELEMENT PRESENT (getIndex): " + selenium.isElementPresent(xPath));
        int alltextElementCount = selenium.getXpathCount(xPath).intValue();
        logger.log(Level.INFO, " ********* alltextElementCount (getIndex): " + alltextElementCount);

        int i = 1;
        while (!selenium.isElementPresent(xPath + "[" + i + "]" + "/*[text() = '" + pieTitle + "']")
                && i < (alltextElementCount + 1)) {
            String a = xPath + "[" + i + "]" + "/*[text() ='" + pieTitle + "']";

            logger.log(Level.INFO, "IS ELEMENT PRESENT for :  " + selenium.isElementPresent(a) + " " + a);
            if (selenium.isElementPresent(a)) {
                break;
            }
            i++;
        }
        selenium.setSpeed("0");
        logger.log(Level.INFO, "*** INDEX *** :  " + i);
        return i;

    }

    private void threadSleep(final int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * drilldown on pie portion associated
     * with pie title index location of pie portion title will be used to find
     * associated pie portion
     */
    public void drilldownOnHeaderPortion(final String piePortion) throws PopUpException {
        int index = getIndex(piePortion, PIE_CHART_LEGEND_XPATH);
        selenium.setSpeed("1000");
        String textPath = "//*[contains(@class,'highcharts-point')][" + index + "]/*[contains(local-name(),'path')][2]";
        selenium.mouseOver(textPath);
        selenium.click(textPath);
        selenium.waitForPageLoadingToComplete();
        selenium.setSpeed("0");
    }

    /**
     * Written by elukpot
     *
     * Takes the first item in the chart's legend and uses that that to drill on the pie chart.
     */
    public void drillOnPieChartUsingFirstLegendItem() {

        selenium.setSpeed("1000");
        String textPath = "//*[contains(@class,'highcharts-point')][1]/*[contains(local-name(),'path')][2]";
        selenium.mouseOver(textPath);
        selenium.click(textPath);
        try {
            selenium.waitForPageLoadingToComplete();
        } catch (PopUpException e) { System.err.println("There was a popup."); e.printStackTrace(); }
        selenium.setSpeed("0");
    }

    /**
     * Written by elukpot
     *
     * Takes the first item in the chart's legend and uses that that to drill on the pie chart, of the specified seleniumTagBaseWindow.
     *
     * @param seleniumTagBaseWindowNumber The launch order of the window that you want to drill on.
     */
    public void drillOnPieChartUsingFirstLegendItemInSeleniumTagBaseWindow(int seleniumTagBaseWindowNumber) {

        selenium.setSpeed("1000");
        String textPath = "//div[@id='selenium_tag_baseWindow']["+seleniumTagBaseWindowNumber+"]//*[contains(@class,'highcharts-point')][1]/*[contains(local-name(),'path')][2]";
        selenium.mouseOver(textPath);
        selenium.click(textPath);
        try {
            selenium.waitForPageLoadingToComplete();
        } catch (PopUpException e) { System.err.println("There was a popup."); e.printStackTrace(); }
        selenium.setSpeed("0");
    }

    /**
     * updated by efreass 20 August 2012 more appropriate for rankingTestGroup
     * hfa/cfa will be refactor later to be more usable or merge with code above
     */
    public void openChartView() throws PopUpException {
        selenium.setSpeed("2000");
        final String CHART_RADIO_BUTTON = "//*[@class='x-panel-body x-panel-body-noheader'] /div/div[2]/div/span[1]/input";
        final String CHART_SELECT_ALL = "//*[@class='x-panel-body x-panel-body-noheader']//div//div[2]/span/input";
        final String CHART_LAUNCH_BUTTON = "//*[@class='x-panel-body x-panel-body-noheader']//div//div[2]//div[3]/button";

        if (selenium.isElementPresent(CHART_RADIO_BUTTON)) {
            selenium.click(CHART_RADIO_BUTTON);
            if (selenium.isElementPresent(CHART_SELECT_ALL)) {
                selenium.click(CHART_SELECT_ALL);
                if (selenium.isElementPresent(CHART_LAUNCH_BUTTON))
                    selenium.click(CHART_LAUNCH_BUTTON);
                selenium.waitForPageLoadingToComplete();
            }
        }
        selenium.setSpeed("0");
    }

    /**
     * Fred assi 20 08 2012
     * drilldown on single pie chart
     */
    public void drilldownOnSinglePieChart() throws PopUpException {
        logger.log(Level.INFO, " ********* : drilldownOnSinglePieChart()");
        selenium.mouseOver(drillablePieChartObjectId);
        selenium.click(drillablePieChartObjectId);
        selenium.waitForPageLoadingToComplete();
    }

    public void drilldownOnBarChartPortion(final String barPortionName) throws PopUpException {
        int index = getIndex(barPortionName, BAR_CHART_X_ASIX_ELEMENTS_XPATH);
        String textPath = "//div[@id='selenium_tag_baseWindow']//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']["
                + index + "]";
        selenium.mouseOver(textPath);
        selenium.click(textPath);
        selenium.waitForPageLoadingToComplete();

    }

}
