package com.spring.grocery.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.grocery.config.WebSecurityConfig;
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
	
	@Autowired
	private WebSecurityConfig config;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		
		log.debug("in UserDetailsImpl with username: " + username);
		CustomUser  customUser = null;
		Set<GrantedAuthority> authorities = new HashSet<>();
		Users user = repo.findByUserName(username);
		log.debug("Users is: " + user);

		Customer customer = cusRepo
				.findById(
						user.getCustomer()
						.getCustomerId())
				.orElseThrow(() -> new UsernameNotFoundException("username not found"));
		log.debug("customer is: " + customer);
		String role = user.getRole() == Role.ADMIN ? "ADMIN" : "USER";
		log.debug("User role is: " + role);

		authorities.add(new SimpleGrantedAuthority(role));		

		PasswordEncoder encoder = config.passwordEncoder();
		String passcodeString = encoder.encode(user.getPassword());
		log.debug("passcodeString: " + passcodeString + " Real password: " + user.getPassword());


		customUser = new CustomUser(username, passcodeString, true, true, true, true, authorities, customer);
		log.debug("CustomUser is:: " + customUser);
		
		return customUser;
	}

}
