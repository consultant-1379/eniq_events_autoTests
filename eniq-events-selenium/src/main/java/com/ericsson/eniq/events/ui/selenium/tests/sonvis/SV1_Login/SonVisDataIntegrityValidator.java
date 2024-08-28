package com.ericsson.eniq.events.ui.selenium.tests.sonvis.SV1_Login;

import com.ericsson.eniq.events.ui.selenium.tests.mss.CsvRead;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SonVisDataIntegrityValidator {

	protected List<Map<String, String>> reserveDataList = new ArrayList<Map<String, String>>();

	public void init(String csvFileName, List<String> headers) {
		loadCsv(csvFileName, headers);
	}
	
	/*
	 * Loads the reserve data from the csv file.
	 */
	protected void loadCsv(String csvFileName, List<String> headers) {
		try {
			// there are records in the reserve data with commas in it, so
			// semicolon should be used as a delimeter`

			CsvRead gsmCFAReservedCSV = new CsvRead(csvFileName, ';');

			gsmCFAReservedCSV.readHeaders(); // the first row are the headers

			while (gsmCFAReservedCSV.readRecord()) {
				Map<String, String> csvRecordMap = new HashMap<String, String>();
				for (String header : headers) {
					csvRecordMap.put(header, gsmCFAReservedCSV.get(header));
				}

				printMapData(csvRecordMap);

				reserveDataList.add(csvRecordMap);
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.out.println("File " + csvFileName + " not found.");
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		System.out.println("Records read: " + reserveDataList.size());
	}

	/*
	 * Prints the data contained in the map.
	 */
	protected void printMapData(Map<String, String> mapToPrint) {
		System.out.println(" *** *** *** *** ***");

		Set<Map.Entry<String, String>> set = mapToPrint.entrySet();
		Iterator<Map.Entry<String, String>> iter = set.iterator();

		while (iter.hasNext()) {
			Map.Entry<String, String> mapEntry = (Map.Entry<String, String>) iter.next();
			System.out.print(mapEntry.getKey() + ": ");
			System.out.println(mapEntry.getValue());
		}
		
		
	}





















}

