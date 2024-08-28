/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac.common;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.AdminUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.Role;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
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
public class RoleManagement {
    private final EricssonSelenium selenium;

    @Autowired
    public RoleManagement(final EricssonSelenium sel) {
        selenium = sel;
    }

    public boolean createRole(final Role role) {
        selenium.open(UIConstants.ROLE_MGMT_URL);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.click("css=button.addButton");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type("id=roleName", role.getName().toLowerCase());
        selenium.type("id=roleTitle", role.getTitle());
        selenium.type("id=roleDesc", role.getDescription());

        for (final String permissionGroupElement : role.getpermissionGroupSet()) {
            selenium.addSelection("name=rolePermGroups", "label=" + permissionGroupElement.toLowerCase());
        }
        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Role " + role.getName().toLowerCase() + " is created");
    }

    public boolean createRole(final String roleName, final String title, final String description,
            final String perimssionGroups) {
        AdminUIBaseSeleniumTest.TEST_FIXTURE_ROLE.setName(roleName);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_ROLE.setTitle(title);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_ROLE.setDescription(description);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_ROLE.setPermissionGroups(perimssionGroups);
        return createRole(AdminUIBaseSeleniumTest.TEST_FIXTURE_ROLE);
    }

    public boolean verifyRole(final Role role) {
        return verifyTitle(role.getName(), role.getTitle())
                && verifyPermissionGroups(role.getName(), role.getpermissionGroupsAsString());
    }

    public boolean verifyTitle(final String roleName, final String title) {
        return verifyAttribute(roleName, "//table[@cellspacing= '12']/tbody/tr[2]/td[2]", title);
    }

    public boolean verifyDescription(final String roleName, final String description) {
        return verifyAttribute(roleName, "//table[@cellspacing= '12']/tbody/tr[3]/td[2]", description);
    }

    public boolean verifyPermissionGroups(String roleName, final String permissionGroups) {

        final String[] allPermissionGroups = (permissionGroups.toLowerCase().split(","));
        final String elementLocator = "//table[@cellspacing= '12']/tbody/tr[4]/td[2]";
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return false;
        }

        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=viewRolePermGroups");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        final String existingPermissionGroups = selenium.getText(elementLocator).toLowerCase();
        for (final String allPermissionGroupsElement : allPermissionGroups) {
            if (!existingPermissionGroups.contains(allPermissionGroupsElement)) {
                return false;
            }
        }
        return true;

    }

    private boolean verifyAttribute(String roleName, final String elementLocator, final String newValue) {
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return false;
        }

        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=viewRolePermGroups");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return newValue.equals(selenium.getText(elementLocator));
    }

    public String[] getPermissionGroups(String roleName) {
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return null;
        }
        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=viewRolePermGroups");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.getText("//table[@cellspacing= '12']/tbody/tr[4]/td[2]").split("\n ");
    }

    private boolean modifyRoleAttribute(String roleName, final String elementLocator, final String newValue) {
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return false;
        }
        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=editRole");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type(elementLocator, newValue);
        // Remove this at later stages
        selenium.addSelection("//select[@name='rolePermGroups']", "allpermissions");
        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Role " + roleName + " is modified");

    }

    public boolean modifyTitle(final String roleName, final String newTitle) {
        return modifyRoleAttribute(roleName, "id=roleTitle", newTitle) && verifyTitle(roleName, newTitle);

    }

    public boolean modifyDescription(final String roleName, final String newDescription) {
        return modifyRoleAttribute(roleName, "id=roleDesc", newDescription)
                && verifyDescription(roleName, newDescription);

    }

    public boolean modifyPermissionGroupSet(String roleName, final String newGroupSet) {
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return false;
        }
        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=editRole");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);

        final Set<String> optionVALUES = new HashSet<String>(Arrays.asList(selenium
                .getSelectOptions("//select[@name='rolePermGroups']")));
        for (final String newPermissionGroupSetElement : newGroupSet.split(",")) {
            if (!optionVALUES.contains(newPermissionGroupSetElement.toLowerCase())) {
                return false;
            }
        }

        final String[] existingPermissionGroupSet = selenium.getSelectedValues("//select[@name='rolePermGroups']");
        for (final String existingPermissionGroupSetElement : existingPermissionGroupSet) {
            selenium.removeSelection("//select[@name='rolePermGroups']", existingPermissionGroupSetElement);
        }
        for (final String newPermissionGroupSetElement : newGroupSet.split(",")) {
            selenium.addSelection("//select[@name='rolePermGroups']", newPermissionGroupSetElement.toLowerCase());
        }

        selenium.click("name=submit");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Role " + roleName + " is modified")
                && verifyPermissionGroups(roleName, newGroupSet);
    }

    public boolean deleteRole(String roleName) {
        roleName = roleName.toLowerCase();
        if (!isRoleExist(roleName)) {
            return false;
        }
        selenium.open(UIConstants.ROLE_MGMT_URL + "?rid=" + roleName + "&action=deleteRole");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return (!isRoleExist(roleName));
    }

    public boolean isRoleExist(String roleName) {
        roleName = roleName.toLowerCase();
        selenium.open(UIConstants.ROLE_MGMT_URL);
        final int rowNum = getRowNumber(roleName);
        if (rowNum > 0) {
            return true;
        }
        return false;
    }

    private int getRowNumber(final String role_name) {
        int rowNo = 0;
        String value = "";
        String xpath = "";
        final int numRows = selenium.getXpathCount("//table[@class= 'sortable']/tbody/tr").intValue();
        for (int i = 1; i < numRows + 1; i++) { // Rows are 1 based
            xpath = "//table[@class= 'sortable']/tbody/tr[" + (i) + "]/td[1]";
            value = selenium.getText(xpath);
            if (value.equals(role_name) || value.equals(role_name + "*")) {
                rowNo = i;
                break;
            }
        }
        return rowNo;
    }

}
