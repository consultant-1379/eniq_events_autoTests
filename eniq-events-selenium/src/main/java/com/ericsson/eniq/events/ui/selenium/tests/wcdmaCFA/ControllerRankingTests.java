/* COPYRIGHT Ericsson 2013
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
 * @author emeacod
 * @since 11/2013
 */

public class ControllerRankingTests extends BaseWcdmaCfa {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    @Qualifier("wcdmaCFARANRanking")
    private CommonWindow wcdmaCallFailureRANRankings;

    @Autowired
    @Qualifier("wcdmaCFANetworkEventAnalysis")
    private CommonWindow wcdmaCFANetworkEventAnalysis;

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
        Thread.sleep(5000);
        verifyCFATitle();
        verifyDropdownMenu();

    }

    @Test
    public void verifyControllerRankingCallDrops() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);
        launchCallDropsRanking();
        Thread.sleep(5000);
        verifyRankingTitle();
        verifyColumnHeaders();
        verifyCallDropsData();

    }

    @Test
    public void verifyControllerRankingCallSetup() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);
        launchCallSetupRanking();
        Thread.sleep(5000);
        verifyRankingTitle();
        verifyColumnHeaders();
        verifyCallSetupData();

    }

    @Test
    public void verifyControllerNEAWindow() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);
        launchCallDropsRanking();
        Thread.sleep(5000);
        final String controller = selenium.getText(WcdmaStingConstantsNewUI.Controller_Name);
        wcdmaCallFailureRANRankings.clickTableCell(0, CONTROLLER);
        verifyNEATableTitle(controller);
        verifyNEAColumnHeaders();

    }

    @Test
    public void verifyDisconnectionCodeWindow() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);

        launchCallDropsRanking();
        Thread.sleep(5000);
        wcdmaCallFailureRANRankings.clickTableCell(0, CONTROLLER);
        wcdmaCFANetworkEventAnalysis.clickTableCell(0, FAILURES);
        selenium.click(WcdmaStingConstantsNewUI.Disconnection_Code);
        Thread.sleep(5000);
        verifyDisconnectTitle();

    }

    @Test
    public void verifyRABTypeWindow() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);

        launchCallDropsRanking();
        Thread.sleep(5000);
        wcdmaCallFailureRANRankings.clickTableCell(0, CONTROLLER);
        wcdmaCFANetworkEventAnalysis.clickTableCell(0, FAILURES);
        Thread.sleep(5000);
        selenium.click(WcdmaStingConstantsNewUI.RAB_Type);
        Thread.sleep(5000);
        verifyRABTypeTitle();

    }

    @Test
    public void verifyDEAWindow() throws PopUpException, NoDataException, InterruptedException {

        launchWindow();
        Thread.sleep(5000);

        launchCallDropsRanking();
        Thread.sleep(5000);
        final String controller = selenium.getText(WcdmaStingConstantsNewUI.Controller_Name);
        wcdmaCallFailureRANRankings.clickTableCell(0, CONTROLLER);
        wcdmaCFANetworkEventAnalysis.clickTableCell(0, FAILURES);
        Thread.sleep(5000);
        selenium.click(WcdmaStingConstantsNewUI.Detailed_Event_Analysis);
        Thread.sleep(20000);
        verifyDEATitle(controller);

    }

    private void verifyRABTypeTitle() {
        final String DisconnectTitle = selenium.getText(WcdmaStingConstantsNewUI.RAB_Chart_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.ControllerRABTitle, DisconnectTitle);
    }

    private void verifyDEATitle(final String controller) {
        final String DisconnectTitle = selenium.getText(WcdmaStingConstantsNewUI.DEA_Chart_Title);
        logger.log(Level.INFO, DisconnectTitle);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, controller + WcdmaStingConstantsNewUI.DEAWindowTitle, DisconnectTitle);
    }

    private void verifyDisconnectTitle() {
        final String DisconnectTitle = selenium.getText(WcdmaStingConstantsNewUI.CD_Chart_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.CONTROLLER_DISCONNECTIONS, DisconnectTitle);
    }

    private void verifyNEATableTitle(final String controller) {
        final String NEAtitle = selenium.getText(WcdmaStingConstantsNewUI.Controller_Table_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, controller + WcdmaStingConstantsNewUI.NEATableTitle, NEAtitle);
    }

    private void verifyCallSetupData() throws NoDataException {
        final List<Map<String, String>> data = wcdmaCallFailureRANRankings.getAllTableData();
        if (data.isEmpty()) {
            throw new NoDataException("No Data");
        }

        final String eventType = selenium.getTable(WcdmaStingConstantsNewUI.Event_Type);

        assertEquals("Wrong Event Type!", WcdmaStingConstantsNewUI.CALL_SETUP_FAILURES, eventType);
    }

    private void verifyCallDropsData() throws NoDataException {
        final List<Map<String, String>> data = wcdmaCallFailureRANRankings.getAllTableData();
        if (data.isEmpty()) {
            throw new NoDataException("No Data");
        }

        final String eventType = selenium.getTable(WcdmaStingConstantsNewUI.Event_Type);

        assertEquals("Wrong Event Type!", WcdmaStingConstantsNewUI.CALL_DROPS, eventType);
    }

    private void verifyColumnHeaders() {
        final List<String> actualColumnHeaders = wcdmaCallFailureRANRankings.getTableHeaders();
        final List<String> expectedHeaders = Arrays.asList(RANK, CONTROLLER, EVENT_TYPE, FAILURES);
        assertTrue(FailureReasonStringConstants.COLUMN_HEADER_MISMATCH, actualColumnHeaders.equals(expectedHeaders));
    }

    private void verifyNEAColumnHeaders() {
        final List<String> actualColumnHeaders = wcdmaCFANetworkEventAnalysis.getTableHeaders();
        final List<String> expectedHeaders = Arrays.asList(RAN_VENDOR, CONTROLLER, EVENT_TYPE, FAILURES,RE_ESTABLISHMENT_FAILURES, IMPACTED_SUBSCRIBERS,
                WcdmaStingConstantsNewUI.RAB_FAILURE_RATIO);
        assertTrue(FailureReasonStringConstants.COLUMN_HEADER_MISMATCH, actualColumnHeaders.equals(expectedHeaders));
    }

    private void verifyRankingTitle() {
        final String CDtitle = selenium.getText(WcdmaStingConstantsNewUI.Ranking_Table_Title);
        assertEquals(FailureReasonStringConstants.HEADER_MISMATCH, WcdmaStingConstantsNewUI.RANKING_TABLE_TITLE, CDtitle);
    }

    private void launchCallDropsRanking() {
        selenium.click(WcdmaStingConstantsNewUI.CFA_Call_Setup_Failures_Radiobutton);
        selenium.click(WcdmaStingConstantsNewUI.CFA_Launch_Button);
    }

    private void launchCallSetupRanking() {
        selenium.click(WcdmaStingConstantsNewUI.CFA_Call_Drops_Radiobutton);
        selenium.click(WcdmaStingConstantsNewUI.CFA_Launch_Button);
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

    private void launchWindow() throws InterruptedException, PopUpException {
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        workspace.selectDimension(SeleniumConstants.RADIO_NETWORK_3G);
        Thread.sleep(5000);
        workspace.selectWindowType("3G Ranking", "Call Failure by Controller");
        Thread.sleep(5000);
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

}
