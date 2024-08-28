/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.SubStartMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author epatmah
 * @since 2012
 * 
 */
public class NetworkTabTestGroupGSM extends EniqEventsUIBaseSeleniumTest {

	// ***** Sub menus *****

	@Autowired
	private NetworkTab networkTab;

	@Autowired
	@Qualifier("controllerGroupNetworkGSMCFA")
	private CommonWindow controllerGroupNetworkGSMCallFailure;

	@Autowired
	@Qualifier("controllerNetworkGSMCFA")
	private CommonWindow controllerNetworkGSMCallFailure;

	@Autowired
	@Qualifier("accessAreaGroupNetworkGSMCFA")
	private CommonWindow accessAreaGroupNetworkGSMCallFailure;

	@Autowired
	@Qualifier("controllerCauseCodeNetworkGSMCFA")
	private CommonWindow controllerCauseCodeNetworkGSMCallFailure;

	@Autowired
	@Qualifier("controllerCauseCodeNetworkGSMCFA")
	private CommonWindow accessAreaCauseCodeNetworkGSMCallFailure;
	
	@Autowired
	@Qualifier("roamingAnalysisByCountryNetworkGSMCFA")
	private CommonWindow roamingAnalysisByCountryGSMCallFailure;
	
	@Autowired
	@Qualifier("roamingAnalysisByCountrySummaryNetworkGSMCFA")
	private CommonWindow roamingAnalysisByCountrySummaryGSMCallFailure;
	
	@Autowired
	@Qualifier("roamingAnalysisByOperatorNetworkGSMCFA")
	private CommonWindow roamingAnalysisByOperatorGSMCallFailure;
	
	@Autowired
	@Qualifier("roamingAnalysisByOperatorSummaryNetworkGSMCFA")
	private CommonWindow roamingAnalysisByOperatorSummaryGSMCallFailure;
	
	// ***** Headers for the different windows *****

	final List<String> controllerGroupNetworkSubMenus = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.CONTROLLER);

	final List<String> accessAreaGroupNetworkSubMenus = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.CAUSE_VALUE, GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.ACCESS_AREA);

	public static final List<String> controllerCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.CAUSE_GROUP_ID,
			GuiStringConstants.CAUSE_GROUP, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	public static final List<String> controllerSubCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.EXTENDED_CAUSE_ID_GSM,
			GuiStringConstants.EXTENDED_CAUSE_VALUE, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	final List<String> controllerDetailedEventAnalysisNetworkSubMenus = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.ACCESS_AREA, GuiStringConstants.VENDOR);

	final List<String> controllerDetailedEventAnalysisNetwork = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.MSISDN, GuiStringConstants.TAC,
			GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.VAMOS_NEIGHBOR_INDICATOR, GuiStringConstants.RSAI, GuiStringConstants.CHANNEL_TYPE,
			GuiStringConstants.URGENCY_CONDITION, GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA);

	final List<String> accessareaSummaryEventAnalysisNetwork = Arrays.asList(GuiStringConstants.RAN_VENDOR,
			GuiStringConstants.ACCESS_AREA, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS,
			GuiStringConstants.FAILURE_RATIO);

	final List<String> accessAreaCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.CAUSE_CODE_ID,
			GuiStringConstants.CAUSE_CODE_DESCRIPTION, GuiStringConstants.OCCURRENCES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	final List<String> accessAreaSubCauseCodeNetworkSubMenus = Arrays.asList(GuiStringConstants.EXTENDED_CAUSE_CODE_ID,
			GuiStringConstants.EXTENDED_CAUSE_CODE_DESC, GuiStringConstants.FAILURES,
			GuiStringConstants.IMPACTED_SUBSCRIBERS);

	final List<String> accessAreaDetailedEventAnalysisNetworkSubMenus = Arrays.asList(GuiStringConstants.EVENT_TIME,
			GuiStringConstants.IMSI, GuiStringConstants.TAC, GuiStringConstants.TERMINAL_MAKE,
			GuiStringConstants.TERMINAL_MODEL, GuiStringConstants.EVENT_TYPE, GuiStringConstants.RELEASE_TYPE,
			GuiStringConstants.EXTENDED_CAUSE_CODE_DESC, GuiStringConstants.CONTROLLER, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.RAN_VENDOR);
	
	public static final List<String> controllerGroupSummaryWindow = Arrays.asList(GuiStringConstants.EVENT_TYPE,
			GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);
			
	
	public static final List<String> controllerGroupEventSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR, GuiStringConstants.CONTROLLER,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS, 
			GuiStringConstants.FAILURE_RATIO);
	
	public static final List<String> controllerGroupCCASummaryWindow = Arrays.asList(GuiStringConstants.CONTROLLER, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS, 
			GuiStringConstants.FAILURE_RATIO);
	
	public static final List<String> accessAreaGroupCCASummaryWindow = Arrays.asList(GuiStringConstants.ACCESS_AREA, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.IMPACTED_CELLS, 
			GuiStringConstants.FAILURE_RATIO);
	
	public static final List<String> accessAreaGroupIndivCCASummaryWindow = Arrays.asList(GuiStringConstants.ACCESS_AREA, 
    		GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, GuiStringConstants.FAILURE_RATIO);
	
	//Arun code
	public static final List<String> accessAreaGroupEventSummaryWindow = Arrays.asList(GuiStringConstants.RAN_VENDOR, GuiStringConstants.ACCESS_AREA,
			GuiStringConstants.EVENT_TYPE, GuiStringConstants.FAILURES, GuiStringConstants.IMPACTED_SUBSCRIBERS, 
			GuiStringConstants.FAILURE_RATIO);
	
	private DataIntegrityValidator dataIntegrValidatorObjCS = new DataIntegrityValidator();

	@Override
	@Before
	public void setUp() {
		super.setUp();
		pause(GSMConstants.DEFAULT_WAIT_TIME);
		if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
			selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
			selenium.waitForElementToBePresent("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']",
					GSMConstants.DEFAULT_WAIT_TIME_STR);
			selenium.click("//a[@id='selenium_tag_Radio - Voice & Data, Core - Data']");
		}

		dataIntegrValidatorObjCS.init(GSMConstants.GSM_CFA_CD_RESERVED_DATA_FILENAME,
				GSMConstants.GSM_CFA_CD_RESERVED_DATA_COLUMNS);

	}

	// ***** Test Cases *****

	/**
	 * Requirement: Test Case: 5.5.1 Description: To verify that it is possible
	 * to view a detailed analysis of Call Drop failure events for a nominated
	 * Controller Group.
	 * 
	 * @throws PopUpException
	 */

	
	// GSMCFR-363, 856, 367, 
	@Test
	public void controllerCCACauseCodeDetailNavigation367() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER,
				SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "BSCB01,Ericsson,GSM");
		controllerCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(controllerCauseCodeNetworkSubMenus));
		
		controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
		pause(GSMConstants.DEFAULT_WAIT_TIME);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(controllerSubCauseCodeNetworkSubMenus));
	
		controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
		pause(GSMConstants.DEFAULT_WAIT_TIME);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_WINDOW_HEADERS));		
		
	/*	checkAllTimeRangesForIndividualNodes(NetworkType.CONTROLLER, controllerCauseCodeNetworkGSMCallFailure,
				controllerSubCauseCodeNetworkSubMenus, controllerDetailedEventAnalysisNetworkSubMenus); */

	}

	//GSMCFR-363 
	@Test
	public void controllerCCACauseCodeDetail363() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER,
				SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "BSCB01,Ericsson,GSM");
		controllerCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(controllerCauseCodeNetworkSubMenus));
		
		final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
		TimeRange[] timeRanges; 
		 if (!allTimeLabel.isEmpty()) {
	     	final String[] timeLabels = allTimeLabel.split(",");
	     	timeRanges = new TimeRange[timeLabels.length];
	     	for (int i = 0; i < timeLabels.length; i++) {
	     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
	     	}
	     } else {
	     	timeRanges = TimeRange.values();
	     }
		 
		 for (final TimeRange time : timeRanges) {
			 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
			 
			 
			// Code for Data Assertion.
			//System.out.println ("Loop Number" + time); 
			 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
			 boolean returnValue = dataIntegrValidatorObjCS.validateCauseCodeAnalysisBSCSummary(uiWindowData, time.getMiniutes());
			 assertTrue("Data Validation Failed", returnValue);		
	}
	}
	
	
	//GSMCFR-856 
		@Test
		public void controllerCCAExtendedCauseCodeDetail856() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "BSCB01,Ericsson,GSM");
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(controllerCauseCodeNetworkSubMenus));
						
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(controllerSubCauseCodeNetworkSubMenus));
					
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateExtendedCauseCodeAnalysisBSCSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}
		/*	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_WINDOW_HEADERS)); */
		}
	
		
	//GSMCFR -378 379 380
		@Test
		public void controllerGroupCCANavigation() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_CONTROLLER_GROUP);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
		//	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("ETENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.CONTROLLER_GROUP_SUMMARY_EVENT_ANALYSIS));	
			
			controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);	
			pause(GSMConstants.DEFAULT_WAIT_TIME);
					
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_WINDOW_HEADERS));	
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
	}
		//378
		@Test
		public void controllerGroupCCACauseGroup378() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_CONTROLLER_GROUP);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateCCACauseGroupBSCGroupSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
		}
		
		//380
		@Test
		public void controllerGroupCCAExtendedCausecode380() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_CONTROLLER_GROUP);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
					
				
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

				final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
			       break;
				            
				      } 
				        
				   } 
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateExtendedCauseCCABSCGroupSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
			
			}
	
		@Test
		public void controllerGroupCCABSCIndividual379() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_CONTROLLER_GROUP);
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
					
				
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

				final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
			       break;
				            
				      } 
				        
				   } 
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("ETENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.CONTROLLER_GROUP_SUMMARY_EVENT_ANALYSIS));
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateCCAIndividualBSCSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
			 controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);	
				pause(GSMConstants.DEFAULT_WAIT_TIME);
						
				assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
						.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_WINDOW_HEADERS));	
				pause(GSMConstants.DEFAULT_WAIT_TIME);
				
				
			
	}
		
		
			
		//Access Area cause code Analysis 914 957 958
		@Test
		public void accessAreaCauseCodeRANGSMCFA914() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("EXTENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_ACCESSAREA_WINDOW_HEADERS));	
								
			/*assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, controllerCauseCodeNetworkGSMCallFailure.getTimeRange()
					.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
			controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
			checkAllTimeRangesForIndividualNodes(NetworkType.ACCESS_AREA, controllerCauseCodeNetworkGSMCallFailure,
					accessAreaSubCauseCodeNetworkSubMenus, accessAreaDetailedEventAnalysisNetworkSubMenus);*/
		}
		@Test
		public void accessAreaCauseCodeCCA914() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateCCAcauseGroupAccessAreaSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
			
	}
		
		@Test
		public void accessAreaExtendedCauseCodeCCA957() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateExtendedCauseCCAAccessArea(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("EXTENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_ACCESSAREA_WINDOW_HEADERS));
		}
		//Access Area Group 386 387 385 958
		
		@Test
		public void accessAreaGroupCauseCodeRANGSMCFA386() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("EXTENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 			
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(accessAreaGroupIndivCCASummaryWindow));	
			
			controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);	
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_ACCESSAREA_WINDOW_HEADERS));	
								
			/*assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, controllerCauseCodeNetworkGSMCallFailure.getTimeRange()
					.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
			controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
			checkAllTimeRangesForIndividualNodes(NetworkType.ACCESS_AREA, controllerCauseCodeNetworkGSMCallFailure,
					accessAreaSubCauseCodeNetworkSubMenus, accessAreaDetailedEventAnalysisNetworkSubMenus);*/
		}
		
		@Test
		public void accessAreaGroupCauseCodeCCA386() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateCCAcauseGroupAccessAreaGroupSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
	}
		
		@Test
		public void accessAreaGroupExtendedCauseCodeCCA387() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateExtendedCauseCCAAccessAreaGroup(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
		}	
		}
		
		@Test
		public void accessAreaGroupCCAIndiidula385() throws PopUpException, NoDataException, InterruptedException {
			networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
					SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);
			controllerCauseCodeNetworkGSMCallFailure.openGridView();
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_CAUSE_GROUP_ANALYSIS_SUMMARY));
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_CAUSE_GROUP; index++) {

		        final String causeGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
		        
		        System.out.println("CAUSE GROUP -- "+causeGroup);
		        if (causeGroup.equals(GSMConstants.CAUSE_GROUP_AUTOMATION)) {

		        	controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.CAUSE_GROUP);
		            break;
		        } 
		        
		    }  
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_EXTENDED_CAUSE_ANALYSIS_SUMMARY));	
			
			for (int index = 0; index < GSMConstants.MAX_NUMBER_EXTENDED_CAUSE; index++) {

				final String extendedcauseGroup = controllerCauseCodeNetworkGSMCallFailure.getTableData(index, 1);
				        		        
				if (extendedcauseGroup.equals(GSMConstants.EXTENDEDCAUSE_GROUP_AUTOMATION)) {
					
					System.out.println("EXTENDED CAUSE GROUP -- "+extendedcauseGroup);
			       controllerCauseCodeNetworkGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
			       break;
				            
				      } 
				        
				   } 			
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(accessAreaGroupIndivCCASummaryWindow));	
			
			final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
			TimeRange[] timeRanges; 
			 if (!allTimeLabel.isEmpty()) {
		     	final String[] timeLabels = allTimeLabel.split(",");
		     	timeRanges = new TimeRange[timeLabels.length];
		     	for (int i = 0; i < timeLabels.length; i++) {
		     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
		     	}
		     } else {
		     	timeRanges = TimeRange.values();
		     }
			 
			 for (final TimeRange time : timeRanges) {
				 controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);				 
				 
				// Code for Data Assertion.
				 List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
				 boolean returnValue = dataIntegrValidatorObjCS.validateCCAIndividualAccessAreaSummary(uiWindowData, time.getMiniutes());
				 assertTrue("Data Validation Failed", returnValue);		
			 }	
			 
			controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);	
			pause(GSMConstants.DEFAULT_WAIT_TIME);
			
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
					.getTableHeaders().containsAll(GSMConstants.DEFAULT_DETAILED_CAUSE_CODE_ANALYSIS_ACCESSAREA_WINDOW_HEADERS));	
		}
	/**
	 * Requirement: Test Case: 5.5.4 Description: Verify that, from the Network
	 * Tab, it is possible to summary the cause codes on the basis of the
	 * failures for a nominated Access Area. From this summary view, it shall be
	 * possible to drill down to extended cause on individual cause codes from
	 * which it can drill down on detailed extended cause failure analysis.
	 * 
	 * @throws PopUpException
	 */

//	@Test
	public void accessAreaCauseCodeRANGSMCFA_5_4() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
				SubStartMenu.GSM_NETWORK_CAUSE_CODE_ANALYSIS, true, GSMConstants.ACCESS_AREA_NAME);
		accessAreaCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(accessAreaCauseCodeNetworkSubMenus));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, accessAreaCauseCodeNetworkGSMCallFailure.getTimeRange()
				.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		accessAreaCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_CODE_DESCRIPTION);
		checkAllTimeRangesForIndividualNodes(NetworkType.ACCESS_AREA, accessAreaCauseCodeNetworkGSMCallFailure,
				accessAreaSubCauseCodeNetworkSubMenus, accessAreaDetailedEventAnalysisNetworkSubMenus);
	}// 5.4 function end

	
	@Test
	public void controllerGroupSummaryWindow795() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER_GROUP,
				SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_CONTROLLER_GROUP);
	assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
			.containsAll(controllerGroupSummaryWindow));
		
	final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
		TimeRange[] timeRanges; 
		 if (!allTimeLabel.isEmpty()) {
	     	final String[] timeLabels = allTimeLabel.split(",");
	     	timeRanges = new TimeRange[timeLabels.length];
	     	for (int i = 0; i < timeLabels.length; i++) {
	     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
	     	}
	     } else {
	     	timeRanges = TimeRange.values();
	     }
		 
		 for (final TimeRange time : timeRanges) {
			 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
			 
			 
			// Code for Data Assertion.
			//System.out.println ("Loop Number" + time); 
			 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
			 boolean returnValue = dataIntegrValidatorObjCS.validateBSCGroupSummary(uiWindowData, time.getMiniutes());
			 assertTrue("Data Validation Failed", returnValue);
	}
}		 

	@Test
	public void controllerGroupCallSetUpFailures797() throws PopUpException, NoDataException, InterruptedException {

		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER_GROUP,
				SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_CONTROLLER_GROUP);
	assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
			.containsAll(controllerGroupSummaryWindow));
	
		controllerGroupNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerGroupEventSummaryWindow));
		
		final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
		TimeRange[] timeRanges; 
		 if (!allTimeLabel.isEmpty()) {
	     	final String[] timeLabels = allTimeLabel.split(",");
	     	timeRanges = new TimeRange[timeLabels.length];
	     	for (int i = 0; i < timeLabels.length; i++) {
	     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
	     	}
	     } else {
	     	timeRanges = TimeRange.values();
	     }
		 
		 for (final TimeRange time : timeRanges) {
			 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
			 
			 
			// Code for Data Assertion.
			//System.out.println ("Loop Number" + time); 
			 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
			 boolean returnValue = dataIntegrValidatorObjCS.validateBSCGroupCallSetUpFailuresSummary(uiWindowData, time.getMiniutes());
			 assertTrue("Data Validation Failed", returnValue);		
	}
}		 
	
@Test
	public void controllerGroupCallDrops796() throws PopUpException, NoDataException, InterruptedException {

		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER_GROUP,
				SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_CONTROLLER_GROUP);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerGroupSummaryWindow));
	
		controllerGroupNetworkGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerGroupEventSummaryWindow));
		
		final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
		TimeRange[] timeRanges; 
		 if (!allTimeLabel.isEmpty()) {
	     	final String[] timeLabels = allTimeLabel.split(",");
	     	timeRanges = new TimeRange[timeLabels.length];
	     	for (int i = 0; i < timeLabels.length; i++) {
	     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
	     	}
	     } else {
	     	timeRanges = TimeRange.values();
	     }
		 
		 for (final TimeRange time : timeRanges) {
			 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
			 
			 
			// Code for Data Assertion.
			//System.out.println ("Loop Number" + time); 
			 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
			 boolean returnValue = dataIntegrValidatorObjCS.validateBSCGroupCallDropsSummary(uiWindowData, time.getMiniutes());
			 assertTrue("Data Validation Failed", returnValue);
		
	}
}	
	
@Test
public void cellSummaryWindow286() throws PopUpException, NoDataException, InterruptedException {
	networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.ACCESS_AREA,
			SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, "CB01002,,BSCB01,Ericsson,GSM");
assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
		.containsAll(controllerGroupEventSummaryWindow));
	
final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
	TimeRange[] timeRanges; 
	 if (!allTimeLabel.isEmpty()) {
     	final String[] timeLabels = allTimeLabel.split(",");
     	timeRanges = new TimeRange[timeLabels.length];
     	for (int i = 0; i < timeLabels.length; i++) {
     		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
     	}
     } else {
     	timeRanges = TimeRange.values();
     }
	 
	 for (final TimeRange time : timeRanges) {
		 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
		 
		 
		// Code for Data Assertion.
		//System.out.println ("Loop Number" + time); 
		 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
		 boolean returnValue = dataIntegrValidatorObjCS.validateBSCSummary(uiWindowData, time.getMiniutes());
		 assertTrue("Data Validation Failed", returnValue);
}
}		 

// Roaming Analysis by Country and Operator test cases

// Roaming Analysis by Country - Call Setup Failure
@Test
public void roamingAnalysisByCountryCSF350() throws Exception {       
	    	
	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_COUNTRY);
    selenium.waitForPageLoadingToComplete();
    
    roamingAnalysisByCountryGSMCallFailure.toggleGraphToGrid();
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_COUNTRY_WINDOW_HEADERS));
    //String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
    roamingAnalysisByCountryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));
    
    roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));
    
    roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.OPERATOR);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));
    
}

@Test
public void roamingAnalysisByCountryCSFDataAG350() throws Exception {       
	    	
	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_COUNTRY);
    selenium.waitForPageLoadingToComplete();
           
    roamingAnalysisByCountryGSMCallFailure.toggleGraphToGrid();
    pause(GSMConstants.DEFAULT_WAIT_TIME);
        
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_COUNTRY_WINDOW_HEADERS));    
    
   for (int index = 0; index < GSMConstants.MAX_NUMBER_ROMAING_COUNTRY; index++) {

        final String country = roamingAnalysisByCountrySummaryGSMCallFailure.getTableData(index, 0);
        
        System.out.println("COUNTRY"+country);
        if (country == GSMConstants.ROAMING_COUNTRY) {

        	roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
            break;
        }
    }       
   assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
           .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));
   
   final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
	TimeRange[] timeRanges; 
	 if (!allTimeLabel.isEmpty()) {
    	final String[] timeLabels = allTimeLabel.split(",");
    	timeRanges = new TimeRange[timeLabels.length];
    	for (int i = 0; i < timeLabels.length; i++) {
    		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
    	}
    } else {
    	timeRanges = TimeRange.values();
    }
	 
	 for (final TimeRange time : timeRanges) {
		 roamingAnalysisByCountrySummaryGSMCallFailure.setTimeRange(time);
		 
		 
		// Code for Data Assertion.
		//System.out.println ("Loop Number" + time); 
		 List<Map<String, String>> uiWindowData = roamingAnalysisByCountrySummaryGSMCallFailure.getAllTableData();
		 boolean returnValue = dataIntegrValidatorObjCS.validateRoamingAnalysisCountry(uiWindowData, time.getMiniutes());
		 assertTrue("Data Validation Failed", returnValue);
	 }
		 
   roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
   pause(GSMConstants.DEFAULT_WAIT_TIME);
   
   assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
           .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));
} 

@Test
public void roamingAnalysisByOperatorCSFDataAG764() throws Exception {       
	    	
	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_OPERATOR);
    selenium.waitForPageLoadingToComplete();
    
    roamingAnalysisByOperatorGSMCallFailure.toggleGraphToGrid();
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_OPERATOR_WINDOW_HEADERS));   
    
   for (int index = 0; index < GSMConstants.MAX_NUMBER_ROMAING_OPERATOR; index++) {

        final String operator = roamingAnalysisByOperatorGSMCallFailure.getTableData(index, 0);
        
        System.out.println("Operator -- "+operator);
        if (operator == GSMConstants.ROAMING_OPERATOR) {

        	roamingAnalysisByOperatorGSMCallFailure.clickTableCell(index, GuiStringConstants.FAILURES);
            break;
        }
    }       
   assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
           .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_OPERATOR_SUMMARY_WINDOW_HEADERS));
      
   final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
	TimeRange[] timeRanges; 
	 if (!allTimeLabel.isEmpty()) {
    	final String[] timeLabels = allTimeLabel.split(",");
    	timeRanges = new TimeRange[timeLabels.length];
    	for (int i = 0; i < timeLabels.length; i++) {
    		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
    	}
    } else {
    	timeRanges = TimeRange.values();
    }
	 
	 for (final TimeRange time : timeRanges) {
		 roamingAnalysisByCountrySummaryGSMCallFailure.setTimeRange(time);
		 
		 
		// Code for Data Assertion.
		//System.out.println ("Loop Number" + time); 
		 List<Map<String, String>> uiWindowData = roamingAnalysisByOperatorSummaryGSMCallFailure.getAllTableData();
		 boolean returnValue = dataIntegrValidatorObjCS.validateRoamingAnalysisOperator(uiWindowData, time.getMiniutes());
		 assertTrue("Data Validation Failed", returnValue);
	 }
		 
	 roamingAnalysisByOperatorSummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
     pause(GSMConstants.DEFAULT_WAIT_TIME);
     
     assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
             .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_BY_OPERATOR_GSM_CALLSETUP_WINDOW_HEADERS));
     
} 

@Test
public void roamingAnalysisByCountryCD350() throws Exception {       
	    	
	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_COUNTRY);
    selenium.waitForPageLoadingToComplete();
    
    roamingAnalysisByCountryGSMCallFailure.toggleGraphToGrid();
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_COUNTRY_WINDOW_HEADERS));
    //String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
    roamingAnalysisByCountryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));
    
    roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_GSM_CALLSETUPFAILURE_WINDOW_HEADERS));
    
    roamingAnalysisByCountrySummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.OPERATOR);
    pause(GSMConstants.DEFAULT_WAIT_TIME);
    
    assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountrySummaryGSMCallFailure.getTableHeaders()
            .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));
    
}
// Roaming Analysis by operator - CallSeupfailure - GSMCFR-350, GSMCFR-752, GSMCFR-764, GSMCFR-762
  @Test
  public void roamingAnalysisByOperatorCSF764() throws Exception {       
    	    	
    	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_OPERATOR);
        selenium.waitForPageLoadingToComplete();
        
        roamingAnalysisByOperatorGSMCallFailure.toggleGraphToGrid();
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_OPERATOR_WINDOW_HEADERS));
        
        assertTrue(GuiStringConstants.ERROR_LOADING + GSMConstants.ROAMING_ANALYSIS_OPERATOR_WINDOW_TITLE,
				roamingAnalysisByOperatorGSMCallFailure.getWindowHeaderLabel()
				.equals(GSMConstants.ROAMING_ANALYSIS_OPERATOR_WINDOW_TITLE));
        
        //String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
        roamingAnalysisByOperatorGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
       
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_OPERATOR_SUMMARY_WINDOW_HEADERS));
        
        roamingAnalysisByOperatorSummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_BY_OPERATOR_GSM_CALLSETUP_WINDOW_HEADERS));
        
        roamingAnalysisByOperatorSummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.COUNTRY);
        pause(GSMConstants.DEFAULT_WAIT_TIME);
        
        assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
                .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS)); 
        
	}

//Roaming Analysis by operator - CallSetupfailure - GSMCFR-350, GSMCFR-752, GSMCFR-764, GSMCFR-762
 @Test
 public void roamingAnalysisByOperatorCD764() throws Exception {       
   	    	
   	networkTab.openRoamingAnalysisWindowGSM(SubStartMenu.ROAMING_ANALYSIS_RAN_GSM_OPERATOR);
       selenium.waitForPageLoadingToComplete();
       
       roamingAnalysisByOperatorGSMCallFailure.toggleGraphToGrid();
       pause(GSMConstants.DEFAULT_WAIT_TIME);
       
       assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorGSMCallFailure.getTableHeaders()
               .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_OPERATOR_WINDOW_HEADERS));
       
       assertTrue(GuiStringConstants.ERROR_LOADING + GSMConstants.ROAMING_ANALYSIS_OPERATOR_WINDOW_TITLE,
				roamingAnalysisByOperatorGSMCallFailure.getWindowHeaderLabel()
				.equals(GSMConstants.ROAMING_ANALYSIS_OPERATOR_WINDOW_TITLE));
       
       roamingAnalysisByOperatorGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
       pause(GSMConstants.DEFAULT_WAIT_TIME);
      
       assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
               .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_OPERATOR_SUMMARY_WINDOW_HEADERS));
       
       roamingAnalysisByOperatorSummaryGSMCallFailure.clickTableCell(1, GuiStringConstants.FAILURES);
       pause(GSMConstants.DEFAULT_WAIT_TIME);
       
       assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
               .containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_BY_OPERATOR_GSM_CALLSETUP_WINDOW_HEADERS));
       
       roamingAnalysisByOperatorSummaryGSMCallFailure.clickTableCell(0, GuiStringConstants.COUNTRY);
       pause(GSMConstants.DEFAULT_WAIT_TIME);
       
       assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorSummaryGSMCallFailure.getTableHeaders()
               .containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS)); 
       
	}
 
 //Arun Code 
 
 @Test
 public void controllerGroupSummaryWindow798() throws PopUpException, NoDataException, InterruptedException {
 	networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
 			SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_ACCESSAREA_GROUP);
 assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
 		.containsAll(controllerGroupSummaryWindow));
 	
 final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
 	TimeRange[] timeRanges; 
 	 if (!allTimeLabel.isEmpty()) {
      	final String[] timeLabels = allTimeLabel.split(",");
      	timeRanges = new TimeRange[timeLabels.length];
      	for (int i = 0; i < timeLabels.length; i++) {
      		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
      	}
      } else {
      	timeRanges = TimeRange.values();
      }
 	 
 	 for (final TimeRange time : timeRanges) {
 		 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
 		 
 		 
 		// Code for Data Assertion.
 		//System.out.println ("Loop Number" + time); 
 		 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
 		 boolean returnValue = dataIntegrValidatorObjCS.validateCellGroupSummary(uiWindowData, time.getMiniutes());
 		assertTrue("Data Validation Failed", returnValue);
 }
 }		 

 @Test
 public void controllerGroupCallSetUpFailures800() throws PopUpException, NoDataException, InterruptedException {

 	networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
 			SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_ACCESSAREA_GROUP);
 assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
 		.containsAll(controllerGroupSummaryWindow));

 	controllerGroupNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
 	assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
 			.containsAll(accessAreaGroupEventSummaryWindow));
 	
 	final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
 	TimeRange[] timeRanges; 
 	 if (!allTimeLabel.isEmpty()) {
      	final String[] timeLabels = allTimeLabel.split(",");
      	timeRanges = new TimeRange[timeLabels.length];
      	for (int i = 0; i < timeLabels.length; i++) {
      		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
      	}
      } else {
      	timeRanges = TimeRange.values();
      }
 	 
 	 for (final TimeRange time : timeRanges) {
 		 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
 		 
 		 
 		// Code for Data Assertion.
 		//System.out.println ("Loop Number" + time); 
 		 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
 		 boolean returnValue = dataIntegrValidatorObjCS.validateCellGroupCallSetUpFailuresSummary(uiWindowData, time.getMiniutes());
 		 assertTrue("Data Validation Failed", returnValue);		
 }
 }		 

 @Test
 public void controllerGroupCallDrops799() throws PopUpException, NoDataException, InterruptedException {

 	networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
 			SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CCA_ACCESSAREA_GROUP);
 	assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
 			.containsAll(controllerGroupSummaryWindow));

 	controllerGroupNetworkGSMCallFailure.clickTableCell(1, GuiStringConstants.EVENT_TYPE);
 	assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
 			.containsAll(accessAreaGroupEventSummaryWindow));
 	
 	final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
 	TimeRange[] timeRanges; 
 	 if (!allTimeLabel.isEmpty()) {
      	final String[] timeLabels = allTimeLabel.split(",");
      	timeRanges = new TimeRange[timeLabels.length];
      	for (int i = 0; i < timeLabels.length; i++) {
      		timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
      	}
      } else {
      	timeRanges = TimeRange.values();
      }
 	 
 	 for (final TimeRange time : timeRanges) {
 		 controllerGroupNetworkGSMCallFailure.setTimeRange(time);
 		 
 		 
 		// Code for Data Assertion.
 		//System.out.println ("Loop Number" + time); 
 		 List<Map<String, String>> uiWindowData = controllerGroupNetworkGSMCallFailure.getAllTableData();
 		 boolean returnValue = dataIntegrValidatorObjCS.validateCellGroupCallDropsSummary(uiWindowData, time.getMiniutes());
 		 assertTrue("Data Validation Failed", returnValue);
 	
 }
 }


 /* ######################
  Following test cases is commented because these test cases are merged in other test cases according to feature flow.
  ####################
  @Test
	public void controllerGroupRANGSMCFA_5_1() throws PopUpException, NoDataException, InterruptedException {

		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER_GROUP,
				SubStartMenu.NETWORK_RAN_GSM_DETAILED_EVENT_ANALYSIS, true, "Regression_bsc_group"); 
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerGroupNetworkSubMenus));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				controllerGroupNetworkGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		checkAllTimeRangesForGroups(GSMConstants.CONTROLLER_GROUP_MEMBER_NAMES, controllerGroupNetworkGSMCallFailure,
				GSMConstants.COLUMN_NUMBER_FOR_CONTROLLER);

	}// 5.1 function end

	// 5.2...

	/**
	 * Requirement: Test Case: 5.5.2 Description: To verify that it is possible
	 * to view a detailed analysis of Call Drop failure events for a nominated
	 * Access Area Group.
	 * 
	 * @throws PopUpException
	 */

/*	@Test
	public void accessAreaGroupRANGSMCFA_5_2() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
				SubStartMenu.NETWORK_RAN_GSM_DETAILED_EVENT_ANALYSIS, true, "Regression_access_area_group");
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(accessAreaGroupNetworkSubMenus));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				accessAreaGroupNetworkGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		checkAllTimeRangesForGroups(GSMConstants.ACCESS_AREA_GROUP_MEMBER_NAMES, accessAreaGroupNetworkGSMCallFailure,
				GSMConstants.COLUMN_NUMBER_FOR_ACCESS_AREAS);

	}// 5.2 function end

	/**
	 * Requirement: Test Case: 5.5.3 Description: Verify that, from the Network
	 * Tab, it is possible to summary the cause codes on the basis of the
	 * failures for a nominated Controller. From this summary view, it shall be
	 * possible to drill down to extended cause on individual cause codes from
	 * which it can drill down on detailed extended cause failure analysis.
	 * 
	 * @throws PopUpException
	 */

/*	@Test
	public void controllerCauseCodeRANGSMCFA_5_3() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.CONTROLLER,
				SubStartMenu.GSM_NETWORK_CAUSE_CODE_ANALYSIS, true, "BSCA01,Ericsson,GSM");
		controllerCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(controllerCauseCodeNetworkSubMenus));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, controllerCauseCodeNetworkGSMCallFailure.getTimeRange()
				.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		controllerCauseCodeNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_CODE_DESCRIPTION);
		checkAllTimeRangesForIndividualNodes(NetworkType.CONTROLLER, controllerCauseCodeNetworkGSMCallFailure,
				controllerSubCauseCodeNetworkSubMenus, controllerDetailedEventAnalysisNetworkSubMenus);

	} // 5.3 function end

  ###############
  /**
	 * DEFT-EE-GSM CFR-HFR GSMCFR-170 Subscriber Flow - details of Extended
	 * Cause by Cause Group and BSC
	 * 
	 * @throws PopUpException
	 *             , NoDataException, InterruptedException
	 */
/*	@Test
	public void detailsExtendedCauseBSC_GSMCFR170() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER,
				SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CONTROLLER_NAME_LONG_FORMAT_ALTERNATIVE);

		controllerNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.EVENT_TYPE);
		controllerNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.CAUSE_GROUP);
		controllerNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);

		assertTrue(
				GuiStringConstants.ERROR_LOADING + GSMConstants.DETAILS_EXTENDED_CAUSE_BSC_GSMCFR_170_WINDOW_TITLE,
				controllerNetworkGSMCallFailure.getWindowHeaderLabel().equals(
						GSMConstants.DETAILS_EXTENDED_CAUSE_BSC_GSMCFR_170_WINDOW_TITLE));

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerDetailedEventAnalysisNetwork));

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				controllerNetworkGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(
				FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				validateDrillDownOnFailuresAllTimeRanges(controllerNetworkGSMCallFailure, GuiStringConstants.IMSI,
						GSMConstants.IMSI_NUMBER, controllerNetworkGSMCallFailure.getTableHeaders()));
	} */

	/*
	 * DEFT-EE-GSM CFR-HFR GSMCFR-171 Subscriber Flow - Call Drop Distribution
	 * across Cells in a BSC
	 * 
	 * @throws PopUpException
	 *             , NoDataException, InterruptedException
	 */
/*	@Test
	public void summaryCells_GSMCFR171() throws PopUpException, NoDataException, InterruptedException {
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(NetworkType.CONTROLLER,
				SubStartMenu.NETWORK_GSM_EVENT_ANALYSIS_SUMMARY, true, GSMConstants.CONTROLLER_NAME_LONG_FORMAT_ALTERNATIVE);

		controllerNetworkGSMCallFailure.clickTableCell(0, GuiStringConstants.IMPACTED_CELLS);

		assertTrue(
				GuiStringConstants.ERROR_LOADING + GSMConstants.SUMMARY_CELL_GSMCFR_171_WINDOW_TITLE,
				controllerNetworkGSMCallFailure.getWindowHeaderLabel().equals(
						GSMConstants.SUMMARY_CELL_GSMCFR_171_WINDOW_TITLE));
		
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerNetworkGSMCallFailure.getTableHeaders()
				.containsAll(accessareaSummaryEventAnalysisNetwork));

		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH,
				controllerNetworkGSMCallFailure.getTimeRange().equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));

		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,
				dataIntegrValidatorObjCS.validateDrillDownAllTimeRanges(controllerNetworkGSMCallFailure,
						GuiStringConstants.ACCESS_AREA, GSMConstants.ACCESS_AREA_NAME_SHORT_FORMAT_ALTERNATIVE));
	}

  
  */ 
 
 
	public void checkSubCauseCodeAnalysis(CommonWindow commonWindow, TimeRange timerange,
			List<String> SubCauseCodeNetworkSubMenus) throws PopUpException, NoDataException, InterruptedException {
		commonWindow.setTimeRange(timerange);

		// checkSubCauseCodeAnalysis(commonWindow, timeRange[t], commonWindow,
		// SubCauseCodeNetworkSubMenus);

		// Controller Sub Cause Code Analysis below this point
		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				commonWindow.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				commonWindow.getTableHeaders().containsAll(SubCauseCodeNetworkSubMenus));
		final int failures = Integer.parseInt(commonWindow.getTableData(0, 2));
		final int impactedSubscribers = Integer.parseInt(commonWindow.getTableData(0, 3));
		assertTrue(FailureReasonStringConstants.IMPACTED_SUBSRIBERS_GREATER_THAN_NO_OF_FAILURES,
				impactedSubscribers <= failures);
	}

	public void checkDetailedEventAnalysis(CommonWindow commonWindow, TimeRange timerange,
			List<String> DetailedEventAnalysisNetworkSubMenus) throws PopUpException, NoDataException,
			InterruptedException {
		commonWindow.setTimeRange(timerange);
		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				commonWindow.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				commonWindow.getTableHeaders().containsAll(DetailedEventAnalysisNetworkSubMenus));
	}

	public void checkAllTimeRangesForIndividualNodes(NetworkType type, CommonWindow commonWindow,
			List<String> SubCauseCodeNetworkSubMenus, List<String> DetailedEventAnalysisNetworkSubMenus)
			throws PopUpException, NoDataException, InterruptedException {

		TimeRange[] timeRange = getAllTimeRanges();

		for (int t = 1; t < timeRange.length; t++) {
			checkSubCauseCodeAnalysis(commonWindow, timeRange[t], SubCauseCodeNetworkSubMenus);
		}

		commonWindow.clickTableCell(0, GuiStringConstants.FAILURES);

		for (int t = 1; t < timeRange.length; t++) {
			checkDetailedEventAnalysis(commonWindow, timeRange[t], DetailedEventAnalysisNetworkSubMenus);
		}
	}

	/*
	 * On a drill down (on failures) view goes through all time ranges and
	 * checks if the data integrity is ok.
	 */
	private boolean validateDrillDownOnFailuresAllTimeRanges(CommonWindow commonWindow, String rankingType,
			String valueToSearchBy, List<String> headers) throws PopUpException, NoDataException {

		// will contain all the failures for the designated BSC, Access Area
		// etc. given in valueToSearchBy
		List<Map<String, String>> failuresForDesignatedOne;
		List<Map<String, String>> failuresInTable;
		Calendar currentCalDateTime;
		Calendar earliestCalDateTime; // this will be used as the starting time
										// for events which we query for
		DateFormat eventTimeDateFormat = new SimpleDateFormat(GSMConstants.DATEFORMAT_STRING_WITHOUT_MSEC);
		int eventTimeLatencyAdjust = 0; // needed because of the latency when
										// querying the different timeranges

		// there is no "Event Time" column in the reserve data
		if (headers.contains(GuiStringConstants.EVENT_TIME)) {
			headers.remove(GuiStringConstants.EVENT_TIME);
		}

		// there is no valid "MSISDN" information in the reserve data
		if (headers.contains(GuiStringConstants.MSISDN)) {
			headers.remove(GuiStringConstants.MSISDN);
		}

		failuresForDesignatedOne = dataIntegrValidatorObjCS.getFailures(headers, rankingType, valueToSearchBy);

		TimeRange[] timeRanges = getAllTimeRanges();

		for (TimeRange timeRange : timeRanges) {
			commonWindow.setTimeRange(timeRange);
			// have to query the current time each time range, as some checks
			// could take more than a minute
			failuresInTable = commonWindow.getAllTableData();
			List<Map<String, String>> failuresInTableReduced = new ArrayList<Map<String, String>>();
			Map<String, String> rowData = new HashMap<String, String>();
			// columns which we want to get from the grid window
			for (Map<String, String> failure : failuresInTable) {
				rowData.put(GuiStringConstants.EVENT_TIME, failure.get(GuiStringConstants.EVENT_TIME));
				for (String header : headers) {
					rowData.put(header, failure.get(header));
				}
			}
			failuresInTableReduced.add(rowData);
			currentCalDateTime = Calendar.getInstance();
			currentCalDateTime.clear(Calendar.SECOND);
			currentCalDateTime.clear(Calendar.MILLISECOND);
			earliestCalDateTime = (Calendar) currentCalDateTime.clone();

			// there is some latency for the time ranges
			eventTimeLatencyAdjust = calculateDateAdjustWithLatencyInMinutes(timeRange);
			// Events before this time, shouldn't be in the table
			/*
			 * TODO: maybe the current datetime should be subtracted with the
			 * latency as well, this doesn't mean the eventTimeLatencyAdjust,
			 * just the latency without the minutes of the time range
			 */
			earliestCalDateTime.add(Calendar.MINUTE, -eventTimeLatencyAdjust);

			if (!dataIntegrValidatorObjCS.isDrillDownOnFailuresDataValid(headers, failuresInTableReduced,
					failuresForDesignatedOne, currentCalDateTime, earliestCalDateTime, eventTimeDateFormat, timeRange))
				return false;
		}

		return true;
	}

	public void checkAllTimeRangesForGroups(String[] gsmConstant, CommonWindow commonWindow, int columnNumber)
			throws PopUpException, NoDataException, InterruptedException {
		TimeRange[] timeRange = getAllTimeRanges();

		for (int t = 0; t < timeRange.length; t++) {
			groupTypeMembershipCheck(timeRange[t], gsmConstant, commonWindow, columnNumber);
		}
	}

	public void groupTypeMembershipCheck(TimeRange timeRange, String[] gsmConstant, CommonWindow commonWindow,
			int columnNumber) throws PopUpException, NoDataException, InterruptedException {
		String[] allNetworkNodeNames = populateSet(timeRange, commonWindow, columnNumber);
		int counter = 0;
		for (int i = 0; i < gsmConstant.length; i++) {
			for (int j = 0; j < allNetworkNodeNames.length; j++) {
				if (allNetworkNodeNames[j].equals(gsmConstant[i])) {
					counter++;
				}
			}
		}
		assertTrue("Contains elements not in array", counter == allNetworkNodeNames.length);
	}

	public void validateNetworkAllTimeRanges() throws PopUpException, NoDataException, InterruptedException {
		TimeRange[] timeRanges = getAllTimeRanges();
	}

	/*
	 * Returns all time ranges.
	 */
	private TimeRange[] getAllTimeRanges() {
		return TimeRange.values();
	}

	public String[] populateSet(TimeRange timeRange, CommonWindow commonWindow, int columnNumber)
			throws PopUpException, NoDataException, InterruptedException {
		commonWindow.setTimeRange(timeRange);
		Set allNodeNames = new HashSet();
		for (int recordNo = 0; recordNo < GSMConstants.DEFAULT_MAX_TABLE_ROWS; recordNo++) {
			final String nodeName = commonWindow.getTableData(recordNo, columnNumber);
			allNodeNames.add(nodeName);
		}
		// remove any blanks
		allNodeNames.remove("");
		// Put set into an array
		String[] array = (String[]) allNodeNames.toArray(new String[allNodeNames.size()]);
		return array;
	}// end populateSet function

	/*
	 * Calculates how many minutes should the starting date be set backward and
	 * adds also the latency according to the time range and the date given as a
	 * parameter. Latencies currently: 5min : 0, 15m, 30m, 1h, 2h: 7m; 6h, 12h,
	 * 1d: 45m>x>=30m, rounded down to the nearest quarter; 1w: rounded down to
	 * 00:00
	 */
	private int calculateDateAdjustWithLatencyInMinutes(TimeRange timeRange) {
		int latency = 0;
		int timeRangeInMinutes = timeRange.getMiniutes();
		// creating a new Calendar instance isn't a problem, since this method
		// is immediately called after creating
		// the ones in the validate... method
		Calendar tempCalendar = Calendar.getInstance();

		if (timeRangeInMinutes == 5)
			latency = 5;
		else if (timeRangeInMinutes < 360) // less, than six hours
			latency = timeRangeInMinutes + GSMConstants.LATENCY_FOR_LESS_THAN_6_HOURS;
		else if (timeRangeInMinutes < 10080) { // more, than six hours, but
												// less, than one week
			// first subtract the time according to the time range...
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			// and then calculate how much we should subtract to get the
			// "quarter before"
			latency = timeRangeInMinutes + (tempCalendar.get(Calendar.MINUTE) % 15);
		} else { // one week
			tempCalendar.add(Calendar.MINUTE, -timeRangeInMinutes);
			latency = timeRangeInMinutes + tempCalendar.get(Calendar.MINUTE)
					+ ((tempCalendar.get(Calendar.HOUR_OF_DAY)) * 60);
		}

		return latency;
	}

}
