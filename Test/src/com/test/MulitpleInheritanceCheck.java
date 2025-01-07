package com.test;

interface Dog {
	void bark();
}

interface Cat {
	void meow();
}

class A1 {
	void display() {
		System.out.println("I am method from class A");
	}
}

class B {
	void print() {
		System.out.println("I am method from class B");
	}
}

class C extends A1 {

	void print() {
		System.out.println("I am method from class B");
	}
}

class Animal implements Dog, Cat {
	public void bark() {
		System.out.println("Dog is barking");
	}

	public void meow() {
		System.out.println("Cat is meowing");
	}
}

public class MulitpleInheritanceCheck {

	public static void main(String[] args) {
		Animal a = new Animal();
		a.bark();
		a.meow();

	}

}
