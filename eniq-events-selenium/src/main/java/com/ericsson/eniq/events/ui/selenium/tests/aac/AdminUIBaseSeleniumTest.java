/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.common.EniqManagement;
import com.ericsson.eniq.events.ui.selenium.tests.aac.common.PermissionGroupManagement;
import com.ericsson.eniq.events.ui.selenium.tests.aac.common.RoleManagement;
import com.ericsson.eniq.events.ui.selenium.tests.aac.common.UserManagementAAC;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.*;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
* @author ejenkav
* @since 2011
*
*/
public class AdminUIBaseSeleniumTest extends BaseSeleniumTest {

    public static final PermissionGroup TEST_FIXTURE_PERMISSION_GROUP = new PermissionGroup("testgroup", "Test Group",
            "This permission group is for testing", PermissionGroup.Permission.NETWORK.getPermissionName());

    public static final Role TEST_FIXTURE_ROLE = new Role("testrole", "Test Role", "This role is for testing",
            TEST_FIXTURE_PERMISSION_GROUP.getName());

    public static final User TEST_FIXTURE_USER = new User("testuser", "ABCD@1234", "Test", "User",
            "testuser@ericsson.com", "", "", "customercare,marketing");

    @Autowired
    protected EricssonSelenium selenium;

    @Autowired
    public PermissionGroupManagement permissionGroupMgmt;

    @Autowired
    public RoleManagement roleMgmt;

    @Autowired
    public UserManagementAAC userMgmt;

    @Autowired
    public EniqManagement eniqMgmt;

    public final ArrayList<String> cleanupUserIDList = new ArrayList<String>();

    public final ArrayList<String> cleanupRoleNameList = new ArrayList<String>();

    public final ArrayList<String> cleanupPermissionNameList = new ArrayList<String>();

    public static final String USERID = ".user.id";

    public static final String PASSWD = ".user.password";

    public static final String CPASSWD = ".user.confirmPassword";

    public static final String FNAME = ".user.firstName";

    public static final String LNAME = ".user.lastName";

    public static final String EMAIL = ".user.email";

    public static final String PHONE = ".user.phone";

    public static final String ORG = ".user.org";

    public static final String ROLES = ".user.roles";

    public static final String RNAME = ".role.name";

    public static final String RTITLE = ".role.title";

    public static final String RDESC = ".role.description";

    public static final String RPGS = ".role.permissionGroups";

    public static final String PGNAME = ".permissionGroup.name";

    public static final String PGTITLE = ".permissionGroup.title";

    public static final String PGDESC = ".permissionGroup.description";

    public static final String PGPS = ".permissionGroup.permissions";

    @Override
    @Before
    public void setUp() {
        selenium.start();
        EniqManagement.seleniumEniq.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(UIConstants.LOGIN_PAGE_URL);
        EniqManagement.seleniumEniq.open(UIConstants.ENIQ_LOGIN_PAGE_URL);

    }

    @After
    @Override
    public void tearDown() {

        selenium.close();
        EniqManagement.seleniumEniq.close();
        //TODO: Add timestamp
        if (selenium != null) {
            selenium.stop();
            selenium = null;
        }
    }

    public void cleanUp() {
        userMgmt.logInAsAdmin();
        for (final String userID : cleanupUserIDList) {
            userMgmt.deleteUser(userID.toLowerCase());
        }
        cleanupUserIDList.clear();
        for (final String roleName : cleanupRoleNameList) {
            roleMgmt.deleteRole(roleName.toLowerCase());
        }
        cleanupRoleNameList.clear();
        for (final String permissionName : cleanupPermissionNameList) {
            permissionGroupMgmt.deletePermissionGroup(permissionName.toLowerCase());
        }
        cleanupPermissionNameList.clear();

        userMgmt.logOut();
    }

    public boolean isTabEnabledForRoles(final String[] roles) {
        userMgmt.logInAsAdmin();
        if (!eniqMgmt.isLoggedIn()) {
            return false;
        }
        for (final String roleElement : roles) {
            for (final String permissionGroupElement : roleMgmt.getPermissionGroups(roleElement.toLowerCase())) {
                for (final String permissionElement : permissionGroupMgmt.getPermissions(permissionGroupElement
                        .toLowerCase())) {
                    if (!eniqMgmt.isTabEnaled(permissionElement)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isTabEnabledForPermissionGroups(final String[] permissionGroups) {
        userMgmt.logInAsAdmin();
        if (!eniqMgmt.isLoggedIn()) {
            return false;
        }

        for (final String permissionGroupElement : permissionGroups) {
            for (final String permissionElement : permissionGroupMgmt.getPermissions(permissionGroupElement
                    .toLowerCase())) {
                if (!eniqMgmt.isTabEnaled(permissionElement)) {
                    return false;
                }
            }
        }

        return true;
    }

    public String getData(final String key) {
        return DataReader.getInstance().getProperty(key);
    }

    public User getUser(final String key) {
        final String username = DataReader.getInstance().getProperty(key + USERID).toLowerCase();
        if (username.equals(Predefined.SYSADMIN_USER.getUserID())) {
            assertTrue("User ID same as Predefined Users ID", false);
            return null;

        }
        cleanupUserIDList.add(username);
        final User user = new User();
        user.setUserID(username);
        user.setPassword(DataReader.getInstance().getProperty(key + PASSWD));
        user.setConfirmPassword(DataReader.getInstance().getProperty(key + CPASSWD));
        user.setFirstName(DataReader.getInstance().getProperty(key + FNAME));
        user.setLastName(DataReader.getInstance().getProperty(key + LNAME));
        user.setEmail(DataReader.getInstance().getProperty(key + EMAIL));
        user.setPhone(DataReader.getInstance().getProperty(key + PHONE));
        user.setOrganization(DataReader.getInstance().getProperty(key + ORG));
        user.setRoles(DataReader.getInstance().getProperty(key + ROLES));
        return user;
    }

    public User getPredefinedUser(final String key) {
        final String username = DataReader.getInstance().getProperty(key + USERID).toLowerCase();
        if (!username.equals(Predefined.SYSADMIN_USER.getUserID())) {
            assertTrue("Invalid Predefined User", false);
        }
        final User user = new User();
        user.setUserID(Predefined.SYSADMIN_USER.getUserID());
        user.setPassword(Predefined.SYSADMIN_USER.getPassword());
        user.setConfirmPassword(Predefined.SYSADMIN_USER.getConfirmPassword());
        user.setFirstName(Predefined.SYSADMIN_USER.getFirstName());
        user.setLastName(Predefined.SYSADMIN_USER.getLastName());
        user.setEmail(Predefined.SYSADMIN_USER.getEmail());
        user.setPhone(Predefined.SYSADMIN_USER.getPhone());
        user.setOrganization(Predefined.SYSADMIN_USER.getOrganization());
        user.setRoles(Predefined.SYSADMIN_USER.getRolesAsString());

        return user;
    }

    public Role getRole(final String key) {
        final String rolename = DataReader.getInstance().getProperty(key + RNAME).toLowerCase();
        if (rolename.equals(Predefined.MARKETING_ROLE.getName())
                || rolename.equals(Predefined.NETWORK_MONITORING_ROLE.getName())
                || rolename.equals(Predefined.NETWORK_TROUBLESHOOTING_ROLE.getName())
                || rolename.equals(Predefined.POWER_USER_ROLE.getName())
                || rolename.equals(Predefined.SYSADMIN_ROLE.getName())
                || rolename.equals(Predefined.TERMINAL_SPECIALIST_ROLE.getName())) {
            assertTrue("Invalid Role: Name same as Predefined Role name", false);
            return null;

        }

        cleanupRoleNameList.add(rolename);
        final Role role = new Role();
        role.setName(rolename);
        role.setTitle(DataReader.getInstance().getProperty(key + RTITLE));
        role.setDescription(DataReader.getInstance().getProperty(key + RDESC));
        role.setPermissionGroups(DataReader.getInstance().getProperty(key + RPGS));
        return role;
    }

    public Role getPredefinedRole(final String key) {
        final String rolename = DataReader.getInstance().getProperty(key + RNAME).toLowerCase();
        Role predefinedRole = null;
        if (rolename.equals(Predefined.MARKETING_ROLE.getName())) {
            predefinedRole = Predefined.MARKETING_ROLE;
        } else if (rolename.equals(Predefined.NETWORK_MONITORING_ROLE.getName())) {
            predefinedRole = Predefined.NETWORK_MONITORING_ROLE;
        } else if (rolename.equals(Predefined.NETWORK_TROUBLESHOOTING_ROLE.getName())) {
            predefinedRole = Predefined.NETWORK_TROUBLESHOOTING_ROLE;
        } else if (rolename.equals(Predefined.POWER_USER_ROLE.getName())) {
            predefinedRole = Predefined.POWER_USER_ROLE;
        } else if (rolename.equals(Predefined.SYSADMIN_ROLE.getName())) {
            predefinedRole = Predefined.SYSADMIN_ROLE;
        } else if (rolename.equals(Predefined.TERMINAL_SPECIALIST_ROLE.getName())) {
            predefinedRole = Predefined.TERMINAL_SPECIALIST_ROLE;
        } else {
            assertTrue("Invalid Predefined Role", false);
            return null;
        }

        final Role role = new Role();
        role.setName(predefinedRole.getName());
        role.setTitle(predefinedRole.getTitle());
        role.setDescription(predefinedRole.getDescription());
        role.setPermissionGroups(predefinedRole.getpermissionGroupsAsString());
        return role;
    }

    public PermissionGroup getPermissionGroup(final String key) {

        final String perissionGroupName = DataReader.getInstance().getProperty(key + PGNAME).toLowerCase();

        if (perissionGroupName.equals(Predefined.TERMINAL_PERMISSION_GROUP.getName())
                || perissionGroupName.equals(Predefined.NETWORK_PERMISSION_GROUP.getName())
                || perissionGroupName.equals(Predefined.RANKING_PERMISSION_GROUP.getName())
                || perissionGroupName.equals(Predefined.SUSCRIBER_PERMISSION_GROUP.getName())
                || perissionGroupName.equals(Predefined.ALLPERMISSIONS_GROUP.getName())) {
            assertTrue("Invalid Permission group: Name same as Predefined Permission Group name", false);
            return null;

        }

        cleanupPermissionNameList.add(perissionGroupName);
        final PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setName(perissionGroupName);
        permissionGroup.setTitle(DataReader.getInstance().getProperty(key + PGTITLE));
        permissionGroup.setDescription(DataReader.getInstance().getProperty(key + PGDESC));
        permissionGroup.setPermissions(DataReader.getInstance().getProperty(key + PGPS));
        return permissionGroup;
    }

    public PermissionGroup getPredefinedPermissionGroup(final String key) {
        final String perissionGroupName = DataReader.getInstance().getProperty(key + PGNAME).toLowerCase();
        PermissionGroup predefinedPermissionGroup = null;
        if (perissionGroupName.equals(Predefined.NETWORK_PERMISSION_GROUP.getName())) {
            predefinedPermissionGroup = Predefined.NETWORK_PERMISSION_GROUP;
        } else if (perissionGroupName.equals(Predefined.RANKING_PERMISSION_GROUP.getName())) {
            predefinedPermissionGroup = Predefined.RANKING_PERMISSION_GROUP;
        } else if (perissionGroupName.equals(Predefined.SUSCRIBER_PERMISSION_GROUP.getName())) {
            predefinedPermissionGroup = Predefined.SUSCRIBER_PERMISSION_GROUP;
        } else if (perissionGroupName.equals(Predefined.TERMINAL_PERMISSION_GROUP.getName())) {
            predefinedPermissionGroup = Predefined.TERMINAL_PERMISSION_GROUP;
        } else if (perissionGroupName.equals(Predefined.ALLPERMISSIONS_GROUP.getName())) {
            predefinedPermissionGroup = Predefined.ALLPERMISSIONS_GROUP;
        } else {
            assertTrue("Invalid Predefined Permission group", false);
            return null;
        }

        final PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setName(predefinedPermissionGroup.getName());
        permissionGroup.setTitle(predefinedPermissionGroup.getTitle());
        permissionGroup.setDescription(predefinedPermissionGroup.getDescription());
        permissionGroup.setPermissions(predefinedPermissionGroup.getpermissionsAsString());
        return permissionGroup;
    }

}
