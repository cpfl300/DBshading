package org.nhnnext.sharding;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Season {
	
	public static final int GALAXY_NUM = 4;
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String USERNAME = "sharding";
	private static final String USERPW = "";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/glodb?noAccessToProcedureBodies=true";
	
	public static List<Galaxy> galaxyList;
	public static ExecutorService shipExecutorService = Executors.newFixedThreadPool(4);
	public static ExecutorService signupExecutorService = Executors.newSingleThreadExecutor();
	
	//은하 4개가 있어야 함
	public Season(){
		createConn();
		createGalaxy();
	}
	
	private void createConn() {
		DataSource.init(Season.DB_DRIVER, Season.USERNAME, Season.USERPW, Season.DB_URL);
	}

	private void createGalaxy() {
		galaxyList = new ArrayList<Galaxy>();
		
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			
			galaxyList.add(new Galaxy(i));
		}
		
	}
	
//	public void dbtest() {
//		Connection conn = DataSource.getInstance().getConnection();
//		PreparedStatement psmt = null;
//		ResultSet rs = null;
//		String query = "select * from db;";
//		
//		try {
//			psmt = conn.prepareStatement(query);
//			rs = psmt.executeQuery();
//			while(rs.next()){
//				System.out.println(rs.getString("dbname"));
//				System.out.println(rs.getString("ip"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			try {
//				rs.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				psmt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		
//		
//		
//		
//		
//	}
	
	public void start() {
//		스레드 4개로 큐?에 들어있는 작업들을 처리
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			final Galaxy curGalaxy = this.galaxyList.get(i);
			
			Runnable runnable = new Runnable() {

				public void run() {
					curGalaxy.start();
					
				}
								
			};
			
			Thread thread = new Thread(runnable);
			thread.start();
			
		}
		
	}
	
	public void shutdown() {
		Season.shipExecutorService.shutdown();
		Season.signupExecutorService.shutdown();
	}
	
//	random은하에 addUser를 호출하는 메서드 필요(스레드 1개)
	public void addUser(int galaxyPos) {
		Connection conn = DataSource.getInstance().getConnection();
		CallableStatement cs = null;
//		ResultSet rs = null;
		
		String query = "{CALL adduser(?, ?, ?)}";

		try {
			cs = conn.prepareCall(query);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.TINYINT);
			cs.execute();
			
			System.out.println("userID: "+cs.getInt(1));
			System.out.println("userGID: "+cs.getInt(2));
			System.out.println("userDBID: "+cs.getInt(3));
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
		
//		final Galaxy galaxy = this.galaxyList.get(galaxyPos);
//		
//		Runnable runnable = new Runnable() {
//
//			public void run() {		
//				galaxy.addUser();
//			}
//			
//		};
//		
//		Season.signupExecutorService.execute(runnable);
		
	}
	
}
