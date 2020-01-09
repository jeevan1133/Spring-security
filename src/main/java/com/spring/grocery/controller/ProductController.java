package com.spring.grocery.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@GetMapping(value="/product-list")
	public String getProductList(HttpServletRequest request) {
		
		return "index";
	}
}
