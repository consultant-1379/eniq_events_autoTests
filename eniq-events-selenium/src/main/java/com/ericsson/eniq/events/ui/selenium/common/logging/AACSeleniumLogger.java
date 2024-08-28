/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.logging;

import com.ericsson.eniq.events.ui.selenium.tests.aac.data.ReportGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.*;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class AACSeleniumLogger {
    static FileHandler fileHandler;

    static Logger logger = Logger.getLogger(AACSeleniumLogger.class.getName());

    public static void writeHeader(final String message) {
        fileHandler.setFormatter(AACHeaderFormatter.getInstance());
        logger.log(Level.INFO, message);
        fileHandler.setFormatter(AACMessageFormatter.getInstance());
    }

    public static void setUp() {
        //Create logger and set level

        logger.setLevel(Level.INFO);

        //Create file handler with a file
        try {
            fileHandler = new FileHandler(ReportGenerator.getLogFileAbsolutePath(), true);
        } catch (final SecurityException e) {
            System.out.println("Cannot create Log file " + ReportGenerator.getLogFileAbsolutePath());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println("Cannot create Log file " + ReportGenerator.getLogFileAbsolutePath());
            e.printStackTrace();
        }

        //set file format in file handler
        fileHandler.setFormatter(AACMessageFormatter.getInstance());
        //add handler to logger
        logger.addHandler(fileHandler);
    }

    public static void tearDown() {
        logger.removeHandler(fileHandler);
        fileHandler.close();
    }

}

class AACMessageFormatter extends Formatter {

    private static final AACMessageFormatter instanceVar = new AACMessageFormatter();

    public static AACMessageFormatter getInstance() {
        return instanceVar;
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        final StringBuilder sb = new StringBuilder();

        sb.append(calcDate(record.getMillis())).append(" ").append(record.getLevel().getLocalizedName()).append(": ")
                .append(formatMessage(record)).append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            try {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (final Exception ex) {
                // ignore
            }
        }

        return sb.toString();
    }

    private String calcDate(final long millisecs) {
        final Date resultdate = new Date(millisecs);
        return ReportGenerator.date_format.format(resultdate);
    }
}

class AACHeaderFormatter extends Formatter {
    private static final AACHeaderFormatter instanceVar = new AACHeaderFormatter();

    public static AACHeaderFormatter getInstance() {
        return instanceVar;
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        return formatMessage(record) + LINE_SEPARATOR;
    }

}
