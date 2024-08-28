/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaHFA;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*F
 * Updated/refactored by efreass based on VS_ENIQ_Events12_2_WCDMA_HFA_2.2.14_SERVER2012
 * 23 july 2012
 * More can be done
 */
class BaseWcdmaHfa extends EniqEventsUIBaseSeleniumTest {

    //#####################################  Ranking tests group CHART VIEW ###########################
    static final String CHART_VIEW = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Chart View']/input";

    static final String SELECT_ALL = "//div[contains(@id, 'selenium_tag_BaseWindow_NETWORK_CAUSE_CODE_ANALYSIS')]//div//span[label='Select all']/input";

    static final String LAUNCH_BUTTON = "//div[contains(@id, 'selenium_tag_BaseWindow') and contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS')]"
            + "/div//div[button='Launch']/button";

    //#####################################  CONSTANTS FOR UI HFA TESTING ###########################
    static final String ACCESS_AREA_VALUE = "82040A,,smartone_R:RNC09:RNC09,Ericsson,3G";

    static final String ACCESS_AREA_GROUP_VALUE = "Automation";

    static final String CONTROLLER_VALUE = "ONRM_ROOT_MO_R:RNC01:RNC01,Ericsson,3G";

    static final String CONTROLLER_2G_VALUE = "BSC5";

    static final String CONTROLLER_IFHO = "ONRM_ROOT_MO_R:RNC05:RNC05";

    static final String CONTROLLER_HSDSCH = "ONRM_ROOT_MO_R:RNC07:RNC07";

    static final String CONTROLLER_SOHO = "ONRM_ROOT_MO_R:RNC06:RNC06";

    public static final String CONTROLLER_GROUP_VALUE = "AutomationControllerGroupTest";//"Controller_WCDMA";

    static final String MODEL_VALUE = "T316";

    static final String TAC_VALUE = "35815004";

    static final String TERMINAL_GROUP = "AutomationTerminalGroupTest";

    static final String TERMINAL_MAKE = "Samsung";

    static final String TERMINAL_MODEL = "GT-I9100";

    static final String IMSI_VALUE = "454061106059153";

    static final String IMSI_SOHO_VALUE = "454063302842760";

    static final String IMSI_IRAT_VALUE = "454063307178959";

    static final String IMSI_IFHO_VALUE = "454061106133426";

    static final String IMSI_HSDSCH_VALUE = "454061100148219";

    static final String MSISDN_VALUE = "123456789";

    static final String PTMSI_VALUE = "123456789";

    static final String IMSI_GROUP_VALUE = "";

    static final String SOHO_LABEL_VALUE = "CC1-SOHO";

    static final String IFHO_LABEL_VALUE = "CC1-IFHO_EXE_ACTIVE";

    static final String IRAT_LABEL_VALUE = "CC1-IFHO_EXE_ACTIVE";

    static final String IMSI = GuiStringConstants.IMSI;

    static final String HANDOVER_TYPE = GuiStringConstants.HANDOVER_TYPE;

    static final String HANDOVER_TYPE_2 = GuiStringConstants.HANDOVER_TYPE_2;

    static final String CONTROLLER = GuiStringConstants.CONTROLLER;

    static final String CONTROLLER_GROUP = GuiStringConstants.CONTROLLER_GROUP;

    static final String FAILURES = GuiStringConstants.FAILURES;

    static final String FAILURES_2 = GuiStringConstants.FAILURES_2;

    static final String IMPACTED_SUBSCRIBERS = GuiStringConstants.IMPACTED_SUBSCRIBERS;

    static final String IMPACTED_SUBSCRIBERS_2 = GuiStringConstants.IMPACTED_SUBSCRIBERS_2;

    static final String DRILL_ON = GuiStringConstants.DRILL_ON;

    static final String RAN_VENDOR = GuiStringConstants.RAN_VENDOR;

    static final String TAC = GuiStringConstants.TAC;

    static final String EVENT_TRIGGER = GuiStringConstants.EVENT_TRIGGER;

    static final String SOURCE_CELL = GuiStringConstants.SOURCE_CELL;

    static final String TARGET_CELL = GuiStringConstants.TARGET_CELL;

    static final String CAUSE_VALUE = GuiStringConstants.CAUSE_VALUE;

    static final String CPICH_EC_NO_SOURCE_CELL = GuiStringConstants.CPICH_EC_NO_SOURCE_CELL;

    static final String RSCP_SOURCE_CELL = GuiStringConstants.RSCP_SOURCE_CELL;

    static final String SOURCE_RAC = GuiStringConstants.SOURCE_RAC;

    static final String SOURCE_LAC = GuiStringConstants.SOURCE_LAC;

    static final String RSCP_EVAL_CELL = GuiStringConstants.RSCP_EVAL_CELL;

    static final String CPICH_EC_NO_EVAL_CELL = GuiStringConstants.CPICH_EC_NO_EVAL_CELL;

    static final String EVENT_TIME = GuiStringConstants.EVENT_TIME;

    /*
     * SHARED WINDOW HEADER by Network Tests Group and Ranking Tests Group
     */
    static final List<String> defaultControllerEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    static final List<String> defaultControllerEventAnalysisHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    static final List<String> defaultControllerEventAnalysisIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriberWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME,/* GuiStringConstants.IMSI, */GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriberFailedEASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriberWCDMAIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, /*GuiStringConstants.IMSI,*/GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriberWCDMAIRATWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriberWCDMAHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultSubscriberFailedEAHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultSubscriberWCDMAHSDSCHWindow51d = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    /*  ############################        NETWORK ANALYSIS TEST GROUP WINDOWS TAB CONSTANTS     ##################*/

    static final List<String> defaultControllerGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    static final List<String> defaultAccessAreaEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_FAILURES,
            GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.TARGET_FAILURES,
            GuiStringConstants.TARGET_IMPACTED_SUBSCRIBERS));

    static final List<String> defaultSourceNetworkEventAnalysiWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, /*GuiStringConstants.IMSI,*/GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultTargetNetworkEventAnalysiWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultGroupNetworkEventAnalysiWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultSourceNetworkEventAnalysiWCDMAIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultTargetNetworkEventAnalysiWCDMAIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.PLMN_TARGET_ID, GuiStringConstants.LAC_TARGET_ID,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL,
            GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultGroupNetworkEventAnalysiWCDMAIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultNetworkEventAnalysiWCDMAIRATWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_BSC, GuiStringConstants.TARGET_LAC,
            GuiStringConstants.TARGET_PLMN, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultGroupNetworkDetailEventAnalysisWCDMAIRATWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
                    GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_LAC,
                    GuiStringConstants.TARGET_PLMN, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
                    GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
                    GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.RSSI, GuiStringConstants.TARGET_CONF,
                    GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
                    GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    static final List<String> defaultGroupTargetNetworkDetailEventAnalysisWCDMAIRATWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
                    GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
                    GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
                    GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
                    GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    static final List<String> defaultNetworkEventAnalysiWCDMAHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
            GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL,
            GuiStringConstants.GBR_DL));

    static final List<String> defaultTargetNetworkEventAnalysiWCDMAHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
            GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL,
            GuiStringConstants.GBR_DL));

    static final List<String> defaultGroupSourceNetworkDetailEventAnalysisWCDMAHSDSCHWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
                    GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
                    GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
                    GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
                    GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    static final List<String> defaultGroupTargetNetworkDetailEventAnalysisWCDMAHSDSCHWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
                    GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
                    GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET,
                    GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    /* ############################# Window Headers ################################ */

    public static final String RNC_WCDMA_Handover_Failure_Event_Ranking = "RNC WCDMA Handover Failure Event Ranking";

    /*  ############################        RANKING ANALYSIS TEST GROUP WINDOWS TAB CONSTANTS     ##################*/

    final static List<String> defaultRNCRankingWCDMAHandOverFailureWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.RANK, GuiStringConstants.RAN_VENDOR, GuiStringConstants.RNC,
                    GuiStringConstants.FAILURES));

    final static List<String> defaultRankingGridViewWCDMAHFAWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.LABEL, GuiStringConstants.CAUSE_CODE, GuiStringConstants.FAILURES,
            GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.HANDOVER_TYPE));

    final static List<String> defaultAccessAreaRankingWCDMAHandOverFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.FAILURES));

    final static List<String> sourceCell3GRankingWCDMAFailuresDrillDownWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
                    GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
                    GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
                    GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
                    GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
                    GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
                    GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET,
                    GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> detailedSourceCell3GRankingWCDMAFailuresDrillDownWindowSOHO = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
                    GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
                    GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
                    GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
                    GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
                    GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowSOHO = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
                    GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
                    GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
                    GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
                    GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> detailedTargetCell3GRankingWCDMAFailuresDrillDownWindowHSDSCH = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TAC,
                    GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
                    GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
                    GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
                    GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
                    GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultAccessAreaTargetRankingWCDMAHandOverFailureWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.RANK, GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
                    GuiStringConstants.TARGET_CELL, GuiStringConstants.FAILURES));

    final static List<String> defaultTerminalRankingWCDMAHandOverFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.TAC,
            GuiStringConstants.FAILURES));

    final static List<String> defaultSubscriberRankingWCDMAHandOverFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.IMSI, GuiStringConstants.FAILURES));
    
    final static List<String> defaultSubscriberRankingWCDMARecurringFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.IMSI, GuiStringConstants.FAILURES, GuiStringConstants.CONTROLLER, 
            GuiStringConstants.ACCESS_AREA));
    
    final static List<String> defaultRankingWCDMARecurringFailureWindow = new ArrayList<String>(Arrays.asList(
    		GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, 
    		GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVALUATION_CASE, 
    		GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, 
    		GuiStringConstants.SEVERITY_INDICATOR_HFA));

    final static List<String> defaultCCRankingWCDMAHandOverFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.CAUSE_CODE,
            GuiStringConstants.FAILURES));

    final static List<String> defaultCauseCodeRankingWCDMAHandOverFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.CAUSE_CODE,
            GuiStringConstants.CAUSE_CODE_ID, GuiStringConstants.FAILURES));

    final static List<String> defaultCauseCodeRankingWCDMACallDropsFailureWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CAUSE_CODE_ID,
            GuiStringConstants.FAILURES));

    final static List<String> defaultTargetCellEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.FAILURES));

    final static List<String> defaultCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.LABEL, GuiStringConstants.CAUSE_CODE, GuiStringConstants.FAILURES,
            GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.GROUP_NAME));

    final static List<String> defaultCauseCodeControllerEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.LABEL, GuiStringConstants.CAUSE_CODE, GuiStringConstants.FAILURES,
            GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.HANDOVER_TYPE));

    final static List<String> defaultDetailedCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.LABEL, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.FAILURES,
            GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.GROUP_NAME));

    final static List<String> defaultDetailedAccessAreaCauseCodeEventAnalysisWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.LABEL, GuiStringConstants.CAUSE_CODE, GuiStringConstants.FAILURES,
                    GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.TARGET_IMPACTED_SUBSCRIBERS,
                    GuiStringConstants.HANDOVER_TYPE));

    final static List<String> defaultDetailedControllerCauseCodeEventAnalysisWindow = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.LABEL, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.FAILURES,
                    GuiStringConstants.SOURCE_IMPACTED_SUBSCRIBERS, GuiStringConstants.HANDOVER_TYPE));

    final static List<String> defaultDetailedSOHOCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
            GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultDetailedIRATCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_BSC,
            GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultDetailedIFHOCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.PLMN_TARGET_ID, GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL,
            GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultDetailedHSDSCHCauseCodeEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
            GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL,
            GuiStringConstants.GBR_DL));

    final static List<String> defaultSourceCellEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultTerminalEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultEventTerminalAnalysisWindowHSDSCH = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultCauseCodeHOAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.SUB_CAUSE_CODE_ID, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultTargetCellCodeHOAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    static final List<RankingsTab.SubStartMenu> subMenusRankingAccessAreaTargetCell = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_WCDMAHFA,
                    RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_WCDMAHFA_RAN,
                    RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA,
                    RankingsTab.SubStartMenu.EVENT_RANKING_TARGET_CELL));

    static final List<RankingsTab.SubStartMenu> subMenusRankingCauseCodeWCDMA = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA_RAN,
                    RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_CAUSE_CODE_WCDMA_HANDOVER_TYPE));

    static final List<RankingsTab.SubStartMenu> subMenusRankingTerminalWCDMA = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_TERMINAL_RAN, RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_TERMINAL_WCDMA_HFA));

    static final List<RankingsTab.SubStartMenu> subMenusRankingRNC = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKINGS_RNC, RankingsTab.SubStartMenu.RANKINGS_RNC_RAN,
                    RankingsTab.SubStartMenu.RANKINGS_RNC_WCDMAHFA));

    static final List<RankingsTab.SubStartMenu> subMenusRankingSubscriberWCDMAHFAIratHandover = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_RAN,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA_IRAT_HANDOVER));

    static final List<RankingsTab.SubStartMenu> subMenusRankingSubscriberWCDMA_HFA_HSDSCH_HANDOVER = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_RAN,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA_HSDSCH_HANDOVER));

    static final List<RankingsTab.SubStartMenu> subMenusRankingSubscriberWCDMA_HFA_SOFT_HANDOVER = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_RAN,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA_SOFT_HANDOVER));

    static final List<RankingsTab.SubStartMenu> subMenusRANKING_SUBSCRIBER_WCDMA_HFA_IFHO_HANDOVER = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_PARENT,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_RAN,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA,
                    RankingsTab.SubStartMenu.RANKING_SUBSCRIBER_WCDMA_HFA_IFHO_HANDOVER));

    static final List<RankingsTab.SubStartMenu> subMenusRANKING_ACCESS_AREA_WCDMA_HFA = new ArrayList<RankingsTab.SubStartMenu>(
            Arrays.asList(RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_WCDMAHFA,
                    RankingsTab.SubStartMenu.RANKINGS_ACCESS_AREA_RAN,
                    RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA,
                    RankingsTab.SubStartMenu.RANKING_ACCESS_AREA_WCDMA_HFA));

    static final List<TerminalTab.SubStartMenu> subMenusTerminalRankingWCDMA = new ArrayList<TerminalTab.SubStartMenu>(
            Arrays.asList(TerminalTab.SubStartMenu.TERMINAL_RANKING_EVENT_MENU_ITEM_WCDMA,
                    TerminalTab.SubStartMenu.TERMINAL_RANKING_RAN_MENU_ITEM_WCDMA,
                    TerminalTab.SubStartMenu.TERMINAL_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    TerminalTab.SubStartMenu.TERMINAL_RANKING_HFA_MENU_ITEM_WCDMA));

    static final List<SubscriberTab.SubStartMenu> subMenusSUBSCRIBER_RANKING_WCDMA_HFA_IRAT_HANDOVER = new ArrayList<SubscriberTab.SubStartMenu>(
            Arrays.asList(SubscriberTab.SubStartMenu.EVENT_RANKING,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_HFA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_HFA_IRAT_HANDOVER));

    static final List<SubscriberTab.SubStartMenu> subMenusSUBSCRIBER_RANKING_WCDMA_HFA_SOFT_HANDOVER = new ArrayList<SubscriberTab.SubStartMenu>(
            Arrays.asList(SubscriberTab.SubStartMenu.EVENT_RANKING,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_HFA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_HFA_SOFT_HANDOVER));

    static final List<SubscriberTab.SubStartMenu> subMenusRankingWCDMAHFAIFHOHANDOVER = new ArrayList<SubscriberTab.SubStartMenu>(
            Arrays.asList(SubscriberTab.SubStartMenu.EVENT_RANKING,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_HFA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_HFA_IFHO_HANDOVER));

    static final List<NetworkTab.SubStartMenu> subMenusNETWORK_RANKING_HFA_MENU_ITEM_WCDMA = new ArrayList<NetworkTab.SubStartMenu>(
            Arrays.asList(NetworkTab.SubStartMenu.NETWORK_RANKING_EVENT_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_RNC_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_RNC_RAN_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_HFA_MENU_ITEM_WCDMA));

    static final List<NetworkTab.SubStartMenu> subMenusNETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA = new ArrayList<NetworkTab.SubStartMenu>(
            Arrays.asList(NetworkTab.SubStartMenu.NETWORK_RANKING_EVENT_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_RAN_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA));

    static final List<NetworkTab.SubStartMenu> subMenusNETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA = new ArrayList<NetworkTab.SubStartMenu>(
            Arrays.asList(NetworkTab.SubStartMenu.NETWORK_RANKING_EVENT_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_RNC_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_ACCESS_AREA_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_RAN_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_WCDMA_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA));

    static final List<NetworkTab.SubStartMenu> subMenusNETWORK_RANKING_HFA_TARGET_RANKING_MENU_ITEM_WCDMA = new ArrayList<NetworkTab.SubStartMenu>(
            Arrays.asList(NetworkTab.SubStartMenu.NETWORK_RANKING_EVENT_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_ACCESS_AREA_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_RAN_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_WCDMA_MENU_ITEM_WCDMA,
                    NetworkTab.SubStartMenu.NETWORK_RANKING_AA_TARGET_RANKING_MENU_ITEM_WCDMA));

    static final List<SubscriberTab.SubStartMenu> subMenusHFA_SubscriberRankingsByHSDSCH_Handover = new ArrayList<SubscriberTab.SubStartMenu>(
            Arrays.asList(SubscriberTab.SubStartMenu.EVENT_RANKING,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_HFA_MENU_ITEM_WCDMA,
                    SubscriberTab.SubStartMenu.SUBSCRIBER_RANKING_WCDMA_HFA_HSDSCH_HANDOVER));

    final static List<String> defaultEventFailureAnalysisWindowIRAT = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultEventFailureAnalysisWindowIFHO = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID, GuiStringConstants.LAC_TARGET_ID,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL,
            GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultEventFailureAnalysisWindowHSDSCH = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultTerminalFailureAnalysisWindowIFHO = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_BSC, GuiStringConstants.TARGET_LAC,
            GuiStringConstants.TARGET_PLMN, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultEventFailureAnalysisWindowSOHO = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME,/* GuiStringConstants.IMSI,*/GuiStringConstants.TAC,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    //     Sub Cause Value, Handover Type, SOURCE_CONNECTION_PROP, SOURCE_CONF,
    //    CPICH_EC_NO_EVAL_CELL, RSCP_EVAL_CELL, SRC_C_ID_1_SS_HSDSCH_CELL

    final static List<String> defaultSubscriber_Rankings_By_Soft_Handover = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
            GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));
    
    final static List<String> default_Rankings_By_Soft_Handover = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE, 
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
            GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));
    
    final static List<String> defaultRankingsBySoftHandoverDrillOnSourceCell = new ArrayList<String>(Arrays.asList(
    		GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.HANDOVER_TYPE,
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));
    
    final static List<String> defaultRankingsBySoftHandoverDrillOnTargetCell = new ArrayList<String>(Arrays.asList(
    		GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.TARGET_CELL, GuiStringConstants.HANDOVER_TYPE,
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    //swathi
    final static List<String> defaultSubscriber_Rankings_By_Inter_Frequency_Handover = new ArrayList<String>(
            Arrays.asList(GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
                    GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE,
                    GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER,
                    GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
                    GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
                    GuiStringConstants.PLMN_TARGET_ID, GuiStringConstants.LAC_TARGET_ID,
                    GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
                    GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
                    GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
                    GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
                    GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF,
                    GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.TARGET_CONNECTION_PROP,
                    GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.SEVERITY_INDICATOR,
                    GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriber_Rankings_By_IRAT_Handover = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_BSC, GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultSubscriber_Rankings_By_HSDSCH_Handover = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
            GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL,
            GuiStringConstants.GBR_DL));

    final static List<String> defaultEvent_Failure_Analysis_IRAT = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_BSC, GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultEvent_Failure_Analysis_IFHO = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL,
            GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultEvent_Failure_Analysis_HSDSCH = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL));

    final static List<String> defaultEvent_Failure_Analysis_SOHO = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
            GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    /*  ############################        SUBSCRIBER ANALYSIS TEST GROUP WINDOWS TAB CONSTANTS     ##################*/

    final static List<String> defaultHeadersOnSubscriberHFAWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES));

    final static List<String> completeHeadersOnSubscriberHFAWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.IMSI, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES));

    final static List<String> defaultHeadersOnSubscriberGroupHFAWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES));

    final static List<String> completeHeadersOnSubscriberGroupHFAWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.GROUP_NAME, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> HeadersOnSubscriberHSDSCH51 = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
            GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER));

    final static List<String> defaultGroupSubscriberWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    /*  ############################        TERMINAL ANALYSIS TEST GROUP WINDOWS TAB CONSTANTS     ##################*/

    final static List<String> defaultTerminalAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.GROUP_NAME, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultTerminalAnalysisWindow_ = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RANK, GuiStringConstants.TAC, GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL,
            GuiStringConstants.FAILURES));

    final static List<String> defaultEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultGroupEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));

    final static List<String> defaultTerminalEventAnalysiWCDMASOHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, /*GuiStringConstants.IMSI,*/GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC,
            GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_EVAL_CELL,
            GuiStringConstants.RSCP_EVAL_CELL, GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultTerminalEventAnalysiWCDMAIFHOWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.PLMN_TARGET_ID,
            GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE,
            GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultTerminalEventAnalysiWCDMAIRATWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.IMSI, GuiStringConstants.EVENT_TYPE,
            GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE,
            GuiStringConstants.EXCEPTION_CLASS, GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL));

    final static List<String> defaultTerminalEventAnalysiWCDMAHSDSCHWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.EVENT_TIME, GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.CAUSE_VALUE, GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NO_TARGET_CELL, GuiStringConstants.RSCP_TARGET_CELL,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.UARFCN_SOURCE,
            GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1, GuiStringConstants.GBR_UL,
            GuiStringConstants.GBR_DL));

    final static List<String> defaultAccesAreaEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));
    
    final static List<String> defaultTargetAccesAreaEventAnalysisWindow = new ArrayList<String>(Arrays.asList(
            GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER, GuiStringConstants.TARGET_CELL,
            GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS));
    
    
    final List<String> defaultFailedEventSOHOAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.HANDOVER_TYPE,
            GuiStringConstants.SOURCE_CONNECTION_PROP, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_EVAL_CELL, GuiStringConstants.RSCP_EVAL_CELL,
            GuiStringConstants.SRC_C_ID_1_SS_HSDSCH_CELL);

    final List<String> defaultFailedEventIFHOAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC,
            GuiStringConstants.PLMN_TARGET_ID, GuiStringConstants.LAC_TARGET_ID, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL,
            GuiStringConstants.CPICH_EC_NU_FREQ_TARGET_CELL, GuiStringConstants.RSCP_NU_FREQ_TARGET_CELL,
            GuiStringConstants.SOURCE_CONF, GuiStringConstants.TARGET_CONF, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.TARGET_CONNECTION_PROP, GuiStringConstants.C_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_1_SS_HSDSCH_CELL);

    final List<String> defaultFailedEventIRATAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.PROCEDURE_INDICATOR, GuiStringConstants.EVENT_TRIGGER,
            GuiStringConstants.SOURCE_CELL, GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC,
            GuiStringConstants.SOURCE_RAC, GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_BSC,
            GuiStringConstants.TARGET_LAC, GuiStringConstants.TARGET_PLMN, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.SUB_CAUSE_VALUE, GuiStringConstants.EVALUATION_CASE, GuiStringConstants.EXCEPTION_CLASS,
            GuiStringConstants.SEVERITY_INDICATOR, GuiStringConstants.SOURCE_CONF,
            GuiStringConstants.CPICH_EC_NO_SOURCE_CELL, GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.RSSI,
            GuiStringConstants.TARGET_CONF, GuiStringConstants.CRNC_ID_SERV_HSDSCH_CELL,
            GuiStringConstants.C_ID_SERV_HSDSCH_CELL, GuiStringConstants.C_ID_1_SS_HSDSCH_CELL);

    final List<String> defaultFailedEventHSDSCHAnalysisColumns = Arrays.asList(GuiStringConstants.EVENT_TIME,
            GuiStringConstants.IMSI, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL,
            GuiStringConstants.EVENT_TYPE, GuiStringConstants.EVENT_TRIGGER, GuiStringConstants.SOURCE_CELL,
            GuiStringConstants.SOURCE_RNC, GuiStringConstants.SOURCE_LAC, GuiStringConstants.SOURCE_RAC,
            GuiStringConstants.TARGET_CELL, GuiStringConstants.TARGET_RNC, GuiStringConstants.CAUSE_VALUE,
            GuiStringConstants.PATHLOSS, GuiStringConstants.SOURCE_CONF, GuiStringConstants.CPICH_EC_NO_SOURCE_CELL,
            GuiStringConstants.RSCP_SOURCE_CELL, GuiStringConstants.CPICH_EC_NO_TARGET_CELL,
            GuiStringConstants.RSCP_TARGET_CELL, GuiStringConstants.SOURCE_CONNECTION_PROP,
            GuiStringConstants.UARFCN_SOURCE, GuiStringConstants.UARFCN_TARGET, GuiStringConstants.UL_SYNC_STATUS_RLS1,
            GuiStringConstants.GBR_UL, GuiStringConstants.GBR_DL);

    final List<String> defaultTerminalAnalysisColumns = Arrays.asList(GuiStringConstants.RANK, GuiStringConstants.TAC,
            GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL, GuiStringConstants.FAILURES);

    final List<String> defaultTerminalEventAnalysisColumns = Arrays.asList(GuiStringConstants.MANUFACTURER,
            GuiStringConstants.MODEL, GuiStringConstants.HANDOVER_TYPE, GuiStringConstants.FAILURES,
            GuiStringConstants.IMPACTED_SUBSCRIBERS);
    /*** added for new GUI ****/
    //-------------------------NetworkTab----------------------------------------------
    public static final String Controller_Event_Analysis = "BSC1";

    //public static final String RNC01 = "ONRM_ROOT_MO_R:RNC01:RNC01";
    public static final String RNC01 = "RNC01";

    public static final String RNC09 = "RNC09";

    //---Table Headers---
    public static final String WCDMA_Handover_Failure_Cause_Code_Analysis = "AutomationControllerGroupTest - Controller Group - WCDMA Handover Failure Cause Code Analysis";

    public static final String WCDMA_Controller_Ranking = "RNC WCDMA Call Failure Event Ranking";

    public static final String WCDMA_AccessArea_Ranking = "Access Area WCDMA Call Failure Event Ranking";

    //---Dimension Filter---
    public static final String Call_Failure_GSM = "Call Failure (GSM)";

    public static final String Call_Failure_WCDMA = "Call Failure (3G)";

    public static final String Handover_Failure_WCDMA = "Handover Failure (3G)";

    public static final String Ranking_CFA_Controller = "Call Failure by Controller";

    public static final String Ranking_CFA_Terminal = "Call Failure by Terminal";

    public static final String Ranking_CFA_AccessArea = "Call Failure by Access Area";

    public static final String Ranking_CFA_CallDrops = "Call Drops";

    public static final String Ranking_CFA_SetupFailure = "Call Setup Failure";

    public static final String Ranking_CFA_RecuringFailures = "Recurring Failures";

    public static final String Network_Event_Analysis = "Network Event Analysis";

    public static final String Call_Failure = "Call Failure";

    /*** subscriber **/
    public static final String Handover_Failure = "Handover Failure";

    public static final String Handover_Failure_3G = "Handover Failure (3G)";

    public static final String Event_Trace_3G = "3G Event Trace";

    public static final String Call_Drops = "Call Drops";

    public static final String Call_Setup_Failure = "Call Setup Failure";

    /*** terminal **/
    public final String MOST_SOFT_HANDOVER_FAILURES_SUMMARY = "Most Soft Handover Failures Summary";

    public final String MOST_INTER_FREQUENCY_HANDOVER_FAILURES = "Most Inter Frequency Handover Failures";

    public final String MOST_IRAT_HANDOVER_FAILURES = "Most IRAT Handover Failures";

    public final String MOST_HSDSCH_HANDOVER_FAILURE = "Most HSDSCH Handover Failures";

    //---Analysis Window Subsets---
    public static final String Ranking_3G = "3G Ranking";

    public static final String Subscriber_Overview = "Subscriber Overview";

    public static final String CauseCode_Ranking_3G = "3G Cause Code Ranking";

    public static final String Terminal_Group_Analysis_3G = "3G Terminal Group Analysis";

    public static final String Terminal_Analysis_3G = "3G Terminal Analysis";

    public static final String Terminal_Event_Analysis_3G = "3G Terminal Event Analysis";

    //---Drill Down options
    public final static String CONTROLLER_Summary = "Controller Summary";

    public final static String Cell_Summary = "Cell Summary";

    //---X-Paths---
    // public static final String DrillByOptionsMenu = "//td[@class='dialogMiddleCenter']/div[@class='dialogMiddleCenterInner dialogContent']/div[@class='GKH1R2KETB center']/div[@class='GKH1R2KATB']";
    public static final String DrillByOptionsMenu = "//div[@class='GKH1R2KATB']";

    public static final String DrillByOptionsCellSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Cell Summary')]";

    public static final String DrillByControllerSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Controller Summary')]";

    public static final String DrillByTerminalSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Terminal Summary')]";

    //-------------Ranking(WCDMA-CFA)------------------------------------------
    public static final String WcdmaRanCfaCellDrillBy = "WCDMA_RAN_CFA_BSC_DRILL_BY";

    public static final String NetworkEventAnalysisDrillOnFailureByBscCallSetup = "NETWORK_EVENT_ANALYSIS_DRILL_ON_FAILURE_BY_BSC_CALL_SETUP";

    //-------------Ranking(WCDMA-HFA)----swathi-------------------------------
    public static final String Handover_Failure_By_Controller = "Handover Failure by Controller";

    public static final String Handover_Failure_By_Source_Cell = "Handover Failure by Source Cell";

    public static final String Handover_Failure_By_Target_Cell = "Handover Failure by Target Cell";

    public static final String Handover_Failure_By_Terminal = "Handover Failure by Terminal";

    public static final String Soft_Handover = "Soft Handover";

    public static final String Inter_Frequency_Handover = "Inter Frequency Handover";

    public static final String IRAT_Handover = "IRAT Handover";

    public static final String HSDSCH_Handover = "HSDSCH Handover";
    
    public static final String Recurring_Failures = "Recurring Failures";


    /***** WCDMA-HFA Ranking Test Group Titles ***/

    public static final String Access_Area_WCDMA_Handover_Failure_by_Source_Cell = "Access Area WCDMA Handover Failure by Source Cell Event Ranking";

    public static final String Access_Area_WCDMA_Handover_Failure_by_Target_Cell = "Access Area WCDMA Handover Failure by Target Cell Event Ranking";

    public static final String Access_Area_Event_Analysis_Source = "Access Area Event Analysis - Source";

    public static final String Access_Area_Event_Analysis_Target = "Access Area Event Analysis - Target";

    public static final String Failed_Event_Analysis = "Failed Event Analysis";

    public static final String Cause_Code_WCDMA_Handover_Failure_Event_Ranking = "Cause Code WCDMA Handover Failure Event Ranking";

    public static final String Sub_Cause_Code_Analysis_Handover_Failure = "Sub Cause Code Analysis for WCDMA Handover Failure";

    public static final String Terminal_WCDMA_Handover_Failure_Event_Ranking = "Terminal WCDMA Handover Failure Event Ranking";

    public static final String Terminal_Analysis_Most_Soft_Handover_Failures = "Terminal Analysis - Most Soft Handover Failures";

    public static final String Terminal_WCDMA_Analysis_Handover_Failure_Analysis = "Terminal WCDMA Analysis - Handover Failure Analysis - WCDMA";

    public static final String Terminal_Event_Analysis_Handover_Failure_Analysis = "Terminal Event Analysis - Handover Failure Analysis - WCDMA";

    public static final String Subscriber_WCDMA_Handover_Failure_by_SOHO = "Subscriber WCDMA Handover Failure by Soft Handover Event Ranking";

    public static final String Subscriber_WCDMA_Handover_Failure_by_IFHO = "Subscriber WCDMA Handover Failure by Inter Frequency Handover Event Ranking";
    
    public static final String Subscriber_WCDMA_Handover_Failure_by_IRAT = "Subscriber WCDMA Handover Failure By IRAT Handover Event Ranking";
    
    public static final String Subscriber_WCDMA_Handover_Failure_by_HSDSCH = "Subscriber WCDMA Handover Failure by HSDSCH Handover Event Ranking";
    
    public static final String Subscriber_WCDMA_Handover_Failure_by_Recurring_Failures = "Subscriber WCDMA Call Failure by Recurring Failure Event Ranking";

    public static final String IMSI_Event_Analysis_For_Recurring_Failures = "IMSI Event Analysis for Recurring Failures";
    
    //-------------Subscriber----------------------------------------------------------

    //-----------------------------------------------------------------------
    public static final String CORE_PS = "Core PS";

    public static final String NETWORK_EVENT_ANALYSIS = "Network Event Analysis";

    public static final String ROAMING_BY_OPERATOR = "Roaming by Operator";

    public static final String ROAMING_BY_COUNTRY = "Roaming by Country";

    public static final String APN_SEARCH_VALUE = "blackberry.net";

    public static final String APN_SEARCH_VALUE_1 = "blackberry.1x.bell.ca";

    public static final String APN_SEARCH_VALUE_2 = "blackberry.net.mnc000.mcc460";

    public static final String APN_SEARCH_VALUE_3 = "blackberry.net.mnc001.mcc208";

    public static final String APN_SEARCH_VALUE_4 = "blackberry.net.mnc001.mcc214";

    //      public static final String CONTROLLER_SEARCH_VALUE = "BSG1,Ericsson,GSM";

    public static final String CONTROLLER_SEARCH_VALUE = "BSC1,Ericsson,GSM";

    public static final String ACCESS_AREA_GROUP_MSS_SEARCH_VALUE = "DG_GroupNameAPN_1";

    public static final String CONTROLLER_GROUP_MSS_SEARCH_VALUE = "DG_GroupNameRATVENDHIER3_1";

    public static final String TERMINAL_MAKE_SEARCH_VALUE = "Blaupunkt-Werke GmbH";

    public static final String TERMINAL_SEARCH_VALUE = "Blaupunkt M 242,45000330";

    public static final String IMSI_SEARCH_VALUE = "460000987654322";

    public static final String SGSN_SEARCH_VALUE = "MME1";

    public static final String SGSN_SEARCH_VALUE2 = "MME2";

    public static final String APN_GROUP_SEARCH_VALUE = "DG_GroupNameAPN_1";

    public static final String SGSN_MME_GROUP_SEARCH_VALUE = "DG_GroupNameEVENTSRC_250";

    public static final String DASHBBOARD_APN_VALUE = "blackberry.net";

    //Error Messages
    public static final String CHECKBOX_ALREADY_CHECKED = "Multiple check box is already checked";

    public static final String CHECKBOX_CHECKED_NOW = "Multiple Check box is checked Now";

    public static final String CHECKBOX_YET_CHECKED = "Multiple check box is Yet checked";

    //public static final String CHECKBOX_UNCHECKED = "Multiple check box is unchecked";

    public static final String SINGLE_TO_MULTIPLE_INSTANCE = "Single to Multiple Instance Change Confirmation";

    public static final String MULTIPLE_TO_SINGLE_INSTANCE = "Multiple Instance to Single Instance Change Confirmation";

    public static final String OK_BUTTON = "On Hitting OK Closed Searchable Only Window";

    public static final String CANCEL_BUTTON = "On Hitting CANCEL confirmation window is closed automatically for Data";

    public static final String EVENT_ANALYSIS = "Event Analysis Window Launched";

    public static final String EVENT_VOLUME = "Event Volume";

    public static final String DATA_VOLUME = "Data Volume";

    public static final String NETWORK_EVENT_VOLUME = "Network Event Volume Launched";

    public static final String NETWORK_DATA_VOLUME = "Network Data Volume Launched";

    public static final String CAUSE_CODE_ANALYSIS = "Cause Code Analysis";

    public static final String APPROPRIATE_MESSAGE = "Displays an appropriate message when there is no input selected on the tool bar";

    public static final String CHECKED_UNCHECKED = "Only One can be Checked and Unchecked at a time";

    public static final String AUTOMATICALLY_CHECKED = "CC types are automatically checked in the Advanced Reconfigure menu";

    public static final String PIE_CHART_APN = "Pie chart of APN launched";

    public static final String PIE_CHART_CONTROLLER = "Pie chart of CONTROLLER launched";

    public static final String PIE_CHART_SGSN = "Pie chart of SGSN launched";

    public static final String PIE_CHART_ACESS_AREA = "Pie chart of ACESS AREA launched";

    public static final String PIE_CHART_APN_GROUP = "Pie chart of APN Group launched";

    public static final String PIE_CHART_CONTROLLER_GROUP = "Pie chart of Controller Group launched";

    public static final String PIE_CHART_SGSN_MME_GROUP = "Pie chart of SGSN MMEM Group launched";

    public static final String PIE_CHART_ACCESS_AREA_GROUP = "Pie chart of Access Area Group Slaunched";

    public static final String GRID_VIEW_APN = "Traditional Grid View and subsequently launches a Cause Code Analysis window for APN";

    public static final String GRID_VIEW_CONTROLLER = "Traditional Grid View and subsequently launches a Cause Code Analysis window for CONTROLLER ";

    public static final String GRID_VIEW_SGSN = "Traditional Grid View and subsequently launches a Cause Code Analysis window for SGSN ";

    public static final String GRID_VIEW_ACCESS_AREA = "Traditional Grid View and subsequently launches a Cause Code Analysis window for ACCESS AREA ";

    public static final String GRID_VIEW_APN_GROUP = "Traditional Grid View and subsequently launches a Cause Code Analysis window for APN GROUP";

    public static final String GRID_VIEW_CONTROLLER_GROUP = "Traditional Grid View and subsequently launches a Cause Code Analysis window for CONTROLLER GROUP";

    public static final String GRID_VIEW_SGSN_GROUP = "Traditional Grid View and subsequently launches a Cause Code Analysis window for SGSN GROUP";

    public static final String GRID_VIEW_ACCESS_AREA_GROUP = "Traditional Grid View and subsequently launches a Cause Code Analysis window for ACCESS AREA GROUP";

    public static final String COLUMN_STRUCTURE_VISIBLE = "New Column Structure Visible and in line with Design Template";

    public static final String NEW_COLUMN_STRUCTURE = "The New Column Structure Not Vissible";

    public static final String BY_DEFAULT_COLUMNS_SELECTED = "By Default Some Columns Selected on Loading in the New Column Structure";

    public static final String ALL_BUTTONS_OPTIONS = "New Cause Code Analysis GUI with All Buttons and Options are Present";

    public static final String CONFIGURE_MENU = "New Cause Code Analysis GUI are Working and Showing All the Necessary Check Boxes";

    public static final String NEW_HEADERS_CHECKED = "New Headers Are Added";

    public static final String NEW_HEADERS_UNCHECKED = "New Headers Are Removed";

    //Logs Info

    public static final String MULTIPLE_CHECKBOX = " Multiple check box is present for all Tabs";

    public static final String UNCHECKED_DEFAULT = "Multiple checkbox is unchecked by default for data";

    //Dashboard Error Messages

    public static final String DASHBOARD_TAB = "Dashboard Tab Titled is not Present";

    public static final String NODE_TYPES = "There are no sub tabs to select the node types";

    public static final String GROUP_NODE_TYPES = "There are no sub tabs to select the group node types";

    public static final String DEFAULT_PORTLETS = "Default test portlets are not in line with dashboard framework ";

    public static final String PLUS_ON_PORTLETS = "'+'cannot be seen in the centre of the portlet";

    public static final String X_ICON = "Portlets dont have 'X' icon";

    public static final String SETTINGS_ICON = "Portlets dont have settings icon";

    public static final String REPLACE_BUTTON = "Replace button was not found";

    public static final String CLOSEWIDGET_BUTTON = "Close widget button was not found";

    public static final String PORTLET_CLOSED_STATE = "portlet is not in closed state";

    public static final String DASHBOARD_COMBOBOX_BUTTON = "Select input ComoboBox was not found in the Dashboard Tab";

    public static final String DASHBOARD_TIMESELECT_COMOBOBOX = "Time Select input ComoboBox was not found in the Dashboard Tab";

    public static final String DATE_DIALOG_COMBOBOX = "Calender style dialog date comboBox is not present";

    public static final String BUSINNESS_OBJECTS_TAB = "Business Objects tab is not present";

    public static final String BY_DEFAULT_RADIO_NETWORK_SELECTED = "By Default Radio Network is Not Selected";

    //MultiInstance Windows Failure Reasons
    public static final String SEARCHABLE_OPENED_WINDOWS = "Searchable opened window for data is not closed";

    public static final String VERIFY_CHECKBOX = " Multiple check box is not present for all Tabs";

    public static final String CHECKBOX_UNCHECKED_DEFAULT = "Multiple checkbox is not unchecked by default for data";

    public static final String CHECKBOX_NOT_CHECKED = "Multiple Check box is not checked";

    public static final String CHECKBOX_CHECKED = "Multiple Check box is checked";

    public static final String SINGLE_TO_MULTIPLE_WINDOW = "Single Instance to Multiple Instance Change Confirmation window Does not Pop up";

    public static final String MULTIPLE_TO_SINGLE_WINDOW = "Multiple Instance to single  Instance Change Confirmation window Does not Pop up";

    public static final String SUB_OVERVIEW_WINDOW_TITLE = "WCDMA Call Failure Subscriber Overview";

    public static final String TRUE = "true";

    static final String GRID_CELL_LINK = GuiStringConstants.GRID_CELL_LINK;

}
