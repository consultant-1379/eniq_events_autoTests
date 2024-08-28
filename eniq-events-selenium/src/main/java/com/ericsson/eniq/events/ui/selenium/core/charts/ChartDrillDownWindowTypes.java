/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core.charts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eseuhon
 * @since 2010
 * This class defines types used when we send a drill down request on an event from ANY chart window
 * These types will be different depends on view menu we are looking at.
 *  
 * Please note that ALL types are extracted out from the UIMetaData.json -"drillDownWindowType" 
 * in \eniq_events\eniq_events_services\services-main\src\main\resources
 */
public class ChartDrillDownWindowTypes {

    public final static Map<String, String> menuMapsToDrillDownWindowTypes = new HashMap<String, String>();

    static {
        //Subscriber Tab        
    	menuMapsToDrillDownWindowTypes.put("Failures_callForwarding", "CS_DRILL_GRID_CHART_SUB_BI_IMSI_callForwarding");
    	menuMapsToDrillDownWindowTypes.put("Failures_locationServices", "CS_DRILL_GRID_CHART_SUB_BI_IMSI_locationServices");
    	menuMapsToDrillDownWindowTypes.put("Failures_mSOriginating", "CS_DRILL_GRID_CHART_SUB_BI_IMSI");
    	menuMapsToDrillDownWindowTypes.put("Failures_mSTerminating", "CS_DRILL_GRID_CHART_SUB_BI_IMSI");
    	menuMapsToDrillDownWindowTypes.put("Failures_roamingCallForwarding", "CS_DRILL_GRID_CHART_SUB_BI_IMSI_roamingCallForwarding");    	
    	menuMapsToDrillDownWindowTypes.put("Failures_mSOriginatingSMSinMSC", "CS_DRILL_GRID_CHART_SUB_BI_IMSI_SMS");    	
    	menuMapsToDrillDownWindowTypes.put("Failures_mSTerminatingSMSinMSC", "CS_DRILL_GRID_CHART_SUB_BI_IMSI_SMS");    	    	
    	menuMapsToDrillDownWindowTypes.put("Failures","CS_DRILL_GRID_CHART_SUB_BI_IMSI");
    	
        menuMapsToDrillDownWindowTypes.put("APN Usage", "DRILL_GRID_CHART_SUB_BI_APN");
        menuMapsToDrillDownWindowTypes.put("Busy Hour", "DRILL_GRID_CHART_SUB_BI_BUSY_HOUR");
        menuMapsToDrillDownWindowTypes.put("Busy Day", "DRILL_GRID_CHART_SUB_BI_BUSY_DAY");
        menuMapsToDrillDownWindowTypes.put("Access Area", "DRILL_GRID_CHART_SUB_BI_CELL");
        //TODO need to add more types for other tabs. i.e Terminal, Network, etc.

        // Terminal Tab
        menuMapsToDrillDownWindowTypes.put("Most Frequent Signaling Terminal Groups",
                "DRILL_CHART_TERMINAL_GA_MOST_POPULAR");
        menuMapsToDrillDownWindowTypes.put("Most Frequent Signaling Event Summary",
                "DRILL_CHART_TERMINAL_GA_MOST_POPULAR_SUMMARY");
        menuMapsToDrillDownWindowTypes.put("Most Attach Failures", "DRILL_CHART_TERMINAL_GA_MOST_ATTACHED_FAILURES");
        menuMapsToDrillDownWindowTypes.put("Most PDP Session Setup Failures",
                "DRILL_CHART_TERMINAL_GA_MOST_PDP_SESSION_SETUP_FAILURES");
        menuMapsToDrillDownWindowTypes.put("Most Mobility Failures", "DRILL_CHART_TERMINAL_GA_MOST_MOBILITY_ISSUES");
    };
}