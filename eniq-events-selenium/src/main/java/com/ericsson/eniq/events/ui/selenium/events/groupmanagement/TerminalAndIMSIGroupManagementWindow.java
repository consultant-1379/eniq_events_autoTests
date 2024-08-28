package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;

import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TerminalAndIMSIGroupManagementWindow {

    
    @Autowired
    private EricssonSelenium selenium;
    
    private static final String INPUT_NAME = "//*[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']/div[2]/div/div[2]/div/div[1]/table/tbody/tr/td[2]/input";
    
    private static final String INPUT_ITEM = "//*[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']/div[2]/div/div[2]/div/div[2]/div/div/div[2]/div/input";
    
    private static final String ADD_BUTTON = "//button[text()='Add']";
    
    
    public void enterGroupName(String groupName) {
        selenium.type(INPUT_NAME, groupName);

    }

    public void enterGroupItem(String groupItem) {
        selenium.type(INPUT_ITEM, groupItem);

    }

    public void clickAddButton(){
        selenium.click(ADD_BUTTON);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

