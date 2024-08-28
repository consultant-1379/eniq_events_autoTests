package com.ericsson.eniq.events.ui.selenium.core;

/**
 *     Copyright (C) 2015 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
/**
 *
 * Complete this class when MetaReader more complete. All String constants used in meta JSON file should be maintained from here (only)
 *
 * MeaderReader should be only user of these (to maintain).
 *
 */
public abstract class Constants {

    public static final String NETWORK = "NETWORK";

    public static final String TERMINAL = "TERMINAL";

    public static final String SUBSCRIBER = "SUBSCRIBER";

    public static final String RANKINGS = "RANKINGS";

    public static final String[] TABNAME = { "NETWORK", "TERMINAL", "SUBSCRIBER", "RANKINGS" };

    public static final String TAB0 = "NETWORK";

    public static final String[] TAB0_STARTMENU = { "NETWORK_CAUSE_CODE_ANALYSIS", "NETWORK_EVENT_ANALYSIS" };

    public static final String TAB1 = "TERMINAL";

    public static final String[] TAB1_STARTMENU = { "TERMINAL_ANALYSIS", "TERMINAL_EVENT_ANALYSIS", "TERMINAL_RANKINGS" };

    public static final String TAB2 = "SUBSCRIBER";

    public static final String[] TAB2_STARTMENU = { "SUBSCRIBER_EVENT_ANALYSIS", "SUBSCRIBER_RANKING" };

    public static final String TAB3 = "RANKINGS";

    public static final String[] TAB3_STARTMENU = { "RANKING_EVENT_ANALYSIS" };

    public static final String MINIMIZE = "//*[@class=' x-nodrag x-tool-minimize x-tool']";

    public static final String MAXIMIZE = "//*[@class=' x-nodrag x-tool-maximize x-tool']";

    public static final String RESTORE = "//*[@class=' x-nodrag x-tool-restore x-tool ']";

    public static final String CLOSE = "//*[@class=' x-nodrag x-tool-close x-tool']";

    public static final String WINDOW_TAG = "selenium_tag_BaseWindow_";

    public static final String TASKBAR_BUTTON_TAG = "selenium_tag_MenuTaskBarButton_";

    public static final String MENU_CASCADE = "link=Cascade";

    public static final String MENU_TILE = "link=Tile";

    public static final String TASKBAR_CASCADE = "]/div/div/table/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/em/button";

    public static final String TASKBAR_TILE = "]/div/div/table/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr[2]/td[2]/em/button";

    public static final String TIME_BUTTON_LOCATOR = "//table[@id='btnTime']/tbody/tr[2]/td[2]/em/button";

    public static final String TIME_RADIO_BUTTON = "//div[@id='selenium_tag_timeRange']/input";

    public static final String DATE_TIME_RADIO_BUTTON = "//div[@id='selenium_tag_dateTimeRange']/input";

    public static final String TIME_WINDOW_OK = "//td[2]/table/tbody/tr/td[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/em/button";

    public static final String TIME_WINDOW_CANCEL = "//td[2]/table/tbody/tr/td[1]/table/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/em/button";

    public static final String[] TIME_DROP_DOWN = { "30 Mins", "1 Hour", "2 Hours" };

    public static final String REFRESH_BUTTON = "//td[11]/table/tbody/tr[2]/td[2]/em/button";

    public static final String OPTIONS_BUTTON = "//div[@id='headerPnl']//div[text()='Options']";

    final public static String ABOUT = "//div[@class='popupContent']//div[text()='About']";

    final public static String EE_RELEASE_VERSION = "//*[@id='x-auto-38']/tbody/tr[2]/td/table/tbody/tr[1]/td/div";

    final public static String KPI_THRESHOLD_NOTIFICATION_LICENSE_INFO = "//*[@id='x-auto-38']/tbody/tr[2]/td/table/tbody/tr[2]/td/div/div/div/div[16]/div[2]";

    public static final String EVENTS_LOG_OUT_BUTTON = "//div[@class='popupContent']//div[contains(text(),'Log Out')]";

    public static final String EVENTS_LOG_IN_BUTTON = "//div[contains(@class, 'login_submit') and @role='button']//input[1]";

    public static final String ADMIN_LOG_IN_BUTTON = "j_security_check";

    public static final String ADMIN_LOG_OUT_BUTTON = "link=Logout";

    public static final String USER_GUIDE_BUTTON = "//div[@id='x-auto-1']/div[3]/div/table[2]/tbody/tr/td[2]/button";

    public static final String ABOUT_BUTTON = "//button[@type='button']";

    public static final String USER_NAME_LABEL = "//div[@id='x-auto-1']/div[3]/div/table[1]/tbody/tr/td[2]/span";

    public static final String LOGO_URL = "url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACsAAAAmCAYAAABdw2IeAAADuElEQVR42u2YaUiUQRjHNzutrIguazu0w6ILCQkjDbMPWRK1mWEGFXZQUhlJ0QHanV3Q+SExCwILLAJNqEAkwgg/dJcRdJlFVFtqaWau/Z/4vzC8bO27grNL7As/1nmcmff/zjzPzDNjs2l4Wlpa2oFocBjcALnAbvOnB4KGg43gDnCZEFuwrwX2BamgCHxzI1IlyhcCu4CZ4Cz44EGgQRMYoVNkJNgDnlgUqLJbh8D2YCW4BX55KfAlOAam6hrNHC8FfgWFwKE1oPCynhZ9UvyxDKSrSxT+7sElzK5DbAh4+w+RD0E2GGtaY+PBaVDNep/BCh2CM0wC33GRjzPVm+gh+L6DwToET+cILpSpVewDwRq6gJXgi9a9xnYHC8AlBpPVwHshMaBr+ZoGToGqVqyxpWCSDqHj/7LXe0J8d68WkRTakZuBVYHVDL54XyUpnvxSorwYLFZ9ksG3GuSDzWpgtqWv3nQjsBmUg/UgTKkfDOaBC8BpanNVZqqtBY+hMBnhSnDA7Icox4AT4LWHWYjUlfnbZaQVWwTYDu564dPjdPpwf5AGroEGL1cH8d0gHSIl0S5w44cui8GXqs5KWwrNbMUaW8EVYJTOaQ9h4mJF4HNwEEwWH/fFOtvLQz77EZwHiaCbPxyvzScFCazrYDkI9be7gCAKy6UfRtgCT+AJPP/Bg2iexTRuPkjmuV7urmbzYiKZB0RJDztwDY1V2seCTSSWtgm8PdwibWkbzZPxVtmulYQokbYloLdyGpFz3RSWh4Ekm5KAyO9PcJEZVTPtxk3gGemM9gq+KI//a2SeUMY7gnrwBbxiLiu7WC2oo62US2AB29fyt5IJ/hEll+jDZMllo0BpPJK5aigIZ+U8Cqxm/hrGjmUTSGAdybj6gc78yKO0Z3KGZDZ20JbNhFzqzqCtiCfkDSxL3V3KprMPLDLEOjmqb3jbsp+CxfaeZ64fYBtzAxnpEpDFzhLcZGTGbD2mG0WBGtqe8eXrWE5hu1CWC7lLNjM3lsE5boiVoX7AI4kQB4ZQoHR8m52IT3Vi/RJlJNLdxMFQ+ms9XaQrGEC/rmEfh9h+rZK8u5hbGFu6g4NTZ4iVm5NHYBl9I1Fxg5OSkPCi7T6T7Uaew8LZSQNHPUXurxiMafzoKtaZC1bxrqHSmBF+jGRxSXQHFwPdEDtI8d8/Yu+xgZN+WcwpkfNTDr/6HF8cw/qXlWukck6ZfPQVsJQf0MQ+ZMrncDSb6P8ZbO9gKiltP4GdtGfxfXbOyFMp/wZWqp3xIn5sNQAAAABJRU5ErkJggg==\")";

    public static final String SUBSCRIBER_EVENT_ANALYSIS_PAGING_LOCATOR = "//div[@id='selenium_tag_BaseWindow_SUBSCRIBER_EVENT_ANALYSIS' and @class=' x-window ']//div[@class='my-paging-display ']";

    public static final String FIRSTNAME = "test";

    public static final String LASTNAME = "test";

    public static final String NAME = FIRSTNAME + " " + LASTNAME;

    public static final String LOGIN = "login";

    public static final String PWORD = "Test&7";

    public static final String EMAIL = PWORD + "@email.com";

    public static final String[] USER1 = { FIRSTNAME + "1", LASTNAME + "1", FIRSTNAME + "1" + " " + LASTNAME + "1", LOGIN + "1", PWORD + "1",
            EMAIL + "1" };

    public static final String[] USER2 = { FIRSTNAME + "2", LASTNAME + "2", FIRSTNAME + "2" + " " + LASTNAME + "2", LOGIN + "2", PWORD + "2",
            EMAIL + "2" };

    public static final String[] USER3 = { FIRSTNAME + "3", LASTNAME + "3", FIRSTNAME + "3" + " " + LASTNAME + "3", LOGIN + "3", PWORD + "3",
            EMAIL + "3" };

    public static final String[] USER4 = { FIRSTNAME + "4", LASTNAME + "4", FIRSTNAME + "4" + " " + LASTNAME + "4", LOGIN + "4", PWORD + "4",
            EMAIL + "4" };

    public static final String WARNING_NO_DATA = "WARNING! No data available. Please check if data exists or not!";

    public static final String ERROR_NO_MAP = "ERROR! No Map installed, or cannot connect to Map Server. Please check and re-run test";

    public static final String EXCEPTION_END_TIME = "23:45";
}
