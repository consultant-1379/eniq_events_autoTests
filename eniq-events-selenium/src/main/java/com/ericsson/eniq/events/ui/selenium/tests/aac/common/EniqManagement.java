/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ui.selenium.tests.aac.common;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.core.Constants;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.PermissionGroup;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import org.springframework.stereotype.Component;

/**
 * @author ejenkav
 * @since 2011
 *
 */
@Component
public class EniqManagement {
    public static EricssonSelenium seleniumEniq = new EricssonSelenium();
    private String eniqVersion;
    public boolean login(String username, final String password) {
        username = username.toLowerCase();
        if (isLoggedInAsUser(username)) {
            return true;
        }
        logout();
        eniqVersion = PropertyReader.getInstance().getEniqVersion();
        System.out.println("The Eniq Version is "+eniqVersion);
        if(eniqVersion.equals("12.1")||eniqVersion.equals("12.2")){
        //seleniumEniq.open(PropertyReader.getInstance().getPath());
        	seleniumEniq.open(UIConstants.ENIQ_LOGIN_PAGE_URL);
        	seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        	if(!seleniumEniq.isElementPresent("//div[@class='GALD-WODG']//input")){
        		System.out.println("Wait for Element //div[@class='GALD-WODG']//input to present ");
        		seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        	}
        	seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
            seleniumEniq.click("//div[@class='GALD-WODG']//input");
            seleniumEniq.type("//div[@class='GALD-WODG']//input", username);
            seleniumEniq.typeKeys("//div[@class='GALD-WODG']//input", "");
            
            seleniumEniq.click("//div[@class='GALD-WODG']//input[contains(@class,'EInputBox EInputBox-light EInputBox-prompt') and @type='text']");
            seleniumEniq.type("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input", password);
            seleniumEniq.typeKeys("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input", "");
            
            seleniumEniq.keyPress(Constants.EVENTS_LOG_IN_BUTTON, "\\13");
        	
        
        }else{
        	seleniumEniq.open(UIConstants.ENIQ_LOGIN_PAGE_URL);
        	seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        	if(!seleniumEniq.isElementPresent("//div[@class='GPBYFDEII']/input")){
        		System.out.println("Wait for Element //div[@class='GPBYFDEII']/input to present ");
        		try {
                    Thread.sleep(30000);
                } catch (final InterruptedException e) {
                }
        		//seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        	}
        	if(!seleniumEniq.isElementPresent("//div[@class='GPBYFDEII']/input")){
    			System.out.println("Refresh Page!!");
    			seleniumEniq.refresh();
    			seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
    		}
            seleniumEniq.click("//div[@class='GPBYFDEII']/input");
            seleniumEniq.type("//div[@class='GPBYFDEII']/input", username);
            seleniumEniq.typeKeys("//div[@class='GPBYFDEII']/input", "");
            
            seleniumEniq.click("//div[@class='GPBYFDEII']//input[contains(@class,'EInputBox EInputBox-light EInputBox-prompt') and @type='text']");
            seleniumEniq.type("//div[@class='GPBYFDEII']//div[@class='GPBYFDEHI GPBYFDEKI']//input", password);
            seleniumEniq.typeKeys("//div[@class='GPBYFDEII']//div[@class='GPBYFDEHI GPBYFDEKI']//input", "");
            
            seleniumEniq.keyPress(Constants.EVENTS_LOG_IN_BUTTON, "\\13");

        }
        
 /*        //seleniumEniq.click("//div[@class='GALD-WODG']//input");
        seleniumEniq.type("//div[@class='GALD-WODG']//input", username);
        //seleniumEniq.typeKeys("//div[@class='GALD-WODG']//input", "");
        
        //seleniumEniq.click("//div[@class='GALD-WODG']//input[contains(@class,'EInputBox EInputBox-light EInputBox-prompt') and @type='text']");
        seleniumEniq.type("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOEG']//input[@class='EInputBox EInputBox-light']", password);
       // seleniumEniq.typeKeys("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOEG']//input", "");
        
        seleniumEniq.keyPress(Constants.EVENTS_LOG_IN_BUTTON, "\\13");
        //seleniumEniq.waitForPageToLoad("30000");
*/        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {
        }
        //seleniumEniq.waitForElementToBePresent(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH, UIConstants.PAGE_LOAD_TIMEOUT);
        if (seleniumEniq.isTextPresent(UIConstants.ENIQ_PASSWORD_CHANGE_MSG)
                || seleniumEniq.isTextPresent(UIConstants.ENIQ_NO_ROLE_MSG)) {
            return true;
        }

        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {
        }
        
        return isLoggedInAsUser(username);
    }

    public boolean logInAsAdmin() {
        return login(PropertyReader.getInstance().getUser(), PropertyReader.getInstance().getPwd());
    }

    public boolean firstTimeLogin(String username, final String password, final String newPassword) {
        username = username.toLowerCase();
        if (isLoggedInAsUser(username)) {
            return false;
        }
        logout();
        seleniumEniq.open(PropertyReader.getInstance().getPath());
        seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        seleniumEniq.type(UIConstants.ENIQ_USERNAME_TEXTBOX, username);
        seleniumEniq.type(UIConstants.ENIQ_PASSWORD_TEXTBOX, password);
        seleniumEniq.click(UIConstants.ENIQ_SUBMIT_BUTTON);
        seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        if (!seleniumEniq.isTextPresent(UIConstants.ENIQ_PASSWORD_CHANGE_MSG)) {
            return false;
        }
        if (!changePassword(username, password, newPassword)) {
            return false;
        }
        try {
            Thread.sleep(5000);
        } catch (final InterruptedException e) {
        }
        return login(username, newPassword);
    }

    public boolean changePassword(final String username, final String password, final String newPassword) {
        //seleniumEniq.open(PropertyReader.getInstance().getPath() + "/login/ChangePassword.jsp");
        //seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
    	eniqVersion = PropertyReader.getInstance().getEniqVersion();
        System.out.println("The Eniq Version is "+eniqVersion);
        if(eniqVersion.equals("12.1")||eniqVersion.equals("12.2")){
        	try {
        	     //System.out.println("1");
        	        seleniumEniq.type("//div[@class='passwordEntryForm']//input[@class='EInputBox EInputBox-light GALD-WOCG EInputBox-prompt']", username);
        	        //System.out.println("2");
        	        seleniumEniq.type("//div[@class='passwordEntryForm']//div[@class='GALD-WOCG GALD-WOFG']//input[@class='EInputBox EInputBox-light']", password);
        	        //System.out.println("3");
        	        //seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='ewcl-light-EIconButton-login_submit GALD-WOCG GALD-WOEG ewcl-light-EIconButton-login_submit-up']//input","\\13");
        	        seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='EIconButton-login_submit GALD-WOEG EIconButton-login_submit-up']//input","\\13");
        	                       
        	                       
        	                             
        	        //System.out.println("4");
        	        Thread.sleep(1000);
        	        //seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']");
        	       
        	        seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']");
        	        //System.out.println("5");
        	        seleniumEniq.type("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']",newPassword);
        	        //System.out.println("6");
        	        seleniumEniq.typeKeys("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']","a");
        	        //System.out.println("7");
        	 
        	        Thread.sleep(1000);
        	        
        	        //seleniumEniq.click("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
        	        seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
        	                      
        	        //System.out.println("8");
        	        //seleniumEniq.type("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input",newPassword);
        	        seleniumEniq.type("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input",newPassword);
        	 
        	        //seleniumEniq.keyPress("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input","\\13");
        	        seleniumEniq.keyPress("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input","\\13");
        	 
        	        //System.out.println("10");
        	       //seleniumEniq.type("//div[2]/div[2]/input", "");
        	        /*seleniumEniq.click("//div[@class='ewcl-light-EIconButton-login_submit GALD-WOCG GALD-WOEG ewcl-light-EIconButton-login_submit-up']");
        	        seleniumEniq.type("//div[6]/input", newPassword);
        	        seleniumEniq.type("//div[7]/input", newPassword);
        	        seleniumEniq.click("//div[2]/div[8]");*/
        	        
        	           Thread.sleep(10000);
        	        } catch (final InterruptedException e) {
        	        }

        }else{
        	try {
    	    	//System.out.println("1");
    	        seleniumEniq.type("//div[@class='passwordEntryForm']//input[@class='EInputBox EInputBox-light GPBYFDEHI EInputBox-prompt']", username);
    	        //System.out.println("2");
    	        seleniumEniq.type("//div[@class='passwordEntryForm']//div[@class='GPBYFDEHI GPBYFDEKI']//input[@class='EInputBox EInputBox-light']", password);
    	        //System.out.println("3");
    	        //seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='ewcl-light-EIconButton-login_submit GALD-WOCG GALD-WOEG ewcl-light-EIconButton-login_submit-up']//input","\\13");
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm']//div[@class='EIconButton-login_submit GPBYFDEJI EIconButton-login_submit-up']/input","\\13");
    	        															
    	        															
    	        																				 
    	        //System.out.println("4");
    	        Thread.sleep(1000);
    	        //seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GALD-WOCG']//input[@class='EInputBox EInputBox-light']");
    	       
    	        seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light EInputBox-prompt']");
    	        //System.out.println("5");
    	        //String[] newPasswordArray = newPassword.split("4");
    	        //String newPassword1 = newPasswordArray[0];
    	        seleniumEniq.type("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']",newPassword);
    	        
    	        //System.out.println("6");
    	        seleniumEniq.typeKeys("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","a");
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","\\8");
    	        //System.out.println("7");
    	        seleniumEniq.keyPress("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI']//input[@class='EInputBox EInputBox-light']","\\13");
    	        Thread.sleep(1000);
    	        
    	        //seleniumEniq.click("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
    	        seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
    	        //System.out.println("1");
    	        //seleniumEniq.click("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI GPBYFDEKI']//input[@class='EInputBox EInputBox-light EInputBox-prompt']"); 				     					
    	        //System.out.println("8");
    	        //seleniumEniq.type("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input",newPassword);
    	       seleniumEniq.type("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input",newPassword);
    	        //seleniumEniq.type("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI GPBYFDEKI']//input[@class='EInputBox EInputBox-light']",newPassword);
    	       //System.out.println("2");
    	       seleniumEniq.click("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[7]/input");
    	        //System.out.println("3");
    	        
    	        //seleniumEniq.keyPress("//html/body/div[9]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input","\\13");
    	        seleniumEniq.keyPress("//html/body/div[5]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/div[2]/div/div/div[2]/div[8]/input","\\13");
    	       // seleniumEniq.keyPress("//div[@class='passwordEntryForm passwordEntryForm-show']//div[@class='GPBYFDEHI GPBYFDEKI']//input[@class='EInputBox EInputBox-light']","\\13");
    	        //System.out.println("10");
    	       //seleniumEniq.type("//div[2]/div[2]/input", "");
    	        /*seleniumEniq.click("//div[@class='ewcl-light-EIconButton-login_submit GALD-WOCG GALD-WOEG ewcl-light-EIconButton-login_submit-up']");
    	        seleniumEniq.type("//div[6]/input", newPassword);
    	        seleniumEniq.type("//div[7]/input", newPassword);
    	        seleniumEniq.click("//div[2]/div[8]");*/
    	        
    	           Thread.sleep(10000);
    	        } catch (final InterruptedException e) {
    	        }

        }
        //return isLoggedInAsUser(username);
        return true;
       // return seleniumEniq.getLocation().endsWith("Login.jsp");

    }

    public boolean isLoggedInAsUser(String username) {
        username = username.toLowerCase();
        if (!isLoggedIn()) {
            return false;
        }
        if (!seleniumEniq.isElementPresent(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH) ) {
            return false;
        }
        seleniumEniq.click(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH);
        if(seleniumEniq.isElementPresent(UIConstants.OPTION_AFTER_LOGGED_IN_XPATH) && 
        		seleniumEniq.getText(UIConstants.OPTION_AFTER_LOGGED_IN_XPATH).toUpperCase().contains(username.toUpperCase())){
        	seleniumEniq.click(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH);//[@class='GMVA0CYBEE GMVA0CYBDE']");
        	return true;
        }
        seleniumEniq.click(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH);
        return  false;
    }

    public boolean logout() {
        if (isLoggedIn()) {
        	seleniumEniq.click(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH);
            seleniumEniq.click(UIConstants.OPTION_AFTER_LOGGED_IN_XPATH);
            seleniumEniq.waitForPageToLoad(UIConstants.PAGE_LOAD_TIMEOUT);
        	
        }
        return true;
    }

    public boolean isLoggedIn() {
        if ((seleniumEniq.isElementPresent(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH))
                && (seleniumEniq.getText(UIConstants.ENIQ_LOGGED_IN_MSG_XPATH).contains(UIConstants.ENIQ_LOGGED_IN_MSG))) {
            return true;
        }
        return false;
    }

    public boolean isTabEnaled(final String permission) {
        if (!isLoggedIn()) {
            return false;
        }
        final String xPath = PermissionGroup.Permission.getEniqXPath(permission);
        try {
            final String classAttributeValue = seleniumEniq.getAttribute(xPath + "@class");

            if (classAttributeValue == null)
                return false;
            if (classAttributeValue.toLowerCase().contains(UIConstants.ENIQ_TAB_CLASS_ITEM_DISABLE)
                    || classAttributeValue.toLowerCase().contains(UIConstants.ENIQ_TAB_CLASS_LOCKED_NO_LICENSE)) {
                return false;
            }
        } catch (final Exception e) {
        }
        return true;
    }
}
