/* COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.tech.sgsn.coreps;

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
    @Qualifier("SGSNCauseCodeAnalysis")
    private CommonWindow causeCodeAnalysisWindow;
    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;

    @Autowired
    @Qualifier("subscriberrankingcore")
    private CommonWindow subscriberRankingWindow;

    private String sgsn = "";

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SGSN-MME Cause Code Analysis Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SGSN-MME Cause Code Analysis Section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
        sgsn = findSGSNFromSubscriberRanking_DrillOnFailures_Window();
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    /**
     * Related Test Session: TSID: 13780 Title: Check default menu items for SGSN-MME Cause Code Analysis window
     */
    @Test
    public void checkDefaultColumns_Sgsn() {
        try {
            workspace.selectDimension(SeleniumConstants.SGSN_MME);
            workspace.enterDimensionValue(sgsn);
            workspace.selectWindowType(GuiStringConstants.LMAW_CAUSE_CODE_ANALYSIS, GuiStringConstants.LMAW_CORE_PS);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            causeCodeAnalysisWindow.openGridView();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            //Check the default selected menu items
            final List<String> shouldBeSelectedList = causeCodeAnalysisWindow.isSelectedMenuOptions(GuiStringConstants.MENU_SUB_CAUSE_CODE_ID,
                    GuiStringConstants.SUB_CAUSE_CODE, GuiStringConstants.OCCURRENCES, GuiStringConstants.IMPACTED_SUBSCRIBERS);
            if (shouldBeSelectedList.size() != 0) {
                fail(formatErrorMessage("The following Menu Items should have been selected by default", shouldBeSelectedList));
            }

            //Check the default unselected menu items
            final List<String> shouldBeUnselectedList = causeCodeAnalysisWindow.isUnselectedMenuOptions(GuiStringConstants.CAUSE_PROTOCOL_TYPE,
                    GuiStringConstants.MENU_CAUSE_CODE_ID, GuiStringConstants.CAUSE_CODE, GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION,
                    GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
            if (shouldBeUnselectedList.size() != 0) {
                fail(formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }

        } catch (final PopUpException e) {
            fail("Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting SGSN-MME");
        }
    }

    @Test
    public void checkHiddenColumnsOnSubCauseCodeDrill_Sgsn() {
        try {
            workspace.selectDimension(SeleniumConstants.SGSN_MME);
            workspace.enterDimensionValue(sgsn);
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
                fail(formatErrorMessage("The following Menu Items should have been unselected by default", shouldBeUnselectedList));
            }
        } catch (final PopUpException e) {
            fail("Sub Cause Code Analysis window did not open.");
        } catch (final InterruptedException e) {
            fail("There was a fault setting SGSN-MME");
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

    private String findSGSNFromSubscriberRanking_DrillOnFailures_Window() {
        String sgsn = "";
        try {
            // Open the SGSN Ranking grid.
            workspace.selectTimeRange(SeleniumConstants.DATE_TIME_1HOUR);

            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);

            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            // Drill on the Top Failing IMSI.
            selenium.click("//div[contains(@id, 'SUBSCRIBER_RANKING_CORE_x-auto')][1]//span[@id='SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED']");
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            // Sort the SGSN-MME Column to get a value.
            selenium.click("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//span[text()='SGSN-MME']");
            sgsn = selenium
                    .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//div[contains(@id,'SUBSCRIBER_EVENT_ANALYSIS_x-auto-')][1]//td//span[@id='SUBSCRIBER_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_SGSN'][text()]");

            // Close the open ranking grids.
            final String closeWindowButton = "//div[contains(@class, 'x-tool-close')]";
            while (selenium.isElementPresent(closeWindowButton)) {
                selenium.click(closeWindowButton);
            }
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sgsn;
    }
}
