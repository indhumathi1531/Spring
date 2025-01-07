package com.spring.learn_spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.learn_spring.game.GameRunner;
import com.spring.learn_spring.game.GamingConsole;
import com.spring.learn_spring.game.PacMan;
import com.spring.learn_spring.game.SuperContraGame;

@Configuration
public class GamingConfiguration {
	@Bean
	public GamingConsole game() {
		// var game = new PacMan();
		var game = new SuperContraGame();
		return game;
	}

	@Bean
	public GameRunner gameRunner(GamingConsole game) {
		var gameRunner = new GameRunner(game);
		return gameRunner;
	}

}
