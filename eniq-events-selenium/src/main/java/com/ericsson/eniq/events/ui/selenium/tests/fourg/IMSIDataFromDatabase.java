/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.tests.fourg;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * @author ekeviry/eantmcm
 * @since 2011
 * 
 */
public class IMSIDataFromDatabase {

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    final static Connection con = DBConnection();

    static Timestamp dateTimeId;

    static java.sql.Statement st;

    public ArrayList<String> getAllIMSIDataFromDB(final String imsiValue, final int eventValue) throws ParseException,
            FileNotFoundException, IOException {

        final int numberOfEvents = getNumberOfEventsForGivenIMSI(imsiValue);

        final int[] eventID = intQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "EVENT_ID");

        final Timestamp[] eventTimeSetWithoutTimezoneOffset = getEventTime(imsiValue, numberOfEvents);

        final String[] eventTimeSet = timezoneOffsetHandling(eventTimeSetWithoutTimezoneOffset);

        final String[] tacSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "TAC");

        final String[] manufacturerSet = stringQueryDIM_E_SGEH_TAC(tacSet, "MANUFACTURER");
        final String[] marketingNameSet = stringQueryDIM_E_SGEH_TAC(tacSet, "MARKETING_NAME");

        final int[] eventResultIDSet = intQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "EVENT_RESULT");
        final String[] eventTypeArray = convertEventIDToEventType(eventResultIDSet);

        final int[] causeCodeSet = intQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "CAUSE_CODE");

        final String[] causeCodeDescSet = stringQueryDIM_E_SGEH_CAUSECODE(causeCodeSet, "CAUSE_CODE_DESC");

        final int[] subCauseCodeSet = intQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "SUBCAUSE_CODE");

        final String[] subCauseCodeDescSet = stringQueryDIM_E_SGEH_SUBCAUSECODE(subCauseCodeSet, "SUBCAUSE_CODE_DESC");

        final String[] eventSourceNameSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "EVENT_SOURCE_NAME");

        final String[] controllerSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "HIERARCHY_3");

        final String[] accessAreaSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "HIERARCHY_1");

        final String[] ranVendorSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "VENDOR");

        final String[] apnSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "APN");

        final String[] mccSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "MCC");

        final String[] mncSet = stringQueryEVENT_E_LTE_RAW(imsiValue, numberOfEvents, "MNC");

        final ArrayList<String> dbResultSet = new ArrayList<String>();

        for (int i = 0; i < numberOfEvents; i++) {

            if (eventID[i] == eventValue) {

                // final String str_date = eventTimeSetWithoutTimezoneOffset[i]
                // .toString();
                // final SimpleDateFormat formatter = new SimpleDateFormat(
                // "yyyy-MM-dd HH:mm:ss");
                // final Date date = formatter.parse(str_date);
                // final Calendar calendar = Calendar.getInstance();
                // final TimeZone tz = calendar.getTimeZone();
                // // System.out.println("Current TimeZone is : "
                // // + tz.getDisplayName());
                // calendar.setTime(date);
                // // System.out.println("Database time" + date);
                // calendar.add(Calendar.MILLISECOND,
                // tz.getOffset(System.currentTimeMillis()));
                // final String eventTime =
                // formatter.format(calendar.getTime());

                dbResultSet.add(eventTimeSet[i]);
                dbResultSet.add(tacSet[i]);
                dbResultSet.add(manufacturerSet[i]);
                dbResultSet.add(marketingNameSet[i]);
                dbResultSet.add(eventTypeArray[i]);
                dbResultSet.add(causeCodeDescSet[i]);
                dbResultSet.add(subCauseCodeDescSet[i]);
                dbResultSet.add(eventSourceNameSet[i]);
                dbResultSet.add(controllerSet[i]);
                dbResultSet.add(accessAreaSet[i]);
                dbResultSet.add(ranVendorSet[i]);
                dbResultSet.add(apnSet[i]);
                dbResultSet.add(mccSet[i]);
                dbResultSet.add(mncSet[i]);
            }
        }

        return dbResultSet;
    }

    public static Timestamp[] getEventTime(final String imsiValue, final int numberOfEvents)
            throws FileNotFoundException, IOException {

        final Timestamp[] EVENT_TIME_event = new Timestamp[numberOfEvents];

        final String queryTimeDB = setqueryTimeDB();
        // System.out.println("Time for DB Query: " + queryTimeDB);

        int i = 0;
        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select EVENT_TIME from EVENT_E_LTE_RAW where DATETIME_ID>='" + queryTimeDB + "'" + " and IMSI="
                    + imsiValue + "order by EVENT_TIME desc");

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                EVENT_TIME_event[i] = result.getTimestamp("EVENT_TIME");
                // System.out.println("Event Time " + i + ": "
                // + EVENT_TIME_event[i]);
                i++;
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return EVENT_TIME_event;
    }

    public static String[] timezoneOffsetHandling(final Timestamp[] eventTimeSetTimeStampFormat) throws ParseException {

        final String[] eventTimeSet = new String[eventTimeSetTimeStampFormat.length];

        for (int i = 0; i < eventTimeSetTimeStampFormat.length; i++) {
            final String eventTime = eventTimeSetTimeStampFormat[i].toString();
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final Date date = formatter.parse(eventTime);
            final Calendar calendar = Calendar.getInstance();
            final TimeZone tz = calendar.getTimeZone();
            // System.out.println("Current TimeZone is : "
            // + tz.getDisplayName());
            calendar.setTime(date);
            // System.out.println("Database time" + date);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(System.currentTimeMillis()));
            eventTimeSet[i] = formatter.format(calendar.getTime());
        }

        return eventTimeSet;

    }

    public static String[] stringQueryEVENT_E_LTE_RAW(final String imsiValue, final int numberOfEvents,
            final String selectArgument) throws FileNotFoundException, IOException {

        final String[] resultSet = new String[numberOfEvents];
        int i = 0;
        final String queryTimeDB = setqueryTimeDB();
        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select " + selectArgument + " from EVENT_E_LTE_RAW where datetime_id>='" + queryTimeDB + "'"
                    + " and IMSI=" + imsiValue + "order by EVENT_TIME desc");

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                resultSet[i] = result.getString(selectArgument);
                i++;
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static int[] intQueryEVENT_E_LTE_RAW(final String imsiValue, final int numberOfEvents,
            final String selectArgument) throws FileNotFoundException, IOException {

        final int[] resultSet = new int[numberOfEvents];
        int i = 0;
        final String queryTimeDB = setqueryTimeDB();
        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select " + selectArgument + " from EVENT_E_LTE_RAW where datetime_id>='" + queryTimeDB + "'"
                    + " and IMSI=" + imsiValue + "order by EVENT_TIME desc");

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                resultSet[i] = result.getInt(selectArgument);
                i++;
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static String[] stringQueryDIM_E_SGEH_TAC(final String[] tacSet, final String selectArgument)
            throws FileNotFoundException, IOException {

        final String[] resultSet = new String[tacSet.length];
        try {
            for (int i = 0; i < tacSet.length; i++) {
                st = con.createStatement();

                final StringBuilder sql = new StringBuilder();

                sql.append("select " + selectArgument + " from DIM_E_SGEH_TAC where TAC = " + tacSet[i]);

                final ResultSet result = st.executeQuery(sql.toString());

                while (result.next()) {
                    resultSet[i] = result.getString(selectArgument);
                }
                result.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static String[] stringQueryDIM_E_SGEH_CAUSECODE(final int[] causeCodeSet, final String selectArgument)
            throws FileNotFoundException, IOException {

        final String[] resultSet = new String[causeCodeSet.length];
        try {
            for (int i = 0; i < causeCodeSet.length; i++) {
                st = con.createStatement();

                final StringBuilder sql = new StringBuilder();

                sql.append("select " + selectArgument + " from DIM_E_SGEH_CAUSECODE where CAUSE_CODE = "
                        + causeCodeSet[i]);

                final ResultSet result = st.executeQuery(sql.toString());

                while (result.next()) {
                    resultSet[i] = result.getString(selectArgument);
                }
                result.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static String[] stringQueryDIM_E_SGEH_SUBCAUSECODE(final int[] subCauseCodeSet, final String selectArgument)
            throws FileNotFoundException, IOException {

        final String[] resultSet = new String[subCauseCodeSet.length];
        try {
            for (int i = 0; i < subCauseCodeSet.length; i++) {
                st = con.createStatement();

                final StringBuilder sql = new StringBuilder();

                sql.append("select " + selectArgument + " from DIM_E_SGEH_SUBCAUSECODE where SUBCAUSE_CODE = "
                        + subCauseCodeSet[i]);

                final ResultSet result = st.executeQuery(sql.toString());

                while (result.next()) {
                    resultSet[i] = result.getString(selectArgument);
                }
                result.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static String[] convertEventIDToEventType(final int[] eventID) {
        final String[] eventType = new String[eventID.length];
        try {
            for (int i = 0; i < eventID.length; i++) {
                st = con.createStatement();

                final StringBuilder sql = new StringBuilder();

                sql.append("select EVENT_RESULT_DESC from DIM_E_SGEH_EVENTRESULT where EVENT_RESULT = " + eventID[i]);

                final ResultSet result = st.executeQuery(sql.toString());

                while (result.next()) {
                    eventType[i] = result.getString("EVENT_RESULT_DESC");
                }
                result.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return eventType;
    }

    public static String[] convertEventResultIDToEventType(final int[] eventResultID) {
        final String[] eventType = new String[eventResultID.length];
        try {
            for (int i = 0; i < eventResultID.length; i++) {
                st = con.createStatement();

                final StringBuilder sql = new StringBuilder();

                sql.append("select EVENT_ID_DESC from DIM_E_LTE_EVENTTYPE where EVENT_ID = " + eventResultID[i]);

                final ResultSet result = st.executeQuery(sql.toString());

                while (result.next()) {
                    eventType[i] = result.getString("EVENT_ID_DESC");
                }
                result.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return eventType;
    }

    public static Connection DBConnection() {

        final RegressionPropertiesFileReader dbPropFileReader = new RegressionPropertiesFileReader();

        String[] dbProperties = new String[6];

        try {
            dbProperties = dbPropFileReader.readDatabaseProperties();

        } catch (final FileNotFoundException e1) {
            System.err.print("Database Properties File not found!");
            System.err.println(e1.getMessage());
            e1.printStackTrace();
        } catch (final IOException e1) {
            System.err.print("Error reading Database Properties File");
            System.err.println(e1.getMessage());
            e1.printStackTrace();
        }

        final String dbPort = dbProperties[0];

        final String dbDriverClass = dbProperties[1];

        final String dbPath = dbProperties[2];

        final String dbName = dbProperties[3];

        final String dbUserName = dbProperties[4];

        final String dbPassword = dbProperties[5];

        java.sql.Connection jdbcConnection = null;
        try {
            // JDBC driver connection for sybase
            Class.forName(dbDriverClass);

        } catch (final Exception e) {
            System.out.println("Failed to load mSQL driver.");
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            jdbcConnection = DriverManager.getConnection(
                    "jdbc:sybase:Tds:" + dbPath + ":" + dbPort + "/" + dbName + "", dbUserName, dbPassword);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return jdbcConnection;
    }

    public static int getNumberOfEventsForGivenIMSI(final String imsiValue) throws FileNotFoundException, IOException {
        int numberOfEvents = 0;

        final String queryTimeDB = setqueryTimeDB();

        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select count(*) from EVENT_E_LTE_RAW where datetime_id>='" + queryTimeDB + "'" + " and IMSI="
                    + imsiValue);
            // EVENT_E_LTE_IMSI_RANK_15MIN

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                numberOfEvents = result.getInt("count()");
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return numberOfEvents;
    }

    public static int getNumberOfEventsForGivenTAC(final String tacValue) throws FileNotFoundException, IOException {
        int numberOfEvents = 0;

        final String queryTimeDB = setqueryTimeDB();

        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select count(*) from EVENT_E_LTE_ERR_RAW where datetime_id>='" + queryTimeDB + "'"
                    + " and TAC=" + tacValue);

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                numberOfEvents = result.getInt("count()");
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return numberOfEvents;
    }

    public static int getNumberOfEventsForDefaultTimeRange(final String imsiValue) throws FileNotFoundException,
            IOException, ParseException {
        int numberOfEvents = 0;

        final String newDate = defaultTimeRangeDB();

        try {
            st = con.createStatement();

            final StringBuilder sql = new StringBuilder();

            sql.append("select count(*) from EVENT_E_LTE_RAW where DATETIME_ID>='" + newDate + "'" + " and IMSI="
                    + imsiValue);

            final ResultSet result = st.executeQuery(sql.toString());

            while (result.next()) {
                numberOfEvents = result.getInt("count()");
            }
            result.close();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return numberOfEvents;
    }

    private static String setqueryTimeDB() throws FileNotFoundException, IOException {

        String newDate;
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        // hh:mm:ss

        int daysToDecrement = RegressionPropertiesFileReader.getTimeRangeFromPropFile();
        daysToDecrement = -daysToDecrement;

        cal.add(Calendar.DATE, daysToDecrement);

        newDate = dateformat.format(cal.getTime());

        // Services Time Range for One Week = Current Date - 7 days and hh:mm:ss = 00:00:00
        newDate = newDate + " 00:00:00";

        System.out.println("*DB Query Time: " + newDate);

        return newDate;
    }

    private static String defaultTimeRangeDB() throws FileNotFoundException, IOException, ParseException {

        String queryTimeDB;
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final int timeToDecrement = -1;

        cal.add(Calendar.DATE, timeToDecrement);
        queryTimeDB = dateformat.format(cal.getTime());

        System.out.println("DB DateTime: " + dateformat.format(cal.getTime()));

        return queryTimeDB;
    }
}
