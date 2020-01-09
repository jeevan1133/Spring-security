package com.spring.grocery.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.grocery.dao.CustomerRepository;
import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Customer;
import com.spring.grocery.entity.Role;
import com.spring.grocery.entity.Users;
import com.spring.grocery.model.CustomUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private CustomerRepository cusRepo;

	//private Customer customer;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Username is: " + username);
		/*
		Users user = repo.findByUsername(username);
		UserBuilder builder = null;
		builder = org.springframework.security.core.userdetails.User.withUsername(username);
		builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
		String[] roles = new String[1];
		roles[0] = user.getRole() == Role.ADMIN ? "ADMIN" : "USER";			
		builder.roles(roles);
		return builder.build();
		*/
		
		log.debug("in UserDetailsImpl with username: " + username);
		CustomUser  customUser = null;
		Set<GrantedAuthority> authorities = new HashSet<>();
		Users user = repo.findByUsername(username);
		Customer customer = cusRepo
				.findById(
						user.getCustomer()
						.getCustomerId())
				.orElseThrow(() -> new UsernameNotFoundException("username not found"));
		String role = user.getRole() == Role.ADMIN ? "ADMIN" : "USER";
		authorities.add(new SimpleGrantedAuthority(role));
		customUser = new CustomUser(username, 
							new BCryptPasswordEncoder().encode(user.getPassword()), 
							true, true, true, true, authorities,
							customer);
		log.debug("CustomUser is:: " + customUser);

		return customUser;
	}

}
