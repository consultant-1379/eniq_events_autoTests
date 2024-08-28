package com.ericsson.eniq.events.ui.selenium.events.login;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.core.Constants;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.common.UserManagementAAC;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.PermissionGroup;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.Role;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;

/**
 * @author egercor
 * @since 2010
 * 
 */
public class CreateDashboardUser extends BaseSeleniumTest {

	@Autowired
	protected DashboardLogin dashboardLogin;
	@Autowired
	public static EricssonSelenium seleniumEniq = new EricssonSelenium();
	String adminUser = PropertyReader.getInstance().getAdminUser();
	String adminPass = PropertyReader.getInstance().getAdminPwd();
	String dashboardUser = PropertyReader.getInstance().getDashboardUser();
	String dashboardTempPass = "Dashboard1";
	String dashboardPass = PropertyReader.getInstance().getDashboardPwd();
	static boolean userAlreadyExists = false;
	public AdminLogin adminLogin = new AdminLogin();
	PermissionGroup permissionGroup = new PermissionGroup("generalnetworkSelenium", "This permission group is for selenium testing using dashboard", "This permission group is for selenium testing using dashboard", "ericssonoss.sonvis.view,eventsui.dashboard.view,eventsui.network.view,eventsui.ranking.view,eventsui.subscriber.view,eventsui.terminal.view");

	Role role = new Role("dashboardSeleniumGroup", "dashboardSelGroup", "", "generalnetworkSelenium");
	UserInfo user = new UserInfo(dashboardUser, dashboardTempPass, dashboardTempPass, dashboardUser, dashboardUser, "", "", "", "dashboardseleniumgroup");
	UserManagementAAC um = new UserManagementAAC(seleniumEniq);
	//EventsLogin el = new EventsLogin();
    private String eniqVersion;
	
	@Test
	public void createPermissionGroupAndUser() throws Exception {

		seleniumEniq.start();
		seleniumEniq.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath() + "servlet/LoaderStatusServlet");
		seleniumEniq.type("id=username", adminUser);
		seleniumEniq.type("id=password", adminPass);
		seleniumEniq.click("id=submit");
		seleniumEniq.waitForPageToLoad("30000");
		Thread.sleep(60000);

		seleniumEniq.click("link=User Management");
		seleniumEniq.waitForPageToLoad("30000");
		if (!seleniumEniq.isTextPresent("dashboard")) {
			createPermissionGroup(permissionGroup);
			createRole(role);
			createUser(user);
		} else {
			userAlreadyExists = true;
		}
		seleniumEniq.click("link=Logout");
		seleniumEniq.waitForPageToLoad("30000");
	}

	@Test
	public void loginAndChanegDashboardPassword() throws Exception {
		eniqVersion = PropertyReader.getInstance().getEniqVersion();
        System.out.println("The Eniq Version is "+eniqVersion);
		// First time to login with the new Dashboard user
		if (!userAlreadyExists) {
			seleniumEniq.start();
			seleniumEniq.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort() + PropertyReader.getInstance().getPath() + "login/Login.jsp");
	        if(eniqVersion.equals("12.1")||eniqVersion.equals("12.2")){
			seleniumEniq.click("//div[@class='GALD-WODG']//input");
			seleniumEniq.type("//div[@class='GALD-WODG']//input", dashboardUser);
			seleniumEniq.typeKeys("//div[@class='GALD-WODG']//input", "");

			seleniumEniq.click("//div[@class='GALD-WODG']//input[contains(@class,'EInputBox EInputBox-light EInputBox-prompt') and @type='text']");
			seleniumEniq.type("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input", dashboardTempPass);
			seleniumEniq.typeKeys("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input", "");

			seleniumEniq.keyPress(Constants.EVENTS_LOG_IN_BUTTON, "\\13");
	        }else{
	        	seleniumEniq.click("//div[@class='GPBYFDEII']//input");
	            seleniumEniq.type("//div[@class='GPBYFDEII']//input", dashboardUser);
	            seleniumEniq.typeKeys("//div[@class='GPBYFDEII']//input", "");
	            
	            seleniumEniq.click("//div[@class='GPBYFDEII']//input[contains(@class,'EInputBox EInputBox-light EInputBox-prompt') and @type='text']");
	            seleniumEniq.type("//div[@class='GPBYFDEII']//div[@class='GPBYFDEHI GPBYFDEKI']//input", dashboardTempPass);
	            seleniumEniq.typeKeys("//div[@class='GPBYFDEII']//div[@class='GPBYFDEHI GPBYFDEKI']//input", "");
	            
	            seleniumEniq.keyPress(Constants.EVENTS_LOG_IN_BUTTON, "\\13");
	        }
			changePassword(dashboardUser, dashboardTempPass, dashboardPass);
			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e) {
			}

			logOut();
			seleniumEniq.stop();
		}
		// Login in with Dashboard user
		else {

			seleniumEniq.start();
			seleniumEniq.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort() + PropertyReader.getInstance().getPath() + "login/Login.jsp");
			logIn(dashboardUser, dashboardPass);
			Thread.sleep(60000);
			logOut();
			seleniumEniq.stop();
		}

	}

	public boolean changePassword(final String username, final String password, final String newPassword) {
		eniqVersion = PropertyReader.getInstance().getEniqVersion();
        System.out.println("The Eniq Version is "+eniqVersion);
        if(eniqVersion.equals("12.1")||eniqVersion.equals("12.2")){
		try {
			seleniumEniq.type("//div[@class='passwordEntryForm']//input[@class='EInputBox EInputBox-light GALD-WOCG EInputBox-prompt']", username);
			seleniumEniq.type("//div[@class='passwordEntryForm']//div[@class='GALD-WOCG GALD-WOFG']//input[@class='EInputBox EInputBox-light']", password);
			seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='EIconButton-login_submit GALD-WOEG EIconButton-login_submit-up']//input", "\\13");
			Thread.sleep(1000);

			seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']");
			seleniumEniq.type("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']", newPassword);
			seleniumEniq.typeKeys("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']", "a");

			Thread.sleep(1000);

			seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
			seleniumEniq.type("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input", newPassword);
			seleniumEniq.keyPress("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input", "\\13");
			Thread.sleep(10000);
		} catch (final InterruptedException e) {
		}
        }else{
        	try {
    	        seleniumEniq.type("//div[@class='passwordEntryForm']//input[@class='EInputBox EInputBox-light GPBYFDEHI EInputBox-prompt']", username);
    	        seleniumEniq.type("//div[@class='passwordEntryForm']//div[@class='GPBYFDEHI GPBYFDEKI']//input[@class='EInputBox EInputBox-light']", password);
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='EIconButton-login_submit GPBYFDEJI EIconButton-login_submit-up']/input","\\13");
    	        Thread.sleep(1000);
    	        
    	        seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light EInputBox-prompt']");
    	        seleniumEniq.type("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']",newPassword);
    	        seleniumEniq.typeKeys("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","a");
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","\\8");
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","\\13");
    	        Thread.sleep(1000);
                
    	        seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input"); 
    	        seleniumEniq.type("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input",newPassword);
    	        seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
    	        seleniumEniq.keyPress("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input","\\13");
                Thread.sleep(10000);
    	        } catch (final InterruptedException e) {
    	        }
        }
		return true;
	}
	
	public boolean createPermissionGroup(final PermissionGroup permissionGroup) {
		seleniumEniq.open(UIConstants.PERMISSION_GROUPS_URL);
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.click("css=button.addButton");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.type("id=permGroupName", permissionGroup.getName().toLowerCase());
		seleniumEniq.type("id=permGroupTitle", permissionGroup.getTitle());
		seleniumEniq.type("id=permGroupDescription", permissionGroup.getDescription());
		for (final String permissionElement : permissionGroup.getPermissionSet()) {
			seleniumEniq.addSelection("id=permissions", "label=" + permissionElement.toLowerCase());
		}
		seleniumEniq.click("name=submit2");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		return seleniumEniq.isTextPresent("Permission Group " + permissionGroup.getName().toLowerCase() + " is created");
	}

	public boolean createRole(final Role role) {
		seleniumEniq.open(UIConstants.ROLE_MGMT_URL);
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.click("css=button.addButton");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.type("id=roleName", role.getName().toLowerCase());
		seleniumEniq.type("id=roleTitle", role.getTitle());
		seleniumEniq.type("id=roleDesc", role.getDescription());

		for (final String permissionGroupElement : role.getpermissionGroupSet()) {
			seleniumEniq.addSelection("name=rolePermGroups", "label=" + permissionGroupElement.toLowerCase());
		}
		seleniumEniq.click("name=submit");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		return seleniumEniq.isTextPresent("Role " + role.getName().toLowerCase() + " is created");
	}

	public boolean createUser(final UserInfo user) {
		seleniumEniq.open(UIConstants.USER_MGMT_URL);
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.click("css=button.addButton");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		seleniumEniq.type("id=userId", user.getUserID().toLowerCase());
		seleniumEniq.type("id=password", user.getPassword());
		seleniumEniq.type("id=confirmPassword", user.getConfirmPassword());
		seleniumEniq.type("id=firstName", user.getFirstName());
		seleniumEniq.type("name=lastName", user.getLastName());
		seleniumEniq.type("id=email", user.getEmail());
		seleniumEniq.type("id=phone", user.getPhone());
		seleniumEniq.type("id=organization", user.getOrganization());
		for (final String roleElement : user.getRoles()) {
			seleniumEniq.addSelection("id=userRoles", "label=" + roleElement.toLowerCase());
		}

		seleniumEniq.click("name=submit");
		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
		return seleniumEniq.isTextPresent("User " + user.getUserID().toLowerCase() + " is created");
	}
	
	public void logIn(final String user, final String pass) {
        if (seleniumEniq.isElementPresent(EVENTS_LOG_IN_BUTTON)) {

            final String usernameField = "//input[@type='text' and not(@tabindex)][1]";
            final String passwordField = "//input[@type='password'][1]";

            seleniumEniq.click(usernameField);
            seleniumEniq.type(usernameField, user);
            seleniumEniq.typeKeys(usernameField, "");

            seleniumEniq.click(passwordField);
            seleniumEniq.type(passwordField, pass);
            seleniumEniq.typeKeys(passwordField, "");

            seleniumEniq.keyPress(EVENTS_LOG_IN_BUTTON, "\\13");
            seleniumEniq.waitForPageToLoad("30000");
        }
    }
	
	public void logOut() {
        if (seleniumEniq.isElementPresent(OPTIONS_BUTTON)) {
            seleniumEniq.click(OPTIONS_BUTTON);

            seleniumEniq.isElementPresent(EVENTS_LOG_OUT_BUTTON);
            seleniumEniq.click(EVENTS_LOG_OUT_BUTTON);
            seleniumEniq.waitForPageToLoad("30000");
        }
    }

	@After
	public void tearDown() throws Exception {
		seleniumEniq.stop();
	}

}
