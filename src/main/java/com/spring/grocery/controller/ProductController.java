package com.spring.grocery.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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
	public MappingJacksonValue getProductList(HttpServletRequest request) {		
		List<?> products = repository.findAll().stream().map(prod -> prod.getProduct()).collect(Collectors.toList());	
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("product","price");
		FilterProvider filters = new SimpleFilterProvider().addFilter("productFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(products);
		mapping.setFilters(filters);
		return mapping;
	}
}
