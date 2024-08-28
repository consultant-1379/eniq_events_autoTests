/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.Transformer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EIKRWAQ
 * @since 2013
 *
 */
public class TimerRangeMaxTransformer implements Transformer<Map<String, String>> {

    public static final String MAX_DATE = "MAX_DATE";

    //@Override
    public Map<String, String> transform(ResultSet resultSet) throws SQLException {
        Map<String, String> result = new HashMap<String, String>();
        while (resultSet.next()) {
            result.put(MAX_DATE, resultSet.getString(MAX_DATE));
        }
        return result;
    }

}
