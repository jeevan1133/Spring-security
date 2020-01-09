package com.spring.grocery.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	private String product;
	private double price;
	private boolean inStock;

	@OneToMany(cascade = { CascadeType.ALL })
	private List<Comment> comments;
	
	public Product() {
	}

	public Product(String prod, double pri, boolean stocked) {
		this.product = prod;
		this.price = pri;
		this.inStock = stocked;
		this.comments = new ArrayList<Comment>(1);
	}
}
