package org.nhnnext.sharding;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Utility {

	public static void csClose(CallableStatement cs) {
		try {
			if (cs != null) {
				cs.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}

	public static void connClose(Connection conn) {
		try {
			if (conn != null) {
				conn.close();				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}

	public static void psmtClose(PreparedStatement psmt) {
		try {
			if (psmt != null) {
				psmt.close();				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}


}
