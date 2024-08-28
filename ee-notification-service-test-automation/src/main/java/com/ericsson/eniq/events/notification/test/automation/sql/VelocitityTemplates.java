package com.ericsson.eniq.events.notification.test.automation.sql;
import java.io.StringWriter;
import java.sql.ResultSet;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.ericsson.eniq.events.notification.test.automation.main.Dateformatting;
import com.ericsson.eniq.events.notification.test.automation.main.ResourceUtils;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;

/**
 * This class renders a Velocity templates 
 * @author ekrchai
 *
 */
public class VelocitityTemplates {
	Dateformatting df = new Dateformatting();
	public String startTime = null;
	public String endTime3G4G = null;
	public String endTimeMSSrawTable = null;
	public String endTimeMSSnotificationTable = null;
	public String endTime3G4GNotifi = null;;
	public String startTimeRaw = null;
	public String endTime3G4GRaw = null;
	public String queryStatus = AutomateJDBC.queryFlag;
	

	public VelocitityTemplates() {
		df.dateFormatting(this);
		}

	enum QueryType {
		IMSIWITH2JOINS, PLMN, SGSN_MME, MSSWITH2JOINS, IMSIWITH3JOINSSUCCESS, MSSWITH3JOINSSUCCESS, IMSIWITH3JOINSDROP, MSSWITH3JOINSDROP, IMSI_SMSWITH2JOINS
	};

	public ResultSet velocity(String[] data, String kpi) throws Exception {
		String kpiName = kpi;
		String eventId = data[0];
		String subType = data[1];
		String sucTable = data[2];
		String errTable = data[3];
		String dropTable = data[4];
		String query = data[5];
	
		/* first, get and initialize a Velocity engine */
		
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		
		/* create a context and add data */

		VelocityContext context = new VelocityContext();
		Template t = null;
		/* next, get the Template */
		switch (QueryType.valueOf(query)) {

		case IMSIWITH2JOINS:
			t = ve.getTemplate(ResourceUtils.getResourcePath("IMSIGroupwith2Joins.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTime3G4GRaw);
			break;

		case PLMN:
			t = ve.getTemplate(ResourceUtils.getResourcePath("PLMN.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTime3G4GRaw);
			break;

		case SGSN_MME:
			t = ve.getTemplate(ResourceUtils.getResourcePath("SGSNGroupwith2joins.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTime3G4GRaw);
			break;

		case MSSWITH2JOINS:
			t = ve.getTemplate(ResourceUtils.getResourcePath("MSSGroupwith2joins.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		case IMSIWITH3JOINSSUCCESS:
			t = ve.getTemplate(ResourceUtils.getResourcePath("IMSIGroupwith3joinsSuccessRatio.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		case MSSWITH3JOINSSUCCESS:
			t = ve.getTemplate(ResourceUtils.getResourcePath("MSSGroupwith3joinsSuccessRatio.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		case IMSIWITH3JOINSDROP:
			t = ve.getTemplate(ResourceUtils.getResourcePath("IMSIGroupwith3joinsDropRatio.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		case MSSWITH3JOINSDROP:
			t = ve.getTemplate(ResourceUtils.getResourcePath("MSSGroupwith3joinsDropRatio.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		case IMSI_SMSWITH2JOINS:
			t = ve.getTemplate(ResourceUtils.getResourcePath("IMSI_SMSWITH2JOINS.vm"));
			context.put("START_TIME", startTimeRaw);
			context.put("END_TIME", endTimeMSSrawTable);
			break;

		}


		context.put("EVENT_ID", eventId);
		context.put("SUB_TYPE", subType);
		context.put("SUC_TABLE_RAW", sucTable);
		context.put("ERR_TABLE_RAW", errTable);
		context.put("DROP_TABLE_RAW", dropTable);
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		AutomationLogger.info("\nKPI Name: " + kpiName); 
		System.out.println("\n\nKPI:  " + kpiName + "\n");
		if(queryStatus.equals("enable")){
			AutomationLogger.info("SGEH Raw Tabe query: \n" + writer.toString()); 
			System.out.println("SGEH Raw Tabe query: \n" +  writer.toString());
		}
		ResultSet result = AutomateJDBC.reteiveData(writer.toString());
		return result;
	}

	public ResultSet velocityOne(String kpi, int ropTime) throws Exception {

		String kpiName = kpi;
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		Template t = ve.getTemplate(ResourceUtils.getResourcePath("PLMNNotifi.vm"));
		VelocityContext context = new VelocityContext();
		context.put("kpiname", kpiName);
		if (ropTime == 15) {
			context.put("START_TIME", startTime);
			context.put("END_TIME", endTimeMSSnotificationTable);
		} else {
			context.put("START_TIME", startTime);
			context.put("END_TIME", endTime3G4GNotifi);
		}
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		if(queryStatus.equals("enable")){
			AutomationLogger.info("\nDC_Z_ALARM_INFO_RAW Table query: \n" + writer.toString()); 
			System.out.println("\nDC_Z_ALARM_INFO_RAW Table query: \n" +  writer.toString());
		}
		ResultSet result = AutomateJDBC.reteiveData(writer.toString());
		return result;
	}
}
