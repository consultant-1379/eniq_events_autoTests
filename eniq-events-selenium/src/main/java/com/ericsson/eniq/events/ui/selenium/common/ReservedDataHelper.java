/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author eseuhon
 * @since 2011
 *
 */
@Component
public class ReservedDataHelper {

    static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    private static String fileName = "reservedData.csv";

    private static String DELIM = ";";

    private final Map<CommonDataType, String> commonReservedData = new HashMap<CommonDataType, String>();

    private final static Map<ImsiNumber, Map<ImsiSpecificDataType, String>> imsiSpecificReservedData = new HashMap<ImsiNumber, Map<ImsiSpecificDataType, String>>();

    public enum CommonDataType {
        PTMSI, SGSN, CONTROLLER, CONTROLLER_2G, CONTROLLER_3G, ACCESS_AREA, ACCESS_AREA_2G, ACCESS_AREA_3G, ACCESS_AREA_GROUP, IMSI_GROUP, APN_GROUP, 
        TERMINAL_GROUP, SGSN_GROUP, CONTROLLER_GROUP, TAC_GROUP, MSC_MSS, CONTROLLER_MSS, ACCESS_AREA_MSS, MSC_GROUP_MSS, CONTROLLER_GROUP_MSS, 
        ACCESS_AREA_GROUP_MSS, TERMINAL_GROUP_MSS, TIME_RANGE, TIME_RANGES, IMSI_GROUP_MSS, MSS_GROUP_DATA_MSS, CONTROLLER_GROUP_DATA_MSS, 
        ACCESS_AREA_GROUP_DATA_MSS, TERMINAL_GROUP_DATA_MSS, IMSI_GROUP_DATA_MSS, MSISDN_MSS, 
        IMSI_LTE, IMSI_GROUP_LTE, IMSI_GROUP_DATA_LTE,IMSI_LTE_ZERO_ERAB, CONTROLLER_LTE, CONTROLLER_GROUP_LTE, CONTROLLER_GROUP_DATA_LTE, ACCESS_AREA_LTE, 
        ACCESS_AREA_GROUP_LTE, ACCESS_AREA_GROUP_DATA_LTE, TRACKING_AREA_LTE, TRACKING_AREA_GROUP_LTE, TRACKING_AREA_GROUP_DATA_LTE, 
        NUMBER_OF_NODES, NUMBER_OF_SUBSCRIBERS, NO_OF_RRC_CONN_SETUP_EVENTS, NO_OF_S1_SIG_CONN_SETUP_EVENTS, NO_OF_ERAB_SETUP_EVENTS, 
        NO_OF_INITIAL_CTXT_SETUP_EVENTS, NO_OF_UE_CTXT_RELEASE_EVENTS, CAUSE_CODE_RRC_CONN_SETUP, CAUSE_CODE_S1_SIG_CONN_SETUP, 
        CAUSE_CODE_INITIAL_CTXT_SETUP, CAUSE_CODE_UE_CTXT_RELEASE, CAUSE_CODE_UE_CTXT_RELEASE_11B, CAUSE_CODE_UE_CTXT_RELEASE_12A, 
        VERSION_12A_SUPPORTED, VERSION_11B_SUPPORTED, VERSION_12B_SUPPORTED,VERSION_13A_SUPPORTED,VERSION_13B_SUPPORTED,NO_OF_PREP_S1_OUT_EVENTS, NO_OF_PREP_S1_IN_EVENTS, NO_OF_EXEC_S1_OUT_EVENTS,
        NO_OF_EXEC_S1_IN_EVENTS, NO_OF_PREP_X2_OUT_EVENTS, NO_OF_PREP_X2_IN_EVENTS, NO_OF_EXEC_X2_OUT_EVENTS, NO_OF_EXEC_X2_IN_EVENTS, 
        CAUSE_CODE_PREP_S1_OUT, CAUSE_CODE_PREP_X2_OUT, CAUSE_CODE_EXEC_S1_IN, CAUSE_CODE_EXEC_S1_OUT, CAUSE_CODE_EXEC_X2_IN, TIME_RANGES_LTE,
        CAUSE_CODE_EXEC_X2_OUT, TERMINAL_MAKE_LTE, TERMINAL_MODEL_LTE,TAC_LTE, TERMINAL_GROUP_LTE, TERMINAL_GROUP_DATA_LTE, KPI_TIME_RANGES,
        GSM_CALL_DROP_FAILURE_FACTOR,GSM_CALL_SET_UP_FAILURE_FACTOR,TIME_RANGES_GSM, SB_IMSI_ATTACH, SB_IMSI_PDP_ACTIVATE, SB_IMSI_RAU, SB_IMSI_ISRAU,
        SB_IMSI_DEACTIVATE, SB_IMSI_DETACH, SB_IMSI_SERVICE_REQUEST, SB_IMSI_DATA_BEARER_SESSION, SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE,
        SB_IMSI_INT_CALL_SETUP_FAILURES, SB_IMSI_INT_SYSTEM_RELEASE, SB_IMSI_INT_OUT_HARD_HANDOVER, SB_IMSI_RRC_MEASUREMENT_REPORT,
        SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE, SB_IMSI_INT_IFHO_HANDOVER_EXECUTION_FAILURE, SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE, 
        SB_IMSI_INT_HSDSCH_NO_CELL_SELECTED, SB_IMSI_FOR_CELL_VISITED_CHART, SB_IMSI_APPLICATION_SUM_CHART, SB_IMSI_TCP_PERFORMANCE_CHART,APN
    }

    public enum ImsiSpecificDataType {
        IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, APN
    }

    public enum ImsiNumber {
        IMSI_A, IMSI_B, IMSI_2G_MSS, IMSI_3G_MSS
    }

    public ReservedDataHelper() {
        setAllReservedData();
    }

    public void setAllReservedData() {
        final List<Map<String, String>> allReservedData = getAllReservedData();
        setUpImsiSpecificData(allReservedData);
        setUpCommonReservedData(allReservedData);
    }

    /**
     * @param allReservedData
     */
    private void setUpCommonReservedData(final List<Map<String, String>> allReservedData) {
        for (final Map<String, String> currentData : allReservedData) {
            if (!currentData.containsKey(GuiStringConstants.IMSI)) {
                final Map<CommonDataType, String> commonData = new HashMap<CommonDataType, String>();
                for (final String key : currentData.keySet()) {
                    commonData.put(CommonDataType.valueOf(key), currentData.get(key));
                }
                commonReservedData.putAll(commonData);
                logger.info("Reserved data commonly used :" + commonData);
                              
            }
        }
    }

    /**
     * @param allReservedData
     */
    private void setUpImsiSpecificData(final List<Map<String, String>> allReservedData) {
        int index = 0;
        for (final Map<String, String> currentData : allReservedData) {
            if (currentData.containsKey(GuiStringConstants.IMSI)) {
                final Map<ImsiSpecificDataType, String> imsiSpecificData = new HashMap<ImsiSpecificDataType, String>();
                for (final String key : currentData.keySet()) {
                    imsiSpecificData.put(ImsiSpecificDataType.valueOf(key), currentData.get(key));
                }
                imsiSpecificReservedData.put(ImsiNumber.values()[index++], new HashMap<ImsiSpecificDataType, String>(
                        imsiSpecificData));
                logger.info("Reserved data for a imsi :" + imsiSpecificData);
                imsiSpecificData.clear();
            }
        }
    }

    /**
     * Parsing all reserved data in a text file(reservedData.csv) and return those all values as a List
     * 
     * i.e. reservedData.csv 
     *  IMSI_A=460000000123456,TAC=33008971,Terminal Make=Alcatel Radiotelephone,Terminal Model=Alcatel One Touch Easy,
     *  IMSI_B=460000000654321,TAC=33104322,Terminal Make=Sagem (75783 Paris),Terminal Model=DC 810,
     *  
     * @return List<Map<String, String>> result  
     */
    private List<Map<String, String>> getAllReservedData() {
        final Map<String, String> typeToValuesAtOneLine = new HashMap<String, String>();
        final List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        BufferedReader reader = null;
        String line;
        StringTokenizer token;
        TypeValue typeValue;
        try {
            reader = new BufferedReader(new FileReader(new File(PropertyReader.getInstance().getResourcesLocation()
                    + File.separator + fileName)));

            while ((line = reader.readLine()) != null) {
                if (line.length() == 0 || line.startsWith("#")) // line is empty or commented out
                {
                    continue;
                }

                token = new StringTokenizer(line, DELIM);
                while (token.hasMoreTokens()) {
                    typeValue = new TypeValue(token.nextToken());
                    typeToValuesAtOneLine.put(typeValue.getType(), typeValue.getValue());
                }
                result.add(new HashMap<String, String>(typeToValuesAtOneLine));
                typeToValuesAtOneLine.clear();
            }
        } catch (final FileNotFoundException e) {
            logger.severe("can not find a file:" + PropertyReader.getInstance().getResourcesLocation() + File.separator
                    + fileName + " Please check if the file is existing or not.");
        } catch (final IOException e) {
            logger.warning("errors in file I/O operation with reservedData.csv file.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    logger.warning("an error occured when closing a file I/O stream - reservedData.csv");
                }
            }
        }
        return result;
    }

    public String getCommonReservedData(final CommonDataType type) {
        return commonReservedData.get(type);
    }

    public String getImsiSpecificData(final ImsiNumber imsi, final ImsiSpecificDataType type) {
        final Map<ImsiSpecificDataType, String> allDataForImsi = getAllReservedDataForIMSI(imsi);
        return allDataForImsi.get(type);
    }

    public Map<ImsiSpecificDataType, String> getAllReservedDataForIMSI(final ImsiNumber imsi) {
        return imsiSpecificReservedData.get(imsi);
    }

    public ImsiNumber getImsiNumber(final String number) {
        for (final ImsiNumber key : imsiSpecificReservedData.keySet()) {
            final Map<ImsiSpecificDataType, String> currentData = imsiSpecificReservedData.get(key);
            if (currentData.get(ImsiSpecificDataType.IMSI).equals(number)) {
                return key;
            }
        }
        //Default value
        return ImsiNumber.IMSI_A;
    }

    private class TypeValue {
        private static final String TYPE_DELIM = "=";

        private TypeValue(final String unParsed) {
            //We are expecting string as "type=value" format i.e. EVENT=ATTACH
            final StringTokenizer typeTok = new StringTokenizer(unParsed, TYPE_DELIM);
            type = typeTok.nextToken();
            value = typeTok.nextToken();
        }

        private String getType() {
            return type;
        }

        private String getValue() {
            return value;
        }

        final private String type;

        final private String value;
    }
}
