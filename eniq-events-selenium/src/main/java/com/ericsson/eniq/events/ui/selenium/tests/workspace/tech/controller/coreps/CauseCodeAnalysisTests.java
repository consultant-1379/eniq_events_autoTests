/* COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.controller.coreps;

import java.util.List;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

public class CauseCodeAnalysisTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    @Qualifier("ControllerCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;

    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

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
        logger.log(Level.INFO, "Start of Controller Cause Code Analysis Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Controller Cause Code Analysis Section");
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
     * Related Test Session: TSID: 13774 Title: Check default menu items for Controller Cause Code Analysis window.
     */
    @Test
    public void checkDefaultColumns_BSC() {
        final String bsc = findBSCFromBSCRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(bsc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.MENU_CAUSE_CODE_ID, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.RAT_ID);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    @Test
    public void checkDefaultColumns_eNodeB() {
        final String eNodeB = findENodeBFromENodeBRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(eNodeB);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.MENU_CAUSE_CODE_ID, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.RAT_ID);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    @Test
    public void checkDefaultColumns_RNC() {
        final String rnc = findRNCFromRNCRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(rnc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.MENU_CAUSE_CODE_ID, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.RAT_ID);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_BSC() {
        final String bsc = findBSCFromBSCRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(bsc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            try {
                networkCauseCodeAnalysisWindow.clickTableCell(0, GuiStringConstants.SUB_CAUSE_CODE);
            } catch (final NoDataException e) {
                fail("Sub Cause Code Analysis window has no data.");
            }

            final List<String> shouldBeUnselectedList = networkCauseCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_eNodeB() {
        final String eNodeB = findENodeBFromENodeBRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(eNodeB);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            try {
                networkCauseCodeAnalysisWindow.clickTableCell(0, GuiStringConstants.SUB_CAUSE_CODE);
            } catch (final NoDataException e) {
                fail("Sub Cause Code Analysis window has no data.");
            }

            final List<String> shouldBeUnselectedList = networkCauseCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_RNC() {
        final String rnc = findRNCFromRNCRankingWindow();
        try {
            workspace.selectDimension(SeleniumConstants.CONTROLLER);
            workspace.enterDimensionValue(rnc);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            try {
                networkCauseCodeAnalysisWindow.clickTableCell(0, GuiStringConstants.SUB_CAUSE_CODE);
            } catch (final NoDataException e) {
                fail("Sub Cause Code Analysis window has no data.");
            }

            final List<String> shouldBeUnselectedList = networkCauseCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting Controller");
        }
    }

    private String findBSCFromBSCRankingWindow() {
        String bsc = "";
        try {
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_BSC);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int bscIndex = bscRankingWindow.getTableHeaderIndex(GuiStringConstants.BSC);
            final int totalRows = bscRankingWindow.getTableRowCount();

            for (int rowNumber = 1; rowNumber <= totalRows; rowNumber++) {
                bsc = bscRankingWindow.getTableData(rowNumber, bscIndex);
                if (bsc.contains(":")) {
                    break;
                }
            }
            bscRankingWindow.closeWindow();

        } catch (final Exception e) {
            fail("No usable BSCs found in BSC Ranking Window.");
        }

        return bsc;
    }

    private String findRNCFromRNCRankingWindow() {
        String rnc = "";
        try {
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_RNC);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int rncIndex = rncRankingWindow.getTableHeaderIndex(GuiStringConstants.RNC);
            final int totalRows = rncRankingWindow.getTableRowCount();

            for (int rowNumber = 1; rowNumber <= totalRows; rowNumber++) {
                rnc = rncRankingWindow.getTableData(rowNumber, rncIndex);
                if (rnc.contains(":")) {
                    break;
                }
            }
            rncRankingWindow.closeWindow();

        } catch (final Exception e) {
            fail("No usable RNCs found in RNC Ranking Window.");
        }

        return rnc;
    }

    private String findENodeBFromENodeBRankingWindow() {
        String eNodeB = "";
        try {
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_ENODEB);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int eNodeBIndex = eNodeBRankingWindow.getTableHeaderIndex(GuiStringConstants.CONTROLLER);
            final int totalRows = eNodeBRankingWindow.getTableRowCount();

            for (int rowNumber = 1; rowNumber <= totalRows; rowNumber++) {
                eNodeB = eNodeBRankingWindow.getTableData(rowNumber, eNodeBIndex);
                if (eNodeB.contains(":")) {
                    break;
                }
            }
            eNodeBRankingWindow.closeWindow();

        } catch (final Exception e) {
            e.printStackTrace();
            fail("No usable eNodeBs found in eNodeB Ranking Window.");
        }

        return eNodeB;
    }
}
