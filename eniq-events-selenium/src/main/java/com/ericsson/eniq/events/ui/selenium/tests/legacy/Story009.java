package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;
/*
 * Epic 1 : Story 009 : Design and Implement line scroll and live paging in grids
 * 
 *	Populate a grid with data via a user action. Verify that you have the ability 
 *		to navigate through the pages of data via the "First", "Prev", "Next", "Last" buttons. 
 *	Verify the Refresh button. 
 *
 */

public class Story009 extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*firefox");
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*iexplore");
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*chrome");
	}
	public void testStory009() throws Exception {
		selenium.open("/ENIQEventsUI/");
		selenium.windowMaximize();
		Thread.currentThread();
		checkTestData();
		//checkRealData();
	}

	public static String[] getStartmenu (int a) {
			String[] startMenu = TAB0_STARTMENU;
			switch (a) {
			case 0:  startMenu = TAB0_STARTMENU; break;
			case 1:  startMenu = TAB1_STARTMENU; break;
			case 2:  startMenu = TAB2_STARTMENU; break;
			case 3:  startMenu = TAB3_STARTMENU; break;
			}
			return startMenu;
	   	}
		//open window
		public void openWindow (String startButton, String windowName) {
			selenium.click(startButton);
			selenium.click(windowName);
		}
		public void checkTestData() throws InterruptedException{
			selenium.click("//li[@id='x-auto-2__SUBSCRIBER_TAB']/a[2]/em/span/span");
			selenium.type("//td[5]/div/input", "440008704283457");
			selenium.click("//table[@id='SUBSCRIBER_TAB_START']/tbody/tr[2]/td[2]/em/button");
			selenium.click("//a[@id='SUBSCRIBER_EVENT_ANALYSIS']/img");
			Thread.sleep(5000);
			String numPages = selenium.getText("//td[6]/div");
			numPages = numPages.substring(3);
	
			verifyEquals("1", selenium.getValue("//td[5]/input"));
			
			//click next page button
			selenium.click("//td[8]/table/tbody/tr[2]/td[2]/em/button");
			verifyEquals("2", selenium.getValue("//td[5]/input"));
			
			//click last page button
			selenium.click("//table[@id='x-auto-39']/tbody/tr[2]/td[2]/em/button");
			verifyEquals(numPages, selenium.getValue("//td[5]/input"));
			
			//click first page
			selenium.click("//div[3]/div/table/tbody/tr/td[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/em/button");
			verifyEquals("1", selenium.getValue("//td[5]/input"));
		}
		public void checkRealData() throws InterruptedException{
			int i,j;
			//Loop each tab "i"
			for (i=0; i<4; i++){
				//setup for tab i
				String tab = "//li[@id='x-auto-2__" + TABNAME[i] + "_TAB']/a[2]/em/span/span";
				String startButton =  "//table[@id='" + TABNAME[i] + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
				String[] startMenu = getStartmenu(i);
				//open tab
				selenium.click(tab);
				//loop to open each window option on the start menu
				//cascade and tile? Not in this US
				//exit loop then move onto next tab.
				for (j=0; j<startMenu.length; j++){
					//setup for menuItem
					String windowName = startMenu[j];
					openWindow(startButton, windowName);
					Thread.sleep(5000);
					String ofOne = "of 1";
					String pages = selenium.getText("//td[6]/div");
					String numPages = pages.substring(3);
					
					if (pages.equals(ofOne) == false && pages !=null && pages.length() > 0){
						verifyEquals("1", selenium.getValue("//td[5]/input"));
						
						//click next page button
						selenium.click("//td[8]/table/tbody/tr[2]/td[2]/em/button");
						verifyEquals("2", selenium.getValue("//td[5]/input"));
						
						//click last page button
						selenium.click("//table[@id='x-auto-39']/tbody/tr[2]/td[2]/em/button");
						verifyEquals(numPages, selenium.getValue("//td[5]/input"));
						
						//click first page
						selenium.click("//div[3]/div/table/tbody/tr/td[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/em/button");
						verifyEquals("1", selenium.getValue("//td[5]/input"));
					}
				}
			}
		}
}