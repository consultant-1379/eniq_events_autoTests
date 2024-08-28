package com.ericsson.eniq.events.ui.selenium.tests.uertt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;

import java.util.List;

public class BaseUerttTest extends EniqEventsUIBaseSeleniumTest {

    static final String IMSI1 = "IMSI";
    static final String IMSI2 = "IMSI2";
    static final String IMSI_GROUP1 = "IMSI Group";

    protected final static String fileNameForImsi = "UerttReservedData.xls";

    protected static ReservedDataHelperReplacement reservedDataHelperReplacement;

    protected void checkHeadersAreOptionalAndHiddenByDefault(final CommonWindow window, final List<String> optionalHeadersToBeChecked,
                                                             final String... headerCheckBoxGroup) {
        List<String> headers = window.getTableHeaders();
        assertTrue("Columns doesnt match", headers.containsAll(optionalHeadersToBeChecked));
        window.openTableHeaderMenu(0);
        window.checkInOptionalHeaderCheckBoxes(optionalHeadersToBeChecked, headerCheckBoxGroup);
        checkStringListContainsArray(window.getTableHeaders(), optionalHeadersToBeChecked.toArray(new String[optionalHeadersToBeChecked.size()]));
    }

    protected void checkDataIntegrity(final String dataToCheck, final String expected, final String actual) {
        assertEquals(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED + " for " + dataToCheck, expected, actual);
    }
}
