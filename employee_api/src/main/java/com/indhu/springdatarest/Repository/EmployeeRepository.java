package com.indhu.springdatarest.Repository;

import org.springframework.data.repository.CrudRepository;

import com.indhu.springdatarest.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
