package com.learn;

public class Employee {
	int eid;
	String eName;
	int eSalary;

	public Employee() {

	}

	public Employee(int eid, String eName, int eSalary) {
		this.eid = eid;
		this.eName = eName;
		this.eSalary = eSalary;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public void setEname(String eName) {
		this.eName = eName;
	}

	public void setEsalary(int eSalary) {
		this.eSalary = eSalary;
	}

	public int getEid() {
		return this.eid;
	}

	public String getEname() {
		return this.eName;
	}

	public int getEsalary() {
		return this.eSalary;
	}

	public String toString() {
		return "eid " + eid + " eName " + eName + " eSalary " + eSalary;
	}

}
