package com.ericsson.eniq.events.ltees.topologygenerator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author EEMOHAQ
 * @see Shared Directory for original file
 */
public class topology {

  public static void main(final String arg[]) {

    int oss = 1;
    String FILE_PATH = "//eniq//data//eventdata//events_oss_1//lteTopologyData//";
    if (arg.length > 1) {
      System.out.println("Invalid Argument...Aborting");
      System.exit(0);
    }
    for (int x = 0; x < arg.length; x++) {
      oss = Integer.parseInt(arg[x]);
    }
    if (oss > 4 || oss < 1) {
      System.out.println("Invalid OSS Instance...Aborting");
      System.exit(0);
    }

    File file;
    RandomAccessFile out;
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
    final String TIME_STAMP = YEAR + MONTH + DAY + "." + HOUR + MINUTE + "_"; // time
                                                                              // stamp
                                                                              // part
                                                                              // of
                                                                              // the
                                                                              // file
                                                                              // name
                                                                              // here

    int count = 0;
    for (int subset = 1; subset <= 2; subset++) {
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
          final String FILE_NAME = TIME_STAMP + "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00"
              + NODE_NUMBER + "_Full.xml";
          final String DIRECTORY = "dir" + (Math.abs(hash % 50) + 1) + "//";
          FILE_PATH = FILE_PATH.concat(DIRECTORY);
          final boolean success = (new File(FILE_PATH)).mkdir();
          if (success) {
            // Do nothing
          }
          file = new File(FILE_PATH.concat(FILE_NAME));
          out = new RandomAccessFile(file, "rw");
          createHeader(out, file);
          createMeContext(out, file, SUBSET, NODE_NUMBER);
          createManagedElement(out, file, SUBSET, NODE_NUMBER);
          createENodeBFunction(out, file, SUBSET, NODE_NUMBER);
          createEUtranCell(out, file, SUBSET, NODE_NUMBER);
          createInternalCellRelation(out, file, SUBSET, NODE_NUMBER);
          createExternalCellRelation(out, file, SUBSET, NODE_NUMBER);
          createInternalGERanCellRelation(out, file, "01", "001");
          createExternalGeRanCellRelation(out, file, "01", "001");
          createInternalUtranCellRelation(out, file, "01", "001");
          createExternalUtranCellRelation(out, file, "01", "001");
          createTermPointToENB(out, file, SUBSET, NODE_NUMBER);
          createProcessorLoad(out, file, SUBSET, NODE_NUMBER);
          createTermPointToMme(out, file, SUBSET, NODE_NUMBER);
          createFooter(out, file);
        }

        catch (final Exception e) {
          System.err.println("Error: " + e.getMessage());
        }

        count = count + 1;
        if (count % 100 == 0)
          System.out.println("Created " + count + " Files");
      }
    }
    for (int nodenumber = 1; nodenumber <= 2; nodenumber++) // run this for
    // 110
    // iterations
    {
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
        final String FILE_NAME = TIME_STAMP + "SubNetwork_ONRM_RootMo_R_MeContext_LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "_Full.xml";
        final String DIRECTORY = "dir" + (Math.abs(hash % 50) + 1) + "//";
        FILE_PATH = FILE_PATH.concat(DIRECTORY);
        final boolean success = (new File(FILE_PATH)).mkdir();
        if (success) {
          // Do nothing
        }
        file = new File(FILE_PATH.concat(FILE_NAME));
        out = new RandomAccessFile(file, "rw");
        createHeader(out, file);
        createMeContext(out, file, SUBSET, NODE_NUMBER);
        createManagedElement(out, file, SUBSET, NODE_NUMBER);
        createENodeBFunction(out, file, SUBSET, NODE_NUMBER);
        createEUtranCell(out, file, SUBSET, NODE_NUMBER);
        createInternalCellRelation(out, file, SUBSET, NODE_NUMBER);
        createExternalCellRelation(out, file, SUBSET, NODE_NUMBER);
        createInternalGERanCellRelation(out, file, "01", "001");
        createExternalGeRanCellRelation(out, file, "01", "001");
        createInternalUtranCellRelation(out, file, "01", "001");
        createExternalUtranCellRelation(out, file, "01", "001");
        createTermPointToENB(out, file, SUBSET, NODE_NUMBER);
        createProcessorLoad(out, file, SUBSET, NODE_NUMBER);
        createTermPointToMme(out, file, SUBSET, NODE_NUMBER);
        createFooter(out, file);
      }

      catch (final Exception e) {
        System.err.println("Error: " + e.getMessage());
      }

      count = count + 1;
      if (count % 100 == 0)
        System.out.println("Created " + count + " Files");

    }

    System.out.println("Total Files Created = " + count);

  }

  public static void createHeader(final RandomAccessFile out, final File file) {

    try {
      out.seek(file.length());
      out.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.write(0x0A);
      out.writeBytes("<!DOCTYPE model PUBLIC \"-//Ericsson NMS CIF CS//CS Export Filter DTD//\" \"export.dtd\">");
      out.write(0x0A);
      out.writeBytes("<model>");
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createFooter(final RandomAccessFile out, final File file) {

    try {
      out.seek(file.length());
      out.writeBytes("</model>");
      out.close();
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createMeContext(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"MeContextId\">LTE" + SUBSET + "ERBS00" + NODE_NUMBER + "</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"neMIMversion\">vB.1.3</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"userLabel\">LTE" + SUBSET + "ERBS00" + NODE_NUMBER + "</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"swVersion\">CXP90112123_R7A06</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createManagedElement(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"siteRef\">SubNetwork=ONRM_RootMo_R,Site=SITELTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + "</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"userLabel\">SITELTE" + SUBSET + "ERBS00" + NODE_NUMBER + "</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createENodeBFunction(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      final int ENODEB_ID = (subset - 1) * 160 + nodenumber;
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"eNodeBPlmnId\">");
      out.write(0x0A);
      out.writeBytes("<struct>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mcc\">191</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mnc\">91</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"mncLength\">2</attr>");
      out.write(0x0A);
      out.writeBytes("</struct>");
      out.write(0x0A);
      out.writeBytes("</attr>");
      out.write(0x0A);
      out.writeBytes("<attr name=\"eNBId\">" + ENODEB_ID + "</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createEUtranCell(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      for (int i = 1; i <= 4; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER + "-" + i
            + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cellId\">" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"tac\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"earfcn\">36000</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"activePlmnList\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"additionalPlmnList\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"EUtranCellFDDId\">LTE" + SUBSET + "ERBS00" + NODE_NUMBER + "-" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"userLabel\">LTE" + SUBSET + "ERBS00" + NODE_NUMBER + "-" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  
  
  public static void createInternalCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      for (int i = 1; i <= 3; i++) {
        final int k = i + 1;
        out.write(0x0A);
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-1,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1" + "C"
            + k + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + k + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

      }
      int h = 1;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-2,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2" + "C"
            + h + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + h + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);


        h = i + 2;
      }
      int j = 4;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-3,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C3" + "C"
            + j + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + j + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);


        j = i;
      }
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-4,EUtranFreqRelation=8,EUtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C4" + "C"
            + i + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);


      }

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());

    }


  }
  
  
  public static void createInternalUtranCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      for (int i = 1; i <= 3; i++) {
        final int k = i + 1;
        out.write(0x0A);
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-1,UtranFreqRelation=10563,UtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1" + "C"
            + k + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalUtranCellFDDRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + k + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"UtranCellRelationId\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
       
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }
      int h = 1;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-2,UtranFreqRelation=10563,UtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2" + "C"
            + h + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalUtranCellFDDRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + h + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"UtranCellRelationId\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
        h = i + 2;
      }
      int j = 4;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-3,UtranFreqRelation=10563,UtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C3" + "C"
            + j + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalUtranCellFDDRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + j + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"UtranCellRelationId\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
        j = i;
      }
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-4,UtranFreqRelation=10563,UtranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C4" + "C"
            + i + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalUtranCellFDDRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"UtranCellRelationId\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

      }

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }
  
  public static void createInternalGERanCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      for (int i = 1; i <= 3; i++) {
        final int k = i + 1;
        out.write(0x0A);
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-1,GeranFreqGroupRelation=14,GeranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1" + "C"
            + k + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"></attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"extGeranCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + k + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalGeranCellRef\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"GeranCellRelationId\">8</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }
      int h = 1;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-2,GeranFreqGroupRelation=14,GeranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2" + "C"
            + h + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"></attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"extGeranCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + h + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"GeranCellRelationId\">8</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
        h = i + 2;
      }
      int j = 4;
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-3,GeranFreqGroupRelation=14,GeranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C3" + "C"
            + j + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"></attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"extGeranCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + j + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalGeranCellRef\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"GeranCellRelationId\">8</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
        j = i;
      }
      for (int i = 1; i <= 3; i++) {
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" + "LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-4,GeranFreqGroupRelation=14,GeranCellRelation=" + "INTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C4" + "C"
            + i + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134241\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"adjacentCell\"></attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"createdBy\">1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"extGeranCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=" + "LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER + "-" + i + "</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"externalGeranCellRef\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"GeranCellRelationId\">8</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timeOfCreation\"/>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"timestampOfChange\">"+currentDateInString()+"</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

      }

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }
  
  
  
  public static void createExternalCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      int neighbor1 = nodenumber + 1;
      int neighbor2 = nodenumber + 2;

      if (subset < 25 && neighbor1 <= 160 && neighbor2 > 160)
        neighbor2 = 1;
      else if (subset < 25 && neighbor1 > 160 && neighbor2 > 160) {
        neighbor1 = 1;
        neighbor2 = 2;
      }

      if (subset >= 25 && neighbor1 <= 110 && neighbor2 > 110)
        neighbor2 = 1;
      else if (subset >= 25 && neighbor1 > 110 && neighbor2 > 110) {
        neighbor1 = 1;
        neighbor2 = 2;
      }
      final int ENBID_NEIGHBOR_1 = (subset - 1) * 160 + neighbor1;
      final int ENBID_NEIGHBOR_2 = (subset - 1) * 160 + neighbor2;

      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      String NEIGHBOR_2 = Integer.toString(neighbor2);
      if (NEIGHBOR_2.length() < 3 && NEIGHBOR_2.length() > 1)
        NEIGHBOR_2 = "0" + NEIGHBOR_2;
      else if (NEIGHBOR_2.length() < 2)
        NEIGHBOR_2 = "00" + NEIGHBOR_2;
      out.seek(file.length());

      // External Cell Relation between n1c1 n2c1
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-1,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1LTE" + SUBSET
            + "ERBS00" + NEIGHBOR_1
            + "C1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
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

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
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

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_1
            + "-1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"localCellId\">5</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }

      // External Cell Relation between n1c2 n2c2
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-2,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2LTE" + SUBSET
            + "ERBS00" + NEIGHBOR_1
            + "C2\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
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

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_1
            + "-2\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"localCellId\">6</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }

      // External Cell Relation between n1c1 n3c1
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-1,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C1LTE" + SUBSET
            + "ERBS00" + NEIGHBOR_2
            + "C1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_2 + "-1</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"eNodeBPlmnId\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mcc\">310</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mnc\">980</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mncLength\">3</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"eNBId\">" + ENBID_NEIGHBOR_2 + "</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_2
            + "-1\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"localCellId\">7</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }

      // External Cell Relation between n1c2 n3c2
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + "-2,EUtranFreqRelation=8,EUtranCellRelation=EXTLTE" + SUBSET + "ERBS00" + NODE_NUMBER + "C2LTE" + SUBSET
            + "ERBS00" + NEIGHBOR_2
            + "C2\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isRemoveAllowed\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"neighborCellRef\">SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_2 + "-2</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"anrCreated\">false</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"isHoAllowed\">true</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2,ExternalEUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NEIGHBOR_2
            + "-2\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"A.9.50.J.9.1\" lastModified=\"1285083134138\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"localCellId\">8</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);
      }

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public static void createExternalUtranCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      int neighbor1 = nodenumber + 1;
      int neighbor2 = nodenumber + 2;

      if (subset < 25 && neighbor1 <= 160 && neighbor2 > 160)
        neighbor2 = 1;
      else if (subset < 25 && neighbor1 > 160 && neighbor2 > 160) {
        neighbor1 = 1;
        neighbor2 = 2;
      }

      if (subset >= 25 && neighbor1 <= 110 && neighbor2 > 110)
        neighbor2 = 1;
      else if (subset >= 25 && neighbor1 > 110 && neighbor2 > 110) {
        neighbor1 = 1;
        neighbor2 = 2;
      }
      final int ENBID_NEIGHBOR_1 = (subset - 1) * 160 + neighbor1;
      final int ENBID_NEIGHBOR_2 = (subset - 1) * 160 + neighbor2;

      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      String NEIGHBOR_2 = Integer.toString(neighbor2);
      if (NEIGHBOR_2.length() < 3 && NEIGHBOR_2.length() > 1)
        NEIGHBOR_2 = "0" + NEIGHBOR_2;
      else if (NEIGHBOR_2.length() < 2)
        NEIGHBOR_2 = "00" + NEIGHBOR_2;
      out.seek(file.length());

      // External Cell Relation between n1c1 n2c1
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NODE_NUMBER + "-1\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"plmnIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mcc\">310</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mnc\">410</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mncLength\">2</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cellIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"rncId\">1048</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cId\">12781</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

       
      }

      // External Cell Relation between n1c2 n2c2

      // External Cell Relation between n1c1 n3c1
      {

        

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER
            + ",ManagedElement=1,ENodeBFunction=1,UtraNetwork=1,UtranFrequency=10563,ExternalUtranCellFDD=LTE"
            + SUBSET + "ERBS00" + NODE_NUMBER + "-2\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"plmnIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mcc\">310</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mnc\">410</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mncLength\">2</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cellIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"rncId\">1048</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cId\">12781</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

     }

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
  

  public static void createExternalGeRanCellRelation(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      int neighbor1 = nodenumber + 1;
      int neighbor2 = nodenumber + 2;

      if (subset < 25 && neighbor1 <= 160 && neighbor2 > 160)
        neighbor2 = 1;
      else if (subset < 25 && neighbor1 > 160 && neighbor2 > 160) {
        neighbor1 = 1;
        neighbor2 = 2;
      }

      if (subset >= 25 && neighbor1 <= 110 && neighbor2 > 110)
        neighbor2 = 1;
      else if (subset >= 25 && neighbor1 > 110 && neighbor2 > 110) {
        neighbor1 = 1;
        neighbor2 = 2;
      }
      final int ENBID_NEIGHBOR_1 = (subset - 1) * 160 + neighbor1;
      final int ENBID_NEIGHBOR_2 = (subset - 1) * 160 + neighbor2;

      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      String NEIGHBOR_2 = Integer.toString(neighbor2);
      if (NEIGHBOR_2.length() < 3 && NEIGHBOR_2.length() > 1)
        NEIGHBOR_2 = "0" + NEIGHBOR_2;
      else if (NEIGHBOR_2.length() < 2)
        NEIGHBOR_2 = "00" + NEIGHBOR_2;
      out.seek(file.length());

      // External Cell Relation between n1c1 n2c1
      {

        
    
        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER

            + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=LTE"
            + SUBSET + "ERBS00" + NODE_NUMBER + "-1\">");
        out.write(0x0A); 
        out.writeBytes("<attr name=\"plmnIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mcc\">310</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mnc\">410</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mncLength\">2</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cellIdentity\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"lac\">14901</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

        
      }

      // External Cell Relation between n1c2 n2c2

      // External Cell Relation between n1c1 n3c1
      {

        out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
            + SUBSET
            + "ERBS00"
            + NODE_NUMBER

            + ",ManagedElement=1,ENodeBFunction=1,GeraNetwork=1,ExternalGeranCell=LTE"
            + SUBSET + "ERBS00" + NODE_NUMBER + "-2\">");
        out.write(0x0A);
        out.writeBytes("<attr name=\"plmnIdentity\">");
        out.write(0x0A);
        out.writeBytes("<struct>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mcc\">310</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mnc\">410</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"mncLength\">2</attr>");
        out.write(0x0A);
        out.writeBytes("</struct>");
        out.write(0x0A);
        out.writeBytes("</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"cellIdentity\">15</attr>");
        out.write(0x0A);
        out.writeBytes("<attr name=\"lac\">14901</attr>");
        out.write(0x0A);
        out.writeBytes("</mo>");
        out.write(0x0A);
        out.write(0x0A);

        
      }

      // External Cell Relation between n1c2 n3c2


    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public static void createTermPointToENB(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      final int subset = Integer.parseInt(SUBSET);
      final int nodenumber = Integer.parseInt(NODE_NUMBER);
      int neighbor1 = nodenumber + 1;
      int neighbor2 = nodenumber + 2;

      if (subset < 25 && neighbor1 <= 160 && neighbor2 > 160)
        neighbor2 = 1;
      else if (subset < 25 && neighbor1 > 160 && neighbor2 > 160) {
        neighbor1 = 1;
        neighbor2 = 2;
      }

      if (subset >= 25 && neighbor1 <= 110 && neighbor2 > 110)
        neighbor2 = 1;
      else if (subset >= 25 && neighbor1 > 110 && neighbor2 > 110) {
        neighbor1 = 1;
        neighbor2 = 2;
      }

      String NEIGHBOR_1 = Integer.toString(neighbor1);
      if (NEIGHBOR_1.length() < 3 && NEIGHBOR_1.length() > 1)
        NEIGHBOR_1 = "0" + NEIGHBOR_1;
      else if (NEIGHBOR_1.length() < 2)
        NEIGHBOR_1 = "00" + NEIGHBOR_1;
      String NEIGHBOR_2 = Integer.toString(neighbor2);
      if (NEIGHBOR_2.length() < 3 && NEIGHBOR_2.length() > 1)
        NEIGHBOR_2 = "0" + NEIGHBOR_2;
      else if (NEIGHBOR_2.length() < 2)
        NEIGHBOR_2 = "00" + NEIGHBOR_2;

      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=1,TermPointToENB=BETWEEN"
          + NODE_NUMBER + "AND" + NEIGHBOR_1
          + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"TermPointToENBId\">20</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE" + SUBSET + "ERBS00" + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=2,TermPointToENB=BETWEEN"
          + NODE_NUMBER + "AND" + NEIGHBOR_2
          + "\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"TermPointToENBId\">40</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createProcessorLoad(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,Equipment=1,Subrack=1,Slot=2,PlugInUnit=2,GeneralProcessorUnit=2,ProcessorLoad=PROCESSOR01\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
      out.write(0x0A);
      out.writeBytes("<attr name=\"ProcessorLoadId\">0</attr>");
      out.write(0x0A);
      out.writeBytes("</mo>");
      out.write(0x0A);
      out.write(0x0A);
    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

  }

  public static void createTermPointToMme(final RandomAccessFile out, final File file, final String SUBSET,
      final String NODE_NUMBER) {

    try {
      out.seek(file.length());
      out.writeBytes("<mo fdn=\"SubNetwork=ONRM_RootMo_R,MeContext=LTE"
          + SUBSET
          + "ERBS00"
          + NODE_NUMBER
          + ",ManagedElement=1,ENodeBFunction=1,TermPointToMme=MME01\" mimName=\"ERBS_NODE_MODEL\" mimVersion=\"B.1.3.K.3.0\" lastModified=\"1294328445168\">");
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
      out.writeBytes("<attr name=\"servedPlmnListOtherRATs\" />");
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
      out.writeBytes("<attr name=\"mmeCodeListOtherRATs\">");
      out.write(0x0A);
      out.writeBytes("<seq count=\"0\">");
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

  }
  
  public static String currentDateInString()
  {
Date currentDate = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
String newDate = sdf.format(currentDate);
return newDate;
  }

}
