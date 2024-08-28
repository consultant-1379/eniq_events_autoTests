package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * DEFTFTS-2494 Automate US 14 - Application Layer - Details Chart
 * 
 * @author evyagrz
 */
public class ApplicationLayerDetailTest extends SBWebDriverBaseUnitTest {

	CommonDataType parameter = CommonDataType.SB_IMSI_APPLICATION_SUM_CHART;

	String appLayerChartPath = "//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[3]";

	@Test
	public void testApplicationLayerChart() throws Exception {

		openSessionDetail(parameter, true, false);

		checkChartTitle();
		checkAxisLabels();
		checkChartLegend();
		checkPrint();
		checkExport();
	}

	private void checkExport() {
		String exportButton = appLayerChartPath
				+ "//*[name()='svg']/*[name()='rect' and contains(@title, 'Export to raster')]";
		List<WebElement> exportButtons = driver.findElements(By
				.xpath(exportButton));

		assertEquals("Missing export button", 1, exportButtons.size());
	}

	private void checkPrint() {
		String printButton = appLayerChartPath
				+ "//*[name()='svg']/*[name()='rect' and contains(@title, 'Print the chart')]";
		List<WebElement> printButtons = driver.findElements(By
				.xpath(printButton));

		assertEquals("Missing print button", 1, printButtons.size());
	}

	private void checkChartTitle() {
		String title = appLayerChartPath
				+ "//div[@class='highcharts-container']/span[@class='highcharts-title']";
		WebElement chartTitle = driver.findElement(By.xpath(title));
		String text = chartTitle.getText();

		String imsi = getIMSIParameter(parameter);
		String chartName = "Application Layer Performance";

		assertTrue(
				"Application Layer Performance chart title does not contain "
						+ imsi, text.contains(imsi));
		assertTrue(
				"Application Layer Performance chart title does not contain "
						+ chartName, text.contains(chartName));
	}

	private void checkAxisLabels() {
		String axis = appLayerChartPath
				+ "//*[name()='svg']/*[name()='text']/*[name()='tspan' and contains(text(), 'Data VoL')]";
		List<WebElement> axisLeftRight = driver.findElements(By.xpath(axis));

		assertEquals("Missing axis for the chart", 2, axisLeftRight.size());
		assertEquals("Wrong axis for DL", "Data VoL DL (Bytes)", axisLeftRight
				.get(0).getText());
		assertEquals("Wrong axis for UL", "Data VoL UL (Bytes)", axisLeftRight
				.get(1).getText());
	}

	private void checkChartLegend() {
		String legend = appLayerChartPath
				+ "//div/*[name()='svg']/*[name()='g' and @class='highcharts-legend']/*[name()='text']";
		List<WebElement> legends = driver.findElements(By.xpath(legend));

		assertFalse("Missing legends in chart", legends.isEmpty());
	}
}