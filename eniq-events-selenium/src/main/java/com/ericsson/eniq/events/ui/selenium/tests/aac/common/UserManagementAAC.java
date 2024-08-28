/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac.common;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.AdminUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ejenkav
 * @since 2011
 *
 */
@Component
public class UserManagementAAC {
    private final EricssonSelenium selenium;

    @Autowired
    public UserManagementAAC(final EricssonSelenium sel) {
        selenium = sel;
    }

    public boolean logIn(String username, final String password) {
        username = username.toLowerCase();
        if (isLoggedInAsUser(username)) {
            return true;
        }
        logOut();

        selenium.open(UIConstants.LOGIN_PAGE_URL);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type(UIConstants.USERNAME_TEXTBOX, username);
        selenium.type(UIConstants.PASSWORD_TEXTBOX, password);
        selenium.click(UIConstants.SUBMIT_BUTTON);
        selenium.waitForElementToBePresent(UIConstants.LOGGED_IN_MSG_XPATH, UIConstants.PAGE_LOAD_TIMEOUT);
        return isLoggedInAsUser(username);
    }

    public boolean logOut() {
        if (isLoggedIn()) {
            selenium.click(UIConstants.LOGOUT_LINK);
            selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        }
        return true;
    }

    public boolean createUser(final User user) {
        selenium.open(UIConstants.USER_MGMT_URL);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.click("css=button.addButton");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type("id=userId", user.getUserID().toLowerCase());
        selenium.type("id=password", user.getPassword());
        selenium.type("id=confirmPassword", user.getConfirmPassword());
        selenium.type("id=firstName", user.getFirstName());
        selenium.type("name=lastName", user.getLastName());
        selenium.type("id=email", user.getEmail());
        selenium.type("id=phone", user.getPhone());
        selenium.type("id=organization", user.getOrganization());
        for (final String roleElement : user.getRoles()) {
            selenium.addSelection("id=userRoles", "label=" + roleElement.toLowerCase());
        }

        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("User " + user.getUserID().toLowerCase() + " is created");
    }

    public boolean createUser(final String uid, final String password, final String confirmPassword,
            final String firstName, final String lastName, final String email, final String phone,
            final String organization, final String roleNames) {
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setUserID(uid);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setPassword(password);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setConfirmPassword(confirmPassword);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setFirstName(firstName);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setLastName(lastName);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setPhone(phone);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setEmail(email);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setOrganization(organization);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_USER.setRoles(roleNames);
        return createUser(AdminUIBaseSeleniumTest.TEST_FIXTURE_USER);
    }

    public boolean verifyUser(final User user) {
        return verifyFirstName(user.getUserID(), user.getFirstName())
                && verifyLastName(user.getUserID(), user.getLastName())
                && verifyRoles(user.getUserID(), user.getRolesAsString());
    }

    public boolean verifyFirstName(final String userID, final String firstName) {
        return verifyNames(userID, "//table[@cellspacing= '12']/tbody/tr[2]/td[2]", firstName, true);
    }

    public boolean verifyLastName(final String userID, final String lastName) {
        return verifyNames(userID, "//table[@cellspacing= '12']/tbody/tr[2]/td[2]", lastName, false);
    }

    public boolean verifyEmail(final String userID, final String email) {
        return verifyAttribute(userID, "//table[@cellspacing= '12']/tbody/tr[3]/td[2]", email);
    }

    public boolean verifyPhone(final String userID, final String phone) {
        return verifyAttribute(userID, "//table[@cellspacing= '12']/tbody/tr[4]/td[2]", phone);
    }

    public boolean verifyOrganization(final String userID, final String organization) {
        return verifyAttribute(userID, "//table[@cellspacing= '12']/tbody/tr[5]/td[2]", organization);
    }

    public boolean verifyRoles(String userID, final String roles) {
        final String[] allRoles = (roles.toLowerCase().split(","));
        final String elementLocator = "//table[@cellspacing= '12']/tbody/tr[6]/td[2]";
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }

        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=viewUserDetails");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        final String existingRoles = selenium.getText(elementLocator).toLowerCase();
        for (final String allRolesElement : allRoles) {
            if (!existingRoles.contains(allRolesElement)) {
                return false;
            }
        }
        return true;
        //return verifyAttribute(userID, "", roles);
    }

    /*public boolean verifyRoles(final String userID, String roles) {
        roles = (roles.toLowerCase().replace(",", "\n "));
        return verifyAttribute(userID, "//table[@cellspacing= '12']/tbody/tr[6]/td[2]", roles);
    }*/

    public String[] getRoles(String userID) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return null;
        }
        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=viewUserDetails");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.getText("//table[@cellspacing= '12']/tbody/tr[6]/td[2]").split("\n ");
    }

    private boolean verifyAttribute(String userID, final String elementLocator, final String newValue) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }

        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=viewUserDetails");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return newValue.equals(selenium.getText(elementLocator));
    }

    private boolean verifyNames(String userID, final String elementLocator, final String newValue,
            final boolean isFirstName) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }

        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=viewUserDetails");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        final String name = selenium.getText(elementLocator);
        if (isFirstName) {
            return name.startsWith((newValue + " "));
        }
        return name.endsWith(" " + newValue);
    }

    public boolean modifyFirstName(final String userID, final String firstName) {
        return modifyAttribute(userID, "id=firstName", firstName) && verifyFirstName(userID, firstName);
    }

    public boolean modifyLastName(final String userID, final String lastName) {
        return modifyAttribute(userID, "name=lastName", lastName) && verifyLastName(userID, lastName);

    }

    public boolean modifyEmail(final String userID, final String email) {
        return modifyAttribute(userID, "id=email", email) && verifyEmail(userID, email);

    }

    public boolean modifyPhone(final String userID, final String phone) {
        return modifyAttribute(userID, "id=phone", phone) && verifyPhone(userID, phone);

    }

    public boolean modifyOrganization(final String userID, final String organization) {
        return modifyAttribute(userID, "id=organization", organization) && verifyOrganization(userID, organization);

    }

    public boolean modifyPassword(String userID, final String password, final String confirmPassword) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }
        boolean logoutFlag = false;
        if (isLoggedInAsUser(userID)) {
            logoutFlag = true;
        }

        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=editUser");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type("id=password", password);
        selenium.type("id=confirmPassword", confirmPassword);
        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        if (logoutFlag) {
            return selenium
                    .isTextPresent("Your changes have been saved successfully, please login again using your new password");
        }
        return selenium.isTextPresent("User " + userID + " is modified");

    }

    public boolean modifyRoles(String userID, final String roleSet) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }
        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=editUser");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);

        final Set<String> optionVALUES = new HashSet<String>(Arrays.asList(selenium.getSelectOptions("id=userRoles")));
        for (final String newRoleSetElement : roleSet.split(",")) {
            if (!optionVALUES.contains(newRoleSetElement.toLowerCase())) {
                return false;
            }
        }

        final String[] existingRoleSet = selenium.getSelectedValues("id=userRoles");
        for (final String existingRoleSetElement : existingRoleSet) {
            selenium.removeSelection("id=userRoles", existingRoleSetElement);
        }
        for (final String newRoleSetElement : roleSet.split(",")) {
            selenium.addSelection("id=userRoles", newRoleSetElement.toLowerCase());
        }

        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("User " + userID + " is modified") && verifyRoles(userID, roleSet);
    }

    private boolean modifyAttribute(String userID, final String elementLocator, final String newValue) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }

        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=editUser");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type(elementLocator, newValue);
        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("User " + userID + " is modified");

    }

    public boolean deleteUser(String userID) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }
        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=deleteUser");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return (!isUserExist(userID));
    }

    public boolean unlockUser(String userID) {
        userID = userID.toLowerCase();
        if (!isUserExist(userID)) {
            return false;
        }
        selenium.open(UIConstants.USER_MGMT_URL + "?uid=" + userID + "&action=unlockUser");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("User " + userID + " is unlocked");
    }

    public boolean isUserExist(String userID) {
        userID = userID.toLowerCase();
        selenium.open(UIConstants.USER_MGMT_URL);
        final int rowNum = getRowNumber(userID);
        if (rowNum > 0) {
            return true;
        }
        return false;

    }

    private int getRowNumber(String userID) {
        userID = userID.toLowerCase();
        int rowNo = 0;
        String value = "";
        String xpath = "";
        final int numRows = selenium.getXpathCount("//table[@class= 'sortable']/tbody/tr").intValue();
        for (int i = 1; i < numRows + 1; i++) { // Rows are 1 based
            xpath = "//table[@class= 'sortable']/tbody/tr[" + (i) + "]/td[1]";
            value = selenium.getText(xpath);
            if (value.equals(userID) || value.equals(userID + "*") || value.equals(userID + "**")) {
                rowNo = i;
                break;
            }
        }
        return rowNo;
    }

    public boolean isLoggedIn() {

        if ((selenium.isElementPresent(UIConstants.LOGGED_IN_MSG_XPATH))
                && (selenium.getText(UIConstants.LOGGED_IN_MSG_XPATH).contains(UIConstants.LOGGED_IN_MSG))) {
            return true;
        }
        return false;
    }

    public boolean isLoggedInAsUser(String username) {
        username = username.toLowerCase();
        if (!isLoggedIn()) {

            return false;
        }
        if (!(selenium.getText(UIConstants.LOGGED_IN_MSG_XPATH).contains(UIConstants.LOGGED_IN_MSG + username))) {
            return false;
        }
        return true;
    }

    public boolean isLoggedInAsAdmin() {
        return isLoggedInAsUser(PropertyReader.getInstance().getAdminUser());
    }

    public boolean logInAsAdmin() {
        return logIn(PropertyReader.getInstance().getAdminUser(), PropertyReader.getInstance().getAdminPwd());
    }

}
