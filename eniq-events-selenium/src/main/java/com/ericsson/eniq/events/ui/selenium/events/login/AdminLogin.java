/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.login;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.Constants;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AdminLogin class provides a number of helper functions for Selenium
 * tests, allowing the user to log in and out of the admin page.
 * 
 * @author ericker
 * @since 2010
 */
@Component
public class AdminLogin extends AUserLogin {

	protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());
	
	@Autowired
	protected EricssonSelenium selenium;

	// TODO: reuse logIn(...) from AUserLogin
	@Override
	public void logIn(final String user, final String pass) {
		if (!isLoggedIn()) {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + "j_username");
			selenium.type("j_username", user);
			loggerDuplicate.log(Level.INFO, "The Element ID : " + "j_password");
			selenium.type("j_password", pass);
			loggerDuplicate.log(Level.INFO, "The Element ID : " + Constants.ADMIN_LOG_IN_BUTTON);
			selenium.click(Constants.ADMIN_LOG_IN_BUTTON);
			selenium.waitForPageToLoad("30000");
		}
	}

	// TODO: reuse logOut() from AUserLogin
	@Override
	public void logOut() {
		if (isLoggedIn()) {
			loggerDuplicate.log(Level.INFO, "The Element ID : " + Constants.ADMIN_LOG_OUT_BUTTON);
			selenium.click(Constants.ADMIN_LOG_OUT_BUTTON);
			selenium.waitForPageToLoad("30000");
		}
	}

	public boolean isLoggedIn() {
		boolean retVal = false;
		if (selenium.isElementPresent("//body/table/tbody/tr[2]/td/table/tbody/tr/td[1]/font") && ("You are logged in as: " + getUserName()).equals(selenium.getText("//body/table/tbody/tr[2]/td/table/tbody/tr/td[1]/font"))) {
			retVal = true;
		}
		return retVal;
	}

	public String getHost() {
		return PropertyReader.getInstance().getAdminServerHost();
	}

	public String getPort() {
		return PropertyReader.getInstance().getAdminServerPort();
	}

	public String getPath() {
		return PropertyReader.getInstance().getAdminServerPath();
	}

	@Override
	public String getUserName() {
		return PropertyReader.getInstance().getAdminUser();
	}

	@Override
	protected String getUserPwd() {
		return PropertyReader.getInstance().getAdminPwd();
	}
}
