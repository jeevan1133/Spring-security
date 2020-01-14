package com.spring.grocery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spring.grocery.model.CustomUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImp userDetailsService;
	
	@Override
	public void autoLogin(String username, String password) {
		// TODO Auto-generated method stub
		log.debug("tyring autologin");

		CustomUser userDetails = (CustomUser)userDetailsService.loadUserByUsername(username);		

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
        								password, 
						        		userDetails.getAuthorities());            
        
        authenticationManager.authenticate(authToken);
        
        if (authToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.debug(String.format("Auto login %s successfully!", username));
        }
	}

}
