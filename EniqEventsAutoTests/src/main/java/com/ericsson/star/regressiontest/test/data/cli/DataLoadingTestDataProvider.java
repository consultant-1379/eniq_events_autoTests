package com.ericsson.star.regressiontest.test.data.cli;

import org.testng.annotations.DataProvider;
import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.DataLoadingDataProvider;

public class DataLoadingTestDataProvider  implements TestData{
	static DataLoadingDataProvider data = new DataLoadingDataProvider();
	
	@DataProvider(name="twoGDataCanBeLoadedOnEniqServer")
	public static Object[][] twoGDataCanBeLoadedOnEniqServer(){
  		Object[][] result = {{data.getCommands("twoGDataCanBeLoadedOnEniqServer")}};
  		return result;
	}
	
	@DataProvider(name="lteDataCanBeLoadedOnEniqServer")
	public static Object[][] lteDataCanBeLoadedOnEniqServer(){
  		Object[][] result = {{data.getCommands("lteDataCanBeLoadedOnEniqServer")}};
  		return result;
	}
	
	@DataProvider(name="threeGSessionBrowserDataCanBeLoadedOnEniqServer")
	public static Object[][] threeGSessionBrowserDataCanBeLoadedOnEniqServer(){
  		Object[][] result = {{data.getCommands("threeGSessionBrowserDataCanBeLoadedOnEniqServer")}};
  		return result;
	}
	
	@DataProvider(name="kpiNotificationDataCanBeloadedOnEniqServer")
	public static Object[][] kpiNotificationDataCanBeloadedOnEniqServer(){
  		Object[][] result = {{data.getCommands("kpiNotificationDataCanBeloadedOnEniqServer")}};
  		return result;
	}
	
	@DataProvider(name="mssDataCanBeLoadedOnEniqServer")
	public static Object[][] mssDataCanBeLoadedOnEniqServer(){
  		Object[][] result = {{data.getCommands("mssDataCanBeLoadedOnEniqServer")}};
  		return result;
	}
}
