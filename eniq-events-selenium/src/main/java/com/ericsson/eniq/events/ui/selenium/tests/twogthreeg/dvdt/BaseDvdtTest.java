/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.dvdt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;

import java.util.List;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class BaseDvdtTest extends EniqEventsUIBaseSeleniumTest {

    //down link data volume related headers
    static final String DOWNLINK_DATA_VOLOUME_KB = "Downlink Data Vol (KB)";

    static final String DOWNLINK_DATA_VOLUME_MB = "Downlink Data Vol (MB)";

    static final String DOWNLINK_THROUGHPUT_MB_SEC = "Downlink Throughput (Mb/s)";

    static final String DOWNLINK_THROUGHPUT_KB_SEC = "Downlink Throughput (Kb/s)";

    static final String DOWNLINK_AVERAGE_USAGES = "Downlink Avg Usage (%)";

    static final String DOWNLINK_AVERAGE_BEARER_VOLUME_KB = "Downlink Avg Bearer Volume (KB)";

    static final String DOWNLINK_PEAK_USAGE = "Downlink Peak Usage (%)";

    static final String TOTAL_DOWNLINK_DATA_VOL_MB = "Total DownLink Data Vol (MB)";

    static final String TOTAL_DOWNLINK_DATA_VOL_KB = "Total Downlink Data Vol (KB)";

    //uplink data volume related headers
    static final String UPLINK_DATA_VOLUME_KB = "Uplink Data Vol (KB)";

    static final String UPLINK_DATA_VOLUME_MB = "Uplink Data Vol (MB)";

    static final String UPLINK_THROUGHPUT_MB_SEC = "Uplink Throughput (Mb/s)";

    static final String UPLINK_THROUGHPUT_KB_SEC = "Uplink Throughput (Kb/s)";

    static final String UPLINK_AVERAGE_USAGE = "Uplink Avg Usage (%)";

    static final String UPLINK_AVERAGE_BEARER_VOLUME_KB = "Uplink Avg Bearer Volume (KB)";

    static final String UPLINK_PEAK_USAGE = "Uplink Peak Usage (%)";

    static final String TOTAL_UPLINK_DATA_VOL_MB = "Total UpLink Data Vol (MB)";

    static final String TOTAL_UPLINK_DATA_VOL_KB = "Total Uplink Data Vol (KB)";

    // other data volume realted headers  
    static final String PDP_DURATION = "PDP Duration";

    static final String BEARER_COUNT = "Bearer Count";

    static final String QOS_UPGRADE = "QOS Upgrade";

    static final String QOS_DOWNGRADE = "QOS Downgrade";

    static final String QOS_MEANTPUT = "QOS Meantput";

    static final String QOS_PEAKTPUT = "QOS Peaktput";

    static final String QOS_RELIABILITY = "QOS Reliability";

    static final String QOS_DELAY = "QOS Delay";

    static final String QOS_PRECEDENCE = "QOS Precedence";

    static final String CHARGING_ID = "Charging ID";

    static final String TOTAL_DATA_VOL_MB = "Total Data Vol (MB)";

    static final String TOTAL_DATA_VOL_KB = "Total Data Vol (KB)";

    static final String TOTAL_BEARER_COUNT = "Total Bearer Count";

    // other headers
    static final String TAC = "TAC";

    static final String TERMINAL_MAKE = "Terminal Make";

    static final String TERMINAL_MODEL = "Terminal Model";

    static final String MANUFACTURER = "Manufacturer";

    static final String MODEL = "Model";

    static final String TERMINAL_GROUP = "Terminal Group";

    static final String SGSN_MME = "SGSN-MME";

    static final String CONTROLLER = "Controller";

    static final String ACCESS_AREA = "Access Area";

    static final String APN = "APN";

    static final String APN_GROUP = "APN Group";

    static final String MCC = "MCC";

    static final String MNC = "MNC";

    static final String IMSI = "IMSI";

    static final String IMSI_GROUP = "IMSI Group";

    static final String GGSN = "GGSN";

    static final String SGSN = "SGSN";

    static final String SGSN_GROUP = "SGSN Group";

    static final String MSISDN = "MSISDN";

    static final String RANK = "Rank";

    static final String OPERATOR = "Operator";

    static final String COUNTRY = "Country";

    static final String PTMSI = "PTMSI";

    static final String EVENT_TYPE = "Event Type";

    // Groups of colum headers  
    static final String DV_THROUGHOUTPUT = "DV Throughput";

    //Time range to load reserved data should be 12 hour. 
    final TimeRange timeToLoadReservedData = TimeRange.TWELVE_HOURS;

    final static String fileNameFor2G3GReservedData = "2G3GDvdtReservedData.xls";

    static ReservedDataHelperReplacement reservedDataHelperReplacement;

    protected void checkHeadersAreOptionalAndHiddenByDefault(final CommonWindow window,
            final List<String> optionalHeadersToBeChecked, final String... headerCheckBoxGroup) {
        assertFalse(window.getTableHeaders().containsAll(optionalHeadersToBeChecked));
        // now we will tick check box of hidden headers to display them and 
        // this way is used to verify those headers are indeed existing as optional ones.
        window.openTableHeaderMenu(0);
        window.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked, headerCheckBoxGroup);
        checkStringListContainsArray(window.getTableHeaders(), optionalHeadersToBeChecked
                .toArray(new String[optionalHeadersToBeChecked.size()]));
    }

    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {
        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck, expected, actual);
    }
}
