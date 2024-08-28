package com.ericsson.eniq.events.ui.selenium.tests.webdriver;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class CommonUtilsMethodsTest {

	@Test
	public void testconvertToDublinTimeZoneDate() throws ParseException {
		String time = "2014-04-09 10:00:00.000";
		String newDublinTime = null;
		newDublinTime = CommonUtils.getTimeInDublinTimeZone(time);
		Assert.assertEquals("2014-04-09 11:00:00.000", newDublinTime);
	}
}
