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
public class BaseTasksTestGroup extends AdminUIBaseSeleniumTest {

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Base Tasks");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "END of Base Tasks");
    }

    @Test
    public void testBaseTasks() {
        final String createFlag = DataReader.getInstance().getProperty("CREATE");
        final String modifyFlag = DataReader.getInstance().getProperty("MODIFY");
        final String deleteFlag = DataReader.getInstance().getProperty("DELETE");
        if ("Y".equals(createFlag)) {
            createTasks();
        }
        if ("Y".equals(modifyFlag)) {
            modifyTasks();
        }
        if ("Y".equals(deleteFlag)) {
            deleteTasks();
        }
        /*        userManagement.logInAsAdmin();
        *//*       final String permissionGroupName = DataReader.getInstance().getProperty("PermissionGroup.name");
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
        /*
                final String userID = DataReader.getInstance().getProperty("User.userID");
                final String newFirstName = "Jenis New";
                if (userManagement.isUserExist(userID)) {
                    userManagement.modifyFirstName(userID, newFirstName);
                    userManagement.modifyLastName(userID, "Kavadiya New");
                    userManagement.modifyEmail(userID, "Jenis.kavadiya@ericsson.com");
                    userManagement.modifyPhone(userID, "123456");
                    userManagement.modifyOrganization(userID, "PPRST");
                    userManagement.modifyPassword(userID, "aBCD@1234", "aBCD@1234");
                }*/
    }

    private void deleteTasks() {
        final String permissionGroups = DataReader.getInstance().getProperty("DELETE.PERMISSIONGROUP");
        final String roles = DataReader.getInstance().getProperty("DELETE.ROLE");
        final String users = DataReader.getInstance().getProperty("DELETE.USER");
        final String seperator = ",";
        for (final String userElement : users.split(seperator)) {
            assertTrue("Cannot Delete User " + userElement, deleteUser(userElement));
        }
        for (final String roleElement : roles.split(seperator)) {
            assertTrue("Cannot Delete Role " + roleElement, deleteRole(roleElement));
        }
        for (final String permissionGroupElement : permissionGroups.split(seperator)) {
            assertTrue("Cannot Delete Permission group " + permissionGroupElement,
                    deletePermissiongroup(permissionGroupElement));
        }

    }

    private boolean deleteUser(final String userID) {
        userMgmt.logInAsAdmin();
        return userMgmt.deleteUser(userID);
    }

    private boolean deleteRole(final String roleName) {
        userMgmt.logInAsAdmin();
        return roleMgmt.deleteRole(roleName);
    }

    private boolean deletePermissiongroup(final String permissionGroupName) {
        userMgmt.logInAsAdmin();
        return permissionGroupMgmt.deletePermissionGroup(permissionGroupName);
    }

    private void modifyTasks() {
        // TODO Auto-generated method stub

    }

    private void createTasks() {
        final String permissionGroups = DataReader.getInstance().getProperty("CREATE.PERMISSIONGROUP");
        final String roles = DataReader.getInstance().getProperty("CREATE.ROLE");
        final String users = DataReader.getInstance().getProperty("CREATE.USER");
        final String seperator = ",";
        for (final String permissionGroupElement : permissionGroups.split(seperator)) {
            assertTrue("Cannot create Permission group " + permissionGroupElement,
                    createPermissiongroup(permissionGroupElement));
        }
        for (final String roleElement : roles.split(seperator)) {
            assertTrue("Cannot create Role " + roleElement, createRole(roleElement));
        }
        for (final String userElement : users.split(seperator)) {
            assertTrue("Cannot create User " + userElement, createUser(userElement));
        }

    }

    private boolean createUser(final String userID) {
        userMgmt.logInAsAdmin();
        TEST_FIXTURE_USER.setUserID(userID);
        TEST_FIXTURE_USER.setPassword(DataReader.getInstance().getProperty(userID + ".password"));
        TEST_FIXTURE_USER.setConfirmPassword(DataReader.getInstance().getProperty(userID + ".confirmPassword"));
        TEST_FIXTURE_USER.setFirstName(DataReader.getInstance().getProperty(userID + ".firstName"));
        TEST_FIXTURE_USER.setLastName(DataReader.getInstance().getProperty(userID + ".lastName"));
        TEST_FIXTURE_USER.setEmail(DataReader.getInstance().getProperty(userID + ".email"));
        TEST_FIXTURE_USER.setPhone(DataReader.getInstance().getProperty(userID + ".phone"));
        TEST_FIXTURE_USER.setOrganization(DataReader.getInstance().getProperty(userID + ".organization"));
        TEST_FIXTURE_USER.setRoles(DataReader.getInstance().getProperty(userID + ".roles"));
        return userMgmt.createUser(TEST_FIXTURE_USER);
    }

    private boolean createRole(final String roleName) {
        userMgmt.logInAsAdmin();
        TEST_FIXTURE_ROLE.setName(roleName);
        TEST_FIXTURE_ROLE.setTitle(DataReader.getInstance().getProperty(roleName + ".title"));
        TEST_FIXTURE_ROLE.setDescription(DataReader.getInstance().getProperty(roleName + ".description"));
        TEST_FIXTURE_ROLE.setPermissionGroups(DataReader.getInstance().getProperty(roleName + ".permissionGroups"));
        return roleMgmt.createRole(TEST_FIXTURE_ROLE);
    }

    private boolean createPermissiongroup(final String permissionGroupName) {
        userMgmt.logInAsAdmin();
        TEST_FIXTURE_PERMISSION_GROUP.setName(permissionGroupName);
        TEST_FIXTURE_PERMISSION_GROUP.setTitle(DataReader.getInstance().getProperty(permissionGroupName + ".title"));
        TEST_FIXTURE_PERMISSION_GROUP.setDescription(DataReader.getInstance().getProperty(
                permissionGroupName + ".description"));
        TEST_FIXTURE_PERMISSION_GROUP.setPermissions(DataReader.getInstance().getProperty(
                permissionGroupName + ".permissions"));
        return permissionGroupMgmt.createPermissionGroup(TEST_FIXTURE_PERMISSION_GROUP);
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

    }
}
