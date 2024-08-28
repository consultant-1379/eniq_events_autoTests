/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author eseuhon
 * @since 2011
 *
 */
public class DBPersistor {

    private static DBPersistor instance;
    
    static java.sql.Statement st;

    public static DBPersistor getInstatnce() {
        if (instance == null) {
            instance = new DBPersistor();
        }
        return instance;
    }

    public void closeConnection() {
        if (instance != null) {
            DBConnectionHelper.getInstance().closeConnection();
        }
    }

    private void closeResources(final ResultSet set, final Statement statement) {
        if (set != null) {
            try {
                set.close();
            } catch (final SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    
    public static String[] stringQuery(final String selectArgument, final String tableName)
    throws FileNotFoundException, IOException {
        int numberOfEvent = getNumberOfEventsForGivenIMSI(tableName);
        String resultSet[] = new String[numberOfEvent];
        DBConnectionHelper connection = null;
        int i = 0;
        try {
        	connection = DBConnectionHelper.getInstance();
                st = connection.createStatement();
        
                final StringBuilder sql = new StringBuilder();
        
                sql.append("select " + selectArgument + " from " + tableName +" where HIERARCHY_1='CELL86505'" );
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                while(result.next()){
                    resultSet[i] = result.getString(selectArgument);
                    i++;
                }

                result.close();
                connection.closeConnection();
           
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        
        return resultSet;
    }
    
    
    public static int getNumberOfEventsForGivenIMSI(final String tableName) throws FileNotFoundException, IOException {
        int numberOfEvents = 0;
        DBConnectionHelper connection = null;

        //final String queryTimeDB = setqueryTimeDB();

        try {
        	connection = DBConnectionHelper.getInstance();
            st = connection.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select count(*) from " + tableName + " where HIERARCHY_1='CELL86505'");
            // EVENT_E_LTE_IMSI_RANK_15MIN

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                numberOfEvents = result.getInt("count()");
            }
            result.close();
            
            connection.closeConnection();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return numberOfEvents;
    }
    
    public static String[] stringQueryGetFaultCodeByCodeNumber(final int codeNumber)
    throws FileNotFoundException, IOException {
        
        String resultSet[] = new String[1];
        int i = 0;
        DBConnectionHelper connection = null;
        
        try {
        	    connection = DBConnectionHelper.getInstance();
                st = connection.createStatement();
        
                final StringBuilder sql = new StringBuilder();
        
                sql.append("select FAULT_CODE_DESC from DIM_E_MSS_FAULT_CODE where FAULT_CODE= " + codeNumber);
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                while (result.next()) {
           
                    resultSet[i] = result.getString("FAULT_CODE_DESC");
                    
                    i++;
                }
                result.close();
                connection.closeConnection();                
                    
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        
        return resultSet;
    }
    
    public static String[] stringQueryGetCauseCodeByCodeNumber(final int codeNumber)
    throws FileNotFoundException, IOException {
        
        String resultSet[] = new String[1];
        int i = 0;
        DBConnectionHelper connection = null;
        
        try {
        	    connection = DBConnectionHelper.getInstance();
                st = connection.createStatement();
        
                final StringBuilder sql = new StringBuilder();
        
                sql.append("select INTERNAL_CAUSE_CODE_DESC from DIM_E_MSS_INTERNAL_CAUSE_CODE where INTERNAL_CAUSE_CODE= " + codeNumber);
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                while (result.next()) {
           
                    resultSet[i] = result.getString("INTERNAL_CAUSE_CODE_DESC");
                    i++;
                }
                result.close();
                connection.closeConnection();
                
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        
        return resultSet;
    }
    
    public List<Map<String,String>> queryLteTopologyData(final String selectArgument, final String Query)
            
    {       
    	List<Map<String, String>> topologyDataList= new ArrayList<Map<String, String>>();
        DBConnectionHelper connection = null;
        
        try 
        {            
        	connection = DBConnectionHelper.getInstance();
            st = connection.createStatement();
        	    
                final StringBuilder sql = new StringBuilder();
                
                sql.append(Query);
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                final String[] argument = selectArgument.split(",");
                
                while(result.next()){
                	Map<String, String> topologyDataMap= new HashMap<String, String>();
                	for(int j =0; j< argument.length; j++)
                	{
                		final String fieldName = argument[j].trim();
                		final String fieldValue = result.getString(fieldName);                		
                		topologyDataMap.put(fieldName, fieldValue);
                	}
                	topologyDataList.add(topologyDataMap);                
                }

                result.close();
                connection.closeConnection();
           
        } 
        catch (final SQLException e) 
        {
            e.printStackTrace();
        }
        return topologyDataList;
    } 
    
    public List<String> queryIMSIGroup(final String query)
    throws FileNotFoundException, IOException {
    	               
        DBConnectionHelper connection = null;
        List<String> listOfIMSI = new ArrayList<String>();        
        
        try {
        	connection = DBConnectionHelper.getInstance();
                st = connection.createStatement();
        
        
                final StringBuilder sql = new StringBuilder();
        
                sql.append(query);
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                while(result.next()){
                    listOfIMSI.add(result.getString("IMSI"));                    
                }

                result.close();
                connection.closeConnection();
           
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        
        return listOfIMSI;
    }

    
    //    public int getNumberOfEvent(final String imsi) {
    //        DBConnectionHelper connection = null;
    //        Statement statement = null;
    //        ResultSet set = null;
    //        long num = 0;
    //        try {
    //            connection = DBConnectionHelper.getInstance();
    //            statement = connection.createStatement();
    //            final String sql = "select MSISDN from DIM_E_IMSI_MSISDN where IMSI = " + imsi;
    //            set = statement.executeQuery(sql);
    //            set.next();
    //            connection.commit();
    //            num = set.getLong("MSISDN");
    //        } catch (final SQLException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        } finally {
    //            closeResources(set, statement);
    //            set = null;
    //            statement = null;
    //        }
    //
    //        return (int) num;
    //    }

    public List<Map<String,String>> queryExtendedCauseCodeValue(final String selectArgument, final String Query)
    
    {       
    	List<Map<String, String>> ExtendedCauseCodeValueList= new ArrayList<Map<String, String>>();
        DBConnectionHelper connection = null;
        
        try 
        {            
        	connection = DBConnectionHelper.getInstance();
            st = connection.createStatement();
        	    
                final StringBuilder sql = new StringBuilder();
                
                sql.append(Query);
        
                final ResultSet result = st.executeQuery(sql.toString());
                
                final String[] argument = selectArgument.split(",");
                
                while(result.next()){
                	Map<String, String> extCauseCodeMap= new HashMap<String, String>();
                	for(int j =0; j< argument.length; j++)
                	{
                		final String fieldName = argument[j].trim();
                		final String fieldValue = result.getString(fieldName);                		
                		extCauseCodeMap.put(fieldName, fieldValue);
                	}
                	ExtendedCauseCodeValueList.add(extCauseCodeMap);                
                }

                result.close();
                connection.closeConnection();
           
        } 
        catch (final SQLException e) 
        {
            e.printStackTrace();
        }
        return ExtendedCauseCodeValueList;
    } 

    
}
