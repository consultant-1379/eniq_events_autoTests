package com.ericsson.eniq.events.notification.test.automation.util;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ericsson.eniq.events.notification.test.automation.main.AutomationMain;
import com.ericsson.eniq.events.notification.test.automation.main.Dataloader;
import com.ericsson.eniq.events.notification.test.automation.sql.AutomateJDBC;

/**
 * This class prints the target to the HTML file.
 * @author ekrchai
 *
 */

public class GenericScripts  {
	public int rowCount;
    public static String scriptName;
    public static String htmlLogDir = AutomationLogger.defaultLogDir + File.separator + AutomationLogger.NOTIFICATION_SERVICE_DIR + "/";
    public static Date date_id = new Date();
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    public String hostblade = AutomateJDBC.hostName;
    public static String htmlFileName = null;
    
	BufferedWriter b = null;
	StringBuffer sb1 = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	StringBuffer sb3 = new StringBuffer();
    
public void testqpsCreateLogFile(){
    	
        try{
        	htmlFileName = scriptName+"_" + format.format(date_id) + ".htm";
        	File file = new File(htmlLogDir+scriptName+"_" + format.format(date_id) + ".htm");
        	file.delete();
        	file.createNewFile();
        	b = new BufferedWriter(new FileWriter(file,true));
        	sb1.append("<html><head><title>Results for - EE - " +scriptName+ "</title></head>");
        	//sb1.append("<body><table style='margin-top:40px' border='5'><tr><td style='color:navy;font-weight: bold'>Executing </td><td>Eniq-Events-Alarm-KPI-Phase-1-Testcases</td></tr>");
        	//sb1.append("<tr><td style='color:navy;font-weight: bold'>Host </td><td>" + hostblade + "</td></tr>");
        	//sb1.append("<tr><td style='color:navy;font-weight: bold'>Start Time</td><td>" +  date_id + "</td></tr></body>");
        	sb1.append("<body><table border = '1' cellspacing = '2' cellpadding = '2'><tr><td>Host </td><td>" + hostblade + "</td></tr>");
        	sb1.append("<tr><td>Start Time</td><td>" +  date_id + "</td></tr></table></body>");
        	//sb2.append("<body><table style='margin-top:40px' border='5'><tr><td style='color:navy;font-weight: bold'>Log File </td><td><a href='http://users.eei.ericsson.se/~eniqftauto/kpithreshold/results/kpi_automation.log.0'>KPI Automation Log</a></td></tr>");
            //sb2.append("<table style='margin-top:40px' border='5'><tr style='background-color:#C0C0C0;color:navy;font-weight: bold'><td >KPI No. </td><td>KPI Name</td><td>Result</td><td>Date</td></tr>");
            sb2.append("<body><h3>Eniq-Events-Alarm-KPI-Phase-1-Testcases</h3>");
            sb2.append("<TABLE  BORDER = 1 CELLSPACING = 2 CELLPADDING = 2><tr><th>KPI No. </th><th>KPI Name</th><th>Result</th><th>Date</th><th>Failure Reason<th></tr>"); 
        
        
        }
        catch(IOException e){
        	e.printStackTrace();
        	String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
        	
        }
    }
    
    public void writePassLog(String desc, int rowNum){
    	
        try{
        	//sb2.append("<tr><td style='background-color:#C0C0C0;color:navy;font-weight: bold'>"+rowNum+"</td><td>"+desc+"</td><td  style='color:navy;font-weight: bold'>PASS</td><td>"+new Date()+"</td>");
        
        	sb2.append("<tr><td>"+rowNum+"</td><td>"+desc+"</td><td  align='center'><font color='darkblue'><b>PASS</b></font></td><td>"+new Date()+"</td><td>N/A</td>");
   
        
        }
        catch(Exception e){
        	e.printStackTrace();
        	String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
        }
    }
    
    public void writeFailLog(String desc, int rowNum){
    	
        try{
        	System.out.println("------");
        	//sb2.append("<tr><td style='background-color:#C0C0C0;color:navy;font-weight: bold'>"+rowNum+"</td><td>"+desc+"</td><td  style='color:red;font-weight: bold'>FAIL</td><td>"+new Date()+"</td>");
        	sb2.append("<tr><td>"+rowNum+"</td><td>"+desc+"</td><td  align='center'><font color='red'><b>FAIL</b></font></td><td>"+new Date()+"</td><td>N/A</td>");
        
        
        }
        catch(Exception e){
        	e.printStackTrace();
        	String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
        }
    }
   
    public void writeMissingEventsLog(String desc, int rowNum){
        
        try{
                System.out.println("------");
                //sb2.append("<tr><td style='background-color:#C0C0C0;color:navy;font-weight: bold'>"+rowNum+"</td><td>"+desc+"</td><td  style='color:orange;font-weight: bold'>NoEvents</td><td>"+new Date()+"</td>");
                sb2.append("<tr><td>"+rowNum+"</td><td>"+desc+"</td><td  align='center'><font color='red'><b>FAIL</b></font></td><td>"+new Date()+"</td><td>MissingEvents</td>");
        
        
        }
        catch(Exception e){
                e.printStackTrace();
                String exception = Dataloader.printException(e);
                        AutomationLogger.info(exception);
        }
    }
    
    public void writeNoEventsLog(String desc, int rowNum){
    	
        try{
        	System.out.println("------");
        	//sb2.append("<tr><td style='background-color:#C0C0C0;color:navy;font-weight: bold'>"+rowNum+"</td><td>"+desc+"</td><td  style='color:orange;font-weight: bold'>NoEvents</td><td>"+new Date()+"</td>");
        	sb2.append("<tr><td>"+rowNum+"</td><td>"+desc+"</td><td  align='center'><font color='red'><b>FAIL</b></font></td><td>"+new Date()+"</td><td>NoEvents</td>");
        
        
        }
        catch(Exception e){
        	e.printStackTrace();
        	String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
        }
    }
    
    public void htmlLogEnd(){
    	try {
    		
    		b.write(sb1.toString());
			//b.write("<tr><td style='color:navy;font-weight: bold'>End Time</td><td>" + AutomationMain.endTime + "</td></tr></body>");
			//b.write("<tr><td style='color:navy;font-weight: bold'>Automation Status</td><td> Completed </td></tr></body>");
			b.write(sb2.toString());
			b.write("</TABLE></body></html>");
			 b.flush();
			b.close();
			
		} catch (IOException e) {
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
			e.printStackTrace();		
			}
    }
}
  

