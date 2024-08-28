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

public class WcdmaStingConstantsNewUI {

    //-------------------------NetworkTab----------------------------------------------
    public static final String Controller_Event_Analysis = "BSC1";

    //public static final String RNC01 = "ONRM_ROOT_MO_R:RNC01:RNC01";
    public static final String RNC09 = "RNC09";

    //---Table Headers---
    public static final String WCDMA_Controller_Ranking = "3G Radio Network - Ranking - Call Failure by Controller > Multi RAB Failures";

    public static final String WCDMA_AccessArea_Ranking = "Access Area WCDMA Call Failure Event Ranking";

    public static final String WCDMA_CFA_Failed_Event_Analysis = "Failed Event Analysis - Call Failure Analysis - WCDMA";

    public static final String RANKING_TABLE_AA_TITLE = "3G Radio Network - Ranking - Call Failure by Access Area > Total RAB Failures";

    public static final String EVENT_ANALYSIS_TABLE_TITLE = " - Access Area Event Analysis > Total RAB Failures";

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

    //---Analysis Window Subsets---
    public static final String Ranking_3G = "3G Ranking";

    public static final String Subscriber_Overview = "Subscriber Overview";

    public static final String CauseCode_Ranking_3G = "3G Cause Code Ranking";

    public static final String DisconnectionCode_Ranking_3G = "3G Disconnection Code Ranking";

    public static final String Terminal_Group_Analysis_3G = "3G Terminal Group Analysis";

    public static final String Terminal_Analysis_3G = "3G Terminal Analysis";

    public static final String TERMINAL_EVENT_ANALYSIS_3G = "3G Terminal Event Analysis";

    public static final String Event_Trace_3G = "3G Event Trace";

    //---Drill Down options
    public final static String CONTROLLER_Summary = "Controller Summary";

    public final static String Cell_Summary = "Cell Summary";

    //---X-Paths---
    // public static final String DrillByOptionsMenu = "//td[@class='dialogMiddleCenter']/div[@class='dialogMiddleCenterInner dialogContent']/div[@class='GKH1R2KETB center']/div[@class='GKH1R2KATB']";
    public static final String DrillByOptionsMenu = "//div[@class='GKH1R2KATB']";

    public static final String DrillByOptionsCellSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Cell Summary')]";

    public static final String DrillOnEventAnalysisSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//div[@id='selenium_tag_DrillDialog_link' and text()='Event Analysis Summary']";

    public static final String DrillByTerminalSummary = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Terminal Summary')]";

    public static final String DrillByDetailedEventAnalysis = "//div[@class='dialogMiddleCenterInner dialogContent']//div[@id='selenium_tag_DrillDialog_link' and text()='Detailed Event Analysis']";

    public static final String DrillByDisconnectionCode = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'Disconnection Code')]";

    public static final String DrillByRabType = "//div[@class='dialogMiddleCenterInner dialogContent']//tbody/tr/td/div/div[contains(text(), 'RAB Type')]";

    public static final String DrillByFailures = "//span[@id='WCDMA_RAN_CFA_BSC_DRILL_BY_DROPS_CONFIG_ALL']";

    public static final String DrillByDisconnCode = "//div[@id='selenium_tag_DrillDialog_link'][1]";

    public static final String ToggleToGrid = "//*[@id='btnToggleToGrid']/tbody/tr[2]/td[2]/em/button/img";

    public static final String ExportToCsv = "//*[@id='btnExport']/img";

    public static final String ClickRabDescriptionAnalysisWindow = "//node()[name()='tspan' and text()='RAB Description Analysis (WCDMA Call Failure)']";

    public static final String ClickRabTypeAnalysisWindow = "//node()[name()='tspan' and text()='RAB Type Analysis (WCDMA Call Failure)']";

    public static final String Access_Area_Toggle = "//*[@id='btnToggleToGrid']/tbody/tr[2]/td[2]/em/button/img";

    public static final String Access_Area_Export = "//td[@class='x-toolbar-cell'][4]/div[@id='btnExport']/img[@class='gwt-Image']/@src";

    public static final String Ranking_Terminal_Toggle = "//*[@id='btnToggleToGrid']/tbody/tr[2]/td[2]/em/button/img";

   public static final String WCDMA_Sub_Cause_Code_Button="//*[@id='btnSCC']/img";


    //-------------Ranking----------------------------------------------------------
    public static final String WcdmaRanCfaCellDrillBy = "WCDMA_RAN_CFA_BSC_DRILL_BY";

    public static final String NetworkEventAnalysisDrillOnFailureByBscCallSetup = "NETWORK_EVENT_ANALYSIS_DRILL_ON_FAILURE_BY_BSC_CALL_SETUP";

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
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

    //    public static final String CONTROLLER_SEARCH_VALUE = "BSG1,Ericsson,GSM";

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

    static final String CFA_Title = "//div[@id='x-auto-34']/div/div[2]/span/text()";
    static final String CFA_Call_Drops_Radiobutton = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[2]/td/span/input/";
    static final String CFA_Call_Setup_Failures_Radiobutton = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[3]/td/span/input/";
    // static final String CFA_Launch_Button = "//div[@id='x-auto-34']//button[@class='gwt-Button GC0MAX4BNQB']";
    static final String CFA_Launch_Button = "//div[@id='x-auto-34']//button[@class='gwt-Button GC0MAX4BOQB']";
    static final String CFA_Dropdown = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div/table";
    static final String CFA_Dropdown_Option1 = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div[1]/div/div/div[1]";
    static final String CFA_Dropdown_Option2 = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div[1]/div/div/div[2]";
    static final String CFA_Dropdown_Option3 = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div[1]/div/div/div[3]";
    static final String CFA_Dropdown_Option4 = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div[1]/div/div/div[4]";
    static final String CFA_Dropdown_Button = "//div[@id='x-auto-34']/div[2]/div/div[1]/div/table/tbody/tr/td[1]/table/tbody/tr[1]/td/div/div/table/tbody/tr/td[2]/div";
    static final String Ranking_Table_Title = "//div[@id='x-auto-31']/div[2]/div[1]/div/div[1]/div/div/div/span";
    static final String Event_Analysis_Table_Title = "//div[@id='x-auto-31']/div[2]/div[1]/div[2]/div[1]/div/div/div/span";
    static final String Detailed_Event_Analysis = "//div[@class='dialogMiddleCenterInner dialogContent']//div[@class='gwt-HTML'][3]";
    static final String Disconnection_Code = "//div[@class='dialogMiddleCenterInner dialogContent']//div[@class='gwt-HTML'][1]";
    static final String RAB_Type = "//div[@class='dialogMiddleCenterInner dialogContent']//div[@class='gwt-HTML'][2]";
    static final String tableRowsXPath = "//div[@class='x-grid3-body']/div[contains(@id,'RAN_CFA')]";
    static final String headerXpath = "//table[@id='x-auto-177']";
    static final String Controller_Table_Title = "//div[@id='x-auto-31']/div[2]/div[1]/div[2]/div[1]/div/div/div/span";
    static final String Failures_link = "//*[@class='x-grid3-body']/div/table/tbody/tr/td[4]/div/div/span";
    static final String CD_Chart_Title = "//div[@id='x-auto-201']/span";
    static final String RAB_Chart_Title = "//div[@id='x-auto-201']/span";
    static final String DEA_Chart_Title = "//div[@id='x-auto-201']/span";
    static final String CD_AA_Chart_Title = "//div[@id='x-auto-471']/span";
    static final String RAB_AA_Chart_Title = "//div[@id='x-auto-471']/span";
    static final String DEA_AA_Chart_Title = "//div[@id='x-auto-542']/span";
    static final String Call_Drops_Text = "//*[@id='x-grid3-body']/div/table/tbody/tr/td[4]/div";
    static final String Event_Type = "//div[@class='x-grid3-body']/div[contains(@id,'RAN_CFA')][1]//table.0.2";
    static final String Event_Type_AA = "//div[@class='x-grid3-body']/div[contains(@id,'NETWORK_CELL_RANKING_RAN_WCDMA_CFA')][1]//table.0.4";
    static final String AccessArea = "//div[@class='x-grid3-body']/div[contains(@id,'NETWORK_CELL_RANKING_RAN_WCDMA_CFA')][1]//table.0.2";
    static final String Controller_Name = "//div[@class='x-grid3-body']/div/table/tbody/tr/td[2]/div/div/span";
    static final String TOTAL_RAB_FAILURE = "Total RAB Failures";
    static final String CIRCUIT_SWITCHED_FAILURE = "Circuit Switched RAB Failures";
    static final String PACKET_SWITCHED_FAILURE = "Packet Switched RAB Failures";
    static final String MULTI_RAB_FAILURE = "Multi RAB Failures";
    static final String RANKING_TABLE_TITLE = "3G Radio Network - Ranking - Call Failure by Controller > Total RAB Failures";
    static final String CALL_FAILURE_ANALYSIS_BY_ACCESS_AREA = "Call Failure Analysis : By Access Area";
    static final String CALL_FAILURE_ANALYSIS_BY_CONTROLLER = "Call Failure Analysis : By Controller";
    static final String CONTROLLER = "Controller";
    static final String NEATableTitle = " - Network Event Analysis > Total RAB Failures";
    static final String RAN_VENDOR = "RAN Vendor";
    static final String EVENT_TYPE = "Event Type";
    static final String FAILURES = "Failures";
    static final String IMPACTED_SUBSCRIBERS = "Impacted Subscribers";
    static final String RAB_FAILURE_RATIO = "RAB Failure Ratio (%)";
    static final String CONTROLLER_DISCONNECTIONS = "Call Drops - Controller Disconnections > Total RAB Failures";
    static final String ACCESS_AREA_DISCONNECTIONS = " - Access Area Disconnections > Total RAB Failures";
    static final String CALL_SETUP_FAILURES = "Call Setup Failures";
    static final String CALL_DROPS = "Call Drops";
    static final String DEAWindowTitle = " - Failed Event Analysis - WCDMA Call Failure > Total RAB Failures";
    static final String ControllerRABTitle = "Call Drops - Controller RAB Type > Total RAB Failures";
    static final String AccessAreaRABTitle = " - Cell RAB Type > Total RAB Failures";
    static final String CALL_FAILURE_TITLE = "Call Failure Analysis";
    static final String ACCESS_AREA_POPUP_TITLE = "//div[@id='x-auto-42']/div[@class='x-window-tl']/div[@class='x-window-tr']/div[@class='x-window-tc']/div[@id='x-auto-41']";
}
