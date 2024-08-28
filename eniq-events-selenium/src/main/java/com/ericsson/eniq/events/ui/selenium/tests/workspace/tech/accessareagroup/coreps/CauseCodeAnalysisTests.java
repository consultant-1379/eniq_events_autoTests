/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2014 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.accessareagroup.coreps;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
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

import java.util.List;
import java.util.logging.Level;

/**
 * @author elukpot
 * @since 14.0.14
 */
public class CauseCodeAnalysisTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired private WorkspaceRC workspace;
    @Autowired @Qualifier("AccessAreaCauseCodeAnalysis") private CommonWindow causeCodeAnalysisWindow;

    @BeforeClass public static void openLog() {
        logger.log(Level.INFO, "Start of Access Area Cause Code Analysis Section");
    }
    @AfterClass public static void closeLog() {
        logger.log(Level.INFO, "End of Access Area Cause Code Analysis Section");
    }
    @Override @Before public void setUp() {
        super.setUp();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    /** TSID: 13778
     * Title: Check default menu items for Access Area Group Cause Code Analysis window. */
    @Test public void checkDefaultColumnsGroup_EQEV_8267_AccessAreaGroup(){
        try {
            workspace.selectDimension(SeleniumConstants.ACCESS_AREA_GROUP);
            workspace.enterDimensionValueForGroups("AutomationAccessAreaGroup");
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            waitForPageLoadingToComplete();

            //Check the default selected menu items
            List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.CAUSE_CODE,
                    GuiStringConstants.SUB_CAUSE_CODE,
                    GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.OCCURRENCES,
                    GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if(shouldBeSelectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.MENU_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if(shouldBeUnselectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }


        } catch (PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting Access Area Group");
        }
    }
}
