package com.ericsson.eniq.events.ui.selenium.tests.workspace.network.coreps;

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

/**
 * @author elukpot
 * @since 14.0.15
 */
public class CoreRankingTests extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private WorkspaceRC workspace;
    @Autowired
    @Qualifier("CoreRanking")
    private CommonWindow coreRankingWindow;
    @Autowired
    @Qualifier("BSCRanking")
    private CommonWindow bscRankingWindow;
    @Autowired
    @Qualifier("RNCRanking")
    private CommonWindow rncRankingWindow;
    @Autowired
    @Qualifier("ENodeBRanking")
    private CommonWindow eNodeBRankingWindow;
    @Autowired
    @Qualifier("termRankings")
    private CommonWindow terminalRankingWindow;
    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow eventAnalysisWindow;
    @Autowired
    @Qualifier("terminalEventAnalysis")
    private CommonWindow terminalEventAnalysisWindow;
    @Autowired
    @Qualifier("terminalFailedEventAnalysis")
    private CommonWindow terminalFailedEventAnalysisWindow;
    @Autowired
    private GroupManagementWindow groupManagementWindow;
    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Core(PS) Network Core Ranking Section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Core(PS) Network Core Ranking Section");
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

    @Test
    public void bscRankingTest() throws NoDataException, InterruptedException, PopUpException {

        openBSCRankingWindow();

        int bscRankingNoOfFailures;
        try {
            bscRankingNoOfFailures = Integer.parseInt(bscRankingWindow.getTableData(0,
                    bscRankingWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
        } catch (IndexOutOfBoundsException e) {
            throw new NoDataException("No Data for BSC Ranking Window.");
        }

        bscRankingWindow.clickRankingDrills("NETWORK_EVENT_ANALYSIS_BSC", GuiStringConstants.BSC);
        coreRankingWindow.closeWindow();

        int controllerEventAnalysisNoOfFailures = sumNoOfFailuresInDetailedEventAnalysis();

        assertEquals("The number of failures in the BSC Ranking grid does not match the number of failures in the Controller Event Analysis Window.",
                bscRankingNoOfFailures, controllerEventAnalysisNoOfFailures);

        controllerEventAnalysisNoOfFailures = drillOnFailuresInDetailedEventAnalysis(eventAnalysisWindow);
        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(eventAnalysisWindow);

        if (controllerEventAnalysisNoOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Controller Event Analysis Window does not match the number of events in the Failed Event Analysis Window.",
                    controllerEventAnalysisNoOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }
    }

    @Test
    public void rncRankingTest() throws InterruptedException, PopUpException, NoDataException {

        openRNCRankingWindow();

        int rncRankingNumberOfFailures;
        try {
            rncRankingNumberOfFailures = Integer.parseInt(rncRankingWindow.getTableData(0,
                    rncRankingWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
        } catch (IndexOutOfBoundsException e) {
            throw new NoDataException("No Data for RNC Ranking Window.");
        }

        coreRankingWindow.clickRankingDrills("NETWORK_EVENT_ANALYSIS_BSC", GuiStringConstants.RNC);
        coreRankingWindow.closeWindow();

        int controllerEventAnalysisNoOfFailures = sumNoOfFailuresInDetailedEventAnalysis();

        assertEquals("The number of failures in the RNC Ranking grid does not match the number of failures in the Controller Event Analysis Window.",
                rncRankingNumberOfFailures, controllerEventAnalysisNoOfFailures);

        controllerEventAnalysisNoOfFailures = drillOnFailuresInDetailedEventAnalysis(eventAnalysisWindow);
        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(eventAnalysisWindow);

        if (controllerEventAnalysisNoOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Controller Event Analysis Window does not match the number of events in the Failed Event Analysis Window.",
                    controllerEventAnalysisNoOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }

    }

    @Test
    public void eNodeBRankingTest() throws InterruptedException, PopUpException, NoDataException {
        openeNodeBRankingWindow();

        int eNodeBRankingNumberOfFailures;
        try {
            eNodeBRankingNumberOfFailures = Integer.parseInt(eNodeBRankingWindow.getTableData(0,
                    eNodeBRankingWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
        } catch (IndexOutOfBoundsException e) {
            throw new NoDataException("No Data for RNC Ranking Window.");
        }
        coreRankingWindow.clickRankingDrills("NETWORK_EVENT_ANALYSIS_BSC", GuiStringConstants.CONTROLLER);
        coreRankingWindow.closeWindow();

        int controllerEventAnalysisNoOfFailures = sumNoOfFailuresInDetailedEventAnalysis();

        assertEquals(
                "The number of failures in the eNodeB Ranking grid does not match the number of failures in the Controller Event Analysis Window.",
                eNodeBRankingNumberOfFailures, controllerEventAnalysisNoOfFailures);

        controllerEventAnalysisNoOfFailures = drillOnFailuresInDetailedEventAnalysis(eventAnalysisWindow);
        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(eventAnalysisWindow);

        if (controllerEventAnalysisNoOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Controller Event Analysis Window does not match the number of events in the Failed Event Analysis Window.",
                    controllerEventAnalysisNoOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }

    }

    @Test
    public void terminalRankingTest_failuresDrill() throws NoDataException, PopUpException, InterruptedException {

        openTerminalRankingWindow();

        int terminalRankingNumberOfFailures;
        try {
            terminalRankingNumberOfFailures = Integer.parseInt(terminalRankingWindow.getTableData(0,
                    terminalRankingWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
        } catch (IndexOutOfBoundsException e) {
            throw new NoDataException("No Data for RNC Ranking Window.");
        }
        terminalRankingWindow.clickRankingDrills("TERMINAL_RANKINGS_DRILL_ON_FAILURES", GuiStringConstants.FAILURES);

        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(terminalFailedEventAnalysisWindow);

        if (terminalRankingNumberOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Terminal Ranking Window does not match the number of events in the Failed Event Analysis Window.",
                    terminalRankingNumberOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }

    }

    @Test
    public void terminalRankingTest_manufacturerDrill() throws NoDataException, PopUpException, InterruptedException {

        openTerminalRankingWindow();

        terminalRankingWindow.clickRankingDrills("TERMINAL_EVENT_ANALYSIS_MAKE", GuiStringConstants.MANUFACTURER);
        terminalRankingWindow.closeWindow();

        int manufacturerEventAnalysisNoOfFailures = drillOnFailuresInDetailedEventAnalysis(terminalEventAnalysisWindow);
        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(terminalEventAnalysisWindow);

        if (manufacturerEventAnalysisNoOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Manufacture Detailed Event Analysis Window does not match the number of events in the Failed Event Analysis Window.",
                    manufacturerEventAnalysisNoOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }

    }

    @Test
    public void terminalRankingTest_modelDrill() throws NoDataException, PopUpException, InterruptedException {

        openTerminalRankingWindow();
        terminalRankingWindow.clickRankingDrills("TERMINAL_EVENT_ANALYSIS_MODEL", GuiStringConstants.MODEL);
        terminalRankingWindow.closeWindow();

        int modelEventAnalysisNoOfFailures = drillOnFailuresInDetailedEventAnalysis(terminalEventAnalysisWindow);
        int noOfEvents = countNoOfEventsInFailedEventsAnalysis(terminalEventAnalysisWindow);

        if (modelEventAnalysisNoOfFailures < 500) {
            assertEquals(
                    "The number of failures in the Model Detailed Event Analysis Window does not match the number of events in the Failed Event Analysis Window.",
                    modelEventAnalysisNoOfFailures, noOfEvents);
        } else {
            assertEquals("The number of events in the Failed Event Analysis Window exceeds 500.", 500, noOfEvents);
        }

    }

    private void openBSCRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.BSC);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openRNCRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.RNC);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openeNodeBRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.eNodeB);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openTerminalRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.TERMINAL);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private int sumNoOfFailuresInDetailedEventAnalysis() throws NoDataException {
        int controllerEventAnalysisNoOfFailures = 0;
        for (int i = 0; i < eventAnalysisWindow.getTableRowCount(); i++) {
            controllerEventAnalysisNoOfFailures = controllerEventAnalysisNoOfFailures
                    + Integer.parseInt(eventAnalysisWindow.getTableData(i, eventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
        }
        return controllerEventAnalysisNoOfFailures;
    }

    private int drillOnFailuresInDetailedEventAnalysis(CommonWindow eventAnalysisWindow) throws NoDataException, PopUpException {
        int eventAnalysisNoOfFailures = 0;
        for (int row = 0; row < eventAnalysisWindow.getTableRowCount(); row++) {
            eventAnalysisNoOfFailures = Integer.parseInt(eventAnalysisWindow.getTableData(row,
                    eventAnalysisWindow.getTableHeaderIndex(GuiStringConstants.FAILURES)));
            if (eventAnalysisNoOfFailures > 0) {
                eventAnalysisWindow.clickTableCell(row, GuiStringConstants.FAILURES);
                break;
            }
        }
        return eventAnalysisNoOfFailures;
    }

    private int countNoOfEventsInFailedEventsAnalysis(CommonWindow eventAnalysisWindow) throws NoDataException {
        int noOfEvents = 0;
        for (int page = 0; page < eventAnalysisWindow.getPageCount(); page++) {
            noOfEvents = noOfEvents + eventAnalysisWindow.getTableRowCount();
            eventAnalysisWindow.clickNextPage();
        }
        return noOfEvents;
    }
}
