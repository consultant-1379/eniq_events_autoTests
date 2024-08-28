/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apngroup.coreps;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.GroupCreationUtils;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.logging.Level;

public class QOSTests extends EniqEventsUIBaseSeleniumTest {

	private static String APN_GROUP_NAME = "";
    @Autowired private WorkspaceRC workspace;
    @Autowired @Qualifier("networkQosStatisticsAnalysis") private CommonWindow qosWindow;
    @Autowired private GroupManagementWindow groupManagementWindow;
    @Autowired private GroupManagementEditWindow groupManagementEditWindow;
    
    @BeforeClass public static void openLog() {
        logger.log(Level.INFO, "Start of APN Group QOS Section");
    }
    @AfterClass public static void closeLog() {
        logger.log(Level.INFO, "End of APN Group QOS Section");
    }
    @Override @Before public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
    }

    /** TSID: 14812 - Action 8
     * Title: Verify APN Group services query update for Quality of Service. */
    @Test public void drillOnFailuresTimeRangeConsistent1Week_EQEV_8531_ApnGroup() throws Exception{
    	drillOnFailures_timeRangeConsistent(TimeRange.ONE_WEEK);
    }

    /** TSID: 14812 - Action 8
     * Title: Verify APN Group services query update for Quality of Service. */
    @Test public void drillOnFailuresNoOfFailuresMatch1Week_EQEV_8531_ApnGroup() throws Exception{
    	drillOnFailures_NoOfFailuresMatch(TimeRange.ONE_WEEK);
    }

    /** TSID: 14812 - Action 8
     * Title: Verify APN Group services query update for Quality of Service. */
    @Test public void drillOnFailuresQciMatch1Week_EQEV_8531_ApnGroup() throws Exception{
    	drillOnFailures_QCIMatch(TimeRange.ONE_WEEK);
    }

    /** TSID: 14812 - Action 8
     * Title: Verify APN Group services query update for Quality of Service. */
    @Test public void propertiesPopupWindowMatchesRow1Week_EQEV_8531_ApnGroup() throws Exception{
    	propertiesPopupWindowMatchesRow(TimeRange.ONE_WEEK);
    }

    /** TSID: 14812 - Action 8
     * Title: Verify APN Group services query update for Quality of Service. */
    @Test public void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures1Week_EQEV_8531_ApnGroup() throws Exception{
    	noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange.ONE_WEEK);
    }

    // ---------- Private Methods ----------
    private void drillOnFailures_timeRangeConsistent(TimeRange timeRange) throws Exception{
    	launchWindow();
    	
    	boolean dataFound = false;
		qosWindow.setTimeRange(timeRange);
		
		int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
		
        for(int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++){
	        String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
	        if(Integer.parseInt(noOfFailures) > 0){
	        	dataFound = true;
	        	String timeFromInitWindow = qosWindow.getTimeLabelText();
	        	
	        	pause(SeleniumConstants.DURATION_TO_SLEEP_IN_FOOTER_TIME_RANGE_TESTS);
	        	
		        qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
		        waitForPageLoadingToComplete();
		        
		        String timeFromDrilldownWindow = qosWindow.getTimeLabelText();
		        assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);
		        
		        qosWindow.clickBackwardNavigation();
		        
		        timeFromInitWindow = qosWindow.getTimeLabelText();
		        assertEquals("Time different between initial window and drilldown window.", timeFromInitWindow, timeFromDrilldownWindow);
		        break;
	        }
        }
    	
    	if(!dataFound){
    		throw new NoDataException("No Data Found For APN Group: " + APN_GROUP_NAME + " for the Time Range: " + timeRange.getLabel());
    	}
    }
    
    private void drillOnFailures_NoOfFailuresMatch(TimeRange timeRange) throws Exception{
    	launchWindow();
    	
    	boolean dataFound = false;
		qosWindow.setTimeRange(timeRange);
		
        for(int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++){
        	int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
        	String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);

            if (Integer.parseInt(noOfFailures) > UIConstants.MAX_ROWS_RETURNED) {
                noOfFailures = "" + UIConstants.MAX_ROWS_RETURNED;
            }

            if(Integer.parseInt(noOfFailures) > 0) {
	        	dataFound = true;
	        	
		        qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
		        waitForPageLoadingToComplete();

                WorkspaceUtils.tickSelectAllQCIColumns(qosWindow, selenium);
                int drilldownNoOfFailures = 0;
		        
		        List<String> data = qosWindow.getAllPagesDataAtColumn(GuiStringConstants.QCI_ERR+" "+(rowIndex+1));
		        
		        for(String value: data){
		        	drilldownNoOfFailures = drilldownNoOfFailures + Integer.parseInt(value);
		        }

                WorkspaceUtils.unTickSelectAllQCIColumns(qosWindow, selenium);

                assertEquals("Number of failures does not match number of failures in drilldown window.", Integer.parseInt(noOfFailures), drilldownNoOfFailures);
		        
		        qosWindow.clickBackwardNavigation();
	        }
        }
    	
    	if(!dataFound){
            throw new NoDataException("No Data Found For APN Group: " + APN_GROUP_NAME + " for the Time Range: " + timeRange.getLabel());
    	}
    }
    
    private void drillOnFailures_QCIMatch(TimeRange timeRange) throws Exception{
    	launchWindow();
    	
    	boolean dataFound = false;
    	
		qosWindow.setTimeRange(timeRange);
		
        for(int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++){
        	int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
        	String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
	        if(Integer.parseInt(noOfFailures) > 0){
	        	
	        	dataFound = true;
	        	
		        qosWindow.clickTableCell(rowIndex, GuiStringConstants.FAILURES);
		        waitForPageLoadingToComplete();

                WorkspaceUtils.tickSelectAllQCIColumns(qosWindow, selenium);

                int qciHeaderIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.QCI_ERR+" "+(rowIndex+1));
	        	String qciValue = qosWindow.getTableData(0, qciHeaderIndex);

                WorkspaceUtils.unTickSelectAllQCIColumns(qosWindow, selenium);

                assertTrue("QCI value is incorrect.", Integer.parseInt(qciValue) > 0);
		        
		        qosWindow.clickBackwardNavigation();
	        	waitForPageLoadingToComplete();
	        }
        }
    	
    	if(!dataFound){
            throw new NoDataException("No Data Found For APN Group: " + APN_GROUP_NAME + " for the Time Range: " + timeRange.getLabel());
    	}
    }
    
    private void propertiesPopupWindowMatchesRow(TimeRange timeRange) throws Exception{
    	launchWindow();
        
    	qosWindow.setTimeRange(timeRange);

    	for(int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++){
			int qosIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.QOS);
	    	int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
	    	int successesIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.SUCCESSES);
	    	int impactedSubscriberIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);
	    	
	    	String qos = qosWindow.getTableData(rowIndex, qosIndex);
	    	String failures = qosWindow.getTableData(rowIndex, failuresIndex);
	    	String successes = qosWindow.getTableData(rowIndex, successesIndex);
	    	String impactedSubscribers = qosWindow.getTableData(rowIndex, impactedSubscriberIndex);
	    	
	    	assertEquals("Row does not match properties popup window.", Integer.toString(rowIndex+1), findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.QCI_ID));
	    	assertEquals("Row does not match properties popup window.", qos, findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.QOS));
	    	assertEquals("Row does not match properties popup window.", failures, findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.FAILURES));
	    	assertEquals("Row does not match properties popup window.", successes, findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.SUCCESSES));
	    	assertEquals("Row does not match properties popup window.", impactedSubscribers, findValueOfPropertyFromPropertiesPopupWindow(rowIndex, GuiStringConstants.IMPACTED_SUBSCRIBERS));
    	}
    }
    
    private void noOfImpactedSubscribersIsLessThanOrEqualToNumberOfFailures(TimeRange timeRange) throws Exception{
    	launchWindow();
    	
    	boolean dataFound = false;
    	
    	qosWindow.setTimeRange(timeRange);
		
        for(int rowIndex = 0; rowIndex < qosWindow.getTableRowCount(); rowIndex++){
        	
        	dataFound = true;
        	
        	int failuresIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.FAILURES);
        	String noOfFailures = qosWindow.getTableData(rowIndex, failuresIndex);
        	int impactedSubscribersIndex = qosWindow.getTableHeaderIndex(GuiStringConstants.IMPACTED_SUBSCRIBERS);
        	String impactedSubscribers = qosWindow.getTableData(rowIndex, impactedSubscribersIndex);
        	
        	assertTrue("Number of impacted subscribers("+impactedSubscribers+") is greater than the number of failures("+noOfFailures+").", Integer.parseInt(impactedSubscribers) <= Integer.parseInt(noOfFailures));
        }
    	
    	if(!dataFound){
            throw new NoDataException("No Data Found For APN Group: " + APN_GROUP_NAME + " for the Time Range: " + timeRange.getLabel());
    	}
    }

	private void launchWindow() throws InterruptedException, PopUpException, NoDataException {
        APN_GROUP_NAME = GroupCreationUtils.makeApnGroupFromTopFailingApns(workspace, groupManagementWindow, groupManagementEditWindow, selenium);
        workspace.selectDimension(SeleniumConstants.APN_GROUP);
        workspace.enterDimensionValueForGroups(APN_GROUP_NAME);
        workspace.selectWindowType(GuiStringConstants.QOS, GuiStringConstants.LMAW_CORE_PS);
        workspace.launch();
        waitForPageLoadingToComplete();
	}
	
	private String findValueOfPropertyFromPropertiesPopupWindow(int rowIndex, String propertyName) {
        String xPathForRowInGrid = "//div[@class='x-grid3-body']/div[" + (rowIndex + 1) + "][contains(concat(' ',@class,' '), ' x-grid3-row ')]";
        selenium.click(xPathForRowInGrid);
        qosWindow.clickButton(SelectedButtonType.PROPERTY_BUTTON);

        String xPathForPropertiesPopup = "//*[@id='selenium_tag_PropertiesWindow']//div[contains(concat(' ',@class,' '), ' contentPanel ')]";
        String[] propertyNamesAndValues = selenium.getText(xPathForPropertiesPopup).split("\n\n");
        String propertyValue = null;
        for (String propertyNameAndValue : propertyNamesAndValues) {
            if (propertyNameAndValue.startsWith(propertyName)) {
                propertyValue = propertyNameAndValue.split(propertyName+": ")[1];
                break;
            }
        }
        
        selenium.click("//*[@id='selenium_tag_PropertiesWindow']//div[contains(concat(' ',@class,' '), ' closeIcon ')]");
        
        return propertyValue;
    }
}