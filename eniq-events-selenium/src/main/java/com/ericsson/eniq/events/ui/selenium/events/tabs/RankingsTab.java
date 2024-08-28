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
package com.ericsson.eniq.events.ui.selenium.events.tabs;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RankingsTab extends Tab {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public RankingsTab() {
        super("RANKINGS_TAB");
    }

    public void openStartMenu(final StartMenu menu) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.click(menu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    public void openSubStartMenu(final StartMenu subMenu, final SubStartMenu subOfStartMenu) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subMenu.getXPath());
        selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
        selenium.mouseOver(subMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subOfStartMenu.getXPath());
        selenium.waitForElementToBePresent(subOfStartMenu.getXPath(), "20000");
        selenium.mouseOver(subOfStartMenu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    public void openRankingFromStartMenu(final StartMenu menu, final SubStartMenu subMenu, final SubStartMenu level1, final SubStartMenu level2,
                                         final SubStartMenu level3, final SubStartMenu level4) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subMenu.getXPath());
        selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
        selenium.mouseOver(subMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + level1.getXPath());
        selenium.waitForElementToBePresent(level1.getXPath(), "20000");
        selenium.mouseOver(level1.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + level2.getXPath());
        selenium.waitForElementToBePresent(level2.getXPath(), "20000");
        selenium.mouseOver(level2.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + level3.getXPath());
        selenium.waitForElementToBePresent(level3.getXPath(), "20000");
        selenium.mouseOver(level3.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + level4.getXPath());
        selenium.waitForElementToBePresent(level4.getXPath(), "20000");
        selenium.click(level4.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    /*
     * This method is to open the sub menus from the start menu irrespective of sub menu length.
     */

    public void openSubMenusFromStartMenu(final StartMenu menu, final List<RankingsTab.SubStartMenu> SubMenuList) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        final int SubMenuListSize = SubMenuList.size();
        final RankingsTab.SubStartMenu lastElement = SubMenuList.get(SubMenuListSize - 1);
        for (final RankingsTab.SubStartMenu subMenu : SubMenuList) {
            if (subMenu.equals(lastElement)) {
                loggerDuplicate.log(Level.INFO, "The Element ID : " + subMenu.getXPath());
                selenium.click(subMenu.getXPath());
                selenium.waitForPageLoadingToComplete();
                break;
            }
            selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
            selenium.mouseOver(subMenu.getXPath());
        }
    }

    @Override
    protected void clickStartMenu() {
        selenium.click(StartMenu.START.getXPath());
    }

    public enum StartMenu {
        START("RANKINGS_TAB_START"), EVENT_RANKING("EVENT_GROUP"), DATA_VOLUME_RANKING("DATA_VOLUME_GROUP"),
        // StartMenu for MSS
        RANKINGS_BSC("CS_BSC_RANKINGS"), RANKINGS_RNC("CS_RNC_RANKINGS"), RANKINGS_ACCESS("CS_CELL_RANKINGS"), RANKINGS_CAUSE_CODE(
                "CS_NETWORK_CAUSE_CODE_RANKING"), RANKINGS_TERMINAL("CS_TERMINAL_RANKINGS"), RANKINGS_SUBSCRIBER("SUBSCRIBER_RANKINGS"), RANKINGS_UNANSWERED_CALLS(
                "CS_UNANSWERED_CALLS_RANKINGS"), RANKINGS_MSC("CS_MSC_RANKINGS"), DURATION_CALLS("CS_DURATION_RANKINGS");

        private final String windowName;

        StartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public void openSubofSubStartMenu(final StartMenu subMenu, final SubStartMenu subOfsubMenu) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subMenu.getXPath());
        selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
        selenium.mouseOver(subMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subOfsubMenu.getXPath());
        selenium.waitForElementToBePresent(subOfsubMenu.getXPath(), "20000");
        selenium.click(subOfsubMenu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    /*
     * Fred added 08 august 2012
     */
    public void openControllerCauseCodeViewWindowForWCDMAHFA(final StartMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>();

        if (useStartMenu) {
            //         openSubMenusFromStartMenu(StartMenu.CAUSE_CODE_ANALYSIS, subMenus);
        } else {
            //         enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    ////////////////////////// 

    public enum SubStartMenu {
        // Sub menu for Event Ranking
        EVENT_RANKING_ENODE_B("NETWORK_ENODEB_RANKING"), EVENT_RANKING_RNC("NETWORK_RNC_RANKING"), EVENT_RANKING_BSC("NETWORK_BSC_RANKING"), EVENT_RANKING_ACCESS_AREA(
                "NETWORK_CELL_RANKING"), EVENT_RANKING_CAUSE_CODE("NETWORK_CAUSE_CODE_RANKING_PARENT"), EVENT_RANKING_APN("NETWORK_APN_RANKING"), EVENT_RANKING_TERMINAL(
                "RANKING_TERMINAL_GROUP"), EVENT_RANKING_SUBSCRIBER("SUBSCRIBER_RANKING"), EVENT_RANKING_ENODEB("NETWORK_RANKING_ENODEB"), EVENT_RANKING_ECELL(
                "NETWORK_CELL_RANKING_PARENT"), EVENT_RANKING_TRACKING_AREA("NETWORK_TRACKING_AREA_RANKING_PARENT"), BSC_PARENT("RANKING_GROUP_BSC"), RNC_PARENT(
                "RANKING_GROUP"), EVENT_RANKING_TARGET_CELL("NETWORK_TARGET_CELL_RANKING_RAN_WCDMA_HFA"),

        // Sub menu for Data Volume Ranking
        DATA_VOLUME_RANKING_GGSN("DATAVOLUME_RANKING_GGSN"), DATA_VOLUME_RANKING_QOS_CLASS("DATAVOLUME_RANKING_QOS"), DATA_VOLUME_RANKING_APN(
                "DATAVOLUME_RANKING_APN"), DATA_VOLUME_RANKING_APN_GROUP("DATAVOLUME_RANKING_APN_GROUP"), DATA_VOLUME_RANKING_SGSN_MME(
                "DATAVOLUME_RANKING_SGSN"), DATA_VOLUME_RANKING_SGSN_MME_GROUP("DATAVOLUME_RANKING_SGSN_GROUP"), DATA_VOLUME_RANKING_TERMINAL(
                "DATAVOLUME_RANKING_TAC"), DATA_VOLUME_RANKING_TERMINAL_GROUP("DATAVOLUME_RANKING_TERMINAL_GROUP"), DATA_VOLUME_RANKING_SUBSCRIBER(
                "DATAVOLUME_RANKING_IMSI"), DATA_VOLUME_RANKING_SUBSCRIBER_GROUP("DATAVOLUME_RANKING_IMSI_GROUP"),
        // Sub menu for MSS

        // Sub menu for Terminal
        RANKING_TERMINAL_BLOCKED_VOICE("CS_TERMINAL_BLOCKED_RANKING"), RANKING_TERMINAL_DROPPED_VOICE("CS_TERMINAL_DROPPED_RANKING"),
        // Sub menu for BSC
        RANKING_BSC_BLOCKED_VOICE("CS_BSC_BLOCKED_RANKING"), RANKING_BSC_DROPPED_VOICE("CS_BSC_DROPPED_RANKING"),
        // Sub menu for RNC
        RANKING_RNC_BLOCKED_VOICE("CS_RNC_BLOCKED_RANKING"), RANKING_RNC_DROPPED_VOICE("CS_RNC_DROPPED_RANKING"),
        // Sub menu for Access Area
        RANKING_ACCESS_AREA_BLOCKED_VOICE("CS_NETWORK_CELL_BLOCKED_RANKING"), RANKING_ACCESS_AREA_DROPPED_VOICE("CS_NETWORK_CELL_DROPPED_RANKING"),
        // Sub menu for Subscriber
        RANKINGS_SUBSCRIBER_DROPPED_VOICE("CS_SUBSCRIBER_DROPPED_RANKING"), RANKINGS_SUBSCRIBER_BLOCKED_VOICE("CS_SUBSCRIBER_BLOCKED_RANKING"),
        // Sub menu for Unanswered calls
        RANKING_MS_ORIGINATING_UNANSWERED_CALLS_VOICE("CS_MS_ORIGINATING_UNANSWERED_CALLS_RANKING"), RANKING_MS_TERMINATING_UNANSWERED_CALLS_VOICE(
                "CS_MS_TERMINATING_UNANSWERED_CALLS_RANKING"),
        // Sub menu for MSC
        RANKING_MSC_BLOCKED_VOICE("CS_MSC_BLOCKED_RANKING"), RANKING_MSC_DROPPED_VOICE("CS_MSC_DROPPED_RANKING"),
        // Sub menu for Duration Calls
        LONG_DURATION_CALLS_VOICE("CS_LONG_DURATION_CALLS_RANKING"), SHORT_DURATION_CALLS_VOICE("CS_SHORT_DURATION_CALLS_RANKING"),

        // Sub Menu for LTE CFA
        RANKINGS_SUBSCRIBER_RAN("SUBSCRIBER_RANKING_RAN"), RANKINGS_SUBSCRIBER_LTE("SUBSCRIBER_RANKING_LTE"), RANKINGS_SUBSCRIBER_CFA(
                "SUBSCRIBER_RANKING_CFA"), RANKINGS_SUBSCRIBER_LTE_CALL_SETUP_FAILURE("SUBSCRIBER_RANKING_LTE_CALL_SETUP_FAILURE"), RANKINGS_SUBSCRIBER_LTE_CALL_DROP(
                "SUBSCRIBER_RANKING_LTE_CALL_DROP"), RANKINGS_SUBSCRIBER_LTE_RECURRING_FAILURES("SUBSCRIBER_RANKING_LTE_RECUR_FAILURES"), RANKINGS_ENODEB_RAN(
                "NETWORK_RANKING_ENODEB_RAN"), RANKINGS_ENODEB_LTE("NETWORK_RANKING_ENODEB_RAN_LTE"), RANKINGS_ENODEB_CFA(
                "NETWORK_RANKING_ENODEB_RAN_LTE_CFA"), RANKINGS_ENODEB_LTE_CALL_SETUP_FAILURE("NETWORK_ENODEB_RANKING_RAN_LTE_CFA_CALL_SETUP"), RANKINGS_ENODEB_LTE_CALL_DROP(
                "NETWORK_ENODEB_RANKING_RAN_LTE_CFA_CALL_DROP"), RANKINGS_ECELL_RAN("NETWORK_CELL_RANKING_RAN"), RANKINGS_ECELL_LTE(
                "NETWORK_CELL_RANKING_RAN_LTE"), RANKINGS_ECELL_CFA("NETWORK_CELL_RANKING_RAN_LTE_CFA"), RANKINGS_ECELL_LTE_CALL_SETUP_FAILURE(
                "NETWORK_CELL_RANKING_RAN_LTE_CFA_CALL_SETUP"), RANKINGS_ECELL_LTE_CALL_DROP("NETWORK_CELL_RANKING_RAN_LTE_CFA_CALL_DROP"), RANKING_TRACKING_AREA_RAN(
                "NETWORK_TRACKING_AREA_RAN_RANKING"), RANKING_TRACKING_AREA_LTE("NETWORK_TRACKING_AREA_RAN_LTE_RANKING"), RANKING_TRACKING_AREA_CFA(
                "NETWORK_TRACKING_AREA_RAN_LTE_CFA_RANKING"), RANKING_TRACKING_AREA_LTE_CALL_SETUP_FAILURE(
                "NETWORK_TRACKING_AREA_RAN_LTE_RANKING_CALL_SETUP"), RANKING_TRACKING_AREA_LTE_CALL_DROP_FAILURE(
                "NETWORK_TRACKING_AREA_RAN_LTE_RANKING_CALL_DROP"),

        RANKING_CAUSE_CODE_RAN("NETWORK_CAUSE_CODE_RAN_RANKING"), RANKING_CAUSE_CODE_LTE("NETWORK_CAUSE_CODE_RAN_LTE_RANKING"), RANKING_CAUSE_CODE_CFA(
                "NETWORK_CAUSE_CODE_RAN_LTE_CFA_RANKING"), RANKING_CAUSE_CODE_LTE_CALL_SETUP_FAILURE("LTE_CFA_CC_CALL_SETUP_RANKING"), RANKING_CAUSE_CODE_LTE_CALL_DROP_FAILURE(
                "LTE_CFA_CC_CALL_DROPS_RANKING"),

        RANKING_TERMINAL_LTE("TERMINAL_RANKING_RAN_LTE"), RANKING_TERMINAL_CFA("TERMINAL_RANKING_RAN_LTE_CFA"), RANKING_TERMINAL_LTE_CALL_SETUP_FAILURE(
                "TERMINAL_RANKING_RAN_LTE_CFA_CALL_SETUP"), RANKING_TERMINAL_LTE_CALL_DROP_FAILURE("TERMINAL_RANKING_RAN_LTE_CFA_CALL_DROP"), RANKING_TERMINAL_HFA(
                "TERMINAL_RANKING_RAN_LTE_HFA"), RANKING_TERMINAL_LTE_PREP_FAILURE("TERMINAL_RANKING_RAN_LTE_HFA_TAC_PREP"), RANKING_TERMINAL_LTE_EXEC_FAILURE(
                "TERMINAL_RANKING_RAN_LTE_HFA_TAC_EXEC"),

        // Sub Menu for LTE HFA
        RANKINGS_SUBSCRIBER_HFA("SUBSCRIBER_RANKING_HFA"), RANKINGS_SUBSCRIBER_LTE_HANDOVER_PREP_FAILURE(
                "SUBSCRIBER_RANKING_LTE_HANDOVER_PREP_FAILURE"), RANKINGS_SUBSCRIBER_LTE_HANDOVER_EXEC_FAILURE(
                "SUBSCRIBER_RANKING_LTE_HANDOVER_EXEC_FAILURE"), RANKINGS_ENODEB_HFA("NETWORK_RANKING_ENODEB_RAN_LTE_HFA"), RANKINGS_ENODEB_LTE_HANDOVER_PREP_FAILURE(
                "NETWORK_ENODEB_RANKING_RAN_LTE_HFA_PREP_FAILURE"), RANKINGS_ENODEB_LTE_HANDOVER_EXEC_FAILURE(
                "NETWORK_ENODEB_RANKING_RAN_LTE_HFA_EXEC_FAILURE"), RANKINGS_SOURCE_CELL_HFA("NETWORK_SOURCE_CELL_RANKING_RAN_LTE_HFA"), RANKINGS_TARGET_CELL_HFA(
                "NETWORK_TARGET_CELL_RANKING_RAN_LTE_HFA"), RANKINGS_SOURCE_ECELL_LTE_HANDOVER_PREP_FAILURE(
                "NETWORK_RAN_LTE_HFA_SOURCE_CELL_RANKING_PREP"), RANKINGS_SOURCE_ECELL_LTE_HANDOVER_EXEC_FAILURE(
                "NETWORK_RAN_LTE_HFA_SOURCE_CELL_RANKING_EXEC"), RANKINGS_TARGET_ECELL_LTE_HANDOVER_PREP_FAILURE(
                "NETWORK_RAN_LTE_HFA_TARGET_CELL_RANKING_PREP"), RANKINGS_TARGET_ECELL_LTE_HANDOVER_EXEC_FAILURE(
                "NETWORK_RAN_LTE_HFA_TARGET_CELL_RANKING_EXEC"), RANKING_CAUSE_CODE_HFA("NETWORK_CAUSE_CODE_RAN_LTE_HFA_RANKING"), RANKING_CAUSE_CODE_LTE_HFA_PREP_FAILURE(
                "NETWORK_RAN_LTE_HFA_CAUSE_CODE_RANKING_PREP"), RANKING_CAUSE_CODE_LTE_HFA_EXEC_FAILURE("NETWORK_RAN_LTE_HFA_CAUSE_CODE_RANKING_EXEC"),

        // Sub Menu for WCDMA HFA
        RANKINGS_ACCESS_AREA_WCDMAHFA_RAN("NETWORK_CELL_RANKING_RAN"), RANKINGS_RNC_WCDMAHFA_RNC("RANKING_GROUP"), RANKINGS_RNC_WCDMAHFA_RAN(
                "NETWORK_RNC_RANKING_RAN"), RANKINGS_RNC_WCDMAHFA("RAN_HFA"), RANKINGS_ACCESS_AREA_WCDMAHFA("NETWORK_CELL_RANKING_PARENT"), RANKING_ACCESS_AREA_WCDMA_HFA(
                "NETWORK_SOURCE_CELL_RANKING_RAN_WCDMA_HFA"), RANKING_CAUSE_CODE_WCDMA_PARENT("NETWORK_CAUSE_CODE_RANKING_PARENT"), RANKING_CAUSE_CODE_WCDMA_RAN(
                "NETWORK_CAUSE_CODE_RAN_RANKING"), RANKING_CAUSE_CODE_WCDMA("NETWORK_CAUSE_CODE_RAN_WCDMA_RANKING"), RANKING_CAUSE_CODE_WCDMA_HANDOVER_TYPE(
                "CC_WCDMA_HFA_HANDOVER_TYPE"), RANKING_TERMINAL_WCDMA_PARENT("RANKING_TERMINAL_GROUP"), RANKING_TERMINAL_RAN(
                "NETWORK_TAC_RANKING_RAN"), RANKING_TERMINAL_WCDMA("TERMINAL_RANKING_RAN_WCDMA"), RANKING_TERMINAL_WCDMA_HFA("RAN_WCDMA_TAC_HFA"), RANKING_SUBSCRIBER_WCDMA_PARENT(
                "SUBSCRIBER_RANKING"), RANKING_SUBSCRIBER_WCDMA_RAN("SUBSCRIBER_RANKING_RAN"), RANKING_SUBSCRIBER_WCDMA_HFA("SUBSCRIBER_RANKING_HFA"), RANKING_SUBSCRIBER_WCDMA_HFA_SOFT_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_SOHO"), RANKING_SUBSCRIBER_WCDMA_HFA_IRAT_HANDOVER("SUBSCRIBER_WCDMA_RAN_HANDOVER_IRAT"), RANKING_SUBSCRIBER_WCDMA_HFA_IFHO_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_IFHO"), RANKING_SUBSCRIBER_WCDMA_HFA_HSDSCH_HANDOVER("SUBSCRIBER_WCDMA_RAN_HANDOVER_HSDSCH"),

        // Sub Menu for WCDMA CFA
        RANKINGS_RNC_WCDMACFA("RAN_CFA"), RANKING_ACCESS_AREA_WCDMA_CFA("NETWORK_CELL_RANKING_RAN_WCDMA_CFA"), RANKINGS_SUBSCRIBER_CALL_SETUP_FAILURES(
                "SUBSCRIBER_RANKING_CALL_SETUP_FAILURES"), RANKING_SUBSCRIBER_CALL_DROPS("SUBSCRIBER_RANKING_CALL_DROPS"), RANKING_SUBSCRIBER_MULTIPLE_RECURRING_FAILURES(
                "SUBSCRIBER_RANKING_MULTIPLE_RECURRING_FAILURES"), RANKING_CC_CFA_CALL_DROPS("CC_CFA_CALL_DROPS"), RANKING_CC_CFA_CALL_SETUP_FAILURES(
                "CC_CFA_CALL_SETUP"), RANKING_TERMINAL_WCDMA_CFA("RAN_WCDMA_TAC_CFA"), RANKING_DC_CFA_CALL_DROPS("DC_CFA_CALL_DROPS"),

        // Common Sub Menu for WCDMA HFA and WCDMA
        RANKINGS_RNC("RANKING_GROUP"), RANKINGS_RNC_RAN("NETWORK_RNC_RANKING_RAN"), RANKING_ACCESS_AREA_WCDMA("NETWORK_CELL_RANKING_RAN_WCDMA"), RANKINGS_ACCESS_AREA_RAN(
                "NETWORK_CELL_RANKING_RAN"), RANKINGS_ACCESS_AREA_RAN_WCDMA_CFA("NETWORK_CELL_RANKING_RAN_WCDMA_CFA"), RANKING_SUBSCRIBER_WCDMA(
                "SUBSCRIBER_RANKING_WCDMA"),

        // Submenus for GSM CFA
        EVENT_RANKING_ACCESS_AREA_PARENT("NETWORK_CELL_RANKING_PARENT"), RANKING_ACCESS_AREA_RAN_GSM("NETWORK_CELL_RANKING_RAN_GSM"), RANKING_ACCESS_AREA_RAN_GSM_CFA(
                "GSM_CFA_CELL_RANKING"), RANKING_BSC("RANKING_GROUP_BSC"), EVENT_RANKING_BSC_RAN("NETWORK_BSC_RANKING_RAN"), EVENT_RANKING_BSC_RAN_CFA(
                "GSM_CFA_BSC_RANKING"), EVENT_RANKING_CAUSE_CODE_RAN_GSM("NETWORK_CAUSE_CODE_RAN_GSM_RANKING"), EVENT_RANKING_CAUSE_CODE_RAN_GSM_CALL_DROP(
                "GSM_CFA_CC_CALL_DROPS_RANKING"), EVENT_RANKING_TERMINAL_RAN_GSM("TERMINAL_RANKING_RAN_GSM"), EVENT_RANKING_TERMINAL_RAN_GSM_CFA(
                "RAN_GSM_TAC_CFA"), EVENT_RANKING_SUBSCRIBER_RAN_GSM("SUBSCRIBER_RANKING_GSM"), EVENT_RANKING_SUBSCRIBER_RAN_GSM_CFA_CALL_DROP(
                "SUBSCRIBER_RANKING_GSM_CALL_DROPS"),

        // Submenus for GSM Data Volume 		
        DATA_VOLUME_GROUP("DATA_VOLUME_GROUP"), DATA_VOLUME_RAN_GROUP("DATA_VOLUME_RAN_GROUP"), GSM_DATAVOLUME_RANKING_IMSI(
                "GSM_DATAVOLUME_RANKING_IMSI"), GSM_DATAVOLUME_RANKING_CONTROLLER("GSM_DATAVOLUME_RANKING_CONTROLLER"), ACCESSAREA_DATAVOLUME_RANKING(
                "ACCESSAREA_DATAVOLUME_RANKING");

        private final String windowName;

        SubStartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

}
