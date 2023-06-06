package com.example.EmployeeCRUDApplication.repository;

import com.example.EmployeeCRUDApplication.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}