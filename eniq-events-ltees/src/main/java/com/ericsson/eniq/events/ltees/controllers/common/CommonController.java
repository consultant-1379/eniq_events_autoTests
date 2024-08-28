/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.controllers.common;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author ESAIMKH
 * @since 2011
 * 
 */
public class CommonController {

  /** ************************* GENERAL COMMANDS *****************************/

  /**
   * @return String: Date and Time in simple date format "02-08-2012 12.10.45"
   */
  public String getDateAndTime() {
    final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
    final Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * @return Boolean True/False - Check if String is a number
   * @param string
   *          (String): Number String - i.e. "2", "15" etc
   */

  public boolean checkIfNumber(final String string) {
    try {
      Integer.parseInt(string);
    } catch (final NumberFormatException ex) {
      return false;
    }
    return true;
  }

  /** *********************** ARRAY MANIPULATION *****************************/

  /**
   * COMBINE ARRAYS
   * 
   * @return Combined ArrayList from 2 different ArrayLists - After each X
   *         (cutAtAfterXElements) element location in Array1 (mainArray1) , add
   *         X (addXNumOfValues) number of values from Array2 (addArray2)
   * @param mainArray1
   *          (String)ArrayList - Main array where you want to add additional
   *          elements to
   * @param addArray2
   *          (String)ArrayList - Array where you want to get additional
   *          elements from
   * @param cutAtAfterXElements
   *          (INT) - Cut location after X elements, i.e. after 5
   *          (cutAtAfterXElements) elements add 2 (addXNumOfValues)elements
   * @param addXNumOfValues
   *          (INT) - Add X number of elements after cut location
   *          (cutAtAfterXElements)
   */
  public ArrayList<String> combineArrays(final ArrayList<String> mainArray, final ArrayList<String> addArray,
      final int cutAtAfterXElements, final int addXNumOfValues) {
    final ArrayList<String> finalArray = new ArrayList<String>();
    int countAddArray = 0;
    int countMainArray = 0;

    final int mainArrayLenght = mainArray.size();
    final int addArrayLenght = addArray.size();
    final int loopTimes = (mainArrayLenght / cutAtAfterXElements) + 1;

    for (int i = 0; i < loopTimes; i++) {
      for (int x = 0; x < cutAtAfterXElements; x++) {
        if (countMainArray < mainArrayLenght) {
          finalArray.add(mainArray.get(countMainArray));
          countMainArray++;
        }
      }

      for (int y = 0; y < addXNumOfValues; y++) {
        if (countAddArray < addArrayLenght) {
          finalArray.add(addArray.get(countAddArray));
          countAddArray++;
        }
      }
    }
    return finalArray;
  }

  /**
   * CONVERT ARRAYS
   * 
   * @return String Array []- Convert String ArrayList to String[] Array
   * @param arrayListName
   *          (ArrayList<> String): String ArrayList that needs to be converted
   *          to String Array[]
   */
  public String[] convertStringArrayListToStringArray(final ArrayList<String> arrayListName) {
    final int arrayLength = arrayListName.size();
    final String[] stringArray = new String[arrayLength];
    for (int r = 0; r < arrayLength; r++) {
      stringArray[r] = arrayListName.get(r);
    }
    return stringArray;
  }

  /**
   * CONVERT ARRAYS
   * 
   * @return Integer Array []- Convert String ArrayList to Integer[] Array
   * @param arrayListName
   *          (ArrayList<> String): String ArrayList that needs to be converted
   *          to Integer Array[]
   */
  public int[] convertStringArrayListToIntArray(final ArrayList<String> arrayListName) {
    final String[] tmpStringArray = convertStringArrayListToStringArray(arrayListName);
    final int arrayLength = tmpStringArray.length;
    final int[] intArray = new int[arrayLength];
    for (int r = 0; r < arrayLength; r++) {
      intArray[r] = Integer.parseInt(tmpStringArray[r]);
    }
    return intArray;
  }

  /**
   * CONVERT ARRAYS
   * 
   * @return Integer Array []- Convert Integer ArrayList to Integer[] Array
   * @param arrayList
   *          (ArrayList<> String): Integer ArrayList that needs to be converted
   *          to Integer Array[]
   */
  public int[] convertIntegerArrayListToIntArray(final ArrayList<Integer> arrayList) {
    final int arrayLength = arrayList.size();
    final int[] tmpArrayInt = new int[arrayLength];
    for (int r = 0; r < arrayLength; r++) {
      tmpArrayInt[r] = arrayList.get(r);
    }
    return tmpArrayInt;
  }

  /**
   * CONVERT ARRAYS
   * 
   * @return File[] Array - Convert File OR String ArrayList to File[] Array
   * @param arrayList
   *          (ArrayList<>): String or File ArrayList
   */

  public File[] convertArrayListToFileArray(final ArrayList<?> arrayList) {
    final int arrayLength = arrayList.size();
    final File[] fileArray = new File[arrayLength];
    for (int r = 0; r < arrayLength; r++) {
      final File file = new File(arrayList.get(r).toString());
      fileArray[r] = file;
    }
    return fileArray;
  }

  /**
   * CONVERT ARRAYS
   * 
   * @return String[] Array - Convert File OR String ArrayList to String[] Array
   * @param arrayList
   *          (ArrayList<>): String or File ArrayList
   */

  public String[] convertArrayListToStringArray(final ArrayList<?> arrayList) {
    final int arrayLength = arrayList.size();
    final String[] stringArray = new String[arrayLength];

    for (int r = 0; r < arrayLength; r++) {
      stringArray[r] = arrayList.get(r).toString();
    }
    return stringArray;
  }

  /**
   * ARRAY: REMOVE DUPLICATE WITH ORDER
   * 
   * @return String[] Array - Remove duplicates from String Array & set order
   * @param arrayName
   *          (String Array[]): Name of the String Array
   * @param descOrder
   *          (Boolean): True for descending order, False for ascending order
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String[] removeDuplicatesWithOrderToStringArray(final String[] arrayName, final boolean descOrder) {
    final ArrayList<String> arrayList = new ArrayList<String>();
    for (int i = 0; i < arrayName.length; i++) {
      arrayList.add(arrayName[i]);
    }

    final HashSet h = new HashSet(arrayList);
    arrayList.clear();
    arrayList.addAll(h);
    Collections.sort(arrayList);

    if (descOrder) {
      final Comparator comparatorRev = Collections.reverseOrder();
      Collections.sort(arrayList, comparatorRev);
    }
    final String[] array = convertStringArrayListToStringArray(arrayList);
    return array;
  }

  /**
   * ARRAY: REMOVE DUPLICATE WITH ORDER
   * 
   * @return String[] - Remove duplicates from String ArrayList & set order
   * @param arrayList
   *          (String ArrayList<>): Name of the String ArrayList
   * @param descOrder
   *          (Boolean): True for descending order, False for ascending order
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String[] removeDuplicatesWithOrderToStringArray(final ArrayList<String> arrayList, final boolean descOrder) {
    final HashSet h = new HashSet(arrayList);
    arrayList.clear();
    arrayList.addAll(h);
    Collections.sort(arrayList);

    if (descOrder) {
      final Comparator comparatorRev = Collections.reverseOrder();
      Collections.sort(arrayList, comparatorRev);
    }

    final String[] array = convertStringArrayListToStringArray(arrayList);
    return array;
  }

  /**
   * ARRAY: REMOVE DUPLICATE WITH ORDER
   * 
   * @return String[] Array- Remove duplicates from String ArrayList & set order
   * @param arrayList
   *          (String ArrayList<>): Name of the String ArrayList
   * @param descOrder
   *          (Boolean): True for descending order, False for ascending order
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ArrayList<String> removeDuplicatesWithOrderToStringArrayList(final ArrayList<String> arrayList,
      final boolean descOrder) {
    final HashSet h = new HashSet(arrayList);
    arrayList.clear();
    arrayList.addAll(h);
    Collections.sort(arrayList);

    if (descOrder) {
      final Comparator comparatorRev = Collections.reverseOrder();
      Collections.sort(arrayList, comparatorRev);
    }
    return arrayList;
  }

  /**
   * ARRAY: REMOVE DUPLICATE WITH ORDER
   * 
   * @return INT[] Array - Remove duplicates from Integer ArrayList & set order
   * @param arrayList
   *          (Integer ArrayList<>): Name of the Integer ArrayList
   * @param descOrder
   *          (Boolean): True for descending order, False for ascending order
   */

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public int[] removeDuplicatesWithOrderToIntegerArray(final ArrayList<Integer> arrayList, final boolean descOrder) {
    final HashSet h = new HashSet(arrayList);
    arrayList.clear();
    arrayList.addAll(h);
    Collections.sort(arrayList);

    if (descOrder) {
      final Comparator comparatorRev = Collections.reverseOrder();
      Collections.sort(arrayList, comparatorRev);
    }
    final int[] array = convertIntegerArrayListToIntArray(arrayList);
    return array;
  }

  /**
   * CHECK ARRAY
   * 
   * @return Boolean - Check if 2 different integer arrays are exactly the same
   *         - i.e. contains same values at same element location
   * @param array1
   * @param array2
   */
  public boolean checkIf2ArraysContainSameElements(final int[] array1, final int[] array2) {
    boolean check = true;
    for (int x = 0; x < array1.length; x++) {
      if (!(array1[x] == array2[x])) {
        check = false;
        break;
      }
    }
    return check;
  }

}
