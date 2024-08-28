/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.login;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.PermissionGroup;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.Role;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EventsLogin extends AUserLogin {

	protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

	@Override
	public String getUserName() {
		return PropertyReader.getInstance().getUser();
	}

	@Override
	protected String getUserPwd() {
		return PropertyReader.getInstance().getPwd();
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

	public boolean createUser(final UserInfo user) {
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
}