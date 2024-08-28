package com.ericsson.eniq.events.ltees.environmentsetup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;
import com.ericsson.eniq.events.ltees.controllers.resultwriters.*;

/**
 * @author ekeviry
 */
public class CheckFilesAndDirectories {

	final static PropertiesFileController propFile = new PropertiesFileController();
	final static LogFileWriter logWriter = new LogFileWriter();
	FTPUtil ftpUtil = new FTPUtil();
	
	MoveUtil moveUtil = new MoveUtil();

	/**
	 * @author ekeviry
	 * @throws IOException
	 * @since 2011
	 */
	public void checkIfDirectoryExists(final String directoryName) throws IOException {
		final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

		final File dir = new File(directoryName);

		if (dir.exists() == true) {
			System.out.println("[Environment Setup] Directory already exists: " + dir.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,"[Environment Setup] Directory already exists: " + dir.getPath());
		}

		else {
			System.out.println("[Environment Setup] Directory does not exist: " + dir.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,"[Environment Setup] Directory does not exist: " + dir.getPath());

			dir.mkdirs();
			System.out.println("Directory Created...\n");
			logWriter.writeToLogFile(topologyLogFileDir,"Directory Created...\n");
		}
	}

	/**
	 * @author ekeviry
	 * @throws IOException
	 * @since 2011
	 */
	public void checkIfFileExists(final String fileNameString) throws IOException {
		final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");

		final File fileName = new File(fileNameString);

		if (fileName.exists() == true) {
			System.out.println("[Environment Setup] File already exists: " + fileName.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,
					"[Environment Setup] File already exists: " + fileName.getPath());
		}

		else {
			System.out.println("[Environment Setup] File does not exist: " + fileName.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,"[Environment Setup] File does not exist: " + fileName.getPath());
			fileName.createNewFile();
			System.out.println("File Created...\n");
			logWriter.writeToLogFile(topologyLogFileDir,"File Created...\n");
		}
	}

	/**
	 * @author ekeviry
	 * @param overwrite TODO
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @since 2011
	 */
	public void fetchNonExistingFilesFromUnixHost(final String fileNameString,
			final String unixHostSourcePath, boolean overwrite) throws FileNotFoundException, IOException {

		final String topologyLogFileDir = propFile.getAppPropSingleStringValue("TopologyLogFile");
		
		
	    final  String hostName = propFile.getAppPropSingleStringValue("Unix_Host");
	    final  String userName = propFile.getAppPropSingleStringValue("Unix_UserName");
	    final  String password = propFile.getAppPropSingleStringValue("Unix_Password");

		final File fileName = new File(fileNameString);

		if (overwrite == false && fileName.exists() == true) {
			System.out.println("[Environment Setup] File already exists: " + fileName.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,"[Environment Setup] File already exists: " + fileName.getPath());
		}

		else {
			System.out.println("[Environment Setup] Fetching File: " + fileName.getPath());
			logWriter.writeToLogFile(topologyLogFileDir,"[Environment Setup] Fetching File: " + fileName.getPath());
			ftpUtil.downloadFile(hostName, userName, password,unixHostSourcePath,fileNameString);
			
			/*final URL unixHost = new URL(unixHostURL);
			final ReadableByteChannel rbc = Channels.newChannel(unixHost.openStream());
			final FileOutputStream fos = new FileOutputStream(fileName.getPath());
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);*/
			
		}

	}
}
