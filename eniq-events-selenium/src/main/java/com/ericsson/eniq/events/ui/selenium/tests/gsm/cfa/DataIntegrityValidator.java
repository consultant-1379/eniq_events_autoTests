package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.db.DBPersistor;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import com.ericsson.eniq.events.ui.selenium.tests.mss.CsvRead;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataIntegrityValidator {

	public List<Map<String, String>> reserveDataList = new ArrayList<Map<String, String>>();
	
    List<Map<String, String>> extendedCauseCodeResvDataList;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    final static DBPersistor dbPersistor = DBPersistor.getInstatnce();
	
	public void init(String csvFileName, List<String> headers) {
		loadCsv(csvFileName, headers);
	}

	// >>>>>>>>>>>>>
	public boolean validateDataRankingDV(List<String> headers, List<Map<String, String>> tableData,
			TimeRange timeRange, String rankingType, Map<String, Integer> failuresForRanking) {

		String valueToSearchBy; // we will search by this value, e.g. in Access
								// Area ranking this will be
								// the value of the cell in the row
		String failuresInTable; // how many failures does the window show for
								// the particular bsc, cell, whatever
		String IMSI;
		int failuresReserveDataCurrentRow;
		int timeRangeMinutes = timeRange.getMiniutes();
		int count = 0;
		// how many times could the reserve data workflow run (max)
		int reserveDataMultiplier = timeRangeMinutes / GSMConstants.RESERVE_DATA_RUN_INTERVAL;
		// we will calculate the numbers from the reserve data according to this
		for (Map<String, String> row : tableData) {
			System.out.println("Ranking Type = " + rankingType);
			System.out.println("Value to search by = " + failuresForRanking.get(row.get(rankingType)));
			valueToSearchBy = row.get(rankingType); // row.get(GUIConstants.IMSI)
			failuresInTable = row.get(GuiStringConstants.ACCESS_AREA);
			IMSI = row.get(GuiStringConstants.IMSI);
			failuresReserveDataCurrentRow = failuresForRanking.get(valueToSearchBy);
			System.out.println("Value to Search by:" + valueToSearchBy + "IMSI: " + IMSI + " row:" + row);
			System.out.println("From GUI: Failures in Table (" + failuresInTable + ") IMSI " + IMSI);
			System.out.println("failuresReserveDataCurrentRowTest " + failuresReserveDataCurrentRow
					+ " reserveDataMultiplier " + reserveDataMultiplier + " multiplied ("
					+ (failuresReserveDataCurrentRow * reserveDataMultiplier) + ")" + "\n");
			System.out.println("Count = " + (++count));

		}
		return true;
		// return true;
	}// end of method

	/**
	 * Validates data in a ranking window.
	 * 
	 * @param headers
	 *            a list of headernames for the table in the window, contains
	 *            all headers in the current table
	 * @param tableData
	 *            all the data from the table in the form of a list, where every
	 *            item in the list is a row in the table
	 * @param timeRange
	 *            the actual timerange set on the UI
	 * @param rankingType
	 *            type of the ranking, it is actually one of the headers
	 * @param failuresForRanking
	 *            the number of failures for all BSCs, Access Areas etc.
	 * 
	 * @return whether the data in the table is valid according to the reserve
	 *         data specification or not
	 */

	// public boolean
	// validateDataRanking(subscriberRankingGSMCallFailure.getHeaders,
	// subscriberRankingGSMCallFailure.getAllTableData,
	// TimeRange timeRange, GUIStringConstants.IMSI,
	// dataIntegrValidatorObj.CalculateNumFailuresMap()GUIStringConstants.IMSI)
	// {
	public boolean validateDataRanking(List<String> headers, List<Map<String, String>> tableData, TimeRange timeRange,
			String rankingType, Map<String, Integer> failuresForRanking) {

		String valueToSearchBy; // we will search by this value, e.g. in Access
								// Area ranking this will be
								// the value of the cell in the row
		int failuresInTable = 0; // how many failures does the window show for the
								// particular bsc, cell, whatever
		String IMSI;
		int failuresReserveDataCurrentRow = 0;
		int timeRangeMinutes = timeRange.getMiniutes();
		// how many times could the reserve data workflow run (max)
		int reserveDataMultiplier = timeRangeMinutes / GSMConstants.RESERVE_DATA_RUN_INTERVAL;
		// we will calculate the numbers from the reserve data according to this
		
		for (Map<String, String> row : tableData) {
			
				valueToSearchBy = row.get(rankingType); // row.get(GUIConstants.IMSI)
			if (!(valueToSearchBy.equals(""))){
				failuresInTable = Integer.parseInt(row.get(GuiStringConstants.FAILURES));
				IMSI = row.get(GuiStringConstants.IMSI);
				System.out.println("valueToSearchBy " + valueToSearchBy);
				failuresReserveDataCurrentRow = failuresForRanking.get(valueToSearchBy);
				System.out.println("Value to Search by:" + valueToSearchBy + "IMSI: " + IMSI + " row:" + row);
				System.out.println("From GUI: Failures in Table (" + failuresInTable + ") IMSI " + IMSI);
				System.out.println("failuresReserveDataCurrentRow " + failuresReserveDataCurrentRow
						+ " reserveDataMultiplier " + reserveDataMultiplier + " multiplied ("
						+ (failuresReserveDataCurrentRow * reserveDataMultiplier) + ")" + "\n");
			}


			/*
			 * if it's not a multiple of the failures in the reserveData or it
			 * shows more, than it could, the data integrity is considered to be
			 * failed
			 */
			// reserve data generation should be started at least one hour
			// before the tests
			/*
			 * need to consider this: if data makes into the db later then 5
			 * minutes after the last db-write, this could cause some issues
			 */
			if (timeRangeMinutes <= 60) {
				if (failuresInTable != failuresReserveDataCurrentRow * reserveDataMultiplier) {
					printErrorReason("1", GuiStringConstants.FAILURES, failuresReserveDataCurrentRow
							* reserveDataMultiplier, failuresInTable, timeRange);
					return false;
				}
			} else {
				/*
				 * for periods longer than 1 hour, we cannot be sure, how many
				 * times did the reserve data workflow run, since, not sure when
				 * we could start it, so we just check if it's bigger, than it
				 * should
				 */
				if ((failuresInTable % failuresForRanking.get(valueToSearchBy) != 0)
						|| ((failuresInTable / failuresForRanking.get(valueToSearchBy)) > reserveDataMultiplier)) {
					printErrorReason("2", GuiStringConstants.FAILURES,
							"less, than " + failuresForRanking.get(valueToSearchBy) * reserveDataMultiplier,
							String.valueOf(failuresInTable), timeRange);
					return false;
				}
			}

			// check the other data in the rows
			for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
				if (relevantReserveDataRecord.get(rankingType).equals(valueToSearchBy)) {
					// we have found the right record in the reserve data, so go
					// through the headers
					for (String header : headers) {
						// exclude headers (failures, ranking), which was
						// checked above
						if ((!header.equals(GuiStringConstants.FAILURES)) && (!header.equals(GuiStringConstants.RANK))
								&& (!row.get(header).equals(relevantReserveDataRecord.get(header)))) {
							System.out.println("Not matching data header: " + header);
							System.out.println("Data expected: " + relevantReserveDataRecord.get(header));
							System.out.println("Data found: " + row.get(header));
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	// In order to check if data is related to a particular IMSI we provide the
	// IMSI number and have the relevant TAC returned from our reserved data
	// then we use that TAC
	public String checkReservedDataForImsi() {
		String tac = "";

		for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
			if (relevantReserveDataRecord.get(GuiStringConstants.IMSI).equals(GSMConstants.IMSI_NUMBER)) {
				tac = relevantReserveDataRecord.get(GuiStringConstants.TAC);
			}
		}
		return tac;
	}

	public String[] checkReservedDataForImsiGroup() {

		Set allIMSINamesFromGUI = new HashSet();

		// init();

		for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
			System.out.println(relevantReserveDataRecord.get(GuiStringConstants.IMSI));
			System.out.println(GSMConstants.IMSI_GROUP_MEMBER_NAMES);

			for (String imsiGroupMember : GSMConstants.IMSI_GROUP_MEMBER_NAMES) {
				if (relevantReserveDataRecord.get(GuiStringConstants.IMSI).equals(imsiGroupMember)) {
					System.out.println("Inside if");
					allIMSINamesFromGUI.add(relevantReserveDataRecord.get(GuiStringConstants.TAC));
				}
			}// end of imsiGroupMember for loop
		}
		allIMSINamesFromGUI.remove("");
		String[] tacs = (String[]) allIMSINamesFromGUI.toArray(new String[allIMSINamesFromGUI.size()]);
		return tacs;
	}

	/**
	 * Validates data in a drill down window.
	 * 
	 * @param headers
	 *            a list of headernames for the table in the window, contains
	 *            all headers in the current table
	 * @param rowToCheck
	 *            one row in the drill down window
	 * @param timeRange
	 *            the actual timerange set on the UI
	 * @param designatedHeader
	 *            name of header from column in UI grid
	 * @param valueForDesignatedHeader
	 *            Actual value contained in the grid under the designated header
	 * @param failuresReserveData
	 *            the number of failures in the reserve data for that particular
	 *            bsc, access area etc.
	 * @param impactedSubscribersReserveData
	 * 
	 * @return whether the data in the row is valid according to the reserve
	 *         data specification or not
	 */
	public boolean validateGridRowDataIncludingFailureRatio(List<String> headers, Map<String, String> rowToCheck,
			TimeRange timeRange, String designatedHeader, String valueForDesignatedHeader, int failuresInReserveData,
			int reserveDataFailedEventsCount, int impactedCellsinReserveData, double failureRatioReservedData) {

		int failuresInTable;
		int impactedSubscribersInTable;
		int impactedCellsInTable;
		double failureRatioInTable;
		int timeRangeMinutes = timeRange.getMiniutes();
		// how many times could the reserve data workflow run (max)
		int reserveDataMultiplier = timeRangeMinutes / GSMConstants.RESERVE_DATA_RUN_INTERVAL;

		for (String header : headers) {
			if (header.equals(GuiStringConstants.FAILURES)) {
				failuresInTable = Integer.parseInt(rowToCheck.get(GuiStringConstants.FAILURES));
				System.out.println("failuresInTable " + failuresInTable);

				/*
				 * For time ranges less, than or equal to one hour, the exact
				 * number of failures is checked For longer periods, it is not
				 * sure, when the reserve data workflow was started, so only
				 * checked is whether the number of failures is not greater,
				 * than it is possible for that range, and it is an integer
				 * muliple of the failures in the reserve data
				 */
				if (timeRangeMinutes <= 60) {
					if (failuresInTable != (failuresInReserveData * reserveDataMultiplier)) {
						printErrorReason("3", header, failuresInReserveData * reserveDataMultiplier, failuresInTable,
								timeRange);
						return false;
					}
				} else {
					if (((failuresInTable % failuresInReserveData) != 0)
							&& (failuresInTable / failuresInReserveData) > reserveDataMultiplier) {
						printErrorReason("4", header, failuresInReserveData * reserveDataMultiplier, failuresInTable,
								timeRange);
						return false;
					}
				}
			} else if (header.equals(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
				impactedSubscribersInTable = Integer.parseInt(rowToCheck.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
				if (impactedSubscribersInTable != reserveDataFailedEventsCount) {
					printErrorReason("5", header, reserveDataFailedEventsCount, impactedSubscribersInTable, timeRange);
					return false;
				}
			} else if (header.equals(GuiStringConstants.IMPACTED_CELLS)) {
				impactedCellsInTable = Integer.parseInt(rowToCheck.get(GuiStringConstants.IMPACTED_CELLS));
				if (impactedCellsInTable != impactedCellsinReserveData) {
					printErrorReason("5.5", header, impactedCellsinReserveData, impactedCellsInTable, timeRange);
					return false;
				}
			} else if ((header.equals(GuiStringConstants.FAILURE_RATIO))) {
				failureRatioInTable = Double.parseDouble(rowToCheck.get(GuiStringConstants.FAILURE_RATIO));
				if (failureRatioInTable != failureRatioReservedData) {
					String failureRatioReservedDataString = Double.toString(failureRatioReservedData);
					String failureRatioInTableString = Double.toString(failureRatioInTable);
					printErrorReason("5.75", header, failureRatioReservedDataString, failureRatioInTableString,
							timeRange);
				}
			} else {
				// check other data in the row
				for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
					if (relevantReserveDataRecord.get(designatedHeader).equals(valueForDesignatedHeader)) {
						// we found the right record, so let's check the header
						// TODO: What if there are more records in the reserved
						// data with the same value for designated header.
						// Because if the first record for the designated header
						// contains different values the test will fail.
						if (!rowToCheck.get(header).equals(relevantReserveDataRecord.get(header))) {
							printErrorReason("6", header, relevantReserveDataRecord.get(header),
									rowToCheck.get(header), timeRange);
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public boolean validateGridRowData(List<String> headers, Map<String, String> rowToCheck, TimeRange timeRange,
			String designatedHeader, String valueForDesignatedHeader, int failuresInReserveData,
			int reserveDataFailedEventsCount) {

		int failuresInTable;
		int impactedSubscribersInTable;
		int impactedCellsInTable;
		double failureRatioInTable;
		int timeRangeMinutes = timeRange.getMiniutes();
		// how many times could the reserve data workflow run (max)
		int reserveDataMultiplier = timeRangeMinutes / GSMConstants.RESERVE_DATA_RUN_INTERVAL;

		for (String header : headers) {
			if (header.equals(GuiStringConstants.FAILURES)) {
				failuresInTable = Integer.parseInt(rowToCheck.get(GuiStringConstants.FAILURES));
				/*
				 * For time ranges less, than or equal to one hour, the exact
				 * number of failures is checked For longer periods, it is not
				 * sure, when the reserve data workflow was started, so only
				 * checked is whether the number of failures is not greater,
				 * than it is possible for that range, and it is an integer
				 * muliple of the failures in the reserve data
				 */
				if (timeRangeMinutes <= 60) {
					if (failuresInTable != (failuresInReserveData * reserveDataMultiplier)) {
						printErrorReason("3", header, failuresInReserveData * reserveDataMultiplier, failuresInTable,
								timeRange);
						return false;
					}
				} else {
					if (((failuresInTable % failuresInReserveData) != 0)
							&& (failuresInTable / failuresInReserveData) > reserveDataMultiplier) {
						printErrorReason("4", header, failuresInReserveData * reserveDataMultiplier, failuresInTable,
								timeRange);
						return false;
					}
				}
			} else if (header.equals(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
				impactedSubscribersInTable = Integer.parseInt(rowToCheck.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
				if (impactedSubscribersInTable != reserveDataFailedEventsCount) {
					printErrorReason("5", header, reserveDataFailedEventsCount, impactedSubscribersInTable, timeRange);
					return false;
				}
			} else {
				// check other data in the row
				for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
					if (relevantReserveDataRecord.get(designatedHeader).equals(valueForDesignatedHeader)) {
						// we found the right record, so let's check the header
						// TODO: What if there are more records in the reserved
						// data with the same value for designated header.
						// Because if the first record for the designated header
						// contains different values the test will fail.
						if (!rowToCheck.get(header).equals(relevantReserveDataRecord.get(header))) {
							printErrorReason("6", header, relevantReserveDataRecord.get(header),
									rowToCheck.get(header), timeRange);
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Validates data when drilling down by failures.
	 * 
	 * @param headers
	 * @param failuresToCheck
	 * @param reserveDataFailedEventsCount
	 */
	public boolean isDrillDownOnFailuresDataValid(List<String> headers, List<Map<String, String>> failuresToCheck,
			List<Map<String, String>> reserveDataFailedEventsCount, Calendar currentDateTime,
			Calendar earliestDateTime, DateFormat eventTimeDateFormat, TimeRange timeRange) {

		Date eventTime = null;
		boolean equals;

		for (Map<String, String> relevantReserveDataRecord : failuresToCheck) {
			equals = false;
			try {
				eventTime = eventTimeDateFormat.parse(relevantReserveDataRecord.get(GuiStringConstants.EVENT_TIME));
			} catch (ParseException pe) {
				System.out.println("The value of \"Event Time\" cannot be parsed!");
				System.out.println("The value found: " + eventTime);
				pe.printStackTrace();
				return false;
			}
			// check if the Event Time is correct, i.e. between the current time
			// and the earliest time it is
			// possible for the actual time range
			// if ((eventTime.before(earliestDateTime.getTime())) ||
			// (eventTime.after(currentDateTime.getTime()))) {
			// printErrorReason("7", GuiStringConstants.EVENT_TIME, "" +
			// earliestDateTime.getTime().toString() + " to " +
			// return false;
			// }
			// check if there is a record with the same values in the reserve
			// data
			for (Map<String, String> oneRecordReserveData : reserveDataFailedEventsCount) {
				System.out.println("relevantReserveDataRecord: " + relevantReserveDataRecord);
				System.out.println("oneRecordReserveData: " + oneRecordReserveData); // IMSI
				System.out.println("headers: " + headers);
				if (checkForEqualityByHeaders(relevantReserveDataRecord, oneRecordReserveData, headers)) {
					equals = true;
					break;
				}
			}
			if (!equals) { // there is no such record in the reserve data
				System.out.println("No such record in the reserve data:");
				for (String header : headers)
					System.out.println(header + ": " + relevantReserveDataRecord.get(header));
				return false;
			}
		}

		return true;
	}

	/*
	 * On a drill down (on failures) view goes through all time ranges and
	 * checks if the data integrity is ok.
	 */
	public boolean validateDrillDownOnFailuresAllTimeRangesForGroups(CommonWindow commonWindow, String rankingType,
			String valueToSearchBy) throws PopUpException, NoDataException {

		List<String> headers = new ArrayList<String>();
		// will contain all the failures for the designated BSC, Access Area
		// etc. given in valueToSearchBy
		List<Map<String, String>> failuresForDesignatedOne;
		List<Map<String, String>> failuresInTable;
		Calendar currentCalDateTime;
		Calendar earliestCalDateTime; // this will be used as the starting time
										// for events which we query for
		DateFormat eventTimeDateFormat = new SimpleDateFormat(GSMConstants.DATEFORMAT_STRING);
		int eventTimeLatencyAdjust = 0; // needed because of the latency when
										// querying the different timeranges

		headers = commonWindow.getTableHeaders();

		failuresForDesignatedOne = getFailures(headers, rankingType, valueToSearchBy);

		TimeRange[] timeRanges = getAllTimeRanges();
		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			// have to query the current time each time range, as some checks
			// could take more than a minute
			failuresInTable = commonWindow.getAllTableData();
			currentCalDateTime = Calendar.getInstance();
			earliestCalDateTime = Calendar.getInstance();
			// there is some latency for the time ranges
			eventTimeLatencyAdjust = calculateDateAdjustWithLatencyInMinutes(timeRange);
			// Events before this time, shouldn't be in the table
			/*
			 * TODO: maybe the current datetime should be subtracted with the
			 * latency as well, this doesn't mean the eventTimeLatencyAdjust,
			 * just the latency without the minutes of the time range
			 */
			earliestCalDateTime.add(Calendar.MINUTE, -eventTimeLatencyAdjust);
			if (!isDrillDownOnFailuresDataValid(headers, failuresInTable, failuresForDesignatedOne, currentCalDateTime,
					earliestCalDateTime, eventTimeDateFormat, timeRange))
				return false;
		}

		return true;
	}

    /* Data validation for TestExtendeCauseValueDeatilScreenCallSetUpFailuresGSMCFR166 and 
    TestExtendeCauseValueDeatilScreenCallSetUpFailuresGSMCFR166 */

    // Captured window details are passed as arguments. 

    public boolean validateDataDrillDownFailures(List<Map<String, String>> uiDataList) {
        //Initialising a variable to check if the IMSI of interest was verified.                

        boolean imsiFound = false;

        //ui info in the form of List of Maps being converted to a Map (uiDataMap) and each of instance of the Map is a row that is displayed. 

        for (final Map<String, String> uiDataMap : uiDataList) {

            //As Event time is not compared, its removed from the uiDataMap                  
            uiDataMap.remove(GuiStringConstants.EVENT_TIME);

            //printMapData(uiDataMap, "UI DATA MAP");

            //Capturing reserve data in the form of List (of Maps) resvDataMap.

            for (final Map<String, String> resvDataMap : reserveDataList) {

                //printMapData(resvDataMap, "Reserve DATA MAP");

                String imsi = resvDataMap.get(GuiStringConstants.IMSI);

                System.out.println("IMSI from reserve Data is:         " + imsi);

                // If the IMSI you are validating is not found in ResvData then continue

                if (!imsi.equals("460000025651469")) {
                    continue;

                }

                //System.out.println("***************************IMSI found*************************************** ");

                imsiFound = true;

                for (final String headerName : GSMConstants.DEFAULT_TERMINAL_EVENT_ANALYSIS_GSM_CFA_WINDOW_HEADERS_DETAILS_WINDOW) {
                    //The below logic is to avoid Event Time again as the header values fetched in the variable headerName will contain Event_Time.                     
                    if (GuiStringConstants.EVENT_TIME.equals(headerName)) {

                        continue;
                    }

                    String resvDataValue = resvDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                    if (headerName.equals(GuiStringConstants.EXTENDED_CAUSE_GSM)) {
                        resvDataValue = resvDataMap.get(GuiStringConstants.CAUSE_GROUP) + ", " + resvDataValue;

                        //System.out.println ("Reserve Built Extended Cuase Value:      " +resvDataValue);
                        //System.out.println ("UI Built Extended Cuase Value:   " +uiDataValue);
                    }

                    //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                        printMapData(uiDataMap, "UI DATA MAP");
                        printMapData(resvDataMap, "Resv DATA MAP");

                        return false; // FAIL the testcase
                    }
                }
            }
        }
        //If the IMSI of interest is not found, then the test case would be failed.    

        if (imsiFound == false) {
            // Fail the testcase
            return false;
        }

        return true; // PASS testcase

    }

    /* 165 Test Case */

    public boolean validateDataForCauseGroup(List<Map<String, String>> uiDataList, int factor, int timeInMinutes) {
        int resvFailureCount = 0;

        getExtendedCauseCodeValueFromDB();

        // Loop through UI list and get every row
        for (final Map<String, String> uiDataMap : uiDataList) {

            // Get cause code for each row from UI
            String uiCauseGroup = uiDataMap.get(GuiStringConstants.CAUSE_GROUP_ID);

            System.out.println("UI Cause Group:   " + uiCauseGroup);
            
            // Interate through reserve data list and check the cause code against UI cause code
            // and if cause code matches increment the failure count
            for (final Map<String, String> resvDataMap : extendedCauseCodeResvDataList) {
                String resvCauseGroup = resvDataMap.get("cause_group");

                if (uiCauseGroup.equals(resvCauseGroup)) {
                    resvFailureCount++;
                }

            }

            // Since rob is for 5 minutes, multiply the number of failure count for each rob
            int totalFailureCount = resvFailureCount * factor * (timeInMinutes / 5);

            int uiFailureCount = Integer.parseInt(uiDataMap.get(GuiStringConstants.FAILURES));

            //System.out.println ("totalFailureCount:" + totalFailureCount);
            //System.out.println ("uiFailureCount:" + uiFailureCount);

            // Validate ui failure count with total failure count of resv data
            if (uiFailureCount != totalFailureCount) {
                System.out.println("UI Failure Count Not Equal to Total Failure Count");
                return false; // FAIL the testcase
            }

            // Reset resvFailureCount and get to the next cause code in ui
            resvFailureCount = 0;

        }

        //System.out.println ("Successfully Validated.");     
        return true; // PASS testcase

    }

    /* For Extended Cause Code 165_EC */
    public boolean validateDataForExtendedCauseCode(List<Map<String, String>> uiDataList, String causeGroup, int factor,
            int timeInMinutes) {
        // Interate through reserve data list and check the cause code against UI cause code
        // and if cause code matches increment the failure count
        getExtendedCauseCodeValueFromDB();

        for (final Map<String, String> resvDataMap : extendedCauseCodeResvDataList) {

            //System.out.println ("Data from DB read");
            String resvCauseGroup = resvDataMap.get(GuiStringConstants.CAUSE_GROUP_DESCRIPTION_DB);

            //System.out.println ("Value of Cause Group Passed:  " +causeGroup);

            //System.out.println ("Value of Reserve Cause Group Passed:  " +resvCauseGroup);

            if (causeGroup.equals(resvCauseGroup)) {

                resvDataMap.remove("cause_group_desc");
                resvDataMap.remove("cause_group");

                System.out.println("Removed the unnecessary columns");

                // Since rob is for 5 minutes, multiply the number of failure count for each rob
                int resvFailureCount = 1 * factor * (timeInMinutes / 5);

                //System.out.println ("Failure Count is " + resvFailureCount );

                resvDataMap.put(GuiStringConstants.FAILURES, Integer.toString(resvFailureCount));
                resvDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, "1");
                resvDataMap.put(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, resvDataMap.get("extended_cause"));
                resvDataMap.put(GuiStringConstants.EXTENDED_CAUSE_VALUE, resvDataMap.get("extended_cause_desc"));

                resvDataMap.remove("extended_cause");
                resvDataMap.remove("extended_cause_desc");

                //System.out.println ("Added the necessary columns");

                if (!uiDataList.contains(resvDataMap)) {

                    for (final Map<String, String> uiDataMap : uiDataList) {
                        printMapData(uiDataMap, "UI DATA MAP");

                    }
                    printMapData(resvDataMap, "RESERVE DATA MAP");
                    // Expected data not found in UI
                    return false; // FAIL testcase
                }

            }

        }

        return true;
    }

    public void getExtendedCauseCodeValueFromDB() {
        final String queryForExtendedCauseCode = "select cg.cause_group, cg.cause_group_desc, cge.extended_cause, cge.extended_cause_desc from dim_e_gsm_cfa_cause_group cg, dim_e_gsm_cfa_extended_cause cge where cg.cause_group = cge.cause_group and cge.extended_cause not in (28,71,72,73,74,75,76,78,79,80,81,82)";
        final String extendedCauseCodeCoulmns = "cause_group, cause_group_desc, extended_cause, extended_cause_desc";
        extendedCauseCodeResvDataList = dbPersistor.queryExtendedCauseCodeValue(extendedCauseCodeCoulmns,
                queryForExtendedCauseCode);

        dbPersistor.closeConnection();
    }
    
   public boolean validateMSISDNSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    	
    	int exp;
    	
    	for (final Map<String, String> uiDataMap : uiDataList) {    		
    		
    		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
    			exp = 1;
    		} else {     			
    			exp = 2; 
    		}
    		
    		switch (exp) {
    		
    		case 1:
    			
    	    	for (final Map<String, String> resvDataMap : reserveDataList) {
    	    		Map<String, String> tempDataMap = new HashMap<String, String>();
    	    		
   	            if (resvDataMap.get("Scenario").equals("174,CSF")) {

    	            	System.out.println("-----------------------**********CSF Executed****************---------------------");
    	            	 	                
    	            	String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
    					int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
    		            
    					tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
    					tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
    					tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
    	                
    	                for (final String headerName : GSMConstants.MSISDN_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS) {

    	                	String resvDataValue = tempDataMap.get(headerName);
    	                    String uiDataValue = uiDataMap.get(headerName);

    	                 //Validation of each field from UI & the reserved data.                    

    	                    if (!resvDataValue.equals(uiDataValue)) {
    	                       return false; // FAIL the testcase
    	                    }
    	                }

    	            }
   	         tempDataMap.clear();
    		}
    	    break;	
    		case 2:
    			
    			for (final Map<String, String> resvDataMap : reserveDataList) {
    				Map<String, String> tempDataMap = new HashMap<String, String>();
    				if (resvDataMap.get("Scenario").equals("174,CD")) {
    	            	System.out.println("-----------------------**********CD Executed****************---------------------");
    	            	    	                
    	            	String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
    					int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
    		            
    					tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
    					tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
    					tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
    	                
    	                
    	                for (final String headerName : GSMConstants.MSISDN_SUBSCRIBER_ANALYSIS_SUMMARY_GSM_CFA_WINDOW_HEADERS) {

    	                	String resvDataValue = tempDataMap.get(headerName);
    	                    String uiDataValue = uiDataMap.get(headerName);

    	                 //Validation of each field from UI & the reserved data.                    

    	                    if (!resvDataValue.equals(uiDataValue)) {
    	                       return false; // FAIL the testcase
    	                    }
    	                }
    	            }
    				tempDataMap.clear();
    			}
    			break;
    		}    	
    
    	}    	
 return true;
} 
    	
    	
public boolean validateBSCGroupSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
        
    	for (final Map<String, String> uiDataMap : uiDataList) {

		int exp;
		
    		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
    			exp =1;
    		} else {     			
    			exp =2; 
    		}
    		
    		switch (exp) {
    		
    		case 1:
		
            for (final Map<String, String> resvDataMap : reserveDataList) {
            	
            	Map<String, String> tempDataMap = new HashMap<String, String>();
			if (resvDataMap.get("Scenario").equals("795,CSF")) {
                
				System.out.println("-----------------------**********CSF Executed****************---------------------");
				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
	            
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
				tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerGroupSummaryWindow) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
            }
			break;
            
			case 2:

            for (final Map<String, String> resvDataMap : reserveDataList) {
			
            	Map<String, String> tempDataMap = new HashMap<String, String>();
			if (resvDataMap.get("Scenario").equals("795,CD")) {
                
				System.out.println("-----------------------**********CD Executed****************---------------------");
				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
	            
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
				tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerGroupSummaryWindow) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}	
			tempDataMap.clear();
            }
			break;
		}
		}
        return true;
    }
    
public boolean validateBSCGroupCallSetUpFailuresSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.CONTROLLER).equals("BSCB01")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("797,CSF,BSCB01")) {
            
			System.out.println("-----------------------**********Controller 1 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER)); 
                       
            
            
			for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		
		}
		break;
        case 2:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("797,CSF,BSCB02")) {
			
			System.out.println("-----------------------**********Controller 2 Executed****************---------------------");
            
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER)); 
                       
            
            
			for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		
		}
		break;
  }
 }

    return true;
}
    
public boolean validateBSCGroupCallDropsSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.CONTROLLER).equals("BSCB01")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("796,CD,BSCB01")) {
            
			System.out.println("-----------------------**********Controller 1 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER)); 
                       
            
			for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		}
		break;
        case 2:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("796,CD,BSCB02")) {
			
			System.out.println("-----------------------**********Controller 2 Executed****************---------------------");
            
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER)); 
                       
            
            
			for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		}
		break;
  }
}
	return true;
}

public boolean validateBSCSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("286,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CSF events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("286,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CD events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}		
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}

public boolean validateCauseCodeAnalysisBSCSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("367,CFA")) {
                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
				//sample
				System.out.println("time = " + timeInMinutes);
				
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP_ID, resvDataMap.get(GuiStringConstants.CAUSE_GROUP_ID));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP, resvDataMap.get(GuiStringConstants.CAUSE_GROUP));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                //sample code

				for (final String headerName : NetworkTabTestGroupGSM.controllerCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);
                    
                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                    
                }
				}	
			
			tempDataMap.clear();
			
		}
		
	}
	return true;
}

public boolean validateExtendedCauseCodeAnalysisBSCSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("856,CFA")) {                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_ID_GSM));				
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_VALUE, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_VALUE));                
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerSubCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
	}

public boolean validateCCACauseGroupBSCGroupSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("378,CG")) {
                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP_ID, resvDataMap.get(GuiStringConstants.CAUSE_GROUP_ID));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP, resvDataMap.get(GuiStringConstants.CAUSE_GROUP));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
}

public boolean validateExtendedCauseCCABSCGroupSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("380,CG")) {                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_ID_GSM));				
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_VALUE, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_VALUE));                
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerSubCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
	}

public boolean validateCCAIndividualBSCSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.CONTROLLER).equals("BSCB01")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("379,BSCB01")) {
            
			System.out.println("-----------------------**********BSCB01 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CSF events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));           
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupCCASummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
        	
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("379,BSCB02")) {
            
			System.out.println("-----------------------BSC02Execute****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CSF events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));           
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.IMPACTED_CELLS, resvDataMap.get(GuiStringConstants.IMPACTED_CELLS));
			tempDataMap.put(GuiStringConstants.CONTROLLER, resvDataMap.get(GuiStringConstants.CONTROLLER));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupCCASummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}

public boolean validateCCAcauseGroupAccessAreaSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			if (resvDataMap.get("Scenario").equals("914,AccessArea")) {
                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP_ID, resvDataMap.get(GuiStringConstants.CAUSE_GROUP_ID));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP, resvDataMap.get(GuiStringConstants.CAUSE_GROUP));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
}

public boolean validateExtendedCauseCCAAccessArea(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("957,AccessArea")) {                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_ID_GSM));				
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_VALUE, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_VALUE));                
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerSubCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
	}

boolean validateCCAcauseGroupAccessAreaGroupSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("386,AccessGroup")) {
                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP_ID, resvDataMap.get(GuiStringConstants.CAUSE_GROUP_ID));
				tempDataMap.put(GuiStringConstants.CAUSE_GROUP, resvDataMap.get(GuiStringConstants.CAUSE_GROUP));
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
}

public boolean validateExtendedCauseCCAAccessAreaGroup(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {
		
		for (final Map<String, String> resvDataMap : reserveDataList) {
			
			Map<String, String> tempDataMap = new HashMap<String, String>();
			
			if (resvDataMap.get("Scenario").equals("387,AccessGroup")) {                
				   				
				String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
				int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				
	
				tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_ID_GSM, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_ID_GSM));				
				tempDataMap.put(GuiStringConstants.EXTENDED_CAUSE_VALUE, resvDataMap.get(GuiStringConstants.EXTENDED_CAUSE_VALUE));                
				tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                
                for (final String headerName : NetworkTabTestGroupGSM.controllerSubCauseCodeNetworkSubMenus) {

                	String resvDataValue = tempDataMap.get(headerName);
                    String uiDataValue = uiDataMap.get(headerName);

                 //Validation of each field from UI & the reserved data.                    

                    if (!resvDataValue.equals(uiDataValue)) {
                       return false; // FAIL the testcase
                    }
                }
				}		
			tempDataMap.clear();
		}
		
	}
	return true;
	}

public boolean validateCCAIndividualAccessAreaSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.ACCESS_AREA).equals("CB01002")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("385,CB01002")) {
            
			System.out.println("-----------------------**********BSCB01 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CSF events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));           
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));
            
            for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupIndivCCASummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("385,CB02002")) {
            
			System.out.println("-----------------------BSC02Execute****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CSF events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));           
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));
            
            for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupIndivCCASummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}
//Failure Count

public boolean validateCallFailureIMSINumber(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("557,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
            
			System.out.println("tempdata---"+tempDataMap);
            for (final String headerName : SubscriberTabTestGroupGSM.IMSISummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);
                
             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("557,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));            
            
            for (final String headerName : SubscriberTabTestGroupGSM.IMSISummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}
//IMSI Group Failure count
public boolean validateCallFailureIMSIGroup(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("579,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
                        
            for (final String headerName : SubscriberTabTestGroupGSM.IMSIGroupSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}		
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		if (resvDataMap.get("Scenario").equals("579,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));            
            
            for (final String headerName : SubscriberTabTestGroupGSM.IMSIGroupSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}


//Cause Group IMSI validation

boolean validateCauseGroupIMSINumber(List<Map<String, String>> uiDataList, int timeInMinutes) {
  
	for (final Map<String, String> uiDataMap : uiDataList) {
		
	for (final Map<String, String> resvDataMap : reserveDataList) {
		Map<String, String> tempDataMap = new HashMap<String, String>();
	if (resvDataMap.get("Scenario").equals("164,CD")) {
    
	   				
		String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
		int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

		tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
		tempDataMap.put(GuiStringConstants.CAUSE_GROUP_ID, resvDataMap.get(GuiStringConstants.CAUSE_GROUP_ID));
		tempDataMap.put(GuiStringConstants.CAUSE_GROUP, resvDataMap.get(GuiStringConstants.CAUSE_GROUP));
		tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
        for (final String headerName : NetworkTabTestGroupGSM.controllerSubCauseCodeNetworkSubMenus) {

    	String resvDataValue = tempDataMap.get(headerName);
        String uiDataValue = uiDataMap.get(headerName);

     //Validation of each field from UI & the reserved data.                    

        if (!resvDataValue.equals(uiDataValue)) {
           return false; // FAIL the testcase
            }
        }
		}		
	tempDataMap.clear();
}

	}
	return true;
}


//Roaming Analysis

public boolean validateRoamingAnalysisCountry(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("350,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                        
            for (final String headerName : GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
        	
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		
		if (resvDataMap.get("Scenario").equals("350,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
             
            for (final String headerName : GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}

// Roaming Analysis Operator

public boolean validateRoamingAnalysisOperator(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("764,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
                        
            for (final String headerName : GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("764,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
             
            for (final String headerName : GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}
    
// Arun code 

public boolean validateCellGroupSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.EVENT_TYPE).equals(GuiStringConstants.CALL_SETUP_FAILURES)){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		
		if (resvDataMap.get("Scenario").equals("798,CSF")) {
            
			System.out.println("-----------------------**********CSF Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
        
		case 2:

        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("798,CD")) {
            
			System.out.println("-----------------------**********CD Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5);				

			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
            
            for (final String headerName : NetworkTabTestGroupGSM.controllerGroupSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }
			}	
		tempDataMap.clear();
        }
		break;
	}
	}
    return true;
}



public boolean validateCellGroupCallSetUpFailuresSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.ACCESS_AREA).equals("CB01002")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("800,CSF,CB01002")) {
            
			System.out.println("-----------------------**********Access 1 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CD events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));        
            
			for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		}
		break;
        case 2:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("800,CSF,CB02002")) {
			
			System.out.println("-----------------------**********Controller 2 Executed****************---------------------");
            
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CD events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));        
            
			for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		}
		break;
  }
 }

    return true;
}


public boolean validateCellGroupCallDropsSummary(List<Map<String, String>> uiDataList, int timeInMinutes) {
    
	for (final Map<String, String> uiDataMap : uiDataList) {

	int exp;
	
		if (uiDataMap.get(GuiStringConstants.ACCESS_AREA).equals("CB01002")){
			exp =1;
		} else {     			
			exp =2; 
		}
		
		switch (exp) {
		
		case 1:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
        	
        	Map<String, String> tempDataMap = new HashMap<String, String>();
		
		if (resvDataMap.get("Scenario").equals("799,CD,CB01002")) {
            
			System.out.println("-----------------------**********Controller 1 Executed****************---------------------");
			
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CD events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));
			           
            
			for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		
		}
		break;
        case 2:
	
        for (final Map<String, String> resvDataMap : reserveDataList) {
		
        	Map<String, String> tempDataMap = new HashMap<String, String>();
        	
		if (resvDataMap.get("Scenario").equals("799,CD,CB02002")) {
			
			System.out.println("-----------------------**********Controller 2 Executed****************---------------------");
            
			String temp = resvDataMap.get(GuiStringConstants.FAILURES);												
			int csfresvFailureCount =  Integer.parseInt(temp) * (timeInMinutes / 5); // 244 is the number of CD events per 5 minute rop for the selected BSC. 
            
			tempDataMap.put(GuiStringConstants.FAILURES, Integer.toString(csfresvFailureCount));
			tempDataMap.put(GuiStringConstants.EVENT_TYPE, resvDataMap.get(GuiStringConstants.EVENT_TYPE));
			tempDataMap.put(GuiStringConstants.FAILURE_RATIO, resvDataMap.get(GuiStringConstants.FAILURE_RATIO));
			tempDataMap.put(GuiStringConstants.IMPACTED_SUBSCRIBERS, resvDataMap.get(GuiStringConstants.IMPACTED_SUBSCRIBERS));
			tempDataMap.put(GuiStringConstants.RAN_VENDOR, resvDataMap.get(GuiStringConstants.RAN_VENDOR));
			tempDataMap.put(GuiStringConstants.ACCESS_AREA, resvDataMap.get(GuiStringConstants.ACCESS_AREA));
            
			for (final String headerName : NetworkTabTestGroupGSM.accessAreaGroupEventSummaryWindow) {

            	String resvDataValue = tempDataMap.get(headerName);
                String uiDataValue = uiDataMap.get(headerName);

             //Validation of each field from UI & the reserved data.                    

                if (!resvDataValue.equals(uiDataValue)) {
                   return false; // FAIL the testcase
                }
            }

        }
		tempDataMap.clear();
		
		}
		break;
  }
}
	return true;
}
    /*
	 * Calculates how many minutes should the starting date be set backward and
	 * adds also the latency according to the time range and the date given as a
	 * parameter. Latencies currently: 5min : 0, 15m, 30m, 1h, 2h: 7m; 6h, 12h,
	 * 1d: 45m>x>=30m, rounded down to the nearest quarter; 1w: rounded down to
	 * 00:00
	 */
	private int calculateDateAdjustWithLatencyInMinutes(TimeRange timeRange) {
		int latency = 0;
		int timeRangeInMinutes = timeRange.getMiniutes();
		// creating a new Calendar instance isn't a problem, since this method
		// is immediately called after creating
		// the ones in the validate... method
		Calendar tempCalendar = Calendar.getInstance();

		if (timeRangeInMinutes == 5)
			latency = 5;
		else if (timeRangeInMinutes < 360) // less, than six hours
			latency = timeRangeInMinutes + GSMConstants.LATENCY_FOR_LESS_THAN_6_HOURS;
		else if (timeRangeInMinutes < 10080) { // more, than six hours, but
												// less, than one week
			// first subtract the time according to the time range...
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			// and then calculate how much we should subtract to get the
			// "quarter before"
			latency = timeRangeInMinutes + (tempCalendar.get(Calendar.MINUTE) % 15);
		} else { // one week
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			latency = timeRangeInMinutes + tempCalendar.get(Calendar.MINUTE)
					+ ((tempCalendar.get(Calendar.HOUR_OF_DAY)) * 60);
		}

		return latency;
	}

	/*
	 * Checks if the two records are meaningfully equal by the given headers.
	 */
	protected boolean checkForEqualityByHeaders(Map<String, String> oneRecord,
			Map<String, String> oneRecordReserveData, List<String> headers) {

		for (String header : headers) {
			if (!oneRecord.get(header).equals(oneRecordReserveData.get(header))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculates the number of failures according to the argument provided.
	 * E.g. for the access areas, for the BSCs etc.
	 * 
	 * @param designatedHeader
	 */
	public Map<String, Integer> calculateNumFailuresMap(String designatedHeader) {

		Map<String, Integer> failuresMap = new HashMap<String, Integer>();
		
		System.out.println("failuresMap" + failuresMap);

		
		for (Map<String, String> oneRecord : reserveDataList) {
			Integer releaseType = Integer.parseInt(oneRecord.get(GSMConstants.RELEASE_TYPE_ID));
			Integer urgencyCondition = Integer.parseInt(oneRecord.get(GSMConstants.URGENCY_CONDITION_ID));
			Integer extendedCause = Integer.parseInt(oneRecord.get(GSMConstants.EXTENDED_CAUSE_ID));
			String eventName = oneRecord.get(GSMConstants.EVENT_NAME);
			String bsc = oneRecord.get(GSMConstants.CONTROLLER);
			Integer numOfFailuresThisRecordOnly = Integer.parseInt(oneRecord.get(GSMConstants.NO_OF_EVENTS));
			Integer numOfFailuresInMap = 0;
			Integer newTotal = 0;
			
			//if the eventType is a failure
			if (!(releaseType == 0 && urgencyCondition == 2 && (extendedCause == 2 || extendedCause == 1))
					&& eventName.equals(GSMConstants.CS_CALL_RELEASE) && (!(bsc.equals(GSMConstants.NO_BSC)))) {
				//check to see if the BSC, Access Area, Imsi etc.. already exists in the Map
				if (failuresMap.containsKey(oneRecord.get(designatedHeader))){			
					//Get the current value of the record and add it to the number of failed events from the oneRecord
					numOfFailuresInMap = failuresMap.get(oneRecord.get(designatedHeader));
					newTotal = numOfFailuresInMap + numOfFailuresThisRecordOnly;
					//Then put the newTotal into the Map
					failuresMap.put(oneRecord.get(designatedHeader), newTotal);				
				}else{
					//just put the new entry directly into the failuresMap
					failuresMap.put(oneRecord.get(designatedHeader), numOfFailuresThisRecordOnly);
					
				}
			}
			
		}
		System.out.println("Final failures map" + failuresMap);
		return failuresMap;
	}

	/**
	 * Calculates the number of failures for a particular access area, BSC etc.
	 * 
	 * @param designatedHeader
	 * @param valueForDesignatedHeader
	 */
	public int calculateNumFailures(String designatedHeader, String valueForDesignatedHeader) {

		int failures = 0;

		for (Map<String, String> oneRecord : reserveDataList) {
			System.out.println("For comparison : " + oneRecord.get(designatedHeader) + " AND " + valueForDesignatedHeader);
			if (oneRecord.get(designatedHeader).equals(valueForDesignatedHeader)
					&& (!(oneRecord.get(GSMConstants.RELEASE_TYPE).equals("")))
					&& (!(oneRecord.get(GSMConstants.URGENCY_CONDITION).equals("")))
					&& (!(oneRecord.get(GSMConstants.EXTENDED_CAUSE_VALUE).equals("")))) {
				System.out.println("Inside if : " + oneRecord.get(designatedHeader) + " AND " + valueForDesignatedHeader);
				System.out.println("IN");
				System.out.println(oneRecord);

				Integer releaseType = Integer.parseInt(oneRecord.get(GSMConstants.RELEASE_TYPE_ID));
				Integer urgencyCondition = Integer.parseInt(oneRecord.get(GSMConstants.URGENCY_CONDITION_ID));
				Integer extendedCause = Integer.parseInt(oneRecord.get(GSMConstants.EXTENDED_CAUSE_ID));
				String eventName = oneRecord.get(GSMConstants.EVENT_NAME);
				String bsc = oneRecord.get(GSMConstants.CONTROLLER);

				if (!(releaseType == 0 && urgencyCondition == 2 && (extendedCause == 2 || extendedCause == 1))
						&& eventName.equals(GSMConstants.CS_CALL_RELEASE) && (!(bsc.equals(GSMConstants.NO_BSC)))) {
					failures += (Integer.parseInt(oneRecord.get(GSMConstants.NO_OF_EVENTS)));
				}

			}

		}
		System.out.println("NumofFailures = " + failures);
		return failures;
	}

	/**
	 * Calculates the number of failures for a particular access area, BSC etc.
	 * 
	 * @param designatedHeader
	 * @param valueForDesignatedHeader
	 */
	public int calculateNumFailuresForGroups(String groupType, String[] groupValues) {

		int failures = 0;

		for (String value : groupValues) {

			for (Map<String, String> oneRecord : reserveDataList) {
				System.out.println("oneRecord :  " + oneRecord);
				if (oneRecord.get(groupType).equals(value) && (!(oneRecord.get(GSMConstants.RELEASE_TYPE).equals("")))
						&& (!(oneRecord.get(GSMConstants.URGENCY_CONDITION).equals("")))
						&& (!(oneRecord.get(GSMConstants.EXTENDED_CAUSE).equals("")))) {
					Integer releaseType = Integer.parseInt(oneRecord.get(GSMConstants.RELEASE_TYPE));
					Integer urgencyCondition = Integer.parseInt(oneRecord.get(GSMConstants.URGENCY_CONDITION));
					Integer extendedCause = Integer.parseInt(oneRecord.get(GSMConstants.EXTENDED_CAUSE));
					String eventName = oneRecord.get(GSMConstants.EVENT_NAME);
					String bsc = oneRecord.get(GSMConstants.CONTROLLER);

					if (!(releaseType == 0 && urgencyCondition == 2 && (extendedCause == 2 || extendedCause == 1))
							&& eventName.equals(GSMConstants.CS_CALL_RELEASE)) {
						failures += (Integer.parseInt(oneRecord.get(GSMConstants.NO_OF_EVENTS)));
					}

				}

			}
		}
		System.out.println("Number of failures = " + failures);
		return failures;
	}

	public int calculateNumSuccesses(String designatedHeader, String valueForDesignatedHeader) {

		int successes = 0;

		for (Map<String, String> oneRecord : reserveDataList) {
			if (oneRecord.get(designatedHeader).equals(valueForDesignatedHeader)
					&& (!(oneRecord.get(GSMConstants.RELEASE_TYPE).equals("")))
					&& (!(oneRecord.get(GSMConstants.URGENCY_CONDITION).equals("")))
					&& (!(oneRecord.get(GSMConstants.EXTENDED_CAUSE_VALUE).equals("")))) {
				Integer releaseType = Integer.parseInt(oneRecord.get(GSMConstants.RELEASE_TYPE_ID));
				Integer urgencyCondition = Integer.parseInt(oneRecord.get(GSMConstants.URGENCY_CONDITION_ID));
				Integer extendedCause = Integer.parseInt(oneRecord.get(GSMConstants.EXTENDED_CAUSE_ID));
				System.out.println("" + releaseType + " " + urgencyCondition + " " + extendedCause);
				String eventName = oneRecord.get(GSMConstants.EVENT_NAME);
				String bsc = oneRecord.get(GSMConstants.CONTROLLER);

				if (releaseType == 0 && urgencyCondition == 2 && (extendedCause == 2 || extendedCause == 1)
						&& eventName.equals(GSMConstants.CS_CALL_RELEASE) && (!(bsc.equals(GSMConstants.NO_BSC)))) {
					successes += Integer.parseInt(oneRecord.get(GSMConstants.NO_OF_EVENTS));
				}
			}

		}

		return successes;
	}

	/**
	 * Calculates the number of impacted subscribers for a particular access
	 * area, BSC etc.
	 * 
	 * @param designatedHeader
	 * @param valueForDesignatedHeader
	 */
	public int calculateImpactedSubscribers(String designatedHeader, String valueForDesignatedHeader) {

		Set<String> differentIMSIs = new HashSet<String>();
		String IMSIReadFromReserveData;

		for (Map<String, String> oneRecord : reserveDataList) {
			if (oneRecord.get(designatedHeader).equals(valueForDesignatedHeader)) {
				IMSIReadFromReserveData = oneRecord.get(GuiStringConstants.IMEISV);
				differentIMSIs.add(IMSIReadFromReserveData);
			}
		}
		differentIMSIs.remove("");
		return differentIMSIs.size();
	}

	/**
	 * Calculates the number of impacted cells for a particular Controller (BSC)
	 * etc.
	 * 
	 * @param designatedHeader
	 * @param valueForDesignatedHeader
	 */
	public int calculateImpactedCells(String designatedHeader, String valueForDesignatedHeader) {

		Set<String> differentCells = new HashSet<String>();
		String cellReadFromReserveData;
		Integer releaseType;
		Integer urgencyCondition;
		Integer extendedCause;

		for (Map<String, String> oneRecord : reserveDataList) {
			releaseType = Integer.parseInt(oneRecord.get(GuiStringConstants.RELEASE_TYPE));
			urgencyCondition = Integer.parseInt(oneRecord.get(GuiStringConstants.URGENCY_CONDITION));
			extendedCause = Integer.parseInt(oneRecord.get(GuiStringConstants.EXTENDED_CAUSE));

			if (oneRecord.get(designatedHeader).equals(valueForDesignatedHeader)
					&& (!(releaseType == 0 && urgencyCondition == 2 && extendedCause == 2 || extendedCause == 1))) {
				cellReadFromReserveData = oneRecord.get(GuiStringConstants.ACCESS_AREA);
				differentCells.add(cellReadFromReserveData);
			}
		}
		differentCells.remove("");
		return differentCells.size();
	}

	public double calculateFailureRatio(Integer numOfFailures, Integer numOfSuccesses) {
		System.out.println("A " + numOfFailures);
		System.out.println("B " + numOfSuccesses);
		String numOfFailuresString = Integer.toString(numOfFailures);
		String numOfSuccessesString = Integer.toString(numOfSuccesses);
		Double numOfFailuresDouble = Double.parseDouble(numOfFailuresString);
		Double numOfSuccessesDouble = Double.parseDouble(numOfSuccessesString);

		double failureRatio = (numOfFailuresDouble / (numOfFailuresDouble + numOfSuccessesDouble)) * 100;
		System.out.println("C " + failureRatio);
		DecimalFormatSymbols dotDecimalSeparator = new DecimalFormatSymbols();
		dotDecimalSeparator.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", dotDecimalSeparator);
		System.out.println(df.format(failureRatio));
		failureRatio = Double.parseDouble(df.format(failureRatio));
		System.out.println("D " + failureRatio);
		return failureRatio;
	}

	/**
	 * General method for retrieving the failures of a particular BSC, Access
	 * Area etc.
	 * 
	 * @param headers
	 * @param type
	 *            Controller, Access Area etc., should be a valid header in the
	 *            reserve data
	 * @param value
	 *            The name we search in the column defined by type (e.g. name of
	 *            the Access Area)
	 * @return
	 */
	public List<Map<String, String>> getFailures(List<String> headers, String type, String value) {
		List<Map<String, String>> failures = new ArrayList<Map<String, String>>();
		Map<String, String> tempHeaderMap;
		String searchedForInReserveDataRecord;
		for (Map<String, String> relevantReserveDataRecord : reserveDataList) {
			searchedForInReserveDataRecord = relevantReserveDataRecord.get(type);
			if ((searchedForInReserveDataRecord != null) && (searchedForInReserveDataRecord.equals(value))) {
				tempHeaderMap = new HashMap<String, String>();
				for (String header : headers) {
					tempHeaderMap.put(header, relevantReserveDataRecord.get(header));
				}

				failures.add(tempHeaderMap);
			}
		}

		return failures;
	}

	/*
	 * Loads the reserve data from the csv file.
	 */
	protected void loadCsv(String csvFileName, List<String> headers) {
		try {
			// there are records in the reserve data with commas in it, so
			// semicolon should be used as a delimeter`
			
			CsvRead gsmCFAReservedCSV = new CsvRead(csvFileName, ';');

			gsmCFAReservedCSV.readHeaders(); // the first row are the headers

	                //CsvReader gsmCFAReservedCSV = new CsvReader(csvFileName);  // From LTE CFA
                        //gsmCFAReservedCSV.readHeaders(); // the first row are the headers
			
			while (gsmCFAReservedCSV.readRecord()) {
				Map<String, String> csvRecordMap = new HashMap<String, String>();
				for (String header : headers) {
					csvRecordMap.put(header, gsmCFAReservedCSV.get(header));
				}

				printMapData(csvRecordMap);

				reserveDataList.add(csvRecordMap);
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.out.println("File " + csvFileName + " not found.");
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		System.out.println("Records read: " + reserveDataList.size());
	}

	protected void printErrorReason(String calledFrom, String header, String expected, String found, TimeRange timeRange) {
		System.out.println(" *** Data Integrity Failed ***");
		System.out.println("Header:\t\t" + header);
		System.out.println("Expected:\t " + expected);
		System.out.println("Found:\t\t" + found);
		System.out.println("Time range:\t" + timeRange);
		System.out.println("Called fromA :\t" + calledFrom);
	}

	protected void printErrorReason(String calledFrom, String header, int expected, int found, TimeRange timeRange) {
		System.out.println(" *** Data Integrity Failed ***");
		System.out.println("Header:\t\t" + header);
		System.out.println("Expected:\t" + expected);
		System.out.println("Found:\t\t" + found);
		System.out.println("Time range:\t" + timeRange);
		System.out.println("Called fromB :\t" + calledFrom);
	}

	/*
	 * Prints the data contained in the map.
	 */
	protected void printMapData(Map<String, String> mapToPrint) {
		System.out.println(" *** *** *** *** ***");

		Set<Map.Entry<String, String>> set = mapToPrint.entrySet();
		Iterator<Map.Entry<String, String>> iter = set.iterator();

		while (iter.hasNext()) {
			Map.Entry<String, String> mapEntry = (Map.Entry<String, String>) iter.next();
			System.out.print(mapEntry.getKey() + ": ");
			System.out.println(mapEntry.getValue());
		}
	}

	public boolean validateDrillDownAllTimeRanges(CommonWindow commonWindow, String designatedHeader,
			String valueForDesignatedHeader) throws PopUpException, NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		int numFailuresReserveData = 0;
		int numSuccessesReserveData = 0;
		int impactedSubscribersReserveData = 0;
		int impactedCellsReserveData = 0;
		double failureRatio = 0;

		headers = commonWindow.getTableHeaders();
		// there is no "Event Time" column in the reserve data
		if (headers.contains(GuiStringConstants.EVENT_TIME))
			headers.remove(GuiStringConstants.EVENT_TIME);

		// there is no "MSISDN" column in the reserve data
		if (headers.contains(GuiStringConstants.MSISDN))
			headers.remove(GuiStringConstants.MSISDN);
		
		// there is no "Rank" column in the reserve data
		if (headers.contains(GuiStringConstants.RANK))
			headers.remove(GuiStringConstants.RANK);

		if (headers.contains(GuiStringConstants.FAILURES)) {
			numFailuresReserveData = calculateNumFailures(designatedHeader, valueForDesignatedHeader);
			System.out.println("Failures Header : " + designatedHeader);
			System.out.println("Value for Failures Header : " + valueForDesignatedHeader);
		}

		if (headers.contains(GuiStringConstants.FAILURE_RATIO)) {
			numSuccessesReserveData = calculateNumSuccesses(designatedHeader, valueForDesignatedHeader);
			failureRatio = calculateFailureRatio(numFailuresReserveData, numSuccessesReserveData);
		}

		if (headers.contains(GuiStringConstants.IMPACTED_SUBSCRIBERS))
			impactedSubscribersReserveData = this.calculateImpactedSubscribers(designatedHeader,
					valueForDesignatedHeader);

		if (headers.contains(GuiStringConstants.IMPACTED_CELLS))
			impactedCellsReserveData = this.calculateImpactedCells(designatedHeader, valueForDesignatedHeader);

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			if (!checkGridRowDataIntegrity(commonWindow, timeRange, designatedHeader, valueForDesignatedHeader,
					headers, numFailuresReserveData, impactedSubscribersReserveData, impactedCellsReserveData,
					failureRatio)) {
				return false;
			}
		}

		return true;
	}

	public boolean validateDrillDownAllTimeRangesForGroupsDetailedView(CommonWindow commonWindow,
			String designatedHeader, String valueForDesignatedHeader, int rowNumber) throws PopUpException,
			NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		int numFailuresReserveData = 0;
		int numSuccessesReserveData = 0;
		int impactedSubscribersReserveData = 0;
		int impactedCellsReserveData = 0;
		double failureRatio = 0;

		headers = commonWindow.getTableHeaders();

		// there is no "Event Time" column in the reserve data
		if (headers.contains(GuiStringConstants.EVENT_TIME))
			headers.remove(GuiStringConstants.EVENT_TIME);

		// there is no "MSISDN" column in the reserve data
		if (headers.contains(GuiStringConstants.MSISDN))
			headers.remove(GuiStringConstants.MSISDN);

		if (headers.contains(GuiStringConstants.FAILURES))
			System.out.println("valueForDesignatedHeader " + valueForDesignatedHeader);
		numFailuresReserveData = calculateNumFailures(designatedHeader, valueForDesignatedHeader);

		if (headers.contains(GuiStringConstants.FAILURE_RATIO)) {
			numSuccessesReserveData = calculateNumSuccesses(designatedHeader, valueForDesignatedHeader);
			failureRatio = calculateFailureRatio(numFailuresReserveData, numSuccessesReserveData);
		}
		// failureRatio = numFailuresReserveData/numSuccessesReserveData;

		if (headers.contains(GuiStringConstants.IMPACTED_SUBSCRIBERS))
			impactedSubscribersReserveData = this.calculateImpactedSubscribers(designatedHeader,
					valueForDesignatedHeader);

		if (headers.contains(GuiStringConstants.IMPACTED_CELLS))
			impactedCellsReserveData = this.calculateImpactedCells(designatedHeader, valueForDesignatedHeader);

		int numOfRowsInGUI = commonWindow.getAllTableData().size();

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			Map<String, String> GUIData = commonWindow.getAllDataAtTableRow(rowNumber);
			System.out.println("Passed GuiData" + GUIData);
			if (!checkGridRowDataIntegrityForGroupsMultipleRows(commonWindow, timeRange, designatedHeader,
					valueForDesignatedHeader, headers, numFailuresReserveData, impactedSubscribersReserveData,
					impactedCellsReserveData, failureRatio, numOfRowsInGUI, GUIData)) {
				return false;
			}
		}

		return true;
	}

	public boolean validateDrillDownAllTimeRangesForGroups(CommonWindow commonWindow, String designatedHeader,
			String[] valuesForDesignatedHeader) throws PopUpException, NoDataException {

		TimeRange[] timeRanges = getAllTimeRanges();
		List<String> headers = new ArrayList<String>();
		int numFailuresReserveData = 0;
		int totalNumFailuresReserveData = 0;
		int numSuccessesReserveData = 0;
		int totalNumSuccessesReserveData = 0;
		int impactedSubscribersReserveData = 0;
		int totalImpactedSubscribersReserveData = 0;
		int impactedCellsReserveData = 0;
		int totalImpactedCellsReserveData = 0;
		String valueDesignatedHeader = "";
		double failureRatio = 0;

		headers = commonWindow.getTableHeaders();

		for (String valueForDesignatedHeader : valuesForDesignatedHeader) {
			if (headers.contains(GuiStringConstants.FAILURES))
				numFailuresReserveData = calculateNumFailures(designatedHeader, valueForDesignatedHeader);
			totalNumFailuresReserveData = totalNumFailuresReserveData + numFailuresReserveData;

			if (headers.contains(GuiStringConstants.FAILURE_RATIO)) {
				numSuccessesReserveData = calculateNumSuccesses(designatedHeader, valueForDesignatedHeader);
				totalNumSuccessesReserveData = totalNumSuccessesReserveData + numSuccessesReserveData;
			}

			if (headers.contains(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
				impactedSubscribersReserveData = calculateImpactedSubscribers(designatedHeader,
						valueForDesignatedHeader);
				totalImpactedSubscribersReserveData = totalImpactedSubscribersReserveData
						+ impactedSubscribersReserveData;
			}

			if (headers.contains(GuiStringConstants.IMPACTED_CELLS)) {
				impactedCellsReserveData = calculateImpactedCells(designatedHeader, valueForDesignatedHeader);
				totalImpactedCellsReserveData = totalImpactedCellsReserveData + impactedCellsReserveData;
			}
			valueDesignatedHeader = valueForDesignatedHeader;
		}

		failureRatio = calculateFailureRatio(totalNumFailuresReserveData, totalNumSuccessesReserveData);

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			if (!checkGridRowDataIntegrity(commonWindow, timeRange, designatedHeader, valueDesignatedHeader, headers,
					totalNumFailuresReserveData, totalImpactedSubscribersReserveData, totalImpactedCellsReserveData,
					failureRatio)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Returns all time ranges.
	 */
	private TimeRange[] getAllTimeRanges() {
		return TimeRange.values();
	}

	private boolean checkGridRowDataIntegrity(CommonWindow commonWindow, TimeRange timeRange, String designatedHeader,
			String valueForDesignatedHeader, List<String> headers, int failuresReserveData,
			int impactedSubscribersReserveData, int impactedCellsReserveData, double failureRatioReservedData)
			throws NoDataException {

		boolean isDataValid;
		Map<String, String> drillDownTableRow = new HashMap<String, String>();

		drillDownTableRow = commonWindow.getAllDataAtTableRow(0);

		isDataValid = this.validateGridRowDataIncludingFailureRatio(headers, drillDownTableRow, timeRange,
				designatedHeader, valueForDesignatedHeader, failuresReserveData, impactedSubscribersReserveData,
				impactedCellsReserveData, failureRatioReservedData);

		return isDataValid;
	}

	private boolean checkGridRowDataIntegrityForGroupsMultipleRows(CommonWindow commonWindow, TimeRange timeRange,
			String designatedHeader, String valueForDesignatedHeader, List<String> headers, int failuresReserveData,
			int impactedSubscribersReserveData, int impactedCellsReserveData, double failureRatioReservedData,
			int numberOfRowsinGUIGrid, Map<String, String> GUIData) throws NoDataException {

		boolean isDataValid;
		Map<String, String> drillDownTableRow = new HashMap<String, String>();

		drillDownTableRow = GUIData;

		isDataValid = this.validateGridRowDataIncludingFailureRatio(headers, drillDownTableRow, timeRange,
				designatedHeader, valueForDesignatedHeader, failuresReserveData, impactedSubscribersReserveData,
				impactedCellsReserveData, failureRatioReservedData);

		return isDataValid;
	}
	
	private void printMapData(Map<String, String> mapToPrint, String trace)
        {
                String printString = "\n########################### " + trace + "  ##########################\n" + "Size of Map : " + mapToPrint.size()+"\n";                                   
                                
                for (Map.Entry<String, String> me : mapToPrint.entrySet())
                {
                        printString =  printString + me.getKey() + ": "+ me.getValue() + "\n";                                          
                }
                
                logger.log(Level.SEVERE, printString);          
        }

}
