/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.logging;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public class SeleniumLogger {
    static FileHandler fileHandler;

    static Logger logger;

    static String fileName = "";

    final static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	
    public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		SeleniumLogger.fileName = fileName;
	}

    final static DateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");

    final static Date date = new Date();

    public static void setUp() throws SecurityException, IOException {
        //Create logger and set level
        logger = Logger.getLogger(SeleniumLogger.class.getName());
        logger.setLevel(Level.INFO);

        //Create file handler with a file
        String browserType = System.getProperty("sel.btype");
        fileName = PropertyReader.getInstance().getTestGroup() + "-" + dayFormat.format(date) + "-" + timeFormat.format(date) + "-testresults";
        if ((browserType != null) && (!browserType.equals(""))) {
            browserType = browserType.replace(" ", "");
            browserType = browserType.replaceAll("[^a-zA-Z 0-9]+", "");
            fileName += "-" + browserType + ".log";
        } else {
            fileName += ".log";
        }
        fileHandler = new FileHandler(fileName, true);

        //set file format in file handler
        fileHandler.setFormatter(new SimpleFormatter());
        //add handler to logger
        logger.addHandler(fileHandler);
    }

    public static void tearDown() {
        logger.removeHandler(fileHandler);
        fileHandler.close();
    }

}
