package com.ericsson.star.regressiontest.operators.ui.optionsmenu;

import java.util.Map;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.star.regressiontest.test.pages.ui.optionsmenu.AboutOptionViewModel;
import com.ericsson.star.regressiontest.test.pages.ui.optionsmenu.OptionsMenuViewModel;
import com.ericsson.star.regressiontest.test.pages.ui.workspace.LoginViewModel;
import com.ericsson.star.regressiontest.test.pages.ui.workspace.MainPageViewModel;

public class OptionsMenuOperator implements UiOperator{
	private final Browser browser;
	private final BrowserTab browserTab;

	private LoginViewModel loginView;
	private MainPageViewModel mainPage;
	private OptionsMenuViewModel optionsMenuPage;
	private AboutOptionViewModel aboutOptionPage;
	
	private static String URL;
	private static String USER;
	private static String PASSWD;

	public OptionsMenuOperator(Map<String, Object> args){
		URL = "https://"+((Host) args.get("host")).getIp() + ":" +(String)args.get("port") + (String)args.get("contextroot") ;
		USER = (String) args.get("username");
		PASSWD = (String) args.get("password");
		this.browser = UI.newBrowser(BrowserType.FIREFOX);
		this.browserTab = browser.open(URL);
		this.loginView = browserTab.getView(LoginViewModel.class);
	}
	
	public void login(){
		loginView.setUsername(USER);
		pause(2000);
		loginView.setPassword(PASSWD);
		loginView.clickLoginButton();
		pause(5000);
		mainPage = browserTab.getView(MainPageViewModel.class);
	}
	
	public void displayOptionsMenu(){
		mainPage.clickOptionsButton();
		pause(2000);
		optionsMenuPage = browserTab.getView(OptionsMenuViewModel.class);
	}
	
	public boolean isOptionMenuLabelDisplayed(){
		return mainPage.isOptionsMenuLabelDisplayed();
	}
	
	public boolean isUserGuideOptionDisplayed(){
		return optionsMenuPage.isUserGuideOptionPresent();
	}
	
	public boolean isAboutOptionDisplayed(){
		return optionsMenuPage.isAboutOptionPresent();
	}
	
	public boolean isGroupManagementOptDisplayed(){
		return optionsMenuPage.isGroupManagementOptionPresent();
	}
	
	public boolean isDeleteGroupsOptionDisplayed(){
		return optionsMenuPage.isDeleteGroupsOptionPresent();
	}
	
	public boolean isImportGroupsOptionDisplayed(){
		return optionsMenuPage.isImportGroupsOptionPresent();
	}
	
	public void clickAboutOption(){
		optionsMenuPage.clickAboutOption();
		pause(4000);
		aboutOptionPage = browserTab.getView(AboutOptionViewModel.class);
	}
	
	public void closeAboutOption(){
		aboutOptionPage.clickXButton();
		pause(2000);
	}
	
	public boolean isAboutOptionWindowVisible(){
		try{
			return aboutOptionPage.isVisible();
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean areAllInstalledFeaturesListed(){
		return aboutOptionPage.areAllInstalledFeaturesListed();
	}
	
	public boolean isEricssonLogoPresent(){
		return aboutOptionPage.isEricssonLogoPresent();
	}
	
	public boolean isEniqEventsVersionDisplayed(){
		return aboutOptionPage.isEniqEventsVersionDisplayed();
	}
	
	public boolean isEricssonCopyrightInfoDisplayed(){
		return aboutOptionPage.isEricssonCopyrightInfoDisplayed();
	}
	
	public boolean isVerticalScrollBarDisplayedOnAboutOptionMenu(){
		return aboutOptionPage.isVerticalScrollBarDisplayedOnAboutOptionMenu();
	}
	
	public void closeBrowser(){
		browser.close();
	}
	
	/******************************* Private Methods *********************************/
	
	private void pause(int timeout){
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}
