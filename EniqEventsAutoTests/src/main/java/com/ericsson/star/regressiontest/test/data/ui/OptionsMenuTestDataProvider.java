package com.ericsson.star.regressiontest.test.data.ui;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.OptionsMenuDataProvider;

public class OptionsMenuTestDataProvider implements TestData{
	static OptionsMenuDataProvider data = new OptionsMenuDataProvider();
	
	@DataProvider(name="aboutBoxIsClosedByClickingOnXIcon")
	public static Object[][] aboutBoxIsClosedByClickingOnXIcon(){
  		Object[][] result = {{data.getCommands("aboutBoxIsClosedByClickingOnXIcon")}};
  		return result;
	}
	
	@DataProvider(name="aboutInOptionsMenuForAllTabs")
	public static Object[][] aboutInOptionsMenuForAllTabs(){
  		Object[][] result = {{data.getCommands("aboutInOptionsMenuForAllTabs")}};
  		return result;
	}
	
	@DataProvider(name="allInstalledFeaturesOnServerListedInAboutBox")
	public static Object[][] allInstalledFeaturesOnServerListedInAboutBox(){
  		Object[][] result = {{data.getCommands("allInstalledFeaturesOnServerListedInAboutBox")}};
  		return result;
	}
	
	@DataProvider(name="ensureBrandingIsPresentInAboutBox")
	public static Object[][] ensureBrandingIsPresentInAboutBox(){
  		Object[][] result = {{data.getCommands("ensureBrandingIsPresentInAboutBox")}};
  		return result;
	}
	
	@DataProvider(name="verticalScrollBarPresentInAboutBox")
	public static Object[][] verticalScrollBarPresentInAboutBox(){
  		Object[][] result = {{data.getCommands("verticalScrollBarPresentInAboutBox")}};
  		return result;
	}
	
	@DataProvider(name="optionsMenuPresentForAllTabs")
	public static Object[][] optionsMenuPresentForAllTabs(){
  		Object[][] result = {{data.getCommands("optionsMenuPresentForAllTabs")}};
  		return result;
	}
}
