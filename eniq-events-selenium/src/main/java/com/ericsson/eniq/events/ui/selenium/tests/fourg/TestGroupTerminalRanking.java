package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.TerminalTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author earimon
 * @since 2011
 * 
 */

public class TestGroupTerminalRanking extends BaseFourGTest {

  static final String RANK = "Rank";

  static final String MANUFACTURER = "Manufacturer";

  static final String MODEL = "Model";

  static final String TAC = "TAC";

  static final String FAILURES = "Failures";

  static final String SUCCESSES = "Successes";

  final TimeRange timeReference = TimeRange.ONE_WEEK;

  @Autowired
  private TerminalTab terminalTab;

  @Autowired
  @Qualifier("termRankings")
  private CommonWindow terminalRankingCoreWindow;

  /*
   * Test Cases: 4.5.13, 4.5.17, 4.5.21, 4.5.25, 4.5.29, 4.5.33, 4.5.37, 4.5.41
   * and 4.5.45
   */

  @Test
  public void terminalRankingForCore() throws PopUpException, NoDataException {
    terminalTab.openTab();
    terminalTab.openSubOfSubStartMenu(TerminalTab.StartMenu.TERMINAL_RANKINGS,
        TerminalTab.SubStartMenu.TERMINAL_RANKING_EVENT_MENU_ITEM_WCDMA,
        TerminalTab.SubOfSubStartMenu.EVENT_RANKING_TERMINAL);

    final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList(RANK, MANUFACTURER, MODEL, TAC, FAILURES,
        SUCCESSES));

    checkStringListContainsArray(terminalRankingCoreWindow.getTableHeaders(),
        expectedHeaders.toArray(new String[expectedHeaders.size()]));

    terminalRankingCoreWindow.setTimeRange(timeReference);

    final int testRow = 1;

    final List<String> allMANUFACTURER = terminalRankingCoreWindow.getAllTableDataAtColumn(MANUFACTURER);

    System.out.println("No Of manufacturers ---> " + allMANUFACTURER.size());
    System.out.println("All manufacturers ---> " + allMANUFACTURER);

    final int row = terminalRankingCoreWindow.findFirstTableRowWhereMatchingAnyValue(MANUFACTURER,
        allMANUFACTURER.get(testRow));
    final Map<String, String> result = terminalRankingCoreWindow.getAllDataAtTableRow(row);

    final String timeDisplayBefore = terminalRankingCoreWindow.getTimeRange();
    final Integer totalFailures = Integer.parseInt(result.get(FAILURES));

    terminalRankingCoreWindow.clickTableCell(row, FAILURES);

    /*
     * To Verify the temporal reference is maintained after drill down.
     */
    final String timeDisplayAfter = terminalRankingCoreWindow.getTimeRange();

    final Integer failuresAfterDrillDown = terminalRankingCoreWindow.getTableRowCount();

    final List<Map<String, String>> failureResult = terminalRankingCoreWindow.getAllTableData();

    // TODO Check the map with reserve data map.

    /*
     * for (int count = 1; count <= terminalRankingCoreWindow.getPageCount();
     * count++) { failuresAfterDrillDown += terminalRankingCoreWindow
     * .getTableRowCount(); terminalRankingCoreWindow.clickNextPage(); }
     */

    /*
     * assertEquals((timeDisplayAfter.equalsIgnoreCase(timeDisplayBefore)),
     * (failuresAfterDrillDown == totalFailures));
     */

  }

}
