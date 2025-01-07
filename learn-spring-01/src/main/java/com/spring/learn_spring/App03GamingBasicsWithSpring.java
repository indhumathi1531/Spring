package com.spring.learn_spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.spring.learn_spring.game.GameRunner;
import com.spring.learn_spring.game.GamingConsole;

public class App03GamingBasicsWithSpring {

	public static void main(String[] args) {

		try (var context = new AnnotationConfigApplicationContext(GamingConfiguration.class)) {
			context.getBean(GamingConsole.class).up();
			context.getBean(GameRunner.class).run();
		}
	}

}
