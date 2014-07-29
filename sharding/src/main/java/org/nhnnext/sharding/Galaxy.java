package org.nhnnext.sharding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Galaxy {
	
	private int pos;
	private List<User> userList; 
	
	public Galaxy(int pos){
		this.pos = pos;
		this.userList = new ArrayList<User>();
		
		saveAtDb(MainClass.YO_DB_IP1);
		saveAtDb(MainClass.YO_DB_IP2);
		
	}
	
	private void saveAtDb(String ip) {
		Connection yodaConn = DataSource.getInstance(ip).getConnection();
		PreparedStatement psmt = null;
		String sql = "insert into galaxy values(?, ?, ?)";
		
		try {
			psmt = yodaConn.prepareStatement(sql);
			psmt.setInt(1, this.pos);
			psmt.setString(2, "galaxy" + this.pos);
			psmt.setInt(3, MainClass.GALAXY_HP);
			psmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			Utility.psmtClose(psmt);
			Utility.connClose(yodaConn);
			
		}
		
	}

	public void addUser(int userId, int galaxyId, String dbIp){
		//1. db에 저장한다
		Connection yodaConn = DataSource.getInstance(dbIp).getConnection();

		PreparedStatement psmt = null;
		String yodaQuery = "insert into user values (?, ?)";
		
		try {
			psmt = yodaConn.prepareStatement(yodaQuery);
			psmt.setInt(1, userId);
			psmt.setInt(2, galaxyId);
			psmt.execute();
			
			//2. 인스턴스를 만들어서 userList에 넣는다
			userList.add(new User(userId, galaxyId, dbIp));
					
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			Utility.psmtClose(psmt);
			Utility.connClose(yodaConn);
			
		}
		
	}

	public void start() {
		System.out.println("user size: " + this.userList.size());
		for (int i=0; i<this.userList.size(); ++i) {
			User curUser = this.userList.get(i);
			curUser.attack();
		}
		
	}
}
