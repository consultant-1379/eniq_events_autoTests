/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class BaseSubscriber extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subRankings")
    protected CommonWindow subRankingsWindow;

    public String getDataFromSubscriberRankingsWindow(final String tableHeader, final TimeRange time)
            throws NoDataException, PopUpException {
        subscriberTab.openTab();
        subscriberTab.openStartMenu(SubscriberTab.StartMenu.SUBSCRIBER_RANKINGS);
        waitForPageLoadingToComplete();

        //change time to 1 week - try best to retrieve all imsi data in case no data by defalut(30 mins)
        subRankingsWindow.setTimeRange(time);
        waitForPageLoadingToComplete();
        final String value = subRankingsWindow
                .getTableData(0, subRankingsWindow.getTableHeaders().indexOf(tableHeader));
        logger.info(tableHeader + " : " + value);
        subRankingsWindow.closeWindow();
        return value;
    }

}
