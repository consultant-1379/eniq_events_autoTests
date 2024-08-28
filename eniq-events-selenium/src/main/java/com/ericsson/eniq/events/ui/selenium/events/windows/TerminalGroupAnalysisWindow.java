/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ui.selenium.events.windows;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.IViewSubMenu;
import com.ericsson.eniq.events.ui.selenium.events.elements.ViewSubMenuController;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class TerminalGroupAnalysisWindow extends CommonWindow {

    public enum ViewMenu {
        MOST_POPULAR("Most Frequent Signaling"), MOST_POPULAR_EVENT_SUMMARY("Most Frequent Signaling Event Summary"), MOST_ATTACH_FAILURES(
                "Most Attach Failures"), MOST_PDP_SESSION_SETUP_FAILURES("Most PDP Session Setup Failures"), MOST_MOBILITY_ISSUES(
                "Most Mobility Issues"), HIGHEST_DATAVOLUME("Highest Data Volume");

        private final String subMenuName;

        ViewMenu(final String name) {
            subMenuName = name;
        }

        public String getDisplayName() {
            return subMenuName;
        }

        public String getXPath() {
            return "//div[contains(@class,'x-menu-list')]//a[contains(text(),'" + subMenuName + "')]";
        }
    }

    private final IViewSubMenu viewSubMenuController;

    public TerminalGroupAnalysisWindow(final String divId) {
        super(divId);
        viewSubMenuController = new ViewSubMenuController();
    }

    @Override
    protected void init() {
        super.init();
        beanFactory.autowireBeanProperties(viewSubMenuController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
    }

    public void clickViewSubMenu(final ViewMenu subMenu) throws PopUpException {
        viewSubMenuController.openViewSubMenu(subMenu.getXPath());
    }

}
