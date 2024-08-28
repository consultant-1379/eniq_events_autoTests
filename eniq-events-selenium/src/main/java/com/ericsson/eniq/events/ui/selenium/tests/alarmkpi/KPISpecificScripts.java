package com.ericsson.eniq.events.ui.selenium.tests.alarmkpi;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KPISpecificScripts extends DefaultDataLibrary{
	
	
		
	public String buttonXpath;
	public String buttonPercentageXpath;
	public String buttonName;
	public int colomnCnt;
	public List<String> colomnHeaders;
	public String severityType = null;
	public String elementIdBefore = null;
	public String elementIdAfter = null;
	public String windowName = null;
    static Logger logger = Logger.getLogger(KPISpecificScripts.class.getName());
        
	public String getSeverityType(CommonWindow window) throws Exception {
		
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
	
	public String getSeverityType(String severityName) throws Exception {
		
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
		String percentageXpath = "//div[@id='selenium_tag_BaseWindow_NETWORK_KPI_NOTIFICATION_" + windowName + "_DATA']";
		return percentageXpath;
		
	}
	
	public String firstRowXpath(final String buttonId) {
		buttonName = buttonId;
		String xpath = "//div[@id='NETWORK_KPI_NOTIFICATION_" + buttonName + "_DATA']//div[contains(@id,'NETWORK_KPI_NOTIFICATION_" + buttonName + "_DATA')][1]/TABLE//td[1]";
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
		Thread.sleep(5000);
		String value = selenium.getText(Xpath);
		return value;
	}
	
	
	
	public void areColomnsEqual(Object colomnHeaders){
		
		List<String> headersList = Arrays.asList ("Event Date", "Managed Object Name",
				"Defined KPI Type", "Computed KPI Value",
				"Defined Threshold", "Computed KPI Name",
				"Managed Object Type", "Defined KPI Formula");
		assertEquals("Colomns are not axactly same as per the requirement", headersList, colomnHeaders);
	}
	
	
	public void clickingOnAllButtonLeadingToNetworkTab(final String windowId) throws Exception{
		windowName = getSeverityType(windowId);
		String windowXpath = getWindowXpath(windowName);
		terminalTab.openTab();
		assertFalse("Clicking on panel leading to terminal tab, not to network tab", selenium.isVisible(windowXpath));
		subscriberTab.openTab();
		assertFalse("Clicking on panel leading to Subscriber tab, not to network tab", selenium.isVisible(windowXpath));
		rankingsTab.openTab();
		assertFalse("Clicking on panel leading to Rankings tab, not to network tab", selenium.isVisible(windowXpath));
		networkTab.openTab();
		assertTrue("By clicking on Critical Button is not leading to NetworkTab", selenium.isVisible(windowXpath));
	}
	
	public void checkPageNavigation(CommonWindow window) throws Exception{
		int pageNumber = 0;
		int currentPageNumber = 0;
		int pagesCnt = window.getPageCount();
		windowName = getSeverityType(window);
		pageNumber = window.getCurrentPageNumber();
	
		if(pagesCnt != 1){
		if(pagesCnt != pageNumber){
		window.clickNextPage();
		currentPageNumber = window.getCurrentPageNumber();
		assertNotSame("Page numbers are same even after navigating to next page on " + windowName + "window", pageNumber, currentPageNumber);
		pageNumber = currentPageNumber;
		}
		window.clickFirstPage();
		currentPageNumber = window.getCurrentPageNumber();
		assertNotSame("Page numbers are same even after navigating to first page on " + windowName + "window", pageNumber, currentPageNumber);
		pageNumber = currentPageNumber;
		
		window.clickLastPage();
		currentPageNumber = window.getCurrentPageNumber();
		assertNotSame("Page numbers are same even after navigating to last page on " + windowName + "window", pageNumber, currentPageNumber);
		pageNumber = currentPageNumber;

		window.clickPreviousPage();
		currentPageNumber = window.getCurrentPageNumber();
		assertNotSame("Page numbers are same even after navigating to previuos page on " + windowName + "window", pageNumber, currentPageNumber);
		}

	}
	
	public void checkTimeOnWindowGoesToDefaultWhenOpenedWindowTypeButtonIsClickedAgain(CommonWindow window, String buttonId) throws Exception{
		
		buttonName = buttonId;
		String severityName = getSeverityType(buttonName);
    	window.setTimeRange(TimeRange.FIFTEEN_MINUTES);	
		String changedTime = selenium.getValue(timeSelectionTextFieldXpath);
    	waitForPageLoadingToComplete();
    	clickOrOpenButton(buttonName);
    	waitForPageLoadingToComplete();
    	String defaultTime = selenium.getValue(timeSelectionTextFieldXpath);
    	assertFalse("Time is same even after clicking on the " + severityName + "severity button which is already opened", defaultTime.equals(changedTime));
		
	}

	public void checkColomnCntAndllColomnsAreVisibleForEachSeverityTypeWindow(CommonWindow window) throws Exception{
		windowName = getSeverityType(window);
		colomnCnt = window.getTableHeaderCount();
		assertEquals(colomnCnt, 8);
		logger.log(Level.INFO, "Colomn count on " + windowName + " window is: "  + colomnCnt);
		colomnHeaders = window.getTableHeaders();
		areColomnsEqual(colomnHeaders);
	}
	
	public void verifyUIControlsAreVisibleAndFunctional(String windowId) throws Exception{
		windowName = getSeverityType(windowId);
		assertTrue("Refresh button on " + windowName + " window is not visible or clickable", selenium.isElementPresent(refreshButtonXpath));
		assertTrue("Next page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(nextPageButtonXpath));
		assertTrue("Previous Page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(previousPageButtonXpath));
		assertTrue("First page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(firstPageButtonXpath));
		assertTrue("Last page button on  " + windowName + "  window is not visible or clickable", selenium.isElementPresent(lastPageButtonXpath));
		assertTrue("Page text box button on  " + windowName + "  window is not visible or enabled", selenium.isElementPresent(pageTextBoxXpath));
		assertTrue("Page count on  " + windowName + "  window is not visible", selenium.isElementPresent(pageCountXpath));
		//assertTrue("Paging display on  " + window + "  window is not visible", selenium.isElementPresent(pagingDisplayXpath));
	}
	
	public void checkPageRefresh(CommonWindow window) throws Exception {
		
		String severityName = getSeverityType(window);
		if(selenium.isElementPresent(firstRowXpath(severityName)))
		{
			elementIdBefore = selenium.getAttribute(firstRowXpath(severityName)+ "@id");
			window.refresh();
			elementIdAfter = selenium.getAttribute(firstRowXpath(severityName)+ "@id");
			assertFalse(severityName + " window is not refreshed even after clicking on refresh button", elementIdBefore.equals(elementIdAfter));
		}
	}
		
	public void CheckIfTimeChangedInListBoxAlsoDataShouldBeChange(CommonWindow window) throws Exception {
		
		buttonName = getSeverityType(window);
		final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES);
		TimeRange[] timeRanges;
   
	    if (allTimeLabel != null) {
	        final String[] timeLabels = allTimeLabel.split(",");
	        timeRanges = new TimeRange[timeLabels.length];
	        for (int i = 0; i < timeLabels.length; i++) {
	            timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
	        }
	        for( TimeRange timeRange :timeRanges)
	        {
	        	if(selenium.isElementPresent(firstRowXpath(severityType)))
	    		{
	        		elementIdBefore = selenium.getAttribute(firstRowXpath(buttonName)+ "@id");
		    		window.setTimeRange(timeRange);
	        		elementIdAfter = selenium.getAttribute(firstRowXpath(buttonName)+ "@id");
	        		assertFalse("Data not changed after changing the time from listbox on " + buttonName + " severity button", elementIdBefore.equals(elementIdAfter));
	    		}
	        }
	
	    }
	}
	
	
	public int getAllPagesRowCount(CommonWindow window) throws Exception {
		
		int pageCnt = 0;
		int totalRowCnt = 0;
		int pageRowCnt = 0;
		severityType = getSeverityType(window);
		if(selenium.isElementPresent(firstRowXpath(severityType)))
		{
			System.out.println("Type: " +firstRowXpath(severityType));
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
}
