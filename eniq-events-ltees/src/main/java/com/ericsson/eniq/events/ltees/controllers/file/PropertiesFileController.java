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

package com.ericsson.eniq.events.ltees.controllers.file;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.applicationdrivers.AppDriver;

public class PropertiesFileController {

    private final String confPropFile = "ConfPropFile_Directory";

    private final String appFileLocal = "test_automation_ltees/app.properties";

    private String appFileServer = "/eniq/northbound/test_automation_ltees/app.properties";

    private final Properties prop = new Properties() {

        private static final long serialVersionUID = -4389132085556896344L;

        public String getProperty(String key) {
            String value = super.getProperty(key + "_" + AppDriver.executionContext.getMsg());
            if (value != null && !value.equals("")) {
                return value;
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_ELEVEN_B)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_ELEVEN_B);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_TWELVE_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_TWELVE_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_TWELVE_B)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_TWELVE_B);
                if (value != null && !value.equals("")) {
                    return value;
                } else {
                    value = super.getProperty(key + "_" + AppDriver.STRING_TWELVE_A);
                    if (value != null && !value.equals("")) {
                        return value;
                    }
                }

            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_THIRTEEN_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_THIRTEEN_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_THIRTEEN_B)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_THIRTEEN_B);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_FOURTEEN_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_FOURTEEN_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_FOURTEEN_B)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_FOURTEEN_B);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_PMS)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_PMS);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_CTRS)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_CTRS);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_FIFTEEN_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_FIFTEEN_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_SIXTEEN_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_SIXTEEN_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_SIXTEEN_B)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_SIXTEEN_B);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_SEVENTEEN_A)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_SEVENTEEN_A);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            if (AppDriver.executionContext.getMsg().contains(AppDriver.STRING_ALL_PMS)) {
                value = super.getProperty(key + "_" + AppDriver.STRING_ALL_PMS);
                if (value != null && !value.equals("")) {
                    return value;
                }
            }
            return super.getProperty(key);
        }
    };

    public String getAppPropDirectoryLocalOrServer() {
        String fileName = null;
        final File fServer = new File(appFileServer);

        if (!fServer.exists()) {
            fileName = appFileLocal;
        } else {
            fileName = appFileServer;
        }

        return fileName;
    }

    /**
     * @return String Array[] - Read property file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public String[] readPropertiesFile(final String tagName, final String propertiesFileName) throws ParserConfigurationException, SAXException,
            IOException {
        final String[] xmlPropFileElements;
        final File file = new File(propertiesFileName);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        final NodeList nodeList = doc.getElementsByTagName(tagName);
        final int numberOfElementsXMLPropertiesFile = nodeList.getLength();
        xmlPropFileElements = new String[numberOfElementsXMLPropertiesFile];

        for (int i = 0; i < numberOfElementsXMLPropertiesFile; i++) {
            final Node nodeListElement = nodeList.item(i);
            if (nodeListElement.getNodeType() == Node.ELEMENT_NODE) {
                xmlPropFileElements[i] = nodeListElement.getTextContent();
            }
        }
        return xmlPropFileElements;
    }

    /**
     * @return Void - Loads Application Properties File - Required Folder Directory Properties Tag for where the .prop file is stored
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public void loadPropertiesFile(final String propFileDir) throws FileNotFoundException, IOException {
        prop.load(new FileInputStream(propFileDir));
    }

    /**
     * @return Void -Loads Application Properties File - Required Folder Directory Properties Key Tag for where the .prop file is stored
     * @param folderDirPropTag
     *            (String): Properties file tag from app.properties file
     */
    public void loadPropertiesDirByAppPropTag(final String folderDirPropKeyTag) throws FileNotFoundException, IOException {
        final String fileName = getAppPropDirectoryLocalOrServer();
        loadPropertiesFile(fileName);
        final String propDir = prop.getProperty(folderDirPropKeyTag);
        prop.load(new FileInputStream(propDir));
    }

    /**
     * @return Object Array[] - Properties File Key Tag Names to Array
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public Object[] propKeySetArray(final String propFileDir) throws FileNotFoundException, IOException {
        loadPropertiesFile(propFileDir);
        final Object[] keySetList = prop.keySet().toArray();
        return keySetList;
    }

    /**
     * @return String -Properties File Single String Value from Prop Key Tag
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     */
    public String getAppPropSingleStringValue(final String propertiesKeyTag) throws FileNotFoundException, IOException {
        final String fileName = getAppPropDirectoryLocalOrServer();
        loadPropertiesFile(fileName);
        final String propTagStringValue = prop.getProperty(propertiesKeyTag);
        return propTagStringValue;
    }

    /**
     * @return String - Properties File Single String Value to string
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public String getPropertiesSingleStringValue(final String propertiesKeyTag, final String propFileDir) throws FileNotFoundException, IOException {
        loadPropertiesFile(propFileDir);
        final String propTagStringValue = prop.getProperty(propertiesKeyTag);
        return propTagStringValue;
    }

    /**
     * @return Integer - Properties File Single Integer Value to integer
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public int getPropertiesSingleIntValue(final String propertiesKeyTag, final String propFileDir) throws FileNotFoundException, IOException {
        loadPropertiesFile(propFileDir);
        final String val = prop.getProperty(propertiesKeyTag);
        final int propTagIntValue = Integer.parseInt(val);
        return propTagIntValue;
    }

    /**
     * @return String Array[] - Properties Multiple String Value to String
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public String[] getPropertiesMultipleStringValues(final String propertiesKeyTag, final String propFileDir) throws FileNotFoundException,
            IOException {
        loadPropertiesFile(propFileDir);
        final String[] multiplePropStringValues = prop.getProperty(propertiesKeyTag).split(",");
        return multiplePropStringValues;
    }

    /**
     * @return Integer Array[] - Properties Multiple String Value to int
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     */
    public int[] getPropertiesMultipleIntValues(final String propertiesKeyTag, final String propFileDir) throws FileNotFoundException, IOException {
        final String[] tempArray = getPropertiesMultipleStringValues(propertiesKeyTag, propFileDir);
        final int[] multiplePropIntValues = new int[tempArray.length];
        for (int i = 0; i < multiplePropIntValues.length; i++) {
            multiplePropIntValues[i] = Integer.parseInt(tempArray[i]);
        }
        return multiplePropIntValues;
    }

    /**
     * @return Integer Array[] - Properties Multiple String Value of every X Value - i.e. get every 2nd value starting at value 2
     * @param propertiesKeyTag
     *            (String): Properties Key Tag name
     * @param propFileDir
     *            (String): Filename and Directory of properties file
     * @param startNum
     *            (Integer): Start location number - i.e. start at the 3rd number in the array
     * @param step
     *            (Integer): Step at every X number - i.e. get every second value in array
     */
    public int[] getPropertiesMultipleIntValuesEveryXvalue(final String propertiesKeyTag, final int startNum, final int step, final String propFileDir)
            throws FileNotFoundException, IOException {
        final ArrayList<Integer> intArrayList = new ArrayList<Integer>();
        final int[] tmpIntArray = getPropertiesMultipleIntValues(propertiesKeyTag, propFileDir);

        for (int i = startNum - 1; i < tmpIntArray.length; i = i + step) {
            intArrayList.add(tmpIntArray[i]);
        }

        final int[] multiplePropIntValues = new int[intArrayList.size()];
        for (int i = 0; i < intArrayList.size(); i++) {
            multiplePropIntValues[i] = intArrayList.get(i);
        }
        return multiplePropIntValues;
    }

    /**
     * @return Integer Array [] - conf_LTE.prop file array with Cause Code Cell values in order
     */
    public int[] getCauseCellArrayInt() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        final String[] tmpArray = getCauseCellArrayString();
        final int[] causeCellIntArray = new int[tmpArray.length];
        for (int i = 0; i < causeCellIntArray.length; i++) {
            causeCellIntArray[i] = Integer.parseInt(tmpArray[i]);
        }
        return causeCellIntArray;
    }

    /**
     * @return Successful Cell Cause Code Integer Array
     */
    @SuppressWarnings("null")
    public int[] getSuccessfulCauseCodeIntArray() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        final String counterPropFileDir = getAppPropSingleStringValue(confPropFile);
        loadPropertiesFile(counterPropFileDir);

        final int[] causeCellIntArray = getCauseCellArrayInt();
        final int arrayLength = causeCellIntArray.length;
        final int[] successCauseCellArray = new int[arrayLength];

        // Success rate in decimal
        final String sRate = getPropertiesSingleStringValue("Successful_Rate", counterPropFileDir);
        double successRate = Double.parseDouble(sRate);
        successRate = successRate / 100;

        if (causeCellIntArray != null) {
            for (int i = 0; i < arrayLength; i++) {
                successCauseCellArray[i] = (int) (causeCellIntArray[i] * successRate);// Calculate
            }
        }
        return successCauseCellArray;
    }

    /**
     * @return Unsuccessful Cell Cause Code Integer Array
     */
    public int[] getUnsuccessfulCauseCodeArrayString() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
        final String counterPropFileDir = getAppPropSingleStringValue(confPropFile);
        loadPropertiesFile(counterPropFileDir);

        final int[] causeCellArray = getCauseCellArrayInt();
        final int[] successfullCauseCellArray = getSuccessfulCauseCodeIntArray();
        final int[] unsuccessfullCauseCellArray = new int[successfullCauseCellArray.length];

        for (int i = 0; i < successfullCauseCellArray.length; i++) {
            unsuccessfullCauseCellArray[i] = causeCellArray[i] - successfullCauseCellArray[i];
        }
        return unsuccessfullCauseCellArray;
    }

    /**
     * @return String Array [] - conf_LTE.prop file array with Cause Code Cell values in order
     */
    public String[] getCauseCellArrayString() throws FileNotFoundException, IOException {
        final String counterPropFileDir = getAppPropSingleStringValue(confPropFile);
        loadPropertiesFile(counterPropFileDir);

        final String[] causecode0 = getPropertiesMultipleStringValues("Cause0_Cell", counterPropFileDir);
        final String[] causecode1 = getPropertiesMultipleStringValues("Cause1_Cell", counterPropFileDir);
        final String[] causecode2 = getPropertiesMultipleStringValues("Cause2_Cell", counterPropFileDir);
        final String[] causecode3 = getPropertiesMultipleStringValues("Cause3_Cell", counterPropFileDir);
        final String[] causecode4 = getPropertiesMultipleStringValues("Cause4_Cell", counterPropFileDir);

        final int c0 = causecode0.length - 1;
        final String[] causeCellStringArray = new String[causecode0.length * 5];
        int j = 0;

        for (int p = 0; p <= c0; p++) {
            causeCellStringArray[j] = causecode0[p];
            j++;
            causeCellStringArray[j] = causecode1[p];
            j++;
            causeCellStringArray[j] = causecode2[p];
            j++;
            causeCellStringArray[j] = causecode3[p];
            j++;
            causeCellStringArray[j] = causecode4[p];
            j++;
        }
        return causeCellStringArray;
    }

    /**
     * @return Bin Ranges
     */
    public int[] getBinRanges(final String binRangeName, final int binValLessNum) throws FileNotFoundException, IOException {
        final String counterPropFileDir = getAppPropSingleStringValue(confPropFile);
        loadPropertiesFile(counterPropFileDir);

        final String[] binRanges = getPropertiesMultipleStringValues(binRangeName, counterPropFileDir);
        final int arrayBinLen = binRanges.length;
        final int[] intBinArray = new int[arrayBinLen];

        if (binRangeName == "binRanges") {
            for (int q = 0; q < binRanges.length - 1; q++) {
                intBinArray[q] = (Integer.parseInt(binRanges[q])) - binValLessNum;
            }
            intBinArray[binRanges.length - 1] = 237 - binValLessNum;
        }

        else if (binRangeName == "binRanges_S1Setup") {
            for (int q = 0; q < binRanges.length - 1; q++) {
                intBinArray[q] = (Integer.parseInt(binRanges[q])) - binValLessNum;
            }
            intBinArray[binRanges.length - 1] = 9000 - binValLessNum;
        }

        else if (binRangeName == "pmUeCtxtRelTimeDistr_bin_range") {
            for (int q = 0; q < binRanges.length - 1; q++) {
                intBinArray[q] = (Integer.parseInt(binRanges[q])) - binValLessNum;
            }
            intBinArray[binRanges.length - 1] = 200 - binValLessNum;
        }

        else if (binRangeName == "binRangesPmErabID") {
            for (int q = 0; q < binRanges.length; q++) {
                intBinArray[q] = (Integer.parseInt(binRanges[q])) - binValLessNum;
            }
        }

        else if (binRangeName == "binRangesPmErabAD") {
            for (int q = 0; q < binRanges.length - 1; q++) {
                intBinArray[q] = (Integer.parseInt(binRanges[q])) - binValLessNum;
            }
            intBinArray[binRanges.length - 1] = 41 - binValLessNum;
        }
        return intBinArray;
    }

}
