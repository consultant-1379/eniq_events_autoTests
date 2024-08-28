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
package com.ericsson.eniq.events.ltees.applicationdrivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ericsson.eniq.events.ltees.controllers.file.FileController;
import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;
import com.ericsson.eniq.events.ltees.controllers.file.XmlController;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.FTPUtil;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.HTMLResultFileWriter;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.LogFileWriter;
import com.ericsson.eniq.events.ltees.environmentsetup.CheckFilesAndDirectories;
import com.ericsson.eniq.events.ltees.environmentsetup.EnableAndDisableDataGen;
import com.ericsson.eniq.events.ltees.environmentsetup.EnvironmentSetupDriver;
import com.ericsson.eniq.events.ltees.verifycounterfiles.VerifyXMLFileCountersGroupResults;
import com.ericsson.eniq.events.ltees.verifycounterfiles.VerifyXMLFileTopology;

public class AppDriver {

    final static FileController getFile = new FileController();

    final static FTPUtil ftpUpload = new FTPUtil();

    private final static VerifyXMLFileTopology topologyChecker = new VerifyXMLFileTopology();

    private final static PropertiesFileController propFile = new PropertiesFileController();

    static VerifyXMLFileCountersGroupResults groupResult = new VerifyXMLFileCountersGroupResults();

    private static LogFileWriter logWriter = new LogFileWriter();

    public static ArrayList<String> allCounterResults = new ArrayList<String>();

    public static ArrayList<String> topologyResults = new ArrayList<String>();

    public static String overallFileResult = null;

    public static String fileExtResult = null;

    private static ArrayList<String> nonEmptyResultFiles = new ArrayList<String>();

    final static String testTimestampStart = (new HTMLResultFileWriter()).getTestTimestamp();

    private static boolean runTopologyFlag = false;

    public static boolean verifyCounterFileFlag = true;

    public static boolean oldDataGen = true;

    public static boolean startConfigLTEES = true;

    public static boolean stopConfigLTEES = true;

    public static boolean isTopologyCachebuilt = false;

    public static final String STRING_ELEVEN_B = "11B";
   public static final String STRING_TWELVE_A = "12A";
   public static final String STRING_TWELVE_B = "12B";
    public static final String STRING_THIRTEEN_A = "13A";
    public static final String STRING_THIRTEEN_B = "13B";
    public static final String STRING_FOURTEEN_A = "14A";
    public static final String STRING_FOURTEEN_B = "14B";
    public static final String STRING_FIFTEEN_A = "15A";
    public static final String STRING_SIXTEEN_A = "16A";
    public static final String STRING_SIXTEEN_B = "16B";
    public static final String STRING_SEVENTEEN_A = "17A";
    public static final String STRING_PMS = "PMS";
    public static final String STRING_ALL_PMS = "ALLPMS";
    public static final String STRING_CTRS = "CTRS";
    public static final String STRING_ONE = "1";
    public static final String STRING_FIVE = "5";
    public static final String STRING_FIFTEEN = "15";
    public static String input = "";

    public enum ExecutionContext {
        ALL("LTEES"), ALL_PMS("ALL_PMS"),

        ELEVEN_B_PMS("11B_PMS"), TWELVE_A_PMS("12A_PMS"), TWELVE_B_PMS("12B_PMS"), ELEVEN_B_PMS_PLUS_TWELVE_A_PMS("11B+12A_PMS"), THIRTEEN_A_PMS(
                "13A_PMS"), THIRTEEN_B_PMS("13B_PMS"), TWELVE_B_PLUS_THIRTEEN_A_PMS("12B+13A_PMS"), FOURTEEN_A_PMS("14A_PMS"), FOURTEEN_B_PMS(
                "14B_PMS"), FIFTEEN_A_PMS("15A_PMS"), SIXTEEN_A_PMS("16A_PMS"), SIXTEEN_B_PMS("16B_PMS"), SEVENTEEN_A_PMS("17A_PMS"), ELEVEN_B_CTRS_ROP_ONE("11B_CTRS_1MT_ROP"), TWELVE_A_CTRS_ROP_ONE("12A_CTRS_1MT_ROP"), ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_ONE(
                "11B+12A_CTRS_1MT_ROP"),

        ELEVEN_B_CTRS_ROP_FIVE("11B_CTRS_5MT_ROP"), TWELVE_A_CTRS_ROP_FIVE("12A_CTRS_5MT_ROP"), ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_FIVE(
                "11B+12A_CTRS_5MT_ROP"), TWELVE_B_CTRS_ROP_FIVE("12B_CTRS_5MT_ROP"), THIRTEEN_A_CTRS_ROP_FIVE("13A_CTRS_5MT_ROP"), THIRTEEN_B_CTRS_ROP_FIVE(
                "13B_CTRS_5MT_ROP"),

        ELEVEN_B_CTRS_ROP_FIFTEEN("11B_CTRS_15MT_ROP"), TWELVE_A_CTRS_ROP_FIFTEEN("12A_CTRS_15MT_ROP"), TWELVE_B_CTRS_ROP_FIFTEEN("12B_CTRS_15MT_ROP"), THIRTEEN_A_CTRS_ROP_FIFTEEN(
                "13A_CTRS_15MT_ROP"), THIRTEEN_B_CTRS_ROP_FIFTEEN("13B_CTRS_15MT_ROP"), ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_FIFTEEN(
                "11B+12A_CTRS_15MT_ROP");

        private ExecutionContext(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        private String msg;
    }

    public static ExecutionContext executionContext;
    public static String executionContextSupport = "";

    private static void usage(String msg) {
        System.out.println(msg);
        System.out.println("Usage:");
        System.out.println("Valid number of arguments: 1 to 4");
        System.out.println("First argument : ALLPMS or ALL or pick from below list");
        System.out.println("Second argument: PMS or CTRS");
        System.out.println("Third argument : 1 or 5 or 15");
        System.out.println("Fourth argument : DoVerification or NoVerification for verification counter file or not");
        System.out.println("\n\n\n\n");
        System.out.println("Possible arguments");
        System.out.println("ALL");
        System.out.println("ALLPMS");
        System.out.println("12B+13A PMS");
        System.out.println("12B PMS");
        System.out.println("12B PMS CTR+CCTR");
        System.out.println("12B PMS CTR+CCTR+DUL");
        System.out.println("12B CTRS 5");
        System.out.println("13A PMS");
        System.out.println("13A CTRS 5");
        System.out.println("13B PMS");
        System.out.println("13B CTRS 5");
        System.out.println("14A PMS");
        System.out.println("14B PMS");
        System.out.println("15A PMS");
        System.out.println("16A PMS");
        System.out.println("16B PMS");
        System.out.println("17A PMS");
        System.out.println("13A PMS CTR+CCTR : NOT SUPPORTED NOW");
        System.out.println("13A PMS CTR+CCTR+DUL : NOT SUPPORTED NOW");
        System.out.println("13B PMS CTR+CCTR : NOT SUPPORTED NOW");
        System.out.println("13B PMS CTR+CCTR+DUL : NOT SUPPORTED NOW");
        System.out.println("11B CTRS 1 : NOT SUPPORTED NOW");
        System.out.println("11B CTRS 5 : NOT SUPPORTED NOW");
        System.out.println("11B CTRS 15 : NOT SUPPORTED NOW");
        System.out.println("12A CTRS 1 : NOT SUPPORTED NOW");
        System.out.println("12A CTRS 15 : NOT SUPPORTED NOW");
        System.out.println("12B CTRS 15 : NOT SUPPORTED NOW");
        System.out.println("13A CTRS 15 : NOT SUPPORTED NOW");
        System.out.println("13B CTRS 15 : NOT SUPPORTED NOW");
        System.out.println("11B+12A CTRS 1 : NOT SUPPORTED NOW");
        System.out.println("11B+12A CTRS 5 : NOT SUPPORTED NOW");
        System.out.println("11B+12A CTRS 15 : NOT SUPPORTED NOW");

        System.exit(0);
    }

    private static void initializeExecutionContext(String[] args) {
        if (args.length > 0 && args.length != 1)
            input = args[1];
        if (args.length == 0 || args.length > 3) {
            usage("Invalid number of arguments: " + args.length + "\n\n");
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("ALL")) {
                executionContext = ExecutionContext.ALL;
            } else if (args[0].equalsIgnoreCase("ALLPMS")) {
                executionContext = ExecutionContext.ALL_PMS;
            } else {
                usage("Invalid Argument " + args[0]);
            }
        } else if (args.length == 2) {
            /*if (args[0].equalsIgnoreCase(STRING_TWELVE_B) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.TWELVE_B_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.THIRTEEN_A_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_B) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.THIRTEEN_B_PMS;
            } else */if (args[0].equalsIgnoreCase(STRING_FOURTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.FOURTEEN_A_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_FOURTEEN_B) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.FOURTEEN_B_PMS;
            } /*else if (args[0].equalsIgnoreCase("12B+13A") && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.TWELVE_B_PLUS_THIRTEEN_A_PMS;
            } */else if (args[0].equalsIgnoreCase(STRING_FIFTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.FIFTEEN_A_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_SIXTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.SIXTEEN_A_PMS;
            }else if (args[0].equalsIgnoreCase(STRING_SIXTEEN_B) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.SIXTEEN_B_PMS;
            }else if (args[0].equalsIgnoreCase(STRING_SEVENTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)) {
                executionContext = ExecutionContext.SEVENTEEN_A_PMS;
            }else {
                usage("Invalid Arguments " + args[0] + " " + args[1]);
            }
        }/*else if (args.length == 3) {
            if (args[0].equalsIgnoreCase(STRING_TWELVE_B) && args[1].equalsIgnoreCase(STRING_CTRS)) {
                if (args[2].equals(STRING_FIVE)) {
                    executionContext = ExecutionContext.TWELVE_B_CTRS_ROP_FIVE;
                } else {
                    usage("Invalid Arguments " + args[0] + " " + args[1] + " " + args[2]);
                }
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_A) && args[1].equalsIgnoreCase(STRING_CTRS)) {
                if (args[2].equals(STRING_FIVE)) {
                    executionContext = ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE;
                } else {
                    usage("Invalid Arguments " + args[0] + " " + args[1] + " " + args[2]);
                }
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_B) && args[1].equalsIgnoreCase(STRING_CTRS)) {
                if (args[2].equals(STRING_FIVE)) {
                    executionContext = ExecutionContext.THIRTEEN_B_CTRS_ROP_FIVE;
                } else {
                    usage("Invalid Arguments " + args[0] + " " + args[1] + " " + args[2]);
                }
            } else if (args[0].equalsIgnoreCase(STRING_TWELVE_B) && args[1].equalsIgnoreCase(STRING_PMS) && args[2].equalsIgnoreCase("CTR+CCTR")) {
                executionContextSupport = "CTR+CCTR";
                executionContext = ExecutionContext.TWELVE_B_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS) && args[2].equalsIgnoreCase("CTR+CCTR")) {
                executionContextSupport = "CTR+CCTR";
                executionContext = ExecutionContext.THIRTEEN_A_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_B) && args[1].equalsIgnoreCase(STRING_PMS) && args[2].equalsIgnoreCase("CTR+CCTR")) {
                executionContextSupport = "CTR+CCTR";
                executionContext = ExecutionContext.THIRTEEN_B_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_TWELVE_B) && args[1].equalsIgnoreCase(STRING_PMS) && args[2].equalsIgnoreCase("CTR+CCTR+DUL")) {
                executionContextSupport = "CTR+CCTR+DUL";
                executionContext = ExecutionContext.TWELVE_B_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_A) && args[1].equalsIgnoreCase(STRING_PMS)
                    && args[2].equalsIgnoreCase("CTR+CCTR+DUL")) {
                executionContextSupport = "CTR+CCTR+DUL";
                executionContext = ExecutionContext.THIRTEEN_A_PMS;
            } else if (args[0].equalsIgnoreCase(STRING_THIRTEEN_B) && args[1].equalsIgnoreCase(STRING_PMS)
                    && args[2].equalsIgnoreCase("CTR+CCTR+DUL")) {
                executionContextSupport = "CTR+CCTR+DUL";
                executionContext = ExecutionContext.THIRTEEN_B_PMS;
            } else {
                usage("Invalid Arguments " + args[0] + " " + args[1] + " " + args[2]);
            }
        }*/

    }

    public static void main(final String[] args) throws Exception {

        if (args[args.length - 1].equalsIgnoreCase("DoVerification")) {
            verifyCounterFileFlag = true;
            String[] tmp = new String[args.length - 1];
            for (int i = 0; i < (args.length - 1); i++) {
                tmp[i] = args[i];
            }
            initializeExecutionContext(tmp);

        } else if (args[args.length - 1].equalsIgnoreCase("NoVerification")) {
            verifyCounterFileFlag = false;
            String[] tmp = new String[args.length - 1];
            for (int i = 0; i < (args.length - 1); i++) {
                tmp[i] = args[i];
            }
            initializeExecutionContext(tmp);
        } else {
            initializeExecutionContext(args);
        }
        final String runLocal = propFile.getAppPropSingleStringValue("runLocal");
        String username = System.getProperty("user.name");
        if (username != null && runLocal != null && runLocal.equalsIgnoreCase("n") && (!username.toUpperCase().contains("DCUSER"))) {
            System.out.println("LTEES Test Automation must be executed as 'dcuser'");
            System.exit(0);
        }
        System.out.println("\n\nLTEES Test Automation Started");
        System.out.println("\nPlease wait until test application execution is complete.........\n");

        final String onlyCounterVerification = propFile.getAppPropSingleStringValue("Counter_Verification_Only");
        if (!onlyCounterVerification.equalsIgnoreCase("y")) {
            EnvironmentSetupDriver.setInitialEnv();
        } else {
            final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
            final String counterLogFileDir = propFile.getAppPropSingleStringValue("CounterLogFile");
            final String overallXMLResults = propFile.getAppPropSingleStringValue("OverallFileResults");
            logWriter.createNewLogFile(topologyLogFileDir, "", false, true);
            logWriter.createNewLogFile(counterLogFileDir, "", false, true);
            logWriter.createNewLogFile(overallXMLResults, "", false, true);
        }

        /*if (executionContext == ExecutionContext.ELEVEN_B_PMS_PLUS_TWELVE_A_PMS) {
            execute(new String[] { STRING_ELEVEN_B, STRING_PMS });
            execute(new String[] { STRING_TWELVE_A, STRING_PMS });
        } else if (executionContext == ExecutionContext.TWELVE_B_PLUS_THIRTEEN_A_PMS) {
            execute(new String[] { STRING_TWELVE_B, STRING_PMS });
            execute(new String[] { STRING_THIRTEEN_A, STRING_PMS });
        } else if (executionContext == ExecutionContext.ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_ONE) {
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_ONE });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_ONE });
        } else if (executionContext == ExecutionContext.ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_FIVE) {
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_FIVE });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_FIVE });
        } else if (executionContext == ExecutionContext.ELEVEN_B_CTRS_PLUS_TWELVE_A_CTRS_ROP_FIFTEEN) {
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_FIFTEEN });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_FIFTEEN });
        } else */if (executionContext == ExecutionContext.ALL_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
           /* execute(new String[] { STRING_TWELVE_B, STRING_PMS });
            execute(new String[] { STRING_THIRTEEN_A, STRING_PMS });
            execute(new String[] { STRING_THIRTEEN_B, STRING_PMS });*/
            execute(new String[] { STRING_FOURTEEN_A, STRING_PMS });
            execute(new String[] { STRING_FOURTEEN_B, STRING_PMS });
            execute(new String[] { STRING_FIFTEEN_A, STRING_PMS });
            execute(new String[] { STRING_SIXTEEN_A, STRING_PMS });
            execute(new String[] { STRING_SIXTEEN_B, STRING_PMS });
            execute(new String[] { STRING_SEVENTEEN_A, STRING_PMS });
        } /*else if (executionContext == ExecutionContext.ALL) {
            execute(new String[] { STRING_ELEVEN_B });
            execute(new String[] { STRING_TWELVE_A });
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_ONE });
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_FIVE });
            execute(new String[] { STRING_ELEVEN_B, STRING_CTRS, STRING_FIFTEEN });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_ONE });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_FIVE });
            execute(new String[] { STRING_TWELVE_A, STRING_CTRS, STRING_FIFTEEN });
        }*/
        /*else if (executionContext == ExecutionContext.TWELVE_B_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_TWELVE_B, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.THIRTEEN_A_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_THIRTEEN_A, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.THIRTEEN_B_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_THIRTEEN_B, STRING_PMS });
        }*/
        else if (executionContext == ExecutionContext.FOURTEEN_A_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_FOURTEEN_A, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.FOURTEEN_B_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_FOURTEEN_B, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.FIFTEEN_A_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_FIFTEEN_A, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.SIXTEEN_A_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_SIXTEEN_A, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.SIXTEEN_B_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_SIXTEEN_B, STRING_PMS });
        }
        else if (executionContext == ExecutionContext.SEVENTEEN_A_PMS) {

            oldDataGen = false;
            startConfigLTEES = true;
            stopConfigLTEES = false;
            execute(new String[] { STRING_SEVENTEEN_A, STRING_PMS });
        }
        else {
            execute(args);
        }
        System.out.println("LTEES Test Automation COMPLETED\n\n");
        System.exit(0);
    }

    public static void execute(final String[] args) throws Exception {

        try {

            if (args[args.length - 1].equalsIgnoreCase("DoVerification")) {
                String[] tmp = new String[args.length - 1];
                for (int i = 0; i < (args.length - 1); i++) {
                    tmp[i] = args[i];
                }
                initializeExecutionContext(tmp);

            } else if (args[args.length - 1].equalsIgnoreCase("NoVerification")) {
                String[] tmp = new String[args.length - 1];
                for (int i = 0; i < (args.length - 1); i++) {
                    tmp[i] = args[i];
                }
                initializeExecutionContext(tmp);
            } else {
                initializeExecutionContext(args);
            }
            System.out.println("\n\nRunning Automation for " + executionContext.getMsg());

            final String testTimestampStartExecutionContext = (new HTMLResultFileWriter()).getTestTimestamp();
            /** Create Instance of logFiles **/
            final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
            final String counterLogFileDir = propFile.getAppPropSingleStringValue("CounterLogFile");
            final String overallXMLResults = propFile.getAppPropSingleStringValue("OverallFileResults");
            logWriter.createNewLogFile(topologyLogFileDir, testTimestampStartExecutionContext, true, false);
            logWriter.createNewLogFile(counterLogFileDir, testTimestampStartExecutionContext, true, false);
            logWriter.createNewLogFile(overallXMLResults, testTimestampStartExecutionContext, true, false);
            /** Server Only: File/Folder Structure & Topology Gen **/
            final String runLocal = propFile.getAppPropSingleStringValue("runLocal");
            if (runLocal.equalsIgnoreCase("n")) {
                final String onlyCounterVerification = propFile.getAppPropSingleStringValue("Counter_Verification_Only");
                if (!onlyCounterVerification.equalsIgnoreCase("y")) {
                    logWriter.systemConsoleLineDivider("=", true);
                    System.out.println("\nCHECK & CREATE DIRECTORIES AND FILES:");
                    logWriter.systemConsoleLineDivider("=", true);
                    EnvironmentSetupDriver.verifyFileAndDirectoryStructure(testTimestampStart);
                }
                if (!runTopologyFlag) {
                    if (!onlyCounterVerification.equalsIgnoreCase("y")) {
                        logWriter.systemConsoleLineDivider("=", true);
                        System.out.println("\nENABLE TOPOLOGY GENERATOR:");
                        logWriter.systemConsoleLineDivider("=", true);
                        EnvironmentSetupDriver.runTopologyGenerator();
                        runTopologyFlag = true;
                    }
                }
                if (!onlyCounterVerification.equalsIgnoreCase("y")) {
                    logWriter.systemConsoleLineDivider("=", true);
                    System.out.println("\nMEDIATION ZONE - ENABLING WORKFLOW GROUPS:");
                    logWriter.systemConsoleLineDivider("=", true);
                    final EnableAndDisableDataGen dataGen = new EnableAndDisableDataGen();
                    if (oldDataGen) {
                        dataGen.runDataGen("DataGen_Run_Time", false, verifyCounterFileFlag);
                    } else {

                        dataGen.prepareContextSpecificInput("DataGen_Run_Time", false, args[0]);
                    }

                }
            }

            logWriter.systemConsoleLineDivider("=", true);
            if (!verifyCounterFileFlag) {
                System.out.println("The flag for counter file verification is NoVerification, so NO FILE VERIFICATION stage.\n");
                System.out.println("Please go to /eniq/northbound/lte_event_stat_file/events_oss_<n>/dir<n> to check the generated counter file.\n");
            } else {
                System.out.println("\nSTARTING FILE VERIFICATION:");
                logWriter.systemConsoleLineDivider("=", true);

                /** Get XML Files from Multiple Directories and OSS-RCs **/
                final String inputFileDir = propFile.getAppPropSingleStringValue("InputFolder_Directory");
                final File parentDirectory = new File(inputFileDir);
                final String[] ossDirectoryNames = parentDirectory.list();

                /**
                 * Get All Files From Specified OSS/Directories (See app.properties)
                 **/
                final ArrayList<File> fileArray = getFile.getFilesFromOSSFolders(parentDirectory, false, false);

                /** Print to console if there are no files and exit application **/
                if (fileArray.size() == 0) {
                    System.out.println("There are NO output (gz/xml) files in folder");
                    logWriter.writeToLogFile(counterLogFileDir, "There are NO output (gz/xml) files in folder");
                    logWriter.systemConsoleLineDivider("=", true);
                    copyOutputHTMLAndlogFilesAndMoveBackupFiles(testTimestampStart);
                    logWriter.writeToLogFile(counterLogFileDir, "Application Terminated\n\n");
                    System.out.println("Application Terminated\n\n");
                    return;
                }

                /** MAIN APPLICATION START **/
                for (int j = 0; j < ossDirectoryNames.length; j++) {
                    final ArrayList<File> ossRCFiles = new ArrayList<File>();
                    final String ossRcDirName = ossDirectoryNames[j];

                    for (int a = 0; a < fileArray.size(); a++) {
                        final File file = fileArray.get(a);
                        final String fileNameA = file.toString();
                        if (fileNameA.indexOf(ossRcDirName) > 0) {
                            ossRCFiles.add(fileArray.get(a));
                        }
                    }

                    /*** Verify file format and GZip file ***/
                    final ArrayList<String> fileFormatCheckResults = getFile.verifyFileFormat(ossRCFiles);
                    final ArrayList<File> allFiles = getFile.unGZip(ossRCFiles, false);

                    /*** Add OSS Directory Names to LogFiles ***/
                    if (allFiles.size() > 0) {
                        final File ossDirectoryPath = new File(parentDirectory + "//" + ossRcDirName);
                        logWriter.systemConsoleLineDivider("*", true);
                        System.out.println("OSS Directory: " + ossDirectoryPath);
                        logWriter.systemConsoleLineDivider("*", true);

                        logWriter.writeOssDirectoryNameToLogFile(counterLogFileDir, ossRcDirName);
                        logWriter.writeOssDirectoryNameToLogFile(topologyLogFileDir, ossRcDirName);
                        logWriter.writeOssDirectoryNameToLogFile(overallXMLResults, ossRcDirName);
                    }

                    /**** Read and process each file ****/
                    for (int b = 0; b < allFiles.size(); b++) {
                        try {
                            final File file = allFiles.get(b);
                            final String fullFileName = file.toString();
                            final String fileName = getFile.getFileNameWithDirExt(fullFileName, true, false, false);

                            /*** XML File Extension Result ***/
                            final String counterPropKeyName = "CounterPropFile_Directory";
                            final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
                            final String yesNo = propFile.getPropertiesSingleStringValue("verifyFileFormat", counterPropFileDir);
                            fileExtResult = fileFormatCheckResults.get(b);
                            if (yesNo.equalsIgnoreCase("y")) {
                                if (fileExtResult.equals("FAIL")) {
                                    fileExtResult = "FAIL";
                                } else {
                                    fileExtResult = "PASS";
                                }
                            } else {
                                fileExtResult = "Not Selected";
                            }

                            /*** COUNTERS: Counter Checker ***/
                            logWriter.logFileDivider(counterLogFileDir, "_", true);
                            logWriter.writeToLogFile(counterLogFileDir, "XML FILE NAME (" + (b + 1) + "):  " + fileName);
                            logWriter.writeToLogFile(counterLogFileDir, "fullFileName=  " + fullFileName);
                            final String counterOnlyResult = groupResult.allCounterResultsOutput(fullFileName);

                            final LinkedHashSet<String> counterListOrdered = new LinkedHashSet<String>(XmlController.counterList);
                            logWriter.writeToLogFile(counterLogFileDir, "\n List Of Counters verified in file are:" + "\n");
                            for (String counter : counterListOrdered) {
                                logWriter.writeToLogFile(counterLogFileDir, counter + "\n");
                            }
                            logWriter.writeToLogFile(counterLogFileDir, "\n***Overall Counter Results: " + counterOnlyResult);

                            /***
                             * OVERALL RESULTS: Get and Add File Extension results to overall Pass/Fail Results (must be executed before 'Topology
                             * Checker'
                             ***/
                            final String completeTopologyResults = groupResult.checktopologyGroupResults(fullFileName);

                            if (fileExtResult.equals("FAIL") || completeTopologyResults.equals("FAIL") || counterOnlyResult.equals("FAIL")) {
                                overallFileResult = "FAIL";
                            } else {
                                overallFileResult = "PASS";
                            }

                            logWriter.writeToLogFile(counterLogFileDir, "***Overall Results (File Topology & Counters): " + overallFileResult);
                            if (!counterOnlyResult.equals("FAIL") && overallFileResult.equals("FAIL")) {
                                logWriter.writeToLogFile(counterLogFileDir, "***[See 'Topology Logfile.log' for overall FAILED results]");
                            }

                            /*** TOPOLOGY: Topology Checker ***/
                            logWriter.logFileDivider(topologyLogFileDir, "_", true);
                            logWriter.writeToLogFile(topologyLogFileDir, "XML FILE NAME (" + (b + 1) + "):  " + fileName);
                            final String[] resultsArray = topologyChecker.collectTopologyResultsForEachFile(fullFileName);
                            topologyResults = topologyChecker.collectAllResults(resultsArray);
                            topologyChecker.writeTopologyResultsToLogFile(resultsArray);

                            String overallToplogyResult = null;
                            final String topologyResulsts = groupResult.checktopologyGroupResults(fullFileName);
                            if (fileExtResult.equals("FAIL") || topologyResulsts.equals("FAIL")) {
                                overallToplogyResult = "FAIL";
                            } else {
                                overallToplogyResult = "PASS";
                            }

                            logWriter.writeToLogFile(topologyLogFileDir, "\n***Overall Topology Results: " + overallToplogyResult);
                            logWriter.writeToLogFile(topologyLogFileDir, "***Overall Results (File Topology & Counters): " + overallFileResult);
                            if (!overallToplogyResult.equals("FAIL") && overallFileResult.equals("FAIL")) {
                                logWriter.writeToLogFile(topologyLogFileDir, "***[See 'Counter Logfile.log' for overall FAILED results]");
                            }

                            /*** OVERALL RESULTS: Overall XML Results ***/
                            System.out.println(overallFileResult + " - XML FILE NAME (" + (b + 1) + "):  " + fileName);
                            logWriter.writeToLogFile(overallXMLResults, "XML FILE NAME (" + (b + 1) + "):  " + fileName + " - " + overallFileResult);
                        } catch (Exception E) {
                            System.out.println("EXCEPTION ==========================> ");
                            E.printStackTrace();
                        }
                    }

                    /*** Output File Directory ***/
                    final String outputFileDir = propFile.getAppPropSingleStringValue("OutputFolder_Directory");

                    /** TOPOLOGY: Topology Results to HTML **/
                    final String[] topologyColumnHeaders = topologyChecker.topologyColumnHeaders();
                    final String topologyLink = "Topology Logfile.log";

                    topologyResults = topologyChecker.mergeFileFormatResultsWithAllOtherResults(topologyColumnHeaders, fileFormatCheckResults,
                            topologyResults);

                    final String topologyHTMLFileName = outputFileDir + ossRcDirName + "_Topology_Results.html";
                    final HTMLResultFileWriter topologyHTMLFileWriter = new HTMLResultFileWriter();
                    nonEmptyResultFiles = topologyHTMLFileWriter.runHTMLFileWriter(topologyHTMLFileName, topologyColumnHeaders, topologyResults,
                            nonEmptyResultFiles, "Topology", ossRcDirName, testTimestampStartExecutionContext, topologyLink);

                    nonEmptyResultFiles.add(topologyLink);
                    topologyResults.clear();

                    /** COUNTERS: Counter Results to HTML File **/
                    final String[] counterColumnHeaders = groupResult.excelOutFileHeaders();
                    final String counterLink = "Counter Logfile.log";

                    final String counterHTMLFileName = outputFileDir + ossRcDirName + "_Counter_Results.html";
                    final HTMLResultFileWriter counterHTMLFileWriter = new HTMLResultFileWriter();

                    counterHTMLFileWriter.runHTMLFileWriter(counterHTMLFileName, counterColumnHeaders, allCounterResults, nonEmptyResultFiles,
                            "Counter", ossRcDirName, testTimestampStartExecutionContext, counterLink);

                    nonEmptyResultFiles.add(counterLink);
                    allCounterResults.clear();
                    topologyChecker.clearAllResultsArrayList();
                }

                /** SUMMARY: Generate Results Summary Page (HTML) **/
                final String outputFileDir = propFile.getAppPropSingleStringValue("OutputFolder_Directory");
                final HTMLResultFileWriter summaryPageHTMLResultWriter = new HTMLResultFileWriter();
                final String testTimestampEnd = summaryPageHTMLResultWriter.getTestTimestamp();
                summaryPageHTMLResultWriter.generateSummaryPageForLTEES(outputFileDir + "ltees_results_summary.html", nonEmptyResultFiles,
                        testTimestampStartExecutionContext, testTimestampEnd, false);
                nonEmptyResultFiles.clear();
                /** FTP: Transfer HTML Result Files to Unix Machine **/
                logWriter.writeToLogFile(topologyLogFileDir, "\n");
                logWriter.logFileDivider(topologyLogFileDir, "*", true);

                if (runLocal.equalsIgnoreCase("n")) {
                    ftpUpload.copyHtmlAndLogFilesToUnixBox(outputFileDir, topologyLogFileDir);
                }

                copyOutputHTMLAndlogFilesAndMoveBackupFiles(testTimestampStart);

                /*** Write Test End Time to Log Files ***/
                logWriter.writeToLogFile(topologyLogFileDir, "\n");
                logWriter.logFileDivider(topologyLogFileDir, "*", true);
                logWriter.writeToLogFile(topologyLogFileDir, "TEST END TIME: " + testTimestampEnd);
                logWriter.writeToLogFile(counterLogFileDir, "\n");
                logWriter.logFileDivider(counterLogFileDir, "*", true);
                logWriter.writeToLogFile(counterLogFileDir, "TEST END TIME: " + testTimestampEnd);
                logWriter.systemConsoleLineDivider("=", true);
            }

        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyOutputHTMLAndlogFilesAndMoveBackupFiles(String testTimestampStart) throws FileNotFoundException, IOException {

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final String outputFileDir = propFile.getAppPropSingleStringValue("OutputFolder_Directory");
        final String inputFileDir = propFile.getAppPropSingleStringValue("InputFolder_Directory");
        final String archiveDirectory = propFile.getAppPropSingleStringValue("ArchiveFolder_Directory");
        final String runLocal = propFile.getAppPropSingleStringValue("runLocal");
        final String archiveSubDirectory = archiveDirectory + testTimestampStart + getFile.getInputDirectorySeparator();
        final String archiveSubDirectoryExecutionContext = archiveSubDirectory + executionContext.getMsg() + getFile.getInputDirectorySeparator();
        final int lastIndexInputFileDirName = inputFileDir.lastIndexOf(getFile.getInputDirectorySeparator());
        final String inputFileDirName = inputFileDir.substring(lastIndexInputFileDirName + 1);
        final CheckFilesAndDirectories filesAndDirs = new CheckFilesAndDirectories();

        filesAndDirs.checkIfDirectoryExists(archiveDirectory);
        filesAndDirs.checkIfDirectoryExists(archiveSubDirectory);
        filesAndDirs.checkIfDirectoryExists(archiveSubDirectoryExecutionContext);

        ftpUpload.copyFilesToArchiveDirectory(outputFileDir, topologyLogFileDir, archiveSubDirectoryExecutionContext);
        /*** Delete all .xml files ***/
        getFile.deleteAllFilesWithExtension("xml");

        if (runLocal.equalsIgnoreCase("n")) {
            final String onlyCounterVerification = propFile.getAppPropSingleStringValue("Counter_Verification_Only");
            if (!onlyCounterVerification.equalsIgnoreCase("y")) {
                ftpUpload.moveInputFilesToArchive(inputFileDir, archiveSubDirectoryExecutionContext + inputFileDirName, testTimestampStart);
            }
            CopyDirectory cpDr = new CopyDirectory();
            cpDr.copyDirectory(new File(inputFileDir), new File(archiveSubDirectoryExecutionContext + inputFileDirName));
        }
    }
}

class CopyDirectory {
    public static void main(String[] args) throws IOException {
        CopyDirectory cd = new CopyDirectory();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the source directory or file name : ");
        String source = in.readLine();
        File src = new File(source);
        System.out.println("Enter the destination directory or file name : ");
        String destination = in.readLine();
        File dst = new File(destination);
        cd.copyDirectory(src, dst);
    }

    public void copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
            }

            String files[] = srcPath.list();
            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
            }
        } else {
            if (!srcPath.exists()) {
                System.out.println("File or directory does not exist.");
                System.exit(0);
            } else {
                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }
}