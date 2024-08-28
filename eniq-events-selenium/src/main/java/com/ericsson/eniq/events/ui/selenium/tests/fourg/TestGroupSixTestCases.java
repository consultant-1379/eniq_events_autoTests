/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow.CheckboxGroup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author ekeviry
 * @since 2011
 * 
 */
public class TestGroupSixTestCases extends BaseFourGTest {

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow eventAnalysisWindow;

    @Autowired
    @Qualifier("eventVolume")
    private CommonWindow eventVolumeWindow;

    /**
     * Requirement: 105 65-0528/00392 Test Case ENIQ_EE_6.1: Verify KPI formula
     * for Attach Success Rate is giving correct value for an MME Purpose: To
     * verify that the KPI formula for Attach Success Rate is giving the correct
     * value
     */
    @Test
    public void verifyKPIFormulaForAttachSuccessRateIsGivingCorrectValueForAnMME_6_1() throws Exception {

        // Action 1
        final String searchValue = getValueOfSelectedTypeByClickingDropDownButton(NetworkType.SGSN_MME, false);
        enterValueAndClickSubmitButton(searchValue, false);

        setTimeRangeToOneWeek(eventAnalysisWindow);
        waitForPageLoadingToComplete();
        pause(4000);

        selenium.click("//table[@id='btnKPI']");
        waitForPageLoadingToComplete();
        pause(2000);

        selenium.click("//table[@id='btnView']");
        pause(2000);

        // ****Get it ticking the correct checkbox****
        eventVolumeWindow.setCheckboxGroup(CheckboxGroup.CHECKBOX2);
        pause(7000);

    }

    /**
     * Requirement: 105 65-0528/00788 Test Case ENIQ_EE_6.2: Verify KPI formula
     * for the PDN Connection Success Rate is giving the correct value for an
     * MME Purpose: To verify that the KPI formula for PDN Connection Success
     * Rate is giving the correct value
     */
    // @Test
    public void verifyKPIFormulaForThePDNConnectionSuccessRateIsGivingTheCorrectValueForAnMME_6_2() throws Exception {
        // Functionality to be added
    }

    /**
     * Requirement: 105 65-0528/00487 Test Case: ENIQ_EE_6.3 Verify KPI formula
     * for the Bearer Activation Success Rate is giving the correct value for an
     * MME Purpose: To verify that the KPI formula for Bearer Activation Success
     * Rate is giving the correct value
     */
    // @Test
    public void verifyKPIFormulaForTheBearerActivationSuccessRateIsGivingTheCorrectValueForAnMME_6_3() throws Exception {
        // Functionality to be added
    }

    /**
     * Requirement: 105 65-0528/00486 Test Case: ENIQ_EE_6.4 Verify KPI formula
     * for the UE Initiated Service Request Failure Ratio is giving the correct
     * value for an MME Purpose: To verify that the KPI formula for UE Initiated
     * Service Request Failure Ratio is giving the correct value
     */
    // @Test
    public void verifyKPIFormulaForTheUEInitiatedServiceRequestFailureRatioIsGivingTheCorrectValueForAnMME_6_4()
            throws Exception {
        // Functionality to be added
    }

    /**
     * Requirement: 105 65-0528/00501 Test Case: ENIQ_EE_6.5 Verify KPI formula
     * for Paging Failure Ratio is giving the correct value for an MME Purpose:
     * To verify that the KPI formula Paging Failure Ratio is giving the correct
     * value
     */
    // @Test
    public void verifyKPIFormulaForPagingFailureRatioIsGivingTheCorrectValueForAnMME_6_5() throws Exception {
        // Functionality to be added
    }

    /**
     * Requirement: 105 65-0528/00647 Test Case: ENIQ_EE_6. 6 Verify KPI formula
     * for Paging Intensity is giving the correct value for an MME Purpose: To
     * verify that the KPI formula Paging Intensity is giving the correct value
     */
    // @Test
    public void verifyKPIFormulaForPagingIntensityIsGivingTheCorrectValueForAnMME_6_6() throws Exception {
        // Functionality to be added
    }

    /**
     * Requirement: 105 65-0528/00488 Test Case: ENIQ_EE_6.7: Verify KPI formula
     * for Inter-MME Tracking Area Update Success Rate is giving the correct
     * value for an MME Purpose: To verify that the Inter-MME Tracking Area
     * Update Success Rate is giving the correct value
     */
    // @Test
    public void verifyKPIFormulaForInterMMETrackingAreaUpdateSuccessRateIsGivingTheCorrectValueForAnMME_6_7()
            throws Exception {
        // Functionality to be added
    }

    //    private void openCauseCodeAnalysisWindow() {
    //        networkTab.openTab();
    //        networkTab.openSubStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS);
    //        assertTrue("Can't open Cause Code Analysis window", selenium.isTextPresent("Cause Code Analysis"));
    //    }

    private String getValueOfSelectedTypeByClickingDropDownButton(final NetworkType type, final boolean group) {
        openDropDownWindow(type, group);

        final String searchValue = networkTab.getValueFromDropDownButton(1);
        logger.info(type + " : " + searchValue);
        return searchValue;
    }

    private void openDropDownWindow(final NetworkType type, final boolean group) {
        networkTab.openTab();
        pause(3000);
        networkTab.setSearchType(type);
        pause(5000);
        networkTab.clickDropDownButton(group);
        pause(7000);
    }

    private void enterValueAndClickSubmitButton(final String searchValue, final boolean group) throws PopUpException {
        networkTab.enterSearchValue(searchValue, group);
        pause(5000);

        networkTab.enterSubmit(group);
        waitForPageLoadingToComplete();
    }

    // private void sortAndClickTableDataAtFirstRow(final String sortBy,
    // final String columnHeader) throws NoDataException, Http500Exception {
    // eventAnalysisWindow.sortTable(SortType.DESCENDING, sortBy);
    // pause(2000);
    // eventAnalysisWindow.clickTableCell(0, columnHeader);
    // waitForPageLoadingToComplete();
    // }

    // private void drillCauseCodeAnalysisDownToSubCauseCode(
    // final String accessAreaGroup, final NetworkType type,
    // final String groupCauseCodeAnalysis,
    // final String subCauseCodeAnalysis) throws Http500Exception,
    // NoDataException {
    // networkTab.openTab();
    // networkTab.setSearchType(type);
    // pause(3000);
    // networkTab.enterSearchValue(accessAreaGroup, true);
    // pause(3000);
    // networkTab.openSubStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS);
    // waitForPageLoadingToComplete();
    // assertTrue(
    // "Can't open " + groupCauseCodeAnalysis
    // + " Cause Code Analysis window",
    // selenium.isTextPresent(groupCauseCodeAnalysis
    // + " Cause Code Analysis"));
    // causeCodeAnalysisWindow.clickCauseCodes("x-grid-group-div");
    // waitForPageLoadingToComplete();
    // causeCodeAnalysisWindow.clickCauseCodes("gridCellLink");
    // waitForPageLoadingToComplete();
    // assertTrue(
    // "Can't open " + subCauseCodeAnalysis
    // + " Sub Cause Code Analysis window",
    // selenium.isTextPresent(subCauseCodeAnalysis
    // + " Sub Cause Code Analysis"));
    // selenium.refresh();
    // pause(2000);
    // }
}
