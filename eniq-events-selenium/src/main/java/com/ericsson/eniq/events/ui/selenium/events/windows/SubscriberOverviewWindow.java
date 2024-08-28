/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.windows;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.ISubscriberDetailsDialog;
import com.ericsson.eniq.events.ui.selenium.events.elements.IViewSubMenu;
import com.ericsson.eniq.events.ui.selenium.events.elements.SubscriberDetailsDialogController;
import com.ericsson.eniq.events.ui.selenium.events.elements.ViewSubMenuController;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eseuhon
 * @since 2010
 * 
 */
public class SubscriberOverviewWindow extends CommonWindow {

    public enum ViewMenu {
        FAILURES("Failures"), APN_USAGE("APN Usage"), BUSY_HOUR("BusyHour"), BUSY_DAY("BusyDay"), ACCESS_AREA(
                "Access Area"), TERMINALS("Terminals"), TAU("TAU"), HANDOVER("Handover"), CALL_FAILURE_ANALYSIS("Call Failure Analysis");

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

    private final ISubscriberDetailsDialog subDetailsDialogController;

    private final IViewSubMenu viewSubMenuController;

    public SubscriberOverviewWindow(final String divId) {
        super(divId);
        subDetailsDialogController = new SubscriberDetailsDialogController(divId);
        viewSubMenuController = new ViewSubMenuController();
    }

    @Override
    protected void init() {
        super.init();
        beanFactory.autowireBeanProperties(subDetailsDialogController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.autowireBeanProperties(viewSubMenuController, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
    }

    public void openSubscriberDetailsDialog() {
        subDetailsDialogController.openSubscriberDetailsDialog();
    }

    public void openSubscriberDetailsDialogWcdmaCfa() {
        subDetailsDialogController.openSubscriberDetailsDialogWcdmaCfa();
    }

    public void closeSubscriberDetailsDialog() {
        subDetailsDialogController.closeSubscriberDetailsDialog();
    }

    public Map<String, String> getSubscriberDetailDialogData() throws NoDataException {
        final Map<String, String> subDetailsDialogData = new HashMap<String, String>();

        for (int i = 0; i < subDetailsDialogController.getTableRowCount(); i++) {
            subDetailsDialogData.put(subDetailsDialogController.getTableData(i, 0),
                    subDetailsDialogController.getTableData(i, 1));

        }
        System.out.println(subDetailsDialogData);
        return subDetailsDialogData;
    }

    public void clickViewSubMenu(final ViewMenu subMenu) throws PopUpException {
        viewSubMenuController.openViewSubMenu(subMenu.getXPath());
    }

    public void clickRefreshButton() throws InterruptedException {
        subDetailsDialogController.clickRefreshButton();
    }
}
