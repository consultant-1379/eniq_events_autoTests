package com.ericsson.star.regressiontest.test.data.ui;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.EniqEventsUIDataProvider;

public class EniqEventsUITestDataProvider implements TestData{
	static EniqEventsUIDataProvider data = new EniqEventsUIDataProvider();
	
	@DataProvider(name="Dummy")
	public static Object[][] seleniumRCServerIsWorking(){
  		Object[][] result = {{data.getCommands("Dummy")}};
  		return result;
	}
	
	@DataProvider(name="UIImprovements")
	public static Object[][] UIImprovements(){
  		Object[][] result = {{data.getCommands("UIImprovements")}};
  		return result;
	}
	
	@DataProvider(name="TwoG3GSgeh")
	public static Object[][] TwoG3GSgeh(){
  		Object[][] result = {{data.getCommands("TwoG3GSgeh")}};
  		return result;
	}
	
	@DataProvider(name="KpiNotification")
	public static Object[][] KpiNotification(){
  		Object[][] result = {{data.getCommands("KpiNotification")}};
  		return result;
	}
	
	@DataProvider(name="Mss")
	public static Object[][] Mss(){
  		Object[][] result = {{data.getCommands("Mss")}};
  		return result;
	}
	
	@DataProvider(name="Aac")
	public static Object[][] Acc(){
  		Object[][] result = {{data.getCommands("Aac")}};
  		return result;
	}
	
	@DataProvider(name="LteCfa")
	public static Object[][] LteCfa(){
  		Object[][] result = {{data.getCommands("LteCfa")}};
  		return result;
	}
	
	@DataProvider(name="LteHfa")
	public static Object[][] LteHfa(){
  		Object[][] result = {{data.getCommands("LteHfa")}};
  		return result;
	}
	
	@DataProvider(name="ThreeGSessionBrowser")
	public static Object[][] ThreeGSessionBrowser(){
  		Object[][] result = {{data.getCommands("ThreeGSessionBrowser")}};
  		return result;
	}
	
	@DataProvider(name="ThreeGKpiAnalysis")
	public static Object[][] ThreeGKpiAnalysis(){
  		Object[][] result = {{data.getCommands("ThreeGKpiAnalysis")}};
  		return result;
	}
}
