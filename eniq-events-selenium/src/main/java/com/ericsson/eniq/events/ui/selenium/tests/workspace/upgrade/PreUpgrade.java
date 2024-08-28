package com.ericsson.eniq.events.ui.selenium.tests.workspace.upgrade;

import java.util.logging.Level;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.events.groupmanagement.*;
import com.ericsson.eniq.events.ui.selenium.events.login.AdminLogin;
import com.ericsson.eniq.events.ui.selenium.events.login.FourGCoreUser;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.aac.data.UIConstants;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites.GroupCreationUtils;
import com.thoughtworks.selenium.SeleniumException;

public class PreUpgrade extends BaseSeleniumTest {
    private static boolean init = false;

    private AdminLogin adminLogin = new AdminLogin();
    private String adminUser = PropertyReader.getInstance().getAdminUser();
    private String adminPass = PropertyReader.getInstance().getAdminPwd();

    @Autowired
    private FourGCoreUser fourGCoreUser;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    private GroupManagementOperations groupManagementOperations;

    @Autowired
    private GroupManagementWindow groupManagementWindow;

    @Autowired
    private GroupCreationUtils groupCreationUtils;

    @Autowired
    private EricssonSelenium selenium;

    @Autowired
    @Qualifier("termRankings")
    private CommonWindow terminalRankingWindow;

    @Autowired
    @Qualifier("accessAreaRankings")
    private CommonWindow accessAreaRankingWindow;

    @Autowired
    @Qualifier("ENodeBRanking")
    private CommonWindow eNodeBRankingWindow;

    @Autowired
    @Qualifier("subRankings")
    private CommonWindow subscriberRankingWindow;

    @Autowired
    private GroupManagementEditWindow groupManagementEditWindow;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of Core(PS) Pre Upgrade Tests");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of Core(PS) Post Upgrade Tests");
    }

    @Override
    @Before
    public void setUp() throws InterruptedException {
        if (init == false) {

            init = true;

            createFourGCoreUser();

            if (selenium != null) {
                selenium.close();
                selenium.stop();
            }
        }
    }

    @After
    @Override
    public void tearDown() {
        if (selenium != null) {
            selenium.close();
            selenium.stop();
            selenium = null;
        }
    }

    @Test
    public void test_fourGCoreUserWasCreated() throws InterruptedException {
        try {
            loginToAdminUI();
            validateUserWasCreated();
        } finally {
            logoutOfAdminUI();
        }
    }

    @Test
    public void test_createSavedWorkspace() throws InterruptedException, PopUpException {
        try {
            loginToEniqEventsUI();

            openSideBar();
            openCauseCodeRankingWindow();

            openSideBar();
            openBSCRankingWindow();

            openSideBar();
            openeNodeBRankingWindow();

            openSideBar();
            openRNCRankingWindow();

            openSideBar();
            openTerminalRankingWindow();

            openSideBar();
            openSubscriberRankingWindow();

            openSideBar();
            openAPNRankingWindow();

            openSideBar();
            openAccessAreaRankingWindow();

            workspace.saveWorkspaceAs(FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE);
            validateWorkspaceWasSaved();
        } finally {
            fourGCoreUser.logOut();
            //Dont want to save an empty workspace
            if (selenium.isElementPresent("//button[text()='Ok']")) {
                selenium.click("//button[text()='Ok']");
            }
        }
    }

    @Test
    public void test_createApnGroupAndVerifyCreated() throws InterruptedException, NoDataException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);

            GroupCreationUtils.makeApnGroupFromTopFailingApns(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    FourGUpgradeConstants.APN_GROUP_NAME);

            //Verify Apn group created
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.APN_GROUP_NAME, GuiStringConstants.APN, groupManagementWindow));
        } finally {
            fourGCoreUser.logOut();
        }
    }

    @Test
    public void test_createTacGroupAndVerifyCreated() throws InterruptedException, NoDataException, PopUpException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
            pause(1000);
            GroupCreationUtils.makeTacGroupFromTopFailingTacs(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    FourGUpgradeConstants.TAC_GROUP_NAME, terminalRankingWindow);
            pause(1000);

            //Test that tac group is present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.TAC_GROUP_NAME, GuiStringConstants.TERMINAL,
                    groupManagementWindow));

        } finally {
            fourGCoreUser.logOut();
        }
    }

    @Test
    public void test_createAccessAreaGroupAndVerifyCreated() throws InterruptedException, NoDataException, PopUpException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
            pause(1000);
            GroupCreationUtils.make4GAccessAreaGroupFromTopFailingAccessAreas(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    FourGUpgradeConstants.ACCESS_AREA_GROUP_NAME, accessAreaRankingWindow);
            pause(1000);

            //Test that Access Area group is present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.ACCESS_AREA_GROUP_NAME, GuiStringConstants.ACCESS_AREA,
                    groupManagementWindow));

        } finally {
            fourGCoreUser.logOut();
        }
    }

    @Test
    public void test_createTrackingAreaGroupAndVerifyCreated() throws InterruptedException, PopUpException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            GroupCreationUtils.makeTrackingAreaGroupFromLiveLoad(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    FourGUpgradeConstants.TRACKING_AREA_GROUP_NAME);

            //Test that tracking area group is present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.TRACKING_AREA_GROUP_NAME, GuiStringConstants.TRACKING_AREA,
                    groupManagementWindow));
        } finally {
            fourGCoreUser.logOut();
        }
    }

    @Test
    public void test_create4GControllerGroupAndVerifyCreated() throws InterruptedException, NoDataException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            GroupCreationUtils.make4GControllerGroup(workspace, groupManagementWindow, groupManagementEditWindow, selenium, eNodeBRankingWindow,
                    FourGUpgradeConstants.CONTROLLER_GROUP_NAME);

            //Test that Controller group is present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.CONTROLLER_GROUP_NAME, GuiStringConstants.CONTROLLER,
                    groupManagementWindow));
        } finally {
            fourGCoreUser.logOut();
        }
    }

    @Test
    public void test_createImsiGroupAndVerifyCreated() throws InterruptedException, NoDataException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            GroupCreationUtils.makeImsiGroupFromTopFailingImsis(workspace, groupManagementWindow, groupManagementEditWindow, subscriberRankingWindow,
                    selenium, FourGUpgradeConstants.IMSI_GROUP_NAME);

            //Test that Imsi Group is Present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.IMSI_GROUP_NAME, GuiStringConstants.IMSI,
                    groupManagementWindow));
        } finally {
            fourGCoreUser.logOut();

        }
    }

    @Test
    public void test_createSgsnGroupAndVerifyCreated() throws InterruptedException {
        try {
            loginToEniqEventsUI();
            pause(1000);
            workspace.checkAndOpenSideLaunchBar();
            pause(1000);
            GroupCreationUtils.makeSgsnGroupFromTopFailingSgsns(workspace, groupManagementWindow, groupManagementEditWindow, selenium,
                    FourGUpgradeConstants.SGSN_GROUP_NAME);

            //Test that SGSN Group is Present
            assertTrue(GroupManagementOperations.isExistingGroup(FourGUpgradeConstants.SGSN_GROUP_NAME, GuiStringConstants.SGSN__MME,
                    groupManagementWindow));
        } finally {
            fourGCoreUser.logOut();
        }
    }

    private void openBSCRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.BSC);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openRNCRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.RNC);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openeNodeBRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.eNodeB);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openTerminalRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.TERMINAL);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openCauseCodeRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.CORE_CAUSE_CODE_RANKING, GuiStringConstants.LMAW_CORE_PS);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openSubscriberRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.SUBSCRIBER);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openAPNRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.APN);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void openAccessAreaRankingWindow() throws InterruptedException, PopUpException {
        workspace.selectDimension(SeleniumConstants.CORE_NETWORK_PS);
        workspace.selectWindowType(GuiStringConstants.LMAW_CORE_RANKING, GuiStringConstants.ACCESS_AREA);
        workspace.clickLaunchButton();
        waitForPageLoadingToComplete();
    }

    private void validateWorkspaceWasSaved() {
        openSideBar();
        selenium.click(SeleniumConstants.WORKSPACES_SIDE_BAR_TAB_XPATH);
        if (!selenium.isElementPresent("//div[text()='" + FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE + "']")) {
            fail(FourGUpgradeConstants.FOUR_G_CORE_WORKSPACE + " was not saved.");
        }
    }

    private void openSideBar() {
        pause(2000);
        workspace.checkAndOpenSideLaunchBar();
        pause(2000);
        workspace.selectTimeRange(SeleniumConstants.DATE_TIME_Week);
        pause(2000);
    }

    private void createFourGCoreUser() throws InterruptedException {
        try {
            loginToAdminUI();
            if (!isUserCreated()) {
                navigateToAddUserPage();
                addUser();
            }
        } finally {
            logoutOfAdminUI();
        }
    }

    private void loginToEniqEventsUI() {
        selenium.start("captureNetworkTraffic=true");
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(PropertyReader.getInstance().getEventHost() + ":" + PropertyReader.getInstance().getEventPort()
                + PropertyReader.getInstance().getPath(), "true");
        selenium.waitForPageToLoad("30000");
        fourGCoreUser.logIn();
        if (selenium.isElementPresent("//span[@class='x-window-header-text']")) {
            assertFalse("Error during loading of Landing Page", selenium.getText("//span[@class='x-window-header-text']").equals("Error"));
        }
        selenium.waitForElementToBePresent("//*[contains(@id,'headerPnl')]//div[contains(text(),'" + UIConstants.ENIQ_LOGGED_IN_MSG + "')]", "30000");
        selenium.waitForElementToBePresent("//*[contains(@role,'tablist') and contains(@class,'tab')]", "30000");
    }

    private void loginToAdminUI() throws InterruptedException {
        selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        selenium.open(adminLogin.getHost() + ":" + adminLogin.getPort() + adminLogin.getPath() + "servlet/LoaderStatusServlet");
        selenium.type("id=username", adminUser);
        selenium.type("id=password", adminPass);
        selenium.click("id=submit");
        selenium.waitForPageToLoad("60000");
        Thread.sleep(30000);
        if (!selenium.isTextPresent("System Status")) {
            Thread.sleep(600000);
        }
    }

    private void navigateToAddUserPage() {
        selenium.click(FourGUpgradeConstants.USER_MANAGEMENT_HYPERLINK_XPATH);
        selenium.waitForPageToLoad("60000");
        selenium.click(FourGUpgradeConstants.ADD_BUTTON_XPATH);
        selenium.waitForPageToLoad("60000");
    }

    private void addUser() throws InterruptedException {
        selenium.type(FourGUpgradeConstants.USER_ID_INPUT_XPATH, SeleniumConstants.FOUR_G_CORE_USER_ID);
        selenium.type(FourGUpgradeConstants.PASSWORD_INPUT_XPATH, SeleniumConstants.FOUR_G_CORE_USER_PASSWORD);
        selenium.type(FourGUpgradeConstants.COMFIRM_PASSWORD_INPUT_XPATH, SeleniumConstants.FOUR_G_CORE_USER_PASSWORD);
        selenium.type(FourGUpgradeConstants.FIRST_NAME_INPUT_XPATH, FourGUpgradeConstants.FIRST_NAME);
        selenium.type(FourGUpgradeConstants.LAST_NAME_INPUT_XPATH, FourGUpgradeConstants.LAST_NAME);
        selenium.select("id=userRoles", "label=sysadmin");
        selenium.click(FourGUpgradeConstants.SAVE_BUTTON_XPATH);
        selenium.waitForPageToLoad("60000");
        Thread.sleep(30000);
    }

    private void validateUserWasCreated() {
        if (!isUserCreated()) {
            logoutOfAdminUI();
            fail(SeleniumConstants.FOUR_G_CORE_USER_ID + " user was not created.");
        }

    }

    private boolean isUserCreated() {
        selenium.click(FourGUpgradeConstants.USER_MANAGEMENT_HYPERLINK_XPATH);
        selenium.waitForPageToLoad("60000");

        int row = 1;
        while (true) {
            String userId = "";
            try {
                userId = selenium.getText("//tbody/tr[" + row + "]/td[@class='viewTableBorder'][1]");
            } catch (NullPointerException e) {
                return false;
            } catch (SeleniumException e) {
                return false;
            }

            if (userId.equals(SeleniumConstants.FOUR_G_CORE_USER_ID + "**")) {
                break;
            }

            row++;
        }

        return true;
    }

    private void logoutOfAdminUI() {
        selenium.click(FourGUpgradeConstants.ADMIN_UI_LOGOUT_HYPERLINK_XPATH);
    }

}
