/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.verifydeltatopology;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.controllers.file.*;

/**
 * @author ESAIMKH
 * @since 2011
 * 
 */
public class VerifyDeltaTopology extends DeltaFileController {

  private final XmlController xml = new XmlController();

  /**
   * @return Overall Delta Topology Results - combined results to delta topology
   *         with 2 different array parameters
   * 
   */
  public ArrayList<String> DeltaTopologyOverallResults() throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    // final ArrayList<String> DeltaResult = new ArrayList<String>();
    final ArrayList<String> array1 = DeltaTopologyResultsArrayType1();
    // final ArrayList<String> array2 = DeltaTopologyResultsArrayType2();
    // DeltaResult = common.combineArrays(array1, array2, 7, 1);

    return array1;
  }

  /**
   * Cell Relation - For odd numbered ROP, delete 2 internal relations (C3C1 and
   * C4C1)
   * 
   * @param fileName
   * @return
   */
  public boolean checkCellRelationOddRopDelete2InternalRelations(final String fileName) throws FileNotFoundException,
      IOException, SAXException, ParserConfigurationException {
    boolean result = false;
    final String moidVal = "EUtranCellRelation";
    final String internalCellRelation1 = "C3C1";
    final String internalCellRelation2 = "C4C1";

    final boolean check1 = checkIfMoidExitsInFile(fileName, moidVal, internalCellRelation1);
    final boolean check2 = checkIfMoidExitsInFile(fileName, moidVal, internalCellRelation2);
    final int moidCount1 = countMoidValue(fileName, moidVal, internalCellRelation1);
    final int moidCount2 = countMoidValue(fileName, moidVal, internalCellRelation2);

    if (!check1 && !check2 && moidCount1 == 0 && moidCount2 == 0)
      result = true;

    return result;
  }

  /**
   * Cell Relation - For odd numbered ROP, delete 2 external relations
   * (N1C1-N2C1 and N1C2-N2C2)
   * 
   * @param fileName
   * @return
   */
  public boolean checkCellRelationOddRopDelete2ExternalRelations(final String fileName) throws FileNotFoundException,
      IOException, SAXException, ParserConfigurationException {
    // numberOfExternalCellRelationsOddRopDelta = 2;
    boolean result = false;
    final Boolean check1 = checkIfExternalEUtranCellRelationExists(fileName, "C1", "C1");
    final Boolean check2 = checkIfExternalEUtranCellRelationExists(fileName, "C2", "C2");
    final int countC1C1 = countExternalEUtranCellRelation(fileName, "C1", "C1");
    final int countC2C2 = countExternalEUtranCellRelation(fileName, "C2", "C2");

    if (check1 && check2 && countC1C1 == 1 && countC2C2 == 1)
      result = true;

    return result;
  }

  /**
   * Cell Relation - For even numbered ROP, re-create 2 internal relations (C3C1
   * and C4C1)
   * 
   * @param fileName
   * @return
   */
  public boolean checkCellRelationEvenRopRecreate2InternalRelations(final String fileName) throws SAXException,
      IOException, ParserConfigurationException {
    // numberOfInternalCellRelationsEvenRopDelta = 12;
    boolean result = false;
    final String moidVal = "EUtranCellRelation=INTLTE";
    final String internalCellRelation1 = "C3C1";
    final String internalCellRelation2 = "C4C1";

    final boolean check1 = checkIfMoidExitsInFile(fileName, moidVal, internalCellRelation1);
    final boolean check2 = checkIfMoidExitsInFile(fileName, moidVal, internalCellRelation2);
    final int moidCount1 = countMoidValue(fileName, moidVal, internalCellRelation1);
    final int moidCount2 = countMoidValue(fileName, moidVal, internalCellRelation2);

    if (check1 && check2 && moidCount1 == 1 && moidCount2 == 1)
      result = true;
    return result;
  }

  /**
   * Cell Relation - For even numbered ROP, re-create 2 external relations
   * (N1C1-N2C1 and N1C2-N2C2)
   * 
   * @param fileName
   * @return
   */
  public boolean checkCellRelationEvenRopRecreate2ExternalRelations(final String fileName)
      throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
    // numberOfExternalCellRelationsEvenRopDelta = 4;
    boolean result = false;

    final Boolean check1 = checkIfExternalEUtranCellRelationExists(fileName, "C1", "C1");
    final Boolean check2 = checkIfExternalEUtranCellRelationExists(fileName, "C2", "C2");
    final int countC1C1 = countExternalEUtranCellRelation(fileName, "C1", "C1");
    final int countC2C2 = countExternalEUtranCellRelation(fileName, "C2", "C2");

    if (check1 && check2 && countC1C1 == 2 && countC2C2 == 2)
      result = true;

    return result;
  }

  /**
   * ENodeBFunction - For Odd numbered ROP, Delete 1 External ENodeBFunction
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkOddRopDelete1ExternalENodeBFunction(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal = "ExternalENodeBFunction";
    final boolean check = checkIfMoidElementValueExists(fileName, moidVal);

    if (!check)
      result = true;

    return result;
  }

  /**
   * EUtranCellFDD - For Odd numbered ROP, Delete 2 External EUtranCellFDD
   * 
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkOddRopDelete2ExternalEUtranCellFDD(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;

    final Boolean check1 = checkIfExternalEUtranCellRelationExists(fileName, "C1", "C1");
    final Boolean check2 = checkIfExternalEUtranCellRelationExists(fileName, "C2", "C2");
    final int countC1C1 = countExternalEUtranCellRelation(fileName, "C1", "C1");
    final int countC2C2 = countExternalEUtranCellRelation(fileName, "C2", "C2");

    if (check1 && check2 && countC1C1 <= 1 && countC2C2 <= 1) {
      result = true;
    }

    return result;
  }

  /**
   * TermPointToENB - For Odd numbered ROP, Delete 1 TermPointToENB
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkOddRopDelete1TermPointToENB(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal = "TermPointToENB";
    int count = 0;

    final boolean check1 = checkIfMoidElementValueExists(fileName, moidVal);
    count = xml.countMvTagsBasedOnMoidValue(fileName, moidVal);

    if (!check1 && count == 0)
      result = true;

    return result;
  }

  /**
   * ENodeBFunction - For Even numbered ROP, Recreate 1 External ENodeBFunction
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  private boolean checkEvenRopRecreate1ExternalENodeBFunction(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal = "ExternalENodeBFunction";
    final boolean check = checkIfMoidElementValueExists(fileName, moidVal);

    if (check)
      result = true;

    return result;
  }

  /**
   * EUtranCellFDD - For Even numbered ROP, Recreate 2 External EUtranCellFDD
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  private boolean checkEvenRopRecreate2ExternalEUtranCellFDD(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;

    final Boolean check1 = checkIfExternalEUtranCellRelationExists(fileName, "C1", "C1");
    final Boolean check2 = checkIfExternalEUtranCellRelationExists(fileName, "C2", "C2");
    final int countC1C1 = countExternalEUtranCellRelation(fileName, "C1", "C1");
    final int countC2C2 = countExternalEUtranCellRelation(fileName, "C2", "C2");

    if (check1 && check2 && countC1C1 == 2 && countC2C2 == 2) {
      result = true;
    }

    return result;
  }

  /**
   * TermPointToENB - For Even numbered ROP, Recreate 1 TermPointToENB
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkEvenRopRecreate1TermPointToENB(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal = "TermPointToENB";
    int count = 0;

    final boolean check1 = checkIfMoidElementValueExists(fileName, moidVal);
    count = xml.countMvTagsBasedOnMoidValue(fileName, moidVal);

    if (check1 && count == 1)
      result = true;

    return result;
  }

  /**
   * TermPointToMme - For Odd numbered ROP, Delete 1 TermPointToMme MV block
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkOddRopDelete1TermPointToMme(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal1 = "TermPointToMme=MME";
    int count = 0;

    final boolean check1 = checkIfMoidElementValueExists(fileName, moidVal1);
    count = xml.countMvTagsBasedOnMoidValue(fileName, moidVal1);

    if (!check1 && count == 0) {
      result = true;
    }
    return result;
  }

  /**
   * TermPointToMme - For Even numbered ROP, Recreate 1 TermPointToMme MV block
   * 
   * @param fileName
   * @return
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public boolean checkEvenRopRecreate1TermPointToMme(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    boolean result = false;
    final String moidVal1 = "TermPointToMme=MME";
    int count = 0;

    final boolean check1 = checkIfMoidElementValueExists(fileName, moidVal1);
    count = xml.countMvTagsBasedOnMoidValue(fileName, moidVal1);

    if (check1 && count == 1)
      result = true;

    return result;
  }

  /**
   * @return Delta Topology Results for Array Type 1 - Array Type 1 is
   *         Applicable to first 10 nodes in set 1 to 24 (240 nodes altogether
   * 
   */
  public ArrayList<String> DeltaTopologyResultsArrayType1() throws FileNotFoundException, IOException, SAXException,
      ParserConfigurationException {
    /*** START - Remove all .XML files from latest Date files ***/
    final ArrayList<String> latestDateFiles = getFile.getAllLatestDateFiles(true, false, false, false);
    getFile.deleteFilesWithExtension(latestDateFiles, "xml");
    /*** END ***/

    final ArrayList<String> tmpArrayList = new ArrayList<String>();// Temporary
    final ArrayList<String> resultsArrayList = new ArrayList<String>();// Results
    final ArrayList<String> array1 = getFilesFromRopForFirstXNodesUnZipped(10, 1, 24, true);

    String result = "FAIL"; // Initiate string Result as PASS

    final String[] ropStart = ropStartTimes(); // ROP Start Times
    final String[] ropEnd = ropEndTimes(); // ROP End Times

    for (int rop = 0; rop < ropStart.length; rop++) {
      final int ropNum = rop + 1;
      final String ropNumber = ropNum + "";// Get RopNumber
      final int oddEvenRop = ropNum % 2;

      // Add RopNumber to String ArrayList
      resultsArrayList.add(ropNumber);

      // Add ROP Start-Time to String ArrayList
      resultsArrayList.add(ropStart[rop]);

      // Add ROP End-Time to String ArrayList
      resultsArrayList.add(ropEnd[rop]);

      /*** Get Overall EUtranCellRelation Results **/
      for (int f1 = 0; f1 < array1.size(); f1++) {
        final String fileName = array1.get(f1);
        if (fileName.indexOf(ropStart[rop]) != -1 && fileName.indexOf(ropEnd[rop]) != -1) {
          final boolean EUtranCellRelationOdd1 = checkCellRelationOddRopDelete2InternalRelations(fileName);
          final boolean EUtranCellRelationOdd2 = checkCellRelationOddRopDelete2ExternalRelations(fileName);
          final boolean EUtranCellRelationEven1 = checkCellRelationEvenRopRecreate2InternalRelations(fileName);
          final boolean EUtranCellRelationEven2 = checkCellRelationEvenRopRecreate2ExternalRelations(fileName);

          if (oddEvenRop == 1 && EUtranCellRelationOdd1 && EUtranCellRelationOdd2) {
            result = "PASS";
          } else if (oddEvenRop == 0 && EUtranCellRelationEven1 && EUtranCellRelationEven2) {
            result = "PASS";
          } else {
            result = "FAIL";
          }
          tmpArrayList.add(result);
          result = "FAIL";// Revert result back to FAIL
        }
      }
      getResultsFromTmpArrayAndAddToMainResultArray(tmpArrayList, resultsArrayList);

      /** Get Overall ENodeBFunction Results **/
      for (int f1 = 0; f1 < array1.size(); f1++) {
        final String fileName = array1.get(f1);
        if (fileName.indexOf(ropStart[rop]) != -1 && fileName.indexOf(ropEnd[rop]) != -1) {
          final boolean ENodeBFunctionOdd1 = checkOddRopDelete1ExternalENodeBFunction(fileName);
          final boolean ENodeBFunctionEven2 = checkEvenRopRecreate1ExternalENodeBFunction(fileName);

          if (oddEvenRop == 1 && ENodeBFunctionOdd1) {
            result = "PASS";
          } else if (oddEvenRop == 0 && ENodeBFunctionEven2) {
            result = "PASS";
          } else {
            result = "FAIL";
          }
          tmpArrayList.add(result);
          result = "FAIL";// Revert result back to FAIL
        }
      }
      getResultsFromTmpArrayAndAddToMainResultArray(tmpArrayList, resultsArrayList);
      // System.out.println("File number " + fileN +
      // " ENodeBFunction Result (test2): " + test2);

      /** Get Overall EUtranCellFDD Results **/
      for (int f1 = 0; f1 < array1.size(); f1++) {
        final String fileName = array1.get(f1);
        xml.openXmlFile(fileName);
        if (fileName.indexOf(ropStart[rop]) != -1 && fileName.indexOf(ropEnd[rop]) != -1) {
          final boolean EUtranCellFDDOdd1 = checkOddRopDelete2ExternalEUtranCellFDD(fileName);
          final boolean EUtranCellFDDEven2 = checkEvenRopRecreate2ExternalEUtranCellFDD(fileName);

          if (oddEvenRop == 1 && EUtranCellFDDOdd1) {
            result = "PASS";
          } else if (oddEvenRop == 0 && EUtranCellFDDEven2) {
            result = "PASS";
          } else {
            result = "FAIL";
          }
          tmpArrayList.add(result);
          result = "FAIL";// Revert result back to FAIL
        }
      }
      getResultsFromTmpArrayAndAddToMainResultArray(tmpArrayList, resultsArrayList);
      // System.out.println("File number " + fileN +
      // " EUtranCellFDD Result (test3): " + test3);

      /** Get Overall TermPointToENB Results **/
      for (int f1 = 0; f1 < array1.size(); f1++) {
        final String fileName = array1.get(f1);
        if (fileName.indexOf(ropStart[rop]) != -1 && fileName.indexOf(ropEnd[rop]) != -1) {
          final boolean TermPointToENBOdd1 = checkOddRopDelete1TermPointToENB(fileName);
          final boolean TermPointToENBEven1 = checkEvenRopRecreate1TermPointToENB(fileName);

          if (oddEvenRop == 1 && TermPointToENBOdd1) {
            result = "PASS";
          } else if (oddEvenRop == 0 && TermPointToENBEven1) {
            result = "PASS";
          } else {
            result = "FAIL";
          }
          tmpArrayList.add(result);
          result = "FAIL";// Revert result back to FAIL
        }
      }
      getResultsFromTmpArrayAndAddToMainResultArray(tmpArrayList, resultsArrayList);
      // System.out.println("File number " + fileN +
      // " EUtranCellFDD Result (test4): " + test4);
    }
    /*** Delete all extracted files ***/
    getFile.deleteFilesWithExtension(array1, "XML");

    return resultsArrayList;
  }

  /**
   * @return Delta Topology Results for Array Type 2 - Array Type 2 is
   *         Applicable to first 15 nodes in set 1 to 25 (15 nodes altogether)
   * 
   */
  // public ArrayList<String> DeltaTopologyResultsArrayType2() throws
  // SAXException, IOException,
  // ParserConfigurationException {
  // /*** START - Remove all .XML files from latest Date files ***/
  // final ArrayList<String> latestDateFiles =
  // getFile.getAllLatestDateFiles(true, false, false, false);
  // getFile.deleteFilesWithExtension(latestDateFiles, "xml");
  // /*** END ***/
  //
  // final ArrayList<String> array2 =
  // getFilesFromRopForFirstXNodesUnZipped(15, 25, 25, true);
  // final ArrayList<String> tmpArrayList = new ArrayList<String>();//
  // Temporary
  // final ArrayList<String> resultsArrayList = new ArrayList<String>();//
  // Results
  //
  // final String[] ropStart = ropStartTimes(); // ROP Start Times
  // final String[] ropEnd = ropEndTimes(); // ROP End Times
  //
  // String result = "FAIL";// Initiate string Result as PASS
  //
  // /** Get Overall TermPointToMMEResults **/
  // for (int rop = 0; rop < ropStart.length; rop++) {
  // final int ropNum = rop + 1;
  // final int oddEvenRop = ropNum % 2;
  // for (int f1 = 0; f1 < array2.size(); f1++) {
  // final String fileName = array2.get(f1);
  // if (fileName.indexOf(ropStart[rop]) != -1 &&
  // fileName.indexOf(ropEnd[rop]) != -1) {
  // final boolean TermPointToMmeOdd1 =
  // checkOddRopDelete1TermPointToMme(fileName);
  // final boolean TermPointToMmeEven1 =
  // checkEvenRopRecreate1TermPointToMme(fileName);
  //
  // if (oddEvenRop == 1 && TermPointToMmeOdd1) {
  // result = "PASS";
  // } else if (oddEvenRop == 0 && TermPointToMmeEven1) {
  // result = "PASS";
  // } else {
  // result = "FAIL";
  // }
  // tmpArrayList.add(result);
  // result = "FAIL";// Revert result back to FAIL
  // }
  // }
  // getResultsFromTmpArrayAndAddToMainResultArray(tmpArrayList,
  // resultsArrayList);
  // }
  //
  // /*** Delete all extracted files ***/
  // getFile.deleteFilesWithExtension(array2, "XML");
  // return resultsArrayList;
  // }

  /**
   * @return Void - Print results to Delta Topology LogFile
   */
  public void printToLogFile() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
    final boolean deleteLatestDateGZippedFiles = true;

    final String deltaTopologyLogFileDir = propFile.getAppPropSingleStringValue("DeltaTopologyLogFile");
    logWriter.createNewLogFile(deltaTopologyLogFileDir, htmlResultWriter.getTestTimestamp() + "\n", false, false);

    final String[] ropStart = ropStartTimes(); // ROP Start Times
    final String[] ropEnd = ropEndTimes(); // ROP End Times
    logWriter.writeToLogFile(deltaTopologyLogFileDir, "Total Number of ROPS: " + ropStart.length);

    logWriter
        .writeToLogFile(
            deltaTopologyLogFileDir,
            "\nThe numbers beside the file name represents xml file <MV> block count in the following order:\n1.EUtranCellFDD; 2.EUtranCellRelation; 3.TermPointToMme; 4.TermPointToENB; 5.ProcessorLoad\n");

    int count = 0;

    final ArrayList<String> arrayType1 = getFilesFromRopForFirstXNodesUnZipped(10, 1, 24, deleteLatestDateGZippedFiles);

    for (int r1 = 0; r1 < ropStart.length; r1++) {
      final int oddEvenRop = (r1 + 1) % 2;
      final int ropNum = (r1 + 1);

      logWriter.writeToLogFile(deltaTopologyLogFileDir, "\n");
      logWriter.logFileDivider(deltaTopologyLogFileDir, "*", true);
      logWriter.writeToLogFile(deltaTopologyLogFileDir, "ROP " + ropNum + ": Start Time[" + ropStart[r1]
          + "] - End Time[" + ropEnd[r1] + "]"
          + "\n[Applicable to first 10 nodes in set 1 to 24 (240 nodes altogether)]");
      logWriter.logFileDivider(deltaTopologyLogFileDir, "*", true);

      for (int f1 = 0; f1 < arrayType1.size(); f1++) {
        final String fileName = arrayType1.get(f1);
        if (fileName.indexOf(ropStart[r1]) != -1 && fileName.indexOf(ropEnd[r1]) != -1) {
          count++;

          final String fileNameAndDelta = writeFileDeltaToLogFile(fileName);
          logWriter.writeToLogFile(deltaTopologyLogFileDir, "\n" + count + ": " + fileNameAndDelta + "\n");

          /** EUtranCellRelation Delta Results **/
          final boolean EUtranCellRelation1 = checkCellRelationOddRopDelete2InternalRelations(fileName);
          final boolean EUtranCellRelation2 = checkCellRelationOddRopDelete2ExternalRelations(fileName);
          final boolean EUtranCellRelation3 = checkCellRelationEvenRopRecreate2InternalRelations(fileName);
          final boolean EUtranCellRelation4 = checkCellRelationEvenRopRecreate2ExternalRelations(fileName);

          // Odd ROP - EUtranCellRelation
          if (oddEvenRop == 1) {
            if (!EUtranCellRelation1 || !EUtranCellRelation2) {
              logWriter
                  .writeToLogFile(deltaTopologyLogFileDir, "[ODD ROP] EUtranCellRelation Delta Topology Changes: ");

              if (!EUtranCellRelation1) {// Internal Cell Relation
                logWriter.writeToLogFile(deltaTopologyLogFileDir,
                    "**FAILED: For odd numbered ROP, delete 2 internal relations (C3C1 and C4C1)");
              }

              if (!EUtranCellRelation2) {// External Cell Relation
                logWriter.writeToLogFile(deltaTopologyLogFileDir,
                    "**FAILED: For odd numbered ROP, delete 2 external relations (N1C1-N2C1 and N1C2-N2C2)");
              }
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          // Even ROP - EUtranCellRelation
          if (oddEvenRop == 0) {
            if (!EUtranCellRelation3 || !EUtranCellRelation4) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "[EVEN ROP] EUtranCellRelation Delta Topology Changes: ");

              if (!EUtranCellRelation3) {// Internal Cell Relation
                logWriter.writeToLogFile(deltaTopologyLogFileDir,
                    "**FAILED: For even numbered ROP, re-create 2 internal relations (C3C1 and C4C1)");
              }

              if (!EUtranCellRelation4) {// External Cell Relation
                logWriter.writeToLogFile(deltaTopologyLogFileDir,
                    "**FAILED: For even numbered ROP, re-create 2 external relations (N1C1-N2C1 and N1C2-N2C2)");
              }
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          /** ENodeBFunction Delta Results **/
          final boolean ENodeBFunction1 = checkOddRopDelete1ExternalENodeBFunction(fileName);
          final boolean ENodeBFunction2 = checkEvenRopRecreate1ExternalENodeBFunction(fileName);

          // Odd ROP - ENodeBFunction
          if (oddEvenRop == 1) {
            if (!ENodeBFunction1) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[ODD ROP] ENodeBFunction Delta Topology Changes:");
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "**FAILED: For odd numbered ROP, delete 1 ExternalENodeBFunction (1)");
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          // Even ROP - ENodeBFunction
          if (oddEvenRop == 0) {
            if (!ENodeBFunction2) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[EVEN ROP] ENodeBFunction Delta Topology Changes:");
              if (!ENodeBFunction2) {
                logWriter.writeToLogFile(deltaTopologyLogFileDir,
                    "**FAILED: For even numbered ROP, re-create 1 ExternalENodeBFunction (1) ");
                logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
              }
            }
          }

          /** EUtranCellFDD Delta Results **/
          final boolean EUtranCellFDD1 = checkOddRopDelete2ExternalEUtranCellFDD(fileName);
          final boolean EUtranCellFDD2 = checkEvenRopRecreate2ExternalEUtranCellFDD(fileName);

          // Odd ROP - EUtranCellFDD
          if (oddEvenRop == 1) {
            if (!EUtranCellFDD1) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[ODD ROP] EUtranCellFDD Delta Topology Changes:");
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "**FAILED: For odd numbered ROP, delete 2 ExternalEUtranCellFDD (N2C1 and N2C2)");
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          // Even ROP - EUtranCellFDD
          if (oddEvenRop == 0) {
            if (!EUtranCellFDD2) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[EVEN ROP] EUtranCellFDD Delta Topology Changes:");
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "**FAILED: For even numbered ROP, re-create 2 ExternalEUtranCellFDD (N2C1 and N2C2)");
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          /** TermPointToENB Results **/
          final boolean TermPointToENB1 = checkOddRopDelete1TermPointToENB(fileName);
          final boolean TermPointToENB2 = checkEvenRopRecreate1TermPointToENB(fileName);

          if (oddEvenRop == 1) {
            if (!TermPointToENB1) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[ODD ROP] TermPointToENB Delta Topology Changes:");
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "**FAILED: For odd numbered ROP, delete 1 TermPointToENB (20)");
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }

          if (oddEvenRop == 0) {
            if (!TermPointToENB2) {
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "[EVEN ROP] TermPointToENB Delta Topology Changes:");
              logWriter.writeToLogFile(deltaTopologyLogFileDir,
                  "**FAILED: For even numbered ROP, re-create 1 TermPointToENB (20)");
              logWriter.writeToLogFile(deltaTopologyLogFileDir, "");
            }
          }
          logWriter.logFileDivider(deltaTopologyLogFileDir, "_", true);
        }
      }
    }

    /*** EXECUTE @ END --- Delete XML Files if not required --- END ***/
    if (deleteLatestDateGZippedFiles) {
      getFile.deleteFilesWithExtension(arrayType1, "XML");
    }
    // final ArrayList<String> arrayType2 =
    // getFilesFromRopForFirstXNodesUnZipped(15, 1, 25,
    // deleteLatestDateGZippedFiles);
    //
    // for (int r2 = 0; r2 < ropStart.length; r2++) {
    // for (int f2 = 0; f2 < arrayType2.size(); f2++) {
    // final String fileName = arrayType2.get(f2);
    //
    // final int oddEvenRop = (r2 + 1) % 2;
    //
    // if (fileName.indexOf(ropStart[r2]) != -1 && fileName.indexOf(ropEnd[r2])
    // != -1) {
    // if (f2 == 0) {
    // logWriter.writeToLogFile(deltaTopologyLogFileDir, "\n");
    // logWriter.logFileDivider(deltaTopologyLogFileDir, "*", true);
    // logWriter.writeToLogFile(deltaTopologyLogFileDir, "ROP " + (r2 + 1) +
    // ": Start Time[" + ropStart[r2]
    // + "] - End Time[" + ropEnd[r2] + "]"
    // +
    // "\n[Applicable to first 15 nodes in set 1 to 25 (15 nodes altogether)]");
    // logWriter.logFileDivider(deltaTopologyLogFileDir, "*", true);
    // }
    //
    // count++;
    // final String fileNameAndDelta = writeFileDeltaToLogFile(fileName);
    // logWriter.writeToLogFile(deltaTopologyLogFileDir, "\n" + count + ": " +
    // fileNameAndDelta + "\n");
    // final boolean TermPointToMme1 =
    // checkOddRopDelete1TermPointToMme(fileName);
    // final boolean TermPointToMme2 =
    // checkEvenRopRecreate1TermPointToMme(fileName);
    //
    // if (!TermPointToMme1 || !TermPointToMme2) {
    // logWriter.writeToLogFile(deltaTopologyLogFileDir,
    // "TermPointToMme Delta Topology Changes: ");
    // if (oddEvenRop == 1 && !TermPointToMme1) {
    // logWriter.writeToLogFile(deltaTopologyLogFileDir,
    // "**FAILED: For odd numbered ROP, delete 1 TermPointToMme");
    // }
    //
    // if (oddEvenRop == 0 && !TermPointToMme2) {
    // logWriter.writeToLogFile(deltaTopologyLogFileDir,
    // "**FAILED: For even numbered ROP, recreate 1 TermPointToMme");
    // }
    // }
    // logWriter.logFileDivider(deltaTopologyLogFileDir, "_", true);
    // }
    // }
    // }
    // /*** EXECUTE @ END --- Delete XML Files if not required --- END ***/
    // if (deleteLatestDateGZippedFiles) {
    // getFile.deleteFilesWithExtension(arrayType2, "XML");
    // }
  }

  /**
   * @return Check & Clear ArrayList - Check temporary ArrayList and get the
   *         overall results (based on all relevant files) and add overall
   *         result to MAIN Delta result ArrayList
   * @param tmpCheckArrayList
   *          ArrayList (String) - Pass Temporary ArrayList to get overall
   *          PASS/FAIL results
   * @param ResultsArrayList
   *          ArrayList (String) - Add overall temporary ArrayList result to
   *          main results
   * 
   */
  public void getResultsFromTmpArrayAndAddToMainResultArray(final ArrayList<String> tmpCheckArrayList,
      final ArrayList<String> ResultsArrayList) {
    String tmpResult = "PASS";
    final String checkValue = "FAIL";

    // Add overall EUtranCellRelation result to DeltaResult ArrayList
    if (tmpCheckArrayList.contains(checkValue)) {
      tmpResult = checkValue;
      ResultsArrayList.add(tmpResult);
    } else {
      ResultsArrayList.add(tmpResult);
    }
    tmpCheckArrayList.clear();// Clear tmpArrayList
    tmpResult = "PASS";// Revert tmpResult back to PASS
  }

  public String writeFileDeltaToLogFile(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    // final String deltaTopologyLogFileDir =
    // propFile.getAppPropSingleStringValue("DeltaTopologyLogFile");
    final ArrayList<String> resultArray = new ArrayList<String>();
    final int miTagCount = xml.countTags("mi");

    final String[] tmpArray = new String[6];
    tmpArray[0] = fileName;// File Name
    xml.openXmlFile(fileName);// Open File

    // EUtranCellFDD
    for (int i = 1; i <= miTagCount; i++) {
      final boolean checkEUtranCellFDD = xml.checkIfMoidElementValuesContainString(i, "EUtranCellFDD",
          "EUtranCellRelation", true);
      tmpArray[1] = "0";
      if (checkEUtranCellFDD) {
        tmpArray[1] = xml.countChildTagsInParentTag("mi", "mv", i) + "";
        break;
      }
    }

    // EUtranCellRelation
    for (int i = 1; i <= miTagCount; i++) {
      final boolean checkEUtranCellRelation = xml.checkIfMoidElementValuesContainString(i, "EUtranCellRelation");
      tmpArray[2] = "0";
      if (checkEUtranCellRelation) {
        tmpArray[2] = xml.countChildTagsInParentTag("mi", "mv", i) + "";
        break;
      }
    }

    // TermPointToMme
    for (int i = 1; i <= miTagCount; i++) {
      final boolean checkTermPointToMme = xml.checkIfMoidElementValuesContainString(i, "TermPointToMme");
      tmpArray[3] = "0";
      if (checkTermPointToMme) {
        tmpArray[3] = xml.countChildTagsInParentTag("mi", "mv", i) + "";
        break;
      }
    }

    // TermPointToENB
    for (int i = 1; i <= miTagCount; i++) {
      final boolean checkTermPointToENB = xml.checkIfMoidElementValuesContainString(i, "TermPointToENB");
      tmpArray[4] = "0";
      if (checkTermPointToENB) {
        tmpArray[4] = xml.countChildTagsInParentTag("mi", "mv", i) + "";
        break;
      }
    }

    // ProcessorLoad
    for (int i = 1; i <= miTagCount; i++) {
      final boolean checkProcessorLoad = xml.checkIfMoidElementValuesContainString(i, "ProcessorLoad");
      tmpArray[5] = "0";
      if (checkProcessorLoad) {
        tmpArray[5] = xml.countChildTagsInParentTag("mi", "mv", i) + "";
        break;
      }
    }

    // Full String Result
    final String fullString = tmpArray[0] + ": " + tmpArray[1] + "," + tmpArray[2] + "," + tmpArray[3] + ","
        + tmpArray[4] + "," + tmpArray[5];
    resultArray.add(fullString);

    return fullString;
  }
}
