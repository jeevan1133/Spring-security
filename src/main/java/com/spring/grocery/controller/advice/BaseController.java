package com.spring.grocery.controller.advice;

import java.security.SignatureException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.spring.grocery.model.JwtResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class BaseController {
	
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
	@ExceptionHandler(UsernameNotFoundException.class)
	public JwtResponse throwException(Exception e) {
		JwtResponse response = new JwtResponse();
        response.setStatus(JwtResponse.Status.ERROR);
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass()
            .getName());

        return response;
	}
}

