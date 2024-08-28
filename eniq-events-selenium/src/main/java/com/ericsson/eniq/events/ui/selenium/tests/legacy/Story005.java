package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;

import java.util.Arrays;

import static com.ericsson.eniq.events.ui.selenium.core.Constants.*;
/*
 * Epic 1 : Story 005 : Design and Implement tab views
 * 
 * The aim of this user story is to implement the tabs for the various services views. 
 * The design and implementation involves
 * 		Design and implement the view class 
 * 		The task bar 
 * 		Cascade buttons for windows visible in the tab 
 * 		Tile button for windows visible in the tab 
 * 		Cascade and Tile functionality 
 */

public class Story005 extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*firefox");
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*iexplore");
		//setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*chrome");
	}
	public void teststory005() throws Exception {
		int i;
		//open URL
		selenium.open("/ENIQEventsUI/");
		selenium.windowMaximize();

		//Loop each tab "i" and open all windows
		for (i=0; i<4; i++){
			//setup for tab i
			String tab = "//li[@id='x-auto-2__" + TABNAME[i] + "_TAB']/a[2]/em/span/span";
			String startButton =  "//table[@id='" + TABNAME[i] + "_TAB_START']/tbody/tr[2]/td[2]/em/button";
			String[] startMenu = getStartmenu(i);
			String cascade = getTaskBarCascadeButton(i);
			String tile = getTaskBarTileButton(i);
			
			//open tab and all windows for tab
			selenium.click(tab);
			openAllWindows(startMenu, startButton);
			
			//CHECK TASKBAR BUTTONS
			selenium.click(cascade);
			assertTrue(0<getLeftPos(startMenu[0]));//check that the left most window has a position greater than 0
			selenium.click(tile);
			checkTile(startMenu);
			
			//CHECK MENU BUTTONS
			selenium.click(startButton);
			selenium.click(MENU_CASCADE);
			checkCascade(startMenu);
			selenium.click(startButton);
			selenium.click(MENU_TILE);
			checkTile(startMenu);
		}
	}
	//check CASCADE
	public int[] checkCascade(String[] startMenu){
		int windowID;
		int[] leftpos = new int[startMenu.length];
		for (windowID = 0; windowID <startMenu.length; windowID++){
			leftpos[windowID] = (getLeftPos(startMenu[windowID]));			
		}
		//verify all left pos's have a diff of 40
		if (startMenu.length>1){
			int index;
			//Sorts the specified array of ints into ascending numerical order.
			Arrays.sort(leftpos);
			for (index=0;index<startMenu.length-1;index++){
				assertTrue(leftpos[index]+40==leftpos[index+1]);
			}
		}
		return leftpos;
	}
	//checkTile(startMenu[0]);
	public void checkTile(String[] startMenu){
		if (startMenu.length>1){
			int startWindow1 = getLeftPos(startMenu[0]);
			int startWindow2 = getLeftPos(startMenu[1]);
			int widthWindow1 = getWindowWidth(startMenu[0]);
			assertTrue(startWindow1<=0); //check that left position is <= 0 
			assertTrue(getTopPos(startMenu[0])<=0); //check that top position is <= 0
			int endWindow1 = (startWindow1+ widthWindow1);
			//verify that startMenu[1] has startPosition == (endWindow1)
			assertTrue(endWindow1 == startWindow2);
		}
	}
	//check Left position in pixels of each window
	public int getLeftPos(String window){	
			String newnew = WINDOW_TAG + window + "@style";
			String styleAttribute = selenium.getAttribute(newnew);
			int leftIndex;
			int pxIndex;
			//making case insensitive to cope with CAPS in IE style attribute
			if (styleAttribute.indexOf("left: ")==-1){
				leftIndex = styleAttribute.indexOf("LEFT: ")+6;
				pxIndex = styleAttribute.length()-2;
			}else{
				leftIndex = styleAttribute.indexOf("left: ")+6;
				pxIndex = styleAttribute.indexOf("px; top:");
			}
			String leftPx = styleAttribute.substring(leftIndex, pxIndex);
			return (Integer.parseInt(leftPx));
	}
	//check Top position in pixels of each window
	public int getTopPos(String window){		
			String styleAttribute = selenium.getAttribute(WINDOW_TAG + window + "@style");
			int topIndex;
			int pxIndex;
			if (styleAttribute.indexOf("top: ")==-1){
				//IE
				topIndex = styleAttribute.indexOf("TOP: ")+5;
				pxIndex = styleAttribute.indexOf("px; LEFT");
			}else{
				//FIREFOX
				topIndex = styleAttribute.indexOf("top: ")+5;
				pxIndex = (styleAttribute.length()-3);
			}
			String topPx = styleAttribute.substring(topIndex, pxIndex);
			return Integer.parseInt(topPx);
	}
	//get window width
		public int getWindowWidth(String window){		
			String styleAttribute = selenium.getAttribute(WINDOW_TAG + window + "@style");
			int widthIndex;
			int pxIndex;
			if (styleAttribute.indexOf("width: ")==-1){
				//IE
				widthIndex = styleAttribute.indexOf("WIDTH: ")+7;
				pxIndex = styleAttribute.indexOf("px; TOP");
			}else{
				//FIREFOX
				widthIndex = styleAttribute.indexOf("width: ") +7 ;
				pxIndex = styleAttribute.indexOf("px; z-index");
			}
			String widthPx = styleAttribute.substring(widthIndex, pxIndex);
			return (Integer.parseInt(widthPx));
	}
	//get menu for tab
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
	//open all windows from menu
	public void openAllWindows(String[] startMenu, String startButton){
		int j;
		for (j=0; j<startMenu.length; j++){
			//setup for menuItem
			String windowName = startMenu[j];
			openWindow(startButton, windowName);
		}		
	}
	//get taksbar cascade button
	public static String getTaskBarCascadeButton(int tabNo){
		tabNo=tabNo+1;
		String divNo = Integer.toString(tabNo);
		return ("//html/body/div/div[2]/div[2]/div[" + divNo + TASKBAR_CASCADE);
	}
	//get taksbar tile button
	public static String getTaskBarTileButton(int tabNo){
		tabNo=tabNo+1;
		String divNo = Integer.toString(tabNo);
		return ("//html/body/div/div[2]/div[2]/div[" + divNo + TASKBAR_TILE);
	}
}
