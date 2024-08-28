/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.aac;

import com.ericsson.eniq.events.ui.selenium.tests.aac.data.DataReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Level;

/**
* @author ejenkav
* @since 2011
*
*/
public class PermissionTestGroup extends AdminUIBaseSeleniumTest {

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Permission test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of User Permission test section");
    }

    @Test
    public void testModifyUserRoleAndPermission() {
        userMgmt.logInAsAdmin();
        /*       final String permissionGroupName = DataReader.getInstance().getProperty("PermissionGroup.name");
               final String newTitle = "New Title";
               if (permissionGroupManagement.isPermissionGroupExist(permissionGroupName)) {
                   permissionGroupManagement.modifyTitle(permissionGroupName, newTitle);
               }
               final String newDescription = "New Description";
               if (permissionGroupManagement.isPermissionGroupExist(permissionGroupName)) {
                   permissionGroupManagement.modifyDescription(permissionGroupName, newDescription);
               }
               permissionGroupManagement.modifyPermissionSet(
                       permissionGroupName,
                       PermissionGroup.Permission.SUBSCRIBER.getPermission() + ","
                               + PermissionGroup.Permission.TERMINAL.getPermission());*/

        /*        final String roleName = DataReader.getInstance().getProperty("Role.name");
                final String newTitleRole = "New Role Title";
                if (roleManagement.isRoleExist(roleName)) {
                    roleManagement.modifyTitle(roleName, newTitleRole);
                }
                final String newDescriptionRole = "New Role Description";
                if (roleManagement.isRoleExist(roleName)) {
                    roleManagement.modifyDescription(roleName, newDescriptionRole);
                }
                if (roleManagement.isRoleExist(roleName)) {
                    roleManagement.modifyPermissionGroupSet(roleName, Predefined.RANKING_PERMISSION_GROUP.getName() + ","
                            + Predefined.TERMINAL_PERMISSION_GROUP.getName());
                }
        */

        final String userID = DataReader.getInstance().getProperty("User.userID");
        final String newFirstName = "Jenis New";
        if (userMgmt.isUserExist(userID)) {
            userMgmt.modifyFirstName(userID, newFirstName);
            userMgmt.modifyLastName(userID, "Kavadiya New");
            userMgmt.modifyEmail(userID, "Jenis.kavadiya@ericsson.com");
            userMgmt.modifyPhone(userID, "123456");
            userMgmt.modifyOrganization(userID, "PPRST");
            userMgmt.modifyPassword(userID, "aBCD@1234", "aBCD@1234");
        }
    }

    @Test
    public void testDeleteUserRoleAndPermission() {
        userMgmt.logInAsAdmin();
        final String userIDToDelete = DataReader.getInstance().getProperty("User.userID");
        if (userMgmt.isUserExist(userIDToDelete)) {
            userMgmt.deleteUser(userIDToDelete);
            assertFalse("User " + userIDToDelete + " cannot be deleted", userMgmt.isUserExist(userIDToDelete));
        }

        final String roleToDelete = DataReader.getInstance().getProperty("Role.name");
        if (roleMgmt.isRoleExist(roleToDelete)) {
            roleMgmt.deleteRole(roleToDelete);
            assertFalse("Role " + roleToDelete + " cannot be deleted", roleMgmt.isRoleExist(roleToDelete));
        }
        final String permissionToDelete = DataReader.getInstance().getProperty("PermissionGroup.name");
        if (permissionGroupMgmt.isPermissionGroupExist(permissionToDelete)) {
            permissionGroupMgmt.deletePermissionGroup(permissionToDelete);
            assertFalse("Permission " + permissionToDelete + " cannot be deleted",
                    permissionGroupMgmt.isPermissionGroupExist(permissionToDelete));
        }

    }

    @Test
    public void testCreatePermissionGroupForEachPredefinedPermission() {
        userMgmt.logInAsAdmin();
        TEST_FIXTURE_PERMISSION_GROUP.setName(DataReader.getInstance().getProperty("PermissionGroup.name"));
        TEST_FIXTURE_PERMISSION_GROUP.setTitle(DataReader.getInstance().getProperty("PermissionGroup.title"));
        TEST_FIXTURE_PERMISSION_GROUP.setDescription(DataReader.getInstance()
                .getProperty("PermissionGroup.description"));
        TEST_FIXTURE_PERMISSION_GROUP.setPermissions(DataReader.getInstance()
                .getProperty("PermissionGroup.permissions"));
        assertFalse("Permission Group " + TEST_FIXTURE_PERMISSION_GROUP.getName() + " already exists",
                permissionGroupMgmt.isPermissionGroupExist(TEST_FIXTURE_PERMISSION_GROUP.getName()));
        permissionGroupMgmt.createPermissionGroup(TEST_FIXTURE_PERMISSION_GROUP);

        TEST_FIXTURE_ROLE.setName(DataReader.getInstance().getProperty("Role.name"));
        TEST_FIXTURE_ROLE.setTitle(DataReader.getInstance().getProperty("Role.title"));
        TEST_FIXTURE_ROLE.setDescription(DataReader.getInstance().getProperty("Role.description"));
        TEST_FIXTURE_ROLE.setPermissionGroups(DataReader.getInstance().getProperty("Role.permissionGroups"));
        assertFalse("Role " + TEST_FIXTURE_ROLE.getName() + " already exists",
                roleMgmt.isRoleExist(TEST_FIXTURE_ROLE.getName()));
        roleMgmt.createRole(TEST_FIXTURE_ROLE);

        TEST_FIXTURE_USER.setUserID(DataReader.getInstance().getProperty("User.userID"));
        TEST_FIXTURE_USER.setPassword(DataReader.getInstance().getProperty("User.password"));
        TEST_FIXTURE_USER.setConfirmPassword(DataReader.getInstance().getProperty("User.confirmPassword"));
        TEST_FIXTURE_USER.setFirstName(DataReader.getInstance().getProperty("User.firstName"));
        TEST_FIXTURE_USER.setLastName(DataReader.getInstance().getProperty("User.lastName"));
        TEST_FIXTURE_USER.setEmail(DataReader.getInstance().getProperty("User.email"));
        TEST_FIXTURE_USER.setPhone(DataReader.getInstance().getProperty("User.phone"));
        TEST_FIXTURE_USER.setOrganization(DataReader.getInstance().getProperty("User.organization"));
        TEST_FIXTURE_USER.setRoles(DataReader.getInstance().getProperty("User.roles"));
        assertFalse("User " + TEST_FIXTURE_USER.getUserID() + " already exists",
                userMgmt.isUserExist(TEST_FIXTURE_USER.getUserID()));

        userMgmt.createUser(TEST_FIXTURE_USER);
    }

    @Test
    public void test1() {
        assertTrue(permissionGroupMgmt.verifyTitle("mygroup", "My Group"));

    }
}
