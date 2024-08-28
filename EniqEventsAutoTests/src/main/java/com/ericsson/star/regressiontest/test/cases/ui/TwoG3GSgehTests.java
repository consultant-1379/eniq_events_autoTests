package com.ericsson.star.regressiontest.test.cases.ui;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.ui.EniqEventsUITestDataProvider;

public class TwoG3GSgehTests extends TorTestCaseHelper implements TestCase {
	CliToolOperator cli = new CliToolOperator();
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="Dummy", 
			dataProviderClass=EniqEventsUITestDataProvider.class)
	public void seleniumRCServerIsWorking(Map<String,Map<String,Object>> args){
		setTestCase("seleniumRCServerIsWorking",
				"Selenium RC Server is started and working");
		
		setTestStep("Run Dummy Test Suite");
		assertTrue("Dummy selenium test suite did not run",
				cli.runSelenium(args.get("Dummy")));
		
		setTestStep("Parse selenium result log");
		assertTrue("Failed to parse selenium result log",
				cli.parseSeleniumLog(args.get("Dummy")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="Dummy", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"seleniumRCServerIsWorking"}, alwaysRun = true)
	public void create2g3gGroups(Map<String,Map<String,Object>> args){
		setTestCase("create2g3gGroups", "Create 2G 3G Groups");
		
		setTestStep("Create 2G 3G groups");
		assertTrue(cli.create2g3gGroups(args.get("TwoG3GSgeh")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="TwoG3GSgeh", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"create2g3gGroups"}, alwaysRun = true)
	public void TwoG3GSgehUIComponentsAreConsistent(Map<String,Map<String,Object>> args){
		setTestCase("TwoG3GSgehUIComponentsAreConsistent",
				"Two G 3G Sgeh UI Components Are Consistent");
		
		setTestStep("Run Two G 3G Sgeh UI Test Suite");
		assertTrue(cli.runSelenium(args.get("TwoG3GSgeh")));
		
		setTestStep("Parse selenium result log");
		assertTrue("Failed to parse selenium result log",
				cli.parseSeleniumLog(args.get("TwoG3GSgeh")));
	}
}
