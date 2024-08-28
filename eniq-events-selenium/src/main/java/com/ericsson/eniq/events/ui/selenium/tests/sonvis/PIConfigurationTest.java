package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.SonvisCommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import org.junit.Test;


/**
 * @author esacsad
 * 
 * This class tests the PIConfiguration Option in SON Visualisation GUI.
 *
 */
public class PIConfigurationTest extends SonvisWebDriverBaseUnitTest{
	
	private final LaunchMenu launchMenu = new LaunchMenu();
	private final SonvisCommonUtils sonvisCommonUtils = new SonvisCommonUtils();
	private final CustomCalendarUtil customCalendarUtil = new CustomCalendarUtil();

	
	@Override
	public void tearDown()throws Exception{
		super.tearDown();
	}
	
	
	@Test
	public void testPIARNRelationAdded()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
	     assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.ANR_X2_RELATIONS_ADDED);	   
	     	     		
	}
	@Test
	public void testPIARNRelationRemoved()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
	     assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.ANR_X2_RELATIONS_REMOVED);
	     sleep(2000);
	}
	@Test
	public void testPCIChanged()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
	     assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.PCI_CHANGES);
	     sleep(2000);
	}
	@Test
	public void testTotalPCIConflicts()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
	     assertTrue("Network Activity tab should be selected by default", launchMenu.isTabSelected(Tab.NETWORK_ACTIVITY));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.TOTAL_PCI_CONFLICTS);
	     sleep(2000);
		
	}
	
	@Test
	public void testPIARNRelationAddedUsingRanking()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.RANKING);
	     assertTrue("Ranking tab should be selected by default", launchMenu.isTabSelected(Tab.RANKING));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.ANR_X2_RELATIONS_ADDED);
	     sleep(2000);
	     		
	}
	
	@Test
	public void testPIARNRelationRemovedUsingRanking()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.RANKING);
	     assertTrue("Ranking tab should be selected by default", launchMenu.isTabSelected(Tab.RANKING));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.ANR_X2_RELATIONS_REMOVED);
	     sleep(2000);
	}
	@Test
	public void testPCIChangedUsingRanking()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.RANKING);
	     assertTrue("Ranking tab should be selected by default", launchMenu.isTabSelected(Tab.RANKING));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.PCI_CHANGES);
	     sleep(2000);
	}
	@Test
	public void testTotalPCIConflictsUsingRanking()throws PopUpException, InterruptedException {
		 init();
	     sleep(2000);
	     webDriverSelenium.windowMaximize();
	     launchMenu.selectTab(Tab.RANKING);
	     assertTrue("Ranking tab should be selected by default", launchMenu.isTabSelected(Tab.RANKING));
	     assertEquals("No date should be selected by default", "--Select Date--", launchMenu.getSelectedDateOption());
	     assertFalse("Launch button should be disabled if configurations are not selected", launchMenu.isLaunchButtonEnabled());
	     customCalendarUtil.clickDateOption();
	     customCalendarUtil.clickCustomDateOption();
	     customCalendarUtil.chooseSONVisualisationUICustomDate();
	     sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForCustomDate(SonVisConstants.TOTAL_PCI_CONFLICTS);
	     sleep(2000);
		
	}
	
}
