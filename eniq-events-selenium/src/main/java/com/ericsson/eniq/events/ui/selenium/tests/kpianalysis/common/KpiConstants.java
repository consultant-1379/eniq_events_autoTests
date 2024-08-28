/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class KpiConstants {

    //Launch Menu Constants
    //************************************************************************************************************************************************************

	public final static String OPEN_LAUNCH_MENU = "//div[@id='KPI_ANALYSIS_BOUNDARY']//img[contains(@title,'show Launcher')]";
			
    //Date time dropdown
    public final static String DATE_TIME_DROPDOWN_LAUNCH = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[1]/div/table/tbody/tr/td[2]/div";

    //selected date time
    
    // (xpath refactored due to automation script)
    //public final static String DATE_TIME_SELECTED = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[1]/div/table/tbody/tr/td[1]/div";
    public final static String DATE_TIME_SELECTED = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[1]/div/table/tbody/tr/td[1]/div";

    //Kpi type dropdown
    public final static String KPI_TYPE_DROPDOWN_LAUNCH = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[2]/div/table/tbody/tr/td[2]/div";

    //selected kpi type
    public final static String KPI_TYPE_DROPDOWN_SELECTED = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[2]/div/table/tbody/tr/td[1]/div";

    //Controller dropdown
    public final static String CONTROLLER_DROPDOWN_LAUNCH = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[3]/div/table/tbody/tr/td[2]/div";

    //Controller input(RNCs)
    public final static String CONTROLLER_INPUT = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[3]/div/table/tbody/tr/td[1]/input";

    //Cell dropdown
    public final static String CELL_DROPDOWN_LAUNCH = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[4]/div/table/tbody/tr/td[2]/div";

    //Cell input(Cells)
    public final static String CELL_INPUT = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[4]/div/table/tbody/tr/td[1]/input";
    
    //KPI dropdown (in launch menu)
    public final static String KPI_DROPDOWN_LAUNCH = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[1]/div[5]/div/table/tbody/tr/td[2]/div";
    
    //KPI

    //KPI Type constants
    //*************************************************************************************
    //Network
    public final static String NETWORK = "//div[@class='popupContent']//div/div[@__idx='1']";

    //Controller
    public final static String CONTROLLER = "//div[@class='popupContent']//div/div[@__idx='0']";
    
    //Window Header constants
    //*************************************************************************************
    
    //All options is the list
    public final static String KPI_OPTION_LIST = "//div[@id='KPI_ANALYSIS_BOUNDARY']//div[@class='popupContent']/div/div";
    
    //KPI dropdown (in kpi window)
    public final static String KPI_DROPDOWN_WINDOW_HEADER = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";
	    
    //Date Time constants
    //*************************************************************************************
    //15 minutes
    public final static String DATE_TIME_15 = "//div[@class='popupContent']//div/div[@__idx='0']";

    //30 minutes
    public final static String DATE_TIME_30 = "//div[@class='popupContent']//div/div[@__idx='1']";

    //1 hour
    public final static String DATE_TIME_1HOUR = "//div[@class='popupContent']//div/div[@__idx='2']";

    //2 hours
    public final static String DATE_TIME_2HOUR = "//div[@class='popupContent']//div/div[@__idx='3']";

    //6 hours
    public final static String DATE_TIME_6HOUR = "//div[@class='popupContent']//div/div[@__idx='4']";

    //12 hours
    public final static String DATE_TIME_12HOUR = "//div[@class='popupContent']//div/div[@__idx='5']";

    //Custom
    public final static String DATE_TIME_CUSTOM = "//div[@class='popupContent']//div/div[@__idx='6']";

    //Update Button
    public final static String UPDATE_BUTTON = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[2]/div[3]/div/div[2]/div/div[2]/table/tbody/tr/td/button";
    
    //KPI Map constants
    //*************************************************************************************
    //Map
    public final static String KPI_MAP = "//div[@id='KPI_ANALYSIS_BOUNDARY']//div[@class='olMap'][1]";
    
    //Map expand button
    public final static String KPI_MAP_EXPAND = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[2]";
    
    //Map hover message
    public final static String KPI_MAP_HOVER_MSG = "//div[contains(@class, 'gwt-PopupPanel')]/div[@class='popupContent']";
    
    //KPI Thresholds
    public final static String KPI_THRESHOLDS = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div/div/div[3]";
    
    //Scale
    public final static String KPI_MAP_SCALE = KPI_MAP + "//div[contains(@id,'ScaleLine')]";
    
    //Overview window
    public final static String KPI_MAP_OVERVIEW = KPI_MAP + "//div[@class='olControlOverviewMapElement']";
    
    //Overview window maximize and mimimize
    public final static String KPI_MAP_OVERVIEW_MAXIMIZE_BUTTON = KPI_MAP + "//div[@class='olControlOverviewMapMaximizeButton']";
    public final static String KPI_MAP_OVERVIEW_MINIMIZE_BUTTON = KPI_MAP + "//div[@class='olControlOverviewMapMinimizeButton']";
    
    //Map zoom controls
    public final static String KPI_MAP_ZOOM = KPI_MAP + "//div[contains(@id,'PanZoomBar')]";
    public final static String KPI_MAP_ZOOM_LEFT = KPI_MAP_ZOOM + "/div[contains(@id,'panleft')]"; 
    public final static String KPI_MAP_ZOOM_RIGHT = KPI_MAP_ZOOM + "/div[contains(@id,'panright')]";
    public final static String KPI_MAP_ZOOM_UP = KPI_MAP_ZOOM + "/div[contains(@id,'panup')]"; 
    public final static String KPI_MAP_ZOOM_DOWN = KPI_MAP_ZOOM + "/div[contains(@id,'pandown')]"; 
    public final static String KPI_MAP_ZOOM_IN = KPI_MAP_ZOOM + "/div[contains(@id,'zoomin')]"; 
    public final static String KPI_MAP_ZOOM_OUT = KPI_MAP_ZOOM + "/div[contains(@id,'zoomout')]";
    
    //Map longitude and latitude
    public final static String KPI_MAP_POSITION = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[2]/table//td[1]//td";
    
    //KPI Map Header
    public final static String KPI_MAP_HEADER = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div/div/div[3]/div/div/div/div[1]";
    
    
}
