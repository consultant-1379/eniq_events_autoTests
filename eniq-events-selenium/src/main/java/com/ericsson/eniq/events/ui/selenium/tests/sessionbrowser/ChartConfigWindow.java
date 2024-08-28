package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser;

import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.DetailTab.Chart;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SessionBrowserTab;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ChartConfigWindow {

    private static final int COLUMS_MAX = 3;

    private static final int ROW_MAX = 12;

    private WebDriver driver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    public static final String WINDOW_TITLE = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/table[1]/tbody/tr/td[2]/div/div[text()='Detail Graph Configurations']";

    public static final String UPDATE_BUTTON = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/button[text()='Update']";

    public static final String CANCEL_BUTTON = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[2]/button[text()='Cancel']";

    public List<String> getSelectedLegends(Chart chart) {
        List<String> selectedLegends = new ArrayList<String>();
        for (int i = 1; i < ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getColumnPath(chart, i, j);
                if (!isColumnPresent(columnPath)) {
                    return selectedLegends;
                }
                if (isOptionSelected(columnPath)) {
                    selectedLegends.add(getLegendText(columnPath));
                }
            }
        }
        return selectedLegends;
    }

    public String getSelectedDropdownLegend(Chart chart) {
        String selectedLegendPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[1]/div[1]/div/div/div["
                + chart.getIndex() + "]/div[2]/div/div/div[2]/div/table/tbody/tr/td[1]/div";
        return driver.findElement(By.xpath(selectedLegendPath)).getText();
    }

    public void selectDropdownLegend(Chart chart) throws InterruptedException {
        String dropdownButtonPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[1]/div[1]/div/div/div["
                + chart.getIndex() + "]/div[2]/div/div/div[2]/div/table/tbody/tr/td[2]/div";
        driver.findElement(By.xpath(dropdownButtonPath)).click();
        Thread.sleep(1000);
        String legendOptionPath = ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[1]/div[1]/div/div/div["
                + chart.getIndex() + "]/div[2]/div/div/div[2]/div[1]/div/div/div[1]";
        driver.findElement(By.xpath(legendOptionPath)).click();
        Thread.sleep(500);
    }

    public void selectTop3Option(boolean select) {
        String columnPath;
        if (select) {
            columnPath = ".//*[@id='Top Application Usage']";
        } else {
            columnPath = ".//*[@id='User Defined Selection']";
        }
        selectOption(columnPath, true);
    }

    private String getColumnPath(Chart chart, int row, int column) {
        return ".//*[@id='BOUNDARY_SESSION_BROWSER']/div[2]/div/div[1]/div/div[1]/div[1]/div/div/div["
                + chart.getIndex() + "]/div[2]/div/div/table/tbody/tr[" + row + "]/td[" + column + "]/span";
    }

    private boolean isLegendEnabled(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/input")).isEnabled();
    }

    private boolean isOptionSelected(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/input")).isSelected();
    }

    private boolean isColumnPresent(String columnPath) {
        return SessionBrowserTab.isElementPresent(columnPath);
    }

    private void selectOption(String columnPath, boolean select) {
        if ((!isOptionSelected(columnPath) && select) || (isOptionSelected(columnPath) && !select)) {
            driver.findElement(By.xpath(columnPath + "/input")).click();
        }
    }

    private String getLegendText(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/label")).getText();
    }

    public boolean isLegendEnabled(Chart chart, String legend) {
        for (int i = 1; i < ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getColumnPath(chart, i, j);
                if (!isColumnPresent(columnPath)) {
                    break;
                }
                if (legend.equals(getLegendText(columnPath))) {
                    return isLegendEnabled(columnPath);
                }
            }
        }
        return false;
    }

    public void selectLegends(Chart chart, boolean select, String... legends) {
        for (int i = 1; i < ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getColumnPath(chart, i, j);
                if (!isColumnPresent(columnPath)) {
                    return;
                }
                String legendText = getLegendText(columnPath);
                for (String string : legends) {
                    if (string.equals(legendText)) {
                        selectOption(columnPath, select);
                        break;
                    }
                }
            }
        }
    }

    public void cancel() throws InterruptedException {
        driver.findElement(By.xpath(CANCEL_BUTTON)).click();
        Thread.sleep(100);
    }

    public void update() throws InterruptedException {
        driver.findElement(By.xpath(UPDATE_BUTTON)).click();
        Thread.sleep(2000);
    }

    public boolean isWindowOpen() {
        return CommonUtils.isElementPresent(WINDOW_TITLE);
    }

}
