package com.example.EmployeeCRUDApplication.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.EmployeeCRUDApplication.connect.EmployeeBookConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeCRUDApplication.Error.ResourceNotFoundException;
import com.example.EmployeeCRUDApplication.model.Employee;
import com.example.EmployeeCRUDApplication.repository.EmployeeRepository;

import com.example.EmployeeCRUDApplication.connect.DefineBook;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final EmployeeBookConnect employeeBookConnect;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, EmployeeBookConnect employeeBookConnect) {
        this.employeeRepository = employeeRepository;
        this.employeeBookConnect = employeeBookConnect;
    }

    @GetMapping("/employees")
    public List < Employee > getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity < Employee > getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity < Employee > updateEmployee(@PathVariable(value = "id") Long employeeId, @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map < String, Boolean > deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/employee/{userId}/book/{bookId}")
    public ResponseEntity<String> fetchEmployeeAndBookDetails(@PathVariable int userId, @PathVariable int bookId) {
        Optional<Employee> employeeOptional = employeeRepository.findById((long) userId);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mono<DefineBook> bookMono = employeeBookConnect.fetchBookDetails(bookId);
        Optional<DefineBook> bookOptional = bookMono.blockOptional();
        if (bookOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String responseString = "Employee details: " + employeeOptional.get().toString() + "\nBook details: " + bookOptional.get().toString();
        return ResponseEntity.ok(responseString);
    }


}