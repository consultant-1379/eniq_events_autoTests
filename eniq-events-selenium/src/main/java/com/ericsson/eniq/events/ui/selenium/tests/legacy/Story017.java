package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;
/*
 * Epic 1 : Story 017 : Time Dialog Parameters Dialog Window
 * 
 * The following functionality needs to be verified. 
 * 	Clicking the Menu Button Launches the Dialog in the centre of the Parent Window.
 * 	Selection of the Time Range radio button disables the Date And Time Range Controls.
 * 	Selection of the Date and Time Range radio button disables the Time Range Controls.
 * 	Clicking cancel button closes the dialog with the parent window unaltered.
 * 	Clicking OK button closes the dialog and refreshes the parent window with the results 
 * 	of the query from the server. 
 */
public class Story017 extends SeleneseTestCase {
	public void setUp() throws Exception {
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*firefox");
		setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*iexplore");
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*chrome");
	}
	public void teststory017() throws Exception {
		//open URL
		selenium.open("/ENIQEventsUI/");
		String tab = "//li[@id='x-auto-2__" + NETWORK + "_TAB']/a[2]/em/span/span";
		String startButton =  "//table[@id='" + NETWORK + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
		
		//open tab, startMenu, window
		selenium.click(tab);
		selenium.click(startButton);
		selenium.click("NETWORK_CAUSE_CODE_ANALYSIS");
		
		//get initial window load time
		Date firstLoadTime = getRefreshTime(selenium.getText("//td[3]/label"));
		
		selenium.windowMaximize();
		//sleep so that next time window is refreshed the time will be at least 1 min later
		/*try{
			  Thread.currentThread();
			  Thread.sleep(60000);//sleep for 120s
			}
			catch(InterruptedException ie){
			}*/
		
		//CHECK CANCEL BUTTON
		openTimeDialog();
		selenium.click(TIME_WINDOW_CANCEL);
		assertFalse(selenium.isElementPresent("//div[@class=' x-window timeDlg']"));
		
		//TOGGLE RADIO BUTTONS
		openTimeDialog();
		selenium.click(DATE_TIME_RADIO_BUTTON);
		selenium.click(TIME_RADIO_BUTTON);

		//CHECK DISABLED
		selenium.click(DATE_TIME_RADIO_BUTTON);
		checkTimeDisabled();
		selenium.click(TIME_RADIO_BUTTON);
		checkDateDisabled();
		
		//selenium.click(TIME_BUTTON_LOCATOR);
		selenium.click("//table[@id='btnTime']/tbody/tr[2]/td[2]/em/button");
		selenium.click("//div[@id='selenium_tag_time']/img");
		
		selenium.type("//div[2]/input[1]", "30 Mins");

		//typeWithFullKeyEvents("//input[@id='selenium_tag_time-input']","30 Mins",false);
		
		//selenium.type("selenium_tag_time-input", "30 Mins");//NOT MF WORKING
		//selenium.click("//td[2]/table/tbody/tr/td[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/em/button");
		//assertEquals("30 Mins", selenium.getValue("//div[2]/input[1]"));
		
		selenium.click(TIME_WINDOW_OK);
		Date thisRefreshTime = getRefreshTime(selenium.getText("//td[3]/label"));
		verifyTrue(firstLoadTime.before(thisRefreshTime));
		firstLoadTime = thisRefreshTime;
		//Thread.sleep(60000);

		//CHECK DROP DOWN - TIME
		//Loop through different times
		/*String timeParam;
		int t;
		for (t=0; t<3; t++){
			timeParam = TIME_DROP_DOWN[t];
			selenium.click(TIME_BUTTON_LOCATOR);
			selenium.type("selenium_tag_time-input", timeParam);
			assertEquals(timeParam, selenium.getValue("//div[2]/input[1]"));
		
			//CLICK OK AND VERIFY REFRESH
			selenium.click(TIME_WINDOW_OK);
			Date thisRefreshTime = getRefreshTime(selenium.getText("//td[3]/label"));
			verifyTrue(firstLoadTime.before(thisRefreshTime));
			firstLoadTime = thisRefreshTime;
			Thread.sleep(60000);
		}*/
		selenium.close();
	}
	
	public void openTimeDialog () {
		checkTimeButtonExists();
		selenium.click(TIME_BUTTON_LOCATOR);
		assertTrue(selenium.isElementPresent("//div[@class=' x-window timeDlg']"));
	}
	public void checkTimeButtonExists (){
		assertTrue(selenium.isElementPresent(TIME_BUTTON_LOCATOR));
	}
	public void checkTimeDisabled(){
		//check 2nd radio button & check time combo boxes are disabled
		checkDisabled("selenium_tag_time");
	}
	public void checkDateDisabled(){
		//check time radio button & check time&date combo boxes are disabled
		checkDisabled("selenium_tag_fromDate");
		checkDisabled("selenium_tag_toDate");
		checkDisabled("selenium_tag_fromTime");
		checkDisabled("selenium_tag_toTime");
	}
	//check item disabled method
	public void checkDisabled(String itemLocator){
		String classAttribute = selenium.getAttribute(itemLocator + "@class");
		assertTrue(classAttribute.endsWith("x-item-disabled"));
	}
	public Date getRefreshTime(String displayedTime){
		String strFormat = "yy-MM-dd HH:mm";
		DateFormat formatter = new SimpleDateFormat(strFormat);
		Date refreshTime = null;
	    try {
			refreshTime = formatter.parse(displayedTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return refreshTime;
	}
	 public void typeWithFullKeyEvents(String locator, String inputString, boolean reset){
		 if (reset){
	            selenium.type(locator, "");
	     }
		 char[] chars = inputString.toCharArray();
	     // Some browsers clear the text field when we start typing,
	     // so we need to pre-populate this string with the existing contents
	     StringBuffer sb = new StringBuffer(selenium.getValue(locator));
	     for (int i = 0; i < chars.length; i++){
	         char aChar = chars[i];
	         String key = Character.toString(aChar);
	         sb.append(aChar);
	         selenium.keyDown(locator, key);
	         // some browsers dont actually input any characters on these events
	         // supposedly to prevent JS spoof attacks. So we type for them
	        // if (!(SeleniumProxyUtil.getInstance().getUserAgent().equals("firefox"))){
	         //    selenium.type(locator, sb.toString());
	         //}
	         selenium.keyPress(locator, key);
	         selenium.keyUp(locator, key);
	      }
	 }
}