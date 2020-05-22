package com.qa.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	private static Connection conn;

	private static Statement stmnt;

	public static void connect() throws SQLException, ClassNotFoundException {
		Class.forName(DBConfig.DRIVER);
		System.out.println("Connecting to Database...");
		conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
		stmnt = conn.createStatement();
	}

	public static void exUpdate(String query) throws SQLException {
		stmnt.executeUpdate(query);
	}

	public static ResultSet exQuery(String query) throws SQLException {
		ResultSet results = stmnt.executeQuery(query);
		return results;
	}

	public static void disconnect() throws SQLException {
		System.out.println("Disconnecting...");
		conn.close();
		System.out.println("Goodbye!");
	}
}