package com.ericsson.star.regressiontest.test.data.cli;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.star.regressiontest.data.ServerStatusDataProvider;

public class ServerStatusTestDataProvider implements TestData{
	static ServerStatusDataProvider data = new ServerStatusDataProvider();
	
	@DataProvider(name="webserverIsOnline")
	public static Object[][] webserverIsOnline(){
  		Object[][] result = {{data.getCommands("webserverIsOnline")}};
  		return result;
	}

	@DataProvider(name="engineIsOnline")
	public static Object[][] engineIsOnline(){
  		Object[][] result = {{data.getCommands("engineIsOnline")}};
  		return result;
	}
	
	@DataProvider(name="engineStatusIsActive")
	public static Object[][] engineStatusIsActive(){
  		Object[][] result = {{data.getCommands("engineStatusIsActive")}};
  		return result;
	}
	
	@DataProvider(name="engineCurrentProfileIsNormal")
	public static Object[][] engineCurrentProfileIsNormal(){
  		Object[][] result = {{data.getCommands("engineCurrentProfileIsNormal")}};
  		return result;
	}
	
	@DataProvider(name="schedulerIsOnline")
	public static Object[][] schedulerIsOnline(){
  		Object[][] result = {{data.getCommands("schedulerIsOnline")}};
  		return result;
	}
	
	@DataProvider(name="licmgrIsOnline")
	public static Object[][] licmgrIsOnline(){
  		Object[][] result = {{data.getCommands("licmgrIsOnline")}};
  		return result;
	}
	
	@DataProvider(name="repdbIsOnline")
	public static Object[][] repdbIsOnline(){
  		Object[][] result = {{data.getCommands("repdbIsOnline")}};
  		return result;
	}
	
	@DataProvider(name="controlzoneIsOnline")
	public static Object[][] controlzoneIsOnline(){
  		Object[][] result = {{data.getCommands("controlzoneIsOnline")}};
  		return result;
	}
	
	@DataProvider(name="swapfileAvailable")
	public static Object[][] swapfileAvailable(){
  		Object[][] result = {{data.getCommands("swapfileAvailable")}};
  		return result;
	}
	
	@DataProvider(name="swapfileInVfstab")
	public static Object[][] swapfileInVfstab(){
  		Object[][] result = {{data.getCommands("swapfileInVfstab")}};
  		return result;
	}
	
	@DataProvider(name="eniq_sp_1InSwapFInVfstab")
	public static Object[][] eniq_sp_1InSwapFInVfstab(){
  		Object[][] result = {{data.getCommands("eniq_sp_1InSwapFInVfstab")}};
  		return result;
	}

	@DataProvider(name="httpRedirectsToHttpsWhenHttpsIsEnabled")
	public static Object[][] httpRedirectsToHttpsWhenHttpsIsEnabled(){
  		Object[][] result = {{data.getCommands("httpRedirectsToHttpsWhenHttpsIsEnabled")}};
  		return result;
	}
	
	@DataProvider(name="httpsRedirectsToHttpWhenHttpsIsDisabled")
	public static Object[][] httpsRedirectsToHttpWhenHttpsIsDisabled(){
  		Object[][] result = {{data.getCommands("httpsRedirectsToHttpWhenHttpsIsDisabled")}};
  		return result;
	}
}
