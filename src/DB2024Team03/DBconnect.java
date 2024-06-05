package DB2024Team03;

import java.sql.*;

public class DBconnect {
	private static Connection connection = null;

	//JDBC driver name and database URL, USER, Password
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/DB2024Team03";

	static final String USER = "DB2024Team03";
	static final String PASS = "DB2024Team03";

	//JDBC와 연결
	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				//db와 연결
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("데이터베이스 연결 실패");
			}
		}
		return connection;
	}

	//DB 연결 끊기
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