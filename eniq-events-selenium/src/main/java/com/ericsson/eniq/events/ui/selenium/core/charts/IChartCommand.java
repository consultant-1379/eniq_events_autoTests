/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core.charts;

import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;

import java.util.List;

/**
 * @author eseuhon
 * @since 2011
 *
 */
public interface IChartCommand {

    public List<String> getChartValues(final int timeAsMiniute, final String jsonMemberElement) throws NoDataException;

    public void processDrillDownChart(final String chartElementClicked, final String drillDownMenuType)
            throws PopUpException;

}
