package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GeneralUtil {
	
    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
    
    public static final int CSV_IMSI_INDEX = 5;    
    public static final int CSV_TAC_INDEX = 6;    
    public static final int CSV_RAT_INDEX = 19;    
    public static final int CSV_CONTROLLER_INDEX = 21;
    public static final int CSV_ACCESSAREA_INDEX = 22;    
    public static final int CSV_MSC_INDEX = 20;    
    public static final int CSV_CALLDURATION_INDEX = 32;    
    public static final int CSV_EVENTTYPE_INDEX = 9;
    public static final int CSV_MCC_INDEX = 45;
    public static final int CSV_MNC_INDEX = 46;
    public static final int CSV_LAC_INDEX = 48;
/*    
    public static final String FILE_PATH_OF_MSSRESERVED_DATA = "E:\\ekumkdn\\ekumkdn_automation_snap_view_1\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources\\MSSReservedData.csv";
    public static final String FILE_PATH_OF_REFERENCE_MSSRESERVED_DATA = "E:\\ekumkdn\\ekumkdn_automation_snap_view_1\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources\\refMSSReservedData.csv";
    public static final String FILE_PATH_OF_COMMON_RESERVED_DATA = "E:\\ekumkdn\\ekumkdn_automation_snap_view_1\\eniq_events\\auto_tests\\eniq-events-selenium\\src\\main\\resources\\reservedData.csv";
    
*/    
//      public static final String FILE_PATH_OF_MSSRESERVED_DATA =     "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/MSSReservedData.csv";
//      public static final String FILE_PATH_OF_REFERENCE_MSSRESERVED_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/refMSSReservedData.csv";
//      public static final String FILE_PATH_OF_COMMON_RESERVED_DATA = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv";


 	  private static final String MSSRESERVED_DATA = "MSSReservedData.csv";
	  private static final String REFERENCE_MSSRESERVED_DATA = "refMSSReservedData.csv";
	  private static final String COMMON_RESERVED_DATA = "reservedData.csv";
	
	  private static String classDirectory=new File(GeneralUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
	  private static String resourcesInDir= classDirectory + File.separator + "resources";
	  private static final String resourcePathOnBlade = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources";
    
	  
	  private static String getResourcePath() {
		  String mssReservedInDir= resourcesInDir + File.separator + MSSRESERVED_DATA;
		  if(new File(resourcePathOnBlade + File.separator + MSSRESERVED_DATA).exists()){
				return resourcePathOnBlade;
			}else if(new File(mssReservedInDir).exists()){
				return resourcesInDir;
			}else{
				logger.severe("Could not find any MSSReservedData.csv files. Returning default");
				return resourcePathOnBlade;
			}
		}
	  
	  private static String FILE_PATH_OF_MSSRESERVED_DATA = getResourcePath() + File.separator + MSSRESERVED_DATA;
	  public static final String FILE_PATH_OF_REFERENCE_MSSRESERVED_DATA = getResourcePath() + File.separator + REFERENCE_MSSRESERVED_DATA;
	  public static final String FILE_PATH_OF_COMMON_RESERVED_DATA = getResourcePath() + File.separator + COMMON_RESERVED_DATA;
	/* This method is to get the data from the csv file and store the data
	 * as list. 
	 * 
	 * @return csvDataAsList - csv file data in a form of list.
	 */
	final static List<List<String>> getCSVDataValues() 
	{
		CsvRead csvRead;
		List<List<String>> csvDataAsList = new ArrayList<List<String>>();
		try
		{
			csvRead = new CsvRead(FILE_PATH_OF_MSSRESERVED_DATA);
			csvRead.skipRecord();
		    while ( csvRead.readRecord())
			{
		    	csvDataAsList.add(Arrays.asList(csvRead.getValues()));
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        catch (IOException e) 
        {
	    	e.printStackTrace();
     	}		
        return csvDataAsList;
	}

    /* This method returns the system time.
     * 
     * @return the system time in the format yyyy-MM-dd HH:mm:ss.
     */
    final static String getSystemTime()
    {
        Calendar firstDAte = Calendar.getInstance();
        SimpleDateFormat dateFormatter = 
        	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateIns = firstDAte.getTime();        	
    	return dateFormatter.format(dateIns);
    }   

    /* This method is used to get the header index from CSV based on the UI 
     * header.
     * This method should hold the mapping only for the reserved data values 
     * not for random data values.
     *
     * @param headerInUI - Holds the header name on UI
     * @return result    - Holds the index of the header on CSV file for the 
     *                     corresponding UI header.
     *
     */
    final static int getReservedCSVDataHeaderIndex(final String headerInUI)
    {
    	int result = -1;    	
    	HashMap<String, Integer> hsmp = new HashMap<String, Integer>();
   	 	
 //   	hsmp.put(GuiStringConstants.EVNTSRC_ID,0);
 //   	hsmp.put(GuiStringConstants.HIER3_ID,1);
 //   	hsmp.put(GuiStringConstants.HIER321_ID,2);
    	hsmp.put(GuiStringConstants.EVENT_TIME,3);
    	hsmp.put(GuiStringConstants.MSISDN,4);
    	hsmp.put(GuiStringConstants.IMSI,5);
    	hsmp.put(GuiStringConstants.TAC,6);
     	hsmp.put(GuiStringConstants.TERMINAL_MAKE,7);
    	hsmp.put(GuiStringConstants.MANUFACTURER,7);
    	hsmp.put(GuiStringConstants.TERMINAL_MODEL,8);
    	hsmp.put(GuiStringConstants.MODEL,8);    	
    	hsmp.put(GuiStringConstants.EVENT_TYPE,9);
    	hsmp.put(GuiStringConstants.EVENT_RESULT,10);
    	hsmp.put(GuiStringConstants.INTERNAL_CAUSE_CODE,11);
    	hsmp.put(GuiStringConstants.INTERNAL_CAUSE_CODE_DESCRIPTION,11);
    	hsmp.put(GuiStringConstants.FAULT_CODE,12);
    	hsmp.put(GuiStringConstants.RECOMMENDED_ACTION,13);
    	hsmp.put(GuiStringConstants.INTERNAL_CAUSE_CODE_ID,14);
    	hsmp.put(GuiStringConstants.FAULT_CODE_ID,15);
  //  	hsmp.put(GuiStringConstants.INTERNAL_LOCATION_CODE,16);
    	hsmp.put(GuiStringConstants.BEARER_SERVICE_CODE,17);
    	hsmp.put(GuiStringConstants.TELE_SERVICE_CODE,18);
    	hsmp.put(GuiStringConstants.RAT,19);
    	hsmp.put(GuiStringConstants.MSC,20);
    	hsmp.put(GuiStringConstants.CONTROLLER,21);
    	hsmp.put(GuiStringConstants.ACCESS_AREA,22);
    	hsmp.put(GuiStringConstants.RAN_VENDOR,23);
  //  	hsmp.put(GuiStringConstants.CALL_ID_NUMBER,24);
  //  	hsmp.put(GuiStringConstants.TYPE_OF_CALLING_SUBSCRIBER,25);
    	hsmp.put(GuiStringConstants.CALLING_PARTY_NUMBER,26);
  //  	hsmp.put(GuiStringConstants.CALLED_PARTY_NUMBER,27);
    	hsmp.put(GuiStringConstants.CALLING_SUBS_IMSI,28);
    	hsmp.put(GuiStringConstants.CALLED_SUBS_IMSI,29);
    	hsmp.put(GuiStringConstants.CALLING_SUBS_IMEI,30);
    	hsmp.put(GuiStringConstants.CALLED_SUBS_IMEI,31);
 //   	hsmp.put(GuiStringConstants.MS_ROAMING_NUMBER,32);
    	hsmp.put(GuiStringConstants.DISCONNECTING_PARTY,33);
//    	hsmp.put(GuiStringConstants.CALL_DURATION,34);
//    	hsmp.put(GuiStringConstants.SEIZURE_TIME,35);
//    	hsmp.put(GuiStringConstants.ORIGINAL_CALLED_NUMBER,36);
  //  	hsmp.put(GuiStringConstants.REDIRECT_NUMBER,37);
  //  	hsmp.put(GuiStringConstants.REDIRECT_COUNTER,38);
    	hsmp.put(GuiStringConstants.REDIRECT_IMSI,39);
    	hsmp.put(GuiStringConstants.REDIRECT_SPN,40);
    	hsmp.put(GuiStringConstants.CALL_POSITION,41);
 //   	hsmp.put(GuiStringConstants.EOS_INFO,42);
 //   	hsmp.put(GuiStringConstants.RECORD_SEQUENCE_NUMBER,43);
 //   	hsmp.put(GuiStringConstants.NETWORK_CALL_REFERENCE,44);
    	hsmp.put(GuiStringConstants.MCC,45);
    	hsmp.put(GuiStringConstants.MNC,46);
    	hsmp.put(GuiStringConstants.RAC,47);
    	hsmp.put(GuiStringConstants.LAC,48);
    	hsmp.put(GuiStringConstants.SMS_RESULT,49);
    	hsmp.put(GuiStringConstants.MSG_TYPE_IND,50);
    	hsmp.put(GuiStringConstants.SMS_RESULT_ID,51);
    	hsmp.put(GuiStringConstants.MSG_TYPE_IND_ID,52);
    	hsmp.put(GuiStringConstants.CALLED_SUB_IMEISV,53);
    	hsmp.put(GuiStringConstants.CALLED_SUB_IMEISV,54);
    	hsmp.put(GuiStringConstants.ORIGINATING_NUM,55);
    	hsmp.put(GuiStringConstants.DEST_NUM,56);
    	hsmp.put(GuiStringConstants.SERVICE_CENTRE,57);
    	hsmp.put(GuiStringConstants.ORIGINATING_TIME,58);
    	hsmp.put(GuiStringConstants.DELIVERY_TIME,59);
    	hsmp.put("UNSUC_POSITION_REASON_DESC",58);
    	hsmp.put("LCS_CLIENT_TYPE_DESC",59);
    	hsmp.put("TYPE_LOCATION_REQ_DESC",60);
    	hsmp.put("TARGET_MSISDN",61);
    	hsmp.put("TARGET_IMSI",62);
    	hsmp.put("TARGET_IMEI",63);
    	hsmp.put("LCS_CLIENT_ID",64);
    	hsmp.put("POSITION_DELIVERY",65);
    	
    	if ( hsmp.get(headerInUI) != null)
        {
        	result = hsmp.get(headerInUI);
        }        
        return result;
    }


    /* This method is to sort the given list in ascending order 
     * @param dataToSort - List of data to be sorted
     * 
     * @return sortedData - Sorted list.
     */
	final static List<String> ascendingOrderSort(final List<String> dataToSort) 
	{
	    List<String> sortedData = new ArrayList<String>();
	    sortedData = dataToSort;
	    if ( sortedData != null && sortedData.size() > 0 )
	    {
	    	Collections.sort(sortedData, String.CASE_INSENSITIVE_ORDER);
	    }
        return sortedData;
	}

    /* This method is to sort the given list in descending order 
     * @param dataToSort - List of data to be sorted
     * 
     * @return sortedData - Sorted list.
     */
	final static List<String> descendingOrderSort(final List<String> dataToSort) 
	{
	    List<String> sortedData = ascendingOrderSort(dataToSort);
	    List<String> descendingOrderData = new ArrayList<String>();
	    if ( sortedData != null && sortedData.size() > 0 )
	    {
	    	for( int i = sortedData.size()-1 ; i >= 0; i--)
	    	{
	    		descendingOrderData.add( sortedData.get(i));
	    	}	    	
	    }
        return descendingOrderData;
	}
	
	/* This method is to find the success ratio
	 * 
	 * @param success - number of successes
	 * @param failure - Number of failures
	 * @return successRatio - success ratio based on success and 
	 *                        failures 
	 */
	final static double getSuccessRatio(float success, float failure)
	{	
	    NumberFormat numberFormat = new DecimalFormat();
	    numberFormat.setMaximumFractionDigits(2);
		final float total = success + failure;
		final float percent = 100/total;
		final double res = success * percent;
		double successRatio = Double.parseDouble(
				numberFormat.format( res ));
		return successRatio;
	}
		
    /* This method is to get the exact seconds from the date
     * passed.
     * 
     *@param csvData - Date
     *@return csvEventInSeconds - seconds 
     */
	final static String getExactDateFormat(final String csvData) {
		String csvEventInSeconds = "";
		if (csvData.contains(".")) {
			csvEventInSeconds = csvData.substring(csvData.lastIndexOf(":") + 1,
					csvData.lastIndexOf("."));
		} else {
			csvEventInSeconds = csvData.substring(csvData.lastIndexOf(":") + 1,
					csvData.length());
		}
		return csvEventInSeconds;
	}
	
	/* This method is to get the corresponding rat id for the rat.
	 * The values mapped based on the table
	 * 
	 * @param typeOfEvent - Type of event.
	 * @return ratID - corresponding rat id. 
	 * 
	 */
    final static int getRATID(final String ratName)
    {
    	int ratID = -1;    	
    	HashMap<String, Integer> hsmp = new HashMap<String, Integer>();
   	 	
    	hsmp.put(DataIntegrityConstants.GSM,0);
    	hsmp.put(DataIntegrityConstants.Three_G,1);
    	hsmp.put(DataIntegrityConstants.LTE,2);
    	if ( hsmp.get(ratName) != null)
        {
    		ratID = hsmp.get(ratName);
        }        
        return ratID;
    }

	/* This method is to get the corresponding rat name for the rat id.
	 * The values mapped based on the table
	 * 
	 * @param typeOfEvent - Type of event.
	 * @return ratID - corresponding rat id. 
	 * 
	 */
    final static String getRATName(final String ratID)
    {
    	String ratName = "";    	
    	HashMap<String, String> hsmp = new HashMap<String, String>();
   	 	
    	hsmp.put("0",DataIntegrityConstants.GSM);
    	hsmp.put("1",DataIntegrityConstants.Three_G);
    	hsmp.put("2",DataIntegrityConstants.LTE);
    	if ( hsmp.get(ratID) != null)
        {
    		ratName = hsmp.get(ratID);
        }        
        return ratName;
    }

    /* This method is to get only the voice related data from the csv file 
	 * and store the data as list. 
	 * 
	 * @return csvDataAsList - csv file data in a form of list.
	 */
	final static List<List<String>> getVoiceRelatedCsvData() 
	{
		CsvRead csvRead;
		List<List<String>> csvDataAsList = new ArrayList<List<String>>();
		try
		{
			csvRead = new CsvRead(FILE_PATH_OF_MSSRESERVED_DATA);
			csvRead.skipRecord();
		    while ( csvRead.readRecord())
			{
		    	final List<String> singleRowData = Arrays.asList(csvRead.getValues());
		    	if( singleRowData.contains(GuiStringConstants.MSORIGINATING) ||
		    		singleRowData.contains(GuiStringConstants.MSTERMINATING) ||
		    		singleRowData.contains(GuiStringConstants.CALLFORWARDING) ||
			    	singleRowData.contains(GuiStringConstants.ROAMINGCALLFORWARDING) )
		    	{
			    	csvDataAsList.add(singleRowData);
		    	}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        catch (IOException e) 
        {
	    	e.printStackTrace();
     	}

        return csvDataAsList;
	}
	
	/*
	 * This method is to return the corresponding Message Type Indicator
	 * for the Message Type Indicator Id passed.
	 * 
	 * @param msgTypeIndicatorID - contains the message Type Indicator ID.
	 * 
	 * @return The Corresponding message Type Indicator.
	 */	
	
    final static String getMsgTypeIndicator(final int msgTypeIndicatorID)
    {
    	HashMap<Integer, String> hsmp = new HashMap<Integer, String>();
    	
    	hsmp.put(0,	DataIntegrityConstants.SMS_DELIVER_SC_TO_MS);
    	hsmp.put(1,	DataIntegrityConstants.SMS_DELIVEREPORT_MS_TO_SC);
    	hsmp.put(2,	DataIntegrityConstants.SMS_STATUS_REPORT_SC_TO_MS);
    	hsmp.put(3,	DataIntegrityConstants.SMS_COMMAND_MS_TO_SC);
    	hsmp.put(4,	DataIntegrityConstants.SMS_SUBMIT_MS_TO_SC);
    	hsmp.put(5,	DataIntegrityConstants.SMS_SUBMIT_REPORT_SC_TO_MS);
        hsmp.put(6,	DataIntegrityConstants.RESERVED_MTI_VALUE);
        
        return hsmp.get(msgTypeIndicatorID);
    }

	/*
	 * This method is to return the corresponding SMS Result
	 * for the SMS Result Id passed.
	 * 
	 * @param smsResultID - contains the SMS Result ID.
	 * 
	 * @return The Corresponding SMS Result.
	 */
    
    final static String getSmsResult(final int smsResultID)
    {
    	HashMap<Integer, String> hsmp = new HashMap<Integer, String>();

    	hsmp.put(0,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_CAMELREASON);
    	hsmp.put(1,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSDUE_TO_OTHERREASON);
    	hsmp.put(2,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_CAMELREASON);
    	hsmp.put(3,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_OTHERREASON);
    	hsmp.put(4,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_SMSCDUE_TO_RTCFAREASON);
    	hsmp.put(5,	DataIntegrityConstants.UNSUCCESSFULMOSMS_DELIVERY_TO_MSDUE_TO_RTCFAREASON);
        
        return hsmp.get(smsResultID);
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
	
	final static List<List<String>> formatUIFailureAnalysisData(
			final List<Map<String,String>> completeUITableValues,
			final List<String> windowHeaders)
	{
		List<List<String>> formattedUIDataList = new ArrayList<List<String>>();
		final int headerSize = windowHeaders.size();
		for(final Map<String,String> rowData : completeUITableValues)
		{
			int count = 0;
			List<String> formattedUIRowData = new ArrayList<String>();
			for(int i=0; i < headerSize; i++)
			{
				formattedUIRowData.add(DataIntegrityConstants.NO_DATA);
			}
			for( final String header : windowHeaders)
			{   
				formattedUIRowData.set(count, rowData.get(header));
			    count++;
			}
			formattedUIDataList.add(formattedUIRowData);
		}
		
		logger.log(Level.INFO, "The Formatted Analysis Data retrieved" +
				               " from UI  :" + formattedUIDataList);
		return formattedUIDataList;
	}	

	/*
	 * This method is to find the callCompletionRatio.
	 * 
	 * @param numberOfSuccessCallEvents - Number of success events.
	 * @param totalEvents               - Total number of events.
	 * 
	 * @return callCompletionRatio - call completion ratio for the events
	 */
 
   final static double callCompletionRatio(
		   final float numberOfSuccessCallEvents, final float totalEvents)
   {
	 //to prevent divide by zero
	    if (totalEvents == 0.0)
	    	return 0.0;
	    NumberFormat numberFormat = new DecimalFormat();
	    numberFormat.setMaximumFractionDigits(2);
		double callCompletionRatio = Double.parseDouble(
				numberFormat.format( 100 * (
						numberOfSuccessCallEvents / totalEvents) ));
	   return callCompletionRatio;
   }

	/*
	 * This method is to find the callAttemptsIntensity.
	 * 
	 * @param totalNumberOfCallEvents   - Total number of events.
	 * @param numberOfImpactedSubscriber- Number of impacted subscribers.
	 * 
	 * @return callAttemptsIntensity - call attempts intensity for the events
	 */
   
   final static double callAttemptsIntensity(final float totalNumberOfCallEvents,
		   final float numberOfImpactedSubscriber,
		   final float manipulatedDuration)
   {
	   //To prevent divide by zero error
	   if (numberOfImpactedSubscriber == 0.0)
		   return 0.0;
	   
	    NumberFormat numberFormat = new DecimalFormat();
	    numberFormat.setMaximumFractionDigits(2);
	    final double callAttemptsIntensity = Double.parseDouble(
				numberFormat.format(( 
						totalNumberOfCallEvents / numberOfImpactedSubscriber) *
						manipulatedDuration));	   
	   return callAttemptsIntensity;
   }
   
	/*
	 * This method is to find the callDropRatio.
	 * 
	 * @param numberOfDroppedCallEvents - Number of dropped events.
	 * @param totalCallEvents           - Total number of events.
	 * 
	 * @return callDropsRatio           - call drop ratio for the events.
	 */

   final static double callDropsRatio(final float numberOfDroppedCallEvents, 
		   final float totalCallEvents)
   {
	   //to prevent divide by zero
	    if (totalCallEvents == 0.0)
	    	return 0.0;
	    NumberFormat numberFormat = new DecimalFormat();
	    numberFormat.setMaximumFractionDigits(2);
	    final double callDropsRatio = Double.parseDouble(
				numberFormat.format(100 * (
						numberOfDroppedCallEvents / totalCallEvents)));
	   
	   return callDropsRatio;
   }

	/*
	 * This method is to find the callDropsIntensity.
	 * 
	 * @param numberOfDroppedCallEvents - Total number of dropped events.
	 * @param numberOfImpactedSubscriber- Number of impacted subscribers.
	 * 
	 * @return callDropsIntensity - call drops intensity for the events
	 */
   
   final static double callDropsIntensity(final float numberOfDroppedCallEvents,
		   final float numberOfImpactedSubscriber,
		   final float manipulatedDuration)
   {
	   //To prevent divide by zero error
	   if (numberOfImpactedSubscriber == 0.0)
		   return 0.0;
	   
	    NumberFormat numberFormat = new DecimalFormat();
	    numberFormat.setMaximumFractionDigits(2);
 	   final double callDropsIntensity = Double.parseDouble(
				numberFormat.format((
						numberOfDroppedCallEvents / numberOfImpactedSubscriber) * 
						manipulatedDuration));
	   
	   return callDropsIntensity;
   }

   /*
    * This method is to get the corresponding terminal group data 
    * from the csv data.
    * 
    * @param csvDataAsList - complete CSV data as list
    * @param tacList       - TAC list which are in the group selected 
    *                        for analysis.
    *                       
    * @return terminalGroupList - contains the corresponding data for the list
    *                             of tac passed. 
    */
	final static List<List<String>> getCSVDataForTerminalGroup(
			final List<List<String>> csvDataAsList,final List<String> tacList) 
	{
		List<List<String>> terminalGroupList = new ArrayList<List<String>>();
		for( final String tac : tacList)
		{
			for ( final List<String> csvRowData : csvDataAsList)
			{
				if ( csvRowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.TAC)).equalsIgnoreCase(tac) )
				{
					terminalGroupList.add(csvRowData);
				}
			}			
		}
		return terminalGroupList;
	}

   /*
	* This method is to get the corresponding terminal data 
	* from the csv data.
	* 
	* @param csvDataAsList - complete CSV data as list
	* @param tac           - TAC value of the terminal Selected
	* @param terminalMake  - TerminalMake name of the terminal Selected
	* @param terminalModel - TerminalModel name of the terminal Selected
	* @return terminalList - contains the corresponding data for the 
	*                        tac,terminalmake and terminalmodel passed. 
	*/

	final static List<List<String>> getCSVDataForTerminal( 
			List<List<String>> csvDataAsList, final String tac,
			final String terminalMake, final String terminalModel)
	{
		List<List<String>> terminalList = new ArrayList<List<String>>();
		for ( final List<String> csvRowData : csvDataAsList)
		{
			if ( csvRowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					GuiStringConstants.TAC)).equalsIgnoreCase(tac) &&
			     csvRowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
			        GuiStringConstants.TERMINAL_MAKE)).equalsIgnoreCase(terminalMake) &&
			     csvRowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
			    	GuiStringConstants.TERMINAL_MODEL)).equalsIgnoreCase(terminalModel))
			{
				terminalList.add(csvRowData);
			}
		}

		return terminalList;
	}

   /*
	* This method is to get the corresponding csv data for the field passed.
	* 
	* @param completeCSVData   - complete CSV data as list
	* @param searchFieldName   - Field Name.
	* @param searchFieldValue  - The corresponding value for the field selected
    *
	* @return requiredCSVDAtaList - contains the corresponding data for the 
	*                               field passed. 
	*/	
	public static List<List<String>> getCSVDataBasedOnField( 
			final List<List<String>> completeCSVData, 
			final String searchFieldName, final String searchFieldValue)
	{
		List<List<String>> requiredCSVDAtaList = new ArrayList<List<String>>();
		
		for(final List<String> rowData : completeCSVData)
		{
			if( rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					searchFieldName)).equalsIgnoreCase(searchFieldValue))
			{
				requiredCSVDAtaList.add(rowData);
			}			
		}
		return requiredCSVDAtaList;
	}

   /*
	* This method is to get the corresponding csv data for the access area.
	* 
	* @param completeCSVData   - complete CSV data as list
	* @param searchFieldName   - Field Name ie Access Area.
	* @param searchFieldValue  - The corresponding value for the access area.
    *
	* @return requiredCSVDAtaList - contains the corresponding data for the 
	*                        access area passed. 
	*/		
	public static List<List<String>> getCSVDataBasedOnAccessArea( 
			final List<List<String>> csvDataAsList, 
			final String searchFieldName, final String searchFieldValue)
	{
		List<List<String>> requiredCSVDAtaList = new ArrayList<List<String>>();
		final String[] test = searchFieldValue.split(DataIntegrityConstants.COMMA_SYMBOL);
		String accessArea = DataIntegrityConstants.BLANK_SPACE;
		String blank = DataIntegrityConstants.BLANK_SPACE;
		String controller = DataIntegrityConstants.BLANK_SPACE;
		String vendor = DataIntegrityConstants.BLANK_SPACE;
		String rat = DataIntegrityConstants.BLANK_SPACE;
		System.out.println(test.length);
		if(test.length == 5 )
		{
			accessArea = test[0];
			blank = test[1];
			controller = test[2];
			vendor = test[3];
			rat = test[4];		
		}
		for(final List<String> rowData : csvDataAsList)
		{
			if( rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					searchFieldName)).equalsIgnoreCase(accessArea) &&
				rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					GuiStringConstants.RAN_VENDOR)).equalsIgnoreCase(vendor) &&
				rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
    			GuiStringConstants.CONTROLLER)).equalsIgnoreCase(controller) &&
				rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.RAT)).equalsIgnoreCase(rat))
			{
				requiredCSVDAtaList.add(rowData);
			}			
		}
		return requiredCSVDAtaList;
	}

   /*
	* This method is to get the corresponding csv data for the access area.
	* 
	* @param completeCSVData   - complete CSV data as list
	* @param searchFieldName   - Field Name ie Controller.
	* @param searchFieldValue  - The corresponding value for the controller.
    *
	* @return requiredCSVDAtaList - contains the corresponding data for the 
	*                               controller passed. 
	*/
	
	public static List<List<String>> getCSVDataBasedOnController( 
			final List<List<String>> completeCSVData, 
			final String searchFieldName, final String searchFieldValue)
	{
		List<List<String>> requiredCSVDAtaList = new ArrayList<List<String>>();
		final String[] test = searchFieldValue.split(DataIntegrityConstants.COMMA_SYMBOL);
		String controller = DataIntegrityConstants.BLANK_SPACE;
		String vendor = DataIntegrityConstants.BLANK_SPACE;
		String rat = DataIntegrityConstants.BLANK_SPACE;
		if(test.length == 3 )
		{
			controller = test[0];
			vendor = test[1];
			rat = test[2];
			
		}
		for(final List<String> rowData : completeCSVData)
		{
			if( rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
					searchFieldName)).equalsIgnoreCase(controller) &&
				rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
				GuiStringConstants.RAN_VENDOR)).equalsIgnoreCase(vendor) &&
				rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(
						GuiStringConstants.RAT)).equalsIgnoreCase(rat))
			{
				requiredCSVDAtaList.add(rowData);
			}			
		}
		return requiredCSVDAtaList;
	}
	
	
   /*
	* This method is to form the topology related data that needs to be updated
	* on the MSSReservedData CSV file so that the datagen and automation will 
	* be in sync.
	* 
	* @param twoGData             - 2G Related topology data
	* @param CountryAndOperator   - Country and Operator.
	* @param tacInformation       - TAC related informations.
	* @param faultCodeInformation - Fault code related informations.
    * @param teleServiceCodeInformation - Teleservice related informations.
	* @param bearerServiceInformation   - Bearerservice related informations.
	* @param IMSI1                - First IMSI name for which these topology
	*                               data will be updated. 
    * @param IMSI2                - Second IMSI name for which these topology
	*                               data will be updated. 
	*/	
	public static List<List<String>> updatingTopologyValues(
			final HashMap<String, String> twoGData,
			final HashMap<String, String> CountryAndOperator, 
			final HashMap<String, String> tacInformation, 
			final HashMap<String, String> faultCodeInformation, 
			final HashMap<String, String> teleServiceCodeInformation,
			final HashMap<String, String> bearerServiceInformation,
			final String IMSI1, final String IMSI2)
	{
		final List<List<String>> modified2GData = new ArrayList<List<String>>();
		final List<List<String>> csvData = GeneralUtil.forReadingMSSReservedData();
		final String tac = tacInformation.get(DataIntegrityConstants.TOPOLOGY_TAC);
		for( final List<String> rowData : csvData)
		{
			List<String> updatedRowData = rowData;
			final int accessAreaIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.ACCESS_AREA);
			final int controllerIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.CONTROLLER);
			final int mccIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.MCC);
			final int mncIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.MNC);
			final int ratIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.RAT);
			final int lacIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.LAC);
			final int tacIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.TAC);
			final int terminalMakeIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.TERMINAL_MAKE);
			final int terminalModelIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.TERMINAL_MODEL);
			final int faultCodeIDIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.FAULT_CODE_ID);	
			final int faultCodeIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.FAULT_CODE);
			final int adviceIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.RECOMMENDED_ACTION);
			final int teleServiceCodeIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.TELE_SERVICE_CODE);					
			final int bearerServiceCodeIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.BEARER_SERVICE_CODE);
			final int callingSubsIMEIIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.CALLING_SUBS_IMEI);
			final int calledSubsIMEIIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.CALLED_SUBS_IMEI);
			final int ranVendorIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.RAN_VENDOR);
			final int callingPartyNumberIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.CALLING_PARTY_NUMBER);
			final int imsiIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI);
			final int msisdnIndex = GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.MSISDN);
			
			if (updatedRowData.get(imsiIndex).equalsIgnoreCase(IMSI1)
					|| updatedRowData.get(imsiIndex).equalsIgnoreCase(IMSI2)) 
			{
				final String imsiName = updatedRowData.get(imsiIndex);

				final String modifiedIMSI = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
				                            twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) +
				                            imsiName.substring(5, imsiName.length());
				updatedRowData.set(imsiIndex, modifiedIMSI.trim());
				if (!updatedRowData.get(callingPartyNumberIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE))
				{
					final String callingPartyNumber = updatedRowData.get(callingPartyNumberIndex);
					final String modifiedCallingPartyNumber = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
					                            twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) +
					                            callingPartyNumber.substring(5, callingPartyNumber.length());
					updatedRowData.set(callingPartyNumberIndex, modifiedCallingPartyNumber.trim());					
				}
				if (!updatedRowData.get(msisdnIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					final String msisdnName = updatedRowData.get(msisdnIndex);
					final String modifiedmsisdn = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
                    twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) +
                    msisdnName.substring(5, msisdnName.length());
					updatedRowData.set(msisdnIndex, modifiedmsisdn.trim());
				}
				if (!updatedRowData.get(accessAreaIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(accessAreaIndex, twoGData
							.get(DataIntegrityConstants.TOPOLOGY_ACCESS_AREA).trim());
				}
				if (!updatedRowData.get(controllerIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(controllerIndex, twoGData
							.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim());
				}
				if (!updatedRowData.get(mccIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(mccIndex,
							twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC).trim());
				}
				if (!updatedRowData.get(mncIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(mncIndex,
							twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC).trim());
				}
				if (!updatedRowData.get(ratIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(ratIndex,
							GeneralUtil.getRATName(twoGData.get(DataIntegrityConstants.TOPOLOGY_RAT)).trim());
				}
				if (!updatedRowData.get(lacIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(lacIndex,
							twoGData.get(DataIntegrityConstants.TOPOLOGY_LAC).trim());
				}
				if (!updatedRowData.get(tacIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(tacIndex, tacInformation
							.get(DataIntegrityConstants.TOPOLOGY_TAC).trim());
				}
				if (!updatedRowData.get(ranVendorIndex).trim().equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(ranVendorIndex,
							twoGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR).trim());
				}
				if (!updatedRowData.get(terminalMakeIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(terminalMakeIndex, tacInformation
							.get(DataIntegrityConstants.TOPOLOGY_MANUFACTURER).trim());
				}
				if (!updatedRowData.get(terminalModelIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData
							.set(terminalModelIndex,
									tacInformation
											.get(DataIntegrityConstants.TOPOLOGY_MARKETING_NAME).trim());
				}
				if (!updatedRowData.get(faultCodeIDIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(faultCodeIDIndex, faultCodeInformation
							.get(DataIntegrityConstants.TOPOLOGY_FAULT_CODE));
				}
				if (!updatedRowData.get(faultCodeIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData
							.set(faultCodeIndex,
									faultCodeInformation
											.get(DataIntegrityConstants.TOPOLOGY_FAULT_CODE_DESC).trim());
				}
				if (!updatedRowData.get(adviceIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData.set(adviceIndex, faultCodeInformation
							.get(DataIntegrityConstants.TOPOLOGY_ADVICE).trim());
				}
				if (!updatedRowData.get(teleServiceCodeIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData
							.set(teleServiceCodeIndex,
									teleServiceCodeInformation
											.get(DataIntegrityConstants.TOPOLOGY_TELE_SERVICE_CODE).trim());
				}
				if (!updatedRowData.get(bearerServiceCodeIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					updatedRowData
							.set(bearerServiceCodeIndex,
									bearerServiceInformation
											.get(DataIntegrityConstants.TOPOLOGY_BEARER_SERVICE_CODE).trim());
				}
				if (!updatedRowData.get(callingSubsIMEIIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					final String callingSubsImei = updatedRowData.get(callingSubsIMEIIndex);
					final int tacLength = tac.length();
					final String modifiedCallingSubsImei = tac.trim() + 
					callingSubsImei.substring(tacLength , callingSubsImei.length());
					updatedRowData.set(callingSubsIMEIIndex, modifiedCallingSubsImei);
				}
				if (!updatedRowData.get(calledSubsIMEIIndex).trim()
						.equalsIgnoreCase(DataIntegrityConstants.BLANK_SPACE)) 
				{
					final String calledSubsImei = updatedRowData.get(calledSubsIMEIIndex);
					final int tacLength = tac.length();
					final String modifiedCallingSubsImei = tac.trim() + 
					calledSubsImei.substring(tacLength , calledSubsImei.length());
					updatedRowData.set(calledSubsIMEIIndex, modifiedCallingSubsImei);
				}				
				
				
				modified2GData.add(updatedRowData);
			}
		}
		
		System.out.println("The modified list for imsi " + IMSI1 + " and " + IMSI2 + " is : " + modified2GData.size());
		return modified2GData;
	}


	
   /*
	* This method is to update/write the topology values on to the 
	* MSSReservedData.csv file.
	* 
	* @param csvDataheaders    - complete CSV data as list
	* @param twoGDataList      - Required Topology data for 2G related data.
	* @param threeGDataList    - Required Topology data for 3G related data.
    *
	*/	
	public static void writingTopologyValuesOnCSVFile(
			final List<List<String>> csvDataheaders,
			final List<List<String>> twoGDataList, 
			final List<List<String>> threeGDataList)
	{
		CsvWriter writer = new CsvWriter(FILE_PATH_OF_MSSRESERVED_DATA);
		writer.setDelimiter(',');
		writer.setComment('a');
		List<List<String>> combinedList = csvDataheaders;
		logger.log(Level.INFO, "headers :" + csvDataheaders.size());
		logger.log(Level.INFO, "twoGDataList :" + twoGDataList.size());
		logger.log(Level.INFO, "threeGDataList :" + threeGDataList.size());
		for (final List<String> rowData : twoGDataList) 
		{
			combinedList.add(rowData);
		}		
		for (final List<String> rowData : threeGDataList) 
		{
			combinedList.add(rowData);
		}
        
		logger.log(Level.INFO, "The combined List of 2g and 3g data size :" + combinedList.size());
		try 
		{
			for (final List<String> rowData : combinedList) 
			{
				String header = DataIntegrityConstants.BLANK_SPACE;
				for (final String fieldValue : rowData) 
				{
					header = header.trim() + fieldValue + DataIntegrityConstants.HASH_SYMBOL;
				}
				logger.log(Level.INFO, header);
				writer.writeRecord(header.split(DataIntegrityConstants.HASH_SYMBOL));
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		writer.close();
	}
		
	
	/* This method is to get the data from the MSSReservedData.csv file
	 * and store the data as list. 
	 * 
	 * @return csvDataAsList - csv file data in a form of list.
	 */
	final static List<List<String>> forReadingMSSReservedData() 
	{
		CsvRead csvRead;
		List<List<String>> csvDataAsList = new ArrayList<List<String>>();
		try
		{
			csvRead = new CsvRead(FILE_PATH_OF_REFERENCE_MSSRESERVED_DATA);
			csvRead.skipRecord();
		    while ( csvRead.readRecord())
			{
		    	csvDataAsList.add(Arrays.asList(csvRead.getValues()));
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        catch (IOException e) 
        {
	    	e.printStackTrace();
     	}		
        return csvDataAsList;
	}
	
	
	/* This method is to get the headers from the csv file and store the data
	 * as list. 
	 * 
	 * @return csvDataHeaders - csv headers in a form of a list.
	 */
	final static List<List<String>> getCSVDataHeaders() 
	{
		CsvRead csvRead;
		List<List<String>> csvDataHeaders = new ArrayList<List<String>>();
		try
		{
			csvRead = new CsvRead(FILE_PATH_OF_REFERENCE_MSSRESERVED_DATA);
		    while ( csvRead.readRecord())
			{
		    	csvDataHeaders.add(Arrays.asList(csvRead.getValues()));
		    	break;
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        catch (IOException e) 
        {
	    	e.printStackTrace();
        }
        System.out.println("The headers are : " + csvDataHeaders);
        return csvDataHeaders;
	}
	
	
	/* This method is to update the data onto the reservedData.csv file and
	 * update the accessarea, Terminal, controller, accessarea group fields etc.
	 * So that the name specified will be picked and used 
	 * during the run of automation.
	 * 
	 * This fields needs to be updated as the datagen created values will 
	 * vary for each and every server.ie Accessarea, Terminal, Controller
	 * will vary.
	 * 
     * @param twoGData      - Required Topology data for 2G related data.
	 * @param threeGData    - Required Topology data for 3G related data.
	 * @param tacDAta       - Required TAC related data.
	 */
	final static List<String> getReservedDataCSVDataValues(
			final HashMap<String, String> twoGData,
			final HashMap<String, String> threeGData,
			final HashMap<String, String> tacData)
	{
		CsvRead csvRead;
		List<String> csvDataAsList = new ArrayList<String>();
		try
		{
			csvRead = new CsvRead(FILE_PATH_OF_COMMON_RESERVED_DATA);
			CsvWriter writer = new CsvWriter(FILE_PATH_OF_COMMON_RESERVED_DATA);
			writer.setDelimiter(',');

		    while ( csvRead.readRecord())
			{
		    	csvDataAsList.add(csvRead.getRawRecord());
			}
		        boolean updateTAC = false;
		        boolean update2GImsi = false;
		        boolean update3GImsi = false;
				for (String rowData : csvDataAsList) 
				{
					String modifiedRow = DataIntegrityConstants.BLANK_SPACE;
					if( updateTAC )
					{
						final List<String> tac = new ArrayList<String>(
								Arrays.asList(rowData.split(";")));
						for(final String field : tac)
						{
							String tagValue = DataIntegrityConstants.BLANK_SPACE;
							String tagName = DataIntegrityConstants.BLANK_SPACE;
							if ( field.contains(DataIntegrityConstants.EQUALS_SYMBOL))
							{
								final List<String> splittedOne = new ArrayList<String>(
										Arrays.asList(field.split(DataIntegrityConstants.EQUALS_SYMBOL)));
								tagName = splittedOne.get(0);
								if( tagName.equalsIgnoreCase(GuiStringConstants.TAC))
								{
									tagValue = tacData.get(DataIntegrityConstants.TOPOLOGY_TAC).trim();
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_DATA_TERMINAL_MAKE))
								{
									tagValue = tacData.get(DataIntegrityConstants.TOPOLOGY_MANUFACTURER).trim();
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_DATA_TERMINAL_MODEL))
								{
									tagValue = tacData.get(DataIntegrityConstants.TOPOLOGY_MARKETING_NAME).trim();
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if(tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_IMSI_MSS))
								{
                                    if(update2GImsi)
                                    {
    									tagValue = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
			                            twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) + "0987654322"; 
    									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";                                    	
                                    }
                                    else if(update3GImsi)
                                    {
    									tagValue = threeGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
    									threeGData.get(DataIntegrityConstants.TOPOLOGY_MNC) + "0987654323";
    									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";                                  	
                                    }
								}
								else
								{
									tagValue = splittedOne.get(1);
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
							}
						}
						updateTAC = false;
						System.out.println("The TAC changes related data : " + modifiedRow);
					}
					else if ( rowData.contains("#IMSI for 2G - For MSS TestData") ||
							rowData.contains("#IMSI for 3G - For MSS TestData"))
					{
						updateTAC = true;
						modifiedRow = rowData;
						
						if(rowData.contains("#IMSI for 2G - For MSS TestData"))
						{
							update2GImsi = true;
							update3GImsi = false;
						}
						else if(rowData.contains("#IMSI for 3G - For MSS TestData"))
						{
							update2GImsi = false;
							update3GImsi = true;							
						}
					}
					else if ( rowData.contains(DataIntegrityConstants.RESERVED_CONTROLLER_MSS) &&
						 rowData.contains(DataIntegrityConstants.RESERVED_ACCESS_AREA_MSS))
					{
						final List<String> mss_data = new ArrayList<String>(
								Arrays.asList(rowData.split(";")));
						for(final String field : mss_data)
						{
							String tagValue = DataIntegrityConstants.BLANK_SPACE;
							String tagName = DataIntegrityConstants.BLANK_SPACE;
							System.out.println("The field name is :" + field);
							if ( field.contains(DataIntegrityConstants.EQUALS_SYMBOL))
							{
								final List<String> splittedOne = new ArrayList<String>(
										Arrays.asList(field.split(DataIntegrityConstants.EQUALS_SYMBOL)));
								tagName = splittedOne.get(0);
								if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_ACCESS_AREA_MSS))
								{
									tagValue = twoGData.get(DataIntegrityConstants.TOPOLOGY_ACCESS_AREA).trim() + ", ,"+ 
							           twoGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+DataIntegrityConstants.COMMA_SYMBOL+
							           twoGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR) +DataIntegrityConstants.COMMA_SYMBOL+
							           GeneralUtil.getRATName(twoGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_CONTROLLER_MSS))
								{
									tagValue = twoGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+ DataIntegrityConstants.COMMA_SYMBOL+
									twoGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR) + DataIntegrityConstants.COMMA_SYMBOL + GeneralUtil.getRATName(twoGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}								
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_ACCESS_AREA_GROUP_MSS))
								{//CELL88260,,BSC442,Ericsson
									final String twoG = twoGData.get(DataIntegrityConstants.TOPOLOGY_ACCESS_AREA).trim() + ", ,"+ 
									           twoGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+DataIntegrityConstants.COMMA_SYMBOL+
									           twoGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR) +DataIntegrityConstants.COMMA_SYMBOL+
									           GeneralUtil.getRATName(twoGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									final String threeG = threeGData.get(DataIntegrityConstants.TOPOLOGY_ACCESS_AREA).trim() + ", ,"+ 
									                      threeGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+DataIntegrityConstants.COMMA_SYMBOL+
									                      threeGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR)+DataIntegrityConstants.COMMA_SYMBOL+
							           GeneralUtil.getRATName(threeGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									tagValue = twoG + DataIntegrityConstants.HYPHEN_SYMBOL + threeG;
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_CONTROLLER_GROUP_MSS))
								{//CELL88260,,BSC442,Ericsson
									final String twoG = twoGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+ DataIntegrityConstants.COMMA_SYMBOL+
									twoGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR).trim() + DataIntegrityConstants.COMMA_SYMBOL + GeneralUtil.getRATName(twoGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									final String threeG = threeGData.get(DataIntegrityConstants.TOPOLOGY_CONTROLLER).trim()+ DataIntegrityConstants.COMMA_SYMBOL +
									threeGData.get(DataIntegrityConstants.TOPOLOGY_VENDOR).trim() + DataIntegrityConstants.COMMA_SYMBOL + GeneralUtil.getRATName(threeGData.get(DataIntegrityConstants.TOPOLOGY_RAT.trim()));
									tagValue = twoG + "-" + threeG;
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_TAC_GROUP_MSS))
								{
									tagValue = tacData.get(DataIntegrityConstants.TOPOLOGY_TAC).trim();									
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_MSISDN_MSS))
								{
									final String msisdn = splittedOne.get(1);
									tagValue = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
				                    twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) +
				                    msisdn.substring(5, msisdn.length());									
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";									
								}
								else if( tagName.equalsIgnoreCase(DataIntegrityConstants.RESERVED_IMSI_GROUP_MSS))
								{
									//460000123456791,460000123456790
    									final String twoGIMSI = twoGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
			                            twoGData.get(DataIntegrityConstants.TOPOLOGY_MNC) + "0123456790"; 
    									final String threeGIMSI = threeGData.get(DataIntegrityConstants.TOPOLOGY_MCC) +
    									threeGData.get(DataIntegrityConstants.TOPOLOGY_MNC) + "0123456791";
    									tagValue = twoGIMSI + DataIntegrityConstants.COMMA_SYMBOL + threeGIMSI;
    									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";                                  	
								}
								else
								{
									tagValue = splittedOne.get(1);
									modifiedRow = modifiedRow.trim() + tagName + DataIntegrityConstants.EQUALS_SYMBOL + tagValue + ";";
								}								
							}
						}                        
					}
					else
					{
						modifiedRow = rowData;
					}
					    
						final boolean valid = rowData.contains(DataIntegrityConstants.HASH_SYMBOL);						
						System.out.println("The final modified data to be updated :" + modifiedRow);
						if(valid)
						{
							writer.writeComment(rowData.substring(1,rowData.length()));
						}
						else
						{
							writer.writeRecord(modifiedRow.split(DataIntegrityConstants.COMMA_SYMBOL));
						}			
				}
				writer.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        catch (IOException e) 
        {
	    	e.printStackTrace();
     	}		
        return csvDataAsList;
	}

}
