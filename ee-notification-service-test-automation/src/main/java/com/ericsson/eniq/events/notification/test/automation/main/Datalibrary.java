package com.ericsson.eniq.events.notification.test.automation.main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.ericsson.eniq.events.notification.test.automation.sql.VelocitityTemplates;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;
import com.ericsson.eniq.events.notification.test.automation.util.GenericScripts;

/**
 * This class get the database results and compare the records of two SQLs and prints it to console.
 * @author ekrchai
 *
 */

public class Datalibrary {
	VelocitityTemplates vt = new VelocitityTemplates();
	static GenericScripts gs;
	static int rowNum = 0;

	public Datalibrary(){
			gs = new GenericScripts();
			gs.scriptName = "AlarmKPI";
			gs.testqpsCreateLogFile();
	}
	
public boolean compareresults(String[] excelData, String kpiName) {
	
	String groupName = null;
	String dateId = null;
	String hourId = null;
	String minId = null;
	float successRatio;
	String definedThreshold = null;
	String isBreached = null;
	ResultSet rawData = null;
	boolean flag = false;
	boolean nullMap = false;
	rowNum=rowNum+1;
	boolean KpiMatchedFlag = false;

	
		try {
			rawData = vt.velocity(excelData, kpiName);
			int ropTime = Integer.parseInt(excelData[6]);
			ResultSet notiData = vt.velocityOne(kpiName, ropTime);
			
			Map<String, Map<String, String>> retriveNotData = retriveNotData(notiData);

			while (rawData != null && rawData.next()) { // process results one
				// row at a time
				if(rawData.getString(10).equalsIgnoreCase("breached") ||  rawData.getString(10).equalsIgnoreCase("not breached"))
				{	
				 groupName = rawData.getString(1);
				 dateId = rawData.getString(2);
				 hourId = rawData.getString(3);
				 minId = rawData.getString(4);
				 successRatio = Float.valueOf(rawData.getString(8));
				 definedThreshold = rawData.getString(9);
				 isBreached = rawData.getString(10);
				}
				else
				{
					groupName = rawData.getString(1);
					dateId = rawData.getString(2);
					hourId = rawData.getString(3);
					minId = rawData.getString(4);
					successRatio = Float.valueOf(rawData.getString(9));
					definedThreshold = rawData.getString(10);
					isBreached = rawData.getString(11);
				}
				

				if (isBreached.equalsIgnoreCase("breached")) {
					flag = true;
					String rawData1 = ("\n RawData  = {Date  = " + dateId + " Defined Threshold  = "+ definedThreshold + " Group Name  = " + groupName + "  Hour  = " + hourId + " Minutes  = " + minId + " Success/DropRatio = "+successRatio);
					System.out.println(""+rawData1);
					AutomationLogger.info(rawData1);
					Map<String, String> map = retriveNotData.get(groupName);
					
					String notifiData = (" NotData  = "+ map);
					System.out.println(" NotData  = "+ map);
					AutomationLogger.info(notifiData);
					if(map == null)
					{
					    nullMap = true;
					}
					if (map!=null&&groupName.equalsIgnoreCase(map.get("GroupName"))
							&& dateId.equalsIgnoreCase(map.get("DataId"))
							&& hourId.equalsIgnoreCase(map.get("HourId"))
							&&  (Integer.parseInt(minId)-Integer.parseInt(map.get("MinId")) <ropTime) 
							&& definedThreshold.contains(map.get("DefinedThreshold"))
							&& hasNoRoundingError(successRatio,
									Float.valueOf(map.get("Success/DropRatio")))) {
						KpiMatchedFlag = true;
					} else {
						KpiMatchedFlag = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);		}
		if (KpiMatchedFlag == true)
		{
			AutomationLogger.info(" \nKpiMatchedFlag -  " + KpiMatchedFlag);
			System.out.println(" \nKpiMatchedFlag -  " + KpiMatchedFlag);
			gs.writePassLog(kpiName,rowNum);
		}
		else if (KpiMatchedFlag == false){
			
			if(flag){
				AutomationLogger.info(" \nKpiMatchedFlag -  " + KpiMatchedFlag);
				System.out.println(" \nKpiMatchedFlag -   " + KpiMatchedFlag);
				if(nullMap)
				{
				    gs.writeMissingEventsLog(kpiName,rowNum);
				}
				else
				{
				    gs.writeFailLog(kpiName,rowNum);
				}
			} else {
				AutomationLogger.info(" \nKpiMatchedFlag -  No Events");
				System.out.println(" \nKpiMatchedFlag -  No Events" );
				gs.writeNoEventsLog(kpiName,rowNum);
			}
		}
		return KpiMatchedFlag;
	}

	private boolean hasNoRoundingError(float successRatio, float successRatio1) {
		return Math.abs(successRatio - successRatio1) < 0.01;
	}
	
	
	private Map<String, Map<String,String>> retriveNotData(ResultSet notiData) throws NumberFormatException, SQLException {
		
		Map<String, Map<String,String>> rawData = new HashMap<String, Map<String,String>>();
		
		Map<String,String> tempRawData = null;
		while (notiData != null && notiData.next()) { //process results one row at a time
					
			tempRawData = new TreeMap<String,String>();
			tempRawData.put("GroupName", notiData.getString(1));
			tempRawData.put("DataId", notiData.getString(2));
			if(TimeZone.getDefault().inDaylightTime( new Date() )){  //this checks the change in time (Daylight savings)
				int hour = Integer.parseInt((notiData.getString(3))) - 1;
				String finalHour = Integer.toString(hour);
				tempRawData.put("HourId", finalHour);
			} else {
				tempRawData.put("HourId", notiData.getString(3));
			}
			tempRawData.put("MinId", notiData.getString(4));
			tempRawData.put("Success/DropRatio", notiData.getString(5));
			tempRawData.put("DefinedThreshold", notiData.getString(6));
			rawData.put(tempRawData.get("GroupName"), tempRawData);
		}	
		return rawData;
	}
	
	 public void htmlLogEnd(){
		 gs.htmlLogEnd();
	 }
	
}
