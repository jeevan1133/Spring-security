package com.spring.grocery.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Role;
import com.spring.grocery.entity.Users;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Username is: " + username);
		Users users = repo.findByUsername(username);

		UserBuilder builder = null;
		log.debug("user is: " + users);
		if (users != null) {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(new BCryptPasswordEncoder().encode(users.getPassword()));
			String[] roles = new String[1];
			roles[0] = users.getRole() == Role.ADMIN ? "ADMIN" : "USER";			
			builder.roles(roles);			
		} else {
			log.debug("user not found in the database");
			throw new UsernameNotFoundException("User not found.");
		}
		return builder.build();
	}

}
