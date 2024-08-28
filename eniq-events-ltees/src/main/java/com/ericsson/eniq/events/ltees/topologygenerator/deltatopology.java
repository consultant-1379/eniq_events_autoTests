package com.ericsson.eniq.events.ltees.topologygenerator;

import java.io.*;
import java.util.*;

/**
 * @author EEMOHAQ
 * @see Shared Directory for original file
 */
public class deltatopology {

  public static void main(final String arg[]) {

    int oss = 1;
    final String FILE_PATH = "//eniq//data//eventdata//events_oss_1//lteTopologyData//";
    int rop = 1;

    if ((arg.length > 4) || (arg.length % 2 != 0)) // makes sure that valid
    // number of arguments
    // is either 0,2 or 4
    {
      System.out.println("Invalid Number of Arguments...Aborting");
      System.exit(0);
    }

    if (arg.length == 2) // if only one 1 argument is supplied
    {
      if (isItNumber(arg[1]) == false) // make sure argument 2 is numeric
      {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      // make sure argument 1 has either oss or rop
      if (arg[0].compareTo("-oss") != 0 && arg[0].compareTo("-rop") != 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-oss") == 0 && (Integer.parseInt(arg[1]) > 4 || Integer.parseInt(arg[1]) < 1)) {
        System.out.println("Invalid OSS Instance...Aborting");
        System.exit(0);
      } else if (arg[0].compareTo("-rop") == 0 && (Integer.parseInt(arg[1]) < 1)) {
        System.out.println("At Least 1 ROP is Required...Aborting");
        System.exit(0);
      }
      if (arg[0].compareTo("-oss") == 0 && arg[0].compareTo("-rop") != 0) {
        oss = Integer.parseInt(arg[1]); // only oss argument
      }

      if (arg[0].compareTo("-oss") != 0 && arg[0].compareTo("-rop") == 0) {
        rop = Integer.parseInt(arg[1]); // only rop argument
      }

    }

    if (arg.length == 4) // if both arguments are supplied
    {
      // make sure argument 2 and 4 are numeric
      if (isItNumber(arg[1]) == false || isItNumber(arg[3]) == false) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }
      // make sure argument 1 and 3 are non-numeric
      if (isItNumber(arg[0]) == true || isItNumber(arg[2]) == true) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }
      // make sure arguments are not duplicated for oss
      if (arg[0].compareTo("-oss") == 0 && arg[2].compareTo("-oss") == 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }
      // make sure arguments are not duplicated for rop
      if (arg[0].compareTo("-rop") == 0 && arg[2].compareTo("-rop") == 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-oss") == 0 && arg[2].compareTo("-rop") != 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-rop") == 0 && arg[2].compareTo("-oss") != 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-oss") != 0 && arg[2].compareTo("-rop") == 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-rop") != 0 && arg[2].compareTo("-oss") == 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-rop") != 0 && arg[0].compareTo("-oss") != 0) {
        System.out.println("Invalid Argument...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-oss") == 0 && (Integer.parseInt(arg[1]) > 4 || Integer.parseInt(arg[1]) < 1)) {
        System.out.println("Invalid OSS Instance...Aborting");
        System.exit(0);
      } else if (arg[0].compareTo("-rop") == 0 && (Integer.parseInt(arg[1]) < 1)) {
        System.out.println("At Least 1 ROP is Required...Aborting");
        System.exit(0);
      }

      if (arg[2].compareTo("-oss") == 0 && (Integer.parseInt(arg[3]) > 4 || Integer.parseInt(arg[3]) < 1)) {
        System.out.println("Invalid OSS Instance...Aborting");
        System.exit(0);
      } else if (arg[2].compareTo("-rop") == 0 && (Integer.parseInt(arg[3]) < 1)) {
        System.out.println("At Least 1 ROP is Required...Aborting");
        System.exit(0);
      }

      if (arg[0].compareTo("-oss") == 0) {
        oss = Integer.parseInt(arg[1]);
        rop = Integer.parseInt(arg[3]);
      }

      if (arg[0].compareTo("-rop") == 0) {
        oss = Integer.parseInt(arg[3]);
        rop = Integer.parseInt(arg[1]);
      }
    }

    int loop = 1;
    while (loop <= rop) // run 96 iterations for 24 hour period
    {
      execute(FILE_PATH, oss, loop);
      loop = loop + 1;
      if (loop > rop)
        break;
      try {
        Thread.sleep(900000); // run for 15 Minutes
      } catch (final InterruptedException ie) {
        ie.printStackTrace();
      }
    }
    System.out.println("Total Number of ROP = " + rop);

  } // end of main

  public static void execute(String FILE_PATH, final int oss, final int loop) {
    int count = 0;
    File file;
    RandomAccessFile out;
    for (int subset = 1; subset <= 24; subset++) {
      // run this for 10 iterations
      for (int nodenumber = 1; nodenumber <= 10; nodenumber++) {
        try {

          if (oss == 1)
            FILE_PATH = "//eniq//data//eventdata//events_oss_1//lteTopologyData//";
          if (oss == 2)
            FILE_PATH = "//eniq//data//eventdata//events_oss_2//lteTopologyData//";
          if (oss == 3)
            FILE_PATH = "//eniq//data//eventdata//events_oss_3//lteTopologyData//";
          if (oss == 4)
            FILE_PATH = "//eniq//data//eventdata//events_oss_4//lteTopologyData//";
          String SUBSET = Integer.toString(subset);
          if (SUBSET.length() < 2)
            SUBSET = "0" + SUBSET;
          String NODE_NUMBER = Integer.toString(nodenumber);
          if (NODE_NUMBER.length() < 3 && NODE_NUMBER.length() > 1)
            NODE_NUMBER = "0" + NODE_NUMBER;
          else if (NODE_NUMBER.length() < 2)
            NODE_NUMBER = "00" + NODE_NUMBER;
          final String FDN_NAME = "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00" + NODE_NUMBER;
          final int hash = FDN_NAME.hashCode();
          final String TIME_STAMP = getTimeStampForFileName();
          final String FILE_NAME = TIME_STAMP + "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00"
              + NODE_NUMBER + "_Delta.xml";
          final String DIRECTORY = "dir" + (Math.abs(hash % 50) + 1) + "//";
          FILE_PATH = FILE_PATH.concat(DIRECTORY);
          final boolean success = (new File(FILE_PATH)).mkdir();
          if (success) {
            // Do nothing
          }
          file = new File(FILE_PATH.concat(FILE_NAME));
          out = new RandomAccessFile(file, "rw");
          if (loop % 2 == 0) // even numbered ROP
          {
            createHeader(out, file);
            createCellRelation(out, file, SUBSET, NODE_NUMBER);
            createExternalENodeBFunction(out, file, SUBSET, NODE_NUMBER);
            createFooter(out, file);
          } else // odd numbered ROP
          {
            createHeader(out, file);
            deleteCellRelation(out, file, SUBSET, NODE_NUMBER);
            deleteExternalENodeBFunction(out, file, SUBSET, NODE_NUMBER);
            createFooter(out, file);
          }
        }

        catch (final Exception e) {
          System.err.println("Error: " + e.getMessage());
        }

        count = count + 1;
        if (count % 50 == 0)
          System.out.println("Created " + count + " Files");
      }
    }
    // run this for 15 iterations
    for (int nodenumber = 1; nodenumber <= 15; nodenumber++) {
      try {
        if (oss == 1)
          FILE_PATH = "//eniq//data//eventdata//events_oss_1//lteTopologyData//";
        if (oss == 2)
          FILE_PATH = "//eniq//data//eventdata//events_oss_2//lteTopologyData//";
        if (oss == 3)
          FILE_PATH = "//eniq//data//eventdata//events_oss_3//lteTopologyData//";
        if (oss == 4)
          FILE_PATH = "//eniq//data//eventdata//events_oss_4//lteTopologyData//";
        final String SUBSET = Integer.toString(25);
        String NODE_NUMBER = Integer.toString(nodenumber);
        if (NODE_NUMBER.length() < 3 && NODE_NUMBER.length() > 1)
          NODE_NUMBER = "0" + NODE_NUMBER;
        else if (NODE_NUMBER.length() < 2)
          NODE_NUMBER = "00" + NODE_NUMBER;
        final String FDN_NAME = "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00" + NODE_NUMBER;
        final int hash = FDN_NAME.hashCode();
        final String TIME_STAMP = getTimeStampForFileName();
        final String FILE_NAME = TIME_STAMP + "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "_Delta.xml";
        final String DIRECTORY = "dir" + (Math.abs(hash % 50) + 1) + "//";
        FILE_PATH = FILE_PATH.concat(DIRECTORY);
        final boolean success = (new File(FILE_PATH)).mkdir();
        if (success) {
          // Do nothing
        }
        file = new File(FILE_PATH.concat(FILE_NAME));
        out = new RandomAccessFile(file, "rw");
        if (loop % 2 == 0) // even numbered ROP
        {
          createHeader(out, file);
          modifyTermPointToMmeForEvenROP(out, file, SUBSET, NODE_NUMBER);
          createFooter(out, file);
        } else // odd numbered ROP
        {
          createHeader(out, file);
          modifyTermPointToMmeForOddROP(out, file, SUBSET, NODE_NUMBER);
          createFooter(out, file);
        }
      }

      catch (final Exception e) {
        System.err.println("Error: " + e.getMessage());
      }

      count = count + 1;
      if (count % 50 == 0)
        System.out.println("Created " + count + " Files");
    }

    System.out.println("Total Files Created at " + getTimeOfChange().substring(0, 2) + ":"
        + getTimeOfChange().substring(2, 4) + " = " + count);
  }// end of execute()

  public static String getTimeStampForFileName() {
    final Calendar calender = Calendar.getInstance();
    final String YEAR = Integer.toString(calender.get(Calendar.YEAR));
    String MONTH = Integer.toString(calender.get(Calendar.MONTH) + 1);
    if (MONTH.length() < 2)
      MONTH = "0" + MONTH;
    String DAY = Integer.toString(calender.get(Calendar.DAY_OF_MONTH));
    if (DAY.length() < 2)
      DAY = "0" + DAY;
    String HOUR = Integer.toString(calender.get(Calendar.HOUR_OF_DAY));
    if (HOUR.length() < 2)
      HOUR = "0" + HOUR;
    String MINUTE = Integer.toString(calender.get(Calendar.MINUTE));
    if (MINUTE.length() < 2)
      MINUTE = "0" + MINUTE;
    calender.add(Calendar.MINUTE, 15); // adding 15 minutes to current time
    String END_HOUR = Integer.toString(calender.get(Calendar.HOUR_OF_DAY));
    if (END_HOUR.length() < 2)
      END_HOUR = "0" + END_HOUR;
    String END_MINUTE = Integer.toString(calender.get(Calendar.MINUTE));
    if (END_MINUTE.length() < 2)
      END_MINUTE = "0" + END_MINUTE;
    calender.add(Calendar.MINUTE, 15);
    // time stamp part of the file
    final String TIMESTAMP = YEAR + MONTH + DAY + "." + HOUR + MINUTE + "-" + END_HOUR + END_MINUTE + "_";
    // name here
    return TIMESTAMP;
  } // end of getTimeStampForFileName()

  public static String getTimeOfChange() {
    final Calendar calender = Calendar.getInstance();
    String HOUR = Integer.toString(calender.get(Calendar.HOUR_OF_DAY));
    if (HOUR.length() < 2)
      HOUR = "0" + HOUR;
    String MINUTE = Integer.toString(calender.get(Calendar.MINUTE));
    if (MINUTE.length() < 2)
      MINUTE = "0" + MINUTE;
    String SECOND = Integer.toString(calender.get(Calendar.SECOND));
    if (SECOND.length() < 2)
      SECOND = "0" + SECOND;
    // time stamp part of the file name here
    final String TIMEOFCHANGE = HOUR + MINUTE + SECOND;

    return TIMEOFCHANGE;
  } // end of getTimeOfChange()

  public static void createHeader(final RandomAccessFile out, final File file) {

    try {
      out.seek(file.length());
      out.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.write(0x0A);
      out.writeBytes("<!DOCTYPE model PUBLIC \"\" \"deltaexport.dtd\">");
      out.write(0x0A);
      out.writeBytes("<model>");
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  } // end of createHeader()

  public static void createFooter(final RandomAccessFile out, final File file) {

    try {
      out.seek(file.length());
      out.writeBytes("</model>");
      out.close();
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of createFooter()

  public static void deleteCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.write(0x0A);
      // Deleting Internal Relation between C3 and C1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-3,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C3" + "C1"
          + "\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting Internal Relation between C4 and C1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-4,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C4" + "C1"
          + "\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of deleteCellRelation()

  public static void createCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.write(0x0A);
      // Creating Internal Relation between C3 and C1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-3,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C3" + "C1"
          + "\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
          + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-" + 1 + "</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"anrCreated\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating Internal Relation between C4 and C1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-4,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C4" + "C1"
          + "\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
          + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-" + 1 + "</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"anrCreated\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of createCellRelation()

  public static void deleteExternalENodeBFunction(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      final int neighbor1 = nodenumber + 1;
      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      out.seek(file.length());
      out.write(0x0A);
      // Deleting ExternalENodeBFunction
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext="
          + "LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1\" typeOfChange=\"Deleted\" timestampOfChange=\""
          + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting ExternalEUtranCellFDD for n2c1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-1\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting ExternalEUtranCellFDD for n2c2
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-2\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting TermPointToENB between Source and Neighbour 1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,TermPointToENB=BETWEEN"
          + NODE_NUMBER + "AND" + NEIGHBOR_1 + "\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting External Relation between n1c1 n2c1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-1,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1LTE" + SUBSET
          + "ERBS00" + NEIGHBOR_1 + "C1\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Deleting External Relation between n1c2 n2c2
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-2,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1LTE" + SUBSET
          + "ERBS00" + NEIGHBOR_1 + "C2\" typeOfChange=\"Deleted\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of deleteExternalENodeBFunction()

  public static void createExternalENodeBFunction(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      final int neighbor1 = nodenumber + 1;
      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      final int ENBID_NEIGHBOR_1 = (subset - 1) * 160 + neighbor1;
      out.seek(file.length());
      out.write(0x0A);
      // Creating ExternalENodeBFunction
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1\" typeOfChange=\"Created\" timestampOfChange=\""
          + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"eNodeBPlmnId\">");
      out.write(0x0A);
      out.writeBytes("<struct>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mcc\">426</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mnc\">57</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mncLength\">2</attr>");
      out.write(0x0A);
      out.writeBytes("</struct>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"eNBId\">" + ENBID_NEIGHBOR_1 + "</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating ExternalEUtranCellFDD for n2c1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-1\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"localCellId\">5</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating ExternalEUtranCellFDD for n2c2
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-2\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"localCellId\">6</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating TermPointToENB between Source and Neighbour 1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,TermPointToENB=BETWEEN"
          + NODE_NUMBER + "AND" + NEIGHBOR_1 + "\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange()
          + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"TermPointToENBId\">20</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating External Relation between n1c1 n2c1
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-1,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1LTE" + SUBSET
          + "ERBS00" + NEIGHBOR_1 + "C1\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-1</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"anrCreated\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

      // Creating External Relation between n1c2 n2c2
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "-2,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2LTE" + SUBSET
          + "ERBS00" + NEIGHBOR_1 + "C2\" typeOfChange=\"Created\" timestampOfChange=\"" + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
          + SUBSET + "ERBS00" + NEIGHBOR_1 + "-2</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"anrCreated\">false</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of createExternalENodeBFunction()

  public static void modifyTermPointToMmeForOddROP(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,TermPointToMme=MME01\" typeOfChange=\"Modified\" timestampOfChange=\""
          + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mmeCodeListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"1\">");
      out.write(0x0A);
      out.writeBytes("<item>1</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);

      out.writeBytes("<attr name=\"servedPlmnListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"1\">");
      out.write(0x0A);
      out.writeBytes("<item>");
      out.write(0x0A);
      out.writeBytes("<struct>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mcc\">666</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mnc\">99</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mncLength\">2</attr>");
      out.write(0x0A);
      out.writeBytes("</struct>");
      out.write(0x0A);
      out.writeBytes("</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mmeGIListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"1\">");
      out.write(0x0A);
      out.writeBytes("<item>1</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of modifyTermPointToMmeForOddROP() which will make CTR file
    // non-matching

  public static void modifyTermPointToMmeForEvenROP(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,TermPointToMme=MME01\" typeOfChange=\"Modified\" timestampOfChange=\""
          + getTimeOfChange() + "\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mmeCodeListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"3\">");
      out.write(0x0A);
      out.writeBytes("<item>4</item>");
      out.write(0x0A);
      out.writeBytes("<item>3</item>");
      out.write(0x0A);
      out.writeBytes("<item>1</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);

      out.writeBytes("<attr name=\"servedPlmnListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"1\">");
      out.write(0x0A);
      out.writeBytes("<item>");
      out.write(0x0A);
      out.writeBytes("<struct>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mcc\">426</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mnc\">857</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mncLength\">3</attr>");
      out.write(0x0A);
      out.writeBytes("</struct>");
      out.write(0x0A);
      out.writeBytes("</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mmeGIListLTERelated\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"3\">");
      out.write(0x0A);
      out.writeBytes("<item>1</item>");
      out.write(0x0A);
      out.writeBytes("<item>4</item>");
      out.write(0x0A);
      out.writeBytes("<item>2</item>");
      out.write(0x0A);
      out.writeBytes("</seq>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  } // end of modifyTermPointToMmeForEvenROP() which will make CTR file
    // matching

  public static boolean isItNumber(final String string) {
    try {
      Integer.parseInt(string);
    } catch (final NumberFormatException nfe) {
      return false;
    }
    return true;
  } // end of isItNumber()

} // end of class
