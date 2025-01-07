package com.spring.learn_spring.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GameRunner {
	GamingConsole game;

	public GameRunner(@Qualifier("MarioGameQualifier") GamingConsole game) {
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
