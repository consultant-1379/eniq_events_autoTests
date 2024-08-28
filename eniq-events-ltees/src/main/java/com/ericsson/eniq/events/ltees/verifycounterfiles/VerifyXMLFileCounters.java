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

import com.ericsson.eniq.events.ltees.controllers.common.*;
import com.ericsson.eniq.events.ltees.controllers.file.*;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.*;

/**
 * @author ESAIMKH
 * @since 2011
 */
public class VerifyXMLFileCounters extends XmlController {

  // Boolean Result
  private boolean result = false;

  // Common functions & methods
  private final CommonController common = new CommonController();

  // Property file related methods
  private final PropertiesFileController propFile = new PropertiesFileController();

  // Log File related methods
  private final LogFileWriter logWriter = new LogFileWriter();

  // Directory tags from app.properties
  private final String counterPropKeyName = "CounterPropFile_Directory";

  private final String confPropKeyName = "ConfPropFile_Directory";

  private final String sampPropKeyName = "SampPropFile_Directory";

  private final String counterValuesOrdered = "CounterValuesInOrder";

  private final String counterLog = "CounterLogFile";

  /**
   * @return Boolean: Verify that necessary counters exist in XML file(s)
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyCounterNames(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {

    final String fileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String fileDirCounter = propFile.getAppPropSingleStringValue("CounterXMLPropFile_Directory");

    final String yesNo = propFile.getPropertiesSingleStringValue("counterNames", fileDir);

    if (yesNo.equalsIgnoreCase("Y")) {
      final String[] xmlFileCounter = getAllElementContent(fileName, "mt");
      final String[] xmlCounters = getAllElementContent(fileDirCounter, "mt");
      final String[] xmlFileCounterCopy = Arrays.copyOf(xmlFileCounter, xmlFileCounter.length);
      final String[] xmlCountersCopy = Arrays.copyOf(xmlCounters,xmlCounters.length);
      Arrays.sort(xmlFileCounterCopy);
      Arrays.sort(xmlCountersCopy);

      result = checkIfArrayAreEqualAndPrintPassFailToLog("Expected and Actual Counter Results Comparison",
              xmlFileCounterCopy, xmlCountersCopy);
      if(!result){
          final String counterLogFile = propFile.getAppPropSingleStringValue(counterLog);
          logWriter.writeToLogFile(counterLogFile, "\nVerify Counter Names present in XML file:");
          logWriter.writeToLogFile(counterLogFile, "Counter Names from XML file:\n"+Arrays.asList(xmlFileCounter).toString());
          logWriter.writeToLogFile(counterLogFile, "Counter Names from property file:\n"+Arrays.asList(xmlCounters).toString());

      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmRrcConnEstabSucc Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmRrcConnEstabSuccValues(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmRrcConnEstabSucc";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] succArray = getCounterValueInt(counterName, false);
      final int[] confPropSuccArray = getConfPropertiesPmRrcConnEstabSuccArray(2);

      result = checkAndPrintToLogIntArrayResult(counterName, succArray, confPropSuccArray);
    }

    return result;
  }

  /**
   * @return Boolean: Verify pmRrcConnEstabTimeSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmRrcConnEstabTimeSumValues(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmRrcConnEstabTimeSum";
    final String counterComparisonName = "pmRrcConnEstabTimeDistr";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);

      final int[] binRanges = propFile.getBinRanges("binRanges", 2);
      final int[] timeSumArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] timeSumDistrBinRangeVal = sumOfMultipleValueCounterMultipliedByBinRange(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, timeSumArray, timeSumDistrBinRangeVal);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmRrcConnEstabTimeDistr Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmRrcConnEstabTimeDistrValues(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmRrcConnEstabTimeDistr";
    final String counterComparisonName = "pmRrcConnEstabSucc";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);

      final int[] succArray = getConfPropertiesPmRrcConnEstabSuccArray(0);

      if (checkElement2) {
        final int[] timeDistrVal = sumOfMultipleValueCounter(counterName);

        result = checkAndPrintToLogIntArrayResult(counterName, timeDistrVal, succArray);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmRrcConnEstabTimeMax Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmRrcConnEstabTimeMaxValues(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmRrcConnEstabTimeMax";
    final String counterComparisonName = "pmRrcConnEstabTimeDistr";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);

      final int[] binRanges = propFile.getBinRanges("binRanges", 2);
      final int[] maxTimeArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] maxTimeDistrArray = getMaxValOfMultipleValueCounter(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);
        result = checkAndPrintToLogIntArrayResult(counterName, maxTimeArray, maxTimeDistrArray);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmS1ConnEstabTimeSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmS1ConnEstabTimeSumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmS1ConnEstabTimeSum";
    final String counterComparisonName = "pmS1ConnEstabTimeDistr";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);

      final int[] binRanges = propFile.getBinRanges("binRanges_S1Setup", 2);
      final int[] timeSumArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] timeSumDistrBinRangeVal = sumOfMultipleValueCounterMultipliedByBinRange(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, timeSumArray, timeSumDistrBinRangeVal);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmS1ConnEstabSucc Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmS1ConnEstabSuccValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmS1ConnEstabSucc";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int num = propFile.getPropertiesSingleIntValue("No_Record_InternalProcS1SetupInt", counterPropFileDir);
      final double confPropSuccValue = (num * (propFile.getPropertiesSingleIntValue("Successful_Rate",
          counterPropFileDir) / 100.00));
      final int succValueresult = (int) confPropSuccValue;

      final int[] confPropArray = new int[xmlCounterValArray.length];
      for (int i = 0; i < xmlCounterValArray.length; i++) {
        if (xmlCounterValArray[i] != 0)
          confPropArray[i] = succValueresult;
        else
          confPropArray[i] = 0;
      }
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, confPropArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmS1ConnEstabTimeDistr Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmS1ConnEstabTimeDistrValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmS1ConnEstabSucc";
    final String counterComparisonName = "pmS1ConnEstabTimeDistr";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] succArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] sumTimeDistrArray = sumOfMultipleValueCounter(getCounterValuesBasedOnMainComparisonCounterStringArray(
            counterName, counterComparisonName));

        result = checkAndPrintToLogIntArrayResult(counterName, succArray, sumTimeDistrArray);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmX2ConnEstabSucc Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmX2ConnEstabSuccValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmX2ConnEstabSucc";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int num = propFile.getPropertiesSingleIntValue("No_Record_InternalProcX2SetupInt", counterPropFileDir);
      final double confPropSuccValue = (num * (propFile.getPropertiesSingleIntValue("Successful_Rate",
          counterPropFileDir) / 100.00));
      final int succValueresult = (int) confPropSuccValue;

      final int[] confPropArray = new int[xmlCounterValArray.length];
      for (int i = 0; i < xmlCounterValArray.length; i++) {
        if (xmlCounterValArray[i] != 0)
          confPropArray[i] = succValueresult;
        else
          confPropArray[i] = 0;
      }

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, confPropArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmS1UlNasTransportSent Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmS1UlNasTransportSentValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmS1UlNasTransportSent";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int num1 = propFile.getPropertiesSingleIntValue("No_Record_S1UplinkNasTransportInt", counterPropFileDir);
      final int confPropSuccValue = num1;
      final int[] confPropArray = new int[xmlCounterValArray.length];
      for (int i = 0; i < xmlCounterValArray.length; i++) {
        if (xmlCounterValArray[i] != 0)
          confPropArray[i] = confPropSuccValue;
        else
          confPropArray[i] = 0;
      }

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, xmlCounterValArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmS1DlNasTransportRcvd Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmS1DlNasTransportRcvdValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmS1DlNasTransportRcvd";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int num1 = propFile.getPropertiesSingleIntValue("No_Record_S1DownlinkNasTransportInt", counterPropFileDir);

      final int confPropSuccValue = num1;
      final int[] confPropArray = new int[xmlCounterValArray.length];

      for (int i = 0; i < xmlCounterValArray.length; i++) {
        if (xmlCounterValArray[i] != 0)
          confPropArray[i] = confPropSuccValue;
        else
          confPropArray[i] = 0;
      }

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, xmlCounterValArray);
    }
    return result;
  }

  /**
   * @return Boolean:Verify pmUeCtxtRelTimeSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeSumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeSum";
    final String counterComparisonName = "pmUeCtxtRelTimeDistr";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] binRanges = propFile.getBinRanges("pmUeCtxtRelTimeDistr_bin_range", 0);
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] timeSumDistrBinRangeVal = new int[xmlCounterValArray.length];

      if (checkElement2) {
        final int[] tmpArray = sumOfMultipleValueCounterMultipliedByBinRange(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        final int mvCount = getMvTagBlockCount(counterName);
        final int miNumber = getMiTagNumber(counterName);

        for (int i = 0; i < mvCount; i++) {
          final String[] moidName = getMoidElementValuesPerMi(miNumber);
          if (counterOrdered.equalsIgnoreCase("y")) {
            Arrays.sort(moidName);
          }
          final int n = moidName[i].length();
          final char moidNumChar = moidName[i].charAt(n - 1);
          final String moidNumStr = moidNumChar + "";
          final int moidNum = Integer.parseInt(moidNumStr);

          final int countDistr = countMultipleValueStringLenght(counterComparisonName, ",");
          if (moidName[i].endsWith(moidNumStr)) {
            final int lessNum = countDistr * moidNum;
            timeSumDistrBinRangeVal[i] = tmpArray[i] - lessNum;
          }
        }
      }
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, timeSumDistrBinRangeVal);
    }

    return result;
  }

  /**
   * @return Boolean: Verify pmUeCtxtRelTimeSamp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeSampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    final int maxMvCount = 4;

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int num = propFile.getPropertiesSingleIntValue("No_Record_InternalProcUeCtxtReleaseEnbInt",
          counterPropFileDir);

      final int confPropSuccValue = num / maxMvCount;
      final int[] confPropArray = new int[xmlCounterValArray.length];

      for (int i = 0; i < xmlCounterValArray.length; i++) {
        if (xmlCounterValArray[i] != 0)
          confPropArray[i] = confPropSuccValue;
        else
          confPropArray[i] = 0;
      }

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, confPropArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmUeCtxtRelTimeDistr Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeDistrValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeDistr";
    final String counterComparisonName = "pmUeCtxtRelTimeSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);

    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1 && checkElement2) {
      final int[] xmlCounterValArray = getCounterValueInt(counterComparisonName, false);
      final int[] sampArray = sumOfMultipleValueCounter(getCounterValuesBasedOnMainComparisonCounterStringArray(
          "pmUeCtxtRelTimeSamp", counterName));

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmUeCtxtRelTimeS1HoSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeS1HoSumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeS1HoSum";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);

    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] arrayValues = getCalculatedSumSampValues(fileName, "pmUeCtxtRelTimeS1HoSamp");

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, arrayValues);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmUeCtxtRelTimeS1HoSamp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeS1HoSampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeS1HoSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] sampValArray = getSampValuesFromSampResultsPropFile(fileName, counterName);

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
    }
    return result;
  }
  public boolean verifyGenericCounterGroupResult(final String counterName,final String fileName, boolean printCounterValue) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String sampCounterName = counterName + "Cell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }
         
         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
            /*if(sampCounterName.contains("pmErabEstab")){
             System.out.println("Property to get ============>"+sampCounterName+ (moidOrder[i] + 1));
             System.out.println("Property to get From============>"+sampDir+"\n\n");
             }*/
             final int[] genericCounterCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < genericCounterCell.length; s++) {
               tmpArrayList.add(genericCounterCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, printCounterValue);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;
  }
  public boolean verifyPmS1ConnShutdownTimeSumValue(final String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmS1ConnShutdownTimeSum";
        final String sampCounterName = "pmS1ConnShutdownTimeSumCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmS1ConnShutdownTimeSumCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmS1ConnShutdownTimeSumCell.length; s++) {
               tmpArrayList.add(pmS1ConnShutdownTimeSumCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;
  }
  public boolean verifyPmS1ConnShutdownTimeSampValue(final String fileName) throws SAXException, IOException,
  ParserConfigurationException {
      openXmlFile(fileName);
      result = false;
      final String counterName = "pmS1ConnShutdownTimeSamp";
      final String sampCounterName = "pmS1ConnShutdownTimeSampCell";
      final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
      final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
      final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
      final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
      final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

      if(checkElement1){
      final int[] moidOrder = getMoidOrder(counterName);
      if (counterOrdered.equalsIgnoreCase("y")) {
          Arrays.sort(moidOrder);
      }

      final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
      for (int i = 0; i < moidOrder.length; i++) {
          final int[] pmS1ConnShutdownTimeSampCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
          for (int s = 0; s < pmS1ConnShutdownTimeSampCell.length; s++) {
              tmpArrayList.add(pmS1ConnShutdownTimeSampCell[s]);
          }
      }

      if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
      }
      }
      return result; 
  }
  public boolean verifyPmS1ConnShutdownTimeDistrValue(final String fileName) throws SAXException, IOException,
  ParserConfigurationException {
      openXmlFile(fileName);
      result = false;
      final String counterName = "pmS1ConnShutdownTimeDistr";
      final String sampCounterName = "pmS1ConnShutdownTimeDistrCell";
      final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
      final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
      final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
      final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
      final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

      if(checkElement1){
      final int[] moidOrder = getMoidOrder(counterName);
      if (counterOrdered.equalsIgnoreCase("y")) {
          Arrays.sort(moidOrder);
      }

      final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
      for (int i = 0; i < moidOrder.length; i++) {
          final int[] pmS1ConnShutdownTimeDistrCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
          for (int s = 0; s < pmS1ConnShutdownTimeDistrCell.length; s++) {
              tmpArrayList.add(pmS1ConnShutdownTimeDistrCell[s]);
          }
      }

      if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
      }
      }
      return result; 
  }
  /**
   * @return Boolean: Verify pmUeCtxtRelTimeX2HoSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeX2HoSumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeX2HoSum";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);

    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] arrayValues = getCalculatedSumSampValues(fileName, "pmUeCtxtRelTimeX2HoSamp");

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, arrayValues);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmUeCtxtRelTimeX2HoSamp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmUeCtxtRelTimeX2HoSampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmUeCtxtRelTimeX2HoSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] sampValArray = getSampValuesFromSampResultsPropFile(fileName, counterName);

      /*** Null Value Array ***/
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
    }
    return result;
  }
  public boolean verifyPmHoExecSuccDrxValue(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        final String counterName = "pmHoExecSuccDrx";
        final String sampCounterName = "pmHoExecSuccDrxCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmHoExecSuccDrxCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmHoExecSuccDrxCell.length; s++) {
               tmpArrayList.add(pmHoExecSuccDrxCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }

  public boolean verifyPmX2ConnEstabFailIn(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmX2ConnEstabFail";
        final String sampCounterName = "pmX2ConnEstabFailCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

         if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmX2ConnEstabFailCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmX2ConnEstabFailCell.length; s++) {
               tmpArrayList.add(pmX2ConnEstabFailCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
         }
        return result;


    }
  public boolean verifyPmS1ConnEstabFail(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmS1ConnEstabFail";
        final String sampCounterName = "pmS1ConnEstabFailCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){ 
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmS1NasNonDelivIndCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmS1NasNonDelivIndCell.length; s++) {
               tmpArrayList.add(pmS1NasNonDelivIndCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }
  public boolean verifyPmS1NasNonDelivInd(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmS1NasNonDelivInd";
        final String sampCounterName = "pmS1NasNonDelivIndCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmS1NasNonDelivIndCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmS1NasNonDelivIndCell.length; s++) {
               tmpArrayList.add(pmS1NasNonDelivIndCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }
  public boolean verifyPmRrcConnEstabFail(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmRrcConnEstabFail";
        final String sampCounterName = "pmRrcConnEstabFailCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmRrcConnEstabFailCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmRrcConnEstabFailCell.length; s++) {
               tmpArrayList.add(pmRrcConnEstabFailCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }

  public boolean verifyPmHoExecAttDrxValue(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmHoExecAttDrx";
        final String sampCounterName = "pmHoExecAttDrxCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmHoExecAttDrxCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmHoExecAttDrxCell.length; s++) {
               tmpArrayList.add(pmHoExecAttDrxCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }

  public boolean verifyPmTimingAdvance(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmTimingAdvance";
        final String sampCounterName = "pmTimingAdvanceCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmTimingAdvanceCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmTimingAdvanceCell.length; s++) {
               tmpArrayList.add(pmTimingAdvanceCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, true);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }

  public boolean verifyPmTaDistr(String fileName) throws SAXException, IOException,
  ParserConfigurationException {
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmTaDistr";
        final String sampCounterName = "pmTaDistrCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmTadistrCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmTadistrCell.length; s++) {
               tmpArrayList.add(pmTadistrCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, true);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;


    }

  public boolean verifyPmX2ErrorIndOutGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmX2ErrorIndOut";
        final String sampCounterName = "pmX2ErrorIndOutCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmX2ErrorIndOutCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmX2ErrorIndOutCell.length; s++) {
               tmpArrayList.add(pmX2ErrorIndOutCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmX2ErrorIndInGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmX2ErrorIndIn";
        final String sampCounterName = "pmX2ErrorIndInCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmX2ErrorIndInCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmX2ErrorIndInCell.length; s++) {
               tmpArrayList.add(pmX2ErrorIndInCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmX2ConnResetInGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmX2ConnResetIn";
        final String sampCounterName = "pmX2ConnResetInCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }
         
         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmX2ConnResetInCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmX2ConnResetInCell.length; s++) {
               tmpArrayList.add(pmX2ConnResetInCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmX2ConnResetOutGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmX2ConnResetOut";
        final String sampCounterName = "pmX2ConnResetOutCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmX2ConnResetOutCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmX2ConnResetOutCell.length; s++) {
               tmpArrayList.add(pmX2ConnResetOutCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmS1ErrorIndMmeGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmS1ErrorIndMme";
        final String sampCounterName = "pmS1ErrorIndMmeCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmS1ErrorIndMmeCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmS1ErrorIndMmeCell.length; s++) {
               tmpArrayList.add(pmS1ErrorIndMmeCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmS1ErrorIndEnbGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmS1ErrorIndEnb";
        final String sampCounterName = "pmS1ErrorIndEnbCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmS1ErrorIndEnbCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmS1ErrorIndEnbCell.length; s++) {
               tmpArrayList.add(pmS1ErrorIndEnbCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmErabRelSuccQciGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmErabRelSuccQci";
        final String sampCounterName = "pmErabRelSuccQciCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmErabRelSuccQciCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmErabRelSuccQciCell.length; s++) {
               tmpArrayList.add(pmErabRelSuccQciCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmErabRelAttQciGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmErabRelAttQci";
        final String sampCounterName = "pmErabRelAttQciCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmErabRelAttQciCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmErabRelAttQciCell.length; s++) {
               tmpArrayList.add(pmErabRelAttQciCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }
  public boolean verifyPmErabRelFailGroupResult(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmErabRelFail";
        final String sampCounterName = "pmErabRelFailCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmErabRelFailCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmErabRelFailCell.length; s++) {
               tmpArrayList.add(pmErabRelFailCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }

  public boolean verifyPmUeCtxtRelS1ResetValue(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmUeCtxtRelS1Reset";
        final String sampCounterName = "pmUeCtxtRelS1ResetCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmUeCtxtRelS1ResetCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmUeCtxtRelS1ResetCell.length; s++) {
               tmpArrayList.add(pmUeCtxtRelS1ResetCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;
        }

  public boolean verifyPmErabEstabFailInitCauseValue(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmErabEstabFailInit";
        final String sampCounterName = "pmErabEstabFailInitCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
        
        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmErabEstabFailInitCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmErabEstabFailInitCell.length; s++) {
               tmpArrayList.add(pmErabEstabFailInitCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }


public boolean verifyPmErabEstabFailAddedRADIO_NETWORK_LAYER_GROUPCauseValue(
            String fileName) throws SAXException, IOException,
              ParserConfigurationException{
        openXmlFile(fileName);
        result = false;
        final String counterName = "pmErabEstabFailAdded";
        final String sampCounterName = "pmErabEstabFailAddedCell";
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
        final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
        final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
        final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

        if(checkElement1){
         final int[] moidOrder = getMoidOrder(counterName);
         if (counterOrdered.equalsIgnoreCase("y")) {
           Arrays.sort(moidOrder);
         }

         final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>(); 
         for (int i = 0; i < moidOrder.length; i++) {
             final int[] pmErabEstabFailAddedCell = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
             for (int s = 0; s < pmErabEstabFailAddedCell.length; s++) {
               tmpArrayList.add(pmErabEstabFailAddedCell[s]);
             }
           }

        if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
          final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
          final int[] sampValArray = common.convertIntegerArrayListToIntArray(tmpArrayList);

          result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampValArray);
        }
        }
        return result;

    }

  /**
   * @return Boolean: Verify pmErabEstabTimeInitSumQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeInitSumQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmErabEstabTimeInitSumQci";
    final String counterComparisonName = "pmErabEstabTimeInitDistrQci";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] binRanges = propFile.getBinRanges("binRangesPmErabID", 0);
      final int[] timeSumArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] timeSumDistrBinRangeVal = sumOfMultipleValueCounterMultipliedByBinRange(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, timeSumArray, timeSumDistrBinRangeVal);
      }
    }
    return result;
  }

  /**
   * @return Boolean:Verify pmErabEstabTimeInitSampQci Values - Removed from XML
   *         Counter file
   * @param fileName
   *          (String): Name of XML File
   */
  // public boolean verifyPmErabEstabTimeInitSampQciValue(final String fileName)
  // throws SAXException, IOException,
  // ParserConfigurationException {
  // openXmlFile(fileName);
  // final String counterName = "pmErabEstabTimeInitSampQci";
  // final String counterDistrName = "pmErabEstabTimeInitDistrQci";
  // final String counterPropFileDir =
  // propFile.getAppPropSingleStringValue(counterPropKeyName);
  // final String yesNo = propFile.getPropertiesSingleStringValue(counterName,
  // counterPropFileDir);
  // final boolean checkElement1 =
  // checkifCounterExistsAndWriteToLogFile(counterName, fileName);
  //
  // if (yesNo.equalsIgnoreCase("y") && checkElement1) {
  // final int[] xmlCounterValArray = getCounterValueInt(counterName, true);
  // final int[] sampArray = sumOfMultipleValueCounter(counterDistrName, true);
  //
  // /*** Null Value Array ***/
  // // final int[] array2 = new int[0];
  // result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray,
  // sampArray);
  // }
  // return result;
  // }

  /**
   * @return Boolean: Verify pmErabEstabTimeInitDistrQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeInitDistrQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    result = false;
    final String counterName = "pmErabEstabTimeInitDistrQci";
    final String sampCounterName = "pmErabEstabTimeInitSampQciCell";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);

    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    if(checkElement1){
    final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>();
    final int[] moidOrder = getMoidOrder(counterName);
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(moidOrder);
    }
    for (int i = 0; i < moidOrder.length; i++) {
      final int[] distrQCI = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
      for (int s = 0; s < distrQCI.length; s++) {
        tmpArrayList.add(distrQCI[s]);
      }
    }

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = sumOfMultipleValueCounter(counterName);
      final int[] sampArray = common.convertIntegerArrayListToIntArray(tmpArrayList);
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray);
    }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmErabEstabTimeInitMaxQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeInitMaxQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmErabEstabTimeInitMaxQci";
    final String counterComparisonName = "pmErabEstabTimeInitDistrQci";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] binRanges = propFile.getBinRanges("binRangesPmErabID", 0);
      final int[] maxTimeArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] maxTimeDistrArray = getMaxValOfMultipleValueCounter(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, maxTimeArray, maxTimeDistrArray);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmErabEstabTimeAddedSumQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeAddedSumQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmErabEstabTimeAddedSumQci";
    final String counterComparisonName = "pmErabEstabTimeAddedDistrQci";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] binRanges = propFile.getBinRanges("binRangesPmErabAD", 1);
      final int[] timeSumArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] timeSumDistrBinRangeVal = sumOfMultipleValueCounterMultipliedByBinRange(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, timeSumArray, timeSumDistrBinRangeVal);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmErabEstabTimeAddedSampQci Values - Removed from
   *         XML Counter file
   * @param fileName
   *          (String): Name of XML File
   */
  // public boolean verifyPmErabEstabTimeAddedSampQciValue(final String
  // fileName) throws SAXException, IOException,
  // ParserConfigurationException {
  // openXmlFile(fileName);
  // final String counterName = "pmErabEstabTimeAddedSampQci";
  // final String counterDistrName = "pmErabEstabTimeAddedDistrQci";
  // final String counterPropFileDir =
  // propFile.getAppPropSingleStringValue(counterPropKeyName);
  // final String yesNo = propFile.getPropertiesSingleStringValue(counterName,
  // counterPropFileDir);
  // final boolean checkElement1 =
  // checkifCounterExistsAndWriteToLogFile(counterName, fileName);
  //
  // if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
  // final int[] xmlCounterValArray = getCounterValueInt(counterName, true);
  // final int[] sampArray = sumOfMultipleValueCounter(counterDistrName, true);
  //
  // /*** Null Value Array ***/
  // // final int[] array2 = new int[0];
  // result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray,
  // sampArray);
  // }
  // return result;
  // }

  /**
   * @return Boolean: Verify pmErabEstabTimeAddedDistrQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeAddedDistrQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    result = false;
    final String counterName = "pmErabEstabTimeAddedDistrQci";
    final String sampCounterName = "pmErabEstabTimeAddedSampQciCell";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final String sampDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    if(checkElement1){
    final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>();
    final int[] moidOrder = getMoidOrder(counterName);
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(moidOrder);
    }
    for (int i = 0; i < moidOrder.length; i++) {
      final int[] distrQCI = propFile.getPropertiesMultipleIntValues(sampCounterName + (moidOrder[i] + 1), sampDir);
      for (int s = 0; s < distrQCI.length; s++) {
        tmpArrayList.add(distrQCI[s]);
      }
    }

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = sumOfMultipleValueCounter(counterName);
      final int[] sampArray = common.convertIntegerArrayListToIntArray(tmpArrayList);
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray);
    }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmErabEstabTimeAddedMaxQci Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmErabEstabTimeAddedMaxQciValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmErabEstabTimeAddedMaxQci";
    final String counterComparisonName = "pmErabEstabTimeAddedDistrQci";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterComparisonName);
      final int[] binRanges = propFile.getBinRanges("binRangesPmErabAD", 1);
      final int[] maxTimeArray = getCounterValueInt(counterName, false);

      if (checkElement2) {
        final int[] maxTimeDistrArray = getMaxValOfMultipleValueCounter(
            getCounterValuesBasedOnMainComparisonCounterStringArray(counterName, counterComparisonName), binRanges);

        result = checkAndPrintToLogIntArrayResult(counterName, maxTimeArray, maxTimeDistrArray);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmHoSigExeOutTimeS1Sum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmHoSigExeOutTimeS1SumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmHoSigExeOutTimeS1Sum";
    final String sampCounterName = "pmHoSigExeOutTimeS1Samp";
    final String sampDistrKeyTag = "pmHoSigExeOutTimeS1SampDISTR";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String sampPropFileDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] xmlSampValArray = getCounterValueInt(sampCounterName, false);
      final int distrVal = propFile.getPropertiesSingleIntValue(sampDistrKeyTag, sampPropFileDir);
      // System.out.println("pmHoSigExeOutTimeS1SampDISTR Value: " + distrVal);
      final int[] sumArray = new int[xmlCounterValArray.length];

      for (int i = 0; i < xmlSampValArray.length; i++) {
        int sum = 0;
        sum = xmlSampValArray[i] * distrVal;
        sumArray[i] = sum;
      }

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sumArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmHoSigExeOutTimeS1Samp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmHoSigExeOutTimeS1SampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmHoSigExeOutTimeS1Samp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);

      /*** Null Value Array ***/
      final int[] sampArray1 = getPmHoSigExeOutTimeSampArray(fileName, false, true, true);
      final int[] sampArray2 = getPmHoSigExeOutTimeSampArray(fileName, false, true, false);

      if (common.checkIf2ArraysContainSameElements(xmlCounterValArray, sampArray1)) {
        result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray1);
      } else if (common.checkIf2ArraysContainSameElements(xmlCounterValArray, sampArray2)) {
        result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray2);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmHoSigExeOutTimeX2Sum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmHoSigExeOutTimeX2SumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmHoSigExeOutTimeX2Sum";
    final String sampCounterName = "pmHoSigExeOutTimeX2Samp";
    final String sampDistrKeyTag = "pmHoSigExeOutTimeX2SampDISTR";
    final String sampPropFileDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] xmlSampValArray = getCounterValueInt(sampCounterName, false);
      final int distrVal = propFile.getPropertiesSingleIntValue(sampDistrKeyTag, sampPropFileDir);
      final int[] sumArray = new int[xmlCounterValArray.length];

      for (int i = 0; i < xmlSampValArray.length; i++) {
        int sumVal = 0;
        sumVal = xmlSampValArray[i] * distrVal;
        sumArray[i] = sumVal;
      }
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sumArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmHoSigExeOutTimeX2Samp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmHoSigExeOutTimeX2SampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmHoSigExeOutTimeX2Samp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);

      /*** Null Value Array ***/
      final int[] sampArray1 = getPmHoSigExeOutTimeSampArray(fileName, true, false, true);
      final int[] sampArray2 = getPmHoSigExeOutTimeSampArray(fileName, true, false, false);

      if (common.checkIf2ArraysContainSameElements(xmlCounterValArray, sampArray1)) {
        result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray1);
      } else if (common.checkIf2ArraysContainSameElements(xmlCounterValArray, sampArray2)) {
        result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray2);
      }
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmProcessorLoadSum Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmProcessorLoadSumValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmProcessorLoadSum";
    final String counterNameSamp = "pmProcessorLoadSamp";

    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterNameSamp);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1 && checkElement2) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] tmpArray = getCounterValueInt(counterNameSamp, false);
      int sum = 0;
      for (int i = 1; i <= tmpArray[0]; i++) {
        sum = sum + i;
      }

      final int[] sumArray = { sum };
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sumArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmProcessorLoadSamp Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmProcessorLoadSampValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmProcessorLoadSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String confPropFileDir = propFile.getAppPropSingleStringValue(confPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int internPropLoad = propFile.getPropertiesSingleIntValue("No_Record_InternalPerProcessorLoadInt",
          confPropFileDir);
      // final int sampVal = internPropLoad;
      final int[] sampArray = { internPropLoad };
      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampArray);
    }
    return result;
  }

  /**
   * @return Boolean: Verify pmProcessorLoadMax Values
   * @param fileName
   *          (String): Name of XML File
   */
  public boolean verifyPmProcessorLoadMaxValue(final String fileName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String counterName = "pmProcessorLoadMax";
    final String counterNameSamp = "pmProcessorLoadSamp";
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);

    final String yesNo = propFile.getPropertiesSingleStringValue(counterName, counterPropFileDir);
    final boolean checkElement1 = checkifCounterExistsAndWriteToLogFile(fileName, counterName);
    final boolean checkElement2 = checkifCounterExistsAndWriteToLogFile(fileName, counterNameSamp);

    if (yesNo.equalsIgnoreCase("Y") && checkElement1 && checkElement2) {
      final int[] xmlCounterValArray = getCounterValueInt(counterName, false);
      final int[] sampXMLArray = getCounterValueInt(counterNameSamp, false);

      result = checkAndPrintToLogIntArrayResult(counterName, xmlCounterValArray, sampXMLArray);
    }
    return result;
  }

  /**
   * @return Boolean: Compare & Check String[] Arrays and Output Result
   *         PASS/FAIL
   * @param resultText
   *          (String): Text to write to result
   * @param array1
   *          (Integer Array []): XML File Counter value results (Original File
   *          Results)
   * @param array2
   *          (Integer Array []): Calculated/Comparison results
   */
  public boolean checkIfArrayAreEqualAndPrintPassFailToLog(final String resultText, final String[] array1,
      final String[] array2) throws FileNotFoundException, IOException {
    final String counterLogFile = propFile.getAppPropSingleStringValue(counterLog);
    final boolean check = Arrays.equals(array1, array2);
    String resultPF = null;
    String logEntry = "\n" + resultText + ":  ";

    if (check == true) {
      resultPF = "PASS";
    } else if (check == false) {
      resultPF = "FAIL";
      logEntry = logEntry + resultPF;
      logWriter.writeToLogFile(counterLogFile, logEntry);
    }
    return check;
  }

  /**
   * @return Boolean: Compare & Check Integer[] Arrays and Output Result
   *         PASS/FAIL
   * @param counterNameToVerify
   *          (String): Counter name to be verified
   * @param array1
   *          (Integer Array []): XML File Counter value results (Original File
   *          Results)
   * @param array2
   *          (Integer Array []): Calculated/Comparison results
   */
  @SuppressWarnings("unused")
  public boolean checkAndPrintToLogIntArrayResult(final String counterNameToVerify, final int[] array1,
      final int[] array2) throws FileNotFoundException, IOException {
    final String counterLogFile = propFile.getAppPropSingleStringValue(counterLog);
    final int[] array1Copy = Arrays.copyOf(array1, array1.length);
    final int[] array2Copy = Arrays.copyOf(array2,array2.length);
    Arrays.sort(array1Copy);
    Arrays.sort(array2Copy);
    final boolean check = Arrays.equals(array1Copy, array2Copy);
    String resultPF = null;
    
    String logEntry_1 = "XML File Values -  ";
    String logEntry_2 = "Comparison/Calculated Values -  ";

    if (check == true) {
      resultPF = "PASS";
    } else if (check == false) {
      resultPF = "FAIL";

      if (array1.length != 0) {
        for (int a1 = 0; a1 < array1.length; a1++) {
          logEntry_1 = logEntry_1 + array1[a1] + ",";
        }
      } else {
        logEntry_1 = "Null Comparison: Null Value Array";
      }

      if (array2.length != 0) {
        for (int a2 = 0; a2 < array2.length; a2++) {
          logEntry_2 = logEntry_2 + array2[a2] + ", ";
        }
      } else {
        logEntry_2 = "Null Comparison: Null Value Array";
      }
      logWriter.writeToLogFile(counterLogFile, "\nVerify " + counterNameToVerify + " Counter:");
      logWriter.writeToLogFile(counterLogFile, logEntry_1);
      logWriter.writeToLogFile(counterLogFile, logEntry_2);
      
      
      // Cell values printing for automation
      
      if (array1.length != 0 && (array1.length % 4 == 0)) {
          for(int i=0;i<=3;i++){
              String cell = "Cell"+(i+1) +": ";
              for(int j=0; j<array1.length / 4;j++){
                  if(i*(array1.length/4)+j < array1.length){
                      cell += array1[i*(array1.length/4)+j] +",";
                  }
              }
              logWriter.writeToLogFile(counterLogFile, cell);
          }
       }

      
      
    }
    return check;
  }

  /**
   * @return INT Array []: Get the Full pmRrcConnEstabSucc values extracted from
   *         from conf_LTE.properties file & XML file.
   * @param lessCounters
   *          (Integer): Less Counters from Full Array based on MV Block Count
   *          and Counter Values extracted from XML file
   */
  public int[] getConfPropertiesPmRrcConnEstabSuccArray(final int lessCounters) throws SAXException, IOException,
      ParserConfigurationException {
    // openXmlFile(file);
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final String[] causeCodeCellArray = propFile.getCauseCellArrayString();
    final String confPropDir = propFile.getAppPropSingleStringValue(confPropKeyName);
    final int successRate = propFile.getPropertiesSingleIntValue("Successful_Rate", confPropDir);

    final int[] sortOrderArray = getMoidSortOrder("pmRrcConnEstabSucc");
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(sortOrderArray);
    }

    for (int i = 0; i < sortOrderArray.length; i++) {
      if (sortOrderArray[i] == 0) {
        for (int s = 0 + lessCounters; s < 5; s++) {
          tmpArrayList.add(causeCodeCellArray[s]);
        }
      } else if (sortOrderArray[i] == 1) {
        for (int s = 5 + lessCounters; s < 10; s++) {
          tmpArrayList.add(causeCodeCellArray[s]);
        }
      } else if (sortOrderArray[i] == 2) {
        for (int s = 10 + lessCounters; s < 15; s++) {
          tmpArrayList.add(causeCodeCellArray[s]);
        }
      } else if (sortOrderArray[i] == 3) {
        for (int s = 15 + lessCounters; s < 20; s++) {
          tmpArrayList.add(causeCodeCellArray[s]);
        }
      }
    }
    final String[] tmpStringArray = new String[tmpArrayList.size()];
    final int[] tmpIntArray = new int[tmpStringArray.length];
    for (int r = 0; r < tmpIntArray.length; r++) {
      tmpStringArray[r] = tmpArrayList.get(r);
      final double val = (Integer.parseInt(tmpStringArray[r]) * successRate) / 100;
      tmpIntArray[r] = (int) (val);
    }
    return tmpIntArray;
  }

  /**
   * @return INT Array []: Get the calculated sum values of Samp Values
   * @param fileName
   *          (String): Name of XML file
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmUeCtxtRelTimeS1HoSamp"
   */
  public int[] getCalculatedSumSampValues(final String fileName, final String counterNameContains) throws SAXException,
      IOException, ParserConfigurationException {
    openXmlFile(fileName);
    // final String counterName = "pmUeCtxtRelTimeS1HoSamp";
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final String sampPropFileDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final ArrayList<Integer> arrayListInt = new ArrayList<Integer>();
    final int miTagNum = getMiTagNumber(counterNameContains);

    final String[] mvCellNames = getMoidElementValuesPerMi(miTagNum);
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(mvCellNames);
    }
    final int[] xmlCounterValArray = getCounterValueInt(counterNameContains, false);
    final String[] binRangeName = { "binRangeCell1", "binRangeCell2", "binRangeCell3", "binRangeCell4" };

    final int maxCellNumber = 4;
    for (int r = 0; r < mvCellNames.length; r++) {
      int sumValue = 0;
      for (int s = 1; s <= maxCellNumber; s++) {
        final String moidCelNum = s + "";
        if (mvCellNames[r].endsWith(moidCelNum)) {
          int binRangeStartNum;
          if (xmlCounterValArray[r] % 2 == 0) {
            binRangeStartNum = 1;
          } else {
            binRangeStartNum = 2;
          }

          final int[] propnumStep = propFile.getPropertiesMultipleIntValuesEveryXvalue(binRangeName[s - 1],
              binRangeStartNum, 2, sampPropFileDir);

          for (int i = 0; i < xmlCounterValArray[r]; i++) {
            sumValue = sumValue + propnumStep[i];
          }
        }
      }
      arrayListInt.add(sumValue);
    }

    final int[] array = common.convertIntegerArrayListToIntArray(arrayListInt);
    return array;
  }

  /**
   * @return INT [] : Get Samp Values from the sampResults.properties file
   * @param fileName
   *          (String): String name of the XML file
   * @param propCounterName
   *          (String): Counter Name relevant to the Samp Values in Properties
   *          File
   */
  public int[] getSampValuesFromSampResultsPropFile(final String fileName, final String propCounterName)
      throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final String sampResutsFileDir = propFile.getAppPropSingleStringValue(sampPropKeyName);
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final int[] tmpArray = propFile.getPropertiesMultipleIntValues(propCounterName, sampResutsFileDir);

    final int miTagNum = getMiTagNumber(propCounterName);
    final String[] mvCellNames = getMoidElementValuesPerMi(miTagNum);
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(mvCellNames);
    }
    final int[] xmlCounterValArray = getCounterValueInt(propCounterName, false);
    final int[] sampValArray = new int[xmlCounterValArray.length];

    final int maxCellNumber = 4;
    for (int r = 0; r < xmlCounterValArray.length; r++) {
      for (int s = 1; s <= maxCellNumber; s++) {
        final String moidCelNum = s + "";

        if (mvCellNames[r].endsWith(moidCelNum)) {
          sampValArray[r] = tmpArray[s - 1];
        }
      }
    }
    return sampValArray;
  }

  /**
   * @return Integer Array - Samp values for PmHoSigExeOutTimeSamp S1 or X2
   * @param fileName
   *          (String): XML File Name
   * @param X2
   *          (Boolean): True/False to get Samp values for PmHoSigExeOutTimeSamp
   *          X2
   * @param S1
   *          (Boolean): True/False to get Samp values for PmHoSigExeOutTimeSamp
   *          S1
   * @param Order
   *          (Boolean): True Or False to get 2 different value order types
   * @Note There must be True,False or False/True combination in parameters
   *       (X2,S1). If S1 and X2 are both False, then results for S1 will be
   *       returned
   */
  public int[] getPmHoSigExeOutTimeSampArray(final String fileName, final boolean X2, final boolean S1,
      final boolean Order) throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);// Open File
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    // Part String for Counter to be verified
    final String CounterNamePartStr = "pmHoSigExeOutTime";
    // Part String for External and Internal MOID values
    final String externalMoidStr = "EUtranCellRelation=EXTLTE";
    final String internalMoidStr = "EUtranCellRelation=INTLTE";

    /** Internal & External Cell Relation Samp Values for S1 **/
    // Internal Cell Relation Samp Values for S1
    final Object s1Internal[][] = {
        { "C1C2", "C1C3", "C1C4", "C2C1", "C2C3", "C2C4", "C3C1", "C3C2", "C3C4", "C4C1", "C4C2", "C4C3" },
        { 4, 2, 3, 4, 3, 2, 6, 7, 2, 3, 4, 5 } };
    // External Cell Relation Samp Values for S1
    final int[] s1C1External = { 3, 7 };
    final int[] s1C2External = { 8, 1 };

    /** Internal & External Cell Relation Samp Values for X2 **/
    // Internal Cell Relation for X2
    final Object x2Internal[][] = {
        { "C1C2", "C1C3", "C1C4", "C2C1", "C2C3", "C2C4", "C3C1", "C3C2", "C3C4", "C4C1", "C4C2", "C4C3" },
        { 8, 4, 6, 8, 6, 4, 12, 14, 4, 6, 8, 10 } };
    // External Cell Relation Samp Values for X1
    final int[] x1C1External = { 6, 14 };
    final int[] x1C2External = { 16, 2 };

    // Get MOID Value Array and Sort Array
    final int miTagNumber = getMiTagNumber(CounterNamePartStr);
    final String[] moidValues = getMoidElementValuesPerMi(miTagNumber);
    if (counterOrdered.equalsIgnoreCase("y")) {
      Arrays.sort(moidValues);
    }

    /** Create Empty Arrays used for Copying **/
    Object[][] internalArray = new Object[2][12];
    int[] externalArrayC1 = new int[2];
    int[] externalArrayC2 = new int[2];
    final int[] resultArray = new int[moidValues.length];

    /** Use relevant array based on boolean parameters X2, S1 **/
    // If X2 is true
    if (X2 && !(S1)) {
      internalArray = x2Internal;
      externalArrayC1 = x1C1External;
      externalArrayC2 = x1C2External;
      // if X2 is not true
    } else {
      internalArray = s1Internal;
      externalArrayC1 = s1C1External;
      externalArrayC2 = s1C2External;
    }

    /** Get Results in MOID Value order and populate final resultArray **/
    // Location reference for external values
    int extCountC1Order1 = 0;// Order 1 - External C1
    int extCountC2Order1 = 0;// Order 1 - External C2
    int extCountC1Order2 = 1;// Order 2 - External C1
    int extCountC2Order2 = 1;// Order 2 - External C2

    /** Get external MOID values to array **/
    final String[] moid = getMoidElementsContainingString(CounterNamePartStr, externalMoidStr, true);

    for (int i = 0; i < moidValues.length; i++) {
      /** Get values for External Cell Relation and add to resultsArray **/
      final String moidValueToLower = moidValues[i].toLowerCase();
      if (moidValueToLower.contains(externalMoidStr.toLowerCase())) {
        for (int m = 0; m < moid.length; m++) {
          // External C1 Values
          if (moidValueToLower.equalsIgnoreCase(moid[m]) && moidValueToLower.endsWith("c1")) {
            if (Order) {// Order 1
              resultArray[i] = externalArrayC1[extCountC1Order1];
              extCountC1Order1++;
              break;
            } else if (!Order) {// Order 2
              resultArray[i] = externalArrayC1[extCountC1Order2];
              extCountC1Order2--;
              break;
            }
            // External C2 Values
          } else if (moidValueToLower.equalsIgnoreCase(moid[m]) && moidValueToLower.endsWith("c2")) {
            if (Order) {// Order 1
              resultArray[i] = externalArrayC2[extCountC2Order1];
              extCountC2Order1++;
              break;
            } else if (!Order) {// Order 2
              resultArray[i] = externalArrayC2[extCountC2Order2];
              extCountC2Order2--;
              break;
            }
          }
        }
      }
      /** Get values for Internal Cell Relation and add to resultsArray **/
      else if (moidValueToLower.contains(internalMoidStr.toLowerCase())) {
        // Get last 4 characters of the MOID Value - i.e. C1C2
        final String str = moidValueToLower.substring(moidValueToLower.length() - 4, moidValueToLower.length());
        // Get value for specified MOID value from 2 dimensional array
        for (int k = 0; k < internalArray[1].length; k++) {
          final String intStrToLower = internalArray[0][k].toString().toLowerCase();
          if (intStrToLower.equalsIgnoreCase(str)) {
            resultArray[i] = Integer.parseInt(internalArray[1][k].toString());
            break;
          }
        }
      }
    }
    return resultArray;
  }

  /**
   * @return Boolean - Write to log file if counter value does not exit in XML
   *         file
   * @param fielName
   *          - String file name
   * @param xmlCounterName
   *          - Counter Name in XML file
   */
  private boolean checkifCounterExistsAndWriteToLogFile(final String fileName, final String xmlCounterName)
      throws SAXException, IOException, ParserConfigurationException {
    final String tagName = "mt";// XML tagName for Counter Values
    final String counterLogFile = propFile.getAppPropSingleStringValue(counterLog);
    final String counterPropFile = propFile.getAppPropSingleStringValue("CounterPropFile_Directory");
    final boolean check = checkIfElementContentExists(fileName, xmlCounterName, tagName);

    // print to log file if element does not exist
    final String yesNo = propFile.getPropertiesSingleStringValue(xmlCounterName, counterPropFile);
    if (yesNo.equalsIgnoreCase("Y") && check == false) {
      logWriter.writeToLogFile(counterLogFile, "\n" + xmlCounterName + " Node Value does NOT exist in xml file. File name : " + fileName);
    }
    return check;
  }

  /**
   * @return INT Array []: Get the sum of Samp Values from the
   *         sampResults.properties file based on given parameters
   */
  // public int[] getSumOfSampValueFromPropFile(final String sampCounterName,
  // final String binRangeName,
  // final int startNum, final int step, final int arrayLenght) throws
  // FileNotFoundException, IOException {
  // final String sampPropFileDir =
  // propFile.getAppPropSingleStringValue(sampPropKeyName);
  // final int[] sampValue =
  // propFile.getPropertiesMultipleIntValues(sampCounterName, sampPropFileDir);
  // final int[] propnumStep =
  // propFile.getPropertiesMultipleIntValuesEveryXvalue(binRangeName, startNum,
  // step,
  // sampPropFileDir);
  //
  // int sumValue = 0;
  // final int[] tmpArray = new int[arrayLenght];
  // final int len = sampValue.length;
  //
  // if (len >= 2) {
  // for (int i = 0; i < tmpArray.length; i++) {
  // sumValue = sumValue + propnumStep[i];
  // tmpArray[i] = sumValue;
  // }
  // } else if (len == 1) {
  // sumValue = sumValue + propnumStep[0];
  // for (int r = 0; r < arrayLenght; r++) {
  // tmpArray[r] = sumValue;
  // }
  // }
  //
  // return tmpArray;
  // }
}
