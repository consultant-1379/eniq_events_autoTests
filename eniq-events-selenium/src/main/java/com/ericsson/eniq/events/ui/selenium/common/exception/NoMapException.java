/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.exception;

import com.ericsson.eniq.events.ui.selenium.core.Constants;

/**
 * @author eeikbe
 * @since 10/2013
 */
public class NoMapException extends Exception{
    final String message;
    
    public NoMapException(final String message){
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(Constants.ERROR_NO_MAP);
        stringBuilder.append(" - ");
        stringBuilder.append(this.message);
        return stringBuilder.toString();
    }
}
