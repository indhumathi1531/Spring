package com.spring.learn_spring.game.a1;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
class DependencyInjection {
	@Autowired
	Dependency1 dependency1;
	@Autowired
	Dependency2 dependency2;

	/*
	 * public DependencyInjection(Dependency1 dependency1, Dependency2 dependency2)
	 * { super();
	 * System.out.println("Using constructor injection - DependencyInjection");
	 * this.dependency1 = dependency1; this.dependency2 = dependency2; }
	 */

	@Autowired
	public void setDependency1(Dependency1 dependency1) {
		System.out.println("Using setDependency1");
		this.dependency1 = dependency1;
	}

	@Autowired
	public void setDependency2(Dependency2 dependency2) {
		System.out.println("Using setDependency2");
		this.dependency2 = dependency2;
	}

	public String toString() {
		return "Using " + dependency1 + " and " + dependency2;
	}

}

@Component
class Dependency1 {

}

class Dependency2 {

}

@Configuration
@ComponentScan
public class DependencyLauncherGamingApp {

	public static void main(String[] args) {

		try (var context = new AnnotationConfigApplicationContext(DependencyLauncherGamingApp.class)) {
			// Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
			System.out.println(context.getBean(DependencyInjection.class));
		}
	}

}
