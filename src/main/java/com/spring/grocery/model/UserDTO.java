package com.spring.grocery.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDTO {
	
	@NotNull
    @Size(min = 2, max = 30)
	private String firstName;
	@NotNull
    @Size(min = 2, max = 30)
	private String lastName;
	
	@NotNull
    @Size(min = 4, max = 30, message = "Name should have atleast 4 characters and maximum of 30 characters")
	private String userName;
	
    @Email(message="Invalid Email provided")
	private String email;
		
    @Size(min=5, message="Password must be at least 5 characters long")
	private String password;
	
	//@Transient	
	private String matchingPassword;	
		
	public UserDTO() {
		
	}
	
	//private String message;
	
	public UserDTO(String firstName, String lastName, String userName, String email, String password,
			String matchingPassword) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}
}
