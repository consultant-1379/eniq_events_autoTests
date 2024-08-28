package com.ericsson.eniq.events.ui.selenium.tests.adminui;

import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;

/**
 * @author erajkir
 * @since 2014 - Added test popup window error message.
 *
 */

public class AdminUIPopupTestGroup extends EniqEventsUIBaseSeleniumTest {
	
	
	
	@Autowired
    private WorkspaceRC workspace;
	
	@Autowired
    private RankingsTab rankingsTab;

	@Override
    @Before
    public void setUp() {
        super.setUp();        
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        //workspace.selectTimeRange(SeleniumConstants.DATE_TIME_1DAY);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_30);
        //workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
        
    }
	
	/*@After
    @Override
    public void tearDown() {
        //TODO: Add timestamp
        if (selenium != null) {
        	selenium.close();
            selenium.stop();
            selenium = null;
        }

	}*/
	
	String closeButtonXPath =  "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";
	 @After
	    @Override
	    public void tearDown() throws Exception {    	
	    	logger.log(Level.INFO, "The Element ID : " + closeButtonXPath);
	        
	        while(selenium.isElementPresent(closeButtonXPath))
	        	selenium.click(closeButtonXPath);
	        super.tearDown();
	    }
	
	 
	 
	 @Test
	 
		public void checkAndOpenSideLaunchBar() {
			// check if Side Launch Bar is closed
			//	 if(driver.findElements(By.xpath("//div[@id='x-auto-0']/div[@id='x-auto-1']/div[@class='x-tab-panel-body x-tab-panel-body-top']/div[@id='NEW_WORKSPACE_1_1_TAB']/div[@id='x-auto-23']/div[@class='GFMUOL5DIX GFMUOL5DOPB']/div[@class='GFMUOL5DLX']")).size() != 0){
			System.out.println("\nHERE");
			String launchItem = "//span[contains(text(),'Update Workspace')]";
			// if(driver.findElements(By.xpath("//div[@class='GFMUOL5DNPB']")).size() != 0){
			assertFalse("Custom workspace opened",selenium.isElementPresent(launchItem));
		//	if(selenium.isElementPresent(launchItem)){
		//		System.out.println("Custom workspace opened, test failed");
		//	 }else{
		//		 System.out.println("Custom workspace opened, test passed");
		//	 }	
			
	 }
	 
	 
	 
	 
	 @Test
		public void checkingPopupError() throws Exception
		{
			openWindow("3G Ranking","Call Drops");
			String failureReason = null;
			if(tablePresent()){
				failureReason=selenium.getText("//*[@id='errorMessage']/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[2]");
			}else{
				failureReason="N/A";
			}
			Assert.assertFalse(failureReason,tablePresent());
			
			//assertFalse(selenium.getText("//*[@id='errorMessage']/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[2]"),tablePresent());
			
			
		}
	
/////////////////////////////////////////////////////////////////////////////
//P R I V A T E   M E T H O D S
///////////////////////////////////////////////////////////////////////////////
	
	private void openWindow(String ranking,String calldrops) 
			throws Exception
			{
				
			    workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);	
			    workspace.selectDimension(SeleniumConstants.RADIO_NETWORK_3G);
				pause(2000);
				workspace.selectWindowType(ranking, calldrops);
		        workspace.clickLaunchButton();
		        waitForPageLoadingToComplete();        
		        pause(2000);
		   }

	
private boolean tablePresent() {
		
		//System.out.println(selenium.getText("//*[@id='errorMessage']/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[2]"));
		return selenium.isElementPresent("//div[@id='selenium_tag_baseWindow']//div[@class='x-panel-body']/table[@id='errorMessage']");
		
		
		
	   // return driver.findElement(By.xpath("//div[@id='selenium_tag_baseWindow']//div[@class='x-panel-body']/table[@id='errorMessage']")).isDisplayed();
	   //System.err.println("window not found: " + e.getMessage());
	}


}
	


	

