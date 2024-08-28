package com.ericsson.eniq.events.notification.test.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ericsson.eniq.events.notification.test.automation.main.AutomationMain;
import com.ericsson.eniq.events.notification.test.automation.main.Dataloader;
import com.ericsson.eniq.events.notification.test.automation.main.ResourceUtils;
/**
 * This class FTP the logs and html reports ro unix shared server.
 * @author ekrchai
 *
 */

public class FTPUtil {
     static String hostName = null;
    final PropertiesFileController propFile = new PropertiesFileController();
    public void copyHtmlAndLogFilesToUnixBox(final String outputFileDir) throws FileNotFoundException, IOException {

        final File parentDirectory = new File(outputFileDir);
        final String[] fileList = parentDirectory.list();
        propFile.loadPropertiesFile(ResourceUtils.getResource("unix_host_and_db.properties"));
        final String hostName = propFile.getPropertiesSingleStringValue("Unix_Host");
        final String userName = propFile.getPropertiesSingleStringValue("Unix_UserName");
        final String password = propFile.getPropertiesSingleStringValue("Unix_Password");
        final String workingDirectory = propFile.getPropertiesSingleStringValue("Unix_Directory");
		AutomationLogger.info("\nFTPing the HTML report and log file to '" +hostName+ "' Central Unix shared machine to /home/eniqftauto/html/kpithreshold/results directory----\n");
        for (int i = 0; i < fileList.length; i++) {
            if ((fileList[i].contains(".htm") || fileList[i].contains(".log.0")) && !fileList[i].contains(".log.0.lck")) {
                uploadFile(outputFileDir, fileList[i], hostName, userName, password, workingDirectory);
            }
        }

    }
    
	public String getHost(){
		String UnixHostName = hostName; 
		return UnixHostName ;
	}
	
    private void uploadFile(final String outputFileDir, final String fileName, final String hostName,
            final String userName, final String password, final String workingDirectory) throws FileNotFoundException,
            IOException {

        InputStream in = null;
        try {
            final FTPClient ftp = login(hostName, userName, password);
            if (ftp == null) {
                return;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory(workingDirectory);
            int reply = ftp.getReplyCode();
            if (FTPReply.isNegativePermanent(reply)) {
            	AutomationLogger.info("Directory on Upload Server does not exist" + workingDirectory);
            	AutomationLogger.info("Creating Directory on Upload Server" + workingDirectory);
                System.out.println("Directory on Upload Server does not exist" + workingDirectory);
                System.out.println("Creating Directory on Upload Server" + workingDirectory);
                if (makeDirectory(hostName, userName, password, workingDirectory)
                        && !ftp.changeWorkingDirectory(workingDirectory)) {
                	AutomationLogger.info(fileName + " could not be transferred.");
                	AutomationLogger.info("FTP Reply String." + ftp.getReplyString());
                    System.out.println(fileName + " could not be transferred.");
                    System.out.println("FTP Reply String." + ftp.getReplyString());
                }
            }

            final File fileForUpload = new File(outputFileDir + fileName);
            AutomationLogger.info("\nFile for Upload: " + fileForUpload.getPath());
            System.out.println("\nFile for Upload: " + fileForUpload.getPath());
            //endTime = new Date();
            in = new FileInputStream(fileForUpload);

            ftp.storeFile(fileName, in);
            reply = ftp.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
            	AutomationLogger.info("Connection to " + hostName + " successful. " + fileName
                        + " successfully transferred.");
                System.out.println("Connection to " + hostName + " successful. " + fileName
                        + " successfully transferred.");
            }

            if (FTPReply.isNegativePermanent(reply)) {
            	AutomationLogger.info("Connection to " + hostName + " failed! " + fileName + " could not be transferred.");
            	AutomationLogger.info("FTP Reply String." + ftp.getReplyString());
                System.out.println("Connection to " + hostName + " failed! " + fileName + " could not be transferred.");
                System.out.println("FTP Reply String." + ftp.getReplyString());
            }

            ftp.logout();
            ftp.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
            String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
        }
    }

    public boolean makeDirectory(final String hostName, final String userName, final String password,
            final String directoryPathName) throws FileNotFoundException, IOException {

        try {
            final FTPClient ftp = login(hostName, userName, password);
            if (ftp == null) {
                return false;
            }
            final boolean directoryExists = ftp.changeWorkingDirectory(directoryPathName);
            if (directoryExists) {
            	AutomationLogger.info();

                System.out.println("Directory " + directoryPathName + " already exist on " + hostName);
                return true;
            }

            ftp.makeDirectory(directoryPathName);
            final int reply = ftp.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
            	AutomationLogger.info("Directory " + directoryPathName + " created on " + hostName);

                System.out.println("Directory " + directoryPathName + " created on " + hostName);
                return true;
            } else if (FTPReply.isNegativePermanent(reply)) {
            	AutomationLogger.info("Directory creation " + directoryPathName + " on " + hostName + " failed! ");

            	AutomationLogger.info("FTP Reply String." + ftp.getReplyString());

                System.out.println("Directory creation " + directoryPathName + " on " + hostName + " failed! ");
                System.out.println("FTP Reply String." + ftp.getReplyString());
                return false;
            }
            ftp.logout();
            ftp.disconnect();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
            return false;
        }
    }

    public FTPClient login(final String hostName, final String userName, final String password)
            throws FileNotFoundException, IOException {
        FTPClient ftp = null;

        ftp = new FTPClient();
        ftp.connect(hostName);
        ftp.login(userName, password);
        final int reply = ftp.getReplyCode();
        if (FTPReply.isNegativePermanent(reply)) {
        	AutomationLogger.info("Connection to " + hostName + " failed! ");

            System.out.println("Connection to " + hostName + " failed! ");
            return null;
        }
        return ftp;
    }

    public void copyFilesToArchiveDirectory(final String outputFileDir, final String topologyLogFileDir,
            final String archiveSubDirectory) throws IOException {
        final File parentDirectory = new File(outputFileDir);

        final String[] htmlFileList = parentDirectory.list();

        for (int i = 0; i < htmlFileList.length; i++) {
            if (htmlFileList[i].contains(".htm") || htmlFileList[i].contains(".log.0")) {

                final String orig = outputFileDir + htmlFileList[i];
                final String dest = archiveSubDirectory + htmlFileList[i];
            	AutomationLogger.info("(Archive) File In: " + orig);
            	AutomationLogger.info("(Archive) File Out: " + dest);

                System.out.println("(Archive) File In: " + orig);
                System.out.println("(Archive) File Out: " + dest);

                final FileChannel in = new FileInputStream(orig).getChannel();
                final FileChannel out = new FileOutputStream(dest).getChannel();

                in.transferTo(0, in.size(), out);
                in.close();
                out.close();
                (new File(orig)).delete();
            }
        }
        AutomationLogger.info("Result files archived to: " + archiveSubDirectory);
        System.out.println("Result files archived to: " + archiveSubDirectory);
    }
    
    
}