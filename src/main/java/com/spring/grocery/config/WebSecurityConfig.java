package com.spring.grocery.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.grocery.service.SecretService;
import com.spring.grocery.service.UserDetailsServiceImp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CsrfTokenRepository jwtCsrfTokenRepository;

	@Autowired
	private SecretService secretService;

	//private String[] ignoreMathcers = {"/images/**", "/favicon.io", "/", "/index", "/css/**"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {    	
		log.debug("In: " + this.toString() + ":: " + "configure");
		http
		.addFilterAfter(new JwtCsrfValidatorFilter(), CsrfFilter.class)
		.authorizeRequests()
		.antMatchers("/images/**", "/favicon.io", "/","/resources/**", "/index", "/css/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.csrf()
		.csrfTokenRepository(jwtCsrfTokenRepository)		
		.and()		
		.formLogin(formLogin -> formLogin
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/userprofile")
				)
		.logout().invalidateHttpSession(true)
		.permitAll();		
	}

	private class JwtCsrfValidatorFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
			// NOTE: A real implementation should have a nonce cache so the token cannot be reused
			log.debug("In private class: JwtCsrfValidatorFilter: doFilterInternal");        	
			CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
			log.debug("CsrfToken header: " + token.getHeaderName() +
					" CsrfToken parameterName:" + token.getParameterName() + 
					" CsrfToken token:" + token.getToken() +
					", filterChain: " + filterChain.toString());
			log.debug("Request servlet path: " + request.getServletPath());
			if (
					// only care if it's a POST	            
					"POST".equals(request.getMethod()) &&	           
					// make sure we have a token
					token != null
					) {
				// CsrfFilter already made sure the token matched. Here, we'll make sure it's not expired
				try {
					log.debug("parsing token");
					JwtParser dummpyParser = Jwts.parser()
							.setSigningKeyResolver(secretService.getSigningKeyResolver());
					log.debug("got secret service key: token is: " + token.getToken());
					Jws<Claims> to = dummpyParser.parseClaimsJws(token.getToken());
					log.debug("JSON<Claims> Signature: " + to.getSignature() + 
							" Header:" + to.getHeader() + 
							" Body:" + to.getBody() +
							" :"  + to.toString()                    
							);

				} catch (JwtException e) {
					// most likely an ExpiredJwtException, but this will handle any
					log.debug("Expired token");
					request.setAttribute("exception", e);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					RequestDispatcher dispatcher = request.getRequestDispatcher("expired-jwt");
					dispatcher.forward(request, response);
				}
			} else {
				log.debug("NOT a POST method or token is null");
			}

			filterChain.doFilter(request, response);
		}
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImp();
	};

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}	

	@Override
	public String toString() {
		return "WebSecurityConfig [jwtCsrfTokenRepository=" + jwtCsrfTokenRepository + ", secretService="
				+ secretService + "]";
	}

}
