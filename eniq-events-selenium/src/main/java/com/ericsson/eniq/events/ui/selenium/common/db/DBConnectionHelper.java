/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.ui.selenium.common.db;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;
import com.sybase.jdbc3.jdbc.SybConnection;
import com.sybase.jdbc3.jdbc.SybDriver;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author eseuhon
 * @since 2011
 * 
 * This class is used to create connection to Sybase DB and close, etc... 
 */
public class DBConnectionHelper {

    private static DBConnectionHelper instance;

    private SybConnection connection;

    private SybDriver driver;

    public Logger logger = Logger.getLogger(SeleniumLogger.class.getName());

    final String DRIVER_NAME = "com.sybase.jdbc3.jdbc.SybDriver";

    public static DBConnectionHelper getInstance() {
        if (instance == null) {
            instance = new DBConnectionHelper();
            try {
                instance.init();
            } catch (final SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return instance;
    }

    void init() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //get DriverManager
        driver = (SybDriver) Class.forName(DRIVER_NAME).newInstance();
        DriverManager.registerDriver(driver);
        //create a new connection
        connection = (SybConnection) DriverManager.getConnection("jdbc:sybase:Tds:"
                + PropertyReader.getInstance().getDbHost() + ":" + PropertyReader.getInstance().getDbPort() + "/"
                + PropertyReader.getInstance().getDbName() + "", PropertyReader.getInstance().getDbUser(),
                PropertyReader.getInstance().getDbPwd());
    }

    public void closeConnection() {
        try {
            connection.close();
            connection = null;
            instance = null;
        } catch (final SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

}
