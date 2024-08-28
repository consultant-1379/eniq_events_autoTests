/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.ui.selenium.core.charts;

import com.ericsson.eniq.events.ui.selenium.common.JsonParsing;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.common.exception.PopUpException;
import com.ericsson.eniq.events.ui.selenium.tests.baseunittest.BaseSeleniumTest;

import java.util.List;

public abstract class AbstractChart extends BaseSeleniumTest implements IChartCommand {

    static final String JSON_MEMBER = "data";

    /**
     * This method returns all values representing different data in X or Y axis
     * 
     * @return all values representing different data in X or Y axis
     * @throws NoDataException
     */
    //@Override
    public List<String> getChartValues(final int timeAsMiniute, final String jsonMemberElement) throws NoDataException {
        final String jsonText = getJsonText(timeAsMiniute);
        final List<String> values = JsonParsing.getElementValues(jsonText, JSON_MEMBER, jsonMemberElement);
        if (values.size() == 0) {
            throw new NoDataException(" The returned JsonText in HttpGET Response: " + jsonText + " and we are looking for " + JSON_MEMBER + "value.");
        }
        return values;
    }

    /**
     * This method calls drillDown() javascript function in Client UI so that we can drill down Chart window.
     * 
     * @param chartElementClicked
     * @param drillDownWindowType see {@link com.ericsson.eniq.events.ui.selenium.core.charts.ChartDrillDownWindowTypes}
     * @throws PopUpException
     */
    //@Override
    public void processDrillDownChart(final String chartElementClicked, final String drillDownMenuType) throws PopUpException {
        final String script = "var chartElement = '" + chartElementClicked + "' ; var drillDownWindowType = '"
                + ChartDrillDownWindowTypes.menuMapsToDrillDownWindowTypes.get(drillDownMenuType)
                + "' ; var result = this.browserbot.getUserWindow().drillDown(chartElement, drillDownWindowType); result.toString();";
        if (selenium.getEval(script) != null) {
            waitForPageLoadingToComplete();
        } else {
            fail("Chart element: " + chartElementClicked + "can not be drilled down.");
        }

    }

    protected String getChartURL() {
        final String script = "var url = this.browserbot.getUserWindow().getURL(); url.toString();";
        final String URL = selenium.getEval(script);
        logger.info("Chart URL: " + URL);
        return URL;
    }

    /**
     * Sending a HTTP GET request using XMLHttpRequest to get JsonText returned in responseText This JsonText will contains information used in
     * displaying Chart.
     */
    public abstract String getJsonText(final int timeAsMiniute);

}
