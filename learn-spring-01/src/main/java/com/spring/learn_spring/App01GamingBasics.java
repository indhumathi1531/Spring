package com.spring.learn_spring;

import com.spring.learn_spring.game.GameRunner;
import com.spring.learn_spring.game.MarioGame;
import com.spring.learn_spring.game.PacMan;
import com.spring.learn_spring.game.SuperContraGame;

public class App01GamingBasics {

	public static void main(String[] args) {
		// svar game = new MarioGame();
		// var game = new SuperContraGame();
		var game = new PacMan();
		var gameRunner = new GameRunner(game);

		gameRunner.run();
	}

}
