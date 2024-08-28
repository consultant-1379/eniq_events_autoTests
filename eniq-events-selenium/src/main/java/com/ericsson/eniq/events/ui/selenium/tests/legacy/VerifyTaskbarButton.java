package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;

public class VerifyTaskbarButton extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*firefox");
	}
	public void testVerifyTaskbarButton() throws Exception {
		//open URL and open Cause Code Analysis
		////////////////////////////////////////
		selenium.open("/ENIQEventsUI/");
		selenium.click("//table[@id='NETWORK_TAB_START']/tbody/tr[2]/td[2]/em/button");
		selenium.click("NETWORK_CAUSE_CODE_ANALYSIS");
		//error event notification handlers
		assertTrue(selenium.getAlert().matches("^TEMP BasewindowPresenter : Making call to http://10\\.42\\.33\\.235:8080/ENIQEventsUI/NETWORK_CAUSE_CODE_ANALYSIS\\.json[\\s\\S]type=SGSN&node=&time=30$"));
		assertEquals("Response: (partial){\"Success\":true,\"errorDescription\":\"\",\"data\":[{\"col1\":\"1111111111\",\"col2\":\"2009-11-11 23:44:59.0\",\"c", selenium.getAlert());
		
		//Verify that button for Cause Code Analysis is present on taskbar
		assertTrue(selenium.isElementPresent("selenium_tag_MenuTaskBarButton_NETWORK_CAUSE_CODE_ANALYSIS"));
		
		//Verify menu item is disabled
		selenium.click("//table[@id='NETWORK_TAB_START']/tbody/tr[2]/td[2]/em/button");
		assertTrue(selenium.isElementPresent("//*[@id=\"x-menu-el-NETWORK_CAUSE_CODE_ANALYSIS\"]"));
		assertEquals("x-menu-list-item x-item-disabled", selenium.getAttribute("x-menu-el-NETWORK_CAUSE_CODE_ANALYSIS@class"));

		//test min restore max restore close
		//selenium.
		selenium.click("//*[@class=\" x-nodrag x-tool-minimize x-tool\"]");
		selenium.click("//html/body/div/div[2]/div[2]/div/div/div/table/tbody/tr/td/table/tbody/tr/td[7]/table/tbody/tr[2]/td[2]/em/button");
		selenium.click("//*[@class=\" x-nodrag x-tool-maximize x-tool\"]");
		selenium.click("//*[@class=\" x-nodrag x-tool-restore x-tool \"]");
		selenium.click("//*[@class=\" x-nodrag x-tool-close x-tool\"]");
		
		//verify menu item enabled again
		selenium.click("//table[@id='NETWORK_TAB_START']/tbody/tr[2]/td[2]/em/button");
		assertTrue(selenium.isElementPresent("//*[@id=\"x-menu-el-NETWORK_CAUSE_CODE_ANALYSIS\"]"));
		assertEquals("x-menu-list-item ", selenium.getAttribute("x-menu-el-NETWORK_CAUSE_CODE_ANALYSIS@class"));
	}
}
