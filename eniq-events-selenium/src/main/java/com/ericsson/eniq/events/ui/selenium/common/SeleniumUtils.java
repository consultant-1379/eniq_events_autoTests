package com.ericsson.eniq.events.ui.selenium.common;

import org.openqa.selenium.By;

/**
 * 
 * @author eprabab
 * Date : 23/10/12
 * Time : 12:15
 */
public class SeleniumUtils
{
	public static By getElementByXpath(String elementXpath)
	{
		return By.xpath(elementXpath);
	}
	
	public static By getElementById(String elementId)
	{
		return By.id(elementId);
		
	}
	
	public static By getElementByLink(String elementString)
	{
		return By.linkText(elementString);
	}
	
	public static void pause(final int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (final InterruptedException e) {
		}
	}
		
}
