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

public class KpiDataloadingTests extends TorTestCaseHelper implements TestCase {
	private String HOST = DataHandler.getHostByName("controlzone").getIp().split("\\.")[0];
	private String DATAGEN_HOST = DataHandler.getHostByName("datagen").getIp().split("\\.")[0];
	private String DATAGEN_NFS_PATH="/net/"+DATAGEN_HOST+"/tmp/CentralDatagen/";
	private String DIR_KPI = "/eniq/data/eventdata/events_oss_1/sgeh/dir3";
	private String[] localDirectories = null;
	private String[] remoteDirectories = null;
	private String[] tablesToCheck = {"EVENT_E_LTE_RAW"};
	private String[] dirsToCheck = {DIR_KPI};
	private String[] columnToCheck = null;
	private String[] columnValue = null;
	private String[] requiredEC = new String[]{"EC1", "EC_SGEH_1"};
	private static Logger log = Logger.getLogger(KpiDataloadingTests.class);
	
	private CliToolOperator cli = new CliToolOperator();
	
	@BeforeTest
	void setup(){
		cli.registerHostOnDatagen(DataHandler.getHostByName("controlzone"));
	}
	
	@Setup 
	void prepareTestCaseForKpiNotificationDataLoading(){
		columnToCheck = new String[]{"ne_version"};
		columnValue = new String[]{"4,13"};
		localDirectories = new String[]{"/eniq/data/eventdata/events_oss_1/sgeh"};
		remoteDirectories = new String[]{DATAGEN_NFS_PATH + HOST + "/50files" + 
				"/eniq/data/eventdata/events_oss_1/sgeh"};
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="kpiNotificationDataCanBeloadedOnEniqServer", 
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
	@Test (groups = {"acceptance"}, dataProvider="kpiNotificationDataCanBeloadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"directoriesAndTablesAreLoadingInitially"}, alwaysRun = true)
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
	@Test (groups = {"acceptance"}, dataProvider="kpiNotificationDataCanBeloadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredECsCanBeEnabled"}, alwaysRun = true)
	public void sgehWorkflowsCanBeProvisioned(Map<String,Map<String,Object>> args){
		setTestCase("sgehWorkflowsCanBeProvisioned",
				"Sgeh workflows can be provisioned");
		 
		setTestStep("Run Provision workflows script");
		assertTrue("Provision workflow execution failed",
				cli.provisionSgehWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="kpiNotificationDataCanBeloadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"sgehWorkflowsCanBeProvisioned"}, alwaysRun = true)
	public void requiredWorkflowsAreEnabledAndRunning(Map<String,Map<String,Object>> args){
		setTestCase("requiredWorkflowsAreEnabledAndRunning",
				"Required workflows are enabled and running");
		 
		setTestStep("Enable Kpi Notification workflows");
		assertTrue("Failed to enable Kpi Notification workflows",
				cli.enableKpiNotificationWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="kpiNotificationDataCanBeloadedOnEniqServer", 
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
