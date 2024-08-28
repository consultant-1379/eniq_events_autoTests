/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac.common;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.AdminUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.PermissionGroup;
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
public class PermissionGroupManagement {
    private final EricssonSelenium selenium;

    @Autowired
    public PermissionGroupManagement(final EricssonSelenium sel) {
        selenium = sel;
    }

    public boolean createPermissionGroup(final PermissionGroup permissionGroup) {
        selenium.open(UIConstants.PERMISSION_GROUPS_URL);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.click("css=button.addButton");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type("id=permGroupName", permissionGroup.getName().toLowerCase());
        selenium.type("id=permGroupTitle", permissionGroup.getTitle());
        selenium.type("id=permGroupDescription", permissionGroup.getDescription());
        for (final String permissionElement : permissionGroup.getPermissionSet()) {
            selenium.addSelection("id=permissions", "label=" + permissionElement.toLowerCase());
        }
        selenium.click("name=submit2");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Permission Group " + permissionGroup.getName().toLowerCase() + " is created");
    }

    public boolean createPermissionGroup(final String permissionGroupName, final String title,
            final String description, final String perimssions) {
        AdminUIBaseSeleniumTest.TEST_FIXTURE_PERMISSION_GROUP.setName(permissionGroupName);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_PERMISSION_GROUP.setTitle(title);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_PERMISSION_GROUP.setDescription(description);
        AdminUIBaseSeleniumTest.TEST_FIXTURE_PERMISSION_GROUP.setPermissions(perimssions);
        return createPermissionGroup(AdminUIBaseSeleniumTest.TEST_FIXTURE_PERMISSION_GROUP);
    }

    public boolean verifyPermissionGroup(final PermissionGroup permissionGroup) {
        return verifyTitle(permissionGroup.getName(), permissionGroup.getTitle())
                && verifyPermissions(permissionGroup.getName(), permissionGroup.getpermissionsAsString());
    }

    public boolean verifyTitle(final String permissionGroupName, final String title) {
        return verifyAttribute(permissionGroupName, "//table[@cellspacing= '12']/tbody/tr[2]/td[2]", title);
    }

    public boolean verifyPermissions(String permissionGroupName, final String permissions) {

        final String[] allPermissions = (permissions.toLowerCase().split(","));
        final String elementLocator = "//table[@cellspacing= '12']/tbody/tr[3]/td[2]";
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return false;
        }

        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=viewPermissions&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        final String existingPermissions = selenium.getText(elementLocator).toLowerCase();
        for (final String allPermissionsElement : allPermissions) {
            if (!existingPermissions.contains(allPermissionsElement)) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyAttribute(String permissionGroupName, final String elementLocator, final String newValue) {
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return false;
        }

        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=viewPermissions&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return newValue.equals(selenium.getText(elementLocator));
    }

    public String[] getPermissions(String permissionGroupName) {
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return null;
        }
        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=viewPermissions&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.getText("//table[@cellspacing= '12']/tbody/tr[3]/td[2]").split("\n ");
    }

    private boolean modifyPermissionGroupAttribute(String permissionGroupName, final String elementLocator,
            final String newValue) {
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return false;
        }
        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=editPermissionGroup&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        selenium.type(elementLocator, newValue);
        selenium.click("name=submit2");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Permission Group " + permissionGroupName + " is modified");

    }

    public boolean modifyTitle(String permissionGroupName, final String newTitle) {
        permissionGroupName = permissionGroupName.toLowerCase();
        return modifyPermissionGroupAttribute(permissionGroupName, "id=permGroupTitle", newTitle)
                && verifyTitle(permissionGroupName, newTitle);

    }

    public boolean modifyDescription(String permissionGroupName, final String newDescription) {
        permissionGroupName = permissionGroupName.toLowerCase();
        // Jenis Change Need to add verification of description when available
        return modifyPermissionGroupAttribute(permissionGroupName, "id=permGroupDescription", newDescription);

    }

    public boolean modifyPermissionSet(String permissionGroupName, final String newPermissionSet) {
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return false;
        }
        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=editPermissionGroup&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);

        final Set<String> optionVALUES = new HashSet<String>(Arrays.asList(selenium.getSelectOptions("id=permissions")));
        for (final String newPermissionSetElement : newPermissionSet.split(",")) {
            if (!optionVALUES.contains(newPermissionSetElement.toLowerCase())) {
                return false;
            }
        }

        final String[] existingPermissionSet = selenium.getSelectedValues("id=permissions");
        for (final String existingPermissionSetElement : existingPermissionSet) {
            selenium.removeSelection("id=permissions", existingPermissionSetElement);
        }
        for (final String newPermissionSetElement : newPermissionSet.split(",")) {
            selenium.addSelection("id=permissions", newPermissionSetElement.toLowerCase());
        }

        selenium.click("name=submit2");
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return selenium.isTextPresent("Permission Group " + permissionGroupName + " is modified");
    }

    public boolean deletePermissionGroup(String permissionGroupName) {
        permissionGroupName = permissionGroupName.toLowerCase();
        if (!isPermissionGroupExist(permissionGroupName)) {
            return false;
        }
        selenium.open(UIConstants.PERMISSION_GROUPS_URL + "?action=deletePermissionGroup&gid=" + permissionGroupName);
        selenium.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        return (!isPermissionGroupExist(permissionGroupName));
    }

    public boolean isPermissionGroupExist(String permissionGroupName) {
        permissionGroupName = permissionGroupName.toLowerCase();
        selenium.open(UIConstants.PERMISSION_GROUPS_URL);
        final int rowNum = getRowNumber(permissionGroupName);
        if (rowNum > 0) {
            return true;
        }
        return false;
    }

    private int getRowNumber(String permissionGroupName) {
        permissionGroupName = permissionGroupName.toLowerCase();
        int rowNo = 0;
        String value = "";
        String xpath = "";
        final int numRows = selenium.getXpathCount("//table[@class= 'sortable']/tbody/tr").intValue();
        for (int i = 1; i < numRows + 1; i++) { // Rows are 1 based
            xpath = "//table[@class= 'sortable']/tbody/tr[" + (i) + "]/td[1]";
            value = selenium.getText(xpath);
            if (value.equals(permissionGroupName) || value.equals(permissionGroupName + "*")) {
                rowNo = i;
                break;
            }
        }
        return rowNo;
    }

}
