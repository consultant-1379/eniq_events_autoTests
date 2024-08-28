package com.ericsson.eniq.events.ui.selenium.tests.sonvis.config;

import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonVisConstants;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author esiakua
 * @since 2013
 *
 */
public class SonvisCommonUtils extends SonvisWebDriverBaseUnitTest {
    protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();
    private final LaunchMenu launchMenu = new LaunchMenu();

    protected static final String DEFAULT_TIMEOUT = "20000";
    
    public void selectRankingMainPIAndDisplyCellDetailsForToday(String mainPI){
        launchMenu.selectMainPI(mainPI);
        launchMenu.clickLaunch();
        waitForPageToLoad();
        launchMenu.selectCGIRowFromTopCells(mainPI);
        waitForPageToLoad();
        launchMenu.selectRankingGridFromCGITab();
        waitForPageToLoad();
    }
    
    public void selectRankingPIAndDisplayForToday(String PI){
        launchMenu.selectTab(Tab.RANKING);
        launchMenu.selectDateOption(SonVisConstants.TODAY, null, null);
        selectRankingMainPIAndDisplyCellDetailsForToday(PI);
    }
    
    public void selectNetworkPIAndDisplyCellDetailsForToday(String mainPI){
        launchMenu.selectMainPI(mainPI);
        launchMenu.clickLaunch();
        waitForPageToLoad();
        launchMenu.selectPeakPoint(mainPI);
        waitForPageToLoad();
    }
    
    public void selectNetworkingPIAndDisplayForToday(String PI){
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption(SonVisConstants.TODAY, null, null);
        selectNetworkPIAndDisplyCellDetailsForToday(PI);
    }
	
	 public void selectNetworkPIAndDisplyCellDetailsForCustomDate(String mainPI){
        launchMenu.selectMainPI(mainPI);
        launchMenu.clickLaunch();
        waitForPageToLoad();
        launchMenu.selectPeakPoint(mainPI);
        waitForPageToLoad();
    }
 
 
}
