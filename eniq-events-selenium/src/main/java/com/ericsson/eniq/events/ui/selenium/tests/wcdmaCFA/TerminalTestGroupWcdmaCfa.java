package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.NavigationController.SwitchViewDropDown;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab.TerminalType;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class TerminalTestGroupWcdmaCfa extends BaseWcdmaCfa {

	@Autowired
	private TerminalTab terminalTab;

	@Autowired
	@Qualifier("rankingsTerminalWcdmaCfa")
	private CommonWindow terminalCfaWindow;

	@Autowired
	@Qualifier("terminalGroupAnalysisWcdmaCfa")
	private CommonWindow terminalGroupAnalysisCfaWindow;

	@Autowired
	@Qualifier("terminalAnalysisCfaMostDrops")
	private CommonWindow terminalAnalysisCfaMostDropsWindow;

	@Autowired
	@Qualifier("terminalEventAnalysisWcdmaCfa")
	private CommonWindow terminalEventAnalysisWcdmaCfaWindow;

	private final static String CFA_WCDMA = GuiStringConstants.DASH
			+ "Call Failure Analysis" + GuiStringConstants.DASH + "WCDMA";

	// Launch Button Sub Menus

	private final static List<TerminalTab.SubStartMenu> subMenuCallFailureAnalysis = Arrays
			.asList(TerminalTab.SubStartMenu.EVENT_RANKING,
					TerminalTab.SubStartMenu.TERMINAL_RANKING_RAN_MENU_ITEM_WCDMA,
					TerminalTab.SubStartMenu.TERMINAL_RANKING_WCDMA_MENU_ITEM_WCDMA,
					TerminalTab.SubStartMenu.TERMINAL_CALL_FAILURE_ANALYSIS);

	private final static List<TerminalTab.SubStartMenu> subMenuTerminalGroupCfa = Arrays
			.asList(TerminalTab.SubStartMenu.TERMINAL_GROUP_RAN_MENU_ITEM_WCDMA,
					TerminalTab.SubStartMenu.TERMINAL_GROUP_WCDMA_MENU_ITEM_WCDMA,
					TerminalTab.SubStartMenu.TERMINAL_GROUP_ANALYSIS_CFA_CALL_DROPS);

	private final static List<TerminalTab.SubStartMenu> subMenuTerminalAnalysisCfaMostDrops = Arrays
			.asList(TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_RAN_MENU_ITEM_WDCMA,
					TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_WCDMA_MENU_ITEM_WDCMA,
					TerminalTab.SubStartMenu.TERMINAL_ANALYSIS_CFA_MOST_DROPS);

	private final List<TerminalTab.SubStartMenu> subMenuTerminalWcdmaCfa = Arrays
			.asList(TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_RAN_MENU_ITEM_WDCMA,
					TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_WCDMA_MENU_ITEM_WDCMA,
					TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_WCDMA_CFA);

	// Default Column Headers

	private final List<String> defaultTerminalAnalysisCfaMostDropsColumns = Arrays
			.asList(GuiStringConstants.RANK, GuiStringConstants.TAC,
					GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL,
					GuiStringConstants.FAILURES);

	private final List<String> defaultTerminalEventAnalysisCfaColumns = Arrays
			.asList(GuiStringConstants.MANUFACTURER, GuiStringConstants.MODEL,
					GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
					GuiStringConstants.IMPACTED_SUBSCRIBERS);

	private final List<String> defaultTerminalGroupEventAnalysisColumns = Arrays
			.asList(GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES,
					GuiStringConstants.IMPACTED_SUBSCRIBERS);

	private final List<String> defaultTerminalGroupAnalysisColumns = Arrays
			.asList(GuiStringConstants.RANK, GuiStringConstants.GROUP_NAME,
					GuiStringConstants.CALL_DROPS,
					GuiStringConstants.IMPACTED_SUBSCRIBERS);

	@Override
	@Before
	public void setUp() {
		super.setUp();

		terminalTab.openTab();

		// terminalTab.setPacketSwitchedMenu(PacketSwitchedMenuOptions.CORE_DATA);
	}

	@Test
	public void terminalTabVerifyTerminalCallFailureAnalysisRanking_5_6_1()
			throws PopUpException {

		pause(4000);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_RANKINGS,
				subMenuCallFailureAnalysis);

		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = "Terminal WCDMA Call Failure : Event Ranking";
		} else {
			expectedWindowTitle = "Terminal WCDMA Call Failure Event Ranking";
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ defaultTerminalCfaRankingColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders.equals(defaultTerminalCfaRankingColumns));
	}

	@Test
	public void terminalTabFromTerminalCallFailureAnalysisRankingVerifyFailedEventAnalysis_5_6_2()
			throws PopUpException, NoDataException {

		final List<String> callDropsAndSetupFailureFailedEventAnalysisColumns = new ArrayList<String>();
		callDropsAndSetupFailureFailedEventAnalysisColumns
				.addAll(defaultTerminalAnalysisCfaMostDropsColumns);
		// callDropsAndSetupFailureFailedEventAnalysisColumns.add(RRC_ESTABLISHMENT_CAUSE);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_RANKINGS,
				subMenuCallFailureAnalysis);

		terminalCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile();

		final String tac = terminalCfaWindow.getTableData(0, 3);
		// final String expectedWindowTitle = tac + GuiStringConstants.DASH +
		// GuiStringConstants.FAILED_EVENT_ANALYSIS;

		terminalCfaWindow.clickTableCell(0, GuiStringConstants.FAILURES);

		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = tac + GuiStringConstants.DASH
					+ GuiStringConstants.WCDMA_CALL_FAILURE
					+ GuiStringConstants.SPACE
					+ GuiStringConstants.EVENT_ANALYSIS;
		} else {
			expectedWindowTitle = tac + GuiStringConstants.DASH
					+ GuiStringConstants.TERMINAL_EVENT_ANALYSIS
					+ GuiStringConstants.DASH
					+ GuiStringConstants.CALL_FAILURE_ANALYSIS
					+ GuiStringConstants.DASH + GuiStringConstants.WCDMA;
		}
		terminalCfaWindow.clickTableCell(0, GuiStringConstants.FAILURES);
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ callDropsAndSetupFailureFailedEventAnalysisColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(
				FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders
						.containsAll(callDropsAndSetupFailureFailedEventAnalysisColumns));
	}

	@Test
	public void terminalTabVerifyGroupAnalysisForTerminalGroupForCallDropsOnly_5_6_3()
			throws PopUpException, InterruptedException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_GROUP_ANALYSIS,
				subMenuTerminalGroupCfa);

		terminalGroupAnalysisCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();
		pause(3000);

		final String expectedChartTitle = "Most Call Drops Summary";
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedChartTitle,
				selenium.isTextPresent(expectedChartTitle));

		terminalGroupAnalysisCfaWindow.toggleGraphToGrid();

		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE
					+ GuiStringConstants.SPACE + "Group Analysis";
		} else {
			expectedWindowTitle = GuiStringConstants.GROUP_ANALYSIS_WCDMA
					+ expectedChartTitle;
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalGroupAnalysisCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ defaultTerminalGroupAnalysisColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders.equals(defaultTerminalGroupAnalysisColumns));

		// Toggle back to graph
		terminalGroupAnalysisCfaWindow.toggleGraphToGrid();
		pause(3000);

		terminalGroupAnalysisCfaWindow.chartRefresh();
		pause(3000);
	}

	@Test
	public void terminalTabFromTerminalGroupAnalysisBasedOnCallDropsVerifyDrilldownToEventSummary_5_6_4_PARTIAL()
			throws PopUpException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_GROUP_ANALYSIS,
				subMenuTerminalGroupCfa);
		pause(2000);

		terminalGroupAnalysisCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();
		pause(2000);

		String expectedChartTitle = "Most Call Drops Summary";
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedChartTitle,
				selenium.isTextPresent(expectedChartTitle));

		terminalGroupAnalysisCfaWindow.drillOnChartObject(1);

		terminalGroupAnalysisCfaWindow
				.switchView(SwitchViewDropDown.MOST_CALL_DROPS);
		// terminalGroupAnalysisCfaWindow.switchView(SwitchViewDropDown.MOST_CALL_SETUP_FAILURES);
		pause(2000);

		expectedChartTitle = "WCDMA Call Failure Group Analysis";
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedChartTitle,
				selenium.isTextPresent(expectedChartTitle));

		terminalGroupAnalysisCfaWindow.drillOnChartObject(2);

		terminalGroupAnalysisCfaWindow
				.switchView(SwitchViewDropDown.MOST_CALL_DROPS);
		pause(2000);

		terminalGroupAnalysisCfaWindow.toggleGraphToGrid();

		final String expectedWindowTitle = GuiStringConstants.GROUP_ANALYSIS_WCDMA
				+ "Most Call Drops Summary";
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> groupAnalysisColumnsMostCallSetupFailures = defaultTerminalGroupAnalysisColumns;
		groupAnalysisColumnsMostCallSetupFailures.set(2, "Call Drops");

		final List<String> actualWindowHeaders = terminalGroupAnalysisCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ groupAnalysisColumnsMostCallSetupFailures
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders
						.equals(groupAnalysisColumnsMostCallSetupFailures));
	}

	@Test
	public void terminalTabVerifyGroupAnalysisForTerminalGroupForCallSetupFailuresOnly_5_6_5_PARTIAL()
			throws PopUpException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_GROUP_ANALYSIS,
				subMenuTerminalGroupCfa);

		terminalGroupAnalysisCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();
		pause(3000);

		terminalGroupAnalysisCfaWindow
				.switchView(SwitchViewDropDown.MOST_CALL_SETUP_FAILURES);

		final String expectedChartTitle = "WCDMA Call Failure Group Analysis";
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedChartTitle,
				selenium.isTextPresent(expectedChartTitle));

		terminalGroupAnalysisCfaWindow.toggleGraphToGrid();

		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE
					+ GuiStringConstants.SPACE + "Group Analysis";
		} else {
			expectedWindowTitle = GuiStringConstants.GROUP_ANALYSIS_WCDMA
					+ "Most Call Setup Failures Summary";
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));
	}

	@Test
	public void terminalTabFromTerminalGroupAnalysisBasedOnCallSetupFailuresVerifyDrilldownToEventSummary_5_6_6_PARTIAL()
			throws PopUpException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_GROUP_ANALYSIS,
				subMenuTerminalGroupCfa);

		terminalGroupAnalysisCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();
		pause(3000);

		final String[] callDropChartWindowText = { "Number of Events",
				"Group Name", "Call Drops", "Impacted Subscribers",
				"Drag To Zoom", TERMINAL_GROUP };

		verifyChartWindowTextValues(callDropChartWindowText);

		terminalGroupAnalysisCfaWindow
				.switchView(SwitchViewDropDown.MOST_CALL_SETUP_FAILURES);

		final String[] callSetupFailureChartWindowText = { "Number of Events",
				"Group Name", "Call Setup Failures", "Impacted Subscribers",
				"Drag To Zoom", TERMINAL_GROUP };

		verifyChartWindowTextValues(callSetupFailureChartWindowText);
	}

	@Test
	public void terminalTabVerifyTerminalAnalysisForTerminalForMostCallDropsOnly_5_6_7()
			throws PopUpException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_ANALYSIS_WCDMA,
				subMenuTerminalAnalysisCfaMostDrops);

		assertTrue(
				GuiStringConstants.ERROR_LOADING
						+ GuiStringConstants.TERMINAL_ANALYSIS_MOST_CALL_DROPS,
				selenium.isTextPresent(GuiStringConstants.TERMINAL_ANALYSIS_MOST_CALL_DROPS));

		final List<String> actualWindowHeaders = terminalAnalysisCfaMostDropsWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ defaultTerminalAnalysisCfaMostDropsColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders
						.equals(defaultTerminalAnalysisCfaMostDropsColumns));
	}

	@Test
	public void terminalTabVerifyTerminalAnalysisForTerminalForMostCallDropsViewFailureDrillDown_5_6_8()
			throws PopUpException, NoDataException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_ANALYSIS_WCDMA,
				subMenuTerminalAnalysisCfaMostDrops);

		terminalAnalysisCfaMostDropsWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();

		terminalAnalysisCfaMostDropsWindow.clickTableCell(0, 4,
				GuiStringConstants.GRID_CELL_LINK);

		final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));
		final List<String> expectedHeaders = callDropsFailedEventAnalysisColumns;
		//expectedHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE;)
		double version = PropertyReader.getInstance().getEniqRootVersion();
		if(version >= 13.0){
			expectedHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
		}
		final List<String> actualWindowHeaders = terminalAnalysisCfaMostDropsWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ expectedHeaders
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders.equals(expectedHeaders));
	}

	@Test
	public void terminalTabVerifyTerminalAnalysisForTerminalForMostCallSetupFailuresOnly_5_6_9()
			throws PopUpException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_ANALYSIS_WCDMA,
				subMenuTerminalAnalysisCfaMostDrops);

		terminalAnalysisCfaMostDropsWindow
				.switchView(SwitchViewDropDown.MOST_CALL_SETUP_FAILURES);

		assertTrue(
				GuiStringConstants.ERROR_LOADING
						+ GuiStringConstants.TERMINAL_ANALYSIS_MOST_CALL_SETUP_FAILURES,
				selenium.isTextPresent(GuiStringConstants.TERMINAL_ANALYSIS_MOST_CALL_SETUP_FAILURES));
	}

	@Test
	public void terminalTabVerifyTerminalAnalysisForTerminalForMostCallSetupFailuresViewFailureDrillDown_5_6_10()
			throws PopUpException, NoDataException {

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.TERMINAL_ANALYSIS_WCDMA,
				subMenuTerminalAnalysisCfaMostDrops);

		terminalAnalysisCfaMostDropsWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();

		terminalAnalysisCfaMostDropsWindow
				.switchView(SwitchViewDropDown.MOST_CALL_SETUP_FAILURES);

		terminalAnalysisCfaMostDropsWindow.clickTableCell(0, 4,
				GuiStringConstants.GRID_CELL_LINK);

		final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS;
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));
		final List<String> expectedWindowHeaders;
		if(PropertyReader.getInstance().getEniqRootVersion() >= 13){
			expectedWindowHeaders = callDropsFailedEventAnalysisColumns;
			expectedWindowHeaders.add(GuiStringConstants.RRC_ESTABLISHMENT_CAUSE);
		}else{
			expectedWindowHeaders = callSetupFailedEventAnalysisColumns;
		}
		final List<String> actualWindowHeaders = terminalAnalysisCfaMostDropsWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ expectedWindowHeaders
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		//assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(expectedWindowHeaders));
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, actualWindowHeaders.equals(callSetupFailedEventAnalysisColumns));
	}

	@Test
	public void terminalTabEventAnalysisForTerminal_5_6_11()
			throws PopUpException, InterruptedException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL, false,
				MANUFACTURER_VALUE, MODEL_VALUE, TAC_VALUE);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);

		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = MANUFACTURER_VALUE
					+ GuiStringConstants.COMMA_SPACE + MODEL_VALUE + ","
					+ TAC_VALUE + GuiStringConstants.DASH
					+ GuiStringConstants.WCDMA_CALL_FAILURE
					+ GuiStringConstants.SPACE
					+ GuiStringConstants.EVENT_ANALYSIS;
		} else {
			expectedWindowTitle = MANUFACTURER_VALUE
					+ GuiStringConstants.COMMA_SPACE + MODEL_VALUE + ","
					+ TAC_VALUE + GuiStringConstants.DASH
					+ GuiStringConstants.TERMINAL_EVENT_ANALYSIS + CFA_WCDMA;
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ defaultTerminalEventAnalysisCfaColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders
						.equals(defaultTerminalEventAnalysisCfaColumns));
	}

	@Test
	public void terminalTabEventAnalysisForTerminalFailureDrillDown_5_6_12()
			throws PopUpException, InterruptedException, NoDataException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL, false,
				MANUFACTURER_VALUE, MODEL_VALUE, TAC_VALUE);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);

		terminalEventAnalysisWcdmaCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();

		terminalEventAnalysisWcdmaCfaWindow.clickTableCell(0, 3,
				GuiStringConstants.GRID_CELL_LINK);
		double version = PropertyReader.getInstance().getEniqRootVersion();
		final String expectedWindowTitle;
		if (version >= 13.0) {
			expectedWindowTitle = GuiStringConstants.WCDMA_CALL_FAILURE
				+ GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
		}else{
			expectedWindowTitle = GuiStringConstants.TERMINAL_EVENT_ANALYSIS
					+ CFA_WCDMA;
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		terminalEventAnalysisWcdmaCfaWindow.clickBackwardNavigation();

		terminalEventAnalysisWcdmaCfaWindow.clickTableCell(1, 3,
				GuiStringConstants.GRID_CELL_LINK);
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));
	}

	@Test
	public void terminalTabEventAnalysisForTerminalFailureDrillDownTacEventAnalysis_5_6_13()
			throws PopUpException, InterruptedException, NoDataException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL, false,
				MANUFACTURER_VALUE, MODEL_VALUE, TAC_VALUE);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);
		
		//emosjil
		terminalEventAnalysisWcdmaCfaWindow.setTimeRangeBasedOnSeleniumPropertiesFile(); // set it to 1 week

		terminalEventAnalysisWcdmaCfaWindow.clickTableCell(0, 3, GuiStringConstants.GRID_CELL_LINK);

		// Drill down on final TAC not yet final delivered

	}

	@Test
	public void terminalTabEventAnalysisForTerminalGroup_5_6_14()
			throws PopUpException, InterruptedException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL_GROUP, false,
				TERMINAL_GROUP);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);

		final String expectedWindowTitle;
		double version = PropertyReader.getInstance().getEniqRootVersion();
		if (version >= 13.0) {
			expectedWindowTitle = TERMINAL_GROUP
					+ GuiStringConstants.DASH + GuiStringConstants.TERMINAL_GROUP
					+ GuiStringConstants.DASH + GuiStringConstants.WCDMA_CALL_FAILURE 
					+ GuiStringConstants.SPACE + GuiStringConstants.EVENT_ANALYSIS;
		}else{
			expectedWindowTitle = TERMINAL_GROUP
					+ GuiStringConstants.DASH + "Terminal Group - Event Analysis"
					+ CFA_WCDMA;
		}
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow
				.getTableHeaders();
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ defaultTerminalGroupEventAnalysisColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders
						.equals(defaultTerminalGroupEventAnalysisColumns));
	}

	@Test
	public void terminalTabEventAnalysisForTerminalGroupFailureDrillDown_5_6_15()
			throws PopUpException, InterruptedException, NoDataException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL_GROUP, false,
				TERMINAL_GROUP);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);

		terminalEventAnalysisWcdmaCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();

		terminalEventAnalysisWcdmaCfaWindow.clickTableCell(0, 1,
				GuiStringConstants.GRID_CELL_LINK);

		final String expectedWindowTitle = GuiStringConstants.FAILED_EVENT_ANALYSIS
				+ CFA_WCDMA;
		assertTrue(GuiStringConstants.ERROR_LOADING + expectedWindowTitle,
				selenium.isTextPresent(expectedWindowTitle));

		final List<String> actualWindowHeaders = terminalEventAnalysisWcdmaCfaWindow
				.getTableHeaders();
		
		//emosjil
		final double version = PropertyReader.getInstance().getEniqRootVersion();
		
		if(version >= 13.0){
			actualWindowHeaders.remove("RRC_ESTABLISHMENT_CAUSE");
		}else{
			//nothing
		}
		
		logger.log(Level.INFO, GuiStringConstants.EXPECTED_COLUMN_HEADERS
				+ callDropsFailedEventAnalysisColumns
				+ GuiStringConstants.ACTUAL_UI_COLUMN_HEADERS
				+ actualWindowHeaders);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				actualWindowHeaders.equals(callDropsFailedEventAnalysisColumns));
	}

	// @Test
	public void terminalTabEventAnalysisForTerminalGroupFailureDrillDownTacEventAnalysis_5_6_16_PARTIAL()
			throws PopUpException, InterruptedException, NoDataException {

		terminalTab.enterAnalysisValues(TerminalType.TERMINAL_GROUP, false,
				TERMINAL_GROUP);

		terminalTab.openSubMenusFromStartMenu(
				TerminalTab.StartMenu.EVENT_ANALYSIS, subMenuTerminalWcdmaCfa);

		terminalEventAnalysisWcdmaCfaWindow
				.setTimeRangeBasedOnSeleniumPropertiesFile();

		terminalEventAnalysisWcdmaCfaWindow.clickTableCell(1, 1,
				GuiStringConstants.GRID_CELL_LINK);

		// Drill down on TAC user story not yet implemented
	}

	// Private method
	private void verifyChartWindowTextValues(final String[] chartWindowText) {
		for (int i = 0; i < chartWindowText.length; i++) {
			assertTrue(GuiStringConstants.TEXT_NOT_FOUND + chartWindowText[i],
					selenium.isTextPresent(chartWindowText[i]));
		}
	}

}
