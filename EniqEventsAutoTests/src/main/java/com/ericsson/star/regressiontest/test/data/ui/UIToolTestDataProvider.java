package com.ericsson.star.regressiontest.test.data.ui;
import org.testng.annotations.DataProvider;
import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.UIToolDataProvider;

/**
 *
 *	Test DataProvider for executing Test Cases for UITool
 */
public class UIToolTestDataProvider implements TestData{

	static UIToolDataProvider dataUITool = new UIToolDataProvider();

	@DataProvider(name="UIToolTestData")
	public static String[][] UIToolTestData(){
  		String[][] result = {{"https://atrcxb1726.athtem.eei.ericsson.se:8181/EniqEventsUI/"}};
  		return result;
	}
	
	@DataProvider(name="loginWithCorrectCredentialsProceedOkay")
	public static Object[][] loginWithCorrectCredentialsProceedOkay(){
  		Object[][] result = {{dataUITool.getCommands("loginWithCorrectCredentialsProceedOkay")}};
  		return result;
	}
	
	@DataProvider(name="optionsMenuHasAllEntries")
	public static Object[][] optionsMenuHasAllEntries(){
  		Object[][] result = {{dataUITool.getCommands("optionsMenuHasAllEntries")}};
  		return result;
	}
}