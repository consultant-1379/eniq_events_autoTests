package com.ericsson.star.regressiontest.data;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;

public class EniqEventsUIDataProvider {
	private CsvReader EniqEventsUICsvReader = DataHandler.readCsv("EniqEventsUI_TestData.csv",",");

	/**
	* Method to obtain host information
	* @param hostname
	* @return 
	*/
	private Host getHosts(String hostName) {
		Host result = DataHandler.getHostByName(hostName);
		return result;
	}

	/**
	* Method to create Input test data from CliTestData Csv file
	* @param testCaseId
	* @return Map of contents of csv file
	*/
	public Map<String,Map<String, Object>> getCommands(String testCaseId){
		Map<String,Map<String,Object>> csvCommands = new HashMap<String,Map<String,Object>>();
		String cellValue;
		for ( int i=1 ; i < EniqEventsUICsvReader.getRowCount(); i++ ){	
			if (EniqEventsUICsvReader.getCell(0,i).contains(testCaseId)){
				cellValue = EniqEventsUICsvReader.getCell(0,i);
				Map<String,Object> tempMap = csvCommands.get(cellValue);
				if(tempMap == null){
					tempMap = new HashMap<String, Object>();
				}
				for ( int j=1 ; j < EniqEventsUICsvReader.getColumnCount(); j++ ){
					tempMap.put(EniqEventsUICsvReader.getCell(j,0), EniqEventsUICsvReader.getCell(j,i));
				}
				csvCommands.put(cellValue, tempMap);
			}
		}
		return csvCommands;
	}
}
