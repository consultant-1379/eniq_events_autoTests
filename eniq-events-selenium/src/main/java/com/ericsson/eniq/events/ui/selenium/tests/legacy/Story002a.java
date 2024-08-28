package com.ericsson.eniq.events.ui.selenium.tests.legacy;

//import static com.ericsson.eniq.events.ui.selenium.Constants.*;

import com.ericsson.eniq.events.ui.selenium.events.login.AdminLogin;
import com.ericsson.eniq.events.ui.selenium.events.login.UserDetails;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/*
 * Epic 12 : Story 002 : User provisioning and management
 * 
 *	Add new system user 
 *	Modify system user (all possible modifiable fields) 
 *	Remove system user
 *	Unlock previously locked out account (due to repeated login failure in ENIQ Events user interface)
 *	Remove system user 
 *	Change user password 
 *
 *	TEST CASES
 *      ENIQ _E_4.4.18; Password quality
 *      Verify that a password for a user must contain a lower case letter, upper case letter, a numerical digit and a special character from this list ()-_><@#$%^&=+-%
 *
 *	Check password strength
 *	Add new user, try password
 *	- too short
 *	- no lower case
 *	- no upper case
 *	- no numbers
 *	- no special characters
 *	- not matching
 *	- 
 */
public class Story002a extends BaseSeleniumTest {

    @Autowired
    private AdminLogin adminLogin;

    @Test
    public void testUntitled() throws Exception {
        //log in
        //login();
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort());

        adminLogin.logIn();
        //open user mgmt
        //openUserMgmt();
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");

        final Map<UserDetails, String> user = new HashMap<UserDetails, String>();
        user.put(UserDetails.PASSWORD, "password");
        user.put(UserDetails.CONFIRM, "password");

        //add user
        selenium.click("link=Add User");
        selenium.waitForPageToLoad("30000");
        selenium.addSelection("Account_Type", "label=UI Users");
        selenium.type("First Name9", "password");
        selenium.type("First Name10", "check");
        selenium.type("First Name11", "pword");
        selenium.type("First Name12", "Aa1");
        selenium.type("First Name13", "Aa1");
        selenium.type("First Name14", "Aa1@email.com");

        //do loop with passwords
        //String[] passwords = {"Aa1"};//,"A&7","a&7","Aa&","Aa7"};
        //int i=0;
        /*for (i=0; i<passwords.length; i++){
        	selenium.type("First Name12", passwords[i]);
        	selenium.type("First Name13", passwords[i]);
        	//selenium.type("First Name14", passwords[i]+"@email.com");
        	
        }*/
        //selenium.click("document.forms[0].elements[10]");
        //selenium.click("//input[@name='Submit' and @value='#{Submit}']");
        //selenium.click("//css=form.onsubmit input[type=submit]");
        //selenium.click("css=form.onsubmit input[type=submit]");
        //selenium.click("xpath=/descendant::input[@name='Submit'][1]");

        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
        //selenium.click("document.forms[0].elements[10]");
        assertEquals(
                "Password should have a lower case letter, upper case letter and a special character from this list ()-_><@#$%^&=+-%",
                selenium.getAlert());
        selenium.open("/servlet/UserManagement?addUser=");

        assertEquals("firstname lastname", selenium.getText("//tr[8]/td[1]/font"));
        assertEquals("login", selenium.getText("//tr[8]/td[2]/font"));
        //modify first name and last name
        assertEquals("Test&7@email.com", selenium.getText("link=Test&7@email.com"));
        selenium.click("//a[contains(@href, '/servlet/UserManagement?uid=login&modify=')]");
        assertEquals("Are you sure", selenium.getConfirmation());
        selenium.waitForPageToLoad("30000");
        selenium.type("First Name9", "first");
        selenium.type("First Name10", "last");
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
        assertEquals("glob:*User login has been modified*", selenium.getText("//td[2]"));
        assertEquals("first last", selenium.getText("//form/table/tbody/tr[8]/td[1]"));
        //modify email address
        selenium.click("//a[contains(@href, '/servlet/UserManagement?uid=login&modify=')]");
        assertEquals("Are you sure", selenium.getConfirmation());
        selenium.waitForPageToLoad("30000");
        selenium.type("First Name14", "Test&7mmm@email.com");
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
        assertEquals("Test&7mmm@email.com", selenium.getText("link=Test&7mmm@email.com"));
        assertEquals("glob:*User login has been modified*", selenium.getText("//td[2]"));
    }

    public void openUserMgmt() {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
    }
}
