package com.ericsson.eniq.events.ui.selenium.events.groupmanagement;

public abstract class GroupManagementConstants {
    public static final String GROUP_MANAGEMENT_WINDOW_ID = "GROUP_MANAGEMENT_WINDOW";

    public static final String ITEMS_PATH = "//*[@id='selenium_tag_F_PANEL_GROUP_MGT_VIEW']/div[3]/div[2]/div/div/div/div[1]/div";

    public static final String GROUP_MANAGEMENT = "//div[@class='popupContent']//div[text()='Group Management']";

    public static final String IMPORT_GROUPS = "//div[@class='popupContent']//div[text()='Import Groups']";

    public static final String DELETE_GROUPS = "//div[@class='popupContent']//div[text()='Delete Groups']";

    public static final String OPTIONS_MENU = "//div[@id='headerPnl']/table/tbody/tr/td[3]/div[text()='Options']";

    public static final String TRIGGER_BUTTON = "//div[@id='selenium_tag_GROUP_MANAGEMENT_WINDOW']//tbody/tr/td[2]/div[@class='GFMUOL5DMU']";

    public static final String CLOSE_BUTTON = "//button[text()='Dismiss']";

    public static final String DISMISS_BUTTON = "//button[text()='Dismiss']";

    public static final String NEW_BUTTON = "//button[text()='New']";

    public static final String EDIT_BUTTON = "//button[text()='Edit']";

    public static final String DELETE_BUTTON = "//button[text()='Delete Selected']";

    public static final String OK_BUTTON = "//button[text()='Ok']";

    public static final String ADD_BUTTON = "//button[text()='Add']";

    public static final String SAVE_BUTTON = "//button[text()='Save']";

    public static final String DELETE_SELECTED = "//button[text()='Delete Selected']";

    public static final String CANCEL_BUTTON = "//button[text()='Cancel']";

    // Top Failing Group Names
    public static final String topFailingAccessAreasGroupName = "TopFailingAccessArea";
    public static final String topFailingApnsGroupName = "TopFailingApns";
    public static final String topFailingControllersGroupName = "TopFailingControllers";
    public static final String topFailing2gControllersGroupName = "TopFailingBSCs";
    public static final String topFailing3gControllersGroupName = "TopFailingRNCs";
    public static final String topFailing4gControllersGroupName = "TopFailingENodeBs";
    public static final String topFailingImsisGroupName = "TopFailingImsis";
    public static final String topFailingMscsGroupName = "TopFailingMscs";
    public static final String topFailingPlmnsGroupName = "TopFailingPlmns";
    public static final String topFailingSgsnMmesGroupName = "TopFailingSgsnMmes";
    public static final String topFailingTerminalGroupName = "TopFailingTerminals";
    public static final String topFailingTrackingAreaGroupName = "TopFailingTrackingAreas";
}
