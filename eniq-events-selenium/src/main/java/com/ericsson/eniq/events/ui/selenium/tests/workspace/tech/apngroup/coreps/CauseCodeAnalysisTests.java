/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2014 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.apngroup.coreps;

import java.util.*;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementEditWindow;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.GroupManagementWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.GroupCreationUtils;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

/**
 * @author elukpot
 * @since 14.0.14
 */
public class CauseCodeAnalysisTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("APNCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;
    @Autowired
    private GroupManagementWindow groupManagementWindow;
    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of APN Group Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of APN Group Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(1000);
    }

    /**
     * TSID: 13773 Title: Check default menu items for APN Group Cause Code Analysis window.
     */
    @Test
    public void checkDefaultColumns_EQEV_8267_ApnGroup() throws NoDataException {
        try {
            String apnGroupName = GroupCreationUtils.makeApnGroupFromTopFailingApns(workspace, groupManagementWindow, groupManagementEditWindow,
                    selenium);
            workspace.selectDimension(SeleniumConstants.APN_GROUP);
            workspace.enterDimensionValueForGroups(apnGroupName);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            waitForPageLoadingToComplete();

            //Check the default selected menu items
            List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.CAUSE_CODE, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.MENU_CAUSE_CODE_ID, GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting APN Group");
        }
    }

    @Test
    public void verifyThatTheNoOfOccurncesAndImpactedSubscribersAreConsistentInTheCauseCodeAnalysisWindows() {

        try {
            String apnGroupName = GroupCreationUtils.makeApnGroupFromTopFailingApns(workspace, groupManagementWindow, groupManagementEditWindow,
                    selenium);

            // Launch the APN Group -> Cause Code Analysis -> Core PS -> Grid View grid.
            workspace.selectDimension(SeleniumConstants.APN_GROUP);
            workspace.enterDimensionValueForGroups(apnGroupName);
            workspace.selectWindowType(GuiStringConstants.CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.clickLaunchButton();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.clickButton(SelectedButtonType.LAUNCH_BUTTON);
            waitForPageLoadingToComplete();

            // Note the number of "Occurrences" and "Impacted Subscribers" in the "APN Group - Cause Code Analysis" grid.
            int causeCodeAnalysisOccurrences = Integer
                    .parseInt(selenium
                            .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']//div[contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS_x-auto-')]//td[10]//div[text()]"));
            int causeCodeAnalysisImpactedSubscribers = Integer
                    .parseInt(selenium
                            .getText("//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']//div[contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS_x-auto-')]//td[11]//div[text()]"));
            logger.log(Level.INFO, "Occurrences: " + causeCodeAnalysisOccurrences + ". Impacted Subscribers: " + causeCodeAnalysisImpactedSubscribers);

            // Drill on the "Sub Cause Code" column.
            causeCodeAnalysisWindow.clickRankingDrills("NETWORK_CAUSE_CODE_ANALYSIS_APN_DRILL", GuiStringConstants.SUB_CAUSE_CODE);

            // Determine the number of events in the "APN Group - Sub Cause Code Analysis" grid.
            int subCauseCodeAnalysisNumberOfEvents = selenium
                    .getXpathCount(
                            "//div[@id='selenium_tag_baseWindow']//div[@id='NETWORK_CAUSE_CODE_ANALYSIS']//div[contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS_x-auto-')]")
                    .intValue();
            // Determine the number of unique IMSIs in the "APN Group - Sub Cause Code Analysis" grid.
            int subCauseCodeAnalysisUniqueImsis = determineNumberOfUniqueImsis(subCauseCodeAnalysisNumberOfEvents);
            logger.log(Level.INFO, "Events: " + subCauseCodeAnalysisNumberOfEvents + ". IMSIs: " + subCauseCodeAnalysisUniqueImsis);

            // Check that Occurrences and Events match.
            assertTrue("Cause Code Analysis grid's Occurrences don't match the number of events in the Sub Cause Code grid.",
                    causeCodeAnalysisOccurrences == subCauseCodeAnalysisNumberOfEvents);
            // Check that Impacted Subscribers and unique IMSIs match.
            assertTrue("Cause Code Analysis grid's Impacted Subscribers don't match the number of unique IMSIs in the Sub Cause Code grid.",
                    causeCodeAnalysisImpactedSubscribers == subCauseCodeAnalysisUniqueImsis);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PopUpException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------- Private Method ----------------------------------------------- //
    private int determineNumberOfUniqueImsis(int rows) throws NoDataException {

        Set<String> imsis = new HashSet<String>();
        String tableRowPrefixXPath = "//div[contains(@id, 'NETWORK_CAUSE_CODE_ANALYSIS_x-auto-')][";
        String tableRowPostfixXPath = "]//td[2]/div[text()]";

        for (int i = 1; i <= rows; i++) {
            imsis.add(selenium.getText("" + tableRowPrefixXPath + i + tableRowPostfixXPath));
        }

        return imsis.size();
    }
}
