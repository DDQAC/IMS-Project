package com.qa.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.qa.utils.DB;

public class User {

	int userID;

	String username;

	String password;

	Customer customerProfile;

	public User() {

	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}

	public User(int userID, String username, String password, Customer customerProfile) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.customerProfile = customerProfile;
	}

	@Override
	public String toString() {
		return "User [userID: " + userID + " | username: " + username + " | password: " + password
				+ " | customerProfile: " + customerProfile + "]";
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Customer getCustomerProfile() {
		return customerProfile;
	}

	public void setCustomerProfile(Customer customerProfile) {
		this.customerProfile = customerProfile;
	}

	public static User search(String name) {

		int UID = 0;
		String username = null;
		String password = null;
		int CID = 0;

		String sql = "SELECT * FROM users WHERE username = \"" + name + "\"";

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				UID = rs.getInt("UID");
				username = rs.getString("username");
				password = rs.getString("password");
				CID = rs.getInt("FK_CID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Customer customerProfile = Customer.search(CID);
		return new User(UID, username, password, customerProfile);

	}

	public static User search(int userID) {

		int UID = 0;
		String username = null;
		String password = null;
		int CID = 0;

		String sql = "SELECT * FROM users WHERE UID = " + userID;

		try {
			ResultSet rs = DB.exQuery(sql);
			while (rs.next()) {
				UID = rs.getInt("UID");
				username = rs.getString("username");
				password = rs.getString("password");
				CID = rs.getInt("FK_CID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Customer customerProfile = Customer.search(CID);
		return new User(UID, username, password, customerProfile);

	}

	public void registerNewUser() {

		String username = this.getUsername();
		String password = this.getPassword();

		String sql = "INSERT INTO users (username, password) VALUES (\"" + username + "\", \"" + password + "\")";

		String retrieveID = "SELECT UID FROM users ORDER BY UID DESC LIMIT 1";

		try {
			DB.exUpdate(sql);
			ResultSet rs = DB.exQuery(retrieveID);
			while (rs.next()) {
				this.setUserID(rs.getInt("UID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void registerAsCustomer(Customer cust) {

		cust.add();
		this.setCustomerProfile(cust);
		int UID = this.getUserID();
		int CID = cust.getCustomerID();

		String sql = "UPDATE users SET FK_CID = " + CID + " WHERE UID = " + UID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean login() {

		username = this.getUsername();
		password = this.getPassword();

		String sql = "SELECT password FROM users WHERE username = \"" + username + "\"";

		try {
			ResultSet rs = DB.exQuery(sql);
			if (!rs.isBeforeFirst()) {
				System.out.println(
						"That user does not exist! If this is your first time using the system then you will need to register...");
			}
			while (rs.next()) {
				if (password.equals(rs.getString("password"))) {
					System.out.println("PASSWORD CORRECT: Logging in as " + username);
					return true;
				} else {
					System.out.println("PASSWORD INCORRECT: The username and password do not match!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	public void update(int userID) {

		String username = this.getUsername();
		String password = this.getPassword();

		String sql = "UPDATE users SET username = \"" + username + "\", password = \"" + password + "\" WHERE UID = "
				+ userID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(int userID) {

		String sql = "DELETE FROM users WHERE UID = " + userID;

		try {
			DB.exUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
