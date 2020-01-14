package com.spring.grocery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.spring.grocery.dao.UserRepository;
import com.spring.grocery.entity.Users;

@Configuration
public class UserValidator implements Validator {

	@Autowired
	private UserRepository userRepo;

	@Override
	public boolean supports(Class<?> aClass) {
		return Users.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Users user = (Users) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "message.firstName", "Firstname is required.");
		
		if (user.getUserName().length() < 4) {
			errors.rejectValue("userName", "Size.userForm.userName");
		}
		
		if (userRepo.findByUserName(user.getUserName()) != null) {
			errors.rejectValue("userName", "Duplicate.userForm.userName");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}

		if (!user.getMatchingPassword().equals(user.getPassword())) {
			errors.rejectValue("matchingPassword", "Diff.userForm.matchingPassword");
		}
	}
}
