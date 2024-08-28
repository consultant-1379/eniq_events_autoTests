package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoMapException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class KPIAnalysisWindow {

    private WebDriver driver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    //All the XPaths...
    public static String WINDOW_TITLE = ".//*[@id='selenium_tag_FLOATWIN_TITLE'][1]";

    public static final String KPI_SELECTOR = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[2]/div";

    public static final String COG_WHEEL = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[2]/div";

    public static final String TIME_SELECTOR = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[2]/table/tbody/tr/td/div/div/table/tbody/tr/td[2]/div";

    public static final String CLOSE_BUTTON = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[1]/tbody/tr/td[4]/div";

    public static final String SELECTED_KPI = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[1]/div/div/table/tbody/tr/td[1]/div";

    public static final String MAP_NOT_INSTALLED = "//div[text()='Error reaching Map Server, if this issue persists, please contact your Administrator']";

    public static final String MAP_NO_DATA = "//div[text()='No data available for selected KPI']";
    
    public static final String MAP_LOADING = "//div[text()='Loading Map ...']";

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
    
    public void selectKpi(String kpi) throws InterruptedException {
        driver.findElement(By.xpath(KPI_SELECTOR)).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(getKpiOptionPath(kpi))).click();
        Thread.sleep(500);
    }

    public void selectDateOption(String option) throws InterruptedException {
        driver.findElement(By.xpath(TIME_SELECTOR)).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(getTimeOptionPath(option))).click();
    }

    public void selectDate(Date startDate, Date endDate) throws InterruptedException {
        CommonUtils.selectCustomDateTimeRange(startDate, endDate);
    }

    public String getSelectedKpi() {
        return driver.findElement(By.xpath(SELECTED_KPI)).getText();
    }

    public String getWindowTitle() {
        return driver.findElement(By.xpath(WINDOW_TITLE)).getText();
    }

    public void openConfigurationWindow() throws InterruptedException {
        driver.findElement(By.xpath(COG_WHEEL)).click();
        Thread.sleep(2000);
    }
    
    public WebElement getElement(String xpath){
		return driver.findElement(By.xpath(xpath));
    }
    
    public List<WebElement> getMulipleElements(String xpath){
		return driver.findElements(By.xpath(xpath));
    }

    public String getKpiOptionPath(String kpi) {
        return ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[1]/table/tbody/tr/td[1]/div/div[1]/div/div/div[text()='"
                + kpi + "']";
    }

    public String getTimeOptionPath(String timeOption) {
        return ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/table[2]/tbody/tr/td[2]/table/tbody/tr/td/div/div[1]/div/div/div[text()='"
                + timeOption + "']";
    }

    public boolean isWindowOpen() {
        return isElementPresent(WINDOW_TITLE);
    }

    public boolean isKPIWindowOpen(){
        boolean isWindowOpen = true;
        if(driver.findElement(By.xpath(WINDOW_TITLE)) == null){
            isWindowOpen = false;
        }
        return isWindowOpen;
    }

    public boolean isWindowOpen(final String windowTitle){
        boolean isWindowOpen = true;
        if(driver.findElement(By.xpath(WINDOW_TITLE)) == null){
            isWindowOpen = false;
        }
        return isWindowOpen;
    }
    
    
    public void close() throws InterruptedException {
        driver.findElement(By.xpath(CLOSE_BUTTON)).click();
        Thread.sleep(500);
    }

    public boolean isControlPlaneLegendPresent(String legend) {
    	
    	String xPath = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[1]/div[1]/div//node()[text()='"
        + legend + "']";

        try {
			return driver.findElement(By.xpath(xPath)) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
    }

    public boolean isChartLegendItemPresent(final String legend){
        boolean success = false;
        String xPath = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[1]/div[1]/div/div/div/*[name()='svg']/*[name()='g'][@class='highcharts-legend']//node()[text()='"+
                legend+"']";
        
        try{
            success = driver.findElement(By.xpath(xPath)) != null;
        } catch (NoSuchElementException e){
            success = false;
        }

        return success;
    }
    
    public List<String> listChartLegendItems(){
        String xPath = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[1]/div[1]/div/div/div/*[name()='svg']/*[name()='g'][@class='highcharts-legend']/*[name()='text']/*[name()='tspan']";
        List<String> legendItems = new ArrayList<String>();

        try{
            List<WebElement> legendWebItems = driver.findElements(By.xpath(xPath));
            for (WebElement webElement : legendWebItems){
                legendItems.add(webElement.getText());
            }
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
        return legendItems;
    }
    
    public boolean isControlPlaneChartPresent() {
        return isElementPresent(".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[1]/div[1]/div//node()[text()='Control Plane KPIs']");
    }

    public String waitForWindowToLoad(String message) throws InterruptedException {
    	String mapError = "//div[text()='Service Error']";
        do {
            Thread.sleep(1000);
            if(isElementPresent(mapError)){
            	logger.log(Level.INFO, "Service error found ...");
            	break;
            }
        } while (isElementPresent("//div[text()='" + message + "']"));
		return message;
    }

    public void waitForMapToLoad() throws InterruptedException, NoDataException, NoMapException {
        do{
            Thread.sleep(1000);
            if(isElementPresent(this.MAP_NO_DATA)){
                throw new NoDataException("No data available for the Map");
            }if(isElementPresent(this.MAP_NOT_INSTALLED)){
                throw new NoMapException("");
            }
        }while(isElementPresent(this.MAP_LOADING));
    }

    /**
     * Use this utility to test if the map is installed. If the map isn't installed then the associated
     * test should be failed with this reason.
     * @return
     */
    public boolean isMapInstalled(){
        //== null? false:true
        return isElementPresent(this.MAP_NOT_INSTALLED)==true? false:true;
//        return isMap;
//        boolean mapInstalled = true;
//        if(isElementPresent(this.MAP_NOT_INSTALLED)){
//            mapInstalled = false;
//        }
//        return mapInstalled;
    }

    /**
     * Use this utility to test if the map has data. If the map doesn't have data then the associated
     * test should be failed with this reason.
     * @return
     */
    public boolean isMapwithData(){
        boolean mapWithData = true;
        if(isElementPresent(this.MAP_NO_DATA)){
            mapWithData = false;
        }
        return mapWithData;
    }


    private boolean isElementPresent(String xPath) {
        try {
            return driver.findElement(By.xpath(xPath)) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickPointOnChart(int pointNumber) throws NoDataException{
        pointNumber = pointNumber + 4; //the chart has 4 points at the start that are not displayed.
        String xPath = ".//*[name()='svg']/*[name()='g'][@class='highcharts-series-group']/*[name()='g'][last()]/*[name()='path']["+pointNumber+"]";
        try{
            WebElement svgPath = driver.findElement(By.xpath(xPath));
            Actions builder = new Actions(driver);
            builder.click(svgPath).build().perform();
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
            throw new NoDataException("No data for KPI");
        }catch (ElementNotVisibleException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
            throw new NoDataException("No data for KPI");
        }
    }

    public WebElement getChartByOptions() {
        String xPath = ".//*[@id='selenium_tag_DrillDialog']";
        
        WebElement chartByOptionsDialog = null;
        try{
            chartByOptionsDialog = driver.findElement(By.xpath(xPath));
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
        return chartByOptionsDialog;
    }

    public List<String> getChartByOptionsLinkNames() {
        String xPath = ".//*[@id='selenium_tag_DrillDialog_link']";

        List<WebElement> chartByOptionsDialogLinks = null;
        List<String> names = new ArrayList<String>();
        try{
            chartByOptionsDialogLinks = driver.findElements(By.xpath(xPath));
            for (WebElement webElement: chartByOptionsDialogLinks){
                names.add(webElement.getText());
            }
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
        return names;
    }

    /**
     * Click on Drill By Option popup menu. Provide the link number (1 = TAC, 2= Make, etc..)
     * Clicking on this popup menu will open the Chart By Chart in a floating window.
     * @param linkNumber
     */
    public void clickDrillByOption(int linkNumber) {
        String xPath = ".//*[@id='selenium_tag_DrillDialog_link']["+linkNumber+"]";

        try{
            driver.findElement(By.xpath(xPath)).click();
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
    }

    /**
     * Get the title of a floating window.
     * @param windowNumber
     * @return
     */
    public String getFloatingWindowTitle(int windowNumber) {
        String title = null;
        String xPath = ".//*[@id='selenium_tag_FLOATWIN']["+windowNumber+"]//*[@id='selenium_tag_FLOATWIN_TITLE']";

        try{
           title = driver.findElement(By.xpath(xPath)).getText();
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
        return title;
    }

    public String getChartTitle(int windowNumber){
        String title = null;
        String xPath = ".//*[@id='selenium_tag_FLOATWIN']["+windowNumber+"]//*[@id='selenium_tag_FLOATWIN_BODY']//*[node()=div]/*[contains(@*, 'highcharts-container')]/*[name()='svg']/*[name()='text'][@class='highcharts-title']/*[name()='tspan'][1]";

        try{
            title = driver.findElement(By.xpath(xPath)).getText();
        }catch (NoSuchElementException e){
            logger.log(Level.INFO, e.getLocalizedMessage());
        }
        return title;
    }
    
    
    public void chartByTAC(int pointNumber) throws NoDataException{
        //Select the last point on the chart...
        try{
            clickPointOnChart(pointNumber);
            //select Chart By TAC from the popup...
            clickDrillByOption(1);
        }catch (NoDataException e){
            throw e;
        }
    }

    public void chartByMake(int pointNumber) throws NoDataException{
        //Select the last point on the chart...
        try{
            clickPointOnChart(pointNumber);
            //select Chart By Make from the popup...
            clickDrillByOption(2);
        }catch (NoDataException e){
            throw e;
        }
    }

    public void chartByMakeModel(int pointNumber) throws NoDataException{
        //Select the last point on the chart...
        try{
            clickPointOnChart(pointNumber);
            //select Chart By Make-Make from the popup...
            clickDrillByOption(3);
        }catch (NoDataException e){
            throw e;
        }
    }

}
