package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * DEFTFTS-2493
 * Automate US 942 - TCP Performance - Details Chart (uplink)
 * <p/>
 * DEFTFTS-15
 * Automation US 15: TCP Performance - Details Chart (Downlink)
 *
 * @author evyagrz
 */
public class TCPPerformanceChartTest extends SBWebDriverBaseUnitTest {

    private CommonDataType parameter = CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART;
    
    @Test
    public void testTCPPerformanceUplink() throws Exception {
        
        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);
        
        checkChartTitle();
        checkAxisLabelForUplink();
        checkLegendTitleForUplink();
    }

    @Test
    public void testTCPPerformanceDownlink() throws Exception {
        openSessionDetail(CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART, false, false);

        checkChartTitle();
        checkAxisLabelForDownlink();
        checkLegendTitleForDownlink();
    }

    private void checkLegendTitleForDownlink() {
        String LEGEND_UPLINK_TITLE = "//*[name()='svg']/*[name()='g']/*[name()='text']/*[name()='tspan' and contains(text(), 'TCP TPut DL (kbps)')]";
        WebElement legendTitle = driver.findElement(By.xpath(LEGEND_UPLINK_TITLE));

        assertEquals("Wrong TPut legend label for Downlink!", "TCP TPut DL (kbps)", legendTitle.getText());
    }

    private void checkAxisLabelForDownlink() {
        String AXIS_UPLINK_TITLE = "//*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'TPut DL(kbps)')]";
        WebElement axisTitle = driver.findElement(By.xpath(AXIS_UPLINK_TITLE));

        assertEquals("Wrong TPut axis label for Downlink!", "TPut DL(kbps)", axisTitle.getText());
    }

    private void checkLegendTitleForUplink() {
        String LEGEND_UPLINK_TITLE = "//*[name()='svg']/*[name()='g']/*[name()='text']/*[name()='tspan' and contains(text(), 'TCP TPut UL (kbps)')]";
        WebElement legendTitle = driver.findElement(By.xpath(LEGEND_UPLINK_TITLE));

        assertEquals("Wrong TPut legend label for Uplink!", "TCP TPut UL (kbps)", legendTitle.getText());
    }

    private void checkAxisLabelForUplink() {
        String AXIS_UPLINK_TITLE = "//*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'TPut UL(kbps)')]";
        WebElement axisTitle = driver.findElement(By.xpath(AXIS_UPLINK_TITLE));

        assertEquals("Wrong TPut axis label for Uplink!", "TPut UL(kbps)", axisTitle.getText());
    }

    private void checkChartTitle() {
    	String chartTitle = "//div[@id='BOUNDARY_SESSION_BROWSER']/div/div/div[2]//span[@class='highcharts-title']";

        WebElement title = driver.findElement(By.xpath(chartTitle));
        String text = title.getText();
        String imsi = getIMSIParameter(parameter);
        String chartName = "TCP Performance";

        assertTrue("TCP Performance chart title does not contain " + imsi, text.contains(imsi));
        assertTrue("TCP Performance chart title does not contain " + chartName, text.contains(chartName));
        
    }
}