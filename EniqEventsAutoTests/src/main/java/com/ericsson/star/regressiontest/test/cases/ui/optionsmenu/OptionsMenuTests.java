package com.ericsson.star.regressiontest.test.cases.ui.optionsmenu;

import java.util.Map;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.operators.ui.optionsmenu.OptionsMenuOperator;
import com.ericsson.star.regressiontest.test.data.ui.OptionsMenuTestDataProvider;

public class OptionsMenuTests extends TorTestCaseHelper implements TestCase{
	OptionsMenuOperator operator = null;
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void optionsMenuPresentForAllTabs(Map<String,Map<String, Object>> args){
		setTestcase("optionsMenuPresentForAllTabs","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("optionsMenuPresentForAllTabs"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Check if Options Menu Label is Displayed");
		assertTrue("Options Menu Label is not Displayed",operator.isOptionMenuLabelDisplayed());
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "aboutInOptionsMenuForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void aboutInOptionsMenuForAllTabs(Map<String,Map<String, Object>> args){
		setTestcase("aboutInOptionsMenuForAllTabs","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("aboutInOptionsMenuForAllTabs"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Open About Menu Option");
		operator.displayOptionsMenu();
		setTestStep("About Option is included in Options Menu");
		assertTrue("About Option is included in Options Menu",operator.isAboutOptionDisplayed());
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "allInstalledFeaturesOnServerListedInAboutBox", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void allInstalledFeaturesOnServerListedInAboutBox(Map<String,Map<String, Object>> args){
		setTestcase("allInstalledFeaturesOnServerListedInAboutBox","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("allInstalledFeaturesOnServerListedInAboutBox"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Open About Menu Option");
		operator.displayOptionsMenu();
		operator.clickAboutOption();
		assertTrue("[ ERROR ] Feature not found in the list",operator.areAllInstalledFeaturesListed());
		
		setTestStep("Close About Menu Option");
		operator.closeAboutOption();
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "verticalScrollBarPresentInAboutBox", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void verticalScrollBarPresentInAboutBox(Map<String,Map<String, Object>> args){
		setTestcase("verticalScrollBarPresentInAboutBox","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("verticalScrollBarPresentInAboutBox"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Open About Menu Option");
		operator.displayOptionsMenu();
		operator.clickAboutOption();
		setTestStep("Check Vertical Scroll Bar on About Menu");
		assertTrue("Vertical Scroll Bar is not displayed on About Menu"
				,operator.isVerticalScrollBarDisplayedOnAboutOptionMenu());
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "aboutBoxIsClosedByClickingOnXIcon",
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void aboutBoxIsClosedByClickingOnXIcon(Map<String,Map<String, Object>> args){
		setTestcase("aboutBoxIsClosedByClickingOnXIcon","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("aboutBoxIsClosedByClickingOnXIcon"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Open About Menu Option");
		operator.displayOptionsMenu();
		operator.clickAboutOption();
		assertTrue("[ ERROR ] About Box did not Appear",operator.isAboutOptionWindowVisible());
		
		setTestStep("Close About Menu Option");
		operator.closeAboutOption();
		assertFalse("[ ERROR ] About Box did not dissappear",operator.isAboutOptionWindowVisible());
		operator.closeBrowser();
	}
	
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "ensureBrandingIsPresentInAboutBox", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
	public void ensureBrandingIsPresentInAboutBox(Map<String,Map<String, Object>> args){
		setTestcase("ensureBrandingIsPresentInAboutBox","Test Options Menu");
		operator = new OptionsMenuOperator(args.get("ensureBrandingIsPresentInAboutBox"));
		setTestStep("Login Events UI");
		operator.login();
		setTestStep("Open About Menu Option");
		operator.displayOptionsMenu();
		operator.clickAboutOption();
		setTestStep("Check Ericsson Logo");
		assertTrue("Ericsson Logo is not Displayed",operator.isEricssonLogoPresent());
		setTestStep("Check Eniq Events Version");
		assertTrue("Eniq Events Version is not Displayed",operator.isEniqEventsVersionDisplayed());
		setTestStep("Check Ericsson Copyright Info");
		assertTrue("Ericsson Copyright Info is not Displayed",operator.isEricssonCopyrightInfoDisplayed());
		
		setTestStep("Close About Menu Option");
		operator.closeAboutOption();
		operator.closeBrowser();
	}
}
