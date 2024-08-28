package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common;

/**
 * 
 * @author ekumpen
 * August 2012
 *
 */

public class Constants {

    //BUTTONS
    public static final String SUMMARY_BUTTON = "//div[@id='SESSION_BROWSER_TAB']//table//tbody//tr//td[text()='Summary']";

    public static final String DETAILS_BUTTON = "//div[@id='SESSION_BROWSER_TAB']//table//tbody//tr//td[text()='Details']";

    //LAUNCH MENU XPATHS
    public static final String HIDE_LAUNCHER_MENU = "//div[@id='SESSION_BROWSER_TAB']//img[contains(@title,'hide the launcher')]";

    public static final String SHOW_LAUNCHER_MENU = "//div[@id='SESSION_BROWSER_TAB']//img[contains(@title,'show Launcher')]";

    public static final String IMSI_INPUT_TEXTFIELD = "//div[@id='SESSION_BROWSER_TAB']//input[contains(@class,'component ') and contains(@type,'text')]";
    //There is no longer IMSI and MSISDN buttons
//    public static final String IMSI_BUTTON = "//div[contains(@id,'SESSION_BROWSER')]//table//tbody//tr//td[text()='IMSI']";
//
//    public static final String MSISDN_BUTTON = "//div[contains(@id,'SESSION_BROWSER')]//table//tbody//tr//td[text()='MSISDN']";

    public static final String UPDATE_BUTTON = "//div[contains(@id,'SESSION_BROWSER')]//div[@class='updateButtonHolder']//button[contains(@class,'update')]";

    public static final String CORE_SIGNALLING_BUTTON = "//div[contains(@id,'SESSION_BROWSER')]//div[@class='bottomPanel']/table[2]/tbody/tr/td[2]/div";

    public static final String RAN_SIGNALLING_BUTTON = "//div[contains(@id,'SESSION_BROWSER')]//div[@class='bottomPanel']/table[3]/tbody/tr/td[2]/div";

    public static final String PERIOD_DROP_DOWN = "//div[@id='SESSION_BROWSER_TAB']//div[2]//table//div[contains(@class,'timeDropDownMenu')]/div/table/tbody/tr/td[2]";

    //SESSION LIST - DETAILS VIEW
    public static final String SESSION_LIST = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[3]";

    public static final String DROPDOWN_CUSTOM_OPTION = "//div[@class='popupContent']//div/div[@__idx='8']";

    public static final String CORE_DATA_SIGNALLING = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td";

    public static final String DBS_SESSION_LIST = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td[text()='Data Bearer Session']";

    public static final String SESSION_LIST_HEADER = "//div[contains(@id,'SESSION_BROWSER')]//div[@class='gwt-Label']";

    public static final String VIEW_DETAILS_WINDOW = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]";

    public static final String DROPDOWN_MENU_LINK = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr[2]/td[2]";

    public static final String DROPDOWN_MENU_CONTENT = "//html/body//div[@class='popupContent']/div/div";

    public static final String VIEW_DETAILS_WINDOW_MAXIMIZE = "//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[1]/tbody/tr/td[4]/img";

    public static final String VIEW_DETAILS_WINDOW_CLOSE = "//div[@id='SESSION_BROWSER_TAB']//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[1]/tbody/tr/td[5]";

    public static final String VIEW_DETAILS = "//div[@class='popupContent']/div/div[2][text()='View Details']";

    public static final String SERVER_DISTRIBUTION = "//div[@class='popupContent']/div/div[3][text()='Server Distribution']";

    public static final String REPORTS = "//div[@class='popupContent']/div/div[1][text()='Reports']";

    public static final String VIEW_DETAILS_SCROLLBAR = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[2]/div/div";

    public static final String VIEW_DETAILS_CONTENTS = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]";

    public static final String COLLAPSABLE_HEADINGS = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]/div/div[1]/div/div[1]";

    public static final String RAN_DETAILS_SESSION_PROPERTIES = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[3]/div[2]/table/tbody/tr/td[1]/div[1]";

    public static final String RAN_DETAILS_NETWORK_LOCATION = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[2]/div[2]/table/tbody/tr/td[1]/div[1]";

    public static final String RAN_DETAILS_RADIO_CONDITIONS = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[4]/div[2]/table/tbody/tr/td[1]/div[1]";

    public static final String RAN_DETAILS_MOBILITY_CONDITIONS = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[5]/div[2]/table/tbody/tr/td[1]/div[1]";

    public static final String RAN_DETAILS_TRAFFIC_CHANNEL_USAGE = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div[@class='wrapper']/div[2]/div[1]//div[1]/div[6]/div[2]/table/tbody/tr/td[1]/div[1]";

    public static final String SESSION_TABLE = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr/td[3]";
    
    public static final String PRINT_PREVIEW_WINDOW_CLOSE = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[1]/tbody/tr/td[5]/div";

    //RADIO AND CELL LOAD CONDITIONS

    public static final String RADIO_AND_CELL_LOAD_CONDITIONS_AREA_GRAPH = "//div[@id='BOUNDARY_SESSION_BROWSER']//div[2]//div[4][starts-with(@id,'Master_')]//div[starts-with(@id,'highcharts-')]//node()[@class='highcharts-series-group']";

    public static final String RADIO_AND_CELL_LOAD_CONDITIONS_EXPORT_BUTTON = "//div[@id='BOUNDARY_SESSION_BROWSER']//div[2]//div[3][starts-with(@id,'Detail_')]//node()[@id='exportButton']";

    public static final String RADIO_AND_CELL_LOAD_CONDITIONS_PRINT_BUTTON = "//div[@id='BOUNDARY_SESSION_BROWSER']//div[2]//div[3][starts-with(@id,'Detail_')]//node()[@id='printButton']";

    public static final String RADIO_AND_CELL_LOAD_CONDITIONS_LEGEND = "//div[@id='BOUNDARY_SESSION_BROWSER']//div[2]//div[3][starts-with(@id,'Detail_')]//node()[@class='highcharts-legend']";

    public static final String RADIO_AND_CELL_LOAD_CONDITIONS_SETTINGS_BUTTON = "//div[@id='BOUNDARY_SESSION_BROWSER']/div[1]/div[2]/div[1]/div/div/div/div/div[2]/img";

    public static final String DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE = "//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[7]/div[1]/span[1]";

    public static final String DBS_DETAIL_VIEW_WINDOW_APPLICATION_PERFORMANCE_SUMMARY_GRID = "//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[7]/div[2]/table/tbody";

    public static final String DBS_DETAIL_VIEW_WINDOW_APPLICATION_TRAFFIC_MIX = "//*[@id=\"BOUNDARY_SESSION_BROWSER\"]/div[2]/div/div[1]/div/div[2]/div[1]/div/div/div[8]/div[1]/span[1]";

    public static final String RAN_DATA_SIGNALLING = "//div[@class='sessionScrollPanel']//table[@class='sessionTable']/tbody/tr//td";

    public static final String VISITED_CELL_RF_CONDITIONS = "//div[@id='SESSION_BROWSER_TAB']/div/div/div[2]/div/div/div[2]/div/table//div[2]/div/div/div";

    public static final String SUMMARY_VISITED_CELL_GRAPH_SVG = "//*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']";
    
    public static final String SUMMARY_VISITED_CELL_GRAPH_Y_AXIS = "/html/body/div/div/div[2]/div[3]/div/div/div[2]/div/div/div[2]/div/table/tbody/tr/td[1]//*[local-name()='svg']/*[local-name()='g'][4]";
    public static final String SUMMARY_VISITED_CELL_GRAPH_X_AXIS_TABLE = "/html/body/div/div/div[2]/div[3]/div/div/div[2]/div/div/div[2]/div/table/tbody/tr/td/div/div/div/div/div[2]/div/table";
    
    public static final String SUMMARY_VISITED_CELL_GRAPH_TOP_LEFT_TITLE = "/html/body/div/div/div[2]/div[3]/div/div/div[2]/div/div/div[2]/div/table/tbody/tr/td/div/div/div/div/div/table/tbody/tr/td[2]/div/div";
        
    public static final String SUMMARY_VISITED_CELL_HEADERS = VISITED_CELL_RF_CONDITIONS + "/div[1]/div[3]/table//th";

    public static final String SUMMARY_VISITED_CELL_TABLE = VISITED_CELL_RF_CONDITIONS + "/div[3]/div//table/tbody";

    public static final String SUMMARY_RIGHT_HAND_SIDE = "//div[@id='SESSION_BROWSER_TAB']/div/div/div[2]/div/div/div/div[2]/div/table/tbody/tr/td[2]";
    
    public static final String TCP_PERFORMANCE_PANE = SUMMARY_RIGHT_HAND_SIDE + "/div/div[2]/div/div";
    
    public static final String TCP_PERFORMANCE_PANE_DIVS = TCP_PERFORMANCE_PANE + "/div[2]/div/div";
    
    public static final String TCP_PERFORMANCE_THROUGHPUT_TABLE = TCP_PERFORMANCE_PANE + "//div[2]//div[2]";
    
    public static final String TCP_PERFORMANCE_PACKET_LOSS_TABLE = TCP_PERFORMANCE_PANE + "//div[2]//div[4]";

    /* IMSIs - these should be moved to a property file or other*/
    public static final String IMSI_ATTACH_EVENT = "454063302334488";

    public static final String IMSI_DETACH_EVENT = "454061101291496";

    public static final String IMSI_PDP_ACTIVATE_EVENT = "454063300530168";

    public static final String IMSI_PDP_DEACTIVATE_EVENT = "454064500581125";

    public static final String IMSI_RAU_EVENT = "454063302234378";

    public static final String IMSI_ISRAU_EVENT = "454063302234378";

    public static final String IMSI_SERVICE_REQ_EVENT = "454064500533035";

    public static final String IMSI_DATA_BEARER_SESSIONS = "454061192646383";

    public static final String IMSI_CFA_EVENT = "454063302579768";

    public static final String IMSI_INTERNAL_SYSTEM_RELEASE_EVENT = "454063302484039";

    public static final String IMSI_INTERNAL_IFHO_HANDOVER_EXECUTION_FAILURE = "454063392563396";

    public static final String IMSI_INTERNAL_SOFT_HANDOVER_EXECUTION_FAILURE = "454064500528999";

    public static final String IMSI_INTERNAL_SOHO_EXEC_FAILURE = "454061106012726";

    public static final String IMSI_TCPTA_PARTIAL = "454064500581540";

    public static final String IMSI_RRC_MEAS_REPORT = "454061101323538";

    public static final String IMSI_DATA_BEARER_SESSION = "454061192660135";

    public static final String IMSI_INT_FAILED_HSDSCH_CELL_CHANGE = "454061101384276";

    public static final String IMSI_INT_SUCCESS_HSDSCH_CELL_CHANGE = "454063307177916";

    public static final String IMSI_INT_HSDSCH_NO_CELL_SELECTED = "454063391517805";
    
    public static final String IMSI_CORE_RAN_USER_PLANE = "454063391406528";

    public static final String HHO_VIEW_DETAIL_IMSI = "454063302403357";

    public static final String HHO_IMSI = "454061101369804";

    public static final String HSDSCH_IMSI = "454061101349761";
    
    public static final String IMSI_FOR_CELL_CHANGE_INDICATOR = "454064500568980";
    
    public static final String IMSI_FOR_THREE_CHART = "454063392561868";

	public static final String IMSI_CORE_RAN_USER_PLANE_CLASS = "454061101213200";
}
