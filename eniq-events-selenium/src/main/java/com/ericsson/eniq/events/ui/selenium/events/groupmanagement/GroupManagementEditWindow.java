package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;

import static com.ericsson.eniq.events.ui.selenium.common.SeleniumUtils.*;
import static com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementConstants.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;

@Component
public class GroupManagementEditWindow {

    @Autowired
    protected EricssonSelenium selenium;

    private static final String groupManagementEditWindow = "//*[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']";
    private static final String CURRENT_ITEMS_PATH = "//div[@id='selenium_tag_F_PANEL_CURRENT_GROUP']//div[contains(@style,'outline: none')]";
    private static final String IMSI_TAC_CURRENT_ITEMS_PATH = groupManagementEditWindow + "/div/div/div/div/div/div/div/div/div";
    private static final String INDEX_PREFIX = "[@__idx='";
    private static final String INDEX_POSTFIX = "']/div";
    protected static final String INPUT_NAME = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[1]/table/tbody/tr/td[2]/input";

    public void waitForLoading(final String loadingMsg) {
        selenium.waitForTextToDisappear(loadingMsg, "60000");

    }

    public List<String> selectAvailableItem(final int noOfItems, final String groupType) {
        selenium.setSpeed("500");
        final Random r = new Random();
        final List<String> selectedItems = new ArrayList<String>();
        selenium.controlKeyDown();
        final String availItemsPath = getItemsPathInGroup(groupType);
        final int countItems = countItems(availItemsPath);
        for (int i = 0; i < noOfItems; i++) {
            final int itemIndex = r.nextInt(countItems);
            final String itemPath = availItemsPath + INDEX_PREFIX + itemIndex + INDEX_POSTFIX;
            selenium.click(itemPath);
            selectedItems.add(selenium.getText(itemPath));
        }
        selenium.controlKeyUp();
        return selectedItems;
    }

    private String createItemPath(final int itemIndex) {
        return "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[contains(@style,'outline:none') and (@__idx='" + itemIndex + "')]/div";
    }

    public void clickAddButton() {
        selenium.click(ADD_BUTTON);
    }

    public void enterGroupName(final String groupName) {
        selenium.type(INPUT_NAME, groupName);
    }

    public int countItems(final String path) {
        return selenium.getXpathCount(path).intValue();
    }

    public void clickSave() {
        selenium.click(SAVE_BUTTON);

    }

    public void clickDeleteSelected() {
        selenium.click(DELETE_SELECTED);
    }

    public void clickCancel() {
        selenium.click(CANCEL_BUTTON);
    }

	public List<String> getCurrentGroupItems() {
		int countItems;
		boolean isImsiOrTac = false;

		if(selenium.isElementPresent(CURRENT_ITEMS_PATH)){
			countItems = countItems(CURRENT_ITEMS_PATH);
		}else{
			countItems = countItems(IMSI_TAC_CURRENT_ITEMS_PATH);
			isImsiOrTac = true;
		}


		List<String> items = new ArrayList<String>();

		if(!isImsiOrTac){
			for (int i = 0; i < countItems; i++) {
				String itemPath = createItemPath(i);
				items.add(selenium.getText(itemPath));
			}
		}else{
			for(int i = 1; i <= countItems; i++){
				items.add(selenium.getText(IMSI_TAC_CURRENT_ITEMS_PATH + "/div/div/div/div[" + i + "]"));
			}
		}

		return items;
	}

    /**
     * Written by elukpot.
     *
     * Removes the specified element from the specified group. The Save, Save As, Delete Selected or Cancel buttons are not pressed.
     *
     * @param groupItem
     */
    public void removeGroupElement(final String groupItem) {

        final String xPathToGroupElement = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[2]/div/div/div[3]/div/div/div/div[1]/div[text()='";
        final String deleteSelectedButton = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[3]/table/tbody/tr/td[1]/button";

        final String xPathToDesiredGroupElement = xPathToGroupElement + groupItem + "']";

        if (selenium.isElementPresent(xPathToDesiredGroupElement)) {
            selenium.click(xPathToDesiredGroupElement);
            selenium.click(deleteSelectedButton);
        }
    }

    /**
     * Written by elukpot.
     *
     * Removes the specified elements in a group one at a time. The Save, Save As, Delete Selected or Cancel buttons are not pressed.
     *
     * @param groupItems
     */
    public void removeGroupElements(final List<String> groupItems) {

        final String xPathToGroupElement = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[2]/div/div/div[3]/div/div/div/div[1]/div[text()='";
        final String deleteSelectedButton = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[3]/table/tbody/tr/td[1]/button";

        for (final String groupItem : groupItems) {
            final String xPathToDesiredGroupElement = xPathToGroupElement + groupItem + "']";

            if (selenium.isElementPresent(xPathToDesiredGroupElement)) {
                selenium.click(xPathToDesiredGroupElement);
                selenium.click(deleteSelectedButton);
            }
        }
    }

    /**
     * Written by elukpot.
     *
     * Adds the comma separated string to the group. The Save, Save As, Delete Selected or Cancel buttons are not pressed.
     *
     * @param groupItems
     *        A String of comma separated group elements.
     */
    public void enterGroupItems(final String groupItems) {

        final String enterGroupItemTextBox = groupManagementEditWindow + "/div[2]/div/div[2]/div/div[2]/div/div/div[2]/div/input";
        selenium.click(enterGroupItemTextBox);
        pause(500);
        selenium.type(enterGroupItemTextBox, groupItems);
        pause(500);
        clickAddButton();
        pause(500);
    }

    /**
     * Written by elukpot.
     *
     * Enters group elements using the filter. The Save, Save As, Delete Selected or Cancel buttons are not pressed.
     *
     * @param groupItems
     *        A List of the group elements.
     * @param groupType
     *        The Type of group to be created, e.g. APN, Controller, etc... (Needed for XPath).
     */
    public void enterGroupItemsUsingFilter(final List<String> groupItems, final String groupType) {

        final String filterPanelXPath = "//div[contains(@id, 'selenium_tag_F_PANEL_" + groupType + "')]";

        for (final String groupItem : groupItems) {

            final String filterXPath = groupManagementEditWindow + filterPanelXPath + "/div[2]/div/input";
            selenium.type(filterXPath, groupItem);
            pause(6000);
            selenium.typeKeys(filterXPath, "\t");
            pause(6000);

            final String filterResultsRowsXPath = groupManagementEditWindow + filterPanelXPath
                    + "/div[3]/div[2]/div/div/div/div[1]/div[1]/div/strong";
            pause(2000);

            if (selenium.isElementPresent(filterResultsRowsXPath)) {
                selenium.click(filterResultsRowsXPath);
                clickAddButton();
            }
        }
    }

    // *********************************************** PRIVATE METHODS ********************************************** //
    private String getItemsPath(final String groupType) {
        //String itemsPath = "//div[@id='selenium_tag_F_PANEL_" + groupType.toUpperCase().replace(" ", "_") + "']//div[contains(@style,'overflow: visible')]/div/div";
        final String itemsPath = "//div[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']//div[contains(@style,'overflow: visible')]/div/div";
        return itemsPath;
    }

    private String getItemsPathInGroup(final String groupType) {
        final String itemsPath = "//div[@id='selenium_tag_F_PANEL_" + groupType.toUpperCase().replace(" ", "_")
                + "']//div[contains(@style,'overflow: visible')]/div/div";

        return itemsPath;
    }

}
