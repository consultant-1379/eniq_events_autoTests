/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ltees.verifycounterfiles;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.ericsson.eniq.events.ltees.applicationdrivers.*;
import com.ericsson.eniq.events.ltees.controllers.common.*;
import com.ericsson.eniq.events.ltees.controllers.file.*;

public class VerifyXMLFileCountersGroupResults extends VerifyXMLFileCounters {

    private final CommonController common = new CommonController();

    private final FileController getFile = new FileController();

    private final PropertiesFileController propFile = new PropertiesFileController();

    private final XmlController xml = new XmlController();

    private final String counterPropKeyName = "CounterPropFile_Directory";

    /**
     * @return Output Excel File Headers
     * @throws IOException
     * @throws FileNotFoundException
     */
    public String[] excelOutFileHeaders() throws FileNotFoundException, IOException {
        String[] excelOuputFileHeaders = { "File Name", "PmRrcConnEstab", "PmS1ConnEstab", "PmX2ConnEstab", "PmS1UlNasTransport", "PmUeCtxtRelTime",
                "PmErabEstabTimeInit<QCI>", "PmErabEstabTimeAdded<QCI>", "PmHoSigExeOutTimeS1", "PmHoSigExeOutTimeX2", "PmProcessorLoad" };
        if (AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_ONE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_FIVE) {
            String[] excelOuputFileHeaders_12A = new String[] { "File Name", "pmUeCtxtRelS1Reset", "pmErabRelAttQci", "pmErabRelSuccQci", "pmHoExec",
                    "pmErabEstabFailInit", "pmErabEstabFailAdded", "pmErabRelFail", "pmS1NasNonDelivInd", "pmRrcConnEstabFail", "pmS1ConnEstabFail",
                    "pmX2ConnEstabFail", "pmS1ConnShutdownTime", "pmS1ErrorIndEnb", "pmS1ErrorIndMme", "pmX2ConnResetOut", "pmX2ConnResetIn",
                    "pmX2ErrorIndIn", "pmX2ErrorIndOut" };
            excelOuputFileHeaders = excelOuputFileHeaders_12A;
        }
        if (AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIFTEEN) {

            String[] excelOuputFileHeaders_12B = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeIn", "pmHoExeSucc", "pmHoPrep", "pmHoSigExeOut", "pmNoOfCmas", "pmNoOfEtws",
                    "pmProcessorLoad", "pmRrcConnEstabTime", "pmRrcConnReconf", "pmRrcConnReest", "pmS1ConnEstab", "pmS1ConnShutdownTime",
                    "pmS1XXNasTransport", "pmUeCtxt", "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn",
                    "pmX2ConnResetIn", "pmS1NasNonDelivInd", "pmErabModFail", "pmErabRelAttQci", "pmHoExecSuccDrxConfig" };
            excelOuputFileHeaders = excelOuputFileHeaders_12B;
        }

        if (AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIFTEEN) {

            String[] excelOutputFileHeaders_13A = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeSucc", "pmHoPrep", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabTime",
                    "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt", "pmX2ConnEstabSucc",
                    "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetIn", "pmS1NasNonDelivInd", "pmErabModFail",
                    "pmErabRelEnbGrp", "pmErabRelAttQci", "pmHoExecSuccDrxConfig", "pmRrcConnReestAttUeUnknown", "pmRrcConnReestSuccUeUnknown",
                    "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt", "pmAdvCellSupDetection",
                    "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran", "pmAnrNeighbrelDelUtran",
                    "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc" };

            excelOuputFileHeaders = excelOutputFileHeaders_13A;
        }

        if (AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_CTRS_ROP_FIFTEEN) {

            String[] excelOutputFileHeaders_13B = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabTime",
                    "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt", "pmX2ConnEstabSucc",
                    "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetIn", "pmS1NasNonDelivInd", "pmErabModFail",
                    "pmErabRelEnbGrp", "pmErabRelAttQci", "pmHoExecSuccDrxConfig", "pmRrcConnReestAttUeUnknown", "pmRrcConnReestSuccUeUnknown",
                    "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt", "pmAdvCellSupDetection",
                    "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran", "pmAnrNeighbrelDelUtran",
                    "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran", "pmHoPrepInAttUtran",
                    "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance" };

            excelOuputFileHeaders = excelOutputFileHeaders_13B;
        }

        if (AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_A_PMS) {

            String[] excelOutputFileHeaders_14A = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabTime",
                    "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt", "pmX2ConnEstabSucc",
                    "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetIn", "pmS1NasNonDelivInd", "pmErabModFail",
                    "pmErabRelEnbGrp", "pmErabRelAttQci", "pmHoExecSuccDrxConfig", "pmRrcConnReestAttUeUnknown", "pmRrcConnReestSuccUeUnknown",
                    "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt", "pmAdvCellSupDetection",
                    "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran", "pmAnrNeighbrelDelUtran",
                    "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran", "pmHoPrepInAttUtran",
                    "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance" };

            excelOuputFileHeaders = excelOutputFileHeaders_14A;
        }

        if (AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_B_PMS) {

            String[] excelOutputFileHeaders_14B = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabFail",
                    "pmRrcConnEstabTime", "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt",
                    "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetIn", "pmS1NasNonDelivInd",
                    "pmErabModFail", "pmErabRelEnbGrp", "pmErabRelAttQci", "pmHoExecSuccDrxConfig", "pmRrcConnReestAttUeUnknown",
                    "pmRrcConnReestSuccUeUnknown", "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt",
                    "pmAdvCellSupDetection", "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran",
                    "pmAnrNeighbrelDelUtran", "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran",
                    "pmHoPrepInAttUtran", "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance",
                    "pmNonPlannedPci", "pmCellHoExeSucc", "pmCellHoExeAtt", "pmCellHoPrepSucc", "pmCellHoPrepAtt" };

            excelOuputFileHeaders = excelOutputFileHeaders_14B;
        }
        if (AppDriver.executionContext == AppDriver.ExecutionContext.FIFTEEN_A_PMS) {

            String[] excelOutputFileHeaders_15A = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExecOut", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabFail",
                    "pmRrcConnEstabTime", "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt",
                    "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetOut", "pmX2ConnResetIn", "pmS1NasNonDelivInd",
                    "pmErabModFail", "pmErabRelEnbGrp", "pmErabRelAttQci", "pmHoExecSuccDrxConfig", "pmRrcConnReestAttUeUnknown",
                    "pmRrcConnReestSuccUeUnknown", "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt",
                    "pmAdvCellSupDetection", "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran",
                    "pmAnrNeighbrelDelUtran", "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran",
                    "pmHoPrepInAttUtran", "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance",
                    "pmTaDistr", "pmNonPlannedPci", "pmCellHoExeSucc", "pmCellHoExeAtt", "pmCellHoPrepSucc", "pmCellHoPrepAtt",
                    "pmAnrUeCapFailGeran","pmAnrUeCapFailUtran", "pmAnrUeCapSuccGeran","pmErabEstabAttAddedPaLsm",
                    "pmErabEstabAttInitPaLsm", "pmErabEstabSuccAddedPaLsm", "pmErabEstabSuccInitPaLsm", "pmHoExeAttBlindIntraFreq",
                    "pmRrcConnEstabFailFailureInRadioProcedure", "pmRrcConnEstabFailLackOfResources", "pmRrcConnEstabFailUnspecified",
                    "pmX2ConnEstabFailOut", "pmHoPrepOutX2", "pmX2ErrorIndOut", "pmS1ErrorIndMme", "pmRrcCsfbParRespCdma20001xRtt", "pmRrcCsfbParReqCdma20001xRtt",
                    "pmRrcUlHOPrepTransferCdma20001xRtt", "pmRrcMobFromEUtraCmdCdma20001xRtt", "pmMeasReportCdma20001xRtt", "pmDlS1Cdma2000TunnelingHOSucc",
                    "pmDlS1Cdma2000TunnelingHOFail", "pmDlS1Cdma2000TunnelingNonHO"};

            excelOuputFileHeaders = excelOutputFileHeaders_15A;
        }
        if (AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_A_PMS) {

            String[] excelOutputFileHeaders_16A = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabFail",
                    "pmRrcConnEstabTime", "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt",
                    "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetOut", "pmX2ConnResetIn", "pmS1NasNonDelivInd",
                    "pmErabModFail", "pmErabRelEnb", "pmErabRelAttQci", "pmRrcConnReestAttUeUnknown",
                    "pmRrcConnReestSuccUeUnknown", "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt",
                    "pmAdvCellSupDetection", "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran",
                    "pmAnrNeighbrelDelUtran", "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran",
                    "pmHoPrepInAttUtran", "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance",
                    "pmTaDistr", "pmNonPlannedPci", "pmCellHoExeSucc", "pmCellHoExeAtt", "pmCellHoPrepSucc", "pmCellHoPrepAtt",
                    "pmAnrUeCapFailGeran","pmAnrUeCapFailUtran", "pmAnrUeCapSuccGeran","pmErabEstabAttAddedPaLsm",
                    "pmErabEstabAttInitPaLsm", "pmErabEstabSuccAddedPaLsm", "pmErabEstabSuccInitPaLsm", "pmHoExeAttBlindIntraFreq",
                    "pmRrcConnEstabFailFailureInRadioProcedure", "pmRrcConnEstabFailLackOfResources", "pmRrcConnEstabFailUnspecified",
                    "pmX2ConnEstabFailOut", "pmHoPrepOutX2", "pmX2ErrorIndOut", "pmS1ErrorIndMme", "pmHoExeAttLteInter", "pmHoExeSuccLteInter", "pmHoPrepAttLteInterFAto", 
                    "pmHoPrepSuccLteInterFAto", "pmAgMeasSuccCgi", "pmDrxMeasSuccCgi", "pmErabRelFail", "pmErabRelMme", "pmErabRelSuccQci", "pmHoExec", "pmHoPrepInRejUtran",
                    "pmHoPrepInS1Rej", "pmHoPrepInX2Rej", "pmHoPrepOutRejIntraEnbInterFreq", "pmHoPrepOutRejIntraEnbIntraFreq", "pmHoPrepOutRejTdScdma", "pmHoPrepOutS1RejInterEnbInterFreq",
                    "pmHoPrepOutS1RejInterEnbIntraFreq" , "pmRrcCsfbParRespCdma20001xRtt", "pmRrcCsfbParReqCdma20001xRtt",
                    "pmRrcUlHOPrepTransferCdma20001xRtt", "pmRrcMobFromEUtraCmdCdma20001xRtt", "pmMeasReportCdma20001xRtt", "pmDlS1Cdma2000TunnelingHOSucc",
                    "pmDlS1Cdma2000TunnelingHOFail", "pmDlS1Cdma2000TunnelingNonHO"};

            excelOuputFileHeaders = excelOutputFileHeaders_16A;
        }
        if (AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_B_PMS) {

            String[] excelOutputFileHeaders_16B = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabFail",
                    "pmRrcConnEstabTime", "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt",
                    "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetOut", "pmX2ConnResetIn", "pmS1NasNonDelivInd",
                    "pmErabModFail", "pmErabRelEnb", "pmErabRelAttQci", "pmRrcConnReestAttUeUnknown",
                    "pmRrcConnReestSuccUeUnknown", "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt",
                    "pmAdvCellSupDetection", "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran",
                    "pmAnrNeighbrelDelUtran", "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran",
                    "pmHoPrepInAttUtran", "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance",
                    "pmTaDistr", "pmNonPlannedPci", "pmCellHoExeSucc", "pmCellHoExeAtt", "pmCellHoPrepSucc", "pmCellHoPrepAtt",
                    "pmAnrUeCapFailGeran","pmAnrUeCapFailUtran", "pmAnrUeCapSuccGeran","pmErabEstabAttAddedPaLsm",
                    "pmErabEstabAttInitPaLsm", "pmErabEstabSuccAddedPaLsm", "pmErabEstabSuccInitPaLsm", "pmHoExeAttBlindIntraFreq",
                    "pmRrcConnEstabFailFailureInRadioProcedure", "pmRrcConnEstabFailLackOfResources", "pmRrcConnEstabFailUnspecified",
                    "pmX2ConnEstabFailOut", "pmHoPrepOutX2", "pmX2ErrorIndOut", "pmS1ErrorIndMme", "pmHoExeAttLteInter", "pmHoExeSuccLteInter", "pmHoPrepAttLteInterFAto", 
                    "pmHoPrepSuccLteInterFAto", "pmAgMeasSuccCgi", "pmDrxMeasSuccCgi", "pmErabRelFail", "pmErabRelMme", "pmErabRelSuccQci", "pmHoExec", "pmHoPrepInRejUtran",
                    "pmHoPrepInS1Rej", "pmHoPrepInX2Rej", "pmHoPrepOutRejIntraEnbInterFreq", "pmHoPrepOutRejIntraEnbIntraFreq", "pmHoPrepOutRejTdScdma", "pmHoPrepOutS1RejInterEnbInterFreq",
                    "pmHoPrepOutS1RejInterEnbIntraFreq" , "pmRrcCsfbParRespCdma20001xRtt", "pmRrcCsfbParReqCdma20001xRtt",
                    "pmRrcUlHOPrepTransferCdma20001xRtt", "pmRrcMobFromEUtraCmdCdma20001xRtt", "pmMeasReportCdma20001xRtt", "pmDlS1Cdma2000TunnelingHOSucc",
                    "pmDlS1Cdma2000TunnelingHOFail", "pmDlS1Cdma2000TunnelingNonHO","pmPrbUlUtil","pmPrbDlUtil","pmCceUtil","pmHoExeAttAto","pmHoExeSuccAto",
                    "pmHoExeAttSrvccAto","pmHoExeSuccSrvccAto","pmHoExeAttUeMeas","pmHoExeAttUeMeasRsrp","pmHoExeAttSrvccUeMeas","pmHoExeAttSrvccUeMeasRsrp",
                    "pmHoExeSuccUeMeas","pmHoExeSuccUeMeasRsrp","pmHoExeSuccSrvccUeMeas","pmHoExeSuccSrvccUeMeasRsrp","pmHoPrepAttAto","pmHoPrepSuccAto",
                    "pmHoPrepAttSrvccAto","pmHoPrepSuccSrvccAto", "pmRrcConnReest", "pmHoPrepOutFailTo", "pmHoPrepOutAttQci", "pmHoPrepOutS1AttInterEnb", "pmHoPrepOutS1SuccInterEnb", "pmHoPrepOutSuccQci", "pmHoPrepSuccLteInterFQci"};

            excelOuputFileHeaders = excelOutputFileHeaders_16B;
        }
        if (AppDriver.executionContext == AppDriver.ExecutionContext.SEVENTEEN_A_PMS) {

            String[] excelOutputFileHeaders_17A = new String[] { "File Name", "pmAnrMeas", "pmAnrNeighbrelAdd", "pmAnrUeCap", "pmErabEstab",
                    "pmHoExeAtt", "pmHoExeSucc", "pmHoSigExeOut", "pmNoOfEtws", "pmProcessorLoad", "pmRrcConnEstabFail",
                    "pmRrcConnEstabTime", "pmRrcConnReconf", "pmS1ConnEstab", "pmS1ConnShutdownTime", "pmS1XXNasTransport", "pmUeCtxt",
                    "pmX2ConnEstabSucc", "pmS1ErrorIndEnb", "pmX2ErrorIndIn", "pmX2ConnEstabFailIn", "pmX2ConnResetOut", "pmX2ConnResetIn", "pmS1NasNonDelivInd",
                    "pmErabModFail", "pmErabRelEnb", "pmErabRelAttQci", "pmRrcConnReestAttUeUnknown",
                    "pmRrcConnReestSuccUeUnknown", "pmHoExeInSuccTdScdmaQci", "pmAdvCellSupRecoveryCellAtt", "pmAdvCellSupRecoveryNodeAtt",
                    "pmAdvCellSupDetection", "pmAdvCellSupRecoveryCellSucc", "pmAdvCellSupRecoveryNodeSucc", "pmAnrNeighbrelDelGeran",
                    "pmAnrNeighbrelDelUtran", "pmNoOfCmasRepReq", "pmNoOfCmasRepSucc", "pmNoOfCmasReq", "pmNoOfCmasSucc", "pmHoExeInSuccUtran",
                    "pmHoPrepInAttUtran", "pmHoPrepInSuccUtran", "pmHoPrepRejInLicMobUtran", "pmHoPrepRejInLicMobInterMode", "pmTimingAdvance",
                    "pmTaDistr", "pmNonPlannedPci", "pmCellHoExeSucc", "pmCellHoExeAtt", "pmCellHoPrepSucc", "pmCellHoPrepAtt",
                    "pmAnrUeCapFailGeran","pmAnrUeCapFailUtran", "pmAnrUeCapSuccGeran","pmErabEstabAttAddedPaLsm",
                    "pmErabEstabAttInitPaLsm", "pmErabEstabSuccAddedPaLsm", "pmErabEstabSuccInitPaLsm", "pmHoExeAttBlindIntraFreq",
                    "pmRrcConnEstabFailFailureInRadioProcedure", "pmRrcConnEstabFailLackOfResources", "pmRrcConnEstabFailUnspecified",
                    "pmX2ConnEstabFailOut", "pmHoPrepOutX2", "pmX2ErrorIndOut", "pmS1ErrorIndMme", "pmHoExeAttLteInter", "pmHoExeSuccLteInter", "pmHoPrepAttLteInterFAto", 
                    "pmHoPrepSuccLteInterFAto", "pmAgMeasSuccCgi", "pmDrxMeasSuccCgi", "pmErabRelFail", "pmErabRelMme", "pmErabRelSuccQci", "pmHoExec", "pmHoPrepInRejUtran",
                    "pmHoPrepInS1Rej", "pmHoPrepInX2Rej", "pmHoPrepOutRejIntraEnbInterFreq", "pmHoPrepOutRejIntraEnbIntraFreq", "pmHoPrepOutRejTdScdma", "pmHoPrepOutS1RejInterEnbInterFreq",
                    "pmHoPrepOutS1RejInterEnbIntraFreq" , "pmRrcCsfbParRespCdma20001xRtt", "pmRrcCsfbParReqCdma20001xRtt",
                    "pmRrcUlHOPrepTransferCdma20001xRtt", "pmRrcMobFromEUtraCmdCdma20001xRtt", "pmMeasReportCdma20001xRtt", "pmDlS1Cdma2000TunnelingHOSucc",
                    "pmDlS1Cdma2000TunnelingHOFail", "pmDlS1Cdma2000TunnelingNonHO","pmPrbUlUtil","pmPrbDlUtil","pmCceUtil","pmHoExeAttAto","pmHoExeSuccAto",
                    "pmHoExeAttSrvccAto","pmHoExeSuccSrvccAto","pmHoExeAttUeMeas","pmHoExeAttUeMeasRsrp","pmHoExeAttSrvccUeMeas","pmHoExeAttSrvccUeMeasRsrp",
                    "pmHoExeSuccUeMeas","pmHoExeSuccUeMeasRsrp","pmHoExeSuccSrvccUeMeas","pmHoExeSuccSrvccUeMeasRsrp","pmHoPrepAttAto","pmHoPrepSuccAto",
                    "pmHoPrepAttSrvccAto","pmHoPrepSuccSrvccAto", "pmRrcConnReest", "pmHoPrepOutFailTo", "pmHoPrepOutAttQci", "pmHoPrepOutS1AttInterEnb", "pmHoPrepOutS1SuccInterEnb", "pmHoPrepOutSuccQci", "pmHoPrepSuccLteInterFQci"};

            excelOuputFileHeaders = excelOutputFileHeaders_17A;
        }
        return excelOuputFileHeaders;

    }

    private String[] merge(String[] arg1, String[] arg2) {
        String[] merged = new String[arg1.length + arg2.length];
        for (int i = 0; i < arg1.length; i++) {
            merged[i] = arg1[i];
        }
        for (int i = 0; i < arg2.length; i++) {
            merged[arg1.length + i] = arg2[i];
        }
        return merged;

    }

    /**
     * @return Overall Counter Results
     */
    public String allCounterResultsOutput(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        xml.openXmlFile(fileName);
        String result = null;
        /*** Add Results value according to Excel File Header Order to arrayList ***/
        final String fullFileName = getFile.getFileNameWithDirExt(fileName, false, false, false);
        AppDriver.allCounterResults.add(fullFileName);

        if (AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.ELEVEN_B_CTRS_ROP_FIFTEEN) {
            final String groupPmRrcConnEstabResult = pmRrcConnEstabGroupResult(fileName);
            final String groupPmS1ConnEstabResult = pmS1ConnEstabGroupResult(fileName);
            final String groupPmX2ConnEstabResult = pmX2ConnEstabGroupResult(fileName);
            final String groupPmS1UlNasTransportResult = pmS1UlNasTransportGroupResult(fileName);
            final String groupPmUeCtxtRelTimeResult = pmUeCtxtRelTimeGroupResult(fileName);
            final String groupPmErabEstabTimeInitQCIResult = pmErabEstabTimeInitQCIGroupResult(fileName);
            final String groupPmErabEstabTimeAddedQCIResult = pmErabEstabTimeAddedQCIGroupResult(fileName);
            final String groupPmHoSigExeOutTimeS1Result = pmHoSigExeOutTimeS1GroupResult(fileName);
            final String groupPmHoSigExeOutTimeX2Result = pmHoSigExeOutTimeX2GroupResult(fileName);
            final String groupPmProcessorLoadResult = pmProcessorLoadGroupResult(fileName);

            if (groupPmProcessorLoadResult.equals("FAIL") || groupPmHoSigExeOutTimeX2Result.equals("FAIL")
                    || groupPmHoSigExeOutTimeS1Result.equals("FAIL") || groupPmErabEstabTimeAddedQCIResult.equals("FAIL")
                    || groupPmErabEstabTimeInitQCIResult.equals("FAIL") || groupPmUeCtxtRelTimeResult.equals("FAIL")
                    || groupPmS1UlNasTransportResult.equals("FAIL") || groupPmX2ConnEstabResult.equals("FAIL")
                    || groupPmS1ConnEstabResult.equals("FAIL") || groupPmRrcConnEstabResult.equals("FAIL")) {

                result = "FAIL";
            } else {
                result = "PASS";
            }
        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_ONE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_A_CTRS_ROP_FIFTEEN) {
            final String singlePmUeCtxtRelS1Reset = pmUeCtxtRelS1ResetResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult(fileName);
            final String groupPmErabRelSuccQci = pmErabRelSuccQciGroupResult(fileName);
            final String groupPmHoExec = pmHoExecGroupResult(fileName);
            final String groupPmErabEstabFailInitResult = pmErabEstabFailInitGroupResult(fileName);
            final String groupPmErabEstabFailAddedResult = pmErabEstabFailAddedGroupResult(fileName);
            final String groupPmErabRelFailResult = pmErabRelFailGroupResult(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGroupResult(fileName);
            final String groupPmS1ConnEstabFailResult = pmS1ConnEstabFailGroupResult(fileName);
            final String groupPmX2ConnEstabFailResult = pmX2ConnEstabFailGroupResult(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult(fileName);
            final String groupPmS1ErrorIndMme = pmS1ErrorIndMmeGroupResult(fileName);
            final String groupPmX2ConnResetOut = pmX2ConnResetOutGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult(fileName);
            final String groupPmX2ErrorIndOut = pmX2ErrorIndOutGroupResult(fileName);

            if (singlePmUeCtxtRelS1Reset.equals("FAIL") || groupPmErabEstabFailInitResult.equals("FAIL")
                    || groupPmErabEstabFailAddedResult.equals("FAIL") || groupPmHoExec.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL")
                    || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabRelFailResult.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL")
                    || groupPmErabRelSuccQci.equals("FAIL") || groupPmS1ConnEstabFailResult.equals("FAIL")
                    || groupPmX2ConnEstabFailResult.equals("FAIL") || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmS1ErrorIndMme.equals("FAIL")
                    || groupPmX2ConnResetOut.equals("FAIL") || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ErrorIndOut.equals("FAIL")) {
                return result = "FAIL";
            } else {
                result = "PASS";
            }

        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.TWELVE_B_CTRS_ROP_FIFTEEN) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeIn = pmHoExeInGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoPrep = pmHoPrepGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfCmas = pmNoOfCmasGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmRrcConnReest = pmRrcConnReestGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);

            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            //final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeIn.equals("FAIL") || groupPmHoExeSucc.equals("FAIL") || groupPmHoPrep.equals("FAIL")
                    || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfCmas.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL") || groupPmRrcConnReconf.equals("FAIL")
                    || groupPmRrcConnReest.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }

        else if (AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_A_CTRS_ROP_FIFTEEN) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoPrep = pmHoPrepGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoPrep.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL")
                    || groupPmNoOfEtws.equals("FAIL") || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnbGrp.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL")) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }

        else if (AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_PMS
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_CTRS_ROP_FIVE
                || AppDriver.executionContext == AppDriver.ExecutionContext.THIRTEEN_B_CTRS_ROP_FIFTEEN) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL") || groupPmRrcConnReconf.equals("FAIL")
                    || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL") || groupPmS1XXNasTransport.equals("FAIL")
                    || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL") || groupPmS1ErrorIndEnb.equals("FAIL")
                    || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL") || groupPmX2ConnResetIn.equals("FAIL")
                    || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL") || groupPmErabRelEnbGrp.equals("FAIL")
                    || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL")

            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }

        else if (AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_A_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);

            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */

            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL") || groupPmRrcConnReconf.equals("FAIL")
                    || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL") || groupPmS1XXNasTransport.equals("FAIL")
                    || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL") || groupPmS1ErrorIndEnb.equals("FAIL")
                    || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL") || groupPmX2ConnResetIn.equals("FAIL")
                    || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL") || groupPmErabRelEnbGrp.equals("FAIL")
                    || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL")

            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        } else if (AppDriver.executionContext == AppDriver.ExecutionContext.FOURTEEN_B_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGenGroupResult(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);

            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);

            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);

            /* 14B New Counter Group */
            final String grouppmNonPlannedPci = pmNonPlannedPciGroupResult(fileName);
            final String grouppmCellHoExeSucc = pmCellHoExeSuccGroupResult(fileName);
            final String grouppmCellHoExeAtt = pmCellHoExeAttGroupResult(fileName);
            final String grouppmCellHoPrepSucc = pmCellHoPrepSuccGroupResult(fileName);
            final String grouppmCellHoPrepAtt = pmCellHoPrepAttGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnbGrp.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL") || grouppmNonPlannedPci.equals("FAIL")
                    || grouppmCellHoExeSucc.equals("FAIL") || grouppmCellHoExeAtt.equals("FAIL") || grouppmCellHoPrepSucc.equals("FAIl")
                    || grouppmCellHoPrepAtt.equals("FAIL")

            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }

        else if (AppDriver.executionContext == AppDriver.ExecutionContext.FIFTEEN_A_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExecOut = pmHoExecOutGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGenGroupResult(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetOut = pmX2ConnResetOutGroupResult15A(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnbGrp = pmErabRelEnbGrpGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmHoExecSuccDrxConfig = pmHoExecSuccDrxConfigGroupResult(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);
            final String grouppmTaDistr = pmTaDistrGroupResult(fileName);

            /* 14B New Counter Group */
            final String grouppmNonPlannedPci = pmNonPlannedPciGroupResult(fileName);
            final String grouppmCellHoExeSucc = pmCellHoExeSuccGroupResult(fileName);
            final String grouppmCellHoExeAtt = pmCellHoExeAttGroupResult(fileName);
            final String grouppmCellHoPrepSucc = pmCellHoPrepSuccGroupResult(fileName);
            final String grouppmCellHoPrepAtt = pmCellHoPrepAttGroupResult(fileName);

            /* 15A counters Group */
            final String grouppmAnrUeCapFailGeran = pmAnrUeCapFailGeranGroupResult(fileName);
            final String grouppmAnrUeCapFailUtran = pmAnrUeCapFailUtranGroupResult(fileName);
            final String grouppmAnrUeCapSuccGeran = pmAnrUeCapSuccGeranGroupResult(fileName);
            final String grouppmErabEstabAttAddedPaLsm = pmErabEstabAttAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabAttInitPaLsm = pmErabEstabAttInitPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccAddedPaLsm = pmErabEstabSuccAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccInitPaLsm = pmErabEstabSuccInitPaLsmGroupResult(fileName);
            final String grouppmHoExeAttBlindIntraFreq = pmHoExeAttBlindIntraFreqGroupResult(fileName);
            final String grouppmRrcConnEstabFailFailureInRadioProcedure = pmRrcConnEstabFailFailureInRadioProcedureGroupResult(fileName);
            final String grouppmRrcConnEstabFailLackOfResources = pmRrcConnEstabFailLackOfResourcesGroupResult(fileName);
            final String grouppmRrcConnEstabFailUnspecified = pmRrcConnEstabFailUnspecifiedGroupResult(fileName);
            final String grouppmX2ConnEstabFailOut = pmX2ConnEstabFailOutGroupResult(fileName);
            final String grouppmHoPrepOutX2 = pmHoPrepOutX2GroupResult(fileName);
            final String grouppmX2ErrorIndOut = pmX2ErrorIndOutGroupResult15A(fileName);
            final String grouppmS1ErrorIndMme = pmS1ErrorIndMmeGroupResult15A(fileName);
            final String grouppmRrcCsfbParRespCdma20001xRtt = pmRrcCsfbParRespCdma20001xRttGroupResult(fileName);
            final String grouppmRrcCsfbParReqCdma20001xRtt = pmRrcCsfbParReqCdma20001xRttGroupResult(fileName);
            final String grouppmRrcUlHOPrepTransferCdma20001xRtt = pmRrcUlHOPrepTransferCdma20001xRttGroupResult(fileName);
            final String grouppmRrcMobFromEUtraCmdCdma20001xRtt = pmRrcMobFromEUtraCmdCdma20001xRttGroupResult(fileName);
            final String grouppmMeasReportCdma20001xRtt = pmMeasReportCdma20001xRttGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOSucc = pmDlS1Cdma2000TunnelingHOSuccGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOFail = pmDlS1Cdma2000TunnelingHOFailGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingNonHO = pmDlS1Cdma2000TunnelingNonHOGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL") || groupPmHoExecOut.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetOut.equals("FAIL") || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnbGrp.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL") || groupPmHoExecSuccDrxConfig.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL") || grouppmTaDistr.equals("FAIL")
                    || grouppmNonPlannedPci.equals("FAIL") || grouppmCellHoExeSucc.equals("FAIL") || grouppmCellHoExeAtt.equals("FAIL")
                    || grouppmCellHoPrepSucc.equals("FAIl") || grouppmCellHoPrepAtt.equals("FAIL") || grouppmAnrUeCapFailGeran.equals("FAIL") || grouppmAnrUeCapFailUtran.equals("FAIL")
                    || grouppmAnrUeCapSuccGeran.equals("FAIL") || grouppmErabEstabAttAddedPaLsm.equals("FAIL") || grouppmErabEstabAttInitPaLsm.equals("FAIL")
                    || grouppmErabEstabSuccAddedPaLsm.equals("FAIL") || grouppmErabEstabSuccInitPaLsm.equals("FAIL") || grouppmHoExeAttBlindIntraFreq.equals("FAIL")
                    || grouppmRrcConnEstabFailFailureInRadioProcedure.equals("FAIL") || grouppmRrcConnEstabFailLackOfResources.equals("FAIL") || grouppmRrcConnEstabFailUnspecified.equals("FAIL")
                    || grouppmX2ConnEstabFailOut.equals("FAIL") || grouppmHoPrepOutX2.equals("FAIL") || grouppmX2ErrorIndOut.equals("FAIL") || grouppmS1ErrorIndMme.equals("FAIL")
                    || grouppmRrcCsfbParRespCdma20001xRtt.equals("FAIL") || grouppmRrcCsfbParReqCdma20001xRtt.equals("FAIL") || grouppmRrcUlHOPrepTransferCdma20001xRtt.equals("FAIL")
                    || grouppmRrcMobFromEUtraCmdCdma20001xRtt.equals("FAIL") || grouppmMeasReportCdma20001xRtt.equals("FAIL") || grouppmDlS1Cdma2000TunnelingHOSucc.equals("FAIL")
                    || grouppmDlS1Cdma2000TunnelingHOFail.equals("FAIL") || grouppmDlS1Cdma2000TunnelingNonHO.equals("FAIL")

            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }
        else if (AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_A_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGenGroupResult(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetOut = pmX2ConnResetOutGroupResult15A(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnb = pmErabRelEnbGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);
            final String grouppmTaDistr = pmTaDistrGroupResult(fileName);

            /* 14B New Counter Group */
            final String grouppmNonPlannedPci = pmNonPlannedPciGroupResult(fileName);
            final String grouppmCellHoExeSucc = pmCellHoExeSuccGroupResult(fileName);
            final String grouppmCellHoExeAtt = pmCellHoExeAttGroupResult(fileName);
            final String grouppmCellHoPrepSucc = pmCellHoPrepSuccGroupResult(fileName);
            final String grouppmCellHoPrepAtt = pmCellHoPrepAttGroupResult(fileName);

            /* 15A counters Group */
            final String grouppmAnrUeCapFailGeran = pmAnrUeCapFailGeranGroupResult(fileName);
            final String grouppmAnrUeCapFailUtran = pmAnrUeCapFailUtranGroupResult(fileName);
            final String grouppmAnrUeCapSuccGeran = pmAnrUeCapSuccGeranGroupResult(fileName);
            final String grouppmErabEstabAttAddedPaLsm = pmErabEstabAttAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabAttInitPaLsm = pmErabEstabAttInitPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccAddedPaLsm = pmErabEstabSuccAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccInitPaLsm = pmErabEstabSuccInitPaLsmGroupResult(fileName);
            final String grouppmHoExeAttBlindIntraFreq = pmHoExeAttBlindIntraFreqGroupResult(fileName);
            final String grouppmRrcConnEstabFailFailureInRadioProcedure = pmRrcConnEstabFailFailureInRadioProcedureGroupResult(fileName);
            final String grouppmRrcConnEstabFailLackOfResources = pmRrcConnEstabFailLackOfResourcesGroupResult(fileName);
            final String grouppmRrcConnEstabFailUnspecified = pmRrcConnEstabFailUnspecifiedGroupResult(fileName);
            final String grouppmX2ConnEstabFailOut = pmX2ConnEstabFailOutGroupResult(fileName);
            final String grouppmHoPrepOutX2 = pmHoPrepOutX2GroupResult(fileName);
            final String grouppmX2ErrorIndOut = pmX2ErrorIndOutGroupResult15A(fileName);
            final String grouppmS1ErrorIndMme = pmS1ErrorIndMmeGroupResult15A(fileName);
            /* 16A Counters Group */
            final String grouppmHoExeAttLteInter = pmHoExeAttLteInterGroupResult(fileName);
            final String grouppmHoExeSuccLteInter = pmHoExeSuccLteInterGroupResult(fileName);
            final String grouppmHoPrepAttLteInterFAto = pmHoPrepAttLteInterFAtoGroupResult(fileName);
            final String grouppmHoPrepSuccLteInterFAto = pmHoPrepSuccLteInterFAtoGroupResult(fileName);
            final String grouppmAgMeasSuccCgi = pmAgMeasSuccCgiGroupResult(fileName);
            final String grouppmDrxMeasSuccCgi = pmDrxMeasSuccCgiGroupResult(fileName);
            final String grouppmErabRelFail = pmErabRelFailGroupResult16A(fileName);
            final String grouppmErabRelMme = pmErabRelMmeGroupResult(fileName);
            final String grouppmErabRelSuccQci = pmErabRelSuccQciGroupResult16A(fileName);
            final String grouppmHoExec = pmHoExecGroupResult16A(fileName);
            final String grouppmHoPrepInRejUtran = pmHoPrepInRejUtranGroupResult(fileName);
            final String grouppmHoPrepInS1Rej = pmHoPrepInS1RejGroupResult(fileName);
            final String grouppmHoPrepInX2Rej = pmHoPrepInX2RejGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbInterFreq = pmHoPrepOutRejIntraEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbIntraFreq = pmHoPrepOutRejIntraEnbIntraFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejTdScdma = pmHoPrepOutRejTdScdmaGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbInterFreq = pmHoPrepOutS1RejInterEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbIntraFreq = pmHoPrepOutS1RejInterEnbIntraFreqGroupResult(fileName);
            
            /*eCSFB*/
            
            final String grouppmRrcCsfbParRespCdma20001xRtt = pmRrcCsfbParRespCdma20001xRttGroupResult(fileName);
            final String grouppmRrcCsfbParReqCdma20001xRtt = pmRrcCsfbParReqCdma20001xRttGroupResult(fileName);
            final String grouppmRrcUlHOPrepTransferCdma20001xRtt = pmRrcUlHOPrepTransferCdma20001xRttGroupResult(fileName);
            final String grouppmRrcMobFromEUtraCmdCdma20001xRtt = pmRrcMobFromEUtraCmdCdma20001xRttGroupResult(fileName);
            final String grouppmMeasReportCdma20001xRtt = pmMeasReportCdma20001xRttGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOSucc = pmDlS1Cdma2000TunnelingHOSuccGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOFail = pmDlS1Cdma2000TunnelingHOFailGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingNonHO = pmDlS1Cdma2000TunnelingNonHOGroupResult(fileName);

            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnb.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL") || grouppmTaDistr.equals("FAIL")
                    || grouppmNonPlannedPci.equals("FAIL") || grouppmCellHoExeSucc.equals("FAIL") || grouppmCellHoExeAtt.equals("FAIL")
                    || grouppmCellHoPrepSucc.equals("FAIl") || grouppmCellHoPrepAtt.equals("FAIL") || grouppmAnrUeCapFailGeran.equals("FAIL") || grouppmAnrUeCapFailUtran.equals("FAIL")
                    || grouppmAnrUeCapSuccGeran.equals("FAIL") || grouppmErabEstabAttAddedPaLsm.equals("FAIL") || grouppmErabEstabAttInitPaLsm.equals("FAIL")
                    || grouppmErabEstabSuccAddedPaLsm.equals("FAIL") || grouppmErabEstabSuccInitPaLsm.equals("FAIL") || grouppmHoExeAttBlindIntraFreq.equals("FAIL")
                    || grouppmRrcConnEstabFailFailureInRadioProcedure.equals("FAIL") || grouppmRrcConnEstabFailLackOfResources.equals("FAIL") || grouppmRrcConnEstabFailUnspecified.equals("FAIL")
                    || grouppmX2ConnEstabFailOut.equals("FAIL") || grouppmHoPrepOutX2.equals("FAIL") || grouppmHoExeAttLteInter.equals("FAIL")
                    || grouppmHoExeSuccLteInter.equals("FAIL") || grouppmHoPrepAttLteInterFAto.equals("FAIL") || grouppmHoPrepSuccLteInterFAto.equals("FAIL")
                    || grouppmAgMeasSuccCgi.equals("FAIL")|| grouppmDrxMeasSuccCgi.equals("FAIL") || grouppmErabRelFail.equals("FAIL")
                    || grouppmErabRelMme.equals("FAIL")|| grouppmErabRelSuccQci.equals("FAIL") || grouppmHoExec.equals("FAIL") || grouppmHoPrepInRejUtran.equals("FAIL")
                    || grouppmHoPrepInS1Rej.equals("FAIL")|| grouppmHoPrepInX2Rej.equals("FAIL") || grouppmHoPrepOutRejIntraEnbInterFreq.equals("FAIL") || grouppmHoPrepOutRejIntraEnbIntraFreq.equals("FAIL")
                    || grouppmHoPrepOutRejTdScdma.equals("FAIL")|| grouppmHoPrepOutS1RejInterEnbInterFreq.equals("FAIL") || grouppmHoPrepOutS1RejInterEnbIntraFreq.equals("FAIL")
                    || grouppmRrcCsfbParRespCdma20001xRtt.equals("FAIL") || grouppmRrcCsfbParReqCdma20001xRtt.equals("FAIL") || grouppmRrcUlHOPrepTransferCdma20001xRtt.equals("FAIL")
                    || grouppmRrcMobFromEUtraCmdCdma20001xRtt.equals("FAIL") || grouppmMeasReportCdma20001xRtt.equals("FAIL") || grouppmDlS1Cdma2000TunnelingHOSucc.equals("FAIL")
                    || grouppmDlS1Cdma2000TunnelingHOFail.equals("FAIL") || grouppmDlS1Cdma2000TunnelingNonHO.equals("FAIL")

            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }
        else if (AppDriver.executionContext == AppDriver.ExecutionContext.SIXTEEN_B_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGenGroupResult(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetOut = pmX2ConnResetOutGroupResult15A(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnb = pmErabRelEnbGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);
            final String grouppmTaDistr = pmTaDistrGroupResult(fileName);

            /* 14B New Counter Group */
            final String grouppmNonPlannedPci = pmNonPlannedPciGroupResult(fileName);
            final String grouppmCellHoExeSucc = pmCellHoExeSuccGroupResult(fileName);
            final String grouppmCellHoExeAtt = pmCellHoExeAttGroupResult(fileName);
            final String grouppmCellHoPrepSucc = pmCellHoPrepSuccGroupResult(fileName);
            final String grouppmCellHoPrepAtt = pmCellHoPrepAttGroupResult(fileName);

            /* 15A counters Group */
            final String grouppmAnrUeCapFailGeran = pmAnrUeCapFailGeranGroupResult(fileName);
            final String grouppmAnrUeCapFailUtran = pmAnrUeCapFailUtranGroupResult(fileName);
            final String grouppmAnrUeCapSuccGeran = pmAnrUeCapSuccGeranGroupResult(fileName);
            final String grouppmErabEstabAttAddedPaLsm = pmErabEstabAttAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabAttInitPaLsm = pmErabEstabAttInitPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccAddedPaLsm = pmErabEstabSuccAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccInitPaLsm = pmErabEstabSuccInitPaLsmGroupResult(fileName);
            final String grouppmHoExeAttBlindIntraFreq = pmHoExeAttBlindIntraFreqGroupResult(fileName);
            final String grouppmRrcConnEstabFailFailureInRadioProcedure = pmRrcConnEstabFailFailureInRadioProcedureGroupResult(fileName);
            final String grouppmRrcConnEstabFailLackOfResources = pmRrcConnEstabFailLackOfResourcesGroupResult(fileName);
            final String grouppmRrcConnEstabFailUnspecified = pmRrcConnEstabFailUnspecifiedGroupResult(fileName);
            final String grouppmX2ConnEstabFailOut = pmX2ConnEstabFailOutGroupResult(fileName);
            final String grouppmHoPrepOutX2 = pmHoPrepOutX2GroupResult(fileName);
            final String grouppmX2ErrorIndOut = pmX2ErrorIndOutGroupResult15A(fileName);
            final String grouppmS1ErrorIndMme = pmS1ErrorIndMmeGroupResult15A(fileName);
            /* 16A Counters Group */
            final String grouppmHoExeAttLteInter = pmHoExeAttLteInterGroupResult(fileName);
            final String grouppmHoExeSuccLteInter = pmHoExeSuccLteInterGroupResult(fileName);
            final String grouppmHoPrepAttLteInterFAto = pmHoPrepAttLteInterFAtoGroupResult(fileName);
            final String grouppmHoPrepSuccLteInterFAto = pmHoPrepSuccLteInterFAtoGroupResult(fileName);
            final String grouppmAgMeasSuccCgi = pmAgMeasSuccCgiGroupResult(fileName);
            final String grouppmDrxMeasSuccCgi = pmDrxMeasSuccCgiGroupResult(fileName);
            final String grouppmErabRelFail = pmErabRelFailGroupResult16A(fileName);
            final String grouppmErabRelMme = pmErabRelMmeGroupResult(fileName);
            final String grouppmErabRelSuccQci = pmErabRelSuccQciGroupResult16A(fileName);
            final String grouppmHoExec = pmHoExecGroupResult16A(fileName);
            final String grouppmHoPrepInRejUtran = pmHoPrepInRejUtranGroupResult(fileName);
            final String grouppmHoPrepInS1Rej = pmHoPrepInS1RejGroupResult(fileName);
            final String grouppmHoPrepInX2Rej = pmHoPrepInX2RejGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbInterFreq = pmHoPrepOutRejIntraEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbIntraFreq = pmHoPrepOutRejIntraEnbIntraFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejTdScdma = pmHoPrepOutRejTdScdmaGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbInterFreq = pmHoPrepOutS1RejInterEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbIntraFreq = pmHoPrepOutS1RejInterEnbIntraFreqGroupResult(fileName);
            /*16B counter*/
            final String grouppmPrbUlUtil = pmPrbUlUtilGroupResult(fileName);
            final String grouppmPrbDlUtil = pmPrbDlUtilGroupResult(fileName);
            final String grouppmCceUtil = pmCceUtilGroupResult(fileName);
            
            /*eCSFB*/
            final String grouppmRrcCsfbParRespCdma20001xRtt = pmRrcCsfbParRespCdma20001xRttGroupResult(fileName);
            final String grouppmRrcCsfbParReqCdma20001xRtt = pmRrcCsfbParReqCdma20001xRttGroupResult(fileName);
            final String grouppmRrcUlHOPrepTransferCdma20001xRtt = pmRrcUlHOPrepTransferCdma20001xRttGroupResult(fileName);
            final String grouppmRrcMobFromEUtraCmdCdma20001xRtt = pmRrcMobFromEUtraCmdCdma20001xRttGroupResult(fileName);
            final String grouppmMeasReportCdma20001xRtt = pmMeasReportCdma20001xRttGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOSucc = pmDlS1Cdma2000TunnelingHOSuccGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOFail = pmDlS1Cdma2000TunnelingHOFailGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingNonHO = pmDlS1Cdma2000TunnelingNonHOGroupResult(fileName);

            /*16B I-RAT Counters*/
            final String grouppmHoExeAttAto = pmHoExeAttAtoGroupResult(fileName);
            final String grouppmHoExeSuccAto = pmHoExeSuccAtoGroupResult(fileName);
            final String grouppmHoExeAttSrvccAto = pmHoExeAttSrvccAtoGroupResult(fileName);
            final String grouppmHoExeSuccSrvccAto = pmHoExeSuccSrvccAtoGroupResult(fileName);
            final String grouppmHoExeAttUeMeas = pmHoExeAttUeMeasGroupResult(fileName);
            final String grouppmHoExeAttUeMeasRsrp = pmHoExeAttUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeAttSrvccUeMeas = pmHoExeAttSrvccUeMeasGroupResult(fileName);
            final String grouppmHoExeAttSrvccUeMeasRsrp = pmHoExeAttSrvccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeSuccUeMeas = pmHoExeSuccUeMeasGroupResult(fileName);
            final String grouppmHoExeSuccUeMeasRsrp = pmHoExeSuccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeSuccSrvccUeMeas = pmHoExeSuccSrvccUeMeasGroupResult(fileName);
            final String grouppmHoExeSuccSrvccUeMeasRsrp = pmHoExeSuccSrvccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoPrepAttAto = pmHoPrepAttAtoGroupResult(fileName);
            final String grouppmHoPrepSuccAto = pmHoPrepSuccAtoGroupResult(fileName);
            final String grouppmHoPrepAttSrvccAto = pmHoPrepAttSrvccAtoGroupResult(fileName);
            final String grouppmHoPrepSuccSrvccAto = pmHoPrepSuccSrvccAtoGroupResult(fileName);
            
            final String groupPmRrcConnReest = pmRrcConnReestGroupResult(fileName);
            final String grouppmHoPrepOutFailTo = pmHoPrepOutFailToGroupResult(fileName);
            final String grouppmHoPrepOutAttQci = pmHoPrepOutAttQciGroupResult(fileName);
            final String grouppmHoPrepOutS1AttInterEnb = pmHoPrepOutS1AttInterEnbGroupResult(fileName);
            final String grouppmHoPrepOutS1SuccInterEnb = pmHoPrepOutS1SuccInterEnbGroupResult(fileName);
            final String grouppmHoPrepOutSuccQci = pmHoPrepOutSuccQciGroupResult(fileName);
            final String grouppmHoPrepSuccLteInterFQci = pmHoPrepSuccLteInterFQciGroupResult(fileName);
            
            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnb.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL") || grouppmTaDistr.equals("FAIL")
                    || grouppmNonPlannedPci.equals("FAIL") || grouppmCellHoExeSucc.equals("FAIL") || grouppmCellHoExeAtt.equals("FAIL")
                    || grouppmCellHoPrepSucc.equals("FAIl") || grouppmCellHoPrepAtt.equals("FAIL") || grouppmAnrUeCapFailGeran.equals("FAIL") || grouppmAnrUeCapFailUtran.equals("FAIL")
                    || grouppmAnrUeCapSuccGeran.equals("FAIL") || grouppmErabEstabAttAddedPaLsm.equals("FAIL") || grouppmErabEstabAttInitPaLsm.equals("FAIL")
                    || grouppmErabEstabSuccAddedPaLsm.equals("FAIL") || grouppmErabEstabSuccInitPaLsm.equals("FAIL") || grouppmHoExeAttBlindIntraFreq.equals("FAIL")
                    || grouppmRrcConnEstabFailFailureInRadioProcedure.equals("FAIL") || grouppmRrcConnEstabFailLackOfResources.equals("FAIL") || grouppmRrcConnEstabFailUnspecified.equals("FAIL")
                    || grouppmX2ConnEstabFailOut.equals("FAIL") || grouppmHoPrepOutX2.equals("FAIL") || grouppmHoExeAttLteInter.equals("FAIL")
                    || grouppmHoExeSuccLteInter.equals("FAIL") || grouppmHoPrepAttLteInterFAto.equals("FAIL") || grouppmHoPrepSuccLteInterFAto.equals("FAIL")
                    || grouppmAgMeasSuccCgi.equals("FAIL")|| grouppmDrxMeasSuccCgi.equals("FAIL") || grouppmErabRelFail.equals("FAIL")
                    || grouppmErabRelMme.equals("FAIL")|| grouppmErabRelSuccQci.equals("FAIL") || grouppmHoExec.equals("FAIL") || grouppmHoPrepInRejUtran.equals("FAIL")
                    || grouppmHoPrepInS1Rej.equals("FAIL")|| grouppmHoPrepInX2Rej.equals("FAIL") || grouppmHoPrepOutRejIntraEnbInterFreq.equals("FAIL") || grouppmHoPrepOutRejIntraEnbIntraFreq.equals("FAIL")
                    || grouppmHoPrepOutRejTdScdma.equals("FAIL")|| grouppmHoPrepOutS1RejInterEnbInterFreq.equals("FAIL") || grouppmHoPrepOutS1RejInterEnbIntraFreq.equals("FAIL")
                    || grouppmRrcCsfbParRespCdma20001xRtt.equals("FAIL") || grouppmRrcCsfbParReqCdma20001xRtt.equals("FAIL") || grouppmRrcUlHOPrepTransferCdma20001xRtt.equals("FAIL")
                    || grouppmRrcMobFromEUtraCmdCdma20001xRtt.equals("FAIL") || grouppmMeasReportCdma20001xRtt.equals("FAIL") || grouppmDlS1Cdma2000TunnelingHOSucc.equals("FAIL")
                    || grouppmDlS1Cdma2000TunnelingHOFail.equals("FAIL") || grouppmDlS1Cdma2000TunnelingNonHO.equals("FAIL") || grouppmPrbUlUtil.equals("FAIL") || grouppmPrbDlUtil.equals("FAIL") 
                    || grouppmCceUtil.equals("FAIL") || grouppmHoExeAttAto.equals("FAIL") || grouppmHoExeSuccAto.equals("FAIL") || grouppmHoExeAttSrvccAto.equals("FAIL") || grouppmHoExeSuccSrvccAto.equals("FAIL")
                    || grouppmHoExeAttUeMeas.equals("FAIL") || grouppmHoExeAttUeMeasRsrp.equals("FAIL") || grouppmHoExeAttSrvccUeMeas.equals("FAIL") || grouppmHoExeAttSrvccUeMeasRsrp.equals("FAIL") || grouppmHoExeSuccUeMeas.equals("FAIL")
                    || grouppmHoExeSuccUeMeasRsrp.equals("FAIL") || grouppmHoExeSuccSrvccUeMeas.equals("FAIL") || grouppmHoExeSuccSrvccUeMeasRsrp.equals("FAIL") || grouppmHoPrepAttAto.equals("FAIL") || grouppmHoPrepSuccAto.equals("FAIL")
                    || grouppmHoPrepAttSrvccAto.equals("FAIL") || grouppmHoPrepSuccSrvccAto.equals("FAIL")
                    || groupPmRrcConnReest.equals("FAIL") 
                    || grouppmHoPrepOutFailTo.equals("FAIL") || grouppmHoPrepOutAttQci.equals("FAIL") || grouppmHoPrepOutS1AttInterEnb.equals("FAIL")
                    || grouppmHoPrepOutS1SuccInterEnb.equals("FAIL") || grouppmHoPrepOutSuccQci.equals("FAIL") || grouppmHoPrepSuccLteInterFQci.equals("FAIL")
                    		
            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }
        else if (AppDriver.executionContext == AppDriver.ExecutionContext.SEVENTEEN_A_PMS) {

            final String groupPmAnrMeas = pmAnrMeasGroupResult(fileName);
            final String groupPmAnrNeighbrelAdd = pmAnrNeighbrelAddGroupResult(fileName);
            final String groupPmAnrUeCap = pmAnrUeCapGroupResult(fileName);
            final String groupPmErabEstab = pmErabEstabGroupResult(fileName);
            final String groupPmHoExeAtt = pmHoExeAttGroupResult(fileName);
            final String groupPmHoExeSucc = pmHoExeSuccGroupResult(fileName);
            final String groupPmHoSigExeOut = pmHoSigExeOutGroupResult(fileName);
            final String groupPmNoOfEtws = pmNoOfEtwsGroupResult(fileName);
            final String groupPmProcessorLoad = pmProcessorLoadGroupResult12B(fileName);
            final String groupPmRrcConnEstabFailResult = pmRrcConnEstabFailGenGroupResult(fileName);
            final String groupPmRrcConnEstabTime = pmRrcConnEstabTimeGroupResult(fileName);
            final String groupPmRrcConnReconf = pmRrcConnReconfGroupResult(fileName);
            final String groupPmS1ConnEstab = pmS1ConnEstabGroupResult12B(fileName);
            final String groupPmS1ConnShutdownTime = pmS1ConnShutdownTimeGroupResult12B(fileName);
            final String groupPmS1XXNasTransport = pmS1XXNasTransportGroupResult(fileName);
            final String groupPmUeCtxt = pmUeCtxtGroupResult(fileName);
            final String groupPmX2ConnEstabSucc = pmX2ConnEstabSuccGroupResult(fileName);
            final String groupPmS1ErrorIndEnb = pmS1ErrorIndEnbGroupResult12B(fileName);
            final String groupPmX2ErrorIndIn = pmX2ErrorIndInGroupResult12B(fileName);
            final String groupPmX2ConnEstabFailIn = pmX2ConnEstabFailInGroupResult(fileName);
            final String groupPmX2ConnResetOut = pmX2ConnResetOutGroupResult15A(fileName);
            final String groupPmX2ConnResetIn = pmX2ConnResetInGroupResult12B(fileName);
            final String groupPmS1NasNonDelivIndResult = pmS1NasNonDelivIndGroupResult12B(fileName);
            final String groupPmErabModFail = pmErabModFailGroupResult(fileName);
            final String groupPmErabRelEnb = pmErabRelEnbGroupResult(fileName);
            final String groupPmErabRelAttQci = pmErabRelAttQciGroupResult12B(fileName);
            final String groupPmRrcConnReestAttUeUnknown = pmRrcConnReestAttUeUnknownGroupResult13A(fileName);
            final String groupPmRrcConnReestSuccUeUnknown = pmRrcConnReestSuccUeUnknownGroupResult13A(fileName);
            final String groupPmHoExeInSuccTdScdmaQci = pmHoExeInSuccTdScdmaQciGroupResult(fileName);
            /* Below are 13A Counters */
            final String groupPmAdvCellSupRecoveryCellAtt = pmAdvCellSupRecoveryCellAttGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeAtt = pmAdvCellSupRecoveryNodeAttGroupResult(fileName);
            final String groupPmAdvCellSupDetection = pmAdvCellSupDetectionGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryCellSucc = pmAdvCellSupRecoveryCellSuccGroupResult(fileName);
            final String groupPmAdvCellSupRecoveryNodeSucc = pmAdvCellSupRecoveryNodeSuccGroupResult(fileName);
            final String groupPmAnrNeighbrelDelGeran = pmAnrNeighbrelDelGeranGroupResult(fileName);
            final String groupPmAnrNeighbrelDelUtran = pmAnrNeighbrelDelUtranGroupResult(fileName);
            final String groupPmNoOfCmasRepReq = pmNoOfCmasRepReqGroupResult(fileName);
            final String groupPmNoOfCmasRepSucc = pmNoOfCmasRepSuccGroupResult(fileName);
            final String groupPmNoOfCmasReq = pmNoOfCmasReqGroupResult(fileName);
            final String groupPmNoOfCmasSucc = pmNoOfCmasSuccGroupResult(fileName);
            /* Below are 13B Counters */
            final String grouppmHoExeInSuccUtran = pmHoExeInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepInAttUtran = pmHoPrepInAttUtranGroupResult(fileName);
            final String grouppmHoPrepInSuccUtran = pmHoPrepInSuccUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobUtran = pmHoPrepRejInLicMobUtranGroupResult(fileName);
            final String grouppmHoPrepRejInLicMobInterMode = pmHoPrepRejInLicMobInterModeGroupResult(fileName);
            final String grouppmTimingAdvance = pmTimingAdvanceGroupResult(fileName);
            final String grouppmTaDistr = pmTaDistrGroupResult(fileName);

            /* 14B New Counter Group */
            final String grouppmNonPlannedPci = pmNonPlannedPciGroupResult(fileName);
            final String grouppmCellHoExeSucc = pmCellHoExeSuccGroupResult(fileName);
            final String grouppmCellHoExeAtt = pmCellHoExeAttGroupResult(fileName);
            final String grouppmCellHoPrepSucc = pmCellHoPrepSuccGroupResult(fileName);
            final String grouppmCellHoPrepAtt = pmCellHoPrepAttGroupResult(fileName);

            /* 15A counters Group */
            final String grouppmAnrUeCapFailGeran = pmAnrUeCapFailGeranGroupResult(fileName);
            final String grouppmAnrUeCapFailUtran = pmAnrUeCapFailUtranGroupResult(fileName);
            final String grouppmAnrUeCapSuccGeran = pmAnrUeCapSuccGeranGroupResult(fileName);
            final String grouppmErabEstabAttAddedPaLsm = pmErabEstabAttAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabAttInitPaLsm = pmErabEstabAttInitPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccAddedPaLsm = pmErabEstabSuccAddedPaLsmGroupResult(fileName);
            final String grouppmErabEstabSuccInitPaLsm = pmErabEstabSuccInitPaLsmGroupResult(fileName);
            final String grouppmHoExeAttBlindIntraFreq = pmHoExeAttBlindIntraFreqGroupResult(fileName);
            final String grouppmRrcConnEstabFailFailureInRadioProcedure = pmRrcConnEstabFailFailureInRadioProcedureGroupResult(fileName);
            final String grouppmRrcConnEstabFailLackOfResources = pmRrcConnEstabFailLackOfResourcesGroupResult(fileName);
            final String grouppmRrcConnEstabFailUnspecified = pmRrcConnEstabFailUnspecifiedGroupResult(fileName);
            final String grouppmX2ConnEstabFailOut = pmX2ConnEstabFailOutGroupResult(fileName);
            final String grouppmHoPrepOutX2 = pmHoPrepOutX2GroupResult(fileName);
            final String grouppmX2ErrorIndOut = pmX2ErrorIndOutGroupResult15A(fileName);
            final String grouppmS1ErrorIndMme = pmS1ErrorIndMmeGroupResult15A(fileName);
            /* 16A Counters Group */
            final String grouppmHoExeAttLteInter = pmHoExeAttLteInterGroupResult(fileName);
            final String grouppmHoExeSuccLteInter = pmHoExeSuccLteInterGroupResult(fileName);
            final String grouppmHoPrepAttLteInterFAto = pmHoPrepAttLteInterFAtoGroupResult(fileName);
            final String grouppmHoPrepSuccLteInterFAto = pmHoPrepSuccLteInterFAtoGroupResult(fileName);
            final String grouppmAgMeasSuccCgi = pmAgMeasSuccCgiGroupResult(fileName);
            final String grouppmDrxMeasSuccCgi = pmDrxMeasSuccCgiGroupResult(fileName);
            final String grouppmErabRelFail = pmErabRelFailGroupResult16A(fileName);
            final String grouppmErabRelMme = pmErabRelMmeGroupResult(fileName);
            final String grouppmErabRelSuccQci = pmErabRelSuccQciGroupResult16A(fileName);
            final String grouppmHoExec = pmHoExecGroupResult16A(fileName);
            final String grouppmHoPrepInRejUtran = pmHoPrepInRejUtranGroupResult(fileName);
            final String grouppmHoPrepInS1Rej = pmHoPrepInS1RejGroupResult(fileName);
            final String grouppmHoPrepInX2Rej = pmHoPrepInX2RejGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbInterFreq = pmHoPrepOutRejIntraEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejIntraEnbIntraFreq = pmHoPrepOutRejIntraEnbIntraFreqGroupResult(fileName);
            final String grouppmHoPrepOutRejTdScdma = pmHoPrepOutRejTdScdmaGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbInterFreq = pmHoPrepOutS1RejInterEnbInterFreqGroupResult(fileName);
            final String grouppmHoPrepOutS1RejInterEnbIntraFreq = pmHoPrepOutS1RejInterEnbIntraFreqGroupResult(fileName);
            /*16B counter*/
            final String grouppmPrbUlUtil = pmPrbUlUtilGroupResult(fileName);
            final String grouppmPrbDlUtil = pmPrbDlUtilGroupResult(fileName);
            final String grouppmCceUtil = pmCceUtilGroupResult(fileName);
            
            /*eCSFB*/
            final String grouppmRrcCsfbParRespCdma20001xRtt = pmRrcCsfbParRespCdma20001xRttGroupResult(fileName);
            final String grouppmRrcCsfbParReqCdma20001xRtt = pmRrcCsfbParReqCdma20001xRttGroupResult(fileName);
            final String grouppmRrcUlHOPrepTransferCdma20001xRtt = pmRrcUlHOPrepTransferCdma20001xRttGroupResult(fileName);
            final String grouppmRrcMobFromEUtraCmdCdma20001xRtt = pmRrcMobFromEUtraCmdCdma20001xRttGroupResult(fileName);
            final String grouppmMeasReportCdma20001xRtt = pmMeasReportCdma20001xRttGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOSucc = pmDlS1Cdma2000TunnelingHOSuccGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingHOFail = pmDlS1Cdma2000TunnelingHOFailGroupResult(fileName);
            final String grouppmDlS1Cdma2000TunnelingNonHO = pmDlS1Cdma2000TunnelingNonHOGroupResult(fileName);

            /*16B I-RAT Counters*/
            final String grouppmHoExeAttAto = pmHoExeAttAtoGroupResult(fileName);
            final String grouppmHoExeSuccAto = pmHoExeSuccAtoGroupResult(fileName);
            final String grouppmHoExeAttSrvccAto = pmHoExeAttSrvccAtoGroupResult(fileName);
            final String grouppmHoExeSuccSrvccAto = pmHoExeSuccSrvccAtoGroupResult(fileName);
            final String grouppmHoExeAttUeMeas = pmHoExeAttUeMeasGroupResult(fileName);
            final String grouppmHoExeAttUeMeasRsrp = pmHoExeAttUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeAttSrvccUeMeas = pmHoExeAttSrvccUeMeasGroupResult(fileName);
            final String grouppmHoExeAttSrvccUeMeasRsrp = pmHoExeAttSrvccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeSuccUeMeas = pmHoExeSuccUeMeasGroupResult(fileName);
            final String grouppmHoExeSuccUeMeasRsrp = pmHoExeSuccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoExeSuccSrvccUeMeas = pmHoExeSuccSrvccUeMeasGroupResult(fileName);
            final String grouppmHoExeSuccSrvccUeMeasRsrp = pmHoExeSuccSrvccUeMeasRsrpGroupResult(fileName);
            final String grouppmHoPrepAttAto = pmHoPrepAttAtoGroupResult(fileName);
            final String grouppmHoPrepSuccAto = pmHoPrepSuccAtoGroupResult(fileName);
            final String grouppmHoPrepAttSrvccAto = pmHoPrepAttSrvccAtoGroupResult(fileName);
            final String grouppmHoPrepSuccSrvccAto = pmHoPrepSuccSrvccAtoGroupResult(fileName);
            
            final String groupPmRrcConnReest = pmRrcConnReestGroupResult(fileName);
            final String grouppmHoPrepOutFailTo = pmHoPrepOutFailToGroupResult(fileName);
            final String grouppmHoPrepOutAttQci = pmHoPrepOutAttQciGroupResult(fileName);
            final String grouppmHoPrepOutS1AttInterEnb = pmHoPrepOutS1AttInterEnbGroupResult(fileName);
            final String grouppmHoPrepOutS1SuccInterEnb = pmHoPrepOutS1SuccInterEnbGroupResult(fileName);
            final String grouppmHoPrepOutSuccQci = pmHoPrepOutSuccQciGroupResult(fileName);
            final String grouppmHoPrepSuccLteInterFQci = pmHoPrepSuccLteInterFQciGroupResult(fileName);
            
            if (groupPmAnrMeas.equals("FAIL") || groupPmAnrNeighbrelAdd.equals("FAIL") || groupPmAnrUeCap.equals("FAIL")
                    || groupPmErabEstab.equals("FAIL") || groupPmHoExeAtt.equals("FAIL")
                    || groupPmHoExeSucc.equals("FAIL") || groupPmHoSigExeOut.equals("FAIL") || groupPmNoOfEtws.equals("FAIL")
                    || groupPmProcessorLoad.equals("FAIL") || groupPmRrcConnEstabFailResult.equals("FAIL") || groupPmRrcConnEstabTime.equals("FAIL")
                    || groupPmRrcConnReconf.equals("FAIL") || groupPmS1ConnEstab.equals("FAIL") || groupPmS1ConnShutdownTime.equals("FAIL")
                    || groupPmS1XXNasTransport.equals("FAIL") || groupPmUeCtxt.equals("FAIL") || groupPmX2ConnEstabSucc.equals("FAIL")
                    || groupPmS1ErrorIndEnb.equals("FAIL") || groupPmX2ErrorIndIn.equals("FAIL") || groupPmX2ConnEstabFailIn.equals("FAIL")
                    || groupPmX2ConnResetIn.equals("FAIL") || groupPmS1NasNonDelivIndResult.equals("FAIL") || groupPmErabModFail.equals("FAIL")
                    || groupPmErabRelEnb.equals("FAIL") || groupPmErabRelAttQci.equals("FAIL")
                    || groupPmRrcConnReestAttUeUnknown.equals("FAIL") || groupPmRrcConnReestSuccUeUnknown.equals("FAIL")
                    || groupPmHoExeInSuccTdScdmaQci.equals("FAIL") || groupPmAdvCellSupRecoveryCellAtt.equals("FAIL")
                    || groupPmAdvCellSupRecoveryNodeAtt.equals("FAIL") || groupPmAdvCellSupDetection.equals("FAIL")
                    || groupPmAdvCellSupRecoveryCellSucc.equals("FAIL") || groupPmAdvCellSupRecoveryNodeSucc.equals("FAIL")
                    || groupPmAnrNeighbrelDelGeran.equals("FAIL") || groupPmAnrNeighbrelDelUtran.equals("FAIL")
                    || groupPmNoOfCmasRepReq.equals("FAIL") || groupPmNoOfCmasRepSucc.equals("FAIL") || groupPmNoOfCmasReq.equals("FAIL")
                    || groupPmNoOfCmasSucc.equals("FAIL") || grouppmHoExeInSuccUtran.equals("FAIL") || grouppmHoPrepInAttUtran.equals("FAIL")
                    || grouppmHoPrepInSuccUtran.equals("FAIL") || grouppmHoPrepRejInLicMobUtran.equals("FAIL")
                    || grouppmHoPrepRejInLicMobInterMode.equals("FAIL") || grouppmTimingAdvance.equals("FAIL") || grouppmTaDistr.equals("FAIL")
                    || grouppmNonPlannedPci.equals("FAIL") || grouppmCellHoExeSucc.equals("FAIL") || grouppmCellHoExeAtt.equals("FAIL")
                    || grouppmCellHoPrepSucc.equals("FAIl") || grouppmCellHoPrepAtt.equals("FAIL") || grouppmAnrUeCapFailGeran.equals("FAIL") || grouppmAnrUeCapFailUtran.equals("FAIL")
                    || grouppmAnrUeCapSuccGeran.equals("FAIL") || grouppmErabEstabAttAddedPaLsm.equals("FAIL") || grouppmErabEstabAttInitPaLsm.equals("FAIL")
                    || grouppmErabEstabSuccAddedPaLsm.equals("FAIL") || grouppmErabEstabSuccInitPaLsm.equals("FAIL") || grouppmHoExeAttBlindIntraFreq.equals("FAIL")
                    || grouppmRrcConnEstabFailFailureInRadioProcedure.equals("FAIL") || grouppmRrcConnEstabFailLackOfResources.equals("FAIL") || grouppmRrcConnEstabFailUnspecified.equals("FAIL")
                    || grouppmX2ConnEstabFailOut.equals("FAIL") || grouppmHoPrepOutX2.equals("FAIL") || grouppmHoExeAttLteInter.equals("FAIL")
                    || grouppmHoExeSuccLteInter.equals("FAIL") || grouppmHoPrepAttLteInterFAto.equals("FAIL") || grouppmHoPrepSuccLteInterFAto.equals("FAIL")
                    || grouppmAgMeasSuccCgi.equals("FAIL")|| grouppmDrxMeasSuccCgi.equals("FAIL") || grouppmErabRelFail.equals("FAIL")
                    || grouppmErabRelMme.equals("FAIL")|| grouppmErabRelSuccQci.equals("FAIL") || grouppmHoExec.equals("FAIL") || grouppmHoPrepInRejUtran.equals("FAIL")
                    || grouppmHoPrepInS1Rej.equals("FAIL")|| grouppmHoPrepInX2Rej.equals("FAIL") || grouppmHoPrepOutRejIntraEnbInterFreq.equals("FAIL") || grouppmHoPrepOutRejIntraEnbIntraFreq.equals("FAIL")
                    || grouppmHoPrepOutRejTdScdma.equals("FAIL")|| grouppmHoPrepOutS1RejInterEnbInterFreq.equals("FAIL") || grouppmHoPrepOutS1RejInterEnbIntraFreq.equals("FAIL")
                    || grouppmRrcCsfbParRespCdma20001xRtt.equals("FAIL") || grouppmRrcCsfbParReqCdma20001xRtt.equals("FAIL") || grouppmRrcUlHOPrepTransferCdma20001xRtt.equals("FAIL")
                    || grouppmRrcMobFromEUtraCmdCdma20001xRtt.equals("FAIL") || grouppmMeasReportCdma20001xRtt.equals("FAIL") || grouppmDlS1Cdma2000TunnelingHOSucc.equals("FAIL")
                    || grouppmDlS1Cdma2000TunnelingHOFail.equals("FAIL") || grouppmDlS1Cdma2000TunnelingNonHO.equals("FAIL") || grouppmPrbUlUtil.equals("FAIL") || grouppmPrbDlUtil.equals("FAIL") 
                    || grouppmCceUtil.equals("FAIL") || grouppmHoExeAttAto.equals("FAIL") || grouppmHoExeSuccAto.equals("FAIL") || grouppmHoExeAttSrvccAto.equals("FAIL") || grouppmHoExeSuccSrvccAto.equals("FAIL")
                    || grouppmHoExeAttUeMeas.equals("FAIL") || grouppmHoExeAttUeMeasRsrp.equals("FAIL") || grouppmHoExeAttSrvccUeMeas.equals("FAIL") || grouppmHoExeAttSrvccUeMeasRsrp.equals("FAIL") || grouppmHoExeSuccUeMeas.equals("FAIL")
                    || grouppmHoExeSuccUeMeasRsrp.equals("FAIL") || grouppmHoExeSuccSrvccUeMeas.equals("FAIL") || grouppmHoExeSuccSrvccUeMeasRsrp.equals("FAIL") || grouppmHoPrepAttAto.equals("FAIL") || grouppmHoPrepSuccAto.equals("FAIL")
                    || grouppmHoPrepAttSrvccAto.equals("FAIL") || grouppmHoPrepSuccSrvccAto.equals("FAIL")
                    || groupPmRrcConnReest.equals("FAIL") 
                    || grouppmHoPrepOutFailTo.equals("FAIL") || grouppmHoPrepOutAttQci.equals("FAIL") || grouppmHoPrepOutS1AttInterEnb.equals("FAIL")
                    || grouppmHoPrepOutS1SuccInterEnb.equals("FAIL") || grouppmHoPrepOutSuccQci.equals("FAIL") || grouppmHoPrepSuccLteInterFQci.equals("FAIL")
                    		
            ) {
                result = "FAIL";
            } else {
                result = "PASS";
            }

        }

        return result;
    }

    /**
     * @return Check File Topology Group Results
     */
    @SuppressWarnings("unused")
    public String checktopologyGroupResults(final String fileName) throws FileNotFoundException, IOException, SAXException,
            ParserConfigurationException {
        final ArrayList<String> topResults = AppDriver.topologyResults;
        final String[] topology = common.convertStringArrayListToStringArray(topResults);
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String yesNo = propFile.getPropertiesSingleStringValue("verifyFileTopologyResults", counterPropFileDir);
        String outputResult = null;

        Arrays.sort(topology);
        if (yesNo.equalsIgnoreCase("Y")) {
            for (int i = 0; i < topology.length; i++) {
                if (topology[i].equals("FAIL")) {
                    outputResult = "FAIL";
                    break;
                } else if (topology[i].equals("PASS")) {
                    outputResult = "PASS";
                    break;
                }
            }
        } else {
            outputResult = "Not Selected";
        }
        return fileName;
    }

    /**
     * @return Counter Name Group Results
     */
    public String counterGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result = verifyCounterNames(fileName);
        final String yesNo = propFile.getPropertiesSingleStringValue("counterNames", counterPropFileDir);

        if (yesNo.equalsIgnoreCase("Y")) {
            if (result == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmRrcConnEstab Group ResultS
     */
    public String pmRrcConnEstabGroupResult(final String fileName) throws FileNotFoundException, IOException, SAXException,
            ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmRrcConnEstabSuccValues(fileName);
        final boolean result2 = verifyPmRrcConnEstabTimeSumValues(fileName);
        final boolean result3 = verifyPmRrcConnEstabTimeDistrValues(fileName);
        final boolean result4 = verifyPmRrcConnEstabTimeMaxValues(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmRrcConnEstabSucc", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmRrcConnEstabTimeSum", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmRrcConnEstabTimeDistr", counterPropFileDir);
        final String yesNo4 = propFile.getPropertiesSingleStringValue("pmRrcConnEstabTimeMax", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y") || yesNo4.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true && result4 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmS1ConnEstab Group ResultS
     */
    public String pmS1ConnEstabGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1ConnEstabTimeSumValue(fileName);
        final boolean result2 = verifyPmS1ConnEstabSuccValue(fileName);
        final boolean result3 = verifyPmS1ConnEstabTimeDistrValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1ConnEstabTimeSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmS1ConnEstabSucc", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmS1ConnEstabTimeDistr", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmX2ConnEstab Group ResultS
     */
    public String pmX2ConnEstabGroupResult(final String fileName) throws FileNotFoundException, IOException, SAXException,
            ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result = verifyPmX2ConnEstabSuccValue(fileName);
        final String yesNo = propFile.getPropertiesSingleStringValue("pmX2ConnEstabSucc", counterPropFileDir);

        if (yesNo.equalsIgnoreCase("Y")) {
            if (result == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmS1UlNasTransport Group ResultS
     */
    public String pmS1UlNasTransportGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1UlNasTransportSentValue(fileName);
        final boolean result2 = verifyPmS1DlNasTransportRcvdValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1UlNasTransportSent", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmS1DlNasTransportRcvd", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmUeCtxtRelTime Group ResultS
     */
    public String pmUeCtxtRelTimeGroupResult(final String fileName) throws FileNotFoundException, IOException, SAXException,
            ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmUeCtxtRelTimeSumValue(fileName);
        final boolean result2 = verifyPmUeCtxtRelTimeSampValue(fileName);
        final boolean result3 = verifyPmUeCtxtRelTimeDistrValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeSamp", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeDistr", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmUeCtxtRelTimeS1Ho Group ResultS
     */
    public String pmUeCtxtRelTimeS1HoGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmUeCtxtRelTimeS1HoSumValue(fileName);
        final boolean result2 = verifyPmUeCtxtRelTimeS1HoSampValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeS1HoSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeS1HoSamp", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmUeCtxtRelTimeX2Ho Group ResultS
     */
    public String pmUeCtxtRelTimeX2HoGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmUeCtxtRelTimeX2HoSumValue(fileName);
        final boolean result2 = verifyPmUeCtxtRelTimeX2HoSampValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeX2HoSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelTimeX2HoSamp", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    public String pmS1ConnShutdownTimeResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1ConnShutdownTimeSumValue(fileName);
        final boolean result2 = verifyPmS1ConnShutdownTimeSampValue(fileName);
        final boolean result3 = verifyPmS1ConnShutdownTimeDistrValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1ConnShutdownTimeSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmS1ConnShutdownTimeSamp", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmS1ConnShutdownTimeDistr", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    private String genericCounterGroupResult(String[] counterName, String fileName, boolean printCounterValue) throws SAXException, IOException,
            ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;
        boolean result = true;
        for (String counterElement : counterName) {
            result = result && verifyGenericCounterGroupResult(counterElement, fileName, printCounterValue);
        }
        boolean yesNoFlag = false;
        for (String counterElement : counterName) {
            yesNoFlag = yesNoFlag || propFile.getPropertiesSingleStringValue(counterElement, counterPropFileDir).equalsIgnoreCase("Y");
        }

        if (yesNoFlag) {
            if (result == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmRrcConnReconfGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnReconf" }, fileName, true);
    }

    private String pmRrcConnReestGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnReest" }, fileName, true);
    }

    private String pmHoPrepGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrep" }, fileName, true);
    }

    private String pmAnrMeasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrMeas" }, fileName, true);
    }

    private String pmHoExeInGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeIn" }, fileName, true);
    }

    private String pmNoOfCmasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfCmas" }, fileName, true);
    }

    private String pmAnrNeighbrelAddGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrNeighbrelAdd" }, fileName, true);
    }

    private String pmNoOfEtwsGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfEtws" }, fileName, true);
    }

    private String pmAnrUeCapGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrUeCap" }, fileName, true);
    }

    private String pmHoExeSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSucc" }, fileName, true);
    }

    private String pmHoSigExeOutGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoSigExeOut" }, fileName, true);
    }

    private String pmProcessorLoadGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmProcessorLoad" }, fileName, true);
    }

    private String pmRrcConnEstabFailGenGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnEstabFail" }, fileName, true);
    }

    private String pmRrcConnEstabTimeGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnEstabTime" }, fileName, true);
    }

    private String pmS1ConnEstabGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1ConnEstab" }, fileName, true);
    }

    private String pmS1ConnShutdownTimeGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1ConnShutdownTime" }, fileName, true);
    }

    private String pmS1DlNasTransportGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1DlNasTransport" }, fileName, true);
    }

    private String pmUeCtxtGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmUeCtxt" }, fileName, true);
    }

    private String pmX2ConnEstabSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ConnEstabSucc" }, fileName, true);
    }

    private String pmHoExeAttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAtt" }, fileName, true);
    }

    private String pmHoExecOutGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExecOut" }, fileName, true);
    }

    private String pmErabEstabGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabEstab" }, fileName, true);
    }

    private String pmS1XXNasTransportGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1DlNasTransport", "pmS1UlNasTransport" }, fileName, true);
    }

    private String pmS1ErrorIndEnbGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1ErrorIndEnb" }, fileName, true);
    }

    private String pmX2ErrorIndInGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ErrorIndIn" }, fileName, true);
    }

    private String pmX2ConnEstabFailInGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ConnEstabFailIn" }, fileName, true);
    }

    private String pmX2ConnResetOutGroupResult15A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ConnResetOut" }, fileName, true);
    }

    private String pmX2ConnResetInGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ConnResetIn" }, fileName, true);
    }

    private String pmS1NasNonDelivIndGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1NasNonDelivInd" }, fileName, true);
    }

    private String pmErabModFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabModFail" }, fileName, true);
    }

    private String pmErabRelEnbGrpGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelEnbGrp" }, fileName, true);
    }

    private String pmErabRelAttQciGroupResult12B(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelAttQci" }, fileName, true);
    }

    private String pmHoExecSuccDrxConfigGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExecSuccDrxConfig" }, fileName, true);
    }

    private String pmRrcConnReestAttUeUnknownGroupResult13A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnReestAttUeUnknown" }, fileName, true);
    }

    private String pmRrcConnReestSuccUeUnknownGroupResult13A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnReestSuccUeUnknown" }, fileName, true);
    }

    private String pmHoExeInSuccTdScdmaQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeInSuccTdScdmaQci" }, fileName, true);
    }

    private String pmAdvCellSupRecoveryCellAttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAdvCellSupRecoveryCellAtt" }, fileName, true);
    }

    private String pmAdvCellSupRecoveryNodeAttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAdvCellSupRecoveryNodeAtt" }, fileName, true);
    }

    private String pmAdvCellSupDetectionGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAdvCellSupDetection" }, fileName, true);
    }

    private String pmAdvCellSupRecoveryCellSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAdvCellSupRecoveryCellSucc" }, fileName, true);
    }

    private String pmAdvCellSupRecoveryNodeSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAdvCellSupRecoveryNodeSucc" }, fileName, true);
    }

    private String pmAnrNeighbrelDelGeranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrNeighbrelDelGeran" }, fileName, true);
    }

    private String pmAnrNeighbrelDelUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrNeighbrelDelUtran" }, fileName, true);
    }

    private String pmNoOfCmasRepReqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfCmasRepReq" }, fileName, true);
    }

    private String pmNoOfCmasRepSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfCmasRepSucc" }, fileName, true);
    }

    private String pmNoOfCmasReqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfCmasReq" }, fileName, true);
    }

    private String pmNoOfCmasSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNoOfCmasSucc" }, fileName, true);
    }

    /* 13B counter Methods */
    /* 14A counter Methods currently reuse 13B ones */

    private String pmHoExeInSuccUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeInSuccUtran" }, fileName, true);
    }

    private String pmHoPrepInAttUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepInAttUtran" }, fileName, true);
    }

    private String pmHoPrepInSuccUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepInSuccUtran" }, fileName, true);
    }

    private String pmHoPrepRejInLicMobUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepRejInLicMobUtran" }, fileName, true);
    }

    private String pmHoPrepRejInLicMobInterModeGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepRejInLicMobInterMode" }, fileName, true);
    }

    /* 14B new Counter Group */

    private String pmNonPlannedPciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmNonPlannedPci" }, fileName, true);
    }

    private String pmCellHoExeSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmCellHoExeSucc" }, fileName, true);
    }

    private String pmCellHoExeAttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmCellHoExeAtt" }, fileName, true);
    }

    private String pmCellHoPrepSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmCellHoPrepSucc" }, fileName, true);
    }

    private String pmCellHoPrepAttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmCellHoPrepAtt" }, fileName, true);
    }
    
    private String pmHoPrepOutAttQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutAttQci" }, fileName, true);
    }
    
    private String pmHoPrepOutFailToGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutFailTo" }, fileName, true);
    }
    
    private String pmHoPrepOutS1AttInterEnbGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutS1AttInterEnb" }, fileName, true);
    }
    
    private String pmHoPrepOutS1SuccInterEnbGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutS1SuccInterEnb" }, fileName, true);
    }
    
    private String pmHoPrepOutSuccQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutSuccQci" }, fileName, true);
    }
    
    private String pmHoPrepSuccLteInterFQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepSuccLteInterFQci" }, fileName, true);
    }

    private String pmTimingAdvanceGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmTimingAdvance(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmTimingAdvance", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmTaDistrGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmTaDistr(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmTaDistr", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    /* 13B Methods End */

    private String pmX2ErrorIndOutGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmX2ErrorIndOutGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmX2ErrorIndOut", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmX2ErrorIndInGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmX2ErrorIndInGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmX2ErrorIndIn", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmX2ConnResetInGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmX2ConnResetInGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmX2ConnResetIn", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmX2ConnResetOutGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmX2ConnResetOutGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmX2ConnResetOut", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmS1ErrorIndMmeGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1ErrorIndMmeGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1ErrorIndMme", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmS1ErrorIndEnbGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1ErrorIndEnbGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1ErrorIndEnb", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmErabRelSuccQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabRelSuccQciGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabRelSuccQci", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmErabRelAttQciGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabRelAttQciGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabRelAttQci", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmErabRelFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabRelFailGroupResult(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabRelFail", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmUeCtxtRelS1ResetResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmUeCtxtRelS1ResetValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmUeCtxtRelS1Reset", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmErabEstabFailInitGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabEstabFailInitCauseValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabEstabFailInit", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmX2ConnEstabFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmX2ConnEstabFailIn(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmX2ConnEstabFail", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmS1ConnEstabFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1ConnEstabFail(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1ConnEstabFail", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmS1NasNonDelivIndGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmS1NasNonDelivInd(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmS1NasNonDelivInd", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmRrcConnEstabFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmRrcConnEstabFail(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmRrcConnEstabFail", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmHoExecGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmHoExecAttDrxValue(fileName);
        final boolean result2 = verifyPmHoExecSuccDrxValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmHoExecAttDrx", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmHoExecSuccDrx", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    private String pmErabEstabFailAddedGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabEstabFailAddedRADIO_NETWORK_LAYER_GROUPCauseValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabEstabFailAdded", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y")) {
            if (result1 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;

    }

    /* 15A counters method */


    private String pmAnrUeCapFailGeranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrUeCapFailGeran" }, fileName, true);
    }

    private String pmAnrUeCapFailUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrUeCapFailUtran" }, fileName, true);
    }

    private String pmAnrUeCapSuccGeranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAnrUeCapSuccGeran" }, fileName, true);
    }

    private String pmErabEstabAttAddedPaLsmGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabEstabAttAddedPaLsm" }, fileName, true);
    }

    private String pmRrcConnEstabFailUnspecifiedGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnEstabFailUnspecified" }, fileName, true);
    }

    private String pmErabEstabSuccAddedPaLsmGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabEstabSuccAddedPaLsm" }, fileName, true);
    }

    private String pmErabEstabSuccInitPaLsmGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabEstabSuccInitPaLsm" }, fileName, true);
    }

    private String pmHoExeAttBlindIntraFreqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttBlindIntraFreq" }, fileName, true);
    }

    private String pmRrcConnEstabFailFailureInRadioProcedureGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnEstabFailFailureInRadioProcedure" }, fileName, true);
    }

    private String pmRrcConnEstabFailLackOfResourcesGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcConnEstabFailLackOfResources" }, fileName, true);
    }

    private String pmErabEstabAttInitPaLsmGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabEstabAttInitPaLsm" }, fileName, true);
    }

    private String pmX2ConnEstabFailOutGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ConnEstabFailOut" }, fileName, true);
    }

    private String pmHoPrepOutX2GroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutX2" }, fileName, true);
    }

    private String pmS1ErrorIndMmeGroupResult15A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmS1ErrorIndMme" }, fileName, true);
    }

    private String pmX2ErrorIndOutGroupResult15A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmX2ErrorIndOut" }, fileName, true);
    }
    
    /* 16A Counters Group */

    private String pmHoExeAttLteInterGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttLteInter" }, fileName, true);
    }
    
    private String pmHoExeSuccLteInterGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccLteInter" }, fileName, true);
    }

    private String pmHoPrepAttLteInterFAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepAttLteInterFAto" }, fileName, true);
    }

    private String pmHoPrepSuccLteInterFAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepSuccLteInterFAto" }, fileName, true);
    }

    private String pmAgMeasSuccCgiGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmAgMeasSuccCgi" }, fileName, true);
    }

    private String pmDrxMeasSuccCgiGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmDrxMeasSuccCgi" }, fileName, true);
    }
    
    private String pmErabRelEnbGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelEnb" }, fileName, true);
    }
    
    private String pmErabRelFailGroupResult16A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelFail" }, fileName, true);
    }
    
    private String pmErabRelMmeGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelMme" }, fileName, true);
    }
    
    private String pmErabRelSuccQciGroupResult16A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmErabRelSuccQci" }, fileName, true);
    }
    
    private String pmHoExecGroupResult16A(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExec" }, fileName, true);
    }
    
    private String pmHoPrepInRejUtranGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepInRejUtran" }, fileName, true);
    }
    
    private String pmHoPrepInS1RejGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepInS1Rej" }, fileName, true);
    }
    
    private String pmHoPrepInX2RejGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepInX2Rej" }, fileName, true);
    }
    
    private String pmHoPrepOutRejIntraEnbInterFreqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutRejIntraEnbInterFreq" }, fileName, true);
    }
    
    private String pmHoPrepOutRejIntraEnbIntraFreqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutRejIntraEnbIntraFreq" }, fileName, true);
    }
    
    private String pmHoPrepOutRejTdScdmaGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutRejTdScdma" }, fileName, true);
    }
    
    private String pmHoPrepOutS1RejInterEnbInterFreqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutS1RejInterEnbInterFreq" }, fileName, true);
    }
    
    private String pmHoPrepOutS1RejInterEnbIntraFreqGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepOutS1RejInterEnbIntraFreq" }, fileName, true);
    }
    

    private String pmRrcCsfbParRespCdma20001xRttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcCsfbParRespCdma20001xRtt" }, fileName, true);
    }

    private String pmRrcCsfbParReqCdma20001xRttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcCsfbParReqCdma20001xRtt" }, fileName, true);
    }

    private String pmRrcUlHOPrepTransferCdma20001xRttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcUlHOPrepTransferCdma20001xRtt" }, fileName, true);
    }

    private String pmRrcMobFromEUtraCmdCdma20001xRttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmRrcMobFromEUtraCmdCdma20001xRtt" }, fileName, true);
    }

    private String pmMeasReportCdma20001xRttGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmMeasReportCdma20001xRtt" }, fileName, true);
    }

    private String pmDlS1Cdma2000TunnelingHOSuccGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmDlS1Cdma2000TunnelingHOSucc" }, fileName, true);
    }

    private String pmDlS1Cdma2000TunnelingHOFailGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmDlS1Cdma2000TunnelingHOFail" }, fileName, true);
    }

    private String pmDlS1Cdma2000TunnelingNonHOGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmDlS1Cdma2000TunnelingNonHO" }, fileName, true);
    }

    /**
     * @return pmErabEstabTimeInitQCI Group ResultS
     */
    public String pmErabEstabTimeInitQCIGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabEstabTimeInitSumQciValue(fileName);
        final boolean result2 = verifyPmErabEstabTimeInitDistrQciValue(fileName);
        final boolean result3 = verifyPmErabEstabTimeInitMaxQciValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitSumQci", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitDistrQci", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitMaxQci", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmErabEstabTimeAddedQCI Group ResultS
     */
    public String pmErabEstabTimeAddedQCIGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmErabEstabTimeAddedSumQciValue(fileName);
        final boolean result2 = verifyPmErabEstabTimeAddedDistrQciValue(fileName);
        final boolean result3 = verifyPmErabEstabTimeAddedMaxQciValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitSumQci", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitDistrQci", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmErabEstabTimeInitMaxQci", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmHoSigExeOutTimeS1 Group ResultS
     */
    public String pmHoSigExeOutTimeS1GroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmHoSigExeOutTimeS1SumValue(fileName);
        final boolean result2 = verifyPmHoSigExeOutTimeS1SampValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmHoSigExeOutTimeS1Sum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmHoSigExeOutTimeS1Samp", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmHoSigExeOutTimeX2 Group ResultS
     */
    public String pmHoSigExeOutTimeX2GroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmHoSigExeOutTimeX2SumValue(fileName);
        final boolean result2 = verifyPmHoSigExeOutTimeX2SampValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmHoSigExeOutTimeX2Sum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmHoSigExeOutTimeX2Samp", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }

    /**
     * @return pmProcessorLoad Group ResultS
     */
    public String pmProcessorLoadGroupResult(final String fileName) throws SAXException, IOException, ParserConfigurationException {
        final String counterPropFileDir = propFile.getAppPropSingleStringValue(counterPropKeyName);
        final String outputResult;

        final boolean result1 = verifyPmProcessorLoadSumValue(fileName);
        final boolean result2 = verifyPmProcessorLoadSampValue(fileName);
        final boolean result3 = verifyPmProcessorLoadMaxValue(fileName);

        final String yesNo1 = propFile.getPropertiesSingleStringValue("pmProcessorLoadSum", counterPropFileDir);
        final String yesNo2 = propFile.getPropertiesSingleStringValue("pmProcessorLoadSamp", counterPropFileDir);
        final String yesNo3 = propFile.getPropertiesSingleStringValue("pmProcessorLoadMax", counterPropFileDir);

        if (yesNo1.equalsIgnoreCase("Y") || yesNo2.equalsIgnoreCase("Y") || yesNo3.equalsIgnoreCase("Y")) {
            if (result1 == true && result2 == true && result3 == true) {
                outputResult = "PASS";
                AppDriver.allCounterResults.add(outputResult);
            } else {
                outputResult = "FAIL";
                AppDriver.allCounterResults.add(outputResult);
            }
        } else {
            outputResult = "Not Selected";
            AppDriver.allCounterResults.add(outputResult);
        }
        return outputResult;
    }
    /*16B*/
    private String pmPrbUlUtilGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmPrbUlUtil" }, fileName, true);
    }
    private String pmPrbDlUtilGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmPrbDlUtil" }, fileName, true);
    }
    private String pmCceUtilGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmCceUtil" }, fileName, true);
    }
    private String pmHoExeAttAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttAto" }, fileName, true);
    }
    private String pmHoExeSuccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccAto" }, fileName, true);
    }
    private String pmHoExeAttSrvccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttSrvccAto" }, fileName, true);
    }
    private String pmHoExeSuccSrvccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccSrvccAto" }, fileName, true);
    }
    private String pmHoExeAttUeMeasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttUeMeas" }, fileName, true);
    }
    private String pmHoExeAttUeMeasRsrpGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttUeMeasRsrp" }, fileName, true);
    }
    private String pmHoExeAttSrvccUeMeasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttSrvccUeMeas" }, fileName, true);
    }
    private String pmHoExeAttSrvccUeMeasRsrpGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeAttSrvccUeMeasRsrp" }, fileName, true);
    }
    private String pmHoExeSuccUeMeasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccUeMeas" }, fileName, true);
    }
    private String pmHoExeSuccUeMeasRsrpGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccUeMeasRsrp" }, fileName, true);
    }
    private String pmHoExeSuccSrvccUeMeasGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccSrvccUeMeas" }, fileName, true);
    }
    private String pmHoExeSuccSrvccUeMeasRsrpGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoExeSuccSrvccUeMeasRsrp" }, fileName, true);
    }
    private String pmHoPrepAttAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepAttAto" }, fileName, true);
    }
    private String pmHoPrepSuccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepSuccAto" }, fileName, true);
    }
    private String pmHoPrepAttSrvccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepAttSrvccAto" }, fileName, true);
    }
    private String pmHoPrepSuccSrvccAtoGroupResult(String fileName) throws SAXException, IOException, ParserConfigurationException {
        return genericCounterGroupResult(new String[] { "pmHoPrepSuccSrvccAto" }, fileName, true);
    }
}
