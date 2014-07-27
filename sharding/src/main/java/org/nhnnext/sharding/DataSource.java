package org.nhnnext.sharding;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

// THIS IS SINGLETON PATTERN
public class DataSource {
	private static DataSource dataSource;
	private BasicDataSource bds;
	
	// CALLED BY DatabaseContextListener
	public static void init(String driver, String userName, String userPw, String dbUrl) {
		dataSource = new DataSource(driver, userName, userPw, dbUrl);
	}
	
	// THIS CONSTRUCTOR RUNS ONLY ONCE
	// 이 부분을 spring으로 injection
	private DataSource(String driver, String userName, String userPw, String dbUrl) {
		bds = new BasicDataSource();
		bds.setDriverClassName(driver);
		bds.setUsername(userName);
		bds.setPassword(userPw);
		bds.setUrl(dbUrl);
		
		bds.setMinIdle(3);
		bds.setMaxIdle(6);
		bds.setMaxOpenPreparedStatements(180);
	}
	
	public static DataSource getInstance() {
		return dataSource;
	}

	// static getConnection makes code thread-unsafe
	public Connection getConnection() {
		Connection con = null;
		try {
			con = this.bds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
