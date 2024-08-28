package com.ericsson.eniq.events.ui.selenium.tests.aac.data;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FTPUpload {

    //private final LogFileWriter logWriter = new LogFileWriter();

    // #****************************************************************************************
    // #UNIX Connection - [For Server Only]
    // #****************************************************************************************
    // #<---Transfer HTML Result Files to Unix Machine (using FTP)--->
    // Unix_Host=eieatx006
    // Unix_UserName=eniqftauto
    // Unix_Password=Nulyphjn8
    // Unix_Directory=/home/eniqftauto/html/automation
    // Unix_URL=http://users.eei.ericsson.se/~eniqftauto/ltees/results/

    public static void copyAllFilesToUnixBox(final String sourceFileAbsolutePathName, final String targetFileName) {
        final String hostName = PropertyReader.getInstance().getUnixHost();
        final String userName = PropertyReader.getInstance().getUnixUserName();
        final String password = PropertyReader.getInstance().getUnixPassword();
        final String workingDirectory = PropertyReader.getInstance().getUnixDirectory();

        FTPClient ftp = null;
        InputStream in = null;
        try {
            ftp = new FTPClient();
            ftp.connect(hostName);
            ftp.login(userName, password);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory(workingDirectory);

            final File fileForUpload = new File(sourceFileAbsolutePathName);
            System.out.println("File for Upload: " + fileForUpload.getPath());
            in = new FileInputStream(fileForUpload);

            ftp.storeFile(targetFileName, in);
            final int reply = ftp.getReplyCode();

            System.out.println("Received Reply from FTP Connection:" + reply);

            if (FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Connection to " + hostName + " successful. " + targetFileName
                        + " successfully transferred.");
            }

            if (FTPReply.isNegativePermanent(reply)) {
                System.out.println("Connection to " + hostName + " failed! " + targetFileName
                        + " could not be transferred.");
            }
            if (sourceFileAbsolutePathName.toUpperCase().endsWith("HTML")) {
                ftp.storeFile("Summary.html", new FileInputStream(new File(sourceFileAbsolutePathName)));
            } else {
                ftp.storeFile("Summary.log", new FileInputStream(new File(sourceFileAbsolutePathName)));
            }

            ftp.logout();
            ftp.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
