package com.ericsson.eniq.events.ui.selenium.tests.ltehfa;

import com.ericsson.eniq.events.ui.selenium.common.CsvReader;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.db.DBPersistor;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class DataIntegrityValidationUtil
{
    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    final static DBPersistor dbPersistor = DBPersistor.getInstatnce();

    private List<String> listOfEventTypesInResvData = new ArrayList<String>();
    private List<String> listOfHandoverPreparationEvents = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.PREP_S1_IN, GuiStringConstants.PREP_S1_OUT, GuiStringConstants.PREP_X2_IN, GuiStringConstants.PREP_X2_OUT));
    private List<String> listOfHandoverExecutionEvents = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EXEC_S1_IN, GuiStringConstants.EXEC_S1_OUT, GuiStringConstants.EXEC_X2_IN, GuiStringConstants.EXEC_X2_OUT));



    public final String LOCAL_FILE_PATH_OF_LTERESERVED_DATA = "E:/eeirmen/eeirmen_snap2/eniq_events/auto_tests/eniq-events-selenium/src/main/resources/reserveDataLTEHFA.csv";
    public final String UNIX_FILE_PATH_OF_LTERESERVED_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reserveDataLTEHFA.csv";
    public final String LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA =  "E:/eeirmen/eeirmen_snap2/eniq_events/auto_tests/eniq-events-selenium/src/main/resources/reserveDataErabLTEHFA.csv";
    public final String UNIX_FILE_PATH_OF_LTERESERVED_ERAB_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reserveDataErabLTEHFA.csv";

    public enum FailureType {CALL_SETUP_FAILURES, CALL_DROPS, RECURRING_FAILURES};

    final List<String> headersOnIMSIFailedEventAnalysisWindow_PREP_X2_IN
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
             GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.HFA_NUMBER_OF_ERABS, GuiStringConstants.FAILURE_REASON,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA,
            GuiStringConstants.MCC, GuiStringConstants.MNC, GuiStringConstants.Duration, GuiStringConstants.SOURCE_TYPE,
            GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SUBSCRIBER_PROFILE_ID));


    final List<String> headersOnIMSIFailedEventAnalysisWindow_PREP_X2_OUT
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR, GuiStringConstants.CAUSE_3GPP_HFA,
            GuiStringConstants.CAUSE_GROUP_3GPP_HFA,GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE,
            GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT, GuiStringConstants.HO_TYPE));


    final List<String> headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_IN
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.Duration, GuiStringConstants.RANDOM_ACCESS_TYPE,GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.PACKET_FORWARD, GuiStringConstants.HO_TYPE));

    final List<String> headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_OUT
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.Duration, GuiStringConstants.TARGET_TYPE,
            GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX, GuiStringConstants.PACKET_FORWARD,  GuiStringConstants.HO_TYPE));

    final List<String> headersOnIMSIFailedEventAnalysisWindow_PREP_S1_IN
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA,GuiStringConstants.FAILURE_REASON,
            GuiStringConstants.MCC, GuiStringConstants.MNC, GuiStringConstants.Duration,
            GuiStringConstants.SOURCE_TYPE, GuiStringConstants.RANDOM_ACCESS_TYPE, GuiStringConstants.SUBSCRIBER_PROFILE_ID));

    final List<String> headersOnIMSIFailedEventAnalysisWindow_PREP_S1_OUT
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.CAUSE_3GPP_HFA, GuiStringConstants.CAUSE_GROUP_3GPP_HFA, GuiStringConstants.SRVCC_TYPE,
            GuiStringConstants.Duration,GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.HO_ATTEMPT));


    final List<String> headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_IN
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.SOURCE_TYPE, GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.RANDOM_ACCESS_TYPE));


    final List<String> headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_OUT
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURE_REASON, GuiStringConstants.SOURCE_CONTROLLER, GuiStringConstants.TARGET_CONTROLLER,
            GuiStringConstants.SOURCE_ACCESS_AREA, GuiStringConstants.TARGET_ACCESS_AREA, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.Duration, GuiStringConstants.HFA_NUMBER_OF_ERABS,
            GuiStringConstants.TARGET_TYPE, GuiStringConstants.SELECTION_TYPE, GuiStringConstants.CONFIG_INDEX));

    final List<String> headersOnPrepERABEventAnalysisWindow
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE, GuiStringConstants.SETUP_REQ_PCI,GuiStringConstants.SETUP_REQ_PVI,
            "HO Prep ERAB Result", "HO Prep ERAB Request","Setup Request ARP", "Prep QCI"));

    final List<String> headersOnExecERABEventAnalysisWindow
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE, GuiStringConstants.SETUP_REQ_PCI,GuiStringConstants.SETUP_REQ_PVI,  "Exec QCI"));

    private List<Map<String, String>> reserveDataList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> reserveERABdataList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> eventVolReserveDataList = new ArrayList<Map<String, String>>();
    private Map<String, List<Map<String, String>>> drillDownResvDataMap = new HashMap<String, List<Map<String, String>>>();
    private List<Map<String, String>> qciFailureStatsList = new ArrayList<Map<String, String>>();

    List<Map<String, String>> topologyInformation;
    List<Map<String, String>> ranInformation;
    List<Map<String, String>> tacInformation;

    // Initialize Date
    public Date gStartDate = new Date();
    public String gStartDateTimeCandidate = "";
    public Date gEndDate = new Date();
    public String gEndDateTimeCandidate = "";

    int maxSubscribers = 0;
    String failCount = " Fail Count";


    /////////////////////////////////////////////////////////////////////////////
    //   P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Description: Initialises DataIntegrityCheckUtil by loading reserve data during startup.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void init(ReservedDataHelper reservedDataHelper)
    {
        updateTopologyChanges();
        loadERABcsvData();

        final String ver12Asupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_12A_SUPPORTED);
        final String ver11Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_11B_SUPPORTED);
        final String ver12Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_12B_SUPPORTED);
        final String ver13Asupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_13A_SUPPORTED);
        final String ver13Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_13B_SUPPORTED);

        if (ver12Asupported.equals("yes"))
        {
            try
            {
                loadCsvData("12A", reservedDataHelper);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver11Bsupported.equals("yes"))
        {
            try
            {
                loadCsvData("11B", reservedDataHelper);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver12Bsupported.equals("yes"))
        {
            try
            {
                loadCsvData("12B", reservedDataHelper);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver13Asupported.equals("yes"))
        {
            try
            {
                loadCsvData("13A", reservedDataHelper);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver13Bsupported.equals("yes"))
        {
            try
            {
                loadCsvData("13B", reservedDataHelper);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //                               SUBSCRIBER TAB                                //
    //////////////////////////////////////////////////////////////////////////////

    /**
     * Description: To validate data displayed on IMSI Event Analysis window
     * @param imsi
     * @param timeInMinutes
     * @param imsiEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     */
    public boolean validateIMSIEventAnalysisWindowData(String imsi, String handoverStage, int timeInMinutes, List<Map<String, String>> imsiEventFailureList) throws NoDataException, PopUpException
    {
        if (imsiEventFailureList.size() == 0)
        {
            logger.log(Level.SEVERE, "NO DATA FOUND");
            return false;
        }

        checkEventTypesConfiguredInResvData(imsi, handoverStage);

        logger.log(Level.INFO, "VALIDATE DATA for TimeRange " + timeInMinutes);

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiEventFailureList)
        {
            // Check if row is empty
            if (eventFailureMap.isEmpty())
            {
                logger.log(Level.SEVERE, "IMSI Event Failure Analysis Window Map is empty");
                return false;
            }

            String eventType = eventFailureMap.get(GuiStringConstants.EVENT_TYPE);
            String failures = eventFailureMap.get(GuiStringConstants.FAILURES);
            int uiFailureCount = Integer.parseInt(failures);

            // Get the failure count of a given EventType configured in the reserved data.
            int resvDataFailureCount = checkNoOfFailuresForEventType(imsi, eventType);

            // Check how many failure events should be generated at a given time range
            int totalFailuresExpected = resvDataFailureCount * (timeInMinutes/5);

            logger.log(Level.INFO, "IMSI Event Analysis Window - Event Type : " + eventType +
                    " Failures : " + uiFailureCount + ". And Number of Failures expected is " + totalFailuresExpected);

            // Check if failure count of a given EventType is not matching with the failure count in reserved data
            if (uiFailureCount != totalFailuresExpected)
            {
                logger.log(Level.SEVERE, "FAILURES count does not match");
                return false;
            }
        }

        logger.log(Level.INFO, "SUCCESSFULLY VALIDATED for TimeRange " + timeInMinutes);
        return true;
    }

    /**
     * Description: To validate data displayed on IMSI Failure Analysis window
     *
     * @param imsi
     * @param imsiEventFailureList
     * @param eventType
     * @param failureCount
     * @return
     * @throws NoDataException
     * @throws PopUpException
     */
    public boolean validateIMSIFailureAnalysisWindowData(String imsi, List<Map<String, String>> imsiEventFailureList,
            String eventType, int failureCount, int timeInMinutes) throws NoDataException, PopUpException, ParseException
    {
        // Validate event time for given TimeRange
        if (!validateEventTime(timeInMinutes, imsiEventFailureList))
        {
            logger.log(Level.SEVERE, "Invalid EventTime for " + timeInMinutes);
            return false;
        }

        List<Map<String, String>> resvDataList = getResvDataForEventType(GuiStringConstants.IMSI, imsi, eventType);

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiEventFailureList)
        {
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.Duration);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);
            //Compliant different naming conventions: "No of ERABS" and "Number of ERABs"
            if(eventFailureMap.containsKey(GuiStringConstants.HFA_NO_OF_ERABS))
            {
                String numberofErabs=eventFailureMap.get(GuiStringConstants.HFA_NO_OF_ERABS);
                eventFailureMap.remove(GuiStringConstants.HFA_NO_OF_ERABS);
                eventFailureMap.put(GuiStringConstants.HFA_NUMBER_OF_ERABS, numberofErabs);
            }

            if (resvDataList.contains(eventFailureMap))
            {
                logger.log(Level.INFO, "SUCCESS - Match Found");
            }
            else
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                for (final Map<String, String> resvDataMap : resvDataList)
                {
                    printMapData(resvDataMap, "RESERVE DATA MAP");
                }
                printMapData(eventFailureMap, "UI DATA MAP");
                return false;
            }
        }

        return true;
    }

    public boolean validateHandoverFailureAnalysisERABsData(String imsi, String eventType, List<Map<String, String>> ERABdataList) throws NoDataException, PopUpException
    {



        String tac = "", terminalMake = "", terminalModel = "", neVersion = "";
        List<Map<String, String>> resvDataERABList = new ArrayList<Map<String, String>>();



        for (Map<String, String> resvDataMap : reserveDataList)
        {
            if (imsi.equals(resvDataMap.get(GuiStringConstants.IMSI)))
            {
                tac = resvDataMap.get(GuiStringConstants.TAC);
                terminalMake = resvDataMap.get(GuiStringConstants.TERMINAL_MAKE);
                terminalModel = resvDataMap.get(GuiStringConstants.TERMINAL_MODEL);
                neVersion =  resvDataMap.get("NE_VERSION");
                break;
            }
        }

        for (Map<String, String> resvERABmap : reserveERABdataList)
        {
            if (eventType.equals(resvERABmap.get(GuiStringConstants.EVENT_TYPE)))
            {
                Map<String, String> resvDataERABmap = new HashMap<String, String>();
                resvDataERABmap.put(GuiStringConstants.IMSI, imsi);

                List<String> listOfERABheaders = new ArrayList<String>();

                if (eventType.contains("PREP"))
                {
                    listOfERABheaders = headersOnPrepERABEventAnalysisWindow;
                }else{
                    listOfERABheaders = headersOnExecERABEventAnalysisWindow;
                }

                for (String header : listOfERABheaders)
                {
                    String resvDataValue = resvERABmap.get(header);

                    if (neVersion.equals("11B"))
                    {
                        if (header.equals(GuiStringConstants.SETUP_REQ_PCI) ||
                                header.equals(GuiStringConstants.SETUP_REQ_PVI))
                        {
                            resvDataValue = "";
                        }
                    }
                    if (header.equals(GuiStringConstants.IMSI))
                    {
                        resvDataValue = imsi;
                    }
                    resvDataERABmap.put(header, resvDataValue);
                }
                //ADD TAC,Terminal Make and Mode Value
                resvDataERABmap.put(GuiStringConstants.TAC, tac);
                resvDataERABmap.put(GuiStringConstants.TERMINAL_MODEL, terminalModel);
                // Add resvDataMap to List
                resvDataERABList.add(resvDataERABmap);
            }
        }



        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> ERABmap : ERABdataList)
        {
            ERABmap.remove(GuiStringConstants.EVENT_TIME);
            ERABmap.remove(GuiStringConstants.ACCESS_AREA);
            ERABmap.remove(GuiStringConstants.ENODEB);
            ERABmap.remove(GuiStringConstants.VENDOR);
            ERABmap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (resvDataERABList.contains(ERABmap))
            {
                  logger.log(Level.INFO, "SUCCESS - Match Found");
            }
            else
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                for (Map<String, String> resvMap : resvDataERABList)
                {
                    printMapData(resvMap, "RESERVE DATA MAP");
                }
                printMapData(ERABmap, "UI DATA MAP");
                return false;
            }
        }

        return true;
    }

    public String getNumberOfERABsFromResvData(String eventType, String neVersion)
    {
        int numberOfERABs = 0;

        if ((neVersion.equals("12B")||(neVersion.equals("13A"))||(neVersion.equals("13B"))) && ((eventType == GuiStringConstants.PREP_X2_OUT) || (eventType ==GuiStringConstants.PREP_S1_OUT)
            || (eventType == GuiStringConstants.EXEC_S1_OUT) || (eventType ==GuiStringConstants.EXEC_X2_OUT) ))
        {
            for (Map<String, String> resvDataMap : reserveDataList)
            {
                if (eventType.equals(resvDataMap.get(GuiStringConstants.EVENT_TYPE)))
                {
                    numberOfERABs = 0;
                String erabFailBitmap = resvDataMap.get("ERAB Fail Bitmap");

                if (erabFailBitmap.isEmpty())
                {
                    continue;
                }

                int iErabFailBitmap = Integer.parseInt(erabFailBitmap);
                String binaryBitmapStr = Integer.toBinaryString(iErabFailBitmap);
                char binaryBitmapChar[] = binaryBitmapStr.toCharArray();
                for(int index=0; index<binaryBitmapChar.length; index++)
                {
                    if (binaryBitmapChar[index] !='0')
                    {
                        numberOfERABs++;
                    }

                }
                }
            }
            return Integer.toString(numberOfERABs);
        }

        /*
         * Calculate the ERAB from CSV file according to the number of rows for below two criteria
         * 1. Events PREP_X2_IN and PREP_S1_IN for all node versions
         * 2. Events EXEC_X2_IN and EXEC_S1_IN for 12B.
         *
         */
        else if((eventType.equals(GuiStringConstants.PREP_X2_IN)) || (eventType.equals(GuiStringConstants.PREP_S1_IN))
                || (((neVersion.equals("12B")||(neVersion.equals("13A"))||(neVersion.equals("13B"))) && ((eventType == GuiStringConstants.EXEC_X2_IN) || (eventType ==GuiStringConstants.EXEC_S1_IN)))))
        {
        for (Map<String, String> erabDataMap : reserveERABdataList)
        {
            if (eventType.equals(erabDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                numberOfERABs++;
            }
        }
        return Integer.toString(numberOfERABs);
        }

        return "";
    }

    /**
     * Description: To validate data displayed on IMSIGroup Failure Analysis window
     * @param imsiGroup
     * @param imsiGroupEventFailureList
     * @param eventType
     * @param failureCount
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateIMSIGroupFailureAnalysisWindowData(List<String> imsiGroupList, List<Map<String, String>> imsiGroupEventFailureList,
                                                              String eventType, int failureCount, int timeInMinutes)
                                                              throws NoDataException, PopUpException, IOException, ParseException
    {
        boolean matched = false;

        // Validate event time for given TimeRange
        if (!validateEventTime(timeInMinutes, imsiGroupEventFailureList))
        {
            logger.log(Level.SEVERE, "Invalid EventTime for " + timeInMinutes);
            return false;
        }

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiGroupEventFailureList)
        {
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);
            eventFailureMap.remove(GuiStringConstants.Duration);

            for (String imsi : imsiGroupList)
            {
                List<Map<String, String>> resvDataList = getResvDataForEventType(GuiStringConstants.IMSI, imsi, eventType);

                for (final Map<String, String> resvDataMap : resvDataList)
                {
                    resvDataMap.remove(GuiStringConstants.TERMINAL_MAKE);
                    resvDataMap.remove(GuiStringConstants.Duration);

                    if (eventFailureMap.equals(resvDataMap))
                    {
                        matched = true;
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                    }
                }
            }

            if (!matched)
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");

                // Print reserved data
                for (String imsi : imsiGroupList)
                {
                    List<Map<String, String>> resvDataList = getResvDataForEventType( GuiStringConstants.IMSI, imsi, eventType);

                    for (final Map<String, String> resvDataMap : resvDataList)
                    {
                        resvDataMap.remove(GuiStringConstants.TERMINAL_MAKE);
                        resvDataMap.remove(GuiStringConstants.Duration);

                        printMapData(resvDataMap, "RESERVE DATA MAP");
                    }
                }
                // Print UI data
                printMapData(eventFailureMap, "UI DATA MAP");
                return false;
            }

            matched = false;
        }

        return true;
    }

    /**
     * Description: To validate data displayed on IMSIGroup Event Analysis window
     * @param imsiGroupList
     * @param imsiGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateIMSIGroupEventAnalysisWindowData(List<String> imsiGroupList, List<Map<String, String>> imsiGroupEventFailureList, String handoverStage, int timeInMinutes)
                                                            throws NoDataException, PopUpException, IOException
    {
        if (imsiGroupEventFailureList.size() == 0)
        {
            logger.log(Level.SEVERE, "imsiGroupEventFailureList is empty");
            return false;
        }

        if (imsiGroupList.size() == 0)
        {
            logger.log(Level.SEVERE, "NO DATA FOUND in Database for given IMSI Group");
        }

        for (String imsi : imsiGroupList)
        {
            checkEventTypesConfiguredInResvData(imsi, handoverStage);
        }

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiGroupEventFailureList)
        {
            // Check if row is empty
            if (eventFailureMap.isEmpty())
            {
                logger.log(Level.SEVERE, "IMSI Event Failure Analysis Window Map is empty");
                return false;
            }

            String eventType = eventFailureMap.get(GuiStringConstants.EVENT_TYPE);
            String failures = eventFailureMap.get(GuiStringConstants.FAILURES);
            String impactedSubscribers = eventFailureMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS);
            int uiFailureCount = Integer.parseInt(failures);
            int uiImpactedSubscribers = Integer.parseInt(impactedSubscribers);

            int resvDataFailureCount = 0;
            int resvDataImpactedSubscribers = 0;

            for (String imsi : imsiGroupList)
            {
                // Get the failure count of a given EventType configured in the reserved data.
                int failureCountOfSubscriber =  checkNoOfFailuresForEventType(imsi, eventType);
                resvDataFailureCount += failureCountOfSubscriber;

                // Check if there are any failures for the given imsi and event type in reserved data
                if (failureCountOfSubscriber > 0)
                {
                    resvDataImpactedSubscribers++;
                }
            }

            // Check how many failure events should be generated at a given time range
            int totalFailuresExpected = resvDataFailureCount * (timeInMinutes/5);

            logger.log(Level.INFO, "IMSI Event Analysis Window - Event Type : " + eventType +
                    " Failures : " + uiFailureCount + ". And Number of Failures expected is " + totalFailuresExpected);

            // Check if failure count of a given EventType is not matching with the failure count in reserved data
            if (uiFailureCount != totalFailuresExpected || uiImpactedSubscribers != resvDataImpactedSubscribers)
            {
                logger.log(Level.SEVERE, "IMSI Group FAILURES count does not match");
                return false;
            }
        }

        return true;
    }


    public boolean validateDrillDownEventAnalysisWindow(String imsi, String drillDownMember, List<Map<String, String>> listOfDrillDownData, int timeInMinutes)
    {
        if (listOfDrillDownData.isEmpty())
        {
            return false;
        }

        if (drillDownMember.equals(GuiStringConstants.ENODEB))
        {
            drillDownMember = GuiStringConstants.CONTROLLER;
        }

        for (Map<String, String> uiDataMap : listOfDrillDownData)
        {
            String drillDownValue = uiDataMap.get(drillDownMember);
            Map<String, String> reserveDataMap = checkNoOfFailuresForDrillDownMember(drillDownValue, uiDataMap.get(GuiStringConstants.EVENT_TYPE), drillDownMember, timeInMinutes);

            uiDataMap.remove(GuiStringConstants.TERMINAL_MAKE);
            uiDataMap.remove(GuiStringConstants.Duration);
            reserveDataMap.remove(GuiStringConstants.TERMINAL_MAKE);
            reserveDataMap.remove(GuiStringConstants.Duration);

            // If UI data does not match with reserve data then
            if (!uiDataMap.equals(reserveDataMap))
            {
                printMapData(uiDataMap, "UI DATA");
                printMapData(reserveDataMap, "RESERVED DATA");
                return false;
            }
        }
        return true;
    }

    private Map<String, String> checkNoOfFailuresForDrillDownMember(String drillDownValue, String eventType, String drillDownMember, int timeInMinutes) {

        int noOfFailures = 0;
        boolean subscriberAlreadyInList = false;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, String> subscriberFailureMap = new HashMap<String, String>();

        String localDrillDownMember = drillDownMember;

        // Change the name of 'Controller' to 'EnodeB', since the column name in reserve data is 'EnodeB'
        if (localDrillDownMember.equals(GuiStringConstants.CONTROLLER))
        {
            localDrillDownMember = GuiStringConstants.TARGET_CONTROLLER;
        }
        if (localDrillDownMember.equals(GuiStringConstants.ACCESS_AREA))
        {
            localDrillDownMember = GuiStringConstants.SOURCE_ACCESS_AREA;
        }

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            // Check if the drillDown value is equal to value in reserve data
            if (drillDownValue.equals(resvDataMap.get(localDrillDownMember)) &&
                    eventType.equals(resvDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                // Add RAN Vendor to Map
                subscriberFailureMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
                // Add DrillDown Member
                subscriberFailureMap.put(drillDownMember, drillDownValue);
                // Add EventType
                subscriberFailureMap.put(GuiStringConstants.EVENT_TYPE, eventType);
                // If its eCell drill down then add Controller to the map, since the UI contains Controller in eCell drill down.
                if (drillDownMember.equals(GuiStringConstants.ACCESS_AREA))
                {
                    subscriberFailureMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.SOURCE_CONTROLLER));
                }

                // Increment the number of failures for the given eventType
                noOfFailures++;

                // Insert the Subscriber to the ImpactedSubscriber List
                if (listOfImpactedSubscribers.isEmpty())
                {
                    listOfImpactedSubscribers.add(resvDataMap.get(GuiStringConstants.IMSI));
                }
                else
                {
                    subscriberAlreadyInList = false;

                    // Check if the subscriber is already present in the list
                    for (String impactedSubscriber : listOfImpactedSubscribers)
                    {
                        if (impactedSubscriber.equals(resvDataMap.get(GuiStringConstants.IMSI)))
                        {
                            subscriberAlreadyInList = true;
                            break;
                        }
                    }

                    // If Not present in the ImpactedSubscriberList, then add to the list
                    if (!subscriberAlreadyInList)
                    {
                        listOfImpactedSubscribers.add(resvDataMap.get(GuiStringConstants.IMSI));
                    }
                }
            }
        }

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        if ((noOfFailures > 0) && !listOfImpactedSubscribers.isEmpty())
        {
            subscriberFailureMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
            subscriberFailureMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));
        }

        return subscriberFailureMap;
    }



    //////////////////////////////////////////////////////////////////////////////
    //                               RANKINGS TAB                    //
    //////////////////////////////////////////////////////////////////////////////


    public boolean validateSubscriberRanking(List<Map<String, String>> listOfSubscriberRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfSubscriberRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfSubscriberRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();
        int noOfFailures = 0;
        logger.log(Level.INFO, "typeOfFailure : " + typeOfFailure);
        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String imsi = resvDataMap.get(GuiStringConstants.IMSI);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                // Loop for next event type if the event does not belong to PREPARATION stage
                if (!listOfHandoverPreparationEvents.contains(eventType))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                // Loop for next event type if the event does not belong to EXECUTION stage
                if (!listOfHandoverExecutionEvents.contains(eventType))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Invalid Handover Category");
                return false;
            }

            if (eventTypeFailuresMap.containsKey(imsi))
            {
                noOfFailures = eventTypeFailuresMap.get(imsi);
            }

            noOfFailures++;
            eventTypeFailuresMap.put(imsi, noOfFailures);
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "eventTypeFailuresMap is Empty");
            return false;
        }

        List<Map<String, String>> resvDataSubscriberRankingList = getSubscriberRanking(eventTypeFailuresMap, timeInMinutes);

        if (resvDataSubscriberRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "resvDataSubscriberRankingList is Empty");
            return false;
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> SubscriberRankingMap : listOfSubscriberRanking)
        {
            String imsi = SubscriberRankingMap.get(GuiStringConstants.IMSI);

            for (Map<String, String> resvDataSubscriberRankingMap : resvDataSubscriberRankingList)
            {
                if (imsi.equals(resvDataSubscriberRankingMap.get(GuiStringConstants.IMSI)))
                {
                    if (!SubscriberRankingMap.equals(resvDataSubscriberRankingMap))
                    {
                        logger.log(Level.SEVERE, "SubscriberRankingMap DOES NOT MATCH with resvDataSubscriberRankingMap");
                        printMapData(resvDataSubscriberRankingMap, "RESERVE DATA MAP");
                        printMapData(SubscriberRankingMap, "UI DATA MAP");
                        return false;
                    }
                    else
                    {
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                        break;
                    }

                }
            }

        }
        return true;
    }

    /* ENODEB RANKING */

    public boolean validateEnodeBRanking(List<Map<String, String>> listOfEnodeBRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfEnodeBRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfEnodeBRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();
        Map<String, String> eNodeBVendorMap = new HashMap<String, String>();

        int noOfFailures = 0;

        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String eNodeB = resvDataMap.get(GuiStringConstants.SOURCE_CONTROLLER);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                // Loop for next event type if the event does not belong to PREPARATION stage
                if (!listOfHandoverPreparationEvents.contains(eventType))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                // Loop for next event type if the event does not belong to EXECUTION stage
                if (!listOfHandoverExecutionEvents.contains(eventType))
                {
                    continue;
                }
            }
            else
            {
                // throw error
                return false;
            }

            // Maintain a Map of ENODEB and No. Of Failures from reserved data
            // Check if eNodeB is already inserted in Map
            if (eventTypeFailuresMap.containsKey(eNodeB))
            {
                // Gets eNodeB's failure count
                noOfFailures = eventTypeFailuresMap.get(eNodeB);
            }

            // Increment Failure Count
            noOfFailures++;

            // Put the total failure count into Map
            eventTypeFailuresMap.put(eNodeB, noOfFailures);

            // Maintain a Map of ENODEB and RAN Vendor
            // Check if eNodeB is already inserted in Map
            if (!eNodeBVendorMap.containsKey(eNodeB))
            {
                // Put the RAN Vendor into Map
                eNodeBVendorMap.put(eNodeB, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
            }
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "eventTypeFailuresMap is Empty");
            return false;
        }

        List<Map<String, String>> resvDataENodeBRankingList = getResvDataRankingBasedOnFailures(eventTypeFailuresMap, timeInMinutes, GuiStringConstants.eNodeB);

        // Add RAN Vendor to List of Reserve Data Map
        for (Map<String, String> eNodeBRankingMap : resvDataENodeBRankingList)
        {
            String eNodeBValue =  eNodeBRankingMap.get(GuiStringConstants.eNodeB);

            // Get the vendor corresponding to this eNodeB
            String vendor = eNodeBVendorMap.get(eNodeBValue);

            // Add vendor to ReserveData Map
            eNodeBRankingMap.put(GuiStringConstants.RAN_VENDOR, vendor);
            eNodeBRankingMap.remove(GuiStringConstants.eNodeB);
            eNodeBRankingMap.put(GuiStringConstants.CONTROLLER, eNodeBValue);
        }

        if (resvDataENodeBRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataENodeBRankingList is Empty");
            return false;
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> eNodeBRankingMap : listOfEnodeBRanking)
        {
            String eNodeB = eNodeBRankingMap.get(GuiStringConstants.CONTROLLER);

            for (Map<String, String> resvDataEnodeBRankingMap : resvDataENodeBRankingList)
            {
                if (eNodeB.equals(resvDataEnodeBRankingMap.get(GuiStringConstants.CONTROLLER)))
                {
                    if (!eNodeBRankingMap.equals(resvDataEnodeBRankingMap))
                    {
                        logger.log(Level.SEVERE, "eNodeBRankingMap DOES NOT MATCH with resvDataEnodeBRankingMap");
                        printMapData(resvDataEnodeBRankingMap, "RESERVE DATA MAP");
                        printMapData(eNodeBRankingMap, "UI DATA MAP");
                        return false;
                    }
                    else
                    {
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                        break;
                    }

                }
            }

        }
        return true;
    }

    /* ECELL RANKING */

    public boolean validateEcellRanking(List<Map<String, String>> listOfEcellRanking, String accessAreaType, String typeOfFailure, int timeInMinutes)
    {
        if (listOfEcellRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfEcellRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();
        Map<String, String> eNodeBMap = new HashMap<String, String>();
        Map<String, String> vendorMap = new HashMap<String, String>();

        int noOfFailures = 0;

        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String eCell = resvDataMap.get(accessAreaType);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                // Loop for next event type if the event does not belong to PREPARATION stage
                if (!listOfHandoverPreparationEvents.contains(eventType))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                // Loop for next event type if the event does not belong to EXECUTION stage
                if (!listOfHandoverExecutionEvents.contains(eventType))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            // Maintain a Map of ENODEB and No. Of Failures from reserved data
            // Check if eNodeB is already inserted in Map
            if (eventTypeFailuresMap.containsKey(eCell))
            {
                // Gets eNodeB's failure count
                noOfFailures = eventTypeFailuresMap.get(eCell);
            }

            // Increment Failure Count
            noOfFailures++;

            // Put the total failure count into Map
            eventTypeFailuresMap.put(eCell, noOfFailures);

            // Maintain a Map of ENODEB and RAN Vendor
            // Check if eNodeB is already inserted in Map
            if (!vendorMap.containsKey(eCell))
            {
                // Put the RAN Vendor into Map
                vendorMap.put(eCell, resvDataMap.get(GuiStringConstants.VENDOR));
            }

            // Maintain a Map of ECELL and ENODEB
            // Check if eNodeB is already inserted in Map
            if (!eNodeBMap.containsKey(eCell))
            {
                // Put the RAN Vendor into Map
                eNodeBMap.put(eCell, resvDataMap.get(GuiStringConstants.ENODEB));
            }
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "eventTypeFailuresMap is Empty");
            return false;
        }

        List<Map<String, String>> resvDataEcellRankingList = getResvDataRankingBasedOnFailures(eventTypeFailuresMap, timeInMinutes, GuiStringConstants.ACCESS_AREA);

        // Add RAN Vendor to List of Reserve Data Map
        for (Map<String, String> eCellRankingMap : resvDataEcellRankingList)
        {
            String eCell =  eCellRankingMap.get(accessAreaType);

            // Get the vendor corresponding to this eNodeB
            String vendor = vendorMap.get(eCell);
            String eNodeB = eNodeBMap.get(eCell);

            // Add vendor to ReserveData Map
            eCellRankingMap.put(GuiStringConstants.RAN_VENDOR, vendor);
            // Add eNodeB to ReserveData Map
            eCellRankingMap.put(GuiStringConstants.CONTROLLER, eNodeB);
        }

        if (resvDataEcellRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataEcellRankingList is Empty");
            return false;
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> eCellRankingMap : listOfEcellRanking)
        {
            String eCell = eCellRankingMap.get(accessAreaType);

            for (Map<String, String> resvDataEcellRankingMap : resvDataEcellRankingList)
            {
                if (eCell.equals(resvDataEcellRankingMap.get(accessAreaType)))
                {
                    if (!eCellRankingMap.equals(resvDataEcellRankingMap))
                    {
                        logger.log(Level.SEVERE, "eCellRankingMap DOES NOT MATCH with resvDataEcellRankingMap");
                        printMapData(resvDataEcellRankingMap, "RESERVE DATA MAP");
                        printMapData(eCellRankingMap, "UI DATA MAP");
                        return false;
                    }
                    else
                    {
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                        break;
                    }

                }
            }

        }
        return true;
    }

    /* TRACKING AREA RANKING */

    public boolean validateTrackingAreaRanking(List<Map<String, String>> listOfTrackingAreaRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfTrackingAreaRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfTrackingAreaRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();

        int noOfFailures = 0;

        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String trackingArea = resvDataMap.get(GuiStringConstants.TRACKING_AREA);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                // Loop for next event type if the event does not belong to PREPARATION stage
                if (!listOfHandoverPreparationEvents.contains(eventType))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                // Loop for next event type if the event does not belong to EXECUTION stage
                if (!listOfHandoverExecutionEvents.contains(eventType))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            // Maintain a Map of trackingArea and No. Of Failures from reserved data
            // Check if trackingArea is already inserted in Map
            if (eventTypeFailuresMap.containsKey(trackingArea))
            {
                // Gets eNodeB's failure count
                noOfFailures = eventTypeFailuresMap.get(trackingArea);
            }

            // Increment Failure Count
            noOfFailures++;
            // Put the total failure count into Map
            eventTypeFailuresMap.put(trackingArea, noOfFailures);
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "eventTypeFailureMap is EMPTY");
            return false;
        }

        List<Map<String, String>> resvDataTrackingAreaRankingList = getResvDataRankingBasedOnFailures(eventTypeFailuresMap, timeInMinutes, GuiStringConstants.TRACKING_AREA);

        if (resvDataTrackingAreaRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataTrackingAreaRankingList is Empty");
            return false;
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> trackingAreaRankingMap : listOfTrackingAreaRanking)
        {
            String trackingArea = trackingAreaRankingMap.get(GuiStringConstants.TRACKING_AREA);

            for (Map<String, String> resvDataTrackingAreaRankingMap : resvDataTrackingAreaRankingList)
            {
                if (trackingArea.equals(resvDataTrackingAreaRankingMap.get(GuiStringConstants.TRACKING_AREA)))
                {
                    if (!trackingAreaRankingMap.equals(resvDataTrackingAreaRankingMap))
                    {
                        logger.log(Level.SEVERE, "trackingAreaRankingMap DOES NOT MATCH with resvDataTrackingAreaRankingMap");
                        printMapData(resvDataTrackingAreaRankingMap, "RESERVE DATA MAP");
                        printMapData(trackingAreaRankingMap, "UI DATA MAP");
                        return false;
                    }
                    else
                    {
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                        break;
                    }

                }
            }

        }
        return true;
    }

    private List<Map<String, String>> getResvDataRankingBasedOnFailures(Map<String, Integer> eventTypeFailuresMap, int timeInMinutes, String rankingType)
    {

        List<Map<String, String>> listofRank = new ArrayList<Map<String, String>>();

        int rank = 0;
        int tempCount = 0;
        int ranking = 0;

        for (Iterator i = sortByValue(eventTypeFailuresMap).iterator(); i.hasNext();)
        {
            Map<String, String> RankingMap = new HashMap<String, String>();

            String key = (String)i.next();
            int failures = eventTypeFailuresMap.get(key);
            int failureCount = failures * (timeInMinutes/5);
            ranking++;

            if (failureCount != tempCount)
            {
                rank = ranking;
            }

            tempCount = failureCount;

            RankingMap.put(rankingType, key);
            RankingMap.put(GuiStringConstants.FAILURES, Integer.toString(failureCount));
            RankingMap.put(GuiStringConstants.RANK, Integer.toString(rank));

            listofRank.add(RankingMap);
        }

        return listofRank;
    }

    /* CAUSE CODE RANKING */

    public boolean validateCauseCodeRanking(List<Map<String, String>> listOfCauseCodeRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfCauseCodeRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfCauseCodeRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();

        int noOfFailures = 0;

        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String causeCode = resvDataMap.get(GuiStringConstants.FAILURE_REASON);
            if (causeCode.isEmpty())
            {
                continue;
            }
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);
            String causeCodeEventType = causeCode + "," + eventType;

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                // Loop for next event type if the event does not belong to PREPARATION stage
                if (!listOfHandoverPreparationEvents.contains(eventType))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                // Loop for next event type if the event does not belong to EXECUTION stage
                if (!listOfHandoverExecutionEvents.contains(eventType))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            // Maintain a Map of trackingArea and No. Of Failures from reserved data
            // Check if trackingArea is already inserted in Map
            if (eventTypeFailuresMap.containsKey(causeCodeEventType))
            {
                // Gets eNodeB's failure count
                noOfFailures = eventTypeFailuresMap.get(causeCodeEventType);
            }

            // Increment Failure Count
            noOfFailures++;
            // Put the total failure count into Map
            eventTypeFailuresMap.put(causeCodeEventType, noOfFailures);
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "eventTypeFailureMap is EMPTY");
            return false;
        }

        List<Map<String, String>> resvDataCauseCodeRankingList = getResvDataRankingBasedOnFailures(eventTypeFailuresMap, timeInMinutes, "CAUSE_CODE_EVENT_TYPE");

        if (resvDataCauseCodeRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataCauseCodeRankingList is Empty");
            return false;
        }

        for (Map<String, String> resvDataCauseCodeMap : resvDataCauseCodeRankingList)
        {
            String causeCodeEventType =  resvDataCauseCodeMap.get("CAUSE_CODE_EVENT_TYPE");
            String causeCodeEventTypeArr[] = causeCodeEventType.split(",");
            String causeCodeDesc = causeCodeEventTypeArr[0];
            String eventType = causeCodeEventTypeArr[1];

            resvDataCauseCodeMap.put(GuiStringConstants.CAUSE_CODE_DESCRIPTION, causeCodeDesc);
            resvDataCauseCodeMap.put(GuiStringConstants.EVENT_TYPE, eventType);
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> causeCodeRankingMap : listOfCauseCodeRanking)
        {
            causeCodeRankingMap.remove("Cause Code ID");
            printMapData(causeCodeRankingMap, "UI DATA MAP");

            String uiCauseCodeEventType = causeCodeRankingMap.get(GuiStringConstants.CAUSE_CODE_DESCRIPTION) +
            "," + causeCodeRankingMap.get(GuiStringConstants.EVENT_TYPE);

            boolean matchFound = false;

            for (Map<String, String> resvDataCauseCodeRankingMap : resvDataCauseCodeRankingList)
            {
                if (uiCauseCodeEventType.equals(resvDataCauseCodeRankingMap.get("CAUSE_CODE_EVENT_TYPE")))
                {
                    resvDataCauseCodeRankingMap.remove("CAUSE_CODE_EVENT_TYPE");
                    printMapData(resvDataCauseCodeRankingMap, "RESERVE DATA MAP");

                    if (!causeCodeRankingMap.equals(resvDataCauseCodeRankingMap))
                    {
                        logger.log(Level.SEVERE, "causeCodeRankingMap DOES NOT MATCH with resvDataCauseCodeRankingMap");
                        return false;
                    }
                    else
                    {
                        matchFound = true;
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                        break;
                    }
                }
            }

            if (!matchFound)
            {
                logger.log(Level.SEVERE, "ERROR - MATCH NOT FOUND");
                return false;
            }

        }
        return true;
    }

   /* TERMINAL RANKING */

    public boolean validateTerminalRanking(List<Map<String, String>> listOfTerminalRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfTerminalRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfTerminalRanking is EMPTY");
        }

        Map<String, Integer> numberOfFailuresMap = new HashMap<String, Integer>();
        Map<String, String> manufacturerMap = new HashMap<String, String>();
        Map<String, String> modelMap = new HashMap<String, String>();

        int noOfFailures = 0;

        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.PREPARATION))
            {
                if (!eventType.contains("PREP"))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.EXECUTION))
            {
                if (!eventType.contains("EXEC"))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            String tac = resvDataMap.get(GuiStringConstants.TAC);

            // Maintain a Map of CONTROLLER and No. Of Failures from reserved data
            // Check if eNodeB is already inserted in Map
            if (numberOfFailuresMap.containsKey(tac))
            {
                // Gets eNodeB's failure count
                noOfFailures = numberOfFailuresMap.get(tac);
            }

            // Increment Failure Count
            noOfFailures++;

            // Put the total failure count into Map
            numberOfFailuresMap.put(tac, noOfFailures);

            // Maintain a Map of CONTROLLER and RAN Vendor
            // Check if eNodeB is already inserted in Map
            if (!manufacturerMap.containsKey(tac))
            {
                // Put the RAN Vendor into Map
                manufacturerMap.put(tac, resvDataMap.get(GuiStringConstants.TERMINAL_MAKE));
            }

            // Maintain a Map of ECELL and CONTROLLER
            // Check if eNodeB is already inserted in Map
            if (!modelMap.containsKey(tac))
            {
                // Put the RAN Vendor into Map
                modelMap.put(tac, resvDataMap.get(GuiStringConstants.TERMINAL_MODEL));
            }
        }

        if (numberOfFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "numberOfFailuresMap is Empty");
            return false;
        }

        List<Map<String, String>> resvDataTerminalRankingList = getResvDataRankingBasedOnFailures(numberOfFailuresMap, timeInMinutes, GuiStringConstants.TAC);

        for (Map<String, String> terminalRankingMap : resvDataTerminalRankingList)
        {
            String tac =  terminalRankingMap.get(GuiStringConstants.TAC);

            // Get the vendor corresponding to this eNodeB
            String model = modelMap.get(tac);

            // Add model to ReserveData Map
            terminalRankingMap.put(GuiStringConstants.TERMINAL_MODEL, model);

        }

        if (resvDataTerminalRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataTerminalRankingList is Empty");
            return false;
        }

        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> terminalRankingMap : listOfTerminalRanking)
        {
            terminalRankingMap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (resvDataTerminalRankingList.contains(terminalRankingMap))
            {
                logger.log(Level.INFO, "resvDataTerminalRankingList MATCHES with UI data");
            }
            else
            {
                for(Map<String, String> mapToPrint : resvDataTerminalRankingList)
                {
                    printMapData(mapToPrint, "RESERVE DATA MAP");
                }
                printMapData(terminalRankingMap, "UI DATA MAP");
                logger.log(Level.SEVERE, "Data displayed on Terminal Ranking Window is incorrect");
                return false;
            }
        }
        return true;
    }


    //////////////////////////////////////////////////////////////////////////////
    //                               NETWORK TAB                                    //
    //////////////////////////////////////////////////////////////////////////////


    /**
     * Description: To validate data displayed on IMSIGroup Failure Analysis window
     * @param imsiGroup
     * @param imsiGroupEventFailureList
     * @param eventType
     * @param failureCount
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateNetworkGroupFailureAnalysisWindowData(List<String> networkGroupList, List<Map<String, String>> networkGroupEventFailureList,
                                                              String networkEntityType, String eventType, int failureCount, int timeInMinutes)
                                                              throws NoDataException, PopUpException, IOException, ParseException
    {
        boolean matched = false;

        if (networkGroupList.isEmpty())
        {
            logger.log(Level.SEVERE, "NetworkGroupList is Empty");
            return false;
        }

        // Validate event time for given TimeRange
        if (!validateEventTime(timeInMinutes, networkGroupEventFailureList))
        {
            logger.log(Level.SEVERE, "Invalid Event Time");
            return false;
        }

        // Loop through all the rows in Failed Event Analysis Window
        for (final Map<String, String> eventFailureMap : networkGroupEventFailureList)
        {
            // Remove EVENT_TIME column validation with reserve data
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);

            for (String networkEntity : networkGroupList)
            {
                List<Map<String, String>> resvDataList = getResvDataForEventType(networkEntityType, networkEntity, eventType);

                for (final Map<String, String> resvDataMap : resvDataList)
                {

                    if (eventFailureMap.equals(resvDataMap))
                    {
                        matched = true;
                        logger.log(Level.INFO, "SUCCESS - Match Found");
                    }
                }
            }

            if (!matched)
            {
                // Print the reserved data
                for (String networkEntity : networkGroupList)
                {
                    List<Map<String, String>> resvDataList = getResvDataForEventType(networkEntityType, networkEntity, eventType);

                    for (final Map<String, String> resvDataMap : resvDataList)
                    {
                        printMapData(resvDataMap, "RESERVE DATA MAP");
                    }
                }
                // Print the UI data
                printMapData(eventFailureMap, "UI DATA MAP");
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                return false;
            }

            matched = false;
        }

        return true;
    }

    /**
     * Description: To validate data displayed on Network Group(ENODEB or ECELL) Event Analysis window
     * @param networkGroupList
     * @param networkGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateNetworkGroupEventAnalysisWindowData(List<String> networkGroupList, List<Map<String, String>> networkGroupEventFailureList, String networkEntityType, int timeInMinutes)
                                                            throws NoDataException, PopUpException, IOException
    {
        if (networkGroupEventFailureList.size() == 0)
        {
            logger.log(Level.SEVERE, "imsiGroupEventFailureList is empty");
            return false;
        }

        if (networkGroupList.size() == 0)
        {
            logger.log(Level.SEVERE, "NO DATA FOUND in Database for given IMSI Group");
        }

        for (String networkEntity : networkGroupList)
        {
            logger.log(Level.SEVERE, "networkEntity : " + networkEntity);
            checkEventTypesConfiguredInResvData(networkEntityType, networkEntity);
        }

        for (final Map<String, String> eventFailureMap : networkGroupEventFailureList)
        {
            // Check if row is empty
            if (eventFailureMap.isEmpty())
            {
                logger.log(Level.SEVERE, "Event Failure Analysis Window Map is empty");
                return false;
            }

            String eventType = eventFailureMap.get(GuiStringConstants.EVENT_TYPE);
            String failures = eventFailureMap.get(GuiStringConstants.FAILURES);
            String impactedSubscribers = eventFailureMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS);
            int uiFailureCount = Integer.parseInt(failures);
            int uiImpactedSubscribers = Integer.parseInt(impactedSubscribers);

            Map<String, Integer> failuresMap=  checkNoOfFailuresForEventTypeForNetwork(networkEntityType, networkGroupList, eventType);
            int resvDataFailureCount = failuresMap.get(GuiStringConstants.FAILURES);
            int resvDataImpactedSubscribers = failuresMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS);


            // Check how many failure events should be generated at a given time range
            int totalFailuresExpected = resvDataFailureCount * (timeInMinutes/5);

            logger.log(Level.INFO, "IMSI Event Analysis Window - Event Type : " + eventType +
                    " Failures : " + uiFailureCount + ". And Number of Failures expected is " + totalFailuresExpected +
                    ". Impacted Subscribers in UI is " + uiImpactedSubscribers + " and impacted subscribers in reserved data is " + resvDataImpactedSubscribers);

            // Check if failure count of a given EventType is not matching with the failure count in reserved data
            if (uiFailureCount != totalFailuresExpected || uiImpactedSubscribers != resvDataImpactedSubscribers)
            {
                logger.log(Level.SEVERE, "Network Group FAILURES count does not match");
                return false;
            }
        }

        return true;
    }


    /**
     * Description: To validate data displayed on Cause Code Analysis window
     * @param imsiGroupList
     * @param imsiGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateCauseCodeAnalysisWindow(String causeCodeAnalysisValue[], String causeCodeAnalysisType, boolean isGroup, String groupName, List<Map<String, String>> listOfCauseCodeAnalysis, int timeInMinutes)
    {
        if (listOfCauseCodeAnalysis.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfCauseCodeAnalysis)
        {

            Map<String, String> resvDataCauseCodeMap = checkCauseCodeInResvData(causeCodeAnalysisType, causeCodeAnalysisValue,
                                                        uiDataMap.get(GuiStringConstants.EVENT_TYPE),
                                                        uiDataMap.get(GuiStringConstants.CAUSE_CODE_DESCRIPTION),
                                                        timeInMinutes);

            uiDataMap.remove("Cause Code ID");

            if (!uiDataMap.equals(resvDataCauseCodeMap))
            {
                printMapData(resvDataCauseCodeMap, "RESERVED DATA");
                printMapData(uiDataMap, "UI DATA");
                return false;
            }
        }
        return true;
    }

    private Map<String, String> checkCauseCodeInResvData(String drillDownMember, String valueArray[], String eventType, String causeCodeDesc, int timeInMinutes)
    {
        int noOfOccurrences = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        List<String> listOfHeaders = new ArrayList<String>();
        List<Map<String, String>> drillDownResvDataList = new ArrayList<Map<String, String>>();

        Map<String, String> causeCodeAnalysisMap = new HashMap<String, String>();

        for (final Map<String, String> resvDataCauseCodeMap : reserveDataList)
        {
            boolean result = checkStringInStringArray(resvDataCauseCodeMap.get(drillDownMember), valueArray);
            // Check if the drillDown value is equal to value in reserve data
            if (result && eventType.equals(resvDataCauseCodeMap.get(GuiStringConstants.EVENT_TYPE))&&
                causeCodeDesc.equals(resvDataCauseCodeMap.get(GuiStringConstants.FAILURE_REASON)))
            {
                // Increment the number of failures for the given eventType
                noOfOccurrences++;
                String imsi = resvDataCauseCodeMap.get(GuiStringConstants.IMSI);
                // If not present, add the Subscriber to the ImpactedSubscriber List
                if (!listOfImpactedSubscribers.contains(imsi))
                {
                    listOfImpactedSubscribers.add(imsi);
                }

                if (eventType.equals(GuiStringConstants.PREP_X2_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_X2_IN;
                }
                else if (eventType.equals(GuiStringConstants.PREP_X2_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_X2_OUT;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_X2_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_IN;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_X2_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_OUT;
                }
                else if (eventType.equals(GuiStringConstants.PREP_S1_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_S1_IN;
                }
                else if (eventType.equals(GuiStringConstants.PREP_S1_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_S1_OUT;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_S1_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_IN;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_S1_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_OUT;
                }
                else
                {
                    logger.log(Level.INFO, "UNKNOWN EVENT TYPE");
                }

                // Create a new map containing specific to that eventType
                Map<String, String> resvDataMap = new HashMap<String, String>();

                for (String header : listOfHeaders)
                {
                    String resvDataValue = resvDataCauseCodeMap.get(header);

                    if (header.equals(GuiStringConstants.NUMBER_OF_ERABS))
                    {
                        resvDataValue = getNumberOfERABsFromResvData(eventType, resvDataCauseCodeMap.get("NE_VERSION"));
                    }

                    resvDataMap.put(header, resvDataValue);
                }

                resvDataMap.remove(GuiStringConstants.TERMINAL_MAKE);
                resvDataMap.remove(GuiStringConstants.Duration);
                resvDataMap.remove(GuiStringConstants.RAN_VENDOR);

                // Added rows of all these eventTypes into a new list
                drillDownResvDataList.add(resvDataMap);

                String causeCodeEventType = causeCodeDesc + "," + eventType;
                drillDownResvDataMap.put(causeCodeEventType, drillDownResvDataList);
            }
        }

        // Check the noOfOccurrences for a given time range
        noOfOccurrences = noOfOccurrences * (timeInMinutes/5);

        if ((noOfOccurrences > 0) && !listOfImpactedSubscribers.isEmpty())
        {
            // Add EventType
            causeCodeAnalysisMap.put(GuiStringConstants.EVENT_TYPE, eventType);
            // Add Cause Code Description
            causeCodeAnalysisMap.put(GuiStringConstants.CAUSE_CODE_DESCRIPTION, causeCodeDesc);

            causeCodeAnalysisMap.put("Occurrences", Integer.toString(noOfOccurrences));
            causeCodeAnalysisMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));
        }

        return causeCodeAnalysisMap;
    }

    /**
     * Description: To validate data displayed on IMSI Failure Analysis window
     *
     * @param imsi
     * @param imsiEventFailureList
     * @param eventType
     * @param failureCount
     * @return
     * @throws NoDataException
     * @throws PopUpException
     */
    public boolean validateCauseCodeDrillDownWindowData(String causeCodeEventType, List<Map<String, String>> causeCodeDetailAnalysisList,
            int failureCount, int timeInMinutes) throws NoDataException, PopUpException, ParseException
    {
        // Validate event time for given TimeRange
        if (!validateEventTime(timeInMinutes, causeCodeDetailAnalysisList))
        {
            logger.log(Level.SEVERE, "Invalid EventTime");
            return false;
        }

        List<Map<String, String>> resvDataList = drillDownResvDataMap.get(causeCodeEventType);

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> uiFailureMap : causeCodeDetailAnalysisList)
        {
            uiFailureMap.remove(GuiStringConstants.EVENT_TIME);
            uiFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);
            uiFailureMap.remove(GuiStringConstants.Duration);
            //Compliant different naming conventions: "No of ERABS" and "Number of ERABs"
            if (uiFailureMap.containsKey(GuiStringConstants.HFA_NO_OF_ERABS))
            {
                String numberofErabs=uiFailureMap.get(GuiStringConstants.HFA_NO_OF_ERABS);
                uiFailureMap.remove(GuiStringConstants.HFA_NO_OF_ERABS);
                uiFailureMap.put(GuiStringConstants.HFA_NUMBER_OF_ERABS, numberofErabs);
            }

            if (resvDataList.contains(uiFailureMap))
            {
                logger.log(Level.INFO, "Record Found");
            }
            else
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                for(Map<String, String> mapToPrint : resvDataList)
                {
                    printMapData(mapToPrint, "RESERVE DATA MAP");
                }
                printMapData(uiFailureMap, "UI DATA MAP");
                return false;
            }
        }

        return true;
    }

    /**
     * Description: Initialises DataIntegrityCheckUtil by loading reserve data during startup.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void loadEventVolumeData(ReservedDataHelper reservedDataHelper, Date startDate)
    {
        final String ver12Asupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_12A_SUPPORTED);
        final String ver11Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_11B_SUPPORTED);
        final String ver12Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_12B_SUPPORTED);
        final String ver13Asupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_13A_SUPPORTED);
        final String ver13Bsupported = reservedDataHelper.getCommonReservedData(CommonDataType.VERSION_13B_SUPPORTED);

        if (ver12Asupported.equals("yes"))
        {
            try
            {
                loadEventVolumeResvData("12A", reservedDataHelper, startDate);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver11Bsupported.equals("yes"))
        {
            try
            {
                loadEventVolumeResvData("11B", reservedDataHelper, startDate);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver12Bsupported.equals("yes"))
        {
            try
            {
                loadEventVolumeResvData("12B", reservedDataHelper, startDate);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver13Asupported.equals("yes"))
        {
            try
            {
                loadEventVolumeResvData("13A", reservedDataHelper, startDate);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (ver13Bsupported.equals("yes"))
        {
            try
            {
                loadEventVolumeResvData("13B", reservedDataHelper, startDate);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: To validate data displayed on IMSIGroup Event Analysis window
     * @param imsiGroupList
     * @param imsiGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateEventVolumeAnalysisWindow(String networkElementType, List<String> networkElementList, List<Map<String, String>> listOfEventVolumeData, boolean allNetworkElements, Date startDate,  int timeInMinutes)
    {
         if (listOfEventVolumeData.isEmpty())
         {
              return false;
          }

         List<Map<String, String>> resvEventVolumeDataList = getListOfResvEventVolumeData(networkElementType, networkElementList, allNetworkElements, startDate, timeInMinutes);

         if (resvEventVolumeDataList.equals(listOfEventVolumeData))
         {
             logger.log(Level.INFO, "SUCCESS - CORRECT DATA DISPLAYED ON UI");
         }
         else
         {
             logger.log(Level.SEVERE, "ERROR - INCORRECT DATA DISPLAYED ON UI");
             for (Map<String, String> uiDataMap : listOfEventVolumeData)
             {
                 // If UI data does not match with reserve data then
                 if (!resvEventVolumeDataList.contains(uiDataMap))
                 {
                     for (Map<String, String> reserveDataMap : resvEventVolumeDataList)
                     {
                         printMapData(reserveDataMap, "RESERVED DATA");
                     }
                     printMapData(uiDataMap, "UI DATA");

                      return false;
                 }
             }

              return false;
         }

         eventVolReserveDataList.clear();
         return true;
    }

    private List<Map<String, String>> getListOfResvEventVolumeData(String networkElementType, List<String> networkElementList, boolean allNetworkElements, Date startDate, int timeInMinutes)
    {
        List<Map<String, String>> resvEventVolumeDataList = new ArrayList<Map<String, String>>();
        Map<String, List<String>> impactedSubscrMap = new HashMap<String, List<String>>();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat eventTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        boolean matchFound = false;

        for (final Map<String, String> resvDataMap : eventVolReserveDataList)
        {
            if ((networkElementList != null && networkElementList.contains(resvDataMap.get(networkElementType))) || allNetworkElements)
            {
                String eventTime = resvDataMap.get(GuiStringConstants.EVENT_TIME);
                String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);
                String imsi = resvDataMap.get(GuiStringConstants.IMSI);

                if (timeInMinutes > 120)
                {
                    try
                    {
                        Date eventDateTime = eventTimeFormatter.parse(eventTime);
                        long eventTimeLong = eventDateTime.getTime();
                        final int minutesValue = Integer.parseInt(minFormatter.format(eventDateTime.getTime()));

                        if (minutesValue < 15) {
                            eventTimeLong = eventTimeLong - (minutesValue * 60 * 1000);
                        } else {
                            final int reminderValue = minutesValue % 15;
                            eventTimeLong = eventTimeLong - (reminderValue * 60 * 1000);
                        }
                        eventDateTime.setTime(eventTimeLong);
                        eventTime = eventTimeFormatter.format(eventDateTime);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                for (Map<String, String> resvEvtVolMap : resvEventVolumeDataList)
                {
                    if (eventTime.equals(resvEvtVolMap.get("Time")))
                    {
                        int failureCount = Integer.parseInt(resvEvtVolMap.get(eventType + failCount));
                        failureCount++;
                        resvEvtVolMap.put(eventType + failCount, Integer.toString(failureCount));

                        if (!allNetworkElements)
                        {
                            if (!impactedSubscrMap.containsKey(eventTime))
                            {
                                List<String> listOfImpactedSubscr = new ArrayList<String>();
                                listOfImpactedSubscr.add(imsi);
                                impactedSubscrMap.put(eventTime, listOfImpactedSubscr);
                                resvEvtVolMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS,  Integer.toString(listOfImpactedSubscr.size()));
                            }
                            else
                            {
                                List<String> listOfImpactedSubscr = impactedSubscrMap.get(eventTime);

                                if (!listOfImpactedSubscr.isEmpty() && !listOfImpactedSubscr.contains(imsi))
                                {
                                    listOfImpactedSubscr.add(imsi);
                                    resvEvtVolMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS,  Integer.toString(listOfImpactedSubscr.size()));
                                }
                            }
                        }

                        matchFound = true;
                        break;
                    }
                }

                if (!matchFound)
                {
                    Map<String, String> resvEventVolumeDataMap = new HashMap<String, String>();

                    resvEventVolumeDataMap.put(GuiStringConstants.PREP_S1_IN + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.EXEC_X2_IN + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.PREP_X2_OUT + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.EXEC_X2_OUT + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.PREP_X2_IN + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.EXEC_S1_IN + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.PREP_S1_OUT + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.EXEC_S1_OUT + failCount, "0");
                    if (!allNetworkElements)
                    {
                        resvEventVolumeDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, "0");
                    }
                    resvEventVolumeDataMap.put("Time", eventTime);

                    int failureCount = Integer.parseInt(resvEventVolumeDataMap.get(eventType + failCount));
                    failureCount++;
                    resvEventVolumeDataMap.put(eventType + failCount, Integer.toString(failureCount));

                    resvEventVolumeDataList.add(resvEventVolumeDataMap);
                }
                matchFound = false;
            }
        }

        return resvEventVolumeDataList;
    }

    public boolean validateFailuresPerQCIWindow(String qciValue, List<Map<String, String>> listOfFailuresPerQCI, int timeInMinutes)
    {
        if (listOfFailuresPerQCI.isEmpty() ||
            qciFailureStatsList.isEmpty())
        {
            return false;
        }

        return true;
    }


    public void getERABfailureAnalysisPerQCIFromResvData(String neType, String valueArray[])
    {
        // Cell Trace iterator
        List<Map<String, String>> reserveERABdataListCopy = new ArrayList<Map<String, String>>(reserveERABdataList);
        Collections.copy(reserveERABdataListCopy, reserveERABdataList);

        if (neType.equals(GuiStringConstants.CONTROLLER))
        {
            neType = GuiStringConstants.SOURCE_CONTROLLER;
        }
        else if (neType.equals(GuiStringConstants.ACCESS_AREA))
        {
            neType = GuiStringConstants.SOURCE_ACCESS_AREA;
        }

        // Make a map of EventType and value as Map of QCI Index and QCI ID
        Map<String, Map<String, String>> eventTypeQCImap = new HashMap<String, Map<String, String>>();

        for (Map<String, String> resvErabMap : reserveERABdataList)
        {
            String eventType = resvErabMap.get(GuiStringConstants.EVENT_TYPE);

            if (!eventTypeQCImap.containsKey(eventType))
            {
                if (resvErabMap.get("QCI Index").isEmpty())
                {
                    continue;
                }
                 Map<String, String> erabQciMap = new HashMap<String, String>();
                 erabQciMap.put(resvErabMap.get("QCI Index"), resvErabMap.get("QCI ID"));
                 eventTypeQCImap.put(eventType, erabQciMap);
            }
            else
            {
                Map<String, String> erabQciMap = eventTypeQCImap.get(eventType);
                erabQciMap.put(resvErabMap.get("QCI Index"), resvErabMap.get("QCI ID"));
            }
        }

        // Loop through reserved data
        for (final Map<String, String> reserveDataMap : reserveDataList)
        {
              boolean neFound = false;

              // Loop through all the networkEntities in the group
               for (int i=0; i<valueArray.length; i++)
            {
                   if (valueArray[i].equals(reserveDataMap.get(neType)) && reserveDataMap.get("NE_VERSION").equals("13B"))
                   {
                       neFound = true;
                       break;
                   }
            }

            if (!neFound)
            {
                continue;
            }

            String eventType = reserveDataMap.get(GuiStringConstants.EVENT_TYPE);
            String imsi = reserveDataMap.get(GuiStringConstants.IMSI);
            String erabFailBitmap = reserveDataMap.get("ERAB Fail Bitmap");

            // Ignore events not having QCI Index
            if (!eventTypeQCImap.containsKey(eventType))
            {
                continue;
            }

            // If ERAB Fail Bitmap is not available for an event, then do not check for QCI Index
            if (erabFailBitmap.isEmpty())
            {
                Map<String, String> qciMap = eventTypeQCImap.get(eventType);
                // Get a set of the entries
                Set set = qciMap.entrySet();
                // Get an iterator
                Iterator itr = set.iterator();

                while(itr.hasNext())
                {
                    Map.Entry iMap = (Map.Entry)itr.next();
                    Map<String, String> qciErabMap = new HashMap<String, String>();
                    qciErabMap.put("QCI ID", (String) iMap.getValue());
                    qciErabMap.put(GuiStringConstants.EVENT_TYPE, eventType);
                    qciErabMap.put(GuiStringConstants.IMSI, imsi);
                    qciFailureStatsList.add(qciErabMap);
                }
            }
            else
            {
                int iErabFailBitmap = Integer.parseInt(erabFailBitmap);
                String binaryBitmapStr = Integer.toBinaryString(iErabFailBitmap);
                char binaryBitmapChar[] = binaryBitmapStr.toCharArray();
                for(int index=0; index<binaryBitmapChar.length; index++)
                {
                    if (binaryBitmapChar[index]=='0')
                    {
                        continue;
                    }

                    Map<String, String> qciMap = eventTypeQCImap.get(eventType);
                    Map<String, String> qciErabMap = new HashMap<String, String>();
                    String qciId = qciMap.get(Integer.toString(index));
                    qciErabMap.put("QCI ID", qciId);
                    qciErabMap.put(GuiStringConstants.EVENT_TYPE, eventType);
                    qciErabMap.put(GuiStringConstants.IMSI, imsi);
                    qciFailureStatsList.add(qciErabMap);
                }
             }
        }
    }


    /**
     * Description: To validate data displayed on QCI Statistics window
     * @param imsiGroupList
     * @param imsiGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateQCIStatisticsWindow(String neValue[], String neType, List<Map<String, String>> listOfQCIStatistics, int timeInMinutes)
    {
        if (listOfQCIStatistics.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfQCIStatistics)
        {
            Map<String, String> resvDataMap = getFailuresAndImpactedSubscribers(qciFailureStatsList, uiDataMap.get("QCI ID"), uiDataMap.get("QCI Description"),
                    uiDataMap.get(GuiStringConstants.HANDOVER_STAGE), timeInMinutes);

            if (uiDataMap.equals(resvDataMap))
            {
                logger.log(Level.INFO, "QCI Statistics is matching Reserve Data");
            }
            else
            {
                logger.log(Level.SEVERE, "QCI Statistics is NOT matching Reserve Data");
                printMapData(uiDataMap, "UI DATA");
                printMapData(resvDataMap, "RESERVE DATA");
                return false;
            }
        }
        return true;
    }



    private Map<String, String> getFailuresAndImpactedSubscribers(List<Map<String, String>> qciFailureStatsList,
            String qciValue, String qciDesc, String category, int timeInMinutes)
    {
        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();

        for(Map<String, String> erabFailureMap : qciFailureStatsList)
        {
            if (!qciValue.equals(erabFailureMap.get("QCI ID")))
            {
                continue;
            }

            String eventType = erabFailureMap.get(GuiStringConstants.EVENT_TYPE);
            if (category.equals(GuiStringConstants.PREPARATION))
            {
                if (eventType.equals(GuiStringConstants.PREP_S1_IN) ||
                        eventType.equals(GuiStringConstants.PREP_S1_OUT) ||
                        eventType.equals(GuiStringConstants.PREP_X2_IN) ||
                        eventType.equals(GuiStringConstants.PREP_X2_OUT))
                {
                    String imsi = erabFailureMap.get(GuiStringConstants.IMSI);
                    if (!listOfImpactedSubscribers.contains(imsi))
                    {
                        listOfImpactedSubscribers.add(imsi);
                    }

                    noOfFailures++;
                }
            }
            else
            {
                if (eventType.equals(GuiStringConstants.EXEC_S1_IN) ||
                        eventType.equals(GuiStringConstants.EXEC_S1_OUT) ||
                        eventType.equals(GuiStringConstants.EXEC_X2_IN) ||
                        eventType.equals(GuiStringConstants.EXEC_X2_OUT))
                {
                    String imsi = erabFailureMap.get(GuiStringConstants.IMSI);
                    if (!listOfImpactedSubscribers.contains(imsi))
                    {
                        listOfImpactedSubscribers.add(imsi);
                    }

                    noOfFailures++;
                }
            }
        }

        Map<String, String> resvDataQCIMap = new HashMap<String, String>();
        resvDataQCIMap.put("QCI ID", qciValue);
        resvDataQCIMap.put("QCI Description", qciDesc);
        resvDataQCIMap.put(GuiStringConstants.HANDOVER_STAGE, category);

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        resvDataQCIMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
        resvDataQCIMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));

        return resvDataQCIMap;
    }

    public boolean validateEventAnalysisPerQCIWindow(String qciValue, List<Map<String, String>> listOfEventAnalysisPerQCI, int timeInMinutes)
    {
        if (listOfEventAnalysisPerQCI.isEmpty() ||
                qciFailureStatsList.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfEventAnalysisPerQCI)
        {
            Map<String, String> resvDataMap = getFailuresAndImpactedSubscribersEventAnalysis(qciFailureStatsList, qciValue, uiDataMap.get(GuiStringConstants.EVENT_TYPE), timeInMinutes);

            if (uiDataMap.equals(resvDataMap))
            {
                logger.log(Level.INFO, "Event Analysis QCI Statistics is matching Reserve Data");
            }
            else
            {
                printMapData(uiDataMap, "UI DATA");
                printMapData(resvDataMap, "RESERVE DATA");
                logger.log(Level.SEVERE, "Event Analysis QCI Statistics is NOT matching Reserve Data");
                return false;
            }
        }
        return true;
    }

    private Map<String, String> getFailuresAndImpactedSubscribersEventAnalysis(List<Map<String, String>> qciFailureStatsList,
                       String qciValue, String uiEventType, int timeInMinutes)
    {
        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();

        for(Map<String, String> erabFailureMap : qciFailureStatsList)
        {
            if (!qciValue.equals(erabFailureMap.get("QCI ID")))
            {
                continue;
            }

            String eventType = erabFailureMap.get(GuiStringConstants.EVENT_TYPE);

            if (eventType.equals(uiEventType))
            {
                String imsi = erabFailureMap.get(GuiStringConstants.IMSI);
                if (!listOfImpactedSubscribers.contains(imsi))
                {
                    listOfImpactedSubscribers.add(imsi);
                }

                noOfFailures++;
            }
        }

        if (noOfFailures == 0)
        {
            return null;
        }

        Map<String, String> resvDataQCIMap = new HashMap<String, String>();

        //     Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        resvDataQCIMap.put(GuiStringConstants.EVENT_TYPE, uiEventType);
        resvDataQCIMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
        resvDataQCIMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));

        return resvDataQCIMap;
    }

     /**
     * Set date and time in time dialog window with a lag of configurable time.
     * For example : By selecting 15 mins on EventAnalysis window, it will display
     *               data with a specific time lag.
     *               i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30,
     *               but due to delay in displaying of UI at current time, it fails to display all the expected data till 11:30.
     *               Therefore to display all the expected data this function creates a time lag for example of 5 mins and
     *               fetches data from 11:10 to 11:25.
     *               Similarly for all other time time ranges, it displays data with a time lag of 5 mins.
     * Note :        This time lag can be configurable from properties.
     *
     * @param TimeRange ex: 15mins, 30mins etc.
     * NOTE : Cannot set timeRange for 5 mins due to UI constraints.
     */
    public void delayAndSetTimeRange(final TimeRange timeRange) throws PopUpException {
        final Calendar cal = Calendar.getInstance();
        final DateFormat minFormatter = new SimpleDateFormat("mm");
        final DateFormat AMPMFormatter = new SimpleDateFormat("a");
        final Formatter startDatefmt = new Formatter();
        final Formatter endDatefmt = new Formatter();
        TimeZone timeZone = TimeZone.getDefault();
        int offset = 0;

        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }

        cal.set(Calendar.SECOND, 0);

        final Date currentDate = cal.getTime();
        logger.log(Level.INFO, "Current Date : " + currentDate);

        final long currentTime = currentDate.getTime(); // gets time in milliseconds

        final int timeRangeInMins = timeRange.getMiniutes();

        if (timeRange == TimeRange.FIFTEEN_MINUTES || timeRange == TimeRange.THIRTY_MINUTES ||
                timeRange == TimeRange.ONE_HOUR || timeRange == TimeRange.TWO_HOURS)
        {
            offset = 10;
        }
        else
        {
            offset = 30;
        }

        long endDateTime = currentTime - (offset * 60 * 1000);

        cal.setTimeInMillis(endDateTime);
        Date endDate = cal.getTime();

        final int minutesValue = Integer.parseInt(minFormatter.format(endDate.getTime()));

        // Since DateAndTime Range Dialog window in UI contains only divisible of 15 in time,
        // Therefore round-off the mins to divisibles of 15 and mins less than 15 will be rounded to 0.
        if (minutesValue < 15) {
            endDateTime = endDateTime - (minutesValue * 60 * 1000);
        } else {
            final int reminderValue = minutesValue % 15;
            endDateTime = endDateTime - (reminderValue * 60 * 1000);
        }

        cal.setTimeInMillis(endDateTime);
        endDate = cal.getTime();

        final long startDateTime = endDateTime - (timeRangeInMins * 60 * 1000);

        cal.setTimeInMillis(startDateTime);
        Date startDate = cal.getTime();

        // Get the time in 12 hour format
        startDatefmt.format("%tl", startDate);
        String hourStartDate = startDatefmt.toString();
        int lengthOfHour = hourStartDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourStartDate = "0" + hourStartDate;
        }

        endDatefmt.format("%tl", endDate);
        String hourEndDate = endDatefmt.toString();
        lengthOfHour = hourEndDate.length();
        if (lengthOfHour == 1) {
            // Prefix with 0 if single digit
            hourEndDate = "0" + hourEndDate;
        }

        // Concatinate AM/PM, HOUR and MIN to form a member variable of TimeCandidate i.e AM_HOUR_MIN
        String startDateTimeCandidate = AMPMFormatter.format(startDate.getTime()) + "_" + hourStartDate + "_"
                + minFormatter.format(startDate.getTime());
        String endDateTimeCandidate = AMPMFormatter.format(endDate.getTime()) + "_" + hourEndDate + "_"
                + minFormatter.format(endDate.getTime());

        gStartDate = startDate;
        gStartDateTimeCandidate = startDateTimeCandidate.toUpperCase().replace(".","");
        gEndDate = endDate;
        gEndDateTimeCandidate = endDateTimeCandidate.toUpperCase().replace(".","");


        logger.log(Level.INFO, "Duration : " + timeRangeInMins + " minutes. Start Date Time Candidate : " + startDate
                + " " + startDateTimeCandidate + " and End Date Time Candidate : " + endDate + " "
                + endDateTimeCandidate);

    }

    //////////////////////////////////////////////////////////////////////////////
    //                       TOPOLOGY DATA                        //
    //////////////////////////////////////////////////////////////////////////////

    @Test
    public void updateTopologyChanges()
    {
        final String queryForTopologyData = "select DISTINCT ACCESS_AREA_ID, ENODEB_ID,MCC,MNC,TRAC,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_LTE_HIER321 where upper(STATUS) = 'ACTIVE'";
        final String topologyColumns = "MCC,MNC,ACCESS_AREA_ID,ENODEB_ID,HIERARCHY_1,HIERARCHY_3,VENDOR";
        topologyInformation = dbPersistor.queryLteTopologyData(topologyColumns, queryForTopologyData);

        final String queryForTac = "select top 10 TAC, MANUFACTURER, MARKETING_NAME from DIM_E_SGEH_TAC  where TAC>10000000 order by tac";
        final String tacArguments = "TAC,MANUFACTURER,MARKETING_NAME";
        tacInformation = dbPersistor.queryLteTopologyData(tacArguments, queryForTac);

        dbPersistor.closeConnection();
    }

    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////

    private void loadCsvData(String neVersion, ReservedDataHelper reservedDataHelper) throws IOException {
        // CTUM iterator
        Iterator ctumIter = topologyInformation.iterator();

        // Cell Trace iterator
        List<Map<String, String>> RANTopologyData = new ArrayList<Map<String, String>>(topologyInformation);
        Collections.copy(RANTopologyData,topologyInformation);
        Iterator cellTraceIter = RANTopologyData.iterator(); // for CELL TRACE data

        // Hand over Neighbouring Network Element
        List<Map<String, String>> neighbouringTopologyData = new ArrayList<Map<String, String>>(topologyInformation);
        Collections.copy(neighbouringTopologyData,topologyInformation);
        Iterator neighbouringTopologyDataIter = neighbouringTopologyData.iterator(); // for Neighbouring Controller and Access Area
        if (neighbouringTopologyDataIter.hasNext())
        {
            neighbouringTopologyDataIter.next();
        }

        // TAC Iterator
        Iterator tacIter = tacInformation.iterator();

        Map<String, String> cellTraceMap = new HashMap<String, String>();
        Map<String, String> neighbouringCTMap = new HashMap<String, String>();
        Map<String, String> ctumMap = new HashMap<String, String>();
        Map<String, String> tacMap = new HashMap<String, String>();

        final int noOfNodes = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_NODES));
        maxSubscribers = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_SUBSCRIBERS));
        final int noOfInternalProcHoPrepS1OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_S1_OUT_EVENTS));
        final int noOfInternalProcHoPrepS1InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_S1_IN_EVENTS));
        final int noOfInternalProcHoExecS1OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_S1_OUT_EVENTS));
        final int noOfInternalProcHoExecS1InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_S1_IN_EVENTS));
        final int noOfInternalProcHoPrepX2OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_X2_OUT_EVENTS));
        final int noOfInternalProcHoPrepX2InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_X2_IN_EVENTS));
        final int noOfInternalProcHoExecX2OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_X2_OUT_EVENTS));
        final int noOfInternalProcHoExecX2InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_X2_IN_EVENTS));

        final String causeCodePrepS1Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_PREP_S1_OUT);
        final String causeCodePrepX2Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_PREP_X2_OUT);
        final String causeCodeExecS1In = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_S1_IN);
        final String causeCodeExecS1Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_S1_OUT);
        final String causeCodeExecX2In = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_X2_IN);
        final String causeCodeExecX2Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_X2_OUT);

        CsvReader products = null;
        if(new java.io.File(LOCAL_FILE_PATH_OF_LTERESERVED_DATA).exists()){
            products = new CsvReader(LOCAL_FILE_PATH_OF_LTERESERVED_DATA);
        }else{
            products = new CsvReader(UNIX_FILE_PATH_OF_LTERESERVED_DATA);
        }
        products.readHeaders();
        Map<String, Map<String, String>> resvDataMap = new HashMap<String, Map<String, String>>();

        while (products.readRecord())
        {
            Map<String, String> csvRecordMap = new HashMap<String, String>();
            csvRecordMap.put(GuiStringConstants.FAILURE_REASON, products.get(GuiStringConstants.FAILURE_REASON));
            csvRecordMap.put(GuiStringConstants.Duration, products.get(GuiStringConstants.Duration));
            csvRecordMap.put(GuiStringConstants.SELECTION_TYPE, products.get(GuiStringConstants.SELECTION_TYPE));
            csvRecordMap.put(GuiStringConstants.RANDOM_ACCESS_TYPE, products.get(GuiStringConstants.RANDOM_ACCESS_TYPE));
            csvRecordMap.put(GuiStringConstants.SUBSCRIBER_PROFILE_ID, products.get(GuiStringConstants.SUBSCRIBER_PROFILE_ID));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS, products.get(GuiStringConstants.NUMBER_OF_ERABS));
            csvRecordMap.put(GuiStringConstants.HO_ATTEMPT, products.get(GuiStringConstants.HO_ATTEMPT));
            csvRecordMap.put(GuiStringConstants.CONFIG_INDEX, products.get(GuiStringConstants.CONFIG_INDEX));
            csvRecordMap.put(GuiStringConstants.PACKET_FORWARD, products.get(GuiStringConstants.PACKET_FORWARD));
            csvRecordMap.put(GuiStringConstants.SOURCE_TYPE, products.get(GuiStringConstants.SOURCE_TYPE));
            csvRecordMap.put("ERAB Fail Bitmap", products.get("ERAB Fail Bitmap"));
            csvRecordMap.put(GuiStringConstants.HO_TYPE, products.get(GuiStringConstants.HO_TYPE));
            csvRecordMap.put(GuiStringConstants.SRVCC_TYPE , products.get(GuiStringConstants.SRVCC_TYPE ));
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP_HFA , products.get(GuiStringConstants.CAUSE_3GPP_HFA ));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP_HFA , products.get(GuiStringConstants.CAUSE_GROUP_3GPP_HFA ));
            csvRecordMap.put(GuiStringConstants.TARGET_TYPE, products.get(GuiStringConstants.TARGET_TYPE));

            resvDataMap.put(products.get(GuiStringConstants.EVENT_TYPE),  csvRecordMap);
        }

        for (int node = 0; node < noOfNodes; node++)
        {
            if (!cellTraceIter.hasNext())
            {
                cellTraceIter = RANTopologyData.iterator();
            }

            if (!neighbouringTopologyDataIter.hasNext())
            {
                neighbouringTopologyDataIter = neighbouringTopologyData.iterator();
            }

            cellTraceMap = (Map<String, String>)cellTraceIter.next();
            neighbouringCTMap = (Map<String, String>)neighbouringTopologyDataIter.next();
            ctumIter = topologyInformation.iterator();
            tacIter = tacInformation.iterator();

            int noOfEvents = 0, noOfSubscribers = 0;

            while (true)
            {
                if (noOfSubscribers == maxSubscribers)
                {
                    noOfSubscribers = 0;
                    ctumIter = topologyInformation.iterator();
                    tacIter = tacInformation.iterator();
                }

                noOfSubscribers++;

                if (!ctumIter.hasNext() || !tacIter.hasNext())
                {
                    ctumIter = topologyInformation.iterator();
                    tacIter = tacInformation.iterator();
                }

                ctumMap = (Map<String, String>)ctumIter.next();
                tacMap = (Map<String, String>)tacIter.next();

                   if (noOfEvents < noOfInternalProcHoPrepS1OutEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_S1_OUT, causeCodePrepS1Out, neVersion, resvDataMap);
                   }

                   if (noOfEvents <  noOfInternalProcHoPrepS1InEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_S1_IN, "", neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecS1OutEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_S1_OUT, causeCodeExecS1Out, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecS1InEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_S1_IN, causeCodeExecS1In, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoPrepX2OutEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_X2_OUT, causeCodePrepX2Out, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoPrepX2InEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_X2_IN, "", neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecX2OutEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_X2_OUT, causeCodeExecX2Out, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecX2InEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_X2_IN, causeCodeExecX2In, neVersion, resvDataMap);
                   }

                   if ((noOfEvents >= noOfInternalProcHoPrepS1OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepS1InEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecS1OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecS1InEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepX2OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepX2InEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecX2OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecX2InEvents))
                   {
                       break;
                   }

                   noOfEvents++;
            }
        }
    }

    private String getImsi(Map<String, String> ctumMap, int imsiSeq)
    {
        String mccStr = getTopologyData(ctumMap, GuiStringConstants.MCC);
        String mncStr = getTopologyData(ctumMap, GuiStringConstants.MNC);

        long mcc = Integer.parseInt(mccStr);
        long mnc = Integer.parseInt(mncStr);

        mcc = mcc * 1000000000000L;
        if (mncStr.length() > 2)
            mnc = mnc * 1000000000;
        else
            mnc = mnc * 10000000000L;

        long imsi  = mcc + mnc + imsiSeq;

        return String.valueOf(imsi);
    }

    private void createEvent(Map<String, String> ctumMap, Map<String, String> tacMap, Map<String, String> cellTraceMap, Map<String, String> neighbouringCTMap,
                                            int imsiSeq, String eventType, String causeCode, String neVersion, Map<String, Map<String, String>> resvDataMap)
    {
        Map<String, String> csvRecordMap = new HashMap<String, String>();

        if (neVersion.equals("11B"))
        {
            imsiSeq = imsiSeq + 10;
        }

        if (neVersion.equals("12B"))
        {
            imsiSeq = imsiSeq + 20;
        }

        if (neVersion.equals("13A"))
        {
            imsiSeq = imsiSeq + 30;
        }

        if (neVersion.equals("13B"))
        {
            imsiSeq = imsiSeq + 40;
        }

        String imsi = getImsi(ctumMap, imsiSeq);
        String tac = getTopologyData(tacMap, GuiStringConstants.TAC);
        String terminalMake = getTopologyData(tacMap, "MANUFACTURER");
        String terminalModel = getTopologyData(tacMap, "MARKETING_NAME");

        csvRecordMap.put(GuiStringConstants.IMSI, imsi);
        csvRecordMap.put(GuiStringConstants.TAC, tac);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MAKE, terminalMake);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MODEL, terminalModel);

        csvRecordMap.put(GuiStringConstants.EVENT_TYPE, eventType);


        csvRecordMap.put(GuiStringConstants.SOURCE_CONTROLLER, getTopologyData(cellTraceMap, "HIERARCHY_3"));
        csvRecordMap.put(GuiStringConstants.TARGET_CONTROLLER, getTopologyData(neighbouringCTMap, "HIERARCHY_3"));

        csvRecordMap.put(GuiStringConstants.SOURCE_ACCESS_AREA, getTopologyData(cellTraceMap, "HIERARCHY_1"));
        csvRecordMap.put(GuiStringConstants.TARGET_ACCESS_AREA, getTopologyData(neighbouringCTMap, "HIERARCHY_1"));

        csvRecordMap.put(GuiStringConstants.TRACKING_AREA, getTopologyData(cellTraceMap, "TRAC"));
        csvRecordMap.put(GuiStringConstants.RAN_VENDOR, getTopologyData(cellTraceMap, "VENDOR"));
        csvRecordMap.put("NE_VERSION", neVersion);

        Map<String, String> resvDataAttributes = resvDataMap.get(eventType) ;

        if (neVersion.equals("12A") || neVersion.equals("12B") || neVersion.equals("13A") || neVersion.equals("13B"))
        {
            csvRecordMap.put(GuiStringConstants.MCC, getTopologyData(cellTraceMap, GuiStringConstants.MCC));
            int mncInt = Integer.parseInt(getTopologyData(cellTraceMap, GuiStringConstants.MNC));
            String mncString;
            if(mncInt/10<1){
                mncString ="0";
                mncString +=  Integer.toString(mncInt);
            }else{
                mncString = Integer.toString(mncInt);
            }
            csvRecordMap.put(GuiStringConstants.MNC, mncString);
           csvRecordMap.put(GuiStringConstants.SUBSCRIBER_PROFILE_ID, resvDataAttributes.get(GuiStringConstants.SUBSCRIBER_PROFILE_ID));
           csvRecordMap.put(GuiStringConstants.CONFIG_INDEX, resvDataAttributes.get(GuiStringConstants.CONFIG_INDEX));
        }
        else if (neVersion.equals("11B"))
        {
            csvRecordMap.put(GuiStringConstants.MCC, "");
            csvRecordMap.put(GuiStringConstants.MNC, "");
            csvRecordMap.put(GuiStringConstants.SUBSCRIBER_PROFILE_ID, "");
            csvRecordMap.put(GuiStringConstants.CONFIG_INDEX, "");
        }

        if (neVersion.equals("12B")||neVersion.equals("13A")||neVersion.equals("13B"))
        {
            csvRecordMap.put(GuiStringConstants.SRVCC_TYPE , resvDataAttributes.get(GuiStringConstants.SRVCC_TYPE ));
            csvRecordMap.put(GuiStringConstants.HO_ATTEMPT , resvDataAttributes.get(GuiStringConstants.HO_ATTEMPT ));
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP_HFA , resvDataAttributes.get(GuiStringConstants.CAUSE_3GPP_HFA ));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP_HFA , resvDataAttributes.get(GuiStringConstants.CAUSE_GROUP_3GPP_HFA ));
        }
        else
        {
            csvRecordMap.put(GuiStringConstants.SRVCC_TYPE , "");
            csvRecordMap.put(GuiStringConstants.HO_ATTEMPT , "");
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP_HFA , "");
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP_HFA , "");
        }

        if((neVersion.equals("11B")|| neVersion.equals("12A") )&& eventType.equals(GuiStringConstants.EXEC_S1_IN)){
        csvRecordMap.put(GuiStringConstants.SOURCE_TYPE, "");
        }else{
            csvRecordMap.put(GuiStringConstants.SOURCE_TYPE, resvDataAttributes.get(GuiStringConstants.SOURCE_TYPE));
        }
        csvRecordMap.put(GuiStringConstants.Duration, resvDataAttributes.get(GuiStringConstants.Duration));
        csvRecordMap.put(GuiStringConstants.FAILURE_REASON, resvDataAttributes.get(GuiStringConstants.FAILURE_REASON));
        csvRecordMap.put(GuiStringConstants.TARGET_TYPE, resvDataAttributes.get(GuiStringConstants.TARGET_TYPE));
        csvRecordMap.put(GuiStringConstants.SELECTION_TYPE, resvDataAttributes.get(GuiStringConstants.SELECTION_TYPE));
        csvRecordMap.put(GuiStringConstants.RANDOM_ACCESS_TYPE, resvDataAttributes.get(GuiStringConstants.RANDOM_ACCESS_TYPE));
        csvRecordMap.put("ERAB Fail Bitmap", resvDataAttributes.get("ERAB Fail Bitmap"));
        csvRecordMap.put("Number of ERABs", getNumberOfERABsFromResvData(eventType, neVersion));
        csvRecordMap.put(GuiStringConstants.HO_ATTEMPT, resvDataAttributes.get(GuiStringConstants.HO_ATTEMPT));
        csvRecordMap.put(GuiStringConstants.PACKET_FORWARD, "");
        csvRecordMap.put(GuiStringConstants.HO_TYPE, resvDataAttributes.get(GuiStringConstants.HO_TYPE));

        reserveDataList.add(csvRecordMap);
    }

    private void loadERABcsvData() {
        try
        {
            CsvReader products = null;
            if(new java.io.File(LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA).exists()){
                products = new CsvReader(LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA);
            }else{
                products = new CsvReader(UNIX_FILE_PATH_OF_LTERESERVED_ERAB_DATA);
            }

            products.readHeaders();

            while (products.readRecord())
            {
                Map<String, String> csvRecordMap = new HashMap<String, String>();

                csvRecordMap.put(GuiStringConstants.EVENT_TYPE, products.get(GuiStringConstants.EVENT_TYPE));
                csvRecordMap.put(GuiStringConstants.SETUP_REQ_PCI, products.get("Service Request PCI"));
                csvRecordMap.put(GuiStringConstants.SETUP_REQ_PVI, products.get("Service Request PVI"));
                csvRecordMap.put("HO Prep ERAB Result", products.get("HO Prep ERAB Result"));
                csvRecordMap.put("HO Prep ERAB Request", products.get("HO Prep ERAB Request"));
                csvRecordMap.put("Setup Request ARP", products.get("Setup Request ARP"));
                csvRecordMap.put("QCI ID", products.get("QCI ID"));
                csvRecordMap.put("QCI Index", products.get("QCI Index"));
                csvRecordMap.put("Prep QCI", products.get("Prep QCI"));
                csvRecordMap.put("Exec QCI", products.get("Exec QCI"));

                reserveERABdataList.add(csvRecordMap);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getTopologyData(Map<String, String> inputMap, String Key)
    {
        String Value = inputMap.get(Key);
        return Value;
    }

    private void checkEventTypesConfiguredInResvData(String imsi, String handoverStage) throws NoDataException, PopUpException {

        listOfEventTypesInResvData.clear();

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            if (imsi.equals(resvDataMap.get(GuiStringConstants.IMSI)))
            {
                String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

                if (!listOfEventTypesInResvData.contains(eventType))
                {
                    if (handoverStage.equals("Preparation"))
                    {
                        // If HO eventType belongs to Preparation then add to the list
                        if (listOfHandoverPreparationEvents.contains(eventType))
                        {
                            listOfEventTypesInResvData.add(eventType);
                        }
                    }
                    else if (handoverStage.equals("Execution"))
                    {
                        // If HO eventType belongs to Preparation then add to the list
                        if (listOfHandoverExecutionEvents.contains(eventType))
                        {
                            listOfEventTypesInResvData.add(eventType);
                        }

                    }
                }
            }
        }
    }

    private int checkNoOfFailuresForEventType(String imsi, String eventType) {

        int noOfFailures = 0;

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            if (imsi.equals(resvDataMap.get(GuiStringConstants.IMSI)))
            {
                if (eventType.isEmpty())
                {
                    noOfFailures++;
                }
                else
                {
                    String resvDataEventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

                    if (resvDataEventType.equals(eventType))
                    {
                        noOfFailures++;
                    }
                }
            }
        }

        return noOfFailures;
    }

    private Map<String, Integer> checkNoOfFailuresForEventTypeForNetwork(String inputType, List<String> GroupList, String eventType) {

        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, Integer> failuresMap = new HashMap<String, Integer>();

        // Change the name of 'Controller' to 'EnodeB', since the column name in reserve data is 'EnodeB'
        if (inputType.equals(GuiStringConstants.CONTROLLER))
        {
            inputType = GuiStringConstants.SOURCE_CONTROLLER;
        }
        if (inputType.equals(GuiStringConstants.ACCESS_AREA))
        {
            inputType = GuiStringConstants.SOURCE_ACCESS_AREA;
        }

        for (String inputValue : GroupList)
        {
            for (final Map<String, String> resvDataMap : reserveDataList)
            {
                if (inputValue.equals(resvDataMap.get(inputType)))
                {
                    String resvDataEventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

                    if (resvDataEventType.equals(eventType))
                    {
                        noOfFailures++;
                        if (!listOfImpactedSubscribers.contains(resvDataMap.get(GuiStringConstants.IMSI)))
                        {
                            listOfImpactedSubscribers.add(resvDataMap.get(GuiStringConstants.IMSI));
                        }
                    }
                }
            }
        }

        failuresMap.put(GuiStringConstants.FAILURES, noOfFailures);
        failuresMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, listOfImpactedSubscribers.size());

        return failuresMap;
    }

    private List<Map<String, String>> getResvDataForEventType(String inputType, String inputValue, String eventType)
    {
        List<Map<String, String>> resvDataList = new ArrayList<Map<String, String>>();
        List<String> listOfHeaders = new ArrayList<String>();

        // Change the name of 'Controller' to 'EnodeB', since the column name in reserve data is 'EnodeB'
        if (inputType.equals(GuiStringConstants.CONTROLLER))
        {
            inputType = GuiStringConstants.SOURCE_CONTROLLER;
        }
        if (inputType.equals(GuiStringConstants.ACCESS_AREA))
        {
            inputType = GuiStringConstants.SOURCE_ACCESS_AREA;
        }

        // Get each row from the resvData
        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            if (inputValue.equals(resvDataMap.get(inputType)) &&
                    eventType.equals(resvDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                if (eventType.equals(GuiStringConstants.PREP_X2_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_X2_IN;
                }
                else if (eventType.equals(GuiStringConstants.PREP_X2_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_X2_OUT;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_X2_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_IN;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_X2_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_X2_OUT;
                }
                else if (eventType.equals(GuiStringConstants.PREP_S1_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_S1_IN;
                }
                else if (eventType.equals(GuiStringConstants.PREP_S1_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_PREP_S1_OUT;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_S1_IN))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_IN;
                }
                else if (eventType.equals(GuiStringConstants.EXEC_S1_OUT))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_EXEC_S1_OUT;
                }
                else
                {
                    logger.log(Level.INFO, "UNKNOWN EVENT TYPE");
                }

                // Create a new map containing specific to that eventType
                Map<String, String> resvDataForEventTypeMap = new HashMap<String, String>();

                for (final String header : listOfHeaders)
                {
                    String resvDataValue = resvDataMap.get(header);
                    resvDataForEventTypeMap.put(header, resvDataValue);
                }

                resvDataForEventTypeMap.remove(GuiStringConstants.Duration);
                resvDataForEventTypeMap.remove(GuiStringConstants.TERMINAL_MAKE);
                resvDataForEventTypeMap.remove(GuiStringConstants.RAN_VENDOR);

                // Added rows of all these eventTypes into a new list
                resvDataList.add(resvDataForEventTypeMap);
            }
        }

        return resvDataList;
    }

    private boolean validateEventTime(int timeInMinutes, List<Map<String, String>> failureList) throws ParseException
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        
        for (Map<String, String> failureMap : failureList)
        {
            String eventTime = failureMap.get(GuiStringConstants.EVENT_TIME);
            Date eventDateAndTime = df.parse(eventTime);

            String startDateTime = df.format(gStartDate);
            String endDateTime = df.format(gEndDate);

            gStartDate = df.parse(startDateTime);
            gEndDate = df.parse(endDateTime);

            logger.log(Level.INFO, "EVENT_TIME displayed in UI : " + eventTime);
            logger.log(Level.INFO, "EVENT_TIME should be between StartDateTime : " + gStartDate.toString() + " , EndDateTime : " + gEndDate.toString());

            if (eventDateAndTime.equals(gStartDate) ||
                eventDateAndTime.equals(gEndDate))
            {
                // EVENT_TIME is equal to either expected startDateTime or endDateTime
                continue;
            }
            else if (eventDateAndTime.after(gStartDate)  &&
                     eventDateAndTime.before(gEndDate))
            {
                // EVENT_TIME is between expected startDateTime and endDateTime
                continue;
            }
            else
            {
                logger.log(Level.SEVERE, "EVENT_TIME displayed on UI is incorrect");
                return false;
            }
        }

        return true;
    }


    @SuppressWarnings("unchecked")
    public static List sortByValue(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                }
                else if (v1 instanceof Comparable) {
                    return -((Comparable) v1).compareTo(v2);
                }
                else {
                    return 0;
                }
            }
        });
        return keys;
    }

    private List<Map<String, String>> getSubscriberRanking(Map<String, Integer> eventTypeFailuresMap, int timeInMinutes)
    {

        List<Map<String, String>> listofSubscriberRank = new ArrayList<Map<String, String>>();

        int rank = 0;
        int tempCount = 0;
        int ranking = 0;

        for (Iterator i = sortByValue(eventTypeFailuresMap).iterator(); i.hasNext();)
        {
            Map<String, String> SubscriberRankMap = new HashMap<String, String>();

            String imsiKey = (String)i.next();
            int failures = eventTypeFailuresMap.get(imsiKey);

            int failureCount = failures * (timeInMinutes/5);

            ranking++;

            if (failureCount != tempCount)
            {
                rank = ranking;
            }

            tempCount = failureCount;

            SubscriberRankMap.put(GuiStringConstants.IMSI, imsiKey);
            SubscriberRankMap.put(GuiStringConstants.FAILURES, Integer.toString(failureCount));
            SubscriberRankMap.put(GuiStringConstants.RANK, Integer.toString(rank));

            listofSubscriberRank.add(SubscriberRankMap);
        }

        return listofSubscriberRank;
    }

    private void printMapData(Map<String, String> mapToPrint, String trace)
    {
        String printString = "\n########################### " + trace + "  ##########################\n" + "Size of Map : " + mapToPrint.size()+"\n";

        for (Map.Entry<String, String> me : mapToPrint.entrySet())
        {
            printString =  printString + me.getKey() + ": "+ me.getValue() + "\n";
        }

        logger.log(Level.SEVERE, printString);
    }

    private boolean checkStringInStringArray(String value, String arrayValue[])
    {
        for (int i = 0; i < arrayValue.length; i++)
        {
            if (value.equals(arrayValue[i]))
            {
                return true;
            }
        }
        return false;
    }

    private void loadEventVolumeResvData(String neVersion, ReservedDataHelper reservedDataHelper, Date startDate) throws IOException {
        // CTUM iterator
        Iterator ctumIter = topologyInformation.iterator();

        // Cell Trace iterator
        List<Map<String, String>> RANTopologyData = new ArrayList<Map<String, String>>(topologyInformation);
        Collections.copy(RANTopologyData,topologyInformation);
        Iterator cellTraceIter = RANTopologyData.iterator(); // for CELL TRACE data

        // Hand over Neighbouring Network Element
        List<Map<String, String>> neighbouringTopologyData = new ArrayList<Map<String, String>>(topologyInformation);
        Collections.copy(neighbouringTopologyData,topologyInformation);
        Iterator neighbouringTopologyDataIter = neighbouringTopologyData.iterator(); // for Neighbouring Controller and Access Area
        if (neighbouringTopologyDataIter.hasNext())
        {
            neighbouringTopologyDataIter.next();
        }

        // TAC Iterator
        Iterator tacIter = tacInformation.iterator();

        Map<String, String> cellTraceMap = new HashMap<String, String>();
        Map<String, String> neighbouringCTMap = new HashMap<String, String>();
        Map<String, String> ctumMap = new HashMap<String, String>();
        Map<String, String> tacMap = new HashMap<String, String>();
        int minuteValue = 0;

        final int noOfNodes = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_NODES));
        maxSubscribers = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_SUBSCRIBERS));
        final int noOfInternalProcHoPrepS1OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_S1_OUT_EVENTS));
        final int noOfInternalProcHoPrepS1InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_S1_IN_EVENTS));
        final int noOfInternalProcHoExecS1OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_S1_OUT_EVENTS));
        final int noOfInternalProcHoExecS1InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_S1_IN_EVENTS));
        final int noOfInternalProcHoPrepX2OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_X2_OUT_EVENTS));
        final int noOfInternalProcHoPrepX2InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_PREP_X2_IN_EVENTS));
        final int noOfInternalProcHoExecX2OutEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_X2_OUT_EVENTS));
        final int noOfInternalProcHoExecX2InEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_EXEC_X2_IN_EVENTS));

        final String causeCodePrepS1Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_PREP_S1_OUT);
        final String causeCodePrepX2Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_PREP_X2_OUT);
        final String causeCodeExecS1In = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_S1_IN);
        final String causeCodeExecS1Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_S1_OUT);
        final String causeCodeExecX2In = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_X2_IN);
        final String causeCodeExecX2Out = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_EXEC_X2_OUT);

        CsvReader products = null;
        if(new java.io.File(LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA).exists()){
            products = new CsvReader(LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA);
        }else{
            products = new CsvReader(UNIX_FILE_PATH_OF_LTERESERVED_ERAB_DATA);
        }
        products.readHeaders();
        Map<String, Map<String, String>> resvDataMap = new HashMap<String, Map<String, String>>();

        while (products.readRecord())
        {
            Map<String, String> csvRecordMap = new HashMap<String, String>();
            csvRecordMap.put(GuiStringConstants.FAILURE_REASON, products.get(GuiStringConstants.FAILURE_REASON));
            csvRecordMap.put(GuiStringConstants.Duration, products.get(GuiStringConstants.Duration));
            csvRecordMap.put(GuiStringConstants.SELECTION_TYPE, products.get(GuiStringConstants.SELECTION_TYPE));
            csvRecordMap.put(GuiStringConstants.RANDOM_ACCESS_TYPE, products.get(GuiStringConstants.RANDOM_ACCESS_TYPE));
            csvRecordMap.put(GuiStringConstants.SUBSCRIBER_PROFILE_ID, products.get(GuiStringConstants.SUBSCRIBER_PROFILE_ID));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS, products.get(GuiStringConstants.NUMBER_OF_ERABS));
            csvRecordMap.put(GuiStringConstants.HO_ATTEMPT, products.get(GuiStringConstants.HO_ATTEMPT));
            csvRecordMap.put(GuiStringConstants.CONFIG_INDEX, products.get(GuiStringConstants.CONFIG_INDEX));
            csvRecordMap.put(GuiStringConstants.PACKET_FORWARD, products.get(GuiStringConstants.PACKET_FORWARD));
            csvRecordMap.put(GuiStringConstants.HO_TYPE, products.get(GuiStringConstants.HO_TYPE));
            csvRecordMap.put(GuiStringConstants.SRVCC_TYPE , products.get(GuiStringConstants.SRVCC_TYPE ));
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP_HFA , products.get(GuiStringConstants.CAUSE_3GPP_HFA ));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP_HFA , products.get(GuiStringConstants.CAUSE_GROUP_3GPP_HFA ));

            resvDataMap.put(products.get(GuiStringConstants.EVENT_TYPE),  csvRecordMap);
        }

        for (int node = 0; node < noOfNodes; node++)
        {
            if (!cellTraceIter.hasNext())
            {
                cellTraceIter = RANTopologyData.iterator();
            }

            if (!neighbouringTopologyDataIter.hasNext())
            {
                neighbouringTopologyDataIter = neighbouringTopologyData.iterator();
            }

            cellTraceMap = (Map<String, String>)cellTraceIter.next();
            neighbouringCTMap = (Map<String, String>)neighbouringTopologyDataIter.next();
            ctumIter = topologyInformation.iterator();
            tacIter = tacInformation.iterator();

            int noOfEvents = 0, noOfSubscribers = 0;
            minuteValue = 0; // reset the value

            while (true)
            {
                if (noOfSubscribers == maxSubscribers)
                {
                    noOfSubscribers = 0;
                    ctumIter = topologyInformation.iterator();
                    tacIter = tacInformation.iterator();
                }

                noOfSubscribers++;

                if (!ctumIter.hasNext() || !tacIter.hasNext())
                {
                    ctumIter = topologyInformation.iterator();
                    tacIter = tacInformation.iterator();
                }

                ctumMap = (Map<String, String>)ctumIter.next();
                tacMap = (Map<String, String>)tacIter.next();

                   if (noOfEvents < noOfInternalProcHoPrepS1OutEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_S1_OUT, causeCodePrepS1Out, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents <  noOfInternalProcHoPrepS1InEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_S1_IN, "", neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecS1OutEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_S1_OUT, causeCodeExecS1Out, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecS1InEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_S1_IN, causeCodeExecS1In, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoPrepX2OutEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_X2_OUT, causeCodePrepX2Out, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoPrepX2InEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.PREP_X2_IN, "", neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecX2OutEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_X2_OUT, causeCodeExecX2Out, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcHoExecX2InEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, neighbouringCTMap, noOfSubscribers, GuiStringConstants.EXEC_X2_IN, causeCodeExecX2In, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if ((noOfEvents >= noOfInternalProcHoPrepS1OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepS1InEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecS1OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecS1InEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepX2OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoPrepX2InEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecX2OutEvents) &&
                       (noOfEvents >= noOfInternalProcHoExecX2InEvents))
                   {
                       break;
                   }

                   noOfEvents++;

                   minuteValue++;
                   if (minuteValue >= 5)
                   {
                       minuteValue = 0; // reset the value when reached to 5 mins
                   }
            }
        }
    }

    private void createEventVolumeEvent(Map<String, String> ctumMap, Map<String, String> tacMap, Map<String, String> cellTraceMap, Map<String, String> neighbouringCTMap, int imsiSeq, String eventType, String causeCode, String neVersion, Date startDate, int minuteValue, Map<String, Map<String, String>> resvDataMap)
    {
        Map<String, String> csvRecordMap = new HashMap<String, String>();

        long eventDateLong = startDate.getTime();
        Date eventDate = new Date();
        eventDate.setTime(eventDateLong);
        long timeInMins = eventDate.getTime() + (minuteValue*60*1000);
        eventDate.setTime(timeInMins);
           final DateFormat minFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String startDateStr = minFormatter.format(eventDate.getTime());
           csvRecordMap.put(GuiStringConstants.EVENT_TIME, startDateStr);

        if (neVersion.equals("11B"))
        {
            imsiSeq = imsiSeq + 10;
        }

        if (neVersion.equals("12B"))
        {
            imsiSeq = imsiSeq + 20;
        }

        if (neVersion.equals("13A"))
        {
            imsiSeq = imsiSeq + 30;
        }

        if (neVersion.equals("13B"))
        {
            imsiSeq = imsiSeq + 40;
        }

        String imsi = getImsi(ctumMap, imsiSeq);
        String tac = getTopologyData(tacMap, GuiStringConstants.TAC);
        String terminalMake = getTopologyData(tacMap, "MANUFACTURER");
        String terminalModel = getTopologyData(tacMap, "MARKETING_NAME");

        csvRecordMap.put(GuiStringConstants.IMSI, imsi);
        csvRecordMap.put(GuiStringConstants.TAC, tac);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MAKE, terminalMake);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MODEL, terminalModel);

        csvRecordMap.put(GuiStringConstants.EVENT_TYPE, eventType);
        csvRecordMap.put(GuiStringConstants.FAILURE_REASON, causeCode);

        csvRecordMap.put(GuiStringConstants.SOURCE_CONTROLLER, getTopologyData(cellTraceMap, "HIERARCHY_3"));
        csvRecordMap.put(GuiStringConstants.TARGET_CONTROLLER, getTopologyData(neighbouringCTMap, "HIERARCHY_3"));

        csvRecordMap.put(GuiStringConstants.SOURCE_ACCESS_AREA, getTopologyData(cellTraceMap, "HIERARCHY_1"));
        csvRecordMap.put(GuiStringConstants.TARGET_ACCESS_AREA, getTopologyData(neighbouringCTMap, "HIERARCHY_1"));

        csvRecordMap.put(GuiStringConstants.TRACKING_AREA, getTopologyData(cellTraceMap, "TRAC"));
        csvRecordMap.put(GuiStringConstants.RAN_VENDOR, getTopologyData(cellTraceMap, "VENDOR"));
        csvRecordMap.put("NE_VERSION", neVersion);

        eventVolReserveDataList.add(csvRecordMap);
    }

}