/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author eseuhon
 * @since 2011
 * This class will load all reserved data from a Excel file. Saved data in the excel file
 * can be different depends on each feature type of automation.
 *  
 * i.e. 
 * Test Case    IMSI                    APN              APN Group           Sgsn     Sgsn Group      
 * Common      460011000000001  smile.world     2G3G_Auto_APN1  SGSN1   2G3G_Auto_SGSN1
 * 7.3             460011000000002  test.net        2G3G_Auto_APN2  SGSN2   2G3G_Auto_SGSN2                               
 * 
 * You can set any reserved data required at each test by specifying test number in the "Test Case" column with new values.
 * Please note that the test number used in "Test Case" should be unique and same as your test case. 
 *   
 */
public class ReservedDataHelperReplacement {

    Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    Map<String, List<TypeValue>> allReservedData;

    public ReservedDataHelperReplacement(final String fileName) {
        logger.info("Loading reserved data from the " + fileName + " has been started.");
        this.allReservedData = loadAllReservedData(fileName);
        logger.info("Loading reserved data from the " + fileName + " has been completed.");
    }

    /**
     * This method will return reserved data saved in the excel file.
     * It will search if there is any matching test case number you are calling from
     * , if so it will return the data of matching type in the line of test case.
     * Otherwise this will return value in the line of "Common"
     *  
     * @param type type to load value from the excel file
     * @return 
     */
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

    /**
     * Parsing all reserved data in a Excel file and return those all values. 
     * 
     * @return Map<String, List<TypeValue>> allReservedData
     */
    private Map<String, List<TypeValue>> loadAllReservedData(final String fileName) {
        final Map<String, List<TypeValue>> testCasesMapToValues = new HashMap<String, List<TypeValue>>();
        FileInputStream input = null;

        try {
            logger.info("Path to load the reserved data file: " + PropertyReader.getInstance().getResourcesLocation()
                    + File.separator + fileName);
            input = new FileInputStream(PropertyReader.getInstance().getResourcesLocation() + File.separator + fileName);
            final HSSFWorkbook wb = new HSSFWorkbook(input);
            final Sheet sheet = wb.getSheetAt(0);
            final List<String> headers = getHeadersOfExcel(sheet);
            saveAllCellInformationAtEachRow(testCasesMapToValues, sheet, headers);
        } catch (final FileNotFoundException e) {
            logger.severe("can not find a file:" + PropertyReader.getInstance().getResourcesLocation() + File.separator
                    + fileName + " Please check if the file is existing or not.");
        } catch (final IOException e) {
            logger.warning("errors in file I/O operation with reservedData.csv file.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                    logger.warning("an error occured when closing a file I/O stream" + fileName);
                }
            }

        }
        return testCasesMapToValues;
    }

    private void saveAllCellInformationAtEachRow(final Map<String, List<TypeValue>> testCasesMapToValues,
            final Sheet sheet, final List<String> headers) {
        final List<TypeValue> typeToValues = new ArrayList<TypeValue>();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            final HSSFRow row = (HSSFRow) sheet.getRow(i);

            for (int c = 0; c < headers.size(); c++) {
                String value = null;
                final HSSFCell cell = row.getCell(c);

                if (cell == null) {
                    value = "";
                } else {
                    switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        value = Double.toString(cell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                    }
                }
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

    private List<String> getHeadersOfExcel(final Sheet sheet) {
        final List<String> headers = new ArrayList<String>();
        final HSSFRow row = (HSSFRow) sheet.getRow(0);
        final int cells = row.getPhysicalNumberOfCells();
        for (int c = 0; c < cells; c++) {
            final HSSFCell cell = row.getCell(c);
            headers.add(cell.getStringCellValue());
        }
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
}
