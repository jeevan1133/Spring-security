package com.spring.grocery.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.grocery.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

}
