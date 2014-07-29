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


public class Season {
	
	public static List<Galaxy> galaxyList = new ArrayList<Galaxy>();
	public static boolean startFlag = false;
	public static Map<Integer, String> ipMap = new HashMap<Integer, String>();
	
	//은하 4개가 있어야 함
	public Season() {
		createConn();
		createGalaxy();
	}
	
	public void start() {
		Season.startFlag = true;
		Thread thread = null;
		
		// galaxy , thread 4
		for (int i=0; i<MainClass.GALAXY_NUM; i++) {
			final Galaxy curGalaxy = Season.galaxyList.get(i);
			
			Runnable runnable = new Runnable() {
				
				public void run() {
					while (Season.startFlag) {
						curGalaxy.start();						
					}
				}
			};
			thread = new Thread(runnable);
			thread.start();

		}		
		
		// view, thread 1
		Runnable runnable = new Runnable() {

			public void run() {
				while (Season.startFlag) {
					viewStatus();
				}
				
			}
			
		};
		thread = new Thread(runnable);
		thread.start();

	}
	
	public void shutdown() {
		Season.startFlag = false;
	}
	

//	random은하에 addUser를 호출하는 메서드 필요(스레드 1개)
	public void signUp(final int count) {
		Thread thread = null;
//		for (int i=0; i<count; ++i) {
		Runnable runnable = new Runnable() {

			public void run() {
				
//				for (int i=0; i<count; ++i) {
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
						
						for (int i=0; i<count; ++i) {
							cs.execute();
							
							//저장한 정보 가져오기
							userId = cs.getInt(1);
							int dbId = cs.getInt(2);
							galaxyId = cs.getInt(3);
							
							galaxyList.get(galaxyId-1).addUser(userId, galaxyId, Season.ipMap.get(dbId));
							if (userId % 100 == 1) {
								System.out.println("user num: " + userId);							
							}
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
						
					} finally{
						Utility.csClose(cs);
						Utility.connClose(conn);
						
					}
				}
//			}
		};
		
		thread = new Thread(runnable);
		thread.start();	
//		}
		
	}

	private void viewStatus() {
		try {
			Thread.sleep(1000);
			
			for (int i = 1; i < 5; i++) {
				final int key = i;

				Connection yodaConn = DataSource.getInstance(Season.ipMap.get(key)).getConnection();
				PreparedStatement psmt = null;
				ResultSet rs = null;
				String sql = "select GID, HP from galaxy where not HP = "+ MainClass.GALAXY_HP;

				try {
					psmt = yodaConn.prepareStatement(sql);
					rs = psmt.executeQuery();

					while (rs.next()) {
						System.out.println("gid: " + rs.getInt(1) + ", hp: " + rs.getInt(2));
					}

				} catch (SQLException e) {
					e.printStackTrace();

				} finally {
					Utility.psmtClose(psmt);
					Utility.connClose(yodaConn);

				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}

	
	private void createConn() {
		DataSource.addDB(MainClass.GLO_DB_IP, MainClass.GLO_DB_NAME);
		DataSource.addDB(MainClass.YO_DB_IP1, MainClass.YODA_DB_NAME);
		DataSource.addDB(MainClass.YO_DB_IP2, MainClass.YODA_DB_NAME);
		
		Season.ipMap.put(1, MainClass.YO_DB_IP1);
		Season.ipMap.put(2, MainClass.YO_DB_IP2);
		Season.ipMap.put(3, MainClass.YO_DB_IP1);
		Season.ipMap.put(4, MainClass.YO_DB_IP2);
	}

	private void createGalaxy() {
		
		for (int i=0; i<MainClass.GALAXY_NUM; i++) {
			galaxyList.add(new Galaxy(i+1));
			
		}
		
	}
	
	
}
