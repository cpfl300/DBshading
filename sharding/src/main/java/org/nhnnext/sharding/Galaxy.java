package org.nhnnext.sharding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Galaxy {
	
	private int pos;
	private int HP = 100000;
	private List<User> userList; 
	
	public int getPos() {
		return pos;
	}

	
	public int getHP() {
		return HP;
	}

	
	public Galaxy(int pos){
		this.pos = pos;
		this.userList = new ArrayList<User>();
		
		saveAtDb(MainClass.YO_DB_IP2);
		saveAtDb(MainClass.YO_DB_IP1);
		
	}
	
	private void saveAtDb(String ip) {
		Connection yoda = DataSource.getInstance(ip).getConnection();
		PreparedStatement psmt = null;
		String sql = "insert into galaxy values(?, ?, ?)";
		
		try {
			psmt = yoda.prepareStatement(sql);
			psmt.setInt(1, this.pos);
			psmt.setString(2, "galaxy"+this.pos);
			psmt.setInt(3, this.HP);
			psmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				yoda.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void addUser(int userId, int galaxyId, String dbIp){
		//1. db에 저장한다
		Connection yodaConn = DataSource.getInstance(dbIp).getConnection();

		PreparedStatement yodaPsmt = null;
		String yodaQuery = "insert into user values (?, ?)";
		
		try {
			yodaPsmt = yodaConn.prepareStatement(yodaQuery);
			yodaPsmt.setInt(1, userId);
			yodaPsmt.setInt(2, galaxyId);
			yodaPsmt.execute();
			
			//2. 인스턴스를 만들어서 userList에 넣는다
			userList.add(new User(userId, galaxyId, dbIp));
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				yodaPsmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				yodaConn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
	
//	synchronized public void minusHP(int power){
//		this.HP -= power;
//	}

//	public void isAlive() {
//		if (this.HP <= 0) {
////			스레드를 모두 종료시킨 후 프로그램을 종료한다.
//			Season.shipExecutorService.shutdown();
//			Season.signupExecutorService.shutdown();
//
////			각 은하의 HP를 모두 출력해 준 후
//			for(int i = 0; i < Season.GALAXY_NUM; i++){
//				System.out.println(Season.galaxyList.get(i).getPos()+"번 은하 HP: "+Season.galaxyList.get(i).getHP());
//			}
//			System.out.println("--GAME END--");
//			
//			System.exit(1);
//		}
//		
//	}

	public void start() {
		
		Iterator<User> ir = this.userList.iterator();
		
		while(ir.hasNext()) {
			User curUser = ir.next();
			curUser.attackRandom();			
		}
		
	}
}
