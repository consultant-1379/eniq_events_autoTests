/**
 * -----------------------------------------------------------------------
t *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa.newui;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.CommonDataType;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.NetworkTab.NetworkType;
import com.ericsson.eniq.events.ui.selenium.events.tabs.newui.NetworkTabUI;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.cfa.DataIntegrityValidator;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

import static com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants.*;


/**
 * @author eprabab
 * @since 2012 - Added for Eniq Events New UI GSM automation.
 * 
 */
public class NetworkTabTestGroupGSM extends EniqEventsUIBaseSeleniumTest 
{
	@Autowired
	private Selection selection;

	@Autowired
	private NetworkTabUI networkTab;

	@Autowired
	@Qualifier("controllerCauseCodeNetworkGSMCFA")
	private CommonWindow controllerCauseCodeNetworkGSMCallFailure;

	@Autowired
	@Qualifier("controllerGroupNetworkGSMCFA")
	private CommonWindow controllerGroupNetworkGSMCallFailure;

	@Autowired
	@Qualifier("controllerCauseCodeNetworkGSMCFA")
	private CommonWindow accessAreaCauseCodeNetworkGSMCallFailure;

	@Autowired
	@Qualifier("roamingAnalysisByCountryNetworkGSMCFA")
	private CommonWindow roamingAnalysisByCountryGSMCallFailure;

	@Autowired
	@Qualifier("roamingAnalysisByOperatorNetworkGSMCFA")
	private CommonWindow roamingAnalysisByOperatorGSMCallFailure;

	private DataIntegrityValidator dataIntegrValidatorObjCS = new DataIntegrityValidator();

	
	/*---------------------------------------------------Start - Automation for Cause Code Analysis For NW Tab-------------------------------------------*/

	@Test
	public void controllerCCACauseCodeDetail363() throws PopUpException, NoDataException, InterruptedException 
	{
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER, "BSCB01", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

		controllerCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(controllerCauseCodeNetworkSubMenus));
		final String allTimeLabel = reservedDataHelper.getCommonReservedData(CommonDataType.TIME_RANGES_GSM);
		TimeRange[] timeRanges; 
		if (!allTimeLabel.isEmpty())
		{
			final String[] timeLabels = allTimeLabel.split(",");
			timeRanges = new TimeRange[timeLabels.length];
			for (int i = 0; i < timeLabels.length; i++)
			{
				timeRanges[i] = getTimeRangeByLabel(timeLabels[i]);
			}
		}
		else
		{
			timeRanges = TimeRange.values();
		}

		for (final TimeRange time : timeRanges) 
		{
			controllerCauseCodeNetworkGSMCallFailure.setTimeRange(time);
			List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
			boolean returnValue = dataIntegrValidatorObjCS.validateCauseCodeAnalysisBSCSummary(uiWindowData, time.getMiniutes());
			assertTrue("Data Validation Failed", returnValue);		
		}
	}


	@Test
	public void controllerCCAExtendedCauseCodeDetail856() throws PopUpException, NoDataException, InterruptedException 
	{

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER, "BSCB01", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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

			List<Map<String, String>> uiWindowData = controllerCauseCodeNetworkGSMCallFailure.getAllTableData();
			boolean returnValue = dataIntegrValidatorObjCS.validateExtendedCauseCodeAnalysisBSCSummary(uiWindowData, time.getMiniutes());
			assertTrue("Data Validation Failed", returnValue);		
		}
	}


	@Test
	public void controllerGroupCCANavigation() throws PopUpException, NoDataException, InterruptedException
	{
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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


	@Test
	public void controllerGroupCCACauseGroup378() throws PopUpException, NoDataException, InterruptedException {

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP,  CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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


	@Test
	public void controllerGroupCCAExtendedCausecode380() throws PopUpException, NoDataException, InterruptedException {

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);


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

	@Test
	public void accessAreaCauseCodeRANGSMCFA914() throws PopUpException, NoDataException, InterruptedException {


		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
		//	SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA, "CB01002,,BSCB01,Ericsson,GSM", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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
	public void accessAreaExtendedCauseCodeCCA957() throws PopUpException, NoDataException, InterruptedException {
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
		//	SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA, "CB01002,,BSCB01,Ericsson,GSM", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);


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

	@Test
	public void accessAreaCauseCodeCCA914() throws PopUpException, NoDataException, InterruptedException {
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
		//	SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, "CB01002,,BSCB01,Ericsson,GSM");
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA, "CB01002,,BSCB01,Ericsson,GSM", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);


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

	/**
	 * Requirement: Test Case: 5.5.4 Description: Verify that, from the Network
	 * Tab, it is possible to summary the cause codes on the basis of the
	 * failures for a nominated Access Area. From this summary view, it shall be
	 * possible to drill down to extended cause on individual cause codes from
	 * which it can drill down on detailed extended cause failure analysis.
	 * 
	 * @throws PopUpException
	 */

	@Test
	public void accessAreaCauseCodeRANGSMCFA_5_4() throws PopUpException, NoDataException, InterruptedException {
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA,
		//SubStartMenu.GSM_NETWORK_CAUSE_CODE_ANALYSIS, true, GSMConstants.ACCESS_AREA_NAME);
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA, "CB01002,,BSCB01,Ericsson,GSM", CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);


		accessAreaCauseCodeNetworkGSMCallFailure.openGridView();
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, accessAreaCauseCodeNetworkGSMCallFailure
				.getTableHeaders().containsAll(accessAreaCauseCodeNetworkSubMenus1));
		assertTrue(GSMConstants.DEFAULT_TIMERANGE_MISMATCH, accessAreaCauseCodeNetworkGSMCallFailure.getTimeRange()
				.equals(GSMConstants.DEFAULT_TIME_RANGE.getLabel()));
		accessAreaCauseCodeNetworkGSMCallFailure.clickTableCell(0, "Cause Group");
		checkAllTimeRangesForIndividualNodes(NetworkType.ACCESS_AREA, accessAreaCauseCodeNetworkGSMCallFailure,
				accessAreaSubCauseCodeNetworkSubMenusNewUi, accessAreaDetailedEventAnalysisNetworkSubMenusNewUI);
	}// 5.4 function end


	@Test
	public void accessAreaGroupCauseCodeRANGSMCFA386() throws PopUpException, NoDataException, InterruptedException {
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
		//SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, GSMConstants.CCA_ACCESSAREA_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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
	public void accessAreaGroupExtendedCauseCodeCCA387() throws PopUpException, NoDataException, InterruptedException {
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
		//SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, GSMConstants.CCA_ACCESSAREA_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);


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
		//networkTab.openRanGSMCFACauseCodeWindowForGSM(NetworkType.ACCESS_AREA_GROUP,
		//SubStartMenu.GSM_NETWORK_DRILL_CAUSE_CODE_ANALYSIS, true, GSMConstants.CCA_ACCESSAREA_GROUP);
		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, GSMConstants.CCA_ACCESSAREA_GROUP, CAUSE_CODE_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFACauseCodeWindowForGSM(selection);

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

	/*---------------------------------------------------End - Cause cause analysis NW Tab-------------------------------------------*/

	/*---------------------------------------------------Start - Automation for Event Analysis For NW Tab-------------------------------------------*/

	@Test
	public void controllerGroupSummaryWindow795() throws PopUpException, NoDataException, InterruptedException {

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.CONTROLLER_GROUP, GSMConstants.CCA_CONTROLLER_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA, "CB01002,,BSCB01,Ericsson,GSM", NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, controllerGroupNetworkGSMCallFailure.getTableHeaders()
				.containsAll(controllerGroupEventSummaryWindowNewUI));

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

	@Test
	public void controllerGroupSummaryWindow798() throws PopUpException, NoDataException, InterruptedException {

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, CCA_ACCESSAREA_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, CCA_ACCESSAREA_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.ACCESS_AREA_GROUP, CCA_ACCESSAREA_GROUP, NETWORK_EVENT_ANALYSIS, CALL_FAILURE);
		networkTab.openRanGSMCFAEventAnalysisWindowForGSM(selection);


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
	/*---------------------------------------------------End Event Analysis For NW Tab-------------------------------------------*/

	/*---------------------------------------------------Start - Automation for Roaming Analysis For NW Tab-------------------------------------------*/
	
	@Test
	public void roamingAnalysisByCountryCSF350() throws Exception 
	{       

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_COUNTRY, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

		selenium.waitForPageLoadingToComplete();

		roamingAnalysisByCountryGSMCallFailure.toggleGraphToGrid();
		pause(GSMConstants.DEFAULT_WAIT_TIME);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByCountryGSMCallFailure.getTableHeaders()
				.containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_COUNTRY_WINDOW_HEADERS));
		//String tac_value = imsiSubscriberGSMCallFailure.getTableData(0, 5);
		/*roamingAnalysisByCountryGSMCallFailure.clickTableCell(0, GuiStringConstants.FAILURES);
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
				.containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_BY_COUNTRY_SUMMARY_WINDOW_HEADERS));*/

	}



	// Since we do not have hyperlink from 2GRadioNetwork->Roaming by Country/Operator -> CallFailure(GSM)-> (Click Grid)
	//Hence commenting the below TCs as they all validate the window further on.
	/*
	public void roamingAnalysisByCountryCSFDataAG350() throws Exception {       

		selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_COUNTRY, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

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
	} */


	@Test
	public void roamingAnalysisByOperatorCSFDataAG764() throws Exception {       

		selectOptions(SeleniumConstants.DATE_TIME_30, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_OPERATOR, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

		selenium.waitForPageLoadingToComplete();

		roamingAnalysisByOperatorGSMCallFailure.toggleGraphToGrid();
		pause(GSMConstants.DEFAULT_WAIT_TIME);

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, roamingAnalysisByOperatorGSMCallFailure.getTableHeaders()
				.containsAll(GSMConstants.DEFAULT_ROAMING_ANALYSIS_INBOUND_OPERATOR_WINDOW_HEADERS));   

		/*for (int index = 0; index < GSMConstants.MAX_NUMBER_ROMAING_OPERATOR; index++) {

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
				.containsAll(GSMConstants.DEFAULT_DETAILED_ROAMING_ANALYSIS_BY_OPERATOR_GSM_CALLSETUP_WINDOW_HEADERS));*/

	} 
	/*
	@Test
	public void roamingAnalysisByCountryCD350() throws Exception {       

		selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_COUNTRY, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_OPERATOR, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

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

		selectOptions(SeleniumConstants.DATE_TIME_Week, SeleniumConstants.RADIO_NETWORK_2G, null, ROAMING_BY_OPERATOR, CALL_FAILURE);
		networkTab.openRoamingAnalysisWindowGSM(selection);

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


	/*---------------------------------------------------End Roaming Analysis For NW Tab-------------------------------------------*/

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

	public void checkDetailedEventAnalysis(CommonWindow commonWindow, TimeRange timerange,
			List<String> DetailedEventAnalysisNetworkSubMenus) throws PopUpException, NoDataException,
			InterruptedException {
		commonWindow.setTimeRange(timerange);
		assertTrue(GSMConstants.MORE_ROWS_THAN + GSMConstants.DEFAULT_MAX_TABLE_ROWS,
				commonWindow.getTableRowCount() <= GSMConstants.DEFAULT_MAX_TABLE_ROWS);
		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH,
				commonWindow.getTableHeaders().containsAll(DetailedEventAnalysisNetworkSubMenus));
	}


	private TimeRange[] getAllTimeRanges() {
		return TimeRange.values();
	}


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

	private void selectOptions(String timeRange,
			String dimension,
			String dimensionValue,
			String windowCategory,
			String windowOption)
	{
		selection.distroy();
		selection.setDimension(dimension);
		selection.setTimeRange(timeRange);
		selection.setDimensionValue(dimensionValue);
		selection.setWindowCategory(windowCategory);
		selection.setWindowOption(windowOption);
		selection.setIsGroup(isGroupDimension(dimension));
	}    

	private boolean isGroupDimension(String dimension)
	{
		return dimension.equals(SeleniumConstants.CONTROLLER_GROUP)||
				dimension.equals(SeleniumConstants.ACCESS_AREA_GROUP)||
				dimension.equals(SeleniumConstants.SGSN_MME_GROUP)||
				dimension.equals(SeleniumConstants.MSC_GROUP)||
				dimension.equals(SeleniumConstants.TERMINAL_GROUP)||
				dimension.equals(SeleniumConstants.IMSI_GROUP)||
				dimension.equals(SeleniumConstants.TRACKING_AREA_Group);
	}
	
	 public void tearDown() throws Exception 
	 {
    	 String closeButtonXPath =  "//div[contains(@class, 'x-nodrag x-tool-close x-tool')]";
    	 selenium.click(closeButtonXPath);
    	 super.tearDown();
	 }
}