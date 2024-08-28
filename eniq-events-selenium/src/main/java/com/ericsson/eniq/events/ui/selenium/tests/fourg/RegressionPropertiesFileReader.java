/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author ekeviry
 * @since 2011
 * 
 */

public class RegressionPropertiesFileReader {

	final static Properties prop = new Properties();

	protected static String fileName = "C:\\regression.properties";

	// protected static String fileName =
	// "/eniq/home/dcuser/selenium/selenium-grid-1.0.8/test-cases/regression.properties";

	public String[] readDatabaseProperties() throws FileNotFoundException,
			IOException {
		loadPropertiesFile();

		final String[] dbProperties = new String[6];

		dbProperties[0] = prop.getProperty("db.port");

		dbProperties[1] = prop.getProperty("db.driver.class");

		dbProperties[2] = prop.getProperty("db.path");

		dbProperties[3] = prop.getProperty("db.name");

		dbProperties[4] = prop.getProperty("db.username");

		dbProperties[5] = prop.getProperty("db.password");

		return dbProperties;
	}

	public String readIMSIValue() throws FileNotFoundException, IOException {
		loadPropertiesFile();

		final String imsiValue = prop.getProperty("imsi.value");

		return imsiValue;
	}

	public String EventTypeValue() throws FileNotFoundException, IOException {
		loadPropertiesFile();

		final String eTValue = prop.getProperty("eventType.value");

		return eTValue;
	}

	public int EventTypeNum() throws FileNotFoundException, IOException {
		loadPropertiesFile();

		final String etNum = prop.getProperty("eventType.num");
		final int aInt = Integer.parseInt(etNum);

		return aInt;
	}

	public static int getTimeRangeFromPropFile() throws FileNotFoundException,
			IOException {
		loadPropertiesFile();

		final String propFileTimeRange = prop.getProperty("time.range");

		// Default time range of 1 day
		int timeRange = 1;

		if (propFileTimeRange.equals("one.week")) {
			timeRange = 7;
		}

		// System.out.println("Time Range: " + timeRange);
		return timeRange;
	}

	private static void loadPropertiesFile() throws FileNotFoundException,
			IOException {
		final String confDir = fileName;
		prop.load(new FileInputStream(confDir));
	}
}
