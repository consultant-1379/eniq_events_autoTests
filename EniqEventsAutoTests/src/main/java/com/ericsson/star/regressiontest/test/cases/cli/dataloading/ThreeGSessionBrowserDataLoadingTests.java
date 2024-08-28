package com.ericsson.star.regressiontest.test.cases.cli.dataloading;

import java.util.Map;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import se.ericsson.jcat.fw.annotations.Setup;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.cli.DataLoadingTestDataProvider;

public class ThreeGSessionBrowserDataLoadingTests extends TorTestCaseHelper implements TestCase{
	private CliToolOperator cli = new CliToolOperator();
	
	private String CONTROLZONE_HOST;
	private String DATAGEN_NFS_PATH="/net/atclvm559/tmp/CentralDatagen/";
	private String DIR_SGEH = "/eniq/data/eventdata/events_oss_1/sgeh/dir2";
	private String REMOTE_DIR_SGEH;
	private String[] tablesToCheckCep;
	private String[] tablesToCheckSgeh = null;
	private String[] dirsToCheckCep = null;
	private String[] dirsToCheckSgeh = null;
	private String[] columnToCheckSgeh = null;
	private String[] columnToCheckGpeh = null;
	private String[] columnValueSgeh = null;
	private String[] unRequiredEC = null;
	private String[] requiredEC = null;
	
	@BeforeSuite
	void setup(){
		CONTROLZONE_HOST = DataHandler.getHostByName("controlzone").getIp().split("\\.")[0];
		REMOTE_DIR_SGEH = DATAGEN_NFS_PATH + CONTROLZONE_HOST + "/50files" + 
				"/ossrc/data/pmMediation/eventData/sgeh/dir1";
		cli.registerHostOnDatagen(DataHandler.getHostByName("controlzone"));
	}
	
	@Setup 
	void prepareTestCaseFor2g3gDataLoading(){
		dirsToCheckSgeh = new String[]{"/eniq/data/eventdata/events_oss_1/sgeh/dir2"};
		dirsToCheckCep = new String[]{
				"/ossrc/data/pmMediation/eventData/GpehEvents_CEP/SubNetwork=RNC09/MeContext=RNC09",
				"/ossrc/data/pmMediation/eventData/SgehEvents_CEP/ManagedElement=SGSN09",
				"/ossrc/data/pmMediation/eventData/PCPEvents_CEP/PCP1/staple/3G/tcpta_partial/454_06_8900",
				"/ossrc/data/pmMediation/eventData/PCPEvents_CEP/PCP1/captool/3G/454_06_8900"};
		tablesToCheckCep = new String[]{"EVENT_E_RAN_HFA_SOHO_ERR_RAW",
				"EVENT_E_RAN_HFA_IFHO_ERR_RAW","EVENT_E_RAN_HFA_IRAT_ERR_RAW","EVENT_E_RAN_HFA_HSDSCH_ERR_RAW",
				"EVENT_E_RAN_CFA_ERR_RAW","EVENT_E_RAN_SESSION_RAW","EVENT_E_CORE_SESSION_RAW",
				"EVENT_E_RAN_SESSION_CELL_VISITED_RAW","EVENT_E_RAN_SESSION_INTER_OUT_HHO_RAW",
				"EVENT_E_RAN_SESSION_INTER_SYS_UTIL_RAW","EVENT_E_RAN_SESSION_RRC_MEAS_RAW",
				"EVENT_E_RAN_SESSION_SUC_HSDSCH_CELL_CHANGE_RAW","EVENT_E_USER_PLANE_TCP_RAW",
				"EVENT_E_USER_PLANE_CLASSIFICATION_RAW"};
		tablesToCheckSgeh = new String[]{"EVENT_E_SGEH_ERR_RAW"};
		columnToCheckSgeh = new String[]{"hierarchy_3"};
		columnToCheckGpeh = new String[]{"imsi","imsi","imsi","imsi","imsi","imsi","imsi",
				"imsi","imsi","imsi","imsi","imsi","imsi","imsi"};
		columnValueSgeh = new String[]{"smartone_R:RNC09:RNC09"};
		unRequiredEC = new String[]{"EC_LTEES_1","EC_LTEES_2","EC_LTEES_3","EC_LTEES_4"};
		requiredEC = new String[]{"EC1", "EC_SGEH_1"};
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class)
	public void cepBladeIsConnectedToEniqServer(Map<String,Map<String,Object>> args){
		setTestCase("cepBladeIsConnectedToEniqServer",
				"CEP blade is connected eniq server");
		
		setTestStep("Confirm CEP blade available");
		assertTrue("There is no CEP blade connected",
				cli.checkCepBlade());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class, 
			dependsOnMethods = {"cepBladeIsConnectedToEniqServer"}, alwaysRun = false)
	public void cepConfigFilesCanBeEditedToSetIp(Map<String,Map<String,Object>> args){
		setTestCase("cepConfigFilesCanBeEditedToSetIp",
				"CEP config files can be edited to set correct IP");
		
		setTestStep("Edit CEP Config files");
		assertTrue("Failed to edit CEP config files",
				cli.editCEPConfigFiles());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"cepConfigFilesCanBeEditedToSetIp"}, alwaysRun = true)
	public void dataLoadingCanBeStartedOnCEP(Map<String,Map<String,Object>> args){
		setTestCase("dataLoadingCanBeStartedOnCEP",
				"Data loading can be started on CEP");
		
		setTestStep("Start Data Loading on CEP");
		assertTrue("Failed to start data loading on CEP",
				cli.startDataLoadingOnCEP());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"dataLoadingCanBeStartedOnCEP"}, alwaysRun = true)
	public void cepMediationServiceIsRunning(Map<String,Map<String,Object>> args){
		setTestCase("cepMediationServiceIsRunning",
				"cep-mediation service is running after being started");
		
		setTestStep("Check cep-mediation service status");
		assertTrue("Cep mediation service not running after being started",
				cli.getCepServiceStatus());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"cepMediationServiceIsRunning"}, alwaysRun = true)
	public void unrequiredECsCanBeDisabled(Map<String,Map<String,Object>> args){
		setTestCase("unrequiredECsCanBeDisabled",
				"Unrequired ECs can be disabled");
		
		setTestStep("Disable unrequired ECs for SGEH");
		assertTrue("Failed to disable unrequired ECs",
				cli.disableUnRequiredECs(unRequiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"unrequiredECsCanBeDisabled"}, alwaysRun = true)
	public void requiredECsForSGEHCanBeEnabled(Map<String,Map<String,Object>> args){
		setTestCase("requiredECsForSGEHCanBeEnabled",
				"Required ECs for SGEH can be enabled");
		
		setTestStep("Enable required ECs for SGEH");
		assertTrue("Failed to enable wanted ECs",
				cli.enableRequiredECs(requiredEC));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"requiredECsForSGEHCanBeEnabled"}, alwaysRun = true)
	public void dataDirectoriesCanBeLinkedToDatagenDirectories(Map<String,Map<String,Object>> args){
		setTestCase("dataDirectoriesCanBeLinkedToDatagenDirectories",
				"Data directories can be linked to datagen directories");
		
		setTestStep("Link data directories to datagen directories");
		assertTrue("Failed to link data directories to datagen directories",
				cli.createDatagenLinks("ec_1",
				new String[]{DIR_SGEH}, 
				new String[]{REMOTE_DIR_SGEH}));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"dataDirectoriesCanBeLinkedToDatagenDirectories"}, alwaysRun = true)
	public void sgehWorkflowsCanBeProvisioned(Map<String,Map<String,Object>> args){
		setTestCase("sgehWorkflowsCanBeProvisioned",
				"SGEH workflows can be provisioned");
		
		setTestStep("Run sgeh provision workflows script");
		assertTrue("SGEH Provision workflow execution failed",
				cli.provisionSgehWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"sgehWorkflowsCanBeProvisioned"}, alwaysRun = true)
	public void sgehTopologyCanBeRefreshed(Map<String,Map<String,Object>> args){
		setTestCase("sgehTopologyCanBeRefreshed",
				"SGEH topology can be refreshed");
		
		setTestStep("Refresh SGEH topology");
		assertTrue("Failed to refresh SGEH topology",
				cli.refreshSgehTopology());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"sgehTopologyCanBeRefreshed"}, alwaysRun = true)
	public void sgehWorkflowsAreEnabledAndRunning(Map<String,Map<String,Object>> args){
		setTestCase("sgehWorkflowsAreEnabledAndRunning",
				"SGEH workflows are enabled and running");
		
		setTestStep("Enable 2g 3g workflows");
		assertTrue("Failed to enable 2g 3g workflows",
				cli.enable2g3gWorkflows());
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"sgehWorkflowsAreEnabledAndRunning"}, alwaysRun = true)
	public void sgehDirectoriesAndTablesAreLoading(Map<String,Map<String,Object>> args){
		setTestCase("sgehDirectoriesAndTablesAreLoading",
				"SGEH directories and tables are loading");
		
		setTestStep("Check SGEH Directories and Tables");
		assertTrue("SGEH Directories and tables not populating",
				cli.checkDataLoading("ec_1",dirsToCheckSgeh,tablesToCheckSgeh,65,
						true,columnToCheckSgeh,columnValueSgeh));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="threeGSessionBrowserDataCanBeLoadedOnEniqServer", 
			dataProviderClass=DataLoadingTestDataProvider.class,
			dependsOnMethods = {"sgehDirectoriesAndTablesAreLoading"}, alwaysRun = true)
	public void gpehDirectoriesAndTablesAreLoading(Map<String,Map<String,Object>> args){
		setTestCase("gpehDirectoriesAndTablesAreLoading",
				"GPEH directories and tables are loading");
		
		setTestStep("Check GPEH Directories and Tables");
		assertTrue("GPEH Directories and tables not populating",
				cli.checkDataLoading("cep",dirsToCheckCep,tablesToCheckCep,45,true,columnToCheckGpeh,null));
	}
}
