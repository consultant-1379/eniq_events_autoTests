package com.ericsson.star.regressiontest.test.data.cli;

import org.testng.annotations.DataProvider;
import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.LoadTopologyDataProvider;

public class LoadTopologyTestDataProvider  implements TestData{
	static LoadTopologyDataProvider data = new LoadTopologyDataProvider();
	
	@DataProvider(name="loadTopologyTwoGTest")
	public static Object[][] loadTopologyTwoGTest(){
  		Object[][] result = {{data.getCommands("loadTopologyTwoGTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyThreeGDimETest")
	public static Object[][] loadTopologyThreeGDimETest(){
  		Object[][] result = {{data.getCommands("loadTopologyThreeGDimETest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyThreeGDimZTest")
	public static Object[][] loadTopologyThreeGDimZTest(){
  		Object[][] result = {{data.getCommands("loadTopologyThreeGDimZTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyFourGTest")
	public static Object[][] loadTopologyFourGTest(){
  		Object[][] result = {{data.getCommands("loadTopologyFourGTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyLteTest")
	public static Object[][] loadTopologyLteTest(){
  		Object[][] result = {{data.getCommands("loadTopologyLteTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyWcdmaTest")
	public static Object[][] loadTopologyWcdmaTest(){
  		Object[][] result = {{data.getCommands("loadTopologyWcdmaTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyWcdmaImsiTest")
	public static Object[][] loadTopologyWcdmaImsiTest(){
  		Object[][] result = {{data.getCommands("loadTopologyWcdmaImsiTest")}};
  		return result;
	}

	@DataProvider(name="loadTopologyMssTest")
	public static Object[][] loadTopologyMssTest(){
  		Object[][] result = {{data.getCommands("loadTopologyMssTest")}};
  		return result;
	}

	@DataProvider(name="loadTopology3GSessBrowserRncTest")
	public static Object[][] loadTopology3GSessBrowserRncTest(){
  		Object[][] result = {{data.getCommands("loadTopology3GSessBrowserRncTest")}};
  		return result;
	}

	@DataProvider(name="loadTopology3GSessBrowserSgsnTest")
	public static Object[][] loadTopology3GSessBrowserSgsnTest(){
  		Object[][] result = {{data.getCommands("loadTopology3GSessBrowserSgsnTest")}};
  		return result;
	}
}
