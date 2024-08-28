/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseWcdmaCfa extends EniqEventsUIBaseSeleniumTest {

    static final String MANUFACTURER_VALUE = "Sony Ericsson";

    static final String MODEL_VALUE = "T316";

    static final String TAC_VALUE = "1015771";

    public static final String TERMINAL_GROUP = "GPEH_Two_TACs";

    public static final String IMSI_VALUE = "460010000000001";//"240010000000030";a

    public static final String IMSI_GROUP_VALUE = "GPEH_Auto_IMSI1"; //"GPEH_Auto_IMSI3";

    static final String PTMSI_VALUE = "123456789101112";

    static final String MSISDN_VALUE = "201918171615141";

    static final String VENDOR_VALUE_DASH = "Ericsson - 3G";

    public static final String ACCESS_AREA_VALUE = "RNC01-1-2,,ONRM_ROOT_MO_R:RNC01:RNC01,Ericsson,3G";

    public static final String ACCESS_AREA_GROUP_VALUE = "GPEH_Auto_Cell1";

    public static final String CONTROLLER_VALUE = "ONRM_ROOT_MO_R:RNC01:RNC01,Ericsson,3G";

    public static final String CONTROLLER_GROUP_VALUE = "GPEH_Auto_RNC1";//"Controller_WCDMA";

    static final String RAN_VENDOR = GuiStringConstants.RAN_VENDOR;

    static final String RNC = GuiStringConstants.RNC;

    static final String FAILURES = GuiStringConstants.FAILURES;

    static final String RE_ESTABLISHMENT_FAILURES = GuiStringConstants.RE_ESTABLISHMENT_FAILURES;

    static final String FAILURES_TREND = "Failure Trend";

    static final String EVENT_TYPE = GuiStringConstants.EVENT_TYPE;

    static final String EVENT_NAME = GuiStringConstants.EVENT_NAME;

    static final String EVENT_ID = "Event ID";

    static final String HIER3_CELL_ID = "HIER3_CELL_ID";

    static final String CONTROLLER = GuiStringConstants.CONTROLLER;

    static final String FAILURE_RATIO_PER = GuiStringConstants.FAILURE_RATIO_PER;

    static final String RAB_FAILURES_RATIO = GuiStringConstants.RAB_FAILURES_RATIO;

    static final String IMPACTED_SUBSCRIBERS = GuiStringConstants.IMPACTED_SUBSCRIBERS;

    static final String EVENT_TIME = GuiStringConstants.EVENT_TIME;

    static final String IMSI = GuiStringConstants.IMSI;

    static final String GRID_CELL_LINK = GuiStringConstants.GRID_CELL_LINK;

    static final String GRID_CELL_LAUNCHER_LINK = GuiStringConstants.GRID_CELL_LAUNCHER_LINK;

    static final String TAC = GuiStringConstants.TAC;

    static final String TERMINAL_MAKE = GuiStringConstants.TERMINAL_MAKE;

    static final String TERMINAL_MODEL = GuiStringConstants.TERMINAL_MODEL;

    static final String SEVERITY_INDICATOR = GuiStringConstants.SEVERITY_INDICATOR_CFA;

    static final String PROCEDURE_INDICATOR = GuiStringConstants.PROCEDURE_INDICATOR;

    static final String EVALUATION_CASE = GuiStringConstants.EVALUATION_CASE;

    static final String EXCEPTION_CLASS = GuiStringConstants.EXCEPTION_CLASS;

    static final String CAUSE_VALUE = GuiStringConstants.CAUSE_VALUE;

    static final String EXTENDED_CAUSE_VALUE = GuiStringConstants.EXTENDED_CAUSE_VALUE;

    static final String RANK = GuiStringConstants.RANK;

    static final String ACCESS_AREA = GuiStringConstants.ACCESS_AREA;

    static final String DASH = GuiStringConstants.DASH;

    static final String SPACE = GuiStringConstants.SPACE;

    static final String WCDMA_CALL_FAILURE = GuiStringConstants.WCDMA_CALL_FAILURE;

    static final String CALL_FAILURE_ANALYSIS = GuiStringConstants.CALL_FAILURE_ANALYSIS;

    static final String CAUSE_CODE = GuiStringConstants.CAUSE_CODE;

    static final String SUB_CAUSE_CODE = GuiStringConstants.SUB_CAUSE_CODE;

    static final String Disconnection_Desc = GuiStringConstants.Disconnection_Desc;

    static final String DISCONNECTION_DESC = GuiStringConstants.DISCONNECTION_DESC;

    static final String DISCONNECTION_SUBCODE = GuiStringConstants.DISCONNECTION_SUBCODE;

    static final String MANUFACTURER = GuiStringConstants.MANUFACTURER;

    static final String MODEL = GuiStringConstants.MODEL;

    static final String SUB_CAUSE_CODE_ID = GuiStringConstants.SUB_CAUSE_CODE_ID;

    static final String LAC = GuiStringConstants.LAC;

    static final String RAC = GuiStringConstants.RAC;

    static final String EVENT_RANKING = GuiStringConstants.EVENT_RANKING;

    static final String DRILL_ON = GuiStringConstants.DRILL_ON;

    static final String DRILL_ON_2 = GuiStringConstants.DRILL_ON_2;

    static final String LIVE_LOAD = GuiStringConstants.LIVE_LOAD;

    static final String COLON = GuiStringConstants.COLON;

    static final String EVENT_ANALYSIS_SUMMARY = GuiStringConstants.EVENT_ANALYSIS_SUMMARY;

    static final String CAUSE_CODE_ID = "Cause Code Id";

    static final String RRC_ESTABLISHMENT_CAUSE = GuiStringConstants.RRC_ESTABLISHMENT_CAUSE;

    static final String DISCONNECTION_DESCRIPTION = GuiStringConstants.DISCONNECTION_DESCRIPTION;

    static final String DISCONNECTION_CODE_ID = GuiStringConstants.DISCONNECTION_CODE_ID;

    static final String DISCONNECTION_SUBCODE_ID = GuiStringConstants.DISCONNECTION_SUBCODE_ID;

    // Default Column Headers

    //This is the column for the sub cause code Widget
    public final List<String> defaultSubCauseCodeWidgetColumns=Arrays.asList(SUB_CAUSE_CODE_ID, SUB_CAUSE_CODE);

    public final List<String> defaultGroupEventAnalysisColumns = Arrays.asList(EVENT_TYPE, FAILURES, IMPACTED_SUBSCRIBERS);

    public final static List<String> defaultTerminalEventAnalysis = Arrays.asList(GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL,
            EVENT_TYPE, FAILURES, RE_ESTABLISHMENT_FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS);

    public final static List<String> defaultCallFailureAnalysisColumns = Arrays.asList(RANK, CONTROLLER, EVENT_TYPE, FAILURES);

    public final static List<String> defaultControllerEventAnalysisWindowColumns = Arrays.asList(RAN_VENDOR, CONTROLLER, EVENT_TYPE, FAILURES,
            RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS, RAB_FAILURES_RATIO);

    public final static List<String> defaultControllerNetworkEventAnalysisWindowColumns = Arrays.asList(RAN_VENDOR, CONTROLLER, EVENT_TYPE, FAILURES,
            RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS, RAB_FAILURES_RATIO);

    public final static List<String> defaultFailedEventAnalysisWindowColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC,
            TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
            EXTENDED_CAUSE_VALUE, LAC, RAC, SEVERITY_INDICATOR, GuiStringConstants.RAB_TYPE, GuiStringConstants.DISCONNECTION_CODE,
            GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC, GuiStringConstants.TRIGGER_POINT,
            RRC_ESTABLISHMENT_CAUSE));

    public final static List<String> defaultFEAnalysisWindowColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
            EXTENDED_CAUSE_VALUE, LAC, RAC, SEVERITY_INDICATOR, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, RRC_ESTABLISHMENT_CAUSE));

    public final static List<String> defaultWcdmaCallFailureRoamingbyNetworkEventsWindowColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME,
            IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            SEVERITY_INDICATOR));

    public final static List<String> defaultWcdmaCallFailureControllerSubCauseWindowColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI,
            TAC, TERMINAL_MAKE, TERMINAL_MODEL, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, LAC, RAC,
            GuiStringConstants.VENDOR));

    public final static List<String> defaultAccessAreaCFAColumns = Arrays.asList(RANK, FAILURES_TREND, ACCESS_AREA, CONTROLLER, EVENT_TYPE, FAILURES);

    final static List<String> defaultAccessAreaEventAnalysisColumns = Arrays.asList(RAN_VENDOR, CONTROLLER, ACCESS_AREA, EVENT_TYPE, FAILURES,
            RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS, RAB_FAILURES_RATIO);

    final static List<String> defaultCauseCodeWcdmaColumns = Arrays.asList(RANK, CAUSE_CODE, CAUSE_CODE_ID, FAILURES);

    final static List<String> defaultDisconnectionCodeWcdmaColumns = Arrays.asList(RANK, DISCONNECTION_DESCRIPTION,FAILURES,IMPACTED_SUBSCRIBERS, DISCONNECTION_CODE_ID,
            DISCONNECTION_SUBCODE_ID );

    final static List<String> defaultDisconnectionCodeWcdmaColumnsGrid = Arrays.asList(DISCONNECTION_DESCRIPTION, FAILURES, IMPACTED_SUBSCRIBERS,
            DISCONNECTION_CODE_ID,DISCONNECTION_SUBCODE_ID);

    final static List<String> defaultControllerCauseCodeWcdmaColumns = Arrays.asList(CAUSE_CODE_ID, CAUSE_CODE, FAILURES, IMPACTED_SUBSCRIBERS);

    final static List<String> defaultExtendedCauseCodeDescColumns = Arrays.asList(SUB_CAUSE_CODE_ID, SUB_CAUSE_CODE, FAILURES, IMPACTED_SUBSCRIBERS);

    //Will need to raise bug on "Sub Cause Code ID" ID should be Id consist with other windows
    final static List<String> defaultExtendedCauseCodeDescWindowColumns = Arrays.asList("Sub Cause Code ID", SUB_CAUSE_CODE, FAILURES,
            IMPACTED_SUBSCRIBERS);

    final static List<String> defaultTerminalCfaRankingColumns = Arrays.asList(RANK, MANUFACTURER, MODEL, TAC, FAILURES, RE_ESTABLISHMENT_FAILURES);

    final static List<String> defaultTerminalMostCallDropsColumns = Arrays
            .asList(RANK, TAC, MANUFACTURER, MODEL, FAILURES, RE_ESTABLISHMENT_FAILURES);

    final static List<String> defaultTerminalGroupAnalysisColumns = Arrays.asList(TAC, MANUFACTURER, MODEL, FAILURES, RE_ESTABLISHMENT_FAILURES);

    final static List<String> defaultTerminalGroupAnalysisCallFailureColumns = Arrays.asList(TAC, MANUFACTURER, MODEL, FAILURES);

    final static List<String> callDropsImsiEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
            EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callDropsTacEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callDropsDisconnectionEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.TRIGGER_POINT));

    public final static List<String> defaultAccessAreaFailedEventAnalysisWindowColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC,
            TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
            EXTENDED_CAUSE_VALUE, LAC, RAC, SEVERITY_INDICATOR, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callSetupImsiEventAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
            EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA);

    final static List<String> defaultcallDropsFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
            EXTENDED_CAUSE_VALUE, LAC, RAC, GuiStringConstants.SEVERITY_INDICATOR_CFA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM,
            GuiStringConstants.RSCP_CELL_ONE_DBM, GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE,
            GuiStringConstants.DISCONNECTION_DESC, GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE));

    final static List<String> defaultCallFailureAnalysisTerminalColumns = new ArrayList<String>(Arrays.asList(GuiStringConstants.EVENT_TIME, IMSI,
            TAC, TERMINAL_MAKE, TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS,
            CAUSE_VALUE, EXTENDED_CAUSE_VALUE, LAC, RAC, GuiStringConstants.SEVERITY_INDICATOR_CFA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callDropsFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, CONTROLLER, ACCESS_AREA, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE,
            EXTENDED_CAUSE_VALUE, LAC, RAC, GuiStringConstants.SEVERITY_INDICATOR_CFA, GuiStringConstants.RAB_TYPE,
            GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB, GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM,
            GuiStringConstants.DISCONNECTION_CODE, GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC,
            GuiStringConstants.TRIGGER_POINT, GuiStringConstants.RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callSetupFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA, GuiStringConstants.RAB_TYPE, GuiStringConstants.TRIGGER_POINT,
            RRC_ESTABLISHMENT_CAUSE));

    final static List<String> callTerminalSetupFailedEventAnalysisColumns = new ArrayList<String>(Arrays.asList(EVENT_TIME, IMSI, TAC, TERMINAL_MAKE,
            TERMINAL_MODEL, EVENT_TYPE, PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE,
            GuiStringConstants.SEVERITY_INDICATOR_CFA, CONTROLLER, ACCESS_AREA, GuiStringConstants.RAB_TYPE, GuiStringConstants.DISCONNECTION_CODE,
            GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC, GuiStringConstants.TRIGGER_POINT,
            RRC_ESTABLISHMENT_CAUSE));

    final static List<String> defaultIMSIEventAnalysisColumns = Arrays.asList(EVENT_TIME, TAC, TERMINAL_MAKE, TERMINAL_MODEL, EVENT_TYPE,
            PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, GuiStringConstants.SEVERITY_INDICATOR_CFA,
            GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA, GuiStringConstants.RAB_TYPE, GuiStringConstants.CPICH_EC_NO_CELL_ONE_DB,
            GuiStringConstants.UL_INT_CELLONE_DBM, GuiStringConstants.RSCP_CELL_ONE_DBM, GuiStringConstants.DISCONNECTION_CODE,
            GuiStringConstants.DISCONNECTION_SUBCODE, GuiStringConstants.DISCONNECTION_DESC, GuiStringConstants.TRIGGER_POINT,
            GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);

    final static List<String> subscriberCallFailureByCallDropsEventRanking = new ArrayList<String>(Arrays.asList(RANK, IMSI, FAILURES,
            RE_ESTABLISHMENT_FAILURES));

    final static List<String> subscriberCallFailureByCallSetupEventRanking = new ArrayList<String>(Arrays.asList(RANK, IMSI, FAILURES));

    final static String[] callDropsWindowColumnsToCheckforDataIntegrity = new String[] { EVENT_TYPE, IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
            PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR, LAC, RAC };

    final static String[] callFailureAnalysisTerminalWindowColumnsToCheckforDataIntegrity = new String[] { IMSI, TAC, TERMINAL_MAKE, TERMINAL_MODEL,
            PROCEDURE_INDICATOR, EVALUATION_CASE, EXCEPTION_CLASS, CAUSE_VALUE, EXTENDED_CAUSE_VALUE, SEVERITY_INDICATOR, LAC, RAC };

    // XML File Constants

    final static String HIER3_ID = "HIER3_ID";

    static final String CC_FAILURES = "CC Failures";

    static final String FAILURES_2 = "Failures 2";

    static final String IMPACTED_SUBSCRIBERS_2 = "Impacted Subscribers 2";

    static final String RE_ESTABLISHMENT_FAILURES_2 = "Re-establishment Failures 2";

    static final String EVENT_TYPE_2 = "Event Type 2";

    static final String ERROR = "ERROR";

    static final String TRUE = "true";

    static final String CALL_SETUP_FAILURES = "Call Setup Failures";

    static final String TOTAL_RAB_FAILURES = "Total RAB Failures";

    static final String SUB_CAUSE_CODE_ANALYSIS_FOR_WCDMA_CFA_CALL_SETUP = "Sub Cause Code Analysis for WCDMA CFA Call Setup";

    static final String SUB_CAUSE_CODE_ANALYSIS_FOR_WCDMA_CFA_CALL_DROPS = "Sub Cause Code Analysis for WCDMA CFA Call Drops";

    static final String DISCONNECTION_CODE_ANALYSIS_FOR_WCDMA_CFA_CALL_DROPS_FAILURE_DRILLON = "Disconnection Code Analysis for WCDMA CFA Call Drops on Failue Drill";

    static final String CAUSE_CODE_WCDMA_CALL_FAILURE_BY_CALL_SETUP_FAILURE_EVENT_RANKING = "Cause Code WCDMA Call Failure by Call Setup Failure Event Ranking";

    static final String CALL_DROPS = "Call Drops";

    static final String MULTI_RAB_FAILURES = "Multi RAB Failures";

    static final String PACKET_SWITCHED_RAB_FAILURES = "Packet Switched RAB Failures";

    static final String CIRCUIT_SWITCHED_RAB_FAILURES = "Circuit Switched RAB Failures";

    static final String DISCONNECTION_CODE_UNKNOWN = "Disconnection Code: Unknown";

    static final String DISCONNECTION_TYPE = "Disconnection Type";

    static final String SRB_13_6_13_6 = "SRB_13.6_13.6";

    static final String RAB_DESCRIPTION = "RAB Description";

}
