package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

public class Role {

    public Role() {
    }

    public Role(final String name, final String title, final String description, final String permissionGroupNames) {
        this.name = name;
        this.title = title;
        this.description = description;
        setPermissionGroups(permissionGroupNames);
    }

    @Override
    public String toString() {
        return "Name=" + name + " Title=" + title + " Description=" + description + " PermissionGroups="
                + getpermissionGroupsAsString();
    }

    public void setPermissionGroups(final String permissionGroupNames) {
        this.permissionGroups = permissionGroupNames.split(",");
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

    public String[] getpermissionGroupSet() {
        return permissionGroups;
    }

    public String getpermissionGroupsAsString() {
        if (permissionGroups == null)
            return null;
        String permissionGroupsString = "";
        boolean firstElement = true;
        for (final String permissionGroupElement : permissionGroups) {
            if (firstElement) {
                permissionGroupsString = permissionGroupsString + permissionGroupElement;
                firstElement = false;
                continue;
            }
            permissionGroupsString = permissionGroupsString + "," + permissionGroupElement;

        }
        return permissionGroupsString;
    }

    private String name;

    private String title;

    private String description;

    private String[] permissionGroups;

}
