package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import java.util.Arrays;
import java.util.List;



/**
 * -----------------------------------------------------------------------
 * Copyright (C) ${year} LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

/**
 * @author epatmah
 * @since May 2012
 * 
 */
public final class SonVisConstants  {
	public static final String SON_VISUALIZATION = "SON Visualization";
	
	public static final String RANKING_TABLE_BUTTON = "//div[@id='SONVIS_TAB']//div//td[text()='Ranking Table']";
	
	public static final String CELL_DETAILS_BUTTON = "//div[@id='SONVIS_TAB']//div//td[text()='Cell Details']";
							
	public static final String RANK_TIME_SELECTOR_DROP_DOWN_BOX = "//div[@id='rankTimeSelector']//select";
	
	public static final String RANKING_MENU_DROP_DOWN_BOX = "//div[@id='rankingMenu']//select";
	
	public static final String CELL_DETAILS_DROP_DOWN_BOX = "//div[@class='piDropdownRanking']/select";
	
	public static final String TODAY = "Today";
	
	public static final String SON_VIS_RESERVED_DATA_FILENAME = "E:\\epatmah\\SonVisReservedDataREV1.csv";
	
	public static final String CGI = "CGI";
	
	public static final String CELL_NAME = "Cell Name";
	
	public static final String RANK = "Rank";
	
	public static final String ANR_X2_REL_ADD = "ANR/X2 Rel Add";
	
	public static final String ANR_X2_RELATIONS_ADDED = "ANR/X2 Relations Added";
	
	public static final String ANR_X2_REL_REM = "ANR/X2 Rel Rem";
	
	public static final String ANR_X2_RELATIONS_REMOVED = "ANR/X2 Relations Removed";
	
	public static final String PCI_CHANGES = "PCI Changes";
	
	public static final String TOTAL_PCI_CONFLICTS = "Total PCI Conflicts";
	
	public static final String PCI_CLRD_CONFLICTS = "PCI Clrd Conflicts";
	
	public static final String INIT_ERAB_EST_SR = "Init ERAB Est SR";
	
	public static final String ADDED_ERAB_EST_SR= "Added ERAB Est SR";
	
	public static final String ERAB_RETAIN = "ERAB Retain";
	
	public static final String DL_LATENCY = "DL Latency";
	
	public static final String UL_THROUGHPUT = "UL Throughput";
	
	public static final String DL_THROUGHPUT = "DL Throughput";
	
	public static final String UL_PKT_LOSS_RATE = "UL Pkt Loss Rate";
	
	public static final String DL_PKT_ERR_LOSS_RATE = "DL Pkt Err Loss Rate";
													  
	public static final String CELL_AVAIL= "Cell Avail";
	
	public static final String SON_VIS = "SON_VIS";
	
	public static final String CONFLICT_TYPE = "Conflict Type";
	
	public static final String CONFLICTING_CELL = "Conflicting Cell";
	
	public static final String DETECTING_CELL = "Detecting Cell";
	
	public static final String DETECTED_BY = "Detected By";
	
	public static final String START_TIME = "Start Time";
	
	public static final String END_TIME = "End Time";
	
	//We need to use the following specific xpath with svg elements. The xpath below that Firebug gives will not work:
	//public static final String Y_LABEL = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[3]/div/div/div/svg/text[2]";
	public static final String LEGEND_LABEL = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[3]/div/div/div//*[local-name()='svg']/*[local-name()='g' and @class='highcharts-legend']/*[local-name()='text']/*[local-name()='tspan']";
			
	//This gives us the corresponding text element that does not have a class attribute. The other text element has class highcharts-legend.
	public static final String Y_LABEL= "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[3]/div/div/div//*[local-name()='svg']/*[local-name()='text' and not(@class)]/*[local-name()='tspan']";
		
	//This is the title on top. Uses class = ... xpath function
	public static final String CHART_TITLE= "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[3]/div/div/div//*[local-name()='svg']/*[local-name()='text' and @class= 'highcharts-title']/*[local-name()='tspan']";
		
	
	
	public static final List<String> SON_VIS_RESERVED_DATA_COLUMNS = Arrays.asList(CGI,CELL_NAME,RANK,ANR_X2_REL_ADD,ANR_X2_REL_REM,PCI_CHANGES,TOTAL_PCI_CONFLICTS,
			PCI_CLRD_CONFLICTS,INIT_ERAB_EST_SR,ADDED_ERAB_EST_SR,ERAB_RETAIN,DL_LATENCY,UL_THROUGHPUT,DL_THROUGHPUT,UL_PKT_LOSS_RATE,DL_PKT_ERR_LOSS_RATE,CELL_AVAIL, CONFLICT_TYPE, 
			CONFLICTING_CELL, DETECTING_CELL, DETECTED_BY, START_TIME, END_TIME);
	
	public static final List<String> SON_VIS_RANKING_TOP_100_CELLS_COLUMNS = Arrays.asList(CGI, CELL_NAME, ANR_X2_REL_ADD, ANR_X2_REL_REM, PCI_CHANGES, TOTAL_PCI_CONFLICTS, 
			INIT_ERAB_EST_SR, ADDED_ERAB_EST_SR, ERAB_RETAIN, DL_LATENCY, UL_THROUGHPUT, DL_THROUGHPUT, UL_PKT_LOSS_RATE, DL_PKT_ERR_LOSS_RATE, CELL_AVAIL);
	
	public static final List<String> SON_VIS_CELL_DETAILS_DATA_COLUMNS = Arrays.asList(CONFLICT_TYPE, CONFLICTING_CELL, DETECTING_CELL, DETECTED_BY);
	

	
}
