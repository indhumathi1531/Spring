package com.spring.learn_spring.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("MarioGameQualifier")
public class MarioGame implements GamingConsole {

	public void up() {
		System.out.println("Jump");

	}

	public void down() {
		System.out.println("Go inside hole");

	}

	public void right() {
		System.out.println("Accelerate");

	}

	public void left() {
		System.out.println("Go back");

	}

}
