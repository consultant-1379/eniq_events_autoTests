/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.login;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertTrue;

/**
 * @author ericker
 * @since 2010
 *
 */
@Component
public class UserManagement {
    @Autowired
    private EricssonSelenium selenium;
    
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public static final String UI_USER = "UI Users";

    public static final String ADMIN_USER = "Admin Users";

    // Presumes that the user is on the 'User Management' page
    public void addUser(final Map<UserDetails, String> user, final String userType) {
        openAddUser();

        selenium.select("//*[@id='Account_Type']", "label=" + userType);
        for (final UserDetails detail : user.keySet()) {
            selenium.type(detail.getFieldName(), user.get(detail));
        }
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//input[@type='submit']");
        selenium.click("//input[@type='submit']");
    }

    public boolean isUserPresentInTable(final Map<UserDetails, String> user) {
        openUserManagement();
        final int rowNum = getRowNumber(user);
        if ((rowNum > 0)
                && (user.get(UserDetails.FIRST_NAME) + " " + user.get(UserDetails.LAST_NAME)).equals(selenium
                        .getText("//html/body/table/tbody/tr/td[2]/form/table/tbody/tr[" + rowNum + "]/td[1]"))
                && user.get(UserDetails.EMAIL).equals(
                        selenium.getText("//html/body/table/tbody/tr/td[2]/form/table/tbody/tr[" + rowNum + "]/td[4]"))) {
            return true;
        }// else
        return false;
    }

    private int getRowNumber(final Map<UserDetails, String> userMap) {
        int rowNo = 0;
        String value = "";
        String xpath = "";
        final int numRows = selenium.getXpathCount("//html/body/table/tbody/tr/td[2]/form/table/tbody/tr").intValue();
        for (int i = 1; i < numRows + 1; i++) { // Rows are 1 based
            xpath = "//html/body/table/tbody/tr/td[2]/form/table/tbody/tr[" + (i) + "]/td[2]";
            loggerDuplicate.log(Level.INFO, "The Element ID : " + xpath);
            value = selenium.getText(xpath);
            if (value.equals(userMap.get(UserDetails.LOGIN_NAME))) {
                rowNo = i;
                break;
            }
        }
        return rowNo;
    }

    private void openAddUser() {
        selenium.open(PropertyReader.getInstance().getAdminServerHost() + ":"
                + PropertyReader.getInstance().getAdminServerPort() + PropertyReader.getInstance().getAdminServerPath()
                + "servlet/UserManagement?action=adduser");
        selenium.waitForPageToLoad("30000");
    }

    /**
     * @param user
     */
    public void deleteUser(final Map<UserDetails, String> user) {
        openUserManagement();

        if (selenium.isElementPresent("//a[contains(@href, '/servlet/UserManagement?uid="
                + user.get(UserDetails.LOGIN_NAME) + "&action=deleteuser')]")) {
            loggerDuplicate.log(Level.INFO, "The Element ID : " + "//a[contains(@href, '/servlet/UserManagement?uid=" + user.get(UserDetails.LOGIN_NAME)
                    + "&action=deleteuser')]");
            selenium.click("//a[contains(@href, '/servlet/UserManagement?uid=" + user.get(UserDetails.LOGIN_NAME)
                    + "&action=deleteuser')]");
            // TODO: Update this when dialog is updated
            assertTrue("Confirmation dialog does not match expected text", selenium.getConfirmation().equals(
                    "Are you sure you want to delete the user?"));
            selenium.waitForPageToLoad("30000");
        }
    }

    public void openUserManagement() {
        // Open the user management page
        assertTrue("User management link not present", selenium.isElementPresent("link=User Management"));
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "link=User Management");
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
    }

    public void modifyUser(final Map<UserDetails, String> oldUser, final Map<UserDetails, String> newUser) {
        openUserManagement();

        // Open the modify page
        assertTrue("User modify link not present", selenium
                .isElementPresent("//a[contains(@href, '/servlet/UserManagement?uid="
                        + oldUser.get(UserDetails.LOGIN_NAME) + "&action=modifyuser')]"));
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//a[contains(@href, '/servlet/UserManagement?uid=" + oldUser.get(UserDetails.LOGIN_NAME)
                + "&action=modifyuser')]");
        selenium.click("//a[contains(@href, '/servlet/UserManagement?uid=" + oldUser.get(UserDetails.LOGIN_NAME)
                + "&action=modifyuser')]");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "Are you sure you want to modify the user?");
        assertTrue(selenium.getConfirmation().equals("Are you sure you want to modify the user?"));
        selenium.waitForPageToLoad("30000");

        // Do internal modification of user details
        for (final UserDetails detail : UserDetails.values()) {
            final String value = newUser.get(detail);
            if (value != null) {
                oldUser.put(detail, value);
                selenium.type(detail.getFieldName(), value);
            }
        }

        // Submit form
        loggerDuplicate.log(Level.INFO, "The Element ID : " + "//input[@type='submit']");
        selenium.click("//input[@type='submit']");
        selenium.waitForPageToLoad("30000");
    }
}
