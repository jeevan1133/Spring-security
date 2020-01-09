package com.spring.grocery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class Users {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Role role;
    private String email;
    
    @OneToOne
    private Customer customer;
    
    
    public Users() {
    	
    }
    
    public Users(Customer cus, String user, String pass, Role role, String email) {
    	this.username = user;
    	this.password = pass;
    	this.role = role;
    	this.customer = cus;
    	this.email = email;
    }
}