/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.windows;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public enum SelectedButtonType {
    // Back button
    BACK_BUTTON("btnBack"),
    // Forward button
    FORWARD_BUTTON("btnForward"),
    // Time Contraints button
    TIME_CONSTRAINS_BUTTON("btnTime"),
    // Toggle button
    TOGGLE_BUTTON("btnToggleToGrid"),
    // View button
    VIEW_BUTTON("btnView"),
    // KPI button
    KPI_BUTTON("btnKPI"), KPI_BUTTON_MSS("btnKPI_CS"),
    // property button 
    PROPERTY_BUTTON("btnProperties"),
    // Failure Recurrence button
    FAILURE_RECURRENCE_BUTTON("btnRecur"),
    // View subscriber details button
    VIEW_SUBSCRIBER_DETAILS_BUTTON("btnSubscriberDetails_CS"),
    //Subscriber details button
    SUBSCRIBER_DETAILS_BUTTON("btnSubscriberDetails"),
    // TODO update button id below to use it. 
    // Hide or Show legend chart button
    HIDE_OR_SHOW_LEGEND_CHART_BUTTON(""),
    // Navigate visited window button
    NAVIGATE_VISITED_WINDOW_BUTTON(""),
    // Export CSV button
    EXPORT_CSV(""),
    // Refresh button
    REFRESH_BUTTON(""),
    // View list of all possible cause codes button
    VIEW_LIST_OF_ALL_POSSIBLE_CAUSE_CODES(""),
    // View list of all possible sub cause codes button
    VIEW_LIST_OF_ALL_POSSIBLE_SUB_CAUSE_CODES(""),
    // Cell info button
    CELL_INFO_BUTTON("btnSac"),
    // Launch button
    LAUNCH_BUTTON("");

    private final String id;

    SelectedButtonType(final String id) {
        this.id = id;
    }

    public String getXPath() {
        // a bit of hack for the time being
        if (this == VIEW_BUTTON || this == TOGGLE_BUTTON || this == KPI_BUTTON_MSS || this == VIEW_SUBSCRIBER_DETAILS_BUTTON) {
            return "//table[@id='" + id + "']//button";
        } else if (this == LAUNCH_BUTTON) {
            return "//button[text()='Launch']";
        }
        return "//div[@id='" + id + "']//input";
    }

    public String getId() {
        return id;
    }
}
