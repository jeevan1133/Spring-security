package com.spring.grocery.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerId;
	private String name;
	private Calendar customerSince;
	
	private String firstName;
	private String lastName;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public Customer(String nameString, Calendar cusDate) {
		name = nameString;
		customerSince = cusDate;
		String[] namesList = nameString.split(" ");
		if (namesList.length >= 1) {
			firstName = namesList[0];
			lastName = namesList[1];
		}
	}
	
	public Customer(String first, String last, Calendar cusDate) {
		this.firstName = first;
		this.lastName = last;
		this.customerSince = cusDate;
		this.name = first + " " + last;
	}
	
}
