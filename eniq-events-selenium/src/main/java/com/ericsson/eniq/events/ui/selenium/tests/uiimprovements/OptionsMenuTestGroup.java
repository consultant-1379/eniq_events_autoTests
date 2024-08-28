package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.Test;


public class OptionsMenuTestGroup extends EniqEventsUIBaseSeleniumTest{

	//Tab Variables
//	@Autowired
//	private DashboardTab dashboardTab;
	
	//XPath Constants
	static final String OPTIONS_MENU = "//div[text()='Options']";
	static final String MENU_OPTION_FOR_ABOUT = "//div[@class='popupContent']//div[text()='About']";
	
	static final String ABOUT_BOX = "//div[contains(@class, 'about')]";
	static final String ABOUT_BOX_FEATURES_DISPLAY_PANE = "//div[contains(@class, 'about')]//div/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div";
	static final String ABOUT_BOX_CLOSE_ICON = "//div[contains(@class, 'about')]//div[contains(@class, 'close')]";
	
	static final String ABOUT_BOX_ERICSSON_LOGO = "//div[contains(@class, 'about')]//table/tbody/tr/td/img[@class='gwt-Image']";
	static final String ABOUT_BOX_ENIQ_EVENTS_VERSION = "//div[contains(@class, 'about')]//td/table/tbody/tr[1]/td/div[@class='gwt-Label']";
	static final String ABOUT_BOX_ERICSSON_COPYRIGHT_INFO = "//div[contains(@class, 'about')]/div[2]/div[1]/div/div/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div[@class='gwt-Label']";
	//div[@class='gwt-Label'][text()=' Ericsson AB 2012 - All Rights Reserved']
	//Test Case Variables
	String optionsMenuNotFound = " [ Error ] Options menu is not found for ";
	String aboutOptionNotFound = " [ Error ] 'About' menu item not found in Options menu for ";
	String dashTab = "Dashboard";
	String netTab = "Network";
	String termTab = "Terminal";
	String subTab = "Subscriber";
	String rankTab = "Rankings";
	String sonTab = "SonVis";
	String tab = " Tab";
	
	
	//Test case - 4.20.1: Verify that when logged in as admin/dashboard user to EE UI, on the top right of EE UI, there is a menu bar titled 'Options'.
    @Test
    public void optionsMenuPresentForAllTabs_4_20_1() throws Exception {
        waitForPageLoadingToComplete();
    	assertTrue(optionsMenuNotFound, selenium.isElementPresent(OPTIONS_MENU));
    }
    
    //Test case - 4.20.2: Verify that when clicked on the menu bar 'Options', there is an option called 'About'.
    @Test
    public void aboutInOptionsMenuForAllTabs_4_20_2() throws Exception {
//    	dashboardTab.openTab();
        waitForPageLoadingToComplete();
    	selenium.click(OPTIONS_MENU);
    	assertTrue(aboutOptionNotFound + dashTab + tab, selenium.isElementPresent(MENU_OPTION_FOR_ABOUT));
    	selenium.click(OPTIONS_MENU);
    }
    
    /*Test case - 4.20.3: Verify that the 'About' dialog window has the list of all the installed applications on the server with their names
     * followed by the product number of the respective application.
     */
    @Test
    public void allInstalledFeaturesOnServerListedInABoutBox_4_20_3() throws Exception {
        waitForPageLoadingToComplete();
        pause(3000);
        selenium.click(OPTIONS_MENU);
    	selenium.click(MENU_OPTION_FOR_ABOUT);
    	//Verify the content of the box are correct.
        String possibleFeatures [] = {"CXC4011640","CXC4021607","CXC4011641","CXC4010923",
        		"CXC4010924", "CXC4010925", "CXC4010926", "CXC4010927", "CXC4010928", "CXC4010929",
                "CXC4010930", "CXC4010933", "CXC4011049", "CXC4011158", "CXC4011268", "CXC4011278", 
                "CXC4011279", "CXC4011280", "CXC4011291","CXC4011309", "CXC4011318", "CXC4011325", 
                "CXC4011380","CXC4011452","CXC4011455", "CXC4011470","CXC4011506","CXC4011507","CXC4011509","CXC4011510"};
    	
        pause(3000);
        
		for(String s : possibleFeatures ) {
    		assertTrue("[ ERROR ] '" + s + "' not found in About box features list.", selenium.isTextPresent(s));
    	}
    }
    
    //Test case - 4.20.4: Verify that the 'About' dialog window has a vertical scrollbar in order to view the list of all the installed applications.
    @Test
    public void verticalScrollBarPresentInABoutBox_4_20_4() throws Exception {
    	selenium.click(OPTIONS_MENU);
    	selenium.click(MENU_OPTION_FOR_ABOUT);
    	
    	assertTrue("[ ERROR ] The list of features pane is not found", selenium.isElementPresent(ABOUT_BOX_FEATURES_DISPLAY_PANE));
    }
    
    //Test case - 4.20.5: Verify that the 'About' dialog window can be closed using the 'x' on the top right of the window.
    @Test
    public void aboutBoxIsClosedByClickingOnXIcon_4_20_5() throws Exception {
    	selenium.click(OPTIONS_MENU);
    	selenium.click(MENU_OPTION_FOR_ABOUT);
    	
    	//click on 'x' icon
    	selenium.click(ABOUT_BOX_CLOSE_ICON);
    	
    	//Assert that About box is no longer there
    	assertFalse("[ ERROR ] About Box did not dissappear", selenium.isElementPresent(ABOUT_BOX));
    }
    
    /*Test case - 4.21.1: Verify that when clicked on 'About', 
     * a dialog window appears on the EE UI and has Ericsson logo, 'Ericsson OSS' branding, 
     * Sprint version of EE UI and the Ericsson copyright and its version on it.
     */
    @Test
    public void ensureBrandingisPresentInABoutBox_4_21_1() throws Exception {
    	selenium.click(OPTIONS_MENU);
    	selenium.click(MENU_OPTION_FOR_ABOUT);
    	
    	//Ericsson Logo
    	assertTrue("[ ERROR ] Ericsson Logo is not in About Box", selenium.isElementPresent(ABOUT_BOX_ERICSSON_LOGO));
    	
    	//Sprint Version
    	assertTrue("[ ERROR ] ENIQ Events Version is not in About Box", selenium.isElementPresent(ABOUT_BOX_ENIQ_EVENTS_VERSION));
    	
    	//Ericsson Copyright
    	assertTrue("[ ERROR ] Ericsson Copyright info is not in About Box", selenium.isElementPresent(ABOUT_BOX_ERICSSON_COPYRIGHT_INFO));
    }
}
