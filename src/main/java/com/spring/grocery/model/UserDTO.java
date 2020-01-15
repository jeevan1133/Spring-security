package com.spring.grocery.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDTO {
	
	@NotNull
    @Size(min = 2, max = 30, message = "Name must have at least 2 characters")
	private String firstName;
	@NotNull
    @Size(min = 2, max = 30, message = "Name must have at least 2 characters")
	private String lastName;
	
	@NotNull
    @Size(min = 4, max = 30, message = "User name must be at least 4 characters long")
	private String userName;
	
	private String email;
	
	@NotNull
    @Min(5)
	private String password;
	
	//@Transient
	@Min(5)
	private String matchingPassword;
		
	public UserDTO() {
		
	}
	
	public UserDTO(String firstName, String lastName, String userName, String email, String password,
			String matchingPassword) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	/*
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}	
	*/
}
