package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.util.List;

/**
 * Automate US 8 - Application Layer - Summary
 * DEFTFTS-2496
 *
 * @author evyagrz
 */
public class ApplicationLayerSummaryTest extends SBWebDriverBaseUnitTest {

    String APPLICATION_LAYER_SUMMARY_PANE = "//div[@id='SESSION_BROWSER_TAB']/div/div/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[2]/div/div[1]";
    String APPLICATION_LAYER_CHART_TITLE = APPLICATION_LAYER_SUMMARY_PANE + "/div[1]/div/div/table/tbody/tr/td[2]/div/div";
    CommonDataType parameter = CommonDataType.SB_IMSI_APPLICATION_SUM_CHART;
    
    @Test
    public void testApplicationLayerChart() throws InterruptedException, ParseException {
    	openSessionSummary(CommonDataType.SB_IMSI_APPLICATION_SUM_CHART, true, false);
        
        waitUntilTCPPerformanceIsLoaded();
        checkApplicationLayerTitle();
        checkChartAxisNames();
        checkChartSeries();
    }

    private void checkApplicationLayerTitle() {
	    WebElement element = driver.findElement(By.xpath(APPLICATION_LAYER_CHART_TITLE));
	    String title = element.getText();
	    String imsi = getIMSIParameter(parameter);
	    	    
	    assertTrue("Application layer title is not correct, missing IMSI!", title.contains("Application Layer Performance, " + imsi));
	}

	private void checkChartSeries() {
        String seriesList = APPLICATION_LAYER_SUMMARY_PANE + "//div/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-series')]/*[name()='g']";
        List<WebElement> elements = driver.findElements(By.xpath(seriesList));

        assertFalse("Missing chart series!", elements.isEmpty());
    }

    private void checkChartAxisNames() {
        String chartTitle = "//div/*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'Application Usage')]";
        String leftAxisTitle = "//div/*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'Uplink')]";
        String rightAxisTitle = "//div/*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'Downlink')]";

        WebElement title = driver.findElement(By.xpath(chartTitle));
        WebElement leftAxis = driver.findElement(By.xpath(leftAxisTitle));
        WebElement rightAxis = driver.findElement(By.xpath(rightAxisTitle));

        assertEquals("Application Layer chart title is wrong!", "Application Usage", title.getText());
        assertEquals("Application Layer chart left Axis title is wrong!", "Uplink", leftAxis.getText());
        assertEquals("Application Layer chart right Axis title is wrong!", "Downlink", rightAxis.getText());
    }
}