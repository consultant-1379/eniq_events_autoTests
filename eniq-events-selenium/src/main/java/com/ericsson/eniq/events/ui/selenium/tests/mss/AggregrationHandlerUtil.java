package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author eanggop
 * @since 2011 
 *
 */ 

public final class AggregrationHandlerUtil 
{  
    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

	private static List<String> MsgTypeIndicatorResults = Arrays.asList(
			DataIntegrityConstants.SMS_DELIVER_SC_TO_MS,
			DataIntegrityConstants.SMS_DELIVEREPORT_MS_TO_SC,
			DataIntegrityConstants.SMS_STATUS_REPORT_SC_TO_MS,
			DataIntegrityConstants.SMS_COMMAND_MS_TO_SC,
			DataIntegrityConstants.SMS_SUBMIT_MS_TO_SC,
			DataIntegrityConstants.SMS_SUBMIT_REPORT_SC_TO_MS,
			DataIntegrityConstants.RESERVED_MTI_VALUE);

	private static List<String> MsgTypeIndicatorID = Arrays.asList("0", "1", "2", "3", "4","5");
	
	private static List<String> SMSResults = Arrays
			.asList(DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_CAMELREASON,
					DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_OTHERREASON,
					DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_CAMELREASON,
					DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_OTHERREASON,
					DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSCDUE_TO_RTCFAREASON,
					DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_RTCFAREASON);

	private static List<String> SMSResultsID = Arrays.asList("0", "1", "2", "3", "4","5","6");

	/* This method will validate whether the events shown in UI are in validate
	 * aggregration range.
	 * 
	 * @param initialDate - The first event time on UI. 
	 * @param lastDate - The last event time on UI. 
	 * @param systemDate - The time at which aggregration intiated. 
	 * @param duration - Aggregration duration selected on the UI.
	 * 
	 * @return AggregrationResult - Results true if the events are within the
	 * aggregration range else returns false.
	 */

	final static boolean validateAggregration(final String InitialDate,
			final String LastDate, final String systemDate, final int duration) {
		boolean AggregrationResult = false;
		final String systemTimeOnSelection = systemDate;
		final String firstEventTime = InitialDate;
		final String lastEventTime = LastDate; 	

    	logger.log(Level.INFO, "The Aggregration Intiated time is : "
				+ systemTimeOnSelection);
    	logger.log(Level.INFO, "The First Event time is : " + firstEventTime);
    	logger.log(Level.INFO, "The Last Event time is : " + lastEventTime);
		AggregrationResult = compareTimes(systemTimeOnSelection,
				firstEventTime, lastEventTime, duration);
		return AggregrationResult;
	}

	/* This method will validate whether the first event time and the last event
	 * time shown in UI is within the aggregration time selected.
	 * 
	 * For Eg: If the Aggregration is triggered at 12:30 for 5 minutes then the
	 * events to be shown in UI has to be within 5 minutes ie. From 12:25 to
	 * 12:29 if not then there is an issue.
	 * 
	 * Same check is done for 15 minutes,30 minutes,1 hour,6 hour,12 hour,1
	 * day,1 week.
	 */
	  
	final static boolean compareTimes(final String InitialDate,
			final String LastDate, final String systemDate, final int duration) 
	{
		boolean allEventsWithinTimeFrame = false;
		GregorianCalendar date = new GregorianCalendar();
		Date currentDate = date.getTime();
		logger.log(Level.INFO, currentDate.toString());
		long currentTime = currentDate.getTime(); // gets time in milliseconds
		long startDateTime = currentTime - (30 * 60 * 1000);
		date.setTimeInMillis(startDateTime);
		Date aggregrationStartDate = date.getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		try 
		{
			final Date firstEventDateOnUI = dateFormatter.parse(InitialDate);
			final Date lastEventDateOnUI = dateFormatter.parse(LastDate);
			logger.log(Level.INFO, "currrent Date " + currentDate
					+ "& firstEventTime " + firstEventDateOnUI);
			logger.log(Level.INFO, "currrent Time " + aggregrationStartDate
					+ "& lastEventTime " + lastEventDateOnUI);
			final boolean isStartTimeValid = lastEventDateOnUI
					.after(aggregrationStartDate);
			final boolean isEndTimeValid = currentDate
					.after(firstEventDateOnUI);
			System.out.println("current time is after first event Time on UI :"
					+ isEndTimeValid);
			System.out.println("Last event time is after the Aggre start time "
					+ isStartTimeValid);
			if (isStartTimeValid && isEndTimeValid) 
			{
				allEventsWithinTimeFrame = true;
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		logger.log(Level.INFO, "Is all events are within the aggregration time :" + allEventsWithinTimeFrame);
		return allEventsWithinTimeFrame;
	}   	
      

    /* This method is to find whether the data diplayed on UI contains
     * exact information as reserved data.  
     * 
     *@param completeUITableValues - contains the data displayed in UI.
     *@param windowHeaders         - contains which header
     *                               fields needs to be validated.
     *@param completeUITableValues - contains the data displayed in UI.
     *@param analysisTypeSelected  - contains which analysis selected.
     *@param selectedFieldValue    - Value of the field selected.
     *@param eventType             - Event type.
     *@return isTestPassed  - Returns whether the data are valid or not. 
     */
	
	final static boolean getAggregrationResult( 
			final List<Map<String, String>> completeUITableValues,
			final List<String> windowHeaders,
			final String analysisTypeSelected,
			final String selectedFieldValue, final String eventType) 
	{
		boolean isTestPassed = false;
		boolean isAllFieldMatches = false;
	    int countUI = 0;

		try {
			List<List<String>> csvDataAsList = getCSVDataForSubscriberTab( analysisTypeSelected,
					selectedFieldValue, eventType );
			for (Map<String, String> uiTableRowValue : completeUITableValues) 
			{
				countUI++;
					final String uiEventTime = uiTableRowValue.get(GuiStringConstants.EVENT_TIME).toString();
					final String uiEventTimeSeconds = GeneralUtil.getExactDateFormat(uiEventTime);
					logger.log(Level.INFO, "The UI Event time in seconds :" + uiEventTimeSeconds);
					for (final List<String> csvRead : csvDataAsList) 
					{
						final String csvEventTime = csvRead.get(GeneralUtil
								.getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_TIME));
						String csvEventTimeSeconds = GeneralUtil
								.getExactDateFormat(csvEventTime);
						if (uiEventTimeSeconds.endsWith(csvEventTimeSeconds)) 
						{
							for (String validationFields : 
								windowHeaders)
							{
								final int validationEventNameIndex = GeneralUtil
										.getReservedCSVDataHeaderIndex(validationFields);
								if (validationEventNameIndex != -1)
								{
									String csvDataValue = csvRead
											.get(validationEventNameIndex);
									String uiDataValue = uiTableRowValue.get(
											validationFields).toString();
									if (validationEventNameIndex == GeneralUtil
											.getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_TIME)
											|| validationEventNameIndex == GeneralUtil
											.getReservedCSVDataHeaderIndex(GuiStringConstants.SEIZURE_TIME)) 
									{
										csvDataValue = GeneralUtil
												.getExactDateFormat(csvDataValue);
										uiDataValue = GeneralUtil
												.getExactDateFormat(uiDataValue);
									}
									if (uiDataValue.equalsIgnoreCase(csvDataValue)) 
									{
										isAllFieldMatches = true;
									}
									else if( validationFields.equalsIgnoreCase(GuiStringConstants.SMS_RESULT) ||
											validationFields.equalsIgnoreCase(GuiStringConstants.SMS_RESULT_ID) ||
											validationFields.equalsIgnoreCase(GuiStringConstants.MSG_TYPE_IND) ||
											validationFields.equalsIgnoreCase(GuiStringConstants.MSG_TYPE_IND))
									{
										isAllFieldMatches = validationOnSMSEvents(validationFields, uiDataValue);
										if (!isAllFieldMatches)
										{
											logger.log(Level.INFO, "The CSV Field : " + 
													 validationFields + " , value is :"
													+ csvDataValue + " and index is :"
													+ validationEventNameIndex);
											logger.log(Level.INFO, "The UI Field  :"
													+ validationFields + " value is :"
													+ uiDataValue);
											break;
										}
									}
									else 
									{
										logger.log(Level.INFO, "The CSV Field : " + 
												 validationFields + " , value is :"
												+ csvDataValue + " and index is :"
												+ validationEventNameIndex);
										logger.log(Level.INFO, "The UI Field  :"
												+ validationFields + " value is :"
												+ uiDataValue);
										isAllFieldMatches = false;
										break;
									}
								}				
							}

							if (isAllFieldMatches) 
							{
								isTestPassed = true;
								break;
							} 
							else if (!isAllFieldMatches) 
							{
								isTestPassed = false;
							}						
						}
					}
					if ( !isTestPassed )
					{
						logger.log(Level.INFO, " The Test is Failed at row " + countUI );
						break;
					}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
        if ( isTestPassed )
        {
			logger.log(Level.INFO, "The Test is passed for all the " + countUI + " rows ");
        }
		return isTestPassed;
	}
	
	/*
	 * This method will get all required information for the corresponding
	 * field under subscriber. For Eg: All information regarding IMSI, 
	 * ACCESS AREA etc.
	 * 
	 * @param typeOfAnalysisSelected - Analysis Field Name. 
	 * @param selectedFieldValue - Corresponding Field Value.
	 * @param eventType - Event type.
	 * 
	 * @return validCSVDataList - contains information corresponding to analysis
	 * field.
	 */
	final static List<List<String>> getCSVDataForSubscriberTab(
			final String typeOfAnalysisSelected,
			final String selectedFieldValue, final String eventType) 
	{
		List<List<String>> csvDataAsList = GeneralUtil.getCSVDataValues();
		List<List<String>> validCSVDataList = new ArrayList<List<String>>();
            final int csvDataHeaderIndex = GeneralUtil.
            	getReservedCSVDataHeaderIndex(typeOfAnalysisSelected);
            final List<String> firstRowValueOfCSV = csvDataAsList.get(0);
            final String valueOfCSVHeaderSelected = 
            	firstRowValueOfCSV.get(csvDataHeaderIndex);
            final int indexValueToPick = 
            	splitAndFindIndex(valueOfCSVHeaderSelected, selectedFieldValue);
            validCSVDataList = getValidCSVData(indexValueToPick , csvDataAsList, eventType,
            		csvDataHeaderIndex, selectedFieldValue);
            logger.log(Level.INFO, "The Required CSV Data for the " + 
            		typeOfAnalysisSelected + " is " + validCSVDataList.toString());
        return validCSVDataList;
	}
	


	/* This method is required only depends on the csv format.
	 * For Example if two imsi values are kept in the format
	 * 460000123456790-460000123456791 then all corresponding
	 * values will be seperated with hypen '-'.
	 * 
	 * In this case this method will split the string by hypen 
	 * and returns the index which matches the imsi value passed.
	 * 
	 * @param fieldValue     - value of the field.
	 * @param valueToCompare - The value to be compared.
	 *  
	 */
	
	final static int splitAndFindIndex( final String fieldValue, 
			final String valueToCompare)
	{
		int matchingIndex = 0;
		if ( fieldValue != null && fieldValue.contains(
				DataIntegrityConstants.HYPHEN_SYMBOL))
		{
			final String[] splitValue = fieldValue.split(
					DataIntegrityConstants.HYPHEN_SYMBOL);
			for( int i=0 ; i < splitValue.length; i++)
			{
				if( splitValue[i].equalsIgnoreCase(valueToCompare))
				{
					matchingIndex = i;
				}
			}
		}
		return matchingIndex;
	}

	
	/* This method is to return the exact IMSI, TAC, RAT, CONTROLLER, 
	 * ACCESSAREA, MSC values for the validation which all other 
	 * corresponding values.
	 * 
	 * @param indexValueToPick - Index of the value to be considered.
	 * @param csvDataAsList - The CSV data list.
	 * @param eventType - event type
	 * @param csvDataHeaderIndex - CSV header index.
	 * @param selectedFieldValue - Value of selected field.
	 * @retrun resultantDataAsList - modified csv data for validation.
	 */
	final static List<List<String>> getValidCSVData( 
			final int indexValueToPick, final List<List<String>> csvDataAsList,
			final String eventType, final int csvDataHeaderIndex,
			final String selectedFieldValue)
	{
		List<List<String>> resultantDataAsList = new ArrayList<List<String>>();

			List<String> modifiedList = new ArrayList<String>(); 
			for( final List<String> listValue : csvDataAsList)
			{
				modifiedList = listValue;
				if ( indexValueToPick > 0)
				{
					final String imsi = listValue.get(DataIntegrityConstants.CSV_IMSI_INDEX);
					final String tac = listValue.get(DataIntegrityConstants.CSV_TAC_INDEX);
					final String rat = listValue.get(DataIntegrityConstants.CSV_RAT_INDEX);
					final String controller = listValue.get(DataIntegrityConstants.CSV_CONTROLLER_INDEX);
					final String accessArea = listValue.get(DataIntegrityConstants.CSV_ACCESSAREA_INDEX);
					final String msc = listValue.get(DataIntegrityConstants.CSV_MSC_INDEX);
					final String callDuration = 
						listValue.get(DataIntegrityConstants.CSV_CALLDURATION_INDEX);
					final String[] imsiValues = imsi.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] tacValues = tac.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] ratValues = rat.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] controllerValues = 
						controller.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] accessAreaValues = 
						accessArea.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] mscValues = msc.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					final String[] callDurationValues = 
						callDuration.split(DataIntegrityConstants.HYPHEN_SYMBOL);
					modifiedList.set(DataIntegrityConstants.CSV_IMSI_INDEX, imsiValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_TAC_INDEX, tacValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_RAT_INDEX, ratValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_CONTROLLER_INDEX, 
							controllerValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_ACCESSAREA_INDEX,
							accessAreaValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_MSC_INDEX,
							mscValues[indexValueToPick]);
					modifiedList.set(DataIntegrityConstants.CSV_CALLDURATION_INDEX, 
							callDurationValues[indexValueToPick]);
				}
				
				if ( modifiedList.get(csvDataHeaderIndex).equalsIgnoreCase(
						selectedFieldValue))
				{
					if( eventType.equalsIgnoreCase("NOT_SPECIFIC"))
					{
						resultantDataAsList.add(modifiedList);					
					}
					else if( eventType.equalsIgnoreCase(
							listValue.get(DataIntegrityConstants.CSV_EVENTTYPE_INDEX)) )
					{
						resultantDataAsList.add(modifiedList);
					}					
				}
			}		

		return resultantDataAsList;
	}

	/* This method is to find the event analysis data based on csv and
	 * compares whether it matches with the UI or not.
     *
     *@param completeUITableValues - contains the data displayed in UI.
     *@param allEventTypes         - All event types.
     *@param windowHeaders         - contains which header
     *                               fields needs to be validated.
     *@param analysisTypeSelected  - contains which analysis selected.
     *@param selectedFieldValue    - Value of the field selected.
     *@param eventType             - Event type.
     *@return resultOfAnalysis     - Returns whether the data are valid 
     *                               or not. 
     */
	
	final public static boolean eventAnalysis( 
			final List<Map<String, String>> completeUITableValues,
			final List<String> allEventTypes,
			final String typeOfAnalysisSelected,
			final String selectedFieldValue,
			final String eventType,
			final List<String> WindowHeaders,
			final int duration) 
	{		
		List<List<String>> csvDataAsList = getCSVDataForSubscriberTab( 
				typeOfAnalysisSelected,	selectedFieldValue, eventType );
	    List<List<String>> failureList = new ArrayList<List<String>>();
	    for(final String event : allEventTypes )
	    {
		    final List<String> completeData = getEventAnalysisOnSubscriberTab(
		    		csvDataAsList, event, duration, WindowHeaders);
		    if ( !completeData.contains(DataIntegrityConstants.NO_DATA))
		    {
			    failureList.add(completeData);		    	
		    }		
	    }
	    logger.log(Level.INFO, "The failure list is :" + failureList);
	    final boolean resultOfAnalysis = 
	    	(GeneralUtil.formatUIFailureAnalysisData(
	    		completeUITableValues,WindowHeaders)).containsAll(failureList);
	    logger.log(Level.INFO, "The result of failure event analysis is : " + 
	    		resultOfAnalysis);
	    return resultOfAnalysis;
	}

    /* This method is get the required event analysis data for IMSI,
     * TAC, MSC, ACCESS AREA, CONTROLLER.
     * 
     * @param csvDataAsList - contains the data of csv.
     * @param eventType     - Type of Event
     * @param minutes       - duration for which the data is required.
     * @param windowHeaders - contains which header
     *                        fields needs to be validated.
     *                        
     * @return eventAnalysisList - contains all corresponding event 
     *                             analysis data based on csv.
     */
	static List<String> getEventAnalysisOnSubscriberTab(
			final List<List<String>> csvDataAsList, final String eventType,
			final int minutes, final List<String> windowHeaders)
	{
        int failureEventCount = 0;
        int successEventCount = 0;
        double successRatio = 0.0;
        boolean eventTypeMatches = false;
        int totalEventsCount = 0;
        int impactedSubscribers = 0;
        HashSet<String> impactedUser = new HashSet<String>();
        final int headerSize = windowHeaders.size();
        List<String> eventAnalysisList = new ArrayList<String>(headerSize);
        List<String> firstMatchingRowData = new ArrayList<String>(); 
        for(int i=0 ; i<headerSize; i++)
        {
        	eventAnalysisList.add(DataIntegrityConstants.NO_DATA);
        }
		for( final List<String> rowData : csvDataAsList)
		{
			final String eventTypeOnCSV = rowData.get(
					GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_TYPE));
			if( eventTypeOnCSV.equalsIgnoreCase(eventType) )
			{	
				firstMatchingRowData = rowData;
				eventTypeMatches = true;
				final String eventResult = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_RESULT));
				final String imsi = rowData.get(
						GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI));
    			if( eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_BLOCKED) ||
    				eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_DROPPED) ||
    				eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_ERROR) )
				{
					failureEventCount++;
				}
				if( eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_SUCCESS) )
				{
					successEventCount++;
				}
				impactedUser.add(imsi);						
			}
		}		
		if ( eventTypeMatches )
		{
			failureEventCount = failureEventCount * minutes;
			successEventCount = successEventCount * minutes;
			totalEventsCount = failureEventCount + successEventCount;
			successRatio = GeneralUtil.getSuccessRatio(
					successEventCount, failureEventCount);
			impactedSubscribers = impactedUser.size();
		}
		int resultOfCSVDataIndexCount=0;
		if ( firstMatchingRowData.size() > 0)
		{
	        for(final String header : windowHeaders)
	        {       	
				if ( header.equalsIgnoreCase(GuiStringConstants.EVENT_TYPE))
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , eventType);
				}
				else if ( header.equalsIgnoreCase(GuiStringConstants.FAILURES))
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , 
							Integer.toString(failureEventCount));
				}
				else if ( header.equalsIgnoreCase(GuiStringConstants.SUCCESSES))
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , 
							Integer.toString(successEventCount));
				}
				else if ( header.equalsIgnoreCase(GuiStringConstants.SUCCESS_RATIO))
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , 
							Double.toString(successRatio));
				}
				else if ( header.equalsIgnoreCase(GuiStringConstants.TOTAL_EVENTS) 
						  || header.equalsIgnoreCase(GuiStringConstants.TOTAL))
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , 
							Integer.toString(totalEventsCount));
				}
				else if ( header.equalsIgnoreCase(GuiStringConstants.IMPACTED_SUBSCRIBERS) )
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount , 
							Integer.toString(impactedSubscribers));
				}
				else
				{
					eventAnalysisList.set(resultOfCSVDataIndexCount, 
							firstMatchingRowData.get(
							GeneralUtil.getReservedCSVDataHeaderIndex(header)));	        		
				}
				resultOfCSVDataIndexCount++;
			}  
		}
		return eventAnalysisList;
	}
	


	/* This method is to format List<hashmap> data format to 
	 * List<List> format.
	 * 
	 * @param completeUITableValues - contains the uiTableData which is in the
	 *                                form of List<hashmap>
	 * @param windowHeaders         - contains the window headers.
	 * 
	 * @return formattedUIDataList  - Contains the formatted data in the form
	 *                                of List<List>. 
	 */
	
	final static boolean validateSMSEvent(
			final List<Map<String,String>> completeUITableValues)
    {
		boolean isTestValid = false;

		final List<String> allSmsResults = new ArrayList<String>(Arrays.asList(
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_CAMELREASON,
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_OTHERREASON,
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_CAMELREASON,
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_OTHERREASON,
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSCDUE_TO_RTCFAREASON,
				DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_RTCFAREASON));
		
		final List<String> allMsgTypeIndicators = new ArrayList<String>(Arrays.asList(
				DataIntegrityConstants.SMS_DELIVER_SC_TO_MS,
				DataIntegrityConstants.SMS_DELIVEREPORT_MS_TO_SC,
				DataIntegrityConstants.SMS_STATUS_REPORT_SC_TO_MS,
				DataIntegrityConstants.SMS_COMMAND_MS_TO_SC,
				DataIntegrityConstants.SMS_SUBMIT_MS_TO_SC,
				DataIntegrityConstants.SMS_SUBMIT_REPORT_SC_TO_MS,
				DataIntegrityConstants.RESERVED_MTI_VALUE));
				
		for (final Map<String, String> rowData : completeUITableValues) 
		{
			final int messageTypeIndicatorID = Integer.parseInt(rowData.get(
					GuiStringConstants.MSG_TYPE_IND_ID));
			final String messageTypeIndicator = rowData.get(
					GuiStringConstants.MSG_TYPE_IND);
			final int smsResultID = Integer.parseInt(rowData.get(
					GuiStringConstants.SMS_RESULT_ID));
			final String smsResult = rowData.get(GuiStringConstants.SMS_RESULT);
			if (messageTypeIndicator.equalsIgnoreCase(GeneralUtil
					.getMsgTypeIndicator(messageTypeIndicatorID))
					&& smsResult.equalsIgnoreCase(GeneralUtil
							.getSmsResult(smsResultID))) 
			{
				if(allMsgTypeIndicators.contains(messageTypeIndicator) && 
						allSmsResults.contains(smsResult))
				{
					isTestValid = true;
				}				
			}
		 }
		return isTestValid;
	 }
	
	/* This method is to validate the fields SMS Result, SMS Result ID, Message Type Indicator,
	 * Message Type Indicator ID of the SMS events.
	 * 
	 * @param validationField - Field to validate 
	 * @param uiValue - The corresponding value for the validating field.
	 * 
	 * @return isValidResult - Returns the validation result.
	 */
	
	final static boolean validationOnSMSEvents(final String fieldName, 
			final String fieldValue)
	{
		boolean isValidResult = false;
		if ( fieldName.equalsIgnoreCase(GuiStringConstants.SMS_RESULT))
		{
			if ( SMSResults.contains(fieldValue))
			{
				isValidResult = true;
			}
		}
		else if ( fieldName.equalsIgnoreCase(GuiStringConstants.SMS_RESULT_ID))
	    {
			if ( SMSResultsID.contains(fieldValue))
			{
				isValidResult = true;
			}	
	    }
		else if ( fieldName.equalsIgnoreCase(GuiStringConstants.MSG_TYPE_IND))
	    {
			if ( MsgTypeIndicatorResults.contains(fieldValue))
			{
				isValidResult = true;
			}	
	    }
		else if ( fieldName.equalsIgnoreCase(GuiStringConstants.MSG_TYPE_IND_ID))
	    {
			if ( MsgTypeIndicatorID.contains(fieldValue))
			{
				isValidResult = true;
			}		
	    }
	    return isValidResult;
	}
	

	/* This method is to find the event analysis data based on csv
	 * for a group selected and compares whether it matches with 
	 * the UI or not.
     *
     *@param completeUITableValues - contains the data displayed in UI.
     *@param allEventTypes         - All event types.
     *@param windowHeaders         - contains which header
     *                               fields needs to be validated.
     *@param analysisTypeSelected  - contains which analysis selected.
     *@param selectedFieldValue    - Value of the field selected.
     *@param eventType             - Event type.
     *@return resultOfAnalysis     - Returns whether the data are valid 
     *                               or not. 
     */
	
	final public static boolean eventAnalysisForGroups( 
			final List<Map<String, String>> completeUITableValues,
			final List<String> allEventTypes,
			final String typeOfAnalysisSelected,
			final List<String> selectedFieldValues,
			final String eventType,
			final List<String> WindowHeaders,
			final int duration) 
	{		
		List<List<String>> csvDataAsList = GeneralUtil.getCSVDataValues();
        final int csvDataHeaderIndex = GeneralUtil.
    	            getReservedCSVDataHeaderIndex(typeOfAnalysisSelected);
		List<List<String>> csvDataForIMSIGroup = getCSVDataForIMSIGroups(
				csvDataAsList, csvDataHeaderIndex, selectedFieldValues, eventType);		
	    List<List<String>> failureList = new ArrayList<List<String>>();
	    for(final String event : allEventTypes )
	    {
		    final List<String> completeData = getEventAnalysisOnSubscriberTab(
		    		csvDataForIMSIGroup, event, duration, WindowHeaders);
		    if ( !completeData.contains(DataIntegrityConstants.NO_DATA))
		    {
			    failureList.add(completeData);		    	
		    }		
	    }
	    logger.log(Level.INFO, "The Analysis Data based on CSV is :" + failureList);
	    final boolean resultOfAnalysis = 
    		(GeneralUtil.formatUIFailureAnalysisData(completeUITableValues,WindowHeaders)).containsAll(failureList);
	    logger.log(Level.INFO, "The result of failure event analysis is : " + resultOfAnalysis);
	    return resultOfAnalysis;
	}

	/* This method is to get the corresponding data from CSV file
	 * for the group selected. 
     *
     *@param csvDataAsList         -  Contains the complete CSV data as
     *                                List.
     *@param analysisTypeSelected  - contains which analysis selected.
     *@param selectedFieldValues   - Values corresponding for the group
     *                               selected.
     *@param eventType             - Event type.
     *@return validCSVDataList     - Contains the corresponding data for 
     *                               the group selected.
     */

	final static List<List<String>> getCSVDataForIMSIGroups(
			final List<List<String>> csvDataAsList,
			final int csvDataHeaderIndex, 
			final List<String> selectedFieldValues,
			final String eventType)
	{
		List<List<String>> validCSVDataList = new ArrayList<List<String>>();
		
		for( final String fieldName : selectedFieldValues)
		{
			for ( final List<String> rowData : csvDataAsList)
			{
				if ( rowData.get(csvDataHeaderIndex).equalsIgnoreCase(
						fieldName))
				{
					if( eventType.equalsIgnoreCase("NOT_SPECIFIC"))
					{
						validCSVDataList.add(rowData);					
					}
					else if( eventType.equalsIgnoreCase(
							rowData.get(DataIntegrityConstants.CSV_EVENTTYPE_INDEX)) )
					{
						validCSVDataList.add(rowData);
					}					
				}		
			}			
		}
		return validCSVDataList;
	}
}
