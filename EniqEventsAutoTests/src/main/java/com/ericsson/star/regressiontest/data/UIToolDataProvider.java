
package com.ericsson.star.regressiontest.data;
import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import java.util.HashMap;
import java.util.Map;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;

/**
 *	DataProvider for executing Test Cases for UITool
 */
public class UIToolDataProvider implements DataProvider{
	
	private CsvReader UiToolCsvReader = DataHandler.readCsv("UiTool_UiTestData.csv",",");
	
	/**
	* Method to create Input test data from CliTestData Csv file
	* @param testCaseId
	* @return Map of contents of csv file
	*/
	public Map<String,Map<String, Object>> getCommands(String testCaseId){
		Map<String,Map<String,Object>> csvCommands = new HashMap<String,Map<String,Object>>();
		String temp1;
		
		for ( int i = 1; i < UiToolCsvReader.getRowCount(); i++){	
			if (UiToolCsvReader.getCell(0,i).contains(testCaseId)){
				temp1 = UiToolCsvReader.getCell(0,i);
				Map<String,Object> tempMap = csvCommands.get(temp1);
				if(tempMap == null){
					tempMap = new HashMap<String, Object>();
				}
				for(int j=1 ; j < UiToolCsvReader.getColumnCount()-1; j++ ){
					tempMap.put(UiToolCsvReader.getCell(j,0), UiToolCsvReader.getCell(j,i));
					csvCommands.put(temp1, tempMap);
				}
				int j = UiToolCsvReader.getColumnCount()-1;
				tempMap.put(UiToolCsvReader.getCell(j,0), getHosts(UiToolCsvReader.getCell(j,i)));
				csvCommands.put(temp1, tempMap);
			}
		}
		
		return csvCommands;
	}
	
	/**
	* Method to obtain host information
	* @param hostname
	* @return 
	*/
	private Host getHosts(String hostName) {
		Host result = DataHandler.getHostByName(hostName);
		return result;
	}
}