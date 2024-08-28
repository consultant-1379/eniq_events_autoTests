package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.DataBaseConnection;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class RanSignallingTestGroup extends SBWebDriverBaseUnitTest {

	@Autowired
	private SessionBrowserTab sessionbrowserTab;

	private final DataBaseConnection database = new DataBaseConnection();

	private final String INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME = "Int Failed HSDSCH Cell Change";

	private final String INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_ACTUAL_NAME = "Internal Failed HSDSCH Cell Change";

	private final String INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME = "Int Successful HSDSCH Cell Change";

	private final String INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_ACTUAL_NAME = "Internal Successful HSDSCH Cell Change";

	private final String INT_SOFT_HO_EXEC_EVENT_UI_NAME = "Int Soft Handover Execution Failure";

	private final String INT_SOFT_HO_EXEC_EVENT_ACTUAL_NAME = "Internal Soft Handover Execution Failure";

	private final String INT_SYSTEM_RELEASE = "Internal System Release";

	private final String RRC_MEAS_REPORT = "RRC Measurement Report";

	/**
	 * US: DEFTFTS-357 Test Case 7.124: verify that a user is able to see
	 * internal Failed HSDSCH Cell Change event on the session list entry panel
	 * in the session browser details tab
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testViewIntFailedHSDSCHCellChange_7_124()
			throws InterruptedException, ParseException {

		// Action 1
		openSessionDetail(CommonDataType.SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE, true, false);

		// Action 2
		final Boolean eventFound = isRanEventFound(INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME);
		assertTrue("No " + INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME
				+ " event found for selected IMSI", eventFound);

		checkForEvent(INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME, false);

	}

	/**
	 * US: DEFTFTS-357 Test case 4.7.125: Verify that a user is able to launch
	 * View Details Window for Internal Failed HSDSCH Cell Change Event on the
	 * Session List entry panel in the Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws ParseException 
	 */
	@Test
	public void testOpenIntFailedHSDSCHCellChange_7_125()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		assertFalse("No events in Session List", sessionTable.isEmpty());

		final int index = sessionbrowserTab.getRecordPosition(
				INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME, sessionTable);
		assertTrue("Int Failed HSDSCH Cell Change Event not found in list",
				isRanEventFound(INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME));

		openViewDetails(index);

		Thread.sleep(1000);
		assertTrue(
				"Clicking on view details does not launch separate window",
				driver.findElement(
						By.xpath("//div[contains(@class,'dragdrop-handle')]"))
						.isDisplayed());

		// Action 3
		final WebElement titleBar = driver
				.findElement(By
						.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
		assertTrue(
				"Window title does not match Event name in session list",
				titleBar.getText().contains(
						INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_ACTUAL_NAME));

		final String sessionTimeOnList = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
								+ index + "']/td[1]")).getText();
		assertTrue("Window time does not match Event time in session list",
				titleBar.getText().contains(sessionTimeOnList));
	}

	/**
	 * US: DEFTFTS-357 Test case 7.126: verify that a user is able to launch
	 * view details window for Internal HSDSCH Cell Change event on the session
	 * list entry panel in the session browser details tab
	 * 
	 * @throws InterruptedException
	 * @throws ParseException 
	 */
	@Test
	public void checkSectionsForIntFailedHSDSCHCellChangeEvent_7_126()
			throws InterruptedException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE, true, false);
		
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		assertFalse("No events in Session List", sessionTable.isEmpty());

		final int index = RAN_SignallingPosition(sessionTable,
				INT_FAILED_HSDSCH_CELL_CHANGE_EVENT_UI_NAME);

		if (index >= 0) {
			openViewDetails(index);

			final List<String> expectedValues = Arrays.asList(
					"Subscriber Identity", "Handover Details", "Event Details",
					"Active Set Details");
			final List<String> actualValues = getViewDetailsHeadings();

			// Action 1
			assertTrue(expectedValues.equals(actualValues));

			// Action 2
			assertTrue(
					"Scroll bar not positioned at the top of the session list pane",
					sessionbrowserTab.getSessionListScrollBarPosition() == 0);

			// Action 3 - Will be tested later along with persistence

			// Action 4
			scrollViewDetailsSectionList(true,100);
			final List<WebElement> visiableSections = driver
					.findElements(By
							.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));

			for (int i = 1; i <= visiableSections.size(); i++) {
				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();

				scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}

			scrollViewDetailsSectionList(true,100);
			for (int i = 1; i <= visiableSections.size(); i++) {
				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();
				
				scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}
			// Action 5 - Covered in DEFTFTS-13

		}
	}

	/**
	 * US: DEFTFTS-158 Test Case 7.131: verify that a user is able to see
	 * Internal Successful HSDSCH Cell Change event on the session list entry
	 * panel in the session browser details tab
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testViewIntSuccHSDSCHCellChange_7_131()
			throws InterruptedException, ParseException {

		// Action 1
		openSessionDetail(CommonDataType.SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE, true, false);

		// Action 2
		final Boolean eventFound = isRanEventFound(INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME);
		assertTrue("No " + INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME
				+ " event found for selected IMSI", eventFound);

		checkForEvent(INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME, false);
	}

	/**
	 * US: DEFTFTS-158 Test Case 7.132 : Verify that a user is able to launch
	 * View Details Window for Internal Successful HSDSCH Cell Change Event on
	 * the Session List entry panel in the Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testOpenIntSuccHSDSCHCellChange_7_132()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		assertFalse("Session List is empty", sessionTable.isEmpty());

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME, sessionTable);
		assertTrue("Int Successful HSDSCH Cell Change Event not found in list",
				sessionbrowserTab
						.isEventFound(
								INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME,
								sessionTable));
		openViewDetails(index);

		Thread.sleep(1000);
		assertTrue(
				"Clicking on view details does not launch separate window",
				driver.findElement(
						By.xpath("//div[contains(@class,'dragdrop-handle')]"))
						.isDisplayed());

		// Action 3
		final WebElement titleBar = driver
				.findElement(By
						.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
		assertTrue(
				"Window title does not match Event name in session list",
				titleBar.getText().contains(
						INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_ACTUAL_NAME));

		final String sessionTimeOnList = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
								+ index + "']/td[1]")).getText();
		assertTrue("Window time does not match Event time in session list",
				titleBar.getText().contains(sessionTimeOnList));
	}

	/**
	 * US: DEFTFTS-158 Test case 7.133: Verify the Sections of the View Details
	 * Window for Internal Successful HSDSCH Cell Change Event on the Session
	 * List entry panel in the Session Browser Details Tab
	 * 
	 * @throws InterruptedException
	 * @throws ParseException 
	 */
	@Test
	public void checkSectionsForIntSuccHSDSCHCellChange_7_133()
			throws InterruptedException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		assertFalse("Session List is empty", sessionTable.isEmpty());

		final int index = RAN_SignallingPosition(sessionTable,
				INT_SUCC_HSDSCH_CELL_CHANGE_EVENT_UI_NAME);

		if (index >= 0) {
			openViewDetails(index);

			final List<String> expectedValues = Arrays.asList(
					"Subscriber Identity", "Handover Details", "Event Details",
					"Active Set Details");
			final List<String> actualValues = getViewDetailsHeadings();

			// Action 1
			assertTrue(expectedValues.equals(actualValues));

			// Action 2
			assertTrue(
					"Scroll bar not positioned at the top of the session list pane",
					sessionbrowserTab.getSessionListScrollBarPosition() == 0);

			// Action 3 - Will be tested later along with persistence

			// Action 4
			scrollViewDetailsSectionList(true,100);
			final List<WebElement> visiableSections = driver
					.findElements(By
							.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));

			for (int i = 1; i <= visiableSections.size(); i++) {

				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();
				
				scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}

			scrollViewDetailsSectionList(true,100);
			for (int i = 1; i <= visiableSections.size(); i++) {

				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();
				scrollViewDetailsSectionListUntilElementVisible(false,driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ i + "]//span[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}
			// Action 5 - Covered in DEFTFTS-13

		}
	}

	/* This test appears to duplicate 7.70 */
	/*
	 * DEFTFTS-356
	 * Test Case 4.7.117: verify that a user is able to see Internal Soft
	 * Handover Execution Failure event on the session list entry panel in the
	 * session browser details tab
	 */
	@Ignore
	@Test
	public void testViewIntSoftHoExecFailure_7_117()
			throws InterruptedException, ParseException {

		// Action 1
		openSessionDetail(CommonDataType.SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		// Action 2
		final Boolean eventFound = sessionbrowserTab.isEventFound(
				INT_SOFT_HO_EXEC_EVENT_UI_NAME, sessionTable);

		assertTrue("No " + INT_SOFT_HO_EXEC_EVENT_UI_NAME
				+ " event found for selected IMSI", eventFound);

	}

	/*
	 * DEFTFTS-356
	 * Test case 4.7.118: Verify that a user is able to launch View Details
	 * Window for Internal Soft Handover Execution Failure Event on the Session
	 * List entry panel in the Session Browser Details Tab.
	 */
	@Test
	public void testRanSignallingIntSoftHoExec_7_118()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, true, false);

		WebElement value = null;

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		assertFalse("Session List is empty", sessionTable.isEmpty());

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SOFT_HO_EXEC_EVENT_UI_NAME, sessionTable);
		assertTrue(
				"Int Soft Handover Execution Failure Event not found in list",
				index >= 0);

		final List<String> expectedOptions = Arrays.asList("Reports",
				"View Details");

		driver.findElement(
				By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
						+ (index) + "']/td[2]")).click();
		
		final List<WebElement> options = driver.findElements(By
				.xpath(Constants.DROPDOWN_MENU_CONTENT));

		// Action 1
		for (int i = 0; i < options.size(); i++) {
			value = options.get(i);
			assertTrue("The value " + value.getText()
					+ " was not found in the drop down menu", expectedOptions
					.get(i).equals((value.getText())));
		}

		// Action 2
		driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
		Thread.sleep(1000);
		assertTrue(
				"Clicking on view details does not launch separate window",
				driver.findElement(
						By.xpath("//div[contains(@class,'dragdrop-handle')]"))
						.isDisplayed());

		// Action 3
		final WebElement titleBar = driver
				.findElement(By
						.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));

		assertTrue("Window title does not match Event name in session list",
				titleBar.getText().contains(INT_SOFT_HO_EXEC_EVENT_ACTUAL_NAME));

		final String sessionTimeOnList = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
								+ (index) + "']/td[1]")).getText();
		
		assertTrue("Window time does not match Event time in session list",
				titleBar.getText().contains(sessionTimeOnList));
	}

	/*
	 * DEFTFTS-356 Test case 4.7.119: verify that a user is able to launch view
	 * details window for Internal Soft Handover Execution Failure event on the
	 * session list entry panel in the session browser details tab
	 */
	@Test
	public void checkSectionsIntSoftHandoverExec_7_119()
			throws InterruptedException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		final Boolean eventFound = sessionbrowserTab.isEventFound(
				INT_SOFT_HO_EXEC_EVENT_UI_NAME, sessionTable);

		assertTrue("No " + INT_SOFT_HO_EXEC_EVENT_UI_NAME
				+ " event found for selected IMSI", eventFound);

		final int index = RAN_SignallingPosition(sessionTable,
				INT_SOFT_HO_EXEC_EVENT_UI_NAME);

		if (index >= 0) {
			openViewDetails(index);
			
			final List<String> expectedValues = Arrays.asList(
					"Subscriber Identity", "Handover Details", "Event Details",
					"Active Set Details");
			final List<String> actualValues = getViewDetailsHeadings();

			// Action 1
			assertTrue(expectedValues.equals(actualValues));

			// Action 2
			assertTrue(
					"Scroll bar not positioned at the top of the session list pane",
					sessionbrowserTab.getSessionListScrollBarPosition() == 0);

			// Action 3 - Will be tested later along with persistence

			// Action 4
			final List<WebElement> visiableSections = driver
					.findElements(By
							.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));
			scrollViewDetailsSectionList(true,100);
			for (int i = 1; i <= visiableSections.size(); i++) {

				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();
				scrollViewDetailsSectionListUntilElementVisible(false,driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}

			for (int i = 1; i <= visiableSections.size(); i++) {

				final boolean visiable = driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]"))
						.getAttribute("style").isEmpty();

				scrollViewDetailsSectionListUntilElementVisible(false,driver
						.findElement(
								By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
										+ i + "]/div[2]")));
				if (visiable) {
					// Section is expanded so collapse it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertFalse(
							"The expanded heading did not collapse : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2]"))
									.getAttribute("style").isEmpty());

				} else {
					// Section is collapse so expand it
					driver.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ i + "]//span[2]")).click();
					assertTrue(
							"The collapsed heading did not expand : " + i,
							driver.findElement(
									By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
											+ i + "]/div[2][@style='']"))
									.isDisplayed());
				}
			}
			// Action 5 - Covered in DEFTFTS-13

		}
	}

	/*
	 * DEFTFTS-881 Test Case 4.4.1: Data Bearer Session - View Details Verify
	 * Compressed Mode parameters
	 */
	@Ignore
	@Test
	public void testCompressedModeParameters_4_4_1() throws SQLException,
			InterruptedException {

		final String IMSI = "204042270452610";

		final String query = "SELECT TOP 10 "
				+ "CM_CNT, CM_DURATION, CM_UL_CNT, CM_DL_CNT, CM_ULDL_CNT, CM_USER_CNT "
				+ "FROM EVENT_E_RAN_SESSION_RAW WHERE IMSI = " + IMSI;

		sessionbrowserTab.openEventDetails(IMSI);
		int index = 0;

		database.openConnection();

		final ResultSet rs = database.executeQuery(query);
		while (rs.next()) {
			final String database_CM_Count = rs.getString(1);
			final String database_CM_DURATION = rs.getString(2);
			final String database_CM_UL_CNT = rs.getString(3);
			final String database_CM_DL_CNT = rs.getString(4);
			final String database_CM_ULDL_CNT = rs.getString(5);
			final String database_CM_USER_CNT = rs.getString(6);

			// open view details
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + index++) + "]/td[2]")).click();

			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
			sessionbrowserTab.waitForPageToLoad();

			// get UI values
			final boolean sectionVisiable = driver
					.findElement(
							By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
									+ 5 + "]/div[2]")).getAttribute("style")
					.isEmpty();

			if (!sectionVisiable) {
				driver.findElement(
						By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
								+ 5 + "]//span[2]")).click();
			}

			final String UI_CM_Count = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[12]/td[2]/div"))
					.getText();
			final String UI_CM_Duration = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[13]/td[2]/div"))
					.getText();
			final String UI_CM_UL_CNT = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[14]/td[2]/div"))
					.getText();
			final String UI_CM_DL_CNT = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[15]/td[2]/div"))
					.getText();

			final String UI_CM_ULDL_CNT = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[16]/td[2]/div"))
					.getText();

			final String UI_CM_USER_CNT = driver
					.findElement(
							By.xpath(Constants.VIEW_DETAILS_CONTENTS
									+ "//div[1]/div[5]/div[2]/table/tbody/tr[17]/td[2]/div"))
					.getText();

			driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW_CLOSE))
					.click();

			// compare to db
			assertTrue("Database and UI values are not equal for CM_CNT",
					UI_CM_Count.equals(database_CM_Count));
			assertTrue("Database and UI values are not equal for CM_DURATION",
					UI_CM_Duration.equals(database_CM_DURATION));
			assertTrue("Database and UI values are not equal for CM_UL_CNT",
					UI_CM_UL_CNT.equals(database_CM_UL_CNT));
			assertTrue("Database and UI values are not equal for CM_DL_CNT",
					UI_CM_DL_CNT.equals(database_CM_DL_CNT));
			assertTrue("Database and UI values are not equal for CM_ULDL_CNT",
					UI_CM_ULDL_CNT.equals(database_CM_ULDL_CNT));
			assertTrue("Database and UI values are not equal for CM_USER_CNT",
					UI_CM_USER_CNT.equals(database_CM_USER_CNT));
		}

		database.openConnection();

	}

	/**
	 * User Story: DEFTFTS-154 Test Case 7.70: Verify that a user is able to see
	 * Internal Soft Handover Execution Failure Event in the Session List entry
	 * panel in the Session Browser Details Tab SQL: select imsi from
	 * EVENT_E_RAN_HFA_SOHO_ERR_RAW where event_id=408
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testInternalSoftHandoverExecutionFailure_7_70()
			throws InterruptedException, ParseException {
		final String INTERNAL_SOFT_HANDOVER_EXECUTION_FAILURE_UI_NAME = "Int Soft Handover Execution Failure";

		openSessionDetail(CommonDataType.SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, true, false);

		checkForEvent(INTERNAL_SOFT_HANDOVER_EXECUTION_FAILURE_UI_NAME, false);
	}

	/**
	 * US: DEFTFTS-154 Test Case 7.71: Verify that a user is able to see
	 * Internal IFHO Handover Execution Failure Event in the Session List entry
	 * panel in the Session Browser Details Tab SQL: select imsi from
	 * EVENT_E_RAN_HFA_IFHO_ERR_RAW where event_id=423
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testInternalIfhoHandoverExecutionFailure_7_71()
			throws InterruptedException, ParseException {
		final String INTERNAL_IFHO_HANDOVER_EXECUTION_FAILURE_UI_NAME = "Int IFHO Handover Execution Failure";

		openSessionDetail(CommonDataType.SB_IMSI_INT_IFHO_HANDOVER_EXECUTION_FAILURE, true, false);
		
		checkForEvent(INTERNAL_IFHO_HANDOVER_EXECUTION_FAILURE_UI_NAME, false);
	}

	/**
	 * US: DEFTFTS-154 Test Case 4.7.72: Verify that a user is able to see
	 * Internal Failed HSDSCH Cell Change Event in the Session List entry panel
	 * in the Session Browser Details Tab
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testInternalFailedHSDSCHCellChange_7_72()
			throws InterruptedException, ParseException {
		final String INTERNAL_FAILED_HSDSCH_CELL_CHANGE_UI_NAME = "Int Failed HSDSCH Cell Change";

		openSessionDetail(CommonDataType.SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE, true, false);
		checkForEvent(INTERNAL_FAILED_HSDSCH_CELL_CHANGE_UI_NAME, false);
	}

	/**
	 * US: DEFTFTS-154 Test Case 4.7.73: : Verify that a user is able to see
	 * Internal HSDSCH No Cell Selected Event in the Session List entry panel in
	 * the Session Browser Details Tab SQL: select imsi from
	 * EVENT_E_RAN_HFA_HSDSCH_ERR_RAW where event_id=436
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Test
	public void testInternalHSDSCHNoCellSelected_7_73()
			throws InterruptedException, ParseException {
		final String INTERNAL_HSDSCH_NO_CELL_SELECTED_UI_NAME = "Int HSDSCH No Cell Selected";

		//** NOTE: THIS SHOULD BE SB_IMSI_INT_HSDSCH_NO_CELL_SELECTED
		openSessionDetail(CommonDataType.SB_IMSI_INT_HSDSCH_NO_CELL_SELECTED, true, false);

		checkForEvent(INTERNAL_HSDSCH_NO_CELL_SELECTED_UI_NAME, false);
	}

	/**
	 * Test Case 7.83: Verify that a user is able to see Internal System Release
	 * Event on the Session List entry panel in the Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException 
	 */
	@Test
	public void testInternalSystemReleaseEvent_7_83()
			throws InterruptedException, PopUpException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);
		zoom(false,5);
		final List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(table, INT_SYSTEM_RELEASE);
	}

	/**
	 * Test Case 7.84: Verify that a user is able to launch View Details Window
	 * for Internal System Release Event on the Session List entry panel in the
	 * Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 */
	@Test
	public void testInternalSystemReleaseEvent_7_84()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);
		zoom(false,6);
		
		final List<WebElement> table = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(table, INT_SYSTEM_RELEASE);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, table);
		final String firstEventName = sessionbrowserTab.getEventName(
				INT_SYSTEM_RELEASE, table);

		if (index >= 0) {
			final List<String> expectedOptions = Arrays.asList("Reports",
					"View Details");
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='"
							+ (index) + "']/td[2]")).click();

			final List<WebElement> options = driver.findElements(By
					.xpath(Constants.DROPDOWN_MENU_CONTENT));

			// Action 1
			for (int i = 0; i < options.size(); i++) {
				final WebElement value = options.get(i);
				assertTrue("The value " + value.getText()
						+ " was not found in the drop down menu",
						expectedOptions.get(i).equals((value.getText())));
			}

			// Action 2
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
			Thread.sleep(1000);
			assertTrue(
					"Clicking on view details does not launch separate window",
					driver.findElement(
							By.xpath("//div[contains(@class,'dragdrop-handle')]"))
							.isDisplayed());

			// Action 3
			final WebElement titleBar = driver
					.findElement(By
							.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
			assertTrue(
					"Window title does not match Event name in session list",
					titleBar.getText().contains(firstEventName));

			final String sessionTimeOnList = driver
					.findElement(
							By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
									+ (2 + index) + "]/td[1]")).getText();
			assertTrue("Window time does not match Event time in session list",
					titleBar.getText().contains(sessionTimeOnList));
		}
	}

	/**
	 * Test Case 7.85: Verify the Sections of the View Details Window for
	 * Internal System Release Event on the Session List entry panel in the
	 * Session Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException 
	 */
	@Test
	public void testInternalSystemReleaseEvent_7_85()
			throws InterruptedException, PopUpException, ParseException {

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);
		zoom(false,5);
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, INT_SYSTEM_RELEASE);
		zoom(true,5);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, sessionTable);

		if (index >= 0) {
			openViewDetails(index);

			if (driver.findElement(
					By.xpath("//div[contains(@class,'dragdrop-handle')]"))
					.isDisplayed()) {

				// Action 1
				final List<String> expectedSections = Arrays.asList(
						"Subscriber Identity", "Network Location",
						"Event Details", "Active Set Measurements");
				final List<String> sections = getViewDetailsHeadings();
				scrollViewDetailsSectionList(true,50);

				
				for (int i = 0; i < sections.size(); i++) {
					assertTrue("The section " + expectedSections.get(i)
							+ " was not found in the view details popup",
							expectedSections.get(i).equals(sections.get(i)));
				}

				// Action 2
				if (driver.findElement(
						By.xpath(Constants.VIEW_DETAILS_SCROLLBAR))
						.isDisplayed()) {
					// get if scrollbar is at the top
					assertTrue(
							"View Details scrollbar is not at the top",
							getViewDetailsScrollBarPosition(driver.findElement(
									By.xpath(Constants.VIEW_DETAILS_SCROLLBAR))
									.getAttribute("style")) == 0);
				}

				// Action 4
				toggleSectionsOfViewDetailsPopup();
				scrollViewDetailsSectionList(true,50);
				toggleSectionsOfViewDetailsPopup();
			}
		}
	}

	/**
	 * Test Case 7.86: Verify the Subscriber Identity Section of the View
	 * Details Window for Internal System Release Event in the Session Browser
	 * Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws ParseException
	 * @throws SQLException
	 */
	@Ignore
	@Test
	public void testInternalSystemReleaseEvent_7_86()
			throws InterruptedException, PopUpException, ParseException,
			SQLException {

		String uiIMSI = null, uiMSISDN = null, uiTerminal = null, uiNetwork = null;
		String dbIMSI = null, dbMSISDN = null, dbTAC = null, dbNetwork = null;

		database.openConnection();

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);
		
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, INT_SYSTEM_RELEASE);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, sessionTable);

		if (index >= 0) {
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + index) + "]/td[2]")).click();
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

			final List<WebElement> section = getSectionHeadings(1);

			final List<String> expectedValues = Arrays.asList("MSISDN", "IMSI",
					"Terminal", "Network");

			for (int i = 0; i < section.size(); i++) {
				final String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}

		final List<WebElement> subscriberIdentityValues = driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]//tr/td[2]"));

		if (!subscriberIdentityValues.isEmpty()) {
			uiMSISDN = subscriberIdentityValues.get(0).getText();
			uiIMSI = subscriberIdentityValues.get(1).getText();
			uiTerminal = subscriberIdentityValues.get(2).getText();
			uiNetwork = subscriberIdentityValues.get(3).getText();
		}

		// Get event time from session list
		final String[] sessionTime = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + index) + "]/td[1]")).getText()
				.split(":");
		final int hours = Integer.parseInt(sessionTime[0]);
		final int mins = Integer.parseInt(sessionTime[1]);
		final int secs = Integer.parseInt(sessionTime[2]);

		final String query = "SELECT TOP 1 IMSI, MSISDN, TAC, DIM_E_SGEH_MCCMNC.COUNTRY, DIM_E_SGEH_MCCMNC.OPERATOR FROM EVENT_E_RAN_CFA_ERR_RAW, DIM_E_SGEH_MCCMNC "
				+ "WHERE IMSI = "
				+ Constants.IMSI_CFA_EVENT
				+ " "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.IMSI_MCC = DIM_E_SGEH_MCCMNC.MCC "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.IMSI_MNC = DIM_E_SGEH_MCCMNC.MNC "
				+ "AND EVENT_TIME >= '2012-05-16 "
				+ (hours - 1)
				+ ":"
				+ mins
				+ ":"
				+ secs
				+ ".000' "
				+ "AND EVENT_TIME <= '2012-05-16 "
				+ (hours - 1) + ":" + mins + ":" + secs + ".999'";

		final ResultSet rs = database.executeQuery(query);
		while (rs.next()) {
			dbIMSI = rs.getString(1);
			dbMSISDN = rs.getString(2) == null ? "-" : rs.getString(2);
			dbTAC = rs.getString(3) == null ? "-" : rs.getString(3);
			dbNetwork = rs.getString(4) + "," + rs.getString(5);
		}
		assertTrue("Value on the UI: " + uiIMSI
				+ " does not match the database!", dbIMSI.equals(uiIMSI));
		assertTrue("Value on the UI: " + uiMSISDN
				+ " does not match the database!", dbMSISDN.equals(uiMSISDN));
		assertTrue("Value on the UI: " + uiTerminal
				+ " does not match the database!", dbTAC.equals(uiTerminal));
		assertTrue("Value on the UI: " + uiNetwork
				+ " does not match the database!", dbNetwork.equals(uiNetwork));

		database.closeConnection();
	}

	/**
	 * Test Case 7.87: Verify the Network Location Section of the View Details
	 * Window for Internal System Release Event in the Session Browser Details
	 * Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Ignore
	@Test
	public void testInternalSystemReleaseEvent_7_87()
			throws InterruptedException, PopUpException, SQLException, ParseException {
		String uiController = null, uiPLMN = null, uiLocationArea = null, uiRoutingArea = null, uiCellId = null, uiCID = null;
		String dbController = null, dbPLMN = null, dbLocationArea = null, dbRoutingArea = null, dbCellId = null, dbCID = null;

		database.openConnection();

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, INT_SYSTEM_RELEASE);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, sessionTable);

		if (index >= 0) {
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + index) + "]/td[2]")).click();
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();

			final List<WebElement> section = getSectionHeadings(2);

			final List<String> expectedValues = Arrays.asList("Controller",
					"PLMN", "Location Area", "Routing Area", "Cell Id", "CID");

			for (int i = 0; i < section.size(); i++) {
				final String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
		final List<WebElement> networkLocationValues = driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]//tr/td[2]"));

		if (!networkLocationValues.isEmpty()) {
			uiController = networkLocationValues.get(0).getText();
			uiPLMN = networkLocationValues.get(1).getText();
			uiLocationArea = networkLocationValues.get(2).getText();
			uiRoutingArea = networkLocationValues.get(3).getText();
			uiCellId = networkLocationValues.get(4).getText();
			uiCID = networkLocationValues.get(5).getText();
		}

		// Get event time from session list
		final String[] sessionTime = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + index) + "]/td[1]")).getText()
				.split(":");
		final int hours = Integer.parseInt(sessionTime[0]);
		final int mins = Integer.parseInt(sessionTime[1]);
		final int secs = Integer.parseInt(sessionTime[2]);

		final String query = "SELECT TOP 1 DIM_E_SGEH_HIER321.HIERARCHY_3, IMSI_MCC, IMSI_MNC, EVENT_E_RAN_CFA_ERR_RAW.LAC, EVENT_E_RAN_CFA_ERR_RAW.RAC, DIM_E_SGEH_HIER321_CELL.CELL_ID, C_ID_1  from EVENT_E_RAN_CFA_ERR_RAW, DIM_E_SGEH_HIER321, DIM_E_SGEH_HIER321_CELL "
				+ "WHERE IMSI = "
				+ Constants.IMSI_CFA_EVENT
				+ " "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.HIER3_ID = DIM_E_SGEH_HIER321.HIER3_ID "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.HIER3_CELL_ID = DIM_E_SGEH_HIER321_CELL.HIER3_CELL_ID "
				+ "AND EVENT_TIME >= '2012-05-16 "
				+ (hours - 1)
				+ ":"
				+ mins
				+ ":"
				+ secs
				+ ".000' "
				+ "AND EVENT_TIME <= '2012-05-16 "
				+ (hours - 1) + ":" + mins + ":" + secs + ".999'";

		final ResultSet rs = database.executeQuery(query);
		while (rs.next()) {
			dbController = rs.getString(1);

			dbPLMN = rs.getString(2) + rs.getString(3);
			dbLocationArea = rs.getString(4);
			dbRoutingArea = rs.getString(5);
			dbCellId = rs.getString(6);
			dbCID = rs.getString(7);
		}

		assertTrue("Value on the UI: " + uiController
				+ " does not match the database!",
				dbController.equals(uiController));
		assertTrue("Value on the UI: " + uiPLMN
				+ " does not match the database!", dbPLMN.equals(uiPLMN));
		assertTrue("Value on the UI: " + uiLocationArea
				+ " does not match the database!",
				dbLocationArea.equals(uiLocationArea));
		assertTrue("Value on the UI: " + uiRoutingArea
				+ " does not match the database!",
				dbRoutingArea.equals(uiRoutingArea));
		assertTrue("Value on the UI: " + uiCellId
				+ " does not match the database!", dbCellId.equals(uiCellId));
		assertTrue("Value on the UI: " + uiCID
				+ " does not match the database!", dbCID.equals(uiCID));

		database.closeConnection();
	}

	/**
	 * Test Case 7.88: Verify the Event Details Section of the View Details
	 * Window for Internal System Release Event in the Session Browser Details
	 * Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Ignore
	@Test
	public void testInternalSystemReleaseEvent_7_88()
			throws InterruptedException, PopUpException, SQLException, ParseException {
		String uiRabType = null, uiRabTypeReconfigure = null, uiTriggerPoint = null, uiUTRAN_RANAP_Cause = null, uiCN_RANAP_Cause = null, uiGBRUplink = null, uiGBRDownlink = null, uiServingHSDSCHCellId = null, uiServingHSDSCHCID = null, uiServingHSDSCHRNCId = null, uiProcedureIndicator = null, uiCauseValue = null, uiExtendedCauseValue = null, uiEvaluationCase = null, uiExceptionClass = null, uiDisconnectionDescription = null, uiDisconnectionCode = null, uiDisconnectionSubCode = null, uiSourceConnectionProp = null, uiTargetConnectionProp = null, uiWantedConnectionProp = null;

		String dbRabType = null, dbRabTypeReconfigure = null, dbTriggerPoint = null, dbUTRAN_RANAP_Cause = null, dbCN_RANAP_Cause = null, dbGBRUplink = null, dbGBRDownlink = null, dbServingHSDSCHCellId = null, dbServingHSDSCHCID = null, dbServingHSDSCHRNCId = null, dbProcedureIndicator = null, dbCauseValue = null, dbExtendedCauseValue = null, dbEvaluationCase = null, dbExceptionClass = null, dbDisconnectionDescription = null, dbDisconnectionCode = null, dbDisconnectionSubCode = null, dbSourceConnectionProp = null, dbTargetConnectionProp = null, dbWantedConnectionProp = null;

		database.openConnection();

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, INT_SYSTEM_RELEASE);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, sessionTable);
		if (index >= 0) {
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + index) + "]/td[2]")).click();
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
			final List<WebElement> section = getSectionHeadings(3);

			final List<String> expectedValues = Arrays.asList("Rab Type",
					"Rab Type (Attempted Reconfigure)", "Trigger Point",
					"UTRAN RANAP Cause", "CN RANAP Cause", "GBR Uplink",
					"GBR Downlink", "Serving HSDSCH Cell Id",
					"Serving HSDSCH CID", "Serving HSDSCH Cell RNC Id",
					"Procedure Indicator", "Cause Value",
					"Extended Cause Value", "Evaluation Case",
					"Exception Class", "Disconnection Description",
					"Disconnection Code", "Disconnection Sub Code",
					"Source Connection Prop", "Target Connection Prop",
					"Wanted Connection Prop");

			for (int i = 0; i < section.size(); i++) {
				final String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}

		final List<WebElement> eventDetailsValues = driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]//tr/td[2]"));
		if (!eventDetailsValues.isEmpty()) {
			uiRabType = eventDetailsValues.get(0).getText();
			uiRabTypeReconfigure = eventDetailsValues.get(1).getText();
			uiTriggerPoint = eventDetailsValues.get(2).getText();
			uiUTRAN_RANAP_Cause = eventDetailsValues.get(3).getText();
			uiCN_RANAP_Cause = eventDetailsValues.get(4).getText();
			uiGBRUplink = eventDetailsValues.get(5).getText();
			uiGBRDownlink = eventDetailsValues.get(6).getText();
			uiServingHSDSCHCellId = eventDetailsValues.get(7).getText();
			uiServingHSDSCHCID = eventDetailsValues.get(8).getText();
			uiServingHSDSCHRNCId = eventDetailsValues.get(9).getText();
			uiProcedureIndicator = eventDetailsValues.get(10).getText();
			uiCauseValue = eventDetailsValues.get(11).getText();
			uiExtendedCauseValue = eventDetailsValues.get(12).getText();
			uiEvaluationCase = eventDetailsValues.get(13).getText();
			uiExceptionClass = eventDetailsValues.get(14).getText();
			uiDisconnectionDescription = eventDetailsValues.get(15).getText();
			uiDisconnectionCode = eventDetailsValues.get(16).getText();
			uiDisconnectionSubCode = eventDetailsValues.get(17).getText();
			uiSourceConnectionProp = eventDetailsValues.get(18).getText();
			uiTargetConnectionProp = eventDetailsValues.get(19).getText();
			uiWantedConnectionProp = eventDetailsValues.get(20).getText();
		}

		// Get event time from session list
		final String[] sessionTime = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + index) + "]/td[1]")).getText()
				.split(":");
		final int hours = Integer.parseInt(sessionTime[0]);
		final int mins = Integer.parseInt(sessionTime[1]);
		final int secs = Integer.parseInt(sessionTime[2]);

		String query = "SELECT TOP 1 DIM_E_RAN_RABTYPE.RABTYPE_DESC, DIM_E_RAN_CFA_TRIGGER_POINT.TRIGGER_POINT_DESC, DIM_E_RAN_CFA_UTRAN_RANAP_CAUSE.UTRAN_RANAP_CAUSE_DESC, "
				+ "EVENT_E_RAN_CFA_ERR_RAW.CN_RANAP_CAUSE, GBR_UL, GBR_DL, DIM_E_SGEH_HIER321_CELL.CELL_ID, C_ID_SERV_HSDSCH_CELL, DIM_E_SGEH_HIER321_CELL.HIERARCHY_3, PROCEDURE_INDICATOR_DESC, CAUSE_VALUE_DESC, EXTENDED_CAUSE_VALUE_DESC, EVALUATION_CASE_DESC, EXCEPTION_CLASS_DESC, DISCONNECTION_DESC, RAN_DISCONNECTION_CODE, RAN_DISCONNECTION_SUBCODE "
				+ "FROM EVENT_E_RAN_CFA_ERR_RAW, DIM_E_RAN_CFA_TRIGGER_POINT, DIM_E_RAN_RABTYPE,DIM_E_RAN_CFA_UTRAN_RANAP_CAUSE, DIM_E_RAN_CFA_PROCEDURE_INDICATOR, DIM_E_RAN_CFA_CAUSE_VALUE, DIM_E_RAN_CFA_EXTENDED_CAUSE_VALUE, DIM_E_SGEH_HIER321_CELL, DIM_E_RAN_CFA_EVALUATION_CASE, DIM_E_RAN_CFA_EXCEPTION_CLASS, DIM_E_RAN_CFA_DISCONNECTION "
				+ "WHERE IMSI = "
				+ Constants.IMSI_CFA_EVENT
				+ " "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.C_ID_SERV_HSDSCH_CELL = DIM_E_SGEH_HIER321_CELL.CID "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.RAN_DISCONNECTION_CODE = DIM_E_RAN_CFA_DISCONNECTION.DISCONNECTION_CODE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.RAN_DISCONNECTION_SUBCODE = DIM_E_RAN_CFA_DISCONNECTION.DISCONNECTION_SUB_CODE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.EXCEPTION_CLASS = DIM_E_RAN_CFA_EXCEPTION_CLASS.EXCEPTION_CLASS "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.EVALUATION_CASE = DIM_E_RAN_CFA_EVALUATION_CASE.EVALUATION_CASE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.EXTENDED_CAUSE_VALUE = DIM_E_RAN_CFA_EXTENDED_CAUSE_VALUE.EXTENDED_CAUSE_VALUE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.CAUSE_VALUE = DIM_E_RAN_CFA_CAUSE_VALUE.CAUSE_VALUE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.PROCEDURE_INDICATOR = DIM_E_RAN_CFA_PROCEDURE_INDICATOR.PROCEDURE_INDICATOR "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.UTRAN_RANAP_CAUSE = DIM_E_RAN_CFA_UTRAN_RANAP_CAUSE.UTRAN_RANAP_CAUSE "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.TRIGGER_POINT = DIM_E_RAN_CFA_TRIGGER_POINT.TRIGGER_POINT "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.SOURCE_CONF = DIM_E_RAN_RABTYPE.RABTYPE "
				+ "AND EVENT_TIME >= '2012-05-16 "
				+ (hours - 1)
				+ ":"
				+ mins
				+ ":"
				+ secs
				+ ".000' "
				+ "AND EVENT_TIME <= '2012-05-16 "
				+ (hours - 1) + ":" + mins + ":" + secs + ".999'";

		ResultSet rs = database.executeQuery(query);
		System.out.println(query);
		while (rs.next()) {
			dbRabType = rs.getString(1);
			dbTriggerPoint = rs.getString(2);
			dbUTRAN_RANAP_Cause = rs.getString(3);
			dbCN_RANAP_Cause = rs.getString(4);
			dbGBRUplink = rs.getString(5);
			dbGBRDownlink = rs.getString(6);
			dbServingHSDSCHCellId = rs.getString(7);
			dbServingHSDSCHCID = rs.getString(8);
			dbServingHSDSCHRNCId = rs.getString(9);
			dbProcedureIndicator = rs.getString(10);
			dbCauseValue = rs.getString(11);
			dbExtendedCauseValue = rs.getString(12);
			dbEvaluationCase = rs.getString(13);
			dbExceptionClass = rs.getString(14);
			dbDisconnectionDescription = rs.getString(15);
			dbDisconnectionCode = rs.getString(16);
			dbDisconnectionSubCode = rs.getString(17);
		}

		assertTrue("Value on the UI: " + uiRabType
				+ " does not match the database!", dbRabType.equals(uiRabType));
		assertTrue("Value on the UI: " + uiTriggerPoint
				+ " does not match the database!",
				dbTriggerPoint.equals(uiTriggerPoint));
		assertTrue("Value on the UI: " + uiUTRAN_RANAP_Cause
				+ " does not match the database!",
				dbUTRAN_RANAP_Cause.equals(uiUTRAN_RANAP_Cause));
		assertTrue("Value on the UI: " + uiCN_RANAP_Cause
				+ " does not match the database!",
				dbCN_RANAP_Cause.equals(uiCN_RANAP_Cause));
		assertTrue("Value on the UI: " + uiGBRUplink
				+ " does not match the database!",
				dbGBRUplink.equals(uiGBRUplink));
		assertTrue("Value on the UI: " + uiGBRDownlink
				+ " does not match the database!",
				dbGBRDownlink.equals(uiGBRDownlink));
		assertTrue("Value on the UI: " + uiServingHSDSCHCellId
				+ " does not match the database!",
				dbServingHSDSCHCellId.equals(uiServingHSDSCHCellId));
		assertTrue("Value on the UI: " + uiServingHSDSCHCID
				+ " does not match the database!",
				dbServingHSDSCHCID.equals(uiServingHSDSCHCID));
		assertTrue("Value on the UI: " + uiServingHSDSCHRNCId
				+ " does not match the database!",
				dbServingHSDSCHRNCId.equals(uiServingHSDSCHRNCId));
		assertTrue("Value on the UI: " + uiProcedureIndicator
				+ " does not match the database!", dbProcedureIndicator
				.replaceAll("_", " ").equals(uiProcedureIndicator));
		assertTrue("Value on the UI: " + uiCauseValue
				+ " does not match the database!",
				dbCauseValue.equals(uiCauseValue));
		assertTrue("Value on the UI: " + uiExtendedCauseValue
				+ " does not match the database!", dbExtendedCauseValue
				.replaceAll("_", " ").equals(uiExtendedCauseValue));
		assertTrue("Value on the UI: " + uiEvaluationCase
				+ " does not match the database!",
				dbEvaluationCase.replaceAll("_", " ").equals(uiEvaluationCase));
		assertTrue("Value on the UI: " + uiExceptionClass
				+ " does not match the database!",
				dbExceptionClass.replaceAll("_", " ").equals(uiExceptionClass));
		assertTrue("Value on the UI: " + uiDisconnectionDescription
				+ " does not match the database!", dbDisconnectionDescription
				.replaceAll("_", " ").equals(uiDisconnectionDescription));
		assertTrue("Value on the UI: " + uiDisconnectionCode
				+ " does not match the database!",
				dbDisconnectionCode.equals(uiDisconnectionCode));
		assertTrue("Value on the UI: " + uiDisconnectionSubCode
				+ " does not match the database!",
				dbDisconnectionSubCode.equals(uiDisconnectionSubCode));

		query = "SELECT DIM_E_RAN_RABTYPE.RABTYPE_DESC, SOURCE_CONNECTION_PROPERTIES, TARGET_CONNECTION_PROPERTIES, WANTED_CONNECTION_PROPERTIES FROM DIM_E_RAN_RABTYPE, EVENT_E_RAN_CFA_ERR_RAW "
				+ "WHERE IMSI = "
				+ Constants.IMSI_CFA_EVENT
				+ " "
				+ "AND EVENT_E_RAN_CFA_ERR_RAW.TARGET_CONF = DIM_E_RAN_RABTYPE.RABTYPE "
				+ "AND EVENT_TIME >= '2012-05-16 "
				+ (hours - 1)
				+ ":"
				+ mins
				+ ":"
				+ secs
				+ ".000' "
				+ "AND EVENT_TIME <= '2012-05-16 "
				+ (hours - 1) + ":" + mins + ":" + secs + ".999'";

		rs = database.executeQuery(query);
		while (rs.next()) {
			dbRabTypeReconfigure = rs.getString(1);
			dbSourceConnectionProp = rs.getString(2);
			dbTargetConnectionProp = rs.getString(3);
			dbWantedConnectionProp = rs.getString(4);
		}

		assertTrue("Value on the UI: " + uiRabTypeReconfigure
				+ " does not match the database!", dbRabTypeReconfigure
				.replaceAll("_", " ").equals(uiRabTypeReconfigure));
		assertFalse("Source Connection Prop is empty",
				uiSourceConnectionProp.isEmpty());
		assertFalse("Target Connection Prop is empty",
				uiTargetConnectionProp.isEmpty());
		assertFalse("Wanted Connection Prop is empty",
				uiWantedConnectionProp.isEmpty());

		database.closeConnection();
	}

	/**
	 * Test Case 7.89: Verify the Active Set Measurements Details Section of the
	 * View Details Window for Internal System Release Event in the Session
	 * Browser Details Tab.
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	@Ignore
	@Test
	public void testInternalSystemReleaseEvent_7_89()
			throws InterruptedException, PopUpException, SQLException, ParseException {
		String uiRSCPCell1 = null, uiRSCPCell2 = null, uiRSCPCell3 = null, uiRSCPCell4 = null, uiRSCPCell1AddedCell = null, uiCPICHEcNoCell1 = null, uiCPICHEcNoCell2 = null, uiCPICHEcNoCell3 = null, uiCPICHEcNoCell4 = null, uiCPICHEcNoAddedCell = null, uiScamblingCodeCell1 = null, uiScamblingCodeCell2 = null, uiScamblingCodeCell3 = null, uiScamblingCodeCell4 = null, uiScamblingCodeAddedCell = null, uiULInterferenceCell1 = null, uiULInterferenceCell2 = null, uiULInterferenceCell3 = null, uiULInterferenceCell4 = null;

		String dbRSCPCell1 = null, dbRSCPCell2 = null, dbRSCPCell3 = null, dbRSCPCell4 = null, dbRSCPCell1AddedCell = null, dbCPICHEcNoCell1 = null, dbCPICHEcNoCell2 = null, dbCPICHEcNoCell3 = null, dbCPICHEcNoCell4 = null, dbCPICHEcNoAddedCell = null, dbScamblingCodeCell1 = null, dbScamblingCodeCell2 = null, dbScamblingCodeCell3 = null, dbScamblingCodeCell4 = null, dbScamblingCodeAddedCell = null, dbULInterferenceCell1 = null, dbULInterferenceCell2 = null, dbULInterferenceCell3 = null, dbULInterferenceCell4 = null;

		database.openConnection();

		openSessionDetail(CommonDataType.SB_IMSI_INT_SYSTEM_RELEASE, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, INT_SYSTEM_RELEASE);

		final int index = sessionbrowserTab.getRecordPosition(
				INT_SYSTEM_RELEASE, sessionTable);
		if (index >= 0) {
			driver.findElement(
					By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
							+ (2 + index) + "]/td[2]")).click();
			driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
			final List<WebElement> section = getSectionHeadings(4);

			final List<String> expectedValues = Arrays.asList("RSCP Cell 1",
					"RSCP Cell 2", "RSCP Cell 3", "RSCP Cell 4",
					"RSCP Cell 1 Added Cell", "CPICH Ec/No Cell 1",
					"CPICH Ec/No Cell 2", "CPICH Ec/No Cell 3",
					"CPICH Ec/No Cell 4", "CPICH Ec/No Added Cell",
					"Scambling Code Cell 1", "Scambling Code Cell 2",
					"Scambling Code Cell 3", "Scambling Code Cell 4",
					"Scambling Code Added Cell", "UL Interference Cell 1",
					"UL Interference Cell 2", "UL Interference Cell 3",
					"UL Interference Cell 4");

			for (int i = 0; i < section.size(); i++) {
				final String text = section.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}

		final List<WebElement> activeSetMeasurementValues = driver
				.findElements(By
						.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]/div[2]//tr/td[2]"));
		if (!activeSetMeasurementValues.isEmpty()) {
			uiRSCPCell1 = activeSetMeasurementValues.get(0).getText();
			uiRSCPCell2 = activeSetMeasurementValues.get(1).getText();
			uiRSCPCell3 = activeSetMeasurementValues.get(2).getText();
			uiRSCPCell4 = activeSetMeasurementValues.get(3).getText();
			uiRSCPCell1AddedCell = activeSetMeasurementValues.get(4).getText();
			uiCPICHEcNoCell1 = activeSetMeasurementValues.get(5).getText();
			uiCPICHEcNoCell2 = activeSetMeasurementValues.get(6).getText();
			uiCPICHEcNoCell3 = activeSetMeasurementValues.get(7).getText();
			uiCPICHEcNoCell4 = activeSetMeasurementValues.get(8).getText();
			uiCPICHEcNoAddedCell = activeSetMeasurementValues.get(9).getText();
			uiScamblingCodeCell1 = activeSetMeasurementValues.get(10).getText();
			uiScamblingCodeCell2 = activeSetMeasurementValues.get(11).getText();
			uiScamblingCodeCell3 = activeSetMeasurementValues.get(12).getText();
			uiScamblingCodeCell4 = activeSetMeasurementValues.get(13).getText();
			uiScamblingCodeAddedCell = activeSetMeasurementValues.get(14)
					.getText();
			uiULInterferenceCell1 = activeSetMeasurementValues.get(15)
					.getText();
			uiULInterferenceCell2 = activeSetMeasurementValues.get(16)
					.getText();
			uiULInterferenceCell3 = activeSetMeasurementValues.get(17)
					.getText();
			uiULInterferenceCell4 = activeSetMeasurementValues.get(18)
					.getText();
		}

		// Get event time from session list
		final String[] sessionTime = driver
				.findElement(
						By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
								+ (2 + index) + "]/td[1]")).getText()
				.split(":");
		final int hours = Integer.parseInt(sessionTime[0]);
		final int mins = Integer.parseInt(sessionTime[1]);
		final int secs = Integer.parseInt(sessionTime[2]);

		final String query = "SELECT RSCP_CELL_1, RSCP_CELL_2, RSCP_CELL_3, RSCP_CELL_4, RSCP_CELL_1_ADDED_CELL, "
				+ "CPICH_EC_NO_CELL_1, CPICH_EC_NO_CELL_2, CPICH_EC_NO_CELL_3, CPICH_EC_NO_CELL_4, CPICH_EC_NO_ADDED_CELL, "
				+ "SCRAMBLING_CODE_CELL_1, SCRAMBLING_CODE_CELL_2, SCRAMBLING_CODE_CELL_3, SCRAMBLING_CODE_CELL_4, SCRAMBLING_CODE_ADDED_CELL, "
				+ "UL_INT_CELL1, UL_INT_CELL2, UL_INT_CELL3, UL_INT_CELL4 FROM EVENT_E_RAN_CFA_ERR_RAW "
				+ "WHERE IMSI = "
				+ Constants.IMSI_CFA_EVENT
				+ " "
				+ "AND EVENT_TIME >= '2012-05-16 "
				+ (hours - 1)
				+ ":"
				+ mins
				+ ":"
				+ secs
				+ ".000' "
				+ "AND EVENT_TIME <= '2012-05-16 "
				+ (hours - 1) + ":" + mins + ":" + secs + ".999'";

		final ResultSet rs = database.executeQuery(query);
		while (rs.next()) {
			dbRSCPCell1 = rs.getString(1) == null ? "-" : rs.getString(1);
			dbRSCPCell2 = rs.getString(2) == null ? "-" : rs.getString(2);
			dbRSCPCell3 = rs.getString(3) == null ? "-" : rs.getString(3);
			dbRSCPCell4 = rs.getString(4) == null ? "-" : rs.getString(4);
			dbRSCPCell1AddedCell = rs.getString(5) == null ? "-" : rs
					.getString(5);
			dbCPICHEcNoCell1 = rs.getString(6) == null ? "-" : rs.getString(6);
			dbCPICHEcNoCell2 = rs.getString(7) == null ? "-" : rs.getString(7);
			dbCPICHEcNoCell3 = rs.getString(8) == null ? "-" : rs.getString(8);
			dbCPICHEcNoCell4 = rs.getString(9) == null ? "-" : rs.getString(9);
			dbCPICHEcNoAddedCell = rs.getString(10) == null ? "-" : rs
					.getString(10);
			dbScamblingCodeCell1 = rs.getString(11) == null ? "-" : rs
					.getString(11);
			dbScamblingCodeCell2 = rs.getString(12) == null ? "-" : rs
					.getString(12);
			dbScamblingCodeCell3 = rs.getString(13) == null ? "-" : rs
					.getString(13);
			dbScamblingCodeCell4 = rs.getString(14) == null ? "-" : rs
					.getString(14);
			dbScamblingCodeAddedCell = rs.getString(15) == null ? "-" : rs
					.getString(15);
			dbULInterferenceCell1 = rs.getString(16) == null ? "-" : rs
					.getString(16);
			dbULInterferenceCell2 = rs.getString(17) == null ? "-" : rs
					.getString(17);
			dbULInterferenceCell3 = rs.getString(18) == null ? "-" : rs
					.getString(18);
			dbULInterferenceCell4 = rs.getString(19) == null ? "-" : rs
					.getString(19);
		}

		assertTrue("Value on the UI: " + uiRSCPCell1
				+ " does not match the database!",
				dbRSCPCell1.equals(uiRSCPCell1));
		assertTrue("Value on the UI: " + uiRSCPCell2
				+ " does not match the database!",
				dbRSCPCell2.equals(uiRSCPCell2));
		assertTrue("Value on the UI: " + uiRSCPCell3
				+ " does not match the database!",
				dbRSCPCell3.equals(uiRSCPCell3));
		assertTrue("Value on the UI: " + uiRSCPCell4
				+ " does not match the database!",
				dbRSCPCell4.equals(uiRSCPCell4));
		assertTrue("Value on the UI: " + uiRSCPCell1AddedCell
				+ " does not match the database!",
				dbRSCPCell1AddedCell.equals(uiRSCPCell1AddedCell));
		assertTrue("Value on the UI: " + uiCPICHEcNoCell1
				+ " does not match the database!",
				dbCPICHEcNoCell1.equals(uiCPICHEcNoCell1));
		assertTrue("Value on the UI: " + uiCPICHEcNoCell2
				+ " does not match the database!",
				dbCPICHEcNoCell2.equals(uiCPICHEcNoCell2));
		assertTrue("Value on the UI: " + uiCPICHEcNoCell3
				+ " does not match the database!",
				dbCPICHEcNoCell3.equals(uiCPICHEcNoCell3));
		assertTrue("Value on the UI: " + uiCPICHEcNoCell4
				+ " does not match the database!",
				dbCPICHEcNoCell4.equals(uiCPICHEcNoCell4));
		assertTrue("Value on the UI: " + uiCPICHEcNoAddedCell
				+ " does not match the database!",
				dbCPICHEcNoAddedCell.equals(uiCPICHEcNoAddedCell));
		assertTrue("Value on the UI: " + uiScamblingCodeCell1
				+ " does not match the database!",
				dbScamblingCodeCell1.equals(uiScamblingCodeCell1));
		assertTrue("Value on the UI: " + uiScamblingCodeCell2
				+ " does not match the database!",
				dbScamblingCodeCell2.equals(uiScamblingCodeCell2));
		assertTrue("Value on the UI: " + uiScamblingCodeCell3
				+ " does not match the database!",
				dbScamblingCodeCell3.equals(uiScamblingCodeCell3));
		assertTrue("Value on the UI: " + uiScamblingCodeCell4
				+ " does not match the database!",
				dbScamblingCodeCell4.equals(uiScamblingCodeCell4));
		assertTrue("Value on the UI: " + uiScamblingCodeAddedCell
				+ " does not match the database!",
				dbScamblingCodeAddedCell.equals(uiScamblingCodeAddedCell));
		assertTrue("Value on the UI: " + uiULInterferenceCell1
				+ " does not match the database!",
				dbULInterferenceCell1.equals(uiULInterferenceCell1));
		assertTrue("Value on the UI: " + uiULInterferenceCell2
				+ " does not match the database!",
				dbULInterferenceCell2.equals(uiULInterferenceCell2));
		assertTrue("Value on the UI: " + uiULInterferenceCell3
				+ " does not match the database!",
				dbULInterferenceCell3.equals(uiULInterferenceCell3));
		assertTrue("Value on the UI: " + uiULInterferenceCell4
				+ " does not match the database!",
				dbULInterferenceCell4.equals(uiULInterferenceCell4));

		database.closeConnection();
	}

	/**
	 * DEFTFTS-159: Check Subscriber Identity section for RRC Measurement
	 * Reports
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	@Test
	public void testRRCMeasurementReport_7_106_1() throws InterruptedException,
			PopUpException, SQLException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true, false);
		
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, RRC_MEAS_REPORT);

		final int position = sessionbrowserTab.getRecordPosition(
				RRC_MEAS_REPORT, sessionTable);

		if (position >= 0) {
			openViewDetails(position);

			final List<WebElement> sectionHeadings = getSectionHeadings(1);

			final List<String> expectedValues = Arrays.asList("MSISDN", "IMSI",
					"Network");

			for (int i = 0; i < sectionHeadings.size(); i++) {
				final String text = sectionHeadings.get(i).getText().trim();

				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}

			/**
			 * THIS CODE MAY NOT BE NEEDED - DATA INTEGRITY
			 */
			// Get event time from session list
			// final String[] sessionTime = driver
			// .findElement(
			// By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
			// + (2 + position) + "]/td[1]"))
			// .getText().split(":");
			// final int hours = Integer.parseInt(sessionTime[0]);
			// final int mins = Integer.parseInt(sessionTime[1]);
			// final int secs = Integer.parseInt(sessionTime[2]);
			//
			// final String query =
			// "SELECT TOP 1 IMSI, DIM_E_SGEH_MCCMNC.COUNTRY, DIM_E_SGEH_MCCMNC.OPERATOR "
			// +
			// "FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW, DIM_E_SGEH_MCCMNC WHERE IMSI = "
			// + Constants.IMSI_RRC_MEAS_REPORT
			// + " "
			// +
			// "AND EVENT_E_RAN_SESSION_RRC_MEAS_RAW.IMSI_MCC = DIM_E_SGEH_MCCMNC.MCC "
			// +
			// "AND EVENT_E_RAN_SESSION_RRC_MEAS_RAW.IMSI_MNC = DIM_E_SGEH_MCCMNC.MNC "
			// + "AND EVENT_TIME >= '2012-05-16 "
			// + (hours - 1)
			// + ":"
			// + mins
			// + ":"
			// + secs
			// + ".000' "
			// + "AND EVENT_TIME <= '2012-05-16 "
			// + (hours - 1)
			// + ":"
			// + mins + ":" + secs + ".999'";
			//
			// final ResultSet rs = database.executeQuery(query);
			// String dbIMSI = null;
			// String dbNetwork = null;
			//
			// while (rs.next()) {
			// // /* MSISDN not in database */
			// dbIMSI = rs.getString(1);
			// dbNetwork = rs.getString(2) + "," + rs.getString(3);
			// }
			// /* The MSISDN was not found in the database query */
			//
			// assertTrue("Value on the UI: " +
			// sectionValues.get(1).getText()
			// + " does not match the database!", sectionValues.get(1)
			// .getText().equals(dbIMSI));
			// assertTrue("Value on the UI: " +
			// sectionValues.get(2).getText()
			// + " does not match the database!", sectionValues.get(2)
			// .getText().equals(dbNetwork));
		}

		// database.closeConnection();
	}

	/**
	 * Check Event Details Section for RRC Measurement Reports
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Test
	public void testRRCMeasurementReport_7_106_2() throws InterruptedException,
			PopUpException, SQLException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, RRC_MEAS_REPORT);

		final int position = sessionbrowserTab.getRecordPosition(
				RRC_MEAS_REPORT, sessionTable);

		if (position >= 0) {
			openViewDetails(position);

			final List<WebElement> sectionHeadings = getSectionHeadings(2);

			final List<String> expectedValues = Arrays.asList("Event Time",
					"Measurement Type");

			for (int i = 0; i < sectionHeadings.size(); i++) {
				final String text = sectionHeadings.get(i).getText().trim();
				assertTrue(
						"Value: " + text + " does not match expected field: "
								+ expectedValues.get(i), expectedValues.get(i)
								.equals(text));
			}
		}
	}

	/**
	 * Check Measured Results sections for RRC Measurement Reports
	 *  
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Test
	public void testRRCMeasurementReport_7_106_3() throws InterruptedException,
			PopUpException, SQLException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true, false);
		
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		checkSessionList(sessionTable, RRC_MEAS_REPORT);
		
		final int position = sessionbrowserTab.getRecordPosition(
				RRC_MEAS_REPORT, sessionTable);

		if (position >= 0) {
			openViewDetails(position);

			/**
			 * THIS CODE MAY NOT BE NEEDED - DATA INTEGRITY
			 */
			// // Get event time from session list
			// final String[] sessionTime = driver
			// .findElement(
			// By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
			// + (2 + position) + "]/td[1]"))
			// .getText().split(":");
			// final int hours = Integer.parseInt(sessionTime[0]);
			// final int mins = Integer.parseInt(sessionTime[1]);
			// final int secs = Integer.parseInt(sessionTime[2]);
			//
			// final String query =
			// "SELECT SCRAMBLINGCODE, BSIC, ECNO, RSCP FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW "
			// + "WHERE IMSI = "
			// + Constants.IMSI_RRC_MEAS_REPORT
			// + " "
			// + "AND EVENT_TIME >= '2012-05-16 "
			// + (hours - 1)
			// + ":"
			// + mins
			// + ":"
			// + secs
			// + ".000' "
			// + "AND EVENT_TIME <= '2012-05-16 "
			// + (hours - 1)
			// + ":"
			// + mins + ":" + secs + ".999'";
			//
			// final List<WebElement> measuredResultsHeadings =
			// getSectionHeadings(3);
			// final List<WebElement> measuredResultsValues =
			// getSectionValues(3);
			//
			// final ResultSet rs = database.executeQuery(query);
			//
			// final List<String> expectedMeasuredResultsHeadings = new
			// ArrayList<String>();
			// final List<String> measuredResultsValuesDB = new
			// ArrayList<String>();
			//
			// int row = 0;
			//
			// while (rs.next()) {
			// final String scrambCode = rs.getString(1);
			// final String BSIC = rs.getString(2);
			// final String ECNO = rs.getString(3);
			// final String RSCP = rs.getString(4);
			//
			// row++;
			//
			// if (scrambCode != null) {
			// expectedMeasuredResultsHeadings.add("Scrambling Code "
			// + row);
			// measuredResultsValuesDB.add(scrambCode);
			// }
			// if (BSIC != null) {
			// expectedMeasuredResultsHeadings.add("BSIC " + row);
			// measuredResultsValuesDB.add(BSIC);
			// }
			// if (ECNO != null) {
			// expectedMeasuredResultsHeadings.add("Ec/No " + row);
			// measuredResultsValuesDB.add(ECNO);
			// }
			// if (RSCP != null) {
			// expectedMeasuredResultsHeadings.add("RSCP " + row);
			// measuredResultsValuesDB.add(RSCP);
			// }
			// }
			//
			// // check headings in Measured Results section
			// for (int i = 0; i < measuredResultsHeadings.size(); i++) {
			// final String text = measuredResultsHeadings.get(i)
			// .getText().trim();
			// assertTrue("Heading: " + text
			// + " does not match expected field: "
			// + expectedMeasuredResultsHeadings.get(i),
			// expectedMeasuredResultsHeadings.get(i).equals(text));
			// }
			//
			// // Verify the values in the result set from db
			// for (int i = 0; i < measuredResultsValues.size(); i++) {
			// final String text = measuredResultsValues.get(i).getText()
			// .trim();
			//
			// assertTrue("Value: " + text
			// + " does not match expected field: "
			// + measuredResultsValuesDB.get(i),
			// measuredResultsValuesDB.get(i).contains(text));
			// }

		}
	}

	/**
	 * Check Event Results Section for RRC Measurement Reports
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException 
	 */
	@Test
	public void testRRCMeasurementReport_7_106_4() throws InterruptedException,
			PopUpException, SQLException, ParseException {
		
		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true, false);
		
		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		
		checkSessionList(sessionTable, RRC_MEAS_REPORT);

		database.openConnection();

		if (!sessionTable.isEmpty()) {
			final int position = sessionbrowserTab.getRecordPosition(
					RRC_MEAS_REPORT, sessionTable);

			if (position >= 0) {
				openViewDetails(position);

				/**
				 * THIS CODE MAY NOT BE NEEDED - DATA INTEGRITY
				 */
				// // Get event time from session list
				// final String[] sessionTime = driver
				// .findElement(
				// By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
				// + (2 + position) + "]/td[1]"))
				// .getText().split(":");
				// final int hours = Integer.parseInt(sessionTime[0]);
				// final int mins = Integer.parseInt(sessionTime[1]);
				// final int secs = Integer.parseInt(sessionTime[2]);

				// final String query =
				// "SELECT TRIGGER_EVENT_ID FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW "
				// + "WHERE IMSI = "
				// + Constants.IMSI_RRC_MEAS_REPORT
				// + " "
				// + "AND EVENT_TIME >= '2012-05-16 "
				// + (hours - 1)
				// + ":"
				// + mins
				// + ":"
				// + secs
				// + ".000' "
				// + "AND EVENT_TIME <= '2012-05-16 "
				// + (hours - 1)
				// + ":"
				// + mins + ":" + secs + ".999'";

				// final ResultSet rs = database.executeQuery(query);

				// final List<String> expectedEventResultsHeadings = new
				// ArrayList<String>();
				// final List<WebElement> eventResultsHeadings =
				// getSectionHeadings(4);
				//
				// int count = 0;
				// String eventTriggerID;
				//
				// expectedEventResultsHeadings.add("Reporting Event");
				// expectedEventResultsHeadings.add("Reporting Event Description");

				// while (rs.next()) {
				// eventTriggerID = rs.getString(1);
				//
				// if (eventTriggerID != null) {
				// count++;
				// expectedEventResultsHeadings
				// .add("Event Results Scrambling Code " + (count));
				// }
				// }

				// for (int i = 0; i < eventResultsHeadings.size(); i++) {
				// final String text = eventResultsHeadings.get(i).getText()
				// .trim();
				// assertTrue("Value: " + text
				// + " does not match expected field: "
				// + expectedEventResultsHeadings.get(i),
				// expectedEventResultsHeadings.get(i).equals(text));
				// }

			}
		}
		database.closeConnection();
	}

	/**
	 * Check Active Set Details Section for RRC Measurement Reports
	 * 
	 * @throws InterruptedException
	 * @throws PopUpException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Test
	public void testRRCMeasurementReport_7_106_5() throws InterruptedException,
			PopUpException, SQLException, ParseException {
		openSessionDetail(CommonDataType.SB_IMSI_RRC_MEASUREMENT_REPORT, true, false);

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));
		
		checkSessionList(sessionTable, RRC_MEAS_REPORT);		
		
		final int position = sessionbrowserTab.getRecordPosition(
				RRC_MEAS_REPORT, sessionTable);

		if (position >= 0) {
			openViewDetails(position);
			// Verify headings
			final List<String> expectedHeadings = Arrays.asList("Cell Id 1",
					"CID 1", "RNC Id 1", "Cell Id 2", "CID 2", "RNC Id 2",
					"Cell Id 3", "CID 3", "RNC Id 3", "Cell Id 4", "CID 4",
					"RNC Id 4");
			
			final List<WebElement> section = getSectionHeadings(5);
			scrollViewDetailsSectionList(false,20);
			for (int i = 0; i < section.size(); i++) {
				final String actualHeading = section.get(i).getText().trim();
				assertTrue(
						"Value: " + actualHeading
								+ " does not match expected field: "
								+ expectedHeadings.get(i), expectedHeadings
								.get(i).equals(actualHeading));
			}

			/**
			 * THIS CODE MAY NOT BE NEEDED - DATA INTEGRITY
			 */
			// // Get event time from session list
			// final String[] sessionTime = driver
			// .findElement(
			// By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr["
			// + (2 + position) + "]/td[1]"))
			// .getText().split(":");
			// final int hours = Integer.parseInt(sessionTime[0]);
			// final int mins = Integer.parseInt(sessionTime[1]);
			// final int secs = Integer.parseInt(sessionTime[2]);
			//
			// final String query = "SELECT TOP 1 "
			// + "CELL_ID_1, C_ID_1, RNC1.HIERARCHY_3, "
			// + "CELL_ID_2, C_ID_2, RNC2.HIERARCHY_3, "
			// + "CELL_ID_3, C_ID_3, RNC3.HIERARCHY_3, "
			// + "CELL_ID_4, C_ID_4, RNC4.HIERARCHY_3 "
			// + "FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW "
			// +
			// "LEFT OUTER JOIN (SELECT DISTINCT HIER3_ID, HIERARCHY_3 FROM DIM_E_SGEH_HIER321_CELL ) AS RNC1 "
			// +
			// "ON ( RNC1.HIER3_ID = EVENT_E_RAN_SESSION_RRC_MEAS_RAW.HIER3_ID_1 ) "
			// +
			// "LEFT OUTER JOIN (SELECT DISTINCT HIER3_ID, HIERARCHY_3 FROM DIM_E_SGEH_HIER321_CELL ) AS RNC2 "
			// +
			// "ON ( RNC2.HIER3_ID = EVENT_E_RAN_SESSION_RRC_MEAS_RAW.HIER3_ID_2 ) "
			// +
			// "LEFT OUTER JOIN (SELECT DISTINCT HIER3_ID, HIERARCHY_3 FROM DIM_E_SGEH_HIER321_CELL ) AS RNC3 "
			// +
			// "ON ( RNC3.HIER3_ID = EVENT_E_RAN_SESSION_RRC_MEAS_RAW.HIER3_ID_3 ) "
			// +
			// "LEFT OUTER JOIN (SELECT DISTINCT HIER3_ID, HIERARCHY_3 FROM DIM_E_SGEH_HIER321_CELL ) AS RNC4 "
			// +
			// "ON ( RNC4.HIER3_ID = EVENT_E_RAN_SESSION_RRC_MEAS_RAW.HIER3_ID_4 ) "
			// + "WHERE IMSI = " + Constants.IMSI_RRC_MEAS_REPORT
			// + " " + "AND EVENT_TIME >= '2012-05-16 " + (hours - 1)
			// + ":" + mins + ":" + secs + ".000' "
			// + "AND EVENT_TIME <= '2012-05-16 " + (hours - 1) + ":"
			// + mins + ":" + secs + ".999'";
			//
			// // Verify values against database
			// final ResultSet rs = database.executeQuery(query);
			//
			// final List<WebElement> activeSetDetailsValues =
			// getSectionValues(5);
			// while (rs.next()) {
			// for (int i = 0; i < activeSetDetailsValues.size(); i++) {
			// String dbValue = rs.getString(i + 1) == null ? "-" : rs
			// .getString(i + 1);
			// // dbValue = dbValue.replaceAll("_", " ");
			// assertTrue(
			// "Database value : "
			// + dbValue
			// + " does not match UI: "
			// + activeSetDetailsValues.get(i)
			// .getText(),
			// activeSetDetailsValues.get(i).getText()
			// .equals(dbValue));
			// }
			// }
		}

	}

	private void openDetailsRanSignalling(String imsi)
			throws InterruptedException {
		sessionbrowserTab.openTab();
		sessionbrowserTab.waitForPageToLoad();
		sessionbrowserTab.openDetailsSessionBrowser2Hour(imsi);
		// sessionbrowserTab.openCustomDateAndTime("2012 Dec", "2012 Dec", "17",
		// "18");

		driver.findElement(By.xpath(Constants.RAN_SIGNALLING_BUTTON)).click();
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}

	private void checkSessionList(List<WebElement> table, String eventName) {
	
		assertTrue("There were no " + eventName + " events found",
				sessionbrowserTab.isEventFound(eventName, table));
	}

	/* private methods for US 154 - to avoid duplication of code */
	private void checkForEvent(final String EventTypeUiName,
			final boolean hasWebServerDistOption) throws ParseException,
			InterruptedException {

		final List<WebElement> sessionTable = driver.findElements(By
				.xpath(Constants.SESSION_TABLE));

		final Boolean eventFound = sessionbrowserTab.isEventFound(
				EventTypeUiName, sessionTable);

		assertTrue("No " + EventTypeUiName + " event found for selected IMSI",
				eventFound);

		final int numberOfEvents = driver
				.findElements(
						By.xpath("./*//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index]"))
				.size();

		for (int event = 0; event < numberOfEvents; event++) {
			final String eventNamePath = "./*//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
					+ event + "']/td[3]";

			final String eventName = driver
					.findElement(By.xpath(eventNamePath)).getText();
			if (eventName.equals(EventTypeUiName)) {

				// Action 1 - Check if the user can see the event
				assertTrue(
						"Int Soft Handover Execution Failure event not found for selected IMSI",
						eventName.equals(EventTypeUiName));

				// Action 2 - Check the tooltip contains the correct structure
				// with Source Cell and Target Cell
				final String actualTooltip = driver.findElement(
						By.xpath(eventNamePath)).getAttribute("title");
				final boolean containsSourceCell = actualTooltip
						.contains("Source Cell:");
				final boolean containsTaregtCell = actualTooltip
						.contains("Target Cell:");
				assertEquals("Tooltip not displayed correctly for "
						+ EventTypeUiName, containsSourceCell,
						containsTaregtCell);

				// Action 3- check for View Details and Reports in dropDown
				checkDropDownOptions(event, hasWebServerDistOption,
						EventTypeUiName);
				break; // we have found one - no need to check them all
			}
		}
	}

	private void checkDropDownOptions(final int rowIndex,
			final boolean serverDistribution, final String eventType)
			throws InterruptedException {
		final String dropDownButton = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[1]/div/div/div/div[2]/div/div[3]/table/tbody/tr/td[2]/div/div[2]/div/div[1]/div/table/tbody//tr[@__index='"
				+ rowIndex + "']/td[2]";
		driver.findElement(By.xpath(dropDownButton)).click();
		Thread.sleep(1000);
		assertTrue("View Details option not found for event: " + eventType,
				sessionbrowserTab.isElementPresent(Constants.VIEW_DETAILS));
		assertTrue("Reports option not found for event: " + eventType,
				sessionbrowserTab.isElementPresent(Constants.REPORTS));
		if (serverDistribution) {
			assertTrue("Server Distribution option not found.",
					sessionbrowserTab
							.isElementPresent(Constants.SERVER_DISTRIBUTION));
		}
		driver.findElement(By.xpath(dropDownButton)).click();
	}

	private void openDataBearerSession(String imsi) throws InterruptedException {
		sessionbrowserTab.openTab();
		sessionbrowserTab.waitForPageToLoad();
		sessionbrowserTab.openDetailsSessionBrowser(imsi);
		sessionbrowserTab.openCustomDateAndTime("2012 Dec", "2012 Dec", "1",
				"31");
		driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
	}
}
