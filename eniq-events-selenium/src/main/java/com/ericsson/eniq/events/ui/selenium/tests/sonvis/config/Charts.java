package com.ericsson.eniq.events.ui.selenium.tests.sonvis.config;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) ${year} LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

/**
 * @author efrfarr
 * @since June 2013
 * 
 */


/*
 * Utility class for testing charts
 */

public class Charts {
	
	protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();
		
	
	public String getLabelText(String xpath)
	{
		WebElement elementToFindTextOf = (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		
		return elementToFindTextOf.getText();
	}

}
