package com.tweetapp;

import com.tweetapp.controller.MainMenuController;

public class TweetappApplication {

	public static void main(String[] args) {
		MainMenuController controller = new MainMenuController();
		controller.beforeLogin();

	}

}
