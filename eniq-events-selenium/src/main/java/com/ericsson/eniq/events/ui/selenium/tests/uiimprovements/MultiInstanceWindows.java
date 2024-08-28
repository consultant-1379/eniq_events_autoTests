package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiInstanceWindows extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    private TerminalTab terminalTab;

    @Autowired
    private SubscriberTab subscriberTab;

    @Autowired
    private RankingsTab rankingsTab;

    @Autowired
    @Qualifier("apnRankings")
    private CommonWindow apnRankingsWindow;
    
    static Logger logger = Logger.getLogger(MultiInstanceWindows.class.getName());

    static final String MULTIPLE_VIEWS = "//div[@id='selenium_tag_MultiWinSelectComponent']/input";

    static final String MULTIVIEW_OK_BUTTON = "//div[(@id='selenium_tag_MultiWinWarningDialog')]//button[text()='Ok']";
    
   

    /*Test Case 4.13.1: Verify that the multiple check box on the toolbar in the Network, Terminal, Subscriber 
     * and Ranking tabs for Data*/

    @Test
    public void multipleCheckboxforNetwork_Terminal_subscriber_RankingTabs_4_13_1() throws Exception {
        networkTab.openTab();
        assertTrue(DataIntegrityStringConstants.VERIFY_CHECKBOX, selenium.isElementPresent(MULTIPLE_VIEWS));

        terminalTab.openTab();
        assertTrue(DataIntegrityStringConstants.VERIFY_CHECKBOX, selenium.isElementPresent(MULTIPLE_VIEWS));

        subscriberTab.openTab();
        assertTrue(DataIntegrityStringConstants.VERIFY_CHECKBOX, selenium.isElementPresent(MULTIPLE_VIEWS));

        rankingsTab.openTab();
        assertTrue(DataIntegrityStringConstants.VERIFY_CHECKBOX, selenium.isElementPresent(MULTIPLE_VIEWS));
       

    }

    /*Test Case 4.13.2: Verify that the multiple check box on the toolbar in the Network, Terminal, Subscriber and Ranking tabs 
     * is unchecked by default for Data*/

    @Test
    public void multipleCheckboxisUncheckedbyDefaultforData_4_13_2() throws Exception {
        networkTab.openTab();
        boolean checkBox = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
        assertFalse(DataIntegrityStringConstants.CHECKBOX_UNCHECKED_DEFAULT, checkBox);

        terminalTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_UNCHECKED_DEFAULT, checkBox);

        subscriberTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_UNCHECKED_DEFAULT, checkBox);

        rankingsTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_UNCHECKED_DEFAULT, checkBox);
       

    }

    /*Test Case 4.13.3: Verify that if any one of the multiple check box on the toolbar in the Network, Terminal, Subscriber and Ranking 
     *tabs is checked, the change reflects on the other three tabs for data*/

    @Test
    public void multipleCheckboxisCheckedforNetworkTabReflectsontheOtherThreeTabsforData_4_13_3() throws Exception {
        networkTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        boolean checkBox = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
       
        
        terminalTab.openTab();
        assertTrue(DataIntegrityStringConstants.CHECKBOX_NOT_CHECKED, checkBox);

        subscriberTab.openTab();
        assertTrue(DataIntegrityStringConstants.CHECKBOX_NOT_CHECKED, checkBox);

        rankingsTab.openTab();
        assertTrue(DataIntegrityStringConstants.CHECKBOX_NOT_CHECKED, checkBox);
        
    }

    /*Test Case 4.13.4: Verify that if any one of the multiple check box on the toolbar in the Network, Terminal, Subscriber 
      and Ranking tabs is unchecked, the change reflects on the other three tabs for data */

    @Test
    public void multipleCheckboxisUncheckedforNetworkTabReflectsontheOtherThreeTabsforData_4_13_4() throws Exception {
        networkTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        boolean checkBox_checked = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue( checkBox_checked);
        
        terminalTab.openTab();
        assertTrue( checkBox_checked);

        subscriberTab.openTab();
        assertTrue( checkBox_checked);

        rankingsTab.openTab();
        assertTrue(checkBox_checked);
        
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
       
        boolean checkBox_unChecked = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
        terminalTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_CHECKED, checkBox_unChecked);

        subscriberTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_CHECKED, checkBox_unChecked);

        rankingsTab.openTab();
        assertFalse(DataIntegrityStringConstants.CHECKBOX_CHECKED, checkBox_unChecked);
       

    }

    /*Test Case 4.13.5: Verify that checking the Multiple check box in Network tab,Terminal tab, Subscriber tab and Rankings tab
      when any SEARCHABLE ONLY windows (eg: EA screen,etc..) are open, pops out a  Single Instance to Multiple instance Change 
      Confirmation window saying There are search field dependant windows open. These windows will be closed if you change mode.
      Are you sure? for Data.*/

    @Test
    public void checkingtheCheckboxPopsoutaSingleInstancetoMultipleInstanceChangeConfirmationInNetworkTab_4_13_5()
            throws Exception {
     
        final String APN_SEARCH_VALUE = "blackberry.net";
        networkTab.openEventAnalysisWindow(NetworkType.APN, true, APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
      
          
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
      
        
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        Thread.sleep(5000);
        //apnRankingsWindow.setTimeRange(TimeRange.THIRTY_MINUTES);
        apnRankingsWindow.clickTableCell(0, "APN");
        
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
    }

    
   
    /*Test Case 4.13.6: Verify that unchecking the Multiple check box in Network tab, Terminal tab, Subscriber tab and Rankings tab
     *Confirmation when any SEARCHABLE ONLY windows (eg: EA screen,etc..) are open, pops out a  Multiple Instance to single 
     *instance Change window saying There are search field dependant windows open. These windows will be closed if you
     *change mode. Are you sure? for Data.*/

    @Test
    public void uncheckingtheCheckboxPopsoutaMultipleInstancetoSingleInstanceChangeConfirmationForNetworkTab_4_13_6()
            throws Exception {
        //selenium.setSpeed("2000");
        networkTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, true, DataIntegrityStringConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        subscriberTab.openTab();
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        Thread.sleep(5000);
        //apnRankingsWindow.setTimeRange(TimeRange.ONE_WEEK);
        apnRankingsWindow.clickTableCell(0, "APN");
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_WINDOW,
                selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
    }

   

    /*Test Case 4.13.7: Verify that checking the Multiple check box in Network tab when any SEARCHABLE ONLY windows (eg: EA screen,etc..) 
     *  are open, pops out a  Single Instance to Multiple instance Change Confirmation window and on
     *  hitting OK, it indeed closes all the searchable only opened windows for Data.	
     */

    @Test
    public void changeConfirmationWindowonHittingOkClosesSearchableOnlyWindowsForNetworkTab_4_13_7() throws Exception {
        networkTab.openTab();
        networkTab.openEventAnalysisWindow(NetworkType.APN, true, DataIntegrityStringConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse(DataIntegrityStringConstants.SEARCHABLE_OPENED_WINDOWS, selenium.isElementPresent(Xpathconstants.NETWORK_EVENT_ANALYSIS));
        
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse(DataIntegrityStringConstants.SEARCHABLE_OPENED_WINDOWS, selenium.isElementPresent(Xpathconstants.TERMINAL_EVENT_ANALYSIS));
        
        subscriberTab.openTab();
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse(DataIntegrityStringConstants.SEARCHABLE_OPENED_WINDOWS, selenium.isElementPresent(Xpathconstants.TERMINAL_EVENT_ANALYSIS));
        
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        waitForPageLoadingToComplete();
        //apnRankingsWindow.setTimeRange(TimeRange.ONE_WEEK);
        apnRankingsWindow.clickTableCell(0, "APN");
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        Thread.sleep(2000);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse(DataIntegrityStringConstants.SEARCHABLE_OPENED_WINDOWS, selenium.isElementPresent(Xpathconstants.TERMINAL_EVENT_ANALYSIS));
    }

    

    /*Test Case 4.13.8: Verify that unchecking the Multiple check box in Network tab when any SEARCHABLE ONLY 
    windows (eg: EA screen,etc..) are open, pops out a  Multiple Instance to Single instance Change Confirmation window and on hitting OK, 
    it indeed closes all the searchable only opened windows for Data*/

    @Test
    public void changeConfirmationWindowonHittingOkClosesSearchableOnlyWindowsForNerworkTab_4_13_8() throws Exception {
        networkTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, true, DataIntegrityStringConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_NETWORK_EVENT_ANALYSIS']"));
        
       
        
        
        
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        logger.log(Level.INFO, DataIntegrityStringConstants.OK_BUTTON);
        
        
        
        subscriberTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_NETWORK_EVENT_ANALYSIS']"));
        
        rankingsTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        waitForPageLoadingToComplete();
        //apnRankingsWindow.setTimeRange(TimeRange.ONE_WEEK);
        apnRankingsWindow.clickTableCell(0, "APN");
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_NETWORK_EVENT_ANALYSIS']"));
   
    }

    

    /*Test Case 4.13.9: Verify that checking the Multiple check box in Network tab when any SEARCHABLE ONLY windows
     *  (eg: EA screen,etc..) are open, pops out a  Single Instance to Multiple instance Change Confirmation window and on hitting CANCEL, 
     *  it unchecks the Multiple check box and the confirmation window is closed automatically for Data. */

    @Test
    public void checkingtheCheckboxinNetworkonHittingCancelitUncheckstheMultipleCheckboxAutomaticallyforData_4_13_9()
            throws Exception {

        networkTab.openEventAnalysisWindow(NetworkType.APN, true, DataIntegrityStringConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
       
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        boolean checkBox = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
        assertFalse("On hitting Cancel it did not uncheck the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
        
        
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        assertFalse("On hitting Cancel it did not uncheck the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
       
        
        subscriberTab.openTab();
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        assertFalse("On hitting Cancel it did not uncheck the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
        
        rankingsTab.openTab();
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        waitForPageLoadingToComplete();
       //apnRankingsWindow.setTimeRange(TimeRange.ONE_WEEK);
        apnRankingsWindow.clickTableCell(0, "APN");
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.SINGLE_TO_MULTIPLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
       
        assertFalse("On hitting Cancel it did not uncheck the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
   
    }

    /*Test Case 4.13.10: Verify that unchecking the Multiple check box in Network tab when any SEARCHABLE ONLY
     *  windows (eg: EA screen,etc..) are open, pops out a  Multiple Instance to Single instance Change Confirmation window and on hitting CANCEL,
     *   it checks the Multiple check box and the confirmation window is closed automatically for Data.   */

    @Test
    public void uncheckingtheCheckboxinNetworkonHittingCancelitCheckstheMultipleCheckboxAutomaticallyforData_4_13_10()
            throws Exception {

        
        networkTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, true, DataIntegrityStringConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
       
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        boolean checkBox = selenium.isChecked(Xpathconstants.MULTIPLE_VIEWS);
        
        assertTrue("On hitting Cancel it did not check the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        
        
        
        selenium.click(MULTIPLE_VIEWS);
        openEventAnalysisWindow(TerminalType.TERMINAL, false, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MAKE), reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.TERMINAL_MODEL),reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A, ImsiSpecificDataType.TAC));
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
                assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
                selenium.click(Xpathconstants.MULTIPLE_VIEWS);
                selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);

        
       
        subscriberTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        subscriberTab.openEventAnalysisWindow(ImsiMenu.IMSI, true, DataIntegrityStringConstants.IMSI_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        
        assertTrue("On hitting Cancel it did not check the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
        
        
        
        rankingsTab.openTab();
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        rankingsTab.openSubStartMenu(RankingsTab.StartMenu.EVENT_RANKING, RankingsTab.SubStartMenu.EVENT_RANKING_APN);
        waitForPageLoadingToComplete();
       // apnRankingsWindow.setTimeRange(TimeRange.ONE_WEEK);
        apnRankingsWindow.clickTableCell(0, "APN");
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        assertTrue(selenium.isTextPresent(DataIntegrityStringConstants.MULTIPLE_TO_SINGLE_INSTANCE));
        selenium.click(Xpathconstants.MULTIVIEW_CANCEL_BUTTON);
        assertTrue("On hitting Cancel it did not check the multiple check box", checkBox);
        assertFalse("Confirmation window is not closed", selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']"));
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        selenium.click(Xpathconstants.MULTIVIEW_OK_BUTTON);
    }

    /*Test Case 4.13.13 : Verify that on checking the Multiple check box in Network, Terminal, Subscriber and Ranking tabs, Multiple windows 
     * which are SEARCH Field Dependant can be launched for Data*/

   /* @Test
    public void multipleViewsWorkforEA_CC_EV_DV_4_13_13() throws Exception {
        selenium.setSpeed("2000");
        networkTab.openTab();
       // final String searchValue = DataIntegrityConstants.APN_SEARCH_VALUE;
        selenium.click(Xpathconstants.MULTIPLE_VIEWS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, false, FailureReasonStringConstants.APN_SEARCH_VALUE);
       // networkTab.setSearchType(NetworkType.APN);
        //networkTab.enterSearchValue(searchValue, false);
       // pause(5000);
      //  String searchSubmitButtonXPath = Xpathconstants.NETWORK_SUBMIT_BUTTON;
       // String searchFieldInputXPath = Xpathconstants.SEARCHFIELD_INPUT_XPATH;
       // selenium.keyDown(searchFieldInputXPath, "\\13");
       // selenium.keyUp(searchFieldInputXPath, "\\13");
      //  selenium.click(searchSubmitButtonXPath);
        waitForPageLoadingToComplete();
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        logger.log(Level.INFO, FailureReasonStringConstants.EVENT_ANALYSIS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, false, FailureReasonStringConstants.APN_SEARCH_VALUE_1);
        //networkTab.openStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS);
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, FailureReasonStringConstants.CAUSE_CODE_ANALYSIS);
        networkTab.openEventAnalysisWindow(NetworkType.APN, false, FailureReasonStringConstants.APN_SEARCH_VALUE_2);
        //networkTab.openStartMenu(NetworkTab.StartMenu.EVENT_VOLUME);
        waitForPageLoadingToComplete();
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        logger.log(Level.INFO, FailureReasonStringConstants.EVENT_VOLUME);
        networkTab.openEventAnalysisWindow(NetworkType.APN, false, FailureReasonStringConstants.APN_SEARCH_VALUE_3);
        //networkTab.openStartMenu(NetworkTab.StartMenu.DATA_VOLUME);
        waitForPageLoadingToComplete();
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        logger.log(Level.INFO, FailureReasonStringConstants.DATA_VOLUME);
        networkTab.openEventAnalysisWindow(NetworkType.APN, false, FailureReasonStringConstants.APN_SEARCH_VALUE_4);
       // networkTab.openStartMenu(NetworkTab.StartMenu.NETWORK_EVENT_VOLUME);
        waitForPageLoadingToComplete();
        assertFalse("Searchable opened window for data is not closed", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        logger.log(Level.INFO, FailureReasonStringConstants.NETWORK_EVENT_VOLUME);
        networkTab.openStartMenu(NetworkTab.StartMenu.NETWORK_DATA_VOLUME);
        waitForPageLoadingToComplete();
        logger.log(Level.INFO, DataIntegrityConstants.NETWORK_DATA_VOLUME);

    }
    */
    
    
    
    
    //******************************************* PRIVATE METHODS **************************************************//
    
    
    
    private void openEventAnalysisWindow(final TerminalType type, final boolean submitButton, final String... values)
            throws PopUpException, InterruptedException {
        
        terminalTab.openEventAnalysisWindow(type, submitButton, values);

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            assertTrue("Can't open " + values[0] + " - Terminal Event Analysis",
                    selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        } else {
            assertTrue("Can't open Terminal Event Analysis", selenium.isElementPresent("//div[@id='selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS']"));
        }
        waitForPageLoadingToComplete();
    }
    
    
    
    private void APN_SEARCH_VALUES(){
        
       final List<String> APN_SEARCH_VALUES = new ArrayList<String>(Arrays.asList("blackberry.net", "blackberry.1x.bell.ca", "blackberry.net.mnc000.mcc460", 
               "blackberry.net.mnc001.mcc208", "blackberry.net.mnc001.mcc214"));
        
    }
       
       private void drillDownOnAPN_RANKING_EVENTS() throws InterruptedException, PopUpException{
           /*String stDate = "2011,01,05";
           String edDate = "2012,02,20";
           
           final String[] startDateArray = stDate.split(",");
           final String[] endDateArray = edDate.split(",");
           
           final Date sDate = apnRankingsWindow.getDate(Integer.parseInt(startDateArray[0]), Integer.parseInt(startDateArray[1]), Integer.parseInt(startDateArray[2]));
           final Date eDate = apnRankingsWindow.getDate(Integer.parseInt(endDateArray[0]), Integer.parseInt(endDateArray[1]), Integer.parseInt(endDateArray[2]));
           apnRankingsWindow.setTimeAndDateRange(sDate, TimeCandidates.AM_00_00, eDate, TimeCandidates.AM_00_00);*/
           selenium.click("//div[@id='NETWORK_APN_RANKING']//span[text()='cmdm']");
           Thread.sleep(5000);
           
           
           
       }
           
           
           
           
       
        
        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}


















