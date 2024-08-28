/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2014 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controllergroup.coreps;

import java.util.List;
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
    private GroupManagementWindow groupManagementWindow;
    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;
    @Autowired
    @Qualifier("ControllerCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;

    @Autowired
    @Qualifier("BSCRanking")
    private CommonWindow bscRankingWindow;

    @Autowired
    @Qualifier("ENodeBRanking")
    private CommonWindow eNodeBRankingWindow;

    @Autowired
    @Qualifier("RNCRanking")
    private CommonWindow rncRankingWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Controller Group Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Controller Group Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    /**
     * Related Test Session: TSID: 13776 Title: Check default menu items for Controller Group Cause Code Analysis window
     */
    @Test
    public void checkDefaultColumns_BSCGroup() {
        try {
            final String bsc = GroupCreationUtils.make2GControllerGroup(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    bscRankingWindow);
            workspace.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
            workspace.enterDimensionValueForGroups(bsc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.CAUSE_CODE, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller Group");
        } catch (final NoDataException e) {
            e.printStackTrace();
            fail("No valid BSCs available");
        }
    }

    @Test
    public void checkDefaultColumns_RNCGroup() {
        try {
            final String rnc = GroupCreationUtils.make3GControllerGroup(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    rncRankingWindow);

            workspace.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
            workspace.enterDimensionValueForGroups(rnc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.CAUSE_CODE, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller Group");
        } catch (final NoDataException e) {
            e.printStackTrace();
            fail("No valid RNCs available");

        }
    }

    @Test
    public void checkDefaultColumns_eNodeBGroup() {
        try {
            final String eNodeB = GroupCreationUtils.make4GControllerGroup(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    eNodeBRankingWindow);
            System.out.println(eNodeB);
            workspace.selectDimension(SeleniumConstants.CONTROLLER_GROUP);
            workspace.enterDimensionValueForGroups(eNodeB);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.CAUSE_CODE, GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller Group");
        } catch (final NoDataException e) {
            e.printStackTrace();
            fail("No valid eNodeBs available");
        }
    }
}
