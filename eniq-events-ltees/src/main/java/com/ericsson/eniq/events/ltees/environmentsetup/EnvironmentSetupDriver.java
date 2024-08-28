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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ericsson.eniq.events.ltees.applicationdrivers.AppDriver;
import com.ericsson.eniq.events.ltees.applicationdrivers.AppDriver.ExecutionContext;
import com.ericsson.eniq.events.ltees.controllers.file.FileController;
import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.FTPUtil;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.HTMLResultFileWriter;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.LogFileWriter;
import com.ericsson.eniq.events.ltees.topologygenerator.topology;
import com.jcraft.jsch.JSchException;

public class EnvironmentSetupDriver {

    public static void setInitialEnv() throws FileNotFoundException, IOException, JSchException, InterruptedException {

        final PropertiesFileController propFile = new PropertiesFileController();
        final HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();
        final LogFileWriter logWriter = new LogFileWriter();
        final CheckFilesAndDirectories filesAndDirs = new CheckFilesAndDirectories();
        final FTPUtil ftpUpload = new FTPUtil();
        final boolean isSingleBladeServer = htmlWriter.getHostname("controlzone").equalsIgnoreCase(htmlWriter.getHostname("ec_ltees_1"));
        final String dataGenDirectory = propFile.getAppPropSingleStringValue("Directory_Data_Gen");
        final String dataGenFile = propFile.getAppPropSingleStringValue("File_Data_Gen");
        final String dataGenURL = propFile.getAppPropSingleStringValue("URL_Data_Gen");
        final String dataGenInputDirectory = propFile.getAppPropSingleStringValue("DATAGEN_INPUT_DIRECTORY");
        final String dataGenInputDirectoryZip = propFile.getAppPropSingleStringValue("DATAGEN_INPUT_DIRECTORY_ZIP");
        final String dataGenInputDirectoryZipURL = propFile.getAppPropSingleStringValue("URL_DATAGEN_INPUT_DIRECTORY_ZIP");
        final String dataGenRandomMzpFile = propFile.getAppPropSingleStringValue("DATAGEN_RANDOM_MZP_FILE");
        final String dataGenRandomMzpFileURL = propFile.getAppPropSingleStringValue("URL_DATAGEN_RANDOM_MZP_FILE");
        final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final String counterLogFileDir = propFile.getAppPropSingleStringValue("CounterLogFile");
        final String overallXMLResults = propFile.getAppPropSingleStringValue("OverallFileResults");

        EnableAndDisableDataGen obj = new EnableAndDisableDataGen();
        String mediationZonePath = "";
        final String anotherVersion = obj.IsAnotherVersionInstalled();

        if (anotherVersion.equals("N")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path");

        } else if (anotherVersion.equals("Y")) {
            mediationZonePath = propFile.getAppPropSingleStringValue("Mediation_Zone_Path_New");
        }

        logWriter.createNewLogFile(topologyLogFileDir, "", false, true);
        logWriter.createNewLogFile(counterLogFileDir, "", false, true);
        logWriter.createNewLogFile(overallXMLResults, "", false, true);

        filesAndDirs.checkIfDirectoryExists(dataGenDirectory);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(dataGenDirectory + File.separator + dataGenFile, dataGenURL, false);

        filesAndDirs.checkIfDirectoryExists(dataGenInputDirectory);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(dataGenInputDirectoryZip, dataGenInputDirectoryZipURL, false);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(dataGenRandomMzpFile, dataGenRandomMzpFileURL, false);
        final String cmdUnzipdataGenInputDirectoryZip = "/usr/bin/unzip " + dataGenInputDirectoryZip + " -d " + dataGenInputDirectory;
        final String cmdPcommitRandomMzp = mediationZonePath + "/mzsh mzadmin/dr " + "pcommit " + dataGenRandomMzpFile;
        if (!isSingleBladeServer) {
            ftpUpload.makeDirectoryOnMZServer(dataGenInputDirectory);
            ftpUpload.copyFileToMZServer(dataGenInputDirectoryZip, false);
            ftpUpload.copyFileToMZServer(dataGenRandomMzpFile, false);
        }
        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
        final String hostname = htmlWriter.getHostname("ec_ltees_1") + propFile.getAppPropSingleStringValue("MZ_Server_Host");

        System.out.println("Creating datagen directory and batch files");
        ExecutorRemote.executeCommandOnRemomteHost(user, password, hostname, cmdUnzipdataGenInputDirectoryZip, 60000);
        System.out.println("Executing random.mzp");
        ExecutorRemote.executeCommandOnRemomteHost(user, password, hostname, cmdPcommitRandomMzp, 60000);
        System.out.println("Importing Datagen Workflows");
        final EnableAndDisableDataGen dataGen = new EnableAndDisableDataGen();
        dataGen.importWorkflow();

    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws JSchException
     * @throws Exception
     */

    public static void verifyFileAndDirectoryStructure(final String testTimestampStart) throws FileNotFoundException, IOException, JSchException {
        final PropertiesFileController propFile = new PropertiesFileController();
        final HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();
        final FTPUtil ftpUpload = new FTPUtil();
        final CheckFilesAndDirectories filesAndDirs = new CheckFilesAndDirectories();
        final String testAutomationDir = propFile.getAppPropSingleStringValue("TestAutomation_Directory");
        final String confPropFile = propFile.getAppPropSingleStringValue("ConfPropFile_Directory");
        String confPropURL = propFile.getAppPropSingleStringValue("URL_Conf_Prop");
        if (AppDriver.executionContextSupport.equalsIgnoreCase("CTR+CCTR")) {
            confPropURL = propFile.getAppPropSingleStringValue("URL_Conf_Prop_12B_PMS_CTR_CCTR");
        } else if (AppDriver.executionContextSupport.equalsIgnoreCase("CTR+CCTR+DULL")) {
            confPropURL = propFile.getAppPropSingleStringValue("URL_Conf_Prop_12B_PMS_CTR_CCTR_DUL");
        }
        final String mzWorkflowNamesFile = propFile.getAppPropSingleStringValue("File_MZ_Workflows");
        final String mzWorkflowNamesURL = propFile.getAppPropSingleStringValue("URL_MZ_Workflows");
        final String topologyDataDir = propFile.getAppPropSingleStringValue("Directory_TopologyData");
        final String celltraceDataDir = propFile.getAppPropSingleStringValue("Directory_CellTraceData");
        final String regressionResultsDir = propFile.getAppPropSingleStringValue("OutputFolder_Directory");
        final String resultArchivesDirectory = propFile.getAppPropSingleStringValue("ArchiveFolder_Directory");
        final String propertiesDirectory = propFile.getAppPropSingleStringValue("Properties_Directory");
        final String propCounterFile = propFile.getAppPropSingleStringValue("CounterPropFile_Directory");
        final String sampResultsPropFile = propFile.getAppPropSingleStringValue("SampPropFile_Directory");
        final String xmlCounterFile = propFile.getAppPropSingleStringValue("CounterXMLPropFile_Directory");
        final String counterPropURL = propFile.getAppPropSingleStringValue("URL_Counter_Properties");
        final String sampResultsPropURL = propFile.getAppPropSingleStringValue("URL_Samp_Results_Properties");
        final String xmlCounterURL = propFile.getAppPropSingleStringValue("URL_XML_Counter");
        final String inputDirectoryProperty = propFile.getAppPropSingleStringValue("InputFolder_Directory");
        final String onlyCounterVerification = propFile.getAppPropSingleStringValue("Counter_Verification_Only");

        final boolean isSingleBladeServer = htmlWriter.getHostname("controlzone").equalsIgnoreCase(htmlWriter.getHostname("ec_ltees_1"));

        final String[] eventsOSS = { "events_oss_1", "events_oss_2", "events_oss_3", "events_oss_4" };
        final String[] resultFiles = { "Topology Logfile.log", "Delta Logfile.log", "Counter Logfile.log", "XML ResultsFile.log",
                "Delta_Results.html", "events_oss_1_Counter_Results.html", "events_oss_2_Counter_Results.html", "events_oss_3_Counter_Results.html",
                "events_oss_4_Counter_Results.html", "events_oss_1_Topology_Results.html", "events_oss_2_Topology_Results.html",
                "events_oss_3_Topology_Results.html", "events_oss_4_Topology_Results.html" };

        filesAndDirs.checkIfDirectoryExists(testAutomationDir);
        filesAndDirs.checkIfDirectoryExists(regressionResultsDir);
        File inputDirectory = new File(inputDirectoryProperty);
        if (!onlyCounterVerification.equalsIgnoreCase("y")) {
            if (inputDirectory.exists() && inputDirectory.isDirectory()) {
                File backupInputDirectory = new File(inputDirectoryProperty + "_backup");
                removeDirectory(backupInputDirectory);
                if (inputDirectory.renameTo(backupInputDirectory)) {
                    File newInputDirectory = new File(inputDirectoryProperty);
                    newInputDirectory.mkdirs();
                    newInputDirectory.setExecutable(inputDirectory.canExecute());
                    newInputDirectory.setReadable(inputDirectory.canRead());
                    newInputDirectory.setWritable(inputDirectory.canWrite());
                    for (int i = 0; i < eventsOSS.length; i++) {
                        new File(inputDirectoryProperty + "/" + eventsOSS[i]).mkdirs();
                    }
                }
            }
        }
        ftpUpload.makeDirectoryOnMZServer(celltraceDataDir);
        for (int i = 0; i < eventsOSS.length; i++) {

            String cellTraceDirsFullPath = "";
            if (AppDriver.executionContext == ExecutionContext.ELEVEN_B_PMS || AppDriver.executionContext == ExecutionContext.TWELVE_A_PMS
                    || AppDriver.executionContext == ExecutionContext.TWELVE_B_PMS || AppDriver.executionContext == ExecutionContext.THIRTEEN_A_PMS
                    || AppDriver.executionContext == ExecutionContext.THIRTEEN_B_PMS || AppDriver.executionContext == ExecutionContext.FOURTEEN_A_PMS
                    || AppDriver.executionContext == ExecutionContext.FOURTEEN_B_PMS || AppDriver.executionContext == ExecutionContext.FIFTEEN_A_PMS
                    || AppDriver.executionContext == ExecutionContext.SIXTEEN_A_PMS || AppDriver.executionContext == ExecutionContext.SIXTEEN_B_PMS
                    || AppDriver.executionContext == ExecutionContext.SEVENTEEN_A_PMS) {
                cellTraceDirsFullPath = celltraceDataDir + eventsOSS[i] + "/lteRbsCellTrace";
            } else if (AppDriver.executionContext == ExecutionContext.ELEVEN_B_CTRS_ROP_FIFTEEN
                    || AppDriver.executionContext == ExecutionContext.TWELVE_A_CTRS_ROP_FIFTEEN
                    || AppDriver.executionContext == ExecutionContext.ELEVEN_B_CTRS_ROP_FIVE
                    || AppDriver.executionContext == ExecutionContext.TWELVE_A_CTRS_ROP_FIVE
                    || AppDriver.executionContext == ExecutionContext.TWELVE_B_CTRS_ROP_FIVE
                    || AppDriver.executionContext == ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE
                    || AppDriver.executionContext == ExecutionContext.TWELVE_B_CTRS_ROP_FIFTEEN
                    || AppDriver.executionContext == ExecutionContext.THIRTEEN_A_CTRS_ROP_FIFTEEN) {
                cellTraceDirsFullPath = celltraceDataDir + eventsOSS[i];
            }

            ftpUpload.makeDirectoryRecursiveOnMZServer(cellTraceDirsFullPath);
            for (int j = 1; j <= 50; j++) {
                ftpUpload.makeDirectoryOnMZServer(cellTraceDirsFullPath + "/dir" + j);
            }
        }

        for (int i = 0; i < eventsOSS.length; i++) {
            ftpUpload.makeDirectoryOnMZServer(topologyDataDir + eventsOSS[i]);
        }

        for (int i = 0; i < eventsOSS.length; i++) {
            ftpUpload.makeDirectoryOnMZServer(topologyDataDir + eventsOSS[i] + "/lteTopologyData");
        }

        for (int i = 0; i < resultFiles.length; i++) {
            filesAndDirs.checkIfFileExists(regressionResultsDir + "/" + resultFiles[i]);
        }

        (new File(confPropFile)).delete();
        filesAndDirs.fetchNonExistingFilesFromUnixHost(confPropFile, confPropURL, false);
        filesAndDirs.checkIfDirectoryExists(resultArchivesDirectory);
        filesAndDirs.checkIfDirectoryExists(propertiesDirectory);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(mzWorkflowNamesFile, mzWorkflowNamesURL, false);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(propCounterFile, counterPropURL, false);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(sampResultsPropFile, sampResultsPropURL, false);
        filesAndDirs.fetchNonExistingFilesFromUnixHost(xmlCounterFile, xmlCounterURL, false);

        if (AppDriver.executionContext == ExecutionContext.ELEVEN_B_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == ExecutionContext.TWELVE_A_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == ExecutionContext.ELEVEN_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == ExecutionContext.TWELVE_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == ExecutionContext.TWELVE_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == ExecutionContext.TWELVE_B_CTRS_ROP_FIFTEEN
                || AppDriver.executionContext == ExecutionContext.THIRTEEN_A_CTRS_ROP_FIFTEEN) {
            setUpENodeBPluginSimulator();
            configureENodeBPluginSimulator();
        }

    }

    private static void setUpENodeBPluginSimulator() throws FileNotFoundException, IOException, JSchException {
        System.out.println("Setting Up the eNodeB Simulator plugin");
        final CheckFilesAndDirectories filesAndDirs = new CheckFilesAndDirectories();
        final PropertiesFileController propFile = new PropertiesFileController();
        final HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();
        final String pluginFile = propFile.getAppPropSingleStringValue("eNodeB_Plugin_Simulator");
        final String pluginFileURL = propFile.getAppPropSingleStringValue("URL_eNodeB_Plugin_Simulator");
        final String simulatorHostUsername = propFile.getAppPropSingleStringValue("eNodeB_Simulator_Host_Username");
        final String simulatorHostPassword = propFile.getAppPropSingleStringValue("eNodeB_Simulator_Host_Password");
        String simulatorHost = propFile.getAppPropSingleStringValue("eNodeB_Simulator_Host_IP");
        String simulatorListeningPorts = propFile.getAppPropSingleStringValue("eNodeB_Simulator_Listening_Ports");
        String streamingHost = propFile.getAppPropSingleStringValue("eNodeB_Streaming_Workflow_Host_IP");
        final String connectionFileName = propFile.getAppPropSingleStringValue("eNodeB_Connection_Filename");
        final String streamingPackage = propFile.getAppPropSingleStringValue("eNodeB_Streaming_Package_Path");
        final String ip2FdnMappingFile = streamingPackage + propFile.getAppPropSingleStringValue("eNodeB_IP_2_FDN_Mapping_File_Relative_Path");
        final String eNodeBSimulatorHostFDN = propFile.getAppPropSingleStringValue("eNodeB_Simulator_Host_FDN");
        final String connectionFileAbsolutePath = new FileController().getParentDir(pluginFile) + connectionFileName;
        boolean eNodeBPluginOnSameHost = true;
        filesAndDirs.fetchNonExistingFilesFromUnixHost(pluginFile, pluginFileURL, false);
        if (simulatorHost != null && !simulatorHost.equals("")) {
            if (!simulatorHost.toUpperCase().contains(htmlWriter.getHostname("controlzone").toUpperCase())
                    && simulatorHost.equals(htmlWriter.getIPAddress("controlzone"))) {
                eNodeBPluginOnSameHost = false;
                new FTPUtil().copyFile(pluginFile, false, simulatorHostUsername, simulatorHostPassword, simulatorHost);
            }
        } else {
            simulatorHost = htmlWriter.getIPAddress("controlzone");
        }

        final String cmdUnzipENodeBPluginSimulator = "/usr/bin/unzip " + pluginFile + " -d " + new FileController().getParentDir(pluginFile);
        ExecutorRemote.executeCommandOnRemomteHost(simulatorHostUsername, simulatorHostPassword, simulatorHost, cmdUnzipENodeBPluginSimulator, 30000);

        try {
            if (simulatorListeningPorts == null || simulatorListeningPorts.equals("")) {
                simulatorListeningPorts = "4000";
            }
            if (streamingHost == null || streamingHost.equals("")) {

                streamingHost = htmlWriter.getIPAddress("ec_2");
            }
            String[] ports = simulatorListeningPorts.split(",");
            String connectionFileContent = "";
            for (String port : ports) {
                connectionFileContent += "CELLTRACE," + streamingHost + "," + port + "\n";
            }
            File connectionFileFile = new File(connectionFileAbsolutePath);
            if (connectionFileFile.exists()) {
                connectionFileFile.delete();
            }
            new File(connectionFileAbsolutePath).createNewFile();
            final BufferedWriter out = new BufferedWriter(new FileWriter(connectionFileAbsolutePath));
            out.write(connectionFileContent);
            out.close();
            if (!eNodeBPluginOnSameHost) {
                new FTPUtil().copyFile(connectionFileAbsolutePath, true, simulatorHostUsername, simulatorHostPassword, simulatorHost);
            }

            File fdnmapFile = new File(ip2FdnMappingFile);
            if (fdnmapFile.exists()) {
                fdnmapFile.delete();
            }
            new File(ip2FdnMappingFile).createNewFile();
            String fdnFileContent = "events_oss_1," + simulatorHost + "," + eNodeBSimulatorHostFDN + "\n";
            final BufferedWriter outfdn = new BufferedWriter(new FileWriter(ip2FdnMappingFile));
            outfdn.write(fdnFileContent);
            outfdn.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void configureENodeBPluginSimulator() {

        //System.exit(0);
    }

    public static void runTopologyGeneratorOnRemote() throws FileNotFoundException, IOException, JSchException {
        final PropertiesFileController propFile = new PropertiesFileController();
        final CheckFilesAndDirectories filesAndDirs = new CheckFilesAndDirectories();
        final HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();
        final LogFileWriter logWriter = new LogFileWriter();

        final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
        final String user = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
        final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
        final String topologyFile = propFile.getAppPropSingleStringValue("Topology_Generator_File");
        final String topologyFileURL = propFile.getAppPropSingleStringValue("URL_Topology_Generator_File");
        final String topologyFilePathToCheck = ("/eniq/data/eventdata/events_oss_1/lteTopologyData/dir1");

        String hostname = htmlWriter.getHostname("ec_ltees_1") + propFile.getAppPropSingleStringValue("MZ_Server_Host");
        final String command = "java -cp /eniq/home/dcuser topology";
        filesAndDirs.fetchNonExistingFilesFromUnixHost(topologyFile, topologyFileURL, true);
        final boolean isSingleBladeServer = htmlWriter.getHostname("controlzone").equalsIgnoreCase(htmlWriter.getHostname("ec_ltees_1"));
        if (isSingleBladeServer) {
            hostname = htmlWriter.getHostname("controlzone") + propFile.getAppPropSingleStringValue("MZ_Server_Host");
        }
        for (int i = 1; i <= 4; i++) {
            String result = ExecutorRemote.executeCommandOnRemomteHost(user, password, hostname, command + " " + i, 20000);
            logWriter.writeToLogFile(topologyLogFile, "Remote execution command " + command);
            logWriter.writeToLogFile(topologyLogFile, "Remote execution result " + result);
            System.out.println("Result: " + result);
        }
        boolean directoryExists = false;
        try {
            FTPUtil ftp = new FTPUtil();
            FTPClient ftpClient;
            ftpClient = ftp.login(hostname, user, password);
            directoryExists = ftpClient.changeWorkingDirectory(topologyFilePathToCheck);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (!directoryExists) {
            for (int i = 1; i <= 4; i++) {
                String result = ExecutorRemote.executeCommandOnRemomteHost(user, password, hostname, command + " " + i, 20000);
                logWriter.writeToLogFile(topologyLogFile, "Remote execution command " + command);
                logWriter.writeToLogFile(topologyLogFile, "Re-generate topology files as failed previously");
            }
        } else {
            logWriter.writeToLogFile(topologyLogFile, "Topology files are successfully generated");
        }

    }

    public static void runTopologyGenerator() throws FileNotFoundException, IOException, JSchException {
        final HTMLResultFileWriter htmlWriter = new HTMLResultFileWriter();
        final boolean isSingleBladeServer = htmlWriter.getHostname("controlzone").equalsIgnoreCase(htmlWriter.getHostname("ec_ltees_1"));
        if (true) {
            runTopologyGeneratorOnRemote();
            return;
        }
        /*** Run Topology Gen ***/
        final LogFileWriter logWriter = new LogFileWriter();
        final PropertiesFileController propFile = new PropertiesFileController();
        final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");

        final String logEntry = "[Environment Setup] Enabling Topology Generator for OSS ";

        final String[] arg = new String[1];

        System.out.println(logEntry + "1...");
        logWriter.writeToLogFile(topologyLogFile, logEntry + "1...");
        arg[0] = "1";
        topology.main(arg);

        System.out.println(logEntry + "2...");
        logWriter.writeToLogFile(topologyLogFile, logEntry + "2...");
        arg[0] = "2";
        topology.main(arg);

        System.out.println(logEntry + "3...");
        logWriter.writeToLogFile(topologyLogFile, logEntry + "3...");
        arg[0] = "3";
        topology.main(arg);

        System.out.println(logEntry + "4...");
        logWriter.writeToLogFile(topologyLogFile, logEntry + "4...");
        arg[0] = "4";
        topology.main(arg);
    }

    public static boolean removeDirectory(File directory) {

        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry))
                        return false;
                } else {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return directory.delete();
    }
}
