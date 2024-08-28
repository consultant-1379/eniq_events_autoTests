package com.ericsson.eniq.events.notification.test.automation.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.ericsson.eniq.events.notification.test.automation.sql.AutomateJDBC;
import com.ericsson.eniq.events.notification.test.automation.sql.DeleteTableDataAndHTMLLogs;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;
import com.ericsson.eniq.events.notification.test.automation.util.FTPUtil;
import com.ericsson.eniq.events.notification.test.automation.util.GenericScripts;

/**
 * This Class starts the automation by collecting the KPI Name from
 * Alarm_KPI-Automation_Framework_ExecutionStatus.csv file and check for the
 * same KPI in "KPI-Automation_Framework.csv" to start reading the data with
 * Dataloader class for that KPI in that file.
 * 
 * @author ekrchai
 * 
 */

public class AutomationMain {
	static Dataloader loader;
	static FTPUtil ftpUtil = new FTPUtil();
	static int rowNum = 0;
    public static Date endTime;

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		try {
			startAutomatiojn();
			BufferedReader r = new BufferedReader(
					new InputStreamReader(
							ResourceUtils
									.getResource("KPI_Automation_Framework_ExecutionStatus.csv")));

			String line;
			while ((line = r.readLine()) != null) {

				String tokens[] = line.split("@");
				String kpi = tokens[0];
				String context = tokens[1];

				if (context.equalsIgnoreCase("Yes")) {
					String scrptName = kpi;
					datalibrary(scrptName);
				}
			}
			r.close();
		} catch (Exception e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();
		}
		endAutomation();
	}

	public static String datalibrary(String scrptName) {

		String kpiName = null;

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					ResourceUtils.getResource("KPI_Automation_Framework.csv")));
			String line;
			while ((line = r.readLine()) != null) {

				String tokens[] = line.split("@");

				String plmndata = tokens[0];

				if (scrptName.equalsIgnoreCase(plmndata)) {
					rowNum = rowNum + 1;
					loader.readdata(plmndata, rowNum);

				}

			}
		} catch (FileNotFoundException e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();
		} catch (IOException e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();
		}

		return kpiName;
	}

	public static String getHostName(String host) {
		String hostName = host;
		return hostName;
	}
	
	public static void  startAutomatiojn() {
		AutomationLogger
		.info("\n--------------------------------------------------------------------------------------------");
		AutomationLogger
				.info(new Date()
						+ ":  Launching KPI Threshold Notification Services Automation Info:");
		AutomationLogger
				.info("--------------------------------------------------------------------------------------------");
		AutomationLogger
		.info("Running Automation on host: " + AutomateJDBC.hostName);
		if(GenericScripts.htmlLogDir != null){
			DeleteTableDataAndHTMLLogs
					.deleteHtmlAndLogFiles(GenericScripts.htmlLogDir);
		}
		DeleteTableDataAndHTMLLogs.deleteData();
		loader = new Dataloader();
		AutomationLogger
				.info("\nExecuting Testcases..................................");
		
	}
	
	public static  void endAutomation() {
		endTime = new Date();
		loader.htmlLogEnd();
		try {
			AutomationLogger
			.info("\nTestcase execution completed Successfully..........");
			new  FTPUtil().copyHtmlAndLogFilesToUnixBox(GenericScripts.htmlLogDir);
			AutomationLogger
			.info("\nCheck HTML result page at:  http://users.eei.ericsson.se/~eniqftauto/kpithreshold/results/" + GenericScripts.htmlFileName);
			AutomationLogger
					.info("\n----------KPI Threshold Notification Service Test Automation Execution Successfully Completed at " + new Date() + "---------- ");
		}catch (FileNotFoundException e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();
		} catch (IOException e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();
		}
	}

}
