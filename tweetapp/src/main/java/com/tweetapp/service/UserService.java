package com.tweetapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tweetapp.dao.UserDao;

public class UserService {

	public UserDao userDao = new UserDao();
	public Connection con = null;
	public PreparedStatement ps = null;
	public Statement st = null;

	public boolean registerUser(String fn, String ln, String g, String dob, String email, String password) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("select * from users where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				System.out.println(
						"Email already in use. Try registering with another email or login using the same email.");
			} else {
				ps = con.prepareStatement(
						"insert into users(first_name,last_name,gender,date_of_birth,email,password) values(?,?,?,?,?,?)");
				ps.setString(1, fn);
				ps.setString(2, ln);
				ps.setString(3, g);
				ps.setString(4, dob);
				ps.setString(5, email);
				ps.setString(6, password);
				ps.executeUpdate();
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}
		return false;
	}

	public boolean login(String email, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("select * from users where email = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String pass = rs.getString(6);
			if (pass.equals(password)) {
				System.out.println("Logged in Successfully.");
				ps = con.prepareStatement("update users set logged_in = ? where email=?");
				ps.setString(1, "TRUE");
				ps.setString(2, email);
				ps.executeUpdate();
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Incorrect Details. Please try again.");
		return false;
	}

	public boolean forgotPassword(String email, String dob, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("select * from users where email = ? ");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (rs != null) {
				if (rs.getString(4).equals(dob)) {
					ps = con.prepareStatement("update users set password =? where email=?");
					ps.setString(1, password);
					ps.setString(2, email);
					ps.executeUpdate();
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public void viewAllUsers(String email) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("select logged_in from users where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String loggedIn = rs.getString(1);
			if (loggedIn.equalsIgnoreCase("true")) {
				ps = con.prepareStatement("select * from users");
				rs = ps.executeQuery();
				System.out.println("List Of Users :");
				while (rs.next()) {
					System.out.println(rs.getString(5));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean resetPassword(String email, String oldPassword, String newPassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("update users set password =? where email=?");
			ps.setString(1, newPassword);
			ps.setString(2, email);
			ps.executeUpdate();
			return true;

		} catch (Exception e) {
			System.out.println(e);
		}
		return false;

	}

	public void logout(String email) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetapp", "root", "root");
			ps = con.prepareStatement("update users set logged_in = ? where email=?");
			ps.setString(1, "false");
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
