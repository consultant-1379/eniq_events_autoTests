package com.ericsson.eniq.events.ui.selenium.tests.sessionbrowser.common;

import com.ericsson.eniq.events.ui.selenium.common.PropertyReader;
import org.junit.After;
import org.junit.Before;

import java.sql.*;

public class DataBaseConnection {

	private Connection conn = null;

	private String Database_driver = "com.sybase.jdbc3.jdbc.SybDriver";

	private String url;

	private String USER_NAME;

	private String PASSWORD;

	@Before
	public void openConnection() {

		try {
			Class.forName(Database_driver).newInstance();
			url = getURL();
			USER_NAME = PropertyReader.getInstance().getDbUser();
			PASSWORD = PropertyReader.getInstance().getDbPwd();
			conn = DriverManager.getConnection(url, USER_NAME, PASSWORD);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@After
	public void closeConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String query) {
		ResultSet rs = null;

		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	public String getRFVisitCellQuery(String imsi) {
		return "SELECT cellRef.HIERARCHY_3 AS RNC_ALTERNATIVE_FDN,cellRef.CELL_ID AS CELL_ID, "
				+ "MIN(EVENT_TIME)                                                     AS START_TIME, "
				+ "CAST(ISNULL(SUM(CAST(DURATION AS FLOAT)/60000),0) AS NUMERIC(16,2)) AS Duration, "
				+ "ISNULL(SUM(RRC_SAMPLES_BC_BS),0) 					AS BC_BS, "
				+ "ISNULL(SUM(RRC_SAMPLES_BC_GS),0) 					AS BC_GS, "
				+ "ISNULL(SUM(RRC_SAMPLES_GC_BS),0)                   AS GC_BS, "
				+ "ISNULL(SUM(RRC_SAMPLES_GC_GS),0) 					AS GC_GS "
				+ "FROM "
				+ "(select EVENT_TIME, HIER3_CELL_ID_1, DATETIME_ID, DURATION, "
				+ "RRC_SAMPLES_GC_BS, RRC_SAMPLES_BC_BS, RRC_SAMPLES_BC_GS, RRC_SAMPLES_GC_GS "
				+ "from dc.EVENT_E_RAN_SESSION_CELL_VISITED_RAW_04 rawview "
				+ "where DATETIME_ID >= '2012-05-15 23:00:00' and DATETIME_ID < '2012-05-16 23:00:00' and IMSI = "
				+ imsi
				+ ") as rawview "
				+ "LEFT OUTER JOIN "
				+ "(SELECT DISTINCT CID, CELL_ID,HIER3_CELL_ID,HIERARCHY_3 "
				+ "FROM DIM_E_SGEH_HIER321_CELL) AS cellRef "
				+ "ON(rawview.HIER3_CELL_ID_1 = cellRef.HIER3_CELL_ID) "
				+ "GROUP BY CELL_ID,RNC_ALTERNATIVE_FDN "
				+ "ORDER BY START_TIME DESC";
	}

	private String getURL() {
		return "jdbc:sybase:Tds:" + PropertyReader.getInstance().getDbHost()
				+ ":" + PropertyReader.getInstance().getDbPort() + "/"
				+ PropertyReader.getInstance().getDbName();
	}

	public String getIMSIForStandardDBS() {
		return "select top 1 EVENT_E_RAN_SESSION_RAW.IMSI from EVENT_E_RAN_SESSION_RAW "+
				"where EVENT_E_RAN_SESSION_RAW.IMSI not in (select distinct IMSI from EVENT_E_CORE_SESSION_RAW) and " +
				"EVENT_E_RAN_SESSION_RAW.IMSI not in (select distinct IMSI from EVENT_E_USER_PLANE_TCP_RAW)";
	}
	
	public String getIMSIForCoreRanDBS() {
		return "select top 1 EVENT_E_RAN_SESSION_RAW.IMSI from EVENT_E_RAN_SESSION_RAW "+
				"where EVENT_E_RAN_SESSION_RAW.IMSI in (select distinct IMSI from EVENT_E_CORE_SESSION_RAW) and " +
				"EVENT_E_RAN_SESSION_RAW.IMSI in (select distinct IMSI from EVENT_E_USER_PLANE_TCP_RAW)";

	}
}
