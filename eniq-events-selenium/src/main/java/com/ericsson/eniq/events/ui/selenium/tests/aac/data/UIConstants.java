/**
*     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
* -----------------------------------------------------------------------
*/
package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;

/**
 * @author ejenkav
 * @since 2011
 *
 */
public abstract class UIConstants {

    public static final String BASE_URL = PropertyReader.getInstance().getAdminServerHost() + ":"
            + PropertyReader.getInstance().getAdminServerPort();

    public static final String LOGIN_PAGE_URL = BASE_URL + PropertyReader.getInstance().getAdminServerPath()
            + "servlet/LoaderStatusServlet";

    public static final String ENIQ_LOGIN_PAGE_URL = PropertyReader.getInstance().getEventHost() + ":"
            + PropertyReader.getInstance().getEventPort() + PropertyReader.getInstance().getPath() + "login/Login.jsp";

    public static final String USER_MGMT_URL = BASE_URL + "/adminui/servlet/UserManagement";

    public static final String PERMISSION_GROUPS_URL = BASE_URL + "/adminui/servlet/PermissionGroupManagement";

    public static final String ROLE_MGMT_URL = BASE_URL + "/adminui/servlet/RoleManagement";

    public static final String LOGGED_IN_MSG = "You are logged in as: ";

    public static final String LOGGED_IN_MSG_XPATH = "//body/table/tbody/tr[2]/td/table/tbody/tr/td[1]/font";
    
    public static final String OPTION_AFTER_LOGGED_IN_XPATH="//div[@class='popupContent']/div/div[last()]";

    public static final String PAGE_LOAD_TIMEOUT = "300000";

    public static final String LINK = "link=";

    public static final String ID = "id=";

    public static final String NAME = "name=";

    public static final String USERNAME_TEXTBOX = "username";

    public static final String PASSWORD_TEXTBOX = "password";

    public static final String SUBMIT_BUTTON = "submit";

    public static final String LOGOUT_LINK = LINK + "Logout";

    public static final String ENIQ_LOGGED_IN_MSG_XPATH = "//html/body/div[@class='headerPnl']/table/tbody/tr/td[3]/div";

    public static final String ENIQ_LOGGED_IN_MSG = "Options";
  												
    public static final String ENIQ_LOGOUT_BUTTON = "//html/body/div[3]/table/tbody/tr/td[2]/table/tbody/tr/td[10]/button";//button[@class='logOutBtn']";

    public static final String ENIQ_USERNAME_TEXTBOX = NAME + "userName";

    public static final String ENIQ_PASSWORD_TEXTBOX = NAME + "userPassword";

    public static final String ENIQ_SUBMIT_BUTTON = "css=button[type=SUBMIT]";

    public static final String ENIQ_TAB_CLASS_LOCKED_NO_LICENSE = "locked_no_license";

    public static final String ENIQ_TAB_CLASS_ITEM_DISABLE = "x-item-disabled";

    public static final String ENIQ_PASSWORD_CHANGE_MSG = "Password change required";

    public static final String ENIQ_NO_ROLE_MSG = "User does not have a role assigned. Please contact the system administrator";
    
    public static final int MAX_ROWS_RETURNED = 500;
}
