package com.ericsson.eniq.events.ui.selenium.tests.alarmkpi;

import com.ericsson.eniq.events.ui.selenium.events.elements.TimeCandidates;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.RankingsTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author ekrchai
 *
 */

public class DefaultDataLibrary extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    public NetworkTab networkTab;

    @Autowired
    public TerminalTab terminalTab;

    @Autowired
    public SubscriberTab subscriberTab;

    @Autowired
    public RankingsTab rankingsTab;

    @Autowired
    public CommonWindow kpiCriticalWindow;

    @Autowired
    public CommonWindow kpiMajorWindow;

    @Autowired
    public CommonWindow kpiMinorWindow;

    @Autowired
    public CommonWindow kpiWarningWindow;

    static Logger logger = Logger.getLogger(DefaultDataLibrary.class.getName());

    private String buttonXpath = null;
    private String buttonName = null;
    private String severityType = null;
    private String windowName = null;
    private String windowType = null;

    public static final String DEFAULT_TIMEOUT = "20000";

    public final String panelXpath = "//div[@id='kpiPanel']";

    public static final String settingsButtonXpath = "//div[@id='kpiPanel']//img[@id='configImage']";

    public static final String refreshButtonXpath = "//*[starts-with(@id,'x-auto')]/tbody/tr[2]/td[2]/em/button/img[contains(@style,'-322px')]";

    public static final String firstPageButtonXpath = "//*[@id='x-auto-52']//tr[2]/td[@class='x-btn-mc']/em/button[@class='x-btn-text']/img";

    public static final String lastPageButtonXpath = "//*[@id='x-auto-60']//tr[2]/td[@class='x-btn-mc']/em/button[@class='x-btn-text']/img";

    public static final String previousPageButtonXpath = "//*[@id='x-auto-53']//tr[2]/td[@class='x-btn-mc']/em/button[@class='x-btn-text']/img";

    public static final String nextPageButtonXpath = "//*[@id='x-auto-59']//tr[2]/td[@class='x-btn-mc']/em/button[@class='x-btn-text']/img";

    public static final String pageTextBoxXpath = "//*[@id='x-auto-34']/table/tbody/tr/td[1]/table/tbody/tr/td[5]";

    public static final String pageCountXpath = "//*[@id='x-auto-56']";

    public static final String pagingDisplayXpath = "//*[@id='x-auto-34']/table/tbody/tr/td[2]/table/tbody/tr/td[1]/table/tbody/tr/td[1]";

    /* These values are commented because of new workspace management-- xdivsig
     public static final String refreshButtonXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[11]//button";

    public static final String firstPageButtonXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[1]//button";

    public static final String lastPageButtonXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[9]//button";

    public static final String previousPageButtonXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']/td[2]//td[2]//button";

    public static final String nextPageButtonXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[8]//button";

    public static final String pageTextBoxXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[5]//input";

    public static final String pageCountXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[@class='x-window-bbar']//tr[@class='x-toolbar-left-row']//td[6]//div";

    public static final String pagingDisplayXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//div[contains(text(),'Displaying')]";

     *
     */
    public static final String timeSelectionTextFieldXpath = "//*[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_CRITICAL_DATA']//*[@id='timeRangeComp-input']";

    public static final String lastPageCountXpath = "1";

    public final String criticalButtonName = "criticalIndicator";

    public final String majorButtonName = "majorIndicator";

    public final String minorButtonName = "minorIndicator";

    public final String warningButtonName = "warningIndicator";

    public final String settingsButtonName = "configImage";

    public static final String settingsWindowXpath = "//span[contains(text(), 'KPI Alarm Configuration')]";

    public static final String updateBtnXpath = "//table[@id='kpiConfigPanel']//button[@id='updateBtn']";

    public static final String cancelBtnXpath = "//button[@id='cancelBtn']";

    public final String propertiesbuttonid = "//*[@id='btnProperties']";

    public final String firstRowxPath = "//*[@id='NETWORK_KPI_NOTIFICATION_CRITICAL_DATA_x-auto-105']";

    public final String closePropertyWindowXPath = "//*[@id='selenium_tag_PropertiesWindow']/div/table/tbody/tr[2]/td[2]/div/table/tbody/tr[3]/td/button";

    //public final String csvButtonId = "//*[@id='btnExport']/tbody/tr[2]/td[2]//button";

    public final String csvButtonId = "//*[@id='btnExport']//input";
    public enum RefreshTimeRange {

        FIVE_MINUTES("5 minutes", 5), FIFTEEN_MINUTES("15 minutes", 15), THIRTY_MINUTES("30 minutes", 30), ONE_HOUR(
                "1 hour", 1 * 60), TWO_HOURS("2 hours", 2 * 60), SIX_HOURS("6 hours", 6 * 60), TWELVE_HOURS("12 hours",
                12 * 60), TWENTYFOUR_HOURS("24 hours", 24 * 60);

        private final String label;

        private int minituesValue;

        private RefreshTimeRange(final String txtLabel, final int minutes) {
            label = txtLabel;
            minituesValue = minutes;
        }

        public String getLabel() {
            return label;
        }

        public static RefreshTimeRange getTimeRange(final String label) {
            for (final RefreshTimeRange range : values()) {
                if (range.label.equals(label)) {
                    return range;
                }
            }
            return null;
        }

        public int getMiniutes() {
            return minituesValue;
        }
    }


    public String getWindowTypeWithWindowName(CommonWindow window) throws Exception {

        if(window.equals(kpiCriticalWindow)){
            severityType = "CRITICAL";
        }else if(window.equals(kpiMajorWindow)){
            severityType = "MAJOR";
        }else if(window.equals(kpiMinorWindow)){
            severityType = "MINOR";
        }else if(window.equals(kpiWarningWindow)){
            severityType = "WARNING";
        }
        return severityType;
    }

    public String getWindowTypeWithButtonName(String severityName) throws Exception {

        if(severityName.equals(criticalButtonName)){
            severityType = "CRITICAL";
        }else if(severityName.equals(majorButtonName)){
            severityType = "MAJOR";
        }else if(severityName.equals(minorButtonName)){
            severityType = "MINOR";
        }else if(severityName.equals(warningButtonName)){
            severityType = "WARNING";
        }
        return severityType;
    }

    public String getButtonXPath(final String buttonID) {
        buttonName = buttonID;
        if(buttonName.contains("Indicator")){
        buttonXpath = panelXpath + "//div[@id='" + buttonName + "']/img";
        }
        else {
            buttonXpath = settingsButtonXpath;
        }
        return buttonXpath;
    }

    public String getPercentageXpath(final String buttonID) {
        buttonName = buttonID;
        String percentageXpath = panelXpath + "//div[@id='" + buttonName + "']/div/text()";
        return percentageXpath;

    }

    public String getWindowXpath(final String windowId) {
        windowName = windowId;
        String percentageXpath = "//div[@id='NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA']";
        return percentageXpath;

    }

    public String getTimeSelectionTextFieldXpath(final String windowId) {
        windowName = windowId;
        //String percentageXpath = "//*[@id='NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA']//*[@id='timeRangeComp-input']";
        String percentageXpath = "//div[contains(@id, 'NEW_WORKSPACE')]//*[@id='timeRangeComp-input']";
        return percentageXpath;

    }

    public String firstRowXpath(final String windowId) {
        windowName = windowId;
        String xpath = "//div[contains(@id,'NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA')][1]/TABLE//td[1]";
        return xpath;
    }
    public void clickOrOpenButton(final String buttonName) throws Exception {
        buttonXpath = getButtonXPath(buttonName);
        selenium.waitForElementToBePresent(buttonXpath, DEFAULT_TIMEOUT);
        selenium.click(buttonXpath);
        selenium.waitForPageLoadingToComplete();
    }

    public String getSeverityPercentage(final String buttonName) throws Exception {
        String Xpath = getPercentageXpath(buttonName);
        String value = selenium.getText(Xpath);
        System.out.println("Value: "+value);
        return value;
    }

    public void areColumnsEqual(Object colomnHeaders){

        List<String> headersList = Arrays.asList ("ROP Date", "Managed Object Name",
                "Defined KPI Type", "Computed KPI Value",
                "Defined Threshold", "Computed KPI Name",
                "Managed Object Type", "Defined KPI Formula");
        assertTrue("Colomns are not axactly same as per the requirement", headersList.equals(colomnHeaders));
    }

    public int getAllPagesRowCount(CommonWindow window) throws Exception {

        int pageCnt = 0;
        int totalRowCnt = 0;
        int pageRowCnt = 0;
        windowType = getWindowTypeWithWindowName(window);
        if(selenium.isElementPresent(firstRowXpath(windowType)))
        {
            pageCnt = window.getPageCount();
            if(pageCnt > 0){
                for(int i=1; i<=pageCnt; i++){
                    pageRowCnt = window.getTableRowCount();
                    totalRowCnt = totalRowCnt + pageRowCnt;
                    window.clickNextPage();
                    pageRowCnt = 0; window.getAllDataAtTableRow(1);
                }
            }
        }
        return totalRowCnt;
    }

    public String getWindowTimeFromListbox(String windowId) throws Exception {
        windowType = getWindowTypeWithButtonName(windowId);
        String listBoxXPath = getTimeSelectionTextFieldXpath(windowType);
        waitForPageLoadingToComplete();
        return selenium.getValue(listBoxXPath);
    }

    public void setLastDataFrom(final RefreshTimeRange timeRange) throws Exception {
        clickOrOpenButton(settingsButtonName);
        selenium.waitForElementToBePresent(settingsWindowXpath, "5000");
        selenium.type("//*[@id='refreshTimeCombo']/div/input[1]", timeRange.getLabel());
        selenium.keyPress("//*[@id='refreshTimeCombo']/div/input[1]", "\13");
        selenium.mouseDown(updateBtnXpath);
        selenium.mouseUp(updateBtnXpath);
        selenium.click(updateBtnXpath);
        waitForPageLoadingToComplete();
    }

    public String getLastDataFrom() throws Exception {
        clickOrOpenButton(settingsButtonName);
        selenium.waitForElementToBePresent(settingsWindowXpath, "5000");
        String value =  selenium.getValue("//div[@id='refreshTimeCombo']/div/input[1]");
        selenium.mouseDown(updateBtnXpath);
        selenium.mouseUp(updateBtnXpath);
        selenium.click(updateBtnXpath);
        waitForPageLoadingToComplete();
        return value;
    }

    public void setRefreshRate(final RefreshTimeRange timeRange) throws Exception {
        clickOrOpenButton(settingsButtonName);
        selenium.waitForElementToBePresent(settingsWindowXpath, "5000");
        selenium.type("//div[@id='refreshRateCombo']/div/input[1]", timeRange.getLabel());
        selenium.keyPress("//div[@id='refreshRateCombo']/div/input[1]", "\13");
        selenium.mouseDown(updateBtnXpath);
        selenium.mouseUp(updateBtnXpath);
        selenium.click(updateBtnXpath);
        waitForPageLoadingToComplete();
    }

    public String getRefreshRate() throws Exception {
        clickOrOpenButton(settingsButtonName);
        selenium.waitForElementToBePresent(settingsWindowXpath, "5000");
        String value =   selenium.getValue("//div[@id='refreshRateCombo']/div/input[1]");
        selenium.mouseDown(updateBtnXpath);
        selenium.mouseUp(updateBtnXpath);
        selenium.click(updateBtnXpath);
        waitForPageLoadingToComplete();
        return value;
    }

    public void setEniqDefaultTimeRange(final TimeRange timeRange) throws Exception {
        Thread.sleep(3000);
        selenium.click("//div[@id='headerPnl']//div[contains(text(),'Options')]");
        selenium.click("//div[contains(text(),'ENIQ Default Time')]");
        selenium.click("//span[@id='selenium_tag_timeRange']/input[@type='radio']");
        selenium.type("//input[@id='selenium_tag_time-input']", timeRange.getLabel());
        selenium.click("//button[contains(text(),'Update')]");
    }

    public   TimeCandidates getTimeCandidates( String description) {
        for(TimeCandidates tc : TimeCandidates.values())
            if(tc.getDescription().equals(description) ) return tc;
        throw new IllegalArgumentException("Unidentified Description : " + description);
    }

    public void checkPropertiesButton(final CommonWindow window){
        try{
        String windowName = getWindowTypeWithWindowName(window);
        window.setTimeRange(TimeRange.SIX_HOURS);
        if (selenium.isElementPresent(firstRowXpath(windowName))) {
            selenium.click(firstRowXpath(windowName));
            selenium.click(propertiesbuttonid);
            pause(2000);
            selenium.click(closePropertyWindowXPath);
        } else {
              logger.info("No data present for the selected time range for the " + windowName +" window.");
        }       
        }
        catch(Exception e){
        e.printStackTrace();
        }
    }     
}

