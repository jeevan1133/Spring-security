package com.spring.grocery.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spring.grocery.config.SuccessfulHandler;
import com.spring.grocery.model.CustomUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SuccessfulHandler succesfulHandler;
	

	@Autowired
	private UserDetailsServiceImp userDetailsService;
	
	@Override
	public void autoLogin(HttpServletRequest request, HttpServletResponse response, String username, String password) {

		CustomUser userDetails = (CustomUser)userDetailsService.loadUserByUsername(username);		

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
        								password, 
						        		userDetails.getAuthorities());            
        
        authenticationManager.authenticate(authToken);
        
        if (authToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.debug(String.format("Auto login %s successfully!", username));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try {
				succesfulHandler.onAuthenticationSuccess(request, response, authentication);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
        }        
	}

}
