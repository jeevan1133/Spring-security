package com.spring.grocery.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.grocery.entity.Users;

@Component
public class PasswordEncrypter  {
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@Autowired
	public PasswordEncrypter( BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }	

	public Users encryptPassword(Users user) {
		// TODO Auto-generated method stub
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return user;
	}
	
}
