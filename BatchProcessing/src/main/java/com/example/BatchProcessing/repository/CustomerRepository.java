package com.example.BatchProcessing.repository;

import com.example.BatchProcessing.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
