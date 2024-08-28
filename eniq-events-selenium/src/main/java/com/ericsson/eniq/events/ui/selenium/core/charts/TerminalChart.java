/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core.charts;

import org.springframework.stereotype.Component;

/**
 * @author eseuhon
 * @since 2011
 *
 */

@Component
public class TerminalChart extends AbstractChart {

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.ui.selenium.core.charts.AbstractChart#getJsonText()
     */
    @Override
    public String getJsonText(final int timeAsMiniute) {
        // TODO Auto-generated method stub
        final String javascirpt = "function httpGet(){"
                + "var Url = \""
                + getChartURL()
                //more options required in URL i.e time, imsi, display... etc.   
                + "?time=" + timeAsMiniute + "&display=chart&tzOffset=+0000&maxRows=500" + "\";"
                + "var xmlHttp = new XMLHttpRequest();" + "xmlHttp.open( \"GET\", Url, false );"
                + "xmlHttp.send(null); " + "var result = xmlHttp.responseText;"
                + "return result.toString();} httpGet();";

        final String jsonText = selenium.getEval(javascirpt);
        logger.info("JsonText returned :\n" + jsonText);
        return jsonText;
    }

}
