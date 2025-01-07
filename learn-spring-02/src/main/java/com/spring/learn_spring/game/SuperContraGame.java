package com.spring.learn_spring.game;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SuperContraGame implements GamingConsole {

	public void up() {
		System.out.println("Go Up");

	}

	public void down() {
		System.out.println("Go Down");

	}

	public void right() {
		System.out.println("Shoot");

	}

	public void left() {
		System.out.println("Go back");

	}
}
