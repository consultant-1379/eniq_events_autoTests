/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2014 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsngroup.coreps;

import java.util.List;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.*;
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
    @Qualifier("SGSNCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SGSN-MME Group Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SGSN-MME Group Cause Section");
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
     * Related Test Session: TSID: 13779 Title: Check default menu items for SGSN-MME Group Cause Code Analysis window
     */
    @Test
    public void checkDefaultColumnsGroup() {
        try {
            GroupCreationUtils.makeSgsnGroupFromTopFailingSgsns(workspace, groupManagementWindow, groupManagementEditWindow, selenium);
            workspace.selectDimension(SeleniumConstants.SGSN_MME_GROUP);
            workspace.enterDimensionValueForGroups(GroupManagementConstants.topFailingSgsnMmesGroupName);
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
                fail(formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(
                    GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting SGSN-MME Group");
        }
    }

    // ---------- Private Method ----------
    /**
     * This method formats the error message.
     * 
     * @param errorMessage
     *        - The error message to output.
     * @param items
     *        - List of items.
     * @return String - Formatted error message to be used in selenium failure.
     */
    private String formatErrorMessage(final String errorMessage, final List<String> items) {
        final String commaSpace = ", ";
        final StringBuilder stringBuilder = new StringBuilder(errorMessage);
        stringBuilder.append(": (");
        for (final String menuOption : items) {
            stringBuilder.append(menuOption);
            stringBuilder.append(commaSpace);
        }
        final int lastIndex = stringBuilder.lastIndexOf(commaSpace);
        stringBuilder.replace(lastIndex, stringBuilder.length(), "");
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
