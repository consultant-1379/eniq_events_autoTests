/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eemeacod
 * @since 11/2013
 */

public class AccessAreaRankingTests extends BaseWcdmaCfa {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    @Qualifier("accessAreaCFARanking")
    private CommonWindow accessAreaCFARanking;

    @Autowired
    @Qualifier("accessAreaNetworkEventAnalysisCell")
    private CommonWindow accessAreaNetworkEventAnalysis;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
    }

    @Test
    public void verifyCFAWindow() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        verifyCFATitle();
        verifyDropdownMenu();
    }

    @Test
    public void verifyAccessAreaRankingCallDrops() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        launchCallDropsRanking();

        verifyRankingTitle();
        verifyColumnHeaders();
        verifyCallDropsData();
    }

    @Test
    public void verifyAccessAreaRankingCallSetup() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        launchCallSetupRanking();
        verifyRankingTitle();
        verifyColumnHeaders();
        verifyCallSetupData();
    }

    @Test
    public void verifyAccessAreaEventAnalysis() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();  
        launchCallDropsRanking();
        Thread.sleep(5000);
        final String accessArea = clickAccessAreaLink();
        verifyEventAnalysisTitle(accessArea);
        verifyNEAColumnHeaders();
    }

    @Test
    public void verifyDisconnectionCodeWindow() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        launchCallDropsRanking();
        Thread.sleep(5000);
        final String accessArea = clickAccessAreaLink();
        launchDisconnectionCodeWindow();
        verifyDisconnectTitle(accessArea);
    }

    @Test
    public void verifyRABTypeWindow() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        launchCallDropsRanking();
        Thread.sleep(5000);
        final String accessArea = clickAccessAreaLink();
        launchRABTypeWindow();
        verifyRABTypeTitle(accessArea);
    }

    @Test
    public void verifyDEAWindow() throws PopUpException, NoDataException, InterruptedException {
        launchWindow();
        launchCallDropsRanking();
        Thread.sleep(5000);
        final String accessArea = clickAccessAreaLink();
        launchDEAWindow();
        Thread.sleep(10000);
        verifyDEATitle(accessArea);
    }

    private String clickAccessAreaLink() throws NoDataException, PopUpException, InterruptedException {
        final String accessArea = selenium.getTable(WcdmaStingConstantsNewUI.AccessArea);
        accessAreaCFARanking.clickTableCell(0, ACCESS_AREA);
        return accessArea;
    }
    
    private void launchDisconnectionCodeWindow() throws NoDataException, PopUpException {
        accessAreaNetworkEventAnalysis.clickTableCell(0, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.Disconnection_Code);
    }
    
    private void launchRABTypeWindow() throws NoDataException, PopUpException {
        accessAreaNetworkEventAnalysis.clickTableCell(0, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.RAB_Type);
    }

    private void launchDEAWindow() throws NoDataException, PopUpException, InterruptedException {
        accessAreaNetworkEventAnalysis.clickTableCell(0, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.Detailed_Event_Analysis);
        Thread.sleep(26000);
    }

    private void verifyRABTypeTitle(final String accessArea) {
        final String RABTitle = selenium.getText(WcdmaStingConstantsNewUI.RAB_AA_Chart_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, accessArea + WcdmaStingConstantsNewUI.AccessAreaRABTitle, RABTitle);
    }

    private void verifyDEATitle(final String accessArea) {
        final String DEATitle = selenium.getText(WcdmaStingConstantsNewUI.DEA_AA_Chart_Title);
        logger.log(Level.INFO, DEATitle);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, accessArea + WcdmaStingConstantsNewUI.DEAWindowTitle, DEATitle);
    }

    private void verifyDisconnectTitle(final String accessArea) {
        final String DisconnectTitle = selenium.getText(WcdmaStingConstantsNewUI.CD_AA_Chart_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, accessArea + WcdmaStingConstantsNewUI.ACCESS_AREA_DISCONNECTIONS, DisconnectTitle);
    }

    private void verifyCallSetupData() throws NoDataException {
        final List<Map<String, String>> data = accessAreaCFARanking.getAllTableData();
        if (data.isEmpty()) {
            throw new NoDataException("No Data");
        }

        final String eventType = selenium.getTable(WcdmaStingConstantsNewUI.Event_Type_AA);

        assertEquals("Wrong Event Type!", WcdmaStingConstantsNewUI.CALL_SETUP_FAILURES, eventType);
    }

    private void verifyCallDropsData() throws NoDataException {
        final List<Map<String, String>> data = accessAreaCFARanking.getAllTableData();
        if (data.isEmpty()) {
            throw new NoDataException("No Data");
        }

        final String eventType = selenium.getTable(WcdmaStingConstantsNewUI.Event_Type_AA);
        logger.log(Level.INFO, eventType);
        assertEquals("Wrong Event Type!", WcdmaStingConstantsNewUI.CALL_DROPS, eventType);
    }

    private void verifyColumnHeaders() {
        final List<String> actualColumnHeaders = accessAreaCFARanking.getTableHeaders();  
        final List<String> expectedHeaders = Arrays.asList(RANK, FAILURES_TREND, ACCESS_AREA, CONTROLLER, EVENT_TYPE,  FAILURES);
        assertTrue(FailureReasonStringConstants.COLUMN_HEADER_MISMATCH, actualColumnHeaders.equals(expectedHeaders));       
    }
    
   

    private void verifyNEAColumnHeaders() {        
        final List<String> actualColumnHeaders = accessAreaNetworkEventAnalysis.getTableHeaders();
        final List<String> expectedHeaders = Arrays.asList(RAN_VENDOR, CONTROLLER, ACCESS_AREA, EVENT_TYPE, FAILURES, RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS,
                                 WcdmaStingConstantsNewUI.RAB_FAILURE_RATIO);
        assertTrue(FailureReasonStringConstants.COLUMN_HEADER_MISMATCH, actualColumnHeaders.equals(expectedHeaders)); 
    }

    private void verifyRankingTitle() {
        final String CDtitle = selenium.getText(WcdmaStingConstantsNewUI.Ranking_Table_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.RANKING_TABLE_AA_TITLE, CDtitle);
    }

    private void verifyEventAnalysisTitle(final String accessArea) {
        final String EAtitle = selenium.getText(WcdmaStingConstantsNewUI.Event_Analysis_Table_Title);
        logger.log(Level.INFO, EAtitle);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, accessArea + WcdmaStingConstantsNewUI.EVENT_ANALYSIS_TABLE_TITLE, EAtitle);
    }

    private void launchCallDropsRanking() {
        selenium.click(WcdmaStingConstantsNewUI.CFA_Call_Setup_Failures_Radiobutton);
        selenium.click(WcdmaStingConstantsNewUI.CFA_Launch_Button);
    }

    private void launchCallSetupRanking() {
        selenium.click(WcdmaStingConstantsNewUI.CFA_Call_Drops_Radiobutton);
        selenium.click(WcdmaStingConstantsNewUI.CFA_Launch_Button);
    }

    private void launchWindow() throws InterruptedException, PopUpException {
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        workspace.selectDimension(SeleniumConstants.RADIO_NETWORK_3G);
        Thread.sleep(5000);
        workspace.selectWindowType("3G Ranking", "Call Failure by Access Area");
        Thread.sleep(5000);
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    private void verifyDropdownMenu() {
        selenium.click(WcdmaStingConstantsNewUI.CFA_Dropdown);
        final String DD1 = selenium.getText(WcdmaStingConstantsNewUI.CFA_Dropdown_Option1);
        final String DD2 = selenium.getText(WcdmaStingConstantsNewUI.CFA_Dropdown_Option2);
        final String DD3 = selenium.getText(WcdmaStingConstantsNewUI.CFA_Dropdown_Option3);
        final String DD4 = selenium.getText(WcdmaStingConstantsNewUI.CFA_Dropdown_Option4);
        assertEquals(FailureReasonStringConstants.DROPDOWN_MISMATCH, WcdmaStingConstantsNewUI.TOTAL_RAB_FAILURE, DD1);
        assertEquals(FailureReasonStringConstants.DROPDOWN_MISMATCH, WcdmaStingConstantsNewUI.CIRCUIT_SWITCHED_FAILURE, DD2);
        assertEquals(FailureReasonStringConstants.DROPDOWN_MISMATCH, WcdmaStingConstantsNewUI.PACKET_SWITCHED_FAILURE, DD3);
        assertEquals(FailureReasonStringConstants.DROPDOWN_MISMATCH, WcdmaStingConstantsNewUI.MULTI_RAB_FAILURE, DD4);
    }

    private void verifyCFATitle() {
        final String title = selenium.getText(WcdmaStingConstantsNewUI.CFA_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.CALL_FAILURE_TITLE, title);
    }

}
