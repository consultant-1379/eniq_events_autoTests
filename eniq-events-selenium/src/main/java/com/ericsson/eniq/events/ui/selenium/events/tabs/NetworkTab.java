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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class NetworkTab extends Tab {

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public NetworkTab() {
        super("NETWORK_TAB");
    }

    public void setSearchType(final NetworkType type) {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + typeButtonXPath);
        selenium.click(typeButtonXPath);
        loggerDuplicate.log(Level.INFO, "The Element ID : " + type.getXPath());
        selenium.click(type.getXPath());
    }

    public void openStartMenu(final StartMenu menu) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), DEFAULT_TIMEOUT);
        selenium.click(menu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    public void openSubStartMenu(final StartMenu startMenu, final SubStartMenu subStartMenu) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + startMenu.getXPath());
        selenium.waitForElementToBePresent(startMenu.getXPath(), DEFAULT_TIMEOUT);
        selenium.mouseOver(startMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subStartMenu.getXPath());
        selenium.waitForElementToBePresent(subStartMenu.getXPath(), DEFAULT_TIMEOUT);
        selenium.click(subStartMenu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    public void openSubOfSubStartMenu(final StartMenu startMenu, final SubStartMenu subStartMenu, final SubOfSubStartMenu subOfSubStartMenu)
            throws PopUpException {
        openTab();
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + startMenu.getXPath());
        selenium.waitForElementToBePresent(startMenu.getXPath(), DEFAULT_TIMEOUT);
        selenium.mouseOver(startMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subStartMenu.getXPath());
        selenium.waitForElementToBePresent(subStartMenu.getXPath(), DEFAULT_TIMEOUT);
        selenium.mouseOver(subStartMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subOfSubStartMenu.getXPath());
        selenium.waitForElementToBePresent(subOfSubStartMenu.getXPath(), DEFAULT_TIMEOUT);
        selenium.click(subOfSubStartMenu.getXPath());
        selenium.waitForPageLoadingToComplete();
    }

    public void openSubMenusFromStartMenu(final StartMenu menu, final List<SubStartMenu> SubMenuList) throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        final int SubMenuListSize = SubMenuList.size();
        final SubStartMenu lastElement = SubMenuList.get(SubMenuListSize - 1);
        for (final SubStartMenu subMenu : SubMenuList) {
            if (subMenu.equals(lastElement)) {
                selenium.click(subMenu.getXPath());
                selenium.waitForPageLoadingToComplete();
                break;
            }
            selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
            selenium.mouseOver(subMenu.getXPath());
        }
    }

    //MSS Enhancement

    @Override
    protected void clickStartMenu() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + StartMenu.START.getXPath());
        selenium.click(StartMenu.START.getXPath());
    }

    public enum StartMenu {
        START("NETWORK_TAB_START"), CAUSE_CODE_ANALYSIS("NETWORK_CAUSE_CODE_ANALYSIS_PARENT"), EVENT_ANALYSIS("NETWORK_EVENT_ANALYSIS_PARENT"), ROAMING_ANALYSIS(
                "ROAMING_ANALYSIS"), EVENT_VOLUME("EVENT_VOLUME_PARENT"), RANKINGS("Network_RANKINGS"), DATA_VOLUME("NETWORK_DATAVOL_ANALYSIS"), NETWORK_DATA_VOLUME(
                "NETWORK_DATAVOL_ANALYSIS"), QOS_STATISTICS("NETWORK_QOS_STATISTICS_PARENT"),
        // For MSS
        CAUSE_CODE_ANALYSIS_FOR_MSS("CS_NETWORK_CAUSE_CODE_ANALYSIS"), EVENT_ANALYSIS_FOR_MSS("CS_NETWORK_EVENT_ANALYSIS"), ROAMING_ANALYSIS_FOR_MSS(
                "CS_ROAMING_ANALYSIS"), NETWORK_EVENT_VOLUME_FOR_MSS("CS_EVENT_VOLUME"), RANKINGS_MSS("NETWORK_RANKINGS"), EVENT_VOLUME_FOR_MSS(
                "CS_EVENT_VOLUME"),

        // For LTE        
        NETWORK_QOS_STATISTICS("NETWORK_QOS_STATISTICS_PARENT"), NETWORK_EVENT_VOLUME("NETWORK_EVENT_VOLUME_PARENT"),

        // For GSM
        ROAMING_ANALYSIS_For_GSM("ROAMING_ANALYSIS");

        private final String windowName;

        StartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum SubStartMenu {
        ROAMING_BY_COUNTRY("ROAMING_BY_COUNTRY"), ROAMING_BY_OPERATOR("ROAMING_BY_OPERATOR"), EVENT_RANKING("EVENT_GROUP"), DATA_VOLUME_RANKING(
                "DATA_VOLUME_GROUP"), CAUSE_CODE_ANALYSIS_CORE("NETWORK_CAUSE_CODE_ANALYSIS"), NETWORK_EVENT_ANALYSIS_CORE("NETWORK_EVENT_ANALYSIS"), ROAMING_ANALYSIS_CORE(
                "ROAMING_ANALYSIS_CORE"), ROAMING_BY_OPERATOR_EVENT_VOLUME("ROAMING_BY_OPERATOR_EVENTS"), ROAMING_BY_COUNTRY_EVENT_VOLUME(
                "ROAMING_BY_COUNTRY_EVENTS"), QOS_STATISTICS("QOS_STATISTICS"),
        // For MSS
        ROAMING_BY_COUNTRY_FOR_MSS("CS_ROAMING_BY_COUNTRY"), ROAMING_BY_OPERATOR_FOR_MSS("CS_ROAMING_BY_OPERATOR"), MSC_RANKING_MSS("CS_MSC_RANKINGS"), RNC_RANKING_MSS(
                "CS_RNC_RANKINGS"), BSC_RANKING_MSS("CS_BSC_RANKINGS"), ACCESS_AREA_RANKING_MSS("CS_CELL_RANKINGS"), CAUSE_CODE_RANKING_MSS(
                "CS_NETWORK_CAUSE_CODE_RANKING"),
        // Menus for WCDMA HFA
        NETWORK_RANKING_EVENT_MENU_ITEM_WCDMA("EVENT_GROUP"), NETWORK_RANKING_RNC_MENU_ITEM_WCDMA("RANKING_GROUP"), NETWORK_RANKING_ACCESS_AREA_MENU_ITEM_WCDMA(
                "NETWORK_CELL_RANKING_PARENT"), NETWORK_RANKING_RNC_RAN_MENU_ITEM_WCDMA("NETWORK_RNC_RANKING_RAN"), NETWORK_RANKING_AA_RAN_MENU_ITEM_WCDMA(
                "NETWORK_CELL_RANKING_RAN"), NETWORK_RANKING_HFA_MENU_ITEM_WCDMA("RAN_HFA"), NETWORK_RANKING_AA_WCDMA_MENU_ITEM_WCDMA(
                "NETWORK_CELL_RANKING_RAN_WCDMA"), NETWORK_RANKING_AA_SOURCE_RANKING_MENU_ITEM_WCDMA("NETWORK_SOURCE_CELL_RANKING_RAN_WCDMA_HFA"), NETWORK_RANKING_AA_TARGET_RANKING_MENU_ITEM_WCDMA(
                "NETWORK_TARGET_CELL_RANKING_RAN_WCDMA_HFA"), NETWORK_CAUSE_CODE_RANKING_MENU_ITEM_WCDMA("NETWORK_CAUSE_CODE_RANKING_PARENT"), NETWORK_CAUSE_CODE_RANKING_RAN_MENU_ITEM_WCDMA(
                "NETWORK_CAUSE_CODE_RAN_RANKING"), NETWORK_CAUSE_CODE_RANKING_WCDMA_MENU_ITEM_WCDMA("NETWORK_CAUSE_CODE_RAN_WCDMA_RANKING"), NETWORK_CAUSE_CODE_RANKING_HOT_MENU_ITEM_WCDMA(
                "CC_WCDMA_HFA_HANDOVER_TYPE"), NETWORK_EVENT_ANALYSIS_WCDMA("EVENT_ANALYSIS_NETWORK_WCDMA"), NETWORK_EVENT_ANALYSIS_WCDMA_HFA(
                "NETWORK_EVENT_ANALYSIS_WCDMA_HFA"), NETWORK_CAUSE_CODE_ANALYSIS_RAN_WCDMA("NETWORK_CAUSE_CODE_ANALYSIS_RAN_WCDMA"), WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS(
                "WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS"),

        // LTE CFA
        EVENT_ANALYSIS_RAN("NETWORK_EVENT_ANALYSIS_RAN"), EVENT_ANALYSIS_RAN_LTE("NETWORK_EVENT_ANALYSIS_RAN_LTE"), RAN_LTE_CFA(
                "NETWORK_RAN_LTE_CFA_SUMMARY"), RAN_LTE_HFA("NETWORK_RAN_LTE_HFA"), CAUSE_CODE_ANALYSIS_RAN("NETWORK_CAUSE_CODE_ANALYSIS_RAN"), CAUSE_CODE_ANALYSIS_RAN_LTE(
                "NETWORK_CAUSE_CODE_ANALYSIS_RAN_LTE"), CAUSE_CODE_ANALYSIS_LTE_CFA("NETWORK_CAUSE_CODE_ANALYSIS_LTE_CFA"), CAUSE_CODE_ANALYSIS_LTE_HFA(
                "NETWORK_CAUSE_CODE_ANALYSIS_LTE_HFA"), QOS_STATISTICS_RAN("NETWORK_QOS_STATISTICS_RAN"), QOS_STATISTICS_RAN_LTE(
                "NETWORK_QOS_STATISTICS_PARENT_LTE"), QOS_STATISTICS_LTE_CFA("NETWORK_QOS_STATISTICS_PARENT_LTE_CFA"), QCI_STATISTICS(
                "NETWORK_RAN_LTE_CFA_QOS_QCI_SUMM"), QCI_STATISTICS_HFA("NETWORK_RAN_LTE_HFA_QOS_QCI_SUMM"), QOS_STATISTICS_LTE_HFA(
                "NETWORK_QOS_STATISTICS_PARENT_LTE_HFA"), EVENT_VOLUME_ANALYSIS_RAN("EVENT_VOLUME_RAN"), EVENT_VOLUME_LTE("EVENT_VOLUME_RAN_LTE"), EVENT_VOLUME_ANALYSIS_LTE_CFA(
                "EVENT_VOLUME_LTE_CFA"), EVENT_VOLUME_ANALYSIS_LTE_HFA("EVENT_VOLUME_LTE_HFA"), NETWORK_EVENT_VOL_RAN("NETWORK_EVENT_VOLUME_RAN"), NETWORK_EVENT_VOL_RAN_LTE(
                "NETWORK_EVENT_VOLUME_RAN_LTE"), NETWORK_EVENT_VOL_LTE_CFA("NETWORK_EVENT_VOLUME_LTE_CFA"), NETWORK_EVENT_VOL_LTE_HFA(
                "NETWORK_EVENT_VOLUME_LTE_HFA"),

        // WCDMA CFA
        NETWORK_RAN_RNC_WCDMA_CFA("RAN_CFA"), NETWORK_ACCESS_AREA_WCDMA_CFA("NETWORK_CELL_RANKING_RAN_WCDMA_CFA"), NETWORK_CC_RANKING_CALL_SETUP_FAILURES(
                "CC_CFA_CALL_SETUP"), NETWORK_CC_RANKING_CALL_DROPS("CC_CFA_CALL_DROPS"), NETWORK_EVENT_ANALYSIS_WCDMA_CFA(
                "NETWORK_EVENT_ANALYSIS_WCDMA_CFA"), NETWORK_DC_RANKING_CALL_DROPS("DC_CFA_CALL_DROPS"),

        //GSM   
        NETWORK_EVENT_ANALYSIS_RAN("NETWORK_EVENT_ANALYSIS_RAN"), NETWORK_EVENT_ANALYSIS_RAN_GSM("NETWORK_EVENT_ANALYSIS_RAN_GSM"), NETWORK_RAN_GSM_DETAILED_EVENT_ANALYSIS(
                "NETWORK_GSM_DETAILED_EVENT_ANALYSIS"), GSM_NETWORK_CAUSE_CODE_ANALYSIS("GSM_NETWORK_CAUSE_CODE_ANALYSIS"), NETWORK_CAUSE_CODE_ANALYSIS_RAN(
                "NETWORK_CAUSE_CODE_ANALYSIS_RAN"), NETWORK_CAUSE_CODE_ANALYSIS_RAN_GSM("NETWORK_CAUSE_CODE_ANALYSIS_RAN_GSM"), NETWORK_GSM_EVENT_ANALYSIS_SUMMARY(
                "NETWORK_GSM_EVENT_ANALYSIS_SUMMARY"), ROAMING_ANALYSIS_RAN("ROAMING_ANALYSIS_RAN"), ROAMING_ANALYSIS_RAN_GSM(
                "ROAMING_ANALYSIS_RAN_GSM"), ROAMING_ANALYSIS_RAN_GSM_COUNTRY("ROAMING_ANALYSIS_RAN_GSM_COUNTRY"), ROAMING_ANALYSIS_RAN_GSM_OPERATOR(
                "ROAMING_ANALYSIS_RAN_GSM_OPERATOR"), GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS("GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS");

        private final String windowName;

        SubStartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum SubOfSubStartMenu {
        // Roaming by operator
        ROAMING_BY_OPERATOR_EVENT_VOLUME("ROAMING_BY_OPERATOR_EVENTS"), ROAMING_BY_OPERATOR_DATA_VOLUME("ROAMING_BY_OPERATOR_DATAVOL"),
        // Roaming by country
        ROAMING_BY_COUNTRY_EVENT_VOLUME("ROAMING_BY_COUNTRY_EVENTS"), ROAMING_BY_COUNTRY_DATA_VOLUME("ROAMING_BY_COUNTRY_DATAVOL"),
        // Sub menu for Event Ranking
        EVENT_RANKING_ENODE_B("NETWORK_ENODEB_RANKING"), EVENT_RANKING_RNC("NETWORK_RNC_RANKING"), EVENT_RANKING_BSC("NETWORK_BSC_RANKING"), EVENT_RANKING_ACCESS_AREA(
                "NETWORK_CELL_RANKING"), EVENT_RANKING_CAUSE_CODE("NETWORK_CAUSE_CODE_RANKING"), EVENT_RANKING_APN("NETWORK_APN_RANKING"),
        // Sub menu for Data Volume Ranking
        DATA_VOLUME_RANKING_GGSN("DATAVOLUME_RANKING_GGSN"), DATA_VOLUME_RANKING_QOS_CLASS("DATAVOLUME_RANKING_QOS"), DATA_VOLUME_RANKING_APN(
                "DATAVOLUME_RANKING_APN"), DATA_VOLUME_RANKING_APN_GROUP("DATAVOLUME_RANKING_APN_GROUP"), DATA_VOLUME_RANKING_SGSN_MME(
                "DATAVOLUME_RANKING_SGSN"), DATA_VOLUME_RANKING_SGSN_MME_GROUP("DATAVOLUME_RANKING_SGSN_GROUP"),

        // ForMSS
        // Sub Menu for MSC Ranking
        MSC_BLOCKED_RANKING("CS_MSC_BLOCKED_RANKING"), MSC_DROPPED_RANKING("CS_MSC_DROPPED_RANKING"),

        // Sub Menu for RNC Ranking
        RNC_BLOCKED_RANKING("CS_RNC_BLOCKED_RANKING"), RNC_DROPPED_RANKING("CS_RNC_DROPPED_RANKING"),
        // Sub Menu for BSC Ranking
        BSC_BLOCKED_RANKING("CS_BSC_BLOCKED_RANKING"), BSC_DROPPED_RANKING("CS_BSC_DROPPED_RANKING"),
        // Sub Menu for Access Area
        ACCESS_AREA_BLOCKED_RANKING("CS_NETWORK_CELL_BLOCKED_RANKING"), ACCESS_AREA_DROPPED_RANKING("CS_NETWORK_CELL_DROPPED_RANKING");
        private final String windowName;

        SubOfSubStartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum NetworkType {
        APN("APN"), CONTROLLER("BSC"), ACCESS_AREA("CELL"), SGSN_MME("SGSN"), APN_GROUP("APNGroup"), CONTROLLER_GROUP("BSCGroup"), ACCESS_AREA_GROUP(
                "CELLGroup"), SGSN_MME_GROUP("SGSNGroup"), MSC("MSC"), MSC_GROUP("MSCGroup"), TRACKING_AREA("TRAC"), TRACKING_AREA_GROUP("TRACGroup");

        private final String id;

        NetworkType(final String name) {
            id = name;
        }

        private String getXPath() {
            return "//*[@id='" + id + "']";
        }
    }

    public void openEventAnalysisWindow(final NetworkType type, final boolean useStartMenu, final String value) throws InterruptedException,
            PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            final List<NetworkTab.SubStartMenu> subMenu = new ArrayList<NetworkTab.SubStartMenu>(
                    Arrays.asList(NetworkTab.SubStartMenu.NETWORK_EVENT_ANALYSIS_CORE));
            openSubMenusFromStartMenu(NetworkTab.StartMenu.EVENT_ANALYSIS, subMenu);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openCauseCodeAnalysisWindow(final NetworkType type, final boolean useStartMenu, final String value) throws InterruptedException,
            PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            final List<NetworkTab.SubStartMenu> subMenu = new ArrayList<NetworkTab.SubStartMenu>(
                    Arrays.asList(NetworkTab.SubStartMenu.CAUSE_CODE_ANALYSIS_CORE));
            openSubMenusFromStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS, subMenu);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openQosStatisticsWindow(final NetworkType type, final boolean useStartMenu, final String value) throws InterruptedException,
            PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            final List<NetworkTab.SubStartMenu> subMenu = new ArrayList<NetworkTab.SubStartMenu>(
                    Arrays.asList(NetworkTab.SubStartMenu.QOS_STATISTICS));
            openSubMenusFromStartMenu(NetworkTab.StartMenu.QOS_STATISTICS, subMenu);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventVolumeVoiceWindowForMSS(final NetworkType type, final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        openStartMenu(StartMenu.EVENT_VOLUME_FOR_MSS);

        selenium.waitForPageLoadingToComplete();
    }

    public void openNetworkEventVolumeVoiceWindowForMSS(final NetworkType type, final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        openStartMenu(StartMenu.NETWORK_EVENT_VOLUME_FOR_MSS);

        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowUsingSubStartMenu(final NetworkType type, final List<NetworkTab.SubStartMenu> subMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        openSubMenusFromStartMenu(NetworkTab.StartMenu.EVENT_ANALYSIS, subMenu);

        selenium.waitForPageLoadingToComplete();
    }

    public void openCauseCodeControllerViewWindowUsingSubStartMenu(final NetworkType type, final List<NetworkTab.SubStartMenu> subMenu,
                                                                   final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));
        openSubMenusFromStartMenu(NetworkTab.StartMenu.CAUSE_CODE_ANALYSIS, subMenu);
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForMSS(final NetworkType type, final boolean useStartMenu, final String value) throws InterruptedException,
            PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            openStartMenu(StartMenu.EVENT_ANALYSIS_FOR_MSS);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openRankingEventAnalysisWindowForMSS(final NetworkType type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // esaiaru, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            openSubOfSubStartMenu(StartMenu.RANKINGS_MSS, SubStartMenu.MSC_RANKING_MSS, SubOfSubStartMenu.MSC_BLOCKED_RANKING);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openInternalCauseCodeAnalysisWindowForMSS(final NetworkType type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // esaiaru, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        if (useStartMenu) {
            openStartMenu(StartMenu.CAUSE_CODE_ANALYSIS_FOR_MSS);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForLTE(final NetworkType type, final SubStartMenu failureType, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(3000);
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.EVENT_ANALYSIS_RAN,
                SubStartMenu.EVENT_ANALYSIS_RAN_LTE, failureType));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.EVENT_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventVolumeAnalysisWindowLTE(final NetworkType type, final SubStartMenu failureType, final boolean useStartMenu,
                                                 final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(3000);
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.EVENT_VOLUME_ANALYSIS_RAN,
                SubStartMenu.EVENT_VOLUME_LTE, failureType));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.EVENT_VOLUME, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openNetworkEventVolumeAnalysisWindowLTE(final SubStartMenu failureType) throws InterruptedException, PopUpException {
        openTab();
        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.NETWORK_EVENT_VOL_RAN,
                SubStartMenu.NETWORK_EVENT_VOL_RAN_LTE, failureType));
        openSubMenusFromStartMenu(StartMenu.NETWORK_EVENT_VOLUME, subMenus);
        selenium.waitForPageLoadingToComplete();
    }

    public void openCauseCodeAnalysisWindowForLTE(final NetworkType type, final SubStartMenu failureType, final boolean useStartMenu,
                                                  final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(3000);
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.CAUSE_CODE_ANALYSIS_RAN,
                SubStartMenu.CAUSE_CODE_ANALYSIS_RAN_LTE, failureType));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.CAUSE_CODE_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openQOSStatisticsWindowForLTE(final NetworkType type, final SubStartMenu failureType, final SubStartMenu typeOfStatistics,
                                              final boolean useStartMenu, final String value) throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(3000);
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.QOS_STATISTICS_RAN,
                SubStartMenu.QOS_STATISTICS_RAN_LTE, failureType, typeOfStatistics));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.NETWORK_QOS_STATISTICS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    /*
     * This method helps to navigate the event analysis menu for the wcdmahfa.
     */
    public void openEventAnalysisWindowForWCDMAHFA(final NetworkType type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.EVENT_ANALYSIS_RAN,
                SubStartMenu.NETWORK_EVENT_ANALYSIS_WCDMA, SubStartMenu.NETWORK_EVENT_ANALYSIS_WCDMA_HFA));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.EVENT_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    /*
     * FRED added to navigate controller Tab in WCDMA based on "openEventAnalysisWindowForWCDMAHFA" method JULY 2012
     */
    public void openControllerCauseCodeViewWindowForWCDMAHFA(final NetworkType type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));
        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.CAUSE_CODE_ANALYSIS_RAN,
                SubStartMenu.NETWORK_CAUSE_CODE_ANALYSIS_RAN_WCDMA, SubStartMenu.WCDMA_HFA_NETWORK_CAUSE_CODE_ANALYSIS));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.CAUSE_CODE_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public boolean isGroup(final NetworkType type) {
        return (type == NetworkType.APN_GROUP) || (type == NetworkType.CONTROLLER_GROUP) || (type == NetworkType.ACCESS_AREA_GROUP)
                || (type == NetworkType.SGSN_MME_GROUP) || (type == NetworkType.MSC_GROUP || (type == NetworkType.TRACKING_AREA_GROUP));
    }

    public void tableHeader() {

    }

    //GSM
    public void openRanGSMCFAEventAnalysisWindowForGSM(final NetworkType type, final SubStartMenu failureType, final boolean useStartMenu,
                                                       final String value) throws InterruptedException, PopUpException {

        openTab();
        setSearchType(type);
        Thread.sleep(7000); //DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.NETWORK_EVENT_ANALYSIS_RAN,
                SubStartMenu.NETWORK_EVENT_ANALYSIS_RAN_GSM, failureType));

        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.EVENT_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openRanGSMCFACauseCodeWindowForGSM(final NetworkType type, final SubStartMenu failureType, final boolean useStartMenu,
                                                   final String value) throws InterruptedException, PopUpException {

        openTab();
        setSearchType(type);
        Thread.sleep(7000); //DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));

        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.NETWORK_CAUSE_CODE_ANALYSIS_RAN,
                SubStartMenu.NETWORK_CAUSE_CODE_ANALYSIS_RAN_GSM, failureType));
        if (useStartMenu) {
            openSubMenusFromStartMenu(StartMenu.CAUSE_CODE_ANALYSIS, subMenus);
        } else {
            enterSubmit(isGroup(type));
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openRoamingAnalysisWindowGSM(final SubStartMenu failureType) throws InterruptedException, PopUpException {
        openTab();
        final List<SubStartMenu> subMenus = new ArrayList<SubStartMenu>(Arrays.asList(SubStartMenu.ROAMING_ANALYSIS_RAN,
                SubStartMenu.ROAMING_ANALYSIS_RAN_GSM, failureType));
        openSubMenusFromStartMenu(StartMenu.ROAMING_ANALYSIS_For_GSM, subMenus);
        selenium.waitForPageLoadingToComplete();
    }

}
