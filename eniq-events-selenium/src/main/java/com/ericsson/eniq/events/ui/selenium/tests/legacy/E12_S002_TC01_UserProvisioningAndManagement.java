//package com.example.tests;
package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.core.Constants;
import com.ericsson.eniq.events.ui.selenium.events.login.AdminLogin;
import com.ericsson.eniq.events.ui.selenium.events.login.EventsLogin;
import com.ericsson.eniq.events.ui.selenium.events.login.UserDetails;
import com.ericsson.eniq.events.ui.selenium.events.login.UserManagement;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.AdminUIBaseSeleniumTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/*
 * Epic 12 : Story 002 : User provisioning and management
 *      Requirement Tag      Description                                                     Test Case Tag
 *      ---------------      -----------                                                     -------------
 *      105 65-0528/00196    It shall be possible to remove a system user using the UI.      ENIQ_E_4.4.9
 *      
 *      105 65-0528/00192    It shall be possible to provision a new system user using a     ENIQ_E_4.4.5 
 *                           graphical user interface.
 *                           
 *      105 65-0528/00197    It shall be possible to modify a system user account            ENIQ_E_4.4.10 
 *                           information and credentials using the UI.  
 *
 *
 *      // TODO: Update modify test cases to validate modification of user role
 *      Positive test cases:
 *      - Add/Modify/Delete user
 *      - Modifying user does not effect password
 *
 *      Negative test cases:
 *      - Add user
 *              - User already exists
 *              - Invalid email
 *              - Invalid phone number
 *              - Password/confirmation mismatch
 *              - Missing first name
 *              - Missing last name
 *              - Missing login name
 *              - Missing password
 *              - No role selected
 *              - Invalid password
 *                      - No capital letter
 *                      - No lower case letter
 *                      - No number
 *                      - No special char
 */
public class E12_S002_TC01_UserProvisioningAndManagement extends AdminUIBaseSeleniumTest {

    @Autowired
    private AdminLogin adminLogin;

    @Autowired
    private EventsLogin eventsLogin;

    @Autowired
    private UserManagement userManagement;

    private Map<UserDetails, String> user;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        user = new HashMap<UserDetails, String>();

        // Create a valid user
        user.put(UserDetails.LOGIN_NAME, "tlogin");
        user.put(UserDetails.FIRST_NAME, "testfirstname");
        user.put(UserDetails.LAST_NAME, "testlastname");
        user.put(UserDetails.PASSWORD, "Testpassword0");
        user.put(UserDetails.CONFIRM, "Testpassword0");
        user.put(UserDetails.EMAIL, "test_email@email.com");
        user.put(UserDetails.PHONE, "12365444");
        user.put(UserDetails.ORGANIZATION, "test_organization");

    }

    @Override
    @After
    public void tearDown() throws Exception {
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath()
                + "servlet/LoaderStatusServlet");
        selenium.waitForPageToLoad("30000");
        adminLogin.logIn();
        userManagement.deleteUser(user);
        adminLogin.logOut();
        super.tearDown();
    }

    @Test
    public void testAddModifyDeleteUIUser() {
        userManagement.addUser(user, UserManagement.UI_USER);
        selenium.waitForPageToLoad("30000");
        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after adding user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been added"));

        assertTrue("User not found in user table", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertTrue("User unable to log in", canUIUserLogIn(user));

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort());
        adminLogin.logIn();

        // Modify user
        final Map<UserDetails, String> newUser = new HashMap<UserDetails, String>();
        newUser.put(UserDetails.FIRST_NAME, user.get(UserDetails.FIRST_NAME) + "_mod");
        newUser.put(UserDetails.LAST_NAME, user.get(UserDetails.LAST_NAME) + "_mod");
        newUser.put(UserDetails.PASSWORD, user.get(UserDetails.PASSWORD) + "_mod");
        newUser.put(UserDetails.CONFIRM, user.get(UserDetails.CONFIRM) + "_mod");
        newUser.put(UserDetails.EMAIL, user.get(UserDetails.EMAIL) + "_mod");
        newUser.put(UserDetails.PHONE, user.get(UserDetails.PHONE) + "0");
        newUser.put(UserDetails.ORGANIZATION, user.get(UserDetails.ORGANIZATION) + "_mod");

        userManagement.modifyUser(user, newUser);

        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after modifying user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been modified"));

        assertTrue("User not found in user table", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertTrue("User unable to log in", canUIUserLogIn(user));

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort());
        adminLogin.logIn();
        userManagement.deleteUser(user);
        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after modifying user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been deleted"));

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(newUser));
        adminLogin.logOut();
        // verifyUserCantLogIn
        assertFalse("User able to log in after deletion", canUIUserLogIn(user));
    }

    @Test
    public void testModifyNamesDoesNotEffectPassword() {
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();
        user.put(UserDetails.PASSWORD, "Rr4$");
        user.put(UserDetails.CONFIRM, "Rr4$");
        // Add the user and verify they can log in
        userManagement.addUser(user, UserManagement.UI_USER);
        selenium.waitForPageToLoad("30000");
        // Verify that a message is shown confirming that the user was added
        assertTrue("Message not displayed after adding user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been added"));
        assertTrue("User not found in user table", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertTrue("User unable to log in", canUIUserLogIn(user));

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort());
        adminLogin.logIn();

        // Do internal modification of first name and last name, don't change password
        final Map<UserDetails, String> newUser = new HashMap<UserDetails, String>();
        newUser.put(UserDetails.FIRST_NAME, user.get(UserDetails.FIRST_NAME) + "_mod");
        newUser.put(UserDetails.LAST_NAME, user.get(UserDetails.LAST_NAME) + "_mod");

        userManagement.modifyUser(user, newUser);

        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after modifying user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been modified"));

        assertTrue("User not found in user table", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertTrue("User unable to log in", canUIUserLogIn(user));

        // Delete the user
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort());
        adminLogin.logIn();
        userManagement.deleteUser(user);

        // Verify that a message is shown confirming that the user was deleted
        assertTrue("Message not displayed after deleting user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been deleted"));

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(newUser));
        adminLogin.logOut();
        // verifyUserCantLogIn
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath());
        pause(100);
        eventsLogin.logIn(user.get(UserDetails.LOGIN_NAME), user.get(UserDetails.PASSWORD));
        assertEquals("Invalid User Details Entered", selenium.getText("//font[3]"));
    }

    @Test
    public void testAddUserNoRoleSelected() {
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        assertTrue("Add user link not present", selenium.isElementPresent("link=Add User"));

        selenium.click("link=Add User");
        selenium.waitForPageToLoad("30000");

        for (final UserDetails detail : user.keySet()) {
            selenium.type(detail.getFieldName(), user.get(detail));
        }
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");

        // Verify that a message is shown confirming that the user was not added
        assertTrue("Incorrect message displayed after adding bad user: " + selenium.getText("//td[2]"), selenium
                .getText("//td[2]").endsWith("User has not been added beacause of missing role"));

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
    }

    @Test
    public void testAddUserAlreadyExists() {
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        selenium.waitForPageToLoad("30000");
        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after adding user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been added"));

        userManagement.addUser(user, UserManagement.UI_USER);
        selenium.waitForPageToLoad("30000");
        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after adding user:", selenium.getText("//td[2]").endsWith(
                "User has not been added - Entry Already Exists"));

        userManagement.deleteUser(user);
        // Verify that a message is shown confirming that the user was modified
        assertTrue("Message not displayed after deleting user:", selenium.getText("//td[2]").endsWith(
                "User " + user.get(UserDetails.LOGIN_NAME) + " has been deleted"));

    }

    @Test
    public void testAddUserPasswordNoCapital() {
        user.put(UserDetails.PASSWORD, "test_password0");
        user.put(UserDetails.CONFIRM, "test_password0");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals(
                "Password should have a lower case letter, upper case letter, number and a special character from this list ()-_><@#$%^&=+-%",
                selenium.getAlert());

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserPasswordNoLowerCase() {
        user.put(UserDetails.PASSWORD, "TEST_PASSWORD0");
        user.put(UserDetails.CONFIRM, "TEST_PASSWORD0");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals(
                "Password should have a lower case letter, upper case letter, number and a special character from this list ()-_><@#$%^&=+-%",
                selenium.getAlert());

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserPasswordNoSpecialChar() {
        user.put(UserDetails.PASSWORD, "Testpassword0");
        user.put(UserDetails.CONFIRM, "Testpassword0");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals(
                "Password should have a lower case letter, upper case letter, number and a special character from this list ()-_><@#$%^&=+-%",
                selenium.getAlert());

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserPasswordNoNumber() {
        user.put(UserDetails.CONFIRM, "Test_password");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals(
                "Password should have a lower case letter, upper case letter, number and a special character from this list ()-_><@#$%^&=+-%",
                selenium.getAlert());

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMismatchPasswordConfirmation() {
        user.put(UserDetails.CONFIRM, "Test_mismatch0");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Password and Confirm Password are not same.", selenium.getAlert());

        assertFalse("User is still present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserInvalidEmail() {
        // invalid domain
        user.put(UserDetails.EMAIL, "test_email@email");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("It is not a valid email address", selenium.getAlert());

        assertFalse("User is still present operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));

        // invalid address
        user.put(UserDetails.EMAIL, "@email.com");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("It is not a valid email address", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));

        // invalid domain
        user.put(UserDetails.EMAIL, "test_email@.com");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("It is not a valid email address", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));

        // invalid domain
        user.put(UserDetails.EMAIL, "test_email.email.com");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("It is not a valid email address", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));

    }

    @Test
    public void testAddUserInvalidPhoneNumber() {
        user.put(UserDetails.PHONE, "NOTANUMBER");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Not a valid phone number", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMissingPassword() {
        user.put(UserDetails.PASSWORD, "");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Please fill in a Password.\nPassword and Confirm Password are not same.", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMissingConfirmation() {
        user.put(UserDetails.PASSWORD, "");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Please fill in Confirm Password.\nPassword and Confirm Password are not same.", selenium
                .getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMissingLoginName() {
        user.put(UserDetails.LOGIN_NAME, "");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Please fill in Login Name.", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMissingFirstName() {
        user.put(UserDetails.FIRST_NAME, "");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Please fill in First Name.", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    @Test
    public void testAddUserMissingLastName() {
        user.put(UserDetails.LAST_NAME, "");

        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath());
        adminLogin.logIn();

        userManagement.addUser(user, UserManagement.UI_USER);
        // Verify that a message is shown confirming that the password was invalid
        assertTrue("No alert box present", selenium.isAlertPresent());
        assertEquals("Please fill in Last Name.", selenium.getAlert());

        assertFalse("User is present after operation:", userManagement.isUserPresentInTable(user));
        adminLogin.logOut();
        assertFalse("User able to log in", canUIUserLogIn(user));
    }

    private boolean canUIUserLogIn(final Map<UserDetails, String> userMap) {
        boolean returnValue = false;
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath());
        selenium.waitForPageToLoad("30000");
        captureLogAndScreenShotOnFailure(new Exception());

        eventsLogin.logIn(userMap.get(UserDetails.LOGIN_NAME), userMap.get(UserDetails.PASSWORD));
        pause(100);
        if (selenium.isElementPresent(Constants.USER_NAME_LABEL)
                && eventsLogin.getUserName().equals(selenium.getText(Constants.USER_NAME_LABEL))) {
            returnValue = true;
        }
        eventsLogin.logOut();

        return returnValue;
    }

}
