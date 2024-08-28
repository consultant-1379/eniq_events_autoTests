package com.ericsson.star.regressiontest.test.cases.ui.ltecfa;

import org.testng.annotations.Test;

import java.util.Map;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.star.regressiontest.test.data.ui.OptionsMenuTestDataProvider;
import com.ericsson.star.regressiontest.operators.ui.workspace.EniqEventsUiOperator;

public class RankingEngineTestGroup implements TestCase{
	EniqEventsUiOperator operator;
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void SubscriberRankingCallSetupFailures_6_2(Map<String,Map<String,Object>> args){  
		operator = new EniqEventsUiOperator(args.get("optionsMenuPresentForAllTabs"));
		operator.login();
		operator.displayTimeRange();
		operator.selectTimeRange();
		
		
		
		operator.closeBrowser();
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void DrillDownIMSIsubscriberRankingCallSetupFailures_6_3(){        
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void SubscriberRankingCallDrop_6_4() throws Exception { 
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void DrillDownIMSIsubscriberRankingCallDrop_6_5(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void SubscriberRankingRecurringFailures_6_6(){        
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void DrillDownIMSIsubscriberRankingRecurringFailures_6_7(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void TrackingAreaRankingCallSetupFailure_6_9(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void TrackingAreaRankingCallDrop_6_11(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void CauseCodeRankingCallSetupFailure_6_14(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void CauseCodeRankingCallDrop_6_15(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void TerminalRankingCallSetupFailure_6_16(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void TerminalRankingCallDrop_6_22(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void eCellRankingCallSetupFailure_6_25(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void eCellRankingCallDrop_6_26(){
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void eNodeBRankingCallSetupFailures_6_29(){        
    }
    
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test(dataProvider = "optionsMenuPresentForAllTabs", 
			dataProviderClass = OptionsMenuTestDataProvider.class)
    public void eNodeBRankingCallDrop_6_30(){
    }
}
