/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eseuhon
 * @since 2010
 *
 */
public class JsonParsing {

    /**
     * This method is used for parsing Json data and return all element's value.   
     * @param jsonText
     * @param member
     * @param element
     * @return List<String> all values representing different data in X or Y axis   
     */
    public static List<String> getElementValues(final String jsonText, final String member, final String element) {
        /* select member of JsonText and iterate its element and then retrieve specific value of it.
         * 
         * i.e. jsonText returned
         * {
         * "success": "true",
         * "errorDescription": "",
         * "yaxis_min": "1",
         * "yaxis_max": "239591",
         * "data": [
         * {
         *   "1": "1",
         *   "2": "someTestGroup-TAC1",
         *   "3": "239566",      
         *   "4": "10"      
         * },
         * {
         *    "1": "1",
         *    "2": "someTestGroup-TAC133",
         *   "3": "239566",      
         *    "4": "10100"      
         * }
         * ]
         * }
         * 
         * call getDataElementsValue(jsonText, "data", "2") will return  
         * List<String>{"someTestGroup-TAC1","someTestGroup-TAC133"} 
         */
        final List<String> values = new ArrayList<String>();

        try {
            final JSONObject json = new JSONObject(jsonText);
            final JSONArray array = json.getJSONArray(member);

            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);
                values.add(obj.get(element).toString());
            }
        } catch (final JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return values;
    }

    /**
     * this method will parse json text and return all values in "data"  
     * @param jsonText
     * @return
     */
    public static List<JSONObject> getAllData(final String jsonText) {
        final List<JSONObject> result = new ArrayList<JSONObject>();
        final String member = "data";

        try {
            final JSONObject json = new JSONObject(jsonText);
            final JSONArray array = json.getJSONArray(member);

            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);
                result.add(obj);
            }

        } catch (final JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
