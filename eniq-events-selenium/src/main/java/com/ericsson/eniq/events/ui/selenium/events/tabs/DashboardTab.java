package com.ericsson.eniq.events.ui.selenium.events.tabs;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DashboardTab extends Tab {

    public static final String TAB_ID = "DASHBOARD_TAB";

    protected static Logger loggerDuplicate = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    public DashboardTab() {
        super(TAB_ID);
    }

    public void setSearchType(final DashboardType type) {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + typeButtonXPath);
        selenium.click(typeButtonXPath);
        loggerDuplicate.log(Level.INFO, "The Element ID : " + type.getXPath());
        selenium.click(type.getXPath());

    }

    public enum DashboardType {
        APN("APN"), CONTROLLER("BSC"), ACCESS_AREA("CELL"), SGSN_MME("SGSN"), MSC("MSC"), APN_GROUP("APNGroup"),
        CONTROLLER_GROUP("BSCGroup"), ACCESS_AREA_GROUP("CELLGroup"), SGSN_MME_GROUP("SGSNGroup"), MSC_GROUP(
                "MSCGroup");

        private final String id;

        DashboardType(final String name) {
            id = name;
        }

        private String getXPath() {
            return "//*[@id='" + id + "']";
        }
    }

    public void openStartMenu(final startMenu menu) {
        clickStartMenu();
        selenium.waitForElementToBePresent(menu.getXPath(), "20000");
        loggerDuplicate.log(Level.INFO, "The Element ID : " + menu.getXPath());
        selenium.click(menu.getXPath());
    }

    public enum startMenu {
        START("selenium_tag_typeButton"), APN("APN"), CONTROLLER("BSC"), ACCESS_AREA("CELL"), SGSN("SGSN"), MSC(
                "MSC"), APN_GROUP("APNGroup"),
        CONTROLLER_GROUP("BSCGroup"), ACCESS_AREA_GROUP("CELLGroup"), SGSN_MME_GROUP("SGSNGroup"), MSC_GROUP(
                "MSCGroup");

        private final String xPath;

        startMenu(final String path) {
            xPath = path;
        }

        private String getXPath() {
            return "//*[@id=\"" + xPath + "\"]";
        }

    }

    @Override
    protected void clickStartMenu() {
        loggerDuplicate.log(Level.INFO, "The Element ID : " + startMenu.START.getXPath());
        selenium.click(startMenu.START.getXPath());
    }


    public void openAPNportlets(final DashboardType type, final boolean useStartMenu, final String value)
            throws InterruptedException, PopUpException {
        openTab();
        setSearchType(type);
        Thread.sleep(7000); // eseuhon, DO NOT change this otherwise you won't expect below method working normally.
        enterSearchValue(value, isGroup(type));
        enterSubmit(isGroup(type));
        selenium.waitForPageLoadingToComplete();
    }

    public boolean isGroup(final DashboardType type) {
        return (type == DashboardType.APN_GROUP) || (type == DashboardType.CONTROLLER_GROUP)
                || (type == DashboardType.ACCESS_AREA_GROUP) || (type == DashboardType.SGSN_MME_GROUP)
                || (type == DashboardType.MSC_GROUP);
    }

    /**
     * @return not <tt>null</tt> columns list of rows list of portlet headers
     * @see #getPortletHeader(int, int)
     * @see #getPortletColumnCount()
     * @see #getPortletRowsCount(int)
     */
    public List<List<String>> getPortletHeadersListOfList() {
        int columnCount = getPortletColumnCount();
        List<List<String>> headers = new ArrayList<List<String>>(columnCount);

        for (int columnIndex = 1; columnIndex < columnCount + 1; columnIndex++) {
            int rowsCount = getPortletRowsCount(columnIndex);
            final ArrayList<String> rowsList = new ArrayList<String>(rowsCount);
            headers.add(columnIndex - 1, rowsList);
            for (int rowIndex = 1; rowIndex < rowsCount + 1; rowIndex++) {
                String header = getPortletHeader(columnIndex, rowIndex);
                rowsList.add(rowIndex - 1, header);
            }
        }

        return headers;
    }

    /**
     * @param columnIndex starting from 1
     * @param rowIndex    starting from 1
     * @return header of portlet
     * @see #getPortletColumnCount()
     * @see #getPortletRowsCount(int)
     * @see #getPortletHeaderLocator(int, int)
     */
    public String getPortletHeader(int columnIndex, int rowIndex) {
        return selenium.getText(getPortletHeaderLocator(columnIndex, rowIndex));
    }

    /**
     * @param columnIndex starting from 1
     * @param rowIndex    starting from 1
     * @return portlet header locator
     * @see #getPortletColumnCount()
     * @see #getPortletRowsCount(int)
     * @see #getPortletHeader(int, int)
     * @see #getPortletHeaderLocatorByName(String)
     */
    public String getPortletHeaderLocator(int columnIndex, int rowIndex) {
        return tabXPath +
                "//div[contains(@class,'dragdrop-boundary')]//table//td[" + columnIndex +
                "]//table[contains(@class,'dragdrop-dropTarget') and contains(@class,'portlets-column')]//tr[" +
                rowIndex +
                "]//div[contains(@class,'dragdrop-draggable')]//div[contains(@class,'dragdrop-handle')]//div[contains(text(),'')]";
    }

    /**
     * @param headerName precise header text
     * @return portlet header locator
     * @see #getPortletColumnCount()
     * @see #getPortletRowsCount(int)
     * @see #getPortletHeader(int, int)
     * @see #getPortletHeaderLocator(int, int)
     */
    public String getPortletHeaderLocatorByName(String headerName) {
        return tabXPath +
                "//div[contains(@class,'dragdrop-boundary')]//table//td//table[contains(@class,'dragdrop-dropTarget') " +
                "and contains(@class,'portlets-column')]//tr//div[contains(@class,'dragdrop-draggable')]" +
                "//div[contains(@class,'dragdrop-handle')]//div[contains(text(),'" + headerName + "')]";
    }

    /**
     * @param columnIndex starting from 1
     * @return portlet drag-drop column area locator
     * @see #getPortletColumnCount()
     * @see #getPortletRowsCount(int)
     * @see #getPortletHeader(int, int)
     */
    public String getPortletDragDropColumnAreaLocator(int columnIndex) {
        return tabXPath + "//div[contains(@class,'dragdrop-boundary')]//table//td[" + columnIndex +
                "]/table[contains(@class,'dragdrop-dropTarget')]";
    }

    /**
     * @return number of portlet columns
     * @see #getPortletRowsCount(int)
     * @see #getPortletHeader(int, int)
     */
    public int getPortletColumnCount() {
        Number count = selenium.getXpathCount(tabXPath +
                "//div[contains(@class,'dragdrop-boundary')]//table//td//table[contains(@class,'dragdrop-dropTarget') and contains(@class,'portlets-column')]");
        return count.intValue();
    }

    /**
     * @param columnIndex starting from 1
     * @return number of portlet rows in the given column
     * @see #getPortletColumnCount()
     * @see #getPortletHeader(int, int)
     */
    public int getPortletRowsCount(int columnIndex) {
        Number count = selenium.getXpathCount(tabXPath +
                "//div[contains(@class,'dragdrop-boundary')]//table//td[" + columnIndex +
                "]//table[contains(@class,'dragdrop-dropTarget') and contains(@class,'portlets-column')]//tr//div[contains(@class,'dragdrop-draggable')]//div[contains(@class,'dragdrop-handle')]//div[contains(text(),'')]");
        return count.intValue();
    }

    public void restorePortletsLayout(List<List<String>> headersLayoutList) {
        try {
            for (int i = 0; i < headersLayoutList.size(); i++) {
                List<String> columnList = headersLayoutList.get(i);
                String columnLocator = getPortletDragDropColumnAreaLocator(i + 1);
                if (selenium.isElementPresent(columnLocator)) {
                    for (String headerName : columnList) {
                        String locatorByName = getPortletHeaderLocatorByName(headerName);

                        if (selenium.isElementPresent(locatorByName)) {
                            selenium.dragAndDropToObject(locatorByName, columnLocator);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    public void waitAllPortletsToBePresent(final String timeout) {
        waitAllPortletsToBePresent(getPortletHeadersListOfList(), timeout);
    }

    public void waitAllPortletsToBePresent(List<List<String>> headersLayoutList,  final String timeout) {
        try {
            for (int i = 0; i < headersLayoutList.size(); i++) {
                List<String> columnList = headersLayoutList.get(i);
                String columnLocator = getPortletDragDropColumnAreaLocator(i + 1);
                if (!selenium.isElementPresent(columnLocator)) {
                    loggerDuplicate.info("Waiting for " + columnLocator + " to be present");
                    selenium.waitForElementToBePresent(columnLocator, timeout);
                }

                if (selenium.isElementPresent(columnLocator)) {
                    for (String headerName : columnList) {
                        String locatorByName = getPortletHeaderLocatorByName(headerName);

                        if (!selenium.isElementPresent(locatorByName)) {
                            loggerDuplicate.info("Waiting for " + columnLocator + " to be present");
                            selenium.waitForElementToBePresent(locatorByName, timeout);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }
}
