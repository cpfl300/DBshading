package org.nhnnext.sharding;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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

	public void attack() {
		Connection userConn = Season.connMap.get(this.dbIp);
		CallableStatement cs = null;
		
		String query = "{CALL attack(?, ?, ?, ?)}";

		try {
			//glodb에 insert
			while (Season.startFlag) {			
				int galaxyHP = 0;
				int galaxyId = pickOne() + 1;
			
				cs = userConn.prepareCall(query);
				
				cs.setInt(1, this.userId);
				cs.setInt(2, this.power);
				cs.setInt(3, galaxyId);
				cs.registerOutParameter(4, Types.INTEGER);
			
				cs.execute();
				galaxyHP = cs.getInt(4);
				
				if (galaxyHP <= 0) {
					Season.shutdown();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally{
			Utility.csClose(cs);
			
		}
	}
	
	private void makeAttackable() {
		this.attackable = new ArrayList<Integer>();
		
		for (int i=0; i<MainClass.GALAXY_NUM; ++i) {
			if (i != this.galaxyId) {
				attackable.add(i);
			}
		}
	}
	
	private int pickOne() {
		int one = (int)(Math.random() * MainClass.GALAXY_NUM - 1);
		
		return attackable.get(one);
	}
	
	private int getPower() {
		Connection conn = Season.connMap.get(this.dbIp);
		CallableStatement cs = null;
		int power = 0;
		String query = "{ CALL getPower(?, ?, ?) }";
		
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
			Utility.csClose(cs);
			
		}
		return power;
		
	}
	
}

