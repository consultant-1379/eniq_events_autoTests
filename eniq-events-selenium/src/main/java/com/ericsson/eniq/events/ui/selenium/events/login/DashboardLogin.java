package com.ericsson.eniq.events.ui.selenium.events.login;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.EVENTS_LOG_IN_BUTTON;

@Component
public class DashboardLogin extends AUserLogin {

	protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

	@Override
	public void logIn(final String user, final String pass) {
		if (selenium.isElementPresent(EVENTS_LOG_IN_BUTTON)) {
			final double version = PropertyReader.getInstance().getEniqRootVersion();
			if(version >= 13.0){
				selenium.click("//div[@class='GPBYFDEII']/input");
				selenium.type("//div[@class='GPBYFDEII']/input", user);
				selenium.typeKeys("//div[@class='GPBYFDEII']/input", "");
				selenium.click("//div[@class='GPBYFDEII']/div[@class='GPBYFDEHI GPBYFDEKI']/input[@class='EInputBox EInputBox-light']");
				selenium.type("//div[@class='GPBYFDEII']/div[@class='GPBYFDEHI GPBYFDEKI']/input[@class='EInputBox EInputBox-light']", pass);
				selenium.typeKeys("//div[@class='GPBYFDEII']/div[@class='GPBYFDEHI GPBYFDEKI']/input[@class='EInputBox EInputBox-light']", "");
			}else{
				selenium.click("//div[@class='GALD-WODG']//input");
				selenium.type("//div[@class='GALD-WODG']//input", user);
				selenium.typeKeys("//div[@class='GALD-WODG']//input", "");
				selenium.click("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input[@type='text' and @class='EInputBox EInputBox-light EInputBox-prompt']");
				selenium.type("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input[@type='password' and @class='EInputBox EInputBox-light']", pass);
				selenium.typeKeys("//div[@class='GALD-WODG']//div[@class='GALD-WOCG GALD-WOFG']//input[@type='password' and @class='EInputBox EInputBox-light']", "");
			}
			selenium.keyPress(EVENTS_LOG_IN_BUTTON, "\\13");
			selenium.waitForPageToLoad("30000");
		}
	}

	@Override
	public String getUserName() {
		return PropertyReader.getInstance().getDashboardUser();
	}

	@Override
	public String getUserPwd() {
		return PropertyReader.getInstance().getDashboardPwd();
	}
}