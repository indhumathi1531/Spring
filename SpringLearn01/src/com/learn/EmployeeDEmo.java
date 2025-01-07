package com.learn;

import org.springframework.context.ApplicationContext;

public class EmployeeDEmo {
	public static void main(String[] args) {
		Employee emp = new Employee();
		emp.setEid(101);
		emp.setEname("Indhu");
		emp.setEsalary(90000);

		System.out.println(emp);

		ApplicationContext context = new ClassPathXMLApplicationContext("Employee.xml");
		Employee e1 = Employee(context.getBean("emp1"));
		Employee e2 = context.getBean("emp1", Employee.class);

		// BeanFactory
		System.out.println(e1);
		System.out.println(e2);

	}

}
