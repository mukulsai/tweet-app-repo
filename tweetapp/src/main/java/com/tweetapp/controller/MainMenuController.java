package com.tweetapp.controller;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;

public class MainMenuController {
	UserService userService = new UserService();
	TweetService tweetService = new TweetService();

	public void beforeLogin() {
		int choice = -1;
		String firstName, lastName, gender, dob, email, password;
		do {

			System.out.println(
					"Hi!\nChoose from the below options and enter your choice:\n1.Register\n2.Login\n3.Forgot Password\n4.Exit");
			Scanner sc = new Scanner(System.in);
			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				System.out.println("Invalid Choice");
			}

			switch (choice) {
			case 1:
				sc.nextLine();
				System.out.println("Enter your First Name :");
				firstName = sc.nextLine();
				System.out.println("Enter your Last Name :");
				lastName = sc.nextLine();
				// sc.nextLine();
				System.out.println("Enter your Gender(M or F) :");
				gender = sc.nextLine();
				System.out.println("Enter your Date Of Birth(dd/mm/YYYY) :");
				dob = sc.nextLine();
				System.out.println("Enter your Email :");
				email = sc.nextLine();
				System.out.println(
						"Enter your password (It must be 8-20 characters, must contain digits, one upper case and one lower case alphabet) :");
				password = sc.nextLine();

				boolean validFirstName = isValidName(firstName);
				boolean validLastName = isValidName(lastName);
				boolean validGender = false;
				if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"))
					validGender = true;
				boolean validDOB = isValidDateOfBirth(dob);
				boolean validEmail = isValidEmail(email);
				boolean validPassword = isValidPassword(password);

				if (validFirstName && validLastName && validEmail && validPassword && validDOB && validGender) {
					if (userService.registerUser(firstName, lastName, gender, dob, email, password)) {
						System.out.println("Registration Successful. Please login using the same details.");
					} else
						System.out.println("Registration Unsuccessful.");
				}
				if (!validFirstName) {
					System.out.println("Invalid First Name");
				}
				if (!validLastName) {
					System.out.println("Invalid Last Name");
				}
				if (!validEmail) {
					System.out.println("Invalid Email");
				}
				if (!validDOB) {
					System.out.println("Invalid Date Of Birth. Please enter in dd/mm/YYYY format.");
				}
				if (!validPassword) {
					System.out.println(
							"Invalid Password(It must be 8-20 characters, must contain digits, one upper case and one lower case alphabet)");
				}
				if (!validGender) {
					System.out.println("Invalid Gender");
				}
				break;

			case 2:
				sc.nextLine();
				System.out.println("Enter your Email :");
				email = sc.nextLine();
				System.out.println("Enter your password :");
				password = sc.nextLine();
				if (isValidEmail(email)) {
					if (userService.login(email, password)) {
						afterLogin(email, password);
					}
				}
				if (!isValidEmail(email)) {
					System.out.println("Invalid Email");
				}

				break;
			case 3:
				sc.nextLine();
				System.out.println("Enter your Email :");
				email = sc.nextLine();

				System.out.println("Enter your Date Of Birth(dd/mm/YYYY) :");
				dob = sc.nextLine();

				System.out.println("Enter your New Password :");
				password = sc.nextLine();
				if (isValidEmail(email) && isValidDateOfBirth(dob) && isValidPassword(password)) {
					if (userService.forgotPassword(email, dob, password))
						System.out.println("Updated password succesfully. Login using new password.");
					else
						System.out.println("Incorrect Details");
				}
				if (!isValidEmail(email)) {
					System.out.println("Invalid Password. Please try again.");
				}
				if (!isValidDateOfBirth(dob))
					System.out.println("Invalid Date Of Birth. Please enter in dd/mm/YYYY format.");
				if (!isValidPassword(password))
					System.out.println(
							"Invalid Password(It must be 8-20 characters, must contain digits, one upper case and one lower case alphabet)");
				break;
			case 4:
				System.out.println("Thank you!");
				sc.close();
				break;
			}

		} while (choice != 4);
	}

	public void afterLogin(String email, String password) {
		int choice = -1;
		do {
			System.out.println("Hi " + email + "!");
			System.out.println(
					"Below are the options you can choose from \n1.Post a tweet.\n2.View my tweets.\n3.View all tweets.\n4.View all users.\n5.Reset Password\n6.Logout");
			System.out.println("Enter your choice:");
			Scanner sc = new Scanner(System.in);
			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				System.out.println("Invalid Choice");
			}
			switch (choice) {
			case 1:
				sc.nextLine();
				System.out.println("Enter the tweet you would like to post:");
				String tweet = sc.nextLine();
				if (tweetService.postTweet(email, tweet))
					System.out.println("Tweet posted Successfully.");
				else
					System.out.println("Something went wrong. Please try again.");
				break;
			case 2:
				tweetService.myTweets(email);
				break;
			case 3:
				tweetService.viewAllTweets(email);
				break;
			case 4:
				userService.viewAllUsers(email);
				break;
			case 5:
				sc.nextLine();
				System.out.println("Enter old password: ");
				String oldPassword = sc.nextLine();
				System.out.println("Enter new password:");
				String newPassword = sc.nextLine();
				if (oldPassword.equals(password)) {
					if (isValidPassword(newPassword)) {
						if (userService.resetPassword(email, oldPassword, newPassword))
							System.out.println("Password Reset Successful");
						else
							System.out.println("Incorrect details. Please try again.");
					} else
						System.out.println("Invalid Password");
				}
				break;
			case 6:
				userService.logout(email);
				System.out.println("Logged out");
				break;
			}
		} while (choice != 6);

	}

	public static boolean isValidPassword(String password) {

		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";

		Pattern p = Pattern.compile(regex);
		if (password == null) {
			return false;
		}
		Matcher m = p.matcher(password);
		return m.matches();
	}

	public static boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(email);
		return m.matches();

	}

	public static boolean isValidDateOfBirth(String dob) {
		String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(dob);
		return m.matches();
	}

	public static boolean isValidName(String name) {

		String regex = "^[A-Za-z]\\w{5,29}$";
		Pattern p = Pattern.compile(regex);
		if (name == null) {
			return false;
		}
		Matcher m = p.matcher(name);
		return m.matches();
	}

}
