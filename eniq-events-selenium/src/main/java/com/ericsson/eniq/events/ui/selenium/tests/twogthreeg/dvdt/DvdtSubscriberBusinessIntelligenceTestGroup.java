/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.core.charts.SubscriberBusinessIntelligenceChart;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.SubscriberOverviewWindow;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.logging.Level;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class DvdtSubscriberBusinessIntelligenceTestGroup extends BaseDvdtTest {
    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberOverview")
    private SubscriberOverviewWindow subOverviewWindow;

    @Autowired
    private SubscriberBusinessIntelligenceChart subChart;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of DvdtSubscriberBusinessIntelligenceTestGroup test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameFor2G3GReservedData);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of DvdtSubscriberBusinessIntelligenceTestGroup test section");
        reservedDataHelperReplacement = null;
    }

    // reserved values created by data gen simulator
    final String msisdn = reservedDataHelperReplacement.getReservedData(MSISDN);

    @Test
    public void openSubscriberOverviewFailuresViewBasedOnMSISDN_6_1() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        final String tileOfWindow = "Failure Event Analysis for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
        //        // get json response
        //        setTimeRangeToOneWeek(subOverviewWindow);
        //
        //        subChart.setOptionsOfURL(msisdn, ImsiMenu.MSISDN);
        //        final List<EventChartData> charElements = subChart.getAllEventChartData(TimeRange.getTimeRange(
        //                subOverviewWindow.getTimeRange()).getMiniutes());

        //        // verify toggle button - click it
        //        subOverviewWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        //        //verify data
        //        for (final Map<String, String> value : subOverviewWindow.getAllTableData()) {
        //            final String eventName = value.get("Event Name");
        //            final String eventFailures = value.get("Event Fairlues");
        //            final String eventSuccess = value.get("Event Success");
        //
        //            for (final EventChartData data : charElements) {
        //                if (data.getEventName().equals(eventName)) {
        //                    assertEquals(eventFailures, data.getNumberOfFailrue());
        //                    assertEquals(eventSuccess, data.getNumberOfSuccess());
        //                }
        //            }
        //        }
        //        // click toggle button again to go back to graph
        //        subOverviewWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        //
        //        // drill down any failure button and compare data
        //        subChart.processDrillDownChart(charElements.get(0).getEventName(), "Failures");
        //        waitForPageLoadingToComplete();
        //        assertTrue("Can't load a Subscriber Overview - Failure Analysis window", selenium
        //                .isTextPresent("Subscriber Overview - Failure Analysis"));
        //        //verify data
        //        assertEquals(charElements.get(0).getEventName(), subOverviewWindow.getAllDataAtTableRow(0).get("Event Type"));
    }

    @Test
    public void openSubscriberOverviewAPNUsageViewBasedOnMSISDN_6_2() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.APN_USAGE);
        final String tileOfWindow = "APN Event Summary for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));

        //        // verify toggle button - click it
        //        subOverviewWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        //        //TODO verify data
        //        // click toggle button again to go back to graph
        //        subOverviewWindow.clickButton(SelectedButtonType.TOGGLE_BUTTON);
        //
        //        // click Subscriber Details button
        //        subOverviewWindow.openSubscriberDetailsDialog();
        //        waitForPageLoadingToComplete();
        //        assertTrue("Can't load a Subscriber Details window", selenium.isTextPresent("Subscriber Details"));
        //
        //        subChart.setOptionsOfURL(msisdn, ImsiMenu.MSISDN);
        //        final List<String> chartElements = subChart.getChartValues(TimeRange.getTimeRange(
        //                subOverviewWindow.getTimeRange()).getMiniutes(), JSON_MEMBER_ELEMENT);
        //        subChart.processDrillDownChart(chartElements.get(0), "Failures");
        //        waitForPageLoadingToComplete();
        //        //TODO verify data
    }

    @Test
    public void openSubscriberOverviewBusyHourViewBasedOnMSISDN_6_3() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_HOUR);
        final String tileOfWindow = "Busy Hour Event Summary for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
    }

    @Test
    public void openSubscriberOverviewBusyDayViewBasedOnMSISDN_6_4() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.BUSY_DAY);
        final String tileOfWindow = "Busy Day Event Summary for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
    }

    @Test
    public void openSubscriberOverviewAccessAreaViewBasedOnMSISDN_6_5() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.ACCESS_AREA);
        final String tileOfWindow = "Access Area Event Summary for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
    }

    @Test
    public void openSubscriberOverviewTerminalViewBasedOnMSISDN_6_6() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.TERMINALS);
        final String tileOfWindow = "Terminal Analysis";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
    }

    @Test
    public void openSubscriberOverviewTAUViewBasedOnMSISDN_6_7() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.TAU);
        final String tileOfWindow = "TAU Event Analysis for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));
    }

    @Test
    public void openSubscriberOverviewHandoverViewBasedOnMSISDN_6_8() throws Exception {
        subscriberTab.openSubscriberOverviewWindow(ImsiMenu.MSISDN, msisdn);
        subOverviewWindow.clickViewSubMenu(SubscriberOverviewWindow.ViewMenu.HANDOVER);
        final String tileOfWindow = "Handover Event Analysis for Subscriber";
        assertTrue("Can't load a Subscriber Overview for " + tileOfWindow, selenium.isTextPresent(tileOfWindow));

    }
}
