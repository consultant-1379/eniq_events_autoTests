/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.workspace.utilites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NetworkTrafficUtils{
	
	public static List<String> extractUrlsFromNetworkTraffic(String trafficOutput, String urlPath) {
		List<String> urls = new ArrayList<String>();
		
	    String[] traffic = trafficOutput.split("================================================================\n\n");
		for(String request: traffic){
			String url = request.split("\n")[0];
		    if(url.contains(urlPath)){
				  urls.add(url.substring(8));
			}
		}
		
		return urls;
	}
	
	public static String[] extractParametersFromUrl(String url) {
		return url.split("\\?")[1].split("&");
	}
	
	/* 
	 *  Examples: 
	 *  input: [dateTo=14052014, dateFrom=14052014, timeTo=1100, timeFrom=1000]
	 *  output: "2014-05-14 10:00 - 2014-05-14 11:00"
	 *  
	 *  input: [dataTimeTo=1401120000000, dataTimeFrom=1401118200000]
	 *  output: "2014-05-26 16:30 - 2014-05-26 17:00"
	 */
	public static String getDateTimeRangeFromParameters(String[] parameters) {
		String dateFrom = "", dateTo = "", timeFrom = "", timeTo = "";
		
		for(String parameter: parameters){
			if(parameter.contains("dateFrom")){
				dateFrom = parameter.split("=")[1].substring(4,8) + "-" + parameter.split("=")[1].substring(2,4) + "-" + parameter.split("=")[1].substring(0,2) +" ";
			}else if(parameter.contains("timeFrom")){
				timeFrom = parameter.split("=")[1].substring(0,2) + ":" + parameter.split("=")[1].substring(2,4) + " - ";
			}else if(parameter.contains("dateTo")){
				dateTo = parameter.split("=")[1].substring(4,8) + "-" + parameter.split("=")[1].substring(2,4) + "-" + parameter.split("=")[1].substring(0,2) +" ";
			}else if(parameter.contains("timeTo")){
				timeTo = parameter.split("=")[1].substring(0,2) + ":" + parameter.split("=")[1].substring(2,4);
			}
		}
		
		if((dateFrom + timeFrom + dateTo + timeTo).equals("")){
			
			String dataTimeFrom = "", dataTimeTo = "";
			
			for(String parameter: parameters){
				if(parameter.contains("dataTimeFrom")){
					dataTimeFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm - ").format(Long.parseLong(parameter.split("=")[1]));
				}else if(parameter.contains("dataTimeTo")){
					dataTimeTo = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.parseLong(parameter.split("=")[1]));
				}
			}
			
			return dataTimeFrom + dataTimeTo;
		}
		
		return dateFrom + timeFrom + dateTo + timeTo;
	}

	/* 
	 *  Example: 
	 *  input: [time=30]
	 *  output: "30"
	 */
	public static String getTimeRangeFromParameters(String[] parameters) {
		String time = "";
		
		for(String parameter: parameters){
			if(parameter.contains("time")){
				time = parameter.split("=")[1];
			}
		}
		
		return time;
	}
}
