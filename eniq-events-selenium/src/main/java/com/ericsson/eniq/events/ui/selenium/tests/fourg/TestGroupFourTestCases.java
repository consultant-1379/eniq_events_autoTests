/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author ekeviry/eantmcm
 * @since 2011
 * 
 */

public class TestGroupFourTestCases extends IMSIDataFromUI {

    protected IMSIDataFromDatabase dataFromDB = new IMSIDataFromDatabase();

    ArrayList<String> resultSetUI = new ArrayList<String>();

    ArrayList<String> resultSetDB = new ArrayList<String>();

    protected RegressionPropertiesFileReader propertiesReader = new RegressionPropertiesFileReader();

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.3: Event accuracy for
     * L_ATTACH and L_DETACH after storage Purpose: The purpose of this test is
     * to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLATTACHAfterStorage_4_3_A() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_ATTACH", 5);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.3: Event accuracy for
     * L_ATTACH and L_DETACH after storage Purpose: The purpose of this test is
     * to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLDETACHAfterStorage_4_3_B() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_DETACH", 6);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.4: Event accuracy for
     * L_SERVICE_REQUEST after storage Purpose: The purpose of this test is to
     * make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLSERVICEREQUESTAfterStorage_4_4() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_SERVICE_REQUEST", 13);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.5: Event accuracy for
     * L_HANDOVER and L_TAU after storage Purpose: The purpose of this test is
     * to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLHANDOVERAfterStorage_4_5_A() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_HANDOVER", 7);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.5: Event accuracy for
     * L_HANDOVER and L_TAU after storage Purpose: The purpose of this test is
     * to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLTAUAfterStorage_4_5_B() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_TAU", 8);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.6: Event accuracy for
     * L_PDN_CONNECT and L_PDN_DISCONNECT after storage Purpose: The purpose of
     * this test is to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLPDNCONNECTAfterStorage_4_6_A() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_PDN_CONNECT", 11);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.6: Event accuracy for
     * L_PDN_CONNECT and L_PDN_DISCONNECT after storage Purpose: The purpose of
     * this test is to make sure all raw event data is loaded accurately
     */
    @Test
    public void eventAccuracyForLPDNCONNECTDISCONNECTAfterStorage_4_6_B() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_PDN_DISCONNECT", 12);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.7: Event accuracy for
     * L_DEDICATED_BEARER_ACTIVATE and L_DEDICATED_BEARER_DEACTIVATE after
     * storage Purpose: The purpose of this test is to make sure all raw event
     * data is loaded accurately
     */
    @Test
    public void eventAccuracyForLDEDICATEDBEARERACTIVATEAfterStorage_4_7_A() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_DEDICATED_BEARER_ACTIVATE", 9);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.7: Event accuracy for
     * L_DEDICATED_BEARER_ACTIVATE and L_DEDICATED_BEARER_DEACTIVATE after
     * storage Purpose: The purpose of this test is to make sure all raw event
     * data is loaded accurately
     */
    @Test
    public void eventAccuracyForLDEDICATEDBEARERDEACTIVATEAfterStorage_4_7_B() throws Exception {

        eventAccuracyForEventTypeAfterStorage("L_DEDICATED_BEARER_DEACTIVATE", 10);
    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.8: Handling of 4G
     * L_ATTACH and L_DETACH Events with different node versions Purpose: The
     * Purpose of this Test is to make sure all raw event data is loaded
     * accurately for different node types
     */
    // @Test
    public void handlingOf4GLATTACHAndLDETACHEventsWithDifferentNodeVersions_4_8() throws Exception {

        // Functionality to be added

    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.9: Handling of 4G
     * L_SERVICE_REQUEST Event with different node versions Purpose: The Purpose
     * of this Test is to make sure all raw event data is loaded accurately for
     * different node types
     */
    // @Test
    public void handlingOf4GLSERVICEREQUESTEventWithDifferentNodeVersions_4_9() throws Exception {

        // Functionality to be added

    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.10: Handling of 4G
     * L_HANDOVER and L_TAU Event with different node versions Purpose: The
     * Purpose of this Test is to make sure all raw event data is loaded
     * accurately for different node types
     */
    // @Test
    public void handlingOf4GLHANDOVERAndLTAUEventWithDifferentNodeVersions_4_10() throws Exception {

        // Functionality to be added

    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_4.11: Handling of 4G
     * L_PDN_CONNECT and L_PDN_DISCONNECT Event with different node versions
     * Purpose: The Purpose of this Test is to make sure all raw event data is
     * loaded accurately for different node types
     */
    // @Test
    public void handlingOf4GLPDNCONNECTAndLPDNDISCONNECTEventWithDifferentNodeVersions_4_11() throws Exception {

        // Functionality to be added

    }

    /**
     * Requirement: 105 65-0528/00386 Test Case: 4G_EE_3.4: Event accuracy for
     * Event Type after storage Purpose: The purpose of this test is to make
     * sure all raw event data is loaded accurately
     */
    // @Test
    public void RunIndividualEventType_3_4() throws Exception {

        final String imsiValue = propertiesReader.readIMSIValue();

        final String eventTypeValue = propertiesReader.EventTypeValue();

        final int eventTypeNum = propertiesReader.EventTypeNum();

        openIMSIEventAnalysisWindow(imsiValue, true);

        resultSetUI = searchSubscriberEventType(eventTypeValue);

        resultSetDB = dataFromDB.getAllIMSIDataFromDB(imsiValue, eventTypeNum);

        if (resultSetUI.size() > resultSetDB.size()) {
            resultSetUI = trimBiggerResultSet(resultSetUI, resultSetDB.size());
        }

        if (resultSetDB.size() > resultSetUI.size()) {
            resultSetDB = trimBiggerResultSet(resultSetDB, resultSetUI.size());
        }

        assertFalse("No " + eventTypeValue + " events found for IMSI " + imsiValue, resultSetUI.size() == 0
                && resultSetDB.size() == 0);

        final ArrayList<String> failList = verifyUIEventValuesAgainstDBEventValues(resultSetDB, resultSetUI);

        assertTrue("Non-matching values identified for IMSI " + imsiValue, failList.size() == 0);
    }

    // //////////////////////////////////////////////////////
    // PRIVATE METHODS
    // //////////////////////////////////////////////////////

    private void eventAccuracyForEventTypeAfterStorage(final String eventType, final int eventTypeID)
            throws FileNotFoundException, IOException, NoDataException, PopUpException, ParseException {

        final String imsiValue = propertiesReader.readIMSIValue();

        openIMSIEventAnalysisWindow(imsiValue, false);

        resultSetUI = searchSubscriberEventType(eventType);

        resultSetDB = dataFromDB.getAllIMSIDataFromDB(imsiValue, eventTypeID);

        System.out.println("UI Size: " + resultSetUI.size());
        System.out.println("DB Size: " + resultSetDB.size());

        if (resultSetUI.size() > resultSetDB.size()) {
            resultSetUI = trimBiggerResultSet(resultSetUI, resultSetDB.size());
        }

        if (resultSetDB.size() > resultSetUI.size()) {
            resultSetDB = trimBiggerResultSet(resultSetDB, resultSetUI.size());
        }

        System.out.println("UI Size: " + resultSetUI.size());
        System.out.println("DB Size: " + resultSetDB.size());

        assertFalse("No L_DETACH events found for IMSI " + imsiValue, resultSetUI.size() == 0
                && resultSetDB.size() == 0);

        final ArrayList<String> failList = verifyUIEventValuesAgainstDBEventValues(resultSetDB, resultSetUI);

        assertTrue("Non-matching values identified for IMSI " + imsiValue, failList.size() == 0);

    }

    private ArrayList<String> trimBiggerResultSet(final ArrayList<String> biggerResultSet,
            final int numberOfEventsSmallerSet) {
        final ArrayList<String> trimmedResultSet = new ArrayList<String>();

        for (int i = 0; i < numberOfEventsSmallerSet; i++) {
            trimmedResultSet.add(biggerResultSet.get(i));
        }

        return trimmedResultSet;
    }

    private ArrayList<String> verifyUIEventValuesAgainstDBEventValues(final ArrayList<String> eventTypeD,
            final ArrayList<String> eventTypeU) {
        final ArrayList<String> list = new ArrayList<String>();

        assertTrue("Differing number of events in UI and DB", eventTypeD.size() == eventTypeU.size());

        for (int i = 0; i < eventTypeD.size(); i++) {

            if (eventTypeD.get(i) == null) {
                eventTypeD.set(i, "");
            }

            // if (eventTypeU.get(i) == null) {
            // eventTypeU.set(i, "");
            // }

            if (!eventTypeU.get(i).equals((eventTypeD.get(i)))) {
                list.add(eventTypeD.get(i));
                logger.log(Level.SEVERE, "Non-matching DB Entry: " + eventTypeD.get(i) + "\nNon-matching UI Entry: "
                        + eventTypeU.get(i));
            } else {
                // System.out.println("Pass DB: " + eventTypeD.get(i));
            }
        }
        return list;
    }
}
