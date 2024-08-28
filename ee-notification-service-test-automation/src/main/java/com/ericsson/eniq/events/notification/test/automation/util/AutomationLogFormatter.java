/*
 * ---------------------------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * ---------------------------------------------------------------------------------------
 */

package com.ericsson.eniq.events.notification.test.automation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Kpi Log file formatter
 * Copy of Eniq Engine formatter.
 */
public class AutomationLogFormatter extends Formatter {
    /**
     * Default date format
     * Taken from Eniq Engine
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");

    private final Date date = new Date();

    /**
     * Formats one log entry.
     *
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(final LogRecord logRecord) {

        final StringBuilder res = new StringBuilder();

        /*res.append(sdf.format(date));
        res.append(",");
        res.append(sdf.format(new Date(logRecord.getMillis())));
        res.append(",");*/
        res.append(logRecord.getMessage());
        res.append("\n");

        Throwable t = logRecord.getThrown();
        int inten = 3;

        while (t != null) {
            appendException(t, inten, res);
            inten += 3;

            t = t.getCause();
        }

        return res.toString();
    }

    /**
     * Convert an exception to a String
     *
     * @param throwable The exception
     * @param inten     Indentation of stack trace
     * @param res       String buffer to append stack too
     */
    private void appendException(final Throwable throwable, final int inten, final StringBuilder res) {
        if (throwable != null) {
            indent(res, inten);
            res.append(throwable.getClass().getName());
            res.append(": ");
            res.append(throwable.getMessage());
            res.append("\n");
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (final StackTraceElement aSte : stackTrace) {
                indent(res, inten + 5);
                res.append(aSte.getClassName());
                res.append(".");
                res.append(aSte.getMethodName());
                res.append("(");
                if (aSte.getFileName() == null) {
                    res.append("Unknown Source");
                } else {
                    res.append(aSte.getFileName());
                    res.append(":");
                    res.append(aSte.getLineNumber());
                }
                res.append(")");
                res.append("\n");
            }
        }
    }

    /**
     * Append a tab
     *
     * @param sBuilder   String to append the tab on too
     * @param indentSize The tab size
     */
    private void indent(final StringBuilder sBuilder, final int indentSize) {
        for (int i = 0; i < indentSize + 5; i++) {
            sBuilder.append(" ");
        }
    }
}
