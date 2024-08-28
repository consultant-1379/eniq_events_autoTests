/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.login;

/**
 * @author ericker
 * @since 2010
 *
 */

public enum UserDetails {
    FIRST_NAME("First_Name"), LAST_NAME("Last_Name"), LOGIN_NAME("Login_Name"), PASSWORD("Password"), CONFIRM("Confirm"), EMAIL(
            "Email"), PHONE("Phone"), ORGANIZATION("Organization");

    private String fieldName;

    UserDetails(final String field) {
        fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }
}
