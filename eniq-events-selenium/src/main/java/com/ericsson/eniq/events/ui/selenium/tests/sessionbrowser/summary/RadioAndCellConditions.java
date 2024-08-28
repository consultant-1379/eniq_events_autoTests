package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.summary;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RadioAndCellConditions extends SBWebDriverBaseUnitTest {

	@Autowired
	private SessionBrowserTab sessionbrowserTab;

	String rfChartPath = "//div[@id='SESSION_BROWSER_TAB']/div/div/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[1]";
	
	/**
	 * US: DEFTFTS-7 19.1: Verify in Summary View for Radio and Cell Conditions
	 * chart is displayed with correct title labels
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	@Ignore
	public void testRadioAndCellConditionChartWithCorrectTitleLabels_19_1()
			throws ParseException, InterruptedException {

		CommonDataType parameter = CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART;
		String chartName = "Radio and Cell Load Conditions";

		openSessionSummary(CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART, true,
				false);

		// Action 3
		final String[] yAxisLabels = new String[] { "Poor Coverage",
				"Poor Signal Quality", "Poor Coverage", "Good Signal Quality",
				"Good Coverage", "Poor Signal Quality", "Good Coverage",
				"Good Signal Quality" };

		final String yAxisLabelsXPath = Constants.SUMMARY_VISITED_CELL_GRAPH_Y_AXIS
				+ "/*[name()='text']/*[name()='tspan']";
		final List<WebElement> yAxisLabelElements = driver.findElements(By
				.xpath(yAxisLabelsXPath));

		int index = 0;
		for (final WebElement yAxisLabelElement : yAxisLabelElements) {
			assertEquals("Y Axis Label does not match expected value",
					yAxisLabels[index], yAxisLabelElement.getText());
			index++;
		}

		// Action 4
		final String[] xAxisLabels = new String[] { "Average RSCP =",
				"Average Ec/No =", "RSCP Threshold =", "Ec/No Threshold =",
				"Total Number of Measurement Samples =" };

		index = 0;
		WebElement xAxisLabelElement = driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE
						+ "/tbody/tr/td"));

		assertEquals("X Axis Label " + xAxisLabels[index] + " does not match",
				xAxisLabels[index], xAxisLabelElement.getText().trim());
		index++;

		xAxisLabelElement = driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE
						+ "/tbody/tr/td[3]"));
		assertEquals("X Axis Label " + xAxisLabels[index] + " does not match",
				xAxisLabels[index], xAxisLabelElement.getText().trim());
		index++;

		xAxisLabelElement = driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE
						+ "/tbody/tr[2]/td"));
		assertEquals("X Axis Label " + xAxisLabels[index] + " does not match",
				xAxisLabels[index], xAxisLabelElement.getText().trim());
		index++;

		xAxisLabelElement = driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE
						+ "/tbody/tr[2]/td[3]"));
		assertEquals("X Axis Label " + xAxisLabels[index] + " does not match",
				xAxisLabels[index], xAxisLabelElement.getText().trim());
		index++;

		// Action 6
		xAxisLabelElement = driver.findElement(By
				.xpath(Constants.SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE
						+ "/tbody/tr[3]/td"));
		assertTrue("X Axis Label '" + xAxisLabels[index] + "' does not match",
				xAxisLabelElement.getText().contains(xAxisLabels[index]));
		index++;

		// Action 5
		final String topLeftTitleXPath = Constants.SUMMARY_VISITED_CELL_GRAPH_TOP_LEFT_TITLE;
		final WebElement topLeftTitleElement = driver.findElement(By
				.xpath(topLeftTitleXPath));

		String imsi = getIMSIParameter(parameter);

		assertTrue("Title on Radio Cell Conditions chart does not contain: "
				+ chartName, topLeftTitleElement.getText().contains(chartName));
		assertTrue("Title on Radio Cell Conditions chart does not contain: "
				+ imsi, topLeftTitleElement.getText().contains(imsi));
	}

	/**
	 * US: DEFTFTS-7 19.3: Verify Export functionality in Summary View for Radio
	 * and Cell Conditions chart
	 * 
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testExportToRasherOrVectorImage_19_3() throws ParseException,
			InterruptedException {
		String exportTitle = "Export to raster or vector image";
		
		openSessionSummary(CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART, true,
				false);

		String exportButtonPath = rfChartPath + "//*[name()='rect' and @id='exportButton']";
		WebElement exportButton = driver.findElement(By
				.xpath(exportButtonPath));
		
		assertEquals("Title attribute of export element does not match",
				exportTitle, exportButton.getAttribute("title"));

		exportButton.click();
		String exportMenuPath = rfChartPath + "//div[@class='highcharts-export-menu']";
		WebElement exportMenuElement = driver
				.findElement(By.xpath(exportMenuPath));

		exportMenuElement = driver
				.findElement(By.xpath(exportMenuPath));
		assertTrue("Export menu should display after click", exportMenuElement
				.getAttribute("style").contains("display: block;"));
	}

	/**
	 * US: DEFTFTS-7 19.4: Verify ToolTip functionality in Summary View for
	 * Radio and Cell Conditions chart
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testToolTipInSummaryViewForRadioAndCellConditionsChart_19_4()
			throws ParseException, InterruptedException {
		openSessionSummary(CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART, true,
				false);
		
		String path = rfChartPath
				+ "//*[name()='g' and @class='highcharts-tracker']";
		WebElement groupElement = driver.findElement(By.xpath(path));

		List<WebElement> rectElements = groupElement.findElements(By
				.cssSelector("rect"));
				
		String[][] tooltipContents = new String [][] { 
				{"Poor Coverage,Poor Signal Quality", "Samples % = ", "Number of Samples = "},
                {"Poor Coverage,Good Signal Quality", "Samples % = ", "Number of Samples = "},
                {"Good Coverage,Poor Signal Quality", "Samples % = ", "Number of Samples = "},
                {"Good Coverage,Good Signal Quality", "Samples % = ", "Number of Samples = "}};

		for(int i =0; i < rectElements.size(); i++){
			WebElement rectElement = rectElements.get(i);
			int height = Integer.parseInt(rectElement.getAttribute("height"));
			
			//test will only work on larger bars
			if(height > 10){
				sessionbrowserTab.mouseOver(rectElement);
				verifyTooltipContents(tooltipContents[i]);
			}
		}
	}

	/**
	 * DEFTFTS-10 Visited Cell - Summary at Cell Level
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Test
	@Ignore
	public void testVisitedCellRFConditionsTable_19_6()
			throws InterruptedException, PopUpException, SQLException,
			ParseException {
		openSessionSummary(CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART, true,
				false);

		Thread.sleep(2000);
		final List<WebElement> visitedCellHeadings = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_HEADERS));
		final List<String> expectedValues = Arrays.asList("Controller", "Cell",
				"First Visited", "Duration (min)",
				"Samples per Radio Condition");

		for (int i = 0; i < visitedCellHeadings.size(); i++) {
			final String text = visitedCellHeadings.get(i).getText().trim();
			assertTrue("Value: " + text + " does not match expected field: "
					+ expectedValues.get(i), expectedValues.get(i).equals(text));
		}

		checkSortOrder();

		final List<WebElement> cells = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[2]/div/div"));
		for (final WebElement e : cells) {
			assertTrue(
					"Incorrect tool tip for Cell column in Visited Cell table",
					e.getAttribute("title").contains("CID"));
		}

		final List<WebElement> firstVisited = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[3]/div/div"));
		final String expectedText = "Date/time when cell was first encountered during selected time period";
		for (final WebElement e : firstVisited) {
			assertTrue(
					"Incorrect tool tip for First Visited column in Visited Cell table",
					e.getAttribute("title").equals(expectedText));
		}

	}

	/**
	 * DEFTFTS-1906 - RF Conditions Summary Relative to RF Conditions Visited
	 * Cell
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Test
	@Ignore
	public void testVisitedCellRFConditionsCompareValues_19_6()
			throws InterruptedException, PopUpException, SQLException,
			ParseException {
		openSessionSummary(CommonDataType.SB_IMSI_FOR_CELL_VISITED_CHART, true,
				false);

		int totalPoorPoor = 0;
		int totalPoorGood = 0;
		int totalGoodPoor = 0;
		int totalGoodGood = 0;

		final List<WebElement> poorCoveragePoorSignal = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[5]/div/div[1]"));
		final List<WebElement> poorCoverageGoodSignal = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[5]/div/div[2]"));
		final List<WebElement> goodCoveragePoorSignal = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[5]/div/div[3]"));
		final List<WebElement> goodCoverageGoodSignal = driver.findElements(By
				.xpath(Constants.SUMMARY_VISITED_CELL_TABLE
						+ "/tr/td[5]/div/div[4]"));

		for (int i = 0; i < poorCoveragePoorSignal.size(); i++) {
			totalPoorPoor += Integer.parseInt(poorCoveragePoorSignal.get(i)
					.getText().trim());
			totalPoorGood += Integer.parseInt(poorCoverageGoodSignal.get(i)
					.getText().trim());
			totalGoodPoor += Integer.parseInt(goodCoveragePoorSignal.get(i)
					.getText().trim());
			totalGoodGood += Integer.parseInt(goodCoverageGoodSignal.get(i)
					.getText().trim());
		}
		final int totalSamplesInVisitedCell = totalPoorPoor + totalPoorGood
				+ totalGoodPoor + totalGoodGood;

		final String totalNoMeasurementSamples = driver
				.findElement(
						By.xpath("//div[@id='SESSION_BROWSER_TAB']/div/div/div[2]/div/div/div[2]/div/table//div[2]/div/table//tr[3]//span"))
				.getText();

		assertTrue(
				"Total number of measurement samples do not match",
				Integer.parseInt(totalNoMeasurementSamples) == totalSamplesInVisitedCell);
	}

	private void checkSortOrder() throws InterruptedException {

		for (int x = 1; x < 5; x++) {
			List<WebElement> table = driver.findElements(By
					.xpath(Constants.SUMMARY_VISITED_CELL_TABLE + "/tr/td[" + x
							+ "]"));
			final List<Comparable> temp = new ArrayList<Comparable>();

			for (final WebElement e : table) {
				if (x == 4)
					temp.add(Double.parseDouble(e.getText()));
				else
					temp.add(e.getText());
			}

			Collections.sort(temp);

			driver.findElement(
					By.xpath(Constants.SUMMARY_VISITED_CELL_HEADERS + "[" + x
							+ "]")).click();
			Thread.sleep(1000);

			table = driver.findElements(By
					.xpath(Constants.SUMMARY_VISITED_CELL_TABLE + "/tr/td[" + x
							+ "]"));
			for (int i = 0; i < table.size(); i++) {
				if (x == 4) {
					double uiValue = Double.parseDouble(table.get(i).getText());

					assertTrue("Value: " + uiValue
							+ " does not match expected field: " + temp.get(i),
							temp.get(i).equals(uiValue));
				} else {
					assertTrue(
							"Value: " + table.get(i).getText()
									+ " does not match expected field: "
									+ temp.get(i),
							temp.get(i).toString()
									.equals(table.get(i).getText()));
				}
			}
		}
	}

	private void verifyTooltipContents(String[] tooltipContents) {
		List<WebElement> tooltipContentElements = getHoverMessages();
		
		for (int i = 0; i < tooltipContentElements.size(); i++) {
			String expected = tooltipContents[i];
			String actual = tooltipContentElements.get(i).getText();
			
			assertTrue("Tooltip content does not contain : "
					+ expected, actual.contains(expected));
		}
	}

	private List<WebElement> getHoverMessages() {
		return driver.findElements(By.xpath(rfChartPath + "//*[name()='svg']/*[@class='highcharts-tooltip']//*[name()='tspan']"));
	}
}
