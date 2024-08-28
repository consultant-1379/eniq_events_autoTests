/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ltees.controllers.resultwriters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author ekeviry
 * @since 2011
 */

public class LogFileWriter {

	// Create new log file - if logfile.log already exists, it is deleted
	public void createNewLogFile(final String fileName, final String testTimestampStart,
			final boolean append, boolean commonSetup) {

		final HTMLResultFileWriter htmlFileWriter = new HTMLResultFileWriter();
		final String hostname = htmlFileWriter.getHostname("controlzone");
		File file = new File(fileName);
		File parentDirectory = file.getParentFile();
		if(!parentDirectory.exists()){
			parentDirectory.mkdirs();
		}
		try {
			final FileWriter logger = new FileWriter(fileName, append);
			if(!commonSetup){
				logger.append('\n');
				logger.append("HOST: " + hostname);
				logger.append('\n');
				logger.append("TEST START TIME: " + testTimestampStart);
			}
			// System.out.println("HOST: " + hostname);
			// System.out.println("TEST START TIME: " + testTimestampStart);

			logger.flush();
			logger.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public void writeToLogFile(final String fileName, final String logEntry) {
		appendLogInfoToFile(fileName, true, logEntry);
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	private void appendLogInfoToFile(final String fileName, final boolean append,
			final String loggableInfo) {
		try {
			final FileWriter logger = new FileWriter(fileName, append);

			logger.append(loggableInfo);
			logger.append('\n');

			// System.out.println(loggableInfo);

			logger.flush();
			logger.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author esaimkh
	 * @since 2011
	 * @return Write OSS Names to LogFile
	 */
	public void writeOssDirectoryNameToLogFile(final String fileDir, final String ossDirName) {
		writeToLogFile(fileDir, "\n");
		// System.out.println("\n");
		logFileDivider(fileDir, "*", true);
		writeToLogFile(fileDir, "OSS-RC: " + ossDirName.toUpperCase());
		// System.out.println("OSS-RC: " + ossDirName.toUpperCase());
		logFileDivider(fileDir, "*", true);

		// System.out.println("\n");

	}

	public void systemConsoleLineDivider(final String divider, final boolean addConsoleDivider) {
		if (addConsoleDivider) {
			System.out.print("\n");
			String consoleDivider = "";
			for (int i = 0; i < 100; i++) {
				consoleDivider = consoleDivider + divider;
			}
			System.out.print(consoleDivider + "\n");
		}
	}

	public void logFileDivider(final String fileName, final String divider,
			final boolean addStringDivider) {
		if (addStringDivider) {
			String dividerString = "";
			for (int x = 0; x < 200; x++) {
				dividerString = dividerString + divider;
			}
			writeToLogFile(fileName, dividerString);
			// System.out.println(dividerString);
		}

	}

}
