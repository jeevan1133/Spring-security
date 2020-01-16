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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.grocery.service.SecretService;
import com.spring.grocery.service.UserDetailsServiceImp;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CsrfTokenRepository jwtCsrfTokenRepository;

	@Autowired
	private SecretService secretService;
	
	@Autowired
	private SuccessfulHandler successfulHandler;

	private final String[] ignoreStaticResources = { "/images/**", "favicon.ico", 
													"/css/**" ,"/swagger-ui**", 
													"/webjars/**",
													"/swagger-resources/**",
													"/static/**"};
	private final String[] ignoreMatchers = { "/", "/index" , "/registration" , "/v2/**", "/swagger**"};	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {    	
		log.debug("In: " + this.toString() + ":: " + "configure");	
		http
			.csrf().disable();
		http	
			.authorizeRequests()
			.antMatchers(ignoreMatchers).permitAll()			
			//.antMatchers("/product-list").hasRole("ADMIN")			
			.anyRequest().authenticated()			
			.and()
			.addFilterAfter(new JwtCsrfValidatorFilter(), CsrfFilter.class)
			.csrf()
			.csrfTokenRepository(jwtCsrfTokenRepository)			
			.and()
			.formLogin(formlogin ->
					formlogin
					.permitAll()
					.loginPage("/login")									
					.loginProcessingUrl("/process-login")
					.successHandler(successfulHandler)
					.failureUrl("/login?error=true")
//	                .defaultSuccessUrl("/")
					)	
			.logout(logout ->
				logout
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/")	
				.permitAll()
			)
			.sessionManagement()    
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.sessionAuthenticationStrategy(new SessionFixationProtectionStrategy())
			.maximumSessions(2)
			.maxSessionsPreventsLogin(true)
			;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
        web
        	.debug(true)
        	.ignoring()
            .antMatchers(ignoreStaticResources);
            //.antMatchers(ignoreMatchers);
    }
	

	private class JwtCsrfValidatorFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(HttpServletRequest request, 
				HttpServletResponse response, 
				FilterChain filterChain) throws ServletException, IOException
		{
			// NOTE: A real implementation should have a nonce cache so the token cannot be reused
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
					Jwts.parser()
						.setSigningKeyResolver(secretService.getSigningKeyResolver())
						.parseClaimsJws(token.getToken());					

				} catch (JwtException e) {
					// most likely an ExpiredJwtException, but this will handle any
					log.debug("Expired token");
					request.setAttribute("exception", e);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					RequestDispatcher dispatcher = request.getRequestDispatcher("error");
					dispatcher.forward(request, response);
				}
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

	@Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("Configuring global auth manager builder");
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	
	@Override
	public String toString() {
		return "WebSecurityConfig [jwtCsrfTokenRepository=" + jwtCsrfTokenRepository + ", secretService="
				+ secretService + "]";
	}

}