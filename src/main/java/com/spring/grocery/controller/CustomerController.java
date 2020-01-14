package com.spring.grocery.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.grocery.dao.CustomerRepository;
import com.spring.grocery.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CustomerController {

	@Autowired
	private final CustomerRepository repository;

	public CustomerController(CustomerRepository repository) {
		log.info("Employee Controller called with repository" + repository);
		this.repository = repository;		
	}

	@GetMapping("/customers")
	List<?> all() {
		log.info("Getting customers from the database");
		List<Customer> customers = repository.findAll()
				.stream()
				.collect(Collectors.toList());
		log.info("have list of employees");
		return customers;
	}
}
