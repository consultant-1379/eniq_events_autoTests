package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.DashboardTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.DashboardTab.DashboardType;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.DashboardUIBaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DashboardFrameworkTestGroup extends DashboardUIBaseSeleniumTest {

    // TODO: consider to do login and logout only once for a pack of tests

    //'X'icon x-path for all the portlets
    static final String X_ICON_BUSINESS_OBJECTS_PORTLET = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    static final String X_ICON_DATAVOL_ANALYSIS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'DATAVOL_ANALYSIS')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    static final String X_ICON_DASHBOARD_TOP_TERMINALS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    static final String X_ICON_HOMER_ROAMER = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'HOMER_ROAMER')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    static final String X_ICON_RADIO_NETWORK_KPI = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'RADIO_NETWORK_KPI')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    static final String X_ICON_CORE_NETWORK_KPIS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'CORE_NETWORK_KPIS')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]";

    //setting icon x-path for all the portlets
    static final String SETTING_ICON_BUSINESS_OBJECTS_PORTLET = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    static final String SETTING_ICON_DATAVOL_ANALYSIS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'DATAVOL_ANALYSIS')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    static final String SETTING_ICON_DASHBOARD_TOP_TERMINALS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    static final String SETTING_ICON_HOMER_ROAMER = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'HOMER_ROAMER')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    static final String SETTING_ICON_RADIO_NETWORK_KPI = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'RADIO_NETWORK_KPI')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    static final String SETTING_ICON_CORE_NETWORK_KPIS = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'CORE_NETWORK_KPIS')]//table/tbody/tr/td[1]/div/img[contains(@class,'gwt-Image')]";

    //Dashboard ComoboBox Buttons
    static final String DASHBOARD_COMBOBOX_BUTTON = "//table[@id='selenium_tag_typeButton']//button";

    static final String DASHBOARD_DATESELECT_COMOBOBOX = "//table//td[@class='x-toolbar-right']//div[contains(@class,'x-form-field-wrap  x-component')]";

    static final String DATE_DIALOG_COMBOBOX = "//div[contains(@class,'x-date-picker dashboardDatePicker x-component x-unselectable')]";

    static final String BUSINESS_OBJECTS_PORTLET = "//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
            + "//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]//div[contains(@class,'GCAJMMPCOB')]//div//img";

    static final String tabname = "BUSINESS_OBJECTS_TAB";

    static final String RADIO_NETWORK = "//table[@id='selenium_tag_typeButton']/tbody/tr[2]/td[2]/em/button[text()='3G Radio Network']";
    
    //Group Management
    static final String OPTIONS_MENU_OPEN = "//div[@id='headerPnl']/table/tbody/tr/td[3]/div[@class='GIJC1R-BEE']";
    static final String GROUP_MANAGEMENT = "//div[@class='GIJC1R-BHE']//div[@class='GIJC1R-BFE'][text()='Group Management']";
    @Autowired
    protected DashboardTab dashboardTab;

    //Test case - 4.1.1: Verify that there is a tab titled Dashboard when logged into the EE UI.

    @Test
    public void tabTitledDashboardTabWhenLoggedintoTheEEUI4_1_1() {
        dashboardTab.openTab();
        assertTrue(DataIntegrityStringConstants.DASHBOARD_TAB, selenium.isTextPresent("Dashboard"));
        logger.log(Level.INFO, Logs.DASHBOARD_TAB);

    }

    /*Test case - 4.1.2: Verify that there are sub tabs to select the node types which have license underneath the Dashboard
      tab in the subtask menu bar*/

    @Test
    public void subTabstoSelecttheNodeTypes4_1_2() {

        dashboardTab.openTab();
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        nodeTypes();
        groupNodeTypes();
        logger.log(Level.INFO, Logs.SUB_TAB_NODE_TYPES);

    }

    /*Test case - 4.1.3: Verify that there are the licensed node types underneath the Dashboard tab in the subtask menu bar and can
     * be selected from the dropdown box*/

    @Test
    public void nodeTypesCanbeSelectedFromSubtaskMenuBar4_1_3() {
        dashboardTab.openTab();
        subTasksSlectedFromMenuBar();
        logger.log(Level.INFO, Logs.SUB_TAB_NODE_TYPES_SELECTED);

    }

    /*Test case - 4.1.4: Verify that by default few of the test portlets will be displayed along with the Network Event Volume portlet in 
     * the dashboard framework, which is in-line with the new Ericsson branding design of the dashboard framework.   */

    @Test
    public void defaultPortletsDisplayedOnDashboardFramework4_1_4() {
        dashboardTab.openTab();
        defaultPortlets();
        logger.log(Level.INFO, Logs.DEFAULT_PORTLETS);

    }

    /*Test case - 4.1.5: Verify that if any of those portlets are closed by clicking on x, the graphs are closed and then a +
     * can be seen in the centre of the portlet.*/

    @Test
    public void portletsAreClosedByClickingOn_X_ThenPlusCanbeSeen4_1_5() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();

        selenium.click(X_ICON_BUSINESS_OBJECTS_PORTLET);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]/div/div/div/div"));
        Thread.sleep(5000);

        selenium.click(X_ICON_DATAVOL_ANALYSIS);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'DATAVOL_ANALYSIS')]/div/div/div/div"));
        Thread.sleep(5000);

        selenium.click(X_ICON_DASHBOARD_TOP_TERMINALS);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]/div/div/div/div"));
        Thread.sleep(5000);

        selenium.click(X_ICON_HOMER_ROAMER);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'HOMER_ROAMER')]/div/div/div/div"));
        Thread.sleep(5000);

        selenium.click(X_ICON_RADIO_NETWORK_KPI);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'RADIO_NETWORK_KPI')]/div/div/div/div"));
        Thread.sleep(5000);

        selenium.click(X_ICON_CORE_NETWORK_KPIS);
        assertTrue("'+'cannot be seen in the centre of the portlet",
                selenium.isElementPresent("//div[contains(@id,'CORE_NETWORK_KPIS')]/div/div/div/div"));

    }

    /*Test case 4.1.6: Verify that if you click on +, which is at the centre of the closed portlet, the graph is opened*/

    @Test
    public void portletsAreReopenedByClickingOnPlusAtTheCentreOfThePortlet4_1_6() throws PopUpException,
            InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        selenium.click(X_ICON_BUSINESS_OBJECTS_PORTLET);
        selenium.click(X_ICON_DATAVOL_ANALYSIS);
        selenium.click(X_ICON_DASHBOARD_TOP_TERMINALS);
        selenium.click(X_ICON_HOMER_ROAMER);
        selenium.click(X_ICON_RADIO_NETWORK_KPI);
        selenium.click(X_ICON_CORE_NETWORK_KPIS);

        selenium.click("//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

        selenium.click("//div[contains(@id,'DATAVOL_ANALYSIS')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

        selenium.click("//div[contains(@id,'HOMER_ROAMER')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

        selenium.click("//div[contains(@id,'RADIO_NETWORK_KPI')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

        selenium.click("//div[contains(@id,'CORE_NETWORK_KPIS')]/div/div/div/div");
        Thread.sleep(5000);
        defaultPortlets();

    }

    /*Test case 4.22.2: Verify that the title of the graph is Top 5 Impacted Inbound Roamers by Country.*/

    @Test
    public void titleOfTheGraphisTop5ImpaxtedInboundRoamersByCountry_4_22_2() throws PopUpException,
            InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        assertTrue("Title of the graph is not expected title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Top 5 Impacted Inbound Roamers By Country')] "));

    }

    /*Test case 4.22.3: Verify that the Y-Axis has the names of the countries of it.*/

    @Test
    public void titleOfTheGraphisTop5ImpaxtedInboundRoamersByCountry_4_22_3() throws PopUpException,
            InterruptedException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        dashbboardSetDate();
        waitForPageLoadingToComplete();

        List<String> countries = new ArrayList<String>();
        countries.add("China");
        countries.add("Bangladesh");
        countries.add("Malaysia");
        countries.add("Greece");

        String countryChina = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-2')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][1]//node()[name()='tspan']//node()");
        String countryBangladesh = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-2')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][2]//node()[name()='tspan']//node()");
        String countryMalaysia = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-2')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][3]//node()[name()='tspan']//node()");
        String countryGreece = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-2')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][4]//node()[name()='tspan']//node()");

        boolean isTrue = countries.get(0).equals(countryChina) && countries.get(1).equals(countryBangladesh)
                && countries.get(2).equals(countryMalaysia) && countries.get(3).equals(countryGreece);
        assertTrue("Y-Axis does not has the names of the countries of it, for Radio Network", isTrue);
    }

    /*Test case 4.23.2: Verify that portlets have an x icon in the top right and a settings icon in the top left*/
    @Test
    public void portletsHavean_X_andSettingsIcon4_23_2() throws PopUpException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();

        //'X'icon and settings icon for Business objects portlet
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_BUSINESS_OBJECTS_PORTLET));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON,
                selenium.isElementPresent(SETTING_ICON_BUSINESS_OBJECTS_PORTLET));

        //'X'icon and settings icon for Core network KPI
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_CORE_NETWORK_KPIS));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON, selenium.isElementPresent(SETTING_ICON_CORE_NETWORK_KPIS));

        //'X'icon and settings icon for Dashboard top terminals
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_DASHBOARD_TOP_TERMINALS));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON,
                selenium.isElementPresent(SETTING_ICON_DASHBOARD_TOP_TERMINALS));

        //'X'icon and settings icon for Datavolume analysis
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_DATAVOL_ANALYSIS));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON, selenium.isElementPresent(SETTING_ICON_DATAVOL_ANALYSIS));

        //'X'icon and settings icon for Homer roamer
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_HOMER_ROAMER));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON, selenium.isElementPresent(SETTING_ICON_HOMER_ROAMER));

        //'X'icon and settings icon for Radio network KPI
        assertTrue(DataIntegrityStringConstants.X_ICON, selenium.isElementPresent(X_ICON_RADIO_NETWORK_KPI));
        assertTrue(DataIntegrityStringConstants.SETTINGS_ICON, selenium.isElementPresent(SETTING_ICON_RADIO_NETWORK_KPI));

    }

    /* Test case 4.23.3: Verify that Portlets have a title that describes their content.*/
    @Test
    public void portletsHaveTitleThatDescribesTheirContent4_23_3() throws PopUpException {

        dashboardTab.openTab();
        waitForPageLoadingToComplete();

        //checking each portlet title
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Top 5 Impacted Inbound Roamers By Country')] "));
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Radio Network KPIs')] "));
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'ENIQ Reports')] "));
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Core Network KPIs')] "));
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Top 5 Impacted Terminals')] "));
        assertTrue("Portlet did not have right Title",
                selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]"
                        + "//table//td//div[contains(text(),'Data Usage')] "));
    }

    /* Test case 4.23.11: Upon clicking on the settings icon, a menu appears and that the menu is in line with the new Ericsson branding*/
    @Test
    public void clikingOnSettingsIconMenuAppears4_23_11() throws PopUpException, InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        selenium.click(SETTING_ICON_RADIO_NETWORK_KPI);
        Thread.sleep(5000);
        assertTrue(
                "Menu did not appeared",
                selenium.isElementPresent("//div[contains(@class,'x-window x-component')]//div[contains(@class,'x-window-bwrap')]"));
        selenium.click("//div[contains(@class,'x-window-bwrap')]//table//tbody//td[2][button='Cancel']/button");
        selenium.click(SETTING_ICON_CORE_NETWORK_KPIS);
        Thread.sleep(5000);
        assertTrue(
                "Menu did not appeared",
                selenium.isElementPresent("//div[contains(@class,'x-window x-component')]//div[contains(@class,'x-window-bwrap')]//div[contains(text(),'Core Network KPIs')]"));
        selenium.click("//div[contains(@class,'x-window-bwrap')]//table//tbody//td[2][button='Cancel']/button");

    }

    /*Testcase 4.23.12: The portlets has options that include Replace and Close Widget */
    @Test
    public void portletsHasReplaceAndClosewidgetOptions4_23_12() throws PopUpException, InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        Thread.sleep(10000);

        //options checked for Core network kpi
        assertTrue(
                DataIntegrityStringConstants.REPLACE_BUTTON,
                selenium.isElementPresent("//div[@id='CORE_NETWORK_KPIS']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[1]"));
        assertTrue(
                DataIntegrityStringConstants.CLOSEWIDGET_BUTTON,
                selenium.isElementPresent("//div[@id='CORE_NETWORK_KPIS']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[2]"));

        //options checked for Home Roamers
        assertTrue(
                DataIntegrityStringConstants.REPLACE_BUTTON,
                selenium.isElementPresent("//div[@id='HOMER_ROAMER']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[1]"));
        assertTrue(
                DataIntegrityStringConstants.CLOSEWIDGET_BUTTON,
                selenium.isElementPresent("//div[@id='HOMER_ROAMER']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[2]"));

        //options checked for Dashboard Top Terminals
        assertTrue(
                DataIntegrityStringConstants.REPLACE_BUTTON,
                selenium.isElementPresent("//div[@id='DASHBOARD_TOP_TERMINALS']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[1]"));
        assertTrue(
                DataIntegrityStringConstants.CLOSEWIDGET_BUTTON,
                selenium.isElementPresent("//div[@id='DASHBOARD_TOP_TERMINALS']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[2]"));

        //Options checked for Radio network kpi
        assertTrue(
                DataIntegrityStringConstants.REPLACE_BUTTON,
                selenium.isElementPresent("//div[@id='RADIO_NETWORK_KPI']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[1]"));
        assertTrue(
                DataIntegrityStringConstants.CLOSEWIDGET_BUTTON,
                selenium.isElementPresent("//div[@id='RADIO_NETWORK_KPI']//div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/div/button[2]"));

    }

    /* Test case 4.23.14: Verify that upon clicking the x icon that the portlet goes into a closed state.*/
    @Test
    public void portletClosesOnClicking_X_4_23_14() throws PopUpException, InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();

        selenium.click(X_ICON_BUSINESS_OBJECTS_PORTLET);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'BUSINESS_OBJECTS_PORTLET')]/div/div/div[1]/div/node()"));
        selenium.click(X_ICON_DATAVOL_ANALYSIS);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'DATAVOL_ANALYSIS')]/div/div/div[1]/div/node()"));

        selenium.click(X_ICON_DASHBOARD_TOP_TERMINALS);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]/div/div/div[1]/div/node()"));

        selenium.click(X_ICON_HOMER_ROAMER);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'HOMER_ROAMER')]/div/div/div[1]/div/node()"));

        selenium.click(X_ICON_RADIO_NETWORK_KPI);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'RADIO_NETWORK_KPI')]/div/div/div[1]/div/node()"));

        selenium.click(X_ICON_CORE_NETWORK_KPIS);
        Thread.sleep(5000);
        assertFalse(DataIntegrityStringConstants.PORTLET_CLOSED_STATE,
                selenium.isElementPresent("//div[contains(@id,'CORE_NETWORK_KPIS')]/div/div/div[1]/div/node()"));

    }

    /*Test case 4.24.2: verify that there is a "select input" comboBox in the top left */
    @Test
    public void selectInputComboBoxinTheTopLeft4_24_2() throws PopUpException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        assertTrue(DataIntegrityStringConstants.DASHBOARD_COMBOBOX_BUTTON,
                selenium.isElementPresent(DASHBOARD_COMBOBOX_BUTTON));
        logger.log(Level.INFO, Logs.DASHBOARD_COMBOBOX_BUTTON);

    }

    /*Test case 4.24.4: Verify that there is a date selection ComboBox in the top right.*/
    @Test
    public void dateSelectionComboBoxinTheTopRight4_24_4() {
        dashboardTab.openTab();
        assertTrue(DataIntegrityStringConstants.DASHBOARD_TIMESELECT_COMOBOBOX,
                selenium.isElementPresent(DASHBOARD_DATESELECT_COMOBOBOX));
        logger.log(Level.INFO, Logs.DASHBOARD_DATESELECT_COMOBOBOX);
    }

    /*Test case 4.24.5:Verify that clicking on the date ComboBox causes a calendar style dialog to pop out of the date ComboBox*/
    @Test
    public void dateComboBoxCausesaCalendarStyleDialogToPopOutifTheDateComoBox4_24_5() throws InterruptedException,
            PopUpException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        selenium.click("//table//td[contains(@class,'x-toolbar-right')]//div[contains(@class,'x-form-field-wrap  x-component')]//img");
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.DATE_DIALOG_COMBOBOX, selenium.isElementPresent(DATE_DIALOG_COMBOBOX));
    }

    /*Test case 4.32.4: Verify that clicking on the Business Objects Portlet launches the Business Objects Tab*/
    @Test
    public void businessObjectsPortletLaunchesTheBusinessObjectsTab4_32_4() throws PopUpException, InterruptedException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        selenium.click(BUSINESS_OBJECTS_PORTLET);
        waitForPageLoadingToComplete();
        assertTrue(DataIntegrityStringConstants.BUSINNESS_OBJECTS_TAB,
                selenium.isElementPresent("//li[contains(@id,'" + tabname + "')]/a[@class='x-tab-right']"));

    }

    /*Test case 4.32.6: The Report lists which populates in the left hand side of the dashboard - must have a scroll bar at the bottom
    for the complete visbility of the Report names & other details
    @Test
    public void leftHandSideofTheDashboardMustHaveaScrollBar4_32_6() throws PopUpException, InterruptedException{
        dashboardTab.openTab();
        Thread.sleep(5000);
        selenium.click(BUSINESS_OBJECTS_PORTLET);
        Thread.sleep(5000);
        
        selenium.click("//div[contains(@id,'BUSINESS_OBJECTS_TAB')]//div//table/tbody/tr/td//div[contains(text(),'UTRAN')]");
        Thread.sleep(5000);
        System.out.println("element present is " +  selenium.getText("//div[contains(@id,'BUSINESS_OBJECTS_TAB')]//div//table/tbody/tr/td//div[contains(text(),'RNC')]") );
        //assertTrue("element not present", selenium.isElementPresent("//div[contains(@id,'BUSINESS_OBJECTS_TAB')]//div//table/tbody/tr/td//div[contains(text(),'RNC')]"));
      
    }*/

    /*Test case 4.33.2: Verify that the left hand side Y-Axis is labeled "Failure Attempts (Millions)", for Radio Network.*/
    @Test
    public void leftHandSideYAxisisLabeledFailureAttempts4_33_2() throws InterruptedException {
        dashboardAnalysis();
        dashbboardSetDate();

        String FA = selenium
                .getAttribute("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//div[contains(@class,'highcharts-container')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='text'][1]//node()[name()='tspan']/@x");

        int failureAttempts = Integer.parseInt(FA);

        String IS = selenium
                .getAttribute("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//div[contains(@class,'highcharts-container')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='text'][2]//node()[name()='tspan']/@x");

        int impactedsubscriber = Integer.parseInt(IS);
        assertTrue("Left Hand Side Y-axis is not labeled with Failure Attempts",
                isFAValueGreaterThanIS(failureAttempts, impactedsubscriber));

    }

    /*Test case 4.33.3: Verify that the right hand side Y-Axis is labeled "Impacted Subscribers (Millions)", for Radio Network*/
    @Test
    public void rightHandSideYaxisisLabeledImpactedSubscribers4_33_3() throws InterruptedException {
        dashboardAnalysis();
        dashbboardSetDate();

        String FA = selenium
                .getAttribute("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//div[contains(@class,'highcharts-container')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='text'][1]//node()[name()='tspan']/@x");

        int failureAttempts = Integer.parseInt(FA);

        String IS = selenium
                .getAttribute("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//div[contains(@class,'highcharts-container')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='text'][2]//node()[name()='tspan']/@x");

        int impactedsubscriber = Integer.parseInt(IS);
        assertTrue("Right Hand Side Y-axis is not labeled with Impacted Subscribers",
                isISValueLesserThanFA(impactedsubscriber, failureAttempts));

    }

    /* Test case 4.33.7: Verify that the graph's legend is at the bottom of the portlet, clicking on the items in the legend toggle 
      * the corresponding item on and off, for Radio Network*/
    @Test
    public void clickingOnLegendToggleCorrespondingItemONandOFF4_33_7() throws InterruptedException, PopUpException {
         dashboardAnalysis();
         dashbboardSetDate();
        assertTrue(
                "Graph Legend is not at the Bottom of the Portlet",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[(@xmlns='http://www.w3.org/2000/svg')]"));
        correspondingFailureAttemptsONandOFF();
        correspondingImpactedSubscribersONandOFF();

    }

    /*Test case 4.33.10: Verify that there is a clear message displayed if there is no data for the graph, for Radio Network*/
    @Test
    public void clearMessageDisplayedIfThereIsNOData4_33_10() throws PopUpException, InterruptedException {
        dashboardAnalysis();
        Thread.sleep(5000);
        boolean isElementPresent = selenium.isElementPresent("//*[@id='highcharts-16']/svg/g[3]");
        //div[contains(@id,'highcharts-2')][contains(@class,'highcharts-container')]
        if (!isElementPresent) {
            assertTrue(
                    "No Data message is not vissible",
                    selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//div[2][contains(text(),'No Data')]"));
        }

    }

    /*Test case 4.33.15: The drilldown from the failure area of the chart is possible and this displays the Raw event information 
     * from the charts view with the new Ericsson Branding design, for Radio Network.*/

    @Test
    public void drillDownFromTheFailureAreaOfTheChartIsPossibleForRadioNetwork4_33_15() throws PopUpException, InterruptedException{
        //dashboardAnalysis();
        selenium.setSpeed("3000");
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        //dashbboardSetDate();
        Thread.sleep(30000);
        assertTrue("Bar is not presented", selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect'][1]"));
        selenium.mouseOver("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect'][1]");
        Thread.sleep(2000);
        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect'][1]");
        
        //selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect' and contains(@visibility,'visible')][1]");
        //selenium.mouseDown("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect' and contains(@visibility,'visible')][1]");
        //selenium.mouseUp("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect' and contains(@visibility,'visible')][1]");
        Thread.sleep(5000);
        //assertTrue(selenium.isElementPresent("//div[contains(@id,'selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS_GRID_FROM_DASHBOARD')]"));
      //div[contains(@class,'GIJC1R-BOB')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='g']
      //div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect' and contains(@visibility,'visible')]
        
      //div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]/node()[name()='rect' and contains(@visibility,'visible')][1]
        
        
        
    }

    /*Test case 4.33.17: The drilldown from the failure area of the chart is possible and this displays the Raw event information from the 
    charts view with the new Ericsson Branding design, for CORE (PS) NETWORK. 452*/

    /*@Test
    public void drillDownFromTheFailureAreaOfTheChartIsPossibleForCoreNetwork4_33_17() throws PopUpException, InterruptedException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        dashboardTab.openStartMenu(DashboardTab.StartMenu.START);  
        dashboardTab.SetSearchType(DashboardType.APN);
        dashboardTab.enterSearchValue(DataIntegrityConstants.DASHBBOARD_APN_VALUE, false);
        dashboardTab.enterSubmit(false);
        selenium.click("//div[contains(@class,'GIJC1R-BOB')]//node()[@class='highcharts-tracker']//node()[name()='rect']");
        Thread.sleep(5000);
        assertTrue(selenium.isElementPresent("//div[contains(@id,'selenium_tag_BaseWindow_TERMINAL_EVENT_ANALYSIS_GRID_FROM_DASHBOARD')]"));
    
    }
    */

    /*Test case 4.34.1:  Verify that the graph is in the "Top 5 Impacted Inbound Roamers by Country" portlet, for Radio Network.*/
    @Test
    public void graphisintheTop5ImpactedInbounRomersByCountry_4_34_1() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.BY_DEFAULT_RADIO_NETWORK_SELECTED, selenium.isElementPresent(RADIO_NETWORK));
        dashbboardSetDate();
        waitForPageLoadingToComplete();

        boolean isElementPresent = selenium
                .isElementPresent("//div[contains(@id,'HOMER_ROAMER')]//div[contains(@class,'highcharts-container')]");
        assertTrue("graph is not in the Top 5 Impacted Inbound Roamers by Country", isElementPresent);

    }

    /*Test case 4.34.2: Verify that the Y-Axis has the names of the countries of it, for Radio Network*/
    @Test
    public void y_AxishastheNamesoftheCountries_4_34_2() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.BY_DEFAULT_RADIO_NETWORK_SELECTED, selenium.isElementPresent(RADIO_NETWORK));
        dashbboardSetDate();
        waitForPageLoadingToComplete();

        List<String> countries = new ArrayList<String>();
        countries.add("China");
        countries.add("Bangladesh");
        countries.add("Malaysia");
        countries.add("Greece");

        String countryChina = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][1]//node()[name()='tspan']//node()");
        String countryBangladesh = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][2]//node()[name()='tspan']//node()");
        String countryMalaysia = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][3]//node()[name()='tspan']//node()");
        String countryGreece = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-axis'][1]/node()[name()='text'][4]//node()[name()='tspan']//node()");

        boolean isTrue = countries.get(0).equals(countryChina) && countries.get(1).equals(countryBangladesh)
                && countries.get(2).equals(countryMalaysia) && countries.get(3).equals(countryGreece);
        assertTrue("Y-Axis does not has the names of the countries of it, for Radio Network", isTrue);

    }

    /*Test case 4.34.3: Verify that the number of impacted inbound roamers is to the right of the bar, it's representing, for Radio Network
    */
    @Test
    public void numberofImpactedInboundRoamersistotheRightoftheBar_4_34_3() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.BY_DEFAULT_RADIO_NETWORK_SELECTED, selenium.isElementPresent(RADIO_NETWORK));
        dashbboardSetDate();
        waitForPageLoadingToComplete();

        //Number of impacted inbound roamers must be in the following Class if not, then they are not right of the bar
        String Class = "highcharts-data-labels";

        selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']"
                + "/node()[@class=" + Class + "]/node()[name()='text'][1]//node()[name()='tspan']//node()");
        selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']"
                + "/node()[@class=" + Class + "]/node()[name()='text'][2]//node()[name()='tspan']//node()");
        selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']"
                + "/node()[@class=" + Class + "]/node()[name()='text'][3]//node()[name()='tspan']//node()");
        selenium.isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]//node()[@xmlns='http://www.w3.org/2000/svg']"
                + "/node()[@class=" + Class + "]/node()[name()='text'][4]//node()[name()='tspan']//node()");

    }

    /*Test case 4.34.4: Verify that the graph has bars that show the top five countries , for Radio Network*/
    @Test
    public void graphShowstheTopFiveCountriesBars_4_34_4() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.BY_DEFAULT_RADIO_NETWORK_SELECTED, selenium.isElementPresent(RADIO_NETWORK));
        dashbboardSetDate();
        waitForPageLoadingToComplete();
        assertTrue(selenium
                .isElementPresent("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-series-group']"));
        String chinaValue = "9";
        String bangladeshValue = "6";
        String malaysiaValue = "4";
        String greeceValue = "1";

        String china = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][1]//node()[name()='tspan']//node()");

        String bangladesh = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][2]//node()[name()='tspan']//node()");

        String malaysia = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][3]//node()[name()='tspan']//node()");

        String greece = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][4]//node()[name()='tspan']//node()");

        boolean isTrue = chinaValue.equals(china) && bangladeshValue.equals(bangladesh)
                && malaysiaValue.equals(malaysia) && greeceValue.equals(greece);
        assertTrue("Graph did not show Top five Countries for Radio Network", isTrue);

    }

    /*Test case 4.34.5: Verify that the bars are sorted in descending order from top to bottom, for Radio Network*/
    @Test
    public void barsAreSortedinDescindingOrder_4_34_5() throws InterruptedException, PopUpException {
        dashboardTab.openTab();
        Thread.sleep(5000);
        assertTrue(DataIntegrityStringConstants.BY_DEFAULT_RADIO_NETWORK_SELECTED, selenium.isElementPresent(RADIO_NETWORK));
        dashbboardSetDate();
        waitForPageLoadingToComplete();
        String china = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][1]//node()[name()='tspan']//node()");

        String bangladesh = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][2]//node()[name()='tspan']//node()");

        String malaysia = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][3]//node()[name()='tspan']//node()");

        String greece = selenium
                .getText("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'highcharts-0')]"
                        + "//node()[@xmlns='http://www.w3.org/2000/svg']/node()[@class='highcharts-data-labels']/node()[name()='text'][4]//node()[name()='tspan']//node()");

        int[] arrVals = new int[] { Integer.parseInt(china), Integer.parseInt(bangladesh), Integer.parseInt(malaysia),
                Integer.parseInt(greece) };

        assertTrue("Not in descending order", isDescendingOrder(arrVals));

    }

    /*@Test
    public void drillDownFromTheFailureAreaOfTheChartIsPossibleForRadioNetwork4_33_15() throws PopUpException, InterruptedException{
       
        dashboardTab.openAPNportlets(DashboardType.APN, false, DataIntegrityConstants.APN_SEARCH_VALUE);
        waitForPageLoadingToComplete();
        Thread.sleep(10000);
        
    }*/

    /*Test case 4.3.8: Verify that if user reorganizes the portlets on the dashboard, it should persist that settings and it should display that portlets in the same order if user revisit dashboard.*/
    @Test
    public void reorganizesThePortletOnTheDashboardShouldPersistThatSettingsInTheSameOrdetOnRevisiting_4_3_8() throws InterruptedException{
        selenium.setSpeed("2000");
        dashboardTab.openTab();
        Thread.sleep(3000);
        
        
        selenium.dragAndDropToObject("//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//table//td//div[contains(text(),'ENIQ Reports')]", 
                "//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//table//td//div[contains(text(),'Top 5 Impacted Inbound Roamers By Country')] ");
        //selenium.close();
        
        /*selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath(), "true");
        selenium.waitForPageToLoad("30000");
        dashboardLogin.logIn();
         if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
            assertFalse("Error during loading of Landing Page", selenium.getText(
                    "//span[@class='x-window-header-text']").equals("Error"));
            
            //dashboardTab.openTab();
            //Thread.sleep(3000);*/
            
            
            
            
            
        }
        
    
    
    
    /*Test case 4.1.1: Verify that the Group Management functionality is available for all users under the Options Menu in the EE UI*/
    @Test
    public void groupManagementFunctionalityUndertheOptionsMenu() throws InterruptedException, PopUpException{
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        selenium.click(OPTIONS_MENU_OPEN);
        assertTrue("Group Management functionality is not available for Dashboardtab", selenium.isElementPresent(GROUP_MANAGEMENT));
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    //******************************************* PRIVATE METHODS ************************************************//

    private void nodeTypes() {
        final String startStr = "//a[@id='";
        final String endStr = "']";
        final List<String> nodeTypes = new ArrayList<String>(Arrays.asList("APN", "BSC", "CELL", "SGSN", "MSC"));
        for (String string : nodeTypes) {
            assertTrue(DataIntegrityStringConstants.NODE_TYPES, selenium.isElementPresent(startStr + string + endStr));
        }

    }

    private void groupNodeTypes() {
        final String startStr = "//a[@id='";
        final String endStr = "']";
        final List<String> groupNodeTypes = new ArrayList<String>(Arrays.asList("APNGroup", "BSCGroup", "CELLGroup",
                "SGSNGroup", "MSCGroup"));
        for (String string : groupNodeTypes) {
            assertTrue(DataIntegrityStringConstants.GROUP_NODE_TYPES, selenium.isElementPresent(startStr + string + endStr));
        }

    }

    private void subTasksSlectedFromMenuBar() {
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.APN);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.CONTROLLER);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.ACCESS_AREA);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.SGSN_MME);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.MSC);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.APN_GROUP);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.CONTROLLER_GROUP);
        dashboardTab.setSearchType(DashboardType.ACCESS_AREA_GROUP);
        dashboardTab.setSearchType(DashboardType.SGSN_MME_GROUP);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.MSC_GROUP);
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);

    }

    private void defaultPortlets() {
        final String portlets = "//table[contains(@class, 'portlets-column dragdrop-dropTarget')]//div[contains(@id,'";
        final List<String> defaultPortlets = new ArrayList<String>(
                Arrays.asList("BUSINESS_OBJECTS_PORTLET", "DATAVOL_ANALYSIS", "DASHBOARD_TOP_TERMINALS",
                        "HOMER_ROAMER", "RADIO_NETWORK_KPI", "CORE_NETWORK_KPIS"));
        for (String string : defaultPortlets) {
            assertTrue(DataIntegrityStringConstants.DEFAULT_PORTLETS, selenium.isElementPresent(portlets + string + "')]"));

        }

    }

    private void clickOnX() {
        final String X = "//table[contains(@class,'portlets-column dragdrop-dropTarget')]//div[contains(@id,'";
        final List<String> portletsX = new ArrayList<String>(
                Arrays.asList("BUSINESS_OBJECTS_PORTLET", "DATAVOL_ANALYSIS", "DASHBOARD_TOP_TERMINALS",
                        "HOMER_ROAMER", "RADIO_NETWORK_KPI", "CORE_NETWORK_KPIS"));
        for (String string : portletsX) {
            selenium.click(X + string + "')]//table/tbody/tr/td[3]/div/img[contains(@class,'gwt-Image')]");

        }

    }

    private boolean isFAValueGreaterThanIS(int FA, int IS) {
        return FA < IS;
    }

    private boolean isISValueLesserThanFA(int IS, int FA) {
        return IS > FA;
    }

    private void dashbboardSetDate() throws InterruptedException {
        selenium.click("//td/table/tbody/tr/td/div/img");
        selenium.click("//tr[2]/td[4]/a/span");
        Thread.sleep(5000);
    }

    private void dashboardAnalysis() throws InterruptedException {
        dashboardTab.openTab();
        dashboardTab.openStartMenu(DashboardTab.startMenu.START);
        dashboardTab.setSearchType(DashboardType.APN);
        dashboardTab.enterSearchValue(DataIntegrityStringConstants.DASHBBOARD_APN_VALUE, false);
        dashboardTab.enterSubmit(false);
        Thread.sleep(5000);

    }

    private void correspondingFailureAttemptsONandOFF() throws InterruptedException {
        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='g']//node()[contains(text(),'Failure Attempts')]");
        Thread.sleep(3000);
        assertFalse(
                "Failure Attempts Graph is vissible",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]"
                        + "/node()[name()='rect' and contains(@visibility,'visible')]"));

        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='g']//node()[contains(text(),'Failure Attempts')]");
        Thread.sleep(3000);
        assertTrue(
                "Failure Attempts Graph is not vissible",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-tracker')]"
                        + "/node()[name()='rect' and contains(@visibility,'visible')]"));

    }

    private void correspondingImpactedSubscribersONandOFF() throws InterruptedException {
        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='g']//node()[contains(text(),'Impacted Subscribers')]");
        Thread.sleep(3000);
        assertTrue(
                "Impacted Subscribers is vissible",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-series')][@Visibility='hidden']"));

        selenium.click("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[@xmlns='http://www.w3.org/2000/svg']/node()[name()='g']//node()[contains(text(),'Impacted Subscribers')]");
        Thread.sleep(3000);
        assertFalse(
                "Impacted Subscribers is Not vissible",
                selenium.isElementPresent("//div[contains(@id,'DASHBOARD_TOP_TERMINALS')]//node()[contains(@class,'highcharts-series')][@Visibility='hidden']"));

    }

    public static boolean isDescendingOrder(int[] arr) {
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] < arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

}
