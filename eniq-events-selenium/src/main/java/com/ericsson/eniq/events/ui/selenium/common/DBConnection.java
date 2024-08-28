/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2013 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.sybase.jdbc3.jdbc.SybConnection;
import com.sybase.jdbc3.jdbc.SybDriver;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author EIKRWAQ
 * @since 2013
 *
 */

public class DBConnection {

    private static final String USERNAME = PropertyReader.getInstance().getDbUser();

    private static final String PASSWORD = PropertyReader.getInstance().getDbPwd();

    private static final String JDBC_URL = "jdbc:sybase:Tds:" + PropertyReader.getInstance().getDbHost() + ":"
            + PropertyReader.getInstance().getDbPort() + "/" + PropertyReader.getInstance().getDbName();

    private static final String REPUSERNAME = PropertyReader.getInstance().getRepDbUser();

    private static final String REPPASSWORD = PropertyReader.getInstance().getRepDbPwd();

    private static final String REPDB_URL = "jdbc:sybase:Tds:" + PropertyReader.getInstance().getRepDbHost() + ":"
            + PropertyReader.getInstance().getRepDbPort() + "/" + PropertyReader.getInstance().getRepDbName();

    private SybConnection connection;

    protected static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    private SybDriver driver;

    final String DRIVER_NAME = "com.sybase.jdbc3.jdbc.SybDriver";

    private static DBConnection instance = null;

    public static DBConnection getInstance() {
        if (instance == null) {
            return new DBConnection();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (!isConnectionNull()) {
                connection.commit();
                connection.close();
                connection = null;
                DriverManager.deregisterDriver(driver);
                driver = null;
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnectionNull() {
        return connection == null;
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * @throws SQLException
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    public SybConnection getConnection() {
        // get DriverManager
        try {
            driver = (SybDriver) Class.forName(DRIVER_NAME).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // create a new connection
        try {
            return connection = (SybConnection) DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.log(Level.ALL, "ERROR : " + e.toString());
            e.printStackTrace();
            logger.log(Level.ALL, "ERROR : " + "Unable to Connect" + JDBC_URL + " as " + USERNAME);
        }
        return connection;
    }

    public SybConnection getRepDBConnection() {
        // get DriverManager
        try {
            driver = (SybDriver) Class.forName(DRIVER_NAME).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // create a new connection
        try {
            return connection = (SybConnection) DriverManager.getConnection(REPDB_URL, REPUSERNAME, REPPASSWORD);
        } catch (SQLException e) {
            logger.log(Level.ALL, "ERROR : " + e.toString());
            e.printStackTrace();
            logger.log(Level.ALL, "ERROR : " + "Unable to Connect To Rep DB " + REPDB_URL + " as " + REPUSERNAME);
        }
        return connection;
    }

    /**
     * @param query
     * @param transformer
     * @return
     */
    public <T> T getDBResult(final String query, final Transformer<T> transformer) {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            //            statement = connection.createStatement();
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            return transformer.transform(resultSet);

        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
            closeConnection();
        }
        return null;
    }

    /**
     * @param statement
     */
    public void closeStatement(Statement statement) {
        try {
            if (!isStatementNull(statement)) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isStatementNull(Statement statement) {
        return statement == null;
    }

    public void getRunQueryInRepDB(final String query) {
        Statement statement = null;
        try {
            //            statement = connection.createStatement();
            statement = getRepDBConnection().createStatement();
            statement.execute(query);
            logger.log(Level.ALL, "Truncation Successfull");

        } catch (final Exception e) {
            logger.log(Level.ALL, "Truncation Failed");
            e.printStackTrace();
        } finally {
            closeStatement(statement);
            closeConnection();
        }
    }
}
