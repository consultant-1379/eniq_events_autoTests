package com.ericsson.eniq.events.notification.test.automation.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.ericsson.eniq.events.notification.test.automation.sql.VelocitityTemplates;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;

/**
 * This class formats the currentdate to the required date and into required format
 * to query for a specific period.
 * @author ekrchai
 *
 */

public class Dateformatting {
	
	
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar cal = Calendar.getInstance();
	
	public void dateFormatting(VelocitityTemplates vt){
		int addMinutes = 0;
		try {
			int timeInSeconds = ((int) (System.currentTimeMillis()/1000))%60;
			int minutes = ((int) (System.currentTimeMillis()/(1000*60))%15);

		if(TimeZone.getDefault().inDaylightTime( new Date() )){
			 addMinutes = 60;
		} else {
			addMinutes = 0;
		}
	
			vt.startTime = dateManipulation(-(30+minutes),-timeInSeconds);
			vt.endTime3G4GNotifi = dateManipulation(-(30+minutes),(59-timeInSeconds));
			vt.startTimeRaw = dateManipulation(-(30 + addMinutes + minutes),-timeInSeconds);
			vt.endTime3G4GRaw = dateManipulation(-(30 + addMinutes + minutes),(59-timeInSeconds));
			vt.endTimeMSSrawTable = dateManipulation(-(27 + addMinutes + minutes), (59-timeInSeconds));
			vt.endTimeMSSnotificationTable = dateManipulation(-(16+minutes), (59-timeInSeconds));
		} catch (Exception e) {
			e.printStackTrace();
			String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);		}
		 
	}
		
		public static String dateManipulation(int minutes, int seconds)
		{
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutes);
		cal.add(Calendar.SECOND, seconds);
		String changedTime = dateFormat.format(cal.getTime());
		
		return changedTime;

        }
			
}
