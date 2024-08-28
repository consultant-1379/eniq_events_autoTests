/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2015 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.details;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.Constants;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class DBSUserPaneTestGroup extends SBWebDriverBaseUnitTest {

    private final String DATA_BEARER_SESSION_EVENT = "Data Bearer Session";

    @Autowired
    private SessionBrowserTab sessionbrowserTab;

    /**
     * US: DEFTFTS-147 Test Case: 7.76 Verify that a user is able to see Data Bearer Session Event (User Plane a Application Performance
     * TCPTA-Partial) on the Session List entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Test
    public void userAbleToSeeDBSEvent_7_76() throws InterruptedException, ParseException {

        //Action 1
        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        //Action 2
        final Boolean eventFound = isRanEventFound(DATA_BEARER_SESSION_EVENT);
        assertTrue("No data found for selected IMSI", eventFound);

    }

    /**
     * US: DEFTFTS-147 Test Case: 7.77 Verify that a user is able to launch View Details Window for Data Bearer session Event (User Plane –
     * Application Performance TCPTA-Partial) on the Session List entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void launchDBSEvent_7_77() throws InterruptedException, PopUpException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        WebElement value;

        final List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));
        assertFalse("Session List is empty", sessionTable.isEmpty());

        final int index = sessionbrowserTab.getRecordPosition(DATA_BEARER_SESSION_EVENT, sessionTable);
        assertTrue("Data Bearer Session Event not found in list", index >= 0);

        final List<String> expectedOptions = Arrays.asList("Reports", "View Details");
        String DBSEventPath = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[@__index='" + (index) + "']";

        driver.findElement(By.xpath(DBSEventPath + "/td[2]")).click();
        final List<WebElement> options = driver.findElements(By.xpath(Constants.DROPDOWN_MENU_CONTENT));

        //Action 1
        for (int i = 0; i < options.size(); i++) {
            value = options.get(i);
            assertTrue("The value " + value.getText() + " was not found in the drop down menu", expectedOptions.get(i).equals((value.getText())));
        }

        //Action 2
        driver.findElement(By.xpath(Constants.VIEW_DETAILS)).click();
        Thread.sleep(1000);
        assertTrue("Clicking on view details does not launch separate window",
                driver.findElement(By.xpath("//div[contains(@class,'dragdrop-handle')]")).isDisplayed());

        //Action 3
        final WebElement titleBar = driver.findElement(By
                .xpath("//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']//div[contains(@class,'dragdrop-handle')]/div"));
        assertTrue("Window title does not match Event name in session list", titleBar.getText().contains(DATA_BEARER_SESSION_EVENT));

        final String sessionTimeOnList = driver.findElement(By.xpath(DBSEventPath + "/td[1]")).getText();
        assertTrue("Window time does not match Event time in session list", titleBar.getText().contains(sessionTimeOnList));
    }

    /**
     * US: DEFTFTS-147 Test Case: 7.78 Verify the Sections of the View Details Window for Data Bearer Session Event (User Plane – Application
     * Performance TCPTA-Partial) on the Session List entry panel in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws PopUpException
     * @throws ParseException
     */
    @Test
    public void verifySectionsViewDetails_7_78() throws InterruptedException, PopUpException, ParseException {

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        final List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));
        final int index = RAN_SignallingPosition(sessionTable, DATA_BEARER_SESSION_EVENT);

        if (index >= 0) {
            openViewDetails(index);

            final List<String> expectedValues = Arrays.asList("Subscriber Identity", "Network Location", "Session Properties", "Radio Conditions",
                    "Mobility", "Traffic Channel Usage", "Application Performance", "Application Traffic Mix");
            final List<String> actualValues = getViewDetailsHeadings();

            //Action 1
            assertTrue(expectedValues.equals(actualValues));

            //Action 2
            assertTrue("Scroll bar not positioned at the top of the session list pane", sessionbrowserTab.getSessionListScrollBarPosition() == 0);
            scrollViewDetailsSectionList(true, 100);
            final List<WebElement> visibleSections = driver.findElements(By
                    .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div"));

            //Action 4
            for (int i = 1; i < visibleSections.size(); i++) {
                System.out.println("Section A: " + i);
                final boolean visible = driver
                        .findElement(
                                By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                        + i + "]/div[2]")).getAttribute("style").isEmpty();
                scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[" + i
                                + "]//span[2]")));
                if (visible) {
                    //Section is expanded so collapse it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertFalse(
                            "The expanded heading did not collapse : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2]")).getAttribute("style").isEmpty());

                } else {
                    //Section is collapse so expand it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                            .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[" + i
                                    + "]/div[2][@style='']")));
                    assertTrue(
                            "The collapsed heading did not expand : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2][@style='']")).isDisplayed());
                }
            }

            scrollViewDetailsSectionList(true, 100);
            for (int i = 1; i <= visibleSections.size(); i++) {
                System.out.println("Section B: " + i);
                final boolean visible = driver
                        .findElement(
                                By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                        + i + "]/div[2]")).getAttribute("style").isEmpty();
                scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                        .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[" + i
                                + "]//span[2]")));
                if (visible) {
                    //Section is expanded so collapse it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    assertFalse(
                            "The expanded heading did not collapse : " + i,
                            driver.findElement(
                                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                            + i + "]/div[2]")).getAttribute("style").isEmpty());

                } else {
                    //Section is collapse so expand it
                    driver.findElement(
                            By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                    + i + "]//span[2]")).click();
                    if (checkDataExists(i)) {
                        scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                                .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                        + i + "]/div[2][@style='']")));
                        assertTrue(
                                "The collapsed heading did not expand : " + i,
                                driver.findElement(
                                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div["
                                                + i + "]/div[2][@style='']")).isDisplayed());
                    }
                }
            }
            //Action 5 - Covered in DEFTFTS-13

        }
    }

    /**
     * DEFTFTS-147 Verify the Application Performance Section of the View Details Window for Data Bearer Session Event (User Plane and Application
     * Performance TCPTA-Partial) in the Session Browser Details Tab.
     *
     * @throws InterruptedException
     * @throws ParseException
     */
    @Ignore
    @Test
    public void testApplicationPerformanceLabels() throws InterruptedException, ParseException {
        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        final List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("Data Bearer Sessions are not displayed", sessionbrowserTab.isEventFound(DATA_BEARER_SESSION_EVENT, sessionTable));

        final int index = sessionbrowserTab.getRecordPosition(DATA_BEARER_SESSION_EVENT, sessionTable);

        openViewDetails(index);

        final WebElement viewDetailsWindow = driver.findElement(By.xpath(Constants.VIEW_DETAILS_WINDOW));

        final WebElement applicationPerformance = driver.findElement(By.xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE));
        scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By.xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE)));
        assertTrue(applicationPerformance.getText().equals("Application Performance"));

        ensureAppPerformanceSectionIsVisible();
        scrollViewDetailsSectionList(false, 15);
        final WebElement applicationPerformanceSummaryGrid = driver.findElement(By
                .xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE_SUMMARY_GRID));

        final List<WebElement> summaryGridDetails = applicationPerformanceSummaryGrid.findElements(By
                .xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE_SUMMARY_GRID + "/tr/td[1]/div"));

        final List<String> expectedValues = Arrays.asList("Downlink Volume (Bytes)", "Uplink Volume (Bytes)", "RTT (UE <-> Server) (ms)",
                "RTT (UE <-> Gn) (ms)", "RTT (Gn <-> Server) (ms)", "Packet loss (UE <-> Server)", "Packet loss (UE <-> Gn)",
                "Packet loss (Gn <-> Server)");

        for (int i = 0; i < summaryGridDetails.size(); i++) {
            final String text = summaryGridDetails.get(i).getText().trim();
            assertTrue("Value:  " + text + " does not match expected field:  " + expectedValues.get(i), expectedValues.get(i).equals(text));
        }

        scrollViewDetailsSectionListUntilElementVisible(false,
                viewDetailsWindow.findElement(By.xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_TRAFFIC_MIX)));
        final WebElement applicationTrafficMix = viewDetailsWindow.findElement(By.xpath(Constants.DBS_DETAIL_VIEW_WINDOW_APPLICATION_TRAFFIC_MIX));

        final String text = applicationTrafficMix.getText();
        assertTrue("Value: " + text + " does not match expected field: Application Traffic Mix ", text.equals("Application Traffic Mix"));

    }

    /**
     * DEFTFTS-147 Verify the Application Performance Section of the View Details Window for Data Bearer Session Event (User Plane and Application
     * Performance TCPTA-Partial) in the Session Browser Details Tab. Action 2: Check Session Properties Details attributes in view: Downlink Volume
     * (BYTES_DOWNLINK in table EVENT_E_USER_PLANE_CLASSIFICATION_RAW), Uplink Volume (BYTES_UPLINK in table EVENT_E_USER_PLANE_CLASSIFICATION_RAW),
     * RTT (UE <-> Server), RTT (UE <-> Gn), RTT (UE <-> Server), Packet loss (UE <-> Server), Starting Packet loss (UE<-> Gn), Packet loss (Gn <->
     * Server) and that they match values in database table EVENT_E_USER_PLANE_TCP_RAW) Result 2: Data in Event Details and match database.
     *
     * @throws Exception
     */

    @Test
    public void testApplicationPerformanceValues() throws Exception {
        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        final List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("Data Bearer Sessions are not displayed", sessionbrowserTab.isEventFound(DATA_BEARER_SESSION_EVENT, sessionTable));

        final int index = sessionbrowserTab.getRecordPosition(DATA_BEARER_SESSION_EVENT, sessionTable);

        openViewDetails(index);
        scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[7]/div[1]/span[1]")));
        final WebElement applicationPerformance = driver.findElement(By
                .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[7]/div[1]/span[1]"));
        assertTrue(applicationPerformance.getText().equals("Application Performance"));

        scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[1]/span[1]")));
        final WebElement applicationTrafficMix = driver.findElement(By
                .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[1]/span[1]"));
        assertTrue(applicationTrafficMix.getText().equals("Application Traffic Mix"));

        /**
         * DATA INTEGITY - NOT CURRENTLY REQUIRED IN UI TESTING
         * */
        /*
         * // final List<WebElement> summaryGridDetails = driver // .findElements(By //
         * .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[7]/div[2]/table/tbody/tr/td[2]/div")); //
         * final List<WebElement> applicationTrafficMixGridDetails = driver // .findElements(By //
         * .xpath("//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[2]/table/tbody/tr/td/div")); final
         * String tcpQuery = "SELECT " + "CAST(AVG(SETUP_TIME_TERM) AS NUMERIC(16,4))      AS RTT_UE_GN,  " +
         * "CAST(AVG(SETUP_TIME_NET) AS NUMERIC(16,4))       AS RTT_GN_SERVER, " +
         * "RTT_UE_GN+RTT_GN_SERVER                          AS RTT_UE_SERVER, " +
         * "CAST(AVG(PACKET_LOSS_TERM)*100 AS NUMERIC(16,4)) AS PACKET_LOSS_UE_GN, " +
         * "CAST(AVG(PACKET_LOSS_NET)*100 AS NUMERIC(16,4))  AS PACKET_LOSS_GN_SERVER, " +
         * "PACKET_LOSS_UE_GN+PACKET_LOSS_GN_SERVER          AS PACKET_LOSS_UE_SERVER " + "FROM (SELECT SETUP_TIME_TERM, " +
         * "             SETUP_TIME_NET, " + "             PACKET_LOSS_TERM, " + "             PACKET_LOSS_NET " +
         * "        FROM dc.EVENT_E_USER_PLANE_TCP_RAW " + "       WHERE FIVE_MIN_AGG_TIME = '" + sessionStart + "' " + "         AND IMSI = " + imsi
         * +") as tcpRaw";
         *
         * final String trafficQuery =
         * "SELECT ISNULL(appRef.FUNCTION_DESC,'Unclassified') as FUNCTION_DESC, TRAFFIC_VOL_DL+TRAFFIC_VOL_UL as TRAFFIC FROM ( " + "(SELECT " +
         * "FUNCTION, " + "SUM(ISNULL(classRaw.BYTES_DOWNLINK, 0)) AS TRAFFIC_VOL_DL, " + "SUM(ISNULL(classRaw.BYTES_UPLINK,0))    AS TRAFFIC_VOL_UL "
         * + "FROM " + "dc.EVENT_E_USER_PLANE_CLASSIFICATION_RAW classRaw " + "WHERE " + "classRaw.FIVE_MIN_AGG_TIME = '" + sessionStart + "' " +
         * "AND classRaw.IMSI = " + imsi + " " + "GROUP BY " + "FUNCTION " + ") as classRaw " + "LEFT OUTER JOIN dc.DIM_E_USER_PLANE_FUNCTION appRef "
         * + "ON classRaw.FUNCTION = appRef.FUNCTION )";
         *
         * final String dataVolumeQuery = "SELECT " + "SUM(BYTES_DOWNLINK) AS DL_VOL, " + "SUM(BYTES_UPLINK)   AS UL_VOL " +
         * "FROM dc.EVENT_E_USER_PLANE_CLASSIFICATION_RAW " + "WHERE FIVE_MIN_AGG_TIME = '2012-05-17 08:30:00.000' " + "  AND IMSI = " + imsi + " ";
         *
         * try { database.openConnection();
         *
         * ResultSet result = database.executeQuery(dataVolumeQuery); result.next(); for (int i = 1; i <= 2; i++) { Object value =
         * result.getObject(i); if (value == null) { value = "-"; }
         * assertTrue(value.toString().trim().equals(summaryGridDetails.get(i-1).getText().trim())); } result.close();
         *
         * result = database.executeQuery(tcpQuery); result.next(); for (int i = 1; i <= 5; i++) { Object value = result.getObject(i); if (null ==
         * value) { value = "-"; } assertTrue(value.toString().trim().equals(summaryGridDetails.get(i+1).getText().trim())); } result.close();
         *
         *
         * final Map<String, String> trafficMixGridMap = new HashMap<String, String>(); final int trafficMixGridSize =
         * applicationTrafficMixGridDetails.size(); for (int i = 0; i < trafficMixGridSize; i+=2) { final String trafficType =
         * applicationTrafficMixGridDetails.get(i).getText(); trafficMixGridMap.put(trafficType, applicationTrafficMixGridDetails.get(i+1).getText());
         * }
         *
         * result = database.executeQuery(trafficQuery); while (result.next()) { int index = 1; final String trafficType = result.getString(index);
         * index++; assertTrue(result.getString(index).equals(trafficMixGridMap.get(trafficType))); } result.close(); } finally {
         * database.closeConnection(); }
         */
    }

    /**
     * 4.7.83 EE13.0_SESSB&KPI_7.83: Verify the Application Traffic Mix Section of the View Details Window for Data Bearer Session Event (User Plane a
     * Application Performance TCPTA-Partial) in the Session Browser Details Tab.
     *
     * @throws Exception
     */
    @Test
    public void testApplicationTrafficMix_7_83() throws Exception {

        openSessionDetail(CommonDataType.SB_IMSI_DATA_BEARER_SESSION, true, true);

        final List<WebElement> sessionTable = driver.findElements(By.xpath(Constants.SESSION_TABLE));

        assertTrue("Data Bearer Sessions are not displayed", sessionbrowserTab.isEventFound(DATA_BEARER_SESSION_EVENT, sessionTable));

        final int index = sessionbrowserTab.getRecordPosition(DATA_BEARER_SESSION_EVENT, sessionTable);

        openViewDetails(index);

        scrollViewDetailsSectionListUntilElementVisible(false,
                driver.findElement(By.xpath("//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[1]/span[1]")));
        final WebElement applicationTrafficMix = driver.findElement(By
                .xpath("//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[1]/span[1]"));
        assertTrue(applicationTrafficMix.getText().equals("Application Traffic Mix"));

        //Note: The labels Application Traffix Mix section can verify depending on data from services

    }

    private boolean checkDataExists(int i) {

        int rows = driver.findElements(
                By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[" + i
                        + "]/div[2]/table/tbody/tr")).size();
        System.out.println(i);
        return (rows > 0);
    }

    private void ensureAppPerformanceSectionIsVisible() {
        final boolean visible = driver
                .findElement(
                        By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[7]/div[2]"))
                .getAttribute("style").isEmpty();

        if (!visible) {
            scrollViewDetailsSectionListUntilElementVisible(false, driver.findElement(By
                    .xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[7]//span[2]")));
            driver.findElement(
                    By.xpath("//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div[7]//span[2]"))
                    .click();
        }
    }
}
