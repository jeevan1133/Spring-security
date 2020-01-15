package com.spring.grocery.controller.advice;

import java.security.SignatureException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spring.grocery.exception.EmployeeNotFoundException;
import com.spring.grocery.model.JwtResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler {
	
	@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ SignatureException.class, MalformedJwtException.class, JwtException.class })
    public JwtResponse exception(Exception e) {
        JwtResponse response = new JwtResponse();
        response.setStatus(JwtResponse.Status.ERROR);
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass()
            .getName());

        return response;
    }
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({UsernameNotFoundException.class, EmployeeNotFoundException.class})
	public JwtResponse throwException(Exception e) {
		JwtResponse response = new JwtResponse();
        response.setStatus(JwtResponse.Status.ERROR);
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass()
            .getName());
        
        return response;
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status,
			WebRequest request) 
	{
		JwtResponse response = new JwtResponse();
		response.setMessage(ex.getBindingResult().toString());
		response.setStatus(JwtResponse.Status.ERROR);
		return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
	}
	
}

