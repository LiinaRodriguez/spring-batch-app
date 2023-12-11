package com.example.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springbatch.entity.Customer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
