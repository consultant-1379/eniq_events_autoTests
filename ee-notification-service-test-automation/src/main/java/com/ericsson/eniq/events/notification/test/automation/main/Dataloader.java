package com.ericsson.eniq.events.notification.test.automation.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;

import jxl.Sheet;
import jxl.Workbook;

/**
 * This class parse the txt file and reads data into an array eventId invokes
 * Datalibrary class.
 * 
 * @author ekrchai
 * 
 */

public class Dataloader {

	Datalibrary datalibrary = new Datalibrary();

	public void readdata(String kpi, int i) {
		String kpiName = kpi;
		String[] csvdata = null;

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					ResourceUtils.getResource("KPI_Automation_Framework.csv")));
			String line;
			while ((line = r.readLine()) != null) {

				String tokens[] = line.split("@");
				int tokenLength = tokens.length;
				csvdata = new String[tokenLength - 1];
				if (tokens[0].equalsIgnoreCase(kpiName)) {
					for (int j = 0; j < tokenLength - 1; j++) {
						csvdata[j] = tokens[j + 1];
					}

					datalibrary.compareresults(csvdata, kpiName);
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
	}

	public static String printException(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		pw.close();
		return stack;

	}
	
	public void htmlLogEnd(){
		datalibrary.htmlLogEnd();
	 }
}
