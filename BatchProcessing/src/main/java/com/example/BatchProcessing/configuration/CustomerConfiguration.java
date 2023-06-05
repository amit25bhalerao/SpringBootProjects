package com.example.BatchProcessing.configuration;

import com.example.BatchProcessing.domain.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerConfiguration implements ItemProcessor <Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
            return customer;
    }
}