package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.SonvisCommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import org.junit.Test;

//import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;

/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 08/10/12
 * Time: 11:58
 * FileModified By: esiakua
 * To change this template use File | Settings | File Templates.
 */
public class RankingTabTest extends SonvisWebDriverBaseUnitTest{
	   private final LaunchMenu launchMenu = new LaunchMenu();
	   private final SonvisCommonUtils sonvisCommonUtils = new SonvisCommonUtils();

    @Override
    public void tearDown() throws Exception {
    	// TODO Auto-generated method stub
    	super.tearDown();
    }

    @Test
    public void testRankingANRX2RelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        //selenium.windowMaximize();
        launchMenu.selectTab(Tab.RANKING);
        launchMenu.selectDateOption(SonVisConstants.TODAY, null, null);
        sonvisCommonUtils.selectRankingMainPIAndDisplyCellDetailsForToday(SonVisConstants.ANR_X2_RELATIONS_ADDED);
    }
    
    @Test
    public void testRankingANRX2AddedRelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectRankingPIAndDisplayForToday(SonVisConstants.ANR_X2_RELATIONS_ADDED);
    }
    
    @Test
    public void testRankingANRX2RemovedRelationsForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectRankingPIAndDisplayForToday(SonVisConstants.ANR_X2_RELATIONS_REMOVED);
    }
    
    @Test
    public void testRankingPCIChangesForToday() throws PopUpException, InterruptedException {
        init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        sonvisCommonUtils.selectRankingPIAndDisplayForToday(SonVisConstants.PCI_CHANGES);
    }

}

