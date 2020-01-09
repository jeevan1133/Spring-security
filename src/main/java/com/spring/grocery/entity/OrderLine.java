package com.spring.grocery.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="orderLine")
public class OrderLine {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long orderLineId;	

	@OneToOne(cascade = CascadeType.MERGE)	
	private Product product;
	private double amount;
	private double purchasePrice;

	public OrderLine() {

	}

	public OrderLine(Product p, double amt, double purPrice) {
		this.product = p;
		this.amount = amt;
		this.purchasePrice = purPrice;		
	}
}
