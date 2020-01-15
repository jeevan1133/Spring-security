package com.spring.grocery;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@SpringBootApplication
@EnableWebSecurity
@EnableAsync
public class GroceryApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GroceryApplication.class, args);
	}

	@Bean	
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.US);
		return sessionLocaleResolver;
	}	
	
	/*
	 * This can be defined in application properties	 
	 */
	
//	@Bean(name="messageSource")
//	public ResourceBundleMessageSource bundleMessageSource() {		
//	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//	    messageSource.setBasename("message");
//	    return messageSource;
//	}	
}