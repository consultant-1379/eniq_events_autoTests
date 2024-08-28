package com.ericsson.eniq.events.ui.selenium.tests.webdriver;
/**
 * Created by IntelliJ IDEA.
 * User: edivkir
 * Date: 18/10/12
 * Time: 13:09
 * To change this template use File | Settings | File Templates.
 */

import com.ericsson.eniq.events.ui.selenium.common.SeleniumUtils;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.events.tabs.Tab;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants.*;

@Component
public class Workspace extends Tab {



    private static WebDriver driver;

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public Workspace()
    {
        super(TAB_NAME);
    }
    
    public void openTab()
    {
        webDriverSelenium = NewEricssonSelenium.getSharedInstance();
        driver = webDriverSelenium.getWrappedDriver();
    }

    /** Waits for an element to be present */
    public void waitForElementToBePresent(final String locator, final String timeout)
    {
    	openTab();
        if (!webDriverSelenium.isElementPresent(locator)) {
            webDriverSelenium.waitForCondition("var value = selenium.isElementPresent('" + locator.replace("'", "\\'") + "'); value == true", timeout);
        }
        loggerDuplicate.log(Level.INFO, "The Element ID : " + locator);

    }
    public void waitForElementToBePresent(final String locator){
        waitForElementToBePresent(locator, DEFAULT_TIMEOUT);
    }

    public void openNewWorkspace()
    {
        /* String tabName = TAB_NAME;  tabButtonXPath
        String tabButtonXPath = "//li[contains(@id,'" + tabName + "')]/a[@class='x-tab-right']";
        loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
        waitForElementToBePresent(tabButtonXPath, DEFAULT_TIMEOUT);
        webDriverSelenium.click(tabButtonXPath);
        */
    }

    public void checkAndOpenSideLaunchBar(){
        //check if Side Launch Bar is closed
    	openTab();
        boolean sideLaunchBarClosed = webDriverSelenium.isElementPresent(SeleniumConstants.SIDE_LAUNCH_BAR_OPEN);

        if (sideLaunchBarClosed){
            driver.findElement(By.xpath(SeleniumConstants.SIDE_LAUNCH_BAR_OPEN)).click();
            loggerDuplicate.log(Level.INFO, "Opening the Side Launch Bar");
        }
        else{
            loggerDuplicate.log(Level.INFO, "Side Launch Bar is already opened");
        }
    }


    public void selectTimeRange(){
        selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);
    }

    /**
     *
     * @param timeRange - xpath of the time range(15,30, etc... found in SeleniumConstants.java)
     */
    public void selectTimeRange(String timeRange)
    {
    	openTab();
        waitForElementToBePresent(TIME_RANGE_ELEMENT_XPATH, DEFAULT_TIMEOUT);
        webDriverSelenium.click(TIME_RANGE_ELEMENT_XPATH);
        webDriverSelenium.click(timeRange);
    }

    public void selectDimension() throws InterruptedException {

        selectDimension(SeleniumConstants.CORE_NETWORK_CS);

    }
    /**
     *
     * @param dimension - xpath of the dimension value(2G Radio Network,3G Radio Network,APN,Controller, etc...)
     */
    public void selectDimension(String dimension) throws InterruptedException {
        Thread.sleep(3000);
        openTab();
        driver.findElement(SeleniumUtils.getElementByXpath(DIMENSION_ELEMENT_XPATH)).click();
        webDriverSelenium.click(dimension);
    }

    /**
     *
     * @param dimensionString - string to be entered in the dimension text area (BSC1 Ericsson GSM ,etc ... )
     */
    public void enterDimensionValue(String dimensionString)
    {
        webDriverSelenium.typeKeys(DIMENSION_TEXTFIELD_ID, dimensionString);
        waitForElementToBePresent(DIMENTION_POP_UP_MENU, DEFAULT_TIMEOUT);
        webDriverSelenium.click("//td[contains(text(),'"+dimensionString+"')]");
    }

    /**
     *
     * @param dimensionString - string to be entered in the dimension text area (BSC1 Ericsson GSM ,etc ... )
     */
    public void enterDimensionValueForGroups(String dimensionString)
    {
    	openTab();
        webDriverSelenium.typeKeys(DIMENSION_GROUP_TEXTFIELD_ID, dimensionString);
        waitForElementToBePresent("//*[@class='popupContent']",DEFAULT_TIMEOUT);
        webDriverSelenium.click("//div[contains(text(),'" + dimensionString + "')][1]");
    }

    /**
     *
     * @param filterValue - text to be entered into the filter text box
     */
    public void enterWindowFilter(String filterValue){
    	openTab();
        webDriverSelenium.type(WINDOW_FILTER_ID, filterValue);
    }

    /**
     *
     * @param category - selected category from categoryPanel
     * @param windowOption - selected category from categoryPanel

     */
    public void selectWindowType(String category, String windowOption) {
    	openTab();
        int categories = (Integer)selenium.getXpathCount(DIMENSION_CATEGORY);
        for(int i=1; i<categories;i++){
            if(selenium.isElementPresent(DIMENSION_CATEGORY+"["+i+"]//div[span[contains(text(), '"+category+"')]]")){
                selenium.click(DIMENSION_CATEGORY+"["+i+"]//div[span[contains(text(), '"+category+"')]]");
                selenium.click(DIMENSION_CATEGORY+"["+i+"]/div[2]/div/div/div//div[contains(text(), '"+windowOption+"')]");
            }
        }
    }

    public void clickLaunchButton(){
    	openTab();
        webDriverSelenium.click(LAUNCH_BUTTON_ID);
    }





    public void closeWindow(String currentWindow)
    {
    	openTab();
        webDriverSelenium.mouseDown(currentWindow);
        webDriverSelenium.click("//div[contains(@class, 'x-nodrag x-tool-close x-tool')]");
    }

    public void launchWindow(String time, String dimension, String demVal,String windowOption, String categoryPanel) throws InterruptedException {
        selectTimeRange(time);
        selectDimension(dimension);
        openTab();
        if(webDriverSelenium.isElementPresent(DIMENSION_GROUP_TEXTFIELD_ID)){
            Thread.sleep(3000);
            enterDimensionValueForGroups(demVal);
        } else{
            enterDimensionValue(demVal);
        }

        enterWindowFilter(windowOption);
        selectWindowType(categoryPanel, windowOption);
        clickLaunchButton();
    }
    public void launchWindow(String dimension, String demVal,String windowOption, String categoryPanel) throws InterruptedException {
        launchWindow(SeleniumConstants.DATE_TIME_30, dimension, demVal, windowOption, categoryPanel);
    }

    @Override
    protected void clickStartMenu() {
        //To change body of implemented methods use File | Settings | File Templates.
    }






}