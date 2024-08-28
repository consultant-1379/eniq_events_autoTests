package com.ericsson.eniq.events.ui.selenium.tests.twogthreeg.sgeh;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelper.ImsiNumber;
import com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.events.windows.CommonWindow;
import com.ericsson.eniq.events.ui.selenium.events.windows.SelectedButtonType;
import com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common.SBWebDriverBaseUnitTest;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.Workspace;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *  
 * @author ejjsjjn November 2012
 * 
 */

public class SubscriberWebDriver extends SBWebDriverBaseUnitTest {

    @Autowired
    @Qualifier("networkEventAnalysis")
    private CommonWindow controllerEventAnalysis;

	@Autowired
	private Workspace workspace;

	public static WebElement element;

	@Test
	public void IMSIEventControllerDrillDownNewLaunchMenu_4_5_7() throws Exception {
	workspace.selectTimeRange("//div[@class='popupContent']//div/div[@__idx='7']");
	workspace.selectDimension(SeleniumConstants.CONTROLLER);
	workspace.enterDimensionValue("RNC06");
	workspace.selectWindowType("Network", "Core PS");
	workspace.clickLaunchButton();
	
	final List<String> expectedHeaders = new ArrayList<String>(Arrays.asList("RAN Vendor", "Controller",
            "Event Type", "Failures", "Successes", "Total Events", "Success Ratio", "Impacted Subscribers"));
	
    final List<String> expectedHeadersOnSelectedEventTypeOfFailure = new ArrayList<String>(Arrays.asList(
            "Event Time", "IMSI", "TAC", "Terminal Make", "Terminal Model", "Event Type", "Event Result",
            "Cause Code", "Sub Cause Code", "SGSN-MME", "Controller", "Access Area", "RAN Vendor", "APN"));
	
    Thread.sleep(1000);
    assertTrue("Headers contains expected entries: ", controllerEventAnalysis.getTableHeaders().containsAll(
            expectedHeaders));

    validateByClickingEachFailurelinkInTableRow(expectedHeadersOnSelectedEventTypeOfFailure,
            controllerEventAnalysis, ImsiNumber.IMSI_A);
	}

	//========================================================================================
	//=====                               PRIVATE METHODS                                =====
	//========================================================================================
	
    private void validateByClickingEachFailurelinkInTableRow(final List<String> tableHeadersAtNewlyOpenedTable,
            final CommonWindow subscriberEventAnalysis, final ImsiNumber imsi) throws NoDataException, PopUpException {

        final List<Map<String, String>> allTableData = subscriberEventAnalysis.getAllTableData();
        for (int i = 0; i < 3 ; i++) {//allTableData.size()
            Map<String, String> result = allTableData.get(i);
            //ignore 0 failures
            if (!result.get("Failures").equals("0")) {
                subscriberEventAnalysis.clickTableCell(i, "Failures"); // Failure links
                waitForPageLoadingToComplete();
                assertEquals("Table headers are not matching.\n", tableHeadersAtNewlyOpenedTable,
                        subscriberEventAnalysis.getTableHeaders());
                //check for first row's values
//                final int row = subscriberEventAnalysis.findFirstTableRowWhereMatchingAnyValue(GuiStringConstants.IMSI,
//                        reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI));
//                result = subscriberEventAnalysis.getAllDataAtTableRow(row);
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.IMSI), result
//                        .get(GuiStringConstants.IMSI));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TAC), result
//                        .get(GuiStringConstants.TAC));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MAKE), result
//                        .get(GuiStringConstants.TERMINAL_MAKE));
//                assertEquals(reservedDataHelper.getImsiSpecificData(imsi, ImsiSpecificDataType.TERMINAL_MODEL), result
//                        .get(GuiStringConstants.TERMINAL_MODEL));
//                assertEquals(reservedDataHelper.getCommonReservedData(CommonDataType.SGSN), result
//                        .get(GuiStringConstants.SGSN));
                subscriberEventAnalysis.clickButton(SelectedButtonType.BACK_BUTTON);
                pause(1000);
            }
        }
    }
}

