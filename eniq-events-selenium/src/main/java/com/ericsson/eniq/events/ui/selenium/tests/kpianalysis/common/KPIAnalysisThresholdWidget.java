package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author edarbla
 * @since November 2012
 */
public class KPIAnalysisThresholdWidget {

    private WebDriver driver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    public void openThresholdConfigurationWindow(int kpiType) throws InterruptedException {
        if (kpiType == 1) {  // Percentages
            driver.findElement(By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[11]")).click();
        } else if (kpiType == 2) { // Milliseconds
            driver.findElement(By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[10]")).click();
        }
        Thread.sleep(2000);
    }

    public List<String> getThresholdWidgetPercentages(int kpiType, int... percentages) {
        List<String> pValues = new ArrayList<String>();
        for (int percentage : percentages) {
            if (kpiType == 1) { // Percentages
                pValues.add(driver.findElement(By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/div[" + percentage + "]/div[1]")).getText());
            } else if (kpiType == 2) { // Milliseconds
                pValues.add(driver.findElement(By.xpath("//div[@id='KPI_ANALYSIS_BOUNDARY']/div[3]/div/div[1]/div/div[3]/div/div[1]/div/div[2]/div[2]/div/div[" + percentage + "]/div[1]")).getText());
            }
        }
        return pValues;
    }

    public String checkInnerHTML(String html) {
        // Need to find all 4 instances of "checked="xx"", use regex?
        int i = html.indexOf("checked=");
        return html.substring(i + 9, i + 11);
    }
    
    public boolean getThresholdState(int index, WebElement e) {
		WebElement offsetElement = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].offsetParent;", e);
	    String innerHTML = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", offsetElement);
	    
	    String[] values = innerHTML.split("checked=");
	    
	    String checkValue = values[index].substring(1,3);
	    
	    return checkValue.equals("on");
	}
}