package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.PLMNGroupEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.TerminalAndIMSIGroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JirasAndTRs extends EniqEventsUIBaseSeleniumTest{
    
    @Autowired
    private NetworkTab networkTab;
    
    @Autowired
    private SubscriberTab subscriberTab;
    
    @Autowired
    private TerminalTab terminalTab;
    
    @Autowired
    private RankingsTab rankingsTab;
    
    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysis;
    
    @Autowired
    @Qualifier("terminalAnalysis")
    private CommonWindow terminalAnalysis;
    
    @Autowired
    @Qualifier("networkQosStatisticsAnalysis")
    private CommonWindow networkQosStatisticsAnalysis;
    
    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;
    
    @Autowired
    @Qualifier("subscriberEventAnalysis")
    private CommonWindow subscriberEventAnalysis;
    
    @Autowired
    @Qualifier("groupMgtWindow")
    private GroupManagementWindow groupMgtWindow;

    @Autowired
    @Qualifier("groupMgtEditWindow")
    private GroupManagementEditWindow groupMgtEditWindow;

    @Autowired
    @Qualifier("plmnEditWindow")
    private PLMNGroupEditWindow plmnEditWindow;

    @Autowired
    @Qualifier("groupTerAndIMSIWindow")
    private TerminalAndIMSIGroupManagementWindow groupTerAndIMSIWindow;

    @Autowired
    private WorkspaceRC workspaceRC;

    private final List<SubscriberTab.SubStartMenu> subMenuSubscriberOverviewcore = Arrays.asList(
            SubscriberTab.SubStartMenu.SUBSCRIBER_OVERVIEW);
    
    private final List<TerminalTab.SubStartMenu> subMenuTermianlGroupAnalysisCore = Arrays.asList(TerminalTab.SubStartMenu.GROUP_ANALYSIS);
    
     private static final String PAGING_OPTIONS = "//div[@id='selenium_tag_BaseWindow_NETWORK_SOURCE_CELL_RANKING_RAN_WCDMA_HFA']" +
            "//div[2]//div[3]/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/em/button[@aria-disabled='true']";
     
     static final String MANUFACTURER = "Manufacturer";

     static final String MODEL = "Model";
     
     static final String TAC = "TAC";
     
     static final String typesComboInputXPath = "//div[@id='TERMINAL_TAB']//input[@id='selenium_tag_typesCombo-input']";
     
     static final String searchFieldInputXPath = "//div[@id='TERMINAL_TAB']//input[@id='selenium_tag_searchField-input']";
     
     //emosjil
     //GROUP_SAVED_TO_DATABASE = "//div[@class='dialogBox']//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group Saved to Database']";
     static final String GROUP_SAVED_TO_DATABASE;
     
     private static final String LOADING_GROUPS = "Loading Groups...";
     
     private static final String SAVING_GROUP = "Saving Group...";
     
     static ReservedDataHelperReplacement reservedDataHelperReplacement;
     
     boolean isCorrectInput = false;

     static {
    	 final double version = PropertyReader.getInstance().getEniqRootVersion();
    	 if(version >= 13.0){
    		 GROUP_SAVED_TO_DATABASE = "//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group Saved to Database']";
    	 }else{
    		 GROUP_SAVED_TO_DATABASE = "//div[@class='dialogBox']//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group Saved to Database']";
    	 }
     }
   //FIXED
    @Test
    public void unexpectedErrorOnTheCauseGridEUI_1616() throws InterruptedException, PopUpException{
//        String searchValue = "MME1";
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME,"Core PS", "Cause Code Analysis");
        networkCauseCodeAnalysis.openGridView();
        selenium.waitForPageLoadingToComplete();
        
        if(selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS']")){
            selenium.click("//div[@id='selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS']//div//table//tbody/tr/td/div[@id='btnCC']/img");
            selenium.waitForPageLoadingToComplete();
            assertFalse("Caught Unexpected error", selenium.isTextPresent("Uexpected error!"));
        }

      if(selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS']")){
          selenium.click("//div[@id='selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS']//div//table//tbody/tr/td/div[@id='btnSCC']/img");
          selenium.waitForPageLoadingToComplete();
          assertFalse("Caught Unexpected error", selenium.isTextPresent("Uexpected error!"));
      }
    }

    //TODO: No data available for SGSN_MME Group so can't test this one.
    @Test
    public void unexpectedErrorOccurredOnSubscriberOverviewEUI_1606() throws PopUpException, InterruptedException{
        subscriberTab.openSubscriberOverviewWindowSubMenu(subMenuSubscriberOverviewcore, ImsiMenu.IMSI_GROUP,
                "DG_GroupNameIMSI_1");
        assertFalse("Caught Unexpected error", selenium.isTextPresent("Uexpected error!"));
    }
    
    //TODO: QOS stats not found for Terminal group in new UI; Still open question to Grainne?
    @Test
    public void errorOccurredWhenTriedToLaunchQosStaticticsForTerminalEUI_1617() throws PopUpException, InterruptedException{
       terminalTab.openQosStatisticsWindow(TerminalType.TERMINAL_GROUP, true, "DG_GroupNameTAC_1");
       assertFalse("Caught Unexpected error", selenium.isTextPresent("Uexpected error!"));
    }
    
    //FIXED
    /*Test case EUI-1615: Window title incorrect on drilldown of QOS statistics failures*/
    @Test
    public void windowTitleIncorrectOnDrillDownOfQosStatisticsFailuresEUI_1615() throws InterruptedException, PopUpException, NoDataException{
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME,"Core PS", "QOS Statistics");
        //emosjil commented out next line
        //networkQosStatisticsAnalysis.getTableHeaders();
        
        networkQosStatisticsAnalysis.clickTableCell(2, GuiStringConstants.FAILURES);
        String windowTitle = networkQosStatisticsAnalysis.getWindowHeaderLabel();
        
        //emosjil: substring called wrongly
        //assertTrue("Window Title incorrect",windowTitle.substring(18).equalsIgnoreCase("Failed Event Analysis"));
        
        assertTrue("Window Title incorrect",windowTitle.substring(0,21).equalsIgnoreCase("Failed Event Analysis"));
    }

    /*Test case EUI-1614: Window title incorrect on first launch ofQOS statistics*/
    //TODO: Roman said, its a reported bug. Open question to Grainne?
    @Test
    public void windowTitleIncorrectOnFirstLaunchOfQosStatisticsEUI_1614() throws InterruptedException, PopUpException{
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME,"Core PS", "QOS Statistics");
        final double version = PropertyReader.getInstance().getEniqRootVersion();
        
        if(version >= 13.0){
        	assertTrue("Title incorrect", selenium.isTextPresent("MME1 - SGSN-MME - Core"));
        }else{
        	assertTrue("Title incorrect", selenium.isTextPresent("MME1 - SGSN-MME Group - QOS Statistics"));
        }
    }

    /*Test case EUI-1604: Unexpected error occurred when tried to drill down on failures bar in group analysis*/
    //TODO: Can't find "Group analysis" from Terminal tab in new workspace GUI. Roman said, its a known bug!
    @Test
    public void errorOccuredToDrillDownOnFailuredBarInGroupAnalysisEUI_1604() throws PopUpException, InterruptedException {
        workspaceRC.launchWindow(SeleniumConstants.TERMINAL_GROUP,"Core PS", "QOS Statistics");
        terminalTab.openTab();
        terminalTab.openSubMenusFromStartMenu(TerminalTab.StartMenu.GROUP_ANALYSIS, subMenuTermianlGroupAnalysisCore);
        selenium.click("//div[@id='timeRangeComp']/img");
        int comboBoxSize = (Integer) selenium.getXpathCount("//div[contains(@class,'x-combo-list x-ignore x-component x-border')]/div/div");
        selenium.click("//div[@id='timeRangeComp']/img");
        for(int i=1; i<=comboBoxSize; i++){
            selenium.click("//div[@id='timeRangeComp']/img");
            
            selenium.mouseDown("//div[contains(@class,'x-combo-list x-ignore x-component x-border')]/div/div["+ i +"]");
            selenium.waitForPageLoadingToComplete();
            selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_GA_MOST_POPULAR_SUMMARY']//div//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']");
            
           if( selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_GA_MOST_POPULAR_SUMMARY']//div//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']")){
                   selenium.mouseOver("//div[@id='selenium_tag_BaseWindow_TERMINAL_GA_MOST_POPULAR_SUMMARY']//div//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][1]");
                   selenium.click("//div[@id='selenium_tag_BaseWindow_TERMINAL_GA_MOST_POPULAR_SUMMARY']//div//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect'][1]");
                   selenium.waitForPageLoadingToComplete();
                   assertFalse("Caught Unexpected error", selenium.isTextPresent("Uexpected error!"));
                   
           }else{
               
               assertFalse("No Data", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_GA_MOST_POPULAR_SUMMARY']//div//node()[contains(@class,'highcharts-tracker')]//node()[name()='rect']"));
           }
        }
    }
    
    /*Test case EUI-1582: Wrong window titles for windows in Network, Terminal and Subscriber tabs that should have search input from user*/
    //GSM feature issue suppose to be implemented in 13.0 release
    /*@Test 
    public void wrongWindowTitlesFor_Network_Terminal_Subscriber_EUI_1582() throws InterruptedException, PopUpException{

        networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER_GROUP, SubStartMenu.NETWORK_RAN_GSM_EVENT_ANALYSIS_SUMMARY, true, "DG_GroupNameRATVENDHIER3_10");
        String eventAnalysisTitle = "DG_GroupNameRATVENDHIER3_10 - Controller Group-GSM Call Failures Event Analysis Summary";
    }*/
        
        /*Test case EEUNM-1319: WCDMA HFA Ranking, Paging options enabled*/
        @Test
        //TODO: Work away with Core CS/PS network etc... A real TODO.. :-)
        public void WCDMA_HFA_RankingPagingOptionsEnabled_EEUNM_1319() throws PopUpException{
            rankingsTab.openTab();
            final List<RankingsTab.SubStartMenu> subMenus = new ArrayList<RankingsTab.SubStartMenu>(Arrays.asList(
                    RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_WCDMAHFA,
                    RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_RAN, RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA_HFA));
            rankingsTab.openSubMenusFromStartMenu(RankingsTab.StartMenu.EVENT_RANKING, subMenus);
            assertTrue("Paging Options are Enabled", selenium.isElementPresent(PAGING_OPTIONS));
        }

        /*EUI-1449: Groups with same name in different cases(upper/lower/camel) can be created using Group Management, and this causes the message to popup saying 'No Data'
         * for all groups except for one group. Acceptance criteria Case-Insensitive*/
        // FIXED
        @Test
        public void createdGroupsShouldBeCaseInsensitve_EUI_1449() throws PopUpException, InterruptedException{
            waitForPageLoadingToComplete();
            pause(5000);
            groupMgtWindow.launchGroupType(GuiStringConstants.IMSI);
            groupMgtWindow.waitForLoading(LOADING_GROUPS);
            groupMgtWindow.clickNewButton();
            String groupName = createRandomGroupName();
            String groupItem = createRandomGroupItem();
            groupTerAndIMSIWindow.enterGroupName(groupName);
            groupTerAndIMSIWindow.enterGroupItem(groupItem);
            groupTerAndIMSIWindow.clickAddButton();
            groupMgtWindow.clickSaveButton();
            groupMgtWindow.waitForLoading(SAVING_GROUP);

            assertTrue("New Group cannot be created in the Group Management for the Selected Group Type",
                    selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
            
            groupMgtWindow.closeWindow();
            pause(3000);
            groupMgtWindow.clickNewButton();
            groupTerAndIMSIWindow.enterGroupName(groupName);
            groupTerAndIMSIWindow.enterGroupItem("548795478952247");
            groupTerAndIMSIWindow.clickAddButton();
            groupMgtWindow.clickSaveButton();
            
            //emosjil: changed assertTrue to assertFalse
            assertFalse("Groups are created irrespective of Case-insensitive",
                    selenium.isElementPresent("//div[@class='dialogBox']//div[@class='dialogMiddleCenterInner dialogContent']" +
                    		"//div[text()='This group name already exists, do you want to modify it?']"));
        }
        
        
        
        /*EUI-1209: New line character in the any group element causes exception in liveload and group management*/
        //FIXED
        @Test
        public void lineCharacterInAnyGroupElementCausesException_EUI_1209() throws PopUpException, InterruptedException{
            waitForPageLoadingToComplete();
            pause(5000);
            groupMgtWindow.launchGroupType(GuiStringConstants.APN);
            groupMgtWindow.waitForLoading(LOADING_GROUPS);
            groupMgtWindow.clickNewButton();
            groupMgtWindow.waitForLoading("Loading APNs...");
            groupMgtEditWindow.selectAvailableItem(1, GuiStringConstants.APN);
            groupMgtEditWindow.clickAddButton();
            groupMgtEditWindow.enterGroupName("test\\  aaaa// n");
            groupMgtEditWindow.clickSave();
            
            //emosjil: changed assertTrue to assertFalse
            assertFalse("Groups are created with illegal character",
                    selenium.isElementPresent("//div[@class='dialogBox']//div[@class='dialogMiddleCenterInner dialogContent']//div[text()]"));
        }
        
        
        /*EEUNM-1324: SGSN-MME Event Analysis shows wrong name*/
        //FIXED
        @Test 
        public void SGSN_MME_EventAnalysisShowsWrongName_EEUNM_1324() throws InterruptedException, PopUpException, NoDataException{
            workspaceRC.launchWindow(SeleniumConstants.IMSI,"310410000014509","Core PS", "Core Event Trace");
            // subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, "310410000014509");
            waitForPageLoadingToComplete();
            // This is 12.1 TR, so the time button x-path differs from 12.2 time button.
            // Possibility of failing test case of different time button path and No data as well for 12.1 on rgression testing.
            subscriberEventAnalysis.setTimeRange(TimeRange.ONE_WEEK);
            subscriberEventAnalysis.clickTableCell(1, "SGSN-MME");
            waitForPageLoadingToComplete();
            String launchedEventAnalysisTitle = selenium.getText("//div[@id='selenium_tag_baseWindow']/div[1]/div/div/div/span");
            assertTrue("SGSN-MME Event analysis shows wrong name", launchedEventAnalysisTitle.equals("MME1 - SGSN-MME Event Analysis"));
        }


        //************************************************** PRIVATE METHODS *****************************************************//

        private void correctInputValue() throws InterruptedException, PopUpException{
            workspaceRC.launchWindow(SeleniumConstants.TERMINAL,"Core PS", "QOS Statistics");
            terminalTab.openTab();
            terminalTab.setSearchType(TerminalType.TERMINAL);
            Thread.sleep(30000);

            selenium.mouseDown(typesComboInputXPath);
            selenium.mouseUp(typesComboInputXPath);
            selenium.keyDown(typesComboInputXPath, "\\40");
            selenium.keyUp(typesComboInputXPath, "\\40");

            assertTrue(selenium.isElementPresent("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']"));
//            Thread.sleep(10000);

            selenium.mouseOver("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']");
            selenium.mouseDown("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']");
            selenium.mouseUp("//div[@id='TERMINAL_TAB']");
         
//            Thread.sleep(2000);
//            System.out.println("One");
            
            Thread.sleep(5000);
            String attrID = selenium.getAttribute("//div[@id='TERMINAL_TAB']//div[contains(@id,'selenium_tag_searchField')]" + "@class");
//            System.out.println("Class: " + attrID);
            if(attrID.contains("disabled")){
//                System.out.println("disabled");
                selenium.click("//div[@id='TERMINAL_TAB']//div[@id='selenium_tag_launchButton']//img");
                Thread.sleep(10000);
                assertFalse("Terminal Event Analysis Launched for invalid terminal make", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
            } else {
//                System.out.println("enabled");
                selenium.keyDown(searchFieldInputXPath, "\\40");
                Thread.sleep(10000);
                assertTrue(selenium.isElementPresent("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']"));
             
//              Thread.sleep(3000);
              selenium.mouseOver("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']");
              selenium.mouseDown("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']");

                Thread.sleep(3000);
                selenium.click("//div[@id='TERMINAL_TAB']//div[@id='selenium_tag_launchButton']//img");
                waitForPageLoadingToComplete();

                assertTrue("Terminal Event Analysis not Launched for valid terminal make", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
                
              //  selenium.close();
                selenium.click("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']//div[@class=' x-panel-toolbar x-component']/table/tbody/tr/td[4]/div");
                ////div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']//div[@class=' x-panel-toolbar x-component']/table/tbody/tr/td[4]
            }
        }
        
        
        private void inCorrectInputValue() throws InterruptedException, PopUpException{
            terminalTab.openTab();
            terminalTab.setSearchType(TerminalType.TERMINAL);
            Thread.sleep(30000);

            selenium.mouseDown(typesComboInputXPath);
            selenium.mouseUp(typesComboInputXPath);
            selenium.keyDown(typesComboInputXPath, "\\40");
            selenium.keyUp(typesComboInputXPath, "\\40");

            assertTrue(selenium.isElementPresent("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']"));
//            Thread.sleep(10000);

            selenium.mouseOver("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']");
            selenium.mouseDown("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='3 Mobiles']");
            selenium.mouseUp("//div[@id='TERMINAL_TAB']");
            selenium.keyPressNative("35");
            selenium.keyPressNative("66");
         
//            Thread.sleep(2000);
//            System.out.println("One");
            
            Thread.sleep(5000);
            String attrID = selenium.getAttribute("//div[@id='TERMINAL_TAB']//div[contains(@id,'selenium_tag_searchField')]" + "@class");
//            System.out.println("Class: " + attrID);
            if(attrID.contains("disabled")){
//                System.out.println("disabled");
                selenium.click("//div[@id='TERMINAL_TAB']//div[@id='selenium_tag_launchButton']//img");
                Thread.sleep(10000);
                assertFalse("Terminal Event Analysis Launched for invalid terminal make", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
            } else {
//                System.out.println("enabled");
                selenium.keyDown(searchFieldInputXPath, "\\40");
                Thread.sleep(10000);
                assertTrue(selenium.isElementPresent("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']"));
//              Thread.sleep(3000);

              selenium.mouseOver("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']");
              selenium.mouseDown("//div[@class='x-liveload x-ignore x-component x-border ']//div[text()='OT401,91053550']");
                Thread.sleep(3000);
                selenium.click("//div[@id='TERMINAL_TAB']//div[@id='selenium_tag_launchButton']//img");
                waitForPageLoadingToComplete();

                assertTrue("Terminal Event Analysis not Launched for valid terminal make", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
                //selenium.close();
            }
        }
   
        private String createRandomGroupName() {
            String r = "ABCDEFGHI";
            Random ran = new Random();
            StringBuilder builder = new StringBuilder("Test_");
            for (int i = 0; i < 9; i++) {
                builder.append(r.charAt(ran.nextInt(9)));
            }
            return builder.toString();
        }

      
        private String createRandomGroupItem() {

            String r = "123456789";
            Random ran = new Random();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                builder.append(r.charAt(ran.nextInt(9)));
            }
            return builder.toString();
        }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
