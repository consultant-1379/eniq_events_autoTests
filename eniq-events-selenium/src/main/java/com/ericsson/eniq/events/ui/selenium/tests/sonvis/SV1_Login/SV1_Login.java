package com.ericsson.eniq.events.ui.selenium.tests.sonvis.SV1_Login;

import com.ericsson.eniq.events.ui.selenium.common.constants.FailureReasonStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.tests.gsm.common.GSMConstants;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonVisConstants;
import com.ericsson.eniq.events.ui.selenium.tests.sonvis.SonvisUIBaseTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.SonvisTabWebDriver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

//import com.ericsson.eniq.events.ui.selenium.events.elements.SonVisTableController;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) ${year} LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public class SV1_Login  extends SonvisUIBaseTest {

   @Autowired
   protected SonvisTabWebDriver sonvisTabWebDriver;
   
   //@Autowired
   //protected RankingsTab rankingTab;
   
	@Autowired
	@Qualifier("rankPanelGridSonVis")
	private CommonWindow rankPanelGridSonVisualisation;
	
	@Autowired
	@Qualifier("cellDetailsPanelGridSonVis")
	private CommonWindow cellDetailsGridSonVisualisation;
	

    //public String id = ("RANKING_PANEL_GRID_SON_VIS");

	private SonVisDataIntegrityValidator sonVisDataIntegrity = new SonVisDataIntegrityValidator();
	protected List<Map<String, String>> reserveDataList = sonVisDataIntegrity.reserveDataList;
	
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
		sonvisTabWebDriver.openTab();

		sonVisDataIntegrity.init(SonVisConstants.SON_VIS_RESERVED_DATA_FILENAME,
				SonVisConstants.SON_VIS_RESERVED_DATA_COLUMNS);
	}
	

   @Test
   public void SV_1_1_login() throws InterruptedException, PopUpException {
      sonvisTabWebDriver.openTab();



      
      Thread.sleep(30000);

      
   }


   
   
   @Test
   public void SV_Ranking_Table_Top_100_Cells_ANR_X2_Relations_Added() throws InterruptedException, PopUpException, NoDataException {
	   SV_Ranking_Table_Top_100_Cells(SonVisConstants.ANR_X2_RELATIONS_ADDED, SonVisConstants.SON_VIS_RANKING_TOP_100_CELLS_COLUMNS, rankPanelGridSonVisualisation);
   }
   
  
   @Test
   public void SV_Ranking_Table_Top_100_Cells_ANR_X2_Relations_Removed() throws InterruptedException, PopUpException, NoDataException {
	   SV_Ranking_Table_Top_100_Cells(SonVisConstants.ANR_X2_RELATIONS_REMOVED, SonVisConstants.SON_VIS_RANKING_TOP_100_CELLS_COLUMNS, rankPanelGridSonVisualisation);
   }
   
   @Test
   public void SV_Ranking_Table_Top_100_Cells_TOTAL_PCI_CONFLICTS() throws InterruptedException, PopUpException, NoDataException {
	   SV_Ranking_Table_Top_100_Cells(SonVisConstants.TOTAL_PCI_CONFLICTS, SonVisConstants.SON_VIS_RANKING_TOP_100_CELLS_COLUMNS, rankPanelGridSonVisualisation);
   }
   
  
   @Test
   public void SV_Ranking_Table_Top_100_Cells_PCI_Changes() throws InterruptedException, PopUpException, NoDataException {
	   SV_Ranking_Table_Top_100_Cells(SonVisConstants.PCI_CHANGES, SonVisConstants.SON_VIS_RANKING_TOP_100_CELLS_COLUMNS, rankPanelGridSonVisualisation);
   }
  
   
   @Test
   	public void SV_1_2_Cell_Details_View_215() throws InterruptedException, PopUpException, NoDataException {
		  assertTrue("SON Visualization tab not found", selenium.isTextPresent(SonVisConstants.SON_VISUALIZATION));
	      selenium.click(SonVisConstants.RANKING_TABLE_BUTTON);
	      selenium.select("//div[@id='rankingMenu']//select", "Total PCI Conflicts");
	      selenium.select(SonVisConstants.RANK_TIME_SELECTOR_DROP_DOWN_BOX, SonVisConstants.TODAY);	      
	      Thread.sleep(5000);
	      selenium.click("//div[@id='RANKING_PANEL_GRID_SON_VIS']//div[@class='x-grid3-cell-inner x-grid3-col-cellname'][text()='ERBS39-2']");
	      Thread.sleep(5000);
	      selenium.click("//div[@id='RANKING_PANEL_GRID_SON_VIS']//div[@class='x-grid3-cell-inner x-grid3-col-pciConflicts']");
	      Thread.sleep(10000);
	      selenium.select(SonVisConstants.CELL_DETAILS_DROP_DOWN_BOX, SonVisConstants.TOTAL_PCI_CONFLICTS);
	      Thread.sleep(10000);
	      System.out.println("Cell Details " + cellDetailsGridSonVisualisation.getAllTableData());
	      
	      //-------------------------------------------------------------------------------------
	      
			assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, cellDetailsGridSonVisualisation.getTableHeaders()
					.containsAll(SonVisConstants.SON_VIS_CELL_DETAILS_DATA_COLUMNS));
			      
			assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,validateGridRowData(cellDetailsGridSonVisualisation.getAllTableData(),SonVisConstants.SON_VIS_CELL_DETAILS_DATA_COLUMNS));
	      
	      
	      //List<Map<String, String>> allWindowData = new ArrayList<Map<String,String>>();
	      // allWindowData = cellDetailsGridSonVisualisation.getAllTableData();
	      //List<String> headers = new ArrayList<String>();
	      //headers = SonVisConstants.SON_VIS_CELL_DETAILS_DATA_COLUMNS;
	      //assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,validateGridRowData(allWindowData, headers));	
   	}
   
   
   @Test
  	public void SV_1_2_Cell_Relations_Filter_209() throws InterruptedException, PopUpException, NoDataException {
	 selenium.click("//div[@id='SONVIS_TAB']//div//table//span[1][text()='Relations Filter']");
	 Thread.sleep(5000);
	 selenium.click("//input[@id='gwt-uid-4']");

	      
  	}
   
   
	public boolean validateGridRowData(List<Map<String, String>> allGuiData, List<String> headers) {
		String reserved = "";
		String gui = "";
		int counter = 0;
		for (Map<String, String> rData : reserveDataList){
			for (Map<String, String> gData : allGuiData){
				if (rData.get(SonVisConstants.CELL_NAME).equals(gData.get(SonVisConstants.CELL_NAME))) {						
				counter ++;	
				System.out.println("Counter = " + counter);
				System.out.println("Cell Name = " + gData.get(SonVisConstants.CELL_NAME));
					//if we end up in here then we have a row that matches and should check all other headers
					for (String header : headers){
						reserved = rData.get(header);
						gui = gData.get(header);
						
						if (!(reserved == null)){
							reserved = rData.get(header);
						}else{
							reserved = "";
						}
						if (!(gui == null)){
							gui = gData.get(header);
						}else{
							gui = "";
						}
						if (!(reserved.equals(gui))){
							printErrorReason("validateGridRowData", header,  reserved, gui);
							return false;
						}
					}
				}
			}
		}	
		
		//This check has been introduced to make sure each row of the GUI data has been checked
		//if not there will be a mismatch between our counter variable and the GUI data list size.
		if (counter != allGuiData.size()){
			System.out.println("Not equals -- C: " + counter + " GUI: " +  allGuiData.size());
			return false;
		}else{
			return true;
		}
	}
	
	
	protected void printErrorReason(String calledFrom, String header, String expected, String found) {
		System.out.println(" *** Data Integrity Failed ***");
		System.out.println("Header:\t\t" + header);
		System.out.println("Expected Reserved:\t " + expected);
		System.out.println("Found in GUI:\t\t" + found);
		System.out.println("Called from Method :\t" + calledFrom);
	}

	public void SV_Ranking_Table_Top_100_Cells(String rankType, List<String> columns, CommonWindow commonWindow) throws InterruptedException, PopUpException, NoDataException {
		assertTrue("SON Visualization tab not found", selenium.isTextPresent(SonVisConstants.SON_VISUALIZATION));
		Thread.sleep(5000);
		selenium.click(SonVisConstants.RANKING_TABLE_BUTTON);
		   
		selenium.select(SonVisConstants.RANK_TIME_SELECTOR_DROP_DOWN_BOX, SonVisConstants.TODAY);
		
		selenium.select("//div[@id='rankingMenu']//select", rankType);
				
		Thread.sleep(5000);
		selenium.waitForPageLoadingToComplete();

		assertTrue(FailureReasonStringConstants.HEADER_MISMATCH, commonWindow.getTableHeaders()
				.containsAll(columns));
		      
		assertTrue(FailureReasonStringConstants.DATA_INTEGRITY_CHECK_FAILED,validateGridRowData(commonWindow.getAllTableData(),columns));
	   }

}
