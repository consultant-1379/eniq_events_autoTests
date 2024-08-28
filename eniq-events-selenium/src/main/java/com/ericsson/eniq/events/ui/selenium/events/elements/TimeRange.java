/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.events.elements;

/**
 * @author ericker
 * @since 2010
 *
 */
public enum TimeRange {

    FIVE_MINUTES("5 minutes", 5), FIFTEEN_MINUTES("15 minutes", 15), THIRTY_MINUTES("30 minutes", 30), ONE_HOUR(
            "1 hour", 1 * 60), TWO_HOURS("2 hours", 2 * 60), SIX_HOURS("6 hours", 6 * 60), TWELVE_HOURS("12 hours",
            12 * 60), ONE_DAY("1 day", 24 * 60), ONE_WEEK("1 week", 7 * 24 * 60);

    private final String label;

    private int minituesValue;

    private TimeRange(final String txtLabel, final int minutes) {
        label = txtLabel;
        minituesValue = minutes;
    }

    public String getLabel() {
        return label;
    }

    public static TimeRange getTimeRange(final String label) {
        for (final TimeRange range : values()) {
            if (range.label.equals(label)) {
                return range;
            }
        }
        return null;
    }

    public int getMiniutes() {
        return minituesValue;
    }
}
