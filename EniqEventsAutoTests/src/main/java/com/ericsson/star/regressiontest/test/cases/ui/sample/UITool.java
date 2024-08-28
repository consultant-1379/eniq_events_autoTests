package com.ericsson.star.regressiontest.test.cases.ui.sample;

import java.util.Map;

import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;

import com.ericsson.star.regressiontest.operators.UIToolOperator;
import com.ericsson.star.regressiontest.test.data.ui.UIToolTestDataProvider;

/**
*
*	Class to execute tests against UITool
**/
public class UITool extends TorTestCaseHelper implements TestCase{

	UIToolOperator operator = null;

	/**
	* 
	*/
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "loginWithCorrectCredentialsProceedOkay", dataProviderClass = UIToolTestDataProvider.class)
	public void loginWithCorrectCredentialsProceedOkay(Map<String,Map<String, Object>> args){
		operator = new UIToolOperator(args.get("loginWithCorrectCredentialsProceedOkay"));

		setTestStep("Check Login Page Title");
		assertEquals("Ericsson OSS Log In", operator.getHTMLTitle());
		
		setTestStep("Login Events UI");
		operator.login();
		
		setTestStep("Check Main Page Title");
		assertEquals("Ericsson OSS", operator.getHTMLTitle());
		
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuHasAllEntries", dataProviderClass = UIToolTestDataProvider.class)
	public void optionsMenuHasAllEntries(Map<String,Map<String, Object>> args){
		setTestcase("Check Options Menu Entries","Sample UI Tests");
		
		operator = new UIToolOperator(args.get("optionsMenuHasAllEntries"));
		setTestStep("Login Events UI");
		operator.login();
		
		setTestStep("Display Options Menu");
		operator.displayOptionsMenu();
		
		setTestStep("User Guide Option is Displayed");
		assertTrue("User Guide Option is not Displayed",operator.isUserGuideOptionDisplayed());

		setTestStep("About Option is Displayed");
		assertTrue("About Option is not Displayed",operator.isAboutOptionDisplayed());
		
		setTestStep("Group Management Option is Displayed");
		assertTrue("Group Management Option is not Displayed",operator.isGroupManagementOptDisplayed());
		
		setTestStep("Import Groups Option is Displayed");
		assertTrue("Import Groups Option is not Displayed",operator.isImportGroupsOptionDisplayed());
		
		setTestStep("Delete Groups Option is Displayed");
		assertTrue("Delete Groups Option is not Displayed",operator.isDeleteGroupsOptionDisplayed());
		
		operator.closeBrowser();
	}
	
	/******************************* Private Methods *********************************/
	
}
