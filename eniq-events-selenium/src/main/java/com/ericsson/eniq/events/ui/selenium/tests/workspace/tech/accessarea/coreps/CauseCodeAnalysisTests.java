/* COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.accessarea.coreps;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.WorkspaceUtils;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.GuiCallback;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.logging.Level;

public class CauseCodeAnalysisTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired private WorkspaceRC workspace;
    @Autowired @Qualifier("AccessAreaCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;

    @Autowired @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

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

    /** TSID: 13777
     * Title: Check default menu items for Access Area Cause Code Analysis window. */
    @Test public void checkDefaultColumns_EQEV_8267_AccessArea(){
        try {
            workspace.selectDimension(SeleniumConstants.ACCESS_AREA);
            workspace.enterDimensionValue("LTE01ERBS00001-2,,ONRM_RootMo_R:LTE01ERBS00001,Ericsson,LTE");
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            waitForPageLoadingToComplete();

            //Check the default selected menu items
            List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE,
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
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.RAT_ID);
            if(shouldBeUnselectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }


        } catch (PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting Access Area");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_AccessArea() throws NoDataException{
        try {
            workspace.selectDimension(SeleniumConstants.ACCESS_AREA);
            workspace.enterDimensionValue("LTE01ERBS00001-2,,ONRM_RootMo_R:LTE01ERBS00001,Ericsson,LTE");
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            waitForPageLoadingToComplete();
            causeCodeAnalysisWindow.openGridView();
            networkCauseCodeAnalysisWindow.clickTableCell(0, GuiStringConstants.SUB_CAUSE_CODE);

            List<String> shouldBeUnselectedList = networkCauseCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);

            if(shouldBeUnselectedList.size() != 0){
                fail(WorkspaceUtils.formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (InterruptedException e) {
            fail("There was a fault setting Access Area");
        }
    }
}
