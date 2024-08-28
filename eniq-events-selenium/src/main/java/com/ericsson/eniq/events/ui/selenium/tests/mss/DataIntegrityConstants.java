package com.ericsson.eniq.events.ui.selenium.tests.mss;

public class DataIntegrityConstants {
	
    public static final String HYPHEN_SYMBOL = "-";
    public static final String BLANK_SPACE = "";
    public static final String COMMA_SYMBOL = ",";
    public static final String HASH_SYMBOL = "#";
    public static final String EQUALS_SYMBOL = "=";
    
    
    
    public static final int CSV_IMSI_INDEX = 5;    
    public static final int CSV_TAC_INDEX = 6;    
    public static final int CSV_RAT_INDEX = 17;    
    public static final int CSV_CONTROLLER_INDEX = 19;
    public static final int CSV_ACCESSAREA_INDEX = 20;    
    public static final int CSV_MSC_INDEX = 18;    
    public static final int CSV_CALLDURATION_INDEX = 32;    
    public static final int CSV_EVENTTYPE_INDEX = 9;
    public final static int EventType_Index = 0;	
    public final static int Failure_Index = 1;
    public final static int Successes_Index = 2;
    public final static int Total_Events_Index = 3;
    public final static int Success_Ratio_Index = 4;
    public final static int Impacted_Subscriber_Index = 5;
    public final static String NO_DATA = "No_Data";
    public final static String NO_VALIDATION_REQUIRED = "NO_VALIDATION";
    public final static String RANKING_ANALYSIS = "RANKING";
    public final static String FAILURE_ANALYSIS = "FAILURE";
    public final static String TERMINAL_ANALYSIS = "TERMINAL";
    public final static String EVENT_VOLUME_ANALYSIS = "EVENTVOLUME";
    public final static String NETWORK_EVENT_VOLUME_ANALYSIS = "NETWORKEVENTVOLUME";
    public final static String EVENT_VOLUME_GROUP_ANALYSIS = "EVENTVOLUMEGROUP";
    public final static String NETWORK_EVENT_VOLUME_GROUP_ANALYSIS = "NETWORKEVENTVOLUMEGROUP";
    public final static String TERMINAL_GROUP_ANALYSIS = "TERMINALGROUP";
    public final static String MSC_GROUP_ANALYSIS = "MSCGROUP";
    public final static String ACCESS_AREA_GROUP_ANALYSIS = "ACCESSAREAGROUP";
    public final static String CONTROLLER_GROUP_ANALYSIS = "CONTROLLERGROUP";
    public final static String AGGREGRATION_ANALYSIS = "AGGREGRATION";
    public final static String KPI_ANALYSIS = "KPIANALYSIS";
    public final static String KPI_GROUP_ANALYSIS = "KPIGROUPANALYSIS";
    public final static String AGGREGRATION_ERROR_MESSAGE = " Data Integrity Check Failed - The Aggregration is failed for ";
    public final static String FAILURE_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The Failure Analysis ends with an error for ";
    public final static String TERMINAL_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The Terminal Analysis ends with an error for ";
    public final static String TERMINAL_GROUP_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The Terminal Group Analysis ends with an error for TAC's ";
    public final static String CAUSE_CODE_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The Cause Code Analysis ends with an error for ";
    public final static String KPI_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The KPI Analysis ends with an error for ";
    public final static String KPI_GROUP_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The KPI Analysis ends with an error for Group ";
    public final static String EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The Event Volume Analysis ends with an error for ";
    public final static String NETWORK_EVENT_VOLUME_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - The KPI Analysis ends with an error for ";
    public final static String ASCENDING_ORDER_SORT_ERROR = " Data Integrity Check Failed - The Ascending order sort failed for column ";
    public final static String DESCENDING_ORDER_SORT_ERROR = " Data Integrity Check Failed - The Descending order sort failed for column ";
    public final static String RANKING_ANALYSIS_ERROR_MESSAGE = " Data Integrity Check Failed - Ranking Analysis ends with an error. ";
    public final static String STATUS_NONE = "NONE";
    public final static String STATUS_SUCCESS = "SUCCESS";
    public final static String STATUS_DROPPED = "DROPPED";
    public final static String STATUS_BLOCKED = "BLOCKED";
    public final static String STATUS_ERROR = "ERROR";
    public final static String NO_RANK_ASSIGNED = "NO_RANK";
    public final static String RNC_CONTROLLER = "RNC";
    public final static String BSC_CONTROLLER = "BSC";
    public final static String GSM = "GSM";
    public final static String Three_G = "3G";
    public final static String LTE = "LTE";
    public final static String RAT_ID = "RAT ID";
    public final static String SMS_DELIVER_SC_TO_MS = "sMSdeliverSCtoMS";
    public final static String SMS_DELIVEREPORT_MS_TO_SC =	"sMSdeliveReportMStoSC";
    public final static String SMS_STATUS_REPORT_SC_TO_MS =	"sMSstatusReportSCtoMS";
    public final static String SMS_COMMAND_MS_TO_SC =	"sMScommandMStoSC";
    public final static String SMS_SUBMIT_MS_TO_SC =	"sMSsubmitMStoSC";
    public final static String SMS_SUBMIT_REPORT_SC_TO_MS =	"sMSsubmitReportSCtoMS";
    public final static String RESERVED_MTI_VALUE =	"reservedMTIValue";
    
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_CAMELREASON = "unsuccessfulMOSMSDeliverytoSMSCDuetoCAMELReason";
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_OTHERREASON = "unsuccessfulMOSMSDeliverytoSMSCDuetoOtherReason";
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_CAMELREASON = "unsuccessfulMTSMSDeliverytoMSDuetoCAMELReason";
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_OTHERREASON =  "unsuccessfulMTSMSDeliverytoMSDuetoOtherReason";
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_SMSCDUE_TO_RTCFAREASON = "unsuccessfulMOSMSDeliverytoSMSCDuetoRTCFAReason";
    public final static String UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_RTCFAREASON = "unsuccessfulMTSMSDeliverytoMSDuetoRTCFAReason";

    public final static String CALL_POSITION_FOR_UNANSWERED_CALL = "2";
    public final static String DISCONNECTED_PARTY_ZERO_FOR_UNANSWERED_CALL = "0";
    public final static String DISCONNECTED_PARTY_ONE_FOR_UNANSWERED_CALL = "1";
    public final static String INTERNAL_CAUSE_CODE_THREE_FOR_UNANSWERED_CALL = "3";
    public final static String INTERNAL_CAUSE_CODE__FOUR_FOR_UNANSWERED_CALL = "4";
    public final static String INTERNAL_CAUSE_CODE__SIX_FOR_UNANSWERED_CALL = "6";
    
    
    public final static String TOPOLOGY_RAT = "RAT";
    public final static String TOPOLOGY_MCC = "MCC";
    public final static String TOPOLOGY_MNC = "MNC";
    public final static String TOPOLOGY_LAC = "LAC";
    public final static String TOPOLOGY_CONTROLLER = "HIERARCHY_3";
    public final static String TOPOLOGY_ACCESS_AREA = "HIERARCHY_1";
    public final static String TOPOLOGY_VENDOR = "VENDOR";
    public final static String TOPOLOGY_COUNTRY = "COUNTRY";
    public final static String TOPOLOGY_OPERATOR = "OPERATOR";
    public final static String TOPOLOGY_TAC = "TAC";
    public final static String TOPOLOGY_MARKETING_NAME = "MARKETING_NAME";
    public final static String TOPOLOGY_MANUFACTURER = "MANUFACTURER";
    public final static String TOPOLOGY_FAULT_CODE = "FAULT_CODE";
    public final static String TOPOLOGY_FAULT_CODE_DESC = "FAULT_CODE_DESC";
    public final static String TOPOLOGY_ADVICE = "ADVICE";
    public final static String TOPOLOGY_TELE_SERVICE_CODE = "TELE_SERVICE_CODE";
    public final static String TOPOLOGY_TELE_SERVICE_CODE_DESC = "TELE_SERVICE_CODE_DESC";
    public final static String TOPOLOGY_BEARER_SERVICE_CODE = "BEARER_SERVICE_CODE"; 
    public final static String TOPOLOGY_BEARER_SERVICE_CODE_DESC = "BEARER_SERVICE_CODE_DESC";
    
    
    public static final String RESERVED_DATA_TERMINAL_MAKE = "TERMINAL_MAKE";
    public static final String RESERVED_DATA_TERMINAL_MODEL = "TERMINAL_MODEL";
    public static final String RESERVED_DATA_TAC = "TAC";
    public static final String RESERVED_CONTROLLER_MSS = "CONTROLLER_MSS";
    public static final String RESERVED_ACCESS_AREA_MSS = "ACCESS_AREA_MSS";
    public static final String RESERVED_CONTROLLER_GROUP_MSS = "CONTROLLER_GROUP_DATA_MSS";
    public static final String RESERVED_ACCESS_AREA_GROUP_MSS = "ACCESS_AREA_GROUP_DATA_MSS";
    public static final String RESERVED_TAC_GROUP_MSS = "TERMINAL_GROUP_DATA_MSS";
    public static final String RESERVED_MSISDN_MSS = "MSISDN_MSS";
    public static final String RESERVED_IMSI_MSS = "IMSI";
    public static final String RESERVED_IMSI_GROUP_MSS = "IMSI_GROUP_DATA_MSS";
    
 //MSS Enhancement for 3.0.11
    
    public static final String EXTERNAL_PROTOCOL_NAME = "External Protocol Name";
    public static final String EXTERNAL_CAUSE_CODE = "External Cause Code";
    public static final String EXTERNAL_CAUSE_VALUE = "External Cause Value";
    public static final String INCOMING_ROUTE = "Incoming Route";
    public static final String OUTGOING_ROUTE = "Outgoing Route";
    
    public static final String FAULTCODE = "Called subscriber is temporarily barred for all incoming traffic. B-subscriber category TBI = 1.";
        
    
}
