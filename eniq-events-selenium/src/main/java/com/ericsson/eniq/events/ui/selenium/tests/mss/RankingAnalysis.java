package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RankingAnalysis{

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
   
    /* This method is to validate whether the ranking analysis data on UI 
     * is valid or not. 
     * 
     * @param completeUITableValues - The data fetched from UI. 
     * @param defaultRankingHeaders - The ranking headers of the ranking analysis 
     *                                selected.
     * @param rankingAnalysisfield - What analysis performed ie. Subscriber, MSC etc.
     * @param analysisType - The type of analysis selected ie. blocked or Dropped 
     * @param duration - Time framed selected.
     *  
     * @return isRankingAnalysisValid - Returns whether the analysis is valid or not.  
     */
    
	public static boolean rankingAnalysis(
			final List<Map<String, String>> completeUITableValues,
			final List<String> defaultRankingHeaders,
			final String rankingAnalysisfield, final String analysisType,
			final int duration)
	{
		List<List<String>> mssVoiceRelatedCsvDataList = GeneralUtil
				.getVoiceRelatedCsvData();
		List<List<String>> analysisDataBasedOnCSV = new ArrayList<List<String>>();
		String SortAndRankBasedOnField = GuiStringConstants.FAILURES; 
		if (rankingAnalysisfield.equalsIgnoreCase(GuiStringConstants.IMSI))
		{
			
			analysisDataBasedOnCSV = getRankingForSubscriber(
					mssVoiceRelatedCsvDataList, rankingAnalysisfield, 
					analysisType, defaultRankingHeaders, duration);
		}
		else if (rankingAnalysisfield.equalsIgnoreCase(
				GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION)) 
		{
			
			analysisDataBasedOnCSV = getRankingForCauseCode(
					mssVoiceRelatedCsvDataList,	rankingAnalysisfield, 
					defaultRankingHeaders, duration);
		}
		else if (rankingAnalysisfield
				.equalsIgnoreCase(GuiStringConstants.MSC)) 
		{
			
			analysisDataBasedOnCSV = getRankingForMSC(
					mssVoiceRelatedCsvDataList, rankingAnalysisfield, 
					defaultRankingHeaders, analysisType, duration);
			
			
		} 
		else if (rankingAnalysisfield
				.equalsIgnoreCase(GuiStringConstants.CONTROLLER)) 
		{
			
			analysisDataBasedOnCSV = getRankingForRNCBSCController(
					mssVoiceRelatedCsvDataList, rankingAnalysisfield,
					defaultRankingHeaders, analysisType, duration);
		}
		else if (rankingAnalysisfield
				.equalsIgnoreCase(GuiStringConstants.TERMINAL_MAKE)) 
		{
			
			analysisDataBasedOnCSV = getRankingForTerminal(
					mssVoiceRelatedCsvDataList, rankingAnalysisfield,
					defaultRankingHeaders, analysisType, duration);
		}
		else if (rankingAnalysisfield
				.equalsIgnoreCase(GuiStringConstants.ACCESS_AREA)) 
		{
			
			analysisDataBasedOnCSV = getRankingForAccessArea(
				mssVoiceRelatedCsvDataList,	rankingAnalysisfield,
				analysisType, defaultRankingHeaders, duration);
		}
		else if (rankingAnalysisfield
				.equalsIgnoreCase(GuiStringConstants.UNANSWERED_CALLS)) 
		{
			
			analysisDataBasedOnCSV = getRankingForUnansweredCalls(
				mssVoiceRelatedCsvDataList,	rankingAnalysisfield,
				analysisType, defaultRankingHeaders, duration);
			SortAndRankBasedOnField = GuiStringConstants.UNANSWERED_CALLS;
		}
		final List<List<String>> orderedListByFailures = 
			sortAscendingOnFailures( analysisDataBasedOnCSV, 
					defaultRankingHeaders, SortAndRankBasedOnField);
		final List<List<String>> rankedList = assigningRanks(
				orderedListByFailures, defaultRankingHeaders,
				SortAndRankBasedOnField);
		final boolean isRankingAnalysisValid =
			(GeneralUtil.formatUIFailureAnalysisData(
					    completeUITableValues,
						defaultRankingHeaders)).containsAll(rankedList);
		return isRankingAnalysisValid;
	}
    
    /* This method is to identify the data for the subsciber ranking based 
     * on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the ranking
     *                                     analysis selected.
     * @param analysisType - The type of analysis selected ie.blocked/Dropped 
     * @param duration     - Time framed selected.
     *  
     * @return allIMSIRankingData - Contains the subscriber ranking analysis 
     *                              data.  
     */
	
	final static List<List<String>> getRankingForSubscriber( 
			final List<List<String>> mssVoiceRelatedCsvDataList,
		    final String rankingAnalysisfield, final String analysisType,
		    final List<String> defaultRankingHeaders, final int duration)
	{
		List<List<String>> allIMSIRankingData = new ArrayList<List<String>>();
        final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
		HashSet<String> imsiList = new HashSet<String>();
		for( final List<String> rowData : mssVoiceRelatedCsvDataList)
		{
			imsiList.add(rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					rankingAnalysisfield)));
		}
		for(final String imsi : imsiList)
		{
			int failureCount = 0;
			int successCount = 0;
			List<String> singleIMSIData = new ArrayList<String>();
			for(final List<String> rowData : mssVoiceRelatedCsvDataList)
			{
				final String csvSelectedHeaderValue = rowData.get(GeneralUtil.
				           getReservedCSVDataHeaderIndex(rankingAnalysisfield));
				final String currentStatus = rowData.get(GeneralUtil.
			             getReservedCSVDataHeaderIndex(
			            		 GuiStringConstants.EVENT_RESULT));
				if ( imsi.equalsIgnoreCase(csvSelectedHeaderValue))
				{
					if((analysisType.equalsIgnoreCase(currentStatus)))
					{
						failureCount++;
					}
					if( successStatus.equalsIgnoreCase(currentStatus) )
					{
						successCount++;
					}		
				}
			}
			for(final String header : defaultRankingHeaders)
			{
				if( header.equalsIgnoreCase(GuiStringConstants.RANK))
				{
					singleIMSIData.add(DataIntegrityConstants.NO_RANK_ASSIGNED);	
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.IMSI))
				{
					singleIMSIData.add(imsi);
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.FAILURES))
				{
					singleIMSIData.add(Integer.toString(failureCount * duration));
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.SUCCESSES))
				{
					singleIMSIData.add(Integer.toString(successCount * duration));
				}
			}
			allIMSIRankingData.add(singleIMSIData);
		}
		logger.log(Level.INFO, "Subscriber Ranking Analysis Data :" + 
				allIMSIRankingData.toString());
		return allIMSIRankingData;
	}


    /* This method is to identify the data for the Cause Code ranking based 
     * on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the ranking
     *                                     analysis selected.
     * @param duration     - Time framed selected.
     *  
     * @return allInternalCauseCodeRankingData -Contains the Internal Cause 
     *                                          Code ranking analysis data.
     */
	
	final static List<List<String>> getRankingForCauseCode( 
			final List<List<String>> mssVoiceRelatedCsvDataList, 
			final String rankingAnalysisfield,
			final List<String> defaultRankingHeaders,
			final int duration)
	{
		List<List<String>> allInternalCauseCodeRankingData = 
			new ArrayList<List<String>>();
		final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
		final int internalCauseCodeDescIndex = 0;
		final int internalCauseCodeIDIndex = 1;
		HashSet<List<String>> internalCauseCodeList = 
			new HashSet<List<String>>();
		for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
		{
			final String internalCauseCodeDescription = rowData.get(GeneralUtil
					.getReservedCSVDataHeaderIndex(rankingAnalysisfield));
			final String internalCauseCodeID = rowData.get(GeneralUtil
							.getReservedCSVDataHeaderIndex(
									GuiStringConstants.INTERNAL_CAUSE_CODE_ID));
			final List<String> internalCauseCode = new ArrayList<String>();
			internalCauseCode.add(internalCauseCodeDescription);
			internalCauseCode.add(internalCauseCodeID);
			internalCauseCodeList.add(internalCauseCode);
		}
		logger.log(Level.INFO,"The Internal Cause Code are : "
				+ internalCauseCodeList);
		for (final List<String> internalCauseCode : internalCauseCodeList) {
			List<String> singleCauseCodeRankingData = new ArrayList<String>();
			int failureCount = 0;
			int successCount = 0;
			final String internalCauseCodeDesc = internalCauseCode.get(
					internalCauseCodeDescIndex);
			final String internalCauseCodeID = internalCauseCode.get(
					internalCauseCodeIDIndex);
			for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
			{
				final String csvSelectedHeaderValue = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(rankingAnalysisfield));
				final String csvEventstatus = rowData.get(GeneralUtil
								.getReservedCSVDataHeaderIndex(
										GuiStringConstants.EVENT_RESULT));
				if (internalCauseCodeDesc
						.equalsIgnoreCase(csvSelectedHeaderValue)) 
				{
					if (DataIntegrityConstants.STATUS_DROPPED
							.equalsIgnoreCase(csvEventstatus)
							|| DataIntegrityConstants.STATUS_BLOCKED
									.equalsIgnoreCase(csvEventstatus))
					{
						failureCount++;
					}
					else if ( successStatus.equalsIgnoreCase(csvEventstatus))
					{ 
						successCount++; 
				    }					 
				}
			}
			for (final String header : defaultRankingHeaders)
			{
				if (header.equalsIgnoreCase(GuiStringConstants.RANK))
				{
					singleCauseCodeRankingData
							.add(DataIntegrityConstants.NO_RANK_ASSIGNED);
				}
				else if (header.equalsIgnoreCase(
						GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION)) 
				{
					singleCauseCodeRankingData.add(internalCauseCodeDesc);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.FAILURES)) 
				{
					singleCauseCodeRankingData.add(Integer.toString(
							failureCount * duration));
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.SUCCESSES)) 
				{
					singleCauseCodeRankingData.add(Integer.toString(
							successCount * duration));
				}
				else if (header.equalsIgnoreCase(
						GuiStringConstants.INTERNAL_CAUSE_CODE_ID))
				{
					singleCauseCodeRankingData.add(internalCauseCodeID);
				}
			}
			allInternalCauseCodeRankingData.add(singleCauseCodeRankingData);
		}
		logger.log(Level.INFO,"InternalCauseCode Ranking Data :"
				+ allInternalCauseCodeRankingData.toString());
		return allInternalCauseCodeRankingData;
	}
	
    /* This method is to identify the data for the MSC ranking based 
     * on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the MSC 
     *                                     ranking analysis.
     * @param analysisType - The type of analysis selected ie.blocked/Dropped 
     * @param duration     - Time framed selected.
     *  
     * @return allMSCRankingData - Contains the MSC ranking analysis 
     *                              data.
     */
	
	final static List<List<String>> getRankingForMSC( 
		   final List<List<String>> mssVoiceRelatedCsvDataList, 
		   final String rankingAnalysisfield,
		   final List<String> defaultRankingHeaders, final String analysisType,
		   final int duration)
 {
		List<List<String>> allMSCRankingData = new ArrayList<List<String>>();
		HashSet<List<String>> completeMSCList = new HashSet<List<String>>();
		final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
		final int mscIndex = 0;
		final int ranVendorIndex = 1;
		for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
		{
			final String msc = rowData.get(GeneralUtil
							.getReservedCSVDataHeaderIndex(
									GuiStringConstants.MSC)).trim();
			final String ranVendor = rowData.get(GeneralUtil
							.getReservedCSVDataHeaderIndex(
									GuiStringConstants.RAN_VENDOR)).trim();
			if (!msc.equalsIgnoreCase("") && !ranVendor.equalsIgnoreCase(""))
			{
				List<String> singleMSCData = new ArrayList<String>();
				singleMSCData.add(msc.trim());
				singleMSCData.add(ranVendor.trim());
				completeMSCList.add(singleMSCData);
			}
		}
		logger.log(Level.INFO,completeMSCList.toString());
		for (final List<String> singleMSCData : completeMSCList) 
		{
			List<String> singleMSCRankingData = new ArrayList<String>();
			int failureCount = 0;
			int successCount = 0;
			final String mscName = singleMSCData.get(mscIndex);
			final String ranVendor = singleMSCData.get(ranVendorIndex);
			for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
			{
				final String csvSelectedHeaderValue = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(rankingAnalysisfield));
				final String csvEventStatus = rowData.get(GeneralUtil
								.getReservedCSVDataHeaderIndex(
										GuiStringConstants.EVENT_RESULT)).trim();
				if (mscName.equalsIgnoreCase(csvSelectedHeaderValue)) 
				{
					if (csvEventStatus.equalsIgnoreCase(analysisType)) 
					{
						failureCount++;
					}
					if (csvEventStatus.equalsIgnoreCase(successStatus)) 
					{
						successCount++;
					}
				}
			}
			for (final String header : defaultRankingHeaders) 
			{
				if (header.equalsIgnoreCase(GuiStringConstants.RANK)) 
				{
					singleMSCRankingData
							.add(DataIntegrityConstants.NO_RANK_ASSIGNED);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.VENDOR)) 
				{
					singleMSCRankingData.add(ranVendor);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.MSC)) 
				{
					singleMSCRankingData.add(mscName);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.FAILURES)) 
				{
					singleMSCRankingData.add(Integer.toString(failureCount
							* duration));
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.SUCCESSES)) 
				{
					singleMSCRankingData.add(Integer.toString(successCount
							* duration));
				}
			}
			allMSCRankingData.add(singleMSCRankingData);
		}
		logger.log(Level.INFO,"MSC Ranking Data :" + allMSCRankingData.toString());
		return allMSCRankingData;
	}

    /* This method is to identify the data for the Terminal ranking based 
     * on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the MSC 
     *                                     ranking analysis.
     * @param analysisType - The type of analysis selected ie.blocked/Dropped 
     * @param duration     - Time framed selected.
     *  
     * @return allTerminalRankingData - Contains the Terminal ranking analysis 
     *                              data.
     */
	
	final static List<List<String>> getRankingForTerminal( 
			final List<List<String>> mssVoiceRelatedCsvDataList,
		    final String rankingAnalysisfield,
		    final List<String> defaultRankingHeaders,
		    final String AnalysisType,
		    final int duration)
    {
		List<List<String>> allTerminalRankingData = new ArrayList<List<String>>();
		final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
		HashSet<List<String>> completeTerminalList = new HashSet<List<String>>();
		final int manufacturerIndex = 0;
		final int modelIndex = 1;
		final int tacIndex = 2;
		for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
		{
			final String manufacturer = rowData.get(GeneralUtil
							.getReservedCSVDataHeaderIndex(
									GuiStringConstants.MANUFACTURER)).trim();
			final String model = rowData.get(GeneralUtil
				.getReservedCSVDataHeaderIndex(
							GuiStringConstants.MODEL)).trim();
			final String tac = rowData.get(GeneralUtil
				.getReservedCSVDataHeaderIndex(GuiStringConstants.TAC)).trim();
			if(!manufacturer.equalsIgnoreCase("") &&
					!model.equalsIgnoreCase("") &&
					!tac.equalsIgnoreCase(""))
			{
				List<String> terminalData = new ArrayList<String>();
				terminalData.add(manufacturer);
				terminalData.add(model);
				terminalData.add(tac);
				completeTerminalList.add(terminalData);
				
			}
		}
		logger.log(Level.INFO,"The complete Terminal :" + completeTerminalList.toString());
		for (final List<String> terminal : completeTerminalList) 
		{
			List<String> singleTerminalRankingData = new ArrayList<String>();
			int failureCount = 0;
			int successCount = 0;
			final String manufacturer = terminal.get(manufacturerIndex);
			final String model = terminal.get(modelIndex);
			final String tac = terminal.get(tacIndex);
			for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
			{
				final String csvSelectedHeaderValue = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(
								rankingAnalysisfield)).trim();
				final String status = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(
								GuiStringConstants.EVENT_RESULT)).trim();
				final String csvTerminalModel = rowData.get(GeneralUtil
								.getReservedCSVDataHeaderIndex(
										GuiStringConstants.MODEL)).trim();
				final String csvTAC = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(
								GuiStringConstants.TAC)).trim();
				if (manufacturer.equalsIgnoreCase(csvSelectedHeaderValue)
						&& model.equalsIgnoreCase(csvTerminalModel)
						&& tac.equalsIgnoreCase(csvTAC)) 
				{
					if (status.equalsIgnoreCase(AnalysisType)) 
					{
						failureCount++;
					}
					if (successStatus.equalsIgnoreCase(status)) 
					{
						successCount++;
					}
				}
			}
			for (final String header : defaultRankingHeaders)
			{
				if (header.equalsIgnoreCase(GuiStringConstants.RANK)) 
				{
					singleTerminalRankingData
							.add(DataIntegrityConstants.NO_RANK_ASSIGNED);
				}
				else if (header
						.equalsIgnoreCase(GuiStringConstants.TERMINAL_MAKE)) 
				{
					singleTerminalRankingData.add(manufacturer);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.TERMINAL_MODEL)) 
				{
					singleTerminalRankingData.add(model);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.TAC)) 
				{
					singleTerminalRankingData.add(tac);
				}
				else if (header.equalsIgnoreCase(GuiStringConstants.FAILURES)) 
				{
					singleTerminalRankingData.add(Integer.toString(failureCount
							* duration));
				}
				else if (header
						.equalsIgnoreCase(GuiStringConstants.SUCCESSES)) 
				{
					singleTerminalRankingData.add(Integer.toString(successCount
							* duration));
				}
			}
				allTerminalRankingData.add(singleTerminalRankingData);
		}
		logger.log(Level.INFO,"Terminal Ranking Data :"
				+ allTerminalRankingData.toString());
		return allTerminalRankingData;
	}


	
	
    /* This method is to identify the data for the RNC/ BSC Controller
     * ranking based on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the MSC 
     *                                     ranking analysis.
     * @param analysisType - The type of analysis selected ie.blocked/Dropped 
     * @param duration     - Time framed selected.
     *  
     * @return allTerminalRankingData - Contains the Terminal ranking analysis 
     *                              data.
     */
	
	final static List<List<String>> getRankingForRNCBSCController( 
			final List<List<String>> mssVoiceRelatedCsvDataList,
		    final String rankingAnalysisfield,
		    final List<String> defaultRankingHeaders,
		    final String analysisType,
		    final int duration)
 {
		List<List<String>> allControllerRankingList = 
			new ArrayList<List<String>>();
		final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
		String controllerType = "";
		if (defaultRankingHeaders.contains(
				DataIntegrityConstants.RNC_CONTROLLER))
		{
			controllerType = DataIntegrityConstants.RNC_CONTROLLER;
		}
		else 
		{
			controllerType = DataIntegrityConstants.BSC_CONTROLLER;
		}
		boolean isRATConsidered = false;
		if (defaultRankingHeaders.contains(GuiStringConstants.RAT)) 
		{
			isRATConsidered = true;
		}
		HashSet<List<String>> completeControllerList =
			getCompleteControllerList(mssVoiceRelatedCsvDataList, 
					isRATConsidered, controllerType);
		for (final List<String> rnc : completeControllerList) 
		{
			List<String> controllerData = new ArrayList<String>();
			int ericcssonFailureCount = 0;
			int ericssonSuccessCount = 0;
			int nonEriccssonFailureCount = 0;
			int nonEricssonSuccessCount = 0;
			String rat = "";
			String ratID = "";
			final String rncName = rnc.get(0);
			final String ranVendor = rnc.get(1);
			if (isRATConsidered) 
			{
				rat = rnc.get(2);
				ratID = rnc.get(3);
			}
			for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
			{
				final String csvSelectedHeaderValue = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(rankingAnalysisfield));
				final String status = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(
								GuiStringConstants.EVENT_RESULT));
				final String csvRanVendor = rowData.get(GeneralUtil
								.getReservedCSVDataHeaderIndex(
										GuiStringConstants.RAN_VENDOR));
				final String csvRAT = rowData.get(GeneralUtil
						.getReservedCSVDataHeaderIndex(GuiStringConstants.RAT));
				final String csvRATID = Integer.toString(GeneralUtil
						.getRATID(csvRAT));
				if (rncName.equalsIgnoreCase(csvSelectedHeaderValue))
				{
					if (csvRanVendor.equalsIgnoreCase("ERICSSON")) 
					{
						if (isRATConsidered) 
						{
							if (rat.equalsIgnoreCase(csvRAT)) 
							{
								if (status.equalsIgnoreCase(analysisType)) 
								{
									ericcssonFailureCount++;
								}
								if (successStatus.equalsIgnoreCase(status)) 
								{
									ericssonSuccessCount++;
								}
							}
						} 
						else
						{
							if (status.equalsIgnoreCase(analysisType)) 
							{
								ericcssonFailureCount++;
							}
							if (successStatus.equalsIgnoreCase(status)) 
							{
								ericssonSuccessCount++;
							}
						}
					} 
					else if (csvRanVendor.equalsIgnoreCase(ranVendor)) 
					{
						if (isRATConsidered) 
						{
							if (rat.equalsIgnoreCase(csvRAT)) 
							{
								if (status.equalsIgnoreCase(analysisType)) 
								{
									nonEriccssonFailureCount++;
								}
								if (successStatus.equalsIgnoreCase(status)) 
								{
									nonEricssonSuccessCount++;
								}
							}
						} 
						else
						{
							if (status.equalsIgnoreCase(analysisType)) 
							{
								nonEriccssonFailureCount++;
							}
							if (successStatus.equalsIgnoreCase(status)) 
							{
								nonEricssonSuccessCount++;
							}
						}
					}
				}
			}
			if (!ranVendor.equalsIgnoreCase("")
					&& !rncName.equalsIgnoreCase("")) 
			{

					controllerData = getCompleteControllerRankingList(
							controllerType, ranVendor, rncName,
							ericcssonFailureCount, nonEriccssonFailureCount,
							ericssonSuccessCount, nonEricssonSuccessCount, rat,
							ratID, duration, defaultRankingHeaders);
					allControllerRankingList.add(controllerData);
			}
		}
		logger.log(Level.INFO,"The Controller Ranking Data :"
				+ allControllerRankingList.toString());
		return allControllerRankingList;
	}

	
	final static List<List<String>> getRankingForAccessArea( 
			final List<List<String>> csvDataAsList,
		    final String csvHeader,
		    final String typeOfSubcriberAnalysis,
		    final List<String> headersOnSelectedAccessArea,
		    final int duration)
	{
		List<List<String>> resultantDataAsList = new ArrayList<List<String>>();
        final String successStatus = "success";
		HashSet<List<String>> allAccessAreaData = new HashSet<List<String>>();
		final int accessAreaIndex = 0;
		final int controllerIndex = 1;
		final int ranVendorIndex = 2;
		final int ratIndex = 3;
		for( final List<String> rowData : csvDataAsList)
		{
			List<String> accessAreaData = new ArrayList<String>();
			final String accessArea = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(csvHeader));
			final String controller = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.CONTROLLER));
			final String ranVendor = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.RAN_VENDOR));
			final String rat = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.RAT));
			if ( !accessArea.equalsIgnoreCase("") &&
				 !controller.equalsIgnoreCase("") &&
				 !ranVendor.equalsIgnoreCase("") &&
				 !rat.equalsIgnoreCase(""))
			{
				accessAreaData.add(accessArea);
				accessAreaData.add(controller);
				accessAreaData.add(ranVendor);
				accessAreaData.add(rat);
				allAccessAreaData.add(accessAreaData);			
			}
		}
		logger.log(Level.INFO,"The Access Areas are :" + allAccessAreaData.toString());
		
		for(final List<String> singleAccessAreaData : allAccessAreaData)
		{
			List<String> modifiedList = new ArrayList<String>();
			int failureCount = 0;
			int successCount = 0;
			final String accessAreaUI = singleAccessAreaData.get(accessAreaIndex);
			final String controllerUI = singleAccessAreaData.get(controllerIndex);
			final String ranVendorUI = singleAccessAreaData.get(ranVendorIndex);
			final String ratUI = singleAccessAreaData.get(ratIndex);
			for(final List<String> rowData : csvDataAsList)
			{
				final String accessAreaCSV = rowData.get(GeneralUtil.
				             getReservedCSVDataHeaderIndex(csvHeader));
				final String ranVendorCSV = rowData.get(GeneralUtil.
			             getReservedCSVDataHeaderIndex(
			            		 GuiStringConstants.RAN_VENDOR));
                final String controllerCSV = rowData.get(GeneralUtil.
	 		             getReservedCSVDataHeaderIndex(
			            		 GuiStringConstants.CONTROLLER));
    			final String ratCSV = rowData.get(GeneralUtil.
			             getReservedCSVDataHeaderIndex(
			            		 GuiStringConstants.RAT));
				final String status = rowData.get(GeneralUtil.
			             getReservedCSVDataHeaderIndex(
			            		 GuiStringConstants.EVENT_RESULT));
				if ( accessAreaUI.equalsIgnoreCase(accessAreaCSV) &&
						controllerUI.equalsIgnoreCase(controllerCSV) &&
						ranVendorUI.equalsIgnoreCase(ranVendorCSV) &&
						ratUI.equalsIgnoreCase(ratCSV))
				{
					if ( typeOfSubcriberAnalysis.equalsIgnoreCase(status))
					{
						failureCount++;						
					}
					if ( successStatus.equalsIgnoreCase(status) )
					{
						successCount++;						
					}
				}
			}
			for(final String header : headersOnSelectedAccessArea)
			{
				if( header.equalsIgnoreCase(GuiStringConstants.RANK))
				{
					modifiedList.add(DataIntegrityConstants.NO_RANK_ASSIGNED);	
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.RAT))
				{
					modifiedList.add(ratUI);
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.RAT_ID))
				{
					final int ratID = GeneralUtil.getRATID(ratUI);
					modifiedList.add(Integer.toString(ratID));
				}
				else if(header.equalsIgnoreCase(
						GuiStringConstants.ACCESS_AREA))
				{
					modifiedList.add( accessAreaUI );
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.RAN_VENDOR))
				{
					modifiedList.add( ranVendorUI );
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.CONTROLLER))
				{
					modifiedList.add( controllerUI );
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.FAILURES))
				{
	    			modifiedList.add(Integer.toString(failureCount * duration));
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.SUCCESSES))
				{
					modifiedList.add(Integer.toString(successCount * duration));
				}
			}			
			resultantDataAsList.add(modifiedList);
		}
		logger.log(Level.INFO,"Access Area Ranking Data :" + resultantDataAsList.toString());
		return resultantDataAsList;
	}

	final static boolean verifyAscendingOrderSortData(
			final List<String> uiColumnDataBeforeSort,
			final List<String> uiColumnDataAfterSort) 
	{
		boolean isSortDataValid = false;
		final List<String> ascendingOrderSortedData = 
			GeneralUtil.ascendingOrderSort(uiColumnDataBeforeSort);
	    logger.log(Level.INFO,"Before Sort: " + uiColumnDataBeforeSort);
	    logger.log(Level.INFO,"After Sort: " + uiColumnDataAfterSort);
	    logger.log(Level.INFO,"Result Sort Order: " + ascendingOrderSortedData);
		if( ascendingOrderSortedData.size() == uiColumnDataAfterSort.size())
		{
			for ( int i = 0; i < ascendingOrderSortedData.size(); i++)
			{
				if( ascendingOrderSortedData.get(i).equalsIgnoreCase(uiColumnDataAfterSort.get(i)))
				{
					isSortDataValid = true;
				}
				else
				{
					isSortDataValid = false;
					break;
				}
			}			
		}
        return isSortDataValid;
	}

	final static boolean verifyDescendingOrderSortData(
			final List<String> uiColumnDataBeforeSort,
			final List<String> uiColumnDataAfterSort) 
	{
		boolean isSortDataValid = false;
	    List<String> descendingOrderSortedData = 
	    	GeneralUtil.descendingOrderSort(uiColumnDataBeforeSort);
	    logger.log(Level.INFO,"Before Sort: " + uiColumnDataBeforeSort);
	    logger.log(Level.INFO,"After Sort: " + uiColumnDataAfterSort);
	    logger.log(Level.INFO,"Result Sort Order: " + descendingOrderSortedData);
		if( descendingOrderSortedData.size() == uiColumnDataAfterSort.size())
		{
			for ( int i = 0; i < descendingOrderSortedData.size(); i++)
			{
				if( descendingOrderSortedData.get(i).equalsIgnoreCase(uiColumnDataAfterSort.get(i)))
				{
					isSortDataValid = true;
				}
				else
				{
					isSortDataValid = false;
					break;
				}
			}			
		}
	    logger.log(Level.INFO,"After Sort: " + uiColumnDataAfterSort);
        return isSortDataValid;
	}

	
	/*
	 * This method is to sort the List<List<String>> values in ascending.
	 * Each and every List<String> failure value is taken and the sorting is 
	 * done for List<List<String>> based on it.
	 * 
	 * @param analysisDataBasedOnCSV - Ranking analysis data.  
	 * @param defaultRankingHeaders - The ranking headers of the ranking analysis 
     *                                selected.
     * 
     *  @return sortedAnalysisData - Sorted data based on failures.
	 * 
	 */
	
	final static List<List<String>> sortAscendingOnFailures(
			final List<List<String>> analysisDataBasedOnCSV,
			final List<String> defaultRankingHeaders, 
			final String sortBasedOnField) 
	{
		final int uiFailureIndex = defaultRankingHeaders
				.indexOf(sortBasedOnField);
		List<List<String>> sortedAnalysisData = new ArrayList<List<String>>();
		sortedAnalysisData = analysisDataBasedOnCSV;
//		logger.log(Level.INFO, " The modified list is " + sortedAnalysisData.size());
		for (int upperIndex = sortedAnalysisData.size() - 1; upperIndex > 0; 
		upperIndex--) 
		{
			for (int lowerIndex = 0; lowerIndex < upperIndex; lowerIndex++) 
			{
				final List<String> firstRow = sortedAnalysisData
						.get(lowerIndex);
				final List<String> secondRow = sortedAnalysisData
						.get(lowerIndex + 1);
				final String firstRowValue = firstRow.get(uiFailureIndex);
				final String secondRowValue = secondRow.get(uiFailureIndex);
				if (Integer.parseInt(firstRowValue) > Integer
						.parseInt(secondRowValue)) {
					sortedAnalysisData.set(lowerIndex, secondRow);
					sortedAnalysisData.set(lowerIndex + 1, firstRow);
				}
			}
		}
//		logger.log(Level.INFO,"The Sorted List :" + sortedAnalysisData.toString());
		return sortedAnalysisData;
	}

	/*
	 * This method is to assign the ranks for the sorted List<List<String>> 
	 * passed.
	 * 
	 * @param orderedListByFailures - Sorted Ranking analysis data based on the 
	 *                                failure.  
	 * @param defaultRankingHeaders - The ranking headers of the ranking 
	 *                                analysis selected. 
     * 
     * @return sortedAnalysisData - Sorted data based on failures.
	 */
	
	final static List<List<String>> assigningRanks(
			final List<List<String>> orderedListByFailures,
			final List<String> defaultRankingHeaders,
			final String SortAndRankBasedOnField) 
    {
		int iteration = 0;
		List<List<String>> rankedList = new ArrayList<List<String>>();
		final int uiFailureIndex = defaultRankingHeaders
				.indexOf(SortAndRankBasedOnField);
		final int uiRankIndex = defaultRankingHeaders
				.indexOf(GuiStringConstants.RANK);

		for (int index = orderedListByFailures.size() - 1; index >= 0; index--)
		{
			boolean sameNumberOfFailures = false;
			final int currentIndex = index;
			final int previousIndex = index + 1;
			final List<String> currentIndexData = orderedListByFailures
					.get(currentIndex);
//			logger.log(Level.INFO,"The first value :" + currentIndexData);
			iteration++;
			if (currentIndex < orderedListByFailures.size() - 1) 
			{
				final List<String> previousIndexData = orderedListByFailures
						.get(previousIndex);
				if (Integer.parseInt(currentIndexData.get(uiFailureIndex)) == 
					   Integer.parseInt(previousIndexData.get(uiFailureIndex))) 
				{
					sameNumberOfFailures = true;
					currentIndexData.set(uiRankIndex,
							previousIndexData.get(uiRankIndex));
				}
			}
			if (!sameNumberOfFailures) 
			{
				currentIndexData.set(uiRankIndex, Integer.toString(iteration));
			}
			rankedList.add(currentIndexData);
		}
		logger.log(Level.INFO,"The Ranking Analysis Data Based On CSV :" + rankedList);
		return rankedList;
	}
	/*
	 * This method is to get the different controllers based on the CSV file. 
	 * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
	 * @param isRATConsidered  - The ranking headers of the ranking 
	 *                                analysis selected. 
     * @param controllerType   - Type of controller ie. RNC/BSC
     * 
     * 
     * @return completeControllerList - Contains the controllers list.
	 */
	
	final private static List<String> getCompleteControllerRankingList(final String controllerType, final String ranVendor,
	  final String rncName, final int ericcssonFailureCount, 
	  final int nonEriccssonFailureCount,final int ericssonSuccessCount,
	  final int nonEricssonSuccessCount, final String rat, 
	  final String ratID, final int duration, final List<String> defaultRankingHeaders) 
    {
		List<String> controllerData = new ArrayList<String>();
		for (final String header : defaultRankingHeaders) 
		{
		if (header.equalsIgnoreCase(GuiStringConstants.RANK)) 
		{
			controllerData.add(DataIntegrityConstants.NO_RANK_ASSIGNED);
		} 
		else if (header.equalsIgnoreCase(GuiStringConstants.RAN_VENDOR)) 
		{
			controllerData.add(ranVendor);
		}
		else if (header.equalsIgnoreCase(controllerType)) 
		{
			controllerData.add(rncName);
		} 
		else if (header.equalsIgnoreCase(GuiStringConstants.FAILURES)) 
		{
			if (ranVendor.equalsIgnoreCase("ERICSSON")) 
			{
				controllerData.add(Integer.toString(ericcssonFailureCount
						* duration));
			}
			else
			{
				controllerData.add(Integer.toString(nonEriccssonFailureCount
						* duration));
			}
		} 
		else if (header.equalsIgnoreCase(GuiStringConstants.SUCCESSES)) 
		{
			if (ranVendor.equalsIgnoreCase("ERICSSON")) 
			{
				controllerData.add(Integer.toString(ericssonSuccessCount
						* duration));
			}
			else
			{
				controllerData.add(Integer.toString(nonEricssonSuccessCount
						* duration));
			}
		} 
		else if (header.equalsIgnoreCase(GuiStringConstants.RAT)) 
		{
			controllerData.add(rat);
		}
		else if (header.equalsIgnoreCase(DataIntegrityConstants.RAT_ID)) 
		{
			controllerData.add(ratID);
		}
		}
		return controllerData;
	}

    /* This method is to identify the data for the subsciber ranking based 
     * on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param rankingAnalysisfield       - What analysis performed.
     * @param defaultRankingHeaders      - The ranking headers of the ranking
     *                                     analysis selected.
     * @param analysisType - The type of analysis selected ie.blocked/Dropped 
     * @param duration     - Time framed selected.
     *  
     * @return allIMSIRankingData - Contains the subscriber ranking analysis 
     *                              data.  
     */
	
	final static List<List<String>> getRankingForUnansweredCalls( 
			final List<List<String>> mssVoiceRelatedCsvDataList,
		    final String rankingAnalysisfield, final String analysisType,
		    final List<String> defaultRankingHeaders, final int duration)
	{
		List<List<String>> allIMSIRankingData = new ArrayList<List<String>>();
		HashSet<List<String>> completeIMSIList = new HashSet<List<String>>();
		final int imsiIndex = 0;
		final int callingPartyIndex = 1;
		for( final List<String> rowData : mssVoiceRelatedCsvDataList)
		{
			ArrayList<String> imsiData = new ArrayList<String>();
			final String imsi = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					GuiStringConstants.IMSI));
			final String callingPartyNumber = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(
					GuiStringConstants.CALLING_PARTY_NUMBER));
			if(!imsi.equalsIgnoreCase("") && !callingPartyNumber.equalsIgnoreCase(""))
			{   final String eventType = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					GuiStringConstants.EVENT_TYPE));
				if ( eventType.equalsIgnoreCase(analysisType))
				{
					imsiData.add(rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.IMSI)));
					imsiData.add(rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.CALLING_PARTY_NUMBER)));
					completeIMSIList.add(imsiData);;					
				}
			}
		}
		logger.log(Level.INFO, "The Unanswered IMSI Data :" + 
				completeIMSIList.toString());		
		for(final List<String> singleImsi : completeIMSIList)
		{
			int unansweredCallCount = 0;
			List<String> singleIMSIData = new ArrayList<String>();
			final String imsi = singleImsi.get(imsiIndex);
			final String callingPartyNumber = singleImsi.get(callingPartyIndex);
			for(final List<String> rowData : mssVoiceRelatedCsvDataList)
			{
				final String imsiCSV = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.IMSI));
				final String callingPartyNumberCSV = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.CALLING_PARTY_NUMBER));
				final String callPositionCSV = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.CALL_POSITION));
				final String disconnectingPartyCSV = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.DISCONNECTING_PARTY));
				final String internalCauseCodeIDCSV = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.INTERNAL_CAUSE_CODE_ID));
				final String eventType = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.EVENT_TYPE));				
				if ( imsi.equalsIgnoreCase(imsiCSV) && 
					callingPartyNumber.equalsIgnoreCase(callingPartyNumberCSV) &&
					eventType.equalsIgnoreCase(analysisType))
				{
					if ( callPositionCSV.equalsIgnoreCase(
							DataIntegrityConstants.CALL_POSITION_FOR_UNANSWERED_CALL))
					{
						if (disconnectingPartyCSV.equalsIgnoreCase(
								DataIntegrityConstants.DISCONNECTED_PARTY_ZERO_FOR_UNANSWERED_CALL)
								|| disconnectingPartyCSV.equalsIgnoreCase(
								DataIntegrityConstants.DISCONNECTED_PARTY_ONE_FOR_UNANSWERED_CALL))
						{
							if ( internalCauseCodeIDCSV.equalsIgnoreCase(
									DataIntegrityConstants.INTERNAL_CAUSE_CODE_THREE_FOR_UNANSWERED_CALL) ||
									internalCauseCodeIDCSV.equalsIgnoreCase(
									DataIntegrityConstants.INTERNAL_CAUSE_CODE__FOUR_FOR_UNANSWERED_CALL) ||
									internalCauseCodeIDCSV.equalsIgnoreCase(
								    DataIntegrityConstants.INTERNAL_CAUSE_CODE__SIX_FOR_UNANSWERED_CALL))
							{
							   unansweredCallCount++;	
							}

						}
					}
				}
			}
			for(final String header : defaultRankingHeaders)
			{
				if( header.equalsIgnoreCase(GuiStringConstants.RANK))
				{
					singleIMSIData.add(DataIntegrityConstants.NO_RANK_ASSIGNED);	
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.IMSI))
				{
					singleIMSIData.add(imsi);
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.CALLING_PARTY_NUMBER))
				{
					singleIMSIData.add(callingPartyNumber);
				}
				else if(header.equalsIgnoreCase(GuiStringConstants.UNANSWERED_CALLS))
				{
					singleIMSIData.add(Integer.toString(unansweredCallCount * duration));
				}
			}
			allIMSIRankingData.add(singleIMSIData);
		}
		logger.log(Level.INFO, "Unanswered Call Ranking Analysis Data :" + 
				allIMSIRankingData.toString());
		return allIMSIRankingData;
	}

	/*
	 * This method is to get the different controllers based on the CSV file. 
	 * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
	 * @param isRATConsidered  - The ranking headers of the ranking 
	 *                                analysis selected. 
     * @param controllerType   - Type of controller ie. RNC/BSC
     * 
     * 
     * @return completeControllerList - Contains the controllers list.
	 */
	
	final private static HashSet<List<String>> getCompleteControllerList(
			final List<List<String>> mssVoiceRelatedCsvDataList,
			final boolean isRATConsidered, final String controllerType) 
	{
		HashSet<List<String>> completeControllerList = 
			new HashSet<List<String>>();
		for (final List<String> rowData : mssVoiceRelatedCsvDataList) 
		{
			final String ranVendor = rowData.get(GeneralUtil
				    .getReservedCSVDataHeaderIndex(
				    		GuiStringConstants.RAN_VENDOR)).trim();
			final String controller = rowData
					.get(GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.CONTROLLER)).trim();
			final String rat = rowData
					.get(GeneralUtil.getReservedCSVDataHeaderIndex(
							GuiStringConstants.RAT)).trim();
			final String ratID = Integer.toString(GeneralUtil.getRATID(rat));
			if (controller.contains(controllerType))
			{
				if (ranVendor.equalsIgnoreCase("ERICSSON")) 
				{
					List<String> ericssonMSC = new ArrayList<String>();
					ericssonMSC.add(controller);
					ericssonMSC.add(ranVendor);
					if (isRATConsidered) 
					{
						ericssonMSC.add(rat);
						ericssonMSC.add(ratID);
					}
					completeControllerList.add(ericssonMSC);
				} 
				else
				{
					List<String> nonEricssonMSC = new ArrayList<String>();
					nonEricssonMSC.add(controller);
					nonEricssonMSC.add(ranVendor);
					if (isRATConsidered) {
						nonEricssonMSC.add(rat);
						nonEricssonMSC.add(ratID);
					}
					completeControllerList.add(nonEricssonMSC);
				}
			}
		}
		return completeControllerList;
	    }

	}

