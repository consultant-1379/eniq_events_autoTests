package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoMapException;
import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.*;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.KpiAnalysisTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : edarbla
 * @since : November 2012
 */
public class KPIAnalysisThresholdTestGroup extends KPIBaseUnitTest {

	@Autowired
	private KpiAnalysisTab kpiAnalysisTab;

	private KPIAnalysisWindow window = new KPIAnalysisWindow();

	private KPIAnalysisThresholdWindow thresholdWindow = new KPIAnalysisThresholdWindow();

	private KPIAnalysisThresholdWidget thresholdWidget = new KPIAnalysisThresholdWidget();

	public final static String[] defaultPercentagesForSRFR = { "100", "30",
			"20", "10", "0" };

	public final static String[] errorMessages = { "Not a valid number",
			"Threshold Value must be between 0% and 100%" };

	public final static String[] unitsMessages = { "Units in %",
			"Units in kbps", "Units in ms" };

	public final static String[] badKpis = { "Service Request Failure Ratio",
			"Paging Failure Ratio", "PDP Context Cut Off",
			"Packet Loss Terminal", "Packet Loss Network" };

	public final static String[] goodKpis = { "RAU Success Rate",
			"ISRAU Success Rate", "PDP Context Activation Success Rate",
			"Detach Success Rate", "Attach Success Rate" };

	public final static int WIDGET_PERCENTAGE_ONE_MS = 3,
			WIDGET_PERCENTAGE_TWO_MS = 5, WIDGET_PERCENTAGE_THREE_MS = 7;

	// In the threshold widget, these variables refer to the percentages, from
	// top-to-bottom order.
	public final static int WIDGET_PERCENTAGE_ONE = 2,
			WIDGET_PERCENTAGE_TWO = 4, WIDGET_PERCENTAGE_THREE = 6,
			WIDGET_PERCENTAGE_FOUR = 8, WIDGET_PERCENTAGE_FIVE = 10;

	// In the threshold window, these variables refer to the percentages, from
	// left-to-right order.
	public final static int WINDOW_PERCENTAGE_ONE = 1,
			WINDOW_PERCENTAGE_FIVE = 9;

	public final static int TEXT_FIELD_TWO = 3;

	public final static int TEXT_FIELD_THREE = 5;

	public final static int TEXT_FIELD_FOUR = 7;

	public final static int PERCENTAGE_KPI = 1;

	public final static int MILLISECOND_KPI = 2;

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.21
	 * 
	 * Check that current KPI has correct title in threshold window (not really mentioned in VS doc)
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkThreadholdConfigTitle_29_21() throws InterruptedException, ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        String kpi = window.getSelectedKpi();
		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);
		String title = thresholdWindow.getTitle();

		assertTrue("Selected KPI does not match threshold window title",
				title.contains(kpi));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.21
	 * 
	 * Verify for KPI Threshold Filtering in map view that it’s possible to filter out all data by 
	 * selecting grey/yellow/orange and red part of KPI Threshold legend
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkColoursAreClickable_29_21() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Downlink Throughput";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }
        
        WebElement e = driver
				.findElement(By
						.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[3]"));

		WebElement offsetElement = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].offsetParent;", e);
		String innerHTML = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", offsetElement);
		String beforeClick = thresholdWidget.checkInnerHTML(innerHTML);

		driver.findElement(
				By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[2]"))
				.click();

		Thread.sleep(2000);

		WebElement offsetElement2 = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].offsetParent;", e);
		String innerHTML2 = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", offsetElement2);
		String afterClick = thresholdWidget.checkInnerHTML(innerHTML2);


		window.waitForWindowToLoad("Loading Map ...");
		assertFalse("Button not clicked!", beforeClick.equals(afterClick));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.21
	 * 
	 * Verify for KPI Threshold Filtering in map view that it’s possible to filter out all data by 
	 * selecting grey/yellow/orange and red part of KPI Threshold legend
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testWidgetButtonsPersist_29_21() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Downlink Throughput";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        String firstbutton = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[2]";
		WebElement button1 = driver.findElement(By.xpath(firstbutton));
		button1.click();
		Thread.sleep(2000);

		WebElement offsetElement = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].offsetParent;", button1);
		String innerHTML = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", offsetElement);
		String checkedStatusBefore = thresholdWidget.checkInnerHTML(innerHTML);

		Thread.sleep(2000);

		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown("Paging Failure Ratio", options);

		Thread.sleep(2000);

		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options2 = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown(kpiToSelect, options2);

		Thread.sleep(2000);

		WebElement button2 = driver.findElement(By.xpath(firstbutton));
		WebElement offsetElement2 = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].offsetParent;", button2);
		String innerHTML2 = (String) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].innerHTML;", offsetElement2);
		String checkedStatusAfter = thresholdWidget.checkInnerHTML(innerHTML2);

		assertTrue("Button state not persisted",
				checkedStatusBefore.equals(checkedStatusAfter));
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.21
	 * Verify for KPI Threshold Filtering in map view that it’s possible to filter out all data by 
	 * selecting grey/yellow/orange and red part of KPI Threshold legend
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkGoodAndBadKpiColourOrder_29_21() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        // Open Threshold settings
		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);
		List<String> windowColours, reverseWindowColours;
	
		// Select a Good KPI
		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown(goodKpis[1], options); // Bad
	
		Thread.sleep(1000);
	
		// Get order of colours for Good KPI
		windowColours = thresholdWindow.getColouredOrder();
	
		// Select a Bad KPI
		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options2 = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown(badKpis[2], options2); // Bad
	
		Thread.sleep(1000);
	
		// Get order of colours for Bad KPI
		reverseWindowColours = thresholdWindow.getColouredOrder();
	
		// Reverse order
		Collections.reverse(reverseWindowColours);
	
		// Verify colours match
		for (int i = 0; i < windowColours.size(); i++) {
			assertTrue(windowColours.get(i).equals(reverseWindowColours.get(i)));
		}
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Check that default percentages on the widget are correct
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkDefaultWidgetPercentages_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        List<String> percentages = thresholdWidget
				.getThresholdWidgetPercentages(PERCENTAGE_KPI,
						WIDGET_PERCENTAGE_ONE, WIDGET_PERCENTAGE_TWO,
						WIDGET_PERCENTAGE_THREE, WIDGET_PERCENTAGE_FOUR,
						WIDGET_PERCENTAGE_FIVE);
		for (int i = 0; i < percentages.size(); i++) {
			assertTrue("Incorrect Value for default percentages",
					percentages.get(i).equals(defaultPercentagesForSRFR[i]));
		}
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void comparePercentages_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        List<String> widgetPercents;
		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);

		thresholdWindow.setText(TEXT_FIELD_TWO, "15");
		thresholdWindow.setText(TEXT_FIELD_THREE, "25");
		thresholdWindow.setText(TEXT_FIELD_FOUR, "55");
		thresholdWindow.update();

		widgetPercents = thresholdWidget.getThresholdWidgetPercentages(
				PERCENTAGE_KPI, WIDGET_PERCENTAGE_ONE, WIDGET_PERCENTAGE_TWO,
				WIDGET_PERCENTAGE_THREE, WIDGET_PERCENTAGE_FOUR,
				WIDGET_PERCENTAGE_FIVE);

		assertTrue("Mismatch in first widget value", widgetPercents.get(0)
				.equals("100"));
		assertTrue("Mismatch in second widget value", widgetPercents.get(1)
				.equals("55"));
		assertTrue("Mismatch in third widget value", widgetPercents.get(2)
				.equals("25"));
		assertTrue("Mismatch in fourth widget value", widgetPercents.get(3)
				.equals("15"));
		assertTrue("Mismatch in fifth widget value", widgetPercents.get(4)
				.equals("0"));
		
		setPercentageKpiDefaults();
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Test that characters cannot be input in threshold config window
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkErrorInvalidCharacter_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);
		thresholdWindow.setText(TEXT_FIELD_TWO, "a");

		String errorMsg = "//div[contains(@class,'gwt-Label') and contains(text(), 'Not a valid number')]";
		WebElement errorMessage = driver.findElement(By.xpath(errorMsg));

		assertTrue("Incorrect error message",
				errorMessage.getText().equals(errorMessages[0]));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Test that null cannot be input in threshold config window
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkErrorEmptyValue_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);
		thresholdWindow.setText(TEXT_FIELD_TWO, " ");

		String errorMsg = "//div[contains(@class,'gwt-Label') and contains(text(), 'Not a valid number')]";
		WebElement errorMessage = driver.findElement(By.xpath(errorMsg));

		assertTrue("Incorrect error message",
				errorMessage.getText().equals(errorMessages[0]));

	}
	
	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Compares values of KPI Thresholds and checks error message on UI
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkErrorValuesPercentageKpi_29_22()
			throws InterruptedException, ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        List<String> percentages = thresholdWidget
				.getThresholdWidgetPercentages(PERCENTAGE_KPI,
						WIDGET_PERCENTAGE_FOUR, WIDGET_PERCENTAGE_THREE,
						WIDGET_PERCENTAGE_TWO);
		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);

		// Set standard values in text boxes at either end
		thresholdWindow.setText(TEXT_FIELD_TWO, percentages.get(1));
		thresholdWindow.update();

		List<String> percentages2 = thresholdWidget
				.getThresholdWidgetPercentages(PERCENTAGE_KPI,
						WIDGET_PERCENTAGE_FOUR, WIDGET_PERCENTAGE_THREE,
						WIDGET_PERCENTAGE_TWO);

		String errorMsg = "//div[contains(@class,'gwt-Label') and contains(text(), 'Threshold must be less than')]";
		WebElement errorMessage = driver.findElement(By.xpath(errorMsg));

		assertTrue(
				"Incorrect error message",
				errorMessage.getText().equals(
						"Threshold must be less than " + percentages.get(1)));
		assertTrue("Incorrect values were updated",
				percentages.containsAll(percentages2));
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Compares values of KPI Thresholds and checks error message on UI
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkErrorValuesMillisecondKpi_29_22()
			throws InterruptedException, ParseException {
		String kpiToSelect = "RTT Terminal";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        List<String> percentages = thresholdWidget
				.getThresholdWidgetPercentages(MILLISECOND_KPI,
						WIDGET_PERCENTAGE_THREE_MS, WIDGET_PERCENTAGE_TWO_MS,
						WIDGET_PERCENTAGE_ONE_MS);
		thresholdWidget.openThresholdConfigurationWindow(MILLISECOND_KPI);
		Thread.sleep(2000);

		// Set standard values in text boxes at either end
		thresholdWindow.setText(TEXT_FIELD_TWO, percentages.get(1));
		thresholdWindow.update();
		List<String> percentages2 = thresholdWidget
				.getThresholdWidgetPercentages(MILLISECOND_KPI,
						WIDGET_PERCENTAGE_THREE_MS, WIDGET_PERCENTAGE_TWO_MS,
						WIDGET_PERCENTAGE_ONE_MS);

		String errorMsg = "//div[contains(@class,'gwt-Label') and contains(text(), 'Threshold must be less than')]";
		WebElement errorMessage = driver.findElement(By.xpath(errorMsg));

		assertTrue(
				"Incorrect error message",
				errorMessage.getText().equals(
						"Threshold must be less than " + percentages.get(1)));
		assertTrue("Incorrect values were updated",
				percentages.containsAll(percentages2));
	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Check label in threshold config box in correct for percentage kpi
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkCorrectUnitsPercentages_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown(badKpis[0], options);

		Thread.sleep(2000);

		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);

		assertTrue("Units do not match selected KPI", thresholdWindow
				.getUnits().equals(unitsMessages[0]));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Check label in threshold config box in correct for kbps kpi
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkCorrectUnitsKbps_29_22() throws InterruptedException,
			ParseException {

		String kpiToSelect = "Uplink Throughput";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        thresholdWidget.openThresholdConfigurationWindow(MILLISECOND_KPI);

		assertTrue("Units do not match selected KPI", thresholdWindow
				.getUnits().equals(unitsMessages[1]));

		thresholdWindow.cancel();

		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options2 = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown("Downlink Throughput", options2);

		Thread.sleep(2000);

		thresholdWidget.openThresholdConfigurationWindow(MILLISECOND_KPI);

		assertTrue("Units do not match selected KPI", thresholdWindow
				.getUnits().equals(unitsMessages[1]));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Check label in threshold config box in correct for millisecond kpi
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkCorrectUnitsMilliseconds_29_22() throws InterruptedException,
			ParseException {

		String kpiToSelect = "RTT Terminal";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        thresholdWidget.openThresholdConfigurationWindow(MILLISECOND_KPI);

		assertTrue("Units do not match selected KPI", thresholdWindow
				.getUnits().equals(unitsMessages[2]));

		thresholdWindow.cancel();

		window.getElement(KpiConstants.KPI_DROPDOWN_WINDOW_HEADER).click();
		List<WebElement> options2 = window
				.getMulipleElements(KpiConstants.KPI_OPTION_LIST);
		selectKPIFromDropdown("RTT Server", options2);

		Thread.sleep(2000);

		thresholdWidget.openThresholdConfigurationWindow(MILLISECOND_KPI);

		assertTrue("Units do not match selected KPI", thresholdWindow
				.getUnits().equals(unitsMessages[2]));

	}

	/**
	 * US: DEFTFTS-178
	 * Test Case: 29.22	 
	 * Verify for KPI Threshold Filtering default values and that the values 
	 * can be changed with map view updated to represent changes
	 * Check bounds in threshold config box in correct for percentage kpi
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void checkBoundsForPercentageKPIs_29_22() throws InterruptedException,
			ParseException {
		String kpiToSelect = "Service Request Failure Ratio";
        try {
            launchKPIMapTwoHours(kpiToSelect);
        } catch (NoMapException e) {
            fail(e.toString());
        } catch (NoDataException e) {
            fail(e.toString());
        }

        thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);
		List<Integer> expected = new ArrayList<Integer>(Arrays.asList(0, 100));
		List<String> widgetPercents = thresholdWindow
				.getThresholdWindowPercentages(WINDOW_PERCENTAGE_ONE,
						WINDOW_PERCENTAGE_FIVE);

		assertTrue("Min percentage should be " + expected.get(0),
				widgetPercents.get(0).equals(expected.get(0).toString()));
		assertTrue("Max percentage should be " + expected.get(1),
				widgetPercents.get(1).equals(expected.get(1).toString()));
	}

	//** Private Methods **//
	private void setPercentageKpiDefaults() throws InterruptedException {
		thresholdWidget.openThresholdConfigurationWindow(PERCENTAGE_KPI);	
		
		thresholdWindow.setText(TEXT_FIELD_TWO, defaultPercentagesForSRFR[3]);
		thresholdWindow.setText(TEXT_FIELD_THREE, defaultPercentagesForSRFR[2]);
		thresholdWindow.setText(TEXT_FIELD_FOUR,  defaultPercentagesForSRFR[1]);
		thresholdWindow.update();
	}

	private void launchKPIMapTwoHours(String kpiToSelect)
            throws InterruptedException, ParseException, NoMapException, NoDataException {
	
		kpiAnalysisTab.launchKPIWindowWithCustomDate("2 hours",
				null, null,
				"Network", null, 1, kpiToSelect);
		window.getElement(KpiConstants.KPI_MAP_EXPAND).click();
        
        window.waitForMapToLoad();
//        window.waitForWindowToLoad("Loading Map ...");
	}

}
