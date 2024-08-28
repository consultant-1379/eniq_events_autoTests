package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RanCoreSignallingSessionListTestGroup extends SBWebDriverBaseUnitTest {
    @Autowired
    private SessionBrowserTab sessionbrowserTab;

    private static final String INTERNAL_CALL_SETUP_FAILURE = "Internal Call Setup Failure";
    private static final String DATA_BEARER_SESSION = "Data Bearer Session";

    /**
     * This method is to check usability - any valid imsi can be used.. Test Case 15.9: Verify that a user can perform the normal operations with all
     * popup windows within the session browser application
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void verifyPopupWindowUsability_15_9() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, false, false);

        List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionTable, DATA_BEARER_SESSION);

        int index = getRecordPosition(DATA_BEARER_SESSION, sessionTable);

        if (index >= 0) {
            openViewDetails(index);

            // Action 1
            boolean open = driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed();

            assertTrue("Clicking on view details does not launch separate window", open);

            // Action 2
            closeViewDetails();
            Thread.sleep(1000);

            // Action 5
            openViewDetails(index);

            int oldHeight = sessionbrowserTab.parseStyle(driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW)).getAttribute("style"), "height");
            int oldWidth = sessionbrowserTab.parseStyle(driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW)).getAttribute("style"), "width");

            Thread.sleep(1000);

            if (isViewDetailsMaximized()) {

                int newdHeight = sessionbrowserTab.parseStyle(driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW)).getAttribute("style"),
                        "height");
                int newWidth = sessionbrowserTab.parseStyle(driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW)).getAttribute("style"),
                        "width");

                // check that maximise caused the size to increase
                assertTrue("Height did not increase after maximize", newdHeight > oldHeight);
                assertTrue("Width did not increase after maximize", newWidth > oldWidth);
            }

            closeViewDetails();
            // driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW_CLOSE))
            // .click();
            // Action 6 cannot be automated

            // Action 7
            // The subsections within the popup window should correspond to
            // that of the
            // previous collapse/expand preference.
            openViewDetails(index);

            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]//span[2]"))
                    .click();

            List<Boolean> beforeClose = checkViewDetalsSubSections();

            closeViewDetails();
            // driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW_CLOSE))
            // .click();

            openViewDetails(index);
            List<Boolean> afterClose = checkViewDetalsSubSections();

            assertTrue("The subsections within view details should persist from previous collapse/expand preference", beforeClose.equals(afterClose));

        }

    }

    /**
     * Test Case 10.1.1: verify that a user is able to see internal call setup failure event on the session list entry panel in the session browser
     * details tab
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void testInternalCallSetupFailure_10_1_1() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        // Action 1
        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        // Action 2
        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);
        if (index > 0) {
            driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']//tbody//tr[" + index + "][contains(@__timestamp,'')]"))
                    .isDisplayed();
        }
    }

    /**
     * Test case 10.1.2: verify that a user is able to launch view details window for internal call setup failure event on the session list entry
     * panel in the session browser details tab
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void testInternalCallSetupFailure_10_1_2() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        // Action 2
        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);
        String firstEventName = RAN_SignallingEvents(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        if (index >= 0) {
            openViewDetails(index);
            assertTrue("Clicking on view details does not launch separate window",
                    driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

            // Action 3
            WebElement titleBar = driver.findElement(By
                    .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
            assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(firstEventName));

            String sessionTimeOnList = driver.findElement(
                    By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + index + "']/td[1]")).getText();

            assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
        }

    }

    /**
     * Test case 10.1.3: verify that a user is able to launch view details window for internal call setup failure event on the session list entry
     * panel in the session browser details tab
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void testInternalCallSetupFailure_10_1_3() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        if (index >= 0) {
            openViewDetails(index);

            List<String> expectedValues = Arrays.asList("Subscriber Identity", "Network Location", "Event Details");
            List<String> actualValues = getViewDetailsHeadings();

            // Action 1
            assertTrue(expectedValues.equals(actualValues));

            // Action 2
            assertTrue("Scroll bar not positioned at the top of the session list pane", sessionbrowserTab.getSessionListScrollBarPosition() == 0);

            // Action 3 - Will be tested later along with persistence

            // Action 4
            List<WebElement> visiableSections = driver.findElements(By
                    .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));

            for (int i = 1; i <= visiableSections.size(); i++) {

                boolean visiable = driver
                        .findElement(
                                By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                        + i + "]/div[2]")).getAttribute("style").isEmpty();

                if (visiable) {
                    // Section is expanded so collapse it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertFalse(
                            "The expanded heading did not collapse : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2]")).getAttribute("style").isEmpty());

                } else {
                    // Section is collapse so expand it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertTrue(
                            "The collapsed heading did not expand : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2][@style='']")).isDisplayed());
                }
            }

            for (int i = 1; i <= visiableSections.size(); i++) {

                boolean visiable = driver
                        .findElement(
                                By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                        + i + "]/div[2]")).getAttribute("style").isEmpty();

                if (visiable) {
                    // Section is expanded so collapse it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertFalse(
                            "The expanded heading did not collapse : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2]")).getAttribute("style").isEmpty());

                } else {
                    // Section is collapse so expand it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertTrue(
                            "The collapsed heading did not expand : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2][@style='']")).isDisplayed());
                }
            }
            // Action 5 - Covered in DEFTFTS-13

        }
    }

    /**
     * Test case 10.1.4: Verify the Subscriber Identity Section of the View Details Window for Internal Call Setup Failure Event in the Session
     * Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void testInternalCallSetupFailure_10_1_4() throws InterruptedException, ParseException {
        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);
        if (index >= 0) {
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

            List<WebElement> section = driver
                    .findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[1]/div[2]//tr/td[1]"));

            List<String> expectedValues = Arrays.asList("MSISDN", "IMSI", "Terminal", "Network");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value " + text + " does not match expected field - " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }

        }
    }

    /**
     * Test case 10.1.5: Verify the Network Location Section of the View Details Window for Internal Call Setup Failure Event in the Session Browser
     * Details Tab
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void testInternalCallSetupFailure_10_1_5() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);
        if (index >= 0) {
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

            List<WebElement> section = driver
                    .findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[2]/div[2]//tr/td[1]"));

            List<String> expectedValues = Arrays.asList("Controller", "PLMN", "Location Area", "Routing Area", "Cell Id", "CID");

            for (int i = 0; i < section.size(); i++) {
                String text = section.get(i).getText().trim();
                assertTrue("Value " + text + " does not match expected field - " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }

        }
    }

    /**
     * Test case 10.1.6: Verify the Event Details Section of the View Details Window for Internal Call Setup Failure Event in the Session Browser
     * Details Tab
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Ignore
    @Test
    public void testInternalCallSetupFailure_10_1_6() throws InterruptedException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_INT_CALL_SETUP_FAILURES, true, false);

        List<WebElement> sessionList = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        checkSessionList(sessionList, INTERNAL_CALL_SETUP_FAILURE);

        int index = RAN_SignallingPosition(sessionList, INTERNAL_CALL_SETUP_FAILURE);
        if (index >= 0) {
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

            scrollViewDetailsSectionList(false, 20);
            List<WebElement> section = driver
                    .findElements(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[3]/div[2]//tr/td[1]"));

            List<String> expectedValues = Arrays.asList("Event Time", "Source Rab Type", "Target Rab Type", "Wanted Rab Type", "Trigger Point",
                    "UTRAN RANAP Cause", "Data in DL RLC Buffers", "Serving HSDSCH Cell Id", "Serving HSDSCH CID", "Serving HSDSCH Cell RNC Id",
                    "Originating State", "Procedure Indicator", "Cause Value", "Extended Cause Value", "Evaluation Case", "Exception Class",
                    "RRC Establishment Cause", "Cello AAL2NCI Reject Reason", "Source Connection Prop", "Target Connection Prop",
                    "Wanted Connection Prop");

            for (int i = 0; i < section.size(); i++) {
                scrollViewDetailsSectionList(false, 2);
                String text = section.get(i).getText().trim();
                assertTrue("Value " + text + " does not match expected field - " + expectedValues.get(i), expectedValues.get(i).equals(text));
            }

        }
    }

    /*
     * NOTE: THIS TEST IS NOT WORKING - REDO Test case 11.1.1: Verify that a user is able to see the Radio(CFA) signalling events for a selected
     * subscriber over a specific time period
     */
    @Ignore
    @Test
    public void radioSignallingEventsForSelectedSubsciberOverSpecificTimePeriod_11_1_1() throws InterruptedException {
        List<String> expectedOptions = Arrays.asList("Reports", "View Details");
        // Action 1

        sessionbrowserTab.openTab();
        waitForPageToLoad();
        sessionbrowserTab.openDetailsSessionBrowser("454063302579768");
        sessionbrowserTab.openCustomDateAndTime("2012 Dec", "2012 Dec", "16", "17");
        driver.findElement(By.xpath(Constants.RAN_SIGNALLING_BUTTON)).click();
        driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
        waitForPageToLoad();

        assertTrue("Radio Details are not displayed on chosen", driver.findElement(By.xpath(Constants.SESSION_LIST)).isDisplayed());

        // Action 2 & Action 3

        assertTrue(
                "User not able to see the start time of each Radio Signalling event in the Session List",
                driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']//tbody//tr[contains(@__timestamp,'')]"))
                        .isDisplayed());
        assertTrue(
                "Radio signalling event is not displayed",
                driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']//tbody//tr[contains(text(),'')]/td[3]"))
                        .isDisplayed());
        assertTrue(
                "User not able to see a tooltip",
                driver.findElement(
                        By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']//tbody//tr//td[3][contains(@title,'')]"))
                        .isDisplayed());

        // Action 4
        List<WebElement> sessionList = driver.findElements(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td"));
        int index = RAN_SignallingPosition(sessionList, "Data Bearer Session");
        if (index >= 0) {
            driver.findElement(By.xpath("//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[" + index + "]/td[2]")).click();
            List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));
            for (int i = 0; i < options.size(); i++) {
                WebElement value = options.get(i);
                assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));

            }
        }
    }

    // ********************* Private Methods *********************//

    private void waitForPageToLoad() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private void checkSessionList(List<WebElement> table, String eventName) {

        assertTrue("There were no " + eventName + " events found", sessionbrowserTab.isEventFound(eventName, table));
    }

    private void openRanEventsSessionList(String imsi) throws InterruptedException {
        sessionbrowserTab.openTab();
        waitForPageToLoad();
        sessionbrowserTab.openDetailsSessionBrowser2Hour(imsi);
        // sessionbrowserTab.openCustomDateAndTime("2012 Dec", "2012 Dec", "1",
        // "18");
        driver.findElement(By.xpath(Constants.RAN_SIGNALLING_BUTTON)).click();
        driver.findElement(By.xpath(Constants.UPDATE_BUTTON)).click();
        waitForPageToLoad();
    }

    private int getRecordPosition(String value, List<WebElement> table) {
        for (int i = 0; i < table.size(); i++) {
            String text = table.get(i).getText().trim();
            if (text.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private List<Boolean> checkViewDetalsSubSections() {

        List<WebElement> sessionList = driver.findElements(By
                .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div/div[2]"));
        List<Boolean> expandedSections = new ArrayList<Boolean>();

        for (WebElement e : sessionList) {
            expandedSections.add(e.getAttribute("style").isEmpty());
        }
        return expandedSections;
    }

}
