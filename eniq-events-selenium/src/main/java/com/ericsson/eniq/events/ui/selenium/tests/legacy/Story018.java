package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import com.thoughtworks.selenium.SeleneseTestCase;

/*
 * Epic 1 : Story 018 : Row Properties Window
 * 
 * The following functionality needs to be verified. 
 * 	Selecting a row in the grid and clicking the Properties Button Launches a Dialog in 
 * 		the centre of the Parent Window.
 * 	If no row in the grid is selected and the Properties window is clicked, a Dialog will 
 * 		appear asking the user to select a row in the grid before clicking the Properties button.
 * 	The row selected in the grid is opened in the dialog, displaying all the columns of the 
 * 		row in a vertical list.
 * 	Clicking Close button closes the dialog.
 *  
 */
public class Story018 extends SeleneseTestCase {
    @Override
    public void setUp() throws Exception {
        setUp("http://10.42.33.235:10086/EniqEventsUI/", "*firefox");
        //setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*iexplore");
        //setUp("http://10.42.33.235:10086/ENIQEventsUI/", "*chrome");
    }

    public void teststory018() throws Exception {
        selenium.open("/EniqEventsUI/");
        Thread.currentThread();
        selenium.click("//table[@id='NETWORK_TAB_START']/tbody/tr[2]/td[2]/em/button");
        selenium.click("link=Cause Code Analysis");
        Thread.sleep(5000);
        final String col1 = selenium
                .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[1]/div/div");
        final String col2 = selenium
                .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[2]/div");
        final String col3 = selenium
                .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[3]/div");
        final String col4 = selenium
                .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[4]/div");

        //open row properties
        selenium
                .click("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']/div[1]/div[1]/div[2]/div/div/table/tbody/tr/td[1]/div/div");
        selenium.click("//table[@id='btnProperties']/tbody/tr[2]/td[2]/em/button");
        Thread.sleep(5000);
        final String col1a = selenium.getText("//td[2]/label");
        final String col2a = selenium.getText("//tr[2]/td[2]/label");
        final String col3a = selenium.getText("//div[2]/div[1]/div/div/div/div/table/tbody/tr[3]/td[2]");
        final String col4a = selenium.getText("//tr[4]/td[2]/label");

        //click close button
        selenium.click("//td[2]/table/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[2]/td[2]/em/button");

        //check displayed data
        verifyEquals(col1, col1a);
        verifyEquals(col2, col2a);
        verifyEquals(col3, col3a);
        verifyEquals(col4, col4a);

        //checkTestData();
        //data presented for subscriber tab is not using json file so row properties button
        //does not work for this data
    }

    public void checkTestData() throws InterruptedException {
        selenium.click("//li[@id='x-auto-2__SUBSCRIBER_TAB']/a[2]/em/span/span");
        selenium.type("//td[5]/div/input", "440008704283457");
        selenium.click("//table[@id='SUBSCRIBER_TAB_START']/tbody/tr[2]/td[2]/em/button");
        selenium.click("//a[@id='SUBSCRIBER_EVENT_ANALYSIS']/img");
        Thread.sleep(5000);

        final String b1 = selenium
                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[1]/div");
        final String b2 = selenium
                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[2]/div");
        final String b3 = selenium
                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[3]/div");
        final String b4 = selenium
                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[4]/div");
        final String b5 = selenium
                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[5]/div");
        //close the pop-up
        selenium
                .click("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']/div[1]/div[1]/div[2]/div/div[1]/table/tbody/tr/td[1]/div");
        selenium.click("//div[2]/div[2]/div[1]/div/div/div[1]/div/div/table[5]/tbody/tr[2]/td[2]/em/button");
        Thread.sleep(5000);

    }
}
