package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CoreSignallingTestGroup extends SBWebDriverBaseUnitTest {

    @Autowired
    private SessionBrowserTab sessionbrowserTab;

    /**
     * User Story: DEFTFTS-13 Test case 7.64: Verify that a user is able to see Core Event on the Session List entry panel in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignalling_7_64() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchAttach();

        List<WebElement> table = driver.findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[3]"));

        assertTrue("There were no Core Signalling session records found", getCSEventName(table) != null);

        //Action 3
        assertTrue(
                "There is no Service Area tool tip for Core Signalling session records",
                driver.findElement(
                        By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[contains(@title,'Service Area')]"))
                        .isDisplayed());

    }

    /**
     * User Story: DEFTFTS-13 Test case 7.65: Verify that a user is able to access the Menu provided with each of the Events on the Session List entry
     * panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignalling_7_65() throws InterruptedException, PopUpException, ParseException {
        List<String> expectedValues = Arrays.asList("Reports", "View Details");
        String firstCSEventName = null;
        launchAttach();

        List<WebElement> table = driver.findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[3]"));

        if (!table.isEmpty()) {
            firstCSEventName = getCSEventName(table);
            assertTrue("There were no Core Signalling session records found", getCSEventName(table) != null);
        }

        //Action 1
        int position = getCSRecordPosition(table);
        if (position >= 0) {
            driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + position) + "]/td[2]"))
                    .click();

            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedValues.get(i).equals((value.getText())));
            }
        }
        driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + position) + "]/td[2]"))
                .click();

        //Action 2
        driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + position) + "]/td[2]"))
                .click();
        driver.findElement(By.xpath("//div[text()='View Details']")).click();

        //should see the Details Window
        Thread.sleep(2000);

        assertTrue("View details widow not displayed", driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW)).isDisplayed());

        WebElement titleDetails = driver.findElement(By
                .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));

        assertTrue("Window title does not match Event name in session list", titleDetails.getText().contains(firstCSEventName));

        String sessionTimeOnList = driver.findElement(
                By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + position) + "]/td[1]")).getText();

        assertTrue("Window time does not match Event time in session list", titleDetails.getText().contains(sessionTimeOnList));

    }

    /**
     * Test case 7.66: Verify the scrolling option for the Core Events on the Session List entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignalling_7_66() throws InterruptedException, PopUpException, ParseException {
        //action 1
        launchAttach();
        //action 2 - check if scroll bar is present
        if (driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//div[2]/div/div")).isDisplayed()) {
            assertTrue("Scroll bar is not positioned at the top of the session list", sessionbrowserTab.getSessionListScrollBarPosition() <= 0);
        }

        //action 3 - events in descending order
        List<WebElement> table = driver.findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[1]"));
        List<Timestamp> csTimes = new ArrayList<Timestamp>();

        for (WebElement row : table) {
            String text = row.getText();

            if (text.matches("\\d\\d:\\d\\d:\\d\\d")) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date parsedDate = sdf.parse(text);
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                csTimes.add(timestamp);
            }
        }
        Timestamp prevTime = null;

        if (!csTimes.isEmpty()) {
            for (int i = 0; i < csTimes.size(); i++) {
                if (i == 0) {
                    prevTime = csTimes.get(i);
                } else {
                    if (prevTime.getHours() > csTimes.get(i).getHours()) {
                        //hour changed so go to next time
                        prevTime = csTimes.get(i);
                        continue;
                    }
                    if (csTimes.get(i).getMinutes() > 0) {
                        assertTrue("Session times not in descending order", prevTime.before(csTimes.get(i)) | prevTime.equals(csTimes.get(i)));
                    }

                    prevTime = csTimes.get(i);
                }
            }
        }

        //action 4 - Date hyperlinks
        if (driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//div[2]/div/div")).isDisplayed()) {

            List<WebElement> hyperlinks = driver.findElements(By.xpath("//div[@id='SESSION_BROWSER_TAB']//div[@class='bar']/div"));

            for (WebElement e : hyperlinks) {

                e.click();
                Thread.sleep(3000);

                assertTrue("Scroll header does not match the date on hyperlink", driver.findElement(By.xpath(Constants.SESSION_LIST_HEADER))
                        .getText().contains(e.getText()));
            }
        }

        //action 5 & 6 - check header after scrolling
        sessionbrowserTab.scrollToBottomOfSessionList();
        scrollPageTop();
        String header = driver.findElement(By.xpath(Constants.SESSION_LIST_HEADER)).getText();

        List<WebElement> tableAfterScroll = driver
                .findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr"));

        assertTrue("Header not in session list after scroll", isHeadingInList(header, tableAfterScroll));

    }

    /*
     * User Story: DEFTFTS-338 Test case 7.7: Verify that a user is able to see Attach Event on the Session List entry panel in the Session Browser
     * Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_7() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchAttach();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling Attach Event found", sessionbrowserTab.isEventFound("Attach", table));

    }

    /*
     * User Story: DEFTFTS-338 Test case 7.8: Verify that a user is able to launch View Details Window for Attach Event on the Session List entry
     * panel in the Session Browser Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_8() throws InterruptedException, PopUpException, ParseException {
        launchAttach();
        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);
            String firstEventName = sessionbrowserTab.getEventName("Attach", sessionTable);

            if (index >= 0) {
                List<String> expectedOptions = Arrays.asList("Reports", "View Details");
                driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + index) + "]/td[2]"))
                        .click();
                List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

                //Action 1
                for (int i = 0; i < options.size(); i++) {
                    WebElement value = options.get(i);
                    assertTrue("The value " + value.getText() + " was not found in the drop down menu",
                            expectedOptions.get(i).equals((value.getText())));
                }

                //Action 2
                driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
                Thread.sleep(1000);
                assertTrue("Clicking on view details does not launch separate window",
                        driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

                //Action 3
                WebElement titleBar = driver.findElement(By
                        .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
                assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

                String sessionTimeOnList = driver.findElement(
                        By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + index) + "]/td[1]")).getText();
                assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
            }
        }
    }

    /*
     * User Story: DEFTFTS-338 Test case 7.9: Verify the Sections of the View Details Window for Attach Event on the Session List entry panel in the
     * Session Browser Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_9() throws InterruptedException, PopUpException, ParseException {
        launchAttach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /*
     * User Story: DEFTFTS-338 Test case 7.10: Verify the Subscriber Identity Section of the View Details Window for Attach Event in the Session
     * Browser Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_10() throws InterruptedException, PopUpException, ParseException {
        launchAttach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /*
     * User Story: DEFTFTS-338 Test case 7.11: Verify the Network Location Section of the View Details Window for Attach Event in the Session Browser
     * Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_11() throws InterruptedException, PopUpException, ParseException {
        launchAttach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "PLMN", "HLR", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /*
     * User Story: DEFTFTS-338 Test Case 7.12: Verify the Event Details Section of the View Details Window for Attach Event in the Session Browser
     * Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_12() throws InterruptedException, PopUpException, ParseException {
        launchAttach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Attach Type", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /*
     * User Story: DEFTFTS-338 Test Case 7.13: Verify the Event Outcome Section of the View Details Window for Attach Event in the Session Browser
     * Details Tab.
     */
    @Test
    public void testCoreSignallingAttachEvent_7_13() throws InterruptedException, PopUpException, ParseException {
        launchAttach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Attach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventOutcomeSection(index);

            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchAttach() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_ATTACH, false, true);
    }

    private void scrollPageTop() {
        WebElement html = driver.findElement(By.tagName("html"));
        html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT, Keys.SUBTRACT));
    }

    /**
     * User Story: DEFTFTS-339 Test case 7.14: Verify that a user is able to see PDP Activate Event on the Session List entry panel in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_14() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchPDPActivate();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling PDP Activate Event found", sessionbrowserTab.isEventFound("PDP Activate", table));

    }

    /**
     * User Story: DEFTFTS-339 Test case 7.15: Verify that a user is able to launch View Details Window for PDP Activate Event on the Session List
     * entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_15() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);
        String firstEventName = sessionbrowserTab.getEventName("PDP Activate", sessionTable);

        if (index >= 0) {
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + index) + "]/td[2]"))
                    .click();
            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            //Action 1
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            //Action 2
            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            //Action 3
            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + (2 + index) + "]/td[1]")).getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-339 Test case 7.16: Verify the Sections of the View Details Window for PDP Activate Event on the Session List entry panel
     * in the Session Browser Details Tab
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_16() throws InterruptedException, PopUpException, ParseException {
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /*  */
    /**
     * User Story: DEFTFTS-339 Test case 7.17: Verify the Subscriber Identity Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_17() throws InterruptedException, PopUpException, ParseException {
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-339 Test case 7.18: Verify the Network Location Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_18() throws InterruptedException, PopUpException, ParseException {
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "PLMN", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-339 Test Case 7.19: Verify the Event Details Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_19() throws InterruptedException, PopUpException, ParseException {
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Activation Type", "Access Point", "IP Address", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-339 Test Case 7.20: Verify the Event Outcome Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPActivateEvent_7_20() throws InterruptedException, PopUpException, ParseException {
        launchPDPActivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Activate", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchPDPActivate() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_PDP_ACTIVATE, false, true);
    }

    /**
     * User Story: DEFTFTS-340 Test case 7.21: : Verify that a user is able to see PDP Deactivate Event on the Session List entry panel in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_21() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchPDPDeactivate();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling PDP Deactivate Event found", sessionbrowserTab.isEventFound("PDP Deactivate", table));

    }

    /**
     * User Story: DEFTFTS-340 Test case 7.22: Verify that a user is able to launch View Details Window for PDP Deactivate Event on the Session List
     * entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_22() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);
        String firstEventName = sessionbrowserTab.getEventName("PDP Deactivate", sessionTable);

        if (index >= 0) {
            String currentRow = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']";
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(By.xpath(currentRow + "/td[2]")).click();
            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            //Action 1
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            //Action 2
            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            //Action 3
            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(By.xpath(currentRow + "/td[1]")).getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-340 Test case 7.23: Verify the Sections of the View Details Window for PDP Activate Event on the Session List entry panel
     * in the Session Browser Details Tab
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_23() throws InterruptedException, PopUpException, ParseException {
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /**
     * User Story: DEFTFTS-340 Test case 7.24: Verify the Subscriber Identity Section of the View Details Window for Attach Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_24() throws InterruptedException, PopUpException, ParseException {
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-340 Test case 7.25: Verify the Network Location Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_25() throws InterruptedException, PopUpException, ParseException {
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "PLMN", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-340 Test case 7.26: Verify the Event Details Section of the View Details Window for PDP Deactivate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_26() throws InterruptedException, PopUpException, ParseException {
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Deactivation Trigger", "Access Point", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-340 Test Case 7.27: Verify the Event Outcome Section of the View Details Window for PDP Activate Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingPDPDeactivateEvent_7_27() throws InterruptedException, PopUpException, ParseException {
        launchPDPDeactivate();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("PDP Deactivate", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchPDPDeactivate() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_DEACTIVATE, false, true);
    }

    /**
     * Removing this Test Case due to jira EQEV-8382 User Story: DEFTFTS-343 Test case 7.28: Verify that a user is able to see Detach Event on the
     * Session List entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_28() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchDetach();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling Detach Event found", sessionbrowserTab.isEventFound("Detach", table));

    }

    /**
     * User Story: DEFTFTS-343 Test case 7.29: Verify that a user is able to launch View Details Window for Detach Event on the Session List entry
     * panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_29() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchDetach();
        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

        String firstEventName = sessionbrowserTab.getEventName("Detach", sessionTable);

        if (index >= 0) {
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[2]")).click();
            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            //Action 1
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            //Action 2
            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            //Action 3

            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[1]"))
                    .getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-343 Test case 7.30: Verify the Sections of the View Details Window for Detach Event on the Session List entry panel in the
     * Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_30() throws InterruptedException, PopUpException, ParseException {
        launchDetach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /**
     * User Story: DEFTFTS-343 Test case 7.31: Verify the Subscriber Identity Section of the View Details Window for Detach Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_31() throws InterruptedException, PopUpException, ParseException {
        launchDetach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-343 Test case 7.32: Verify the Network Location Section of the View Details Window for Detach Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_32() throws InterruptedException, PopUpException, ParseException {
        launchDetach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "PLMN", "HLR", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-343 Test case 7.33: Verify the Event Details Section of the View Details Window for Detach Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_33() throws InterruptedException, PopUpException, ParseException {
        launchDetach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Detach Type", "Detach Trigger", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-343 Test Case 7.34: Verify the Event Outcome Section of the View Details Window for Detach Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    //@Test
    public void testCoreSignallingDetachEvent_7_34() throws InterruptedException, PopUpException, ParseException {
        launchDetach();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Detach", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchDetach() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_DETACH, false, true);
    }

    /**
     * User Story: DEFTFTS-341 Test case 7.35: Verify that a user is able to see RAU Event on the Session List entry panel in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_35() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchRAU();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling RAU Event found", sessionbrowserTab.isEventFound("RAU", table));

    }

    /**
     * User Story: DEFTFTS-341 Test case 7.36: Verify that a user is able to launch View Details Window for RAU Event on the Session List entry panel
     * in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_36() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchRAU();
        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);
        String firstEventName = sessionbrowserTab.getEventName("RAU", sessionTable);

        if (index >= 0) {
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[2]")).click();

            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            //Action 1
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            //Action 2
            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            //Action 3
            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[1]"))
                    .getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-341 Test case 7.37: Verify the Sections of the View Details Window for RAU Event on the Session List entry panel in the
     * Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Ignore
    @Test
    public void testCoreSignallingRAUEvent_7_37() throws InterruptedException, PopUpException, ParseException {
        launchRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /**
     * User Story: DEFTFTS-341 Test case 7.38: Verify the Subscriber Identity Section of the View Details Window for RAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_38() throws InterruptedException, PopUpException, ParseException {
        launchRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-341 Test case 7.39: Verify the Network Location Section of the View Details Window for RAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_39() throws InterruptedException, PopUpException, ParseException {
        launchRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "HLR", "PLMN", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-341 Test case 5.1.6: Verify the Event Details Section of the View Details Window for RAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_40() throws InterruptedException, PopUpException, ParseException {
        launchRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Intra RAU Type", "Update Type", "Transferred PDPs", "Dropped PDPs", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-341 Test Case 7.41: Verify the Event Outcome Section of the View Details Window for RAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingRAUEvent_7_41() throws InterruptedException, PopUpException, ParseException {
        launchRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("RAU", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchRAU() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_RAU, false, true);
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.42: Verify that a user is able to see ISRAU Event on the Session List entry panel in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_42() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchISRAU();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling ISRAU Event found", sessionbrowserTab.isEventFound("ISRAU", table));
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.43: Verify that a user is able to launch View Details Window for ISRAU Event on the Session List entry
     * panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_43() throws InterruptedException, PopUpException, ParseException {
        //Action 1
        launchISRAU();
        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);
        String firstEventName = sessionbrowserTab.getEventName("ISRAU", sessionTable);

        if (index >= 0) {
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[2]")).click();

            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[1]"))
                    .getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.44: Verify the Sections of the View Details Window for ISRAU Event on the Session List entry panel in the
     * Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Ignore
    @Test
    public void testCoreSignallingISRAUEvent_7_44() throws InterruptedException, PopUpException, ParseException {
        launchISRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.45: Verify the Subscriber Identity Section of the View Details Window for ISRAU Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_45() throws InterruptedException, PopUpException, ParseException {
        launchISRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.46: Verify the Network Location Section of the View Details Window for ISRAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_46() throws InterruptedException, PopUpException, ParseException {
        launchISRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "HLR", "PLMN", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-342 Test case 7.47: Verify the Event Details Section of the View Details Window for ISRAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_47() throws InterruptedException, PopUpException, ParseException {
        launchISRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Intra RAU Type", "Update Type", "Transferred PDPs", "Dropped PDPs", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-342 Test Case 7.48: Verify the Event Outcome Section of the View Details Window for ISRAU Event in the Session Browser
     * Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingISRAUEvent_7_48() throws InterruptedException, PopUpException, ParseException {
        launchISRAU();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("ISRAU", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    private void launchISRAU() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_ISRAU, false, true);
    }

    /**
     * User Story: DEFTFTS-344 Test case 7.49: Verify that a user is able to see Service Request Event on the Session List entry panel in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_49() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> table = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("There were no Core Signalling Service Request Event found", sessionbrowserTab.isEventFound("Service Request", table));

    }

    /**
     * User Story: DEFTFTS-344 Test case 7.50: Verify that a user is able to launch View Details Window for Service Request Event on the Session List
     * entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_50() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();
        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);
        String firstEventName = sessionbrowserTab.getEventName("Service Request", sessionTable);

        if (index >= 0) {
            List<String> expectedOptions = Arrays.asList("Reports", "View Details");
            driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[2]")).click();
            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

            //Action 1
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
            }

            //Action 2
            driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
            Thread.sleep(1000);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            //Action 3
            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']/td[1]"))
                    .getText();
            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }
    }

    /**
     * User Story: DEFTFTS-344 Test case 7.51: Verify the Sections of the View Details Window for Service Request Event on the Session List entry
     * panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_51() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        if (!sessionTable.isEmpty()) {
            int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);

            if (index >= 0) {
                openViewDetails(index);

                if (driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed()) {

                    //Action 1
                    List<String> expectedSections = Arrays.asList("Subscriber Identity", "Network Location", "Event Details", "Event Outcome");
                    List<WebElement> sections = driver.findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']//span[1]"));

                    for (int i = 0; i < sections.size(); i++) {
                        WebElement value = sections.get(i);
                        assertTrue("The section " + expectedSections.get(i) + " was not found in the view details popup", expectedSections.get(i)
                                .equals((value.getText())));
                    }

                    //Action 2
                    if (driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).isDisplayed()) {
                        //get if scrollbar is at the top
                        assertTrue(
                                "View Details scrollbar is not at the top",
                                getViewDetailsScrollBarPosition(driver.findElement(By.xpath(Constants.VIEW_DETAILS_SCROLLBAR)).getAttribute("style")) == 0);
                    }

                    //Action 4
                    toggleSectionsOfViewDetailsPopup();
                    toggleSectionsOfViewDetailsPopup();
                }
            }
        }
    }

    /**
     * User Story: DEFTFTS-344 Test case 7.52: Verify the Subscriber Identity Section of the View Details Window for Service Request Event in the
     * Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_52() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getSubscriberIdentitySection(index);

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-344 Test case 7.53: Verify the Network Location Section of the View Details Window for Service Request Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_53() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getNetworkLocationSection(index);

            List<String> expectedValues = Arrays.asList("SGSN", "Controller", "PLMN", "Location Area", "Routing Area", "Service Area");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-344 Test case 7.54: Verify the Event Details Section of the View Details Window for Service Request Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_54() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);

        if (index >= 0) {
            List<WebElement> section = getEventDetailsSection(index);

            List<String> expectedValues = Arrays.asList("Event Type", "Event Time", "Event Result", "Duration (ms)", "Radio Access Technology",
                    "Service Type", "Service Request Trigger", "Paging Attempts", "Request Retries");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    /**
     * User Story: DEFTFTS-344 Test Case 7.55: Verify the Event Outcome Section of the View Details Window for Service Request Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void testCoreSignallingServiceRequestEvent_7_55() throws InterruptedException, PopUpException, ParseException {
        launchServiceRequest();

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        int index = sessionbrowserTab.getRecordPosition("Service Request", sessionTable);

        if (index >= 0) {

            List<WebElement> section = getEventOutcomeSection(index);
            List<String> expectedValues = Arrays.asList("Cause Code", "Cause Code Description", "Sub Cause Code", "Sub Cause Code Description");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value: " + text + " does not match expected field: " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }
        }
    }

    // ********************************************* PRIVATE METHODS ********************************************//
    private void launchServiceRequest() throws ParseException, InterruptedException {
        openSessionDetail(CommonDataType.SB_IMSI_SERVICE_REQUEST, false, true);
    }

    private boolean isHeadingInList(String header, List<WebElement> tableAfterScroll) {
        for (WebElement row : tableAfterScroll) {
            if (header.equals(row.getText())) {
                return true;
            }
        }

        return false;
    }

    private String getCSEventName(List<WebElement> table) {
        List<String> csValues = Arrays.asList("Attach", "PDP Activate", "PDP Deactivate", "Detach", "RAU", "ISRAU", "Service Request");
        for (WebElement row : table) {
            String text = row.getText().trim();
            if (csValues.contains(text)) {
                return text;
            }
        }
        return null;
    }

    private int getCSRecordPosition(List<WebElement> table) {

        List<String> csValues = Arrays.asList("Attach", "PDP Activate", "PDP Deactivate", "Detach", "RAU", "ISRAU", "Service Request");
        for (int i = 0; i < table.size(); i++) {
            String text = table.get(i).getText().trim();
            if (csValues.contains(text)) {
                return i;
            }
        }
        return -1;
    }

    private List<WebElement> getSubscriberIdentitySection(int index) {
        openViewDetails(index);

        boolean sectionVisiable = driver
                .findElement(
                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]"))
                .getAttribute("style").isEmpty();

        if (!sectionVisiable) {
            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]//span[2]"))
                    .click();
        }

        return driver
                .findElements(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]//tr/td[1]"));
    }

    private List<WebElement> getNetworkLocationSection(int index) {
        openViewDetails(index);

        boolean sectionVisiable = driver
                .findElement(
                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]"))
                .getAttribute("style").isEmpty();

        if (!sectionVisiable) {
            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]//span[2]"))
                    .click();
        }
        return driver
                .findElements(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]//tr/td[1]"));
    }

    private List<WebElement> getEventDetailsSection(int index) {

        openViewDetails(index);

        boolean sectionVisiable = driver
                .findElement(
                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]"))
                .getAttribute("style").isEmpty();

        if (!sectionVisiable) {
            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]//span[2]"))
                    .click();
        }

        return driver
                .findElements(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]//tr/td[1]"));
    }

    private List<WebElement> getEventOutcomeSection(int index) {
        openViewDetails(index);

        boolean sectionVisiable = driver
                .findElement(
                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]/div[2]"))
                .getAttribute("style").isEmpty();

        if (!sectionVisiable) {
            scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                    .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]//span[2]")));
            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]//span[2]"))
                    .click();
            scrollViewDetailsSectionList(false, 5);
        }

        return driver
                .findElements(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[4]/div[2]//tr/td[1]"));
    }
}
