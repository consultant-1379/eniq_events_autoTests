/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.tabs;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SubscriberTab extends Tab {

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public SubscriberTab() {
        super("SUBSCRIBER_TAB");
        dropGoupButtonXPath = tabXPath + "//table[@id='selenium_tag_typeButton']//button";
        loggerDuplicate.log(Level.INFO, "The Element ID : " + dropGoupButtonXPath);
        // searchFieldInputXPath = "//div[@id='" + tabName + "']//input";
        searchFieldInputXPath = tabXPath +
                "//div[@class=' x-form-field-wrap  x-spinner-field x-component ']//input";
        loggerDuplicate.log(Level.INFO, "The Element ID : " + searchFieldInputXPath);
    }

    public void openStartMenu(final StartMenu menu) {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.click(menu.getXPath());
    }

    public void openSubStartMenu(final StartMenu startMenu, final SubStartMenu subStartMenu) {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + startMenu.getXPath());
        selenium.waitForElementToBePresent(startMenu.getXPath(), "20000");
        selenium.mouseOver(startMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subStartMenu.getXPath());
        selenium.waitForElementToBePresent(subStartMenu.getXPath(), "20000");
        selenium.click(subStartMenu.getXPath());
    }

    public void openSubOfSubStartMenu(final StartMenu startMenu, final SubStartMenu subStartMenu,
            final SubOfSubStartMenu subOfSubStartMenu) {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + startMenu.getXPath());
        selenium.waitForElementToBePresent(startMenu.getXPath(), "20000");
        selenium.mouseOver(startMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subStartMenu.getXPath());
        selenium.waitForElementToBePresent(subStartMenu.getXPath(), "20000");
        selenium.mouseOver(subStartMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + subOfSubStartMenu.getXPath());
        selenium.waitForElementToBePresent(subOfSubStartMenu.getXPath(), "20000");
        selenium.click(subOfSubStartMenu.getXPath());
    }

    public void openLaunchMenu(final LaunchMenu menu, final SubLaunchMenuLevelOne levelOneMenu,
            final SubLaunchMenuLevelTwo levelTwoMenu, final SubLaunchMenuLevelThree levelThreeMenu) {
        clickLaunchMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + levelOneMenu.getXPath());
        selenium.waitForElementToBePresent(levelOneMenu.getXPath(), "20000");
        selenium.mouseOver(levelOneMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + levelTwoMenu.getXPath());
        selenium.waitForElementToBePresent(levelTwoMenu.getXPath(), "20000");
        selenium.mouseOver(levelTwoMenu.getXPath());
        loggerDuplicate.log(Level.INFO, "The Element ID : " + levelThreeMenu.getXPath());
        selenium.waitForElementToBePresent(levelThreeMenu.getXPath(), "20000");
        selenium.click(levelThreeMenu.getXPath());
    }

    /*
     * This method is to open the sub menus from the start menu irrespective of
     * sub menu length.
     */

    public void openSubMenusFromStartMenu(final StartMenu menu, final List<SubscriberTab.SubStartMenu> SubMenuList)
            throws PopUpException {
        clickStartMenu();
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        final int SubMenuListSize = SubMenuList.size();
        final SubscriberTab.SubStartMenu lastElement = SubMenuList.get(SubMenuListSize - 1);
        for (final SubscriberTab.SubStartMenu subMenu : SubMenuList) {
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

    // The last element in subMenuList should be the sub menu item that is disabled
    public boolean checkSubMenuItemIsDisabled(final StartMenu menu, final List<SubscriberTab.SubStartMenu> subMenuList)
            throws PopUpException, InterruptedException {
        boolean isSubMenuItemDisabled = false;
        clickStartMenu();
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        final int subMenuListSize = subMenuList.size();
        final SubscriberTab.SubStartMenu lastElement = subMenuList.get(subMenuListSize - 1);
        for (final SubscriberTab.SubStartMenu subMenu : subMenuList) {
            if (subMenu.equals(lastElement)) {
                System.out.println("In if statement (outer)");
                if (selenium.isTextPresent(subMenu.getXPath()
                        + " x-menu-item x-menu-item-arrow x-component x-item-disabled")) {
                    //                if (selenium.isElementPresent(subMenu.getXPath()
                    //                        + " x-menu-item x-menu-item-arrow x-component x-item-disabled")) {
                    System.out.println("In if statement (inner)");
                    logger.log(Level.SEVERE, "Launch menu sub item NOT disabled: " + subMenu.getXPath());
                    return isSubMenuItemDisabled;
                } else {
                    System.out.println("In else statement");
                    isSubMenuItemDisabled = true;
                    return isSubMenuItemDisabled;
                }
            }

            selenium.waitForElementToBePresent(subMenu.getXPath(), "20000");
            selenium.mouseOver(subMenu.getXPath());
        }
        return isSubMenuItemDisabled;
    }

    public void setSearchType(final ImsiMenu menu) {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + dropGoupButtonXPath);
        selenium.click(dropGoupButtonXPath);
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.click(menu.getXPath());
    }

    public enum StartMenu {
        START("SUBSCRIBER_TAB_START"), SUBSCRIBER_OVERVIEW("SUB_BI_FAILED_EVENTS_PARENT"), EVENT_ANALYSIS(
                "SUBSCRIBER_EVENT_ANALYSIS_PARENT"), SUBSCRIBER_RANKINGS("SUBSCRIBER_RANKING_GROUP"), EVENT_ANALYSIS_FOR_MSS(
                "CS_SUBSCRIBER_EVENT_ANALYSIS"), SUBSCRIBER_OVERVIEW_FOR_MSS("CS_SUB_BI_FAILED_EVENTS");

        private final String xPath;

        StartMenu(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    public enum SubStartMenu {
        EVENT_RANKING("EVENT_GROUP"), DATA_VOLUME_RANKING("DATA_VOLUME_GROUP"), SUBSCRIBER_EVENT_ANALYSIS_CORE(
                "SUBSCRIBER_EVENT_ANALYSIS"), SUBSCRIBER_OVERVIEW("SUB_BI_FAILED_EVENTS"),

        // For WCDMA Menu Items
        SUBSCRIBER_RANKING_RAN_MENU_ITEM_WCDMA("SUBSCRIBER_RANKING_RAN"), SUBSCRIBER_RANKING_WCDMA_MENU_ITEM_WCDMA(
                "SUBSCRIBER_RANKING_WCDMA"), SUBSCRIBER_RANKING_HFA_MENU_ITEM_WCDMA("SUBSCRIBER_RANKING_HFA"), SUBSCRIBER_RANKING_CFA_MENU_ITEM(
                "SUBSCRIBER_RANKING_CFA"), SUBSCRIBER_RANKING_WCDMA_HFA_SOFT_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_SOHO"), SUBSCRIBER_RANKING_WCDMA_HFA_IRAT_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_IRAT"), SUBSCRIBER_RANKING_WCDMA_HFA_IFHO_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_IFHO"), SUBSCRIBER_RANKING_WCDMA_HFA_HSDSCH_HANDOVER(
                "SUBSCRIBER_WCDMA_RAN_HANDOVER_HSDSCH"), SUBSCRIBER_RANKING_WCDMA_CFA_CALL_SETUP_FAILURES(
                "SUBSCRIBER_RANKING_CALL_SETUP_FAILURES"), SUBSCRIBER_RANKING_WCDMA_CFA_CALL_DROPS(
                "SUBSCRIBER_RANKING_CALL_DROPS"), SUBSCRIBER_RANKING_WCDMA_CFA_RECURRING_FAILURES(
                "SUBSCRIBER_RANKING_MULTIPLE_RECURRING_FAILURES"),

        // Subscriber Overview Sub Menus
        SUB_OVERVIEW_RAN("SUB_BI_FAILED_EVENTS_RAN"), SUB_OVERVIEW_RAN_WCDMA("SUB_BI_FAILED_EVENTS_WCDMA"), SUB_OVERVIEW_RAN_WCDMA_CFA(
                "SUB_BI_FAILED_EVENTS_CFA"),

        // Event Analysis Sub Menus
        SUB_EVENT_ANALYSIS_RAN("SUBSCRIBER_EVENT_ANALYSIS_RAN"), SUB_EVENT_ANALYSIS_WCDMA(
                "SUBSCRIBER_EVENT_ANALYSIS_RAN_WCDMA"), SUB_EVENT_ANALYSIS_WCDMA_CFA(
                "SUBSCRIBER_CALL_FAILURE_EVENT_ANALYSIS"),

        //GSMSubMenus
        RANKING("SUBSCRIBER_RANKING_GROUP"), EVENT_GROUP("EVENT_GROUP"), SUBSCRIBER_RANKING_RAN(
                "SUBSCRIBER_RANKING_RAN"), SUBSCRIBER_RANKING_GSM("SUBSCRIBER_RANKING_GSM"), SUBSCRIBER_RANKING_CFA(
                "SUBSCRIBER_RANKING_CFA"), SUBSCRIBER_RANKING_GSM_CALL_DROPS("SUBSCRIBER_RANKING_GSM_CALL_DROPS");

        private final String xPath;

        SubStartMenu(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    public enum SubOfSubStartMenu {
        EVENT_RANKING_SUBSCRIBER("SUBSCRIBER_RANKING"), DATA_VOLUME_RANKING_SUBSCRIBER("DATAVOLUME_RANKING_IMSI"), DATA_VOLUME_RANKING_SUBSCRIBER_GROUP(
                "DATAVOLUME_RANKING_IMSI_GROUP"), SUBSCRIBER_EVENT_RANKING_CORE("SUBSCRIBER_RANKING_CORE");

        private final String xPath;

        SubOfSubStartMenu(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    // Added to support LTE CFA feature for 12.1

    public enum LaunchMenu {
        LAUNCH("SUBSCRIBER_TAB_START"), EVENT_ANALYSIS("SUBSCRIBER_EVENT_ANALYSIS_PARENT");

        private final String xPath;

        LaunchMenu(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    public enum SubLaunchMenuLevelOne {
        EVENT_ANALYSIS_RAN("SUBSCRIBER_EVENT_ANALYSIS_RAN");

        private final String xPath;

        SubLaunchMenuLevelOne(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    public enum SubLaunchMenuLevelTwo {
        EVENT_ANALYSIS_RAN_LTE("SUBSCRIBER_EVENT_ANALYSIS_RAN_LTE"), EVENT_ANALYSIS_RAN_WCDMAHFA(
                "SUBSCRIBER_EVENT_ANALYSIS_RAN_WCDMA"), EVENT_ANALYSIS_RAN_GSM("SUBSCRIBER_EVENT_ANALYSIS_RAN_GSM");

        private final String xPath;

        SubLaunchMenuLevelTwo(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    public enum SubLaunchMenuLevelThree {
        EVENT_ANALYSIS_RAN_LTE_CFA("RAN_LTE_CFA_SUMMARY"), EVENT_ANALYSIS_RAN_LTE_HFA("RAN_LTE_HFA"), EVENT_ANALYSIS_RAN_WCDMAHFA(
                "SUBSCRIBER_HANDOVER_FAILURE_EVENT_ANALYSIS"), EVENT_ANALYSIS_RAN_GSM_CFA(
                "GSM_SUBSCRIBER_SUMMARY_EVENT_ANALYSIS_BY"), GSM_PS_SUBSCRIBER_SUMMARY_EVENT_ANALYSIS_BY(
                "GSM_PS_SUBSCRIBER_SUMMARY_EVENT_ANALYSIS_BY");

        private final String xPath;

        SubLaunchMenuLevelThree(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }
    }

    // End of changes for LTE CFA

    public enum ImsiMenu {
        IMSI("IMSI", "imsi"), IMSI_GROUP("IMSIGroup", "groupname"), PTMSI("PTMSI", "ptmsi"), MSISDN("MSISDN", "msisdn");

        private final String windowName;

        private final String nameUsedInURL; // is used when you send HTTP GET
                                            // request

        ImsiMenu(final String name, final String letter) {
            windowName = name;
            nameUsedInURL = letter;
        }

        private String getXPath() {
            return "//a[@id=\"" + windowName + "\"]";
        }

        public String getNameUsedInURL() {
            return nameUsedInURL;
        }
    }

    @Override
    protected void clickStartMenu() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + StartMenu.START.getXPath());
        selenium.click(StartMenu.START.getXPath());
    }

    protected void clickLaunchMenu() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + LaunchMenu.LAUNCH.getXPath());
        selenium.click(LaunchMenu.LAUNCH.getXPath());
    }

    public void openEventAnalysisWindow(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            final List<SubscriberTab.SubStartMenu> subMenu = new ArrayList<SubscriberTab.SubStartMenu>(
                    Arrays.asList(SubscriberTab.SubStartMenu.SUBSCRIBER_EVENT_ANALYSIS_CORE));
            openSubMenusFromStartMenu(SubscriberTab.StartMenu.EVENT_ANALYSIS, subMenu);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowSubMenu(final List<SubscriberTab.SubStartMenu> subMenu, final ImsiMenu type,
            final String value) throws InterruptedException, PopUpException {

        enterValue(type, value);

        openSubMenusFromStartMenu(SubscriberTab.StartMenu.EVENT_ANALYSIS, subMenu);

        selenium.waitForPageLoadingToComplete();
    }

    public void openSubscriberOverviewWindow(final ImsiMenu type, final String value) throws PopUpException,
            InterruptedException {
        enterValue(type, value);
        Thread.sleep(5000);
        openStartMenu(StartMenu.SUBSCRIBER_OVERVIEW);
        selenium.waitForPageLoadingToComplete();
    }

    public void openSubscriberOverviewWindowSubMenu(final List<SubscriberTab.SubStartMenu> subMenu,
            final ImsiMenu type, final String value) throws PopUpException, InterruptedException {
        enterValue(type, value);
        Thread.sleep(5000);
        openSubMenusFromStartMenu(StartMenu.SUBSCRIBER_OVERVIEW, subMenu);
        selenium.waitForPageLoadingToComplete();
    }

    public boolean subscriberOverviewSubMenuWithDisabledItem(final List<SubscriberTab.SubStartMenu> subMenu,
            final ImsiMenu type, final String value) throws PopUpException, InterruptedException {
        enterValue(type, value);
        Thread.sleep(5000);
        final boolean isSubMenuItemDisabled = checkSubMenuItemIsDisabled(StartMenu.SUBSCRIBER_OVERVIEW, subMenu);
        Thread.sleep(3000);

        return isSubMenuItemDisabled;
        // selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForMSS(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openStartMenu(StartMenu.EVENT_ANALYSIS_FOR_MSS);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForLTECFA(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openLaunchMenu(LaunchMenu.EVENT_ANALYSIS, SubLaunchMenuLevelOne.EVENT_ANALYSIS_RAN,
                    SubLaunchMenuLevelTwo.EVENT_ANALYSIS_RAN_LTE, SubLaunchMenuLevelThree.EVENT_ANALYSIS_RAN_LTE_CFA);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForLTEHFA(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openLaunchMenu(LaunchMenu.EVENT_ANALYSIS, SubLaunchMenuLevelOne.EVENT_ANALYSIS_RAN,
                    SubLaunchMenuLevelTwo.EVENT_ANALYSIS_RAN_LTE, SubLaunchMenuLevelThree.EVENT_ANALYSIS_RAN_LTE_HFA);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForWCDMAHFA(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openLaunchMenu(LaunchMenu.EVENT_ANALYSIS, SubLaunchMenuLevelOne.EVENT_ANALYSIS_RAN,
                    SubLaunchMenuLevelTwo.EVENT_ANALYSIS_RAN_WCDMAHFA,
                    SubLaunchMenuLevelThree.EVENT_ANALYSIS_RAN_WCDMAHFA);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    private void enterValue(final ImsiMenu type, final String value) throws InterruptedException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't
                            // expect below method working normally.
        enterSearchValue(value, type == ImsiMenu.IMSI_GROUP);
    }

    //>>>>>>>>>>>>
    public void openEventAnalysisWindowForGSMCFA(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openLaunchMenu(LaunchMenu.EVENT_ANALYSIS, SubLaunchMenuLevelOne.EVENT_ANALYSIS_RAN,
                    SubLaunchMenuLevelTwo.EVENT_ANALYSIS_RAN_GSM, SubLaunchMenuLevelThree.EVENT_ANALYSIS_RAN_GSM_CFA);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }

    //>>>>>>>>>>>>

    public void openEventAnalysisWindowForGSMCFA_PS(final ImsiMenu type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        enterValue(type, value);
        if (useStartMenu) {
            openLaunchMenu(LaunchMenu.EVENT_ANALYSIS, SubLaunchMenuLevelOne.EVENT_ANALYSIS_RAN,
                    SubLaunchMenuLevelTwo.EVENT_ANALYSIS_RAN_GSM,
                    SubLaunchMenuLevelThree.GSM_PS_SUBSCRIBER_SUMMARY_EVENT_ANALYSIS_BY);
        } else {
            enterSubmit(type == ImsiMenu.IMSI_GROUP);
        }
        selenium.waitForPageLoadingToComplete();
    }
}
