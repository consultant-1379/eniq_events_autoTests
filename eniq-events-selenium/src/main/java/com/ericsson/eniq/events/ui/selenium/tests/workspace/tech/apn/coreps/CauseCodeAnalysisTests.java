/* COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apn.coreps;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CauseCodeAnalysisTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired private WorkspaceRC workspace;
    @Autowired @Qualifier("APNCauseCodeAnalysis") private CommonWindow causeCodeAnalysisWindow;

    @Autowired @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

    @BeforeClass public static void openLog() {
        logger.log(Level.INFO, "Start of APN Cause Code Analysis Section");
    }
    @AfterClass public static void closeLog() {
        logger.log(Level.INFO, "End of APN Cause Code Analysis Section");
    }
    @Override @Before public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    /**
     * TSID:  14987
     * Title: Confirm drill down on pie chart uses the same time range as the original query.
     */
    @Test public void confirmDrillDownOnPieChartUsesTheSameTimeRangeAsTheOriginalQuery_EQEV_12588() {

        // Launch the APN Cause Code Analysis.
        try {
            String testCaseTimeRange = SeleniumConstants.DATE_TIME_30;
            String apn = retrieveTopFailingApn(testCaseTimeRange);

            workspace.selectTimeRange(testCaseTimeRange);
            workspace.selectDimension(SeleniumConstants.APN);
            workspace.enterDimensionValue(apn);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openChartViewViaClickingSingleSelectAllCheckBox();
            waitForPageLoadingToComplete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PopUpException e) {
            e.printStackTrace();
        }

        // Note the Time Range on the chart.
        String footerTime = causeCodeAnalysisWindow.getFooterTimeRange();

        // Wait the time duration.
        pause(SeleniumConstants.DURATION_TO_SLEEP_IN_CAUSE_CODE_FOOTER_TIME_RANGE_TESTS);

        // Drill on the Cause Code Pie chart.
        causeCodeAnalysisWindow.drillOnPieChartUsingFirstLegendItem();


        // Note the Time Range on the chart.
        assertEquals("Footer Time in Sub Cause Code Analysis chart does not match Footer Time in Cause Code Analysis chart",
                footerTime, causeCodeAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(2));

        // Wait the time duration.
        pause(SeleniumConstants.DURATION_TO_SLEEP_IN_CAUSE_CODE_FOOTER_TIME_RANGE_TESTS);

        // Drill on the Sub Cause Code Pie chart.
        causeCodeAnalysisWindow.drillOnPieChartUsingFirstLegendItemInSeleniumTagBaseWindow(2);

        // Note the Time Range on the grid.
        assertEquals("Footer Time in Sub Cause Code Analysis grid does not match Footer Time in Sub Cause Code Analysis chart",
                footerTime, causeCodeAnalysisWindow.getFooterTimeRangeForSeleniumTagBaseWindowNumber(3));
    }

    /**
     * Related Test Session:
     * TSID: 13772
     * Title: Check default menu items for APN Cause Code Analysis window
     */
    @Test public void checkDefaultColumns_EQEV_8267_Apn(){
        try {
            workspace.selectDimension(SeleniumConstants.APN);
            workspace.enterDimensionValue("1");
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            waitForPageLoadingToComplete();

            //Check the default selected menu items
            List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.SUB_CAUSE_CODE,
                    GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES,
                    GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if(shouldBeSelectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.CAUSE_CODE,
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if(shouldBeUnselectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }


        } catch (PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting APN");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_Apn(){
        try {
            workspace.selectDimension(SeleniumConstants.APN);
            workspace.enterDimensionValue("1");
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            waitForPageLoadingToComplete();
            try {
                networkCauseCodeAnalysisWindow.clickTableCell(0, GuiStringConstants.SUB_CAUSE_CODE);
            } catch (NoDataException e) {
                fail("Sub Cause Code Analysis window has no open.");
            }
            List<String> shouldBeUnselectedList = networkCauseCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if(shouldBeUnselectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }
        } catch (PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting APN");
        }
    }

    // ---------- Private Method ----------
    /**
     * Returns the top failing APN from the APN Ranking for the specified {@code TimeRange}.
     * The following APN values will not be returned;
     * <ul>
     *     <li>"Not.Applicable.for.Event.Type"</li>
     *     <li>"APN.Not.Provided"</li>
     * </ul>
     *
     * @param timeRange The {@code SeleniumConstants} for the time range to get the top failing APN for.
     * @return A String representation of the Top Failing APN.
     */
    private String retrieveTopFailingApn(String timeRange) throws InterruptedException, PopUpException {

        String apnInRankingRow = "";
        List<String> excludedApns = new ArrayList<String>(Arrays.asList("Not.Applicable.for.Event.Type", "APN.Not.Provided"));

        workspace.selectTimeRange(timeRange);
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.APN);
        workspace.launch();
        waitForPageLoadingToComplete();

        for (int rankingRow = 1; ; rankingRow++) {

            apnInRankingRow = selenium.getText("//div[contains(@id,'NETWORK_APN_RANKING_x-auto')][" + rankingRow + "]//span[@id='NETWORK_EVENT_ANALYSIS_APN'][text()]");

            if (! excludedApns.contains(apnInRankingRow)) break;
        }

        // Close the APN Ranking Grid
        selenium.click("//div[contains(@class, 'x-tool-close')]");

        return apnInRankingRow;
    }
}
