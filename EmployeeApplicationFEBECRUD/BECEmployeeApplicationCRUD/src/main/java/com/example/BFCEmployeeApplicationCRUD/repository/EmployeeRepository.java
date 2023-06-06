package com.example.BFCEmployeeApplicationCRUD.repository;

import com.example.BFCEmployeeApplicationCRUD.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
