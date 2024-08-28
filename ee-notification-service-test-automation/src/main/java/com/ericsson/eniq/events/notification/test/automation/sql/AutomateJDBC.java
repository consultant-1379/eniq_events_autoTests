package com.ericsson.eniq.events.notification.test.automation.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ericsson.eniq.events.notification.test.automation.main.Dataloader;
import com.ericsson.eniq.events.notification.test.automation.main.ResourceUtils;
import com.ericsson.eniq.events.notification.test.automation.util.AutomationLogger;
import com.ericsson.eniq.events.notification.test.automation.util.PropertiesFileController;

/**
 * This class establish the database(JDBC) connection and execute the query.
 * 
 * @author ekrchai
 * 
 */

public class AutomateJDBC {

	final static PropertiesFileController propFile = new PropertiesFileController();
	static Connection con = null;
	public static String hostName = null;
	public static String logsRootDir = null;
	public static String queryFlag = null;
	static {

		try {

			propFile.loadPropertiesFile(ResourceUtils
					.getResource("unix_host_and_db.properties"));
			String dbHostName = propFile
					.getPropertiesSingleStringValue("db_host");
			if (System.getProperty("HOST") != null) {
				dbHostName = System.getProperty("HOST");
			}
			System.out.println("Host: " + dbHostName);
			final String dbName = propFile
					.getPropertiesSingleStringValue("db_name");
			final String dbPort = propFile
					.getPropertiesSingleStringValue("db_port");
			final String dbUserName = propFile
					.getPropertiesSingleStringValue("db_user");
			final String dbPassword = propFile
					.getPropertiesSingleStringValue("db_pwd");
			final String dbUrl = ("jdbc:sybase:Tds:" + dbHostName);
			final String driver = "com.sybase.jdbc3.jdbc.SybDriver";
			logsRootDir = propFile.getPropertiesSingleStringValue("log_directory");
			queryFlag = propFile.getPropertiesSingleStringValue("db_query");
			Class.forName(driver);
			hostName = dbHostName;
			con = DriverManager.getConnection(dbUrl + ":" + dbPort + "/"
					+ dbName, dbUserName, dbPassword);

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		} catch (final IOException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		} catch (final SQLException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		}
	}

	public static ResultSet reteiveData(final String query) {
		ResultSet result = null;
		try {
			final Statement select = con.createStatement();
			result = select.executeQuery(query);

		} catch (final SQLException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		}
		return result;

	}

	public static ResultSet deleteData(final String query,
			final String tableName) {
		final ResultSet result = null;
		try {
			final Statement select = con.createStatement();
			final int deletedRows = select.executeUpdate(query);
			System.out.println("\nNo. of rows deleted from table '" + tableName
					+ "'  =  " + deletedRows);
			AutomationLogger.info("No. of rows deleted from table '"
					+ tableName + "'  =  " + deletedRows);

		} catch (final SQLException e) {
			e.printStackTrace();
			final String exception = Dataloader.printException(e);
			AutomationLogger.info(exception);
		}
		return result;

	}

}
