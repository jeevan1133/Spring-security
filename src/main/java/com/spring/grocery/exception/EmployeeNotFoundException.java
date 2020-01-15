package com.spring.grocery.exception;

public class EmployeeNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4901503510144701546L;

	public EmployeeNotFoundException(String message) {
		super(message);		
	}
}