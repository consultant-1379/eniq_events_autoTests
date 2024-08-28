
package com.ericsson.star.regressiontest.operators.cli; 

import com.ericsson.cifwk.taf.CliOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.v2.Session;
import com.ericsson.star.regressiontest.getters.cli.CliToolCliGetter;
import com.ericsson.star.regressiontest.operators.cli.datagen.DataGenCliOperator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
 *
 *	CLI Context Operator for executing Test Cases for CliTool
 */
public class CliToolCliOperator implements CliOperator{

	private Session session;
	private static String CMD = "command";
	private static String ARGS = "args";
	private static String TIMEOUT = "timeout";
	private static String HOST = "host";
	private static Logger log = Logger.getLogger(CliToolCliOperator.class);
	private DataGenCliOperator dataGenOperator = new DataGenCliOperator();
	private String timeOut = "300000";
	
	
	/**
	* Sends the command for  svcs1
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean runCommand (Map<String, Object> args){ 
		boolean result;
		session = Session.getSession((Host) args.get(HOST));
		result = session.send((String) args.get(CMD), (String) args.get(ARGS));
		session.waitForFinish(1200000L);
		return result;
	}
	
	public boolean enableHttps (Map<String, Object> args){ 
		boolean result = true;
		session = Session.getSession((Host) args.get(HOST));
		result = session.send("/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh", "status");
		session.waitForFinish();
		if(!session.getStdOut().contains("enable")){
			result = session.send("/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh",
					"enable");
			session.waitForFinish(1200000L);
		}
		return result;
	}
	
	public boolean disableHttps (Map<String, Object> args){ 
		boolean result = true;
		session = Session.getSession((Host) args.get(HOST));
		result = session.send("/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh", "status");
		session.waitForFinish();
		if(!session.getStdOut().contains("disable")){
			result = session.send("/eniq/glassfish/glassfish3/glassfish/bin/change_glassfish_security.sh",
					"disable");
			session.waitForFinish(Long.valueOf((String) args.get(TIMEOUT)));
		}
		return result;
	}
	
	public boolean runWget(Map<String, Object> args){
		boolean result = false;
		session = Session.getSession((Host) args.get(HOST));
		result = session.send("/usr/sfw/bin/wget --quiet --no-check-certificate -O " +
				"/dev/null --keep-session-cookies --save-cookies",
				"/eniq/home/dcuser/check_https_cookies.txt \"http://localhost:18080/EniqEventsUI/\"");
		session.waitForFinish(1200000L);
		return result;
	}
	
	public boolean checkWgetResult(Map<String, Object> args){
		boolean result = false;
		session = Session.getSession((Host) args.get(HOST));
		session.send("cat", "/eniq/home/dcuser/check_https_cookies.txt");
		session.waitForFinish(1200000L);
		String stdOut = session.getStdOut();
		if((((String)args.get(ARGS)).contains("enable") && stdOut.contains("8181"))||
				(((String)args.get(ARGS)).contains("disable") && stdOut.contains("18080"))){
			result = true;
		}
		
		return result;
	}

	/**
	* Sends the command for  svcs1
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean loadTopology(Map<String, Object> args){ 
		boolean result = false;
		return result;
	}
	
	public boolean checkTopologyTables(Map<String, Object> args){
		String[] table_name = ((String)args.get("table_name")).split(" ");
		String[] column_name = ((String)args.get("column_name")).split(" ");
		String[] column_value = ((String)args.get("column_value")).split(" ");
		String[] cmp_operator = ((String)args.get("cmp_operator")).split(" ");
		int threshold = Integer.parseInt(args.get("threshold").toString());
		
		for(int i = 0; i < table_name.length; i++){
			String query;
			if(!isLong(column_value[i])){
				query = "SELECT COUNT(*) FROM " + table_name[i] + " WHERE " + column_name[i] +
						" " + cmp_operator[i] + " \\'" + column_value[i] + "\\'";
			}else{
				query = "SELECT COUNT(*) FROM " + table_name[i] + " WHERE " + column_name[i] +
						" " + cmp_operator[i] + " " + column_value[i];
			}
			String[] res=sqlSelect((Host) args.get(HOST),query);
			if(Integer.parseInt(res[0].trim()) < threshold){
				return false;
			}
		}
		
		return true;
	}
	
	public int checkInterface(Map<String, Object> args){
		String oss_name = (String) args.get("oss_name");
		String interface_name = (String) args.get("interface_name");
		String command = "cd /eniq/sw/installer; ./activate_interface -o "
				+ oss_name + " -i " + interface_name;
		String output;
		session = Session.getNewSession((Host)args.get(HOST));
		session.send(command, "");
		session.waitForFinish(1200000L);
		output = session.getStdOut();
		if(output.contains("is already activated")){
			return 1;
		}else{
			session.send(command, "");
			session.waitForFinish(1200000L);
			output = session.getStdOut();
			if(output.contains("is already activated")){
				return 2;
			}
		}
		return 0;
	}
	
	public boolean moveTopologyToFolder(Map<String, Object> args){
		String topology_action = (String) args.get("topology_action");
		String topology_file = (String) args.get("topology_file");
		String topology_dest_folder = (String) args.get("topology_dest_folder");
		String command = null;
		if(topology_action.contains("unzip")){
			command = "unzip -o " + topology_file + " -d " + topology_dest_folder;
		}else{
			command = "cp " + topology_file + " " + topology_dest_folder;
		}
		session = Session.getNewSession((Host) args.get(HOST));
		session.send(command, "");
		session.waitForFinish(1200000L);
		
		if(session.getExitCode()>0)
			return false;
		return true;
	}
	
	public boolean activateEngineSet(Map<String, Object> args){
		String interface_name = (String) args.get("interface_name");
		String oss_name = (String) args.get("oss_name");
		String adapter_name = (String) args.get("adapter_name");
		String command = "/eniq/sw/bin/engine -e startSet "
				+ interface_name + "-" + oss_name + " " + adapter_name;
		
		session = Session.getNewSession((Host) args.get(HOST));
		session.send(command, "");
		session.waitForFinish(1200000L);
		if(session.getExitCode()>0)
			return false;
		return true;
	}
	
	public boolean loadHomeNetworkGroup(Map<String, Object> args){
		String command = "gpmgt -i -add -f /eniq/home/dcuser/automation/topology" +
				"/3GSessionBrowserTopology/home_network.xml";
		String query = "select * from dc.GROUP_TYPE_E_MCC_MNC " +
				"where mcc = '454' and mnc = '06'";
		session = Session.getSession((Host) args.get(HOST));
		session.send(command, "");
		session.waitForFinish(1200000L);
		if(session.getExitCode()==0){
			if(sqlSelect((Host) args.get(HOST),query).length > 0)
				return true;
		}
		return false;
	}
	
	/**
	* Sends the command for  https_status
	* @param Map<String, Object> containing arguments for command
	* @return 
	*/
	public boolean https_statusCommand (Map<String, Object> args){ 
		boolean result;
		session = Session.getSession((Host) args.get(HOST));
		result = session.send(CliToolCliGetter.getCommand("HTTPS_STATUS"), (String) args.get(ARGS));
		session.waitForFinish(1200000L);
		return result;
	}
	
	public boolean prepareServerForLTEES(){
		boolean result = false;
		Session czSession = Session.getSession(DataHandler.getHostByName("controlzone"));
		Session mzSession = Session.getSession(DataHandler.getHostByName("ec_1"));
		mzSession.send("source ~/.profile; ec stop", "");
		session.waitForFinish(1200000L);
		mzSession.send("source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEEFA_1 EC_LTEEFA_2 EC_LTEEFA_3 EC_SGEH_1", "");
		session.waitForFinish(1200000L);
		mzSession.send("source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr restart EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4 EC1", "");
		session.waitForFinish(1200000L);
		czSession.send("rm -r /eniq/northbound/test_automation_ltees /eniq/northbound/lte_event_stat_file", "");
		session.waitForFinish(1200000L);
		mzSession.send("rm -r /eniq/data/eventdata/events_oss_*/lteRbsCellTrace/* /eniq/data/eventdata/events_oss_*/lteTopologyData/*", "");
		session.waitForFinish(1200000L);
		czSession.send("mkdir -p /eniq/northbound/lte_event_stat_file", "");
		session.waitForFinish(1200000L);
		czSession.send("cp /eniq/home/dcuser/automation/ltees/app.properties /eniq/home/dcuser/automation/test_automation_ltees", "");
		session.waitForFinish(1200000L);
		czSession.send("mkdir -p /eniq/northbound/test_automation_ltees", "");
		session.waitForFinish(1200000L);
		czSession.send("cp -r /eniq/home/dcuser/automation/test_automation_ltees/* /eniq/northbound/test_automation_ltees", "");
		session.waitForFinish(1200000L);
		czSession.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable Sim*", "");
		session.waitForFinish(1200000L);
		czSession.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable EBSL*", "");
		session.waitForFinish(1200000L);
//		czSession.send("/eniq/sw/runtime/jdk1.6.0_33/bin/java -jar","/eniq/northbound/test_automation_ltees/lteesAutomation.jar 13A PMS");
//		czSession.waitForFinish(1000000000L);

		return result;
	}
				
	/**
	* Returns the ExitCode for the last command in the session
	* @return 
	*/
	public int getExitCode(){
		return session.getExitCode();
	}
	
	/**
	* Returns the StdErr content for the last command in the session
	* @return 
	*/	
	public String getStdErr(){
		return session.getStdErr();
	}
	
	/**
	* Returns the StdOut content for the last command in the session
	* @return 
	*/	
	public String getStdOut(){
		return session.getStdOut();
	}
			
	/**
	* Disconnect from the session
	* @return 
	*/
	public boolean disconnect(){
		return session.close();
	}

	public void registerHostOnDatagen(Host host) {
		String datagenServer = DataHandler.getHostByName("datagen").getIp().split("\\.")[0]; 
		String hostName = host.getIp().split("\\.")[0];
		String pathToDatagen = "/net/" + datagenServer + "/tmp/CentralDatagen/" + hostName;
		
		session = Session.getNewSession(host);
		session.send("ls", pathToDatagen);
		session.waitForFinish(1200000L);
		if(session.getExitCode() > 0){
			session.send("mkdir -p", pathToDatagen);
			session.waitForFinish(1200000L);
		}
	}

	public boolean checkDataLoading(String mediation_hostname, String[] dirsToCheck,
			String[] tablesToCheck, int timeWrap, boolean doWait, String[] column, String[] columnValue) {
		long dataLoadingStartTime = System.currentTimeMillis()/1000L-40*60;
		String[] tableDates = new String[tablesToCheck.length];
		Host host = DataHandler.getHostByName("controlzone");
		Boolean success = false;
		
		if(doWait){
			log.info("Checking latest date in event tables");
			for(int i = 0; i < tablesToCheck.length; i++){
				String query = "SELECT max(datetime_id) FROM " + tablesToCheck[i];
				String[] result = sqlSelect(host,query);
				if(!result[0].contains(":")){
					log.info("Current latest date in table " + tablesToCheck[i] + " is null. After waiting, " +
							"any valid date from the last two days will be accepted");
					tableDates[i] = "2880";
				}else{
					log.info("Current latest date in table " + tablesToCheck[i] +": " + result[0]);
					tableDates[i] = result[0];
				}
			}
				
			log.info("Sleeping for up to " + timeWrap + " mins to allow raw tables " + StringUtils.join(tablesToCheck,' ') + " to populate");
			for(int i = 1; i <= timeWrap; i++){
				pause(60);
				log.info(i + " minutes of a maximum " + timeWrap + " have passed, checking directories and tables.");
				success = true;
				
				for(String dir : dirsToCheck){
					if(!verifyDataDir(mediation_hostname,dir,dataLoadingStartTime)){
						log.info("Directories are not yet populating. Continuing to wait.");
						success = false;
						break;
					}
				}
				
				if(success){
					String colVal;
					for(int j = 0; j < tablesToCheck.length; j++){
						try{
							colVal = columnValue[j];
						}catch(Exception e){
							colVal = null;
						}
						
						if(!verfifyDataTable(tablesToCheck[j], column[j], colVal, tableDates[j])){
							log.info("Tables are not yet populating. Continuing to wait.");
							success = false;
							break;
						}
					}
				}
				
				if(success){
					log.info("Tables and directories are populating ok. Proceeding to log info.");
					break;
				}
			}
		}else{
			for(int i = 0; i < tablesToCheck.length; i++)
				tableDates[i] = ""+timeWrap;
		}
		
		// log results for directories
		for(String dir : dirsToCheck){
			if(!verifyDataDir(mediation_hostname, dir, dataLoadingStartTime)){
				log.info(dir + " not loading.");
				success = false;
			}else{
				log.info(dir + " is loading OK.");
			}
		}
		
		// log results for tables iff directories are loading
		if(success){
			String colVal;
			for(int j = 0; j < tablesToCheck.length; j++){
				try{
					colVal = columnValue[j];
				}catch(Exception e){
					colVal = null;
				}
				if(!verfifyDataTable(tablesToCheck[j], column[j], colVal, tableDates[j])){
					log.info(tablesToCheck[j] + " is not loading.");
					success = false;
				}else{
					log.info(tablesToCheck[j] + " is loading OK.");
				}
			}
		}
		
		if(!doWait){
			success = true;
		}
		
		return success;
	}

	public boolean disableMatchingWorkflows(String pattern) {
		Host host = DataHandler.getHostByName("controlzone");
		String[] allWorkflows;
		StringBuilder unwantedWorkflows = new StringBuilder();
		boolean success = false;
		
		log.info("Disabling workflows related to " + pattern + " in order to reduce load");
		session = Session.getNewSession(host);
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist", "");
		session.waitForFinish(1200000L);
		allWorkflows = session.getStdOut().split("\n");
		for(String workflow : allWorkflows){
			if(workflow.trim().contains(pattern)){
				unwantedWorkflows.append(workflow.trim().substring(0, workflow.trim().indexOf('(')));
				unwantedWorkflows.append(" ");
			}
		}
		
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable",
				unwantedWorkflows.toString().trim());
		session.waitForFinish(1200000L);
		success = session.getExitCode() == 0;
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop",
				unwantedWorkflows.toString().trim());
		session.waitForFinish(1200000L);
		return success;
	}
	
	/**
	 * Creates links to remote directories on the supplied 
	 * local end points
	 * @param mediator_host data processing host as in host.properties file
	 * @param localDirectories path to local link
	 * @param remoteDirectories path to remote directories on datagen server
	 * @return true
	 */
	public boolean createDatagenLinks(String mediator_host, String[] localDirectories,
			String[] remoteDirectories) {
		Host host = DataHandler.getHostByName(mediator_host);
		boolean success = true;
		session = Session.getSession(host);
		
		for(int i = 0; i < localDirectories.length; i++){
			session.send("ls -ld", localDirectories[i]);
			session.waitForFinish(1200000L);
			if((!session.getStdOut().startsWith("l")) || session.getExitCode() != 0){
				session.send("mkdir -p", localDirectories[i].substring(0,localDirectories[i].lastIndexOf('/')));
				session.waitForFinish(1200000L);
				session.send("rm -rf", localDirectories[i]);
				session.waitForFinish(1200000L);
				session.send("ln -s", remoteDirectories[i] + " " + localDirectories[i]);
				session.waitForFinish(1200000L);
				success = session.getExitCode() == 0;
				
				if(success){
					log.info("Symbolic link to " + remoteDirectories[i] + " created.");
				}else{
					log.error("Failed to create symbolic link to " + remoteDirectories[i]);
				}
			}else{
				log.info("Links to datagen directories already exists.");
			}
		}
		return success;
	}

	public boolean provisionSgehWorkflows() {
		Host host = DataHandler.getHostByName("ec_1");
		boolean success = true;
		session = Session.getSession(host);
		
		session.send("source ~/.profile;cd /eniq/mediation_inter/M_E_SGEH/bin;./provision_workflows.sh", "");
		session.waitForFinish(120000000L);
		String result = session.getStdOut();
		if(result.contains("ERROR")||result.contains("Can NOT")||result.contains("failed")
				||result.contains("No OSS information found")){
			success = false;
			log.info("Set up 2G3G4G workflows has been done by invoking provision_workflows.sh");
		}else{
			log.error("Failed to provision 2G3G4G workflows even by invoking provision_workflows.sh");
		}
		return session.getExitCode() == 0 && success;
	}
	
	public boolean provisionMssWorkflows() {
		Host host = DataHandler.getHostByName("controlzone");
		boolean success = true;
		session = Session.getSession(host);
		
		session.send("/eniq/mediation_inter/M_E_MSS/bin/mss_populate.sh", "");
		session.waitForFinish(120000000L);
		String result = session.getStdOut();
		if(result.contains("ERROR")||result.contains("Can NOT")||result.contains("failed")
				||result.contains("No OSS information found")){
			success = false;
			log.info("Set up 2G3G4G workflows has been done by invoking provision_workflows.sh");
		}else{
			log.error("Failed to provision 2G3G4G workflows even by invoking provision_workflows.sh");
		}
		return session.getExitCode() == 0 && success;
	}

	public boolean disableUnRequiredECs(String[] unRequiredEC) {
		Host host = DataHandler.getHostByName("ec_1");
		
		session = Session.getSession(host);
		session.send("source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown", 
				StringUtils.join(unRequiredEC,' '));
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}

	public boolean enableRequiredECs(String[] requiredEC) {
		Host host = DataHandler.getHostByName("ec_1");
		
		session = Session.getSession(host);
		session.send("source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup", 
				StringUtils.join(requiredEC,' '));
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}

	public boolean refreshSgehTopology() {
		Host host = DataHandler.getHostByName("controlzone");
		boolean success = false;
		
		log.info("Running /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh");
		session = Session.getNewSession(host);
		session.send("cd /eniq/mediation_inter/M_E_SGEH/bin;./mg_topology_refresh.sh", "");
		session.waitForFinish(120000L);
		success = session.getExitCode() == 0;
		
		if(success){
			log.info("SGEH topology refresh script run correctly.");
		}else{
			log.error("SGEH topology refresh script failed.");
		}
		return success;
	}
	
	public boolean refreshMssTopology() {
		Host host = DataHandler.getHostByName("controlzone");
		boolean success = false;
		
		log.info("Running /eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh");
		session = Session.getNewSession(host);
		session.send("/eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh", "");
		session.waitForFinish(120000L);
		success = session.getExitCode() == 0;
		
		if(success){
			log.info("SGEH topology refresh script run correctly.");
		}else{
			log.error("SGEH topology refresh script failed.");
		}
		return success;
	}

	public boolean enable2g3gWorkflows() {
		StringBuilder workflowGroups = new StringBuilder();
		StringBuilder workflows = new StringBuilder();
		StringBuilder unwantedWorkflows = new StringBuilder();
		
		workflowGroups.append("SGEH.WG00_LogParsing_Inter ");
		workflowGroups.append("SGEH.WFG_SGEH_Processing_NFS_OSSRC1_* ");
		workflowGroups.append("SGEH.WFG_Cell_Lookup_Refresh_DB");
		
		workflows.append("SGEH.WF00_ParsingLog_Inter.logging ");
		workflows.append("SGEH.WF_Cell_Lookup_Refresh_DB.now ");
		workflows.append("SGEH.WF_Cell_Lookup_Refresh_DB.scheduled ");
		workflows.append("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir*");
		
		unwantedWorkflows.append("SGEH.WF*.1[0-5] ");
		unwantedWorkflows.append("SGEH.WF_SGEH_Processing_Node.SGSN[2345] ");
		unwantedWorkflows.append("SGEH.WF_SGEH_Processing_Node.SGSN_* ");
		unwantedWorkflows.append("SGEH.WF_SGEH_Processing_Node.MME*");
		disableWorkflow(unwantedWorkflows.toString());
		
		return enableWorkflowGroup(workflowGroups.toString()) && 
				enableWorkflow(workflows.toString());
	}
	
	public boolean enableKpiNotificationWorkflows() {
		StringBuilder workflowGroups = new StringBuilder();
		StringBuilder workflows = new StringBuilder();
		
		workflowGroups.append("SGEH.WFG_SGEH_Processing_NFS_OSSRC1_3* ");
		workflowGroups.append("SGEH.WFG_SGEH_Processing_NFS_OSSRC1_4*");
		
		workflows.append("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir3_* ");
		workflows.append("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir4_*");
		
		return enableWorkflowGroup(workflowGroups.toString()) && 
				enableWorkflow(workflows.toString());
	}

	public boolean enableWorkflowGroup(String workflowGroupName){
		session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable", workflowGroupName);
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}
	
	public boolean enableWorkflow(String workflowName){
		session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable", workflowName);
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}
	
	public boolean disableWorkflow(String workflowName){
		session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable", workflowName);
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}
	
	public boolean create4GAndMSSGroup(boolean force){
		//boolean result;
		session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		session.send("ls /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone", "");
		session.waitForFinish(1200000L);
		if(session.getExitCode()!=0 || force){
			session.send("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G.zip -d /eniq/home/dcuser/automation/DataGenTopology", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Unzip DataGenTopology4G.zip");
			}else{
				log.error("Failed to Unzip DataGenTopology4G.zip");
			}
			
			session.send("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS.zip -d /eniq/home/dcuser/automation/DataGenTopology", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Unzip DataGenTopologyMSS.zip");
			}else{
				log.error("Failed to Unzip DataGenTopologyMSS.zip");
			}
			
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/predropDG.sql", "");	
			session.waitForFinish(1200000L);
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/createDG2.sql", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Create 2G 3G SGEH & CDR Data Generation Tables");
			}else{
				log.error("Failed to Create 2G 3G SGEH & CDR Data Generation Tables");
			}
			
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDG.sql", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Load 2g3g4g Data Generation Topology");
			}else{
				log.error("Failed to Load 2g3g4g Data Generation Topology");
			}
		   
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/updateDG.sql", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Update 2g3g4g DataGen Tables");
			}else{
				log.error("Failed to Update 2g3g4g DataGen Tables");
			}
		   
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/deleteDGGroup.sql", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Delete 2g3g4g Data Generation Groups");
			}else{
				log.error("Failed to Delete 2g3g4g Data Generation Groups");
			}
		   
			session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/deleteDGGroup.sql", "");	
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Delete MSS Data Generation Groups");
			}else{
				log.error("Failed to Delete MSS Data Generation Groups");
			}
		   
			session.send("chmod 755 ", "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh");
			session.waitForFinish(1200000L);
			session.send("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh","");
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Load 2g3g4g DataGen Groups");
			}else{
				log.error("Failed to Load 2g3g4g DataGen Groups");
			}
		
			session.send("chmod 755 ", "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh");
			session.waitForFinish(1200000L);
			session.send("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh","");
			session.waitForFinish(1200000L);
			if(session.getExitCode()==0){
				log.info("Load MSS DataGen Groups");
			}else{
				log.error("Load MSS DataGen Groups");
			}
		   
			session.send("echo $\'done\' >/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone", "");
			session.waitForFinish(1200000L);
		}else if(!force){
			log.info("Skipping creating 4G and MSS groups because it's been done.");
		}
		return session.getExitCode() == 0;
	}
	
	public boolean updateMssPreprocessingWorkflows(String[] mssDir){
		session = Session.getSession(DataHandler.getHostByName("controlzone"));
		session.send("rm", "/tmp/mssworkflows.csv");
		session.waitForFinish(3000L);
		session.send("rm", "/tmp/msseditedworkflows.csv");
		session.waitForFinish(3000L);
		
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport MSS.WF01_PreProcessing /tmp/mssworkflows.csv", "");
		session.waitForFinish(10000L);
		String status = session.getStdOut();
		session.send("ls /tmp/ | grep mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		int executionResult = session.getExitCode();
		
		if(status.contains("Export finished") && executionResult == 0){
			log.info("MSS.WF01_PreProcessing workflow exported successfully");
		}else{
			log.error("MSS.WF01_PreProcessing workflow did not export");
		}
		
		session.send("cat", "/tmp/mssworkflows.csv");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		String[] workflows = session.getStdOut().split("\n");
		int updatedWorkflows=0;
		for(int i=1;i<workflows.length;i++){
			if(workflows[i].contains("MSS_3")){
				workflows[i].replaceAll("/.*\"", mssDir[0]+"\"");
				updatedWorkflows++;
			}
			if(workflows[i].contains("MSS_4")){
				workflows[i].replaceAll("/.*\"", mssDir[1]+"\"");
				updatedWorkflows++;
			}
		}

		if(updatedWorkflows<2){
			log.error("Did not find MSS_3 and MSS_4 in MSS.WF01_PreProcessing workflow:");
			for(int i=1;i<workflows.length;i++){
				log.error(workflows[i]);
			}
		}
		
		for(int i=0;i<workflows.length;i++){
			log.info("Contents are written into /tmp/msseditedworkflows.csv: "+workflows[i]);
			session.send("echo $\'"+workflows[i]+"\n\' >>/tmp/msseditedworkflows.csv", "");
			session.waitForFinish(5000L);
		}
		
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport MSS.WF01_PreProcessing /tmp/msseditedworkflows.csv","");
		session.waitForFinish(240000L);
		log.info("Import results: "+session.getStdOut());
		boolean success = session.getExitCode() == 0;
		
		session.send("rm", "/tmp/mssworkflows.csv");
		session.waitForFinish(3000L);
		session.send("rm", "/tmp/msseditedworkflows.csv");
		session.waitForFinish(3000L);
		
		return success;
	}
	
	public boolean restartWorkflows(String[] wfToRestart,String[] wfGroupsToRestart){
		boolean stillActive = true;
		boolean success = false;
		int stopTimeout=0;
		session = Session.getSession(DataHandler.getHostByName("controlzone"));
		
		while(stillActive&&stopTimeout<120){
			stillActive=false;
			for(String workflow : wfToRestart){
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop "+workflow, "");
				session.waitForFinish(600000L);
				if(!session.getStdOut().contains("Configuration not activated")){
					stillActive=true;
				}
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable "+workflow, "");
				session.waitForFinish(600000L);
				if(!session.getStdOut().contains("Already")){
					stillActive=true;
				}
			}
			for(String workflowGP : wfGroupsToRestart){
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop "+workflowGP, "");
				session.waitForFinish(600000L);
				if(!session.getStdOut().contains("Configuration not activated")){
					stillActive=true;
				}
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable "+workflowGP, "");
				session.waitForFinish(600000L);
				if(!session.getStdOut().contains("Already")){
					stillActive=true;
				}
			}
			if(stillActive){
				log.info("Waiting 30 seconds to confirm that workflows have stopped");
				pause(30);
			}else{
				log.info("Workflows stopped and disabled");
			}
			
			stopTimeout++;
		}
		
		for(String wf : wfToRestart){
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable "+wf, "");
			session.waitForFinish(600000L);
		}
		success = session.getExitCode() == 0;
		
		for(String wfGroup : wfGroupsToRestart){
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable "+wfGroup, "");
			session.waitForFinish(600000L);
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart "+wfGroup,"");
			session.waitForFinish(600000L);
		}
		
		return success;
	}

	public boolean refreshLteefaTopology() {
		Host host = DataHandler.getHostByName("controlzone");
		
		session = Session.getSession(host);
		session.send("cd /eniq/mediation_inter/M_E_LTEEFA/bin;./mg_topology_refresh.sh", "");
		session.waitForFinish(1200000L);
		return session.getExitCode() == 0;
	}

	public boolean disableAllWorkflows(String[] exceptions) {
		String exclude = StringUtils.join(exceptions, '|');
		session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist","");
		session.waitForFinish(1200000L);
		String[] wflist = session.getStdOut().trim().split("\n");
		StringBuilder wfToDisable = new StringBuilder();
		
		for(String wf : wflist){
			if((!wf.matches(exclude))&& (wf.contains("EBSL")||
					wf.contains("GPEH")||wf.contains("MSS")||wf.contains("SGEH")||
					wf.contains("Sim")||wf.contains("eankmuk_4G"))){
				wf = wf.split("\\(")[0];
				wfToDisable.append(wf + " ");
			}
		}

		return disableWorkflow(wfToDisable.toString().trim());
	}

	public boolean provisioLteefaWorkflows() {
		Host host = DataHandler.getHostByName("ec_1");
		boolean success = true;
		session = Session.getNewSession(host);
		
		session.send("source ~/.profile;cd /eniq/mediation_inter/M_E_LTEEFA/bin;./provision_workflows.sh", "");
		session.waitForFinish(120000000L);
		String result = session.getStdOut();
		if(result.contains("ERROR")||result.contains("Can NOT")||result.contains("failed")
				||result.contains("No OSS information found"))
			success = false;
		return session.getExitCode() == 0 && success;
	}
	
	public boolean updateReservedDataTimeRanges(){
		String reservedDataLocation = 
				"/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
		Host host = DataHandler.getHostByName("controlzone");
		session = Session.getNewSession(host);
		
		session.send("ls", reservedDataLocation);
		session.waitForFinish(120000000L);
		if(session.getExitCode()==0){
			session.send("cat", reservedDataLocation);
			session.waitForFinish(120000000L);
			String[] contents = session.getStdOut().trim().split("\n");
			for(int i = 0; i < contents.length; i++){
				contents[i] = contents[i].replaceAll("TIME_RANGES=.*$", "TIME_RANGES=15 minutes,30 minutes,1 hour,2 hours");
				contents[i] = contents[i].replaceAll("TIME_RANGES_LTE=.*$", "TIME_RANGES_LTE=15 minutes");
				contents[i] = contents[i].replaceAll("KPI_TIME_RANGES.*$", "KPI_TIME_RANGES=5 minutes,15 minutes,30 minutes,1 hour,2 hours");
				contents[i] = contents[i].replaceAll("NUMBER_OF_NODES=\\d+", "NUMBER_OF_NODES=3");
				contents[i] = contents[i].replaceAll("NUMBER_OF_SUBSCRIBERS=\\d+", "NUMBER_OF_SUBSCRIBERS=3");
			}
			session.send("cp ",reservedDataLocation+ " " + reservedDataLocation + ".bkp");
			session.waitForFinish(120000000L);
			session.send("echo  $\'" + StringUtils.join(contents,"\n") + "\' >" , reservedDataLocation);
			session.waitForFinish(120000000L);
			
			log.info("Updated time ranges in reservedData.csv to be 2 hours max");
			return session.getExitCode() == 0;
		}
		
		log.error("Could not find reserved data file at location: " + reservedDataLocation);
		return false;
	}

	public boolean startDataLoadingOnCEP() {
		String remotePath = "/net/atclvm559/tmp/CentralDatagen/"+
				DataHandler.getHostByName("cep").getIp().split("\\.")[0] +
				"/50files/ossrc/data/pmMediation/eventData";
		String localPath = "/ossrc/data/pmMediation/eventData";
		boolean success;
		
		log.info("dataGenStart_OnCEP()");
		
		registerHostOnDatagen(DataHandler.getHostByName("cep"));

		Session cep = Session.getNewSession(DataHandler.getHostByName("cep"));
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp",
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml.bkp");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp",
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml.bkp");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp", 
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml.bkp");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py", "ntpdate nasconsole 2>/dev/null");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation stop", 
				"2>/dev/null");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp",
				"/eniq/home/dcuser/automation/topology/CEP_configs/network_elements.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/network_elements.xml");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp", 
				"/eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp", 
				"/eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml" +
				" /opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py rm -rf", localPath);
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py mkdir -p", 
				"/ossrc/data/pmMediation 2>/dev/null");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py ln -s", 
				remotePath + " " + localPath + " 2>/dev/null");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation start", 
				"2>/dev/null");
		cep.waitForFinish(12000L);
		success = cep.getExitCode()==0;
		if(success)
			log.info("cep-mediation service started successfully");
		else
			log.error("failed to start cep-mediation service");
		wcdmaDbSetup();
		return success;
	}
	
	public boolean editCEPConfigFiles(){
		String cep_ip="";
		Session cep= Session.getNewSession(DataHandler.getHostByName("cep"));
		Session controlzone= Session.getNewSession(DataHandler.getHostByName("controlzone"));
		String[] cepConfigFiles = {
				"/eniq/home/dcuser/automation/topology/CEP_configs/network_elements.xml",
				"/eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml",
				"/eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml"};
		String contents;
		controlzone.send("cat", "/eniq/installation/config/cep_mediation.ini");
		controlzone.waitForFinish(120000000L);
		contents = controlzone.getStdOut();
		
		Pattern pattern = Pattern.compile("SERVICES_IP=(.*)\n");
		Matcher matcher = pattern.matcher(contents);
		matcher.matches();
		
		if(matcher.find()){
			cep_ip = matcher.group(1).trim();
		}
		
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py bash", 
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/restore.sh");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp", 
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/standalone.xml " +
				"/eniq/home/dcuser/automation/topology/CEP_configs/standalone.xml");
		cep.waitForFinish(120000000L);
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py /bin/cp",
				"/opt/ericsson/cep-mediation/cep-mediation/etc/app-config/hazelcast.xml " +
				"/eniq/home/dcuser/automation/topology/CEP_configs/hazelcast.xml");
		cep.waitForFinish(120000000L);
		
		for(String cepConfig : cepConfigFiles){
			controlzone.send("cat", cepConfig);
			controlzone.waitForFinish(120000000L);
			contents = controlzone.getStdOut();
			contents = contents.replaceAll("ip=\\\"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\\"",
					"ip=\\\""+cep_ip+"\\\"");
			contents = contents.replaceAll("<interface>\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}</interface>",
					"<interface>"+cep_ip+"</interface>");
			controlzone.send("echo  $\'" + contents + "\' >" , cepConfig);
			controlzone.waitForFinish(12000L);
		}
		
		return controlzone.getExitCode() == 0 && cep.getExitCode() == 0;
	}

	public boolean getCepServiceStatus() {
		Session cep = Session.getNewSession(DataHandler.getHostByName("cep"));
		cep.send("/eniq/home/dcuser/automation/runCommandAsRoot.py service cep-mediation status", 
				"2>/dev/null");
		cep.waitForFinish(120000000L);
		String out = cep.getStdOut();
		return cep.getExitCode() == 0 && out.contains("Application is running");
	}

	public boolean checkCepBlade() {
		Session coordinator = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		coordinator.send("cat", "/etc/hosts");
		coordinator.waitForFinish(60000L);
		boolean success = coordinator.getStdOut().contains("cep_med_1") &&
				coordinator.getExitCode() == 0;
		return success;
	}
	
	public boolean provisionKPINotificationWorkflows(boolean remoteDG, boolean force, Map<String, Object> args){
		if(dataGenOperator.isMultiBladeServer(args)){
			dataGenOperator.disableUnrelatedWorkflows("SGEH*", args);
			dataGenOperator.disableUnrelatedWorkflows("MSS*", args);
		}
		return dataGenOperator.setup2g3g4gWorkflows(remoteDG, force, args);
	}
	
	public boolean loadKPINotificationData(Map<String, Object> args){
		
		String dir = "/eniq/data/eventdata/events_oss_1/sgeh/dir3";
		String host =((Host)args.get("host")).getIp().substring(0, 10);
		String[] remoteDirs = {"/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/eventdata/events_oss_1/sgeh",
				"/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/pushData/04/mss/MSS_3",
				"/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/pushData/03/mss/MSS_4"};
	    String[] tablesToCheck = {"event_e_lte_raw","dc.event_e_mss_voice_cdr_raw","dc.event_e_mss_sms_cdr_raw"};
		String[] columns = {"ne_version","HIER3_ID","HIER3_ID"};
		String[] columnsValues = {"4,13"};
		
		boolean result1=dataGenOperator.enableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir3_*", args);
		boolean result2=dataGenOperator.enableWorkflow("SGEH.WF_SGEH_Processing_NFS.events_oss_1_dir4_*", args);
		boolean result3=dataGenOperator.enableWorkflowGroup("SGEH.WFG_SGEH_Processing_NFS_OSSRC1_3*", args);
		boolean result4=dataGenOperator.enableWorkflowGroup("SGEH.WFG_SGEH_Processing_NFS_OSSRC1_4*", args);
		
		session = Session.getSession((Host) args.get("host"));
		
		if(!(result1&&result2&&result3&&result4)){
			
			dataGenOperator.disableWorkflow("EBSL*", args);
			session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4'", "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
		}
		
		session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1 EC_SGEH_1'", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		
		session.send("ls -d /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		if(session.getExitCode()!=0){
			session.send("echo $\'temp\' >/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$", "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
			session.send("rm /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$", "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
			session.send("mkdir /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+ host, "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
		}
		
		return checkDataLoading("ec_1",remoteDirs, tablesToCheck, 40, true, columns,columnsValues);
	}
	
	public boolean loadMSSData (boolean remoteDG, boolean force, Map<String, Object> args){
		String dataGenDir = "/eniq/data/pushData/00/mss/MSS_3";
		String host =((Host)args.get("host")).getIp().substring(0, 10);
		String[] nfs_dirs={"/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/pushData/04/mss/MSS_3",
				"/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/pushData/03/mss/MSS_4"};
		
		String[] mss_dirs={"/eniq/data/pushData/04/mss/MSS_3","/eniq/data/pushData/03/mss/MSS_4"};
		String[] tablesToCheck={"dc.event_e_mss_voice_cdr_raw","dc.event_e_mss_sms_cdr_raw"};
		String[] columns = {"HIER3_ID","HIER3_ID"};
		String[] columnsValues = {};
		
		session = Session.getSession((Host) args.get("host"));
		
		if(remoteDG){
			dataGenDir = mss_dirs[0];
			session.send("ls -d /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"", "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
			if(session.getExitCode()!=0){
				session.send("echo $\'temp\' >/net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$", "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				session.send("rm /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/tempfile.$$", "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				session.send("mkdir /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+ host, "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
			}
		}
		
		updateReservedDataTimeRanges();
		dataGenOperator.create4GAndMSSGroup(force, args);
		
		session.send("ls /eniq/data/pushData", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		String[] dirs = session.getStdOut().split("  ");
		mss_dirs[0]="";
		mss_dirs[1]="";

		for(String subdir : dirs){
			String patternStr="[0-1][0-9]";
			Pattern p = Pattern.compile(patternStr);
			Matcher m = p.matcher(subdir);
			if(m.find()){
				log.info(subdir);
			    session.send("ls /eniq/data/pushData/"+subdir+"/mss", "");
			    session.waitForFinish((Long) Long.valueOf(timeOut));
			    String[] subdirs=session.getStdOut().split("  ");
			    for(String subSubDir : subdirs){
			    	String patternStr1="^MSS";
			    	Pattern p1 = Pattern.compile(patternStr1);
			    	Matcher m1 = p1.matcher(subSubDir);
			    	if(m1.find()){
			    		for(String nfsDir : nfs_dirs){
			    			String tempNfsDir = nfsDir;
			    			tempNfsDir=tempNfsDir.replaceAll(".*/", "");
			    			if(subSubDir.contains(tempNfsDir)){
			    				for(int i=0;;i++){
			    					mss_dirs[i]="/eniq/data/pushData/"+subdir+"/mss/"+subSubDir;
			    					log.info("MSS DIR: " + mss_dirs[i]);
			    				}
			    				
			    			}
			    		}
			    	}
			    }
			    
			}
		}
		
		if(mss_dirs.length==0){
			mss_dirs[0]="/eniq/data/pushData/04/mss/MSS_3";
			mss_dirs[1]="/eniq/data/pushData/03/mss/MSS_4";
		}
		
		if(!force){
		  boolean dataLoadingResults=checkDataLoading("ec_1",nfs_dirs, tablesToCheck, 40, false, columns,columnsValues);
		  if(dataLoadingResults){
			  log.info("Skipping datagenStart_MSS because it's been done. To force it to load put STARTDATAGEN_MSS force or STARTDATAGEN_KPI force in a config file");
			  log.info("Start up EC_1 anyway to make sure they are running");
			  session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4'", "");
			  session.waitForFinish((Long) Long.valueOf(timeOut));
			  session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'", "");
			  session.waitForFinish((Long) Long.valueOf(timeOut));
		  }
		}else{
			force=true;
		}
		
		if(!dataGenOperator.isMultiBladeServer(args)){
			dataGenOperator.disableUnrelatedWorkflows("SGEH*", args);
			dataGenOperator.disableUnrelatedWorkflows("MSS*", args);
		}
		
		log.info("Disabling EBSL workflows and shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
		dataGenOperator.disableWorkflow("EBSL*", args);
		session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr shutdown EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4'", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("ssh dcuser\\@ec_1 'source ~/.profile; /eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr startup EC1'", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		
		if(remoteDG){
		   log.info("Running mss_populate.sh");
		   session.send("/eniq/mediation_inter/M_E_MSS/bin/mss_populate.sh", "");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   mss_dirs=nfs_dirs;
		   if(mss_dirs.length!=0){
			   dataGenDir=mss_dirs[0];
			   dataGenOperator.updateMssPreprocessingWorkflow(mss_dirs[0], mss_dirs[1], args);
			   String[] wfToRestart = {"MSS.WF01_PreProcessing.MSS_3","MSS.WF01_PreProcessing.MSS_4"};
			   String[] wfGroupsToRestart={"MSS.WG01_PreProcessing"};
			   dataGenOperator.restartWorkflows(wfToRestart, wfGroupsToRestart, args);
			   log.info("Running mss_topology_refresh.sh");
			   log.info("Disabling unused workflows");
			   session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable MSS.WF*.0[4-9]", "");
			   session.waitForFinish((Long) Long.valueOf(timeOut));
			   session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable MSS.WF*.1[0-5]", "");
			   session.waitForFinish((Long) Long.valueOf(timeOut));
			   session.send("/eniq/mediation_inter/M_E_MSS/bin/mss_topology_refresh.sh", "");
			   session.waitForFinish((Long) Long.valueOf(timeOut));
		   }else{
			   log.error("ERROR:Remote datagen for MSS_3 and/or MSS_4 at /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/pushData/04/mss/MSS_3/4 are not being populated");
		   }
		   
		}
		
		return checkDataLoading("ec_1",nfs_dirs, tablesToCheck, 40, true, columns,columnsValues);
	}
	
	/**************** PRIVATE METHODS *********************/
	
	private Session getSession(Host host) {
		session = Session.getSession(host);
		return session;
	}
	
	private boolean isLong(String str){
		try{
			Long.parseLong(str);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private void pause(int seconds){
		long mseconds = 1000*seconds;
		try{
			Thread.sleep(mseconds);
		}catch(InterruptedException e){
			
		}
	}
	
	private boolean sqlCreate(Host host,String query){
		boolean success = false;
		session = Session.getNewSession(host);
		session.send("echo $\'"+query+"\ngo\' >/eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish(12000L);
		session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 " +
				"-Ddwhdb -Sdwhdb -w 50 -b -i /eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish(12000L);
		success = session.getExitCode() == 0;
		session.send("rm /eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish(12000L);
		return success;
	}
	
	private String[] sqlSelect(Host host,String query){
		String[] output = null;
		
		session = Session.getSession(host);
		session.send("echo $\'"+query+"\ngo\' >/eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish(30000L);
		session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 " +
				"-Ddwhdb -Sdwhdb -w 500 -b -i /eniq/home/dcuser/automation/SQLselect.sql > /tmp/taf.query.output", "");
		session.waitForFinish(600000L);
		
		session.send("cat /tmp/taf.query.output");
		session.waitForFinish(600000L);
		
		output = (session.getStdOut().trim()).split("\n");
		ArrayList<String> list = new ArrayList<String>(); 
		for(String line : output){
			line = line.trim();
			if((!line.isEmpty()) && (!line.contains("row affected")) && (!line.contains("rows affected"))){
				list.add(line);
			}
		}
		
		session.send("rm /eniq/home/dcuser/automation/SQLselect.sql");
		session.waitForFinish(30000L);
		session.send("rm /tmp/taf.query.output");
		session.waitForFinish(30000L);
		
		return list.toArray(new String[list.size()]);
	}
	
	private boolean sqlInsert(String query){
		boolean success = false;
		session = Session.getSession(DataHandler.getHostByName("controlzone"));
		session.send("echo $\'"+query+"\ngo\' >/eniq/home/dcuser/automation/SQLinsert.sql", "");
		session.waitForFinish(30000L);
		session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 " +
				"-Ddwhdb -Sdwhdb -w 50 -b -i /eniq/home/dcuser/automation/SQLinsert.sql", "");
		session.waitForFinish(180000L);
		success = session.getExitCode() == 0;
		session.send("rm /eniq/home/dcuser/automation/SQLinsert.sql", "");
		session.waitForFinish(30000L);
		return success;
	}
	
	private boolean verfifyDataTable(String tableName, String columnName,
			String columnValue, String dateTime) {
		boolean success = true;
		Host host = DataHandler.getHostByName("controlzone");
		String query;
		String[] result;
		
		if(!(columnName == null || columnName.equals(""))){
			query = "SELECT " + columnName + " FROM " + tableName +
					" WHERE datetime_id > \\'" + dateTime + "\\'";
			result = sqlSelect(host, query);
			
			for(String raw : result){
				if(raw == null || raw.contains("0 rows affected")){
					success = false;
					break;
				}else if((!raw.equals(""))&&(!raw.contains("rows affected"))
						&&((columnValue==null)||(columnValue.equals(""))||(raw.toLowerCase().contains(columnValue.toLowerCase())))){
					log.info("Found data in " + tableName + " : " + raw);
					success = true;
					break;
				}
			}
		}else{
			query = "SELECT count(*) FROM " + tableName +
					" WHERE datetime_id > \\'" + dateTime + "\\'";
			result = sqlSelect(host, query);
			if(Integer.parseInt(result[0].trim()) == 0)
				success = false;
		}
		return success;
	}

	private boolean verifyDataDir(String mediation_hostname, String dir, long startTime){
		String results;
		Host host = DataHandler.getHostByName(mediation_hostname);

		session = Session.getNewSession(host);
		session.send("echo $\'#!/usr/bin/perl\nmy $var=(stat(\""+dir+"\"))[9];\nprint $var;\' >/tmp/cmdfile.pl", "");
		session.waitForFinish(1200000L);
		session.send("chmod 755 /tmp/cmdfile.pl", "");
		session.waitForFinish(1200000L);
		session.send("perl","/tmp/cmdfile.pl");
		session.waitForFinish(1200000L);
		results = session.getStdOut();
		try{
			log.info("Timestamp on " + dir + " : " + results.trim());
			return session.getExitCode()==0 && Long.parseLong(results.trim())>startTime;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private void wcdmaDbSetup(){
		log.info("wcdmaDbSetup()");
		String insert_query = "insert into mapping_gpeh_d select dc.dim_e_ran_rncfunction.rncid, " +
				"dc.dim_e_sgeh_hier321_cell.cid, dc.dim_e_ran_rncmodule.rncmodule, dc.dim_e_ran_rnc.rnc_name " +
				"from dc.dim_e_sgeh_hier321, dc.dim_e_sgeh_hier321_cell, dc.dim_e_ran_rnc, " +
				"dc.dim_e_ran_rncfunction , dc.dim_e_ran_rncmodule " +
				"where dc.dim_e_sgeh_hier321.hier321_id = dc.dim_e_sgeh_hier321_cell.hier321_id " +
				"and dc.dim_e_sgeh_hier321.hierarchy_3 = dc.dim_e_ran_rnc.alternative_fdn and " +
				"dc.dim_e_ran_rnc.rnc_fdn = dc.dim_e_ran_rncfunction.sn " +
				"and dc.dim_e_ran_rnc.rnc_fdn = dc.dim_e_ran_rncmodule.sn;";
		String create_query = "create table dc.MAPPING_gpeh_D (rncId INTEGER NULL, cId INTEGER NULL, " +
				"RncModule INTEGER NULL, RNC_NAME VARCHAR(50) NULL);";
		sqlCreate(DataHandler.getHostByName("controlzone"),create_query);
		sqlCreate(DataHandler.getHostByName("controlzone"),insert_query);
	}

	public boolean runSelenium(Map<String, Object> args) {
		String testGroup = (String)args.get("testgroup");
		String seleniumJar = "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/"
				+ (String)args.get("jar");
		String seleniumRCHost = (String)args.get("host");
		String localHostArg="";
		String portArg="";
		String startCommand="start";
		String stopCommand="stop";
		String browserURL="http://"+DataHandler.getHostByName("glassfish").getIp();
		String hostURL="http://"+DataHandler.getHostByName("controlzone").getIp();
		String eniqVersion = "1" + getEniqVersion();
		Host host = DataHandler.getHostByName("controlzone");
		
		Session coordinator_session = Session.getSession(host);
		
		coordinator_session.send("ls", testGroup+"*log*");
		coordinator_session.waitForFinish(60000L);
		if(coordinator_session.getExitCode()==0){
			coordinator_session.send("rm", testGroup+"*");
			coordinator_session.waitForFinish(60000L);
		}
		
		if(seleniumRCHost.equalsIgnoreCase("localhost")){
			coordinator_session.send("chmod 0755", "/eniq/home/dcuser/automation/localSeleniumRC.sh");
			coordinator_session.waitForFinish(60000L);
			stopLocalSeleniumRCServer();
			startLocalSeleniumRCServer();
			startCommand="startUnix";
			stopCommand="stopUnix";
			localHostArg="-DRUN_LOCATION=localhost";
			portArg="-DSERVERPORT=4566";
		}
		
		StringBuilder seleniumArgs = new StringBuilder();
		seleniumArgs.append(localHostArg + " ");
		seleniumArgs.append(portArg + " ");
		seleniumArgs.append("-DHOST="+browserURL+" ");
		seleniumArgs.append("-DSERVERHOST="+seleniumRCHost+" ");
		seleniumArgs.append("-DENIQVERSION="+eniqVersion+" ");
		seleniumArgs.append("-DHOSTADMINUI="+hostURL+" ");
		seleniumArgs.append("-DTEST_GROUP="+testGroup+" ");
		seleniumArgs.append("-DTEST_BLADE"+" ");
		seleniumArgs.append("-jar "+seleniumJar);
		
		System.out.println(seleniumArgs.toString().trim());
		
		boolean result = executeSeleniumAndWait(seleniumArgs.toString().trim(), 10800000L);
		
		return result;
	}

	private String getEniqVersion() {
		Host host = DataHandler.getHostByName("controlzone");
		Session coordinator_session = Session.getSession(host);
		
		coordinator_session.send("grep ENIQ_STATUS", 
				"/eniq/admin/version/eniq_status");
		coordinator_session.waitForFinish(60000L);
		String eniq_version = coordinator_session.getStdOut();
		
		int beginIndex = eniq_version.indexOf("ENIQ_Events_Shipment_");
		beginIndex += "ENIQ_Events_Shipment_".length();
		eniq_version = eniq_version.substring(beginIndex);
		int stopIndex = eniq_version.indexOf(' ');
		eniq_version = eniq_version.substring(0,stopIndex);
		
		return eniq_version;
	}

	private boolean executeSeleniumAndWait(String args, long timeout) {
		Host host = DataHandler.getHostByName("controlzone");
		Session coordinator_session = Session.getSession(host);
		
		coordinator_session.send("cd /eniq/home/dcuser/automation; /eniq/sw/runtime/java/bin/java",args);
		coordinator_session.waitForFinish(timeout);
		System.out.println("EXIT CODE:"+coordinator_session.getExitCode());
		return coordinator_session.getExitCode() == 0;
	}

	private boolean startLocalSeleniumRCServer() {
		Host host = DataHandler.getHostByName("controlzone");
		Session coordinator_session = Session.getSession(host);
		
		coordinator_session.send("echo \"/eniq/home/dcuser/automation/localSeleniumRC.sh " +
				"-port 4566 >/dev/null 2>&1\" | at now", "");
		coordinator_session.waitForFinish(60000L);
		coordinator_session.send("ps -ef | grep localSeleniumRC.sh | grep -v grep","");
		coordinator_session.waitForFinish(60000L);
		
		return coordinator_session.getStdOut().contains("localSeleniumRC");
	}

	private void stopLocalSeleniumRCServer() {
		Host host = DataHandler.getHostByName("controlzone");
		Session coordinator_session = Session.getSession(host);
		
		coordinator_session.send("/eniq/home/dcuser/automation/localSeleniumRC.sh", 
				"kill undofirefoxfix >/dev/null 2>&1");
		coordinator_session.waitForFinish(180000L);
	}

	public boolean areKpiAnalysisMapsLoaded(Map<String, Object> args) {
		Host glassfish = DataHandler.getHostByName("glassfish");
		Session glassfish_session = Session.getSession(glassfish);
		
		glassfish_session.send("psql -l -U postgres postgres | grep rnc");
		glassfish_session.waitForFinish(180000L);
		String stdOut = glassfish_session.getStdOut().trim();
		if(stdOut.contains("rnc_cell_datastore")){
			log.info("KpiAnalysis Maps Already loaded");
			return true;
		}
		
		return loadKpiAnalysisMaps(args);
	}

	private boolean loadKpiAnalysisMaps(Map<String, Object> args2) {
		String mapsFile = "natural_earth_raster_map_R2A02.tar.gz";
		String mapsFolder = "/eniq/backup/natural_earth_maps/";
		String remoteFolder = "/net/atclvm559.athtem.eei.ericsson.se/package/maps/";
		Host glassfish = DataHandler.getHostByName("glassfish");
		Session glassfish_session = Session.getSession(glassfish);
		
		glassfish_session.send("mkdir -p", mapsFolder);
		glassfish_session.waitForFinish(30000L);
		glassfish_session.send("ls /net/atclvm559.athtem.eei.ericsson.se");
		glassfish_session.waitForFinish(30000L);
		glassfish_session.send("cp " + remoteFolder + mapsFile + " " + mapsFolder);
		glassfish_session.waitForFinish(240000L);
		glassfish_session.send("gunzip -f " + mapsFolder+mapsFile + " -d " + mapsFolder);
		glassfish_session.waitForFinish(240000L);
		glassfish_session.send("cd " + mapsFolder + "; tar -xvf natural_earth_raster_map_R2A02.tar");
		glassfish_session.waitForFinish(240000L);
		
		glassfish_session.send("cd /eniq/backup/natural_earth_maps/opengeo/map/natural_earth_raster_map/; " +
				"./setup.sh geoserver.properties; cd /eniq/home/dcuser/automation");
		glassfish_session.waitForFinish(600000L);
		glassfish_session.send("cd /eniq/backup/natural_earth_maps/opengeo/map/natural_earth_raster_map/; " +
				"./setup_RNC_Cell.sh geoserver.properties; cd /eniq/home/dcuser/automation");
		glassfish_session.waitForFinish(240000L);
		
		glassfish_session.send("cp /net/atclvm559.athtem.eei.ericsson.se/package/maps/AmericaCells.csv " +
				"/eniq/backup/natural_earth_maps/opengeo/map/natural_earth_raster_map/");
		glassfish_session.waitForFinish(240000L);
		glassfish_session.send("cp /net/atclvm559.athtem.eei.ericsson.se/package/maps/AmericaRncs13_9.csv " +
				"/eniq/backup/natural_earth_maps/opengeo/map/natural_earth_raster_map/");
		glassfish_session.waitForFinish(240000L);
		
		glassfish_session.send("cd /eniq/backup/natural_earth_maps/opengeo/map/natural_earth_raster_map/; " +
				"./uploadData.sh AmericaCells.csv AmericaRncs13_9.csv; cd /eniq/home/dcuser/automation");
		glassfish_session.waitForFinish(240000L);
		
		glassfish_session.send("psql -l -U postgres postgres | grep rnc");
		glassfish_session.waitForFinish(240000L);
		
		return glassfish_session.getStdOut().trim().contains("rnc_cell_datastore");
	}

	public boolean updateSessionBrowserImsis(Map<String, Object> args) {
		String[] query = new String[]{
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_DATA_BEARER_SESSION FROM (SELECT DISTINCT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_RAW where IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_ATTACH FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 0 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_PDP_ACTIVATE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 1 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_RAU FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 2 AND HIERARCHY_3!='Unknown'  AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_ISRAU FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 3 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_DEACTIVATE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 4 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_DETACH FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 14 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_SERVICE_REQUEST FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_SGEH_RAW WHERE EVENT_ID = 15 AND HIERARCHY_3!='Unknown' AND HIERARCHY_3!=NULL AND IMSI!=NULL AND IMSI!=0 AND RAT=1) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SUCCESSFUL_HSDSCH_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_SUC_HSDSCH_CELL_CHANGE_RAW WHERE EVENT_ID = 432  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_CALL_SETUP_FAILURES FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_CFA_ERR_RAW WHERE EVENT_ID = 456  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SYSTEM_RELEASE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_CFA_ERR_RAW WHERE EVENT_ID = 438  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_OUT_HARD_HANDOVER FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_INTER_OUT_HHO_RAW WHERE EVENT_ID = 458  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_RRC_MEASUREMENT_REPORT FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_RRC_MEAS_RAW WHERE EVENT_ID = 8  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_FOR_CELL_VISITED_CHART FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_SESSION_CELL_VISITED_RAW WHERE  IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_SOFT_HANDOVER_EXECUTION_FAILURE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_SOHO_ERR_RAW WHERE EVENT_ID = 408  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_IFHO_HANDOVER_EXECUTION_FAILURE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_IFHO_ERR_RAW WHERE EVENT_ID = 423  AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_FAILED_HSDSCH_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_HSDSCH_ERR_RAW WHERE EVENT_ID = 433 AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME AS SB_IMSI_INT_HSDSCH_NO_CELL_CHANGE FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_RAN_HFA_HSDSCH_ERR_RAW WHERE EVENT_ID = 436 AND IMSI!=NULL AND IMSI!=0) a ORDER BY a.EVENT_TIME DESC",
				"SELECT TOP 1 APPLICATION.IMSI ||','|| APPLICATION.DATETIME_ID||';' AS SB_IMSI_APPLICATION_CHART FROM (SELECT IMSI, DATETIME_ID, COUNT(*) AS x FROM EVENT_E_USER_PLANE_CLASSIFICATION_RAW WHERE IMSI!=NULL AND IMSI!=0 AND APN!=NULL AND FIVE_MIN_AGG_TIME!=NULL AND PACKETS_DOWNLINK>0 AND PACKETS_UPLINK>0 GROUP BY IMSI, DATETIME_ID) AS APPLICATION WHERE APPLICATION.x >2 ORDER BY APPLICATION.DATETIME_ID DESC",
				"SELECT TOP 1 a.IMSI ||','|| a.EVENT_TIME||';' AS SB_IMSI_TCP_PERFORMANCE_CHART FROM (SELECT IMSI AS IMSI, EVENT_TIME AS EVENT_TIME FROM EVENT_E_USER_PLANE_TCP_RAW WHERE IMSI!=NULL AND IMSI!=0 AND START_APN!=NULL AND START_APN!=NULL AND DATETIME_ID!=NULL) a ORDER BY a.EVENT_TIME DESC"};
		String[] eventType = new String[]{
				"DATA_BEARER_SESSION",
				"ATTACH",
				"PDP_ACTIVATE",
				"RAU",
				"ISRAU",
				"DEACTIVATE",
				"DETACH",
				"SERVICE_REQUEST",
				"INT_SUCCESSFUL_HSDSCH_CELL_CHANGE",
				"INT_CALL_SETUP_FAILURES",
				"INT_SYSTEM_RELEASE",
				"INT_OUT_HARD_HANDOVER",
				"RRC_MEASUREMENT_REPORT",
				"FOR_CELL_VISITED_CHART",
				"INT_SOFT_HANDOVER_EXECUTION_FAILURE",
				"INT_IFHO_HANDOVER_EXECUTION_FAILURE",
				"INT_FAILED_HSDSCH_CELL_CHANGE",
				"INT_HSDSCH_NO_CELL_SELECTED",
				"APPLICATION_SUM_CHART",
				"TCP_PERFORMANCE_CHART"};
		
		int wait_time = 2*60;	// 2 hours
		
		while(wait_time > 0){
			boolean success = false;
			boolean allImsiFound = true;
			for(int i = 0; i < eventType.length; i++){
				success = updateSBImsi(query[i],eventType[i]);
				if(!success){
					allImsiFound = false;
				}
			}
			
			if(allImsiFound){
				log.info("All SB IMSIs required for UI tests populated");
				break;
			}
			log.info("Not all IMSIs are available, waiting for a max of " + wait_time + " minutes");
			wait_time--;
			pause(60);
		}
		
		if(wait_time <= 0){
			log.error("Not all SB IMSIs required for UI tests populated");
			return false;
		}
		
		return true;
	}

	private boolean updateSBImsi(String query, String eventName) {
		String reservedDataLocation="/eniq/home/dcuser/selenium/" +
				"selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
		String reservedDataImsiLine = "SB_IMSI_"+eventName+"=var1";
		
		log.info(query);
		String[] result = sqlSelect(DataHandler.getHostByName("controlzone"),query);
		log.info(result[0]);
		
		if(result.length > 0){
			reservedDataImsiLine = reservedDataImsiLine.replaceAll("var1", result[0].trim());
			Session coordinator_session = Session.getSession(DataHandler.getHostByName("controlzone"));
			coordinator_session.send("ls", reservedDataLocation);
			coordinator_session.waitForFinish(30000L);
			if(coordinator_session.getExitCode() == 0){
				coordinator_session.send("cat", reservedDataLocation);
				coordinator_session.waitForFinish(30000L);
				String[] content = coordinator_session.getStdOut().split("\n");
				
				for(String line : content){
					line = line.replaceAll("SB_IMSI_"+eventName+"\\.*$", reservedDataImsiLine);
				}
				
				coordinator_session.send("echo \""+StringUtils.join(content,'\n')+"\" > " + reservedDataLocation);
				coordinator_session.waitForFinish(30000L);
				
				return true;
			}else{
				log.error("Could not find reserved data file at location");
			}
		}
		
		return false;
	}

	public boolean parseSeleniumLog(Map<String, Object> args) {
		Session coordinator_session = Session.getSession(DataHandler.getHostByName("controlzone"));
		String testGroup = (String)args.get("testgroup");
		
		coordinator_session.send("ls -ltr", "/eniq/home/dcuser/automation/");
		coordinator_session.waitForFinish(30000L);
		String[] files = coordinator_session.getStdOut().split("\n");
		boolean success = false;
		
		for(String file : files){
			try{
				file = file.substring(file.lastIndexOf(' ')).trim();
				if(file.contains(testGroup) && file.endsWith("testresults.log")){
					log.info("Processing selenium log file " + file);
					coordinator_session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
					coordinator_session.send("cat", "/eniq/home/dcuser/automation/" + file);
					coordinator_session.waitForFinish(30000L);
					String[] logs = coordinator_session.getStdOut().split("\n");
					coordinator_session.close();
					for(String line : logs){
						if(line.contains("TestResult")){
							String testTag = getPattern(line,"TestTag: (\\S*) TestResult:");
							String testName = getPattern(line," TestName: (\\S*) TestTag:");
							String testResult = getPattern(line,"TestResult: (\\S*) FailureReason:");
							String failureReason = getPattern(line,"FailureReason: (\\S*)");
							if(testResult.contains("PASS")){
								log.info("Test Tag: "+testTag+"\tTest Name: "+testName+"\tTest Result: "+testResult+"\tFailure Reason: " + failureReason);
							}else{
								log.error("Test Tag: "+testTag+"\tTest Name: "+testName+"\tTest Result: "+testResult+"\tFailure Reason: " + failureReason);
							}
						}
					}
					success = true;
				}
			}catch(StringIndexOutOfBoundsException e){
			}
		}
		
		if(!success){
			log.error("No selenium log files for " + testGroup + " were found.");
		}
		
		return success;
	}
	
	private String getPattern(String content, String regex){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		matcher.matches();
		
		if(matcher.find()){
			return matcher.group(1).trim();
		}
		
		return "";
	}

	public boolean create2g3gGroups(Map<String, Object> args2) {
		String warp = getDateWithArg(30);
		boolean success = true;
		
		log.info("Creating and importing groups");
		log.info("Creating and importing  IMSI group");
		String statement = "SELECT TOP 3 IMSI FROM EVENT_E_SGEH_SUC_RAW, DIM_E_SGEH_TAC WHERE " +
				"EVENT_E_SGEH_SUC_RAW.TAC = DIM_E_SGEH_TAC.TAC AND APN != null AND " +
				"EVENT_E_SGEH_SUC_RAW.datetime_id > \\'" + warp + "\\'";
		String[][] imsis = sqlSelectArray(statement);
		
		List<Map<String,String>> elements = new ArrayList<Map<String,String>>();
		Map<String,String> element;
		for(String[] imsi : imsis){
			element = new HashMap<String,String>();
			element.put("IMSI", imsi[0]);
			elements.add(element);
		}
		
		String imsiGroup = createGroupXml(false,"DG_ReserveIMSI_group", "IMSI", elements);
		sqlDelete("delete from GROUP_TYPE_E_IMSI where group_name='DG_ReserveIMSI_group'");
		if(importGroup(imsiGroup)){
			log.info("Imported IMSI groups successfully");
		}else{
			success = false;
			log.error("Failed to import IMSI groups");
		}
		
		log.info("Creating and importing  TAC group");
		statement = "SELECT TOP 3 TAC FROM DIM_E_SGEH_TAC ORDER BY CREATED_TIME DESC";
		String[][] tacs = sqlSelectArray(statement);
		elements = new ArrayList<Map<String,String>>();
		for(String[] tac : tacs){
			element = new HashMap<String,String>();
			element.put("tac", tac[0]);
			elements.add(element);
		}
		
		String tacGroup = createGroupXml(true,"DG_ReserveTERMINAL_group", "TAC", elements);
		sqlDelete("delete from GROUP_TYPE_E_TAC where group_name='DG_ReserveTERMINAL_group'");
		if(importGroup(tacGroup)){
			log.info("Imported TAC groups successfully");
		}else{
			success = false;
			log.error("Failed to import TAC groups");
		}
		
		log.info("Creating and importing  controller groups");
		statement = "SELECT TOP 3 RAT, VENDOR, HIERARCHY_3 from DIM_E_SGEH_HIER321";
		String[][] controllers = sqlSelectArray(statement);
		elements = new ArrayList<Map<String,String>>();
		for(String[] controller : controllers){
			element = new HashMap<String,String>();
			element.put("RAT", controller[0]);
			element.put("VENDOR", controller[1]);
			element.put("HIERARCHY_3", controller[2]);
			elements.add(element);
		}
		
		String controllerGroup = createGroupXml(true,"DG_ReserveController_group", "RAT_VEND_HIER3", elements);
		sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER3 where group_name='DG_ReserveController_group'");
		if(importGroup(controllerGroup)){
			log.info("Imported Controller groups successfully");
		}else{
			success = false;
			log.error("Failed to import Controller groups");
		}
		
		log.info("Creating and importing  access area groups");
		statement = "SELECT TOP 3 RAT, VENDOR, HIERARCHY_3, HIERARCHY_1, CELL_ID FROM DIM_E_SGEH_HIER321_CELL";
		String[][] access_areas = sqlSelectArray(statement);
		elements = new ArrayList<Map<String,String>>();
		for(String[] access_area : access_areas){
			element = new HashMap<String,String>();
			element.put("RAT", access_area[0]);
			element.put("VENDOR", access_area[1]);
			element.put("HIERARCHY_3", access_area[2]);
			element.put("HIERARCHY_1", access_area[3]);
			element.put("CELL_ID", access_area[4]);
			elements.add(element);
		}
		
		String accessAreaGroup = createGroupXml(true,"DG_ReserveAccessArea_group", "RAT_VEND_HIER321_CELL", elements);
		sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER321_CELL where group_name='DG_ReserveAccessArea_group'");
		if(importGroup(accessAreaGroup)){
			log.info("Imported Access Area groups successfully");
		}else{
			success = false;
			log.error("Failed to import Access Area groups");
		}
		
		log.info("Creating and importing  SGSN groups");
		statement = "SELECT TOP 3 SGSN_NAME FROM DIM_E_SGEH_SGSN ORDER BY CREATED DESC";
		String[][] sgsns = sqlSelectArray(statement);
		elements = new ArrayList<Map<String,String>>();
		for(String[] sgsn : sgsns){
			element = new HashMap<String,String>();
			element.put("EVENT_SOURCE_NAME", sgsn[0]);
			elements.add(element);
		}
		
		String sgsnGroup = createGroupXml(true, "DG_ReserveSGSN_group", "EVNTSRC", elements);
		sqlDelete("delete from GROUP_TYPE_E_EVNTSRC where group_name='DG_ReserveSGSN_group'");
		if(importGroup(sgsnGroup)){
			log.info("Imported SGSN groups successfully");
		}else{
			success = false;
			log.error("Failed to import SGSN groups");
		}
		
		log.info("Done Importing Groups");
		return success;
	}
	
	private boolean importGroup(String groupContent) {
		Session coordinator_session = Session.getNewSession(DataHandler.getHostByName("controlzone"));
		String xmlFile = "/tmp/importGroup.xml";
		coordinator_session.send("echo \"" + groupContent + "\" > " + xmlFile);
		coordinator_session.waitForFinish(60000L);
		coordinator_session.send("/eniq/sw/bin/gpmgt -i -add -f " + xmlFile);
		coordinator_session.waitForFinish(300000L);
		boolean success = coordinator_session.getExitCode() == 0;
		coordinator_session.send("rm", xmlFile);
		coordinator_session.waitForFinish(60000L);
		return success;
	}

	private String createGroupXml(boolean standalone, String name, String type, List<Map<String,String>> elements){
		String xmlGroup = null;
		
		if(standalone){
			xmlGroup = "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>\n<groupmgt>\n";
		}else{
			xmlGroup = "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\n<groupmgt>\n";
		}
		xmlGroup += "\t<group name=\\\"" + name + "\\\" type=\\\"" + type + "\\\">\n";
		
		Iterator<Map<String,String>> it = elements.iterator();
		while(it.hasNext()){
			Map<String,String> element = it.next();
			xmlGroup += "\n\t\t<group-element>\n";
			for(String key : element.keySet()){
				xmlGroup += "\t\t\t<key name=\\\"" + key + "\\\" value=\\\"" + element.get(key) + "\\\"/>\n";
			}
			xmlGroup += "\t\t</group-element>\n";
		}
		
		xmlGroup +="\n\t</group>\n";
		xmlGroup +="</groupmgt>\n";
		
		return xmlGroup;
	}
	
	private boolean sqlDelete(String statement){
		boolean success = false;
		String query_file = "/eniq/home/dcuser/automation/SQLdelete.sql";
		Session coordinator_session = Session.getSession(DataHandler.getHostByName("controlzone"));
		
		coordinator_session.send("echo $\'"+statement+"\ngo\' >" + query_file);
		coordinator_session.waitForFinish(30000L);
		coordinator_session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 " +
				"-Ddwhdb -Sdwhdb -w 50 -b -i " + query_file);
		coordinator_session.waitForFinish(180000L);
		
		success = coordinator_session.getExitCode() == 0;
		coordinator_session.send("rm", query_file);
		coordinator_session.waitForFinish(30000L);
		
		return success;
	}
	
	private boolean isFilePresent(Host host, String filename) {
		Session host_session = Session.getNewSession(host);
		host_session.send("ls", filename);
		host_session.waitForFinish(30000L);
		boolean success = host_session.getExitCode() == 0;
		host_session.close();
		return success;
	}

	private String getDateWithArg(int minutes_warp){
		Date date = new Date(System.currentTimeMillis() - minutes_warp*60*1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	private String[][] sqlSelectArray(String statement){
		String[] res = null;
		session = Session.getSession(DataHandler.getHostByName("controlzone"));
		session.send("echo $\'"+statement+"\ngo\' >/eniq/home/dcuser/automation/SQLselectArray.sql", "");
		session.waitForFinish(30000L);
		session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 " +
				"-Ddwhdb -Sdwhdb -w 500 -s# -b -i /eniq/home/dcuser/automation/SQLselectArray.sql", "");
		session.waitForFinish(180000L);
		res = session.getStdOut().trim().split("\n");
		session.send("rm /eniq/home/dcuser/automation/SQLselectArray.sql", "");
		session.waitForFinish(30000L);
		
		String separator = "#";
		List<String> buffer = new ArrayList<String>();
		List<String[]> result = new ArrayList<String[]>();
		for(String line : res){
			if(line.contains(separator)){
				String[] values = line.split(separator);
				for(String value : values){
					value = value.trim();
					if(value.isEmpty()){
						continue;
					}
					buffer.add(value);
				}
				
				String[] tmp = new String[buffer.size()];
				for(int i=0;i<buffer.size();i++){
				   tmp[i] = buffer.get(i);
				}
				result.add(tmp);
				buffer = new ArrayList<String>();
			}
		}
		
		String[][] string_array = new String[result.size()][];
		for(int i = 0; i < string_array.length; i++){
			string_array[i] = result.get(i);
		}
		return string_array;
	}
	
	private boolean isMultiBladeServer(){
		String controlzoneHost = null; 
		String ec1Host = null;
		session = getSession(DataHandler.getHostByName("controlzone"));
		session.send("cat", "/etc/hosts");
		session.waitForFinish(12000L);
		String[] hosts = session.getStdOut().trim().split("\n");
		
		for(int i=9;i<hosts.length-1;i++){
			if(hosts[i].contains("controlzone")){
				controlzoneHost = hosts[i].split("  ")[1];
			}
			if(hosts[i].contains("ec_1")){
				 ec1Host = hosts[i].split("  ")[1];
			}
		}
		
		return controlzoneHost.equalsIgnoreCase(ec1Host);
	}

	public boolean createKpiPhaseOneGroups(Map<String, Object> args) {
		boolean success = true;
		String[] tables=new String[]{"dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw"};
		String[] columns=new String[]{"mcc","mnc"};
		String[] dataTypes=new String[]{"s","s"};
		if(!kpiNotificationGroupsDB(tables,columns,dataTypes,"dc.group_type_e_mcc_mnc","DG_GroupNameMCCMNC",1)){
			log.error("Failed to create MCCMNC group");
			success = false;
		}
		
		tables=new String[]{"dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw"};
		columns=new String[]{"event_source_name"};
		dataTypes=new String[]{"s"};
		if(!kpiNotificationGroupsDB(tables,columns,dataTypes,"dc.group_type_e_evntsrc","DG_GroupNameEVENTSRC",1)){
			log.error("Failed to create EVENTSRC group");
			success = false;
		}
		
		tables=new String[]{"dc.DIM_E_MSS_EVNTSRC"};
		columns=new String[]{"event_source_name","evntsrc_id"};
		dataTypes=new String[]{"s","n"};
		if(!kpiNotificationGroupsDB(tables,columns,dataTypes,"dc.GROUP_TYPE_E_EVNTSRC_CS","DG_GroupNameMSS",1)){
			log.error("Failed to create MSS group");
			success = false;
		}
		
		tables=new String[]{"dc.event_e_sgeh_err_raw","dc.event_e_lte_err_raw",
				"dc.EVENT_E_MSS_LOC_SERVICE_CDR_suc_RAW","EVENT_E_MSS_SMS_CDR_ERR_RAW","dc.EVENT_E_MSS_VOICE_CDR_ERR_RAW"};
		columns=new String[]{"imsi"};
		if(!gpmgtXmlStuff(tables,columns,"DG_GroupNameIMSI","IMSI")){
			log.error("Failed to create IMSI group");
			success = false;
		}
		
		return success;
	}

	private boolean gpmgtXmlStuff(String[] tables, String[] columns,
			String groupName, String groupType) {
		String commaSeparatedColumns = StringUtils.join(columns,',');
		String query;
		String[] uniqueRows = null;
		int groupNumber = 1;
		boolean success = false;
		
		for(String table : tables){
			query = "SELECT DISTINCT " + commaSeparatedColumns + " FROM " + table;
			uniqueRows = sqlSelect(DataHandler.getHostByName("controlzone"),query);
		}
		
		Session coordinator_session = Session.getSession(DataHandler.getHostByName("controlzone"));
		coordinator_session.send("rm /tmp/gpmgtfile.tmp");
		coordinator_session.waitForFinish(30000L);
		
		coordinator_session.send("echo \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\n<groupmgt>\" > /tmp/gpmgtfile.tmp");
		coordinator_session.waitForFinish(30000L);
		
		for(String row : uniqueRows){
			String[] values = row.split("\\s+");
			int i = 0;
			
			coordinator_session.send("echo \"\t<group name=\\\""+groupName+"_"+groupNumber+"\\\" " +
					"type=\\\""+groupType.toUpperCase()+"\\\">\n\t\t<group-element>\" >> /tmp/gpmgtfile.tmp");
			coordinator_session.waitForFinish(30000L);
			
			for(String value : values){
				coordinator_session.send("echo \"\t\t\t<key name=\\\"" + columns[i] + "\\\" value=\\\"" + value 
						+ "\\\"/>\" >> /tmp/gpmgtfile.tmp");
				i++;
			}
			
			groupNumber++;
			if(groupNumber == 11){
				groupNumber = 0;
			}
			
			coordinator_session.send("echo \"\t\t</group-element>\n\t</group>\" >> /tmp/gpmgtfile.tmp");
			coordinator_session.waitForFinish(30000L);
		}
		
		coordinator_session.send("echo \"</groupmgt>\" >> /tmp/gpmgtfile.tmp");
		coordinator_session.waitForFinish(30000L);
		
		coordinator_session.send("/eniq/sw/bin/gpmgt -i -add -f /tmp/gpmgtfile.tmp");
		coordinator_session.waitForFinish(180000L);
		success = coordinator_session.getExitCode() == 0;
		
		coordinator_session.send("rm /tmp/gpmgtfile.tmp");
		coordinator_session.waitForFinish(30000L);
		
		return success;
	}

	private boolean kpiNotificationGroupsDB(String[] tables, String[] columns,
			String[] dataTypes, String destTable, String groupName, int doGroupNum) {
		String commaSeparatedColumns = StringUtils.join(columns,',');
		String query;
		String[] uniqueRows = null;
		int groupNumber = 1;
		boolean success = true;
		
		for(String table : tables){
			query = "SELECT DISTINCT " + commaSeparatedColumns + " FROM " + table;
			uniqueRows = sqlSelect(DataHandler.getHostByName("controlzone"),query);
		}
		
		for(String row : uniqueRows){
			row = row.trim();
			String[] values = row.split("\\s+");
			String whereString = "";
			String valuesString = "";
			int i = 0;
			
			for(String value : values){
				if(!whereString.isEmpty()){
					whereString += " AND ";
				}
				if(!valuesString.isEmpty()){
					valuesString += ",";
				}
				if(dataTypes[i].matches("n")){
					whereString += columns[i] + " = " + value;
					valuesString += value;
				}else{
					whereString += columns[i] + " = \\'" + value + "\\'";
					valuesString += "\\'" + value + "\\'";
				}
				i++;
			}
			
			query = "SELECT " + commaSeparatedColumns + " FROM " + destTable + " WHERE " + whereString;
			String[] result = sqlSelect(DataHandler.getHostByName("controlzone"),query);
			query = "INSERT INTO " + destTable + " (" + commaSeparatedColumns + ", group_name) VALUES ("
					+ valuesString + ", \\'" + groupName;
			
			if(doGroupNum == 1){
				query += "_" + groupNumber + "\\')";
			}else{
				query += "\\')";
			}
			
			if(result == null || result.length == 0){
				log.info("Inserting Entry: " + query);
				if(!sqlInsert(query)){
					log.error("Insert Failed: " + query);
					success = false;
				}
				groupNumber++;
				if(groupNumber == 11){
					groupNumber = 0;
				}
			}else{
				log.info("Entry already exists, not inserting: " + query);
			}
		}
		
		return success;
	}

	public boolean createLteGroups(Map<String, Object> args) {
		String imsiGroup="<group name=\\\"LTE_Group\\\" type=\\\"IMSI\\\">";
		String rat3Group="<group name=\\\"LTE_Group\\\" type=\\\"RAT_VEND_HIER3\\\">";
		String rat321Group="<group name=\\\"LTE_Group\\\" type=\\\"RAT_VEND_HIER321\\\">";
		String tracGroup="<group name=\\\"LTETrackingAreaGroup\\\" type=\\\"LTE_TRAC\\\">";
		String resDataImsiLine="IMSI_LTE=var1;IMSI_GROUP_LTE=LTE_Group;IMSI_GROUP_DATA_LTE=var2;";
		String resDataController="CONTROLLER_LTE=var1;CONTROLLER_GROUP_LTE=LTE_Group;CONTROLLER_GROUP_DATA_LTE=var2;";
		String resDataAccessArea="ACCESS_AREA_LTE=var1;ACCESS_AREA_GROUP_LTE=LTE_Group;ACCESS_AREA_GROUP_DATA_LTE=var2;";
		String resDataTrackingArea="TRACKING_AREA_LTE=var1;TRACKING_AREA_GROUP_LTE=LTETrackingAreaGroup;TRACKING_AREA_GROUP_DATA_LTE=var2;";
		String firstImsi="";
		String imsiData="";
		String firstController="";
		String controllerData="";
		String firstAccessArea="";
		String accessAreaData="";
		String firstTrackingArea="";
		String trackingAreaData="";	
		
		int firstRun=1;
		String timeWarp = getDateWithArg(30);
		String[] versions = new String[]{"13B"};
		
		String query = "";
		String[] result;
		
		for(String version : versions){
			query = "select top 1 imsi,hier321_id from dc.EVENT_E_LTE_CFA_ERR_RAW where ne_version=\\'" + version +
				"\\' and ne_version!=null and imsi!=null and hier321_id!=null and datetime_id>\\'" + timeWarp + "\\'";
			log.info(query);
			result = sqlSelect(DataHandler.getHostByName("controlzone"),query);
			
			if(result.length > 0){
				log.info("SQL result:");
				for(String row : result){
					log.info(row);
				}
				
				String[] imsiAndCellId = result[0].split("\\s+");
				query = "select top 1 HIERARCHY_3,HIERARCHY_1 from DIM_E_LTE_HIER321 where hier321_id="+imsiAndCellId[1]
						+" and HIERARCHY_3!=null and HIERARCHY_1!=null";
				log.info(query);
				result = sqlSelect(DataHandler.getHostByName("controlzone"),query);
				if(result.length > 0){
					log.info("SQL result:");
					for(String row : result){
						log.info(row);
					}
					
					String[] controllerAndCellName = result[0].split("\\s+");
					if(imsiGroup.contains(imsiAndCellId[0])){
						imsiGroup += "<group-element><key name=\"IMSI\" value=\""+imsiAndCellId[0]+"\"/></group-element>";
						if(firstRun > 0){
							firstImsi = imsiAndCellId[0];
							imsiData = imsiAndCellId[0];
						}else{
							imsiData = "," + imsiAndCellId[0];
						}
					}
					
					if(rat321Group.contains(controllerAndCellName[0])){
						rat321Group += "<group-element><key value=\""+controllerAndCellName[0]+"\" name=\"HIERARCHY_3\"/>" +
								"<key value=\""+controllerAndCellName[1]+"\" name=\"HIERARCHY_1\"/><key value=\"2\" " +
										"name=\"RAT\"/><key value=\"Ericsson\" name=\"VENDOR\"/></group-element>";
						
						if(firstRun > 0){
							firstAccessArea = controllerAndCellName[1] + ",," +controllerAndCellName[0];
							accessAreaData = controllerAndCellName[1];
						}else{
							accessAreaData += "," + controllerAndCellName[1];
						}
					}
					firstRun = 0;
				}
			}
		}
		
		query = "select distinct trac from EVENT_E_LTE_CFA_ERR_RAW where trac!=null and datetime_id>\\'"+timeWarp+"\\'";
		log.info(query);
		result = sqlSelect(DataHandler.getHostByName("controlzone"),query);
		
		if(result.length > 0){
			log.info("SQL Result");
			
			for(String row : result){
				log.info(row);
				if(row.matches("^[0-9]+$")){
					if(firstTrackingArea.isEmpty()){
						firstTrackingArea = row;
						
					}
					trackingAreaData += row + ",";
					tracGroup += "<group-element><key value =\\\""+row+"\\\" name=\\\"trac\\\"/></group-element>";
				}
			}
		}
		
		trackingAreaData = trackingAreaData.replaceAll(",$", "");
		imsiGroup += "\n\t</group>";
		rat3Group += "\n\t</group>";
		rat321Group += "\n\t</group>";
		tracGroup += "\n\t</group>";
		
		resDataImsiLine = resDataImsiLine.replaceAll("var1",firstImsi);
		resDataImsiLine = resDataImsiLine.replaceAll("var2",imsiData);
		
		resDataController = resDataController.replaceAll("var1",firstController);
		resDataController = resDataController.replaceAll("var2",controllerData);
		
		resDataAccessArea = resDataAccessArea.replaceAll("var1", firstAccessArea);
		resDataAccessArea = resDataAccessArea.replaceAll("var2", accessAreaData);
		
		resDataTrackingArea = resDataTrackingArea.replaceAll("var1", firstTrackingArea);
		resDataTrackingArea = resDataTrackingArea.replaceAll("var2", trackingAreaData);
		
		boolean foundValues = true;
		
		if(imsiGroup.length() < 62){
			log.info("No IMSIs found for LTE IMSI group");
			foundValues = false;
		}
		
		if(rat3Group.length() < 62){
			log.info("No controller found for LTE controller group");
			foundValues = false;
		}
		
		if(rat321Group.length() < 62){
			log.info("No controller or cell found for LTE controller group");
			foundValues = false;
		}
		
		if(tracGroup.length() < 62){
			log.info("No trac values found for LTE tracking area group");
			foundValues = false;
		}
		
		if(foundValues){
			String reservedDataLocation="/eniq/home/dcuser/selenium/" +
					"selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
			Session coordinator = Session.getSession(DataHandler.getHostByName("controlzone"));
			coordinator.send("ls", reservedDataLocation);
			if(coordinator.getExitCode() != 0){
				coordinator.send("cat", reservedDataLocation);
				coordinator.waitForFinish(30000L);
				String[] contents = coordinator.getStdOut().split("\n");
				
				coordinator.send("echo \"\" \\>", "/tmp/temp_res_data.csv");
				coordinator.waitForFinish(30000L);
				
				for(String line : contents){
					line = line.replaceAll("IMSI_LTE.*$", resDataImsiLine);
					line = line.replaceAll("CONTROLLER_LTE.*$", resDataController);
					line = line.replaceAll("ACCESS_AREA_LTE.*$", resDataAccessArea);
					line = line.replaceAll("TRACKING_AREA_LTE.*$", resDataTrackingArea);
					line = line.replaceAll("VERSION_12A_SUPPORTED.*$", "VERSION_12A_SUPPORTED=no;VERSION_11B_SUPPORTED=no;VERSION_12B_SUPPORTED=no;VERSION_13A_SUPPORTED=no;VERSION_13B_SUPPORTED=yes;");
					coordinator.send("echo \""+line+"\" \\>", "/tmp/temp_res_data.csv");
					coordinator.waitForFinish(30000L);
				}
				coordinator.send("mv /tmp/temp_res_data.csv " + reservedDataLocation);
				coordinator.waitForFinish(30000L);
			}else{
				log.error("Could not find reserved data file at location: " + reservedDataLocation);
			}
			
			sqlDelete("delete from GROUP_TYPE_E_LTE_TRAC where group_name='LTETrackingAreaGroup'");
			sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER321 where group_name='LTE_Group'");
			sqlDelete("delete from GROUP_TYPE_E_RAT_VEND_HIER3 where group_name='LTE_Group'");
			sqlDelete("delete from GROUP_TYPE_E_IMSI where group_name='LTE_Group'");
			
			String groups="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<groupmgt>\n\t" + imsiGroup + "\n\t" 
			+ rat3Group + "\n\t" + rat321Group + "\n\t" + tracGroup + "\n</groupmgt>";
			coordinator.send("echo \""+groups+"\" \\>", "/tmp/gpmgtfile.tmp");
			coordinator.waitForFinish(30000L);
			log.info("Creating groups using XML:");
			log.info(groups);
			
			coordinator.send("/eniq/sw/bin/gpmgt -i -add -f /tmp/gpmgtfile.tmp");
			coordinator.waitForFinish(30000L);
			coordinator.send("rm /tmp/gpmgtfile.tmp");
			coordinator.waitForFinish(30000L);
		}
		
		return false;
	}
}