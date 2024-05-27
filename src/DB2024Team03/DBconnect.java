package DB2024Team03;

import java.sql.*;

public class DBconnect {
	private static Connection connection = null;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/DB2024Team03";
	
	static final String USER = "DB2024Team03";
	static final String PASS = "DB2024Team03";

	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("데이터베이스 연결 성공");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("데이터베이스 연결 실패");
			}
		}
		return connection;
	}

	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				System.out.println("데이터베이스 연결 종료");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("데이터베이스 연결 종료 실패");
			}
		}
	}

}
