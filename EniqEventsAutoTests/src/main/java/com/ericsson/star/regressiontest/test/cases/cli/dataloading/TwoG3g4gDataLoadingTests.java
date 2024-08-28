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
import com.ericsson.star.regressiontest.test.data.cli.DataLoadingTestDataProvider;

public class TwoG3g4gDataLoadingTests extends TorTestCaseHelper implements TestCase{
	private CliToolOperator cli = new CliToolOperator();
	
	private String HOST = DataHandler.getHostByName("controlzone").getIp().split("\\.")[0];
	private String DATAGEN_HOST = DataHandler.getHostByName("datagen").getIp().split("\\.")[0];
	private String DATAGEN_NFS_PATH="/net/"+DATAGEN_HOST+"/tmp/CentralDatagen/";
	private String DIR_2G_3G = "/eniq/data/eventdata/events_oss_1/sgeh/dir5";
	private String DIR_4G = "/eniq/data/eventdata/events_oss_1/sgeh/dir1";
	private String[] tablesToCheck = {"EVENT_E_SGEH_RAW","EVENT_E_LTE_RAW"};
	private String[] dirsToCheck = {DIR_2G_3G,DIR_4G};
	private String[] columnToCheck = null;
	private String[] columnValue = null;
	private String[] localDirectories = null;
	private String[] remoteDirectories = null;
	private String[] unRequiredEC = new String[]{"EC_LTEES_1","EC_LTEES_2","EC_LTEES_3","EC_LTEES_4"};
	private String[] requiredEC = new String[]{"EC1", "EC_SGEH_1"};
	private static Logger log = Logger.getLogger(TwoG3g4gDataLoadingTests.class);
	
	@BeforeTest
	void setup(){
		cli.registerHostOnDatagen(DataHandler.getHostByName("controlzone"));
	}
	
	@Setup 
	void prepareTestCaseFor2g3gDataLoading(){
		columnToCheck = new String[]{"HIERARCHY_3","HIERARCHY_3"};
		columnValue = new String[]{null,null};
		localDirectories = new String[]{"/eniq/data/eventdata/events_oss_1/sgeh"};
		remoteDirectories = new String[]{DATAGEN_NFS_PATH + HOST + "/50files" + 
				"/eniq/data/eventdata/events_oss_1/sgeh"};
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
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
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"directoriesAndTablesAreLoadingInitially"}, alwaysRun = true)
	public void fourGandMSSGroupsCanBeCreated(Map<String,Map<String,Object>> args){
		setTestCase("fourGandMSSGroupsCanBeCreated",
				"Four G and MSS Groups can be created");
		 
		setTestStep("Create 4g and MSS Groups");
		assertTrue("Failed to create 4g and MSS groups",
				cli.create4GAndMSSGroup(false));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"fourGandMSSGroupsCanBeCreated"}, alwaysRun = true)
	public void unrequiredWorkflowsCanBeDisabled(Map<String,Map<String,Object>> args){
		setTestCase("unrequiredWorkflowsCanBeDisabled",
				"Unrequired workflows can be disabled");
		 
		setTestStep("Disable unwanted workflows");
		log.info("Disabling EBSL workflows");
		assertTrue("Could not Disable unwanted workflows",
				cli.disableMatchingWorkflows("EBSL"));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"unrequiredWorkflowsCanBeDisabled"}, alwaysRun = true)
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
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"unrequiredECsCanBeDisabled"}, alwaysRun = true)
	public void requiredECsCanBeEnabled(Map<String,Map<String,Object>> args){
		setTestCase("requiredECsCanBeEnabled",
				"Required ECs can be enabled");
		 
		setTestStep("Enable required ECs");
		log.info("Starting EC1 and EC_SGEH_1");
		assertTrue("Failed to enable wanted ECs",
				cli.enableRequiredECs(requiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredECsCanBeEnabled"}, alwaysRun = true)
	public void linksToDatagenCanBeCreated(Map<String,Map<String,Object>> args){
		setTestCase("linksToDatagenCanBeCreated",
				"Links to datagen can be created");
		 
		setTestStep("Link data directories to datagen directories");
		assertTrue("Failed to link data directories to datagen directories",
				cli.createDatagenLinks("ec_1",localDirectories, remoteDirectories));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"linksToDatagenCanBeCreated"}, alwaysRun = true)
	public void sgehWorkflowsCanBeProvisioned(Map<String,Map<String,Object>> args){
		setTestCase("sgehWorkflowsCanBeProvisioned",
				"Sgeh workflows can be provisioned");
		 
		setTestStep("Run Provision workflows script");
		assertTrue("Provision workflow execution failed",
				cli.provisionSgehWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"sgehWorkflowsCanBeProvisioned"}, alwaysRun = true)
	public void sgehTopologyCanBeRefreshed(Map<String,Map<String,Object>> args){
		setTestCase("sgehTopologyCanBeRefreshed",
				"Sgeh topology can be refreshed");
		 
		setTestStep("Refresh topology");
		assertTrue("Failed to refresh topology",
				cli.refreshSgehTopology());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"sgehTopologyCanBeRefreshed"}, alwaysRun = true)
	public void requiredWorkflowsAreEnabledAndRunning(Map<String,Map<String,Object>> args){
		setTestCase("requiredWorkflowsAreEnabledAndRunning",
				"Required workflows are enabled and running");
		 
		setTestStep("Enable 2g 3g workflows");
		assertTrue("Failed to enable 2g 3g workflows",
				cli.enable2g3gWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="twoGDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredWorkflowsAreEnabledAndRunning"}, alwaysRun = true)
	public void dataIsLoadingInDirectoriesAndTables(Map<String,Map<String,Object>> args){
		setTestCase("dataIsLoadingInDirectoriesAndTables",
				"Data is loading in directories and tables");
		
		setTestStep("Check Directories and Tables");
		assertTrue("Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheck,tablesToCheck,40,true,columnToCheck,columnValue));
	}
}
