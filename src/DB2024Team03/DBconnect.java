package DB2024Team03;

import java.sql.*;

public class DBconnect {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/DB2024Team03";
	
	static final String USER = "DB2024Team03";
	static final String PASS = "DB2024Team03";
	
	public static Connection getConnection() throws SQLException {
		System.out.println("db 연결 성공");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

}
