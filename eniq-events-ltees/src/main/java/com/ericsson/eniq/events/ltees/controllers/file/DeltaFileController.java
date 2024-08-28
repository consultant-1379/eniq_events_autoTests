/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.controllers.file;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.controllers.common.*;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.*;
import com.ericsson.eniq.events.ltees.topologygenerator.*;
import com.ericsson.eniq.events.ltees.verifycounterfiles.*;

/**
 * @author ESAIMKH/EKEVIRY
 * @since 2011
 */

public class DeltaFileController extends XmlController {

  protected final FileController getFile = new FileController();

  protected final CommonController common = new CommonController();

  protected final LogFileWriter logWriter = new LogFileWriter();

  protected final HTMLResultFileWriter htmlResultWriter = new HTMLResultFileWriter();

  protected final PropertiesFileController propFile = new PropertiesFileController();

  private final VerifyXMLFileTopology xmlTopology = new VerifyXMLFileTopology();

  /*** ArrayList and Arrays ***/
  private final ArrayList<String> arrayList = new ArrayList<String>();

  public final ArrayList<String> fullStringCompareArrayList = new ArrayList<String>();

  public final ArrayList<String> fullFileNameStringCompareArrayList = new ArrayList<String>();

  private final String[] deltaHeader = { "File Count", "OSS-RC", "Directory Folder", "EUtranCellFDD",
      "EUtranCellRelation", "TermPointToMme", "TermPointToENB", "ProcessorLoad" };

  private final String[] deltaHeaderPerRop = { "Rop Number", "Start Time", "End Time", "EUtranCellFDD",
      "EUtranCellRelation", "TermPointToMme", "TermPointToENB", "ProcessorLoad" };

  /***
   * Run Delta Topology Generator. OSS and Number of Rops can be specified in
   * app.properties
   ***/
  public void runDeltaTopologyGenerator() throws FileNotFoundException, IOException {
    final String timeStamp = htmlResultWriter.getTestTimestamp();
    final String deltaLogFile = propFile.getAppPropSingleStringValue("DeltaTopologyLogFile");
    final String deltaTopologyGenOSS = propFile.getAppPropSingleStringValue("Delta_Topology_Gen_OSS");
    final String deltaTopologyGenRop = propFile.getAppPropSingleStringValue("Delta_Topology_Gen_Rop");

    final String logEntry = "[Environment Setup] Enabling Delta Topology Generator for OSS ";
    final String[] arg = new String[4];

    System.out.println(logEntry + deltaTopologyGenOSS + ". Number of Rops: " + deltaTopologyGenRop + ". Time: "
        + timeStamp);
    logWriter.writeToLogFile(deltaLogFile, logEntry + deltaTopologyGenOSS + ". Number of Rops: " + deltaTopologyGenRop
        + ". Time: " + timeStamp);
    arg[0] = "-oss";
    arg[1] = deltaTopologyGenOSS;
    arg[2] = "-rop";
    arg[3] = deltaTopologyGenRop;
    deltatopology.main(arg);
  }

  /**
   * @return Integer - Method with criteria's set to get a count of MOID value
   *         (like comparison)in Odd or Even ROPs
   */
  public String recordFileRopNumberRopTimesAndSpecifiedMoidValuesCount(final String fileNameUnZipped,
      final String moidValueLike1, final String moidValueLike2) throws FileNotFoundException, IOException,
      SAXException, ParserConfigurationException {

    final String[] ropStart = ropStartTimes(); // ROP Start Times
    final String[] ropEnd = ropEndTimes(); // ROP End Times
    int counter = 0; // Counter for MOID Value
    String result = "";

    // Loop through start & end times to find out what ROP the file belongs to
    // and how many of the specified MOID values the file contains
    for (int i = 0; i < ropStart.length; i++) {
      final int ropNum = i + 1;
      final String startTime = ropStart[i];
      final String endTime = ropEnd[i];

      if (fileNameUnZipped.indexOf(startTime) != -1 && fileNameUnZipped.indexOf(endTime) != -1) {
        final int miTagNumber = getMiBlockNumberBasedOnMoidValue(fileNameUnZipped, moidValueLike1);
        counter = countMoidValueBasedOn2Strings(fileNameUnZipped, moidValueLike1, moidValueLike2, miTagNumber);

        result = "Rop" + ropNum + "," + startTime + "," + endTime + "," + fileNameUnZipped + "," + "MoidCount:"
            + counter;// Print result to String
        // System.out.println(result);// DELETE!!!!!!!!!!
        break;// Break out of loop if file is found
      }
    }
    return result;
  }

  /**
   * @return Integer: Count the number of External Cell relation in MI-MV block
   *         in File
   */
  public boolean checkIfExternalEUtranCellRelationExists(final String fileName, final String internalExt,
      final String externalExt) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
    boolean checkInternalExt = false;
    boolean result = false;
    final String moidVal = "EUtranCellRelation=EXTLTE";
    final String[] partStrings = { moidVal, "ERBS00", "LTE" };
    final String fileNameUnZipped = getFile.gunzipFileIfGzFileFormatWithoutDeletingOriginalFile(fileName);
    final int miTagNumber = getMiBlockNumberBasedOnMoidValue(fileNameUnZipped, moidVal);
    final String[] moidValues = getMoidElementValuesPerMi(miTagNumber);

    for (int s = 0; s < moidValues.length; s++) {
      final String fullString = moidValues[s];
      final String[] stringSplit = fullString.split("[,]");
      final String string = stringSplit[stringSplit.length - 1];

      final boolean checkResult = checkIfFullStringContainsPartStringFromArray(string, partStrings);
      if (string.indexOf(internalExt + "LTE") != -1) {
        checkInternalExt = true;
      }

      final boolean endString = string.endsWith(externalExt);
      if (checkResult && endString && checkInternalExt) {
        result = true;
      }
    }
    return result;
  }

  public int countExternalEUtranCellRelation(final String fileName, final String internalExt, final String externalExt)
      throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
    boolean checkInternalExt = false;
    // boolean result = false;
    int count = 0;

    final String moidVal = "EUtranCellRelation=EXTLTE";
    final String[] partStrings = { moidVal, "ERBS00", "LTE" };
    final String fileNameUnZipped = getFile.gunzipFileIfGzFileFormatWithoutDeletingOriginalFile(fileName);
    final int miTagNumber = getMiBlockNumberBasedOnMoidValue(fileNameUnZipped, moidVal);
    final String[] moidValues = getMoidElementValuesPerMi(miTagNumber);

    for (int s = 0; s < moidValues.length; s++) {
      final String fullString = moidValues[s];
      final String[] stringSplit = fullString.split("[,]");
      final String string = stringSplit[stringSplit.length - 1];

      final boolean checkResult = checkIfFullStringContainsPartStringFromArray(string, partStrings);
      if (string.indexOf(internalExt + "LTE") != -1) {
        checkInternalExt = true;
      }

      final boolean endString = string.endsWith(externalExt);
      if (checkResult && endString && checkInternalExt) {
        // result = true;
        count++;
      }
    }
    return count;
  }

  /**
   * @return Boolean true/false: Check if a full String contains one or more
   *         part strings
   */
  public boolean checkIfFullStringContainsPartStringFromArray(final String fullString, final String[] partMatchStrings) {
    boolean result = true;
    for (int i = 0; i < partMatchStrings.length; i++) {
      if (fullString.indexOf(partMatchStrings[i]) == -1) {
        result = false;
      }
    }
    return result;
  }

  /**
   * @return Boolean - Check if a specified MOID values exist in a .XML file
   * @param files
   *          (String ArrayList) - List of files
   * @param moidValueLike1
   *          : String comparison 1 - Compared against full MOID value
   * @param moidValueLike2
   *          : String comparison 2 - Compared against full MOID value
   */
  public boolean checkIfMoidExitsInFile(final String fileNameUnZipped, final String moidValueLike1,
      final String moidValueLike2) throws SAXException, IOException, ParserConfigurationException {
    boolean checkMoid = false;
    final int miTagNumber = getMiBlockNumberBasedOnMoidValue(fileNameUnZipped, moidValueLike1);
    final int mvCount = countMvTagsBasedOnMoidValue(fileNameUnZipped, moidValueLike1);
    for (int s = 0; s < mvCount; s++) {
      checkMoid = checkIfMoidValueExistsInMiBlock(fileNameUnZipped, moidValueLike2, miTagNumber);
    }
    return checkMoid;
  }

  /**
   * @return Boolean - Check if a specified MOID values exist in a .XML file
   * @param files
   *          (String ArrayList) - List of files
   * @param moidValueLike1
   *          : String comparison 1 - Compared against full MOID value
   * @param moidValueLike2
   *          : String comparison 2 - Compared against full MOID value
   */

  public int countMoidValue(final String fileNameUnZipped, final String moidValueLike1, final String moidValueLike2)
      throws SAXException, IOException, ParserConfigurationException {
    int count = 0;
    final int miTagNumber = getMiBlockNumberBasedOnMoidValue(fileNameUnZipped, moidValueLike1);
    final String[] moidValues = getMoidElementValuesPerMi(miTagNumber);

    for (int s = 0; s < moidValues.length; s++) {
      final String moidValue = moidValues[s].toLowerCase();
      final String moidString = moidValueLike2.toLowerCase();
      if (moidValue.contains(moidString)) {
        count++;
      }
    }
    return count;
  }

  /**
   * @return String[] - Get the Start Times for latest ROP
   * @throws IOException
   * @throws FileNotFoundException
   */

  public String[] ropStartTimes() throws FileNotFoundException, IOException {
    final int numOfRops = getFile.getNumberOfRops();// Number of ROPs
    final ArrayList<String> files = getFile.getAllLatestDateFiles(true, false, false, false);
    final String[] ropStart = getFile.getRopStartTimes(files, numOfRops, false);
    return ropStart;
  }

  /**
   * @return String[] - Get the End Times for latest ROP
   */

  public String[] ropEndTimes() throws FileNotFoundException, IOException {
    final int numOfRops = getFile.getNumberOfRops();// Number of ROPs
    final ArrayList<String> files = getFile.getAllLatestDateFiles(true, false, false, false);
    final String[] ropEnd = getFile.getRopEndTimes(files, numOfRops, false);
    return ropEnd;
  }

  /**
   * @return ArrayList - First X-Node files from required Rop Times in set
   *         between minimum node number and maximum node number
   * @see Example: ArrayList<String> array = getFileilesFromRopForFirstXNodes(5,
   *      1, 100); ---> First 5 nodes from set between node 1 & node 100
   */

  public ArrayList<String> getFilesFromRopForFirstXNodesUnZipped(final int numberOfNodes, final int minNodeNumber,
      final int maxNodesNumber, final boolean GunZipFile) throws FileNotFoundException, IOException {
    final ArrayList<String> array = new ArrayList<String>();
    final ArrayList<String> latestDateFiles = getFile.getAllLatestDateFiles(true, false, false, false);

    final int numOfRops = getFile.getNumberOfRops();// Number of Rops
    final String[] ropStartTimes = getFile.getRopStartTimes(latestDateFiles, numOfRops, false);
    final String[] ropEndTimes = getFile.getRopEndTimes(latestDateFiles, numOfRops, false);

    // 1. get files from each rop
    for (int s = 0; s < numOfRops; s++) {
      final ArrayList<String> filesFromRop = new ArrayList<String>();

      // Loop through all files & get files from rop x
      for (int i = 0; i < latestDateFiles.size(); i++) {
        final String fileName = latestDateFiles.get(i);
        if (!(fileName.indexOf(ropStartTimes[s]) == -1) && !(fileName.indexOf(ropEndTimes[s]) == -1)) {
          filesFromRop.add(fileName);
        }
      }

      // 2.get files where erbs number is between minNodeNumber &
      // maxNodesNumber
      final ArrayList<String> erbslessThanNum = new ArrayList<String>();
      for (int r = 0; r < filesFromRop.size(); r++) {
        final String erbsValue = xmlTopology.getERBSValueFromFileName(filesFromRop.get(r));
        String erbsFileName;

        for (int num = minNodeNumber; num <= maxNodesNumber; num++) {
          if (num <= 9) {
            erbsFileName = "ERBS00" + "00" + num;
          } else if (num >= 10 && num <= 99) {
            erbsFileName = "ERBS00" + "0" + num;
          } else {
            erbsFileName = "ERBS00" + num;
          }
          if (!(erbsValue.indexOf(erbsFileName) == -1)) {
            erbslessThanNum.add(filesFromRop.get(r));
          }
        }
      }

      // 3.create tmp array and remove duplicate of above (2)
      final ArrayList<String> filesErbs = new ArrayList<String>();
      for (int r = 0; r < erbslessThanNum.size(); r++) {
        final String fileName = getFile.getFileNameWithDirExt(erbslessThanNum.get(r), false, false, false);
        filesErbs.add(fileName);
      }
      final String[] removeDup = common.removeDuplicatesWithOrderToStringArray(filesErbs, false);

      // 4.get files (2) from first x (numberOfNodes) nodes in tmp array
      // (3)
      final ArrayList<String> topTenNodeFiles = new ArrayList<String>();
      if (removeDup.length > numberOfNodes) {
        for (int i = 0; i < numberOfNodes; i++) {
          topTenNodeFiles.add(removeDup[i]);
        }
      } else {
        for (int i = 0; i < removeDup.length; i++) {
          topTenNodeFiles.add(removeDup[i]);
        }
      }

      for (int i = 0; i < erbslessThanNum.size(); i++) {
        final String erbslessThanNumFileName = erbslessThanNum.get(i);
        for (int r = 0; r < topTenNodeFiles.size(); r++) {
          final String removeDupFileName = topTenNodeFiles.get(r);
          if (erbslessThanNumFileName.endsWith(removeDupFileName)) {

            if (GunZipFile) {
              final String fileNameUnZipped = getFile
                  .gunzipFileIfGzFileFormatWithoutDeletingOriginalFile(erbslessThanNum.get(i));
              array.add(fileNameUnZipped);
            } else {
              final String fileNameUnZipped = erbslessThanNum.get(i);
              array.add(fileNameUnZipped);
            }
          }
        }
      }
    }
    return array;
  }

  public ArrayList<String> deltaPerRopTimeAllFilesFromAllDirOss() throws FileNotFoundException, IOException,
      SAXException, ParserConfigurationException {
    final ArrayList<String> filesProducedAtLastExec = new ArrayList<String>();
    final int date = getFile.getLatestFileDate();
    final ArrayList<String> files = getFile.getAllLatestDateFiles(true, false, false, false);

    int ropNum = getFile.getNumberOfRops();
    final String[] ropStartTimes = getFile.getRopStartTimes(files, ropNum, false);
    final String[] ropEndTimes = getFile.getRopEndTimes(files, ropNum, false);

    for (int r = 0; r < ropStartTimes.length; r++) {
      final String d = date + "";
      String f = null;
      final String sTime = ropStartTimes[r];
      final String eTime = ropEndTimes[r];
      int counter = 0;
      final ArrayList<String> tmpFileArray = new ArrayList<String>();
      for (int i = 0; i < files.size(); i++) {
        f = files.get(i);
        final int fileNameDate = getDateFromFileName(f);
        final String startTime = getStartTimeFromFileName(f);
        final String endTime = getEndTimeFromFileName(f);

        if (d.equals(fileNameDate) && sTime.equals(startTime) || eTime.equals(endTime)) {
          counter++;
          tmpFileArray.add(f);
        }
      }

      final String file = tmpFileArray.get(0);
      final ArrayList<String> tmp = getFileullFileDeltaStructureToArray(file, 3);
      final String fullString = "Rop " + ropNum + "," + sTime + "," + eTime + "," + tmp.get(0) + "," + tmp.get(1) + ","
          + tmp.get(2) + "," + tmp.get(3) + "," + tmp.get(4);

      filesProducedAtLastExec.add(fullString);
      ropNum = ropNum - 1;
      counter = 0;
    }
    Collections.sort(filesProducedAtLastExec);
    return filesProducedAtLastExec;
  }

  public void deltaToExcel(final String fileName) throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final String fileNameOssDir = getFile.getFileNameWithDirExt(fileName, true, false, false);

    arrayList.add(fileNameOssDir);
    fileDeltaOssDirStringValue(fileName, 3);
    fileDeltaFilenameOssFolderDirValueLogFileOutput(fileName, 2);
  }

  public void fileDeltaFilenameValue(final String fileName, final int compareWithNumOfHeaders) throws SAXException,
      IOException, ParserConfigurationException {
    getFileDeltaStructure(fileName, compareWithNumOfHeaders);
    final String fileOssExt = getFile.getOssDirNameFromFileName(fileName);
    final String[] arrayString = common.convertStringArrayListToStringArray(arrayList);
    arrayList.clear();

    final String fullString = fileOssExt + "," + arrayString[1] + "," + arrayString[2] + "," + arrayString[3] + ","
        + arrayString[4] + "," + arrayString[5];

    fullFileNameStringCompareArrayList.add(fullString);
  }

  public void fileDeltaFilenameOssFolderDirValueLogFileOutput(final String fileName, final int compareWithNumOfHeaders)
      throws SAXException, IOException, ParserConfigurationException {
    getFileDeltaStructure(fileName, compareWithNumOfHeaders);
    final String[] arrayString = common.convertStringArrayListToStringArray(arrayList);
    arrayList.clear();

    final String fullString = fileName + " - " + arrayString[1] + ", " + arrayString[2] + ", " + arrayString[3] + ", "
        + arrayString[4] + ", " + arrayString[5];

    fullFileNameStringCompareArrayList.add(fullString);
  }

  public void fileDeltaOssDirStringValue(final String fileName, final int compareWithNumOfHeaders) throws SAXException,
      IOException, ParserConfigurationException {
    getFileDeltaStructure(fileName, compareWithNumOfHeaders);
    final String fileOssExt = getFile.getOssDirNameFromFileName(fileName);
    final String fileDirFolder = getFile.getFileFolderNameFromFileName(fileName);
    final String[] arrayString = common.convertStringArrayListToStringArray(arrayList);
    arrayList.clear();

    final String fullString = fileOssExt + "," + fileDirFolder + "," + arrayString[1] + "," + arrayString[2] + ","
        + arrayString[3] + "," + arrayString[4] + "," + arrayString[5];

    fullStringCompareArrayList.add(fullString);
  }

  public void getFileDeltaStructure(final String fileName, final int lessHeaders) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String[] miValues = miNames(fileName);
    final int[] mvCount = new int[miValues.length];
    final String[] tmpArray = new String[deltaHeader.length - lessHeaders];

    for (int i = 0; i < tmpArray.length; i++) {
      final String str = checkIfElementExistsInArray(miValues, deltaHeader[i + lessHeaders]);
      tmpArray[i] = str;
    }

    for (int s = 0; s < mvCount.length; s++) {
      mvCount[s] = countChildTagsInParentTag("mi", "mv", s + 1);
    }

    int x = 0;
    for (int r = 0; r < tmpArray.length; r++) {
      if (tmpArray[r].equals("TRUE")) {
        final String mvCountString = mvCount[x] + "";
        arrayList.add(mvCountString);
        // System.out.println(mvCountString);

        x++;
      } else if (tmpArray[r].equals("FALSE")) {
        // System.out.println(0);
        arrayList.add("0");
      }
    }
  }

  public ArrayList<String> getFileullFileDeltaStructureToArray(String fileName, final int lessHeaders)
      throws SAXException, IOException, ParserConfigurationException {
    final File fileGz = new File(fileName);
    if (fileGz.isFile() && getFile.getFileExtensionName(fileGz).indexOf("gz") != -1) {
      final File unZipped = getFile.unGzip(fileGz, false);
      fileName = unZipped.toString();
    }

    openXmlFile(fileName);
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final String[] miValues = miNames(fileName);
    final int[] mvCount = new int[miValues.length];
    final String[] tmpArray = new String[deltaHeaderPerRop.length - lessHeaders];

    for (int i = 0; i < tmpArray.length; i++) {
      final String str = checkIfElementExistsInArray(miValues, deltaHeaderPerRop[i + lessHeaders]);
      tmpArray[i] = str;
    }

    for (int s = 0; s < mvCount.length; s++) {
      mvCount[s] = countChildTagsInParentTag("mi", "mv", s + 1);
    }

    int x = 0;
    for (int r = 0; r < tmpArray.length; r++) {
      if (tmpArray[r].equals("TRUE")) {
        final String mvCountString = mvCount[x] + "";
        tmpArrayList.add(mvCountString);
        x++;
      } else if (tmpArray[r].equals("FALSE")) {
        tmpArrayList.add("0");
      }
    }

    final File fileXmlCopy = new File(fileName);
    if (fileGz.isFile() && getFile.getFileExtensionName(fileGz).indexOf("gz") != -1
        && getFile.getFileExtensionName(fileXmlCopy).indexOf("xml") != -1) {
      fileXmlCopy.delete();
    }
    return tmpArrayList;
  }

  public String checkIfElementExistsInArray(final String[] arrayName, final String elementName) {
    final String elementExits = "FALSE";

    for (int i = 0; i < arrayName.length; i++) {
      if (elementName.equals(arrayName[i])) {
      }
    }
    return elementExits;
  }

  public String[] miNames(final String fileName) throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final int miCount = countTags("mi");
    final String[] miMoidName = new String[miCount];

    for (int r = 1; r <= miCount; r++) {
      final String[] moidValueArray = getMoidElementValuesPerMi(r);
      for (int i = 0; i < 1; i++) {
        final String[] splitComma = moidValueArray[i].split(",");
        final String[] splitEqualSign = splitComma[splitComma.length - 1].split("=");
        final String miName = splitEqualSign[0];
        miMoidName[r - 1] = miName;
        // System.out.println(miName);
      }
    }
    return miMoidName;
  }

  // /**
  // * @param numberOfNodes
  // * (Integer) - The first number of nodes that you want the files from
  // * @param minNodeNumber
  // * (Integer) - The minimum Node Number
  // * @param maxNodesNumber
  // * (Integer) - The maximum Node Number
  // * @return Latest Files from delta topology ROPs
  // * (runTimeProperties.deltaTopology_numOfRops) for the first Z number
  // * of Nodes in set of X and Y number Nodes
  // */
  //
  // public ArrayList<String>
  // getLatestFilesFromFirstZNumberOfNodeFromRopsWhereNodeBetweenXandY(final int
  // numberOfNodes,
  // final int minNodeNumber, final int maxNodesNumber, final boolean GZipFile)
  // throws FileNotFoundException,
  // IOException {
  // final int numOfRops = mz.numberOfRops(false, true);// Number of ROPs
  //
  // /** NOTE: Change 1 to numOdRops **/
  // final ArrayList<String> filesFromRops =
  // latestFilesFromXNumberOfRops(numOfRops);
  // final ArrayList<String> filesFromNodeBetweenXandY =
  // filesWhereNodeIsBetweenXandY(filesFromRops, minNodeNumber,
  // maxNodesNumber);
  //
  // ArrayList<String> fileArray = new ArrayList<String>();
  // final ArrayList<String> fileArrayUnZipped = new ArrayList<String>();
  // fileArrayUnZipped.clear();
  // fileArray.clear();
  //
  // fileArray = filesFromFirstXNumberOfNodes(filesFromNodeBetweenXandY,
  // numberOfNodes);
  //
  // if (GZipFile) {
  // final int arraysize = fileArray.size();
  // for (int i = 0; i < arraysize; i++) {
  // final String fileNameUnZipped =
  // gunzipFileIfGzFileFormatWithoutDeletingOriginalFile(fileArray.get(i));
  // fileArrayUnZipped.add(fileNameUnZipped);
  // }
  // return fileArrayUnZipped;
  // }
  // return fileArray;
  // }

  // /**
  // * @return Latest file from given number of ROP's
  // * @param numberOfRops
  // * (Integer)-Number of ROP's you require to get files from
  // */
  //
  // public ArrayList<String> latestFilesFromXNumberOfRops(final int
  // numberOfRops) throws FileNotFoundException,
  // IOException {
  // final ArrayList<String> filesFromRop = new ArrayList<String>();
  // filesFromRop.clear();
  //
  // ArrayList<String> latestDateFiles = new ArrayList<String>();
  // latestDateFiles.clear();
  //
  // latestDateFiles = mz.getAllLatestDateFilesWithFullExt();
  // final String[] ropStartTimes = ropStartTimes(); // ROP Start Times
  // final String[] ropEndTimes = ropEndTimes(); // ROP End Times
  //
  // /*** Get files from each ROP ***/
  // for (int s = 0; s < numberOfRops; s++) {
  // for (int i = 0; i < latestDateFiles.size(); i++) {
  // final String fileName = latestDateFiles.get(i);
  // if (!(fileName.indexOf(ropStartTimes[s]) == -1) &&
  // !(fileName.indexOf(ropEndTimes[s]) == -1)) {
  // filesFromRop.add(fileName);
  // }
  // }
  // }
  // return filesFromRop;
  // }

  // /**
  // * @return Files that are between X and Y Nodes
  // * @param files
  // * (String ArrayList) - List of files
  // * @param minNodeNumber
  // * (Integer)- Minimum Node Number
  // * @param maxNodeNumber
  // * (Integer)- Maximum Node Number
  // */
  //
  // public ArrayList<String> filesWhereNodeIsBetweenXandY(final
  // ArrayList<String> files, final int minNodeNumber,
  // final int maxNodeNumber) {
  // final ArrayList<String> filesFromNode = new ArrayList<String>();
  // filesFromNode.clear();
  //
  // for (int r = 0; r < files.size(); r++) {
  // final String erbsValue =
  // xmlTopology.getERBSValueFromFileName(files.get(r));
  // String erbsFileName;
  //
  // for (int num = minNodeNumber; num <= maxNodeNumber; num++) {
  // if (num <= 9) {
  // erbsFileName = "ERBS00" + "00" + num;
  // } else if (num >= 10 && num <= 99) {
  // erbsFileName = "ERBS00" + "0" + num;
  // } else {
  // erbsFileName = "ERBS00" + num;
  // }
  // if (!(erbsValue.indexOf(erbsFileName) == -1)) {
  // filesFromNode.add(files.get(r));
  // }
  // }
  // }
  // return filesFromNode;
  // }

  //
  // public ArrayList<String> filesFromFirstXNumberOfNodes(final
  // ArrayList<String> files, final int numberOfNodes)
  // throws FileNotFoundException, IOException {
  // final ArrayList<String> array = new ArrayList<String>();
  // array.clear();
  //
  // /** Create temporary array and remove duplicate from file array ***/
  // final ArrayList<String> filesFromNodes = new ArrayList<String>();
  // for (int r = 0; r < files.size(); r++) {
  // final String fileName =
  // xml.fileNameWithoutDirExtensions(files.get(r));
  // filesFromNodes.add(fileName);
  // }
  // final String[] removeDup =
  // mz.removeDuplicateWithOrderString(filesFromNodes, false);
  //
  // /*** Get files from first 10 nodes in temporary array ***/
  // final ArrayList<String> topXNumberOfNodeFiles = new ArrayList<String>();
  // if (removeDup.length > numberOfNodes) {
  // for (int i = 0; i < numberOfNodes; i++) {
  // topXNumberOfNodeFiles.add(removeDup[i]);
  // }
  // } else if (removeDup.length < numberOfNodes) {
  // for (int i = 0; i < removeDup.length; i++) {
  // topXNumberOfNodeFiles.add(removeDup[i]);
  // }
  // }
  //
  // for (int i = 0; i < files.size(); i++) {
  // final String nodesLessThanNumFileName = files.get(i);
  // for (int r = 0; r < topXNumberOfNodeFiles.size(); r++) {
  // final String removeDupFileName = topXNumberOfNodeFiles.get(r);
  // if (nodesLessThanNumFileName.endsWith(removeDupFileName)) {
  // array.add(files.get(i));
  // }
  // }
  // }
  // return array;
  // }
}
