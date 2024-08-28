/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.tabs;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 * 
 */
@Component
public abstract class Tab {
	/**
	 * Shifted to new version of selenium(i.e WebDriver). Use webDriverSelenium
	 * instead of selenium. Now webDriver can be used to write new testcases.
	 * For example e webDriver in Sewe usssionBrowser package. We can get
	 * webDriver instance by calling webDriverSelenium.getWrappedDriver().
	 */
	@Deprecated
	@Autowired
	protected EricssonSelenium selenium;

	protected NewEricssonSelenium webDriverSelenium ;

	protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

	protected String tabName;

	protected String tabXPath;

	protected String tabButtonXPath;

	protected final String cascadeWindowsXPath;

	protected final String tileWindowsXPath;

	protected final String groupSubmitButtonXPath;

	protected final String searchSubmitButtonXPath;

	protected String searchFieldInputXPath;

	protected final String searchFieldArrowXPath;

	protected final String groupComboBoxArrowXPath;

	protected final String groupComboBoxInputXPath;

	protected final String groupInfoButtonXPath;

	protected final String typeButtonXPath;

	protected String dropGoupButtonXPath;

	protected final String groupSearchSubmitButtonXPath;

	protected static final String DEFAULT_TIMEOUT = "30000";

	protected Tab(final String tabId) {
		tabName = tabId;

		tabButtonXPath = "//li[contains(@id,'" + tabName + "')]/a[@class='x-tab-right']";
		tabXPath = "//div[@id='" + tabName + "']";

		cascadeWindowsXPath = tabXPath + "//table[@id='CASCADE']";
		tileWindowsXPath = tabXPath + "//table[@id='TILE']";
		groupSubmitButtonXPath = tabXPath + "//table[@id='selenium_tag_launchButton']//img";
		searchSubmitButtonXPath = tabXPath + "//div[@id='selenium_tag_launchButton']//img";
		groupSearchSubmitButtonXPath = tabXPath + "//div[@id='selenium_tag_launchButton']//img";

		searchFieldInputXPath = tabXPath + "//input[@id='selenium_tag_searchField-input']";

		searchFieldArrowXPath = tabXPath + "//div[@id='selenium_tag_searchField']//img[@class='x-form-trigger x-form-trigger-arrow']";
		groupComboBoxArrowXPath = tabXPath + "//div[@id='selenium_tag_groupComboBox']//img[@class='x-form-trigger x-form-trigger-arrow']";

		groupComboBoxInputXPath = tabXPath + "//input[@id='selenium_tag_groupComboBox-input']";
		groupInfoButtonXPath = tabXPath + "//table[@id='selenium_tag_infoButton']//button";
		dropGoupButtonXPath = tabXPath + "//table[@id='selenium_tag_toggleGroupButton']//button";
		typeButtonXPath = "//table[@id='selenium_tag_typeButton']//button";
	}

	public void openTab() {
		loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
		selenium.waitForElementToBePresent(tabButtonXPath, DEFAULT_TIMEOUT);
		selenium.click(tabButtonXPath);
	}

	public void cascadeWindowsStart() {
		clickStartMenu();
		loggerDuplicate.log(Level.INFO, "The Element ID : " + StaticStartMenuItems.CASCADE.getXPath());
		selenium.click(StaticStartMenuItems.CASCADE.getXPath());
	}

	public boolean isCascadeWindowsStartActive() {
		clickStartMenu();
		loggerDuplicate.log(Level.INFO, "The Element ID : " + StaticStartMenuItems.CASCADE.getXPath());
		if (selenium.getAttribute(StaticStartMenuItems.CASCADE.getXPath()).contains("x-item-disabled")) {
			return false;
		}
		return true;
	}

	public void tileWindowsStart() {
		clickStartMenu();
		loggerDuplicate.log(Level.INFO, "The Element ID : " + StaticStartMenuItems.TILE.getXPath());
		selenium.click(StaticStartMenuItems.TILE.getXPath());
	}

	public boolean isTileWindowsStartActive() {
		clickStartMenu();
		loggerDuplicate.log(Level.INFO, "The Element ID : " + StaticStartMenuItems.TILE.getXPath());
		if (selenium.getAttribute(StaticStartMenuItems.TILE.getXPath()).contains("x-item-disabled")) {
			return false;
		}
		return true;
	}

	public String getTabXPath() {
		return tabXPath;
	}

	public boolean isTabActive() {
		if (selenium.getAttribute(tabXPath + "@class").contains("x-hide-display")) {
			return false;
		}
		return true;
	}

	public void closeAllWindowsStart() {
		clickStartMenu();
		loggerDuplicate.log(Level.INFO, "The Element ID : " + StaticStartMenuItems.CLOSE_ALL.getXPath());
		selenium.click(StaticStartMenuItems.CLOSE_ALL.getXPath());
	}

	public boolean isCloseAllWindowsStartActive() {
		clickStartMenu();
		if (selenium.getAttribute(StaticStartMenuItems.CLOSE_ALL.getXPath()).contains("x-item-disabled")) {
			return false;
		}
		return true;
	}

	public void enterSearchValue(final String string, final boolean group) {
		if (group) {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + groupComboBoxArrowXPath);
			selenium.click(groupComboBoxArrowXPath);
			loggerDuplicate.log(Level.INFO, "The Element ID : " + groupComboBoxInputXPath);
			selenium.type(groupComboBoxInputXPath, string);
			selenium.typeKeys(groupComboBoxInputXPath, "");
		} else {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + searchFieldInputXPath);
			selenium.type(searchFieldInputXPath, string);
			selenium.typeKeys(searchFieldInputXPath, "");
		}
	}

	public void enterSubmit(final boolean group) {
		if (group) {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + groupComboBoxInputXPath);
			selenium.keyDown(groupComboBoxInputXPath, "\\13");
			selenium.keyUp(groupComboBoxInputXPath, "\\13");
			loggerDuplicate.log(Level.INFO, "The Element ID : " + groupSearchSubmitButtonXPath);
			selenium.click(groupSearchSubmitButtonXPath);
		} else {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + searchFieldInputXPath);
			selenium.keyDown(searchFieldInputXPath, "\\13");
			selenium.keyUp(searchFieldInputXPath, "\\13");
			loggerDuplicate.log(Level.INFO, "The Element ID : " + searchSubmitButtonXPath);
			selenium.click(searchSubmitButtonXPath);
		}
	}

	public String getValueFromDropDownButton(final int index) {
		final String searchValue = selenium.getText("//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]" + "//div[contains(@class,'x-view x-liveload-inner  x-unselectable') or contains(@class,'x-view x-liveload-inner x-unselectable')]" + "//div[" + index + "]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]" + "//div[contains(@class,'x-view x-liveload-inner  x-unselectable') or contains(@class,'x-view x-liveload-inner x-unselectable')]" + "//div[" + index + "]");
		return searchValue;
	}

	/**
	 * select a value from the options appeared when clicking the Group drop
	 * down button index 1 should select the first value
	 */
	public String getAGroupValueFromGroupDropDownButton(final int index) {
		clickDropDownButton(true);
		return getValueFromDropDownButton(index);
	}

	public void clickDropDownButton(final boolean group) {
		if (group) {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + groupComboBoxArrowXPath);
			selenium.click(groupComboBoxArrowXPath);
		} else {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + searchFieldArrowXPath);
			selenium.click(searchFieldArrowXPath);
		}
		selenium.waitForElementToBePresent("//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]", "12000");
	}

	public void selectValueFromDropDownedWindow(final int index) {
		selenium.mouseDown("//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]" + "//div[contains(@class,'x-view x-liveload-inner  x-unselectable') or contains(@class,'x-view x-liveload-inner x-unselectable')]" + "//div[" + index + "]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]" + "//div[contains(@class,'x-view x-liveload-inner  x-unselectable') or contains(@class,'x-view x-liveload-inner x-unselectable')]" + "//div[" + index + "]");
	}

	public String getAPNFromPropertiesWindow() {
		final String apnValue = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		return apnValue;
	}

	public String getAccessAreaFromPropertiesWindow() {
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[6]/td[2]");
		final String value1 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[6]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		final String value2 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[4]/td[2]");
		final String value3 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[4]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		final String value4 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		final String accessAreaValue = value1 + "," + value2 + "," + value3 + "," + value4;
		return accessAreaValue;
	}

	public String getControllerValueFromPropertiesWindow() {
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		final String value1 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		final String value2 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[5]/td[2]");
		loggerDuplicate.log(Level.INFO, "The Element ID : " + "xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		final String value3 = selenium.getText("xpath=/descendant::div[@class='x-window-body']//tr[2]/td[2]");
		final String controllerValue = value1 + "," + value2 + "," + value3;
		return controllerValue;
	}

	protected abstract void clickStartMenu();

	protected enum StaticStartMenuItems {
		CASCADE("CASCADE"), TILE("TILE"), CLOSE_ALL("CLOSE_ALL");

		private final String id;

		StaticStartMenuItems(final String name) {
			id = name;
		}

		public String getXPath() {
			return "//div[@id='x-menu-el-" + id + "']/a[@id='" + id + "']";
		}
	}

	public enum PacketSwitchedMenuOptions {
		CORE_VOICE("selenium_tag_Core - Voice"), CORE_DATA("selenium_tag_Radio - Voice & Data, Core - Data");

		private final String packetSwitchType;

		PacketSwitchedMenuOptions(final String name) {
			packetSwitchType = name;
		}

		private String getXPath() {
			return "//a[@id='" + packetSwitchType + "']";
		}
	}

	/**
	 * This method is for switching between: 'Radio - Voice & Data, Core - Data'
	 * and 'Core - Voice'
	 */
	public void setPacketSwitchedMenu(final PacketSwitchedMenuOptions packetSwitchedMenu) {
		final String packetSwitchMenuDropDownXPath = "//table[@id='selenium_tag_MetaDataChangeComponent']";
		loggerDuplicate.log(Level.INFO, "The Element ID : " + packetSwitchMenuDropDownXPath);
		selenium.click(packetSwitchMenuDropDownXPath);
		loggerDuplicate.log(Level.INFO, "The Element ID : " + packetSwitchedMenu.getXPath());
		final String packetSwitchType = packetSwitchedMenu.getXPath();
		selenium.click(packetSwitchType);
	}
}
