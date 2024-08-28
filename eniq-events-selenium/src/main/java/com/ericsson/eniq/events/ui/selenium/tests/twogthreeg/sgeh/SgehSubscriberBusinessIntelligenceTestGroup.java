/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.charts.EventChartData;
import com.ericsson.eniq.events.ui.selenium.core.charts.SubscriberBusinessIntelligenceChart;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
import junit.framework.AssertionFailedError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eseuhon
 * @since 2010
 *
 */
public class SgehSubscriberBusinessIntelligenceTestGroup extends BaseSubscriber {

    @Autowired
    @Qualifier("subscriberOverview")
    private SubscriberOverviewWindow subOverviewWindow;

    @Autowired
    private SubscriberBusinessIntelligenceChart subChart;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberBusinessIntelligence test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberBusinessIntelligence test section");
    }

    /**
     * Test Case: 4.6.2 Display Subscriber BI details using IMSI
     * verify that Subscriber Details are displayed when a valid IMSI is provided
     */
    @Test
    public void displaySubscriberBIdetails_4_6_2() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        pause(1000);
        subOverviewWindow.openSubscriberDetailsDialog();
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Details window", selenium.isTextPresent("Subscriber Details"));

        final Map<String, String> dialogData = subOverviewWindow.getSubscriberDetailDialogData();
        final List<String> expectedContentsTitle = Arrays.asList(new String[] { "Home Country",
                "Mobile Network Operator", "Roaming Status", "Last Cell", "Last Routing Area", "Last SGSN",
                "First Event Date", "Last Event Date", "Last PTMSI" });

        for (final String title : expectedContentsTitle) {
            assertTrue(title + " is not displayed in Subscriber Details Dialog!", dialogData.keySet().contains(title));
        }
        assertSame(expectedContentsTitle.size(), dialogData.size());
    }

    /**
     * Test Case: 4.6.3 4.6.3 Subscriber BI details
     * Verify that Subscriber Business Intelligence is displayed when a valid IMSI is provided
     */
    @Test
    public void subscriberBIDetails_4_6_3() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
        final EventChartData data = listChartData.get(0);
        subChart.processDrillDownChart(data.getEventName(), "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", selenium
                .isTextPresent("Subscriber Overview - Failure Analysis"));

        //validate data
        assertSame(data.getNumberOfFailrue(), subOverviewWindow.getTableRowCount());
    }

    /**
     * Test Case:4.6.4
     * verify that subscriber business intelligence is displayed when a valid IMSI is provided
     */
    @Test
    public void invalidIMSIForSubscribeBIDetails_4_6_4() throws Exception {
        subscriberTab.openTab();
        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_OVERVIEW);
        assertTrue(selenium.isTextPresent("Missing or invalid input data"));
        subscriberTab.closeAllWindowsStart();

        subscriberTab.openTab();
        final String invalidImsi = "11";
        subscriberTab.enterSearchValue(invalidImsi, false);
        try {
            subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_OVERVIEW);
            waitForPageLoadingToComplete();
        } catch (final AssertionFailedError e) {
            assertEquals("Error occured in page loading - Please input a valid value", e.getMessage());
        }
    }

    /**
     * Test Case: 4.6.5
     * verify the recent failed event information displayed for a subscriber in the business intelligence feature 
     * when a valid IMSI is provided.
     */
    @Test
    public void displayRecentFailedEventsForSubscriber_4_6_5() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));

        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.FAILURES);

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
        final EventChartData data = listChartData.get(0);
        subChart.processDrillDownChart(data.getEventName(), "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", selenium
                .isTextPresent("Subscriber Overview - Failure Analysis"));
    }

    /**
     * Test Case: 4.6.6
     * verify the failure statistics displayed for a subscriber in the business intelligence 
     * when a valid IMSI is provided.
     */
    @Test
    public void displayFailureStatisticsForSubscriber_4_6_6() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
        final EventChartData data = listChartData.get(0);
        subChart.processDrillDownChart(data.getEventName(), "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", selenium
                .isTextPresent("Subscriber Overview - Failure Analysis"));

        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.FAILURES);

        for (final TimeRange time : TimeRange.values()) {
            subOverviewWindow.setTimeRange(time);
            waitForPageLoadingToComplete();
        }
    }

    /**
     * Test Case: 4.6.7
     * verify the data provided is based on the data minimum of the last 7 days. 
     */
    @Test
    public void dataToBeBasedOnLast7Days_4_6_7() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));

        subOverviewWindow.setTimeRange(TimeRange.ONE_WEEK);
        waitForPageLoadingToComplete();

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);
        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
        final EventChartData data = listChartData.get(0);
        subChart.processDrillDownChart(data.getEventName(), "Failures");
        waitForPageLoadingToComplete();
    }

    /**
     * Test Case: 4.6.8
     * verify that access area summary is displayed when a valid IMSI is provided.
     */
    @Test
    public void displayAccessAreaSummary_4_6_8() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.ACCESS_AREA);

        //TODO Action 4,5 and 6 are testing data based behavior (i.e. the most successful aceess area from the chart)
        //but we don't support this kinds of chart testing
        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
        final EventChartData data = listChartData.get(0);
        subChart.processDrillDownChart(data.getEventName(), "Failures");
        waitForPageLoadingToComplete();

        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.ACCESS_AREA);
        waitForPageLoadingToComplete();

        //Action 9 is not clear
        for (final TimeRange time : TimeRange.values()) {
            subOverviewWindow.setTimeRange(time);
            waitForPageLoadingToComplete();
        }
    }

    /**
     * Test Case: 4.6.10
     * verify that busy hour information displayed when a valid IMSI is provided        
     */
    @Test
    public void displayBusyhourOfTheDay_4_6_10() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_HOUR);

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);
        verityAllEventsAgainstTimeRange(SubscriberOverviewWindow.ViewMenu.BUSY_HOUR, TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()));

    }

    /**
     * Test Case: 4.6.11
     * verify that busy day information is displayed when a valid IMSI is provided
     */
    @Test
    public void displayBusydayOfTheWeek_4_6_11() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_DAY);

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);
        verityAllEventsAgainstTimeRange(SubscriberOverviewWindow.ViewMenu.BUSY_DAY, TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()));
    }

    /**
     * Test Case: 4.6.12
     * verify APN summary information displayed when a valid IMSI is provided.
     */
    @Test
    public void displayAPNAnalysisForTheSubscriber_4_6_12() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.APN_USAGE);

        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);
        verityAllEventsAgainstTimeRange(SubscriberOverviewWindow.ViewMenu.APN_USAGE, TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()));
    }

    /**
     * Test Case: 4.6.15
     * verify that when Terminal Analysis feature is installed, terminal analysis information is displayed 
     * for a subscriber in the business intelligence feature when a valid IMSI is provided
     */
    @Test
    public void displayTerminalAnalysis_4_6_15() throws Exception {
        //Note selenium is not able to run CLI to run License manger checking "Terminal Analysis" feature installed
        // i.e.>  licmgr -isvalid CXC4010926
        // instead, we assume this feafture is already installed
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.TERMINALS);

        assertTrue("Can't load a Subscriber Overview - Terminal Analysis window", selenium
                .isTextPresent("Subscriber Overview - Terminal Analysis"));
    }

    /**
     * Test Case: 4.6.16
     * verify terminal analysis information displayed when a valid IMSI is provided.
     */
    @Test
    public void display3GEvents_4_6_16() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);

        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());

        for (final EventChartData data : listChartData) {
            subChart.processDrillDownChart(data.getEventName(), "Failures");
            waitForPageLoadingToComplete();
            verify2Gor3GEventData(subOverviewWindow);
            subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.FAILURES);
        }
    }

    /**
     * Test Case: 4.6.17
     *  verify subscriber KPI information displayed for subscriber who uses both 2G and 3G data bearers 
     *  when a valid IMSI is provided.
     */
    @Test
    public void verifyKPIsVisibleFor2GAnd3GForASubscriber_4_6_17() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI, reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI));
        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_A,
                ImsiSpecificDataType.IMSI), ImsiMenu.IMSI);
        verifyFirst2Gor3GEventAgainstAllMenu();
    }

    /**
     * Test Case: 4.6.18
     * verify subscriber KPI information displayed for subscriber group who uses both 2G and 3G data bearers 
     * when a valid group is provided.
     */
    @Test
    public void verifyKPIsVisibleFor2Gand3GForSubscriberGroup_4_6_18() throws Exception {
        openSubscriberOverviewWindow(ImsiMenu.IMSI_GROUP, reservedDataHelper
                .getCommonReservedData(CommonDataType.IMSI_GROUP));
        setUrlParametersOfChartBeforeSendingAnyRequest(reservedDataHelper
                .getCommonReservedData(CommonDataType.IMSI_GROUP), ImsiMenu.IMSI_GROUP);
        verifyFirst2Gor3GEventAgainstAllMenu();
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void openSubscriberOverviewWindow(final ImsiMenu type, final String value) throws PopUpException {
        subscriberTab.openTab();
        pause(2000);
        subscriberTab.setSearchType(type);
        pause(2000);
        subscriberTab.enterSearchValue(value, type == ImsiMenu.IMSI_GROUP);
        pause(5000);
        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_OVERVIEW);
        waitForPageLoadingToComplete();
        assertTrue("Can't load a Subscriber Overview window", selenium.isTextPresent("Subscriber Overview"));
    }

    private void setUrlParametersOfChartBeforeSendingAnyRequest(final String value, final ImsiMenu menu) {
        subChart.setParametersToBeIncludedInURL(value, menu);
    }

    private void verityAllEventsAgainstTimeRange(final SubscriberOverviewWindow.ViewMenu menu, final TimeRange time)
            throws PopUpException, NoDataException {
        final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(time.getMiniutes());

        for (final EventChartData element : listChartData) {
            subChart.processDrillDownChart(element.getEventName(), menu.getDisplayName());
            waitForPageLoadingToComplete();
            //TODO verify more stuff based on data i.e. the number of events, event time 
            assertTrue("Can't load a Subscriber Overview window", selenium.isTextPresent("Subscriber Overview"));
            subOverviewWindow.clickViewSubMenu(menu);
        }
    }

    private void verifyFirst2Gor3GEventAgainstAllMenu() throws PopUpException, NoDataException {
        for (final SubscriberOverviewWindow.ViewMenu menu : SubscriberOverviewWindow.ViewMenu.values()) {
            //No 2G and 3G data displayed ? in Subscriber Overview - Terminal Analysis
            if (menu != SubscriberOverviewWindow.ViewMenu.TERMINALS) {
                subOverviewWindow.clickViewSubMenu(menu);
                final List<EventChartData> listChartData = subChart.getAllEventChartDataWithFailureNumbers(TimeRange
                        .getTimeRange(subOverviewWindow.getTimeRange()).getMiniutes());
                final EventChartData data = listChartData.get(0);
                //hopefully when we drill down the first event there will be 2G and 3G data in it.
                subChart.processDrillDownChart(data.getEventName(), menu.getDisplayName());
                waitForPageLoadingToComplete();
                verify2Gor3GEventData(subOverviewWindow);
            }
        }
    }

}
