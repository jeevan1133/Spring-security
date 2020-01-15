package com.spring.grocery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Configuration
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

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "message.firstName", "First name is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "message.lastName", "Last name is required.");
		
		if (user.getUserName().length() < 4) {
			errors.rejectValue("userName", "Size.userForm.userName");
		}
		log.debug("Searching username in the database: " + userRepo.findByUserName(user.getUserName()));
		if (userRepo.findByUserName(user.getUserName()) != null) {
			errors.rejectValue("userName", "message.userName", "Username already taken");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message.password", "NotEmpty", "Password can't be empty");
		log.debug("User's password's length is: " + user.getPassword().length());

		if (user.getPassword().length() > 8 || user.getPassword().length() < 32) {
			//Check if commonly used password
		} else {
			errors.rejectValue("password", "message.password", "Please use between 8 and 32 characters");
		}

		if (!user.getMatchingPassword().equals(user.getPassword())) {
			errors.rejectValue("matchingPassword", "message.matchingPassword", "Passwords don't match");
		}
	}
}
