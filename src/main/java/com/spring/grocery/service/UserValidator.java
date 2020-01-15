package com.spring.grocery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserValidator implements Validator {

	@Autowired
	private UserRepository userRepo;

	@Override
	public boolean supports(Class<?> aClass) {
		return UserDTO.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		UserDTO user = (UserDTO) target;
		log.debug("IN VALIDATION: " + user);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "message.firstName", "First name is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "message.lastName", "Last name is required.");
		
		if (user.getUserName().length() < 4) {
			errors.rejectValue("userName", "message.size.userName", "Username must be greater than 4 characters");
		}
		log.debug("Searching username in the database: " + userRepo.findByUserName(user.getUserName()));
		if (userRepo.findByUserName(user.getUserName()) != null) {
			errors.rejectValue("userName", "message.userName.taken", "Username already taken");
		}
		log.debug("Checking email");
		
		if (userRepo.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "message.email", "Email already exists");
		}

		log.debug("checking passwords");

		if (!user.getMatchingPassword().equals(user.getPassword())) {
			log.debug("passwords don't match");			
			errors.rejectValue("matchingPassword", "message.matchingPassword", "Passwords don't match");
		} else {
			log.debug("Passwords match");

		}
	}
}
