/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

import java.util.Date;

/**
 * @author ericker
 * @since 2010
 *
 */
public interface TimeDialogControls {
    public void setTimeRange(TimeRange timeRange) throws PopUpException;

    public String getTimeRange();

    public void setTimeAndDateRange(final Date startDate, final TimeCandidates startTimeCandidate, final Date endDate,
            final TimeCandidates endTimeCandidate) throws PopUpException;

    public Date getDate(final int year, final int month, final int day);

    public String getTimeLabelText();

    public void setTimeRangeBasedOnSeleniumPropertiesFile() throws PopUpException;
}
