package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class DetailTab {

    private WebDriver driver;

    private ChartConfigWindow chartConfigWindow = new ChartConfigWindow();

    enum Chart {
        RADIO_CHART(1), TCP_CHART(2), APPLICATION_CHART(3);
        int index;

        Chart(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public DetailTab(WebDriver driver) {
        this.driver = driver;
    }

    public void selectLegend(Chart chart, String legend) throws InterruptedException {
        driver.findElement(By.xpath(getDropDownPath(chart))).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(getLegendOptionPath(chart, legend))).click();
        Thread.sleep(500);
    }

    public String getSelectedLegend(Chart chart) {
        String selectedLegendPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + chart.getIndex()
                + "]/div/div/div[2]/div/div[1]/div/table/tbody/tr/td[1]/div";
        return driver.findElement(By.xpath(selectedLegendPath)).getText();
    }

    public List<String> getMasterChartLegend(Chart chart) {
        String selectedLegendPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Master')]//div[@class='highcharts-container']//descendant::*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']//*[@class='highcharts-title']/descendant::*[local-name()='tspan']";
        List<WebElement> elements = driver.findElements(By.xpath(selectedLegendPath));
        List<String> legend = new ArrayList<String>();
        for (WebElement webElement : elements) {
            legend.add(webElement.getText());
        }
        return legend;
    }

    public void openConfigurationWindow(Chart chart) throws InterruptedException {
        String cogWheelPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + chart.getIndex()
                + "]/div/div/div[2]/div/div[2]";
        driver.findElement(By.xpath(cogWheelPath)).click();
        Thread.sleep(2000);
    }

    public ChartConfigWindow getChartConfigWindow() {
        return chartConfigWindow;
    }

    private String getLegendOptionPath(Chart chart, String legend) {
        return ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + chart.getIndex()
                + "]/div/div/div[2]/div/div[1]/div[1]/div/div/div[text()='" + legend + "']";
    }

    public boolean isLegendPresent(Chart chart, String legend) {
        String legendPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["+chart.getIndex()+"]//*[@class='highcharts-legend']/descendant::*[local-name()='tspan'][text()='"
                + legend + "']";
        	 return legend.equals(driver.findElement(By.xpath(legendPath)).getText());
        }
    
    public boolean isLegendPresentInChart(Chart chart, String legend) {
			try {
				String legendPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["+chart.getIndex()+"]//*[@class='highcharts-legend']/descendant::*[local-name()='tspan'][text()='"
		                + legend + "']";		
				boolean present = NewEricssonSelenium.getSharedInstance()
						.getWrappedDriver().findElement(By.xpath(legendPath)) != null;		
				return present;
			} catch (final NoSuchElementException e) {	
				return false;
			}
	}

    public boolean isChartPresent(Chart chart, String chartTitle) {
    	String chartTitleXPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
				+ chart.getIndex()
				+ "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//*[@class='highcharts-title']";

        return chartTitle.equals(driver.findElement(By.xpath(chartTitleXPath)).getText());
    }

    public String prepareChartTitle(String imsi, String chartName, String dateFrom, String dateTo) {
        return chartName + ", " + imsi + ", " + dateFrom + " - " + dateTo;
    }

    private String getDropDownPath(Chart chart) {
        return ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + chart.getIndex()
                + "]/div/div/div[2]/div/div[1]/div/table/tbody/tr/td[2]/div";
    }

    public List<String> getLegendsFromChart(Chart chart) {
        String legendsPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div["
                + chart.getIndex()
                + "]//div[contains(@id,'Detail')]//div[@class='highcharts-container']//descendant::*[local-name()='svg' and namespace-uri()='http://www.w3.org/2000/svg']//*[@class='highcharts-legend']/descendant::*[local-name()='tspan']";
        List<WebElement> elements = driver.findElements(By.xpath(legendsPath));
        List<String> legends = new ArrayList<String>();
        for (WebElement webElement : elements) {
            legends.add(webElement.getText());
        }
        return legends;
    }

    public void selectRandomOptionInDropDown(Chart chart) throws InterruptedException {
        driver.findElement(By.xpath(getDropDownPath(chart))).click();
        Thread.sleep(1000);
        String allOptions = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div/div[2]/div[" + chart.getIndex()
                + "]/div/div/div[2]/div/div[1]/div[1]/div/div/div";
        List<WebElement> elements = driver.findElements(By.xpath(allOptions));
        driver.findElement(By.xpath(allOptions + "[" + CommonUtils.getRandomNumber(1, elements.size()) + "]")).click();
        Thread.sleep(500);
    }

    public void waitForWindowToLoad(String message) throws InterruptedException {
        do {
            Thread.sleep(1000);
        } while (CommonUtils.isElementPresent("//div[text()='" + message + "']"));
    }
}
