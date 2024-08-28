package com.ericsson.star.regressiontest.test.data.cli;
import org.testng.annotations.DataProvider;
import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.CliToolDataProvider;

/**
 *
 *	Test DataProvider for executing Test Cases for CliTool
 */
public class CliToolTestDataProvider implements TestData{

	static CliToolDataProvider dataCliTool = new CliToolDataProvider();


	@DataProvider(name="DEFTFC-615_Func_1")
	public static Object[][] DEFTFC_615_Func_1TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-615_Func_1")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-615_Func_2")
	public static Object[][] DEFTFC_615_Func_2TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-615_Func_2")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_1")
	public static Object[][] DEFTFC_618_Func_1TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_1")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_2")
	public static Object[][] DEFTFC_618_Func_2TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_2")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_3")
	public static Object[][] DEFTFC_618_Func_3TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_3")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_4")
	public static Object[][] DEFTFC_618_Func_4TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_4")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_5")
	public static Object[][] DEFTFC_618_Func_5TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_5")}};
		 return result;
	}

	@DataProvider(name="DEFTFC-618_Func_6")
	public static Object[][] DEFTFC_618_Func_6TestData(){		
		Object [][] result = {{dataCliTool.getCommands("DEFTFC-618_Func_6")}};
		 return result;
	}
}