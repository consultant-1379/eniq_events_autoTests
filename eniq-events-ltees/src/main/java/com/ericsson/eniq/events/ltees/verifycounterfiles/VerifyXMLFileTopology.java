/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ltees.verifycounterfiles;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.applicationdrivers.*;
import com.ericsson.eniq.events.ltees.controllers.file.*;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.*;

public class VerifyXMLFileTopology {

  private final FileController getFile = new FileController();

  private final PropertiesFileController propFile = new PropertiesFileController();

  private final XmlController xml = new XmlController();

  private final AppDriver app = new AppDriver();

  private final LogFileWriter logWriter = new LogFileWriter();;

  private final ArrayList<String> allResults = new ArrayList<String>();

  // private final String counterPropKeyName = "CounterPropFile_Directory";

  /**
   * @return File Topology Column Headers for HTML
   */
  public String[] topologyColumnHeaders() {
    final String[] topologyColumnHeaders = { "File Name", "<ffv> Value", "Collection Begin Time",
        "Collection End Time", "Sub Network", "<neid> Values", "<ts> Value", "File Format [.gz]" ,
        "<eNBId>values(exist in valid file)","<eNBId>values(not exist in corrupted file)"};

    return topologyColumnHeaders;
  }

  /**
   * @return String Array[]: Collect Topology Results For Each File
   */
  public String[] collectTopologyResultsForEachFile(final String fileName) throws FileNotFoundException, IOException,
      SAXException, ParserConfigurationException {
    final String splitTypeIn = getFile.getInputDirectorySeparator();
    final String splitTypeOut = getFile.getOutputDirectorySeparator();
    final String[] resultsArray = new String[9];
    final String[] file = fileName.split(splitTypeIn);

    final String fileN = file[file.length - 1];
    final String folderDir = file[file.length - 2];
    final String ossDir = file[file.length - 3];
    final String fullFileName = ossDir + splitTypeOut + folderDir + splitTypeOut + fileN;

    resultsArray[0] = fullFileName;
    resultsArray[1] = checkFFVvalue(fileName);
    resultsArray[2] = checkCollectionBeginTime(fileName);
    resultsArray[3] = checkCollectionEndTime(fileName);
    resultsArray[4] = checkSubNetworkValue(fileName);
    resultsArray[5] = checkNEIDvalues(fileName);
    resultsArray[6] = checkTSvalue(fileName);
    resultsArray[7] = checkeNBIdForValidTopology();
    resultsArray[8] = checkeNBIdForCorruptedTopology();
    return resultsArray;
  }

  /**
   * @return Void: Write Topology Results To Log File
   */
  @SuppressWarnings("static-access")
  public void writeTopologyResultsToLogFile(final String[] resultsArray) throws FileNotFoundException, IOException {
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

    logWriter.writeToLogFile(fileDir, "\nResult (File Extension '.gz'): " + app.fileExtResult);

    final String counterPropKeyName = "CounterPropFile_Directory";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue("verifyFileTopologyResults", counterPropFileDir);
    if (yesNo.equalsIgnoreCase("y")) {
      logWriter.writeToLogFile(fileDir, "Result (<ffv> Value): " + resultsArray[1]);
      logWriter.writeToLogFile(fileDir, "Result (Collection Begin Time): " + resultsArray[2]);
      logWriter.writeToLogFile(fileDir, "Result (Collection End Time): " + resultsArray[3]);
      logWriter.writeToLogFile(fileDir, "Result (Sub Network Value): " + resultsArray[4]);
      logWriter.writeToLogFile(fileDir, "Result (<neid> Values): " + resultsArray[5]);
      logWriter.writeToLogFile(fileDir, "Result (<ts> Value): " + resultsArray[6]);
      logWriter.writeToLogFile(fileDir, "Result (<eNBId> values exist in valid file): " + resultsArray[7]);
      logWriter.writeToLogFile(fileDir, "Result (<eNBId> values not exist in corrupted file): " + resultsArray[8]);
    } else {
      logWriter.writeToLogFile(fileDir, "Result (<ffv> Value): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (Collection Begin Time): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (Collection End Time): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (Sub Network Value): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (<neid> Values): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (<ts> Value): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (<eNBId> values exist in valid file): " + "Not Selected");
      logWriter.writeToLogFile(fileDir, "Result (<eNBId> values not exist in corrupted file): " + "Not Selected");
    }
  }

  /**
   * @return String: Merge File Format Results With All Other Results
   */
  public ArrayList<String> mergeFileFormatResultsWithAllOtherResults(final String[] topologyColumnHeaders,
      final ArrayList<String> fileFormatCheckResults, final ArrayList<String> topologyResults)
      throws FileNotFoundException, IOException {
    /** Merge fileFormatCheckResults array into topologyResults array **/
    final int insertInterval = topologyColumnHeaders.length - 1;
    int insertPositions = insertInterval;

    for (int i = 0; i < fileFormatCheckResults.size(); i++) {
      topologyResults.add(insertPositions, fileFormatCheckResults.get(i));
      insertPositions = insertPositions + insertInterval + 1;
    }
    return topologyResults;
  }

  /**
   * @return String: [PASS/FAIL] Check FFV Value From XML file
   */
  public String checkFFVvalue(final String fileName) throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    final String fileDirCounter = propFile.getAppPropSingleStringValue("CounterXMLPropFile_Directory");
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String ffvValueOutputXML = xml.getElementContentForUniqueTagName(fileName, "ffv");
    final String ffvValueXMLPropFile = xml.getElementContentForUniqueTagName(fileDirCounter, "ffv");
    String ffvValueCheckResult = "FAIL";

    if (ffvValueOutputXML.equals(ffvValueXMLPropFile)) {
      ffvValueCheckResult = "PASS";
    } else {
      logWriter.writeToLogFile(fileDir, "\nExpected <ffv> Value: " + ffvValueXMLPropFile);
      logWriter.writeToLogFile(fileDir, "<ffv> Value in Output XML: " + ffvValueOutputXML);
    }
    return ffvValueCheckResult;
  }

  /**
   * @return String: [PASS/FAIL] Check Collection Begin Time [CBT] from file
   *         name name
   */
  public String checkCollectionBeginTime(final String fileName) throws FileNotFoundException, IOException,
      SAXException, ParserConfigurationException {
    String collectionBeginTimeCheckResult = "FAIL";
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String fullTime = xml.getElementContentForUniqueTagName(fileName, "cbt");
    final String collectionBeginTime = fullTime.substring(8, 12);
    final String startTime = xml.getStartTimeFromFileName(fileName);

    if (collectionBeginTime.equals(startTime)) {
      collectionBeginTimeCheckResult = "PASS";
    } else {
      logWriter.writeToLogFile(fileDir, "\nStart Time Stamp in File Name: " + startTime);
      logWriter.writeToLogFile(fileDir, "Collection Begin Time <cbt>: " + collectionBeginTime);
    }
    return collectionBeginTimeCheckResult;
  }

  /**
   * @return String: [PASS/FAIL] Check Collection End Time from file name
   */
  public String checkCollectionEndTime(final String fileName) throws FileNotFoundException, IOException {
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String fullMtsValue = xml.getElementValueOfUniqueChildTagInParentTag("mts", "mi", 1);
    final String mtsValue = fullMtsValue.substring(8, 12);
    final String endTimeStampInFileName = xml.getEndTimeFromFileName(fileName);
    String resultForEndTimeCheck = "FAIL";

    if (endTimeStampInFileName.equals(mtsValue)) {
      resultForEndTimeCheck = "PASS";
    } else {
      logWriter.writeToLogFile(fileDir, "\nEnd Time Stamp in File Name: " + endTimeStampInFileName);
      logWriter.writeToLogFile(fileDir, "End Time Stamp <mts>: " + mtsValue);
    }
    return resultForEndTimeCheck;
  }

  /**
   * @return String: [PASS/FAIL] Check Sub-Network Value from file name
   */
  public String checkSubNetworkValue(final String fileName) throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String subNetworkValueInOutputXMLFile = xml.getElementContentForUniqueTagName(fileName, "sn");
    final String subNetworkValueInFileName = getSubNetworkValueFromFileName(fileName);

    String resultForSubNetworkValueCheck = "FAIL";

    if (subNetworkValueInFileName.equals(subNetworkValueInOutputXMLFile)) {
      resultForSubNetworkValueCheck = "PASS";
    } else {
      logWriter.writeToLogFile(fileDir, "\nSub Network Value in File Name: " + subNetworkValueInFileName);
      logWriter.writeToLogFile(fileDir, "Sub Network Value <sn>: " + subNetworkValueInOutputXMLFile);
    }
    return resultForSubNetworkValueCheck;
  }

  /**
   * @return String: [PASS/FAIL] Check NEID Value from file name
   */
  public String checkNEIDvalues(final String fileName) throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String[] neidValues = getChildNodesNEID(fileName);
    final String erbsValueFromFileName = getERBSValueFromFileName(fileName);
    final String subNetworkValueInFileName = getSubNetworkValueFromFileName(fileName);

    String neidCheckResult = "FAIL";

    if (erbsValueFromFileName.equals(neidValues[0]) && subNetworkValueInFileName.equals(neidValues[1])) {
      neidCheckResult = "PASS";
    } else {
      logWriter.writeToLogFile(fileDir, "\nERBS value in file name:     " + erbsValueFromFileName);
      logWriter.writeToLogFile(fileDir, "ERBS in body of file <neun>: " + neidValues[0]);
      logWriter.writeToLogFile(fileDir, "Sub Network value in file name: " + subNetworkValueInFileName);
      logWriter.writeToLogFile(fileDir, "Sub Network value in body of file <nedn>: " + neidValues[1]);
    }
    return neidCheckResult;
  }

  /**
   * @return String: [PASS/FAIL] Check TS Value from file name
   */
  public String checkTSvalue(final String fileName) throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    final String fileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
    final String tsValue = xml.getElementContentForUniqueTagName(fileName, "ts");
    final String fileEndTime = tsValue.substring(8, 12);
    final String endTimeStampInFileName = xml.getEndTimeFromFileName(fileName);
    String tsCheckResult = "FAIL";

    if (fileEndTime.equals(endTimeStampInFileName)) {
      tsCheckResult = "PASS";
    } else {
      tsCheckResult = "FAIL";
      logWriter.writeToLogFile(fileDir, "\nEnd Time Stamp in File Name: " + endTimeStampInFileName);
      logWriter.writeToLogFile(fileDir, "Time Stamp <ts>: " + fileEndTime);
    }
    return tsCheckResult;
  }

  /**
   * @return String Array []: Get Child Nodes NEID from XML file
   */
  private String[] getChildNodesNEID(final String fileName) throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    final String neunValue = xml.getElementContentForUniqueTagName(fileName, "neun");
    final String nednValue = xml.getElementContentForUniqueTagName(fileName, "nedn");
    final String[] neidValues = { neunValue, nednValue };

    return neidValues;
  }

  /**
   * @return String: Get ERBS Value From file name
   */
  public String getERBSValueFromFileName(final String fileName) {
    final String[] firstFileNameDelimiter = fileName.split("[=]");
    final String[] secondFileNameDelimiter = firstFileNameDelimiter[2].split("[_]");
    final String erbsValueFromFileName = secondFileNameDelimiter[0];

    return erbsValueFromFileName;
  }

  /**
   * @return String: Get ERBS Value And OSS Counter file number
   */
  public String getErbsValueAndOssCounterFileNumber(final String fileName) {
    final String[] firstFileNameDelimiter = fileName.split("[=]");
    final String[] splitAtDot = firstFileNameDelimiter[firstFileNameDelimiter.length - 1].split(".");
    final String value = splitAtDot[0];
    return value;
  }

  /**
   * @return String: Get Sub-Network Value From File Name
   */
  private String getSubNetworkValueFromFileName(final String fileName) {
    final String[] firstDelimiter = fileName.split("[_]");
    String subNetworkValueInFileName = "";

    for (int i = 6; i < 9; i++) {
      subNetworkValueInFileName = subNetworkValueInFileName + firstDelimiter[i];
      if (i < 8) {
        subNetworkValueInFileName = subNetworkValueInFileName + "_";
      }
    }
    return subNetworkValueInFileName;
  }

  /**
   * @return String: Collect All Results
   */
  public ArrayList<String> collectAllResults(final String[] resultsArray) throws IOException {
    for (int i = 0; i < resultsArray.length; i++) {
      allResults.add(resultsArray[i]);
    }
    return allResults;
  }

  /**
   * @return String: Get ERBS Value From file name
   */
  public void clearAllResultsArrayList() {
    allResults.clear();
  }

  public String checkeNBIdForValidTopology()throws FileNotFoundException, IOException, SAXException,
            ParserConfigurationException {
    return new FullFileController().eNBIdStsForValidTopology();
  }

  public String checkeNBIdForCorruptedTopology()throws FileNotFoundException, IOException, SAXException,
    ParserConfigurationException {
    return new FullFileController().eNBIdStsForCorruptedTopology();
  }
}
