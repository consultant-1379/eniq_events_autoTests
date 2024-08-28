/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiSpecificDataType;
import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.db.DBPersistor;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.elements.SortType;
import com.ericsson.eniq.events.ui.selenium.events.elements.TimeRange;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.EniqEventsUIBaseSeleniumTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;


/**
 * @author evinpra
 * @since 2011
 *
 */
public class TopologyTestGroup extends EniqEventsUIBaseSeleniumTest {
    @Autowired
    protected SubscriberTab subscriberTab;

    @Autowired
    @Qualifier("subscriberEventAnalysisForMSS")
    private CommonWindow subscriberEventAnalysis;

    final static DBPersistor dbPersistor = DBPersistor.getInstatnce();

    final List<String> msgTypeIndAndsmsResultHeaders = new ArrayList<String>(Arrays.asList(GuiStringConstants.MSG_TYPE_IND, 
    		GuiStringConstants.MSG_TYPE_IND_ID, GuiStringConstants.SMS_RESULT_ID));
    
    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of SubscriberSession test section");
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of SubscriberSession test section");
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();

        pause(2000);
        selenium.waitForElementToBePresent("//table[@id='selenium_tag_MetaDataChangeComponent']","50000");
        if (selenium.isElementPresent("//table[@id='selenium_tag_MetaDataChangeComponent']")) {
            selenium.click("//table[@id='selenium_tag_MetaDataChangeComponent']");
            selenium.waitForElementToBePresent("//a[@id='selenium_tag_Core - Voice']", "50000");
            if (selenium.isElementPresent("//a[@id='selenium_tag_Core - Voice']")) {
                selenium.click("//a[@id='selenium_tag_Core - Voice']");
            }else{
                logger.log(Level.SEVERE, "Element //a[@id='selenium_tag_Core - Voice'] is not present");
            }
        }else{
            logger.log(Level.SEVERE, "Element //table[@id='selenium_tag_MetaDataChangeComponent'] is not present");
        }
        pause(2000);
    }

    /*
     * This Test is not as part of VS Testcases. 
     * 
     * This test is to update the necessary topology values
     * on the MSSReservedData.csv and reservedData.csv and to make sure it is done
     * properly.
     */
    @Test
    public void updateTopologyChanges()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	/////
    	//final String queryForTwoG = "select top 1  RAT,MCC,MNC,LAC,ACCESS_AREA_ID,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_SGEH_HIER321 t where RAT =0 and ACCESS_AREA_ID !=0";
		final String queryForTwoG = "select top 1  RAT,MCC,MNC,LAC,ACCESS_AREA_ID,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_SGEH_HIER321 t where RAT =0 and ACCESS_AREA_ID =2 order by hierarchy_3";
    	final String twoGAndThreeGArguments = "RAT,MCC,MNC,LAC,ACCESS_AREA_ID,HIERARCHY_1,HIERARCHY_3,VENDOR";
    	final HashMap<String, String> twoGDataFromDimESgehHier321 = DBconnector.excuteQueriesForTopologyChanges(twoGAndThreeGArguments, queryForTwoG);
    	/////
    	final String queryFor2GCountryAndOperator = "select COUNTRY,OPERATOR FROM DIM_E_SGEH_MCCMNC where MCC='"+ twoGDataFromDimESgehHier321.get("MCC") +"'and MNC ='"+ twoGDataFromDimESgehHier321.get("MNC") +"'";
    	final String twoGAndThreeGCountryAndOperator = "COUNTRY,OPERATOR";
    	final HashMap<String, String> CountryAndOperatorFor2G = DBconnector.excuteQueriesForTopologyChanges(twoGAndThreeGCountryAndOperator, queryFor2GCountryAndOperator);
    	
    	//final String queryForThreeG = "select top 1 RAT,MCC,MNC,LAC,ACCESS_AREA_ID,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_SGEH_HIER321 t where RAT =1 and ACCESS_AREA_ID !=0";
		final String queryForThreeG = "select top 1 RAT,MCC,MNC,LAC,ACCESS_AREA_ID,HIERARCHY_1,HIERARCHY_3,VENDOR from dc.DIM_E_SGEH_HIER321 t where RAT =1 and ACCESS_AREA_ID =1 order by hierarchy_3";	
    	//////
    	final HashMap<String, String> threeGDataFromDimESgehHier321 = DBconnector.excuteQueriesForTopologyChanges(twoGAndThreeGArguments, queryForThreeG);
    	
    	final String queryFor3GCountryAndOperator = "select COUNTRY,OPERATOR FROM DIM_E_SGEH_MCCMNC where MCC='"+ threeGDataFromDimESgehHier321.get("MCC") +"'and MNC ='"+ threeGDataFromDimESgehHier321.get("MNC") +"'";
    	final HashMap<String, String> CountryAndOperatorFor3G = DBconnector.excuteQueriesForTopologyChanges(twoGAndThreeGCountryAndOperator, queryFor3GCountryAndOperator);
    	/////
    	final String queryForTac = "select top 1 TAC, MARKETING_NAME, MANUFACTURER from DIM_E_SGEH_TAC  where TAC>40000000 order by tac";
    	final String tacArguments = "TAC,MARKETING_NAME,MANUFACTURER";
    	final HashMap<String, String> tacInformation = DBconnector.excuteQueriesForTopologyChanges(tacArguments, queryForTac);
    	/////
    	
    	final String queryForFaultCode = "select distinct FAULT_CODE,FAULT_CODE_DESC,ADVICE from DIM_E_MSS_FAULT_CODE t where rowid(t) in (3)";
    	final String faultCodeArguments = "FAULT_CODE,FAULT_CODE_DESC,ADVICE";
    	final HashMap<String, String> faultCodeInformation = DBconnector.excuteQueriesForTopologyChanges(faultCodeArguments, queryForFaultCode);
    	
    	/////
    	
    	final String queryForTeleServiceCode = "select distinct TELE_SERVICE_CODE,TELE_SERVICE_CODE_DESC from DIM_E_MSS_TELE_SERVICE_CODE t where rowid(t) in (3)";
    	final String teleServiceCodeArguments = "TELE_SERVICE_CODE,TELE_SERVICE_CODE_DESC";
    	final HashMap<String, String> teleServiceCodeInformation = DBconnector.excuteQueriesForTopologyChanges(teleServiceCodeArguments, queryForTeleServiceCode);
    	
    	/////
    	
    	final String queryForBearerServiceCode = "select BEARER_SERVICE_CODE,BEARER_SERVICE_CODE_DESC from DIM_E_MSS_BEARER_SERVICE_CODE t where rowid(t) in (3)";
    	final String bearierServiceCodeArguments = "BEARER_SERVICE_CODE,BEARER_SERVICE_CODE_DESC";
    	final HashMap<String, String> bearerServiceInformation = DBconnector.excuteQueriesForTopologyChanges(bearierServiceCodeArguments, queryForBearerServiceCode);
    	
    	/////
		logger.log(Level.INFO, "The twoGDataFromDimESgehHier321 :" + twoGDataFromDimESgehHier321);
		logger.log(Level.INFO, "The threeGDataFromDimESgehHier321 :" + threeGDataFromDimESgehHier321);
		logger.log(Level.INFO, "The tacInformation :" + tacInformation);
		logger.log(Level.INFO, "The faultCodeInformation :" + faultCodeInformation);
		logger.log(Level.INFO, "The teleServiceCodeInformation :" + teleServiceCodeInformation);
		logger.log(Level.INFO, "The bearerServiceInformation :" + bearerServiceInformation);
		
    	final List<List<String>> headersList  = GeneralUtil.getCSVDataHeaders();
    	final List<List<String>> twoGDataList   = GeneralUtil.updatingTopologyValues(twoGDataFromDimESgehHier321, CountryAndOperatorFor2G, tacInformation, faultCodeInformation, teleServiceCodeInformation,bearerServiceInformation,"460000123456790","460000987654322");
    	final List<List<String>> threeGDataList = GeneralUtil.updatingTopologyValues(threeGDataFromDimESgehHier321, CountryAndOperatorFor2G, tacInformation, faultCodeInformation, teleServiceCodeInformation,bearerServiceInformation,"460000123456791","460000987654323");
    	GeneralUtil.writingTopologyValuesOnCSVFile(headersList, twoGDataList, threeGDataList);				
    	GeneralUtil.getReservedDataCSVDataValues(twoGDataFromDimESgehHier321, threeGDataFromDimESgehHier321, tacInformation);
    	dbPersistor.closeConnection();
    }

    //
    // 4.2.1 - To verify topology Access Area and Controller handling for MSC event having the event from Ericsson 2G network - MSOriginating Event.
    //
    @Test
    public void TopologyAccessAreaAndControllerHandlingForMSCEventHavingEventFromEricsson2GNetwork_2_1()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.ACCESS_AREA, GuiStringConstants.CONTROLLER);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATING));
        
    }
    
    //
    // 4.2.2 - To verify topology Service Area Code handling for MSC event having the event from Ericsson 3G network - MSOriginating Event.
    //
    @Test
    public void TopologyAccessAreaAndControllerHandlingForMSCEventHavingEventFromEricsson3GNetwork_2_2()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_3G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.ACCESS_AREA, GuiStringConstants.CONTROLLER);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATING));        
    
    }
    
    //
    // 4.2.3 - To verify topology Controller handling for MSC event having the event from Non-Ericsson 2G network - MSOriginating Event.
    //
    @Test
    public void TopologyAccessAreaAndControllerHandlingForMSCEventHavingEventFromNonEricsson2GNetwork_2_3()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.ACCESS_AREA, GuiStringConstants.CONTROLLER);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATING));        
    }
    
    //
    // 4.2.4 - To verify topology Controller handling for MSC event having the event from Non-Ericsson 3G network - MSOriginating Event.
    //
    @Test
    public void TopologyAccessAreaAndControllerHandlingForMSCEventHavingEventFromNonEricsson3GNetwork_2_4()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_3G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.ACCESS_AREA, GuiStringConstants.CONTROLLER);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATING));
    }   
   
    //
    // 4.2.5 - Topology messageTypeIndicator and smsResult handling for MSC event --- mSOriginatingSMSinMSC Event.
    //
    @Test
    public void TopologyMsgTypeIndAndsmsResultHandlingForMSoriginatingSMSinMSCEvent_2_5()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_3G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(msgTypeIndAndsmsResultHeaders, GuiStringConstants.EVENT_TYPE);
        waitForPageLoadingToComplete();
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue( GuiStringConstants.MSORIGINATINGSMSINMSC + DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.validateSMSEvent(dataDisplayedOnUI));
    }
    
    //
    // 4.2.6 - Topology messageTypeIndicator and smsResult handling for MSC event --- mSTerminatingSMSinMSC Event.
    //
    @Test
    public void TopologyMsgTypeIndAndsmsResultHandlingForMSterminatingSMSinMSCEvent_2_6()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_3G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSTERMINATINGSMSINMSC);
        waitForPageLoadingToComplete();
        subscriberEventAnalysis.openTableHeaderMenu(0);
        subscriberEventAnalysis.checkInOptionalHeaderCheckBoxes(msgTypeIndAndsmsResultHeaders, GuiStringConstants.EVENT_TYPE);
        waitForPageLoadingToComplete();
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllTableData();
        assertTrue(GuiStringConstants.MSTERMINATINGSMSINMSC + DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.validateSMSEvent(dataDisplayedOnUI));
    }
    
    //
    // 4.2.7 - General topology information handling
    //
    @Test
    public void GeneralTopologyInformationHandling_2_7() throws InterruptedException, PopUpException, NoDataException,
            FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.MSORIGINATING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.MSC, GuiStringConstants.FAULT_CODE, 
        		GuiStringConstants.INTERNAL_CAUSE_CODE, GuiStringConstants.TERMINAL_MAKE, GuiStringConstants.TERMINAL_MODEL);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.MSORIGINATING));
    }

    //
    // 4.2.8 - Topology 2 digits of MNC and MCC extracted from IMSI for MSC event --- callForwarding Event
    //      
    @Test
    public void Topology2digitsofMNCandMCCextractedfromIMSIforMSCeventCallForw_2_8()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.CALLFORWARDING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.CALLFORWARDING));
    }
    
    //
    // 4.2.9 - Topology 3 digits of MNC and MCC extracted from IMSI for MSC event --- callForwarding Event
    //
    @Test
    public void Topology3digitsofMNCandMCCextractedfromIMSIforMSCeventCallForw_2_9()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        waitForPageLoadingToComplete();
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.CALLFORWARDING);        
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.CALLFORWARDING));
    }
    
    //
    // 4.2.10 - Topology 2 digits of MNC and MCC extracted from IMSI for MSC event --- roamingCallForwarding Event
    //
    @Test
    public void Topology2digitsofMNCandMCCextractedfromIMSIforMSCeventRoamingCallForw_2_10()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.ROAMINGCALLFORWARDING);
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.ROAMINGCALLFORWARDING));
    }
    
    //
    // 4.2.11 - Topology 3 digits of MNC and MCC extracted from IMSI for MSC event --- roamingCallForwarding Event
    //
    @Test
    public void Topology3digitsofMNCandMCCextractedfromIMSIforMSCeventRoamingCallForw_2_11()
            throws InterruptedException, PopUpException, NoDataException, FileNotFoundException, IOException {
    	final String imsi = reservedDataHelper.getImsiSpecificData(ImsiNumber.IMSI_2G_MSS, ImsiSpecificDataType.IMSI);
        subscriberTab.openEventAnalysisWindowForMSS(ImsiMenu.IMSI, true, imsi);
        TimeRange timeRange = getTimeRangeFromProperties();
        subscriberEventAnalysis.setTimeRange(timeRange);
        waitForPageLoadingToComplete();
        drillDownFailuresOnEventAnalysisWindow(subscriberEventAnalysis, GuiStringConstants.EVENT_TYPE, GuiStringConstants.ROAMINGCALLFORWARDING);        
        waitForPageLoadingToComplete();
        final List<String> headersToBeValidated = Arrays.asList(GuiStringConstants.MCC, GuiStringConstants.MNC);
        final List<Map<String,String>> dataDisplayedOnUI = subscriberEventAnalysis.getAllPagesData();
        assertTrue( DataIntegrityConstants.AGGREGRATION_ERROR_MESSAGE + imsi, AggregrationHandlerUtil.getAggregrationResult(dataDisplayedOnUI, headersToBeValidated, 
        		GuiStringConstants.IMSI, imsi, GuiStringConstants.ROAMINGCALLFORWARDING));
        
    }   
    
    /////////////////////////////////////////////////////////////////////////////
    //   P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////
    
    private void drillDownFailuresOnEventAnalysisWindow(final CommonWindow window, final String columnToCheck,
            final String... values) throws NoDataException, PopUpException {
        window.sortTable(SortType.DESCENDING, "Failures");
        final int row = window.findFirstTableRowWhereMatchingAnyValue(columnToCheck, values);
        window.clickTableCell(row, "Failures");
        waitForPageLoadingToComplete();
        assertTrue("Can't open Failed Event Analysis page", selenium.isTextPresent("Event Analysis"));
    }

}
