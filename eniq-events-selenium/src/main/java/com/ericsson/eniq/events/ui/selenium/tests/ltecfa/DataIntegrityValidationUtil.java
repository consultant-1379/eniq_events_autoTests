package com.ericsson.eniq.events.ui.selenium.tests.ltecfa;

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
    protected static Logger dataLogger = Logger.getLogger(SeleniumLogger.class.getSimpleName());

    final static DBPersistor dbPersistor = DBPersistor.getInstatnce();

    private List<String> listOfEventTypesInResvData = new ArrayList<String>();

    public final String LOCAL_FILE_PATH_OF_LTERESERVED_DATA = "C:/Users/eeirmen/git/CAR/eniq_events_autoTests/eniq-events-selenium/src/main/resources/reserveDataLTECFA.csv";
    public final String UNIX_FILE_PATH_OF_LTERESERVED_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reserveDataLTECFA.csv";
    public final String LOCAL_FILE_PATH_OF_LTERESERVED_ERAB_DATA = "C:/Users/eeirmen/git/CAR/eniq_events_autoTests/eniq-events-selenium/src/main/resources/reserveDataErabLTECFA.csv";
    public final String UNIX_FILE_PATH_OF_LTERESERVED_ERAB_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reserveDataErabLTECFA.csv";




    public enum FailureType {CALL_SETUP_FAILURES, CALL_DROPS, RECURRING_FAILURES};

    final List<String> headersOnIMSIFailedEventAnalysisWindow_RRC_CONN_SETUP
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.DURATION,
            GuiStringConstants.RRC_ESTABL_CAUSE,GuiStringConstants.GUMMEI_Type_Desc)); //New Column Gummei Type added for 13B


    final List<String> headersOnIMSIFailedEventAnalysisWindow_UE_CTXT_RELEASE
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR, "Number of ERABs", GuiStringConstants.DURATION,GuiStringConstants.TTI_Bundling_Mode_Desc,    //New Column TTI Bundling Mode added for 13B
            "Internal Release Cause", "Triggering Node", "Number of ERABs", "ERAB Data Lost Bitmap", "ERAB Data Lost",
            "ERAB Release Success", "Number Of Failed ERABs", "Number Of ERABs With Data Lost"));


    final List<String> headersOnIMSIFailedEventAnalysisWindow_ERAB_SETUP
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
             GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR,  "Number of ERABs", GuiStringConstants.CAUSE_CODE, GuiStringConstants.DURATION,
            "Accumulated Up Link Requested GBR", "Accumulated Up Link Admitted GBR",
            "Accumulated Down Link Requested GBR", "Accumulated Down Link Admitted GBR"));

    final List<String> headersOnIMSIFailedEventAnalysisWindow_INITIAL_CTXT_SETUP
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR, "Number of ERABs",
            GuiStringConstants.MCC, GuiStringConstants.MNC, GuiStringConstants.DURATION,
            "Accumulated Up Link Requested GBR", "Accumulated Up Link Admitted GBR",
            "Accumulated Down Link Requested GBR", "Accumulated Down Link Admitted GBR"));

    final List<String> headersOnIMSIFailedEventAnalysisWindow_S1_SIG_CONN_SETUP
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.CAUSE_CODE, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.RAN_VENDOR,  GuiStringConstants.DURATION,
            GuiStringConstants.RRC_ESTABL_CAUSE));


    final List<String> headersOnERABEventAnalysisWindow
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            "Setup Result", "Setup Request QCI",
            "Setup Request ARP", "Setup Request PCI", "Setup Request PVI",
            "Failure 3GPP Cause", "Setup Attempted Access Type", "Setup Successful Access Type", "Setup Failure 3GPP Cause Group"));

    final List<String> headersOnUeERABEventAnalysisWindow
    = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE, "Setup Result", "Setup Request QCI", "Setup Request ARP", "Setup Request PCI", "Setup Request PVI"));

    private List<Map<String, String>> reserveDataList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> reserveERABdataList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> eventVolReserveDataList = new ArrayList<Map<String, String>>();

    private Map<String, List<Map<String, String>>> drillDownResvDataMap = new HashMap<String, List<Map<String, String>>>();

    private Map<String, List<Map<String, String>>> qciFailureStatsMap = new HashMap<String, List<Map<String, String>>>();

    List<Map<String, String>> topologyInformation;
    List<Map<String, String>> ranInformation;
    List<Map<String, String>> tacInformation;

    // Initialize Date
    Date gStartDate = new Date();
    String gStartDateTimeCandidate = "";
    Date gEndDate = new Date();
    String gEndDateTimeCandidate = "";

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

        logger.log(Level.INFO, "Size of reserveDataList : " + reserveDataList.size());
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


    public void resetUI() {
       
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
    public boolean validateIMSIEventAnalysisWindowData(String imsi, int timeInMinutes, List<Map<String, String>> imsiEventFailureList) throws NoDataException, PopUpException
    {
        if (imsiEventFailureList.size() == 0)
        {
            logger.log(Level.SEVERE, "NO DATA FOUND");
            return false;
        }

        checkEventTypesConfiguredInResvData(GuiStringConstants.IMSI, imsi);

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

            // Check if EventType displayed in UI is matching with the event type configured in reserved data
            if (!listOfEventTypesInResvData.contains(eventType))
            {

            }

            // Get the failure count of a given EventType configured in the reserved data.
            int resvDataFailureCount = checkNoOfFailuresForEventType(GuiStringConstants.IMSI, imsi, eventType);

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

    public boolean validateCallFailureAnalysisERABsData(String imsi, String eventType, List<Map<String, String>> ERABdataList) throws NoDataException, PopUpException
    {
        String tac = "", terminalMake = "", terminalModel = "", neVersion = "";
        List<Map<String, String>> resvDataERABList = new ArrayList<Map<String, String>>();

        // Since below headers are not available in ERAB reserve data sheet, get the below header data from normal reserveData.
        for (Map<String, String> resvDataMap : reserveDataList)
        {
            if (imsi.equals(resvDataMap.get(GuiStringConstants.IMSI)))
            {
                tac = resvDataMap.get(GuiStringConstants.TAC);
                terminalMake = resvDataMap.get(GuiStringConstants.TERMINAL_MAKE);
                terminalModel = resvDataMap.get(GuiStringConstants.TERMINAL_MODEL);
                neVersion = resvDataMap.get("NE_VERSION");
                break;
            }
        }

        for (Map<String, String> resvERABmap : reserveERABdataList)
        {
            if (eventType.equals(resvERABmap.get(GuiStringConstants.EVENT_TYPE)))
            {
                resvERABmap.put(GuiStringConstants.IMSI, imsi);

                Map<String, String> resvDataERABmap = new HashMap<String, String>();
                resvDataERABmap.put(GuiStringConstants.IMSI, imsi);

                List<String> listOfERABheaders = new ArrayList<String>();

                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    listOfERABheaders = headersOnUeERABEventAnalysisWindow;
                }
                else
                {
                    listOfERABheaders = headersOnERABEventAnalysisWindow;
                }

                for (String header : listOfERABheaders)
                {
                    String resvDataValue = resvERABmap.get(header);

                    if (neVersion.equals("11B"))
                    {
                        if (header.equals("Setup Request PCI") || header.equals("Setup Request PVI") || header.equals("Failure 3GPP Cause"))
                        {
                            resvDataValue = "";
                        }
                    }

                    // Since below headers are not available in ERAB reserve data sheet, get the below header data from normal reserveData.
                    if (header.equals(GuiStringConstants.IMSI))
                    {
                        resvDataValue = imsi;
                    }
                    else if (header.equals(GuiStringConstants.TAC))
                    {
                        resvDataValue = tac;
                    }
                    else if (header.equals(GuiStringConstants.TERMINAL_MAKE))
                    {
                        resvDataValue = terminalMake;
                        continue;
                    }
                    else if (header.equals(GuiStringConstants.TERMINAL_MODEL))
                    {
                        resvDataValue = terminalModel;
                    }
                    else if (header.equals("NE_VERSION"))
                    {
                        resvDataValue = neVersion;
                    }

                    resvDataERABmap.put(header, resvDataValue);
                }
                // Add resvDataMap to List
                resvDataERABList.add(resvDataERABmap);
            }
        }

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> ERABmap : ERABdataList)
        {
            ERABmap.remove(GuiStringConstants.EVENT_TIME);
            ERABmap.remove(GuiStringConstants.ACCESS_AREA);
            ERABmap.remove(GuiStringConstants.CONTROLLER);
            ERABmap.remove(GuiStringConstants.RAN_VENDOR);

            ERABmap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (resvDataERABList.contains(ERABmap))
            {
                   logger.log(Level.SEVERE, "SUCCESS - Match Found");
            }
            else
            {
                for(Map<String, String> mapToPrint : resvDataERABList)
                {
                    printMapData(mapToPrint, "RESERVE DATA MAP");
                }
                printMapData(ERABmap, "UI DATA MAP");
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                return false;
            }
        }

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
            logger.log(Level.SEVERE, "Invalid Event Time for timeInMinutes");
            return false;
        }

        List<Map<String, String>> resvDataList = getResvDataForEventType(GuiStringConstants.IMSI, imsi, eventType);

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiEventFailureList)
        {
            // Remove event_time before validating ui data with reference data
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (resvDataList.contains(eventFailureMap))
            {
                logger.log(Level.INFO, "UI Data matched with Reserved Data");
            }
            else
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                for(Map<String, String> mapToPrint : resvDataList)
                {
                    printMapData(mapToPrint, "RESERVE DATA MAP");
                }
                printMapData(eventFailureMap, "UI DATA MAP");
                return false;
            }
        }

        return true;
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
            logger.log(Level.SEVERE, "Incorrect Event Time");
            return false;
        }

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> eventFailureMap : imsiGroupEventFailureList)
        {
            // Remove EVENT_TIME column validation with reserve data
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (!imsiGroupList.contains(eventFailureMap.get(GuiStringConstants.IMSI)))
            {
                logger.log(Level.SEVERE, "IMSI not found in IMSI Group");
                return false;
            }

            List<Map<String, String>> resvDataList = getResvDataForEventType(GuiStringConstants.IMSI, eventFailureMap.get(GuiStringConstants.IMSI), eventType);

            for (final Map<String, String> resvDataMap : resvDataList)
            {
                if (eventFailureMap.equals(resvDataMap))
                {
                    matched = true;
                    logger.log(Level.INFO, "SUCCESS - Match Found");
                }
            }

            if (!matched)
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                for (final Map<String, String> resvDataMap : resvDataList)
                {
                    printMapData(resvDataMap, "RESERVE DATA MAP");
                }
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
    public boolean validateIMSIGroupEventAnalysisWindowData(List<String> imsiGroupList, List<Map<String, String>> imsiGroupEventFailureList, int timeInMinutes)
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
            checkEventTypesConfiguredInResvData(GuiStringConstants.IMSI, imsi);
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

            // Check if EventType displayed in UI is matching with the event type configured in reserved data
            if (!listOfEventTypesInResvData.contains(eventType))
            {
                logger.log(Level.SEVERE, "Event Type " + eventType + " is not present in the reserved data list.");
                return false;
            }

            int resvDataFailureCount = 0;
            int resvDataImpactedSubscribers = 0;

            for (String imsi : imsiGroupList)
            {
                // Get the failure count of a given EventType configured in the reserved data.
                int failureCountOfSubscriber =  checkNoOfFailuresForEventType(GuiStringConstants.IMSI, imsi, eventType);
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


    /**
     * Description: To validate data displayed on IMSIGroup Event Analysis window
     * @param imsiGroupList
     * @param imsiGroupEventFailureList
     * @return
     * @throws NoDataException
     * @throws PopUpException
     * @throws IOException
     */
    public boolean validateDrillDownEventAnalysisWindow(String drillDownValue, String drillDownMember, List<Map<String, String>> listOfDrillDownData, int timeInMinutes)
    {
        if (listOfDrillDownData.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfDrillDownData)
        {
            Map<String, String> reserveDataMap = checkNoOfFailuresForDrillDownMember(uiDataMap.get(drillDownMember), uiDataMap.get(GuiStringConstants.EVENT_TYPE), drillDownMember, timeInMinutes);


            if (drillDownMember.equals(GuiStringConstants.TRACKING_AREA))
            {
                reserveDataMap.remove(GuiStringConstants.RAN_VENDOR);
            }

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

    public boolean validateDrillDownEventAnalysiSummaryWindow(String imsi, String drillDownMember, List<Map<String, String>> listOfDrillDownData, int timeInMinutes)
    {
        if (listOfDrillDownData.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfDrillDownData)
        {
            String drillDownValue = uiDataMap.get(drillDownMember);
            Map<String, String> reserveDataMap = checkNoOfSummaryFailuresForDrillDownMember(drillDownValue, uiDataMap.get(GuiStringConstants.CATEGORY), drillDownMember, timeInMinutes);

            // If UI data does not match with reserve data then
            if (drillDownMember.equals(GuiStringConstants.TRACKING_AREA))
            {
                reserveDataMap.remove(GuiStringConstants.RAN_VENDOR);
            }

            if (!uiDataMap.equals(reserveDataMap))
            {
                printMapData(uiDataMap, "UI DATA");
                printMapData(reserveDataMap, "RESERVED DATA");
                return false;
            }
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
                    try {
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
                    } catch (ParseException e) {
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
                                List<String> listOfImpactedSubscriber = impactedSubscrMap.get(eventTime);

                                if (!listOfImpactedSubscriber.isEmpty() && !listOfImpactedSubscriber.contains(imsi))
                                {
                                    listOfImpactedSubscriber.add(imsi);
                                    resvEvtVolMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS,  Integer.toString(listOfImpactedSubscriber.size()));
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
                    resvEventVolumeDataMap.put(GuiStringConstants.RRC_CONN_SETUP + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.INITIAL_CTXT_SETUP + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.S1_SIG_CONN_SETUP + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.ERAB_SETUP + failCount, "0");
                    resvEventVolumeDataMap.put(GuiStringConstants.UE_CTXT_RELEASE + failCount, "0");
                    if (!allNetworkElements)
                    {
                        resvEventVolumeDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, "0");
                    }
                    resvEventVolumeDataMap.put("Time", eventTime);

                    resvEventVolumeDataMap.put(eventType + failCount, "1");

                    resvEventVolumeDataList.add(resvEventVolumeDataMap);
                }
                matchFound = false;

            }
        }

        return resvEventVolumeDataList;
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

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (final Map<String, String> resvDataCauseCodeMap : reserveDataList)
        {
            boolean result = checkStringInStringArray(resvDataCauseCodeMap.get(drillDownMember), valueArray);


            // Check if the drillDown value is equal to value in reserve data
            if (result && eventType.equals(resvDataCauseCodeMap.get(GuiStringConstants.EVENT_TYPE))&&
                causeCodeDesc.equals(resvDataCauseCodeMap.get(GuiStringConstants.CAUSE_CODE)))
            {
                // Increment the number of failures for the given eventType
                noOfOccurrences++;
                String imsi = resvDataCauseCodeMap.get(GuiStringConstants.IMSI);
                // If not present, add the Subscriber to the ImpactedSubscriber List
                if ((!listOfImpactedSubscribers.contains(imsi)) && (!eventType.equals(GuiStringConstants.RRC_CONN_SETUP)))
                {
                    listOfImpactedSubscribers.add(imsi);
                }

                if (eventType.equals(GuiStringConstants.RRC_CONN_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_RRC_CONN_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_S1_SIG_CONN_SETUP;

                }
                else if (eventType.equals(GuiStringConstants.ERAB_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_ERAB_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_INITIAL_CTXT_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_UE_CTXT_RELEASE;
                }
                else
                {
                    // Throw error here - INVALID EVENT TYPE
                    logger.log(Level.INFO, "UNKNOWN EVENT TYPE");
                }

                // Create a new map containing specific to that eventType
                Map<String, String> resvDataMap = new HashMap<String, String>();

                for (final String header : listOfHeaders)
                {
                    String resvDataValue = resvDataCauseCodeMap.get(header);

                    if (header.equals(GuiStringConstants.NUMBER_OF_ERABS))
                    {
                        resvDataValue = getNumberOfERABsFromResvData(eventType, resvDataCauseCodeMap.get("NE_VERSION"));
                    }

                    resvDataMap.put(header, resvDataValue);
                }

                resvDataMap.remove(GuiStringConstants.TERMINAL_MAKE);

                // Added rows of all these eventTypes into a new list
                drillDownResvDataList.add(resvDataMap);

                String causeCodeEventType = causeCodeDesc + "," + eventType;
                drillDownResvDataMap.put(causeCodeEventType, drillDownResvDataList);
            }
        }

        // Check the noOfOccurrences for a given time range

        noOfOccurrences = noOfOccurrences * (timeInMinutes/5);


        if (noOfOccurrences > 0)
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

        if (!validateEventTime(timeInMinutes, causeCodeDetailAnalysisList))
        {
            return false;
        }

        List<Map<String, String>> resvDataList = drillDownResvDataMap.get(causeCodeEventType);

        // Loop through all the rows in IMSI Event Analysis Window
        for (final Map<String, String> uiFailureMap : causeCodeDetailAnalysisList)
        {
            uiFailureMap.remove(GuiStringConstants.EVENT_TIME);
            uiFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);

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

    private Map<String, String> checkNoOfFailuresForDrillDownMember(String drillDownValue, String eventType, String drillDownMember, int timeInMinutes) {

        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, String> subscriberFailureMap = new HashMap<String, String>();

        String localDrillDownMember = drillDownMember;

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
                    subscriberFailureMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
                }

                // Increment the number of failures for the given eventType
                noOfFailures++;

                // If not present, add the Subscriber to the ImpactedSubscriber List
                if ((!listOfImpactedSubscribers.contains(resvDataMap.get(GuiStringConstants.IMSI))) && (!eventType.equals(GuiStringConstants.RRC_CONN_SETUP)))
                {
                    listOfImpactedSubscribers.add(resvDataMap.get(GuiStringConstants.IMSI));
                }
            }
        }

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        if (noOfFailures > 0) 
        {
            subscriberFailureMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
            subscriberFailureMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));
        }

        return subscriberFailureMap;
    }

private Map<String, String> checkNoOfSummaryFailuresForDrillDownMember(String drillDownValue, String categoryIdDesc, String drillDownMember, int timeInMinutes) {

        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, String> subscriberFailureMap = new HashMap<String, String>();

        String localDrillDownMember = drillDownMember;

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            String resvDataEventType =  resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            // Check if the drillDown value is equal to value in reserve data
            if (drillDownValue.equals(resvDataMap.get(localDrillDownMember)))
            {
                if (categoryIdDesc.equals("Call Setup Failures") &&
                        resvDataEventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
                else if (categoryIdDesc.equals("Call Drops") &&
                        (resvDataEventType.equals(GuiStringConstants.RRC_CONN_SETUP) ||
                         resvDataEventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP) ||
                         resvDataEventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP)))
                {
                    continue;
                }

                // Add RAN Vendor to Map
                subscriberFailureMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
                // Add DrillDown Member
                subscriberFailureMap.put(drillDownMember, drillDownValue);
                // If its eCell drill down then add Controller to the map, since the UI contains Controller in eCell drill down.
                if (drillDownMember.equals(GuiStringConstants.ACCESS_AREA))
                {
                    subscriberFailureMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
                }

                // Increment the number of failures for the given eventType
                noOfFailures++;

                // If not present, add the Subscriber to the ImpactedSubscriber List
                if (!listOfImpactedSubscribers.contains(resvDataMap.get(GuiStringConstants.IMSI)))
                {
                    listOfImpactedSubscribers.add(resvDataMap.get(GuiStringConstants.IMSI));
                }
            }
        }

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        if ((noOfFailures > 0) && !listOfImpactedSubscribers.isEmpty())
        {
            subscriberFailureMap.put(GuiStringConstants.CATEGORY, categoryIdDesc);
            subscriberFailureMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
            subscriberFailureMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));
        }

        return subscriberFailureMap;
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

            Map<String, String> resvDataMap = getFailuresAndImpactedSubscribers(qciFailureStatsMap, uiDataMap.get("QCI ID"), uiDataMap.get("QCI Description"),
                    uiDataMap.get(GuiStringConstants.FAILURE_CATEGORY), timeInMinutes);

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

    private Map<String, String> getFailuresAndImpactedSubscribers(Map<String, List<Map<String, String>>> qciFailureStatsMap,
                                                                String qciValue, String qciDesc, String category, int timeInMinutes)
    {
        int noOfFailures = 0;
        List<Map<String, String>> listOfERABFailures = qciFailureStatsMap.get(qciValue);
        List<String> listOfImpactedSubscribers = new ArrayList<String>();

        for(Map<String, String> erabFailureMap : listOfERABFailures)
        {

            String eventType = erabFailureMap.get(GuiStringConstants.EVENT_TYPE);
            if (category.equals(GuiStringConstants.CALL_SETUP_FAILURES) )
            {

                if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP) ||
                    eventType.equals(GuiStringConstants.ERAB_SETUP))
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

                if (eventType.equals(GuiStringConstants.ERAB_SETUP))
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
        resvDataQCIMap.put(GuiStringConstants.FAILURE_CATEGORY, category);

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        resvDataQCIMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
        resvDataQCIMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));

        return resvDataQCIMap;
    }

    public boolean validateEventAnalysisPerQCIWindow(String qciValue, List<Map<String, String>> listOfEventAnalysisPerQCI, int timeInMinutes)
    {
        if (listOfEventAnalysisPerQCI.isEmpty() ||
            qciFailureStatsMap.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfEventAnalysisPerQCI)
        {
            Map<String, String> resvDataMap = getFailuresAndImpactedSubscribersEventAnalysis(qciFailureStatsMap, qciValue, uiDataMap.get(GuiStringConstants.EVENT_TYPE), timeInMinutes);

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

    private Map<String, String> getFailuresAndImpactedSubscribersEventAnalysis(Map<String, List<Map<String, String>>> qciFailureStatsMap,
                                                                       String qciValue, String uiEventType, int timeInMinutes)
    {
        int noOfFailures = 0;
        List<Map<String, String>> listOfERABFailures = qciFailureStatsMap.get(qciValue);
        List<String> listOfImpactedSubscribers = new ArrayList<String>();

        for(Map<String, String> erabFailureMap : listOfERABFailures)
        {
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

        Map<String, String> resvDataQCIMap = new HashMap<String, String>();

        // Check the NoOfFailures for a given time range
        noOfFailures = noOfFailures * (timeInMinutes/5);

        resvDataQCIMap.put(GuiStringConstants.EVENT_TYPE, uiEventType);
        resvDataQCIMap.put(GuiStringConstants.FAILURES, Integer.toString(noOfFailures));
        resvDataQCIMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, Integer.toString(listOfImpactedSubscribers.size()));

        return resvDataQCIMap;
    }

    public boolean validateFailuresPerQCIWindow(String qciValue, List<Map<String, String>> listOfFailuresPerQCI, int timeInMinutes)
    {
        if (listOfFailuresPerQCI.isEmpty() ||
            qciFailureStatsMap.isEmpty())
        {
            return false;
        }

        List<Map<String, String>> listOfResvERABFailures = qciFailureStatsMap.get(qciValue);

        for (Map<String, String> uiDataMap : listOfFailuresPerQCI)
        {
            uiDataMap.remove(GuiStringConstants.EVENT_TIME);
            uiDataMap.remove(GuiStringConstants.TERMINAL_MAKE);

            if (listOfResvERABFailures.contains(uiDataMap))
            {
                logger.log(Level.INFO, "Event Analysis QCI Statistics is matching Reserve Data");
            }
            else
            {
                for(Map<String, String> mapToPrint : listOfResvERABFailures)
                {
                    printMapData(mapToPrint, "RESERVE DATA MAP");
                }
                printMapData(uiDataMap, "UI DATA");
                logger.log(Level.SEVERE, "Event Analysis QCI Statistics is NOT matching Reserve Data");
                return false;
            }
        }
        return true;
    }

    public void getERABfailureAnalysisPerQCIFromResvData(String neType, String valueArray[])
    {
        // Cell Trace iterator
        List<Map<String, String>> reserveERABdataListCopy = new ArrayList<Map<String, String>>(reserveERABdataList);
        Collections.copy(reserveERABdataListCopy, reserveERABdataList);

        // Get the count of each QCI present in reserved data
        for (final Map<String, String> erabReserveDataMap : reserveERABdataListCopy)
        {
            List<Map<String, String>> CallFailureAnalysisERABList = new ArrayList<Map<String, String>>();

            String resvQCI = erabReserveDataMap.get("Setup Request QCI");

            if (qciFailureStatsMap.containsKey(resvQCI))
            {
                CallFailureAnalysisERABList = qciFailureStatsMap.get(resvQCI);
            }

            String eventType = erabReserveDataMap.get(GuiStringConstants.EVENT_TYPE);
            if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
            {
                erabReserveDataMap.remove("Failure 3GPP Cause");
                erabReserveDataMap.remove("Setup Attempted Access Type");
                erabReserveDataMap.remove("Setup Successful Access Type");
                erabReserveDataMap.remove("Setup Failure 3GPP Cause Group");
            }


            for (int i=0; i<valueArray.length; i++)
            {

                for (final Map<String, String> reserveDataMap : reserveDataList)
                {
                    if (valueArray[i].equals(reserveDataMap.get(neType)) &&
                            eventType.equals(reserveDataMap.get(GuiStringConstants.EVENT_TYPE)))
                    {
                        String imsi = reserveDataMap.get(GuiStringConstants.IMSI);

                        Map<String, String> CallFailureERABmap = new HashMap<String, String>();
                        CallFailureERABmap.put(GuiStringConstants.IMSI, imsi);
                        CallFailureERABmap.put(GuiStringConstants.TAC, reserveDataMap.get(GuiStringConstants.TAC));
                        CallFailureERABmap.put(GuiStringConstants.TERMINAL_MODEL, reserveDataMap.get(GuiStringConstants.TERMINAL_MODEL));
                        CallFailureERABmap.put(GuiStringConstants.CONTROLLER, reserveDataMap.get(GuiStringConstants.CONTROLLER));
                        CallFailureERABmap.put(GuiStringConstants.ACCESS_AREA, reserveDataMap.get(GuiStringConstants.ACCESS_AREA));
                        CallFailureERABmap.put(GuiStringConstants.RAN_VENDOR, reserveDataMap.get(GuiStringConstants.RAN_VENDOR));

                        String neVersion = reserveDataMap.get("NE_VERSION");

                        if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE) && !neVersion.equals("12B"))
                        {
                            continue;
                        }

                        if (neVersion.equals("11B"))
                        {
                            erabReserveDataMap.put("Setup Request PCI", "");
                            erabReserveDataMap.put("Setup Request PVI", "");
                            erabReserveDataMap.put("Failure 3GPP Cause", "");
                        }

                        if (neVersion.equals("12B"))
                        {
                            erabReserveDataMap.remove("Setup Result");
                            erabReserveDataMap.remove("Setup Request QCI");
                        }

                        else
                        {
                            erabReserveDataMap.remove("Release Request QCI");
                            erabReserveDataMap.put("Setup Successful Access Type", "EVENT_VALUE_NORMAL_ACCESS");
                            erabReserveDataMap.put("Setup Attempted Access Type", "EVENT_VALUE_NORMAL_ACCESS");
                            erabReserveDataMap.put("Setup Failure 3GPP Cause Group", "EVENT_VALUE_TRANSPORT_LAYER_GROUP");
                        }

                        CallFailureERABmap.putAll(erabReserveDataMap);

                        CallFailureAnalysisERABList.add(CallFailureERABmap);
                    }
                }
            }

            qciFailureStatsMap.put(resvQCI, CallFailureAnalysisERABList);
        }

    }


    //////////////////////////////////////////////////////////////////////////////
    //                       TERMINAL TAB                            //
    //////////////////////////////////////////////////////////////////////////////

    public boolean validateTerminalEventAnalysisWindow(String tacArray[], List<Map<String, String>> listOfDrillDownData, int timeInMinutes)
    {
        if (listOfDrillDownData.isEmpty())
        {
            return false;
        }

        for (Map<String, String> uiDataMap : listOfDrillDownData)
        {
            Map<String, String> reserveDataMap = checkNoOfFailuresOnTerminalEventAnalysis(tacArray, uiDataMap.get(GuiStringConstants.EVENT_TYPE), timeInMinutes);

            // Remove validation for EVENT_ID
            uiDataMap.remove(GuiStringConstants.EVENT_ID);

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

    private Map<String, String> checkNoOfFailuresOnTerminalEventAnalysis(String tacArray[], String eventType, int timeInMinutes) {

        int noOfFailures = 0;
        boolean subscriberAlreadyInList = false;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, String> subscriberFailureMap = new HashMap<String, String>();

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            boolean result = checkStringInStringArray(resvDataMap.get(GuiStringConstants.TAC), tacArray);

            // Check if the drillDown value is equal to value in reserve data
            if (result && eventType.equals(resvDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                subscriberFailureMap.put(GuiStringConstants.MANUFACTURER,  resvDataMap.get(GuiStringConstants.TERMINAL_MAKE));
                subscriberFailureMap.put("Model",  resvDataMap.get(GuiStringConstants.TERMINAL_MODEL));
                // Add EventType
                subscriberFailureMap.put(GuiStringConstants.EVENT_TYPE, eventType);

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
    //                        RANKINGS TAB                           //
    //////////////////////////////////////////////////////////////////////////////

    /* SUBSCRIBER RANKING */
    public boolean validateSubscriberRanking(List<Map<String, String>> listOfSubscriberRanking, String typeOfFailure, int timeInMinutes)
    {
        if (listOfSubscriberRanking.size() == 0)
        {
            logger.log(Level.SEVERE, "listOfSubscriberRanking is EMPTY");
        }

        Map<String, Integer> eventTypeFailuresMap = new HashMap<String, Integer>();
        int noOfFailures = 0;



        // Get Number of Failures for each IMSI
        for (Map <String, String> resvDataMap :  reserveDataList)
        {
            noOfFailures = 0;
            String imsi = resvDataMap.get(GuiStringConstants.IMSI);

            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else
            {
                // throw error
                return false;
            }

            if (eventTypeFailuresMap.containsKey(imsi))
            {
                noOfFailures = eventTypeFailuresMap.get(imsi);
            }

            noOfFailures++;

            if (!imsi.isEmpty()){
            eventTypeFailuresMap.put(imsi, noOfFailures);
            }
        }

        if (eventTypeFailuresMap.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - eventTypeFailuresMap is Empty");
            return false;
        }


        List<Map<String, String>> resvDataSubscriberRankingList = getResvDataRankingBasedOnFailures(eventTypeFailuresMap, timeInMinutes, GuiStringConstants.IMSI);

        if (resvDataSubscriberRankingList.size() == 0)
        {
            logger.log(Level.SEVERE, "ERROR - resvDataSubscriberRankingList is Empty");
            return false;
        }




        // Compare UI List of Maps with ResvData List Of Maps
        for (Map <String, String> SubscriberRankingMap : listOfSubscriberRanking)
        {

            String imsi = SubscriberRankingMap.get(GuiStringConstants.IMSI);

            for (Map<String, String> resvDataSubscriberRankingMap : resvDataSubscriberRankingList)
            {
                printMapData(resvDataSubscriberRankingMap, "RESERVE DATA MAP");
                if (imsi.equals(resvDataSubscriberRankingMap.get(GuiStringConstants.IMSI)))
                {
                    if (!SubscriberRankingMap.equals(resvDataSubscriberRankingMap))
                    {
                        logger.log(Level.SEVERE, "SubscriberRankingMap DOES NOT MATCH with resvDataSubscriberRankingMap");
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


    /* CONTROLLER RANKING */

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
            String eNodeB = resvDataMap.get(GuiStringConstants.CONTROLLER);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            // Maintain a Map of CONTROLLER and No. Of Failures from reserved data
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

            // Maintain a Map of CONTROLLER and RAN Vendor
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

            // Change eNodeB to Controller
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
                printMapData(resvDataEnodeBRankingMap, "RESERVE DATA MAP");
                if (eNodeB.equals(resvDataEnodeBRankingMap.get(GuiStringConstants.CONTROLLER)))
                {
                    if (!eNodeBRankingMap.equals(resvDataEnodeBRankingMap))
                    {
                        logger.log(Level.SEVERE, "eNodeBRankingMap DOES NOT MATCH with resvDataEnodeBRankingMap");
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

    public boolean validateEcellRanking(List<Map<String, String>> listOfEcellRanking, String typeOfFailure, int timeInMinutes)
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
            String eCell = resvDataMap.get(GuiStringConstants.ACCESS_AREA);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Unknown Failure Type");
                return false;
            }

            // Maintain a Map of CONTROLLER and No. Of Failures from reserved data
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

            // Maintain a Map of CONTROLLER and RAN Vendor
            // Check if eNodeB is already inserted in Map
            if (!vendorMap.containsKey(eCell))
            {
                // Put the RAN Vendor into Map
                vendorMap.put(eCell, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
            }

            // Maintain a Map of ECELL and CONTROLLER
            // Check if eNodeB is already inserted in Map
            if (!eNodeBMap.containsKey(eCell))
            {
                // Put the RAN Vendor into Map
                eNodeBMap.put(eCell, resvDataMap.get(GuiStringConstants.CONTROLLER));
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
            String eCell =  eCellRankingMap.get(GuiStringConstants.ACCESS_AREA);

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
            String eCell = eCellRankingMap.get(GuiStringConstants.ACCESS_AREA);

            for (Map<String, String> resvDataEcellRankingMap : resvDataEcellRankingList)
            {
                printMapData(resvDataEcellRankingMap, "RESERVE DATA MAP");
                if (eCell.equals(resvDataEcellRankingMap.get(GuiStringConstants.ACCESS_AREA)))
                {
                    if (!eCellRankingMap.equals(resvDataEcellRankingMap))
                    {
                        logger.log(Level.SEVERE, "eCellRankingMap DOES NOT MATCH with resvDataEcellRankingMap");
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

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
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
                printMapData(resvDataTrackingAreaRankingMap, "RESERVE DATA MAP");
                if (trackingArea.equals(resvDataTrackingAreaRankingMap.get(GuiStringConstants.TRACKING_AREA)))
                {
                    if (!trackingAreaRankingMap.equals(resvDataTrackingAreaRankingMap))
                    {
                        logger.log(Level.SEVERE, "trackingAreaRankingMap DOES NOT MATCH with resvDataTrackingAreaRankingMap");
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
            String causeCode = resvDataMap.get(GuiStringConstants.CAUSE_CODE);
            String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);
            String causeCodeEventType = causeCode + "," + eventType;

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE) ||
                    eventType.equals(GuiStringConstants.ERAB_SETUP))
                {
                    continue;
                }
            }
            else
            {
                logger.log(Level.SEVERE, "Uknown Failure Type");
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
                        printMapData(causeCodeRankingMap, "UI DATA MAP");
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

            if (typeOfFailure.equals(GuiStringConstants.CALL_DROPS))
            {
                if (!eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    continue;
                }
            }
            else if (typeOfFailure.equals(GuiStringConstants.CALL_SETUP_FAILURES))
            {
                if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
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
            if (!tac.isEmpty()) {
                numberOfFailuresMap.put(tac, noOfFailures);
            }

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

        logger.log(Level.INFO, "----------------------------------------------------------numberOfFailuresMap = " + numberOfFailuresMap);

        List<Map<String, String>> resvDataTerminalRankingList = getResvDataRankingBasedOnFailures(numberOfFailuresMap, timeInMinutes, GuiStringConstants.TAC);

        for (Map<String, String> terminalRankingMap : resvDataTerminalRankingList)
        {
            String tac =  terminalRankingMap.get(GuiStringConstants.TAC);

            // Get the vendor corresponding to this eNodeB
            String manufacturer = manufacturerMap.get(tac);
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
            terminalRankingMap.remove(GuiStringConstants.MANUFACTURER);

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
                                                              String networkEntityType, String eventType, int timeInMinutes)
                                                              throws NoDataException, PopUpException, IOException, ParseException
    {
        boolean matchFound = false;

        int count = 1;

        System.out.println("networkGroupList = " + networkGroupList);

        for (final Map<String, String> eventFailureMap : networkGroupEventFailureList)
        {

            // Remove EVENT_TIME column validation with reserve data
            eventFailureMap.remove(GuiStringConstants.EVENT_TIME);
            eventFailureMap.remove(GuiStringConstants.TERMINAL_MAKE);


            for (String networkEntity : networkGroupList)
            {

                List<Map<String, String>> resvDataList = getResvDataForEventType(networkEntityType, networkEntity, eventType);

                if (resvDataList.size() != 0){
                if (resvDataList.contains(eventFailureMap))
                {
                    logger.log(Level.INFO, "SUCCESS - Match Found");
                    matchFound = true;
                    break;
                }

                logger.log(Level.INFO, "NOT SUCCESS - Match Not Found");
                for (final Map<String, String> resvDataMap : resvDataList)
                {
                    printMapData(resvDataMap, "RESERVE DATA MAP");
                }
                printMapData(eventFailureMap, "UI DATA MAP");
                }
                else{
                    logger.log(Level.INFO, "resvDataList empty for networkEntity = " + networkEntity);
                }

            }

            if (!matchFound)
            {
                logger.log(Level.SEVERE, "Record found in FailureEventAnalysis window is incorrect");
                return false;
            }

            matchFound = false;
            count ++;
        }

        return true;

    }

    /**
     * Description: To validate data displayed on Network Group(CONTROLLER or ECELL) Event Analysis window
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
            logger.log(Level.INFO, "networkEntity : " + networkEntity);
            checkEventTypesConfiguredInResvData(networkEntityType, networkEntity);
        }

        // Loop through all the rows in IMSI Event Analysis Window
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

            // Check if EventType displayed in UI is matching with the event type configured in reserved data

            if (!listOfEventTypesInResvData.contains(eventType))
            {
                logger.log(Level.SEVERE, "Event Type " + eventType + " is not present in the reserved data list.");
                return false;
            }

            // Get the failure count of a given EventType configured in the reserved data.
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
     * Set date and time in time dialog window with a lag of configurable time.
     * For example : By selecting 15 mins on EventAnalysis window, it will display
     *               data with a specific time lag.
     *               i.e if current time is 11:30, ideally it should display data between 11:15 and 11:30,
     *               but due to delay in displaying of UI at current time, it fails to display all the expected data till 11:30.
     *               Therefore to display all the expected datathis function creates a time lag for example of 5 mins and
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


        int offset = 0;

        if (timeRange == TimeRange.FIVE_MINUTES) {
            logger.log(Level.WARNING, "Cannot set TimeRange for 5 mins due to UI constraints.");
            return;
        }

        int hourOffset = TimeZone.getDefault().getOffset(cal.getTime().getTime())/(1000*60*60);
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)-hourOffset);

        cal.set(Calendar.SECOND, 0);

        final Date currentDate = cal.getTime();

        logger.log(Level.INFO, "Current Date : " + currentDate);

        final long currentTime = currentDate.getTime(); // gets time in milliseconds

        if (timeRange == TimeRange.FIFTEEN_MINUTES || timeRange == TimeRange.THIRTY_MINUTES ||
                timeRange == TimeRange.ONE_HOUR || timeRange == TimeRange.TWO_HOURS)
        {
            offset = 10;
        }
        else
        {
            offset = 30;
        }

        final int timeRangeInMins = timeRange.getMiniutes();

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
    //                               TOPOLOGY DATA                                //
    //////////////////////////////////////////////////////////////////////////////

    @Test
    public void updateTopologyChanges()
    {
        final String queryForTopologyData = "select DISTINCT ACCESS_AREA_ID,ENODEB_ID,MCC,MNC,TRAC,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_LTE_HIER321 where upper(STATUS) = 'ACTIVE'";
        final String topologyColumns = "MCC,MNC,ACCESS_AREA_ID,ENODEB_ID,TRAC,HIERARCHY_1,HIERARCHY_3,VENDOR";
        topologyInformation = dbPersistor.queryLteTopologyData(topologyColumns, queryForTopologyData);

        final String queryForTac = "select top 100 TAC, MANUFACTURER, MARKETING_NAME from DIM_E_SGEH_TAC  where TAC>10000000 order by tac";
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

        // TAC Iterator
        Iterator tacIter = tacInformation.iterator();

        Map<String, String> cellTraceMap = new HashMap<String, String>();
        Map<String, String> ctumMap = new HashMap<String, String>();
        Map<String, String> tacMap = new HashMap<String, String>();

        final int noOfNodes = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_NODES));
        maxSubscribers = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_SUBSCRIBERS));
        final int noOfInternalProcRrcConnSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_RRC_CONN_SETUP_EVENTS));
        final int noOfInternalProcS1SigConnSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_S1_SIG_CONN_SETUP_EVENTS));
        final int noOfInternalProcInitialCtxtSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_INITIAL_CTXT_SETUP_EVENTS));
        final int noOfInternalProcErabSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_ERAB_SETUP_EVENTS));
        final int noOfInternalProcUeCtxtReleaseEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_UE_CTXT_RELEASE_EVENTS));

        final String causeCodeRrcConnSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_RRC_CONN_SETUP);
        final String causeCodeS1SigConnSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_S1_SIG_CONN_SETUP);
        final String causeCodeInitialCtxtSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_INITIAL_CTXT_SETUP);
        String causeCodeUeCtxtRelease = "";
        if (neVersion.equals("12A") || neVersion.equals("12B")||neVersion.equals("13A")||neVersion.equals("13B"))
        {
            causeCodeUeCtxtRelease = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_UE_CTXT_RELEASE_12A);
        }
        else
        {
            causeCodeUeCtxtRelease = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_UE_CTXT_RELEASE_11B);
        }
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
            csvRecordMap.put(GuiStringConstants.CAUSE_CODE, products.get(GuiStringConstants.CAUSE_CODE));
            csvRecordMap.put(GuiStringConstants.INTERNAL_RELEASE_CAUSE, products.get(GuiStringConstants.INTERNAL_RELEASE_CAUSE));
            csvRecordMap.put(GuiStringConstants.DURATION, products.get(GuiStringConstants.DURATION));
            csvRecordMap.put(GuiStringConstants.RRC_ESTABL_CAUSE, products.get(GuiStringConstants.RRC_ESTABL_CAUSE));
            if (neVersion.equals("13B")){
            csvRecordMap.put(GuiStringConstants.GUMMEI_Type_Desc, products.get(GuiStringConstants.GUMMEI_Type_Desc));
            csvRecordMap.put(GuiStringConstants.TTI_Bundling_Mode_Desc, products.get(GuiStringConstants.TTI_Bundling_Mode_Desc)); //Gummei_type and TTi Bundling VAlue needs to be hard coded as 0 in datagen.
            }
            csvRecordMap.put(GuiStringConstants.TRIGGERING_NODE, products.get(GuiStringConstants.TRIGGERING_NODE));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS, getNumberOfERABsFromResvData(products.get(GuiStringConstants.EVENT_TYPE), neVersion));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST_BITMAP, products.get(GuiStringConstants.ERAB_DATA_LOST_BITMAP));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST, products.get(GuiStringConstants.ERAB_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ERAB_RELEASE_SUCCESS, products.get(GuiStringConstants.ERAB_RELEASE_SUCCESS));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_FAILED_ERABS, products.get(GuiStringConstants.NUMBER_OF_FAILED_ERABS));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, products.get(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, products.get(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, products.get(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, products.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, products.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR));
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP , products.get(GuiStringConstants.CAUSE_3GPP ));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP , products.get(GuiStringConstants.CAUSE_GROUP_3GPP ));

            resvDataMap.put(products.get(GuiStringConstants.EVENT_TYPE),  csvRecordMap);
        }

        for (int node = 0; node < noOfNodes; node++)
        {
            cellTraceMap = (Map<String, String>)cellTraceIter.next();
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

                ctumMap = (Map<String, String>)ctumIter.next();
                tacMap = (Map<String, String>)tacIter.next();



                   if (noOfEvents < noOfInternalProcRrcConnSetupEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.RRC_CONN_SETUP, causeCodeRrcConnSetup, neVersion, resvDataMap);
                   }

                   if (noOfEvents <  noOfInternalProcS1SigConnSetupEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.S1_SIG_CONN_SETUP, causeCodeS1SigConnSetup, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcInitialCtxtSetupEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.INITIAL_CTXT_SETUP, causeCodeInitialCtxtSetup, neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcErabSetupEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.ERAB_SETUP, "", neVersion, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcUeCtxtReleaseEvents)
                   {
                       createEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.UE_CTXT_RELEASE, causeCodeUeCtxtRelease, neVersion, resvDataMap);
                   }

                   if ((noOfEvents >= noOfInternalProcRrcConnSetupEvents) &&
                       (noOfEvents >= noOfInternalProcS1SigConnSetupEvents) &&
                       (noOfEvents >= noOfInternalProcInitialCtxtSetupEvents) &&
                       (noOfEvents >= noOfInternalProcErabSetupEvents) &&
                       (noOfEvents >= noOfInternalProcUeCtxtReleaseEvents))
                   {
                       break;
                   }

                   noOfEvents++;
            }
        }
    }

    private String getImsi(Map<String, String> ctumMap, int imsiSeq)
    {

        String mccStr, mncStr;


        switch (imsiSeq) {
        case 41:
            mccStr = "344";
            mncStr = "930";
            break;
        case 42:
            mccStr = "310";
            mncStr = "560";
            break;
        case 43:
            mccStr = "240";
            mncStr = "210";
            break;
        default:
            mccStr = "310";
            mncStr = "410";
            break;
        }

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

    private void createEvent(Map<String, String> ctumMap, Map<String, String> tacMap, Map<String, String> cellTraceMap, int imsiSeq, String eventType, String causeCode, String neVersion, Map<String, Map<String, String>> resvDataMap)
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
        csvRecordMap.put(GuiStringConstants.EVENT_TIME, "12:"+Integer.toString(imsiSeq-1)+":00");

        String imsi = getImsi(ctumMap, imsiSeq);
        String tac = getTopologyData(tacMap, GuiStringConstants.TAC);
        String terminalMake = getTopologyData(tacMap, "MANUFACTURER");
        String terminalModel = getTopologyData(tacMap, "MARKETING_NAME");

        if(!eventType.equals(GuiStringConstants.RRC_CONN_SETUP)){
        csvRecordMap.put(GuiStringConstants.IMSI, imsi);
        csvRecordMap.put(GuiStringConstants.TAC, tac);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MAKE, terminalMake);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MODEL, terminalModel);
        }else{
            csvRecordMap.put(GuiStringConstants.IMSI, "");
            csvRecordMap.put(GuiStringConstants.TAC, "");
            csvRecordMap.put(GuiStringConstants.TERMINAL_MAKE, "");
            csvRecordMap.put(GuiStringConstants.TERMINAL_MODEL, "");
        }



        csvRecordMap.put(GuiStringConstants.EVENT_TYPE, eventType);


        csvRecordMap.put(GuiStringConstants.CONTROLLER, getTopologyData(cellTraceMap, "HIERARCHY_3"));

        csvRecordMap.put(GuiStringConstants.ACCESS_AREA, getTopologyData(cellTraceMap, "HIERARCHY_1"));
        csvRecordMap.put(GuiStringConstants.TRACKING_AREA, getTopologyData(cellTraceMap, "TRAC"));
        csvRecordMap.put(GuiStringConstants.RAN_VENDOR, getTopologyData(cellTraceMap, "VENDOR"));
        csvRecordMap.put("NE_VERSION", neVersion);

        Map<String, String> resvDataAttributes = resvDataMap.get(eventType) ;

        if (neVersion.equals("12A") || neVersion.equals("12B") || neVersion.equals("13A")||neVersion.equals("13B"))
        {

            csvRecordMap.put(GuiStringConstants.MCC, getTopologyData(cellTraceMap, GuiStringConstants.MCC));
            int mncInt = Integer.parseInt(getTopologyData(cellTraceMap, GuiStringConstants.MNC));

            if (mncInt < 10)
            {
                csvRecordMap.put(GuiStringConstants.MNC, "0" + Integer.toString(mncInt));
            }
            else
            {
                csvRecordMap.put(GuiStringConstants.MNC, Integer.toString(mncInt));
            }
            csvRecordMap.put(GuiStringConstants.INTERNAL_RELEASE_CAUSE, resvDataAttributes.get(GuiStringConstants.INTERNAL_RELEASE_CAUSE));

            if ((neVersion.equals("12B") || (neVersion.equals("12A") || neVersion.equals("13A") || neVersion.equals("13B"))&&(eventType.equals(GuiStringConstants.RRC_CONN_SETUP)||eventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP))))
            {
                csvRecordMap.put(GuiStringConstants.RRC_ESTABL_CAUSE, resvDataAttributes.get(GuiStringConstants.RRC_ESTABL_CAUSE));
            }
            else
            {
                csvRecordMap.put(GuiStringConstants.RRC_ESTABL_CAUSE, "");
            }
        }

        if (neVersion.equals("13B")&&eventType.equals(GuiStringConstants.RRC_CONN_SETUP)){  //Adding new Column Gummei Type

            csvRecordMap.put(GuiStringConstants.GUMMEI_Type_Desc, resvDataAttributes.get(GuiStringConstants.GUMMEI_Type_Desc));
        }

if (neVersion.equals("13B")&&eventType.equals(GuiStringConstants.UE_CTXT_RELEASE)){  //Adding new Column TTI Bundling Mode

            csvRecordMap.put(GuiStringConstants.TTI_Bundling_Mode_Desc, resvDataAttributes.get(GuiStringConstants.TTI_Bundling_Mode_Desc));
        }

        else if (neVersion.equals("11B"))
        {
            csvRecordMap.put(GuiStringConstants.MCC, "");
            csvRecordMap.put(GuiStringConstants.MNC, "");
            csvRecordMap.put(GuiStringConstants.INTERNAL_RELEASE_CAUSE, "");
            csvRecordMap.put(GuiStringConstants.RRC_ESTABL_CAUSE, "");
        }
        else if (neVersion.equals("12B")||neVersion.equals("13A")||neVersion.equals("13B"))
        {
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP, resvDataAttributes.get(GuiStringConstants.CAUSE_3GPP));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP, resvDataAttributes.get(GuiStringConstants.CAUSE_GROUP_3GPP));
        }

        csvRecordMap.put(GuiStringConstants.DURATION, resvDataAttributes.get(GuiStringConstants.DURATION));

        if (causeCode.isEmpty())
        {
            csvRecordMap.put(GuiStringConstants.CAUSE_CODE, resvDataAttributes.get(GuiStringConstants.CAUSE_CODE));
        }
        else
        {
            csvRecordMap.put(GuiStringConstants.CAUSE_CODE, causeCode);
        }
        csvRecordMap.put(GuiStringConstants.TRIGGERING_NODE, resvDataAttributes.get(GuiStringConstants.TRIGGERING_NODE));
        csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS, getNumberOfERABsFromResvData(eventType, neVersion));

        if (neVersion.equals("12B")||neVersion.equals("13A")||neVersion.equals("13B"))
        {
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, "");
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST_BITMAP, "");
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST, "");
            csvRecordMap.put(GuiStringConstants.ERAB_RELEASE_SUCCESS, "");
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_FAILED_ERABS, "");
        }
        else
        {
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, resvDataAttributes.get(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST_BITMAP, resvDataAttributes.get(GuiStringConstants.ERAB_DATA_LOST_BITMAP));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST, resvDataAttributes.get(GuiStringConstants.ERAB_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ERAB_RELEASE_SUCCESS, resvDataAttributes.get(GuiStringConstants.ERAB_RELEASE_SUCCESS));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_FAILED_ERABS, resvDataAttributes.get(GuiStringConstants.NUMBER_OF_FAILED_ERABS));
        }


        csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, resvDataAttributes.get(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR));
        csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, resvDataAttributes.get(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR));
        csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, resvDataAttributes.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR));
        csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, resvDataAttributes.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR));

        printMapData(csvRecordMap, "RESERVA DATA IN MAP");

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

                csvRecordMap.put("Setup Result", products.get("Setup Result"));
                csvRecordMap.put("Setup Request QCI", products.get("Setup Request QCI"));
                csvRecordMap.put("Setup Request ARP", products.get("Setup Request ARP"));
                csvRecordMap.put("Setup Request PCI", products.get("Setup Request PCI"));
                csvRecordMap.put("Setup Request PVI", products.get("Setup Request PVI"));
                csvRecordMap.put("Failure 3GPP Cause", products.get("Failure 3GPP Cause"));
                csvRecordMap.put("Release Request QCI", products.get("Release Request QCI"));
                csvRecordMap.put("Setup Attempted Access Type", products.get("Setup Attempted Access Type"));
                csvRecordMap.put("Setup Successful Access Type", products.get("Setup Successful Access Type"));
                csvRecordMap.put("Setup Failure 3GPP Cause Group", products.get("Setup Failure 3GPP Cause Group"));

                printMapData(csvRecordMap, "ERAB RESERVA DATA MAP");

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

    private void checkEventTypesConfiguredInResvData(String inputType, String inputValue) throws NoDataException, PopUpException {

        for (final Map<String, String> resvDataMap : reserveDataList)
        {


            if (inputValue.equals(resvDataMap.get(inputType)))
            {
                String eventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

                if (!listOfEventTypesInResvData.contains(eventType))
                {
                    listOfEventTypesInResvData.add(eventType);
                }
            }
        }
    }

    private int checkNoOfFailuresForEventType(String inputType, String inputValue, String eventType) {

        int noOfFailures = 0;

        for (final Map<String, String> resvDataMap : reserveDataList)
        {
            if (inputValue.equals(resvDataMap.get(inputType)))
            {
                String resvDataEventType = resvDataMap.get(GuiStringConstants.EVENT_TYPE);

                if (resvDataEventType.equals(eventType))
                {
                    noOfFailures++;
                }
            }
        }

        return noOfFailures;
    }

    private Map<String, Integer> checkNoOfFailuresForEventTypeForNetwork(String inputType, List<String> GroupList, String eventType) {

        int noOfFailures = 0;
        List<String> listOfImpactedSubscribers = new ArrayList<String>();
        Map<String, Integer> failuresMap = new HashMap<String, Integer>();

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
                        if (!listOfImpactedSubscribers.contains(resvDataMap.get(GuiStringConstants.IMSI)) && (!eventType.equals(GuiStringConstants.RRC_CONN_SETUP)))
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

        // Get each row from the resvData
        for (final Map<String, String> resvDataMap : reserveDataList)
        {

            if (inputValue.equals(resvDataMap.get(inputType)) &&
                    eventType.equals(resvDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                if (eventType.equals(GuiStringConstants.RRC_CONN_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_RRC_CONN_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.S1_SIG_CONN_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_S1_SIG_CONN_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.ERAB_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_ERAB_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.INITIAL_CTXT_SETUP))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_INITIAL_CTXT_SETUP;
                }
                else if (eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
                {
                    listOfHeaders = headersOnIMSIFailedEventAnalysisWindow_UE_CTXT_RELEASE;
                }
                else
                {
                    // Throw error here - INVALID EVENT TYPE
                    logger.log(Level.INFO, "UNKNOWN EVENT TYPE");
                }

                // Create a new map containing specific to that eventType
                Map<String, String> resvDataForEventTypeMap = new HashMap<String, String>();

                for (final String header : listOfHeaders)
                {
                    String resvDataValue = resvDataMap.get(header);

                    if (header.equals(GuiStringConstants.NUMBER_OF_ERABS))
                    {
                        resvDataValue = getNumberOfERABsFromResvData(eventType, resvDataMap.get("NE_VERSION"));
                    }

                    resvDataForEventTypeMap.put(header, resvDataValue);
                }

                resvDataForEventTypeMap.remove(GuiStringConstants.TERMINAL_MAKE);

                // Added rows of all these eventTypes into a new list
                resvDataList.add(resvDataForEventTypeMap);
            }
        }

        return resvDataList;
    }

    public String getNumberOfERABsFromResvData(String eventType, String neVersion)
    {
        int numberOfERABs = 0;

        if (((!neVersion.equals("12B"))||(!neVersion.equals("13A"))||(!neVersion.equals("13B"))) && eventType.equals(GuiStringConstants.UE_CTXT_RELEASE))
        {
            if (!(neVersion.equals("13A")||neVersion.equals("13B"))){

                return "0";
            }

        }


        for (Map<String, String> erabDataMap : reserveERABdataList)
        {
            if (eventType.equals(erabDataMap.get(GuiStringConstants.EVENT_TYPE)))
            {
                numberOfERABs++;
            }
        }

        return Integer.toString(numberOfERABs);
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

    private List<Map<String, String>> getResvDataRankingBasedOnFailures(Map<String, Integer> eventTypeFailuresMap, int timeInMinutes, String rankingType)
    {

        List<Map<String, String>> listofRank = new ArrayList<Map<String, String>>();

        int rank = 0;
        int tempCount = 0;
        int ranking = 0;

        // Sort MAP value(i.e failures) in descending order, such that Ranking can be done from highest failure count

        //for (String key : sorted_map.keySet())
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

    private void printMapData(Map<String, String> mapToPrint, String trace)
    {
        String printString = "\n########################### " + trace + "  ##########################\n" + "Size of Map : " + mapToPrint.size()+"\n";

        for (Map.Entry<String, String> me : mapToPrint.entrySet())
        {
            printString =  printString + me.getKey() + ": "+ me.getValue() + "\n";
        }

        logger.log(Level.SEVERE, printString);
    }

    class ValueComparator implements Comparator {

          Map baseMap;
          public ValueComparator(Map base) {
              this.baseMap = base;
          }

          public int compare(Object a, Object b) {

            if((Integer)baseMap.get(a) < (Integer)baseMap.get(b)) {
              return 1;
            } else if((Integer)baseMap.get(a) == (Integer)baseMap.get(b)) {
              return 0;
            } else {
              return -1;
            }
          }
    }

    /*********************** EVENT VOLUME  *********************/

    private void loadEventVolumeResvData(String neVersion, ReservedDataHelper reservedDataHelper, Date startDate) throws IOException {
        // CTUM iterator
        Iterator ctumIter = topologyInformation.iterator();

        // Cell Trace iterator
        List<Map<String, String>> RANTopologyData = new ArrayList<Map<String, String>>(topologyInformation);
        Collections.copy(RANTopologyData,topologyInformation);
        Iterator cellTraceIter = RANTopologyData.iterator(); // for CELL TRACE data

        // TAC Iterator
        Iterator tacIter = tacInformation.iterator();

        Map<String, String> cellTraceMap = new HashMap<String, String>();
        Map<String, String> ctumMap = new HashMap<String, String>();
        Map<String, String> tacMap = new HashMap<String, String>();

        int minuteValue = 0;
        final int noOfNodes = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_NODES));
        maxSubscribers = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NUMBER_OF_SUBSCRIBERS));
        final int noOfInternalProcRrcConnSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_RRC_CONN_SETUP_EVENTS));
        final int noOfInternalProcS1SigConnSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_S1_SIG_CONN_SETUP_EVENTS));
        final int noOfInternalProcInitialCtxtSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_INITIAL_CTXT_SETUP_EVENTS));
        final int noOfInternalProcErabSetupEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_ERAB_SETUP_EVENTS));
        final int noOfInternalProcUeCtxtReleaseEvents = Integer.parseInt(reservedDataHelper.getCommonReservedData(CommonDataType.NO_OF_UE_CTXT_RELEASE_EVENTS));

        final String causeCodeRrcConnSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_RRC_CONN_SETUP);
        final String causeCodeS1SigConnSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_S1_SIG_CONN_SETUP);
        final String causeCodeInitialCtxtSetup = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_INITIAL_CTXT_SETUP);

        String causeCodeUeCtxtRelease = "";
        if (neVersion.equals("12A") || neVersion.equals("12B")||neVersion.equals("13A")||neVersion.equals("13B"))
        {
            causeCodeUeCtxtRelease = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_UE_CTXT_RELEASE_12A);
        }
        else
        {
            causeCodeUeCtxtRelease = reservedDataHelper.getCommonReservedData(CommonDataType.CAUSE_CODE_UE_CTXT_RELEASE_11B);
        }

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
            csvRecordMap.put(GuiStringConstants.CAUSE_CODE, products.get(GuiStringConstants.CAUSE_CODE));
            csvRecordMap.put(GuiStringConstants.INTERNAL_RELEASE_CAUSE, products.get(GuiStringConstants.INTERNAL_RELEASE_CAUSE));
            csvRecordMap.put(GuiStringConstants.DURATION, products.get(GuiStringConstants.DURATION));
            csvRecordMap.put(GuiStringConstants.RRC_ESTABL_CAUSE, products.get(GuiStringConstants.RRC_ESTABL_CAUSE));
            csvRecordMap.put(GuiStringConstants.TRIGGERING_NODE, products.get(GuiStringConstants.TRIGGERING_NODE));
            if (neVersion.equals("13B")){
            csvRecordMap.put(GuiStringConstants.GUMMEI_Type_Desc, products.get(GuiStringConstants.GUMMEI_Type_Desc));
            csvRecordMap.put(GuiStringConstants.TTI_Bundling_Mode_Desc, products.get(GuiStringConstants.TTI_Bundling_Mode_Desc));
            }
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS, getNumberOfERABsFromResvData(products.get(GuiStringConstants.EVENT_TYPE), neVersion));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST_BITMAP, products.get(GuiStringConstants.ERAB_DATA_LOST_BITMAP));
            csvRecordMap.put(GuiStringConstants.ERAB_DATA_LOST, products.get(GuiStringConstants.ERAB_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ERAB_RELEASE_SUCCESS, products.get(GuiStringConstants.ERAB_RELEASE_SUCCESS));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_FAILED_ERABS, products.get(GuiStringConstants.NUMBER_OF_FAILED_ERABS));
            csvRecordMap.put(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST, products.get(GuiStringConstants.NUMBER_OF_ERABS_WITH_DATA_LOST));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR, products.get(GuiStringConstants.ACCUMULATED_UP_LINK_REQ_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR, products.get(GuiStringConstants.ACCUMULATED_UP_LINK_ADMIT_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR, products.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_REQ_GBR));
            csvRecordMap.put(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR, products.get(GuiStringConstants.ACCUMULATED_DOWN_LINK_ADMIT_GBR));
            csvRecordMap.put(GuiStringConstants.CAUSE_3GPP , products.get(GuiStringConstants.CAUSE_3GPP ));
            csvRecordMap.put(GuiStringConstants.CAUSE_GROUP_3GPP , products.get(GuiStringConstants.CAUSE_GROUP_3GPP ));

            resvDataMap.put(products.get(GuiStringConstants.EVENT_TYPE),  csvRecordMap);
        }

        for (int node = 0; node < noOfNodes; node++)
        {
            cellTraceMap = (Map<String, String>)cellTraceIter.next();
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

                ctumMap = (Map<String, String>)ctumIter.next();
                tacMap = (Map<String, String>)tacIter.next();

                   if (noOfEvents < noOfInternalProcRrcConnSetupEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.RRC_CONN_SETUP, causeCodeRrcConnSetup, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents <  noOfInternalProcS1SigConnSetupEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.S1_SIG_CONN_SETUP, causeCodeS1SigConnSetup, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcInitialCtxtSetupEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.INITIAL_CTXT_SETUP, causeCodeInitialCtxtSetup, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcErabSetupEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.ERAB_SETUP, "", neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if (noOfEvents < noOfInternalProcUeCtxtReleaseEvents)
                   {
                       createEventVolumeEvent(ctumMap, tacMap, cellTraceMap, noOfSubscribers, GuiStringConstants.UE_CTXT_RELEASE, causeCodeUeCtxtRelease, neVersion, startDate, minuteValue, resvDataMap);
                   }

                   if ((noOfEvents >= noOfInternalProcRrcConnSetupEvents) &&
                       (noOfEvents >= noOfInternalProcS1SigConnSetupEvents) &&
                       (noOfEvents >= noOfInternalProcInitialCtxtSetupEvents) &&
                       (noOfEvents >= noOfInternalProcErabSetupEvents) &&
                       (noOfEvents >= noOfInternalProcUeCtxtReleaseEvents))
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

    private void createEventVolumeEvent(Map<String, String> ctumMap, Map<String, String> tacMap, Map<String, String> cellTraceMap, int imsiSeq, String eventType, String causeCode, String neVersion, Date startDate, int minuteValue, Map<String, Map<String, String>> resvDataMap)
    {
        Map<String, String> csvRecordMap = new HashMap<String, String>();

        long eventDateLong = startDate.getTime();
        Date eventDate = new Date();
        eventDate.setTime(eventDateLong);
        long timeInMins = eventDate.getTime() + (minuteValue*60*1000);
        eventDate.setTime(timeInMins);
           final DateFormat minFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String startDateStr = minFormatter.format(eventDate.getTime());

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
           csvRecordMap.put(GuiStringConstants.EVENT_TIME, startDateStr);

        String imsi = getImsi(ctumMap, imsiSeq);
        String tac = getTopologyData(tacMap, GuiStringConstants.TAC);
        String terminalMake = getTopologyData(tacMap, "MANUFACTURER");
        String terminalModel = getTopologyData(tacMap, "MARKETING_NAME");

        csvRecordMap.put(GuiStringConstants.IMSI, imsi);
        csvRecordMap.put(GuiStringConstants.TAC, tac);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MAKE, terminalMake);
        csvRecordMap.put(GuiStringConstants.TERMINAL_MODEL, terminalModel);

        csvRecordMap.put(GuiStringConstants.EVENT_TYPE, eventType);

        csvRecordMap.put(GuiStringConstants.CONTROLLER, getTopologyData(cellTraceMap, "HIERARCHY_3"));
        csvRecordMap.put(GuiStringConstants.ACCESS_AREA, getTopologyData(cellTraceMap, "HIERARCHY_1"));
        csvRecordMap.put(GuiStringConstants.TRACKING_AREA, getTopologyData(cellTraceMap, "TRAC"));
        csvRecordMap.put(GuiStringConstants.RAN_VENDOR, getTopologyData(cellTraceMap, "VENDOR"));
        csvRecordMap.put("NE_VERSION", neVersion);

        eventVolReserveDataList.add(csvRecordMap);
    }

}