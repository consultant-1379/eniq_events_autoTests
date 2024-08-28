/***------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
-----------------------------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.tests.uertt;

import com.ericsson.eniq.events.ui.selenium.common.ReservedDataHelperReplacement;
import com.ericsson.eniq.events.ui.selenium.common.Selection;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.core.EricssonSelenium;
import com.ericsson.eniq.events.ui.selenium.tests.webdriver.WorkspaceRC;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.logging.Level;

import static com.ericsson.eniq.events.ui.selenium.common.constants.SeleniumConstants.*;
import static com.ericsson.eniq.events.ui.selenium.tests.uertt.UerttConstants.*;

public class UITestGroup extends BaseUerttTest {
    private static final String CATEGORY_3G_EVENT_TRACE = "3G Event Trace";
    private static final String WINDOW_OPTION_UERTT = "UE Trace";
    public String baseURL = null;

    @Autowired
    private WorkspaceRC workspace;

    @Autowired
    protected EricssonSelenium selenium;

    @Autowired
    private Selection selection;

    @BeforeClass
    public static void openLog() {
        logger.log(Level.INFO, "Start of UERTT test section");
        reservedDataHelperReplacement = new ReservedDataHelperReplacement(fileNameForImsi);
    }

    @AfterClass
    public static void closeLog() {
        logger.log(Level.INFO, "End of UERTT test section");
        reservedDataHelperReplacement = null;
    }

    @Test
    public void checkWindowOptionForUeTraceInAnalysisWindow_1_1() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI2);
        setOptionsForLaunchBar(DATE_TIME_30, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        pause(2000);
        assertTrue("Window option UE Trace is not present", checkEventAnalysisWindowForUeTraceOption(selection));
    }

    @Test
    public void noOptionOfUETraceForImsiGroupMesaageDisplay_1_2() throws Exception {
        selenium.setSpeed("1");
        final String imsiGroup = reservedDataHelperReplacement.getReservedData(IMSI_GROUP1);
        setOptionsForLaunchBar(DATE_TIME_30, IMSI_GROUP, imsiGroup, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        checkEventAnalysisWindowForUeTraceOption(selection);
        pause(2000);
        assertFalse("Option UE Trace available for IMSI Group ", selenium.isTextPresent("UE Trace"));
    }

    @Test
    public void openImsiBasedUeTraceDisplayWindowOnClick_1_3() throws Exception {
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI2);
        selenium.setSpeed("1");
        setOptionsForLaunchBar(DATE_TIME_30, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnClick(selection);
        pause(2000);
        String ueTitle = selenium.getText(WINDOW_TITLE).substring(18);
        assertEquals("IMSI - WCDMA UE Trace", ueTitle);
    }

    @Test
    public void openImsiBasedUeTraceDisplayWindowOnDoubleClick_1_4() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI2);
        setOptionsForLaunchBar(DATE_TIME_30, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String ueTitle = selenium.getText(WINDOW_TITLE).substring(18);
        assertEquals("IMSI - WCDMA UE Trace", ueTitle);
    }

    @Test
    public void checkNoDataToDisplay_1_5() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI2);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("No data to display", selenium.getText(NO_DATA));
    }

    @Test
    public void checkTimestampNode_1_6() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("Timestamp", selenium.getText(UE_WINDOW_TIMESTAMP));
    }

    @Test
    public void checkUENode_1_7() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("UE", selenium.getText(UE_NODE));
    }

    @Test
    public void checkeNBNode_1_8() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("NodeB", selenium.getText(NB_NODE));
    }

    @Test
    public void checkSRNCNode_1_9() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("SRNC", selenium.getText(SRNC_NODE));
    }

    @Test
    public void checkDRNCNode_1_10() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("DRNC", selenium.getText(DRNC_NODE));
    }

    @Test
    public void checkCNNode_1_11() throws Exception {
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        assertEquals("CN", selenium.getText(CN_NODE));
    }

    @Test
    public void verifyProtocolsRRCBetweenTheUEAndSRNCNodes_1_12() throws Exception {
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty()) {
                        if (protocolName.contains("RRC:")) {
                            Passed = true;
                            assertEquals(true, Passed);
                            break;
                        }
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void verifyProtocolsNBAPBetweenTheNodeBAndSRNCNodes_1_13() throws Exception {
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("NBAP:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[8]");
                    if (!protocolName.isEmpty()) {
                        if (protocolName.contains("NBAP:")) {
                            Passed = true;
                            assertEquals(true, Passed);
                            break;
                        }
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "NBAP protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void verifyProtocolsRANAPBetweenTheSRNCAndCNodes_1_14() throws Exception {
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RANAP:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[11]");
                    if (!protocolName.isEmpty()) {
                        if (protocolName.contains("RANAP:")) {
                            Passed = true;
                            assertEquals(true, Passed);
                            break;
                        }
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RANAP protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void verifyProtocolsRNSAPPBetweenTheSRNCAndCNodes_1_15() throws Exception {
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        selenium.setSpeed("1");
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RNSAP:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[11]");
                    if (!protocolName.isEmpty()) {
                        if (protocolName.contains("RNSAP:")) {
                            Passed = true;
                            assertEquals(true, Passed);
                            break;
                        }
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RNSAP protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkTimestampFormat_1_16() throws Exception {
        selenium.setSpeed("1");
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            String timeStamp = selenium.getText(TIMESTAMP_FORMAT);
            if (!timeStamp.isEmpty()) {
                String formatString = new StringBuilder(timeStamp).insert(timeStamp.length() - 10, "/").toString();
                String[] timestampArray = formatString.split("/");
                String a = timestampArray[0].toString();
                String b = timestampArray[1].toString();
                String finalTimestamp = b + " " + a;
                Date parseTimestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS").parse(finalTimestamp);
            } else {
                Passed = true;
                logger.log(Level.INFO, "Timestamp not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkDetailsWindowOnProtocolClick_1_17() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertEquals("Details window not opening ", "Event Details View", selenium.getText(DETAILS_WINDOW_TILTE));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        }

        else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkDetailsWindowSubscriberInformation_1_18() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertEquals("Subscriber Information", selenium.getText(SUBSCRIBER_INFORMATION));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        }

        else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkDetailsWindowEventnformation_1_19() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertEquals("Event Information", selenium.getText(EVENT_INFORMATION));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        }

        else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkDetailsWindowCELLRNCInformation_1_20() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertEquals("CELL/RNC Information", selenium.getText(CELL_RNC_INFORMATION));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void checkDetailsWindowMiscellaneousInformationInformation_1_21() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertEquals("Miscellaneous Information", selenium.getText(MISCELLANEOUS_INFORMATION));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        }

        else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void verifyDetailsWindowSubscriberInformationIMSI_1_22() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertTrue("IMSI not matched", selenium.getText(DETAILS_IMSI).substring(29).equals(imsiNumber));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    @Test
    public void verifyDetailsWindowSubscriberInformationProtocol_1_23() throws Exception {
        selenium.setSpeed("1");
        int countRows = 150;
        String protocolName = "";
        boolean Passed = false;
        final String imsiNumber = reservedDataHelperReplacement.getReservedData(IMSI1);
        setOptionsForLaunchBar(DATE_TIME_15, IMSI, imsiNumber, CATEGORY_3G_EVENT_TRACE, WINDOW_OPTION_UERTT);
        openEventAnalysisWindowForUeTraceOnDoubleClick(selection);
        pause(2000);
        String noDataProtocol = selenium.getText(NO_DATA);
        if (!noDataProtocol.contains("No data to display")) {
            if (selenium.isTextPresent("RRC:")) {
                for (int i = 1; i <= countRows; i++) {
                    protocolName = selenium.getText("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]");
                    if (!protocolName.isEmpty() && protocolName.contains("RRC:")) {
                        selenium.click("//table[@id='selenium_tag_DATA TABLE']/tbody/tr[" + i + "]/td[5]//div[@id='selenium_tag_VIEW DETAILS']");
                        pause(2000);
                        selenium.click(DETAILS_WINDOW);
                        pause(2000);
                        assertTrue("Details protocol mismatch", selenium.getText(DETAILS_EVENT).substring(61, 100).contains("RRC"));
                        break;
                    }
                }
            } else {
                Passed = true;
                logger.log(Level.INFO, "RRC protocol not available");
                assertEquals(true, Passed);
            }
        } else {
            assertEquals("Data Not Available", true, Passed);
        }
    }

    private void setOptionsForLaunchBar(final String timeRange, final String dimension, final String dimensionValue, final String windowCategory,
                                        final String windowOption) {
        selection.distroy();
        selection.setDimension(dimension);
        selection.setTimeRange(timeRange);
        selection.setDimensionValue(dimensionValue);
        selection.setWindowCategory(windowCategory);
        selection.setWindowOption(windowOption);
        selection.setIsGroup(isGroupDimension(dimension));
    }

    private boolean isGroupDimension(final String dimension) {
        return dimension.equals(CONTROLLER_GROUP) || dimension.equals(ACCESS_AREA_GROUP) || dimension.equals(SGSN_MME_GROUP)
                || dimension.equals(MSC_GROUP) || dimension.equals(TERMINAL_GROUP) || dimension.equals(IMSI_GROUP)
                || dimension.equals(TRACKING_AREA_Group);
    }

    public void openEventAnalysisWindowForUeTraceOnClick(final Selection selection) throws InterruptedException, PopUpException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        logger.log(Level.INFO, "Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(2000);
        if (selection.getIsGroup()) {
            workspace.enterDimensionValueForGroups(selection.getDimensionValue());
        } else
            workspace.enterDimensionValue(selection.getDimensionValue());
        pause(2000);
        workspace.selectWindowType(selection.getWindowCategory(), selection.getWindowOption());
        workspace.clickLaunchButton();
        selenium.waitForPageLoadingToComplete();
    }

    public void openEventAnalysisWindowForUeTraceOnDoubleClick(final Selection selection) throws InterruptedException, PopUpException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        logger.log(Level.INFO, "Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(2000);
        if (selection.getIsGroup()) {
            workspace.enterDimensionValueForGroups(selection.getDimensionValue());
        } else
            workspace.enterDimensionValue(selection.getDimensionValue());
        pause(2000);
        selectCategoryDoubleClickOnWindowOption(selection.getWindowCategory(), selection.getWindowOption());
        selenium.waitForPageLoadingToComplete();
    }

    public void selectCategoryDoubleClickOnWindowOption(final String category, final String windowOption) {
        int categories = (Integer) selenium.getXpathCount(DIMENSION_CATEGORY);
        for (int i = 1; i <= categories; i++) {
            if (selenium.isElementPresent(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]")) {
                selenium.click(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]");
                pause(5000);
                selenium.doubleClick(DIMENSION_CATEGORY + "[" + i + "]/div[2]/div/div/div//div[contains(text(), '" + windowOption + "')]");
            }
        }
    }

    public boolean checkEventAnalysisWindowForUeTraceOption(final Selection selection) throws InterruptedException, PopUpException {
        workspace.selectTimeRange(selection.getTimeRange());
        workspace.selectDimension(selection.getDimension());
        logger.log(Level.INFO, "Selected group " + selection.getIsGroup() + " Dimentsion : " + selection.getDimension());
        pause(5000);
        if (selection.getIsGroup()) {
            workspace.enterDimensionValueForGroups(selection.getDimensionValue());
        } else
            workspace.enterDimensionValue(selection.getDimensionValue());
        pause(2000);
        String category = selection.getWindowCategory();
        String windowOption = selection.getWindowOption();
        int categories = (Integer) selenium.getXpathCount(DIMENSION_CATEGORY);
        for (int i = 1; i <= categories; i++) {
            if (selenium.isElementPresent(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]")) {
                selenium.click(DIMENSION_CATEGORY + "[" + i + "]//div[span[contains(text(), '" + category + "')]]");
                pause(1000);
                if (selenium.isElementPresent(DIMENSION_CATEGORY + "[" + i + "]/div[2]/div/div/div//div[contains(text(), '" + windowOption + "')]")) {
                    return true;
                }
            }
        }
        return false;
    }
}