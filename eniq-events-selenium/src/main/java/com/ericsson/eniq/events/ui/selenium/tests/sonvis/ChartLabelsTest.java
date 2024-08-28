package com.ericsson.eniq.events.ui.selenium.tests.sonvis;

/*
 * So far this tests that the two labels, left and righ ton the initial network activity chart are correct.
 * 
 * To implement: That graph disappears when right lable clicked. 
 * Test that tooltip gives right time and value
 * 
 */


import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.Charts;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.config.SonvisCommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch.LaunchMenu.Tab;
import org.junit.Test;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) ${year} LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

/**
 * @author efrfarr
 * @since June 2013
 * 
 */

public class ChartLabelsTest extends SonvisWebDriverBaseUnitTest{

	private final LaunchMenu launchMenu = new LaunchMenu();
	private final SonvisCommonUtils sonvisCommonUtils = new SonvisCommonUtils();

	//protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();
	
	private final Charts charts = new Charts();
		
	@Override
    public void tearDown() throws Exception {
    	// TODO Auto-generated method stub
    	super.tearDown();
    }
	
	 
	@Test
	public void testNetworkViewANRX2AddedRelationsChartLabels() throws PopUpException, InterruptedException {
		init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        //selenium.windowMaximize();
        
        //Set up launch menu, etc
        
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption("Custom", null, null);
        sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForToday(SonVisConstants.ANR_X2_RELATIONS_ADDED);
        //sleep(7000);
       						
        String relationsAddedLegendText =charts.getLabelText(SonVisConstants.LEGEND_LABEL);
        
        assertEquals("ANR Relations Added", relationsAddedLegendText);
        
        String relationsAddedYLabel =charts.getLabelText(SonVisConstants.Y_LABEL);
        
        assertEquals("ANR Relations Added", relationsAddedYLabel);
        
        String relationsAddedChartTitle =charts.getLabelText(SonVisConstants.CHART_TITLE);
        
        assertEquals( "Performance Indicator / Key Performance Indicator Activity View", relationsAddedChartTitle);

	}

	@Test
	public void testNetworkViewANRX2RemovedRelationsChartLabels() throws PopUpException, InterruptedException {
		init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        //selenium.windowMaximize();
        
        //Set up launch menu, etc
        
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption("Custom", null, null);
        sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForToday(SonVisConstants.ANR_X2_RELATIONS_REMOVED);
        //sleep(7000);
       						
        String relationsRemovedLegendText =charts.getLabelText(SonVisConstants.LEGEND_LABEL);
        
        assertEquals("ANR Relations Removed", relationsRemovedLegendText);
        
        
        String relationsRemovedYLabel =charts.getLabelText(SonVisConstants.Y_LABEL);
      
        assertEquals("ANR Relations Removed", relationsRemovedYLabel);
        
        String relationsRemovedChartTitle =charts.getLabelText(SonVisConstants.CHART_TITLE);
        
        assertEquals( "Performance Indicator / Key Performance Indicator Activity View", relationsRemovedChartTitle);

	}
	
	@Test
	public void testNetworkViewPCIChangesChartLabels() throws PopUpException, InterruptedException {
		init();
        sleep(2000);
        webDriverSelenium.windowMaximize();
        //selenium.windowMaximize();
        
        //Set up launch menu, etc
        
        launchMenu.selectTab(Tab.NETWORK_ACTIVITY);
        launchMenu.selectDateOption("Custom", null, null);
        sonvisCommonUtils.selectNetworkPIAndDisplyCellDetailsForToday(SonVisConstants.PCI_CHANGES);
        //sleep(7000);
       						
        String PCIChangesLegendText =charts.getLabelText(SonVisConstants.LEGEND_LABEL);
        
        assertEquals("PCI Changes", PCIChangesLegendText);
        
        String PCIChangesYLabel =charts.getLabelText(SonVisConstants.Y_LABEL);
        
        assertEquals("PCI Changes", PCIChangesYLabel);
        
        String PCIChangesChartTitle =charts.getLabelText(SonVisConstants.CHART_TITLE);
        
        assertEquals( "Performance Indicator / Key Performance Indicator Activity View", PCIChangesChartTitle);

	}
	
	
}
