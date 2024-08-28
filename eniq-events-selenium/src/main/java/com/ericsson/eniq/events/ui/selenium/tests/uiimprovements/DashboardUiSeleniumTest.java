package com.ericsson.eniq.events.ui.selenium.tests.uiimprovements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLoggerDuplicate;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.tabs.DashboardTab;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.DashboardUIBaseSeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Tests in this class cover last tests from <tt>"EE UI Improvs_Feature Test_XX X_X X X.xls"</tt>:<br>
 * * Test Group 3: T18: Persist user screen and layout settings - on MOZILLA FIREFOX<br>
 * * Test Group 6: T18: Persist user screen and layout settings - on GOOGLE CHROME<br>
 *
 * @author ealeerm - Alexey Ermykin
 * @see PersistUserScreenAndLayoutSettingsTestGroup
 * @since 05 2012
 */
@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnit4AnnotatedMethodInJUnit3TestCase"})
public class DashboardUiSeleniumTest extends DashboardUIBaseSeleniumTest {

    private static final int NUMBER_OF_RANDOM_PORTLET_DRAGS = 7;
    private static Logger log = Logger.getLogger(SeleniumLoggerDuplicate.class.getName());

    @Autowired
    protected DashboardTab dashboardTab;

    private Random random;

    /**
     * <b>Test cases subjects:</b><br>
     * * Verify that if user reorganizes the portlets on the dashboard, it should persist that settings and
     * it should display that portlets in the same order if user revisit dashboard.
     * <p/>
     * <p/>
     * <b>Jiras:</b> <a href="http://jira.eei.ericsson.se:8081/browse/EUI-1365">EUI-1365</a>
     * <p/>
     * <b>Test cases:</b><br>
     * * EE12.2_UI_Imp_3.7: Verify that if user reorganizes the portlets on the dashboard, it should
     * persist that settings and it should display that portlets in the same order if user revisits dashboard.<br>
     * * EE12.2_UI_Imp_6.7: Verify that if user reorganizes the portlets on the dashboard, it should
     * persist that settings and it should display that portlets in the same order if user revisits dashboard.<br>
     */
    @Test
    public void testPortletsReorganizationPersists_T18_3_7_and_6_7() throws PopUpException {
        dashboardTab.openTab();
        waitForPageLoadingToComplete();
        dashboardTab.waitAllPortletsToBePresent("17000");

        List<List<String>> initialHeadersLayoutList = dashboardTab.getPortletHeadersListOfList();
        log.info("Initial layout: " + initialHeadersLayoutList);
        try {
            random = new Random();

            for (int i = 0; i < NUMBER_OF_RANDOM_PORTLET_DRAGS; i++) {
                changeRandomlyAnyPortletPosition();
            }
            dashboardTab.waitAllPortletsToBePresent("12000");

            List<List<String>> portletHeadersListBeforeRelogin = dashboardTab.getPortletHeadersListOfList();
            log.info("Layout after changes: " + portletHeadersListBeforeRelogin);

            dashboardLogin.relogin();

            dashboardTab.openTab();
            waitForPageLoadingToComplete();

            List<List<String>> portletHeadersListAfterRelogin = dashboardTab.getPortletHeadersListOfList();
            assertEquals("Result portlet layout must persist after relogin",
                    String.valueOf(portletHeadersListBeforeRelogin),
                    String.valueOf(portletHeadersListAfterRelogin));
        } finally {
            dashboardTab.restorePortletsLayout(initialHeadersLayoutList);
            log.info("Restored layout : " + dashboardTab.getPortletHeadersListOfList());
            dashboardTab.waitAllPortletsToBePresent("18000");
        }
    }

    private void changeRandomlyAnyPortletPosition() {
        List<List<String>> portletHeadersListOfList = dashboardTab.getPortletHeadersListOfList();
        assertFalse("Portlet Headers List cannot be null or empty: check that you have portlets " +
                "[portletHeadersListOfList=" + portletHeadersListOfList + "]",
                portletHeadersListOfList == null || portletHeadersListOfList.isEmpty());

        assertFalse("More than one portlet must be in dashboard for this test [portletHeadersListOfList=" +
                portletHeadersListOfList + "]",
                portletHeadersListOfList.size() == 1 && portletHeadersListOfList.get(0).size() == 1);

        int portletColumnCount = dashboardTab.getPortletColumnCount();

        assertTrue("number of portlets columns must be positive: portletColumnCount=" + portletColumnCount,
                portletColumnCount > 0);

        final int ATTEMPTS_NUMBER = 10; // needed because some portlets can be unavailable due to loading os something like it
        LocatorsToDragHandler dragHelper = new LocatorsToDragHandler(portletColumnCount);
        int i = 0;
        for (; i < ATTEMPTS_NUMBER; i++) {
            if (dragHelper.calculateLocatorsRandomly()) {
                break;
            }
        }
        if (i > 0) {
            log.info((i + 1) + " of " + ATTEMPTS_NUMBER + " attempts to calculate locators were taken");
        }

        selenium.waitForElementToBePresent(dragHelper.getPortletToDrag(), "15000");
        selenium.waitForElementToBePresent(dragHelper.getPortletAreaDestination(), "15000");
        selenium.dragAndDropToObject(dragHelper.getPortletToDrag(), dragHelper.getPortletAreaDestination());
    }

    private class LocatorsToDragHandler {

        private final int portletColumnCount;
        private String portletToDrag;
        private String portletAreaDestination;

        public LocatorsToDragHandler(int portletColumnCount) {
            this.portletColumnCount = portletColumnCount;
        }

        public String getPortletToDrag() {
            return portletToDrag;
        }

        public String getPortletAreaDestination() {
            return portletAreaDestination;
        }

        /**
         * @return are locators found
         */
        public boolean calculateLocatorsRandomly() {
            int randColumnToDrag, randRowToDrag, randDestColumn;
            do {
                do {
                    randColumnToDrag = getRandColumn();
                    randRowToDrag = getRandRow(randColumnToDrag);
                } while (randColumnToDrag == -1 || randRowToDrag == -1); // if not defined
                randDestColumn = getRandColumn();
            } while (randDestColumn == randColumnToDrag && randRowToDrag == 1); // no real layout change if first
            // portlet is moved to the same column

            portletToDrag = dashboardTab.getPortletHeaderLocator(randColumnToDrag, randRowToDrag);
            assertTrue("Portlet to drag not found: portletToDrag=" + portletToDrag,
                    portletToDrag != null && !portletToDrag.isEmpty());

            portletAreaDestination = dashboardTab.getPortletDragDropColumnAreaLocator(randDestColumn);
            assertTrue("Destination portlet area not found: portletAreaDestination=" + portletAreaDestination,
                    portletAreaDestination != null && !portletAreaDestination.isEmpty());

            assertFalse("Destination portlet cannot be the same as draggable one: " +
                    "portletAreaDestination=" + portletAreaDestination + " portletToDrag=" + portletToDrag,
                    portletAreaDestination.equals(portletToDrag));
            if (selenium.isElementPresent(portletToDrag) && selenium.isElementPresent(portletAreaDestination)) {
                return true;
            } else {
                yield();
            }
            return false;
        }

        /**
         * @return starting from 1; -1 if portletColumnCount is 0
         */
        private int getRandColumn() {
            return portletColumnCount > 0 ? random.nextInt(portletColumnCount) + 1 : -1;
        }

        /**
         * @param column column index starting from 1
         * @return starting from 1; -1 if no portlets in this column
         */
        private int getRandRow(int column) {
            int portletRowsCount = dashboardTab.getPortletRowsCount(column);
            return portletRowsCount > 0 ? random.nextInt(portletRowsCount) + 1 : -1;
        }

        /**
         * Wait a little bit (e.g. for loading)
         */
        private void yield() {
            log.finest("yield was necessary");
            try {
                final EricssonSelenium seleniumLoc = selenium;
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (seleniumLoc) {
                    seleniumLoc.wait(250);
                }
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
}