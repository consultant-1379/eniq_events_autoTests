package com.ericsson.eniq.events.ltees.controllers.resultwriters;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ericsson.eniq.events.ltees.controllers.file.PropertiesFileController;

/*This class is used to copy files inside 
 local server if can not access to the remote server*/

public class MoveUtil {
    
	
	private final LogFileWriter logWriter = new LogFileWriter();
	private String  result;
	
	public String transferFileFromLocal(String sourceUnixFilePath,
			String targetLocalFilePath) throws FileNotFoundException,
			IOException {
		// TODO copy the files from automation package to /eniq/northbound
		File oldFile = new File(sourceUnixFilePath);
		InputStream inStream = null;
		FileOutputStream fout = null;
		final PropertiesFileController propFile = new PropertiesFileController();
		final String topologyLogFile = propFile
				.getAppPropSingleStringValue("TopologyLogFile");

		System.out.println("[Environment Setup] Fetching files from"
				+ sourceUnixFilePath + "\n");
		try {

			int bytesum = 0;
			int byteread = 0;
			if (oldFile.exists()) {
				inStream = new FileInputStream(oldFile);
				fout = new FileOutputStream(targetLocalFilePath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fout.write(buffer, 0, byteread);

				}
				System.out.println(bytesum + " bytes has been copied");
				logWriter.writeToLogFile(topologyLogFile, bytesum
						+ " bytes has been copied from" +sourceUnixFilePath);
				fout.flush();
				result = "Copy file from local successfully";
			} else {
				throw new Exception();
			}
			
		} catch (Exception e) {
			
			System.out.println("[Environment Setup] Copying file from local error...");
			logWriter.writeToLogFile(topologyLogFile,
					"[Environment Setup] Copying file from local error...");
			//e.printStackTrace();
			
			result="Copy fails..."+e.toString();
			
		} finally {
			try {

				if (inStream != null) {

					inStream.close();
				}
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}

		}
		return result;
	}

}
