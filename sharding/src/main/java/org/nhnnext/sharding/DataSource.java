package org.nhnnext.sharding;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;

// THIS IS SINGLETON PATTERN
public class DataSource {
//	private static DataSource dataSource;
	static final String USERNAME = "sharding";
	static final String USERPW = "";
	static final String DB_URL_PRE = "jdbc:mysql://";
	static final String DB_URL_PORT = ":3306/";
	static final String DB_URL_POST = "?noAccessToProcedureBodies=true";
	static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	private BasicDataSource bds;
	
	// CALLED BY DatabaseContextListener

	// THIS CONSTRUCTOR RUNS ONLY ONCE
	private DataSource(String ip, String dbname) {
		bds = new BasicDataSource();
		bds.setDriverClassName(DataSource.DB_DRIVER);
		bds.setUsername(DataSource.USERNAME);
		bds.setPassword(DataSource.USERPW);
		bds.setUrl(DataSource.DB_URL_PRE + ip + DataSource.DB_URL_PORT + dbname + DataSource.DB_URL_POST);
		
		bds.setMinIdle(3);
		bds.setMaxIdle(6);
		bds.setMaxOpenPreparedStatements(180);
	}
	public static void addDB(String ip, String dbname) {
		DataSource.dataSourceMap.put(ip, new DataSource(ip, dbname));
	}
	
	public static DataSource getInstance(String ip) {
		
		return DataSource.dataSourceMap.get(ip);
	}

	// static getConnection makes code thread-unsafe
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = this.bds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}

}
