package Database;

import java.sql.*;

public class Connect {
private static Connection con;

public static Connection getConnect() {
	try {
		String url = "jdbc:sqlserver://DESKTOP-3E8MFQU:1433;databaseName=FantaChat;user=sa;password=sa";
		con = DriverManager.getConnection(url);
	} catch (Exception e) {
		System.out.println(" Connected failed!");
	}
	return con;
}

public static String testConnect() {
	try {
		con=Connect.getConnect();
		 return "Connected";
	} catch (Exception e) {
		return " Connected failed!";
	}
}
}
