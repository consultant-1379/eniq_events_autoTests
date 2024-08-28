/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.constants;

/**
 * @author EIKRWAQ
 * @since 2013
 *
 */
public class WCDMASqlQueriesConstants {

    public static final String START_DATE_TIME_REGEX = "\\$startDate";

    public static final String END_DATE_TIME_REGEX = "\\$endDate";
    
    public static final String CFA_RANKING_BY_CONTROLLER_QUERY = "SELECT TOP 10 RANK() OVER (ORDER BY FAILURES DESC) AS Rank, VENDOR AS RAN_Vendor,HIERARCHY_3 AS RNC, FAILURES As Failures FROM "
            + "( SELECT HIER3_ID, COUNT (*) AS FAILURES FROM "
            + "( SELECT HIER3_ID FROM DC.EVENT_E_RAN_CFA_ERR_RAW WHERE "
            + "DATETIME_ID >= '$startDate' "
            + "AND "
            + "DATETIME_ID < '$endDate' "
            + "AND ISNULL(TAC,-1) "
            + "NOT IN "
            + "( SELECT TAC FROM DC.GROUP_TYPE_E_TAC WHERE GROUP_NAME = 'EXCLUSIVE_TAC') ) AS RAWVIEW "
            + "WHERE HIER3_ID IS NOT NULL GROUP BY HIER3_ID ) AS RESULT_DATA "
            + "LEFT OUTER JOIN "
            + "( SELECT DISTINCT HIERARCHY_3, HIER3_ID, VENDOR FROM DIM_E_SGEH_HIER321 WHERE RAT=1) AS HIER_TABLE"
            + " ON " + "RESULT_DATA.HIER3_ID = HIER_TABLE.HIER3_ID";

 
}
