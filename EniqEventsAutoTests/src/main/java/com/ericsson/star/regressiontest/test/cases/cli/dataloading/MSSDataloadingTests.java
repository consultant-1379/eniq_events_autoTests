package com.ericsson.star.regressiontest.test.cases.cli.dataloading;

import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import se.ericsson.jcat.fw.annotations.Setup;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.cli.CliToolTestDataProvider;
import com.ericsson.star.regressiontest.test.data.cli.DataLoadingTestDataProvider;


public class MSSDataloadingTests extends TorTestCaseHelper implements TestCase{
	private String HOST = DataHandler.getHostByName("controlzone").getIp().split("\\.")[0];
	private String DATAGEN_HOST = DataHandler.getHostByName("datagen").getIp().split("\\.")[0];
	private String DATAGEN_NFS_PATH="/net/"+DATAGEN_HOST+"/tmp/CentralDatagen/";
	private String[] unRequiredEC = new String[]{"EC_LTEES_1","EC_LTEES_2","EC_LTEES_3","EC_LTEES_4"};
	private String[] requiredEC = new String[]{"EC1"};
	private String[] dirsToCheck = {};
	private String[] tablesToCheck = {"EVENT_E_MSS_VOICE_CDR_RAW","EVENT_E_MSS_SMS_CDR_RAW"};
	private String[] columnToCheck = null;
	private String[] columnValue = null;
	private String[] wfToRestart = new String[]{
			"MSS.WF01_PreProcessing.MSS_3",
			"MSS.WF01_PreProcessing.MSS_4"};
	private String[] wfGroupsToRestart = new String[]{"MSS.WG01_PreProcessing"};
	private static Logger log = Logger.getLogger(MSSDataloadingTests.class);
	
	private CliToolOperator cli = new CliToolOperator();
	
	@BeforeTest
	void setup(){
		cli.registerHostOnDatagen(DataHandler.getHostByName("controlzone"));
	}
	
	@Setup 
	void prepareTestCaseFor2g3gDataLoading(){
		columnToCheck = new String[]{"HIER3_ID","HIER3_ID"};
		columnValue = new String[]{null,null};
		dirsToCheck = new String[]{DATAGEN_NFS_PATH + HOST + "/50files" + "/eniq/data/pushData/04/mss/MSS_3",
				DATAGEN_NFS_PATH + HOST + "/50files" + "/eniq/data/pushData/03/mss/MSS_4"};
	}
	
	/**
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="DEFTFC-615_Func_1", dataProviderClass=CliToolTestDataProvider.class)
	public void MSSDataGenTest(Map<String,Map<String, Object>> args){
		setTestcase("DEFTFC-615_Func_1","KPI DataGen Tests");
		setTestStep("Load data for MSS");
		assertTrue(cli.loadMSSData(remoteDG, force, args.get("DEFTFC-615_Func_1_Step1")));
	}
	*/
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class)
	public void directoriesAndTablesAreLoadingInitially(Map<String,Map<String,Object>> args){
		setTestCase("directoriesAndTablesAreLoadingInitially",
				"Directories And Tables Are Loading Initially");
		 
		setTestStep("Check Directories and Tables");
		assertTrue("Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheck,tablesToCheck,10,false,columnToCheck,columnValue));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"directoriesAndTablesAreLoadingInitially"}, alwaysRun = true)
	public void updateReservedDataRanges(Map<String,Map<String,Object>> args){
		setTestCase("updateReservedDataRanges",
				"Update reserved data ranges");
		
		setTestStep("Update reserved data time ranges");
		assertTrue("Could not update reserved data time ranges",
				cli.updateReservedDataTimeRanges());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"updateReservedDataRanges"}, alwaysRun = true)
	public void fourGandMSSGroupsCanBeCreated(Map<String,Map<String,Object>> args){
		setTestCase("fourGandMSSGroupsCanBeCreated",
				"Four G and MSS Groups can be created");
		 
		setTestStep("Create 4g and MSS Groups");
		assertTrue("Failed to create 4g and MSS groups",
				cli.create4GAndMSSGroup(false));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"fourGandMSSGroupsCanBeCreated"}, alwaysRun = true)
	public void unrequiredECsCanBeDisabled(Map<String,Map<String,Object>> args){
		setTestCase("unrequiredECsCanBeDisabled",
				"Unrequired ECs can be disabled");
		 
		setTestStep("Disable unrequired ECs");
		log.info("shutting down EC_LTEES_1 EC_LTEES_2 EC_LTEES_3 EC_LTEES_4");
		assertTrue("Failed to disable unrequired ECs",
				cli.disableUnRequiredECs(unRequiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"unrequiredECsCanBeDisabled"}, alwaysRun = true)
	public void requiredECsCanBeEnabled(Map<String,Map<String,Object>> args){
		setTestCase("requiredECsCanBeEnabled",
				"Required ECs can be enabled");
		 
		setTestStep("Enable required ECs");
		log.info("Starting EC1");
		assertTrue("Failed to enable wanted ECs",
				cli.enableRequiredECs(requiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredECsCanBeEnabled"}, alwaysRun = true)
	public void mssWorkflowsCanBeProvisioned(Map<String,Map<String,Object>> args){
		setTestCase("mssWorkflowsCanBeProvisioned",
				"MSS workflows can be provisioned");
		 
		setTestStep("Run Provision workflows script");
		assertTrue("Provision workflow execution failed",
				cli.provisionMssWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"mssWorkflowsCanBeProvisioned"}, alwaysRun = true)
	public void updateMssPreprocessingWorkflows(Map<String,Map<String,Object>> args){
		setTestCase("updateMssPreprocessingWorkflows",
				"Update MSS preprocessing workflows");
		 
		setTestStep("Refresh topology");
		assertTrue("Failed to refresh topology",
				cli.updateMssPreprocessingWorkflows(dirsToCheck));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"updateMssPreprocessingWorkflows"}, alwaysRun = true)
	public void requiredWorkflowsAreEnabledAndRunning(Map<String,Map<String,Object>> args){
		setTestCase("requiredWorkflowsAreEnabledAndRunning",
				"Required workflows are enabled and running");
		 
		setTestStep("Restart MSS workflows and groups");
		assertTrue("Failed to restart MSS workflows and groups",
				cli.restartWorkflows(wfToRestart,wfGroupsToRestart));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredWorkflowsAreEnabledAndRunning"}, alwaysRun = true)
	public void mssTopologyCanBeRefreshed(Map<String,Map<String,Object>> args){
		setTestCase("mssTopologyCanBeRefreshed",
				"MSS topology can be refreshed");
		 
		setTestStep("Refresh topology");
		assertTrue("Failed to refresh topology",
				cli.refreshMssTopology());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="mssDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"mssTopologyCanBeRefreshed"}, alwaysRun = true)
	public void dataIsLoadingInDirectoriesAndTables(Map<String,Map<String,Object>> args){
		setTestCase("dataIsLoadingInDirectoriesAndTables",
				"Data is loading in directories and tables");
		
		setTestStep("Check Directories and Tables");
		assertTrue("Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheck,tablesToCheck,40,true,columnToCheck,columnValue));
	}
}
