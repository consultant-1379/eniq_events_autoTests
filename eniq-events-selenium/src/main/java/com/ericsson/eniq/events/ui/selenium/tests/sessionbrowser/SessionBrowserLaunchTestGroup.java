package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * // DEFTFTS-5: Session Browser Launch
 * 
 * @author ekumpen August 2012
 * 
 */

public class SessionBrowserLaunchTestGroup extends SBWebDriverBaseUnitTest {
	public static WebElement element;
	@Autowired
	private SessionBrowserTab sessionbrowserTab;
	
	@Test
	public void enterIMSIAndGetAvailableTimeValues()
			throws InterruptedException, PopUpException {
		openTab();
		// click to details button
		driver.findElement(By.xpath(Constants.DETAILS_BUTTON)).click();

		// enter an IMSI
		driver.findElement(By.xpath(Constants.IMSI_INPUT_TEXTFIELD)).sendKeys(
				"12345678910111213");
		Thread.sleep(4000);
		final List<String> timePeriodList = new LinkedList<String>(Arrays.asList("15 minutes", "30 minutes", "1 hour", "2 hours","6 hours", "12 hours", "1 day", "1 week", "Custom Time"));

		driver.findElement(By.xpath(Constants.PERIOD_DROP_DOWN)).click();

		List<WebElement> timesOnUI = driver.findElements(By
				.xpath("//div[@class='popupContent']//div/div"));

		// loop through all time periods in the drop down
		for (int i = 0; i < timePeriodList.size(); i++) {
			String expectedTime = timePeriodList.get(i);
			String actualTime = timesOnUI.get(i).getText();

			assertTrue("Time period expected: " + expectedTime + " actual: "
					+ actualTime, actualTime.equals(expectedTime));
		}
	}

	/**
	 * Test Case: x.x.2
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void byDefaultSessionDataIsDisplayed() throws InterruptedException,
			PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);

		waitForPageToLoad();

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Data Bearer Sessions are not displayed",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));
	}

	@Test
	public void displayDataCoreOn() throws PopUpException, InterruptedException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		waitForPageToLoad();
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Data Bearer Sessions are not displayed for Core",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));
	}

	@Test
	public void displayDataRanOn() throws PopUpException, InterruptedException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, false);
		
		waitForPageToLoad();
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Data Bearer Sessions are not displayed for Ran",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));

	}

	/**
	 * US-DEFTFTS-11: Data Bearer Session-View (RAN)Details
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException 
	 */
	@Test
	public void dataBearerSessionViewRANDetails() throws InterruptedException,
			PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, false);

		// View Details option can be viewed and launched in a new window
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_LIST));

		assertTrue("Data Bearer Sessions are not displayed",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						table));
		
		int position = sessionbrowserTab.getRecordPosition(
				"Data Bearer Session", table);

		openViewDetails(position);

		Thread.sleep(1000);

		assertTrue("View details widow not displayed",
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.isDisplayed());

		// Required collapsable headings are presented
		List<String> expected_collapsableHeadings = Arrays.asList(
				"Subscriber Identity", "Network Location",
				"Session Properties", "Radio Conditions", "Mobility",
				"Traffic Channel Usage", "Application Performance",
				"Application Traffic Mix");

		List<WebElement> required_collapsableHeadings = driver.findElements(By
				.xpath(Constants.COLLAPSABLE_HEADINGS));

		for (int i = 0; i < required_collapsableHeadings.size(); i++) {
			WebElement value = required_collapsableHeadings.get(i);
			if(!value.isDisplayed()){
				scrollViewDetailsSectionList(false,35);
			}
			Assert.assertTrue("Required collapsable heading" + value.getText()
					+ "is not present", expected_collapsableHeadings.get(i)
					.equals(value.getText()));
		}

		// Possible to scroll from top to bottom and possible to expand/collapse
		// menu items
		scrollViewDetailsSectionList(true,200);
		expandAllHeaders();

		// RAN details will be presented under the Session Properties heading..
		List<String> Expected_RANdetails_SessionProperties = Arrays.asList(
				"PS RAB Activity Start Time", "PS RAB Activity End Time",
				"Starting RAB", "Ending RAB", "No. of new PS RAB",
				"IP Address Assigned", "Starting Access Point",
				"Ending Access Point", "Radio Access Technology",
				"HS Category", "EUL Category");
		List<WebElement> Actual_RANdetails_SessionProperties = driver
				.findElements(By
						.xpath(Constants.RAN_DETAILS_SESSION_PROPERTIES));

		for (int i = 0; i < Expected_RANdetails_SessionProperties.size(); i++) {
			WebElement values = Actual_RANdetails_SessionProperties.get(i);
			scrollViewDetailsSectionListUntilElementVisible(true,values);
			assertTrue(
					"Expected RAN details are not presented under the Session Properties",
					Expected_RANdetails_SessionProperties.get(i).equals(
							values.getText()));
		}

	}

	/**
	 * US-DEFTFTS-11: Possible to scroll lock the headers, headers will only
	 * change when a new header is scrolled to in the dialog box and scroll bar
	 * have directional arrows
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException 
	 */
	@Test
	public void scrollLockHeaders() throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);
		
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_LIST));

		assertFalse("No Data displayed in session list", table.isEmpty());

		int position = sessionbrowserTab.getRecordPosition(
				"Data Bearer Session", table);

		openViewDetails(position);

		Thread.sleep(1000);

		assertTrue("View details widow not displayed",
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.isDisplayed());

		// Possible to scroll from the top to the bottom of the dialog box
		expandAllHeaders();
		scrollToBottomOfViewDetailsSectionList();
		
		assertTrue(
				"Upward directional arrow is not present",
				driver.findElement(
						By.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[1]"))
						.isDisplayed());
		assertTrue(
				"Downward directional arrow is not present",
				driver.findElement(
						By.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[2]"))
						.isDisplayed());

	}

	/*
	 * US:DEFTFTS-359: Data Bearer Session - View Details - Radio Conditions
	 * US:DEFTFTS-360: Data Bearer Session - View Details - Cell Conditions
	 * US:DEFTFTS-361: Data Bearer Session - View Details - Mobility Heading
	 * US:DEFTFTS-362: Data Bearer Session - View Details - Traffic Channel
	 * Usage
	 */
	@Test
	public void dataBearerSessionViewDatailsRadioConditions_359()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertFalse("No Data displayed in session list", sessionTable.isEmpty());

		sessionbrowserTab.launchViewDetais();
		assertTrue("View details window not displayed",
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.isDisplayed());

		String titleOfViewDetailsWindow = driver.findElement(
				By.xpath("//div[contains(@class,'dragdrop-handle')]/div"))
				.getText();
		List<WebElement> sessionListTable = driver.findElements(By
				.xpath(Constants.DBS_SESSION_LIST));

		if (driver.findElement(By.xpath(Constants.DBS_SESSION_LIST))
				.isDisplayed()) {
			int position = getDBSRecordPosition(sessionListTable);
			String selectedTimePeriodDBS = driver
					.findElement(
							By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
									+ (2 + position) + "]/td[1]")).getText();
			assertTrue(
					"Launched view details time period and selected DBS time period is not same",
					titleOfViewDetailsWindow.contains(selectedTimePeriodDBS));
		}

		// Aecceptance Criteria 2 & 3:Tested in US DEFTFTS-47 in
		// DBSRanTestGroup.java
		expandAllHeaders();
		scrollViewDetailsSectionListUntilElementVisible(true,driver.findElement(By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]/table/tbody/tr[2]")));
		
		assertTrue(
				"IMSI not available under Subscriber Identity as per POC",
				driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]/table/tbody/tr[2]"))
						.isDisplayed());

		/*
		 * 
		 * Check Network Location Heading
		 */

		List<String> requiredNetworkDetails = Arrays.asList("Starting SGSN",
				"Ending SGSN", "Starting RNC", "Ending RNC",
				"Starting Cell ID", "Ending Cell ID", "Starting CID",
				"Ending CID");

		List<WebElement> displayedDetails = driver.findElements(By
				.xpath(Constants.RAN_DETAILS_NETWORK_LOCATION));
		assertTrue(
				"Number of required Network Location Details are not equal to displayed Network Location Details in the view details window",
				displayedDetails.size() == requiredNetworkDetails.size());

		for (int i = 0; i < requiredNetworkDetails.size(); i++) {
			WebElement Value = displayedDetails.get(i);
			assertTrue(
					"Details presented under the Network Location did not match with the Acceptance Criteria required details",
					requiredNetworkDetails.get(i).equals(Value.getText()));
		}
		pause(2000);

		/*
		 * 
		 * Check Radio Conditions Heading
		 */
		List<String> requiredRadioDetails = Arrays.asList(
				"No. of RRC Meas. Reports", "No. of new RRC Connections",
				"RRC Connection Start Time", "RRC Connection End Time",
				"Average RSCP (dBm)", "Average Ec/No (dB)",
				"Average Uplink Interference (dB)",
				"No. Samples in Good Coverage, Good Signal",
				"No. Samples in Good Coverage, Poor Signal",
				"No. Samples in Poor Coverage, Good Signal",
				"No. Samples in Poor Coverage, Poor Signal",
				"Average no of HS users in Cell", "Average DL non-HS TX power");

		List<WebElement> displayedRadioDetails = driver.findElements(By
				.xpath(Constants.RAN_DETAILS_RADIO_CONDITIONS));
		assertTrue(
				"Number of required Radio Conditions are not equal to displayed Radio Conditions in the view details window",
				displayedRadioDetails.size() == requiredRadioDetails.size());
		WebElement ranHeader = driver.findElement(By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]/div[1]"));
		scrollViewDetailsSectionListUntilElementVisible(false,ranHeader);
		//ranHeader.click();
		for (int i = 0; i < requiredRadioDetails.size(); i++) {
			WebElement values = displayedRadioDetails.get(i);
			scrollViewDetailsSectionListUntilElementVisible(false,values);
			assertTrue(
					"Details presented under the Radio Conditions did not match with the Acceptance Criteria required details",
					requiredRadioDetails.get(i).equals(values.getText()));
		}

		/*
		 * 
		 * Check Mobility Heading
		 */
		List<String> requiredMobilityDetails = Arrays.asList(
				"No. of Cells Visited",
				"No. of Successful Routing Area Updates",
				"No. of Successful Inter Frequency handovers",
				"No. of Successful IRAT handovers",
				"No. of Successful Soft handovers",
				"No. of Successful HS cell changes",
				"No. of Failed Routing Area Updates",
				"No. of Failed Inter Frequency handovers",
				"No. of Failed IRAT handovers", "No. of Failed Soft handovers",
				"No. of Failed HS cell changes",
				"No. of times in Compressed Mode",
				"Time spent in Compressed Mode (ms)",
				"No. of times in UL Compressed Mode",
				"No. of times in DL Compressed Mode",
				"No. of times in UL and DL Compressed Mode",
				"Avg No. of Compressed Mode users");

		List<WebElement> displayedMobilityDetails = driver.findElements(By
				.xpath(Constants.RAN_DETAILS_MOBILITY_CONDITIONS));
		assertTrue(
				"Number of required Mobility details are not equal to displayed Mobility details in the view details window",
				displayedMobilityDetails.size() == requiredMobilityDetails
						.size());

		for (int i = 0; i < requiredMobilityDetails.size(); i++) {
			WebElement values = displayedMobilityDetails.get(i);
			scrollViewDetailsSectionListUntilElementVisible(false,values);
			assertTrue(
					"Details presented under the Mobility details did not match with the Acceptance Criteria required details",
					requiredMobilityDetails.get(i).equals(values.getText()));
		}

		/*
		 * 
		 * Check Traffic Channel Usage Heading
		 */
		List<String> requiredTraffic_Channel_Usage_Details = Arrays.asList(
				"% Time Spent in DCH/HS Activity", "% Time Spent in HS",
				"% Time Spent in EUL", "No. of channel upswitches",
				"CUS Attempt", "CUS Success", "CUS Failure",
				"No. of channel downswitches", "CDS Attempt", "CDS Success",
				"CDS Failure", "No. of switches due to UE activity",
				"No. of switches due to capacity",
				"No. of switches due to mobility coverage",
				"No. of switches due to Qos DCH",
				"No. of switches due to queue",
				"No. of switches due to other reason");

		// if (sessionbrowserTab
		// .isElementPresent("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[6]/div[2][@style='display: none;']"))
		// {
		// driver.findElement(
		// By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[6]//span[2]"))
		// .click();
		// }

		List<WebElement> displayedTraffic_Channel_Usage_Details = driver
				.findElements(By
						.xpath(Constants.RAN_DETAILS_TRAFFIC_CHANNEL_USAGE));
		assertTrue(
				"Number of required Traffic Channel Usage details are not equal to displayed Traffic Channel Usage details in the view details window",
				displayedTraffic_Channel_Usage_Details.size() == requiredTraffic_Channel_Usage_Details
						.size());

		for (int i = 0; i < requiredTraffic_Channel_Usage_Details.size(); i++) {
			WebElement values = displayedTraffic_Channel_Usage_Details.get(i);
			scrollViewDetailsSectionListUntilElementVisible(false,values);
			assertTrue(
					"Details presented under the Traffic Channel Usage did not match with the Acceptance Criteria required details",
					requiredTraffic_Channel_Usage_Details.get(i).equals(
							values.getText()));
		}
	}

	// *********** Private Methods *********** \\

	private void expandAllHeaders() {
		List<WebElement> visibleSectionSize = driver.findElements(By
				.xpath(Constants.COLLAPSABLE_HEADINGS));
		for (int i = 1; i <= visibleSectionSize.size(); i++) {
			boolean flag = driver
					.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]/div[2]")).isDisplayed();
			if (!flag) {
				scrollViewDetailsSectionList(false,35);
				driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")).click();
			}
		}
	}

	private void waitForPageToLoad() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	private int getDBSRecordPosition(List<WebElement> table) {

		String dbsValue = "Data Bearer Session";
		for (int i = 0; i < table.size(); i++) {
			String text = table.get(i).getText().trim();
			if (dbsValue.equals(text)) {
				return i;
			}
		}
		return -1;
	}
	
	private void scrollToBottomOfViewDetailsSectionList() {
		int oldPos = 0;
		int newPos = 0;
		List<String> current_collapsableHeadings = Arrays.asList(
				"Subscriber Identity", "Network Location",
				"Session Properties", "Radio Conditions", "Mobility",
				"Traffic Channel Usage", "Application Performance",
				"Application Traffic Mix");

		do {
			oldPos = getScrollBarPosition();
			for (int x = 0; x < 10; x++) {
				webDriverSelenium
						.click("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/img[2]");
				String scrollLockHeaders = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//div[starts-with(@class,'gwt-Label')]"))
						.getText();
				assertTrue(current_collapsableHeadings
						.contains(scrollLockHeaders));

			}
			newPos = getScrollBarPosition();
		} while (oldPos < newPos);
	}
	
	private int getScrollBarPosition() {
		if (webDriverSelenium
				.isElementPresent("//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[2]/div/div")) {
			String scrollAttr = webDriverSelenium
					.getAttribute("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div/div/div[2]/div[2]/div/div@style");

			String[] temp = scrollAttr.split("top:");

			return Integer.parseInt((temp[1].split("px")[0]).trim());
		}

		return 0;
	}

}
