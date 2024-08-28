package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PLMNGroupEditWindow extends GroupManagementEditWindow {

    private static final String INDEX_PREFIX = " and (@__idx='";

    private static final String INDEX_POSTFIX = "')]/div";

    public List<String> selectAvailableItem(int noOfItems, String selector) {
        Random r = new Random();
        List<String> selectedItems = new ArrayList<String>();
        if (noOfItems >= 1) {
            selenium.controlKeyDown();
        }
        String availItemsPath = getItemsPath(selector);
        System.out.println("Path: " + availItemsPath);
        int countItems = countItems(availItemsPath);
        for (int i = 0; i < noOfItems; i++) {
            int itemIndex = r.nextInt(countItems);
            String itemPath = availItemsPath.substring(0, availItemsPath.length() - 1) + INDEX_PREFIX + itemIndex
                    + INDEX_POSTFIX;
            System.out.println("item path: " + itemPath);
            selenium.click(itemPath);
            selectedItems.add(selenium.getText(itemPath));
        }
        if (noOfItems >= 1) {
            selenium.controlKeyUp();
        }
        return selectedItems;
    }

    public String selectCountry() {
        List<String> selectAvailableItem = selectAvailableItem(1, "COUNTRIES");
        return selectAvailableItem.get(0);
    }

    public List<String> selectOperator(int noOfOperators) {
        List<String> selectAvailableItem = selectAvailableItem(noOfOperators, "OPERATORS");
        return selectAvailableItem;
    }

    private String getItemsPath(String selector) {
        return "//div[@id='selenium_tag_F_PANEL_" + selector
                + "']//div[contains(@style,'overflow: visible')]/div/div[contains(@style,'outline:none')]";
    }

    private String createItemPath(int itemIndex) {
        return "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//div[contains(@style,'outline: medium none') and (@__idx='"
                + itemIndex + "')]/div";
    }

}
