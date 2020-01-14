package com.spring.grocery.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.grocery.dao.ProductRepository;

@RestController
public class ProductController {

	
	private ProductRepository repository;
	
	@Autowired
	public ProductController(ProductRepository prod) {
		// TODO Auto-generated constructor stub
		this.repository = prod;
	}

	@GetMapping(value="/product-list")
	public List<?> getProductList(HttpServletRequest request) {		
		return repository.findAll().stream().map(prod -> prod.getProduct()).collect(Collectors.toList());	
	}
}
