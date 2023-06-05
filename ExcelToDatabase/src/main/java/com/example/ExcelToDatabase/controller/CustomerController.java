package com.example.ExcelToDatabase.controller;

import com.example.ExcelToDatabase.domain.Customer;
import com.example.ExcelToDatabase.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/uploadCustomersData")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file")MultipartFile file){
        this.customerService.saveCustomersToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Customers Data Uploaded and Saved to Database Successfully"));
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getCustomers(){
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.FOUND);
    }
}