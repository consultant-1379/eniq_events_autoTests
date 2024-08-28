package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * // DEFTFTS-49: Data Bearer Session - Core // DEFTFTS-44: Data Bearer Session
 * - View (Core) Details
 * 
 * @author edecmcc
 */
public class DBSCoreTestGroup extends SBWebDriverBaseUnitTest {

	@Autowired
	private SessionBrowserTab sessionbrowserTab;

	/**
	 * US: DEFTFTS-453 No single test case for this, testing user story
	 * DEFTFTS_452 Verify that a user is able to identify a change in one or
	 * more Cells for the 5 minute Data Bearer Session in the Session List
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testDataBearerSessionCellChangeIndicationA_7_138()
			throws InterruptedException, ParseException {
		final String RAB_CHANGE_CLASS_NAME = "rabCellChanged";
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		
		checkForDBS();
		
		int numberOfEvents = driver
				.findElements(
						By.xpath("./*//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index]"))
				.size();
		for (int event = 0; event < numberOfEvents; event++) {
			String eventRowPath = "./*//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
					+ event + "']";
			String eventNamePath = eventRowPath + "/td[3]";
			String eventName = driver.findElement(By.xpath(eventNamePath))
					.getText();
			String cssClassName = driver.findElement(By.xpath(eventRowPath))
					.getAttribute("class");
			if (eventName.equals("Data Bearer Session")
					&& cssClassName.contains(RAB_CHANGE_CLASS_NAME)) {
				assertTrue("Cell Change Style not found",
						cssClassName.contains(RAB_CHANGE_CLASS_NAME));
				break;
			}
		}
	}
	
	/**
	 * US: DEFTFTS-453 7.138: Verify the Cell Change Indicator arrow in the View
	 * Details Window for Data Bearer Session Event in the Session Browser
	 * Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws ParseException 
	 */
	@Test
	public void testDataBearerSessionCellChangeIndicationB_7_138()
			throws InterruptedException, ParseException {
		// Action 1 & 2

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		
		checkForDBS();
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		// Action 3

		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				table);

		String tooltip = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
								+ index + "']/td[3]")).getAttribute("title");

		assertTrue("Tooltip does not contain Start Cell",
				tooltip.contains("Start Cell"));
		assertTrue("Tooltip does not contain End Cell",
				tooltip.contains("End Cell"));
		assertTrue("Tooltip does not contain Cell Count",
				tooltip.contains("Cell Count"));

		openViewDetails(index);

		final String networkLocationSummaryGridXPath = "//div[./div/span[./text()='Network Location']]/div/table[./@class='summaryGrid']//tbody";
		final WebElement networkLocationSummaryGrid = driver.findElement(By
				.xpath(networkLocationSummaryGridXPath));

		final List<WebElement> networkLocationSummaryGridDetails = networkLocationSummaryGrid
				.findElements(By.xpath(networkLocationSummaryGridXPath
						+ "/tr//div"));

		assertEquals("Starting cell id label does not match",
				"Starting Cell ID", networkLocationSummaryGridDetails.get(8)
						.getText());
		assertTrue(
				"Start cell id does not match tooltip in session activity",
				networkLocationSummaryGridDetails.get(9).getText()
						.equals(getStartCell(tooltip)));
		assertEquals("Ending cell id label does not match", "Ending Cell ID",
				networkLocationSummaryGridDetails.get(10).getText());
		assertTrue(
				"End cell id does not match tooltip in session activity",
				networkLocationSummaryGridDetails.get(11).getText()
						.equals(getEndCell(tooltip)));

	}


	/**
	 * US: DEFTFTS-49 Test case 13.1.1: Verify that a user is able to see Core
	 * Data Bearer Session for 5 minutes on the Session List entry panel in the
	 * Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDataBearerSessionCore13_1_1() throws InterruptedException,
			PopUpException, ParseException {
		// Action 1
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Data Bearer Sessions are not displayed",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));

		int position = getDBSRecordPosition(sessionTable);

		openViewDetails(position);

		Thread.sleep(1000);

		assertTrue("View details window did not open",
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.isDisplayed());

		// action 2 & 3 - These actions were tested in US DEFTFTS-47
	}

	/**
	 * US: DEFTFTS-49 Test case 13.1.2: Verify that a user is able to access the
	 * Menu provided with each of the Data Bearer Sessions on the Session List
	 * entry panel in the Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDataBearerSessionCore13_1_2() throws InterruptedException,
			PopUpException, ParseException {
		// Action 1 - tested in US DEFTFTS-47
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("Data Bearer Sessions are not displayed",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));

		int position = getDBSRecordPosition(sessionTable);
		// Action 2
		openViewDetails(position);

		Thread.sleep(1000);
		assertTrue("View details widow not displayed",
				driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW))
						.isDisplayed());

		// Action 3
		WebElement titleDetails = driver
				.findElement(By
						.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
		assertTrue("Window title does not match Event name in session list",
				titleDetails.getText().contains(getDBSEventName(sessionTable)));

		String sessionTimeOnList = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + position) + "]/td[1]")).getText();
		assertTrue("Window time does not match Event time in session list",
				titleDetails.getText().contains(sessionTimeOnList));

	}

	/**
	 * US: DEFTFTS-49 Test case 13.1.3: Verify the scrolling option for the Data
	 * Bearer Session on the Session List entry panel in the Session Browser
	 * Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDataBearerSessionCore13_1_3() throws InterruptedException,
			PopUpException, ParseException {
		// Action 1
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue("No Data Bearer Sessions for Core found",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));

		// Action 2
		assertTrue(
				"Scroll bar not positioned at the top of the session list pane",
				sessionbrowserTab.getSessionListScrollBarPosition() == 0);

		// Action 3 - tested in US DEFTFTS-47

		// Action 4,5 & 6 - tested in US DEFTFTS-13
	}

	// Note: DEFTFTS-44 has will be changed due to DEFTFTS-330
	/*
	 * AC 1 & 2 - Verify collapsible headings for Data Bearer Session Core
	 * details
	 */
	@Test
	public void testDataBearerSessionCore_DEFTFTS_44_1()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertFalse("No Sessions records for found", table.isEmpty());

		if (driver.findElement(By.xpath(Constants.DBS_SESSION_LIST))
				.isDisplayed()) {

			int index = sessionbrowserTab.getRecordPosition(
					"Internal Soft Handover Execution Failure", table);

			if (index >= 0) {
				driver.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + index) + "]/td[2]")).click();
				driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
				Thread.sleep(5000);
				if (driver.findElement(
						By.xpath("//div[contains(@class,'dragdrop-handle')]"))
						.isDisplayed()) {
					List<String> expectedSections = Arrays.asList(
							"Subscriber Identity", "Network Location",
							"Session Properties", "Radio Conditions",
							"Mobility", "Traffic Channel Usage",
							"Application Performance",
							"Application Traffic Mix");
					List<WebElement> sections = driver
							.findElements(By
									.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

					for (int i = 0; i < sections.size(); i++) {
						WebElement value = sections.get(i);
						assertTrue(
								"The section "
										+ expectedSections.get(i)
										+ " was not found in the view details popup",
								expectedSections.get(i).equals(
										(value.getText())));
					}
				}

				// AC: 3 & 4 - Tested by other tests
			}

		}
	}

	/* AC: 5 - Verify details presented under the Subscriber Identity heading */
	@Test
	public void testDataBearerSessionCore_DEFTFTS_44_2()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		
		assertTrue("No Data Bearer Sessions for Core found",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						table));
		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				table);

		if (index >= 0) {
			List<WebElement> section = getSubscriberIdentitySection(index);
			List<String> expectedValues = Arrays.asList("MSISDN", "IMSI",
					"Terminal", "Network");

			for (int i = 0; i < section.size(); i++) {
				String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
	}

	/* AC: 6 - Verify details presented under the Network Location heading */
	@Test
	public void testDataBearerSessionCore_DEFTFTS_44_3()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		
		assertTrue("No Data Bearer Sessions for Core found",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						table));
		
		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				table);

		if (index >= 0) {
			List<WebElement> section = getNetworkLocationSection(index);
			List<String> expectedValues = Arrays.asList("Starting SGSN",
					"Ending SGSN", "Starting RNC", "Ending RNC",
					"Starting Cell ID", "Ending Cell ID", "Starting CID",
					"Ending CID");

			for (int i = 0; i < section.size(); i++) {
				String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
	}

	/* AC: 7 - Verify details presented under the Session Properties heading */
	@Test
	public void testDataBearerSessionCore_DEFTFTS_44_4()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				table);
		
		assertTrue("No Data Bearer Sessions for Core found",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						table));
		if (index >= 0) {
			List<WebElement> section = getSessionPropertiesSection(index);
			List<String> expectedValues = Arrays.asList(
					"PS RAB Activity Start Time", "PS RAB Activity End Time",
					"Starting RAB", "Ending RAB", "No. of new PS RAB",
					"IP Address Assigned", "Starting Access Point",
					"Ending Access Point", "Radio Access Technology",
					"HS Category", "EUL Category");
			for (int i = 0; i < section.size(); i++) {
				String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
	}

	/* AC: 8 - Verify details presented under the Mobility heading */
	@Test
	public void testDataBearerSessionCore_DEFTFTS_44_5()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);
		List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				table);
		
		assertTrue("No Data Bearer Sessions for Core found",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						table));

		if (index >= 0) {
			List<WebElement> section = getMobilitySection(index);
			List<String> expectedValues = Arrays.asList("No. of Cells Visited",
					"No. of Successful Routing Area Updates",
					"No. of Successful Inter Frequency handovers",
					"No. of Successful IRAT handovers",
					"No. of Successful Soft handovers",
					"No. of Successful HS cell changes",
					"No. of Failed Routing Area Updates",
					"No. of Failed Inter Frequency handovers",
					"No. of Failed IRAT handovers",
					"No. of Failed Soft handovers",
					"No. of Failed HS cell changes",
					"No. of times in Compressed Mode",
					"Time spent in Compressed Mode (ms)",
					"No. of times in UL Compressed Mode",
					"No. of times in DL Compressed Mode",
					"No. of times in UL and DL Compressed Mode",
					"Avg No. of Compressed Mode users");
			for (int i = 0; i < section.size(); i++) {
				String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
		// AC: 9 - Cannot be tested by automation
	}

	/**
	 * US: DEFTFTS-902 Test case: As a Network Engineer I want to view the user
	 * plane Classification part of 5 minute Data Bearer Sessions for a selected
	 * IMSI in my network over a selected time period so that I can determine
	 * the affects this has on the subscriber.
	 * 
	 * Use IMSI from EVENT_E_USER_PLANE_CLASSIFICATION_RAW
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testDataBearerSessionUserPaneClassification_7_77()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, true);

		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertTrue(
				"There are no Data Bearer Sessions Displayed in session list",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));

		int index = sessionbrowserTab.getRecordPosition("Data Bearer Session",
				sessionTable);

		List<String> expectedValues = Arrays.asList("Reports", "View Details");

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

		checkToolTipDBS(index);
		checkTimesDecending();
	}

	private void openDataBearerSession(String imsi) throws InterruptedException {
		sessionbrowserTab.openTab();
		waitForPageToLoad();
		sessionbrowserTab.openDetailsSessionBrowser2Hour(imsi);
		// sessionbrowserTab.openCustomDateAndTime("2012 Dec", "2012 Dec", "1",
		// "31");
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	private void checkForDBS() {
		List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
	
		assertTrue("Data Bearer Sessions are not displayed",
				sessionbrowserTab.isEventFound("Data Bearer Session",
						sessionTable));
	}

	private void checkTimesDecending() throws ParseException {

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

	// *********** PRIVATE METHODS ***********//
	private void checkToolTipDBS(int index) {

		String tooltip = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']//tr[@__index='"
								+ index + "']/td[3]")).getAttribute("title");

		assertTrue("There is no Start cell tool tip for DBS",
				tooltip.contains("Start Cell"));
		assertTrue("There is no End cell tool tip for DBS",
				tooltip.contains("End Cell"));
		assertTrue("There is no Cell Count tool tip for DBS",
				tooltip.contains("Cell Count"));

	}

	private List<WebElement> getSubscriberIdentitySection(int index) {
		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
						+ (2 + index) + "]/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]"))
				.getAttribute("style").isEmpty();

		if (!sectionVisiable) {
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]//span[2]"))
					.click();
		}

		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]//tr/td[1]"));
	}

	private List<WebElement> getNetworkLocationSection(int index) {
		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
						+ (2 + index) + "]/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]"))
				.getAttribute("style").isEmpty();

		if (!sectionVisiable) {
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]//span[2]"))
					.click();
		}
		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]//tr/td[1]"));
	}

	private List<WebElement> getSessionPropertiesSection(int index) {
		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
						+ (2 + index) + "]/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]"))
				.getAttribute("style").isEmpty();

		if (!sectionVisiable) {
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]//span[2]"))
					.click();
		}
		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]//tr/td[1]"));
	}

	private List<WebElement> getMobilitySection(int index) {
		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
						+ (2 + index) + "]/td[2]")).click();
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

		boolean sectionVisiable = driver
				.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[5]/div[2]"))
				.getAttribute("style").isEmpty();

		if (!sectionVisiable) {
			scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[5]//span[2]")));
			driver.findElement(
					By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[5]//span[2]"))
					.click();
			scrollViewDetailsSectionList(false,36);
		}
		return driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[5]/div[2]//tr/td[1]"));
	}

	private boolean noDataAvailable() {
		return driver
				.findElement(
						By.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]//div[text()='No Data Available']"))
				.isDisplayed();
	}
	
	private String getStartCell(String tooltip) {

		return tooltip.split(":")[1].split(",")[0].trim();
	}

	private String getEndCell(String tooltip) {

		return tooltip.split(":")[2].split(",")[0].trim();
	}

	private String getValueInDetailsTable(String find,
			List<WebElement> detailsTable) {
		for (int i = 0; i < detailsTable.size(); i++) {
			// System.out.println("details: " + detailsTable.get(i).getText()
			// + "\nfind: " + find);
			if (find.equals(detailsTable.get(i).getText())) {
				// System.out.println("i: " + detailsTable.get(i).getText()
				// + "\ni+1: " + detailsTable.get(i + 1).getText() + "\n");
				return detailsTable.get(i).getText();
			}
		}
		return null;
	}

	private List<WebElement> getDetailsTable() {
		return driver
				.findElements(By
						.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]//table[@class='summaryGrid']/tbody/tr/td"));
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

	private String getDBSEventName(List<WebElement> table) {

		String dbsValue = "Data Bearer Session";
		for (int i = 0; i < table.size(); i++) {
			String text = table.get(i).getText().trim();
			if (dbsValue.equals(text)) {
				return text;
			}
		}
		return null;
	}
}
