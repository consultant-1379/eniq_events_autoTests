package com.ericsson.star.regressiontest.test.cases.cli.topology;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.cli.LoadTopologyTestDataProvider;

public class TopologyLoadingTests extends TorTestCaseHelper implements TestCase{
	private CliToolOperator cli = new CliToolOperator();
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyTwoGTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load2GTopology(Map<String,Map<String, Object>> args){
		setTestCase("load2GTopology","2G Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyTwoGTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyTwoGTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyTwoGTest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyTwoGTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyTwoGTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyThreeGDimETest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load3GDimETopology(Map<String,Map<String, Object>> args){
		setTestCase("load3GDimETopology","3G Dim E Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyThreeGDimETest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyThreeGDimETest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyThreeGDimETest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyThreeGDimETest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyThreeGDimETest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyThreeGDimZTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load3GDimZTopology(Map<String,Map<String, Object>> args){
		setTestCase("load3GDimZTopology","3G Dim Z Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyThreeGDimZTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyThreeGDimZTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyThreeGDimZTest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyThreeGDimZTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyThreeGDimZTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyFourGTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load4GTopology(Map<String,Map<String, Object>> args){
		setTestCase("load4GTopology","4G Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyFourGTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyFourGTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyFourGTest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyFourGTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyFourGTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyLteTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void loadLteTopology(Map<String,Map<String, Object>> args){
		setTestCase("loadLteTopology","Lte Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyLteTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyLteTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyLteTest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyLteTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyLteTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyWcdmaImsiTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void loadWcdmaImsiTopology(Map<String,Map<String, Object>> args){
		setTestCase("loadWcdmaImsiTopology",
				"Wcdma Imsi Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyWcdmaImsiTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyWcdmaImsiTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyWcdmaImsiTest")));
		
		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyWcdmaImsiTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyWcdmaImsiTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyWcdmaTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void loadWcdmaTopology(Map<String,Map<String, Object>> args){
		setTestCase("loadWcdmaTopology","Wcdma Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyWcdmaTest")));
		
		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyWcdmaTest")) > 0);
		
		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyWcdmaTest")));

		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyWcdmaTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyWcdmaTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopologyMssTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void loadMssTopology(Map<String,Map<String, Object>> args){
		setTestCase("loadMssTopology","Mss Topology Can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopologyMssTest")));

		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopologyMssTest")) > 0);

		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopologyMssTest")));

		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopologyMssTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopologyMssTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopology3GSessBrowserRncTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load3GSessionBrowserRncTopology(Map<String,Map<String, Object>> args){
		setTestCase("load3GSessionBrowserRncTopology",
				"3G Session Browser Rnc Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopology3GSessBrowserRncTest")));

		setTestStep("Check Interface");
		assertTrue("Interface not activated",
				cli.checkInterface(args.get("loadTopology3GSessBrowserRncTest")) > 0);

		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopology3GSessBrowserRncTest")));

		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",
				cli.activateEngineSet(args.get("loadTopology3GSessBrowserRncTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopology3GSessBrowserRncTest")));
		
		setTestStep("Load Home Network Group");
		assertTrue("3g Session Browser home network group could not be loaded.",
				cli.loadHomeNetworkGroup(args.get("loadTopology3GSessBrowserRncTest")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="loadTopology3GSessBrowserSgsnTest", 
			dataProviderClass=LoadTopologyTestDataProvider.class)
	public void load3GSessionBrowserSgsnTopology(Map<String,Map<String, Object>> args){
		setTestCase("load3GSessionBrowserSgsnTopology",
				"3G Session Browser Sgsn Topology can be loaded");
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Currently Populated",
				cli.checkTopologyTables(args.get("loadTopology3GSessBrowserSgsnTest")));

		setTestStep("Check Interface");
		assertTrue("Interface not activated",cli.checkInterface(args.get("loadTopology3GSessBrowserSgsnTest")) > 0);

		setTestStep("Copy/Unzip topology files to destination folder");
		assertTrue("Topology files could not be placed in destination folder",
				cli.moveTopologyToFolder(args.get("loadTopology3GSessBrowserSgsnTest")));

		setTestStep("Activate Engine Set");
		assertTrue("Failed to Activate Set",cli.activateEngineSet(args.get("loadTopology3GSessBrowserSgsnTest")));
		
		setTestStep("Check Topology Tables");
		assertTrue("Topology tables are not Populated",
				cli.checkTopologyTables(args.get("loadTopology3GSessBrowserSgsnTest")));
	}
}
