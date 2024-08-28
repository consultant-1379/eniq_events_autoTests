/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.common.constants;

public class SeleniumConstants {

    final public static String DEFAULT_TIMEOUT = "30000";

    final public static String TAB_NAME = "PLUS_TAB";

    final public static String TIME_RANGE_ELEMENT_XPATH = "//*[@id='selenium_tag_timeRangeDropDown']/div/table/tbody/tr/td[1]";

    final public static String DIMENSION_ELEMENT_XPATH = "//*[@id='selenium_tag_dimensionSelect']/div/table/tbody/tr/td[2]/div";

    final public static String DIMENSION_CATEGORY = "//*[@id='selenium_tag_categoryPanel']";

    final public static String DIMENSION_WINDOW_OPTION = "//div[contains(@class, \"GKH1R2KEX GKH1R2KDX\")]//div[contains(text(), ";

    final public static String DIMENSION_TEXTFIELD_ID = "//div[@id='selenium_tag_searchField']/div/table/tbody/tr//input";

    final public static String TERMINAL_NAME_TEXTFIELD_ID = "//*[@id='selenium_tag_searchFieldInput']/div/table/tbody/tr/td[1]/input";

    final public static String DIMENSION_TEXTFIELD_ARROW = "//div[@id='selenium_tag_searchField']/div/table/tbody/tr/td[2]/div";

    final public static String TEXT_FIELD_LIVELOAD_ALL_CONTENT = "//div[@class='suggestPopupMiddleCenterInner suggestPopupContent']/div/table/tbody/tr";

    final public static String DIMENSION_IMSI = "//input[@id='selenium_tag_textEntryBox']";

    final public static String DIMENSION_SECOND_VALUE = "//*[@id='selenium_tag_searchFieldInput']/div/table/tbody/tr/td[1]/input";

    final public static String DIMENSION_GROUP_TEXTFIELD_ID = "//div[@id='selenium_tag_groupSuggestBox']/div/table/tbody/tr/td[1]/input";

    final public static String DIMENSION_GROUP_TEXTFIELD_ARROW = "//div[@id='selenium_tag_groupSuggestBox']/div/table/tbody/tr/td[2]/div";

    final public static String TERMINAL_MAKE_XPATH = "//*[@id='selenium_tag_pairedSuggestBox']/div//input";

    final public static String WINDOW_FILTER_ID = "selenium_tag_windowFilter";

    final public static String LAUNCH_BUTTON_ID = "selenium_tag_launchWindowButton";

    final public static String DIMENTION_POP_UP_MENU = "//*[@class='suggestPopupMiddleCenterInner suggestPopupContent']";

    final public static String SECOND_DIMENTION_POP_UP_MENU = "//*[@class='suggestPopupMiddleCenterInner suggestPopupContent']";

    final public static String DIMENTION_GROUP_POP_UP_MENU = "//div[@class='popupContent']/div/div[1]";

    final public static String TERMINAL_POP_UP_MENU = "//*[@class='popupContent']";

    final public static int DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS = 0 * (1000 * 60); // In Milliseconds
    final public static int DURATION_TO_SLEEP_IN_CAUSE_CODE_FOOTER_TIME_RANGE_TESTS = 2 * (1000 * 60); // In Milliseconds

    final public static String WORKSPACES_SIDE_BAR_TAB_XPATH = "//div[text()='Workspaces']";

    /* -------------------------------- Time Range Constants ------------------------------------ */
    //15 minutes
    final public static String DATE_TIME_15 = "//div[@class='popupContent']//div/div[@__idx='0']";

    //30 minutes
    final public static String DATE_TIME_30 = "//div[@class='popupContent']//div/div[@__idx='1']";

    //1 hour
    final public static String DATE_TIME_1HOUR = "//div[@class='popupContent']//div/div[@__idx='2']";

    //2 hours
    final public static String DATE_TIME_2HOUR = "//div[@class='popupContent']//div/div[@__idx='3']";

    //6 hours
    final public static String DATE_TIME_6HOUR = "//div[@class='popupContent']//div/div[@__idx='4']";

    //12 hours
    final public static String DATE_TIME_12HOUR = "//div[@class='popupContent']//div/div[@__idx='5']";

    //1 day
    final public static String DATE_TIME_1DAY = "//div[@class='popupContent']//div/div[@__idx='6']";

    //1 week
    final public static String DATE_TIME_Week = "//div[@class='popupContent']//div/div[@__idx='7']";

    //custom
    final public static String DATE_TIME_Custom = "//div[@class='popupContent']//div/div[@__idx='8']";

    /* -------------------------------- Dimension Constants ------------------------------------ */

    final public static String RADIO_NETWORK_2G_ID = "//div[@class='popupContent']//div/div[@__idx='0']";

    /* -------------------------------- Dimension Constants ------------------------------------ */

    final public static String RADIO_NETWORK_2G = "//div[@class='popupContent']//div/div[@__idx='0']";

    final public static String RADIO_NETWORK_3G = "//div[@class='popupContent']//div/div[@__idx='0']";

    final public static String RADIO_NETWORK_4G = "//div[@class='popupContent']//div/div[@__idx='1']";

    final public static String CORE_NETWORK_PS = "//div[@class='popupContent']//div/div[@__idx='2']";

    final public static String CORE_NETWORK_CS = "//div[@class='popupContent']//div/div[@__idx='3']";

    final public static String APN = "//div[@class='popupContent']//div/div[@__idx='5']";

    final public static String CONTROLLER = "//div[@class='popupContent']//div/div[@__idx='6']";

    final public static String ACCESS_AREA = "//div[@class='popupContent']//div/div[@__idx='7']";

    final public static String SGSN_MME = "//div[@class='popupContent']//div/div[@__idx='8']";

    final public static String MSC = "//div[@class='popupContent']//div/div[@__idx='9']";

    final public static String TERMINAL = "//div[@class='popupContent']//div/div[@__idx='10']";

    final public static String IMSI = "//div[@class='popupContent']//div/div[@__idx='11']";

    final public static String PTIMSI = "//div[@class='popupContent']//div/div[@__idx='12']";

    final public static String MSISDN = "//div[@class='popupContent']//div/div[@__idx='13']";

    final public static String TRACKING_AREA = "//div[@class='popupContent']//div/div[@__idx='14']";

    final public static String APN_GROUP = "//div[@class='popupContent']//div/div[@__idx='16']";

    final public static String CONTROLLER_GROUP = "//div[@class='popupContent']//div/div[@__idx='17']";

    final public static String ACCESS_AREA_GROUP = "//div[@class='popupContent']//div/div[@__idx='18']";

    final public static String SGSN_MME_GROUP = "//div[@class='popupContent']//div/div[@__idx='19']";

    final public static String MSC_GROUP = "//div[@class='popupContent']//div/div[@__idx='20']";

    final public static String TERMINAL_GROUP = "//div[@class='popupContent']//div/div[@__idx='21']";

    final public static String IMSI_GROUP = "//div[@class='popupContent']//div/div[@__idx='22']";

    final public static String TRACKING_AREA_Group = "//div[@class='popupContent']//div/div[@__idx='23']";

    final public static String CAUSE_CODE_HIGH_CHART = "//div[contains(@class,'x-window-mr')]//div[contains(@id, 'highcharts')]";

    /*-------------X-Paths for side launch bar --------------------------------------------------*/

    final public static String SIDE_LAUNCH_BAR_OPEN = "//img[@title='Show']";

    //x-path for closing the launcher side bar
    final public static String SIDE_LAUNCH_BAR_CLOSE = "//img[@title='Hide']";

    /*-------------X-Paths for Customize Date Picker --------------------------------------------------*/
    final public static String DATE_PICKER_START_MONTH_YEAR = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//td[@class='datePickerMonth']";

    final public static String DATE_PICKER_START_DATE_PERVIOUS_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//div[contains(@class, 'datePickerPreviousButton')]";

    final public static String DATE_PICKER_END_TAG = "']";

    final public static String DATE_PICKER_AND_TABINDER_IS_EQUAL_TO_ZERO = "' and @tabindex='0";

    final public static String DATE_PICKER_STARTDATE_CALENDER_DAY = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[1]//table[@class='datePickerDays']//td/div[text()='";

    final public static String DATE_PICKER_ENDDATE_CALENDER_DAY = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//table[@class='datePickerDays']//td/div[text()='";

    final public static String DATE_PICKER_START_DATE_HOUR_DROPDOWN_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]//td[1]/div/div/table//tbody/tr/td[2]";

    final public static String DATE_PICKER_START_DATE_MIN_DROPDOWN_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]//td[2]/div/div/table//tbody/tr/td[2]";

    final public static String DATE_PICKER_SELECT_START_DATE_HOUR_OPTION_PATH = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='";

    final public static String DATE_PICKER_SELECT_START_DATE_MIN_OPTION_PATH = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[1]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='";

    final public static String DATE_PICKER_END_MONTH_YEAR = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//td[@class='datePickerMonth']";

    final public static String DATE_PICKER_END_DATE_PERVIOUS_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td[3]//div[contains(@class, 'datePickerPreviousButton')]";

    final public static String DATE_PICKER_END_DATE_HOUR_DROPDOWN_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]//td[1]/div/div/table//tbody/tr/td[2]";

    final public static String DATE_PICKER_END_DATE_MIN_DROPDOWN_BUTTON = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]//td[2]/div/div/table//tbody/tr/td[2]";

    final public static String DATE_PICKER_SELECT_END_DATE_HOUR_OPTION_PATH = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='";

    final public static String DATE_PICKER_SELECT_END_DATE_MIN_OPTION_PATH = "//div[@class='popupContent']/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/table/tbody/tr[3]/td/table/tbody/tr/td/div/div[1]/div//div[text()='";

    final public static String DATE_PICKER_OK_BUTTON = "//button[text()='Ok']";

    /*-------------X-Paths for Call Failure Analysis Windows --------------------------------------------------*/
    public static final String CALL_FAILURE_ANALYSIS_WINDOWS_SPAN_ID = "//span[contains(text(),'Call Failure Analysis')]";

    public static final String CALL_FAILURES_ANALYSIS_DROPDOWN_ARROW_XPATH = "//table[@class='container']/tbody/tr[1]/td[1]/div[1]/div[1]/table/tbody/tr/td[2]";

    public static final String CALL_FAILURES_ANALYSIS_WINDOW_LAUNCH_BUTTON = "//button[contains(text(),'Launch')]";

    /*-------------X-Paths for LTE Call Failure Network Event Volume Windows -----------------------------------*/

    public static final String GRID_LAUNCH_BUTTON = "//*[@id='x-auto-77']/button";

    public static final String TIME_COLUMN_HEADING = "//*[@id='x-auto-156']";

    public static final String INTERNAL_PROC_RRC_CONN_SETUP_Count_COLUMN_HEADING = "//*[@id='x-auto-158']";

    public static final String INTERNAL_PROC_S1_SIG_CONN_SETUP_COLUMN_HEADING = "//*[@id='x-auto-160']";

    public static final String INTERNAL_PROC_INITIAL_CTXT_SETUP_COLUMN_HEADING = "//*[@id='x-auto-162']";

    public static final String INTERNAL_PROC_ERAB_SETUP_COLUMN_HEADING = "//*[@id='x-auto-164']";

    public static final String INTERNAL_PROC_UE_CTXT_RELEASE_COLUMN_HEADING = "//*[@id='x-auto-166']";

    public static final String CHART_VIEW = "//*[@id='x-auto-79']";

    public static final String SELECT_ALL = "//*[@id='selenium_tag_baseWindow']/div[2]/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div[3]/div[2]/div[1]//span/input[@type='checkbox']";

    public static final String CHART_LAUNCH_BUTTON = "//button[contains(text(),'Launch')]";

    public static final String GRID_HEADINGS = "//*[@id='selenium_tag_baseWindow']/div[2]/div[1]/div/div/div[2]";

    /* 4G Core User Constants */

    public static final String FOUR_G_CORE_USER_ID = "4GCoreUser";

    public static final String FOUR_G_CORE_USER_PASSWORD = "4GCoreUser";

}
