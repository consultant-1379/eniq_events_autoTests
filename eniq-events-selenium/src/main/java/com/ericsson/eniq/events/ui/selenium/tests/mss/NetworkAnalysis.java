package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.constants.GuiStringConstants;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NetworkAnalysis {

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    /* This method is to identify whether the kpi analysis is valid for 
     * the selected field or not.
     * 
     * @param completeUITableValues   - The MSS voice related data fetched 
     *                                  from CSV. 
     * @param defaultKPIHeaders       - The ranking headers of the ranking
     *                                  analysis selected.
     * @param kpiAnalysisfield        - Field name for which KPI Analysis
     *                                  to be performed.
     * @param kpiAnalysisFieldValue   - Corresponding field value for the
     *                                  KPI Analysis field.  
     * @param eventTypes              - The event types involved in analysis.
     * @param duration                - Time framed selected.
     *  
     * @return kpiAnalysisValid - Return true if Kpianalysis is valid else 
     *                            false.
     */
    public static boolean kpiAnalysis(final List<Map<String, String>> completeUITableValues,
            final List<String> defaultKPIHeaders, final String kpiAnalysisfield, final String kpiAnalysisFieldValue,
            final List<String> eventTypes, final int duration, final String validationString) {
        final List<List<String>> completeCSVData = GeneralUtil.getCSVDataValues();
        final List<String> kpiAnalysisCSVData = getKPIAnalysis(completeCSVData, kpiAnalysisfield,
                kpiAnalysisFieldValue, defaultKPIHeaders, eventTypes, duration, validationString);
        boolean kpiAnalysisValid = true;
        final List<List<String>> formattedKpiAnalysisUIData = GeneralUtil.formatUIFailureAnalysisData(
                completeUITableValues, defaultKPIHeaders);
        int iterationCount = 0;
        final int headerSize = defaultKPIHeaders.size();
        final int csvDataListSize = kpiAnalysisCSVData.size();
       

        for (final List<String> kpiAnalysisData : formattedKpiAnalysisUIData) {
            kpiAnalysisData.set(0, "0.0");
           
            final int uiDataListSize = kpiAnalysisData.size();
            if ((headerSize == csvDataListSize) && (headerSize == uiDataListSize)) {
                final double UIData = Double.parseDouble(kpiAnalysisData.get(iterationCount));
                final double CSVData = Double.parseDouble(kpiAnalysisCSVData.get(iterationCount));
                if (CSVData != UIData) {
                    kpiAnalysisValid = false;
                    logger.log(Level.INFO, "Failed at row " + iterationCount + " and corresponding row data on UI is "
                            + kpiAnalysisData);
                    break;
                }
            } else {
                kpiAnalysisValid = false;
                logger.log(Level.INFO, "Failed at row " + iterationCount + " " + "and corresponding row data on UI is "
                        + kpiAnalysisData);
                break;
            }
            iterationCount++;
            if (headerSize == iterationCount) {
                iterationCount = 0;
            }
        }

        return kpiAnalysisValid;
    }

    /* This method is to form the KPI analysis data based on the CSV data.
     * 
     * @param completeCSVData         - Contains the complete CSV data as
     *                                  List. 
     * @param kpiAnalysisfield        - Field name for which KPI Analysis
     *                                  to be performed.
     * @param kpiAnalysisFieldValue   - Corresponding field value for the
     *                                  KPI Analysis field.
     * @param defaultKPIHeaders       - The ranking headers of the ranking
     *                                  analysis selected.
     * @param eventTypes              - The event types involved in analysis.
     *  
     * @return kpiAnalysisValid - Return true if Kpianalysis is valid else 
     *                            false.
     */

    final static List<String> getKPIAnalysis(final List<List<String>> completeCSVData, final String kpiAnalysisfield,
            final String kpiAnalysisFieldValue, final List<String> defaultKPIHeaders, final List<String> eventTypes,
            final int duration, final String validationString) {
        final List<String> kpiAnalysisData = new ArrayList<String>();
        final HashSet<String> subscribers = new HashSet<String>();
        for (final String Header : defaultKPIHeaders) {
            kpiAnalysisData.add("0.0");
        }
        float manipulatedDuration = 1;
        if (duration > 720) {
            manipulatedDuration = 15;
        }
        for (final String eventType : eventTypes) {
            List<List<String>> csvData = new ArrayList<List<String>>();
            if (validationString.equalsIgnoreCase(DataIntegrityConstants.KPI_ANALYSIS)) {
                csvData = getCSVDataBasedOnAnalysisField(completeCSVData, kpiAnalysisfield, kpiAnalysisFieldValue);
            } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.KPI_GROUP_ANALYSIS)) {
                final List<String> kpiAnalysisFieldValueList = new ArrayList<String>(
                        Arrays.asList(kpiAnalysisFieldValue.split(DataIntegrityConstants.HYPHEN_SYMBOL)));
                csvData = getCSVDataBasedOnAnalysisFieldForGroups(completeCSVData, kpiAnalysisfield,
                        kpiAnalysisFieldValueList);
            }

            final List<List<String>> validateList = new ArrayList<List<String>>();
            for (final List<String> rowData : csvData) {
                final String eventResult = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_TYPE));
                if (eventType.equalsIgnoreCase(eventResult)) {
                    validateList.add(rowData);
                }
            }
            float successedEvents = 00;
            float failuredEvents = 00;
            float droppedEvents = 00;

            for (final List<String> rowData : validateList) {
                final String imsi = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI));
                final String eventResult = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_RESULT));
                if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_SUCCESS)) {
                    successedEvents++;
                } else {
                    failuredEvents++;
                    if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_DROPPED)) {
                        droppedEvents++;
                    }
                }
                subscribers.add(imsi);
            }

            final NumberFormat numberFormat = new DecimalFormat();
            numberFormat.setMaximumFractionDigits(2);
            final float impactedSubscribers = Float.parseFloat(numberFormat.format(subscribers.size()));
            final int impactedSubscribersInt = subscribers.size();
            successedEvents = Float.parseFloat(numberFormat.format(successedEvents));
            failuredEvents = Float.parseFloat(numberFormat.format(failuredEvents));
            final float totalEvents = Float.parseFloat(numberFormat.format(successedEvents + failuredEvents));
            droppedEvents = Float.parseFloat(numberFormat.format(droppedEvents));
            int count = 0;
            for (final String header : defaultKPIHeaders) {
                if (header.equalsIgnoreCase(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
                    kpiAnalysisData.set(count, Integer.toString(impactedSubscribersInt));
                }
                if (eventType.equalsIgnoreCase(GuiStringConstants.MSORIGINATING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_CALL_COMPLETION_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_CALL_DROP_RATIO)) {
                        kpiAnalysisData.set(count,
                                Double.toString(GeneralUtil.callDropsRatio(droppedEvents, totalEvents)));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_CALL_ATTEMPTS_INTENSITY)) {
                        kpiAnalysisData.set(count, Double.toString(GeneralUtil.callAttemptsIntensity(totalEvents,
                                subscribers.size(), manipulatedDuration)));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_CALL_DROP_INTENSITY)) {
                        kpiAnalysisData.set(count, Double.toString(GeneralUtil.callDropsIntensity(droppedEvents,
                                impactedSubscribers, manipulatedDuration)));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_COMPLETION_RATIO)) {
                        kpiAnalysisData.set(count, "0.0");
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_ORIGINATING_EMERGENCY_CALL_DROP_RATIO)) {
                        kpiAnalysisData.set(count, "0.0");
                    }

                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSTERMINATING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_TERMINATING_CALL_COMPLETION_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_TERMINATING_CALL_DROP_RATIO)) {
                        kpiAnalysisData.set(count,
                                Double.toString(GeneralUtil.callDropsRatio(droppedEvents, totalEvents)));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_TERMINATING_CALL_ATTEMPTS_INTENSITY)) {
                        kpiAnalysisData.set(count, Double.toString(GeneralUtil.callAttemptsIntensity(totalEvents,
                                subscribers.size(), manipulatedDuration)));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MS_TERMINATING_CALL_DROP_INTENSITY)) {
                        kpiAnalysisData.set(count, Double.toString(GeneralUtil.callDropsIntensity(droppedEvents,
                                impactedSubscribers, manipulatedDuration)));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.CALLFORWARDING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.CALL_FORWARDING_SUCCESS_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.CALL_FORWARDING_DROP_RATIO)) {
                        double callDropsRatio = 0;
                        if (totalEvents != 0) {
                            callDropsRatio = GeneralUtil.callDropsRatio(droppedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callDropsRatio));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.ROAMINGCALLFORWARDING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.ROAMING_CALL_SUCCESS_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.ROAMING_CALL_DROP_RATIO)) {
                        double callDropsRatio = 0;
                        if (totalEvents != 0) {
                            callDropsRatio = GeneralUtil.callDropsRatio(droppedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callDropsRatio));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSORIGINATINGSMSINMSC)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.SMS_ORIGINATING_SUCCESS_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSTERMINATINGSMSINMSC)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.SMS_TERMINATING_SUCCESS_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.LOCATIONSERVICES)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.LOCATION_REQUESTS_SUCCESS_RATIO)) {
                        double callCompletionRatio = 0;
                        if (totalEvents != 0) {
                            callCompletionRatio = GeneralUtil.callCompletionRatio(successedEvents, totalEvents);
                        }
                        kpiAnalysisData.set(count, Double.toString(callCompletionRatio));
                    }
                }
                count++;
            }
        }
        logger.log(Level.INFO, " The kpi analysis data based on CSV : " + kpiAnalysisData.toString());
        return kpiAnalysisData;
    }

    /* 
     * This method is to get the corresponding csv data for the field passed.
     * 
     * @param completeCSVData         - Contains the complete CSV data as
     *                                  List. 
     * @param analysisFieldName       - Field name for which Analysis
     *                                  to be performed.
     * @param analysisFieldValue      - Corresponding field value for the
     *                                  Analysis field.
     *  
     * @return csvData - Contains the corresponding CSV data for the analysis 
     *                   field.
     *                  
     */
    public static List<List<String>> getCSVDataBasedOnAnalysisField(final List<List<String>> completeCSVData,
            final String analysisFieldName, final String analysisFieldValue) {
        List<List<String>> csvData = new ArrayList<List<String>>();

        if (analysisFieldName.equalsIgnoreCase(GuiStringConstants.ACCESS_AREA)) {
            csvData = GeneralUtil.getCSVDataBasedOnAccessArea(completeCSVData, analysisFieldName, analysisFieldValue);
        } else if (analysisFieldName.equalsIgnoreCase(GuiStringConstants.CONTROLLER)) {
            csvData = GeneralUtil.getCSVDataBasedOnController(completeCSVData, analysisFieldName, analysisFieldValue);
        } else {
            csvData = GeneralUtil.getCSVDataBasedOnField(completeCSVData, analysisFieldName, analysisFieldValue);
        }
        return csvData;
    }

    /* 
     * This method is to get the corresponding csv data for the group passed.
     * 
     * @param completeCSVData         - Contains the complete CSV data as
     *                                  List. 
     * @param analysisFieldName       - Field name for which Analysis
     *                                  to be performed.
     * @param selectedFieldValueList  - Corresponding field value for the
     *                                  Analysis field.
     *  
     * @return csvData - Contains the corresponding CSV data for the analysis 
     *                   group.              
     */

    public static List<List<String>> getCSVDataBasedOnAnalysisFieldForGroups(final List<List<String>> completeCSVData,
            final String AnalysisfieldName, final List<String> selectedFieldValueList) {
        final List<List<String>> consolidatedCsvData = new ArrayList<List<String>>();

        if (AnalysisfieldName.equalsIgnoreCase(GuiStringConstants.ACCESS_AREA)) {
            for (final String selectedFieldValue : selectedFieldValueList) {
                final List<List<String>> accessAreaDataList = GeneralUtil.getCSVDataBasedOnAccessArea(completeCSVData,
                        AnalysisfieldName, selectedFieldValue);
                for (final List<String> accessArea : accessAreaDataList) {
                    consolidatedCsvData.add(accessArea);
                }
            }
        } else if (AnalysisfieldName.equalsIgnoreCase(GuiStringConstants.CONTROLLER)) {
            for (final String selectedFieldValue : selectedFieldValueList) {
                final List<List<String>> controllerDataList = GeneralUtil.getCSVDataBasedOnController(completeCSVData,
                        AnalysisfieldName, selectedFieldValue);
                for (final List<String> controller : controllerDataList) {
                    consolidatedCsvData.add(controller);
                }
            }
        } else {
            for (final String selectedFieldValue : selectedFieldValueList) {
                final List<List<String>> selectedFieldDataList = GeneralUtil.getCSVDataBasedOnField(completeCSVData,
                        AnalysisfieldName, selectedFieldValue);
                for (final List<String> selectedFieldData : selectedFieldDataList) {
                    consolidatedCsvData.add(selectedFieldData);
                }
            }
        }

        return consolidatedCsvData;
    }

    /* This method is to identify whether the cause code analysis summary 
     * displayed on the UI is valid or not. 
     * 
     * @param completeUITableValues      - The analysis data displayed on UI. 
     * @param searchFieldName            - For field in Which cause code 
     *                                     analysis to be performed.
     * @param searchFieldValue           - For field in Which cause code 
     *                                     analysis to be performed                                     
     * @param defaultCauseCodeHeaders    - The headers for the CauseCode 
     *                                     analysis selected.
     * @param duration     - Time framed selected.
     *  
     * @return isCauseCodeAnalysisValid - Returns true if the UI data and csv 
     *                                    analysis data matches else false.
     */

    public static boolean causeCodeAnalysis(final List<Map<String, String>> completeUITableValues,
            final String searchFieldName, final String searchFieldValue, final List<String> defaultCauseCodeHeaders,
            final int duration) {
        final List<List<String>> voiceEventsRelatedCSVData = GeneralUtil.getVoiceRelatedCsvData();
        final List<List<String>> csvList = getCSVDataBasedOnAnalysisField(voiceEventsRelatedCSVData, searchFieldName,
                searchFieldValue);
        final List<List<String>> internalCauseCodeCSVAnalysisData = getInternalCauseCodeAnalysisForVoice(csvList,
                defaultCauseCodeHeaders, duration);
        final boolean isCauseCodeAnalysisValid = (GeneralUtil.formatUIFailureAnalysisData(completeUITableValues,
                defaultCauseCodeHeaders)).containsAll(internalCauseCodeCSVAnalysisData);
        return isCauseCodeAnalysisValid;
    }

    /* 
     * This method is to identify the analysis data for the Cause Code analysis
     * based on the CSV data. 
     * 
     * @param mssVoiceRelatedCsvDataList - The MSS voice related data fetched 
     *                                     from CSV. 
     * @param defaultCauseCodeHeaders    - The cause code headers of the cause
     *                                     code analysis selected.
     * @param duration                   - Time framed selected.
     *  
     * @return allInternalCauseCodeAnalysisData -Contains the Internal Cause 
     *                                          Code analysis data.
     */

    final static List<List<String>> getInternalCauseCodeAnalysisForVoice(
            final List<List<String>> mssVoiceRelatedCsvDataList, final List<String> defaultCauseCodeHeaders,
            final int duration) {
        final List<List<String>> allInternalCauseCodeAnalysisData = new ArrayList<List<String>>();
        final String successStatus = DataIntegrityConstants.STATUS_SUCCESS;
        final int internalCauseCodeIDIndex = 0;
        final int faultCodeIDIndex = 1;
        final int faultCauseCodeIndex = 2;
        final int recommendedActionIndex = 3;
        final HashSet<List<String>> internalCauseCodeList = new HashSet<List<String>>();
        for (final List<String> rowData : mssVoiceRelatedCsvDataList) {
            final String internalCauseCodeID = rowData.get(GeneralUtil
                    .getReservedCSVDataHeaderIndex(GuiStringConstants.INTERNAL_CAUSE_CODE_ID));
            final String faultCodeID = rowData.get(GeneralUtil
                    .getReservedCSVDataHeaderIndex(GuiStringConstants.FAULT_CODE_ID));
            final String faultCauseCode = rowData.get(GeneralUtil
                    .getReservedCSVDataHeaderIndex(GuiStringConstants.FAULT_CODE));
            final String recommendedAction = rowData.get(GeneralUtil
                    .getReservedCSVDataHeaderIndex(GuiStringConstants.RECOMMENDED_ACTION));
            final String eventStatus = rowData.get(GeneralUtil
                    .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_RESULT));
            if (!eventStatus.equalsIgnoreCase(DataIntegrityConstants.STATUS_SUCCESS)) {
                final List<String> internalCauseCode = new ArrayList<String>();
                internalCauseCode.add(internalCauseCodeID);
                internalCauseCode.add(faultCodeID);
                internalCauseCode.add(faultCauseCode);
                internalCauseCode.add(recommendedAction);
                internalCauseCodeList.add(internalCauseCode);
            }
        }
        logger.log(Level.INFO, "The Internal Cause Code are : " + internalCauseCodeList);
        for (final List<String> internalCauseCode : internalCauseCodeList) {
            final List<String> singleCauseCodeRankingData = new ArrayList<String>();
            int totalCount = 0;
            final HashSet<String> impactedSubscribers = new HashSet<String>();
            final String internalCauseCodeID = internalCauseCode.get(internalCauseCodeIDIndex);
            final String faultCodeID = internalCauseCode.get(faultCodeIDIndex);
            final String faultCauseCode = internalCauseCode.get(faultCauseCodeIndex);
            final String recommendedAction = internalCauseCode.get(recommendedActionIndex);
            for (final List<String> rowData : mssVoiceRelatedCsvDataList) {
                final String csvSelectedHeaderValue = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.INTERNAL_CAUSE_CODE_ID));
                final String csvEventstatus = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_RESULT));
                if (internalCauseCodeID.equalsIgnoreCase(csvSelectedHeaderValue)) {
                    if (DataIntegrityConstants.STATUS_DROPPED.equalsIgnoreCase(csvEventstatus)
                            || DataIntegrityConstants.STATUS_BLOCKED.equalsIgnoreCase(csvEventstatus)) {
                        totalCount++;
                    }
                    final String imsiData = rowData.get(GeneralUtil
                            .getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI));
                    logger.log(Level.INFO, "The imsiData : " + imsiData);
                    impactedSubscribers.add(rowData.get(GeneralUtil
                            .getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI)));
                }
            }
            for (final String header : defaultCauseCodeHeaders) {
                if (header.equalsIgnoreCase(GuiStringConstants.INTERNAL_CAUSE_CODE_ID)) {
                    singleCauseCodeRankingData.add(internalCauseCodeID);
                } else if (header.equalsIgnoreCase(GuiStringConstants.FAULT_CODE_ID)) {
                    singleCauseCodeRankingData.add(faultCodeID);
                } else if (header.equalsIgnoreCase(GuiStringConstants.FAULT_CODE)) {
                    singleCauseCodeRankingData.add(faultCauseCode);
                } else if (header.equalsIgnoreCase(GuiStringConstants.RECOMMENDED_ACTION)) {
                    singleCauseCodeRankingData.add(recommendedAction);
                } else if (header.equalsIgnoreCase(GuiStringConstants.OCCURRENCES)) {
                    singleCauseCodeRankingData.add(Integer.toString(totalCount * duration));
                } else if (header.equalsIgnoreCase(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
                    singleCauseCodeRankingData.add(Integer.toString(impactedSubscribers.size()));
                }

            }
            allInternalCauseCodeAnalysisData.add(singleCauseCodeRankingData);
        }
        logger.log(Level.INFO, "InternalCauseCode Ranking Data :" + allInternalCauseCodeAnalysisData.toString());
        return allInternalCauseCodeAnalysisData;
    }

    /* 
     * This method is to identify the analysis data in UI is valid or not.
     * 
     * @param completeUITableValues      - The analysis data displayed on UI. 
     * @param eventVolumeAnalysisHeaders - The headers for the event volume 
     *                                     analysis selected.
     * @param analysisfield              - For field in Which event volume 
     *                                     analysis to be performed.
     * @param analysisFieldValue         - For field in Which event volume 
     *                                     analysis to be performed                                     
     * @param selectedGroupFieldValue    - The field value for the respective
     *                                     group.                                   
     * @param duration     - Time framed selected.
     *  
     * @return iseventVolumeAnalysis - Returns true if the UI data and csv 
     *                                 analysis data matches else false.
     */
    
   /*for old ui*/
    
    public static boolean eventVolumeAnalysis(final List<Map<String, String>> completeUITableValues,
            final List<String> eventVolumeAnalysisHeaders, final String analysisfield, final String analysisFieldValue,
            final List<String> selectedGroupFieldValue, final List<String> eventTypes, final String validationString) {
        final List<List<String>> completeCSVData = GeneralUtil.getCSVDataValues();
        final List<String> kpiAnalysisCSVData = getEventVolumeAnalysis(completeCSVData, analysisfield,
                analysisFieldValue, selectedGroupFieldValue, eventVolumeAnalysisHeaders, eventTypes, validationString);
        boolean iseventVolumeAnalysis = true;
        final List<List<String>> formattedeventVolumeAnalysisUIData = GeneralUtil.formatUIFailureAnalysisData(
                completeUITableValues, eventVolumeAnalysisHeaders);
        int iterationCount = 0;
        final int headerSize = eventVolumeAnalysisHeaders.size();
        final int csvDataListSize = kpiAnalysisCSVData.size();
        for (final List<String> kpiAnalysisRowData : formattedeventVolumeAnalysisUIData) {
            kpiAnalysisRowData.set(0, "0.0");
            final int uiDataListSize = kpiAnalysisRowData.size();
            logger.log(Level.INFO, " uiDataListSize :" + uiDataListSize + " csvDataListSize :" + csvDataListSize
                    + " headerSize :" + headerSize + " iterationCount :" + iterationCount);
            if ((headerSize == csvDataListSize) && (headerSize == uiDataListSize)) {
                final double UIData = Double.parseDouble(kpiAnalysisRowData.get(iterationCount));
                final double CSVData = Double.parseDouble(kpiAnalysisCSVData.get(iterationCount));
                if (CSVData != UIData) {
                    iseventVolumeAnalysis = false;
                    logger.log(Level.INFO, " Failed at row " + iterationCount);
                    logger.log(Level.INFO, "The CSVData : " + CSVData);
                    logger.log(Level.INFO, "The UIData  : " + UIData);
                    logger.log(Level.INFO, "The CSVData : " + kpiAnalysisCSVData);
                    logger.log(Level.INFO, "The UIData  : " + kpiAnalysisRowData);
                    break;
                }
            } else {
                iseventVolumeAnalysis = false;
                logger.log(Level.INFO, "Failed at row " + iterationCount);
                break;
            }
            iterationCount++;
            if (headerSize == iterationCount) {
                iterationCount = 0;
            }
        }
        return iseventVolumeAnalysis;
    }
    
    
   /*for new UI added argument time label*/
    
   public static boolean eventVolumeAnalysisNewUI(final List<Map<String, String>> completeUITableValues,
            final List<String> eventVolumeAnalysisHeaders, final String analysisfield, final String analysisFieldValue,
            final List<String> selectedGroupFieldValue, final List<String> eventTypes, final String validationString, String timeLabel){    
    	final List<List<String>> completeCSVData = GeneralUtil.getCSVDataValues();
        final List<String> kpiAnalysisCSVData = getEventVolumeAnalysis(completeCSVData, analysisfield,
                analysisFieldValue, selectedGroupFieldValue, eventVolumeAnalysisHeaders, eventTypes, validationString);
        boolean iseventVolumeAnalysis = true;
        final List<List<String>> formattedeventVolumeAnalysisUIData = GeneralUtil.formatUIFailureAnalysisData(
                completeUITableValues, eventVolumeAnalysisHeaders);
        
        final int headerSize = eventVolumeAnalysisHeaders.size();
        final int csvDataListSize = kpiAnalysisCSVData.size();
        
        
        int multiplier = 1;
                
        if(timeLabel.equals("6 hours") || timeLabel.equals("12 hours") ||timeLabel.equals("1 day"))
        	multiplier = 15;
        else if (timeLabel.equals("1 week"))
        	multiplier = 24* 60;
        logger.log(Level.INFO, "multiplier="  + multiplier);
      
        	
        for (final List<String> kpiAnalysisRowData : formattedeventVolumeAnalysisUIData) {
        	
        	for(int iterationCount = 0;iterationCount < headerSize;  iterationCount++){        // added for to loop over coloumns
            kpiAnalysisRowData.set(0, "0.0");
            final int uiDataListSize = kpiAnalysisRowData.size();
            logger.log(Level.INFO, " uiDataListSize :" + uiDataListSize + " csvDataListSize :" + csvDataListSize
                    + " headerSize :" + headerSize + " iterationCount :" + iterationCount);
            if ((headerSize == csvDataListSize) && (headerSize == uiDataListSize)) {
            	 double UIData = Double.parseDouble(kpiAnalysisRowData.get(iterationCount));
            	 
            	if(!eventVolumeAnalysisHeaders.get(iterationCount).equals("Impacted Subscribers"))    
            		
            		//For time greater than 2 hrs UI shows data in group of 15 mins and group og one day in 1 week;
            		 UIData = UIData/multiplier;
            	
            	
            	
            		
                 double CSVData = Double.parseDouble(kpiAnalysisCSVData.get(iterationCount));
              
                if (CSVData  != UIData) {
                    iseventVolumeAnalysis = false;
                    logger.log(Level.INFO, " Failed at row " + iterationCount);
                    logger.log(Level.INFO, "The CSVData : " + CSVData);
                    logger.log(Level.INFO, "The UIData  : " + UIData);
                    logger.log(Level.INFO, "The CSVData : " + kpiAnalysisCSVData);
                    logger.log(Level.INFO, "The UIData  : " + kpiAnalysisRowData);
                    //break;
                    return iseventVolumeAnalysis;
                }
                
              //else {
                //iseventVolumeAnalysis = false;
                //logger.log(Level.INFO, "Failed at row " + iterationCount);
                //break;
              	//}
               // This part is incorrect as iseventVolumeAnalysis always returns false;
            
        	}
        	} 
        }
        return iseventVolumeAnalysis;
    }

    /* 
     * This method is to get the analysis data for the event volume 
     * analysis based on the CSV data. 
     * 
     * @param completeCSVData            - The complete csv data. 
     * @param eventVolumeAnalysisHeaders - The headers for the event volume 
     *                                     analysis selected.
     * @param analysisfield              - For field in Which event volume 
     *                                     analysis to be performed.
     * @param analysisFieldValue         - For field in Which event volume 
     *                                     analysis to be performed                                     
     * @param selectedGroupFieldValue    - The field value for the respective
     *                                     group.                                   
     * @param validationString           - Indicates validation is for single 
     *                                     or Group.
     *  
     * @return iseventVolumeAnalysis - Returns true if the UI data and csv 
     *                                 analysis data matches else false.
     */
    final static List<String> getEventVolumeAnalysis(final List<List<String>> completeCSVData,
            final String analysisfield, final String analysisFieldValue, final List<String> selectedGroupFieldValue,
            final List<String> eventVolumeAnalysisHeaders, final List<String> eventTypes, final String validationString) {
        final List<String> eventVolumeData = new ArrayList<String>();
        final HashSet<String> subscribers = new HashSet<String>();
        int totalNetworkEvents = 0;
        for (final String Header : eventVolumeAnalysisHeaders) {
            eventVolumeData.add("0");
        }
        for (final String eventType : eventTypes) {
            List<List<String>> csvData = new ArrayList<List<String>>();
            if (validationString.equalsIgnoreCase(DataIntegrityConstants.EVENT_VOLUME_ANALYSIS)) {
            	
                csvData = getCSVDataBasedOnAnalysisField(completeCSVData, analysisfield, analysisFieldValue);
               
            }
            if (validationString.equalsIgnoreCase(DataIntegrityConstants.EVENT_VOLUME_GROUP_ANALYSIS)) {
                csvData = getCSVDataBasedOnAnalysisFieldForGroups(completeCSVData, analysisfield,
                        selectedGroupFieldValue);
               
            } else if (validationString.equalsIgnoreCase(DataIntegrityConstants.NETWORK_EVENT_VOLUME_ANALYSIS)) {
                //csvData = completeCSVData;
                csvData = getCSVDataBasedOnAnalysisField(completeCSVData, analysisfield, analysisFieldValue);
               
            }
            final List<List<String>> validateList = new ArrayList<List<String>>();
            for (final List<String> rowData : csvData) {
                final String eventResult = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_TYPE));
                final String EventRourceName = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.MSC));
                if (eventType.equalsIgnoreCase(eventResult)){
                		
                	
                    validateList.add(rowData);
                }
            }
           
            int successCount = 0;
            int failureCount = 0;
            int droppedCount = 0;
            int blockedCount = 0;
            int errorCount = 0;
            for (final List<String> rowData : validateList) {
                final String imsi = rowData.get(GeneralUtil.getReservedCSVDataHeaderIndex(GuiStringConstants.IMSI));
                final String eventResult = rowData.get(GeneralUtil
                        .getReservedCSVDataHeaderIndex(GuiStringConstants.EVENT_RESULT));
                if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_SUCCESS)) {
                    successCount++;
                } else {
                    failureCount++;
                    if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_DROPPED)) {
                        droppedCount++;
                    }
                    if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_BLOCKED)) {
                        blockedCount++;
                    }
                    if (eventResult.equalsIgnoreCase(DataIntegrityConstants.STATUS_ERROR)) {
                        errorCount++;
                    }
                }
                
                subscribers.add(imsi);
            }

            final NumberFormat numberFormat = new DecimalFormat();
            numberFormat.setMaximumFractionDigits(2);
            final int impactedSubscribers = subscribers.size();
            final int totalEvents = successCount + failureCount;
            totalNetworkEvents = totalNetworkEvents + totalEvents;
            int count = 0;
            for (final String header : eventVolumeAnalysisHeaders) {
                if (header.equalsIgnoreCase(GuiStringConstants.IMPACTED_SUBSCRIBERS)) {
                    eventVolumeData.set(count, Integer.toString(impactedSubscribers));
                }
                if (header.equalsIgnoreCase(GuiStringConstants.TOTAL_NETWORK_EVENTS)) {
                    eventVolumeData.set(count, Integer.toString(totalNetworkEvents));
                }
                if (eventType.equalsIgnoreCase(GuiStringConstants.MSORIGINATING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_CALL_COMPLETION_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_CALL_BLOCK_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(blockedCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_CALL_DROP_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(droppedCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_COMPLETION_COUNT)) {

                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_BLOCK_COUNT)) {

                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_EMERGENCY_CALL_DROP_COUNT)) {

                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSTERMINATING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MSTERMINATING_CALL_COMPLETION_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSTERMINATING_CALL_BLOCK_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(blockedCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSTERMINATING_CALL_DROP_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(droppedCount));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.CALLFORWARDING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.CALLFORWARDING_CALL_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.CALL_FORWARDING_CALL_BLOCK_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(blockedCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.CALL_FORWARDING_CALL_DROP_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(droppedCount));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.ROAMINGCALLFORWARDING)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.ROAMINGCALLFORWARDING_CALL_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.ROAMINGCALLFORWARDING_CALL_BLOCK_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(blockedCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.ROAMINGCALLFORWARDING_CALL_DROP_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(droppedCount));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSORIGINATINGSMSINMSC)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_SMS_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSORIGINATING_SMS_FAIL_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(failureCount));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.MSTERMINATINGSMSINMSC)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.MSTERMINATING_SMS_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.MSTERMINATING_SMS_FAIL_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(failureCount));
                    }
                } else if (eventType.equalsIgnoreCase(GuiStringConstants.LOCATIONSERVICES)) {
                    if (header.equalsIgnoreCase(GuiStringConstants.LOCATION_REQUESTS_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(successCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUESTCOUNT)) {
                        eventVolumeData.set(count, Integer.toString(failureCount));
                    }
                    if (header.equalsIgnoreCase(GuiStringConstants.UNSUCCESSFUL_LOCATION_REQUEST_COUNT)) {
                        eventVolumeData.set(count, Integer.toString(failureCount));
                    }
                }
                count++;
            }
        }
        logger.log(Level.INFO, " The Event volume headers are : " + eventVolumeAnalysisHeaders.toString());
        logger.log(Level.INFO, " The Event volume Analysis data are : " + eventVolumeData.toString());
        return eventVolumeData;
    }

    /* This method is to find the event analysis data based on csv and
     * compares whether it matches with the UI or not.
     *
     *@param completeUITableValues - contains the data displayed in UI.
     *@param allEventTypes         - All event types.
     *@param windowHeaders         - contains which header
     *                               fields needs to be validated.
     *@param analysisTypeSelected  - contains which analysis selected.
     *@param selectedFieldValue    - Value of the field selected.
     *@param eventType             - Event type.
     *@return resultOfAnalysis     - Returns whether the data are valid 
     *                               or not. 
     */

    final public static boolean eventAnalysis(final List<Map<String, String>> completeUITableValues,
            final List<String> allEventTypes, final String typeOfAnalysisSelected, final String selectedFieldValue,
            final String eventType, final List<String> WindowHeaders, final int duration) {
        final List<List<String>> completeCSVData = GeneralUtil.getCSVDataValues();
        final List<List<String>> csvDataAsList = getCSVDataBasedOnAnalysisField(completeCSVData,
                typeOfAnalysisSelected, selectedFieldValue);

        final List<List<String>> failureList = new ArrayList<List<String>>();
        for (final String event : allEventTypes) {
            final List<String> completeData = AggregrationHandlerUtil.getEventAnalysisOnSubscriberTab(csvDataAsList,
                    event, duration, WindowHeaders);
            if (!completeData.contains(DataIntegrityConstants.NO_DATA)) {
                failureList.add(completeData);
            }
        }
        logger.log(Level.INFO, "The failure list is :" + failureList);
        final boolean resultOfAnalysis = (GeneralUtil.formatUIFailureAnalysisData(completeUITableValues, WindowHeaders))
                .containsAll(failureList);
        logger.log(Level.INFO, "The result of failure event analysis is : " + resultOfAnalysis);
        return resultOfAnalysis;
    }

}
