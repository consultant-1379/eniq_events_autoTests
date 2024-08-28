package com.ericsson.star.regressiontest.test.cases.cli.dataloading;

import java.util.Map;

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

public class LteDataLoadingTests  extends TorTestCaseHelper implements TestCase{
	private CliToolOperator cli = new CliToolOperator();
	
	private String HOST = DataHandler.getHostByName("controlzone").getIp().split("\\.")[0];
	private String DATAGEN_HOST=DataHandler.getHostByName("datagen").getIp().split("\\.")[0];
	private String DATAGEN_NFS_PATH="/net/"+DATAGEN_HOST+"/tmp/CentralDatagen/";
	private String DIR_CTUM = "/eniq/data/pmdata/eventdata/00/CTRS/ctum/5min";
	private String DIR_TRACE = "/eniq/data/pmdata/eventdata/00/CTRS/lte_cfa/5min/dir1";
	private String[] tablesToCheck = {"EVENT_E_LTE_CFA_ERR_RAW","EVENT_E_LTE_HFA_ERR_RAW"};
	private String[] dirsToCheck = {DIR_CTUM,DIR_TRACE};
	private String[] columnToCheck = null;
	private String[] columnValue = null;
	private String[] localDirectories = null;
	private String[] remoteDirectories = null;
	private String[] unRequiredEC = null;
	private String[] requiredEC = null;
	
	@BeforeTest
	void setup(){
		cli.registerHostOnDatagen(DataHandler.getHostByName("controlzone"));
	}
	
	@Setup 
	void prepareTestCaseFor2g3gDataLoading(){
		columnToCheck = new String[]{"HIER3_ID","HIER3_ID"};
		columnValue = new String[]{null,null};
		localDirectories = new String[]{"/eniq/data/pmdata/eventdata/00/CTRS"};
		remoteDirectories = new String[]{DATAGEN_NFS_PATH + HOST + "/50files" +
				"/eniq/data/pmdata/eventdata/00/CTRS"};
		unRequiredEC = new String[]{"EC_LTEES_1","EC_LTEES_2","EC_LTEES_3","EC_LTEES_4"};
		requiredEC = new String[]{"EC1", "EC_LTEEFA_1", "EC_LTEEFA_2", "EC_LTEEFA_3"};
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class)
	public void lteDataIsLoadingInitially(Map<String,Map<String,Object>> args){
		setTestCase("lteDataIsLoadingInitially",
				"LTE Data is currently loading on eniq server");
		
		setTestStep("Check Directories and Tables");
		assertTrue("Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheck,tablesToCheck,10,false,columnToCheck,columnValue));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"lteDataIsLoadingInitially"}, alwaysRun = true)
	public void updateReservedDataRanges(Map<String,Map<String,Object>> args){
		setTestCase("updateReservedDataRanges",
				"Update reserved data ranges");
		
		setTestStep("Update reserved data time ranges");
		assertTrue("Could not update reserved data time ranges",
				cli.updateReservedDataTimeRanges());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"updateReservedDataRanges"}, alwaysRun = true)
	public void ebslWorkflowsAreDisabled(Map<String,Map<String,Object>> args){
		setTestCase("ebslWorkflowsAreDisabled",
				"EBSL workflows are disabled");
		
		setTestStep("Disable unwanted EBSL workflows");
		assertTrue("Could not Disable unwanted EBSL workflows",
				cli.disableMatchingWorkflows("EBSL"));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"ebslWorkflowsAreDisabled"}, alwaysRun = true)
	public void unRequiredWorkflowsCanBeDisabledOnSingleBlades(Map<String,Map<String,Object>> args){
		setTestCase("unRequiredWorkflowsCanBeDisabledOnSingleBlades",
				"Unrequired workflows can be disabled on single blades");
		
		setTestStep("Disable unwanted workflows (for single blades)");
		assertTrue("Could not Disable unwanted workflows",
				cli.disableAllWorkflows(new String[]{"LTEEFA","LCHS"}));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"unRequiredWorkflowsCanBeDisabledOnSingleBlades"}, alwaysRun = true)
	public void unRequiredECsCanBeDisabled(Map<String,Map<String,Object>> args){
		setTestCase("unRequiredECsCanBeDisabled",
				"Unrequired ECs can be disabled");
		
		setTestStep("Disable unrequired ECs");
		assertTrue("Failed to disable unrequired ECs",
				cli.disableUnRequiredECs(unRequiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"unRequiredECsCanBeDisabled"}, alwaysRun = true)
	public void requiredECsCanBeEnabled(Map<String,Map<String,Object>> args){
		setTestCase("requiredECsCanBeEnabled",
				"Required ECs can be enabled");
		
		setTestStep("Enable required ECs");
		assertTrue("Failed to enable wanted ECs",
				cli.enableRequiredECs(requiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredECsCanBeEnabled"}, alwaysRun = true)
	public void linksToDatagenDirectoriesCanBeCreated(Map<String,Map<String,Object>> args){
		setTestCase("linksToDatagenDirectoriesCanBeCreated",
				"Links to datagen directories can be created");
		
		setTestStep("Link data directories to datagen directories");
		assertTrue("Failed to link data directories to datagen directories",
				cli.createDatagenLinks("ec_1",localDirectories, remoteDirectories));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"requiredECsCanBeEnabled"}, alwaysRun = true)
	public void lteefaTopologyCanBeRefreshed(Map<String,Map<String,Object>> args){
		setTestCase("lteefaTopologyCanBeRefreshed",
				"LTEEFA topology can be refreshed");
		
		setTestStep("Refresh M_E_LTEEFA topology");
		assertTrue("Failed to refresh M_E_LTEEFA topology",
				cli.refreshLteefaTopology());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"lteefaTopologyCanBeRefreshed"}, alwaysRun = true)
	public void lteefaWorkflowsCanBeProvisioned(Map<String,Map<String,Object>> args){
		setTestCase("lteefaWorkflowsCanBeProvisioned",
				"Lteefa workflows can be provisioned");
		
		setTestStep("Provision Lteefa workflows");
		assertTrue("Failed to provision Lteefa workflows",
				cli.provisionLteefaWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="lteDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"lteefaWorkflowsCanBeProvisioned"}, alwaysRun = true)
	public void dataIsLoadingInDirectoriesAndTables(Map<String,Map<String,Object>> args){
		setTestCase("dataIsLoadingInDirectoriesAndTables",
				"Data is loading in directories and tables");
		
		setTestStep("Check Directories and Tables");
		assertTrue("Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheck,tablesToCheck,30,true,columnToCheck,columnValue));
	}
}
