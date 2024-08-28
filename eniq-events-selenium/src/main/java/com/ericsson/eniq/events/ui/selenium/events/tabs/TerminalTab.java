/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.tabs;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericker
 * @since 2010
 *
 */
@Component
public class TerminalTab extends Tab {

    private final String typesComboInputXPath;
    
    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public TerminalTab() {
        super("TERMINAL_TAB");
        this.typesComboInputXPath = tabXPath + "//input[@id='selenium_tag_typesCombo-input']";
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
    }

    public void openSubStartMenu(final StartMenu menu) throws PopUpException {
        clickStartMenu();
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + tabButtonXPath);
        selenium.waitForElementToBePresent(menu.getXPath(), DEFAULT_TIMEOUT);
        selenium.mouseOver(menu.getXPath());
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

    public void openSubOfSubStartMenu(final StartMenu startMenu, final SubStartMenu subStartMenu,
            final SubOfSubStartMenu subOfSubStartMenu) throws PopUpException {
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

    /* This method is to open the sub menus from the start menu
     * irrespective of sub menu length.
     *  
     * 
     */

    public void openSubMenusFromStartMenu(final StartMenu menu, final List<TerminalTab.SubStartMenu> SubMenuList)
            throws PopUpException {
        clickStartMenu();
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        selenium.mouseOver(menu.getXPath());
        final int SubMenuListSize = SubMenuList.size();
        final TerminalTab.SubStartMenu lastElement = SubMenuList.get(SubMenuListSize - 1);
        for (final TerminalTab.SubStartMenu subMenu : SubMenuList) {
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

    public void setSearchType(final TerminalType type) {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + dropGoupButtonXPath);
        selenium.click(dropGoupButtonXPath);
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + type.getXPath());
        selenium.click(type.getXPath());
    }

    @Override
    protected void clickStartMenu() {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + StartMenu.START.getXPath());
        selenium.click(StartMenu.START.getXPath());
    }

    public void openTerminalMakerFiledsDropDownWindow() {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + typesComboInputXPath);
        selenium.type(typesComboInputXPath, "");
        selenium.keyDown(typesComboInputXPath, "\\31");
        selenium.keyUp(typesComboInputXPath, "\\31");

        selenium.waitForElementToBePresent(
                "//div[contains(@class, 'x-liveload x-ignore x-border')][contains(@style, 'visibility: visible')]",
                "12000");
    }

    public void openTerminalMakerSelect(final String string) {
  	    loggerDuplicate.log(Level.INFO, "The Element ID : " + typesComboInputXPath);
        
  	    selenium.click(typesComboInputXPath);
  	    selenium.type(typesComboInputXPath, "");
        selenium.type(typesComboInputXPath, string);
        
        selenium.keyDown(typesComboInputXPath, "\\31");
        selenium.keyUp(typesComboInputXPath, "\\31");
        try {
			Thread.sleep(2000); //To undisable second text-box
		} catch (InterruptedException e) {

		}
        selenium.keyDown(typesComboInputXPath, "\\9");
        selenium.keyUp(typesComboInputXPath, "\\9");
    }

    public enum StartMenu {
        START("TERMINAL_TAB_START"), GROUP_ANALYSIS("TERMINAL_GROUP_ANALYSIS"), TERMINAL_ANALYSIS(
                "TERMINAL_MOST_POPULAR_SUMMARY"), EVENT_ANALYSIS("EVENT_ANALYSIS_TERMINAL"), TERMINAL_RANKINGS(
                "TERMINAL_RANKING_GROUP"), TERMINAL_GROUP_ANALYSIS("TERMINAL_GROUP_ANALYSIS"), TERMINAL_ANALYSIS_WCDMA(
                "TERMINAL_ANALYSIS"), TERMINAL_EVENT_ANALYSIS("EVENT_ANALYSIS_TERMINAL"), QOS_STATISTICS("TERMINAL_QOS_STATISTICS");

        private final String windowName;

        StartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum SubStartMenu {
        EVENT_RANKING("EVENT_GROUP"), DATA_VOLUME_RANKING("DATA_VOLUME_GROUP"),
        
       EVENT_ANALYSIS_RAN("EVENT_ANALYSIS_TERMINAL_RAN"), GROUP_ANALYSIS("TERMINAL_GA_MOST_POPULAR_SUMMARY"),

        //For WCDMA Menu items
        TERMINAL_RANKING_EVENT_MENU_ITEM_WCDMA("EVENT_GROUP"), TERMINAL_RANKING_RAN_MENU_ITEM_WCDMA(
                "NETWORK_TAC_RANKING_RAN"), TERMINAL_RANKING_WCDMA_MENU_ITEM_WCDMA("TERMINAL_RANKING_RAN_WCDMA"), TERMINAL_RANKING_HFA_MENU_ITEM_WCDMA(
                "RAN_WCDMA_TAC_HFA"), TERMINAL_GROUP_RAN_MENU_ITEM_WCDMA("TERMINAL_GROUP_ANALYSIS_RAN"), TERMINAL_GROUP_WCDMA_MENU_ITEM_WCDMA(
                "TERMINAL_GROUP_ANALYSIS_RAN_WCDMA"), TERMINAL_GROUP_HFA_SOHO_MENU_ITEM_WCDMA(
                "TERMINAL_GROUP_ANALYSIS_RAN_WCDMA_HFA_SOHO"), TERMINAL_ANALYSIS_RAN_MENU_ITEM_WDCMA(
                "TERMINAL_ANALYSIS_RAN"), TERMINAL_ANALYSIS_WCDMA_MENU_ITEM_WDCMA("TERMINAL_ANALYSIS_RAN_WCDMA"), TERMINAL_ANALYSIS_HFA_MENU_ITEM_WDCMA(
                "TERMINAL_ANALYSIS_RAN_WCDMA_HFA_SOHO"), TERMINAL_EVENT_ANALYSIS_RAN_MENU_ITEM_WDCMA(
                "EVENT_ANALYSIS_TERMINAL_RAN"), TERMINAL_EVENT_ANALYSIS_WCDMA_MENU_ITEM_WDCMA(
                "EVENT_ANALYSIS_TERMINAL_RAN_WCDMA"), TERMINAL_EVENT_ANALYSIS_WCDMA_HFA_SUMMARY(
                "TERMINAL_EVENT_ANALYSIS_WCDMA_HFA_SUMMARY"), TERMINAL_EVENT_ANALYSIS_CORE("TERMINAL_EVENT_ANALYSIS"),

        //WCDMA CFA
        TERMINAL_CALL_FAILURE_ANALYSIS("RAN_WCDMA_TAC_CFA"), TERMINAL_GROUP_ANALYSIS_CFA_CALL_DROPS(
                "TERMINAL_GROUP_ANALYSIS_RAN_WCDMA_CFA_CALL_DROPS"), TERMINAL_ANALYSIS_CFA_MOST_DROPS(
                "TERMINAL_ANALYSIS_RAN_WCDMA_CALLFAILURE_MOST_DROPS"), TERMINAL_EVENT_ANALYSIS_WCDMA_CFA(
                "TERMINAL_EVENT_ANALYSIS_WCDMA_CFA_SUMMARY"),
                
          // LTE
          EVENT_ANALYSIS_RAN_LTE ("EVENT_ANALYSIS_TERMINAL_RAN_LTE"), EVENT_ANALYSIS_LTE_CFA_SUMMARY ("TERMINAL_EVENT_ANALYSIS_LTE_CFA_SUMMARY"),
          EVENT_ANALYSIS_LTE_HFA_SUMMARY ("TERMINAL_EVENT_ANALYSIS_LTE_HFA_SUMMARY");          

        private final String windowName;

        SubStartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum SubOfSubStartMenu {
        EVENT_RANKING_TERMINAL("TERMINAL_RANKINGS"), DATA_VOLUME_RANKING_TERMINAL("DATAVOLUME_RANKING_TAC"), DATA_VOLUME_RANKING_TERMINAL_GROUP(
                "DATAVOLUME_RANKING_TERMINAL_GROUP");

        private final String windowName;

        SubOfSubStartMenu(final String name) {
            windowName = name;
        }

        private String getXPath() {
            return "//*[@id='" + windowName + "']";
        }
    }

    public enum TerminalType {
        TERMINAL("mobile_phone"), TERMINAL_GROUP("group_terminal");

        private final String id;

        TerminalType(final String name) {
            id = name;
        }

        private String getXPath() {
            return "//*[contains(@class, 'x-menu-item " + id + "')]";
        }
    }

    public void openEventAnalysisWindow(final TerminalType type, final boolean submitButton, final String... values)
            throws PopUpException, InterruptedException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            enterSearchValue(values[0], true);
            Thread.sleep(5000);
        } else {
            openTerminalMakerSelect(values[0]); // terminal maker
            Thread.sleep(5000);
            enterSearchValue(values[1] + "," + values[2], false); // terminal model + TAC 
            Thread.sleep(5000);
        }

        if (submitButton) {
            enterSubmit(type.equals(TerminalType.TERMINAL_GROUP));
        } else {
            final List<TerminalTab.SubStartMenu> subMenu = new ArrayList<TerminalTab.SubStartMenu>(
                    Arrays.asList(TerminalTab.SubStartMenu.TERMINAL_EVENT_ANALYSIS_CORE));
            openSubMenusFromStartMenu(TerminalTab.StartMenu.EVENT_ANALYSIS, subMenu);
        }

        selenium.waitForPageLoadingToComplete();
    }
    
    public void openQosStatisticsWindow(final TerminalType type, final boolean submitButton, final String... values)
            throws PopUpException, InterruptedException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            enterSearchValue(values[0], true);
            Thread.sleep(5000);
        } else {
            openTerminalMakerSelect(values[0]); // terminal maker
            Thread.sleep(5000);
            enterSearchValue(values[1] + "," + values[2], false); // terminal model + TAC 
            Thread.sleep(5000);
        }
        
        openSubStartMenu(TerminalTab.StartMenu.QOS_STATISTICS);
        selenium.waitForPageLoadingToComplete();
        
        
    }

    
    public void openEventAnalysisWindowForLTE(final TerminalType type, final SubStartMenu failureType, final String... values)
            throws PopUpException, InterruptedException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            enterSearchValue(values[0], true);
            Thread.sleep(5000);
        } else {
            openTerminalMakerSelect(values[0]); // terminal maker
            Thread.sleep(5000);
            enterSearchValue(values[1] + "," + values[2], false); // terminal model + TAC 
            Thread.sleep(5000);
        }

        final List<TerminalTab.SubStartMenu> subMenu = new ArrayList<TerminalTab.SubStartMenu>(
        Arrays.asList(SubStartMenu.EVENT_ANALYSIS_RAN, SubStartMenu.EVENT_ANALYSIS_RAN_LTE, failureType));
        openSubMenusFromStartMenu(TerminalTab.StartMenu.EVENT_ANALYSIS, subMenu);     

        selenium.waitForPageLoadingToComplete();
    }
    
    public void enterAnalysisValues(final TerminalType type, final boolean submitButton, final String... values)
            throws PopUpException, InterruptedException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't
                            // expect below method working normally.

        if (type.equals(TerminalType.TERMINAL_GROUP)) {
            enterSearchValue(values[0], true);
            Thread.sleep(5000);
        } else {
            openTerminalMakerSelect(values[0]); // terminal maker
            Thread.sleep(5000);
            enterSearchValue(values[1] + "," + values[2], false); // terminal
                                                                  // model +
                                                                  // TAC
            Thread.sleep(5000);
        }
    }
}
