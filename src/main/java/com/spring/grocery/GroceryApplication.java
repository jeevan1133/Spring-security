package com.spring.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class GroceryApplication {
	
	@Bean(name="messageSource")
	public ResourceBundleMessageSource bundleMessageSource() {		
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(GroceryApplication.class, args);
	}
}