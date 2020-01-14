package com.spring.grocery.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class Users {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String userName;
    
   
    private String firstName;    
    private String lastName;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @Transient
    private String matchingPassword;
    
    private Role role = Role.USER;
   
    @NotNull
    @NotEmpty
    private String email;
    
    @OneToOne(cascade = CascadeType.MERGE)
    private Customer customer;     
    
    public Users() {
    	
    }
    
    public Users(Customer cus, String user, String pass, Role role, String email) {
    	this.userName = user;
    	this.password = pass;
    	this.role = role;
    	this.customer = cus;
    	this.email = email;
    }
}