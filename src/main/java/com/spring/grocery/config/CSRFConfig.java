package com.spring.grocery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import com.spring.grocery.service.SecretService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CSRFConfig {

    @Autowired
    private SecretService secretService;    
    
    @Bean
    public CsrfTokenRepository jwtCsrfTokenRepository() {    	
    	log.debug("In: " + this.toString() + "::jwtCsrfTokenRepository");
        return new JWTCsrfTokenRepository(secretService.getSecretBytes());
    }

	@Override
	public String toString() {
		return "CSRFConfig [secretService=" + secretService + "]";
	}
}
