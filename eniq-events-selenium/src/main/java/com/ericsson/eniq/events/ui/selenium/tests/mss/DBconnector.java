/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.mss;

import com.ericsson.eniq.events.ui.selenium.common.db.DBConnectionHelper;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Logger;


/**
 * @author ekeviry/ewandaf
 * @since 2011
 *
 */
public class DBconnector { 
    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
    
    final static DBConnectionHelper con =  DBConnectionHelper.getInstance();
    
    static Timestamp dateTimeId;
    
    static java.sql.Statement statement;
    
    public static HashMap<String,String> excuteQueriesForTopologyChanges(
    		final String selectArgument, final String Query)
            throws FileNotFoundException, IOException 
    {
        HashMap<String, String> topologyData= new HashMap<String, String>();
        try 
        {            
        	    statement = con.createStatement();
        	    
                final StringBuilder sql = new StringBuilder();
                
                sql.append(Query);
        
                final ResultSet resultSet = statement.executeQuery(sql.toString());
                final String[] argument = selectArgument.split(",");
                System.out.println("The Length is :" + argument.length);
                while(resultSet.next()){
                	for(int j =0; j< argument.length; j++)
                	{
                		final String fieldName = argument[j].trim();
                		final String fieldValue = resultSet.getString(fieldName); 
                		System.out.println("The fieldName is :" + fieldName + "and the fieldValue is :" + fieldValue);
                		topologyData.put(fieldName, fieldValue);
                	}
                }

                resultSet.close();
                statement.close();
           
        } 
        catch (final SQLException e) 
        {
            e.printStackTrace();
        }
        return topologyData;
    } 
}
