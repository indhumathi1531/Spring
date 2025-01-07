package com.spring.learn_spring.game;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.spring/learn_spring.game")
public class App03GamingBasicsWithSpring {

	public static void main(String[] args) {

		try (var context = new AnnotationConfigApplicationContext(App03GamingBasicsWithSpring.class)) {
			context.getBean(GamingConsole.class).up();
			context.getBean(GameRunner.class).run();
		}
	}

}
