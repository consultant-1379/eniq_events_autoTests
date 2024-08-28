package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.util.List;

/**
 * Covers both DEFTFTS-1621 & DEFTFTS-9
 */
public class TCPPerformanceSummaryTest extends SBWebDriverBaseUnitTest {

    /* DEFTFTS-1621 TCP Performance Summary - Supporting Multiple Hosts with the same Values */
    @Test
    public void testTCPPerformanceSummaryMultipleHosts()
            throws InterruptedException, ParseException {
        CommonDataType parameter = CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART;
    	
    	openSessionSummary(parameter, true, false);

        waitUntilTCPPerformanceIsLoaded();
        verifyThroughputTable();
        verifyPacketLossTable();
        checkGNToServerMaxValues();
        checkUEToServerMaxValues();
        checkServerSlowestTransferTimes();
    }

    // Automate US 9 - TCP Performance - Summary
    @Test
    public void testTCPPerformanceSummaryLatency() throws InterruptedException, ParseException {
        CommonDataType parameter = CommonDataType.SB_IMSI_TCP_PERFORMANCE_CHART;
    	
    	openSessionSummary(parameter, true, false);
        waitUntilTCPPerformanceIsLoaded();
        verifyLatencyChart();
    }
    
    private void verifyLatencyChart() {
        String latencyTitle = "//div/div[2]/div/table/tbody/tr/td[2]/div/div[2]/div/div/div[2]/div/h1[2]";
        
        String peakChartEuServer = "//div/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-series')]/*[count(*)=3]/*[name()='rect' and contains(@fill, '#a66c99') and @height>0]";
        String peakChartEuGnGnServer = "//div/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-series')]/*[count(*)=3]/*[name()='rect' and contains(@fill, '#c19cbb') and @height>0 and @width=11]";

        String averageChartEuServer = "//div/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-series')]/*[count(*)=3]/*[name()='rect' and contains(@fill, '#5b6592') and @height>0]";
        String averageChartEuGnGnServer = "//div/*[name()='svg']/*[name()='g' and contains(@class,'highcharts-series')]/*[count(*)=3]/*[name()='rect' and contains(@fill, '#8d92b4') and @height>0 and @width=11]";

        // Check chart title
        WebElement latencyTitleElement = driver.findElement(By.xpath(latencyTitle));

        // Check that Peak bars are there with proper color
        WebElement peakUeServer = driver.findElement(By.xpath(peakChartEuServer));
        List<WebElement> peakEuGnGnServer = driver.findElements(By.xpath(peakChartEuGnGnServer));

        // Check that Average bars are there with proper color
        WebElement averageUeServer = driver.findElement(By.xpath(averageChartEuServer));
        List<WebElement> averageEuGnGnServer = driver.findElements(By.xpath(averageChartEuGnGnServer));

        assertEquals("Latency chart title is not correct!", "Latency (ms)", latencyTitleElement.getText());
        assertEquals("Chart color for peaks UE->Server values not correct!", "#a66c99", peakUeServer.getAttribute("fill"));
        assertEquals("Chart color for peaks UE->Gn Evalues not correct!", "#c19cbb", peakEuGnGnServer.get(0).getAttribute("fill"));
        assertEquals("Chart color for peaks Gn->Server values not correct!", "#c19cbb", peakEuGnGnServer.get(1).getAttribute("fill"));

        assertEquals("Chart color for average UE->Server values not correct!", "#5b6592", averageUeServer.getAttribute("fill"));
        assertEquals("Chart color for average UE->Gn Evalues not correct!", "#8d92b4", averageEuGnGnServer.get(0).getAttribute("fill"));
        assertEquals("Chart color for average Gn->Server values not correct!", "#8d92b4", averageEuGnGnServer.get(1).getAttribute("fill"));
    }

    private void verifyThroughputTable() {
        List<WebElement> throughputTable = driver.findElements(By
                .xpath(Constants.TCP_PERFORMANCE_THROUGHPUT_TABLE
                        + "/table/tbody/tr/td"));

        for (int i = 0; i < throughputTable.size(); i++) {
            assertTrue("The value(s) in throughtput table is incorrect",
                    checkDashOrData(throughputTable.get(i).getText()));
        }
    }

    private void verifyPacketLossTable() {
        List<WebElement> packetLossTable = driver.findElements(By
                .xpath(Constants.TCP_PERFORMANCE_PACKET_LOSS_TABLE
                        + "/table/tbody/tr/td"));
        for (int i = 0; i < packetLossTable.size(); i++) {
            assertTrue("The value(s) in packet loss table is incorrect",
                    checkDashOrData(packetLossTable.get(i).getText()));
        }
    }

    private boolean checkDashOrData(String text) {
        if (text == "-" || !text.isEmpty()) {
            return true;
        }
        return false;
    }

    private void checkGNToServerMaxValues() {
        List<WebElement> gnToSerMaxValues = driver.findElements(By
                .xpath(Constants.TCP_PERFORMANCE_PACKET_LOSS_TABLE
                        + "/table/tbody/tr/td[2]"));

        for (int i = 0; i < gnToSerMaxValues.size(); i++) {
            assertTrue("GN to server max value is zero", gnToSerMaxValues
                    .get(i).getText() != "0");
        }
    }

    private void checkUEToServerMaxValues() {
        List<WebElement> ueToSerMaxValues = driver.findElements(By
                .xpath(Constants.TCP_PERFORMANCE_PACKET_LOSS_TABLE
                        + "/table/tbody/tr/td[3]"));

        for (int i = 0; i < ueToSerMaxValues.size(); i++) {
            assertTrue("UE to server max value is zero", ueToSerMaxValues
                    .get(i).getText() != "0");
        }
    }

    private void checkServerSlowestTransferTimes() {
        String slowestTransferTime = driver.findElement(By
                .xpath(Constants.TCP_PERFORMANCE_PANE + "/div[2]/div/h2/span")).getText();
        assertTrue("Server with slowest transfer times is incorrect",
                checkDashOrData(slowestTransferTime));
    }

}
