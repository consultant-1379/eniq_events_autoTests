package com.ericsson.star.regressiontest.test.cases.ui;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.ui.EniqEventsUITestDataProvider;

public class LteHfaTests extends TorTestCaseHelper implements TestCase {
	CliToolOperator cli = new CliToolOperator();
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="Dummy", 
			dataProviderClass=EniqEventsUITestDataProvider.class)
	public void seleniumRCServerIsWorking(Map<String,Map<String,Object>> args){
		setTestCase("seleniumRCServerIsWorking",
				"Selenium RC Server is started and working");
		
		setTestStep("Run Dummy Test Suite");
		assertTrue(cli.runSelenium(args.get("Dummy")));
		
		setTestStep("Parse selenium result log");
		assertTrue("Failed to parse selenium result log",
				cli.parseSeleniumLog(args.get("Dummy")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="LteHfa", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"seleniumRCServerIsWorking"}, alwaysRun = true)
	public void createLteGroups(Map<String,Map<String,Object>> args){
		setTestCase("createLteGroups", "Create LTE Groups");
		
		setTestStep("Create Lte Groups");
		assertTrue(cli.createLteGroups(args.get("LteHfa")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="LteHfa", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"createLteGroups"}, alwaysRun = true)
	public void LteHfaUIComponentsAreConsistent(Map<String,Map<String,Object>> args){
		setTestCase("LteHfaUIComponentsAreConsistent",
				"Lte Hfa UI Components Are Consistent");

		setTestStep("Lte Hfa UI Test Suite");
		assertTrue(cli.runSelenium(args.get("LteHfa")));
		
		setTestStep("Parse selenium result log");
		assertTrue("Failed to parse selenium result log",
				cli.parseSeleniumLog(args.get("LteHfa")));
	}
}
