/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.applicationdrivers;

import java.io.*;
import java.util.*;

import com.ericsson.eniq.events.ltees.controllers.file.*;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.*;
import com.ericsson.eniq.events.ltees.environmentsetup.*;
import com.ericsson.eniq.events.ltees.verifydeltatopology.*;

/**
 * @author ESAIMKH
 * @since 2011
 * 
 */
public class DeltaTopologyDriver {

  static String runAppLocal = "runLocal";

  private static VerifyDeltaTopology delta = new VerifyDeltaTopology();

  private static LogFileWriter logWriter = new LogFileWriter();

  private static PropertiesFileController propFile = new PropertiesFileController();

  private static FileController file = new FileController();

  // Result Table Titles
  static String[] deltaTopologyHeader = new String[] { "Rop Number", "Start Time", "End Time", "EUtranCellRelation",
      "ENodeBFunction", "EUtranCellFDD", "TermPointToENB" };

  // Complete True/False result
  static ArrayList<String> DeltaResult = new ArrayList<String>();

  /**
   * @param args
   * @throws Exception
   */
  public static void main(final String[] args) throws Exception {
    final String runLocal = propFile.getAppPropSingleStringValue(runAppLocal);

    System.out.println("\nDelta Topology Automation STARTED");
    System.out.println("Please wait until test application execution is complete.........\n");
    System.out.println("Delta Topology Results per ROP: ");

    if (runLocal.equalsIgnoreCase("n")) {
      final EnvironmentSetupDriver environDriver = new EnvironmentSetupDriver();
      environDriver.verifyFileAndDirectoryStructure("");
    }

    /***
     * Run Delta Topology Generator and LTE Topology Gen
     ***/
    if (runLocal.equalsIgnoreCase("n")) {
      final EnableAndDisableDataGen dataGen = new EnableAndDisableDataGen();
      dataGen.runDataGen("Delta_DataGen_Run_Time", true, true);
    }
    /** Start Delete - Delete all final latest ROP date files **/
    final ArrayList<String> latestDateFiles = file.getAllLatestDateFiles(true, false, false, false);
    file.deleteFilesWithExtension(latestDateFiles, "xml");
    /*** End Delete ***/

    // Delta Topology Results
    final ArrayList<String> deltaResult = delta.DeltaTopologyOverallResults();
    delta.printToLogFile(); // Delta Topology Results to LogFile

    final int headerCount = deltaTopologyHeader.length;
    final int deltaResultCount = deltaResult.size();
    final ArrayList<String> tmpArray = new ArrayList<String>(); // Temporary

    String result = null;
    int loopCount = 0;
    int ropNum = 0;

    for (int i = 0; i < deltaResultCount; i++) {// Delta Results Loop
      loopCount++;
      tmpArray.add(deltaResult.get(i));
      if (loopCount == headerCount) {
        ropNum++;
        if (tmpArray.contains("FAIL")) {
          result = "FAIL";
        } else {
          result = "PASS";
        }
        System.out.println("ROP " + ropNum + ": " + result);
        loopCount = 0;
        tmpArray.clear();
      }
    }

    final String outputFileDir = propFile.getAppPropSingleStringValue("OutputFolder_Directory");
    final String fullPathDeltaLogFile = propFile.getAppPropSingleStringValue("DeltaTopologyLogFile");
    final String deltaLogFileLink = propFile.getAppPropSingleStringValue("URL_Delta_Logfile_Link");
    final String deltaHTMLResultPageName = outputFileDir + "Delta_Results.html";

    final File deltaLinkF = new File(deltaLogFileLink);
    final HTMLResultFileWriter deltaHTMLWriter = new HTMLResultFileWriter();
    final String testTimeStamp = deltaHTMLWriter.getTestTimestamp();

    deltaHTMLWriter.createNewHTMLFile(deltaHTMLResultPageName, deltaTopologyHeader, "Delta Topology Test Results",
        testTimeStamp, false);
    deltaHTMLWriter.writeResultsToHTMLFile(deltaHTMLResultPageName, deltaResult, deltaTopologyHeader, deltaLinkF, true);
    logWriter.systemConsoleLineDivider("_", true);

    if (runLocal.equalsIgnoreCase("n")) {
      final FTPUtil ftpUpload = new FTPUtil();
      ftpUpload.copyHtmlAndLogFilesToUnixBox(outputFileDir, fullPathDeltaLogFile);
    } else {
      System.out.println("Delta Topology Result Files:\n\nResult files can be obained from the following directories");
      System.out.println(">>HTML View [" + deltaHTMLResultPageName + "]");
      System.out.println(">>Log File View [" + fullPathDeltaLogFile + "]");
    }

    logWriter.systemConsoleLineDivider("=", true);
    System.out.println("Delta Topology Test Automation COMPLETED");

  }
}
