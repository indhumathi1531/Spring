package com.spring.learn_spring.game;

public class GameRunner {
	GamingConsole game;

	public GameRunner(GamingConsole game) {
		this.game = game;
	}

	public void run() {
		System.out.println("GameRunner : " + game);
		game.up();
		game.down();
		game.right();
		game.left();
	}

}
