package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;

import static com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementConstants.*;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;

@Component
public class GroupManagementWindow {

    // private static final String GROUP_SAVED_TO_DATABASE = null;
    @Autowired
    private EricssonSelenium selenium;
    private static final String groupManagementWindow = "//*[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']";

    public void launchGroupManagementWindow() {
        selenium.click(OPTIONS_MENU);
        selenium.waitForElementToBePresent(GROUP_MANAGEMENT, "2000");
        selenium.click(GROUP_MANAGEMENT);
    }

    public void launchImportGroupsWindow() {
        selenium.click(OPTIONS_MENU);
        selenium.click(IMPORT_GROUPS);
    }

    public void launchDeleteGroupsWindow() {
        selenium.click(OPTIONS_MENU);
        selenium.click(DELETE_GROUPS);
    }

    public void launchGroupType(final String groupType) throws PopUpException {
        launchGroupManagementWindow();
        selenium.waitForElementToBePresent(TRIGGER_BUTTON, "10000");
        selenium.click(TRIGGER_BUTTON);
        selenium.click("//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[@class='GFMUOL5DGT'][text()='" + groupType + "']");
        waitForLoading("Loading Groups...");
    }

    public void closeWindow() {
        selenium.click(CLOSE_BUTTON);
    }

    public void clickNewButton() throws PopUpException {
        selenium.click(NEW_BUTTON);
        selenium.waitForPageLoadingToComplete();
    }

    public void clickEditButton() {
        selenium.click(EDIT_BUTTON);
    }

    public void clickDeleteButton() {
        selenium.click(DELETE_BUTTON);
    }

    public void clickCancelButton() {
        selenium.click(CANCEL_BUTTON);
    }

    public void clickSaveButton() {
        selenium.click(SAVE_BUTTON);
    }

    public void clickOpenDialogOkButton() {
        selenium.click(OK_BUTTON);
    }

    public void clickOpenDialogDismissButton() {
        selenium.click(DISMISS_BUTTON);
    }

    public void closeLaunchedUI_Window(final String groupID) {
        selenium.click("//div[@id='selenium_tag_" + groupID + "']//img[@class='gwt-Image']");
    }

    public String selectItem() {
        Random r = new Random();
        int itemIndex = r.nextInt(countItems());
        String itemPath = createItemPath(itemIndex);
        System.err.println("selectedIndex:" + itemIndex);
        selenium.click(itemPath);
        String selectedItem = selenium.getText(itemPath);
        System.err.println("selectedItem:" + selectedItem);
        return selectedItem;
    }

    public void selectItem(String itemName) {
        int count = countItems();
        for (int i = 1; i <= count; i++) {
            String itemPath = createItemPath(i);
            String text = selenium.getText(itemPath);
            if (text.equals(itemName)) {
                selenium.click(itemPath);
                break;
            }
        }
    }

    public void waitForLoading(String loadingMsg) {
        selenium.waitForTextToDisappear(loadingMsg, "30000");
    }

    public boolean isGroupNameExist(String groupName) {
        boolean exist = false;
        int countItems = countItems();
        for (int i = countItems; i >= 1; i--) {
            String itemPath = createItemPath(i);
            boolean s = selenium.isElementPresent(itemPath);

            if (s == true && selenium.getText(itemPath).equals(groupName)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void closeGroupManagementWindow() {
        selenium.click(groupManagementWindow + "/div[1]/div[1]/img");
    }

    // *********************************************** PRIVATE METHODS ********************************************** //
    private String createItemPath(int itemIndex) {
        return "//*[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div[1]/div[" + itemIndex + "]/div";
    }

    private int countItems() {
        int count = selenium.getXpathCount(ITEMS_PATH).intValue();
        return count;
    }
}
