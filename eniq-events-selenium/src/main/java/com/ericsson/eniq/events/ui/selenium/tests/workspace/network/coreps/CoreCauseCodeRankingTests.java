/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.network.coreps;

import java.util.*;
import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

public class CoreCauseCodeRankingTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("causeCodeRankings")
    private CommonWindow causeCodeRankingsWindow;
    @Autowired
    @Qualifier("networkCauseCodeAnalysis")
    private CommonWindow networkCauseCodeAnalysisWindow;
    @Autowired
    @Qualifier("networkCauseCodeAnalysisGrid")
    private CommonWindow networkCauseCodeAnalysisGrid;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Core(PS) Network Core Cause Code Ranking Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Core(PS) Network Core Cause Code Ranking Section");
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
     * TSID: 14808 - Action 1 Title: Ensure that the ‘Recommended Action’ column performs as desired in the ‘Cause Code Ranking’ grid.
     */
    @Test
    public void openCauseCodeRankingsWindowRecommendedActionNotDisabledByDefault_EQEV_6590() throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        List<String> causeCodeRankingTableHeaders = causeCodeRankingsWindow.getTableHeaders();
        assertFalse("Cause Code Recommended action not disabled by default.",
                causeCodeRankingTableHeaders.contains(GuiStringConstants.RECOMMENDED_ACTION));
    }

    /**
     * TSID: 14808 - Action 2 Title: Ensure that the ‘Recommended Action’ column performs as desired in the ‘Cause Code Ranking’ grid.
     */
    @Test
    public void openCauseCodeRankingsWindowAndTickRecommendedActionCheckBoxRecommendedActionColumnIsDisplayedInTheGrid_EQEV_6590() throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        final List<String> optionalHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.RECOMMENDED_ACTION));
        causeCodeRankingsWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        List<String> causeCodeRankingTableHeaders = causeCodeRankingsWindow.getTableHeaders();
        assertTrue("Cause Code Recommended Action not displayed in grid.",
                causeCodeRankingTableHeaders.contains(GuiStringConstants.RECOMMENDED_ACTION));

        causeCodeRankingsWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
    }

    /**
     * TSID: 14808 - Action 3 Title: Ensure that the ‘Recommended Action’ column performs as desired in the ‘Cause Code Ranking’ grid.
     */
    @Test
    public void openCauseCodeRankingsWindowAndViewPropertiesOfARowRecommendedActionInPropertiesShouldMatchRecommendedActionInGrid_EQEV_6590()
            throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        final List<String> optionalHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.RECOMMENDED_ACTION));
        causeCodeRankingsWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        int recommendedActionColumnIndex = causeCodeRankingsWindow.getTableHeaderIndex(GuiStringConstants.RECOMMENDED_ACTION);
        String recommendedActionValueFromGrid = findFirstTableRowWithValidData(causeCodeRankingsWindow, recommendedActionColumnIndex);
        int selectedRowIndex = causeCodeRankingsWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.RECOMMENDED_ACTION,
                recommendedActionValueFromGrid);
        String recommendedActionValueFromPropertiesPopup = findValueOfPropertyFromPropertiesPopupWindow(causeCodeRankingsWindow, selectedRowIndex,
                GuiStringConstants.RECOMMENDED_ACTION);
        causeCodeRankingsWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        assertEquals("Cause Code Recommended Action in grid did not match Cause Code Recommended Action in properties popup.",
                recommendedActionValueFromGrid, recommendedActionValueFromPropertiesPopup);
    }

    /**
     * TSID: 14809 - Action 1 Title: Ensure that the ‘Cause Code Recommended Action’ and the ‘Sub Cause Code Recommended Action’ columns perform as
     * desired in the ‘Network Cause Code Analysis’ grid.
     */
    @Test
    public void openNetworkCauseCodeAnalysisWindowRecommendedActionsNotDisabledByDefault_EQEV_6590() throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        List<String> causeCodeRankingTableHeaders = causeCodeRankingsWindow.getTableHeaders();
        int causeCodeDescriptionIndex = causeCodeRankingsWindow.getTableHeaderIndex(GuiStringConstants.CAUSE_CODE_DESCRIPTION);
        causeCodeRankingsWindow.clickTableCell(1, causeCodeRankingTableHeaders.get(causeCodeDescriptionIndex));
        closePreviousWindow();
        List<String> networkCauseCodeAnalysisTableHeaders = networkCauseCodeAnalysisWindow.getTableHeaders();
        assertFalse("Sub Cause Code Recommended Action not disabled by default.",
                networkCauseCodeAnalysisTableHeaders.contains(GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION));
        assertFalse("Cause Code Recommended Action not disabled by default.",
                networkCauseCodeAnalysisTableHeaders.contains(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION));
    }

    /**
     * TSID: 14809 - Action 2 Title: Ensure that the ‘Cause Code Recommended Action’ and the ‘Sub Cause Code Recommended Action’ columns perform as
     * desired in the ‘Network Cause Code Analysis’ grid.
     */
    @Test
    public void openNetworkCauseCodeAnalysisWindowAndTickRecommendedActionCheckBoxesRecommendedActionColumnsAreDisplayedInTheGrid_EQEV_6590()
            throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));
        List<String> causeCodeRankingTableHeaders = causeCodeRankingsWindow.getTableHeaders();
        int causeCodeDescriptionIndex = causeCodeRankingsWindow.getTableHeaderIndex(GuiStringConstants.CAUSE_CODE_DESCRIPTION);
        causeCodeRankingsWindow.clickTableCell(1, causeCodeRankingTableHeaders.get(causeCodeDescriptionIndex));
        closePreviousWindow();
        waitForPageLoadingToComplete();
        final List<String> optionalHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION,
                GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION));
        networkCauseCodeAnalysisWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.CAUSE_PROTOCOL_TYPE);
        List<String> networkCauseCodeAnalysisTableHeaders = networkCauseCodeAnalysisWindow.getTableHeaders();
        assertTrue("Sub Cause Code Recommended Action not disabled in grid.",
                networkCauseCodeAnalysisTableHeaders.contains(GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION));
        assertTrue("Cause Code Recommended Action not disabled in grid.",
                networkCauseCodeAnalysisTableHeaders.contains(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION));
        networkCauseCodeAnalysisWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.CAUSE_PROTOCOL_TYPE);
    }

    /**
     * TSID: 14809 - Action 3 Title: Ensure that the ‘Cause Code Recommended Action’ and the ‘Sub Cause Code Recommended Action’ columns perform as
     * desired in the ‘Network Cause Code Analysis’ grid.
     */
    @Test
    public void openNetworkCauseCodeAnalysisWindowAndViewPropertiesOfARowRecommendedActionsInPropertiesShouldMatchRecommendedActionsInGrid_EQEV_6590()
            throws Exception {
        openRankingWindow(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        assertTrue("Can't open Cause Code Ranking page", selenium.isTextPresent("Cause Code Ranking"));

        List<String> causeCodeRankingTableHeaders = causeCodeRankingsWindow.getTableHeaders();
        List<String> optionalHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.RECOMMENDED_ACTION));
        causeCodeRankingsWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        int recommendedActionColumnIndex = causeCodeRankingsWindow.getTableHeaderIndex(GuiStringConstants.RECOMMENDED_ACTION);
        String recommendedActionValueFromGrid = findFirstTableRowWithValidData(causeCodeRankingsWindow, recommendedActionColumnIndex);
        int selectedRowIndex = causeCodeRankingsWindow.getRowNumberWithMatchingValueForGivenColumn(GuiStringConstants.RECOMMENDED_ACTION,
                recommendedActionValueFromGrid);
        causeCodeRankingsWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.RANK);
        int causeCodeDescriptionIndex = causeCodeRankingsWindow.getTableHeaderIndex(GuiStringConstants.CAUSE_CODE_DESCRIPTION);
        causeCodeRankingsWindow.clickTableCell(selectedRowIndex, causeCodeRankingTableHeaders.get(causeCodeDescriptionIndex));
        closePreviousWindow();
        waitForPageLoadingToComplete();

        optionalHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION,
                GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION));
        networkCauseCodeAnalysisWindow.checkInOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.CAUSE_PROTOCOL_TYPE);
        int subCauseCodeRecommendedActionColumnIndex = networkCauseCodeAnalysisWindow
                .getTableHeaderIndex(GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
        String subCauseCodeRecommendedActionValueFromGrid = findFirstTableRowWithValidData(networkCauseCodeAnalysisWindow,
                subCauseCodeRecommendedActionColumnIndex);
        selectedRowIndex = networkCauseCodeAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(
                GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION, subCauseCodeRecommendedActionValueFromGrid);
        String subCauseCodeRecommendedActionValueFromPropertiesPopup = findValueOfPropertyFromPropertiesPopupWindow(networkCauseCodeAnalysisWindow,
                selectedRowIndex, GuiStringConstants.SUB_CAUSE_CODE_RECOMMENDED_ACTION);
        closePropertyWindow();
        int causeCodeRecommendedActionColumnIndex = networkCauseCodeAnalysisWindow
                .getTableHeaderIndex(GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION);
        String causeCodeRecommendedActionValueFromGrid = findFirstTableRowWithValidData(networkCauseCodeAnalysisWindow,
                causeCodeRecommendedActionColumnIndex);
        selectedRowIndex = networkCauseCodeAnalysisWindow.getRowNumberWithMatchingValueForGivenColumn(
                GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION, causeCodeRecommendedActionValueFromGrid);
        String causeCodeRecommendedActionValueFromPropertiesPopup = findValueOfPropertyFromPropertiesPopupWindow(networkCauseCodeAnalysisWindow,
                selectedRowIndex, GuiStringConstants.CAUSE_CODE_RECOMMENDED_ACTION);
        closePropertyWindow();
        networkCauseCodeAnalysisWindow.uncheckOptionalHeaderCheckBoxes(optionalHeaders, GuiStringConstants.CAUSE_PROTOCOL_TYPE);

        assertEquals("Sub cause code recommended action in grid did not match sub cause code recommended action in properties popup.",
                subCauseCodeRecommendedActionValueFromGrid, subCauseCodeRecommendedActionValueFromPropertiesPopup);
        assertEquals("Cause code recommended action in grid did not match cause code recommended action in properties popup.",
                causeCodeRecommendedActionValueFromGrid, causeCodeRecommendedActionValueFromPropertiesPopup);
    }

    @Test
    public void verifyThatNumberOfFailuresIsConsistentInCauseCodeRankingWindowDrillDown() throws InterruptedException, PopUpException,
            NoDataException {

        // Core Cause Code Ranking Grid columns.
        final String causeCodeRankingGridRow = "//div[contains(@id, 'NETWORK_CAUSE_CODE_RANKING_x-auto-')][1]";
        final String causeProtocolTypeColumnXPath = causeCodeRankingGridRow + "//td[2]/div[text()]";
        final String failuresColumnXPath = causeCodeRankingGridRow + "//td[6]/div[text()]";

        // Network Cause Code Analysis Grid column.
        final String networkCauseCodeAnalysisGridRow = "//div[contains(@id, 'EXTRA_NETWORK_CAUSE_CODE_ANALYSIS_x-auto-')][";
        final String networkCauseCodeCauseProtocolTypeColumnXPath = "]//td[1]/div[text()]";
        final String networkCauseCodeOccurrencesColumnXPath = "]//td[6]/div[text()]";
        final String networkCauseCodeImpactedSubscribersColumnXPath = "]//td[7]/div[text()]";

        // Launch Core(PS) Network -> Core Cause Code Ranking -> Core PS.
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();

        // Note the Cause Code Ranking grid's "Cause Protocol Type" and "Failures" column's values.
        String rankingCauseProtocolType = selenium.getText(causeProtocolTypeColumnXPath);
        int rankingFailures = Integer.parseInt(selenium.getText(failuresColumnXPath));
        System.out.println(rankingCauseProtocolType + " - " + rankingFailures);

        // Drill on the "Cause Code Description" column.
        causeCodeRankingsWindow.clickRankingDrills("NETWORK_CAUSE_CODE_ANALYSIS_CC", GuiStringConstants.CAUSE_CODE_DESCRIPTION);
        causeCodeRankingsWindow.closeWindow();

        // Sum the number of "Occurrences" for each row with the same "Cause Protocol Type" as previously noted.
        int row = 1, occurrencesSum = 0;
        while (selenium.isElementPresent(networkCauseCodeAnalysisGridRow + row + "]")) {

            if (selenium.getText(networkCauseCodeAnalysisGridRow + row + networkCauseCodeCauseProtocolTypeColumnXPath).equals(
                    rankingCauseProtocolType)) {
                occurrencesSum += Integer.parseInt(selenium.getText(networkCauseCodeAnalysisGridRow + row + networkCauseCodeOccurrencesColumnXPath));
            }
            row++;
        }

        // Assert that the number of failures matches the number of occurrences.
        assertTrue(
                "The number of failures from the Core Cause Code Ranking grid does not match the number of Occurrences in the Network Cause Code Analysis grid.",
                occurrencesSum == rankingFailures);

        // Assert that the each row's "Occurrences" value is equal to or greater than its "Impacted Subscribers" value.
        row = 1;
        while (selenium.isElementPresent(networkCauseCodeAnalysisGridRow + row + "]")) {
            assertTrue("The number of Impacted Subscribers is exceeding the number of Occurrences in the Network Cause Code Analysis grid.",
                    Integer.parseInt(selenium.getText(networkCauseCodeAnalysisGridRow + row + networkCauseCodeOccurrencesColumnXPath)) >= Integer
                            .parseInt(selenium.getText(networkCauseCodeAnalysisGridRow + row + networkCauseCodeImpactedSubscribersColumnXPath)));
            row++;
        }

    }

    // ---------- Private Methods ----------
    private String findFirstTableRowWithValidData(CommonWindow window, int columnIndex) throws NoDataException {
        int numberOfRows = window.getTableRowCount();
        String valueFromGrid = null;
        for (int i = 0; i < numberOfRows; i++) {
            try {
                valueFromGrid = window.getTableData(i, columnIndex);
                break;
            } catch (NoDataException noDataException) {
                if (i == (numberOfRows - 1)) {
                    throw noDataException;
                }
            }
        }
        return valueFromGrid;
    }

    private String findValueOfPropertyFromPropertiesPopupWindow(CommonWindow window, int rowIndex, String propertyName) {
        String xPathForRowInGrid = "//div[@class='x-grid3-body']/div[" + (rowIndex + 1) + "][contains(concat(' ',@class,' '), ' x-grid3-row ')]";
        selenium.click(xPathForRowInGrid);
        window.clickButton(SelectedButtonType.PROPERTY_BUTTON);

        String xPathForPropertiesPopup = "//*[@id='selenium_tag_PropertiesWindow']//div[contains(concat(' ',@class,' '), ' contentPanel ')]";
        String[] propertyNamesAndValues = selenium.getText(xPathForPropertiesPopup).split("\n\n");
        String propertyValue = null;
        for (String propertyNameAndValue : propertyNamesAndValues) {
            if (propertyNameAndValue.startsWith(propertyName)) {
                propertyValue = propertyNameAndValue.split(propertyName + ": ")[1];
                break;
            }
        }
        return propertyValue;
    }

    private void openRankingWindow(String categoryPanel, String windowOption) throws Exception {

        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        pause(2000);
        workspace.selectWindowType(categoryPanel, windowOption);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
        pause(2000);
    }

    private void closePreviousWindow() {
        String closeButtonXPath = "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";
        selenium.click(closeButtonXPath);
    }

    private void closePropertyWindow() {
        selenium.click("//div[@id='selenium_tag_PropertiesWindow']//table[@class='panelAlign']//button[@class='gwt-Button button']");
    }
}
