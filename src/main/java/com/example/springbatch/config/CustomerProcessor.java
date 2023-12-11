package com.example.springbatch.config;

import com.example.springbatch.entity.Customer;
import com.example.springbatch.repository.CustomerRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer process(Customer customer) throws Exception {
      /*  Optional<Customer> existingCustomer = customerRepository.findById(customer.getId());
        if (existingCustomer.isPresent()) {
            Customer currentCustomer = existingCustomer.get();
            if (!currentCustomer.equals(customer)) {
                customer.setLastModified(LocalDateTime.now());
                return customer;
            } else {
                return null;
            }
        } else {
            customer.setLastModified(LocalDateTime.now());
            return customer;
        }*/
        return customer;
    }
}
