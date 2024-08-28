package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author edarbla
 * @since November 2012
 */
public class KPIAnalysisThresholdWindow {

    private WebDriver driver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    //Main window
    public final static String KPI_THRESHOLD_CONFIG = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div[2]/div";

//    public final static String TITLE = "//div[@style='width: 100%;']/div[contains(@class,'gwt-Label') and contains(text(), 'Service Request')]";//KPI_THRESHOLD_CONFIG + "/div[1]/div/div[1]/div";
    public final static String TITLE = "//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[1]";
    public final static String UPDATE = KPI_THRESHOLD_CONFIG + "//button[text()='Update']";

    public final static String CANCEL = KPI_THRESHOLD_CONFIG + "//button[text()='Cancel']";

    public final static String COLOURED_BOXES = KPI_THRESHOLD_CONFIG + "/div[1]/div/div[2]//td/div/div[1]";

    public final static String ERROR_MSG = "//div[contains(@class,'gwt-Label') and contains(text(), 'Threshold')]";

    public final static String UNITS = KPI_THRESHOLD_CONFIG + "/div[1]/div/div[2]/div[1]";


    public String getTitle() throws InterruptedException {
        String title = driver.findElement(By.xpath(TITLE)).getText();
        return title;
    }


    public List<String> getThresholdWindowPercentages(int... percentages){

        List<String> pValues = new ArrayList<String>();
        for(int i = 0; i < percentages.length; i++){
            pValues.add(driver.findElement(By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div[1]/div[2]/div/div[1]/div/div[2]//td[" + percentages[i] + "]")).getText());
        }

        return pValues;
    }

    /*
    * Click Update in Window
    * */
    public void update() throws InterruptedException {
        driver.findElement(By.xpath(UPDATE)).click();
        Thread.sleep(2000);
    }

    /*
    * Click Cancel in Window
    * */
    public void cancel() throws InterruptedException {
        driver.findElement(By.xpath(CANCEL)).click();
        Thread.sleep(2000);
    }

    /*
    * enter text into Threshold Window text boxes, and hits "enter"
    * */
    public void setText(int textFieldNumber, String text){
        String inputbox = KPI_THRESHOLD_CONFIG + "/div[1]/div/div[2]//td[" + textFieldNumber + "]//input";
        driver.findElement(By.xpath(inputbox)).clear();         // clear text field
        driver.findElement(By.xpath(inputbox)).sendKeys(text);  // enter text

    }

    public String getText(int textFieldNumber){
        String inputbox = KPI_THRESHOLD_CONFIG + "/div[1]/div/div[2]//td[" + textFieldNumber + "]//input";
        driver.findElement(By.xpath(inputbox)).clear();
        return driver.findElement(By.xpath(inputbox)).getText();
    }

    public void clickTextField(int textFieldNumber){
        String inputbox = KPI_THRESHOLD_CONFIG + "/div[1]/div/div[2]//td[" + textFieldNumber + "]//input";
        driver.findElement(By.xpath(inputbox)).click();
    }

    /**
     * retrieves order of coloured boxes
     *
     * @return ordered list of background colours
     */
    public ArrayList<String> getColouredOrder(){
        ArrayList<String> order = new ArrayList<String>();
        List<WebElement> boxes = driver.findElements(By.xpath(COLOURED_BOXES));

        for(int i=1; i < boxes.size(); i++){
            order.add(boxes.get(i).getAttribute("style").split("background-color: ")[1]);
        }
        return order;
    }

    public String getErrorMessage(){
        return driver.findElement(By.xpath(ERROR_MSG)).getText();
    }

    public String getUnits(){
        return driver.findElement(By.xpath(UNITS)).getText();
    }
}