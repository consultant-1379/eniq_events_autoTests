/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.exception;

import com.ericsson.eniq.events.ui.selenium.core.Constants;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class NoDataException extends Exception {

    final String message;

    public NoDataException(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return Constants.WARNING_NO_DATA + " " + message;
    }
}
