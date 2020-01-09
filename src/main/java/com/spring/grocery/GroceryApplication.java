package com.spring.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@SpringBootApplication(scanBasePackageClasses = {LoadDatabase.class, CSRFConfig.class, 
//		 WebSecurityConfig.class, SecretService.class, HomeController.class, CustomerController.class, 
//		 BaseController.class}) // MyAccessDeniedHandler.class})
@EnableWebMvc 
@SpringBootApplication
@EnableWebSecurity
public class GroceryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryApplication.class, args);
	}
}
