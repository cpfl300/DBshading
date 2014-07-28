package org.nhnnext.sharding;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
	private int userId;
	private int galaxyId;
	private int power;
	private String dbIp;
	private List<Integer> attackable;
	
	public User(int userId, int galaxyId, String dbIp){
		this.userId = userId;
		this.galaxyId = galaxyId;
		this.dbIp = dbIp;
		this.power = getPower();
		makeAttackable();
	}

private int getPower() {
	Connection conn = DataSource.getInstance(this.dbIp).getConnection();
	CallableStatement cs = null;
	int power = 0;
//	(IN UserID int, IN GID int, OUT POWER int)
	String query = "{CALL getPower(?, ?, ?)}";
	try {
		//glodb에 insert
		cs = conn.prepareCall(query);
		cs.setInt(1, this.userId);
		cs.setInt(2, this.galaxyId);
		
		cs.registerOutParameter(3, Types.INTEGER);
		cs.execute();
		
		//저장한 정보 가져오기
		power = cs.getInt(3);
		
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally{
		try {
			if (cs == null) {
				System.out.println("is NULL");
			}
			else {
				
				cs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return power;
	
}

	public void attack() {
		int targetId = pickOne();
		String targetIp = Season.ipMap.get(targetId);
		Connection yoda = DataSource.getInstance(targetIp).getConnection();
		PreparedStatement psmt = null;
		String sql = "update galaxy set HP = HP - ? WHERE gid = ?";
		
		try {
			psmt = yoda.prepareStatement(sql);
			psmt.setInt(1, this.power);
			psmt.setInt(2, targetId);
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
	
	private void makeAttackable() {
		this.attackable = new ArrayList<Integer>();
		
		for (int i=0; i<Season.GALAXY_NUM; ++i) {
			if (i != this.galaxyId) {
				attackable.add(i);
			}
		}
	}
	
	private int pickOne() {
		int one = (int)(Math.random() * Season.GALAXY_NUM-1);
		return attackable.get(one);
	}
	
	public void attackRandom(){
		Runnable runnable = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				attack();
				Season.showStatus();
			}
			
		};
		
		Season.shipExecutorService.execute(runnable);
		
	}
		
}

