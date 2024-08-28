package com.ericsson.star.regressiontest.test.cases.cli.serverstatus;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.cli.ServerStatusTestDataProvider;

public class CoordinatorServerTests extends TorTestCaseHelper implements TestCase{
	private static String STDOUT = "STDOUT";
	private static String EXITCODE = "EXITCODE";
	
	private CliToolOperator cliToolOperator = new CliToolOperator();
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="webserverIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void webserverIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("webserverIsOnline","Admin Webserver is Online");

		setTestStep("svcs -a | grep webserver");
		assertTrue(cliToolOperator.runCommand(args.get("webserverIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("webserverIsOnline").get(STDOUT).toString()));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="engineIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void engineIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("engineStatusIsOnline","Engine status is Online");

		setTestStep("svcs -a | grep engine");
		assertTrue(cliToolOperator.runCommand(args.get("engineIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("engineIsOnline").get(STDOUT).toString()));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="engineStatusIsActive", dataProviderClass=ServerStatusTestDataProvider.class)
	public void engineStatusIsActive(Map<String,Map<String, Object>> args){
		setTestcase("engineStatusIsActive","Engine status is Active");

		setTestStep("engine status | grep Status");
		assertTrue(cliToolOperator.runCommand(args.get("engineStatusIsActive")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("engineStatusIsActive").get(STDOUT).toString()));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="engineCurrentProfileIsNormal", dataProviderClass=ServerStatusTestDataProvider.class)
	public void engineCurrentProfileIsNormal(Map<String,Map<String, Object>> args){
		setTestcase("engineCurrentProfileIsNormal","Current Engine Profile is Normal");

		setTestStep("engine status | grep Current Profile");
		assertTrue(cliToolOperator.runCommand(args.get("engineCurrentProfileIsNormal")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("engineCurrentProfileIsNormal").get(STDOUT).toString()));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="schedulerIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void schedulerIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("schedulerIsOnline","Scheduler is Online");

		setTestStep("svcs -a | grep scheduler");
		assertTrue(cliToolOperator.runCommand(args.get("schedulerIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("schedulerIsOnline").get(STDOUT).toString()));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="licmgrIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void licmgrIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("licmgrIsOnline","Licence Manager is Online");

		setTestStep("svcs -a | grep licmgr");
		assertTrue(cliToolOperator.runCommand(args.get("licmgrIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("licmgrIsOnline").get(STDOUT).toString()));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="repdbIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void repdbIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("licmgrIsOnline","RepDB is Online");

		setTestStep("svcs -a | grep repdb");
		assertTrue(cliToolOperator.runCommand(args.get("repdbIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("repdbIsOnline").get(STDOUT).toString()));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="controlzoneIsOnline", dataProviderClass=ServerStatusTestDataProvider.class)
	public void controlzoneIsOnline(Map<String,Map<String, Object>> args){
		setTestcase("controlzoneIsOnline","Control Zone is Online");

		setTestStep("svcs -a | grep repdb");
		assertTrue(cliToolOperator.runCommand(args.get("controlzoneIsOnline")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("controlzoneIsOnline").get(STDOUT).toString()));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="swapfileAvailable", dataProviderClass=ServerStatusTestDataProvider.class)
	public void swapfileAvailable(Map<String,Map<String, Object>> args){
		setTestcase("swapfileAvailable","Swap File is available");

		setTestStep("ls /swapfile");
		assertTrue(cliToolOperator.runCommand(args.get("swapfileAvailable")));
		setTestStep("STDOUT As Expected");
		assertTrue("Swapfile Missing",cliToolOperator.getExitCode() == Integer.parseInt((args.get("swapfileAvailable").get(EXITCODE).toString())));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="swapfileInVfstab", dataProviderClass=ServerStatusTestDataProvider.class)
	public void swapfileInVfstab(Map<String,Map<String, Object>> args){
		setTestcase("swapfileInVfstab","Control Zone is Online");

		setTestStep("Check swap entry in vfstab file");
		assertTrue(cliToolOperator.runCommand(args.get("swapfileInVfstab")));
		setTestStep("STDOUT As Expected");
		assertTrue("Swap entry not in vfstab file",cliToolOperator.getStdOut().contains(args.get("swapfileInVfstab").get(STDOUT).toString()));
	}

	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="eniq_sp_1InSwapFInVfstab", dataProviderClass=ServerStatusTestDataProvider.class)
	public void eniq_sp_1InSwapFInVfstab(Map<String,Map<String, Object>> args){
		setTestcase("eniq_sp_1InSwapFInVfstab","Control Zone is Online");

		setTestStep("eniq_sp_1 entry in vfstab");
		assertTrue(cliToolOperator.runCommand(args.get("eniq_sp_1InSwapFInVfstab")));
		setTestStep("STDOUT As Expected");
		assertTrue(cliToolOperator.getStdOut().contains(args.get("eniq_sp_1InSwapFInVfstab").get(STDOUT).toString()));
	}
}
