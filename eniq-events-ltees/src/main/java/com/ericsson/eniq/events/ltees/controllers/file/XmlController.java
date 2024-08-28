/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.controllers.file;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.controllers.common.*;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.LogFileWriter;

/**
 * @author ESAIMKH
 * @since 2011
 */
public class XmlController {

  // Document
  private Document doc;

  private final CommonController common = new CommonController();

  private final PropertiesFileController propFile = new PropertiesFileController();

  private final String counterValuesOrdered = "CounterValuesInOrder";
  
  private final LogFileWriter logWriter = new LogFileWriter();
  private final String counterLog = "CounterLogFile";
  
  public static final ArrayList<String> counterList = new ArrayList<String>();
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // COMMON XML FILE METHODS
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * COMMON XML File Method
   * 
   * @return Open XML File
   * @param fileName
   *          (String): XML File Name
   */
  public void openXmlFile(final String fileName) throws SAXException, IOException, ParserConfigurationException {
    final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    doc = docBuilder.parse(new File(fileName));
  }

  /**
   * COMMON XML File Method
   * 
   * @return Integer - Count XML File Tag Names [tagName]
   * @param tagName
   *          (String): Name of XML element/tag - i.e "mt"
   */
  public int countTags(final String tagName) throws SAXException, IOException, ParserConfigurationException {
    doc.getDocumentElement().normalize();
    // rootElementTag = doc.getDocumentElement().getNodeName();
    final NodeList xmlTags = doc.getElementsByTagName(tagName);
    final int result = xmlTags.getLength();
    return result;
  }

  /**
   * COMMON XML File Method
   * 
   * @return Boolean - check if counter value [xmlCounterName] exits in XML file
   *         from a given XML Tag Name [tagName]
   * 
   * @param fileName
   *          (String): XML File Name
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to check if
   *          it exists - i.e. "pmRrcConnEstabSucc"
   * @param tagName
   *          (String): XML File Tag Name - i.e. "mt"
   */
  public boolean checkIfElementContentExists(final String fileName, final String counterNameContains,
      final String tagName) throws SAXException, IOException, ParserConfigurationException {
    final String[] allElements = getAllElementContent(fileName, tagName);
    boolean result = false;

    for (int i = 0; i < allElements.length; i++) {
      final String val = allElements[i];
      if (val.toLowerCase().contains(counterNameContains.toLowerCase())) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * COMMON XML File Method
   * 
   * @return Integer - Count XML File Tags <> based of Parent [parentTagName]
   *         and Child Tag [childTagName]
   * @param parentTagName
   *          (String): XML Parent TagName (Main Tag) - i.e. "mi"
   * @param childTagName
   *          (String): XML Child TagName (child tag contained inside parent
   *          tag) - i.e. "mv"
   * @param parentTagNumber
   *          (Integer): Parent tag number (XML File can contain more than 1
   *          Parent tag block)
   */
  public int countChildTagsInParentTag(final String parentTagName, final String childTagName, final int parentTagNumber)
      throws SAXException, IOException, ParserConfigurationException {
    final NodeList xmlTags = doc.getElementsByTagName(parentTagName);
    final Element tagElement = (Element) xmlTags.item(parentTagNumber - 1);
    final NodeList tag = tagElement.getElementsByTagName(childTagName);
    final int result = tag.getLength();
    return result;
  }

  /**
   * COMMON XML File Method
   * 
   * @return String Array [] - List of XML element content for given Tag Name
   *         [tagName]
   * @param fileName
   *          (String): XML File Name
   * @param tagName
   *          (String): XML Tag Name - i.e. "mt"
   */
  public String[] getElementContentForTagName(final String fileName, final String tagName) throws SAXException,
      IOException, ParserConfigurationException {
    openXmlFile(fileName);
    doc.getDocumentElement().normalize();
    // rootElementTag = doc.getDocumentElement().getNodeName();
    final NodeList xmlTags = doc.getElementsByTagName(tagName);

    final String[] resultArray = new String[xmlTags.getLength()];
    for (int s = 0; s < xmlTags.getLength(); s++) {
      final Element rElement = (Element) xmlTags.item(s);
      final NodeList elementList = rElement.getChildNodes();
      resultArray[s] = elementList.item(0).getTextContent() + "";
    }
    return resultArray;
  }

  /**
   * COMMON XML File Method
   * 
   * @return String - XML Element content for given Unique (where there is no
   *         more than one XML Tag Name) Tag Name [tagName]
   * @param fileName
   *          (String): XML File Name
   * @param tagName
   *          (String): XML Tag Name - i.e. "ffv"
   */
  public String getElementContentForUniqueTagName(final String fileName, final String tagName) throws SAXException,
      IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final String[] tmpArray = getElementContentForTagName(fileName, tagName);
    final String result = tmpArray[0];
    return result;
  }

  /**
   * COMMON XML File Method
   * 
   * @return String Array [] - Get All Counter Names in XML File
   * @param fileName
   *          (String): XML File Name
   * @param tagName
   *          (String): XML Tag Name - i.e. "mt"
   */
  public String[] getAllElementContent(final String fileName, final String tagName) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    final String[] resultArray;

    final NodeList mtTags = doc.getElementsByTagName(tagName);
    final int totalValues = mtTags.getLength();
    resultArray = new String[totalValues];

    for (int i = 0; i < totalValues; i++) {
      final Element xmlElement = (Element) mtTags.item(i);
      final NodeList textCountList = xmlElement.getChildNodes();
      resultArray[i] = textCountList.item(0).getTextContent() + "";
    }
    return resultArray;
  }

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // XML COUNTER FILE RELATED METHODS
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * @return Integer: Get Date From File Name
   * @param fileName
   *          (String): XML File Name
   */
  public int getDateFromFileName(final String fileName) {
    final String[] splitDot = fileName.split("[.]");
    final String fullString = splitDot[0];
    final int stringLen = fullString.length();
    final String subStringDate = fullString.substring(stringLen - 8, stringLen);

    final int date = Integer.parseInt(subStringDate);
    return date;
  }

  /**
   * @return String Array[]: Get Start Times from files in an ArrayList
   * @param fileName
   *          (ArrayList<?>): String or File ArrayList with file names (filename
   *          & directory)
   */
  public String[] getStartTimeFromFileName(final ArrayList<?> fileName) throws FileNotFoundException, IOException {
    final String[] startTimeArray = new String[fileName.size()];

    for (int i = 0; i < fileName.size(); i++) {
      final Object filesObj = fileName.get(i);
      final String fileStr = filesObj.toString();
      final String[] splitByDot = fileStr.split("[.]");
      startTimeArray[i] = splitByDot[1].substring(0, 4);
    }
    return startTimeArray;
  }

  /**
   * @return String: Get Start Time Stamp From File Name
   */
  public String getStartTimeFromFileName(final String fileName) throws FileNotFoundException, IOException {
    final String[] splitByDot = fileName.split("[.]");
    final String startTime = splitByDot[1].substring(0, 4);
    return startTime;
  }

  /**
   * @return String: Get End Time Stamp From File Name
   */
  public String getEndTimeFromFileName(final String fileName) throws FileNotFoundException, IOException {
    final String[] splitByDot = fileName.split("[.]");
    final String fileNameWithNoDateAndExt = splitByDot[1];
    final String[] splitByUnderscore = fileNameWithNoDateAndExt.split("[_]");
    final String[] splitByMinus = splitByUnderscore[0].split("[-]");
    final int splitByMinusLen = splitByMinus.length;
    String endtime = null;

    if (splitByMinusLen == 4) {
      endtime = splitByMinus[2];
    } else if (splitByMinusLen == 2) {
      final String[] splitByPlus2 = splitByMinus[splitByMinus.length - 1].split("[+]");
      endtime = splitByPlus2[0];
    } else if (splitByMinusLen == 1) {
      final String[] splitByPlus2 = splitByMinus[0].split("[+]");
      endtime = splitByPlus2[splitByPlus2.length - 1];
    }
    return endtime;
  }

  /**
   * @return String Array[]: Get File End Times from files in an ArrayList
   */
  public String[] getEndTimeFromFileName(final ArrayList<?> files) throws FileNotFoundException, IOException {
    final String[] endTimeArray = new String[files.size()];

    for (int i = 0; i < files.size(); i++) {
      final Object filesObj = files.get(i);
      final String filesStr = filesObj.toString();
      final String[] splitByDot = filesStr.split("[.]");
      final String[] splitByUnderscore = splitByDot[1].split("[_]");
      final String[] splitByMinus = splitByUnderscore[0].split("[-]");
      final int splitByMinusLen = splitByMinus.length;

      // Get File End Times from various file name formats
      if (splitByMinusLen == 4) {
        endTimeArray[i] = splitByMinus[2];
      } else if (splitByMinusLen == 2) {
        final String[] splitByPlus2 = splitByMinus[splitByMinus.length - 1].split("[+]");
        endTimeArray[i] = splitByPlus2[0];
      } else if (splitByMinusLen == 1) {
        final String[] splitByPlus2 = splitByMinus[0].split("[+]");
        endTimeArray[i] = splitByPlus2[splitByPlus2.length - 1];
      }
    }
    return endTimeArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array []- Get All Counter Values based on String that the
   *         Counter Name contains [counterNameContains]
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmRrcConnEstabSuccMta"
 * @param printCounterValue TODO
   */
  public String[] getCounterValue(final String counterNameContains, boolean printCounterValue) throws SAXException, IOException,
      ParserConfigurationException {
    // Find out which <MI> tag the counter belongs to
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final int mvTagCount = getMvTagBlockCount(counterNameContains);
    final int[] sortOrder = getMoidSortOrder(counterNameContains);

    for (int i = 0; i < mvTagCount; i++) {
      final int mvNum;
      if (counterOrdered.equalsIgnoreCase("y")) {
        mvNum = (sortOrder[i] + 1);
      } else {
        mvNum = i + 1;
      }

      final String[] rTagsArray = getCounterValuesPerMvBlock(counterNameContains, mvNum, printCounterValue);
      for (int r = 0; r < rTagsArray.length; r++) {
        tmpArrayList.add(rTagsArray[r]);
      }
    }
    // Convert String ArrayList to Array
    final String[] resultArray = common.convertStringArrayListToStringArray(tmpArrayList);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String - Get Counter Value based on Counter Name [counterName]
   * @param counterName
   *          (String): Full Name of the Counter that you want to get the value
   *          of - i.e. "pmRrcConnEstabSuccMta"
   * @param mvBlockNumber
   *          (Integer): MV Block Number for the Counter Value Required - i.e.
   *          get the value of "pmRrcConnEstabSuccMta" in MV block 1
   */
  public String getCounterValue(final String counterName, final int mvBlockNumber) throws SAXException, IOException,
      ParserConfigurationException {
    final int miTagNumber = getMiTagNumber(counterName);
    final String[] countersPerMi = getCounterNamesPerMi(miTagNumber);
    String result = "";

    for (int r = 0; r < countersPerMi.length; r++) {
      if (countersPerMi[r].equalsIgnoreCase(counterName)) {
        final String[] rTagsArray = getCounterValuesPerMvBlock(counterName, mvBlockNumber, false);
        result = rTagsArray[0];
      }
    }
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array []- Get All Counter Values based on String that the
   *         Counter Name contains [counterNameContains]
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
 * @param printCounterValue TODO
   */
  public int[] getCounterValueInt(final String counterNameContains, boolean printCounterValue) throws SAXException, IOException,
      ParserConfigurationException {
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final String[] counterValues = getCounterValue(counterNameContains, printCounterValue);

    for (int r = 0; r < counterValues.length; r++) {
      final String[] tmpSplitArray = counterValues[r].split(",");
      for (int i = 0; i < tmpSplitArray.length; i++) {
        tmpArrayList.add(tmpSplitArray[i]);
      }
    }
    final int[] resultArray = common.convertStringArrayListToIntArray(tmpArrayList);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array []- Get XML Counter Values based on String that the
   *         Counter Name contains [counterNameContains], Per MV block;
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
 * @param printCounterValue TODO
   */
  public String[] getCounterValuesPerMvBlock(final String counterNameContains, final int mvTagNumber, boolean printCounterValue)
      throws SAXException, IOException, ParserConfigurationException {

    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final String[] countersPerMi = getCounterNamesPerMi(miTagNumber);
    final String counterLogFile = propFile.getAppPropSingleStringValue(counterLog);
    for (int r = 0; r < countersPerMi.length; r++) {
      final String tmpArrayElement = countersPerMi[r].toLowerCase();

      if (tmpArrayElement.contains(counterNameContains.toLowerCase())) {
        final String[] rTagsArray = getRTagElementValuesPerMiMv(miTagNumber, mvTagNumber);
        if(printCounterValue){
        	//System.out.println("Counter:"+countersPerMi[r]+" Cell ID:"+mvTagNumber +" Value:"+rTagsArray[r]);
        	//logWriter.writeToLogFile(counterLogFile, "Counter:"+countersPerMi[r]+" Cell ID:"+mvTagNumber +" Value:"+rTagsArray[r]);
        	//System.out.println(countersPerMi[r]+","+mvTagNumber +","+rTagsArray[r]);
        	logWriter.writeToLogFile(counterLogFile, countersPerMi[r]+","+mvTagNumber +","+rTagsArray[r]);
        	counterList.add(countersPerMi[r]);
        }
        tmpArrayList.add(rTagsArray[r]);
      }
    }
    final String[] resultArray = common.convertStringArrayListToStringArray(tmpArrayList);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array []- Get XML Counter Values based on String that the
   *         Counter Name contains [counterNameContains], Per MV block;
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
   */
  public int[] getCounterValuesPerMvBlockInt(final String counterNameContains, final int mvTagNumber)
      throws SAXException, IOException, ParserConfigurationException {
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final String[] counterValues = getCounterValuesPerMvBlock(counterNameContains, mvTagNumber, false);

    for (int r = 0; r < counterValues.length; r++) {
      final String[] tmpSplitArray = counterValues[r].split(",");
      for (int i = 0; i < tmpSplitArray.length; i++) {
        tmpArrayList.add(tmpSplitArray[i]);
      }
    }
    final int[] resultArray = common.convertStringArrayListToIntArray(tmpArrayList);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer - Get MV Tag Block Count, based on on String that the
   *         Counter Name contains [counterNameContains], which will determine
   *         the MV block the Counter Name belongs to
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
   */
  public int getMvTagBlockCount(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final int result = countChildTagsInParentTag("mi", "mv", miTagNumber);
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer - Get MI Tag Block Count, based on on String that the
   *         Counter Name contains [counterNameContains], which will determine
   *         the MI block the Counter Name belongs to
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
   */
  public int getMiTagNumber(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {
    int result = 0;
    final int miTagCount = countTags("mi");

    for (int i = 1; i <= miTagCount; i++) {
      final String[] countersPerMi = getCounterNamesPerMi(i);
      for (int r = 0; r < countersPerMi.length; r++) {
        final String tmpArrayElement = countersPerMi[r].toLowerCase();
        if (tmpArrayElement.contains(counterNameContains.toLowerCase())) {
          result = i;
          break;
        }
      }
    }
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String - Get Element Value of Unique Child Tag in a Parent Tag
   * @param parentTagName
   *          (String): Parent Tag Name - i.e. "mi"
   * @param childTagName
   *          (String): Unique Child Tag contained in a Parent Tag - i.e. "mts"
   * @param parentTagNumber
   *          (Integer): Parent tag number (XML File can contain more than 1
   *          Parent tag block)
   */
  public String getElementValueOfUniqueChildTagInParentTag(final String childTagName, final String parentTagName,
      final int parentTagNumber) {
    final String result;
    final NodeList miTags = doc.getElementsByTagName(parentTagName);
    final Node miNode = miTags.item(parentTagNumber - 1);
    final Element miElement = (Element) miNode;

    final NodeList mtTags = miElement.getElementsByTagName(childTagName);
    result = mtTags.item(0).getTextContent();

    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array [] - Get Full List of Counter Names, based on on a
   *         String that the Counter Name contains [counterNameContains]. Based
   *         on Counter names from 1 MI Block only
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTime"
   */
  public String[] getCounterNames(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final String[] countersPerMi = getCounterNamesPerMi(miTagNumber);

    for (int i = 0; i < countersPerMi.length; i++) {
      final String counterName = countersPerMi[i].toLowerCase();
      if (counterName.startsWith(counterNameContains.toLowerCase())) {
        tmpArrayList.add(countersPerMi[i]);
      }
    }
    final String[] resultArray = common.convertStringArrayListToStringArray(tmpArrayList);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array [] - Get XML Counter names per MI Tag Number
   *         [miTagNumber]
   * @param miTagNumber
   *          - MI Tag Number (XML File can contain more than 1 MI tag block)
   */
  public String[] getCounterNamesPerMi(final int miTagNumber) throws SAXException, IOException,
      ParserConfigurationException {
    // Get Elements from MT Tag in MI Block Number X
    final NodeList miTags = doc.getElementsByTagName("mi");
    final Node miNode = miTags.item(miTagNumber - 1);
    final Element miElement = (Element) miNode;
    final NodeList mtTags = miElement.getElementsByTagName("mt");

    // Populate String array with element values
    final String[] resultArray = new String[mtTags.getLength()];
    for (int s = 0; s < mtTags.getLength(); s++) {
      final Element counterElement = (Element) mtTags.item(s);
      final NodeList textCountList = counterElement.getChildNodes();
      resultArray[s] = textCountList.item(0).getTextContent() + "";
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array [] - Get XML rTag Values per MV Tag number in MI Tag
   * @param miTagNumber
   *          (Integer): MI Tag Number
   * @param mvTagNumber
   *          (Integer): MV Tag Number Within MI Tag Number Block
   */
  public String[] getRTagElementValuesPerMiMv(final int miTagNumber, final int mvTagNumber) throws SAXException,
      IOException, ParserConfigurationException {
    // Get Elements from R Tag in MI Block Number X and MV Block Number Y
    final NodeList miTags = doc.getElementsByTagName("mi");
    final Node miNode = miTags.item(miTagNumber - 1);
    final Element miElement = (Element) miNode;

    final NodeList mvTags = miElement.getElementsByTagName("mv");
    final Node mvNode = mvTags.item(mvTagNumber - 1);
    final Element mvElement = (Element) mvNode;

    final NodeList rTags = mvElement.getElementsByTagName("r");

    // Populate String array with element values
    final String[] resultArray = new String[rTags.getLength()];
    for (int s = 0; s < rTags.getLength(); s++) {
      final Element rElement = (Element) rTags.item(s);
      final NodeList textRList = rElement.getChildNodes();
      resultArray[s] = textRList.item(0).getTextContent() + "";
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get MOID Tag Sort Order based on MI Tag
   * @param miTagNumber
   *          (Integer): MI Tag Number
   */
  public int[] getMoidOrder(final int miTagNumber) throws SAXException, IOException, ParserConfigurationException {
    // MOID Array Unsorted
    final String[] moidValueUnsorted = getMoidElementValuesPerMi(miTagNumber);
    for (int s = 0; s < moidValueUnsorted.length; s++) {
    }

    // Copy & Sort MOID Array
    final String[] moidValueSorted = new String[moidValueUnsorted.length];
    for (int s = 0; s < moidValueUnsorted.length; s++) {
      moidValueSorted[s] = moidValueUnsorted[s];
    }
    Arrays.sort(moidValueSorted);

    // MOID Value Sort Order
    int t = 0;
    final int[] resultArray = new int[moidValueUnsorted.length];
    for (int sort = 0; sort < moidValueSorted.length; sort++) {
      for (int unsort = 0; unsort < moidValueSorted.length; unsort++) {
        if (moidValueSorted[unsort].equalsIgnoreCase(moidValueUnsorted[sort])) {
          resultArray[t] = unsort;
          break;
        }
      }
      t++;
    }
    return resultArray;
  }

  public int[] getMoidSortOrder(final int miTagNumber) throws SAXException, IOException, ParserConfigurationException {
    final int[] moidOrder = getMoidOrder(miTagNumber);
    final int[] sortOrder = new int[moidOrder.length];
    for (int r = 0; r < moidOrder.length; r++) {
      for (int i = 0; i < moidOrder.length; i++) {
        if (moidOrder[i] == r) {
          sortOrder[r] = i;
        }
      }
    }
    return sortOrder;
  }

  public int[] getMoidSortOrder(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {

    final int[] moidOrder = getMoidOrder(counterNameContains);
    final int[] sortOrder = new int[moidOrder.length];
    for (int r = 0; r < moidOrder.length; r++) {
      for (int i = 0; i < moidOrder.length; i++) {
        if (moidOrder[i] == r) {
          sortOrder[r] = i;
        }
      }
    }
    return sortOrder;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array - Get the order of the MOID Tags based on String that
   *         the Counter Name contains [counterNameContains]
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
   */
  public int[] getMoidOrder(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final int[] resultArray = getMoidOrder(miTagNumber);
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String Array [] - Get MOID values for given counter containing a
   *         specified string
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmErabEstabTimeInit"
   */
  public String[] getMoidElementsContainingString(final String counterNameContains, final String moidValueContains,
      final boolean sortMoidValues) throws SAXException, IOException, ParserConfigurationException {
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final String[] moidElements = getMoidElementValuesPerMi(miTagNumber);
    final ArrayList<String> moidExternal = new ArrayList<String>();

    for (int i = 0; i < moidElements.length; i++) {
      if (moidElements[i].contains(moidValueContains)) {
        moidExternal.add(moidElements[i]);
      }
      if (sortMoidValues) {
        Collections.sort(moidExternal);
      }
    }
    final String[] result = common.convertArrayListToStringArray(moidExternal);
    return result;
  }

  /**
   * @return Boolean: Check if any MOID Element Value from a given MI block
   *         [miTagNumber] contains a given string [moidValueContains]
   * @param miTagNumber
   *          (Integer): MI Tag Number
   * @param moidValueContains
   *          (String): Full/Part String that a MOID Element Value may contain
   */
  public boolean checkIfMoidElementValuesContainString(final int miTagNumber, final String moidValueContains)
      throws SAXException, IOException, ParserConfigurationException {
    final String[] moidElements = getMoidElementValuesPerMi(miTagNumber);
    boolean check = false;
    for (int i = 0; i < moidElements.length; i++) {
      if (moidElements[i].toLowerCase().contains(moidValueContains.toLowerCase())) {
        check = true;
        break;
      }
    }
    return check;
  }

  /**
   * @return Boolean: Check if any MOID Element Value from a given MI block
   *         [miTagNumber] contains a given string [moidValueContains]
   * @param miTagNumber
   *          (Integer): MI Tag Number
   * @param moidValueContains1
   *          (String): Full/Part String that a MOID Element Value may contain
   * @param moidValueContains2
   *          (String): Full/Part String that a MOID Element Value DOES NOT
   *          contain
   * @param doesNotContainString2
   *          (Boolean): True if MOID value contains the second string
   *          [moidValueContains2]
   */
  public boolean checkIfMoidElementValuesContainString(final int miTagNumber, final String moidValueContains1,
      final String moidValueContains2, final boolean doesNotContainString2) throws SAXException, IOException,
      ParserConfigurationException {
    final String[] moidElements = getMoidElementValuesPerMi(miTagNumber);
    boolean check = false;
    for (int i = 0; i < moidElements.length; i++) {
      if (!doesNotContainString2) {
        if (moidElements[i].toLowerCase().contains(moidValueContains1.toLowerCase())
            && moidElements[i].toLowerCase().contains(moidValueContains2)) {
          check = true;
          break;
        }
      }
      if (doesNotContainString2) {
        if (moidElements[i].toLowerCase().contains(moidValueContains1.toLowerCase())
            && !moidElements[i].toLowerCase().contains(moidValueContains2)) {
          check = true;
          break;
        }
      }
    }
    return check;
  }

  /**
   * @return Boolean: Check if any MOID Element Value from a given MI block
   *         [miTagNumber] contains a given string [moidValueContains]
   * @param fileName
   *          (Integer):Name of XML file
   * @param moidValueContains
   *          (String): Full/Part String that a MOID Element Value may contain
   */
  public boolean checkIfMoidElementValueExists(final String fileName, final String moidValueContains)
      throws SAXException, IOException, ParserConfigurationException {
    final String[] moidElements = getAllElementContent(fileName, "moid");
    boolean check = false;
    for (int i = 0; i < moidElements.length; i++) {
      if (moidElements[i].toLowerCase().contains(moidValueContains.toLowerCase())) {
        check = true;
        break;
      }
    }
    return check;
  }

  /**
   * @return Boolean: Check if any MOID Element Value from a given MI block
   *         [miTagNumber] contains a given string [moidValueContains]
   * @param fileName
   *          (Integer):Name of XML file
   * @param moidValueContains
   *          (String): Full/Part String that a MOID Element Value may contain
   */
  public boolean checkIfMoidElementValueExistsA(final String fileName, final String moidValueContains1,
      final String moidValueContains2) throws SAXException, IOException, ParserConfigurationException {
    final String[] moidElements = getAllElementContent(fileName, "moid");
    boolean check = false;
    for (int i = 0; i < moidElements.length; i++) {
      if (moidElements[i].toLowerCase().contains(moidValueContains1.toLowerCase())
          && moidElements[i].toLowerCase().contains(moidValueContains2.toLowerCase())) {
        check = true;
        break;
      }
    }
    return check;
  }

  /**
   * @return Boolean: Check if any MOID Element Value from a given MI block
   *         [miTagNumber] contains a given string [moidValueContains]
   * @param miTagNumber
   *          (Integer): MI Tag Number
   * @param moidValueContains
   *          (String): Full/Part String that a MOID Element Value may contain
   */
  public boolean checkIfMoidElementValueExistsB(final String fileName, final String moidValueContains,
      final String moidValueDoesNotContains) throws SAXException, IOException, ParserConfigurationException {
    final String[] moidElements = getAllElementContent(fileName, "moid");
    boolean check = false;
    for (int i = 0; i < moidElements.length; i++) {
      if (moidElements[i].toLowerCase().contains(moidValueContains.toLowerCase())
          && !moidElements[i].toLowerCase().contains(moidValueDoesNotContains.toLowerCase())) {
        check = true;
        break;
      }
    }
    return check;
  }

  /**
   * @return String Array - Get MOID Tag Element Values based on MI Tag number
   * @param miTagNumber
   *          (Integer): MI Tag Number
   */
  public String[] getMoidElementValuesPerMi(final int miTagNumber) throws SAXException, IOException,
      ParserConfigurationException {
    final int mvTagCount = countChildTagsInParentTag("mi", "mv", miTagNumber);
    final String[] resultArray = new String[mvTagCount];

    // Get Elements from R Tag in MI Block Number X and MV Block Number Y
    final NodeList miTags = doc.getElementsByTagName("mi");
    final Node miNode = miTags.item(miTagNumber - 1);
    final Element miElement = (Element) miNode;

    final NodeList mvTags = miElement.getElementsByTagName("mv");

    // Populate String array with element values
    for (int i = 0; i < mvTagCount; i++) {
      final Element mvElement = (Element) mvTags.item(i);
      final NodeList moidTags = mvElement.getElementsByTagName("moid");
      resultArray[i] = moidTags.item(0).getTextContent() + "";
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer - Count the number of MV Blocks based on on a String that
   *         the MOID Element Value contains [moidValueContains]
   * @param fileName
   *          (String): XML File Name
   * @param moidValueContains
   *          (String): Name/PartName of the MOID Element Value that you want to
   *          count - i.e. "EUtranCellRelation"
   */
  public int countMvTagsBasedOnMoidValue(final String fileName, final String moidValueContains) throws SAXException,
      IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final int totalMiTags = countTags("mi");
    int count = 0;

    for (int i = 1; i <= totalMiTags; i++) {
      final String[] moidValuePerMi = getMoidElementValuesPerMi(i);
      for (int x = 0; x < moidValuePerMi.length; x++) {
        if ((moidValuePerMi[x].contains(moidValueContains))) {
          count++;
        }
      }
      // Break out of loop if count is more than 1
      if (count > 0) {
        break;
      }
    }
    return count;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer - Get the MI Block number based on on a String that the
   *         MOID Element Value contains [moidValueContains]
   * @param fileName
   *          (String): XML File Name
   * @param moidValueContains
   *          (String): Name/PartName of the MOID Element Value that you want to
   *          count - i.e. "EUtranCellRelation"
   */
  public int getMiBlockNumberBasedOnMoidValue(final String fileName, final String moidValueContains)
      throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    final int totalMiTags = countTags("mi");
    int result = 0;
    for (int i = 0; i < totalMiTags; i++) {
      final String[] moidValuePerMi = getMoidElementValuesPerMi(i + 1);

      for (int x = 0; x < moidValuePerMi.length; x++) {
        if ((moidValuePerMi[x].toLowerCase().contains(moidValueContains.toLowerCase()))) {
          result = i + 1;
          break;
        }
      }
    }
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return boolean: Checks if a MOID Values that contains the specified String
   *         [moidValueLike] exists in the MI Block
   * @param fileName
   *          : Name of .XML file
   * @param moidValueLike
   *          : String comparison - Compared against full MOID value
   * @param miTagNumber
   *          : MI number in which the MOID value exists
   */
  public boolean checkIfMoidValueExistsInMiBlock(final String fileName, final String moidValueLike,
      final int miTagNumber) throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    boolean result = false;
    final String[] moidValuePerMi = getMoidElementValuesPerMi(miTagNumber);
    for (int x = 0; x < moidValuePerMi.length; x++) {
      if (moidValuePerMi[x].indexOf(moidValueLike) > 0) {
        result = true;
      }
    }
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return INT: Counts the number of MOID Values that contains the specified
   *         String [moidValueLike]
   * @param fileName
   *          : Name of .XML file
   * @param moidValueLike
   *          : String comparison - Compared against full MOID value
   * @param miTagNumber
   *          : MI number in which the MOID value exists
   */
  public int countMoidValueBasedOn1String(final String fileName, final String moidValueLike, final int miTagNumber)
      throws SAXException, IOException, ParserConfigurationException {
    openXmlFile(fileName);
    int count = 0;
    final String[] moidValuePerMi = getMoidElementValuesPerMi(miTagNumber);
    for (int x = 0; x < moidValuePerMi.length; x++) {
      if (moidValuePerMi[x].indexOf(moidValueLike) > -1) {
        count++;
      }
    }
    return count;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return INT - Counts the number of MOID Values that contains the specified
   *         Strings [moidValueLike1] and [moidValueLike2]
   * @param fileName
   *          : Name of .XML file
   * @param moidValueLike1
   *          : String comparison 1 - Compared against full MOID value
   * @param moidValueLike2
   *          : String comparison 2 - Compared against full MOID value
   * @param miTagNumber
   *          : MI number in which the MOID value exists
   */
  public int countMoidValueBasedOn2Strings(final String fileName, final String moidValueLike1,
      final String moidValueLike2, final int miTagNumber) throws SAXException, IOException,
      ParserConfigurationException {
    openXmlFile(fileName);
    int count = 0;
    final String[] moidValuePerMi = getMoidElementValuesPerMi(miTagNumber);
    for (int x = 0; x < moidValuePerMi.length; x++) {
      if ((moidValuePerMi[x].indexOf(moidValueLike1) > -1) && (moidValuePerMi[x].indexOf(moidValueLike2) > -1)) {
        count++;
      }
    }
    return count;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String[] Array - Extract Unique Substring (end string of the
   *         counter name) and put into Array based on String that the Counter
   *         Name Starts with [counterStringStartString]
   * @param counterNameStartString
   *          (String): Start String of the Counter that you want to get the
   *          value of - i.e. "pmRrcConnEstabSucc"
   */
  public String[] extractCounterNameSubstring(final String counterNameStartString) throws SAXException, IOException,
      ParserConfigurationException {
    // get counter names based on string it contains
    final String[] counterNames = getCounterNames(counterNameStartString);
    final String[] resultArray = new String[counterNames.length];
    final int counterNameCount = counterNameStartString.length();

    for (int i = 0; i < counterNames.length; i++) {
      final String fullString = counterNames[i];
      final int fullStringCount = fullString.length();
      final String subString = fullString.substring(counterNameCount, fullStringCount);
      resultArray[i] = subString;
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer - Get the multiple value counters X bin ranges and sum the
   *         values
   * @param counterNameContains
   *          (String): Name/PartName of the Counter (counter set) that you want
   *          to get the length of - i.e. "pmErabEstabTimeInitDistrQci1" or
   *          "pmErabEstabTimeInitDistr"
   * @param splitDilimiter
   *          (String): Split Delimiter of the multiple value string counter -
   *          i.e. for String Counter "0,5,0,5,0,5,0,5,0,5" the split delimiter
   *          is "[,]"
   * 
   */
  public int countMultipleValueStringLenght(final String counterNameContains, final String splitDelimiter)
      throws SAXException, IOException, ParserConfigurationException {
    final String[] tmpArray = getCounterValue(counterNameContains, false);
    final String[] distrArray = tmpArray[0].split(splitDelimiter);
    final int result = distrArray.length;
    return result;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get the sum of multiple value counters
   *         [counterName]
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmRrcConnEstabTimeDistr"
   */
  public int[] sumOfMultipleValueCounter(final String counterNameContains) throws SAXException, IOException,
      ParserConfigurationException {
    final String[] tmpArray = getCounterValue(counterNameContains, false);
    final int[] resultArray = new int[tmpArray.length];

    for (int r = 0; r < tmpArray.length; r++) {
      final String[] tmpArrayString = tmpArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]);
      }

      int sumValues = 0;
      for (int t = 0; t < tmpArrayInt.length; t++) {
        final int tmp = tmpArrayInt[t];
        sumValues = sumValues + tmp;
      }
      resultArray[r] = (sumValues);
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get the sum of multiple value counters
   *         [counterName]
   * @param counterArray
   *          (String Array []): String Array of Multiple Value counters values.
   *          String Array must contain string of numbers only - i.e.
   *          {"0,2,0,2,0,2,0,2,0,2", "0,5,0,5,0,5,0,5,0,5",
   *          "3,1,3,5,2,4,3,2,3,1"} etc.
   */
  public int[] sumOfMultipleValueCounter(final String[] counterArray) throws SAXException, IOException,
      ParserConfigurationException {
    final int[] resultArray = new int[counterArray.length];

    for (int r = 0; r < counterArray.length; r++) {
      final String[] tmpArrayString = counterArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]);
      }

      int sumValues = 0;
      for (int t = 0; t < tmpArrayInt.length; t++) {
        final int tmp = tmpArrayInt[t];
        sumValues = sumValues + tmp;
      }
      resultArray[r] = (sumValues);
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get the sum of Multiple Value Counter Multiplies
   *         By a Bin Range
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmRrcConnEstabTimeDistr"
   * @param binRangeName
   *          (Integer Array[]): Name of the bin range that you want the sum
   *          values to be multiplies against (Bin Range from
   *          conf_LTE.properties file)
   * 
   */
  public int[] sumOfMultipleValueCounterMultipliedByBinRange(final String counterNameContains, final int[] binRangeName)
      throws SAXException, IOException, ParserConfigurationException {
    final String[] tmpArray = getCounterValue(counterNameContains, false);
    final int[] resultArray = new int[tmpArray.length];

    for (int r = 0; r < tmpArray.length; r++) {
      final String[] tmpArrayString = tmpArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]) * binRangeName[s];
      }

      int sumValues = 0;
      for (int t = 0; t < tmpArrayInt.length; t++) {
        final int tmp = tmpArrayInt[t];
        sumValues = sumValues + tmp;
      }
      resultArray[r] = (sumValues);
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get the multiple value counters Multiplied by
   *         bin ranges and then sum all the values - i.e. From Counter value
   *         "0,5,0,5,0,5,0" multiply each value with a bin range value such as
   *         [(0*b[0])+ (5*b[1] )+ (0*b[2])+ (5*b[3])+ (0*b[4])+ (5*b[5])+
   *         (0*b[6])=Sum]
   * @param counterArray
   *          (String Array []): String Array of Multiple Value counters values.
   *          String Array must contain string of numbers only - i.e.
   *          {"0,2,0,2,0,2,0,2,0,2", "0,5,0,5,0,5,0,5,0,5",
   *          "3,1,3,5,2,4,3,2,3,1"} etc.
   * @param binRangeName
   *          (Integer Array[]): Name of the bin range that you want the sum
   *          values to be multiplies against (Bin Range from
   *          conf_LTE.properties file)
   */
  public int[] sumOfMultipleValueCounterMultipliedByBinRange(final String[] counterArray, final int[] binRangeName)
      throws SAXException, IOException, ParserConfigurationException {
    final int[] resultArray = new int[counterArray.length];

    for (int r = 0; r < counterArray.length; r++) {
      final String[] tmpArrayString = counterArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]) * binRangeName[s];
      }

      int sumValues = 0;
      for (int t = 0; t < tmpArrayInt.length; t++) {
        final int tmp = tmpArrayInt[t];
        sumValues = sumValues + tmp;
      }
      resultArray[r] = (sumValues);
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get the Max Value of a Multiple Value Counter
   * @param counterNameContains
   *          (String): Name/PartName of the Counter that you want to get the
   *          value of - i.e. "pmRrcConnEstabTimeDistr"
   * @param binRangeName
   *          (Integer Array []): Name of the bin range that you want extract
   *          the max value from (Bin Range from conf_LTE.properties OR
   *          Samp.properties files)
   */
  public int[] getMaxValOfMultipleValueCounter(final String counterNameContains, final int[] binRangeName)
      throws SAXException, IOException, ParserConfigurationException {
    final String[] tmpArray = getCounterValue(counterNameContains, false);
    final int[] resultArray = new int[tmpArray.length];

    for (int r = 0; r < tmpArray.length; r++) {
      final String[] tmpArrayString = tmpArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]);
      }
      int num = tmpArrayInt.length - 1;
      while (tmpArrayInt[num] == 0) {
        num--;
      }
      resultArray[r] = binRangeName[num];
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get Max Values of Multiple Value Counters
   * @param counterArray
   *          (String Array []): String Array of Multiple Value counters values.
   *          String Array must contain string of numbers only - i.e.
   *          {"0,2,0,2,0,2,0,2,0,2", "0,5,0,5,0,5,0,5,0,5",
   *          "3,1,3,5,2,4,3,2,3,1"} etc.
   * @param binRangeName
   *          (Integer Array []): Name of the bin range that you want extract
   *          the max value from (Bin Range from conf_LTE.properties OR
   *          Samp.properties files)
   */
  public int[] getMaxValOfMultipleValueCounter(final String[] counterArray, final int[] binRangeName)
      throws SAXException, IOException, ParserConfigurationException {
    final int[] resultArray = new int[counterArray.length];

    for (int r = 0; r < counterArray.length; r++) {
      final String[] tmpArrayString = counterArray[r].split(",");
      final int[] tmpArrayInt = new int[tmpArrayString.length];

      for (int s = 0; s < tmpArrayString.length; s++) {
        tmpArrayInt[s] = Integer.parseInt(tmpArrayString[s]);
      }
      final int num = tmpArrayInt.length - 1;
      for (int i = num; i >= 0; i--) {
        if (tmpArrayInt[i] == 0) {
          if (i == 0) {
            resultArray[r] = 0;
          }
        } else {
          resultArray[r] = binRangeName[i];
          break;
        }
      }
    }
    return resultArray;
  }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get Counter values based on a String/PartString
   *         that a counter name contains for X number of counters only
   *         [getCounterNameContainsLength]
   * @param getCounterNameContainsLength
   *          (String): Name/PartName of the Counter(s) (counter set) that you
   *          want to get the length of (the counter name will determine the
   *          length required) - i.e. "pmErabEstabTimeAddedSumQci";
   * @param counterNameContains
   *          (String): Name/PartName of the required/Main Counter - i.e.
   *          "pmErabEstabTimeInitDistrQci" or "pmErabEstabTimeAddedDistrQci9";
   */
  public String[] getCounterValuesBasedOnMainComparisonCounterStringArray(final String getCounterNameContainsLength,
      final String counterNameContains) throws SAXException, IOException, ParserConfigurationException {
    final String counterOrdered = propFile.getAppPropSingleStringValue(counterValuesOrdered);

    final String[] subStringTimeSumArray = extractCounterNameSubstring(getCounterNameContainsLength);
    final String[] subStringtimeSumDistrBinRangeValArray = extractCounterNameSubstring(counterNameContains);
    final ArrayList<String> tmpArrayList = new ArrayList<String>();
    final int miTagNumber = getMiTagNumber(counterNameContains);
    final int[] mvSortOrder = getMoidSortOrder(miTagNumber);

    for (int mvNum = 0; mvNum < getMvTagBlockCount(getCounterNameContainsLength); mvNum++) {
      final int mvTagNum;
      if (counterOrdered.equalsIgnoreCase("y")) {
        mvTagNum = mvSortOrder[mvNum] + 1;
      } else {
        mvTagNum = mvNum + 1;
      }

      for (int i = 0; i < subStringTimeSumArray.length; i++) {
        final String subStringTimeSum = subStringTimeSumArray[i];

        for (int s = 0; s < subStringtimeSumDistrBinRangeValArray.length; s++) {
          final String subStringtimeSumDistrBinRangeVal = subStringtimeSumDistrBinRangeValArray[s];
          if (subStringTimeSum.equals(subStringtimeSumDistrBinRangeVal)) {
            final String[] tmpArray = getCounterValuesPerMvBlock(counterNameContains + subStringTimeSum, mvTagNum, false);
            for (int t = 0; t < tmpArray.length; t++) {
              tmpArrayList.add(tmpArray[t]);
            }
          }
        }
      }
    }
    final String[] mainIntArray = new String[tmpArrayList.size()];
    for (int x = 0; x < tmpArrayList.size(); x++) {
      mainIntArray[x] = tmpArrayList.get(x);
    }
    return mainIntArray;
  }
  // public String[] getChildNodesFromParentNode(final String fileName, final
  // String parentNodeTag) throws SAXException,
  // IOException, ParserConfigurationException {
  // openXmlFile(fileName);
  //
  // final NodeList xmlTags = doc.getElementsByTagName(parentNodeTag);
  //
  // final String[] childNodeValues = new String[xmlTags.getLength()];
  // for (int s = 0; s < xmlTags.getLength(); s++) {
  // final Element counterElement = (Element) xmlTags.item(s);
  // final NodeList textCountList = counterElement.getChildNodes();
  // childNodeValues[s] = textCountList.item(0).getTextContent() + "";
  // }
  // return childNodeValues;
  // }

  // public int[] getSingleValueCountersBasedOnMainComparisonCounterIntArray(
  // final String arraySizeBasedOncounterNameContains, final String
  // counterNameContains) throws SAXException,
  // IOException, ParserConfigurationException {
  // final String[] subStringTimeSumArray =
  // extractCounterNameSubstring(arraySizeBasedOncounterNameContains);
  // final String[] subStringtimeSumDistrBinRangeValArray =
  // extractCounterNameSubstring(counterNameContains);
  // final ArrayList<Integer> tmpArrayList = new ArrayList<Integer>();
  //
  // for (int mvNum = 1; mvNum <=
  // getMvTagBlockCount(arraySizeBasedOncounterNameContains); mvNum++) {
  // for (int i = 0; i < subStringTimeSumArray.length; i++) {
  // final String subStringTimeSum = subStringTimeSumArray[i];
  //
  // for (int s = 0; s < subStringtimeSumDistrBinRangeValArray.length; s++) {
  // final String subStringtimeSumDistrBinRangeVal =
  // subStringtimeSumDistrBinRangeValArray[s];
  // if (subStringTimeSum.equals(subStringtimeSumDistrBinRangeVal)) {
  // final int[] tmpArray = getCounterValuesPerMvBlockInt(counterNameContains +
  // subStringTimeSum, mvNum);
  // for (int t = 0; t < tmpArray.length; t++) {
  // tmpArrayList.add(tmpArray[t]);
  // }
  // }
  // }
  // }
  // }
  //
  // final int[] mainIntArray = new int[tmpArrayList.size()];
  // for (int x = 0; x < tmpArrayList.size(); x++) {
  // mainIntArray[x] = tmpArrayList.get(x);
  // }
  // return mainIntArray;
  // }

  /**
   * XML COUNTER File Related Method
   * 
   * @return String[] Array - Extract a Counter Name Unique Substring from an
   *         array and put into Array
   */
  // public String[] extractCounterStringArraySubstring(final String[]
  // arrayName, final String counterStringToExtract)
  // throws SAXException, IOException, ParserConfigurationException {
  // final String[] subStringArray = new String[arrayName.length];
  // final int counterNameCount = counterStringToExtract.length();
  //
  // for (int i = 0; i < arrayName.length; i++) {
  // final String fullString = arrayName[i];
  // final int fullStringCount = fullString.length();
  // final String subString = fullString.substring(counterNameCount,
  // fullStringCount);
  // subStringArray[i] = subString;
  // }
  // return subStringArray;
  // }

  /**
   * XML COUNTER File Related Method
   * 
   * @return Integer Array [] - Get MOID Tag Sort Order based on MI Tag
   * @param miTagNumber
   *          (Integer): MI Tag Number
   */
  // public int[] getMoidSortedOrder(final int miTagNumber) throws SAXException,
  // IOException, ParserConfigurationException {
  // final ArrayList<String> tmpArrayList = new ArrayList<String>();
  // final String[] moidValue = getMoidElementValuesPerMi(miTagNumber);
  // int t = 0;
  //
  // for (int s = 0; s < moidValue.length; s++) {
  // final String[] tmpArray = moidValue[s].split(",");
  // tmpArrayList.add(tmpArray[tmpArray.length - 1]);
  // }
  //
  // // MOID Value Unsorted
  // final String[] tmpArrayUnsort = new String[tmpArrayList.size()];
  // for (int unsort = 0; unsort < tmpArrayList.size(); unsort++) {
  // tmpArrayUnsort[unsort] = tmpArrayList.get(unsort);
  // }
  //
  // // MOID Value Sorted
  // Collections.sort(tmpArrayList);
  // final String[] tmpArraySort = new String[tmpArrayList.size()];
  // for (int sort = 0; sort < tmpArrayList.size(); sort++) {
  // tmpArraySort[sort] = tmpArrayList.get(sort);
  // }
  //
  // // MOID Value Sort Order
  // final int[] sortValArray = new int[tmpArraySort.length];
  // for (int i = 0; i < sortValArray.length; i++) {
  // for (int sortVal = 0; sortVal < tmpArraySort.length; sortVal++) {
  // if (tmpArraySort[i] == tmpArrayUnsort[sortVal]) {
  // sortValArray[t] = sortVal;
  // }
  // }
  // t++;
  // }
  // return sortValArray;
  // }

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////GENERAL METHODS
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // /**
  // * @return String Array []- Convert String ArrayList to String[] Array
  // * @param arrayListName
  // * (ArrayList<> String): String ArrayList that needs to be converted
  // * to String Array[]
  // */
  // public String[] convertStringArrayListToStringArray(final ArrayList<String>
  // arrayListName) {
  // final int arrayLength = arrayListName.size();
  // final String[] stringArray = new String[arrayLength];
  // for (int r = 0; r < arrayLength; r++) {
  // stringArray[r] = arrayListName.get(r);
  // }
  // return stringArray;
  // }
  //
  // /**
  // * @return Integer Array []- Convert String ArrayList to Integer[] Array
  // * @param arrayListName
  // * (ArrayList<> String): String ArrayList that needs to be converted
  // * to Integer Array[]
  // */
  // public int[] convertStringArrayListToIntArray(final ArrayList<String>
  // arrayListName) {
  // final String[] tmpStringArray =
  // convertStringArrayListToStringArray(arrayListName);
  // final int arrayLength = tmpStringArray.length;
  // final int[] intArray = new int[arrayLength];
  // for (int r = 0; r < arrayLength; r++) {
  // intArray[r] = Integer.parseInt(tmpStringArray[r]);
  // }
  // return intArray;
  // }
  //
  // /**
  // * @return Integer Array []- Convert Integer ArrayList to Integer[] Array
  // * @param arrayListName
  // * (ArrayList<> String): Integer ArrayList that needs to be converted
  // * to Integer Array[]
  // */
  // public int[] convertIntegerArrayListToIntArray(final ArrayList<Integer>
  // arrayListName) {
  // final int arrayLength = arrayListName.size();
  // final int[] tmpArrayInt = new int[arrayLength];
  // for (int r = 0; r < arrayLength; r++) {
  // tmpArrayInt[r] = arrayListName.get(r);
  // }
  // return tmpArrayInt;
  // }

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////FILE RELATED METHODS
  // ////PUT INTO FileControls.java
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // /**
  // * @see app.properties file
  // * @return String - Forward or back slash for INPUT files/directory.
  // Different
  // * for local or server files
  // * @renamed - splitForwardBackSlashInput() separator
  // */
  // public String getInputDirectorySeparator() throws FileNotFoundException,
  // IOException {
  // final String dirSeperator =
  // propFile.getAppPropSingleStringValue("DirSeperator_LocalOrServer");
  // return dirSeperator;
  // }
  //
  // /**
  // * @see app.properties file
  // * @return String - Forward or back slash for OUTPUT files/directory.
  // * Different for local or server files
  // * @renamed - splitForwardBackSlashOutput()
  // */
  // public String getOutputDirectorySeparator() throws FileNotFoundException,
  // IOException {
  // final String dirSeperator =
  // propFile.getAppPropSingleStringValue("DirSeperator_OutputFile");
  // return dirSeperator;
  // }
  //
  // /**
  // * @return String - Get the OSS Directory Folder name from the file name
  // * [fileName]
  // * @param fileName
  // * (String): XML File Name
  // * @rename - fileNameOssExtension();
  // */
  // public String getOssDirNameFromFileName(final String fileName) throws
  // FileNotFoundException, IOException {
  // final String splitType = getInputDirectorySeparator();
  // final String[] file = fileName.split(splitType);
  // String ossExt;
  //
  // if (file.length != 0 && file.length >= 3) {
  // final String ossDir = file[file.length - 3];
  // ossExt = ossDir;
  // } else {
  // ossExt = "Not Available";
  // }
  // return ossExt;
  // }
  //
  // /**
  // * @return String - Get the file directory name from the file name
  // [fileName]
  // * @param fileName
  // * (String): XML File Name
  // * @Renamed - fileNameDirectoryFolder
  // */
  // public String getFileFolderNameFromFileName(final String fileName) throws
  // FileNotFoundException, IOException {
  // final String splitType = getInputDirectorySeparator();
  // final String[] file = fileName.split(splitType);
  // final String dir;
  //
  // if (file.length != 0 && file.length >= 2) {
  // final String folderDir = file[file.length - 2];
  // dir = folderDir;
  // } else {
  // dir = "Not Available";
  // }
  // return dir;
  // }
  //
  // /**
  // * @return String - Get XML File name with or without extensions
  // * @param fileName
  // * (String): XML File Name
  // * @param fileNameWithOssExt
  // * (Boolean): File Name with OSS Directory Extension
  // * @param fileNameWithFileDirExt
  // * (Boolean): File Name with File Directory Extension
  // *
  // * @renamed -
  // fileNameWithOssAndDirExtension()&&fileNameWithoutDirExtensions()
  // */
  // public String getFileNameWithExt(final String fileName, final boolean
  // fileNameWithOssExt,
  // final boolean fileNameWithFileDirExt) throws FileNotFoundException,
  // IOException {
  // final String splitType = getInputDirectorySeparator();
  // final String splitTypeOut = getOutputDirectorySeparator();
  // String fullFileName = null;
  //
  // final String[] file = fileName.split(splitType);
  // final String fName = file[file.length - 1];
  //
  // if (file.length > 3 && fileNameWithOssExt) {
  // fullFileName = file[file.length - 3] + splitTypeOut + file[file.length - 2]
  // + splitTypeOut + fName;
  // } else if (file.length > 2 && fileNameWithFileDirExt) {
  // fullFileName = file[file.length - 2] + splitTypeOut + file[file.length -
  // 1];
  // } else if (!fileNameWithOssExt && !fileNameWithFileDirExt) {
  // fullFileName = fName;
  // } else {
  // fullFileName = fileName;
  // }
  // return fullFileName;
  // }

}
