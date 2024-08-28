package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.SonvisCommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import org.junit.Test;


/**
 * Created by IntelliJ IDEA.
 * User: esiakua
 * Date: 16/06/13
 * Time: 11:58
 */
public class NetworkViewTabTest extends SonvisWebDriverBaseUnitTest{
	   private final LaunchMenu launchMenu = new LaunchMenu();
	   private final SonvisCommonUtils sonvisCommonUtils = new SonvisCommonUtils();

    @Override
    public void tearDown() throws Exception {
    	// TODO Auto-generated method stub
    	super.tearDown();
    }

    @Test
    public void testNetworkViewANRX2RelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        //selenium.windowMaximize();
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption(SonVisConstants.TODAY, null, null);
        sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForToday(SonVisConstants.ANR_X2_RELATIONS_ADDED);
    }
    
    @Test
    public void testRankingANRX2AddedRelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectNetworkingPIAndDisplayForToday(SonVisConstants.ANR_X2_RELATIONS_ADDED);
    }
    
    @Test
    public void testRankingANRX2RemovedRelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectNetworkingPIAndDisplayForToday(SonVisConstants.ANR_X2_RELATIONS_REMOVED);
    }
    
    @Test
    public void testRankingPCIChangesForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectNetworkingPIAndDisplayForToday(SonVisConstants.PCI_CHANGES);
    }

}

