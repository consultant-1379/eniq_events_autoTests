package com.ericsson.eniq.events.notification.test.automation.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.ericsson.eniq.events.notification.test.automation.main.Dataloader;
import com.ericsson.eniq.events.notification.test.automation.main.ResourceUtils;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;
import com.ericsson.eniq.events.notification.test.automation.util.GenericScripts;
import com.ericsson.eniq.events.notification.test.automation.util.PropertiesFileController;


/**
 * This class deletes all the Raw Table records up to last 2 hour and also the log files.
 * @author ekrchai
 *
 */

public class DeleteTableDataAndHTMLLogs {
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar cal = Calendar.getInstance();

	public static void deleteData(){
	
		final PropertiesFileController propFile = new PropertiesFileController();

	/*String[] rawTables = { "EVENT_E_LTE_SUC_RAW_01", "EVENT_E_LTE_SUC_RAW_02","EVENT_E_LTE_SUC_RAW_03", "EVENT_E_LTE_SUC_RAW_04", 
			"EVENT_E_LTE_SUC_RAW_05", "EVENT_E_LTE_SUC_RAW_06", "EVENT_E_LTE_SUC_RAW_07", "EVENT_E_LTE_SUC_RAW_08", "EVENT_E_LTE_SUC_RAW_09", 
			"EVENT_E_LTE_SUC_RAW_10", "EVENT_E_LTE_SUC_RAW_11", "EVENT_E_LTE_SUC_RAW_12", "EVENT_E_LTE_SUC_RAW_13", "EVENT_E_LTE_SUC_RAW_14", "EVENT_E_LTE_SUC_RAW_15", 
			"EVENT_E_LTE_ERR_RAW_01","EVENT_E_LTE_ERR_RAW_02", "EVENT_E_LTE_ERR_RAW_03",
			"EVENT_E_SGEH_SUC_RAW_01", "EVENT_E_SGEH_SUC_RAW_02","EVENT_E_SGEH_SUC_RAW_03", "EVENT_E_SGEH_SUC_RAW_04",
			"EVENT_E_SGEH_SUC_RAW_05", "EVENT_E_SGEH_SUC_RAW_06","EVENT_E_SGEH_SUC_RAW_07", "EVENT_E_SGEH_SUC_RAW_08",
			"EVENT_E_SGEH_SUC_RAW_09", "EVENT_E_SGEH_SUC_RAW_10","EVENT_E_SGEH_SUC_RAW_11", "EVENT_E_SGEH_SUC_RAW_12",
			"EVENT_E_SGEH_SUC_RAW_13", "EVENT_E_SGEH_SUC_RAW_14","EVENT_E_SGEH_SUC_RAW_15", "EVENT_E_SGEH_ERR_RAW_01",
			"EVENT_E_SGEH_ERR_RAW_02", "EVENT_E_SGEH_ERR_RAW_03","EVENT_E_MSS_LOC_SERVICE_CDR_ERR_RAW_01","EVENT_E_MSS_LOC_SERVICE_CDR_ERR_RAW_02",
			"EVENT_E_MSS_LOC_SERVICE_CDR_ERR_RAW_03","EVENT_E_MSS_LOC_SERVICE_CDR_SUC_RAW_01","EVENT_E_MSS_LOC_SERVICE_CDR_SUC_RAW_02",
			"EVENT_E_MSS_LOC_SERVICE_CDR_SUC_RAW_03","EVENT_E_MSS_VOICE_CDR_ERR_RAW_01","EVENT_E_MSS_VOICE_CDR_ERR_RAW_02",
			"EVENT_E_MSS_VOICE_CDR_ERR_RAW_03","EVENT_E_MSS_VOICE_CDR_SUC_RAW_01","EVENT_E_MSS_VOICE_CDR_SUC_RAW_02",
			"EVENT_E_MSS_VOICE_CDR_SUC_RAW_03","EVENT_E_MSS_VOICE_CDR_SUC_RAW_04","EVENT_E_MSS_VOICE_CDR_SUC_RAW_05",
			"EVENT_E_MSS_VOICE_CDR_SUC_RAW_06","EVENT_E_MSS_VOICE_CDR_SUC_RAW_07","EVENT_E_MSS_VOICE_CDR_SUC_RAW_08",
			"EVENT_E_MSS_VOICE_CDR_SUC_RAW_09","EVENT_E_MSS_VOICE_CDR_SUC_RAW_10","EVENT_E_MSS_VOICE_CDR_SUC_RAW_11",	
			"EVENT_E_MSS_VOICE_CDR_DROP_CALL_RAW_01","EVENT_E_MSS_VOICE_CDR_DROP_CALL_RAW_02","EVENT_E_MSS_VOICE_CDR_DROP_CALL_RAW_03",
			"EVENT_E_MSS_SMS_CDR_ERR_RAW_01","EVENT_E_MSS_SMS_CDR_ERR_RAW_02","EVENT_E_MSS_SMS_CDR_ERR_RAW_03",
			"EVENT_E_MSS_SMS_CDR_SUC_RAW_01","EVENT_E_MSS_SMS_CDR_SUC_RAW_02","EVENT_E_MSS_SMS_CDR_SUC_RAW_03",
			"EVENT_E_MSS_SMS_CDR_SUC_RAW_04","EVENT_E_MSS_SMS_CDR_SUC_RAW_05","EVENT_E_MSS_SMS_CDR_SUC_RAW_06", 
			"DC_Z_ALARM_INFO_RAW_01", "DC_Z_ALARM_INFO_RAW_02", "DC_Z_ALARM_INFO_RAW_03"};*/
		
		try {
			propFile.loadPropertiesFile(ResourceUtils
					.getResource("rawtables.properties"));

			final String tables = propFile
					.getPropertiesSingleStringValue("Raw_Tables");
			String[] rawTables = null;
			if (tables != null) {
				rawTables = tables.split(",");
			}
			AutomationLogger
					.info("\nDeleting records from Raw tables...............\n");
			for (String tableName : rawTables) {

				cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, -2);
				String hourBackTime = dateFormat.format(cal.getTime());
				String query = "delete from " + tableName
						+ " where datetime_id < '" + hourBackTime + "'";
				AutomateJDBC.deleteData(query, tableName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		} catch (IOException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		}

	}
	public static void deleteHtmlAndLogFiles(final String outputFileDir){

		try {
			final File parentDirectory = new File(outputFileDir);
			AutomationLogger.info("Deleting the existing files from  " + GenericScripts.htmlLogDir + "  directory..........");
			final String[] fileList = parentDirectory.list();
			boolean flag = false;
			for (String file : fileList) {
				
				File fileForUpload = new File(outputFileDir + file);
				if(file.contains("AlarmKPI")){
				fileForUpload.delete();
				System.out.println("Deleted file: "+file);
				AutomationLogger.info("\nfile : "+file+" successfully deleted");
				flag = true;
				}
			}
			if(flag == false){
					System.out.println("There are no files to delete");
					AutomationLogger.info("There are no files to delete in this directory");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		}

	}
}
	