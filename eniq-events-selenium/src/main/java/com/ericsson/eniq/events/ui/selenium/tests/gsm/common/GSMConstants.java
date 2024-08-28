/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.gsm.common;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;

import java.util.*;

/**
 * @author ezolmag
 * @since 2011
 * 
 */
public final class GSMConstants {
    public static final int DEFAULT_WAIT_TIME = 2000;

    public static final String DEFAULT_WAIT_TIME_STR = String.valueOf(DEFAULT_WAIT_TIME);

    // public static final String GSM_CFA_RESERVED_DATA_FILENAME =
    // "/eniq/home/dcuser/" +
    // "selenium-grid-1.0.8/test-cases/resources/gsmCFAReservedData.csv";
    // public static final String GSM_CFA_RESERVED_DATA_FILENAME =
    // "C:\\Users\\ezolmag\\gsmCFAReservedData.csv";
    public static final String GSM_CFA_CD_RESERVED_DATA_FILENAME = "E:\\eprabab\\eprabab_view\\eniq_events\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources\\GSM_CFA_ReservedData.csv";

    public static final String GSM_CFA_PS_RESERVED_DATA_FILENAME = "E:\\eprabab\\eprabab_view\\eniq_events\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources\\GSM_CFA_ReservedData.csv";

    public static final int RESERVE_DATA_RUN_INTERVAL = 5;

    
    public static final TimeRange DEFAULT_TIME_RANGE = TimeRange.THIRTY_MINUTES;

    public static final int DEFAULT_MAX_TABLE_ROWS = 50;

    public static final String DATEFORMAT_STRING = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATEFORMAT_STRING_WITHOUT_MSEC = "yyyy-MM-dd HH:mm:ss";

    public static final int LATENCY_FOR_LESS_THAN_6_HOURS = 7;

    // *** Errors ***

    public static final String DEFAULT_TIMERANGE_MISMATCH = "Default time range mismatch.";

    public static final String MORE_ROWS_THAN = "The number of rows is more than ";

    // *** Window titles Events ***

    public static final String BSC_RANKING_WINDOW_TITLE = "Controller - GSM Call Failure Analysis";

    public static final String ACCESS_AREA_RANKING_WINDOW_TITLE = "Access Area - GSM Call Failure Analysis";

    public static final String CAUSE_CODE_RANKING_WINDOW_TITLE = "Cause Code - Call Drops - GSM Call Failure Analysis";

    public static final String TERMINAL_RANKING_WINDOW_TITLE = "Terminal Call Failure Analysis Ranking";

    public static final String SUBSCRIBER_RANKING_WINDOW_TITLE = "Subscriber Ranking (Events) By Call Drops";

    public static final String SUBSCRIBER_DETAILED_EVENT_ANALYSIS_WINDOW_TITLE = "IMSI Event Analysis";

    public static final String DETAILS_EXTENDED_CAUSE_BSC_GSMCFR_170_WINDOW_TITLE = "BSCA01 - Call Drops - AIR INTERFACE - AIR INTERFACE, T200 EXPIRED - Controller GSM Call Failure : Event Analysis Detail";

    public static final String SUMMARY_CELL_GSMCFR_171_WINDOW_TITLE = "BSCA01 - Call Drops - Access Area GSM Call Failure : Event Analysis Summary";

    public static final String DETAILS_IMSI_GROUP_EVENT_ANALYSIS_WINDOW_TITLE = "Regression_IMSI_group - 460000021041788 - Call Drops - Subscriber GSM Call Failure : Event Analysis Detail";

    // *** Window titles Data ***
    public static final String SUBSCRIBER_DATA_RANKING_WINDOW_TITLE = "GSM Data Connection : Total Data Volume Ranking";

    public static final String BSC_DATA_RANKING_WINDOW_TITLE = "BSC GSM Data Connection : Total Data Volume Ranking";

    public static final String ACCESS_AREA_DATA_RANKING_WINDOW_TITLE = "Subscriber GSM Data Connection : Total Data Volume Ranking";
    
  //Window Title Roaming Analysis 
  	public static final String ROAMING_ANALYSIS_OPERATOR_WINDOW_TITLE= "GSM Call Failure Event Volume Inbound Roaming by Operator";
  	public static final String ROAMING_ANALYSIS_COUNTRY_WINDOW_TITLE= "GSM Call Failure Event Volume Inbound Roaming by Country";
    // Constants for groups,
    public static final String[] CONTROLLER_GROUP_MEMBER_NAMES = { "BSCB01", "BSCB02" };

    public static final String[] ACCESS_AREA_GROUP_MEMBER_NAMES = { "CB01002", "CB02002"};

    public static final String[] IMSI_GROUP_MEMBER_NAMES = { "460000109041946", "460000002487903"};

    public static final int COLUMN_NUMBER_FOR_CONTROLLER = 10;
    
    public static final int MAX_NUMBER_ROMAING_COUNTRY = 10;
    
    public static final int MAX_NUMBER_ROMAING_OPERATOR = 10;
    
    public static final String ROAMING_COUNTRY = "Australia";
    
    public static final String ROAMING_OPERATOR = "Telstra";
    
    public static final int MAX_NUMBER_CAUSE_GROUP = 7;
    
    public static final int MAX_NUMBER_EXTENDED_CAUSE = 15;
    
    public static final String CAUSE_GROUP_AUTOMATION = "AIR INTERFACE";
    
    public static final String EXTENDEDCAUSE_GROUP_AUTOMATION = "AIR INTERFACE, T200 EXPIRED";

    public static final int COLUMN_NUMBER_FOR_ACCESS_AREAS = 9;

    public static final String ACCESS_AREA_NAME = "CA00000,,BSCA00,Ericsson,GSM";

    public static final String ACCESS_AREA_NAME_SHORT_FORMAT_ALTERNATIVE = "CA01016";

    public static final String IMSI_NUMBER = "460000021041788";

    public static final String CALLSETUP_IMSI_NUMBER = "460000109041946";
    
    public static final String REGRESSION_IMSI_GROUP_NAME = "Regression_IMSI_group"; // Use these IMSI { "460000109041946", "460000002487903"};

    public static final String CONTROLLER_NAME_LONG_FORMAT_ALTERNATIVE = "BSCA01,Ericsson,GSM";
    
    public static final String CCA_CONTROLLER_GROUP = "automation_regression";
    
    public static final String CCA_ACCESSAREA_GROUP = "Regression_access_area_group";

    // GSM DV

    public final static String DOWNLINK_DATA_VOLUME_KB = "Downlink Data Vol (KB)";

    public final static String UPLINK_DATA_VOLUME_KB = "Uplink Data Vol (KB)";

    public final static String TOTAL_DATA_VOLUME_KB = "Total Data Vol (KB)";

    public final static String DOWNLINK_DATA_VOLUME_MB = "Downlink Data Volume(MB)";

    public final static String UPLINK_DATA_VOLUME_MB = "Uplink Data Volume(MB)";

    public final static String TOTAL_DATA_VOLUME_MB = "Total Data Volume(MB)";

    public final static int CONVERSION_FACTOR_TO_KB = 1024;

    public final static int CONVERSION_FACTOR_TO_MB = 1024 * 1024;

    public final static String NO_OF_EVENTS = "Number of Events";

    public final static String RELEASE_TYPE = "Release Type";

    public final static String RELEASE_TYPE_ID = "Release Type ID";

    public final static String URGENCY_CONDITION = "Urgency Condition";

    public final static String URGENCY_CONDITION_ID = "Urgency Condition ID";

    public final static String EXTENDED_CAUSE = "Extended Cause";

    public final static String EXTENDED_CAUSE_ID = "Extended Cause ID";

    public final static String VAMOS_NEIGHBOR_INDICATOR_ID = "Vamos Neighbor Indicator ID";

    public final static String RSAI = "RSAI";

    public final static String RSAI_ID = "RSAI ID";

    public final static String EVENT_NAME = "Event Name";

    public final static String CS_CALL_RELEASE = "CS Call Release";

    // Column Names from reserved data

    public final static String TAC = "TAC";

    public final static String IMSI = "IMSI";

    public final static String TERMINAL_MAKE = "Terminal Make";

    public final static String TERMINAL_MODEL = "Terminal Model";

    public final static String RELEASE_TYPE_DESC = "Release Type Desc";

    public final static String CAUSE_GROUP = "Cause Group";

    public final static String EXTENDED_CAUSE_VALUE = "Extended Cause Value";

    public final static String EVENT_TYPE = "Event Type";

    public final static String VAMOS_NEIGHBOR_INDICATOR = "Vamos Neighbor Indicator";

    public final static String RAN_VENDOR = "RAN Vendor";

    public final static String CONTROLLER = "Controller";

    public final static String CHANNEL_TYPE = "Channel Type";

    public final static String CHANNEL_TYPE_ID = "Channel Type ID";

    public final static String NO_BSC = "NoBSC";

    public final static String MANUFACTURER = "Manufacturer";

    public final static String MODEL = "Model";

 // public static final String IMSI_NUMBER_CAUSE_GROUP_ANALYSIS_old = "902002385747230";
    
    public static final String IMSI_NUMBER_CAUSE_GROUP_ANALYSIS = "460000025651469";

    public static final String CAUSE_GROUP_FOR_EXTENDED_CAUSE = "OTHER";

    public static final String MSISDN_FOR_CALL_FAILURE_ANALYIS = "460000025651469";

    // *** Cause Codes mapping ***
    // Cause Codes and Urgency Condition are the same

    public static final Map<String, Integer> CAUSE_CODES;

    static {
        final Map<String, Integer> tempMap = new HashMap<String, Integer>();
        tempMap.put("EXCESSIVE TA", 1);
        tempMap.put("SUDDENLY LOST CONNECTION", 2);
        tempMap.put("LOW SS BOTHLINK", 3);
        tempMap.put("LOW SS DOWNLINK", 4);
        tempMap.put("LOW SS UPLINK", 5);
        tempMap.put("BAD RXQUAL BOTHLINK", 6);
        tempMap.put("BAD RXQUAL DOWNLINK", 7);
        tempMap.put("BAD RXQUAL UPLINK", 8);
        tempMap.put("HIGH FER BOTHLINK", 9);
        tempMap.put("HIGH FER DOWNLINK", 10);
        tempMap.put("HIGH FER UPLINK", 11);
        tempMap.put("NO URGENCY CONDITION", 12);
        tempMap.put("NOT CHECKED FOR URGENCY CONDITION", 13);

        CAUSE_CODES = Collections.unmodifiableMap(tempMap);
    }

    // *** Window Headers ***
    public static final List<String> GSM_CFA_PS_RESERVED_DATA_COLUMNS = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.TBF_RELEASE_CAUSE, GuiStringConstants.TBF_DATA_VOLUME,
            GuiStringConstants.TBF_DURATION, GuiStringConstants.RP_NUMBER,
            GuiStringConstants.CHANNEL_RELATED_RELEASE_CAUSE, GuiStringConstants.CHANNEL_RELATED_RELEASE_CAUSE_GROUP,
            GuiStringConstants.TBF_MUX, GuiStringConstants.EFACTOR_SETTINGS, GuiStringConstants.DATA_VALID_INDICATOR,
            GuiStringConstants.MS_SAIC_CAP, GuiStringConstants.AQM_ACTIVE, GuiStringConstants.DTM_FLAG,
            GuiStringConstants.RLC_MODE, GuiStringConstants.DIR, GuiStringConstants.OFLW,
            GuiStringConstants.MS_3GPP_CAP, GuiStringConstants.TTI_MODE, GuiStringConstants.REDUCED_LATENCY,
            GuiStringConstants.RADIO_LINK_BITRATE, GuiStringConstants.GPRS_MEAS_REPORT_RXQUAL_DL,
            GuiStringConstants.GPRS_MEAS_REPORT_SIGN_VAR, GuiStringConstants.GPRS_MEAS_REPORT_MEAN_BEP,
            GuiStringConstants.GPRS_MEAS_REPORT_CV_BEP, GuiStringConstants.GPRS_MEAS_REPORT_CVALUE,
            GuiStringConstants.IP_LATENCY, GuiStringConstants.LOW_PRIORITY_MODE_TIME, GuiStringConstants.BLER,
            GuiStringConstants.MS_FREQ_BAND_CAP_GSM_850, GuiStringConstants.MS_FREQ_BAND_CAP_GSM_900,
            GuiStringConstants.MS_FREQ_BAND_CAP_GSM_1800, GuiStringConstants.MS_FREQ_BAND_CAP_GSM_1900,
            GuiStringConstants.AQM_DATA_DELIVERED, GuiStringConstants.AQM_DATA_RECIEVED,
            GuiStringConstants.PAN_INDICATOR, GuiStringConstants.TBF_MODE, GuiStringConstants.DCDL_CAPABILITY,
            GuiStringConstants.DCDL_INDICATOR, GuiStringConstants.MS_MSLOT_CAP_REDUCTION,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.VENDOR,
            GuiStringConstants.IMSI, GuiStringConstants.DOWNLINK_DATA_VOL, GuiStringConstants.UPLINK_DATA_VOL,
            GuiStringConstants.TOTAL_DATA_VOL, GSMConstants.NO_OF_EVENTS);

    /*
     * public static final List<String> GSM_CFA_CD_RESERVED_DATA_COLUMNS =
     * Arrays.asList( GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC,
     * GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
     * GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
     * GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE,
     * GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
     * GuiStringConstants.IMSI);
     */

    public static final List<String> GSM_CFA_CD_RESERVED_DATA_COLUMNS = Arrays.asList(GuiStringConstants.EVENT_NAME,
            GuiStringConstants.EVENT_ID, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.IMSI, GuiStringConstants.RSAI, GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.URGENCY_CONDITION,
            GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.NO_OF_EVENTS,
            GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RAN_VENDOR, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.TERMINAL_MODEL,
            RELEASE_TYPE_ID, GuiStringConstants.CAUSE_GROUP, EXTENDED_CAUSE_ID, VAMOS_NEIGHBOR_INDICATOR_ID, RSAI_ID,
            CHANNEL_TYPE_ID, URGENCY_CONDITION_ID, GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, "Scenario", GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO, GuiStringConstants.EXTENDED_CAUSE_ID_GSM, GuiStringConstants.FAILURES, GuiStringConstants.CAUSE_GROUP_ID,
            GuiStringConstants.IMPACTED_CELLS, GuiStringConstants.COUNTRY, GuiStringConstants.OPERATOR );

    public static final List<String> DEFAULT_SUMMARY_EVENT_ANALYSIS_GSM_CFA_PS_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> DEFAULT_SUMMARY_SUBSCRIBER_RANKING_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.RANK, GuiStringConstants.IMSI, GuiStringConstants.FAILURES);

    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
                    GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
                    GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);

    //sample
    /*	public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS = Arrays.asList(
    			GuiStringConstants.EVENT_TIME, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RAN_VENDOR,
    			GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.IMSI, GuiStringConstants.TAC, 
    			GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE, 
    			GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
    			GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, 
    			GuiStringConstants.RSAI			  
    			); */

    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE, GuiStringConstants.CAUSE_GROUP,
            GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,
            GuiStringConstants.RSAI, GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.URGENCY_CONDITION,
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA);
    
    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_NEWUI = Arrays.asList(
            GuiStringConstants.EVENT_TIME,
            GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.URGENCY_CONDITION,
            GuiStringConstants.CAUSE_GROUP,
            GuiStringConstants.EXTENDED_CAUSE_VALUE,
            GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE,
            GuiStringConstants.CHANNEL_TYPE,
            GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,
            GuiStringConstants.RSAI);
    
    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_NEWUI2 = Arrays.asList(
            GuiStringConstants.EVENT_TIME,
            GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA,
            GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.URGENCY_CONDITION,
            GuiStringConstants.CAUSE_GROUP,
            GuiStringConstants.EXTENDED_CAUSE_VALUE,
            GuiStringConstants.CHANNEL_TYPE,
            GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,
            GuiStringConstants.RSAI);


    public static final List<String> DEFAULT_CONTROLLER_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS,
            GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_ACCESS_AREA_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CONTROLLER, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
    
    

    public static final List<String> DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO);

    public static final List<String> MSISDN_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO);

    public static final List<String> SUBSCRIBER_ANALYSIS_SUMMARY_CAUSE_GROUP_CELL_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TYPE, GuiStringConstants.CONTROLLER, GuiStringConstants.RAN_VENDOR,
                    GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
                    GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_SUBSCRIBER_ANALYSIS_CAUSEGROUP_SUMMARY_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.CAUSE_GROUP_ID, GuiStringConstants.CAUSE_GROUP, GuiStringConstants.FAILURES,
                    GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> SUBSCRIBER_ANALYSIS_EXTENDED_CAUSE_ID_SUMMARY_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    //GSM CFR 164
    public static final List<String> DEFAULT_SUBSCRIBER_CAUSEGROUPBYTERMINAL_EVENTANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE, GuiStringConstants.CAUSE_VALUE,
                    GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.CONTROLLER,
                    GuiStringConstants.ACCESS_AREA);

    // GSM CFR 564

    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CAUSEGROUP_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.CONTROLLER, GuiStringConstants.IMSI,
                    GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
                    GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);
    
    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CONTROLLER_CAUSEGROUP_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.CONTROLLER, GuiStringConstants.IMSI,
                    GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
                    GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);

    public static final List<String> DEFAULT_IMPACTED_CELL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);

    // TODO the OCCURENCES constant referred to here may need to be replaced by
    // failures depending on discussions with
    // Noreen....................................................See directly
    // below
    public static final List<String> DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY = Arrays.asList(
            GuiStringConstants.CAUSE_GROUP_ID, GuiStringConstants.CAUSE_GROUP, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY = Arrays.asList(
            GuiStringConstants.EXTENDED_CAUSE_ID_GSM, GuiStringConstants.EXTENDED_CAUSE_VALUE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> DEFAULT_ACCESS_AREA_EVENT_ANALYSIS_SUMMARY = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.CONTROLLER, GuiStringConstants.RAN_VENDOR,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> DEFAULT_SUBSCRIBER_GROUP_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_SUBSCRIBER_GROUP_DETAIL_ANALYSIS_FAILURE_RATIO_HEADERS = Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_SUBSCRIBER_ANALYSIS_SUMMARY_WITH_IMSI_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO,
                    GuiStringConstants.IMSI);

    public static final List<String> DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.FAILURE_RATIO, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public static final List<String> DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_DETAILS_WINDOW = Arrays
            .asList(GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.IMSI,
                    GuiStringConstants.EVENT_TIME, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_GSM,
                    GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,
                    GuiStringConstants.RSAI);

    //Roaming Analysis
    public static final List<String> DEFAULT_ROAMING_ANALYSIS_INBOUND_COUNTRY_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.COUNTRY, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.MCC);

    public static final List<String> DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_DETAILED_ROAMING_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.OPERATOR, GuiStringConstants.CONTROLLER,
                    GuiStringConstants.ACCESS_AREA, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
                    GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);

    public static final List<String> DEFAULT_DETAILED_ROAMING_ANALYSIS_GSM_CALLDROP_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.OPERATOR, GuiStringConstants.CONTROLLER,
            GuiStringConstants.ACCESS_AREA, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE,
            GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.CAUSE_GROUP,
            GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
            GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);

    public static final List<String> DEFAULT_ROAMING_ANALYSIS_INBOUND_OPERATOR_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.OPERATOR, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.COUNTRY, GuiStringConstants.MCC, GuiStringConstants.MNC);

    public static final List<String> DEFAULT_ROAMING_ANALYSIS_BY_OPERATOR_SUMMARY_WINDOW_HEADERS = Arrays.asList(
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
            GuiStringConstants.FAILURE_RATIO);

    public static final List<String> DEFAULT_DETAILED_ROAMING_ANALYSIS_BY_OPERATOR_GSM_CALLSETUP_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.COUNTRY, GuiStringConstants.CONTROLLER,
                    GuiStringConstants.ACCESS_AREA, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,
                    GuiStringConstants.RSAI);

    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CSF_GSM_CFA_WINDOW_HEADERS = Arrays
            .asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
                    GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
                    GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
                    GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
                    GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE,
                    GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI);
    
    public static final List<String> DEFAULT_DETAILED_SUBSCRIBER_EVENT_ANALYSIS_CSF_GSM_CFA_WINDOW_HEADERS_NEWUI = Arrays
            .asList(GuiStringConstants.IMSI,
            		GuiStringConstants.FAILURES,
            		GuiStringConstants.FAILURE_RATIO
            		);
    
    public static final List<String> DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_WINDOW_HEADERS = Arrays.asList(
			GuiStringConstants.EVENT_TIME, GuiStringConstants.EVENT_TYPE, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
			GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
			GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI			
			);
    
    public static final List<String> DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_ACCESSAREA_WINDOW_HEADERS = Arrays.asList(
			GuiStringConstants.EVENT_TIME, GuiStringConstants.EVENT_TYPE, GuiStringConstants.CONTROLLER,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
			GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE,
			GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE, GuiStringConstants.CHANNEL_TYPE, GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI			
			);
    
    public static final List<String> CONTROLLER_GROUP_SUMMARY_EVENT_ANALYSIS = Arrays.asList(GuiStringConstants.CONTROLLER, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS, 
			GuiStringConstants.FAILURE_RATIO);
    
    public static final List<String> ACCESSAREA_GROUP_SUMMARY_EVENT_ANALYSIS = Arrays.asList(GuiStringConstants.ACCESS_AREA, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS, 
			GuiStringConstants.FAILURE_RATIO);
    
    /*--------------------------------------------Constants for new UI GSM Automation ------------------------------*/
    
	public static final List<String> controllerCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.CAUSE_GROUP_ID,
			GuiStringConstants.CAUSE_GROUP, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	public static final List<String> controllerSubCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.EXTENDED_CAUSE_ID_GSM,
			GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);
	
	public static final List<String> accessAreaCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.CAUSE_CODE_ID,
			GuiStringConstants.CAUSE_CODE_DESCRIPTION, GuiStringConstants.OCCURRENCES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);
	
	public static final List<String> accessAreaCauseCodeNetworkSubMenus1 = Arrays.asList("Cause Group ID",
			"Cause Group", "Failures",
			GuiStringConstants.IMPACTED_SUBSCRIBERS);


	public static final List<String> accessAreaSubCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.EXTENDED_CAUSE_CODE_ID,
			GuiStringConstants.EXTENDED_CAUSE_CODE_DESC, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);
	
	public static final List<String> accessAreaSubCauseCodeNetworkSubMenusNewUi = Arrays.asList("Extended Cause ID",
			"Extended Cause Value", GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);


	public static final List<String> accessAreaDetailedEventAnalysisNetworkSubMenus = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.EXTENDED_CAUSE_CODE_DESC, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.RAN_VENDOR);
	
	public static final List<String> accessAreaDetailedEventAnalysisNetworkSubMenusNewUI = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.EVENT_TYPE,
			GuiStringConstants.CONTROLLER,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE, GuiStringConstants.URGENCY_CONDITION,
			GuiStringConstants.CAUSE_GROUP, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.ASSIGNMENT_FAILURE_CAUSE_VALUE,
			GuiStringConstants.CHANNEL_TYPE,GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR,GuiStringConstants.RSAI);

	 public static final List<String> accessAreaGroupIndivCCASummaryWindow = Arrays.asList(GuiStringConstants.ACCESS_AREA, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);
	 
	 public static final String CAUSE_CODE_ANALYSIS = "Cause Code Analysis";
	 
	 public static final String NETWORK_EVENT_ANALYSIS = "Network Event Analysis";
	 
	 public static final String ROAMING_BY_COUNTRY = "Roaming by Country";
	 
	 public static final String ROAMING_BY_OPERATOR = "Roaming by Operator";
	 
	 public static final String GSM_EVENT_TRACE = "GSM Event Trace";
	 
	 public static final String CALL_FAILURE = "Call Failure";

	public static final List<String> controllerGroupSummaryWindow = Arrays.asList(GuiStringConstants.EVENT_TYPE,
				GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);

	public static final List<String> controllerGroupEventSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS,
			GuiStringConstants.FAILURE_RATIO);
	
	public static final List<String> controllerGroupEventSummaryWindowNewUI = Arrays.asList(GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, 
			GuiStringConstants.FAILURE_RATIO);

	public static final List<String> accessAreaGroupEventSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, 
			GuiStringConstants.FAILURE_RATIO);
    
    
    
}