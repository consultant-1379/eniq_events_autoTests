/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;

import java.io.*;
import java.util.*;

//import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement.TypeValue;

/**
 * @author eannkee
 * @since 2013
 *
 */
public class CSVReaderCFAHFA {
    Map<String, List<TypeValue>> allReservedData;

    public CSVReaderCFAHFA(final String fileName) {
        System.err.println("Loading reserved data from the " + fileName + " has been started.");
        this.allReservedData = loadAllReservedData(fileName);
        System.err.println("Loading reserved data from the " + fileName + " has been completed.");
    }

    public Map<String, List<TypeValue>> loadAllReservedData(final String fileName) {
        final Map<String, List<TypeValue>> testCasesMapToValues = new HashMap<String, List<TypeValue>>();
        FileInputStream input = null;

        try {
            System.err.println("Path to load the reserved data file: " + fileName);
            input = new FileInputStream(PropertyReader.getInstance().getResourcesLocation() + File.separator + fileName);
            DataInputStream ois = new DataInputStream(input);
            BufferedReader br = new BufferedReader(new InputStreamReader(ois));

            List<String> headers = getHeadersOfExcel(br);
            saveAllCellInformationAtEachRow(testCasesMapToValues, br, headers);

        } catch (final FileNotFoundException e) {
            System.err.println("can not find a file:" + PropertyReader.getInstance().getResourcesLocation()
                    + File.separator + fileName + " Please check if the file is existing or not.");
        } catch (final IOException e) {
            System.err.println("errors in file I/O operation with reservedData.csv file.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                    System.err.println("an error occured when closing a file I/O stream" + fileName);
                }
            }

        }
        return testCasesMapToValues;
    }

    private List<String> getHeadersOfExcel(final BufferedReader br) throws IOException {
        final List<String> headers = new ArrayList<String>();
        String[] header = br.readLine().split(",");
        headers.addAll(Arrays.asList(header));
        System.err.println("Header : " + headers.toString());
        return headers;
    }

    private class TypeValue {
        private TypeValue(final String type, final String value) {
            this.type = type;
            this.value = value;
        }

        private String getType() {
            return type;
        }

        private String getValue() {
            return value;
        }

        final private String type;

        final private String value;
    }

    private void saveAllCellInformationAtEachRow(final Map<String, List<TypeValue>> testCasesMapToValues,
            final BufferedReader br, final List<String> headers) throws IOException {
        final List<TypeValue> typeToValues = new ArrayList<TypeValue>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokenizer = line.split(",", -1);
            for (int c = 0; c < headers.size(); c++) {
                String value = tokenizer[c];
                typeToValues.add(new TypeValue(headers.get(c), value));
            }
            testCasesMapToValues.put(typeToValues.get(0).getValue(), copyAllList(typeToValues));
        }
    }

    private List<TypeValue> copyAllList(final List<TypeValue> original) {
        final List<TypeValue> newList = new ArrayList<TypeValue>();
        for (final TypeValue value : original) {
            newList.add(value);
        }
        original.clear();
        return newList;
    }

    public String getReservedData(final String type, final String testCaseNumber) {
        final List<TypeValue> values = allReservedData.get(testCaseNumber);
        for (final TypeValue typeValue : values) {
            if (typeValue.getType().equals(type)) {
                return typeValue.getValue();
            }
        }
        return "NULL";
    }

    public String getReservedData(final String type) {
        final List<TypeValue> values = allReservedData.get(getTestCaseNumberToLoadReservedData());
        for (final TypeValue typeValue : values) {
            if (typeValue.getType().equals(type)) {
                return typeValue.getValue();
            }
        }
        return "NULL";
    }

    private String getTestCaseNumberToLoadReservedData() {
        final String testNum = getCurrentTestMethodNumber();
        if (allReservedData.get(testNum) != null) {
            return testNum;
        }
        return "Common";
    }

    private String getCurrentTestMethodNumber() {
        final StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();

        String methodName = "";
        for (final StackTraceElement element : stackTraceElements) {
            if (element.getMethodName().matches("\\S+(\\0137\\d+)+\\S+")) {
                methodName = element.getMethodName();
                break;
            }
        }

        final StringBuffer buffer = new StringBuffer();
        final int firstOfUnderscore = methodName.indexOf(GuiStringConstants.UNDERSCORE);
        if (firstOfUnderscore != -1) {
            final String testNumberWithUnderscore = methodName.substring(firstOfUnderscore + 1, methodName.length());
            for (final String currentNumber : testNumberWithUnderscore.split(GuiStringConstants.UNDERSCORE)) {
                buffer.append(currentNumber);
                buffer.append(GuiStringConstants.DOT);
            }

            final int startOfLastComma = buffer.lastIndexOf(GuiStringConstants.DOT);
            buffer.delete(startOfLastComma, buffer.length());
        }
        return buffer.toString();
    }

    //    public static void main(final String[] args) {
    //        CSVReaderCFAHFA s = new CSVReaderCFAHFA("WcdmaHfaReserveDataS.csv");
    //        System.err.println("Result: " + s.getReservedData("Impacted", "5.1"));
    //    }

}
