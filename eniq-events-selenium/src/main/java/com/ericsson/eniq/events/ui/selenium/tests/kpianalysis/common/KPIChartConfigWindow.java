package com.ericsson.eniq.events.ui.selenium.tests.kpianalysis.common;

import com.ericsson.eniq.events.ui.selenium.tests.webdriver.CommonUtils;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.NewEricssonSelenium;
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
public class KPIChartConfigWindow {

    private static final String SELECTED_KPI = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[1]/div[2]/div/table/tbody/tr/td[1]/div";

    private static final int COLUMS_MAX = 4;

    private static final int CONTROL_PLANE_ROW_MAX = 4;

    private static final int USER_PLANE_ROW_MAX =3;

    private WebDriver driver = NewEricssonSelenium.getSharedInstance().getWrappedDriver();

    public static final String KPI_SELECTOR = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[1]/div[2]/div[2]/table/tbody/tr/td[2]/div";

    public static final String CONTROL_PLANE_TITLE = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[2]/div[1]/span[1]";

    public static final String USER_PLANE_TITLE = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[2]/div[1]/span[1]";

    public static final String WINDOW_TITLE = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/table[1]/tbody/tr/td[2]/div/div[text()='Chart Configuration']";

    public static final String UPDATE_BUTTON = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[2]/button[text()='Update']";

    public static final String CANCEL_BUTTON = ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[2]/button[text()='Cancel']";

    public List<String> getSelectedControlPlaneLegends() {
        List<String> selectedLegends = new ArrayList<String>();
        for (int i = 1; i < CONTROL_PLANE_ROW_MAX; i++) {
            for (int j = 1; (j < COLUMS_MAX && !(i == 3 && j == 3)); j++) {
                String columnPath = getControlPlaneColumnPath(i, j);
                if (isLegendSelected(columnPath)) {
                    selectedLegends.add(getLegendText(columnPath));
                }
            }
        }
        return selectedLegends;
    }

    private String getControlPlaneColumnPath(int row, int column) {
        return ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[2]/div[2]/div/table/tbody/tr[" + row
                + "]/td[" + column + "]/span";
    }

    private String getUserPlaneColumnPath(int row, int column) {
        return ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[3]/div[2]/div/table/tbody/tr[" + row
                + "]/td[" + column + "]/span";
    }

    public List<String> getSelectedUserPlaneLegends() {
        List<String> selectedLegends = new ArrayList<String>();
        for (int i = 1; i < USER_PLANE_ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getUserPlaneColumnPath(i, j);
                if (isLegendSelected(columnPath)) {
                    selectedLegends.add(getLegendText(columnPath));
                }
            }
        }
        return selectedLegends;
    }

    public String getSelectedKpi() {
        return driver.findElement(By.xpath(SELECTED_KPI)).getText();
    }

    public void selectKpi(String kpi) throws InterruptedException {
        driver.findElement(By.xpath(KPI_SELECTOR)).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(getKpiOptionPath(kpi))).click();
        Thread.sleep(500);
    }

    private boolean isLegendEnabled(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/input")).isEnabled();
    }

    private boolean isLegendSelected(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/input")).isSelected();
    }

    private void selectLegend(String columnPath, boolean select) {
        if (!isLegendSelected(columnPath)) {
            driver.findElement(By.xpath(columnPath + "/input")).click();
        }
    }

    private String getLegendText(String columnPath) {
        return driver.findElement(By.xpath(columnPath + "/label")).getText();
    }

    public boolean isUserPlaneLegendEnabled(String legend) {
        for (int i = 1; i < USER_PLANE_ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getUserPlaneColumnPath(i, j);
                if (legend.equals(getLegendText(columnPath))) {
                    return isLegendEnabled(columnPath);
                }
            }
        }
        return false;
    }

    public boolean isControlPlaneLegendEnabled(String legend) {
        for (int i = 1; i < CONTROL_PLANE_ROW_MAX; i++) {
            for (int j = 1; (j < COLUMS_MAX && !(i == 3 && j == 3)); j++) {
                String columnPath = getControlPlaneColumnPath(i, j);
                if (legend.startsWith(getLegendText(columnPath))) {
                    return isLegendEnabled(columnPath);
                }
            }
        }
        return false;
    }

    public void selectControlPlaneLegends(boolean select, String... legends) {
        for (int i = 1; i < CONTROL_PLANE_ROW_MAX; i++) {
            for (int j = 1; (j < COLUMS_MAX && !(i == 3 && j == 3)); j++) {
                String columnPath = getControlPlaneColumnPath(i, j);
                String legendText = getLegendText(columnPath);
                for (String string : legends) {
                    if (string.equals(legendText)) {
                        selectLegend(columnPath, select);
                        break;
                    }
                }
            }
        }
    }

    public void selectUserPlaneLegends(boolean select, String... legends) {
        for (int i = 1; i < USER_PLANE_ROW_MAX; i++) {
            for (int j = 1; j < COLUMS_MAX; j++) {
                String columnPath = getUserPlaneColumnPath(i, j);
                for (String string : legends) {
                    if (string.equals(getLegendText(columnPath))) {
                        selectLegend(columnPath, select);
                    }
                }
            }
        }
    }

    public void cancel() throws InterruptedException {
        driver.findElement(By.xpath(CANCEL_BUTTON)).click();
        Thread.sleep(500);
    }

    public void update() throws InterruptedException {
        driver.findElement(By.xpath(UPDATE_BUTTON)).click();
        Thread.sleep(2000);
    }

    public boolean isWindowOpen() {
        return CommonUtils.isElementPresent(WINDOW_TITLE);
    }

    private String getKpiOptionPath(String kpi) {
        return ".//*[@id='KPI_ANALYSIS_BOUNDARY']/div[4]/div/div[1]/div/div[1]/div[1]/div[2]/div[1]/div/div/div[text()='"
                + kpi + "']";
    }
}
