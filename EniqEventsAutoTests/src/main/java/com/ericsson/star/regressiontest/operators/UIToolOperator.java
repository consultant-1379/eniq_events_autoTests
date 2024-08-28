
package com.ericsson.star.regressiontest.operators;

import java.util.Map;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.star.regressiontest.test.pages.ui.optionsmenu.OptionsMenuViewModel;
import com.ericsson.star.regressiontest.test.pages.ui.workspace.LoginViewModel;
import com.ericsson.star.regressiontest.test.pages.ui.workspace.MainPageViewModel;

/**
*
*Operator for executing Test Cases for UITool
*/
public class UIToolOperator implements UiOperator{
	private final Browser browser;
	private final BrowserTab browserTab;

	private LoginViewModel loginView;
	private MainPageViewModel mainPage;
	private OptionsMenuViewModel optionsMenuPage;
	
	private static String URL;
	private static String USER;
	private static String PASSWD;

	public UIToolOperator(Map<String, Object> args){
		URL = "https://"+((Host) args.get("host")).getIp() + ":" +(String)args.get("port") + (String)args.get("contextroot") ;
		USER = (String) args.get("username");
		PASSWD = (String) args.get("password");
		this.browser = UI.newBrowser(BrowserType.FIREFOX);
		this.browserTab = browser.open(URL);
		loginView = browserTab.getView(LoginViewModel.class);
	}
	
	public UIToolOperator(String url){
		this.browser = UI.newBrowser(BrowserType.FIREFOX);
		this.browserTab = browser.open(url);
		loginView = browserTab.getView(LoginViewModel.class);
	}
	
	public String getHTMLTitle(){
		return browserTab.getTitle();
	}
	
	public void login(){
		loginView.setUsername(USER);
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
	
	public void closeBrowser(){
		browser.close();
	}
	
	/******************************* Private Methods *********************************/
	
	private void pause(int timeout){
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			System.out.println("Sleep Interrupted Exception Msg: " + e.getMessage());
		}
	}	 
}
		