/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.constants;

/**
 * @author eseuhon
 * @since 2011
 * 
 */
public class FailureReasonStringConstants {

	public static final String DATA_INTEGRITY_CHECK_FAILED = "Data Integrity Check Failed";

	public static final String NO_DATA = "No Data";

	public static final String REQUEST_TIMEOUT = "Request Timeout";

	public static final String EVENT_TYPE_NA_FOR_IMSI = "Event Type N/A for IMSI";

	public static final String NON_MATCHING_VALUES = "Non matching Values";

	public static final String DIFFERING_NUMBER_OF_EVENT = "Differing number of events";

	public static final String HEADER_MISMATCH = "Table Header Mismatch";
	
	public static final String COLUMN_HEADER_MISMATCH = "Column Header Mismatch";
	
	public static final String DROPDOWN_MISMATCH = "Dropdown Option Mismatch";

	public static final String HISTORY_DROP_DOWN_MISMATCH = "Unexpected field(s) in History Drop Down";

	public static final String TIME_BUTTON_NOT_DISABLED = "Temporal Reference is editable. Time Range button should be disabled";
	
    public static final String UI_NAVIGATION_FAILED = "GUI Navigation Failed";
    
    public static final String IMPACTED_SUBSRIBERS_GREATER_THAN_NO_OF_FAILURES = "The number of Impacted Subscribers exceeds the number of Failures";
    
    public static final String TEMPORAL_REFERENCE_DIFFERENT_THAN_EXPECTED = "The actual Time Range is different from the expected Time Range";
    
    public static final String INVALID_EVENT_TIME_IN_GUI_DATA = "An event in the GUI has an invalid date/time for this search.";
    
    public static final String NOT_A_GROUP_MEMBER = "At least one element from the GUI is not a member of this reserved data group";
    
    
}
