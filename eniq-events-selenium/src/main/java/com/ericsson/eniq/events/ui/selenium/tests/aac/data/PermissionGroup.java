/**
*     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
* -----------------------------------------------------------------------
*/
package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

import org.springframework.stereotype.Component;

/**
 * @author ejenkav
 * @since 2011
 *
 */
@Component
public class PermissionGroup {

    public PermissionGroup() {
    }

    public PermissionGroup(final String name, final String title, final String description, final String permissions) {
        this.name = name;
        this.title = title;
        this.description = description;
        setPermissions(permissions);
    }

    @Override
    public String toString() {
        return "Name=" + name + " Title=" + title + " Description=" + description + " Permissions="
                + getpermissionsAsString();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String[] getPermissionSet() {
        return permissions;
    }

    public String getpermissionsAsString() {
        if (permissions == null)
            return null;
        String permissionsString = "";
        boolean firstElement = true;
        for (final String permissionElement : permissions) {
            if (firstElement) {
                permissionsString = permissionsString + permissionElement;
                firstElement = false;
                continue;
            }
            permissionsString = permissionsString + "," + permissionElement;

        }
        return permissionsString;
    }

    public void setPermissions(final String permissions) {
        this.permissions = permissions.split(",");
    }

    private String name;

    private String title;

    private String description;

    private String[] permissions;

    public enum Permission {
        NETWORK("eventsui.network.view"), RANKING("eventsui.ranking.view"), SUBSCRIBER("eventsui.subscriber.view"), TERMINAL(
                "eventsui.terminal.view");

        private String permissionName;

        private Permission(final String name) {
            this.permissionName = name;
        }

        public String getPermissionName() {
            return permissionName;
        }

        public static String getEniqXPath(final String permissionName) {

            if (NETWORK.getPermissionName().equalsIgnoreCase(permissionName)) {
                return "//li[@id='x-auto-1__NETWORK_TAB']";
            }
            if (TERMINAL.getPermissionName().equalsIgnoreCase(permissionName)) {
                return "//li[@id='x-auto-1__TERMINAL_TAB']";
            }
            if (SUBSCRIBER.getPermissionName().equalsIgnoreCase(permissionName)) {
                return "//li[@id='x-auto-1__SUBSCRIBER_TAB']";
            }
            return "//li[@id='x-auto-1__RANKINGS_TAB']";
        }
    };

}
