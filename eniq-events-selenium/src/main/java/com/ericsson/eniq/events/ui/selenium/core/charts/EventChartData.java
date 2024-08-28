/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.core.charts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author eseuhon
 * @since 2011
 * This class holds all values of JSON text under "data" section as a returned value after sending GET request to server side.
 * Especially, this class will represent char data related with event type, number of failure and number of success. 
 *
 * i.e. 
 * {"success":"true","errorDescription":"","yaxis_min":"0","yaxis_max":"1984","data":[{"1":"4","2":"149","3":"Sunday"}]}
 *
 */
public class EventChartData {

    private final JSONObject dataObj;

    enum JsonDataElement {
        FAILURE_NUMBERS("1"), SUCCESS_NUMBERS("2"), EVENT_NAME("3");

        String index;

        private JsonDataElement(final String index) {
            this.index = index;
        }

        public String getIndex() {
            return index;
        }
    }

    private int numOfFailure;

    private int numOfSuccess;

    private String eventName;

    public EventChartData(final JSONObject obj) {
        this.dataObj = obj;
        setAllData();
    }

    private void setAllData() {
        try {
            numOfSuccess = Integer.parseInt(dataObj.get(JsonDataElement.SUCCESS_NUMBERS.getIndex()).toString());
            numOfFailure = Integer.parseInt(dataObj.get(JsonDataElement.FAILURE_NUMBERS.getIndex()).toString());
            eventName = dataObj.get(JsonDataElement.EVENT_NAME.getIndex()).toString();
        } catch (final NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getEventName() {
        return eventName;
    }

    public int getNumberOfFailrue() {
        return numOfFailure;
    }

    public int getNumberOfSuccess() {
        return numOfSuccess;
    }
}
