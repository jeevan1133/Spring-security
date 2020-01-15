package com.spring.grocery.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class Users implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2478190570506172285L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String userName;    
    
    @NotNull
    @NotEmpty
    private String password;  
    
    private Role role = Role.USER;
   
    @NotNull
    @NotEmpty
    private String email;
    
//    @Transient
//    private String matchingPassword;
//    
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