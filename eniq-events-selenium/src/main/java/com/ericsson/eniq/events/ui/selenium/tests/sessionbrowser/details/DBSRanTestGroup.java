package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.DataBaseConnection;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author edecmcc
 * 
 */
public class DBSRanTestGroup extends SBWebDriverBaseUnitTest {

	DataBaseConnection dbcon = new DataBaseConnection();

	@Autowired
	private SessionBrowserTab sessionbrowserTab;

	/*
	 * US: DEFTFTS-723 Test Case 7.90: Verify that when Toggle ON that the 3
	 * toggle buttons are the have correct colour in inside of button and Stroke
	 * colour on border of button
	 */
	@Test
	public void testInternalSystemReleaseEvent_7_90()
			throws InterruptedException, PopUpException, ParseException {

		// openDataBearerSessionRanCore(Constants.IMSI_CORE_RAN_USER_PLANE);
		openSessionDetail(CommonDataType.SB_IMSI_ATTACH, true, true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		int numberOfToggles = driver
				.findElements(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div"))
				.size();

		assertTrue(
				"Did not find 3 toggle buttons at the bottom of session activity pane",
				numberOfToggles == 3);

		assertTrue("Core signalling events should be present in session list",
				isCoreSignallingEventPresent(sessionTable));
		driver.findElement(
				By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[2]/input"))
				.click();

		sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));
		assertFalse(
				"Core signalling events should not be present in session list",
				isCoreSignallingEventPresent(sessionTable));

		String coreToggleTitle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[2]"))
				.getAttribute("title");

		assertTrue("The title of Core Signalling is not correct",
				"Core Signalling".equals(coreToggleTitle));

	}

	@Test
	public void testInternalSystemReleaseEvent_7_90_CHECK_RAN_SIGNALLING()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, true);

		int numberOfToggles = driver
				.findElements(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div"))
				.size();

		assertTrue(
				"Did not find 3 toggle buttons at the bottom of session activity pane",
				numberOfToggles == 3);
		zoom(false,6);
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Ran signalling events should be present in session list",
				isRanSignallingEventPresent(sessionTable));
		zoom(true,6);
		driver.findElement(
				By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[3]/input"))
				.click();

		sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));
		assertFalse(
				"Ran signalling events should not be present in session list",
				isRanSignallingEventPresent(sessionTable));
		String ranToggleTitle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[3]"))
				.getAttribute("title");

		assertTrue("The title of Ran Signalling is not correct",
				"RAN Signalling".equals(ranToggleTitle));
	}

	/**
	 * US: DEFTFTS-723 Test Case 7.91: Verify that when Toggle ON and Hover that
	 * the 3 toggle buttons are have correct colour in inside of button and
	 * Stroke colour on border of button
	 * 
	 * Note: There are more test cases for DEFTFTS-723 but it is not possible to
	 * write automation tests
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	//Removing Test Case due to EQEV-8382
	//@Test
	public void testInternalSystemReleaseEvent_7_91()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DETACH, true, true);

		Thread.sleep(1000);

		String coreToggleStyle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[2]"))
				.getAttribute("style");
		String coreBefore = getStyleValue(coreToggleStyle, "background-color");

		driver.findElement(
				By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[2]/input"))
				.click();

		coreToggleStyle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[2]"))
				.getAttribute("style");
		String coreAfter = getStyleValue(coreToggleStyle, "background-color");

		assertFalse("The color of the Core Signalling toggle does not change",
				coreAfter.equals(coreBefore));

		String ranToggleStyle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[3]"))
				.getAttribute("style");
		String ranBefore = getStyleValue(ranToggleStyle, "background-color");

		driver.findElement(
				By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[3]/input"))
				.click();

		ranToggleStyle = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[4]/div[3]"))
				.getAttribute("style");
		String ranAfter = getStyleValue(ranToggleStyle, "background-color");

		assertFalse("The color of the Ran Signalling toggle does not change",
				ranAfter.equals(ranBefore));

	}

	/**
	 * US: DEFTFTS-47 Test case: Show Data Bearer Sessions AC 1 : The radio part
	 * of 5 minute Data Bearer Sessions for the selected subscriber and time
	 * period are presented. AC 5 : It is possible to view Data Bearer Sessions
	 * for specified IMSIs.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testViewDataBearerSession_4_4_3() throws InterruptedException,
			PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false,
				true);
		checkForDBS();

	}

	/**
	 * Test case: Start time of Data Bearer Sessions AC 2 : The start time of
	 * each 5 minute Data Bearer Sessions is presented
	 * 
	 * NOTE: This test case is failing on the current server due to incomplete
	 * data
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	// @Test
	// public void testDBSStartTimes() throws InterruptedException,
	// PopUpException, ParseException {
	//
	// sessionbrowserTab.openTab();
	// waitForPageToLoad();
	// sessionbrowserTab.openDetailsSessionBrowser("454061192646383");
	// sessionbrowserTab.openCustomDateAndTime();
	//
	// driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	//
	// Thread.sleep(1000);
	//
	// List<WebElement> table =
	// driver.findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[1]"));
	// List<Timestamp> dbsTimes = new ArrayList<Timestamp>();
	//
	// for (WebElement row : table) {
	// String text = row.getText();
	//
	// if(text.matches("\\d\\d:\\d\\d:\\d\\d")){
	// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	// Date parsedDate = sdf.parse(text);
	// Timestamp timestamp = new Timestamp(parsedDate.getTime());
	// dbsTimes.add(timestamp);
	// }
	// }
	//
	// assertFalse("There were no DBS session times found..",
	// dbsTimes.isEmpty());
	//
	// Timestamp prevTime = null;
	//
	// for(int i = 0; i < dbsTimes.size(); i++){
	//
	// if(i == 0){
	// prevTime = dbsTimes.get(i);
	// }
	// else {
	//
	// int newMins = dbsTimes.get(i).getMinutes();
	// int newHrs = dbsTimes.get(i).getHours();
	//
	// if(newMins > 0){
	// assertTrue("Interval not five mins before time: " +
	// dbsTimes.get(i).getHours() + ":" + dbsTimes.get(i).getMinutes(),
	// newMins == (prevTime.getMinutes() + 5));
	// }
	// else if(newHrs > 0){
	// assertTrue("Hours not correct at time: " + dbsTimes.get(i),
	// newHrs == (prevTime.getHours() + 1));
	// }
	//
	// prevTime = dbsTimes.get(i);
	// }
	// }
	// }

	/**
	 * US: DEFTFTS-47 Test case: To verify the tool tip is correct for a Data
	 * Bearer Session record AC 3: A tool tip indicates the following for each 5
	 * minute Data Bearer Session: Start cell and end cell.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testToolTipForDataBearerSession_4_4_3()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false,
				true);

		checkForDBS();

		assertTrue(
				"There is no Start cell tool tip for DBS",
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[contains(@title,'Start Cell') and text()='Data Bearer Session']"))
						.isDisplayed());
		assertTrue(
				"There is no End cell tool tip for DBS",
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[contains(@title,'End Cell') and text()='Data Bearer Session']"))
						.isDisplayed());

	}

	/**
	 * US: DEFTFTS-47 Test case: To verify the drop down menu correct on a Data
	 * Bearer Session record AC 4: A drop down menu is provided with the
	 * following options: Reports, Server Distribution and View details.
	 * 
	 * There is a difference in the list order between the user story and the
	 * UI, the UI has the order: Reports, View Details and Server Distribution.
	 * This was discussed and approved by the PO
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDropDownForDataBearerSession_4_4_3()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false,
				true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkForDBS();

		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				sessionTable);
		List<String> expectedValues = Arrays.asList("Reports", "View Details",
				"Server Distribution");

		if (index >= 0) {
			driver.findElement(By.xpath(Constants.DROPDOWN_MENU_LINK)).click();

			List<WebElement> options = driver.findElements(By
					.xpath(Constants.DROPDOWN_MENU_CONTENT));

			for (int i = 0; i < options.size(); i++) {
				WebElement value = options.get(i);

				assertTrue("The value " + value.getText()
						+ " was not found in the drop down menu",
						expectedValues.get(i).equals((value.getText())));
			}
		}
	}

	/**
	 * US: DEFTFTS-47 Test case: To verify that Data Bearer Session records are
	 * in descending order AC 8: The sessions will be presented in descending
	 * order from the start date and time down to the end date and time.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDBSTimesInDescendingOrder_4_4_3()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false,
				true);
		checkForDBS();

		List<WebElement> table = driver
				.findElements(By
						.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[1]"));
		List<Timestamp> dbsTimes = new ArrayList<Timestamp>();

		for (WebElement row : table) {
			String text = row.getText();

			if (text.matches("\\d\\d:\\d\\d:\\d\\d")) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date parsedDate = sdf.parse(text);
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				dbsTimes.add(timestamp);
			}
		}

		Timestamp prevTime = null;

		if (!dbsTimes.isEmpty()) {
			for (int i = 0; i < dbsTimes.size(); i++) {
				if (i == 0) {
					prevTime = dbsTimes.get(i);
				} else {
					if (prevTime.getHours() > dbsTimes.get(i).getHours()) {
						// hour changed
						prevTime = dbsTimes.get(i);
						continue;
					}
					if (dbsTimes.get(i).getMinutes() > 0) {
						assertTrue("Session times not in descending order",
								prevTime.before(dbsTimes.get(i)));
					}

					prevTime = dbsTimes.get(i);
				}
			}
		}
	}

	/**
	 * US: DEFTFTS-47 Test case: To verify that header changes when scroll down
	 * the session list AC 9: The date and day header for the sessions will be
	 * scroll locked and only changed when a new date and day is scrolled to in
	 * the browser.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testHeaderChangesWhileScrolling_4_4_3()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false,
				true);
		String initialDateInList = null;

		// check if scroll bar is present
		if (driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//div[2]/div/div"))
				.isDisplayed()) {

			List<WebElement> tableBeforeScroll = driver
					.findElements(By
							.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr"));

			// store initial date
			initialDateInList = tableBeforeScroll.get(0).getText();

			String header = driver.findElement(
					By.xpath(Constants.SESSION_LIST_HEADER)).getText();

			// check the value of the header to match the first element in the
			// table
			assertTrue("Header does not match first date before scrolling ",
					header.equals(initialDateInList));

			sessionbrowserTab.scrollToBottomOfSessionList();

			header = driver
					.findElement(By.xpath(Constants.SESSION_LIST_HEADER))
					.getText();

			List<WebElement> tableAfterScroll = driver
					.findElements(By
							.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr"));

			assertTrue("Header not in session list after scroll",
					isHeadingInList(header, tableAfterScroll));
		}
	}

	/**
	 * US: DEFTFTS-146 Test case 7.75: Verify that the user plane TVP
	 * TCPTA-Partial events are 5 minutes apart, is descending order, have drop
	 * down menu options and tool tip options in Data Bearer Session of Details
	 * View note: some actions are not completed as they were covered in
	 * previous US - DEFTFTS 47
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void test_UserPlaneTVP_TCPTA_Partial_Event_7_75()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true,
				true);

		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("There were no Data Bearer Session Event found",
				sessionbrowserTab.isEventFound("Data Bearer Session", table));

		assertTrue(
				"There is no Start cell tool tip for DBS",
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[contains(@title,'Start Cell') and text()='Data Bearer Session']"))
						.isDisplayed());
		assertTrue(
				"There is no End cell tool tip for DBS",
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[contains(@title,'End Cell') and text()='Data Bearer Session']"))
						.isDisplayed());

	}

	/**
	 * US: DEFTFTS-156: RAN Signalling RRC MR - Session List Entry Test Case
	 * 4.7.105: verify that a user should able to see all the RRC Measurement
	 * Reports generated in the last 30 seconds from when dropped call
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 * @throws Exception
	 */
	@Test
	public void test_RRC_Measurement_Report_4_7_105()
			throws InterruptedException, ParseException {
		String eventName = "RRC Measurement Report";

		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true,
				false);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		boolean eventFound = sessionbrowserTab.isEventFound(eventName,
				sessionTable);

		assertTrue("The RRC Measurement Report event was found", eventFound);

	}

	/**
	 * US: DEFTFTS-355: RAN Signalling HFA (HHO) - Details - View RAN Details
	 * Action
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void ranSignallingHHOviewDetailPerAC_4_7_116()
			throws InterruptedException, ParseException {
		final String INTERNAL_HHOF = "Int Out Hard Handover Failure";

		openSessionDetail(CommonDataType.SB_IMSI_INT_OUT_HARD_HANDOVER, true,
				false);
		LinkedHashMap<String, List<String>> perACElements = new LinkedHashMap<String, List<String>>();

		perACElements.put("Subscriber Identity",
				Arrays.asList("MSISDN", "IMSI", "Network"));
		perACElements.put("Handover Details", Arrays.asList("Source Rab Type",
				"Wanted Rab Type", "Target Rab Type", "Source Cell ID 1",
				"Source CID 1", "Source RNC 1", "Serving HSDSCH CID",
				"Target PLMN", "Target Location Area", "Target Cell Id",
				"Target CID", "Target Controller", "Ec/No Cell 1 (dB)",
				"RSCP Cell 1 (dBm)", "Target Cell RSSI (dBm)",
				"Target Cell Ec/No (dB)", "Target Cell RSCP (dBm)"));
		perACElements.put("Event Details", Arrays.asList("Event Time",
				"Procedure Indicator", "Evaluation Case",
				"Source Connection Properties", "Wanted Connection Properties",
				"Target Connection Properties"));
		perACElements.put("Active Set Details", Arrays.asList(
				"Source Cell Id 2", "Source CID 2", "Source RNC 2",
				"Source Cell Id 3", "Source CID 3", "Source RNC 3",
				"Source Cell Id 4", "Source CID 4", "Source RNC 4",
				"Ec/No Cell 2 (dB)", "Ec/No Cell 3 (dB)", "Ec/No Cell 4 (dB)",
				"RSCP Cell 2 (dBm)", "RSCP Cell 3 (dBm)", "RSCP Cell 4 (dBm)"));

		int count = driver
				.findElements(
						By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index]"))
				.size();

		for (int i = 0; i < count; i++) {
			String hho = driver
					.findElement(
							By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
									+ i + "']/td[3]")).getText();

			if (hho.equals(INTERNAL_HHOF)) {
				driver.findElement(
						By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
								+ i + "']/td[2]")).click();
				Thread.sleep(10);
				driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
				waitForPageToLoad();

				int noOfheadings = driver
						.findElements(
								By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div"))
						.size();

				for (int eachHeading = 1; eachHeading <= noOfheadings; eachHeading++) {
					expandheading(eachHeading);
					// ArrayList<String> elements = new ArrayList<String>();
					String headingTile = driver
							.findElement(
									By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
											+ eachHeading + "]/div/span[1]"))
							.getText();
					List<String> acElements = perACElements.get(headingTile);
					assertNotNull("Invalid Title " + headingTile, acElements);
					int noOfelements = driver
							.findElements(
									By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
											+ eachHeading + "]//tbody//tr"))
							.size();
					for (int eachElement = 1; eachElement <= noOfelements; eachElement++) {
						scrollViewDetailsSectionListUntilElementVisible(false,driver
								.findElement(By
										.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
												+ eachHeading
												+ "]/div[2]//tbody//tr["
												+ eachElement + "]//td[1]/div")));
						String element = (driver
								.findElement(By
										.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
												+ eachHeading
												+ "]/div[2]//tbody//tr["
												+ eachElement + "]//td[1]/div"))
								.getText());
						assertEquals(acElements.get(eachElement - 1), element);
					}
				}
				closeViewDetails();
				
				break;
			}
		}
	}

	/**
	 * DEFTFTS-155 RAN Signalling HFA (HHO & Internal Successful HSDSCH Cell
	 * Change) - Session List Entry 4.7.115 EE13.0_SESSB&KPI_7.115: Verify that
	 * a user is able to see Int Successful HSDSCH Cell Change Event in the
	 * Session List entry panel in the Session Browser Details Tab.
	 * 
	 * @throws SQLException
	 * @throws ParseException
	 */

	@Test
	public void ranSignallingHSDSCH_4_7_115() throws InterruptedException,
			ParseException {
		final String INTERNAL_HSDSCH = "Int Successful HSDSCH Cell Change";

		openSessionDetail(
				CommonDataType.SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE, true,
				false);
		LinkedHashMap<String, List<String>> perACElements = new LinkedHashMap<String, List<String>>();

		perACElements.put("Subscriber Identity",
				Arrays.asList("MSISDN", "IMSI", "Network"));
		perACElements.put("Handover Details", Arrays.asList("Source Cell Id",
				"Source CID", "Source RNC", "Source Sec. Serving Hsdsch Cell",
				"Target Cell Id", "Target CID", "Target RNC",
				"Target Sec. Serving Hsdsch Cell", "Ec/No Source Cell (dB)",
				"Ec/No Target Cell (dB)", "RSCP Source Cell (dBm)",
				"RSCP Target Cell (dBm)"));
		perACElements.put("Event Details", Arrays.asList("Event Time",
				"Event Trigger", "RAB Type", "GBR Uplink (bps)",
				"GBR Downlink (bps)", "Source Connection Prop",
				"Target Connection Prop", "Source Connection Prop Ext.",
				"Target Connection Prop Ext."));
		perACElements.put("Active Set Details", Arrays.asList(
				"Source Cell ID 1", "Source CID 1", "Source RNC 1",
				"Source Cell ID 2", "Source CID 2", "Source RNC 2",
				"Source Cell ID 3", "Source CID 3", "Source RNC 3",
				"Source Cell ID 4", "Source CID 4", "Source RNC 4"));

		int count = driver
				.findElements(
						By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index]"))
				.size();

		for (int i = 0; i < count; i++) {
			String hsdsch = driver
					.findElement(
							By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
									+ i + "']/td[3]")).getText();

			if (hsdsch.equals(INTERNAL_HSDSCH)) {
				driver.findElement(
						By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
								+ i + "']/td[2]")).click();
				Thread.sleep(10);
				driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
				waitForPageToLoad();

				int noOfheadings = driver
						.findElements(
								By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div"))
						.size();

				for (int eachHeading = 1; eachHeading <= noOfheadings; eachHeading++) {
					expandheading(eachHeading);
					// ArrayList<String> elements = new ArrayList<String>();
					String headingTile = driver
							.findElement(
									By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
											+ eachHeading + "]/div/span[1]"))
							.getText();
					List<String> acElements = perACElements.get(headingTile);
					assertNotNull("Invalid Title " + headingTile, acElements);
					int noOfelements = driver
							.findElements(
									By.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
											+ eachHeading + "]//tbody//tr"))
							.size();
					for (int eachElement = 1; eachElement <= noOfelements; eachElement++) {
						scrollViewDetailsSectionListUntilElementVisible(false,driver
								.findElement(By
										.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
												+ eachHeading
												+ "]/div[2]//tbody//tr["
												+ eachElement + "]//td[1]/div")));
						String element = (driver
								.findElement(By
										.xpath(".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div["
												+ eachHeading
												+ "]/div[2]//tbody//tr["
												+ eachElement + "]//td[1]/div"))
								.getText());
						assertEquals(acElements.get(eachElement - 1), element);
					}
				}
				closeViewDetails();
				
//				driver.findElement(
//						By.xpath(Constants.VIEW_DETAILS_WINDOW_CLOSE)).click();
				break;
			}
		}
	}

	private void checkForDBS() {
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue(
				"There are no Data Bearer Sessions Displayed in session list",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));
	}

	// ********************************************* PRIVATE METHODS
	// ********************************************//

	private String getStyleValue(String style, String field) {
		String[] temp = style.split(field + ":");

		return (temp[1].split("px")[0]).trim();
	}

	private boolean isCoreSignallingEventPresent(List<WebElement> sessionTable) {
		List<String> coreEvents = Arrays.asList("Attach", "Detach",
				"PDP Activate", "PDP Deactivate", "Service Request", "RAU",
				"ISRAU");

		for (WebElement e : sessionTable) {
			if (coreEvents.contains(e.getText())) {
				return true;
			}
		}
		return false;
	}

	private boolean isRanSignallingEventPresent(List<WebElement> sessionTable) {
		List<String> coreEvents = Arrays.asList("Internal System Release",
				"Internal Call Setup Failure",
				"Internal Soft Handover Execution Failure");

		for (WebElement e : sessionTable) {
			if (coreEvents.contains(e.getText())) {
				return true;
			}
		}
		return false;
	}

	private void waitForPageToLoad() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private boolean isHeadingInList(String header,
			List<WebElement> tableAfterScroll) {
		for (WebElement row : tableAfterScroll) {
			if (header.equals(row.getText())) {
				return true;
			}
		}
		return false;
	}

	private void expandheading(int eachHeading) {
		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ eachHeading + "]/div[2]"))
				.getAttribute("style").isEmpty();
		if (!sectionVisiable) {
			scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
							+ eachHeading + "]//span[2]")));
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
							+ eachHeading + "]//span[2]")).click();
		}
	}
}
