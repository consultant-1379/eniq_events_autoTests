package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TerminalAnalysis {
    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
	
	final public static boolean eventAnalysisOnTerminal( 
			final List<Map<String, String>> completeUITableValues,
			final String tac, final String terminalMake, 
			final String terminalModel,
			final List<String> allEventTypes,
			final List<String> WindowHeaders,
			final int duration) 
	{		
		final List<List<String>> csvDataAsList = GeneralUtil.getCSVDataValues();
		final List<List<String>> terminalRelatedCSVData = 
			GeneralUtil.getCSVDataForTerminal( 
					csvDataAsList, tac, terminalMake, terminalModel);
	    List<List<String>> failureList = new ArrayList<List<String>>();
	    for(final String eventName : allEventTypes )
	    {
		    final List<String> completeData = 
		    	AggregrationHandlerUtil.getEventAnalysisOnSubscriberTab(
		    			terminalRelatedCSVData, eventName, duration, WindowHeaders);
		    if ( !completeData.contains(DataIntegrityConstants.NO_DATA))
		    {
			    failureList.add(completeData);		    	
		    }		
	    }
	    logger.log(Level.INFO, "The failure list is :" + failureList);
	    final boolean resultOfAnalysis = 
    		(GeneralUtil.formatUIFailureAnalysisData(
    				completeUITableValues,WindowHeaders)).containsAll(failureList);
	    return resultOfAnalysis;
	 }
	
	

	
	final public static boolean eventAnalysisOnTerminalGroups( 
			final List<Map<String, String>> completeUITableValues,
			List<String> tacList, 
			final List<String> allEventTypes,
			final List<String> WindowHeaders,
			final int duration) 
	{		
		List<List<String>> csvDataAsList = GeneralUtil.getCSVDataValues(); 
		List<List<String>> terminalGroupCSVData = 
			GeneralUtil.getCSVDataForTerminalGroup(csvDataAsList, tacList);
	    List<List<String>> failureList = new ArrayList<List<String>>();
	    for(final String event : allEventTypes )
	    {
		    final List<String> completeData = 
		    	AggregrationHandlerUtil.getEventAnalysisOnSubscriberTab(
		    			terminalGroupCSVData, event, duration, WindowHeaders);
		    if ( !completeData.contains(DataIntegrityConstants.NO_DATA))
		    {
			    failureList.add(completeData);		    	
		    }		
	    }
	    logger.log(Level.INFO, "The failure list is :" + failureList);
	    final boolean resultOfAnalysis = 
    		(GeneralUtil.formatUIFailureAnalysisData(completeUITableValues,
    				WindowHeaders)).containsAll(failureList);
	    return resultOfAnalysis;
	 }
	
	
}
