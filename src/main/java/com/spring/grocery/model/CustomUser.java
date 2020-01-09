package com.spring.grocery.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.spring.grocery.entity.Customer;

public class CustomUser extends User {
	
    private static final long serialVersionUID = -3531439484732724601L;


	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Customer cus) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
		this.customer = cus;
	}
	
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	@Override
	public String toString() {
		return "CustomUser [customer=" + customer + "]";
	}
}
