package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ColumnChooserFunctiomalityTestGroup extends EniqEventsUIBaseSeleniumTest {

    @Autowired
    private NetworkTab networkTab;

    @Autowired
    private Workspace workspace;

    @Autowired
    WorkspaceRC workspaceRC;

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow networkEventAnalysis;

    static final String COLUMNS = "//div[contains(@class, 'x-ignore x-menu x-component')]//div/a[contains(@class,' x-menu-item x-menu-item-arrow x-component')]";
    static final String TABLE_HEADER_EA = "//div[span='Event Type']/a";
    static final String CURRENT_WINDOW = "//div[@id='NETWORK_EVENT_ANALYSIS']//div[contains(@class,'x-grid3-scroller')]";

    /*
     * Test Case 4.42.1 :On all of the Event Analysis pages, the new column structure should be visible.
     */
    public void tableHeader() throws InterruptedException, PopUpException {
        waitForPageLoadingToComplete();
        selenium.mouseOver("//div[span='Event Type']");
        selenium.click(TABLE_HEADER_EA);
        Thread.sleep(2000);
        selenium.mouseOver(COLUMNS);
        Thread.sleep(2000);
    }

    public void columnStructureVisibleUtil(String dimension) throws Exception {

        workspaceRC.waitForElementToBePresent(TABLE_HEADER_EA);
        tableHeader();
        assertTrue(DataIntegrityStringConstants.NEW_COLUMN_STRUCTURE, selenium.isElementPresent(COLUMNS));

        workspaceRC.closeWindow(CURRENT_WINDOW);
        workspaceRC.checkAndOpenSideLaunchBar();
        logger.log(Level.INFO, DataIntegrityStringConstants.COLUMN_STRUCTURE_VISIBLE + " for " + dimension);
    }

    @Test
    public void APNColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.APN);
    }

    @Test
    public void ControllerColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.CONTROLLER);
    }

    @Test
    public void AccessAreColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.ACCESS_AREA);
    }

    @Test
    public void SGSNColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.SGSN_MME);
    }

    @Test
    public void APNGroupcolumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        final String searchvalueforAPNGroup = reservedDataHelper.getCommonReservedData(CommonDataType.APN_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.APN_GROUP);
    }

    @Test
    public void ControllerGroupColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        final String searchvalueforControllerGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.CONTROLLER_GROUP);
    }

    @Test
    public void AcessAreaGroupColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        final String searchvalueforAccessAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.ACCESS_AREA_GROUP);
    }

    @Test
    public void SGSNGroupColumnStructureVisibleinLineWithDesignTemplate_4_42_1() throws Exception {
        final String searchvalueforSGSNGroup = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        columnStructureVisibleUtil(SeleniumConstants.SGSN_MME_GROUP);
    }

    /* Test Case 4.42.2: On all of the Event Analysis pages, in the new column structure there should be some default columns selected on loading */

    final String OPTIONS_BY_DEFAULT_CHECKED = "//a[contains(@class, 'x-menu-checked')][contains(text(),'";

    private void defaultColumnsSelectedOnLoadingUtil(List<String> defaultOptionsChecked) throws PopUpException, InterruptedException {
        tableHeader();
        for (String string : defaultOptionsChecked) {
            assertTrue("Default columns are not selected on loading", selenium.isElementPresent(OPTIONS_BY_DEFAULT_CHECKED + string + "')]"));
        }
        Thread.sleep(7000);
        workspaceRC.closeWindow(CURRENT_WINDOW);
    }

    @Test
    public void APNDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.APN, DataIntegrityStringConstants.CORE_PS, DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("APN", "Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void ControllerDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller", "Event Type", "Failures", "Successes",
                "Total Events", "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void AccessAreaDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller", "Access Area", "Event Type", "Failures",
                "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void SGSNDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("SGSN-MME", "Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void APNGroupDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        final String searchvalueforAPNGroup = reservedDataHelper.getCommonReservedData(CommonDataType.APN_GROUP);//"DG_GroupNameAPN_1",
        workspaceRC.launchWindow(SeleniumConstants.APN_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void ControllerGroupDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        final String searchvalueforControllerGroup = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void AccessAreaGroupDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        final String searchvalueforAccessAreaGroup = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    @Test
    public void SGSNGrouprDefaultColumnsSelectedOnLoading_4_42_2() throws Exception {
        final String searchvalueforSGSNGroup = reservedDataHelper.getCommonReservedData(CommonDataType.SGSN_GROUP);
        workspaceRC.launchWindow(SeleniumConstants.SGSN_MME_GROUP, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        List<String> defaultOptionsChecked = new ArrayList<String>(Arrays.asList("Event Type", "Failures", "Successes", "Total Events",
                "Success Ratio", "Impacted Subscribers"));
        defaultColumnsSelectedOnLoadingUtil(defaultOptionsChecked);
    }

    /* Test Case 4.42.3:On all of the Event Analysis pages, in the new column structure, columns can be added or removed as per the users preference. */

    final String NEW_HEADERS = "//a[contains(@class, 'x-menu-check')][contains(text(),'";
    final String OPTIONAL_HEADERS_CHECKED = "//a[contains(@class, 'x-menu-checked')][contains(text(),'";
    final String OPTIONAL_HEADERS_UNCHECKED = "//a[contains(@class, 'x-menu-checked')][contains(text(),'";

    @Test
    public void ControllerNewColumnsAddedorRemoved_4_42_3() throws Exception {
        final String searchvalueForController = reservedDataHelper.getCommonReservedData(CommonDataType.CONTROLLER);
        workspaceRC.launchWindow(SeleniumConstants.CONTROLLER, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        tableHeader();
        hiddenHeadersAreCheckedForController();
        workspaceRC.closeWindow(CURRENT_WINDOW);
        launchEventAnalysisWindow();
        tableHeader();
        optionalCloumnsAreUncheckedForController();
        workspaceRC.closeWindow(CURRENT_WINDOW);
    }

    @Test
    public void AccessAreaNewColumnsAddedorRemoved_4_42_3() throws Exception {
        final String searchvalueforAccessArea = reservedDataHelper.getCommonReservedData(CommonDataType.ACCESS_AREA);
        workspaceRC.launchWindow(SeleniumConstants.ACCESS_AREA, DataIntegrityStringConstants.CORE_PS,
                DataIntegrityStringConstants.NETWORK_EVENT_ANALYSIS);
        tableHeader();
        hiddenHeadersAreCheckedForAccessArea();
        workspaceRC.closeWindow(CURRENT_WINDOW);
        launchEventAnalysisWindow();
        tableHeader();
        optionalCloumnsAreUncheckedForAccessArea();
        workspaceRC.closeWindow(CURRENT_WINDOW);
    }

    private void hiddenHeadersAreCheckedForController() {
        final List<String> hiddenHeadersForAPN = new ArrayList<String>(Arrays.asList("RAT ID"));
        for (String string : hiddenHeadersForAPN) {
            if (!selenium.isElementPresent(OPTIONAL_HEADERS_CHECKED + string + "')]")) {
                selenium.click(NEW_HEADERS + string + "')]");
            }
            assertTrue(selenium.isElementPresent(OPTIONAL_HEADERS_CHECKED + string + "')]"));
        }
    }

    private void hiddenHeadersAreCheckedForAccessArea() {
        final List<String> hiddenHeadersForAPN = new ArrayList<String>(Arrays.asList("RAT ID"));
        for (String string : hiddenHeadersForAPN) {
            if (!selenium.isElementPresent(OPTIONAL_HEADERS_CHECKED + string + "')]")) {
                selenium.click(NEW_HEADERS + string + "')]");
            }
            assertTrue(selenium.isElementPresent(OPTIONAL_HEADERS_CHECKED + string + "')]"));
        }
    }

    private void optionalCloumnsAreUncheckedForController() {
        final List<String> optionalCloumnsForAPN = new ArrayList<String>(Arrays.asList("RAT ID"));
        for (String string : optionalCloumnsForAPN) {
            selenium.click(OPTIONAL_HEADERS_UNCHECKED + string + "')]");
            assertTrue(selenium.isElementPresent(NEW_HEADERS + string + "')]"));
        }
    }

    private void optionalCloumnsAreUncheckedForAccessArea() {
        final List<String> optionalCloumnsForAPN = new ArrayList<String>(Arrays.asList("RAT ID"));
        for (String string : optionalCloumnsForAPN) {
            selenium.click(OPTIONAL_HEADERS_UNCHECKED + string + "')]");
            assertTrue(selenium.isElementPresent(NEW_HEADERS + string + "')]"));
        }
    }

    private void launchEventAnalysisWindow() {

        workspaceRC.clickLaunchButton();
    }
}
