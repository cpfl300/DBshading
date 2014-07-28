package org.nhnnext.sharding;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Season {
	
	public static final int GALAXY_NUM = 4;
	
	public static List<Galaxy> galaxyList;
	public static ExecutorService shipExecutorService = Executors.newFixedThreadPool(4);
	public static ExecutorService signupExecutorService = Executors.newSingleThreadExecutor();
	public static Map<Integer, String> ipMap = new HashMap<Integer, String>();
	
	//은하 4개가 있어야 함
	public Season(){
		createConn();
		createGalaxy();
	}
	
	private void createConn() {
		DataSource.addDB(MainClass.GLO_DB_IP, MainClass.GLO_DB_NAME);
		DataSource.addDB("10.73.45.72", MainClass.YODA_DB_NAME);
		DataSource.addDB("10.73.45.59", MainClass.YODA_DB_NAME);
		
		ipMap.put(1, MainClass.YO_DB_IP1);
		ipMap.put(2, MainClass.YO_DB_IP2);
		ipMap.put(3, MainClass.YO_DB_IP1);
		ipMap.put(4, MainClass.YO_DB_IP2);
	}

	private void createGalaxy() {
		galaxyList = new ArrayList<Galaxy>();
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			galaxyList.add(new Galaxy(i+1));
		}
		
	}
	
	
	public void start() {
//		스레드 4개로 큐?에 들어있는 작업들을 처리
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			final Galaxy curGalaxy = this.galaxyList.get(i);
			
			Runnable runnable = new Runnable() {
				public void run() {
					curGalaxy.start();
				}
			};
			Season.shipExecutorService.execute(runnable);
		}
	}
	
	public void shutdown() {
		Season.shipExecutorService.shutdownNow();
		Season.signupExecutorService.shutdownNow();
	}
	
	public static void showStatus() {
		for (int i=0; i<2; i++) {
			showStatusByKey(i);
		}
	}
	
	private static void showStatusByKey(int key) {
		
		Connection yoda = DataSource.getInstance(ipMap.get(key)).getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "select GID, HP from galaxy where not HP = 100000";
		
		try {
			psmt = yoda.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				System.out.println("gid: " + rs.getInt(1) + ", hp: " + rs.getInt(2));
			}
			
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
	
	
//	random은하에 addUser를 호출하는 메서드 필요(스레드 1개)
	public void addUser() {
		Connection conn = DataSource.getInstance(MainClass.GLO_DB_IP).getConnection();
		CallableStatement cs = null;
		
		String query = "{CALL adduser(?, ?, ?)}";
		String ip = null;
		int userId = 0;
		int galaxyId = 0;

		try {
			//glodb에 insert
			cs = conn.prepareCall(query);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.TINYINT);
			cs.execute();
			
			//저장한 정보 가져오기
			userId = cs.getInt(1);
			int dbId = cs.getInt(2);
			galaxyId = cs.getInt(3);
//			if (dbId == 1) {
//				ip = MainClass.YO_DB_IP1;
//			}else {
//				ip = MainClass.YO_DB_IP2;
//			}
			
			galaxyList.get(galaxyId-1).addUser(userId, galaxyId, ipMap.get(dbId));
			
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
		
		
		
	}
	
}
