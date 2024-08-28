/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core.charts;

import com.ericsson.eniq.events.ui.selenium.common.JsonParsing;
import com.ericsson.eniq.events.ui.selenium.common.exception.NoDataException;
import com.ericsson.eniq.events.ui.selenium.events.tabs.SubscriberTab.ImsiMenu;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eseuhon
 * @since 2011
 *
 */
@Component
public class SubscriberBusinessIntelligenceChart extends AbstractChart {

    private String parametersInURL;

    /**
     * Sending a HTTP GET request using XMLHttpRequest to get JsonText returned in responseText
     * This JsonText will contains information used in displaying Chart. 
     * @param parametersInURL  
     * @return
     */
    @Override
    public String getJsonText(final int timeAsMiniute) {
        final String javascirpt = "function httpGet(){" + "var Url = \""
                + getChartURL()
                //more options required in URL i.e time, imsi, display... etc.   
                + "?time=" + timeAsMiniute + getParametersInURL() + "&display=chart&tzOffset=+0100&maxRows=500" + "\";"
                + "var xmlHttp = new XMLHttpRequest();" + "xmlHttp.open( \"GET\", Url, false);" + "xmlHttp.send(); "
                + "var result = xmlHttp.responseText;" + "return result.toString();} httpGet();";
        final String jsonText = selenium.getEval(javascirpt);
        logger.info("JsonText returned :\n" + jsonText);
        return jsonText;
    }

    private List<EventChartData> getAllEventChartData(final int timeAsMiniutes) throws NoDataException {
        final String jsonText = getJsonText(timeAsMiniutes);
        final List<JSONObject> objList = JsonParsing.getAllData(jsonText);
        if (objList.size() == 0) {
            throw new NoDataException("There is no retured data of JsonText in the HttpGET Response.");
        }

        final List<EventChartData> result = new ArrayList<EventChartData>();
        for (final JSONObject obj : objList) {
            result.add(new EventChartData(obj));
        }

        return result;
    }

    /**
     * Returns all events including failure numbers at current chart. This method will exclude any events which doesn't have any failure numbers
     * @return List<EventChartData> list of all event 
     */
    public List<EventChartData> getAllEventChartDataWithFailureNumbers(final int timeAsMiniutes) throws NoDataException {
        final List<EventChartData> result = new ArrayList<EventChartData>();
        for (final EventChartData data : getAllEventChartData(timeAsMiniutes)) {
            if (data.getNumberOfFailrue() != 0) {
                result.add(data);
            }
        }

        if (result.size() == 0) {
            throw new NoDataException(
                    "All returned chart data has no failure cases. We are only able to drill down failure numbers in chart.");
        }

        return result;
    }

    public void setParametersToBeIncludedInURL(final String value, final ImsiMenu menu) {
        // According to IMSI menu type i.e. IMSI, IMSIGroup or PTMSI 
        // the url parameters of HTTP GET Request are different 
        // i.e. IMSI - &type=IMSI&imsi=310190001987605
        //      IMSIGROUP - &type=IMSI&groupname=DG_GroupNameIMSI_250
        this.parametersInURL = "&type=" + menu.toString() + "&" + menu.getNameUsedInURL() + "=" + value;
    }

    public String getParametersInURL() {
        return parametersInURL;
    }

}
