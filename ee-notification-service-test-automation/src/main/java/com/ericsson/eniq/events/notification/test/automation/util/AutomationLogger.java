/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.notification.test.automation.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ericsson.eniq.events.notification.test.automation.sql.AutomateJDBC;



/**
 * @author ekrchai
 * @since 2012
 *
 */
public class AutomationLogger {

    protected static int fileLimit = 3;

    final static Logger nsLogger = Logger.getLogger("AutomationLogger");

    private static Handler logFileHandler = null;

    private static final int BYTES_IN_MB = 1048576;


    public AutomationLogger() {
    }

    /**
     * Default name for services in the DEFAULT_LOG_DIR dir
     */
    public static final String NOTIFICATION_SERVICE_DIR = "kpi_automation";

    /**
     * Default base log directory
     * Change the directory below to save the logs anf html reports
     * Ex: if the directory is /eniq/log/sw_log then it should be given like
     * File.separator + "eniq" + File.separator + "log" + File.separator +"sw_log"
     */

    public static final String defaultLogDir = AutomateJDBC.logsRootDir; 
    static {
        initializePropertiesAndLoggers();
    }

    public static void initializePropertiesAndLoggers() {
        resetHandlers();
    }

    public static void info(final Object... info) {
        nsLogger.log(Level.INFO, buildMessage(info));
    }

    public static void warn(final Object... info) {
        nsLogger.log(Level.WARNING, buildMessage(info));
    }

    public static String buildMessage(final Object... info) {

        final StringBuilder sb = new StringBuilder();
        sb.append(info[0]);
        for (int i = 1; i < info.length; i++) {
            sb.append(",");
            sb.append(info[i]);
        }

        return sb.toString();
    }

    static void resetHandlers() {

        for (final Handler handler : nsLogger.getHandlers()) {
            handler.close();
            nsLogger.removeHandler(handler);
        }

        try {
            final File dir = new File(getServicesLogDirectory());
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Logger.getLogger("").log(Level.SEVERE, "Failed to create the directory tree " + dir);
                }
            }

            final int rolloverLimit = 10 * BYTES_IN_MB; //lookupRolloverLimitPropertyInJNDI() * BYTES_IN_MB;

            logFileHandler = new FileHandler(dir + File.separator + NOTIFICATION_SERVICE_DIR  + ".log", rolloverLimit,
                    fileLimit, false);

            logFileHandler.setFormatter(new AutomationLogFormatter());
        } catch (final SecurityException e) {
            Logger.getLogger("").log(Level.SEVERE, "Failed to start Performance Trace Logger" + e.getStackTrace());
        } catch (final IOException e) {
            Logger.getLogger("").log(Level.SEVERE, "Failed to start Performance Trace Logger" + e.getStackTrace());
        }

        nsLogger.setUseParentHandlers(false);
        nsLogger.addHandler(logFileHandler);

    }

    /**
    * Get the log output directory
    * @return Log output dir
    */
    public static String getServicesLogDirectory() {
        final String baseLogDir = System.getProperty("LOG_DIR", defaultLogDir);
        return baseLogDir + File.separator + NOTIFICATION_SERVICE_DIR;

    }

    /**
     * Close any open log files.
     * Mainly used in tests.
     */
    public static void closeLogFiles() {
        if (logFileHandler != null) {
            logFileHandler.close();
        }
    }

    public static void setLevel(final Level logLevel) {
        nsLogger.setLevel(logLevel);
    }
    
  }
