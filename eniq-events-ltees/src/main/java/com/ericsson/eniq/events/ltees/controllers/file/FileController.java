/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ltees.controllers.file;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.ericsson.eniq.events.ltees.controllers.common.*;

/**
 * @author ESAIMKH/EKEVIRY
 * @since 2011
 * 
 */
public class FileController {

  private final String counterPropKeyName = "CounterPropFile_Directory";

  private final CommonController common = new CommonController();

  private final XmlController xml = new XmlController();

  private final PropertiesFileController propFile = new PropertiesFileController();

  private final String[] runOSSRCNames = { "RunApp_OSSRC1", "RunApp_OSSRC2", "RunApp_OSSRC3", "RunApp_OSSRC4" };

  private final String[] ossRcFolderRange = { "OSSRC1_DirNum", "OSSRC2_DirNum", "OSSRC3_DirNum", "OSSRC4_DirNum" };

  private final String[] oss = { "OSS 1", "OSS 2", "OSS 3", "OSS 4" };

  /**
   * @see app.properties file
   * @return String - Forward or back slash for INPUT files/directory. Different
   *         for local or server files
   * @renamed - splitForwardBackSlashInput() separator
   */
  public String getInputDirectorySeparator() throws FileNotFoundException, IOException {
    final String runLocal = propFile.getAppPropSingleStringValue("runLocal");
    String dirSeperator = null;
    if (runLocal.equalsIgnoreCase("y") || runLocal.equalsIgnoreCase("yes")) {
      dirSeperator = "\\\\";
    } else {
      dirSeperator = "/";
    }

    return dirSeperator;
  }

  public String getParentDir(String absoluteFilePath){
	  return absoluteFilePath.substring(0, absoluteFilePath.lastIndexOf(File.separator)+1);
  }
  public String getFileName(String absoluteFilePath){
	  return absoluteFilePath.substring(absoluteFilePath.lastIndexOf(File.separator)+1);
  }
  /**
   * @see app.properties file
   * @return String - Forward or back slash for OUTPUT files/directory.
   *         Different for local or server files
   * @renamed - splitForwardBackSlashOutput()
   */
  public String getOutputDirectorySeparator() throws FileNotFoundException, IOException {
    final String runLocal = propFile.getAppPropSingleStringValue("runLocal");
    String dirSeperator = null;
    if (runLocal.equalsIgnoreCase("y") || runLocal.equalsIgnoreCase("yes")) {
      dirSeperator = "\\";
    } else {
      dirSeperator = "/";
    }
    return dirSeperator;
  }

  /**
   * @return ArrayList String<>: Get all Files from app.properties
   *         "InputFolder_Directory"
   * @param fullFileName
   *          (Boolean): Get files with full Directory & FileName - i.e.
   *          C:\Automation\files\events_oss_1\dir38\filename.xml.gz
   * @param fileNameWithOssExtOnly
   *          (Boolean): Get files with OSS Directory & FileName - i.e.
   *          events_oss_1\dir38\filename.xml.gz
   * @param fileNameWithFileDirExtOnly
   *          (Boolean): Get files with File Folder & FileName - i.e.
   *          dir38\filename.xml.gz
   * @param fileNameOnly
   *          (Boolean): Get files with File Folder & FileName - i.e.
   *          filename.xml.gz
   * @Note Select for no more than 1 of the above or else if there are more than
   *       1 "true" then full Directory & filename will be provided (same as if
   *       fullFileName is set to true)
   */
  public ArrayList<String> getAllFiles(final boolean fullFileName, final boolean fileNameWithOssExtOnly,
      final boolean fileNameWithFileDirExtOnly, final boolean fileNameOnly) throws FileNotFoundException, IOException {
    final ArrayList<String> latestFiles = new ArrayList<String>();
    final ArrayList<String> allFileNames = getAllFilesFromInputFolderToStringArrayList();

    for (int i = 0; i < allFileNames.size(); i++) {
      final String fileNameFull = allFileNames.get(i);

      // Get Full File Names - i.e. "C:\LTEES Test
      // Automation\lte_event_stat_file\events_oss_1\dir38\filename.xml.gz"
      if (fullFileName && !fileNameWithOssExtOnly && !fileNameWithFileDirExtOnly && !fileNameOnly) {
        latestFiles.add(fileNameFull);
      }

      // Get File Names with OSS Dir Only - i.e.
      // "events_oss_1\dir38\filename.xml.gz"
      else if (!fullFileName && fileNameWithOssExtOnly && !fileNameWithFileDirExtOnly && !fileNameOnly) {
        final String fileName = getFileNameWithDirExt(fileNameFull, fileNameWithOssExtOnly, false, false);
        latestFiles.add(fileName);

        // Get File Names Folder Directory Only - i.e. "dir38\filename.xml.gz"
      } else if (!fullFileName && !fileNameWithOssExtOnly && fileNameWithFileDirExtOnly && !fileNameOnly) {
        final String fileName = getFileNameWithDirExt(fileNameFull, false, fileNameWithFileDirExtOnly, false);
        latestFiles.add(fileName);
      }

      // Get File Names Only - i.e. "filename.xml.gz"
      else if (!fullFileName && !fileNameWithOssExtOnly && !fileNameWithFileDirExtOnly && fileNameOnly) {
        final String fileName = getFileNameWithDirExt(fileNameFull, false, false, fileNameOnly);
        latestFiles.add(fileName);
      }

      // Get Full File Names
      else {
        latestFiles.add(fileNameFull);
      }
    }
    return latestFiles;
  }

  /**
   * @return ArrayList String<>: Get all Latest Date Files from app.properties
   *         "InputFolder_Directory"
   * @param fullFileName
   *          (Boolean): Get files with full Directory & FileName - i.e.
   *          C:\Automation\files\events_oss_1\dir38\filename.xml.gz
   * @param fileNameWithOssExtOnly
   *          (Boolean): Get files with OSS Directory & FileName - i.e.
   *          events_oss_1\dir38\filename.xml.gz
   * @param fileNameWithFileDirExtOnly
   *          (Boolean): Get files with File Folder & FileName - i.e.
   *          dir38\filename.xml.gz
   * @param fileNameOnly
   *          (Boolean): Get files with File Folder & FileName - i.e.
   *          filename.xml.gz
   * @Note Select for no more than 1 of the above or else if there are more than
   *       1 "true" then full Directory & filename will be provided (same as if
   *       fullFileName is set to true)
   */
  public ArrayList<String> getAllLatestDateFiles(final boolean fullFileName, final boolean fileNameWithOssExtOnly,
      final boolean fileNameWithFileDirExtOnly, final boolean fileNameOnly) throws FileNotFoundException, IOException {
    final ArrayList<String> latestFiles = new ArrayList<String>();
    final ArrayList<String> allFileNames = getAllFiles(fullFileName, fileNameWithOssExtOnly,
        fileNameWithFileDirExtOnly, fileNameOnly);
    final int date = getLatestFileDate();

    for (int i = 0; i < allFileNames.size(); i++) {
      final String fileName = allFileNames.get(i);
      final boolean containsDate = fileName.indexOf((date + "")) > 0;

      if (containsDate) {
        latestFiles.add(fileName);
      }
    }
    return latestFiles;
  }

  /**
   * @return String: get UnZipped file without deleting compressed file
   * @throws IOException
   */
  public String gunzipFileIfGzFileFormatWithoutDeletingOriginalFile(final String fileName) throws IOException {
    if (fileName.endsWith(".gz")) {
      final File fileGz = new File(fileName);
      final File unzipedFile = unGzip(fileGz, false);
      final String file = unzipedFile.toString();
      return file;
    }
    return fileName;
  }

  /**
   * @return String - Get the OSS Directory Folder name from the file name
   *         [fileName]
   * @param fileName
   *          (String): XML File Name
   * @rename - fileNameOssExtension();
   */
  public String getOssDirNameFromFileName(final String fileName) throws FileNotFoundException, IOException {
    final String splitType = getInputDirectorySeparator();
    final String[] file = fileName.split(splitType);
    String ossExt;

    if (file.length != 0 && file.length >= 3) {
      final String ossDir = file[file.length - 3];
      ossExt = ossDir;
    } else {
      ossExt = "Not Available";
    }
    return ossExt;
  }

  /**
   * @return String - Get the file directory name from the file name [fileName]
   * @param fileName
   *          (String): XML File Name
   * @Renamed - fileNameDirectoryFolder
   */
  public String getFileFolderNameFromFileName(final String fileName) throws FileNotFoundException, IOException {
    final String splitType = getInputDirectorySeparator();
    final String[] file = fileName.split(splitType);
    final String dir;

    if (file.length != 0 && file.length >= 2) {
      final String folderDir = file[file.length - 2];
      dir = folderDir;
    } else {
      dir = "Not Available";
    }
    return dir;
  }

  /**
   * @return String - Get XML File name with or without extensions
   * @param fileName
   *          (String): XML File Name
   * @param fileNameWithOssExt
   *          (Boolean): File Name with OSS Directory Extension
   * @param fileNameWithFileDirExt
   *          (Boolean): File Name with File Directory Extension
   * 
   * @renamed - fileNameWithOssAndDirExtension()&&fileNameWithoutDirExtensions()
   */
  public String getFileNameWithDirExt(final String fullFileName, final boolean fileNameWithOssExt,
      final boolean fileNameWithFileDirExt, final boolean fileNameOnly) throws FileNotFoundException, IOException {
    final String splitType = getInputDirectorySeparator();
    final String splitTypeOut = getOutputDirectorySeparator();
    String fileName = null;

    final String[] file = fullFileName.split(splitType);
    final String fName = file[file.length - 1];

    if (file.length > 3 && fileNameWithOssExt && !fileNameWithFileDirExt && !fileNameOnly) {
      fileName = file[file.length - 3] + splitTypeOut + file[file.length - 2] + splitTypeOut + fName;
    } else if (file.length > 2 && !fileNameWithOssExt && fileNameWithFileDirExt && !fileNameOnly) {
      fileName = file[file.length - 2] + splitTypeOut + file[file.length - 1];
    } else if (!fileNameWithOssExt && !fileNameWithFileDirExt && fileNameOnly) {
      fileName = file[file.length - 1];
    } else {
      fileName = fullFileName;
    }
    return fileName;
  }

  /**
   * @return File ArrayList - All Files with criteria specified in
   *         app.properties (See RunApp_OSSRC(x) and OSSRC(x)_DirNum) *
   * @param parentDirectory
   *          (File) - File main.parent directory - i.e. C:\\Files
   * @param gunZipFiles
   *          (Boolean): True/False to unZip .GZ files
   * @param deleteZippedFilesAfterGunzip
   *          (Boolean): Delete .GZ Zipped/Compressed Files after UnZip
   */
  public ArrayList<File> getFilesFromOSSFolders(final File parentDirectory, final boolean gunZipFiles,
      final boolean deleteZippedFilesAfterGunzip) throws FileNotFoundException, IOException {
    final String[] subDirectoryList = parentDirectory.list();
    final ArrayList<File> allFiles = new ArrayList<File>();
   // System.out.println("ParentDirectory="+parentDirectory+"<--");
   // System.out.println("subDirectoryList.length="+subDirectoryList.length);
    // Get all files with criteria from app.properties file
    if (!(subDirectoryList == null)) {
      System.out.print("\n");// new line before printing folder ranges
      final ArrayList<File> files1 = getFilesFromOneFolder(parentDirectory);
      final ArrayList<File> files2 = getFilesFromFoldersRangingFromAtoB(parentDirectory);
      final ArrayList<File> files3 = getAllFilesFromFolder(parentDirectory);

      // Add all ArrayList to 1 ArrayList
      allFiles.addAll(files1);
      allFiles.addAll(files2);
      allFiles.addAll(files3);
    }

    final ArrayList<File> tmp = new ArrayList<File>(); // Temporary

    // UnZip file, add UnZipped file to ArrayList and delete compressed files
    // from folder if deleteZippedFilesAfterGunzip true
    for (int i = 0; i < allFiles.size(); i++) {
      final File fileName = allFiles.get(i);
      //System.out.println("FILE NAME="+ fileName.toString());
      final String ext = getFileExtensionName(fileName);

      if (gunZipFiles && ext.equalsIgnoreCase("gz")) {
        tmp.add(fileName);
        final File fileGunzip = unGzip(fileName, deleteZippedFilesAfterGunzip);

        if (!allFiles.contains(fileGunzip)) {
          allFiles.add(fileGunzip);
        }
      }
    }
    // Delete compressed files that have been UnZipped from files arrayList
    for (int i = 0; i < tmp.size(); i++) {
      final File fileName = tmp.get(i);
      if (deleteZippedFilesAfterGunzip && allFiles.contains(fileName)) {
        allFiles.remove(fileName);
      }
    }
    return allFiles;
  }

  /**
   * @return File ArrayList - All Files with specified file extension
   *         (fileExtension - i.e. 'XML') and with criteria specified in
   *         app.properties (See RunApp_OSSRC(x) and OSSRC(x)_DirNum)
   * @param filesWithExtension
   *          (String): File Extension name - i.e. "gz"
   * @param parentDirectory
   *          (File) - File main.parent directory - i.e. C:\\Files
   * @param gunZipFiles
   *          (Boolean): True/False to unZip .GZ files
   * @param deleteZippedFilesAfterGunzip
   *          (Boolean): Delete .GZ Zipped/Compressed Files after UnZip
   */
  public ArrayList<File> getFilesFromOSSFolders(final String filesWithExtension, final File parentDirectory,
      final boolean gunZipFiles, final boolean deleteZippedFilesAfterGunzip) throws FileNotFoundException, IOException {

    final ArrayList<File> allFiles = getFilesFromOSSFolders(parentDirectory, gunZipFiles, deleteZippedFilesAfterGunzip);
    final ArrayList<File> allFilesWithExt = new ArrayList<File>();

    for (int i = 0; i < allFiles.size(); i++) {
      final File fileName = allFiles.get(i);
      final String fileNameStr = fileName.toString();

      if (fileNameStr.endsWith(filesWithExtension)) {
        allFilesWithExt.add(fileName);
      }
    }
    return allFilesWithExt;
  }

  /**
   * @return ArrayList String<>: Get all Files from app.properties Input Folder
   *         Directory (InputFolder_Directory) to String ArrayList
   * @See App.properties - InputFolder_Directory key tag
   */
  public ArrayList<String> getAllFilesFromInputFolderToStringArrayList() throws FileNotFoundException, IOException {
    final ArrayList<String> getAllFilesStringArray = new ArrayList<String>();
    ArrayList<File> getAllFilesFileArray = new ArrayList<File>();
    getAllFilesStringArray.clear();
    getAllFilesFileArray.clear();

    getAllFilesFileArray = getAllFilesFromInputFolderToFileArrayList();

    for (int r = 0; r < getAllFilesFileArray.size(); r++) {
      final File fileNameAllFiles = getAllFilesFileArray.get(r);
      final String fileName = fileNameAllFiles.toString();
      getAllFilesStringArray.add(fileName);
    }

    return getAllFilesStringArray;
  }

  /**
   * @return ArrayList File<>: Get all Files from app.properties Input Folder
   *         Directory (InputFolder_Directory) to File ArrayList
   * @See App.properties - InputFolder_Directory key tag
   */
  public ArrayList<File> getAllFilesFromInputFolderToFileArrayList() throws FileNotFoundException, IOException {
    final String parentFileDir = propFile.getAppPropSingleStringValue("InputFolder_Directory");
    final ArrayList<File> getAllFilesFileArray = new ArrayList<File>();
    ArrayList<File> allDir = new ArrayList<File>();
    getAllFilesFileArray.clear();

    final File parentDirectory = new File(parentFileDir);
    allDir = getAllDirectories(parentDirectory);

    for (int i = 0; i < allDir.size(); i++) {
      final File file = allDir.get(i);
      final File ossFullDirPath = new File(file.toString());
      final File[] directory = ossFullDirPath.listFiles();

      for (final File pf : directory) {
        final File[] folderFiles = pf.listFiles();
        for (int f = 0; f < folderFiles.length; f++) {

          if (folderFiles[f].isFile()) {
            getAllFilesFileArray.add(folderFiles[f]);
          }
        }
      }
    }
    allDir.clear();
    return getAllFilesFileArray;
  }

  /**
   * @return ArrayList Files<>: Get all Directories from all OSS Folders
   *         (app.properties)
   * @param parentDirectory
   *          (File): Main/Parent Directory
   * 
   */
  public ArrayList<File> getAllDirectories(final File parentDirectory) throws FileNotFoundException, IOException {
    String[] subDirectoryList = parentDirectory.list();
    final ArrayList<File> directories = new ArrayList<File>();
    final String directorySplit = getOutputDirectorySeparator();

    if (subDirectoryList != null) {
      for (int i = 0; i < subDirectoryList.length; i++) {
        final File subDirectory = new File(parentDirectory + directorySplit + subDirectoryList[i]);
        // System.out.println("Sub Directory Path: " + subDirectory);
        directories.add(subDirectory);
        // getFilesFromEachSubDirectory(subDirectory);
      }
    }
    subDirectoryList = new String[0];
    // return allFilesAllOSS;
    return directories;
  }

  /**
   * @return ArrayList File<>: Get Files In Each Sub Directory
   * @param subDirectory
   *          (File): File Sub-directory i.e. C:\\LTEES Test
   *          Automation\\lte_event_stat_file\\events_oss_1
   */
  public ArrayList<File> getFilesFromEachSubDirectory(final String subDirectory) throws FileNotFoundException,
      IOException {
    final ArrayList<File> result = new ArrayList<File>();
    final File fileDir = new File(subDirectory);
    final String[] subDirectoryList = fileDir.list();
    final String directorySplit = getOutputDirectorySeparator();

    if (subDirectoryList != null) {
      for (int i = 0; i < subDirectoryList.length; i++) {
        final File subDirectoryList2 = new File(subDirectory + directorySplit + subDirectoryList[i]);

        final File[] files = subDirectoryList2.listFiles();
        for (int f = 0; f < files.length; f++) {
          if (files[f].isFile()) {
            result.add(files[f]);
          }
        }
      }
    }
    return result;
  }

  /**
   * @return File Array[] - Get Folder Contents (Files and Folders) and return
   *         to File Array []
   * @param folder
   *          - String Folder directory - i.e.
   *          "C:\\LTEES Test Automation\\lte_event_stat_file\\events_oss_1\\dir3"
   */
  public File[] getFilesFromFolder(final String folder) throws FileNotFoundException, IOException {
    final File fileFolder = new File(folder);
    final File[] files = fileFolder.listFiles();
    return files;
  }

  /**
   * @return Integer - Get Date from the most current/latest file
   */
  public int getLatestFileDate() throws FileNotFoundException, IOException {
    final ArrayList<String> allFileNames = getAllFiles(false, false, false, true);
    final ArrayList<Integer> tmpDateList = new ArrayList<Integer>();
    for (int t = 0; t < allFileNames.size(); t++) {
      final String fileName = allFileNames.get(t);
      final int date = getDateFromFileName(fileName);
      tmpDateList.add(date);
    }
    final int[] tmpDateNoDup = common.removeDuplicatesWithOrderToIntegerArray(tmpDateList, true);
    final int date = tmpDateNoDup[0];
    return date;
  }

  /**
   * @return Integer - Get Date from the File Name
   * @param
   */
  public int getDateFromFileName(final String fileName) {
    final String[] splitDot = fileName.split("[.]");
    final String fullString = splitDot[0];
    final int stringLen = fullString.length();
    final String subStringDate = fullString.substring(stringLen - 8, stringLen);
    final int date = Integer.parseInt(subStringDate);
    return date;
  }

  /**
   * @return ArrayList File<>: Files from folders ranging from A and B for
   *         specified OSS
   * @param parentDirectory
   *          (File): Main/Parent Directory that contains the OSS Folder
   *          Structure as specified in application i.e. lte_event_stat_file
   */
  public ArrayList<File> getFilesFromFoldersRangingFromAtoB(final File parentDirectory) throws FileNotFoundException,
      IOException {
    final String appProp = propFile.getAppPropDirectoryLocalOrServer();
    final ArrayList<File> allFiles = new ArrayList<File>();
    final String[] ossDirectoryNames = parentDirectory.list();
    final String directorySplit = getOutputDirectorySeparator();

    // Check to run each OSS-RC
    for (int r = 0; r < runOSSRCNames.length; r++) {
      final String runOSSRc = propFile.getAppPropSingleStringValue(runOSSRCNames[r]);

      // If runOSSRC IS Yes then check the folder range
      if (runOSSRc.equalsIgnoreCase("Y")) {
    	  //System.out.println("Y getFilesFromFoldersRangingFromAtoB");
        final String[] folderRange = propFile.getPropertiesMultipleStringValues(ossRcFolderRange[r], appProp);
       // System.out.println("folderRange.length="+folderRange.length);
        // Check folder range (Start and End Folder)
        if (folderRange.length == 2 && common.checkIfNumber(folderRange[0]) && common.checkIfNumber(folderRange[1])) {
          final int startNum = Integer.parseInt(folderRange[0]);
          final int endNum = Integer.parseInt(folderRange[1]);
          System.out.println("-Running application for " + oss[r] + ": Folders Numbers Ranging from " + startNum
              + " to " + endNum);

          // get all folders in Directory
          for (int t = 1; t < ossDirectoryNames.length + 1; t++) {
            final String ossDirectoryNamesStr = parentDirectory + directorySplit + ossDirectoryNames[r];
            final String dirNum = t + "";

            if (ossDirectoryNamesStr.endsWith(dirNum)) {
              final File subDirectory = new File(ossDirectoryNamesStr);
              final String[] subDirectoryList = subDirectory.list();
              //System.out.println("In getFilesFromFoldersRangingFromAtoB_subDirectory="+subDirectory);
              //System.out.println("In getFilesFromFoldersRangingFromAtoB_subDirectoryList="+subDirectoryList.length);
              for (int y = 0; y < subDirectoryList.length; y++) {
              }

              for (int x = startNum; x <= endNum; x++) {
                for (int i = 0; i < subDirectoryList.length; i++) {
                  final String folderNumDir = x + "";
                  // check if folder from folder range exits
                  if (subDirectoryList[i].endsWith("dir" + folderNumDir)) {
                    // if folder exists then add files to arrayList
                    final String fulDirName = ossDirectoryNamesStr + directorySplit + subDirectoryList[i];

                    final File ossFullDirPath = new File(fulDirName);
                    final File[] files = ossFullDirPath.listFiles();
                    for (final File pf : files) {
                      if (pf.isFile()) {
                        allFiles.add(pf);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return allFiles;
  }

  /**
   * @return ArrayList File<>: Files from one folder only for specified OSS
   * @param parentDirectory
   *          (File): Main/Parent Directory that contains the OSS Folder
   *          Structure as specified in application i.e. lte_event_stat_file
   */
  public ArrayList<File> getFilesFromOneFolder(final File parentDirectory) throws FileNotFoundException, IOException {
    final String appProp = propFile.getAppPropDirectoryLocalOrServer();
    final ArrayList<File> allFiles = new ArrayList<File>();
    final String[] ossDirectoryNames = parentDirectory.list();
    final String directorySplit = getOutputDirectorySeparator();

    // Check to run each OSS-RC
    for (int r = 0; r < runOSSRCNames.length; r++) {
      final String runOSSRc = propFile.getAppPropSingleStringValue(runOSSRCNames[r]);

      // If runOSSRC IS Yes then check the folder range
      if (runOSSRc.equalsIgnoreCase("Y")) {
    	 // System.out.println("runOSSRC IS Yes");
        final String[] folderRange = propFile.getPropertiesMultipleStringValues(ossRcFolderRange[r], appProp);
        if (folderRange.length == 1 && common.checkIfNumber(folderRange[0])) {
          System.out.println("-Running application for " + oss[r] + ": Folder Number " + folderRange[0]);

          for (int t = 1; t < ossDirectoryNames.length + 1; t++) {
            final String ossDirectoryNamesStr = parentDirectory + directorySplit + ossDirectoryNames[r];
            final String dirNum = t + "";

            if (ossDirectoryNamesStr.endsWith(dirNum)) {
              final File subDirectory = new File(ossDirectoryNamesStr);
              final String[] subDirectoryList = subDirectory.list();
              //System.out.println("In getFilesFromOneFolder_subDirectory="+subDirectory);
              //System.out.println("In getFilesFromOneFolder_subDirectoryList.length="+subDirectoryList.length);
              for (int i = 0; i < subDirectoryList.length; i++) {
                if (subDirectoryList[i].endsWith("dir" + folderRange[0])) {
                  final String fulDirName = ossDirectoryNamesStr + directorySplit + subDirectoryList[i];
                  // getFilesFromDirAndAddToArrayList(fulDirName);

                  final File ossFullDirPath = new File(fulDirName);
                  final File[] files = ossFullDirPath.listFiles();
                  for (final File pf : files) {
                    if (pf.isFile()) {
                      allFiles.add(pf);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return allFiles;
  }

  /**
   * @return ArrayList File<>: Files from ALL folders indicated by Asterix
   *         symbol (*) for specified OSS
   * @param parentDirectory
   *          (File): Main/Parent Directory that contains the OSS Folder
   *          Structure as specified in application i.e. lte_event_stat_file
   */
  public ArrayList<File> getAllFilesFromFolder(final File parentDirectory) throws FileNotFoundException, IOException {
    final String appProp = propFile.getAppPropDirectoryLocalOrServer();
    final ArrayList<File> allFiles = new ArrayList<File>();
    final String[] ossDirectoryNames = parentDirectory.list();
    final String directorySplit = getOutputDirectorySeparator();

    // Check to run each OSS-RC
    for (int r = 0; r < runOSSRCNames.length; r++) {
      final String runOSSRc = propFile.getAppPropSingleStringValue(runOSSRCNames[r]);

      // If runOSSRC IS Yes then check the folder range
      if (runOSSRc.equalsIgnoreCase("Y")) {
    	 // System.out.println("Y getAllFilesFromFolder");
        final String[] folderRange = propFile.getPropertiesMultipleStringValues(ossRcFolderRange[r], appProp);
        if (folderRange.length == 1 && folderRange[0].equals("*")) {
          System.out.println("-Running application for " + oss[r] + ": All Folders");
         // System.out.println("ossDirectoryNames.length="+ossDirectoryNames.length);
          for (int t = 1; t < ossDirectoryNames.length + 1; t++) {
            final String ossDirectoryNamesStr = parentDirectory + directorySplit + ossDirectoryNames[r];
           // System.out.println("ossDirectoryNamesStr="+ossDirectoryNamesStr + "<--");
            final String dirNum = t + "";
            
            //System.out.println("Sub directory list has size " + subDirectoryList.length);         
           if (ossDirectoryNamesStr.endsWith(dirNum)) {
        	   String[] subDirectoryList = null;
               File subDirectory = new File(ossDirectoryNamesStr);
               subDirectoryList = subDirectory.list();	
             /* if (dirNum.equals("1")){
            	  subDirectoryList = new String[]{"dir1"};  
              } else {
            	   subDirectory = new File(ossDirectoryNamesStr);
                  subDirectoryList = subDirectory.list();
              }
              */
             // System.out.println("Sub directory list has size " + subDirectoryList.length);
              for (int i = 0; i < subDirectoryList.length; i++) {
                final String fulDirName = ossDirectoryNamesStr + directorySplit + subDirectoryList[i];
                // getFilesFromDirAndAddToArrayList(fulDirName);
                final File ossFullDirPath = new File(fulDirName);
               // System.out.println("ossFullDirPath="+ossFullDirPath.toString());
                final File[] files = ossFullDirPath.listFiles();
                for (final File pf : files) {
                  if (pf.isFile()) {
                    allFiles.add(pf);
                  }
                }
              }
            }
          }
        }
      }
    }
    return allFiles;
  }

  /**
   * @return - Delete files containing string value from File folder directory
   * @param folder
   *          (File): Folder to delete files from - i.e.
   *          test_automation_ltees/regression_results
   * @param stringVal
   *          (String): Delete files with specified string value - i.e. delete
   *          all files that contains the String "A20110802"
   */
  public void deleteFilesContainingString(final File folder, String stringVal) {
    stringVal = stringVal.toLowerCase();

    final File[] files = folder.listFiles();
    for (final File file : files) {
      final String fileNameToLower = file.getName().toLowerCase();

      if (file.isFile() && fileNameToLower.contains(stringVal)) {
        file.delete();
      }
    }
  }

  /**
   * @return - Delete files containing string value from File ArrayList
   *         directory
   * @param arrayList
   *          (ArrayList): File or String ArrayList containing files
   * @param stringVal
   *          (String): Delete files with specified string value - i.e. delete
   *          all files that contains the String "A20110802"
   */
  public void deleteFilesContainingString(final ArrayList<?> arrayList, String stringVal) {
    stringVal = stringVal.toLowerCase();

    for (int i = 0; i < arrayList.size(); i++) {
      final Object fileName = arrayList.get(i);
      final String fileNameStr = fileName.toString();
      final String fileNameToLower = fileNameStr.toLowerCase();

      if (fileNameToLower.contains(stringVal)) {
        final File file = new File(fileNameStr);
        file.delete();
      }
    }
  }

  /**
   * @return File Array []: Get Files with specified extension
   * @param arrayList
   *          (ArrayList): File or String ArrayList containing files
   * @param extension
   *          (String): File Extension - i.e. "XML", "gz" etc.
   */
  public File[] getFilesWithExtension(final File folder, final String extension) {
    final List<File> xmlList = new ArrayList<File>();
    xmlList.clear();
    final File[] files = folder.listFiles();

    for (final File pf : files) {
      if (pf.isFile() && getFileExtensionName(pf).equalsIgnoreCase(extension)) {
        xmlList.add(pf);
      }
    }
    return xmlList.toArray(new File[xmlList.size()]);
  }

  /**
   * @return File Array []: Get Files with specified extension
   * @param folder
   *          (File): Folder to delete files from - i.e.
   *          test_automation_ltees/regression_results
   * @param extension
   *          (String): File Extension - i.e. "XML", "gz" etc.
   */
  public ArrayList<File> getFilesWithExtension(final ArrayList<?> arrayList, final String extension) {
    final ArrayList<File> filesExt = new ArrayList<File>();
    for (int i = 0; i < arrayList.size(); i++) {
      final Object fileName = arrayList.get(i);
      final File file = new File(fileName.toString());
      final String fileExt = getFileExtensionName(file);

      if (file.isFile() && fileExt.equalsIgnoreCase(extension)) {
        filesExt.add(file);
      }
    }
    return filesExt;
  }

  /**
   * @return ArrayList File<>: unZip files and add unZipped files to ArrayList
   * @param arrayList
   *          (ArrayList): File or String ArrayList containing files
   * @param deleteGzipfileOnSuccess
   *          (Boolean): True/False to Delete GZ files after unZipped
   */
  public ArrayList<File> unGZip(final ArrayList<?> arrayList, final boolean deleteGzipfileOnSuccess)
      throws FileNotFoundException, IOException {
    final ArrayList<File> fileArray = new ArrayList<File>();

    for (int i = 0; i < arrayList.size(); i++) {
      final Object fileObj = arrayList.get(i);
      final File file = new File(fileObj.toString());
      final String fileName = file.toString().toLowerCase();

      if (file.isFile() && getFileExtensionName(fileName).indexOf("gz") != -1) {
        final File filUnZip = unGzip(file, deleteGzipfileOnSuccess);
        fileArray.add(filUnZip);
      } else {
        fileArray.add(file);
      }
    }
    return fileArray;
  }

  /**
   * @return File: Uncompressed/unZipped .GZ file
   * @param file
   *          (File): File that needs to be unZipped
   * @param deleteGzipfileOnSuccess
   *          (Boolean): True/False to Delete GZ files after unZipped
   */
  public File unGzip(final File file, final boolean deleteGzipfileOnSuccess) throws IOException {
    final GZIPInputStream gin = new GZIPInputStream(new FileInputStream(file));
    final File outFile = new File(file.getParent(), file.getName().replaceAll("\\.gz$", ""));
    final FileOutputStream fos = new FileOutputStream(outFile);
    final byte[] buf = new byte[100000];
    int len;

    while ((len = gin.read(buf)) > 0) {
      fos.write(buf, 0, len);
    }

    gin.close();
    fos.close();

    if (deleteGzipfileOnSuccess) {
      file.delete();
    }
    return outFile;
  }

  /**
   * @return Verify that Files in an ArrayList are .GZ compressed format
   *         (PASS/FAIL/Not Selected Results)
   * @param files
   *          (ArrayList File<>): File ArrayList
   */
  public ArrayList<String> verifyFileFormat(final ArrayList<File> files) throws FileNotFoundException, IOException {
    final ArrayList<String> fileExtensionCheckResults = new ArrayList<String>();
    final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
    final String yesNo = propFile.getPropertiesSingleStringValue("verifyFileFormat", counterPropFileDir);

    for (final File fileName : files) {
      final boolean check = fileName.isFile() && getFileExtensionName(fileName).indexOf("gz") != -1;

      if (yesNo.equalsIgnoreCase("Y")) {
        if (check == true) {
          fileExtensionCheckResults.add("PASS");
        } else {
          fileExtensionCheckResults.add("FAIL");
        }
      } else
        fileExtensionCheckResults.add("Not Selected");
    }
    return fileExtensionCheckResults;
  }

  /**
   * @return Boolean - Verify that file is .GZ compressed format (True/False
   *         result)
   * @param fileName
   *          (String): XML File Name
   */
  public boolean verifyFileFormat(final String fileName) throws FileNotFoundException, IOException {
    boolean check = false;
    if (fileName.endsWith("gz")) {
      check = true;
    } else {
      check = false;
    }
    return check;
  }

  /**
   * @return Void: Copy File from File Source [src] to File Destination [dst]
   * @param src
   *          (File): File Source i.e. C:\\LTEES Test
   *          Automation\\lte_event_stat_file\\events_oss_3\\dir38\\file.xml *
   * @param dst
   *          (File): File Destination i.e C:\\LTEES Test
   *          Automation\\lte_event_stat_file\\events_oss_2\\dir12\\file.xml
   */
  void copy(final File src, final File dst) throws IOException {
    final InputStream in = new FileInputStream(src);
    final OutputStream out = new FileOutputStream(dst);

    // Transfer bytes from in to out
    final byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }
    in.close();
    out.close();
  }

  /**
   * @return ArrayList File<>: Get all files from ArrayList with specified file
   *         extension [fileExtension]
   * @param files
   *          (ArrayList File<>): File ArrayList
   * @param fileExtension
   *          (String): File Extension i.e. "xml" or "gz" etc
   */
  public ArrayList<File> getFilesWithExt(final ArrayList<File> fileArray, final String fileExtension) {
    final ArrayList<File> gzZipFiles = new ArrayList<File>();
    for (int i = 0; i < fileArray.size(); i++) {
      final File pf = fileArray.get(i);
      if (pf.isFile() && getFileExtensionName(pf).endsWith(fileExtension.toLowerCase())) {
        gzZipFiles.add(pf);
      }
    }
    return gzZipFiles;
  }

  /**
   * @return File Array[]: Get all files from File Folder with specified file
   *         extension [fileExtension]
   * @param folder
   *          (File): Folder to get the files from - i.e. C:\\Files
   * @param fileExtension
   *          (String): File Extension i.e. "xml" or "gz" etc
   */
  public File[] getFilesWithExt(final File folder, final String fileExtension) {
    final File[] files = folder.listFiles();
    final ArrayList<File> gzZipList = new ArrayList<File>();

    for (final File pf : files) {
      if (pf.isFile() && getFileExtensionName(pf).endsWith(fileExtension.toLowerCase())) {
        gzZipList.add(pf);
      }
    }
    return gzZipList.toArray(new File[gzZipList.size()]);
  }

  /**
   * @return Void - Delete all files from all directories (files from
   *         "InputFolder_Directory" specified in app.properties file) with
   *         specified file extension
   * @param fileExtension
   *          (String): File Extension i.e. "xml" or "gz" etc
   */
  public void deleteAllFilesWithExtension(final String fileExtension) throws FileNotFoundException, IOException {
    final ArrayList<File> allFiles = getAllFilesFromInputFolderToFileArrayList();

    for (int i = 0; i < allFiles.size(); i++) {
      final File fileName = allFiles.get(i);
      final String fileExt = getFileExtensionName(fileName);
      if (fileExt.equalsIgnoreCase(fileExtension)) {
        fileName.delete();
      }
    }
  }

  /**
   * @return Void - Delete XML files from File ArrayList
   * @param files
   *          (ArrayList <>): File or String ArrayList
   * @param fileExtension
   *          (String): File Extension i.e. "xml" or "gz" etc
   * 
   */
  public void deleteFilesWithExtension(final ArrayList<?> files, final String fileExtension)
      throws FileNotFoundException, IOException {
    for (int i = 0; i < files.size(); i++) {
      final Object fileName = files.get(i);
      final File file = new File(fileName.toString());
      final String fileExt = getFileExtensionName(file);
      if (fileExt.equalsIgnoreCase(fileExtension)) {
        file.delete();
      }
    }
  }

  /**
   * @return ArrayList File<>: Delete Copy Files from ArrayList [files] and
   *         return new File ArrayList without "copy" files
   * @param files
   *          (ArrayList <>): File or String ArrayList
   * @param fileNameContainsStr
   *          (String): String values contained in the FileName
   */
  public ArrayList<File> deleteFilesWhereFileNameContains(final ArrayList<?> files, final String fileNameContainsStr) {
    final ArrayList<File> newFileArray = new ArrayList<File>();

    for (int i = 0; i < files.size(); i++) {
      final Object fileNameObj = files.get(i);
      final String fileNameStr = fileNameObj.toString();
      final File file = new File(fileNameStr);
      final boolean indexContains = fileNameStr.toLowerCase().contains(fileNameContainsStr.toLowerCase());

      if (file.isFile() && indexContains) {
        file.delete();
      } else {
        newFileArray.add(file);
      }
    }
    return newFileArray;
  }

  /**
   * @return ArrayList File<>: Delete Files, from a File Folder [folder], where
   *         FileName contains a specified string [fileNameContainsStr]
   * @param folder
   *          (File): Folder to get the files from - i.e. C:\\Files
   * @param fileNameContainsStr
   *          (String): String values contained in the FileName
   */
  public void deleteCopyFilesFromDirectory(final File folder, final String fileNameContainsStr) {
    final File[] files = folder.listFiles();

    for (final File fileName : files) {
      final String fileNameStr = fileName.getName();
      final boolean indexContains = fileNameStr.toLowerCase().contains(fileNameContainsStr.toLowerCase());

      if (fileName.isFile() && indexContains) {
        fileName.delete();
      }
    }
  }

  /**
   * @return String: Get File Extension from FileName
   * @param fileName
   *          (File): File FileName
   */
  public String getFileExtensionName(final File fileName) {
    String extension = "";
    final String fileNameStr = fileName.toString();
    if (fileNameStr.indexOf(".") != -1) {
      final String[] fileStr = fileNameStr.split("[.]");
      extension = fileStr[fileStr.length - 1];
    }
    return extension;
  }

  /**
   * @return String: Get File Extension from FileName
   * @param fileName
   *          (File): File FileName
   */
  public String getFileExtensionName(final String fileName) {
    String extension = "";
    if (fileName.indexOf(".") != -1) {
      final String[] fileStr = fileName.split("[.]");
      extension = fileStr[fileStr.length - 1];
    }
    return extension;
  }

  // //////////////////////////////////////////////////////////////////////////////////////////
  // MZCONTROLLER
  // /////////////////////////////////////////////////////////////////////////////////////////
  /**
   * @author esaimkh
   * @return int - Accumulative Total number of files from rops
   */
  public int countNumberOfFilesFromRops(final ArrayList<String> latestFiles, final int numOfRops)
      throws FileNotFoundException, IOException {
    final int[] filePerRop = countFilesPerRop(latestFiles, numOfRops);
    int totalFiles = 0;

    for (int i = 0; i < filePerRop.length; i++) {
      final int val = filePerRop[i];
      totalFiles = totalFiles + val;
    }
    return totalFiles;
  }

  /**
   * @return int[] - Total number of files per rop
   */
  public int[] countFilesPerRop(final ArrayList<?> latestFiles, final int numOfRops) throws FileNotFoundException,
      IOException {
    final int[] ropFileCount = new int[numOfRops];
    final String[] startTime = getRopStartTimes(latestFiles, numOfRops, true);
    final String[] endTime = getRopEndTimes(latestFiles, numOfRops, true);

    for (int i = 0; i < ropFileCount.length; i++) {
      int counter = 0;

      for (int r = 0; r < latestFiles.size(); r++) {
        final String start = startTime[i];
        final String end = endTime[i];
        final Object fileNameObj = latestFiles.get(r);
        final String fileNameStr = fileNameObj.toString();

        if (fileNameStr.indexOf(start) != -1 && fileNameStr.indexOf(end) != -1) {
          counter = counter + 1;
        }
      }
      ropFileCount[i] = counter;
    }
    return ropFileCount;
  }

  /**
   * @return Integer: Get the Number of ROP's based on Data Generator TimeFrame
   */
  public int getNumberOfRops() throws FileNotFoundException, IOException {
    final int runTimeMinutes = Integer.parseInt(propFile.getAppPropSingleStringValue("DataGen_Run_Time"));
    final int numOfRops = runTimeMinutes / 15;
    return numOfRops;
  }

  /**
   * @return
   */
  public String[] getRopStartTimes(final ArrayList<?> latestFiles, final int numOfRops, final boolean descOrder)
      throws FileNotFoundException, IOException {
    final String[] allStartTimes = xml.getStartTimeFromFileName(latestFiles);
    final int ropLen = numOfRops;
    final String[] startTimes = common.removeDuplicatesWithOrderToStringArray(allStartTimes, false);
    final String[] ropStartTimes = new String[ropLen];

    for (int i = 0; i < ropStartTimes.length; i++) {
      ropStartTimes[i] = startTimes[i];
    }
    Arrays.sort(ropStartTimes);
    final String[] ropStartTimesSort = common.removeDuplicatesWithOrderToStringArray(ropStartTimes, descOrder);
    return ropStartTimesSort;
  }

  /**
   * @author esaimkh
   * @return
   */
  public String[] getRopEndTimes(final ArrayList<?> latestFiles, final int numOfRops, final boolean descOrder)
      throws FileNotFoundException, IOException {
    final String[] allEndTimes = xml.getEndTimeFromFileName(latestFiles);
    final int ropLen = numOfRops;
    final String[] endTimes = common.removeDuplicatesWithOrderToStringArray(allEndTimes, false);
    final String[] ropEndTimes = new String[ropLen];

    for (int i = 0; i < ropEndTimes.length; i++) {
      ropEndTimes[i] = endTimes[i];
    }
    Arrays.sort(ropEndTimes);
    final String[] ropEndTimesSort = common.removeDuplicatesWithOrderToStringArray(ropEndTimes, descOrder);
    return ropEndTimesSort;
  }

  /**
   * @return Integer: Number of ROP's for either Data Generator OR Delta
   *         Topology
   * @param dataGenTimeFrame
   * @param deltaTopologyRops
   */
  // public int numberOfRops(final boolean dataGenTimeFrame, final boolean
  // deltaTopologyRops)
  // throws FileNotFoundException, IOException {
  // final int runTimeMinutes =
  // Integer.parseInt(propFile.getAppPropSingleStringValue("DataGen_Run_Time"));
  // final int ropNumDeltaTop =
  // Integer.parseInt(propFile.getAppPropSingleStringValue("Delta_Topology_Gen_Rop"));
  // int numOfRops = 0;
  //
  // if (dataGenTimeFrame) {
  // numOfRops = runTimeMinutes / 15;
  // } else if (deltaTopologyRops) {
  // numOfRops = ropNumDeltaTop;
  // }
  //
  // return numOfRops;
  // }

}
