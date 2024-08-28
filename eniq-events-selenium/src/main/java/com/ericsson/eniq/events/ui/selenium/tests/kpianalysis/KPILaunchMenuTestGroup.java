/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis;

import com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common.KpiConstants;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.EniqEventsWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.KpiAnalysisTab;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class KPILaunchMenuTestGroup extends EniqEventsWebDriverBaseUnitTest {

    @Autowired
    private KpiAnalysisTab kpiAnalysisTab;

    /**
     * US: DEFTFTS-1103
     * Test Case: 26.4
     * Ensure that for KPI Analysis Drop down menu for ‘Select Time Range’ 
     * that the range of times are 15mins, 30 mins, 1 hour, 2 hours, 6 hours and 12 hours
     * 
     * @throws InterruptedException
     */
    @Test
    public void checkContentsOfDateTimeDropdown_26_4() throws InterruptedException {
        //Click on KPI TAB.
        kpiAnalysisTab.openTab();
        //check the default selected date/time is 15 minutes...
        String defaultTimeDate = driver.findElement(By.xpath(KpiConstants.DATE_TIME_SELECTED)).getText();
        assertEquals("15 minutes", defaultTimeDate);

        //click the Date Time dropdown
        driver.findElement(By.xpath(KpiConstants.DATE_TIME_DROPDOWN_LAUNCH)).click();
        Thread.sleep(1000);
        //check the order of the entries in the date/time dropdown.
        assertEquals("15 minutes", driver.findElement(By.xpath(KpiConstants.DATE_TIME_15)).getText());
        assertEquals("30 minutes", driver.findElement(By.xpath(KpiConstants.DATE_TIME_30)).getText());
        assertEquals("1 hour", driver.findElement(By.xpath(KpiConstants.DATE_TIME_1HOUR)).getText());
        assertEquals("2 hours", driver.findElement(By.xpath(KpiConstants.DATE_TIME_2HOUR)).getText());
        assertEquals("6 hours", driver.findElement(By.xpath(KpiConstants.DATE_TIME_6HOUR)).getText());
        assertEquals("12 hours", driver.findElement(By.xpath(KpiConstants.DATE_TIME_12HOUR)).getText());
        assertEquals("Custom Time", driver.findElement(By.xpath(KpiConstants.DATE_TIME_CUSTOM)).getText());

    }

    /**
     * US: DEFTFTS-1103
     * Test Case: 26.5
     */
    @Test
	public void checkSelectNetworkOption_26_5() {
	    //Click on KPI TAB.
	    kpiAnalysisTab.openTab();
	
	    //check the default selected Kpi Type is Network...
	    assertEquals("Network", driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_SELECTED)).getText());
	
	    //click the Kpi Type dropdown
	    driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_LAUNCH)).click();
	
	    //select Controller...
	    driver.findElement(By.xpath(KpiConstants.NETWORK)).click();
	
	    //Check Controller (RNC) field is disabled.
	    boolean isEnabled = driver.findElement(By.xpath(KpiConstants.CONTROLLER_INPUT)).isEnabled();
	    assertFalse("The Controller (RNC) field should be DISABLED.", isEnabled);
	
	    //Check Cell field is disabled.
	    isEnabled = driver.findElement(By.xpath(KpiConstants.CELL_INPUT)).isEnabled();
	    assertFalse("The Cell field should be DISABLED.", isEnabled);
	
	    //Check if update button is enabled.
	    WebElement updateButton = driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON));
	    assertTrue("The update button should be ENABLED.", updateButton.isEnabled());
	}


	/**
     * US: DEFTFTS-1103
     * Test Case: 26.6
     * Ensure that for KPI Analysis Drop down menu for ‘Select Controller’ that when enabled options 
     * are available to select or enter RNC’s
     * @throws InterruptedException
     */
    @Test
    public void checkSelectControllerOption_26_6() throws InterruptedException {
        //Click on KPI TAB.
        kpiAnalysisTab.openTab();
        
    

        String kpi = "smartone_R:RNC09:RNC09,Ericsson,3G";
        
 

        //check the default selected Kpi Type is Network...
        assertEquals("Network", driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_SELECTED)).getText());

        //click the Kpi Type dropdown
        driver.findElement(By.xpath(KpiConstants.KPI_TYPE_DROPDOWN_LAUNCH)).click();

        //check the order of the entries in the kpi type dropdown.
        assertEquals("Network", driver.findElement(By.xpath(KpiConstants.NETWORK)).getText());
        assertEquals("Controller", driver.findElement(By.xpath(KpiConstants.CONTROLLER)).getText());

        //select Controller...
        driver.findElement(By.xpath(KpiConstants.CONTROLLER)).click();

        //Check Controller (RNC) field is ENABLED
        boolean isEnabled = driver.findElement(By.xpath(KpiConstants.CONTROLLER_INPUT)).isEnabled();
        assertTrue("The Controller (RNC) field should be ENABLED.", isEnabled);

        //Check Cell field is disabled.
        isEnabled = driver.findElement(By.xpath(KpiConstants.CELL_INPUT)).isEnabled();
        assertFalse("The Cell field should be DISABLED.", isEnabled);

        //Check if update button is disabled.
        WebElement updateButton = driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON));
        assertFalse("The update button is enabled, but it should be disabled.", updateButton.isEnabled());

        //Click the controller dropdown.
        kpiAnalysisTab.liveloadControllerDropdown(KpiConstants.CONTROLLER_DROPDOWN_LAUNCH, kpi);
        Thread.sleep(2000);
        //Check Cell field is disabled.

        isEnabled = driver.findElement(By.xpath(KpiConstants.CELL_INPUT)).isEnabled();
        assertTrue("The Cell field should be ENABLED.", isEnabled);
        //Check if update button is disabled.
        updateButton = driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON));
        assertFalse("The update button should be DISABLED.", updateButton.isEnabled());

        //Click the cell dropdown
        kpiAnalysisTab.liveloadDropdownClick(KpiConstants.CELL_DROPDOWN_LAUNCH, 1);
        Thread.sleep(1000);

        //Check if update button is enabled.
        updateButton = driver.findElement(By.xpath(KpiConstants.UPDATE_BUTTON));
        assertTrue("The update button should be ENABLED.", updateButton.isEnabled());
    }
}
