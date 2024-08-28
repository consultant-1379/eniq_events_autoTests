/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.ltees.controllers.resultwriters;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;

/**
 * @author ekeviry
 * @since 2011
 * 
 */

public class HTMLResultFileWriter {

	protected String[] summaryPageTableHeaders = { "OSS-RC", "Test Type", "Pass", "Fail",
			"Pass Rate", "Detailed Summary", "Log File" };

	public ArrayList<String> runHTMLFileWriter(final String fileName, final String[] columnHeaders,
			final ArrayList<String> allResults, final ArrayList<String> nonEmptyResultFiles,
			final String testType, final String ossDirName, final String testTimestampStartOSS,
			final String logFileHyperlink) throws IOException {

		if (!allResults.isEmpty()) {
			final String[] columnHeadersHTMLFormat = convertColumnHeadersToHTMLFriendlyFormat(columnHeaders);

			createNewHTMLFile(fileName, columnHeadersHTMLFormat, ossDirName, testTimestampStartOSS,
					false);

			final String testTimestampEndOSS = getTestTimestamp();

			final File logFileLink = new File(logFileHyperlink);
			writeResultsToHTMLFile(fileName, allResults, columnHeadersHTMLFormat, logFileLink, true);

			final int[] numberOfPassAndFails = getNumberOfPassAndFails(allResults);
			final String passCount = Integer.toString(numberOfPassAndFails[0]);
			final String failCount = Integer.toString(numberOfPassAndFails[1]);

			final int successRate = calculateSuccessRate(numberOfPassAndFails);

			final String successRateString = Integer.toString(successRate);
			// successRateString = successRateString + "%";

			writeNumberOfPassAndFailsToHTMLOutputFile(fileName, numberOfPassAndFails,
					testTimestampEndOSS);

			final PropertiesFileController propFileReader = new PropertiesFileController();
			final String unixURL = propFileReader.getAppPropSingleStringValue("Unix_URL");
			final String detailedSummaryHyperlink = unixURL + ossDirName + "_" + testType
					+ "_Results.html";

			/** REFACTOR **/
			nonEmptyResultFiles.add(detailedSummaryHyperlink);
			nonEmptyResultFiles.add(ossDirName);
			nonEmptyResultFiles.add(testType);
			nonEmptyResultFiles.add(passCount);
			nonEmptyResultFiles.add(failCount);
			nonEmptyResultFiles.add(successRateString);
			nonEmptyResultFiles.add("-");
		}

		else {
			final File oldResultPage = new File(fileName);

			if (oldResultPage.exists()) {
				oldResultPage.delete();
			}
			nonEmptyResultFiles.add(fileName);
			nonEmptyResultFiles.add(ossDirName);
			nonEmptyResultFiles.add(testType);
			nonEmptyResultFiles.add("-");
			nonEmptyResultFiles.add("-");
			nonEmptyResultFiles.add("-1");
			nonEmptyResultFiles.add("No Data");
		}

		return nonEmptyResultFiles;
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	// Create HTML file - if HTML already exists, it is deleted
	public void createNewHTMLFile(final String fileName, final String[] columnHeaders,
			final String hyperlinkTextValue, final String testTimestampStartOSS,
			final boolean append) {

		final String hostname = getHostname("controlzone");

		// final String eniqVersion = getEniqVersion();

		try {
			final PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, append));

			printWriter.println("<HTML>");

			printWriter.println("<TITLE>ENIQ Events | LTEES Regression Results | "
					+ hyperlinkTextValue + "</TITLE>");

			printWriter.println("<FONT FACE = VERDANA>");
			printWriter.println("<FONT COLOR = DARKBLUE>");

			printWriter.println("<H2>" + hyperlinkTextValue + "</H2>");

			printWriter.println("<H4>HOST: " + hostname + "</H4>");

			printWriter.println("<H4>STARTTIME: " + testTimestampStartOSS + "</H4>");
			printWriter.println("</FONT COLOR>");

			printWriter.println("<BODY><TABLE BORDER = 2 CELLSPACING = 2><TR ALIGN = CENTER>");

			for (int i = 0; i < columnHeaders.length; i++) {
				printWriter.println("<TD BGCOLOR = 	#B8B8B8><FONT SIZE = 2><B>" + columnHeaders[i]
						+ "</B></FONT></TD>");
			}

			printWriter.println("</TR>");

			printWriter.close();

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public void writeResultsToHTMLFile(final String fileName, final ArrayList<String> allResults,
			final String[] columnHeaders, final File logFileHyperlink, final boolean append) {
		try {
			// System.out.println("Col Header Length: " + columnHeaders.length);
			// System.out.println("allResults Size: " + allResults.size());
			// System.out.println("Delta Log File Link: " +
			// logFileHyperlink.getPath());
			System.out.println();
			final PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, append));

			for (int i = 0; i < allResults.size(); i++) {
				printWriter.println("<TR>");

				for (int j = 0; j < columnHeaders.length; j++) {
					if (allResults.get(i).equals("FAIL")) {
						printWriter.println("<TD ALIGN = CENTER><A HREF='" + logFileHyperlink
								+ "'><FONT COLOR = #FF0000 SIZE = 2><B>" + allResults.get(i)
								+ "</B></FONT></A></TD>");
					} else if (allResults.get(i).equals("PASS")) {
						printWriter.println("<TD ALIGN = CENTER><FONT SIZE = 2>"
								+ allResults.get(i) + "</FONT></TD>");
					} else {
						printWriter.println("<TD><FONT SIZE = 2>" + allResults.get(i)
								+ "</FONT></TD>");
					}

					i++;
				}
				i--;

				printWriter.println("</TR>");
			}

			printWriter.println("</TABLE>");
			printWriter.close();

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public String getTestTimestamp() {

		final Date date = new Date();

		final Timestamp testTime = new Timestamp(date.getTime());

		final String testTimeStamp = testTime.toString();

		return testTimeStamp;
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public String getHostname(String fmri) {
		
		return getDetailForHosts(fmri,1);
	}
	public String getIPAddress(String fmri){
		return getDetailForHosts(fmri, 0);
	}
    public String getDetailForHosts(String fmri, int i){
	String result = "";

	try {

		// final Process p = Runtime.getRuntime().exec("hostname");
		final Process p = Runtime.getRuntime().exec("grep -iw "+fmri+" /etc/hosts");
		
		final BufferedReader commandLineOutput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		result = commandLineOutput.readLine();

		final String[] splitCZHostname = result.split("\\s+");

		result = splitCZHostname[i];

		

		commandLineOutput.close();

	} catch (final IOException e) {
		e.printStackTrace();
	}

	return result;
}
	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public String[] getEniqVersion() {
		/**************************************************/
		/*********** WONT WORK ON LOCAL MACHINE ***********/
		/**************************************************/
		final String[] eniqVersionAndInstallDate = new String[2];

		try {
			final FileInputStream fstream = new FileInputStream("/eniq/admin/version/eniq_status");
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));

			for (int i = 0; i < eniqVersionAndInstallDate.length; i++) {
				eniqVersionAndInstallDate[i] = br.readLine();
			}

		} catch (final Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		return eniqVersionAndInstallDate;
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	private int[] getNumberOfPassAndFails(final ArrayList<String> allResults) {

		int passCount = 0;
		int failCount = 0;

		for (int i = 0; i < allResults.size(); i++) {
			if (allResults.get(i).equals("PASS")) {
				passCount++;
			}

			if (allResults.get(i).equals("FAIL")) {
				failCount++;
			}
		}

		final int[] numberOfPassAndFailsArray = new int[2];

		numberOfPassAndFailsArray[0] = passCount;
		numberOfPassAndFailsArray[1] = failCount;

		return numberOfPassAndFailsArray;
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	private void writeNumberOfPassAndFailsToHTMLOutputFile(final String fileName,
			final int[] numberOfPassAndFails, final String testTimestampEndOSS) throws IOException {
		final PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, true));

		final int passCount = numberOfPassAndFails[0];
		final int failCount = numberOfPassAndFails[1];

		printWriter.println("<BR><TABLE BORDER = 2 CELLSPACING = 2 CELLPADDING = 4>");
		printWriter.println("<TR><TH>PASS Total<TH>FAIL Total</TR>");
		printWriter.println("<TR ALIGN = CENTER><TD>" + passCount + "<TD>" + failCount
				+ "</TR></TABLE>");

		printWriter.println("<FONT FACE = VERDANA>");
		printWriter.println("<FONT COLOR = DARKBLUE>");

		printWriter.println("<BR><H4>ENDTIME: " + testTimestampEndOSS + "</H4>");

		printWriter.println("</FONT></BODY></HTML>");

		printWriter.close();
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	private String[] convertColumnHeadersToHTMLFriendlyFormat(final String[] columnHeaders) {

		for (int i = 0; i < columnHeaders.length; i++) {
			if (columnHeaders[i].contains("<")) {
				String tmp = columnHeaders[i];
				final String[] splitArray = tmp.split("<");
				tmp = splitArray[0] + "&lt;";

				if (splitArray.length > 1) {
					tmp = tmp + splitArray[1];
				}
				columnHeaders[i] = tmp;
			}

			if (columnHeaders[i].contains(">")) {
				String tmp = columnHeaders[i];
				final String[] splitArray = tmp.split(">");
				tmp = splitArray[0] + "&gt;";

				if (splitArray.length > 1) {
					tmp = tmp + splitArray[1];
				}
				columnHeaders[i] = tmp;
			}
		}

		return columnHeaders;
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	private int calculateSuccessRate(final int[] numberOfPassAndFails) {
		final int passCount = numberOfPassAndFails[0];
		final int failCount = numberOfPassAndFails[1];

		final int total = passCount + failCount;

		int successRate = passCount * 100;
		successRate = successRate / total;

		return successRate;
	}

	/**
	 * @author ekeviry
	 * @throws IOException
	 * @since 2011
	 */
	@SuppressWarnings("unused")
	public void generateSummaryPageForLTEES(final String fileName,
			final ArrayList<String> nonEmptyResultFiles, final String testTimestampStart,
			final String testTimestampEnd, final boolean append) throws IOException {

		/*******************************************************************************/
		/********************************** REFACTOR ***********************************/
		/*******************************************************************************/

		final String hostname = getHostname("controlzone");

		final String[] eniqVersionAndInstallTime = getEniqVersion();
		String eniqVersion = eniqVersionAndInstallTime[0];
		String eniqInstallDate = eniqVersionAndInstallTime[1];

		if (eniqVersion == null) {
			eniqVersion = "N/A";
		}
		if (eniqInstallDate == null) {
			eniqInstallDate = "N/A";
		}

		final PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, append));

		final String tableTextFormat = "<FONT FACE = VERDANA COLOR = DARKBLUE SIZE = 2>";

		printWriter.println("<HTML>");

		printWriter.println("<TITLE>ENIQ Events | LTEES Regression Test Summary</TITLE>");

		printWriter.println("<FONT FACE = VERDANA COLOR = DARKBLUE>");

		printWriter.println("<BODY><H2>LTEES Regression Summary</H2>");

		final PropertiesFileController propFile = new PropertiesFileController();
		final String numberOfTestCasesExecuted = propFile
				.getAppPropSingleStringValue("Total_Test_Cases_Executed");
		printWriter.println("<H4>HOST:" + hostname + "<BR>");
		printWriter.println("STARTTIME:" + testTimestampStart + "<BR>");
		printWriter.println("VERSION:" + eniqVersion + "<BR>");
		printWriter.println("INSTALL DATE:" + eniqInstallDate + "<BR>");
		printWriter.println("TOTAL TC's EXECUTED:" + numberOfTestCasesExecuted + "</H4><BR>");

		for (int i = 0; i < nonEmptyResultFiles.size(); i++) {

			final String detailedSummaryHyperlink = nonEmptyResultFiles.get(i);
			final String ossDirName = nonEmptyResultFiles.get(i + 1);
			final String testType = nonEmptyResultFiles.get(i + 2);
			final String passCount = nonEmptyResultFiles.get(i + 3);
			String failCount = nonEmptyResultFiles.get(i + 4);
			final String passRate = nonEmptyResultFiles.get(i + 5);
			final String detailedSummary = nonEmptyResultFiles.get(i + 6);
			final String logFileHyperlink = nonEmptyResultFiles.get(i + 7);

			final int passRateInteger = Integer.parseInt(passRate);

			printWriter
					.println("<TABLE BORDER = 1 CELLSPACING = 2 CELLPADDING = 2><TR ALIGN = CENTER>");

			for (int j = 0; j < summaryPageTableHeaders.length; j++) {
				printWriter.println("<TH BGCOLOR = 	#B8B8B8>" + tableTextFormat
						+ summaryPageTableHeaders[j] + "</FONT>");
			}

			printWriter.println("<TR ALIGN = CENTER><TD><B>" + tableTextFormat + ossDirName
					+ "</B>");

			printWriter.println("<TD>" + tableTextFormat + testType + "</FONT>");

			printWriter.println("<TD>" + tableTextFormat + passCount + "</FONT>");

			if (failCount.equals("0")) {
				failCount = "-";
			}

			printWriter.println("<TD BGCOLOR = RED>" + tableTextFormat + failCount + "</FONT>");

			if (passRateInteger == 100) {
				printWriter.println("<TD>" + tableTextFormat + passRateInteger + "%</FONT>");
			}

			else if (passRateInteger < 100 && passRateInteger >= 85) {
				printWriter.println("<TD BGCOLOR = ORANGE>" + tableTextFormat + passRateInteger
						+ "%</FONT>");
			}

			else if (passRateInteger == -1) {
				printWriter.println("<TD>" + tableTextFormat + "-</FONT>");
			}

			else {
				printWriter.println("<TD BGCOLOR = RED>" + tableTextFormat + passRateInteger
						+ "%</FONT>");
			}

			if (detailedSummary.equals("No Data")) {
				printWriter.println("<TD BGCOLOR = RED>" + tableTextFormat + "No Data</FONT>");
				printWriter.println("<TD>" + tableTextFormat + "-</FONT>");
			} else {
				printWriter.println("<TD><A HREF='" + detailedSummaryHyperlink + "'>"
						+ tableTextFormat + "View</FONT></A>");

				printWriter.println("<TD><A HREF='" + logFileHyperlink + "'>" + tableTextFormat
						+ "Open</FONT></A>");
			}

			printWriter.println("</TABLE><BR><BR>");
			i = i + 7;
		}

		printWriter.println("</FONT></BODY></HTML>");
		printWriter.close();
	}

	/**
	 * @author ekeviry
	 * @since 2011
	 */
	public int calculateOverallPassRateForLTEES(final ArrayList<String> nonEmptyResultFiles) {

		final ArrayList<Integer> passRateArrayList = new ArrayList<Integer>();

		for (int i = 5; i < nonEmptyResultFiles.size(); i = i + 8) {
			final String passRateString = nonEmptyResultFiles.get(i);

			final int passRate = Integer.parseInt(passRateString);

			if (passRate != -1) {
				passRateArrayList.add(passRate);
				System.out.println("Pass Rate: " + passRate);
			}

		}

		int overallPassRate = 0;

		for (int i = 0; i < passRateArrayList.size(); i++) {
			overallPassRate = overallPassRate + passRateArrayList.get(i);
		}

		overallPassRate = overallPassRate / passRateArrayList.size();

		System.out.println("Overall Pass Rate: " + overallPassRate);

		return overallPassRate;
	}

}
