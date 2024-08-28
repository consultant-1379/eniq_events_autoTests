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

package com.ericsson.eniq.events.ltees.environmentsetup;

import com.ericsson.eniq.events.ltees.applicationdrivers.AppDriver;
import com.ericsson.eniq.events.ltees.controllers.file.DeltaFileController;
import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.HTMLResultFileWriter;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.LogFileWriter;
import com.jcraft.jsch.JSchException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EnableAndDisableDataGen {

    final static PropertiesFileController propFile = new PropertiesFileController();

    static LogFileWriter logWriter = new LogFileWriter();

    PropertiesFileController xmlFileReader = new PropertiesFileController();

    HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();

    static DeltaFileController delta = new DeltaFileController();

    public void runDataGen(final String runTimeAccordingToTestType, final boolean isDelta, final boolean verifyCounters) throws Exception {

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

        final HTMLResultFileWriter htmlResultWriter = new HTMLResultFileWriter();

        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");

        final String hostname = htmlWriter.getHostname("controlzone");
        System.out.println("CONTROL ZONE HOST NAME: " + hostname);
        String MZ_HOST_ID = propFile.getAppPropSingleStringValue("MZ_Server_Host");
        MZ_HOST_ID = hostname + MZ_HOST_ID;

        final String dataGenRunTimeString = propFile.getAppPropSingleStringValue(runTimeAccordingToTestType);
        String lteesRunTimeString = propFile.getAppPropSingleStringValue("EBSL_WFG_LTEES_Run_Time");

        logWriter.writeToLogFile(topologyLogFileDir, "\nUser: " + user);
        logWriter.writeToLogFile(topologyLogFileDir, "MZ_HOST_ID: " + MZ_HOST_ID + "\n");

        lteesWorkflowAction(true);
        if (AppDriver.input.equals("PMS")) {
            workflowAction("wfgroupenable", "DATA_GEN_GROUP_PMS");
            workflowAction("wfenable", "DATA_GEN_WF");
        } else {
            workflowAction("wfgroupenable", "DATA_GEN_GROUP_ENB");
            workflowAction("wfenable", "DATA_GEN_WF");
        }

        if (isDelta == true) {
            delta.runDeltaTopologyGenerator();

            if (AppDriver.input.equals("PMS")) {
                workflowAction("wfgroupdisable", "DATA_GEN_GROUP_PMS");
                workflowAction("wfdisable", "DATA_GEN_WF");
            } else {
                workflowAction("wfgroupdisable", "DATA_GEN_GROUP_ENB");
                workflowAction("wfdisable", "DATA_GEN_WF");
            }
        }

        else {

            /**
             * Put thread execution to sleep for specified time. Workflows will be disabled after this time has elapsed.
             **/

            if (verifyCounters == true) {
                int dataGenRunTime = Integer.parseInt(dataGenRunTimeString);

                logWriter.writeToLogFile(topologyLogFileDir, "[Mediation Zone] Data Gen Run Time (in minutes): " + dataGenRunTime);
                System.out.println("[Mediation Zone] Data Gen Run Time (in minutes): " + dataGenRunTime);

                dataGenRunTime = dataGenRunTime * 60;
                dataGenRunTime = dataGenRunTime * 1000;

                final String dataGenStartTime = htmlResultWriter.getTestTimestamp();
                System.out.println("\n[Mediation Zone] Data Gen Start Time: " + dataGenStartTime);
                logWriter.writeToLogFile(topologyLogFileDir, "\n[Mediation Zone] Data Gen Start Time: " + dataGenStartTime);
                pause(dataGenRunTime);
                final String dataGenEndTime = htmlResultWriter.getTestTimestamp();
                System.out.println("[Mediation Zone] Data Gen End Time: " + dataGenEndTime + "\n");
                logWriter.writeToLogFile(topologyLogFileDir, "[Mediation Zone] Data Gen End Time: " + dataGenEndTime + "\n");

                if (AppDriver.input.equals("PMS")) {
                    workflowAction("wfgroupdisable", "DATA_GEN_GROUP_PMS");
                    workflowAction("wfdisable", "DATA_GEN_WF");
                } else {
                    workflowAction("wfgroupdisable", "DATA_GEN_GROUP_ENB");
                    workflowAction("wfdisable", "DATA_GEN_WF");
                }
            }

            /**
             * Put thread execution to sleep for x mins, x is defined in app.properties file with key LTEES_ROP. LTE Workflows will run automatically
             * during this time, picking up the cell trace files and producing output XML files.
             **/
            if (lteesRunTimeString == null || lteesRunTimeString.equals("")) {
                lteesRunTimeString = "20";
            }
            int pauseTime = Integer.parseInt(lteesRunTimeString);
            ;

            final String sleepStartTime = htmlResultWriter.getTestTimestamp();
            System.out.println("\n" + sleepStartTime + " - putting thread execution to sleep for " + pauseTime + " mins.");
            logWriter.writeToLogFile(topologyLogFileDir, sleepStartTime + " - putting thread execution to sleep for " + pauseTime + " mins. "
                    + "LTE Workflows will run automatically during this time, " + "picking up the cell trace files and producing output XML files.");

            pauseTime = pauseTime * 60;
            pauseTime = pauseTime * 1000;
            pause(pauseTime);
            if (verifyCounters == true) {
                lteesWorkflowAction(false);
            }
            final String endSleepTime = htmlResultWriter.getTestTimestamp();
            System.out.println(endSleepTime + " - resuming thread execution.");
            logWriter.writeToLogFile(topologyLogFileDir, endSleepTime + " - resuming thread execution.\n");
        }
    }

    public void prepareContextSpecificInput(final String runTimeAccordingToTestType, final boolean isDelta, final String context) throws Exception {

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

        final HTMLResultFileWriter htmlResultWriter = new HTMLResultFileWriter();

        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");

        final String hostname = htmlWriter.getHostname("controlzone");

        String fileTimeZoneDetails = propFile.getAppPropSingleStringValue("FileTimeZoneDetails");

        if (null == fileTimeZoneDetails || fileTimeZoneDetails.length() < 1 || !(fileTimeZoneDetails.contains(","))) {
            fileTimeZoneDetails = "100,0,1,-1";
        }

        System.out.println("CONTROL ZONE HOST NAME: " + hostname);
        String MZ_HOST_ID = propFile.getAppPropSingleStringValue("MZ_Server_Host");
        MZ_HOST_ID = hostname + MZ_HOST_ID;

        final String dataGenRunTimeString = propFile.getAppPropSingleStringValue(runTimeAccordingToTestType);
        String lteesRunTimeString = propFile.getAppPropSingleStringValue("EBSL_WFG_LTEES_Run_Time");

        logWriter.writeToLogFile(topologyLogFileDir, "\nUser: " + user);
        logWriter.writeToLogFile(topologyLogFileDir, "MZ_HOST_ID: " + MZ_HOST_ID + "\n");

        if (AppDriver.startConfigLTEES) {
            lteesWorkflowAction(true);
        }
        if (!AppDriver.isTopologyCachebuilt) {
            if (!lteesWorkGroupAction("status")) {

                logWriter.writeToLogFile(topologyLogFileDir, "\n\nFAILURE :: TOPOLOGY CACHE COULD NOT BE PREPARED IN AN HOUR :: "
                        + " \nEXITING THE VALIDATION AS NO OUTPUT WILL BE GENERATED. \n "
                        + "\n PLEASE CHECK THE REASONS FOR DELAY IN GENERATION OF TOPOLOGY CACHE\n");
                System.out.println("\n\nFAILURE :: TOPOLOGY CACHE COULD NOT BE PREPARED IN AN HOUR :: \n ");

                System.exit(0);
            }
        }

        final String dataGenStartTime = htmlResultWriter.getTestTimestamp();
        logWriter.writeToLogFile(topologyLogFileDir, "\n[Datagen Decoupling] No Data Gen Now. File placement Start Time: " + dataGenStartTime);

        if (createSymLinks(context, fileTimeZoneDetails)) {
            logWriter.writeToLogFile(topologyLogFileDir, "Input File SymLink Creation succeeded for :: \n " + context);
        } else {
            logWriter.writeToLogFile(topologyLogFileDir, " Input File SymLink Creation FAILED for :: \n " + context);
        }
        final String dataGenEndTime = htmlResultWriter.getTestTimestamp();

        logWriter.writeToLogFile(topologyLogFileDir, "[Datagen Decoupling] No Data Gen Now. File placement Finish Time:" + dataGenEndTime + "\n");

        if (isDelta == true) {
            delta.runDeltaTopologyGenerator();
        }

        /**
         * Put thread execution to sleep for x mins, x is defined in app.properties file with key LTEES_ROP. LTE Workflows will run automatically
         * during this time, picking up the cell trace files and producing output XML files.
         **/
        if (lteesRunTimeString == null || lteesRunTimeString.equals("")) {
            lteesRunTimeString = "8";
        }

        /**
         * The wait time associated with the EBSL workflow need not be exceeding 8 min if running the application with ALLPMS parameter. Running the
         * application for individual version requires more time as specified in the app.properties.
         */
        if ((Integer.parseInt(lteesRunTimeString)) > 8) {
            lteesRunTimeString = "20";
        }

        int pauseTime = Integer.parseInt(lteesRunTimeString);

        final String sleepStartTime = htmlResultWriter.getTestTimestamp();
        System.out.println("\n" + sleepStartTime + " - putting thread execution to sleep for " + pauseTime + " mins.");
        logWriter.writeToLogFile(topologyLogFileDir, sleepStartTime + " - putting thread execution to sleep for " + pauseTime + " mins. "
                + "LTE Workflows will run automatically during this time, " + "picking up the cell trace files and producing output XML files.");
        if (!checkOP(pauseTime)) {
            System.out.println("\n" + " ---------------------- Output Counter Files are not generated with in 20 mins -------------------- ");
            logWriter.writeToLogFile(topologyLogFileDir, sleepStartTime + " - Output Counter Files are not generated with in 20 mins ");

        }

        if (AppDriver.stopConfigLTEES) {
            lteesWorkflowAction(false);
        }

        final String endSleepTime = htmlResultWriter.getTestTimestamp();
        System.out.println(endSleepTime + " - resuming thread execution.");
        logWriter.writeToLogFile(topologyLogFileDir, endSleepTime + " - resuming thread execution.\n");
    }

    private boolean checkOP(int maxTime) throws IOException, JSchException {
        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
        boolean filesGenerated = false;
        int noOfTries = 0;

        final String hostname = "ec_ltees_1";

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final LogFileWriter logWriter = new LogFileWriter();
        String cmd = "find /eniq/northbound/lte_event_stat_file/ -type f | wc -l ";

        while (!filesGenerated && noOfTries < maxTime) {
            noOfTries++;
            System.out.println(" [running command : " + cmd);
            String result = ExecutorRemote.executeComand(hostname, user, password, cmd);
            logWriter.writeToLogFile(topologyLogFileDir, "Remote execution command " + cmd);
            logWriter.writeToLogFile(topologyLogFileDir, "Remote execution result:\n" + result);
            System.out.println("Number of Output Counter Files Generated as of now: " + result + " LTEES RESULT:" + result);
            if (result.trim().equals("4")) {
                filesGenerated = true;
            } else {
                pause(60000);
            }
        }

        return filesGenerated;
    }

    private boolean lteesWorkGroupAction(String status) throws IOException, JSchException, ParserConfigurationException, SAXException {
        final String lteesConfigurationScriptPath = propFile.getAppPropSingleStringValue("LTEES_Configuration_Script_Path");
        String cmdToCheckTopoWfgStatus = lteesConfigurationScriptPath + " " + "-status | grep -i topology";
        String cmdToCheckWfgStatus = lteesConfigurationScriptPath + " " + "-status ";
        String result = "";
        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");

        final String hostname = htmlWriter.getHostname("controlzone") + propFile.getAppPropSingleStringValue("MZ_Server_Host");

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final LogFileWriter logWriter = new LogFileWriter();
        result = ExecutorRemote.executeConfigureLteesScriptStatus(hostname, user, password, cmdToCheckTopoWfgStatus);
        logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution command " + cmdToCheckTopoWfgStatus);
        logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution result:\n" + result);

        if (result.startsWith("OK")) {
            AppDriver.isTopologyCachebuilt = true;
            return true;
        }
        String statusresult = ExecutorRemote.executeLteesTopoCacheStatus(hostname, user, password, cmdToCheckWfgStatus);
        logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution command " + cmdToCheckWfgStatus);
        logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution result:\n" + statusresult);

        return false;
    }

    private boolean createSymLinks(String context, String fileTimeZoneDetails) throws IOException {

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final String fileLoc = propFile.getAppPropSingleStringValue("LocInputFiles");
        String destName = "";
        String destFileName = "";

        final File inputFolder = new File(fileLoc.toString());
        final File[] fileList = inputFolder.listFiles();
        ArrayList<File> ipfiles = new ArrayList<File>();

        String[] fileTZ = fileTimeZoneDetails.split(",");

        int curMin = getCurrentTimeMin();

        if ((curMin % 15) > 3) {
            logWriter.writeToLogFile(topologyLogFileDir,
                    "\n SymLink Creation :: Links will only be created before 03, 18, 33, and 48 min past every hour \n " + "Current time is  "
                            + curMin + " past hour. \n\n  Waiting [ " + (15 - (curMin % 15)) + " ] more min before generating "
                            + " SYMLINKs to files.");
            int waitTime = (15 - (curMin % 15)) * 60 * 1000;
            System.out.println("\n SymLink Creation :: Links will only be created before 03, 18, 33, and 48 min past every hour \n "
                    + "Current time is  " + curMin + " past hour. \n\n  Waiting [ " + (15 - (curMin % 15)) + " ] more min before generating "
                    + " SYMLINKs to files.");
            pause(waitTime);

            System.out.println("proceeding ahead to create SYMLINKS after wait of " + waitTime);
        }

        for (File f : fileList) {
            if (!f.isDirectory() && f.getName().startsWith("1") && f.getName().contains("DUL")) {
                ipfiles.add(f);
            }
        }

        if (context.equalsIgnoreCase("12B")) {

            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("12B")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "1/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "12B SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("13A")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("13A")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "2/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "13A SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("13B")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("13B")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "3/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "13B SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }

        if (context.equalsIgnoreCase("14A")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("14A")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "4/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "14A SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("14B")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("14B")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "5/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "14B SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("15A")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("15A")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "4/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "15A SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("16A")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("16A")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "4/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "16A SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("16B")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("16B")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "4/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "16B SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        if (context.equalsIgnoreCase("17A")) {
            for (File f1 : ipfiles) {
                if (f1.getName().startsWith("17A")) {
                    destName = f1.getName();
                    destName = destName.substring(destName.indexOf("_"));

                    for (int i = 1; i < 5; i++) {
                        String destDir = "/eniq/data/eventdata/events_oss_" + i + "/lteRbsCellTrace/dir" + i + "4/";
                        destFileName = updateName(destName, fileTZ[i - 1]);
                        try {
                            createSymlinkOnEc(fileLoc + f1.getName(), destDir + destFileName);
                            logWriter.writeToLogFile(topologyLogFileDir, "17A SymLink Creation :: \n " + fileLoc + f1.getName() + "\n to \n"
                                    + destDir + destFileName);
                        } catch (JSchException e) {
                            logWriter.writeToLogFile(topologyLogFileDir, "Unable to create symlink forinput file from \n " + fileLoc + f1.getName()
                                    + "\n to \n" + destDir + destFileName);
                        }
                    }
                }
            }
        }
        return true;
    }

    private void createSymlinkOnEc(String inputFile, String outFile) throws IOException, JSchException {

        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");

        final String hostname = "ec_ltees_1";

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final LogFileWriter logWriter = new LogFileWriter();
        String cmd = "ln -s " + inputFile + "  " + outFile;

        System.out.println(" [running command : " + cmd);
        String result = ExecutorRemote.executeComand(hostname, user, password, cmd);
        logWriter.writeToLogFile(topologyLogFileDir, "Remote execution command " + cmd);
        logWriter.writeToLogFile(topologyLogFileDir, "Remote execution result:\n" + result);
        System.out.println("Symbolic Link to the File creation Result:\n" + result);

    }

    private int getCurrentTimeMin() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd.HH:mm");
        SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd.");
        Date now = new Date();
        sdfDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        tf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = sdfDate.format(now);
        String timeNow = tf.format(now);
        String tzoffset = "";

        String hr = strDate.substring((strDate.indexOf(".") + 1), strDate.indexOf(":"));

        String min = strDate.substring(strDate.indexOf(":") + 1);

        return Integer.parseInt(min);

    }

    public String updateName(String ipFileName, String context) {
        String destName = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd.HH:mm");
        SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd.");
        Date now = new Date();
        sdfDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        tf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = sdfDate.format(now);
        String timeNow = tf.format(now);
        String tzoffset = "";

        String hr = strDate.substring((strDate.indexOf(".") + 1), strDate.indexOf(":"));

        String min = strDate.substring(strDate.indexOf(":") + 1);

        if (context.equals("-1")) {
            tzoffset = "-0100".trim();
            if (hr.equals("00")) {
                hr = "24";
            }
            hr = String.valueOf((Integer.parseInt(hr) - 1));
        } else if (context.equals("100")) {
            tzoffset = "".trim();
        } else {
            if (context.length() < 2) {
                tzoffset = ("+0" + context + "00").trim();
            } else {
                tzoffset = context + "00";
            }
            hr = String.valueOf((Integer.parseInt(hr) + Integer.parseInt(context)));
            if (Integer.parseInt(hr) >= 24) {
                hr = String.valueOf((Integer.parseInt(hr)) - 24);
            }

        }

        if (hr.length() < 2) {
            hr = "0" + hr;
        }

        if (Integer.parseInt(min) / 15 == 0) {
            String phr = String.valueOf((Integer.parseInt(hr) - 1));
            if (phr.length() < 2) {
                phr = "0" + phr;
            }
            if (phr.contains("-")) {
                phr = "23";
            }
            destName = "A" + timeNow + phr + "45" + tzoffset + "-" + hr + "00" + tzoffset;
        } else if (Integer.parseInt(min) / 15 == 1) {
            destName = "A" + timeNow + hr + "00" + tzoffset + "-" + hr + "15" + tzoffset;
        } else if (Integer.parseInt(min) / 15 == 2) {
            destName = "A" + timeNow + hr + "15" + tzoffset + "-" + hr + "30" + tzoffset;

        } else {
            destName = "A" + timeNow + hr + "30" + tzoffset + "-" + hr + "45" + tzoffset;
        }
        System.out.println("updateName(): FileName generated " + destName + ipFileName);
        return destName + ipFileName;
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void renameInputFiles() throws IOException {

    }

    /**
     * @author ekeviry
     * @throws IOException
     * @throws JSchException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @since 2011
     */
    private void workflowAction(final String action, final String tagName) throws JSchException, IOException, ParserConfigurationException,
            SAXException {

        final String fileName = propFile.getAppPropSingleStringValue("File_MZ_Workflows");

        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");

        final String hostname = htmlWriter.getHostname("controlzone");
        String MZ_HOST_ID = propFile.getAppPropSingleStringValue("MZ_Server_Host");
        MZ_HOST_ID = hostname + MZ_HOST_ID;

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

        String mediationZonePath = "";
        final String anotherVersion = IsAnotherVersionInstalled();

        if (anotherVersion.equals("N")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path");

        } else if (anotherVersion.equals("Y")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path_New");
        }

        final String[] workflowNames = xmlFileReader.readPropertiesFile(tagName, fileName);

        for (int i = 0; i < workflowNames.length; i++) {
            final String mz_enable_WorkflowGroup;
            if (workflowNames[i].startsWith("Sim")) {
                mz_enable_WorkflowGroup = " " + mediationZonePath + "/mzsh mzadmin/dr " + action + " " + workflowNames[i];
            } else
                mz_enable_WorkflowGroup = " " + "/eniq/mediation_sw/mediation_gw/bin" + "/mzsh mzadmin/dr " + action + " " + workflowNames[i];

            final String statusEnableGroupOutput = ExecutorRemote.executeComand(MZ_HOST_ID, user, password, mz_enable_WorkflowGroup).trim();

            System.out.println("[Mediation Zone] " + statusEnableGroupOutput);
            logWriter.writeToLogFile(topologyLogFileDir, "[Mediation Zone] " + statusEnableGroupOutput);
        }
    }

    public void importWorkflow() throws JSchException, IOException {
        logWriter.systemConsoleLineDivider("=", true);
        System.out.println("\nIMPORT  WORKFLOWS:");
        logWriter.systemConsoleLineDivider("=", true);

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final String dataGenDirectory = propFile.getAppPropSingleStringValue("Directory_Data_Gen");
        final String dataGenFile = propFile.getAppPropSingleStringValue("File_Data_Gen");
        final String workFlowZipFile = dataGenDirectory + File.separator + dataGenFile;
        String mediationZonePath = "";
        final String mz_enable_WorkflowGroup;
        final String anotherVersion = IsAnotherVersionInstalled();

        if (anotherVersion.equals("N")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path");

        } else if (anotherVersion.equals("Y")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path_New");
        } else {
            System.out.println("NO MZ path selected");
        }
        mz_enable_WorkflowGroup = mediationZonePath + "/mzsh mzadmin/dr systemimport " + workFlowZipFile;
        System.out.println("WF Import Path: " + mz_enable_WorkflowGroup);
        Runtime.getRuntime().exec(mz_enable_WorkflowGroup);
        try {
            System.out.println("Wait 2 mins to import the datagen");
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[Mediation Zone] Importing Data Gen Workflow completed");
        logWriter.writeToLogFile(topologyLogFileDir, "[Mediation Zone] Importing Data Gen Workflow completed");

    }

    public void lteesWorkflowAction(boolean enable) throws IOException, JSchException, ParserConfigurationException, SAXException {

        final String lteesConfigurationScriptPath = propFile.getAppPropSingleStringValue("LTEES_Configuration_Script_Path");
        String cmd = lteesConfigurationScriptPath + " " + "-start";
        String cmd_to_disable = lteesConfigurationScriptPath + " " + "-stop";
        String cmdToCheckWfgStatus = "/eniq/mediation_sw/mediation_gw/bin/mzsh mzadmin/dr wfgrouplist |grep EBSL";

        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");

        final String hostname = htmlWriter.getHostname("controlzone") + propFile.getAppPropSingleStringValue("MZ_Server_Host");

        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final LogFileWriter logWriter = new LogFileWriter();
        if (enable) {
            if (enable) {
                ExecutorRemote.executeConfigureLteesScript(hostname, user, password, cmd, "true");
                System.out.println("Wait 3 mins to check EBSL workflow status");
                pause(180000);
                String result = ExecutorRemote.executeComand(hostname, user, password, cmdToCheckWfgStatus);
                logWriter.writeToLogFile(topologyLogFileDir, "Remote execution command " + cmdToCheckWfgStatus);
                logWriter.writeToLogFile(topologyLogFileDir, "Remote execution result:\n" + result);
                System.out.println("Result:\n" + result);
            } else {
                ExecutorRemote.executeConfigureLteesScript(hostname, user, password, cmd, "false");
            }
        } else {
            ExecutorRemote.executeConfigureLteesScript(hostname, user, password, cmd_to_disable, "true");
            System.out.println("Wait 660 seconds to finish disable EBSL workflow groups");
            pause(180000);
            String result = ExecutorRemote.executeComand(hostname, user, password, cmdToCheckWfgStatus);
            logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution command " + cmdToCheckWfgStatus);
            logWriter.writeToLogFile(topologyLogFileDir, "\n\n\nRemote execution result:\n" + result);
        }
    }

    public String IsAnotherVersionInstalled() throws FileNotFoundException, IOException {
        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
        final String hostname = htmlWriter.getHostname("controlzone");
        String MZ_HOST_ID = propFile.getAppPropSingleStringValue("MZ_Server_Host");
        String newMZPath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path_New");

        if (new File(newMZPath).isDirectory()) {
            return "Y";
        } else {
            return "N";
        }
    }

    public static void pause(final int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (final InterruptedException e) {
        }
    }

}
