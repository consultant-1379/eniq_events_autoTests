package com.ericsson.star.regressiontest.operators.cli.datagen;


import java.util.Map;
import com.ericsson.cifwk.taf.CliOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.v2.Session;

public class DataGenCliOperator implements CliOperator {

	private Session session;
	private static String ARGS = "args";
	private static String TIMEOUT = "timeout";
	private static String HOST = "host";
	private String timeOut = "300000";
	//private String reservedDataLocation="/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/resources/reservedData.csv";
	private Session getSession(Host host) {
		session = Session.getSession(host);
		return session;
	}
	
	
	public String sqlSelect(String sqlstatement, Map<String,Object> args){
		String results;
		session = getSession((Host) args.get(HOST));
		session.send("echo $\'"+sqlstatement+"\ngo\' >/eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -h0 -Ddwhdb -Sdwhdb -w 50 -b -i /eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		results = session.getStdOut();
		System.out.println("SQL Results is "+results);
		session.send("rm /eniq/home/dcuser/automation/SQLselect.sql", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		return results;
	}
	
	public boolean verifyDataGenDir(String dir, Map<String,Object> args){
		String results;
		long dataGenStartTime = System.currentTimeMillis()/1000L-40*60;
		session = getSession((Host) args.get(HOST));
		session.send("echo $\'#!/usr/bin/perl\nmy $var=(stat(\""+dir+"\"))[9];\nprint $var;\' >/tmp/cmdfile.pl", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("chmod 755 /tmp/cmdfile.pl", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("scp /tmp/cmdfile.pl dcuser\\@ec_1:/tmp", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("ssh dcuser\\@ec_1 'perl /tmp/cmdfile.pl'","");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		results = session.getStdOut();
		System.out.println("Time Results is "+results);
		return Long.parseLong(results)>dataGenStartTime;
	}
	
	public boolean enableWorkflow(String workflowName, Map<String,Object> args){
		boolean results = false;
		session = getSession((Host) args.get(HOST));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable "+workflowName, "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
	    if(session.getExitCode() == 0){
	    	results = true;
	    }
		return results;
	}
    
	
	public boolean enableWorkflowGroup(String workflowGroupName, Map<String,Object> args){
		boolean results = false;
		session = getSession((Host) args.get(HOST));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable "+workflowGroupName, "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		if(session.getExitCode() == 0){
	    	results = true;
	    }
		return results;
	}
	
	public boolean disableWorkflow(String workflowName, Map<String,Object> args){
		boolean results = false;
		session = getSession((Host) args.get(HOST));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable "+workflowName, "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		if(session.getExitCode() == 0){
	    	results = true;
	    }
		return results;
	}
    
	
	public boolean disableWorkflowGroup(String workflowGroupName, Map<String,Object> args){
		boolean results = false;
		session = getSession((Host) args.get(HOST));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable "+workflowGroupName, "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		if(session.getExitCode() == 0){
	    	results = true;
	    }
		return results;
	}
	
	public boolean disableUnrelatedWorkflows(String relatedWorkflowName, Map<String,Object> args){
		boolean results = true;
		session = getSession((Host) args.get(HOST));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wflist","");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		String wflist = session.getStdOut();
		//String[] wfNameList = null;
		//System.out.println("WF LIST IS "+wflist.split("\n")[0]);
		for(int i=1;i<wflist.split("\n").length;i++){
			//wflist.split("\n")[i].split(" ")[0];
			//System.out.println("wf name is "+wflist.split("\n")[i].split(" ")[0]);
			if(!wflist.split("\n")[i].split(" ")[0].equalsIgnoreCase(relatedWorkflowName)&&
					(wflist.split("\n")[i].split(" ")[0].contains("EBSL")||wflist.split("\n")[i].split(" ")[0].contains("GPEH")||wflist.split("\n")[i].split(" ")[0].contains("MSS")||wflist.split("\n")[i].split(" ")[0].contains("SGEH")||wflist.split("\n")[i].split(" ")[0].contains("LTEEFA")||wflist.split("\n")[i].split(" ")[0].contains("Sim")||wflist.split("\n")[i].split(" ")[0].contains("eankmuk_4G"))){
				//System.out.println("wf name is "+wflist.split("\n")[i].split(" ")[0]);
				results=disableWorkflow(wflist.split("\n")[i].split(" ")[0],args);
			} 
			}

		return results;
	}
	
	public boolean setup2g3g4gWorkflows(boolean remoteDataGen,boolean force,Map<String,Object> args){
		session = getSession((Host) args.get(HOST));
		
		int totalDirs=6;
		String result;
		String host =((Host)args.get(HOST)).getIp().substring(0, 10);
		System.out.println("Host is " + host);
		session.send("ssh dcuser\\@ec_1 'ls -ld /eniq/data/eventdata/events_oss_1/sgeh | grep ^l'","");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		int linkExists = session.getExitCode();
	    session.send("ssh dcuser\\@ec_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1'", "");
	    session.waitForFinish((Long) Long.valueOf(timeOut));
	    int success = session.getExitCode();
	    if((remoteDataGen && linkExists!=0) || (!remoteDataGen && (linkExists==0 || success!=0)) || force){
	    	if(remoteDataGen){
	    		session.send("ssh dcuser\\@ec_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh/dir1 /eniq/data/eventdata/events_oss_1/sgeh/dir5 /eniq/data/eventdata/events_oss_1/sgeh/dir3 /eniq/data/eventdata/events_oss_1/sgeh/dir4'", "");
	    		session.waitForFinish((Long) Long.valueOf(timeOut));
	    		session.send("ssh dcuser\\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1'","");
	    		session.waitForFinish((Long) Long.valueOf(timeOut));
	    		session.send("ssh dcuser\\@ec_1 'ln -s /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/eventdata/events_oss_1/sgeh /eniq/data/eventdata/events_oss_1/'", "");
	    		session.waitForFinish((Long) Long.valueOf(timeOut));
	    	    result = session.getStdOut();
	    	    if(!(result.contains("Exception")||result.contains("Fail")||result.contains("Error"))){
	    	       System.out.println("INFO:Symbolic link created to /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/eventdata/events_oss_1/sgeh at /eniq/data/eventdata/events_oss_1/sgeh on ec_1");
	    	    }else{
	    	    	System.out.println("ERROR:Failed to create symbolic link created to /net/atclvm559.athtem.eei.ericsson.se/tmp/CentralDatagen/"+host+"/50files/eniq/data/eventdata/events_oss_1/sgeh at /eniq/data/eventdata/events_oss_1/sgeh on ec_1");
	    	    }
           }else{
	        	 if(linkExists==0){
	        		session.send("ssh dcuser\\@ec_1 'rm -rf /eniq/data/eventdata/events_oss_1/sgeh'", "");
	        		session.waitForFinish((Long) Long.valueOf(timeOut));
	        	 }
	        	 session.send("ssh dcuser\\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh'", "");
	        	 session.waitForFinish((Long) Long.valueOf(timeOut));
	        	 for(int i=1;i<totalDirs;i++){
	        		 String dirNo = String.valueOf(i);
	        		 session.send("ssh dcuser\\@ec_1 'mkdir -p /eniq/data/eventdata/events_oss_1/sgeh/dir"+dirNo+"'", "");
	        		 session.waitForFinish((Long) Long.valueOf(timeOut));
	        	 }
	    }
	    }else{
	    	System.out.println("INFO:/eniq/data/eventdata/events_oss_1/sgeh already exists on ec_1. Put dataGenStart_2G3G4G force in a config file to force re-creation");
	    }
	    
	   String dirExists;
	   session.send("ssh dcuser\\@ec_1 'ls /eniq/data/eventdata/events_oss_1/sgeh/dir1'","");
	   session.waitForFinish((Long) Long.valueOf(timeOut));
	   dirExists=session.getStdOut();
	   if(dirExists.contains("No such file or directory")){
		   System.out.println("ERROR:/eniq/data/eventdata/events_oss_1/sgeh/dir1 doesn't exist on ec_1");
	   }
	   
	   boolean allWorkflowAdded=true;
	   String provisionWorkflowsResults;
	   session.send("ssh dcuser\\@ec_1 'source ~/.profile;cd /eniq/mediation_inter/M_E_SGEH/bin;./provision_workflows.sh'", "");
	   session.waitForFinish(120000000000000L);
	   provisionWorkflowsResults=session.getStdOut();
	   System.out.println("Provision Workflows result: "+provisionWorkflowsResults);
	   
	   if(session.getExitCode()!=0 || provisionWorkflowsResults.contains("No OSS information found. Workflow provisioning will not continue.")){
		 allWorkflowAdded = false;  
	   }
	   
	   if(allWorkflowAdded){
		   System.out.println("Set up 2G3G4G workflows has been done by invoking provision_workflows.sh");
	   }else{
		   System.out.println("Failed to provision 2G3G4G workflows even by invoking provision_workflows.sh"); 
	   }
	   
	   return allWorkflowAdded;
	}
	
	public void create4GAndMSSGroup(boolean force,Map<String,Object> args){
		//boolean result;
		session = getSession((Host) args.get(HOST));
		session.send("ls /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		if(session.getExitCode()!=0 || force){
		   session.send("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G.zip -d /eniq/home/dcuser/automation/DataGenTopology", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Unzip DataGenTopology4G.zip");
		   }else{
			   System.out.println("Failed to Unzip DataGenTopology4G.zip");
		   }
			
		   session.send("unzip -o /eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS.zip -d /eniq/home/dcuser/automation/DataGenTopology", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Unzip DataGenTopologyMSS.zip");
		   }else{
			   System.out.println("Failed to Unzip DataGenTopologyMSS.zip");
		   }
			
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/predropDG.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/createDG2.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Create 2G 3G SGEH & CDR Data Generation Tables");
		   }else{
			   System.out.println("Failed to Create 2G 3G SGEH & CDR Data Generation Tables");
		   }
			
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDG.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Load 2g3g4g Data Generation Topology");
		   }else{
			   System.out.println("Failed to Load 2g3g4g Data Generation Topology");
		   }
		   
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/updateDG.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Update 2g3g4g DataGen Tables");
		   }else{
			   System.out.println("Failed to Update 2g3g4g DataGen Tables");
		   }
		   
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/deleteDGGroup.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Delete 2g3g4g Data Generation Groups");
		   }else{
			   System.out.println("Failed to Delete 2g3g4g Data Generation Groups");
		   }
		   
		   session.send("/eniq/sybase_iq/IQ-15_2/bin64/iqisql -Udc -Pdc -Sdwhdb -i/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/deleteDGGroup.sql", "");	
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
			System.out.println("Delete MSS Data Generation Groups");
		   }else{
			   System.out.println("Failed to Delete MSS Data Generation Groups");
		   }
		   
		   session.send("chmod 755 ", "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   session.send("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/loadDGGroups.sh","");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
				System.out.println("Load 2g3g4g DataGen Groups");
			   }else{
				   System.out.println("Failed to Load 2g3g4g DataGen Groups");
			   }
		
		   session.send("chmod 755 ", "/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   session.send("/eniq/home/dcuser/automation/DataGenTopology/DataGenTopologyMSS/loadDGGroups.sh","");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		   if(session.getExitCode()==0){
				System.out.println("Load MSS DataGen Groups");
			   }else{
				   System.out.println("Load MSS DataGen Groups");
			   }
		   
		   session.send("echo $\'done\' >/eniq/home/dcuser/automation/DataGenTopology/DataGenTopology4G/.loadingdone", "");
		   session.waitForFinish((Long) Long.valueOf(timeOut));
		}else if(!force){
			System.out.println("INFO:Skipping creating 4G and MSS groups because it's been done.");
		}
	}
	
	public boolean isMultiBladeServer(Map<String,Object> args){
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
	
	public void updateMssPreprocessingWorkflow(String mss3Dir, String mss4Dir, Map<String,Object> args){
		session = getSession((Host) args.get(HOST));
		session.send("rm /tmp/mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("rm ", "/tmp/msseditedworkflows.csv");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfexport MSS.WF01_PreProcessing /tmp/mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		String status = session.getStdOut();
		session.send("ls /tmp/ | grep mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		int executionResult = session.getExitCode();
		if(status.contains("Export finished") && executionResult == 0){
			System.out.println("INFO:MSS.WF01_PreProcessing workflow exported successfully");
		}else{
			System.out.println("ERROR:MSS.WF01_PreProcessing workflow did not export");
		}
		session.send("cat /tmp/mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		String[] workflows = session.getStdOut().split("\n");
		int updatedWorkflows=0;
		for(int i=1;i<workflows.length;i++){
			if(workflows[i].contains("MSS_3")){
				workflows[i].replaceAll("/.*\"", mss3Dir+"\"");
				updatedWorkflows++;
			}
			if(workflows[i].contains("MSS_4")){
				workflows[i].replaceAll("/.*\"", mss4Dir+"\"");
				updatedWorkflows++;
			}
			
		}
		

		if(updatedWorkflows<2){
           System.out.println("ERROR:Did not find MSS_3 and MSS_4 in MSS.WF01_PreProcessing workflow:");
           for(int i=1;i<workflows.length;i++){
        	   System.out.println(workflows[i]);
           }
		}
		
		for(int i=0;i<workflows.length;i++){
			System.out.println("Contents are written into /tmp/msseditedworkflows.csv: "+workflows[i]);
			session.send("echo $\'"+workflows[i]+"\n\' >>/tmp/msseditedworkflows.csv", "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
		}
		
		session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfimport MSS.WF01_PreProcessing /tmp/msseditedworkflows.csv","");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		System.out.println("Import results: "+session.getStdOut());
		session.send("rm /tmp/mssworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		session.send("rm /tmp/msseditedworkflows.csv", "");
		session.waitForFinish((Long) Long.valueOf(timeOut));
		
	}
	
	public void restartWorkflows(String[] wfToRestart,String[] wfGroupsToRestart,Map<String,Object> args){
		//String status = null;
		boolean stillActive = true;
		int stopTimeout=0;
		session = getSession((Host) args.get(HOST));
		
		while(stillActive&&stopTimeout<120){
			stillActive=false;
			for(String workflow : wfToRestart){
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfstop "+workflow, "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				if(!session.getStdOut().contains("Configuration not activated")){
					stillActive=true;
				}
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfdisable "+workflow, "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				if(!session.getStdOut().contains("Already")){
					stillActive=true;
				}
			}
			for(String workflowGP : wfGroupsToRestart){
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstop "+workflowGP, "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				if(!session.getStdOut().contains("Configuration not activated")){
					stillActive=true;
				}
				session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupdisable "+workflowGP, "");
				session.waitForFinish((Long) Long.valueOf(timeOut));
				if(!session.getStdOut().contains("Already")){
					stillActive=true;
				}
			}
			if(stillActive){
				System.out.println("INFO:Waiting 30 seconds to confirm that workflows have stopped");
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("INFO:Workflows stopped and disabled");
			}
			
			stopTimeout++;
		}
		
		for(String wf : wfToRestart){
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfenable "+wf, "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
		}
		for(String wfGroup : wfGroupsToRestart){
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupenable "+wfGroup, "");
			session.waitForFinish((Long) Long.valueOf(timeOut));
			session.send("/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgroupstart "+wfGroup,"");
			session.waitForFinish((Long) Long.valueOf(timeOut));
		}
	}

}

