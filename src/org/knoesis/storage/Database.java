package org.knoesis.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class Database {
	
	/**
	 * Getting connection for the database
	 * @param username
	 * @param password
	 * @return
	 * 
	 * TODO: Should write a separate class for all DB related things.
	 */
	public static Connection getConnectionToDB(String username,String password){
		Connection conn = null;
		System.out.println(new Date() + " Connecting to Database");
		String url = "jdbc:mysql://130.108.5.96/twitris_healthcare?user=" + username + "&password=" + password;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database");
		return conn;

	}
}
