package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis;

import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIAnalysisThresholdWidget;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIAnalysisWindow;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KPIBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KpiConstants;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.KpiAnalysisTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class KPIAnalysisMapTestGroup extends KPIBaseUnitTest {

	@Autowired
	private KpiAnalysisTab kpiAnalysisTab;

	private KPIAnalysisThresholdWidget thresholdWidget = new KPIAnalysisThresholdWidget();
	private KPIAnalysisWindow window = new KPIAnalysisWindow();
	// private String time = "Custom Time";
	private String controllerOption = "Network";
	private String kpiToSelect = "Service Request Failure Ratio";
	//private String controller = "operator_R:RNC09:RNC09,Ericsson,3G";
	private String controller = "smartone_R:RNC09:RNC09,Ericsson,3G";

	/**
	 * US: DEFTFTS-1888 verify that map has basic elements
	 */
	@Test
	public void testKpiMapBasicElementsArePresent_29_1()
			throws InterruptedException, ParseException {
		launchKPIMapTwoHours();

		assertTrue("Main KPI Map is not displayed",
				window.getElement(KpiConstants.KPI_MAP).isDisplayed());
		assertTrue("Scale line in KPI Map is not displayed",
				window.getElement(KpiConstants.KPI_MAP_SCALE).isDisplayed());
		assertTrue("The Kpi Thresholds element is not displayed", window
				.getElement(KpiConstants.KPI_THRESHOLDS).isDisplayed());

		// expand KPI map
		window.getElement(KpiConstants.KPI_MAP_EXPAND).click();

		checkMapHeader();
		checkMapOverview();
		checkMapFooter();
	}

	/**
	 * US: DEFTFTS-1888 verify that user can click on an RNC to see the cells
	 */
	@Test
	public void testKpiMapRNCDrillDown_29_2() throws InterruptedException,
			ParseException {
		// look for svg elements in the map
		launchKPIMapTwoHours();

		clickOnThresholds();

		List<WebElement> images = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='image']");

		assertFalse("There was no RNC available", images.isEmpty());

		images.get(0).click();

		window.waitForWindowToLoad("Loading Map ...");
		Thread.sleep(4000);

		List<WebElement> cells = driver
				.findElements(By
						.xpath(KpiConstants.KPI_MAP
								+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='circle']"));

		assertFalse("There are no cells shown when RNC was clicked",
				(cells.isEmpty()));
	}

	/**
	 * US: DEFTFTS-177 verify that user can click on an RNC to see the cells
	 * 
	 * For this test there should be more than one RNC shown on the map
	 */
	@Test
	public void testKpiMapClusteredRNC_29_3() throws InterruptedException,
			ParseException {
		// look for svg elements in the map
		launchKPIMapTwoHours();

		workAroundForShowElementOnMap();

		clickOnThresholds();

		zoomOutToClusteredRNC();

		assertTrue("There should be a clustered RNC shown", isClusteredRNC());

	}

	/**
	 * US: DEFTFTS-1888 verify that KPI map navigation works correctly
	 * 
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	@Test
	public void testKpiMapNavigation_29_5() throws InterruptedException,
			ParseException {
		launchKPIMapTwoHours();
		// Zoom in on the map
		window.getElement(KpiConstants.KPI_MAP_ZOOM_IN).click();
		window.getElement(KpiConstants.KPI_MAP_ZOOM_IN).click();
	
		// Zoom out on the map
		window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();
		window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();
	
		// Move map to left
		window.getElement(KpiConstants.KPI_MAP_ZOOM_LEFT).click();
		window.getElement(KpiConstants.KPI_MAP_ZOOM_LEFT).click();
	
		// Move map to right
		window.getElement(KpiConstants.KPI_MAP_ZOOM_RIGHT).click();
		window.getElement(KpiConstants.KPI_MAP_ZOOM_RIGHT).click();
	
		List<WebElement> images = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='image']");
	
		assertFalse("There was no RNC available", images.isEmpty());
	}

	/**
	 * US: DEFTFTS-1888 verify that map tool tip is correct over an RNC
	 */
	@Test
	public void testKpiMapRNCHoverShowsToolTip_29_11() throws InterruptedException,
			ParseException {
		launchKPIMapTwoHours();
	
		clickOnThresholds();
	
		List<WebElement> images = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='image']");
	
		assertFalse("There was no RNC available", images.isEmpty());
	
		for (WebElement image : images) {
			mouseOver(image);
			List<WebElement> popup = window
					.getMulipleElements(KpiConstants.KPI_MAP_HOVER_MSG + "//tr");
	
			if (!popup.isEmpty()) {
	
				String toolTipRNC = popup.get(0).getText();
				String toolTipKpi = popup.get(1).getText();
				String toolTipValue = popup.get(2).getText();
	
				assertTrue("RNC tool tip does not contain the RNC ID label",
						toolTipRNC.contains("RNC ID"));
				assertTrue("RNC tool tip does not contain the KPI label",
						toolTipKpi.contains("KPI"));
				assertTrue("RNC tool tip does not contain the Value label",
						toolTipValue.contains("Value"));
				assertTrue("RNC tool tip does not contain the KPI selected",
						toolTipKpi.contains(kpiToSelect));
			}
		}
	}

	@Test
	public void testKpiMapHoverShowsToolTipSingleCell_29_12() throws InterruptedException,
			ParseException {
		
		launchKPIMapForController(kpiToSelect);

		clickOnThresholds();

		List<WebElement> cells = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='circle']");

		assertFalse("There was no cells available", cells.isEmpty());

		String hoverStroke = "#00A4D9";

		WebElement currCell = cells.get(0);
		mouseOver(currCell);

		assertTrue("The hover stroke color is not as expected",
					getStrokeColor(currCell).equals(hoverStroke));
		WebElement popup = window
				.getElement(KpiConstants.KPI_MAP_HOVER_MSG);

		assertTrue("Cell Details not found in cell popup", popup.getText()
				.contains("Cell Details"));
		assertTrue("Selected KPI not found in cell popup", popup.getText()
				.contains(kpiToSelect));
	}

	/**
	 * US: DEFTFTS-177 see cell details when hovering over cell
	 */
	@Test
	public void testKpiMapHoverShowsToolTipMultiCell_29_12()
			throws InterruptedException, ParseException {
		String hoverStroke = "#00A4D9";
		String kpi = kpiToSelect;
		launchKPIMapForController(kpi);

		clickOnThresholds();
		
		window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();
		window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();

		List<WebElement> cells = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='circle']");

		assertFalse("No Cells found for this search", cells.isEmpty());

		for (WebElement currCell : cells) {
			mouseOver(currCell);

			if (cells.indexOf(currCell) == 0) {
				assertTrue("The hover stroke color is not as expected",
						getStrokeColor(currCell).equals(hoverStroke));
			}
			// compare colors between cell and tooltip
			// WebElement hoverCircle = currCell.findElement(By
			// .xpath("//*[name()='circle']"));
			//
			// assertTrue("Circle in hover should match the cell color",
			// getCellColor(hoverCircle)
			// .equals(getCellColor(currCell)));

			// Thread.sleep(500);
			
			WebElement popup = window
					.getElement(KpiConstants.KPI_MAP_HOVER_MSG);

			assertTrue("Cell Details not found in cell popup", popup.getText()
					.contains("Cell Details"));
			assertTrue("Selected KPI not found in cell popup", popup.getText()
					.contains(kpi));
		
			// stop after ten cells
			if (cells.indexOf(currCell) == 10) {
				break;
			}
		}
	}

	/**
	 * US: DEFTFTS-177 see popup when click a cell
	 */
	@Test
	public void testKpiMapCellClickShowsMenu_29_13() throws InterruptedException,
			ParseException {
		String kpi = "Service Request Failure Ratio";
		
		launchKPIMapForController(kpi);
		
		List<WebElement> cells = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='circle']");

		if (!cells.isEmpty()) {

			for (WebElement currentCell : cells) {

				if (isColocated(currentCell)) {

					currentCell.click();
					Thread.sleep(100);

					List<WebElement> cellsInMenu = driver
							.findElements(By
									.xpath("//div[@class='popupContent']//tr//*[name()='circle']"));

					List<WebElement> labelsInMenu = driver
							.findElements(By
									.xpath("//div[@class='popupContent']//tr//div//div[contains(@class,'Label')]"));

					for (int i = 0; i < labelsInMenu.size(); i++) {
						assertFalse("Label in menu should not be blank",
								labelsInMenu.get(i).getText().isEmpty());

						// assertTrue("Color in menu is not as expected",
						// isValidColor(getCellColor(cellsInMenu.get(i))));
					}
					break;
				} else {
					continue;
				}
			}
		}
	}

	private void zoomOutToClusteredRNC() throws InterruptedException {
		List<WebElement> images = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='image']");

		assertTrue("There should be more than one RNC on the map",
				images.size() > 1);

		if (images.size() > 1) {
			do {
				window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();
				window.waitForWindowToLoad("Loading Map ...");
			} while (!isClusteredRNC());
		}

	}

	private boolean isClusteredRNC() {
		List<WebElement> images = window
				.getMulipleElements(KpiConstants.KPI_MAP
						+ "//div/*[name()='svg']/*[name()='g']/*[name()='g']/*[name()='image']");

		return images.get(0).getAttribute("href").contains("clustered");

	}

	private void workAroundForShowElementOnMap() throws InterruptedException {
		// Temporary work around because elements dont show until a threshold is
		// clicked
		driver.findElement(
				By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div//div[@checked='on'][1]"))
				.click();
	}

	private void clickOnThresholds() throws InterruptedException {
		List<WebElement> buttons = driver
				.findElements(By
						.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[@checked]"));

		for (WebElement button : buttons) {
			boolean currThresholdOn = thresholdWidget.getThresholdState(
					buttons.indexOf(button) + 1, button);

			if (!currThresholdOn) {
				button.click();

				window.waitForWindowToLoad("Loading Map ...");
			}
		}
	}

	private boolean isColocated(WebElement currentCell) {
		mouseOver(currentCell);

		WebElement popup = window
				.getElement("//div[contains(@class, 'gwt-PopupPanel')]/div[@class='popupContent']");

		return popup.getText().contains("count");

	}

	private boolean isValidColor(String value) {
		List<String> validCellColors = Arrays.asList("#DD160D", "#E2E1E1");

		return validCellColors.contains(value);
	}

	private String getStrokeColor(WebElement webElement) {
		return webElement.getAttribute("stroke");
	}

	private String getCellColor(WebElement webElement) {
		return webElement.getAttribute("fill");
	}

	private void launchKPIMapForController(String kpi) throws InterruptedException,
			ParseException {
		
		kpiAnalysisTab.launchKPIWindowWithCustomDate("2 hours",
				null, null,
				"Controller", controller, 1, kpi);
		window.getElement(KpiConstants.KPI_MAP_EXPAND).click();
		Thread.sleep(1000);
		window.getElement(KpiConstants.KPI_MAP_ZOOM_OUT).click();
		window.waitForWindowToLoad("Loading Map ...");

	}

	private void launchKPIMapTwoHours() throws InterruptedException,
			ParseException {

		kpiAnalysisTab.launchKPIWindowWithCustomDate("2 hours",
				null, null,
				controllerOption, "1", 1, kpiToSelect);
		window.getElement(KpiConstants.KPI_MAP_EXPAND).click();
		window.waitForWindowToLoad("Loading Map ...");
	}

	private void checkMapOverview() {
		String overviewStyle = window.getElement(KpiConstants.KPI_MAP_OVERVIEW)
				.getAttribute("style");
		assertTrue("The map overview should not display by default",
				overviewStyle.contains("display: none"));

		window.getElement(KpiConstants.KPI_MAP_OVERVIEW_MAXIMIZE_BUTTON)
				.click();
		overviewStyle = window.getElement(KpiConstants.KPI_MAP_OVERVIEW)
				.getAttribute("style");
		assertTrue(
				"The map overview should display after maximumize is clicked",
				overviewStyle.isEmpty());

		window.getElement(KpiConstants.KPI_MAP_OVERVIEW_MINIMIZE_BUTTON)
				.click();
		overviewStyle = window.getElement(KpiConstants.KPI_MAP_OVERVIEW)
				.getAttribute("style");
		assertTrue(
				"The map overview should not display after minimumize is clicked",
				overviewStyle.contains("display: none"));
	}

	private void checkMapHeader() {
		String mapHeader = window.getElement(KpiConstants.KPI_MAP_HEADER)
				.getText();
		assertTrue("Map header doesn't contain the controller option of "
				+ controllerOption, mapHeader.contains(controllerOption));

		assertTrue("Map header doesn't match kpi selected",
				mapHeader.contains(kpiToSelect));
	}

	private void checkMapFooter() {
		List<WebElement> postionTable = window
				.getMulipleElements(KpiConstants.KPI_MAP_POSITION);

		assertTrue("Map footer should contain Longitude", postionTable.get(0)
				.getText().contains("Longitude"));
		assertFalse("Map footer - Longitude value is null", postionTable.get(1)
				.getText().isEmpty());
		assertTrue("Map footer should contain Latitude", postionTable.get(2)
				.getText().contains("Latitude"));
		assertFalse("Map footer - Latitude value is null", postionTable.get(3)
				.getText().isEmpty());
	}
}
