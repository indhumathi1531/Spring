package com.spring.learn_spring.helloWorld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

record Person(String name, int age, Address address) {
}

record Address(String firstLine, String city) {
}

@Configuration
public class HelloWorldConfiguration {
	@Bean
	public String name() {
		return "Indhu";
	}

	@Bean
	public int age() {
		return 24;
	}

	@Bean
	public Person person() {
		return new Person("vaibhav", 19, new Address("Tanjore", "TN"));
	}

	@Bean
	public Person personwithMethodCall() {
		return new Person(name(), age(), address());
	}

	@Bean(name = "customizedName")
	public Address address() {
		return new Address("Erode", "TamilNadu");
	}

}
