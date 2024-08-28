/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.wcdmaCFA;

import com.ericsson.eniq.events.ui.selenium.common.Transformer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EIKRWAQ
 * @since 2013
 *
 */
public class CFAControllerRankingTransformer implements Transformer<List<Map<String, String>>> {

    private static final String FAILURES = "Failures";

    private static final String RNC = "RNC";

    private static final String RAN_VENDOR = "RAN_Vendor";

    private static final String RANK = "Rank";

    //@Override
    public List<Map<String, String>> transform(ResultSet resultSet) throws SQLException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        while (resultSet.next()) {
            Map<String, String> rowResult = new HashMap<String, String>();
            rowResult.put(RANK, resultSet.getString(RANK));
            rowResult.put(RAN_VENDOR, resultSet.getString(RAN_VENDOR));
            rowResult.put(RNC, resultSet.getString(RNC));
            rowResult.put(FAILURES, resultSet.getString(FAILURES));
            result.add(rowResult);
        }
        return result;
    }

}
