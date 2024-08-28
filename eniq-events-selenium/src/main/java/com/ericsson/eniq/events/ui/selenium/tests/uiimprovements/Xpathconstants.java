package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

public class Xpathconstants {


//X-Paths	
public static final String MULTIPLE_VIEWS =  "//div[@id='selenium_tag_MultiWinSelectComponent']/input";

public static final String NETWORK_SUBMIT_BUTTON = "//div[@id='" + "NETWORK_TAB" + "']//div[@id='selenium_tag_launchButton']/img";

public static final String TERMINAL_SUBMIT_BUTTON = "//div[@id='" + "TERMINAL_TAB" + "']//div[@id='selenium_tag_launchButton']/img";

public static final String SUBSCRIBER_SUBMIT_BUTTON = "//div[@id='" + "SUBSCRIBER_TAB" + "']//div[@id='selenium_tag_launchButton']/img";

public static final String SEARCHFIELD_INPUT_XPATH = "//div[@id='" + "NETWORK_TAB" + "']//input[@id='selenium_tag_searchField-input']";

public static final String ENTER_TERMINAL_MAKE = "//div[@id='" + "TERMINAL_TAB" + "']//input[@id='selenium_tag_typesCombo-input']";

public static final String ENTER_TERMINAL_XPATH = "//div[@id='" + "TERMINAL_TAB" + "']//input[@id='selenium_tag_searchField-input']";

public static final String TIME_SETTINGS_BUTTON = "//table[@id='btnTime']//button";

public static final String REFRESH_BUTTON = "//table[@id='btnRefresh']//button";

public static final String BTNHIDE_SHOW_LEGEND = "//table[@id='btnHideShowLegend']//button";

public static final String MULTIVIEW_OK_BUTTON = "//div[(@id='selenium_tag_MultiWinWarningDialog')]//button[name()='button' and contains(text(), 'Ok')]";


public static final String MULTIVIEW_CANCEL_BUTTON = "//div[(@id='selenium_tag_MultiWinWarningDialog')]//button[name()='button' and contains(text(), 'Cancel')]";

public static final String LAUNCH_BUTTON = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]/div//div[button='Launch']/button";

public static final String CHART_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]/div//div[label='Chart View']/input";

public static final String GRID_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]/div//div[label='Grid View']/input";

public static final String TIME_RANGE_COMP = "//div[@id='timeRangeComp']/input";

public static final String SELECT_ALL = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]//span/input[@id='gwt-uid-1']";

public static final String TABLE_HEADER_EA  = "//div[contains(@id, 'NETWORK_EVENT_ANALYSIS')]//table//div[span='Event Type']/a ";

public static final String ON_MOUSE_PATH = "//div[contains(@class, 'x-ignore x-menu x-component')]//div/a[contains(@class,' x-menu-item x-menu-item-arrow x-component')]";

public static final String  OPTIONS_BY_DEFAULT_CHECKED = "//a[contains(@class, 'x-menu-checked')][contains(text(),'";

public static final String NEW_HEADERS = "//a[contains(@class, 'x-menu-check')][contains(text(),'";

public static final String NETWORK_EVENT_ANALYSIS = "//div[@id='selenium_tag_BaseWindow_NETWORK_EVENT_ANALYSIS']";

public static final String TERMINAL_EVENT_ANALYSIS = "//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']";

public static final String CAUSE_CODE_ANALYSIS_CHART_VIEW_OPTION_BUTTON = "//div[@class='x-panel-body wizard-top-body']//div/table/tbody/tr/td/div/table/tbody/tr/td[1]/div/input";

public static final String CAUSE_CODE_ANALYSIS_GRID_VIEW_OPTION_BUTTON = "//div[@class='x-panel-body wizard-top-body']//div/table/tbody/tr/td/div/table/tbody/tr/td[2]/div/input";

public static final String CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_DISABLED = "//div[@id='allCheckBoxPanel']//div[@class='x-panel-body x-panel-body-noheader wizard-content-body'] /span[@class='gwt-CheckBox chk-all x-component chk-all-disabled gwt-CheckBox-disabled']/input";

public static final String CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_ENABLED = "//div[@id='allCheckBoxPanel']//div[@class='x-panel-body x-panel-body-noheader wizard-content-body'] /span[@class='gwt-CheckBox chk-all x-component']/input";

public static final String CAUSE_CODE_ANALYSIS_LAUNCH_BUTTON = "//div/button[text()='Launch']";

//div[contains(@class, 'gwt-Image')]/img
//div[contains(@class, 'gwt-Image')]/img

}
