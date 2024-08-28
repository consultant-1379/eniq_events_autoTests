package com.ericsson.star.regressiontest.test.cases.ui;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.ui.EniqEventsUITestDataProvider;

public class ThreeGKpiAnalysis extends TorTestCaseHelper implements TestCase {
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
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="ThreeGKpiAnalysis", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"seleniumRCServerIsWorking"}, alwaysRun = true)
	public void mapsAreLoaded(Map<String,Map<String,Object>> args){
		setTestCase("mapsAreLoaded",
				"Maps are Loaded Prior to UI tests");
		
		setTestStep("Three G KPI Analysis UI Test Suite");
		assertTrue(cli.areKpiAnalysisMapsLoaded(args.get("ThreeGKpiAnalysis")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="ThreeGKpiAnalysis", 
			dataProviderClass=EniqEventsUITestDataProvider.class,
			dependsOnMethods = {"mapsAreLoaded"}, alwaysRun = true)
	public void ThreeGKpiAnalysisUIComponentsAreConsistent(Map<String,Map<String,Object>> args){
		setTestCase("ThreeGKpiAnalysisUIComponentsAreConsistent",
				"Three G KPI Analysis UI Components Are Consistent");
		
		setTestStep("Three G KPI Analysis UI Test Suite");
		assertTrue(cli.runSelenium(args.get("ThreeGKpiAnalysis")));
		
		setTestStep("Parse selenium result log");
		assertTrue("Failed to parse selenium result log",
				cli.parseSeleniumLog(args.get("ThreeGKpiAnalysis")));
	}
}
