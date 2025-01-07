package com.test;

public class Employees {
	private String name;
	private long salary;

	public Employees(String name, long salary) {
		super();
		this.name = name;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSalary() {
		return salary;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public String toString() {
		return String.valueOf(salary);
	}
}
