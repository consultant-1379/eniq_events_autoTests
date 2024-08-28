package com.ericsson.eniq.events.ltees.controllers.resultwriters;

import java.io.*;
import java.nio.channels.*;
import org.apache.commons.net.ftp.*;
import com.ericsson.eniq.events.ltees.controllers.file.*;

public class FTPUtil {

	private final LogFileWriter logWriter = new LogFileWriter();
	final PropertiesFileController propFile = new PropertiesFileController();

	public boolean makeDirectoryOnMZServer(String directoryPathName) throws FileNotFoundException, IOException{
		System.out.println("Making Directory on MZ server:"+directoryPathName);
		final String userName = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
		final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
		String hostName = (new HTMLResultFileWriter()).getHostname("ec_ltees_1") + propFile.getAppPropSingleStringValue("MZ_Server_Host");
		return makeDirectory(hostName, userName, password, directoryPathName);
	}
	public boolean makeDirectoryRecursiveOnMZServer(String directoryPathName) throws FileNotFoundException, IOException{
		System.out.println("Making Directory Recursively on MZ server:"+directoryPathName);
		final String userName = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
		final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
		String hostName = (new HTMLResultFileWriter()).getHostname("ec_ltees_1") + propFile.getAppPropSingleStringValue("MZ_Server_Host");
		return makeDirectoryRecursive(hostName, userName, password, directoryPathName);
		
	}
	public boolean makeDirectoryRecursive(String hostName, String userName, String password,String directoryPathName) throws FileNotFoundException, IOException{
		String fileSeprator = File.separator;
		String directories [] = directoryPathName.split(fileSeprator);
		String path = "";
		for(String directory : directories){
			path =  path + fileSeprator +directory;
			if(!makeDirectory(hostName, userName, password, path)){
				return false;
			}
		}
		return true;
	}
	

	public void copyFileToMZServer(String absoluteFilePath, boolean overwrite) throws FileNotFoundException, IOException{
		
		
		final String userName = propFile.getAppPropSingleStringValue("MZ_Server_UserName");
		final String password = propFile.getAppPropSingleStringValue("MZ_Server_Password");
		String hostName = (new HTMLResultFileWriter()).getHostname("ec_ltees_1") + propFile.getAppPropSingleStringValue("MZ_Server_Host");

		copyFile(absoluteFilePath, overwrite, userName, password, hostName);
	}
	public void copyFile(String absoluteFilePath, boolean overwrite, String userName, String password, String hostName) throws FileNotFoundException, IOException{
		System.out.println("Copying file "+absoluteFilePath+" to "+hostName);
		final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
		
		
		FileController fileController = new FileController();
		String fileParent = fileController.getParentDir(absoluteFilePath);
		uploadFile(fileParent, fileController.getFileName(absoluteFilePath), fileParent, overwrite, userName, password, hostName, topologyLogFile);
	}

	public void copyHtmlAndLogFilesToUnixBox(final String outputFileDir, final String topologyLogFileDir) throws FileNotFoundException, IOException {

		final File parentDirectory = new File(outputFileDir);
		final String[] fileList = parentDirectory.list();

		final String hostName = propFile.getAppPropSingleStringValue("Unix_Host");
		final String userName = propFile.getAppPropSingleStringValue("Unix_UserName");
		final String password = propFile.getAppPropSingleStringValue("Unix_Password");
		final String workingDirectory = propFile.getAppPropSingleStringValue("Unix_Directory");


		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].contains(".html") || fileList[i].contains(".log")) {
				uploadFile(outputFileDir, fileList[i], workingDirectory, false, userName, password, hostName, topologyLogFileDir);
			}
		}

	}

	private void uploadFile(final String sourceDir, final String sourcefileName, String targetDirectory, boolean overwrite, String userName, String password, String hostName, final String topologyLogFileDir) throws FileNotFoundException, IOException {

		InputStream in = null;
		try {
			FTPClient ftp = login(hostName, userName, password);
			if (ftp == null){
				return;
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(targetDirectory);
		    int reply = ftp.getReplyCode();
			if (FTPReply.isNegativePermanent(reply)) {
				logWriter.writeToLogFile(topologyLogFileDir, "Directory on Upload Server does not exist"+targetDirectory);
				logWriter.writeToLogFile(topologyLogFileDir, "Creating Directory on Upload Server"+targetDirectory);
				System.out.println("Directory on Upload Server does not exist"+targetDirectory);
				System.out.println("Creating Directory on Upload Server"+targetDirectory);
				if(makeDirectoryRecursive(hostName, userName, password,targetDirectory) && !ftp.changeWorkingDirectory(targetDirectory)){
					logWriter.writeToLogFile(topologyLogFileDir, sourcefileName + " could not be transferred.");
					logWriter.writeToLogFile(topologyLogFileDir, "FTP Reply String."+ftp.getReplyString());
				}
			}
			final File fileForUpload = new File(sourceDir + sourcefileName);
			if(!fileForUpload.exists()){
				logWriter.writeToLogFile(topologyLogFileDir, "File "+fileForUpload.getPath() +" does not exists locally for uploading to "+hostName);
				System.out.println("File "+fileForUpload.getPath() +" does not existslocally for uploading to "+hostName);
				return;
			}
			System.out.println("File for Upload: " + fileForUpload.getPath());
			
			if(overwrite){
				ftp.deleteFile(sourcefileName);
			}else{
				FTPFile [] ftpFileList = ftp.listFiles();
				for(FTPFile ftpFileListItem : ftpFileList){
					if(sourcefileName.equals(ftpFileListItem.getName())){
						return;
					}
				}
			}
			
			
			in = new FileInputStream(fileForUpload);

			ftp.storeFile(sourcefileName, in);
			reply = ftp.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				logWriter.writeToLogFile(topologyLogFileDir, "Connection to " + hostName + " successful. " + sourcefileName  + " successfully transferred.");
				System.out.println("Connection to " + hostName + " successful. " + sourcefileName + " successfully transferred.");
			}

			if (FTPReply.isNegativePermanent(reply)) {
				logWriter.writeToLogFile(topologyLogFileDir, "Connection to " + hostName + " failed! " + sourcefileName+ " could not be transferred.");
				System.out.println("Connection to " + hostName + " failed! " + sourcefileName + " could not be transferred.");
				logWriter.writeToLogFile(topologyLogFileDir, "FTP Reply String."+ftp.getReplyString());
				System.out.println("FTP Reply String."+ftp.getReplyString());
			}

			ftp.logout();
			ftp.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}


	public void downloadFile(String hostName, String userName, String password, String sourceUnixFilePath, String targetLocalFilePath) throws FileNotFoundException, IOException {
	
		OutputStream out = null;
		MoveUtil moveUtil = new MoveUtil();
		
		if(!moveUtil.transferFileFromLocal(sourceUnixFilePath, targetLocalFilePath).equals("Copy file from local successfully")){

			try {
				System.out.println("FTP TO REMOTE SERVER");
				FTPClient ftp = login(hostName, userName, password);
				
			if (ftp == null){
				System.out.println("File transfer from " + hostName + " failed! \nNow try to fetch files from Local");
				//If can not access to remote server, tyr to copy files from local server instead
				
				return;
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			File parentDirectory = new File(new FileController().getParentDir(targetLocalFilePath));
			if(!parentDirectory.exists()){
				parentDirectory.mkdirs();
			}
			out = new FileOutputStream(targetLocalFilePath);

			ftp.retrieveFile(sourceUnixFilePath, out);
			final int reply = ftp.getReplyCode();
			final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
			logWriter.writeToLogFile(topologyLogFile, "Received Reply from FTP Connection:" + reply);
			

			if (FTPReply.isPositiveCompletion(reply)) {
				logWriter.writeToLogFile(topologyLogFile, "File transfer from " + hostName + " successful. " + targetLocalFilePath
						+ " successfully downloaded.");
				System.out.println("File " + targetLocalFilePath + " successfully downloaded.");
			}

			if (FTPReply.isNegativePermanent(reply)) {
				logWriter.writeToLogFile(topologyLogFile, "File transfer from " + hostName + " failed! " + sourceUnixFilePath
						+ " could not be transferred.");
				logWriter.writeToLogFile(topologyLogFile, "FTP Reply String."+ftp.getReplyString());
				System.out.println("FTP Reply String."+ftp.getReplyString());
				logWriter.writeToLogFile(topologyLogFile, "Now try to fetch files from Local package");
			}

			ftp.logout();
			ftp.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
			//return "Download fails: "+e.toString() ;
		}}
		//return "Download file sucessfully";
	}
	public boolean makeDirectory(String hostName, String userName, String password,String directoryPathName) throws FileNotFoundException, IOException{
		
		final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
		try {
			FTPClient ftp = login(hostName, userName, password);
			if (ftp == null){
				return false;
			}
			boolean directoryExists = ftp.changeWorkingDirectory(directoryPathName);
			if(directoryExists){
				logWriter.writeToLogFile(topologyLogFile, "Directory "+directoryPathName+" already exist on " + hostName);
				System.out.println("Directory "+directoryPathName+" already exist on " + hostName);
				return true;
			}

			ftp.makeDirectory(directoryPathName);
			int reply = ftp.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				logWriter.writeToLogFile(topologyLogFile, "Directory "+directoryPathName+" created on " + hostName);
				System.out.println("Directory "+directoryPathName+" created on " + hostName );
				return true;
			}else if (FTPReply.isNegativePermanent(reply)) {
				logWriter.writeToLogFile(topologyLogFile, "Directory creation "+directoryPathName+" on " + hostName +  " failed! ");
				System.out.println("Directory creation "+directoryPathName+" on " + hostName +  " failed! ");
				logWriter.writeToLogFile(topologyLogFile, "FTP Reply String."+ftp.getReplyString());
				System.out.println("FTP Reply String."+ftp.getReplyString());
				return false;
			}

			ftp.logout();
			ftp.disconnect();
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public FTPClient login(String hostName, String userName, String password) throws FileNotFoundException, IOException{
		FTPClient ftp = null;
		final String topologyLogFile = propFile.getAppPropSingleStringValue("TopologyLogFile");
		
			ftp = new FTPClient();
			ftp.connect(hostName);
			ftp.login(userName, password);
			int reply = ftp.getReplyCode();
			if (FTPReply.isNegativePermanent(reply)) {
				logWriter.writeToLogFile(topologyLogFile, "Connection to " + hostName + " failed! ");
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
			if (htmlFileList[i].contains(".html") || htmlFileList[i].contains(".log")) {

				final String orig = outputFileDir + htmlFileList[i];
				final String dest = archiveSubDirectory + htmlFileList[i];

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
		System.out.println("Result files archived to: " + archiveSubDirectory);
		logWriter.writeToLogFile(topologyLogFileDir, "Result files archived to: " + archiveSubDirectory);
	}

	public void moveInputFilesToArchive(String inputFileDir, String archiveSubDirectoryExecutionContext, String testTimestampStart) {
		File originalFile = new File(inputFileDir);
		File backupFile = new File(inputFileDir+"_backup");
		File destinationFile = new File(archiveSubDirectoryExecutionContext);
		originalFile.renameTo(destinationFile);
		backupFile.renameTo(new File(inputFileDir));

	}
}
