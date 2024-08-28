package com.ericsson.star.regressiontest.test.cases.cli.serverstatus;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.CliToolOperator;
import com.ericsson.star.regressiontest.test.data.cli.ServerStatusTestDataProvider;

public class GlassfishServerTests extends TorTestCaseHelper implements TestCase{
	
	private CliToolOperator cliToolOperator = new CliToolOperator();
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="httpRedirectsToHttpsWhenHttpsIsEnabled", 
		dataProviderClass=ServerStatusTestDataProvider.class)
	public void httpRedirectsToHttpsWhenHttpsIsEnabled(Map<String,Map<String, Object>> args){
		setTestcase("httpRedirectsToHttpsWhenHttpsIsEnabled",
				"Http Redirects to Https when Https is Enabled");
	
		setTestStep("Enable Https");
		assertTrue("Failed to enable HTTPS",
				cliToolOperator.enableHttps(args.get("httpRedirectsToHttpsWhenHttpsIsEnabled")));
		setTestStep("Run wget");
		assertTrue(cliToolOperator.runWget(args.get("httpRedirectsToHttpsWhenHttpsIsEnabled")));
		setTestStep("Check Wget Result");
		assertTrue("Http did not redirect to Https",
				cliToolOperator.checkWgetResult(args.get("httpRedirectsToHttpsWhenHttpsIsEnabled")));
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.CLI})
	@Test (groups = {"acceptance"}, dataProvider="httpsRedirectsToHttpWhenHttpsIsDisabled", 
		dataProviderClass=ServerStatusTestDataProvider.class)
	public void httpsRedirectsToHttpWhenHttpsIsDisabled(Map<String,Map<String, Object>> args){
		setTestcase("httpsRedirectsToHttpWhenHttpsIsDisabled",
				"Https Redirects to Http when Https is disabled");
	
		setTestStep("Disable Https");
		assertTrue("Failed to enable HTTPS",
				cliToolOperator.disableHttps(args.get("httpsRedirectsToHttpWhenHttpsIsDisabled")));
		setTestStep("Run wget");
		assertTrue(cliToolOperator.runWget(args.get("httpsRedirectsToHttpWhenHttpsIsDisabled")));
		setTestStep("Check Wget Result");
		assertTrue("Https did not redirect to Http",
				cliToolOperator.checkWgetResult(args.get("httpsRedirectsToHttpWhenHttpsIsDisabled")));
		setTestStep("Reenable Https");
		assertTrue("Failed to reenable HTTPS",
				cliToolOperator.enableHttps(args.get("httpsRedirectsToHttpWhenHttpsIsDisabled")));
	}
}
