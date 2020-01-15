package com.spring.grocery.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/*
 * Don't really need a handler 
 */
@Slf4j
@Component
public class SuccessfulHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.debug("In onAuthenticationSucccess with authentication: " + authentication.toString());

		boolean hasAdminRole = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		log.debug("authorities for user: " + authorities.size());

		for (GrantedAuthority grantedAuthority : authorities) {
			log.debug("granted Authority is: " + grantedAuthority.getAuthority());
			log.debug("GA is: " + grantedAuthority.toString());

			if (grantedAuthority.getAuthority().equals("ADMIN")) {
				hasAdminRole = true;
				break;
			}
		}
		
		String path = "/";		
		if (hasAdminRole) {
			path = "/userprofile"; // Should have been a different web page
		}		
		redirectStrategy.sendRedirect(request, response, path);
	}
}
