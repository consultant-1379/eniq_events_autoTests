
package	com.ericsson.star.regressiontest.data;
import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import java.util.HashMap;
import java.util.Map;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;  

/**
 *	DataProvider for executing Test Cases for CliTool
 */
public class CliToolDataProvider implements DataProvider{


	private CsvReader CliToolCsvReader = DataHandler.readCsv("CliTool_CliTestData.csv",",");

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
		String temp1;
			for ( int i=1 ; i < CliToolCsvReader.getRowCount(); i++ ){	
				if (CliToolCsvReader.getCell(0,i).contains(testCaseId)){
					temp1 = CliToolCsvReader.getCell(0,i);
						Map<String,Object> tempMap = csvCommands.get(temp1);
							if(tempMap == null){
								tempMap = new HashMap<String, Object>();
							}
							for ( int j=1 ; j < CliToolCsvReader.getColumnCount()-1; j++ ){
								tempMap.put(CliToolCsvReader.getCell(j,0), CliToolCsvReader.getCell(j,i));
								csvCommands.put(temp1, tempMap);
								};
							int j = CliToolCsvReader.getColumnCount()-1;
							tempMap.put(CliToolCsvReader.getCell(j,0), getHosts(CliToolCsvReader.getCell(j,i)));
						csvCommands.put(temp1, tempMap);
					}
				}
		return csvCommands;
	}

}