package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.PLMNGroupEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.TerminalAndIMSIGroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

import static com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementConstants.*;

public class GroupManagementTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired @Qualifier("groupMgtWindow") private GroupManagementWindow groupMgtWindow;
    @Autowired @Qualifier("groupMgtEditWindow") private GroupManagementEditWindow groupMgtEditWindow;
    @Autowired @Qualifier("plmnEditWindow") private PLMNGroupEditWindow plmnEditWindow;
    @Autowired @Qualifier("groupTerAndIMSIWindow") private TerminalAndIMSIGroupManagementWindow groupTerAndIMSIWindow;

    static final String GROUP_MANAGEMENT_UI = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']";
    static final String DELETE_GROUP_UI = "//div[@id='selenium_tag_DELETE_GROUPS']";
    static final String IMPORT_GROUP_UI = "//div[@id='selenium_tag_IMPORT_GROUPS']";
    static final String GROUP_TYPE_ACCESS_AREA = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='Access Area']";
    static final String GROUP_TYPE_APN = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='APN']";
    static final String GROUP_TYPE_CONTROLLER = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='Controller']";
    static final String GROUP_TYPE_IMSI = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='IMSI']";
    static final String GROUP_TYPE_MSC = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='MSC']";
    static final String GROUP_TYPE_PLMN = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[text()='PLMN']";
    static final String GROUP_TYPE_SGSN = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='SGSN-MME']";
    static final String GROUP_TYPE_TERMINAL = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='Terminal']";
    static final String GROUP_TYPE_TRACKING_AREA = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class ='GKH1R2KFX'][text()='Tracking Area']";
    static final String VISSIBLE_VALUES = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[contains(@style,'overflow: visible')]/div[1][contains(@style,'')]/div";
    static final String GROUP_SAVED_TO_DATABASE = "//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group Saved to Database']";
    
    private static final String LOADING_GROUP_ELEMENTS = "Loading Group Elements...";
    private static final String LOADING_GROUPS = "Loading Groups...";
    private static final String LOADING_COUNTRIES = "Loading Countries...";
    private static final String LOADING_OPERATORS = "Loading Operators...";
    private static final String SAVING_GROUP = "Saving Group...";
    private static final String DELETEING_GROUP_ELEMENTS = "Deleting Group Element(s)";

    /**
     * Test case 4.1.1: Verify that the Group Management functionality is available for all users under the Options Menu
     * in the EE UI.
     */
    @Test public void groupManagementFunctionalityUnderTheOptionsMenu_4_1_1() throws InterruptedException, PopUpException {
        //Options Menu checked in workspace;
        waitForPageLoadingToComplete();
        selenium.click(OPTIONS_MENU);
        assertTrue("Group Management functionality is not available for Networktab", selenium.isElementPresent(GROUP_MANAGEMENT));
        selenium.click(OPTIONS_MENU);
    }

    /**
     * Test case 4.1.2: Verify that the Group Management functionality under the Options Menu has options to Group
     * Management, Upload Groups and Delete Groups
     */
    @Test public void optionsMenuHasGroupManagementImportGroupsDeleteGroups_4_1_2() throws PopUpException {
        waitForPageLoadingToComplete();
        selenium.click(OPTIONS_MENU);
        assertTrue("Options Menu did not have Group Management functionality ", selenium.isElementPresent(GROUP_MANAGEMENT));
        assertTrue("Options Menu did not have Import Group functionality ", selenium.isElementPresent(IMPORT_GROUPS));
        assertTrue("Options Menu did not have Delete Group functionality ", selenium.isElementPresent(DELETE_GROUPS));
        selenium.click(OPTIONS_MENU);
        pause(30000);
    }

    /**
     * Test case 4.1.3: Verify that appropriate UI launches for each of the options like Group Management, Import Groups
     * and Delete Groups.
     */
    @Test public void appropriateUiLaunchesForGroupManagementImportGroupsDeleteGroups_4_1_3() throws PopUpException {
    	//verified UI launches for group management, import and delete groups for network tab
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupManagementWindow();
        pause(1000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(GROUP_MANAGEMENT_UI));
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        pause(1000);
        groupMgtWindow.launchImportGroupsWindow();
        pause(1000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(IMPORT_GROUP_UI));
        groupMgtWindow.closeLaunchedUI_Window("IMPORT_GROUPS");
        pause(1000);
        groupMgtWindow.launchDeleteGroupsWindow();
        pause(1000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(DELETE_GROUP_UI));
        groupMgtWindow.closeLaunchedUI_Window("DELETE_GROUPS");
    }

    /**
     * Test case 4.1.5: Verify that the UI launched for Group Management, lists Group Types such as APN, Controller,
     * Access Area, SGSN, MSC, Tracking Area, Terminal and IMSI
     */
    @Test public void groupManagementListsGroupTypesApnControllerAccessAreaSgsnMscTrackingAreaTerminalImsi_4_1_5() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupManagementWindow();
        waitForLaunch(5000);
        pause(5000);
        selenium.click(TRIGGER_BUTTON);
        listsOfGroupTypes();
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
    }
    
    /**
     * Test case 4.1.7: Verify that in the Group Management, the lists items of the Group Types such as APN, Controller,
     * Access Area, SGSN, MSC, Tracking Area, Terminal and IMSI can be selected and the values are visible in the space
     * below for each of them
     */
    @Test public void selectedGroupTypeValuesAreVisibleInTheSpaceBelow_4_1_7() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        pause(5000);

        //Select Any Group Type from the Groupmanagement Window and checked for the values are vissible
        String[] groupTypes = new String[] { GuiStringConstants.APN, GuiStringConstants.ACCESS_AREA,
                GuiStringConstants.CONTROLLER, GuiStringConstants.IMSI, GuiStringConstants.SGSN,
                GuiStringConstants.PLMN, GuiStringConstants.TERMINAL };//No Data available forTrackin Area and MSC 
        for (String groupType : groupTypes) {
            pause(1500);
            groupMgtWindow.launchGroupType(groupType);
            groupMgtEditWindow.waitForLoading(LOADING_GROUPS);
            assertTrue("Selected Group Type values are not vissible", selenium.isElementPresent(VISSIBLE_VALUES));
            groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        }
    }

    /**
     * Test case 4.1.8: Verify that new groups can be created in Group Management, for the Group Types such as APN,
     * Controller, Access Area, SGSN, MSC and Tracking Area
     */
    @Test public void newGroupsCanBeCreatedInGroupManagement_4_1_8() throws PopUpException, InterruptedException {
        waitForPageLoadingToComplete();
        pause(5000);
        String[] groupTypes = new String []{GuiStringConstants.APN, GuiStringConstants.ACCESS_AREA,
                GuiStringConstants.CONTROLLER, GuiStringConstants.SGSN};//No Data for MSC and Tracking Area
        
        for(String groupType : groupTypes){
        	groupMgtWindow.launchGroupType(groupType);
        	groupMgtEditWindow.waitForLoading(LOADING_GROUPS);
        	groupMgtWindow.clickNewButton();
        	groupMgtWindow.waitForLoading(createGroupItemsLdMsg(groupType));

        	//After clicking button New select the available items for the group type

        	groupMgtEditWindow.selectAvailableItem(1, groupType);
        	groupMgtEditWindow.clickAddButton();
        	String groupName = createRandomGroupName();
   
        	groupMgtEditWindow.enterGroupName(groupName);
        	groupMgtEditWindow.clickSave();
        	groupMgtEditWindow.waitForLoading(SAVING_GROUP);
        	waitForLaunch(2000);

        	//check the new group is created or not
        	assertTrue("New Group cannot be created in the Group Management", selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        	waitForLaunch(2000);
        	groupMgtWindow.closeWindow();
        	
        	waitForLaunch(2000);
        	pause(4000);
        	assertTrue("New Group Name does not exist", groupMgtWindow.isGroupNameExist(groupName));
        	groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        }
    }

    /**
     * Test case 4.1.9: Verify that created/available groups can be edited in Group Management, for the Group Types such
     * as APN, Controller, Access Area, SGSN, MSC and Tracking Area.
     */
    @Test public void createdOrAvailableGroupsCanBeEditedInGroupManagement_4_1_9() throws PopUpException, InterruptedException {
        //Created or available groups are edited by using Edit Button
    	//selenium.setSpeed("500");
//        networkTab.openTab();
        waitForPageLoadingToComplete();
        pause(5000);
        String[] groupTypes = new String[] {GuiStringConstants.APN,GuiStringConstants.ACCESS_AREA,
                GuiStringConstants.SGSN};
        for(String groupType : groupTypes){
        	groupMgtWindow.launchGroupType(groupType);
        	groupMgtWindow.waitForLoading(LOADING_GROUPS);
        	String selectItem;
        	if(groupType.equals(GuiStringConstants.ACCESS_AREA)){
        		selectItem = "DG_ReserveAccessArea_group";
        		selenium.click("//*[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div[1]/div/div[contains(text(),'DG_ReserveAccessArea_group')]");
        	}else{
        		selectItem = groupMgtWindow.selectItem();
        	}
        	groupMgtWindow.clickEditButton();
        	groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        	groupMgtEditWindow.waitForLoading(createGroupItemsLdMsg(groupType));
        	groupMgtEditWindow.selectAvailableItem(1, groupType);
        	groupMgtEditWindow.clickAddButton();

        	List<String> editedItems = groupMgtEditWindow.getCurrentGroupItems();
        	groupMgtEditWindow.clickSave();
        	groupMgtEditWindow.waitForLoading(SAVING_GROUP);
        	waitForLaunch(5000);

        	//Edited group is saved
        	assertTrue(
        			"New Group cannot be created in the Group Management for the Selected Group Type",
        			selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        	Thread.sleep(2000);
        	groupMgtWindow.closeWindow();

        	//The same item is selected and checked for the edited actions is saved or not
        	groupMgtWindow.selectItem(selectItem);
        	groupMgtWindow.clickEditButton();
        	groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        	groupMgtWindow.waitForLoading(createGroupItemsLdMsg(groupType));

        	List<String> savedItems = groupMgtEditWindow.getCurrentGroupItems();
        	assertTrue("Items Not Matched", compareItems(editedItems, savedItems));
        	groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        }

        pause(2000);
        String groupTypeLabe = GuiStringConstants.CONTROLLER;
        groupMgtWindow.launchGroupType(groupTypeLabe);
        groupMgtEditWindow.waitForLoading(LOADING_GROUPS);
        groupMgtWindow.clickNewButton();
        groupMgtWindow.waitForLoading(createGroupItemsLdMsg(groupTypeLabe));

        //After clicking button New select the available items for the group type
        pause(5000);
        groupMgtEditWindow.selectAvailableItem(1, groupTypeLabe);
        groupMgtEditWindow.clickAddButton();
        String groupName = createRandomGroupName();

        groupMgtEditWindow.enterGroupName(groupName);
        groupMgtEditWindow.clickSave();
        groupMgtEditWindow.waitForLoading(SAVING_GROUP);

        //check the new group is created or not
        assertTrue(
                "New Group cannot be created in the Group Management",
                selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        waitForLaunch(2000);
        groupMgtWindow.closeWindow();
        waitForLaunch(2000);
        groupMgtWindow.selectItem(groupName);
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtEditWindow.waitForLoading(createGroupItemsLdMsg(groupTypeLabe));
        pause(5000);
        groupMgtEditWindow.selectAvailableItem(1, groupTypeLabe);
        groupMgtEditWindow.clickAddButton();
        List<String> editedItems1 = groupMgtEditWindow.getCurrentGroupItems();
        groupMgtEditWindow.clickSave();
        groupMgtEditWindow.waitForLoading(SAVING_GROUP);
        pause(5000);
        //Edited group is saved
        assertTrue(
                "New Group cannot be created in the Group Management for the Selected Group Type",
                selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        Thread.sleep(2000);
        groupMgtWindow.closeWindow();

        //The same item is selected and checked for the edited actions is saved or not
        groupMgtWindow.selectItem(groupName);
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtWindow.waitForLoading(createGroupItemsLdMsg(groupTypeLabe));
        List<String> savedItems1 = groupMgtEditWindow.getCurrentGroupItems();
        assertTrue("Items Not Matched", compareItems(editedItems1, savedItems1));
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
    }

    /**
     * Test case 4.1.10: Verify that created/available groups can be deleted in Group Management, for the Group Types
     * such as APN, Controller, Access Area, SGSN, MSC, Tracking Area, Terminal and IMSI.
     */
    @Test public void createdOrAvailableGroupsCanBeDeletedInGroupManagement_4_1_10() throws PopUpException, InterruptedException {
        //Created/Available groups deleted fromm the group Management for group type APN
        waitForPageLoadingToComplete();
        pause(5000);
        String[] groupTypes = new String[] {GuiStringConstants.APN,
                GuiStringConstants.SGSN,};
        for(String groupType : groupTypes){
        	groupMgtWindow.launchGroupType(groupType);
        	groupMgtWindow.waitForLoading(LOADING_GROUPS);
        	String selectedItem = groupMgtWindow.selectItem();
        	groupMgtWindow.clickDeleteButton();
        	groupMgtWindow.clickOpenDialogOkButton();
        	groupMgtWindow.waitForLoading("Retrieving Group Elements for Deletion...");
        	groupMgtWindow.waitForLoading("Deleting Group...");

        	assertTrue(
        			"Selected Item is not removed from database",
        			selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group removed from Database']"));
        	Thread.sleep(2000);
        	groupMgtWindow.closeWindow();
        	assertFalse("Selected Group Name still exist", groupMgtWindow.isGroupNameExist(selectedItem));
        	groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        }

         //Groups cannot be deleted for group types Access Area and Controller because the test data is invalid.Its in jira EUI-475
         
        groupMgtWindow.launchGroupType(GuiStringConstants.ACCESS_AREA);
        groupMgtWindow.waitForLoading(LOADING_GROUPS);
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);

        groupMgtWindow.launchGroupType(GuiStringConstants.CONTROLLER);
        groupMgtWindow.waitForLoading(LOADING_GROUPS);
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
    }
    
    /**
     * Test case 4.1.11: Verify that Exclusive TAC group can be Created in Group Management, for the Group Type Terminal
     * and IMSI.
     */
    @Test public void tacGroupCanBeCreatedInGroupManagementForTheGroupTypeTerminal_4_1_11() throws PopUpException, InterruptedException {

        waitForPageLoadingToComplete();
        pause(5000);
        String[] groupTypes = new String[] {GuiStringConstants.TERMINAL,GuiStringConstants.IMSI};
        for(String groupType : groupTypes){
        	groupMgtWindow.launchGroupType(groupType);
        	groupMgtWindow.waitForLoading(LOADING_GROUPS);
        	groupMgtWindow.clickNewButton();
        	String groupName = createRandomGroupName();
        	String groupItem = createRandomGroupItem();
        	groupTerAndIMSIWindow.enterGroupName(groupName);
        	groupTerAndIMSIWindow.enterGroupItem(groupItem);
        	groupTerAndIMSIWindow.clickAddButton();
        	groupMgtEditWindow.clickSave();
        	pause(5000);
        	groupMgtWindow.waitForLoading(SAVING_GROUP);
        	pause(5000);
        	assertTrue(
        			"New Group cannot be created in the Group Management for the Selected Group Type",
        			selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        	groupMgtWindow.closeWindow();
        	pause(2000);
        	assertTrue("New Group Name does not exist", groupMgtWindow.isGroupNameExist(groupName));
        	groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
        }
    }
    
    /**
     * Test case 4.1.12: Verify that Upload Groups option from the Options Menu launches a window to enable importing of
     * groups in xml format.
     */
    @Test public void importGroupsLaunchesAWindow_4_1_12() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchImportGroupsWindow();
        pause(1000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(IMPORT_GROUP_UI));
    }

    /**
     * Test case 4.1.17: Verify that Delete Groups option from the options Menu is available  and launches a new window
     * to delete xml groups.
     */
    @Test public void deleteGroupsLaunchesAWindow_4_1_17() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchDeleteGroupsWindow();
        pause(1000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(DELETE_GROUP_UI));
    }
    
    /**
     * Test case 4.14.1: Verify that the UI launched for Group Management lists PLMN Group Type.
     */
    @Test public void iuLaunchedForGroupManagementListsPlmnGroupType_4_14_1() throws PopUpException, InterruptedException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupManagementWindow();
        waitForLaunch(2000);
        assertTrue("Does not Launch Appropriate UI Window", selenium.isElementPresent(GROUP_MANAGEMENT_UI));
        selenium.click(TRIGGER_BUTTON);
        assertTrue("In Group Management lists PLMN Group Type not found", selenium.isElementPresent(GROUP_TYPE_PLMN));
    }

    /**
     * Test case 4.14.3: Verify that in the Group Management, the PLMN Group Type can be selected and the values are
     * visible in the space below that.
     */
    @Test public void plmnGroupCanBeSelectedAndTheValuesAreVisibleInTheSpaceBelow_4_14_3() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        assertTrue("Selected Group Type values are not vissible", selenium.isElementPresent(VISSIBLE_VALUES));
    }

    /**
     * Test case 4.14.4:Verify that there is an option 'New' to create a new PLMN Group type.
     */
    @Test public void optionNewToCreateNewPlmnGroupType_4_14_4() throws PopUpException, InterruptedException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        waitForLaunch(5000);
        assertTrue("There is no option 'New' to create a new PLMN Group Type", selenium.isElementPresent(NEW_BUTTON));
    }

    /**
     * Test case 4.14.5:Verify that new PLMN groups can be created by adding the PLMNs available to that new group.
     */
    @Test public void plmnGroupsCanBeCreatedByAddingThePlmnsAvailableToThatNewGroup_4_14_5() throws PopUpException, InterruptedException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        groupMgtWindow.clickNewButton();

        plmnEditWindow.waitForLoading(LOADING_COUNTRIES);
        
        // Select country from the available countries
        plmnEditWindow.selectCountry();
        plmnEditWindow.waitForLoading(LOADING_OPERATORS);
        
        // Select operators for the selected country
        plmnEditWindow.selectOperator(1);
        plmnEditWindow.clickAddButton();
        
        //Create a groupname
        String groupName = createRandomGroupName();
        plmnEditWindow.enterGroupName(groupName);
        plmnEditWindow.clickSave();
        plmnEditWindow.waitForLoading(SAVING_GROUP);
        
        //Check the saving group dialog box
        assertTrue(
                "New Group cannot be created in the Group Management",
                selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        groupMgtWindow.closeWindow();
        groupMgtWindow.waitForLoading("Loading Groups...");
        
        //Check the saved is exist or not
        assertTrue("New Group Name does not exist", groupMgtWindow.isGroupNameExist(groupName));
    }

    /**
     * Test case 4.14.8:Verify that the PLMN group edited will not be saved by clicking on 'Cancel' option available.
     */
    @Test public void plmnGroupsEditedWillNotBeSavedByClickingOnCancelOption_4_14_8() throws PopUpException, InterruptedException {

        waitForPageLoadingToComplete();
        pause(5000);        
        //Launch group type PLMN
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        Random r = new Random();
        
        // Count No of Available Groups
        Number totalNoOfPLMNAvailableGroups = CountNoOfPLMNAvailableGroups();

        // Randomly select a Group
        int itemIndex = r.nextInt(totalNoOfPLMNAvailableGroups.intValue());

        selenium.click("//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div//div[@__idx='"+itemIndex+ "']/div");
        
        // Click on Edit Button
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtWindow.waitForLoading(LOADING_COUNTRIES);
        
        //Count num of Current group elements
        Number totalNoOfElementsInCurrentGroup = CountNoOfElementsInCurrentGroup();
        ArrayList<String> currentGroupElements = CurrentElementsInEditedGroup(totalNoOfElementsInCurrentGroup);
        
        // Add New Element In Current Group
        plmnEditWindow.selectCountry();
        plmnEditWindow.waitForLoading(LOADING_OPERATORS);
        plmnEditWindow.selectOperator(2);
        plmnEditWindow.clickAddButton();
        groupMgtWindow.clickCancelButton();

        selenium.click("//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div//div[@__idx='"
                +itemIndex+ "']/div");
        // Click on Edit Button
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtWindow.waitForLoading(LOADING_COUNTRIES);

        // Count No of Elements in the group when the same group is opened again
        // after cancel action
        
        Number totalNoOfElementsInCurrentGroupAfterCancel = CountNoOfElementsInCurrentGroup();
        // Elements in the group when the same group is opened again after
        // cancel
        ArrayList<String> currentGroupElementsAfterCancel = CurrentElementsInEditedGroup(totalNoOfElementsInCurrentGroupAfterCancel);
        // check weather elements in the group are change or not
        for (int hs = 0; hs < currentGroupElementsAfterCancel.size(); hs++) {
            assertEquals(currentGroupElementsAfterCancel.get(hs), currentGroupElements.get(hs));
        }
        assertEquals(currentGroupElementsAfterCancel.size(), currentGroupElements.size());
    }

    /**
     * Test case 4.15.1: Verify that created/available groups can be edited in Group Management, for the PLMN Group
     * Type, by selecting the PLMN group and use Edit option.
     * Test case 4.15.2: Verify that PLMN Group Type can be edited by adding or deleting already existing PLMNs from the
     * group using 'Edit'.
     * Test case 4.15.3: Verify that the PLMN group edited can be saved by clicking on 'Save' or 'Save as' option
     * available.
     * Test case 4.15.5: Verify that the PLMN group is successfully edited and can be confirmed by a popup message which
     * says Group(s) saved to database. Author: EIKRWAQ
     */
    @Test public void plmnGroupsAddingEditingDeletingSaving_4_15_1() throws PopUpException, InterruptedException {
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        Random r = new Random();

        // Count No of Available Groups
        Number totalNoOfPLMNAvailableGroups = CountNoOfPLMNAvailableGroups();

        // Randomly select a Group
        int itemIndex = r.nextInt(totalNoOfPLMNAvailableGroups.intValue());
        selenium.click("//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div//div[@__idx='"+itemIndex+ "']/div");
        // Click on Edit Button
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtWindow.waitForLoading(LOADING_COUNTRIES);

        // Count No of ELements in Edited Group
        Number totalNoOfElementsInCurrentGroup = CountNoOfElementsInCurrentGroup();
        ArrayList<String> currentElements = CurrentElementsInEditedGroup(totalNoOfElementsInCurrentGroup);
        ArrayList<String> modifiedGroupAfterAddDel;
        while (true) {
            //do Add or Delete Operation randomly
            addDeletOper(currentElements);
            //Keep track of Changes
            totalNoOfElementsInCurrentGroup = CountNoOfElementsInCurrentGroup();
             modifiedGroupAfterAddDel = CurrentElementsInEditedGroup(totalNoOfElementsInCurrentGroup);
            if (!compareChanges(currentElements, modifiedGroupAfterAddDel)) {
                break;
            }
        }

        // Click on Save Button, in order to save changes
        groupMgtWindow.clickSaveButton();
        groupMgtWindow.waitForLoading(SAVING_GROUP);
        groupMgtWindow.waitForLoading(DELETEING_GROUP_ELEMENTS);
        assertTrue("Group cannot be saved in the Group Management",selenium.isElementPresent(GROUP_SAVED_TO_DATABASE));
        waitForLaunch(3000);
        groupMgtWindow.closeWindow();

        selenium.click("//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div//div[@__idx='"+itemIndex+ "']/div");
        // Click on Edit Button
        groupMgtWindow.clickEditButton();
        groupMgtWindow.waitForLoading(LOADING_GROUP_ELEMENTS);
        groupMgtWindow.waitForLoading(LOADING_COUNTRIES);

        // Count No of Elements in the group when the same group is opened again
        // after Changes
        Number totalNoOfElementsInCurrentGroupAfterChanges = CountNoOfElementsInCurrentGroup();
        // Elements in the group when the same group is opened again after
        // Changes
        ArrayList<String> currentElementsAfterChanges = CurrentElementsInEditedGroup(totalNoOfElementsInCurrentGroupAfterChanges);

        // check weather elements in the group are changed or not
        for (int hs = 0; hs < currentElementsAfterChanges.size(); hs++) {
            assertEquals(currentElementsAfterChanges.contains(hs), modifiedGroupAfterAddDel.contains(hs));
        }
        assertEquals(modifiedGroupAfterAddDel.size(), currentElementsAfterChanges.size());
    }
    
    /**
     * Test case 4.16.1: Verify that created/available groups can be deleted in Group Management, for the PLMN Group
     * Type.
     * Test case 4.16.2: Verify that PLMN Group Type can be deleted by using 'Delete Selected'.
     * Test case 4.16.3: Verify that the PLMN group is successfully deleted and can be confirmed by a popup message
     * which says Group(s) deleted from database.
     */
    @Test public void groupsCanBeDeletedInGroupManagementForThePlmnGroupType_4_16_1() throws PopUpException, InterruptedException{
        waitForPageLoadingToComplete();
        pause(5000);
        groupMgtWindow.launchGroupType(GuiStringConstants.PLMN);
        groupMgtWindow.waitForLoading(LOADING_GROUPS);
        pause(2000);
        String selectedItem = groupMgtWindow.selectItem();
        groupMgtWindow.clickDeleteButton();
        groupMgtWindow.clickOpenDialogOkButton();
        groupMgtWindow.waitForLoading("Retrieving Group Elements for Deletion...");
        groupMgtWindow.waitForLoading("Deleting Group...");

        assertTrue(
                "Selected Item is not removed from database",
                selenium.isElementPresent("//div[@class='dialogMiddleCenterInner dialogContent']//div[text()='Group removed from Database']"));
        waitForLaunch(2000);
        selenium.click(CLOSE_BUTTON);
        pause(2000);
        assertFalse("Selected Group Name still exist", groupMgtWindow.isGroupNameExist(selectedItem));
        groupMgtWindow.closeLaunchedUI_Window(GROUP_MANAGEMENT_WINDOW_ID);
    }


    // *********************************************** PRIVATE METHODS ********************************************** //

    private boolean compareChanges(ArrayList<String> currentElements, ArrayList<String> modifiedGroupAfterAddDel) {
        if (modifiedGroupAfterAddDel.size() == 0 || currentElements.size() != modifiedGroupAfterAddDel.size()) {
            return false;
        }
        ArrayList<String> copy = new ArrayList<String>();

        for (String ele : modifiedGroupAfterAddDel) {
            copy.add(ele);
        }

        for (String string : currentElements) {
            if (!copy.remove(string)) {
                return false;
            }
        }

        return (copy.size() == 0);
    }

    private void addDeletOper(ArrayList<String> currentElements) throws InterruptedException {
        Random r = new Random();
        Number totalNoOfElementsInCurrentGroup;
        for (int n = 0; n < 4; n++) {
            // Count No of Countries Available
            Number totalCountries = CountNofAvailableCountries();
            totalNoOfElementsInCurrentGroup = CountNoOfElementsInCurrentGroup();
            
            // Randomly add or delete elements from the edited group
            boolean add = false;

            // Delete Element from the group
            if ((!add) && (totalNoOfElementsInCurrentGroup.intValue() > 1)) {
                int ns = r.nextInt(totalNoOfElementsInCurrentGroup.intValue());
                selenium.click("//div[@id='selenium_tag_F_PANEL_CURRENT_GROUP']/div[3]/div[2]/div/div/div/div[1]//div[@__idx='"
                        + ns + "']/div");
                Thread.sleep(500);
                selenium.click("//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']/div[2]/div/div[2]/div/div[2]/div[3]//button[text()='Delete Selected']");
             //   selenium.click(DELETE_SELECTED);

                // Add Element from the group
            } else if ((add) || ((!add) && (totalNoOfElementsInCurrentGroup.intValue() == 1))) {
                int x = r.nextInt(totalCountries.intValue());

                // Select Country randomly
                selenium.click("//div[@id='selenium_tag_F_PANEL_COUNTRIES']//div[@__idx='" + x + "']/div");
                selenium.waitForTextToDisappear(LOADING_OPERATORS, "24000");
                // No of Total Operators Available in selected country
                Number totalNoOfoperators = CountNoOfOperators();

                int xs = r.nextInt(totalNoOfoperators.intValue());

                // Select Operator randomly
                selenium.click("//div[@id='selenium_tag_F_PANEL_OPERATORS']//div[3]/div[2]//div[@__idx='" + xs
                        + "']/div");
                // Add Operator using Add button
                selenium.click(ADD_BUTTON);

            }
        }
    }

    final List<String> listsOfGroupTypes = new LinkedList<String>(Arrays.asList("Access Area", "APN", "Controller",
            "IMSI", "MSC", "PLMN", "SGSN-MME", "Terminal", "Tracking Area"));

    private void listsOfGroupTypes() {
        for (int i = 1; i <= listsOfGroupTypes.size(); i++) {
            String groupName = selenium              
            		.getText("//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class='GFMUOL5DGT']["+ i +"]");   		   
            if (!listsOfGroupTypes.get(i - 1).equals(groupName)) {
                assertTrue("GroupTypes are not same", false);
                break;
            }
        }

    }

    private Number CountNofAvailableCountries() {
        Number totalCountries = selenium
                .getXpathCount("//div[@id='selenium_tag_F_PANEL_COUNTRIES']//div[3]/div[2]/div/div/div/div[1]/div");
        return totalCountries;

    }

    private Number CountNoOfPLMNAvailableGroups() {
        Number totalNoOfPLMNAvailableGroups = selenium
                .getXpathCount("//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div[1]/div");
        return totalNoOfPLMNAvailableGroups;
    }

    private Number CountNoOfElementsInCurrentGroup() {
        Number totalNoOfElementsInCurrentGroup = selenium
                .getXpathCount("//div[@id='selenium_tag_F_PANEL_CURRENT_GROUP']/div[3]/div[2]/div/div/div/div[1]/div");
        return totalNoOfElementsInCurrentGroup;
    }

    private Number CountNoOfOperators() {
        Number totalNoOfoperators = selenium
                .getXpathCount("//div[@id='selenium_tag_F_PANEL_OPERATORS']//div[3]/div[2]/div/div/div/div[1]/div");
        return totalNoOfoperators;
    }

    private ArrayList<String> CurrentElementsInEditedGroup(Number totalNoOfElementsInCurrentGroup) {
        ArrayList<String> cs = new ArrayList<String>();
        for (int ix = 0; ix < totalNoOfElementsInCurrentGroup.intValue(); ix++) {
            cs.add(selenium
                    .getText("//div[@id='selenium_tag_F_PANEL_CURRENT_GROUP']/div[3]/div[2]/div/div/div/div//div[@__idx='"
                            + ix + "']/div"));
        }
        return cs;
    }

    private String createGroupItemsLdMsg(String groupType) {
        return "Loading " + groupType + "s...";
    }

    private String createRandomGroupName() {
        String r = "abcdefghi";
        Random ran = new Random();
        StringBuilder builder = new StringBuilder("test_");
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

    private boolean compareItems(List<String> editedItems, List<String> savedItems) {
        if (savedItems.size() != editedItems.size()) {
            return false;
        }

        ArrayList<String> copy = new ArrayList<String>();

        for (String ele : savedItems) {
            copy.add(ele);
        }
        for (String item : editedItems) {
            if (!copy.remove(item)) {
                return false;
            }
        }
        return copy.size() == 0;
    }

    private void waitForLaunch(int milliSec) throws InterruptedException {
        Thread.sleep(milliSec);
    }
}
