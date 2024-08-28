package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.StartMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants.DIMENSION_GROUP_TEXTFIELD_ID;

public class PieChartsTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    protected NetworkTab networkTab;
    
    @Autowired
    private Workspace workspace;
    
    @Autowired
    WorkspaceRC workspaceRC;
    
    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;
    
    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysis;
    
    static final String TIME_SETTINGS_BUTTON = "//div[@id='btnTime']";
  //table[@id='btnTime']//button
    static final String REFRESH_BUTTON = "//div[@id='btnRefresh']";

    static final String LEGEND_BUTTON = "//div[@id='btnHideShowLegend']";

    //static final String LAUNCH_BUTTON = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]"
     //       + "/div//div[button='Launch']/button";
    
    static final String LAUNCH_BUTTON = "//div//button[text()='Launch']";

    static final String TIME_RANGE_COMP = "//div[@id='timeRangeComp']/img";

    static final String CHART_VIEW;

    static final String GRID_VIEW;

    static final String SELECT_ALL;

    static final String MULTIPLE_VIEWS = "//div[@id='selenium_tag_MultiWinSelectComponent']/input";

    
    
     

    
    
    
    
    static {
    	final double version = PropertyReader.getInstance().getEniqRootVersion(); 
    	
    	if(version >= 13.0){
    		CHART_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//div[label='Chart View']/input";

    	    GRID_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//div[label='Grid View']/input";

    	    SELECT_ALL = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Select all']/input";
    	}else{
    		CHART_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Chart View']/input";

    	    GRID_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Grid View']/input";

    	    SELECT_ALL = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Select all']/input";
    	}
    }

    //Test case - 4.15.2: View the new Cause Code Analysis GUI with all buttons and options for Controller from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforController_4_15_2() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();	
    }

    //Test case - 4.15.3 view the new Cause Code Analysis GUI with all buttons and options for SGSN-MME from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforSGSN_MME_4_15_3() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.4 view the new Cause Code Analysis GUI with all buttons and options for Access Area from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforAccess_Area_4_15_4() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.6 view the new Cause Code Analysis GUI with all buttons and options for Access Area Group from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforAccess_Area_Group_4_15_6() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.7 view the new Cause Code Analysis GUI with all buttons and options for Controller Group from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforController_Group_4_15_7() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.8 View the new Cause Code Analysis GUI with all buttons and options for APN from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforAPN_4_15_8() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.9 View the new Cause Code Analysis GUI with all buttons and options for APN Group from Network Tab

    @Test
    public void causeCodeAnalysisGUIWithAllButtonsandOptionsforAPN_Group_4_15_9() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    //Test case - 4.15.10 View the new Cause Code Analysis GUI with all buttons and options for SGSN-MME Group from Network Tab

    @Test
    public void causeCodeanalysisGUIwithallbuttonsandoptionsforSGSN_MME_Group_4_15_10() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	checkButtons();
    }

    /*Test case - 4.15.11 verify that the new Cause Code Analysis GUI doesn't launch
       * when there if no input selected on the tool bar, and displays an appropriate 
       * message for the same -- with new launch bar, we now populate the input text box and 
       * then blank it. We then check to ensure launch button returns to a disabled state*/
        
    @Test
    public void causeCodeAnalysisGUIDoesntLaunchifnoInputSelected_4_15_11() throws Exception {
    	//revised
    	checkLaunchButtonCannotBeClickedIfDimensionValueTextBoxIsPopulatedAndThenNulled(null, 
    			SeleniumConstants.APN, null , DataIntegrityStringConstants.CORE_PS, 
    			DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	
    }

    /*Test case- 4.15.12 Verify that the expand and collapse of the Reconfigure and Advanced Configure menus on the new Cause Code Analysis GUI
     *  are working and showing all the necessary check boxes.*/

    @Test
    public void causeCodeAnalysisGUIWithNewConfigureMenuandAllRadioButtonsandCheckBoxesareChecked_4_15_12()
            throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        waitForPageLoadingToComplete();
        assertTrue("Chart View Radio Button Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_CHART_VIEW_OPTION_BUTTON));
        assertTrue("Grid View Radio Button Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_GRID_VIEW_OPTION_BUTTON));
        assertTrue("Select All Checkbox Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_DISABLED));
        logger.log(Level.INFO, DataIntegrityStringConstants.CONFIGURE_MENU);

    }

    
    //BELOW TEST IS NOT VALID SO IS NOT RUN AS PART OF THIS SUITE
    /*Test case- 4.15.13 Verify that in the Reconfigure menu on the new Cause Code Analysis GUI you have two check boxes as All Cause Codes and
     * Traditional Grid View and only one can be checked and un-checked at a time.*/
    /*@Test
    public void onlyOnecCanbeCheckedandUncheckedataTime_4_15_13() throws Exception {

        networkTab.openTab();
        final String searchValue = reservedDataHelper.getCommonReservedData(CommonDataType.APN_GROUP);
        networkTab.setSearchType(NetworkType.APN_GROUP);
        openCauseCodeAnalysisWindow(searchValue, true);
        waitForPageLoadingToComplete();
        selenium.click(CHART_VIEW);
        logger.log(Level.INFO, DataIntegrityConstants.CHECKED_UNCHECKED);
    }*/

    /*Test case- 4.15.14 Verify that in the Reconfigure menu on the new Cause Code Analysis GUI when you check the All Cause Codes
    and the check boxes for theCC types are automatically checked in the Advanced Reconfigure menu.
    */
	@Test
    public void checkBoxesfortheCCTypesaAreAutomaticallyCheckedintheAdvancedReconfigureMenu_4_15_14() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.CAUSE_CODE_ANALYSIS_CHART_VIEW_OPTION_BUTTON);
        selenium.click(Xpathconstants.CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_ENABLED);
        
        int numberOfMatchingXPaths = selenium.getXpathCount("//div[@id='responseHolder']/div[@class='x-panel-bwrap']/div[@class='x-panel-body x-panel-body-noheader wizard-content-body']/span/input[@checked='']").intValue();
        
        if (numberOfMatchingXPaths>0){
            for (int i=1;i<(numberOfMatchingXPaths+1);i++) {       
                assertTrue("All the CC types are not checked automatically by checking Select All check box",
                        selenium.isElementPresent("//div[@id='responseHolder']/div[@class='x-panel-bwrap']/div[@class='x-panel-body x-panel-body-noheader wizard-content-body']/span[" + i + "]/input[@checked='']"));
            }	
        	
        }else{
        		assertTrue("All the CC types are not checked automatically by checking Select All check box, or there is no data",
        				numberOfMatchingXPaths>0);	
        }
        logger.log(Level.INFO, DataIntegrityStringConstants.AUTOMATICALLY_CHECKED);

    }

    /*Test case- 4.15.16 Verify that the update button is working for the selected/All Cause Codes and subsequently draws a 
    Pie Chart launched for APN/Controller/SGSN/Access Area.
    */
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewCauseCodes_4_15_16_APN() throws Exception {
    	// revised 
    	workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_APN);
        
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewCauseCodes_4_15_16_Controller() throws Exception {
    	// revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_CONTROLLER);   
    }  

    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewCauseCodes_4_15_16_SGSN_MME() throws Exception {
    	// revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_SGSN);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewCauseCodes_4_15_16_Access_Area() throws Exception {
    	// revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_ACESS_AREA);
    }

    /*Test case- Verify that the update button is working for the selected/All Cause Codes and subsequently draws a 
    Pie Chart launched for APN/Controller/SGSN/Access Area Groups.
    */

    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewSubsequentGroupCauseCodes_APNGroup() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_APN_GROUP);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewSubsequentGroupCauseCodes_ControllerGroup() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_CONTROLLER_GROUP);
    }  
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewSubsequentGroupCauseCodes_SGSNMMEGroup() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_SGSN_MME_GROUP);
    } 
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllChartViewSubsequentGroupCauseCodes_AccessAreaGroup() throws Exception {
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
    	pieChartPresentCheck();
        logger.log(Level.INFO, DataIntegrityStringConstants.PIE_CHART_ACCESS_AREA_GROUP);
    } 
        
        
    /*Test case- 4.15.17 Verify that the update button is working for the Traditional Grid View and subsequently launches a Cause Code
    Analysis window for APN/Controller/SGSN/Access Area.
    */

    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_APN() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_APN);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_Controller() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_CONTROLLER);
        
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_SGSN() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_SGSN);
    }

    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_AccessArea() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_ACCESS_AREA);
    }

    /*Test case: Verify that the update button is working for the Traditional Grid View and subsequently launches a Cause Code
    Analysis window for APN/Controller/SGSN/Access Area Groups.
    */
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_APN_Group() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_APN_GROUP);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_Controller_Group() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.CONTROLLER_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_CONTROLLER_GROUP);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_SGSN_MME_Group() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.SGSN_MME_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_SGSN_GROUP);
    }
    
    @Test
    public void launchButtonisWorkingfortheSelectedAllGridViewCauseCodes_4_15_17_Access_Area_Group() throws Exception { 
    	//revised
    	workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA_GROUP, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.CAUSE_CODE_ANALYSIS);
        openGridView();
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityStringConstants.GRID_VIEW_ACCESS_AREA_GROUP);
    }
    
    


    //*** PRIVATE METHODS***//

    private void openCauseCodeAnalysisWindow(final String searchValue, final boolean group) throws PopUpException,
            InterruptedException {
        networkTab.enterSearchValue(searchValue, group);
        Thread.sleep(2000);
        final List<NetworkTab.SubStartMenu> subMenu = new ArrayList<NetworkTab.SubStartMenu>(
                Arrays.asList(NetworkTab.SubStartMenu.CAUSE_CODE_ANALYSIS_CORE));
        networkTab.openSubMenusFromStartMenu(StartMenu.CAUSE_CODE_ANALYSIS, subMenu);
    }
    
    private void pieChartPresentCheck() throws PopUpException {
    	openChartView();
    	waitForPageLoadingToComplete();
    	assertTrue("High Chart not present", selenium.isElementPresent(SeleniumConstants.CAUSE_CODE_HIGH_CHART));
    }
    
    private void openChartView() {
    	assertFalse("No data available to test", selenium.isTextPresent("No cause codes found for selected time"));
    	assertTrue("Chart View Radio Button Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_CHART_VIEW_OPTION_BUTTON));
        selenium.click(Xpathconstants.CAUSE_CODE_ANALYSIS_CHART_VIEW_OPTION_BUTTON);
        assertTrue("Enabled Select All Checkbox Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_ENABLED));
        selenium.click(Xpathconstants.CAUSE_CODE_ANALYSIS_GRID_SELECT_ALL_CHECK_BOX_WHEN_ENABLED);
        assertTrue("Launch Button Not Found", selenium.isElementPresent(Xpathconstants.CAUSE_CODE_ANALYSIS_LAUNCH_BUTTON));
        selenium.click(Xpathconstants.CAUSE_CODE_ANALYSIS_LAUNCH_BUTTON);
        
    }

    private void openGridView() {
    	assertTrue("Launch Button Not Found", selenium.isElementPresent(LAUNCH_BUTTON));
    	selenium.click(LAUNCH_BUTTON);
        
    }

    private void checkMultipleViews() {
        selenium.click(MULTIPLE_VIEWS);
        assertTrue("Multiple Views Checkbox Not Found", selenium.isElementPresent(MULTIPLE_VIEWS));
    }

    private void checkButtons(){
        assertTrue("Time Settings Button Not Found", selenium.isElementPresent(TIME_SETTINGS_BUTTON));
        assertTrue("Refresh Button Not Found", selenium.isElementPresent(REFRESH_BUTTON));
        assertTrue("Legend Button Not Found", selenium.isElementPresent(LEGEND_BUTTON));
        assertTrue("Time Constraints Not Found", selenium.isElementPresent(TIME_RANGE_COMP));
        logger.log(Level.INFO, DataIntegrityStringConstants.ALL_BUTTONS_OPTIONS);
    }
    
    public void checkLaunchButtonCannotBeClickedIfDimensionValueTextBoxIsPopulatedAndThenNulled
    		(String time, String dimension, String dimensionValue,String windowOption,
            String categoryPanel) throws InterruptedException, PopUpException {
        selenium.waitForPageLoadingToComplete();
        if(time !=null){
            workspaceRC.selectTimeRange(time);
        }
        Thread.sleep(5000);
        workspaceRC.selectDimension(dimension);
        
        if(selenium.isVisible(DIMENSION_GROUP_TEXTFIELD_ID)){
        	workspaceRC.enterDimensionValueForGroups(dimensionValue);
        } else{
        	Thread.sleep(2000);
        	workspaceRC.enterDimensionValue(dimensionValue);
        }
        workspaceRC.enterWindowFilter(windowOption);
        workspaceRC.selectWindowType(categoryPanel, windowOption);
        Thread.sleep(10000);
        workspaceRC.selectDimension(dimension);
        workspaceRC.enterDimensionValue(null);
        assertTrue("Launch button is not disabled when there is a null input in the APN Name text box",selenium.isElementPresent("//button[@id='selenium_tag_launchWindowButton'][@type='button'][@disabled='']"));
        selenium.waitForPageLoadingToComplete();
     }
}
