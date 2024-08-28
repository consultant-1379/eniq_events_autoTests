package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * 
 * @author ekumpen 2012
 */
public class EricssonFirefoxWebDriver extends FirefoxDriver {
	private static EricssonFirefoxWebDriver INSTANCE;

	public static EricssonFirefoxWebDriver getSharedInstance() {
		if (INSTANCE == null) {
			// To avoid Firebug startup screen
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("extensions.firebug.currentVersion", "1.8.1");
			//profile.setPreference("extensions.firebug.currentVersion", "1.10.6");
			
			INSTANCE = new EricssonFirefoxWebDriver(profile);
			
//			INSTANCE = new EricssonFirefoxWebDriver(new FirefoxBinary(new File("E:\\edecmcc\\browsers\\Firefox 10.0.9\\core\\firefox.exe")), profile);
			//INSTANCE = new EricssonFirefoxWebDriver(new FirefoxBinary(new File("C:\\Program Files (x86)\\Mozilla Firefox 16\\firefox.exe")), profile);
		}
		return INSTANCE;

	}

	private EricssonFirefoxWebDriver(FirefoxProfile profile) {
		super(profile);
	}
	
	private EricssonFirefoxWebDriver(FirefoxBinary binary , FirefoxProfile profile) {
	    super(binary, profile);
	}
	
	public static void destroy() {
		INSTANCE = null;
	}
}
