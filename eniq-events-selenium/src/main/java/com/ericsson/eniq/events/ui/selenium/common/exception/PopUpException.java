/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.exception;

/**
 * @author eseuhon
 * @since 2011
 *
 * This user defined exception is used to represent various error types in pop up window 
 * which is retrieved by a server side. 
 */
public class PopUpException extends Exception {

    String errorMessage;

    public PopUpException(final String message) {
        errorMessage = message;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
