package com.ericsson.eniq.events.ui.selenium.tests.sonvis.launch;

import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonVisConstants;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class LaunchMenu {
    protected WebDriver webDriver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    protected static final String DEFAULT_TIMEOUT = "20000";

    private static final String CONFIG_BUTTON = "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/button";

    private static final String LAUNCH_BUTTON = "html/body/div[4]/div/div[3]/div/div[2]/div[2]/button";

    private static final String LAUNCH_FOOTER = "html/body/div[4]/div/div[4]/span";

    private static final String DATE_DROPDOWN = "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[2]/div/table/tbody/tr/td[1]/div";
    
    private static final String PI_DROPDOWN = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr/td/div/div/div[2]/div/div/div/table/tbody/tr/td/div";
    
    private static final String CONFIG_UPDATE_BUTTON = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr[3]/td/table/tbody/tr/td/button";
    
    public static final String RANKING_ROW_TOP100CELLS = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[2]/div/div/div/div/div/div[2]/div/div/table/tbody/tr/td[2]";
    
    public static final String RANKING_ROW_TOP100CELLS_353 = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[2]/div/div/div/div/div/div[2]/div/div/table/tbody/tr/td[2]";
    
    public static final String RANKING_ROW_CGI = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/div/div[2]/div/div/div/div/div/div[2]/div/div/table/tbody/tr/td[3]";
   	
   	public static final String RANK_STARTDATE_20JUN = "/html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr[5]/td[4]";
   	
   	public static final String RANK_ENDDATE_25JUN = "/html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr/td[3]/div/table/tbody/tr[2]/td/table/tbody/tr[6]/td[2]";
   	                                                
   	private static final String CUSTOMDATE_OK_BUTTON = "/html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr[3]/td/div/button";
   	
   	private static final String RANKTABLE = ".//*[contains(@id,'RANKING_PANEL_GRID_SON_VIS')]/table/tbody/tr/td/";
   	
   	//private static final String ANR_RELATIONS_SELECTABLE_CELL = "ERBS35-3" ;
   	private static final String ANR_RELATIONS_SELECTABLE_CELL = "/html/body/div[4]/table/tbody/tr[2]/td/div/div[2]/div/div/table/tbody/tr/td/div/div[2]/div[2]/div/div/div/table/tbody/tr/td[2]/div" ;
   	
   	
   	//private static final String PCI_CHANGES_SELECTABLE_CELL = "ERBS39-2" ;
   	private static final String PCI_CHANGES_SELECTABLE_CELL = "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr/td/div/div/div[2]/div/div/div/div/div/div[3]" ;
   	
 
    public enum Tab {
        RANKING("Ranking"), NETWORK_ACTIVITY("Network Activity");
        private final String label;

        Tab(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }

    private String getTabPath(final Tab tab) {
        return "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[1]/table/tbody/tr/td[text()='" + tab.getLabel()
                + "']";
    }

    private String getDateDropdownOptionPath(final String type) {
        return "html/body/div[4]/div/div[3]/div/div[2]/div[1]/div/div[2]/div[1]/div/div/div[text()='" + type + "']";
    }
    
    

    public void selectTab(final Tab tab) {
        webDriver.findElement(By.xpath(getTabPath(tab))).click();
    }

    public void selectDateOption(final String type, final String startDate, final String endDate) {
    	
        webDriver.findElement(By.xpath(DATE_DROPDOWN)).click();
        webDriver.findElement(By.xpath(getDateDropdownOptionPath(type))).click();
        if (type.equalsIgnoreCase("Custom")) {
        	webDriver.findElement(By.xpath(RANK_STARTDATE_20JUN)).click();
        	webDriver.findElement(By.xpath("/html/body/div[5]/div/div/table/tbody/tr[2]/td/table/tbody/tr/td[3]/div/table/tbody/tr/td/table/tbody/tr/td/div/div")).click();
        	webDriver.findElement(By.xpath(RANK_ENDDATE_25JUN)).click();
        	webDriver.findElement(By.xpath(CUSTOMDATE_OK_BUTTON)).click();
        }
    }
    
    public void selectConfigPIKPIOption(final String pi) {
        webDriver.findElement(By.xpath(PI_DROPDOWN)).click();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       webDriver.findElement(By.xpath(getPIDropdownOptionPath(pi))).click();
    }
    
    public void selectMainPI(final String pi) {
        launchConfigurationWindow();
        selectConfigPIKPIOption(pi);
        clickConfigUpdate();
    }
    public void selectPeakPoint(String mainPI){
    	
    }
    
    
    private String getPIDropdownOptionPath(final String type) {
    	
        
    	if(type.equals(SonVisConstants.ANR_X2_RELATIONS_ADDED) || type.equals(SonVisConstants.ANR_X2_RELATIONS_REMOVED))
    		return "/html/body/div[4]/table/tbody/tr[2]/td/div/div/div/div/table/tbody/tr/td/div/div/div[2]/div/div/div/div/div/div";
    	else	
    		//webDriver.findElement(By.xpath(selectCell(PCI_CHANGES_SELECTABLE_CELL))).click();
    		return PCI_CHANGES_SELECTABLE_CELL;
    	
    	
    }
    


    public void launchConfigurationWindow() {
        webDriver.findElement(By.xpath(CONFIG_BUTTON)).click();
    }

    public boolean isTabSelected(final Tab tab) {
        return !webDriver.findElement(By.xpath(getTabPath(tab))).getAttribute("class").trim().isEmpty();
    }

    public boolean isConfigButtonEnabled() {
        return isEnabled(CONFIG_BUTTON);
    }

    public boolean isLaunchButtonEnabled() {
        return isEnabled(LAUNCH_BUTTON);
    }

    private boolean isEnabled(final String xPath) {
        return webDriver.findElement(By.xpath(xPath)).isEnabled();
    }

    public void clickLaunch() {
        webDriver.findElement(By.xpath(LAUNCH_BUTTON)).click();
    }

    public void clickConfigUpdate() {
        webDriver.findElement(By.xpath(CONFIG_UPDATE_BUTTON)).click();
    }
    
    public String getLaunchFooterText() {
        return webDriver.findElement(By.xpath(LAUNCH_FOOTER)).getText();
    }

    public String getSelectedDateOption() {
        return webDriver.findElement(By.xpath(DATE_DROPDOWN)).getText();
    }
    
    private String selectCell(String cellName) {
    	return RANKTABLE+"/div[text()='" + cellName + "']";
    }
    public void selectCGIRowFromTopCells(String mainPI){
    	if(mainPI.equals(SonVisConstants.ANR_X2_RELATIONS_ADDED) || mainPI.equals(SonVisConstants.ANR_X2_RELATIONS_REMOVED))
    	webDriver.findElement(By.xpath(selectCell(ANR_RELATIONS_SELECTABLE_CELL))).click();
    	else
    	webDriver.findElement(By.xpath(selectCell(PCI_CHANGES_SELECTABLE_CELL))).click();
    	
    }
    
    public void selectRankingGridFromCGITab(){
     	webDriver.findElement(By.xpath(RANKING_ROW_CGI)).click();
    }
    

}
