package com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites;

import static com.ericsson.eniq.events.ui.selenium.common.SeleniumUtils.*;

import java.util.*;

import org.springframework.stereotype.Component;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.*;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;

/**
 * Handles creating the groups, so they have data.
 * 
 * @author elukpot
 * @since 27/05/14
 */
@Component
public class GroupCreationUtils {

    /**
     * Written by elukpot.
     *
     * Makes an APN Group with the top failing APNs, except "Not.Applicable.for.Event.Type" and "APN.Not.Provided".
     *
     * @param workspace
     * @param groupManagementWindow
     * @param groupManagementEditWindow
     * @param selenium
     * @return The APN Group Name that was created.
     * @throws InterruptedException
     */
    public static String makeApnGroupFromTopFailingApns(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                        final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium)
            throws InterruptedException, NoDataException {

        return makeApnGroupFromTopFailingApns(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                GroupManagementConstants.topFailingApnsGroupName);

    }

    public static String makeApnGroupFromTopFailingApns(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                        final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                                        final String groupName) throws InterruptedException, NoDataException {

        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.APN, groupManagementWindow)) {

            String apnInRankingRow = "";
            final List<String> excludedApns = new ArrayList<String>(Arrays.asList("Not.Applicable.for.Event.Type", "APN.Not.Provided"));
            final List<String> topFailingApns = new ArrayList<String>();

            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.APN);
            workspace.launch();
            pause(5000);

            if (!selenium.isElementPresent("//div[contains(@id,'NETWORK_APN_RANKING_x-auto')][1]//span[@id='NETWORK_EVENT_ANALYSIS_APN'][text()]")) {
                throw new NoDataException("No Data for CORE(PS) NETWORK -> APN RANKING");
            }

            for (int rankingRow = 1; rankingRow <= 5; rankingRow++) {

                apnInRankingRow = selenium.getText("//div[contains(@id,'NETWORK_APN_RANKING_x-auto')][" + rankingRow
                        + "]//span[@id='NETWORK_EVENT_ANALYSIS_APN'][text()]");

                if (!excludedApns.contains(apnInRankingRow))
                    topFailingApns.add(apnInRankingRow);
            }

            // Close the APN Ranking Grid
            selenium.click("//div[contains(@class, 'x-tool-close')]");

            // Create the APN Group
            GroupManagementOperations.createApnGroup(groupName, topFailingApns, groupManagementWindow, groupManagementEditWindow);
        }

        return groupName;
    }

    /**
     * Written by elukpot.
     *
     * Makes an IMSI group with the top failing APNs.
     *
     * @param workspace
     * @param groupManagementWindow
     * @param groupManagementEditWindow
     * @param selenium
     * @return The name of the group that was created.
     * @throws InterruptedException
     * @throws NoDataException
     */
    public static String makeImsiGroupFromTopFailingImsis(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                          final GroupManagementEditWindow groupManagementEditWindow,
                                                          final CommonWindow subscriberRankingWindow, final EricssonSelenium selenium)
            throws InterruptedException, NoDataException {

        return makeImsiGroupFromTopFailingImsis(workspace, groupManagementWindow, groupManagementEditWindow, subscriberRankingWindow, selenium,
                GroupManagementConstants.topFailingImsisGroupName);

    }

    public static String makeImsiGroupFromTopFailingImsis(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                          final GroupManagementEditWindow groupManagementEditWindow,
                                                          CommonWindow subscriberRankingWindow, final EricssonSelenium selenium,
                                                          final String groupName) throws InterruptedException, NoDataException {

        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.IMSI, groupManagementWindow)) {
            final List<String> topFailingImsis = new ArrayList<String>();

            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
            workspace.launch();
            pause(5000);

            for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
                String imsi = subscriberRankingWindow.getTableData(rowIndex, subscriberRankingWindow.getTableHeaderIndex(GuiStringConstants.IMSI));
                topFailingImsis.add(imsi);
            }

            // Close the IMSI Ranking Grid
            selenium.click("//div[contains(@class, 'x-tool-close')]");

            // Create the IMSI Group
            GroupManagementOperations.createImsiGroup(groupName, topFailingImsis, groupManagementWindow, groupManagementEditWindow);
        }

        return groupName;
    }

    /**
     * Written by elukpot.
     * 
     * Makes a SGSN-MME group from the most common MMEs from the IMSI ranking drills.
     * 
     * @param workspace
     * @param groupManagementWindow
     * @param groupManagementEditWindow
     * @param selenium
     * @return The name of the group that was created.
     * @throws InterruptedException
     */
    public static String makeSgsnGroupFromTopFailingSgsns(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                          final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium)
            throws InterruptedException {

        return makeSgsnGroupFromTopFailingSgsns(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                GroupManagementConstants.topFailingSgsnMmesGroupName);

    }

    public static String makeSgsnGroupFromTopFailingSgsns(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                                          final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                                          final String groupName) throws InterruptedException {

        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.SGSN__MME, groupManagementWindow)) {

            final List<String> topFailingSgsns = new ArrayList<String>();

            // Open the IMSI Ranking grid.
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
            workspace.launch();
            pause(5000);

            // Drill on the Top Failing IMSI.
            selenium.click("//div[contains(@id, 'SUBSCRIBER_RANKING_CORE_x-auto')][1]//span[@id='SUBSCRIBER_EVENT_ANALYSIS_IMSI_TIERED']");

            // Sort the SGSN-MME Column to get a value.
            selenium.waitForElementToBePresent("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//span[text()='SGSN-MME']", "60000");
            for (int i = 0; i < 2; i++) {
                selenium.click("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//span[text()='SGSN-MME']");
                topFailingSgsns
                        .add(selenium
                                .getText("//div[@id='SUBSCRIBER_EVENT_ANALYSIS']//div[contains(@id,'SUBSCRIBER_EVENT_ANALYSIS_x-auto-')][1]//td//span[@id='SUBSCRIBER_EVENT_ANALYSIS_DRILL_ON_EVENTTYPE_SGSN'][text()]"));
            }

            // Close the open ranking grids.
            final String closeWindowButton = "//div[contains(@class, 'x-tool-close')]";
            while (selenium.isElementPresent(closeWindowButton)) {
                selenium.click(closeWindowButton);
            }

            // Create the SGSN-MME Group.
            GroupManagementOperations.createSgsnGroup(groupName, topFailingSgsns, groupManagementWindow, groupManagementEditWindow);
        }
        return groupName;
    }

    /**
     * Written by eboojus.
     * 
     * Makes a Terminal group from the most common Terminals from the Terminal ranking drills.
     * 
     * @param workspace
     * @param groupManagementWindow
     * @param groupManagementEditWindow
     * @param selenium
     * @return The name of the group that was created.
     * @throws InterruptedException
     * @throws NoDataException
     * @throws PopUpException
     */
    public static String makeTacGroupFromTopFailingTacs(WorkspaceRC workspace, GroupManagementWindow groupManagementWindow,
                                                        GroupManagementEditWindow groupManagementEditWindow, EricssonSelenium selenium,
                                                        CommonWindow terminalRankingWindow) throws InterruptedException, NoDataException,
            PopUpException {

        return makeTacGroupFromTopFailingTacs(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                GroupManagementConstants.topFailingTerminalGroupName, terminalRankingWindow);

    }

    public static String makeTacGroupFromTopFailingTacs(WorkspaceRC workspace, GroupManagementWindow groupManagementWindow,
                                                        GroupManagementEditWindow groupManagementEditWindow, EricssonSelenium selenium,
                                                        String groupName, CommonWindow terminalRankingWindow) throws InterruptedException,
            NoDataException, PopUpException {

        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.TERMINAL, groupManagementWindow)) {

            List<String> exclusiveTacsToAvoid = GroupManagementOperations.getASpecificGroupsItems(GuiStringConstants.EXCLUSIVE_TAC,
                    GuiStringConstants.TERMINAL, groupManagementWindow, groupManagementEditWindow);

            List<String> topFailingTacs = new ArrayList<String>();

            // Open the Terminal Ranking grid.
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            pause(1000);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.TERMINAL);
            workspace.launch();
            List<String> tacsFromRanking = terminalRankingWindow.getAllTableDataAtColumn(GuiStringConstants.TAC);

            for (int i = 0; i < tacsFromRanking.size() && topFailingTacs.size() < 3; i++) {
                if (!exclusiveTacsToAvoid.contains(tacsFromRanking.get(i))) {
                    topFailingTacs.add(tacsFromRanking.get(i));
                }
            }

            // Close the open ranking grids.
            final String closeWindowButton = "//div[contains(@class, 'x-tool-close')]";
            while (selenium.isElementPresent(closeWindowButton)) {
                selenium.click(closeWindowButton);
            }

            //Create the Terminal Group.
            groupManagementWindow.launchGroupType(GuiStringConstants.TERMINAL);
            GroupManagementOperations.createTerminalGroupFromList(groupName, topFailingTacs, groupManagementWindow, groupManagementEditWindow);
            groupManagementWindow.closeGroupManagementWindow();
        }
        return groupName;
    }

    public static String make4GAccessAreaGroupFromTopFailingAccessAreas(WorkspaceRC workspace, GroupManagementWindow groupManagementWindow,
                                                                        GroupManagementEditWindow groupManagementEditWindow,
                                                                        EricssonSelenium selenium, String groupName,
                                                                        CommonWindow accessAreaRankingWindow) throws InterruptedException,
            NoDataException, PopUpException {

        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.ACCESS_AREA, groupManagementWindow)) {

            List<String> topFailingAccessAreas = new ArrayList<String>();

            // Open the EnodeB Ranking grid.
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            pause(1000);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.ACCESS_AREA);
            workspace.launch();

            List<String> rankingWindowAccessAreas = accessAreaRankingWindow.getAllTableDataAtColumn(GuiStringConstants.ACCESS_AREA);
            pause(4000);
            List<String> matchingRats = accessAreaRankingWindow.getAllTableDataAtColumn(GuiStringConstants.RAT);
            List<String> matchingVendors = accessAreaRankingWindow.getAllTableDataAtColumn(GuiStringConstants.RAN_VENDOR);

            for (int i = 0; i < rankingWindowAccessAreas.size() && topFailingAccessAreas.size() < 3; i++) {
                if (matchingRats.get(i).equals("LTE") && !matchingVendors.get(i).equals("Unknown")) {
                    topFailingAccessAreas.add(rankingWindowAccessAreas.get(i));
                }
            }

            // Close the open ranking grids.
            final String closeWindowButton = "//div[contains(@class, 'x-tool-close')]";
            while (selenium.isElementPresent(closeWindowButton)) {
                selenium.click(closeWindowButton);
            }

            //Create the Access Area Group.
            groupManagementWindow.launchGroupType(GuiStringConstants.ACCESS_AREA);
            GroupManagementOperations.createAccessAreaGroupFromList(groupName, topFailingAccessAreas, groupManagementWindow,
                    groupManagementEditWindow);
            groupManagementWindow.closeGroupManagementWindow();

            pause(5000);
        }
        return groupName;
    }

    public static String make2GControllerGroup(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                               final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                               final CommonWindow bscRankingWindow) throws InterruptedException, NoDataException {
        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(GroupManagementConstants.topFailing2gControllersGroupName, GuiStringConstants.CONTROLLER,
                groupManagementWindow)) {

            final List<String> topFailingBSCs = new ArrayList<String>();

            String bsc = "";

            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_BSC);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int bscIndex = bscRankingWindow.getTableHeaderIndex(GuiStringConstants.BSC);
            final int totalRows = bscRankingWindow.getTableRowCount();
            ;

            for (int rowNumber = 0; rowNumber < totalRows; rowNumber++) {
                bsc = bscRankingWindow.getTableData(rowNumber, bscIndex);
                if (bsc.contains(":")) {
                    topFailingBSCs.add(bsc);
                }
                if (topFailingBSCs.size() > 5) {
                    break;
                }
            }

            if (topFailingBSCs.isEmpty()) {
                throw new NoDataException("No valid BSCs available");
            }
            bscRankingWindow.closeWindow();

            GroupManagementOperations.createControllerGroup(GroupManagementConstants.topFailing2gControllersGroupName, topFailingBSCs,
                    groupManagementWindow, groupManagementEditWindow);
        }

        return GroupManagementConstants.topFailing2gControllersGroupName;
    }

    public static String make3GControllerGroup(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                               final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                               final CommonWindow rncRankingWindow) throws InterruptedException, NoDataException {
        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(GroupManagementConstants.topFailing3gControllersGroupName, GuiStringConstants.CONTROLLER,
                groupManagementWindow)) {

            final List<String> topFailingRNCs = new ArrayList<String>();

            String rnc = "";

            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_RNC);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int eNodeBIndex = rncRankingWindow.getTableHeaderIndex(GuiStringConstants.RNC);
            final int totalRows = rncRankingWindow.getTableRowCount();

            for (int rowNumber = 0; rowNumber < totalRows; rowNumber++) {
                rnc = rncRankingWindow.getTableData(rowNumber, eNodeBIndex);
                if (rnc.contains(":")) {
                    topFailingRNCs.add(rnc);
                }
                if (topFailingRNCs.size() > 5) {
                    break;
                }
            }
            if (topFailingRNCs.isEmpty()) {
                throw new NoDataException("No valid RNCs available");
            }

            rncRankingWindow.closeWindow();

            GroupManagementOperations.createControllerGroup(GroupManagementConstants.topFailing3gControllersGroupName, topFailingRNCs,
                    groupManagementWindow, groupManagementEditWindow);
        }

        return GroupManagementConstants.topFailing3gControllersGroupName;

    }

    public static String make4GControllerGroup(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                               final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                               final CommonWindow eNodeBRankingWindow) throws InterruptedException, NoDataException {
        return make4GControllerGroup(workspace, groupManagementWindow, groupManagementEditWindow, selenium, eNodeBRankingWindow,
                GroupManagementConstants.topFailing4gControllersGroupName);
    }

    public static String make4GControllerGroup(final WorkspaceRC workspace, final GroupManagementWindow groupManagementWindow,
                                               final GroupManagementEditWindow groupManagementEditWindow, final EricssonSelenium selenium,
                                               final CommonWindow eNodeBRankingWindow, String groupName) throws InterruptedException, NoDataException {
        // Check if the group already exists.
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.CONTROLLER, groupManagementWindow)) {

            final List<String> topFailingENodeBs = new ArrayList<String>();

            String eNodeB = "";

            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.checkAndOpenSideLaunchBar();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);
            workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
            workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.LMAW_ENODEB);
            workspace.launch();
            WorkspaceUtils.pauseUntilWindowLoads(selenium);

            final int eNodeBIndex = eNodeBRankingWindow.getTableHeaderIndex(GuiStringConstants.CONTROLLER);
            final int totalRows = eNodeBRankingWindow.getTableRowCount();

            for (int rowNumber = 0; rowNumber < totalRows; rowNumber++) {
                eNodeB = eNodeBRankingWindow.getTableData(rowNumber, eNodeBIndex);
                if (eNodeB.contains(":")) {
                    topFailingENodeBs.add(eNodeB);
                }
                if (topFailingENodeBs.size() > 5) {
                    break;
                }
            }

            if (topFailingENodeBs.isEmpty()) {
                throw new NoDataException("No valid eNodeBs available");
            }

            eNodeBRankingWindow.closeWindow();

            GroupManagementOperations.createControllerGroup(groupName, topFailingENodeBs, groupManagementWindow, groupManagementEditWindow);
        }

        return groupName;
    }

    public static String makeTrackingAreaGroupFromLiveLoad(WorkspaceRC workspace, GroupManagementWindow groupManagementWindow,
                                                           GroupManagementEditWindow groupManagementEditWindow, EricssonSelenium selenium,
                                                           String groupName) throws InterruptedException, PopUpException {
        if (!GroupManagementOperations.isExistingGroup(groupName, GuiStringConstants.TRACKING_AREA, groupManagementWindow)) {

            workspace.selectDimension(SeleniumConstants.TRACKING_AREA);
            selenium.click(SeleniumConstants.DIMENSION_TEXTFIELD_ARROW);
            List<String> trackingAreasToAdd = new ArrayList<String>();

            pause(3000);
            int noOfTrackingAreas = selenium.getXpathCount(SeleniumConstants.TEXT_FIELD_LIVELOAD_ALL_CONTENT).intValue();
            pause(3000);
            for (int i = 1; i <= noOfTrackingAreas && trackingAreasToAdd.size() < 5; i++) {
                trackingAreasToAdd.add(selenium.getText(SeleniumConstants.TEXT_FIELD_LIVELOAD_ALL_CONTENT + "[" + i + "]"));
            }

            GroupManagementOperations
                    .createTrackingAreaGroupFromList(groupName, trackingAreasToAdd, groupManagementWindow, groupManagementEditWindow);
        }
        return groupName;
    }
}
