package com.spring.grocery.entity;


import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	private Calendar orderDate;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="customer_id")
	private Customer customer;

	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval=true)
	@JoinTable(name="orders_order_line",
		joinColumns = { @JoinColumn(name="orderId")},
		inverseJoinColumns = {@JoinColumn(name="orderLineId")})
	private Set<OrderLine> orderLine = null;

	public Orders() {

	}
	
	public Orders(Calendar date, Customer cus) {
		this.orderDate = date;
		this.customer = cus;
		this.orderLine = new HashSet<OrderLine>(1);
	}	
}
