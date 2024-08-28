/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.upgrade;

import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementOperations;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.login.FourGCoreUser;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class PostUpgrade extends BaseSeleniumTest {

	@Autowired
	private FourGCoreUser fourGCoreUser;

	@Autowired
	private WorkspaceRC workspace;

	@Autowired
	private EricssonSelenium selenium;

	@Autowired
	private GroupManagementOperations groupManagementOperations;

	@Autowired
	private GroupManagementWindow groupManagementWindow;

	@BeforeClass
	public static void openLog() {
		logger.log(Level.INFO, "Start of Core(PS) Post Upgrade Tests");
	}

	@AfterClass
	public static void closeLog() {
		logger.log(Level.INFO, "End of Core(PS) Post Upgrade Tests");
	}

	@Override
	@Before
	public void setUp() {
	}

	@After
	@Override
	public void tearDown() {
		if (selenium != null) {
			selenium.close();
			selenium.stop();
			selenium = null;
		}
	}

	@Test
	public void test_savedWorkspaceStillExists() throws Exception {
		try {
			loginToEniqEventsUI();
			validateWorkspaceExists();

			workspace.openSavedWorkspace(FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE);
			validateThatWorkspaceWindowsLaunched();
		} finally {
			fourGCoreUser.logOut();
			//Dont want to save an empty workspace
			if (selenium.isElementPresent("//button[text()='Ok']")) {
				selenium.click("//button[text()='Ok']");
			}
		}
	}

	@Test
	public void test_apnGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.APN_GROUP_NAME, GuiStringConstants.APN, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_accessAreaGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.ACCESS_AREA_GROUP_NAME, GuiStringConstants.ACCESS_AREA, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_controllerGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.CONTROLLER_GROUP_NAME, GuiStringConstants.CONTROLLER, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_imsiGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.IMSI_GROUP_NAME, GuiStringConstants.IMSI, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_sgsnMmeGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.SGSN_GROUP_NAME, GuiStringConstants.SGSN__MME, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_terminalGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.TAC_GROUP_NAME, GuiStringConstants.TERMINAL, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}
	
	@Test
	public void test_trackingAreaGroupStillExists(){
		try {
			loginToEniqEventsUI();
			pause(1000);
			assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.TRACKING_AREA_GROUP_NAME, GuiStringConstants.TRACKING_AREA, groupManagementWindow));

		}finally {
			fourGCoreUser.logOut();
		}
	}

	private void loginToEniqEventsUI() {
		selenium.start("captureNetworkTraffic=true");
		selenium.windowFocus();
		selenium.windowMaximize();
		selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
				+ PropertyReader.getInstance().getPath(), "true");
		selenium.waitForPageToLoad("30000");
		fourGCoreUser.logIn();
		if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
			assertFalse("Error during loading of Landing Page", selenium.getText("//span[@class='x-window-header-text']").equals("Error"));
		}
		selenium.waitForElementToBePresent("//*[contains(@id,'headerPnl')]//div[contains(text(),'" + UIConstants.ENIQ_LOGGED_IN_MSG + "')]", "30000");
		selenium.waitForElementToBePresent("//*[contains(@role,'tablist') and contains(@class,'tab')]", "30000");
	}

	private void openSideBar() {
		pause(2000);
		workspace.checkAndOpenSideLaunchBar();
		pause(2000);
		workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
		pause(2000);
	}

	private void validateWorkspaceExists() {
		openSideBar();
		selenium.click(SeleniumConstants.WORKSPACES_SIDE_BAR_TAB_XPATH);
		if (!selenium.isElementPresent("//div[text()='" + FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE + "']")) {
			fail(FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE + " does not exist after upgrade.");
		}
	}

	private void validateThatWorkspaceWindowsLaunched() {
		assertEquals("Cause Code Ranking Window didn't launch.", "Cause Code Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][1]/div[@class='x-window-tl']//span"));
		assertEquals("BSC Ranking Window didn't launch.", "BSC Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][2]/div[@class='x-window-tl']//span"));
		assertEquals("eNodeB Ranking Window didn't launch.", "eNodeB Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][3]/div[@class='x-window-tl']//span"));
		assertEquals("RNC Ranking Window didn't launch.", "RNC Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][4]/div[@class='x-window-tl']//span"));
		assertEquals("Events Terminal Ranking Window didn't launch.", "Events Terminal Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][5]/div[@class='x-window-tl']//span"));
		assertEquals("Events Subscriber Ranking Window didn't launch.", "Events Subscriber Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][6]/div[@class='x-window-tl']//span"));
		assertEquals("Events APN Ranking Window didn't launch.", "Events APN Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][7]/div[@class='x-window-tl']//span"));
		assertEquals("Access Area Ranking Window didn't launch.", "Access Area Ranking",
				selenium.getText("//div[@id='selenium_tag_baseWindow'][8]/div[@class='x-window-tl']//span"));
	}
}
